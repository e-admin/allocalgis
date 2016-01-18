/**
 * ReferenciaCatastralPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.searchcatreference;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.geopista.app.AppContext;
import com.geopista.ui.GeopistaLayerNameRenderer;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;


public class ReferenciaCatastralPanel extends JPanel
{

    private static final long serialVersionUID = 1L;
    private JLabel referenciaCatastralJPanel = null;
    private JFormattedTextField referenciaCatastralJTextField = null;
    private JComboBox refLayersJComboBox = null;
    private JLabel layerJLabel = null;
    private GeopistaLayerNameRenderer layerRenderer = null;
    
    public static final String ReferenciaCatastralSelectedLayer = "ReferenciaCatastralSelectedLayer";  //  @jve:decl-index=0:
    
    

    /**
     * This is the default constructor
     */
    public ReferenciaCatastralPanel()
        {
            super();
            initialize();
        }

    
    
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        
        layerJLabel = new JLabel();
        layerJLabel.setBounds(new Rectangle(10, 9, 216, 21));
        layerJLabel.setText(I18N.get(SearchReferenciaCatastralPlugIn.SearchReferenciaCatastralI18N,"ReferenciaCatastralPanel.SeleccioneCapaBusqueda"));
        referenciaCatastralJPanel = new JLabel();
        referenciaCatastralJPanel.setBounds(new Rectangle(239, 9, 186, 21));
        referenciaCatastralJPanel.setText(I18N.get(SearchReferenciaCatastralPlugIn.SearchReferenciaCatastralI18N,"ReferenciaCatastralPanel.IntroduzcaReferenciaCatastral"));
        
        Border border = new LineBorder(Color.gray,1);
        this.setBorder(border);
        this.setSize(431, 71);
        this.setLayout(null);
        this.add(referenciaCatastralJPanel, null);
        this.add(getReferenciaCatastralJTextField(), null);
        this.add(getRefLayersJComboBox(), null);
        this.add(layerJLabel, null);
    }

    /**
     * This method initializes referenciaCatastralJTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getReferenciaCatastralJTextField()
    {
        if (referenciaCatastralJTextField == null)
        {
            
            referenciaCatastralJTextField = new JFormattedTextField();
            
            referenciaCatastralJTextField.setBounds(new Rectangle(239, 39, 138, 21));
        }
        return referenciaCatastralJTextField;
    }

    /**
     * This method initializes refLayersJList	
     * 	
     * @return javax.swing.JList	
     */
    public JComboBox getRefLayersJComboBox()
    {
        if (refLayersJComboBox == null)
        {
            refLayersJComboBox = new JComboBox();
            refLayersJComboBox.setBounds(new Rectangle(10, 39, 182, 21));
            DefaultComboBoxModel newModel = new DefaultComboBoxModel();
            refLayersJComboBox.setModel(newModel);
            layerRenderer = new GeopistaLayerNameRenderer();
            layerRenderer.setCheckBoxVisible(false);
            refLayersJComboBox.setRenderer(layerRenderer);
            Border border = new LineBorder(Color.GRAY,1);
            refLayersJComboBox.setBorder(border);
            
    
        }
        return refLayersJComboBox;
    }
    
    public void addLayers(Collection layers)
    {
        if(layers.isEmpty()) return;
        DefaultComboBoxModel listModel = (DefaultComboBoxModel) getRefLayersJComboBox().getModel();
        Iterator layersIterator = layers.iterator();
        while(layersIterator.hasNext())
        {
            Layer currentLayer = (Layer) layersIterator.next();
            listModel.addElement(currentLayer);
        }
        
        Blackboard blackboard = AppContext.getApplicationContext().getBlackboard();
        Layer selectLayer = (Layer) blackboard.get(ReferenciaCatastralSelectedLayer);
        if(selectLayer==null)
        {
            getRefLayersJComboBox().setSelectedIndex(0);
        }
        else
        {
            getRefLayersJComboBox().setSelectedItem(selectLayer);
        }
        
        
        
    }
    
    public boolean validateInput()
    {
        String referenciaCatastral = getReferenciaCatastralJTextField().getText();
        if(referenciaCatastral==null) return false;
        referenciaCatastral=referenciaCatastral.trim();
        if(referenciaCatastral.equals("")) return false;
        
        if(referenciaCatastral.length()>14)
        {
            JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), I18N.get(SearchReferenciaCatastralPlugIn.SearchReferenciaCatastralI18N,"ReferenciaCatastralPanel.ReferenciaCatastralExcedida"));
            return false;
        }
        
        return true;
    }


}  //  @jve:decl-index=0:visual-constraint="10,10"
