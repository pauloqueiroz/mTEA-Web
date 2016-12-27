package br.com.ufpi.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.ufpi.model.Usuario;
import br.com.ufpi.util.Md5Utils;

/**
 * 
 * @author Paulo Sergio
 *
 */
@Stateless
public class UsuarioDao implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager em;
	
	public Usuario buscarUsuario(String email, String senha) {
		TypedQuery<Usuario> query = em
				.createQuery(
						"SELECT u FROM Usuario u WHERE u.login = :login AND u.senha = :senha",
						Usuario.class);
		query.setParameter("login", email);
		query.setParameter("senha", Md5Utils.convertStringToMd5(senha));

		List<Usuario> usuarios = query.getResultList();

		if (usuarios.size() == 1)
			return usuarios.get(0);

		return null;
	}

	public Usuario buscaUsuarioPorUser(String login) {
		TypedQuery<Usuario> query = em.createQuery(
				"SELECT user FROM Usuario user WHERE user.login = :login",
				Usuario.class);
		query.setParameter("login", login);
		List<Usuario> usuarios = query.getResultList();

		if (usuarios.size() == 1)
			return usuarios.get(0);

		return null;
	}

	public void adiciona(Usuario usuario) {
		em.persist(usuario);
	}

	public void atualiza(Usuario usuario) {
		em.merge(usuario);
	}

}
