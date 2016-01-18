/**
 * GEOPISTAWorkbench.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.UIManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.GeopistaXMLProperties;
import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.global.WebAppConstants;
import com.geopista.style.sld.model.impl.SLDStyleImpl;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaMapUtil;
import com.geopista.util.GeopistaStreamHandleFactory;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.util.commandline.CommandLine;
import com.vividsolutions.jump.util.commandline.Option;
import com.vividsolutions.jump.util.commandline.OptionSpec;
import com.vividsolutions.jump.util.commandline.ParseException;
import com.vividsolutions.jump.workbench.JUMPWorkbench;
import com.vividsolutions.jump.workbench.Setup;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.WorkbenchException;
import com.vividsolutions.jump.workbench.WorkbenchProperties;
import com.vividsolutions.jump.workbench.driver.DriverManager;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.FenceLayerFinder;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.plugin.PlugInManager;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.SplashPanel;
import com.vividsolutions.jump.workbench.ui.SplashWindow;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrame;

/**
 * This class is responsible for setting up and displaying the main
 * GEOPISTA workbench window.
 * 
 * It extends JUMPWorkbench just for compatibility with some plugins
 */

public class GEOPISTAWorkbench implements JUMPWorkbench, LayerListener{
	/**
	 * Logger for this class
	 */
	
	/*static{
	 System.setProperty("log4j.defaultInitOverride", "true");
	 System.setProperty("log4j.debug", "true");
	  System.setProperty("log4j.properties", "log4j_lcg3.properties");
	 System.out.println("INICIANDO");
	}*/
	
	 private static final Log logger;
	    static {
	       createDir();
	       logger  = LogFactory.getLog(GEOPISTAWorkbench.class);
	    } 
	
	//private static final Log logger = LogFactory.getLog(GEOPISTAWorkbench.class);

    private static ImageIcon splashImage;
    private static GEOPISTAWorkbench localWorkbench;
    
    private static ApplicationContext appContext=AppContext.getApplicationContext();

