package domain;

import miscellaneous.Utility;

public class Pair {
	
	private double first;
	private double second;
	
	public Pair(double first, double second) {
		this.first = first;
		this.second = second;
	}

	public double getFirst() {
		return first;
	}

	public void setFirst(double first) {
		this.first = first;
	}

	public double getSecond() {
		return second;
	}

	public void setSecond(double second) {
		this.second = second;
	}
	
	public String toString() {
		return "first: " + this.first + " second: " + this.second;
	}
	
	public Pair fixPair(){
		return new Pair(Utility.fixDouble(this.first), Utility.fixDouble(this.second));
	}

}
