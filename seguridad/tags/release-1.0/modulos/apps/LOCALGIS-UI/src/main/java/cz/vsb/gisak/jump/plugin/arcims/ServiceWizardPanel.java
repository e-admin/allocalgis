/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 * 
 * Code is based on code from com.vividsolutions.jump.workbench.ui.plugin.wms.URLWizardPanel.java
 *
 * $Id: ServiceWizardPanel.java,v 0.1 20041110 
 *
 * Copyright (C) 2004 Jan Ruzicka jan.ruzicka@vsb.cz
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
 */

/**
 *
 * <p>Title: ServiceWizardPanel</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Jan Ruzicka jan.ruzicka@vsb.cz
 * @version 0.1
 */

package cz.vsb.gisak.jump.plugin.arcims;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
//import javax.swing.JList;
//import javax.swing.DefaultListModel;
//import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;


import com.vividsolutions.jump.workbench.WorkbenchException;
import com.vividsolutions.jump.workbench.ui.InputChangedFirer;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.geopista.app.AppContext;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;

/**
* Panel for setting URL to ArcIMS Server.
* @author jan.ruzicka@vsb.cz
*/

public class ServiceWizardPanel extends JPanel implements WizardPanel {
    public static final String SERVICE_KEY = "SERVICE";
    private InputChangedFirer inputChangedFirer = new InputChangedFirer();
    private Map dataMap;
    //private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JLabel servicesLabel = new JLabel();
    private JComboBox servicesList = new JComboBox();
    //private JScrollPane scrollPane = new JScrollPane(servicesList);

    //private JPanel fillerPanel = new JPanel();
    private String serviceURL;
   

    public ServiceWizardPanel(String initialURL) {
        try {
            serviceURL = initialURL;
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void add(InputChangedListener listener) {
        inputChangedFirer.add(listener);
    }

    public void remove(InputChangedListener listener) {
        inputChangedFirer.remove(listener);
    }
   
    /**
    * Creates UI on JPanel
    */
    void jbInit() throws Exception {
        servicesLabel.setText(AppContext.getApplicationContext().getI18nString("AvailableServices"));
        //this.setLayout(gridBagLayout1);
        //servicesList.setPreferredSize(new Dimension(150, 150));
        //urlTextField.setCaretPosition(urlTextField.getText().length());

        this.add(servicesLabel);
        this.add(servicesList);
	
    }

    public String getInstructions() {
        return AppContext.getApplicationContext().getI18nString("PleaseSelectOneOfTheAvailableServices");
    }
     

    /**
    * Works after Finish button. Set value from list to the dataMap object.
    */
    public void exitingToRight() throws IOException, WorkbenchException {
        dataMap.put(SERVICE_KEY, (String) servicesList.getSelectedItem());

    }

    public void enteredFromLeft(Map dataMap) {
        this.dataMap = dataMap;
	connect((ArcIMSConnector) dataMap.get(URLWizardPanel.CONNECTOR_KEY));
    }

    public String getTitle() {
        return AppContext.getApplicationContext().getI18nString("SelectService");
    }

    public String getID() {
        return getClass().getName();
    }

    public boolean isInputValid() {
	if (servicesList.getItemCount() > 0) {
	  return true; 	
	} else {
	  return false;
        }
        //return true;
    }

    public String getNextID() {
        return null;
    }

    /**
    * Connects to the ArcIMS Server and ads services to the list.
    */
    public void connect(ArcIMSConnector arcims) {
	ArrayList ser = arcims.getServices();
        //DefaultListModel listModel = new DefaultListModel();
	for (int i=0; i<ser.size(); i++) {
  	  servicesList.addItem(ser.get(i));
	  System.out.println("Service: " + ser.get(i));
	}	
        //servicesList = new JList(listModel);   
    }
    

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#setWizardContext(com.geopista.ui.wizard.WizardContext)
     */
    public void setWizardContext(WizardContext wd)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#setNextID(java.lang.String)
     */
    public void setNextID(String nextID)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }
}

