package ec.edu.ups.utilidades;

/**
 * Esta clase permite generar claves.
 */
public class GeneradorClave {
	 
	private static String NUMEROS = "0123456789";
	private static String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";
 
	private GeneradorClave() {
	}
	
	/**
	 * Devuelve la clave generada.
	 */
	public static String getNuevaClave(int longitud) {
		return generarClave(NUMEROS+MAYUSCULAS+MINUSCULAS, longitud);
	}
 
	/**
	 * Genera una clave randomica.
	 */
	private static String generarClave(String caracteres, int longitud) {
		String password = "";
		for (int i = 0; i < longitud; i++) {
			password += (caracteres.charAt((int)(Math.random() * caracteres.length())));
		}
		return password;
	}
}