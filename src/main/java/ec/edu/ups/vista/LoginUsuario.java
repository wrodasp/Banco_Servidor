package ec.edu.ups.vista;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import ec.edu.ups.modelos.Persona;
import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.modelos.enums.TipoUsuario;
import ec.edu.ups.negocio.ProcesoGestionLocalON;
import ec.edu.ups.negocio.ProcesoSesionLocalON;

@ManagedBean
@ViewScoped
public class LoginUsuario {
	
	@Inject
	private ProcesoSesionLocalON procesoSesionON;
	private Usuario usuario;
	private String correo;
	private String clave;
	
	public LoginUsuario() {
	}
	
	@PostConstruct
	public void init() {
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public ProcesoSesionLocalON getProcesoSesionON() {
		return procesoSesionON;
	}

	public void setProcesoSesionON(ProcesoSesionLocalON procesoSesionON) {
		this.procesoSesionON = procesoSesionON;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public void loguear() {
		FacesContext contexto = FacesContext.getCurrentInstance();
		try {
			usuario = procesoSesionON.validarCredenciales(correo, clave);
			usuario.getCorreo();
			contexto.addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Autenticado con éxito.", "")
				);
		} catch (Exception e) {
			contexto.addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Fallo al loguear: "+e.getMessage(),"")
				);
		}
	}
}