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
import ec.edu.ups.negocio.ProcesoCajeroLocalON;
import ec.edu.ups.negocio.ProcesoGestionLocalON;

@ManagedBean
@ViewScoped
public class RegistrarCliente {
	
	@Inject
	private ProcesoGestionLocalON procesoGestion;
	
	@Inject
	private ProcesoCajeroLocalON procesoCajero;
	
	private Persona persona;
	private Usuario usuario;
	private double monto;
	
	public RegistrarCliente() {
	}
	
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
	
	public String registrarCliente() {
		usuario.setRol(TipoUsuario.CLIENTE);
		FacesContext contexto = FacesContext.getCurrentInstance();
		try {
			if (!(persona.getCedula() == null)) {
				if(procesoGestion.validarCedula(persona.getCedula())) {
					procesoGestion.registrarUsuario(persona, usuario);
					procesoCajero.abrirCuenta(persona, monto);;
					contexto.addMessage(null, 
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente registrado exitosamente.", "")
					);
				} else {
					contexto.addMessage(null, 
						new FacesMessage(FacesMessage.SEVERITY_INFO, "La cedula no es valida.", "")
					);
				}
			}
		} catch (Exception e) {
			contexto.addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se ha podido registrar el cliente.", e.getMessage())
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