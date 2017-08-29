package br.com.ufpi.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.collections.CollectionUtils;

import br.com.ufpi.dao.ArquivoDao;
import br.com.ufpi.dao.AtividadeDao;
import br.com.ufpi.dao.EstudanteDao;
import br.com.ufpi.dao.ItemAtividadeDao;
import br.com.ufpi.dao.ItemListaEstudanteDao;
import br.com.ufpi.dao.TarefaDao;
import br.com.ufpi.enuns.SituacaoEnum;
import br.com.ufpi.model.Arquivo;
import br.com.ufpi.model.Atividade;
import br.com.ufpi.model.Estudante;
import br.com.ufpi.model.ItemAtividade;
import br.com.ufpi.model.ItemListaEstudante;
import br.com.ufpi.model.Tarefa;
import br.com.ufpi.util.ArquivoUtil;
import br.com.ufpi.util.EstudanteConverter;
import br.com.ufpi.util.EstudanteUtils;

@Path("")
@RequestScoped
public class Services {

	@Inject
	private EstudanteDao estudanteDao;

	@Inject
	private AtividadeDao atividadeDao;

	@Inject
	private ArquivoDao arquivoDao;
	
	@Inject
	private TarefaDao tarefaDao;
	
	@Inject
	private ItemListaEstudanteDao itemDao;
	
	@Inject
	private ItemAtividadeDao itemAtividadeDao;

	@GET
	@Path("/student")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEstudantes() {
		List<Estudante> estudantes = estudanteDao.listarTodos();
		List<Student> students = EstudanteUtils.converterEstudante(estudantes);
		return Response.status(200).entity(students).build();
	}

	@GET
	@Path("/student/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEstudante(@Context HttpServletRequest request, @PathParam("id") Long id) {

		/*
		 * busca pelo id do estudante
		 */
		Estudante estudante = estudanteDao.buscarPorId(id);
		if (estudante != null) {
			ItemListaEstudante listaAtual = null;
			List<SituacaoEnum> situacoes = Arrays.asList(SituacaoEnum.CADASTRADO,SituacaoEnum.ENVIADO);
			List<ItemListaEstudante> listasDoAluno = itemDao.buscarListaAtual(estudante,situacoes);
			List<Atividade> atividades = new ArrayList<>();
			if(!CollectionUtils.isEmpty(listasDoAluno)){
				listaAtual = listasDoAluno.get(0);
				listaAtual.getLista();
				Set<ItemAtividade> atividadesDaLista = itemAtividadeDao.carregarAtividades(listaAtual.getLista());
				if (!CollectionUtils.isEmpty(atividadesDaLista)) {
					for (ItemAtividade item : atividadesDaLista) {
						Atividade atividade = item.getAtividade();
						List<Arquivo> arquivosDaAtividade = arquivoDao.carregarArquivosDaAtividade(atividade);
						atividade.setArquivos(arquivosDaAtividade);
						atividades.add(atividade);
					}
				}
			}
			Student studentJson = EstudanteUtils.converterEstudante(estudante, atividades, arquivoDao);
			if(listaAtual != null){
				listaAtual.setSituacao(SituacaoEnum.ENVIADO);
				itemDao.atualizar(listaAtual);	
			}
			return Response.status(200).entity(studentJson).build();
		}
		return Response.status(404).entity("Estudante não encontrado").build();
	}

	@GET
	@Path("/reinforcements/{id}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getReforco(@Context HttpServletRequest request, @PathParam("id") Long id) {

		if (id != null && !id.equals(0l)) {
			Arquivo arquivo = arquivoDao.buscarPorId(id);
			try {
				FileOutputStream file = new FileOutputStream(ArquivoUtil.getDiretorio() + arquivo.getNomeArquivo());
				file.write(arquivo.getBytesArquivo());
				file.close();
			} catch (IOException e) {
				System.out.println("Erro ao criar arquivo");
				e.printStackTrace();
			}

			File file = new File(ArquivoUtil.getDiretorio() + arquivo.getNomeArquivo());
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=" + arquivo.getNomeArquivo());
			return response.build();
		}
		return Response.status(404).entity("Reforço não encontrado").build();
	}
	
	@GET
	@Path("/audios/{id}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getAudio(@Context HttpServletRequest request, @PathParam("id") Long id) {

		if (id != null && !id.equals(0l)) {
			Arquivo arquivo = arquivoDao.buscarPorId(id);
			try {
				FileOutputStream file = new FileOutputStream(ArquivoUtil.getDiretorio() + arquivo.getNomeArquivo());
				file.write(arquivo.getBytesArquivo());
				file.close();
			} catch (IOException e) {
				System.out.println("Erro ao criar arquivo");
				e.printStackTrace();
			}

			File file = new File(ArquivoUtil.getDiretorio() + arquivo.getNomeArquivo());
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=" + arquivo.getNomeArquivo());
			return response.build();
		}
		return Response.status(404).entity("Reforço não encontrado").build();
	}

