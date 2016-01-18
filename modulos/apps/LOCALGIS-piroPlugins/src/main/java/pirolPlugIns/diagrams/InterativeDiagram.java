/**
 * InterativeDiagram.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 08.12.2004
 *
 * CVS header information:
 *  $RCSfile: InterativeDiagram.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:45 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/diagrams/InterativeDiagram.java,v $
 */
package pirolPlugIns.diagrams;

import java.awt.Frame;
import java.awt.HeadlessException;

import javax.swing.JDialog;

import pirolPlugIns.cursorTools.featureSelector;

/**
 * @author orahn
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class InterativeDiagram extends JDialog {

	protected featureSelector selector;

	public InterativeDiagram(Frame parent, String titel, boolean modal, featureSelector selector )
			throws HeadlessException {
		super(parent, titel, modal);
		this.selector = selector;
		
	}
	
	protected abstract void setupDialog();

}
