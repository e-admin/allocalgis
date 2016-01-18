package com.geopista.protocol.control;

import java.util.Hashtable;

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
 * Date: 17-jun-2004
 * Time: 10:37:35
 */
public class ListaSesionesSimple {
     Hashtable<String, SesionSimple> lista = new Hashtable<String, SesionSimple>();

    public ListaSesionesSimple() {
    }

    public ListaSesionesSimple(Hashtable<String, SesionSimple> lista) {
        this.lista = lista;
    }

    public Hashtable<String, SesionSimple> getLista() {
        return lista;
    }

    public void setLista(Hashtable<String, SesionSimple> lista) {
        this.lista = lista;
    }
}
