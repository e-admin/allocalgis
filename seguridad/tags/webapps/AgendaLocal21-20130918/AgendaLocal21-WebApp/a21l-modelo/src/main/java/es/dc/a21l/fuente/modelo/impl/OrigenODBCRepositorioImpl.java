/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.modelo.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import es.dc.a21l.fuente.modelo.OrigenODBCRepositorio;

public class OrigenODBCRepositorioImpl extends RepositorioBaseImpl<Fuente> implements OrigenODBCRepositorio {
	private static final Logger log = LoggerFactory.getLogger(OrigenODBCRepositorioImpl.class);
	private static final String DRIVER_ODBC = "sun.jdbc.odbc.JdbcOdbcDriver"; 
	private static final String CADENA_BD_ODBC="jdbc:odbc:";
	
	public EncapsuladorListSW<TablaFuenteDatosDto> listarTablasFuenteExterna(Fuente fuente) {
		String cadenaConexion = fuente.getInfoConexion();
		if(!cadenaConexion.contains(CADENA_BD_ODBC)){
			cadenaConexion=CADENA_BD_ODBC.concat(cadenaConexion);
		}
    	String usuario = fuente.getUsuario();
    	String password = fuente.getPassword();
    	String[] tiposTabla = {"TABLE"};
    	EncapsuladorListSW<TablaFuenteDatosDto> listaTablas = new EncapsuladorListSW<TablaFuenteDatosDto>();
    	
    	Connection con = null;
    	try {
    		try {
	           //cargar el Driver
	           Class.forName(DRIVER_ODBC);
    	    } catch(Exception e) {
    	    	log.debug("No se ha cargado  Driver JDBC-ODBC");
    	    }

	        //establecer la conexion
	        con = DriverManager.getConnection(cadenaConexion,usuario, password);

	        //lista de tablas
	        DatabaseMetaData meta = con.getMetaData();
	        ResultSet rs = meta.getTables(null, null, null, tiposTabla);
	        while (rs.next()) {
	            String nombreTablaActual = rs.getString("TABLE_NAME");
	            TablaFuenteDatosDto tabla = new TablaFuenteDatosDto();
	            tabla.setNombre(nombreTablaActual);
	            listaTablas.add(tabla);
	        }
	        con.close();
	        //TODO quitar esta llamada
	        //obtenerDatosTablaFuenteExterna(fuente, "Tb_Sistema_Usuarios");
        } catch(Exception e) {
        	try {
				con.close();
			} catch (SQLException e1) {
				log.debug("No se pudo cerrar la conexion o ya se había cerrado previamente");
			} catch (NullPointerException ex) {
				log.debug("No se pudo cerrar la conexion porque esta es nula");
			}
        	log.debug("conexion no establecida");
        	return null;
        } 
    	return listaTablas;
    }
	
