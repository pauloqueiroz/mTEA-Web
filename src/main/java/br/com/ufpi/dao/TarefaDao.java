package br.com.ufpi.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.collections.CollectionUtils;

import br.com.ufpi.enuns.TemplateEnum;
import br.com.ufpi.model.Estudante;
import br.com.ufpi.model.ItemListaEstudante;
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
	
	public List<Tarefa> buscarTarefasPorEstudante(Estudante estudante, TemplateEnum template, int firstResult,
			int numeroDeRegistros) {
		String hql = "SELECT t FROM Tarefa t";
		if(estudante != null || template != null)
			hql += " where ";
		if(estudante != null)
			hql += "t.estudante = :estudante ";
		if(template != null)
			hql += "and t.atividade.template = :template ";
		hql += " ORDER by t.inicio desc";
		TypedQuery<Tarefa> query = em.createQuery(hql,
				Tarefa.class);
		if(estudante != null)
			query.setParameter("estudante", estudante);
		if(template != null)
			query.setParameter("template", template);
		query.setFirstResult(firstResult);
		query.setMaxResults(numeroDeRegistros);
		List<Tarefa> tarefasExecutadasPeloEstudante = query.getResultList();

		if (!CollectionUtils.isEmpty(tarefasExecutadasPeloEstudante)) {
			return tarefasExecutadasPeloEstudante;
		}
		return null;
	}
	
	public List<TarefaGrafico> buscarTarefasPorEstudante(Estudante estudante, TemplateEnum template) {
		String hql = "SELECT NEW br.com.ufpi.util.TarefaGrafico(t.acertos, t.erros, t.inicio) FROM Tarefa t";
		if(estudante != null || template != null)
			hql += " where ";
		if(estudante != null)
			hql += "t.estudante = :estudante ";
		if(template != null)
			hql += "and t.atividade.template = :template ";
		hql += " order by t.inicio";
		TypedQuery<TarefaGrafico> query = em.createQuery(hql,
				TarefaGrafico.class);
		if(estudante != null)
			query.setParameter("estudante", estudante);
		if(template != null)
			query.setParameter("template", template);
		List<TarefaGrafico> tarefasExecutadasPeloEstudante = query.getResultList();

		if (!CollectionUtils.isEmpty(tarefasExecutadasPeloEstudante)) {
			return tarefasExecutadasPeloEstudante;
		}
		return new ArrayList<>();
	}
	
	public int contarTarefas(Estudante estudante, TemplateEnum template) {
		String hql = "SELECT COUNT(t) FROM Tarefa t";
		if(estudante != null || template != null)
			hql += " where ";
		if(estudante != null)
			hql += "t.estudante = :estudante ";
		if(template != null)
			hql += "and t.atividade.template = :template ";
		TypedQuery<Number> query = em.createQuery(hql,Number.class);
		if(estudante != null)
			query.setParameter("estudante", estudante);
		if(template != null)
			query.setParameter("template", template);

		return query.getSingleResult().intValue();
	}

	public int contarTarefasPorLista(ItemListaEstudante lista){
		String hql = "SELECT COUNT(t) FROM Tarefa t WHERE t.lista = :lista";
		TypedQuery<Number> query = em.createQuery(hql,Number.class);
		query.setParameter("lista", lista);
		return query.getSingleResult().intValue();
	}

	
}
