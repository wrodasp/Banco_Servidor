package ec.edu.ups.vista;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ec.edu.ups.modelos.RegistroSesion;
import ec.edu.ups.modelos.Usuario;

@ManagedBean
@ViewScoped
public class ResumenSesion {
	
	private Usuario usuario;
	
	@PostConstruct
	public void init() {
		try {
			usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		} catch (Exception e) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public String estadoSesion(RegistroSesion registro) {
		return registro.isExitoso()? "EXITOSO": "FALLIDO";
	}
}