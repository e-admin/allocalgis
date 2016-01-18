package com.geopista.ui.plugin.routeenginetools.networkfusion.dialogs.tables;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import com.localgis.route.graph.structure.basic.GraphType;
import com.vividsolutions.jump.I18N;

public class GraphTypeCellRenderer  implements TableCellRenderer{

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		GraphType gType = (GraphType) value;
		JLabel aux = null;
		String desc = "";
		if(gType.equals(GraphType.GENERIC))
			desc = I18N.get("networkFusion","routeengine.networkfusion.graphType.generic");
		if(gType.equals(GraphType.STREET))
			desc = I18N.get("networkFusion","routeengine.networkfusion.graphType.street");
		aux = new JLabel(desc);
		 if (isSelected)
	        {
	             aux.setBorder(BorderFactory.createLineBorder(Color.RED));
	         }
        return aux;
	}

}
