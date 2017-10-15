package br.com.ufpi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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
import org.primefaces.model.UploadedFile;

import br.com.ufpi.dao.ArquivoDao;
import br.com.ufpi.dao.AtividadeDao;
import br.com.ufpi.enuns.TipoArquivoEnum;
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
	
	private List<Arquivo> imagens;
	
	private Arquivo audioExistente;
	
	private UploadedFile audioNovo;

	@PostConstruct
	public void init() {
		conteudoArquivos = new ArrayList<>();
		imagens = new ArrayList<>();
	}

	public void buscarAtividade() {
		if (idAtividade != null){
			atividade = atividadeDao.carregarAtividadeComArquivos(Long.parseLong(idAtividade));
			List<Arquivo> arquivos = arquivoDao.carregarArquivosDaAtividade(atividade);
			if(atividade.possuiAudio()) {
				for (Arquivo arquivo : arquivos) {
					if(arquivo.getTipoArquivo().equals(TipoArquivoEnum.AUDIO)) {
						audioExistente = arquivo;
					}else {
						imagens.add(arquivo);
					}
				}
			}else {
				imagens = arquivos;
			}
			atividade.setArquivos(arquivos);
		}
		
	}

	public StreamedContent downloadArquivo(Arquivo arq) throws IOException {
//		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
//		Date d = new Date();
//
//		String parteNomeArquivo = sdf.format(d);

		String dirTmp = ArquivoUtil.getDiretorio();
		String nomeArquivo = arq.getNomeArquivo();

		FileOutputStream file = new FileOutputStream(dirTmp + nomeArquivo);
		file.write(arq.getBytesArquivo());
		file.close();
		
		String extensaoArquivo = ArquivoUtil.getExtensaoArquivo(arq.getNomeArquivo());

		DefaultStreamedContent arquivo = new DefaultStreamedContent(
				new FileInputStream(new File(dirTmp + nomeArquivo)), "application/"+extensaoArquivo, nomeArquivo);
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
		for (Arquivo arquivo : imagens) {
			if (arquivo.isDeletar())
				arquivosSelecionados.add(arquivo);
		}
		int quantidadeArquivos = (imagens.size() - arquivosSelecionados.size()
				+ conteudoArquivos.size());
		if (quantidadeArquivos > atividade.getTemplate().getQuantidadeMaximaArquivos()){
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"O número máximo de arquivos permitidos para o template " + atividade.getTemplate().getDescricao()
							+ " é de " + atividade.getTemplate().getQuantidadeMaximaArquivos() + ". Existem "
							+ quantidadeArquivos + " arquivos associados a atividade.",
					null));
			return;
		}else if(atividade.getTemplate().getQuantidadeMaximaArquivos() > quantidadeArquivos){
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"A quantidade de imagens para o template desta tarefa não pode ser "+quantidadeArquivos+".",
					null));
			return;
		}
		
		atividadeDao.atualizar(atividade);
		for (Arquivo arquivo : arquivosSelecionados){
			arquivoDao.delete(arquivo);
			atividade.getArquivos().remove(arquivo);
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
				atividade.getArquivos().add(arquivo);
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

	public List<Arquivo> getImagens() {
		return imagens;
	}

	public void setImagens(List<Arquivo> imagens) {
		this.imagens = imagens;
	}

	public Arquivo getAudioExistente() {
		return audioExistente;
	}

	public void setAudioExistente(Arquivo audioExistente) {
		this.audioExistente = audioExistente;
	}

	public UploadedFile getAudioNovo() {
		return audioNovo;
	}

	public void setAudioNovo(UploadedFile audioNovo) {
		this.audioNovo = audioNovo;
	}

}
