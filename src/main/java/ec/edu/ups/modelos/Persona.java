package ec.edu.ups.modelos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Esta clase permite guardar datos 
 * referentes a una Persona.
 */
@Entity
@Table(name = "personas")
public class Persona implements Serializable {
	
	@Id
	@Column(nullable = false, length = 10)
	private String cedula;
	
	@Column(nullable = false, length = 100)
	private String nombre;
	
	@Column(nullable = false, length = 100)
	private String apellido;
	
	/**
	 * Crea una nueva instancia de la clase Persona.
	 */
	public Persona() {
	}

	/**
	 * Devuelve el valor de la cedula.
	 */
	public String getCedula() {
		return cedula;
	}

	/**
	 * Establece el valor de la cedula, siempre y cuando esta sea valida.<br><br>
	 * 
	 * @throws Exception - Si la cedula es incorrecta.
	 */
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	/**
	 * Devuelve el valor del nombre.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece el valor del nombre.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve el valor del apellido.
	 */
	public String getApellido() {
		return apellido;
	}
	
	/**
	 * Establece el valor del apellido.
	 */
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	@Override
	public String toString() {
		return nombre + " " + apellido;
	}
}