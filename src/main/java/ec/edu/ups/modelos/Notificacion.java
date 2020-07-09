package ec.edu.ups.modelos;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Esta clase permite guardar datos 
 * referentes a una Notificación.
 */
@Entity
@Table(name = "notificaciones")
public class Notificacion implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private int id;
	
	@Column(nullable = false, length = 5000)
	private String mensaje;
	
	@Column(nullable = false)
	private LocalDate fecha;
	
	/**
	 * Crea una nueva instancia de la clase Notificacion.
	 */
	public Notificacion() {
		fecha = LocalDate.now();
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
	 * Devuelve el mensaje asociado.
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * Establece el mensaje asociado.
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * Devuelve el valor de la fecha de notificación.
	 */
	public LocalDate getFecha() {
		return fecha;
	}

	/**
	 * Establece el valor de la fecha de notificación.
	 */
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
}