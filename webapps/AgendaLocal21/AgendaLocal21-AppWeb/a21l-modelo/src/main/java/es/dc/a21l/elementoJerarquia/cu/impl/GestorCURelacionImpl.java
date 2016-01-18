/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.elementoJerarquia.cu.GestorCURelacion;
import es.dc.a21l.elementoJerarquia.cu.RelacionDto;
import es.dc.a21l.elementoJerarquia.modelo.Relacion;
import es.dc.a21l.elementoJerarquia.modelo.RelacionRepositorio;

public class GestorCURelacionImpl implements GestorCURelacion {

	
	private Mapper mapper;
	private RelacionRepositorio relacionRepositorio;
	
	@Autowired
	public void setRelacionRepositorio(RelacionRepositorio relacionRepositorio) {
		this.relacionRepositorio = relacionRepositorio;
	}

	@Autowired
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}


	public RelacionDto cargaPorId(Long id){
		return new Relacion2RelacionDtoTransformer(mapper).transform(relacionRepositorio.carga(id));
	}
	
	public RelacionDto guardar(RelacionDto relacionDto,EncapsuladorErroresSW errores){
		
		if(errores.getHashErrors())
			return null;
		
		RelacionDto2RelacionTransformer transformer = new RelacionDto2RelacionTransformer(mapper, relacionRepositorio);
		Relacion relacion = relacionRepositorio.guarda(transformer.transform(relacionDto));
		return new Relacion2RelacionDtoTransformer(mapper).transform(relacion);
	}
	
	public RelacionDto borra(Long id) {
        RelacionDto relacionDto = cargaPorId(id);
        relacionRepositorio.borra(relacionDto.getId());
        return relacionDto;
    }
	
	public void borraPorIdIndicador(Long id) {
        List<Relacion> lista = new ArrayList<Relacion>();
        lista = relacionRepositorio.cargaPorIdIndicador(id);
        for ( Relacion rela : lista ) {
        	relacionRepositorio.borra(rela.getId());
        }
    }
}
