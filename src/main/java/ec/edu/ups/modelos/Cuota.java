package ec.edu.ups.modelos;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ec.edu.ups.modelos.enums.EstadoCuota;

/**
 * Esta clase permite guardar datos 
 * referentes a una Cuota.
 */
@Entity
@Table(name = "cuotas")
public class Cuota implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private int id;
	
	@Column(nullable = false, precision = 2)
	private double monto;
	
	@Column(nullable = false, precision = 2)
	private double saldo;
	
	@Column(nullable = false)
	private LocalDate fechaVencimiento;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	private EstadoCuota estado;
	
	/**
	 * Crea una nueva instancia de la clase Cuota.
	 */
	public Cuota() {
		estado = EstadoCuota.PENDIENTE;
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
	 * Devuelve el valor del monto.
	 */
	public double getMonto() {
		return monto;
	}
	
	/**
	 * Devuelve el valor del monto.
	 */
	public void setMonto(double monto) {
		this.monto = monto;
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
	 * Devuelve el valor de la fecha de vencimiento.
	 */
	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}

	/**
	 * Establece el valor de la fecha de vencimiento.
	 */
	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	/**
	 * Devuelve el valor del estado (PENDIENTE, PAGADA, VENCIDA).
	 */
	public EstadoCuota getEstado() {
		return estado;
	}

	/**
	 * Establece el valor del estado (PENDIENTE, PAGADA, VENCIDA).
	 */
	public void setEstado(EstadoCuota estado) {
		this.estado = estado;
	}
	
	/**
	 * Abona el monto especificado a la cuota.
	 */
	public void abonar(double monto) {
	    saldo -= monto;	
		if (saldo <= 0) {
			estado = EstadoCuota.PAGADA;
		}
	}
}