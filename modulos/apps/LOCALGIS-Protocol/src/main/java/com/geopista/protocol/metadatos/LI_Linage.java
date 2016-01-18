/**
 * LI_Linage.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.metadatos;

import java.util.Vector;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 25-ago-2004
 * Time: 14:45:36
 */
public class LI_Linage {
    String id;
    String statement;
    Vector steps;
    Vector sources;

    public LI_Linage() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Vector getSources() {
        return sources;
    }

    public void setSources(Vector sources) {
        this.sources = sources;
    }

    public void addSource(String source)
    {
        if (this.sources==null) this.sources= new Vector();
        this.sources.add(source);
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public Vector getSteps() {
        return steps;
    }

    public void setSteps(Vector steps) {
        this.steps = steps;
    }
    public void addStep(String step)
      {
          if (this.steps==null) this.steps= new Vector();
          this.steps.add(step);
      }

}
