/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.modelo.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorMapSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorStringSW;
import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.base.utils.enumerados.TipoAtributoFD;
import es.dc.a21l.base.utils.enumerados.TiposDatoPostGis;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.DescripcionAtributoDto;
import es.dc.a21l.fuente.cu.TablaFuenteDatosDto;
import es.dc.a21l.fuente.cu.ValorFDDto;
import es.dc.a21l.fuente.modelo.Fuente;
import es.dc.a21l.fuente.modelo.OrigenPostGisRepositorio;

public class OrigenPostGisRepositorioImpl extends RepositorioBaseImpl<Fuente> implements OrigenPostGisRepositorio {
	private static final String CADENA_BD_ESPACIAL="jdbc:postgresql://";
	private static final Logger log = LoggerFactory.getLogger(OrigenPostGisRepositorioImpl.class);
	//private static final String queryTablas = "SELECT table_name FROM information_schema.tables WHERE table_type = 'BASE TABLE' AND table_schema NOT IN ('pg_catalog', 'information_schema');";
	private static final String queryTablas = "SELECT schemaname, relname FROM pg_stat_user_tables order by schemaname, relname";
	//private static final String queryColumnas = "SELECT column_name,data_type,column_default,is_nullable,character_maximum_length,numeric_precision FROM information_schema.columns WHERE table_name =?";
	private static final String queryColumnas = "SELECT column_name,data_type,column_default,is_nullable,character_maximum_length,numeric_precision FROM information_schema.columns WHERE table_schema =? AND table_name =?";
	//private static final String queryPrimaryKey = "SELECT pg_attribute.attname FROM pg_index, pg_class, pg_attribute WHERE indrelid = pg_class.oid AND pg_attribute.attrelid = pg_class.oid AND pg_attribute.attnum = any(pg_index.indkey) AND indisprimary AND pg_class.oid = ?::regclass";
	private static final String queryPrimaryKey = "SELECT column_name FROM information_schema.key_column_usage WHERE table_name=? AND table_schema=?";
	private static final String queryGeometryColumnConcondantes="Select srid from geometry_columns where srid <> ?";
	private static final String COLUMNA_GEOMETRIA_TEXTO = "geom_txt";
	private static final String COLUMNA_GEOMETRIA = "geom";
	private static final String DATO_GEOMETRIA = "the_geom";
	private static final String queryDatos = "SELECT * FROM ";
	private static final String queryDatosMapa = "SELECT st_astext("+COLUMNA_GEOMETRIA+") as "+COLUMNA_GEOMETRIA_TEXTO+"  FROM ";
	private static final String driverPostGis = "org.postgresql.Driver";
	
