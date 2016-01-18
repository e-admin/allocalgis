/**
 * LayerOperations.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.layer;


/**
 * Definicion de metodos para la realizacion de operaciones sobre base de 
 * datos
 * 
 * @author cotesa
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.geopista.app.AppConstants;
import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.layerutil.dbtable.ColumnDB;
import com.geopista.app.layerutil.dbtable.LoginDBDialog;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.app.layerutil.layer.exportimport.beans.PostgreSQLConstraint;
import com.geopista.app.layerutil.layerfamily.LayerFamilyTable;
import com.geopista.app.layerutil.schema.column.ColumnRow;
import com.geopista.feature.Attribute;
import com.geopista.feature.AutoFieldDomain;
import com.geopista.feature.BooleanDomain;
import com.geopista.feature.CodeBookDomain;
import com.geopista.feature.CodedEntryDomain;
import com.geopista.feature.Column;
import com.geopista.feature.DateDomain;
import com.geopista.feature.Domain;
import com.geopista.feature.NumberDomain;
import com.geopista.feature.StringDomain;
import com.geopista.feature.Table;
import com.geopista.feature.TreeDomain;
import com.geopista.model.LayerFamily;
import com.geopista.protocol.administrador.Acl;
import com.geopista.ui.wms.Style;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

public class LayerOperations
{
	Logger logger = Logger.getLogger(LayerOperations.class);

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
    /**
     * Blackboard para intercambio de variables entre distintas zonas de la aplicacion
     */
    private Blackboard Identificadores = aplicacion.getBlackboard();
    /**
     * Locale que identifica el idioma del usuario
     */
    private String locale = UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, AppConstants.DEFAULT_LOCALE, false);
    public static Set familiasModificadas = new HashSet(); 
    //private static final String pass = UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS,"",false);
    
    private static final int NO_RESULTS=-1;
    
    private static final int UTM_30N_ETRS89=25830;
    
    /**
     * Constructor por defecto
     *
     */
    public LayerOperations()
    {        
        try
        {
            conn = getDBConnection();
        }
        catch(Exception e)
        { 
            e.printStackTrace();
        }        
    }
    
    public static void directConnect()
    {
        try
        {
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
    private static Connection getDBConnection () throws SQLException
    {        
        Connection con=  aplicacion.getConnection();
        con.setAutoCommit(false);
        return con;
    }  
    
    
    /**
     * Obtiene una conexion directa a la base de datos, sin utilizar el administrador de 
     * cartografia
     * @return Conexion a la base de datos
     * @throws SQLException 
     */
    
    private static Connection getDirectConnection () throws SQLException
    {   
        if (directConn==null
                && aplicacion.getBlackboard().get("DirectConnection")==null)
        {    
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
        else if (aplicacion.getBlackboard().get("DirectConnection")!=null)
        {
            directConn = (Connection)aplicacion.getBlackboard().get("DirectConnection");  
            if (directConn.isClosed())
                directConn = aplicacion.getJDBCConnection(aplicacion.getBlackboard().get("USER_BD").toString(),
                        aplicacion.getBlackboard().get("PASS_BD").toString());
        }            
        else if (directConn.isClosed() && aplicacion.getBlackboard().get("USER_BD")!=null
                && aplicacion.getBlackboard().get("PASS_BD") !=null)
        {
            directConn = aplicacion.getJDBCConnection(aplicacion.getBlackboard().get("USER_BD").toString(),
                    aplicacion.getBlackboard().get("PASS_BD").toString());
        }
        return directConn;
    }  
    
    
    /**
     * Busca el objeto Column cuyo identificador se pasa como parametro
     * @param idColumna Valor del atributo idColumn de la Column que se busca
     * @return
     */
    public Column buscarColumna(int idColumna){
        
        Column columna = new Column();
        
        List lstColumnas = (List)Identificadores.get("Columnas");
        
        for (int i =1 ; i< lstColumnas.size(); i++) {
            
            
            Column col = (Column)lstColumnas.get(i);
            if (col.getIdColumn() == idColumna)
                return col;
            
        }
        
        return columna;
        
    }
    
    /**
     * Obtiene un objeto tipo Dominio, de acuerdo al tipo especificado en
     * la base de datos
     * @param idDominio Identificador del dominio
     * @param nombreDominio Nombre del dominio
     * @return Domain tipo
     * @throws DataException Si se produce un error de acceso a datos 
     */ 
    
    public Domain obtenerDominioTipo(int idDominio, String nombreDominio) throws DataException{
        
        Domain dRet = null;
        int idTipo = 0;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {
            
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMobtenertipodominionull");
            s.setInt(1, idDominio);
            
            
            r = s.executeQuery();  
            if (r.next())
            {           
                idTipo =r.getInt("type");
                
            }
                       
            dRet = obtenerDominioTipo(idDominio, idTipo, nombreDominio);
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
                try{r.close();}catch(Exception e){};
                try{s.close();}catch(Exception e){};
                try{conn.close();}catch(Exception e){};
        }
        
        return dRet;    
        
    }
    
    /**
     * Obtiene un objeto tipo Dominio, de acuerdo al tipo especificado en
     * la base de datos
     * @param idDominio Identificador del dominio
     * @param idTipo Tipo del dominio
     * @param nombreDominio Nombre del dominio
     * @return Domain tipo
     */
    
    public Domain obtenerDominioTipo(int idDominio, int idTipo, String nombreDominio){
        
        Domain dRet=null;
        
        switch (idTipo){
        
        case Domain.CODEBOOK:
            dRet= new CodeBookDomain();               
            break;
        case Domain.PATTERN:
            dRet= new StringDomain();        
            break;
        case Domain.DATE:
            dRet= new DateDomain();           
            break;
        case Domain.NUMBER:
            dRet= new NumberDomain();        
            break;
        case Domain.TREE:
            dRet=new TreeDomain();          
            break;
        case Domain.BOOLEAN:
            dRet=new BooleanDomain();       
            break;
        case Domain.CODEDENTRY:
            dRet=new CodedEntryDomain();         
            break;
        case Domain.AUTO:
            dRet=new AutoFieldDomain();            
            break;
            
        }
        
        if (dRet != null){
            dRet.setSystemID(idDominio);
            dRet.setName(nombreDominio);
        }
        
        return dRet;        
        
    }
    
    
    
    /**
     * Recupera la lista de todas las tablas dadas de alta en el sistema
     * 
     * @return Lista de las tablas del sistema
     * @throws DataException Si se produce un error de acceso a datos 
     */    
    public List obtenerListaTablas() throws DataException{
        
        List lstTablas = null;        
        PreparedStatement s = null;
        ResultSet r = null;
        ArrayList ar=new ArrayList();
        
        Comparator tablesComparator = new Comparator(){
            public int compare(Object o1, Object o2) {
            	Table c1 = (Table) o1;
            	Table c2 = (Table) o2;
                
                String desc1 = c1.getName();
                String desc2 = c2.getName();
                
                return desc1.compareTo(desc2);
                
                //Collator myCollator=Collator.getInstance(new Locale("es_ES"));
                //myCollator.setStrength(Collator.PRIMARY);
                //return myCollator.compare(desc1, desc2);
                
                //return desc1.compareToIgnoreCase(desc2);
            }
        };
        try
        {
            lstTablas = new ArrayList();
            //String query = "select * from tables";
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMobtenertablas");
            
            r = s.executeQuery();  
            while (r.next())
            {     
                Table tab = new Table();
                tab.setName(r.getString("name"));
                tab.setIdTabla(r.getInt("id_table"));
                tab.setExternal(r.getInt("external"));
                lstTablas.add(tab);                
            }
                         
            
            Table[] listaTablas = (Table[])lstTablas.toArray((new Table[lstTablas.size()]));
            Arrays.sort(listaTablas, tablesComparator);
          
            for (int i=0;i<listaTablas.length;i++){
            	ar.add(listaTablas[i]);
            }
            
        }        
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
            try{r.close();}catch(Exception e){};
            try{s.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
        }
        
        return ar;
        
    }
    
    
    /**
     * Recupera una lista de todas las columnas dadas de alta en el sistema
     * 
     * @return Lista de columnas
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public List obtenerListaColumnas() throws DataException{
        
        return obtenerListaColumnas (null);
        
    }
    
    /**
     * Recupera una lista con todas las columnas que estan dadas de alta
     * en el sistema para la tabla que se indica como parametro
     * 
     * @param tabla Objeto Table al que pertenecen las columnas
     * 
     * @return Lista de Columnas
     * 
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public List obtenerListaColumnas(Table tabla) throws DataException{
        
        List lstColumnas = null;
        
        String query = "";
        int idColumna = 0;
        int idTabla = 0;
        String idDominio = null;
        
        if (tabla !=null) idTabla = tabla.getIdTabla();
        
        PreparedStatement s = null;
        ResultSet r = null;
        
        try
        {     
            lstColumnas = new ArrayList();
            
            if (conn == null){
            	conn = getDBConnection();
            }
            
            if (idTabla ==0){
                query="LMobtenercolumnastotal";
                s = conn.prepareStatement(query);
            }
            else{
                query="LMobtenercolumnastabla";
                s = conn.prepareStatement(query);
                s.setInt(1, idTabla);                 
            }            
            
            r = s.executeQuery();  
            while (r.next())
            {
                Column col = new Column();            
                Attribute att = new Attribute();
                Hashtable ht = new Hashtable();
                
                col.setName(r.getString("name"));
                col.setlongitud(r.getInt("Length"));
                att.setType(r.getString("Type"));
                att.setColumn(col);  
                ht.put(locale, "");
                att.setHtTraducciones(ht);
                
                col.setLevel(r.getInt("level_columna"));
                idDominio = r.getString("id_dominio");
                if (idDominio!= null)
                {
                    Domain dom = obtenerDominioTipo(r.getInt("id_dominio"), r.getInt("tipo"), r.getString("nombre_dominio"));
                    if (dom!=null)
                        dom.setPattern(r.getString("pattern"));
                    col.setDomain(dom);
                }  
                
                if (tabla!=null)
                {
                    col.setTable(tabla);
                }
                else
                {
                    Table auxTab = new Table();
                    auxTab.setIdTabla(r.getInt("id_table"));
                    col.setTable(auxTab);
                }
                col.setAttribute(att);
                idColumna= r.getInt("id");
                col.setIdColumn(idColumna);
                
                lstColumnas.add(col);
                
            }
                        
        }
        
        catch (SQLException ex)
        {
            throw new DataException(ex);            
        }finally{
            try{r.close();}catch(Exception e){};
            try{s.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
        }
        
        
        return lstColumnas;
    }
    
    
    
 
    /**
     * Elimina la asociacion entre una columna y un dominio
     * 
     * @param columna Column a actualizar
     * @return Numero de registros modificados (-1 en caso de error)
     * @throws DataException Si se produce un error de acceso a datos 
     * 
     */
    public int desasociarColumna (Column columna) throws DataException
    {   
        int iRes = NO_RESULTS;
        
        PreparedStatement s = null;
        try
        {
            if (columna.getDomain()==null)
                return 0;
            
            int idColumna = columna.getIdColumn();              
            int idDomain = columna.getDomain().getSystemID();
            
            
            if (conn == null){
            	conn = getDBConnection();
            }
            //delete from columns_domains where id_column=? and id_domain=?; update columns set id_domain=null where id = ? 
            s = conn.prepareStatement("LMdesasociardominiocolumna");
            s.setInt(1, idColumna);
            s.setInt(2, idDomain);
            s.setInt(3, idColumna);
            iRes = s.executeUpdate();  
            //conn.commit();     
            
            if(iRes>=0)
                GeopistaFunctionUtils.generarSQL (s);
            
           
            columna.setDomain(null);           
            
            
        }        
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
            try{s.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
        }
        return iRes;  
    }
    
    
    /**
     * Obtiene el valor numerico del tipo de dominio para el Domain que se le pasa como parametro
     *  
     * @param dominio Domain cuyo tipo se desea conocer
     * @param idMunicipio Identificador del municipio
     * @return Valor numerico del tipo de dominio o 0 si no se encuentra correspondencia
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public int obtenerTipoDominio (com.geopista.protocol.administrador.dominios.Domain dominio, int idMunicipio)
    throws DataException
    {
        int tipo = 0;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {    
            
            int idDominio = Integer.parseInt(dominio.getIdDomain());
            if (conn == null){
            	conn = getDBConnection();
            }
            
            if (idMunicipio==0)
            {
                s = conn.prepareStatement("LMobtenertipodominionull");
                s.setInt(1, idDominio);
            }
            else
            {
                s = conn.prepareStatement("LMobtenertipodominio");
                s.setInt(1, idDominio);
                s.setInt(2, idMunicipio);
            }
            
            r = s.executeQuery();  
            if (r.next())
            {           
                tipo=  r.getInt("type");
                
            }
            
        }
        
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
            try{r.close();}catch(Exception e){};
            try{s.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
        }       
        
        return tipo;
    }
    
    
    /**
     * Obtiene un array de LayerTable con todas las capas dadas de alta en el sistema
     * @param obtenerCapasNoModificables true si se desea devolver todas las capas dadas de alta, incluidas
     * las que no son modificables 
     * @return Array de LayerTable
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public LayerTable[] obtenerLayerTable (boolean obtenerCapasNoModificables) throws DataException
    {
        LayerTable[] capasArray;
        PreparedStatement s = null;
        ResultSet r = null;        
        try
        {  
            //String query = "select layers.*,dictionary.locale, dictionary.traduccion from layers left join dictionary on layers.id_name = dictionary.id_vocablo order by layers.id_layer"
            if (conn == null){
            	conn = getDBConnection();
            }
            
            s = conn.prepareStatement("LMobtenerlayertables");
            s.setInt(1, obtenerCapasNoModificables?0:1);
            
            r = s.executeQuery();
            
            
            Hashtable htNombres = new Hashtable();
            
            r.last();
            int numFilas = r.getRow();
            r.first();
            
            Map<Integer, LayerTable> map  = new HashMap<Integer, LayerTable>();
            for (int i =0; i< numFilas; i++)  
            {  
                boolean variosLocales = false;
                int idLayer = r.getInt("id_layer");
                LayerTable layerTab = new LayerTable();
                Layer layer = new Layer ();
                LayerManager lm = new LayerManager();
                //mientras se crea la capa, no existe categoria (layerfamily). 
                //Para evitar AssertionFailedException, se pone firingEvents a false
                lm.setFiringEvents(false);                    
                layer.setLayerManager(lm);            
                
                layer.setName(r.getString("id_name"));
                layer.setDescription(r.getString("name"));
                layer.setEditable(r.getInt("modificable")==1);
                layer.setVersionable(r.getInt("versionable")==1);
                layer.setValidator(r.getString("validator"));
                
                //Miro si la capa es dinamica
                //TODO. Esta operacion es muy lenta para cada una de las capas.
                //Para optimizar habria que pedirlas todas juntas. Ademas
                //la mayoria no son dinamicas por lo que vamos a pedir solo las
                //que no son dinamicas y actualiiar.
                //isDynamicLayer(layer, idLayer, AppContext.getIdEntidad());
                
                layerTab.setLayer(layer);
                layerTab.setAcl(new Acl(r.getString("idacl"), r.getString("aclname")));
                layerTab.setIdEntidadLayer(r.getString("id_entidad"));
                
                try {
					while (r.getInt("id_layer") == idLayer && i<numFilas)
					{
						if (r.getString("locale") != null)
							htNombres.put(r.getString("locale"), r.getString("traduccion"));
					    if (i<numFilas-1) 
					        r.next();
					    i++;
					    variosLocales = true;
					}
				} catch (Exception e) {
					 throw new DataException(e); 
				}
                
                
                layerTab.setHtNombre(htNombres);
                layerTab.setIdLayer(idLayer);
                map.put(new Integer(idLayer), layerTab);
                htNombres = new Hashtable();
                
                if (!variosLocales)
                    r.next();
                else
                    i--;

            }
            updateDynamicLayers((Map<Integer, LayerTable>) map,AppContext.getIdEntidad());           
            
            Comparator layerComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    LayerTable c1 = (LayerTable) o1;
                    LayerTable c2 = (LayerTable) o2;
                    
                    String desc1 = c1.getLayer().getDescription();
                    String desc2 = c2.getLayer().getDescription();
                    
                    if (c1.getHtNombre().get(locale)!=null)
                        desc1 = c1.getHtNombre().get(locale).toString();
                    else if (c1.getHtNombre().get(AppConstants.DEFAULT_LOCALE)!=null)
                        desc1 = c1.getHtNombre().get(AppConstants.DEFAULT_LOCALE).toString();
                    
                    if (c2.getHtNombre().get(locale)!=null)
                        desc2 = c2.getHtNombre().get(locale).toString();
                    else if (c2.getHtNombre().get(AppConstants.DEFAULT_LOCALE)!=null)
                        desc2 = c2.getHtNombre().get(AppConstants.DEFAULT_LOCALE).toString();                    
                    
                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);
                    
                    //return desc1.compareToIgnoreCase(desc2);
                }
            };
            
            capasArray = (LayerTable[])map.values().toArray(new LayerTable[map.values().size()]);
            
            Arrays.sort(capasArray, layerComparator);
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);            
        }
        
        finally{
            try{r.close();}catch(Exception e){};
            try{s.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
        }   
        
        return capasArray;
        
    }    
    
    
    /**
     * Obtiene un array de LayerTable con todas las capas dadas de alta en el sistema
     * @param obtenerCapasNoModificables true si se desea devolver todas las capas dadas de alta, incluidas
     * las que no son modificables 
     * @return Array de LayerTable
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public LayerTable[] obtenerLayerTable (boolean obtenerCapasNoModificables, int idEntidad) throws DataException
    {
        LayerTable[] capasArray;
        PreparedStatement s = null;
        ResultSet r = null;
        
        try
        {  
            //String query = "select layers.*,dictionary.locale, dictionary.traduccion from layers left join dictionary on layers.id_name = dictionary.id_vocablo order by layers.id_layer"
            if (conn == null){
            	conn = getDBConnection();
            }           
            s = conn.prepareStatement("LMobtenerlayertablesEntidad");
            s.setInt(1, obtenerCapasNoModificables?0:1);
            s.setInt(2, idEntidad);
            
            r = s.executeQuery();
            
            
            Hashtable htNombres = new Hashtable();
            
            r.last();
            int numFilas = r.getRow();
            r.first();
            
            Map<Integer, LayerTable> map  = new HashMap<Integer, LayerTable>();
            for (int i =0; i< numFilas; i++)  
            {  
                boolean variosLocales = false;
                int idLayer = r.getInt("id_layer");
                LayerTable layerTab = new LayerTable();
                Layer layer = new Layer ();
                LayerManager lm = new LayerManager();
                //mientras se crea la capa, no existe categoria (layerfamily). 
                //Para evitar AssertionFailedException, se pone firingEvents a false
                lm.setFiringEvents(false);                    
                layer.setLayerManager(lm);            
                
                layer.setName(r.getString("id_name"));
                layer.setDescription(r.getString("name"));
                
                //Miro si la capa es dinamica
                //Cambiamos este metodo porque es muy lento en cada pasada
                //isDynamicLayer(layer, idLayer, idEntidad);
                
                layer.setEditable(r.getInt("modificable")==1);
                layer.setVersionable(r.getInt("versionable")==1);
                layer.setValidator(r.getString("validator"));
                layerTab.setLayer(layer);
                layerTab.setAcl(new Acl(r.getString("idacl"), r.getString("aclname")));
                layerTab.setIdEntidadLayer(r.getString("id_entidad"));
                
                while (r.getInt("id_layer") == idLayer && i<numFilas)
                {
                	if (r.getString("locale") != null)
                		htNombres.put(r.getString("locale"), r.getString("traduccion"));
                    if (i<numFilas-1) 
                        r.next();
                    i++;
                    variosLocales = true;
                }
                
                
                layerTab.setHtNombre(htNombres);
                layerTab.setIdLayer(idLayer);
                map.put(new Integer(idLayer), layerTab);
                htNombres = new Hashtable();
                
                if (!variosLocales)
                    r.next();
                else
                    i--;
            }
            
            updateDynamicLayers((Map<Integer, LayerTable>) map,AppContext.getIdEntidad());        
                  
            Comparator layerComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    LayerTable c1 = (LayerTable) o1;
                    LayerTable c2 = (LayerTable) o2;
                    
                    String desc1 = c1.getLayer().getDescription();
                    String desc2 = c2.getLayer().getDescription();
                    
                    if (c1.getHtNombre().get(locale)!=null)
                        desc1 = c1.getHtNombre().get(locale).toString();
                    else if (c1.getHtNombre().get(AppConstants.DEFAULT_LOCALE)!=null)
                        desc1 = c1.getHtNombre().get(AppConstants.DEFAULT_LOCALE).toString();
                    
                    if (c2.getHtNombre().get(locale)!=null)
                        desc2 = c2.getHtNombre().get(locale).toString();
                    else if (c2.getHtNombre().get(AppConstants.DEFAULT_LOCALE)!=null)
                        desc2 = c2.getHtNombre().get(AppConstants.DEFAULT_LOCALE).toString();                    
                    
                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);
                    
                    //return desc1.compareToIgnoreCase(desc2);
                }
            };
            
            capasArray = (LayerTable[])map.values().toArray(new LayerTable[map.values().size()]);
            
            Arrays.sort(capasArray, layerComparator);
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);            
        }finally{
            try{r.close();}catch(Exception e){};
            try{s.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
        } 
        
        return capasArray;
        
    }    

    /**
     * Obtiene un array de LayerFamily con todas las categorias o familias dadas de alta en el sistema
     * 
     * @return Array de LayerFamily
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public LayerFamily[] obtenerLayerFamilies () throws DataException
    {        
        LayerFamily[] fams;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        { 
            Map map = new HashMap();
            
            //String query = "select layerfamilies.*, dictionary.traduccion, dictionary.locale from layerfamilies, dictionary where layerfamilies.id_name = dictionary.id_vocablo and (locale =? or locale ="es_ES") order by traduccion";
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMobtenerlayerfamilies");
            s.setString(1,locale);
            
            r = s.executeQuery();  
            while (r.next())
            {
                LayerFamily familia = new LayerFamily();
                String idLayerFamily = r.getString("id_layerfamily");
                familia.setSystemId(idLayerFamily);
                familia.setName(r.getString("id_name"));
                familia.setDescription(r.getString("traduccion"));
                
                if (map.containsKey(idLayerFamily))
                {
                    if (r.getString("locale").equals(AppConstants.DEFAULT_LOCALE))
                        map.remove(idLayerFamily);
                    
                }
                
                map.put(idLayerFamily, familia);
                
                
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            
            Comparator layerFamiliesComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    LayerFamily lf1 = (LayerFamily) o1;
                    LayerFamily lf2 = (LayerFamily) o2;
                    
                    String desc1 = lf1.getDescription();
                    String desc2 = lf2.getDescription();
                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);
                    
                    //return desc1.compareToIgnoreCase(desc2);
                }
            };
            
            Collection familias = map.values();
            fams = new LayerFamily[familias.size()];
            int i=0;
            
            for (Iterator it = familias.iterator();it.hasNext(); ){
                fams[i] = (LayerFamily)it.next();
                i++;
                
            }
            //LayerFamily[] fams = (LayerFamily[])map.values().toArray();
            Arrays.sort(fams, layerFamiliesComparator);
            
            
            
        }
        catch (SQLException ex)
        {           
            throw new DataException(ex);
        }finally{
            try{r.close();}catch(Exception e){};
            try{s.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
        } 
        return fams;
    }
    
    /**
     * Obtiene la traduccion a todos los locales del sistema del nombre de la 
     * capa que se pasa por parametro
     * 
     * @param capa Layer cuyo nombre se desea conocer
     * @return Hashtable con los nombres de la capa traducidos en todos los locales disponibles
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public Hashtable buscarTraduccionNombres (Layer capa) throws DataException
    {        
        return obtenerTraduccion (capa);
        
    }
    
    /**
     * Obtiene la traduccion a todos los locales del sistema del 
     * atributo que se pasa por parametro
     * 
     * @param att Attribute cuyo nombre se desea conocer
     * @return Hashtable con los nombres del atributo traducidos en todos los locales disponibles
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public Hashtable buscarTraduccionAtributos (Attribute att) throws DataException
    {
        return obtenerTraduccion (att);           
    }
    
    /**
     * Obtiene la traduccion a todos los locales del sistema del nombre  
     * del objeto que se pasa por parametro
     * 
     * @param objeto Objeto cuyo nombre se desea conocer (Layer o Attribute)
     * @return Hashtable con los nombres del objeto traducidos en todos los locales disponibles
     * @throws DataException Si se produce un error de acceso a datos 
     */
    private Hashtable obtenerTraduccion (Object objeto) throws DataException
    {
        Hashtable ht = null;
        int idVocablo=0;
        
        PreparedStatement s = null;
        ResultSet r = null;
        if (objeto instanceof Attribute)
        {
            idVocablo = ((Attribute)objeto).getIdAlias();
        }
        else if (objeto instanceof Layer && objeto != null)
        {        
            idVocablo = Integer.parseInt(((Layer)objeto).getName());
        }
        
        
        try
        {      
            ht = new Hashtable();
            //String query = "select locale,traduccion from dictionary where id_vocablo=?";
            
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMtraducciones");
            s.setInt(1, idVocablo);
            
            r = s.executeQuery();  
            while (r.next())
            {
                ht.put(r.getString(1), r.getString(2));       
            }                    
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
            try{r.close();}catch(Exception e){};
            try{s.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
        } 
        
        return ht;
        
    }
    
    /**
     * Recupera todos los atributos incluidas las traducciones de los mismos
     * para una capa determinada
     *  
     * @param idLayer Identificador numerico de la capa 
     * @return Vector con los atributos (Attribute)
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public Vector buscarColumnasAtributos (int idLayer) throws DataException
    {
        
        Vector vec = new Vector();
        PreparedStatement s = null;
        ResultSet r = null;
        
        try
        {            
            //String query = "select * from attributes where id_layer=?";
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMbuscaratributos");
            s.setInt(1, idLayer);
            
            r = s.executeQuery();  
            while (r.next())
            {
                Attribute atributo = new Attribute();
                atributo.setSystemID(r.getInt("id"));
                atributo.setIdAlias(r.getInt("id_alias"));                
                atributo.setIdLayer(idLayer);
                atributo.setEditable(r.getBoolean("editable"));
                atributo.setPosition(r.getInt("position"));
                Column col = buscarColumna(r.getInt("id_column"));
                if (col!=null)
                    atributo.setColumn(col);
                
                //para buscar la traduccion de los atributos, basta con el id_alias
                atributo.setHtTraducciones(buscarTraduccionAtributos(atributo));                 
                vec.add(atributo);           
                
            }        
 
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);     
        }finally{
            try{r.close();}catch(Exception e){};
            try{s.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
        } 
        
        return vec;
    }
    
    /**
     * Recupera las consultas SQL para una capa determinada
     * 
     * @param idLayer Identificador numerico de la capa 
     * 
     * @return Hashtable con las consultas SQL (objetos Query) cuya key es el identificador del
     * tipo de Base de Datos.
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public Hashtable buscarQueries (int idLayer)throws DataException
    {        
        Hashtable ht = new Hashtable();
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {            
            //String query = "select * from queries where id_layer=? order by databasetype";
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMbuscarqueries");
            s.setInt(1, idLayer);
            
            r = s.executeQuery();  
            while (r.next())
            {
                Query query = new Query();
                query.setSelectQuery (r.getString("selectquery"));
                query.setUpdateQuery (r.getString("updatequery"));
                query.setInsertQuery (r.getString("insertquery"));
                query.setDeleteQuery (r.getString("deletequery"));
                query.setTipoDatabase(r.getInt("databasetype"));
                query.setIdQuery (r.getInt("id"));  
                query.setIdLayer(idLayer);
                
                ht.put(new Integer(r.getInt("databasetype")), query);
                
            }        
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
            try{r.close();}catch(Exception e){};
            try{s.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
        } 
        
        return ht;
    }
    
    /**
     * Actualiza la tabla de traducciones del sistema (inserciones, eliminaciones y 
     * actualizaciones) para una entrada determinada
     * @param htTraducciones Tabla de traducciones, cuya clave son los distintos locales. Si es null
     * se tratara de un borrado
     * @param idVocablo Identificador de la entrada
     * @return Identificador del vocablo actualizado, o 0 en caso de error
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public int actualizarDictionary (Hashtable htTraducciones, int idVocablo) throws DataException
    {
    	PreparedStatement s = null;
        ResultSet r = null;
        
        try
        {            
            //update dictionary set traduccion=? where id_vocablo=? and locale=?
            //insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),?,?)
            //insert into dictionary (id_vocablo,locale, traduccion) values (?,?,?)
            //select id_vocablo from dictionary where locale=? and traduccion=?
            //delete from dictionary where id_vocablo=?
            
            if (conn == null){
            	conn = getDBConnection();
            }
            
            //INSERTAR O ACTUALIZAR
            if (htTraducciones!=null)
            {                
                //Se trata de una insercion. 
                //Hay que asegurarse de que todas las traducciones tienen el mismo id_vocablo para los distintos locale
                if(idVocablo ==0)
                {   
                    for (Enumeration e=htTraducciones.keys();e.hasMoreElements();) 
                    {   
                        String locale = e.nextElement().toString();
                        String traduccion = htTraducciones.get(locale).toString();
                        
                        if (idVocablo ==0)
                        {
                            s = conn.prepareStatement("LMinsertarDiccionarioSecuencial");
                            s.setString(1, locale);
                            s.setString(2, traduccion);
                            if(s.executeUpdate()>=0)
                                GeopistaFunctionUtils.generarSQL (s);
                            
                            s = null;
                            r = null;
                            s = conn.prepareStatement("LMseleccionarDiccionario");
                            s.setString (1,locale);
                            s.setString (2, traduccion);
                            r=s.executeQuery();
                            while (r.next())
                            {
                                idVocablo = r.getInt(1);
                            }       
                        }
                        else
                        {
                        	s=null;
                            s = conn.prepareStatement("LMexisteDiccionario");
                            s.setInt(1, idVocablo);
                            s.setString(2, locale);
                            r=s.executeQuery();
                            if (r.next() && r.getInt(1)==0){
	                            s=null;
	                            s = conn.prepareStatement("LMinsertarDiccionario");
	                            s.setInt(1, idVocablo);
	                            s.setString(2, locale);
	                            s.setString(3, traduccion);
	                            if (s.executeUpdate()>=0)   
	                            	GeopistaFunctionUtils.generarSQL (s);                            
                            }    
                            else GeopistaFunctionUtils.generarSQL (s);   
                        }
                    }                    
                }                
                //Actualizacion de traducciones
                else
                {
                    for (Enumeration e=htTraducciones.keys();e.hasMoreElements();) 
                    {   
                        String locale = e.nextElement().toString();
                        String traduccion = htTraducciones.get(locale).toString();
                        
                        s = null;
                        s = conn.prepareStatement("LMactualizarDiccionario");
                        s.setString(1, traduccion);
                        s.setInt(2, idVocablo);
                        s.setString(3, locale);
                        int iRes = s.executeUpdate();
                        if (iRes >=0)
                            GeopistaFunctionUtils.generarSQL(s);
                        
                        // Si no se realizan actualizaciones, habra que realizar inserciones
                        if (iRes == 0)
                        {   
                            s=null;
                            s = conn.prepareStatement("LMinsertarDiccionario");
                            s.setInt(1, idVocablo);
                            s.setString(2, locale);
                            s.setString(3, traduccion);
                            if(s.executeUpdate()>=0)
                                GeopistaFunctionUtils.generarSQL(s);
                            
                        }
                        
                    }
                }
                
            }
            
            //ELIMINAR
            else
            {
                s = conn.prepareStatement("LMeliminarDiccionario");
                s.setInt(1, idVocablo);
                if(s.executeUpdate()>=0)
                    GeopistaFunctionUtils.generarSQL (s);
                
            }
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
            try{r.close();}catch(Exception e){};
            try{s.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
        } 
        
        return idVocablo;
        
    }
    
    
    /**
     * Actualiza una entrada en la tabla de atributos del sistema (insercion, borrado, o
     * actualizacion)
     * 
     * @param att Atributo a actualizar
     * 
     * @return int Numero de registros actualizados (-1 en caso de error)
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public int actualizarAtributo (Attribute att) throws DataException
    {
        int iRes = NO_RESULTS;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {        
            if (conn == null){
            	conn = getDBConnection();
            }
            
            //update attributes set id_alias =?, id_layer=?, id_column=?, position=?, editable=? where id=?
            //insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),?,?,?,?,?)
            //select id from attributes where id_alias=? and id_layer=?

            //Se trata de una insercion. 
            if(att.getSystemID()==0)
            {                   
                s = conn.prepareStatement("LMinsertarAttributesSecuencial");
                s.setInt(1, att.getIdAlias());
                s.setInt(2, att.getIdLayer());
                s.setInt(3, att.getColumn().getIdColumn());
                s.setInt(4, att.getPosition());
                s.setBoolean(5, att.isEditable());
                iRes = s.executeUpdate();    
                if(iRes>=0)
                    GeopistaFunctionUtils.generarSQL (s);
                
                /*
                 s = null;
                 r = null;
                 s = conn.prepareStatement("LMseleccionarAttributes");
                 s.setInt (1,att.getIdAlias());
                 s.setInt (2, att.getIdLayer());
                 r=s.executeQuery();
                 while (r.next())
                 {
                 idAtributo = r.getInt(1);
                 }
                 
                 */
            }
            
            //Actualizacion de atributos
            else
            {
                s = conn.prepareStatement("LMactualizarAttributes");
                s.setInt(1, att.getIdAlias());
                s.setInt(2, att.getIdLayer());
                s.setInt(3, att.getColumn().getIdColumn());
                s.setInt(4, att.getPosition());
                s.setBoolean(5, att.isEditable());
                s.setInt(6, att.getSystemID());
                iRes = s.executeUpdate();  
                if (iRes>=0)
                    GeopistaFunctionUtils.generarSQL (s);                
            }            
          
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);  
        }finally{
            try{r.close();}catch(Exception e){};
            try{s.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
        } 
        
        return iRes;
    }
    
    /**
     * Actualiza las consultas SQL de una determinada capa 
     * 
     * @param idLayer Identificador de la capa cuyas consultas se desea actualizar
     * 
     * @param htQueries Hashtable cuya key es el identificador de la base de datos
     * y cuyo valor es un objeto Query con las consultas SQL actualizadas
     * 
     * @return int Numero de registros actualizados (-1 en caso de error)
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public int actualizarConsultas (int idLayer, Hashtable htQueries) throws DataException
    {        
        int iRes =NO_RESULTS;
        int idQuery = 0;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {            
            if (conn == null){
            	conn = getDBConnection();
            }
            
            //update queries set selectquery=?, updatequery=?, insertquery=?, deletequery=? where id=?            
            //insert into queries (id, id_layer, databasetype, selectquery, updatequery, insertquery, deletequery) values (nextval('seq_queries'),?,?,?,?,?,?)
            //select id from queries where id_layer=? and databasetype=?
            //delete from queries where id_layer=?

            //INSERTAR O ACTUALIZAR
            if (htQueries!=null)
            {                
                for (Enumeration e=htQueries.keys();e.hasMoreElements();) 
                {   
                    int tipoBD = ((Integer)e.nextElement()).intValue();
                    Query query = (Query)htQueries.get(new Integer(tipoBD));
                    
                    //Comprobar si ya existia en la BD
                    s = null;
                    s = conn.prepareStatement("LMseleccionarQueries");
                    s.setInt(1,idLayer);
                    s.setInt(2, tipoBD);
                    r = s.executeQuery(); 
                    
                    if (r.next())
                    {
                        idQuery = r.getInt(1);
                    }
                    
                    r= null;
                    s= null;
                    
                    if (idQuery > 0)
                    {
                        s = conn.prepareStatement("LMactualizarQueries");
                        s.setString(1,query.getSelectQuery());
                        s.setString(2, query.getUpdateQuery());
                        s.setString(3, query.getInsertQuery());
                        s.setString(4, query.getDeleteQuery());
                        s.setInt(5, idQuery);
                        iRes = s.executeUpdate();
                        if (iRes>=0)
                            GeopistaFunctionUtils.generarSQL (s);
                    }
                    else
                    {
                        
                        s = conn.prepareStatement("LMinsertarQueriesSecuencial");
                        s.setInt(1, idLayer);
                        s.setInt(2, tipoBD);
                        s.setString(3,query.getSelectQuery());
                        s.setString(4, query.getUpdateQuery());
                        s.setString(5, query.getInsertQuery());
                        s.setString(6, query.getDeleteQuery());
                        iRes = s.executeUpdate();     
                        if (iRes>=0)
                            GeopistaFunctionUtils.generarSQL (s);                        
                    }                                    
                }                
            }
            //ELIMINAR
            else
            {
                s = conn.prepareStatement("LMeliminarQueries");
                s.setInt(1, idLayer);
                iRes = s.executeUpdate(); 
                if (iRes>=0)
                    GeopistaFunctionUtils.generarSQL (s);
            }
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);   
        }finally{
            try{r.close();}catch(Exception e){};
            try{s.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
        } 
        
        return iRes;
    }
    
    public String obtenerEstilos(int idLayer) throws DataException{
    	PreparedStatement s = null;
        ResultSet r = null;
        
        String xml = null;
        //select xml from styles where id_style in (select id_styles from layers where id_layer=?)              
        try {
			s = conn.prepareStatement("LMobtenerEstilo");
			s.setInt(1, idLayer);
	        r = s.executeQuery();
	        if (r.next())
	        {
	            xml = r.getString(1);
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return xml;
    }
    
    public int actualizarEstilos(int idLayer, String style) throws DataException{
    	PreparedStatement s = null;
        ResultSet r = null;
        
        int iRes = 0;
        //select xml from styles where id_style in (select id_styles from layers where id_layer=?)              
        try {

            s = conn.prepareStatement("LMactualizarEstilo");
            s.setString(1, style);
            s.setInt(2, idLayer);
            iRes = s.executeUpdate();
            if (iRes>=0)
                GeopistaFunctionUtils.generarSQL (s);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return iRes;
    }
    
    /**
     * Actualiza una capa determinada (insercion o modificacion)
     * 
     * @param lt Objeto LayerTable con los campos que se desea que tenga la capa
     * @param idLayer Identificador de la capa en el sistema (0 si es creacion)
     * @param oldName Nombre de la capa antes de la modificacion, o null si no aplica
     * 
     * @return El identificador de la capa si se ha actualizado correctamente,
     * o 0 en caso de error
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public int actualizarLayer (LayerTable lt, String oldName) throws DataException
    {
        
        StringBuffer estilo = new StringBuffer();      
        int idEstilo = 0;
        int idLayer = lt.getIdLayer();
        PreparedStatement s = null;
        ResultSet r = null;
        
        try
        {   
            if (conn == null){
            	conn = getDBConnection();
            }
            //Se trata de una insercion. 
            if(idLayer == 0)
            {     
                String descripcion = lt.getLayer().getDescription();
                estilo.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?> <StyledLayerDescriptor version=\"1.0.0\" xmlns=\"http://www.opengis.net/sld\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> <NamedLayer><Name>")
                .append(descripcion).append("</Name><UserStyle><Name>default:")
                .append(descripcion).append("</Name><Title>default:")
                .append(descripcion).append("</Title><Abstract>default:")
                .append(descripcion).append("</Abstract><FeatureTypeStyle><Name>")
                //.append(descripcion).append("</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name='fill-opacity'>1.0</CssParameter><CssParameter name='fill'>#ccffcc</CssParameter></Fill><Stroke><CssParameter name='stroke'>#339900</CssParameter><CssParameter name='stroke-width'>1.0</CssParameter><CssParameter name='stroke-linejoin'>mitre</CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke-linecap'>butt</CssParameter></Stroke></PolygonSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>");
                .append(descripcion).append("</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator>")
                .append("<PolygonSymbolizer><Fill><CssParameter name='fill'>#ffffff</CssParameter><CssParameter name='fill-opacity'>1.0</CssParameter></Fill><Stroke><CssParameter name='stroke-width'><ogc:Literal>1.0</ogc:Literal>")
                .append("</CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke'>#339900</CssParameter><CssParameter name='stroke-linejoin'>mitre</CssParameter><CssParameter name='stroke-linecap'>butt</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract>")
                .append("<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name='stroke-width'><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke'>#0066ff</CssParameter><CssParameter name='stroke-linejoin'>round</CssParameter>")
                .append("<CssParameter name='stroke-linecap'>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name='fill'>#ffffff</CssParameter>")
                .append("<CssParameter name='fill-opacity'>1.0</CssParameter></Fill><Stroke><CssParameter name='stroke-width'><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke'>#000000</CssParameter><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke-linecap'>round</CssParameter></Stroke></Mark>")
                .append("<Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>");
                
                
                s= conn.prepareStatement("LMinsertarEstiloCapaSecuencial");
                s.setString(1, estilo.toString());
                if (s.executeUpdate()>=0)
                    GeopistaFunctionUtils.generarSQL (s);
                
                s = null;
                s = conn.prepareStatement("LMseleccionarEstilo");
                s.setString(1, estilo.toString());
                
                r = s.executeQuery();
                while (r.next())
                {
                    idEstilo = r.getInt(1);
                }       
                
                s = conn.prepareStatement("LMinsertarLayersSecuencial");
                s.setInt(1, new Integer(lt.getLayer().getName()).intValue()); //id_name es numerico en la tabla
                s.setString(2, descripcion);
                s.setInt(3, idEstilo);
                s.setInt(4, Integer.parseInt(lt.getAcl().getId()));
                s.setInt(5, Integer.parseInt(lt.getIdEntidadLayer()));
                s.setInt(6, lt.getLayer().isVersionable()?1:0);
                s.setString(7, lt.getLayer().getValidator());
                if(s.executeUpdate()>=0)     
                    GeopistaFunctionUtils.generarSQL (s);
                
                s = null;
                r = null;
                s = conn.prepareStatement("LMseleccionarLayers");
                s.setInt(1, new Integer(lt.getLayer().getName()).intValue()); //id_name es numerico en la tabla
                s.setString(2, lt.getLayer().getDescription());
                s.setInt(3, idEstilo);
                
                r = s.executeQuery();
                while (r.next())
                {
                    idLayer = r.getInt(1);
                }                
                
            }
            
            //Actualizacion de capa
            else 
            {
                if (oldName!=null)
                {
                    //Se actualiza el estilo XML si el nombre de la capa se ha modificado
                    String xml = new String();
                    //select xml from styles where id_style in (select id_styles from layers where id_layer=?)              
                    s = conn.prepareStatement("LMobtenerEstilo");
                    s.setInt(1, idLayer);
                    r = s.executeQuery();
                    if (r.next())
                    {
                        xml = r.getString(1);
                    }
                    xml = xml.replaceAll(oldName, lt.getLayer().getDescription());                
                    
                    
                    //update styles set xml=? where id_style in (select id_style from layers where id_layer=?)
                    s=null;
                    s = conn.prepareStatement("LMactualizarEstilo");
                    s.setString(1, xml);
                    s.setInt(2, idLayer);
                    if(s.executeUpdate()>=0)       
                        GeopistaFunctionUtils.generarSQL (s);
                } 
                
                s=null;
                s = conn.prepareStatement("LMactualizarLayers");
                s.setString(1, lt.getLayer().getDescription());
                s.setInt(2, Integer.parseInt(lt.getAcl().getId()));
                s.setInt(3, Integer.parseInt(lt.getIdEntidadLayer()));
                s.setInt(4, lt.getLayer().isVersionable()?1:0);
                s.setString(5, lt.getLayer().getValidator());
                s.setInt(6, idLayer);
                
                if(s.executeUpdate()>=0)       
                    GeopistaFunctionUtils.generarSQL (s);
                
            }
            s = conn.prepareStatement("LMborrarDynamicLayers");
            s.setInt(1, idLayer); //id_name es numerico en la tabla
            s.setInt(2, Integer.parseInt(lt.getIdEntidadLayer()));
            if(s.executeUpdate()>=0)     
                GeopistaFunctionUtils.generarSQL (s);

            if (lt.getLayer().isDinamica()){
                s = conn.prepareStatement("LMinsertarDynamicLayers");
                s.setInt(1, idLayer); //id_name es numerico en la tabla
                s.setString(2, lt.getLayer().getUrl());
                s.setInt(3, Integer.parseInt(lt.getIdEntidadLayer()));
                if(s.executeUpdate()>=0)     
                    GeopistaFunctionUtils.generarSQL (s);
            }

            lt.setIdLayer(idLayer);
            
        }
        catch (SQLException ex)
        {
            idLayer=0;
            throw new DataException(ex);
        }finally{
            try{r.close();}catch(Exception e){};
            try{s.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
        } 
        
        return idLayer;
        
    }
    
    /**
     * Elimina del diccionario las referencias a una serie de atributos
     * 
     * @param hsAtributos Conjunto de atributos a borrar
     * @return int Numero de registros actualizados (-1 en caso de error)
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public int eliminarAtributos (HashSet hsAtributos) throws DataException
    {      
        int iRes = NO_RESULTS;
        ResultSet r = null;
        PreparedStatement s = null;
        try
        {            
            if (conn == null){
            	conn = getDBConnection();
            }
            
            Iterator it = hsAtributos.iterator();
                                   
            while (it.hasNext()) 
            {
                Attribute att= (Attribute)it.next();
                
                int idAtributo = att.getSystemID();
                
                if (idAtributo>0)
                {                    
                    s = null;                   
                    //delete from attributes where id=?
                    s = conn.prepareStatement("LMborrarReferenciaAtributo");
                    s.setInt(1, idAtributo);
                    iRes = s.executeUpdate();
                    if (iRes>=0)
                        GeopistaFunctionUtils.generarSQL (s);
                    
                    int idAlias = att.getIdAlias();
                    
                    
                    
                    s = conn.prepareStatement("LMbuscarAliasAttributes");
                    s.setInt(1, idAlias);
                    
                    r = s.executeQuery();  
                    if (r.getFetchSize()==0){ 
                        s = null;                       
                        //delete from dictionary where id_vocablo=?
                        s = conn.prepareStatement("LMborrarReferenciaDiccionario");
                        s.setInt(1, idAlias);
                        iRes = s.executeUpdate();
                        if (iRes>=0)
                            GeopistaFunctionUtils.generarSQL (s);
                    }
                }
                
                else
                    iRes= 0;
            }
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);         
        }finally{
            try{r.close();}catch(Exception e){};
            try{s.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
        } 
        
        return iRes;     
        
    }
    
    /**
     * Realiza una consulta SQL directamente sobre la Base de Datos
     * 
     * @param consulta Consulta SQL a realizar
     * @param idMunicipio Identificador numerico de 5 caracteres del municipio 
     * 
     * @return StringBuffer con el resultado de la consulta
     * 
     * @throws DataException Si se produce un error de acceso a datos 
     * @throws SQLException 
     */
    public StringBuffer obtenerResultadoTest (String consulta, int idEntidad) throws DataException, SQLException
    {
        StringBuffer resultado =new StringBuffer();
        PreparedStatement s = null;
        ResultSet r = null;
        try{
            if (conn == null){
            	conn = getDBConnection();
            }
	        if (consulta.length()>0)
	        {   
	            Pattern pattern=Pattern.compile("\\?M");
	            String cadenaMunicipios = obtenerSecuenciaMunicipios(idEntidad);
	            consulta= pattern.matcher(consulta).replaceAll(cadenaMunicipios);
	            pattern=Pattern.compile("\\?S");
	            consulta= pattern.matcher(consulta).replaceAll(String.valueOf(UTM_30N_ETRS89));
	            pattern=Pattern.compile("\\?T");
	            consulta= pattern.matcher(consulta).replaceAll(String.valueOf(UTM_30N_ETRS89));
	            resultado.append("> ").append(consulta).append("\n\n"); 
	            
	            
	            
	            s = conn.prepareStatement(consulta);
	            
	            r = s.executeQuery();            
	                  
	            ResultSetMetaData rsmd = r.getMetaData();
	            int numColumns = rsmd.getColumnCount();
	            
	            //Nombres de las columnas              
	            for (int i=1; i<numColumns+1; i++) 
	            {                    
	                String tableName = rsmd.getTableName(i);
	                String columnName = rsmd.getColumnName(i);                    
	                resultado.append(tableName).append(" ").append(columnName).append(" \t");                    
	            }
	            
	            StringBuffer cadenaAux = new StringBuffer();
	            for (int i=0; i<resultado.length(); i++)
	            {
	                cadenaAux.append("-");
	            }
	            
	            resultado.append("\n").append(cadenaAux).append("\n");
	            
	            while (r.next()) {
	                
	                //Datos de las columnas
	                for (int j=1; j< numColumns+1; j++)
	                {
	                    String dato = r.getString(j);
	                    resultado.append(dato).append("\t\t");                        
	                }                    
	                resultado.append("\n");                    
	            }                      
	        }
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);         
        }finally{
	        try{r.close();}catch(Exception e){};
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    } 
        return resultado;
    }
    
    
    
    /**
     * Elimina de la base de datos cualquier relacion existente entre una categoria
     * de capas o layerfamily y todas las layers que contiene
     * 
     * @param idLayerFamily Identificador numerico de la categoria o layerfamily
     * @throws DataException Si se produce un error de acceso a datos 
     */
    
    public void eliminarRelacionesLayerFamily (int idLayerFamily) throws DataException
    {        
    	PreparedStatement s = null;
        try
        {         
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMeliminarRelacionesLayerFamily");
            s.setInt(1, idLayerFamily);
            if(s.executeUpdate()>=0)     
                GeopistaFunctionUtils.generarSQL (s);            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }      
    }
    
    /**
     * Elimina de la base de datos la relacion existente entre una categoria
     * de capas o layerfamily y una capa o layer
     * 
     * @param idLayerFamily Identificador numerico de la categoria o layerfamily
     * @param idLayer Identificador numerico de la capa o layer
     * @throws DataException Si se produce un error de acceso a datos 
     * @return true si la operacion se realiza con exito
     */
    public boolean eliminarRelacionLayerFamilyLayer (int idLayerFamily, int idLayer) throws DataException
    {    
        boolean bRes= true;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {             
            if (conn == null){
            	conn = getDBConnection();
            }           
            s = conn.prepareStatement("LMseleccionarCapasFamilia");
            s.setInt(1, idLayerFamily);
            r = s.executeQuery();   
            
            Vector idCapas = new Vector();
            while (r.next())
            {
                idCapas.add(new Integer(r.getInt(1)));
            }
            idCapas.remove(new Integer(idLayer));            
            
            //eliminar todas las relaciones de la layerfamily
            eliminarRelacionesLayerFamily(idLayerFamily);            
            
            Iterator it = idCapas.iterator();
            while (it.hasNext())
            {
                asociarLayerFamilyLayer(idLayerFamily, ((Integer)it.next()).intValue());
            }
        }
        catch (SQLException ex)
        {
            bRes = false;
            throw new DataException(ex);
        }finally{
	        try{r.close();}catch(Exception e){};
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }       
        
        return bRes;
        
    }
    /**
     * Realiza una asociacion entre una capa y una categoria o layerfamily
     * 
     * @param idLayerFamily IDentificador numerico de la layerfamily
     * 
     * @param idLayer Identificador numerico de la layer
     * 
     * @return Posicion en la que se inserta la capa dentro de la layerfamily, o 
     * 0 en caso de error 
     */
    public int asociarLayerFamilyLayer(int idLayerFamily, int idLayer) throws DataException
    {   
        int posicion = 0;
        PreparedStatement s = null;
        ResultSet r = null;            
        
        try
        {             
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMbuscarMaxPosicionLayer");
            s.setInt(1, idLayerFamily);
            
            r= s.executeQuery();
            posicion=1;
            if (r.next()){
                
                posicion = r.getInt(1) + 1;
            }
            
            s = null;
            r = null;
            s = conn.prepareStatement("LMinsertarRelacionLayerFamily");
            s.setInt(1, idLayerFamily);
            s.setInt(2, idLayer);
            s.setInt(3, posicion);
            if(s.executeUpdate()>=0)
                GeopistaFunctionUtils.generarSQL (s);
        }
        catch (SQLException ex)
        {
            posicion=0;
            throw new DataException(ex);
        }finally{
	        try{r.close();}catch(Exception e){};
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }  
        
        return posicion;
    }
    
    /**
     * Obtiene los identificadores de todas las categorias o layerfamilies
     * en las que esta presente una capa
     * 
     * @param idLayer Identificador numerico de la capa
     * 
     * @return Vector de identificadores de layerfamilies
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public Vector obtenerLayerfamiliesConLayer (int idLayer) throws DataException
    {
        Vector vcIdFamily = new Vector();
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {          
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMseleccionarFamiliasConCapa");
            s.setInt(1, idLayer);
            r= s.executeQuery();    
            
            while (r.next())
            {
                vcIdFamily.add(new Integer(r.getInt(1)));
            }
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);     
        }finally{
	        try{r.close();}catch(Exception e){};
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }  
        
        return vcIdFamily; 
        
    }
    

    /**
     * Recupera un array con todas las layerfamilies del sistema, con las traducciones 
     * de sus nombres ordenadas alfabeticamente
     * 
     * @return Array de LayerFamilyTable con todos los atributos disponibles de la layerfamily
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public LayerFamilyTable[] obtenerLayerFamilyTable() throws DataException
    {
        LayerFamilyTable[] lft; 
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {
            //String query = "select id_layerfamily, id_name, locale, traduccion from layerfamilies, dictionary where layerfamilies.id_name= dictionary.id_vocablo order by id_layerfamily";
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMobtenerlayerfamiliesTraducNombres");
            r = s.executeQuery();  
            
            Hashtable htNombres = new Hashtable();
            
            r.last();
            int numFilas = r.getRow();
            r.first();
            
            List lstLayerFamilies = new ArrayList();
            
            for (int i =0; i< numFilas; i++)  
            {  
                boolean variosLocales = false;
                int idLayerFamily = r.getInt("id_layerfamily");
                LayerFamilyTable familiesTab = new LayerFamilyTable();
                LayerFamily fam = new LayerFamily();
                fam.setSystemId(String.valueOf(idLayerFamily));
                fam.setDescription(r.getString("id_name"));
                familiesTab.setLayerFamily(fam);
                
                while (r.getInt("id_layerfamily") == idLayerFamily && i<numFilas)
                {
                    htNombres.put(r.getString("locale"), r.getString("traduccion"));
                    if (i<numFilas-1) 
                        r.next();
                    i++;
                    variosLocales = true;
                }
                
                
                familiesTab.setHtNombre(htNombres);
                lstLayerFamilies.add(familiesTab);
                htNombres = new Hashtable();
                
                if (!variosLocales)
                    r.next();
                else
                    i--;
            }                     
            
            Comparator layerFamilyComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    LayerFamilyTable c1 = (LayerFamilyTable) o1;
                    LayerFamilyTable c2 = (LayerFamilyTable) o2;
                    
                    String desc1="";                    
                    if(c1.getHtNombre().get(locale)!=null)
                        desc1 = c1.getHtNombre().get(locale).toString();
                    else if (c1.getHtNombre().get(AppConstants.DEFAULT_LOCALE)!= null)
                        desc1 = c1.getHtNombre().get(AppConstants.DEFAULT_LOCALE).toString();
                    else if (c1.getHtDescripcion().get(locale)!=null)
                        desc1 = c1.getHtDescripcion().get(locale).toString();
                    else if (c1.getHtDescripcion().get(AppConstants.DEFAULT_LOCALE)!= null)
                        desc1 = c1.getHtDescripcion().get(AppConstants.DEFAULT_LOCALE).toString();   
                    
                    String desc2="";                    
                    if(c2.getHtNombre().get(locale)!=null)
                        desc2 = c2.getHtNombre().get(locale).toString();
                    else if (c2.getHtNombre().get(AppConstants.DEFAULT_LOCALE)!= null)
                        desc2 = c2.getHtNombre().get(AppConstants.DEFAULT_LOCALE).toString();
                    else if (c2.getHtDescripcion().get(locale)!=null)
                        desc2 = c2.getHtDescripcion().get(locale).toString();
                    else if (c2.getHtDescripcion().get(AppConstants.DEFAULT_LOCALE)!= null)
                        desc2 = c2.getHtDescripcion().get(AppConstants.DEFAULT_LOCALE).toString();   
                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);
                    
                    //return desc1.compareToIgnoreCase(desc2);
                }
            };
            
            
            lft = (LayerFamilyTable[])lstLayerFamilies.toArray((new LayerFamilyTable[lstLayerFamilies.size()]));
            Arrays.sort(lft, layerFamilyComparator);
        }      
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
	        try{r.close();}catch(Exception e){};
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }  
        
        return lft;
        
    }
    
    /**
     * Obtiene los identificadores de todas las layerfamilies y capas disponibles en
     * el sistema.
     *  
     * @return Mapa con todos los identificadores de las layerfamilies del sistema como
     * clave y una lista ordenada por posicion de los identificadores de las capas que contiene
     * @throws DataException Si se produce un error de acceso a datos
     */
    public HashMap obtenerRelacionesLayerFamiliesLayers () throws DataException
    {   
        HashMap hmRelaciones = null;
        PreparedStatement s = null;
        ResultSet r = null;
        
        try
        {   
            hmRelaciones = new HashMap();
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMobtenerlayerfamiliesrelations");            
            r= s.executeQuery();
            
            r.last();
            int numFilas = r.getRow();
            r.first();            
            
            for (int i =0; i< numFilas; i++)  
            {  
                boolean variasLayers = false;
                int idLayerFamily = r.getInt("id_layerfamily");
                
                List lstNombres = new ArrayList();
                
                while (r.getInt("id_layerfamily") == idLayerFamily && i<numFilas)
                {
                    try
                    {
                        lstNombres.add(r.getInt("position")-1, new Integer(r.getString("id_layer")));
                    }
                    catch (IndexOutOfBoundsException bex)
                    {
                        lstNombres.add(new Integer(r.getString("id_layer")));                            
                    }
                    
                    if (i<numFilas-1) 
                        r.next();
                    i++;
                    variasLayers = true;
                }
                
                hmRelaciones.put(new Integer(idLayerFamily), lstNombres);
                
                
                if (!variasLayers)
                    r.next();
                else
                    i--;
            }

        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
	        try{r.close();}catch(Exception e){};
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }  
        
        return hmRelaciones;
    }
    
    /**
     * Elimina del sistema una capa determinada
     *  
     * @param idLayer Identificador numerico de la capa a eliminar
     * 
     * @return int Numero de registros actualizados (-1 en caso de error)
     * 
     * @throws DataException Si se produce un error de acceso a datos
     */
    public int eliminarLayer (int idLayer) throws DataException
    {    
        int iRes =NO_RESULTS;
        PreparedStatement s = null;
        try
        {             
            if (conn == null){
            	conn = getDBConnection();
            }      
            s = conn.prepareStatement("LMeliminarLayer");
            s.setInt(1, idLayer);
            iRes=s.executeUpdate();
            GeopistaFunctionUtils.generarSQL (s);                  
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }       
        return iRes;
    }
    
    /**
     * Obtiene todos los mapas en los que se incluye una capa determinada
     * 
     * @param idLayer Identificador numerico de la capa
     * 
     * @return Vector con los identificadores de los mapas en los que se encuentra
     * la capa
     * 
     * @throws DataException Si se produce un error de acceso a datos
     */ 
    public Vector buscarMapasWithLayer (int idLayer) throws DataException
    {
        Vector vcIdMapas = new Vector();
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {                   
        	if (conn == null){
        		conn = getDBConnection();
        	}      
        	s = conn.prepareStatement("LMbuscarMapas");
            s.setInt(1, idLayer);
            
            r = s.executeQuery();
            
            while (r.next())
            {
                vcIdMapas.add(new Integer(r.getInt(1)));
            } 
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);      
        }finally{
	        try{r.close();}catch(Exception e){};
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }   
        
        return vcIdMapas;
    }
    
    /**
     * Elimina el estilo de la capa indicada
     * 
     * @param idLayer Identificador numerico de la capa
     * 
     * @return int Numero de registros actualizados (-1 en caso de error)
     *      
     * @throws DataException Si se produce un error de acceso a datos
     */ 
    public int eliminarStyle (int idLayer) throws DataException
    {    
        int iRes = NO_RESULTS;
        PreparedStatement s = null;
        
        try
        {             
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMeliminarStyle");
            s.setInt(1, idLayer);
            
            iRes = s.executeUpdate();
            if(iRes>=0)
                GeopistaFunctionUtils.generarSQL (s);                
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }          
        return iRes;
    }
    
    /**
     * Elimina el layerStyle de la capa indicada
     * 
     * @param idLayer Identificador numerico de la capa
     * 
     * @return int Numero de registros actualizados (-1 en caso de error)
     *      
     * @throws DataException Si se produce un error de acceso a datos
     */ 
    public int eliminarLayerStyle(int idLayer) throws DataException
    {    
        int iRes = NO_RESULTS;
        PreparedStatement s = null;
        try
        {             
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMeliminarLayerStyle");
            s.setInt(1, idLayer);
            
            iRes = s.executeUpdate(); 
            if(iRes>=0)                 
                GeopistaFunctionUtils.generarSQL (s);               
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }          
        return iRes;
    } 
    
    /**
     * Crea una secuencia para insertar secuencialmente las features de una capa
     * 
     * @param nombreTabla Nombre de la tabla de la que se extrae el atributo GEOMETRY
     * 
     * @return true si la operacion se realiza correctamente
     * 
     * @throws DataException Si se produce un error de acceso a base de datos
     */
    public boolean crearSecuencia(String nombreTabla) throws DataException
    {   
    	PreparedStatement s = null;
        boolean bRes = false;
        try{
            if (conn == null){
            	conn = getDBConnection();
            }
        	if (!existeSecuencia("seq_"+nombreTabla) && (!existeSecuencia("seq_"+nombreTabla.toLowerCase()))){
	            String query = "CREATE SEQUENCE \"public\".\"seq_"+ nombreTabla.toLowerCase()
	            + "\" INCREMENT 1 MINVALUE 1 START 1 CACHE 1;";            
	            
	          
	            s = conn.prepareStatement(query.toString());
	            s.executeUpdate();
	            GeopistaFunctionUtils.generarSQL(s);   
	            bRes = true;

        	}           
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }        
        return bRes;
        
    } 
    /**
     * Actualiza una secuencia para insertar secuencialmente las features de una capa
     * 
     * @param newTableName Nombre de la tabla de la que se extrae el atributo GEOMETRY
     *
     * @param oldTableName Nombre de la anterior tabla de la que se extraia el atributo GEOMETRY
     * 
     * @return true si la operacion se realiza correctamente
     * 
     * @throws SQLException Si no puede acceder a base de datos para crear la secuencia
     */
    public boolean actualizarSecuencia(String newTableName, String oldTableName) throws DataException, SQLException
    {    
        boolean bRes = false;
        PreparedStatement s = null;
        try{
            if (conn == null){
            	conn = getDBConnection();
            }
	        if (!oldTableName.equals(newTableName)){
	            
	            StringBuffer query = new StringBuffer();
	            query.append("CREATE SEQUENCE \"public\".\"seq_").append(newTableName)
	            .append("\" INCREMENT 1 MINVALUE 1 START 1 CACHE 1;")
	            .append("select setval('seq_").append(newTableName).append("',cast(nextval('seq_").append(oldTableName).
	            append("') as bigint),'t');");            
	            
	            s = conn.prepareStatement(query.toString());
	            s.executeUpdate();
	            bRes =true;
	            GeopistaFunctionUtils.generarSQL(s);   

	        }
	    }catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }     
        return bRes;        
    } 
    
    /*
     public static void generarSQL (PreparedStatement ps)
     {
     boolean genSQL = new Boolean(aplicacion.getString("geopista.generarsql")).booleanValue();
     
     if (genSQL)
     {   
     try
     {              
     String rutaFich = aplicacion.getString("ruta.base.mapas");
     File fichLog = new File (rutaFich, "BDqueries.sql");
     OutputStream out = new FileOutputStream(fichLog, true);
     String stQuery = new String(); 
     
     if (ps instanceof GEOPISTAPreparedStatement)
     {
     
     PreparedStatement s = null;
     ResultSet r = null;
     java.sql.Connection conn=  aplicacion.getConnection();
     conn.setAutoCommit(false);            
     
     s = conn.prepareStatement("getQueryById");
     s.setString(1, ((GEOPISTAPreparedStatement)ps).getQueryId());                
     r = s.executeQuery();
     
     if (r.next())
     {
     stQuery= r.getString(1);
     Object[] oData=(Object[])(new ObjectInputStream(new ByteArrayInputStream(((GEOPISTAPreparedStatement)ps).getParams())).readObject());
     
     
     for (int i=0;i<oData.length;i++)
     {
     String cadSustitucion = new String();
     
     if (oData[i]!=null)
     {
     cadSustitucion = oData[i].toString();
     cadSustitucion = cadSustitucion.replaceAll("\\?", "\\!");
     
     if (oData[i] instanceof String)
     cadSustitucion = "'"+cadSustitucion+"'";
     }
     else
     {
     cadSustitucion="null";
     }
     stQuery = stQuery.replaceFirst("\\?", cadSustitucion);
     
     }  
     stQuery= stQuery.replaceAll("\\!", "\\?") +";\n";
     out.write(stQuery.getBytes());                      
     
     }
     }
     else if (ps instanceof StandardXAPreparedStatement)
     {   
     java.sql.Connection conex=CPoolDatabase.getConnection();
     if (((org.enhydra.jdbc.core.CoreConnection)conex).con instanceof org.postgresql.PGConnection)
     {
     stQuery = ((Jdbc2PreparedStatement)(((StandardXAPreparedStatement)ps).ps)).toString()+ ";\n";;
     out.write(stQuery.getBytes());                     
     
     }
     conex.close();
     CPoolDatabase.releaseConexion();               
     }
     out.close();
     }
     catch (SQLException ex)
     {
     ex.printStackTrace();
     ErrorDialog.show(aplicacion.getMainFrame(), I18N.get("GestorCapas","SQLError.Titulo"), I18N.get("GestorCapas","SQLError.Aviso"), StringUtil.stackTrace(ex));                  
     return;
     }
     
     catch (Exception ex)
     {            
     ex.printStackTrace();
     return;
     }            
     }
     }
     */
    
 
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
    
    public HashMap obtenerListaColumnasSistema(int idTable) throws DataException
    {
        HashMap htColSis = new HashMap();            
        PreparedStatement s = null;
        ResultSet r = null; 
        try
        {            
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMobtenercolumnasSis");
            s.setInt(1, idTable);                 
            
            r = s.executeQuery();  
            while (r.next())
            {
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
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
	        try{r.close();}catch(Exception e){};
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }   
        return htColSis;        
    }
    
  
 
    public int getNextColumnPosition(HashMap columns)
    {
        int pos = 0;
        
        Iterator it= columns.keySet().iterator();
        while (it.hasNext())
        {   
            Object o = it.next();
            if(o instanceof Integer)
            {
                int aux= Integer.valueOf(o.toString()).intValue();
                if (aux>pos)
                    pos = aux;
            }
                        
        }     
        
        return ++pos;
    }


    /**
     * Busca la posicion de una columna dentro de una tabla
     * 
     * @param table Table donde buscar la columna
     * @param colrow ColumnRow a buscar dentro de la tabla
     * @return Posicion de la columna, o 0 en caso de no encontrarla
     */
    public int findColumnPosition(Table table, ColumnRow colrow)
    {
        int pos=0;
        HashMap hm = new HashMap(table.getColumns());
        
        Iterator it = hm.keySet().iterator();
        while (it.hasNext())
        {
            Object obj = it.next();
            if(obj instanceof Integer && hm.get((Integer)obj) instanceof ColumnDB && 
                    ((ColumnDB)hm.get((Integer)obj)).equals(colrow.getColumnaBD()))
                pos = ((Integer)obj).intValue();
            
//            else if (hm.get(obj) instanceof Column)
//                System.out.println("   ************** ES COLUMN!!!! *********");
//            else if (hm.get(obj) instanceof ColumnRow)
//                System.out.println("   ************** ES COLUMN ROW!!!! *********");
            
        }
        
        return pos;
    }
     
 
    /**
     * Obtiene la lista de ACLs y su descripcion
     * @return ArrayList con la lista de ACL dados de alta en el sistema
     * @throws DataException
     */
    public ArrayList getAclList() throws DataException
    {

        ArrayList lstAcl= new ArrayList();
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {  
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMobtenerAcls");
            r = s.executeQuery();  
            
            while (r.next())
            {     
                Acl acl = new Acl(r.getString(1), r.getString(2));
                lstAcl.add(acl);                
            }     
            
            
            //Los ordenamos alfabeticamente		
			Object[] arrayNodos= lstAcl.toArray();
	    	Arrays.sort(arrayNodos,new AclComparatorByDescripcion());
	
	    	//Los devolvemos ordenados
	    	lstAcl.clear();
			for (int i = 0; i < arrayNodos.length; ++i) {
				Acl acl = (Acl) arrayNodos[i];
				lstAcl.add(acl);
			}
			           
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);            
        }finally{
	        try{r.close();}catch(Exception e){};
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }   
        
        return lstAcl;    
    }    
    
    /**
     * Actualiza las relaciones entre columnas y dominios
     * 
     * @param columna Column a actualizar
     * @param dominio Domain a actualizar
     * @param level Valor del atributo level de un dominio de tipo TreeDomain
     * @return Numero de registros actualizados (-1 en caso de error)
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public int actualizarDominioColumna (Column columna, Domain dominio, int level) throws DataException
    {        
        
        boolean columnaExistente = false;
        int idDominioActual = 0;
        int idColumna = 0;
        int iRes=NO_RESULTS;
        PreparedStatement s = null;
        try
        {    
            if (conn == null){
            	conn = getDBConnection();
            }
            int idDominioNuevo = dominio.getSystemID();
            idColumna = columna.getIdColumn(); 
            
            //Se extrae el dominio que utiliza actualmente la columna
            if (columna.getDomain()!=null){
                idDominioActual = columna.getDomain().getSystemID();
                
                columnaExistente = true;
            }  
            
            
            
            if (columnaExistente)
            {
            	logger.info("Actualizando dominio: "+idDominioNuevo);
                //update columns_domains set id_domain=?, level=? where id_domain=? and id_column=?; update columns set id_domain=? where id = ? 
                s = conn.prepareStatement("LMactualizardominiocolumna");
                s.setInt(1, idDominioNuevo);
                s.setInt(2, level);
                s.setInt(3, idDominioActual);
                s.setInt(4, idColumna);
                s.setInt(5, idDominioNuevo);
                s.setInt(6, idColumna);
            }
            else
            {
                //insert into columns_domains (id_domain, id_column, level) values (?,?,?); update columns set id_domain=? where id = ?
        		logger.info("Insertando nuevo dominio: "+idDominioNuevo+":"+idColumna+":"+level);
                s = conn.prepareStatement("LMinsertardominiocolumna");
                s.setInt(1, idDominioNuevo);
                s.setInt(2, idColumna);
                s.setInt(3, level);
                s.setInt(4, idDominioNuevo);
                s.setInt(5, idColumna);
            }
            
            iRes = s.executeUpdate();  
            //conn.commit();            
            
            if(iRes>=0)
                GeopistaFunctionUtils.generarSQL (s);
                        
            columna.setLevel(level);
            columna.setDomain(dominio);            
            
        }
        
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }   
        
        return iRes;
        
    }

    /**
     * Comprueba si existe una capa en el sistema con el Id pasado como parametro
     * @param idLayer
     * @return
     */
    public boolean existeCapaId(String idLayer) throws DataException
    {
        boolean existeCapa = false;
        PreparedStatement s = null;
        ResultSet r = null;
        
        try
        {  
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMexisteLayerId");
            s.setString(1, idLayer);
            r = s.executeQuery();  
            
            if (r.next())
            {     
                existeCapa = true;              
            }
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();                        
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);  
        }finally{
	        try{r.close();}catch(Exception e){};
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }   
              
        return existeCapa;
    }
    
    /**
     * Obtiene los datos relativos a  una capa pasandole el nombre
     * @param layerName
     * @return
     */
    public boolean obtenerPermisoCapa(String sLayerName, String sUser, String idPerm) throws DataException
    {
        boolean permiso = false;
        PreparedStatement s = null;
        ResultSet r = null;
        
        try{  
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMobtenerPermisoLayer");
            s.setString(1, sLayerName);
            s.setString(2, idPerm);
            s.setString(3, sUser);
            r = s.executeQuery();  

            if (r.next())
            {     
            	if (r.getInt("modificable") == 1 && r.getInt("aplica")==1 )
            		permiso = true;
            }
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();                        
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);  
        }finally{
	        try{r.close();}catch(Exception e){};
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }     
              
        return permiso;
    }

    /**
     * Comprueba si existe una secuenca en el sistema con el nombre de la secuencia pasado como parametro
     * @param idLayer
     * @return
     */
    public boolean existeSecuencia(String nombreSecuencia) throws DataException{
    	
    	PreparedStatement s = null;
        ResultSet r = null;
    	try{  

            if (conn == null){
            	conn = getDBConnection();
            }
            //s = conn.prepareStatement("SELECT * FROM \""+nombreSecuencia+"\"");
            
            s=conn.prepareStatement("SELECT 0 FROM pg_class where relname='"+nombreSecuencia+"'");
            r = s.executeQuery();  
                    
            if (r.next()){
            	return true;
            }else
            	return false;    
        }
        catch (SQLException ex){
        	return false;
        }finally{
	        try{r.close();}catch(Exception e){};
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }  

    }
    
    public int insertarAcl(String nombre) throws DataException{    
        int iRes = NO_RESULTS;
        PreparedStatement s = null;
        try{             
            if (conn == null){
            	conn = getDBConnection();
            }
            
            s = conn.prepareStatement("LMInsertarAcl");
            s.setString(1, nombre);
            
            iRes = s.executeUpdate();

        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }         
        return iRes;
    }
    
    public int insertarPermisosAcl(String idAcl) throws DataException{    
        int iRes = NO_RESULTS;
        PreparedStatement s = null;
        try{             
            
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMInsertarPermisosAcl");
            //s.setString(1, Integer.parseInt(idAcl));
            //s.setString(2, idAcl);
            //s.setString(3, idAcl);
            //s.setString(4, idAcl);

            s.setInt(1, Integer.parseInt(idAcl));
            s.setInt(2, Integer.parseInt(idAcl));
            s.setInt(3, Integer.parseInt(idAcl));
            s.setInt(4, Integer.parseInt(idAcl));
            
            iRes = s.executeUpdate();
                
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }finally{
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }          
        return iRes;
    }
    
    public Acl getAcl(String nombre) throws DataException{
    	Acl acl = null;
    	PreparedStatement s = null;
        ResultSet r = null;
        try{  
            if (conn == null){
            	conn = getDBConnection();
            }
            s = conn.prepareStatement("LMobtenerAcl");
            s.setString(1, nombre);
            r = s.executeQuery();  
            
            if (r.next()){     
                acl = new Acl(r.getString("idacl"), r.getString("name"));             
            }
                                  
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);            
        }finally{
	        try{r.close();}catch(Exception e){};
	        try{s.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }         
        
        return acl;    
    } 
    
    public com.geopista.server.administrador.web.LocalgisLayer publishMap(LayerTable lt) throws Exception
    {
         PreparedStatement ps = null;
         ResultSet rs = null;
         try {
             if (conn == null){
              	conn = getDBConnection();
              }
             String query = "select t.geometrytype from tables t where t.name=?";
             ps = conn.prepareStatement(query);
             ps.setString(1, lt.getLayer().getDescription());
             rs = ps.executeQuery();
             int geometryType = -1;
             if (rs.next()) {
                 geometryType = rs.getInt("geometryType");
             }
             ps = conn.prepareStatement("LMbuscarqueries");
             ps.setInt(1, lt.getIdLayer());
             rs = ps.executeQuery();
             String selectquery = "";
             if (rs.next()) {
            	 selectquery = rs.getString("selectquery");
             }
             
             StringBuffer st = new StringBuffer();
             st = st.append("select c.*, a.id_alias as idAlias, d.traduccion as alias ");
             st = st.append("from attributes a inner join columns c on (a.id_column = c.id) ");
             st = st.append("left join columns_domains cd on (c.id = cd.id_column) ");
             st = st.append("inner join tables t on (c.id_table = t.id_table) ");
             st = st.append("inner join dictionary d on (a.id_alias=d.id_vocablo) ");
             st = st.append("where a.id_layer=? ");
             st = st.append("order by a.\"position\",t.id_table,c.id");
             ps = conn.prepareStatement(st.toString());
             ps.setInt(1, lt.getIdLayer());
             rs = ps.executeQuery();
             StringBuffer mapaAtributos = new StringBuffer();
             while (rs.next()){
            	 mapaAtributos = mapaAtributos.append(rs.getString("alias"));
            	 mapaAtributos = mapaAtributos.append("=");
            	 mapaAtributos = mapaAtributos.append(rs.getString("name"));
            	 mapaAtributos = mapaAtributos.append(";");
             }
             
             com.geopista.server.administrador.web.LocalgisLayer localgisLayer = new com.geopista.server.administrador.web.LocalgisLayer();
	         localgisLayer.setLayerid(Integer.valueOf(lt.getIdLayer()));
	         localgisLayer.setLayername(lt.getLayer().getDescription());
	         localgisLayer.setLayerquery(selectquery);
	         localgisLayer.setGeometrytype(geometryType);
	         localgisLayer.setMapaAtributos(mapaAtributos.toString());

	         return localgisLayer;
	    }
       catch (Exception ex) {
           throw new DataException(ex);            
       }finally{
			try{rs.close();}catch(Exception e){throw new DataException(e);};
			try{ps.close();}catch(Exception e){throw new DataException(e);};
			try{conn.close();}catch(Exception e){throw new DataException(e);};
       }
    }
    
    public List getStylesLayer(int idLayer) throws DataException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        List stylesList = new ArrayList();
        try {
            if (conn == null){
            	conn = getDBConnection();
            }
            String query = "select distinct l.stylename,s.xml from layers_styles l, styles s where l.id_style=s.id_style and l.id_layer=?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, idLayer);
            rs = ps.executeQuery();
            while (rs.next()) {
                Style style = new Style();
            	style.setName(rs.getString("xml"));
            	style.setTitle(rs.getString("stylename"));
            	stylesList.add(style);
            }
			return stylesList;
	    }
      catch (Exception ex) {
          throw new DataException(ex);            
      }finally{
			try{rs.close();}catch(Exception e){throw new DataException(e);};
			try{ps.close();}catch(Exception e){throw new DataException(e);};
			try{conn.close();}catch(Exception e){throw new DataException(e);};
      }
    }
    
    /**
     * Se insertan los campos de versionado en las tablas involucradas en la capa
     * @param layerName
     * @return boolean indicando si algun campo de la clave tiene otra constraint aparte de primary key o no
     * @throws DataException
     */
    public void crearCamposVersionado(String tableName, boolean tablaVersionada) throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean restricciones = false;
        String mensaje = "";
        try {
            if (conn == null){
            	conn = getDBConnection();
            }
            List listaFKs = obtieneForeignKeys(tableName);
            StringBuffer query = null;
            if (tablaVersionada){
            	try {
            	    //Annado a la tabla los campos de versionado
            		query = new StringBuffer("alter table \"").append(tableName).append("\" add ")
		        				.append(LayersPanel.REVISION_ACTUAL).append(" numeric(10) default 0");
            		ps = conn.prepareStatement(query.toString());
            		ps.executeUpdate();
            	}catch(SQLException e){
            	    //Si ya existe el campo se producira una excepcion SQL al tratar de crearlo. 
            	    //Esta excepcion se captura y se continua con la ejecucion
            		//e.printStackTrace();
            		logger.info("Columna "+ LayersPanel.REVISION_ACTUAL + " ya existe en tabla " + tableName);
            	}
	            
            	try{
			        query = new StringBuffer("alter table \"").append(tableName).append("\" add ")
			        			.append(LayersPanel.REVISION_EXPIRADA).append(" numeric(10) default 9999999999");
			        ps = conn.prepareStatement(query.toString());
			        ps.executeUpdate();
			        ps.close();
            	}catch(SQLException e){
            	    //Si ya existe el campo es que se trata de una tabla que ya esta versionada. Puede ocurrir
            		//que distintas capas tengan la misma tabla
            		//e.printStackTrace();
            		logger.info("Columna "+ LayersPanel.REVISION_EXPIRADA + " ya existe en tabla " + tableName);
            		return;
            	}
		        
            	//Creo secuencia para tabla
		        ps = conn.prepareStatement("obtenerIdTabla");
		        ps.setString(1, tableName);
                rs = ps.executeQuery();
                long idTabla = -1;
                if (rs.next())
                    idTabla = rs.getLong("id_table");
                rs.close();
                ps.close();
                
            	crearSecuencia("version_"+idTabla);
            	
		        //Annado primera revision a la tabla de versiones
		        ps = conn.prepareStatement("insertaVersionInicial");
		        ps.setLong(1, 0);
		        ps.setString(2, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME));
		        ps.setDate(3, new java.sql.Date(((java.util.Date)new Date()).getTime()));
		        ps.setLong(4, idTabla);
                ps.executeUpdate();
                ps.close();
            }
            
            
            if ((listaFKs != null && listaFKs.size() > 0) || tablaVersionada){
            	StringBuffer sbPK = new StringBuffer();
            	camposPrimaryKey(tableName,sbPK);
		        
		        //Borro la clave primaria
				query = new StringBuffer("ALTER TABLE \"").append(tableName).append("\" DROP CONSTRAINT \"").append(tableName).append("_pkey\" cascade");
				ps = conn.prepareStatement(query.toString());
				ps.executeUpdate();
				        
			        //Vuelvo a crear la clave primaria ampliada con los campos de versionado
				query = new StringBuffer("ALTER TABLE \"").append(tableName).append("\" ADD CONSTRAINT \"").append(tableName)
				            		.append("_pkey\" PRIMARY KEY(").append(sbPK.toString()).append(",").append(LayersPanel.REVISION_ACTUAL).append(")");
				ps = conn.prepareStatement(query.toString());
				ps.executeUpdate();
				ps.close();
            }
            //Regenero las foreign keys que referenciaban la tabla
            regeneroForeignKey(listaFKs,tableName,true);
		    
            if (listaFKs != null && listaFKs.size() > 0){
            	String[] arrayFKs = (String[])listaFKs.get(0);
            	crearCamposVersionado(arrayFKs[0], false);
            }
        }finally{
        	try{if (conn != null) conn.close();}catch(Exception e){throw new DataException(e);};
        	try{if (rs != null) rs.close();}catch(Exception e){throw new DataException(e);};
			try{if (ps != null) ps.close();}catch(Exception e){throw new DataException(e);};
        }
    }
    
    /**
     * Se crea la secuencia de versionado de la capa y su primer valor
     * @param tableName
     * @return
     * @throws DataException
     */
    public void crearSecuenciaVersionado(String tableName, boolean tablaVersionada) throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean restricciones = false;
        String mensaje = "";
        try {
            if (conn == null){
            	conn = getDBConnection();
            }
            StringBuffer query = null;
            
            //Creo secuencia para tabla
	        ps = conn.prepareStatement("obtenerIdTabla");
	        ps.setString(1, tableName);
            rs = ps.executeQuery();
            long idTable = -1;
            if (rs.next())
                idTable = rs.getLong("id_table");
            rs.close();
            ps.close();
            
            crearSecuencia("version_"+idTable);            
      
            //NUEVO
            long existeVersion = 0;
            try{
	            ps = conn.prepareStatement("existeVersion");
	            ps.setLong(1, 0);
			    ps.setLong(2, idTable);
			    rs = ps.executeQuery();		  
			    if (rs.next())
			    	existeVersion = rs.getLong(1);
	            rs.close();
	            ps.close();    
            }catch(Exception ex){
            	System.out.println(ex.getMessage());
            	logger.debug(ex.getMessage());
            }
            
            if(existeVersion==0){
            //FIN NUEVO
			     //Annado primera revision a la tabla de versiones
			     ps = conn.prepareStatement("insertaVersionInicial");
			     ps.setLong(1, 0);
			     ps.setString(2, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME));
			     ps.setDate(3, new java.sql.Date(((java.util.Date)new Date()).getTime()));
			     ps.setLong(4, idTable);
	             ps.executeUpdate();
	             ps.close();       
            }
            
            if (tablaVersionada){
            	StringBuffer sbPK = new StringBuffer();
            	camposPrimaryKey(tableName,sbPK);
        		if ((sbPK!=null) && sbPK.length()!=0){
	            	try{
				        //Borro la clave primaria
						query = new StringBuffer("ALTER TABLE \"").append(tableName).append("\" DROP CONSTRAINT \"").append(tableName).append("_pkey\" cascade");
						ps = conn.prepareStatement(query.toString());
						ps.executeUpdate();
	            	} catch(Exception ex){
	              		 logger.error(ex); 
	               	}
        		}
            	
            	try{
					//Borro la clave unique
					query = new StringBuffer("ALTER TABLE \"").append(tableName).append("\" DROP CONSTRAINT \"").append(tableName).append("_id_key\" cascade");
					ps = conn.prepareStatement(query.toString());
					ps.executeUpdate();
            	} catch(Exception ex){
              		 logger.error(ex); 
               	}
            	
        		if ((sbPK!=null) && sbPK.length()!=0){
	            	try{
					    //Vuelvo a crear la clave primaria ampliada con los campos de versionado
						query = new StringBuffer("ALTER TABLE \"").append(tableName).append("\" ADD CONSTRAINT \"").append(tableName)
						            		.append("_pkey\" PRIMARY KEY(").append(sbPK.toString()).append(",").append(LayersPanel.REVISION_ACTUAL).append(")");
						ps = conn.prepareStatement(query.toString());
						ps.executeUpdate();
						ps.close();
	            	} catch(Exception ex){
	           		 logger.error(ex); 
	            	}
            	}
            }
        }finally{
        	try{conn.close();}catch(Exception e){throw new DataException(e);};
        	try{rs.close();}catch(Exception e){throw new DataException(e);};
			try{ps.close();}catch(Exception e){throw new DataException(e);};
        }
    }

    /**
     * Se insertan los campos de versionado en las tablas involucradas en la capa
     * @param layerName
     * @return
     * @throws DataException
     */
    public void deshacerCamposVersionado(String tableName, boolean tablaVersionada) throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            List listaFKs = obtieneForeignKeys(tableName);
            if (conn == null){
            	conn = getDBConnection();
            }
            StringBuffer query = new StringBuffer();
	        if ((listaFKs != null && listaFKs.size() > 0) || tablaVersionada){
	            StringBuffer sb = new StringBuffer();
	            camposPrimaryKey(tableName,sb);
	
	        	if (tablaVersionada){
	        		try{
		            	//Borro campos de versiones anteriores
		            	query = new StringBuffer("delete from \"").append(tableName).append("\" where ").append(LayersPanel.REVISION_EXPIRADA ).append("<9999999999");
	    	            ps = conn.prepareStatement(query.toString());
	    	            ps.executeUpdate();
	    	            //conn.commit();
	        		}catch(Exception e){
	        			//Puede ocurrir que la tabla ya no este versionada. Hay capas que comparten tablas
	        			return;
	        		}
	        	}	
	            //Borro la clave primaria
		        query = new StringBuffer("ALTER TABLE \"").append(tableName).append("\" DROP CONSTRAINT \"").append(tableName).append("_pkey\" cascade");
		        ps = conn.prepareStatement(query.toString());
		        ps.executeUpdate();
	
		        //Vuelvo a crear la clave primaria quitando los campos de versionadao
		        query = new StringBuffer("ALTER TABLE \"").append(tableName).append("\" ADD CONSTRAINT \"").append(tableName).append("_pkey\" PRIMARY KEY(").append(sb.toString()).append(")");
		        ps = conn.prepareStatement(query.toString());
		        ps.executeUpdate();
	            ps.close();
	        }
	        
            if (tablaVersionada){
	            //Quito a la tabla de datos los campos de versionado
	        	query = new StringBuffer("alter table \"").append(tableName).append("\" drop ").append(LayersPanel.REVISION_ACTUAL);
	            ps = conn.prepareStatement(query.toString());
	            ps.executeUpdate();
	            
	        	query = new StringBuffer("alter table \"").append(tableName).append("\" drop ").append(LayersPanel.REVISION_EXPIRADA);
	            ps = conn.prepareStatement(query.toString());
	            ps.executeUpdate();     
	            ps.close();
	            
            	//Creo secuencia para tabla
		        ps = conn.prepareStatement("obtenerIdTabla");
		        ps.setString(1, tableName);
                rs = ps.executeQuery();
                long idTabla = -1;
                if (rs.next())
                    idTabla = rs.getLong("id_table");
                rs.close();
                ps.close();

                ps = conn.prepareStatement("deleteRevisionesTabla");
	            ps.setLong(1, idTabla);
	            ps.executeUpdate();     
	            ps.close();
	            
	            ps = conn.prepareStatement("DROP SEQUENCE \"seq_version_"+idTabla+"\"");
	            ps.executeUpdate();     
	            ps.close();
            }
            
	        //Regenero las foreign keys que referenciaban la tabla
	        if (listaFKs != null && listaFKs.size() > 0){
		        String[] arrayFKs = (String[])listaFKs.get(0);
		        if (!perteneceVersionadoAForeignKeys(arrayFKs[0]))
			        deshacerCamposVersionado(arrayFKs[0],false);
			    regeneroForeignKey(listaFKs,tableName,false);
		        
	        }
        }catch(Exception e){
	        	throw new DataException(e);
        }finally{
        	try{conn.close();}catch(Exception e){throw new DataException(e);};
        	try{rs.close();}catch(Exception e){throw new DataException(e);};
			try{ps.close();}catch(Exception e){throw new DataException(e);};
        }
    }
    
    public void annadirColumna(Table table, ColumnRow colrow) throws DataException{
        PreparedStatement s = null;
        ResultSet r = null;
    	try {
            if (conn == null){
            	conn = getDBConnection();
            }
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
                    tipo.equalsIgnoreCase("int4"))
                idTipo = 2;
            else if (tipo.equalsIgnoreCase("bpchar") || 
                    tipo.equalsIgnoreCase("varchar"))
                idTipo = 3;
            else if (tipo.equalsIgnoreCase("timestamp")||
                    tipo.equalsIgnoreCase("date"))
                idTipo = 4;
            else if (tipo.equalsIgnoreCase("bool"))
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
    		
            table.getColumns().put(new Integer(getNextColumnPosition(table.getColumns())), colrow.getColumnaBD());
    	}catch (Exception ex) {
            throw new DataException(ex);            
        }finally{
        	try{r.close();}catch(Exception e){throw new DataException(e);};
      		try{s.close();}catch(Exception e){throw new DataException(e);};
      		try{conn.close();}catch(Exception e){throw new DataException(e);};
        }
    }

    /**
     * Obtiene una lista con las foreign keys que referencian la tabla
     * @param table
     * @param conn
     * @return
     * @throws SQLException
     */
    private List obtieneForeignKeys(String tableName) throws Exception{
    	StringBuffer sb = new StringBuffer();
    	List listaFK = new ArrayList();
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try{
            if (conn == null){
            	conn = getDBConnection();
            }
	    	sb = sb.append("SELECT c.conname AS constraint_name,t.relname AS table_name,array_to_string(c.conkey, ' ') AS constraint_key,");
	    	sb = sb.append("t2.relname AS references_table,array_to_string(c.confkey, ' ') AS fk_constraint_key  FROM pg_constraint c ");
	    	sb = sb.append("LEFT JOIN pg_class t  ON c.conrelid  = t.oid LEFT JOIN pg_class t2 ON c.confrelid = t2.oid WHERE t2.relname = ? ");
	    	sb = sb.append("and c.contype='f' ORDER BY t.relname");
	    	
	    	ps = conn.prepareStatement(sb.toString());
	    	ps.setString(1, tableName);
	    	rs = ps.executeQuery();
	    	String[] arrayFK = new String[2];
	    	if (rs.next()){
	    		arrayFK[0] = rs.getString("table_name");
	    		String[] arrayClaves = rs.getString("constraint_key").split(" ");
	    		int n = arrayClaves.length;
	    		StringBuffer sbClaves = new StringBuffer();
				Iterator itTablas = this.obtenerListaTablas().iterator();
				while(itTablas.hasNext()){
					Table table = (Table)itTablas.next();
					if (table.getName().equals(arrayFK[0])){
						for (int i=0;i<n;i++){
	    	    			String sColumna = buscarAtributo(arrayFK[0],Integer.parseInt(arrayClaves[i]));
	    	    			if (!sColumna.equals(LayersPanel.REVISION_ACTUAL)){
	    						if (i>0)
	    							sbClaves = sbClaves.append(",");
	    	    				sbClaves = sbClaves.append(sColumna);
	    	    			}
	    				}
	    				break;
	    			}
	    		}
	    		arrayFK[1] = sbClaves.toString();
	    		listaFK.add(arrayFK);
	    	}
    	}catch (Exception ex) {
            throw new DataException(ex);            
        }finally{
        	try{rs.close();}catch(Exception e){throw new DataException(e);};
      		try{ps.close();}catch(Exception e){throw new DataException(e);};
      		try{conn.close();}catch(Exception e){throw new DataException(e);};
        }	        
    	return listaFK;
    }
    
    /**
     * Una vez realizados los cambios de annadido o borrado del versionado, nos disponemos a regenerar las foreign keys
     * que existian antes
     * @param listaFKs
     * @param tableName
     * @param crear
     * @throws SQLException
     */
    private void regeneroForeignKey(List listaFKs,String tableName,boolean crear) throws DataException{
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	StringBuffer query = null;
    	try{
            if (conn == null){
            	conn = getDBConnection();
            }
	    	Iterator itFKs = listaFKs.iterator();
	        while (itFKs.hasNext()){
	            String[]campos = (String[]) itFKs.next();
	        	if (crear){
	        		try{
		                //Annado campo revision_actual a tabla que posee la foreign key
		                query = new StringBuffer("alter table \"").append(campos[0]).append("\" add ")
		    				.append(LayersPanel.REVISION_ACTUAL).append(" numeric(10) default 0");
		                ps = conn.prepareStatement(query.toString());
		                ps.executeUpdate();
	        		}catch(SQLException e){
	        			//Si ya existe el campo se producira una excepcion SQL al tratar de crearlo. 
	        			//Esta excepcion se captura y se continua con la ejecucion
	        		}
	                //Regenero foreign key
	        	    query = new StringBuffer("ALTER TABLE \"").append(campos[0]).append("\" ADD FOREIGN KEY (").append(campos[1]);
	        	    query = query.append(",").append(LayersPanel.REVISION_ACTUAL).append(") REFERENCES ").append(tableName);
	        	    ps = conn.prepareStatement(query.toString());
	        	    ps.executeUpdate();
		    	}else{
		    		try{
		            	//Annado campo revision_actual a tabla que posee la foreign key
		            	query = new StringBuffer("alter table \"").append(campos[0]).append("\" drop column ")
						.append(LayersPanel.REVISION_ACTUAL);
		            	ps = conn.prepareStatement(query.toString());
		            	ps.executeUpdate();
		    		}catch(SQLException e){
		    			//Si la columna es foreign key de otra tabla, no se podra borrar y dara error.
		    			//En este caso, se captura el error para continuar con la ejecucion
		    		}
	            	//Regenero foreign key
	    	        query = new StringBuffer("ALTER TABLE \"").append(campos[0]).append("\" ADD FOREIGN KEY (").append(campos[1]);
	    	        query = query.append(") REFERENCES \"").append(tableName).append("\"");
	    	        ps = conn.prepareStatement(query.toString());
	    	        ps.executeUpdate();
		    	}
	            ps.close();
	        }
    	}catch (Exception ex) {
            throw new DataException(ex);            
        }finally{
	  		try{conn.close();}catch(Exception e){throw new DataException(e);};
	    }	     
    }
    
    /**
     * Busco el nombre de un atributo en una tabla dada su posicion
     * @param tableName
     * @param atributo
     * @return
     * @throws SQLException
     */
    private String buscarAtributo(String tableName,int atributo) throws DataException{
    	PreparedStatement ps = null;
    	String sNombreAtributo = "";
    	ResultSet rs = null;
    	try{
            if (conn == null){
            	conn = getDBConnection();
            }
	    	StringBuffer query = new StringBuffer("select a.attname from pg_attribute a,pg_class c where a.attrelid = c.oid and c.relname = ? and a.attnum = ? ");
	        ps = conn.prepareStatement(query.toString());
	        ps.setString(1,tableName);
	        ps.setInt(2,atributo);
	        rs = ps.executeQuery();
	        if (rs.next())
	        	sNombreAtributo = rs.getString("attname");
	        rs.close();
	        ps.close();
		}catch (Exception ex) {
	        throw new DataException(ex);            
	    }finally{
	    	try{rs.close();}catch(Exception e){throw new DataException(e);};
	  		try{ps.close();}catch(Exception e){throw new DataException(e);};
	  		try{conn.close();}catch(Exception e){throw new DataException(e);};
	    }
        return sNombreAtributo;
    }

    /**
     * Se mira los campos por los que esta compuesta la primary key
     * @param layerName
     * @param sb
     * @throws DataException
     */
    private void camposPrimaryKey(String layerName, StringBuffer sb) throws DataException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean restricciones = false;
        try {
            if (conn == null){
            	conn = getDBConnection();
            }
	    	StringBuffer query = new StringBuffer("select column_name,constraint_name from information_schema.constraint_column_usage where table_name = ?");
	        ps = conn.prepareStatement(query.toString());
	        ps.setString(1, layerName);
	        rs = ps.executeQuery();
	        while (rs.next()){
	        	if (rs.getString("constraint_name").equals(layerName+"_pkey")){
	        		if (!rs.getString("column_name").equals(LayersPanel.REVISION_ACTUAL)){
		            	if (!sb.toString().equals(""))
		            		sb.append(",");	   
		            	sb.append(rs.getString("column_name"));
	        		}
	        	}
	        }
        }catch(Exception e){
        	throw new DataException(e);
	    }finally{
	    	try{rs.close();}catch(Exception e){throw new DataException(e);};
	  		try{ps.close();}catch(Exception e){throw new DataException(e);};
	  		try{conn.close();}catch(Exception e){throw new DataException(e);};
	    }
    }
    
    /**
     * Comprueba si un determinado campo existe en una tabla
     * @param tableName
     * @param fieldName
     * @return
     */
    private boolean existeCampo(String tableName, String fieldName) throws DataException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            if (conn == null){
            	conn = getDBConnection();
            }
        	boolean restricciones = false;
        	StringBuffer query = new StringBuffer("select c.name from columns c,tables t where c.id_table = t.id_table and t.name = ? and c.name = ?");
        	ps = conn.prepareStatement(query.toString());
        	ps.setString(1, tableName);
        	ps.setString(2, fieldName);
        	rs = ps.executeQuery();
        	if (rs.next())
        		return true;
        	else
        		return false;
        }catch(Exception e){
        	throw new DataException(e);
        }finally{
        	try{rs.close();}catch(Exception e){throw new DataException(e);};
      		try{ps.close();}catch(Exception e){throw new DataException(e);};
      		try{conn.close();}catch(Exception e){throw new DataException(e);};
        }
    }
    
    /**
     * Se comprueba si la tabla actual tiene foreign keys que referencian a otras tablas con versionado
     * @param table
     * @param conn
     * @return
     * @throws SQLException
     */
    private boolean perteneceVersionadoAForeignKeys(String tableName) throws Exception{
    	StringBuffer sb = new StringBuffer();
    	List listaFK = new ArrayList();
    	ResultSet rs = null;
    	PreparedStatement ps = null;
    	try{
            if (conn == null){
            	conn = getDBConnection();
            }
	    	sb = sb.append("SELECT c.conname AS constraint_name,t.relname AS table_name,array_to_string(c.conkey, ' ') AS constraint_key,");
	    	sb = sb.append("t2.relname AS references_table,array_to_string(c.confkey, ' ') AS fk_constraint_key  FROM pg_constraint c ");
	    	sb = sb.append("LEFT JOIN pg_class t  ON c.conrelid  = t.oid LEFT JOIN pg_class t2 ON c.confrelid = t2.oid WHERE t.relname = ? ");
	    	sb = sb.append("and c.contype='f' ORDER BY t.relname");
	    	
	    	ps = conn.prepareStatement(sb.toString());
	    	ps.setString(1, tableName);
	    	rs = ps.executeQuery();
	    	String[] arrayFK = new String[2];
	    	if (rs.next()){
	    		arrayFK[0] = rs.getString("table_name");
	    		String[] arrayClaves = rs.getString("constraint_key").split(" ");
	    		int n = arrayClaves.length;
	    		StringBuffer sbClaves = new StringBuffer();
				Iterator itTablas = this.obtenerListaTablas().iterator();
				while(itTablas.hasNext()){
					Table table = (Table)itTablas.next();
					if (table.getName().equals(arrayFK[0])){
						for (int i=0;i<n;i++){
	    	    			String sColumna = buscarAtributo(arrayFK[0],Integer.parseInt(arrayClaves[i]));
	    	    			if (sColumna.equals(LayersPanel.REVISION_ACTUAL)){
	    	    				return true;
	    	    			}
	    				}
	    			}
	    		}
	    	}
	    }catch(Exception e){
	    	throw new DataException(e);
	    }finally{
	    	try{rs.close();}catch(Exception e){throw new DataException(e);};
	  		try{ps.close();}catch(Exception e){throw new DataException(e);};
	  		try{conn.close();}catch(Exception e){throw new DataException(e);};
	    }
    	return false;
    }
    
    
    
    /**
     * Construye un innerJoin con la tabla de versiones
     * @param tableName
     * @return
     */
    public String getJoinVersiones(String tableName, String oldQuery) {
        String join = null;
        StringBuffer queryBuffer = new StringBuffer(oldQuery);
        
        join = " INNER JOIN Versiones ON (" + tableName + "." + LayersPanel.REVISION_ACTUAL + " = Versiones.revision)";
        int index = queryBuffer.toString().toUpperCase().indexOf(" WHERE");
        queryBuffer.insert(index, join);
        
        return queryBuffer.toString();
    }
    
    public String getQueryCamposVersion(String tableName, String oldQuery) {
        
        StringBuffer query = new StringBuffer(oldQuery);
        int index = query.toString().toUpperCase().indexOf(" FROM");
        query.insert(index, ", \"" + tableName + "\"." + LayersPanel.REVISION_ACTUAL + ", \"" + tableName + "\"." + LayersPanel.REVISION_EXPIRADA);
        int index2 = query.toString().toUpperCase().indexOf(" GROUP BY");
        if (index2 != -1)
        	query.insert(query.length(), ", \"" + tableName + "\"." + LayersPanel.REVISION_ACTUAL + ", \"" + tableName + "\"." + LayersPanel.REVISION_EXPIRADA);
        return query.toString();
    }
    
    public String getQueryCamposTime (String tableName, String oldQuery) {
        StringBuffer query = new StringBuffer(oldQuery);
        int index = query.toString().toUpperCase().indexOf(" FROM");
        query.insert(index, " ,to_timestamp('%TIME%','yyyy-MM-dd HH24:MI:ss') as time");
        int index2 = query.toString().toUpperCase().indexOf(" GROUP BY");
        if (index2 == -1)
        	index2 = query.length();
        StringBuffer st = new StringBuffer(" and revision_actual<=(select coalesce(max(revision),0) from versiones v, tables t where t.id_table=v.id_table_versionada and t.name='");
        st = st.append(tableName);
        st = st.append("' and fecha<=CASE position('-' in '%TIME%') WHEN 5 THEN to_timestamp('%TIME%','yyyy-MM-dd HH24:MI:ss') ELSE localtimestamp END) ");
        st = st.append(" and revision_expirada>(select coalesce(max(revision),0) from versiones v, tables t where t.id_table=v.id_table_versionada and t.name='");
        st = st.append(tableName);
        st = st.append("' and fecha<=CASE position('-' in '%TIME%') WHEN 5 THEN to_timestamp('%TIME%','yyyy-MM-dd HH24:MI:ss') ELSE localtimestamp END) ");
    	query = query.insert(index2,st.toString());
        return query.toString();
    }
    
    /**
     * Obtenemos las fecha de la primera version
     * @return
     */
    public String getFechaPrimeraVersion() throws DataException{
	String fechaPrimeraVersion = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
        if (conn == null){
        	conn = getDBConnection();
        }
	    String query = "select fecha from versiones where versiones.revision = (select min(revision) from versiones)";
	    ps = conn.prepareStatement(query.toString());
	    rs = ps.executeQuery();
	    Timestamp fecha = null;
	    if (rs.next()) {
		fecha = rs.getTimestamp("fecha");
	    }

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    fechaPrimeraVersion = sdf.format(new Date(fecha.getTime()));
	} catch (Exception e) {
	    throw new DataException(e);
    }finally{
    	try{rs.close();}catch(Exception e){throw new DataException(e);};
  		try{ps.close();}catch(Exception e){throw new DataException(e);};
  		try{conn.close();}catch(Exception e){throw new DataException(e);};
    	return fechaPrimeraVersion;
    }

    }
    
    private String obtenerSecuenciaMunicipios(int idEntidad) throws DataException{
    	StringBuffer sb = new StringBuffer("");
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try{
            if (conn == null){
            	conn = getDBConnection();
            }
	    	ps = conn.prepareStatement("getMunicipiosEntidad");
	    	ps.setInt(1, idEntidad);
	    	rs = ps.executeQuery();
	    	while (rs.next()){
	    		if (!sb.toString().equals(""))
	    			sb = sb.append(",");
	    		sb = sb.append(rs.getInt("id"));
	    	}
		} catch (Exception e) {
		    throw new DataException(e);
	    }finally{
	    	try{rs.close();}catch(Exception e){throw new DataException(e);};
	  		try{ps.close();}catch(Exception e){throw new DataException(e);};
	  		try{conn.close();}catch(Exception e){throw new DataException(e);};
	    }
    	return sb.toString();
    }
    
    /**
     * Se comprueba que no exista otra capa versionable que comparta la misma tabla
     */
    public boolean compartenOtrasCapasTabla(Table table, Layer layer) throws DataException{
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try{
            if (conn == null){
            	conn = getDBConnection();
            }
	    	StringBuffer sb = new StringBuffer("select c.id_table,l.name from layers l, attributes a,columns c ");
	    	sb = sb.append("where l.id_layer = a.id_layer and a.id_column = c.id ");
	    	sb = sb.append("and a.position = 1 and l.name != ? and l.versionable=1 and c.id_table=?");
	    	ps = conn.prepareStatement(sb.toString());
	    	ps.setString(1, layer.getDescription());
	    	ps.setInt(2, table.getIdTabla());
	    	rs = ps.executeQuery();
	    	if (rs.next())
	    		return true;
	    	else
	    		return false;
    	} catch (Exception e) {
		    throw new DataException(e);
	    }finally{
	    	try{rs.close();}catch(Exception e){throw new DataException(e);};
	  		try{ps.close();}catch(Exception e){throw new DataException(e);};
	  		try{conn.close();}catch(Exception e){throw new DataException(e);};
	    }
    }
    
    /**
     * Metodo que mira si una capa es dinamica y, en caso que lo sea, obtiene
     * la informacion necesaria para su publicacion
     * @param idLayer
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public void setDynamicLayer(Layer layer, int idLayer, int idEntidad) throws DataException{
    	PreparedStatement s1 = null;
    	ResultSet r1 = null;
    	try{
            if (conn == null){
            	conn = getDBConnection();
            }
            //Miro si la capa es dinamica
            s1 = conn.prepareStatement("isDynamicLayer");
            s1.setInt(1, idLayer);                
            s1.setInt(2, idEntidad);                
            r1 = s1.executeQuery();
            if (r1.next()){
                layer.setDinamica(true);
                layer.setUrl(r1.getString("url"));
            }else{
            	layer.setDinamica(false);
            }
    	}catch(SQLException e){
    		throw new DataException(e);
	    }finally{
	    	try{r1.close();}catch(Exception e){throw new DataException(e);};
	  		try{s1.close();}catch(Exception e){throw new DataException(e);};
	  		try{conn.close();}catch(Exception e){throw new DataException(e);};
	    }
    }
    
    private void updateDynamicLayers(Map<Integer,LayerTable> map,int idEntidad) throws DataException{
    	PreparedStatement s1 = null;
    	ResultSet r1 = null;
    	try{
            if (conn == null){
            	conn = getDBConnection();
            }
            //Miro si la capa es dinamica
            s1 = conn.prepareStatement("getAllDynamicLayersEntidad");
            s1.setInt(1, idEntidad);                
            r1 = s1.executeQuery();
            while (r1.next()){
            	LayerTable layerTable=map.get(r1.getInt("id"));
            	if (layerTable!=null){
	            	Layer layer=layerTable.getLayer();
            		logger.info("Fijando layer dinamica para:"+layer.getName());
	                layer.setDinamica(true);
	                layer.setUrl(r1.getString("url"));
            	}
            }
    	}catch(SQLException e){
    		throw new DataException(e);
	    }finally{
	    	try{r1.close();}catch(Exception e){throw new DataException(e);};
	  		try{s1.close();}catch(Exception e){throw new DataException(e);};
	  		try{conn.close();}catch(Exception e){throw new DataException(e);};
	    }
    }

    class AclComparatorByDescripcion implements Comparator {
        public int compare(Object o1, Object o2) {
        	Acl c1 = (Acl) o1;
        	Acl c2 = (Acl) o2;
            
            String desc1 = c1.getNombre();
            String desc2 = c2.getNombre();
            
            
          //Los blancos al final
			if (desc1.equals(""))
				return 1;
			if (desc1.compareTo(desc2)>0)
				return 1;
			else if (desc1.compareTo(desc2)<0)
				return -1;
			else
				return 0;
        }
    };
        
    
    
    /**
     * Recupera las constraints de la tabla
     * @param tableName
     * @throws Exception
     */
    public List<PostgreSQLConstraint> getConstraints(String tableName) throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PostgreSQLConstraint> constratinsList = null;
        try {
            if (conn == null){
            	conn = getDBConnection();
            }
                        
         	ps = conn.prepareStatement("select co.conname, co.consrc, cl.relname FROM pg_class cl INNER JOIN pg_constraint co ON(cl.relfilenode=co.conrelid) WHERE contype='c' AND relname=?");
	    	ps.setString(1, tableName);
	    	rs = ps.executeQuery();
	    	if(!rs.isLast()){
	    		constratinsList = new ArrayList<PostgreSQLConstraint>();	    	
		    	if (rs.next()){
		    		PostgreSQLConstraint postgresConstraint = new PostgreSQLConstraint();	    
		    		postgresConstraint.setConstraintName(rs.getString(1));
		    		postgresConstraint.setSrc(rs.getString(2));
		    		postgresConstraint.setTableName(rs.getString(3));
		    		constratinsList.add(postgresConstraint);	    		
		    	}	    
	    	}
        }finally{
        	try{conn.close();}catch(Exception e){throw new DataException(e);};
        	try{rs.close();}catch(Exception e){throw new DataException(e);};
			try{ps.close();}catch(Exception e){throw new DataException(e);};
        }
        return constratinsList;
    }
    
	/**
     * Se crean las constraints de la tabla
     * @param tableName
     * @return boolean: resultado de la creacion del constraints
     * @throws Exception
     */
    public boolean crearConstraints(List<PostgreSQLConstraint> constratinsList) throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuffer query = null;
        try {
            if (conn == null){
            	conn = getDBConnection();
            }
            
            Iterator<PostgreSQLConstraint> itConstraint = constratinsList.iterator();
            if(itConstraint.hasNext()){
            	PostgreSQLConstraint postgreSQLConstraint = itConstraint.next();
            	
            	query = new StringBuffer("ALTER TABLE \"").append(postgreSQLConstraint.getTableName()).append("\" ADD CONSTRAINT \"").append(postgreSQLConstraint.getConstraintName())
	            		.append("\" CHECK \"").append(postgreSQLConstraint.getSrc()).append("\"");
				ps = conn.prepareStatement(query.toString());
				ps.executeUpdate();
				ps.close();             	
            }
                            
			return true;
        }catch(Exception ex){
        	logger.error(ex);
        }finally{
        	try{conn.close();}catch(Exception e){throw new DataException(e);};
        	try{rs.close();}catch(Exception e){throw new DataException(e);};
			try{ps.close();}catch(Exception e){throw new DataException(e);};
        }
        return false;
    }
    
}


