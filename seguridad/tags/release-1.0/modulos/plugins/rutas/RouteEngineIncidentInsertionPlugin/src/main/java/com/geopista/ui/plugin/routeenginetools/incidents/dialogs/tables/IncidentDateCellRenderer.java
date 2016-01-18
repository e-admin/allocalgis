package com.geopista.ui.plugin.routeenginetools.incidents.dialogs.tables;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class IncidentDateCellRenderer implements TableCellRenderer{

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		String data = "";
		if(value != null){
			Date incidentDate = (Date) value;
			String format = "yyyy-MM-dd";
			SimpleDateFormat formato = new SimpleDateFormat(format);
			data = formato.format(incidentDate);
		}
		JLabel aux = new JLabel(data);
        if (isSelected)
        {
             aux.setBorder(BorderFactory.createLineBorder(Color.RED));
         }
        return aux;
	}

}
