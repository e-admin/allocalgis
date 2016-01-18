/**
 * TurnImpedanceListCellRender.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.turnimpedancesplugin.dialog;

import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.uva.geotools.graph.structure.Edge;
import org.uva.route.graph.structure.phantom.basic.EquivalentEdge;
import org.uva.route.network.Network;

import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.TurnImpedance;
import com.localgis.route.network.NetworkProperty;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;

public class TurnImpedanceListCellRender  extends JLabel implements ListCellRenderer
{

	private Network network = null;
	private HashMap<TurnImpedance, String> valuesText = new HashMap<TurnImpedance, String>();
	private HashMap<Integer, String> edgesText = new HashMap<Integer, String>();
	
	public HashMap<Integer, String> getEdgesText(){
		return edgesText;
	}
	
	public void setEdgesText(HashMap<Integer, String> map){
		this.edgesText = map;
	}
	
	
	public HashMap<TurnImpedance, String> getValuesText(){
		return valuesText;
	}
	
	public void setValuesText(HashMap<TurnImpedance, String> map){
		this.valuesText = map;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 5206952892050743631L;
	
	public TurnImpedanceListCellRender(Network network)
	{
		this.network = network;
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
	}

	/* listamos los numeros de policia en el combo */
	public Component getListCellRendererComponent(
			JList list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus)
	{
		if (value==null) return this;


			if (isSelected)
			{
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
				//setBorder(BorderFactory.createLineBorder(Color.red,2));
			}
			else
			{
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			setHorizontalAlignment(LEFT);
			if (value instanceof TurnImpedance){
				setText(getTurnImpedanceText((TurnImpedance) value));
			} else{
				setText(value.toString());
			}
		return this;
	}


	private String getTurnImpedanceText(TurnImpedance turn){
		String resultado = "";
		if (this.valuesText.get(turn)!=null){
			resultado = this.valuesText.get(turn);
		} else{
		resultado = "Giros ";

		ILocalGISEdge startEdge = null;
		ILocalGISEdge endEdge = null;

		Iterator<Edge> it = network.getGraph().getEdges().iterator();
		while (it.hasNext()){
			Edge actualEdge = it.next();
			while(actualEdge instanceof EquivalentEdge){
				actualEdge = (Edge)((EquivalentEdge)actualEdge).getEquivalentTo();
			}
			if (actualEdge.getID() == turn.getIdEdgeEnd()){
				endEdge = (ILocalGISEdge) actualEdge;
			} else if (actualEdge.getID() == turn.getIdEdgeStart()){
				startEdge = (ILocalGISEdge) actualEdge;
			}
		}


		String startDescription = getStreetDescription(startEdge);
		String endDescription = getStreetDescription(endEdge);

		if ( startDescription != null && endDescription != null && 
				!startDescription.equals("") && !endDescription.equals("")){		
			resultado = resultado.concat( 
					"entre '" + startDescription + "' "
					+ "y '"  + endDescription   +"'");
		}

		resultado = resultado.concat(
				"("
				+ turn.getIdEdgeStart() + "|"
				+ turn.getIdEdgeEnd()
				+")");
		
		valuesText.put(turn, resultado);
		}

		return resultado;
	}



	/**
	 * @param startEdge
	 */
	private String getStreetDescription(ILocalGISEdge edge) {
		String columnDescriptor = "";
		String resultado = "";
		try{
			columnDescriptor = ((NetworkProperty) this.network.getProperties().get("ColumnDescriptor")).getValue(
					Integer.toString(edge.getIdLayer())
			);

			if (columnDescriptor != null && !columnDescriptor.equals("")){
				resultado = getColumnsDescriptorFromIdLayer(edge.getIdLayer(), edge.getIdFeature(), columnDescriptor);
			}
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}		
		
		return getTypeStreetDescriptor(edge) +  " " + resultado ;
	}

	private String getTypeStreetDescriptor(ILocalGISEdge edge){
		String typeColumnDescriptor = "";
		String typeDescription = "calle";
		if (edge instanceof LocalGISStreetDynamicEdge){
			try{
				typeColumnDescriptor = ((NetworkProperty) network.getProperties().
						get("TypeColumnDescriptor")).getValue(
								Integer.toString(edge.getIdLayer())
						);

				if (typeColumnDescriptor != null && !typeColumnDescriptor.equals("") && !typeColumnDescriptor.equals("SIN TIPO")){
					typeDescription = getColumnsDescriptorFromIdLayer(edge.getIdLayer(), edge.getIdFeature(), typeColumnDescriptor);
				}
			} catch (Exception e) {
				// TODO: handle exception
				return typeDescription;
			}
		}
		return typeDescription;
	}

	private String getColumnsDescriptorFromIdLayer(int idLayer, int idFeature, String column){

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
		Connection con = connectionFactory.getConnection();
		try {
			String sqlQuery = "SELECT " + column  +" FROM " + getQueryFromIdLayer(con, idLayer) + " resultTable where resultTable.id = " + idFeature ;
			preparedStatement = con.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
			if(rs.next()){ 
				return rs.getString(column);
			}
			preparedStatement.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}

		return "";
	}

	public String getQueryFromIdLayer(Connection con, int idLayer) {

		String unformattedQuery = "";
		String sqlQuery = "select name from layers where id_layer = " + idLayer;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			if (con != null){
				preparedStatement = con.prepareStatement(sqlQuery);
				preparedStatement.setInt(1,idLayer);
				rs = preparedStatement.executeQuery ();

				if(rs.next()){ 
					unformattedQuery = rs.getString("name");
				}
				preparedStatement.close();
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}

		return unformattedQuery;

	}


}