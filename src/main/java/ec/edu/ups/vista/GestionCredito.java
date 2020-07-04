package ec.edu.ups.vista;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import ec.edu.ups.negocio.ProcesoGestionLocalON;

@ManagedBean
@ViewScoped
public class GestionCredito {
	
	@Inject
	private ProcesoCreditoLocalON procesoCredito;

	@Inject
	private ProcesoGestionLocalON procesoGestion;

	private List<Cuenta> cuentas;
	private List<SolicitudCredito> solicitudes;
	
	public GestionCredito() {
	}
	
	@PostConstruct
	public void init() {
		Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		try {
			if(usuario.getRol() != TipoUsuario.JEFE_DE_CREDITO) {
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true");
			}
			cuentas = procesoGestion.listarCuentas();
			solicitudes = new ArrayList<>();
			cuentas.forEach(cuenta -> {
				cuenta.getListaSolicitudes().forEach(aux -> {
					if (aux.getEstado() == EstadoSolicitud.TRAMITANDO) {
						solicitudes.add(aux);
					}
				});
			});
		} catch (Exception e) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public List<SolicitudCredito> getSolicitudes() {
		return solicitudes;
	}
	
	public void revisarSolicitud(SolicitudCredito solicitud) {
		Cuenta cuentaOrigen = cuentas.stream().filter(cuenta -> 
			cuenta.getListaSolicitudes().contains(solicitud)
		).findFirst().get();
		try {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cuentaOrigen", cuentaOrigen);
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("solicitudRevision", solicitud);
			FacesContext.getCurrentInstance().getExternalContext().redirect("revisarSolicitud.xhtml?faces-redirect=true");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede revisar la solicitud.", "")
			);
		}
	}
}
