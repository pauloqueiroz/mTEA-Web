package br.com.ufpi.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.com.ufpi.dao.UsuarioDao;
import br.com.ufpi.model.Usuario;

/**
 * 
 * @author Paulo Sergio
 *
 */
@Named
@SessionScoped
public class LoginBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioDao usuarioDao;

	private String login;
	private String senha;

	public void login() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (getUsuarioLogado() == null) {
			Usuario usuario = usuarioDao.buscarUsuario(getLogin(),
					senha);
			if (usuario != null) {
				Map<String, Object> session = facesContext.getExternalContext()
						.getSessionMap();
				session.put("usuarioDaSessao", usuario);
				try {
					facesContext.getExternalContext().redirect("index.xhtml");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				facesContext.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Usuario e/ou senha incorretos.",
						null));
			}
		}
	}

	public void fazerLogout() {
		Map<String, Object> session = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		session.remove("usuarioDaSessao");
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		request.getSession().invalidate();
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("login.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Usuario getUsuarioLogado() {
		Map<String, Object> session = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		return (Usuario) session.get("usuarioDaSessao");
	}
	
	public boolean isPaginaInicial(){
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		String uri = ((HttpServletRequest) externalContext.getRequest()).getRequestURI();
//		System.out.println("URI: " +uri);
		if(uri.contains("index.xhtml"))
			return true;
		return false;
	}

	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
