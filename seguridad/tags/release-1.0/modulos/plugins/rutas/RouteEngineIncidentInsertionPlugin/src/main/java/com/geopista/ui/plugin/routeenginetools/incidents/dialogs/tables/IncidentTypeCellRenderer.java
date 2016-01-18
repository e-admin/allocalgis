package com.geopista.ui.plugin.routeenginetools.incidents.dialogs.tables;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.localgis.route.graph.structure.basic.LocalGISIncident;

public class IncidentTypeCellRenderer implements TableCellRenderer{

	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		LocalGISIncident incident = (LocalGISIncident) value;
		String incidentType = "";
		if(incident.getIncidentType() == LocalGISIncident.PATH_CLOSED_TO_VEHICLES)
			incidentType = "Cerrado a Vehiculos";
		if(incident.getIncidentType() == LocalGISIncident.PATH_DISABLED)
			incidentType = "Cerrado";
		JLabel aux = new JLabel(incidentType);
        if (isSelected)
        {
             aux.setBorder(BorderFactory.createLineBorder(Color.RED));
         }
        return aux;
	}

}
