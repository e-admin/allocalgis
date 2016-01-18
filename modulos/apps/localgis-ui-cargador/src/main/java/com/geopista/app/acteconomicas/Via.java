/**
 * Via.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.acteconomicas;



import java.util.Vector;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 06-jun-2006
 * Time: 13:08:43
 */
public class Via {
   int id;
   String tipoviaine;
   String nombreviaine;
   Vector numerosPolicia;

    public Via( int id, String tipoviaine, String nombreviaine) {
        this.nombreviaine = nombreviaine;
        this.id = id;
         this.tipoviaine = tipoviaine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreviaine() {
        return nombreviaine;
    }

    public void setNombreviaine(String nombreviaine) {
        this.nombreviaine = nombreviaine;
    }

    public Vector getNumerosPolicia() {
        return numerosPolicia;
    }

    public void setNumerosPolicia(Vector numerosPolicia) {
        this.numerosPolicia = numerosPolicia;
    }

    public String getTipoviaine() {
        return tipoviaine;
    }

    public void setTipoviaine(String tipoviaine) {
        this.tipoviaine = tipoviaine;
    }
    public void addNumeroPolicia(NumeroPolicia numeroPolicia)
    {
        if (numerosPolicia==null) numerosPolicia=new Vector();
        numerosPolicia.add(numeroPolicia);
    }
    public String toString()
    {
        return nombreviaine!=null?nombreviaine:"";
    }
}
