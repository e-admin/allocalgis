/**
 * ValidacionCultivos.java
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
import java.util.ArrayList;

import com.geopista.app.catastro.intercambio.exception.DataExceptionCatastro;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;
import com.vividsolutions.jump.I18N;

public class ValidacionCultivos
{
    private static final String rustica = "RU";
    private static final String urbana = "UR";
    private static final String bice = "BI";
	/**
	 * Método que comprueba que no haya códigos de subparcelas repetidas en los cultivos de la finca.
	 * @param finca Finca que contiene los cultivos a validar.
	 * @return
	 */
	private boolean codigosSubparcelasCultivosRepetidos(FincaCatastro finca)
	{
		ArrayList lstCultivos = finca.getLstCultivos();
		String codSubparcela = null;
		
		for (int i=0;i<lstCultivos.size();i++)
		{
			Cultivo cultivo = (Cultivo) lstCultivos.get(i);
			if (codSubparcela != null)
			{
				if (cultivo.getCodSubparcela().equalsIgnoreCase(codSubparcela))
					return true;
			}
			codSubparcela = cultivo.getCodSubparcela();
		}
		
		return false;
	}
	
	/**
	 * Método que comprueba que los códigos de subparcelas del cultivo no esté repetido en las Unidades
	 * Constructivas.
	 * @param cultivo Cultivo que se está validando.
	 * @param finca Finca que contiene las Unidades Constructivas.
	 * @return 
	 * 
	 */
	private boolean codigoSubparcelaRepetidoCultivoUC(Cultivo cultivo, FincaCatastro finca)
	{
		ArrayList lstUC = finca.getLstUnidadesConstructivas();
        if(lstUC!=null)
        {
		
            for (int i=0; i<lstUC.size();i++)
            {
                UnidadConstructivaCatastro uc = (UnidadConstructivaCatastro) lstUC.get(i);

                if (uc.getCodUnidadConstructiva().equalsIgnoreCase(cultivo.getCodSubparcela()))
                    return true;
            }
        }
		
        return false;
	}

	
	/**
	 * Método que comprueba que exista el tipo evaluatorio.
	 * @param conn Conexión a la BBDD.
	 * @param cultivo Cultivo que se está validando.
	 * @param finca Finca que contiene el municipio de agregación, necesario para esta validación.
	 * @return 
	 */
	private boolean existeTipoEvaluatorio(Connection conn, Cultivo cultivo, FincaCatastro finca) throws Exception
	{
		String sSQL = "MCgetTipoEvaluatorio"; 
		 //String sSQL= "select * from ruevaluatorio where codigo_delegacionmeh=? AND codigo_municipio_meh=? AND codigo_municipio_agregado=?
		//			     AND cc=? AND ip=?;
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setString(1, cultivo.getCodDelegacionMEH());
	            ps.setString(2, cultivo.getCodMunicipioDGC());
//	            ps.setString(3, finca.getDirParcela().getCodMunOrigenAgregacion());
	            ps.setString(3, cultivo.getIdCultivo().getCalifCultivo());
	            ps.setInt(4, cultivo.getIntensidadProductiva().intValue());
	            rs= ps.executeQuery();
	            if(rs.next())
	            {
	            	return true;
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
		return false;
	}
	
	/**
	 * Método que comprueba si existe el cargo asociado al cultivo.
	 * @param conn Conexión a la BBDD.
	 * @param cultivo Cultivo que se está validando.
	 * @return
	 */
	private boolean existeCargoAsociadoCultivo(Connection conn, Cultivo cultivo) throws Exception
	{
		String sSQL = "MCgetCargoAsociadoCultivo"; 
		 //String sSQL= "select identificador from bien_inmueble where numero_cargo=?;
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setString(1, cultivo.getIdCultivo().getNumOrden());
	            rs= ps.executeQuery();
	            if(rs.next())
	            {
	            	return true;
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
		return false;
	}
	
	/**
	 * Método que comprueba si existe el código de reparto del cultivo.
	 * @param conn Conexión a la BBDD.
	 * @param cultivo Cultivo que se está validando.
	 * @return
	 */
	private boolean existeCodReparto(Connection conn, Cultivo cultivo) throws Exception
	{
		String sSQL = "MCgetReparto"; 
		 //String sSQL= "select id_reparto from repartoscultivos where id_cultivoorigen=? AND id_bieninmuebledestino=?;
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setString(1, cultivo.getIdCultivo().getIdCultivo());
	            ps.setString(2, cultivo.getIdCultivo().getNumOrden());
	            rs= ps.executeQuery();
	            if(rs.next())
	            {
	            	return true;
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
		return false;
	}
	
	/**
	 * Método que realiza la validación parcial del cultivo que llega por parámetro.
	 * @param conn Conexión a la BBDD.
	 * @param cultivo Cultivo que se está validando.
	 * @param finca Finca que contiene el cultivo que se valida.
	 * @return String con el código de error si se ha producido alguno ó null en caso contrario.
	 * @throws DataExceptionCatastro 
	 */
	public StringBuffer validacionParcialCultivo(Connection conn, Cultivo cultivo, FincaCatastro finca) throws DataExceptionCatastro
	{
		StringBuffer sbVal = null;
		//Tipo de subparcela
		if (cultivo.getTipoSubparcela()==null || cultivo.getTipoSubparcela().equalsIgnoreCase("")){

			if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.cultivo") +"- "+
					cultivo.getIdCultivo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G109")+"\n\n\r");
		}
		
		//Codigo de subparcela
		if (!cultivo.getTipoSubparcela().equalsIgnoreCase("A"))
		{
			//No puede haber códigos de subparcelas repetidos
			if (codigosSubparcelasCultivosRepetidos(finca)){

				if(sbVal == null){
	        		sbVal = new StringBuffer();
	        	}
	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.cultivo") +"- "+
						cultivo.getIdCultivo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G110")+"\n\n\r");
			}
		}
		
		if (codigoSubparcelaRepetidoCultivoUC(cultivo, finca)){

			if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.cultivo") +"- "+
					cultivo.getIdCultivo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G169")+"\n\n\r");
		}
		
		//Superficie
		if (cultivo.getSuperficieParcela().longValue() < 1){

			if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.cultivo") +"- "+
					cultivo.getIdCultivo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G111")+"\n\n\r");
		}
		
		//Clase
		if ((!cultivo.getTipoSuelo().equalsIgnoreCase(urbana)) && (!cultivo.getTipoSuelo().equalsIgnoreCase(rustica))){

			if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.cultivo") +"- "+
					cultivo.getIdCultivo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G057")+"\n\n\r");
		}
		
		//Calificación catastral
		try
        {
			
			//Tipo evaluatorio
			if (!existeTipoEvaluatorio(conn, cultivo, finca)){

				if(sbVal == null){
	        		sbVal = new StringBuffer();
	        	}
	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.cultivo") +"- "+
						cultivo.getIdCultivo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G113")+"\n\n\r");
			}
			
			//Cargo
			if (cultivo.getCodModalidadReparto()!=null && cultivo.getCodModalidadReparto().length() > 0&&false)
			{
				//Comprobar que el cargo asociado exista
				if (!existeCargoAsociadoCultivo(conn, cultivo)){

					if(sbVal == null){
		        		sbVal = new StringBuffer();
		        	}
		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.cultivo") +"- "+
							cultivo.getIdCultivo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G114")+"\n\n\r");
				}
			}
			
		} catch (Exception e) {
			throw new DataExceptionCatastro(e);
		}
		
		//Código de reparto
		if (cultivo.getCodModalidadReparto()!=null && cultivo.getCodModalidadReparto().length() > 0)
		{
			try {
				if (!cultivo.getCodModalidadReparto().equalsIgnoreCase("AC"+1) && !cultivo.getCodModalidadReparto().equalsIgnoreCase("AC"+2) && !cultivo.getCodModalidadReparto().equalsIgnoreCase("AC"+3) && !cultivo.getCodModalidadReparto().equalsIgnoreCase("AC"+4)
						&& !cultivo.getCodModalidadReparto().equalsIgnoreCase("TC"+1) && !cultivo.getCodModalidadReparto().equalsIgnoreCase("TC"+2) && !cultivo.getCodModalidadReparto().equalsIgnoreCase("TC"+3) && !cultivo.getCodModalidadReparto().equalsIgnoreCase("TC"+4)){

					if(sbVal == null){
		        		sbVal = new StringBuffer();
		        	}
		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.cultivo") +"- "+
							cultivo.getIdCultivo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G93")+"\n\n\r");
				}

				/*No se comprobará esta condición
				if (cultivo.getCodModalidadReparto().startsWith("AC"))
				{
					//Comprobar que existe el reparto
					if (!existeCodReparto(conn, cultivo))
						return "Error.G115";
				}*/
			} catch (Exception e) {
				throw new DataExceptionCatastro(e);
			}
		}
		
		//Codigo de subparcela unica
		StringBuffer sbValCodSubParcelaUnica =  comprobarCodigoSubParcelaUnica(cultivo, finca);
		if(sbValCodSubParcelaUnica != null){
			if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
			sbVal.append(sbValCodSubParcelaUnica.toString());
		}
		
		return sbVal;
	}
	
	/**
	 * Método que comprueba que comprueba que los códigos de subparcela de los cultivos que contiene 
	 * la finca sean disitintos de ' 0'.
	 * @param cultivo Cultivo que se está validando.
	 * @param finca Finca que contiene la lista de cultivos a validar.
	 * @return String con el código de error si se ha producido alguno ó null en caso contrario.
	 */
	private StringBuffer comprobarCodigoSubParcelaUnica(Cultivo cultivo, FincaCatastro finca)
	{
		ArrayList lstCultivos = finca.getLstCultivos();
		StringBuffer sbVal = null;
		
		if (lstCultivos.size() > 1)
		{
			//El código de subparcela de ninguno de ellos puede ser  '   0'. 
			for (int i=0;i<lstCultivos.size();i++)
			{
				Cultivo c = (Cultivo) lstCultivos.get(i);
				if (c.getCodSubparcela().equalsIgnoreCase(" 0")){
					if(sbVal == null){
		        		sbVal = new StringBuffer();
		        	}
		        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G116")+"\n\n\r");
				}
			}
		}
		else
		{
			//Solo hay un cultivo
			if (!CriteriosValidacion.esNecesariaPonencia(finca) && !cultivo.getCodSubparcela().equalsIgnoreCase(" 0")){
				if(sbVal == null){
	        		sbVal = new StringBuffer();
	        	}
	        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G116")+"\n\n\r");
			}
		}
		
		return sbVal;
	}
	
	/**
	 * Método que realiza la validación de los cultivos de la finca.
	 * @param conn Conexión a la BBDD.
	 * @param finca Finca que contiene los cultivos a validar.
	 * @return String con el código de error si se ha producido alguno ó null en caso contrario.
	 * @throws DataExceptionCatastro 
	 */
	public StringBuffer validacionCultivos(Connection conn, FincaCatastro finca) throws DataExceptionCatastro
	{
		ArrayList lstCultivos = finca.getLstCultivos();
		StringBuffer sbVal = null;
		if ((lstCultivos != null)&&(lstCultivos.size() > 0))
		{
			for (int i=0;i<lstCultivos.size();i++)
			{
				Cultivo cultivo = (Cultivo) lstCultivos.get(i);
					
				//Para cada subparcela rustiva
				StringBuffer sbValParcialCultivo = validacionParcialCultivo(conn, cultivo, finca);
				if(sbValParcialCultivo == null){
					sbVal.append(sbValParcialCultivo.toString());
				}
			}
		}
		return sbVal;
	}

}
