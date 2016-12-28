package br.com.ufpi.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.ufpi.model.Atividade;
/**
 * 
 * @author Paulo Sergio
 *
 */
@Stateless
public class AtividadeDao implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager em;

	public AtividadeDao() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void adicionar(Atividade atividade) {
		em.persist(atividade);
	}

	public void atualizar(Atividade atividade) {
		em.merge(atividade);
	}

}
