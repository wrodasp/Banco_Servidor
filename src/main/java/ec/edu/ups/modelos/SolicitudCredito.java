package ec.edu.ups.modelos;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import ec.edu.ups.modelos.enums.EstadoSolicitud;

/**
 * Esta clase permite guardar datos 
 * referentes a una solicitud de Crédito.
 */
@Entity
@Table(name = "solicitudesCredito")
public class SolicitudCredito implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private int id;
	
	@Column(nullable = false, length = 5000)
	private String texto;
	
	@Column(nullable = false, precision = 2)
	private double montoSolicitado;
	
	@Column(nullable = false)
	private LocalDate fecha;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	private EstadoSolicitud estado;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "archivosAdjuntos", 
	                 joinColumns = @JoinColumn(name = "solicitud_id"))
	
	private List<File> archivosAdjuntos;
	
	/**
	 * Crea una nueva instancia de la clase SolicitudCredito.
	 */
	public SolicitudCredito() {
		fecha = LocalDate.now();
		estado = EstadoSolicitud.TRAMITANDO;
		archivosAdjuntos = new ArrayList<>();
	}

	/**
	 * Devuelve el valor del ID.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Establece el valor del ID.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Devuelve el valor del texto.
	 */
	public String getTexto() {
		return texto;
	}
	
	/**
	 * Establece el valor del texto.
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	/**
	 * Devuelve el valor del monto solicitado.
	 */
	public double getMontoSolicitado() {
		return montoSolicitado;
	}

	/**
	 * Establece el valor del monto solicitado.
	 */
	public void setMontoSolicitado(double montoSolicitado) {
		this.montoSolicitado = montoSolicitado;
	}

	/**
	 * Devuelve el valor de la fecha de solicitud.
	 */
	public LocalDate getFecha() {
		return fecha;
	}

	/**
	 * Establece el valor de la fecha de solicitud.
	 */
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	/**
	 * Devuelve el valor del estado (PENDIENTE, APROBADA, RECHAZADA).
	 */
	public EstadoSolicitud getEstado() {
		return estado;
	}

	/**
	 * Establece el valor del estado (PENDIENTE, APROBADA, RECHAZADA).
	 */
	public void setEstado(EstadoSolicitud estado) {
		this.estado = estado;
	}
	
	/**
	 * Devuelve los archivos adjuntos asociados.
	 */
	public List<File> getArchivosAdjuntos() {
		return archivosAdjuntos;
	}
	
	/**
	 * Establece los archivos adjuntos asociados.
	 */
	public void setArchivosAdjuntos(List<File> archivosAdjuntos) {
		this.archivosAdjuntos = archivosAdjuntos;
	}
}