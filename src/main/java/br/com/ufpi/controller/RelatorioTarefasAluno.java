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
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
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

	private LineChartModel graficoLinha;
	
	private BarChartModel graficoBar;

	private List<TarefaGrafico> tarefaGraficos;
	
	private TemplateEnum templateSelecionado;
	
	private boolean exibirGraficoBarra = false;
	
	private boolean exibirFiltroAluno;

	public RelatorioTarefasAluno() {
		super();
	}

	@PostConstruct
	public void init() {
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
			if(exibirGraficoBarra) {
				montarGraficoBarra();
			}else {
				montarGraficoLinha();
			}
		}
	}

	private void montarGraficoBarra() {
		setGraficoBar(new BarChartModel());
		
		ChartSeries acertos = new ChartSeries();
		acertos.setLabel("Acertos");
		
		ChartSeries erros = new ChartSeries();
		erros.setLabel("Erros");
		for (TarefaGrafico tarefaGrafico : tarefaGraficos) {
			String dataGrafico = EstudanteUtils.getDataPadraoInternacional(tarefaGrafico.getInicio());
			acertos.set(dataGrafico,
					tarefaGrafico.getAcertos());
			erros.set(dataGrafico,
					tarefaGrafico.getErros());
		}
		
		getGraficoBar().addSeries(acertos);
		getGraficoBar().addSeries(erros);
		getGraficoBar().setTitle("Gráfico de Desempenho");
		getGraficoBar().setLegendPosition("ne");
 
        Axis xAxis = getGraficoBar().getAxis(AxisType.X);
        xAxis.setLabel("Data de Execução");
 
        Axis yAxis = getGraficoBar().getAxis(AxisType.Y);
        yAxis.setLabel("Quantidade");
	}

	private void montarGraficoLinha() {
		setGraficoLinha(new LineChartModel());
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

		getGraficoLinha().addSeries(serieAcertos);
		getGraficoLinha().addSeries(serieErros);
		getGraficoLinha().setTitle("Relatório de desempenho");
		getGraficoLinha().getAxis(AxisType.Y).setLabel("Quantidade");
		getGraficoLinha().setLegendPosition("c");
		getGraficoLinha().setShowPointLabels(true);
		getGraficoLinha().getAxes().put(AxisType.X, new CategoryAxis("Data de Execução"));
		
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

	public BarChartModel getGraficoBar() {
		return graficoBar;
	}

	public void setGraficoBar(BarChartModel graficoBar) {
		this.graficoBar = graficoBar;
	}

	public LineChartModel getGraficoLinha() {
		return graficoLinha;
	}

	public void setGraficoLinha(LineChartModel graficoLinha) {
		this.graficoLinha = graficoLinha;
	}

}
