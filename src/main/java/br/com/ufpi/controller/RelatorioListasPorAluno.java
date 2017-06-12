package br.com.ufpi.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

import br.com.ufpi.dao.EstudanteDao;
import br.com.ufpi.model.Estudante;
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

	private String nomeEstudante;
	
	private Estudante estudante;
	
	private LazyDataModel<ItemListaEstudante> listas;
	
}
