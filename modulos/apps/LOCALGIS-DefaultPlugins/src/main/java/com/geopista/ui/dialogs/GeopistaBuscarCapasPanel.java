/**
 * GeopistaBuscarCapasPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaLayerManager;
import com.geopista.ui.GeopistaLayerNameRenderer;
import com.geopista.ui.GeopistaOneLayerAttributeTab;
import com.geopista.ui.components.FeatureExpressionPanel;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.CloneableInternalFrame;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelProxy;
import com.vividsolutions.jump.workbench.ui.SelectionManager;
import com.vividsolutions.jump.workbench.ui.SelectionManagerProxy;
import com.vividsolutions.jump.workbench.ui.TaskFrameProxy;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;
public class GeopistaBuscarCapasPanel extends JPanel
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
												.getLog(GeopistaBuscarCapasPanel.class);

  private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
  private JPanel pnlVentana = new JPanel();
  private JLabel lblCapa = new JLabel();
  private JPanel pnlVentanaRb = new JPanel();
  private JLabel lblTexto = new JLabel();
  private JButton btnCerrar = new JButton();
  private JButton btnBuscar = new JButton();
  private JLabel lblCampo = new JLabel();
  private JTextField txtTexto = new JTextField();
  private JComboBox cmbCapa = new JComboBox();
  private JComboBox cmbCampo = new JComboBox();
  private JCheckBox chkPalabras = new JCheckBox();
  private JCheckBox chkShift = new JCheckBox();
  private List layerList = null;
  private PlugInContext context = null;
  private Collection attributeCollection=new ArrayList();
  private HashMap layerAttributes = new HashMap();
	private JPanel literalSearchPanel = null;
	private JTabbedPane jTabbedPane = null;
	private JPanel formulaSearchPanel = null;
	private SelectLayerFieldPanel selectLayerFieldPanel = null;
	private FeatureExpressionPanel featureExpressionPanel = null;
	private JPanel jPanel = null;
	private JPanel jPanel2 = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
  public GeopistaBuscarCapasPanel(List layerList,PlugInContext context, ToolboxDialog toolbox)
  {
    this.layerList = layerList;
    this.context = context;
      jbInit();
      
      toolbox.addWindowListener(new WindowAdapter() {
    	
        public void windowActivated(WindowEvent e) {
            refresh(null);
        }
    });
//    GUIUtil
//        .addInternalFrameListener( // solo para el modelo MDI con InternalFrames
//            (JDesktopPane)toolbox.getContext().getWorkbench().getFrame().getDesktopPane(),
//            GUIUtil.toInternalFrameListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//            if (e.getID()==InternalFrameEvent.INTERNAL_FRAME_ACTIVATED ||
//            		e.getID()== InternalFrameEvent.INTERNAL_FRAME_OPENED)
//            	refresh(null);
//        }
//
//		
//    }));
  }
public void refresh(PlugInContext context)
{
	if (context!=null)
		this.context=context;
	selectLayerFieldPanel.updateLayerList();
	updateFeatureExpr();
	// para la parte de busqueda literal
	try
		{
		List layerList = this.context.getWorkbenchGuiComponent()
				.getActiveTaskComponent().getLayerManager().getLayers();
		this.layerList = layerList;
		refreshComponents();
		} catch (NullPointerException e)
		{
		// Ventana que no es un mapa
			if (logger.isDebugEnabled())
				{
				logger.debug("refresh(PlugInContext) - no era un mapa");
				}
		}
}
  private void jbInit() 
  {
  
    java.awt.GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
    java.awt.GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
    this.setLayout(new GridBagLayout());
//    this.setSize(new Dimension(564, 300));
    lblCapa.setText("Capa:");
    btnCerrar.setText("Cerrar");
    btnBuscar.setText("Buscar");
    lblTexto.setText("Texto a buscar:");
    ButtonGroup grupoRadio = new ButtonGroup();

    String elementos_lista[];
    elementos_lista =       new String[6];
    chkPalabras.setText("Palabras completas");
    chkShift.setText("Mayúsculas y Minúsculas");
    lblCampo.setText("Campo:");
    cmbCapa.setRenderer(new GeopistaLayerNameRenderer());
    cmbCapa.addActionListener(new java.awt.event.ActionListener() { 
    	public void actionPerformed(java.awt.event.ActionEvent e) {    
    		 cmbCapa_actionPerformed(e);
    	}
    });
    btnCerrar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cancelarbtn_actionPerformed(e);
        }
      });

    cmbCapa.setBackground(Color.white);
    cmbCapa.setPreferredSize(new java.awt.Dimension(126,21));
    cmbCampo.setBackground(Color.white);
    cmbCampo.setPreferredSize(new java.awt.Dimension(126,21));
    gridBagConstraints10.gridx = 1;
    gridBagConstraints10.gridy = 2;
    gridBagConstraints10.gridwidth = -1;
    gridBagConstraints10.gridheight = -1;
    gridBagConstraints10.ipadx = -10;
    gridBagConstraints10.ipady = -10;
    this.setSize(300, 230);
    this.setPreferredSize(new java.awt.Dimension(300,230));
    gridBagConstraints16.gridx = 0;
    gridBagConstraints16.gridy = 0;
    gridBagConstraints16.weightx = 1.0;
    gridBagConstraints16.weighty = 1.0;
    gridBagConstraints16.fill = java.awt.GridBagConstraints.BOTH;
    this.add(pnlVentanaRb, gridBagConstraints10);
    this.add(getJTabbedPane(), gridBagConstraints16);
    
refreshComponents();
    
  
    btnBuscar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnBuscar_actionPerformed(e);
        }
      });
    
  

  }

  

  /**
 * Refresca los componentes del panel de busqueda literal
 */
