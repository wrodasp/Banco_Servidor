package ec.edu.ups.negocio;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import ec.edu.ups.datos.CuentaDAO;
import ec.edu.ups.datos.UsuarioDAO;
import ec.edu.ups.modelos.Credito;
import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.modelos.Cuota;
import ec.edu.ups.modelos.Notificacion;
import ec.edu.ups.modelos.Persona;
import ec.edu.ups.modelos.SolicitudCredito;
import ec.edu.ups.modelos.Transaccion;
import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.modelos.enums.EstadoCuota;
import ec.edu.ups.modelos.enums.EstadoSolicitud;
import ec.edu.ups.modelos.enums.TipoTransaccion;
import ec.edu.ups.utilidades.UtilidadCorreo;

/**
 * 
 * @author BenavidesJuan, CalvaByron, RodasWilson
 *
 * Esta clase funciona como fachada para 
 * realizar las operaciones de un 
 * proceso de credito.
 */
@Stateless
public class ProcesoCreditoON implements ProcesoCreditoRemotoON, ProcesoCreditoLocalON, Serializable {

	@Inject
	private CuentaDAO cuentaDAO;
	
	@Inject 
	private UsuarioDAO usuarioDAO;
	
	/**
	 * Crea una nueva instanacia de la clase ProcesoCreditoON.
	 */
	public ProcesoCreditoON() {
	}
	
	/**
	 * Logica que permite al cliente solicitar un credito al banco
	 * por el cual le permite llenar un formulario y adjuntar documentso
	 * para posterior aprobacion
	 */
	@Override
	public void solicitarCredito(Cuenta cuenta, SolicitudCredito solicitud, String extra) throws Exception {
		try {
			Persona propietario = cuenta.getPropietario();
			solicitud.setTexto(
				"Yo, " + propietario.toString() + ", " +
			    "portador de la cedula de identidad Nro " + propietario.getCedula() + ", " +
				"solicito de la manera más comedida se me conceda el " +
				"credito de " + solicitud.getMontoSolicitado() + "$.\n\n" +
				"Motivos:\n\n" + extra + "\n\n"
			);
			cuenta.getListaSolicitudes().add(solicitud);
			cuentaDAO.modificar(cuenta);
		} catch (Exception e) {
			throw new Exception("No se ha podido enviar la solicitud.\nERROR: " + e.getMessage());
		}
		
	}

	
	/**
	 * Metodo que permite cambiar el estado de una solicitud de credito
	 * 
	 */
	@Override
	public void cambiarEstadoSolicitud(Cuenta cuenta, SolicitudCredito solicitud) throws Exception {
		try {
			List<SolicitudCredito> listaActualizada = cuenta.getListaSolicitudes().stream().map(
				aux -> aux.getId() == solicitud.getId()? solicitud : aux 
			).collect(Collectors.toList());
			cuenta.setListaSolicitudes(listaActualizada);
			cuentaDAO.modificar(cuenta);
		} catch (Exception e) {
			throw new Exception("No se ha podido cambir el estado de la solicitud.\nERROR: " + e.getMessage());
		}
		
	}

	
	/**
	 * Metodo que le notifica al cliente acercade una solicitud de credito
	 */
	@Override
	public void notificarSobreSolicitud(Usuario usuario, SolicitudCredito solicitud, String observaciones) throws Exception {
		try {
			Notificacion notificacion = new Notificacion();
			if (solicitud.getEstado() == EstadoSolicitud.TRAMITANDO) {
				notificacion.setMensaje(
					"Banco Mashi le informa mediante este medio que " + 
					"su solicitud de credito enviada el " + solicitud.getFecha().toString() + ", " +
					"se encuentra en estado: " + solicitud.getEstado() + ".\n\n" +
					"Solicitud enviada: \n\n" + solicitud.getTexto() + "\n\n"
				);
			} else if (solicitud.getEstado() == EstadoSolicitud.APROBADA) {
				notificacion.setMensaje(
					"Banco Mashi le informa mediante este medio que " + 
					"su solicitud de credito enviada el " + solicitud.getFecha().toString() + ", " +
					"se ha sido " + solicitud.getEstado() + ".\n\n"
				);
			} else {
				notificacion.setMensaje(
					"Banco Mashi le informa mediante este medio que " + 
					"su solicitud de credito enviada el " + solicitud.getFecha().toString() + ", " +
					"se encuentra en estado: " + solicitud.getEstado() + ".\n\n" +
					"Observaciones: " + observaciones + "\n\n"
				);
			}
			UtilidadCorreo.enviarCorreo(usuario.getCorreo(), "Cambio de estado de solicitud", notificacion.getMensaje());
			usuario.getListaNotificaciones().add(notificacion);
			usuarioDAO.modificar(usuario);
		} catch (Exception e) {
			throw new Exception("No se ha podido enviar el correo electrónico.\nERROR: " + e.getMessage());
		}
		
	}

