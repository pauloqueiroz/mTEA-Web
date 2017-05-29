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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;

import br.com.ufpi.dao.ItemAtividadeDao;
import br.com.ufpi.dao.ListaAtividadeDao;
import br.com.ufpi.model.Arquivo;
import br.com.ufpi.model.ItemAtividade;
import br.com.ufpi.model.ListaAtividade;
import br.com.ufpi.util.ArquivoUtil;

/**
 * 
 * @author Paulo Sergio
 *
 */
@Named
@ViewScoped
public class ListagemListaAtividadeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LazyDataModel<ListaAtividade> listas;

	private String nomeListaAtividade;

	private String descricao;

	private Date dataInicio;

	private Date dataFinal;

	private Set<ItemAtividade> atividades;

	@Inject
	private ListaAtividadeDao listaAtividadeDao;

	@Inject
	private ItemAtividadeDao itemAtividadeDao;

	private ListaAtividade listaSelecionada;

	@PostConstruct
	public void postConstructor() {
		atividades = new HashSet<>();
		pesquisar();
	}

	public void pesquisar() {
		listas = new LazyDataModel<ListaAtividade>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<ListaAtividade> load(int first, int pageSize, List<SortMeta> multiSortMeta,
					Map<String, Object> filters) {
				List<ListaAtividade> listas = new ArrayList<>();

				listas = listaAtividadeDao.listar(nomeListaAtividade, descricao, dataInicio, dataFinal, first, pageSize,
						multiSortMeta);

				this.setRowCount(listaAtividadeDao.contar(nomeListaAtividade, descricao, dataInicio, dataFinal));

				return listas;
			}

		};

	}

	/**
	 * Limpa os dados de pesquisa.
	 */
	public void limparInformacoes() {
		nomeListaAtividade = "";
		descricao = "";
		dataInicio = null;
		dataFinal = null;
		pesquisar();
	}

	// public TemplateEnum[] getTemplates() {
	// return TemplateEnum.values();
	// }

	public void definirListaDetalhes(ListaAtividade atividade) {
		this.setListaSelecionada(atividade);
		if (atividade != null) {
			Set<ItemAtividade> arquivos = itemAtividadeDao.carregarAtividades(atividade);
			atividade.setAtividades(arquivos);
			setAtividades(atividade.getAtividades());
		}
	}

	// public void excluir(){
	// atividadeDao.delete(atividadeSelecionada);
	// FacesContext facesContext = FacesContext.getCurrentInstance();
	// facesContext.addMessage(null, new FacesMessage(
	// FacesMessage.SEVERITY_INFO, "Atividade apagada com sucesso.",
	// "Atividade apagada com sucesso."));
	// }

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

	public LazyDataModel<ListaAtividade> getListas() {
		return listas;
	}

	public void setListas(LazyDataModel<ListaAtividade> listas) {
		this.listas = listas;
	}

	public String getNomeListaAtividade() {
		return nomeListaAtividade;
	}

	public void setNomeListaAtividade(String nomeListaAtividade) {
		this.nomeListaAtividade = nomeListaAtividade;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public ListaAtividadeDao getListaAtividadeDao() {
		return listaAtividadeDao;
	}

	public void setListaAtividadeDao(ListaAtividadeDao listaAtividadeDao) {
		this.listaAtividadeDao = listaAtividadeDao;
	}

	public ItemAtividadeDao getItemAtividadeDao() {
		return itemAtividadeDao;
	}

	public void setItemAtividadeDao(ItemAtividadeDao itemAtividadeDao) {
		this.itemAtividadeDao = itemAtividadeDao;
	}

	public ListaAtividade getListaSelecionada() {
		return listaSelecionada;
	}

	public void setListaSelecionada(ListaAtividade listaSelecionada) {
		this.listaSelecionada = listaSelecionada;
	}

	public Set<ItemAtividade> getAtividades() {
		return atividades;
	}

	public void setAtividades(Set<ItemAtividade> atividades) {
		this.atividades = atividades;
	}

}
