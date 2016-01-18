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
 * PanelView.java
 * 
 * Created on January 25, 2006, 5:24 PM
 *
 */

package it.businesslogic.ireport.gui.docking;

import java.awt.Component;

/**
 *
 * @author gtoffoli
 */
public class PanelView {
    
    private static int ID = 0;
    
    private String name = "";
    private Component component = null;
    private int position = 0;
    private boolean closable = false;
    private boolean minimized = false;
    private int id = 0;
    
    /**
     * Used only for drag'n'drop operations...
     */
    private DockingContainer dockingContainer = null;
    
    /** Creates a new instance of PanelView */
    public PanelView(String name, Component component, int position, boolean closable) {
    
            this.component = component;
            this.name = name;
            this.position = position;
            this.closable = closable;
            setId(ID++);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public DockingContainer getDockingContainer() {
        return dockingContainer;
    }

    public void setDockingContainer(DockingContainer dockingContainer) {
        this.dockingContainer = dockingContainer;
    }

    public boolean isClosable() {
        return closable;
    }

    public void setClosable(boolean closable) {
        this.closable = closable;
    }

    public boolean isMinimized() {
        return minimized;
    }

    public void setMinimized(boolean minimized) {
        this.minimized = minimized;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
