package com.geopista.ui.plugin.toolboxnetwork;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.routeconfig.CalcRutaConfigDialog;
import com.geopista.ui.plugin.routeenginetools.routeconfig.CalcRutaConfigFileReaderWriter;
import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.geopista.ui.plugin.toolboxnetwork.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;


public class GeopistaNetworkEditingPlugIn extends ToolboxNetworkPlugIn {
	
    AppContext appContext = (AppContext) AppContext.getApplicationContext();
     
    public String getName() { return "Editing Route Toolbox"; }

    public static ImageIcon ICON = IconLoader.icon("btn_herramruta.gif");

    public static final String KEY = GeopistaNetworkEditingPlugIn.class.getName();

    WorkbenchContext workbenchContext = null;

	private JButton configureButton = null;
	
	PlugInContext plugInContext = null;
	
	
	public GeopistaNetworkEditingPlugIn() {
    	Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.toolboxnetwork.languages.ToolBoxNetworkPlugIni18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("ToolBoxNetwork",bundle);
    }

	
    public void initialize(PlugInContext context) throws Exception {
    	
    	
    	 if(!isRegisteredPlugin(this,context))
    		 registerPlugin(this,context);
    	    	
        context.getWorkbenchContext().getIWorkbench().getBlackboard().put(KEY, this);

        workbenchContext = context.getWorkbenchContext();
        
        plugInContext = context;

        final JToggleButton toggleButton = new JToggleButton();
        context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar().add(toggleButton,
            this.getName(), this.ICON,
            AbstractPlugIn.toActionListener(this, context.getWorkbenchContext(),
                new TaskMonitorManager()), null);
        context.getWorkbenchContext().getIWorkbench().getGuiComponent();
        context.getWorkbenchContext().getIWorkbench().getGuiComponent().addComponentListener(new ComponentAdapter() {
                public void componentShown(ComponentEvent e) {
                    //Can't #getToolbox before Workbench is thrown. Otherwise, get 
                    //IllegalComponentStateException. Thus, do it inside #componentShown. [Jon Aquino]
                    getToolbox(workbenchContext)
                                 .addComponentListener(new ComponentAdapter() {
                            //There are other ways to show/hide the toolbox. Track 'em. [Jon Aquino]
                            public void componentShown(ComponentEvent e) {
                                toggleButton.setSelected(true);
                            }

                            public void componentHidden(ComponentEvent e) {
                                toggleButton.setSelected(false);
                            }
                        });
                }
            });
    }
    
    private CalcRutaConfigFileReaderWriter configProperties =new CalcRutaConfigFileReaderWriter();
    
    private void onConfiguratorButtonDo() {
		// TODO Auto-generated method stub
		CalcRutaConfigDialog dialog = getConfigDialog();

		this.configProperties.loadPropertiesFromConfigFile();

	} 
    
    private CalcRutaConfigDialog getConfigDialog(){
    	
		CalcRutaConfigDialog configDialog = new CalcRutaConfigDialog(null, FuncionesAuxiliares.getNetworkManager(plugInContext));
		configDialog.setVisible(true);

		if (configDialog.finished()){
			return configDialog;
		}

		return null;
	}

//    protected void initializeToolbox(ToolboxNetworkDialog toolbox) {
    protected void initializeToolbox(ToolboxDialog toolbox) {
        
    	toolbox.setTitle(I18N.get("ToolBoxNetwork","com.geopista.ui.plugin.toolboxnetwork.title"));
        
    	configureButton = new JButton(I18N.get("ToolBoxNetwork","com.geopista.ui.plugin.toolboxnetwork.configure"));
    	
        configureButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			onConfiguratorButtonDo();
    		}
    	});
        
        EnableCheckFactory checkFactory = new EnableCheckFactory(toolbox.getContext());
                 
        for (Iterator plugInIter = aditionalPlugIns.iterator(); plugInIter.hasNext();) 
		{
			AbstractPlugIn plugIn = (AbstractPlugIn) plugInIter.next();
			plugIn.addButton(toolbox);
		 
		}
        
        toolbox.getCenterPanel().add(configureButton, BorderLayout.CENTER);
        
        toolbox.setInitialLocation(new GUIUtil.Location(20, true, 20, false));
        toolbox.setResizable(false);
        
    }

	protected JButton getOptionsButton() {
		return configureButton;
	}
	
	
	private Vector aditionalPlugIns=new Vector();
	
	public void addAditionalPlugIn(AbstractPlugIn plugIn)
    {
		aditionalPlugIns.add(plugIn);
    }



	@Override
	public void run(TaskMonitor monitor, PlugInContext context)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
    
    
}
    