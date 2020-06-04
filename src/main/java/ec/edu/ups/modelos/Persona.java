package ec.edu.ups.modelos;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
@ManagedBean
@ViewScoped
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
	
	/**
	 * Comprueba que la cedula ingresada sea válida.
	 */
	public boolean validarCedula() {
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
}