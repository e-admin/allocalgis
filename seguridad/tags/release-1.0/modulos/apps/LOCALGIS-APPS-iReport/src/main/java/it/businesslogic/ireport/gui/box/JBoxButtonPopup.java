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
 * JBoxButtonPopup.java
 * 
 * Created on May 18, 2006, 12:00 PM
 *
 */

package it.businesslogic.ireport.gui.box;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 *
 * @author gtoffoli
 */
public class JBoxButtonPopup extends JPopupMenu {
    
    private Component parent;
    private Component component;
    private boolean installedTopListener = false;
    
    /** Creates a new instance of JBoxButtonPopup */
    public JBoxButtonPopup(Component parent, Component c) {
        super();
        this.setParentComponent(parent);
        this.setComponent(c);
        super.add(getComponent());

    }
    
     public void show()
    {
        if (!installedTopListener)
        {
              Container rootContainer = (Container)SwingUtilities.getRootPane(getParentComponent());
              rootContainer.addComponentListener(new ComponentListener() {
                  public void componentHidden(ComponentEvent e) {
                      setVisible(false);
                  }
                  public void componentMoved(ComponentEvent e) {
                      setVisible(false);
                  }
                  public void componentResized(ComponentEvent e) {
                      setVisible(false);
                  }
                  public void componentShown(ComponentEvent e) {
                      setVisible(false);
                  }
              } );

        }
        super.setPopupSize(getComponent().getPreferredSize());
        // location specified is relative to comboBox
        super.show(getParentComponent(), 0, getParentComponent().getHeight());
    }

    public Component getParentComponent() {
        return parent;
    }

    public void setParentComponent(Component parent) {
        this.parent = parent;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

}
