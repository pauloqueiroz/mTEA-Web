package br.com.ufpi.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.inputtext.InputText;

import br.com.ufpi.dao.EstudanteDao;
import br.com.ufpi.model.Estudante;

/**
 * 
 * @author Paulo Sergio
 *
 */
@Named
@ViewScoped
public class CadastrarAlunoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EstudanteDao estudanteDao;
	
	@Inject
	private Estudante estudante;
	
	public void salvar(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		estudanteDao.adicionar(estudante);
		facesContext.addMessage(null, new FacesMessage("Sucesso ao realizar operaÁ„o.", "Aluno cadastrado com sucesso."));
	}
	
	/**
	 * Metodo que limpa as informa√ß√µes do formulario de cadastro de um novo
	 * usu√°rio.
	 */
	public void limpar() {
		this.estudante = new Estudante();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		UIViewRoot uiViewRoot = facesContext.getViewRoot();
		InputText inputText = (InputText) uiViewRoot
				.findComponent("formPrincipal:nome");
		inputText.setSubmittedValue("");
		inputText = (InputText) uiViewRoot.findComponent("formPrincipal:dataNascimento");
		inputText.setSubmittedValue("");

	}


	public EstudanteDao getEstudanteDao() {
		return estudanteDao;
	}

	public void setEstudanteDao(EstudanteDao estudanteDao) {
		this.estudanteDao = estudanteDao;
	}

	public Estudante getEstudante() {
		return estudante;
	}

	public void setEstudante(Estudante estudante) {
		this.estudante = estudante;
	}

}
