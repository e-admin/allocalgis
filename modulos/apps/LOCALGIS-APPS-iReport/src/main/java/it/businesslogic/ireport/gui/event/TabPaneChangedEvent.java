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
 * TabPaneChangedEvent.java
 * 
 * Created on 10 febbraio 2003, 2.04
 *
 */

package it.businesslogic.ireport.gui.event;

import java.awt.Component;

/**
 * This event is fired when a group is added or when a cell is resized or when the crosstab element changes
 * his dimension...
 * @author  Administrator
 */
public class TabPaneChangedEvent {
       
    public static final int MINIMIZED = 0;
    public static final int CLOSED = 1;
    
    private int tabIndex = -1;
    private Component tabComponent = null;
    private int operation = -1;
    
    /** Creates a new instance of ValueChangedEvent */
    public TabPaneChangedEvent(int  operation, Component tabComponent, int tabIndex) {
        this.setOperation(operation);
        this.setTabComponent(tabComponent);
        this.setTabIndex(tabIndex);
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public Component getTabComponent() {
        return tabComponent;
    }

    public void setTabComponent(Component tabComponent) {
        this.tabComponent = tabComponent;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }
    
}
