package br.com.ufpi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.DragDropEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;

import br.com.ufpi.dao.ArquivoDao;
import br.com.ufpi.dao.AtividadeDao;
import br.com.ufpi.enuns.TemplateEnum;
import br.com.ufpi.model.Arquivo;
import br.com.ufpi.model.Atividade;
import br.com.ufpi.model.ListaAtividade;
import br.com.ufpi.util.ArquivoUtil;

@Named
@ViewScoped
public class CadastroListaAtividadeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private AtividadeDao atividadeDao;

	private List<Arquivo> imagens;

	private String nomeAtividade;

	private String palavra;

	private TemplateEnum templateSelecionado;

	private Atividade atividadeSelecionada;

	@Inject
	private ArquivoDao arquivoDao;

	private LazyDataModel<Atividade> atividades;

	private Set<Atividade> atividadesSelecionadas;

	private Set<Long> idsAtividadesSelecionadas;

	@Inject
	private ListaAtividade lista;

	@PostConstruct
	public void postConstructor() {
		imagens = new ArrayList<>();
		atividadesSelecionadas = new HashSet<>();
		idsAtividadesSelecionadas = new HashSet<>();
		pesquisar();
	}

	public void pesquisar() {
		setAtividades(new LazyDataModel<Atividade>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Atividade> load(int first, int pageSize, List<SortMeta> multiSortMeta,
					Map<String, Object> filters) {
				List<Atividade> listaAtividades = new ArrayList<>();

				listaAtividades = atividadeDao.listarAtividades(getNomeAtividade(), getTemplateSelecionado(),
						getPalavra(), idsAtividadesSelecionadas, first, pageSize, multiSortMeta);
				listaAtividades.removeAll(atividadesSelecionadas);

				int count = atividadeDao.contarAtividades(getNomeAtividade(), getTemplateSelecionado(), getPalavra(),
						idsAtividadesSelecionadas);
				this.setRowCount(count);

				return listaAtividades;
			}

		});

	}

	public void onAtividadeDrop(DragDropEvent ddEvent) {
		Atividade atividade = ((Atividade) ddEvent.getData());
		adicionar(atividade);

	}

	public void definirAtividadeDetalhes(Atividade atividade) {
		this.atividadeSelecionada = atividade;
		atividade = atividadeDao.carregarAtividadeComArquivos(atividade.getId());
		if (atividade != null) {
			List<Arquivo> arquivos = arquivoDao.carregarArquivosDaAtividade(atividade);
			atividade.setImagens(arquivos);
			atividadeSelecionada = atividade;
			imagens = atividade.getImagens();
		}
	}

	public void adicionar(Atividade atividade) {
		atividadesSelecionadas.add(atividade);
		idsAtividadesSelecionadas.add(atividade.getId());
		pesquisar();

	}

	public void remover(Atividade atividade) {
		atividadesSelecionadas.remove(atividade);
		System.out.println("Atividades: " + atividadesSelecionadas.size());
		idsAtividadesSelecionadas.remove(atividade.getId());
		System.out.println("Ids: " + idsAtividadesSelecionadas.size());
		pesquisar();
	}

	public void cadastrar() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String validacoes = validarListaAtividades();
		if (!StringUtils.isEmpty(validacoes))
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, validacoes, null));
	}

	private String validarListaAtividades() {
		if (StringUtils.isEmpty(lista.getNome()))
			return "É necessário informar o nome da lista.";
		if (CollectionUtils.isEmpty(atividadesSelecionadas))
			return "É necessário adicionar atividades para a lista.";

		return null;
	}

	public void cancelar() {
		lista = new ListaAtividade();
		atividadesSelecionadas = new HashSet<>();
		idsAtividadesSelecionadas = new HashSet<>();
		limparInformacoes();
	}

	public StreamedContent downloadImagem(Arquivo imagem) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
		Date d = new Date();

		String parteNomeArquivo = sdf.format(d);

		String dirTmp = ArquivoUtil.getDiretorio();

		FileOutputStream file = new FileOutputStream(dirTmp + "imagem_" + parteNomeArquivo + ".jpg");
		file.write(imagem.getBytesArquivo());
		file.close();

		DefaultStreamedContent arquivo = new DefaultStreamedContent(
				new FileInputStream(new File(dirTmp + "imagem_" + parteNomeArquivo + ".jpg")), "application/jpg",
				"imagem_" + parteNomeArquivo + ".jpg");
		return arquivo;
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

	public List<Arquivo> getImagens() {
		return imagens;
	}

	public void setImagens(List<Arquivo> imagens) {
		this.imagens = imagens;
	}

	public ArquivoDao getArquivoDao() {
		return arquivoDao;
	}

	public void setArquivoDao(ArquivoDao arquivoDao) {
		this.arquivoDao = arquivoDao;
	}

	public LazyDataModel<Atividade> getAtividades() {
		return atividades;
	}

	public void setAtividades(LazyDataModel<Atividade> atividades) {
		this.atividades = atividades;
	}

	public Atividade getAtividadeSelecionada() {
		return atividadeSelecionada;
	}

	public void setAtividadeSelecionada(Atividade atividadeSelecionada) {
		this.atividadeSelecionada = atividadeSelecionada;
	}

	public String getNomeAtividade() {
		return nomeAtividade;
	}

	public void setNomeAtividade(String nomeAtividade) {
		this.nomeAtividade = nomeAtividade;
	}

	public String getPalavra() {
		return palavra;
	}

	public void setPalavra(String palavra) {
		this.palavra = palavra;
	}

	public Set<Atividade> getAtividadesSelecionadas() {
		return atividadesSelecionadas;
	}

	public void setAtividadesSelecionadas(Set<Atividade> atividadesSelecionadas) {
		this.atividadesSelecionadas = atividadesSelecionadas;
	}

	public Set<Long> getIdsAtividadesSelecionadas() {
		return idsAtividadesSelecionadas;
	}

	public void setIdsAtividadesSelecionadas(Set<Long> idsAtividadesSelecionadas) {
		this.idsAtividadesSelecionadas = idsAtividadesSelecionadas;
	}

	public ListaAtividade getLista() {
		return lista;
	}

	public void setLista(ListaAtividade lista) {
		this.lista = lista;
	}

	public TemplateEnum getTemplateSelecionado() {
		return templateSelecionado;
	}

	public void setTemplateSelecionado(TemplateEnum templateSelecionado) {
		this.templateSelecionado = templateSelecionado;
	}

}
