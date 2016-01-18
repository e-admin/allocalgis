package com.gestorfip.ws.xml.beans.importacion.layers;

import java.io.Serializable;


/**
 * The purpose of this class is to offer support to parse and load the FIP1 file
 * data into the database exploited by the GestorFip.
 * This class represents the content of the FIP1 file. When parsing the FIP1
 * file, the SAX parser populates the different attributes of the FIP1Bean with
 * the XML data.
 * The FIP1Bean possess various methods to load its data into the database.
 * 
 * @author davidriou
 *
 */
public class FIP1LayersBean implements Serializable {

	private static final long serialVersionUID = -2685400793754882320L;
	
	private DiccionarioLayerBean diccionario;
	private CatalogoSistematizadoLayerBean catalogosistematizado;

	public CatalogoSistematizadoLayerBean getCatalogosistematizado() {
		return catalogosistematizado;
	}

	public void setCatalogosistematizado(
			CatalogoSistematizadoLayerBean catalogosistematizado) {
		this.catalogosistematizado = catalogosistematizado;
	}
	
	public DiccionarioLayerBean getDiccionario() {
		return diccionario;
	}

	public void setDiccionario(DiccionarioLayerBean diccionario) {
		this.diccionario = diccionario;
	}

}
