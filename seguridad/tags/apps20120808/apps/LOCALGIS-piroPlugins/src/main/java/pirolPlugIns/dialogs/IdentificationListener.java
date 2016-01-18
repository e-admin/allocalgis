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
