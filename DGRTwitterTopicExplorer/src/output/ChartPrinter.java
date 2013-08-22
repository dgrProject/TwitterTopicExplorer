package output;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;

import data.DataAnalyzer;
import data.DataCatcher;

public abstract class ChartPrinter implements Printer{
	
	@Override
	public void print(DataAnalyzer da){
		int i=1;
		for(DataCatcher dc: da.getDataCatchers()){
			Dataset dataset = createDataset(dc);
			JFreeChart chart = createChart(dataset, i);
			createImage(chart, i);
			i++;
		}
		
	}
	
	public abstract Dataset createDataset(DataCatcher dc);

	public abstract JFreeChart createChart(Dataset dataset, int iteration);
	
	public abstract void createImage(JFreeChart chart, int iteration);
	
}
	/*
	@Override
	public void print(DataAnalyzer da) {
		JFreeChart chart = createChart(createDataset(), "prova");
		PrintStream out;
		try {
			out = new PrintStream(new File("prova.png"));
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
	
    private  PieDataset createDataset() {
        DefaultPieDataset result = new DefaultPieDataset();
        result.setValue("Linux", 29);
        result.setValue("Mac", 20);
        result.setValue("Windows", 51);
        return result;
        
    }
    
    

    private JFreeChart createChart(PieDataset dataset, String title) {
        
        JFreeChart chart = ChartFactory.createPieChart3D(title,          // chart title
            dataset,                // data
            false,                   // include legend
            true,
            false);

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;
    }
    public static void main(String[] argv){
    	DataAnalyzer da = new DataAnalyzer(1, null);
    	PieChartPrinter p = new PieChartPrinter();
    	p.print(da);
    }*/
