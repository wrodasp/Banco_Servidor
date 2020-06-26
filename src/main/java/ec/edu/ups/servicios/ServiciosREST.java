package ec.edu.ups.servicios;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
	@GET
	//@Path("loguear/{credenciales}/{otroparam}")
	@Path("loguear/{credenciales}")
	@Produces(MediaType.TEXT_PLAIN)
	public String iniciarSesion(@PathParam("credenciales") String credenciales){
		try {
			sesion.validarCredenciales(credenciales.split(":")[0], credenciales.split(":")[1]).getClass();
			return "correcto";
		} catch (Exception e) {
			return "El usuario o contraseña son incorrectos: ("+e.getMessage()+").";
		}
	}
}
