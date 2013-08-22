package data;

import java.util.Date;
import java.util.LinkedList;

import output.CandidatesBarChartPrinter;
import output.ExcelPrinter;
import output.MainBarChartPrinter;
import output.Printer;

public class DataAnalyzer {
	private LinkedList<DataCatcher> dataCatchers;
	int limit; //number of iterations
	String[] startingQuery;
	int masterTotal;
	Date start;
	Date end;
	
	
	public DataAnalyzer(int limit, String[] startingQuery){
		this.dataCatchers = new LinkedList<DataCatcher>();
		this.limit = limit;
		this.startingQuery = startingQuery;		
	}
	
	public void add(DataCatcher catcher){
		this.dataCatchers.add(catcher);
		this.masterTotal += catcher.getMainStreamTotalTweets();
		for(String s: catcher.getHashtagToTotalTweets().keySet())
			this.masterTotal += catcher.getHashtagToTotalTweets().get(s);
	}
	
	public LinkedList<DataCatcher> getDataCatchers(){
		return this.dataCatchers;
	}
	
	public String[] getStartingQuery(){
		return this.startingQuery;
	}

	public void printResults() {
		Printer excelPrinter = new ExcelPrinter();
		Printer candidatesBarChartPrinter = new CandidatesBarChartPrinter();
		Printer mainBarChartPrinter = new MainBarChartPrinter();
		
		excelPrinter.print(this);
		candidatesBarChartPrinter.print(this);
		mainBarChartPrinter.print(this);
	}
	
	public void setTime(Date start, Date end){
		this.start = start;
		this.end = end;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public int getMasterTotal() {
		return masterTotal;
	}
	
}
