/**
 * ActionEventFirer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Keeps registry of action listeners, triggers each of them on demand 
 * with a given ActionEvent.
 */


public class ActionEventFirer {
    private ArrayList actionListeners = new ArrayList();

    public void add(ActionListener listener) {
        actionListeners.add(listener);
    }

    public void remove(ActionListener listener) {
        actionListeners.remove(listener);
    }

    public void fire(Object source, int id, String command) {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener listener = (ActionListener) i.next();
            listener.actionPerformed(new ActionEvent(source, id, command));
        }
    }
}
