package ec.edu.ups.negocio;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import ec.edu.ups.datos.CuentaDAO;
import ec.edu.ups.datos.UsuarioDAO;
import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.modelos.Persona;
import ec.edu.ups.modelos.Transaccion;
import ec.edu.ups.modelos.enums.TipoTransaccion;
/**
 * 
 * @author BenavidesJuan, CalvaByron, RodasWilson
 *
 * Esta clase funciona como fachada para 
 * realizar las operaciones de un 
 * proceso de cajero.
 */
@Stateless
public class ProcesoCajeroON implements ProcesoCajeroRemotoON, ProcesoCajeroLocalON, Serializable {

	@Inject 
	private CuentaDAO cuentaDAO;

	
	/**
	 * Crea una nueva instancia de la clase ProcesoCajeroON. 
	 */
	public ProcesoCajeroON() {
	}

	@Override
	public void abrirCuenta(Persona propietario, double montoInicial) throws Exception {
		try {
			Cuenta cuenta = new Cuenta();
			cuenta.setPropietario(propietario);
			depositar(cuenta, montoInicial);
		} catch (Exception e) {
			throw new Exception("No se ha podido abrir la cuenta.\nERROR: " + e.getMessage());
		}
	}

	
	/**
	 * Metodo devuelve una cuenta por meido de un parametro numero de cuenta
	 */
	@Override
	public Cuenta buscarCuenta(int numeroCuenta) throws Exception {
		try {
			return cuentaDAO.buscar(numeroCuenta);
		} catch (Exception e) {
			throw new Exception("No se ha podido encontrar la cuenta.\nERROR: " + e.getMessage());
		}
	}
	
	
	/**
	 * Metodo que devuelve una cuenta por su numero de cuenta
	 */
	@Override
	public int buscarCuentaCedula(String cedula) {
		return cuentaDAO.buscarCedula(cedula);
	}
	
	/**
	 * Metodo que permite depositar un monto en una cuenta destino 
	 */
	@Override
	public void depositar(Cuenta cuenta, double monto) throws Exception {
		try {
			Transaccion transaccion = new Transaccion();
			transaccion.setMonto(monto);
			transaccion.setTipo(TipoTransaccion.DEPOSITO);
			cuenta.depositarDinero(monto);
			cuenta.getListaTransacciones().add(transaccion);
			cuentaDAO.modificar(cuenta);
		} catch (Exception e) {
			throw new Exception("No se ha podido depositar el dinero.\nERROR: " + e.getMessage());
		}
	}

	/**
	 * Lógica para débitos
	 * Metodo que permite retirar dinero de una cuenta
	 */
	@Override
	public void retirar(Cuenta cuenta, double monto) throws Exception {
		try {
			Transaccion transaccion = new Transaccion();
			transaccion.setMonto(monto);
			transaccion.setTipo(TipoTransaccion.RETIRO);
			cuenta.retirarDinero(monto);
			cuenta.getListaTransacciones().add(transaccion);
			cuentaDAO.modificar(cuenta);
		} catch (Exception e) {
			throw new Exception("No se ha podido retirar el dinero.\nERROR: " + e.getMessage());
		}
	}

	/**
	 * Metodo que permite transferir un monto de una cuenta a otra
	 */
	@Override
	public void transferir(Cuenta cuentaOrigen, Cuenta cuentaDestino, double monto) throws Exception {
		try {
			Transaccion transaccionRetiro = new Transaccion();
			transaccionRetiro.setMonto(monto);
			transaccionRetiro.setTipo(TipoTransaccion.RETIRO);
			Transaccion transaccionDeposito = new Transaccion();
			transaccionDeposito.setMonto(monto);
			transaccionDeposito.setTipo(TipoTransaccion.DEPOSITO);
			cuentaOrigen.getListaTransacciones().add(transaccionRetiro);
			cuentaOrigen.retirarDinero(monto);
			cuentaDestino.getListaTransacciones().add(transaccionDeposito);
			cuentaDestino.depositarDinero(monto);
			cuentaDAO.modificar(cuentaOrigen);
			cuentaDAO.modificar(cuentaDestino);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}	
	}	
}