package br.com.ufpi.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import br.com.ufpi.dao.EstudanteDao;
import br.com.ufpi.dao.TarefaDao;
import br.com.ufpi.model.Estudante;
import br.com.ufpi.model.Tarefa;

@Named
@ViewScoped
public class RelatorioTarefasAluno implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private TarefaDao tarefaDao;
	
	@Inject
	private EstudanteDao estudanteDao;
	
	private Estudante estudanteSelecionado;
	
	private LazyDataModel<Tarefa> tarefasDoEstudante;
	
	private LineChartModel graficoLinha;
	
	public RelatorioTarefasAluno() {
		super();
	}
	
	@PostConstruct
	public void init() {
		
		pesquisar();
		
		createLineModels();
	}
	
	private void createLineModels() {
		graficoLinha = initLinearModel();
		graficoLinha.setTitle("Linear Chart");
		graficoLinha.setLegendPosition("e");
        Axis yAxis = graficoLinha.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(30);
    }
	
	 private LineChartModel initLinearModel() {
	        LineChartModel model = new LineChartModel();
	 
	        LineChartSeries series1 = new LineChartSeries();
	        series1.setLabel("Acertos");
	 
	        series1.set("22-09-2017", 2);
	        series1.set("23-09-2017", 1);
	        series1.set("25-09-2017", 3);
	        series1.set("27-09-2017", 6);
	        series1.set("29-09-2017", 8);
	 
	        model.addSeries(series1);
	         
	        return model;
	    }
	
	public void pesquisar(){
		setTarefasDoEstudante(new LazyDataModel<Tarefa>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Tarefa> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {

				List<Tarefa> listaDocumento = tarefaDao.buscarTarefasPorEstudante(estudanteSelecionado, first, pageSize);

				this.setRowCount(tarefaDao.contarDocumentosPorStatusSetorProcessual(estudanteSelecionado));
				return listaDocumento;
			}

		});
	}

	public void limpar(){
		this.estudanteSelecionado = null;
		pesquisar();
	}
	
	public List<Estudante> buscarEstudante(String nome) {
		return estudanteDao.buscarEstudante(nome);
	}

	public Estudante getEstudanteSelecionado() {
		return estudanteSelecionado;
	}



	public void setEstudanteSelecionado(Estudante estudanteSelecionado) {
		this.estudanteSelecionado = estudanteSelecionado;
	}

	public LazyDataModel<Tarefa> getTarefasDoEstudante() {
		return tarefasDoEstudante;
	}

	public void setTarefasDoEstudante(LazyDataModel<Tarefa> tarefasDoEstudante) {
		this.tarefasDoEstudante = tarefasDoEstudante;
	}

	public LineChartModel getGraficoLinha() {
		return graficoLinha;
	}

	public void setGraficoLinha(LineChartModel graficoLinha) {
		this.graficoLinha = graficoLinha;
	}
		

}
