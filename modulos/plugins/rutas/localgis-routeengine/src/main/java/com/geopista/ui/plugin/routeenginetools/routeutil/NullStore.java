/**
 * NullStore.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil;
import java.io.IOException;
import java.util.ArrayList;

import org.uva.geotools.graph.io.GraphReaderWriter;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.basic.BasicGraph;
import org.uva.route.graph.io.GraphableReaderWriter;
import org.uva.routeserver.ElementNotFoundException;
/**
 * Devuelve siempre un Graph vacío.
 * @author juacas
 *
 */
public class NullStore implements GraphReaderWriter, GraphableReaderWriter
{

    @Override
    public void deleteAll() throws IOException
    {
    }

    @Override
    public Object getProperty(String arg0)
    {
	return null;
    }

    @Override
    public Graph read() throws IOException
    {
	return new BasicGraph(new ArrayList(),new ArrayList());
    }

    @Override
    public void setProperty(String arg0, Object arg1)
    {
    }

    @Override
    public void write(Graph arg0) throws IOException
    {
    }

    @Override
    public int getNodeUniqueID()
    {
	return 0;
    }

    @Override
    public int getEdgeUniqueID()
    {
	return 0;
    }

    @Override
    public Graph read(int nodeId) throws IOException, ElementNotFoundException
    {
	return read();
    }

    @Override
    public int getNodeCount() throws IOException
    {
	return 0;
    }

    @Override
    public int getEdgeCount() throws IOException
    {
	return 0;
    }

    @Override
    public Graph readByEdge(Integer edgeId) throws IOException, ElementNotFoundException
    {
	return null;
    }

    @Override
    public Graph update(Graph dirtyGraph) throws IOException
    {
	throw new IOException("Las aceras han sido generadas, recuerde grabar la red si es necesario.");
	//return dirtyGraph;
    }

    @Override
    public void remove(Graph g) throws IOException
    {	
    }

}
