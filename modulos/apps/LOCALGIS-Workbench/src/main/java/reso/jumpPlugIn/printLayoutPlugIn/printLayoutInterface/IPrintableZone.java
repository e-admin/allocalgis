/**
 * IPrintableZone.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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