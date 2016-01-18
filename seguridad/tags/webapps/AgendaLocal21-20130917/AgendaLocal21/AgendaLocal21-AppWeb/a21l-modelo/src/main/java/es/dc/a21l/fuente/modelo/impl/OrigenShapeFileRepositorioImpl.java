/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.modelo.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import com.vividsolutions.jts.geom.Geometry;

import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorStringSW;
import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.base.utils.enumerados.TipoAtributoFD;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.DescripcionAtributoDto;
import es.dc.a21l.fuente.cu.TablaFuenteDatosDto;
import es.dc.a21l.fuente.cu.ValorFDDto;
import es.dc.a21l.fuente.modelo.Fuente;
import es.dc.a21l.fuente.modelo.OrigenShapeFileRepositorio;

public class OrigenShapeFileRepositorioImpl extends RepositorioBaseImpl<Fuente> implements OrigenShapeFileRepositorio {
	private static final Logger log = LoggerFactory.getLogger(OrigenShapeFileRepositorioImpl.class);
	private static final String DATO_GEOMETRIA = "the_geom";
	
	public EncapsuladorListSW<TablaFuenteDatosDto> listarTablasFuenteExterna(Fuente fuente, String path) {
    	EncapsuladorListSW<TablaFuenteDatosDto> listaTablas = new EncapsuladorListSW<TablaFuenteDatosDto>();
    	
    	TablaFuenteDatosDto tabla = new TablaFuenteDatosDto();
    	tabla.setNombre(fuente.getNombre());
    	listaTablas.add(tabla);
    	return listaTablas;    	
    }
	
    public EncapsuladorListSW<AtributoFuenteDatosDto> listarColumnasTablaFuenteExterna(Fuente fuente, String tabla, String path, boolean anhadirGeometria) {
    	
    	String cadenaConexionDbf = path+fuente.getId()+"/"+fuente.getFich_dbf();
    	
    	EncapsuladorListSW<AtributoFuenteDatosDto> listaColumnas = new EncapsuladorListSW<AtributoFuenteDatosDto>();
    	InputStream inputStream = null;
    	try {
    		inputStream  = new FileInputStream( cadenaConexionDbf ); // fichero dbf
    		DBFReader reader = new DBFReader( inputStream ); 
    		int numeroCampos = reader.getFieldCount();
    		Object []filaElementos;
    		for( int i=0; i<numeroCampos; i++) {
    			DBFField field = reader.getField( i);
    			filaElementos = reader.nextRecord();
    			AtributoFuenteDatosDto attr = new AtributoFuenteDatosDto();
    			attr.setNombre(field.getName());
    			TipoAtributoFD tipoDato = obtenerTipoColumna(field,filaElementos[i]);
    			if ( tipoDato == null )
    				throw new Exception();
    			attr.setTipoAtributo(tipoDato);
    			listaColumnas.add(attr);
    		}    		
    		inputStream.close();
	    } catch( DBFException e) {
	    	log.debug(e.getMessage());
	    } catch( IOException e) {
	    	log.debug("No se puede leer el fichero. "+e.getMessage());
	    } catch (Exception ex) {
	    	log.debug("Error desconocido al acceder al fichero ShapeFile");
	    } finally {
			try {
				if ( inputStream!=null)
					inputStream.close();
			} catch (IOException e) {
				log.debug("no se pudo cerrar el fichero");
			}
		}
    	
    	if ( anhadirGeometria ) {
	    	//Añado la columna con los datos geograficos
	    	AtributoFuenteDatosDto columnaGeo = new AtributoFuenteDatosDto();
	    	columnaGeo.setNombre(DATO_GEOMETRIA);
	    	columnaGeo.setDefinicion("");
	    	columnaGeo.setTipoAtributo(TipoAtributoFD.VALORFDGEOGRAFICO);
	    	listaColumnas.add(columnaGeo);
    	}
    	
    	return listaColumnas;
    }
    public EncapsuladorListSW<DescripcionAtributoDto> obtenerEsquemaTablaFuenteExterna(Fuente fuente, String tabla, String path) {
    	List<AtributoFuenteDatosDto> listaColumnas = listarColumnasTablaFuenteExterna(fuente, tabla, path, true);
    	EncapsuladorListSW<DescripcionAtributoDto> listaEsquema = new EncapsuladorListSW<DescripcionAtributoDto>();
    	for ( AtributoFuenteDatosDto attr : listaColumnas ) {
    		DescripcionAtributoDto desc = new DescripcionAtributoDto();
    		desc.setNombre(attr.getNombre());
    		desc.setTipo(attr.getTipoAtributo());
    		desc.setDefinicion(attr.getDefinicion());
			listaEsquema.add(desc);
    	}
    	return listaEsquema;
    }
    
