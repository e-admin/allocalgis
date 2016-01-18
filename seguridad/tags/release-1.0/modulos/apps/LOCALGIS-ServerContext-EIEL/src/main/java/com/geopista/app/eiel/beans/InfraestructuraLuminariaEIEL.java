package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class InfraestructuraLuminariaEIEL extends WorkflowEIEL implements Serializable, EIELPanel{
	
	public InfraestructuraLuminariaEIEL(){

		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código provincia","codprov","eiel_c_alum_pl",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_c_alum_pl",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_c_alum_pl",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Entidad","codentidad","eiel_c_alum_pl",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Núcleo","codpoblamiento","eiel_c_alum_pl",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Orden","orden_pl","eiel_c_alum_pl",""));
	}	

	public Hashtable getIdentifyFields() {
		return null;
	}
	
	
}
