/**
 * IGeopistaMap.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 * Created on 03-jun-2005 by juacas
 *
 * 
 */
package com.geopista.model;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jdom.JDOMException;

import com.geopista.editor.TaskComponent;
import com.vividsolutions.jump.workbench.model.ILayerManager;


/**
 * TODO Documentación
 * @author juacas
 *
 */
public interface IGeopistaMap
{
	public abstract ILayerManager getLayerManager();

	/**
	 * @return Returns the isExtracted.
	 */
	public abstract boolean isExtracted();

	/**
	 * @param isExtracted The isExtracted to set.
	 */
	public abstract void setExtracted(boolean isExtracted);

	/**
	 * @return Returns the timeStamp.
	 */
	public abstract Date getTimeStamp();

	/**
	 * @param timeStamp The timeStamp to set.
	 */
	public abstract void setTimeStamp(Date timeStamp);

	public abstract String getDescription();

	public abstract void setDescription(String newDescription);

	/**
	 * @return Returns the thumbnail.
	 */
	public abstract Image getThumbnail();

	public abstract void loadThumbnail();

	/**
	 * @param thumbnail The thumbnail to set.
	 */
	public abstract void setThumbnail(Image thumbnail);

	/**
	 * @return Returns the basePath.
	 */
	public abstract String getBasePath();

	/**
	 * @param basePath The basePath to set.
	 */
	public abstract void setBasePath(String basePath);

	/**
	 * @return Returns the systemId.
	 */
	public abstract String getSystemId();

	/**
	 * @param systemId The systemId to set.
	 */
	public abstract void setSystemId(String systemId);

	public abstract String getMapUnits();

	public abstract void setMapUnits(String newMapUnits);

	public abstract String getMapScale();

	public abstract void setMapScale(String newMapScale);

	public abstract String getMapProjection();

	public abstract void setMapProjection(String newMapProjection);

	public abstract String getDescriptor();

	public abstract void setDescriptor(String xmlDescriptor)
			throws JDOMException, IOException;

	public abstract List getSystemCategories();

	public abstract Collection getLayersStylesRelation();

	/**
	 * @param frame
	 */
	public abstract void setTaskFrame(TaskComponent frame);

	public abstract TaskComponent getTaskComponent();

	public abstract void setOrdersLayer(Map orderLayers);

	public abstract Map getOrdersLayer();
	
	public File getProjectFile();
	
	public String getName();
	
	public int getIdMunicipio();
	
	public int getIdEntidad();
	
	public String getIdEntidadSeleccionada();
	
	public boolean isSystemMap();
	
	
	
	
}