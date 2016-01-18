/*
 * Created on 14.12.2004
 *
 * CVS header information:
 *  $RCSfile: DiagramCanvas.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:45 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/diagrams/DiagramCanvas.java,v $
 */
package pirolPlugIns.diagrams;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import pirolPlugIns.utilities.colors.ColorGenerator;

/**
 * @author Ole Rahn
 * 
 * FH Osnabrück - University of Applied Sciences Osnabrück
 * Project PIROL 2005
 * Daten- und Wissensmanagement
 */
public abstract class DiagramCanvas extends JPanel {
	protected HashMap items;
	
	protected int axisBottomOffset = 100;
	protected int axisLeftOffset = 50;
	
	protected int axisRightOffset = 50;
	protected int axisTopOffset = 50;

	protected int rowHeight = 15;
	
	protected int oldWidth, oldHeight, paintCounter;
	
	protected ColorGenerator colorGenerator = null;
	
	//protected Color[] colors;

	public abstract List xyToFID( int x, int y );
	public abstract int xyToCategory( int x, int y );
	public void paint(Graphics g){
	    super.paint(g);
	}

	protected abstract void drawAxis(Graphics2D graf);
	protected abstract void drawStatistics(Graphics2D graf);
	
	public DiagramCanvas(boolean doubleBuffered, ColorGenerator cg){
	    super(doubleBuffered);
	    this.colorGenerator = cg;
	}
	
	protected void drawStatistics(Graphics2D graf, String[] texte){
	
		int x = this.getDiagrammWidth() - 80;
		int y = - this.getDiagrammHeight(); 
		
		for ( int i=0; i<texte.length; i++ ){
			AlphaComposite aComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f);
			graf.setComposite(aComp);
			
			graf.setColor(Color.WHITE);
			graf.fillRect(x-5, y+(i*rowHeight)-rowHeight, 90+this.axisRightOffset, rowHeight);
			graf.setColor(Color.BLACK);
			
			aComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f);
			graf.setComposite(aComp);
			
			graf.drawString(texte[i], x, y+(i*rowHeight));
		}
	}
	
	protected int getDiagrammWidth(){
		return this.getWidth() - axisRightOffset - axisLeftOffset;
	}
	
	protected int getDiagrammHeight(){
		return this.getHeight() - axisBottomOffset - axisTopOffset;
	}

}
