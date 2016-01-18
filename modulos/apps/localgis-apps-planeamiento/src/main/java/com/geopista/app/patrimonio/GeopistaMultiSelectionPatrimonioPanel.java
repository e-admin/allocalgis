/**
 * GeopistaMultiSelectionPatrimonioPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.patrimonio;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.geopista.app.AppContext;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.GeopistaOneLayerAttributeTab;
import com.geopista.util.ApplicationContext;
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

/**
 * GeopistaMultiSelectionPatrimonioPanel
 * Clase para realizar la selección múltiple de features en los mapas de Patrimonio
 */
public class GeopistaMultiSelectionPatrimonioPanel extends JPanel
{
  ApplicationContext appContext=AppContext.getApplicationContext();
  private JLabel lblTituloMultiSelectionPatrimonio = new JLabel();
  private JLabel lblDireccion = new JLabel();
  private JTextField txtDireccion = new JTextField();
  private JTextField txtLinderoN = new JTextField();
  private JLabel lblLinderoN = new JLabel();
  private JLabel lblLinderoS = new JLabel();
  private JTextField txtLinderoS = new JTextField();
  private JTextField txtLinderoE = new JTextField();
  private JLabel lblLinderoE = new JLabel();
  private JLabel lblLinderoO = new JLabel();
  private JTextField txtLinderoO = new JTextField();
  private JTextField txtTipoInmueble = new JTextField();
  private JLabel lblTipo = new JLabel();
  private JLabel lblRefPar = new JLabel();
  private JTextField txtRefPar = new JTextField();
  private JButton btnAceptar = new JButton();
  private JButton btnCancelar = new JButton();

