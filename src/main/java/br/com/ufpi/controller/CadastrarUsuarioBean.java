package br.com.ufpi.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.inputtext.InputText;

import br.com.ufpi.dao.UsuarioDao;
import br.com.ufpi.model.Usuario;
import br.com.ufpi.util.Md5Utils;

/**
 * Bean respons√°vel pelo fluxo de cadastro de um novo usu√°rio.
 * 
 * @author Paulo Sergio
 *
 */
@Named
@ViewScoped
public class CadastrarUsuarioBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioDao usuarioEJB;

	@Inject
	private Usuario usuario;

	private String senha;

	/**
	 * MÈtodo que salva as informacoes do usuario cadastrado.
	 */
	public void salvar() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (this.getUsuario().getLogin() == null) {
			facesContext.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Erro de validaÁ„o", "… necess·rio informar um username"));
			return;
		}
		Usuario usuario = usuarioEJB.buscaUsuarioPorUser(this.getUsuario()
				.getLogin());
		if (usuario != null) {
			facesContext.addMessage(null, new FacesMessage("Erro de validaÁ„o", "J√° existe um usuario cadastrado com o username "
							+ this.getUsuario().getLogin()));
		} else {
			this.getUsuario().setAdmin(false);
			this.getUsuario().setSenha(
					Md5Utils.convertStringToMd5(senha));

			usuarioEJB.adiciona(this.getUsuario());
			facesContext.addMessage(null, new FacesMessage("OperaÁ„o realizada com sucesso!", "Usu·rio cadastrado com sucesso!"));
			limpar();
		}

	}

	/**
	 * Metodo que limpa as informa√ß√µes do formulario de cadastro de um novo
	 * usu√°rio.
	 */
	public void limpar() {
		setUsuario(new Usuario());
		this.senha = new String();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		UIViewRoot uiViewRoot = facesContext.getViewRoot();
		InputText inputText = (InputText) uiViewRoot
				.findComponent("formPrincipal:nome");
		inputText.setSubmittedValue("");
		inputText = (InputText) uiViewRoot.findComponent("formPrincipal:email");
		inputText.setSubmittedValue("");
		inputText = (InputText) uiViewRoot
				.findComponent("formPrincipal:userName");
		inputText.setSubmittedValue("");

	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
