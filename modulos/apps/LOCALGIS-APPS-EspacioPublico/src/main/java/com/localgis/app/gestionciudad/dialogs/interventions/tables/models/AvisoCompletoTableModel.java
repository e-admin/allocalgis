/**
 * AvisoCompletoTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.dialogs.interventions.tables.models;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.table.DefaultTableModel;

import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.app.gestionciudad.utils.OperacionesConFechasObraCivil;
import com.vividsolutions.jump.I18N;

/**
 * @author javieraragon
 *
 */
public class AvisoCompletoTableModel extends DefaultTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6184941525168464067L;
	
	protected static String[] columnNames = {
		I18N.get("LocalGISObraCivil","localgisgestionciudad.panels.avisos.tablemodels.columna.idaviso"),
		I18N.get("LocalGISObraCivil","localgisgestionciudad.panels.avisos.tablemodels.columna.descripcion"),
		I18N.get("LocalGISObraCivil","localgisgestionciudad.panels.avisos.tablemodels.columna.actuacion"),
		I18N.get("LocalGISObraCivil","localgisgestionciudad.panels.avisos.tablemodels.columna.causa"),
		I18N.get("LocalGISObraCivil","localgisgestionciudad.panels.avisos.tablemodels.columna.fechaalta"),
		I18N.get("LocalGISObraCivil","localgisgestionciudad.panels.avisos.tablemodels.columna.fechaaviso"),
		I18N.get("LocalGISObraCivil","localgisgestionciudad.panels.avisos.tablemodels.columna.retraso"),
		I18N.get("LocalGISObraCivil","localgisgestionciudad.panels.avisos.tablemodels.columna.ficheros"),
	};

	public AvisoCompletoTableModel() {
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
			return lstElementos.get(row).getActuationType();
		case 3: 
			return lstElementos.get(row).getCauses();
		case 4: 
			return lstElementos.get(row).getStartWarning();
		case 5: 
			return lstElementos.get(row).getNextWarning();
		case 6: 
			return OperacionesConFechasObraCivil.obtenerDuracionVencimiento((GregorianCalendar) lstElementos.get(row).getNextWarning());
		case 7: 
			return lstElementos.get(row).toString();
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
}
