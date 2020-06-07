package ec.edu.ups.vista;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.modelos.RegistroSesion;
import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.negocio.ProcesoGestionLocalON;
import ec.edu.ups.negocio.ProcesoSesionLocalON;

@ManagedBean
@ViewScoped
public class ResumenSesion 
{
	@Inject
	private ProcesoSesionLocalON procesoSesion;
	
	@Inject
	private ProcesoGestionLocalON procesoGestion;
	
	private List<RegistroSesion> listaSesion;
	
	private Cuenta cuenta;
	private RegistroSesion sesion;
	
	@PostConstruct
	public void init()
	{
		Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		try {
			cuenta = procesoGestion.listarCuentas()
		               .stream()
		               .filter(c -> c.getPropietario().getCedula().equals(usuario.getPropietario().getCedula()))
		               .findFirst().get();	
			listaSesion = procesoGestion.listarSesiones();

			
		} catch (Exception e) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	

	public List<RegistroSesion> getListaSesion() {
		return listaSesion;
	}

	public void setListaSesion(List<RegistroSesion> listaSesion) {
		this.listaSesion = listaSesion;
	}
	
	
	
	
	
	

}
