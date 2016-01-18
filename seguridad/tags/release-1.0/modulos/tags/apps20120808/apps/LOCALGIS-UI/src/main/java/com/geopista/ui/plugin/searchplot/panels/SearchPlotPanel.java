package com.geopista.ui.plugin.searchplot.panels;
/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkbenchGuiComponent;
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


public class SearchPlotPanel extends JPanel
{
  private static final Log    logger  = LogFactory.getLog(GeopistaUtil.class);
  
  private JTextField txtName = new JTextField();
  private JTextField txtSurname1 = new JTextField();
  private JTextField txtSurname2 = new JTextField();
  
  private JLabel lblNif = new JLabel();
  private JLabel lblName = new JLabel();
  private JLabel lblSurname1 = new JLabel();
  private JLabel lblSurname2 = new JLabel();
  
  private JPanel pnlVentana = new JPanel();
  private JLabel lblCriterio = new JLabel();
  private JTextField txtValor = new JTextField();
  private JButton btnCancelar = new JButton();
  private JButton btnAceptar = new JButton();
  private PlugInContext context;
  private Collection attributeCollection=new ArrayList();
  private JLabel lblTitulo = new JLabel();
  private JCheckBox chkIsCompany = new JCheckBox();
  private JRadioButton rdbNif = new JRadioButton();
  private JRadioButton rdbName = new JRadioButton();
  private ButtonGroup bgOrden= new ButtonGroup();
  int align = FlowLayout.LEFT;  
  private JPanel jPanelRadio = new JPanel(new FlowLayout(align));
  
  public Connection con = null;
  
  ApplicationContext appContext=AppContext.getApplicationContext();
  public SearchPlotPanel()
  {
  	
  }
  public SearchPlotPanel(PlugInContext context)
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
    this.setSize(550, 180);
    
    //labels y textbox
    lblCriterio.setText(appContext.getI18nString("lblCriterioBusqueda"));
    lblCriterio.setBounds(new Rectangle(10, 35, 100, 20));
    
    lblTitulo.setText(appContext.getI18nString("lblTituloSearchPlot"));
    lblTitulo.setBounds(new Rectangle(10, 5, 315, 25));
    lblTitulo.setFont(new Font("Dialog", 1, 11));
    
    lblNif.setText(appContext.getI18nString("lblNif"));
    lblNif.setBounds(new Rectangle(10, 80, 60, 20));
    lblName.setText(appContext.getI18nString("lblName"));
    lblName.setBounds(new Rectangle(10, 80, 60, 20));
    lblSurname1.setText(appContext.getI18nString("lblSurname1"));
    lblSurname1.setBounds(new Rectangle(215, 80, 60, 20));
    lblSurname2.setText(appContext.getI18nString("lblSurname2"));
    lblSurname2.setBounds(new Rectangle(375, 80, 60, 20));
    
