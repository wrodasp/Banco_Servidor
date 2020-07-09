package ec.edu.ups.servicios;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;

import ec.edu.ups.negocio.ProcesoCajeroLocalON;
import ec.edu.ups.negocio.ProcesoSesionLocalON;

@ApplicationPath(value = "rest")
@Path(value = "/servicios")
public class ServiciosREST extends Application {

	@Inject
	private ProcesoSesionLocalON sesion;
		
	@Inject
	private ProcesoCajeroLocalON cajero;
		
	/**
	 * Devuelve un valor booleano que indica si el inicio de sesion fue exitoso.
	 */
	@GET
	@Path(value = "/iniciarSesion")
	@Produces(value = "application/json")
	public boolean iniciarSesion(@QueryParam(value = "correo") String correo,
			                     @QueryParam(value = "clave") String clave) throws Exception {
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