  private String temp = appContext.getI18nString("cmbLogic");
  private String[] cmbLogicArray = temp.split("\\,");
  private PlugInContext context;
  private Collection attributeCollection=new ArrayList();
  private JLabel lblCondicionTodas = new JLabel();
  private ButtonGroup grupoCondicion = new ButtonGroup();
  private JRadioButton radCondicion1 = new JRadioButton();
  private JRadioButton radCondicion2 = new JRadioButton();
  
  
  private JLabel lblCondicionCualquiera = new JLabel();
  
  
  public GeopistaMultiSelectionPatrimonioPanel(PlugInContext context)
  {
    grupoCondicion.add(radCondicion1);
    grupoCondicion.add(radCondicion2);
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
    this.setPreferredSize(new Dimension(325, 200));
    this.setLayout(null);
    lblTituloMultiSelectionPatrimonio.setBounds(new Rectangle(15, 10, 370, 20));
    lblTituloMultiSelectionPatrimonio.setText(appContext.getI18nString("lblTituloMultiSelectionPatrimonio"));
    lblDireccion.setBounds(new Rectangle(55, 40, 110, 20));
    lblDireccion.setText(appContext.getI18nString("lblDireccion"));
    txtDireccion.setBounds(new Rectangle(170, 40, 215, 20));
    txtLinderoN.setBounds(new Rectangle(170, 65, 215, 20));
    lblLinderoN.setBounds(new Rectangle(55, 65, 110, 20));
    lblLinderoN.setText(appContext.getI18nString("lblLinderoN"));
    lblLinderoS.setBounds(new Rectangle(55, 90, 110, 20));
    lblLinderoS.setText(appContext.getI18nString("lblLinderoS"));
    txtLinderoS.setBounds(new Rectangle(170, 90, 215, 20));
    txtLinderoE.setBounds(new Rectangle(170, 115, 215, 20));
    lblLinderoE.setBounds(new Rectangle(55, 115, 110, 20));
    lblLinderoE.setText(appContext.getI18nString("lblLinderoE"));
    lblLinderoO.setBounds(new Rectangle(55, 140, 110, 20));
    lblLinderoO.setText(appContext.getI18nString("lblLinderoO"));
    txtLinderoO.setBounds(new Rectangle(170, 140, 215, 20));
    txtTipoInmueble.setBounds(new Rectangle(170, 165, 215, 20));
    lblTipo.setBounds(new Rectangle(55, 165, 110, 20));
    lblTipo.setText(appContext.getI18nString("lblTipoInmueble"));
    lblRefPar.setBounds(new Rectangle(55, 190, 110, 20));
    lblRefPar.setText(appContext.getI18nString("lblRefPar"));
    txtRefPar.setBounds(new Rectangle(170, 190, 215, 20));
    btnAceptar.setBounds(new Rectangle(200, 265, 90, 25));
    btnAceptar.setText(appContext.getI18nString("btnAceptar"));
    btnAceptar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnAceptar_actionPerformed(e);
        }
      });
    btnCancelar.setBounds(new Rectangle(295, 265, 90, 25));
    btnCancelar.setText(appContext.getI18nString("btnCancelar"));
    btnCancelar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnCancelar_actionPerformed(e);
        }
      });
    lblCondicionTodas.setText(appContext.getI18nString("lblCondicionTodas"));
    lblCondicionTodas.setBounds(new Rectangle(55, 215, 330, 20));
    
    radCondicion1.setBounds(new Rectangle(30, 220, 15, 15));
    radCondicion2.setBounds(new Rectangle(30, 240, 20, 20));
    radCondicion2.setSelected(true);
    lblCondicionCualquiera.setText(appContext.getI18nString("lblCondicionCualquiera"));
    lblCondicionCualquiera.setBounds(new Rectangle(55, 240, 330, 20));
    this.add(lblCondicionCualquiera, null);
    this.add(radCondicion2, null);
    this.add(radCondicion1, null);
    this.add(lblCondicionTodas, null);
    this.add(btnCancelar, null);
    this.add(btnAceptar, null);
    this.add(txtRefPar, null);
    this.add(lblRefPar, null);
    this.add(lblTipo, null);
    this.add(txtTipoInmueble, null);
    this.add(txtLinderoO, null);
    this.add(lblLinderoO, null);
    this.add(lblLinderoE, null);
    this.add(txtLinderoE, null);
    this.add(txtLinderoS, null);
    this.add(lblLinderoS, null);
    this.add(lblLinderoN, null);
    this.add(txtLinderoN, null);
    this.add(txtDireccion, null);
    this.add(lblDireccion, null);
    this.add(lblTituloMultiSelectionPatrimonio, null);
  }
  
  private void btnCancelar_actionPerformed(ActionEvent e)
  {
    JDialog dialogPadre = (JDialog) SwingUtilities.getAncestorOfClass(JDialog.class,this);
    dialogPadre.setVisible(false);
  }
  /**
   * btnAceptar_actionPerformed(ActionEvent e)
   * Método ejecutado al pulsar el botón de aceptar. Realiza una búsqueda de las features
   * que cumplen los criterios del formulario bien todos o cualquiera de ellos.
   */
  private void btnAceptar_actionPerformed(ActionEvent e)
  {
      
      String layerName=appContext.getI18nString("lyrInmuebles");
     
      
      //NOMBRE DE LA VIA
      String attributeName1 = appContext.getI18nString("mspAttribute1");
      String value1 = (String)this.txtDireccion.getText();
      String attributeName2 = appContext.getI18nString("mspAttribute2");
      String value2 = (String)this.txtLinderoN.getText();
      String attributeName3 = appContext.getI18nString("mspAttribute3");
      String value3 = (String)this.txtLinderoS.getText();
      String attributeName4 = appContext.getI18nString("mspAttribute4");
      String value4 = (String)this.txtLinderoE.getText();
      String attributeName5 = appContext.getI18nString("mspAttribute5");
      String value5 = (String)this.txtLinderoO.getText();
      String attributeName6 = appContext.getI18nString("mspAttribute6");
      String value6 = (String)this.txtTipoInmueble.getText();
      String attributeName7 = appContext.getI18nString("mspAttribute7");
      String value7 = (String)this.txtRefPar.getText();
      
      ArrayList attributeNames = new ArrayList();
      ArrayList values = new ArrayList();

      if(!value1.equals(""))
      {
        attributeNames.add(attributeName1);
        values.add(value1);
      }
      if(!value2.equals(""))
      {
        attributeNames.add(attributeName2);
        values.add(value2);
      }
      if(!value3.equals(""))
      {
        attributeNames.add(attributeName3);
        values.add(value3);
      }
      if(!value4.equals(""))
      {
        attributeNames.add(attributeName4);
        values.add(value4);
      }
      if(!value5.equals(""))
      {
        attributeNames.add(attributeName5);
        values.add(value5);
      }
      if(!value6.equals(""))
      {
        attributeNames.add(attributeName6);
        values.add(value6);
      }
      if(!value7.equals(""))
      {
        attributeNames.add(attributeName7);
        values.add(value7);
      }
        
      

      String logicOperator =""; 
      if(radCondicion1.isSelected())
      {
        logicOperator="AND";
      }else
      {
        logicOperator="OR";
      }
          
      
      this.attributeCollection = GeopistaUtil.searchByAttributes(context.getLayerManager(),layerName,attributeNames,values,logicOperator);
      if (attributeCollection.size()==0)
      	{
      	JOptionPane.showMessageDialog(this,appContext.getI18nString("noResultsShowMessageDialog"));
      	return;
      	}
     
      final ViewAttributesFrame frame = new ViewAttributesFrame(context,this.attributeCollection,layerName);

      // REVISAR: Cast to WorkbenchFrame for the MDI model
      ((WorkbenchGuiComponent)context.getWorkbenchGuiComponent()).addInternalFrame( frame);

      btnCancelar_actionPerformed(null); // cierra el diálogo.
  }

  /**
   * ViewAttributesFrame
   * Frame asociado a la búsqueda, que muestra los resultados de la misma
   */
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
}