/**
 * DataStoreDriver.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.datastore;

import com.vividsolutions.jump.parameter.ParameterList;
import com.vividsolutions.jump.parameter.ParameterListSchema;

/**
 * A driver for a given type of datastore
 */
public interface DataStoreDriver {
    public static final Object REGISTRY_CLASSIFICATION = DataStoreDriver.class
            .getName();

    String getName();

    ParameterListSchema getParameterListSchema();

    DataStoreConnection createConnection(ParameterList params) throws Exception;

    /**
     * @return a description of the driver
     */
    public String toString();
    
    boolean isAdHocQuerySupported();    
}