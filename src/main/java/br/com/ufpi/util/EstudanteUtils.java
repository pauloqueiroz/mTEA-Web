package br.com.ufpi.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

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

	public static List<Student> converterEstudante(List<Estudante> estudantes) {
		if (!CollectionUtils.isEmpty(estudantes)) {
			List<Student> students = new ArrayList<>();
			 for (Estudante estudante : estudantes)
				 students.add(converterEstudanteLazy(estudante));
			return students;
		}
		return null;
	}

	public static Student converterEstudante(Estudante estudante, List<Atividade> atividades, ArquivoDao arquivoDao) {
		 System.out.println(atividades.size());
		Student student = new Student(estudante.getId(), estudante.getNome(),
				getDataFormatada(estudante.getDataNascimento()), arquivoDao.buscarIdReforco(estudante), "null");
		 List<Lesson> lessons =
		 EstudanteUtils.converterAtividades(estudante, atividades,
		 arquivoDao);
		 student.setLessons(lessons);
		return student;
	}

	
	public static Student converterEstudanteLazy(Estudante estudante) {
		Student student = new Student(estudante.getId(), estudante.getNome(),
				getDataFormatada(estudante.getDataNascimento()), null, null);
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

	public static String getDataPadraoInternacional(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		return data != null ? sdf.format(data) : null;
	}

	public static Date getDiaPosterior(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	public static Date processarDataFinalGrafico(List<TarefaGrafico> tarefas) {
		if (CollectionUtils.isEmpty(tarefas) || tarefas.size() < 2)
			return new Date();
		Seconds diferencaMenor = null;
		for (int i = 1; i < tarefas.size(); i++) {
			DateTime data1 = new DateTime(tarefas.get(i - 1).getInicio());
			DateTime data2 = new DateTime(tarefas.get(i).getInicio());
			Seconds segundos = Seconds.secondsBetween(data1, data2);
			if (diferencaMenor == null || segundos.isLessThan(diferencaMenor))
				diferencaMenor = segundos;
		}
		DateTime ultimaData = new DateTime(tarefas.get(tarefas.size() - 1).getInicio());
		DateTime dataFinal = ultimaData.plusSeconds(diferencaMenor.getSeconds());
		return dataFinal.toDate();
	}

	
	public static List<Lesson> converterAtividades(Estudante estudante, List<Atividade> atividades, ArquivoDao arquivoDao) {
		List<Lesson> tarefas = new ArrayList<>();

		for (Atividade atividade : atividades) {
			Lesson lesson = new Lesson(atividade.getId(), atividade.getPalavra(), atividade.getTemplate().ordinal(),
					atividade.getReforco(), String.valueOf(estudante.getId()), null);

			// Formar ou sobrepor palavras

			if (atividade.getTemplate().ordinal() < 2) {
				String idImagem = arquivoDao.buscarIdImagem(atividade);
				lesson.setImage(idImagem);
			} else {
				List<Answer> answers = converterAnswers(atividade.getArquivos());
				lesson.setAnsewers(answers);
			}
			tarefas.add(lesson);
		}
		return CollectionUtils.isEmpty(tarefas) ? null : tarefas;
	}
	 

	private static List<Answer> converterAnswers(List<Arquivo> imagens) {
		List<Answer> answers = new ArrayList<>();
		for (Arquivo imagem : imagens) {
			String tipoArquivo = imagem.getTipoArquivo() != null ? imagem.getTipoArquivo().getDescricao() : "";
			Answer answer = new Answer(imagem.getId(), String.valueOf(imagem.getId()), tipoArquivo, imagem.getAtividade().getId());
			answers.add(answer);
		}
		return answers;
	}

	public static Calendar beginDateToCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal;
	}

	public static Calendar endDateToCalendar(Date date) {
		Calendar cal = beginDateToCalendar(date);
		cal.add(Calendar.DATE, 1);
		cal.add(Calendar.MILLISECOND, -1);
		return cal;
	}
}
