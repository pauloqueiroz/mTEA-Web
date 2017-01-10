package br.com.ufpi.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.ufpi.model.Arquivo;

@Stateless
public class ArquivoDao implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager em;

	public ArquivoDao() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void adicionar(Arquivo arquivo) {
		em.merge(arquivo);
//		em.flush();
	}

	public void atualizar(Arquivo arquivo) {
		em.merge(arquivo);
	}
}
