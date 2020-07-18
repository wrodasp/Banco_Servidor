package ec.edu.ups.modelos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Esta clase permite guardar datos 
 * referente a una cuenta bancaria.
 */
@Entity
@Table(name = "cuentas")
public class Cuenta implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private int id;
	
	@Column(nullable = false, precision = 2)
	private double saldo;
	
	@Column(nullable = false)
	private LocalDate fechaApertura;
	
	@JoinColumn(name = "persona_id", referencedColumnName = "cedula")
	@OneToOne(fetch = FetchType.EAGER)
	private Persona propietario;
	
	@JoinColumn(name = "cuenta_id")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Transaccion> listaTransacciones;
	
	@JoinColumn(name = "cuenta_id")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<SolicitudCredito> listaSolicitudes;
	
	@JoinColumn(name = "cuenta_id")
	@OneToMany(fetch = FetchType.LAZY)
	private List<Credito> listaCreditos;
	
	/**
	 * Asigna los datos de una cuenta nueva.
	 */
	public Cuenta() {
		fechaApertura = LocalDate.now();
		listaTransacciones = new ArrayList<>();
		listaSolicitudes = new ArrayList<>();
		listaCreditos = new ArrayList<>();
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
	 * Devuelve el valor del saldo.
	 */
	public double getSaldo() {
		return saldo;
	}

	/**
	 * Establece el valor del saldo.
	 */
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	/**
	 * Devuelve el valor de la fecha de apertura.
	 */
	public LocalDate getFechaApertura() {
		return fechaApertura;
	}

	/**
	 * Establece el valor de la fecha de apertura.
	 */
	public void setFechaApertura(LocalDate fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	/**
	 * Devuelve el valor del propietario de esta cuenta.
	 */
	public Persona getPropietario() {
		return propietario;
	}
	
	/**
	 * Establece el valor del propietario de esta cuenta.
	 */
	public void setPropietario(Persona propietario) {
		this.propietario = propietario;
	}
	
	/**
	 * Devuelve la lista de transacciones asociadas.
	 */
	public List<Transaccion> getListaTransacciones() {
		return listaTransacciones;
	}

	/**
	 * Establece la lista de transacciones asociadas.
	 */
	public void setListaTransacciones(List<Transaccion> listaTransacciones) {
		this.listaTransacciones = listaTransacciones;
	}

	/**
	 * Devuelve la lista de solicitudes asociadas.
	 */
	public List<SolicitudCredito> getListaSolicitudes() {
		return listaSolicitudes;
	}

	/**
	 * Establece la lista de solicitudes asociadas.
	 */
	public void setListaSolicitudes(List<SolicitudCredito> listaSolicitudes) {
		this.listaSolicitudes = listaSolicitudes;
	}

	/**
	 * Devuelve la lista de creditos asociados.
	 */
	public List<Credito> getListaCreditos() {
		return listaCreditos;
	}

	/**
	 * Establece la lista de creditos asociados.
	 */
	public void setListaCreditos(List<Credito> listaCreditos) {
		this.listaCreditos = listaCreditos;
	}
	
	public void depositarDinero(double monto) {
		saldo += monto;
	}
	
	public void retirarDinero(double monto) throws Exception {
		if (monto > saldo) {
			throw new Exception("Saldo insuficiente. Solo se puede retirar " + saldo + "$.");
		}
		saldo -= monto;
	}

	@Override
	public String toString() {
		return "Cuenta [id=" + id + ", saldo=" + saldo + ", fechaApertura=" + fechaApertura + ", propietario="
				+ propietario + "]";
	}
}
