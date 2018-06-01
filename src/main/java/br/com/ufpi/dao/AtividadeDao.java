package br.com.ufpi.dao;

import java.io.Serializable;
import java.util.ArrayList;
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

import br.com.ufpi.enuns.TemplateEnum;
import br.com.ufpi.model.Atividade;
import br.com.ufpi.model.Estudante;
import br.com.ufpi.model.Usuario;
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
	}
	
	public List<Atividade> buscarAtividade(String estudanteAtividade){
		TypedQuery<Atividade> query = em.createQuery("Select a from Atividade a where upper(a.estudanteTemplate) like:estudanteTemplate", Atividade.class);
		query.setParameter("estudanteTemplate", "%"+estudanteAtividade.toUpperCase()+"%");
		List<Atividade> estudantes = query.getResultList();
		return estudantes;
	}
	
	public void adicionar(Atividade atividade) {
		em.persist(atividade);
	}

	public void atualizar(Atividade atividade) {
		em.merge(atividade);
	}
	
	public Atividade buscarPorId(Long id){
		TypedQuery<Atividade> query = em.createQuery("Select a from Atividade a where a.id = :idAtividade", Atividade.class);
		query.setParameter("idAtividade", id);
		return query.getSingleResult();
	}

	public List<Atividade> carregarAtividadesDoEstudante(Estudante estudante) {
		TypedQuery<Atividade> query = em.createQuery("Select a from Atividade a where a.estudante = :estudante",
				Atividade.class);
		query.setParameter("estudante", estudante);
		List<Atividade> atividades = query.getResultList();
		return atividades;
	}
	
	/**
	 * Carrega os arquivos da atividade.
	 * 
	 * @param id
	 * @return
	 * 
	 */
	public Atividade carregarAtividadeComArquivos(Long id) {
		TypedQuery<Atividade> query = em.createQuery(
				"select atividade from Atividade atividade where atividade.id = :id",
				Atividade.class);

		query.setParameter("id", id);

		List<Atividade> listaAtividade = query.getResultList();

		if (listaAtividade == null || listaAtividade.isEmpty()) {
			return null;
		}
		

		return listaAtividade.get(0);
	}
	
	public List<Atividade> listarAtividades(String nomeAtividade, TemplateEnum templateSelecionado, String palavra, Usuario usuario, Set<Long> idsAtividadesExcluidas, int first, int pageSize,
			List<SortMeta> multiSortMeta) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<Atividade> atividadeQuery = criteriaBuilder.createQuery(Atividade.class);
		Root<Atividade> atividadeRoot = atividadeQuery.from(Atividade.class);

		if (multiSortMeta != null) {
			for (SortMeta sortMeta : multiSortMeta) {
				if (sortMeta.getSortOrder() == SortOrder.ASCENDING) {
					if (sortMeta.getSortField().contains(".")) {
						String[] campos = sortMeta.getSortField().split("\\.");
						atividadeQuery.orderBy(criteriaBuilder.asc(atividadeRoot.get(campos[0]).get(campos[1])));
					} else {
						atividadeQuery.orderBy(criteriaBuilder.asc(atividadeRoot.get(sortMeta.getSortField())));
					}
				} else if (sortMeta.getSortOrder() == SortOrder.DESCENDING) {
					if (sortMeta.getSortField().contains(".")) {
						String[] campos = sortMeta.getSortField().split("\\.");
						atividadeQuery.orderBy(criteriaBuilder.desc(atividadeRoot.get(campos[0]).get(campos[1])));
					} else {
						atividadeQuery.orderBy(criteriaBuilder.desc(atividadeRoot.get(sortMeta.getSortField())));
					}
				}
			}
		}
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (!StringUtils.isEmpty(nomeAtividade)) {
			Predicate palavraPredicate = criteriaBuilder.like(
					criteriaBuilder.lower(atividadeRoot.<String> get("nome")), "%" + nomeAtividade.toLowerCase() + "%");
			predicates.add(palavraPredicate);
		}
		
		if (templateSelecionado != null) {
			Predicate tipoSelecionadoPredicate = criteriaBuilder.equal(atividadeRoot.<TemplateEnum> get("template"),
					templateSelecionado);
			predicates.add(tipoSelecionadoPredicate);
		}

		if (!StringUtils.isEmpty(palavra)) {
			Predicate palavraPredicate = criteriaBuilder.like(
					criteriaBuilder.lower(atividadeRoot.<String> get("palavra")), "%" + palavra.toLowerCase() + "%");
			predicates.add(palavraPredicate);
		}
		
		if(usuario != null) {
			Predicate usuarioPredicate = criteriaBuilder.equal(atividadeRoot.<Usuario> get("usuarioCriador"),
					usuario);
			predicates.add(usuarioPredicate);
		}
		
		if(!CollectionUtils.isEmpty(idsAtividadesExcluidas)){
			Predicate idsPredicate = criteriaBuilder.not(atividadeRoot.<String> get("id").in(idsAtividadesExcluidas));
			predicates.add(idsPredicate);
		}
		
		if (!CollectionUtils.isEmpty(predicates))
			atividadeQuery.where(predicates.toArray(new Predicate[] {}));
		TypedQuery<Atividade> query = em.createQuery(atividadeQuery);
		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		List<Atividade> lista = query.getResultList();

		return lista;
	}

	public void delete(Atividade atividadeSelecionada) {
		atividadeSelecionada = em.merge(atividadeSelecionada);
		em.remove(atividadeSelecionada);
	}

	public int contarAtividades(String nomeAtividade, TemplateEnum templateSelecionado, String palavra, Usuario usuario, Set<Long> idsAtividadesExcluidas) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> atividadeQuery = criteriaBuilder.createQuery(Long.class);
		Root<Atividade> atividadeRoot = atividadeQuery.from(Atividade.class);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (!StringUtils.isEmpty(nomeAtividade)) {
			Predicate palavraPredicate = criteriaBuilder.like(
					criteriaBuilder.lower(atividadeRoot.<String> get("nome")), "%" + nomeAtividade.toLowerCase() + "%");
			predicates.add(palavraPredicate);
		}
		
		if (templateSelecionado != null) {
			Predicate tipoSelecionadoPredicate = criteriaBuilder.equal(atividadeRoot.<TemplateEnum> get("template"),
					templateSelecionado);
			predicates.add(tipoSelecionadoPredicate);
		}

		if (!StringUtils.isEmpty(palavra)) {
			Predicate palavraPredicate = criteriaBuilder.like(
					criteriaBuilder.lower(atividadeRoot.<String> get("palavra")), "%" + palavra.toLowerCase() + "%");
			predicates.add(palavraPredicate);
		}
		
		if(usuario != null) {
			Predicate usuarioPredicate = criteriaBuilder.equal(atividadeRoot.<Usuario> get("usuarioCriador"),
					usuario);
			predicates.add(usuarioPredicate);
		}
		
		if(!CollectionUtils.isEmpty(idsAtividadesExcluidas)){
			Predicate idsPredicate = criteriaBuilder.not(atividadeRoot.<String> get("id").in(idsAtividadesExcluidas));
			predicates.add(idsPredicate);
		}
		
		if (!CollectionUtils.isEmpty(predicates))
			atividadeQuery.where(predicates.toArray(new Predicate[] {}));
		
		TypedQuery<Long> query = em.createQuery(atividadeQuery.select(criteriaBuilder.count(atividadeRoot)));

		return query.getSingleResult().intValue();
		
	}
}
