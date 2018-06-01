package br.com.ufpi.util;

import java.util.Map;

import javax.faces.context.FacesContext;

import br.com.ufpi.model.Usuario;

public class UsuarioUtils {

	public static Usuario getUsuarioLogado() {
		Map<String, Object> session = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		Usuario usuario = (Usuario) session.get("usuarioDaSessao");
		return usuario;
	}
}
