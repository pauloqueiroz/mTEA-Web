package br.com.ufpi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import br.com.ufpi.dao.EstudanteDao;
import br.com.ufpi.dao.TarefaDao;
import br.com.ufpi.model.Estudante;
import br.com.ufpi.model.Tarefa;
import br.com.ufpi.util.EstudanteUtils;
import br.com.ufpi.util.TarefaGrafico;

@Named
@ViewScoped
public class RelatorioTarefasAluno implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private TarefaDao tarefaDao;

	@Inject
	private EstudanteDao estudanteDao;

	private String idEstudante;

	private LazyDataModel<Tarefa> tarefasDoEstudante;

	private LineChartModel graficoAcertos;

	private LineChartModel graficoErros;

	private List<TarefaGrafico> tarefaGraficos;

	public RelatorioTarefasAluno() {
		super();
	}

	@PostConstruct
	public void init() {
		tarefaGraficos = new ArrayList<>();
		// pesquisar();

		// createDateModel();
	}

	private void createDateModel() {
		if (idEstudante != null) {
			tarefaGraficos = tarefaDao.buscarTarefasPorEstudante(Long.parseLong(idEstudante));
			graficoAcertos = new LineChartModel();
			graficoErros = new LineChartModel();
			LineChartSeries serieAcertos = new LineChartSeries();
			serieAcertos.setLabel("Acertos");
			LineChartSeries serieErros = new LineChartSeries();
			serieErros.setLabel("Erros");
			for (TarefaGrafico tarefaGrafico : tarefaGraficos) {
				serieAcertos.set(EstudanteUtils.getDataPadraoInternacional(tarefaGrafico.getInicio()),
						tarefaGrafico.getAcertos());
				serieErros.set(EstudanteUtils.getDataPadraoInternacional(tarefaGrafico.getInicio()),
						tarefaGrafico.getErros());
			}
			Date ultimaData = null;
			if (!CollectionUtils.isEmpty(tarefaGraficos))
				ultimaData = tarefaGraficos.get(tarefaGraficos.size() - 1).getInicio();

			ultimaData = ultimaData != null ? ultimaData : new Date();
			ultimaData = EstudanteUtils.getDiaPosterior(ultimaData);

			povoarGrafico(graficoAcertos, serieAcertos, ultimaData);
			graficoAcertos.setTitle("Relat�rio de acertos");
			graficoAcertos.getAxis(AxisType.Y).setLabel("Acertos");
			Date dataFinal = EstudanteUtils.processarDataFinalGrafico(tarefaGraficos); 
			graficoAcertos.getAxis(AxisType.X).setMax(EstudanteUtils.getDataPadraoInternacional(dataFinal));

			povoarGrafico(graficoErros, serieErros, ultimaData);
			graficoErros.setTitle("Relat�rio de erros");
			graficoErros.getAxis(AxisType.Y).setLabel("Erros");
			graficoErros.getAxis(AxisType.X).setMax(EstudanteUtils.getDataPadraoInternacional(dataFinal));
		}
	}

	private void povoarGrafico(LineChartModel graficoAcertos, LineChartSeries dadosGrafico, Date ultimaDataGrafico) {

		graficoAcertos.addSeries(dadosGrafico);
		graficoAcertos.setZoom(true);
		DateAxis axis = new DateAxis("Data de inicio");
		axis.setTickAngle(-50);
		axis.setTickFormat("%d/%m/%y %H:%M:%S");

		graficoAcertos.getAxes().put(AxisType.X, axis);
	}

	public void pesquisar() {
		createDateModel();

		setTarefasDoEstudante(new LazyDataModel<Tarefa>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Tarefa> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {

				List<Tarefa> listaDocumento = tarefaDao.buscarTarefasPorEstudante(Long.parseLong(idEstudante), first,
						pageSize);

				this.setRowCount(tarefaDao.contarDocumentosPorStatusSetorProcessual(Long.parseLong(idEstudante)));
				return listaDocumento;
			}

		});
	}

	public void limpar() {
		this.idEstudante = "";
		pesquisar();
	}

	public List<Estudante> buscarEstudante(String nome) {
		return estudanteDao.buscarEstudante(nome);
	}

	public LazyDataModel<Tarefa> getTarefasDoEstudante() {
		return tarefasDoEstudante;
	}

	public void setTarefasDoEstudante(LazyDataModel<Tarefa> tarefasDoEstudante) {
		this.tarefasDoEstudante = tarefasDoEstudante;
	}

	public void setTarefaGraficos(List<TarefaGrafico> tarefaGraficos) {
		this.tarefaGraficos = tarefaGraficos;
	}

	public LineChartModel getGraficoAcertos() {
		return graficoAcertos;
	}

	public void setGraficoAcertos(LineChartModel graficoAcertos) {
		this.graficoAcertos = graficoAcertos;
	}

	public LineChartModel getGraficoErros() {
		return graficoErros;
	}

	public void setGraficoErros(LineChartModel graficoErros) {
		this.graficoErros = graficoErros;
	}

	public String getIdEstudante() {
		return idEstudante;
	}

	public void setIdEstudante(String idEstudante) {
		this.idEstudante = idEstudante;
	}
	
	public boolean isAlunoExecutouTarefa(){
		return (!CollectionUtils.isEmpty(tarefaGraficos));
	}

}
