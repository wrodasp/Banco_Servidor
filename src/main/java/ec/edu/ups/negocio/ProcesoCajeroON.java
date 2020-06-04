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
 * Esta clase funciona como fachada para 
 * realizar las operaciones de un 
 * proceso de cajero.
 */
@Stateless
public class ProcesoCajeroON implements ProcesoCajeroRemotoON, Serializable {

	@Inject 
	private CuentaDAO cuentaDAO;
	
	@Inject
	private UsuarioDAO usuarioDAO;
	
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
			cuentaDAO.agregar(cuenta);
		} catch (Exception e) {
			throw new Exception("No se ha podido abrir la cuenta.");
		}
		
	}

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
			throw new Exception("No se ha podido realizar la transacción de deposito.");
		}
	}

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
			throw new Exception("No se ha podido realizar la transacción de retiro.");
		}
	}

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
			cuentaDestino.getListaTransacciones().add(transaccionDeposito);
			cuentaDAO.modificar(cuentaOrigen);
			cuentaDAO.modificar(cuentaDestino);
		} catch (Exception e) {
			throw new Exception("No se ha podido realizar la transacción de transferencia.");
		}	
	}	
}