private void refreshComponents()
{
	  
    List layers = this.getLayers();
    if (layers==null)return;
    Layer selected = (Layer) cmbCapa.getSelectedItem();
    
    ListIterator Iter =  layers.listIterator();
   cmbCapa.removeAllItems();
   cmbCampo.removeAllItems();
    ArrayList todos = new ArrayList();
    todos.add("Todos los campos");
    layerAttributes.clear();
    layerAttributes.put("Todas las capas",todos);
    
    cmbCampo.addItem("Todos los campos");
    Vector clonelayers = new Vector(layers);
    clonelayers.add(0,dummyLayer);
    GeopistaFunctionUtils.initializeLayerComboBox("Layers",cmbCapa,selected==null?dummyLayer:selected,"LAYERS",clonelayers);
    
    while (Iter.hasNext()) {
        Layer lyr = (Layer)Iter.next();
        ArrayList attributes = new ArrayList();
        
        for (int i=0; i<lyr.getFeatureCollectionWrapper().getFeatureSchema().getAttributeCount();i++)
        {
          attributes.add(lyr.getFeatureCollectionWrapper().getFeatureSchema().getAttributeName(i));
        }
        String layer = lyr.getName();
        layerAttributes.put(layer,attributes);
        //cmbCapa.addItem(layer);
        
        // simula una selección
        //cmbCapa_actionPerformed(null);
    }
}
private void cancelarbtn_actionPerformed(ActionEvent e)
  {
    JDialog dialogPadre = (JDialog) SwingUtilities.getAncestorOfClass(JDialog.class,this);
    dialogPadre.setVisible(false);
  }
  private List getLayers()
  {
    return this.layerList;
  }
  private ArrayList getAttributes(String layer, boolean remove)
  {
    if (remove == true)
    {
      this.cmbCampo.removeAllItems();  
    }
    ArrayList attributes = (ArrayList)layerAttributes.get(layer);
    return attributes;
    
  }

  

  private void cmbCapa_actionPerformed(ActionEvent e)
  {

    Layer layer = (Layer)cmbCapa.getSelectedItem(); 
    if (layer==null)return;
   FeatureSchema sch = layer.getFeatureCollectionWrapper().getFeatureSchema();

    
    cmbCampo.removeAllItems();
    for(int i=0;i<sch.getAttributeCount();i++)
    {
        String attribute = sch.getAttributeName(i);
        cmbCampo.addItem(attribute);
    }
  }
  private void btnBuscarExpresion(ActionEvent e)
  {
  	GeopistaLayer layer=(GeopistaLayer) selectLayerFieldPanel.getSelectedLayer();
    Collection attributeCollection = GeopistaFunctionUtils.searchByExpression(layer, this.featureExpressionPanel.getExpParser());
    final ViewAttributesFrame frame = new ViewAttributesFrame(context,attributeCollection,layer.getSystemId());
//
//    // REVISAR: Cast to WorkbenchFrame for the MDI model
    ((WorkbenchGuiComponent)context.getWorkbenchGuiComponent()).addInternalFrame( frame);
    JDialog dialogPadre = (JDialog) SwingUtilities.getAncestorOfClass(JDialog.class,this);
    dialogPadre.setVisible(false); 
  }
  private static Layer dummyLayer=new Layer("Todas las Capas", Color.WHITE, new FeatureDataset(new FeatureSchema()) , new GeopistaLayerManager());
  private void btnBuscar_actionPerformed(ActionEvent e)
  {
      Layer layer= (Layer) cmbCapa.getSelectedItem();
      String layerName=layer instanceof GeopistaLayer ? ((GeopistaLayer)layer).getSystemId():layer.getName();
      if (layer == dummyLayer)
      	layerName = "Todas las capas";
      String attributeName = (String)cmbCampo.getSelectedItem();
      String value = this.txtTexto.getText();

      LayerManager layerManager = context.getLayerManager();
      if (layerName=="Todas las capas")
      {
        for(int i=1;i<cmbCapa.getItemCount();i++)
        {
          layerName = cmbCapa.getItemAt(i).toString();
          ArrayList attributes = this.getAttributes(layerName,false);
          Iterator Iter =  attributes.iterator();

          Collection attributeTemp = null;       
          ArrayList layerNames = (ArrayList)context.getLayerManager().getLayers();
          while (Iter.hasNext()) {
                  attributeName = (String)Iter.next();

                  attributeTemp = GeopistaFunctionUtils.searchByAttribute(layerManager,layerName,attributeName,value);
                  if(attributeTemp.size()>0)
                  {
                    this.attributeCollection.addAll(attributeTemp);  
                  }

                  
                  final ViewAttributesFrame attributesFrame = new ViewAttributesFrame(context,this.attributeCollection,layerName);

                  // REVISAR: Cast to WorkbenchFrame for the MDI model
                  ((WorkbenchGuiComponent)context.getWorkbenchGuiComponent()).addInternalFrame(attributesFrame);
                  
                  
                  //attributeTemp = null;
          }
        }
        
      }else
      {

          this.attributeCollection = GeopistaFunctionUtils.searchByAttribute(context.getLayerManager(),layerName,attributeName,value);
          final ViewAttributesFrame frame = new ViewAttributesFrame(context,this.attributeCollection,layerName);

          // REVISAR: Cast to WorkbenchFrame for the MDI model
          ((WorkbenchGuiComponent)context.getWorkbenchGuiComponent()).addInternalFrame( frame);
      }

      
  
      JDialog dialogPadre = (JDialog) SwingUtilities.getAncestorOfClass(JDialog.class,this);
      dialogPadre.setVisible(false); 

  }

  private class ViewAttributesFrame extends JInternalFrame
        implements LayerManagerProxy, SelectionManagerProxy,
            LayerNamePanelProxy, TaskFrameProxy, LayerViewPanelProxy {
	/**
	 * Logger for this class
	 */
	private final Log	logger	= LogFactory.getLog(ViewAttributesFrame.class);

        private LayerManager layerManager;
        private GeopistaOneLayerAttributeTab attributeTab;

        public ViewAttributesFrame(PlugInContext context, Collection attributeCollection,String layerName) {
            this.layerManager = context.getLayerManager();
            addInternalFrameListener(new InternalFrameAdapter() {
                    public void internalFrameClosed(InternalFrameEvent e) {
                        //Assume that there are no other views on the model [Jon Aquino]
                        attributeTab.getModel().dispose();
                    }
                });
            setResizable(true);
            setClosable(true);
            setMaximizable(true);
            setIconifiable(true);
            getContentPane().setLayout(new BorderLayout());
           

            attributeTab = new GeopistaOneLayerAttributeTab(context.getWorkbenchContext(),
                    (TaskComponent) context.getActiveInternalFrame(), this).setLayer(attributeCollection,context.getLayerManager().getLayer(layerName));

            context.getActiveInternalFrame().addInternalFrameListener(new InternalFrameAdapter()
            		{
            	public void internalFrameClosed(InternalFrameEvent e)
            	{
            		ViewAttributesFrame.this.dispose();
            	}
            	});
            addInternalFrameListener(new InternalFrameAdapter() {
                    public void internalFrameOpened(InternalFrameEvent e) {
                        attributeTab.getToolBar().updateEnabledState();
                    }
                });
            getContentPane().add(attributeTab, BorderLayout.CENTER);
            setSize(500, 300);
            
            updateTitle(attributeTab.getLayer());
            context.getLayerManager().addLayerListener(new LayerListener() {
                    public void layerChanged(LayerEvent e) {
                        if (attributeTab.getLayer() != null) {
                            updateTitle(attributeTab.getLayer());
                        }
                    }

                    public void categoryChanged(CategoryEvent e) {
                    }

                    public void featuresChanged(FeatureEvent e) {
                    }
                });
            Assert.isTrue(!(this instanceof CloneableInternalFrame),
                "There can be no other views on the InfoModel");
        }

        // sobrecargar usando un arraylist de layers

        public ViewAttributesFrame(PlugInContext context, Collection attributeCollection,ArrayList layerNames) {
            this.layerManager = context.getLayerManager();
            addInternalFrameListener(new InternalFrameAdapter() {
                    public void internalFrameClosed(InternalFrameEvent e) {
                        //Assume that there are no other views on the model [Jon Aquino]
                        attributeTab.getModel().dispose();
                    }
                });
            setResizable(true);
            setClosable(true);
            setMaximizable(true);
            setIconifiable(true);
            getContentPane().setLayout(new BorderLayout());
            //attributeTab = new OneLayerAttributeTab(context.getWorkbenchContext(),
            //        (TaskComponent) context.getActiveInternalFrame(), this).setLayer(context.getSelectedLayer(
            //            0));

            Iterator layerNamesIter  = layerNames.iterator();
            
            String layerName = "";
            while(layerNamesIter.hasNext())
            {
                layerName  = ((Layer)layerNamesIter.next()).getName();
                //System.out.println("layer : "+ layerName);
                attributeTab = new GeopistaOneLayerAttributeTab(context.getWorkbenchContext(),
                        (TaskComponent) context.getActiveInternalFrame(), this).setLayer(attributeCollection,context.getLayerManager().getLayer(layerName));

            
                addInternalFrameListener(new InternalFrameAdapter() {
                        public void internalFrameOpened(InternalFrameEvent e) {
                            attributeTab.getToolBar().updateEnabledState();
                        }
                    });
            }
            getContentPane().add(attributeTab, BorderLayout.CENTER);
            setSize(500, 300);
            
            updateTitle(attributeTab.getLayer());
            context.getLayerManager().addLayerListener(new LayerListener() {
                    public void layerChanged(LayerEvent e) {
                        if (attributeTab.getLayer() != null) {
                            updateTitle(attributeTab.getLayer());
                        }
                    }

                    public void categoryChanged(CategoryEvent e) {
                    }

                    public void featuresChanged(FeatureEvent e) {
                    }
                });
            Assert.isTrue(!(this instanceof CloneableInternalFrame),
                "There can be no other views on the InfoModel");
        }


        public LayerViewPanel getLayerViewPanel() {
            return (LayerViewPanel)getTaskComponent().getLayerViewPanel();
        }

        public LayerManager getLayerManager() {
            return layerManager;
        }

        private void updateTitle(Layer layer) {
            setTitle((layer.isEditable() ? "Edit" : "View") + " Attributes: " +
                layer.getName());
        }

        public TaskComponent getTaskComponent() {
            return attributeTab.getTaskFrame();
        }

        public SelectionManager getSelectionManager() {
            return attributeTab.getPanel().getSelectionManager();
        }

        public LayerNamePanel getLayerNamePanel() {
            return attributeTab;
        }
     

    }

    
	/**
	 * This method initializes literalSearchPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getLiteralSearchPanel() {
		if (literalSearchPanel == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			literalSearchPanel = new JPanel();
			java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			literalSearchPanel.setLayout(new GridBagLayout());
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 2;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.insets = new java.awt.Insets(0,0,0,5);
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.insets = new java.awt.Insets(0,0,0,5);
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 2;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.insets = new java.awt.Insets(0,0,0,5);
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.gridy = 3;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 4;
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			literalSearchPanel.setName("literalSearch");
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 5;
			gridBagConstraints11.gridwidth = 2;
			literalSearchPanel.add(lblCapa, gridBagConstraints1);
			literalSearchPanel.add(lblCampo, gridBagConstraints2);
			literalSearchPanel.add(lblTexto, gridBagConstraints3);
			literalSearchPanel.add(cmbCapa, gridBagConstraints4);
			literalSearchPanel.add(cmbCampo, gridBagConstraints5);
			literalSearchPanel.add(txtTexto, gridBagConstraints6);
			literalSearchPanel.add(chkPalabras, gridBagConstraints8);
			literalSearchPanel.add(chkShift, gridBagConstraints9);
			literalSearchPanel.add(getJPanel(), gridBagConstraints11);
		}
		return literalSearchPanel;
	}
	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */    
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab(aplicacion.getI18nString("literalSearch"), null, getLiteralSearchPanel(), null);
			jTabbedPane.addTab(aplicacion.getI18nString("FormulaSearch"), null, getFormulaSearchPanel(), null);
			
		}
		return jTabbedPane;
	}
	/**
	 * This method initializes formulaSearchPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getFormulaSearchPanel() {
		if (formulaSearchPanel == null) {
			formulaSearchPanel = new JPanel();
			formulaSearchPanel.setLayout(new BorderLayout());
			formulaSearchPanel.add(getSelectLayerFieldPanel(), java.awt.BorderLayout.CENTER);
			formulaSearchPanel.add(getFeatureExpressionPanel(), java.awt.BorderLayout.SOUTH);
		}
		return formulaSearchPanel;
	}
	/**
	 * This method initializes selectLayerFieldPanel	
	 * 	
	 * @return com.geopista.ui.dialogs.SelectLayerFieldPanel	
	 */    
	private SelectLayerFieldPanel getSelectLayerFieldPanel() {
		if (selectLayerFieldPanel == null) {
			selectLayerFieldPanel = new SelectLayerFieldPanel(context);
			selectLayerFieldPanel.addPropertyChangeListener(new PropertyChangeListener()
					{

						public void propertyChange(PropertyChangeEvent evt)
						{
							if (evt.getPropertyName().equals("LAYERSELECTED"))
							{
								updateFeatureExpr();
							}
							
						}
				
					});
			selectLayerFieldPanel.addActionListener(new ActionListener()
					{

						public void actionPerformed(ActionEvent e)
						{
						featureExpressionPanel.pasteText(selectLayerFieldPanel.getSelectedFieldName().replaceAll(" ","_"));
						}
				
					});
		}
		return selectLayerFieldPanel;
	}
	/**
	 * 
	 */
	protected void updateFeatureExpr()
	{
		Layer lay=selectLayerFieldPanel.getSelectedLayer();
		if (lay==null) return; //no se ha seleccionado una capa
		List features=lay.getFeatureCollectionWrapper().getFeatures();
		if (features.size()==0)return;
		Feature ftr=(Feature) features.get(0);
		if (ftr!=null)
		{
			featureExpressionPanel.getExpParser().setFeature(ftr);
		}
		else
		featureExpressionPanel.getExpParser().setSchema(lay.getFeatureCollectionWrapper().getFeatureSchema());
		
	}
	/**
	 * This method initializes featureExpressionPanel	
	 * 	
	 * @return com.geopista.ui.FeatureExpressionPanel	
	 */    
	private FeatureExpressionPanel getFeatureExpressionPanel() {
		if (featureExpressionPanel == null) {
			java.awt.GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			featureExpressionPanel = new FeatureExpressionPanel();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.gridy = 2;
			gridBagConstraints21.gridwidth = 3;
			featureExpressionPanel.add(getJPanel2(), gridBagConstraints21);
		}
		return featureExpressionPanel;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.add(btnBuscar, null);
			jPanel.add(btnCerrar, null);
		}
		return jPanel;
	}
	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.add(getJButton(), null);
			jPanel2.add(getJButton1(), null);
		}
		return jPanel2;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Buscar");
			jButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					btnBuscarExpresion(e);
					}
			});
		}
		return jButton;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("Cerrar");
			jButton1.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					cancelarbtn_actionPerformed(e);
					}
			});
		}
		return jButton1;
	}
          }  //  @jve:decl-index=0:visual-constraint="10,10"