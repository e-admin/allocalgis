package com.geopista.ui.plugin.routeenginetools.incidents.dialogs.tables;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class IncidentDescriptionCellRenderer implements TableCellRenderer{

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		String incidentDesc = (String) value;
		JLabel aux = new JLabel(incidentDesc);
        if (isSelected)
        {
             aux.setBorder(BorderFactory.createLineBorder(Color.RED));
         }
        return aux;
	}

}
