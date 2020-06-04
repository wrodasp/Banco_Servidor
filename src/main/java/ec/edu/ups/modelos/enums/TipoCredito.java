package ec.edu.ups.modelos.enums;

/**
 * Este enum guarda las constantes 
 * para los posibles tipos de un Credito. 
 **/
public enum TipoCredito {
	CONSUMO("Consumo"), 
	COMERCIAL("Comercial"),
	HIPOTECARIO("Hipotecario"),
	ESTUDIANTIL("Estudiantil");
	
	private String etiqueta;
	
	private TipoCredito(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	
	public String getEtiqueta() {
		return etiqueta;
	}
}