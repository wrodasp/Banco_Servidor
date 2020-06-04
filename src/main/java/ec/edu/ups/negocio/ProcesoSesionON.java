package ec.edu.ups.negocio;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import ec.edu.ups.datos.UsuarioDAO;
import ec.edu.ups.modelos.Notificacion;
import ec.edu.ups.modelos.RegistroSesion;
import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.utilidades.GeneradorClave;
import ec.edu.ups.utilidades.UtilidadCorreo;

/**
 * Esta clase funciona como fachada para 
 * realizar las operaciones de un 
 * proceso de sesión.
 */
@Stateless
public class ProcesoSesionON implements ProcesoSesionRemotaON, ProcesoSesionLocalON, Serializable {

	@Inject
	private UsuarioDAO usuarioDAO;
	
	/**
	 * Crea una nueva instancia de la clase ProcesoSesion.
	 **/
	public ProcesoSesionON() {
	}
	
	@Override
	public Usuario buscarUsuario(String correo) throws Exception {
		try {
			return usuarioDAO.buscar(correo);
		} catch (Exception e) {
			throw new Exception("No se ha encontrado el usuario.");
		}
	}
	
	@Override
	public boolean validarCredenciales(String correo, String clave) throws Exception {
		try {
			Usuario usuario = buscarUsuario(correo);
			boolean estadoSesion = usuario.getClave().equals(clave);
			String mensajeNotificado = notificarIntentoSesion(usuario, estadoSesion);
			registrarIntentoSesion(usuario, estadoSesion);
			String extra = estadoSesion? "": "Has sido tú?. Si no, entonces genera una\n" +
			                                 "nueva clave desde de cuenta online. Si no\n" +
					                         "tienes accesso entonces acercate a una sucursal\n" +
			                                 "de MashiBank y pide que generen una nueva clave\n" +
					                         "para ti.";
			UtilidadCorreo.enviarCorreo(correo, "Intento de inicion de sesión", 
				mensajeNotificado + "\n\n" + extra
			);
			return estadoSesion;
		} catch (Exception e) {
			throw new Exception("No existe el usuario especificado.");
		}
	}
	
	@Override
	public void cambiarClave(Usuario usuario) throws Exception {
		try {
			usuario.setClave(GeneradorClave.getNuevaClave(8));
			usuarioDAO.modificar(usuario);
		} catch(Exception e) {
			throw new Exception("No se ha podido generar la clave.");
		}
	}
	
	@Override
	public String notificarIntentoSesion(Usuario usuario, boolean exitoso) throws Exception {
		try {
			Notificacion notificacion = new Notificacion();
			notificacion.setMensaje(
					"Has intentado iniciar sesión el " + notificacion.getFecha().toString() + ".\n\n" +
					"Estado: " + (exitoso? "EXITOSO": "FALLIDO")
			);
			usuario.getListaNotificaciones().add(notificacion);
			usuarioDAO.modificar(usuario);
			return notificacion.getMensaje();
		} catch (Exception e) {
			throw new Exception("No se ha podido notificar del intento de sesión al usuario.");
		}
	}
	
	@Override
	public void registrarIntentoSesion(Usuario usuario, boolean exitoso) throws Exception {
		try {
			RegistroSesion registroSesion = new RegistroSesion();
			registroSesion.setExitoso(exitoso);
			usuario.getListaRegistroSesiones().add(registroSesion);
			usuarioDAO.modificar(usuario);
		} catch (Exception e) {
			throw new Exception("No se ha podido registrar el intento de sesión del usuario.");
		}
	}
}
