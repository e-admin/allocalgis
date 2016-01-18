/**
 * 
 */
package com.localgis.app.gestionciudad.dialogs.interventions.tables;

import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.app.gestionciudad.dialogs.interventions.renderers.IconTableHeaderRenderer;
import com.localgis.app.gestionciudad.dialogs.interventions.renderers.InterventionsTableRenderer;
import com.localgis.app.gestionciudad.dialogs.interventions.tables.models.AvisoSimpleTableModel;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.localgis.app.gestionciudad.utils.TableSorted;

/**
 * @author javieraragon
 *
 */
public class AvisosListTable extends JTable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8923088283751869418L;

	private AvisoSimpleTableModel avisoSimpleTableModel = null;

	public AvisosListTable(ArrayList<LocalGISIntervention> listaAvisos){
		super();

		avisoSimpleTableModel  = new AvisoSimpleTableModel();
		TableSorted tblSorted= new TableSorted((TableModel)avisoSimpleTableModel);

		tblSorted.setTableHeader(this.getTableHeader());
		this.setModel(tblSorted);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setCellSelectionEnabled(false);
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(true);
		this.getTableHeader().setReorderingAllowed(false);

		((AvisoSimpleTableModel)((TableSorted)this.getModel()).getTableModel()).setData(listaAvisos);

		this.pakcsAvisosListColumns();

		int vColIndex = 0; 
		this.getColumnModel().getColumn(vColIndex).setCellRenderer(new InterventionsTableRenderer());
		
		SetIcon(this, 0);
	}

	public void SetIcon(JTable table, int col_index){
		table.getTableHeader().getColumnModel().getColumn(col_index).setHeaderRenderer(new IconTableHeaderRenderer());
	}

	private void pakcsAvisosListColumns(){
		UtilidadesAvisosPanels.packColumns(this, 2);
//		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		DefaultTableCellRenderer tcrCenter = new DefaultTableCellRenderer();
		tcrCenter.setHorizontalAlignment(SwingConstants.CENTER);

		DefaultTableCellRenderer tcrRight = new DefaultTableCellRenderer();
		tcrRight.setHorizontalAlignment(SwingConstants.LEFT);
		tcrRight.setSize(0, 0);

		this.getColumnModel().getColumn(0).setCellRenderer(tcrCenter);
		this.getColumnModel().getColumn(1).setCellRenderer(tcrRight);
		this.getColumnModel().getColumn(2).setCellRenderer(tcrCenter);
		this.getColumnModel().getColumn(3).setCellRenderer(tcrCenter);
		this.getColumnModel().getColumn(4).setCellRenderer(tcrCenter);
	}

}
