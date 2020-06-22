package ec.edu.ups.servicios;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.sound.midi.Soundbank;

import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.negocio.ProcesoCajeroLocalON;
import ec.edu.ups.negocio.ProcesoSesionLocalON;

@WebService
public class ServiciosSOAP {
	@Inject
	private ProcesoSesionLocalON sesion;
	
	@Inject
	private ProcesoCajeroLocalON cajero;
	
	/**
	 * Servicio para validar las credenciales del usuario
	 * Made by Byron Calva
	 * @param correo
	 * @param clave
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	public boolean login(String correo, String clave) throws Exception {
		try {
			return sesion.validarCredenciales(correo, clave)!=null;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	//    Transferir dinero entre cuenta de la misma cooperativa
	@WebMethod
	public boolean transferir(int co, int cd, double monto) throws Exception {
		try {
			cajero.transferir(cajero.buscarCuenta(co), cajero.buscarCuenta(cd), monto);
			return true;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	//Realizar un deposito
	public boolean depositar(int cuentaOrigen, double monto) throws Exception {
		try {
			cajero.depositar(cajero.buscarCuenta(cuentaOrigen), monto);
			return true;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	
	
    //Realizar un retiro
	public boolean retirar(int cuentaOrigen, double monto) throws Exception {
		try {
			cajero.retirar(cajero.buscarCuenta(cuentaOrigen), monto);
			return true;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}
