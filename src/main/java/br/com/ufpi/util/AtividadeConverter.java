package br.com.ufpi.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.ufpi.model.Atividade;

@FacesConverter(value = "atividadeConverter", forClass = Atividade.class)
public class AtividadeConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext facesContext,
			UIComponent uiComponent, String value) {
		if (value != null && !value.isEmpty()) {
			return (Atividade) uiComponent.getAttributes().get(value);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext facesContext,
			UIComponent uiComponent, Object value) {
		if (value instanceof Atividade) {
			Atividade entity = (Atividade) value;
			if (entity != null && entity instanceof Atividade
					&& entity.getId() != null) {
				uiComponent.getAttributes().put(entity.getId().toString(),
						entity);
				return entity.getId().toString();
			}
		}
		return "";
	}
	
}
