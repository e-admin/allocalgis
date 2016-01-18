package es.satec.localgismobile.ui.utils.impl;

public abstract class ItemNode {

	
	String name;
	
	public ItemNode (String name)  {
		this.name =name;
	}
		 
	
	public abstract void setContent (String value) ;
		
	 
}
