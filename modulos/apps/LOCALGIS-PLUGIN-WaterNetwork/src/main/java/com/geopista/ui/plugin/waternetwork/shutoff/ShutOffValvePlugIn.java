/**
 * ShutOffValvePlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.waternetwork.shutoff;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.style.sld.model.SLDFactory;
import com.geopista.ui.cursortool.AbstractCursorTool;
import com.geopista.ui.plugin.GeopistaAddNewLayerPlugIn;
import com.geopista.ui.plugin.waternetwork.images.IconLoader;
import com.geopista.ui.plugin.waternetwork.toolbox.WaterNetworkPlugIn;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class ShutOffValvePlugIn extends AbstractPlugIn{
	
	private boolean shutOffValvePlugIn = false;
	public String getName(){return I18N.get("WaterNetworkPlugIn","ShutOffValve");}
	private static ShutOffTools shutOffTools = new ShutOffTools();
	static PlugInContext context;
	
	
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

	@SuppressWarnings({ "static-access", "unchecked" })
	public boolean execute(PlugInContext context) throws Exception{
		 Collection<Layer> layers;
	     layers = (Collection<Layer>) context.getWorkbenchContext().getLayerNamePanel().getLayerManager().getLayers();
	     for(Layer layer : layers){
	    	 if(layer.getName().equals(I18N.get("WaterNetworkPlugIn","ShutOffValve.Layer"))){
	    		 this.context = context;     
	    		 ToolboxDialog toolbox = new ToolboxDialog(context.getWorkbenchContext());
	    		 context.getLayerViewPanel().setCurrentCursorTool(ShutOffValvePointTool.create(toolbox.getContext()));
	    		 return true;
	    	 }	    		 
	     }
	     ErrorDialog.show(context.getWorkbenchFrame().getMainFrame(), I18N.get("WaterNetworkPlugIn","ShutOff.TitleError"), I18N.get("WaterNetworkPlugIn","ShutOff.Error"),"");
	     return false;
	}

	@SuppressWarnings("unchecked")
	public static UndoableCommand createAddCommand(Point point,AbstractCursorTool tool){
		// TODO Auto-generated method stub
		final Point p = point;
		final LayerViewPanel layerViewPanel = (LayerViewPanel) context.getWorkbenchContext().getLayerViewPanel();	
		Collection<Layer> layers = (Collection<Layer>) context.getWorkbenchContext().getLayerNamePanel().getLayerManager().getLayers();
		final ArrayList<Feature> features = new ArrayList<Feature>();
		final ArrayList<Feature> valves = new ArrayList<Feature>();
		for(Layer l : layers){
        	if (l.getName().equals(I18N.get("WaterNetworkPlugIn","ShutOffValve.Layer"))){
                FeatureCollection featureCollection = l.getFeatureCollectionWrapper();
                for (Iterator<Feature> i = featureCollection.iterator(); i.hasNext();){
                    Feature feature = i.next();
                    features.add(feature);
                    if(feature.getAttribute("tipo").equals("v"))valves.add(feature);
                }
        	}
    	}
		return new UndoableCommand(tool.getName()){
			public void execute(){
				final TaskMonitorDialog progressDialog= new TaskMonitorDialog(context.getWorkbenchFrame().getMainFrame(), null);
                progressDialog.setTitle(I18N.get("WaterNetworkPlugIn","ShutOffValve.Loading"));
                progressDialog.addComponentListener(new ComponentAdapter(){
                    public void componentShown(ComponentEvent e){
                        new Thread(new Runnable(){
							public void run(){
                                try{
                                	progressDialog.report(I18N.get("WaterNetworkPlugIn","ShutOffValve.Create"));
									ArrayList<Feature> houses = shutOffTools.shutOffValve(shutOffTools.pointNear(p,valves),features);
									if(!houses.isEmpty()){
										final Layer layer = layerViewPanel.getLayerManager().addLayer(StandardCategoryNames.WORKING,
												I18N.get("WaterNetworkPlugIn","ShutOffValve.PoliceNumbersAffected"),
												GeopistaAddNewLayerPlugIn.createBlankFeatureCollection());
										layer.setVisible(true);
										layer.setEditable(true);
										layer.getFeatureCollectionWrapper().addAll(houses);
										ArrayList<Feature> affected = new ArrayList<Feature>();										
										layer.removeStyle(layer.getBasicStyle());																							
										String xml = "<?xml version='1.0' encoding='ISO-8859-1'?>"+
													 "<sld:StyledLayerDescriptor version='String' xmlns:sld='http://www.opengis.net/sld' "+
													 "xmlns:gml='http://www.opengis.net/gml' xmlns:ogc='http://www.opengis.net/ogc' "+
													 "xmlns:xlink='http://www.w3.org/1999/xlink' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"+
													 "<sld:NamedLayer><sld:Name>"+layer.getName()+"</sld:Name><sld:UserStyle><sld:Name>default:Aislar</sld:Name>"+
													 "<sld:Title>Aislar</sld:Title><sld:IsDefault>1</sld:IsDefault><sld:FeatureTypeStyle><sld:Name>Aislar</sld:Name>"+
									           		 "<sld:Rule><sld:Name>Aislar</sld:Name><sld:LineSymbolizer><sld:Stroke>"+
													 "<sld:CssParameter name='stroke'>#FF3300</sld:CssParameter><sld:CssParameter name='stroke-width'>6</sld:CssParameter>"+
									                 "<sld:CssParameter name='stroke-linecap'>square</sld:CssParameter></sld:Stroke></sld:LineSymbolizer>"+
													 "</sld:Rule></sld:FeatureTypeStyle></sld:UserStyle></sld:NamedLayer></sld:StyledLayerDescriptor>";
										layer.addStyle(SLDFactory.createSLDStyle(xml, layer.getName()));
										for(Iterator<Feature> i=layer.getFeatureCollectionWrapper().iterator();i.hasNext();)
											affected.add(i.next());										
										final ShutOffValveFrame frame = new ShutOffValveFrame(context,affected);
			                    		((WorkbenchGuiComponent)context.getWorkbenchGuiComponent()).addInternalFrame( frame);
			                    		frame.position(); 									}
									else
										JOptionPane.showMessageDialog(context.getWorkbenchFrame().getMainFrame(), I18N.get("WaterNetworkPlugIn","ShutOffValve.Error"), I18N.get("WaterNetworkPlugIn","ShutOffValve.TitleError"), 1);
                                }catch(Exception e){
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
			@Override
			public void unexecute(){
				// TODO Auto-generated method stub				
			}
		};
	}
	
	public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext){		
		EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
		return new MultiEnableCheck()
		.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
		.add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
		.add(checkFactory.createTaskWindowMustBeActiveCheck())
		.add(checkFactory.createAtLeastNLayersMustExistCheck(1));
	}

	public void addButton(final ToolboxDialog toolbox){
		if (!shutOffValvePlugIn){
			toolbox.addToolBar();
			ShutOffValvePlugIn select = new ShutOffValvePlugIn();
		    toolbox.addPlugIn(select, null, select.getIcon());
		    toolbox.finishAddingComponents();
		    toolbox.validate();
		    shutOffValvePlugIn = true;
		}
	}

	public Icon getIcon(){
		return IconLoader.icon("shut_off_valve.png");
    }
}