    chkIsCompany.setBounds(new Rectangle(10, 120, 100, 20));
    chkIsCompany.setText(appContext.getI18nString("lblEsEmpresa"));
    chkIsCompany.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e)
        {
            if(chkIsCompany.isSelected()){
                lblSurname1.setVisible(false);
                lblSurname2.setVisible(false);
                txtSurname2.setVisible(false);
                txtSurname1.setVisible(false);
                txtName.setBounds(new Rectangle(60, 80, 250, 20));
            }else{
                lblSurname1.setVisible(true);
                lblSurname2.setVisible(true);
                txtSurname2.setVisible(true);
                txtSurname1.setVisible(true);
                txtName.setBounds(new Rectangle(60, 80, 150, 20));
            }
        }
    });
    
    txtName.setBounds(new Rectangle(60, 80, 150, 20));
    txtName.setHorizontalAlignment(JTextField.LEFT);
    txtSurname1.setBounds(new Rectangle(270, 80, 100, 20));
    txtSurname1.setHorizontalAlignment(JTextField.LEFT);
    txtSurname2.setBounds(new Rectangle(435, 80, 100, 20));
    txtSurname2.setHorizontalAlignment(JTextField.LEFT);
    
    
    txtValor.setBounds(new Rectangle(60, 80, 150, 20));
    txtValor.setHorizontalAlignment(JTextField.LEFT);
    
    rdbNif.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e)
        {
            if(rdbNif.isSelected()){
                chkIsCompany.setSelected(false);
                txtName.setBounds(new Rectangle(60, 80, 150, 20));
                txtValor.setVisible(true);
                lblNif.setVisible(true);
                txtSurname2.setVisible(false);
                txtSurname1.setVisible(false);
                txtName.setVisible(false);
                lblName.setVisible(false);
                lblSurname1.setVisible(false);
                lblSurname2.setVisible(false);
                chkIsCompany.setVisible(false);
                txtValor.setText("");
                txtSurname2.setText("");
                txtSurname1.setText("");
                txtName.setText("");
            }
        }
    });
    
    rdbName.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e)
        {
            if(rdbName.isSelected()){
                chkIsCompany.setSelected(false);
                txtName.setBounds(new Rectangle(60, 80, 150, 20));
                txtSurname2.setVisible(true);
                txtSurname1.setVisible(true);
                txtName.setVisible(true);
                lblName.setVisible(true);
                lblSurname1.setVisible(true);
                lblSurname2.setVisible(true);
                chkIsCompany.setVisible(true);
                txtValor.setVisible(false);
                lblNif.setVisible(false);
                txtValor.setText("");
                txtSurname2.setText("");
                txtSurname1.setText("");
                txtName.setText("");
 
            }
        }
    });
    
    //radio Button
    jPanelRadio.setBounds(130, 30, 160, 30);
    rdbNif.setText(appContext.getI18nString("lblNif"));
    rdbNif.setActionCommand("nif");
    rdbName.setText(appContext.getI18nString("lblTitular"));
    rdbName.setActionCommand("titular");
    bgOrden.add(rdbNif);
    bgOrden.add(rdbName);
    
    ButtonModel model = rdbNif.getModel();
    bgOrden.setSelected(model, true);
    rdbNif.doClick();
    
    btnCancelar.setText(appContext.getI18nString("btnCancelar"));
    btnCancelar.setBounds(new Rectangle(435, 120, 90, 25));
    btnCancelar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnCancelar_actionPerformed(e);
        }
      });
    btnAceptar.setText(appContext.getI18nString("btnAceptar"));
    btnAceptar.setBounds(new Rectangle(335, 120, 90, 25));
    btnAceptar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnAceptar_actionPerformed(e);
        }
      });

    jPanelRadio.setVisible(true);
    jPanelRadio.add(rdbNif, null);
    jPanelRadio.add(rdbName, null);
    
    this.add(jPanelRadio, null);
    this.add(lblTitulo, null);
    this.add(lblNif, null);
    this.add(lblName, null);
    this.add(lblSurname1, null);
    this.add(lblSurname2, null);
    
    this.add(btnAceptar, null);
    this.add(btnCancelar, null);
    this.add(chkIsCompany, null);
    
    this.add(txtValor, null);
    this.add(txtName, null);
    this.add(txtSurname1, null);
    this.add(txtSurname2, null);
    //this.add(txtNif, null);
    
    this.add(lblCriterio, null);
    //this.add(lblNif, null);
  }

  private void btnCancelar_actionPerformed(ActionEvent e)
  {
    JDialog dialogPadre = (JDialog) SwingUtilities.getAncestorOfClass(JDialog.class,this);
    dialogPadre.setVisible(false);
  }

  private void btnAceptar_actionPerformed(ActionEvent e)
  {
      //String layerName = context.getSelectedLayer(0).getName();
      String srefCatastro="referencia_catastral";          
      String valuetext=null;
      String valueDni=null;
      String valueNif=null;
      boolean selectionNif=false;
      boolean selectionName=false;
      boolean interruptor=false;
      String[] letradni={"T","R","W","A","G","M","Y","F","P","D","X","B","N","J","Z","S","Q","V","H","L","C","K","E"};
      String layerName=appContext.getString("lyrParcelas");
      List refcatastrovalues = new LinkedList();
    try{
        selectionNif=this.rdbNif.isSelected();
        selectionName=this.rdbName.isSelected();
        String sql=null;
        String sql1=null;
          if (con == null)
          {
              con = getDBConnection();
          }
          //solo accederemos si tenemos marcada la busqueda por NIF
          if(selectionNif){
              valuetext=this.txtValor.getText();
              sql="parcetitunif";
              sql1="parcesujenif";
              char ini=valuetext.charAt(0);
              boolean isletra = Character.isLetter(ini);
              //comprobamos que sea in NIF o UN CIF si es de empresa le dejaremos tal y como le introduzca el usuario
              //miramos que el primer carascter sea o no numerico
          if(!isletra){
              //si el caracter es numerico significa que es un NIF
              // ahora comprobaremos si el ultimo caracter es una letra
              
                  //Comprobamos que el NIF tenga menos de 8 caracteres
                  if(!(valuetext.length()>8)){
                      
                      char fin=valuetext.charAt((valuetext.length()-1));
                      if(Character.isLetter(fin)){
                          //si el ultimo caracter era una letra y es menos de 9 caracteres rellenaremos con 0 por la izquierda
                          int numZero=9 - valuetext.length();
                          String zero="0";
                          for(int j=0;j<numZero;j++){
                              valuetext=zero+valuetext;
                          }
                      }else{
                          //si no es una letra entonces pondremos la letra segun el algoritmo siguiente
                          long numero=Long.valueOf(valuetext).longValue();
                          long mod=numero%23;
                          String letra=letradni[(int)mod];
                          // añadimos la letra a nuestro NIF
                          valuetext=valuetext+letra;
                          //si con la letra no tuviera todabia 9 caracteres rellenamos con ceros por la izquierda
                          if(!(valuetext.length()>8)){
                              int numZero=9 - valuetext.length();
                              String zero="0";
                              for(int j=0;j<numZero;j++){
                                 valuetext=zero+valuetext;
                              }
                          }                           
                      }
                  }
                  //en este punto tenemos que tener dos valores diferentes el el DNI(sin LETRA) y el NIF(con LETRA)  
                  valueNif=valuetext;
                  valueDni=valueNif.substring(0,(valueNif.length()-1));
              }
                  interruptor=true;
                  //solo accederemos si tenemos marcada la busqueda por nombre
              }else  if(selectionName){
                  sql="parcetituname";
                  sql1="parcesujename";
                  // solo se comprueba si no ha introducido datos en caso de que quede alguno en blanco si que funciona
                  //pero queda un poco chapuza ya que si se busca por nombre seria %%%nombre% el valor a introducir
                  if(this.txtSurname1.getText()!="" || this.txtSurname2.getText()!="" || this.txtName.getText()!=""){
                      valuetext="%"+this.txtSurname1.getText()+"%"+
                                  this.txtSurname2.getText()+"%"+
                                  this.txtName.getText()+"%";
                  }
                  if(this.txtSurname1.getText()=="" && this.txtSurname2.getText()=="" && this.txtName.getText()==""){
                      JOptionPane.showMessageDialog(this,appContext.getI18nString("searchPlotPaneltextempty"));
                      return;
                  }
              }
          
          
          if (valuetext.equals("")){
            JOptionPane.showMessageDialog(this,appContext.getI18nString("searchPlotPaneltextempty"));
            return;
          }
          int contador=0;
          for(int i=0;i<=1;i++){
              ResultSet r = null;
              PreparedStatement  ps = null;
              PreparedStatement  ps2 = null;
              int acumulador=0;
              contador++;
              if(i==0){
      
                  ps = con.prepareStatement(sql);
                  ps.setString(1,valuetext);
                  // Se ejecuta la consulta
                      r=ps.executeQuery();
                      while (r.next())
                      {
                          refcatastrovalues.add(r.getString("referencia_catastral"));
                          acumulador++;
                          
                      }
                      r.close();
                      ps.close();
                      
                  if(interruptor==true && contador<2 && acumulador==0){
                      i=-1;
                      valuetext=valueDni;
                  }else if(interruptor==true){
                      valuetext=valueNif;
                      contador=0;
                  }
              }
              else{
                  ps2 = con.prepareStatement(sql1);
                  ps2.setString(1,valuetext);
                  // Se ejecuta la consulta
                      r=ps2.executeQuery();
                      
                      while (r.next())
                      {
                          refcatastrovalues.add(r.getString("referencia_catastral"));
                          acumulador++;
                      }
                      r.close();
                      ps2.close();  
                  if(interruptor==true && contador<2 && acumulador==0){
                      i=0;
                      valuetext=valueDni;
                  }else if(interruptor==true){
                      break;
                  }
              }

          }

    }catch(Exception SQLException ){
        logger.error("[GeopistaNumerosPoliciaPanel]Error realizando zoom",SQLException);
        return;
    }
    
    if(refcatastrovalues.size()==0){
        if (interruptor==true){
            JOptionPane.showMessageDialog(this,appContext.getI18nString("SearchPlotNoLocationDni"));
            return;
        }else{
            JOptionPane.showMessageDialog(this,appContext.getI18nString("SearchPlotNoLocationName"));
            return;
        }       
    }
        
        
    //recogemos el echema para una capa en concreto para poder buscar los atributos con el nombnre de la columna
    GeopistaSchema schema = (GeopistaSchema) (context.getLayerManager().getLayer(layerName).getFeatureCollectionWrapper().getFeatureSchema());
    String srefCatastroAttri = schema.getAttributeByColumn(srefCatastro);
    this.attributeCollection = GeopistaFunctionUtils.searchByAttributes(context.getLayerManager(),layerName,refcatastrovalues,srefCatastroAttri);
  if (attributeCollection.size()==0)
  {
     JOptionPane.showMessageDialog(this,appContext.getI18nString("SearchPlotNoLocation"));
     return;
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
  /**
   * Establece la conexion con la base de datos
   * 
   * @return Connection, conexion
   */
  public static Connection getDBConnection() throws SQLException
  {
      ApplicationContext app = AppContext.getApplicationContext();
      Connection conn = app.getConnection();
      conn.setAutoCommit(true);
      return conn;
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