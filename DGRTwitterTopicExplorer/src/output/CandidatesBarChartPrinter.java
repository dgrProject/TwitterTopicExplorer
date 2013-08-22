package output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.TreeMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import data.DataCatcher;
import domain.Pair;

public class CandidatesBarChartPrinter extends ChartPrinter {
	

	@Override
	public Dataset createDataset(DataCatcher dc) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		String first = "% in main query stream    ";
		String second = "% in single-candidate stream";
		TreeMap<String, Pair> dataMap = dc.getCompleteSecondaryStreamsData();
		Pair p;
		
		for (String candidate: dataMap.keySet()){
			p = dataMap.get(candidate);
			dataset.setValue(p.getFirst(), first, candidate);
			dataset.setValue(p.getSecond(), second, candidate);
		
		}
		return dataset;
	}

	@Override
	public JFreeChart createChart(Dataset dataset, int iteration) {
		JFreeChart chart = ChartFactory.createBarChart(
	                "Candidates intersection with main query - Iteration # " + iteration, // chart title
	                "Candidates", // domain axis label
	                "Intersection", // range axis label
	                (CategoryDataset) dataset, // data
	                PlotOrientation.VERTICAL, // orientation
	                true, // include legend
	                false, // tooltips
	                false // URLs?
	                );
	      
	    //CUSTOMIZATION
		
		final CategoryPlot plot = chart.getCategoryPlot();
		
		//y scale 0-100
		String[] grade =  new String[101];
		for (int i = 0 ; i < grade.length ; i ++){
		    grade[i] = Integer.toString(i);
		}
		SymbolAxis rangeAxis = new SymbolAxis("", grade);
		rangeAxis.setTickUnit(new NumberTickUnit(5));
		rangeAxis.setAutoRange(false);
		rangeAxis.setRange(0, 100);
		plot.setRangeAxis(rangeAxis);
		
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setBarPainter(new StandardBarPainter());
		renderer.setItemMargin(0);
		renderer.setMaximumBarWidth(0.1);
	      
		return chart;
	}

	@Override
	public void createImage(JFreeChart chart, int iteration) {
		PrintStream out;
		try {
			out = new PrintStream(new File("output/charts/iter" + iteration + "_CandidatesBarChart.png"));
			try {
				ChartUtilities.writeChartAsPNG(out, chart, 800, 600);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

}
