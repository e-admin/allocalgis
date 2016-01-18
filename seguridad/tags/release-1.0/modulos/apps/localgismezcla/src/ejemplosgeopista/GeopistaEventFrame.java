package ejemplosgeopista;
import com.geopista.editor.GeopistaEditor;


import com.vividsolutions.jump.workbench.model.Layer;

import java.awt.BorderLayout;
import java.awt.*;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.*;
import java.util.*;
import com.vividsolutions.jump.workbench.ui.zoom.*;
import com.vividsolutions.jump.workbench.ui.style.*;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.*;
import com.geopista.ui.plugin.*;
import com.geopista.model.GeopistaListener;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;

import com.vividsolutions.jump.workbench.model.*;
import com.vividsolutions.jump.workbench.ui.renderer.style.*;
import java.awt.Color;
import com.vividsolutions.jump.feature.*;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.BorderFactory;


public class GeopistaEventFrame extends JFrame
{

 
  
 
 
  

  
  private JPanel eventPanel = new JPanel();

  private JLabel a1 = new JLabel();
  private JLabel a2 = new JLabel();
  private JLabel a3 = new JLabel();
  private JLabel a4 = new JLabel();
  private JLabel a5 = new JLabel();
  private JLabel e1 = new JLabel();
  private JLabel e2 = new JLabel();
  private JLabel e3 = new JLabel();
  private JLabel e4 = new JLabel();
  private JLabel e5 = new JLabel();

  public GeopistaEventFrame() 
  {
      
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }

  public void setAttribute(FeatureEvent featureEvent, Layer layer)
  {
    // not implemented
  }
  public void setAttribute(AbstractSelection abstractSelection, Layer layer)
  {
         
    Collection allFeaturesList = abstractSelection.getFeaturesWithSelectedItems(layer);
    layer.getName();
    Iterator allFeaturesListIter = allFeaturesList.iterator();

     
    while(allFeaturesListIter.hasNext())
      {
        Feature localFeature = (Feature)allFeaturesListIter.next();

        a1.setText(localFeature.getString("PARCELA").trim());
        a2.setText(localFeature.getString("HOJA").trim());
        a3.setText(localFeature.getString("COORX").trim());
        a4.setText(localFeature.getString("COORY").trim());
        a5.setText(localFeature.getString("AREA").trim());
        
        
       
      }

 
    
    
  }

  private void jbInit() throws Exception
  {
    
      
    
    this.setSize(new Dimension(250, 220));
    eventPanel.setLayout(new GridLayout(5,2));
    eventPanel.setMinimumSize(new Dimension(250, 220));
    a1.setBackground(Color.white);
    a1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    a1.setOpaque(true);
    a2.setBackground(Color.white);
    a2.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    a2.setOpaque(true);
    a3.setBackground(Color.white);
    a3.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    a3.setOpaque(true);
    a4.setBackground(Color.white);
    a4.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    a4.setOpaque(true);
    a5.setBackground(Color.white);
    a5.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    a5.setOpaque(true);

    e1.setText("PARCELA : ");
    e2.setText("HOJA    : ");
    e3.setText("X       : ");
    e4.setText("Y       : ");
    e5.setText("AREA    : ");
    
    eventPanel.add(e5);
    eventPanel.add(a5);


    eventPanel.add(e4);
    eventPanel.add(a4);
    eventPanel.add(e3);
    eventPanel.add(a3);    
    eventPanel.add(e2);
    eventPanel.add(a2);
    eventPanel.add(e1);
    eventPanel.add(a1);    
   

    

    this.getContentPane().add(eventPanel, BorderLayout.CENTER);
   
    this.setLocation(15,15);
    
    this.setVisible(true);
  }
  
}