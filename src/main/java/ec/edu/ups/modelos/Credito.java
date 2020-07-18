package ec.edu.ups.modelos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ec.edu.ups.modelos.enums.TipoCredito;

/**
 * Esta clase permite guardar datos 
 * referentes a un credito.
 **/
@Entity
@Table(name = "creditos")
public class Credito implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private int id;
	
	@Column(nullable = false)
	private double monto;
	
	@Column(nullable = false)
	private double saldo;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	private TipoCredito tipo;
	
	@Column(nullable = false)
	private LocalDate fechaVencimiento;
	
	@JoinColumn(name = "credito_id")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Cuota> listaCuotas;
	
	/**
	 * Crea un nueva instancia de la clase Credito.
	 */
	public Credito() {
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
	 * Devuelve el valor del tipo de crédito.
	 */
	public TipoCredito getTipo() {
		return tipo;
	}
	
	/**
	 * Establece el valor del tipo de crédito.
	 */
	public void setTipo(TipoCredito tipo) {
		this.tipo = tipo;
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
	 * Devuelve la lista de cuotas asociadas.
	 */
	public List<Cuota> getListaCuotas() {
		return listaCuotas;
	}

	/**
	 * Establece la lista de cuotas asociadas.
	 */
	public void setListaCuotas(List<Cuota> listaCuotas) {
		this.listaCuotas = listaCuotas;
	}
}