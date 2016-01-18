/**
 * GeopistaEditor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MenuBar;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.awt.geom.NoninvertibleTransformException;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.GeopistaXMLProperties;
import com.geopista.app.AppContext;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaLayerManager;
import com.geopista.model.GeopistaListener;
import com.geopista.model.GeopistaMap;
import com.geopista.ui.GeopistaTreeLayerNamePanel;
import com.geopista.ui.LayerNameTabbedPanel;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaMapUtil;
import com.geopista.util.GeopistaStreamHandleFactory;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.geom.CoordUtil;
import com.vividsolutions.jump.geom.EnvelopeUtil;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.CollectionUtil;
import com.vividsolutions.jump.util.LangUtil;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.JUMPWorkbench;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.WorkbenchProperties;
import com.vividsolutions.jump.workbench.driver.DriverManager;
import com.vividsolutions.jump.workbench.model.Category;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.FeatureEventType;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.ITask;
import com.vividsolutions.jump.workbench.model.ITaskNameListener;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerEventType;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.model.LayerTreeModel;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.model.Task;
import com.vividsolutions.jump.workbench.model.UndoableEditReceiver;
import com.vividsolutions.jump.workbench.model.WMSLayer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.PlugInManager;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.EnableableToolBar;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.HTMLFrame;
import com.vividsolutions.jump.workbench.ui.IAbstractSelection;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.InfoFrame;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelListener;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelContext;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelListener;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelProxy;
import com.vividsolutions.jump.workbench.ui.PrimaryInfoFrame;
import com.vividsolutions.jump.workbench.ui.RecursiveKeyListener;
import com.vividsolutions.jump.workbench.ui.SelectionManager;
import com.vividsolutions.jump.workbench.ui.SelectionManagerProxy;
import com.vividsolutions.jump.workbench.ui.TaskFrame;
import com.vividsolutions.jump.workbench.ui.TaskFrameProxy;
import com.vividsolutions.jump.workbench.ui.TitledPopupMenu;
import com.vividsolutions.jump.workbench.ui.TrackedPopupMenu;
import com.vividsolutions.jump.workbench.ui.ViewportListener;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrame;
import com.vividsolutions.jump.workbench.ui.WorkbenchToolBar;
import com.vividsolutions.jump.workbench.ui.WorkbenchToolBar.ToolConfig;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;
  

public class GeopistaEditor extends JPanel 
		implements JUMPWorkbench ,LayerViewPanelContext,
					WorkBench, WorkbenchGuiComponent,ViewportListener,
					TaskComponent, SelectionManagerProxy,LayerManagerProxy,
					LayerNamePanelProxy,TaskFrameProxy, LayerViewPanelProxy, LayerListener
{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(GeopistaEditor.class);
    HashMap toolBarMap = new HashMap();
  	private InfoFrame infoFrame = null;
    private BorderLayout borderLayout1 = new BorderLayout();
    private JFrame mostrar = new JFrame();
    private JLabel statusLabel = new JLabel();
    private JSplitPane splitPane = new JSplitPane();
    private JPanel jViewPanel = new JPanel();
    private ErrorHandler errorHandler;
    private PlugInManager plugInManager;
    private static Class progressMonitorClass = SingleLineProgressMonitor.class;
    private TaskMonitor monitor = null;
    private Set choosableStyleClasses = new HashSet();
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
   	private Blackboard blackboardInformes = aplicacion.getBlackboard();
    private URLStreamHandlerFactory geopistaFactory = null;
    private Vector listaToolConfig=new Vector();
	//private JPopupMenu popupMenu = new TrackedPopupMenu();
	private TrackedPopupMenu popupMenu = new TrackedPopupMenu();
    private WorkbenchToolBar toolBar = new WorkbenchToolBar(
    		new LayerViewPanelProxy()
				{
    			public 	LayerViewPanel getLayerViewPanel()
    				{
    				return layerViewPanel;
    				}
    			});
    // REVISAR: El LayerManager está en el objeto Task [Juan Pablo]
    //private LayerManager layerManager = new LayerManager();
    private WorkbenchProperties dummyProperties = new WorkbenchProperties() {
            public List getPlugInClasses() {
                return new ArrayList();
            }

            public List getInputDriverClasses() {
                return new ArrayList();
            }

            public List getOutputDriverClasses() {
                return new ArrayList();
            }

            public List getConfigurationClasses() {
                return new ArrayList();
            }
        };

    private WorkbenchProperties properties = dummyProperties;
   
    
    private LayerNamePanel layerNamePanel;
    private JPanel centrePanel = new JPanel();
    private LayerViewPanel layerViewPanel;
    private BorderLayout borderLayout2 = new BorderLayout();
    private JPanel toolbarPanel = new JPanel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private Blackboard blackboard = new Blackboard();
    private ArrayList nameListeners = new ArrayList();

    private HashMap keyCodeAndModifiersToPlugInAndEnableCheckMap = new HashMap();
    private ArrayList easyKeyListeners = new ArrayList();

    private GeopistaContext workbenchContext = new GeopistaContext(this);
  	private GeopistaMap task = new GeopistaMap(workbenchContext); // Representa en proyecto con el que se trabaja en este componente
    private GeopistaLayerManager layerManager = (GeopistaLayerManager) task.getLayerManager();

    FeatureInstaller featureInstaller = new FeatureInstaller(workbenchContext);

    private ArrayList geopistaListeners = new ArrayList();
    private ArrayList layerListeners = new ArrayList();
    private boolean firingEvents = true;

    private TitledPopupMenu wmsLayerNamePopupMenu = new TitledPopupMenu()
    {
    	{
            addPopupMenuListener(new PopupMenuListener()
                {
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e)
                    {
                        LayerNamePanel panel = ((LayerNamePanelProxy) GeopistaEditor.this).getLayerNamePanel();
                        setTitle((panel.selectedNodes(WMSLayer.class).size() != 1) ? ("(" //$NON-NLS-1$
                                + panel.selectedNodes(WMSLayer.class).size() + AppContext
                                .getApplicationContext().getI18nString(
                                        "GeopistaWorkbenchFrame.WMS_layer_selected")) //$NON-NLS-1$
                                : ((Layerable) panel.selectedNodes(WMSLayer.class)
                                        .iterator().next()).getName());
                    }

                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
                    {
                    }

                    public void popupMenuCanceled(PopupMenuEvent e)
                    {
                    }
                });
        }
    };
        
    private TitledPopupMenu layerNamePopupMenu = new TitledPopupMenu() 
    {
            {
                addPopupMenuListener(new PopupMenuListener()
                {
                        public void popupMenuWillBecomeVisible(PopupMenuEvent e)
                        {
                            LayerNamePanel panel = ((LayerNamePanelProxy) GeopistaEditor.this).getLayerNamePanel();
                            setTitle((panel.selectedNodes(Layer.class).size() != 1)
                                ? ("(" +
                                panel.selectedNodes(Layer.class).size() +
                                " layers selected)")
                                : ((Layerable) panel.selectedNodes(Layer.class)
                                                    .iterator().next()).getName());
                        }

                        public void popupMenuWillBecomeInvisible(
                            PopupMenuEvent e) {
                        }

                        public void popupMenuCanceled(PopupMenuEvent e) {
                        }
                    });
            }
        };

        private TitledPopupMenu categoryPopupMenu = new TitledPopupMenu()
        {
            {
                addPopupMenuListener(new PopupMenuListener()
                    {
                        public void popupMenuWillBecomeVisible(PopupMenuEvent e)
                        {
                            LayerNamePanel panel = ((LayerNamePanelProxy) GeopistaEditor.this)
                                    .getLayerNamePanel();
                            setTitle((panel.selectedNodes(Category.class).size() != 1) ? ("(" //$NON-NLS-1$
                                    + panel.selectedNodes(Category.class).size() + AppContext
                                    .getApplicationContext().getI18nString(
                                            "GeopistaWorkbenchFrame.categories_selected")) //$NON-NLS-1$
                                    : ((Category) panel.selectedNodes(Category.class)
                                            .iterator().next()).getName());
                        }

                        public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
                        {
                        }

                        public void popupMenuCanceled(PopupMenuEvent e)
                        {
                        }
                    });
            }
        };

            
        
        private Map nodeClassToLayerNamePopupMenuMap = CollectionUtil.createMap(new Object[] {
                Layerable.class, layerNamePopupMenu, WMSLayer.class,
                wmsLayerNamePopupMenu, Category.class, categoryPopupMenu });

    /**
     * Eliminamos las capas que no pertenecen a la categoria indicada.
     * @param category
     */
    public ActualLayers reset(String[] categoryName)
    {

      ILayerManager resetLayerManager = workbenchContext.getLayerManager();
      List resetList = resetLayerManager.getLayers();
      Iterator resetListIterator = resetList.iterator();
      
      ActualLayers actualLayers=new ActualLayers();
      
      while (resetListIterator.hasNext())
      {
        Layerable resetLayer = (Layerable) resetListIterator.next();
        
        boolean encontrado=false;
        Category category=resetLayerManager.getCategory(resetLayer);
        if (categoryName!=null){
        	for (int i=0;i<categoryName.length;i++){	        
	        	if (category.getName().equals(categoryName[i])){
	        		encontrado=true;
	        		actualLayers.addLayer(category.getName(),resetLayer);
	        	}
        	}
        	//if (!encontrado)
        		resetLayerManager.remove(resetLayer);
        }
      }
      
      //Borrado de capas WMS.
      try {
		resetList = resetLayerManager.getWMSLayers();
		  resetListIterator = resetList.iterator();
		  while (resetListIterator.hasNext())
		  {
			boolean encontrado=false;
		    Layerable resetLayer = (Layerable) resetListIterator.next();
	        Category category=resetLayerManager.getCategory(resetLayer);
	        if (categoryName!=null){
	        	for (int i=0;i<categoryName.length;i++){	        
		        	if (category.getName().equals(categoryName[i])){
		        		encontrado=true;
		        		actualLayers.addLayer(category.getName(),resetLayer);
		        	}
	        	}
	        	//if (!encontrado)
	        		resetLayerManager.remove(resetLayer);
	        }
		  }
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
      removeAllCategories(layerManager);
      
      return actualLayers;
    }
    
    public void reset()
    {

      ILayerManager resetLayerManager = workbenchContext.getLayerManager();
      List resetList = resetLayerManager.getLayers();
      Iterator resetListIterator = resetList.iterator();
      while (resetListIterator.hasNext())
      {
        Layerable resetLayer = (Layerable) resetListIterator.next();
        
        Category category=resetLayerManager.getCategory(resetLayer);
        resetLayerManager.remove(resetLayer);
      }
            
      //Borrado de capas WMS.
      try {
		resetList = resetLayerManager.getWMSLayers();
		  resetListIterator = resetList.iterator();
		  while (resetListIterator.hasNext())
		  {			
		    Layerable resetLayer = (Layerable) resetListIterator.next();	        
	        resetLayerManager.remove(resetLayer);
	        
		  }
      }
      catch (Exception e){
    	  
      }
           
      removeAllCategories(layerManager);
    }
    
    /**
     * inicializa una instancia con modelo compartido
     * @param task
     */
   public GeopistaEditor(ITask task)
   {
	   
   setTask(task);
   String plugInFileConfiguration = aplicacion.getString("plugInFileConfiguration");


    inicializarEditor(plugInFileConfiguration);
   }
    public GeopistaEditor()
    {
    	
     String plugInFileConfiguration = aplicacion.getString("plugInFileConfiguration");
    
     inicializarEditor(plugInFileConfiguration);
    }

    public GeopistaEditor(String xmlPluginFile)
    {
            inicializarEditor(xmlPluginFile);
    }

    public void inicializarEditor(String xmlPluginFile)
    {
    	     	
    	initViewer();
    	//linea necesaria para que funcionen los ThreadedPlugins
    	toolBar.setTaskMonitorManager(new TaskMonitorManager());
      Blackboard localBlackboard = aplicacion.getBlackboard();
        geopistaFactory = (URLStreamHandlerFactory)localBlackboard.get("urlProtocol");
        if(geopistaFactory==null)
        {
          geopistaFactory = new GeopistaStreamHandleFactory();
          try {
              //para el entorno gráfico
              if(xmlPluginFile!=null && xmlPluginFile.length()>0){
            	  URL.setURLStreamHandlerFactory(geopistaFactory);
              }
          } catch (Error e) {
        	  logger.error("URL.setURLStreamHandlerFactory(geopistaFactory) -> Ya se ha creado la factoria.");
          }
          
          localBlackboard.get("urlProtocol",geopistaFactory);
        }
          try
        {
          initLookAndFeel();
        }catch(Exception e)
        {
        logger.error("inicializarEditor(String)", e);
        }

        try
        {
             monitor = (ProgressMonitor) progressMonitorClass.newInstance();
        }catch(Exception e)
        {
        logger.error("inicializarEditor(String)", e);
        }
        
        try
        {
        	
            //cargamos los plugin definidos en el fichero xml que sacamos del
            //fichero properties.
            
        //para el entorno gráfico
        if(xmlPluginFile!=null && xmlPluginFile.length()>0){
        	properties = new GeopistaXMLProperties(xmlPluginFile, errorHandler);
	        plugInManager = new PlugInManager(workbenchContext, null/*new File("./lib/ext")*/, monitor);
	        getPlugInManager().load();
        }
          
        }catch(Exception e)
        {
        	logger.error("inicializarEditor(String)",e);
        }
       
        task.setName("Default Map");
        task.setExtracted(true);
        task.setTimeStamp(new Date());
        task.setDescription("Geopista Default Map");
        layerManager.addCategory(StandardCategoryNames.SYSTEM); // Añade la categoría por defecto
         
    }
private void initViewer()
{
    
    if(aplicacion.getMainFrame() instanceof ErrorHandler)
        this.errorHandler = (ErrorHandler) aplicacion.getMainFrame();
    layerViewPanel = new LayerViewPanel(layerManager, this);
   
    layerNamePanel = createLayerNamePanel();


    try {
        jbInit();
    } catch (Exception ex) {
        logger.error("initViewer()", ex);
    }

    



    new RecursiveKeyListener(this) {
            public void keyTyped(KeyEvent e) {
                for (Iterator i = easyKeyListeners.iterator(); i.hasNext();) {
                    KeyListener l = (KeyListener) i.next();
                    l.keyTyped(e);
                }
            }

            public void keyPressed(KeyEvent e) {
                for (Iterator i = new ArrayList(easyKeyListeners).iterator();
                        i.hasNext();) {
                    KeyListener l = (KeyListener) i.next();
                    l.keyPressed(e);
                }
            }

            public void keyReleased(KeyEvent e) {
                for (Iterator i = new ArrayList(easyKeyListeners).iterator();
                        i.hasNext();) {
                    KeyListener l = (KeyListener) i.next();
                    l.keyReleased(e);
                }
            }
        };
        installKeyboardShortcutListener();

        getLayerManager().getUndoableEditReceiver()
             .add(new UndoableEditReceiver.Listener() {
            public void undoHistoryChanged() {
                toolBar.updateEnabledState();
            }

            public void undoHistoryTruncated() {
                toolBar.updateEnabledState();
                log("Undo history was truncated");
            }
        });

        inicializarEventos();
}
    private void inicializarEventos()
    {
    
      layerViewPanel.addListener(new LayerViewPanelListener() {
            public void painted(Graphics graphics) {}

            public void selectionChanged() {
               
            }

            public void cursorPositionChanged(String x, String y) {
                setStatusMessage("(" + x + ", " + y + ")");
            }
            
            public void componentShown(ComponentEvent e){
            	
            }
        });
    
      layerViewPanel.getLayerManager().addLayerListener(new LayerListener()
      {
        
        public void featuresChanged(FeatureEvent e)
        {
          FeatureEventType tipoEvento = e.getType();
          if(tipoEvento==FeatureEventType.ADDED)
          {
               fireGeopistaFeatureAdded(e);
          }
          if(tipoEvento==FeatureEventType.DELETED)
          {
              fireGeopistaFeatureRemoved(e);
          }
          if(tipoEvento==FeatureEventType.GEOMETRY_MODIFIED)
          {
              fireGeopistaFeatureModified(e);
          }
          if(tipoEvento==FeatureEventType.ATTRIBUTES_MODIFIED)
          {
              fireGeopistaFeatureModified(e);
          }

        }

        public void layerChanged(LayerEvent e){
        	LayerEventType tipoEvento = e.getType();
        	if(tipoEvento == LayerEventType.METADATA_CHANGED)
        	{
        		((EnableableToolBar) toolBar).updateEnabledState();
        	}
        	 
        }

        public void categoryChanged(CategoryEvent e){}
      });
    }
    
    

    public void handleThrowable(final Throwable t) {
        if (errorHandler!=null)
        	errorHandler.handleThrowable(t);
    }

    public void warnUser(String warning) {
        setStatusMessage(warning);
    }

    public void setStatusMessage(String message) {

        statusLabel.setText(
            ((message == null) || (message.length() == 0)) ? " " : message);
    }

    void jbInit() throws Exception {


        
        
        this.setLayout(borderLayout1);
        centrePanel.setLayout(borderLayout2);
 
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                           (JPanel) layerNamePanel,layerViewPanel);
        splitPane.setOneTouchExpandable(true);
        
        splitPane.setDividerLocation(175);

        jPanelStatus.setLayout(new BorderLayout());
        jPanelStatus.add(statusLabel, BorderLayout.WEST);
        statusLabel.setText(" ");
       
        jPanelStatus.add(timeLabel, BorderLayout.EAST);
        statusLabel.setText(" ");

        add(jPanelStatus, BorderLayout.SOUTH);

        this.add(centrePanel, BorderLayout.CENTER);
        centrePanel.add(toolbarPanel,BorderLayout.NORTH);
        
        centrePanel.add(splitPane,BorderLayout.CENTER);
        toolBar.setTaskMonitorManager(new TaskMonitorManager());
     
        mostrar.setTitle("GeoPISTA Editor");
      
        toolbarPanel.add(
            toolBar,
            new GridBagConstraints(
                0,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));

      

    }

    public void addPlugIn(String nombreClase)
    {
      addPlugIn(nombreClase,null);
    }

    public void addPlugIn(String nombreClase,String icono)
    {

      try
      {
        PlugIn cargandoPlugin = (PlugIn) (Class.forName(nombreClase)).newInstance();
       
        addPlugIn(cargandoPlugin,icono);

      }catch(ClassNotFoundException e)
      {
        logger.error("addPlugIn(String, String)", e);
      }catch(IllegalAccessException e)
      {
        logger.error("addPlugIn(String, String)", e);
      }catch(InstantiationException e)
      {
        logger.error("addPlugIn(String, String)", e);
      }catch(Exception e)
      {
        logger.error("addPlugIn(String, String)", e);
      }
    }

    public void addPlugIn(PlugIn plugin)
    {
    	addPlugIn(plugin, null);
    }

    public void addPlugIn(PlugIn plugin, String icono)
    {
    	try
        {
    		
    		Icon iconoIcon = null;
    		
          if(icono!=null)
          {
        	  iconoIcon = IconLoader.icon(icono);
          }
          	toolBar.setTaskMonitorManager(new TaskMonitorManager());
          
          
          

          plugin.initialize(new PlugInContext(workbenchContext, getTask(),this , layerNamePanel, layerViewPanel));
          
          if(icono!=null)
          {
        	  toolBar.addPlugIn(
            	  iconoIcon,
                  plugin,
                  new MultiEnableCheck(),
                  workbenchContext);
          }


        }catch(ClassNotFoundException e)
        {
          logger.error("addPlugIn(String, String)", e);
        }catch(IllegalAccessException e)
        {
          logger.error("addPlugIn(String, String)", e);
        }catch(InstantiationException e)
        {
          logger.error("addPlugIn(String, String)", e);
        }catch(Exception e)
        {
          logger.error("addPlugIn(String, String)", e);
        }

      
    }

    public void addCursorTool(String texto, String nombreClase)
    {
      try
      {
    	ToolConfig tool=toolBar.addCursorTool(texto, (CursorTool)(Class.forName(nombreClase)).newInstance() );
        listaToolConfig.add(tool);
      }catch(ClassNotFoundException e)
      {
        logger.error("addCursorTool(String, String)", e);
      }catch(IllegalAccessException e)
      {
        logger.error("addCursorTool(String, String)", e);
      }catch(InstantiationException e)
      {
        logger.error("addCursorTool(String, String)", e);
      }      
      
    }

    public Layer loadData(String layer, String categoria) throws Exception {
      ArrayList layers = new ArrayList();
      ArrayList categoriaArray = new ArrayList();
      layers.add(layer);
      categoriaArray.add(categoria);
      ArrayList nuevaCapa = loadData(layers,categoriaArray);
      return (Layer) nuevaCapa.get(0);
    }

   

    public ArrayList loadData(ArrayList layers, ArrayList categorias) throws Exception {
       
        Iterator layersIter = layers.iterator();
        Iterator categoriasIter = categorias.iterator();
        ArrayList nuevasCapas = new ArrayList();
        while(layersIter.hasNext())
        {
        
          File fileLayer = new File((String) layersIter.next());
          String categoria = (String) categoriasIter.next();
          String extension = getExtension(fileLayer);
          String driver = aplicacion.getString(extension);
         
          DataSourceQuery dataSourceQuery = toDataSourceQuery(fileLayer,Class.forName(driver));
        
        
          Connection connection = dataSourceQuery.getDataSource().getConnection();
          FeatureCollection dataset = connection.executeQuery(dataSourceQuery.getQuery(),monitor);

          Layer nueva = layerManager.addLayer(categoria,dataSourceQuery.toString(),dataset);
          nueva.setDataSourceQuery(dataSourceQuery);
          nueva.setFeatureCollectionModified(false);
          task.getLayerManager().addCategory(categoria);
          Category nuevaCategoria = task.getLayerManager().getCategory(categoria);
          
          nuevasCapas.add(nueva);
          
        }
        return nuevasCapas;
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }


    protected DataSourceQuery toDataSourceQuery(File file,Class dataSourceClass) {
        
        DataSource dataSource = (DataSource) LangUtil.newInstance(dataSourceClass);
        dataSource.setProperties(toProperties(file));
        return new DataSourceQuery(dataSource, null,
        GUIUtil.nameWithoutExtension(file));
    }

    protected Map toProperties(File file) {
        HashMap properties = new HashMap();
        properties.put(DataSource.FILE_KEY, file.getPath());
        properties.put(DataSource.COORDINATE_SYSTEM_KEY,
            CoordinateSystem.UNSPECIFIED.getName());
        return properties;
    }
    

