package miscellaneous;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

import core.QueryUpdater;


public class Utility {
	
	static String readFile(String path, Charset encoding) throws IOException{
		byte [] encoded=Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
	
	public static boolean exists(String s, String [] query ){
		boolean b=false;
		int i=0;
		while(i<=query.length-1 && !b){
			if(query[i].equals(s)){
				b=true;}
			i++;
		}
		return b;
	}
	

	public static boolean exists(LinkedList<String> hashtags, String[] query) {
		for(String s: hashtags){
			if (exists(s, query))
				return true;
		}		
		return false;
	
	}
	
	public static String[] mergeArrays (String[] a1, String[] a2, int limit) {
		int lenght;
		if (a1.length + a2.length <= limit)
			lenght = a1.length + a2.length;
		else
			lenght = limit;
		
		String[] fusion = new String[lenght];
		
		int i;
		for (i=0; i<a1.length; i++)
			fusion[i] = a1[i];
		
		int j=0;
		for (i=a1.length; i<lenght; i++) {
			fusion[i] = a2[j];
			j++;
		}
		
		return fusion;
	}
		
	public static void printArray (Object[] array) {
		for (int i=0; i<array.length; i++)
			System.out.println(array[i]);
	}

	public static int minsToMS(double d) {		
		return (int) (d*60*1000);
	}
	
	public static double fixDouble(double d){
		return Math.rint(d*100)/100;
	}
	
	public static String[] getDifference(String[] a1, String[] a2) {
		String[] difference = new String[0];
		for (String s: a1)
			if (!exists(s, a2)) {
				difference = QueryUpdater.copyQuery(difference);
				difference[difference.length-1] = s;
			}
		return difference;
	}
	
}