	public EncapsuladorListSW<TablaFuenteDatosDto> listarTablasFuenteExterna(Fuente fuente) {
		String cadenaConexion = fuente.getInfoConexion();
		if(!cadenaConexion.contains(CADENA_BD_ESPACIAL)){
			cadenaConexion=CADENA_BD_ESPACIAL.concat(cadenaConexion);
		}
		EncapsuladorListSW<TablaFuenteDatosDto> listaTablas = new EncapsuladorListSW<TablaFuenteDatosDto>();
		Connection con = null;
		try {
			Class.forName(driverPostGis);
			con = DriverManager.getConnection(cadenaConexion, fuente.getUsuario(),fuente.getPassword());
			
			Statement consulta = con.createStatement();
			ResultSet res = consulta.executeQuery(queryTablas);
			while (res.next()) {
				TablaFuenteDatosDto tabla = new TablaFuenteDatosDto();
				//tabla.setNombre(res.getString("table_name"));
				tabla.setEsquema(res.getString("schemaname"));
				//tabla.setNombre(res.getString("schemaname")+"___"+res.getString("relname"));
				tabla.setNombre(res.getString("relname"));
				listaTablas.add(tabla);
			}
			consulta.close();
			con.close();
			return listaTablas;
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e1) {
				log.debug("No se pudo cerrar la conexion o ya se había cerrado previamente");
			}
        	log.debug("conexion no establecida");
        	return null;
		}
	}

	public EncapsuladorListSW<AtributoFuenteDatosDto> listarColumnasTablaFuenteExterna(Fuente fuente, String esquema, String tabla) {
		String cadenaConexion = fuente.getInfoConexion();
		if(!cadenaConexion.contains(CADENA_BD_ESPACIAL)){
			cadenaConexion=CADENA_BD_ESPACIAL.concat(cadenaConexion);
		}
		EncapsuladorListSW<AtributoFuenteDatosDto> listaColumnas = new EncapsuladorListSW<AtributoFuenteDatosDto>();
		List<String> listaPks = new ArrayList<String>();
		Connection con = null;
		try {
			Class.forName(driverPostGis);
			con = DriverManager.getConnection(cadenaConexion, fuente.getUsuario(),fuente.getPassword());
			
			PreparedStatement preparedStatement = null;
		    String strQuery = queryPrimaryKey;
		    preparedStatement = con.prepareStatement(strQuery);
//		    String esquema = tabla.substring(0, tabla.indexOf("___")); 
//		    tabla = tabla.substring(esquema.length()+3, tabla.length());
		    preparedStatement.setString(1,tabla);
		    preparedStatement.setString(2,esquema);
		    ResultSet pks = preparedStatement.executeQuery();
		    while ( pks.next() ) {
		    	//listaPks.add(pks.getString("attname"));
		    	listaPks.add(pks.getString("column_name"));
		    }
			
		    strQuery = queryColumnas;
		    preparedStatement = con.prepareStatement(strQuery);
		    preparedStatement.setString(1,esquema);
		    preparedStatement.setString(2,tabla);
			ResultSet res = preparedStatement.executeQuery();
			while (res.next()) {
				String nombreCol = res.getString("COLUMN_NAME");
	        	AtributoFuenteDatosDto attr = new AtributoFuenteDatosDto();
	        	attr.setNombre(nombreCol);
	        	attr.setTipoAtributo(obtenerTipoColumna(res.getString("DATA_TYPE")));
	        	attr.setDefinicion(obtenerDefinicionColumna(nombreCol,res.getString("column_default"),res.getString("is_nullable"),listaPks));
	        	listaColumnas.add(attr);
			}
			preparedStatement.close();
			con.close();
			return listaColumnas;
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e1) {
				log.debug("No se pudo cerrar la conexion o ya se había cerrado previamente");
			}
        	log.debug("conexion no establecida");
        	return null;
		}
	}
	
	public boolean comprobarSridColumnasGeometricas(Fuente fuente,String srid) {
		String cadenaConexion = fuente.getInfoConexion();
		if(!cadenaConexion.contains(CADENA_BD_ESPACIAL)){
			cadenaConexion=CADENA_BD_ESPACIAL.concat(cadenaConexion);
		}
		Connection con = null;
		try {
			Class.forName(driverPostGis);
			con = DriverManager.getConnection(cadenaConexion, fuente.getUsuario(),fuente.getPassword());
			
			PreparedStatement preparedStatement = null;
		    String strQuery = queryGeometryColumnConcondantes;
		    preparedStatement = con.prepareStatement(strQuery);
		    preparedStatement.setInt(1,Integer.valueOf(srid));
		    ResultSet pks = preparedStatement.executeQuery();
		    if ( pks.next() ) {
		    	return false;
		    }
			return true;
		    
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e1) {
				log.debug("No se pudo cerrar la conexion o ya se había cerrado previamente");
			}
        	log.debug("conexion no establecida");
        	return false;
		}
	}

	public EncapsuladorListSW<DescripcionAtributoDto> obtenerEsquemaTablaFuenteExterna(Fuente fuente, String esquema, String tabla) {
//		 String esquema = tabla.substring(0, tabla.indexOf("___")); 
//		    tabla = tabla.substring(esquema.length()+3, tabla.length());
		List<AtributoFuenteDatosDto> listaColumnas = listarColumnasTablaFuenteExterna(fuente, esquema, tabla);
		EncapsuladorListSW<DescripcionAtributoDto> listaEsquema = new EncapsuladorListSW<DescripcionAtributoDto>();
		for (AtributoFuenteDatosDto attr : listaColumnas) {
			DescripcionAtributoDto desc = new DescripcionAtributoDto();
			desc.setNombre(attr.getNombre());
			desc.setTipo(attr.getTipoAtributo());
			desc.setDefinicion(attr.getDefinicion());
			listaEsquema.add(desc);
		}
		return listaEsquema;
	}

	public AtributosMapDto obtenerDatosTablaFuenteExterna(Fuente fuente, String esquema, String tabla, boolean completos) {
//		 String esquema = tabla.substring(0, tabla.indexOf("___")); 
//		    tabla = tabla.substring(esquema.length()+3, tabla.length());
		String cadenaConexion = fuente.getInfoConexion();
		if(!cadenaConexion.contains(CADENA_BD_ESPACIAL)){
			cadenaConexion=CADENA_BD_ESPACIAL.concat(cadenaConexion);
		}
		List<AtributoFuenteDatosDto> listaColumnas = listarColumnasTablaFuenteExterna(fuente, esquema, tabla);
		AtributosMapDto mapaValores = new AtributosMapDto();
		Connection con = null;
		
		if ( listaColumnas == null || listaColumnas.isEmpty()) return null;
		try {
			Class.forName(driverPostGis);
			con = DriverManager.getConnection(cadenaConexion, fuente.getUsuario(),fuente.getPassword());
			
			PreparedStatement preparedStatement = null;
		    String strQuery = queryDatos+"\""+esquema+"\"."+"\""+tabla+"\"";
		    preparedStatement = con.prepareStatement(strQuery);
		    ResultSet datos = preparedStatement.executeQuery();
			
	        while (datos.next()) {
	        	EncapsuladorMapSW<EncapsuladorStringSW,EncapsuladorStringSW> mapaFila = new EncapsuladorMapSW<EncapsuladorStringSW,EncapsuladorStringSW>();
	        	for (int i = 0; i < listaColumnas.size(); i++) {
	        		String nombreColumna = new String();
	        		TipoAtributoFD tipoColumna = listaColumnas.get(i).getTipoAtributo();
	        		nombreColumna = listaColumnas.get(i).getNombre();
	        		
	        		EncapsuladorStringSW valorColumna = new EncapsuladorStringSW();
	        		if ( !completos ) {
	                    if ( tipoColumna==TipoAtributoFD.VALORFDGEOGRAFICO ) {
	            		    //TODO probablemente haya q cambiar esto
	                    	valorColumna.setTexto("valor mapa");
	                    	//valorColumna.setTexto(datos.getString(COLUMNA_GEOMETRIA_TEXTO));
	                    } else {
	                    	valorColumna.setTexto(obtenerStringValor(datos,listaColumnas.get(i).getNombre()));
	                    }
	        		} else {
	        			if ( tipoColumna == TipoAtributoFD.VALORFDGEOGRAFICO )
	        				valorColumna.setTexto(datos.getString(COLUMNA_GEOMETRIA_TEXTO).trim());
	        			else
	        				valorColumna.setTexto(obtenerStringValor(datos,listaColumnas.get(i).getNombre()));
	        		}
                    
                    ValorFDDto valorFDTmp = mapaValores.getContenido().get(nombreColumna);
                    if ( valorFDTmp == null )
                    	valorFDTmp = new ValorFDDto();
                    valorFDTmp.getValores().add(valorColumna);
    	        	mapaValores.setValor(nombreColumna, valorFDTmp);
    	        	
    	        	mapaFila.put(new EncapsuladorStringSW(nombreColumna), valorColumna);
                }
            }
	        con.close();
		} catch (Exception e) {
			log.debug("Error desconocido: " + e.getMessage());
		}
		return mapaValores;
	}
	
	public AtributosMapDto obtenerMapaTablaFuenteExterna(Fuente fuente, String esquema, String tabla) {
//		 String esquema = tabla.substring(0, tabla.indexOf("___")); 
//		    tabla = tabla.substring(esquema.length()+3, tabla.length());
		String cadenaConexion = fuente.getInfoConexion();
		if(!cadenaConexion.contains(CADENA_BD_ESPACIAL)){
			cadenaConexion=CADENA_BD_ESPACIAL.concat(cadenaConexion);
		}
		List<AtributoFuenteDatosDto> listaColumnas = listarColumnasTablaFuenteExterna(fuente, esquema, tabla);
		AtributosMapDto mapaValores = new AtributosMapDto();
		Connection con = null;
		String columnaGeografica = "";
		for ( AtributoFuenteDatosDto attr : listaColumnas ) {
			if ( attr.getTipoAtributo() == TipoAtributoFD.VALORFDGEOGRAFICO )
				columnaGeografica = attr.getNombre();
		}	
		
		try {
			Class.forName(driverPostGis);
			con = DriverManager.getConnection(cadenaConexion, fuente.getUsuario(),fuente.getPassword());
			
			PreparedStatement preparedStatement = null;
			String strQuery = "SELECT st_astext("+columnaGeografica+") as "+COLUMNA_GEOMETRIA_TEXTO+"  FROM "+tabla;
		    //String strQuery = queryDatosMapa+tabla;
		    preparedStatement = con.prepareStatement(strQuery);
		    ResultSet datos = preparedStatement.executeQuery();
			
	        while (datos.next()) {
	        	EncapsuladorMapSW<EncapsuladorStringSW,EncapsuladorStringSW> mapaFila = new EncapsuladorMapSW<EncapsuladorStringSW,EncapsuladorStringSW>();
        		String nombreColumna = new String();
        		nombreColumna = COLUMNA_GEOMETRIA_TEXTO;
        		EncapsuladorStringSW valorColumna = new EncapsuladorStringSW();
                valorColumna.setTexto(datos.getString(COLUMNA_GEOMETRIA_TEXTO));
                
                ValorFDDto valorFDTmp = mapaValores.getContenido().get(nombreColumna);
                if ( valorFDTmp == null )
                	valorFDTmp = new ValorFDDto();
                valorFDTmp.getValores().add(valorColumna);
	        	mapaValores.setValor(nombreColumna, valorFDTmp);
	        	
	        	mapaFila.put(new EncapsuladorStringSW(nombreColumna), valorColumna);
            }
	        con.close();
		} catch (Exception e) {
			log.debug("Error desconocido: " + e.getMessage());
		}
		//Despues de obtener los valores de la Columna Geométrica se convierte el nombre de la columna en mapaValores a DATA_GEOMETRIA
		ValorFDDto datosColumnaMapa = mapaValores.getContenido().get(COLUMNA_GEOMETRIA_TEXTO);
		mapaValores.setValor(DATO_GEOMETRIA, datosColumnaMapa);
		//mapaValores.getContenido().remove(COLUMNA_GEOMETRIA_TEXTO);
		return mapaValores;
	}

	private TipoAtributoFD obtenerTipoColumna(String tipo) {
    	try {
	    	if ( tipo.equals(TiposDatoPostGis.INTEGER.getDescripcion()) || tipo.equals(TiposDatoPostGis.NUMERIC.getDescripcion()) || tipo.equals(TiposDatoPostGis.BIGINT.getDescripcion()) || tipo.equals(TiposDatoPostGis.DOUBLE.getDescripcion()) || tipo.equals(TiposDatoPostGis.DOUBLEPRECISION.getDescripcion()))
	    		return TipoAtributoFD.VALORFDNUMERICO;
	    	else if ( tipo.equals(TiposDatoPostGis.DATE.getDescripcion()))
	    		return TipoAtributoFD.VALORFDFECHA;
	    	else if ( tipo.equals(TiposDatoPostGis.VARCHAR.getDescripcion()))
	    		return TipoAtributoFD.VALORFDTEXTO;
	    	else if ( tipo.equals(TiposDatoPostGis.USER.getDescripcion()))
	    		return TipoAtributoFD.VALORFDGEOGRAFICO;
	    	else return TipoAtributoFD.VALORFDTEXTO;
    	} catch ( Exception ex) {
    		log.debug("Error al obtener el tipo de la columna");
    		return TipoAtributoFD.VALORFDTEXTO;
    	}
    }
	private String obtenerStringValor(ResultSet valor, String nombreColumna) {
		String respuesta;
    	try {
    		respuesta = valor.getString(nombreColumna).trim();
    	} catch (Exception ex ) {
    		try {
    			respuesta = String.valueOf(valor.getFloat(nombreColumna));
        	} catch (Exception ex2 ) {
        		try {
            		respuesta = (String)valor.getObject(nombreColumna);
            	} catch (Exception ex3 ) {
            		return null;
            	}
        	}
    	}
    	return respuesta;
	}
	
	private String obtenerDefinicionColumna(String nombreCol, String columnDefault, String isNullable, List<String> pks) {
		String respuesta = "";
		if ( isNullable.equals("NO") )
			respuesta +=" not null ";
		else
			respuesta += "null ";
			
		if ( columnDefault != null && !columnDefault.equals(""))
			respuesta +=" default: "+columnDefault;
		
		if (pks.contains(nombreCol)) {
			respuesta +=" primary key";
		}
		return respuesta;
	}
	
	public boolean probarFuente(Fuente fuente) {
		String cadenaConexion = fuente.getInfoConexion();
		if(!cadenaConexion.contains(CADENA_BD_ESPACIAL)){
			cadenaConexion=CADENA_BD_ESPACIAL.concat(cadenaConexion);
		}
		boolean resultado = false;
		Connection con = null;
		try {
			Class.forName(driverPostGis);
			con = DriverManager.getConnection(cadenaConexion, fuente.getUsuario(),fuente.getPassword());
			
			Statement consulta = con.createStatement();
			ResultSet res = consulta.executeQuery(queryTablas);
			consulta.close();
			con.close();
			resultado = true;
		} catch (Exception e) {
			resultado = false;
			try {
				con.close();
			} catch (Exception e1) {
				log.debug("No se pudo cerrar la conexion o ya se había cerrado previamente");
			}
        	log.debug("conexion no establecida");
        	return resultado;
		}
		return resultado;
	}
}