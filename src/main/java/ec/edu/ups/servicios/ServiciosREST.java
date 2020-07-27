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

@Path("/cliente")
public class ServiciosREST{

	@Inject
	private ProcesoSesionLocalON sesion;
		
	@Inject
	private ProcesoCajeroLocalON cajero;
	
	@Inject
	private ProcesoGestionLocalON cliente;
	
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
	 * Devuelve un valor booleano que indica si el inicio de sesion fue exitoso.
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
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Devuelve un valor booleano que indica si el deposito 
	 * a la cuenta de destino fue exitosa.
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
	 * Devuelve un valor booleano que indica si el retiro
	 * de la cuenta destino fue exitosa.
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
}
