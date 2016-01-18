/**
 * SpatialReferenceSystemID.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.datastore;

/**
 * Represents an ID used by DataStores to refer to a
 * particular Spatial Reference System.
 * Often an integer value.
 */
public class SpatialReferenceSystemID {

  private String sridString = null;

  public SpatialReferenceSystemID() {
  }

  public SpatialReferenceSystemID(String sridString) {
    this.sridString = sridString;
  }

  public SpatialReferenceSystemID(int srid) {
    this.sridString = Integer.toString(srid);
  }

  public int getInt() {
    if (sridString == null)
      return -1;
    // could do something cleverer here, like try and extract an integer
    return Integer.parseInt(sridString);
  }

  public String getString()
  {
    return sridString;
  }

  public boolean isNull() { return sridString == null; }
}