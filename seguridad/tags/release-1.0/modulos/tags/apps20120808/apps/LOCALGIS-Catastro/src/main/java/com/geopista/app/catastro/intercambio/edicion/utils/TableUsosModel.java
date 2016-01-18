/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 *
 */

package com.geopista.app.catastro.intercambio.edicion.utils;

/**
 * Modelo para mostrar los locales en las tablas de locales
 * 
 * @author cotesa
 *
 */

import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.UsoCatastro;
import com.vividsolutions.jump.I18N;

public class TableUsosModel extends AbstractTableModel {

	private final String[] columnNames = {
			I18N.get("Expedientes", "tabla.usos.columna.codigo"),
			I18N.get("Expedientes", "tabla.usos.columna.superficie") };

	private ArrayList lstElementos = new ArrayList();

	// suscriptores de los cambios
	private final LinkedList suscriptores = new LinkedList();

	public void addTableModelListener(TableModelListener l) {
		suscriptores.add(l);
	}

	public void removeTableModelListener(TableModelListener l) {
		suscriptores.remove(l);
	}

	/**
	 * @return número de columnas de la tabla
	 */
	public int getColumnCount() {
		return columnNames.length;
	}

	/**
	 * @return número de filas de la tabla
	 */
	public int getRowCount() {
		return lstElementos.size();
	}

	/**
	 * Devuelve el nombre de la columna solicitada
	 * 
	 * @param col
	 *            Índice de la columna
	 * @return nombre de la columna
	 */
	public String getColumnName(int col) {
		return columnNames[col];
	}

	/**
	 * Devuelve el objeto que contiene la celda en la posición indicada
	 * 
	 * @param row
	 *            Índice de la fila
	 * @param col
	 *            Índice de la columna
	 * 
	 * @return Objeto contenido en la posición seleccionada
	 */
	public Object getValueAt(int row, int col) {

		if (lstElementos.get(row) == null)
			return null;

		switch (col) {

		case 0:
			return ((UsoCatastro) lstElementos.get(row)).getCodigoUso();
		case 1:
			return ((UsoCatastro) lstElementos.get(row))
					.getSuperficieDestinada();
		default:
			return null;

		}
	}

	/**
	 * Devuelve la ConstruccionCatastro completa de la fila seleccionada
	 * 
	 * @param row
	 *            Índice de la fila cuyo objeto ConstruccionCatastro se solicita
	 * @return ConstruccionCatastro completa
	 */
	public ConstruccionCatastro getValueAt(int row) {

		return (ConstruccionCatastro) lstElementos.get(row);

	}

	/**
	 * JTable uses this method to determine the default renderer/ editor for
	 * each cell.
	 */
	public Class getColumnClass(int c) {
		if (getValueAt(0, c) != null)
			return getValueAt(0, c).getClass();
		else
			return String.class;
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

	/**
	 * Establece los datos mostrados en el modelo
	 * 
	 * @param datos
	 *            Datos a mostrar en el modelo
	 */
	public void setData(ArrayList datos) {
		this.lstElementos = datos;
	}

	/**
	 * Recupera los datos del modelo
	 * 
	 * @return Datos del modelo
	 */
	public ArrayList getData() {
		return lstElementos;
	}

	public boolean anniadirUso(UsoCatastro uso) {
		try {
			boolean result = lstElementos.add(uso);

			// Avisa a los suscriptores creando un TableModelEvent...
			TableModelEvent evento;
			evento = new TableModelEvent(this, this.getRowCount() - 1, this
					.getRowCount() - 1, TableModelEvent.ALL_COLUMNS,
					TableModelEvent.INSERT);

			for (int i = 0; i < suscriptores.size(); i++)
				((TableModelListener) suscriptores.get(i)).tableChanged(evento);

			return result;

		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
			return false;
		}
	}

	public boolean borrarUso(UsoCatastro uso) {

		try {
			// Se borra la fila
			int fila = lstElementos.indexOf(uso);
			boolean result = lstElementos.remove(uso);

			// Y se avisa a los suscriptores, creando un TableModelEvent...
			TableModelEvent evento = new TableModelEvent(this, fila, fila,
					TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);

			// ... y pasándoselo a los suscriptores
			int i;

			for (i = 0; i < suscriptores.size(); i++)
				((TableModelListener) suscriptores.get(i)).tableChanged(evento);

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
			TableModelEvent evento = new TableModelEvent(this, fila, fila,
					TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);

			// ... y pasándoselo a los suscriptores
			int i;

			for (i = 0; i < suscriptores.size(); i++)
				((TableModelListener) suscriptores.get(i)).tableChanged(evento);

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
			TableModelEvent evento = new TableModelEvent(this);

			// ... y pasándoselo a los suscriptores
			for (int m = 0; m < suscriptores.size(); m++)
				((TableModelListener) suscriptores.get(m)).tableChanged(evento);

			return true;
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
}