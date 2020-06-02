package ec.edu.ups.utilidades;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class UtilidadCorreo {

	public static void enviarCorreo(String correoDestino, String asunto, String contenido) {
		String usuario = "";
		String clave = "";
		Properties propiedades = new Properties();
		propiedades.setProperty("mail.smtp.host", "smtp.live.com");
		propiedades.setProperty("mail.smtp.starttls.enable", "true");
		propiedades.setProperty("mail.smtp.port", "587");
		propiedades.setProperty("mail.smtp.user", usuario);
		propiedades.setProperty("mail.smtp.auth", "true");
		
		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(usuario, clave);
			}
		};
		
		Session sesion = Session.getDefaultInstance(propiedades, auth);
		sesion.setDebug(true);
		
		try {
			MimeMessage mensaje = new MimeMessage(sesion);
			mensaje.setFrom(new InternetAddress(usuario));
			mensaje.addRecipient(RecipientType.TO, new InternetAddress(correoDestino));
			mensaje.setSubject(asunto);
			mensaje.setText(contenido);
			
			Transport transporte = sesion.getTransport("smtp");
			transporte.connect(usuario, clave);
			transporte.send(mensaje, mensaje.getAllRecipients());	
			transporte.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}