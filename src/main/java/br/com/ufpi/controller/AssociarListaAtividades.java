package br.com.ufpi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.DragDropEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import br.com.ufpi.dao.EstudanteDao;
import br.com.ufpi.dao.ListaAtividadeDao;
import br.com.ufpi.model.Estudante;
import br.com.ufpi.model.ListaAtividade;

/**
 * 
 * @author Paulo Sergio
 *
 */
@Named
@ViewScoped
public class AssociarListaAtividades implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<ListaAtividade> listas;
	
	private Set<ListaAtividade> listasSelecionadas;

	private Set<Long> idsListasSelecionadas;
	
	private Estudante estudanteSelecionado;
	
	@Inject
	private EstudanteDao estudanteDao;
	
	@Inject
	private ListaAtividadeDao listaAtividadeDao;
	
	private String nomeListaAtividade;

	private String descricao;

	private Date dataInicio;

	private Date dataFinal;

	public AssociarListaAtividades() {
		super();
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
	
	public void adicionar(ListaAtividade lista) {
		getListasSelecionadas().add(lista);
		idsListasSelecionadas.add(lista.getId());
		pesquisar();

	}

	public void remover(ListaAtividade atividade) {
		getListasSelecionadas().remove(atividade);
		idsListasSelecionadas.remove(atividade.getId());
		pesquisar();
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
	
	public void onListaDrop(DragDropEvent ddEvent) {
		ListaAtividade lista = ((ListaAtividade) ddEvent.getData());
		adicionar(lista);

	}
	
	public List<Estudante> buscarEstudante(String nome) {
		return estudanteDao.buscarEstudante(nome);
	}

	public EstudanteDao getEstudanteDao() {
		return estudanteDao;
	}

	public void setEstudanteDao(EstudanteDao estudanteDao) {
		this.estudanteDao = estudanteDao;
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

	public Estudante getEstudanteSelecionado() {
		return estudanteSelecionado;
	}

	public void setEstudanteSelecionado(Estudante estudanteSelecionado) {
		this.estudanteSelecionado = estudanteSelecionado;
	}

	public LazyDataModel<ListaAtividade> getListas() {
		return listas;
	}

	public void setListas(LazyDataModel<ListaAtividade> listas) {
		this.listas = listas;
	}

	public Set<Long> getIdsListasSelecionadas() {
		return idsListasSelecionadas;
	}

	public void setIdsListasSelecionadas(Set<Long> idsListasSelecionadas) {
		this.idsListasSelecionadas = idsListasSelecionadas;
	}

	public ListaAtividadeDao getListaAtividadeDao() {
		return listaAtividadeDao;
	}

	public void setListaAtividadeDao(ListaAtividadeDao listaAtividadeDao) {
		this.listaAtividadeDao = listaAtividadeDao;
	}

	public Set<ListaAtividade> getListasSelecionadas() {
		return listasSelecionadas;
	}

	public void setListasSelecionadas(Set<ListaAtividade> listasSelecionadas) {
		this.listasSelecionadas = listasSelecionadas;
	}

}