package br.com.ufpi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.ufpi.model.Estudante;

@FacesConverter(value = "estudanteConverter", forClass = Estudante.class)
public class EstudanteConverter implements Converter {
	private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);

	@Override
	public Object getAsObject(FacesContext facesContext,
			UIComponent uiComponent, String value) {
		if (value != null && !value.isEmpty()) {
			return (Estudante) uiComponent.getAttributes().get(value);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext facesContext,
			UIComponent uiComponent, Object value) {
		if (value instanceof Estudante) {
			Estudante entity = (Estudante) value;
			if (entity != null && entity instanceof Estudante
					&& entity.getId() != null) {
				uiComponent.getAttributes().put(entity.getId().toString(),
						entity);
				return entity.getId().toString();
			}
		}
		return "";
	}
	
  public static Date parse(String dateString) throws ParseException {
	Date result = format.parse(dateString);
	return result;
  }
  
  public static String format(Date data){
      return format.format(data);
  }

}
