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
 * Created on 18-jun-2004 by juacas
 *
 * 
 */
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Map;

import javax.help.HelpBroker;
import javax.help.HelpSet;
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
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

import java.awt.BorderLayout;
/**

*/

public class printPanel01 extends javax.swing.JPanel implements WizardPanel{
	private JPanel selectTitleDescriptionPanel;
	private JScrollPane jScrollPane1;
  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
  private Blackboard blackboardInformes  = aplicacion.getBlackboard();
  private WizardContext wizardContext;
	private JTextField titleTextField;
	private JTextArea descriptionTextField;
	private JLabel descriptionLabel;
	private JLabel titleLabel;
  private String localId = null;
private PlugInContext	context;

  
	public printPanel01(String id, String nextId, PlugInContext context2) {
	    this.context=context2;
	    this.nextID = nextId;
	    this.localId = id;
    
		setName((I18N.get("printPlugin.Name"))); //$NON-NLS-1$
		initGUI();
	}

	/**
	* Initializes the GUI.
	* Auto-generated code - any changes you make will disappear.
	*/
	public void initGUI(){
		try {
			preInitGUI();
			titleLabel = new JLabel();
			descriptionTextField = new JTextArea();
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			GridBagLayout thisLayout = new GridBagLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(535, 314));
			
			selectTitleDescriptionPanel = new JPanel();
			this.add(selectTitleDescriptionPanel, new GridBagConstraints(
				99,
				0,
				GridBagConstraints.REMAINDER,
				1,
				0.0,
				0.0,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0,
				0));
			selectTitleDescriptionPanel.setLayout(new GridBagLayout());
			selectTitleDescriptionPanel.setVisible(true);
			selectTitleDescriptionPanel.setPreferredSize(new java.awt.Dimension(200,500));  // Generated
			gridBagConstraints9.gridx = 0;  // Generated
			gridBagConstraints9.gridy = 0;  // Generated
			gridBagConstraints9.insets = new java.awt.Insets(5,5,5,5);  // Generated
			gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.NORTHEAST;  // Generated
			gridBagConstraints10.gridx = 1;  // Generated
			gridBagConstraints10.weighty = 0.0;  // Generated
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.NORTHWEST;  // Generated
			gridBagConstraints10.gridy = 0;  // Generated
			gridBagConstraints10.weightx = 1.0;  // Generated
			gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints10.ipadx = 97;  // Generated
			gridBagConstraints10.ipady = 0;  // Generated
			gridBagConstraints10.insets = new java.awt.Insets(5,5,5,5);  // Generated
			gridBagConstraints11.gridx = 0;  // Generated
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.NORTHEAST;  // Generated
			gridBagConstraints11.gridy = 1;  // Generated
			gridBagConstraints11.insets = new java.awt.Insets(5,5,5,9);  // Generated
			gridBagConstraints12.gridx = 1;  // Generated
			gridBagConstraints12.weightx = 1.0;  // Generated
			gridBagConstraints12.insets = new java.awt.Insets(5,5,5,5);  // Generated
			gridBagConstraints12.weighty = 1.0;  // Generated
			gridBagConstraints12.fill = java.awt.GridBagConstraints.BOTH;  // Generated
			gridBagConstraints12.gridy = 1;  // Generated
		
			//jLabel1.setText(aplicacion.getI18nString("printPanel01.Titulo"));
			titleLabel.setPreferredSize(new java.awt.Dimension(31, 16));
		
			titleTextField = new JTextField();
			titleTextField.setPreferredSize(new java.awt.Dimension(239, 21));
			titleTextField.setSize(100, 20);
			titleTextField.addCaretListener(new CaretListener() {
				public void caretUpdate(CaretEvent evt) {
					jTextField1CaretUpdate(evt);
				}
			});
		
		
			descriptionLabel = new JLabel();
			descriptionLabel.setText(I18N.get("printPlugin.Subtitle")); //$NON-NLS-1$
			descriptionLabel.setPreferredSize(new java.awt.Dimension(68, 16));
		
		
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setPreferredSize(new java.awt.Dimension(398,300));  // Generated
			jScrollPane1.setMinimumSize(new java.awt.Dimension(200,150));  // Generated
			
				
			jScrollPane1.setViewportView(descriptionTextField);
			descriptionTextField.setVisible(true);
			descriptionTextField.setSize(0, 200);
			descriptionTextField.setBounds(117, 55, 75, 40);
			titleLabel.setText(I18N.get("printPlugin.Title"));  // Generated //$NON-NLS-1$
			descriptionTextField.setLineWrap(true);  // Generated
			descriptionTextField.setRows(5);  // Generated
			descriptionTextField.setWrapStyleWord(true);  // Generated
			selectTitleDescriptionPanel.add(titleLabel, gridBagConstraints9);  // Generated
			selectTitleDescriptionPanel.add(titleTextField, gridBagConstraints10);  // Generated
			selectTitleDescriptionPanel.setPreferredSize(new java.awt.Dimension(300,500));
		
	
			selectTitleDescriptionPanel.add(descriptionLabel, gridBagConstraints11);  // Generated
			selectTitleDescriptionPanel.add(jScrollPane1, gridBagConstraints12);  // Generated
					
			this.add(selectTitleDescriptionPanel);
		
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
			printPanel01 inst = new printPanel01(null,null,null);
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
                hb.enableHelpKey(this,"planeamientoGenerarMapaPlaneamiento02",hs); 
          }catch (Exception excp){
          }                        
 //fin de la ayuda

    ArrayList titulos = (ArrayList) blackboardInformes.get(GeopistaPrintPlugIn.TITULOMAPAIMPRIMIR);
    if(titulos!=null)
    {
      String tituloMapa = (String) titulos.get(0);
      titleTextField.setText(tituloMapa);
    }

    String descripcionMapa = (String) blackboardInformes.get(GeopistaPrintPlugIn.DESCRIPCIONMAPAIMPRIMIR);
    if(descripcionMapa!=null)
    {
      descriptionTextField.setText(descripcionMapa);
    }
   
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
      ArrayList titulos = new ArrayList();
      titulos.add(titleTextField.getText());
      blackboardInformes.put(GeopistaPrintPlugIn.TITULOMAPAIMPRIMIR,titulos);
      blackboardInformes.put(GeopistaPrintPlugIn.DESCRIPCIONMAPAIMPRIMIR,descriptionTextField.getText());
    
      /*
      PrintLayoutFrame frame = new PrintLayoutFrame(context);
      frame.setVisible(true);
      frame.forceOpen();
      */
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
      return  (I18N.get("printPlugin.Instructions")); //$NON-NLS-1$
      
    }

    public boolean isInputValid()
    {
      if(titleTextField.getText()!=null)
      {
        if(!titleTextField.getText().trim().equals(""))
        {
          return true; 
        }
      }
      return false;
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

  public printPanel01()
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
    this.setSize(new java.awt.Dimension(500,400));
    this.setLayout(new BorderLayout());

    this.add(selectTitleDescriptionPanel, java.awt.BorderLayout.CENTER);  // Generated
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
