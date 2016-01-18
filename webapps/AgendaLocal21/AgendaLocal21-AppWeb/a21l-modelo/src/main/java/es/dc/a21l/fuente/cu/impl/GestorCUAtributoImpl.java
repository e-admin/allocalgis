/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorIntegerSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorStringSW;
import es.dc.a21l.base.modelo.utils.UtilFecha;
import es.dc.a21l.base.utils.MapaRelaciones;
import es.dc.a21l.base.utils.Par;
import es.dc.a21l.base.utils.enumerados.TipoAtributoFD;
import es.dc.a21l.base.utils.enumerados.TiposFuente;
import es.dc.a21l.criterio.cu.CriterioDto;
import es.dc.a21l.criterio.cu.GestorCUCriterio;
import es.dc.a21l.elementoJerarquia.cu.GestorCUIndicadorExpresion;
import es.dc.a21l.elementoJerarquia.cu.GestorCURelacion;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorExpresionRepositorio;
import es.dc.a21l.expresion.cu.ExpresionDto;
import es.dc.a21l.expresion.cu.GestorCUExpresion;
import es.dc.a21l.fuente.cu.AtributoDto;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.FuenteDto;
import es.dc.a21l.fuente.cu.GestorCUAtributo;
import es.dc.a21l.fuente.cu.GestorCUAtributoFuenteDatos;
import es.dc.a21l.fuente.cu.GestorCUFuente;
import es.dc.a21l.fuente.cu.GestorCUTablaFuenteDatos;
import es.dc.a21l.fuente.cu.TablaFuenteDatosDto;
import es.dc.a21l.fuente.cu.ValorFDDto;
import es.dc.a21l.fuente.modelo.Atributo;
import es.dc.a21l.fuente.modelo.AtributoRepositorio;

/**
 *
 * @author Balidea Consulting & Programming
 */
public class GestorCUAtributoImpl implements GestorCUAtributo {
	private static final Logger log = LoggerFactory.getLogger(GestorCUAtributoImpl.class);
	private static final String FECHA_CORTA = "Corta";
	private static final String TABLA_FORMULAS = "formula";
	private Mapper mapper;
    private AtributoRepositorio atributoRepositorio;
    private IndicadorExpresionRepositorio indicadorExpresionRepositorio;
    private GestorCUFuente gestorCUFuente;
    private GestorCUExpresion gestorCUExpresion;
    private GestorCUCriterio gestorCUCriterio;
    
    private GestorCURelacion gestorCURelacion;
    private GestorCUAtributoFuenteDatos gestorCUAtributoFuenteDatos;
    private GestorCUIndicadorExpresion gestorCUIndicadorExpresion;
    private GestorCUTablaFuenteDatos gestorCUTablaFuenteDatos;
    
    
    
