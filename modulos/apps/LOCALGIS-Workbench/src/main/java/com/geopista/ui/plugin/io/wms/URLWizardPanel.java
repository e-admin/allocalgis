/**
 * URLWizardPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.io.wms;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.geopista.app.AppContext;
import com.geopista.io.datasource.wms.MapImageFormatChooser;
import com.geopista.io.datasource.wms.WMService;
import com.geopista.ui.wizard.WizardContext;
import com.vividsolutions.jump.workbench.WorkbenchException;
import com.vividsolutions.jump.workbench.ui.InputChangedFirer;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.plugin.wms.MapLayerWizardPanel;


public class URLWizardPanel extends JPanel implements com.geopista.ui.wizard.WizardPanel {
    
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    public static final String SERVICE_KEY = "SERVICE";
    public static final String FORMAT_KEY = "FORMAT";
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
            urlTextField.setFont(new JLabel().getFont());
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

    void jbInit() throws Exception {
        urlLabel.setText("URL:");
        this.setLayout(gridBagLayout1);
        urlTextField.setPreferredSize(new Dimension(300, 21));
        urlTextField.setText("http://");
        urlTextField.setCaretPosition(urlTextField.getText().length());
        urlLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 3) {
                    urlTextField.setText("http://slkapps2.env.gov.bc.ca/servlet/com.esri.wms.Esrimap");
                }
                super.mouseClicked(e);
            }
        });
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
        return  aplicacion.getI18nString("IntroduzcaWMSURL");
    }

    private String fix(String url) {
        return url.trim() + ((!url.trim().endsWith("?") && !url.trim().endsWith("&")) ? "?" : "");
    }

    public void exitingToRight() throws IOException, WorkbenchException {
        dataMap.put(URL_KEY, urlTextField.getText());

        WMService service = new WMService(fix(urlTextField.getText()));
        service.initialize();
        dataMap.put(SERVICE_KEY, service);

        MapImageFormatChooser formatChooser = new MapImageFormatChooser();
        String format = formatChooser.chooseFormat(service.getCapabilities()
                                                          .getMapFormats());

        if (format == null) {
            throw new WorkbenchException(
                "The server does not support GIF, PNG, or JPEG formats");
        }

        dataMap.put(FORMAT_KEY, format);
        dataMap.put(MapLayerWizardPanel.INITIAL_LAYER_NAMES_KEY, null);
        formatChooser.setPreferLossyCompression(false);
        formatChooser.setTransparencyRequired(true);
    }

    public void enteredFromLeft(Map dataMap) {
        this.dataMap = dataMap;
        urlTextField.setCaretPosition(0);
        urlTextField.moveCaretPosition(urlTextField.getText().length());
    }

    public String getTitle() {
        return aplicacion.getI18nString("SeleccioneURL");
    }

    public String getID() {
        return getClass().getName();
    }

    public boolean isInputValid() {
        return true;
    }

    public String getNextID() {
        return MapLayerWizardPanel.class.getName();
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