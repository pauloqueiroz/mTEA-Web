package br.com.ufpi.api;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Paulo Sergio
 *
 */
@XmlRootElement
public class Answer {

	private long id;
	
	private String ansewer;
	
	private String typeFile;
	
	private Long lesson_id;

	public Answer(long id, String ansewer, Long lesson_id) {
		super();
		this.id = id;
		this.ansewer = ansewer;
		this.lesson_id = lesson_id;
	}

	public Answer(long id, String ansewer, String typeFile, Long lesson_id) {
		super();
		this.id = id;
		this.ansewer = ansewer;
		this.typeFile = typeFile;
		this.lesson_id = lesson_id;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAnsewer() {
		return ansewer;
	}

	public void setAnsewer(String ansewer) {
		this.ansewer = ansewer;
	}

	public Long getLesson_id() {
		return lesson_id;
	}

	public void setLesson_id(Long lesson_id) {
		this.lesson_id = lesson_id;
	}

	public String getTypeFile() {
		return typeFile;
	}

	public void setTypeFile(String typeFile) {
		this.typeFile = typeFile;
	}
	
}
