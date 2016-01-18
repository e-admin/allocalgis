/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorIntegerSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorLongSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorStringSW;
import es.dc.a21l.base.utils.enumerados.TipoAtributoFD;
import es.dc.a21l.base.utils.enumerados.TiposFuente;
import es.dc.a21l.elementoJerarquia.cu.GestorCUIndicador;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.GestorCUAtributoFuenteDatos;
import es.dc.a21l.fuente.cu.GestorCUFuente;
import es.dc.a21l.fuente.cu.GestorCUTablaFuenteDatos;
import es.dc.a21l.fuente.cu.TablaFuenteDatosDto;
import es.dc.a21l.fuente.cu.ValorFDDto;
import es.dc.a21l.fuente.modelo.AtributoFuenteDatos;
import es.dc.a21l.fuente.modelo.AtributoFuenteDatosRepositorio;

/**
 *
 * @author Balidea Consulting & Programming
 */
public class GestorCUAtributoFuenteDatosImpl implements GestorCUAtributoFuenteDatos{
	private static final Logger log = LoggerFactory.getLogger(GestorCUAtributoFuenteDatosImpl.class);
	
	private static final String patronTrocearExpresion="||";
	private Mapper mapper;
    private AtributoFuenteDatosRepositorio atributoFuenteDatosRepositorio;
    private GestorCUFuente gestorCUFuente;
    private GestorCUTablaFuenteDatos gestorCUTablaFuenteDatos;
    private GestorCUIndicador gestorCUIndicador;
    
    
    @Autowired
    public void setGestorCUIndicador(GestorCUIndicador gestorCUIndicador) {
		this.gestorCUIndicador = gestorCUIndicador;
	}

	@Autowired
    public void setGestorCUTablaFuenteDatos(
			GestorCUTablaFuenteDatos gestorCUTablaFuenteDatos) {
		this.gestorCUTablaFuenteDatos = gestorCUTablaFuenteDatos;
	}

	@Autowired
    public void setGestorCUFuente(GestorCUFuente gestorCUFuente) {
		this.gestorCUFuente = gestorCUFuente;
	}
    
