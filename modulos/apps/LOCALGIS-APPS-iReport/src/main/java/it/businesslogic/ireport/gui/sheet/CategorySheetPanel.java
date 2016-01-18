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
 * CategorySheetPanel.java
 * 
 * Created on 4 ottobre 2004, 23.21
 *
 */

package it.businesslogic.ireport.gui.sheet;
import it.businesslogic.ireport.gui.JNumberComboBox;
import it.businesslogic.ireport.gui.JNumberField;
import it.businesslogic.ireport.gui.event.SheetPropertyValueChangedEvent;
import it.businesslogic.ireport.gui.event.SheetPropertyValueChangedListener;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;

import java.awt.Component;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

/**
 *
 * @author  Administrator
 */
public class CategorySheetPanel extends javax.swing.JPanel implements SheetPropertyValueChangedListener {
    
    public static final int VIEW_BY_CATEGORY = 1;
    public static final int VIEW_SORTED = 2;
    public static final int VIEW_UNSORTED = 3;
    
    public static final javax.swing.ImageIcon opened_icon = new javax.swing.ImageIcon( CategorySheetPanel.class.getResource("/it/businesslogic/ireport/icons/sheet/opened.png"));
    public static final javax.swing.ImageIcon closed_icon =new javax.swing.ImageIcon( CategorySheetPanel.class.getResource("/it/businesslogic/ireport/icons/sheet/closed.png"));
    
    
    /* Optimization variables.
     * All these variables are created to optimize the recreateSheet method
     */
    private java.awt.GridBagConstraints staticGridBagConstraints = null;
    private static final java.awt.Dimension standardComponentDimension = new java.awt.Dimension(50,0);
    private static final java.awt.Dimension dimension16x19 = new java.awt.Dimension(16,19);
    private static final java.awt.Dimension dimension0x1 = new java.awt.Dimension(0, 1);
    private static final javax.swing.border.EmptyBorder emptyBorder = new javax.swing.border.EmptyBorder(0,0,0,0);
    private static final java.awt.Insets inset0202 =  new java.awt.Insets(0, 2, 0, 2);
    private static final java.awt.Insets inset0000 =  new java.awt.Insets(0, 0, 0, 0);
    private static final java.awt.Dimension dimension41x18 = new java.awt.Dimension(41, 18);
    private static final java.awt.Color color_234_234_234 = new java.awt.Color(234,234,234);
    
    private static java.awt.GridBagConstraints spGridBagConstraints = new java.awt.GridBagConstraints();
        
    
    
    protected java.util.Vector properties;
    protected java.util.Vector categories;
    protected java.util.HashMap propCategories;
    protected java.util.Vector compressedCategories;
    private int viewMode = 1; 
    private int current_position = 0;
        
    private boolean showResetButton = true;
    
