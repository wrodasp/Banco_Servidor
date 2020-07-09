package ec.edu.ups.negocio;

import java.util.List;

import javax.ejb.Local;

import ec.edu.ups.modelos.Credito;
import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.modelos.Cuota;
import ec.edu.ups.modelos.SolicitudCredito;
import ec.edu.ups.modelos.Usuario;

/**
 * Esta interface define método útiles
 * para el proceso de credito de un usuario.
 **/
@Local
public interface ProcesoCreditoLocalON {

	/**
	 * Registra la solicitud del usuario
	 **/
	public void solicitarCredito(Cuenta cuenta, SolicitudCredito solicitud, String extra) throws Exception;
	
	/**
	 * Cambia el estado de la solicitud especificada a APROBADA o RECHAZADA.
	 **/
	public void cambiarEstadoSolicitud(Cuenta cuenta, SolicitudCredito solicitud) throws Exception;
	
	/**
	 * Envia una notificación al usuario sobre la solicitud especificada.
	 **/
	public void notificarSobreSolicitud(Usuario usuario, SolicitudCredito solicitud, String observaciones) throws Exception;
	
	/**
	 * Resgitra el credito en la cuenta especificada.
	 **/
	public void registrarCredito(Cuenta cuenta, Credito credito) throws Exception;
	
	/**
	 * Genera una tabla de amortización eb base al credito.
	 **/
	public List<Cuota> generarAmortizacion(Credito credito) throws Exception;
	
	/**
	 * Paga o abona la cuota con el monto especificado.
	 **/
	public void pagarCuota(Cuenta cuenta, Credito credito, Cuota cuota, double monto) throws Exception;
	
	/**
	 * Debita la cuota vencida de forma automatica si esta esta vencida.
	 **/
	public void debitarCuotaVencida(Cuenta cuenta, Credito credito, Cuota cuota) throws Exception;

	/**
	 * Muestra el total de creditos de una persona
	 */
	public List<Credito> listarCreditos(Cuenta cuenta);
}