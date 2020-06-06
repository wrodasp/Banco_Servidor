package ec.edu.ups.negocio;

import javax.ejb.Remote;

import ec.edu.ups.modelos.Usuario;

/**
 * Esta interface define métodos útiles 
 * para el proceso de sesión de un usuario.
 **/
@Remote
public interface ProcesoSesionRemotaON {
	
	/**
	 * Devuelve un valor booleano indicando si las credenciales son validas.<br><br>
	 **/
	public Usuario validarCredenciales(String correo, String clave) throws Exception;
	
	/**
	 * Genera una nueva clave para el usuario.
	 **/
	public void cambiarClave(Usuario usuario) throws Exception;
	
	/**
	 * Envia una notificación al usuario sobre el inicio de sesión.
	 **/
	public String notificarIntentoSesion(Usuario usuario, boolean exitoso) throws Exception;
	
	/**
	 * Registra el intento de inicio de sesión del usuario.
	 **/
	public void registrarIntentoSesion(Usuario usuario, boolean exitoso) throws Exception;
	
}