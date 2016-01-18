package com.geopista.app.catastro.intercambio.edicion.validacion;

import java.util.ArrayList;


import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;

public class CriteriosValidacion {
	private static Boolean hayUrbana;
	private static Boolean hayRustica;
	private static Boolean hayBICE;
    private static final String rustica = "RU";
    private static final String urbana = "UR";
    private static final String bice = "BI";

    /**
	 * Método que indica si existe suelo urbano en la finca. Es decir, si hay algún suelo en la finca 
	 * cuya zona de valor sea distinta de ‘RUSTI’.
	 * @param finca
	 * @return
	 */
	private static boolean existeSueloUrbanoFinca(FincaCatastro finca)
	{
		
		ArrayList lstSuelos = finca.getLstSuelos();

        if(lstSuelos!=null && lstSuelos.size()>0)
        {
            return true;
           //TODO comentado porque no se entiende, se supone que si hay suelo es urbana
            /*for (int i=0;i<lstSuelos.size(); i++)
            {
                SueloCatastro suelo = (SueloCatastro) lstSuelos.get(i);

                if (!suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().equalsIgnoreCase("RUSTI"))
                    return true;
            } */
        }
		return false;
	}
	
	/**
	 * Método que indica si existen unidades constructivas en la finca.
	 * @param finca
	 * @return
	 */
	private static boolean existeUCFinca(FincaCatastro finca)
	{
		ArrayList lstUC = finca.getLstUnidadesConstructivas();
		
		if ((lstUC != null)&&(lstUC.size() > 0))
			return true;
		
		return false;
	}
	
	/**
	 * Método que indica si existe o no unidades constructivas de tipo no RUSTI en la finca.
	 * @param finca
	 * @return
	 */
	private static boolean existeUCNoRustica(FincaCatastro finca)
	{
		if (existeUCFinca(finca))
		{
			ArrayList lstUC = finca.getLstUnidadesConstructivas();
			
			for (int i=0;i<lstUC.size();i++)
			{
				UnidadConstructivaCatastro uc = (UnidadConstructivaCatastro) lstUC.get(i);
				
				if (!uc.getTipoUnidad().equalsIgnoreCase(rustica))
					return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Método que indica si es necesaria la ponencia o no. Es necesaria si hay suelo urbano,
	 * o en el caso de que no lo haya, si hay UC en la finca.
	 * 
	 * @param finca
	 * @return
	 */
	public static boolean esNecesariaPonencia(FincaCatastro finca)
	{
		//Comprobamos si hay suelo urbano en la finca.
		if (existeSueloUrbanoFinca(finca) || existeUCFinca(finca))
			return true;
		
		return false;
	}
	
	/**
	 * Método que indica si hay urbana en finca. Hay urbana en finca si se cumple alguna de las condiciones:
	 * -	si es necesaria ponencia y existe suelo urbano en la finca.
	 * -	si existe para esa finca alguna unidad constructiva con clase distinta de rústica (R).	
	 * @param finca
	 * @return
	 */
	public static boolean existeUrbanaEnFinca(FincaCatastro finca)
	{
		/*if (hayUrbana == null)
		{*/
			if (esNecesariaPonencia(finca) && existeSueloUrbanoFinca(finca))
				hayUrbana = new Boolean(true);
			else
			{
				if (existeUCNoRustica(finca))
					hayUrbana = new Boolean(true);
				else
					hayUrbana = new Boolean(false);
			}
		//}
		
		return hayUrbana.booleanValue();
	}
	
	/**
	 * método que indica si existe un cultivo de tipo rústico en la finca.
	 * @param finca
	 * @return
	 */
	private static boolean existeCultivoRustica(FincaCatastro finca)
	{
		ArrayList lstCultivos = finca.getLstCultivos();
		
		if (lstCultivos != null)
		{
			for (int i=0;i<lstCultivos.size();i++)
			{
				Cultivo cultivo = (Cultivo)lstCultivos.get(i);
				
				if (cultivo.getTipoSuelo().equalsIgnoreCase(rustica))
					return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Método que indica si existe o no unidades constructivas de tipo RUSTI en la finca.
	 * @param finca
	 * @return
	 */
	private static boolean existeUCRustica(FincaCatastro finca)
	{
		if (existeUCFinca(finca))
		{
			ArrayList lstUC = finca.getLstUnidadesConstructivas();
			
			for (int i=0;i<lstUC.size();i++)
			{
				UnidadConstructivaCatastro uc = (UnidadConstructivaCatastro) lstUC.get(i);
				
				if (uc.getTipoUnidad().equalsIgnoreCase(rustica))
					return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Método que indica si hay rustica en la finca. Hay rustica si se cumple alguna de las condiciones:
	 * -	si hay algún cultivo de clase rústica (R).
	 * -	si hay alguna unidad constructiva de clase rústica (R).

	 * @param finca
	 * @return
	 */
	public static boolean existeRusticaEnFinca(FincaCatastro finca)
	{
		/*if (hayRustica == null)
		{*/
			if (existeCultivoRustica(finca) || existeUCRustica(finca))
				hayRustica = new Boolean(true);
			else
				hayRustica = new Boolean(false);
		//}
		
		return hayRustica.booleanValue();
	}
	
	public static boolean esFincaBICE(FincaCatastro finca)
	{
		/*if (hayBICE == null)
		{*/
			if (finca.getBICE() != null)
				hayBICE = new Boolean(true);
			else
				hayBICE = new Boolean(false);
		//}
		return hayBICE.booleanValue();
	}
	

}
