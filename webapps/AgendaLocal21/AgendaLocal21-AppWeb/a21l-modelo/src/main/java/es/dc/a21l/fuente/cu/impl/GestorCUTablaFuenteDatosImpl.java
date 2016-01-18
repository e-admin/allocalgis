/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu.impl;

import java.util.List;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.fuente.cu.GestorCUTablaFuenteDatos;
import es.dc.a21l.fuente.cu.TablaFuenteDatosDto;
import es.dc.a21l.fuente.modelo.AtributoFuenteDatos;
import es.dc.a21l.fuente.modelo.AtributoFuenteDatosRepositorio;
import es.dc.a21l.fuente.modelo.TablaFuenteDatos;
import es.dc.a21l.fuente.modelo.TablaFuenteDatosRepositorio;

/**
 *
 * @author Balidea Consulting & Programming
 */
public class GestorCUTablaFuenteDatosImpl implements GestorCUTablaFuenteDatos{
	private static final Logger log = LoggerFactory.getLogger(GestorCUTablaFuenteDatosImpl.class);
	private Mapper mapper;
    private TablaFuenteDatosRepositorio tablaFuenteDatosRepositorio;
    private AtributoFuenteDatosRepositorio atributoFuenteDatosRepositorio;
    
    @Autowired
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }
    @Autowired
    public void setTablaDatosFuenteRepositorio(TablaFuenteDatosRepositorio tablaFuenteDatosRepositorio) {
        this.tablaFuenteDatosRepositorio = tablaFuenteDatosRepositorio;
    }
    
    @Autowired
    public void setAtributoDatosFuenteRepositorio(AtributoFuenteDatosRepositorio atributoFuenteDatosRepositorio) {
        this.atributoFuenteDatosRepositorio = atributoFuenteDatosRepositorio;
    }
    
    public EncapsuladorListSW<TablaFuenteDatosDto> cargaTodosPorIndicadorYFuente(Long idIndicador, Long idFuente) {
        List<TablaFuenteDatos> listaTablas = tablaFuenteDatosRepositorio.cargaTodosPorIndicadorYFuente(idIndicador,idFuente);
        EncapsuladorListSW<TablaFuenteDatosDto> result = new EncapsuladorListSW<TablaFuenteDatosDto>();
        TablaFuenteDatos2TablaFuenteDatosDtoTransformer transformer = new TablaFuenteDatos2TablaFuenteDatosDtoTransformer(mapper);
        for (TablaFuenteDatos tabla: listaTablas)
        	result.add(transformer.transform(tabla));
        return result;
    }
    
    public TablaFuenteDatosDto cargaPorIndicadorYFuenteYTabla(Long idIndicador, Long idFuente, String nombreTabla) {
        TablaFuenteDatos tabla = tablaFuenteDatosRepositorio.cargaPorIndicadorYFuenteYTabla(idIndicador,idFuente, nombreTabla);
        TablaFuenteDatosDto result = new TablaFuenteDatosDto();
        TablaFuenteDatos2TablaFuenteDatosDtoTransformer transformer = new TablaFuenteDatos2TablaFuenteDatosDtoTransformer(mapper);
        result = transformer.transform(tabla);
        return result;
    }
    
    public TablaFuenteDatosDto carga(Long id) {
        return new TablaFuenteDatos2TablaFuenteDatosDtoTransformer(mapper).transform(tablaFuenteDatosRepositorio.carga(id));
    }

    public TablaFuenteDatosDto borra(Long id, EncapsuladorErroresSW erros) {
        TablaFuenteDatosDto tablaFuenteDatosDto = carga(id);
        
        if(!atributoFuenteDatosRepositorio.existenAtributosDeTabla(id)){
        	tablaFuenteDatosRepositorio.borra(id);
        }
        return tablaFuenteDatosDto;
    }
    
    public TablaFuenteDatosDto guarda(TablaFuenteDatosDto tablaFuenteDatosDto, EncapsuladorErroresSW erros) {
        //Validador<FuenteDto> fuenteDtoValidador = new FuenteDtoValidador();
        //tablaFuenteDatosValidador.valida(tablaFuenteDatosDto, erros);
        if (erros.getHashErrors()) return null;

        TablaFuenteDatos tabla = new TablaFuenteDatosDto2TablaFuenteDatosTransformer(mapper, tablaFuenteDatosRepositorio).transform(tablaFuenteDatosDto);
        
        TablaFuenteDatos tabla2 = tablaFuenteDatosRepositorio.guarda(tabla);

        return new TablaFuenteDatos2TablaFuenteDatosDtoTransformer(mapper).transform(tabla2);
    }
    
    public TablaFuenteDatosDto cargaFuentePorIndicadorYNombreYFuente(Long idIndicador, Long idFuente, String nombre){
    	TablaFuenteDatos temp= tablaFuenteDatosRepositorio.cargaFuentePorIndicadorYNombreYFuente(idIndicador, idFuente, nombre);
    	if(temp==null)
    		return null;
    	return new TablaFuenteDatos2TablaFuenteDatosDtoTransformer(mapper).transform(temp);
    }
}