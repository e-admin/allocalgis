package com.geopista.util;


public class Rango {

	private double max;
	private double min;


	public Rango(
			double val) {
		this.min = val;
		this.max = val;
	
	}
	
	public Rango(
			double min,
			double max) {
		this.min = min;
		this.max = max;
	
	}
	public boolean equals(Object obj) {

		boolean isEquals = false;
		if (obj instanceof Rango)
		{
			if(max == ((Rango)obj).getMax() && min ==((Rango)obj).getMin())
				isEquals = true;
		}
		return isEquals;
	}

	public double getMax() {
		return max;
	}

	public double getMin() {
		return min;
	}
	

	public void setMax(double object) {
		max = object;
	}

	public void setMin(double object) {
		min = object;
	}

	public boolean contains (Rango r)
	{
		boolean isContained = false;
		
		if (r.getMin()>= this.getMin() 
				&& r.getMax()<= this.getMax() )
			isContained = true;
		
		return isContained;
	}
	
	public int hashCode ()
	{
		return 0;
	}

}
