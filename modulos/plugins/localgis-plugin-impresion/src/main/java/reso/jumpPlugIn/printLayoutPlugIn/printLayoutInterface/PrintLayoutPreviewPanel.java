/*
 * Package reso.JumpPlugIn.PrintLayoutPlugIn.PrintLayoutInterface pour JUMP
 *
 * Copyright (C) 2004
 * Olivier Bedel, ingénieur informaticien laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien.
 * Céline Foureau, stagiaire MIAGE, laboratoire Reso UMR ESO 6590.
 * Erwan Bocher, doctorant en géographie, laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien
 *
 * Date de création : 1 sept. 2004
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

package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

/**
 * @author FOUREAU_C
 */
public class PrintLayoutPreviewPanel
	extends JScrollPane
	implements ItemListener, MouseListener {

    private PrintLayoutFrame parent;
    
	private Rule columnView;
	private Rule rowView;
	private PreviewPanel preview;
	private JToggleButton btnUnits;

	public PrintLayoutPreviewPanel(PrintLayoutFrame parent) {
	    this.parent = parent;
	    
		setPreferredSize(new Dimension(875, 675));
		setSize(getPreferredSize());
		getViewport().setPreferredSize(new Dimension(800, 600));
		getViewport().setSize(getViewport().getPreferredSize());

		//Create the row and column headers.
		columnView = new Rule(Rule.HORIZONTAL, true);
		rowView = new Rule(Rule.VERTICAL, true);

		columnView.setPreferredWidth(
			Math.round((float) getViewport().getSize().getWidth()));
		rowView.setPreferredHeight(
			Math.round((float) getViewport().getSize().getWidth()));

		//Create the corners.
		JPanel buttonCorner = new JPanel(); //use FlowLayout
		btnUnits = new JToggleButton("cm", true);
		btnUnits.setFont(new Font("SansSerif", Font.PLAIN, 11));
		btnUnits.setPreferredSize(new Dimension(25, 25));
		btnUnits.setSize(btnUnits.getPreferredSize());
		btnUnits.setMargin(new Insets(2, 2, 2, 2));
		btnUnits.addItemListener(this);
		buttonCorner.add(btnUnits);

		//Set up the scroll pane.
		setColumnHeaderView(columnView);
		setRowHeaderView(rowView);
		setViewportBorder(BorderFactory.createLineBorder(Color.BLACK));

		//Set the corners.
		setCorner(JScrollPane.UPPER_LEFT_CORNER, buttonCorner);
		setCorner(JScrollPane.LOWER_LEFT_CORNER, new Corner());
		setCorner(JScrollPane.UPPER_RIGHT_CORNER, new Corner());
		
		addMouseListener(this);
	}

	public void itemStateChanged(ItemEvent e) {
		// TODO Raccord de méthode auto-généré
		if (e.getStateChange() == ItemEvent.SELECTED) {
			//Turn it to metric.
			rowView.setIsMetric(true, preview.getCmUnit());
			columnView.setIsMetric(true, preview.getCmUnit());
			btnUnits.setText("cm");
		} else {
			//Turn it to inches.
			rowView.setIsMetric(false, preview.getInchUnit());
			columnView.setIsMetric(false, preview.getInchUnit());
			btnUnits.setText("in");
		}
	}

	public void setPreview (PreviewPanel preview) {
		this.preview = preview;
		setViewportView(preview);
		if (rowView.isMetric()) {
			rowView.setIsMetric(true, preview.getCmUnit());
			columnView.setIsMetric(true, preview.getCmUnit());
			btnUnits.setText("cm");
		} else {
			rowView.setIsMetric(false, preview.getInchUnit());
			columnView.setIsMetric(false, preview.getInchUnit());
			btnUnits.setText("in");
		}
	}

	public PreviewPanel getPreviewPanel() {
		return preview;
	}

	public boolean isMetric() {
		return rowView.isMetric();
	}
	
	private class Corner extends JComponent {
		protected void paintComponent(Graphics g) {
			// Fill me with dirty brown/orange.
			//g.setColor(new Color(230, 163, 4));
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}
	
	private class Rule extends JComponent {
		protected final static int HORIZONTAL = 0;
		protected final static int VERTICAL = 1;
	
		private final int INCH = Toolkit.getDefaultToolkit().getScreenResolution();
		private final int CM = Math.round((float)(INCH/2.54));
		private final int SIZE = 35;

		private int orientation;
		private boolean isMetric;
		private int increment;
		private int units;

		public Rule(int o, boolean m) {
			orientation = o;
			isMetric = m;
			setIncrementAndUnits();
		}

		public Rule(int o, boolean m, int unit) {
			orientation = o;
			isMetric = m;
			setIncrementAndUnits(unit);
		}

		public void setIsMetric(boolean isMetric) {
			this.isMetric = isMetric;
			setIncrementAndUnits();
			repaint();
		}

		public void setIsMetric(boolean isMetric, int unit) {
			this.isMetric = isMetric;
			setIncrementAndUnits(unit);
			repaint();
		}

		private void setIncrementAndUnits() {
			if (isMetric) {
				units = CM;
				// dots per centimeter
				increment = units / 2;
			} else {
				units = INCH;
				increment = units / 2;
			}
		}

		private void setIncrementAndUnits(int unit) {
			if (isMetric) {
				units = unit;
				// dots per centimeter
				increment = units;
			} else {
				units = unit;
				increment = units / 2;
			}
		}

		public boolean isMetric() {
			return this.isMetric;
		}

		public int getIncrement() {
			return increment;
		}

		public void setPreferredHeight(int ph) {
			setPreferredSize(new Dimension(SIZE, ph));
		}

		public void setPreferredWidth(int pw) {
			setPreferredSize(new Dimension(pw, SIZE));
		}

		protected void paintComponent(Graphics g) {
			Rectangle drawHere = g.getClipBounds();

			// Fill clipping area with dirty brown/orange.
			//g.setColor(new Color(230, 163, 4));
			g.setColor(Color.WHITE);
			g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);

			// Do the ruler labels in a small font that's black.
			g.setFont(new Font("SansSerif", Font.PLAIN, 10));
			g.setColor(Color.black);

			// Some vars we need.
			int end = 0;
			int start = 0;
			int tickLength = 0;
			String text = null;

			// Use clipping bounds to calculate first and last tick locations.
			if (orientation == HORIZONTAL) {
				start = (drawHere.x / increment) * increment;
				end = (((drawHere.x + drawHere.width) / increment) + 1) * increment;
			} else {
				start = (drawHere.y / increment) * increment;
				end =
					(((drawHere.y + drawHere.height) / increment) + 1) * increment;
			}

			// Make a special case of 0 to display the number
			// within the rule and draw a units label.
			if (start == 0) {
				text = Integer.toString(0);
				tickLength = 10;
				if (orientation == HORIZONTAL) {
					g.drawLine(0, SIZE - 1, 0, SIZE - tickLength - 1);
					g.drawString(text, 2, 21);
				} else {
					g.drawLine(SIZE - 1, 0, SIZE - tickLength - 1, 0);
					g.drawString(text, 9, 10);
				}
				text = null;
				start = increment;
			}

			// ticks and labels
			for (int i = start; i < end; i += increment) {
				if (i % units == 0) {
					tickLength = 10;
					text = Integer.toString(i / units);
				} else {
					tickLength = 7;
					text = null;
				}

				if (tickLength != 0) {
					if (orientation == HORIZONTAL) {
						g.drawLine(i, SIZE - 1, i, SIZE - tickLength - 1);
						if (text != null)
							g.drawString(text, i - 3, 21);
					} else {
						g.drawLine(SIZE - 1, i, SIZE - tickLength - 1, i);
						if (text != null)
							g.drawString(text, 9, i + 3);
					}
				}
			}
		}
	}
	
    public void mouseClicked(MouseEvent arg0) {
        if(parent.getSelectedComponent() != null){
            parent.getSelectedComponent().setSelected(false);
        }
        parent.setSelectedComponent(null);
    }

    public void mouseEntered(MouseEvent arg0) {
       
    }

 
    public void mouseExited(MouseEvent arg0) {
        
    }

  
    public void mousePressed(MouseEvent arg0) {
        
    }

    public void mouseReleased(MouseEvent arg0) {
        
    }
}
