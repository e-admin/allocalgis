/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * SheetPanel.java
 * 
 * Created on 4 ottobre 2004, 23.21
 *
 */

package it.businesslogic.ireport.gui.sheet;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSeparator;
/**
 *
 * @author  Administrator
 */
public class SheetPanel extends javax.swing.JPanel {
    
    private java.util.Vector properties;
    /** Creates new form SheetPanel */
    public SheetPanel() {
        initComponents();
        properties = new Vector();
        
    }
    
    public java.util.Properties getPropertiesValues()
    {
         java.util.Properties prop = new  java.util.Properties();
         
         for (int i=0; i<getProperties().size(); ++i)
         {
            SheetProperty sp = (SheetProperty)getProperties().elementAt(i);
            Object value = "";
            value = getPropertyValue(i);
            if (value != null)
            {
                prop.setProperty(sp.getKeyName(), value+"");
            }
         }
         return prop;
    } 
    
    public void setPropertiesValues(java.util.Properties prop)
    {
         Enumeration enum_keys = prop.keys();
         while (enum_keys.hasMoreElements())
         {
             String key = (String)enum_keys.nextElement();
             // Looking for this property...
             for (int i=0; i<getProperties().size(); ++i)
             {
                SheetProperty sp = (SheetProperty)getProperties().elementAt(i);
                if (sp.getKeyName().equals(key) )
                {
                    String value = prop.getProperty(key);
                    sp.setEditorValue( (javax.swing.JComponent)jPanelEditors.getComponent((2*i) +1 ), value  );
                }
             }
         }    
    } 
    
    public Object getPropertyValue(int index)
    {
         SheetProperty sp = (SheetProperty)getProperties().elementAt(index);
         return sp.getEditorValue( (javax.swing.JComponent)jPanelEditors.getComponent((2*index) +1 ) );
    } 
    
    public void removeAllProperties()
    {
         getProperties().removeAllElements();
         jPanelLabels.removeAll();
         jPanelEditors.removeAll();
         jPanelLabels.setLayout(new java.awt.GridBagLayout());
         jPanelEditors.setLayout(new java.awt.GridBagLayout());
         java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
         gridBagConstraints.gridx = 0;
         gridBagConstraints.gridy = 400;
         gridBagConstraints.weighty = 1.0;
         
         jPanelEditors.add(jPanelFillerEditors, gridBagConstraints);
         jPanelLabels.add(jPanelFillerLabels, gridBagConstraints);
         
         jPanelEditors.setMinimumSize( new java.awt.Dimension(50,0) );
         jPanelLabels.setMinimumSize(new java.awt.Dimension(50,0));
         jPanelLabels.updateUI();
         jPanelEditors.updateUI();
         jScrollPane1.updateUI();
    }
    
    public void addSheetProperty(SheetProperty sp)
    {
        properties.add(sp);
        
        JLabel jLabel = new JLabel(sp.getName());
        jLabel.setMaximumSize(new java.awt.Dimension(41, 18));
        jLabel.setMinimumSize(new java.awt.Dimension(41, 18));
        jLabel.setPreferredSize(new java.awt.Dimension(41, 18));
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = (properties.size()-1)*2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
 
        jPanelLabels.add(jLabel, gridBagConstraints);
 
        /******** Set editor *******/
        javax.swing.JComponent editor = sp.getEditor();
        editor.setOpaque(false);
        editor.setBackground( java.awt.Color.WHITE);
        editor.setMaximumSize(new java.awt.Dimension(41, 18));
        editor.setMinimumSize(new java.awt.Dimension(41, 18));
        editor.setPreferredSize(new java.awt.Dimension(41, 18));
        
        if (editor instanceof JComboBox || editor instanceof ColorSelectorPanel)
        {
            gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        }
        jPanelEditors.add(editor, gridBagConstraints);
        
        
        JSeparator jSeparator = new JSeparator(JSeparator.HORIZONTAL);
        jSeparator.setPreferredSize(new java.awt.Dimension(0, 1));
        JSeparator jSeparator1 = new JSeparator(JSeparator.HORIZONTAL);
        jSeparator1.setPreferredSize(new java.awt.Dimension(0, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = (properties.size()*2)-1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        
        jPanelLabels.add(jSeparator, gridBagConstraints);
        jPanelEditors.add(jSeparator1, gridBagConstraints);
        
        jPanelLabels.setMinimumSize(new java.awt.Dimension(30, 20*properties.size()));
        jPanelLabels.setPreferredSize(new java.awt.Dimension(30, 20*properties.size()));
        jPanelEditors.setMinimumSize(new java.awt.Dimension(30, 20*properties.size()));
        jPanelEditors.setPreferredSize(new java.awt.Dimension(30, 20*properties.size()));

        jPanelLabels.updateUI();
        jScrollPane1.updateUI();
    }

    public java.util.Vector getProperties() {
        return properties;
    }

    public void setProperties(java.util.Vector properties) {
        this.properties = properties;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanelSheet = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanelLabels = new javax.swing.JPanel();
        jPanelFillerLabels = new javax.swing.JPanel();
        jPanelEditors = new javax.swing.JPanel();
        jPanelFillerEditors = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setOpaque(false);
        jPanelSheet.setLayout(new java.awt.GridBagLayout());

        jPanelSheet.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(16, 2147483647));
        jPanel1.setMinimumSize(new java.awt.Dimension(16, 20));
        jPanel1.setPreferredSize(new java.awt.Dimension(16, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelSheet.add(jPanel1, gridBagConstraints);

        jSplitPane1.setBackground(new java.awt.Color(255, 255, 255));
        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerSize(1);
        jSplitPane1.setResizeWeight(0.5);
        jSplitPane1.setContinuousLayout(true);
        jPanelLabels.setLayout(new java.awt.GridBagLayout());

        jPanelLabels.setBackground(new java.awt.Color(255, 255, 255));
        jPanelLabels.setMinimumSize(new java.awt.Dimension(100, 10));
        jPanelLabels.setPreferredSize(new java.awt.Dimension(100, 10));
        jPanelFillerLabels.setMinimumSize(new java.awt.Dimension(10, 0));
        jPanelFillerLabels.setPreferredSize(new java.awt.Dimension(10, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 400;
        gridBagConstraints.weighty = 1.0;
        jPanelLabels.add(jPanelFillerLabels, gridBagConstraints);

        jSplitPane1.setLeftComponent(jPanelLabels);

        jPanelEditors.setLayout(new java.awt.GridBagLayout());

        jPanelEditors.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFillerEditors.setMinimumSize(new java.awt.Dimension(10, 0));
        jPanelFillerEditors.setPreferredSize(new java.awt.Dimension(10, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 400;
        gridBagConstraints.weighty = 1.0;
        jPanelEditors.add(jPanelFillerEditors, gridBagConstraints);

        jSplitPane1.setRightComponent(jPanelEditors);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelSheet.add(jSplitPane1, gridBagConstraints);

        jScrollPane1.setViewportView(jPanelSheet);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelEditors;
    private javax.swing.JPanel jPanelFillerEditors;
    private javax.swing.JPanel jPanelFillerLabels;
    private javax.swing.JPanel jPanelLabels;
    private javax.swing.JPanel jPanelSheet;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    // End of variables declaration//GEN-END:variables
    
}
