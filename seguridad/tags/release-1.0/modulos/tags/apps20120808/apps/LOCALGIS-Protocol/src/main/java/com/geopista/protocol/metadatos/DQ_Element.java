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
 * Date: 25-ago-2004
 * Time: 14:49:22
 */
public class DQ_Element {
    String id;
    String subclass_id;
    String valueunit;
    String value;
    CI_Citation citation;
    String explanation;
    boolean pass;

    public DQ_Element() {
    }

    public CI_Citation getCitation() {
        return citation;
    }

    public void setCitation(CI_Citation citation) {
        this.citation = citation;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public String getSubclass_id() {
        return subclass_id;
    }

    public void setSubclass_id(String subclass_id) {
        this.subclass_id = subclass_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueunit() {
        return valueunit;
    }

    public void setValueunit(String valueunit) {
        this.valueunit = valueunit;
    }
}
