/*
 * Created on 06-oct-2004
 */
package com.geopista.style.sld.classifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Enxenio, SL
 */
public class NaturalBreaks implements Classifier {

	public boolean canBeApplied(Object aValue) {
		try {
			new Double((aValue.toString()));
			return true;
		}
		catch (NumberFormatException e) {
			return false; 
		}
	}

	public Set classify(SortedSet values, int numberOfClasses) {
		int[][] groups;
		int[][] groupsPlus;
		int[][] groupsMinus;
	
			
		groups = new int[numberOfClasses][2];
		double elementsInQuantile = values.size() / (double) numberOfClasses;
		for (int i=0; i<numberOfClasses; i++) {
			// OJO, las clases no deben solapar
			// el extremo inferior está includio
			// el extremo superior NO esta incluido
			groups[i][0] = (int) Math.round(i * elementsInQuantile);
			groups[i][1] = (int) Math.round((i+1) * elementsInQuantile);
		}
		// For safety reasons only
		groups[0][0] = 0;
		groups[numberOfClasses-1][1] = values.size();
		//printGroupsArray("Grupos", groups);

		double sdam = computeSADM(values);
		//System.out.println("SDAM: " + sdam);
		boolean changeOccurred = false;
		do {
			changeOccurred = false;
			for (int i=0; i<(numberOfClasses-1); i++) {
				// Bajo
				groupsPlus = changeGroup(groups, i, 1); 
				//printGroupsArray("Grupos plus", groupsPlus);
				// Subo
				groupsMinus = changeGroup(groups, i, -1);
				//printGroupsArray("Grupos minus", groupsMinus);
				// Me quedo con la mejor de las tres
				double gvf = computeGVF(values, groups, sdam);
				
				double gvfPlus = computeGVF(values, groupsPlus, sdam);
				double gvfMinus = computeGVF(values, groupsMinus, sdam);
				if (!((gvf <= gvfMinus) && (gvf <= gvfPlus))) {
					if (gvfPlus <= gvfMinus) {
						groups = groupsPlus;
					}
					else {
						groups = groupsMinus;
					}
					changeOccurred = true; 
				}
				//System.out.print("Clase: " + i + " GVF " + gvf);
				//System.out.print(" GVFPlus " + gvfPlus);
				//System.out.println(" GVFMinus " + gvfMinus);
				//printGroupsArray("Grupos", groups);
			}
		} while (changeOccurred);
		
		List valueList = new ArrayList(values);
		Set resultValues = new TreeSet();
		for (int i = 0; i < numberOfClasses; i++) {
			resultValues.add(valueList.get(groups[i][0]));
		}
		return resultValues;
		
	}
	
	private double computeSADM(SortedSet values) {
		double result;
		double average;
		Iterator valuesIterator;
		
		result = 0;
		average = average(values, 0, values.size());
		valuesIterator = values.iterator();
		while (valuesIterator.hasNext()) {
			result = result + Math.pow(Double.parseDouble(valuesIterator.next().toString()) - average, 2); 
		}
		return result;
	}
	
	private int[][] changeGroup(int[][] groups, int groupNumber, int direction) {
		int [][]result;
		result = new int[groups.length][2];
		for (int i=0; i<groups.length; i++) {
			result[i][0] = groups[i][0];
			result[i][1] = groups[i][1]; 			
		}
		result[groupNumber][1] = result[groupNumber][1] + direction;
		result[groupNumber+1][0] = result[groupNumber+1][0] + direction;
		return result;
	}
	
	private double computeGVF(SortedSet values, int[][] groups, double sdam) {
		
		double[] variances = computeVariance(values, groups);
		double sum = 0;
		for (int i=0; i< groups.length; i++) {
			sum = sum + variances[i];  			
		}
		return sum / sdam;
	}
	
	private double[] computeVariance(SortedSet values, int[][] groups) {
		
		List valueList = new ArrayList(values);
		double[] centroids = computeCentroids(values, groups);
		double[] variance = new double[groups.length];
		for (int i=0; i<groups.length; i++) {
			double sum = 0;
			for (int j=groups[i][0]; j<groups[i][1]; j++) {
				sum = sum + Math.pow(Double.parseDouble(valueList.get(j).toString()) - centroids[i], 2);  
			}
			variance[i] = sum; 
		}
		return variance;
	}
	
	
	private double[] computeCentroids(SortedSet values, int[][] groups) {
		double[] centroids;
		
		centroids = new double[groups.length];
		for (int i=0; i<groups.length; i++) {
			centroids[i] = average(values, groups[i][0], groups[i][1]);
		}
		return centroids;
	}
	
	private double average(SortedSet values, int bottom, int top) {
		
		List valueList = new ArrayList(values);
		
		double sum = 0; 
		for (int i=bottom; i<top; i++) {
			sum = sum + Double.parseDouble(valueList.get(i).toString());
		}
		return sum / (top-bottom); 
	}
	
	private void printArray(String name, double[] array) {
		System.out.print(name +": { ");
		for (int i=0; i<(array.length -1);i++) {
			System.out.print(array[i] + ", ");
		}
		System.out.println(array[array.length-1] + " }");
	}

	private void printGroupsArray(String name, int[][] array) {
		System.out.print(name +": { ");
		for (int i=0; i<(array.length -1);i++) {
			System.out.print("(" + array[i][0] + "," + array[i][1] +"), ");
		}
		System.out.println("(" + array[array.length-1][0] + "," + array[array.length-1][1] +")}");
	}
}
