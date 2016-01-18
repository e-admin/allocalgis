package com.geopista.app.catastro.intercambio.edicion.validacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.geopista.app.catastro.intercambio.exception.DataException;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;

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
	 * @throws DataException 
	 */
	public String validacionParcialCultivo(Connection conn, Cultivo cultivo, FincaCatastro finca) throws DataException
	{
		//Tipo de subparcela
		if (cultivo.getTipoSubparcela()==null || cultivo.getTipoSubparcela().equalsIgnoreCase(""))
			return "Error.G109";
		
		//Codigo de subparcela
		if (!cultivo.getTipoSubparcela().equalsIgnoreCase("A"))
		{
			//No puede haber códigos de subparcelas repetidos
			if (codigosSubparcelasCultivosRepetidos(finca))
				return "Error.G110";
		}
		
		if (codigoSubparcelaRepetidoCultivoUC(cultivo, finca))
			return "Error.G169";
		
		//Superficie
		if (cultivo.getSuperficieParcela().longValue() < 1)
			return "Error.G111";
		
		//Clase
		if ((!cultivo.getTipoSuelo().equalsIgnoreCase(urbana)) && (!cultivo.getTipoSuelo().equalsIgnoreCase(rustica)))
				return "Error.G057";
		
		//Calificación catastral
		try
        {
			
			//Tipo evaluatorio
			if (!existeTipoEvaluatorio(conn, cultivo, finca))
				return "Error.G113";
			
			//Cargo
			if (cultivo.getCodModalidadReparto()!=null && cultivo.getCodModalidadReparto().length() > 0&&false)
			{
				//Comprobar que el cargo asociado exista
				if (!existeCargoAsociadoCultivo(conn, cultivo))
					return "Error.G114";
			}
			
		} catch (Exception e) {
			throw new DataException(e);
		}
		
		//Código de reparto
		if (cultivo.getCodModalidadReparto()!=null && cultivo.getCodModalidadReparto().length() > 0)
		{
			try {
				if (!cultivo.getCodModalidadReparto().equalsIgnoreCase("AC"+1) && !cultivo.getCodModalidadReparto().equalsIgnoreCase("AC"+2) && !cultivo.getCodModalidadReparto().equalsIgnoreCase("AC"+3) && !cultivo.getCodModalidadReparto().equalsIgnoreCase("AC"+4)
						&& !cultivo.getCodModalidadReparto().equalsIgnoreCase("TC"+1) && !cultivo.getCodModalidadReparto().equalsIgnoreCase("TC"+2) && !cultivo.getCodModalidadReparto().equalsIgnoreCase("TC"+3) && !cultivo.getCodModalidadReparto().equalsIgnoreCase("TC"+4))
					return "Error.G93";
		
				/*No se comprobará esta condición
				if (cultivo.getCodModalidadReparto().startsWith("AC"))
				{
					//Comprobar que existe el reparto
					if (!existeCodReparto(conn, cultivo))
						return "Error.G115";
				}*/
			} catch (Exception e) {
				throw new DataException(e);
			}
		}
		
		//Codigo de subparcela unica
		return comprobarCodigoSubParcelaUnica(cultivo, finca);
	}
	
	/**
	 * Método que comprueba que comprueba que los códigos de subparcela de los cultivos que contiene 
	 * la finca sean disitintos de ' 0'.
	 * @param cultivo Cultivo que se está validando.
	 * @param finca Finca que contiene la lista de cultivos a validar.
	 * @return String con el código de error si se ha producido alguno ó null en caso contrario.
	 */
	private String comprobarCodigoSubParcelaUnica(Cultivo cultivo, FincaCatastro finca)
	{
		ArrayList lstCultivos = finca.getLstCultivos();
		
		if (lstCultivos.size() > 1)
		{
			//El código de subparcela de ninguno de ellos puede ser  '   0'. 
			for (int i=0;i<lstCultivos.size();i++)
			{
				Cultivo c = (Cultivo) lstCultivos.get(i);
				if (c.getCodSubparcela().equalsIgnoreCase(" 0"))
					return "Error.G116";
			}
		}
		else
		{
			//Solo hay un cultivo
			if (!CriteriosValidacion.esNecesariaPonencia(finca) && !cultivo.getCodSubparcela().equalsIgnoreCase(" 0"))
				return "Error.G116";
		}
		
		return null;
	}
	
	/**
	 * Método que realiza la validación de los cultivos de la finca.
	 * @param conn Conexión a la BBDD.
	 * @param finca Finca que contiene los cultivos a validar.
	 * @return String con el código de error si se ha producido alguno ó null en caso contrario.
	 * @throws DataException 
	 */
	public String validacionCultivos(Connection conn, FincaCatastro finca) throws DataException
	{
		ArrayList lstCultivos = finca.getLstCultivos();
		String resultado= null;
		
		if ((lstCultivos != null)&&(lstCultivos.size() > 0))
		{
			for (int i=0;i<lstCultivos.size()&& resultado==null;i++)
			{
				Cultivo cultivo = (Cultivo) lstCultivos.get(i);
					
				//Para cada subparcela rustiva
				resultado = validacionParcialCultivo(conn, cultivo, finca);
			}
		}
		return resultado;
	}

}
