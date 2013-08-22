package core;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

import miscellaneous.Utility;

import connection.Twitter4jPropertiesHandler;
import data.DataAnalyzer;
import data.DataCatcher;
import domain.Pair;
import domain.Tweet;

import twitter4j.TwitterException;

public class StreamAnalyzer {
	
	private int iterationMinutes;
	private int internalIterations;
	
	private String[] startingQuery;
	private String[] currentQuery;
	private Twitter4jPropertiesHandler handler;
	private int iterations;	
	private LinkedList<Tweet> mainStreamTweets;
	private DataAnalyzer dataAnalyzer;
	
	public StreamAnalyzer(String[] startingQuery, int iterations, int internalIterations, double iterationMinutes){
		this.startingQuery = startingQuery;
		this.currentQuery = startingQuery;
		this.handler = new Twitter4jPropertiesHandler();
		this.iterations = iterations;
		this.dataAnalyzer = new DataAnalyzer(iterations, this.startingQuery);
		this.iterationMinutes = Utility.minsToMS(iterationMinutes);
		this.internalIterations = internalIterations;
	}
	
	
	public void Start() throws TwitterException, InterruptedException{
		Date start = new Date();
		int iteration = 1;
		
		doOneCompleteAnalysisCicle();
		iteration++;
		while (iteration <= iterations){
			Thread.sleep(600000); //10 minutes wait time between reconnections
			doOneCompleteAnalysisCicle();
			iteration++;
		}		
		Date end = new Date();
		this.dataAnalyzer.setTime(start, end);
		printResults();
	}
	
	private void printResults() {
		this.dataAnalyzer.printResults();
	}

	public void doOneCompleteAnalysisCicle() throws InterruptedException {
		StreamCatcher mainStreamCatcher = new StreamCatcher(this.currentQuery, this.handler.getProperties());		
		DataCatcher dataCatcher = new DataCatcher();
		dataCatcher.addQueryEvolution((this.currentQuery));
		
		this.mainStreamTweets = new LinkedList<Tweet>();
		
		String[] queryCandidates = startSingleMainStream(mainStreamCatcher, dataCatcher);
		int queryCandidateSize = queryCandidates.length;
		
		String[] confirmedQueryCanditates = new String[0];		
		
		HashMap<String, LinkedList<Pair>> intersectionPercents;
		
		if (queryCandidateSize > 0) {
			
			intersectionPercents = new HashMap<String, LinkedList<Pair>>();
			Pair[] percents = new Pair[queryCandidateSize];
			String hashTag;
			LinkedList<Pair> percentLists;			
			
			LinkedList<StreamCatcher> listStreams = new LinkedList<StreamCatcher>();

			StreamCatcher secondaryStreamCatcher;
			String[] arraySingleQuery = new String[1];
			
			for (int i=0; i<queryCandidateSize; i++) {
				arraySingleQuery[0] = queryCandidates[i];
				secondaryStreamCatcher = new StreamCatcher(arraySingleQuery, this.handler.getProperties());
				listStreams.add(secondaryStreamCatcher);
			}
			mainStreamCatcher.clear();
			for (StreamCatcher sc: listStreams) {
				sc.getTweetsStream();
				Thread.sleep(5000);
			}
			listStreams.add(mainStreamCatcher);
			for (int i=0; i<internalIterations; i++) {
				TreeMap<String, Pair> map = new TreeMap<String, Pair>();
				
				percents = startMultipleStream(queryCandidates, dataCatcher, listStreams);
				for (int j=0; j<queryCandidateSize; j++) {
					
					hashTag = queryCandidates[j];
					
					if (intersectionPercents.containsKey(hashTag))
						percentLists = intersectionPercents.get(hashTag);
					else
						percentLists = new LinkedList<Pair>();
					
					percentLists.add(percents[j]);
					map.put(queryCandidates[j], percents[j]);
					
					intersectionPercents.put(hashTag, percentLists);	
				}
				dataCatcher.addPartialSecondaryStreamsData(map);
			}
			for (StreamCatcher sc: listStreams)
				sc.closeStream();
			this.currentQuery = QueryUpdater.updateMainQuery(this.mainStreamTweets, this.currentQuery, this.startingQuery, dataCatcher);
			confirmedQueryCanditates = QueryUpdater.evaluateInsertions(intersectionPercents, dataCatcher);
		}
		
		this.currentQuery = Utility.mergeArrays(this.currentQuery, confirmedQueryCanditates, 6);
		dataCatcher.addQueryEvolution(currentQuery);
		dataCatcher.calculateMainStreamFlatData(mainStreamTweets);
		this.dataAnalyzer.add(dataCatcher);
		
	}
	
	public String[] startSingleMainStream(StreamCatcher mainStream, DataCatcher dataCatcher) throws InterruptedException {
		
		mainStream.getTweetsStream();		
		Thread.sleep(this.iterationMinutes/(internalIterations+1));
			
		int totalTweets = mainStream.getTotalTweets();
		mainStream.updateMap();
		TreeMap<String, Integer> mainStreamResults = mainStream.getMap();
		dataCatcher.setSingleMainStreamCandidates(mainStreamResults);
		
		String[] queryCandidates = QueryUpdater.getNewQueryCandidates(mainStreamResults, this.currentQuery, totalTweets);
		
		this.mainStreamTweets = mainStream.getTweets();
		dataCatcher.setTweetSingleMainStream(this.mainStreamTweets.size());
		
		return queryCandidates;
	}
	
	
	public Pair[] startMultipleStream(String[] queryCandidates, DataCatcher dataCatcher, LinkedList<StreamCatcher> listStreams) throws InterruptedException {
		
		int queryCanditatesSize = queryCandidates.length;	

		Thread.sleep(this.iterationMinutes/(this.internalIterations +1));

		LinkedList<Tweet> tweets = listStreams.get(0).getTweets();
		this.mainStreamTweets.addAll(tweets);
		dataCatcher.addTweets2MultipleStreamMap("query", tweets.size());
		
		/*returning query candidates percents*/
		double d1;
		double d2;
		Pair pair;
		Pair[] percents = new Pair[queryCanditatesSize];
		StreamCatcher sc;
		
		for (int i=0; i<queryCanditatesSize; i++) {
			sc = listStreams.get(i+1);
			LinkedList<Tweet> l = sc.getTweets();
			dataCatcher.addHashtagToTotalTweets(queryCandidates[i], l.size());
			dataCatcher.addTweets2MultipleStreamMap(queryCandidates[i], l.size());
			d1 = Evaluator.getIntersectionMainInSecondary(tweets, l);
			d2 = Evaluator.getIntersectionSecondaryInMain(tweets, l);
			pair = new Pair(d1, d2);
			percents[i]=pair;
		}
		for (StreamCatcher toClear: listStreams) {
			toClear.clear(); 
		}
		
		return percents;		
	}
	
}
