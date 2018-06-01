package br.com.ufpi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author Paulo Sergio
 * 
 * Classe que representa uma lista de atividades previamente cadastradas.
 *
 */
@Entity
public class ListaAtividade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	
	private String descricao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private Set<ItemAtividade> atividades = new HashSet<>();
	
	@OneToOne
	@JoinColumn(name="idUsuario")
	private Usuario usuarioCriador;

	public ListaAtividade() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Set<ItemAtividade> getAtividades() {
		return atividades;
	}

	public void setAtividades(Set<ItemAtividade> atividades) {
		this.atividades = atividades;
	}

	public Usuario getUsuarioCriador() {
		return usuarioCriador;
	}

	public void setUsuarioCriador(Usuario usuarioCriador) {
		this.usuarioCriador = usuarioCriador;
	}
	
}
