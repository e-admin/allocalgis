package com.geopista.app.administrador.dominios;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.administrador.init.Constantes;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.administrador.dominios.ListaDomainNode;

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
 * Date: 11-jun-2004
 * Time: 16:14:42
 */
public class CodeBookTableModel extends DefaultTableModel {
    public static final int idIndex=0;
    public static final int idNombre=1;

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CodeBookTableModel.class);
    private static String[] codeBookModelNames = new String[] { "ID","Patron","Descripcion"};


    public void setModelData(ListaDomainNode listaDomainNode) {
        try
        {
            if (listaDomainNode!=null)
            {
                Hashtable auxModel= listaDomainNode.gethDom();
                for (Enumeration e=auxModel.elements();e.hasMoreElements();) {
			        DomainNode nodo =(DomainNode) e.nextElement();
			        Object row[] = new Object[] { nodo.getIdNode(),nodo.getPatron(),nodo.getTerm(Constantes.Locale)};
			        this.addRow(row);
                }
		    }

	    	fireTableDataChanged();


        }catch(Exception e)
        {
            logger.error("Error al poner la lista de roles: "+ e.toString());
        }
	}



	public int getColumnCount() {
		return codeBookModelNames.length;
	}

	public String getColumnName(int c) {
		return codeBookModelNames[c];
	}
    public String setColumnName(int c, String sName) {
        return codeBookModelNames[c]=sName;
    }

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}





}

