package br.com.ufpi.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import br.com.ufpi.model.ListaAtividade;
import br.com.ufpi.model.Usuario;
import br.com.ufpi.util.EstudanteUtils;

/**
 * 
 * @author Paulo Sergio
 *
 */
@Stateless
public class ListaAtividadeDao implements Serializable {

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

	public int buscarListaPorNome(String nome, Usuario usuario) {
		String hql = "Select count(l.id) from ListaAtividade l where l.nome = :nome and l.usuarioCriador = :usuario";
		TypedQuery<Long> query = em.createQuery(hql, Long.class);
		query.setParameter("nome", nome);
		query.setParameter("usuario", usuario);
		return query.getSingleResult().intValue();
	}

	public void adicionar(ListaAtividade lista) {
		em.persist(lista);
	}

	public void atualizar(ListaAtividade lista) {
		em.merge(lista);
	}

	public ListaAtividade buscarPorId(long id) {
		TypedQuery<ListaAtividade> query = em.createQuery("select lista from ListaAtividade lista where lista.id = :id",
				ListaAtividade.class);

		query.setParameter("id", id);

		List<ListaAtividade> listaAtividade = query.getResultList();

		if (listaAtividade == null || listaAtividade.isEmpty()) {
			return null;
		}

		return listaAtividade.get(0);
	}

	public List<ListaAtividade> listar(String nomeListaAtividade, String descricao, Usuario usuario, Date dataInicial, Date dataFinal,
			Set<Long> idsListasSelecionadas, int first, int pageSize, List<SortMeta> multiSortMeta) {

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<ListaAtividade> listaQuery = criteriaBuilder.createQuery(ListaAtividade.class);
		Root<ListaAtividade> listaRoot = listaQuery.from(ListaAtividade.class);

		if (multiSortMeta != null) {
			for (SortMeta sortMeta : multiSortMeta) {
				if (sortMeta.getSortOrder() == SortOrder.ASCENDING) {
					if (sortMeta.getSortField().contains(".")) {
						String[] campos = sortMeta.getSortField().split("\\.");
						listaQuery.orderBy(criteriaBuilder.asc(listaRoot.get(campos[0]).get(campos[1])));
					} else {
						listaQuery.orderBy(criteriaBuilder.asc(listaRoot.get(sortMeta.getSortField())));
					}
				} else if (sortMeta.getSortOrder() == SortOrder.DESCENDING) {
					if (sortMeta.getSortField().contains(".")) {
						String[] campos = sortMeta.getSortField().split("\\.");
						listaQuery.orderBy(criteriaBuilder.desc(listaRoot.get(campos[0]).get(campos[1])));
					} else {
						listaQuery.orderBy(criteriaBuilder.desc(listaRoot.get(sortMeta.getSortField())));
					}
				}
			}
		}

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (!StringUtils.isEmpty(nomeListaAtividade)) {
			Predicate nomePredicate = criteriaBuilder.like(criteriaBuilder.lower(listaRoot.<String> get("nome")),
					"%" + nomeListaAtividade.toLowerCase() + "%");
			predicates.add(nomePredicate);
		}

		if (!StringUtils.isEmpty(descricao)) {
			Predicate descricaoPredicate = criteriaBuilder.like(
					criteriaBuilder.lower(listaRoot.<String> get("descricao")), "%" + descricao.toLowerCase() + "%");
			predicates.add(descricaoPredicate);
		}
		
		if(usuario != null) {
			Predicate usuarioPredicate = criteriaBuilder.equal(listaRoot.<Usuario> get("usuarioCriador"),
					usuario);
			predicates.add(usuarioPredicate);
		}

		Date dataF = null, dataI = null;
		if (dataInicial != null) {
			dataI = EstudanteUtils.beginDateToCalendar(dataInicial).getTime();
		}
		if (dataFinal != null) {
			dataF = EstudanteUtils.endDateToCalendar(dataFinal).getTime();
		}

		if (dataInicial != null || dataFinal != null) {
			if (dataFinal != null && dataInicial != null) {
				Predicate dataEnvioInicioFimPredicate = criteriaBuilder
						.between(listaRoot.<Date> get("dataCriacao"), dataI, dataF);
				predicates.add(dataEnvioInicioFimPredicate);
			} else if (dataInicial != null && dataFinal == null) {
				Predicate dataEnvioInicioPredicate = criteriaBuilder
						.greaterThanOrEqualTo(listaRoot.<Date> get("dataCriacao"), dataI);
				predicates.add(dataEnvioInicioPredicate);
			} else if (dataInicial == null && dataFinal != null) {
				Predicate dataEnvioFimPredicate = criteriaBuilder
						.lessThanOrEqualTo(listaRoot.<Date> get("dataCriacao"), dataF);
				predicates.add(dataEnvioFimPredicate);
			}
		}
		
		if(!CollectionUtils.isEmpty(idsListasSelecionadas)){
			Predicate idsPredicate = criteriaBuilder.not(listaRoot.<String> get("id").in(idsListasSelecionadas));
			predicates.add(idsPredicate);
		}
		
		if (predicates.size() > 0) {
            listaQuery.where(predicates.toArray(new Predicate[] {}));
        }
        TypedQuery<ListaAtividade> query = em.createQuery(listaQuery);
 
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
 
        List<ListaAtividade> result = query.getResultList();
 
        if (result == null | result.size() == 0) {
            return null;
        }
 
        return result;

	}

