/**
 * Via.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.georeference.beans;



import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.vividsolutions.jts.geom.Point;

/**
 * @author rubengomez
 * 
 * Bean que almacena todos los datos referentes a cada linea del archivo csv
 * para georreferenciación
 * Variables    TIPO		NOMBRE
 *				String 		nombre	
 * 				String 		direccion				
 * 				String 		calle
 * 				int	   		numeroPolicia
 * 				Hashtable	data // Todos los datos de la fila
 * 				ArrayList	listaCoincidencias // Respuesta del geocoder. Tipo PoliciaCoincidencias.
 */
public class Via {
	
   private String nombre;
   private String direccion;
   private String calle;
   private String numeroPolicia;
   private Hashtable data;
   private ArrayList listaCoincidencias;// Guarda elementos de tipo PoliciaCoincidencias;

    public Via( String nombre, String calle,String numeroPolicia,Hashtable data, ArrayList listaCoincidencias) {    	
        this.nombre = nombre;
        this.calle = calle;
        this.numeroPolicia = numeroPolicia;
        this.data = data;
        this.listaCoincidencias=listaCoincidencias;
    }
    public Via(String nombre,String direccion){
    	this.nombre = nombre;
    	this.direccion = direccion;
    }
    public Via(Hashtable datos){
    	this.data = datos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

	public String getCalle() {
		return calle;
	}


	public void setCalle(String calle) {
		this.calle = calle;
	}


	public ArrayList getListaCoincidencias() {
		return listaCoincidencias;
	}

	public void removeListaCoincidencias(){
		this.listaCoincidencias.clear();
	}
	public void addListaCoincidencias(PoliciaCoincidencias nuevaCoincidencia)
	    {
	        if (listaCoincidencias==null) 
	        	listaCoincidencias=new ArrayList();
	        listaCoincidencias.add(nuevaCoincidencia);
	    }
	public void setData(Object key,Object value){
		this.data.put(key, value);
	}
	public Object getData(Object key){
		return this.data.get(key);
	}
	public Enumeration getKeys(){
		return this.data.keys();
	}
	public String getDireccion() {
		return direccion;
	}
	public String getNumeroPolicia() {
		return numeroPolicia;
	}
	/*
	 * Indica si hay mas de una coincidencia proveniente del GeoCoder.
	 */
	public boolean isListaMultiple(){
		if (this.listaCoincidencias.size() > 1){
			return true;
		}
		return false;
	}
	/*
	 * Indica si no hay coincidencias del GeoCoder.
	 */
	public boolean isNotLista(){
		return this.listaCoincidencias.isEmpty();
	}
	public void dropListaCoincidencias(){
		listaCoincidencias.clear();
	}
	public Hashtable getData(){
		return this.data;
	}
	public Point getFirstGeometry(){
		return ((PortalGeorreferenciado)((PoliciaCoincidencias)listaCoincidencias.get(0)).getDatos().get(0)).getGeometria();
	}
}
