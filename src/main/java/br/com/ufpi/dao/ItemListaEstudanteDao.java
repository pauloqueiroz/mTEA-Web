package br.com.ufpi.dao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.collections.CollectionUtils;

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

	public Set<ItemListaEstudante> buscar(Estudante estudante, List<SituacaoEnum> situacoes) {
		String hql = "Select i from ItemListaEstudante i where i.estudante = :estudante";
		if(!CollectionUtils.isEmpty(situacoes)){
			hql += "i.situacao in (:situacoes)";
		}
		TypedQuery<ItemListaEstudante> query = em.createQuery(hql, ItemListaEstudante.class);
		query.setParameter("estudante", estudante);
		if(!CollectionUtils.isEmpty(situacoes)){
			query.setParameter("situacoes", situacoes);
		}
		return new HashSet<>(query.getResultList());
	}

}
