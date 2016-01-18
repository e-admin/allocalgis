/**
 * NetworkFusionFactorySelectNetworksDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.networkfusion.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import com.geopista.app.utilidades.TableSorted;
import com.geopista.ui.plugin.routeenginetools.networkfusion.beans.NetworkBean;
import com.geopista.ui.plugin.routeenginetools.networkfusion.dialogs.tables.GraphTypeCellRenderer;
import com.geopista.ui.plugin.routeenginetools.networkfusion.dialogs.tables.NetworkNameCellRenderer;
import com.geopista.ui.plugin.routeenginetools.networkfusion.dialogs.tables.NetworkTableModel;
import com.geopista.ui.plugin.routeenginetools.networkfusion.dialogs.tables.ReaderWriterTypeCellRenderer;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class NetworkFusionFactorySelectNetworksDialog extends JPanel implements WizardPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Objetos para fusion de redes
	private ArrayList<NetworkBean> networkList;
	private ArrayList<NetworkBean> networksToFusion;
	// objetos generales
	private WizardContext wizardContext;
	
	// objetos visuales
	
	private JTable availableNetworks;
	private JTable selectedNetworks;
	private JButton attach;
	private JButton detach;
	private JPanel jPanelInfo;
	private JPanel jPanelData;
	private JPanel jPanelTable;
	
    
    public NetworkFusionFactorySelectNetworksDialog(ArrayList<NetworkBean> networks) {
    	networkList = networks;
    	networksToFusion = new ArrayList<NetworkBean>();
    	initialize();
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
    	
    	this.setLayout(new GridBagLayout());
    	this.setSize(new Dimension(600, 550));
    	this.setPreferredSize(new Dimension(600, 550));
    	       
    	
    	this.add(getJPanelInfo(), 
				new GridBagConstraints(0,0,1,1, 1, 0.05,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));

		this.add(getJPanelDatos(), 
				new GridBagConstraints(0,1,1,1, 1, 0.95,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
    		
    }
    private Component getJPanelDatos() {
    	
    	
    	selectedNetworks = new JTable();
    	
    	buildNetworksTable(selectedNetworks,false);
    	
    	
    	if (jPanelData == null){
    		
    		jPanelData   = new JPanel();
    		jPanelTable   = new JPanel();
    		attach = new JButton();
    		detach = new JButton();
    		jPanelData.setLayout(new GridBagLayout());
    		
    		jPanelData.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("networkFusion","routeengine.networkfusion.borderPanelData"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
    		
    		
    		jPanelTable.setLayout(new GridBagLayout());
            
    		availableNetworks = new JTable();
    		
    		jPanelTable.add(buildNetworksTable(availableNetworks,true), 
    				new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
            
    		jPanelTable.add(buildNetworksTable(selectedNetworks,false), 
    				new GridBagConstraints(2,0,1,1, 1, 1,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
            
            
    		attach.setText(I18N.get("networkFusion","routeengine.networkfusion.attach"));
    		attach.setEnabled(true);
    		attach.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    attachNetworks();
                }
            });
            
            
            detach.setText(I18N.get("networkFusion","routeengine.networkfusion.detach"));
            detach.setEnabled(true);
            detach.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                	detachNetworks();
                }

				
            });
            
            jPanelTable.add(attach, 
    				new GridBagConstraints(1,0,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
    						GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
           
            jPanelTable.add(detach, 
    				new GridBagConstraints(1,1,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
    						GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));


            jPanelData.add(jPanelTable,
    				new GridBagConstraints(0,0,1,1, 0.8, 0.8,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
    		
    	}
    	return jPanelData;
	}
    private void attachNetworks() {
    	if(availableNetworks.getSelectedRow() < 0) return;

	    int[] idSelectedRows = availableNetworks.getSelectedRows();
	    
	    for (int i = 0; i< idSelectedRows.length; i++){
	    	NetworkBean network = (NetworkBean)availableNetworks.getValueAt(idSelectedRows[i], NetworkTableModel.ROW_NETWORK);
	    	networksToFusion.add(network);
	    	networkList.remove(network);
	    }
	    updateModelNetworks(availableNetworks,true);
	    updateModelNetworks(selectedNetworks,false);
	    this.wizardContext.inputChanged();
	}
    
    private void detachNetworks() {
    	if(selectedNetworks.getSelectedRow() < 0) return;

	    int[] idSelectedRows = selectedNetworks.getSelectedRows();
	    
	    for (int i = 0; i< idSelectedRows.length; i++){
	    	NetworkBean network = (NetworkBean)selectedNetworks.getValueAt(idSelectedRows[i], NetworkTableModel.ROW_NETWORK);
	    	networkList.add(network);
	    	networksToFusion.remove(network);
	    }
	    updateModelNetworks(availableNetworks,true);
	    updateModelNetworks(selectedNetworks,false);
	    this.wizardContext.inputChanged();
	}
	private Component getJPanelInfo() {
		if (jPanelInfo == null){

			jPanelInfo    = new JPanel();
			jPanelInfo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("networkFusion","routeengine.networkfusion.borderInfo"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

			JTextArea jTextAreaInfo = new JTextArea(I18N.get("networkFusion","routeengine.networkfusion.textAreaInfo"));
			jTextAreaInfo.setEnabled(false);
			jTextAreaInfo.setFont(new JLabel().getFont());
			jTextAreaInfo.setOpaque(false);
			jTextAreaInfo.setDisabledTextColor(Color.black);
			jTextAreaInfo.setEditable(false);
			jTextAreaInfo.setWrapStyleWord(true);
			jPanelInfo.add(jTextAreaInfo);
		}
		return jPanelInfo;
	}

	private JScrollPane buildNetworksTable(JTable table,boolean isAvailable) {
		
    	//table.setRowSelectionAllowed(true);
    	table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    	//table.setCellSelectionEnabled(true);
    	//table.setColumnSelectionAllowed(false);
    	table.getTableHeader().setReorderingAllowed(false);
    	
        JScrollPane jScrollPane = new JScrollPane(table);
        String text = "";
        if(isAvailable)
        	text = I18N.get("networkFusion","routeengine.networkfusion.available");
        else
        	text = I18N.get("networkFusion","routeengine.networkfusion.toFusion");
        jScrollPane.setBorder(new TitledBorder(text));
        updateModelNetworks(table, isAvailable);
		return jScrollPane;
		
	}

	private void updateModelNetworks(JTable table,boolean isAvailable) {
		
		NetworkTableModel tableModel = new NetworkTableModel();
		if(isAvailable)
			tableModel.setModelData(networkList);
		else{
			tableModel.setModelData(networksToFusion);
		}
        TableSorted sorter = new TableSorted(tableModel);
        sorter.setTableHeader(table.getTableHeader());
        table.setModel(sorter);
        
        table.getColumnModel().getColumn(NetworkTableModel.ROW_NETWORK).setCellRenderer(new NetworkNameCellRenderer());
        table.getColumnModel().getColumn(NetworkTableModel.ROW_NETWORK).setPreferredWidth(70);
        table.getColumnModel().getColumn(NetworkTableModel.ROW_STATUS).setCellRenderer(new ReaderWriterTypeCellRenderer());
        table.getColumnModel().getColumn(NetworkTableModel.ROW_STATUS).setPreferredWidth(60);
        table.getColumnModel().getColumn(NetworkTableModel.ROW_TYPE).setCellRenderer(new GraphTypeCellRenderer());
        table.getColumnModel().getColumn(NetworkTableModel.ROW_TYPE).setPreferredWidth(30);
        
        table.setEnabled(true);
		
	}

	/**
     * Called when the user presses Next on this panel's previous panel
     */
    public void enteredFromLeft(Map dataMap)
    { 
        
    }
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {   
        wizardContext.setData("networksToFusion",networksToFusion);
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
      return I18N.get("networkFusion","routeengine.networkfusion.selectNetworksTitle");
    }
    public String getInstructions()
    {
    	return I18N.get("networkFusion","routeengine.networkfusion.instructions");
    }
    /**
     * Este metodo valida y activa o no el boton de next
     */
    public boolean isInputValid()
    {
    	if(networksToFusion.size()<2)
    		return false;
    	return true;
       
    }
    public void setWizardContext(WizardContext wd)
    {
        wizardContext =wd;
    }
    public String getID()
    {
      return "selectNetworks";
    }
    public void setNextID(String nextID)
    {
       
    }
    public String getNextID()
    {
      return "fusionNetworks";
    }
    public void exiting()
    {
        
    }
}
