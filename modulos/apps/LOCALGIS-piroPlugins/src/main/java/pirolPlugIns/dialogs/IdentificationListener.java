/**
 * IdentificationListener.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package pirolPlugIns.dialogs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

import pirolPlugIns.cursorTools.featureSelector;
import pirolPlugIns.diagrams.DiagramCanvas;

/**
 * @author orahn
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IdentificationListener extends MouseAdapter {

	private featureSelector selector;
	
	private Vector alreadySelected = null;
	
	public IdentificationListener(featureSelector selector){
		this.selector = selector;
		this.alreadySelected = new Vector();
	}
	
	public void mouseClicked(MouseEvent arg0) {
		DiagramCanvas canvas = (DiagramCanvas)arg0.getSource();
		int x = arg0.getX();
		int y = arg0.getY();
		
		int kat = canvas.xyToCategory(x,y);
		
		if (kat < 0)
			return;
		
		for ( int i=0; i<alreadySelected.size(); i++){
			if ( ((Integer)this.alreadySelected.get(i)).intValue() == kat ){
				//System.out.println("alreadySelected");
				return;
			}
		}
		
		List selFeat = canvas.xyToFID(x,y);
		
		if ( selFeat.size() > 0 ){
			this.alreadySelected.add( new Integer(kat));
			this.selector.selectFeatures(selFeat, kat);
		}
		
	}
}
