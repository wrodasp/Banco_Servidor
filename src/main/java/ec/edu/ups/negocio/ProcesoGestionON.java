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
import ec.edu.ups.modelos.RegistroSesion;
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
public class ProcesoGestionON implements ProcesoGestionRemotoON, ProcesoGestionLocalON, Serializable {

	@Inject
	private CuentaDAO cuentaDAO;
	
	@Inject
	private UsuarioDAO usuarioDAO;
	
	public ProcesoGestionON() {
	}
	
	@Override
	public void registrarUsuario(Persona cliente, Usuario usuario) throws Exception {
		try {
			if(usuarioDAO.buscar(usuario.getCorreo()) == null) {
				usuario.setPropietario(cliente);
				usuario.setClave(GeneradorClave.getNuevaClave(8));
				usuarioDAO.agregar(usuario);
				String mensaje = "Hola, bienvenido a MashiBank, desde ahora podrás usar tu " +
			             "usuario: " + usuario.getCorreo() + ", " + 
					     "con la clave: " + usuario.getClave() + " para ingresar " +
			             "a tu cuenta de usuario en el sitio web www.mashibank.com.\n\n" +
					     "Agredecemos tu afiliación a nosotros.";
				//UtilidadCorreo.enviarCorreo(usuario.getCorreo(), "MashiBank - Creación de cuenta de usuario", mensaje);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public Usuario buscarUsuario(String correo) throws Exception {
		try {
			return usuarioDAO.buscar(correo);
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
	public List<Credito> listarCreditosCuenta(int cue_id) throws Exception {
		try {
			Cuenta cuenta = cuentaDAO.buscar(cue_id);
			return cuenta.getListaCreditos();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	@Override
	public List<Transaccion> listarMovimientos(Cuenta cuenta, LocalDate fechaInicio, LocalDate fechaFin) {
		return cuenta.getListaTransacciones().stream().filter(aux -> 
			aux.getFecha().isAfter(fechaInicio.minusDays(1)) &&
			aux.getFecha().isBefore(fechaFin.plusDays(1))
		).collect(Collectors.toList());
	}
	
	@Override
	public boolean validarCedula(String cedula) {
        if (cedula.length() == 10) {
            int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
            if (tercerDigito < 6) {
                int[] coefValCedula = {2, 1, 2, 1, 2, 1, 2, 1, 2};
                int verificador = Integer.parseInt(cedula.substring(9, 10));
                int suma = 0;
                int digito = 0;
                for (int i = 0; i < (cedula.length() - 1); i++) {
                    digito = Integer.parseInt(cedula.substring(i, i + 1)) * coefValCedula[i];
                    suma += ((digito % 10) + (digito / 10));
                }
                if ((suma % 10 == 0) && (suma % 10 == verificador)) {
                    return true;
                } else if ((10 - (suma % 10)) == verificador) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
	

	@Override
	public List<RegistroSesion> listarSesiones() {
		List<RegistroSesion> listaSesiones = new ArrayList<>();
		listarUsuarios().forEach(usuario ->{
			usuario.getListaRegistroSesiones().forEach(registroSesion ->
			listaSesiones.add(registroSesion));
		});
		return listaSesiones;
	}
}