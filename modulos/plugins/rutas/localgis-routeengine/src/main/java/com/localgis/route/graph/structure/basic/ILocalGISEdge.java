/**
 * ILocalGISEdge.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.structure.basic;

import java.util.Set;

import org.uva.geotools.graph.structure.Node;
import org.uva.route.graph.structure.dynamic.IDynamicEdge;
import org.uva.route.graph.structure.impedance.EdgeImpedance;
import org.uva.routeserver.street.Incident;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.Feature;

/**
 * @author rubengomez
 */

public interface ILocalGISEdge extends IDynamicEdge
{

	public void setImpedanceAToB(double impedanceAToB);

	public void setImpedanceBToA(double impedanceBToA);
	
	public void setImpedanceBidirecccional(double impedance);
	
	public void setImpedanceAToB(EdgeImpedance impedanceAToB);

	public void setImpedanceBToA(EdgeImpedance impedanceBToA);
	
	public void setImpedanceBidirecccional(EdgeImpedance impedance);
	
	public void setImpedance(Node from, EdgeImpedance impedance);

	public int getIdFeature();

	public void setIdFeature(int idFeature) ;

	public int getIdLayer();
	
	public void setIdLayer(int idLayer);
	
	public Set<Incident> getIncidents();

	public void putIncident(Incident incident);
	
	public void setEdgeLength (double length );
	
	public double getEdgeLength ();
	
	public int getIdNetworkNodeA();
	
	public int getIdNetworkNodeB();

	public abstract Geometry getGeometry();

	public abstract void setGeometry(Geometry geom);

	public abstract void setFeature(Feature feature);

	public abstract Feature getFeature();
/**
 * Intenta mapear el atributo pasado a alguno de los campos internos del Edge
 * Debe ignorar silenciosamente los errores.
 * @param attributeName
 * @param attribute
 * @return true si ha conseguido asignarlo, false si lo ha ignorado
 */
	public boolean setAttribute(String attributeName, Object attribute);
}
