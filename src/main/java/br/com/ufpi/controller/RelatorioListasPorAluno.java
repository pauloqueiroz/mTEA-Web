package br.com.ufpi.controller;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import br.com.ufpi.dao.EstudanteDao;
import br.com.ufpi.dao.ItemAtividadeDao;
import br.com.ufpi.dao.ItemListaEstudanteDao;
import br.com.ufpi.enuns.SituacaoEnum;
import br.com.ufpi.model.Estudante;
import br.com.ufpi.model.ItemAtividade;
import br.com.ufpi.model.ItemListaEstudante;

/**
 * 
 * @author Paulo Sergio
 *
 */
@Named
@ViewScoped
public class RelatorioListasPorAluno implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EstudanteDao estudanteDao;
	
	@Inject
	private ItemListaEstudanteDao itemListaEstudanteDao;
	
	@Inject
	private ItemAtividadeDao itemAtividadeDao;
	
	private SituacaoEnum situacaoSelecionada;
	
	private Estudante estudante;
	
	private LazyDataModel<ItemListaEstudante> listas;
	
	private ItemListaEstudante listaSelecionada;
	
	private Set<ItemAtividade> atividades;

	public RelatorioListasPorAluno() {
		super();
		atividades = new HashSet<>();
	}
	
	public SituacaoEnum[] getSituacoes(){
		return SituacaoEnum.values();
	}
	
	
	public void pesquisar(){
		listas = new LazyDataModel<ItemListaEstudante>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<ItemListaEstudante> load(int first, int pageSize, List<SortMeta> multiSortMeta,
					Map<String, Object> filters) {

				List<ItemListaEstudante> listasRetornadas = itemListaEstudanteDao.buscarListas(estudante,situacaoSelecionada, first,
						pageSize, multiSortMeta);

				this.setRowCount(itemListaEstudanteDao.contarListas(estudante,situacaoSelecionada));
				return listasRetornadas;
			}

		};
	}
	
	public void limpar(){
		estudante = null;
		situacaoSelecionada = null;
	}
	
	public void doNothing(){
		
	}
	
	public void excluir(){
		itemListaEstudanteDao.delete(listaSelecionada);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, "Lista apagada com sucesso.",
				"Atividade apagada com sucesso."));
	}
	
	public void definirListaDetalhes(ItemListaEstudante item){
		this.listaSelecionada = item;
		
		if(listaSelecionada != null){
			atividades = itemAtividadeDao.carregarAtividades(item.getLista());
		}	
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

	public Estudante getEstudante() {
		return estudante;
	}

	public void setEstudante(Estudante estudante) {
		this.estudante = estudante;
	}

	public LazyDataModel<ItemListaEstudante> getListas() {
		return listas;
	}

	public void setListas(LazyDataModel<ItemListaEstudante> listas) {
		this.listas = listas;
	}

	public SituacaoEnum getSituacaoSelecionada() {
		return situacaoSelecionada;
	}

	public void setSituacaoSelecionada(SituacaoEnum situacaoSelecionada) {
		this.situacaoSelecionada = situacaoSelecionada;
	}

	public ItemListaEstudante getListaSelecionada() {
		return listaSelecionada;
	}

	public void setListaSelecionada(ItemListaEstudante listaSelecionada) {
		this.listaSelecionada = listaSelecionada;
	}

	public Set<ItemAtividade> getAtividades() {
		return atividades;
	}

	public void setAtividades(Set<ItemAtividade> atividades) {
		this.atividades = atividades;
	}
	
}
