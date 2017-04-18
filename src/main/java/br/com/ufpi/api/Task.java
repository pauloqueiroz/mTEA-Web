package br.com.ufpi.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Classe que representa uma execucao de tarefa pelo aluno.
 * 
 * @author Paulo Sergio
 *
 */
public class Task {

    private Long id;
    
    private String start;
    
    private String end;
    
    private Date inicio;
    
    private Date fim;
    
    private Integer touches;
    
    private Integer hits;
    
    private Integer faults;
    
    private Boolean finished;
    
    private Long lesson_id;

    // pontuacao dada pelo aplicativo ao desempenho do aluno
    private Integer rating;

    // id do aluno relacionado ao teste
    private Long student_id;

    public Task(){
        this.touches = 0;
        this.hits = 0;
        this.faults = 0;
        this.finished = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Date getStart() {
//        return start;
//    }

//    public void setStart(Date start) {
//        this.start = start;
//    }

//    public Date getEnd() {
//        return end;
//    }

//    public void setEnd(Date end) {
//        this.end = end;
//    }

    public Integer getTouches() {
        return touches;
    }

    public void setTouches(Integer touches) {
        this.touches = touches;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Integer getFaults() {
        return faults;
    }

    public void setFaults(Integer faults) {
        this.faults = faults;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

//    public Long getDuration(){
//        return (end.getTime() - start.getTime()) / 1000l;
//    }

    public Long getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(Long lesson_id) {
        this.lesson_id = lesson_id;
    }

    public String getDate(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
        return format.format(getStart());
    }

    public void addHit(){
        this.hits++;
    }

    public void addFault(){
        this.faults++;
    }

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Long getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Long student_id) {
		this.student_id = student_id;
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

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}
}
