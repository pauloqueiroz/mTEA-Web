package br.com.ufpi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.ufpi.enuns.TemplateEnum;

/**
 * 
 * @author Paulo Sergio
 *
 */
@Entity
public class Atividade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private TemplateEnum template;

	private String palavra;

	@OneToMany(mappedBy = "atividade", cascade = { CascadeType.MERGE, CascadeType.REMOVE })
	private List<Arquivo> imagens;

	@OneToMany(mappedBy = "atividade", cascade = CascadeType.REMOVE)
	private List<Tarefa> tarefas;

	private String estudanteTemplate;

	private String reforco;

	/*
	 * Atributo novo para armazenar o nome da atividade, que sera utilizado nas
	 * buscas das montagens das listas de atividades.
	 */
	private String nome;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;

	public Atividade() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TemplateEnum getTemplate() {
		return template;
	}

	public void setTemplate(TemplateEnum template) {
		this.template = template;
	}

	public String getPalavra() {
		return palavra;
	}

	public void setPalavra(String palavra) {
		this.palavra = palavra;
	}

	public List<Arquivo> getImagens() {
		return imagens;
	}

	public void setImagens(List<Arquivo> imagens) {
		this.imagens = imagens;
	}

	public String getTemplateToString() {
		return template.toString();
	}

	public List<Tarefa> getTarefas() {
		return tarefas;
	}

	public void setTarefas(List<Tarefa> tarefas) {
		this.tarefas = tarefas;
	}

	public String getEstudanteTemplate() {
		return estudanteTemplate;
	}

	public void setEstudanteTemplate(String estudanteTemplate) {
		this.estudanteTemplate = estudanteTemplate;
	}

	public String getReforco() {
		return reforco;
	}

	public void setReforco(String reforco) {
		this.reforco = reforco;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

}
