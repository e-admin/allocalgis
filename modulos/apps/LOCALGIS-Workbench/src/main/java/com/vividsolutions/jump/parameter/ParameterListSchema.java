/**
 * ParameterListSchema.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * The schema for a {@link ParameterList}.
 * Parameter names should be case-retentive (i.e. comparisons are case-insensitive,
 * but the case is maintained).
 */
public class ParameterListSchema {

  private String[] paramNames;
  private Class[] paramClasses;
  private Map nameMap = new HashMap();

  public ParameterListSchema(String[] paramNames, Class[] paramClasses) {
    initialize(paramNames, paramClasses);
  }

  protected ParameterListSchema initialize(String[] paramNames, Class[] paramClasses) {
    this.paramNames = paramNames;
    this.paramClasses = paramClasses;

    for (int i = 0; i < paramNames.length; i++) {
      nameMap.put(paramNames[i], new Integer(i));
    }
    return this;
  }

  public String[] getNames()  { return paramNames; }
  public Class[] getClasses()  { return paramClasses; }
  public boolean isValidName(String name)
  {
    return nameMap.containsKey(name);
  }
  
  public boolean equals(Object obj) {      
    return equals((ParameterListSchema)obj);
  }
  private boolean equals(ParameterListSchema other) {
    if (paramNames.length != other.paramNames.length) { return false; }
    for (int i = 0; i < paramNames.length; i++) {
      if (!paramNames[i].equals(other.paramNames[i])) { return false; }    
    }
    for (int i = 0; i < paramNames.length; i++) {
      if (paramClasses[i] != other.paramClasses[i]) { return false; }    
    }    
    return true;
  }

  public Class getClass(String name) {
    return paramClasses[((Integer)nameMap.get(name)).intValue()];
  }
}