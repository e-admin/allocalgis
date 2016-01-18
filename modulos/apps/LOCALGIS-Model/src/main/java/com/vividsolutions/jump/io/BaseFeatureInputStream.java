/**
 * BaseFeatureInputStream.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.io;

import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;

/**
 * Base class for FeatureInputStreamReaders.
 * Handles the details of buffering the stream of features
 * to allow for lookahead.
 * This allows subclasses to implement the simpler semantics
 * of "return null if no more features".
 *
 * Subclasses need to define readNext and close.
 * They also need to set the featureSchema instance variable.
 */
public abstract class BaseFeatureInputStream
    implements FeatureInputStream
{
  private Feature nextFeature = null;

  public abstract FeatureSchema getFeatureSchema();

  public Feature next()
      throws Exception
  {
    if (nextFeature == null)
      return readNext();
    Feature currFeature = nextFeature;
    nextFeature = null;
    return currFeature;
  }

  public boolean hasNext()
      throws Exception
  {
    if (nextFeature == null) {
        nextFeature = readNext();
    }
    return nextFeature != null;
  }

  /**
   * Read the next feature, if any.
   *
   * @return the next Feature, or <code>null</code> if there is none
   * @throws Exception
   */
  protected abstract Feature readNext()        throws Exception;

  public abstract void close()      throws Exception;
}