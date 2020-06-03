package ec.edu.ups.negocio;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import ec.edu.ups.datos.CuentaDAO;
import ec.edu.ups.datos.UsuarioDAO;
import ec.edu.ups.modelos.Credito;
import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.modelos.Cuota;
import ec.edu.ups.modelos.Notificacion;
import ec.edu.ups.modelos.Persona;
import ec.edu.ups.modelos.SolicitudCredito;
import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.modelos.enums.EstadoSolicitud;
import ec.edu.ups.modelos.enums.TipoCredito;

/**
 * Esta clase funciona como fachada para 
 * realizar las operaciones de un 
 * proceso de credito.
 */
public class ProcesoCreditoON implements ProcesoCreditoRemotoON, Serializable {

	@Inject
	private CuentaDAO cuentaDAO;
	
	@Inject 
	private UsuarioDAO usuarioDAO;
	
	/**
	 * Crea una nueva instanacia de la clase ProcesoCreditoON.
	 */
	public ProcesoCreditoON() {
	}
	
	@Override
	public void solicitarCredito(Cuenta cuenta, SolicitudCredito solicitud) throws Exception {
		try {
			Persona propietario = cuenta.getPropietario();
			solicitud.setTexto(
				"Yo, " + propietario.getNombre() + " " + propietario.getApellido() + ", " +
			    "portador de la cedula de identidad Nro " + propietario.getCedula() + ", " +
				"solicito de la manera más comedida se me conceda el " +
				"credito de " + solicitud.getMontoSolicitado()		
			);
			cuenta.getListaSolicitudes().add(solicitud);
			cuentaDAO.modificar(cuenta);
		} catch (Exception e) {
			throw new Exception("No se ha podido registrar la solicitud.");
		}
		
	}

	@Override
	public void cambiarEstadoSolicitud(Cuenta cuenta, SolicitudCredito solicitud) throws Exception {
		try {
			List<SolicitudCredito> listaActualizada = cuenta.getListaSolicitudes().stream().map(
				aux -> aux.getId() == solicitud.getId()? solicitud : aux 
			).collect(Collectors.toList());
			cuenta.setListaSolicitudes(listaActualizada);
			cuentaDAO.modificar(cuenta);
		} catch (Exception e) {
			throw new Exception("No se ha podido cambiar el estado de la solicitud.");
		}
		
	}

	@Override
	public void notificarSobreSolicitud(Usuario usuario, SolicitudCredito solicitud) throws Exception {
		try {
			Notificacion notificacion = new Notificacion();
			notificacion.setMensaje(
				"Banco Mashi le informa mediante este medio que " + 
				"su solicitud de credito enviada el " + solicitud.getFecha().toString() + ", " +
				"se encuentra en estado: " + solicitud.getEstado() + ".\n\n" +
				"Solicitud enviada: \n\n" + solicitud.getTexto() + "\n\n"
			);
			usuario.getListaNotificaciones().add(notificacion);
			usuarioDAO.modificar(usuario);
		} catch (Exception e) {
			throw new Exception("No se ha podido notificar sobre la solicitud.");
		}
		
	}

	@Override
	public void registrarCredito(Cuenta cuenta, Credito credito) throws Exception {
		try {
			credito.setListaCuotas(generarAmortizacion(credito));
			cuenta.getListaCreditos().add(credito);
			cuentaDAO.modificar(cuenta);
		} catch (Exception e) {
			throw new Exception("No se ha podido registrar el credito.");
		}
	}

	@Override
	public List<Cuota> generarAmortizacion(Credito credito) throws Exception {
		try {
			List<Cuota> listaCuotas = new ArrayList<Cuota>();
			int numeroMeses = (int) ChronoUnit.MONTHS.between(LocalDate.now(), credito.getFechaVencimiento());
			double montoAPagarEnCuotas = credito.getMonto() / numeroMeses;
			LocalDate siguienteFecha = LocalDate.now().plusMonths(1);
			for(int i = 0; i < numeroMeses; i++) {
				Cuota cuota = new Cuota();
				cuota.setMonto(montoAPagarEnCuotas);
				cuota.setFechaVencimiento(siguienteFecha);
				siguienteFecha = siguienteFecha.plusMonths(1);
				listaCuotas.add(cuota);
			}
			return listaCuotas;
		} catch (Exception e) {
			throw new Exception("No se ha podido generar la amortizacion.");
		}
	}
	
	@Override
	public void pagarCuota(Cuenta cuenta, Credito credito, Cuota cuota, double monto) throws Exception {
		try {
			cuota.abonar(monto);
			List<Cuota> listaCuotasActualizada = credito.getListaCuotas().stream().map(
				aux -> aux.getId() == cuota.getId()? cuota : aux
			).collect(Collectors.toList());
			credito.setListaCuotas(listaCuotasActualizada);
			List<Credito> listaCreditosActualizada = cuenta.getListaCreditos().stream().map(
				aux -> aux.getId() == credito.getId()? credito : aux
			).collect(Collectors.toList());
			cuenta.setListaCreditos(listaCreditosActualizada);
			cuentaDAO.modificar(cuenta);
		} catch (Exception e) {
			throw new Exception("No se ha podido realizar el pago de la cuota.");
		}
	}
}
