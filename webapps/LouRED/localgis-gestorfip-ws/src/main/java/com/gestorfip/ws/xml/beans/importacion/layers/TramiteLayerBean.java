package com.gestorfip.ws.xml.beans.importacion.layers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gestorfip.ws.xml.beans.importacion.migracionasistida.DeterminacionLayerBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.adscripcion.AdscripcionBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.aplicacionambito.AplicacionAmbitoBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.condicionurbanistica.CondicionUrbanisticaBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.determinacion.DeterminacionBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.documento.DocumentoBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.entidad.EntidadBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.operacion.OperacionDeterminacionBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.operacion.OperacionEntidadBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.unidad.UnidadBean;



public class TramiteLayerBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7761285713493365599L;

	private String codigo;
	private String texto;
	
	private DeterminacionLayerBean[] lstDeterminaciones;

	public DeterminacionLayerBean[] getLstDeterminaciones() {
		return lstDeterminaciones;
	}

	public void setLstDeterminaciones(
			DeterminacionLayerBean[] lstDeterminaciones) {
		this.lstDeterminaciones = lstDeterminaciones;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

}
