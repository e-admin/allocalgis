/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu.impl;


import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.cu.impl.EstrategiaCreacionEntidadRepositorio;
import es.dc.a21l.base.cu.impl.TransformadorDtoBase2EntidadBase;
import es.dc.a21l.fuente.cu.FuenteDto;
import es.dc.a21l.fuente.modelo.CaracterSeparadorRepositorio;
import es.dc.a21l.fuente.modelo.Fuente;
import es.dc.a21l.fuente.modelo.FuenteRepositorio;
import es.dc.a21l.fuente.modelo.TipoCodificacionRepositorio;


public class FuenteDto2FuenteTransformer extends  TransformadorDtoBase2EntidadBase<FuenteDto, Fuente>  {
    CaracterSeparadorRepositorio caracterSeparadorRepositorio;
    TipoCodificacionRepositorio tipoCodificacionRepositorio;
	public FuenteDto2FuenteTransformer(Mapper mapper, FuenteRepositorio fuenteRepositorio,CaracterSeparadorRepositorio caracteSeparadorRepositorio,TipoCodificacionRepositorio tipoCodificacionRepositorio) {
        super(mapper);
        this.caracterSeparadorRepositorio=caracteSeparadorRepositorio;
        this.tipoCodificacionRepositorio=tipoCodificacionRepositorio;
        setEstrategiaCreacion(new EstrategiaCreacionEntidadRepositorio<FuenteDto, Fuente>(fuenteRepositorio, this));
        
    }
	@Override
	protected void aplicaPropiedadesEstendidas(FuenteDto origen, Fuente destino) {
		super.aplicaPropiedadesEstendidas(origen, destino);
		if(origen.getIdCaracterSeparador()!=null && origen.getIdCaracterSeparador()!=0){
			destino.setCaracterSeparador(caracterSeparadorRepositorio.carga(origen.getIdCaracterSeparador().longValue()));
		}else{
			destino.setCaracterSeparador(null);
		}
		if(origen.getIdTipoCodificacion()!=null && origen.getIdTipoCodificacion()!=0){
			destino.setTipoCodificacion(tipoCodificacionRepositorio.carga(origen.getIdTipoCodificacion()));
		}else{
			destino.setCaracterSeparador(null);
		}
	}

}