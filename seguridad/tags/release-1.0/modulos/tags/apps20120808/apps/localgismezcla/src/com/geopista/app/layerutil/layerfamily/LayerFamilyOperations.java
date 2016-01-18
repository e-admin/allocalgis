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


package com.geopista.app.layerutil.layerfamily;


/**
 * Definición de métodos para la realización de operaciones sobre base de 
 * datos
 * 
 * @author cotesa
 *
 */


import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
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
import java.util.Arrays;

import com.geopista.app.AppContext;
import com.geopista.app.layerutil.GestorCapas;
import com.geopista.app.layerutil.dbtable.LoginDBDialog;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.feature.Attribute;
import com.geopista.feature.AutoFieldDomain;
import com.geopista.feature.BooleanDomain;
import com.geopista.feature.CodeBookDomain;
import com.geopista.feature.CodedEntryDomain;
import com.geopista.feature.DateDomain;
import com.geopista.feature.Domain;
import com.geopista.feature.NumberDomain;
import com.geopista.feature.StringDomain;
import com.geopista.feature.TreeDomain;
import com.geopista.model.LayerFamily;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.model.Layer;


public class LayerFamilyOperations
{
    /**
     * Conexión a base de datos
     */
    public Connection conn = null;
    /**
     * Conexión a base de datos sin pasar por el administrador de cartografía
     */
    public static Connection directConn = null;
    
