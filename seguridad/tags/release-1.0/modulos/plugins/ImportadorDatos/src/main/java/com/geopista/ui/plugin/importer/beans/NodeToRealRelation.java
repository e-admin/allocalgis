package com.geopista.ui.plugin.importer.beans;


public class NodeToRealRelation {	
	
	NodoDominio nodoDominio;
	Object realValue;
	
	public NodeToRealRelation ()
	{		
	}
	
	public NodeToRealRelation (NodoDominio nodoDominio, Object realValue)
	{		
		this.nodoDominio = nodoDominio;
		this.realValue = realValue;
	}

	public NodoDominio getNodoDominio() {
		return nodoDominio;
	}

	public void setNodoDominio(NodoDominio nodoDominio) {
		this.nodoDominio = nodoDominio;
	}

	public Object getRealValue() {
		return realValue;
	}

	public void setRealValue(Object realValue) {
		this.realValue = realValue;
	}	

}