/**
 * Borra todas las categorías qeu estén vacías excepto la que se llama "System"
 * que debe estar siempre.
 * @param layerManager
 */

    private void removeAllCategories(LayerManager layerManager) {


    //Creamos una nueva lista independiente para evitar ConcurrentModificationException
    List tempList = new ArrayList();
    tempList.addAll(layerManager.getCategories());
        for (Iterator i = tempList.iterator(); i.hasNext();) {
            Category cat = (Category) i.next();
            if(! cat.getName().equals(StandardCategoryNames.SYSTEM))
            	layerManager.removeIfEmpty(cat);
        }
        
    }

    public void showLayerName(boolean showNames)
    {
      if(showNames)
      {
         splitPane.setLeftComponent((JPanel)layerNamePanel);
         splitPane.setDividerSize(10);         
      }
      else
      {
          splitPane.remove((JPanel)layerNamePanel);  
          splitPane.setDividerSize(0);
      }

    }
    
    public void hideLayerNamePanel(){
    	
    	splitPane.setDividerLocation(0);
    }

    public LayerViewPanel getLayerViewPanel()
    {
      return layerViewPanel;
    }

    public LayerNamePanel getLayerNamePanel()
    {
      return layerNamePanel;
    }

    public LayerManager getLayerManager()
    {
      return layerManager;
    }

    public PlugInManager getPlugInManager()
    {
      return plugInManager;
    }

     public WorkbenchGuiComponent getGuiComponent()
     {
       return this;
     }

      public Blackboard getBlackboard()
      {
        return blackboard;
      }
      
      public WorkbenchProperties getProperties()
      {
        return properties;
      }

      /* Interfaz ViewportListerner */

      public void zoomChanged(Envelope modelEnvelope)
      {
        
      }
      
            /* Fin interfaz ViewportListener */

      /* Intefaz WorkbenchGuiComponent */

      public TitledPopupMenu getCategoryPopupMenu() {
        return categoryPopupMenu;
      }

      public TitledPopupMenu getWMSLayerNamePopupMenu() {         
    	  return wmsLayerNamePopupMenu;
      }
      
      public TitledPopupMenu getLayerNamePopupMenu() {
        return layerNamePopupMenu;
      }

      public WorkbenchToolBar getToolBar() {
        return toolBar;
      }

      public HTMLFrame getOutputFrame() {
        return null;
      }

      public JInternalFrame getActiveInternalFrame() {
    	  return new TaskFrame(task,workbenchContext);
      }
      public TaskComponent getActiveTaskComponent() {
      return this;
    }

      public Container getDesktopPane() {
        return this.splitPane;
      }

      public void addChoosableStyleClass(Class choosableStyleClass) {
      } 

      public void setVisible(boolean visible)
      {
     
      }

      public void log(String message) {
 
      }

      public Map getNodeClassToPopupMenuMap() {
        return nodeClassToLayerNamePopupMenuMap;
      }
