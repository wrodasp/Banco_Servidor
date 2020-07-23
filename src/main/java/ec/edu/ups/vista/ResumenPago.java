package ec.edu.ups.vista;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import ec.edu.ups.modelos.Credito;
import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.modelos.Cuota;
import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.negocio.ProcesoCreditoLocalON;

@ManagedBean
@ViewScoped
public class ResumenPago {

	@Inject
	private ProcesoCreditoLocalON procesoCredito;

	private Cuenta cuenta;
	private Credito credito;
	private Cuota cuota;
	private double monto;
	
	public ResumenPago() {
	}
	
	@PostConstruct
	public void init() {
		Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		cuenta = (Cuenta) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cuenta");
		credito = (Credito) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("credito");
		cuota = (Cuota) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cuota");
		try {
			if (usuario == null) {
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Cuota getCuota() {
		return cuota;
	}
	
	public void setCuota(Cuota cuota) {
		this.cuota = cuota;
	}
	
	public double getMonto() {
		return monto;
	}
	
	public void setMonto(double monto) {
		this.monto = monto;
	}
	
	public void guardarPago() {
		try {
			if (monto > cuota.getSaldo()) {
				procesoCredito.pagarCuota(cuenta, credito, cuota, cuota.getSaldo());
			} else {
				procesoCredito.pagarCuota(cuenta, credito, cuota, monto);
			}
			FacesContext.getCurrentInstance().getExternalContext().redirect("resumenCuotas.xhtml?faces-redirect=true");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Error al realizar pago. Intentelo de nuevo.", "")
			);
		}
	}
}
