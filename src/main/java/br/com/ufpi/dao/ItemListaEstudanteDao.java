package br.com.ufpi.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import br.com.ufpi.enuns.SituacaoEnum;
import br.com.ufpi.model.Estudante;
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
	
	public void salvar(ItemListaEstudante item){
		em.persist(item);
	}

	public Set<ItemListaEstudante> buscar(Estudante estudante) {
		String hql = "Select i from ItemListaEstudante i where i.estudante = :estudante";
		TypedQuery<ItemListaEstudante> query = em.createQuery(hql, ItemListaEstudante.class);
		query.setParameter("estudante", estudante);
		return new HashSet<>(query.getResultList());
	}

	public List<ItemListaEstudante> buscarListas(Estudante estudante, SituacaoEnum situacao, int first, int pageSize, List<SortMeta> multiSortMeta) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<ItemListaEstudante> itemQuery = criteriaBuilder.createQuery(ItemListaEstudante.class);
		Root<ItemListaEstudante> itemRoot = itemQuery.from(ItemListaEstudante.class);

		if (multiSortMeta != null) {
			for (SortMeta sortMeta : multiSortMeta) {
				if (sortMeta.getSortOrder() == SortOrder.ASCENDING) {
					if (sortMeta.getSortField().contains(".")) {
						String[] campos = sortMeta.getSortField().split("\\.");
						itemQuery.orderBy(criteriaBuilder.asc(itemRoot.get(campos[0]).get(campos[1])));
					} else {
						itemQuery.orderBy(criteriaBuilder.asc(itemRoot.get(sortMeta.getSortField())));
					}
				} else if (sortMeta.getSortOrder() == SortOrder.DESCENDING) {
					if (sortMeta.getSortField().contains(".")) {
						String[] campos = sortMeta.getSortField().split("\\.");
						itemQuery.orderBy(criteriaBuilder.desc(itemRoot.get(campos[0]).get(campos[1])));
					} else {
						itemQuery.orderBy(criteriaBuilder.desc(itemRoot.get(sortMeta.getSortField())));
					}
				}
			}
		}
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (estudante != null) {
			Predicate estudantePredicate = criteriaBuilder.equal(itemRoot.<Estudante> get("estudante"),estudante);
			predicates.add(estudantePredicate);
		}
		if(situacao != null){
			Predicate tipoSelecionadoPredicate = criteriaBuilder.equal(itemRoot.<SituacaoEnum> get("situacao"),
					situacao);
			predicates.add(tipoSelecionadoPredicate);
		}
		if (!CollectionUtils.isEmpty(predicates))
			itemQuery.where(predicates.toArray(new Predicate[] {}));
		TypedQuery<ItemListaEstudante> query = em.createQuery(itemQuery);
		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		List<ItemListaEstudante> lista = query.getResultList();

		return lista;
	}

	public int contarListas(Estudante estudante, SituacaoEnum situacao) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<Long> itemQuery = criteriaBuilder.createQuery(Long.class);
		Root<ItemListaEstudante> itemRoot = itemQuery.from(ItemListaEstudante.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (estudante != null) {
			Predicate estudantePredicate = criteriaBuilder.equal(itemRoot.<Estudante> get("estudante"),estudante);
			predicates.add(estudantePredicate);
		}
		if(situacao != null){
			Predicate tipoSelecionadoPredicate = criteriaBuilder.equal(itemRoot.<SituacaoEnum> get("situacao"),
					situacao);
			predicates.add(tipoSelecionadoPredicate);
		}
		if (!CollectionUtils.isEmpty(predicates))
			itemQuery.where(predicates.toArray(new Predicate[] {}));
		
		TypedQuery<Long> query = em.createQuery(itemQuery.select(criteriaBuilder.count(itemRoot)));

		return query.getSingleResult().intValue();
	}

}
