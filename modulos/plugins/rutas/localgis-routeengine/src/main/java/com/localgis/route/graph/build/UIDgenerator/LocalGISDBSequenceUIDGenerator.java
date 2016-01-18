/**
 * LocalGISDBSequenceUIDGenerator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.build.UIDgenerator;

import java.sql.SQLException;

import org.uva.graph.build.UIDgenerator.DBSequenceUIDGenerator;

import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.util.RouteConnectionFactory;

/**
 * A simple implementation of an UIDgenerator based on DB sequences and similar functions
 * In order to save database queries it tries to reserve blocks of ids.
 *  
 * @author juacas
 */
public class LocalGISDBSequenceUIDGenerator extends DBSequenceUIDGenerator
{
	private RouteConnectionFactory	connectionFactory;
	int					maxReservedUID	=-1;
	private int			blockSize;

	/*public LocalGISDBSequenceUIDGenerator(DataSource ds)
	{
		super(); // Defaults to a non-partitioned numbering
		setBlockSize(1);
		this.dataSource=ds;
	}*/
	public LocalGISDBSequenceUIDGenerator(RouteConnectionFactory connectionFactory){
		super(null);
		this.connectionFactory =connectionFactory;  
	}
	/**
	 * Size of the blocks of IDs reserved in the DataBase
	 * 
	 * @param i
	 */
	public void setBlockSize(int i)
	{
		this.blockSize=i;
	}

	public int getEdgeUniqueID()
	{
		LocalGISNetworkDAO dao = new LocalGISNetworkDAO();
		int i = 0;
		try {
			i = dao.getNextDatabaseIdEdge(connectionFactory.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

	

	
	public int getNodeUniqueID()
	{
		LocalGISNetworkDAO dao = new LocalGISNetworkDAO();
		int i = 0;
		try {
			i = dao.getNextDatabaseIdNode(connectionFactory.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}
	@Override
	protected String getQueryForABlockOfIDs(int n) {
		
		return "";
	}

}
