package br.com.ufpi.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import br.com.ufpi.api.Answer;
import br.com.ufpi.api.Lesson;
import br.com.ufpi.api.Student;
import br.com.ufpi.dao.ArquivoDao;
import br.com.ufpi.model.Arquivo;
import br.com.ufpi.model.Atividade;
import br.com.ufpi.model.Estudante;

/**
 * Classe utilizada para converter da classe @see {@link Estudante} para @see
 * {@link Student}.
 * 
 * @author Paulo Sergio
 *
 */
public class EstudanteUtils {

	public static List<Student> converterEstudante(List<Estudante> estudantes, ArquivoDao arquivoDao) {
		if (!CollectionUtils.isEmpty(estudantes)) {
			List<Student> students = new ArrayList<>();
			for (Estudante estudante : estudantes)
				students.add(converterEstudante(estudante, arquivoDao));
			return students;
		}
		return null;
	}
	
	public static Student converterEstudante(Estudante estudante, ArquivoDao arquivoDao){
		System.out.println(estudante.getAtividades().size());
//		estudante.getAtividades(), arquivoDao
		Student student = new Student(estudante.getId(), estudante.getNome(),
				getDataFormatada(estudante.getDataNascimento()), arquivoDao.buscarIdReforco(estudante) , "null");
		List<Lesson> lessons = EstudanteUtils.converterAtividades(estudante.getAtividades(), arquivoDao);
		student.setLessons(lessons);
		return student; 
	}

	public static String getDataFormatada(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(data);
	}

	public static String getDataFormatadaEmSegundos(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		return sdf.format(data);
	}
	
	public static String getDataPadraoInternacional(Date data){
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		return sdf.format(data);
	}
	
	public static Date getDiaPosterior(Date data){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}
	
	public static List<Lesson> converterAtividades(List<Atividade> atividades, ArquivoDao arquivoDao) {
		List<Lesson> tarefas = new ArrayList<>();
		
		for (Atividade atividade : atividades) {
			Lesson lesson = new Lesson(atividade.getId()
					, atividade.getPalavra()
					, atividade.getTemplate().ordinal()
					, null
					, String.valueOf(atividade.getEstudante().getId())
					, null);
			/*
			 * Formar ou sobrepor palavras
			 */
			if (atividade.getTemplate().ordinal() < 2) {
				String idImagem = arquivoDao.buscarIdImagem(atividade);
				lesson.setImage(idImagem);
			}else{
				List<Answer> answers = converterAnswers(atividade.getImagens());
				lesson.setAnsewers(answers);
			}
			tarefas.add(lesson);
		}
		return CollectionUtils.isEmpty(tarefas)?null:tarefas;
	}

	private static List<Answer> converterAnswers(List<Arquivo> imagens) {
		List<Answer> answers = new ArrayList<>();
		for (Arquivo imagem : imagens) {
			Answer answer = new Answer(imagem.getId(), String.valueOf(imagem.getId()), imagem.getAtividade().getId());
			answers.add(answer);
		}
		return answers;
	}

}
