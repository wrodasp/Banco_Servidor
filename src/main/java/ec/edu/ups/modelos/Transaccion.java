package ec.edu.ups.modelos;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ec.edu.ups.modelos.enums.TipoTransaccion;

/**
 * Esta clase permite guardar datos 
 * referentes a una Transacción.
 */
@Entity
@Table(name = "transacciones")
public class Transaccion implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, precision = 2)
	private double monto;
	
	@Column(nullable = false)
	private LocalDate fecha;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	private TipoTransaccion tipo;
	
	/**
	 * Crea una nueva instancia de la clase Transaccion.
	 */
	public Transaccion() {
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
	 * Devuelve el valor del monto.
	 */
	public double getMonto() {
		return monto;
	}

	/**
	 * Establece el valor del monto.
	 */
	public void setMonto(double monto) {
		this.monto = monto;
	}
	
	/**
	 * Devuelve el valor de la fecha de la transacción.
	 */
	public LocalDate getFecha() {
		return fecha;
	}
	
	/**
	 * Establece el valor de la fecha de la transacción.
	 */
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	/**
	 * Devuelve el valor del tipo de transacción (DEPOSITO, RETIRO).
	 */
	public TipoTransaccion getTipo() {
		return tipo;
	}

	/**
	 * Establece el valor del tipo de transacción (DEPOSITO, RETIRO).
	 */
	public void setTipo(TipoTransaccion tipo) {
		this.tipo = tipo;
	}
}
