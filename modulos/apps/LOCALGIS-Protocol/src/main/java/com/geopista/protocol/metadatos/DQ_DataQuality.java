/**
 * DQ_DataQuality.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.metadatos;

import java.util.Enumeration;
import java.util.Vector;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 25-ago-2004
 * Time: 14:47:34
 */
public class DQ_DataQuality {
    String id;
    LI_Linage linage;
    Vector elements;

    public DQ_DataQuality() {
    }

    public Vector getElements() {
        return elements;
    }

    public void setElements(Vector element) {
        this.elements = element;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LI_Linage getLinage() {
        return linage;
    }

    public void setLinage(LI_Linage linage) {
        this.linage = linage;
    }
    public void addElement(DQ_Element element)
    {
        if (elements==null) elements= new Vector();
        elements.add(element);
    }
    public DQ_Element getElement(String sCodeElement)
    {
        if ((elements==null) || (sCodeElement==null)) return null;
        for (Enumeration e=elements.elements();e.hasMoreElements();)
        {
            DQ_Element auxElement=(DQ_Element)e.nextElement();
            if (sCodeElement.equals(auxElement.getId()))
                return auxElement;

        }
        return null;
    }
    public DQ_Element getElementByTitle(String sTitle)
    {
        if ((elements==null) || (sTitle==null)) return null;
        for (Enumeration e=elements.elements();e.hasMoreElements();)
        {
            DQ_Element auxElement=(DQ_Element)e.nextElement();
            if (auxElement.getCitation()!=null)
                if (sTitle.equals(auxElement.getCitation().getTitle()))
                    return auxElement;

        }
        return null;
        }
   public void removeElement(DQ_Element element)
   {
        if ((elements==null)||(element==null)) return;
        elements.remove(element);
   }

}
