package ec.edu.ups.servicios;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ec.edu.ups.negocio.ProcesoCajeroLocalON;
import ec.edu.ups.negocio.ProcesoSesionLocalON;

@Path("/clientes")
public class ServiciosREST {
	
	//Se debe hacer un inject por cada gestionLocal debe ser local()
	@Inject
	private ProcesoSesionLocalON sesion;
	
	@Inject
	private ProcesoCajeroLocalON cajero;
	
	/**
	 * Devuelve un valor booleano que indica si el inicio de sesion fue exitoso.
	 */
	@POST
	@Path("loguear")
	@Produces("text/plain")
	public String iniciarSesion(String credenciales){
		try {
			if(sesion.validarCredenciales(credenciales.split(":")[0], credenciales.split(":")[1])!=null) {
				return "correcto";
			}
			return "El usuario o contraseña son incorrectos";
		} catch (Exception e) {
			return e.getMessage();
		}
	}
}
