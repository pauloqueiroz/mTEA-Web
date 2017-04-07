package br.com.ufpi.api;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;

import br.com.ufpi.dao.ArquivoDao;
import br.com.ufpi.dao.AtividadeDao;
import br.com.ufpi.dao.EstudanteDao;
import br.com.ufpi.model.Arquivo;
import br.com.ufpi.model.Atividade;
import br.com.ufpi.model.Estudante;
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
	
	@GET
	@Path("/student")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEstudantes(){
		List<Estudante> estudantes = estudanteDao.listarTodos();
		List<Student> students = EstudanteUtils.converterEstudante(estudantes, arquivoDao);
		return Response.status(200).entity(students).build();
	}
	
	@GET
	@Path("/student/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEstudante(@Context HttpServletRequest request, @PathParam("id") Long id){
		
		/*
		 * busca pelo id do estudante
		 */
		Estudante estudante = estudanteDao.buscarPorId(id);
		if(estudante != null){
			List<Atividade> atividadesDoEstudante = atividadeDao.carregarAtividadesDoEstudante(estudante);
			if(!CollectionUtils.isEmpty(atividadesDoEstudante)){
				for (Atividade atividade : atividadesDoEstudante) {
					List<Arquivo> arquivosDaAtividade = arquivoDao.carregarArquivosDaAtividade(atividade);
					atividade.setImagens(arquivosDaAtividade);
				}
			}
			estudante.setAtividades(atividadesDoEstudante);
			Student studentJson  = EstudanteUtils.converterEstudante(estudante, arquivoDao);
			return Response.status(404).entity(studentJson).build();
		}
		return Response.status(404).entity("Estudante não encontrado").build();
	}

}
