package ec.edu.ups.vista;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.modelos.Transaccion;
import ec.edu.ups.modelos.enums.TipoTransaccion;
import ec.edu.ups.negocio.ProcesoCajeroLocalON;

@ManagedBean
@ViewScoped
public class ResumenCuenta {

	@Inject
	private ProcesoCajeroLocalON procesoCajero;
	
	private Cuenta cuenta;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private TipoTransaccion tipoMovimiento;
	private List<Transaccion> movimientosRealizados;
	
	public ResumenCuenta() {
	}
	
	@PostConstruct
	public void init() {
		String id_propietario = (String) FacesContext.getCurrentInstance()
				          							 .getExternalContext()
				          							 .getSessionMap()
				          							 .get("id_propietario");
		movimientosRealizados = cuenta.getListaTransacciones();
		fechaFin = LocalDate.now();
		fechaInicio = getFechaFin().minusDays(30);
		tipoMovimiento = TipoTransaccion.DEPOSITO;
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
		return "Sin transacciones";
	}
	
	public String cerrarSesion() {
		return "login.xhtml?faces-redirect=true";
	}
	
	public List<Transaccion> getMovimientosRealizados() {
		return movimientosRealizados;
	}
	
	public DateTimeFormatter getFormatoFecha() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy");
	}
	
	public LocalDate getFechaInicio() {
		return fechaInicio;
	}
	
	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	
	public LocalDate getFechaFin() {
		return fechaFin;
	}
	
	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public void setTipoMovimiento(TipoTransaccion tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}
	
	public TipoTransaccion getTipoMovimiento() {
		return tipoMovimiento;
	}
	
	public TipoTransaccion[] getTiposMovimiento() {
		return TipoTransaccion.values();
	}
	
	public void filtrar() {
		movimientosRealizados = cuenta.getListaTransacciones()
				 					  .stream()
				 					  .filter(t -> t.getFecha().isAfter(fechaInicio))
				 					  .filter(t -> t.getFecha().isBefore(fechaFin))
				 					  .filter(t -> t.getTipo() == tipoMovimiento)
				 					  .collect(Collectors.toList());
	}
}