public ApplicationContext getAppContext(){
	return workbenchContext;
}
      public WorkbenchContext getContext() {
        return workbenchContext;
      }

      public void removeInternalFrame(JInternalFrame internalFrame) {
      }
      
      public boolean hasInternalFrame(JInternalFrame internalFrame) {
        return false;
      }

      public void addWindowListener(WindowListener l)
      {
        
      }
      
      // Este método solo se llama para crear subventanas en GeopistaEditor
      // El interfaz usa JInternalFrame en lugar de TaskComponent ya que no se
      // va a tener más de un TaskComponent en cada instancia
      public void addInternalFrame(final JInternalFrame internalFrame) {
        addInternalFrame(internalFrame, false, true);
        }
      public void addInternalFrame(final JInternalFrame internalFrame, boolean alwaysOnTop, boolean autoUpdateToolBar)
      {
          /*if (internalFrame instanceof LayerManagerProxy) {
            setClosingBehaviour((LayerManagerProxy) internalFrame);
            installTitleBarModifiedIndicator((LayerManagerProxy) internalFrame);
        }*/
        

        //<<TODO:IMPROVE>> Listen for when the frame closes, and when it does,
        //activate the topmost frame. Because Swing does not seem to do this
        //automatically. [Jon Aquino]
        /*internalFrame.setFrameIcon(icon);*/

        //Call JInternalFrame#setVisible before JDesktopPane#add; otherwise, the
        //TreeLayerNamePanel starts too narrow (100 pixels or so) for some reason.
        //<<TODO>>Investigate. [Jon Aquino]
        /*JFrame framePadre = (JFrame) SwingUtilities.getAncestorOfClass(Frame.class,(Component) this);
     framePadre.getContentPane().add(((JInternalFrame)internalFrame));
        ((JInternalFrame)internalFrame).setVisible(true);*/
            	    	  
    	  mostrar.getContentPane().removeAll();
    	  mostrar.getContentPane().add((JInternalFrame) internalFrame);
    	  mostrar.setSize(500,500);
    	  mostrar.setVisible(true);
    	  internalFrame.setVisible(true);
    	  internalFrame.setResizable(false);
    	  internalFrame.setClosable(false);
    	  internalFrame.setMaximizable(false);
    	  internalFrame.setIconifiable(false);    	        
        
        //desktopPane.add((JInternalFrame)internalFrame,
        //mostrar.getContentPane().add((JInternalFrame)internalFrame,
        //    alwaysOnTop ? JLayeredPane.PALETTE_LAYER : JLayeredPane.DEFAULT_LAYER);
        /*if (autoUpdateToolBar) {
            internalFrame.addInternalFrameListener(new InternalFrameListener() {
                    public void internalFrameActivated(InternalFrameEvent e) {
                        toolBar.updateEnabledState();

                        //Associate current cursortool with the new frame [Jon Aquino]
                        toolBar.reClickSelectedCursorToolButton();
                    }

                    public void internalFrameClosed(InternalFrameEvent e) {
                        toolBar.updateEnabledState();
                    }

                    public void internalFrameClosing(InternalFrameEvent e) {
                        toolBar.updateEnabledState();
                    }

                    public void internalFrameDeactivated(InternalFrameEvent e) {
                        toolBar.updateEnabledState();
                    }

                    public void internalFrameDeiconified(InternalFrameEvent e) {
                        toolBar.updateEnabledState();
                    }

                    public void internalFrameIconified(InternalFrameEvent e) {
                        toolBar.updateEnabledState();
                    }

                    public void internalFrameOpened(InternalFrameEvent e) {
                        toolBar.updateEnabledState();
                    }
                });

            //Call #activateFrame *after* adding the listener. [Jon Aquino]
            activateFrame((JInternalFrame) internalFrame);
            //position(internalFrame);
        }*/

            
      }
      
      
      //private int positionIndex = -1;

      //private int primaryInfoFrameIndex = -1;
      
      
      /*private void position(JInternalFrame internalFrame)
      {
          final int STEP = 5;
          GUIUtil.Location location = null;
          if (internalFrame instanceof PrimaryInfoFrame)
          {
              primaryInfoFrameIndex++;

              int offset = (primaryInfoFrameIndex % 3) * STEP;
              location = new GUIUtil.Location(offset, true, offset, true);
          } else
          {
              positionIndex++;

              int offset = (positionIndex % 5) * STEP;
              location = new GUIUtil.Location(offset, false, offset, false);
          }
          GUIUtil.setLocation((Component) internalFrame, location, mostrar.getContentPane());
          
      }*/

  	/* (non-Javadoc)
  	 * @see com.geopista.editor.WorkbenchGuiComponent#activateFrame(javax.swing.JInternalFrame)
  	 */
  	public void activateFrame(JInternalFrame frame) {
  		// TODO Auto-generated method stub
  		
  		//frame.moveToFront();
  		/*frame.toFront();
  		frame.setVisible(false);
  		frame.show();
        frame.requestFocus();
        try
        {
            frame.setSelected(true);
            if (!(frame instanceof TaskFrame))
            {
                frame.setMaximum(false);
            }
        } catch (PropertyVetoException e)
        {
            warnUser(StringUtil.stackTrace(e));
        }*/
  		
  	}
      public void activateFrame(TaskComponent frame) 
      {
        
        /*((JInternalFrame)frame).moveToFront();
        ((JInternalFrame)frame).requestFocus();
       try {
            ((JInternalFrame)frame).setSelected(true);
            if (!(frame instanceof TaskFrame)) {
                frame.setMaximum(false);
            }
        } catch (PropertyVetoException e) {
            warnUser(StringUtil.stackTrace(e));
        }*/
      }
      
      public LayerNamePanelListener getLayerNamePanelListener() {
        return layerNamePanelListener;
      }
      
    
      
      public String getLog() {
        return "";
      }

      public String getTitle(){
        return "";
      }

      public Set getChoosableStyleClasses()
      {
        return Collections.unmodifiableSet(choosableStyleClasses);
      }
      public JInternalFrame[] getInternalFrames()
      {
        return null;
      }

      public TaskFrame addTaskFrame()
      {
        return null;
      }
      
      public MenuBar getMenuBar()
      {
        return null;
      }

      public void setJMenuBar(JMenuBar menubar)
      {
   
      }

      public JMenuBar getJMenuBar()
      {
        return new JMenuBar();
      }

      public void removeComponentListener(ComponentListener l)
      {
        
      }

      public String getMBCommittedMemory()
      {
        return null;
      }

      public void addKeyboardShortcut(final int keyCode, final int modifiers, final PlugIn plugIn, final EnableCheck enableCheck)
      {
        keyCodeAndModifiersToPlugInAndEnableCheckMap.put(keyCode+":"+modifiers,
            new Object[]{plugIn, enableCheck});  
      }

      public LayerViewPanelListener getLayerViewPanelListener()
      {
        return null;
      }

      public void toFront()
      {
        
      }

      JLabel timeLabel = new JLabel();
      JPanel jPanelStatus = new JPanel();
      
      public void setTimeMessage(String message) {
    	  timeLabel.setText((message == "") ? " " : message);
     }
