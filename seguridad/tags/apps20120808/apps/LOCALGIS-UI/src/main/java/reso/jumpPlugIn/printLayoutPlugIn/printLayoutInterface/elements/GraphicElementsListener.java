/*
 * Package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements pour JUMP
 *
 * Copyright (C) 2004
 * Olivier Bedel, ingénieur informaticien laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien.
 * Céline Foureau, stagiaire MIAGE, laboratoire Reso UMR ESO 6590.
 * Erwan Bocher, doctorant en géographie, laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien
 *
 * Date de création : 20 oct. 2004
 *
 * Développé dans le cadre du Projet APARAD 
 *  (Laboratoire Reso UMR ESO 6590 CNRS / Bassin Versant du Jaudy-Guindy-Bizien)
 *    Responsable : Erwan BOCHER
 *    Développeurs : Céline FOUREAU, Olivier BEDEL
 *
 * olivier.bedel@uhb.fr ou olivier.bedel@yahoo.fr
 * erwan.bocher@uhb.fr ou erwan.bocher@free.fr
 * celine.foureau@uhb.fr ou celine.foureau@wanadoo.fr
 * 
 * Ce package hérite de la licence GPL de JUMP. Il est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. (http://www.cecill.info)
 * 
 */

package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame;

/**
 * @author FOUREAU_C
 */
public class GraphicElementsListener implements MouseListener, MouseMotionListener {
	
	protected GraphicElements ge;

	private Point topLeftCorner = new Point();

	private Point topRightCorner = new Point();

	private Point BottomLeftCorner = new Point();

	private Point BottomRightCorner = new Point();

	private PrintLayoutFrame frame;

	private Point depart = new Point();

	private Point arrive = new Point();

	public GraphicElementsListener(GraphicElements ge, PrintLayoutFrame plf) {
		this.ge = ge;
		this.frame = plf;
		setCorner(ge.getCornerPoint());
	}

	public void setFrame(PrintLayoutFrame plf){
		frame=plf;
	}

