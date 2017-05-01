package br.com.ufpi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import br.com.ufpi.dao.EstudanteDao;
import br.com.ufpi.model.Estudante;

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
	
	

}