// REVISAR: Creo que este método debería devolver un Task 
      //esta signatura la cubre getTaskFrame() [Juan Pablo]
     public Task getTask() {
        return task;
     }
public void setTask(ITask task)
{
this.task=(GeopistaMap) task;
this.layerManager=(GeopistaLayerManager) task.getLayerManager();
//this.fireLayerChanged(new LayerEvent(null,));
}
     public void moveToFront()
     {
       
     }

     public void requestFocus()
     {
       
     }

     public void setSelected(boolean selected) throws java.beans.PropertyVetoException
     {
       
     }
     public void setMaximum(boolean b) throws java.beans.PropertyVetoException
     {
       
     }
     public SelectionManager getSelectionManager() {
        return getLayerViewPanel().getSelectionManager();
      }

      public void setFrameIcon(Icon icon)
      {
        
      }

      public void addInternalFrameListener(InternalFrameListener l)
      {
        
      }

      public TaskComponent getTaskComponent()
      {
        return this;
      }

      public String getName()
      {
        return "GeopistaEditor";
      }
      
      public void add(ITaskNameListener nameListener) {
        nameListeners.add(nameListener);
      }

      public InfoFrame getInfoFrame() {
        if (infoFrame == null || infoFrame.isClosed()) {
            infoFrame = new PrimaryInfoFrame(workbenchContext, this, this);
        }
        return infoFrame;
    }

    private void installKeyboardShortcutListener() {            
		addEasyKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				Object[] plugInAndEnableCheck =
					(
						Object[]) keyCodeAndModifiersToPlugInAndEnableCheckMap
							.get(
						e.getKeyCode() + ":" + e.getModifiers());
				if (plugInAndEnableCheck == null) {
					return;
				}
				PlugIn plugIn = (PlugIn) plugInAndEnableCheck[0];
				EnableCheck enableCheck = (EnableCheck) plugInAndEnableCheck[1];
				if (enableCheck != null && enableCheck.check(null) != null) {
					return;
				}
                //#toActionListener handles checking if the plugIn is a ThreadedPlugIn,
                //and making calls to UndoableEditReceiver if necessary. [Jon Aquino 10/15/2003]
                AbstractPlugIn.toActionListener(plugIn, workbenchContext, new TaskMonitorManager())
                    .actionPerformed(null);
			}
		} 
		);       
    }

    public void addEasyKeyListener(KeyListener l) {
        easyKeyListeners.add(l);
    }

    private static void initLookAndFeel() throws Exception {
	    //Apple stuff from Raj Singh's startup script [Jon Aquino 10/30/2003]
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.showGrowBox", "true");
        if (UIManager.getLookAndFeel() != null 
                && UIManager.getLookAndFeel().getClass().getName().equals(UIManager.getSystemLookAndFeelClassName())) {
            return;
        }
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }


    /*public void setTreeLayerPopupMenu(ArrayList plugins, Layer localLayer)
    {
        
      try
      {
        PlugIn cargandoPlugin = (AbstractPlugIn) (Class.forName((String)plugins.get(0))).newInstance();
        //ChangeStylesPlugIn changeStylesPlugIn = new ChangeStylesPlugIn();
        JPopupMenu localPopup = new JPopupMenu();
        featureInstaller.addPopupMenuItem(localPopup,
            cargandoPlugin, cargandoPlugin.getName() + "...", false,
            null,
            null);
     
           
        localPopup.add("opcion 1");
        localPopup.add("opcion 2");
        localPopup.add("opcion 3");
        localPopup.add("opcion 4");
        localPopup.add("opcion 5");

      
        JPopupMenu localPopup2 = new JPopupMenu();
        localPopup2.add("opcion 1");
        localPopup2.add("opcion 2");
        localPopup2.add("opcion 3");
        localPopup2.add("opcion 4");

//        localPopup.setVisible(true);

        
        
     if(localLayer.hashCode()<400)
     {
      layerNamePanel.addPopupMenuContext(localLayer.hashCode(),localPopup);
     }else
     {
       layerNamePanel.addPopupMenuContext(localLayer.hashCode(),localPopup2);
     }

     }catch(Exception e)
      {
        
      }

     
    }*/

    
    public void panTo(Feature feature) throws NoninvertibleTransformException
    {
      // RECUPERAMOS EL CENTROIDE
      com.vividsolutions.jts.geom.Point centroide = feature.getGeometry().getCentroid();
     
      double centroX = centroide.getX();
      double centroY = centroide.getY();      

      // CONSERVAMOS EL ENVELOPE ANTIGUO
      Envelope oldEnvelope = layerViewPanel.getViewport().getEnvelopeInModelCoordinates();
      

      // CALCULAMOS EL DESPLAZAMIENTO QUE DEBEMOS REALIZAR
      // CALCULANDO LA DISTANCIA EN X,Y DE LOS RESPECTIVOS CENTROIDES
      double centroideEnvelopeX = oldEnvelope.getMinX()+((oldEnvelope.getMaxX() - oldEnvelope.getMinX())/2);
      double centroideEnvelopeY = oldEnvelope.getMaxY()-((oldEnvelope.getMaxY() - oldEnvelope.getMinY())/2);

      // EL DESPLAZAMIENTO QUE DEBEMOS REALIZAR EN X,Y
      double xDisplacement = -(centroX - centroideEnvelopeX);
      double yDisplacement = -(centroY - centroideEnvelopeY);

      // HACEMOS ZOOM AL ENVELOPE DESPLAZADO
      layerViewPanel.getViewport().zoom(new Envelope(oldEnvelope.getMinX() - xDisplacement, oldEnvelope.getMaxX() - xDisplacement, oldEnvelope.getMinY() - yDisplacement, oldEnvelope.getMaxY() - yDisplacement));
      
      
    }

    public void zoomTo(Feature feature) throws NoninvertibleTransformException
    {
     
      // RECUPERAMOS EL ENVELOPE DE LA FEATURE
      
      Envelope envelopeFeature = feature.getGeometry().getEnvelopeInternal();

      // ESTABLECEMOS UN FACTOR DE TOLERANCIA PARA QUE EL ZOOM
      // SE AJUSTE EXACTAMENTE A LA FEATURE Y NOS IMPIDA VER
      // EL ENTORNO DE LA FEATURE
      
      int tolerancia = 5;

      // CALCULAMOS EL ENVELOPE DE LA FEATURE      
      double minX = envelopeFeature.getMinX()-(envelopeFeature.getWidth()/tolerancia);
      double maxX = envelopeFeature.getMaxX()+(envelopeFeature.getWidth()/tolerancia);
      double minY = envelopeFeature.getMinY()-(envelopeFeature.getHeight()/tolerancia);
      double maxY = envelopeFeature.getMaxY()+(envelopeFeature.getHeight()/tolerancia);

      // INICIALIZAMOS EL ENVELOPE
      envelopeFeature.init(minX,maxX,minY,maxY);

      // ZOOM AL ENVELOPE DE LA FEATURE
      layerViewPanel.getViewport().zoom(envelopeFeature);
      

    }

    public void select(Layer layer, Feature feature)
    {
        boolean originalPanelUpdatesEnabled = layerViewPanel.getSelectionManager().arePanelUpdatesEnabled();
        try
        {
          layerViewPanel.getSelectionManager().setPanelUpdatesEnabled(false);
          layerViewPanel.getSelectionManager().getFeatureSelection().selectItems(layer, feature);
          
        }finally
        {
          layerViewPanel.getSelectionManager().setPanelUpdatesEnabled(originalPanelUpdatesEnabled);
          
        }
          layerViewPanel.getSelectionManager().updatePanel();
    }

    public void select(Layer layer, Collection feature)
    {
        boolean originalPanelUpdatesEnabled = layerViewPanel.getSelectionManager().arePanelUpdatesEnabled();
        try
        {
          layerViewPanel.getSelectionManager().setPanelUpdatesEnabled(false);
          layerViewPanel.getSelectionManager().getFeatureSelection().selectItems(layer, feature);
          
        }finally
        {
          layerViewPanel.getSelectionManager().setPanelUpdatesEnabled(originalPanelUpdatesEnabled);
          
        }
          layerViewPanel.getSelectionManager().updatePanel();
    } 


    public Collection getSelection()
    {
      return layerViewPanel.getSelectionManager().getFeatureSelection().getFeaturesWithSelectedItems();
    }


    public Collection searchByAttribute(String layerName, String attributeName, String value)
    {
      Collection finalFeatures = new ArrayList();
      
      Layer localLayer = layerManager.getLayer(layerName);
      List allFeaturesList = localLayer.getFeatureCollectionWrapper().getFeatures();
      Iterator allFeaturesListIter = allFeaturesList.iterator();
      while(allFeaturesListIter.hasNext())
      {
        Feature localFeature = (Feature)allFeaturesListIter.next();

        String nombreAtributo = localFeature.getString(attributeName).trim();

        if(nombreAtributo.equals(value))
        {
          finalFeatures.add(localFeature);
        }
      }

      return finalFeatures;
    }
