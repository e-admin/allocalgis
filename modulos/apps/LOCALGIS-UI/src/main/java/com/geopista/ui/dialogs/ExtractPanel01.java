/**
 * ExtractPanel01.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import com.geopista.app.AppContext;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.model.GeopistaLayer;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.ui.plugin.ExtractPlugIn;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
public class ExtractPanel01 extends JPanel implements WizardPanel
{
 
  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
  private Blackboard blackboard  = aplicacion.getBlackboard();
  private String localId = null;
  private WizardContext wizardContext;
  private PlugInContext context;
  private HashMap checkBoxMap = null;
  public ExtractPanel01(String id, String nextId, PlugInContext context)
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
    gridBagConstraints9.weightx = 1.0;
    gridBagConstraints9.weighty = 1.0;
    gridBagConstraints9.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints9.ipadx = -21;
    gridBagConstraints9.ipady = 141;
    gridBagConstraints9.insets = new java.awt.Insets(5,5,5,5);  // Generated
    gridBagConstraints10.gridx = 1;
    gridBagConstraints10.gridy = 0;
    gridBagConstraints10.insets = new java.awt.Insets(0,0,0,0);
    gridBagConstraints11.gridx = 2;
    gridBagConstraints11.gridy = 0;
    gridBagConstraints11.weightx = 1.0;
    gridBagConstraints11.weighty = 1.0;
    gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints11.ipadx = -21;
    gridBagConstraints11.ipady = 141;
    gridBagConstraints11.insets = new java.awt.Insets(5,5,5,5);
    this.add(getAvailableLayerScrollPane(), gridBagConstraints9);  // Generated
    this.add(getButtonsPanel(), gridBagConstraints10);  // Generated
    this.add(getLayersToExtractScrollPane(), gridBagConstraints11);  // Generated
   
    
    
  }

 

  

  public void enteredFromLeft(Map dataMap)
  {
 
  

    
  
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        Enumeration extractLayers = ((DefaultListModel) layersToExtractList.getModel()).elements();
        ArrayList toExtractLayers = new ArrayList();
        ArrayList lockedLayers = new ArrayList();
        ArrayList toUnlockLayers = new ArrayList();
        
        GeopistaConnection geoconn = null;
        
        //Comprobar si las capas estan bloqueadas
        while(extractLayers.hasMoreElements())
        {
            GeopistaLayer gl = (GeopistaLayer) extractLayers.nextElement();
            //Se intenta bloquear cada capa
            try
            {
                String sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");
                AdministradorCartografiaClient acClient = new AdministradorCartografiaClient(
                        sUrlPrefix + "/AdministradorCartografiaServlet");
                acClient.lockLayer(gl.getSystemId(), null);
                toExtractLayers.add(gl);
                toUnlockLayers.add(gl);
                
            } 
            catch (Exception e1)
            {
                // Si falla algo en el bloqueo seguimos con el resto de
                // las capas bloqueadas
                //e1.printStackTrace();
                lockedLayers.add(gl);
            }
                       
        }
        
        if (lockedLayers.size()>0)
        {
            
            String string1 = aplicacion.getI18nString("general.si"); 
            String string2 = aplicacion.getI18nString("general.no"); 
            Object[] options = {string1, string2};
            
            StringBuffer mensaje = new StringBuffer(aplicacion.getI18nString("ExtractMapPlugin.capasbloqueadas"));
            mensaje.append("\n");
            Iterator it = lockedLayers.iterator();
            
            while (it.hasNext())
            {
                mensaje.append(" - ").append(it.next()).append("\n");
            }
            
            mensaje.append(aplicacion.getI18nString("ExtractMapPlugin.avisoconflicto"))
            .append("\n").append(aplicacion.getI18nString("ExtractMapPlugin.extraerseguro"));
            
            int n = JOptionPane.showOptionDialog(this,
                    mensaje.toString(),
                    aplicacion.getI18nString("ExtractMapPlugin.titulo"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
            
            if (n==JOptionPane.YES_OPTION)
            {   
                it = lockedLayers.iterator();
                
                while (it.hasNext())
                {
                    GeopistaLayer gl = (GeopistaLayer)(it.next());
                    toExtractLayers.add(gl);
                    toUnlockLayers.add(gl);
                }
                
            }
            else if (n==JOptionPane.NO_OPTION)
            {
                
                ListModel modr = layersToExtractList.getModel();
                ListModel model = availableLayerList.getModel();
                for(int i=0; i<lockedLayers.size(); i++)
                {                        
                    if (model instanceof DefaultListModel)
                    {
                        DefaultListModel mod=(DefaultListModel)model;
                        mod.addElement(lockedLayers.get(i));
                    }
                    if (modr instanceof DefaultListModel)
                    {
                        DefaultListModel mod=(DefaultListModel)modr;
                        mod.removeElement(lockedLayers.get(i));
                    }
                }
                
                wizardContext.inputChanged();
                
            }            
            
        }
        
        if (layersToExtractList.getModel().getSize()==0)
        {
            this.nextID="";
            
        }
        else
        {
            this.nextID="ExtractPanel02";
        }
         
        Iterator itUnlock = toUnlockLayers.iterator();
        while (itUnlock.hasNext())
        {
            GeopistaLayer gl = (GeopistaLayer)itUnlock.next();
            
            
            // Se intenta desbloquear cada capa
            try
            {
                String sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");
                AdministradorCartografiaClient acClient = new AdministradorCartografiaClient(
                        sUrlPrefix + "/AdministradorCartografiaServlet");
                acClient.unlockLayer(gl.getSystemId());                    
                
            } 
            catch (Exception e1)
            {
                // Si falla algo en el desbloqueo seguimos con el resto de
                // las capas bloqueadas, pero no se extraerá la capa
                //e1.printStackTrace();
                
                toExtractLayers.remove(gl);
                
                //Se informa al usuario
                StringBuffer msg = new StringBuffer();
                msg.append("No ha podido desbloquearse la capa: ").append(gl.toString())
                .append("\n").append("Esta capa no se extraerá. Se continuará con el resto");
                
                JOptionPane optionPane= new JOptionPane(msg.toString(),
                        JOptionPane.ERROR_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();                    
                
            }                
        }
        
        
        blackboard.put(ExtractPlugIn.EXTRACTLAYERS,toExtractLayers);
        
       
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
      return aplicacion.getI18nString("ExtractPanel01Instructions");
    }

    public boolean isInputValid()
    {
        if (layersToExtractList.getModel().getSize()!=0)
            return true;
        else 
            return false;
        
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
	private JScrollPane layersToExtractScrollPane = null;
	private JList layersToExtractList = null;
	private JPanel buttonsPanel = null;
	private JButton toLeftButton = null;
	private JButton toRightButton1 = null;
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
			GeopistaUtil.initializeLayerList("available",availableLayerList,null,"",
					context.getLayerManager().getLayers());
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
	private JScrollPane getLayersToExtractScrollPane() {
		if (layersToExtractScrollPane == null) {
			layersToExtractScrollPane = new JScrollPane();
			layersToExtractScrollPane.setViewportView(getLayersToExtractList());
		}
		return layersToExtractScrollPane;
	}
	/**
	 * This method initializes layersToExtractList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getLayersToExtractList() {
		if (layersToExtractList == null) {
			layersToExtractList = new JList();
			GeopistaUtil.initializeLayerList("available",layersToExtractList,null,"",
					new Vector());
		}
		return layersToExtractList;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getButtonsPanel() {
		if (buttonsPanel == null) {
			buttonsPanel = new JPanel();
			buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
			buttonsPanel.add(getToRightButton1(), null);
			buttonsPanel.add(getToLeftButton(), null);
		}
		return buttonsPanel;
	}
	/**
	 * This method initializes toLeftButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getToLeftButton() {
		if (toLeftButton == null) {
			toLeftButton = new JButton();
			toLeftButton.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/Left.gif")));
			toLeftButton.addActionListener(new java.awt.event.ActionListener() { 
			    public void actionPerformed(java.awt.event.ActionEvent e) {    
			        Object[] lay= layersToExtractList.getSelectedValues();
			        if (lay!=null)
			        {
			            ListModel modr = layersToExtractList.getModel();
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
		return toLeftButton;
	}
	/**
	 * This method initializes toRightButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getToRightButton1() {
		if (toRightButton1 == null) {
			toRightButton1 = new JButton();
			toRightButton1.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/Right.gif")));
			toRightButton1.addActionListener(new java.awt.event.ActionListener() { 
			    public void actionPerformed(java.awt.event.ActionEvent e) {    
			        
			        Object[] lay=  availableLayerList.getSelectedValues();
			        if (lay!=null)
			        {
			            ListModel modl = availableLayerList.getModel();
			            ListModel modr = layersToExtractList.getModel();
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
		return toRightButton1;
	}

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"