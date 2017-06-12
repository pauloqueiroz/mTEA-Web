package br.com.ufpi.controller;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ufpi.dao.EstudanteDao;
import br.com.ufpi.dao.ItemListaEstudanteDao;
import br.com.ufpi.model.Estudante;
import br.com.ufpi.model.ItemListaEstudante;

/**
 * 
 * @author Paulo Sergio
 *
 */
@Named
@ViewScoped
public class ListasDoEstudanteBean implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String idEstudante;
	
	@Inject
	private EstudanteDao estudanteDao;
	
	@Inject
	private ItemListaEstudanteDao itemListaEstudanteDao;
	
	private Estudante estudante;
	
	private Set<ItemListaEstudante> listas = new HashSet<>();
	
	@PostConstruct
	public void init(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, "Sucesso ao realizar operação.", null));
	}
	
	
	public void buscarEstudante() {
		System.out.println("estudante id" +idEstudante);
		if (idEstudante != null){
			estudante = estudanteDao.buscarPorId(Long.parseLong(idEstudante));
			listas = itemListaEstudanteDao.buscar(estudante,null);
		}
	}


	public Set<ItemListaEstudante> getListas() {
		return listas;
	}


	public void setListas(Set<ItemListaEstudante> listas) {
		this.listas = listas;
	}


	public String getIdEstudante() {
		return idEstudante;
	}


	public void setIdEstudante(String idEstudante) {
		this.idEstudante = idEstudante;
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

}
