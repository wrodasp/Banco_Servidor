package ec.edu.ups.vista;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import ec.edu.ups.modelos.Credito;
import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.negocio.ProcesoCreditoLocalON;
import ec.edu.ups.negocio.ProcesoGestionLocalON;
import ec.edu.ups.negocio.ProcesoGestionON;


@ManagedBean
@ViewScoped
public class ResumenCreditos 
{
	@Inject
	private ProcesoCreditoLocalON procesoCredito;
	
	@Inject
	private ProcesoGestionLocalON procesoGestion;
	
	private Cuenta cuenta;
	
	private List<Credito> listCreditos;

	@PostConstruct
	public void init()
	{
		Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		try {
			cuenta = procesoGestion.listarCuentas()
		               .stream()
		               .filter(c -> c.getPropietario().getCedula().equals(usuario.getPropietario().getCedula()))
		               .findFirst().get();	
			listCreditos = procesoGestion.listarCreditos();
			System.out.println("lista**: "+ listCreditos.size());
			
		} catch (Exception e) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public DateTimeFormatter getFormatoFecha() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy");
	}
	
	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public List<Credito> getListCreditos() {
		return listCreditos;
	}

	public void setListCreditos(List<Credito> listCreditos) {
		this.listCreditos = listCreditos;
	}

	

	
	

}
