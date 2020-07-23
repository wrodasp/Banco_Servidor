package ec.edu.ups.datos;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ec.edu.ups.modelos.Credito;
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
	}
	
	/**
	 * Actualiza la cuenta especificada en la base de datos.
	 **/
	public void modificar(Cuenta cuenta) {
		cuenta.getListaCreditos().forEach(credito -> {
			Credito aux = manager.find(Credito.class, credito.getId());
			if (aux != null) {
				System.out.println("Existe el credito");
				credito.getListaCuotas().forEach(cuota -> {
					manager.merge(cuota);
				});
				manager.merge(credito);
			} else {
				manager.persist(credito);
			}
		});
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
	 * @throws Exception 
	 **/
	public Cuenta buscar(int id) throws Exception {
		try {
			System.out.println("Llega");
			Cuenta cuenta = manager.find(Cuenta.class, id);
			if (cuenta != null) {
				cuenta.getListaCreditos().size();
				cuenta.getListaSolicitudes().size();
				cuenta.getListaTransacciones().size();
			}
			return cuenta;
		} catch (Exception e) {
			throw new Exception("Se ha producido un error al buscar la cuenta.");
		}
	}
	
	/**
	 * Busca la cuenta asociada al ID especificado en la base de datos según la cédula (APP MÓVIL).
	 **/
	public int buscarCedula(String cedula) {
		Cuenta cuenta = manager.createQuery(
				"SELECT c FROM Cuenta c WHERE c.propietario = '" + cedula + "'", Cuenta.class)
				.getSingleResult();
		return cuenta.getId();
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
