/**
 * 
 */
package com.localgis.app.gestionciudad.dialogs.interventions.utils;

import java.awt.Component;
import java.awt.TextArea;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 * @author javieraragon
 *
 */
public class TextRenderer extends TextArea implements TableCellRenderer{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8375206650209301561L;

	public TextRenderer(){
		super("",2,20,
		TextArea.SCROLLBARS_VERTICAL_ONLY); 
			}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column){
		append((value == null) ? "": value.toString());
		return this;
	}
}