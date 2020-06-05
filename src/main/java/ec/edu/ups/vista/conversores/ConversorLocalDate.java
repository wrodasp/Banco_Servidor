package ec.edu.ups.vista.conversores;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "banco.ConversorFecha")
public class ConversorLocalDate implements Converter<LocalDate>{

	@Override
	public LocalDate getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null) {
			return null;
		}
		return LocalDate.parse(value, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, LocalDate value) {
		if (value == null) {
			return null;
		}
		return value.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}
}