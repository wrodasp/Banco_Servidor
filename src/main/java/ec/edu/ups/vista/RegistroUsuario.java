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

@ManagedBean
@ViewScoped
public class RegistroUsuario {
	
	@Inject
	private ProcesoGestionLocalON procesoGestion;
	
	private Persona persona;
	private Usuario usuario;
	
	public RegistroUsuario() {
	}
	
	@PostConstruct
	public void init() {
		persona = new Persona();
		usuario = new Usuario();
		Usuario userSesion = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		try {
			if(userSesion.getRol()!=TipoUsuario.ADMIN) {
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true");
			}
		} catch (Exception e) {
			
		}
	}
	
	public ProcesoGestionLocalON getProcesoGestion() {
		return procesoGestion;
	}

	public void setProcesoGestion(ProcesoGestionLocalON procesoGestion) {
		this.procesoGestion = procesoGestion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	
	public String registrar() {
		FacesContext contexto = FacesContext.getCurrentInstance();
		try {
			if (!(persona.getCedula() == null)) {
				if(procesoGestion.validarCedula(persona.getCedula())) {
					procesoGestion.registrarUsuario(persona, usuario);
					contexto.addMessage(null, 
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario registrado exitosamente.", "")
					);
					init();
				} else {
					contexto.addMessage(null, 
						new FacesMessage(FacesMessage.SEVERITY_INFO, "La cedula no es valida.", "")
					);
				}
			}
		} catch (Exception e) {
			contexto.addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se ha podido registrar el usuario.", e.getMessage())
			);
		}
		return null;
	}
	
	public TipoUsuario[] getTiposUsuario() {
		return new TipoUsuario[]{TipoUsuario.ADMIN, TipoUsuario.JEFE_DE_CREDITO, TipoUsuario.CAJERO} ;
	}
}