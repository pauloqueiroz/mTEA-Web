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
import org.primefaces.model.UploadedFile;

import br.com.ufpi.dao.ArquivoDao;
import br.com.ufpi.dao.AtividadeDao;
import br.com.ufpi.enuns.TemplateEnum;
import br.com.ufpi.enuns.TipoArquivoEnum;
import br.com.ufpi.model.Arquivo;
import br.com.ufpi.model.Atividade;
import br.com.ufpi.util.EstudanteUtils;

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
	private ArquivoDao arquivoDao;
	
	@Inject
	private Atividade atividade;

	private TemplateEnum templateSelecionado;
	
	private String palavra;
	
	private String nomeAtividade;
	
	private List<Arquivo> listaArquivos;
	
	private List<InputStream> conteudoArquivos;
	
	private List<String> nomesArquivos;
	
	private List<TipoArquivoEnum> tiposArquivo;
	
	private boolean templateTemImagens = false;
	
	private boolean templatePalavra = false;
	
	private boolean templateAudio = false;
	
	private UploadedFile audio;

	@PostConstruct
	public void init(){
		listaArquivos = new ArrayList<>();
		conteudoArquivos = new ArrayList<>();
		nomesArquivos = new ArrayList<>();
		tiposArquivo = new ArrayList<>();
	}

	public TemplateEnum[] getTemplates() {
		return TemplateEnum.values();
	}
	
	public void salvarUploads(FileUploadEvent event) {
		if (event.getFile() != null) {
			try {
				conteudoArquivos.add(event.getFile().getInputstream());
				nomesArquivos.add(event.getFile().getFileName());
				tiposArquivo.add(TipoArquivoEnum.IMAGEM);
			} catch (IOException e) {
				System.out.println("Erro ao salvar arquivo");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Metodo que armazena os arquivos carregados em um lista para serem
	 * adicionados posteriormente na atividade.
	 * @throws IOException 
	 */
	public void salvarArquivos() throws IOException {
		
		listaArquivos.clear();
		int indiceNomeArquivo = 0;
		if(templateAudio && audio != null) {
			String nomeAudio = audio.getFileName();
			InputStream inputstreamAudio = audio.getInputstream();
			nomesArquivos.add(nomeAudio);
			conteudoArquivos.add(inputstreamAudio);
			tiposArquivo.add(TipoArquivoEnum.AUDIO);
		}
		for (InputStream stream : conteudoArquivos) {
		
			Arquivo arquivo = new Arquivo();
			arquivo.setDataUpload(new Date());
			try {
				byte[] bytes = IOUtils.toByteArray(stream);
				arquivo.setBytesArquivo(bytes);
				arquivo.setNomeArquivo(nomesArquivos.get(indiceNomeArquivo));
				arquivo.setTipoArquivo(tiposArquivo.get(indiceNomeArquivo));
				indiceNomeArquivo++;
				this.listaArquivos.add(arquivo);
				getAtividade().setArquivos(this.listaArquivos);
				arquivo.setAtividade(getAtividade());
				arquivoDao.adicionar(arquivo);
			} catch (IOException e) {
				System.out.println("Erro ao salvar arquivos.");
				e.printStackTrace();
			}
		}
	}

	public void atualizarFlags() {
		if (templateSelecionado.ordinal() < 5) {
			templateTemImagens = true;
			templateAudio = false;
		} else {
			templateAudio = true;
			if(templateSelecionado.equals(TemplateEnum.COPIA_POR_COMPOSICAO)) {				
				templateTemImagens = false;
			}else {
				templateTemImagens = true;
			}
		}
		if (templateSelecionado.equals(TemplateEnum.FORMAR_PALAVRA)
				|| templateSelecionado.equals(TemplateEnum.NOMEAR_FIGURA)
				|| templateSelecionado.equals(TemplateEnum.SOBREPOR_PALAVRA)
				|| templateSelecionado.equals(TemplateEnum.COPIA_POR_COMPOSICAO)
				|| templateSelecionado.equals(TemplateEnum.DITADO_MUDO)) {
			templatePalavra = true;
		} else {
			templatePalavra = false;
		}
	}
	
	public void salvar() throws IOException{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext ec = facesContext.getExternalContext();
		getAtividade().setPalavra(palavra);
		atividade.setDataCriacao(new Date());
		if(!StringUtils.isEmpty(nomeAtividade))
			atividade.setNome(nomeAtividade);
		else
			atividade.setNome(criarNome(atividade, templateSelecionado, palavra, templatePalavra));
		getAtividade().setTemplate(templateSelecionado);
		String validacoes = validarAtividade(atividade);
		if(!StringUtils.isEmpty(validacoes)){
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, validacoes,
					null));
			return;
		}
		atividadeDao.adicionar(getAtividade());
		salvarArquivos();
		ec.redirect("sucessoCadastrarAtividade.xhtml?idAtividade="+atividade.getId());
	}
	
	private String criarNome(Atividade atividade, TemplateEnum templateSelecionado, String palavra, boolean templatePalavra) {
		String result = templateSelecionado.getDescricao();
		if(templatePalavra)
			result += " "+palavra;
		result += EstudanteUtils.getDataFormatada(atividade.getDataCriacao());
		return result;
	}

	private String validarAtividade(Atividade atividade){
		if(atividade.getTemplate() == null)
			return "É necessário informar o template.";
		TemplateEnum templateSelecionado = atividade.getTemplate();
		if(templateTemImagens && conteudoArquivos!= null && conteudoArquivos.size() > templateSelecionado.getQuantidadeMaximaArquivos())
			return "A quantidade máxima de imagens para o template " + templateSelecionado.getDescricao() + " É "
					+ templateSelecionado.getQuantidadeMaximaArquivos() + ". Você fez upload de "
					+ conteudoArquivos.size();
		if(templateTemImagens && templateSelecionado.getQuantidadeMinimaArquivos() > 0){
			if(conteudoArquivos == null || (conteudoArquivos != null && conteudoArquivos.size() < templateSelecionado.getQuantidadeMinimaArquivos()))
				return "A quantidade mínima de imagens para o template " + templateSelecionado.getDescricao() + " É "
				+ templateSelecionado.getQuantidadeMaximaArquivos() + ".";
		}
		if(templateAudio && audio == null) {
			return "É necessário informar o áudio do template.";
		}
		return null;
	}
	
	public void limpar(){
		atividade = new Atividade();
		listaArquivos.clear();
		nomesArquivos.clear();
		conteudoArquivos.clear();
		palavra = "";
	}
	
	public void removerArquivo(String nome) {
		int indiceArquivo = 0;
		for (int i = 0; i < nomesArquivos.size(); i++) {
			if(nome.equals(nomesArquivos.get(i))){
				indiceArquivo = i;
				nomesArquivos.remove(i);
				break;
			}	
		}
		try {
			conteudoArquivos.remove(indiceArquivo);
		}catch(Exception e) {
			System.out.println("Indice nao encontrado");
			e.printStackTrace();
		}
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

	public boolean isTemplateTemImagens() {
		return templateTemImagens;
	}

	public void setTemplateTemImagens(boolean templateTemImagens) {
		this.templateTemImagens = templateTemImagens;
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

	public String getNomeAtividade() {
		return nomeAtividade;
	}

	public void setNomeAtividade(String nomeAtividade) {
		this.nomeAtividade = nomeAtividade;
	}

	public UploadedFile getAudio() {
		return audio;
	}

	public void setAudio(UploadedFile audio) {
		this.audio = audio;
	}

	public boolean isTemplateAudio() {
		return templateAudio;
	}

	public void setTemplateAudio(boolean templateAudio) {
		this.templateAudio = templateAudio;
	}

	public List<String> getNomesArquivos() {
		return nomesArquivos;
	}

	public void setNomesArquivos(List<String> nomesArquivos) {
		this.nomesArquivos = nomesArquivos;
	}

	public List<TipoArquivoEnum> getTiposArquivo() {
		return tiposArquivo;
	}

	public void setTiposArquivo(List<TipoArquivoEnum> tiposArquivo) {
		this.tiposArquivo = tiposArquivo;
	}

}
