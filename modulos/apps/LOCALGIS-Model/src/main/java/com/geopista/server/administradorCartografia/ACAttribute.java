/**
 * ACAttribute.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.Serializable;

import com.geopista.feature.Attribute;
import com.geopista.feature.Column;


/** Datos de atributo para el interfaz con el Administrador de Cartografia */
public class ACAttribute implements Serializable{
    int id;
    String name;
    IACColumn column;
    int position;
    boolean editable;

    public int getId() {
        return id; 
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IACColumn getColumn() {
        return column;
    }

    public void setColumn(IACColumn column) {
        this.column = column;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /** Obtiene un atributo de Jump */
    public Attribute convert(Column c){
        Attribute aRet=new Attribute();
        aRet.setName(this.name);
        if (!editable)
            aRet.setAccessType("R");
        return aRet;
    }
}
