package es.satec.localgismobile.core;

import java.util.ArrayList;
import java.util.Vector;

public class Application {

	private String name;
	private String layerId;
	//private String keyAttribute;
	private ArrayList keyAttribute;
	private Vector options;

	public Application() {
		
	}

	public Application(String name, String layerId, ArrayList keyAttribute, Vector options) {
		this.name = name;
		this.layerId = layerId;
		this.keyAttribute = keyAttribute;
		this.options = options;
	}

	public ArrayList getKeyAttribute() {
		return keyAttribute;
	}

	public void setKeyAttribute(ArrayList keyAttribute) {
		this.keyAttribute = keyAttribute;
	}

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Vector getOptions() {
		return options;
	}
	
	public void setOptions(Vector options) {
		this.options = options;
	}
}