    public EncapsuladorListSW<AtributoFuenteDatosDto> listarColumnasTablaFuenteExterna(Fuente fuente, String tabla) {
    	String cadenaConexion = fuente.getInfoConexion();
		if(!cadenaConexion.contains(CADENA_BD_ODBC)){
			cadenaConexion=CADENA_BD_ODBC.concat(cadenaConexion);
		}
    	String usuario = fuente.getUsuario();
    	String password = fuente.getPassword();
    	EncapsuladorListSW<AtributoFuenteDatosDto> listaColumnas = new EncapsuladorListSW<AtributoFuenteDatosDto>();
    	Connection con = null;
    	try {
    		try {
	           //cargar el Driver
	           Class.forName(DRIVER_ODBC);
    	    } catch(Exception e) {
    	    	log.debug("No se ha cargado  Driver JDBC-ODBC");
    	    }

	        //establecer la conexion
    		con = DriverManager.getConnection(cadenaConexion,usuario, password);
	        DatabaseMetaData meta = con.getMetaData();
	        ResultSet rs = meta.getColumns(null, null, tabla, "%");
	        while (rs.next()) {
	        	String nombreCol = rs.getString("COLUMN_NAME");
	        	AtributoFuenteDatosDto attr = new AtributoFuenteDatosDto();
	        	attr.setNombre(nombreCol);
	        	attr.setTipoAtributo(obtenerTipoColumna(rs.getInt("DATA_TYPE")));
	        	listaColumnas.add(attr);
	        }
	        con.close();
        }catch(Exception e){
        	try {
				con.close();
			} catch (SQLException e1) {
				log.debug("No se pudo cerrar la conexion");
			}
        	log.debug("conexion no establecida");
        	return null;
        } 
    	return listaColumnas;
    }
    public EncapsuladorListSW<DescripcionAtributoDto> obtenerEsquemaTablaFuenteExterna(Fuente fuente, String tabla) {
    	List<AtributoFuenteDatosDto> listaColumnas = listarColumnasTablaFuenteExterna(fuente, tabla);
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
    public AtributosMapDto obtenerDatosTablaFuenteExterna(Fuente fuente, String tabla, boolean completos) {
    	String cadenaConexion = fuente.getInfoConexion();
		if(!cadenaConexion.contains(CADENA_BD_ODBC)){
			cadenaConexion=CADENA_BD_ODBC.concat(cadenaConexion);
		}
    	String usuario = fuente.getUsuario();
    	String password = fuente.getPassword();
    	List<AtributoFuenteDatosDto> listaColumnas = listarColumnasTablaFuenteExterna(fuente, tabla);
    	Connection con = null;
    	if ( listaColumnas == null ) { return null; }
    	
    	AtributosMapDto mapaValores = new AtributosMapDto();
    	try {
    		try {
	           //cargar el Driver
	           Class.forName(DRIVER_ODBC);
    	    } catch(Exception e) {
    	    	log.debug("No se ha cargado  Driver JDBC-ODBC");
    	    	return null;
    	    }

	        //establecer la conexion
	        //con = DriverManager.getConnection("jdbc:odbc:usc","telfi", "telfi00");
    		con = DriverManager.getConnection(cadenaConexion,usuario, password);
	        Statement consulta = con.createStatement();
	        ResultSet rs;
	        try  {
	        	rs = consulta.executeQuery("select * from ["+tabla+"]");
	        } catch (Exception ex) {
	        	try {
	        	rs = consulta.executeQuery("select * from ["+tabla+"$]");
	        	}
	        	catch (Exception ex2)
	        	{
		        	rs = consulta.executeQuery("select * from "+tabla);
	        	}
	        }
	        
	        //EncapsuladorListSW<EncapsuladorMapSW<EncapsuladorStringSW,EncapsuladorStringSW>> listaFilas = new EncapsuladorListSW<EncapsuladorMapSW<EncapsuladorStringSW,EncapsuladorStringSW>>();
	        while (rs.next()) {
	        	for (int i = 0; i < listaColumnas.size(); i++) {
	        		String nombreColumna = new String();
	        		nombreColumna = listaColumnas.get(i).getNombre();
	        		EncapsuladorStringSW valorColumna = new EncapsuladorStringSW();
	        		valorColumna.setTexto(obtenerStringValor(rs,nombreColumna));
                    ValorFDDto valorFDTmp = mapaValores.getContenido().get(nombreColumna);
                    if ( valorFDTmp == null )
                    	valorFDTmp = new ValorFDDto();
                    valorFDTmp.getValores().add(valorColumna);
    	        	mapaValores.setValor(nombreColumna, valorFDTmp);
                }	
            }
	        con.close();
        }catch(Exception e){
        	try {
				con.close();
			} catch (SQLException e1) {
				log.debug("No se pudo cerrar la conexion");
			}
        	log.debug("conexion no establecida");
        	return null;
        }
    	return mapaValores;
    }
    
    private String obtenerStringValor(ResultSet rs, String nombreColumna) {
    	String respuesta;
    	try {
    		respuesta = rs.getString(nombreColumna).trim();
    	} catch (Exception ex ) {
    		try {
    			respuesta = String.valueOf(rs.getFloat(nombreColumna));
        	} catch (Exception ex2 ) {
        		try {
            		respuesta = (String)rs.getObject(nombreColumna);
            	} catch (Exception ex3 ) {
            		return null;
            	}
        	}
    	}
    	return respuesta;
    }
    
    private TipoAtributoFD obtenerTipoColumna(int tipo) {
    	try {
	    	if ( tipo == Types.CHAR || tipo == Types.LONGVARCHAR || tipo == Types.NVARCHAR || tipo == Types.NCHAR || tipo == Types.VARCHAR)
	    		return TipoAtributoFD.VALORFDTEXTO;
	    	else if ( tipo == Types.DATE || tipo == Types.TIME || tipo == Types.TIMESTAMP)
	    		return TipoAtributoFD.VALORFDFECHA;
	    	else if ( tipo == Types.DECIMAL || tipo == Types.DOUBLE || tipo == Types.FLOAT || tipo == Types.INTEGER || tipo == Types.NUMERIC || tipo == Types.REAL || tipo == Types.SMALLINT || tipo == Types.TINYINT || tipo == Types.BIGINT)
	    		return TipoAtributoFD.VALORFDNUMERICO;
	    	else return null;
    	} catch ( Exception ex) {
    		log.debug("Error al obtener el tipo de la columna");
    		return TipoAtributoFD.VALORFDTEXTO;
    	}
    }
    
    public boolean probarFuente(Fuente fuente) {
    	String cadenaConexion = fuente.getInfoConexion();
		if(!cadenaConexion.contains(CADENA_BD_ODBC)){
			cadenaConexion=CADENA_BD_ODBC.concat(cadenaConexion);
		}
    	String usuario = fuente.getUsuario();
    	String password = fuente.getPassword();
    	
    	boolean resultado = false;
    	Connection con = null;
    	try {
    		try {
	           //cargar el Driver
	           Class.forName(DRIVER_ODBC);
    	    } catch(Exception e) {
    	    	log.debug("No se ha cargado  Driver JDBC-ODBC");
    	    }

	        //establecer la conexion
	        con = DriverManager.getConnection(cadenaConexion,usuario, password);

	        con.close();
	        resultado = true;
        } catch(Exception e) {
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