/**
 * Inicia el asistente para la impresión de mapas tomando como mapa el 
 * visible actualmente.
 *
 */
//    public void printMap()
//    {
//      GeopistaFunctionUtils.executePlugIn(new GeopistaPrintPlugIn(),workbenchContext,new TaskMonitorManager());
//    }
/**
 * Imprime el mapa con unos atributos predeterminados.
 * El asistente pasa directamente a la previsualización.
 * @param configuredPrintJob Seleccion de impresora y formatos
 * @param title
 * @param comment
 * @param scale		Escala a la que se representa -1 si es al tamaño actual
 * @param legend	true/false presenta la leyenda o no
 */
/*    public void printMap(PrintJob configuredPrintJob, String title, String comment, int scale, boolean legend)
    {
      ArrayList titulos = new ArrayList();
      titulos.add(title);
      blackboardInformes.put(GeopistaPrintPlugIn.IMPRIMIRPF,configuredPrintJob);
      blackboardInformes.put(GeopistaPrintPlugIn.TITULOMAPAIMPRIMIR,titulos);
      blackboardInformes.put(GeopistaPrintPlugIn.DESCRIPCIONMAPAIMPRIMIR,comment);
      blackboardInformes.put(GeopistaPrintPlugIn.ESCALASELECCIONADAIMPRIMIR,String.valueOf(scale));
      blackboardInformes.put(GeopistaPrintPlugIn.MOSTRARLEYENDAIMPRIMIR,legend);

      GeopistaUtil.executePlugIn(new GeopistaPrintPlugIn(),workbenchContext,new TaskMonitorManager());
      
      blackboardInformes.put(GeopistaPrintPlugIn.IMPRIMIRPF,null);
      blackboardInformes.put(GeopistaPrintPlugIn.TITULOMAPAIMPRIMIR,null);
      blackboardInformes.put(GeopistaPrintPlugIn.DESCRIPCIONMAPAIMPRIMIR,null);
      blackboardInformes.put(GeopistaPrintPlugIn.ESCALASELECCIONADAIMPRIMIR,null);
      blackboardInformes.put(GeopistaPrintPlugIn.MOSTRARLEYENDAIMPRIMIR,null);

      
    }*/
/**
 * Imprime una serie de mapas utilizando las features de la capa mapLayer
 * para generar el mosaico de hojas.
 * Lanza el asistente de impresión.
 * 
 * @param mapLayer nombre de la capa que se usa para paginar.
 */
/*    public void printMapSeries(String mapLayer)
    {
      blackboardInformes.put(SeriePrintPlugIn.CAPAPLANTILLA,mapLayer);
      GeopistaUtil.executePlugIn(new GeopistaPrintPlugIn(),workbenchContext,new TaskMonitorManager());
      blackboardInformes.put(SeriePrintPlugIn.CAPAPLANTILLA,null);
    }*/
