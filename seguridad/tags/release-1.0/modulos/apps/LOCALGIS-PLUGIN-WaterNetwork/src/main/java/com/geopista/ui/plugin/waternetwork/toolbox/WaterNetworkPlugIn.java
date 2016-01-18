package com.geopista.ui.plugin.waternetwork.toolbox;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import com.geopista.ui.plugin.waternetwork.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class WaterNetworkPlugIn extends ToolBoxPlugIn{

	public String getName() {return I18N.get("WaterNetworkPlugIn","WaterNetwork");}
    public ImageIcon ICON = IconLoader.icon("tools.png");
    public static final String KEY = WaterNetworkPlugIn.class.getName();
    WorkbenchContext workbenchContext = null;
	PlugInContext plugInContext = null;	
	private Vector<AbstractPlugIn> aditionalPlugIns=new Vector<AbstractPlugIn>();

	@SuppressWarnings("unchecked")
	public WaterNetworkPlugIn(){
    	Locale loc=Locale.getDefault();
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.waternetwork.languages.WaterNetworkPlugIni18n",loc,
		this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("WaterNetworkPlugIn",bundle);
    }

    public void initialize(PlugInContext context) throws Exception{
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
        context.getWorkbenchContext().getIWorkbench().getGuiComponent().addComponentListener(new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    getToolbox(workbenchContext)
                                .addComponentListener(new ComponentAdapter(){
                            public void componentShown(ComponentEvent e){
                                toggleButton.setSelected(true);
                            }
                            public void componentHidden(ComponentEvent e){
                                toggleButton.setSelected(false);
                            }
                        });
                }
            });
    }

    protected void initializeToolbox(ToolboxDialog toolbox){    	
    	toolbox.setTitle(I18N.get("WaterNetworkPlugIn","Toolbox.Title"));
        for(AbstractPlugIn plugIn : aditionalPlugIns)
			plugIn.addButton(toolbox);
        toolbox.setInitialLocation(new GUIUtil.Location(20, true, 20, false));
        toolbox.setResizable(false);
    }

	public void addAditionalPlugIn(AbstractPlugIn plugIn){
		aditionalPlugIns.add(plugIn);
    }

	public void run(TaskMonitor monitor, PlugInContext context)	throws Exception{
		// TODO Auto-generated method stub
	}
}