    public AtributosMapDto obtenerDatosTablaFuenteExterna(Fuente fuente, String tabla, String path, boolean completos) {
    	String cadenaConexionDbf = path+fuente.getId()+"/"+fuente.getFich_dbf();
    	List<AtributoFuenteDatosDto> listaColumnas = listarColumnasTablaFuenteExterna(fuente, tabla, path, false);
    	AtributosMapDto mapaValores = new AtributosMapDto();
    	DBFReader reader = null;
    	InputStream inputStream = null;
    	try {
    		inputStream  = new FileInputStream( cadenaConexionDbf ); // fichero dbf
    		reader = new DBFReader( inputStream); 
    		Object []filaElementos;
    		while (( filaElementos = reader.nextRecord()) != null ) {
    			for( int i=0; i<filaElementos.length; i++) {
                    String nombreColumna = new String();
	        		nombreColumna = listaColumnas.get(i).getNombre();
	        		EncapsuladorStringSW valorColumna = new EncapsuladorStringSW();
	        		valorColumna.setTexto(obtenerStringValor(filaElementos[i],listaColumnas.get(i).getTipoAtributo()));
                    
                    ValorFDDto valorFDTmp = mapaValores.getContenido().get(nombreColumna);
                    if ( valorFDTmp == null )
                    	valorFDTmp = new ValorFDDto();
                    valorFDTmp.getValores().add(valorColumna);
    	        	mapaValores.setValor(nombreColumna, valorFDTmp);
                }
    		}
    		
    		inputStream.close();
	    } catch( DBFException e) {
	    	log.debug(e.getMessage());
	    } catch( IOException e) {
	    	log.debug("No se puede leer el fichero: "+e.getMessage());
	    } finally {
			try {
				if ( inputStream!=null)
					inputStream.close();
			} catch (IOException e) {
				log.debug("no se pudo cerrar el fichero");
			}
		}
    	
    	if ( completos ) {
    		AtributosMapDto datosMapa = new AtributosMapDto();
    		datosMapa = obtenerMapaTablaFuenteExterna(fuente, tabla, path);
    		LinkedHashMap<String,ValorFDDto> datos = new LinkedHashMap<String,ValorFDDto>();
    		datos = mapaValores.getContenido();
    		datos.put(DATO_GEOMETRIA, datosMapa.getContenido().get(DATO_GEOMETRIA));
    		mapaValores.setContenido(datos);
    	}
	    return mapaValores;
    }
    
