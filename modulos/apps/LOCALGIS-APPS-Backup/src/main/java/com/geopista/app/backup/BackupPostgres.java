/**
 * BackupPostgres.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.backup;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import com.geopista.app.backup.consola.BackupConsola;


public class BackupPostgres implements BackupAdapter {

    /**
     * Constantes que se corresponden con las claves de los datos que vamos a guardar en los objetos InformacionTabla
     */
	
	
    private static String KEY_NOMBRE_TABLA = "nombreTabla";
    private static String KEY_NAMESPACE = "namespace";
    private static String KEY_ID_INTERNO = "idInterno";
    private static String KEY_NUM_TRIGGERS = "numTriggers";
    private static String KEY_SECUENCIA_ASOCIADA = "nombreSecuencia";
    private static String KEY_COLUMNA_SECUENCIA = "columnaSecuencia";
    private static String KEY_COLUMNA_ID_MUNICIPIO = "columnaIdMunicipio";
    private ArrayList listaMunicipios = new ArrayList();
    private int idEntidad;
    private ArrayList tablasEnti = new ArrayList();

    public static int fiche=0;
    //private Vector cascadeKeys = new Vector();
    
    
    /*
     * (non-Javadoc)
     * @see com.geopista.backup.Backup#obtenerCadenaConexion()
     */
    public String obtenerCadenaConexion(String host, int puerto, String baseDatos) {
        return "jdbc:postgresql://" + host + ":" + puerto + "/" + baseDatos;
    }
    
    /*
     * (non-Javadoc)
     * @see com.geopista.backup.Backup#obtenerClaseDriver()
     */
    public String obtenerClaseDriver() {
        return "org.postgresql.Driver";
    }

    /*
     * (non-Javadoc)
     * @see com.geopista.backup.Backup#obtenerSentenciaDeshabilitarConstraints(com.geopista.backup.InformacionTabla)
     */
    
    
    public String obtenerSentenciaDeshabilitarConstraints(InformacionTabla tabla,Connection connection) throws SQLException {
    	
    	return "UPDATE pg_catalog.pg_class SET reltriggers = 0 WHERE oid = '" + tabla.getInformacion(KEY_NAMESPACE) + ".\"" + tabla.getInformacion(KEY_NOMBRE_TABLA) + "\"'::pg_catalog.regclass;";
    }
      
	public String obtenerSentenciaHabilitarConstraints(InformacionTabla tabla,
			Connection connection) throws SQLException {
		
		return "UPDATE pg_catalog.pg_class SET reltriggers = (SELECT pg_catalog.count(*) " +
			   "FROM pg_catalog.pg_trigger where pg_class.oid = tgrelid) WHERE oid = " +
					"'" + tabla.getInformacion(KEY_NAMESPACE) + ".\"" + tabla.getInformacion(KEY_NOMBRE_TABLA) + "\"'::pg_catalog.regclass;";
		
	}

	/*
     * (non-Javadoc)
     * @see com.geopista.backup.Backup#obtenerSentenciaActualizarSecuencia(com.geopista.backup.InformacionTabla)
     */
    public String obtenerSentenciaActualizarSecuencia(InformacionTabla tabla) {
        String secuenciaAsociada = (String)tabla.getInformacion(KEY_SECUENCIA_ASOCIADA);
        String columnaSecuencia = (String)tabla.getInformacion(KEY_COLUMNA_SECUENCIA);
        String nombreEspacio = (String)tabla.getInformacion(KEY_NAMESPACE); 
        if (secuenciaAsociada != null && columnaSecuencia != null && nombreEspacio!=null) {
            return "select setval('"+nombreEspacio+"." + secuenciaAsociada + "', max(" + columnaSecuencia + ")::bigint) from " + getNombreTabla(tabla) + ";";
        } else {
            return null;
        }
    }    
    
    /*
     * (non-Javadoc)
     * @see com.geopista.backup.Backup#obtenerTablasComunes()
     */
    public InformacionTabla[] obtenerTablasComunes(Connection conexion,boolean isVersion8) throws SQLException {
    	Properties prop = new Properties(); 
    	  try { 
    	          prop.load((getClass().getClassLoader().getResourceAsStream("com/geopista/app/backup/backup.properties"))); 
    	     } catch (IOException e1) { 
    	          // TODO Auto-generated catch block 
    	          e1.printStackTrace(); 
    	     }
    	    
    	ArrayList tablas = new ArrayList();
    	
    	String relTriggers="reltriggers";    	
    	if (!isVersion8)
    		relTriggers="relhastriggers";
    	
    	// sql: select aux.oid as id_interno,        aux.relname as tabla,        aux.nspname as namespace,        aux.reltriggers as num_triggers,        (select class2.relname         from pg_class class2         where ((lower(class2.relname) = lower('seq_' || aux.relname)) or (lower(class2.relname) = lower(aux.relname || '_sequence'))) LIMIT 1) as secuencia_asociada,        (select att2.attname         from pg_constraint constraints, pg_attribute att2         where constraints.conrelid = aux.oid and               att2.attrelid = aux.oid and               constraints.contype = 'p' and               constraints.conkey[1] = att2.attnum) as columna_secuencia from ((select class.oid, class.relname, namespace.nspname, class.reltriggers, class.relnamespace        from pg_class class, pg_namespace namespace        where class.relnamespace = namespace.oid and relkind = 'r' and namespace.nspname in ('visualizador', 'public', 'guiaurbana','localgisguiaurbana'))       except       (select class.oid, class.relname, namespace.nspname, class.reltriggers, class.relnamespace        from pg_attribute att, pg_class class, pg_namespace namespace, pg_type type        where class.relnamespace = namespace.oid and att.attrelid = class.oid and att.atttypid = type.oid and (type.typname = 'numeric' or type.typname = 'integer' ) and att.attname like '%municipio%' and class.relkind = 'r')) as aux

        String sql = 
            "select aux.oid as id_interno, " +
            "       aux.relname as tabla, " +
            "       aux.nspname as namespace, " +
            "       aux."+relTriggers+" as num_triggers, " +
            "       (select class2.relname " +
            "        from pg_class class2 " +
            "        where ((class2.relnamespace = aux.relnamespace) and ((lower(class2.relname) = lower('seq_' || aux.relname)) or (lower(class2.relname) = lower(aux.relname || '_sequence')))) LIMIT 1) as secuencia_asociada, " +
            "       (select att2.attname " +
            "        from pg_constraint constraints, pg_attribute att2 " +
            "        where constraints.conrelid = aux.oid and " +
            "              att2.attrelid = aux.oid and " +
            "              constraints.contype = 'p' and " +
            "              constraints.conkey[1] = att2.attnum) as columna_secuencia " +
            "from ((select class.oid, class.relname, namespace.nspname, class."+relTriggers+", class.relnamespace " +
            "       from pg_class class, pg_namespace namespace " +
            "       where class.relnamespace = namespace.oid and relkind = 'r' and namespace.nspname in ('visualizador', 'public', 'guiaurbana','cementerios','padron','localgisguiaurbana')) " +
            "      except " +
            "      (select class.oid, class.relname, namespace.nspname, class."+relTriggers+", class.relnamespace " +
            "       from pg_attribute att, pg_class class, pg_namespace namespace, pg_type type " +
            "       where class.relnamespace = namespace.oid and att.attrelid = class.oid and att.atttypid = type.oid and (type.typname = 'numeric' or type.typname = 'integer' ) and att.attname like '%municipio%' and class.relkind = 'r')) as aux";
        Statement statement = conexion.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            String nombre = resultSet.getString("tabla");
            String namespace = resultSet.getString("namespace");
            long idInterno = resultSet.getLong("id_interno");
            long numTriggers=0;
            if (!isVersion8){
                Boolean triggers= resultSet.getBoolean("num_triggers");
                //TODO Habría que verificar como saber cuantos trigges tiene en la version 9
                if (triggers)
                	numTriggers=1;
            }
            else
            	numTriggers = resultSet.getLong("num_triggers");
            String secuenciaAsociada = resultSet.getString("secuencia_asociada");
            String columnaSecuencia = resultSet.getString("columna_secuencia");
            InformacionTabla informacionTabla = new InformacionTabla();
            informacionTabla.putInformacion(KEY_NOMBRE_TABLA, nombre);
            informacionTabla.putInformacion(KEY_NAMESPACE, namespace);
            informacionTabla.putInformacion(KEY_ID_INTERNO, new Long(idInterno));
            informacionTabla.putInformacion(KEY_NUM_TRIGGERS, new Long(numTriggers));
            informacionTabla.putInformacion(KEY_SECUENCIA_ASOCIADA, secuenciaAsociada);
            informacionTabla.putInformacion(KEY_COLUMNA_SECUENCIA, columnaSecuencia);

            if(prop.containsKey("select."+namespace+".\""+nombre+"\"")){
            	tablasEnti.add(informacionTabla);
            }else{
            	tablas.add(informacionTabla);
            }
//            System.out.println(nombre+", "+namespace+", "+idInterno+", "+numTriggers+", "+secuenciaAsociada+", "+columnaSecuencia);
        }
        
        
        resultSet.close();
        statement.close();
        InformacionTabla[] tablasArray = new InformacionTabla[tablas.size()];
        tablas.toArray(tablasArray);
        return tablasArray;
    }

    /*
     * (non-Javadoc)
     * @see com.geopista.backup.Backup#obtenerTablasMunicipio(int)
     */
    public InformacionTabla[] obtenerTablasMunicipio(Connection conexion,boolean isVersion8) throws SQLException {
    	
    	
    	String relTriggers="reltriggers";    	
    	if (!isVersion8)
    		relTriggers="relhastriggers";
    	
        ArrayList tablas = new ArrayList();
        String sql = 
            "select distinct on (class.oid) " +
            "       class.oid as id_interno, " +
            "       class.relname as tabla, " + 
            "       att.attname as columna_id_municipio, " + 
            "       namespace.nspname as namespace, " + 
            "       class."+relTriggers+" as num_triggers, " + 
            "       (select class2.relname " +
            "        from pg_class class2 " +
            "        where ((class2.relnamespace = class.relnamespace) and ((lower(class2.relname) = lower('seq_' || class.relname)) or (lower(class2.relname) = lower(class.relname || 'sequence')))) LIMIT 1) as secuencia_asociada, " +
            "       (select att2.attname " +
            "        from pg_constraint constraints, pg_attribute att2 " +
            "        where constraints.conrelid = class.oid and " +
            "              att2.attrelid = class.oid and " +
            "              constraints.contype = 'p' and " +
            "              constraints.conkey[1] = att2.attnum) as columna_secuencia " +
            "from pg_attribute att, " + 
            "     pg_class class, " + 
            "     pg_namespace namespace, " + 
            "     pg_type type " +
            "where att.attrelid = class.oid and " + 
            "      class.relnamespace = namespace.oid and " + 
            "      att.atttypid = type.oid and " +
            "      (type.typname = 'numeric' or type.typname = 'integer') and " +
            "      attname like '%municipio%' and " + 
            "      class.relkind = 'r' and " + 
            "      namespace.nspname in ('visualizador', 'public', 'guiaurbana','localgisguiaurbana')";
        Statement statement = conexion.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            String nombre = resultSet.getString("tabla");
            String namespace = resultSet.getString("namespace");
            String columnaIdMunicipio = resultSet.getString("columna_id_municipio");
            long idInterno = resultSet.getLong("id_interno");
            //long numTriggers = resultSet.getLong("num_triggers");
            long numTriggers=0;
            if (!isVersion8){
                Boolean triggers= resultSet.getBoolean("num_triggers");
                //TODO Habría que verificar como saber cuantos trigges tiene en la version 9
                if (triggers)
                	numTriggers=1;
            }
            else
            	numTriggers = resultSet.getLong("num_triggers");
            
            String secuenciaAsociada = resultSet.getString("secuencia_asociada");
            String columnaSecuencia = resultSet.getString("columna_secuencia");
            InformacionTabla informacionTabla = new InformacionTabla();
            informacionTabla.putInformacion(KEY_NOMBRE_TABLA, nombre);
            informacionTabla.putInformacion(KEY_NAMESPACE, namespace);
            informacionTabla.putInformacion(KEY_ID_INTERNO, new Long(idInterno));
            informacionTabla.putInformacion(KEY_NUM_TRIGGERS, new Long(numTriggers));
            informacionTabla.putInformacion(KEY_SECUENCIA_ASOCIADA, secuenciaAsociada);
            informacionTabla.putInformacion(KEY_COLUMNA_SECUENCIA, columnaSecuencia);
            informacionTabla.putInformacion(KEY_COLUMNA_ID_MUNICIPIO, columnaIdMunicipio);
//            System.out.println(nombre+", "+namespace+", "+idInterno+", "+numTriggers+", "+secuenciaAsociada+", "+columnaSecuencia+", "+columnaIdMunicipio);
            tablas.add(informacionTabla);
        }
        for(int i=0;i<tablasEnti.size();i++){
        	tablas.add(tablasEnti.get(i));
        }
        resultSet.close();
        statement.close();
        InformacionTabla[] tablasArray = new InformacionTabla[tablas.size()];
        tablas.toArray(tablasArray);
        return tablasArray;
    }
    
    /*
     * (non-Javadoc)
     * @see com.geopista.backup.BackupAdapter#crearScriptInicial(java.sql.Connection, java.io.PrintStream, java.io.PrintStream)
     */
    public void crearScriptInicial(Connection conexion, FilterOutputStream salidaSql, PrintStream logError) throws SQLException {
    	logError.println("[crearScriptInicial] Postgress -> No hago nada");
    }
    
    /*
     * (non-Javadoc)
     * @see com.geopista.backup.BackupAdapter#crearScript(java.sql.Connection, java.io.PrintStream, java.io.PrintStream, com.geopista.backup.InformacionTabla, boolean, int)
     */
    public void crearScript(Connection conexion, FilterOutputStream salidaSql, PrintStream logError, InformacionTabla tabla, boolean tablaMunicipio, String municipios) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	StringBuffer sqlDelete = new StringBuffer();;
    	String sentenciaSql=null;
    	String deleteSql = null;	
    	String nombreTabla =  (getNombreTabla(tabla));
		//Almacenamos la ruta del properties
    	Properties prop = new Properties(); 
    	try { 
    		prop.load((getClass().getClassLoader().getResourceAsStream("com/geopista/app/backup/backup.properties"))); 
    	} 
    	catch (IOException e1) { 
			// TODO Auto-generated catch block 
			e1.printStackTrace(); 
	     }
    	
		if(prop.containsKey("select."+nombreTabla)){
			sentenciaSql = prop.getProperty("select."+nombreTabla);
			if (sentenciaSql.contains("%MUNICIPIOS_SUBSTRING%")){
				int i= 0;
				// Reemplazamos %MUNICIPIOS% por si existiese alguno:
				sentenciaSql = sentenciaSql.replace("%MUNICIPIOS%", municipios);
				
				String[] lista = sentenciaSql.split("%");
				StringBuffer substringMunicipios = new StringBuffer();
				while (i < lista.length){
					if (lista[i].equals("MUNICIPIOS_SUBSTRING")){
						Iterator it = listaMunicipios.iterator();
						substringMunicipios = new StringBuffer();
						while(it.hasNext()){
							Integer elemento = (Integer) it.next();
							if (it.hasNext())
								substringMunicipios.append("substring("+elemento+"::text, "+lista[i+1]+", "+lista[i+2]+"), ");
							else
								substringMunicipios.append("substring("+elemento+"::text, "+lista[i+1]+", "+lista[i+2]+")");
						}
						sql.append(substringMunicipios);
					i = i+3;	
					}
					else{
						sql.append(lista[i]);
						i++;
					}
				}
			}
			else{
				sql=new StringBuffer(sentenciaSql.replace("%MUNICIPIOS%", municipios));
			}
			
			deleteSql = prop.getProperty("delete."+nombreTabla);
			if (deleteSql.contains("%MUNICIPIOS_SUBSTRING%")){
				int i= 0;
				// Reemplazamos %MUNICIPIOS% por si existiese alguno:
				deleteSql = deleteSql.replace("%MUNICIPIOS%", municipios);
				
				String[] lista = deleteSql.split("%");
				StringBuffer substringMunicipios = new StringBuffer();
				while (i < lista.length){
					if (lista[i].equals("MUNICIPIOS_SUBSTRING")){
						Iterator it = listaMunicipios.iterator();
						substringMunicipios = new StringBuffer();
						while(it.hasNext()){
							Integer elemento = (Integer) it.next();
							if (it.hasNext())
								substringMunicipios.append("substring("+elemento+"::text, "+lista[i+1]+", "+lista[i+2]+"), ");
							else
								substringMunicipios.append("substring("+elemento+"::text, "+lista[i+1]+", "+lista[i+2]+")");
						}
						sqlDelete.append(substringMunicipios);
					i = i+3;	
					}
					else{
						sqlDelete.append(lista[i]);
						i++;
					}
				}
			}
			else{
				sqlDelete=new StringBuffer(deleteSql.replace("%MUNICIPIOS%", municipios));
			}

			System.out.println("Insert="+sql);
			System.out.println("Delete="+sqlDelete);

		}else{
			//Fin seleccion tabla con properties
			sql = new StringBuffer("select * from "+getNombreTabla(tabla));
			sqlDelete = new StringBuffer ("delete from "+getNombreTabla(tabla));
			if (tablaMunicipio){
	        	if (municipios != "") {
	        		String columnaIdMunicipio = (String)tabla.getInformacion(KEY_COLUMNA_ID_MUNICIPIO);
	        		sql.append(" where \""+columnaIdMunicipio+"\" IN ("+municipios+", 0) or \""+columnaIdMunicipio+"\" IS null");
	        		sqlDelete.append(" where \""+columnaIdMunicipio+"\" IN ( "+municipios+", 0) or \""+columnaIdMunicipio+"\" IS null");
	        	}
			}
			sqlDelete.append(";");
			//Fin Parentesis del else del property
      }
     
		BackupConsola.writeFile(salidaSql, sqlDelete.toString());
		sqlDelete = null; //Ayudar al GC.
		
        Statement statement = conexion.createStatement();
        ResultSet resultSet;
        try {
        	String szSql = sql.toString();
        	logError.println(BackupConsola.getLogHeader() + szSql);
            resultSet = statement.executeQuery(szSql);
            logError.println(BackupConsola.getLogHeader() + "query ejecutada!");
            sql = null; //Ayudar al GC.
        } catch (SQLException e) {
        	logError.println("Error al hacer la query: "+e.toString());
        	e.printStackTrace(logError);
            throw e;
        }
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        /*
         * Obtenemos el numero de columnas y un array con los tipos y los
         * nombres de las mismas para luego componer las sentencias insert
         */
        int numColumnas = resultSetMetaData.getColumnCount();
        String[] nombreColumnas = new String[numColumnas];
        int[] tipoColumnas = new int[numColumnas];
        for (int i = 0; i < numColumnas; i++) {
            nombreColumnas[i] = resultSetMetaData.getColumnName(i+1);
            tipoColumnas[i] = resultSetMetaData.getColumnType(i+1);
        }
        /*
         * Para cada fila devuelta de la tabla creamos una sentencia insert que
         * luego imprimimos en el fichero
         */
       
        while (resultSet.next() ) {
            StringBuffer insert = new StringBuffer("insert into "+getNombreTabla(tabla)+ " (");
            for (int i = 0; i < numColumnas; i++) {
                if (i != 0) {
                    insert.append(", ");
                }
                insert.append("\""+nombreColumnas[i]+"\"");
            }
            insert.append(") values (");
                 
            for (int i = 0; i < numColumnas; i++) {
                if (i != 0) {
                    insert.append(", ");
                }

                // Si el tipo de dato de la columna es un String, reemplazamos el caracter \ por \\, 
                // si no hacemos esto, al meter los datos en el fichero desapareceria el \:
                if ((tipoColumnas[i]==Types.CHAR)||(tipoColumnas[i]==Types.VARCHAR)){                
                    insert.append(valorColumna(logError, tabla, nombreColumnas[i], resultSet.getObject(i+1), tipoColumnas[i]).replace("\\", "\\\\"));
                }
                else{
                	insert.append(valorColumna(logError, tabla, nombreColumnas[i], resultSet.getObject(i+1), tipoColumnas[i]));
                }
            }
            insert.append(");"); 
            
    
			BackupConsola.writeFile(salidaSql ,insert.toString());

        }
        
        
        resultSet.close();
        statement.close();
    	}
    
		
    
    
    /**
     * Metodo para obtener la representacion como String de los valores de las columnas
     * @param logError Flujo para volcar los posibles errores
     * @param tabla Tabla que contiene la columna
     * @param nombreColumna Nombre de la columna
     * @param valor Valor de la columna 
     * @param tipo Tipo de la columna (ver java.sql.Types)
     * @return La representacion en String del valor de la columna segun su tipo
     */
    public void crearScriptUTF8(Connection conexion, FilterOutputStream salidaSql, PrintStream logError, InformacionTabla tabla, boolean tablaMunicipio, String municipios,FilterOutputStream salidaSql_UTF8) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	StringBuffer sqlDelete = new StringBuffer();
    	String sentenciaSql=null;
    	String deleteSql=null;
	
	    String nombreTabla =  (getNombreTabla(tabla));
  
    	    	
		System.out.println("Tabla= "+getNombreTabla(tabla));
		
    	 
    	Properties prop = new Properties(); 
  		try { 
  			prop.load((getClass().getClassLoader().getResourceAsStream("com/geopista/app/backup/backup.properties"))); 
     	} catch (IOException e1) { 
        	e1.printStackTrace(); 
     	}
  		
		if(prop.containsKey("select."+nombreTabla)){
			sentenciaSql = prop.getProperty("select."+nombreTabla);
			if (sentenciaSql.contains("%MUNICIPIOS_SUBSTRING%")){
				int i= 0;
				// Reemplazamos %MUNICIPIOS% por si existiese alguno:
				sentenciaSql = sentenciaSql.replace("%MUNICIPIOS%", municipios);
				
				String[] lista = sentenciaSql.split("%");
				StringBuffer substringMunicipios = new StringBuffer();
				while (i < lista.length){
					if (lista[i].equals("MUNICIPIOS_SUBSTRING")){
						Iterator it = listaMunicipios.iterator();
						substringMunicipios = new StringBuffer();
						while(it.hasNext()){
							Integer elemento = (Integer) it.next();
							if (it.hasNext())
								substringMunicipios.append("substring("+elemento+"::text, "+lista[i+1]+", "+lista[i+2]+"), ");
							else
								substringMunicipios.append("substring("+elemento+"::text, "+lista[i+1]+", "+lista[i+2]+")");
						}
						sql.append(substringMunicipios);
					i = i+3;	
					}
					else{
						sql.append(lista[i]);
						i++;
					}
				}
			}
			else{
				sql=new StringBuffer(sentenciaSql.replace("%MUNICIPIOS%", municipios));
			}
			
			deleteSql = prop.getProperty("delete."+nombreTabla);
			if (deleteSql.contains("%MUNICIPIOS_SUBSTRING%")){
				int i= 0;
				// Reemplazamos %MUNICIPIOS% por si existiese alguno:
				deleteSql = deleteSql.replace("%MUNICIPIOS%", municipios);
				
				String[] lista = deleteSql.split("%");
				StringBuffer substringMunicipios = new StringBuffer();
				while (i < lista.length){
					if (lista[i].equals("MUNICIPIOS_SUBSTRING")){
						Iterator it = listaMunicipios.iterator();
						substringMunicipios = new StringBuffer();
						while(it.hasNext()){
							Integer elemento = (Integer) it.next();
							if (it.hasNext())
								substringMunicipios.append("substring("+elemento+"::text, "+lista[i+1]+", "+lista[i+2]+"), ");
							else
								substringMunicipios.append("substring("+elemento+"::text, "+lista[i+1]+", "+lista[i+2]+")");
						}
						sqlDelete.append(substringMunicipios);
					i = i+3;	
					}
					else{
						sqlDelete.append(lista[i]);
						i++;
					}
				}
			}
			else{
				sqlDelete=new StringBuffer(deleteSql.replace("%MUNICIPIOS%", municipios));
			}

			System.out.println("Insert="+sql);
			System.out.println("Delete="+sqlDelete);

		}else{
			//Fin seleccion tabla con properties
			sql = new StringBuffer("select * from "+getNombreTabla(tabla));
			sqlDelete = new StringBuffer ("delete from "+getNombreTabla(tabla));
			if (tablaMunicipio){
	        	if (municipios != "") {
	        		String columnaIdMunicipio = (String)tabla.getInformacion(KEY_COLUMNA_ID_MUNICIPIO);
	        		sql.append(" where \""+columnaIdMunicipio+"\" IN ("+municipios+", 0) or \""+columnaIdMunicipio+"\" IS null");
	        		sqlDelete.append(" where \""+columnaIdMunicipio+"\" IN ( "+municipios+", 0) or \""+columnaIdMunicipio+"\" IS null");
	        	}
			}
			sqlDelete.append(";");
			//Fin Parentesis del else del property
      }
  		   	     
		BackupConsola.writeFile(salidaSql, sqlDelete.toString());
		BackupConsola.writeFile(salidaSql_UTF8, sqlDelete.toString());
		sqlDelete = null; //Ayudar al GC.

        Statement statement = conexion.createStatement();
        ResultSet resultSet;
        try {
        	String szSql = sql.toString();
        	logError.println(BackupConsola.getLogHeader() + szSql);
            resultSet = statement.executeQuery(szSql);
            logError.println(BackupConsola.getLogHeader() + "query ejecutada!");
            sql = null; //Ayudar al GC.
        } catch (SQLException e) {
        	logError.println("Error al hacer la query: "+e.toString());
        	e.printStackTrace(logError);
            throw e;
        }
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        /*
         * Obtenemos el numero de columnas y un array con los tipos y los
         * nombres de las mismas para luego componer las sentencias insert
         */
        int numColumnas = resultSetMetaData.getColumnCount();
        String[] nombreColumnas = new String[numColumnas];
        int[] tipoColumnas = new int[numColumnas];
        for (int i = 0; i < numColumnas; i++) {
            nombreColumnas[i] = resultSetMetaData.getColumnName(i+1);
            tipoColumnas[i] = resultSetMetaData.getColumnType(i+1);
        }
        /*
         * Para cada fila devuelta de la tabla creamos una sentencia insert que
         * luego imprimimos en el fichero
         */
       
        while (resultSet.next() ) {
//    	        	System.out.println(getNombreTabla(tabla)+", ");
            StringBuffer insert = new StringBuffer("insert into "+getNombreTabla(tabla)+ " (");
            for (int i = 0; i < numColumnas; i++) {
                if (i != 0) {
                    insert.append(", ");
                }
                insert.append("\""+nombreColumnas[i]+"\"");
            }
            insert.append(") values (");
                 
            for (int i = 0; i < numColumnas; i++) {
                if (i != 0) {
                    insert.append(", ");
                }

                // Si el tipo de dato de la columna es un String, reemplazamos el caracter \ por \\, 
                // si no hacemos esto, al meter los datos en el fichero desapareceria el \:
                if ((tipoColumnas[i]==Types.CHAR)||(tipoColumnas[i]==Types.VARCHAR)){                
                    insert.append(valorColumna(logError, tabla, nombreColumnas[i], resultSet.getObject(i+1), tipoColumnas[i]).replace("\\", "\\\\"));
                }
                else{
                	insert.append(valorColumna(logError, tabla, nombreColumnas[i], resultSet.getObject(i+1), tipoColumnas[i]));
                }
            }
            insert.append(");"); 
 
			BackupConsola.writeFile(salidaSql ,insert.toString());
			BackupConsola.writeFile(salidaSql_UTF8 ,insert.toString());
        }
    	        
    	        
        resultSet.close();
        statement.close();
		}
    
    
    	    
    private String valorColumna(PrintStream logError, InformacionTabla tabla, String nombreColumna, Object valor, int tipo) {
//    	System.out.println("Valor: "+valor+" tipo: "+tipo+ "Nombre columna: "+ nombreColumna+ "Nombre Tabla: "+getNombreTabla(tabla));
//		if (getNombreTabla(tabla).equals("public.\"maps\"")){
//			System.out.println("prueba");
//		}

        if (valor == null) {
            return "null";
        }
        /*
         * En otro caso el valor se traduce segun sea el tipo
         */
        else {
            switch (tipo) {
            case Types.BINARY:
                return valorBinary((byte[]) valor);
            case Types.BIT:
                return valor.toString();
            case Types.BOOLEAN:
                return valorBoolean((Boolean) valor);
            case Types.CHAR:
                return "'"+ valorString(valor.toString()) +"'";
            case Types.DATE:
                return "'"+ valor.toString() +"'";
            case Types.DOUBLE:
                return valor.toString();
            case Types.REAL:
            	return valor.toString();
            case Types.INTEGER:
                return valor.toString();
            case Types.NUMERIC:
                return valor.toString();           
            case Types.OTHER:
                return "'"+ valorString(valor.toString())+"'";
            case Types.SMALLINT:
                return valor.toString();
            case Types.TIMESTAMP:
                return valorTimestamp(valor);
            case Types.VARCHAR:
                return "'"+ valorString(valor) +"'";
            case Types.BIGINT:
            	return valor.toString();
            default:
                	String linea = "Tipo [" + tipo + "] no definido para la columna [" + nombreColumna + "] de la tabla [" + getNombreTabla(tabla) + "]";
                	logError.println(linea);

                return null;
            }
        }
    }
    
    /**
     * Metodo para obtener la representacion como String de un Boolean
     * @param valor Valor a representar
     * @return La representacion en String
     */
    private String valorBoolean(Boolean valor) {
        if (valor.booleanValue()) {
            return "'true'";
        } else {
            return "'false'";
        }
    }

    /**
     * Metodo que devuelve la representacion en String de un timestamp 
     * @param timestamp El timestamp que se desea saber su representacion como un Object
     * @return La representacion en String
     */
    private String valorTimestamp(Object timestamp) {
        return "'"+timestamp.toString()+"'";
    }
    
    /**
     * Metodo que devuelve la representacion en String de un binary 
     * @param binary El binary que se desea saber su representacion
     * @return La representacion en String
     */
    
    private String valorBinary(byte[] binary) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < binary.length; i++) {
            stringBuffer.append("\\\\");
            // para imprimir un cero o dos en caso de que se necesite
            if ((binary[i] & 0xF8) == 0x00) {
                stringBuffer.append("00");
            } else if ((binary[i] & 0xC0) == 0x00) {
                stringBuffer.append("0");
            }
            stringBuffer.append(Integer.toOctalString(binary[i] & 0xFF));
        }
        return "'" + stringBuffer.toString() + "'";
    }
    
    private String valorString(Object object) {
    	String string = (String) object;
    	if (string.indexOf("'")<0) {
    		return string;
    	}  	
    	
    	return string.replaceAll("'", "''");
    }
    
    private String getNombreTabla(InformacionTabla tabla) {
        return tabla.getInformacion(KEY_NAMESPACE)+".\""+tabla.getInformacion(KEY_NOMBRE_TABLA)+"\"";
    }

	public Hashtable obtenerEntidades(Connection connection) throws SQLException {
		Statement  statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT id_entidad, nombreoficial FROM entidad_supramunicipal order by nombreoficial");
		Hashtable hashtable = new Hashtable();
		while(resultSet.next()) {
			Integer id = new Integer(resultSet.getInt("id_entidad"));
			String nombre = resultSet.getString("nombreoficial");
			hashtable.put(nombre, id);
		}
		resultSet.close();
        statement.close();
		return hashtable;
	}
	public Hashtable<Integer, String> obtenerEntidadesYMunicipiosBackup(Connection connection) throws SQLException {
		Statement  statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT entidad_supramunicipal.id_entidad , id_municipio FROM entidad_supramunicipal, " +
				"entidades_municipios where entidad_supramunicipal.id_entidad=entidades_municipios.id_entidad and backup=1 order by nombreoficial");
		Hashtable<Integer,String> listaEntidades = new Hashtable<Integer,String>();
		while(resultSet.next()) {
			listaEntidades.put(new Integer(resultSet.getInt("id_entidad")),resultSet.getString("id_municipio"));
		}
		resultSet.close();
        statement.close();
        return listaEntidades;
	
	}

	public String obtenerStringMunicipiosEntidad(Connection connection, int idEntidad) throws SQLException {
		Statement  statement = connection.createStatement();
		String sql = "SELECT DISTINCT id_municipio FROM entidades_municipios ";
		if (idEntidad != -1)
			sql += " WHERE id_entidad = " + idEntidad;
		ResultSet resultSet = statement.executeQuery(sql);
		
		String municipios = "";
		this.idEntidad = idEntidad;
		listaMunicipios = new ArrayList();
		while(resultSet.next()) {
			Integer municipio = new Integer(resultSet.getInt("id_municipio"));
			listaMunicipios.add(municipio);
			municipios = municipios + municipio;
			if (!resultSet.isLast())
				municipios += ", ";
		}
		resultSet.close();
        statement.close();
		return municipios;
	}
	
	public String getVersion(Connection connection) throws SQLException {
		Statement  statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT version from version()");
		String version=null;
		while(resultSet.next()) {
			version= resultSet.getString("version");
		}
		resultSet.close();
        statement.close();
		return version;
	}
}
