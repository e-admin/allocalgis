/**
 * InformacionTributaria.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion.xml.contents;

public class InformacionTributaria
{
    //bajo el nodo dcbl

    private String ert = new String();
    private String erp = new String();
    
    /**
     * Constructor por defecto
     *
     */
    public InformacionTributaria(){
        
    }

    /**
     * @return Returns the erp.
     */
    public String getErp()
    {
        return erp;
    }

    /**
     * @param erp The erp to set.
     */
    public void setErp(String erp)
    {
        this.erp = erp;
    }

    /**
     * @return Returns the ert.
     */
    public String getErt()
    {
        return ert;
    }

    /**
     * @param ert The ert to set.
     */
    public void setErt(String ert)
    {
        this.ert = ert;
    }
    
    
}
