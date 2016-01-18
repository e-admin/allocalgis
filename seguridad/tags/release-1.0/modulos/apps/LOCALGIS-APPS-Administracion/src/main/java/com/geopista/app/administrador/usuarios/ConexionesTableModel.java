package com.geopista.app.administrador.usuarios;

import com.geopista.protocol.administrador.Conexion;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.util.Enumeration;
import java.text.SimpleDateFormat;

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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 06-oct-2004
 * Time: 15:05:47
 */
public class ConexionesTableModel extends DefaultTableModel {

	public static final int idIndex=0;
	
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ConexionesTableModel.class);
    private static String[] tableModelNames = new String[] { "ID_CONEXION", "ID_APP", "FECHA INICIO", "FECHA FIN"};

    public void setModelData(Vector listaConexiones) {
        try
        {
            if (listaConexiones==null)return;
            for (Enumeration e=listaConexiones.elements();e.hasMoreElements();) {
			    Conexion auxConexion =(Conexion) e.nextElement();
                SimpleDateFormat formatter = new SimpleDateFormat ("dd-MM-yyyy HH:mm:ss");
                String fechaInicio =(auxConexion.getFechaInicio()==null?"":formatter.format(auxConexion.getFechaInicio()));
                String fechaFin =(auxConexion.getFechaFin()==null?"":formatter.format(auxConexion.getFechaFin()));
                Object row[] = new Object[] {auxConexion.getIdConexion(), auxConexion.getIdApp(), fechaInicio, fechaFin, auxConexion.getIdConexion()};
			    this.addRow(row);
	    }

	    fireTableDataChanged();


        }catch(Exception e)
        {
            logger.error("Error al poner la lista de conexiones: "+ e.toString());
        }
	}



	public int getColumnCount() {
		return tableModelNames.length;
	}

	public String getColumnName(int c) {
		return tableModelNames[c];
	}
    public String setColumnName(int c, String sName) {
        return tableModelNames[c]=sName;
    }

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}





}
