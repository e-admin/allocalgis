/**
 * GeopistaNumerosPoliciaPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.ui.GeopistaOneLayerAttributeTab;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jts.util.Assert;
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


public class GeopistaNumerosPoliciaPanel extends JPanel
{
  private static final Log    logger  = LogFactory.getLog(GeopistaUtil.class);
    
  private JPanel pnlVentana = new JPanel();
  //private JLabel lblTipovia = new JLabel();
  private JLabel lblNombreVia = new JLabel();
  private JTextField txtNombreVia = new JTextField();
  private JTextField txtNumero = new JTextField();
  private JButton btnCancelar = new JButton();
  private JButton btnAceptar = new JButton();
  private JLabel lblNumero = new JLabel();
  private PlugInContext context;
  private Collection attributeCollection=new ArrayList();
  //private JComboBox cmbVia = new JComboBox();
  private JLabel lblTitulo = new JLabel();
  private JLabel lblLiteral = new JLabel();
  private JCheckBox chkLiteraltext = new JCheckBox();
  //private ComboBoxEstructuras cmbVia= null;
  private String PREFERENCES_LOCALE_KEY="geopista.user.language";
  
 
  ApplicationContext appContext=AppContext.getApplicationContext();
  public GeopistaNumerosPoliciaPanel()
  {
  	
  }
  public GeopistaNumerosPoliciaPanel(PlugInContext context)
  {
    this.context = context;
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
  	
    this.setLayout(null);
    this.setSize(new Dimension(461, 150));
    this.setSize(375, 150);
//    lblTipovia.setText(appContext.getI18nString("lblTipoVia"));
//    lblTipovia.setBounds(new Rectangle(15, 40, 130, 20));
    lblNombreVia.setText(appContext.getI18nString("lblNombreVia"));
    lblNombreVia.setBounds(new Rectangle(15, 65, 120, 20));
    txtNombreVia.setBounds(new Rectangle(80, 65, 220, 20));
    txtNumero.setBounds(new Rectangle(340, 65, 25, 20));
    txtNumero.setHorizontalAlignment(JTextField.LEFT);

//En este punto tengdrmos que cargar la clase Estructutras el dominio que corresponda    
//     cmbVia= new ComboBoxEstructuras(Estructuras.getListaTiposViaIni(),null,
//           UserPreferenceStore.getUserPreference(PREFERENCES_LOCALE_KEY,"es_ES",false),true);
   

   
    chkLiteraltext.setBounds(new Rectangle( 15,90 ,20,20));
    lblLiteral.setText(appContext.getI18nString("lblLiteral"));
    lblLiteral.setBounds(new Rectangle(40, 90, 80, 20));
    
    btnCancelar.setText(appContext.getI18nString("btnCancelar"));
    btnCancelar.setBounds(new Rectangle(275, 90, 90, 25));
    btnCancelar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnCancelar_actionPerformed(e);
        }
      });
    btnAceptar.setText(appContext.getI18nString("btnAceptar"));
    btnAceptar.setBounds(new Rectangle(175, 90, 90, 25));
    btnAceptar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnAceptar_actionPerformed(e);
        }
      });
    lblNumero.setText(appContext.getI18nString("lblNumero"));
    lblNumero.setBounds(new Rectangle(310, 65, 25, 20));
//    cmbVia.setBounds(new Rectangle(80, 40, 110, 20));
    lblTitulo.setText(appContext.getI18nString("lblTituloNumerosPolicia"));
    lblTitulo.setBounds(new Rectangle(15, 10, 315, 25));
    lblTitulo.setFont(new Font("Dialog", 1, 11));
    this.add(lblTitulo, null);
//    this.add(cmbVia, null);
    this.add(lblNumero, null);
    this.add(btnAceptar, null);
    this.add(btnCancelar, null);
    this.add(txtNumero, null);
    this.add(txtNombreVia, null);
    this.add(chkLiteraltext,null);
    this.add(lblLiteral,null);
    
//    this.setSize(250, 290);
    this.add(lblNombreVia, null);
//    this.add(lblTipovia, null);
  }

  private void btnCancelar_actionPerformed(ActionEvent e)
  {
    JDialog dialogPadre = (JDialog) SwingUtilities.getAncestorOfClass(JDialog.class,this);
    dialogPadre.setVisible(false);
  }

  private void btnAceptar_actionPerformed(ActionEvent e)
  {
      //String layerName = context.getSelectedLayer(0).getName();
      String snombreCatastro="nombrecatastro";
      String stipoVia="tipovianormalizadocatastro";
      String scodigoViaNumPolice="id_via";
      String scodigoVia="id";
      String srotuloNumPolice="rotulo";
      String snumPolice=null;
      String snumPoliceAttri=null;
      boolean tipoBusqueda=false;
      String valNumPolice=null;

      String layerName=appContext.getString("lyrVias");
      
      //NOMBRE DE LA VIA

      String value1 = (String)this.txtNombreVia.getText();
      //coge datos del combobox
     
//      String value2 = (String)this.cmbVia.getSelectedPatron();
      String value2=null;;
      String value3 = (String)this.txtNumero.getText();
      
      ArrayList attributeNames = new ArrayList();
      ArrayList values = new ArrayList();
      ArrayList attributeNamesNumPolice = new ArrayList();
      ArrayList valuesNumPolice = new ArrayList();
      
      if(value3.equals("")){
          value3=null;
      }
      
      
      
      if(chkLiteraltext.isSelected())
          tipoBusqueda=true;
          
      values.add(value1);
      //values.add(value2);
      
      //recogemos el schema para una capa en concreto para poder buscar los atributos con el nombnre de la columna
     GeopistaSchema schema = (GeopistaSchema) (context.getLayerManager().getLayer(layerName).getFeatureCollectionWrapper().getFeatureSchema());
     String snombreCatastroAttri = schema.getAttributeByColumn(snombreCatastro);
     String stipoViaAttri = schema.getAttributeByColumn(stipoVia);
     String scodigoViaAttri = schema.getAttributeByColumn(scodigoVia);
     
     attributeNames.add(snombreCatastroAttri);
      
       
      this.attributeCollection = GeopistaFunctionUtils.searchByAttributes(context.getLayerManager(),layerName,attributeNames,values,value2,stipoViaAttri,tipoBusqueda);
      if (attributeCollection.size()==0)
      {
      	JOptionPane.showMessageDialog(this,appContext.getI18nString("GeopistaNumPolicePanelNoLocation"));
      	return;
      }else{
          layerName=appContext.getString("lyrNumerosPolicia");
          Iterator attributeCollectionIter = attributeCollection.iterator();
          GeopistaSchema schemaNumpolice = (GeopistaSchema) (context.getLayerManager().getLayer(layerName).getFeatureCollectionWrapper().getFeatureSchema());
          String scodigoViaNumPoliceAttri= schemaNumpolice.getAttributeByColumn(scodigoViaNumPolice);
          String srotuloNumPoliceAttri= schemaNumpolice.getAttributeByColumn(srotuloNumPolice);
          while (attributeCollectionIter.hasNext())
          {
              GeopistaFeature fviasGeopistaFeature = (GeopistaFeature) attributeCollectionIter.next();
//                 valNumPolice = fviasGeopistaFeature.getString(scodigoViaAttri);
                 
              valuesNumPolice.add(fviasGeopistaFeature.getString(scodigoViaAttri));
              attributeNamesNumPolice.add(scodigoViaNumPoliceAttri);

          }
          if(value3 != null){
              snumPolice=value3;
              snumPoliceAttri=srotuloNumPoliceAttri;
//              valuesNumPolice.add(value3);
//              attributeNamesNumPolice.add(srotuloNumPoliceAttri);
          }
              
          this.attributeCollection.clear();
          this.attributeCollection = GeopistaFunctionUtils.searchByAttributes(context.getLayerManager(),layerName,attributeNamesNumPolice,valuesNumPolice,snumPolice,snumPoliceAttri,tipoBusqueda);
          if (attributeCollection.size()==0)
          {
            if(value3 == null){
                JOptionPane.showMessageDialog(this,appContext.getI18nString("GeopistaNumPolicePanelnumesInexis"));
                return;
            }else{
                JOptionPane.showMessageDialog(this,appContext.getI18nString("GeopistaNumPolicePanelnumInexis"));
                
            }
          }
          LayerManager layerManager = context.getLayerManager();
          Layer localLayer = layerManager.getLayer(layerName);
          //primero desseleccionamos los objetos ateriormente seleccionados
          context.getLayerViewPanel().getSelectionManager().getFeatureSelection().unselectItems();
          context.getLayerViewPanel().getSelectionManager().getFeatureSelection().selectItems(localLayer,attributeCollection);
          //el el siguiente bloque try...catch es donde realizamos el zoom a la zona seleccionada
          try{
              GeopistaEditor zoomAction=new GeopistaEditor();
              zoomAction.zoom(context.getLayerViewPanel().getSelectionManager().getSelectedItems() ,context.getLayerViewPanel());
              
          }catch (Exception NoninvertibleTransformException)
          {
              logger.error("[GeopistaNumerosPoliciaPanel]Error realizando zoom",NoninvertibleTransformException);
              return;
          }
          
      }
     
      final ViewAttributesFrame frame = new ViewAttributesFrame(context,this.attributeCollection,layerName);

      // REVISAR: Cast to WorkbenchFrame for the MDI model
      ((WorkbenchGuiComponent)context.getWorkbenchGuiComponent()).addInternalFrame( frame);

      btnCancelar_actionPerformed(null); // cierra el diálogo.
  }
  private class ViewAttributesFrame extends JInternalFrame
        implements LayerManagerProxy, SelectionManagerProxy,
            LayerNamePanelProxy, TaskFrameProxy, LayerViewPanelProxy {
        private LayerManager layerManager;
        private GeopistaOneLayerAttributeTab attributeTab;

        public ViewAttributesFrame(PlugInContext context, Collection attributeCollection, String layerName) {
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

            attributeTab = new GeopistaOneLayerAttributeTab(context.getWorkbenchContext(),
                    (TaskComponent) context.getActiveInternalFrame(), this).setLayer(attributeCollection,context.getLayerManager().getLayer(layerName));

            
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

  
public PlugInContext getContext()
{
	return context;
}
public void setContext(PlugInContext context)
{
	this.context = context;
}
}