/**
 * Ponencia.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion.validacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.geopista.app.catastro.model.datos.ponencia.PonenciaCNT;

public class Ponencia {

	public static PonenciaCNT recopilarDatos(Connection conn, Integer anioAprobacion) throws Exception
	{
		 String sSQL = "MCgetDatosPonencia";
		 //String sSQL= "select * from cntponurb where anno_aprobacion="+anioAprobacion;
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     PonenciaCNT ponencia = null;
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setInt(1, anioAprobacion.intValue());
	            rs= ps.executeQuery();
	            if(rs.next())
	            {
	            	ponencia = new PonenciaCNT();
	            	ponencia.setTipoPonencia(rs.getString("tipo_ponencia"));
	            	ponencia.setAnioNormas(new Integer(rs.getInt("anno_normas")));
	            	ponencia.setAnioEfectosTotal(new Integer(rs.getInt("anno_efectostotal")));
	            	ponencia.setPropVertical(rs.getString("propiedad_vertical1"));
	            	ponencia.setAntiguedad(new Integer(rs.getInt("antiguedad_infraedificada")));
	            	ponencia.setEstadoPonencia(rs.getString("estado_ponencia"));
	            	ponencia.setInfraedificacion(new Float (rs.getFloat("finca_infraedificada")));
	            	ponencia.setAnioCuadroMarco(new Integer(rs.getInt("anno_cuadromarco")));
	            	ponencia.setAplicFormula(rs.getString("aplicacion_formula"));
	            	ponencia.setSinDesarrollar(rs.getString("suelo_sindesarrollar"));
	            	ponencia.setRuinoso(rs.getString("ruinosa"));
	            	ponencia.setFechaCierreRevisiona(rs.getDate("fecha_cierrerevision"));
	            }
	        }
	        catch (Exception e)
	        {
	            throw e;
	        }
	        finally
	        {
	            try{ps.close();}catch(Exception e){};
	            try{rs.close();}catch(Exception e){};
	         }
	        return ponencia;
	}
	
	/**
	 * Devuelve el coeficiente de coordinación en modificaciones de planeamiento  
	 * @param conn
	 * @param codigoPoligono
	 * @return
	 * @throws Exception
	 */
	public static double getCoefCoordPlan(Connection conn, String codigoPoligono) throws Exception
	{
		 String sSQL = "MCgetCoefCoordPlan";
		 //String sSQL= "select * from cntponurb where anno_aprobacion="+anioAprobacion;
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     PonenciaCNT ponencia = null;
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setString(1, codigoPoligono);
	            rs= ps.executeQuery();
	            if(rs.next())
	            {
	            	return rs.getDouble("coef_coordplan");
	            }
	        }
	        catch (Exception e)
	        {
	            throw e;
	        }
	        finally
	        {
	            try{ps.close();}catch(Exception e){};
	            try{rs.close();}catch(Exception e){};
	         }
	        return 0;
	}

}
