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
 * Created on 23-sep-2004 by juacas
 *
 * 
 */
package com.geopista.util;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.SeriePrintPlugIn;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class SelectSheetLayerPrintPanel extends JPanel implements WizardPanel
{

	private JLabel jLabel = null;
	private JComboBox jComboBox = null;
  private String localId = null;
  private String nextId = null;
  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

  private Blackboard blackboardInformes  = aplicacion.getBlackboard();

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#enteredFromLeft(java.util.Map)
	 */
	public void enteredFromLeft(Map dataMap)
	{
    Layer initialSelectLayer = (Layer) context.getLayerManager().getLayer(SeriePrintPlugIn.LAYER);
    if(initialSelectLayer==null) initialSelectLayer=context.getLayerManager().getLayer(0);
		jComboBox = GeopistaFunctionUtils.initializeLayerComboBox("SheetLayer",jComboBox,initialSelectLayer,null,context.getLayerManager().getLayers());
    
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#exitingToRight()
	 */
	public void exitingToRight() throws Exception
	{
	Layer selected=(Layer)jComboBox.getSelectedItem() ;
		blackboardInformes.put(SeriePrintPlugIn.CAPAPLANTILLA,selected);

	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#add(com.vividsolutions.jump.workbench.ui.InputChangedListener)
	 */
	public void add(InputChangedListener listener)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#remove(com.vividsolutions.jump.workbench.ui.InputChangedListener)
	 */
	public void remove(InputChangedListener listener)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#getTitle()
	 */
	public String getTitle()
	{
	    return this.getName();
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#getID()
	 */
	public String getID()
	{
		// TODO Auto-generated method stub
		return localId;
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#getInstructions()
	 */
	public String getInstructions()
	{
	      return aplicacion.getI18nString("SelectSheetLayerPrintPanel.Instructions");
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#isInputValid()
	 */
	public boolean isInputValid()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#setWizardContext(com.geopista.ui.wizard.WizardContext)
	 */
	public void setWizardContext(WizardContext wd)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#getNextID()
	 */
	public String getNextID()
	{
		// TODO Auto-generated method stub
		return nextId;
	}
	private WorkbenchContext context;
	public SelectSheetLayerPrintPanel(String id, String nextID, WorkbenchContext context) {
		super();
		
    this.localId = id;
    this.nextId = nextID;
		this.context=context;
		initialize();
		}
	/**
	 * This is the default constructor
	 */
	public SelectSheetLayerPrintPanel() {
		super();
		initialize();
   

	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		jLabel = new JLabel();
		super.setName(aplicacion.getI18nString("SelectSheetLayerPrintPanel.Name"));
		java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		this.setSize(365, 274);
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints1.insets = new java.awt.Insets(0,34,0,0);
		gridBagConstraints2.gridwidth = 2;
		gridBagConstraints2.insets = new java.awt.Insets(0,0,0,40);
		this.add(jLabel, gridBagConstraints1);
		this.add(getJComboBox(), gridBagConstraints2);
		jLabel.setText(aplicacion.getI18nString("cbSheetLayer")+": ");
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
    
	}
	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new JComboBox();
			jComboBox.addActionListener(
					new ActionListener(){
					public void actionPerformed(java.awt.event.ActionEvent arg0) {
			Layer selected=(Layer)jComboBox.getSelectedItem();
			if (selected!=null)
				{
				jLabel.setText(aplicacion.getI18nString("SelectSheetLayerPrintPanel.Generar")+" " +selected.getFeatureCollectionWrapper().size()+" "+aplicacion.getI18nString("SelectSheetLayerPrintPanel.Hojas"));
				}
			
			}});
			
		}
		return jComboBox;
	}

  public void setNextID(String nextId)
  {
    this.nextId = nextId;
  }

/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}

  


}  //  @jve:decl-index=0:visual-constraint="10,10"