	@GET
	@Path("/lessons/{id}")
	@Produces("image/png")
	public Response getImagem(@Context HttpServletRequest request, @PathParam("id") Long id) {

		if (id != null && !id.equals(0l)) {
			Arquivo arquivo = arquivoDao.buscarPorId(id);
			try {
				FileOutputStream file = new FileOutputStream(ArquivoUtil.getDiretorio() + arquivo.getNomeArquivo());
				file.write(arquivo.getBytesArquivo());
				file.close();
			} catch (IOException e) {
				System.out.println("Erro ao criar arquivo");
				e.printStackTrace();
			}

			File file = new File(ArquivoUtil.getDiretorio() + arquivo.getNomeArquivo());

			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=" + arquivo.getNomeArquivo());
			return response.build();
		}
		return Response.status(404).entity("Imagem n�o encontrada").build();
	}

	@GET
	@Path("/ansewers/{id}")
	@Produces("image/png")
	public Response getAnswer(@Context HttpServletRequest request, @PathParam("id") Long id) {

		if (id != null && !id.equals(0l)) {
			Arquivo arquivo = arquivoDao.buscarPorId(id);
			try {
				FileOutputStream file = new FileOutputStream(ArquivoUtil.getDiretorio() + arquivo.getNomeArquivo());
				file.write(arquivo.getBytesArquivo());
				file.close();
			} catch (IOException e) {
				System.out.println("Erro ao criar arquivo");
				e.printStackTrace();
			}

			File file = new File(ArquivoUtil.getDiretorio() + arquivo.getNomeArquivo());

			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=" + arquivo.getNomeArquivo());
			return response.build();
		}
		return Response.status(404).entity("Imagem n�o encontrada").build();
	}
	
	
	@POST
	@Path("/task")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTaskInJSON(Task task) throws ParseException {
		if(task != null && task.getStudent_id() != null){
			System.out.println("Start: "+task.getStart());
			System.out.println("End: "+task.getEnd());
			Estudante estudante = estudanteDao.buscarPorId(task.getStudent_id());
			if(estudante != null){
				Atividade atividade = null;
				if(task.getLesson_id() != null){
					atividade = atividadeDao.buscarPorId(task.getLesson_id());
				}
				task.setInicio(EstudanteConverter.parse(task.getStart()));
				task.setFim(EstudanteConverter.parse(task.getEnd()));
				Tarefa tarefa = new Tarefa(task.getInicio(), task.getFim(), task.getTouches(), task.getHits(),
						task.getFaults(), task.getFinished(), task.getRating(), atividade, estudante);
				ItemListaEstudante listaAtual = buscarListaAtual(estudante);
				tarefa.setLista(listaAtual);
				tarefaDao.adicionar(tarefa);
				String result = "Task saved : " + tarefa.getId();
				boolean isListaEncerrada = verificarListaEncerrada(listaAtual);
				if(isListaEncerrada){
					listaAtual.setDataExecucao(new Date());
					listaAtual.setSituacao(SituacaoEnum.CONCLUIDO);
					itemDao.atualizar(listaAtual);
				}	
				return Response.status(201).entity(result).build();
			}
		}
		System.out.println("Task nao encontrada!");
		return Response.status(404).entity("Estudante n�o encontrado").build();
	}

	private boolean verificarListaEncerrada(ItemListaEstudante listaAtual) {
		int quantTarefasExecutadas = tarefaDao.contarTarefasPorLista(listaAtual);
		int quantidadeAtividadesDaLista = itemDao.quantidadeAtividadesLista(listaAtual);
		if(quantTarefasExecutadas >= quantidadeAtividadesDaLista){
			return true;
		}
		return false;
	}

	private ItemListaEstudante buscarListaAtual(Estudante estudante) {
		List<SituacaoEnum> situacoes = Arrays.asList(SituacaoEnum.ENVIADO);
		List<ItemListaEstudante> listasDoAluno = itemDao.buscarListaAtual(estudante,situacoes);
		if(!CollectionUtils.isEmpty(listasDoAluno)){
			return listasDoAluno.get(0);
		}
		return null;
		
	}

	@SuppressWarnings("unused")
	private Set<Long> pegarIdsAtividades(Set<ItemAtividade> atividadesDaLista) {
		Set<Long> idsAtividades = new HashSet<>();
		for (ItemAtividade itemAtividade : atividadesDaLista) 
			idsAtividades.add(itemAtividade.getAtividade().getId());
		return idsAtividades;
	}

	public TarefaDao getTarefaDao() {
		return tarefaDao;
	}

	public void setTarefaDao(TarefaDao tarefaDao) {
		this.tarefaDao = tarefaDao;
	}


}
