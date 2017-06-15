package br.com.ufpi.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.ufpi.enuns.SituacaoEnum;

/**
 * 
 * @author Paulo Sergio
 *
 */
@Entity
public class ItemListaEstudante implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch=FetchType.EAGER)
	private ListaAtividade lista;
	
	@ManyToOne
	private Estudante estudante;
	
	@Enumerated(EnumType.STRING)
	private SituacaoEnum situacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataExecucao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAlteracao;

	public ItemListaEstudante() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ListaAtividade getLista() {
		return lista;
	}

	public void setLista(ListaAtividade lista) {
		this.lista = lista;
	}

	public Estudante getEstudante() {
		return estudante;
	}

	public void setEstudante(Estudante estudante) {
		this.estudante = estudante;
	}

	public SituacaoEnum getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoEnum situacao) {
		this.situacao = situacao;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Date getDataExecucao() {
		return dataExecucao;
	}

	public void setDataExecucao(Date dataExecucao) {
		this.dataExecucao = dataExecucao;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public boolean isPodeSerExcluido() {
		boolean naoEnviado = this.situacao.equals(SituacaoEnum.CADASTRADO);
		System.out.println("Pode ser excluido:" +naoEnviado);
		return naoEnviado?true:false;
	}
	
}
