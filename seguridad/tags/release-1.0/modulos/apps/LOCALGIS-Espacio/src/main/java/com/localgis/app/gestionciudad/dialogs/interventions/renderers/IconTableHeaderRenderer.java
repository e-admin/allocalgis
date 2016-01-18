package com.localgis.app.gestionciudad.dialogs.interventions.renderers;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import com.localgis.app.gestionciudad.dialogs.interventions.images.IconLoader;
import com.vividsolutions.jump.I18N;

public class IconTableHeaderRenderer extends JLabel implements TableCellRenderer{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6173920142751431021L;


	public Component getTableCellRendererComponent(JTable table, 
			Object obj,boolean isSelected, boolean hasFocus, int row, int column) {

		setIcon(IconLoader.icon(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.table.priorityIcon")));

//		setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		setHorizontalAlignment(JLabel.CENTER);
		setToolTipText(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.table.priority"));
		return this;
	}

}
