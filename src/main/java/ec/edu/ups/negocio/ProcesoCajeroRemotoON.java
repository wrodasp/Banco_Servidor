package ec.edu.ups.negocio;

import javax.ejb.Remote;

import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.modelos.Persona;

/**
 * Esta interface define métodos útiles 
 * para el proceso de cajero de un usuario.
 **/
@Remote
public interface ProcesoCajeroRemotoON {
	
	/**
	 * Abre la cuenta bancaria para el propietario especificado.
	 **/
	public void abrirCuenta(Persona propietario, double montoInicial) throws Exception;
	
	/**
	 * Busca la cuenta asociada al numero especificado.
	 **/
	public Cuenta buscarCuenta(int numeroCuenta) throws Exception;
	
	/**
	 * Realiza el depósito del monto especificado en la cuenta.
	 **/
	public void depositar(Cuenta cuenta, double monto) throws Exception;
	
	/**
	 * Realiza el retiro del monto especificado de la cuenta.
	 **/
	public void retirar(Cuenta cuenta, double monto) throws Exception;
	
	/**
	 * Método para buscar una cuenta según la cédula (APP MOVIL).
	 * @param cedula
	 * @return
	 */
	public int buscarCuentaCedula(String cedula);
	
	/**
	 * Realiza la transferencia del monto especifica desde la cuenta origen a la cuenta destino.
	 **/
	public void transferir(Cuenta cuentaOrigen, Cuenta cuentaDestino, double monto) throws Exception;
}