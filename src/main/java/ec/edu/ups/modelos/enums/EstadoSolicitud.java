package ec.edu.ups.modelos.enums;

/**
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