package br.com.ufpi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.ufpi.dao.ItemAtividadeDao;
import br.com.ufpi.dao.ListaAtividadeDao;
import br.com.ufpi.model.Arquivo;
import br.com.ufpi.model.ItemAtividade;
import br.com.ufpi.model.ListaAtividade;
import br.com.ufpi.util.ArquivoUtil;

/**
 * 
 * @author Paulo Sergio
 *
 */
@Named
@ViewScoped
public class SucessoCadastrarListaAtividadeBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String idListaAtividade;

	@Inject
	private ListaAtividadeDao listaAtividadeDao;
	
	@Inject
	private ItemAtividadeDao itemAtividadeDao;
	
	private ListaAtividade lista;
	
	private List<ItemAtividade> atividades;
	
	@PostConstruct
	public void init(){
		atividades = new ArrayList<>();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, "Sucesso ao realizar operação.", null));
	}
	
	public void buscarListaAtividade() {
		System.out.println("lista atividade id" +getIdListaAtividade());
		if (getIdListaAtividade() != null){
			lista = listaAtividadeDao.buscarPorId(Long.parseLong(getIdListaAtividade()));
			atividades = itemAtividadeDao.buscarAtividades(lista);
			
		}
		
	}
	
	public StreamedContent downloadArquivo(Arquivo imagem) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
		Date d = new Date();

		String parteNomeArquivo = sdf.format(d);

		String dirTmp = ArquivoUtil.getDiretorio();

		FileOutputStream file = new FileOutputStream(dirTmp + "imagem_" + parteNomeArquivo + ".jpg");
		file.write(imagem.getBytesArquivo());
		file.close();

		DefaultStreamedContent arquivo = new DefaultStreamedContent(
				new FileInputStream(new File(dirTmp + "imagem_" + parteNomeArquivo + ".jpg")), "application/jpg",
				"imagem_" + parteNomeArquivo + ".jpg");
		return arquivo;
	}

	public ListaAtividadeDao getListaAtividadeDao() {
		return listaAtividadeDao;
	}

	public void setListaAtividadeDao(ListaAtividadeDao listaAtividadeDao) {
		this.listaAtividadeDao = listaAtividadeDao;
	}

	public ListaAtividade getLista() {
		return lista;
	}

	public void setLista(ListaAtividade lista) {
		this.lista = lista;
	}

	public ItemAtividadeDao getItemAtividadeDao() {
		return itemAtividadeDao;
	}

	public void setItemAtividadeDao(ItemAtividadeDao itemAtividadeDao) {
		this.itemAtividadeDao = itemAtividadeDao;
	}

	public String getIdListaAtividade() {
		return idListaAtividade;
	}

	public void setIdListaAtividade(String idListaAtividade) {
		this.idListaAtividade = idListaAtividade;
	}

	public List<ItemAtividade> getAtividades() {
		return atividades;
	}

	public void setAtividades(List<ItemAtividade> atividades) {
		this.atividades = atividades;
	}	

}
