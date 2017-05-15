package br.com.ufpi.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
/**
 * 
 * @author Paulo Sergio
 *
 */
@Entity
public class ItemAtividade implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private int ordem;
	
	@ManyToOne
	private ListaAtividade lista;
    
	@ManyToOne
	private Atividade atividade;

	public ItemAtividade() {
	
	}

}
