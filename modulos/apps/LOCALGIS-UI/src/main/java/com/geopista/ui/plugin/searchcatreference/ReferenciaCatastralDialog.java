/**
 * ReferenciaCatastralDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.searchcatreference;

import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Collection;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;


public class ReferenciaCatastralDialog extends JDialog
{

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;
    private ReferenciaCatastralPanel  referenciaCatastralPanel = null;
    protected OKCancelPanel oKCancelPanel = null;
    protected boolean okPressed = false;

    /**
     * This is the default constructor
     */
    public ReferenciaCatastralDialog()
        {
            super();
            initialize();
        }

    
    public ReferenciaCatastralDialog(JFrame owner, boolean modal)
    {
        super(owner,modal);
        initialize();
    }
    
    public ReferenciaCatastralDialog(JFrame owner, boolean modal, Collection layers)
    {
        super(owner,modal);
        initialize();
        getReferenciaCatastralPanel().addLayers(layers);
    }
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        this.setSize(439, 147);
        this.setContentPane(getJContentPane());
        this.getContentPane().add(getReferenciaCatastralPanel());
        this.setTitle(I18N.get(SearchReferenciaCatastralPlugIn.SearchReferenciaCatastralI18N,"ReferenciaCatastralDialog.BusquedaReferenciaCatastral"));
        
        this.addComponentListener(new ComponentListener()
        {
    
            public void componentHidden(ComponentEvent arg0)
            {
            // TODO Auto-generated method stub
    
            }
    
            public void componentMoved(ComponentEvent arg0)
            {
            // TODO Auto-generated method stub
    
            }
    
            public void componentResized(ComponentEvent arg0)
            {
            // TODO Auto-generated method stub
    
            }
    
            public void componentShown(ComponentEvent arg0)
            {

            GUIUtil.centreOnScreen(ReferenciaCatastralDialog.this);
    
            }
    
        });
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane()
    {
        if (jContentPane == null)
        {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getReferenciaCatastralPanel(), null);
            jContentPane.add(getOKCancelPanel(), null);
        }
        return jContentPane;
    }

    /**
     * @return the referenciaCatastralPanel
     */
    public ReferenciaCatastralPanel getReferenciaCatastralPanel()
    {
        if (referenciaCatastralPanel == null)
        {
            referenciaCatastralPanel = new ReferenciaCatastralPanel();
            referenciaCatastralPanel.setBounds(new Rectangle(4, 1, 424, 67));
            
        }
        return referenciaCatastralPanel;
    }

    /**
     * @return the oKCancelPanel
     */
    public OKCancelPanel getOKCancelPanel()
    {
        if (oKCancelPanel == null)
        {
            oKCancelPanel = new OKCancelPanel();
            oKCancelPanel.setBounds(new Rectangle(105, 74, 206, 36));
            oKCancelPanel.addActionListener(new java.awt.event.ActionListener() { 
                public void actionPerformed(java.awt.event.ActionEvent e) {    
                    
                    if (oKCancelPanel.wasOKPressed()) {
                        
                            boolean validate = getReferenciaCatastralPanel().validateInput();
                            if(!validate) return; 
                        
                            Object selectItem = getReferenciaCatastralPanel().getRefLayersJComboBox().getSelectedItem();
                            AppContext.getApplicationContext().getBlackboard().put(ReferenciaCatastralPanel.ReferenciaCatastralSelectedLayer,selectItem);
                            setOkPressed(true);
                            setVisible(false);
                    }
                    setVisible(false);
                }
            });
        

        
        }
        return oKCancelPanel;
    }

    /**
     * @return the okPressed
     */
    public boolean wasOkPressed()
    {
        return okPressed;
    }

    /**
     * @param okPressed the okPressed to set
     */
    protected void setOkPressed(boolean okPressed)
    {
        this.okPressed = okPressed;
    }
    
    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */


}  //  @jve:decl-index=0:visual-constraint="10,10"
