/**
 * ParameterList.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.parameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.vividsolutions.jump.util.LangUtil;

/**
 * A strongly-typed list of parameters for passing to a component
 */
public class ParameterList {
    private ParameterListSchema schema;

    private Map params = new HashMap();

    public ParameterList(ParameterListSchema schema) {
        initialize(schema);
    }

    public ParameterList(ParameterList other) {
        initialize(other.getSchema());
        for (Iterator i = Arrays.asList(other.getSchema().getNames()).iterator(); i.hasNext(); ) {
            String name = (String) i.next();
            setParameter(name, other.getParameter(name));
        }
    }

    protected ParameterList initialize(ParameterListSchema schema) {
        this.schema = schema;
        return this;
    }

    public ParameterListSchema getSchema() {
        return schema;
    }

    public ParameterList setParameter(String name, Object value) {
        params.put(name, value);
        return this;
    }

    public boolean equals(Object obj) {
        return equals((ParameterList) obj);
    }

    private boolean equals(ParameterList other) {
        if (!schema.equals(other.schema)) {
            return false;
        }
        for (Iterator i = params.keySet().iterator(); i.hasNext();) {
            String name = (String) i.next();
            if (!LangUtil.bothNullOrEqual(params.get(name), other.params
                    .get(name))) {
                return false;
            }
        }
        return true;
    }

    public Object getParameter(String name) {
        return params.get(name);
    }

    public String getParameterString(String name) {
        return (String) params.get(name);
    }

    public int getParameterInt(String name)
    {
      Object value = params.get(name);
      if (value instanceof String)
        return Integer.parseInt((String) value);
      return ((Integer) params.get(name)).intValue();
    }

}