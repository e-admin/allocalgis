/**
 * GetExpedientesParcelaSQL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.tools.expedientesparcela;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.tools.expedientesparcela.vo.Estado;
import com.geopista.ui.plugin.tools.expedientesparcela.vo.TipoExpediente;

/**
 * Clase que se utiliza para devolver los tipos y los estados posibles de un expediente.
 * @author fjcastro
 *
 */
public class GetExpedientesParcelaSQL {

	public Connection conexion = null;
	public AppContext aplicacion;
	public Locale loc;
	
	TipoExpediente[] tiposExpedientes;
	Estado[] tiposEstados;
	
	public GetExpedientesParcelaSQL(AppContext aplicacion) {
		
		tiposExpedientes = new TipoExpediente[5];

		for (int i = 0; i <= 4; i++)
			tiposExpedientes[i] = new TipoExpediente();

		
		tiposExpedientes[0].setIdTipoExpediente(GetExpedientesParcelaConstantes.IDTODOS);
		tiposExpedientes[1].setIdTipoExpediente(GetExpedientesParcelaConstantes.IDOBRAMAYOR);
		tiposExpedientes[2].setIdTipoExpediente(GetExpedientesParcelaConstantes.IDOBRAMENOR);
		tiposExpedientes[3].setIdTipoExpediente(GetExpedientesParcelaConstantes.IDACTIVIDADCALIFICADA);
		tiposExpedientes[4].setIdTipoExpediente(GetExpedientesParcelaConstantes.IDACTIVIDADNOCALIFICADA);
		
		tiposExpedientes[1].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYOBRAMAYOR);
		tiposExpedientes[2].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYOBRAMENOR);
		tiposExpedientes[3].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYACTIVIDADCALIFICADA);
		tiposExpedientes[4].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYACTIVIDADNOCALIFICADA);
			
		tiposEstados = new Estado[19];
		
		for (int i = 0; i <= 18; i++)
			tiposEstados[i] = new Estado();

		tiposEstados[0].setIdEstado(GetExpedientesParcelaConstantes.IDESTADOTODOS);
		tiposEstados[1].setIdEstado(GetExpedientesParcelaConstantes.IDESTADOAPERTURAEXPEDIENTE);
		tiposEstados[2].setIdEstado(GetExpedientesParcelaConstantes.IDESTADOMEJORADATOS);
		tiposEstados[3].setIdEstado(GetExpedientesParcelaConstantes.IDESTADOSOLICITUDINFORMES);
		tiposEstados[4].setIdEstado(GetExpedientesParcelaConstantes.IDESTADOSESPERAINFORMES);
		tiposEstados[5].setIdEstado(GetExpedientesParcelaConstantes.IDESTADOSEMISIONINFORMERES);
		tiposEstados[6].setIdEstado(GetExpedientesParcelaConstantes.IDESTADOSESPERAALEGACIONES);
		tiposEstados[7].setIdEstado(GetExpedientesParcelaConstantes.IDESTADOSACTUALIZAINFORMERES);
		tiposEstados[8].setIdEstado(GetExpedientesParcelaConstantes.IDESTADOSEMISIONPROPRES);
		tiposEstados[9].setIdEstado(GetExpedientesParcelaConstantes.IDESTADONOTIFDENEGACION);
		tiposEstados[10].setIdEstado(GetExpedientesParcelaConstantes.IDESTADOSFORMALIZALICENCIA);
		tiposEstados[11].setIdEstado(GetExpedientesParcelaConstantes.IDESTADOSNOTIFICACIONAPROB);
		tiposEstados[12].setIdEstado(GetExpedientesParcelaConstantes.IDESTADOSEJECUCION);
		tiposEstados[13].setIdEstado(GetExpedientesParcelaConstantes.IDESTADOSDURMIENTE);
		tiposEstados[14].setIdEstado(GetExpedientesParcelaConstantes.IDESTADOSPUBLICARBOP);
		tiposEstados[15].setIdEstado(GetExpedientesParcelaConstantes.IDESTADOSREMISIONCP);
		tiposEstados[16].setIdEstado(GetExpedientesParcelaConstantes.IDESTADOSREMISIONDGI);
		tiposEstados[17].setIdEstado(GetExpedientesParcelaConstantes.IDESTADOSSOLICITUDACTA);
		tiposEstados[18].setIdEstado(GetExpedientesParcelaConstantes.IDESTADOSLICENCIAFUNC);
		
		tiposEstados[1].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYESTADOAPERTURAEXPEDIENTE);
		tiposEstados[2].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYESTADOMEJORADATOS);
		tiposEstados[3].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYESTADOSOLICITUDINFORMES);
		tiposEstados[4].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYESTADOSESPERAINFORMES);
		tiposEstados[5].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYESTADOSEMISIONINFORMERES);
		tiposEstados[6].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYESTADOSESPERAALEGACIONES);
		tiposEstados[7].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYESTADOSACTUALIZAINFORMERES);
		tiposEstados[8].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYESTADOSEMISIONPROPRES);
		tiposEstados[9].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYESTADONOTIFDENEGACION);
		tiposEstados[10].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYESTADOSFORMALIZALICENCIA);
		tiposEstados[11].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYESTADOSNOTIFICACIONAPROB);
		tiposEstados[12].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYESTADOSEJECUCION);
		tiposEstados[13].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYESTADOSDURMIENTE);
		tiposEstados[14].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYESTADOSPUBLICARBOP);
		tiposEstados[15].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYESTADOSREMISIONCP);
		tiposEstados[16].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYESTADOSREMISIONDGI);
		tiposEstados[17].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYESTADOSSOLICITUDACTA);
		tiposEstados[18].setIdDictionary(GetExpedientesParcelaConstantes.DICTIONARYESTADOSLICENCIAFUNC);
		
		loc = Locale.getDefault();
		
		try {
			this.aplicacion = aplicacion;
			this.conexion = aplicacion.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Obtenemos los distintos tipos de expedientes:
	 * Hay que insertar en la tabla public.query_catalog, una fila:
	 * 
	 * @return
	 */
	public TipoExpediente[] obtenerTiposExpedientes(){

		try {		
			PreparedStatement ps = null;
	        ResultSet resultSet = null;	
			
			ps = conexion.prepareStatement("GEPfindTiposExpDictionary");
            ps.setString(1, loc.toString());
            ps.setInt(2, GetExpedientesParcelaConstantes.DICTIONARYOBRAMAYOR);
            ps.setInt(3, GetExpedientesParcelaConstantes.DICTIONARYOBRAMENOR);
            ps.setInt(4, GetExpedientesParcelaConstantes.DICTIONARYACTIVIDADCALIFICADA);
            ps.setInt(5, GetExpedientesParcelaConstantes.DICTIONARYACTIVIDADNOCALIFICADA);
    
            resultSet = ps.executeQuery();
			
            tiposExpedientes[0].setDescripcion("");
			 
            while(resultSet.next()){
            	for (int i=1; i<tiposExpedientes.length; i++){
            		if (tiposExpedientes[i].getIdDictionary()==resultSet.getInt("id_vocablo")){
            			tiposExpedientes[i].setDescripcion(resultSet.getString("traduccion"));
            		}
            	}
            }						
		 } catch (SQLException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
		 return tiposExpedientes;
		 
	 }
	 
	/**
	 * Obtenemos los distintos tipos de expedientes:
	 * Hay que insertar en la tabla public.query_catalog, una fila:
	 * insert into query_catalog(id, query) values ('GEPgetEstadosExp','select id_estado, nombre_estado from estado_expediente')
	 * @return
	 */
	 public Estado[] obtenerEstadosInforme(){

		 try {
				PreparedStatement ps = null;
		        ResultSet resultSet = null;	
				
				ps = conexion.prepareStatement("GEPfindTiposEstadosDictionary");
	            ps.setString(1, loc.toString());
	            ps.setInt(2, GetExpedientesParcelaConstantes.DICTIONARYESTADOAPERTURAEXPEDIENTE);
	            ps.setInt(3, GetExpedientesParcelaConstantes.DICTIONARYESTADOMEJORADATOS);
	            ps.setInt(4, GetExpedientesParcelaConstantes.DICTIONARYESTADOSOLICITUDINFORMES);
	            ps.setInt(5, GetExpedientesParcelaConstantes.DICTIONARYESTADOSESPERAINFORMES);
	            ps.setInt(6, GetExpedientesParcelaConstantes.DICTIONARYESTADOSEMISIONINFORMERES);
	            ps.setInt(7, GetExpedientesParcelaConstantes.DICTIONARYESTADOSESPERAALEGACIONES);
	            ps.setInt(8, GetExpedientesParcelaConstantes.DICTIONARYESTADOSACTUALIZAINFORMERES);   
	            ps.setInt(9, GetExpedientesParcelaConstantes.DICTIONARYESTADOSEMISIONPROPRES);   
	            ps.setInt(10, GetExpedientesParcelaConstantes.DICTIONARYESTADONOTIFDENEGACION);   
	            ps.setInt(11, GetExpedientesParcelaConstantes.DICTIONARYESTADOSFORMALIZALICENCIA);   
	            ps.setInt(12, GetExpedientesParcelaConstantes.DICTIONARYESTADOSNOTIFICACIONAPROB);   
	            ps.setInt(13, GetExpedientesParcelaConstantes.DICTIONARYESTADOSEJECUCION);   
	            ps.setInt(14, GetExpedientesParcelaConstantes.DICTIONARYESTADOSDURMIENTE);   
	            ps.setInt(15, GetExpedientesParcelaConstantes.DICTIONARYESTADOSPUBLICARBOP);   
	            ps.setInt(16, GetExpedientesParcelaConstantes.DICTIONARYESTADOSREMISIONCP);   
	            ps.setInt(17, GetExpedientesParcelaConstantes.DICTIONARYESTADOSREMISIONDGI);   
	            ps.setInt(18, GetExpedientesParcelaConstantes.DICTIONARYESTADOSSOLICITUDACTA);
	            ps.setInt(19, GetExpedientesParcelaConstantes.DICTIONARYESTADOSLICENCIAFUNC); 
	            resultSet = ps.executeQuery();
				
	            tiposEstados[0].setNombreEstado("");
				 
	            while(resultSet.next()){
	            	for (int i=1; i<tiposEstados.length; i++){
	            		if (tiposEstados[i].getIdDictionary()==resultSet.getInt("id_vocablo")){
	            			tiposEstados[i].setNombreEstado(resultSet.getString("traduccion"));
	            		}
	            	}
	            }		

		 } catch (SQLException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
		 return tiposEstados;		
		 
	 }
	
	/**
	 * Metodo que cierra la conexion
	 */
	public void cerrarConexion() {
		
		 aplicacion.closeConnection(conexion, null, null, null);
		 
	}
	 
}
