/**
 * WFSPanelButtons.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
//$Header: /usr/cvslocalgis/.CVSROOT/localgismodelo/plugins/WFSPlugin-src-1.1.0/src/de/latlon/deejump/wfs/ui/WFSPanelButtons.java,v 1.1 2012/09/12 10:52:04 satec Exp $
/*----------------    FILE HEADER  ------------------------------------------
 This file is part of deegree.
 Copyright (C) 2001-2006 by:
 Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 Contact:

 Andreas Poth
 lat/lon GmbH
 Aennchenstr. 19
 53177 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 Prof. Dr. Klaus Greve
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de

 ---------------------------------------------------------------------------*/

package de.latlon.deejump.wfs.ui;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.latlon.deejump.wfs.i18n.I18N;

/**
 * Includes the OK, cancel, Options etc. buttons.
 *
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * @author last edited by: $Author: satec $
 *
 * @version $Revision: 1.1 $, $Date: 2012/09/12 10:52:04 $
 */
class WFSPanelButtons extends JPanel {
        
    private static final long serialVersionUID = 5027400556779030070L;

    JButton okButton;
    
    JButton cancelButton;
    
    Window parentWindow;
    
    WFSPanel wfsPanel;
    
    WFSPanelButtons( Window parent, WFSPanel wfsPanel ){
        super();
        this.parentWindow = parent;
        this.wfsPanel = wfsPanel;
        initUI();
    }

    private void initUI() {
        Box box = Box.createHorizontalBox();
        box.setBorder( BorderFactory.createEmptyBorder( 20, 5, 10, 5 ));
        
        okButton = new JButton( I18N.getString( "OK" ) );
        
        okButton.setEnabled( true );
        okButton.setFocusable( true );

        cancelButton = new JButton( I18N.getString( "CANCEL" ) );
        cancelButton.setAlignmentX( 0.5f );
        final String optionsTxt = I18N.getString( "WFSPanel.options" );
        JButton optionsbutton = new JButton(optionsTxt);
        optionsbutton.addActionListener( new ActionListener(){
            public void actionPerformed( ActionEvent e ) {
                WFSOptions opts = wfsPanel.getOptions();
                JOptionPane.showMessageDialog(  parentWindow,
                                                new WFSOptionsPanel2(opts), 
                                                optionsTxt,
                                                -1);
            }
        });
        
        final String showAdvanced =  I18N.getString( "WFSPanel.showAdvanced" );
        final String hideAdvanced = I18N.getString( "WFSPanel.hideAdvanced" );

        JButton extrasButton = new JButton( showAdvanced );
        extrasButton.setBounds( 260, 20, 80, 20 );
        extrasButton.setAlignmentX( 0.5f );
        extrasButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                String actComm = e.getActionCommand();
                JButton b = (JButton) e.getSource();
                if ( showAdvanced.equals( actComm ) ) {
                    wfsPanel.setTabsVisible( true );
                    parentWindow.setSize( 500, 900 );
                    b.setText( hideAdvanced );
                    b.setActionCommand( hideAdvanced );
                } else {
                    wfsPanel.setTabsVisible( false );
                    parentWindow.setSize( 500, 300 );
                    b.setText( showAdvanced );
                    b.setActionCommand( showAdvanced );

                }
            }
        } );

        Dimension d = new Dimension(15,10);
        box.add( optionsbutton );
        box.add( Box.createRigidArea( d ));
        box.add( extrasButton );
        box.add( Box.createRigidArea( d ));        
        box.add( okButton );
        box.add( Box.createRigidArea( d ));
        box.add( cancelButton );
        add( box );
    }

}
