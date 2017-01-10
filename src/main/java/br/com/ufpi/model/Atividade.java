package br.com.ufpi.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
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
	
	@OneToMany(mappedBy="atividade")
	private Set<Arquivo> imagens;
	
	@ManyToOne
	@JoinColumn(name="idEstudante")
	private Estudante estudante;
 
	
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

	public Set<Arquivo> getImagens() {
		return imagens;
	}

	public void setImagens(Set<Arquivo> imagens) {
		this.imagens = imagens;
	}

	public Estudante getEstudante() {
		return estudante;
	}

	public void setEstudante(Estudante estudante) {
		this.estudante = estudante;
	}

	
}
