/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 * 
 * Code is based on code from com.vividsolutions.jump.workbench.ui.plugin.wms.URLWizardPanel.java
 *
 * $Id: URLWizardPanel.java,v 0.1 20041110 
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
 * <p>Title: URLWizardPanel</p>
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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.workbench.WorkbenchException;
import com.vividsolutions.jump.workbench.ui.InputChangedFirer;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

/**
* Panel for setting URL to ArcIMS Server.
* @author jan.ruzicka@vsb.cz
*/

public class URLWizardPanel extends JPanel implements WizardPanel {
    public static final String CONNECTOR_KEY = "CONNECTOR";
    public static final String URL_KEY = "URL";
    private InputChangedFirer inputChangedFirer = new InputChangedFirer();
    private Map dataMap;
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JLabel urlLabel = new JLabel();
    private JTextField urlTextField = new JTextField();
    private JPanel fillerPanel = new JPanel();
   

    public URLWizardPanel(String initialURL) {
        try {
            jbInit();
	    urlTextField.setText(initialURL);
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
        urlLabel.setText("URL:");
        this.setLayout(gridBagLayout1);
        urlTextField.setPreferredSize(new Dimension(300, 21));
        urlTextField.setCaretPosition(urlTextField.getText().length());

        this.add(urlLabel,
            new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 4), 0, 0));
        this.add(urlTextField,
            new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 4), 0, 0));

        this.add(fillerPanel,
            new GridBagConstraints(2, 10, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
	
    }

    public String getInstructions() {
        return AppContext.getApplicationContext().getI18nString("URLWizardPanel.PleaseEnterTheURL");
    }

    /**
    * Cuts spaces from given pathname
    */
    private String fix(String url) {
        return url.trim() + ((!url.trim().endsWith("?") && !url.trim().endsWith("&")) ? "?" : "");
    }
   

    /**
    * Works after Finish button. Sets the values from text boxes to the dataMap object.
    */
    public void exitingToRight() throws IOException, WorkbenchException {
        dataMap.put(URL_KEY, fix(urlTextField.getText()));

	ArcIMSConnector connector = new ArcIMSConnector(fix(urlTextField.getText()));
        //connector.initialize();
        dataMap.put(CONNECTOR_KEY, connector);


    }

    public void enteredFromLeft(Map dataMap) {
        this.dataMap = dataMap;
    }

    public String getTitle() {
        return AppContext.getApplicationContext().getI18nString("SelectUniformResourceLocator");
    }

    public String getID() {
        return getClass().getName();
    }

    public boolean isInputValid() {
        return true;
    }

    public String getNextID() {
        return ServiceWizardPanel.class.getName();
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

