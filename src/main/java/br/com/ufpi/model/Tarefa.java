package br.com.ufpi.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Tarefa implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private Date inicio;
	
	private Date fim;
	
	private Integer toques;
	
	private Integer acertos;
	
	private Integer erros;
	
	private Boolean encerrada;
	
	@ManyToOne
	@JoinColumn(name="idAtividade")
	private Atividade atividade;
	
	@ManyToOne
	@JoinColumn(name="idEstudante")
	private Estudante estudante;
	
	private transient String tarefaEncerrada;
	
	public Tarefa() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Tarefa(Long id, Date inicio, Date fim, Integer toques, Integer acertos, Integer erros, Boolean encerrada,
			Atividade atividade) {
		super();
		this.id = id;
		this.inicio = inicio;
		this.fim = fim;
		this.toques = toques;
		this.acertos = acertos;
		this.erros = erros;
		this.encerrada = encerrada;
		this.atividade = atividade;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public Integer getToques() {
		return toques;
	}

	public void setToques(Integer toques) {
		this.toques = toques;
	}

	public Integer getAcertos() {
		return acertos;
	}

	public void setAcertos(Integer acertos) {
		this.acertos = acertos;
	}

	public Integer getErros() {
		return erros;
	}

	public void setErros(Integer erros) {
		this.erros = erros;
	}

	public Boolean getEncerrada() {
		return encerrada;
	}

	public void setEncerrada(Boolean encerrada) {
		this.encerrada = encerrada;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public Estudante getEstudante() {
		return estudante;
	}

	public void setEstudante(Estudante estudante) {
		this.estudante = estudante;
	}

	public String getTarefaEncerrada() {
		if(this.encerrada == null){
			return "SIM";
		}else if(this.encerrada)
			return "SIM";
		else
			return "NÃO";
	}

	public void setTarefaEncerrada(String tarefaEncerrada) {
		this.tarefaEncerrada = tarefaEncerrada;
	}

}
