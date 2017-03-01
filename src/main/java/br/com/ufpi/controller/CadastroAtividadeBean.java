package br.com.ufpi.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;

import br.com.ufpi.dao.ArquivoDao;
import br.com.ufpi.dao.AtividadeDao;
import br.com.ufpi.dao.EstudanteDao;
import br.com.ufpi.enuns.TemplateEnum;
import br.com.ufpi.model.Arquivo;
import br.com.ufpi.model.Atividade;
import br.com.ufpi.model.Estudante;

@Named
@ViewScoped
public class CadastroAtividadeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private AtividadeDao atividadeDao;

	@Inject
	private EstudanteDao estudanteDao;
	
	@Inject
	private ArquivoDao arquivoDao;
	
	@Inject
	private Atividade atividade;

	private Estudante estudanteSelecionado;

	private TemplateEnum templateSelecionado;
	
	private String palavra;
	
	private List<Arquivo> listaArquivos;
	
	private List<InputStream> conteudoArquivos;
	
	private boolean templateUpload = false;
	
	private boolean templatePalavra = false;

	@PostConstruct
	public void init(){
		listaArquivos = new ArrayList<>();
		conteudoArquivos = new ArrayList<>();
	}

	public List<Estudante> buscarEstudante(String nome) {
		return estudanteDao.buscarEstudante(nome);
	}

	public TemplateEnum[] getTemplates() {
		return TemplateEnum.values();
	}
	
	public void salvarUploads(FileUploadEvent event) {
		if (event.getFile() != null) {
			try {
				conteudoArquivos.add(event.getFile().getInputstream());
			} catch (IOException e) {
				System.out.println("Erro ao salvar arquivo");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Metodo que armazena os arquivos carregados em um lista para serem
	 * adicionados posteriormente na atividade.
	 */
	public void salvarArquivos() {
		
		listaArquivos.clear();
		
		for (InputStream stream : conteudoArquivos) {
		
			Arquivo arquivo = new Arquivo();
			arquivo.setDataUpload(new Date());
			try {
				byte[] bytes = IOUtils.toByteArray(stream);
				arquivo.setBytesArquivo(bytes);;
				this.listaArquivos.add(arquivo);
				getAtividade().setImagens(this.listaArquivos);
				arquivo.setAtividade(getAtividade());
				int numeroArquivo = arquivoDao.contarArquivos();
				arquivo.setNomeArquivo(String.valueOf(numeroArquivo)+".jpg");
				arquivoDao.adicionar(arquivo);
			} catch (IOException e) {
				System.out.println("Erro ao salvar imagens.");
				e.printStackTrace();
			}
		}
	}

	public void atualizarFlags() {
		if (templateSelecionado.equals(TemplateEnum.JOGO_MEMORIA)
				|| templateSelecionado.equals(TemplateEnum.NOMEAR_FIGURA)
				|| templateSelecionado.equals(TemplateEnum.GENIOS))
			templateUpload = true;
		else
			templateUpload = false;
		if (templateSelecionado.equals(TemplateEnum.FORMAR_PALAVRA)
				|| templateSelecionado.equals(TemplateEnum.NOMEAR_FIGURA)
				|| templateSelecionado.equals(TemplateEnum.SOBREPOR_PALAVRA))
			templatePalavra = true;
		else
			templatePalavra = false;
		System.out.println(String.valueOf(templateUpload +""+templatePalavra));
	}
	
	public void salvar(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		System.out.println("lista size: " +listaArquivos.size());
		System.out.println("palavra: "+palavra);
		System.out.println(templateSelecionado.toString());
		System.out.println(estudanteSelecionado.getNome());
		getAtividade().setEstudante(estudanteSelecionado);
		getAtividade().setPalavra(palavra);
		getAtividade().setTemplate(templateSelecionado);
		getAtividade().setEstudanteTemplate(estudanteSelecionado.getNome() + " - " +templateSelecionado.getDescricao());
		atividadeDao.adicionar(getAtividade());
		salvarArquivos();
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, "Sucesso na operação!",
				"Atividade cadastrada com sucesso."));
	}
	
	public void limpar(){
		listaArquivos.clear();
		templateSelecionado = null;
		estudanteSelecionado = new Estudante();
		palavra = "";
	}

	public AtividadeDao getAtividadeDao() {
		return atividadeDao;
	}

	public void setAtividadeDao(AtividadeDao atividadeDao) {
		this.atividadeDao = atividadeDao;
	}

	public TemplateEnum getTemplateSelecionado() {
		return templateSelecionado;
	}

	public void setTemplateSelecionado(TemplateEnum templateSelecionado) {
		this.templateSelecionado = templateSelecionado;
	}

	public Estudante getEstudanteSelecionado() {
		return estudanteSelecionado;
	}

	public void setEstudanteSelecionado(Estudante estudanteSelecionado) {
		this.estudanteSelecionado = estudanteSelecionado;
	}

	public boolean isTemplateUpload() {
		return templateUpload;
	}

	public void setTemplateUpload(boolean templateUpload) {
		this.templateUpload = templateUpload;
	}

	public String getPalavra() {
		return palavra;
	}

	public void setPalavra(String palavra) {
		this.palavra = palavra;
	}

	public List<InputStream> getConteudoArquivos() {
		return conteudoArquivos;
	}

	public void setConteudoArquivos(List<InputStream> conteudoArquivos) {
		this.conteudoArquivos = conteudoArquivos;
	}

	public boolean isTemplatePalavra() {
		return templatePalavra;
	}

	public void setTemplatePalavra(boolean templatePalavra) {
		this.templatePalavra = templatePalavra;
	}

	public List<Arquivo> getListaArquivos() {
		return listaArquivos;
	}

	public void setListaArquivos(List<Arquivo> listaArquivos) {
		this.listaArquivos = listaArquivos;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

}
