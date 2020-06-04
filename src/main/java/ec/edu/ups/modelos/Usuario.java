package ec.edu.ups.modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ec.edu.ups.modelos.enums.TipoUsuario;

/**
 * Esta clase permite guardar datos 
 * referentes a un Usuario.
 */
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

	@Id
	@Column(nullable = false)
	private String correo;
	
	@Column(nullable = false)
	private String clave;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	private TipoUsuario rol;
	
	@JoinColumn(name = "persona_id", referencedColumnName = "cedula")
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Persona propietario;
	
	@JoinColumn(name = "usuario_id")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Notificacion> listaNotificaciones;
	
	@JoinColumn(name = "usuario_id")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<RegistroSesion> listaRegistroSesiones;
	
	/**
	 * Crea una nueva instancia de la clase Usuario.
	 */
	public Usuario() {
		listaNotificaciones = new ArrayList<>();
		listaRegistroSesiones = new ArrayList<>();
	}

	/**
	 * Devuelve el valor del correo electrónico.
	 */
	public String getCorreo() {
		return correo;
	}

	/**
	 * Establece el valor del correo electrónico.
	 */
	public void setCorreo(String correo) {
		this.correo = correo;
	}

	/**
	 * Devuelve el valor de la clave.
	 */
	public String getClave() {
		return clave;
	}

	/**
	 * Establece el valor de la clave.
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}

	/**
	 * Devuelve el valor del rol (CAJERO,JEFE DE CREDITO, CLIENTE).
	 */
	public TipoUsuario getRol() {
		return rol;
	}

	/**
	 * Establece el valor del rol (CAJERO, JEFE DE CREDITO, CLIENTE).
	 */
	public void setRol(TipoUsuario rol) {
		this.rol = rol;
	}

	/**
	 * Devuelve el valor del propietario de esta cuenta de usuario.
	 */
	public Persona getPropietario() {
		return propietario;
	}
	
	/**
	 * Establece el valor del propietario de esta cuenta de usuario.
	 */
	public void setPropietario(Persona propietario) {
		this.propietario = propietario;
	}
	
	/**
	 * Devuelve la lista de notifaciones asociadas.
	 */
	public List<Notificacion> getListaNotificaciones() {
		return listaNotificaciones;
	}
	
	/**
	 * Establece la lista de notificaciones asociadas.
	 */
	public void setListaNotificaciones(List<Notificacion> listaNotificaciones) {
		this.listaNotificaciones = listaNotificaciones;
	}
	
	/**
	 * Devuelve la lista de registros de sesión asociados.
	 */
	public List<RegistroSesion> getListaRegistroSesiones() {
		return listaRegistroSesiones;
	}
	
	/**
	 * Establece la lista de registros de sesión asociados.
	 */
	public void setListaRegistroSesiones(List<RegistroSesion> listaRegistroSesiones) {
		this.listaRegistroSesiones = listaRegistroSesiones;
	}
}