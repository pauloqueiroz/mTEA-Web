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
		// TODO Auto-generated constructor stub
	}

	public void atualizar(ItemAtividade itemAtividade) {
		em.merge(itemAtividade);
	}
	
	public Set<ItemAtividade> carregarAtividades(ListaAtividade lista){
		String hql = "Select i from ItemAtividade i where i.lista = :lista";
		TypedQuery<ItemAtividade> query = em.createQuery(hql, ItemAtividade.class);
		query.setParameter("lista", lista);
		List<ItemAtividade> list = query.getResultList();
		if(!CollectionUtils.isEmpty(list))
			return new HashSet<>(list);
		return null;
	}

}
