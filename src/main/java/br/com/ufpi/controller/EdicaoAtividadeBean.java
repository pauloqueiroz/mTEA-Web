package br.com.ufpi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.ufpi.dao.ArquivoDao;
import br.com.ufpi.dao.AtividadeDao;
import br.com.ufpi.model.Arquivo;
import br.com.ufpi.model.Atividade;
import br.com.ufpi.util.ArquivoUtil;

@Named
@ViewScoped
public class EdicaoAtividadeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String idAtividade;

	@Inject
	private AtividadeDao atividadeDao;

	@Inject
	private Atividade atividade;

	private List<InputStream> conteudoArquivos;
	
	@Inject
	private ArquivoDao arquivoDao;

	@PostConstruct
	public void init() {
		conteudoArquivos = new ArrayList<>();
	}

	public void buscarAtividade() {
		System.out.println("atividade id" +idAtividade);
		if (idAtividade != null)
			atividade = atividadeDao.carregarAtividadeComArquivos(Long.parseLong(idAtividade));
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

	public void salvarUploads(FileUploadEvent event) {
		if (event.getFile() != null) {
			try {
				conteudoArquivos.add(event.getFile().getInputstream());
			} catch (IOException e) {
				System.out.println("Erro ao salvar arquivo");
				e.printStackTrace();
			}
		}
	}

	public void editar() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		List<Arquivo> arquivosSelecionados = new ArrayList<>();
		for (Arquivo arquivo : atividade.getImagens()) {
			if (arquivo.isDeletar())
				arquivosSelecionados.add(arquivo);
		}
		int quantidadeArquivos = (atividade.getImagens().size() - arquivosSelecionados.size()
				+ conteudoArquivos.size());
		System.out.println("quant arquivos"+quantidadeArquivos);
		System.out.println("quant permitida"+atividade.getTemplate().getQuantidadeArquivos());
		if (quantidadeArquivos > atividade.getTemplate().getQuantidadeArquivos()){
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"O número máximo de arquivos permitidos para o template " + atividade.getTemplate().getDescricao()
							+ " é de " + atividade.getTemplate().getQuantidadeArquivos() + ". Existem "
							+ quantidadeArquivos + " arquivos associados a atividade.",
					null));
			return;
		}else if(atividade.getTemplate().getQuantidadeArquivos() > 0 && quantidadeArquivos == 0){
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"A quantidade de imagens para o template desta tarefa não pode ser 0.",
					null));
			return;
		}
		
		atividadeDao.atualizar(atividade);
		System.out.println("Atualizou");
		for (Arquivo arquivo : arquivosSelecionados){
			System.out.println("id do arquivo: " +arquivo.getId());
			arquivoDao.delete(arquivo);
			atividade.getImagens().remove(arquivo);
		}
		salvarArquivosNovos();
		buscarAtividade();
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, "Atividade alterada com sucesso.",
				null));
	}
	
	/**
	 * Metodo que armazena os arquivos carregados em um lista para serem
	 * adicionados posteriormente na atividade.
	 */
	public void salvarArquivosNovos() {
		
		for (InputStream stream : conteudoArquivos) {
		
			Arquivo arquivo = new Arquivo();
			arquivo.setDataUpload(new Date());
			try {
				byte[] bytes = IOUtils.toByteArray(stream);
				arquivo.setBytesArquivo(bytes);;
				atividade.getImagens().add(arquivo);
				arquivo.setAtividade(atividade);
				int numeroArquivo = arquivoDao.contarArquivos();
				arquivo.setNomeArquivo(String.valueOf(numeroArquivo)+".jpg");
				arquivoDao.adicionar(arquivo);
			} catch (IOException e) {
				System.out.println("Erro ao salvar imagens.");
				e.printStackTrace();
			}
		}
	}

	public EdicaoAtividadeBean() {
		super();
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public String getIdAtividade() {
		return idAtividade;
	}

	public void setIdAtividade(String idAtividade) {
		this.idAtividade = idAtividade;
	}

}