	public int contar(String nomeListaAtividade, String descricao, Usuario usuario, Date dataInicial, Date dataFinal, Set<Long> idsListasSelecionadas) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<ListaAtividade> listaRoot = countQuery.from(ListaAtividade.class);
 
        List<Predicate> predicates = new ArrayList<Predicate>();
 
        if (!StringUtils.isEmpty(nomeListaAtividade)) {
			Predicate nomePredicate = criteriaBuilder.like(criteriaBuilder.lower(listaRoot.<String> get("nome")),
					"%" + nomeListaAtividade.toLowerCase() + "%");
			predicates.add(nomePredicate);
		}

		if (!StringUtils.isEmpty(descricao)) {
			Predicate descricaoPredicate = criteriaBuilder.like(
					criteriaBuilder.lower(listaRoot.<String> get("descricao")), "%" + descricao.toLowerCase() + "%");
			predicates.add(descricaoPredicate);
		}
		
		if(usuario != null) {
			Predicate usuarioPredicate = criteriaBuilder.equal(listaRoot.<Usuario> get("usuarioCriador"),
					usuario);
			predicates.add(usuarioPredicate);
		}

		Date dataF = null, dataI = null;
		if (dataInicial != null) {
			dataI = EstudanteUtils.beginDateToCalendar(dataInicial).getTime();
		}
		if (dataFinal != null) {
			dataF = EstudanteUtils.endDateToCalendar(dataFinal).getTime();
		}

		if (dataInicial != null || dataFinal != null) {
			if (dataFinal != null && dataInicial != null) {
				Predicate dataEnvioInicioFimPredicate = criteriaBuilder
						.between(listaRoot.<Date> get("dataCriacao"), dataI, dataF);
				predicates.add(dataEnvioInicioFimPredicate);
			} else if (dataInicial != null && dataFinal == null) {
				Predicate dataEnvioInicioPredicate = criteriaBuilder
						.greaterThanOrEqualTo(listaRoot.<Date> get("dataCriacao"), dataI);
				predicates.add(dataEnvioInicioPredicate);
			} else if (dataInicial == null && dataFinal != null) {
				Predicate dataEnvioFimPredicate = criteriaBuilder
						.lessThanOrEqualTo(listaRoot.<Date> get("dataCriacao"), dataF);
				predicates.add(dataEnvioFimPredicate);
			}
		}
		
		if(!CollectionUtils.isEmpty(idsListasSelecionadas)){
			Predicate idsPredicate = criteriaBuilder.not(listaRoot.<String> get("id").in(idsListasSelecionadas));
			predicates.add(idsPredicate);
		}
 
        if (predicates.size() > 0) {
            countQuery.where(predicates.toArray(new Predicate[] {}));
        }
        TypedQuery<Long> query = em.createQuery(countQuery.select(criteriaBuilder.count(listaRoot)));
 
        return query.getSingleResult().intValue();
	}

	public void delete(ListaAtividade listaSelecionada) {
		if (em.contains(listaSelecionada)) {
	        em.remove(listaSelecionada);
	    } else {
	    	ListaAtividade lista = em.getReference(listaSelecionada.getClass(), listaSelecionada.getId());
	        em.remove(lista);
	    }
		
	}
}
