package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class RedDistribucionEIEL extends WorkflowEIEL implements Serializable, EIELPanel{
	
	public RedDistribucionEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código provincia","codprov","eiel_c_abast_rd",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_c_abast_rd",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_c_abast_rd",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Entidad","codentidad","eiel_c_abast_rd",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Núcleo","codpoblamiento","eiel_c_abast_rd",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Tramo","tramo_rd","eiel_c_abast_rd",""));
	}	

	public Hashtable getIdentifyFields() {
		return null;
	}
	
	
}
