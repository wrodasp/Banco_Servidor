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

import ec.edu.ups.modelos.Credito;
import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.modelos.Cuota;
import ec.edu.ups.modelos.Transaccion;
import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.modelos.enums.EstadoCuota;
import ec.edu.ups.modelos.enums.TipoTransaccion;
import ec.edu.ups.negocio.ProcesoCreditoLocalON;
import ec.edu.ups.negocio.ProcesoGestionLocalON;

@ManagedBean
@ViewScoped
public class ResumenCuenta {

	@Inject
	private ProcesoGestionLocalON procesoGestion;
	
	@Inject
	private ProcesoCreditoLocalON procesoCredito;
	
	private Cuenta cuenta;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private TipoTransaccion tipoMovimiento;
	private List<Transaccion> movimientosRealizados;
	private List<Credito> creditos;
	
	public ResumenCuenta() {
	}
	
	@PostConstruct
	public void init() {
		Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		try {
			cuenta = procesoGestion.listarCuentas()
		               .stream()
		               .filter(c -> c.getPropietario().getCedula().equals(usuario.getPropietario().getCedula()))
		               .findFirst().get();	   
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cuenta", cuenta);
			movimientosRealizados = cuenta.getListaTransacciones();
			creditos = cuenta.getListaCreditos();
			fechaFin = LocalDate.now();
			fechaInicio = getFechaFin().minusDays(30);
			tipoMovimiento = TipoTransaccion.DEPOSITO;
			debitarPagoAtrasados();
		} catch (Exception e) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public void debitarPagoAtrasados() {
		LocalDate fechaActual = LocalDate.now();
		creditos.forEach(credito -> {
			credito.getListaCuotas().forEach(cuota -> {
				if (fechaActual.isEqual(cuota.getFechaVencimiento()) || 
					fechaActual.isAfter(cuota.getFechaVencimiento())) {
					try {
						procesoCredito.debitarCuotaVencida(cuenta, credito, cuota);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		});
	}
	
	public Cuenta getCuenta() {
		return cuenta;
	}

	public String determinarUltimaTransaccion() {
		if (cuenta.getListaTransacciones().size() > 0) {
			return cuenta.getListaTransacciones()
					 	 .stream()
             		 	 .sorted((t1,t2) -> t2.getFecha().compareTo(t1.getFecha()))
             		 	 .findFirst().get().getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		}
		return "Sin transacciones";
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