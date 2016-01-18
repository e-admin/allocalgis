package com.geopista.protocol.metadatos;

import java.util.Vector;

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
 * Date: 20-ago-2004
 * Time: 14:33:40
 */
public class CI_Citation {
    String Citation_id;
    String Title;
    Vector CI_Dates;

    public CI_Citation() {
    }

    public Vector getCI_Dates() {
        return CI_Dates;
    }

    public void setCI_Dates(Vector CI_Dates) {
        this.CI_Dates = CI_Dates;
    }

    public String getCitation_id() {
        return Citation_id;
    }

    public void setCitation_id(String citation_id) {
        Citation_id = citation_id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
    public void addCI_Date(CI_Date date)
    {
       if (CI_Dates==null) CI_Dates = new Vector();
       CI_Dates.add(date);
    }



}
