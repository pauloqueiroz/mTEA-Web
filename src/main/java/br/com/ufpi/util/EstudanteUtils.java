package br.com.ufpi.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import br.com.ufpi.api.Lesson;
import br.com.ufpi.api.Student;
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
				students.add(converterEstudante(estudante));
			return students;
		}
		return null;
	}
	
	public static Student converterEstudante(Estudante estudante){
		return new Student(estudante.getId(), estudante.getNome(),
				getDataFormatada(estudante.getDataNascimento()), "null", "null", estudante.getAtividades()); 
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
	
	public static List<Lesson> converterAtividades(List<Atividade> atividades) {
		List<Lesson> tarefas = new ArrayList<>();
		for (Atividade atividade : atividades) {
			Lesson lesson = new Lesson(atividade.getId(), atividade.getPalavra(), atividade.getTemplate().ordinal(), "null", String.valueOf(atividade.getEstudante().getId()), "null", null);
			tarefas.add(lesson);
		}
		return CollectionUtils.isEmpty(tarefas)?null:tarefas;
	}

}
