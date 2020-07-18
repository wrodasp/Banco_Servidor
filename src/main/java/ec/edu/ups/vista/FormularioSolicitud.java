package ec.edu.ups.vista;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFiles;

import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.modelos.SolicitudCredito;
import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.negocio.ProcesoCreditoLocalON;

@ManagedBean
@ViewScoped
public class FormularioSolicitud {

	@Inject
	private ProcesoCreditoLocalON procesoCredito;
	
	private Usuario usuario;
	private Cuenta cuenta;
	private SolicitudCredito solicitud;
	private String motivos;
	
	private UploadedFiles adjuntos;
	
	public FormularioSolicitud() {
	}
	
	@PostConstruct
	public void init() {
		solicitud = new SolicitudCredito();
		usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		cuenta = (Cuenta) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cuenta");
		try {
			if (usuario == null) {
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Cuenta getCuenta() {
		return cuenta;
	}
	
	public SolicitudCredito getSolicitud() {
		return solicitud;
	}
	
	public void setSolicitud(SolicitudCredito solicitud) {
		this.solicitud = solicitud;
	}
	
	public String getMotivos() {
		return motivos;
	}
	
	public void setMotivos(String motivos) {
		this.motivos = motivos;
	}
	
	public UploadedFiles getAdjuntos() {
		return adjuntos;
	}
	
	public void setAdjuntos(UploadedFiles adjuntos) {
		this.adjuntos = adjuntos;
	}
	
	public void enviarSolicitud() {
		if (adjuntos.getFiles().size() == 0) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Se requieren adjuntar los archivos.", "")
			);
			return;
		}
		for (int i = 0; i < adjuntos.getFiles().size(); i++) {
			try {
				//String nombreArchivo = "/home/wilson/ArchivosAdjuntos/" + cuenta.getId() + "_" + i;
				String nombreArchivo = "Archivo-" + cuenta.getId() + "_" + i;
				File archivo = new File(nombreArchivo);
				OutputStream streamSalida = new FileOutputStream(archivo);
				streamSalida.write(adjuntos.getFiles().get(i).getContent());
				streamSalida.close();
				solicitud.getArchivosAdjuntos().add(archivo);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Error al procesar archivos subidos.", "")
				);
			}
		}
		try {
			procesoCredito.solicitarCredito(cuenta, solicitud, motivos);
			procesoCredito.notificarSobreSolicitud(usuario, solicitud, "");
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Solicitud enviada.", "")
			);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "No se ha podido enviar la solicitud.", "")
			);
		}	
	}
}
