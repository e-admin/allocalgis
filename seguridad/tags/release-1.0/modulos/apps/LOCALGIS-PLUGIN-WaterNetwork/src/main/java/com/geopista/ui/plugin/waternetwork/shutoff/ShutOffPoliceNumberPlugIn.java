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

public class ShutOffPoliceNumberPlugIn extends AbstractPlugIn{
	
	private boolean shutOffHousePlugIn = false;
	public String getName(){return I18N.get("WaterNetworkPlugIn","ShutOffPoliceNumber");}
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
	    	 if(layer.getName().equals(I18N.get("WaterNetworkPlugIn","ShutOffPoliceNumber.Layer"))){
	    		 this.context = context;     
	    		 ToolboxDialog toolbox = new ToolboxDialog(context.getWorkbenchContext());
	    		 context.getLayerViewPanel().setCurrentCursorTool(ShutOffPoliceNumberPointTool.create(toolbox.getContext()));
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
		Collection<Layer> layers = (Collection<Layer>) 
			context.getWorkbenchContext().getLayerNamePanel().getLayerManager().getLayers();
		final ArrayList<Feature> features = new ArrayList<Feature>();
		final ArrayList<Feature> features2 = new ArrayList<Feature>();
		final ArrayList<Feature> houses = new ArrayList<Feature>();
		for(Layer l : layers){
        	if (l.getName().equals(I18N.get("WaterNetworkPlugIn","ShutOffPoliceNumber.Layer"))){
                FeatureCollection featureCollection = l.getFeatureCollectionWrapper();
                for (Iterator<Feature> i = featureCollection.iterator(); i.hasNext();){
                    Feature feature = i.next();
                    features.add(feature);features2.add(feature);
                    if(feature.getAttribute("tipo").equals("h"))houses.add(feature);
                }
        	}
    	}
		return new UndoableCommand(tool.getName()){
			public void execute(){
				 final TaskMonitorDialog progressDialog= new TaskMonitorDialog(context.getWorkbenchFrame().getMainFrame(), null);
	                progressDialog.setTitle(I18N.get("WaterNetworkPlugIn","ShutOffPoliceNumber.Loading"));
	                progressDialog.addComponentListener(new ComponentAdapter(){
	                    public void componentShown(ComponentEvent e){
	                        new Thread(new Runnable(){
	                            public void run(){
	                                try{
	                                	progressDialog.report(I18N.get("WaterNetworkPlugIn","ShutOffPoliceNumber.Create"));
	                                	ArrayList<Feature> valves = shutOffTools.shutOffAlg(features, shutOffTools.pointNear(p,houses));
										if(!valves.isEmpty()){
											final Layer layer = layerViewPanel.getLayerManager().addLayer(StandardCategoryNames.WORKING,
													I18N.get("WaterNetworkPlugIn","ShutOffPoliceNumber.ValvesClosed"), GeopistaAddNewLayerPlugIn.createBlankFeatureCollection());
											layer.setVisible(true);
											layer.setEditable(true);											
											layer.getFeatureCollectionWrapper().addAll(shutOffTools.optimizeV(valves, features2));
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
										}
										else
											JOptionPane.showMessageDialog(context.getWorkbenchFrame().getMainFrame(), I18N.get("WaterNetworkPlugIn","ShutOffPoliceNumber.Error"), I18N.get("WaterNetworkPlugIn","ShutOffPoliceNumber.TitleError"), 1);	
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
		if (!shutOffHousePlugIn){
			toolbox.addToolBar();
			ShutOffPoliceNumberPlugIn select = new ShutOffPoliceNumberPlugIn();
		    toolbox.addPlugIn(select, null, select.getIcon());
		    toolbox.finishAddingComponents();
		    toolbox.validate();
		    shutOffHousePlugIn = true;
		}
	}

	public Icon getIcon(){
		return IconLoader.icon("shut_off_house.png");
    }
}