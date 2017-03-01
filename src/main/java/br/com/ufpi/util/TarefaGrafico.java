package br.com.ufpi.util;

import java.io.Serializable;
import java.util.Date;

public class TarefaGrafico implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer acertos;
	
	private Integer erros;
	
	private Date inicio;
	
	public TarefaGrafico() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TarefaGrafico(Integer acertos, Integer erros, Date inicio) {
		super();
		this.acertos = acertos;
		this.erros = erros;
		this.inicio = inicio;
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

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	
	
}
