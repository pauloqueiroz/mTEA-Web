package br.com.ufpi.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import br.com.ufpi.enuns.TemplateEnum;

/**
 * 
 * @author Paulo Sergio
 *
 */
@Entity
public class Atividade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private TemplateEnum template;
	
	private String palavra;
	
	@OneToMany(mappedBy="atividade", cascade={CascadeType.MERGE, CascadeType.REMOVE})
	private List<Arquivo> imagens;
	
	@OneToMany(mappedBy="atividade", cascade=CascadeType.REMOVE)
	private List<Tarefa> tarefas;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="idEstudante")
	private Estudante estudante;
 
	private String estudanteTemplate;
	
	private String reforco;
	
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

	public Estudante getEstudante() {
		return estudante;
	}

	public void setEstudante(Estudante estudante) {
		this.estudante = estudante;
	}

	public List<Arquivo> getImagens() {
		return imagens;
	}

	public void setImagens(List<Arquivo> imagens) {
		this.imagens = imagens;
	}

	public String getTemplateToString(){
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
	
}
