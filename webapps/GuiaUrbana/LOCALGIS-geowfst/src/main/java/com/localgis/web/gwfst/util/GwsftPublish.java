/**
 * GwsftPublish.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.localgis.web.core.gwfst.functions.GwfstPublish;
import com.localgis.web.core.wm.functions.AutoPublish;

public class GwsftPublish {
	
	private static Log logger = LogFactory.getLog(GwsftPublish.class);
	
	public static Integer gwfstPublish(Integer idEntidad, String procedureId, String mapName){
		return GwsftPublish.gwfstPublish(idEntidad, procedureId, mapName, false);
	}
	
	public static Integer gwfstPublish(Integer idEntidad, String procedureId, String mapName, Boolean publicMap){
		Integer idMap = null;
		Integer idMapPublish = null;
		idMapPublish = AutoPublish.isTypePublish(idEntidad, mapName, publicMap);		
		if(idMapPublish==null){ //Si no está publicado
			//Si no está publicado
			//MapServer
			idMapPublish = AutoPublish.mapTypeRelationPublish(idEntidad, mapName, publicMap);
			//Geoserver
			//idMapPublish = com.localgis.web.core.wm.geoserver.functions.AutoPublish.mapTypeRelationPublish(idEntidad, procedureId, publicMap);
		} 	
		if(idMapPublish!=null){	
			idMap = GwfstPublish.getMapTypeRelation(idEntidad, procedureId);	
			if(idMap==null){		
				//Se inserta el registro relacional
				GwfstPublish.insertMapTypeRelation(idEntidad, procedureId,idMapPublish);
			}else if(!idMapPublish.equals(idMap)){
				GwfstPublish.updateMapTypeRelation(idEntidad, procedureId,idMapPublish);
			}
			idMap = idMapPublish;
		}
		return idMap;
	}
		
}
