package com.localgis.mobile.svg.beans;

public class Coordinates {
	
	double x;
	double y;
	
	public Coordinates(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	};
	
	@Override
	public String toString() {
		return "("+x+","+y+")";
	}

}
