package com.geopista.ui.plugin.routeenginetools.networkfusion.dialogs.tables;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import com.localgis.route.graph.structure.basic.ReaderWriterType;
import com.vividsolutions.jump.I18N;

public class ReaderWriterTypeCellRenderer  implements TableCellRenderer{

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		ReaderWriterType rwType = (ReaderWriterType) value;
		JLabel aux = null;
		String desc = "";
		if(rwType.equals(ReaderWriterType.DATABASE))
			desc = I18N.get("networkFusion","routeengine.networkfusion.rwType.database");
		if(rwType.equals(ReaderWriterType.MEMORY))
			desc = I18N.get("networkFusion","routeengine.networkfusion.rwType.memory");
		aux = new JLabel(desc);
		 if (isSelected)
	        {
	             aux.setBorder(BorderFactory.createLineBorder(Color.RED));
	         }
        return aux;
	}

}
