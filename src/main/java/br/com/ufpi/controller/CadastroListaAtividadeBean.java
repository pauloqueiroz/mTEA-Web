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

import br.com.ufpi.dao.ArquivoDao;
import br.com.ufpi.dao.AtividadeDao;
import br.com.ufpi.enuns.TemplateEnum;
import br.com.ufpi.model.Arquivo;
import br.com.ufpi.model.Atividade;

@Named
@ViewScoped
public class CadastroListaAtividadeBean implements Serializable{

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
	
	@PostConstruct
	public void postConstructor() {
		imagens = new ArrayList<>();
		pesquisar();
	}
	
	public void pesquisar() {
		setAtividades(new LazyDataModel<Atividade>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Atividade> load(int first, int pageSize,
					List<SortMeta> multiSortMeta, Map<String, Object> filters) {
				List<Atividade> listaAtividades = new ArrayList<>();

				listaAtividades = atividadeDao
						.listarAtividades(getNomeAtividade(), templateSelecionado, getPalavra(), first, pageSize, multiSortMeta);
				
				this.setRowCount(atividadeDao
						.contarAtividades(getNomeAtividade(), templateSelecionado, getPalavra()));
				
				return listaAtividades;
			}

		});
		
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

}
