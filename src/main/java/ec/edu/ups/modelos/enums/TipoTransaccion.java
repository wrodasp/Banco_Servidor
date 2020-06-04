package ec.edu.ups.modelos.enums;

/**
 * Este enum guarda las constantes 
 * para los posibles tipos de una Transaccion. 
 **/
public enum TipoTransaccion {
	DEPOSITO("Deposito"),
	RETIRO("Retiro");
	
	private String etiqueta;
	
	private TipoTransaccion(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	
	public String getEtiqueta() {
		return etiqueta;
	}
}