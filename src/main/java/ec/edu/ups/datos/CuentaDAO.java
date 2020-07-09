package ec.edu.ups.datos;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ec.edu.ups.modelos.Cuenta;

/**
 * Esta clase permite realizar las operaciones 
 * de mantenimiento de una Cuenta.
 **/
@Stateless
public class CuentaDAO {

	@PersistenceContext(name = "cuentaService")
	private EntityManager manager;
	
	/**
	 * Crea una nueva instancia de la clase CuentaDAO.
	 **/
	public CuentaDAO() {
	}
	
	/**
	 * Agrega la cuenta especificada a la base de datos.
	 **/
	public void agregar(Cuenta cuenta) {
		manager.persist(cuenta);
		manager.flush();
	}
	
	/**
	 * Actualiza la cuenta especificada en la base de datos.
	 **/
	public void modificar(Cuenta cuenta) {
		manager.merge(cuenta);
	}
	
	/**
	 * Elimina la cuenta especificada de la base de datos.
	 **/
	public void eliminar(Cuenta cuenta) {
		manager.remove(cuenta);
	}
	
	/**
	 * Busca la cuenta asociada al ID especificado en la base de datos.
	 **/
	public Cuenta buscar(int id) {
		Cuenta cuenta = manager.find(Cuenta.class, id);
		if (cuenta != null) {
			cuenta.getListaCreditos().size();
			cuenta.getListaSolicitudes().size();
			cuenta.getListaTransacciones().size();
		}
		return cuenta;
	}
	
	/**
	 * Devuelve un lista de cuentas de la base de datos.
	 **/
	public List<Cuenta> listar() {
		String jpql = "select c from Cuenta c";
		List<Cuenta> cuentas = manager.createQuery(jpql, Cuenta.class).getResultList();
		cuentas.forEach(cuenta -> {
			cuenta.getListaCreditos().size();
			cuenta.getListaSolicitudes().size();
			cuenta.getListaTransacciones().size();
		});
		return cuentas;
	}
}
