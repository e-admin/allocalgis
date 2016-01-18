package com.localgis.app.gestionciudad.dialogs.notes.tables.models;

import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import com.localgis.app.gestionciudad.ConstantessLocalGISObraCivil;
import com.localgis.app.gestionciudad.beans.LocalGISNote;
import com.vividsolutions.jump.I18N;

/**
 * @author javieraragon
 *
 */
public class NoteSimpleTableModel extends DefaultTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5614359295796638697L;

	protected static String[] columnNames = {
		I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.tablemodels.columna.idaviso"),
		I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.tablemodels.columna.descripcion"),
		I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.tablemodels.columna.fechaalta")
	};

	public NoteSimpleTableModel() {
	}

	private ArrayList<LocalGISNote> lstElementos = new ArrayList<LocalGISNote>();

	/**
	 * @return número de columnas de la tabla
	 */
	public int getColumnCount() {
		if (columnNames!=null){
			return columnNames.length;
		}
		else
			return 0;
	}

	/**
	 * @return número de filas de la tabla
	 */
	public int getRowCount() {
		if (lstElementos != null)
			return lstElementos.size();
		else
			return 0;
	}


	/**
	 * Devuelve el nombre de la columna solicitada
	 * @param col Índice de la columna
	 * @return nombre de la columna
	 */
	public String getColumnName(int col) {
		return columnNames[col];
	}

	/**
	 * Devuelve el objeto que contiene la celda en la posición indicada
	 * @param row Índice de la fila
	 * @param col Índice de la columna
	 * 
	 * @return Objeto contenido en la posición seleccionada
	 */
	public Object getValueAt(int row, int col) {

		if (lstElementos.get(row)==null)
			return null;

		switch (col)
		{
		case 0: 
			return lstElementos.get(row).getId();            
		case 1: 
			return lstElementos.get(row).getDescription();
		case 2: 
			try{
				return ConstantessLocalGISObraCivil.DateFormat.format(lstElementos.get(row).getStartWarning().getTime());
			} catch (Exception e) {
			// TODO: handle exception
				e.printStackTrace();
				return "ERROR";
			}
		default: 
			return null;

		}        
	}



	/**
	 * Devuelve el note completo de la fila seleccionada
	 * @param row Índice de la fila cuyo objeto Aviso se solicita
	 * @return Aviso completo
	 */
	public LocalGISNote getValueAt(int row) {
		return (LocalGISNote)lstElementos.get(row);
	}
	
	
	
	/**
	 * JTable uses this method to determine the default renderer/
	 * editor for each cell.  
	 */
	@SuppressWarnings("unchecked")
	public Class getColumnClass(int c) {

		if (getValueAt(0, c)!=null)        
			return getValueAt(0, c).getClass();
		else
			return String.class;
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}


	/**
	 * Establece los datos mostrados en el modelo
	 * @param datos Datos a mostrar en el modelo
	 */
	public void setData (ArrayList<LocalGISNote> datos)
	{
		this.lstElementos = datos;
	}

	/**
	 * Recupera los datos del modelo
	 * @return Datos del modelo
	 */
	public ArrayList<LocalGISNote> getData (){
		return lstElementos;
	}
	
	public boolean anniadirAviso(LocalGISNote note) {
		try {
			// Añade el lindero al modelo
			boolean result = lstElementos.add(note);

			// Avisa a los suscriptores creando un TableModelEvent...
			TableModelEvent evento;
			evento = new TableModelEvent(this, this.getRowCount() - 1, this
					.getRowCount() - 1, TableModelEvent.ALL_COLUMNS,
					TableModelEvent.INSERT);
//
			
			
			for (int i = 0; i < this.getTableModelListeners().length; i++)
				((TableModelListener) this.getTableModelListeners()[i]).tableChanged(evento);

			return result;
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}


}
