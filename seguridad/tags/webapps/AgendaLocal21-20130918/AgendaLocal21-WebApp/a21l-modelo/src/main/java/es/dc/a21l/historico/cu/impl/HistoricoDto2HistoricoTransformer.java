/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.historico.cu.impl;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.EstrategiaCreacionEntidadRepositorio;
import es.dc.a21l.base.cu.impl.TransformadorDtoBase2EntidadBase;
import es.dc.a21l.historico.cu.HistoricoDto;
import es.dc.a21l.historico.modelo.Historico;
import es.dc.a21l.historico.modelo.HistoricoRepositorio;


public class HistoricoDto2HistoricoTransformer extends  TransformadorDtoBase2EntidadBase<HistoricoDto, Historico>  {
    
	public HistoricoDto2HistoricoTransformer(Mapper mapper, HistoricoRepositorio historicoRepositorio) {
        super(mapper);
        setEstrategiaCreacion(new EstrategiaCreacionEntidadRepositorio<HistoricoDto, Historico>(historicoRepositorio, this));
    }
	
	@Override
	protected void aplicaPropiedadesEstendidas(HistoricoDto origen,	Historico destino) {
		super.aplicaPropiedadesEstendidas(origen, destino);
		destino.setDatos(origen.getDatosDto());
		destino.setMapa(origen.getMapaDto());
		destino.setFecha(stringToDate(origen.getStrFecha()));
	}
	public Date stringToDate(String strFecha){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sdf.setLenient(false);
			Date fecha;
			fecha = sdf.parse(strFecha);
			return fecha;
		}catch (Exception e) {
			return null;
		}
	}
}