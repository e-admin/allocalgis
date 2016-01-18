/**
 * 
 */
package com.localgis.app.gestionciudad.dialogs.notes.tables;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.localgis.app.gestionciudad.beans.LocalGISNote;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.localgis.app.gestionciudad.dialogs.notes.tables.models.NoteSimpleTableModel;
import com.localgis.app.gestionciudad.utils.TableSorted;

/**
 * @author javieraragon
 *
 */
public class NotesListTable extends JTable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8923088283751869418L;
	
	private NoteSimpleTableModel avisoSimpleTableModel = null;
	
	public NotesListTable(){
		super();
		avisoSimpleTableModel  = new NoteSimpleTableModel();
		TableSorted tblSorted= new TableSorted((TableModel)avisoSimpleTableModel);
		
		tblSorted.setTableHeader(this.getTableHeader());
		this.setModel(tblSorted);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setCellSelectionEnabled(false);
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(true);
		this.getTableHeader().setReorderingAllowed(false);
		this.pakcsEmptyNotesListColumns();
	}
	
	public NotesListTable(ArrayList<LocalGISNote> listaNotes){
		super();

		avisoSimpleTableModel  = new NoteSimpleTableModel();
		TableSorted tblSorted= new TableSorted((TableModel)avisoSimpleTableModel);
		
		tblSorted.setTableHeader(this.getTableHeader());
		this.setModel(tblSorted);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setCellSelectionEnabled(false);
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(true);
		this.getTableHeader().setReorderingAllowed(false);
		
		((NoteSimpleTableModel)((TableSorted)this.getModel()).getTableModel()).setData(listaNotes);
		
		this.pakcsNotesListColumns();
	}
	
	public void pakcsNotesListColumns(){
		UtilidadesAvisosPanels.packColumns(this, 2);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		DefaultTableCellRenderer tcrCenter = new DefaultTableCellRenderer();
		tcrCenter.setHorizontalAlignment(SwingConstants.CENTER);
		
		DefaultTableCellRenderer tcrRight = new DefaultTableCellRenderer();
		tcrRight.setHorizontalAlignment(SwingConstants.LEFT);
		tcrRight.setSize(0, 0);

		this.getColumnModel().getColumn(0).setCellRenderer(tcrCenter);
		this.getColumnModel().getColumn(1).setCellRenderer(tcrRight);
		this.getColumnModel().getColumn(2).setCellRenderer(tcrCenter);
	}
	
	
	private void pakcsEmptyNotesListColumns(){
		packColumns(this, 2);
//		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		DefaultTableCellRenderer tcrCenter = new DefaultTableCellRenderer();
		tcrCenter.setHorizontalAlignment(SwingConstants.CENTER);
		
		DefaultTableCellRenderer tcrRight = new DefaultTableCellRenderer();
		tcrRight.setHorizontalAlignment(SwingConstants.LEFT);
		tcrRight.setSize(0, 0);

		this.getColumnModel().getColumn(0).setCellRenderer(tcrCenter);
		this.getColumnModel().getColumn(1).setCellRenderer(tcrRight);;
		this.getColumnModel().getColumn(2).setCellRenderer(tcrCenter);
	}
	
	public void updateTable(){
		this.pakcsNotesListColumns();
		this.updateUI();
	}

	
	private void packColumns(JTable table, int margin) {
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int c=0; c<table.getColumnCount(); c++) {
			if (c!=1)
			packColumn(table, c, 2);
		}
	}
	
	private void packColumn(JTable table, int vColIndex, int margin) {
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		DefaultTableColumnModel colModel = (DefaultTableColumnModel)table.
		getColumnModel();
		TableColumn col = colModel.getColumn(vColIndex);
		int width = 0;    

		// obtiene la anchura de la cabecera de la columna
		TableCellRenderer renderer = col.getHeaderRenderer();
		if (renderer == null) {
			renderer = table.getTableHeader().getDefaultRenderer();
		}
		Component comp = renderer.getTableCellRendererComponent(
				table, col.getHeaderValue(), false, false, 0, 0);
//		width = comp.getPreferredSize().width;    
		width = 5;

		// Obtine la anchura maxima de la coluna de
		for (int r=0; r<table.getRowCount(); r++) {
			renderer = table.getCellRenderer(r, vColIndex);
			comp = renderer.getTableCellRendererComponent(
					table, table.getValueAt(r, vColIndex), false, false, r,
					vColIndex);
			width = Math.max(width, 0);
		}    

		width += 2*margin;    

		// Configura el ancho
		col.setPreferredWidth(width);
	}
}
