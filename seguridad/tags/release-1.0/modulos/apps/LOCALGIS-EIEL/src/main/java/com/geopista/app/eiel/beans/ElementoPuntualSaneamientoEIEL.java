package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class ElementoPuntualSaneamientoEIEL extends WorkflowEIEL implements Serializable, EIELPanel{
	
	public ElementoPuntualSaneamientoEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código provincia","codprov","eiel_c_saneam_pr",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_c_saneam_pr",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_c_saneam_pr",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Orden","orden_pr","eiel_c_saneam_pr",""));
	}	

	public Hashtable getIdentifyFields() {
		return null;
	}
	
	
}
