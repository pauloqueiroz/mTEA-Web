package br.com.ufpi.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
	
	private List<String> nomesArquivos;
	
	private boolean templateUpload = false;
	
	private boolean templatePalavra = false;

	@PostConstruct
	public void init(){
		listaArquivos = new ArrayList<>();
		conteudoArquivos = new ArrayList<>();
		nomesArquivos = new ArrayList<>();
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
				nomesArquivos.add(event.getFile().getFileName());
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
		int indiceNomeArquivo = 0;
		for (InputStream stream : conteudoArquivos) {
		
			Arquivo arquivo = new Arquivo();
			arquivo.setDataUpload(new Date());
			try {
				byte[] bytes = IOUtils.toByteArray(stream);
				arquivo.setBytesArquivo(bytes);
				arquivo.setNomeArquivo(nomesArquivos.get(indiceNomeArquivo));
				indiceNomeArquivo++;
				this.listaArquivos.add(arquivo);
				getAtividade().setImagens(this.listaArquivos);
				arquivo.setAtividade(getAtividade());
//				int numeroArquivo = arquivoDao.contarArquivos();
//				arquivo.setNomeArquivo(String.valueOf(numeroArquivo)+".jpg");
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
	
	public void salvar() throws IOException{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext ec = facesContext.getExternalContext();
		System.out.println("lista size: " +listaArquivos.size());
		System.out.println("palavra: "+palavra);
		System.out.println(templateSelecionado.toString());
		System.out.println(estudanteSelecionado.getNome());
		getAtividade().setEstudante(estudanteSelecionado);
		getAtividade().setPalavra(palavra);
		getAtividade().setTemplate(templateSelecionado);
		getAtividade().setEstudanteTemplate(estudanteSelecionado.getNome() + " - " +templateSelecionado.getDescricao());
		String validacoes = validarAtividade(atividade);
		if(!StringUtils.isEmpty(validacoes)){
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, validacoes,
					null));
			return;
		}
		atividadeDao.adicionar(getAtividade());
		salvarArquivos();
//		limpar();
//		ec.getRequestMap().put("atividade", atividade);
		ec.redirect("sucessoCadastrarAtividade.xhtml?idAtividade="+atividade.getId());
	}
	
	private String validarAtividade(Atividade atividade){
		if(atividade.getEstudante() == null)
			return "É necessário informar o estudante.";
		if(atividade.getTemplate() == null)
			return "É necessário informar o template.";
		TemplateEnum templateSelecionado = atividade.getTemplate();
		if(conteudoArquivos!= null && conteudoArquivos.size() > templateSelecionado.getQuantidadeMaximaArquivos())
			return "A quantidade máxima de imagens para o template " + templateSelecionado.getDescricao() + " é "
					+ templateSelecionado.getQuantidadeMaximaArquivos() + ". Você fez upload de "
					+ conteudoArquivos.size();
		if(templateSelecionado.getQuantidadeMinimaArquivos() > 0){
			if(conteudoArquivos == null || (conteudoArquivos != null && conteudoArquivos.size() < templateSelecionado.getQuantidadeMinimaArquivos()))
				return "A quantidade mínima de imagens para o template " + templateSelecionado.getDescricao() + " é "
				+ templateSelecionado.getQuantidadeMaximaArquivos() + ".";
		}
		return null;
	}
	
	public void limpar(){
		atividade = new Atividade();
		listaArquivos.clear();
		nomesArquivos.clear();
		conteudoArquivos.clear();
//		templateSelecionado = null;
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
