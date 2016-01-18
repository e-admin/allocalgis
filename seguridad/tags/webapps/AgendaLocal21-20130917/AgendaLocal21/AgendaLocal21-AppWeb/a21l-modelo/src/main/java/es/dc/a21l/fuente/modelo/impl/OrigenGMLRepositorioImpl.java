/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.modelo.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.geotools.gml.GMLFilterDocument;
import org.geotools.gml.GMLFilterFeature;
import org.geotools.gml.GMLFilterGeometry;
import org.geotools.gml.GMLHandlerFeature;
import org.opengis.feature.Attribute;
import org.opengis.feature.simple.SimpleFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorMapSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorStringSW;
import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.base.modelo.utils.UtilFecha;
import es.dc.a21l.base.utils.enumerados.TipoAtributoFD;
import es.dc.a21l.base.utils.enumerados.TiposDatoGML;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.DescripcionAtributoDto;
import es.dc.a21l.fuente.cu.TablaFuenteDatosDto;
import es.dc.a21l.fuente.cu.ValorFDDto;
import es.dc.a21l.fuente.modelo.Fuente;
import es.dc.a21l.fuente.modelo.OrigenGMLRepositorio;


public class OrigenGMLRepositorioImpl extends RepositorioBaseImpl<Fuente> implements OrigenGMLRepositorio {
	private static final Logger log = LoggerFactory.getLogger(OrigenGMLRepositorioImpl.class);
	private File fichero;
	private static final String DATO_GEOMETRIA = "the_geom";
	
	public File getFichero() {
		return fichero;
	}
	public void setFichero(File fichero) {
		this.fichero = fichero;
	}
	
	public EncapsuladorListSW<TablaFuenteDatosDto> listarTablasFuenteExterna(Fuente fuente, String path) {
    	String nombre = fuente.getNombre();
    	EncapsuladorListSW<TablaFuenteDatosDto> listaTablas = new EncapsuladorListSW<TablaFuenteDatosDto>();
    	TablaFuenteDatosDto tabla = new TablaFuenteDatosDto();
    	tabla.setNombre(nombre);
    	listaTablas.add(tabla);
    	return listaTablas;    	
    }
	
