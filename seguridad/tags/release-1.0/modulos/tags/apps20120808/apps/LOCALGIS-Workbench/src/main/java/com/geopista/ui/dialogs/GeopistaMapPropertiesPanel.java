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

package com.geopista.ui.dialogs;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaMap;
import com.geopista.security.GeopistaPermission;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;


public class GeopistaMapPropertiesPanel extends JPanel
{


  private JLabel lblMapName = new JLabel();
  private JTextField txtMapName = new JTextField();
  private JLabel lblMapDescription = new JLabel();
  private JTextField txtMapDescription = new JTextField();
  private JLabel lblMapUnits = new JLabel();
  private JTextField txtMapUnits = new JTextField();
  private JLabel lblMapScale = new JLabel();
  private JTextField txtMapScale = new JTextField();
  private JLabel lblMapProjection = new JLabel();
  private boolean okPressed = false;

  private JLabel lblMapEntidad= new JLabel();
  private JComboBox jcomboBoxMapEntidad;

  private ApplicationContext appContext=AppContext.getApplicationContext();

 
  
 
  private JComboBox cmbMapProjection = null;
  private JButton btnAceptar = new JButton();
  private JButton btnCancelar = new JButton();
  private PlugInContext context = null;


  public GeopistaMapPropertiesPanel(PlugInContext context)
  {
  
  Object[] projections = CoordinateSystemRegistry
  .instance(context.getWorkbenchContext()
          .getBlackboard()).getCoordinateSystems().toArray();
          
    cmbMapProjection=new JComboBox(projections);
    
    this.context=context;
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

    String dirBase = appContext.getString(AppContext.PREFERENCES_DATA_PATH_KEY);
    String mapName = context.getTask().getName();
    
    this.setLayout(null);
    //this.setSize(new Dimension(400, 226));
    this.setSize(new Dimension(400, 246));
    lblMapName.setBounds(new Rectangle(15, 15, 130, 20));
    lblMapName.setText(appContext.getI18nString("lblMapName"));
    txtMapName.setBounds(new Rectangle(150, 15, 230, 20));
    txtMapName.setText(((GeopistaMap) context.getTask()).getName());
    lblMapDescription.setBounds(new Rectangle(15, 45, 130, 20));
    lblMapDescription.setText(appContext.getI18nString("lblMapDescription"));
    txtMapDescription.setBounds(new Rectangle(150, 45, 230, 20));
    txtMapDescription.setText(((GeopistaMap) context.getTask()).getDescription());

    lblMapUnits.setBounds(new Rectangle(15, 75, 130, 20));
    lblMapUnits.setText(appContext.getI18nString("lblMapUnits"));
    txtMapUnits.setBounds(new Rectangle(150, 75, 230, 20));
    txtMapUnits.setText(((GeopistaMap) context.getTask()).getMapUnits());

    lblMapScale.setBounds(new Rectangle(15, 105, 125, 20));
    lblMapScale.setText(appContext.getI18nString("lblMapScale"));
    txtMapScale.setBounds(new Rectangle(150, 105, 230, 20));
    txtMapScale.setText(((GeopistaMap) context.getTask()).getMapScale());
    lblMapProjection.setBounds(new Rectangle(15, 135, 125, 20));
    lblMapProjection.setText(appContext.getI18nString("lblMapProjection"));
    cmbMapProjection.setBounds(new Rectangle(150, 135, 230, 20));

    lblMapEntidad.setBounds(new Rectangle(15, 165, 130, 20));
    lblMapEntidad.setText("Entidad");

    
    GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.Map.CrearMapaGlobal");
    if (appContext.checkPermission(geopistaPerm, "General")){
        String []valores={String.valueOf(AppContext.getIdEntidad()),"0"};
        jcomboBoxMapEntidad=new JComboBox(valores);
    }
    else{
    	  String []valores={String.valueOf(AppContext.getIdEntidad())};
          jcomboBoxMapEntidad=new JComboBox(valores);	
          jcomboBoxMapEntidad.setEnabled(false);
    }
    
    jcomboBoxMapEntidad.setBounds(new Rectangle(150, 165, 230, 20));
    
  

  
    // Selecciona el actual
   
    cmbMapProjection.setSelectedItem(context.getTask().getLayerManager().getCoordinateSystem());
    cmbMapProjection.setEnabled(false);
    btnAceptar.setText(appContext.getI18nString("btnAceptar"));
    btnAceptar.setBounds(new Rectangle(150, 215, 100, 25));
    btnAceptar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnAceptar_actionPerformed(e);
        }
      });
    btnCancelar.setText(appContext.getI18nString("btnCancelar"));
    btnCancelar.setBounds(new Rectangle(280, 215, 100, 25));
    btnCancelar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnCancelar_actionPerformed(e);
        }
      });
    this.add(btnCancelar, null);
    this.add(btnAceptar, null);
    this.add(cmbMapProjection, null);
    this.add(lblMapProjection, null);
    this.add(txtMapScale, null);
    this.add(lblMapScale, null);
    this.add(txtMapUnits, null);
    this.add(lblMapUnits, null);
    this.add(txtMapDescription, null);
    this.add(lblMapDescription, null);
    this.add(txtMapName, null);
    this.add(lblMapName, null);
    this.add(jcomboBoxMapEntidad, null);
    this.add(lblMapEntidad, null);
    
  }

  private void btnAceptar_actionPerformed(ActionEvent e)
  {
    ((GeopistaMap) context.getTask()).setName(txtMapName.getText());
    ((GeopistaMap) context.getTask()).setDescription(txtMapDescription.getText());
    ((GeopistaMap) context.getTask()).setMapUnits(txtMapUnits.getText());
    ((GeopistaMap) context.getTask()).setMapScale(txtMapScale.getText());
    ((GeopistaMap) context.getTask()).setMapScale(txtMapScale.getText());
    ((GeopistaMap) context.getTask()).setIdEntidadSeleccionada(jcomboBoxMapEntidad.getSelectedItem().toString());
    //REVISAR: Reproyectar el mapa o evitar que se pueda cambiar aquí.
    // Creo que es mejor evitar los cambios en este diálogo.
    //((GeopistaMap) context.getTask()).setMapCoordinateSystem((CoordinateSystem)cmbMapProjection.getSelectedItem());

    okPressed=true;
    this.setVisible(false);
    JDialog Dialog = (JDialog) SwingUtilities.getWindowAncestor(this );
    Dialog.setVisible(false);
  }

  private void btnCancelar_actionPerformed(ActionEvent e)
  {
    this.setVisible(false);
    JDialog Dialog = (JDialog) SwingUtilities.getWindowAncestor(this );
    Dialog.setVisible(false);
  }
  
  public boolean wasOKPressed() {
      return okPressed;
  }

  public void setOKPressed(boolean okPressed) {
      this.okPressed = okPressed;
  }

}