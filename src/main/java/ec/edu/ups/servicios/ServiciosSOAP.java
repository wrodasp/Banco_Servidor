package ec.edu.ups.servicios;

import java.util.List;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;

import ec.edu.ups.modelos.Credito;
import ec.edu.ups.negocio.ProcesoCajeroLocalON;
import ec.edu.ups.negocio.ProcesoSesionLocalON;

/**
 * Esta clase permite define un servicio
 * web para el sistema bancario.
 */
@WebService
public class ServiciosSOAP {
	
	@Inject
	private ProcesoSesionLocalON sesion;
	
	@Inject
	private ProcesoCajeroLocalON cajero;
	
	/**
	 * Devuelve un valor booleano que indica si el inicio de sesion fue exitoso.
	 */
	@WebMethod
	public boolean iniciarSesion(String correo, String clave) throws Exception {
		try {
			return sesion.validarCredenciales(correo, clave) != null;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Devuelve un valor booleano que indica si la transferencia de 
	 * de la cuenta de origen a la cuenta de destino fue exitosa.
	 */
	@WebMethod
	public boolean transferir(int numeroCuentaOrigen, int numeroCuentaDestino, double monto) throws Exception {
		try {
			cajero.transferir(cajero.buscarCuenta(numeroCuentaOrigen), cajero.buscarCuenta(numeroCuentaDestino), monto);
			return true;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Devuelve un valor booleano que indica si el deposito 
	 * a la cuenta de destino fue exitosa.
	 */
	@WebMethod
	public boolean depositar(int numeroCuentaOrigen, double monto) throws Exception {
		try {
			cajero.depositar(cajero.buscarCuenta(numeroCuentaOrigen), monto);
			return true;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	
	
	/**
	 * Devuelve un valor booleano que indica si el retiro
	 * de la cuenta destino fue exitosa.
	 */
	@WebMethod
	public boolean retirar(int numeroCuentaOrigen, double monto) throws Exception {
		try {
			cajero.retirar(cajero.buscarCuenta(numeroCuentaOrigen), monto);
			return true;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}
