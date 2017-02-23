package br.com.ufpi.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.collections.CollectionUtils;

import br.com.ufpi.model.Estudante;
import br.com.ufpi.model.Tarefa;

@Stateless
public class TarefaDao implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager em;

	public TarefaDao() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void adicionar(Tarefa tarefa) {
		em.persist(tarefa);
	}

	public void atualizar(Tarefa tarefa) {
		em.merge(tarefa);
	}
	
	public List<Tarefa> buscarTarefasPorEstudante(Estudante estudante, int firstResult,
			int numeroDeRegistros) {
		String hql = "SELECT t FROM Tarefa t";
		if(estudante != null)
			hql += " where t.estudante = :estudante ";
		TypedQuery<Tarefa> query = em.createQuery(hql,
				Tarefa.class);
		if(estudante != null)
			query.setParameter("estudante", estudante);
		query.setFirstResult(firstResult);
		query.setMaxResults(numeroDeRegistros);
		List<Tarefa> tarefasExecutadasPeloEstudante = query.getResultList();

		if (!CollectionUtils.isEmpty(tarefasExecutadasPeloEstudante)) {
			return tarefasExecutadasPeloEstudante;
		}
		return null;
	}
	
	public int contarDocumentosPorStatusSetorProcessual(Estudante estudante) {
		String hql = "SELECT COUNT(t) FROM Tarefa t";
		if(estudante != null)
			hql += " where t.estudante = :estudante ";
		TypedQuery<Number> query = em.createQuery(hql,Number.class);
		if(estudante != null)
			query.setParameter("estudante", estudante);

		return query.getSingleResult().intValue();
	}


	
}
