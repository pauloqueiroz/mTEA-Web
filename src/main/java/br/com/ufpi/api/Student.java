package br.com.ufpi.api;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.collections.CollectionUtils;

import br.com.ufpi.model.Atividade;
import br.com.ufpi.util.EstudanteUtils;

/**
 * Classe utilizada para conformizar o nome dos parametros dos objetos 
 * retornados no json com os atributos que eram retornados na versao 
 * passada da aplicacao.
 * @author Paulo Sergio
 *
 */
@XmlRootElement
public class Student{
	
	private long id;
	
	private String name;
	
	private String birth;
	
	private String default_reinforcement;
	
	private String user_id;
	
	private List<Lesson> lessons;

	public Student(long id, String name, String birth, String default_reinforcement, String user_id, List<Atividade> atividades) {
		super();
		this.id = id;
		this.name = name;
		this.setBirth(birth);
		this.setDefault_reinforcement(default_reinforcement);
		this.setUser_id(user_id);
		List<Lesson> lessons;
		if (CollectionUtils.isEmpty(atividades))
			lessons = new ArrayList<>();
		else
			lessons = EstudanteUtils.converterAtividades(atividades); 
		setLessons(lessons);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefault_reinforcement() {
		return default_reinforcement;
	}

	public void setDefault_reinforcement(String default_reinforcement) {
		this.default_reinforcement = default_reinforcement;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}
	
}
