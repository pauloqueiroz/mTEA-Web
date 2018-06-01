package br.com.ufpi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;

import br.com.ufpi.dao.ArquivoDao;
import br.com.ufpi.dao.AtividadeDao;
import br.com.ufpi.dao.ItemAtividadeDao;
import br.com.ufpi.enuns.TemplateEnum;
import br.com.ufpi.model.Arquivo;
import br.com.ufpi.model.Atividade;
import br.com.ufpi.model.ItemAtividade;
import br.com.ufpi.model.Usuario;
import br.com.ufpi.util.ArquivoUtil;
import br.com.ufpi.util.UsuarioUtils;

@Named
@ViewScoped
public class ListagemAtividadesBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private AtividadeDao atividadeDao;
	
	@Inject
	private ArquivoDao arquivoDao;
	
	private LazyDataModel<Atividade> atividades;
	
	private String nomeAtividade;
	
	private String palavra;
	
	private TemplateEnum templateSelecionado;
	
	private Atividade atividadeSelecionada;
	
	private List<Arquivo> imagens;
	
	@Inject
	private ItemAtividadeDao itemAtividadeDao;
	
	@PostConstruct
	public void postConstructor() {
		imagens = new ArrayList<>();
		pesquisar();
	}


	public void pesquisar() {
		atividades = new LazyDataModel<Atividade>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Atividade> load(int first, int pageSize,
					List<SortMeta> multiSortMeta, Map<String, Object> filters) {
				List<Atividade> listaAtividades = new ArrayList<>();
				Usuario usuario = UsuarioUtils.getUsuarioLogado();
				listaAtividades = atividadeDao
						.listarAtividades(nomeAtividade, templateSelecionado, palavra, usuario, null, first, pageSize, multiSortMeta);
				
				this.setRowCount(atividadeDao
						.contarAtividades(nomeAtividade, templateSelecionado, palavra, usuario, null));
				
				return listaAtividades;
			}

		};
		
	}
	
	/**
	 * Limpa os dados de pesquisa.
	 */
	public void limparInformacoes() {
		nomeAtividade = "";
		templateSelecionado = null;
		palavra = "";
		pesquisar();
	}

	public TemplateEnum[] getTemplates() {
		return TemplateEnum.values();
	}
	
	public void definirAtividadeDetalhes(Atividade atividade){
		this.atividadeSelecionada = atividade;
		atividade = atividadeDao.carregarAtividadeComArquivos(atividade.getId());
		if(atividade != null){
			List<Arquivo> arquivos = arquivoDao.carregarArquivosDaAtividade(atividade);
			atividade.setArquivos(arquivos);
			atividadeSelecionada = atividade;
			imagens = atividade.getArquivos();
		}	
	}
	
	public void excluir(){
		List<ItemAtividade> itens = itemAtividadeDao.buscarAtividades(atividadeSelecionada);
		if(!CollectionUtils.isEmpty(itens)) {
			for (ItemAtividade itemAtividade : itens) {
				itemAtividade.setAtividade(null);
				itemAtividadeDao.deletar(itemAtividade);
			}
		}
		atividadeDao.delete(atividadeSelecionada);
//		if(!CollectionUtils.isEmpty(itens)) {
//			for (ItemAtividade itemAtividade : itens) {
//				itemAtividadeDao.deletar(itemAtividade);
//			}
//		}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, "Atividade apagada com sucesso.",
				"Atividade apagada com sucesso."));
	}
	
	public StreamedContent downloadArquivo(Arquivo arq) throws IOException {
//		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
//		Date d = new Date();
//
//		String parteNomeArquivo = sdf.format(d);

		String dirTmp = ArquivoUtil.getDiretorio();
		String nomeArquivo = arq.getNomeArquivo();

		FileOutputStream file = new FileOutputStream(dirTmp + nomeArquivo);
		file.write(arq.getBytesArquivo());
		file.close();
		
		String extensaoArquivo = ArquivoUtil.getExtensaoArquivo(arq.getNomeArquivo());

		DefaultStreamedContent arquivo = new DefaultStreamedContent(
				new FileInputStream(new File(dirTmp + nomeArquivo)), "application/"+extensaoArquivo, nomeArquivo);
		return arquivo;
	}
	
	public LazyDataModel<Atividade> getAtividades() {
		return atividades;
	}


	public void setAtividades(LazyDataModel<Atividade> atividades) {
		this.atividades = atividades;
	}

	public String getPalavra() {
		return palavra;
	}


	public void setPalavra(String palavra) {
		this.palavra = palavra;
	}


	public TemplateEnum getTemplateSelecionado() {
		return templateSelecionado;
	}


	public void setTemplateSelecionado(TemplateEnum templateSelecionado) {
		this.templateSelecionado = templateSelecionado;
	}


	public Atividade getAtividadeSelecionada() {
		return atividadeSelecionada;
	}


	public void setAtividadeSelecionada(Atividade atividadeSelecionada) {
		this.atividadeSelecionada = atividadeSelecionada;
	}


	public List<Arquivo> getArquivos() {
		return imagens;
	}


	public void setArquivos(List<Arquivo> imagens) {
		this.imagens = imagens;
	}


	public ArquivoDao getArquivoDao() {
		return arquivoDao;
	}


	public void setArquivoDao(ArquivoDao arquivoDao) {
		this.arquivoDao = arquivoDao;
	}


	public String getNomeAtividade() {
		return nomeAtividade;
	}


	public void setNomeAtividade(String nomeAtividade) {
		this.nomeAtividade = nomeAtividade;
	}


	public ItemAtividadeDao getItemAtividadeDao() {
		return itemAtividadeDao;
	}


	public void setItemAtividadeDao(ItemAtividadeDao itemAtividadeDao) {
		this.itemAtividadeDao = itemAtividadeDao;
	}

}
