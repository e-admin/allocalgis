package com.geopista.ui.plugin.georeference.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;


/**
 * 
 * Lista de calles del GEOCODER
 * Contiene la calle y el tipo, y un arrayList de PortalGeorreferenciado
 */
public class PoliciaCoincidenciasCatastro {
	
    private String calle;
    private String tipo;
    private ArrayList datos;

    public PoliciaCoincidenciasCatastro(String calle,String tipo)
    {
        this.calle=calle;
        this.tipo=tipo;
        datos = new ArrayList();
        
    }
    /*
     * Genera una entrada en el Hashtable con clave el portal y con valor
     * la geometria
     */
    public void setDatos(String numeroPolicia, double coordenadaX,double coordenadaY){
    	
    	datos.add(new PortalGeorreferenciadoCatastro(numeroPolicia, new GeometryFactory().createPoint(new Coordinate(coordenadaX,coordenadaY))));
    }
    public void setDatos(String numeroPolicia,Point geometria){
    	if (numeroPolicia != "" && numeroPolicia != null && geometria != null)
    		datos.add(new PortalGeorreferenciadoCatastro(numeroPolicia,geometria));
    		
    }
    public Point getDatos(String numeroPolicia){
    	if (datos == null || datos.size() == 0)
    		return null;
    	Iterator recorrePortales = datos.iterator();
    	PortalGeorreferenciadoCatastro datoPortal = null;
    	while(recorrePortales.hasNext()){
    		datoPortal = (PortalGeorreferenciadoCatastro)recorrePortales.next();
    		if (datoPortal.getPortal() == numeroPolicia)
    			return datoPortal.getGeometria();
    	}
    	return null;
    	
    }
    public ArrayList getDatos(){
		return datos;
    	
    }
    /*
     * Elimina todos los datos del arrayList y la rellena sólo con el 
     * dato seleccionado.
     */
    public void sustituyeDatos(String key){
    	if (datos != null && datos.size() == 0){
	    	Iterator recorrePortales = datos.iterator();
	    	PortalGeorreferenciadoCatastro datoPortal = null;
	    	while(recorrePortales.hasNext()){
	    		datoPortal = (PortalGeorreferenciadoCatastro)recorrePortales.next();
	    		if (datoPortal.getPortal() == key){
	    			PortalGeorreferenciadoCatastro nuevoPortal = new PortalGeorreferenciadoCatastro(datoPortal.getPortal(),datoPortal.getGeometria());
	    			datos.clear();
	    			datos.add(nuevoPortal);
	    			break;
	    		}
	
	    	}
    	}
    	
    }
    
	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public void setDatos(ArrayList newData){
		datos = newData;
		
	}

}
