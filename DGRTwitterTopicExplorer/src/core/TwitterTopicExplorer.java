package core;

import twitter4j.TwitterException;

public class TwitterTopicExplorer {

	final static int ITERATIONS = 6;
	final static int INTERNAL_ITERATIONS = 3;
	final static double ITERATION_MINUTES = 50;
	
	public static void main(String[] args) {
		
		//STARTING QUERY
		String [] query= new String[]{"#wimbledon"};
		StreamAnalyzer analyzer = new StreamAnalyzer(query, ITERATIONS, INTERNAL_ITERATIONS, ITERATION_MINUTES);
		
		try {
			analyzer.Start();
		} catch (TwitterException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
