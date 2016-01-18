/**
 * OKCancelListener.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 20.06.2005
 *
 * CVS information:
 *  $Author: miriamperez $
 *  $Date: 2009/07/03 12:31:46 $
 *  $ID$
 *  $Revision: 1.1 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/OKCancelListener.java,v $
 *  $Log: OKCancelListener.java,v $
 *  Revision 1.1  2009/07/03 12:31:46  miriamperez
 *  Rama única LocalGISDOS
 *
 *  Revision 1.1  2006/05/18 08:10:15  jpcastro
 *  Pirol RasterImage
 *
 *  Revision 1.4  2006/01/09 12:55:24  orahn
 *  korrektere Fehlerausgabe
 *
 *  Revision 1.3  2005/07/06 13:02:46  orahn
 *  Code bereinigen, Performance erhöhen(?)
 *
 *  Revision 1.2  2005/06/30 10:43:30  orahn
 *  Standard ok/Cancel-Listener erweitert um die Möglichkeit, den OK Button ausser Funktion zu setzen, wenn die Eingabewert nicht schlüssig sind...
 *
 *  Revision 1.1  2005/06/20 18:18:23  orahn
 *  Vorbereitung für Formeleditor
 *
 */
package pirolPlugIns.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;

import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

/**
 * 
 * Class that implements a default Action Listener behavior for
 * an OKCancelButtonPanel. It remembers if ok was clicked and closes 
 * a given dialog.
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 */
public class OKCancelListener implements ActionListener {
    
    protected boolean okWasClicked = false;
    
    protected JDialog dialog = null;
    
    protected PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);
    
    protected ArrayList valueCheckers = new ArrayList();

    

    /**
     * @param dialog dialog to be closed after ok or cancel was clicked.
     */
    public OKCancelListener(JDialog dialog) {
        super();
        this.dialog = dialog;
    }
    
    /**
     * The first invokation of this method enables value checking (enables/disables funtionality of the ok button) 
     *@param valChecker object that checks if the given value are ok or not
     */
    public void addValueChecker( ValueChecker valChecker ){
        this.valueCheckers.add(valChecker);
    }
    
    /**
     * @inheritDoc
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        try {
            JButton button = (JButton)arg0.getSource();
            
            if (button.getActionCommand().equals(OkCancelButtonPanel.OK_BUTTON_ACTION_COMMAND)){
                
                // disable ok button, if value are not ok!
                if (!this.valuesOk()) return;
                
                this.okWasClicked = true;
            }
            
            if (this.dialog != null){
                this.dialog.setVisible(false);
                this.dialog.dispose();
            }
            
        } catch (ClassCastException e){
            this.logger.printError(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * asks the existent value checkers (if any), if the values are ok
     *@return true if values are ok, else false
     */
    protected boolean valuesOk(){
        if (this.valueCheckers.isEmpty()) return true;
        
        boolean valsOk = true;
        ValueChecker[] valueCheckerArray = (ValueChecker[])this.valueCheckers.toArray(new ValueChecker[0]);
        
        for (int i=0; i<valueCheckerArray.length; i++){
            valsOk = valsOk && ( valueCheckerArray[i].areValuesOk() );
        }
        
        return valsOk;
    }

    /**
     * Tells you, if ok was clicked to close the dialog
     * @return Returns the okWasClicked.
     */
    public boolean wasOkClicked() {
        return okWasClicked;
    }
}
