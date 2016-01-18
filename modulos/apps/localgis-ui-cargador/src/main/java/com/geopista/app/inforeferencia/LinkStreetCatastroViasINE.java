/**
 * LinkStreetCatastroViasINE.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inforeferencia;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.editor.GeopistaEditor;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class LinkStreetCatastroViasINE 
{
    
    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();
    private Blackboard blackboard = aplicacion.getBlackboard();
    
    public LinkStreetCatastroViasINE(){
        
    }
    
    public static void resetLinkStreet()
    {
        AppContext.getApplicationContext().getBlackboard().put("viasINEInsertadas",null);
        AppContext.getApplicationContext().getBlackboard().put("mostrarError", null);
        
        GeopistaEditor localEditor = (GeopistaEditor) AppContext.getApplicationContext().getBlackboard().get("geopistaEditorEnlazarPoliciaCalles");
        if(localEditor!=null) localEditor.reset();
        localEditor=null;
        AppContext.getApplicationContext().getBlackboard().put("geopistaEditorEnlazarPoliciaCalles",null);
    }
    
   
         
  
    /**
     * saca datos de viasINE y los almacena en un ArrayList
     * 
     * @return Connection, conexion
     */
    public static void createDatosViasINE()
    {
        try{
            Collection viasIneList=new ArrayList();
            int idMunicipio = Integer.parseInt(aplicacion
                    .getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
           
            Connection con = getDBConnection();
            PreparedStatement  ps = null;
            ResultSet r = null;
            
            ps = con.prepareStatement("selectViasIne");
            ps.setInt(1,idMunicipio);
            r=ps.executeQuery();
            while (r.next())
            {
                DatosViasINE viasine=new DatosViasINE();
//                viasine.setIdMunicipio(r.getString("id_municipio")==null?"":r.getString("id_municipio"));
//                viasine.setIdVia(r.getString("id_via")==null?"":r.getString("id_via"));
//                viasine.setNombreCorto(r.getString("nombrecortoine")==null?"":r.getString("nombrecortoine"));
//                viasine.setNombreVia(r.getString("nombreine")==null?"":r.getString("nombreine"));
//                viasine.setPosicionVia(r.getString("posiciontipovia")==null?"":r.getString("posiciontipovia"));
//                viasine.setTipoVia(r.getString("tipovia")==null?"":r.getString("tipovia"));
//                viasine.setCodigoViaINE(r.getString("codigoviaine")==null?"":r.getString("codigoviaine"));
                  viasine.setIdMunicipio(r.getString("id_municipio"));
                  viasine.setIdVia(r.getString("id_via"));
                  viasine.setNombreCorto(r.getString("nombrecortoine"));
                  viasine.setNombreVia(r.getString("nombreine"));
                  viasine.setPosicionVia(r.getString("posiciontipovia"));
                  viasine.setTipoVia(r.getString("tipovia"));
                  viasine.setCodigoViaINE(r.getString("codigoviaine"));
               
                viasIneList.add(viasine);
                
            }
            r.close();
            ps.close();
            //Si estamos ejecutando esta funcion significara que intentamos enlazar la 
            //calles de manera ajena al proceso de importacion por lo tanto el panel no 
            //tendra que realizar el enlazado automatico para lo que utilizaremos la variable noautolink
            
            AppContext.getApplicationContext().getBlackboard().put("noautolink",true);
            AppContext.getApplicationContext().getBlackboard().put("viasINEInsertadas",viasIneList);
            AppContext.getApplicationContext().getBlackboard().put("mostrarError", true);
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        
    }
    /**
     * Crea un geopista editor
     * 
     * @return Connection, conexion
     */
    public static void loadGeopistaEditor()
    {
        
        
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);

        progressDialog.setTitle(aplicacion.getI18nString("GeopistaLoadMapPlugIn.CargandoMapa"));
        progressDialog.report(aplicacion.getI18nString("GeopistaLoadMapPlugIn.CargandoMapa"));
        progressDialog.addComponentListener(new ComponentAdapter()
            {
                public void componentShown(ComponentEvent e)
                {
                    new Thread(new Runnable()
                        {
                            public void run()
                            {
                                try
                                {
                                    GeopistaEditor geopistaEditor = new GeopistaEditor(aplicacion.getString("fichero.vacio"));
                                    geopistaEditor.loadMap(aplicacion.getString("url.mapa.callejero"));
                                    AppContext.getApplicationContext().getBlackboard().put("geopistaEditorEnlazarPoliciaCalles",geopistaEditor);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }finally
                                {
                                    progressDialog.setVisible(false);
                                }
                            }
                        }).start();
                }
            });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
    }
    /**
     * Establece la conexion con la base de datos
     * 
     * @return Connection, conexion
     */
    public static Connection getDBConnection() throws SQLException
    {

        ApplicationContext app = AppContext.getApplicationContext();
        Connection conn = app.getConnection();
        conn.setAutoCommit(false);
        return conn;
    }
}