    /**
     * Contexto de la aplicación
     */
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
     /**
     * Locale que identifica el idioma del usuario
     */
    private String locale = aplicacion.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY, GestorCapas.DEFAULT_LOCALE, false);
    public static Set familiasModificadas = new HashSet(); 
    //private static final String pass = aplicacion.getUserPreference("conexion.pass","",false);
    
    private static final int NO_RESULTS=-1;
    
    /**
     * Constructor por defecto
     *
     */
    public LayerFamilyOperations()
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
     * Obtiene una conexión a la base de datos
     * @return Conexión a la base de datos
     * @throws SQLException
     */
    private static Connection getDBConnection () throws SQLException
    {        
        Connection con=  aplicacion.getConnection();
        con.setAutoCommit(false);
        return con;
    }  
    
    
    /**
     * Obtiene una conexión directa a la base de datos, sin utilizar el administrador de 
     * cartografía
     * @return Conexión a la base de datos
     * @throws SQLException 
     */
    private static Connection getDirectConnection () throws SQLException
    {   
        if (directConn==null)
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
            
            directConn.setAutoCommit(true);
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
        
        try
        {
            
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("LMobtenertipodominionull");
            s.setInt(1, idDominio);
            
            
            r = s.executeQuery();  
            if (r.next())
            {           
                idTipo =r.getInt("type");
                
            }
            
            s.close();
            r.close(); 
            conn.close();
            
            dRet = obtenerDominioTipo(idDominio, idTipo, nombreDominio);
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
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
     * Obtiene un array de LayerFamily con todas las categorias o familias dadas de alta en el sistema
     * 
     * @return Array de LayerFamily
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public LayerFamily[] obtenerLayerFamilies () throws DataException
    {        
        LayerFamily[] fams;
        
        try
        { 
            Map map = new HashMap();
            
            //String query = "select layerfamilies.*, dictionary.traduccion, dictionary.locale from layerfamilies, dictionary where layerfamilies.id_name = dictionary.id_vocablo and (locale =? or locale ="es_ES") order by traduccion";
            PreparedStatement s = null;
            ResultSet r = null;
            
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
                    if (r.getString("locale").equals(GestorCapas.DEFAULT_LOCALE))
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
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("LMtraducciones");
            s.setInt(1, idVocablo);
            
            r = s.executeQuery();  
            while (r.next())
            {
                ht.put(r.getString(1), r.getString(2));       
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }
        
        return ht;
        
    }
    
  
    /**
     * Actualiza la tabla de traducciones del sistema (inserciones, eliminaciones y 
     * actualizaciones) para una entrada determinada
     * @param htTraducciones Tabla de traducciones, cuya clave son los distintos locales. Si es null
     * se tratará de un borrado
     * @param idVocablo Identificador de la entrada
     * @return Identificador del vocablo actualizado, o 0 en caso de error
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public int actualizarDictionary (Hashtable htTraducciones, int idVocablo) throws DataException
    {
        try
        {            
            //update dictionary set traduccion=? where id_vocablo=? and locale=?
            //insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),?,?)
            //insert into dictionary (id_vocablo,locale, traduccion) values (?,?,?)
            //select id_vocablo from dictionary where locale=? and traduccion=?
            //delete from dictionary where id_vocablo=?
            
            PreparedStatement s = null;
            ResultSet r = null;
            
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
                                GeopistaUtil.generarSQL (s);
                            
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
                            s = conn.prepareStatement("LMinsertarDiccionario");
                            s.setInt(1, idVocablo);
                            s.setString(2, locale);
                            s.setString(3, traduccion);
                            if (s.executeUpdate()>=0)   
                                GeopistaUtil.generarSQL (s);                            
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
                            GeopistaUtil.generarSQL(s);
                        
                        // Si no se realizan actualizaciones, habrá que realizar inserciones
                        if (iRes == 0)
                        {   
                            s=null;
                            s = conn.prepareStatement("LMinsertarDiccionario");
                            s.setInt(1, idVocablo);
                            s.setString(2, locale);
                            s.setString(3, traduccion);
                            if(s.executeUpdate()>=0)
                                GeopistaUtil.generarSQL(s);
                            
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
                    GeopistaUtil.generarSQL (s);
                
            }
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }
        
        return idVocablo;
        
    }
    
    
 
    /**
     * Elimina de la base de datos cualquier relación existente entre una categoría
     * de capas o layerfamily y todas las layers que contiene
     * 
     * @param idLayerFamily Identificador numérico de la categoría o layerfamily
     * @throws DataException Si se produce un error de acceso a datos 
     */
    
    public void eliminarRelacionesLayerFamily (int idLayerFamily) throws DataException
    {        
        try
        {             
            PreparedStatement s = null;
            
            s = conn.prepareStatement("LMeliminarRelacionesLayerFamily");
            s.setInt(1, idLayerFamily);
            if(s.executeUpdate()>=0)     
                GeopistaUtil.generarSQL (s);            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }       
    }
    
    /**
     * Elimina de la base de datos la relación existente entre una categoría
     * de capas o layerfamily y una capa o layer
     * 
     * @param idLayerFamily Identificador numérico de la categoría o layerfamily
     * @param idLayer Identificador numérico de la capa o layer
     * @throws DataException Si se produce un error de acceso a datos 
     * @return true si la operación se realiza con éxito
     */
    public boolean eliminarRelacionLayerFamilyLayer (int idLayerFamily, int idLayer) throws DataException
    {    
        boolean bRes= false;
        try
        {             
            PreparedStatement s = null;
            ResultSet r = null;
            
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
            
            int resultadoOperacion = 1;
            Iterator it = idCapas.iterator();
            while (it.hasNext())
            {
                //Si la asociación se realiza de forma incorrecta, se devuelve un 0
                //Si alguna de las asociaciones es 0, resultadoOperacion=0
                resultadoOperacion*=asociarLayerFamilyLayer(idLayerFamily, ((Integer)it.next()).intValue());
            }
            
            if (resultadoOperacion != 0)
                bRes=true;
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }        
        
        return bRes;
        
    }
    /**
     * Obtiene los identificadores de todas las categorías o layerfamilies
     * en las que está presente una capa
     * 
     * @param idLayer Identificador numérico de la capa
     * 
     * @return Vector de identificadores de layerfamilies
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public Vector obtenerLayerfamiliesConLayer (int idLayer) throws DataException
    {
        Vector vcIdFamily = new Vector();
        
        try
        {             
            PreparedStatement s = null;
            ResultSet r = null;
            
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
        }
        
        return vcIdFamily; 
        
    }
    
    /**
     * Realiza una asociación entre una capa y una categoría o layerfamily
     * 
     * @param idLayerFamily IDentificador numérico de la layerfamily
     * 
     * @param idLayer Identificador numérico de la layer
     * 
     * @return Posición en la que se inserta la capa dentro de la layerfamily, o 
     * 0 en caso de error 
     */
    public int asociarLayerFamilyLayer(int idLayerFamily, int idLayer) throws DataException
    {   
        int posicion = 0;
        try
        {             
            PreparedStatement s = null;
            ResultSet r = null;            
            
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
                GeopistaUtil.generarSQL (s);
        }
        catch (SQLException ex)
        {
            posicion=0;
            throw new DataException(ex);
        }
        
        return posicion;
    }
    
    /**
     * Recupera un array con todas las layerfamilies del sistema, con las traducciones 
     * de sus nombres ordenadas alfabéticamente
     * 
     * @return Array de LayerFamilyTable con todos los atributos disponibles de la layerfamily
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public LayerFamilyTable[] obtenerLayerFamilyTable() throws DataException
    {
        LayerFamilyTable[] lft; 
        
        try
        {
            //String query = "select id_layerfamily, id_name, locale, traduccion from layerfamilies, dictionary where layerfamilies.id_name= dictionary.id_vocablo order by id_layerfamily";
            PreparedStatement s = null;
            ResultSet r = null;
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
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();            
            
            Comparator layerFamilyComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    LayerFamilyTable c1 = (LayerFamilyTable) o1;
                    LayerFamilyTable c2 = (LayerFamilyTable) o2;
                    
                    String desc1="";                    
                    if(c1.getHtNombre().get(locale)!=null)
                        desc1 = c1.getHtNombre().get(locale).toString();
                    else if (c1.getHtNombre().get(GestorCapas.DEFAULT_LOCALE)!= null)
                        desc1 = c1.getHtNombre().get(GestorCapas.DEFAULT_LOCALE).toString();
                    else if (c1.getHtDescripcion().get(locale)!=null)
                        desc1 = c1.getHtDescripcion().get(locale).toString();
                    else if (c1.getHtDescripcion().get(GestorCapas.DEFAULT_LOCALE)!= null)
                        desc1 = c1.getHtDescripcion().get(GestorCapas.DEFAULT_LOCALE).toString();   
                    
                    String desc2="";                    
                    if(c2.getHtNombre().get(locale)!=null)
                        desc2 = c2.getHtNombre().get(locale).toString();
                    else if (c2.getHtNombre().get(GestorCapas.DEFAULT_LOCALE)!= null)
                        desc2 = c2.getHtNombre().get(GestorCapas.DEFAULT_LOCALE).toString();
                    else if (c2.getHtDescripcion().get(locale)!=null)
                        desc2 = c2.getHtDescripcion().get(locale).toString();
                    else if (c2.getHtDescripcion().get(GestorCapas.DEFAULT_LOCALE)!= null)
                        desc2 = c2.getHtDescripcion().get(GestorCapas.DEFAULT_LOCALE).toString();   
                    
                    
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
        }//catch
        
        return lft;
        
    }
    
    /**
     * Obtiene los identificadores de todas las layerfamilies y capas disponibles en
     * el sistema.
     *  
     * @return Mapa con todos los identificadores de las layerfamilies del sistema como
     * clave y una lista ordenada por posición de los identificadores de las capas que contiene
     * @throws DataException Si se produce un error de acceso a datos
     */
    public HashMap obtenerRelacionesLayerFamiliesLayers () throws DataException
    {   
        HashMap hmRelaciones = null;
        
        try
        {   
            hmRelaciones = new HashMap();
            
            PreparedStatement s = null;
            ResultSet r = null;            
            
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
            
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();            
            
            
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }
        
        return hmRelaciones;
    }
    
  
 
    /**
     * Crea una nueva layerfamily o categoría de capas en el sistema
     * 
     * @param newLayerfamily LayerFamilyTable con los datos de la layerfamily a generar
     * 
     * @return int Número de registros actualizados (-1 en caso de error)
     * 
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public int crearLayerFamily(LayerFamilyTable newLayerfamily) throws DataException
    {
        int iRes = NO_RESULTS;
        
        PreparedStatement s = null;
        //insert into layerfamilies (id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),?,? )       
        try
        {            
            int idName = actualizarDictionary(newLayerfamily.getHtNombre(),0);
            int idDescription = actualizarDictionary(newLayerfamily.getHtDescripcion(), 0);
            if (idName!=0 && idDescription!=0)
            {
                s = conn.prepareStatement("LMinsertarLayerFamily");
                s.setInt(1, idName);
                s.setInt(2, idDescription);
                iRes = s.executeUpdate();
                if (iRes>=0)
                    GeopistaUtil.generarSQL (s);                
            }
            
            int id_layerfamily = 0;
            
            s = null;
            ResultSet r = null;
            s = conn.prepareStatement("LMseleccionarIdFamiliaCapa");
            s.setInt(1, idName);
            s.setInt(2, idDescription);
            
            r = s.executeQuery();
            while (r.next())
            {
            	id_layerfamily = r.getInt(1);
            }  
            
            newLayerfamily.getLayerFamily().setSystemId(String.valueOf(id_layerfamily));            
        }  
        catch (SQLException ex)
        {            
            throw new DataException(ex);
        }  
        return iRes;        
    } 
    
    
    /**
     * Elimina una layerfamily o categoría de capas en el sistema
     * 
     * @param lft LayerFamilyTable con los datos de la layerfamily a eliminar
     * 
     * @return int Número de registros eliminados (-1 en caso de error)
     * 
     * @throws DataException Si se produce un error de acceso a datos 
     */
    
    public int eliminarLayerFamily(LayerFamilyTable ltf) throws DataException
    {
        int iRes = NO_RESULTS;
        
        PreparedStatement s = null;
        //delete from layerfamilies where id_layerfamily=?
        //delete from layerfamilies_layers_relations where id_layerfamily=?
        try
        {   
            //Eliminar las relaciones con las capas 
            s= conn.prepareStatement("LMeliminarRelacionesLayerFamily");
            s.setInt(1, ltf.getIdLayerFamily());
            iRes = s.executeUpdate();
            if (iRes>0)
                GeopistaUtil.generarSQL (s);   
            
            //Eliminar las traducciones de nombre y descripcion de la layerfamily
            s=null;
            s=conn.prepareStatement("LMeliminarTraduccionesLayerFamily");
            s.setInt(1,ltf.getIdLayerFamily());
            s.setInt(2,ltf.getIdLayerFamily());
            iRes = s.executeUpdate();
            if (iRes>0)
                GeopistaUtil.generarSQL (s);

            //Elimina la layerfamily
            s=null;
            s = conn.prepareStatement("LMeliminarLayerFamily");
            s.setInt(1, ltf.getIdLayerFamily());
            iRes = s.executeUpdate();
            if (iRes>0)
                GeopistaUtil.generarSQL (s);  
        }  
        catch (SQLException ex)
        {            
            throw new DataException(ex);
        }  
        return iRes;        
    }
    
    /**
     * Obtiene todos los mapas en los que se incluye una capa determinada
     * 
     * @param idLayer Identificador numérico de la capa
     * 
     * @return Vector con los identificadores de los mapas en los que se encuentra
     * la capa
     * 
     * @throws DataException Si se produce un error de acceso a datos
     */ 
    public Vector buscarMapasWithLayerFamily (int idLayerFamily) throws DataException
    {
        Vector vcIdMapas = new Vector();
        
        try
        {             
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("LMbuscarMapasWithLayerFamily");
            s.setInt(1, idLayerFamily);
            
            r = s.executeQuery();
            
            while (r.next())
            {
                vcIdMapas.add(new Integer(r.getInt(1)));
            } 
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);      
        }
        
        return vcIdMapas;
    }

    public boolean eliminarRelacionesMapas(int idLayerFamily) throws DataException
    {
        boolean bRes = true;
        
        try
        {             
            PreparedStatement s = null;
            s = conn.prepareStatement("LMeliminarRelacionStylesLayerFamily");
            s.setInt(1, idLayerFamily);
            
            if(s.executeUpdate()>=0)     
                GeopistaUtil.generarSQL (s);    
            
            
            s=null;
            s = conn.prepareStatement("LMeliminarRelacionMapasLayerFamily");
            s.setInt(1, idLayerFamily);
            
            if(s.executeUpdate()>=0)     
                GeopistaUtil.generarSQL (s);   
        }
        catch (SQLException ex)
        {
            bRes = false;
            throw new DataException(ex);
        }
        
        return bRes;
    }
    
}


