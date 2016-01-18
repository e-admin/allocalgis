package com.geopista.app.metadatos.busqueda;

import com.geopista.protocol.metadatos.MD_MetadataMini;

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
 * Date: 23-ago-2004
 * Time: 16:40:57
 */
public class MetadatosTableModel   extends DefaultTableModel {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MetadatosTableModel.class);
    private static String[] metadatosModelNames = new String[] { "ID","Titulo", "Responsable","Fecha","Resumen"};

    public void setModelData(Vector listaMetadatos) {
        try
        {
            for (Enumeration e=listaMetadatos.elements();e.hasMoreElements();) {
			    MD_MetadataMini auxMetadataMini =(MD_MetadataMini) e.nextElement();
                SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy");
                String sFecha =df.format(auxMetadataMini.getFecha());
			    Object row[] = new Object[] { auxMetadataMini.getId(),auxMetadataMini.getTitulo(),
                                              auxMetadataMini.getNombre()+
                                             (auxMetadataMini.getOrganizacion()!=null&&auxMetadataMini.getOrganizacion().length()>0?" ["+auxMetadataMini.getOrganizacion()+"] ":"")+
                                              (auxMetadataMini.getCargo()!=null&&auxMetadataMini.getCargo().length()>0?" ["+auxMetadataMini.getCargo()+"] ":""),
                                              sFecha,auxMetadataMini.getResumen()};
                this.addRow(row);
		    }
	    	fireTableDataChanged();
        }catch(Exception e)
        {
            logger.error("Error al poner la lista parcial de metadatos: "+ e.toString());
        }
	}



	public int getColumnCount() {
		return metadatosModelNames.length;
	}

	public String getColumnName(int c) {
		return metadatosModelNames[c];
	}
    public String setColumnName(int c, String sName) {
        return metadatosModelNames[c]=sName;
    }

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}
