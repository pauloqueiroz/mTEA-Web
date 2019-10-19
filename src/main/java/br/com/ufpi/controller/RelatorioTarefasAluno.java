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
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import br.com.ufpi.dao.EstudanteDao;
import br.com.ufpi.dao.TarefaDao;
import br.com.ufpi.enuns.TemplateEnum;
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
	
	private Estudante estudanteSelecionado;

	private LazyDataModel<Tarefa> tarefasDoEstudante;

	private LineChartModel graficoAcertos;

	private LineChartModel graficoErros;
	
	private BarChartModel graficoAcertosBar;
	
	private BarChartModel graficoErrosBar;

	private List<TarefaGrafico> tarefaGraficos;
	
	private TemplateEnum templateSelecionado;
	
	private boolean exibirGraficoBarra = false;
	
	private boolean exibirFiltroAluno;

	public RelatorioTarefasAluno() {
		super();
	}

	@PostConstruct
	public void init() {
		System.out.println("init");
		tarefaGraficos = new ArrayList<>();
		if(estudanteSelecionado == null) {
			exibirFiltroAluno = true;
		}
		
		if(!StringUtils.isEmpty(idEstudante)){
			
			pesquisar();

		}
	}

	private void createDateModel() {
		if (estudanteSelecionado != null) {
			tarefaGraficos = tarefaDao.buscarTarefasPorEstudante(estudanteSelecionado, templateSelecionado);
			if(exibirGraficoBarra)
				montarGraficoBarra();
			else
				montarGraficoLinha();
		}
	}

	private void montarGraficoBarra() {
		graficoAcertosBar = new BarChartModel();
		
		graficoErrosBar = new BarChartModel();
		
		ChartSeries acertos = new ChartSeries();
		acertos.setLabel("Acertos");
		
		ChartSeries erros = new ChartSeries();
		erros.setLabel("Erros");
		for (TarefaGrafico tarefaGrafico : tarefaGraficos) {
			acertos.set(EstudanteUtils.getDataPadraoInternacional(tarefaGrafico.getInicio()),
					tarefaGrafico.getAcertos());
			erros.set(EstudanteUtils.getDataPadraoInternacional(tarefaGrafico.getInicio()),
					tarefaGrafico.getErros());
		}
//		System.out.println("Acertos: ");
		
		graficoAcertosBar.addSeries(acertos);
		graficoAcertosBar.setTitle("Gráfico de Acertos");
        Axis xAxis = graficoAcertosBar.getAxis(AxisType.X);
        xAxis.setLabel("Data");
         
        Axis yAxis = graficoAcertosBar.getAxis(AxisType.Y);
        yAxis.setLabel("Acertos");
        
        graficoErrosBar.addSeries(erros);
        graficoErrosBar.setTitle("Gráfico de Erros");
        Axis xAxisErros = graficoErrosBar.getAxis(AxisType.X);
        xAxisErros.setLabel("Data");
         
        Axis yAxisErros = graficoErrosBar.getAxis(AxisType.Y);
        yAxisErros.setLabel("Erros");
	}

	private void montarGraficoLinha() {
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
		graficoAcertos.setTitle("Relatório de acertos");
		graficoAcertos.getAxis(AxisType.Y).setLabel("Acertos");
		Date dataFinal = EstudanteUtils.processarDataFinalGrafico(tarefaGraficos); 
		graficoAcertos.getAxis(AxisType.X).setMax(EstudanteUtils.getDataPadraoInternacional(dataFinal));

		povoarGrafico(graficoErros, serieErros, ultimaData);
		graficoErros.setTitle("Relatório de erros");
		graficoErros.getAxis(AxisType.Y).setLabel("Erros");
		graficoErros.getAxis(AxisType.X).setMax(EstudanteUtils.getDataPadraoInternacional(dataFinal));
		
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
		

		setTarefasDoEstudante(new LazyDataModel<Tarefa>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Tarefa> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				List<Tarefa> listaDocumento = null;
				if (isParametroInformado()) {
					if (!StringUtils.isEmpty(idEstudante) && isParametroBuscaNaoInformado()) {
						estudanteSelecionado = estudanteDao.buscarPorId(Long.parseLong(idEstudante));
					}
					listaDocumento = tarefaDao.buscarTarefasPorEstudante(estudanteSelecionado, templateSelecionado,
							first, pageSize);

					this.setRowCount(tarefaDao.contarTarefas(estudanteSelecionado, templateSelecionado));
				} else {
					this.setRowCount(0);
				}
				createDateModel();
				return listaDocumento;
			}

		});
		
	}
	
	public void doNothing() {
		System.out.println("=)");
	}
	
	private boolean isParametroInformado(){
		return (!StringUtils.isEmpty(idEstudante)) || estudanteSelecionado != null || templateSelecionado != null;
	}
	
	private boolean isParametroBuscaNaoInformado(){
		return estudanteSelecionado == null && templateSelecionado == null;
	}

	public void limpar() {
		this.idEstudante = "";
		estudanteSelecionado = null;
		templateSelecionado = null;
		tarefaGraficos = new ArrayList<>();
		
		pesquisar();
	}
	
	public boolean isExibeGraficoLinha() {
		if(!this.exibirGraficoBarra && !CollectionUtils.isEmpty(tarefaGraficos)) {
			return true;
		}
		return false;
	}
	
	public boolean isExibeGraficoBarra() {
		if(this.exibirGraficoBarra && !CollectionUtils.isEmpty(tarefaGraficos)) {
			return true;
		}
		return false;
	}
	
	public TemplateEnum[] getTemplates() {
		return TemplateEnum.values();
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

	public Estudante getEstudanteSelecionado() {
		return estudanteSelecionado;
	}

	public void setEstudanteSelecionado(Estudante estudanteSelecionado) {
		this.estudanteSelecionado = estudanteSelecionado;
	}

	public TemplateEnum getTemplateSelecionado() {
		return templateSelecionado;
	}

	public void setTemplateSelecionado(TemplateEnum templateSelecionado) {
		this.templateSelecionado = templateSelecionado;
	}

	public BarChartModel getGraficoAcertosBar() {
		return graficoAcertosBar;
	}

	public void setGraficoAcertosBar(BarChartModel graficoAcertosBar) {
		this.graficoAcertosBar = graficoAcertosBar;
	}

	public BarChartModel getGraficoErrosBar() {
		return graficoErrosBar;
	}

	public void setGraficoErrosBar(BarChartModel graficoErrosBar) {
		this.graficoErrosBar = graficoErrosBar;
	}

	public boolean isExibirGraficoBarra() {
		return exibirGraficoBarra;
	}

	public void setExibirGraficoBarra(boolean exibirGraficoBarra) {
		this.exibirGraficoBarra = exibirGraficoBarra;
	}

	public boolean isExibirFiltroAluno() {
		return exibirFiltroAluno;
	}

	public void setExibirFiltroAluno(boolean exibirFiltroAluno) {
		this.exibirFiltroAluno = exibirFiltroAluno;
	}

}
