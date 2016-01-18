/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 * 
 * Created on 31-oct-2004 by juacas
 *
 * 
 */
package com.geopista.ui.legend;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import com.geopista.ui.components.MultiLineLabelUI;
import com.geopista.ui.images.IconLoader;
// import com.sun.java.swing..SwingUtilities2;
import com.vividsolutions.jump.workbench.model.Layer;

/**
 * TODO Documentación
 * 
 * @author juacas
 * 
 */
public class LayerEntryPanel extends RuleEntryPanel {
	Layer layer;

    /**
     * rules
     */
	HashMap rules = new HashMap();// lista de reglas de esta capa.

	private JPanel textPanel = null;

	private JLabel jLabel = null;

	private JToggleButton jToggleButton = null;

	/**
	 * 
	 */
	public LayerEntryPanel() {
		super();
		initialize();
		// TODO Auto-generated constructor stub
	}
//
//	/**
//	 * @param layer2
//	 * @param i
//	 * @param j
//	 */
//	public LayerEntryPanel(Layer layer2, int i, int j) {
//		this(layer2, i, j, false);
//	}

	/**
     * 
     * @param layer 
     * @param w 
     * @param h 
     * @param elasticMode 
     */
	public LayerEntryPanel(Layer layer, int w, int h, boolean elasticMode) {
		super(w, h, elasticMode);
		
		font_divisor = 2.3f;
		this.layer = layer;
		getTitleLabel().setText(layer.getName());
		BorderLayout borderLayout= new BorderLayout(w,0);
		this.setLayout(borderLayout);
		this.add(getTextPanel(), BorderLayout.CENTER);

		initialize();
	}


	
	
    /**
     * initialize
     */
	void initialize() {
		//super.initialize();
		getCheckBox().addItemListener(new ItemListener() 
		{
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				selectRules(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
	}
	
    /**
     * 
     * @param shown 
     */
	public void showRules(boolean shown) {
		if (rules.size() == 1)
			return;
		Iterator rule = rules.values().iterator();
		while (rule.hasNext()) {
			RuleEntryPanel element = (RuleEntryPanel) rule.next();
			element.setVisible(shown);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.geopista.ui.legend.LegendEntryPanel#getTextPanel()
	 */
    /**
     * Devuelve el panel texto
     * @return 
     */
	protected JPanel getTextPanel() {
		if (textPanel == null) {
			textPanel = new JPanel();
			textPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			textPanel.add(getTitleLabel());
			textPanel.setOpaque(false);
		}
		return textPanel;
	}

    /**
     * 
     * @return 
     */
	public HashMap getRules() {
		return rules;
	}

	/**
	 * @param panel
	 */
	public void setSymbolizerPanel(SymbolizerPanel panel) {
		// getLeftPanel().remove(symbolPanel);
		// symbolPanel=panel;
		getLeftPanel().add(panel, 2);

	}

    /**
     * 
     * @return 
     */
	protected JPanel getLeftPanel() {
		JPanel pan = super.getLeftPanel();
		pan.add(getJToggleButton(), 1);
		return pan;
	}

	/**
	 * This method initializes jToggleButton
	 * 
	 * @return javax.swing.JToggleButton
	 */
	JToggleButton getJToggleButton() {
		if (jToggleButton == null) {
			jToggleButton = new JToggleButton();
			jToggleButton.setSelected(true);
			jToggleButton.setIcon(IconLoader
					.icon("Mapa_zoom_features_seleccionados.GIF"));
			jToggleButton.setPreferredSize(new java.awt.Dimension(20, 20));
			jToggleButton.setOpaque(false);

			jToggleButton.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					LayerEntryPanel.this
							.showRules(e.getStateChange() == ItemEvent.SELECTED);
					LayerEntryPanel.this
							.selectRules(e.getStateChange() == ItemEvent.SELECTED);
				}
			});
		}
		return jToggleButton;
	}

	
    /**
     * 
     * @param editingmode 
     */
	public void setEditingMode(boolean editingmode) {
		super.setEditingMode(editingmode);
		
		if (rules.size() == 1)
			((RuleEntryPanel) rules.values().iterator().next()).setVisible(false);

		getJToggleButton().setVisible(editingmode && rules.size() > 1);
	}


	/**
	 * @param b
	 */
	protected void selectRules(boolean b) {
		Iterator rul = rules.values().iterator();
		while (rul.hasNext()) {
			RuleEntryPanel element = (RuleEntryPanel) rul.next();
			element.setSelected(b);

		}
	}

    /**
     * Redimensionar el panel de texto
     */
	protected void resizeTextPanel() {
		Font font = getTitleLabel().getFont();
		getTitleLabel().setFont(
				font.deriveFont(getSymbolHeight() / font_divisor));
	}

	/**
	 * @return
	 */
	private JLabel getTitleLabel() {
		if (jLabel == null) {
			jLabel = new JLabel();
			Font layerNameFont = new Font("Arial", Font.BOLD,
					(int) (getSymbolHeight() / font_divisor));
			jLabel.setFont(layerNameFont);

			jLabel.setOpaque(false);
			jLabel.setHorizontalAlignment(JLabel.LEFT);
			jLabel.setVerticalAlignment(JLabel.CENTER);
			jLabel.setUI(new MultiLineLabelUI());
		}
		return jLabel;
	}
}
