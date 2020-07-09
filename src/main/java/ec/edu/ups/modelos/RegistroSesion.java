package ec.edu.ups.modelos;

import java.io.Serializable;
import java.time.LocalDate;

import javax.enterprise.inject.Typed;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Esta clase permite guardar datos 
 * referentes a un registro de Sesión.
 */
@Entity
@Table(name = "registrosSesion")
public class RegistroSesion implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private int id;
	
	@Column(nullable = false)
	private boolean exitoso;
	
	@Column(nullable = false)
	private LocalDate fecha;
	
	/**
	 * Crea una nueva instancia de la clase RegistroSesion.
	 */
	public RegistroSesion() {
		fecha = LocalDate.now();
	}

	/**
	 * Duevuelve el valor del ID.
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
	 * Devuelve un valor booleano indicando si el inicion de sesión fue exitoso.
	 */
	public boolean isExitoso() {
		return exitoso;
	}

	/**
	 * Establece si el inicio de sesión fue exitoso.
	 */
	public void setExitoso(boolean exitoso) {
		this.exitoso = exitoso;
	}

	/**
	 * Devuelve el valor de la fecha del registro de sesión.
	 */
	public LocalDate getFecha() {
		return fecha;
	}

	/**
	 * Establece el valor de la fecha del registro de sesión.
	 */
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
}