    /** Creates new form SheetPanel */
    public CategorySheetPanel() {
        initComponents();
        
        jPanel1.setBackground(color_234_234_234 );
        properties = new Vector();
        categories = new Vector();
        propCategories = new HashMap();
        compressedCategories = new Vector();
        jSplitPane1.setResizeWeight(0.5);
        
        jPanelLabels.setLayout(new java.awt.GridBagLayout());
        jPanel1.setLayout(new java.awt.GridBagLayout());
        jPanelEditors.setLayout(new java.awt.GridBagLayout());
        
        staticGridBagConstraints = new java.awt.GridBagConstraints();
        staticGridBagConstraints.gridx = 0;
        staticGridBagConstraints.gridy = 400;
        staticGridBagConstraints.weighty = 1.0;
        
        
        spGridBagConstraints.gridx = 0;
        spGridBagConstraints.gridy = 0;
        spGridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        spGridBagConstraints.weightx = 1.0;
        spGridBagConstraints.insets = inset0000;
        
        recreateSheet();
        
        I18n.addOnLanguageChangedListener(new LanguageChangedListener() {
            public void languageChanged(LanguageChangedEvent evt) {
                
                CategorySheetPanel.this.updateUI();
            
            }
        });
        
        jScrollPane1.getVerticalScrollBar().setUnitIncrement( 19 );
        applyI18n();
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
    
    public void setPropertyValue(String propertyName, Object value)
    {
        for (int i=0; i<getProperties().size(); ++i)
             {
                SheetProperty sp = (SheetProperty)getProperties().elementAt(i);
                if (sp.getKeyName().equals(propertyName) )
                {
                    //(javax.swing.JComponent)jPanelEditors.getComponent((2*i) +1 )
                    sp.setValue(value); // EditorValue( sp.getEditor() , value  );
                }
             }
    }
    
    public Object getPropertyValue(String propertyName)
    {
        for (int i=0; i<getProperties().size(); ++i)
             {
                SheetProperty sp = (SheetProperty)getProperties().elementAt(i);
                if (sp.getKeyName().equals(propertyName) )
                {
                    //(javax.swing.JComponent)jPanelEditors.getComponent((2*i) +1 )
                    return sp.getValue(); // getEditorValue( sp.getEditor() );
                }
             }
        return null;
    }
    
    public Object getSheetProperty(String propertyName)
    {
        for (int i=0; i<getProperties().size(); ++i)
             {
                SheetProperty sp = (SheetProperty)getProperties().elementAt(i);
                if (sp != null && sp.getKeyName().equals(propertyName) )
                {
                    return sp; // getEditorValue( sp.getEditor() );
                }
             }
        return null;
    }
    
    public Object getPropertyValue(int index)
    {
         SheetProperty sp = (SheetProperty)getProperties().elementAt(index);
         return sp.getValue();
    } 
    
    public void removeAllProperties()
    {
         for (int i=0; i<getProperties().size(); ++i)
         {
                SheetProperty sp = (SheetProperty)getProperties().elementAt(i);
                sp.removeSheetPropertyValueChangedListener(this);
         }
         
         getProperties().removeAllElements();
         categories.removeAllElements();
         propCategories.clear();
         //recreateSheet();
    }
    
    public void addSheetProperty(String category, SheetProperty sp)
    {
        properties.add(sp);
        
        Vector v_category = (Vector)propCategories.get(category);
        if (v_category == null)
        {
            v_category = new Vector();
            categories.add(category);
            propCategories.put(category, v_category);
        }
        v_category.add(sp);
        //recreateSheet();
    }
    
    
    public void recreateSheet()
    {
        current_position = 0;
        
        
        
        jPanelLabels.removeAll();
        jPanel1.removeAll();
        jPanelEditors.removeAll();
        
        
        
        //jPanelLabels.setLayout(new java.awt.GridBagLayout());
        //jPanel1.setLayout(new java.awt.GridBagLayout());
        //jPanelEditors.setLayout(new java.awt.GridBagLayout());
         
        jPanelEditors.setMinimumSize( standardComponentDimension );
        jPanelLabels.setMinimumSize( standardComponentDimension );
        
        if (getViewMode() == VIEW_BY_CATEGORY)
        {
            for (int i=0; i<categories.size(); ++i)
            {
                // Add the category....
                addCategory( ""+categories.elementAt(i) );
            }
        }
        else if (getViewMode() == VIEW_UNSORTED )
        {
            addItems( properties );
        }
        else
        {
            Vector items = getSortedProperties( properties );
            addItems( items );
        }
        
       
        jPanelSheet.setMinimumSize(new java.awt.Dimension(16,19*current_position));
        jPanelSheet.setPreferredSize(new java.awt.Dimension(16,19*current_position));
        
        jPanelEditors.add(jPanelFillerEditors, staticGridBagConstraints);
        jPanelLabels.add(jPanelFillerLabels, staticGridBagConstraints);
        jPanel1.add(jPanelFiller, staticGridBagConstraints);
        
       
        jPanelLabels.updateUI();
        jPanelEditors.updateUI();
        jScrollPane1.updateUI();
        
        
    }
    
    
    protected void addItem( SheetProperty sp )
    {
        boolean setting = sp.isSetting();
        sp.setSetting(true);
        
        sp.addSheetPropertyValueChangedListener( this );
        JLabel jLabel = new JLabel(sp.getName());
        //jLabel.setText();
        //jLabel.setFont( UIManager..getDefaults().getFont("Label.font"));
        //jLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, jLabel.getFont().getSize() ));
        
        if (sp.getType() != sp.CATEGORY_LABEL)
        {
            jLabel.setToolTipText(sp.getName());
        }
        else
        {
            jLabel.setText("<html><table><tr><td nowrap><b>" +  sp.getName() + "</b></td></tr></table></html>");
            
            //java.awt.Font oldFont = jLabel.getFont();
            //jLabel.setFont(new java.awt.Font( oldFont.getName(), java.awt.Font.BOLD, oldFont.getSize()));
        } 
        
        jLabel.setMaximumSize(dimension41x18);
        jLabel.setMinimumSize(dimension41x18);
        jLabel.setPreferredSize(dimension41x18);
        
        spGridBagConstraints.gridx = 0;
        spGridBagConstraints.gridy = (current_position)*2;
        spGridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        spGridBagConstraints.weightx = 1.0;
        spGridBagConstraints.insets = inset0000;
        
        if (sp.getType() != sp.CATEGORY_LABEL)
        {
            spGridBagConstraints.insets = inset0202;
        }
        else
        {
            spGridBagConstraints.insets = inset0000;
            jLabel.setBackground(color_234_234_234 );
            jLabel.setOpaque(true);
        }
        
        
        jPanelLabels.add(jLabel, spGridBagConstraints);
        
 
        /* Set pulsante category... */
        //spGridBagConstraints.gridx = 0;
        //spGridBagConstraints.gridy = (current_position)*2;
        spGridBagConstraints.insets = inset0000;
        
        if (sp.getType() == sp.CATEGORY_LABEL)
        {
            JLabel b = new JLabel( (!compressedCategories.contains( sp.getName()) ) ? opened_icon : closed_icon);
            b.setName(sp.getName());
            //jLabel.addMouseListener(  );
            b.setMinimumSize(dimension16x19);
            b.setMaximumSize(dimension16x19);
            b.setPreferredSize(dimension16x19);
            b.setBorder(emptyBorder);
            b.setOpaque(false);
            jPanel1.add(b, spGridBagConstraints);
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    categoryClicked(evt);
                }
            });
        }
        else
        {
            JPanel p = new JPanel();
            p.setMinimumSize(dimension16x19);
            p.setMaximumSize(dimension16x19);
            p.setPreferredSize(dimension16x19);
            p.setBorder(emptyBorder);
            p.setOpaque(false);
            jPanel1.add(p, spGridBagConstraints);
            
        }
        
        spGridBagConstraints.insets = inset0202;
        
        /******** Set editor *******/
        javax.swing.JComponent editor = sp.getEditor();
        
        if (sp.isReadOnly())
        {
            editor.setEnabled( false );
        }
        else
        {
            editor.setEnabled( true );
        }
        
        if (sp.getType() != sp.CATEGORY_LABEL)
        {
            editor.setOpaque(false);
            editor.setBackground( java.awt.Color.WHITE);
            spGridBagConstraints.gridwidth = (!sp.isShowResetButton()) ? 2 : 1;
        }
        else
        {
            spGridBagConstraints.insets = inset0000;
            spGridBagConstraints.gridwidth = 2;
            editor.setBackground(color_234_234_234 );
            editor.setOpaque(true);
        }
        
        editor.setMaximumSize(dimension41x18);
        editor.setMinimumSize(dimension41x18);
        editor.setPreferredSize(dimension41x18);
        
        if (editor instanceof JPanel || sp.getType() != sp.CATEGORY_LABEL)
        {
            spGridBagConstraints.insets = inset0000;
        }
        
        jPanelEditors.add(editor, spGridBagConstraints);
        
        spGridBagConstraints.gridwidth=1;
        
        if (sp.getType() != sp.CATEGORY_LABEL && sp.isShowResetButton())
        {
            spGridBagConstraints.gridx = 1;
            spGridBagConstraints.gridy = (current_position)*2;
            spGridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            spGridBagConstraints.weightx = 0;
            spGridBagConstraints.insets = inset0000;
            ResetButton resetButton = new ResetButton(sp);
            jPanelEditors.add(resetButton, spGridBagConstraints);
        }
        
        JSeparator jSeparator1 = new JSeparator(JSeparator.HORIZONTAL);
        jSeparator1.setPreferredSize(dimension0x1);
        //gridBagConstraints = new java.awt.GridBagConstraints();
        spGridBagConstraints.insets = inset0000;
        spGridBagConstraints.gridx = 0;
        spGridBagConstraints.gridwidth = 2;
        spGridBagConstraints.weightx = 0;
        spGridBagConstraints.gridy = (current_position*2)+1;
        spGridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        
        jPanelEditors.add(jSeparator1, spGridBagConstraints);
        
        JSeparator jSeparator = new JSeparator(JSeparator.HORIZONTAL);
        jSeparator.setPreferredSize(dimension0x1);
        jPanelLabels.add(jSeparator, spGridBagConstraints);
        
        java.awt.Dimension tmpdim = new java.awt.Dimension(30, 20*properties.size());
        jPanelLabels.setMinimumSize(tmpdim);
        jPanelLabels.setPreferredSize(tmpdim);
        jPanelEditors.setMinimumSize(tmpdim);
        jPanelEditors.setPreferredSize(tmpdim);

        sp.setLabelComponent(jLabel);
        
        sp.updateLabel();
        
        //jPanelLabels.updateUI();
        //jPanelEditors.updateUI();
        //jScrollPane1.updateUI();
        
        current_position++;
        
        sp.setSetting(setting);
    }
    
    protected Vector getSortedProperties(Vector props)
    {
        SheetProperty[] names = new SheetProperty[props.size()];
        HashMap unsorted_hm = new HashMap();
        for (int i=0; i< props.size(); ++i)
        {
            SheetProperty sp = (SheetProperty)props.elementAt(i);
            names[i] = sp;
        }
        
        java.util.Arrays.sort(names, new Comparator()
            {
                public int compare(Object o1,Object o2 )
                {
                    return ((SheetProperty)o1).getName().compareTo( ((SheetProperty)o2).getName() );
                }
            });
        Vector v = new Vector();
        for (int i=0; i< names.length; ++i)
        {
            v.add(i,names[i]);
        }
        
        return v;
    }
    
    protected void addItems( Vector sheetProperties)
    {
        for (int i=0; i< sheetProperties.size(); ++i)
        {
            addItem( (SheetProperty)sheetProperties.elementAt(i));
        }
    }
    
    protected void addCategory(String name)
    {
        SheetProperty sp = new SheetProperty(name,name,SheetProperty.CATEGORY_LABEL);
        addItem( sp );
        if (!compressedCategories.contains( sp.getName()) )
        {
            addItems( (Vector)propCategories.get( name )  );
        }
    }
    
    public java.util.Vector getProperties() {
        return properties;
    }

    public void setProperties(java.util.Vector properties) {
        this.properties = properties;
    }

    public int getViewMode() {
        return viewMode;
    }

    public void setViewMode(int viewMode) {
        this.viewMode = viewMode;
        recreateSheet();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroupToolbar = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanelSheet = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanelFiller = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanelLabels = new javax.swing.JPanel();
        jPanelFillerLabels = new javax.swing.JPanel();
        jPanelEditors = new javax.swing.JPanel();
        jPanelFillerEditors = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButtonOrderByCategory = new javax.swing.JButton();
        jButtonOrderByAlpha = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(22, 200));
        setPreferredSize(new java.awt.Dimension(128, 200));
        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(22, 0));
        jScrollPane1.setOpaque(false);
        jPanelSheet.setLayout(new java.awt.GridBagLayout());

        jPanelSheet.setBackground(new java.awt.Color(255, 255, 255));
        jPanelSheet.setMinimumSize(new java.awt.Dimension(126, 0));
        jPanelSheet.setPreferredSize(new java.awt.Dimension(126, 0));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(16, 2147483647));
        jPanel1.setMinimumSize(new java.awt.Dimension(16, 20));
        jPanel1.setPreferredSize(new java.awt.Dimension(16, 0));
        jPanelFiller.setMinimumSize(new java.awt.Dimension(10, 0));
        jPanelFiller.setPreferredSize(new java.awt.Dimension(10, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 400;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanelFiller, gridBagConstraints);

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
        jSplitPane1.setOneTouchExpandable(true);
        jSplitPane1.setOpaque(false);
        jSplitPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jSplitPane1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jSplitPane1MouseExited(evt);
            }
        });

        jPanelLabels.setLayout(new java.awt.GridBagLayout());

        jPanelLabels.setBackground(new java.awt.Color(255, 255, 255));
        jPanelLabels.setMinimumSize(new java.awt.Dimension(70, 10));
        jPanelLabels.setPreferredSize(new java.awt.Dimension(70, 10));
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

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setMinimumSize(new java.awt.Dimension(10, 20));
        jPanel2.setPreferredSize(new java.awt.Dimension(10, 24));
        jButtonOrderByCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/sheet/tree.png")));
        jButtonOrderByCategory.setBorderPainted(false);
        buttonGroupToolbar.add(jButtonOrderByCategory);
        jButtonOrderByCategory.setContentAreaFilled(false);
        jButtonOrderByCategory.setFocusPainted(false);
        jButtonOrderByCategory.setMinimumSize(new java.awt.Dimension(25, 25));
        jButtonOrderByCategory.setPreferredSize(new java.awt.Dimension(25, 25));
        jButtonOrderByCategory.setSelected(true);
        jButtonOrderByCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOrderByCategoryActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        jPanel2.add(jButtonOrderByCategory, gridBagConstraints);

        jButtonOrderByAlpha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/sheet/az.png")));
        jButtonOrderByAlpha.setBorderPainted(false);
        buttonGroupToolbar.add(jButtonOrderByAlpha);
        jButtonOrderByAlpha.setContentAreaFilled(false);
        jButtonOrderByAlpha.setFocusPainted(false);
        jButtonOrderByAlpha.setMinimumSize(new java.awt.Dimension(25, 25));
        jButtonOrderByAlpha.setPreferredSize(new java.awt.Dimension(25, 25));
        jButtonOrderByAlpha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOrderByAlphaActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        jPanel2.add(jButtonOrderByAlpha, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jPanel3, gridBagConstraints);

        add(jPanel2, java.awt.BorderLayout.NORTH);

    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOrderByAlphaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOrderByAlphaActionPerformed
       this.setViewMode( VIEW_SORTED );
    }//GEN-LAST:event_jButtonOrderByAlphaActionPerformed

    private void jButtonOrderByCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOrderByCategoryActionPerformed
        this.setViewMode( VIEW_BY_CATEGORY );
    }//GEN-LAST:event_jButtonOrderByCategoryActionPerformed

    private void jSplitPane1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSplitPane1MouseExited
        //System.out.println("exited");
        //this.setCursor( new java.awt.Cursor(java.awt.Cursor.W_RESIZE_CURSOR));
        
        
    }//GEN-LAST:event_jSplitPane1MouseExited

    private void jSplitPane1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSplitPane1MouseEntered
        
        //System.out.println("entered");
        //this.setCursor( new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        
        
    }//GEN-LAST:event_jSplitPane1MouseEntered
 
    public void categoryClicked(java.awt.event.MouseEvent evt){
        
        String category = "";
        try {
            JLabel jlabel = (JLabel)(evt.getSource());
            if (compressedCategories.contains( jlabel.getName()) )
            {
                jlabel.setIcon(opened_icon);
                compressedCategories.removeElement(jlabel.getName());
                recreateSheet();
            }
            else
            {
                jlabel.setIcon(closed_icon);
                
                compressedCategories.add(jlabel.getName());
                recreateSheet();
            }
        
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Collapse the specified category.
     *
     */
    public void collapseCategory(String category)
    {
        if (!compressedCategories.contains(category))
        {
            compressedCategories.add(category);
            recreateSheet();
            return;
        }
    }

    /**
     * Explode the specified category.
     *
     */
    public void explodeCategory(String category)
    {
        
        if (compressedCategories.contains(category))
        {
            compressedCategories.removeElement(category);
            recreateSheet();
            return;
        }
    }
    
    /**
     * Registers SheetPropertyValueChangedListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addSheetPropertyValueChangedListener(it.businesslogic.ireport.gui.event.SheetPropertyValueChangedListener listener) {

        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add (it.businesslogic.ireport.gui.event.SheetPropertyValueChangedListener.class, listener);
    }

    /**
     * Removes SheetPropertyValueChangedListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeSheetPropertyValueChangedListener(it.businesslogic.ireport.gui.event.SheetPropertyValueChangedListener listener) {

        listenerList.remove (it.businesslogic.ireport.gui.event.SheetPropertyValueChangedListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event The event to be fired
     */
    private void fireSheetPropertyValueChangedListenerSheetPropertyValueChanged(it.businesslogic.ireport.gui.event.SheetPropertyValueChangedEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.SheetPropertyValueChangedListener.class) {
                ((it.businesslogic.ireport.gui.event.SheetPropertyValueChangedListener)listeners[i+1]).sheetPropertyValueChanged (event);
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupToolbar;
    private javax.swing.JButton jButtonOrderByAlpha;
    private javax.swing.JButton jButtonOrderByCategory;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelEditors;
    private javax.swing.JPanel jPanelFiller;
    private javax.swing.JPanel jPanelFillerEditors;
    private javax.swing.JPanel jPanelFillerLabels;
    private javax.swing.JPanel jPanelLabels;
    private javax.swing.JPanel jPanelSheet;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    // End of variables declaration//GEN-END:variables
 
    public void sheetPropertyValueChanged(SheetPropertyValueChangedEvent evt)
    {
        evt.setSource( this );
        fireSheetPropertyValueChangedListenerSheetPropertyValueChanged(evt);
    }

    /**
     * Utility field used by event firing mechanism.
     */
    private javax.swing.event.EventListenerList listenerList =  null;

    public boolean isShowResetButton() {
        return showResetButton;
    }

    /**
     * This method does not perform a recreateSheet...
     *
     */
    public void setShowResetButton(boolean showResetButton) {
        this.showResetButton = showResetButton;
        for (int i=0; i<getProperties().size(); ++i)
         {
            SheetProperty sp = (SheetProperty)getProperties().elementAt(i);
            sp.setShowResetButton( showResetButton );
         }
    }

    public void setResizeWeight(double resizeWeight) {
        jSplitPane1.setResizeWeight(resizeWeight);
    }

    public void updateUI() {
        super.updateUI();
        if (jPanelLabels != null && this.getProperties().size() > 0)
        {
            recreateSheet();
        }
    }
    
    public void applyI18n(){
                // Start autogenerated code ----------------------
                // End autogenerated code ----------------------
        DIFFERENT_VALUES = it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.differentValues","<different values>");
    }
    
   public void setPropertyLabelError(String propertyName, String error) {
        final SheetProperty sp = (SheetProperty)this.getSheetProperty(propertyName);
        if (sp == null) return;
        sp.setLabelError(error);
        sp.updateLabel();
        // try to ensure visibility...
        // 1. calc label position...
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ensureVisible(sp);
            }
        });
    }
   
   public void ensureVisible(String propertyName)
   {
       ensureVisible((SheetProperty)this.getSheetProperty(propertyName));
   }
   
   public void ensureVisible(SheetProperty sp)
   {
       if (sp == null) return;
       Component[] components = jPanelLabels.getComponents();
       
        int y = sp.getLabelComponent().getY();
        int h = sp.getLabelComponent().getHeight();
        if (h > jScrollPane1.getVisibleRect().height) h = jScrollPane1.getVisibleRect().height;
        
        int visible_h = jScrollPane1.getVisibleRect().height;
        int scroll_val = jScrollPane1.getVerticalScrollBar().getValue();
        int max_y_visible_now = scroll_val + visible_h;
        
        if (max_y_visible_now < y)
        {
            jScrollPane1.getVerticalScrollBar().setValue( y + h - visible_h);
        }
        else if (scroll_val > y)
        {
            jScrollPane1.getVerticalScrollBar().setValue( y );
        }
        else if (visible_h > h && max_y_visible_now < y + h)
        {
            jScrollPane1.getVerticalScrollBar().setValue( y + h - visible_h);
        }
   
   }
   
   
   
    
    protected boolean setComboBox( boolean firstTime, Object value, javax.swing.JComboBox comboField ) {
        if (( !firstTime ) && (!(comboField.getSelectedItem() == value))) {

            comboField.insertItemAt(DIFFERENT_VALUES,0);
            comboField.setSelectedIndex(0);
            return false;
        }
        else {
            try {
                comboField.setSelectedItem( value );
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    protected boolean setTextArea( boolean firstTime, String value, SheetProperty textProperty ) {
        
        if ((!firstTime) && (!(textProperty.getValue().equals(value) ))) {
            
            JComponent editor = textProperty.getEditor();
            if (editor instanceof ExpressionSheetPropertyComponent)
            {
                ((ExpressionSheetPropertyComponent)editor).setExpression("");
               ((ExpressionSheetPropertyComponent)editor).setText(DIFFERENT_VALUES);
            }
            return false;
        }
        else {
            try {
                textProperty.setValue(value);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    protected boolean setTextPattern( boolean firstTime, Object value, SheetProperty textProperty ) {
        
        if ((!firstTime) && (!((textProperty.getValue()+"").equals(value) ))) {
            
            JComponent editor = textProperty.getEditor();
            if (editor instanceof PatternSheetPropertyComponent)
            {
                ((PatternSheetPropertyComponent)editor).setExpression("");
               ((PatternSheetPropertyComponent)editor).setText(DIFFERENT_VALUES);
            }
            return false;
        }
        else {
            try {
                textProperty.setValue(value);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }
   
  
 /**
  * Set the value for a tagged combobox. 
  */
 protected boolean setTagComboBox( boolean firstTime, Object value, SheetProperty sp ) {
        
      if (firstTime)
      {
        removeNullItem(sp);
        sp.setValue( value );
        return true;  
      }
      else
      {
          Object s = sp.getValue();
          if (s == null && value == null) return true;
          if (s!= null && s.equals(value)) return true;;
          
          sp.setSetting(true);
          
          JComboBox combobox = (JComboBox)sp.getEditor();
          if (combobox.getItemCount() > 0)
          {
             Object obj = combobox.getItemAt(0);
             sp.setValue(null);
             if ((obj+"").equals(DIFFERENT_VALUES))
             {
                // null item already present...
             }
             else
             {
                 ((JComboBox)sp.getEditor()).insertItemAt(new Tag(null,DIFFERENT_VALUES),0);
             }
          }
          ((JComboBox)sp.getEditor()).setSelectedIndex(0);
          sp.setSetting(false);
          return false;
      }     
    }
    
    protected boolean setElementNumber( boolean firstTime, double value, JNumberField numberField ) {
        
        if (numberField == null) return false;
        if (( ! firstTime ) && (!(numberField.getValue() == value))) {
            numberField.setText("");
            return false;
        }
        else {
            try {
                numberField.setValue( value );
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
         
        return true;
    }
    
    protected boolean setElementNumber( boolean firstTime, int defaultValue, Object value, NumberComboBoxSheetProperty sp) {
        
        if (sp == null) return false;
        if ( ( ! firstTime ) && (!((sp.getValue()+"").equals(""+value))) )  {
            
            boolean setting = sp.isSetting();
            sp.setSetting(true);
            sp.setValue(null);
            sp.setSetting(setting);
            //System.out.println("Setting value of editor to blank ");
            sp.setEditorValue( sp.getEditor(), "");
            return false;
        }
        else {
            try {
                 boolean setting = sp.isSetting();
                 sp.setSetting(true);
                 sp.setValue(value);
                 if (value == null)
                 {
                     if (!firstTime && !(((JNumberComboBox)sp.getEditor()).getSelectedItem()+"").equals(""+defaultValue))
                     {
                         sp.setEditorValue( sp.getEditor(), "");
                     }
                     else
                     {
                        sp.setEditorValue( sp.getEditor(), new Integer( defaultValue));
                    }
                 }
                 sp.setSetting(setting);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
         
        return true;
    }
    
    protected boolean setGenericSheetProperty( boolean firstTime, Object value, SheetProperty sp) {
        if (firstTime )
        {    
            try {
                sp.setValue( value );
            } catch (Exception ex){
                ex.printStackTrace();
            }
            return true;
        } else
        {
            Object value2 = sp.getValue();
            if (value2 == value)
            {
                return true;
            }
            if (value2 != null && (""+value2).equals( value +""))
            {
                return true;
            }
            
            sp.setValue(null);
        }
       
        return false;
    }
    
    
    protected boolean setColorProperty( boolean firstTime, java.awt.Color color, SheetProperty sp) {
        
        
        if (firstTime )
        {    
            try {
                sp.setValue( color );
            } catch (Exception ex){
                ex.printStackTrace();
            }
            return true;
        } else
        {
            java.awt.Color c1 = (java.awt.Color)sp.getValue();
            if (c1 == color)
            {
                return true;
            }
            if (c1 != null && color != null && c1.getRGB() == color.getRGB())
            {
                return true;
            }
            
            sp.setValue(null);
        }
       
        return false;
    }
            
    protected boolean setCheckBox( boolean firstTime, boolean value, boolean isNull, SheetProperty sp ) {
        if (firstTime )
        {
            try {
                if (isNull)
                {
                    sp.setValue( null );
                    sp.setSetting(true);
                    ((JCheckBox)sp.getEditor()).setSelected(value);
                    sp.setSetting(false);
                }
                else
                {
                    sp.setValue(""+value);
                }
                
            } catch (Exception ex){
                ex.printStackTrace();
            }
            return true;
        }
        else
        {
             if (sp.getValue() == null && isNull) return true;
             if (sp.getValue() != null && sp.getValue().equals("" + value)  ) return true;
            
             sp.setValue( null );
             return false;
        }
        
    }
    
     /*
      * This method remove the first entry in comboboxes if the first entry
      * is something like "<Different values>"....
      *
      */
     public void removeNullItem(SheetProperty comboProperty)
     {
         if (comboProperty == null) return;
          if (comboProperty.getEditor() instanceof JComboBox)
          {
             JComboBox combobox = (JComboBox)comboProperty.getEditor();
             if (combobox.getSelectedIndex() > 0)
             {
                Object obj = combobox.getItemAt(0);
                if ((obj+"").equals(DIFFERENT_VALUES))
                {
                    combobox.removeItemAt(0);
                }
             }
          }
     }
     
     public String DIFFERENT_VALUES = "<different values>";
     
     /*
      * This method says if the selected value is actually something like "<Different values>"....
      *
      */
     public boolean isNullItem(SheetProperty comboProperty)
     {
         if (comboProperty == null) return false;
          if (comboProperty.getEditor() instanceof JComboBox)
          {
             JComboBox combobox = (JComboBox)comboProperty.getEditor();
             if (combobox.getSelectedIndex() == 0)
             {
                Object obj = combobox.getItemAt(0);
                if ((obj+"").equals(DIFFERENT_VALUES))
                {
                    return true;
                }
             }
          }
         return false;
     }
}
