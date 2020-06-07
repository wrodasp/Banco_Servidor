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
	private Usuario usuario;

	public LoginUsuario() {
	}

	@PostConstruct
	public void init() {
		usuario = new Usuario();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void iniciarSesion() {
		try {
			usuario = procesoSesionON.validarCredenciales(usuario.getCorreo(), usuario.getClave());
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usuario);
			if (usuario.getRol() == TipoUsuario.ADMIN) {
				FacesContext.getCurrentInstance().getExternalContext().redirect("crearUsuario.xhtml?faces-redirect=true");
			} else if (usuario.getRol() == TipoUsuario.CAJERO) {
				FacesContext.getCurrentInstance().getExternalContext().redirect("crearCliente.xhtml?faces-redirect=true");
			} else if (usuario.getRol() == TipoUsuario.JEFE_DE_CREDITO) {
				// Por implementar
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