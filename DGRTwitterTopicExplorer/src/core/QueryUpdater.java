package core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import miscellaneous.Utility;

import data.DataCatcher;
import domain.Pair;
import domain.Tweet;

public class QueryUpdater {
	
	public static String[] getNewQueryCandidates(Map <String, Integer> map, String[] query, int totalTweets){
		
		int limit = 3;	// top 3
		String[] newQuery = new String[0];
				
		for(String key: map.keySet()){
			
			if((!(Utility.exists("#" + key, query))) && (newQuery.length < limit)) {
				newQuery = copyQuery(newQuery);
				newQuery[newQuery.length-1] = "#" + key;
			}
		}
		return newQuery;
	}

	public static String[] copyQuery(String[] query){
		String [] returnQuery=new String[query.length+1];
		for(int i=0; i<=query.length-1;i++){
			returnQuery[i]=query[i];
		}
		return returnQuery;
	}
	
	public static String[] listToArray(LinkedList<String> list){
		String[] array = new String[list.size()];
		int i = 0;
		for (String s: list) {
			array[i] = s;
			i++;
		}
		return array;
	}
	
	
	public static String[] evaluateInsertions(HashMap<String, LinkedList<Pair>> percentualMap, DataCatcher catcher) {
		String[] candidates = new String[0];
		
		double average1;
		double average2;
		double average;
		Set<String> hashTags = percentualMap.keySet();
		for (String s: hashTags) {
			average1 = getAverage(getAllFirsts(percentualMap.get(s)));
			average2 = getAverage(getAllSeconds(percentualMap.get(s)));
			catcher.addCompleteSecondaryStreamsData(s, new Pair(average1, average2));
			average = Math.max(average1, average2);
			if (average >= 50) {
				candidates = QueryUpdater.copyQuery(candidates);
				candidates[candidates.length-1] = s;
			}
			else {
				if ((average2 >= 5) && (isConstant(getAllSeconds(percentualMap.get(s)), average2))) {
					candidates = QueryUpdater.copyQuery(candidates);
					candidates[candidates.length-1] = s;
				}
			}				
		}
						
		return candidates;
	} 
	
	
	
	public static LinkedList<Double> getAllFirsts(LinkedList<Pair> list) {
		LinkedList<Double> firsts = new LinkedList<Double>();
		for (Pair p: list)
			firsts.add(p.getFirst());
		return firsts;
	}
	
	public static LinkedList<Double> getAllSeconds(LinkedList<Pair> list) {
		LinkedList<Double> seconds = new LinkedList<Double>();
		for (Pair p: list)
			seconds.add(p.getSecond());
		return seconds;
	}
		
	
	public static double getAverage(LinkedList<Double> list) {
		double sum = 0;
		for (double d: list)
			sum += d;
		return (double)(sum/(list.size()));
	}

	public static boolean isConstant(LinkedList<Double> list, double average){
		double variance=0;
		for(Double d: list){
			variance += Math.pow((d-average), 2);
		}
		variance = variance/list.size();
		double deviation = Math.sqrt(variance);
		if (deviation > 2) {
			return false;
		}
		return true;
	}
	
	
	public static String[] updateMainQuery(LinkedList<Tweet> mainStream, String[] mainQuery, String[] startingQuery, DataCatcher catcher){
		String[] result = new String[0];
		double percent;
		for(int i=0; i<mainQuery.length; i++){
			percent = Evaluator.getMainQueryIntersection(mainStream, startingQuery, mainQuery[i]);
			catcher.addMainStreamData(mainQuery[i], percent);
			if(percent >= 3){
				result = copyQuery(result);
				result[result.length-1] = mainQuery[i];
			}
		}
		return result;
	}

}
