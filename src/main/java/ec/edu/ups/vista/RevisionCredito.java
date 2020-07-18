package ec.edu.ups.vista;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ec.edu.ups.modelos.Credito;
import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.modelos.Persona;
import ec.edu.ups.modelos.SolicitudCredito;
import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.modelos.enums.EstadoSolicitud;
import ec.edu.ups.modelos.enums.TipoCredito;
import ec.edu.ups.modelos.enums.TipoUsuario;
import ec.edu.ups.negocio.ProcesoCreditoLocalON;
import ec.edu.ups.negocio.ProcesoGestionLocalON;

@ManagedBean
@ViewScoped
public class RevisionCredito {

	@Inject
	private ProcesoCreditoLocalON procesoCredito;
	
	@Inject
	private ProcesoGestionLocalON procesoGestion;
	
	private Cuenta cuenta;
	private SolicitudCredito solicitud;
	private EstadoSolicitud estado;
	private TipoCredito tipoCredito;
	private StreamedContent[] archivosAdjuntos;
	private LocalDate fechaVencimiento;
	private String observaciones;
	
	public RevisionCredito() {
	}
	
	@PostConstruct
	public void init() {
		Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		cuenta = (Cuenta) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cuentaOrigen");
		solicitud = (SolicitudCredito) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("solicitudRevision");
		archivosAdjuntos = new StreamedContent[solicitud.getArchivosAdjuntos().size()];
		try {
			if(usuario.getRol() != TipoUsuario.JEFE_DE_CREDITO) {
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true");
			}
			for (int i = 0; i < solicitud.getArchivosAdjuntos().size(); i++) {
				File aux = solicitud.getArchivosAdjuntos().get(i);
				InputStream stream = new FileInputStream(aux);
				archivosAdjuntos[i] = DefaultStreamedContent.builder()
															.name(aux.getName())
															.stream(() -> stream)
															.build();
			}
		} catch (Exception e) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public Cuenta getCuenta() {
		return cuenta;
	}
	
	public SolicitudCredito getSolicitud() {
		return solicitud;
	}
	
	public EstadoSolicitud[] getEstadosSolicitud() {
		return new EstadoSolicitud[] {
			EstadoSolicitud.APROBADA,
			EstadoSolicitud.RECHAZADA
		};
	}
	
	public TipoCredito[] getTiposCredito() {
		return TipoCredito.values();
	}
	
	public EstadoSolicitud getEstado() {
		return estado;
	}
	
	public void setEstado(EstadoSolicitud estado) {
		this.estado = estado;
	}
	
	public StreamedContent[] getArchivosAdjuntos() {
		return archivosAdjuntos;
	}
	
	public void setArchivosAdjuntos(StreamedContent[] archivosAdjuntos) {
		this.archivosAdjuntos = archivosAdjuntos;
	}
	
	public TipoCredito getTipoCredito() {
		return tipoCredito;
	}
	
	public void setTipoCredito(TipoCredito tipoCredito) {
		this.tipoCredito = tipoCredito;
	}
	
	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}
	
	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
		
	}
	
	public String getObservaciones() {
		return observaciones;
	}
	
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public String cambiarEstadoSolicitud() {
		try {
			solicitud.setEstado(estado);
			if (estado == EstadoSolicitud.APROBADA) {
				Credito credito = new Credito();
				credito.setMonto(solicitud.getMontoSolicitado());
				credito.setTipo(tipoCredito);
				credito.setFechaVencimiento(fechaVencimiento);
				procesoCredito.registrarCredito(cuenta, credito);
			}
			procesoCredito.cambiarEstadoSolicitud(cuenta, solicitud);
			procesoCredito.notificarSobreSolicitud(buscarPropietario(), solicitud, observaciones);
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_WARN, "El estado de la solicitud ha sido actualizado.", "")
			);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha podido cambiar el estado de la solicitud.", "")
			);
		}
		return null;
	}
	
	private Usuario buscarPropietario() {
		Persona persona = cuenta.getPropietario();
		return procesoGestion.listarUsuarios()
				             .stream()
					         .filter(aux -> aux.getPropietario().getCedula().equals(persona.getCedula()))
						     .findFirst().get();
	}
}