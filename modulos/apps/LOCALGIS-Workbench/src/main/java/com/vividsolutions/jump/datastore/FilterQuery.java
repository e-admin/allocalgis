/**
 * FilterQuery.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.datastore;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A spatial filter {@link Query} on a {@link DataStoreConnection}.
 */
public class FilterQuery
    implements Query
{

  private String datasetName;
  private String[] propertyNames;
  private Geometry geom;
  private String condition;
  /**
   * Not all query processors need this.
   */
  private String geomAttrName = null;
  /**
   * For those query processors which need a CRS
   */
  private SpatialReferenceSystemID srid = new SpatialReferenceSystemID();

  public FilterQuery() {
  }

  public void setDatasetName(String datasetName) { this.datasetName = datasetName; }
  public String getDatasetName() { return datasetName; }
  public void setPropertyNames(String[] propertyNames) { this.propertyNames = propertyNames; }
  public String[] getPropertyNames() { return propertyNames; }
  public void setFilterGeometry(Geometry geom) { this.geom = geom; }
  public Geometry getFilterGeometry() { return geom; }
  public void setCondition(String condition) { this.condition = condition; }
  public String getCondition() { return condition; }

  public void setGeometryAttributeName(String geomAttrName) { this.geomAttrName = geomAttrName; }
  public String getGeometryAttributeName() { return geomAttrName; }

  /**
   * Sets the SpatialReferenceSystem for a query.
   * This is optional; whether it is required depends on the datastore implemention.
   * Datastore drivers may set this themselves
   * and override any user settings.
   *
   * @param srid the SpatialReferenceSystem ID
   */
  public void setSRSName(SpatialReferenceSystemID srid) { this.srid = srid; }
  public SpatialReferenceSystemID getSRSName() { return srid; }
}