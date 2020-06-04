package ec.edu.ups.negocio;

import java.time.LocalDate;
import java.util.List;

import javax.ejb.Local;

import ec.edu.ups.modelos.Credito;
import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.modelos.Persona;
import ec.edu.ups.modelos.SolicitudCredito;
import ec.edu.ups.modelos.Transaccion;
import ec.edu.ups.modelos.Usuario;

/**
 * Esta inteface define métodos útiles
 * para el proceso de gestión del Banco. 
 */
@Local
public interface ProcesoGestionLocalON {

	/**
	 * Registra el cliente especificado en el banco.
	 **/
	public void registrarUsuario(Persona cliente, Usuario usuario) throws Exception;
	
	/**
	 * Devuelve la lista completa de usuarios registrados.
	 **/
	public List<Usuario> listarUsuarios();

	/**
	 * Devuelve la lista completa de cuentas abiertas.
	 **/
	public List<Cuenta> listarCuentas();
	
	/**
	 * Devuelve la lista completa de solicitudes de credito registradas.
	 **/
	public List<SolicitudCredito> listarSolicitudesCredito();
	
	/**
	 * Devuelve la lista completa de creditos registrados..
	 **/
	public List<Credito> listarCreditos();
	
	/**
	 * Devuelve la lista de movimientos realizada dentro del rango de fechas especificado.
	 **/
	public List<Transaccion> listarMovimientos(Cuenta cuenta, LocalDate fechaInicio, LocalDate fechaFin);

	/**
	 * 
	 * @param cedula
	 * @return
	 * Método que valida la cédula de un nuevo cliente
	 */
	public boolean validarCedula(String cedula);
}