    public EncapsuladorListSW<AtributoFuenteDatosDto> listarColumnasTablaFuenteExterna(Fuente fuente, String tabla, String path) {
    	String cadenaConexion = path+fuente.getId()+"/"+fuente.getFich_csv_gml();
    	
    	EncapsuladorListSW<AtributoFuenteDatosDto> listaColumnas = new EncapsuladorListSW<AtributoFuenteDatosDto>();
    	InputStream is = null;
    	try  {
    		this.setFichero(new File(cadenaConexion));
    		is = new FileInputStream (this.getFichero());
    		InputSource input = new InputSource(is);
    		
    		class InlineHandler extends XMLFilterImpl implements GMLHandlerFeature {
    		    List<SimpleFeature> features = new ArrayList<SimpleFeature>();
    		    
    		    public void feature(SimpleFeature feature) {
    		        features.add(feature);
    		    }
    		}
		    InlineHandler inlineHandler = new InlineHandler();
		    GMLFilterFeature filterFeature = new GMLFilterFeature(inlineHandler);
		    GMLFilterGeometry filterGeometry = new GMLFilterGeometry(filterFeature);
		    GMLFilterDocument filterDocument = new GMLFilterDocument(filterGeometry);
		    
		    // parse xml
		    XMLReader reader = XMLReaderFactory.createXMLReader();
		    reader.setContentHandler(filterDocument);
		    reader.parse(input);
		    
		    List<SimpleFeature> features = inlineHandler.features;
    		for ( SimpleFeature elemento : features ) {
    			for ( Object attr : elemento.getProperties()) {
    				Attribute simpleAttr = (Attribute)attr;
    				AtributoFuenteDatosDto col = new AtributoFuenteDatosDto();
    				col.setNombre(simpleAttr.getName().toString());
    				col.setTipoAtributo(obtenerTipoColumna(simpleAttr.getType().toString(),simpleAttr.getValue().toString()));
    				col.setDefinicion(simpleAttr.getDescriptor().toString());
    				listaColumnas.add(col);
    			}
    			//Solo necesito las caracteristicas de la primera feature para saber las columnas.
				break;
    		}
    		is.close();
    	} catch ( FileNotFoundException fnf) {
    		log.debug("No se ha encontrado el fichero: "+cadenaConexion);
    		return null;
    	} catch ( IOException ioe) {
    		log.debug("Error de lectura del fichero csv: "+cadenaConexion);
    		return null;
		} catch ( Exception ex) {
			log.debug("Error desconocido al leer el fichero csv: "+cadenaConexion);
			return null;
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				log.debug("no se pudo cerrar el fichero");
			}
		}
    	return listaColumnas;
    }
    public EncapsuladorListSW<DescripcionAtributoDto> obtenerEsquemaTablaFuenteExterna(Fuente fuente, String tabla, String path) {
    	List<AtributoFuenteDatosDto> listaColumnas = listarColumnasTablaFuenteExterna(fuente, tabla, path);
    	EncapsuladorListSW<DescripcionAtributoDto> listaEsquema = new EncapsuladorListSW<DescripcionAtributoDto>();
    	if ( listaColumnas == null ) return listaEsquema;
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
    	String cadenaConexion = path+fuente.getId()+"/"+fuente.getFich_csv_gml();
    	AtributosMapDto mapaValores = new AtributosMapDto();
    	InputStream is = null;
    	try  {
    		this.setFichero(new File(cadenaConexion));
    		is = new FileInputStream (this.getFichero());
    		InputSource input = new InputSource(is);
    		
    		class InlineHandler extends XMLFilterImpl implements GMLHandlerFeature {
    		    List<SimpleFeature> features = new ArrayList<SimpleFeature>();
    		    
    		    public void feature(SimpleFeature feature) {
    		        features.add(feature);
    		    }
    		}
		    InlineHandler inlineHandler = new InlineHandler();
		    GMLFilterFeature filterFeature = new GMLFilterFeature(inlineHandler);
		    GMLFilterGeometry filterGeometry = new GMLFilterGeometry(filterFeature);
		    GMLFilterDocument filterDocument = new GMLFilterDocument(filterGeometry);
		    
		    // parse xml
		    XMLReader reader = XMLReaderFactory.createXMLReader();
		    reader.setContentHandler(filterDocument);
		    reader.parse(input);
		    
		    List<SimpleFeature> features = inlineHandler.features;
    		for ( SimpleFeature elemento : features ) {
    			EncapsuladorMapSW<EncapsuladorStringSW,EncapsuladorStringSW> mapaFila = new EncapsuladorMapSW<EncapsuladorStringSW,EncapsuladorStringSW>();
    			for ( Object attr : elemento.getProperties()) {
    				Attribute simpleAttr = (Attribute)attr;
    				
    				String nombreColumna = simpleAttr.getName().toString();
	        		EncapsuladorStringSW valorColumna = new EncapsuladorStringSW();
	        		if ( !completos ) {
		        		if ( obtenerTipoColumna(simpleAttr.getType().toString(),simpleAttr.getValue().toString()) == TipoAtributoFD.VALORFDGEOGRAFICO) {
		        			valorColumna.setTexto("datos mapa");
		        		} else 
		        			valorColumna.setTexto(simpleAttr.getValue().toString().trim());
	        		} else {
	        			valorColumna.setTexto(simpleAttr.getValue().toString().trim());
	        		}
	        		ValorFDDto valorFDTmp = mapaValores.getContenido().get(nombreColumna);
	        		if ( valorFDTmp == null )
                    	valorFDTmp = new ValorFDDto();
                    valorFDTmp.getValores().add(valorColumna);
    	        	mapaValores.setValor(nombreColumna, valorFDTmp);
    	        	mapaFila.put(new EncapsuladorStringSW(nombreColumna), valorColumna);
    			}
    		}    		
    	} catch ( FileNotFoundException fnf) {
    		log.debug("No se ha encontrado el fichero gml: "+cadenaConexion);
    		return null;
    	} catch ( IOException ioe) {
    		log.debug("Error de lectura del fichero gml: "+cadenaConexion);
    		return null;
		} catch ( Exception ex) {
			log.debug("Error desconocido al leer el fichero gml: "+cadenaConexion);
			return null;
		}  finally {
			try {
				if ( is!=null)
					is.close();
			} catch (Exception e) {
				log.debug("no se pudo cerrar el fichero");
			}
		}
    	return mapaValores;    	
    }
    
    public AtributosMapDto obtenerMapaTablaFuenteExterna(Fuente fuente, String tabla, String path) {
    	String cadenaConexion = path+fuente.getId()+"/"+fuente.getFich_csv_gml();
    	AtributosMapDto mapaValores = new AtributosMapDto();
    	InputStream is = null;
    	try  {
    		this.setFichero(new File(cadenaConexion));
    		is = new FileInputStream (this.getFichero());
    		InputSource input = new InputSource(is);
    		
    		class InlineHandler extends XMLFilterImpl implements GMLHandlerFeature {
    		    List<SimpleFeature> features = new ArrayList<SimpleFeature>();
    		    
    		    public void feature(SimpleFeature feature) {
    		        features.add(feature);
    		    }
    		}
		    InlineHandler inlineHandler = new InlineHandler();
		    GMLFilterFeature filterFeature = new GMLFilterFeature(inlineHandler);
		    GMLFilterGeometry filterGeometry = new GMLFilterGeometry(filterFeature);
		    GMLFilterDocument filterDocument = new GMLFilterDocument(filterGeometry);
		    
		    // parse xml
		    XMLReader reader = XMLReaderFactory.createXMLReader();
		    reader.setContentHandler(filterDocument);
		    reader.parse(input);
		    
		    List<SimpleFeature> features = inlineHandler.features;
    		for ( SimpleFeature elemento : features ) {
    			EncapsuladorMapSW<EncapsuladorStringSW,EncapsuladorStringSW> mapaFila = new EncapsuladorMapSW<EncapsuladorStringSW,EncapsuladorStringSW>();
    			for ( Object attr : elemento.getProperties()) {
    				Attribute simpleAttr = (Attribute)attr;
    				
    				String nombreColumna = simpleAttr.getName().toString();
	        		EncapsuladorStringSW valorColumna = new EncapsuladorStringSW();
	        		//Solo quiero la columna con datos geograficos. Pero puede tener la columna boundedby que tiene los limites de cada feature. Qeu no
	        		//nos interesan
	        		if ( obtenerTipoColumna(simpleAttr.getType().toString(),simpleAttr.getValue().toString()) == TipoAtributoFD.VALORFDGEOGRAFICO && !nombreColumna.toLowerCase().equalsIgnoreCase("boundedby")) {
	        			valorColumna.setTexto(simpleAttr.getValue().toString().trim());
	        			ValorFDDto valorFDTmp = mapaValores.getContenido().get(nombreColumna);
		        		if ( valorFDTmp == null )
	                    	valorFDTmp = new ValorFDDto();
	                    valorFDTmp.getValores().add(valorColumna);
	    	        	mapaValores.setValor(nombreColumna, valorFDTmp);
	    	        	mapaFila.put(new EncapsuladorStringSW(nombreColumna), valorColumna);
	        		} else 
	        			continue;		
    			}
    		}    		
    	} catch ( FileNotFoundException fnf) {
    		log.debug("No se ha encontrado el fichero: "+cadenaConexion);
    		return null;
    	} catch ( IOException ioe) {
    		log.debug("Error de lectura del fichero csv: "+cadenaConexion);
    		return null;
		} catch ( Exception ex) {
			log.debug("Error desconocido al leer el fichero csv: "+cadenaConexion);
			return null;
		} finally {
			try {
				if ( is!=null)
					is.close();
			} catch (Exception e) {
				log.debug("no se pudo cerrar el fichero");
			}
		}  	
    	return mapaValores;
    }
    
    private TipoAtributoFD obtenerTipoColumna(String tipo, String valor) {
    	try {
	    	if ( tipo.contains(TiposDatoGML.INTEGER.getDescripcion()) || tipo.contains(TiposDatoGML.DOUBLE.getDescripcion()))
	    		return TipoAtributoFD.VALORFDNUMERICO;
	    	else if ( tipo.contains(TiposDatoGML.STRING.getDescripcion())) {
	    		if ( esFecha(valor))
	    			return TipoAtributoFD.VALORFDFECHA;
	    		else
	    			return TipoAtributoFD.VALORFDTEXTO;
	    	} else if ( tipo.contains(TiposDatoGML.GEOMETRIA.getDescripcion()))
	    		return TipoAtributoFD.VALORFDGEOGRAFICO;
	    	else return TipoAtributoFD.VALORFDTEXTO;
    	} catch ( Exception ex) {
    		log.debug("Error al obtener el tipo de la columna");
    		return TipoAtributoFD.VALORFDTEXTO;
    	}
    }
    
    private boolean esFecha(String cadena) {
    	if (UtilFecha.multiParse(cadena)==null)
    		return false;
    	else
    		return true;
    }
    
    public boolean probarFuente(Fuente fuente, String path) {
    	String cadenaConexion = path+fuente.getId()+"/"+fuente.getFich_csv_gml();
    	boolean resultado = false;
    	try  {
    		this.setFichero(new File(cadenaConexion));
    		InputStream is = new FileInputStream (this.getFichero());
    		InputSource input = new InputSource(is);
    		resultado = true;
    	} catch ( FileNotFoundException fnf) {
    		resultado = false;
    		log.debug("No se ha encontrado el fichero: "+cadenaConexion);
    		return resultado;
    	} catch ( IOException ioe) {
    		resultado = false;
    		log.debug("Error de lectura del fichero csv: "+cadenaConexion);
    		return resultado;
		} catch ( Exception ex) {
			resultado = false;
			log.debug("Error desconocido al leer el fichero csv: "+cadenaConexion);
			return resultado;
		}
    	return resultado;
    }
}