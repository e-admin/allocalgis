package com.geopista.ui.dialogs.mobile;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.Border;

import com.geopista.model.GeopistaLayer;
import com.geopista.ui.dialogs.global.Constants;
import com.geopista.ui.plugin.mobile.MobileExtractPlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaUtil;
import com.geopista.util.LCGIII_GeopistaUtil;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class MobileExtractPanel01 extends JPanel implements WizardPanel
{
 
  private Blackboard blackboard  = Constants.APLICACION.getBlackboard();
  private String localId = null;
  private WizardContext wizardContext;
  private PlugInContext context;
  private JLabel writeableLabel;
  private JLabel readableLabel;
  public static final String MOBILE_WRITEABLE_LAYERS = "MOBILE_WRITEABLE_LAYERS";
  public static final String MOBILE_READABLE_LAYERS = "MOBILE_READABLE_LAYERS";
  public static final String MOBILE_PROJECT_NAME = "MOBILE_PROJECT_NAME";
  public static final String SELECTED_LAYER = "SELECTED_LAYER";

  public MobileExtractPanel01(String id, String nextId, PlugInContext context)
  {
    this.nextID = nextId;
    this.localId = id;
    this.context = context;
    this.writeableLabel = new JLabel(I18N.get(MobileExtractPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileExtractPanel01_edicion));
    this.readableLabel = new JLabel(I18N.get(MobileExtractPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileExtractPanel01_lectura));
    this.extractionProjectNameLabel = new JLabel(I18N.get(MobileExtractPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileExtractPanel01_name));
    
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
    	this.add(new JLabel(e.getMessage()));
    }

  }

  private void jbInit() throws Exception
  {
	java.awt.GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
    java.awt.GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
	java.awt.GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
	java.awt.GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
    java.awt.GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
    java.awt.GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
    java.awt.GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
    java.awt.GridBagConstraints gridBagConstraints8 = new GridBagConstraints();


   
    this.setLayout(new GridBagLayout());
    
    gridBagConstraints8.gridx = 0;
    gridBagConstraints8.gridy = 0;
    gridBagConstraints8.gridheight =1;
    gridBagConstraints8.gridwidth= 3;


    
    gridBagConstraints9.gridx = 0;
    gridBagConstraints9.gridy = 2;
    gridBagConstraints9.weightx = 1;
    gridBagConstraints9.weighty = 1;
    gridBagConstraints9.gridheight = 4;
    gridBagConstraints9.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints9.ipadx = 0;
    gridBagConstraints9.ipady = 141;
    gridBagConstraints9.insets = new java.awt.Insets(5,5,5,5); 
    
    gridBagConstraints10.gridx = 1;
    gridBagConstraints10.gridy = 1;
    gridBagConstraints10.gridheight = 2;
    gridBagConstraints10.insets = new java.awt.Insets(0,0,0,0);

    
    gridBagConstraints11.gridx = 2;
    gridBagConstraints11.gridy = 2;
    gridBagConstraints11.weightx = 1;
    gridBagConstraints11.weighty = 1;
    gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints11.ipadx = 0;
    gridBagConstraints11.ipady = 141;
    gridBagConstraints11.insets = new java.awt.Insets(5,5,5,5);
    
    
    gridBagConstraints12.gridx = 2;
    gridBagConstraints12.gridy = 4;
    gridBagConstraints12.weightx = 1;
    gridBagConstraints12.weighty = 1;
    gridBagConstraints12.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints12.ipadx = 0;
    gridBagConstraints12.ipady = 141;
    gridBagConstraints12.insets = new java.awt.Insets(5,5,5,5);
    
    
    gridBagConstraints13.gridx = 1;
    gridBagConstraints13.gridy = 3;
    gridBagConstraints13.gridheight = 2;
    gridBagConstraints13.insets = new java.awt.Insets(0,0,0,0);
    
    gridBagConstraints14.gridx = 2;
    gridBagConstraints14.gridy = 1;
    gridBagConstraints14.insets = new java.awt.Insets(0,0,0,0);
    
    gridBagConstraints15.gridx = 2;
    gridBagConstraints15.gridy = 3;
    gridBagConstraints15.insets = new java.awt.Insets(0,0,0,0);
    
   
    
    this.add(getExtractionProjectNamePanel(),gridBagConstraints8);
    this.add(getAvailableLayerScrollPane(), gridBagConstraints9); 
    
    this.add(getWritableButtonsPanel(), gridBagConstraints10);  
    this.add(getWritableLayersScrollPane(), gridBagConstraints11);  
    this.add(getReadableLayersScrollPane(), gridBagConstraints12); 
    this.add(getReadableButtonsPanel(), gridBagConstraints13); 
    this.add(writeableLabel,gridBagConstraints14);
    this.add(readableLabel,gridBagConstraints15);
  }

  public void enteredFromLeft(Map dataMap)
  {
  
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
    	boolean activaSelected=false;
    	//salvamos las capas seleccionadas tanto editables como de sólo lectura        
	    Enumeration writeableEnumLayers = ((DefaultListModel) writableLayersList.getModel()).elements();
		ArrayList writeableLayers = new ArrayList();
		while(writeableEnumLayers.hasMoreElements())
		{
			GeopistaLayer gl = (GeopistaLayer) writeableEnumLayers.nextElement();
			
			//Indicamos la capa primera seleccionada como escritura como capa activa
			if (!activaSelected){
				blackboard.put(SELECTED_LAYER,gl.getName());
				activaSelected=true;
			}
			writeableLayers.add(gl);
	    }	
		blackboard.put(MOBILE_WRITEABLE_LAYERS,writeableLayers);
		
		
		Enumeration readableEnumLayers = ((DefaultListModel) readableLayersList.getModel()).elements();
		ArrayList readableLayers = new ArrayList();	
		while(readableEnumLayers.hasMoreElements())
		{
			GeopistaLayer gl = (GeopistaLayer) readableEnumLayers.nextElement();
			//Indicamos la capa primera seleccionada como escritura como capa activa
			if (!activaSelected){
				blackboard.put(SELECTED_LAYER,gl.getName());
				activaSelected=true;
			}

			readableLayers.add(gl);
		}	
		blackboard.put(MOBILE_READABLE_LAYERS,readableLayers);
		
		blackboard.put(MOBILE_PROJECT_NAME,extractionProjectName.getText());

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
      return I18N.get(MobileExtractPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileExtractPanel01_selecCapas);
    }

    public boolean isInputValid()
    {
        if (writableLayersList.getModel().getSize()!=0 || readableLayersList.getModel().getSize()!=0){
            return true;
        }
        else { 
            return false;
        }
        
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
	private JPanel extractionProjectNamePanel=null;
	private JTextField extractionProjectName = null;
	private JLabel extractionProjectNameLabel=null;

	
	private JScrollPane availableLayerScrollPane = null;
	private JScrollPane writableLayersScrollPane = null;
	private JScrollPane readableLayersScrollPane = null;
	private JList writableLayersList = null;
	private JList readableLayersList = null;
	private JPanel buttonsWritablePanel = null;
	private JPanel buttonsReadablePanel = null;
	private JButton toLeftButtonWrite = null;
	private JButton toRightButtonWrite = null;
	private JButton toLeftButtonRead = null;
	private JButton toRightButtonRead = null;
	
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
			GeopistaUtil.initializeNoLocalLayerList("available",availableLayerList,null,"",
					context.getLayerManager().getLayers());
		}
		
		return availableLayerList;
	}
	private JPanel getExtractionProjectNamePanel() {
		if (extractionProjectNamePanel == null) {
			Border border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
			extractionProjectNamePanel = new JPanel();
			extractionProjectNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			extractionProjectNamePanel.setBorder(border1);
			extractionProjectNamePanel.add(getExtractionProjectNameLabel());
			extractionProjectNamePanel.add(getExtractionProjectName());
		}
		return extractionProjectNamePanel;
	}
	
	private JTextField getExtractionProjectName() {
		if (extractionProjectName == null) {
			extractionProjectName = new JTextField(25);
			extractionProjectName.setText("");
		}
		return extractionProjectName;
	}
	private JLabel getExtractionProjectNameLabel() {
		if (extractionProjectNameLabel == null) {
			extractionProjectNameLabel = new JLabel();
		}
		return extractionProjectNameLabel;
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
	private JScrollPane getWritableLayersScrollPane() {
		if (writableLayersScrollPane == null) {
			writableLayersScrollPane = new JScrollPane();
			writableLayersScrollPane.setViewportView(getWritableLayersList());
			writableLayersScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return writableLayersScrollPane;
	}
	/**
	 * This method initializes layersToExtractList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getWritableLayersList() {
		if (writableLayersList == null) {
			writableLayersList = new JList();
			GeopistaUtil.initializeNoLocalLayerList("writable",writableLayersList,null,"",
					new Vector());
		}
		return writableLayersList;
	}
	/**
	 * This method initializes layersToExtractScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getReadableLayersScrollPane() {
		if (readableLayersScrollPane == null) {
			readableLayersScrollPane = new JScrollPane();
			readableLayersScrollPane.setViewportView(getReadableLayersList());
			readableLayersScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return readableLayersScrollPane;
	}
	/**
	 * This method initializes layersToExtractList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getReadableLayersList() {
		if (readableLayersList == null) {
			readableLayersList = new JList();
			GeopistaUtil.initializeNoLocalLayerList("readable",readableLayersList,null,"",
					new Vector());
		}
		return readableLayersList;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getWritableButtonsPanel() {
		if (buttonsWritablePanel == null) {
			buttonsWritablePanel = new JPanel();
			buttonsWritablePanel.setLayout(new BoxLayout(buttonsWritablePanel, BoxLayout.Y_AXIS));
			buttonsWritablePanel.add(getToRightButtonWrite(), null);
			buttonsWritablePanel.add(getToLeftButtonWrite(), null);
		}
		return buttonsWritablePanel;
	}
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getReadableButtonsPanel() {
		if (buttonsReadablePanel == null) {
			buttonsReadablePanel = new JPanel();
			buttonsReadablePanel.setLayout(new BoxLayout(buttonsReadablePanel, BoxLayout.Y_AXIS));
			buttonsReadablePanel.add(getToRightButtonRead(), null);
			buttonsReadablePanel.add(getToLeftButtonRead(), null);
		}
		return buttonsReadablePanel;
	}	
	
	/**
	 * This method initializes toLeftButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getToLeftButtonWrite() {
		if (toLeftButtonWrite == null) {
			toLeftButtonWrite = new JButton();
			toLeftButtonWrite.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/Left.gif")));
			toLeftButtonWrite.addActionListener(new java.awt.event.ActionListener() { 
			    public void actionPerformed(java.awt.event.ActionEvent e) {    
			        Object[] lay= writableLayersList.getSelectedValues();
			        if (lay!=null)
			        {
			            ListModel modr = writableLayersList.getModel();
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
		return toLeftButtonWrite;
	}
	/**
	 * This method initializes toRightButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getToRightButtonWrite() {
		if (toRightButtonWrite == null) {
			toRightButtonWrite = new JButton();
			toRightButtonWrite.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/Right.gif")));
			toRightButtonWrite.addActionListener(new java.awt.event.ActionListener() { 
			    public void actionPerformed(java.awt.event.ActionEvent e) {    
			        
			        Object[] lay=  availableLayerList.getSelectedValues();
			        if (lay!=null)
			        {
			            ListModel modl = availableLayerList.getModel();
			            ListModel modr = writableLayersList.getModel();
			            GeopistaLayer layer = null;
			            String strCapasVersionables = "";
			            for(int n=0; n<lay.length; n++)
			            {
			            	if(lay[n] instanceof GeopistaLayer){
			            		layer = (GeopistaLayer) lay[n];
			            		//la versión actual se puede editar
				            	if(layer.getRevisionActual()==-1 || layer.getRevisionActual()==layer.getUltimaRevision()){ 
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
				            	//las versiones anteriores no son editables
				            	else {
				            		strCapasVersionables += "\n* " + layer.getName();
				            	}
			            	}
			            }
			            //ventana emergente de aviso de capas versionables.
			            if(strCapasVersionables.length()>0){
			            	JOptionPane.showMessageDialog(context.getWorkbenchFrame(), "Las versiones anteriores de la capa son de sólo lectura:" + strCapasVersionables, 
			            			"Advertencia: Capas versionables", 
		            				JOptionPane.WARNING_MESSAGE);
			            }			            
			        }
			        
			        wizardContext.inputChanged();
			        
			    }
			});
		}
		return toRightButtonWrite;
	}
	
	/**
	 * This method initializes toLeftButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getToLeftButtonRead() {
		if (toLeftButtonRead == null) {
			toLeftButtonRead = new JButton();
			toLeftButtonRead.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/Left.gif")));
			toLeftButtonRead.addActionListener(new java.awt.event.ActionListener() { 
			    public void actionPerformed(java.awt.event.ActionEvent e) {    
			        Object[] lay= readableLayersList.getSelectedValues();
			        if (lay!=null)
			        {
			            ListModel modr = readableLayersList.getModel();
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
		return toLeftButtonRead;
	}
	/**
	 * This method initializes toRightButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getToRightButtonRead() {
		if (toRightButtonRead == null) {
			toRightButtonRead = new JButton();
			toRightButtonRead.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/Right.gif")));
			toRightButtonRead.addActionListener(new java.awt.event.ActionListener() { 
			    public void actionPerformed(java.awt.event.ActionEvent e) {    
			        
			        Object[] lay=  availableLayerList.getSelectedValues();
			        if (lay!=null)
			        {
			            ListModel modl = availableLayerList.getModel();
			            ListModel modr = readableLayersList.getModel();
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
		return toRightButtonRead;
	}

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"