/**
 * Lanza el asistente de impresión con datos preconfigurados.
 * 
 * @param mapLayer
 * @param configuredPrintJob
 * @param title
 * @param comment
 * @param scale
 * @param legend
 */
 /*   public void printMapSeries(String mapLayer,PrintJob configuredPrintJob, String title, String comment, int scale, boolean legend)
    {
      ArrayList titulos = new ArrayList();
      titulos.add(title);
      blackboardInformes.put(GeopistaPrintPlugIn.IMPRIMIRPF,configuredPrintJob);
      blackboardInformes.put(GeopistaPrintPlugIn.TITULOMAPAIMPRIMIR,titulos);
      blackboardInformes.put(GeopistaPrintPlugIn.DESCRIPCIONMAPAIMPRIMIR,comment);
      blackboardInformes.put(GeopistaPrintPlugIn.ESCALASELECCIONADAIMPRIMIR,String.valueOf(scale));
      blackboardInformes.put(GeopistaPrintPlugIn.MOSTRARLEYENDAIMPRIMIR,legend);
      blackboardInformes.put(SeriePrintPlugIn.CAPAPLANTILLA,mapLayer);
      GeopistaUtil.executePlugIn(new GeopistaPrintPlugIn(),workbenchContext,new TaskMonitorManager());
      blackboardInformes.put(GeopistaPrintPlugIn.IMPRIMIRPF,null);
      blackboardInformes.put(GeopistaPrintPlugIn.TITULOMAPAIMPRIMIR,null);
      blackboardInformes.put(GeopistaPrintPlugIn.DESCRIPCIONMAPAIMPRIMIR,null);
      blackboardInformes.put(GeopistaPrintPlugIn.ESCALASELECCIONADAIMPRIMIR,null);
      blackboardInformes.put(GeopistaPrintPlugIn.MOSTRARLEYENDAIMPRIMIR,null);
      blackboardInformes.put(SeriePrintPlugIn.CAPAPLANTILLA,null);
    }*//*

  
    public Image printMap(int x, int y)
    {

        int altoPanel = layerViewPanel.getViewport().getPanel().getHeight();
        int anchoPanel = layerViewPanel.getViewport().getPanel().getWidth();

        Image image = this.createImage(anchoPanel,altoPanel);
        Graphics g = image.getGraphics();
        layerViewPanel.getViewport().getPanel().printAll(g);        




        
        double ratiox = (double) x / (double) anchoPanel;
        double ratioy = (double) y / (double) altoPanel;

        BufferedImage thumbImage = new BufferedImage(x,y, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = thumbImage.createGraphics();
        graphics2D.scale(ratiox,ratioy);
        graphics2D.drawImage(image, 0, 0, null);
        
*/

        //para prueba de la funcion
        /*JFrame f = new JFrame();

        JLabel label = new JLabel(new ImageIcon(thumbImage));
        label.setSize(550,550);
        f.getContentPane().add(label);
        f.setIconImage(image);
        f.setSize(600,600);
        f.setVisible(true);*/
/*
        return thumbImage;
    }*/

    public void zoomToSelected()
    {
      try
      {
        zoom(layerViewPanel.getSelectionManager().getSelectedItems(), layerViewPanel);
      }catch(Exception e)
      {
        logger.error("zoomToSelected()", e);
      }
    }

    public void zoom(final Collection geometries, final ILayerViewPanel panel)
        throws NoninvertibleTransformException {
        if (envelope(geometries).isNull()) {
            return;
        }
        Envelope proposedEnvelope =
            EnvelopeUtil.bufferByFraction(
                envelope(geometries),
                zoomBufferAsExtentFraction(geometries));

        if ((proposedEnvelope.getWidth()
            > panel.getLayerManager().getEnvelopeOfAllLayers().getWidth())
            || (proposedEnvelope.getHeight()
                > panel.getLayerManager().getEnvelopeOfAllLayers().getHeight())) {
            //We've zoomed out farther than we would if we zoomed to all layers.
            //This is too far. Set scale to that of zooming to all layers,
            //and center on the selected features. [Jon Aquino]
            proposedEnvelope = panel.getViewport().fullExtent();
            EnvelopeUtil.translate(
                proposedEnvelope,
                CoordUtil.subtract(
                    EnvelopeUtil.centre(envelope(geometries)),
                    EnvelopeUtil.centre(proposedEnvelope)));
        }

        panel.getViewport().zoom(proposedEnvelope);

    }

    private Envelope envelope(Collection geometries) {
        Envelope envelope = new Envelope();

        for (Iterator i = geometries.iterator(); i.hasNext();) {
            Geometry geometry = (Geometry) i.next();
            envelope.expandToInclude(geometry.getEnvelopeInternal());
        }

        return envelope;
    }

    private double zoomBufferAsExtentFraction(Collection geometries) {
        //Easiest to express zoomBuffer as a multiple of the average extent of
        //the individual features, rather than a multiple of the average extent
        //of the features combined. For example, 2 * the average extent of the
        //features combined can be a huge zoomBuffer if the features are far
        //apart. But if you consider the average extent of the individual features,
        //you don't need to think about how far apart the features are. [Jon Aquino]
        double zoomBuffer = 2 * averageExtent(geometries);
        double averageFullExtent = averageFullExtent(geometries);

        if (averageFullExtent == 0) {
            //Point feature. Just return 0. Rely on EnvelopeUtil#buffer to choose
            //a reasonable buffer for point features. [Jon Aquino]
            return 0;
        }

        return zoomBuffer / averageFullExtent;
    }

    private double averageFullExtent(Collection geometries) {
        Envelope envelope = envelope(geometries);

        return (envelope.getWidth() + envelope.getHeight()) / 2d;
    }

    private double averageExtent(Collection geometries) {
        Assert.isTrue(!geometries.isEmpty());

        double extentSum = 0;

        for (Iterator i = geometries.iterator(); i.hasNext();) {
            Geometry geometry = (Geometry) i.next();
            extentSum += geometry.getEnvelopeInternal().getWidth();
            extentSum += geometry.getEnvelopeInternal().getHeight();
        }

        return extentSum / (2d * geometries.size());
        //2 because width and height [Jon Aquino]
    }


    public void addGeopistaListener(GeopistaListener geopistaListener) {
        Assert.isTrue(!geopistaListeners.contains(geopistaListener));
        geopistaListeners.add(geopistaListener);
    }

    public void removeLayerListener(LayerListener layerListener) {
        layerListeners.remove(layerListener);
    }

    private void fireFeaturesChanged(final FeatureEvent e) {
        if (!firingEvents) {
            return;
        }

        //New ArrayList to avoid ConcurrentModificationException [Jon Aquino]
        for (Iterator i = new ArrayList(layerListeners).iterator();
                i.hasNext();) {
            final LayerListener layerListener = (LayerListener) i.next();
            fireLayerEvent(new Runnable() {
                    public void run() {
                        layerListener.featuresChanged(e);
                    }
                });
        }
    }

    private void fireLayerChanged(final LayerEvent e) {
        if (!firingEvents) {
            return;
        }

        //New ArrayList to avoid ConcurrentModificationException [Jon Aquino]
        for (Iterator i = new ArrayList(layerListeners).iterator();
                i.hasNext();) {
            final LayerListener layerListener = (LayerListener) i.next();
            fireLayerEvent(new Runnable() {
                    public void run() {
                        layerListener.layerChanged(e);
                    }
                });
        }
    }

    private void fireCategoryChanged(final CategoryEvent e) {
        if (!firingEvents) {
            return;
        }

        //New ArrayList to avoid ConcurrentModificationException [Jon Aquino]
        for (Iterator i = new ArrayList(layerListeners).iterator();
                i.hasNext();) {
            final LayerListener layerListener = (LayerListener) i.next();
            fireLayerEvent(new Runnable() {
                    public void run() {
                        layerListener.categoryChanged(e);
                    }
                });
        }
    }

    public void featuresChanged(FeatureEvent e) {
        fireFeaturesChanged(e);
     }

     public void layerChanged(LayerEvent e) {
       fireLayerChanged(e);
     }

     public void categoryChanged(CategoryEvent e) {
       fireCategoryChanged(e);
     }
    

    public void addLayerListener(LayerListener layerListener) {
        Assert.isTrue(!layerListeners.contains(layerListener));
        layerListeners.add(layerListener);
    }

    public void removeGeopistaListener(GeopistaListener geopistaListener) {
        geopistaListeners.remove(geopistaListener);
    }
    /**
     * Renueva 
     */
    public void removeAllGeopistaListener() {
        geopistaListeners= new ArrayList();
    }

    private void fireGeopistaFeatureModified(final FeatureEvent e) {
        if (!firingEvents) {
            return;
        }

        //New ArrayList to avoid ConcurrentModificationException [Jon Aquino]
        for (Iterator i = new ArrayList(geopistaListeners).iterator();
                i.hasNext();) {
            final GeopistaListener geopistaListener = (GeopistaListener) i.next();
            fireLayerEvent(new Runnable() {
                    public void run() {
                        geopistaListener.featureModified(e);
                    }
                });
        }
    }

    private void fireGeopistaFeatureRemoved(final FeatureEvent e) {
        if (!firingEvents) {
            return;
        }

        //New ArrayList to avoid ConcurrentModificationException [Jon Aquino]
        for (Iterator i = new ArrayList(geopistaListeners).iterator();
                i.hasNext();) {
            final GeopistaListener geopistaListener = (GeopistaListener) i.next();
            fireLayerEvent(new Runnable() {
                    public void run() {
                        geopistaListener.featureRemoved(e);
                    }
                });
        }
    }

    private void fireGeopistaFeatureAdded(final FeatureEvent e) {
        if (!firingEvents) {
            return;
        }

        //New ArrayList to avoid ConcurrentModificationException [Jon Aquino]
        for (Iterator i = new ArrayList(geopistaListeners).iterator();
                i.hasNext();) {
            final GeopistaListener geopistaListener = (GeopistaListener) i.next();
            fireLayerEvent(new Runnable() {
                    public void run() {
                        geopistaListener.featureAdded(e);
                    }
                });
        }
    }

   

    private void fireLayerEvent(Runnable eventFirer) {
       
        try {
            GUIUtil.invokeOnEventThread(eventFirer);
        } catch (InterruptedException e) {
            Assert.shouldNeverReachHere();
        } catch (InvocationTargetException e) {
            e.printStackTrace(System.err);
            Assert.shouldNeverReachHere();
        }

    }

    public boolean isFiringEvents() {
        return firingEvents;
    }

    public void setFiringEvents(boolean firingEvents) {
        this.firingEvents = firingEvents;
    }

    public void fireGeopistaFeatureActioned(final IAbstractSelection abtractSelection)
    {
      if (!firingEvents) {
            return;
        }

        //New ArrayList to avoid ConcurrentModificationException [Jon Aquino]
        for (Iterator i = new ArrayList(geopistaListeners).iterator();
                i.hasNext();) {
            final GeopistaListener geopistaListener = (GeopistaListener) i.next();
            fireLayerEvent(new Runnable() {
                    public void run() {
                        geopistaListener.featureActioned(abtractSelection);
                    }
                });
        }
    }

    public void fireGeopistaSelectionChanged(final AbstractSelection abtractSelection)
    {
      if (!firingEvents) {
            return;
        }

        //New ArrayList to avoid ConcurrentModificationException [Jon Aquino]
        for (Iterator i = new ArrayList(geopistaListeners).iterator();
                i.hasNext();) {
            final GeopistaListener geopistaListener = (GeopistaListener) i.next();
            fireLayerEvent(new Runnable() {
                    public void run() {
                        geopistaListener.selectionChanged(abtractSelection);
                    }
                });
        }
    }

	/* 
	 * 
	 * (non-Javadoc)
	 * @see com.geopista.editor.WorkbenchGuiComponent#getViewportListener()
	 */
	public ViewportListener getViewportListener() {
		
		return this;
	}

	/* No se pueden crear nuevas TaskFrame: se ignora la llamada
	 * (non-Javadoc)
	 * @see com.geopista.editor.WorkbenchGuiComponent#addTaskFrame(com.vividsolutions.jump.workbench.model.Task)
	 */
	public TaskFrame addTaskFrame(Task task) {
		
		return null;
	}

    public DriverManager getDriverManager()
    {
    	return null;
    }
