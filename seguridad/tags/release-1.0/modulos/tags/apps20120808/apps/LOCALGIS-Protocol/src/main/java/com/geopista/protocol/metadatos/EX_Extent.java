package com.geopista.protocol.metadatos;

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
 * Date: 24-ago-2004
 * Time: 15:50:55
 */
public class EX_Extent {
    String extent_id;
    String description;
    EX_VerticalExtent vertical;
    EX_GeographicBoundingBox box;

    public EX_Extent() {
    }

    public EX_GeographicBoundingBox getBox() {
        return box;
    }

    public void setBox(EX_GeographicBoundingBox box) {
        this.box = box;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtent_id() {
        return extent_id;
    }

    public void setExtent_id(String extend_id) {
        this.extent_id = extend_id;
    }

    public EX_VerticalExtent getVertical() {
        return vertical;
    }

    public void setVertical(EX_VerticalExtent vertical) {
        this.vertical = vertical;
    }
}
