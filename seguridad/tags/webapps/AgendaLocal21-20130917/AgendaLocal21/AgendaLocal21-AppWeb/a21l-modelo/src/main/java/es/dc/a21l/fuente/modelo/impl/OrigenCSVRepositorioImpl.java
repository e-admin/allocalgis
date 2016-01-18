/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.modelo.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorMapSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorStringSW;
import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.base.modelo.utils.UtilFecha;
import es.dc.a21l.base.utils.enumerados.TipoAtributoFD;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.DescripcionAtributoDto;
import es.dc.a21l.fuente.cu.TablaFuenteDatosDto;
import es.dc.a21l.fuente.cu.ValorFDDto;
import es.dc.a21l.fuente.modelo.Fuente;
import es.dc.a21l.fuente.modelo.OrigenCSVRepositorio;

public class OrigenCSVRepositorioImpl extends RepositorioBaseImpl<Fuente> implements OrigenCSVRepositorio {
	private static final Logger log = LoggerFactory.getLogger(OrigenCSVRepositorioImpl.class);
	private FileReader fichero;
	
	public FileReader getFichero() {
		return fichero;
	}
	public void setFichero(FileReader fichero) {
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
	
    public EncapsuladorListSW<AtributoFuenteDatosDto> listarColumnasTablaFuenteExterna(Fuente fuente, String tabla, String path, String caracterSeparador) {
    	String cadenaConexion = path+fuente.getId()+"/"+fuente.getFich_csv_gml();
    	
    	EncapsuladorListSW<AtributoFuenteDatosDto> listaColumnas = new EncapsuladorListSW<AtributoFuenteDatosDto>();
    	CSVReader reader = null;
    	try  {
    		this.setFichero(new FileReader(cadenaConexion));
    		Character separador =null;
	    	String codificacion="UTF-8";
	    	if(fuente.getCaracterSeparador()==null ||(fuente.getCaracterSeparador()!=null && fuente.getCaracterSeparador().getCaracter()==null)){
	    		separador=caracterSeparador.charAt(0);
	    	}else{
	    		separador=fuente.getCaracterSeparador().getCaracter().charAt(0);
	    	}
	    	if(fuente.getTipoCodificacion()!=null && fuente.getTipoCodificacion().getNombre()!=null){
	    		codificacion=fuente.getTipoCodificacion().getNombre();
	    	}
    		reader=new CSVReader( new InputStreamReader(new FileInputStream(cadenaConexion), codificacion), separador);
	        String [] linea;
	        linea = reader.readNext();
	        Integer columna = 0;
	        for ( String cabecera : linea) {
	        	AtributoFuenteDatosDto attr = new AtributoFuenteDatosDto();
	        	attr.setNombre(cabecera);
	        	//Obtengo la siguiente linea para poder saber de que tipo son los valores.
	        	//Por supuesto se asume que todos los valores de una columna son del mismo tipo
	        	try {
	        		String[] siguienteLinea = reader.readNext();
	        		attr.setTipoAtributo(obtenerTipoColumna(siguienteLinea[columna]));
	        	} catch (Exception ex) {
	        		attr.setTipoAtributo(TipoAtributoFD.VALORFDTEXTO);
	        	}
	        	listaColumnas.add(attr);
	        	columna++;
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
				if ( reader!=null)
					reader.close();
			} catch (IOException e) {
				log.debug("no se pudo cerrar el fichero");
			}
		}
    	return listaColumnas;
    }
    public EncapsuladorListSW<DescripcionAtributoDto> obtenerEsquemaTablaFuenteExterna(Fuente fuente, String tabla, String path, String caracterSeparador) {
    	List<AtributoFuenteDatosDto> listaColumnas = listarColumnasTablaFuenteExterna(fuente, tabla, path, caracterSeparador);
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
    public AtributosMapDto obtenerDatosTablaFuenteExterna(Fuente fuente, String tabla, String path, String caracterSeparador, boolean completos) {
    	String cadenaConexion = path+fuente.getId()+"/"+fuente.getFich_csv_gml();
    	List<AtributoFuenteDatosDto> listaColumnas = listarColumnasTablaFuenteExterna(fuente, tabla, path, caracterSeparador);
    	AtributosMapDto mapaValores = new AtributosMapDto();
    	CSVReader reader = null;
    	try {
	    	if ( listaColumnas == null ) return mapaValores;
	    	
	    	this.setFichero(new FileReader(cadenaConexion));
	    	reader = new CSVReader(getFichero(),caracterSeparador.charAt(0));
	    	Character separador =null;
	    	String codificacion="UTF-8";
	    	if(fuente.getCaracterSeparador()==null ||(fuente.getCaracterSeparador()!=null && fuente.getCaracterSeparador().getCaracter()==null)){
	    		separador=caracterSeparador.charAt(0);
	    	}else{
	    		separador=fuente.getCaracterSeparador().getCaracter().charAt(0);
	    	}
	    	if(fuente.getTipoCodificacion()!=null && fuente.getTipoCodificacion().getNombre()!=null){
	    		codificacion=fuente.getTipoCodificacion().getNombre();
	    	}
    		reader=new CSVReader( new InputStreamReader(new FileInputStream(cadenaConexion), codificacion), separador);
	        String [] linea;
	        boolean primero = true;
	        
	        while ((linea = reader.readNext()) != null) {
	        	//La cabecera se salta
	        	if (primero) { primero=false;continue; }
	        	
	        	EncapsuladorMapSW<EncapsuladorStringSW,EncapsuladorStringSW> mapaFila = new EncapsuladorMapSW<EncapsuladorStringSW,EncapsuladorStringSW>();
	        	for (int i = 0; i < listaColumnas.size(); i++) {
	        		String nombreColumna = listaColumnas.get(i).getNombre();
	        		EncapsuladorStringSW valorColumna = new EncapsuladorStringSW();
	        		valorColumna.setTexto(linea[i].trim());
                    ValorFDDto valorFDTmp = mapaValores.getContenido().get(nombreColumna);
                    if ( valorFDTmp == null )
                    	valorFDTmp = new ValorFDDto();
                    valorFDTmp.getValores().add(valorColumna);
    	        	mapaValores.setValor(nombreColumna, valorFDTmp);
    	        	mapaFila.put(new EncapsuladorStringSW(nombreColumna), valorColumna);
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
		}  finally {
			try {
				if ( reader!=null)
					reader.close();
			} catch (IOException e) {
				log.debug("no se pudo cerrar el fichero");
			}
		}
    	return mapaValores;
    }
    
    
    private TipoAtributoFD obtenerTipoColumna(String valor) {
    	try {
    		int i = Integer.valueOf(valor.trim()).intValue();
    		return TipoAtributoFD.VALORFDNUMERICO;
    	} catch (Exception ex ) {
    		try {
    			float f = Float.valueOf(valor.trim()).floatValue();
    			return TipoAtributoFD.VALORFDNUMERICO;
    		} catch ( Exception ex1 ) {
    			if ( esFecha(valor) ) {
    				return TipoAtributoFD.VALORFDFECHA;
    			} else {
    				return TipoAtributoFD.VALORFDTEXTO;
    			}
    		}
    	}
    }
    
    private boolean esFecha(String cadena) {
    	if (UtilFecha.multiParse(cadena)==null)
    		return false;
    	else
    		return true;
    }
    
    public boolean probarFuente(Fuente fuente, String path, String caracterSeparador) {
    	String cadenaConexion = path+fuente.getId()+"/"+fuente.getFich_csv_gml();
    	boolean resultado = false; 
    	CSVReader reader = null;
    	try  {
    		this.setFichero(new FileReader(cadenaConexion));
    		Character separador =null;
	    	String codificacion="UTF-8";
	    	if(fuente.getCaracterSeparador()==null ||(fuente.getCaracterSeparador()!=null && fuente.getCaracterSeparador().getCaracter()==null)){
	    		separador=caracterSeparador.charAt(0);
	    	}else{
	    		separador=fuente.getCaracterSeparador().getCaracter().charAt(0);
	    	}
	    	if(fuente.getTipoCodificacion()!=null && fuente.getTipoCodificacion().getNombre()!=null){
	    		codificacion=fuente.getTipoCodificacion().getNombre();
	    	}
    		reader=new CSVReader( new InputStreamReader(new FileInputStream(cadenaConexion), codificacion), separador);
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
		} finally {
			try {
				if ( reader!=null)
					reader.close();
			} catch (IOException e) {
				log.debug("no se pudo cerrar el fichero");
			}
		}
    	return resultado;
    }
}