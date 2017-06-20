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
 * Bean responsável pelo fluxo de cadastro de um novo usuário.
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
	 * Método que salva as informacoes do usuario cadastrado.
	 */
	public void salvar() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (this.getUsuario().getLogin() == null) {
			facesContext.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Erro de validação", "É necessário informar um username"));
			return;
		}
		Usuario usuario = usuarioEJB.buscaUsuarioPorUser(this.getUsuario()
				.getLogin());
		if (usuario != null) {
			facesContext.addMessage(null, new FacesMessage("Erro de validação", "Já existe um usuario cadastrado com o username "
							+ this.getUsuario().getLogin()));
		} else {
			this.getUsuario().setAdmin(false);
			this.getUsuario().setSenha(
					Md5Utils.convertStringToMd5(senha));

			usuarioEJB.adiciona(this.getUsuario());
			facesContext.addMessage(null, new FacesMessage("Operação realizada com sucesso!", "Usuário cadastrado com sucesso!"));
			limpar();
		}

	}

	/**
	 * Metodo que limpa as informações do formulario de cadastro de um novo
	 * usuário.
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
