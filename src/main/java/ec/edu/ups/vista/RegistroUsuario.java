package ec.edu.ups.vista;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import ec.edu.ups.modelos.Persona;
import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.modelos.enums.TipoUsuario;
import ec.edu.ups.negocio.ProcesoGestionRemotoON;

@ManagedBean
@RequestScoped
public class RegistroUsuario {

	@Inject
	private ProcesoGestionRemotoON procesoGestion;
	
	public RegistroUsuario() {
	}
	
	public String registrarUsuario(Persona persona, Usuario usuario) {
		FacesContext contexto = FacesContext.getCurrentInstance();
		try {
			if (!(persona.getCedula() == null)) {
				if(persona.validarCedula()) {
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
}