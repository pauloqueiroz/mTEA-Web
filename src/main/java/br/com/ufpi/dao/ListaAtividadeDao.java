package br.com.ufpi.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.ufpi.model.ListaAtividade;

/**
 * 
 * @author Paulo Sergio
 *
 */
@Stateless
public class ListaAtividadeDao implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager em;

	public ListaAtividadeDao() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void adicionar(ListaAtividade lista) {
		em.persist(lista);
	}

	public void atualizar(ListaAtividade lista) {
		em.merge(lista);
	}
}
