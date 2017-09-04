package br.com.ufpi.controller;

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

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import br.com.ufpi.dao.EstudanteDao;
import br.com.ufpi.dao.ItemListaEstudanteDao;
import br.com.ufpi.enuns.SituacaoEnum;
import br.com.ufpi.model.Estudante;
import br.com.ufpi.model.ItemListaAtividade;
import br.com.ufpi.model.ItemListaEstudante;

@Named
@ViewScoped
public class ListarAlunoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EstudanteDao estudanteDao;
	
	private LazyDataModel<Estudante> estudantes;
	
	private String nomeEstudante;
	
	private Estudante estudanteSelecionado;
	
	@Inject
	private ItemListaEstudanteDao itemListaEstudanteDao;

	public ListarAlunoBean() {
		super();
	}

	@PostConstruct
	public void postConstructor() {

		pesquisar();
	}

	
	public void pesquisar() {
		estudantes = new LazyDataModel<Estudante>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Estudante> load(int first, int pageSize,
					List<SortMeta> multiSortMeta, Map<String, Object> filters) {
				List<Estudante> listaEstudantes = new ArrayList<>();

				listaEstudantes = estudanteDao
						.listarEstudantes(nomeEstudante, first, pageSize, multiSortMeta);
				
				this.setRowCount(estudanteDao
						.contarEstudantes(nomeEstudante));
				
				return listaEstudantes;
			}

		};
		
	}
	
	/**
	 * Limpa os dados de pesquisa.
	 */
	public void limparInformacoes() {
		nomeEstudante = "";
		pesquisar();
	}
	
	public void definirEstudante(Estudante estudante) {
		if(estudante != null) {
			estudanteSelecionado = estudante;
		}
	}
	
	public void inativar() {
		if(estudanteSelecionado != null) {
			estudanteSelecionado.setSituacao(SituacaoEnum.INATIVO);
			atualizarEstudante();
		}
	}
	
	public void ativar() {
		if(estudanteSelecionado != null) {
			estudanteSelecionado.setSituacao(SituacaoEnum.ATIVO);
			atualizarEstudante();
		}
	}
	
	public void excluir() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if(estudanteSelecionado != null) {
			estudanteSelecionado = estudanteDao.atualizar(estudanteSelecionado);
			for(ItemListaEstudante item: itemListaEstudanteDao.buscar(estudanteSelecionado)) {
				System.out.println("deletar item lista estudante");
				itemListaEstudanteDao.delete(item);
			}
			estudanteDao.remove(estudanteSelecionado);
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Aluno apagado com sucesso.",
					null));
		}
	}
	
	public void atualizarEstudante() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if(estudanteSelecionado != null) {
			estudanteDao.atualizar(estudanteSelecionado);
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Operação realizada com sucesso.",
					null));
		}else {
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Selecione o aluno.",
					null));
		}
	}

	public EstudanteDao getEstudanteDao() {
		return estudanteDao;
	}

	public void setEstudanteDao(EstudanteDao estudanteDao) {
		this.estudanteDao = estudanteDao;
	}

	public LazyDataModel<Estudante> getEstudantes() {
		return estudantes;
	}

	public void setEstudantes(LazyDataModel<Estudante> estudantes) {
		this.estudantes = estudantes;
	}

	public String getNomeEstudante() {
		return nomeEstudante;
	}

	public void setNomeEstudante(String nomeEstudante) {
		this.nomeEstudante = nomeEstudante;
	}

	public Estudante getEstudanteSelecionado() {
		return estudanteSelecionado;
	}

	public void setEstudanteSelecionado(Estudante estudanteSelecionado) {
		this.estudanteSelecionado = estudanteSelecionado;
	}
	
	

}
