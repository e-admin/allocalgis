package com.geopista.protocol.metadatos;

import com.geopista.protocol.administrador.dominios.DomainNode;

import java.util.Date;
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
 * Time: 10:10:36
 */
public class CI_Date {
    String date_id;
    Date date;
    DomainNode tipo;

    public CI_Date() {
    }

    public CI_Date(Date date, DomainNode tipo) {
        this.date = date;
        this.tipo = tipo;
    }

    public CI_Date(String date_id, Date date, DomainNode tipo) {
        this.date = date;
        this.date_id = date_id;
        this.tipo = tipo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDate_id() {
        return date_id;
    }

    public void setDate_id(String date_id) {
        this.date_id = date_id;
    }

    public DomainNode getTipo() {
        return tipo;
    }

    public void setTipo(DomainNode tipo) {
        this.tipo = tipo;
    }

    public void setTipo(String sIdTipo, String sPatron)
    {
        DomainNode auxNode=new DomainNode();
        auxNode.setIdNode(sIdTipo);
        auxNode.setPatron(sPatron);
        setTipo(auxNode);
    }
    public String toString()
    {
        if (date!=null)
        {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            return df.format(date)+"["+(tipo!=null?tipo.getPatron():"nulo")+"]";
        }
        else
            return "";
    }

}
