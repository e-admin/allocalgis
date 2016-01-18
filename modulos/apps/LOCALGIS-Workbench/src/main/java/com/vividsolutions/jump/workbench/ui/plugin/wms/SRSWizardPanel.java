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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.ui.wizard.WizardContext;
import com.vividsolutions.jump.workbench.ui.InputChangedFirer;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.wms.Capabilities;
import com.vividsolutions.wms.WMService;


public class SRSWizardPanel extends JPanel implements com.geopista.ui.wizard.WizardPanel {
    public static final String SRS_KEY = "SRS";
    public static final String FORMAT_KEY = "FORMAT";
    private InputChangedFirer inputChangedFirer = new InputChangedFirer();
    private Map dataMap;
    private JPanel jpSRS=new JPanel();
    private JLabel srsLabel = new JLabel();
    private DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
    private JComboBox comboBox = new JComboBox();
    private JPanel jpFormat=new JPanel();
    private JLabel jlFormat = new JLabel("Formato: ");
    private JComboBox jcbFormats=new JComboBox();
   
    
    public SRSWizardPanel() {
        try {
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

    public String getInstructions() {
        return AppContext.getApplicationContext().getI18nString("ThelayerssupportmoreoneReference");
        
    }

    void jbInit() throws Exception {
        srsLabel.setText(AppContext.getApplicationContext().getI18nString("CoordinateReferenceSystem")+":");
      
        FlowLayout fl=new FlowLayout();
        fl.setAlignment(FlowLayout.LEFT);
        jpSRS.setLayout(fl);
        FlowLayout fl1=new FlowLayout();
        fl1.setAlignment(FlowLayout.LEFT);
        jpFormat.setLayout(fl1);
        jpSRS.add(srsLabel);
        jpSRS.add(comboBox);
        jpFormat.add(jlFormat);
        jpFormat.add(jcbFormats);
       
        this.add(jpSRS,BorderLayout.NORTH);
        this.add(jpFormat,BorderLayout.CENTER);
    }

    public void exitingToRight() {
        int index = comboBox.getSelectedIndex();
        String srsCode = (String) getCommonSrsList().get( index );
        dataMap.put( SRS_KEY, srsCode );
        int formatIndex=this.jcbFormats.getSelectedIndex();  
		dataMap.put( FORMAT_KEY, getFormats()[formatIndex] );
    }

    private List getCommonSrsList() {
        return (List) dataMap.get(MapLayerWizardPanel.COMMON_SRS_LIST_KEY);
    }
    
    
    private String[] getFormats(){
    WMService service= (WMService) dataMap.get(URLWizardPanel.SERVICE_KEY);
    Capabilities capabilities=service.getCapabilities();
    return capabilities.getMapFormats();
    }//fin del método getFormats
    
    
    public void enteredFromLeft(Map dataMap) {
    	jcbFormats.removeAllItems();
        this.dataMap = dataMap;
        
        
        WMService service= (WMService) dataMap.get(URLWizardPanel.SERVICE_KEY);
        Capabilities capabilities=service.getCapabilities();
        String formats[]=capabilities.getMapFormats();
        for(int i=0;i<formats.length;i++)
        	jcbFormats.addItem(formats[i]);
        
        for (Iterator i = getCommonSrsList().iterator();i.hasNext();) {
            String srs = (String) i.next();
            String srsName = SRSUtils.getName( srs );
            comboBoxModel.addElement( srsName );
        }

        comboBox.setModel(comboBoxModel);
    }

    public String getTitle() {
        return AppContext.getApplicationContext().getI18nString("SelectCoordinateReferenceSystem");
    }

    public String getID() {
        return getClass().getName();
    }

    public boolean isInputValid() {
        return true;
    }

    public String getNextID() {
        return null;
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
