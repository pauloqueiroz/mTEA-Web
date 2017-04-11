package br.com.ufpi.api;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Lesson {

	private long id;
	
	private String word;
	
	private int template;
	
	private String reinforcement;
	
	private String student_id;
	
	private String user_id;
	
	private String image;
	
	private List<Answer> ansewers;
	
	public Lesson(long id, String word, int template, String reinforcement, String student_id, String user_id,
			String image, List<Answer> ansewers) {
		super();
		this.id = id;
		this.word = word;
		this.template = template;
		this.reinforcement = reinforcement;
		this.student_id = student_id;
		this.user_id = user_id;
		this.image = image;
		this.ansewers = ansewers;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getTemplate() {
		return template;
	}

	public void setTemplate(int template) {
		this.template = template;
	}

	public String getReinforcement() {
		return reinforcement;
	}

	public void setReinforcement(String reinforcement) {
		this.reinforcement = reinforcement;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public List<Answer> getAnsewers() {
		return ansewers;
	}

	public void setAnsewers(List<Answer> ansewers) {
		this.ansewers = ansewers;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
}