/**
 * hace que este componente comparta el modelo de datos con otro
 * @param model
 */
    public void setSharedModel(GeopistaMap model)
    {
    	this.task= model;
    	layerManager=(GeopistaLayerManager) task.getLayerManager();
    	task.getLayerManager().addLayerListener(this);
    }

    public void loadMap(String mapPath) throws Exception {
    	loadMap(mapPath,null,null);
    }
       
    public void loadMap(String mapPath,TaskMonitor monitorDialog) throws Exception {
    	loadMap(mapPath,null,monitorDialog);
    }
    
    public void loadMap(String mapPath,String[] filtro,TaskMonitor monitorDialog) throws Exception {

      File file = null;
      try
      {
        URL urlMap = new URL(mapPath);
        if(urlMap.getProtocol().equalsIgnoreCase("geopista"))
        {

            String idMapa = urlMap.getFile().substring(1);
            Task map= (Task)AppContext.getApplicationContext().getMap(idMapa);
            if (map!=null) // have cached map
            	{
            	this.setTask((ITask) map);
            	}
            else
            	{
            	GeopistaMapUtil.loadDataBaseMap(idMapa,filtro,workbenchContext,monitorDialog);
            //AppContext.getApplicationContext().storeMap(idMapa,this.getTask());
            	}
        }
        else
        {
          try
          {
            loadFileMap(new File(urlMap.getFile()));
          }catch(Exception e1)
          {
           JOptionPane.showMessageDialog((Component) workbenchContext.getIWorkbench().getGuiComponent(),aplicacion.getI18nString("GeopistaEditor.MapaNoCorrecto"));
           return;
          }
        }
      }catch(MalformedURLException e)
      {
        try
        {
          file = new File(mapPath);
          loadFileMap(file);
        }catch(Exception e1)
        {
         JOptionPane.showMessageDialog((Component) workbenchContext.getIWorkbench().getGuiComponent(),aplicacion.getI18nString("GeopistaEditor.MapaNoCorrecto"));
         return;
        }
      }
 
      ((EnableableToolBar) getToolBar()).updateEnabledState();
    }

    private void loadFileMap(File file) throws Exception
    {
       GeopistaMap sourceTask = GeopistaMap.getMapUTF8(file.getAbsolutePath());
            
              this.reset();
             
              sourceTask.setName(GUIUtil.nameWithoutExtension(file));
              sourceTask.setProjectFile(file);
            

            ILayerManager sourceLayerManager = sourceTask.getLayerManager();
            for (Iterator i = sourceLayerManager.getCategories().iterator(); i.hasNext();) {
              Category sourceLayerCategory = (Category) i.next();

             
              CoordinateSystemRegistry registry = CoordinateSystemRegistry.instance(blackboard);
    
              layerManager.addCategory(sourceLayerCategory.getName());
            
            
              ArrayList layerables = new ArrayList(sourceLayerCategory.getLayerables());
              //Collections.reverse(layerables);

              for (Iterator j = layerables.iterator(); j.hasNext();) {
                  Layerable layerable = (Layerable) j.next();
                  layerable.setLayerManager((ILayerManager)layerManager);
                  if (layerable instanceof Layer) {
                      Layer layer = (Layer) layerable;
                      layer.setFeatureCollection(
                          executeQuery(
                              layer.getDataSourceQuery().getQuery(),
                              layer.getDataSourceQuery().getDataSource(), registry, null));
                      layer.setFeatureCollectionModified(false);   
                      
                  }


                layerManager.addLayerable(sourceLayerCategory.getName(), layerable);
            }
        }
            
    }

    private FeatureCollection executeQuery(String query, DataSource dataSource, CoordinateSystemRegistry registry, TaskMonitor monitor)
        throws Exception {
        Connection connection = dataSource.getConnection();
        try {
            return dataSource.installCoordinateSystem(connection.executeQuery(query, monitor), registry);
        } finally {
            connection.close();
        }
    }



private static class SingleLineProgressMonitor extends ProgressMonitor {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(SingleLineProgressMonitor.class);

        public SingleLineProgressMonitor() {
            super(new JLabel(" "));
            ((JLabel) getComponent()).setFont(((JLabel) getComponent()).getFont()
                                               .deriveFont(Font.BOLD));
            ((JLabel) getComponent()).setHorizontalAlignment(JLabel.LEFT);
        }

        protected void addText(String s) {
            ((JLabel) getComponent()).setText(s);
        }
    }