    private static String imagePath = UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,UserPreferenceConstants.DEFAULT_DATA_PATH,false);
    private static String icono = UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_APPICON_KEY,null,false);

    private URLStreamHandlerFactory geopistaFactory = null;
    
    public static void createDir(){
    	File file = new File("logs");

    	if (! file.exists()) {
    		file.mkdirs();
    	}
    }

    public static ImageIcon splashImage() {
        // Lazily initialize it, as it may not even be called (e.g. EZiLink),
    // and we want the splash screen to appear ASAP [Jon Aquino]
//splashImage=APP_ICON;
        if (splashImage == null) 
        {
            splashImage = IconLoader.icon("splash.png");

        }
        return splashImage;
    }


    private static ImageIcon APP_ICON = 
    		UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_APPICON_KEY,null,false)==null?
    				IconLoader.icon("app-icon.gif"):
    				new javax.swing.ImageIcon(UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,UserPreferenceConstants.DEFAULT_DATA_PATH,false)+
    						UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_APPICON_KEY,null,false));
    public final static String PROPERTIES_OPTION = "properties";
    public final static String PLUG_IN_DIRECTORY_OPTION = "plug-in-directory";
    public final static String MAP_OPTION = "map";
    public final static String CLOSE_OPTION = "closeall";
    private static final String LIB_DIRECTORY_OPTION = "lib";
    /**
     * AZABALA -opciones añadidas-
     * */
    
    public final static String CATALOGS_DIRECTORY_OPTION = "catalogos-directory";
    public final static String SPATIAL_INDEX_DIRECTOR_OPTION = "spatial-index-directory";
        
    private static Class progressMonitorClass = SingleLineProgressMonitor.class;
    private ArrayList layerListeners = new ArrayList();
    private boolean firingEvents = true;

    
    private static CommandLine commandLine;
    private WorkbenchContext context = new GEOPISTAWorkbenchContext(this);
    private GeopistaWorkbenchFrame frame;
    private DriverManager driverManager = new DriverManager(frame);
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
    private PlugInManager plugInManager;
    private Blackboard blackboard = new Blackboard();

    public GEOPISTAWorkbench()
    {
    	//System.out.println("STARTING2");
    }

    /**
     * @param s a visible SplashWindow to close when initialization is complete
     * and the WorkbenchFrame is opened
     */
    public GEOPISTAWorkbench(String title,
                         String[] args,
                         ImageIcon icon,
                         final JWindow s,
                         TaskMonitor monitor) throws Exception {
                         
        ((AppContext) appContext).initHeartBeat(); // Forces to start HeartBeat
        Blackboard localBlackboard = appContext.getBlackboard();
        geopistaFactory = (URLStreamHandlerFactory)localBlackboard.get("urlProtocol");
        if(geopistaFactory==null)
        {
        
          geopistaFactory = new GeopistaStreamHandleFactory();
          URL.setURLStreamHandlerFactory(geopistaFactory); 
          localBlackboard.get("urlProtocol",geopistaFactory);
        }
  
        frame = new GeopistaWorkbenchFrame(title, icon, context);
        
        /*frame.addWindowListener(new WindowAdapter() {
                public void windowOpened(WindowEvent e) {
                    s.setVisible(false);
                }
            });*/
        parseCommandLine(args);

        if (commandLine.hasOption(PROPERTIES_OPTION)) {

        	//Modificacion para que tome todos los ficheros de properties
        	Option opt = commandLine.getOption(PROPERTIES_OPTION);
        	ArrayList<String> optArgs = new ArrayList<String>();
        	for (int i=0; i<opt.getNumArgs(); i++)
        	{
        		optArgs.add(opt.getArg(i));
        	}        	

        	properties = new GeopistaXMLProperties(optArgs, frame);
        	//  { System.out.println("JUMP: Warning: Properties file does not exist: " + propertiesFile); }
        }

        File extensionsDirectory = null;
        if (commandLine.hasOption(PLUG_IN_DIRECTORY_OPTION)) {
            extensionsDirectory = new File(commandLine.getOption(PLUG_IN_DIRECTORY_OPTION).getArg(0));
            if (!extensionsDirectory.exists()) {
				if (logger.isDebugEnabled())
				{
					logger.warn(
						"GEOPISTAWorkbench() - Extensions directory does not exist: : File extensionsDirectory = "
								+ extensionsDirectory, null);
	
			
				}
                extensionsDirectory = null;
            }
        }
        else {
            extensionsDirectory = new File("../lib/ext");
            logger.info("Searching Extensions in:"+extensionsDirectory.getAbsolutePath());
            if (!extensionsDirectory.exists()) {
                extensionsDirectory = null;
            }
        }
        plugInManager = new PlugInManager(context, extensionsDirectory, monitor);


        //Load drivers before initializing the frame because part of the frame
        //initialization is the initialization of the driver dialogs. [Jon Aquino]
        //The initialization of some plug-ins (e.g. LoadDatasetPlugIn) requires that
        //the drivers be loaded. Thus load the drivers here, before the plug-ins
        //are initialized.
        driverManager.loadDrivers(properties);
        
        //Por ultimo, pasamos al Blackboard (que es la configuracion) los parametros de linea de
        //comando referentes a ubicacion de CATALOGOS, SPATIAL_INDEX y FEATURE_COLLECTION
//azabala. esto lo he añadido yo
        /*
        blackboard.put(CATALOGS_DIRECTORY_OPTION,commandLine.getOption(CATALOGS_DIRECTORY_OPTION).getArg(0));
        blackboard.put(SPATIAL_INDEX_DIRECTOR_OPTION,commandLine.getOption(SPATIAL_INDEX_DIRECTOR_OPTION).getArg(0));
       */


       //obtenemos el valor por defecto del usaurio para el Escalado  de pintado

        String scalePaint = UserPreferenceStore.getUserPreference(SLDStyleImpl.SYMBOLIZER_SIZES_REFERENCE_SCALE,null,false);
        Double doubleScalePaint = null;
        if(scalePaint!=null)
        {
          try
          {
            doubleScalePaint = new Double(scalePaint);
          }catch(Exception e)
          {
            //e.printStackTrace();
            doubleScalePaint = null;
          }
        }
        appContext.getBlackboard().put(SLDStyleImpl.SYMBOLIZER_SIZES_REFERENCE_SCALE,doubleScalePaint);
         
    }

  

	public static void main(String[] args) {
        try {
        	
        	//System.out.println("STARTING....");
        	appContext.setUrl(AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.GEOPISTA_WEBAPP_NAME);
      		
            //Init the L&F before instantiating the progress monitor [Jon Aquino]
            initLookAndFeel();
            
            String nameIcon=UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_APPICON_KEY,null,false);
            String pathIcon=UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,UserPreferenceConstants.DEFAULT_DATA_PATH,false);
          URL urlIcon=new URL("file","",pathIcon+nameIcon);
         
            ImageIcon icono = 
    		nameIcon==null?
    				IconLoader.icon("app-icon.gif"):
    				new javax.swing.ImageIcon(urlIcon);
          APP_ICON=icono;
          
            ProgressMonitor progressMonitor = (ProgressMonitor) progressMonitorClass.newInstance();
            SplashPanel splashPanel = new SplashPanel(splashImage(), Constantes.VERSION_TEXT);
            splashPanel.add(progressMonitor,
                new GridBagConstraints(0, 10, 1, 1, 1, 0,
                    GridBagConstraints.NORTHWEST,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0, 0));
          
            main(args,
                 appContext.getString("localgis.release")+" GIS workbench",
                 new GEOPISTAConfiguration(),
                 splashPanel,
                 progressMonitor);
        } catch (Throwable t) {
            GeopistaWorkbenchFrame.handleThrowable(t, null);
        }
    }

 

	/**
     * setupClass is specified as a String to prevent it from being loaded before
     * we display the splash screen, in case setupClass takes a long time to load.
     * @param setupClass the name of a class that implements Setup
     * @param splashWindow a window to open until the workbench frame is displayed
     * @param taskMonitor notified of progress of plug-in loading
     */
    public static void main(String[] args,
                            String title,
                            Setup setup,
                            JComponent splashComponent,
                            TaskMonitor taskMonitor) {
    	  GEOPISTAWorkbench workbench=null;
    	  SplashWindow splashWindow = new SplashWindow(splashComponent);;
        try {
            //I don't know if we still need to specify the SAX driver [Jon Aquino 10/30/2003]
            System.setProperty("org.xml.sax.driver", "org.apache.xerces.parsers.SAXParser");

            initLookAndFeel();
          
			
            splashWindow.setVisible(true);
          
          
			workbench = new GEOPISTAWorkbench(title,
                    args,
                    APP_ICON,
                    splashWindow,
                    taskMonitor);

            localWorkbench = workbench;
        
            setup.setup(workbench.context, taskMonitor);

            workbench.context.getIWorkbench().getPlugInManager().load();
         


            //parche para carga automatica de capas
            /*String basepath = "C:/PruebasMap/data";
            Layer inicLayer = GeopistaUtil.loadData(basepath+"/prueba.jml","Catastro",workbench.context.getLayerManager(),workbench.context.getTask());*/

          
            
        } catch (Throwable t) {
            GeopistaWorkbenchFrame.handleThrowable(t, null);
        }
        finally
		{
        	if (workbench==null)
        	{
				logger.error("main(args = " + args + ", title = " + title
						+ ", setup = " + setup
						+ ") - No se puede iniciarla aplicación.", null);
				System.exit(-1);
        	}
        workbench.getGuiComponent().setVisible(true);
        splashWindow.setVisible(false);
		}
        if (commandLine.hasOption(CLOSE_OPTION)) {
            String closeAll = commandLine.getOption(CLOSE_OPTION).getArg(0);
            boolean closeAllBoolean = (new Boolean(closeAll)).booleanValue();
            if(closeAllBoolean==false)
            {
              ((GeopistaWorkbenchFrame)localWorkbench.getGuiComponent()).setCloseAll(closeAllBoolean);
            }

        }

        if (commandLine.hasOption(MAP_OPTION)) {
            String mapPath = commandLine.getOption(MAP_OPTION).getArg(0);
            try
            {
              loadMap(mapPath);
            }catch(Exception e)
            {
			logger
					.error(
							"main(String[], String, Setup, JComponent, TaskMonitor)",
							e);
            }
        }
    }


    public static void loadMap(String mapPath) throws Exception
    {
     File file = null;
      try
      {
        URL urlMap = new URL(mapPath);
        if(urlMap.getProtocol().equalsIgnoreCase("geopista"))
        {

            String idMapa = urlMap.getFile().substring(1);
            GeopistaMapUtil.loadDataBaseMap(idMapa,localWorkbench.context,null);
        }
        else
        {
          try
          {
        	  GeopistaMapUtil.loadMap(urlMap.getFile(),localWorkbench.context.getLayerManager(),localWorkbench.context.getBlackboard(),localWorkbench.getGuiComponent());
          }catch(Exception e1)
          {
           JOptionPane.showMessageDialog((Component) localWorkbench.context.getIWorkbench().getGuiComponent(),appContext.getI18nString("GeopistaEditor.MapaNoCorrecto"));
           return;
          }
        }
      }catch(MalformedURLException e)
      {
        try
        {
          file = new File(mapPath);
          GeopistaMapUtil.loadMap(mapPath,localWorkbench.context.getLayerManager(),localWorkbench.context.getBlackboard(),localWorkbench.getGuiComponent());
        }catch(Exception e1)
        {
         JOptionPane.showMessageDialog((Component) localWorkbench.context.getIWorkbench().getGuiComponent(),appContext.getI18nString("GeopistaEditor.MapaNoCorrecto"));
         return;
        }
      }
      
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

    public DriverManager getDriverManager() {
        return driverManager;
    }

    /**
     * The properties file; not to be confused with the WorkbenchContext
     * properties.
     */
    public WorkbenchProperties getProperties() {
        return properties;
    }

    public WorkbenchGuiComponent getGuiComponent() {
        return frame;
    }
    public WorkbenchFrame getFrame() {
    return (WorkbenchFrame) frame;
    }
    public WorkbenchContext getContext() {
        return context;
    }

    private static void parseCommandLine(String[] args) throws WorkbenchException {
        //<<TODO:QUESTION>> Notify MD: using CommandLine [Jon Aquino]
        commandLine = new CommandLine('-');
        commandLine.addOptionSpec(new OptionSpec(PROPERTIES_OPTION, OptionSpec.NARGS_ONE_OR_MORE));
        commandLine.addOptionSpec(new OptionSpec(PLUG_IN_DIRECTORY_OPTION, 1));
        commandLine.addOptionSpec(new OptionSpec(MAP_OPTION, 1));
        commandLine.addOptionSpec(new OptionSpec(CLOSE_OPTION, 1));
        commandLine.addOptionSpec(new OptionSpec(LIB_DIRECTORY_OPTION,1));
        /*
        AZABALA: aqui añadimos las nuevas opciones de linea de comando
*/
        commandLine.addOptionSpec(new OptionSpec(CATALOGS_DIRECTORY_OPTION, 1));
        commandLine.addOptionSpec(new OptionSpec(SPATIAL_INDEX_DIRECTOR_OPTION, 1));
          
        try {
            commandLine.parse(args);
        } catch (ParseException e) {
            throw new WorkbenchException(
                "A problem occurred parsing the command line: " + e.toString());
        }
    }

    public PlugInManager getPlugInManager() {
        return plugInManager;
    }

    //<<TODO>> Make some properties persistent using a #makePersistent(key) method. [Jon Aquino]
    /**
     * Expensive data structures can be cached on the blackboard so that several
     * plug-ins can share them.
     */
    public Blackboard getBlackboard() {
        return blackboard;
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
            add(component, BorderLayout.CENTER);
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
        }
    }

    private static class VerticallyScrollingProgressMonitor
        extends ProgressMonitor {
		/**
		 * Logger for this class
		 */
		private static final Log logger = LogFactory.getLog(VerticallyScrollingProgressMonitor.class);

        private static int ROWS = 3;
        private JLabel[] labels;

        public VerticallyScrollingProgressMonitor() {
            super(new JPanel(new GridLayout(ROWS, 1)));

            JPanel panel = (JPanel) getComponent();
            panel.setOpaque(false);
            labels = new JLabel[ROWS];

            for (int i = 0; i < ROWS; i++) {
                //" " not "", to give the label some height. [Jon Aquino]
                labels[i] = new JLabel(" ");
                labels[i].setFont(labels[i].getFont().deriveFont(Font.BOLD));
                panel.add(labels[i]);
            }
        }

        protected void addText(String s) {
            for (int i = 0; i < (ROWS - 1); i++) { //-1
                labels[i].setText(labels[i + 1].getText());
            }

            labels[ROWS - 1].setText(s);
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

    private static class HorizontallyScrollingProgressMonitor
        extends ProgressMonitor {
		/**
		 * Logger for this class
		 */
		private static final Log logger = LogFactory.getLog(HorizontallyScrollingProgressMonitor.class);

        private static final String BUFFER = "   ";

        public HorizontallyScrollingProgressMonitor() {
            super(new JLabel(" "));
            ((JLabel) getComponent()).setFont(((JLabel) getComponent()).getFont()
                                               .deriveFont(Font.BOLD));
            ((JLabel) getComponent()).setHorizontalAlignment(JLabel.RIGHT);
        }

        protected void addText(String s) {
            ((JLabel) getComponent()).setText(BUFFER + s +
                ((JLabel) getComponent()).getText());
        }
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
    // Ignora los eventos de Fence
    if (e.getLayer().getName()==FenceLayerFinder.LAYER_NAME) return;
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
    
    public boolean isFiringEvents() {
        return firingEvents;
    }

    public void setFiringEvents(boolean firingEvents) {
        this.firingEvents = firingEvents;
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

    public void removeLayerListener(LayerListener layerListener) {
        layerListeners.remove(layerListener);
    }

   
}
