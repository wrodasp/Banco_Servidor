package ec.edu.ups.datos;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ec.edu.ups.modelos.Usuario;

/**
 * Esta clase permite realizar operaciones 
 * de mantenimiento de un Usuario.
 **/
@Stateless
public class UsuarioDAO {

	@PersistenceContext(name = "usuarioService")
	private EntityManager manager;
	
	/**
	 * Crea una nueva instancia de la clase UsuarioDAO.
	 **/
	public UsuarioDAO() {
	}
	
	/**
	 * Agrega el usuario especificado a la base de datos.
	 **/
	public void agregar(Usuario usuario) {
		manager.persist(usuario);
	}
	
	/**
	 * Actualiza el usuario especificado en la base de datos.
	 **/
	public void modificar(Usuario usuario) {
		manager.merge(usuario);
	}
	
	/**
	 * Elimina el usuario especificado de la base de datos.
	 **/
	public void eliminar(Usuario usuario) {
		manager.remove(usuario);
	}
	
	/**
	 * Busca el usuario asociado al correo especificado en la base de datos.
	 **/
	public Usuario buscar(String correo) {
		Usuario usuario = manager.find(Usuario.class, correo);
		if (usuario != null) {
			usuario.getListaNotificaciones().size();
			usuario.getListaRegistroSesiones().size();
		}
		return usuario;
	}
	
	/**
	 * Devuelve una lista de usuarios de la base de datos.
	 **/
	public List<Usuario> listar() {
		String jpql = "select u from Usuario u";
		List<Usuario> usuarios = manager.createQuery(jpql, Usuario.class).getResultList();
		usuarios.forEach(usuario -> {
			usuario.getListaNotificaciones().size();
			usuario.getListaRegistroSesiones().size();
		});
		return usuarios;
	}
}