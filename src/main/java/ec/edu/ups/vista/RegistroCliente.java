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
public class RegistroCliente {
	
	@Inject
	private ProcesoGestionLocalON procesoGestion;
	
	@Inject
	private ProcesoCajeroLocalON procesoCajero;
	
	private Persona persona;
	private Usuario usuario;
	private double monto;
	
	public RegistroCliente() {
	}
	
	@PostConstruct
	public void init() {
		monto = 20;
		persona = new Persona();
		usuario = new Usuario();
		Usuario userSesion = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		try {
			if(userSesion.getRol()!=TipoUsuario.CAJERO) {
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

	public ProcesoCajeroLocalON getProcesoCajero() {
		return procesoCajero;
	}

	public void setProcesoCajero(ProcesoCajeroLocalON procesoCajero) {
		this.procesoCajero = procesoCajero;
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
	
	public String registrar() {
		FacesContext contexto = FacesContext.getCurrentInstance();
		try {
			if (monto >= 20) {
				if (!(persona.getCedula() == null)) {
					if(procesoGestion.validarCedula(persona.getCedula())) {
						usuario.setRol(TipoUsuario.CLIENTE);
						procesoGestion.registrarUsuario(persona, usuario);
						procesoCajero.abrirCuenta(persona, monto);;
						contexto.addMessage(null, 
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente registrado exitosamente.", "")
						);
						init();
					} else {
						contexto.addMessage(null, 
							new FacesMessage(FacesMessage.SEVERITY_INFO, "La cedula no es valida.", "")
						);
					}
				}
			} else {
				contexto.addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "El monto minimo es de 20$.", "")
				);
			}
		} catch (Exception e) {
			contexto.addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se ha podido registrar el cliente.", e.getMessage())
			);
		}
		return null;
	}
}