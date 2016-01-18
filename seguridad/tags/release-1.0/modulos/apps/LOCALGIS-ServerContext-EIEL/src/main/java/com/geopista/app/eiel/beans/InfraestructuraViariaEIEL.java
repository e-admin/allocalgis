package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class InfraestructuraViariaEIEL extends WorkflowEIEL implements Serializable, EIELPanel{
	
	public InfraestructuraViariaEIEL(){
		
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código provincia","codprov","eiel_c_redviaria_tu",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_c_redviaria_tu",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_c_redviaria_tu",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Entidad","codentidad","eiel_c_redviaria_tu",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Núcleo","codpoblamiento","eiel_c_redviaria_tu",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código tramo viario","tramo_vu","eiel_c_redviaria_tu",""));
		
	}	

	public Hashtable getIdentifyFields() {
		return null;
	}
	
	
}
