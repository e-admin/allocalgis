/**
 * DiagrammDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 01.12.2004
 *
 * CVS header information:
 *  $RCSfile: DiagrammDialog.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/DiagrammDialog.java,v $
 */
package pirolPlugIns.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JPanel;

import pirolPlugIns.cursorTools.featureSelector;
import pirolPlugIns.diagrams.InterativeDiagram;


/**
 * Dialog, that just displays a diagramm, that was drawn into the given JPanel
 * 
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabrück - University of Applied Sciences Osnabrück
 * <br>Project PIROL 2005
 * <br>Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 */
public class DiagrammDialog extends InterativeDiagram {
	
    private static final long serialVersionUID = 1869932994840521595L;

	private JPanel drawingPanel;

	public DiagrammDialog(Frame parent, String titel, boolean modal, JPanel canvas, featureSelector selector )
			throws HeadlessException {
		super( parent, titel, modal, selector);
		
		this.drawingPanel = canvas;
		
		this.setupDialog();
	}
	
	protected void setupDialog(){
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(500,550));
		panel.setLayout(new BorderLayout());
		
		this.drawingPanel.addMouseListener(new IdentificationListener(this.selector));
        
        JButton saveImgButton = new JButton();
        saveImgButton.setAction(new SaveImageAsAction(this, this.drawingPanel));
		
		panel.add(this.drawingPanel, BorderLayout.CENTER);
        panel.add(saveImgButton, BorderLayout.SOUTH);
		panel.doLayout();
		
		this.getContentPane().add(panel);
		this.pack();
	}

}
