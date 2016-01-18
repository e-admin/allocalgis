/**
 * CheckBoxPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 18.08.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: CheckBoxPanel.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/CheckBoxPanel.java,v $
 */
package pirolPlugIns.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import pirolPlugIns.i18n.PirolPlugInMessages;

/**
 * Panel that shows a checkbox plus n labels that display the given text. 
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * 
 */
public class CheckBoxPanel extends JPanel {

    private static final long serialVersionUID = -9036521063881486853L;
    
    protected JCheckBox checkBox = new JCheckBox();
    
    public CheckBoxPanel(String resourceTextKey){
        super(new BorderLayout());
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        this.add(this.checkBox, BorderLayout.WEST);
        this.add( DialogTools.getPanelWithLabels(PirolPlugInMessages.getString(resourceTextKey), 45), BorderLayout.CENTER);
        this.doLayout();
    }

    public void addActionListener(ActionListener arg0) {
        checkBox.addActionListener(arg0);
    }

    public boolean isSelected() {
        return checkBox.isSelected();
    }

    public void setEnabled(boolean arg0) {
        checkBox.setEnabled(arg0);
    }

    public void setSelected(boolean arg0) {
        checkBox.setSelected(arg0);
    }

    public void setSelectedIcon(Icon arg0) {
        checkBox.setSelectedIcon(arg0);
    }
    
}
