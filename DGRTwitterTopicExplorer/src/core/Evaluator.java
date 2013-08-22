package core;

import java.util.LinkedList;

import miscellaneous.Utility;

import domain.Tweet;

public class Evaluator {

	public static double getIntersectionMainInSecondary(LinkedList<Tweet> main, LinkedList<Tweet> secondary){
		double cont = 0;
		
		for(Tweet t1: main){
			for(Tweet t2: secondary){
				if(t1.getId() == t2.getId())
					cont++;
			}
		}
		if (secondary.size() == 0)
			return 0;
		return (double)(cont/secondary.size())*100;
	}
	
	public static double getIntersectionSecondaryInMain(LinkedList<Tweet> main, LinkedList<Tweet> secondary){
		double cont = 0;
		
		for(Tweet t1: main){
			for(Tweet t2: secondary){
				if(t1.getId() == t2.getId())
					cont++;
			}
		}
		if (main.size() == 0)
			return 0;
		return (double)(cont/main.size())*100;
	}
	
	public static double getMainQueryIntersection(LinkedList<Tweet> main, String[] startingQuery, String hashtag){
		double cont1 = 0.0;
		double cont2 = 0.0;
		boolean b;
		
		for(Tweet t1: main){
			if(t1.hasHashtag(hashtag.substring(1))){
				b=false;
				for(String s: t1.getHashtags()){
					if (Utility.exists("#" + s, startingQuery) && !b){
						cont1++;
						b=true;
					}
				}
				cont2++;
			}			
		}
		if (cont2 == 0)
			return 0;
		return (double)(cont1/cont2)*100;
	}
	
	
	

}
