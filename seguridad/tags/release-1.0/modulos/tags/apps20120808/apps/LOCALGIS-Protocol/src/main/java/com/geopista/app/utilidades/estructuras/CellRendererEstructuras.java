package com.geopista.app.utilidades.estructuras;

import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.ListaEstructuras;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

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


public class CellRendererEstructuras  extends JLabel implements TableCellRenderer {
    private String locale;
    private ListaEstructuras listaEstructuras;

    public CellRendererEstructuras(String locale, ListaEstructuras listaEstructuras) {

        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        this.locale=locale;
        this.listaEstructuras=listaEstructuras;
    }


    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value==null) return this;
        //Get the selected index. (The index param isn't
        //always valid, so just use the value.)

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
            //setBorder(BorderFactory.createLineBorder(Color.red,2));
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        setHorizontalAlignment(LEFT);
        if (value==null)
        {
            setText("        ");
            return this;
        }
        if (value instanceof DomainNode)
            setText((((DomainNode)value).getTerm(locale)!=null?((DomainNode)value).getTerm(locale):"            "));
        else if (value instanceof String)
        {
              DomainNode nodo=listaEstructuras.getDomainNode((String)value);
              setText(nodo!=null&&nodo.getTerm(locale)!=null?(nodo).getTerm(locale):"            ");

        }else
        {
            setText(value.toString());
        }

        return this;

    }
}
