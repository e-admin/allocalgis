/**
 * GeopistaNetworkEditingPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.toolboxnetwork;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Graphable;
import org.uva.geotools.graph.structure.Node;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.routeserver.ElementNotFoundException;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.editor.WorkBench;
import com.geopista.ui.GEOPISTAWorkbench;
import com.geopista.ui.plugin.routeenginetools.routeconfig.CalcRutaConfigDialog;
import com.geopista.ui.plugin.routeenginetools.routeconfig.CalcRutaConfigFileReaderWriter;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.toolboxnetwork.images.IconLoader;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.FeatureEventType;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;


public class GeopistaNetworkEditingPlugIn extends ToolboxNetworkPlugIn implements LayerListener {
	
    AppContext appContext = (AppContext) AppContext.getApplicationContext();
     
    public String getName() { return "Editing Route Toolbox"; }

    public static ImageIcon ICON = IconLoader.icon("btn_herramruta.gif");

    public static final String KEY = GeopistaNetworkEditingPlugIn.class.getName();
    public static final Log LOG=LogFactory.getLog(GeopistaNetworkEditingPlugIn.class);
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
       
        toggleNetworkToolbox = new JToggleButton();
        String networkToolbarName = this.appContext.getI18nString("ui.MenuNames.TOOLS.ROUTEENGINEADMINISTRATION");
	context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar(networkToolbarName).add(toggleNetworkToolbox, this.getName(), this.ICON,AbstractPlugIn.toActionListener(this, context.getWorkbenchContext(),
                new TaskMonitorManager()), null);
//        context.getWorkbenchContext().getIWorkbench().getGuiComponent();
        context.getWorkbenchContext().getIWorkbench().getGuiComponent().addComponentListener(new ComponentAdapter() {
                public void componentShown(ComponentEvent e) {
                    //Can't #getToolbox before Workbench is thrown. Otherwise, get 
                    //IllegalComponentStateException. Thus, do it inside #componentShown. [Jon Aquino]
                    getToolbox(workbenchContext)
                                 .addComponentListener(new ComponentAdapter() {
                            //There are other ways to show/hide the toolbox. Track 'em. [Jon Aquino]
                            public void componentShown(ComponentEvent e) {
                                toggleNetworkToolbox.setSelected(true);
                            }

                            public void componentHidden(ComponentEvent e) {
                                toggleNetworkToolbox.setSelected(false);
                            }
                        });
                }
            });
        
        // Register Layer events
        WorkBench frame = context.getWorkbenchContext().getIWorkbench();
        if (frame instanceof GeopistaEditor)
            {
                ((GeopistaEditor) frame).addLayerListener(this);
            }

            if (frame instanceof GEOPISTAWorkbench)
            {
                ((GEOPISTAWorkbench) frame).addLayerListener(this);
            }
// Boton para activar rastreo
            MiEnableCheckFactory checkFact=new MiEnableCheckFactory(context.getWorkbenchContext());
            activateSearch = new JToggleButton("Feature->Arco",false);
            context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar(networkToolbarName)
            .add((AbstractButton)activateSearch, 
        	    "Activa rastreo de enlace de datos Feature->Arcos",
        	    (Icon)com.geopista.ui.images.IconLoader.icon("SheetMultiple.gif"),
        	    null,
        	    checkFact.createNetworksMustBeLoadedCheck());
    }
    
    private CalcRutaConfigFileReaderWriter configProperties =new CalcRutaConfigFileReaderWriter();
    
    private void onConfiguratorButtonDo() {
		// TODO Auto-generated method stub
		CalcRutaConfigDialog dialog = getConfigDialog();

		this.configProperties.loadPropertiesFromConfigFile();

	} 
    
    private CalcRutaConfigDialog getConfigDialog(){
    	
		CalcRutaConfigDialog configDialog = new CalcRutaConfigDialog(null, NetworkModuleUtilWorkbench.getNetworkManager(plugInContext));
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

	protected JToggleButton activateSearch;

	protected JToggleButton toggleNetworkToolbox;
	
	public void addAditionalPlugIn(AbstractPlugIn plugIn)
    {
		aditionalPlugIns.add(plugIn);
    }



	@Override
	public void featuresChanged(FeatureEvent e)
	{
	    if (!activateSearch.isSelected())
		return;
	    // Adivina si corresponde con una feature de red y propone 
	    // actualizar los campos
	    Collection features = e.getFeatures();
	    for (Object object : features)
		{
		  if (object==null)
		      {
			  continue;
		      }
		    Feature feature = (Feature)object;
		    try{ 
		    String netName=feature.getString("networkName");
		    Object idEjeObj=feature.getAttribute("idEje");
		    
		    // Busca el tramo en la red
		    NetworkManager netMgr = NetworkModuleUtilWorkbench.getNetworkManager(plugInContext);
		    Network network= netMgr.getNetwork(netName);
		    if (network!=null)
			{
			    Graph graph =network.getGraph();
			    int intId = Integer.parseInt(idEjeObj.toString());
			    Edge edge=graph.getEdge(intId);
			    if (e.getType()==FeatureEventType.DELETED)
				{
				    if (graph instanceof DynamicGraph)
					{
					    DynamicGraph dynGraph = (DynamicGraph) graph;
					    int response = JOptionPane.showConfirmDialog(plugInContext.getWorkbenchFrame(),"Se ha borrado la Feature: "+feature.getID()+" asociada al arco "+netName+":"+edge.getID()+".\n Â¿Desea eliminar el arco de la red igualmente?Â¿Desea traspasar los nuevos datos al arco de la red? Pulse CANCEL si quiere desactivar este proceso automático.","Eliminación de arco", JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION);
					    if (JOptionPane.YES_OPTION==response)
					     {
						 dynGraph.removeGraphable(edge);
					     }else
					if (response==JOptionPane.CANCEL_OPTION)
					    {
						activateSearch.setSelected(false);
					    }
					}
				}
			    else if (e.getType()==FeatureEventType.ATTRIBUTES_MODIFIED)
				{
				    if (edge==null) // No está en la red pero tiene la red asignada atributos Â¿esta en creación?
					{
					plugInContext.getWorkbenchGuiComponent().warnUser("No implementado: creación de un nuevo arco.");
					}
				    else
				    {
					int response = JOptionPane.showConfirmDialog(plugInContext.getWorkbenchFrame(),"Se ha modificado la Feature: "+feature.getID()+" asociada al arco "+netName+":"+edge.getID()+".\n Â¿Desea traspasar los nuevos datos al arco de la red? Pulse CANCEL si quiere desactivar este proceso automático.","Modificación de arco", JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION);
					if (JOptionPane.YES_OPTION==response)
					     {
						 if (copyAttributes(feature, edge, graph))
						     plugInContext.getWorkbenchGuiComponent().warnUser("Arco modificado en la red cargada:"+netName);
						 else
						     plugInContext.getWorkbenchGuiComponent().warnUser("Arco NO modificado. Algún error en los valores");

					     }
					else
					if (response==JOptionPane.CANCEL_OPTION)
					    {
						activateSearch.setSelected(false);
					    }
				    }
				}
			   
			}
		    
		   }
		   catch(NumberFormatException ex)
			{
			    if (LOG.isTraceEnabled())
				    LOG.trace("Feature(idEdje) "+feature+" no contiene un identificador válido.",ex);
			}
		    catch(IllegalArgumentException ex)
		    {
			// feature no representa red de arcos
			if (LOG.isTraceEnabled())
			    LOG.trace("Feature "+feature+" no representa una red.",ex);
		    } catch (ElementNotFoundException ex)
			{
			    if (LOG.isTraceEnabled())
				    LOG.trace("Feature "+feature+" no identifica un Edge existente.",ex);
			}
		}
	}


	public boolean copyAttributes(Feature feature, Edge edge, Graph graph) throws ElementNotFoundException
	{
	    boolean touched=false;
	    // Pasa los atributos al ILocalGISEdge
	    if (edge instanceof ILocalGISEdge)
	    {
	        ILocalGISEdge lgEdge = (ILocalGISEdge) edge;
	        int attNum=feature.getSchema().getAttributeCount();
	        FeatureSchema schema = feature.getSchema();
	        for (int i = 0; i < attNum; i++)
	    	{
	    	    
	    	String attributeName = schema.getAttributeName(i);
		Object attribute = feature.getAttribute(i);
		/**
		 * Check editing the graph
		 */
	    	    if ("idNodoA".equals(attributeName))
		    {
			touched=touched|changeNode(graph,lgEdge,lgEdge.getNodeA(),Integer.parseInt(attribute.toString()));
		    }
		else
		    if ("idNodoB".equals(attributeName))
			{
		
			touched=touched| changeNode(graph,lgEdge,lgEdge.getNodeB(),Integer.parseInt(attribute.toString()));
		    }
		    else			
		touched=touched | lgEdge.setAttribute(attributeName,attribute);
			   
	    	    
	    	}
	        if (touched && graph instanceof DynamicGraph)
			{
			    ((DynamicGraph) graph).touch(lgEdge);							    
			}
	    }
	    return touched;
	}
