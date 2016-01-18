package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface;

import java.awt.event.MouseEvent;

public interface IPrintableZone {

	public abstract double getScaleFactor();

	public abstract void setScaleFactor(double scaleFactor);

	public abstract int getPageStyle();

	public abstract void mouseClicked(MouseEvent e);

	public abstract void mouseEntered(MouseEvent e);

	public abstract void mouseExited(MouseEvent e);

	public abstract void mousePressed(MouseEvent e);

	public abstract void PosGraphicElement(Object plf);

	public abstract void mouseReleased(MouseEvent e);

	public abstract void mouseDragged(MouseEvent e);

	public abstract void mouseMoved(MouseEvent e);

	public abstract void reSize(int facteur1, int facteur2);

}