package ec.edu.ups.vista;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import ec.edu.ups.modelos.Persona;
import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.modelos.enums.TipoUsuario;
import ec.edu.ups.negocio.ProcesoGestionLocalON;

@ManagedBean
@RequestScoped
public class UsuarioBean {
	@Inject
	private ProcesoGestionLocalON procesoGestion;
	
	private Persona persona;
	
	private Usuario usuario;
	
	private double monto;
	
	@PostConstruct
	public void init() {
		monto = 20;
		persona = new Persona();
		usuario = new Usuario();
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public ProcesoGestionLocalON getProcesoGestion() {
		return procesoGestion;
	}

	public void setProcesoGestion(ProcesoGestionLocalON procesoGestion) {
		this.procesoGestion = procesoGestion;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public UsuarioBean() {
	}
	
	public String registrarUsuario() {
		usuario.setRol(TipoUsuario.CLIENTE);
		FacesContext contexto = FacesContext.getCurrentInstance();
		try {
			if (!(persona.getCedula() == null)) {
				if(procesoGestion.validarCedula(persona.getCedula())) {
					procesoGestion.registrarUsuario(persona, usuario);
					contexto.addMessage(null, 
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario registrado exitosamente!", "")
					);
				} else {
					contexto.addMessage(null, 
						new FacesMessage(FacesMessage.SEVERITY_INFO, "La cedula no es valida!", "")
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
	
	public void limpiar() {
		persona = new Persona();
		usuario = new Usuario();
		monto = 20;
	}
}