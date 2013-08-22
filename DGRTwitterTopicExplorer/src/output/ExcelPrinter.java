package output;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import miscellaneous.Utility;

import data.DataAnalyzer;
import data.DataCatcher;
import domain.Pair;

public class ExcelPrinter implements Printer {

	@Override
	public void print(DataAnalyzer dataAnalyzer) {
		try {
			FileWriter writer = new FileWriter("output/output.csv");
			
			// print time
			this.printTime(writer, dataAnalyzer);
			writer.append("\n");
			
			// print master total tweets
			this.printMasterTotalTweets(writer, dataAnalyzer);
			writer.append("\n\n");
			
			// print starting query
			this.PrintStartingQuery(writer, dataAnalyzer);
			writer.append("\n\n");
			
			
			writer.append("\n");
			writer.append("------- BEGIN ITERATION ------");
			writer.append("\n");
			writer.append("\n");
			
			
			for (DataCatcher dc: dataAnalyzer.getDataCatchers()) {
				
				// print map single stream
				this.printMapSingleStream(writer, dc);
				writer.append("\n");
				
				// print percents secondary streams
				this.printPercentsSecondaryStreams(writer, dc);	
				writer.append("\n");
				
				// print query after addition
				this.printQueryAfterAddition(writer, dc);
				writer.append("\n\n");
				
				// print percents main stream
				this.printPercentsMainStream(writer, dc);
				writer.append("\n");
				
				// print query after deletion				
				this.printQueryAfterDeletion(writer, dc);
				writer.append("\n\n");
				
				// print new query
				this.printNewQuery(writer, dc);	
				writer.append("\n");
				
				writer.append("\n");
				writer.append("------- END ITERATION ------");
				writer.append("\n");
				writer.append("\n");
				
			} //end   for (DataCatcher dc: da.getDataCatchers()) {
			
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void printTime(FileWriter writer, DataAnalyzer dataAnalyzer) throws IOException {
		long start = dataAnalyzer.getStart().getTime();
		long end = dataAnalyzer.getEnd().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		writer.append("start time");
		writer.append(",");
		writer.append(sdf.format(start));
		writer.append("\n");
		writer.append("end time");
		writer.append(",");
		writer.append(sdf.format(end));
		writer.append("\n");
		writer.append("total time");
		writer.append(",");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		writer.append(sdf.format(end-start));
	}  
	
	
	public void printMasterTotalTweets(FileWriter writer, DataAnalyzer dataAnalyzer) throws IOException {
		writer.append("TOTAL analyzed tweets");
		writer.append(",");
		writer.append(Integer.toString(dataAnalyzer.getMasterTotal()));
	}
	
	
	public  void PrintStartingQuery(FileWriter writer, DataAnalyzer dataAnalyzer) throws IOException {
		String[] startingQuery = dataAnalyzer.getStartingQuery();
		writer.append("starting query");
		writer.append("\n");
		for (int i=0; i<startingQuery.length; i++) {
			writer.append(startingQuery[i]);
			writer.append(",");
		}
	}
	
	public void printMapSingleStream(FileWriter writer, DataCatcher dc) throws IOException {
		writer.append("main stream total tweets");
		writer.append(",");
		writer.append(Integer.toString(dc.getTweetSingleMainStream()));
		writer.append("\n");
		writer.append("di cui");
		writer.append("\n");
		TreeMap<String, Integer> singleMainStreamCandidates = dc.getSingleMainStreamMap();
		for (String s:  singleMainStreamCandidates.keySet()) {
			writer.append(s);
			writer.append(",");
			writer.append(singleMainStreamCandidates.get(s).toString());
			writer.append("\n");
		}
	}
	
	
	
	public void printPercentsSecondaryStreams(FileWriter writer, DataCatcher dc) throws IOException {
		LinkedList<TreeMap<String, Pair>> mapList = dc.getPartialSecondaryStreamsData();
		Set<String> candidates = dc.getHashtagToTotalTweets().keySet();
		HashMap<String, LinkedList<Integer>> tweets2MultipleStreamMap = dc.getTweets2MultipleStreamMap();
		
		writer.append(",");
		writer.append("query");
		writer.append(",");
		writer.append(",");
		
		for (String s: candidates) {
			writer.append(s);
			writer.append(",");
			writer.append(",");
			writer.append(",");
			writer.append(",");
		}
		writer.append("\n");
		
		writer.append(",");
		writer.append(",");
		writer.append(",");
		writer.append(",");
		
		for (int i=0; i<candidates.size(); i++) {
			writer.append("% fst");
			writer.append(",");
			writer.append("% snd");
			writer.append(",");
			writer.append(",");
			writer.append(",");
		}
		writer.append("\n");
		
		
		int iteration = 0;
		int tweetsNumber = 0;
		Pair pair;
		
		for (TreeMap<String, Pair> map: mapList) {
			writer.append("iter " + (iteration+1));
			writer.append(",");
			tweetsNumber = tweets2MultipleStreamMap.get("query").get(iteration);
			writer.append(Integer.toString(tweetsNumber));
			writer.append(",");
			writer.append(",");
			
			for (String s: candidates) {				
				tweetsNumber = tweets2MultipleStreamMap.get(s).get(iteration);
				writer.append(Integer.toString(tweetsNumber));
				writer.append(",");
				
				pair = map.get(s);
				writer.append(Double.toString(pair.getFirst()));
				writer.append(",");
				writer.append(Double.toString(pair.getSecond()));
				writer.append(",");
				writer.append(",");
			}
			iteration++;
			writer.append("\n");
		}
	}
	
	
	
	
	public void printQueryAfterAddition(FileWriter writer, DataCatcher dc) throws IOException {
		String[] query0 = dc.getQueryEvolution().get(0);
		String[] query1 = dc.getQueryEvolution().get(1);
		String[] added = Utility.getDifference(query1, query0);
		if (added.length > 0) {
			writer.append("added terms:");
			writer.append("\n");
			for (int i=0; i<added.length; i++) {
				writer.append(added[i]);
				writer.append(",");
			}
		}
		else
			writer.append("no new terms added");			
	}
	
	
	
	public void printPercentsMainStream(FileWriter writer, DataCatcher dc) throws IOException {
		writer.append("percents main stream");
		writer.append("\n");
		TreeMap<String, Double> mainStreamData = dc.getMainStreamData();
		for (String s: mainStreamData.keySet()) {
			writer.append(s);
			writer.append(",");
			writer.append(mainStreamData.get(s).toString());
			writer.append("\n");
		}
	}
	
	
	
	public void printQueryAfterDeletion(FileWriter writer, DataCatcher dc) throws IOException {
		String[] query0 = dc.getQueryEvolution().get(0);
		String[] query1 = dc.getQueryEvolution().get(1);
		String[] deleted = Utility.getDifference(query0, query1);
		if (deleted.length > 0) {
			writer.append("deleted terms:");
			writer.append("\n");
			for (int i=0; i<deleted.length; i++) {
				writer.append(deleted[i]);
				writer.append(",");
			}
		}
		else
			writer.append("no terms deleted");
	}
	
	
	public void printNewQuery(FileWriter writer, DataCatcher dc) throws IOException {
		String[] query1 = dc.getQueryEvolution().get(1);
		writer.append("new query");
		writer.append("\n");
		for (String s: query1) {
			writer.append(s);
			writer.append(",");
		}
	}

}
