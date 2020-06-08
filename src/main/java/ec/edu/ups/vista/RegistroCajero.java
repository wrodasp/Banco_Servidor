package ec.edu.ups.vista;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.modelos.Transaccion;
import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.modelos.enums.TipoUsuario;
import ec.edu.ups.negocio.ProcesoCajeroLocalON;
import ec.edu.ups.negocio.ProcesoGestionLocalON;


@ManagedBean
@ViewScoped
public class RegistroCajero 
{
	@Inject
	private ProcesoCajeroLocalON procesoCajero;
	
	private Cuenta cuenta;
	
	
	private Transaccion transaccion;
	
	@PostConstruct
	public void init()
	{
		cuenta = new Cuenta();
		transaccion = new Transaccion();
		Usuario userSesion = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		try {
			if(userSesion.getRol()!=TipoUsuario.CAJERO) {
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true");
			}
		} catch (Exception e) {
			
		}
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public Transaccion getTransaccion() {
		return transaccion;
	}

	public void setTransaccion(Transaccion transaccion) {
		this.transaccion = transaccion;
	}
	
	public String obtenerCuenta()
	{
		cuenta= procesoCajero.buscarCuenta(cuenta.getId());
		System.out.println(cuenta);
		return null;
	}
	
	
	public String realizaDeposito()
	{
		try {
			procesoCajero.depositar(cuenta, transaccion.getMonto());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String realizaRetiro()
	{
		try {
			procesoCajero.retirar(cuenta, transaccion.getMonto());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}