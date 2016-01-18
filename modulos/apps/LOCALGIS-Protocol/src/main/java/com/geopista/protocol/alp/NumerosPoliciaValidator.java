/**
 * NumerosPoliciaValidator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.alp;

import java.io.Serializable;
import java.util.Iterator;

import com.geopista.protocol.AbstractValidator;
import com.geopista.server.administradorCartografia.ACAttribute;
import com.geopista.server.administradorCartografia.ACFeature;
import com.geopista.server.administradorCartografia.ACFeatureUpload;
import com.geopista.server.administradorCartografia.IACLayer;

public class NumerosPoliciaValidator extends AbstractValidator{

	public NumerosPoliciaValidator(){
		
	}
	
	public void afterInsert(ACFeatureUpload uploadFeature, IACLayer acLayer){
		System.out.println("Posterior a la insercciï¿½n");
		//Situar aquï¿½ la llamada al cliente de los servicios web de ALP
		ACFeature acFeature=new ACFeature();
        ACAttribute att=null;
        Object oAttValue=null;
        for (Iterator it=acLayer.getAttributes().values().iterator();it.hasNext();){
            att=(ACAttribute)it.next();
            try
            {
//               oAttValue=rs.getObject(att.getColumn().getName());
            	int index = att.getPosition() - 1;
               acFeature.setAttribute(att.getName(),(Serializable) uploadFeature.getAttValues()[index]);
            }catch(Exception ex)
            {
//                logger.error("No se puede cargar la columna: "+att.getColumn().getName(),ex);
            }
        }
        System.out.println("Posterior a la insercciï¿½n");
	}

}