	/**
	 * Metodo que permite registrar un credito en la tabla de creditos
	 */
	@Override
	public void registrarCredito(Cuenta cuenta, Credito credito) throws Exception {
		try {
			credito.setSaldo(credito.getMonto());
			credito.setListaCuotas(generarAmortizacion(credito));
			cuenta.depositarDinero(credito.getMonto());
			cuenta.getListaCreditos().add(credito);
			cuentaDAO.modificar(cuenta);
		} catch (Exception e) {
			throw new Exception("No se pudo registrar el credito.\nERROR: " + e.getMessage());
		}
	}

	/**
	 * Lógica y obtención de la información
	 */
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
				cuota.setSaldo(montoAPagarEnCuotas);
				cuota.setFechaVencimiento(siguienteFecha);
				siguienteFecha = siguienteFecha.plusMonths(1);
				listaCuotas.add(cuota);
			}
			return listaCuotas;
		} catch (Exception e) {
			throw new Exception("No se ha podido generar la amortizacion.\nERROR: " + e.getMessage());
		}
	}
	
	
	/**
	 * Logica que permite pagar una cuota del credito al cliente
	 */
	@Override
	public void pagarCuota(Cuenta cuenta, Credito credito, Cuota cuota, double monto) throws Exception {
		try {
			cuota.abonar(monto);
			credito.setSaldo(credito.getSaldo() - monto);
			cuenta.retirarDinero(monto);
			Transaccion transaccion = new Transaccion();
			transaccion.setMonto(monto);
			transaccion.setTipo(cuota.getSaldo() <= 0? TipoTransaccion.PAGO_CUOTA : TipoTransaccion.ABONO_CUOTA);
			List<Cuota> listaCuotasActualizada = credito.getListaCuotas().stream().map(
				aux -> aux.getId() == cuota.getId()? cuota : aux
			).collect(Collectors.toList());
			credito.setListaCuotas(listaCuotasActualizada);
			List<Credito> listaCreditosActualizada = cuenta.getListaCreditos().stream().map(
				aux -> aux.getId() == credito.getId()? credito : aux
			).collect(Collectors.toList());
			cuenta.setListaCreditos(listaCreditosActualizada);
			cuenta.getListaTransacciones().add(transaccion);
			cuentaDAO.modificar(cuenta);
		} catch (Exception e) {
			throw new Exception(".\nERROR: " + e.getMessage());
		}
	}
	
	
	/**
	 * Metodo que permite debitar al cliente una cuota de un credito
	 */
	public void debitarCuotaVencida(Cuenta cuenta, Credito credito, Cuota cuota) throws Exception {
		try {
			if (cuenta.getSaldo() >= cuota.getSaldo()) {
				LocalDate fechaActual = LocalDate.now();
				double montoAPagar = cuota.getMonto() - cuota.getSaldo();
				cuota.abonar(montoAPagar);
				cuota.setEstado(cuota.getSaldo() <= 0? EstadoCuota.PAGADA : EstadoCuota.PENDIENTE);
				credito.setSaldo(credito.getMonto() - montoAPagar);
				cuenta.retirarDinero(cuota.getMonto());
				Transaccion transaccion = new Transaccion();
				transaccion.setMonto(montoAPagar);
				transaccion.setTipo(TipoTransaccion.DEBITO_AUTOMATICO);
				List<Cuota> listaActualizada = credito.getListaCuotas()
					                              	  .stream()
					                              	  .map(aux -> aux.getId() == cuota.getId()? cuota : aux)
					                              	  .collect(Collectors.toList());
				credito.setListaCuotas(listaActualizada);
				cuenta.getListaTransacciones().add(transaccion);
				List<Credito> listaActualizadaCredito = cuenta.getListaCreditos()
	                  	  .stream()
	                  	  .map(aux -> aux.getId() == credito.getId()? credito : aux)
	                  	  .collect(Collectors.toList());
				cuenta.setListaCreditos(listaActualizadaCredito);
				cuentaDAO.modificar(cuenta);
			}
			else {
				cuota.setEstado(EstadoCuota.VENCIDA);
				List<Cuota> listaActualizada = credito.getListaCuotas()
                    	  .stream()
                    	  .map(aux -> aux.getId() == cuota.getId()? cuota : aux)
                    	  .collect(Collectors.toList());
				credito.setListaCuotas(listaActualizada);
				List<Credito> listaActualizadaCredito = cuenta.getListaCreditos()
	                  	  .stream()
	                  	  .map(aux -> aux.getId() == credito.getId()? credito : aux)
	                  	  .collect(Collectors.toList());
				cuenta.setListaCreditos(listaActualizadaCredito);
				cuentaDAO.modificar(cuenta);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Logica que permite obtener una lista de creditos
	 */
	@Override
	public List<Credito> listarCreditos(Cuenta cuenta) {
		return cuenta.getListaCreditos();	
	}	
}