package com.geopista.editor;

import java.util.ArrayList;
import java.util.HashMap;

import com.vividsolutions.jump.workbench.model.Layerable;

public class ActualLayers {
	
	private ArrayList capasActual=new ArrayList();
	
	private HashMap categoryList=new HashMap();
	
	
	public ActualLayers(){
		
	}

	public void addLayer(String category,Layerable layer){
		categoryList.put(layer.getName(),category);
		capasActual.add(layer);
	}
	
	public ArrayList getLayers(){
		return capasActual;
	}

	public String getCategory(String name) {
		return (String)categoryList.get(name);
	}
	
	
}