	protected void setCorner(Point[] points) {
		topLeftCorner = points[0];
		topRightCorner = points[1];
		BottomLeftCorner = points[2];
		BottomRightCorner = points[3];
	}

	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (frame==null)return;
			if (frame.getSelectedComponent() != null) {
				frame.getSelectedComponent().setSelected(false);
			}
			ge.setSelected(true);
			frame.setSelectedComponent(ge);
			frame.validate();
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		depart = new Point((int) e.getPoint().getX(), (int) e.getPoint().getY());
	}

	public void mouseReleased(MouseEvent e) {
		// mouseClicked(e);
	}

	public void mouseDragged(MouseEvent e) {
		arrive = new Point((int) e.getPoint().getX(), (int) e.getPoint().getY());
		switch (ge.getGraphicElementsOnScreen().getCursor().getType()) {
		case Cursor.MOVE_CURSOR:
			if (arrive.getX() > depart.getX()) {
				if (arrive.getY() > depart.getY()) {
					ge.getGraphicElementsOnScreen().setBounds(
							(int) (ge.getGraphicElementsOnScreen().getBounds()
									.getX() + (arrive.getX() - depart.getX())),
									(int) (ge.getGraphicElementsOnScreen().getBounds()
											.getY() + (arrive.getY() - depart.getY())),
											(int) ge.getGraphicElementsOnScreen().getBounds()
											.getWidth(),
											(int) ge.getGraphicElementsOnScreen().getBounds()
											.getHeight());
				} else {
					ge.getGraphicElementsOnScreen().setBounds(
							(int) (ge.getGraphicElementsOnScreen().getBounds()
									.getX() + (arrive.getX() - depart.getX())),
									(int) (ge.getGraphicElementsOnScreen().getBounds()
											.getY() - (depart.getY() - arrive.getY())),
											(int) ge.getGraphicElementsOnScreen().getBounds()
											.getWidth(),
											(int) ge.getGraphicElementsOnScreen().getBounds()
											.getHeight());
				}
			} else {
				if (arrive.getY() < depart.getY()) {
					ge.getGraphicElementsOnScreen().setBounds(
							(int) (ge.getGraphicElementsOnScreen().getBounds()
									.getX() - (depart.getX() - arrive.getX())),
									(int) (ge.getGraphicElementsOnScreen().getBounds()
											.getY() - (depart.getY() - arrive.getY())),
											(int) ge.getGraphicElementsOnScreen().getBounds()
											.getWidth(),
											(int) ge.getGraphicElementsOnScreen().getBounds()
											.getHeight());
				} else {
					ge.getGraphicElementsOnScreen().setBounds(
							(int) (ge.getGraphicElementsOnScreen().getBounds()
									.getX() - (depart.getX() - arrive.getX())),
									(int) (ge.getGraphicElementsOnScreen().getBounds()
											.getY() + (arrive.getY() - depart.getY())),
											(int) ge.getGraphicElementsOnScreen().getBounds()
											.getWidth(),
											(int) ge.getGraphicElementsOnScreen().getBounds()
											.getHeight());
				}
			}

			break;
		case Cursor.NW_RESIZE_CURSOR:
			ge.getGraphicElementsOnScreen()
			.setBounds(
					(int) (ge.getGraphicElementsOnScreen().getBounds()
							.getX() + e.getPoint().getX()),
							(int) (ge.getGraphicElementsOnScreen().getBounds()
									.getY() + e.getPoint().getY()),
									(int) (ge.getGraphicElementsOnScreen().getBounds()
											.getWidth() - e.getPoint().getX()),
											(int) (ge.getGraphicElementsOnScreen().getBounds()
													.getHeight() - e.getPoint().getY()));
			break;
		case Cursor.NE_RESIZE_CURSOR:
			if (topRightCorner.getX() < e.getPoint().getX()) {
				if (topRightCorner.getY() < e.getPoint().getY()) {
					ge.getGraphicElementsOnScreen()
					.setBounds(
							(int) ge.getGraphicElementsOnScreen()
							.getBounds().getX(),
							(int) (ge.getGraphicElementsOnScreen()
									.getBounds().getY() + e.getPoint()
									.getY()),
									(int) (ge.getGraphicElementsOnScreen()
											.getBounds().getWidth() + (e
													.getPoint().getX() - topRightCorner
													.getX())),
													(int) (ge.getGraphicElementsOnScreen()
															.getBounds().getHeight() - e
															.getPoint().getY()));
				} else {
					ge.getGraphicElementsOnScreen()
					.setBounds(
							(int) ge.getGraphicElementsOnScreen()
							.getBounds().getX(),
							(int) (ge.getGraphicElementsOnScreen()
									.getBounds().getY() + e.getPoint()
									.getY()),
									(int) (ge.getGraphicElementsOnScreen()
											.getBounds().getWidth() + (e
													.getPoint().getX() - topRightCorner
													.getX())),
													(int) (ge.getGraphicElementsOnScreen()
															.getBounds().getHeight() - e
															.getPoint().getY()));
				}
			} else {
				if (topRightCorner.getY() < e.getPoint().getY()) {
					ge.getGraphicElementsOnScreen().setBounds(
							(int) ge.getGraphicElementsOnScreen().getBounds()
							.getX(),
							(int) (ge.getGraphicElementsOnScreen().getBounds()
									.getY() + e.getPoint().getY()),
									(int) (ge.getGraphicElementsOnScreen().getBounds()
											.getWidth() - (topRightCorner.getX() - e
													.getPoint().getX())),
													(int) (ge.getGraphicElementsOnScreen().getBounds()
															.getHeight() - e.getPoint().getY()));
				} else {
					ge.getGraphicElementsOnScreen().setBounds(
							(int) ge.getGraphicElementsOnScreen().getBounds()
							.getX(),
							(int) (ge.getGraphicElementsOnScreen().getBounds()
									.getY() + e.getPoint().getY()),
									(int) (ge.getGraphicElementsOnScreen().getBounds()
											.getWidth() - (topRightCorner.getX() - e
													.getPoint().getX())),
													(int) (ge.getGraphicElementsOnScreen().getBounds()
															.getHeight() - e.getPoint().getY()));
				}
			}
			break;
		case Cursor.SW_RESIZE_CURSOR:
			if (BottomLeftCorner.getX() < e.getPoint().getX()) {
				if (BottomLeftCorner.getY() < e.getPoint().getY()) {
					ge
					.getGraphicElementsOnScreen()
					.setBounds(
							(int) (ge.getGraphicElementsOnScreen()
									.getBounds().getX() + e.getPoint()
									.getX()),
									(int) ge.getGraphicElementsOnScreen()
									.getBounds().getY(),
									(int) (ge.getGraphicElementsOnScreen()
											.getBounds().getWidth() - e
											.getPoint().getX()),
											(int) (ge.getGraphicElementsOnScreen()
													.getBounds().getHeight() + (e
															.getPoint().getY() - BottomLeftCorner
															.getY())));
				} else {
					ge.getGraphicElementsOnScreen().setBounds(
							(int) (ge.getGraphicElementsOnScreen().getBounds()
									.getX() + e.getPoint().getX()),
									(int) ge.getGraphicElementsOnScreen().getBounds()
									.getY(),
									(int) (ge.getGraphicElementsOnScreen().getBounds()
											.getWidth() - e.getPoint().getX()),
											(int) (ge.getGraphicElementsOnScreen().getBounds()
													.getHeight() - (BottomLeftCorner.getY() - e
															.getPoint().getY())));
				}
			} else {
				if (BottomLeftCorner.getY() < e.getPoint().getY()) {
					ge
					.getGraphicElementsOnScreen()
					.setBounds(
							(int) (ge.getGraphicElementsOnScreen()
									.getBounds().getX() + e.getPoint()
									.getX()),
									(int) ge.getGraphicElementsOnScreen()
									.getBounds().getY(),
									(int) (ge.getGraphicElementsOnScreen()
											.getBounds().getWidth() - e
											.getPoint().getX()),
											(int) (ge.getGraphicElementsOnScreen()
													.getBounds().getHeight() + (e
															.getPoint().getY() - BottomLeftCorner
															.getY())));
				} else {
					ge.getGraphicElementsOnScreen().setBounds(
							(int) (ge.getGraphicElementsOnScreen().getBounds()
									.getX() + e.getPoint().getX()),
									(int) ge.getGraphicElementsOnScreen().getBounds()
									.getY(),
									(int) (ge.getGraphicElementsOnScreen().getBounds()
											.getWidth() - e.getPoint().getX()),
											(int) (ge.getGraphicElementsOnScreen().getBounds()
													.getHeight() - (BottomLeftCorner.getY() - e
															.getPoint().getY())));
				}
			}
			break;
		case Cursor.SE_RESIZE_CURSOR:
			ge.getGraphicElementsOnScreen().setBounds(
					(int) ge.getGraphicElementsOnScreen().getBounds().getX(),
					(int) ge.getGraphicElementsOnScreen().getBounds().getY(),
					(int) e.getPoint().getX(), (int) e.getPoint().getY());

			break;
		}
		ge.initCornerPoint();
		this.setCorner(ge.getCornerPoint());
		ge.refreshForPrintBounds();
		ge.repaint();
	}


	public void mouseMoved(MouseEvent e) {
		if (!ge.isSelected())
		{
			ge.getGraphicElementsOnScreen().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		}
		else
			if (e.getPoint().distance(topLeftCorner.getX(), topLeftCorner.getY()) < GraphicElements._RESIZE_HANDLER_SIZE) {
				ge.getGraphicElementsOnScreen().setCursor(
						Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
			} else if (e.getPoint().distance(topRightCorner.getX(),
					topRightCorner.getY()) < GraphicElements._RESIZE_HANDLER_SIZE) {
				ge.getGraphicElementsOnScreen().setCursor(
						Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
			} else if (e.getPoint().distance(BottomLeftCorner.getX(),
					BottomLeftCorner.getY()) < GraphicElements._RESIZE_HANDLER_SIZE) {
				ge.getGraphicElementsOnScreen().setCursor(
						Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
			} else if (e.getPoint().distance(BottomRightCorner.getX(),
					BottomRightCorner.getY()) < GraphicElements._RESIZE_HANDLER_SIZE) {
				ge.getGraphicElementsOnScreen().setCursor(
						Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
			} else {
				ge.getGraphicElementsOnScreen().setCursor(
						Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			}
	}
}