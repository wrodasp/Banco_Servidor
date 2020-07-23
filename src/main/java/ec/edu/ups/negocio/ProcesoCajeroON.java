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
public class ProcesoCajeroON implements ProcesoCajeroRemotoON, ProcesoCajeroLocalON, Serializable {

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
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public Cuenta buscarCuenta(int numeroCuenta) throws Exception {
		try {
			return cuentaDAO.buscar(numeroCuenta);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	@Override
	public int buscarCuentaCedula(String cedula) {
		return cuentaDAO.buscarCedula(cedula);
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
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * Lógica para débitos
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
			throw new Exception(e.getMessage());
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