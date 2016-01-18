/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 *
 */


package com.geopista.app.layerutil.dbtable;


/**
 * Definicion de metodos para la realizacion de operaciones sobre base de 
 * datos
 * 
 * @author cotesa
 *
 */


import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.geopista.app.AppContext;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.app.layerutil.schema.column.ColumnRow;
import com.geopista.app.layerutil.schema.table.TableRow;
import com.geopista.feature.Attribute;
import com.geopista.feature.Column;
import com.geopista.feature.Table;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.LCGIII_GeopistaUtil;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.GUIUtil;


public class TablesDBOperations{
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TablesDBOperations.class);
	
    /**
     * Conexion a base de datos
     */
    public Connection conn = null;
    /**
     * Conexion a base de datos sin pasar por el administrador de cartografia
     */
    public static Connection directConn = null;
    
    /**
     * Contexto de la aplicacion
     */
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
   
    public static Set familiasModificadas = new HashSet(); 
    //private static final String pass = aplicacion.getUserPreference("conexion.pass","",false);
    
    private static final int NO_RESULTS=-1;
    
    /**
     * Constructor por defecto
     *
     */
    public TablesDBOperations(){        
        try{
            conn = getDBConnection();
        }
        catch(Exception e){ 
            e.printStackTrace();
        }        
    }
    
    public static void directConnect(){
        try{
            directConn = getDirectConnection();
        }
        catch(Exception e)
        { 
            e.printStackTrace();
        }     
    }
    
    /**
     * Obtiene una conexion a la base de datos
     * @return Conexion a la base de datos
     * @throws SQLException
     */
    private static Connection getDBConnection () throws SQLException{        
        Connection con =  aplicacion.getConnection();
        con.setAutoCommit(false);
        return con;
    }  
    
    
    /**
     * Obtiene una conexion directa a la base de datos, sin utilizar el administrador de 
     * cartografia
     * @return Conexion a la base de datos
     * @throws SQLException 
     */
    
    private static Connection getDirectConnection () throws SQLException{   
        if (directConn==null && aplicacion.getBlackboard().get("DirectConnection")==null){    
            LoginDBDialog dial = new LoginDBDialog(aplicacion.getMainFrame());
            if (aplicacion.getMainFrame()==null)
                GUIUtil.centreOnScreen(dial);
            else
                GUIUtil.centreOnWindow(dial);
            
            dial.show();
            
            directConn = aplicacion.getJDBCConnection(dial.getLogin(), dial.getPassword());
            aplicacion.getBlackboard().put("USER_BD", dial.getLogin());
            aplicacion.getBlackboard().put("PASS_BD", dial.getPassword());
            aplicacion.getBlackboard().put("DirectConnection", directConn);        
            
            directConn.setAutoCommit(true);
        } 
        else if (aplicacion.getBlackboard().get("DirectConnection")!=null){
            directConn = (Connection)aplicacion.getBlackboard().get("DirectConnection");  
            if (directConn.isClosed())
                directConn = aplicacion.getJDBCConnection(aplicacion.getBlackboard().get("USER_BD").toString(),
                        aplicacion.getBlackboard().get("PASS_BD").toString());
        }            
        else if (directConn.isClosed() && aplicacion.getBlackboard().get("USER_BD")!=null
                && aplicacion.getBlackboard().get("PASS_BD") !=null){
            directConn = aplicacion.getJDBCConnection(aplicacion.getBlackboard().get("USER_BD").toString(),
                    aplicacion.getBlackboard().get("PASS_BD").toString());
        }
        return directConn;
    }  
    
    public boolean existeTabla(String nombreTabla){
        PreparedStatement s = null;
        ResultSet r = null;
        
    	try {   

            // SELECT * FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE' AND table_name = ?
    		
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();
    		
            s = conn.prepareStatement("LMexisteTabla");
            s.setString(1, nombreTabla);                 
            
            r = s.executeQuery();  
            if (r.next()){
            	return true;
            }

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			cerrarConexiones(r, null, s, conn, null);
		}
        return false;
    }
    
    /**
     * Recupera la lista de todas las tablas no externas dadas de alta en la Base de Datos 
     * con esquema public
     * 
     * @throws DataException Si se produce un error de acceso a datos
     */
    public List obtenerListaTablasBDNoExternas() throws DataException{   
        List lstTablas = new ArrayList();
        PreparedStatement s = null;
        ResultSet r = null;
        
        try{
            //String query = "select pgt.tablename, t.* from pg_tables pgt, tables t where pgt.schemaname='public' and pgt.tablename=t.name;";

    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();

            s = conn.prepareStatement("LMobtenertablasBDNoExternas");
            
            r = s.executeQuery();  
            while (r.next()){     
                Table tab = new Table();
                tab.setName(r.getString("name"));
                tab.setIdTabla(r.getInt("id_table"));
                if (r.getString("geometrytype")!=null)
                    tab.setGeometryType(r.getInt("geometrytype"));
                else
                    tab.setGeometryType(-1);
                
                //En la descripcion se introduce el valor del tablename (nombre de la tabla en BD)
                tab.setDescription(r.getString("tablename"));
                lstTablas.add(tab);                
            }
            
        }        
        catch (SQLException ex){
            throw new DataException(ex);
        }
        finally{
        	cerrarConexiones(r, null, s, conn, null);
        }
        return lstTablas;
    }
    
    /**
     * Recupera la lista de todas las tablas dadas de alta en la Base de Datos 
     * con esquema public
     * 
     * @throws DataException Si se produce un error de acceso a datos
     */
    public List obtenerListaTablasBD() throws DataException{   
        List lstTablas = new ArrayList();
        PreparedStatement s = null;
        ResultSet r = null;
        try{
            //String query = "select pgt.tablename, t.* from pg_tables pgt, tables t where pgt.schemaname='public' and pgt.tablename=t.name;";

    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();
    		
            s = conn.prepareStatement("LMobtenertablasBD");
            
            r = s.executeQuery();  
            while (r.next()){     
                Table tab = new Table();
                tab.setName(r.getString("name"));
                tab.setIdTabla(r.getInt("id_table"));
                if (r.getString("geometrytype")!=null)
                    tab.setGeometryType(r.getInt("geometrytype"));
                else
                    tab.setGeometryType(-1);
                
                //En la descripcion se introduce el valor del tablename (nombre de la tabla en BD)
                tab.setDescription(r.getString("tablename"));
                lstTablas.add(tab);                
            }
            
        }        
        catch (SQLException ex){
            throw new DataException(ex);
        }
        finally{
        	cerrarConexiones(r, null, s, conn, null);
        }        
        return lstTablas;
    }
    
    /**
     * Recupera una tabla de la Base de Datos por nombre
     * con esquema public
     * 
     * @throws DataException Si se produce un error de acceso a datos
     */
    public Table obtenerTablaPorNombre(String name) throws DataException{   
    	Table table = null;
        PreparedStatement s = null;
        ResultSet r = null;
        try{
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();
    		
            s = conn.prepareStatement("LMobtenertablaPorNombre");
            s.setString(1, name);
            
            r = s.executeQuery();  
            while (r.next()){     
            	table = new Table();
                table.setName(r.getString("name"));
                table.setIdTabla(r.getInt("id_table"));
                if (r.getString("geometrytype")!=null)
                	table.setGeometryType(r.getInt("geometrytype"));
                else
                	table.setGeometryType(-1);
                
                //En la descripcion se introduce el valor del tablename (nombre de la tabla en BD)
                table.setDescription(r.getString("tablename"));
                          
            }
        }        
        catch (SQLException ex){
            throw new DataException(ex);
        }
        finally{
        	cerrarConexiones(r, null, s, conn, null);
        }        
        return table;
    }
    
    /**
     * Recupera la lista de columnas de Base de datos que contiene la tabla
     * @param tableName Nombre de la tabla
     * 
     * @return Hashtable con las columnas de la tabla como valor y la posicion 
     * en la tabla como clave
     *      
     * @throws DataException Si se produce un error de acceso a datos
     */
    
    public HashMap obtenerListaColumnasBD(String tableName) throws DataException{
        
        HashMap htColumns = new HashMap();
        PreparedStatement s = null;
        ResultSet r = null;
        
        try{
            //String query = "SELECT relname, attname, atttypmod, attnotnull, typname, adsrc, description, attnum
            //                  FROM ((((pg_class LEFT JOIN pg_attribute
            //                  ON ((pg_attribute.attrelid=pg_class.oid))) LEFT JOIN pg_type
            //                  ON ((atttypid = pg_type.oid))) LEFT JOIN pg_attrdef
            //                  ON (((attrelid = pg_attrdef.adrelid) AND (attnum = pg_attrdef.adnum))))
            //                  LEFT JOIN pg_description ON (((attrelid = pg_description.objoid)
            //                      AND (attnum = pg_description.objsubid))))
            //                      WHERE attnum>=0 AND typname is not null and relname=?
            //                      ORDER BY attnum;";
            
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();
    		
            s = conn.prepareStatement("LMobtenercolumnasBD");
            s.setString(1, tableName);
            r = s.executeQuery();  
            
            while (r.next()){     
                ColumnDB col = new ColumnDB();
                col.setName(r.getString("attname"));
                col.setType(r.getString("typname"));
                col.setNotNull(r.getBoolean("attnotnull"));                
                col.setLength(getFieldLength (r.getString("format_type")));
                col.setPrecission(getFieldPrecission(r.getString("format_type")));
                
                
                col.setDescription(r.getString("description"));
                col.setDefaultValue(r.getString("adsrc"));
                col.setTableName(r.getString("relname"));
                
                //clave: posicion de la columna dentro de la tabla
                //valor: columna 
                htColumns.put(new Integer(r.getInt("attnum")),col);                
            }            
            
            s = null;
            r = null;
            //select contype, conkey from pg_constraint,pg_class
            //  where pg_class.relname=?
            //    and pg_constraint.conrelid=pg_class.oid
            //    and (contype='p' or contype='u');
            s = conn.prepareStatement("LMobtenerColumnasUnicas"); 
            s.setString(1, tableName);
            r = s.executeQuery();            
            
            ArrayList lst = new ArrayList();
            while (r.next()){
                lst.add(r.getString("conkey"));
                
                ((ColumnDB)htColumns.get(new Integer(r.getInt("conkey")))).setUnique(true);
                if (r.getString("contype").equalsIgnoreCase("P"))
                    ((ColumnDB)htColumns.get(new Integer(r.getInt("conkey")))).setPrimary(true);
                
            }            
            
        }        
        catch (SQLException ex){
            throw new DataException();            
        }        
        finally{
        	cerrarConexiones(r, null, s, conn, null);
        }         
        return htColumns;
    }
    
    /**
     * Obtiene la precision de un campo a partir del valor del formato en el catalogo
     * de PostgreSQL
     * 
     * @param formatType Cadena de caracteres que contiene el valor del formato en el 
     * catalogo de PostgreSQL
     * 
     * @return Precision del campo
     */
    private int getFieldPrecission(String formatType){
        int prec =0;
        try{
            if (formatType.indexOf(",")>0)            
                prec = Integer.valueOf(formatType.substring(formatType.indexOf(",")+1,formatType.indexOf(")"))).intValue();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return prec;
    }
    
    /**
     * Obtiene la longitud de un campo a partir del valor del formato en el catalogo
     * de PostgreSQL
     * @param formatType Cadena de caracteres que contiene el valor del formato en el 
     * catalogo de PostgreSQL
     * @return Longitud del campo
     */
    private int getFieldLength(String formatType){
    	
        int lon=0;
        try{
            if (formatType.indexOf(",")>0)           
                lon= Integer.valueOf(formatType.substring(formatType.indexOf("(")+1,formatType.indexOf(","))).intValue();
            else if (formatType.indexOf("(")>0)           
                lon= Integer.valueOf(formatType.substring(formatType.indexOf("(")+1,formatType.indexOf(")"))).intValue();            
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return lon;
        
    }
    
    /**
     * Recupera la lista de columnas dadas de alta en GeoPISTA para una tabla dada
     * 
     * @param idTable Identificador de la tabla
     * 
     * @return Hashtable con las columnas como valor (objeto com.geopista.feature.Column)
     * y el nombre de la colulmna en base de datos como clave.
     * 
     * @throws DataException Si se produce un error de acceso a datos
     */
    
    public HashMap obtenerListaColumnasSistema(int idTable) throws DataException{
        HashMap htColSis = new HashMap();            
        PreparedStatement s = null;
        ResultSet r = null;
        
        try{        	
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();
    		
            s = conn.prepareStatement("LMobtenercolumnasSis");
            s.setInt(1, idTable);                 
            
            r = s.executeQuery();  
            while (r.next()){
                Column col = new Column();            
                Attribute att = new Attribute();                
                
                col.setName(r.getString("name"));
                col.setIdColumn(r.getInt("id"));
                att.setType(r.getString("Type"));                
                att.setColumn(col); 
                col.setAttribute(att);          
                
                htColSis.put(col.getName(), col);                
            }       
        }        
        catch (SQLException ex){
            throw new DataException(ex);
        }
        finally{
        	cerrarConexiones(r, null, s, conn, null);
        } 
        return htColSis;        
    }
    
    /**
     * Crea una tabla en la base de datos y la da de alta en GeoPISTA
     * 
     * @param table Objeto com.geopista.feature.Table con los datos de la tabla a crear
     * 
     * @return Verdadero si se crea correctamente
     * 
     * @throws DataException Si se produce un error de acceso a datos
     */
    
    public boolean crearTablaBD(Table table) throws DataException{    
        boolean bRes = false;
        PreparedStatement s = null;
        ResultSet r = null;
        
        try{ 
            //create table ? () WITH OIDS;
            //ALTER TABLE ? OWNER TO postgres;
            //GRANT ALL ON TABLE ? TO postgres;
            //GRANT SELECT ON TABLE ? TO visualizador;
            //GRANT SELECT ON TABLE ? TO guiaurbana;
            //GRANT SELECT ON TABLE ? TO consultas;
            
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();
    		 		
            StringBuffer query = new StringBuffer();
            String nomTable = table.getDescription();            
            query.append("CREATE TABLE \"").append(nomTable).append("\" () WITH OIDS; GRANT ALL ON TABLE \"")
            .append(nomTable).append("\" TO visualizador; GRANT SELECT ON TABLE \"")
            .append(nomTable).append("\" TO guiaurbana; GRANT SELECT ON TABLE \"")
            .append(nomTable).append("\" TO consultas;");

            s = conn.prepareStatement(query.toString());

            if (s.executeUpdate()>=0)
                GeopistaFunctionUtils.generarSQL(s);
            
            //insert into tables (id_table, name, geometrytype, external) values(nextval(''seq_tables''),?,?,?)'
            s = conn.prepareStatement("LMcrearTablaSistema");
            s.setString(1, nomTable);
            if (table.getGeometryType()<0)
                s.setNull(2, Types.INTEGER);
            else
                s.setInt(2, table.getGeometryType());
            s.setInt(3, table.getExternal());
            if(s.executeUpdate()>=0)           
                GeopistaFunctionUtils.generarSQL (s);
            
            //select currval('seq_tables');
            s = null;
            s = conn.prepareStatement("LMsecTables");
            r = s.executeQuery();
            if (r.next()){
                table.setIdTabla(r.getInt(1));
                table.setName(table.getDescription());
            }
            
            bRes = true; 
            
        }
        catch (SQLException ex){
            throw new DataException(ex);
        }
        finally{
        	cerrarConexiones(r, null, s, conn, null);
        }         
        return bRes;
    }    
    
    /**
     * Modifica una tabla de base de datos y la actualiza en el sistema
     * 
     * @param oriTable Objeto de tipo com.geopista.feature.Table con los datos de la tabla
     * a modificar
     * 
     * @param modTable Objeto de tipo com.geopista.feature.Table con los datos modificados
     * 
     * @return Verdadero si la operacion se realiza correctamente
     * 
     * @throws DataException Si se produce un error de acceso a datos
     */
    public boolean modificarTablaBD(Table oriTable, Table modTable) throws DataException{       
        boolean bRes = false;
        PreparedStatement s = null;
        try{             
            //ALTER TABLE ?
            //RENAME TO ?;
        	
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();
    		   		
            if (oriTable!=null && oriTable.getDescription()!=null){
                if (!oriTable.getDescription().equals(modTable.getDescription())){
                    StringBuffer query = new StringBuffer();
                    query.append("ALTER TABLE \"").append(oriTable.getDescription())
                    .append("\" RENAME TO \"").append(modTable.getDescription().toLowerCase()).append("\";");
                    query.append("ALTER TABLE \"seq_").append(oriTable.getDescription())
                    .append("\" RENAME TO \"seq_").append(modTable.getDescription().toLowerCase()).append("\";");

                    s = conn.prepareStatement(query.toString());
                    if (s.executeUpdate()>=0)
                        GeopistaFunctionUtils.generarSQL(s);
                    
                }
                
                
                //Si tiene nombre es porque existe la tabla de sistema, ademas de la tabla de BD
                //Solo se modifica si la geometria o el nombre han variado
                if (oriTable.getName()!=null
                        && (!oriTable.getDescription().equals(modTable.getDescription())
                                || oriTable.getGeometryType()!= modTable.getGeometryType())){                    
                    //modificar el nombre y la geometria de la tabla de sistema...
                                        
                    //update tables set name=?, geometrytype=? where id_table=?
                    s = conn.prepareStatement("LMmodificarTablaSistema");
                    s.setString(1, modTable.getDescription());
                    s.setInt(2, modTable.getGeometryType());
                    s.setInt(3, oriTable.getIdTabla());
                    if(s.executeUpdate()>=0)
                        GeopistaFunctionUtils.generarSQL(s);
                    
                    modTable.setIdTabla(oriTable.getIdTabla());
                    modTable.setName(modTable.getDescription());

                }               
            }            
            bRes = true;            
        }
        catch (SQLException ex){
            throw new DataException(ex);
        }
        finally{
        	cerrarConexiones(null, null, s, conn, null);
        } 
        
        return bRes;
    }    
    
    /**
     * Elimina una tabla de Base de Datos y del sistema
     * 
     * @param table Objeto de tipo com.geopista.feature.Table con los datos de la tabla a eliminar
     *    
     * @return Verdadero si la operacion se realiza correctamente
     * 
     * @throws DataException Si se produce un error de acceso a datos
     */
    public boolean eliminarTablaBD(Table table) throws DataException{        
        boolean bRes = false;

        PreparedStatement s = null;
        ResultSet r = null;
        
        try{             
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();
    		
            //DROP TABLE ?
    		
            if (table.getDescription()!=null)
            {
                
                StringBuffer query = new StringBuffer();
                
                query.append("SELECT * FROM queries WHERE selectquery like '%\"").append(table.getDescription()).append("\"%' or updatequery like '%\"")
                .append(table.getDescription()).append("\"%' or deletequery like '%\"").append(table.getDescription()).append("\"%';");
                s = conn.prepareStatement(query.toString());

                r = s.executeQuery();
                // Si devuelve algo es porque hay alguna capa que este usando la tabla, por lo que no se puede borrar
                if (!r.next()){
                	query = new StringBuffer();
                	query.append("DROP TABLE \"").append(table.getDescription()).append("\";");
                         
	                s = conn.prepareStatement(query.toString());
	                if (s.executeUpdate()>=0)
	                    GeopistaFunctionUtils.generarSQL(s);
	                                
	                //Si tiene nombre es porque existe la tabla de sistema, ademas de la tabla de BD
	                if (table.getName()!=null){
	                    //select id_layer, Position, id_alias from attributes 
	                    // where id_column in (select id from columns where id_table=?);
	
	                    s = conn.prepareStatement("LMbuscarCapasModificar");
	                    s.setInt(1, table.getIdTabla());
	                    
	                    Hashtable ht = new Hashtable();
	                    ArrayList lstAlias = new ArrayList();
	                    r = s.executeQuery();  
	                    while (r.next()){
	                        ht.put(r.getObject("id_layer"), r.getObject("Position"));
	                        lstAlias.add(r.getObject("id_alias"));
	                    }                    
	                    
	                    //Si la tabla forma parte de una capa, habra que eliminarla de
	                    //la capa, del diccionario, de la lista de atributos, y de las tablas
	                    //de sistema tables y columns
	                    if (eliminarTablaFromLayer(table.getIdTabla(),ht)>=0){
	                        if(eliminarAliasFromDictionary (lstAlias)>=0)
	                            if (eliminarAtributosTabla(table.getIdTabla())>=0)
	                                if (eliminarColumnsTable (table.getIdTabla())>=0)
	                                    if(eliminarTable (table.getIdTabla())>=0)
	                                        bRes = true;    
	                    }
	                    //Si la tabla no forma parte de ninguna capa, solo habra que eliminarla
	                    //de las tablas de sistema tables y columns
	                    else{
	                        if (eliminarColumnsTable (table.getIdTabla())>=0)
	                            if(eliminarTable (table.getIdTabla())>=0)
	                                bRes = true;
	                    }
	                } 
	                else
	                    bRes = true;
                }
            }  
        }
        catch (SQLException ex){
            throw new DataException(ex);
        }
        finally{
        	cerrarConexiones(r, null, s, conn, null);
        } 
        return bRes;
    }    
    
    /**
     * Elimina la entrada de la tabla <code>tables</code>
     * 
     * @param idTabla Identificador de la tabla a eliminar
     * 
     * @return int Numero de registros actualizados (-1 en caso de error)
     * 
     * @throws DataException Si se produce un error de acceso a datos
     */
    private int eliminarTable(int idTabla) throws DataException{
        int iRes = NO_RESULTS;
        PreparedStatement s = null;
        
        try{                 
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();
    		
            s = conn.prepareStatement("LMborrarTables");
            s.setInt(1, idTabla);      
            iRes = s.executeUpdate();            
            if(iRes>=0)            
                GeopistaFunctionUtils.generarSQL (s);                

        }
        catch (SQLException ex){
            throw new DataException(ex);
        }
        finally{
        	cerrarConexiones(null, null, s, conn, null);
        }         
        return iRes;
    }
    
    /**
     * Elimina las entradas de la tabla <code>columns</code> para el borrado de una tabla
     * 
     * @param idTabla Identificador de la tabla cuyas columnas se van a eliminar
     * 
     * @return int Numero de registros actualizados (-1 en caso de error)
     * 
     * @throws DataException Si se produce un error de acceso a datos
     */
    private int eliminarColumnsTable(int idTabla) throws DataException{   
        int iRes = NO_RESULTS;
        PreparedStatement s = null;
        try{                             
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();

            s = conn.prepareStatement("LMborrarColumns");
            s.setInt(1, idTabla);   
            s.setInt(2, idTabla);
            
            iRes = s.executeUpdate();
            if (iRes>=0)
                GeopistaFunctionUtils.generarSQL (s);

        }
        catch (SQLException ex){
            throw new DataException(ex);
        }
        finally{
        	cerrarConexiones(null, null, s, conn, null);
        }
        return iRes;
    }
    /**
     * Elimina las entrada de la tabla <code>attributes</code>
     * 
     * @param idTabla Identificador de la tabla cuyos atributos se van a eliminar
     * 
     * @return int Numero de registros actualizados (-1 en caso de error)
     * 
     * @throws DataException Si se produce un error de acceso a datos
     */ 
    private int eliminarAtributosTabla(int idTabla) throws DataException{
        int iRes = NO_RESULTS;        
        PreparedStatement s = null;
        
        try{                 	
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();
    		
            s = conn.prepareStatement("LMborrarAtributosTabla");
            s.setInt(1, idTabla);      
            iRes = s.executeUpdate();
            if (iRes>=0)
                GeopistaFunctionUtils.generarSQL(s);      
        }
        catch (SQLException ex){
            throw new DataException(ex);
        }
        finally{
        	cerrarConexiones(null, null, s, conn, null);
        }
        return iRes;
        
    }
    
  
    /**
     * Elimina las relaciones entre una tabla y las capas a las que hace referencia
     * 
     * @param table int Identificador numerico de la tabla
     * 
     * @param ht Hashtable con las capas que hacen referencia a la tabla a borrar como valor, y
     * la posicion de los atributos dentro de la capa como clave.
     * 
     * @return int Numero de registros actualizados (-1 en caso de error)
     * 
     * @throws DataException Si se produce un error de acceso a datos
     */
    private int eliminarTablaFromLayer(int idTable, Hashtable ht) throws DataException{         
        int iRes = NO_RESULTS;
        PreparedStatement s = null;   
        Iterator it = ht.keySet().iterator();
        while (it.hasNext()){
            Object key = it.next();
            int idLayer = Integer.valueOf(key.toString()).intValue();   
            int position = Integer.valueOf(ht.get(key).toString()).intValue();
            
            try{             
        		if ((conn == null) || (conn.isClosed()))
        			conn = getDBConnection();

                //delete from attributes where id_layer=? and Position=?; 
                //update layers set Position=Position-1 where id_layer=? and Position>?
                s = conn.prepareStatement("LMactualizarLayerPositions");
                s.setInt(1, idLayer);
                s.setInt(2, position);
                s.setInt(3, idLayer);
                s.setInt(4, position);
                iRes = s.executeUpdate();
                if (iRes>=0)
                    GeopistaFunctionUtils.generarSQL(s);
                
            }
            catch (SQLException ex){
                throw new DataException(ex);
            }
            finally{
            	cerrarConexiones(null, null, s, conn, null);
            }
        }          
        return iRes;        
    }
    
    /**
     * Crea una nueva columna de Base de datos, y la da de alta en la tabla
     * <code>columns</code> de GeoPISTA
     * 
     * @param table Tabla a la que pertenece la columna que se va a crear
     * 
     * @param colrow Columna a crear
     * 
     * @return Verdadero si la operacion se realiza correctamente
     * 
     * @throws DataException Si se produce un error de acceso a datos
     */ 
    public boolean crearColumnaBD(Table table, ColumnRow colrow) throws DataException{   
        boolean bRes = false;
        PreparedStatement s = null;
        ResultSet r = null;
        try{ 
            //ALTER TABLE nomTable
            //ADD COLUMN nomColumn type[(tam)] [UNIQUE] (ej. CHAR(3) UNIQUE;)            
            //ALTER TABLE nomTable
            //ALTER COLUMN nomColumn SET DEFAULT defaultValue;            
            //ALTER TABLE nomTable
            //ALTER COLUMN nomColumn SET NOT NULL;            
            //COMMENT ON COLUMN nomTable.nomColumn
            //IS 'comment';            
            
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();

            StringBuffer query = new StringBuffer();
            String nomTable = table.getDescription();            
            query.append("ALTER TABLE \"").append(nomTable).append("\" ADD COLUMN \"")
            .append(colrow.getColumnaBD().getName()).append("\" ")
            .append(JDialogDBColumn.translateBDtoComboItem(colrow.getColumnaBD().getType()));
            
            if (colrow.getColumnaBD().getLength()!=0){
                query.append("(").append(colrow.getColumnaBD().getLength());
                
                if (colrow.getColumnaBD().getPrecission()!=0){
                    query.append(",").append(colrow.getColumnaBD().getPrecission());
                }
                query.append(") ");
            }
            
            if (colrow.getColumnaBD().isUnique()){
                query.append(" UNIQUE; ");
            }
            else{
                query.append(";");
            }
            if (colrow.getColumnaBD().isPrimary()){
                query.append("ALTER TABLE \"").append(nomTable).append("\" ADD CONSTRAINT \"")
                .append(nomTable).append("_pkey\" PRIMARY KEY(")
                .append(colrow.getColumnaBD().getName()).append(");");
            }
            
            if ((colrow.getColumnaBD().getDefaultValue()!=null)&&(!colrow.getColumnaBD().getDefaultValue().trim().equals(""))){
                query.append("ALTER TABLE \"").append(nomTable).append("\" ALTER COLUMN \"")
                .append(colrow.getColumnaBD().getName()).append("\" SET DEFAULT '")
                .append(colrow.getColumnaBD().getDefaultValue().replaceAll("'", "")).append("';");
            }
            
            if (colrow.getColumnaBD().isNotNull()){
                query.append("ALTER TABLE \"").append(nomTable).append("\" ALTER COLUMN \"")
                .append(colrow.getColumnaBD().getName()).append("\" SET NOT NULL;");
            }
            
            if ((colrow.getColumnaBD().getDescription()!=null)&&(!colrow.getColumnaBD().getDescription().trim().equals(""))){
                query.append("COMMENT ON COLUMN \"").append(nomTable).append("\".\"")
                .append(colrow.getColumnaBD().getName()).append("\" IS '")
                .append(colrow.getColumnaBD().getDescription()).append("';");
            }
                      
            s = conn.prepareStatement(query.toString());
            if (s.executeUpdate()>=0)
                GeopistaFunctionUtils.generarSQL(s);
            
            if (colrow.getColumnaSistema().getDescription()!=null){                 
              
                //insert into columns (id, name,id_table, "Type") 
                // values(nextval('seq_columns'),?,?,?)
                //s = conn.prepareStatement("LMcrearColumnaSistema");
                s = conn.prepareStatement("LMinsertarColumnaSistema");
                
                s.setInt(1, table.getIdTabla());
                s.setString(2, colrow.getColumnaBD().getName());
                if (ColumnDB.getGeometryType(colrow.getColumnaBD().getType())==ColumnDB.VARCHAR){
                	s.setInt(3, colrow.getColumnaBD().getLength());
                	s.setNull(4,Types.NUMERIC);
                	s.setNull(5,Types.NUMERIC);
                }
                else{
                	s.setNull(3, Types.NUMERIC);
                	s.setInt(4, colrow.getColumnaBD().getLength());
                    s.setInt(5, colrow.getColumnaBD().getPrecission());
                }
                                
                //s.setString(1, colrow.getColumnaSistema().getDescription());
                //s.setInt(2, table.getIdTabla());
                
                String tipo = colrow.getColumnaBD().getType();
                int idTipo = 0;
                
                if (tipo.equalsIgnoreCase("geometry"))
                    idTipo = 1;
                else if (tipo.equalsIgnoreCase("numeric") ||
                        tipo.equalsIgnoreCase("int4") || tipo.equalsIgnoreCase("float8"))
                    idTipo = 2;
                else if (tipo.equalsIgnoreCase("bpchar") || 
                        tipo.equalsIgnoreCase("varchar") || tipo.equalsIgnoreCase("character"))
                    idTipo = 3;
                else if (tipo.equalsIgnoreCase("timestamp")||
                        tipo.equalsIgnoreCase("date"))
                    idTipo = 4;
                else if (tipo.equalsIgnoreCase("bool")||
                tipo.equalsIgnoreCase("boolean"))
                    idTipo = 5;
                
                if (idTipo == 0)
                    s.setNull(6, 2); //tipo 2 para numericos
                else
                    s.setInt(6, idTipo);
                
                if(s.executeUpdate()>=0)                
                    GeopistaFunctionUtils.generarSQL (s);
                
                // select currval('seq_columns')
                s = null;
                r = null;
                s = conn.prepareStatement("LMcolumnaSistemaCurrval");
                r = s.executeQuery();
                if (r.next())        
                    colrow.getColumnaSistema().setIdColumn(r.getInt(1));

            }            
            table.getColumns().put(new Integer(getNextColumnPosition(table.getColumns())), colrow.getColumnaBD());
            bRes = true;            
            
        }
        catch (SQLException ex){
            throw new DataException(ex);
        }
        finally{
        	cerrarConexiones(r, null, s, conn, null);
        }
        return bRes;
    }    
    
    public int getNextColumnPosition(HashMap columns){
        int pos = 0;
        
        Iterator it= columns.keySet().iterator();
        while (it.hasNext()){   
            Object o = it.next();
            if(o instanceof Integer){
                int aux= Integer.valueOf(o.toString()).intValue();
                if (aux>pos)
                    pos = aux;
            }
                        
        }     
        
        return ++pos;
    }

    /**
     * Modifica una columna de Base de datos, y actualiza sus datos en la tabla
     * <code>columns</code> de GeoPISTA
     * @param table Tabla a la que pertenece la columna que se va a modificar
     * @param colrowMod Columna a modificar
     * @param colrowOri Columna original
     * @return Verdadero si la operacion se realiza correctamente
     * @throws DataException Si se produce un error de acceso a datos
     */     
    public boolean modificarColumnaBD(Table table, ColumnRow colrowMod, ColumnRow colrowOri) throws DataException{  
        boolean bRes = false;

        PreparedStatement s = null;
        
        try{  
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();
    		
  		
            String query = getBDQuery(table, colrowMod, colrowOri);
                      
            s = conn.prepareStatement(query);
            if (s.executeUpdate()>=0)
                GeopistaFunctionUtils.generarSQL(s);
            
            if (colrowMod.getColumnaSistema().getDescription()!=null
                    && colrowOri.getColumnaSistema().getIdColumn()!=0){
                colrowMod.getColumnaSistema().setIdColumn(colrowOri.getColumnaSistema().getIdColumn());
                
                //update columns set name=?, "Length"=?, "Precision"=?, "Type"=? where id=?
                s = conn.prepareStatement("LMmodificarColumnaSistema");
                s.setString(1, colrowMod.getColumnaSistema().getName());
                
                if (ColumnDB.getGeometryType(colrowMod.getColumnaBD().getType())==ColumnDB.VARCHAR){
                	s.setInt(2, colrowMod.getColumnaBD().getLength());
                	s.setNull(3,Types.NUMERIC);
                	s.setNull(4,Types.NUMERIC);
                }
                else{
                	s.setNull(2, Types.NUMERIC);
                	s.setInt(3, colrowMod.getColumnaBD().getLength());
                    s.setInt(4, colrowMod.getColumnaBD().getPrecission());
                }
                
                s.setInt(5, ColumnDB.getGeometryType(colrowMod.getColumnaBD().getType()));
                s.setInt(6, colrowMod.getColumnaSistema().getIdColumn());
                if(s.executeUpdate()>=0)
                    GeopistaFunctionUtils.generarSQL(s);
                
            }
            
            // Si la columna de base de datos que se ha modificado no tenia previamente una
            //columna de sistema asociada, y ahora se ha rellenado el campo de columna de sistema
            if (colrowOri.getColumnaSistema().getName()==null && 
                    colrowMod.getColumnaSistema().getName()!=null && 
                    !colrowMod.getColumnaSistema().getName().trim().equals("")){
                if(crearColumnaSistema(table, colrowMod)>=0)
                    bRes=true;
            }
            
            // Si se desea eliminar la columna de sistema asociada a la columna de base de datos
            if (colrowMod.getColumnaSistema().getName()==null &&
                    colrowOri.getColumnaSistema().getName()!=null){
                return eliminarColumnaSistema(table, colrowOri);
            }
            
            bRes = true;
        }
        catch (SQLException ex){
            throw new DataException(ex);
        }  
        finally{
        	cerrarConexiones(null, null, s, conn, null);
        }
        
        return bRes;
    }    
    
    private String getBDQuery(Table table, ColumnRow colrowMod, ColumnRow colrowOri) throws DataException{
        StringBuffer query = new StringBuffer();
        String nomTable = table.getName();
        
        long ts= System.currentTimeMillis(); 
        
        if (!colrowOri.getColumnaBD().getType().equalsIgnoreCase(colrowMod.getColumnaBD().getType())
                || colrowOri.getColumnaBD().getLength() != colrowMod.getColumnaBD().getLength()
                || colrowOri.getColumnaBD().getPrecission() != colrowMod.getColumnaBD().getPrecission()){
            //CREATE LOCAL TEMPORARY TABLE "xxx" (col1 tipo1, col2 tipo2 ...) with oids;
            //Recupera la posicion de la columna a modificar dentro de la tabla (empieza en 1)
            int pos = findColumnPosition(table, colrowOri);
            if (pos==0) 
                throw new DataException(I18N.get("GestorCapas","tablasBD.error.columna"));
            
            query.append("CREATE LOCAL TEMPORARY TABLE \"")
            .append(nomTable).append(ts).append("\" (");
            
            ColumnDB[] colsBD = getColumnsFromTableBD(table);
            
            for (int i=0; i< colsBD.length; i++){
                query.append("\"").append(colsBD[i].getName()).append("\" ")
                .append(colsBD [i].getType()).append(" ,");
                
            }
            query = new StringBuffer(query.substring(0, query.length()-1));
            query.append(") WITH OIDS; ");
            
            //INSERT INTO "xxx" ("col1", "col2", ...)
            // SELECT "c1", "c2", "c3" FROM "yy";    
            query.append("INSERT INTO \"").append(nomTable).append(ts).append("\" (");
            StringBuffer nomColumns = new StringBuffer();
            StringBuffer nomNewColumns = new StringBuffer();
            for (int i=0; i< colsBD.length; i++){
                nomColumns.append("\"").append(colsBD[i].getName()).append("\",");
                
                if (i!=pos-1 && pos>0){   
                    nomNewColumns.append("\"").append(colsBD[i].getName()).append("\",");                        
                }   
                else{
                    //CAST("c3" AS char(1))
                    nomNewColumns.append(" CAST(\"").append(colrowOri.getColumnaBD().getName())
                    .append("\" AS ").append(JDialogDBColumn.translateBDtoComboItem(colrowMod.getColumnaBD().getType()));
                    
                    if (colrowMod.getColumnaBD().getLength()!=0){
                        nomNewColumns.append("(").append(colrowMod.getColumnaBD().getLength());
                        
                        if (colrowMod.getColumnaBD().getPrecission()!=0){
                            nomNewColumns.append(",").append(colrowMod.getColumnaBD().getPrecission());
                        }
                        nomNewColumns.append(")");
                    }                        
                    nomNewColumns.append("),");
                }                    
            }
            nomNewColumns = new StringBuffer(nomNewColumns.substring(0, nomNewColumns.length()-1));
            nomColumns = new StringBuffer(nomColumns.substring(0, nomColumns.length()-1));
            query.append(nomColumns).append(") SELECT ").append(nomColumns)
            .append(" FROM \"").append(nomTable).append("\";");                
            
            //DROP TABLE "YYY"
            query.append("DROP TABLE \"").append(nomTable).append("\";");
            
            //CREATE TABLE "YYY" () WITH OIDS;
            query.append ("CREATE TABLE \"").append(nomTable).append("\" () WITH OIDS; ALTER TABLE \"")
            .append(nomTable).append("\" OWNER TO ")
            
            //Cuando se entra directamente a traves del administrador de cartografia no se solicita al
            //usuario el usuario de la base de Datos por lo que la propiedad USER_BD viene a null
            //Vamos a poner directamente geopista aunque es demasiado "a fuego".
            //.append(aplicacion.getBlackboard().get("USER_BD"))
            .append("geopista")
            
            .append("; GRANT ALL ON TABLE \"")
            .append(nomTable).append("\" TO visualizador; GRANT SELECT ON TABLE \"")
            .append(nomTable).append("\" TO guiaurbana; GRANT SELECT ON TABLE \"")
            .append(nomTable).append("\" TO consultas;");
            
            
            //ADD COLUMN
            for (int i=0; i<colsBD.length; i++){
                ColumnDB cbd = null;
                if (i==pos-1)                    
                    cbd = colrowMod.getColumnaBD();
                else                   
                    cbd = colsBD[i];     
                
                query.append("ALTER TABLE \"").append(nomTable).append("\" ADD COLUMN \"")
                .append(colsBD[i].getName()).append("\" ")
                .append(JDialogDBColumn.translateBDtoComboItem(cbd.getType()));
                
                if (cbd.getLength()!=0){
                    query.append("(").append(cbd.getLength());
                    
                    if (cbd.getPrecission()!=0){
                        query.append(",").append(cbd.getPrecission());
                    }
                    query.append(") ");
                }
                
                if (cbd.isUnique()){
                    query.append(" UNIQUE; ");
                }
                else{
                    query.append("; ");
                }
                
                //Para los valores por defecto, el comentario, y los check de no nulo y unico
                //se utiliza como nombre el anterior a la modificacion, dado que el proceso de 
                //edicion realiza el cambio de nombre al final del todo.
                if (cbd.getDefaultValue()!=null 
                        && !cbd.getDefaultValue().trim().equals("")){
                    query.append("ALTER TABLE \"").append(nomTable).append("\" ALTER COLUMN \"")
                    .append(colsBD[i].getName()).append("\" SET DEFAULT '")
                    .append(cbd.getDefaultValue().replaceAll("'", "")).append("';");
                }
                
                if (cbd.isNotNull()){
                    query.append("ALTER TABLE \"").append(nomTable).append("\" ALTER COLUMN \"")
                    .append(colsBD[i].getName()).append("\" SET NOT NULL;");
                }
                
                if (cbd.getDescription()!=null &&
                        !cbd.getDescription().trim().equals("")){
                    query.append("COMMENT ON COLUMN \"").append(nomTable).append("\".\"")
                    .append(colsBD[i].getName()).append("\" IS '")
                    .append(cbd.getDescription()).append("';");
                }                    
            }
            
            //INSERT INTO "public"."a2" ("c1", "c3", "c2")
            // SELECT "c1", CAST("c3" AS char(1)), "c2" FROM "a20lqwaa";
            query.append("INSERT INTO \"").append(nomTable).append("\" (").append(nomColumns)
            .append(") SELECT ").append(nomNewColumns).append(" FROM \"").append(nomTable).append(ts).append("\";");
            
        }
        else{
            //Modifica el valor por defecto, si este ha sido editado
            
            if (colrowOri.getColumnaBD().getDefaultValue()!=null &&
                    colrowMod.getColumnaBD().getDefaultValue()!=null && 
                    !colrowOri.getColumnaBD().getDefaultValue().equals(
                            colrowMod.getColumnaBD().getDefaultValue())  && 
                            !colrowMod.getColumnaBD().getDefaultValue().trim().equals("")){
                //ALTER TABLE YY ALTER COLUMN CC SET DEFAULT AA
                query.append("ALTER TABLE \"").append(nomTable).append("\" ALTER COLUMN \"")
                .append(colrowOri.getColumnaBD().getName()).append("\" SET DEFAULT '")
                .append(colrowMod.getColumnaBD().getDefaultValue().replaceAll("'", "")).append("';");
                
            }
            else if (colrowOri.getColumnaBD().getDefaultValue()!=null &&
                    colrowMod.getColumnaBD().getDefaultValue()!=null && 
                    !colrowOri.getColumnaBD().getDefaultValue().equals("") &&                    
                    colrowMod.getColumnaBD().getDefaultValue().trim().equals("")){
                //ALTER TABLE YY ALTER COLUMN CC DROP DEFAULT
                query.append("ALTER TABLE \"").append(nomTable).append("\" ALTER COLUMN \"").
                append(colrowOri.getColumnaBD().getName()).append("\" DROP DEFAULT;");
            }
            
            //Modifica el comentario
            //COMMENT ON COLUMN YY.CC IS 'com';
            if (colrowMod.getColumnaBD().getDescription()!=null){
                query.append("COMMENT ON COLUMN \"").append(nomTable).append("\".\"")
                .append(colrowOri.getColumnaBD().getName()).append("\" IS '")
                .append(colrowMod.getColumnaBD().getDescription()).append("';");                        
            }
            
            //Modifica si es unico
            if (colrowMod.getColumnaBD().isUnique()!=colrowOri.getColumnaBD().isUnique()
                    &&  colrowMod.getColumnaBD().isUnique()){
                //ALTER TABLE yyy ADD UNIQUE ("cc");
                query.append("ALTER TABLE \"").append(nomTable).append("\" ADD UNIQUE(\"")
                .append(colrowOri.getColumnaBD().getName()).append("\");");
            }
            else if (colrowMod.getColumnaBD().isUnique()!=colrowOri.getColumnaBD().isUnique()
                    &&  colrowOri.getColumnaBD().isUnique()){
                if (colrowOri.getColumnaBD().isPrimary()){
                    //ALTER TABLE "public"."AAA0" DROP CONSTRAINT "AAA0_pkey" RESTRICT;
                    query.append("ALTER TABLE \"").append(nomTable).append("\" DROP CONSTRAINT \"")
                    .append(nomTable).append("_pkey\" RESTRICT;");
                    colrowOri.getColumnaBD().setPrimary(false);
                }
                else{
                    //ALTER TABLE "public"."a2" DROP CONSTRAINT "a2_c111_key" RESTRICT;
                    query.append("ALTER TABLE \"").append(nomTable).append("\" DROP CONSTRAINT \"")
                    .append(nomTable).append("_").append(colrowOri.getColumnaBD().getName())
                    .append("_key\" RESTRICT;");
                }
            }
            
            //Modifica si es no nulo
            if (colrowMod.getColumnaBD().isNotNull()){
                //ALTER TABLE YY ALTER COLUMN CC SET NOT NULL;
                query.append("ALTER TABLE \"").append(nomTable).append("\" ALTER COLUMN \"")
                .append(colrowOri.getColumnaBD().getName()).append("\" SET NOT NULL;");
                
            }
            else{
                //ALTER TABLE YY ALTER COLUMN CC DROP NOT NULL;
                query.append("ALTER TABLE \"").append(nomTable).append("\" ALTER COLUMN \"")
                .append(colrowOri.getColumnaBD().getName()).append("\" DROP NOT NULL;");
            }
        }
        
        //ALTER TABLE XX RENAME COLUMN CC TO CCC;
        if (!colrowMod.getColumnaBD().getName().equals(colrowOri.getColumnaBD().getName())){
            query.append("ALTER TABLE \"").append(nomTable).append("\" RENAME COLUMN \"")
            .append(colrowOri.getColumnaBD().getName()).append("\" TO \"")
            .append(colrowMod.getColumnaBD().getName()).append("\";");
        }
        
        table = replaceColumn(table,colrowOri, colrowMod);
        
        return query.toString();
         
    }

    /**
     * Busca la posicion de una columna dentro de una tabla
     * 
     * @param table Table donde buscar la columna
     * @param colrow ColumnRow a buscar dentro de la tabla
     * @return Posicion de la columna, o 0 en caso de no encontrarla
     */
    public int findColumnPosition(Table table, ColumnRow colrow){
        int pos=0;
        HashMap hm = new HashMap(table.getColumns());
        
        Iterator it = hm.keySet().iterator();
        while (it.hasNext()){
            Object obj = it.next();
            if(obj instanceof Integer && hm.get((Integer)obj) instanceof ColumnDB && 
                    ((ColumnDB)hm.get((Integer)obj)).equals(colrow.getColumnaBD()))
                pos = ((Integer)obj).intValue();
            
            else if (hm.get(obj) instanceof Column)
                System.out.println("   ************** ES COLUMN!!!! *********");
            else if (hm.get(obj) instanceof ColumnRow)
                System.out.println("   ************** ES COLUMN ROW!!!! *********");
            
        }
        
        return pos;
    }
    
    /**
     * Sustituye una columna por otra dentro de las columnas de base de datos de un objeto Table
     * @param table Table a la que pertenece la columna a sustituir
     * @param colrowOri ColumnRow original
     * @param colrowMod ColumnRow con los datos de la modificacion
     * @return Table modificada
     */
    private Table replaceColumn(Table table, ColumnRow colrowOri, ColumnRow colrowMod) throws DataException{
        if (!table.getColumns().containsValue(colrowOri.getColumnaBD()))
            throw new DataException(I18N.get("GestorCapas","tablasBD.error.columna"));
        
        HashMap hm = new HashMap(table.getColumns());
        
        Iterator it = hm.keySet().iterator();
        while (it.hasNext()){
            Object obj = it.next();
            if (obj instanceof Integer &&((ColumnDB)hm.get((Integer)obj)).equals(colrowOri.getColumnaBD()))
                hm.put(obj, colrowMod.getColumnaBD());
            
            if (hm.get(obj) instanceof Column)
                System.out.println("   ************** ES COLUMN!!!! *********");
            /*
            if(obj instanceof Integer && hm.get((Integer)obj) instanceof ColumnDB && 
                    ((ColumnDB)hm.get((Integer)obj)).equals(colrowOri.getColumnaBD()))
                hm.put(obj, colrowMod.getColumnaBD());
            else if(obj instanceof String && hm.get((String)obj) instanceof Column && 
                    ((Column)hm.get((String)obj)).equals(colrowOri.getColumnaSistema()))
                hm.put(obj, colrowMod.getColumnaBD());
                */
            
        }
        table.setColumns(hm);
        
        return table;
    }
    
    /**
     * Obtiene la lista de columnas de base de datos de una tabla en forma de array de ColumnDB
     * @param table Table de base de datos
     * @return ColumnDB[] con la lista de columnas de base de datos de la tabla
     */
    private ColumnDB[] getColumnsFromTableBD(Table table){
        HashMap hmAux = new HashMap(table.getColumns());
        ColumnDB col[] = new ColumnDB[hmAux.size()];
        int position=0;
        int maxPos = getNextColumnPosition(table.getColumns());
        for (int i=0; i< maxPos; i++){
            if (!hmAux.isEmpty()){
                if (hmAux.get(new Integer(i))!=null 
                        && hmAux.get(new Integer(i)) instanceof ColumnDB){
                    col[position] = (ColumnDB)hmAux.get(new Integer(i));
                    hmAux.remove(new Integer(i));
                    position++;
                }
            }
            else{
                break;
            }            
        }           
        return col;
    }
    
    /**
     * Elimina una columna de Base de datos, y la da de baja en la tabla
     * <code>columns</code> de GeoPISTA
     * @param table Tabla a la que pertenece la columna que se va a eliminar
     * @param colrow Columna a eliminar
     * @return Verdadero si la operacion se realiza correctamente
     */
    public boolean eliminarColumnaBD (Table table, ColumnRow colrow) throws DataException{    
        boolean bRes = false;
        PreparedStatement s = null;

        try{

    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();
    		
            //ALTER TABLE "nomTable"
            //DROP COLUMN "nomColumn";
            StringBuffer query = new StringBuffer();
            String nomTable = table.getDescription();            
            query.append("ALTER TABLE \"").append(nomTable).append("\" DROP COLUMN \"")
            .append(colrow.getColumnaBD().getName()).append("\";");            
                         
            s = conn.prepareStatement(query.toString());
            if (s.executeUpdate()>=0)
                GeopistaFunctionUtils.generarSQL(s);      
            
            if (colrow.getColumnaSistema().getName()!=null){
                return eliminarColumnaSistema(table, colrow);
            }
            
        }
        catch (SQLException ex){            
            throw new DataException(ex);
        }
        finally{
        	cerrarConexiones(null, null, s, conn, null);
        }
        return bRes;
    }
    
    /**
     * Elimina una columna de sistema
     * @param table Table con la tabla donde se encuentra la columna a eliminar del sistema
     * @param colrow ColumnRow cuya columna de sistema se desea eliminar
     * @return True si se elimina correctamente la columna de sistema
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public boolean eliminarColumnaSistema(Table table, ColumnRow colrow) throws DataException{
    	
        boolean bRes = false;
        PreparedStatement s = null;
        ResultSet r = null;
        try{
            // select id_layer, Position, id_alias from attributes 
            // where id_column=?;
                  
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();
    		
            s = conn.prepareStatement("LMbuscarCapasModificarColumn");
            s.setInt(1, colrow.getColumnaSistema().getIdColumn());
            
            Hashtable ht = new Hashtable();
            ArrayList lstAlias = new ArrayList();
            r = s.executeQuery();  
            while (r.next()){
                ht.put(r.getObject("id_layer"), r.getObject("Position"));
                lstAlias.add(r.getObject("id_alias"));
            }            
            
            //Si la columna se usa como atributo en el esquema de alguna capa
            //se borran las referencias a la misma
            if (!ht.isEmpty() && !lstAlias.isEmpty()){
                if (eliminarTablaFromLayer(table.getIdTabla(),ht)>=0)
                    if(eliminarAliasFromDictionary (lstAlias)>=0)
                        if (eliminarAtributosColumna(colrow.getColumnaSistema().getIdColumn())>=0)
                            if (eliminarColumnTable (colrow.getColumnaSistema().getIdColumn())>=0)                                
                                bRes = true;      
            }
            //Si la columna no se usa como atributo de una capa, se borra directamente
            //la columna de sistema
            else{
                if (eliminarColumnTable (colrow.getColumnaSistema().getIdColumn())>=0)                                
                    bRes=true;      
            }
            bRes = true;
        }
        catch (SQLException ex){            
            throw new DataException(ex);
        }
        finally{
        	cerrarConexiones(r, null, s, conn, null);
        }
        return bRes;
        
    }
    
    /**
     * Elimina las entrada de la tabla <code>attributes</code>
     * @param idColumn Identificador de la columna cuyos atributos se van a eliminar
     * @return int Numero de registros actualizados (-1 en caso de error)
     * @throws DataException Si se produce un error de acceso a datos 
     */
    private int eliminarAtributosColumna(int idColumn) throws DataException{
        int iRes = NO_RESULTS;
        PreparedStatement s = null;
        try{
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();

            s = conn.prepareStatement("LMborrarAtributosColumna");
            s.setInt(1, idColumn);   
            iRes = s.executeUpdate();
            if (iRes>=0)
                GeopistaFunctionUtils.generarSQL (s);
        }
        catch (SQLException ex){
            throw new DataException(ex);
        }
        finally{
        	cerrarConexiones(null, null, s, conn, null);
        }
        return iRes;
        
        
    }
    
    /**
     * Elimina la entrada de la tabla <code>columns</code> para el borrado de una columna
     * asi como la entrada de <code>columns_domains</code> si la columna esta asociada
     * a algun dominio determinado
     * @param idColumn Identificador de la columna de sistema a eliminar
     * @return int Numero de registros actualizados (-1 en caso de error)
     * @throws DataException Si se produce un error de acceso a datos 
     */
    
    private int eliminarColumnTable(int idColumn) throws DataException{        
        int iRes = NO_RESULTS;
        PreparedStatement s = null;
        try{   
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();

            s = conn.prepareStatement("LMborrarColumn");
            s.setInt(1, idColumn); 
            s.setInt(2, idColumn); 
            iRes = s.executeUpdate();
            if (iRes>=0)
                GeopistaFunctionUtils.generarSQL (s);
        }
        catch (SQLException ex){
            throw new DataException(ex);
        }
        finally{
        	cerrarConexiones(null, null, s, conn, null);
        }
        return iRes;        
        
    }
    
    /**
     * Crea una nueva columna en el sistema de Geopista
     * @param table Table que contiene la columna a crear
     * @param colrow ColumnRow cuya columna de sistema es la columna a crear
     * @return int Numero de registros actualizados (-1 en caso de error)
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public int crearColumnaSistema(Table table, ColumnRow colrow) throws DataException{
        int iRes = NO_RESULTS;
        PreparedStatement s = null;
        ResultSet r = null;
        //insert into columns (id,id_table,name,"Length","Precision","Type") values (nextval('seq_columns'),?,?,?,?,?)
        try{
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();

            if (table.getIdTabla()!=0
                    || (table.getIdTabla()==0 && crearTablaSistema (table)>=0)){
                
                s = conn.prepareStatement("LMinsertarColumnaSistema");
                s.setInt(1, table.getIdTabla());
                s.setString(2, colrow.getColumnaBD().getName());
                if (ColumnDB.getGeometryType(colrow.getColumnaBD().getType())==ColumnDB.VARCHAR){
                	s.setInt(3, colrow.getColumnaBD().getLength());
                	s.setNull(4,Types.NUMERIC);
                	s.setNull(5,Types.NUMERIC);
                }
                else{
                	s.setNull(3, Types.NUMERIC);
                	s.setInt(4, colrow.getColumnaBD().getLength());
                    s.setInt(5, colrow.getColumnaBD().getPrecission());
                }
                
                s.setInt(6, ColumnDB.getGeometryType(colrow.getColumnaBD().getType()));
                iRes = s.executeUpdate();                
                if (iRes>=0)
                    GeopistaFunctionUtils.generarSQL(s);
                
                if (colrow.getColumnaSistema().getName()==null ||
                        colrow.getColumnaSistema().getName().trim().equals(""))
                    colrow.getColumnaSistema().setName(colrow.getColumnaBD().getName());
                if (colrow.getColumnaSistema().getDescription()==null ||
                        colrow.getColumnaSistema().getDescription().trim().equals(""))
                    colrow.getColumnaSistema().setDescription(colrow.getColumnaBD().getName());
                
                colrow.getColumnaSistema().setTable(table);
                
                // select currval('seq_columns')
                s = null;
                s = conn.prepareStatement("LMcolumnaSistemaCurrval");
                r = s.executeQuery();
                if (r.next())           
                    colrow.getColumnaSistema().setIdColumn(r.getInt(1));    
            }
        }  
        catch (SQLException ex){           
            throw new DataException(ex);
        }
        finally{
        	cerrarConexiones(r, null, s, conn, null);
        }
        return iRes;
    }
    
    /**
     * Crea una nueva tabla de sistema con el identificador indicado en la secuencia seq_tables
     * 
     * @param table Table con los datos de la tabla a crear
     * 
     * @return int Numero de registros actualizados (-1 en caso de error)
     * 
     * @throws DataException Si se produce un error de acceso a datos 
     */
    private int crearTablaSistema(Table table) throws DataException{
        int iRes = NO_RESULTS;
        PreparedStatement s = null;
        ResultSet r = null;
        //insert into tables (id_table,name,geometrytype) values (nextval('seq_tables'),?,?        
        try{
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();

            s = conn.prepareStatement("LMinsertarTablaSistema");
            s.setString(1, table.getDescription());
            s.setInt(2, table.getGeometryType());
            iRes = s.executeUpdate();
            
            if(iRes>=0)
                GeopistaFunctionUtils.generarSQL(s);
            
            table.setName(table.getDescription());
            // select currval('seq_tables')
            s = null;
            s = conn.prepareStatement("LMtablaSistemaCurrval");
            r = s.executeQuery();
            if (r.next())           
                table.setIdTabla(r.getInt(1));
            
        }  
        catch (SQLException ex){            
            throw new DataException(ex);
        }  
        finally{
        	cerrarConexiones(r, null, s, conn, null);
        }
        return iRes;        
    }
    
    /**
     * Elimina las entradas de la tabla <code>dictionary</code> 
     * 
     * @param lstAlias Lista de identificadores de las entradas a <code>dictionary</code> 
     * que se desean borrar
     * 
     * @return int Numero de registros actualizados (-1 en caso de error)
     * 
     * @throws DataException Si se produce un error de acceso a datos
     */
    private int eliminarAliasFromDictionary(ArrayList lstAlias) throws DataException
    {
        int iRes = NO_RESULTS;
        
        Iterator it = lstAlias.iterator();
        
        while (it.hasNext()){ 
        	PreparedStatement s = null;
            try{      
        		if ((conn == null) || (conn.isClosed()))
        			conn = getDBConnection();

                s = conn.prepareStatement("LMeliminarAlias");
                s.setInt(1, Integer.valueOf(it.next().toString()).intValue());      
                iRes = s.executeUpdate();
                if (iRes>=0)
                    GeopistaFunctionUtils.generarSQL (s);            
            }
            catch (SQLException ex){
                throw new DataException(ex);
            }
            finally{
            	cerrarConexiones(null, null, s, conn, null);
            }
        }
        return iRes;
        
    }
    
    /**
     * Recupera la lista de columnas not null de una tabla
     * 
     * @throws DataException Si se produce un error de acceso a datos
     */
    public List obtenerColumnasNotNullTabla(String nombreTabla, List lstColumnas) throws DataException{   
 //       List lstColumnas = new ArrayList();

        PreparedStatement s = null;
        ResultSet r = null;
        
        try{
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();

            //String query = "SELECT attname FROM pg_class, pg_attribute where pg_attribute.attrelid=pg_class.oid and attnotnull and attstattarget=-1 and relname=?;";
            s = conn.prepareStatement("LMobtenerColumnasNotNullTabla");
            s.setString(1, nombreTabla);
            r = s.executeQuery();
            
            if (r.next()){
            	TableRow tableRow = new TableRow();
                tableRow.setColumna(new Column());
                tableRow.getColumna().setTable(new Table());
                tableRow.getColumna().getTable().setName(nombreTabla);
            	tableRow.getColumna().setName(r.getString("attname"));
            	lstColumnas.add(tableRow);
            	while (r.next()){
            		tableRow = new TableRow();
                    tableRow.setColumna(new Column());
                    tableRow.getColumna().setTable(new Table());
                    tableRow.getColumna().getTable().setName(nombreTabla);
            		tableRow.getColumna().setName(r.getString("attname"));
            		lstColumnas.add(tableRow);          
            	}
            }
            else
            	if (lstColumnas.size()>0)
            		return lstColumnas;
            	else
            		return null;
            
        }        
        catch (SQLException ex){
            throw new DataException(ex);
        }
        finally{
        	cerrarConexiones(r, null, s, conn, null);
        }
        return lstColumnas;
    }

	private static boolean cerrarConexiones(ResultSet rs, Statement statement, PreparedStatement preparedStatement, Connection connection, Connection directConnection) {
		
		try{
			if (rs!=null)
				rs.close();
		}
		catch (Exception e) {
			logger.error("No se puede cerrar el ResultSet", e);
		}

		try{
			if (statement!=null)
				statement.close();
		}
		catch (Exception e) {
			logger.error("No se puede cerrar el Statement", e);
		}

		try{
			if (preparedStatement!=null)
				preparedStatement.close();
		}
		catch (Exception e) {
			logger.error("No se puede cerrar el PreparedStatement", e);
		}
		
		try{
			if (connection!=null)
				connection.close();                     
		}
		catch (SQLException e) {               
			logger.error("No se puede cerrar la Conexion",e);
		}
		
		try{
			if (directConnection!=null)
				directConnection.close();                     
		}
		catch (SQLException e) {               
			logger.error("No se puede cerrar la Conexion Directa",e);
		}		

		return true;
	}
    
	
    public void crearScriptInsert(FilterOutputStream salidaSql, String nombreTabla, int idEntidad) throws SQLException {
    	ResultSet r = null;
    	PreparedStatement s = null;
		try {
			StringBuffer columnas = new StringBuffer();
			HashMap columnsDB = obtenerListaColumnasBD(nombreTabla);
			if (columnsDB != null) {
				Iterator it = columnsDB.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry e = (Map.Entry) it.next();
					if (!((ColumnDB) e.getValue()).getName().equals("GEOMETRY")){
						columnas.append("\""+((ColumnDB)e.getValue()).getName()+"\"");
					}
					else
						columnas.append("'GeometryFromText(\\'' || astext(\"GEOMETRY\") || '\\', ' || getsrid(\"GEOMETRY\")::text || ')' as \"GEOMETRY\"");
					if (it.hasNext())
						columnas.append(", ");
				}
		    	String sql = "SELECT "+columnas+" FROM \""+nombreTabla+"\" where id_municipio IN (select id_municipio from entidades_municipios where id_entidad='"+idEntidad+"');";
		
				if ((conn == null) || (conn.isClosed()))
					conn = getDBConnection();
				
		        s = conn.prepareStatement(sql);
		     
	        	r = s.executeQuery();
	
		        ResultSetMetaData resultSetMetaData = r.getMetaData();
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
		       
		        while (r.next() ) {
		            StringBuffer insert = new StringBuffer("insert into \""+nombreTabla+"\" (");
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
		                if (nombreColumnas[i].equals("GEOMETRY")){
		                	insert.append(r.getObject(i+1));
		                }
		                else{
		                	if (nombreColumnas[i].equals("id")){
		                		insert.append("nextval('seq_"+nombreTabla.toLowerCase()+"')");
		                	}
		                	else{
				                if ((tipoColumnas[i]==Types.CHAR)||(tipoColumnas[i]==Types.VARCHAR)){                
				                    insert.append(valorColumna(r.getObject(i+1), tipoColumnas[i]).replace("\\", "\\\\"));
				                }
				                else{
				                	insert.append(valorColumna(r.getObject(i+1), tipoColumnas[i]));
				                }
		                	}
		                }
		            }
		            insert.append(");"); 
		            
		    
					writeFile(salidaSql ,insert.toString());
				}
	        }
        } catch (DataException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
        } catch (SQLException e) {
        	logger.error(e.toString());
            throw e;
        }
	    finally{
	    	cerrarConexiones(r, null, s, conn, null);
	    }
    }
    
    private String valorColumna(Object valor, int tipo) {

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
    
    public static void writeFile(FilterOutputStream out, String cadena){
    	try{
    		if (cadena==null || out==null) return;
    		out.write(cadena.getBytes());
    		
    		out.write('\n');
    		
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
    
    public void crearScriptInsertExportImport(FileOutputStream salidaInsert, String nombreTabla, int idEntidad) throws SQLException {
    	ResultSet r = null;
    	PreparedStatement s = null;
		try {
			StringBuffer columnas = new StringBuffer();
			HashMap columnsDB = obtenerListaColumnasBD(nombreTabla);
			if (columnsDB != null) {
				Iterator it = columnsDB.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry e = (Map.Entry) it.next();
					if (!((ColumnDB) e.getValue()).getName().equals("GEOMETRY")){
						columnas.append("\""+((ColumnDB)e.getValue()).getName()+"\"");
					}
					else
						columnas.append("'GeometryFromText(\\'' || astext(\"GEOMETRY\") || '\\', ' || getsrid(\"GEOMETRY\")::text || ')' as \"GEOMETRY\"");
					if (it.hasNext())
						columnas.append(", ");
				}
		    	String sql = "SELECT "+columnas+" FROM \""+nombreTabla+"\" where id_municipio IN (select id_municipio from entidades_municipios where id_entidad='"+idEntidad+"');";
		
				if ((conn == null) || (conn.isClosed()))
					conn = getDBConnection();
				
		        s = conn.prepareStatement(sql);
		     
	        	r = s.executeQuery();
	
		        ResultSetMetaData resultSetMetaData = r.getMetaData();
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
		       
		        while (r.next() ) {
		            StringBuffer insert = new StringBuffer("insert into \""+nombreTabla+"\" (");
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
		                if (nombreColumnas[i].equals("GEOMETRY")){
		                	insert.append(r.getObject(i+1));
		                }
		                else{
		                	if (nombreColumnas[i].equals("id")){
		                		insert.append("nextval('seq_"+nombreTabla.toLowerCase()+"')");
		                	}
		                	else{
				                if ((tipoColumnas[i]==Types.CHAR)||(tipoColumnas[i]==Types.VARCHAR)){                
				                    insert.append(valorColumna(r.getObject(i+1), tipoColumnas[i]).replace("\\", "\\\\"));
				                }
				                else{
				                	insert.append(valorColumna(r.getObject(i+1), tipoColumnas[i]));
				                }
		                	}
		                }
		            }
		            insert.append(");"); 
		            
		    
		            writeFileExportImport(salidaInsert ,insert.toString());
				}
	        }
        } catch (DataException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
        } catch (SQLException e) {
        	logger.error(e.toString());
            throw e;
        }
	    finally{
	    	cerrarConexiones(r, null, s, conn, null);
	    }
    }
    
    public boolean cargarSQL(String query) throws DataException{       
        boolean bRes = false;
        PreparedStatement s = null;
        try{             
       	
    		if ((conn == null) || (conn.isClosed()))
    			conn = getDBConnection();
    		   		
            if (!query.equals("")){
                    s = conn.prepareStatement(query.toString());
                    if (s.executeUpdate()>=0)
                    	LCGIII_GeopistaUtil.generarSQL(s);             
            }            
            bRes = true;            
        }
        catch (SQLException ex){
            throw new DataException(ex);
        }
        finally{
        	cerrarConexiones(null, null, s, conn, null);
        } 
        
        return bRes;
    }    
    
    public static void writeFileExportImport(FileOutputStream salidaInsert, String cadena){
    	try{
    		if (cadena==null || salidaInsert==null) return;
    		salidaInsert.write(cadena.getBytes());
    		
    		salidaInsert.write('\n');
    		
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
}


