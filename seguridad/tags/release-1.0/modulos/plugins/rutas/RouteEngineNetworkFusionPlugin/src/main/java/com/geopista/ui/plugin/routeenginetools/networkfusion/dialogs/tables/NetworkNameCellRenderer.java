package com.geopista.ui.plugin.routeenginetools.networkfusion.dialogs.tables;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import com.geopista.ui.plugin.routeenginetools.networkfusion.beans.NetworkBean;

public class NetworkNameCellRenderer implements TableCellRenderer{

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		NetworkBean network = (NetworkBean) value;
		JLabel aux = new JLabel(network.getNetworkName());
        if (isSelected)
        {
             aux.setBorder(BorderFactory.createLineBorder(Color.RED));
         }
        return aux;
	}

}