/**
 * Cambia el grafo moviendo el arco del nodo indicado a otro con el identificador pasado
 * @param graph
 * @param lgEdge
 * @param nodeB
 * @param parseInt
 * @return true si se han realizado cambios en el edge
 */
	private boolean changeNode(Graph graph, ILocalGISEdge lgEdge, Node nodeFrom, int idNode)
	{
	    // busca nodo destino
	    try
		{
		    Node toNode= graph.getNode(idNode);
		    if (nodeFrom.equals(toNode))
			return false;
		    lgEdge.setOtherNode(lgEdge.getOtherNode(nodeFrom), toNode); // cambia el nodo nodeFrom por toNode. Sí, reconozco que es curioso el mÃ©todo ;)
		    nodeFrom.remove(lgEdge);
		    toNode.add(lgEdge);
		    ArrayList<Graphable> dirt=(ArrayList<Graphable>) Arrays.asList(lgEdge,nodeFrom,toNode);
		    NetworkModuleUtil.addDirtyGraphablesIsSupported(graph, dirt);
		    JOptionPane.showMessageDialog(workbenchContext.getIWorkbench().getFrame(), "El arco "+lgEdge.getID()+" ahora une los nodos :"+lgEdge.getNodeA()+" y "+lgEdge.getNodeB()+".  Las representaciones gráficas y las geometrías no se pueden modificar mediante este proceso.");
		   return true;

		} catch (ElementNotFoundException e)
		{
		   JOptionPane.showMessageDialog(workbenchContext.getIWorkbench().getFrame(), "El nodo "+idNode+"no existe en el grafo. El cambio del atributo idNodo(A/B) en la feature no se ha traducido en ningún cambio en el grafo.");
		   return false; // informa que no hay cambios
		}
	}


	private void touchedchangeNode(Graph graph, Node nodeB, int parseInt)
	{
	    // TODO Auto-generated method stub
	    
	}


	@Override
	public void layerChanged(LayerEvent e)
	{
	    // TODO Auto-generated method stub
	    
	}


	@Override
	public void categoryChanged(CategoryEvent e)
	{
	    // TODO Auto-generated method stub
	    
	}


	@Override
	public void run(TaskMonitor monitor, PlugInContext context) throws Exception
	{
	    // TODO Auto-generated method stub
	    
	}
	
    
    
}
    