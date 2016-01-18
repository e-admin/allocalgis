/**
 * ColorEditor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/* 
 * ColorEditor.java (compiles with releases 1.3 and 1.4) is used by 
 * TableDialogEditDemo.java.
 */
package com.geopista.style.sld.ui.impl;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * @author Enxenio S.L.
 */
public class ColorEditor extends AbstractCellEditor
						 implements TableCellEditor,
						MouseListener, ActionListener {
	Color currentColor;
	JPanel panel;
	JColorChooser colorChooser;
	JDialog dialog;
	protected static final String EDIT = "edit";

	public ColorEditor() {
		//Set up the editor (from the table's point of view),
		//which is a button.
		//This button brings up the color chooser dialog,
		//which is the editor from the user's point of view.
		panel = new JPanel();
		panel.addMouseListener(this);

		//Set up the dialog that the button brings up.
		colorChooser = new JColorChooser();
		dialog = JColorChooser.createDialog(panel,
										"Pick a Color",
										true,  //modal
										colorChooser,
										this,  //OK button handler
										null); //no CANCEL button handler
	}

	/**
	 * Handles events from the editor button and from
	 * the dialog's OK button.
	 */
	public void actionPerformed(ActionEvent e) {
		//User pressed dialog's "OK" button.
		currentColor = colorChooser.getColor();
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() >= 2) {
			//The user has clicked the cell, so
			//bring up the dialog.
			panel.setBackground(currentColor);
			colorChooser.setColor(currentColor);
			dialog.setVisible(true);

			//Make the renderer reappear.
			fireEditingStopped();
		}
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}
	
	//Implement the one CellEditor method that AbstractCellEditor doesn't.
	public Object getCellEditorValue() {
		return currentColor;
	}

	//Implement the one method defined by TableCellEditor.
	public Component getTableCellEditorComponent(JTable table,
												 Object value,
												 boolean isSelected,
												 int row,
												 int column) {
		currentColor = (Color)value;
		return panel;
	}
}