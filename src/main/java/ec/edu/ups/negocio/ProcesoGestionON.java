package ec.edu.ups.negocio;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import ec.edu.ups.datos.CuentaDAO;
import ec.edu.ups.datos.UsuarioDAO;
import ec.edu.ups.modelos.Credito;
import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.modelos.Persona;
import ec.edu.ups.modelos.SolicitudCredito;
import ec.edu.ups.modelos.Transaccion;
import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.utilidades.GeneradorClave;
import ec.edu.ups.utilidades.UtilidadCorreo;

/**
 * Esta clase funciona como fachada para 
 * realizar las operaciones de un 
 * proceso de gestión.
 */
@Stateless
public class ProcesoGestionON implements ProcesoGestionRemotoON, Serializable {

	@Inject
	private CuentaDAO cuentaDAO;
	
	@Inject
	private UsuarioDAO usuarioDAO;
	
	public ProcesoGestionON() {
	}
	
	@Override
	public void registrarUsuario(Persona cliente, Usuario usuario) throws Exception {
		try {
			usuario.setPropietario(cliente);
			usuario.setClave(GeneradorClave.getNuevaClave(8));
			usuarioDAO.agregar(usuario);
			String mensaje = "Hola, bienvenido a MashiBank, desde ahora podrás usar tu " +
			                 "usuario: " + usuario.getCorreo() + ", " + 
					         "con la clave: " + usuario.getClave() + " para ingresar " +
			                 "a tu cuenta en el sitio web www.mashibank.com.\n\n" +
					         "Agredecemos tu afiliación a nosotros.";
			UtilidadCorreo.enviarCorreo(usuario.getCorreo(), "MashiBank - Creación de cuenta", mensaje);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public List<Usuario> listarUsuarios() {
		return usuarioDAO.listar();
	}

	@Override
	public List<Cuenta> listarCuentas() {
		return cuentaDAO.listar();
	}

	@Override
	public List<SolicitudCredito> listarSolicitudesCredito() {
		List<SolicitudCredito> listaSolicitudCreditos = new ArrayList<>();
		listarCuentas().forEach(cuenta -> {
			cuenta.getListaSolicitudes().forEach(solicitud -> 
				listaSolicitudCreditos.add(solicitud)
			);
		});
		return listaSolicitudCreditos;
	}

	@Override
	public List<Credito> listarCreditos() {
		List<Credito> listaCreditos = new ArrayList<>();
		listarCuentas().forEach(cuenta -> {
			cuenta.getListaCreditos().forEach(credito -> 
				listaCreditos.add(credito)
			);
		});
		return listaCreditos;
	}
	
	@Override
	public List<Transaccion> listarMovimientos(Cuenta cuenta, LocalDate fechaInicio, LocalDate fechaFin) {
		return cuenta.getListaTransacciones().stream().filter(aux -> 
			aux.getFecha().isAfter(fechaInicio.minusDays(1)) &&
			aux.getFecha().isBefore(fechaFin.plusDays(1))
		).collect(Collectors.toList());
	}
}
