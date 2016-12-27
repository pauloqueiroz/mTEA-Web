package br.com.ufpi.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import br.com.ufpi.model.Estudante;

/**
 * 
 * @author Paulo Sergio
 *
 */
@Stateless
public class EstudanteDao implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager em;
	

	public void adiciona(Estudante aluno) {
		em.persist(aluno);
	}

	public void atualiza(Estudante aluno) {
		em.merge(aluno);
	}
	
	public List<Estudante> listarEstudantes(String nomeEstudante, int first, int pageSize,
											List<SortMeta> multiSortMeta){
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		
		CriteriaQuery<Estudante> estudanteQuery = criteriaBuilder
				.createQuery(Estudante.class);
		Root<Estudante> estudanteRoot = estudanteQuery.from(Estudante.class);
		
		if (multiSortMeta != null) {
			for (SortMeta sortMeta : multiSortMeta) {
				if (sortMeta.getSortOrder() == SortOrder.ASCENDING) {
					if (sortMeta.getSortField().contains(".")) {
						String[] campos = sortMeta.getSortField().split("\\.");
						estudanteQuery.orderBy(criteriaBuilder
								.asc(estudanteRoot.get(campos[0])
										.get(campos[1])));
					} else {
						estudanteQuery
								.orderBy(criteriaBuilder.asc(estudanteRoot
										.get(sortMeta.getSortField())));
					}
				} else if (sortMeta.getSortOrder() == SortOrder.DESCENDING) {
					if (sortMeta.getSortField().contains(".")) {
						String[] campos = sortMeta.getSortField().split("\\.");
						estudanteQuery.orderBy(criteriaBuilder
								.desc(estudanteRoot.get(campos[0]).get(
										campos[1])));
					} else {
						estudanteQuery
								.orderBy(criteriaBuilder.desc(estudanteRoot
										.get(sortMeta.getSortField())));
					}
				}
			}
		}
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(nomeEstudante != null && !nomeEstudante.equals("")){
			Predicate nomePredicate = criteriaBuilder.like(
					estudanteRoot.<String> get("nome"),
					nomeEstudante);
			predicates.add(nomePredicate);
		}
		estudanteQuery.where(predicates.toArray(new Predicate[] {}));
		TypedQuery<Estudante> query = em.createQuery(estudanteQuery);

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		List<Estudante> lista = query.getResultList();

		return lista;
	}
	
	public int contarEstudantes() {

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> estudanteQuery = criteriaBuilder
				.createQuery(Long.class);
		Root<Estudante> estudanteRoot = estudanteQuery.from(Estudante.class);
		estudanteQuery.where();
		TypedQuery<Long> query = em.createQuery(estudanteQuery
				.select(criteriaBuilder.count(estudanteRoot)));

		return query.getSingleResult().intValue();
	}

}
