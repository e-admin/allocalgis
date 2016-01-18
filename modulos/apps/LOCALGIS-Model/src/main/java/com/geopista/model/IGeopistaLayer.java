/**
 * IGeopistaLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.model;

import com.geopista.ui.plugin.LogFeatutesEvents;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.model.ILayer;
import com.vividsolutions.jump.workbench.model.ILayerManager;


public interface IGeopistaLayer extends ILayer {

	public abstract int getId_LayerDataBase();

	public abstract void setId_LayerDataBase(int id_LayerDataBase);

	public abstract int getId_layer();

	public abstract void setId_layer(int id_layer);

	/**
	 * Nos indica si la capa esta activa
	 */
	public abstract boolean isActiva();

	public abstract void setActiva(boolean activa);

	public abstract String getFieldExtendedForm();

	public abstract void setFieldExtendedForm(String newFieldExtendedForm);

	/**
	 * @return Returns the systemId.
	 */
	public abstract String getSystemId();

	/**
	 * @param systemId
	 *            The systemId to set.
	 */
	public abstract void setSystemId(String systemId);

	public abstract void setName(String name);

	/**
	 * Editability is not enforced; all parties are responsible for heeding this
	 * flag.
	 */

	public abstract void setEditable(boolean editable);

	public abstract boolean isLocal();

	public abstract void setLocal(boolean newIsLocal);

	/**
	 * @return Returns the isExtracted.
	 */
	public abstract boolean isExtracted();

	/**
	 * @param isExtracted
	 *            The isExtracted to set.
	 */
	public abstract void setExtracted(boolean isExtracted);

	/**
	 * @return Returns the logFeatutesEvents.
	 */
	public abstract LogFeatutesEvents getLogger();

	public abstract void activateLogger(IGeopistaMap map) throws Exception;

	public abstract void activateLogger(String logFilePath) throws Exception;

	/**
	 * @deprecated
	 * Falta soluionar el problema de que el en metodo setattribute utilice la matriz de atribitos
	 * antigua para introducir en la nueva las valores que son de no lectura, con lo que si la 
	 * nueva matriz en mas grande o a cambiado de orden los atributos del esquema falla
	 * 
	 * @param newSchema
	 * @throws ConversionException
	 */
	public abstract void changeGeopistaSchema(FeatureSchema newSchema)
			throws ConversionException;

	public abstract Feature convert(Feature oldFeature, FeatureSchema newSchema)
			throws ConversionException;

	public abstract void dispose();

	public abstract boolean isVersionable();

	public abstract void setVersionable(boolean isVersionable);

	public abstract long getRevisionActual();

	public abstract void setRevisionActual(long revisionActual);

	public abstract long getUltimaRevision();

	public abstract void setUltimaRevision(long ultimaRevision);
	
	public ILayerManager getLayerManager();

}