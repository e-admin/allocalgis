package com.localgis.app.gestionciudad.dialogs.interventions.tables.models;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.table.DefaultTableModel;

import com.localgis.app.gestionciudad.ConstantessLocalGISObraCivil;
import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.app.gestionciudad.utils.OperacionesConFechasObraCivil;
import com.vividsolutions.jump.I18N;

/**
 * @author javieraragon
 *
 */
public class AvisoCaducadoTableModel extends DefaultTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 781115231291570918L;

	protected static String[] columnNames = {
		I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.tablemodels.columna.idaviso"),
		I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.tablemodels.columna.descripcion"),
		I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.tablemodels.columna.fechaaviso"),
		I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.tablemodels.columna.retraso"),
	};

	public AvisoCaducadoTableModel() {
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
			return lstElementos.get(row).getId();            
		case 1: 
			return lstElementos.get(row).getDescription();
		case 2: 
			try{
				return ConstantessLocalGISObraCivil.DateFormat.format(lstElementos.get(row).getNextWarning().getTime());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return "ERROR";
			}
		case 3: 
			return OperacionesConFechasObraCivil.obtenerDuracionVencimiento((GregorianCalendar) lstElementos.get(row).getNextWarning());
		default: 
			return null;

		}        
	}



	/**
	 * Devuelve la Depuradora1EIEL completa de la fila seleccionada
	 * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
	 * @return FincaCatastro completa
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
	public void setData (ArrayList<LocalGISIntervention>  datos)
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

	public boolean borrarLindero(LocalGISIntervention aviso) {

		try {
			// Se borra la fila
			@SuppressWarnings("unused")
			int fila = lstElementos.indexOf(aviso);
			boolean result = lstElementos.remove(aviso);

			// Y se avisa a los suscriptores, creando un TableModelEvent...
//			TableModelEvent evento = new TableModelEvent(this, fila, fila,
//					TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
//
//			// ... y pasándoselo a los suscriptores
//			int i;
//
//			for (i = 0; i < suscriptores.size(); i++)
//				((TableModelListener) suscriptores.get(i)).tableChanged(evento);
//
			return result;

		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean borrarFila(int fila) {

		try {
			// Se borra la fila
			lstElementos.remove(fila);

			// Y se avisa a los suscriptores, creando un TableModelEvent...
//			TableModelEvent evento = new TableModelEvent(this, fila, fila,
//					TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
//
//			// ... y pasándoselo a los suscriptores
//			int i;
//
//			for (i = 0; i < suscriptores.size(); i++)
//				((TableModelListener) suscriptores.get(i)).tableChanged(evento);

			return true;

		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean borrarTodasFilas() {
		try {
			int t = lstElementos.size();
			for (int i = t - 1; i >= 0; i--) {
				lstElementos.remove(i);
			}

			// se modifican todas los elementos de la tabla.
//			TableModelEvent evento = new TableModelEvent(this);
//
//			// ... y pasándoselo a los suscriptores
//			for (int m = 0; m < suscriptores.size(); m++)
//				((TableModelListener) suscriptores.get(m)).tableChanged(evento);

			return true;
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

}
