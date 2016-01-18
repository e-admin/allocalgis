package com.geopista.ui.dialogs.mobile;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import com.geopista.ui.dialogs.global.Constants;
import com.geopista.ui.plugin.mobile.MobileExtractPlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.WMSLayer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class MobileExtractPanel02 extends JPanel implements WizardPanel
{
 
  private Blackboard blackboard  = Constants.APLICACION.getBlackboard();
  private String localId = null;
  private WizardContext wizardContext;
  private PlugInContext context;
  public static final String MOBILE_WMS_LAYERS = "MOBILE_WMS_LAYERS";


  public MobileExtractPanel02(String id, String nextId, PlugInContext context)
  {
    this.nextID = nextId;
    this.localId = id;
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
	java.awt.GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
    java.awt.GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
    java.awt.GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
   
    this.setLayout(new GridBagLayout());
    gridBagConstraints9.gridx = 0;
    gridBagConstraints9.gridy = 0;
    gridBagConstraints9.weightx = 1;
    gridBagConstraints9.weighty = 1;
    gridBagConstraints9.gridheight = 2;
    gridBagConstraints9.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints9.ipadx = 0;
    gridBagConstraints9.ipady = 141;
    gridBagConstraints9.insets = new java.awt.Insets(5,5,5,5);
    
    gridBagConstraints10.gridx = 1;
    gridBagConstraints10.gridy = 0;
    gridBagConstraints10.insets = new java.awt.Insets(0,0,0,0);
    
    gridBagConstraints11.gridx = 2;
    gridBagConstraints11.gridy = 0;
    gridBagConstraints11.weightx = 1;
    gridBagConstraints11.weighty = 1;
    gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints11.ipadx = 0;
    gridBagConstraints11.ipady = 141;
    gridBagConstraints11.insets = new java.awt.Insets(5,5,5,5);
    

    this.add(getAvailableLayerScrollPane(), gridBagConstraints9); 
    this.add(getWmsButtonsPanel(), gridBagConstraints10);  
    this.add(getWmsLayersScrollPane(), gridBagConstraints11);
  }

  public void enteredFromLeft(Map dataMap)
  {
  
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
    	//salvamos las capas seleccionadas (podría no ser ninguna)      
	    Enumeration wmsEnumLayers = ((DefaultListModel) wmsLayersList.getModel()).elements();
	    if(wmsEnumLayers!=null && wmsEnumLayers.hasMoreElements()){
			ArrayList<WMSLayer> wmsLayers = new ArrayList<WMSLayer>();
			while(wmsEnumLayers.hasMoreElements())
			{
				WMSLayer wl = (WMSLayer) wmsEnumLayers.nextElement();
				wmsLayers.add(wl);
		    }	
			blackboard.put(MOBILE_WMS_LAYERS,wmsLayers);
	    }
	    else {
	    	blackboard.remove(MOBILE_WMS_LAYERS);
	    }
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

    public String getID()
    {
      return localId;
    }

    public String getInstructions()
    {
      return I18N.get(MobileExtractPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileExtractPanel02_selecCapas);
    }

    public boolean isInputValid()
    {
//        if (wmsLayersList.getModel().getSize()!=0){
//            return true;
//        }
//        else { 
//            return false;
//        }
        return true; //siempre se puede pasar sin coger nada
    }

    public void setWizardContext(WizardContext wd)
    {
      wizardContext = wd;
    }

    /**
     * @return null to turn the Next button into a Finish button
     */

    private String nextID = null;
	private JList availableLayerList = null;
	private JScrollPane availableLayerScrollPane = null;
	private JScrollPane wmsLayersScrollPane = null;
	private JList wmsLayersList = null;
	private JPanel buttonsWmsPanel = null;
	private JButton toLeftButtonWms = null;
	private JButton toRightButtonWms = null;
	
    public void setNextID(String nextID)
    {
    	this.nextID=nextID;
    }
    public String getNextID()
    {
      return nextID;
    }

  
	/**
	 * This method initializes availableLayerList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getAvailableLayerList() {
		if (availableLayerList == null) {
			availableLayerList = new JList();
			GeopistaUtil.initializeWMSLayerList("available",availableLayerList,null,"",
					context.getLayerManager().getOrderLayers());
		}
		
		return availableLayerList;
	}
	/**
	 * This method initializes availableLayerScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getAvailableLayerScrollPane() {
		if (availableLayerScrollPane == null) {
			availableLayerScrollPane = new JScrollPane();
			availableLayerScrollPane.setViewportView(getAvailableLayerList());
			availableLayerScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return availableLayerScrollPane;
	}
	/**
	 * This method initializes layersToExtractScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getWmsLayersScrollPane() {
		if (wmsLayersScrollPane == null) {
			wmsLayersScrollPane = new JScrollPane();
			wmsLayersScrollPane.setViewportView(getWmsLayersList());
			wmsLayersScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return wmsLayersScrollPane;
	}
	/**
	 * This method initializes layersToExtractList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getWmsLayersList() {
		if (wmsLayersList == null) {
			wmsLayersList = new JList();
			GeopistaUtil.initializeWMSLayerList("wms",wmsLayersList,null,"",
					new Vector());
		}
		return wmsLayersList;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getWmsButtonsPanel() {
		if (buttonsWmsPanel == null) {
			buttonsWmsPanel = new JPanel();
			buttonsWmsPanel.setLayout(new BoxLayout(buttonsWmsPanel, BoxLayout.Y_AXIS));
			buttonsWmsPanel.add(getToRightButtonWms(), null);
			buttonsWmsPanel.add(getToLeftButtonWms(), null);
		}
		return buttonsWmsPanel;
	}
	
	/**
	 * This method initializes toLeftButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getToLeftButtonWms() {
		if (toLeftButtonWms == null) {
			toLeftButtonWms = new JButton();
			toLeftButtonWms.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/Left.gif")));
			toLeftButtonWms.addActionListener(new java.awt.event.ActionListener() { 
			    public void actionPerformed(java.awt.event.ActionEvent e) {    
			        Object[] lay= wmsLayersList.getSelectedValues();
			        if (lay!=null)
			        {
			            ListModel modr = wmsLayersList.getModel();
			            ListModel model = availableLayerList.getModel();
			            for(int n=0; n<lay.length; n++)
			            {
			                if (model instanceof DefaultListModel)
			                {
			                    DefaultListModel mod=(DefaultListModel)model;
			                    mod.addElement(lay[n]);
			                }
			                if (modr instanceof DefaultListModel)
			                {
			                    DefaultListModel mod=(DefaultListModel)modr;
			                    mod.removeElement(lay[n]);
			                }
			            }
			        }
			        wizardContext.inputChanged();
			    }
			});
		}
		return toLeftButtonWms;
	}
	/**
	 * This method initializes toRightButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getToRightButtonWms() {
		if (toRightButtonWms == null) {
			toRightButtonWms = new JButton();
			toRightButtonWms.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/Right.gif")));
			toRightButtonWms.addActionListener(new java.awt.event.ActionListener() { 
			    public void actionPerformed(java.awt.event.ActionEvent e) {    
			        
			        Object[] lay=  availableLayerList.getSelectedValues();
			        if (lay!=null)
			        {
			            ListModel modl = availableLayerList.getModel();
			            ListModel modr = wmsLayersList.getModel();
			            for(int n=0; n<lay.length; n++)
			            {
			                
			                if (modr instanceof DefaultListModel)
			                {
			                    DefaultListModel mod=(DefaultListModel)modr;
			                    mod.addElement(lay[n]);
			                }
			                if (modl instanceof DefaultListModel)
			                {
			                    DefaultListModel mod=(DefaultListModel)modl;
			                    mod.removeElement(lay[n]);
			                }
			            }
			            
			        }
			        
			        wizardContext.inputChanged();
			        
			    }
			});
		}
		return toRightButtonWms;
	}
	
    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        
        
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
