/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.publicacion.cu.impl;

import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.publicacion.cu.GestorCUPublicacion;
import es.dc.a21l.publicacion.cu.PublicacionDto;
import es.dc.a21l.publicacion.modelo.Publicacion;
import es.dc.a21l.publicacion.modelo.PublicacionRepositorio;

public class GestorCUPublicacionImpl implements GestorCUPublicacion{
	private Mapper mapper;
    private PublicacionRepositorio publicacionRepositorio;
    
    @Autowired
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }
    @Autowired
    public void setPublicacionRepositorio(PublicacionRepositorio publicacionRepositorio) {
        this.publicacionRepositorio = publicacionRepositorio;
    }
    
	public PublicacionDto guardar(PublicacionDto publicacionDto, EncapsuladorErroresSW errores) {
		Publicacion publicacion = new PublicacionDto2PublicacionTransformer(mapper, publicacionRepositorio).transform(publicacionDto);
		publicacion = publicacionRepositorio.guardar(publicacion, errores);
		if (errores.getHashErrors()) return null;
		return new Publicacion2PublicacionDtoTransformer(mapper).transform(publicacion);
	}
	
	public PublicacionDto obtenerEstado() {
		List<Publicacion> listaPublicacion = publicacionRepositorio.cargaTodos();
		PublicacionDto publiDto = new PublicacionDto();
		for ( Publicacion publi : listaPublicacion ) {
			publiDto = new Publicacion2PublicacionDtoTransformer(mapper).transform(publi);
			break;
		}
		return publiDto;
	}
}