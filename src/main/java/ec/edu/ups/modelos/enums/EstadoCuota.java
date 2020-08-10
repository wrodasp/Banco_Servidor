package ec.edu.ups.modelos.enums;

/**
 * 
 * @author BenavidesJuan, CalvaByron, RodasWilson
 *
 * Este enum guarda las constantes 
 * para los posibles estados de una Cuota. 
 **/
public enum EstadoCuota {
	PENDIENTE("Pendiente"),
	PAGADA("Pagada"),
	VENCIDA("Vencida");
	
	private String etiqueta;
	
	private EstadoCuota(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	
	public String getEtiqueta() {
		return etiqueta;
	}
}