    @Autowired
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }
    
    @Autowired
    public void setIndicadorExpresionRepositorio(IndicadorExpresionRepositorio indicadorExpresionRepositorio) {
        this.indicadorExpresionRepositorio = indicadorExpresionRepositorio;
    }
    
    @Autowired
    public void setAtributoRepositorio(AtributoRepositorio atributoRepositorio) {
        this.atributoRepositorio = atributoRepositorio;
    }
    
    @Autowired
    public void setGestorCUFuente(GestorCUFuente gestorCUFuente) {
    	this.gestorCUFuente = gestorCUFuente;
    }
    
    @Autowired
    public void setGestorCUExpresion(GestorCUExpresion gestorCUExpresion) {
    	this.gestorCUExpresion = gestorCUExpresion;
    }
    
    @Autowired
    public void setGestorCURelacion(GestorCURelacion gestorCURelacion) {
    	this.gestorCURelacion = gestorCURelacion;
    }
    
    @Autowired
    public void setGestorCUAtributoFuenteDatos(GestorCUAtributoFuenteDatos gestorCUAtributoFuenteDatos) {
    	this.gestorCUAtributoFuenteDatos = gestorCUAtributoFuenteDatos;
    }
    
    @Autowired
    public void setGestorCUIndicadorExpresion(GestorCUIndicadorExpresion gestorCUIndicadorExpresion) {
    	this.gestorCUIndicadorExpresion = gestorCUIndicadorExpresion;
    }
    
    @Autowired
    public void setGestorCUTablaFuenteDatos(GestorCUTablaFuenteDatos gestorCUTablaFuenteDatos) {
    	this.gestorCUTablaFuenteDatos = gestorCUTablaFuenteDatos;
    }
    
 @Autowired
    public void setGestorCUCriterio(GestorCUCriterio gestorCUCriterio) {
		this.gestorCUCriterio = gestorCUCriterio;
	}
    
    public AtributoDto cargaPorAtributoFuenteDatos(Long idAtributoFuenteDatos) {
        Atributo atributo = atributoRepositorio.cargaPorAtributoFuenteDatos(idAtributoFuenteDatos);
        AtributoDto result = new AtributoDto();
        Atributo2AtributoDtoTransformer transformer = new Atributo2AtributoDtoTransformer(mapper);
        	result = transformer.transform(atributo);
        return result;
    }
    
    public EncapsuladorListSW<AtributoDto> cargaPorIndicador(Long idIndicador) {
    	List<Atributo> atributos = atributoRepositorio.cargaPorIndicador(idIndicador);
        EncapsuladorListSW<AtributoDto> result = new EncapsuladorListSW<AtributoDto>();
        Atributo2AtributoDtoTransformer transformer = new Atributo2AtributoDtoTransformer(mapper);
        for (Atributo atributo: atributos)
        	result.add(transformer.transform(atributo));
        return result;
    }
    
    public AtributoDto carga(Long id) {
        Atributo attr = atributoRepositorio.carga(id);
    	return new Atributo2AtributoDtoTransformer(mapper).transform(attr);
    }

    public AtributoDto borra(Long id, EncapsuladorErroresSW erros) {
        AtributoDto atributoDto = carga(id);
        atributoRepositorio.borra(id);
        return atributoDto;
    }
    
    public AtributoDto guarda(AtributoDto atributoDto, EncapsuladorErroresSW erros) {
        //Validador<FuenteDto> fuenteDtoValidador = new FuenteDtoValidador();
        //tablaFuenteDatosValidador.valida(tablaFuenteDatosDto, erros);
        if (erros.getHashErrors()) return null;

        Atributo atributo = new AtributoDto2AtributoTransformer(mapper, atributoRepositorio, indicadorExpresionRepositorio).transform(atributoDto);
        
        Atributo atributo2 = atributoRepositorio.guarda(atributo);

        return new Atributo2AtributoDtoTransformer(mapper).transform(atributo2);
    }
    
    public AtributosMapDto cargarDatosIndicadorTodasColumnas(EncapsuladorListSW<AtributoDto> listaAtributos, String caracterSeparador, String tipoFecha, Map<TiposFuente, String> mapaPath) {
    	//TODO - Una vez esten aplicadas, formulas, relaciones y criterios, substituir este metodo por cargarDAtosIndicador y quitarle las limitaciones para que
    	AtributosMapDto mapaValores = new AtributosMapDto();
    	LinkedHashMap<String,ValorFDDto> mapaColumnas = new LinkedHashMap<String,ValorFDDto>();
    	EncapsuladorIntegerSW numValoresAmbito=new EncapsuladorIntegerSW(0);
    	for ( AtributoDto attr : listaAtributos ) {
    		if(attr!=null && attr.getEsAmbito()!=null && attr.getColumna()!=null){
    			numValoresAmbito= gestorCUAtributoFuenteDatos.cargaTamnhoColumna(attr.getColumna(), caracterSeparador, mapaPath);
    			break;
    		}
    	}
    	//Para cada columna.
		//Para su tabla / fuente, obtengo los datos y me quedo con la columna correspondiente.
		//Asi hasta tener todas las columnas. Luego simplemente devolver la estructura.
    	for ( AtributoDto attr : listaAtributos ) {
    		ValorFDDto datosColumna = new ValorFDDto();
    		
    		//Los atributos/columnas que corresponden a formulas hay que calcular sus valroes, etc
    		if ( (attr.getColumna()==null && attr.getIndicadorExpresion()!=null) || (attr.getColumna()!=null && attr.getColumna().getEsFormula())) {
    			//CALCULAR VALORES FORMULA
    			AtributosMapDto datosTabla = new AtributosMapDto();
    			
    			List<String> listaCalculada = gestorCUExpresion.cargaEvaluacionExpresion(gestorCUExpresion.cargaPorId(attr.getIndicadorExpresion().getIdExpresion()), caracterSeparador, mapaPath,numValoresAmbito.getValor());
    			List<EncapsuladorStringSW> listaCalculadaEncap = new ArrayList<EncapsuladorStringSW>();
    			for ( String valor : listaCalculada ) {
    				EncapsuladorStringSW encapString = new EncapsuladorStringSW();
    				encapString.setTexto(valor);
    				listaCalculadaEncap.add(encapString);
    			}
    			datosColumna.setValores(listaCalculadaEncap);
	    		String nombreColumna = attr.getNombre();	    		
	    		
	    		mapaColumnas.put(nombreColumna, datosColumna);    			
    		} else if ( attr.getRelacion()!=null ) {
    			//RELACIONES!!!!!
    			datosColumna = new ValorFDDto();
    			String nombreColumna = attr.getNombre();
    			//Obtengo los valores de la columna relacion
    			FuenteDto fuenteRelacion = gestorCUFuente.carga(Long.valueOf(attr.getRelacion().getIdFuenteRelacion()));
    			String pathFuente = mapaPath.get(fuenteRelacion.getTipo());
    			AtributosMapDto datosTabla = gestorCUFuente.obtenerDatosTablaFuenteExterna(Long.valueOf(attr.getRelacion().getIdFuenteRelacion()),
    					"", attr.getRelacion().getNombreTablaRelacion(), pathFuente, caracterSeparador);
    			ValorFDDto valoresColumnaRelacion = datosTabla.getContenido().get(attr.getRelacion().getNombreColumnaRelacion());
    			
    			//Obtengo los valores de la columna relacionada
    			FuenteDto fuenteRelacionada = gestorCUFuente.carga(Long.valueOf(attr.getRelacion().getIdFuenteRelacionada()));
    			pathFuente = mapaPath.get(fuenteRelacionada.getTipo());
    			datosTabla = gestorCUFuente.obtenerDatosTablaFuenteExterna(Long.valueOf(attr.getRelacion().getIdFuenteRelacionada()),
    					"", attr.getRelacion().getNombreTablaRelacionada(), pathFuente, caracterSeparador);
    			ValorFDDto valoresColumnaRelacionada = datosTabla.getContenido().get(attr.getRelacion().getNombreColumnaRelacionada());

    			//Obtengo los valores de la columna "columna relacion" para la tabla relacionada.
    			ValorFDDto valoresColumnaRelacionEnTablaRelacionada = datosTabla.getContenido().get(attr.getRelacion().getNombreColumnaRelacion());
    			//Por si los nombres de las columnas estan en diferente mayus/minus. Comprobamos las diferetnes posibilidades, hasta encontrar valores
    			if ( valoresColumnaRelacionEnTablaRelacionada == null )
    				valoresColumnaRelacionEnTablaRelacionada = datosTabla.getContenido().get(attr.getRelacion().getNombreColumnaRelacion().toLowerCase());
    			if ( valoresColumnaRelacionEnTablaRelacionada == null )
    				valoresColumnaRelacionEnTablaRelacionada = datosTabla.getContenido().get(attr.getRelacion().getNombreColumnaRelacion().toUpperCase());
    				
    			ValorFDDto valoresFinales = new ValorFDDto();
    			for ( EncapsuladorStringSW valor : valoresColumnaRelacion.getValores() ) {
    				Integer contador = 0;
    				Boolean encontrado = false;
    				for ( EncapsuladorStringSW valorColumnaRelacionEnTablaRelacionada : valoresColumnaRelacionEnTablaRelacionada.getValores()) {
    					if ( valorColumnaRelacionEnTablaRelacionada.getTexto().equals(valor.getTexto())) {
    						//Al ser iguales la claves, obtengo el valor real.
    						EncapsuladorStringSW nuevoValor = new EncapsuladorStringSW();
    						nuevoValor.setTexto(valoresColumnaRelacionada.getValores().get(contador).getTexto());
    						valoresFinales.addValor(nuevoValor);
    						encontrado = true;
    						break;
    					}
    					contador++;
    				}
    				if ( !encontrado )
    					valoresFinales.addValor(new EncapsuladorStringSW());
    			}
    			
	    		datosColumna = valoresFinales;
	    		mapaColumnas.put(nombreColumna, datosColumna);
    		} else {
    			//Valores normales de columna
	    		AtributoFuenteDatosDto columna = attr.getColumna();
	    		TablaFuenteDatosDto tabla = columna.getTabla();
	    		
	    		AtributosMapDto datosTabla = gestorCUFuente.obtenerDatosTablaFuenteExternaCompletos(tabla.getFuente().getId(), tabla.getEsquema(), tabla.getNombre(), attr.getPath(), caracterSeparador);
	    		List<EncapsuladorStringSW> listaValoresColumna = new ArrayList<EncapsuladorStringSW>();
	    		//Aqui tngo TODOS los datos para esta tabla. ahora extraigo a la estructura final la columna correspondiente.
	    		//Por defecto fecha corta
	    		if ( tipoFecha==null || tipoFecha.equals("no"))
	    			tipoFecha = FECHA_CORTA; 
	    		if ( attr.getColumna().getTipoAtributo()==TipoAtributoFD.VALORFDFECHA ) {
	    			ValorFDDto datosColumnaSinFecha = datosTabla.getContenido().get(columna.getNombre());
	    			for ( EncapsuladorStringSW fecha : datosColumnaSinFecha.getValores() ) {
	    				EncapsuladorStringSW encapString = new EncapsuladorStringSW();
	    				
	    			    Date fechaConvertida = UtilFecha.multiParse(fecha.getTexto());
	    			    SimpleDateFormat formatoDelTexto = null;
	    				if ( fechaConvertida != null ) {
	    					if ( tipoFecha.equals(FECHA_CORTA) )
	    						formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
		       			    else
		       			    	formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    				
		    			    String strFecha = null;
		    			    try {
		    			         strFecha = formatoDelTexto.format(fechaConvertida);
		    			    } catch (Exception ex) {
		    			         strFecha = null; 
		    			    }   				
		    				if ( strFecha!=null)
		    					encapString.setTexto(strFecha);
		    				else
		    					encapString.setTexto("");
		    				listaValoresColumna.add(encapString);
	    				}
	    			}
	    			datosColumna.setValores(listaValoresColumna);
	    		} else
	    			datosColumna = datosTabla.getContenido().get(columna.getNombre());
	    		String nombreColumna = columna.getNombre();
	    		Integer i = 1; 
	    		while ( mapaColumnas.get(nombreColumna)!=null ) {
	    			nombreColumna = nombreColumna+i.toString();
	    			i++;
	    		}
	    		mapaColumnas.put(nombreColumna, datosColumna);
    		}
    	}
    	mapaValores.setContenido(mapaColumnas);
    	mapaValores=filtraMapaValoresPorCritetios(mapaValores, listaAtributos);
    	return mapaValores;
    }
    
    /*
     * Método encargado de cargar los valores en los INDICADORES cuando este se guarda (non-Javadoc)
     * @see es.dc.a21l.fuente.cu.GestorCUAtributo#cargarDatosIndicador(es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW, java.lang.String, java.lang.String, java.util.Map)
     */
    public AtributosMapDto cargarDatosIndicador(EncapsuladorListSW<AtributoDto> listaAtributos, String caracterSeparador, String tipoFecha, Map<TiposFuente, String> mapaPath) {
    	
    	AtributosMapDto mapaValores = new AtributosMapDto();
    	
    	//Mapa que almacena el resultado final
    	LinkedHashMap<String,AtributosMapDto> mapaTablas = new LinkedHashMap<String,AtributosMapDto>();
    	
    	//Estructura utilizada unicamente para enviar el resultado a la vista
    	LinkedHashMap<String,ValorFDDto> mapaColumnas = new LinkedHashMap<String,ValorFDDto>();    	
    	
    	//Mapas temporales utilizado unicamente para controlar las columnas repetidas y el resultado
    	WeakHashMap<String,ValorFDDto> mapaColumnasAux = new WeakHashMap<String,ValorFDDto>();
    	
    	//Código incorporado para obtener el tamaño de la columna ámbito
    	EncapsuladorIntegerSW numValoresAmbito=new EncapsuladorIntegerSW(0);
    	for ( AtributoDto attr : listaAtributos ) {
    		if(attr!=null && attr.getEsAmbito()!=null && attr.getColumna()!=null && (!attr.getEsMapa())) {
    			numValoresAmbito= gestorCUAtributoFuenteDatos.cargaTamnhoColumna(attr.getColumna(), caracterSeparador, mapaPath);
    			break;
    		}
    	}
    	
    	//Método que rellena la estructura mapaTablas a partir de la listaAtributos y las fuentes externas de datos
 		tipoFecha = rellenarEstructura(listaAtributos, caracterSeparador, tipoFecha, mapaPath, mapaTablas, mapaColumnasAux, numValoresAmbito);    	
// 		mapaColumnasAux = null;
 		
// 		PARTE UTILIZANDO JOINER. Obtiene bien los datos, pero parece que luego se postprocesan mal
//		¿La salida puede no estar en el formato correcto?
//
// 		MapaRelaciones<String, String> relaciones = obtenerRelacionesOrden(listaAtributos);
// 		new JoinerTabla().run(mapaTablas, relaciones);
// 		System.out.println(mapaTablas);
 		
 		/* Se obtiene la estructura de relaciones y se utiliza para recorrer y para gestionar
 		 * los datos de la estructura mapaTablas y así obtener el resultado final
 		 */
 		MapaRelaciones<String, String> mapaRelaciones = obtenerRelacionesOrden (listaAtributos);
 		
 		Set<Par<String, String>> setTablas = mapaRelaciones.keySet();
 		Iterator<Par<String, String>> itTablas = setTablas.iterator();
 		Par<String, String> strTabla = null;
 		while (itTablas.hasNext()) {			
 			
 			//Atributos utilizados para el análisis de cada relación
 			AtributosMapDto datosTablaRelacion, datosTablaRelacionada=null;
    		ValorFDDto valoresColumnaRelacion, valoresColumnaRelacionada=null;
    		
    		String strNombreTablaRelacion, strNombreTablaRelacionada=null;
 			
    		strTabla = itTablas.next();
 		
    		strNombreTablaRelacion=strTabla.getValor1();
    		strNombreTablaRelacionada=strTabla.getValor2();
    		
    		datosTablaRelacion = mapaTablas.get(strNombreTablaRelacion);
    		datosTablaRelacionada = mapaTablas.get(strNombreTablaRelacionada);
    		
    		//SE RECORRE LA LISTA DE PARES ATRIBUTOS (RELACION,RELACIONADO) que existe entre cada PAR TABLAS (RELACION,RELACIONADA)
    		List<Par<String, String>> listaAtributosRelacion = mapaRelaciones.get(strTabla);
    		
    		boolean esPrimeraRelacion = true;
    		for (Par<String, String> relacion : listaAtributosRelacion) {
    			
    			//SE OBTIENEN LOS DATOS DE LA COLUMNA RELACIÓN DEL MAPA DE TABLAS Y NO DE LA FUENTE DIRECTAMENTE
    			String strNombreColumnaRelacion = relacion.getValor1();
    			LinkedHashMap<String, ValorFDDto> contenidoRelacion = datosTablaRelacion.getContenido();
				valoresColumnaRelacion = contenidoRelacion.get(strNombreColumnaRelacion);
    			
    			 //SE OBTIENEN LOS DATOS DE LA COLUMNA RELACIONADA DEL MAPA DE TABLAS Y NO DE LA FUENTE DIRECTAMENTE
    			LinkedHashMap<String, ValorFDDto> contenidoRelacionado = datosTablaRelacionada.getContenido();
    			String strNombreColumnaRelacionada = relacion.getValor2();
				valoresColumnaRelacionada = contenidoRelacionado.get(strNombreColumnaRelacionada);
    			
    			//Si es la primera relación entre dos tablas es necesario realizar el INNER JOIN 
    			if (esPrimeraRelacion) {
    				procesoInnerJoin(mapaTablas, valoresColumnaRelacion, valoresColumnaRelacionada, strNombreTablaRelacion, strNombreTablaRelacionada);
    				esPrimeraRelacion = false;
    			//Si no es primera relación se FILTRA por las columnas relación - relacionada. Si son IGUALES se mantienen en el mapa, si son distintas se eliminan
    			} else {
    				procesoFiltrarDatos(mapaTablas, valoresColumnaRelacion,valoresColumnaRelacionada, strNombreTablaRelacion,strNombreTablaRelacionada);
    			}
    			
    		} //FIN LISTA ATRIBUTOS POR TABLAS
  		} //FIN RELACIONES
 		
 		//Sobre el resultado final es necesario aplicar las fórmulas
 		//Por todos los atributos existentes obtengo las fórmulas que se piden.
 		for ( AtributoDto attr : listaAtributos ) {
 			aplicarFormulasResultado(mapaTablas, mapaColumnasAux, numValoresAmbito, attr);    		
		} 
 		
		//Se vuelca el resultado en mapaColumnas para que sea leido en la VISTA
    	WeakHashMap<String, ValorFDDto> mapaColumnasRtdo = volcarEnEstructuraFinal(listaAtributos, mapaTablas);
		//Se libera mapaTablas
		mapaTablas =null;
		
		/* Se introducen los atributos en mapaColumnas tal y como se recogen del formulario (lista de atributos a visualizar)
		 * y de esta forma mantener el orden en la visualización final
		 */
		for (AtributoDto atributoOrden : listaAtributos) {
			if (mapaColumnasRtdo.containsKey(atributoOrden.getNombre())) {
				mapaColumnas.put(atributoOrden.getNombre(), mapaColumnasRtdo.get(atributoOrden.getNombre()));
			}
		}
		
		//Se reinicia mapaColumnasRtdo
		mapaColumnasRtdo=null;
    	mapaValores.setContenido(mapaColumnas);
    	mapaColumnas=null;
    	mapaValores=filtraMapaValoresPorCritetios(mapaValores, listaAtributos);
    	return mapaValores;
    }

    /*
     * Método que se encarga de aplicar sobre el resultado final las fórmulas definidas en la lista de atributos
     * Los datos que participarán del cálculo de las fórmulas se leen de la estructura de datos LinkedHashMap<String, AtributosMapDto> mapaTablas
     * v.01.31 
     */
	private void aplicarFormulasResultado(LinkedHashMap<String, AtributosMapDto> mapaTablas, WeakHashMap<String, ValorFDDto> mapaColumnasAux, EncapsuladorIntegerSW numValoresAmbito, AtributoDto attr) {
		
		//Si cumple estas condiciones, se trata de una fórmula
		if ( (attr.getColumna()==null && attr.getIndicadorExpresion()!=null) || (attr.getColumna()!=null && attr.getColumna().getEsFormula())) {
			
			ValorFDDto datosColumna = new ValorFDDto();
			
			//Calcula los valores de la fórmula
			List<String> listaCalculada = gestorCUExpresion.cargaEvaluacionExpresionDeEstructura(gestorCUExpresion.cargaPorId(attr.getIndicadorExpresion().getIdExpresion()), mapaTablas, numValoresAmbito.getValor());
			List<EncapsuladorStringSW> listaCalculadaEncap = new ArrayList<EncapsuladorStringSW>();
			for ( String valor : listaCalculada ) {
				EncapsuladorStringSW encapString = new EncapsuladorStringSW();
				encapString.setTexto(valor);
				listaCalculadaEncap.add(encapString);
			}
			
			datosColumna.setValores(listaCalculadaEncap);
			String nombreColumnaExpresion = attr.getNombre();
			nombreColumnaExpresion = obtenerNombreColumnaMapa (nombreColumnaExpresion, mapaColumnasAux, datosColumna);
			
			//Se almacenan a mayores los atributos que de cada tabla se van a visualizar en el resultado para poder trabajar con ellos a la hora de gestionar las relaciones
			AtributosMapDto mapaTablaTratada = mapaTablas.get(GestorCUAtributoImpl.TABLA_FORMULAS);
			almacenarAtributoTabla (mapaTablaTratada, mapaTablas, GestorCUAtributoImpl.TABLA_FORMULAS, nombreColumnaExpresion, datosColumna);	    			
			//Se liberan recursos
			listaCalculadaEncap=null;
			nombreColumnaExpresion=null;
			datosColumna=null;
			mapaTablaTratada=null;
		}
	}

    /*
     * Método que rellena una estructura del tipo LinkedHashMap<String, AtributosMapDto> a partir de la lista de atributos EncapsuladorListSW<AtributoDto> que el
     * usuario quiere mostrar en pantalla
     */
	private String rellenarEstructura(EncapsuladorListSW<AtributoDto> listaAtributos, String caracterSeparador, String tipoFecha, Map<TiposFuente, String> mapaPath,
			LinkedHashMap<String, AtributosMapDto> mapaTablas, WeakHashMap<String, ValorFDDto> mapaColumnasAux, EncapsuladorIntegerSW numValoresAmbito) {
		
		for ( AtributoDto attr : listaAtributos ) {
 			
 			/* CAMBIO DE PLANTEAMIENTO:
 			 * Si existen relaciones, los datos de salida no se correponden con los datos de entrada. Por tal motivo no es factible obtener los datos del campo mapa
 			 * a posteriori ya que de esta forma nos encontrariamos con dos conjuntos de datos no relacionados entre si. Si en mapaTablas incluimos la columna mapa
 			 * podremos eliminar los datos que no deberán tenerse en cuenta en el resultado
 			 * Ahora el campo MAPA se gestionará como si fuese un CAMPO NORMAL y por tanto se introducirá en mapaTablas 
 			 */
 			if (!attr.getEsMapa() && !attr.getMostrar() && attr.getRelacion()==null)
 				continue;
 			
    		ValorFDDto datosColumna = new ValorFDDto();
    		
    		//Los atributos/columnas que corresponden a formulas hay que calcular sus valores, etc
    		if ( (attr.getColumna()==null && attr.getIndicadorExpresion()!=null) || (attr.getColumna()!=null && attr.getColumna().getEsFormula())) {
				
    			mapaTablas = gestorCUExpresion.cargaInicialValoresDeFuentesExpresionEnEstructura(gestorCUExpresion.cargaPorId(attr.getIndicadorExpresion().getIdExpresion()), 
    					mapaTablas, caracterSeparador, mapaPath);
    			
//    			//Calcula los valores de la fórmula
//    			List<String> listaCalculada = gestorCUExpresion.cargaEvaluacionExpresion(gestorCUExpresion.cargaPorId(attr.getIndicadorExpresion().getIdExpresion()), caracterSeparador, mapaPath,numValoresAmbito.getValor());
//    			List<EncapsuladorStringSW> listaCalculadaEncap = new ArrayList<EncapsuladorStringSW>();
//    			for ( String valor : listaCalculada ) {
//    				EncapsuladorStringSW encapString = new EncapsuladorStringSW();
//    				encapString.setTexto(valor);
//    				listaCalculadaEncap.add(encapString);
//    			}
//    			
//    			datosColumna.setValores(listaCalculadaEncap);
//    			String nombreColumnaExpresion = attr.getNombre();
//    			nombreColumnaExpresion = obtenerNombreColumnaMapa (nombreColumnaExpresion, mapaColumnasAux, datosColumna);
//    			
//	    		//Se almacenan a mayores los atributos que de cada tabla se van a visualizar en el resultado para poder trabajar con ellos a la hora de gestionar las relaciones
//	    		AtributosMapDto mapaTablaTratada = mapaTablas.get(GestorCUAtributoImpl.TABLA_FORMULAS);
//	    		almacenarAtributoTabla (mapaTablaTratada, mapaTablas, GestorCUAtributoImpl.TABLA_FORMULAS, nombreColumnaExpresion, datosColumna);	    			
//	    		//Se liberan recursos
//	    		listaCalculadaEncap=null;
//	    		nombreColumnaExpresion=null;
//	    		datosColumna=null;
//	    		mapaTablaTratada=null;
//	    		
    		} else if ( attr.getRelacion()!=null ) {
    			
    			AtributosMapDto mapaTablaTratada = null;
    			
    			//SE INCORPORA AL MAPA DE CADA TABLA TODOS LOS ATRIBUTOS QUE FORMAN PARTE DE UNA RELACIÓN PARA NO PERDERLOS CUANDO SE GESTIONAN LAS RELACIONES (COLUMNA RELACIÓN)
    			FuenteDto fuenteRelacion = gestorCUFuente.carga(Long.valueOf(attr.getRelacion().getIdFuenteRelacion()));
    			String pathFuente = mapaPath.get(fuenteRelacion.getTipo());    			
    			AtributosMapDto datosTabla = gestorCUFuente.obtenerDatosTablaFuenteExterna(Long.valueOf(attr.getRelacion().getIdFuenteRelacion()), "",
    					attr.getRelacion().getNombreTablaRelacion(), pathFuente, caracterSeparador);
    			datosColumna = datosTabla.getContenido().get(attr.getRelacion().getNombreColumnaRelacion());
    			
    			String strNombreColumnaRelacion = (attr.getRelacion().getNombreColumnaRelacion())+"_relacion";
    			
    			//Se almacenan a mayores los atributos que de cada tabla se van a visualizar en el resultado para poder trabajar con ellos a la hora de gestionar las relaciones	    		
	    		mapaTablaTratada = mapaTablas.get(attr.getRelacion().getNombreTablaRelacion());
	    		if (mapaTablaTratada==null || (mapaTablaTratada!=null && !mapaTablaTratada.getContenido().containsKey(strNombreColumnaRelacion))) {
	    			almacenarAtributoTabla (mapaTablaTratada, mapaTablas, attr.getRelacion().getNombreTablaRelacion(), strNombreColumnaRelacion, datosColumna);
	    		}
	    		
    			//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
    			
	    		//SE INCORPORA AL MAPA DE CADA TABLA TODOS LOS ATRIBUTOS QUE FORMAN PARTE DE UNA RELACIÓN PARA NO PERDERLOS CUANDO SE GESTIONAN LAS RELACIONES (COLUMNA RELACIONADA)
    			FuenteDto fuenteRelacionada = gestorCUFuente.carga(Long.valueOf(attr.getRelacion().getIdFuenteRelacionada()));
    			pathFuente = mapaPath.get(fuenteRelacionada.getTipo());
    			datosTabla = gestorCUFuente.obtenerDatosTablaFuenteExterna(Long.valueOf(attr.getRelacion().getIdFuenteRelacionada()), "",
    					attr.getRelacion().getNombreTablaRelacionada(), pathFuente, caracterSeparador);
    			datosColumna = datosTabla.getContenido().get(attr.getRelacion().getNombreColumnaRelacionada());
    			
    			String strNombreColumnaRelacionada = (attr.getRelacion().getNombreColumnaRelacionada())+"_relacionada";
    			
    			//Se almacenan a mayores los atributos que de cada tabla se van a visualizar en el resultado para poder trabajar con ellos a la hora de gestionar las relaciones	    		
	    		mapaTablaTratada = mapaTablas.get(attr.getRelacion().getNombreTablaRelacionada());
	    		if (mapaTablaTratada==null || (mapaTablaTratada!=null && !mapaTablaTratada.getContenido().containsKey(strNombreColumnaRelacionada))) {
	    			almacenarAtributoTabla (mapaTablaTratada, mapaTablas, attr.getRelacion().getNombreTablaRelacionada(), strNombreColumnaRelacionada, datosColumna);
	    		}
	    		//Se liberan recursos
	    		datosTabla=null;
	    		strNombreColumnaRelacion=null;
	    		strNombreColumnaRelacionada=null;
	    		datosColumna=null;
	    		mapaTablaTratada=null;
    		} else {
    			//Valores normales de columna
	    		AtributoFuenteDatosDto columna = attr.getColumna();
	    		TablaFuenteDatosDto tabla = columna.getTabla();
	    		
	    		AtributosMapDto datosTabla = null;
	    		if (attr.getEsMapa()) {
	    			datosTabla = gestorCUFuente.obtenerMapaTablaFuenteExterna(tabla.getFuente().getId(), tabla.getEsquema(), tabla.getNombre(), attr.getPath());
	    		} else {
		    		datosTabla = gestorCUFuente.obtenerDatosTablaFuenteExterna(tabla.getFuente().getId(), tabla.getEsquema(), tabla.getNombre(), 
		    				attr.getPath(), caracterSeparador);
	    		}
	    		
	    		List<EncapsuladorStringSW> listaValoresColumna = new ArrayList<EncapsuladorStringSW>();
	    		//Aqui tngo TODOS los datos para esta tabla. ahora extraigo a la estructura final la columna correspondiente.
	    		//Por defecto fecha corta
	    		if ( tipoFecha==null || tipoFecha.equals("no"))
	    			tipoFecha = FECHA_CORTA; 
	    		if ( attr.getColumna().getTipoAtributo()==TipoAtributoFD.VALORFDFECHA ) {
	    			ValorFDDto datosColumnaSinFecha = datosTabla.getContenido().get(columna.getNombre());
	    			for ( EncapsuladorStringSW fecha : datosColumnaSinFecha.getValores() ) {
	    				EncapsuladorStringSW encapString = new EncapsuladorStringSW();
	    				
	    			    Date fechaConvertida = UtilFecha.multiParse(fecha.getTexto());
	    			    SimpleDateFormat formatoDelTexto = null;
	    				if ( fechaConvertida != null ) {
	    					if ( tipoFecha.equals(FECHA_CORTA) )
	    						formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
		       			    else
		       			    	formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    				
		    			    String strFecha = null;
		    			    try {
		    			         strFecha = formatoDelTexto.format(fechaConvertida);
		    			    } catch (Exception ex) {
		    			         strFecha = null; 
		    			    }   				
		    				if ( strFecha!=null)
		    					encapString.setTexto(strFecha);
		    				else
		    					encapString.setTexto("");
		    				listaValoresColumna.add(encapString);
	    				}
	    			}
	    			datosColumna.setValores(listaValoresColumna);
	    		} else {
	    			datosColumna = datosTabla.getContenido().get(columna.getNombre());
	    		}
	    		String nombreColumna = columna.getNombre();
	    		nombreColumna = obtenerNombreColumnaMapa(nombreColumna, mapaColumnasAux, datosColumna);
	    		
	    		//Se almacenan a mayores los atributos que de cada tabla se van a visualizar en el resultado para poder trabajar con ellos a la hora de gestionar las relaciones	    		
	    		AtributosMapDto mapaTablaTratada = mapaTablas.get(tabla.getNombre());
	    		almacenarAtributoTabla (mapaTablaTratada, mapaTablas, tabla.getNombre(), nombreColumna, datosColumna);	    		
	    		datosTabla=null;
	    		listaValoresColumna=null;
	    		nombreColumna=null;
	    		datosColumna=null;
	    		mapaTablaTratada=null;
    		}
		} //FOR ATRIBUTOS
		return tipoFecha;
	}

    /*
     * Método que implementa el proceso que se encarga de realizar el Inner Join entre los atributos de dos tablas
     */
	private void procesoInnerJoin(LinkedHashMap<String, AtributosMapDto> mapaTablas, ValorFDDto valoresColumnaRelacion, ValorFDDto valoresColumnaRelacionada, String strNombreTablaRelacion, String strNombreTablaRelacionada) {
		
		//Se inicializa el mapa donde se almacena temporalmente el resultado de la gestión de la relación        			
		WeakHashMap<String,AtributosMapDto> mapaTablasRelacion = new WeakHashMap<String,AtributosMapDto>();
		
		/* --------------------------------------------------------------------------------------------------
		 * SE COMPARA LA COLUMNA RELACIÓN CON LA COLUMNA RELACIONADA, si el valor coincide se añade a la
		 * estructura final mapaTablasRelacion la fila completa tablas/atributos, en caso contrario no
		 --------------------------------------------------------------------------------------------------- */
		Integer indiceRelacionExterno = 0;    			
		for (EncapsuladorStringSW valorRelacion : valoresColumnaRelacion.getValores()) {
			
			Integer indiceRelacionInterno = 0;
			
			/* Se comprueba si el valorRelacion está contenido dentro de valoresColumnaRelacionada,
			 * sino existe ya no se evalua el FOR y se incrementa indiceRelacionExterno 
			 */
			for ( EncapsuladorStringSW valorRelacionado : valoresColumnaRelacionada.getValores()) {
				
				if (valorRelacionado.compareTo(valorRelacion)!= -1) {

					//Se obtienen las claves de todas las tablas almacenadas en mapaTablas
					Set<String> setTablasIndicador = mapaTablas.keySet();
					Iterator<String> itTablasIndicador = setTablasIndicador.iterator();
					
					String strTablaIndicador = null;
					
					WeakHashMap<String,ValorFDDto> mapaColumnasIndicadorTabla=null;
					AtributosMapDto mapaValoresRelacion=null;
					
					while (itTablasIndicador.hasNext()) {
						
						//Se reinician a nivel de tabla
						mapaColumnasIndicadorTabla = new WeakHashMap<String,ValorFDDto>();
						mapaValoresRelacion = new AtributosMapDto();
						
						strTablaIndicador = itTablasIndicador.next();
						//Para cada Tabla PARTICIPANTE DE LA RELACION obtendo todos los atributos que a ella están asociados
						if (strTablaIndicador!=null && 
							((strTablaIndicador.equals(strNombreTablaRelacion)) || 
							(strTablaIndicador.equals(strNombreTablaRelacionada)))) {
							    							
							Set<String> setAtributosTabla = mapaTablas.get(strTablaIndicador).getContenido().keySet();
							Iterator<String> itAtributosTabla = setAtributosTabla.iterator();
							String strAtributoTabla = null;
							
							while (itAtributosTabla.hasNext()) {
								
								//Por cada atributo asociado a la tabla lo copio en la estructura que albergará el resultado final
								strAtributoTabla = itAtributosTabla.next();
								
								//Si ya existe la entrada en el mapa para la tabla, el atributo y este tiene valores	    									
								AtributosMapDto atributosTIndicador = mapaTablasRelacion.get(strTablaIndicador);
								
								if ( (atributosTIndicador==null) || ((atributosTIndicador!=null) && (atributosTIndicador.getContenidoTemporal()!=null) && (atributosTIndicador.getContenidoTemporal().get(strAtributoTabla)==null)) ||
										((atributosTIndicador!=null) && (atributosTIndicador.getContenidoTemporal()!=null) && 
										(atributosTIndicador.getContenidoTemporal().get(strAtributoTabla)!=null) && 
										(atributosTIndicador.getContenidoTemporal().get(strAtributoTabla).getValores()==null))) {
									
									List<EncapsuladorStringSW> listaValorAtributo = new ArrayList<EncapsuladorStringSW>();
		    						ValorFDDto datosColumnaAtributo = new ValorFDDto();	    	    	    						
		    						
		    						if (strTablaIndicador!=null && strTablaIndicador.equals(strNombreTablaRelacion)) {
		    							listaValorAtributo.add(mapaTablas.get(strTablaIndicador).getContenido().get(strAtributoTabla).getValores().get(indiceRelacionExterno));
		    						} else if (strTablaIndicador!=null && strTablaIndicador.equals(strNombreTablaRelacionada)) {
		    							listaValorAtributo.add(mapaTablas.get(strTablaIndicador).getContenido().get(strAtributoTabla).getValores().get(indiceRelacionInterno));
		    						}
		    						
		    						datosColumnaAtributo.setValores(listaValorAtributo);
									mapaColumnasIndicadorTabla.put(strAtributoTabla, datosColumnaAtributo);
									mapaValoresRelacion.setContenidoTemporal(mapaColumnasIndicadorTabla);
									mapaTablasRelacion.put(strTablaIndicador, mapaValoresRelacion);
									
									//Se inicializan los atributos utilizados para cada iteración a null para que el recolector de basura libere
									listaValorAtributo = null;
									datosColumnaAtributo = null;
									
								} else {
									WeakHashMap<String, ValorFDDto> contTempAtributos = atributosTIndicador.getContenidoTemporal();
									ValorFDDto valoresAtributoTabla = contTempAtributos.get(strAtributoTabla);
									if (strTablaIndicador!=null && strTablaIndicador.equals(strNombreTablaRelacion)) {
										valoresAtributoTabla.getValores().add(mapaTablas.get(strTablaIndicador).getContenido().get(strAtributoTabla).getValores().get(indiceRelacionExterno));
									} else if (strTablaIndicador!=null && strTablaIndicador.equals(strNombreTablaRelacionada)) {
										valoresAtributoTabla.getValores().add(mapaTablas.get(strTablaIndicador).getContenido().get(strAtributoTabla).getValores().get(indiceRelacionInterno));
									}
								}
							}
						}
					}
					mapaColumnasIndicadorTabla=null;
					mapaValoresRelacion=null;
				}
				indiceRelacionInterno++;
			} // FIN FOR  valoresColumnaRelacionada
			
			indiceRelacionExterno++;
		} // FIN valoresColumnaRelacion
			    			
		//En mapaTablas se sobreescribe las columnas de las tablas que participan en la relación para obtener el resultado final en mapaTablas
		volcarContenidoEnMapaTablas (mapaTablasRelacion, mapaTablas, strNombreTablaRelacion, strNombreTablaRelacionada);
		mapaTablasRelacion = null;
	}

    /*
     * Método que vuelca una estructura del tipo LinkedHashMap<String, AtributosMapDto> en una estructura del tipo WeakHashMap<String, ValorFDDto>
     */
	private WeakHashMap<String, ValorFDDto> volcarEnEstructuraFinal(EncapsuladorListSW<AtributoDto> listaAtributos,LinkedHashMap<String, AtributosMapDto> mapaTablas) {
		
		WeakHashMap<String,ValorFDDto> mapaColumnasRtdo = new WeakHashMap<String,ValorFDDto>();
		Set<String> setTablasResultado = mapaTablas.keySet();
		Iterator<String> itTablasResultado = setTablasResultado.iterator();
		String strTablaResultado = null;
		while (itTablasResultado.hasNext()) {			
			strTablaResultado = itTablasResultado.next();			
			Set<String> setAtributosTablaResultado = mapaTablas.get(strTablaResultado).getContenido().keySet();
			Iterator<String> itAtributosTablaResultado = setAtributosTablaResultado.iterator();
			String strAtributoTablaResultado = null;
			while (itAtributosTablaResultado.hasNext()) {
				strAtributoTablaResultado = itAtributosTablaResultado.next();
				if (mapaTablas.get(strTablaResultado).getContenido().get(strAtributoTablaResultado) != null) {
					if (existeAtributo (listaAtributos, strAtributoTablaResultado)) {
						mapaColumnasRtdo.put(strAtributoTablaResultado, mapaTablas.get(strTablaResultado).getContenido().get(strAtributoTablaResultado));
					}
				}
			}
		}
		return mapaColumnasRtdo;
	}

    /*
     * Método que filtra los datos cuando ya se ha establecido una relación entre las tablas
     */
	private void procesoFiltrarDatos(LinkedHashMap<String, AtributosMapDto> mapaTablas, ValorFDDto valoresColumnaRelacion, ValorFDDto valoresColumnaRelacionada,
			String strNombreTablaRelacion, String strNombreTablaRelacionada) {
		
		Set<Integer> registrosEliminar = new HashSet<Integer>();
		String strValorColumnaRelacion = null;
		String strValorColumnaRelacionada = null;
		for (int i=0; i<valoresColumnaRelacion.getValores().size(); i++) {

			strValorColumnaRelacion = valoresColumnaRelacion.getValores().get(i).getTexto();
			strValorColumnaRelacionada = valoresColumnaRelacionada.getValores().get(i).getTexto();
			
			if (strValorColumnaRelacion!=null && !strValorColumnaRelacion.equals(strValorColumnaRelacionada)) {
				registrosEliminar.add(i);
			}
		}
		eliminarValoresEnMapaTablas (registrosEliminar, mapaTablas,strNombreTablaRelacion, strNombreTablaRelacionada);
		registrosEliminar=null;
	}
    
    public AtributosMapDto cargarDatosMapaIndicador(EncapsuladorListSW<AtributoDto> listaAtributos, String caracterSeparador, Map<TiposFuente, String> mapaPath) {
    	AtributosMapDto mapaValores = new AtributosMapDto();
    	LinkedHashMap<String,ValorFDDto> mapaColumnas = new LinkedHashMap<String,ValorFDDto>();
    	//Para cada columna.
		//Para su tabla / fuente, obtengo los datos y me quedo con la columna correspondiente.
		//Asi hasta tener todas las columnas. Luego simplemente devolver la estructura.
    	
    	EncapsuladorIntegerSW numValoresAmbito=new EncapsuladorIntegerSW(0);
    	for ( AtributoDto attr : listaAtributos ) {
    		if(attr!=null && attr.getEsAmbito()!=null && attr.getColumna()!=null && (!attr.getEsMapa())){
    			numValoresAmbito= gestorCUAtributoFuenteDatos.cargaTamnhoColumna(attr.getColumna(), caracterSeparador, mapaPath);
    			break;
    		}
    	}
    	for ( AtributoDto attr : listaAtributos ) {
    		ValorFDDto datosColumna = new ValorFDDto();
    		if ( (attr.getColumna()==null && attr.getIndicadorExpresion()!=null) || attr.getColumna().getEsFormula()) {
    			AtributosMapDto datosTabla = new AtributosMapDto();
    			
    			List<String> listaCalculada = gestorCUExpresion.cargaEvaluacionExpresion(gestorCUExpresion.cargaPorId(attr.getIndicadorExpresion().getIdExpresion()), caracterSeparador, mapaPath,numValoresAmbito.getValor());
    			List<EncapsuladorStringSW> listaCalculadaEncap = new ArrayList<EncapsuladorStringSW>();
    			for ( String valor : listaCalculada ) {
    				EncapsuladorStringSW encapString = new EncapsuladorStringSW();
    				encapString.setTexto(valor);
    				listaCalculadaEncap.add(encapString);
    			}
    			datosColumna.setValores(listaCalculadaEncap);
	    		String nombreColumna = attr.getNombre();	    		
	    		
	    		mapaColumnas.put(nombreColumna, datosColumna);    			
    			
    		} else {
	    		AtributoFuenteDatosDto columna = attr.getColumna();
	    		TablaFuenteDatosDto tabla = columna.getTabla();
	    		
	    		AtributosMapDto datosTabla = gestorCUFuente.obtenerDatosTablaFuenteExternaCompletos(tabla.getFuente().getId(), tabla.getEsquema(), tabla.getNombre(), attr.getPath(), caracterSeparador);
	    		//Aqui tngo TODOS los datos para esta tabla. ahora extraigo a la estructura final la columna correspondiente.
	    		datosColumna = datosTabla.getContenido().get(columna.getNombre());
	    		String nombreColumna = columna.getNombre();
	    		
	    		mapaColumnas.put(nombreColumna, datosColumna);
    		}
    	}
    	mapaValores.setContenido(mapaColumnas);
    	return mapaValores;
    }
    
    public AtributosMapDto cargarMapaIndicador(EncapsuladorListSW<AtributoDto> listaAtributos, String caracterSeparador) {
    	AtributosMapDto mapaValores = new AtributosMapDto();
    	LinkedHashMap<String,ValorFDDto> mapaColumnas = new LinkedHashMap<String,ValorFDDto>();
    	//Para cada columna.
		//Para su tabla / fuente, obtengo los datos y me quedo con la columna correspondiente.
		//Asi hasta tener todas las columnas. Luego simplemente devolver la estructura.
    	for ( AtributoDto attr : listaAtributos ) {
    		if ( !attr.getEsMapa())
    			continue;
    		AtributoFuenteDatosDto columna = attr.getColumna();
    		TablaFuenteDatosDto tabla = columna.getTabla();
    		
    		AtributosMapDto datosTabla = gestorCUFuente.obtenerMapaTablaFuenteExterna(tabla.getFuente().getId(), tabla.getEsquema(), tabla.getNombre(), attr.getPath());
    		//Aqui tngo TODOS los datos para esta tabla. ahora extraigo a la estructura final la columna correspondiente.
    		ValorFDDto datosColumna = datosTabla.getContenido().get(columna.getNombre());
    		String nombreColumna = columna.getNombre();
    		
    		mapaColumnas.put(nombreColumna, datosColumna);
    	}
    	mapaValores.setContenido(mapaColumnas);
    	return mapaValores;
    }
    
    //FUNCIONES PRIVADAS DEL GESTOR
    
    private  AtributosMapDto filtraMapaValoresPorCritetios(AtributosMapDto mapaValores,List<AtributoDto> listaAtributos){
    	CriterioDto criterioDto=null;
    	Set<Integer> posicionesEliminar= new HashSet<Integer>();
    	
    	List<AtributoDto> listaAtributosExistentes= new ArrayList<AtributoDto>();
    	for(AtributoDto temp:listaAtributos){
    		if(mapaValores.getContenido().get(temp.getNombre())!=null)
    			listaAtributosExistentes.add(temp);
    	}
    		
    	
    	for(AtributoDto temp:listaAtributosExistentes){
    		criterioDto=gestorCUCriterio.cargaPorAtributo(temp.getId());
    		
    		if(criterioDto != null) {   		
	    		// Si es un campo de una tabla
	    		if (temp.getIndicadorExpresion() == null && temp.getColumna() != null && temp.getRelacion() == null)
	    		{
	    			posicionesEliminar.addAll(gestorCUCriterio.cargaPosicionesNoValidasESW(criterioDto, mapaValores.getContenido().get(temp.getNombre()).getValores(),temp.getColumna().getTipoAtributo()));
	    			continue;
	    		}
	    		// Si es una fórmula => los datos sólo pueden ser de tipo numérico
	    		else if (temp.getIndicadorExpresion() != null && temp.getRelacion() == null) {
	    			posicionesEliminar.addAll(gestorCUCriterio.cargaPosicionesNoValidasESW(criterioDto, mapaValores.getContenido().get(temp.getNombre()).getValores(), TipoAtributoFD.VALORFDNUMERICO));
	    			continue;
	    		}
	    	}
    	}
    	
    	for(int i:set2ListIntegerOrderDesc(posicionesEliminar)){
    		for(AtributoDto temp:listaAtributosExistentes){
    			try{
    				mapaValores.getContenido().get(temp.getNombre()).getValores().remove(i);
    			}catch (IndexOutOfBoundsException e) {
					// entra si la columna no tiene ese índice
				}
    		}
    	}

    	return mapaValores;
    	
    }
    
    private List<Integer> set2ListIntegerOrderDesc(Set<Integer> conjunto){
    	List<Integer> temp=new ArrayList<Integer>(conjunto);
    	Collections.sort(temp, new Comparator<Integer>() {

			public int compare(Integer o1, Integer o2) {
				return o2.compareTo(o1);
			}
    		
		});
    	return temp;
    }
    
    private boolean esFecha(String cadena) {
    	if (UtilFecha.multiParse(cadena)==null)
    		return false;
    	else
    		return true;
    }
    
    public void borraPorIdIndicador(Long idIndicador) {
    	Set<Long> listaAtributosFuenteDatosBorrar = new HashSet<Long>();
		Set<Long> listaIndicadoresExpresionBorrar = new HashSet<Long>();
		Set<Long> listaRelacionBorrar = new HashSet<Long>();
		Set<Long> listaTablasBorrar = new HashSet<Long>();
		Set<Long> listaAtributosBorrar = new HashSet<Long>();
		
    	EncapsuladorListSW<AtributoDto> listaAtributos = cargaPorIndicador(idIndicador);
    	
    	for ( AtributoDto attr : listaAtributos ) {
			listaAtributosBorrar.add(attr.getId());
			if ( attr.getColumna()!=null) {
				listaAtributosFuenteDatosBorrar.add(attr.getColumna().getId());
				listaTablasBorrar.add(attr.getColumna().getTabla().getId());
			}
			if ( attr.getIndicadorExpresion()!=null)
				listaIndicadoresExpresionBorrar.add(attr.getIndicadorExpresion().getId());
			
			if ( attr.getRelacion()!=null)
				listaRelacionBorrar.add(attr.getRelacion().getId());
		}
		for ( Long idAttr : listaAtributosFuenteDatosBorrar ) {
			gestorCUAtributoFuenteDatos.borra(idAttr);
		}
		for ( Long idTabla : listaTablasBorrar ) {
			gestorCUTablaFuenteDatos.borra(idTabla, null);
		}
		for ( Long idAtributo : listaAtributosBorrar ) {
			borra(idAtributo,null);
		}
		for ( Long idRel : listaRelacionBorrar ) {
			gestorCURelacion.borra(idRel);
		}
		for ( Long idIndiExpre : listaIndicadoresExpresionBorrar ) {
			gestorCUIndicadorExpresion.borra(idIndiExpre);
		}
    }
    
    /*
     * Método que se encarga de almacenar el atributo (lista atributos) junto con su lista de valores
     * en el mapa de tablas que posteriormente será entrada para gestionar las relaciones
     */
    private void almacenarAtributoTabla (AtributosMapDto mapaTablaTratada, LinkedHashMap<String,AtributosMapDto> mapaTablas, 
    		String strNombreTabla, String strNombreColumna, ValorFDDto colDatosColumna) {
    
    	AtributosMapDto mapaAtributosTabla;
    	LinkedHashMap<String,ValorFDDto> mapaColumnaTabla;
    	
    	if (mapaTablaTratada!=null) {
    		LinkedHashMap<String,ValorFDDto> mapaColumnasTablaTratada = mapaTablas.get(strNombreTabla).getContenido();
    		mapaColumnasTablaTratada.put(strNombreColumna, colDatosColumna);
    	//SI ES LA PRIMERA VEZ, NO EXISTE LA TABLA EN EL MAPA DE TABLAS
    	} else {
        	//Atributo que se reinicia cada vez que incorporamos un atributo
    		mapaAtributosTabla = new AtributosMapDto();
    		mapaColumnaTabla = new LinkedHashMap<String,ValorFDDto>();
    		mapaColumnaTabla.put(strNombreColumna, colDatosColumna);
    		mapaAtributosTabla.setContenido(mapaColumnaTabla);
    		mapaTablas.put(strNombreTabla, mapaAtributosTabla);
    	}
    	mapaAtributosTabla = null;
    	mapaColumnaTabla = null;
    }
    
    /*
     * Método que comprueba si en la lista de atributos que no sean relaciones 
     * existe el atributo concreto que se le pasa como parámetro
     * Devuelve TRUE si existe el atributo, FALSE en caso contrario
     */
    private boolean existeAtributo (List<AtributoDto> listaAtributos, String strNombreAtributo) {
 		
    	boolean encontrado = false;
 		
    	for ( AtributoDto atrBuscar : listaAtributos ) {
 			if (atrBuscar.getRelacion()==null) {
 				if (atrBuscar.getNombre().equals(strNombreAtributo)) {
 					encontrado = true;
 					break;
 				}
 			} else {
 				continue;
 			}
 		}
 		return encontrado;
    }
    
	/*
	 * Método que comprueba en el mapa de columnas si ya existe la columna strNombreColumnaInicial, si existe
	 * se busca el nombre de la columna + un secuencial.
	 * Se introduce en el mapa de Columnas Auxiliar
	 */
	private String obtenerNombreColumnaMapa (String strNombreColumnaInicial, 
			WeakHashMap<String,ValorFDDto> mapaColumnasAux, ValorFDDto colDatosColumna) {

		String nombreColumna = strNombreColumnaInicial;
		Integer i = 1; 
		while ( mapaColumnasAux.get(nombreColumna)!=null ) {
			nombreColumna = nombreColumna+i.toString();
			i++;
		}
		//MAPA COLUMNAS AUXILIAR UTILIZADO PARA ALMACENAR LAS COLUMNAS Y CONTROLAR LAS COLUMNAS REPETIDAS
		mapaColumnasAux.put(nombreColumna, colDatosColumna);
		return nombreColumna;
	}
	
	/*
	 * Método que vuelca el resultado de de la gestión de la relación como mapa en memoria
	 * para la gestión de las relaciones y la visualización de la información final
	 */
	private void volcarContenidoEnMapaTablas (WeakHashMap<String,AtributosMapDto> mapaTablasRelacion, 
			LinkedHashMap<String,AtributosMapDto> mapaTablas, 
			String strNombreTablaRelacion, String strNombreTablaRelacionada) {
		
		/*
		 * Si el tamaño de mapasTablasRelacion es igual a CERO quiere decir que en la relación que se ha analizado no se han obtenido valores iguales,
		 * mapaTablas deberia estar vacio
		 */
		if (mapaTablasRelacion.size()==0) {
			//Inicializar MapaTablas, se recorren las tablas y por cada uno de sus atributos se inicializa a NULL
			inicializarEstructura(mapaTablas);
		}

		/*
		 * Si el tamaño de mapaTablasRelación no es igual a CERO quiere decir que en la relación que se ha analizado se han obtenido valores iguales,
		 * en mapaTablas se actualizaran los atributos que han formado parte de la relación  
		 */
		Set<String> setTablasIndicadorRelacion = mapaTablasRelacion.keySet();
		Iterator<String> itTablasIndicadorRelacion = setTablasIndicadorRelacion.iterator();
		String strTablaIndicadorRelacion = null;
		while (itTablasIndicadorRelacion.hasNext()) {			
			strTablaIndicadorRelacion = itTablasIndicadorRelacion.next();			
			//Para cada Tabla PARTICIPANTE DE LA RELACION obtendo todos los atributos que a ella están asociados para inicializar sus valores
			if (strTablaIndicadorRelacion!=null &&
				((strTablaIndicadorRelacion.equals(strNombreTablaRelacion)) || 
				(strTablaIndicadorRelacion.equals(strNombreTablaRelacionada)))) {

				Set<String> setAtributosTablaRelacion = mapaTablasRelacion.get(strTablaIndicadorRelacion).getContenidoTemporal().keySet();
				Iterator<String> itAtributosTablaRelacion = setAtributosTablaRelacion.iterator();
				String strAtributoTablaRelacion = null;
				while (itAtributosTablaRelacion.hasNext()) {
					
					strAtributoTablaRelacion = itAtributosTablaRelacion.next();
					mapaTablas.get(strTablaIndicadorRelacion).getContenido().get(strAtributoTablaRelacion).setValores(null);
					mapaTablas.get(strTablaIndicadorRelacion).getContenido().get(strAtributoTablaRelacion).setValores(mapaTablasRelacion.get(strTablaIndicadorRelacion).getContenidoTemporal().get(strAtributoTablaRelacion).getValores());
					
				}
			}
		}
	}

	/*
	 * Método que inicializa una estructura del tipo LinkedHashMap<String, AtributosMapDto>
	 */
	private void inicializarEstructura(LinkedHashMap<String, AtributosMapDto> mapaTablas) {
		
		Set<String> setTablasResultado = mapaTablas.keySet();
		Iterator<String> itTablasResultado = setTablasResultado.iterator();
		String strTablaResultado = null;
		while (itTablasResultado.hasNext()) {			
			strTablaResultado = itTablasResultado.next();			
			Set<String> setAtributosTablaResultado = mapaTablas.get(strTablaResultado).getContenido().keySet();
			Iterator<String> itAtributosTablaResultado = setAtributosTablaResultado.iterator();
			String strAtributoTablaResultado = null;
			while (itAtributosTablaResultado.hasNext()) {
				strAtributoTablaResultado = itAtributosTablaResultado.next();
				mapaTablas.get(strTablaResultado).getContenido().get(strAtributoTablaResultado).setValores(null);
			}
		}
	}
	
	/*
	 * Método que elimina las entradas de mapa tablas que se almacenan en registrosEliminar
	 */
	private void eliminarValoresEnMapaTablas (Set<Integer> registrosEliminar, LinkedHashMap<String,AtributosMapDto> mapaTablas,
			String strNombreTablaRelacion, String strNombreTablaRelacionada) {

		Set<String> setTablasIndicador = mapaTablas.keySet();
		Iterator<String> itTablasIndicador = setTablasIndicador.iterator();
		String strTablaIndicador = null;
		while (itTablasIndicador.hasNext()) {			
			strTablaIndicador = itTablasIndicador.next();			
			//Para cada Tabla PARTICIPANTE del resultado final debo eliminar los elementos que se encuentran en la lista de registrosEliminar
			if (strTablaIndicador!=null &&
					((strTablaIndicador.equals(strNombreTablaRelacion)) || 
					(strTablaIndicador.equals(strNombreTablaRelacionada)))) {
				Set<String> setAtributosTabla = mapaTablas.get(strTablaIndicador).getContenido().keySet();
				Iterator<String> itAtributosTabla = setAtributosTabla.iterator();
				String strAtributoTabla = null;
				while (itAtributosTabla.hasNext()) {
					strAtributoTabla = itAtributosTabla.next();
					for (int i:set2ListIntegerOrderDesc(registrosEliminar)) {
						try {
							mapaTablas.get(strTablaIndicador).getContenido().get(strAtributoTabla).getValores().remove(i);
						} catch (IndexOutOfBoundsException e) {
							// entra si la columna no tiene ese índice
						}	
					}
				}
			}
		}
	}
	
	/*
	 * Método que a partir de los atributos de entrada recupera las relaciones en una estructura
	 * TablaOrigen, TablaDestino, lista de atributos que relacionan las tablas
	 */
	private MapaRelaciones<String, String> obtenerRelacionesOrden (EncapsuladorListSW<AtributoDto> listaAtributosEntrada) {
		
		MapaRelaciones<String, String> mapaRelaciones = new MapaRelaciones<String, String>();
		 
		for (AtributoDto atributo : listaAtributosEntrada) {
			
 			if (atributo.getRelacion()!=null) {
 				
 				mapaRelaciones.put(atributo.getRelacion().getNombreTablaRelacion(),atributo.getRelacion().getNombreTablaRelacionada(),
 						atributo.getRelacion().getNombreColumnaRelacion(), atributo.getRelacion().getNombreColumnaRelacionada());
 			}
 		}
		return mapaRelaciones;
	}
}