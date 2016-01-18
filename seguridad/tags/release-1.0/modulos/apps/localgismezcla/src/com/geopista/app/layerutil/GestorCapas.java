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


package com.geopista.app.layerutil;


/**
 * Clase inicial de la aplicación del Gestor de Capas
 * 
 * @author cotesa
 *
 */
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.layerutil.datosexternos.DatosExternosPanel;
import com.geopista.app.layerutil.dbtable.TablesDBPanel;
import com.geopista.app.layerutil.domain.TablesDomainsPanel;
import com.geopista.app.layerutil.layer.LayersPanel;
import com.geopista.app.layerutil.layerfamily.LayerFamiliesPanel;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GestorCapas extends JFrame
{
	 private static Logger logger;
	    static {
	       createDir();
	       logger  = Logger.getLogger(GestorCapas.class);
	    }  		
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    /**
     * Número de intentos para hacer login en el gestor de capas antes de que se cierre  
     */
    private static final int MAX_ATTEMPS = 3;
    
    private JTabbedPane pestanaTables;
    
    
    
    /**
     * Constructor por defecto
     *
     */
    public GestorCapas()
    {
        try 
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            initComponents();             
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            System.err.println("Couldn't use system look and feel.");
        }       
        
    }
    public static void createDir(){
    	File file = new File("logs");

    	if (! file.exists()) {
    		file.mkdirs();
    	}
    }      
    /**
     * Inicialización de componentes
     */
    private void initComponents(){
        
        //Variables para control de refrescos de árboles entre los distintos paneles        
        Identificadores.put("TablasModificadas", false);
        Identificadores.put("DomainsModificados", false);
        Identificadores.put("CapasModificadas", false);  
        Identificadores.put("LayersActualizada", false);
        Identificadores.put("TablasDominiosActualizada", false);
        Identificadores.put("ColumnasBorradas", false);
        //------NUEVO------>
        if(SSOAuthManager.isSSOActive()) Constantes.url = aplicacion.getString(SSOAuthManager.SSO_SERVER_URL);
        else Constantes.url = aplicacion.getString("geopista.conexion.servidorurl")+"administracion";
        //----FIN NUEVO---->  
        Constantes.Locale = aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES");
      
        
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.layerutil.language.GestorCapasi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("GestorCapas",bundle);
        
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);        
             
        if (!aplicacion.isLogged())
        	aplicacion.login(); 
        
        /*int cont = 0;
        while(!aplicacion.isLogged() && cont<=MAX_ATTEMPS)
        {                     
            cont++;
            if (cont>MAX_ATTEMPS)
            {              
                System.exit(0);  
            }            
            aplicacion.login(); 
        }*/
        
        if (!aplicacion.isLogged()){
        	stopApp();
        	return;
        }
        
        aplicacion.setMainFrame(this);        
        
        progressDialog.setSize(480, 90);
        progressDialog.setTitle(aplicacion.getI18nString("CargandoDatosIniciales"));
        progressDialog.report(aplicacion.getI18nString("CargandoDatosIniciales"));    
        progressDialog.allowCancellationRequests();
        progressDialog.addComponentListener(new ComponentAdapter()
            {
                public void componentShown(ComponentEvent e)
                {
                    // Wait for the dialog to appear before starting the
                    // task. Otherwise
                    // the task might possibly finish before the dialog
                    // appeared and the
                    // dialog would never close. [Jon Aquino]
                    new Thread(new Runnable()
                        {
                            public void run()
                            {
                                try
                                {                             
                                	String title=I18N.get("GestorCapas","general.titulo");
                        			String release=aplicacion.getString("localgis.release");
                        			if (release==null)
                        				release="LocalGIS";
                        			title=title.replaceAll("\\$\\{localgis\\.release\\}",release);				
                        	        setTitle(title);
                                    //setTitle(I18N.get("GestorCapas","general.titulo"));
                                    
                                    addWindowListener( new WindowAdapter() {
                                        public void windowClosing( WindowEvent evt ) {
                                            System.exit(0);
                                        }
                                    } );
                                    
                                    setSize(1020,650);
                                    setVisible(true);
                                    toBack();
                                    
                                    //aplicacion.setMainFrame(this);
                                    
                                    pestanaTables = new JTabbedPane();
                                    pestanaTables.addChangeListener(new javax.swing.event.ChangeListener() {
                                        
                                        private FeatureExtendedPanel oldpanel=null;
                                        public void stateChanged(ChangeEvent e)
                                        {
                                            if (oldpanel!=null) oldpanel.exit();
                                            // Notifica a los paneles de los cambios de pestañas
                                            FeatureExtendedPanel panel=(FeatureExtendedPanel) pestanaTables.getSelectedComponent();
                                            if (panel!=null) panel.enter();
                                            oldpanel=panel;
                                        }
                                    });
                                        
                                    progressDialog.report(I18N.get("GestorCapas","cargar.panel.dominios"));
                                    TablesDomainsPanel paneltablasdominios = new TablesDomainsPanel();        
                                    pestanaTables.addTab(I18N.get("GestorCapas","general.pestana.dominios"),paneltablasdominios);
                                    
                                    if (progressDialog.isCancelRequested())
                                    {
                                        cancelarProceso(pestanaTables);
                                        return;
                                    }
                                        
                                    
                                    progressDialog.report(I18N.get("GestorCapas","cargar.panel.layers"));
                                    LayersPanel panelLayers = new LayersPanel();
                                    panelLayers.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                                    pestanaTables.addTab(I18N.get("GestorCapas","general.pestana.layers"), panelLayers);
                                    
                                    if (progressDialog.isCancelRequested())
                                    {
                                        cancelarProceso(pestanaTables);
                                        return;
                                    }
                                    
                                    progressDialog.report(I18N.get("GestorCapas","cargar.panel.layerfamilies"));
                                    LayerFamiliesPanel panelLayerFamilies = new LayerFamiliesPanel();
                                    panelLayerFamilies.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                                    pestanaTables.addTab(I18N.get("GestorCapas","general.pestana.layerfamilies"), panelLayerFamilies);
                                       
                                    if (progressDialog.isCancelRequested())
                                    {
                                        cancelarProceso(pestanaTables);
                                        return;
                                    }
                                    
                                    progressDialog.report(I18N.get("GestorCapas","cargar.panel.datosExternos"));
                                    DatosExternosPanel panelDatosExternos = new DatosExternosPanel();
                                    panelDatosExternos.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                                    pestanaTables.addTab(I18N.get("GestorCapas","general.pestana.datosExternos"), panelDatosExternos);
                                       
                                    if (progressDialog.isCancelRequested())
                                    {
                                        cancelarProceso(pestanaTables);
                                        return;
                                    }
                                    
                                    
                                    //se añade esta pestaña al final para evitar que solicite usuario y pass de la bd geopista
                                    progressDialog.report(I18N.get("GestorCapas","cargar.panel.tablasBD"));  
                                    TablesDBPanel paneltablasBD = new TablesDBPanel();        
                                    //pestanaTables.addTab(I18N.get("GestorCapas","general.pestana.tablas"),paneltablasBD);
                                    pestanaTables.insertTab(I18N.get("GestorCapas","general.pestana.tablas"),null, paneltablasBD,"",0);    
                                    
                                    
                                    getContentPane().add(pestanaTables, null);
                                    
                                } catch (Exception e)
                             
                                {
                                   	e.printStackTrace();
                                } finally
                                {
                                    progressDialog.setVisible(false);
                                }
                            }

                            private void cancelarProceso(JTabbedPane pestanaTables)
                            {
                                JOptionPane optionPane=
                                    new JOptionPane(I18N.get("GestorCapas","cargar.mensaje.no.finalizado"),
                                            JOptionPane.INFORMATION_MESSAGE);
                                JDialog dialog =optionPane.createDialog(aplicacion.getMainFrame(),"");
                                dialog.show();                                
                                getContentPane().add(pestanaTables, null);                                
                            }
                        }).start();
                }
            });
        GUIUtil.centreOnScreen(progressDialog);        
        progressDialog.setVisible(true);
        progressDialog.toFront();    
        
    }
    
    private void  stopApp(){
    	JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Inicio de aplicación cancelado. Se reiniciará el aplicativo");
    	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    	System.exit(1);
    }
    
    /**
     * Método principal
     * @param args
     */
    public static void main( String args[] ) {
        
        GestorCapas lf = new GestorCapas();
        lf.show();        
    }
    /**
     * Obtiene las pestañas de la aplicación
     * @return JTabbedPane con las pestañas de la aplicación
     */
    public JTabbedPane getPestanaTables()
    {
        return pestanaTables;
    }     
    
}
