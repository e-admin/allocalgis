package com.geopista.app.eiel.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.LocalGISEIELClient;
import com.geopista.app.eiel.beans.WorkflowEIEL;
import com.geopista.app.eiel.beans.filter.LCGNucleoEIEL;

public abstract class EIELTableModel extends DefaultTableModel{

	 /**
	 * 
	 */
	private static final long serialVersionUID = -6062337709320798104L;

    private AppContext aplicacion;
	
	public abstract WorkflowEIEL getValueAt(int row);
	public abstract void setData (ArrayList datos);

	 
	 
	public String getNombreNucleo(String codentidad,String codnucleo){
		 
		 String nombreNucleo=codentidad;
		 aplicacion= (AppContext) AppContext.getApplicationContext();

		 HashMap map=(HashMap)aplicacion.getBlackboard().get("HASH_NUCLEOS_MUNICIPIO");
		 
		 String claveBusqueda=codentidad+"_"+codnucleo;
		 
		 LCGNucleoEIEL nucleoEIEL= (LCGNucleoEIEL)map.get(claveBusqueda);
		 if (nucleoEIEL!=null){
			 nombreNucleo="("+claveBusqueda+") "+nucleoEIEL.getDenominacion() ;
		 }
		 
		 return nombreNucleo;
	 }
}
