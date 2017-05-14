package br.com.ufpi.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ufpi.dao.AtividadeDao;
import br.com.ufpi.dao.TarefaDao;
import br.com.ufpi.model.Atividade;
import br.com.ufpi.model.Tarefa;

@Named
@ViewScoped
public class CadastrarTarefa implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private TarefaDao tarefaDao;
	
	@Inject
	private AtividadeDao atividadeDao;
	
	@Inject
	private Tarefa tarefa;

	public CadastrarTarefa() {
		super();
	}
	
	public List<Atividade> buscarAtividade(String templateEstudante) {
		return atividadeDao.buscarAtividade(templateEstudante);
	}
	
	public void salvar(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
//		tarefa.setEstudante(tarefa.getAtividade().getEstudante());
		tarefaDao.adicionar(tarefa);
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, "Tarefa cadastrada com sucesso.",
				null));
		limpar();
	}
	
	public void limpar(){
		tarefa = new Tarefa();
	}

	public TarefaDao getTarefaDao() {
		return tarefaDao;
	}

	public void setTarefaDao(TarefaDao tarefaDao) {
		this.tarefaDao = tarefaDao;
	}

	public Tarefa getTarefa() {
		return tarefa;
	}

	public void setTarefa(Tarefa tarefa) {
		this.tarefa = tarefa;
	}

	public AtividadeDao getAtividadeDao() {
		return atividadeDao;
	}

	public void setAtividadeDao(AtividadeDao atividadeDao) {
		this.atividadeDao = atividadeDao;
	}
	

}
