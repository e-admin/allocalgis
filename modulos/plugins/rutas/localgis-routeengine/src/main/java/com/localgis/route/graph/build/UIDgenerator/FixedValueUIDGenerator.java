/**
 * FixedValueUIDGenerator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 *    IDELabRoute - An Open Source Java Graph analysis and route server
 *    http://idelab.uva.es/dev/routeserver
 *
 *    (C) 2009, IDELab University of Valladolid
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    General Public License for more details.
 */
package com.localgis.route.graph.build.UIDgenerator;

import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;


/**
 * Dumb UIDgenerator that only serves one id.
 * It is used to avoid the use of constructors using prefixed Id.
 * 
 * @author juacas
 */
public class FixedValueUIDGenerator extends SequenceUIDGenerator
{
	/**
	 * Instantiates a new fixed value uid generator.
	 * 
	 * @param id the id
	 */
	public FixedValueUIDGenerator(int id)
	{
		super();
		this.setMaxId(id);
		this.setSeq(id);
	}
}