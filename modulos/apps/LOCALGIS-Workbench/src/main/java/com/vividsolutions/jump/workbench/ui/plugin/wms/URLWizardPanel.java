/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
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
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

package com.vividsolutions.jump.workbench.ui.plugin.wms;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.geopista.app.AppContext;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wms.MapImageFormatChooser;
import com.vividsolutions.jump.workbench.WorkbenchException;
import com.vividsolutions.jump.workbench.ui.InputChangedFirer;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.wms.WMService;


public class URLWizardPanel extends JPanel implements com.geopista.ui.wizard.WizardPanel {
	
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    public static final String SERVICE_KEY = "SERVICE";
    public static final String FORMAT_KEY = "FORMAT";
    public static final String URL_KEY = "URL";
    private InputChangedFirer inputChangedFirer = new InputChangedFirer();
    private Map dataMap;
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JLabel urlLabel = new JLabel();
    private JTextField urlTextField = new JTextField();
    private JPanel fillerPanel = new JPanel();
//  [UT]
    public static final String VERSION_KEY = "WMS_VERSION";
    private String wmsVersion = WMService.WMS_1_1_1;
    private boolean lossyPreferred = true;
 
    
    public URLWizardPanel(String initialURL, String wmsVersion) {
        try {
            this.wmsVersion = wmsVersion;
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
        
        
        JComboBox comboURL=new JComboBox(getDefaultWMS());
        
    	comboURL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JComboBox cb = (JComboBox)evt.getSource();
            	String selectedURL = (String)cb.getSelectedItem();
            	urlTextField.setText(selectedURL);;            	
            }
        });
    	
    	 this.add(comboURL,
    	            new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
    	                GridBagConstraints.CENTER, GridBagConstraints.NONE,
    	                new Insets(0, 0, 0, 4), 0, 0));
    	 
        this.add(urlLabel,
            new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 4), 0, 0));
        
        this.add(urlTextField,
            new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 4), 0, 0));
        
        this.add(fillerPanel,
            new GridBagConstraints(2, 10, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
//      [UT]
        this.add(createVersionButtons(
            new String[]{WMService.WMS_1_0_0, WMService.WMS_1_1_0, WMService.WMS_1_1_1}),
                new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        
        //[UT] 20.10.2005 not added yet; need more testing
        /*
        this.add(createLossyCheckBox(),
                new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        */
        
    }
    /**
     * 
     */
    private String[] getDefaultWMS(){
		String[] defaultURLs={};
		String strDefaultURL=aplicacion.getString("wms.default");
		if (strDefaultURL!=null)
			defaultURLs = strDefaultURL.split(";");
		
		return defaultURLs;

    }
    
    public String getInstructions() {
        return "Please enter the URL of the WMS server. " +
        "Then press Next to establish the connection.";
    }


    //
    // The WMService appends other parameters to the end of the URL
    //
    private String fixUrlForWMService(String url) {
        String fixedURL = url.trim();

        if ( fixedURL.indexOf( "?" ) == -1 ) {
            fixedURL = fixedURL + "?";
        } else {
            if ( fixedURL.endsWith( "?" ) ) {
                // ok
            } else {
                // it must have other parameters
                if ( !fixedURL.endsWith( "&" ) ) {
                    fixedURL = fixedURL + "&";
                }
            }
        }

        return fixedURL;
    }


    public void exitingToRight() throws IOException, WorkbenchException {
        dataMap.put(URL_KEY, urlTextField.getText());
//      [UT]
        //String ver = (String)dataMap.get(VERSION_KEY);
        
        String url = fixUrlForWMService( urlTextField.getText() );
        //[UT] 20.04.2005 
        WMService service = new WMService( url, wmsVersion );
        //WMService service = new WMService( url );
        
        service.initialize();
        dataMap.put(SERVICE_KEY, service);
//[UT] 20.04.2005 added version
        MapImageFormatChooser formatChooser = new MapImageFormatChooser(wmsVersion);
        
        formatChooser.setPreferLossyCompression( false );
        formatChooser.setTransparencyRequired( true );

        String format = formatChooser.chooseFormat(service.getCapabilities()
                                                          .getMapFormats());
        
        if (format == null) {
            throw new WorkbenchException(
                "The server does not support GIF, PNG, or JPEG formats");
        }

        dataMap.put(FORMAT_KEY, format);
        dataMap.put(MapLayerWizardPanel.INITIAL_LAYER_NAMES_KEY, null);
        dataMap.put(VERSION_KEY, wmsVersion);

    }

    public void enteredFromLeft(Map dataMap) {
        this.dataMap = dataMap;
        urlTextField.setCaretPosition(0);
        urlTextField.moveCaretPosition(urlTextField.getText().length());
    }

    public String getTitle() {
        return "Select Uniform Resource Locator (URL)";
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
    //[UT] 10.01.2005 
    private Component createVersionButtons(String[] versions){
        JPanel p = new JPanel();
        
        ActionListener al = new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JRadioButton jb = (JRadioButton)e.getSource();
                wmsVersion = jb.getText();
            }	
        };
        
        
        ButtonGroup group = new ButtonGroup();        
        JRadioButton[] buttons = new JRadioButton[ versions.length ];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JRadioButton(versions[i]);
            buttons[i].addActionListener(al);
            group.add(buttons[i]);
            p.add(buttons[i]);
            //click the last one
            if ( versions[i].equals( wmsVersion ) ){
                buttons[i].setSelected( true );
            }
        }
        
        return p;
    }
    
    //[UT] 20.10.2005 
    private Component createLossyCheckBox(){
        JPanel p = new JPanel();
        JCheckBox checkBox = new JCheckBox( "Preferr Lossy images", true );//I18N.get("ui.plugin.wms.URLWizardPanel.select-uniform-resource-locator-url") );
        checkBox.setToolTipText( "This will try to load JPEG images, if the WMS allows it." );
        checkBox.addActionListener( new ActionListener(){
            public void actionPerformed( ActionEvent e ) {
                lossyPreferred = ((JCheckBox)e.getSource()).isSelected();
            }
        });
        p.add( checkBox );
        return p;
    }
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
