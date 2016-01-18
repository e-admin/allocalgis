/**
 * LocalGISBasicVirtualNode.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.util;

import org.uva.route.graph.structure.VirtualNode;
import org.uva.route.graph.structure.dynamic.DynamicGeographicNode;
import org.uva.route.graph.structure.dynamic.DynamicGraph;


public class LocalGISBasicVirtualNode extends DynamicGeographicNode implements VirtualNode{
	
		/**
	 * 
	 */
	private static final long serialVersionUID = 3073472124849966388L;
		DynamicGraph graph;
		/**
		 * Create a Virtual Node. put persistent flag to false so that it won't be saved into
		 * store
		 */
		public LocalGISBasicVirtualNode(int id)
		{
			super(id);
			this.setPersistent(false);
		}

		@Override
		public DynamicGraph getDynamicGraph() {
			return this.graph;
		}

		@Override
		public void setGraph(DynamicGraph graph) {
			this.graph = graph;
		}

	}


