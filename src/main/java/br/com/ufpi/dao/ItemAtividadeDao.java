package br.com.ufpi.dao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
		em.flush();
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
		Query query = em.createNativeQuery("DELETE FROM listaatividade_itematividade WHERE atividades_id = :idAtividadeExcluida");
		query.setParameter("idAtividadeExcluida", atividade.getId());
		query.executeUpdate();
		if (em.contains(atividade)) {
	        em.remove(atividade);
	    } else {
	        ItemAtividade ee = em.getReference(atividade.getClass(), atividade.getId());
	        em.remove(ee);
	    }
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