    public AtributosMapDto obtenerMapaTablaFuenteExterna(Fuente fuente, String tabla, String path) {
    	String cadenaConexionShp = path+fuente.getId()+"/"+fuente.getFich_shp();
    	AtributosMapDto mapaValores = new AtributosMapDto();
    	
    	//Hemos obtenido los valores del fichero dbf, ahora obtendremos los valores geograficos
    	try {
	    	File file = new File( cadenaConexionShp );
			Map<String, Serializable> parametrosConexion = new HashMap<String, Serializable>();
			parametrosConexion.put("url", file.toURI().toURL());
			DataStore dataStore = DataStoreFinder.getDataStore(parametrosConexion);
			String[] typeNames = dataStore.getTypeNames();
			String typeName = typeNames[0];
			FeatureSource origen = dataStore.getFeatureSource(typeName); 
			FeatureCollection<SimpleFeatureType, SimpleFeature> collection = origen.getFeatures();
			FeatureIterator<SimpleFeature> iterador = collection.features();
			String nombreColumna = DATO_GEOMETRIA;
			
			try {
				while (iterador.hasNext()) {
					SimpleFeature feature = iterador.next();
					Geometry geometria = (Geometry) feature.getDefaultGeometry();
					ValorFDDto valorFDTmp = mapaValores.getContenido().get(nombreColumna);
                    if ( valorFDTmp == null )
                    	valorFDTmp = new ValorFDDto();
                    EncapsuladorStringSW valorColumna = new EncapsuladorStringSW();
                    valorColumna.setTexto(geometria.toText().trim());
                    valorFDTmp.getValores().add(valorColumna);
    	        	mapaValores.setValor(nombreColumna, valorFDTmp);
				} // while iterator.hasNext()
			} finally {
				if (iterador != null) {
					iterador.close();
				}
			}
    	} catch(Exception ex ) {
    		log.debug("Se ha producido algun error al procesar el fichero shp, dbf, shx: ");
    		ex.printStackTrace();
    		return mapaValores;
    	}
		return mapaValores;
    }
    
    private TipoAtributoFD obtenerTipoColumna(DBFField field, Object valor) {
    	TipoAtributoFD tipo;
    	if ( field.getDataType() == DBFField.FIELD_TYPE_C )
    		tipo = TipoAtributoFD.VALORFDTEXTO;
    	else if ( field.getDataType() == DBFField.FIELD_TYPE_D )
    		tipo = TipoAtributoFD.VALORFDFECHA;
    	else if ( field.getDataType() == DBFField.FIELD_TYPE_F )
    		tipo = TipoAtributoFD.VALORFDNUMERICO;
    	else if ( field.getDataType() == DBFField.FIELD_TYPE_N )
    		tipo = TipoAtributoFD.VALORFDNUMERICO;
    	else tipo = null;
    	
    	if ( tipo == TipoAtributoFD.VALORFDTEXTO ) {
    		try {
        		int i = Integer.valueOf(String.valueOf(valor).trim()).intValue();
        		return TipoAtributoFD.VALORFDNUMERICO;
        	} catch (Exception ex ) {
        		try {
        			float f = Float.valueOf(String.valueOf(valor).trim()).floatValue();
        			tipo = TipoAtributoFD.VALORFDNUMERICO;
        		} catch ( Exception ex1 ) {
        				tipo = TipoAtributoFD.VALORFDTEXTO;
        		}
        	}
    	}
    	return tipo;
    }
    
    private String obtenerStringValor(Object valor, TipoAtributoFD tipo) {
    	String respuesta = null;
    	if ( tipo == TipoAtributoFD.VALORFDNUMERICO ){
    		respuesta = String.valueOf(valor).trim();
    		respuesta = respuesta.trim();
    	}
    	else if ( tipo == TipoAtributoFD.VALORFDFECHA )
    		respuesta = (String)valor;
    	else if ( tipo == TipoAtributoFD.VALORFDTEXTO){
    		respuesta = (String)valor;
    		respuesta = respuesta.trim();
    	}
    	return respuesta;
    }
    
    public boolean probarFuente(Fuente fuente, String path) {
    	String cadenaConexionDbf = path+fuente.getId()+"/"+fuente.getFich_dbf();
    	boolean resultado = false;
    	    	
    	try {
    		InputStream inputStream  = new FileInputStream( cadenaConexionDbf ); // fichero dbf
    		DBFReader reader = new DBFReader( inputStream ); 
    		inputStream.close();
    		resultado = true;
	    } catch( DBFException e) {
	    	resultado = false;
	    	log.debug(e.getMessage());
	    	return resultado;
	    } catch( IOException e) {
	    	resultado = false;
	    	log.debug("No se puede leer el fichero. "+e.getMessage());
	    	return resultado;
	    } catch (Exception ex) {
	    	resultado = false;
	    	log.debug("Error desconocido al acceder al fichero ShapeFile");
	    	return resultado;
	    }
    	return resultado;
    }
}