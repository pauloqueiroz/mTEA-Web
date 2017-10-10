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

import br.com.ufpi.model.Atividade;
import br.com.ufpi.model.ItemAtividade;
import br.com.ufpi.model.ListaAtividade;

/**
 * 
 * @author Paulo Sergio
 *
 */
@Stateless
public class ItemAtividadeDao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private EntityManager em;

	public ItemAtividadeDao() {
		super();
	}

	public void atualizar(ItemAtividade itemAtividade) {
		em.merge(itemAtividade);
	}
	
	public Set<ItemAtividade> carregarAtividades(ListaAtividade lista){
		List<ItemAtividade> list = buscarAtividades(lista);
		if(!CollectionUtils.isEmpty(list))
			return new HashSet<>(list);
		return null;
	}
	
	public List<ItemAtividade> buscarAtividades(ListaAtividade lista){
		String hql = "Select i from ItemAtividade i where i.lista = :lista order by i.ordem";
		TypedQuery<ItemAtividade> query = em.createQuery(hql, ItemAtividade.class);
		query.setParameter("lista", lista);
		List<ItemAtividade> list = query.getResultList();
		if(!CollectionUtils.isEmpty(list)) {
			return list;
		}
			return null;
	}

	public void deletar(ItemAtividade atividade) {
		atividade = em.merge(atividade);
		em.remove(atividade);
		
	}

	public List<ItemAtividade> buscarAtividades(Atividade atividade) {
		String hql = "Select i from ItemAtividade i where i.atividade = :atividade";
		TypedQuery<ItemAtividade> query = em.createQuery(hql, ItemAtividade.class);
		query.setParameter("atividade", atividade);
		List<ItemAtividade> list = query.getResultList();
		if(!CollectionUtils.isEmpty(list)) {
			return list;
		}
			return null;
	}

}
