package ec.edu.ups.vista;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.modelos.enums.TipoUsuario;
import ec.edu.ups.negocio.ProcesoSesionLocalON;

@ManagedBean
@SessionScoped
public class LoginUsuario {

	@Inject
	private ProcesoSesionLocalON procesoSesionON;
	private String correo;
	private String clave;

	public LoginUsuario() {
	}

	@PostConstruct
	public void init() {
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}
	
	public String getCorreo() {
		return correo;
	}
	
	public void setClave(String clave) {
		this.clave = clave;
	}
	
	public String getClave() {
		return clave;
	}

	public void iniciarSesion() {
		try {
			Usuario usuario = procesoSesionON.validarCredenciales(correo, clave);
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usuario);
			if (usuario.getRol() == TipoUsuario.ADMIN) {
				FacesContext.getCurrentInstance().getExternalContext().redirect("crearUsuario.xhtml?faces-redirect=true");
			} else if (usuario.getRol() == TipoUsuario.CAJERO) {
				FacesContext.getCurrentInstance().getExternalContext().redirect("crearCliente.xhtml?faces-redirect=true");
			} else if (usuario.getRol() == TipoUsuario.JEFE_DE_CREDITO) {
				FacesContext.getCurrentInstance().getExternalContext().redirect("gestionCreditos.xhtml?faces-redirect=true");
			} else {
				FacesContext.getCurrentInstance().getExternalContext().redirect("resumenCuenta.xhtml?faces-redirect=true");
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha podido iniciar sesion.", "")
			);
		}
	}

	public String cerrarSesion() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "index.xhtml?faces-redirect=true";
	}
}