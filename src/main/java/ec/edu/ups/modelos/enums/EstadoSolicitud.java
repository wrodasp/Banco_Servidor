package ec.edu.ups.modelos.enums;
/**
 * 
 * @author BenavidesJuan, CalvaByron, RodasWilson
 *
 * Este enum guarda las constantes 
 * para los posibles estados de una SolicitudCredito. 
 **/
public enum EstadoSolicitud {
	TRAMITANDO("En tramite"),
	APROBADA("Aprobada"),
	RECHAZADA("Rechazada");
	
	private String etiqueta;
	
	private EstadoSolicitud(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	
	public String getEtiqueta() {
		return etiqueta;
	}
}