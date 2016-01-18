package com.localgis.app.gestionciudad.dialogs.interventions.tables.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.localgis.app.gestionciudad.ConstantessLocalGISObraCivil;
import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.app.gestionciudad.images.IconLoader;
import com.vividsolutions.jump.I18N;

/**
 * @author javieraragon
 *
 */
public class AvisoSimpleTableModel extends DefaultTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5614359295796638697L;

	protected static String[] columnNames = {
		I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.tablemodels.columna.priority"),
//		IconLoader.icon("flechas.gif").toString(),
		I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.tablemodels.columna.idaviso"),
		I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.tablemodels.columna.descripcion"),
		I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.tablemodels.columna.fechaalta"),
		I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.tablemodels.columna.fechaaviso"),
	};

	public AvisoSimpleTableModel() {
	}

	private ArrayList<LocalGISIntervention> lstElementos = new ArrayList<LocalGISIntervention>();

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
			return lstElementos.get(row).getPriority();
		case 1: 
			return lstElementos.get(row).getId();   
		case 2: 
			return lstElementos.get(row).getDescription();
		case 3: 
			try{
				return ConstantessLocalGISObraCivil.DateFormat.format(lstElementos.get(row).getStartWarning().getTime());
			} catch (Exception e) {
			// TODO: handle exception
				e.printStackTrace();
				return "ERROR";
			}
		case 4: 
			try{
				if (lstElementos.get(row).getNextWarning() != null){
					return ConstantessLocalGISObraCivil.DateFormat.format(lstElementos.get(row).getNextWarning().getTime());	
				} else{
					return "*";
				}
				
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
	 * Devuelve el aviso completo de la fila seleccionada
	 * @param row Índice de la fila cuyo objeto Aviso se solicita
	 * @return Aviso completo
	 */
	public LocalGISIntervention getValueAt(int row) {
		return (LocalGISIntervention)lstElementos.get(row);
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
	public void setData (ArrayList<LocalGISIntervention> datos)
	{
		this.lstElementos = datos;
	}

	/**
	 * Recupera los datos del modelo
	 * @return Datos del modelo
	 */
	public ArrayList<LocalGISIntervention> getData (){
		return lstElementos;
	}
	
	public boolean anniadirAviso(LocalGISIntervention aviso) {
		try {
			// Añade el lindero al modelo
			boolean result = lstElementos.add(aviso);

			// Avisa a los suscriptores creando un TableModelEvent...
//			TableModelEvent evento;
//			evento = new TableModelEvent(this, this.getRowCount() - 1, this
//					.getRowCount() - 1, TableModelEvent.ALL_COLUMNS,
//					TableModelEvent.INSERT);
//
//			for (int i = 0; i < suscriptores.size(); i++)
//				((TableModelListener) suscriptores.get(i)).tableChanged(evento);

			return result;
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}


}
