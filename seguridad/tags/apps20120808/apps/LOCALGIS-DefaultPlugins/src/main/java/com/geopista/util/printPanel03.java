package com.geopista.util;
/*
 * * The GEOPISTA project is a set of tools and applications to manage
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 * 
 * Created on 30-sep-2005 by hgarcia
 *
 * 
 */
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Map;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.GeopistaPrintPlugIn;
import com.geopista.ui.plugin.SeriePrintPlugIn;
import com.geopista.ui.plugin.print.PrintLayoutFrame;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

import java.awt.BorderLayout;
/**

*/

public class printPanel03 extends javax.swing.JPanel implements WizardPanel{
	
  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
  private Blackboard blackboardInformes  = aplicacion.getBlackboard();
  private WizardContext wizardContext;
	
  private JLabel mensaje = new JLabel();
  private String localId = null;
  private PlugInContext	context;
  private JPanel panel= new JPanel();

  
	public printPanel03(String id, String nextId, PlugInContext context2) {
	    this.context=context2;
	    this.nextID = nextId;
	    this.localId = id;
    
		setName((I18N.get("printPlugin.Plantilla"))); //$NON-NLS-1$
		initGUI();
	}

	/**
	* Initializes the GUI.
	* Auto-generated code - any changes you make will disappear.
	*/
	public void initGUI(){
		try {
			preInitGUI();
		
			
			panel.removeAll();
		     
			//panel.setBounds (300, 50, 240, 175);
			 
			mensaje.setText(I18N.get("printPlugin.ElegirPlantilla"));	
			mensaje.setBounds(100, 100, 100, 100);
			
			panel.add(mensaje, null);
			this.add(panel, null);
				
			postInitGUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** Add your pre-init code in here 	*/
	public void preInitGUI(){
	}

	/** Add your post-init code in here 	*/
	public void postInitGUI(){
	}

	
 	/** Auto-generated main method */
	public static void main(String[] args){
		showGUI();
	}

	/**
	* This static method creates a new instance of this class and shows
	* it inside a new JFrame, (unless it is already a JFrame).
	*
	* It is a convenience method for showing the GUI, but it can be
	* copied and used as a basis for your own code.	*
	* It is auto-generated code - the body of this method will be
	* re-generated after any changes are made to the GUI.
	* However, if you delete this method it will not be re-created.	*/
	public static void showGUI(){
		try {
			javax.swing.JFrame frame = new javax.swing.JFrame();
			printPanel03 inst = new printPanel03(null,null,null);
			frame.setContentPane(inst);
			frame.getContentPane().setSize(inst.getSize());
			frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** Auto-generated event handler method */
	protected void jTextField1CaretUpdate(CaretEvent evt){
		wizardContext.inputChanged();
	}
	 public void enteredFromLeft(Map dataMap)
  {
        
	     
	     //Iniciamos la ayuda
            try {
                String helpHS="ayuda.hs";
                HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
                HelpBroker hb = hs.createHelpBroker();
                hb.enableHelpKey(this,"planeamientoGenerarMapaPlaneamiento03",hs); 
          }catch (Exception excp){
          }                        
 //fin de la ayuda


    
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
    PrintLayoutFrame frame = new PrintLayoutFrame(context);
    
    // check whether are a layer of sheets in the blackboard
    Layer selectedLayer = (Layer) blackboardInformes.get(SeriePrintPlugIn.CAPAPLANTILLA);
    if (selectedLayer!=null)
    	{
    	//Construct Extents from features
    	FeatureCollection col=selectedLayer.getFeatureCollectionWrapper();
    	
    	frame.getExtentManager().setExtentsFromFeatures(col);
    	frame.getExtentManager().setCurrentExtent(0);
    	frame.setMapsExtents();
    	}
      
      frame.setVisible(true);
      frame.forceOpen();
      
    }
     /**
     * Tip: Delegate to an InputChangedFirer.
     * @param listener a party to notify when the input changes (usually the
     * WizardDialog, which needs to know when to update the enabled state of
     * the buttons.
     */
    public void add(InputChangedListener listener)
    {
        
    }

    public void remove(InputChangedListener listener)
    {
      
    }

    public String getTitle()
    {
      return this.getName();
    }

  
	

    public String getInstructions()
    {
      return  (""); 
      
    }

    public boolean isInputValid()
    {
     return true;
    }

    public void setWizardContext(WizardContext wd)
    {
      wizardContext = wd;
    }
    public String getID()
    {
      return localId;
    }
    /**
     * @return null to turn the Next button into a Finish button
     */
    private String nextID=null;

    public void setNextID(String nextID)
    {
    	this.nextID=nextID;
    }
    public String getNextID()
    {
      return nextID;
    }

  public printPanel03()
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

  private void jbInit() throws Exception
  {
    this.setSize(new java.awt.Dimension(300,300));
    this.setLayout(new BorderLayout());

    this.add(panel, java.awt.BorderLayout.NORTH);  // Generated
     // Generated
  
  }

/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}


}
