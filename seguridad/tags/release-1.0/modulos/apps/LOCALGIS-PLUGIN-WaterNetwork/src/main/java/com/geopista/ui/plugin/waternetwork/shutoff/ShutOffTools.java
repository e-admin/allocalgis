package com.geopista.ui.plugin.waternetwork.shutoff;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.feature.Feature;

public class ShutOffTools{
	
	//Algoritmo que encuentra las valvulas que, cerrandolas, cortan el flujo que
	//llega a un numero de policia.
	public ArrayList<Feature> shutOffAlg(ArrayList<Feature> pipes, Feature house){			
		ArrayList<Feature> valvesFind = valvesFind(house,pipes,new ArrayList<Feature>());
		return valvesFind;
	}
	
	//Devuelve las valvulas más cercanas a un numero de policia.
	public ArrayList<Feature> valvesFind(Feature next, ArrayList<Feature> pipes, ArrayList<Feature> valves){		
		pipes.remove(next);
		for(Feature pipe : siguientes(next,pipes)){
			if(esValvula(pipe)){
				pipes.remove(pipe);
				valves.add(pipe);
				valvesFind(next,pipes,valves);
			}else
				valvesFind(pipe,pipes,valves);	
		}
		return valves;
	}
	
	//Devuelve las features consecutivas a una feature dada.
	public ArrayList<Feature> siguientes(Feature feature, ArrayList<Feature> features){
		ArrayList<Feature> aux = new ArrayList<Feature>();
		for(Feature f : features)
			if(feature.getAttribute("node2").equals(f.getAttribute("node1"))
					||feature.getAttribute("node2").equals(f.getAttribute("node2"))
					||feature.getAttribute("node1").equals(f.getAttribute("node1"))
					||feature.getAttribute("node1").equals(f.getAttribute("node2")))
				aux.add(f);
		return aux;
	}
	
	//Partiende de un punto y una lista de features, devuelve la feature
	//que esté más cerca de dicho punto.
	public Feature pointNear(Point point, ArrayList<Feature> features){
		double distanceMax = 0;
		for(Feature feature : features){
			double dist = point.distance(feature.getGeometry());
			if(dist>distanceMax) distanceMax = dist;
		}
		double distanceMin = distanceMax;
		for(Feature feature : features){
			double dist = point.distance(feature.getGeometry());
			if(dist<distanceMin) distanceMin = dist;
		}
		for(Feature feature : features)
			if(point.distance(feature.getGeometry())==distanceMin)
				return feature;
		return null;
	}	
	
	//Elimina las valvulas cerradas que no tienen utilidad, es decir, si el cierre de una
	//valvula no repercute en el funcionamiento de la red se vuelve a abrir.
	public ArrayList<Feature> optimizeV(ArrayList<Feature> valves, ArrayList<Feature> pipes){
		ArrayList<Feature> finalV = new ArrayList<Feature>();
		ArrayList<Feature> pipes2 = new ArrayList<Feature>();
		for(Feature valve : valves){
			pipes2.addAll(pipes);	
			valvesCloseF(valve,pipes2,valves);
			pipes2.clear();
			if(embalse!=0)finalV.add(valve);
			embalse=0;
		}
		return finalV;
	}	
	
	private int embalse = 0;
	
	//Encuentra las válvulas cerradas que no tienen como destino un embalse.
	public void valvesCloseF(Feature next,ArrayList<Feature> pipes,ArrayList<Feature> valves){			
		if(esValvulaCerrada(next,valves))pipes.remove(next);
		for(Feature pipe : siguientes(next,pipes)){
			if(esEmbalse(pipe,pipes))
				embalse++;
			else if(esValvulaCerrada(pipe,valves))
				continue;
			else{ 
				pipes.remove(pipe);				
				valvesCloseF(pipe,pipes,valves);
			}
		}
	}
	
	//Devuelve true si la feature es una valvula cerrada.
	public boolean esValvulaCerrada(Feature pipe,ArrayList<Feature> valves){
		if(valves.contains(pipe))return true;
		return false;
	}
	
	//Devuelve true si la feature es de tipo embalse.
	public boolean esEmbalse(Feature pipe, ArrayList<Feature> pipes){		
		if(pipe.getAttribute("tipo").equals("r") || auxEmbalse(pipe,pipes))return true;
		return false;
	}
	
	public boolean auxEmbalse(Feature pipe, ArrayList<Feature> pipes){
		if(pipe.getAttribute("tipo").equals("a")){
			Coordinate first = pipe.getGeometry().getCoordinates()[0];
			Coordinate last  = pipe.getGeometry().getCoordinates()[pipe.getGeometry().getCoordinates().length-1];
			for(Feature p : pipes)
				if((!p.equals(pipe) && p.getAttribute("tipo").equals("r")) &&
						(p.getGeometry().getCoordinates()[0].equals(first)
								|| p.getGeometry().getCoordinates()[0].equals(last)
								|| p.getGeometry().getCoordinates()[p.getGeometry().getCoordinates().length-1].equals(first)
								|| p.getGeometry().getCoordinates()[p.getGeometry().getCoordinates().length-1].equals(last)))
					return true;
			
		}
		return false;
	}
	
	//Devuelve true si la feature es de tipo valvula.
	public boolean esValvula(Feature pipe){
		if(pipe.getAttribute("tipo").equals("v"))return true;
		return false;
	}
	
	//Devuelve true si la feature es de tipo numero de policia.
	public boolean esEdificio(Feature pipe){
		if(pipe.getAttribute("tipo").equals("h"))return true;
		return false;
	}
	
	//Algoritmo que encuentra los edificios afectados por el cierre de una 
	//determinada valvula.
	public ArrayList<Feature> shutOffValve(Feature valve, ArrayList<Feature> pipes){
		ArrayList<Feature> housesFind = houseAffected(valve,pipes);		
		return housesFind;
	}
	
	//Comprueba si las features consecutivas a la valvula llegan a un embalse, si no
	//llega, busca los edificios que hay en esa subred.
	public ArrayList<Feature> houseAffected(Feature valve, ArrayList<Feature> features){
		ArrayList<Feature> houses = new ArrayList<Feature>();
		ArrayList<Feature> pipes = new ArrayList<Feature>();
		ArrayList<Feature> pipesL = new ArrayList<Feature>();
		features.remove(valve);
		ArrayList<Feature> siguientes = siguientes(valve,features);		
		pipes.addAll(features);pipesL.addAll(features);
		for(Feature siguiente : siguientes){
			pipes.clear();pipes.addAll(features);			
			hayEmbalse(siguiente,pipes);
			if(embalse==0){
				pipesL.clear();pipesL.addAll(features);
				pipesL.remove(siguiente);
				houses=houseList(siguiente,pipesL,new ArrayList<Feature>());
			}
			embalse=0;
		}
		return houses;
	}
	
	//Devuelve la lista de edificios de una subred.
	public ArrayList<Feature> houseList(Feature next, ArrayList<Feature> pipes, ArrayList<Feature> houses){
		pipes.remove(next);
		for(Feature pipe : siguientes(next,pipes)){
			if(esEdificio(pipe))
				houses.add(pipe);
			houseList(pipe,pipes,houses);
		}
		return houses;
	}
	
	//Comprueba que desde una feature dada llega a un embalse.
	public void hayEmbalse(Feature feature,ArrayList<Feature> features){
		for(Feature f : siguientes(feature,features)){
			if(esEmbalse(f,features))
				embalse++;
			else{ 
				features.remove(f);				
				hayEmbalse(f,features);
			}
		}		
	}
}
