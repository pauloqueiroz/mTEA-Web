package br.com.ufpi.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.ufpi.model.ItemListaEstudante;

/**
 * 
 * @author Paulo Sergio
 *
 */
@Stateless
public class ItemListaEstudanteDao implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager em;

	public ItemListaEstudanteDao() {
		super();
	}

	public void atualizar(ItemListaEstudante item) {
		em.merge(item);
	}

}
