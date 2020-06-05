package ec.edu.ups.vista;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.negocio.ProcesoGestionLocalON;

@ManagedBean
@ViewScoped
public class ResumenCuenta {

	@Inject
	private ProcesoGestionLocalON procesoGestion;
	
	private Cuenta cuenta;
	
	public ResumenCuenta() {
	}
	
	@PostConstruct
	public void init() {
		String id_propietario = (String) FacesContext.getCurrentInstance()
				          							 .getExternalContext()
				          							 .getSessionMap()
				          							 .get("id_propietario");
		cuenta = procesoGestion.listarCuentas().stream()
				 							   .filter(aux -> aux.getPropietario().getCedula().equals(id_propietario))
				 							   .findFirst().get();
	}
	
	public Cuenta getCuenta() {
		return cuenta;
	}
	
	public String determinarUltimaTransaccion() {
		if (cuenta.getListaTransacciones().size() > 0) {
			return cuenta.getListaTransacciones()
					 	 .stream()
             		 	 .sorted((t1,t2) -> t1.getFecha().compareTo(t2.getFecha()))
             		 	 .findFirst().get().getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		}
		return LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
}