private static abstract class ProgressMonitor extends JPanel
        implements TaskMonitor {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ProgressMonitor.class);

        private Component component;

        public ProgressMonitor(Component component) {
            this.component = component;
            setLayout(new BorderLayout());
            //add(component, BorderLayout.CENTER);
            setOpaque(false);
        }

        protected Component getComponent() {
            return component;
        }

        protected abstract void addText(String s);

        public void report(String description) {
            addText(description);
        }

        public void report(int itemsDone, int totalItems, String itemDescription) {
            addText(itemsDone + " / " + totalItems + " " + itemDescription);
        }

        public void report(Exception exception) {
            addText(StringUtil.toFriendlyName(exception.getClass().getName()));
        }

        public void allowCancellationRequests() {
        }

        public boolean isCancelRequested() {
            return false;
            //return cancelled;
        }
    }

    private LayerNamePanelListener layerNamePanelListener = new LayerNamePanelListener() {
        public void layerSelectionChanged() {
            toolBar.updateEnabledState();
        }
    };

    public WorkbenchToolBar getToolBar(String toolBarName)
    {
    	//Gestionamos que pueda haber varias barras de herramientas.
		 WorkbenchToolBar actualToolBar = (WorkbenchToolBar) toolBarMap.get(toolBarName);
	     if (actualToolBar == null)
	     {
	    	 return toolBar;
	     }      
	     else
	    	 return actualToolBar;
    }

    public void deleteSelectedFeatures()
    {
      Collection layersWithSelectedItems = layerViewPanel.getSelectionManager().getLayersWithSelectedItems();
      Iterator layersWithSelectedItemsIter = layersWithSelectedItems.iterator();
      while(layersWithSelectedItemsIter.hasNext())
      {
        Layer actualLayer = (Layer) layersWithSelectedItemsIter.next();
        Collection selectedItems = layerViewPanel.getSelectionManager().getFeaturesWithSelectedItems(actualLayer);
        actualLayer.getFeatureCollectionWrapper().removeAll(selectedItems);
      }

    }
	/* (non-Javadoc)
	 * @see com.geopista.editor.WorkbenchGuiComponent#getMainFrame()
	 */
	public JFrame getMainFrame()
	{
		return aplicacion.getMainFrame();
	}
	/* No tiene sentido ya que el Editor funciona en un ambiente de JavaBean
	 * dentro de otras aplicaciones que no se basan en Workbench
	 * @see com.geopista.editor.WorkBench#getFrame()
	 */
	public WorkbenchFrame getFrame()
	{
	return null;
	}
	/* (non-Javadoc)
	 * @see com.geopista.editor.TaskComponent#getTaskFrame()
	 */
	public TaskComponent getTaskFrame()
	{
	
	return null;
	}
    public void destroy()
    {
        try
        {
        	   //System.out.println("Destruyendo Editor");
               getLayerManager().setFiringEvents(false);
               List lista =getLayerManager().getLayers();
               for (Iterator it=lista.iterator();it.hasNext();)
               {
                      GeopistaLayer layer=((GeopistaLayer)it.next());
                      layer.getFeatureCollectionWrapper().clear();
                      layer.getFeatureCollectionWrapper().getWrappee().clear();
                      layer.dispose();
              }

              if (getLayerManager()!=null)
                      getLayerManager().dispose();
			  layerManager=null;
              if (getToolBar()!=null){
	              getToolBar().removeAll();
	              getToolBar().removeCursorTools();
	              getToolBar().dispose();
              }
              if (listaToolConfig!=null){
	              for (int i=0;i<listaToolConfig.size();i++){
	            	  ToolConfig tool=(ToolConfig)listaToolConfig.get(i);
	            	  tool.remove();
	            	  tool=null;
	            	  
	              }
            	  listaToolConfig.clear();             
              }
              toolBar=null;
              removeAllGeopistaListener();
              
              
              if (task!=null)
            	  if (task.getLayerManager()!=null)
            		  task.getLayerManager().dispose();
              task=null;
             
              removeAll();
              
              //Dispose
              if (workbenchContext!=null)
            	  workbenchContext.dispose();
              workbenchContext=null;
              
              easyKeyListeners=null;
              layerNamePanelListener=null;
              dummyProperties=null;
             
              //JAVI


             borderLayout1=null;
              borderLayout2=null;
              centrePanel=null;
              choosableStyleClasses=null;
              dummyProperties=null;
              easyKeyListeners=null;
              errorHandler=null;

              
              gridBagLayout1=null;
              infoFrame=null;

              jViewPanel=null;
              keyCodeAndModifiersToPlugInAndEnableCheckMap=null;
              layerListeners=null;
              layerNamePanelListener=null;
              listaToolConfig=null;
              monitor=null;
              mostrar=null;
              nameListeners=null;
              
              if (nodeClassToLayerNamePopupMenuMap!=null)
            	  nodeClassToLayerNamePopupMenuMap.clear();
              nodeClassToLayerNamePopupMenuMap=null;
              plugInManager=null;
              properties=null;
              statusLabel=null;
              task=null;
              timeLabel=null;
              toolBar=null;
              toolbarPanel=null;

 
              //Borramos los PopupMenu
              destroyTitledPopupMenus(wmsLayerNamePopupMenu);
              destroyTitledPopupMenus(layerNamePopupMenu);
              destroyTitledPopupMenus(categoryPopupMenu);
 
			 /*   private Map nodeClassToLayerNamePopupMenuMap = CollectionUtil.createMap(new Object[] {
			            Layerable.class, layerNamePopupMenu, WMSLayer.class,
			            wmsLayerNamePopupMenu, Category.class, categoryPopupMenu });
			
			 */
              if (wmsLayerNamePopupMenu!=null)
            	  wmsLayerNamePopupMenu.destroy();
              if (layerNamePopupMenu!=null)
            	  layerNamePopupMenu.destroy();              
              if (categoryPopupMenu!=null)
            	  categoryPopupMenu.destroy();
              wmsLayerNamePopupMenu=null;
              layerNamePopupMenu=null;              
              categoryPopupMenu=null;
               
              if (popupMenu!=null)
            	  ((TrackedPopupMenu)popupMenu).destroy();
              popupMenu=null;
              
              if (layerManager!=null)		
            	  layerManager.dispose();
              layerManager=null;

              if (layerViewPanel!=null){
            	  if (layerViewPanel.getLayerManager()!=null)
            			  layerViewPanel.getLayerManager().removeAllLayerListeners();
            	  if (layerViewPanel.getRenderingManager()!=null)
            		  layerViewPanel.getRenderingManager().dispose();
	              layerViewPanel.removeAll();
	              layerViewPanel.removeAllListener();
	              layerViewPanel.dispose();
	              layerViewPanel=null;
              }
              
              
              if (layerNamePanel!=null){
                  if (layerNamePanel.getLayerManager()!=null){
                	  layerNamePanel.getLayerManager().dispose();
                  }
            	  layerNamePanel.dispose();
            	  layerNamePanel=null;
              }

              if (splitPane!=null)
            	  splitPane.removeAll();
              splitPane=null;

              if (featureInstaller!=null)
            	  featureInstaller.destroy();
              featureInstaller=null;
              
              
              jPanelStatus=null;
              blackboard=null;
              blackboardInformes=null;
              geopistaFactory=null;
              
              //PROPIEDADES QUE NO SE DEBEN BORRAR              
              //aplicacion.getBlackboard().remove("urlProtocol");
              //aplicacion=null;
              //progressMonitorClass=null;
 
              //System.out.println("Editor destruido");
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    
  private static void destroyTitledPopupMenus(TitledPopupMenu popup){
    	
	  	if (popup!=null){
	        PopupMenuListener[] lsnr = popup.getPopupMenuListeners();
	        for(int i=0; i<lsnr.length; i++) {
	      	  popup.removePopupMenuListener(lsnr[i]);
	      	  lsnr[i]=null;
	        }
	        popup.removeAll();
	  	}
    }

    public TaskComponent[] getInternalTaskComponents()
    {
        return new TaskComponent[]{this};
    }

    protected LayerNamePanel createLayerNamePanel() {
        GeopistaTreeLayerNamePanel treeLayerNamePanel =  new GeopistaTreeLayerNamePanel(
                this,
                new LayerTreeModel(this),
                this.getLayerViewPanel().getRenderingManager(),
                new HashMap());
        
        Map nodeClassToPopupMenuMap =
            this.workbenchContext.getIWorkbench().getGuiComponent().getNodeClassToPopupMenuMap();
        for (Iterator i = nodeClassToPopupMenuMap.keySet().iterator(); i.hasNext();) {
            Class nodeClass = (Class) i.next();
            treeLayerNamePanel.addPopupMenu(nodeClass, (JPopupMenu) nodeClassToPopupMenuMap.get(nodeClass));
        }

//        SplitLayerNamePanel splitLayerNamePanel = new SplitLayerNamePanel((TreeLayerNamePanel) treeLayerNamePanel);
        LayerNameTabbedPanel layerNamePanel = new LayerNameTabbedPanel((GeopistaTreeLayerNamePanel) treeLayerNamePanel);
        
        return layerNamePanel;
    }
    public JPopupMenu getLayerViewPopupMenu()
    {
        return popupMenu;
    }

    public WorkbenchToolBar addToolbar(String toolBarName){
    	WorkbenchToolBar newToolBar = new WorkbenchToolBar(
    		new LayerViewPanelProxy()
				{
    			public 	LayerViewPanel getLayerViewPanel()
    				{
    				return layerViewPanel;
    				}
    			});
    	toolbarPanel.add(
    			newToolBar,
                new GridBagConstraints(
                    0,
                    0,
                    1,
                    1,
                    0.0,
                    0.0,
                    GridBagConstraints.CENTER,
                    GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0),
                    0,
                    0));
    	toolBarMap.put(toolBarName, newToolBar);
    	return newToolBar;
    }
}