	@Autowired
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }
    @Autowired
    public void setAtributoDatosFuenteRepositorio(AtributoFuenteDatosRepositorio atributoFuenteDatosRepositorio) {
        this.atributoFuenteDatosRepositorio = atributoFuenteDatosRepositorio;
    }
    
    public EncapsuladorListSW<AtributoFuenteDatosDto> cargaTodosPorTabla(Long idTabla) {
        List<AtributoFuenteDatos> listaAtributos = atributoFuenteDatosRepositorio.cargaTodosPorTabla(idTabla);
        EncapsuladorListSW<AtributoFuenteDatosDto> result = new EncapsuladorListSW<AtributoFuenteDatosDto>();
        AtributoFuenteDatos2AtributoFuenteDatosDtoTransformer transformer = new AtributoFuenteDatos2AtributoFuenteDatosDtoTransformer(mapper);
        for (AtributoFuenteDatos atributo: listaAtributos)
        	result.add(transformer.transform(atributo));
        return result;
    }
    
    public AtributoFuenteDatosDto carga(Long id) {
        return new AtributoFuenteDatos2AtributoFuenteDatosDtoTransformer(mapper).transform(atributoFuenteDatosRepositorio.carga(id));
    }

    public AtributoFuenteDatosDto borra(Long id) {
        AtributoFuenteDatosDto atributoFuenteDatosDto = carga(id);
        atributoFuenteDatosRepositorio.borra(id);
        return atributoFuenteDatosDto;
    }
    
    public AtributoFuenteDatosDto guarda(AtributoFuenteDatosDto atributoFuenteDatosDto, EncapsuladorErroresSW erros) {
        //Validador<FuenteDto> fuenteDtoValidador = new FuenteDtoValidador();
        //tablaFuenteDatosValidador.valida(tablaFuenteDatosDto, erros);
        if (erros.getHashErrors()) return null;

        AtributoFuenteDatos atributo = new AtributoFuenteDatosDto2AtributoFuenteDatosTransformer(mapper, atributoFuenteDatosRepositorio).transform(atributoFuenteDatosDto);
        
        AtributoFuenteDatos atributo2 = atributoFuenteDatosRepositorio.guarda(atributo);

        return new AtributoFuenteDatos2AtributoFuenteDatosDtoTransformer(mapper).transform(atributo2);
    }
    
    
    public EncapsuladorListSW<EncapsuladorStringSW> cargaValores(AtributoFuenteDatosDto atributoFuenteDatosDto,String caracterSeparador, Map<TiposFuente, String> mapaPath,boolean completos){
    	AtributosMapDto mapaColumnas=null;
    	if(atributoFuenteDatosDto.getTipoAtributo().equals(TipoAtributoFD.VALORFDGEOGRAFICO)){
    		mapaColumnas=gestorCUFuente.obtenerMapaTablaFuenteExterna(atributoFuenteDatosDto.getTabla().getFuente().getId(), 
        			atributoFuenteDatosDto.getTabla().getEsquema(), atributoFuenteDatosDto.getTabla().getNombre(), 
        			mapaPath.get(atributoFuenteDatosDto.getTabla().getFuente().getTipo()));
    	}else{
    		mapaColumnas=gestorCUFuente.obtenerDatosTablaFuenteExterna(atributoFuenteDatosDto.getTabla().getFuente().getId(), 
    			atributoFuenteDatosDto.getTabla().getEsquema(), atributoFuenteDatosDto.getTabla().getNombre(), 
    			mapaPath.get(atributoFuenteDatosDto.getTabla().getFuente().getTipo()), caracterSeparador);
    	}
    	return new EncapsuladorListSW<EncapsuladorStringSW>(mapaColumnas.getContenido().get(atributoFuenteDatosDto.getNombre()).getValores());
    }
    
    
    public EncapsuladorIntegerSW cargaTamnhoColumna(AtributoFuenteDatosDto atributoFuenteDatosDto,String caracterSeparador, Map<TiposFuente, String> mapaPath){
    	AtributosMapDto mapaColumnas=gestorCUFuente.obtenerDatosTablaFuenteExterna(atributoFuenteDatosDto.getTabla().getFuente().getId(), 
    			atributoFuenteDatosDto.getTabla().getEsquema(), atributoFuenteDatosDto.getTabla().getNombre(), 
    			mapaPath.get(atributoFuenteDatosDto.getTabla().getFuente().getTipo()), caracterSeparador);
    	EncapsuladorIntegerSW result= new EncapsuladorIntegerSW();
    	result.setValor(mapaColumnas.getContenido().get(atributoFuenteDatosDto.getNombre()).getValores().size());
    	return result;
    }
    
    /*
     * Método que en un estructura del tipo LinkedHashMap<String, AtributosMapDto> obtiene el número de valores que contiene
     * la relación TABLA - NOMBRE ATRIBUTO
     * Recibe la estructura mapaTablas, la información de la tabla y del atributo
     * v.01.31
     */
    public EncapsuladorIntegerSW obtenerTamanhoColumnaEstructura(AtributoFuenteDatosDto atributoFuenteDatosDto, LinkedHashMap<String, AtributosMapDto> mapaTablas) {
    	
    	EncapsuladorIntegerSW result= new EncapsuladorIntegerSW();
//    	String strNombreAtributo = null;
    	
    	String strNombreTabla = atributoFuenteDatosDto.getTabla().getNombre();
		AtributosMapDto mapaColumnas = mapaTablas.get(strNombreTabla);
    	String strNomAtributoInicial = atributoFuenteDatosDto.getNombre()+"_formula";
		ValorFDDto valoresValorFDDto = mapaColumnas.getContenido().get(strNomAtributoInicial);
		List<EncapsuladorStringSW> valoresColumna = null;
//    	if (valoresValorFDDto ==null) {
//    		strNombreAtributo = obtenerNombreColumna(strNomAtributoInicial, mapaColumnas);
//    		valoresColumna = mapaColumnas.getContenido().get(strNombreAtributo).getValores();
//    	} else {
    		valoresColumna = valoresValorFDDto.getValores();
//    	}
    	result.setValor(valoresColumna.size());
		return result;
    }
    
    /*
     * Método que en un estructura del tipo LinkedHashMap<String, AtributosMapDto> obtiene la lista de valores
     * de la relación TABLA - NOMBRE ATRIBUTO
     * Recibe la estructura mapaTablas, la información de la tabla y del atributo
     * v.01.31
     */
    public EncapsuladorListSW<EncapsuladorStringSW> cargaValoresEstructura(AtributoFuenteDatosDto atributoFuenteDatosDto, LinkedHashMap<String, AtributosMapDto> mapaTablas) {
    	
//    	String strNombreAtributo = null;
    	String strNombreTabla = atributoFuenteDatosDto.getTabla().getNombre();
		AtributosMapDto mapaColumnas = mapaTablas.get(strNombreTabla);
    	String strNomAtributoInicial = atributoFuenteDatosDto.getNombre()+"_formula";
		ValorFDDto valoresValorFDDto = mapaColumnas.getContenido().get(strNomAtributoInicial);
    	List<EncapsuladorStringSW> valoresColumna = null;
//    	if (valoresValorFDDto ==null) {
//    		strNombreAtributo = obtenerNombreColumna(strNomAtributoInicial, mapaColumnas);
//    		valoresColumna = mapaColumnas.getContenido().get(strNombreAtributo).getValores();
//    	} else {
    		valoresColumna = valoresValorFDDto.getValores();
//    	}
    	
		EncapsuladorListSW<EncapsuladorStringSW> listaValoresEncapsulada = new EncapsuladorListSW<EncapsuladorStringSW>(valoresColumna);
		return listaValoresEncapsulada;		
    	    
    }
    
    /*
	 * Método que busca en los datos de una fuente si existe el nombre de una columna, 
	 * si no se encuentra el nombre de la columna tal y como existe en la fuente,
	 * puede ser porque sea una columna repetida y se le incluyese un secuencial al nombre 
	 */
	private String obtenerNombreColumna (String strNombreColumnaInicial, AtributosMapDto datosTabla) {
		
		LinkedHashMap<String, ValorFDDto> contenido = datosTabla.getContenido();
		if (contenido.containsKey(strNombreColumnaInicial)) return strNombreColumnaInicial;
		int contNombreColumna = 1;
		do {
			String strNuevoNombre = strNombreColumnaInicial + contNombreColumna;
			if (contenido.containsKey(strNuevoNombre)) {
				return strNuevoNombre;
			}
			contNombreColumna++;
		} while(true);
	}
    
    public AtributoFuenteDatosDto cargaPorTablaYNombreYEsForumula(String nombre,Boolean esFormula, Long idTabla){
    	AtributoFuenteDatos temp= atributoFuenteDatosRepositorio.cargaPorTablaYNombreYEsForumula(nombre, esFormula, idTabla);
    	if(temp==null)
    		return null;
    	return new AtributoFuenteDatos2AtributoFuenteDatosDtoTransformer(mapper).transform(temp);
    }
    
    public AtributoFuenteDatosDto guardaOCargaPorCadenaExpresion(String expresion,Long idIndicador,EncapsuladorErroresSW errores){
    	String[] trozos=StringUtils.split(expresion, patronTrocearExpresion);
    	Long idFuente=Long.valueOf(trozos[0]);
    	String nombreTabla=trozos[1];
    	String nombreColumna=trozos[2];
    	
    	TablaFuenteDatosDto tablaFuenteDatosDto= gestorCUTablaFuenteDatos.cargaFuentePorIndicadorYNombreYFuente(idIndicador, idFuente, nombreTabla);
    	//No existe la tabla ni el atributo
    	if(tablaFuenteDatosDto==null){
    		// se crea la tabla
    		tablaFuenteDatosDto= new TablaFuenteDatosDto();
    		tablaFuenteDatosDto.setFuente(gestorCUFuente.carga(idFuente));
    		tablaFuenteDatosDto.setIndicador(gestorCUIndicador.cargaPorId(idIndicador));
    		tablaFuenteDatosDto.setNombre(nombreTabla);
    		tablaFuenteDatosDto= gestorCUTablaFuenteDatos.guarda(tablaFuenteDatosDto, errores);
    		
    		// se cera el atributo
    		AtributoFuenteDatosDto atributoFuenteDatosDto= new AtributoFuenteDatosDto();
    		atributoFuenteDatosDto.setEsFormula(true);
    		atributoFuenteDatosDto.setNombre(nombreColumna);
    		atributoFuenteDatosDto.setTabla(tablaFuenteDatosDto);
    		atributoFuenteDatosDto.setTipoAtributo(TipoAtributoFD.VALORFDNUMERICO);
    		return guarda(atributoFuenteDatosDto, errores);
    		
    	}
    	AtributoFuenteDatosDto atributoFuenteDatosDto= cargaPorTablaYNombreYEsForumula(nombreColumna, true, tablaFuenteDatosDto.getId());
    	if(atributoFuenteDatosDto!=null)
    		return atributoFuenteDatosDto;
    	
    	atributoFuenteDatosDto= new AtributoFuenteDatosDto();
		atributoFuenteDatosDto.setEsFormula(true);
		atributoFuenteDatosDto.setNombre(nombreColumna);
		atributoFuenteDatosDto.setTabla(tablaFuenteDatosDto);
		atributoFuenteDatosDto.setTipoAtributo(TipoAtributoFD.VALORFDNUMERICO);
		return guarda(atributoFuenteDatosDto, errores);
    	
    }
}