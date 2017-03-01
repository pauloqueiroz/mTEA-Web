package br.com.ufpi.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.collections.CollectionUtils;

import br.com.ufpi.model.Tarefa;
import br.com.ufpi.util.TarefaGrafico;

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
	
	public List<Tarefa> buscarTarefasPorEstudante(Long idEstudante, int firstResult,
			int numeroDeRegistros) {
		String hql = "SELECT t FROM Tarefa t";
		if(idEstudante != null)
			hql += " where t.estudante.id = :idEstudante ";
		TypedQuery<Tarefa> query = em.createQuery(hql,
				Tarefa.class);
		if(idEstudante != null)
			query.setParameter("idEstudante", idEstudante);
		query.setFirstResult(firstResult);
		query.setMaxResults(numeroDeRegistros);
		List<Tarefa> tarefasExecutadasPeloEstudante = query.getResultList();

		if (!CollectionUtils.isEmpty(tarefasExecutadasPeloEstudante)) {
			return tarefasExecutadasPeloEstudante;
		}
		return null;
	}
	
	public List<TarefaGrafico> buscarTarefasPorEstudante(Long idEstudante) {
		String hql = "SELECT NEW br.com.ufpi.util.TarefaGrafico(t.acertos, t.erros, t.inicio) FROM Tarefa t";
		if(idEstudante != null)
			hql += " where t.estudante.id = :idEstudante";
		hql += " order by t.inicio";
		TypedQuery<TarefaGrafico> query = em.createQuery(hql,
				TarefaGrafico.class);
		if(idEstudante != null)
			query.setParameter("idEstudante", idEstudante);
		List<TarefaGrafico> tarefasExecutadasPeloEstudante = query.getResultList();

		if (!CollectionUtils.isEmpty(tarefasExecutadasPeloEstudante)) {
			return tarefasExecutadasPeloEstudante;
		}
		return new ArrayList<>();
	}
	
	public int contarDocumentosPorStatusSetorProcessual(Long idEstudante) {
		String hql = "SELECT COUNT(t) FROM Tarefa t";
		if(idEstudante != null)
			hql += " where t.estudante.id = :idEstudante";
		TypedQuery<Number> query = em.createQuery(hql,Number.class);
		if(idEstudante != null)
			query.setParameter("idEstudante", idEstudante);

		return query.getSingleResult().intValue();
	}


	
}
