package ec.edu.ups.modelos.enums;

/**
 * Este enum guarda las constantes 
 * para los posibles tipos de un Usuario. 
 **/
public enum TipoUsuario {
	ADMIN("Administrador"),
	CAJERO("Cajero"),
	JEFE_DE_CREDITO("Jefe de Credito"),
	CLIENTE("Cliente");
	
	private String etiqueta;
	
	private TipoUsuario(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	
	public String getEtiqueta() {
		return etiqueta;
	}
}