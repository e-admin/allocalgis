/**
 * NetworkPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.waternetwork.network;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.waternetwork.images.IconLoader;
import com.geopista.ui.plugin.waternetwork.toolbox.WaterNetworkPlugIn;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class NetworkPlugIn extends AbstractPlugIn{

    private boolean newtworkPlugIn = false;
    public String getName() {return I18N.get("WaterNetworkPlugIn","Network");}
    private NetworkTools networkTools = new NetworkTools();
    private AppContext appContext = (AppContext) AppContext.getApplicationContext();

    @SuppressWarnings("unchecked")
	public void initialize(PlugInContext context) throws Exception{
    	Locale loc=Locale.getDefault();
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.waternetwork.languages.WaterNetworkPlugIni18n",loc,
				this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("WaterNetworkPlugIn",bundle2);
        String pathMenuNames[] =new String[] { MenuNames.EDIT };
		String name = I18N.get("WaterNetworkPlugIn",getName());
        context.getFeatureInstaller().addMainMenuItem(this, pathMenuNames, name,
    			false, null, createEnableCheck(context.getWorkbenchContext()));
        WaterNetworkPlugIn moduloAguasPI = (WaterNetworkPlugIn) (context.getWorkbenchContext().getBlackboard().get(WaterNetworkPlugIn.KEY));
        moduloAguasPI.addAditionalPlugIn(this);
    }

	@SuppressWarnings("unchecked")
	public boolean execute(final PlugInContext context) throws Exception{
		reportNothingToUndoYet(context);
    	Collection<Layer> layers; 
        layers = (Collection<Layer>) context.getWorkbenchContext().getLayerNamePanel().getLayerManager().getLayers();    
        boolean ok = false;
        for(final Layer layer : layers)
            if (layer.getName().equals(I18N.get("WaterNetworkPlugIn","Network.FusionLayer"))) {
            	ok = true;
            	if (!appContext.isLogged()){
            		appContext.setProfile("Geopista");
            		appContext.login();
        		}
            	if(appContext.isLogged()){
            		final TaskMonitorDialog progressDialog= new TaskMonitorDialog(context.getWorkbenchFrame().getMainFrame(), null);
	    	        progressDialog.setTitle(I18N.get("WaterNetworkPlugIn","Network.Loading"));
	    	        progressDialog.addComponentListener(new ComponentAdapter(){
	    	            public void componentShown(ComponentEvent e){
	    	                new Thread(new Runnable(){
	    	                    public void run(){
	    	                        try{
	    	                        	progressDialog.report(I18N.get("WaterNetworkPlugIn","Network.LoadDB"));
	    	                        	ArrayList<Feature> pipes = new ArrayList<Feature>();
	    	                            for (Iterator<Feature> i = layer.getFeatureCollectionWrapper().iterator(); i.hasNext();)
	    	                            	 pipes.add(i.next());
	    	                            for(Feature pipe: pipes)
	    	                            	networkTools.dividePipes(layer, pipe, pipes);
	    	                        	networkTools.loadNetworkLayer(appContext,context,layer);
	    	                        }catch(Exception e){
	    	                            ErrorDialog.show(context.getWorkbenchFrame().getMainFrame(), I18N.get("WaterNetworkPlugIn","Network.TitleError"), I18N.get("WaterNetworkPlugIn","Network.Error"), StringUtil.stackTrace(e));
	    	                            return;
	    	                        }finally{
	    	                            progressDialog.setVisible(false);
	    	                        }
	    	                    }
	    	                }).start();
	    	            }
	    	        });
	    	        GUIUtil.centreOnWindow(progressDialog);
	    	        progressDialog.setVisible(true);
            	}            		          		           		
            } 
          	if(ok==false)   		
        	  ErrorDialog.show(context.getWorkbenchFrame().getMainFrame(), I18N.get("WaterNetworkPlugIn","Network.TitleError"), I18N.get("WaterNetworkPlugIn","Network.Error3"),"");
        return true;
    }

    public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext){
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
        		.add(checkFactory.createAtLeastNLayersMustExistCheck(1));
    }

    public void addButton(final ToolboxDialog toolbox){
        if (!newtworkPlugIn){
        	toolbox.addToolBar();
        	NetworkPlugIn select = new NetworkPlugIn();
            toolbox.addPlugIn(select, null, select.getIcon());
            toolbox.finishAddingComponents();
            toolbox.validate();
            newtworkPlugIn = true;
        }
    }

    public Icon getIcon(){
        return IconLoader.icon("gen_net.png");
    }
}