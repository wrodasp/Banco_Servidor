package ec.edu.ups.servicios;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ec.edu.ups.modelos.Credito;
import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.negocio.ProcesoCajeroLocalON;
import ec.edu.ups.negocio.ProcesoGestionLocalON;
import ec.edu.ups.negocio.ProcesoSesionLocalON;

/**
 * 
 * @author BenavidesJuan, CalvaByron, RodasWilson
 *
 * Esta clase permite definir un servicio web
 * bancario de tipo REST
 */
@Path("/cliente")
public class ServiciosREST{

	@Inject
	private ProcesoSesionLocalON sesion;
		
	@Inject
	private ProcesoCajeroLocalON cajero;
	
	@Inject
	private ProcesoGestionLocalON cliente;
	
	/**
	 * Servicio que permite obetener los creditos de un usuario
	 * @param cue_id parametro por numero de cuenta
	 * @return lista de creditos
	 * @throws Exception en caso de que no se ejecute  la transaccion correctamente
	 */
	@GET
	@Path("/getCreditos")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Credito> getCreditos(@QueryParam("cue_id") int cue_id) throws Exception {
		try {
			return cajero.buscarCuenta(cue_id).getListaCreditos();
		} catch (Exception e) {
			throw new Exception("Se ah producido un error"+e.getMessage());
		}
	}
		
	/**
	 * 
	 */
	/**
	 * Devuelve un valor booleano que indica si el inicio de sesion fue exitoso.
	 * @param user parametro usuario
	 * @param clave parametro contraseña
	 * @return mensaje de exito o falla
	 * @throws Exception en caso de que el inicio de sesion sea fallido
	 */
	@GET
	@Path("/loguear")
	@Produces(MediaType.TEXT_PLAIN)
	public String iniciarSesion(@QueryParam("user") String user,
			                     @QueryParam("clave") String clave) throws Exception {
		try {
			Usuario usuario = sesion.validarCredenciales(user, clave);			
			return user != null ? "exitoso"+ cajero.buscarCuentaCedula(usuario.getPropietario().getCedula()) : "fallido";
		} catch (Exception e) {
			return "fallido";
		}
	}
	
	/**
	 * Devuelve un valor booleano que indica si la transferencia de 
	 * de la cuenta de origen a la cuenta de destino fue exitosa.
	 */
	/***
	 * Devuelve un valor booleano que indica si la transferencia de 
	 * de la cuenta de origen a la cuenta de destino fue exitosa.
	 * @param origen parametro cuenta que va a transferir
	 * @param destino cuenta a la que sera transferido
	 * @param monto parametro cantidad
	 * @return string en caso de que sea exitoso o fallido
	 * @throws Exception
	 */
	@GET
	@Path(value = "/transferir")
	@Produces(value = "application/json")
	public String transferir(@QueryParam(value = "origen") int origen, 
			                  @QueryParam(value = "destino") int destino, 
			                  @QueryParam(value = "monto") double monto) throws Exception {
		try {
			cajero.transferir(cajero.buscarCuenta(origen), cajero.buscarCuenta(destino), monto);
			return "exitoso";
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 
	 */
	/**
	 * Devuelve un valor booleano que indica si el deposito 
	 * a la cuenta de destino fue exitosa.
	 * @param numeroCuenta numero de cuenta del usuario
	 * @param monto cantidad de la transaccion
	 * @return booleano true cuando fue exitoso
	 * @throws Exception
	 */
	@GET
	@Path(value = "/depositar")
	@Produces(value = "application/json")
	public boolean depositar(@QueryParam(value = "numeroCuenta") int numeroCuenta,
							 @QueryParam(value = "monto") double monto) throws Exception {
		try {
			cajero.depositar(cajero.buscarCuenta(numeroCuenta), monto);
			return true;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * 
	 */
	/**
	 * Devuelve un valor booleano que indica si el retiro
	 * de la cuenta destino fue exitosa.
	 * @param numeroCuenta parametro numero de cuenta usuario
	 * @param monto cantidad a retirar en la transaccion
	 * @return booleano true si es que se realizo correctamente
	 * @throws Exception
	 */
	@GET
	@Path(value = "/retirar")
	@Produces(value = "application/json")
	public boolean retirar(@QueryParam(value = "numeroCuenta") int numeroCuenta, 
						   @QueryParam(value = "monto") double monto) throws Exception {
		try {
			cajero.retirar(cajero.buscarCuenta(numeroCuenta), monto);
			return true;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	
	/**
	 * Logica que permite hacer el cambio de contraseña a un usuario
	 * @param correo parameetro correo electronico del usuario
	 * @return String en caso de que fue exitoso o fallido el cambio de clave
	 * @throws Exception
	 */
	@GET
	@Path("/cambiarClave")
	@Produces(MediaType.TEXT_PLAIN)
	public String cambiarClave(@QueryParam("correo") String correo) throws Exception {
		try {
			sesion.cambiarClave(cliente.buscarUsuario(correo));
			return "Clave cambiada";
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Parametro que permite obtener el saldo de una cuenta
	 * @param cuenta objeto de tipo cuenta
	 * @return String con el salgo de la cuenta
	 * @throws Exception en caso de que exista un error
	 */
	@GET
	@Path("/getSaldo")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSaldo(@QueryParam("cuenta") int cuenta) throws Exception {
		try {
			return String.valueOf(cajero.buscarCuenta(cuenta).getSaldo());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Logica que devuelve una lista de tipo Usuario
	 * @return lista de tipo usuario
	 * @throws Exception en caso de que exista error
	 */
	@GET
	@Path("/getUsuarios")
	@Produces(MediaType.TEXT_PLAIN)
	public List<Usuario> getUsuarios() throws Exception {
		try {
			return cliente.listarUsuarios();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}
