/**
 * EntidadSupramunicipalComparator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * GeopistaMunicipioComparator.java
 *
 * Created on 8 de octubre de 2007, 14:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.localgis.web.wm.util;

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Comparator;
import com.localgis.web.wm.struts.beans.EntidadSupramunicipalBean;

/**
 *
 * @author arubio
 */

public class EntidadSupramunicipalComparator implements Comparator{
        

        public int compare(Object o1, Object o2) {
            EntidadSupramunicipalBean entidad,other; 
            entidad = (EntidadSupramunicipalBean) o1;
            other = (EntidadSupramunicipalBean) o2;
            Collator collator = RuleBasedCollator.getInstance();
            return collator.compare(entidad.getName(),other.getName());
        }

}
