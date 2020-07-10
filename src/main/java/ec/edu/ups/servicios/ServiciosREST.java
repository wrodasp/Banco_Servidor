package ec.edu.ups.servicios;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ec.edu.ups.modelos.Credito;
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
			return cliente.listarCreditosCuenta(cue_id);
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
			return sesion.validarCredenciales(user, clave) != null ? "exitoso" : "fallido";
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
	public boolean transferir(@QueryParam(value = "numeroCuentaOrigen") int numeroCuentaOrigen, 
			                  @QueryParam(value = "numeroCuentaDestino") int numeroCuentaDestino, 
			                  @QueryParam(value = "monto") double monto) throws Exception {
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
}
