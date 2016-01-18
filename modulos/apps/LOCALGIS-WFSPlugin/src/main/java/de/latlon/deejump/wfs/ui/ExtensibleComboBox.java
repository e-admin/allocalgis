/**
 * ExtensibleComboBox.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * (c) 2007 by lat/lon GmbH
 *
 * @author Ghassan Hamammi (hamammi@latlon.de)
 *
 * This program is free software under the GPL (v2.0)
 * Read the file LICENSE.txt coming with the sources for details.
 */
package de.latlon.deejump.wfs.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

/**
 * @author hamammi
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java -
 * Code Style - Code Templates
 */
public class ExtensibleComboBox extends JComboBox {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param objects
     */
    public ExtensibleComboBox( Object[] objects ) {
        super( objects );
        createListener();
        setEditable( true );
    }

    /**
     * 
     */
    public void createListener() {
        addItemListener( new ItemListener() {
            public void itemStateChanged( ItemEvent e ) {
                if ( e.getStateChange() == ItemEvent.SELECTED ) {
                    JComboBox box = (JComboBox) e.getSource();
                    String newServer = (String) e.getItem();
                    int size = box.getModel().getSize();
                    List<Object> candidateGeoProps = new ArrayList<Object>( size );
                    for ( int i = 0; i < size; i++ ) {
                        candidateGeoProps.add( box.getModel().getElementAt( i ) );
                    }
                    if ( newServer != null && !candidateGeoProps.contains( newServer ) ) {
                        box.addItem( newServer );
                    }
                }
            }
        } );
    }

}
