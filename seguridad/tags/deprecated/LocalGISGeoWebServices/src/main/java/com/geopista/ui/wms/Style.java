package com.geopista.ui.wms;

public class Style {
private String name;
private String title;


/**Constructor
 * @param name Nombre del estilo.
 * @param title Título del estilo.
 */
public Style(){
}



/**Getters y setters*/

public String getName() {
	return name;
}


public void setName(String name) {
	this.name = name;
}


public String getTitle() {
	return title;
}


public void setTitle(String title) {
	this.title = title;
}

/**fin getters y setters*/


public String toString(){
	return "Nombre: "+name+", título: "+title;
}



}//fin de la clase
