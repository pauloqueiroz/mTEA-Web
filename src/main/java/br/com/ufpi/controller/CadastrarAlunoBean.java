package br.com.ufpi.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.ufpi.dao.ArquivoDao;
import br.com.ufpi.dao.EstudanteDao;
import br.com.ufpi.enuns.SituacaoEnum;
import br.com.ufpi.model.Arquivo;
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
	
	@Inject
	private Arquivo arquivoEstudante;
	
	private UploadedFile arquivo;
	
	@Inject
	private ArquivoDao arquivoDao;
	
	public void salvar(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String validacoes = validarAluno(estudante);
		if(!StringUtils.isEmpty(validacoes)){
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, validacoes,
					null));
			return;
		}
		estudante.setSituacao(SituacaoEnum.ATIVO);
		estudanteDao.adicionar(estudante);
		facesContext.addMessage(null, new FacesMessage("Aluno cadastrado com sucesso.", null));
		adicionarArquivo();
	}
	
	private String validarAluno(Estudante estudante) {
		int count = estudanteDao.contarEstudantes(estudante.getNome());
		if(count > 0)
			return "Já existe um aluno com o nome "+estudante.getNome()+" cadastrado.";
		if(estudante.getDataNascimento().after(new Date()))
			return "A data de nascimento não pode ser futura.";
		return null;
	}

	private void adicionarArquivo() {
		if (arquivo != null && arquivo.getSize() != 0) {
			arquivoEstudante.setDataUpload(new Date());
			arquivoEstudante.setNomeArquivo(arquivo.getFileName());
			InputStream is = null;
			try {
				is = arquivo.getInputstream();
				byte[] bytes = IOUtils.toByteArray(is);
				arquivoEstudante.setBytesArquivo(bytes);
				is.close();
			} catch (IOException e) {
				System.out.println("Erro ao tentar pegar bytes do arquivo!");
				throw new RuntimeException();
			}
			
			System.out.println("adicionar arquivo.");
			arquivoEstudante.setEstudante(estudante);
			arquivoDao.adicionar(arquivoEstudante);
			limpar();
		}
		
	}

	/**
	 * Metodo que limpa as informações do formulario de cadastro de um novo
	 * usuário.
	 */
	public void limpar() {
		this.estudante = new Estudante();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		UIViewRoot uiViewRoot = facesContext.getViewRoot();
		InputText inputText = (InputText) uiViewRoot
				.findComponent("formPrincipal:nome");
		inputText.setSubmittedValue("");
		Calendar calendarComponent = (Calendar) uiViewRoot.findComponent("formPrincipal:dataNascimento");
		calendarComponent.setSubmittedValue("");
		arquivo = null;
		estudante = new Estudante();
		arquivoEstudante = new Arquivo();
		
	}
	
	public void upload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
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

	public UploadedFile getArquivo() {
		return arquivo;
	}

	public void setArquivo(UploadedFile arquivo) {
		this.arquivo = arquivo;
	}

	public Arquivo getArquivoEstudante() {
		return arquivoEstudante;
	}

	public void setArquivoEstudante(Arquivo arquivoEstudante) {
		this.arquivoEstudante = arquivoEstudante;
	}

}
