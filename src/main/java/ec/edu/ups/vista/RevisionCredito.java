package ec.edu.ups.vista;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.modelos.SolicitudCredito;
import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.modelos.enums.EstadoSolicitud;
import ec.edu.ups.modelos.enums.TipoUsuario;
import ec.edu.ups.negocio.ProcesoCreditoLocalON;

@ManagedBean
@ViewScoped
public class RevisionCredito {

	@Inject
	private ProcesoCreditoLocalON procesoCredito;
	
	private Cuenta cuenta;
	private SolicitudCredito solicitud;
	private EstadoSolicitud estado;
	
	public RevisionCredito() {
	}
	
	@PostConstruct
	public void init() {
		Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		cuenta = (Cuenta) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cuentaOrigen");
		solicitud = (SolicitudCredito) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("solicitudRevision");
		try {
			if(usuario.getRol() != TipoUsuario.JEFE_DE_CREDITO) {
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true");
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
	
	public EstadoSolicitud[] getEstadoSolicitud() {
		return EstadoSolicitud.values();
	}
	
	public void setEstado(EstadoSolicitud estado) {
		this.estado = estado;
	}
	
	public void cambiarEstadoSolicitud() {
		try {
			solicitud.setEstado(estado);
			procesoCredito.cambiarEstadoSolicitud(cuenta, solicitud);
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_WARN, "El estado de la solicitud ha sido actualizado.", "")
			);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha podido cambiar el estado de la solicitud.", "")
			);
		}
	}
}