/**
 * ValidacionFinca.java
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
import java.sql.SQLException;
import java.util.ArrayList;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.exception.DataExceptionCatastro;
import com.geopista.app.catastro.model.beans.*;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaCNT;
import com.vividsolutions.jump.I18N;

public class ValidacionFinca {
	
    private static final String rustica = "RU";
    private static final String urbana = "UR";
    private static final String bice = "BI";
    private String metodoFormulaCalculo;
    private Integer formulaCalculo;
    private boolean esPonenciaCorrecta;
    private Integer finca_infraedificada;
    private PonenciaCNT ponencia;
    private String CodigoTipoExpediente;
    
    
    public ValidacionFinca(DatosValoracion datoValoracion, String CodigoTipoExpediente)
    {
    	this.metodoFormulaCalculo = datoValoracion.getMetodoCalculoValorSuelo();
    	this.esPonenciaCorrecta = datoValoracion.isEsCorrectaFormaCalculo();
    	this.formulaCalculo = datoValoracion.getFormaDeCalculo();
        this.CodigoTipoExpediente = CodigoTipoExpediente;
    }
    
    /**
	 * Comprueba que el código de polígono sea correcto. (tabla parcelas)
	 * @param codpoligono
	 * @return
	 * @throws Exception 
	 */
    //nombrevia, tipovia, codigoVia
	public boolean comprobarCodigoPoligono(Connection conn, String codpoligono) throws Exception
	{
		 String sSQL = "MCgetCodPoligono";
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try                               
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setString(1, codpoligono);
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
	 * Comprueba que el código de vía sea correcto. (tabla vía)
	 * @param codigoTipoVia
	 * @return
	 * @throws Exception 
	 */
    //nombrevia, tipovia, codigoVia
	public boolean comprobarCodigoVia(Connection conn, String nombreVia, String tipoVia, int codVia) throws Exception
	{
		 String sSQL = "MCbuscaridvia";
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try                               
	        {
	            ps= conn.prepareStatement(sSQL);
                ps.setString(1, tipoVia);
                ps.setString(2, nombreVia);
                ps.setInt(3, codVia);
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
	 * Se comprueba que si se indica código de paraje, éste sea correcto. (tabla ruparaje) 
	 * @return
	 * @throws Exception
	 */
	public boolean comprobarCodigoParaje(Connection conn, String codigoParaje) throws Exception
	{
		 //String sSQL= "select * from paraje where codigoparaje="+codigoParaje;
		 String sSQL = "MCgetCodigoParaje";
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setInt(1, Integer.parseInt(codigoParaje));
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
	
	public boolean comprobarCodigoParaje(Connection conn, DireccionLocalizacion direccion) throws Exception
	{		
    	long identificador = 0;

    	if (direccion.getCodParaje() != null && !direccion.getCodParaje().equals("") 
    			&& direccion.getMunicipioINE() != null && !direccion.getMunicipioINE().equals("")
    			&& direccion.getProvinciaINE() != null && !direccion.getProvinciaINE().equals("")){

    		PreparedStatement ps = null;
    		ResultSet rs = null;

    		try{
    			
    			String codProvincia = EdicionUtils.paddingString(String.valueOf(direccion.getProvinciaINE()),
        				2,'0',true);
    			
    			String codMunicipio = EdicionUtils.paddingString(String.valueOf(direccion.getMunicipioINE()),
        				3,'0',true);

        		String codParaje = EdicionUtils.paddingString(String.valueOf(direccion.getCodParaje()),
        				5,'0',true);
        		
        		identificador = Long.parseLong(codProvincia + codMunicipio + codParaje);
        		
        		
    			ps = conn.prepareStatement("MCexisteParaje");
    			ps.setLong(1, identificador);

    			rs = ps.executeQuery();
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
    	}
		return false;
	}
	
	
	/**
	 * Se comprueba que si se indica código de paraje, éste sea correcto. (tabla ruparaje)
	 * @return
	 * @throws Exception
	 */
	public boolean comprobarZonaConcentracion(Connection conn, String zona) throws Exception
	{
		 //String sSQL= "select * from paraje where codigoparaje="+zona;
		 String sSQL = "MCgetZonaConcentracion";
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setString(1, zona);
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
	 * Si el municipio agregado es mayor que 0, se comprueba que sea correcto. (tabla ruevaluatorio).
	 * @return
	 * @throws Exception 
	 */
	private boolean comprobarMunicipioAgregado(Connection conn, long Idmunicipio) throws Exception
	{
		 String sSQL = "MCgetMunicipioAgregacion";
		 //String sSQL= "select * from ruevaluatorio where codigo_municipio_agregado="+Idmunicipio;
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setLong(1, Idmunicipio);
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
	
	
	private boolean construccionPorFases(FincaCatastro finca)
	{
		ArrayList lstConstrucciones = finca.getLstConstrucciones();
		if(lstConstrucciones!=null)
        {
            for (int i=0;i<lstConstrucciones.size();i++)
            {
                ConstruccionCatastro construccion = (ConstruccionCatastro) lstConstrucciones.get(i);
                if (construccion.getNumOrdenConstruccion().equalsIgnoreCase("00000"))
                    return true;
            }
        }
        return false;
	}
	
	private long sumaSuelos(FincaCatastro finca)
	{
		ArrayList lstSuelos = finca.getLstSuelos();
		long supTotal = 0;
		
		if (lstSuelos != null)
		{
			for (int i=0;i<lstSuelos.size();i++)
			{
				SueloCatastro suelo = (SueloCatastro) lstSuelos.get(i);
				supTotal += suelo.getDatosFisicos().getSupOcupada().longValue();
			}
		}
		return supTotal;
	}
	
	public float calcularSupTeoricaInfraedificada(FincaCatastro finca)
	{
		float supTeoricaInfraedificada = 0;
		
		ArrayList lstSuelos = finca.getLstSuelos();
		
		if (lstSuelos != null)
		{
			for (int i=0;i<lstSuelos.size();i++)
			{
				SueloCatastro suelo = (SueloCatastro) lstSuelos.get(i);
				long sup = suelo.getDatosFisicos().getSupOcupada().longValue();
				
				if (suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad() != null){
					if (suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().getEquipamientos() == null)
						suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().setEquipamientos(new Float(0));
					if (suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().getUsoComercial() == null)
						suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().setUsoComercial(new Float(0));
					if (suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().getUsoOficinas() == null)
						suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().setUsoOficinas(new Float(0));
					if (suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().getUsoIndustrial() == null)
						suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().setUsoIndustrial(new Float(0));
					if (suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().getUsoTuristico() == null)
						suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().setUsoTuristico(new Float(0));
					if (suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().getOtrosUsos2() == null)
						suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().setOtrosUsos2(new Float(0));
					if (suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().getOtrosUsos3() == null)
						suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().setOtrosUsos3(new Float(0));
					if (suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().getEquipamientos() == null)
						suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().setEquipamientos(new Float(0));


					supTeoricaInfraedificada += (sup * suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().getUsoResidencial().floatValue())
					+ (sup*suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().getUsoComercial().floatValue())
					+ (sup*suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().getUsoOficinas().floatValue())
					+ (sup*suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().getUsoIndustrial().floatValue())
					+ (sup*suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().getUsoTuristico().floatValue())
					+ (sup*suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().getOtrosUsos2().floatValue())
					+ (sup*suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().getOtrosUsos3().floatValue())
					+ (sup*suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().getEquipamientos().floatValue());
				}
			}
		}
		return supTeoricaInfraedificada;
	}
	
	private boolean ucCorrecta(String codUC, FincaCatastro finca)
	{
		ArrayList lstUC = finca.getLstUnidadesConstructivas();
		
		if (lstUC != null)
		{
			for (int i=0;i<lstUC.size();i++)
			{
				UnidadConstructivaCatastro uc = (UnidadConstructivaCatastro) lstUC.get(i);
				
				if (uc.getCodUnidadConstructiva().equalsIgnoreCase(codUC) && (uc.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("P")))
					return true;
			}
		}
		
		return false;
	}
	
	private long calcularSupRealInfraedificada(FincaCatastro finca)
	{
		long supRealInfra = 0;
		
		ArrayList lstConstrucciones = finca.getLstConstrucciones();
		
		if (lstConstrucciones != null)
		{
			for (int i=0;i<lstConstrucciones.size();i++)
			{
				ConstruccionCatastro construccion = (ConstruccionCatastro) lstConstrucciones.get(i);
				
				if (!construccion.getDomicilioTributario().getPlanta().equalsIgnoreCase("SS") && !construccion.getDomicilioTributario().getPlanta().startsWith("-")
						&& !construccion.getDatosEconomicos().getTipoConstruccion().startsWith("1032")
						&& !construccion.getDatosEconomicos().getTipoConstruccion().startsWith("1035")
						&& ucCorrecta(construccion.getDatosFisicos().getCodUnidadConstructiva(), finca)
						&& (Integer.parseInt(construccion.getDatosEconomicos().getCodTipoValor()) != 7))
						
					supRealInfra += construccion.getDatosFisicos().getSupTotal().longValue();
				
			}
		}
		
		return supRealInfra;
	}
	
	private boolean ucCorrectaUrbana(ConstruccionCatastro construccion, String codUC, FincaCatastro finca)
	{
		ArrayList lstUC = finca.getLstUnidadesConstructivas();
		
		if (lstUC != null)
		{
			for (int i=0;i<lstUC.size();i++)
			{
				UnidadConstructivaCatastro uc = (UnidadConstructivaCatastro) lstUC.get(i);
			
				if (uc.getCodUnidadConstructiva().equalsIgnoreCase(codUC) && uc.getTipoUnidad().equalsIgnoreCase("U"))
				{
					int aa_construccion = construccion.getDatosFisicos().getAnioAntiguedad().intValue();
					int aa_reforma = construccion.getDatosFisicos().getAnioReforma().intValue();
					int aa_infra = ponencia.getAntiguedad().intValue();
					long op = (long) (aa_construccion + ((aa_reforma - aa_construccion) * 0.75));
					long op2 = (long) (aa_construccion + ((aa_reforma - aa_construccion) * 0.5));
					long op3 = (long) (aa_construccion + ((aa_reforma - aa_construccion) * 0.25));
					
					if ((construccion.getDatosFisicos().getTipoReforma().equalsIgnoreCase("R")) 
						&&(aa_infra > aa_reforma))
						return true;
					
					if (construccion.getDatosFisicos().getTipoReforma().equalsIgnoreCase("O")
							&& (aa_infra > op))
						return true;
					
					if (construccion.getDatosFisicos().getTipoReforma().equalsIgnoreCase("E")
							&& (aa_infra > op2))
						return true;
					
					if (construccion.getDatosFisicos().getTipoReforma().equalsIgnoreCase("I")
							&& (aa_infra > op3))
						return true;
					else
						if (aa_infra > aa_construccion)
							return true;
						
				}
			}
		}
		
		return false;
	}
	
	
	private boolean ucCCCO(ConstruccionCatastro construccion, String codUC, FincaCatastro finca)
	{
		ArrayList lstUC = finca.getLstUnidadesConstructivas();
		
		if (lstUC != null)
		{
			for (int i=0;i<lstUC.size();i++)
			{
				UnidadConstructivaCatastro uc = (UnidadConstructivaCatastro) lstUC.get(i);
			
				if (uc.getCodUnidadConstructiva().equalsIgnoreCase(codUC) && uc.getTipoUnidad().equalsIgnoreCase("U")
						&& uc.getDatosEconomicos().getCorrectorConservacion().equalsIgnoreCase("O"))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	private long calcularSupTotalConstrucciones(FincaCatastro finca)
	{
		long supTotalConstrucciones = 0;
		
		ArrayList lstConstrucciones = finca.getLstConstrucciones();
		
		if (lstConstrucciones != null)
		{
			for (int i=0;i<lstConstrucciones.size();i++)
			{
				ConstruccionCatastro construccion = (ConstruccionCatastro) lstConstrucciones.get(i);
				
				if (ucCorrectaUrbana(construccion, construccion.getDatosFisicos().getCodUnidadConstructiva(), finca))
					supTotalConstrucciones += construccion.getDatosFisicos().getSupTotal().longValue();
			}
		}
		
		return supTotalConstrucciones;
	}
	
	private long calcularSupTotalConstruccionesCCCO(FincaCatastro finca)
	{
		long supTotalConstruccionCCCO = 0;
		
		ArrayList lstConstrucciones = finca.getLstConstrucciones();
		
		if (lstConstrucciones != null)
		{
			for (int i=0;i<lstConstrucciones.size();i++)
			{
				ConstruccionCatastro construccion = (ConstruccionCatastro) lstConstrucciones.get(i);
				
				if (ucCCCO(construccion, construccion.getDatosFisicos().getCodUnidadConstructiva(), finca))
					supTotalConstruccionCCCO += construccion.getDatosFisicos().getSupTotal().longValue();
			}
		}
		
		return supTotalConstruccionCCCO;
	}
	
	
	private int contarSuelosPnoPPnoUG(FincaCatastro finca)
	{
		int numSuelos = 0;
		
		ArrayList lstSuelos = finca.getLstSuelos();
		
		if (lstSuelos != null)
		{
			for (int i=0;i<lstSuelos.size();i++)
			{
				SueloCatastro suelo = (SueloCatastro) lstSuelos.get(i);
				
				if (!suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("P")
						|| (!suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("PP")
								&& !suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("UG")))
					numSuelos++;
			}
		}
		
		return numSuelos;
	}
	
	
	private boolean criteriosExclusionInfraedificada(FincaCatastro finca)
	{
		//Criterios de exclusión
        if (finca.getDatosEconomicos().getAnioAprobacion() != null &&
                finca.getDatosEconomicos().getAnioAprobacion().intValue() < 2006)
            return false;
		
		if (!CriteriosValidacion.existeUrbanaEnFinca(finca))
			return false;
		
		if (finca.getDatosEconomicos().getIndInfraedificabilidad()==null ||
                finca.getDatosEconomicos().getIndInfraedificabilidad().equalsIgnoreCase("N"))
			return false;
		
		if (ponencia.getInfraedificacion().floatValue() > 0)
			return false;
		
		if (finca.getDatosEconomicos().getIndModalidadReparto().equalsIgnoreCase("C"))
			return false;
		
		if (formulaCalculo.intValue() == 11)
			return false;
		
		return true;
	}
	
	private void casos134Infraedificada(Connection conn, FincaCatastro finca)
	{
		//Caso 1
		if ((ponencia.getInfraedificacion().floatValue() > 0) && 
			((!ponencia.getPropVertical().equalsIgnoreCase("S"))||
			(finca.getDatosEconomicos().getIndInfraedificabilidad()!=null &&
            !finca.getDatosEconomicos().getIndInfraedificabilidad().equalsIgnoreCase("S"))) || 
            (formulaCalculo.intValue() < 40))
			{
				float supTeoricaInfra = calcularSupTeoricaInfraedificada(finca);
				float supRealInfra = 0;
				
				if (supTeoricaInfra > 0)
				{
					supRealInfra = calcularSupRealInfraedificada(finca);
					if ((supRealInfra / supTeoricaInfra) < (ponencia.getInfraedificacion().floatValue()))
					{
						//Comprobar criterio adicional de antigüedad
						if (ponencia.getAntiguedad().intValue() == 0)
						{
                            finca.getDatosEconomicos().setIndInfraedificabilidad("1");
                            finca.getDatosEconomicos().setCodigoCalculoValor(null);
						}
						
						if (ponencia.getAntiguedad().intValue() > 0)
						{
							float supTotalConstrucciones = calcularSupTotalConstrucciones(finca);
							
							if ((2*supTotalConstrucciones) >= (finca.getDatosFisicos().getSupTotal().longValue()))
							{
								//indicador de que la finca es infraedificada será 1 y el método de cálculo del valor del suelo será blanco
                                finca.getDatosEconomicos().setIndInfraedificabilidad("1");
                                finca.getDatosEconomicos().setCodigoCalculoValor(null);
							}
						}
					}
				}
			}
			
			//Caso 3
			long supTotalCCCO = calcularSupTotalConstruccionesCCCO(finca);
			
			if ((2*supTotalCCCO) >=( finca.getDatosFisicos().getSupTotal().longValue()))
            {
                finca.getDatosEconomicos().setIndInfraedificabilidad("3");
                finca.getDatosEconomicos().setCodigoCalculoValor(null);
            }
			
			
			//Caso 4
			int numSuelos = contarSuelosPnoPPnoUG(finca);
			if (numSuelos == 0)
			{
                finca.getDatosEconomicos().setIndInfraedificabilidad("4");
                finca.getDatosEconomicos().setCodigoCalculoValor(null);
			}
	}
	
	
	public StringBuffer validacionParcialFinca(Connection conn, FincaCatastro finca, String idMunicipio) throws Exception
	{
		StringBuffer sbVal = null;
		//VALIDACION DEL DOMICILIO
    	//No pueden faltar a la vez la vía y el polígono
        if ((finca.getDirParcela().getCodPoligono()==null || finca.getDirParcela().getCodPoligono().length() < 1)
                &&(finca.getDirParcela().getNombreVia()==null || finca.getDirParcela().getNombreVia().length()<1)){
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G5")+"\n\n\r");
        }

    	
    	try
        {
    		if (finca.getDirParcela().esUrbana()){ 
    			//Si la finca es urbana se comprueba que el código de vía sea correcto
	            if ((finca.getDirParcela().getNombreVia()!=null&&finca.getDirParcela().getNombreVia().length() > 0)&&
	                    (!comprobarCodigoVia(conn, finca.getDirParcela().getNombreVia(),finca.getDirParcela().getTipoVia(), finca.getDirParcela().getCodigoVia()))){
	            	if(sbVal == null){
	            		sbVal = new StringBuffer();
	            	}
	            	sbVal.append(I18N.get("ValidacionMensajesError","Error.G6")+"\n\n\r");
	            }
    		}else if (finca.getDirParcela().esRustica()){
    			//Si la finca es rustica se comprueba que el código de polígono sea correcto
    			if ((finca.getDirParcela().getCodPoligono() != null && !finca.getDirParcela().getCodPoligono().equals(""))&&
    					(!comprobarCodigoPoligono(conn,finca.getDirParcela().getCodPoligono()))){
    				if(sbVal == null){
    	        		sbVal = new StringBuffer();
    	        	}
    	        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G7")+"\n\n\r");
    			}
    			//Si la finca es rustica se comprueba que el código de paraje sea correcto
	   	    	if ((finca.getDirParcela().getCodParaje()!=null && finca.getDirParcela().getCodParaje().length() > 0)
	 	    			&&(!comprobarCodigoParaje(conn, finca.getDirParcela()))){
	   	    		if(sbVal == null){
	   	        		sbVal = new StringBuffer();
	   	        	}
	   	        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G8")+"\n\n\r");
	   	    	}
    			//Si la finca es rustica se comprueba que la zona de concentración sea correcta
		    	if ((finca.getDirParcela().getCodZonaConcentracion()!=null&&finca.getDirParcela().getCodZonaConcentracion().length() > 0)&&
	                    (!comprobarZonaConcentracion(conn, finca.getDirParcela().getCodZonaConcentracion()))){
		    		if(sbVal == null){
		        		sbVal = new StringBuffer();
		        	}
		        	sbVal.append(I18N.get("ValidacionMensajesError","Error.M1")+"\n\n\r");
		    	}
		    	//Si la finca tiene cultivos se ha de indicar el municipio agregado y comprobar si es correcto
		    	if (finca.getLstCultivos().size()>0){
			    	if (finca.getDirParcela().getCodMunOrigenAgregacion()!=null&&finca.getDirParcela().getCodMunOrigenAgregacion().length() > 0) 
			    	{
			    		if (Long.parseLong(finca.getDirParcela().getCodMunOrigenAgregacion()) > 0 && !comprobarMunicipioAgregado(conn, Long.parseLong(finca.getDirParcela().getCodMunOrigenAgregacion()))){
			    			if(sbVal == null){
			            		sbVal = new StringBuffer();
			            	}
			            	sbVal.append(I18N.get("ValidacionMensajesError","Error.M2")+"\n\n\r");
			    		}
			    	}else{
			    		if(sbVal == null){
			        		sbVal = new StringBuffer();
			        	}
			        	sbVal.append(I18N.get("ValidacionMensajesError","Error.M21")+"\n\n\r");
			    	};
		    	}
    		}
    	} catch (Exception e) {
    		throw new DataExceptionCatastro(e);
		}


        //PONENCIA
		//Si es necesaria la ponencia,se comprueba que esté indicado el año de ponencia, y que sea correcto 
        if(finca.getDatosEconomicos().getAnioAprobacion() != null){
            ponencia = Ponencia.recopilarDatos(conn, finca.getDatosEconomicos().getAnioAprobacion());
            //Se considerará que el tipo de ponencia será V si el coeficiente de coordinación 
            //en modificaciones de planeamiento es mayor que 0 
	        if (Ponencia.getCoefCoordPlan(conn, finca.getDirParcela().getCodPoligono())>0)
	           	ponencia.setTipoPonencia("V");
        }else if (CriteriosValidacion.esNecesariaPonencia(finca)){
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G2")+"\n\n\r");
        }
        
        String tipoPonencia = null;
        int anioNormas = 0;
        int anioRevisionTotal = 0;

        if(ponencia!=null)
        {
            tipoPonencia = ponencia.getTipoPonencia();
		    anioNormas = ponencia.getAnioNormas().intValue();
		    anioRevisionTotal = ponencia.getAnioEfectosTotal().intValue();
        }else if (CriteriosValidacion.esNecesariaPonencia(finca)){
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G3")+"\n\n\r");
        }

        if (esPonenciaCorrecta)
    	{
    		
	    	//Si en la finca hay unidades constructivas urbanas o  si se indica el polígono y 
	    	//la ponencia es correcta, se comprueba la existencia del polígono
	    	if (CriteriosValidacion.existeUrbanaEnFinca(finca)){
	    		if (finca.getDirParcela().getCodPoligono() != null && !finca.getDirParcela().getCodPoligono().equals(""))
		    		if (!comprobarCodigoPoligono(conn,finca.getDirParcela().getCodPoligono()))
		    				if (anioRevisionTotal >=2006){
		    					if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G9")+"\n\n\r");
		    				}    		
	    	}
	    	
    		//Situacion de la ponencia
    		if (CriteriosValidacion.esNecesariaPonencia(finca))
    		{
    			if ((tipoPonencia != null) && (tipoPonencia.equalsIgnoreCase("P") || tipoPonencia.equalsIgnoreCase("V"))){
    				if(sbVal == null){
    	        		sbVal = new StringBuffer();
    	        	}
    	        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G10")+"\n\n\r");
    			}
    		}
    		
    		//Reparto de vuelo
    		if (tipoPonencia.equalsIgnoreCase("V"))
    		{
    			if (finca.getDatosEconomicos().getIndModalidadReparto()!=null &&
                        !finca.getDatosEconomicos().getIndModalidadReparto().equalsIgnoreCase("N")){
    				if(sbVal == null){
    	        		sbVal = new StringBuffer();
    	        	}
    	        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G13")+"\n\n\r");
    			}
    		}
    		else
    		{
    			if ((anioNormas > 1986)&&(anioRevisionTotal < 2006))
    				if (!(finca.getDatosEconomicos().getIndModalidadReparto().equalsIgnoreCase("1") ||
                    finca.getDatosEconomicos().getIndModalidadReparto().equalsIgnoreCase("2") ||
                    finca.getDatosEconomicos().getIndModalidadReparto().equalsIgnoreCase("3") ||
                    finca.getDatosEconomicos().getIndModalidadReparto().equalsIgnoreCase("A") ||
                    finca.getDatosEconomicos().getIndModalidadReparto().equalsIgnoreCase("B") ||
                    finca.getDatosEconomicos().getIndModalidadReparto().equalsIgnoreCase("C") ||
                    finca.getDatosEconomicos().getIndModalidadReparto().equalsIgnoreCase("N"))){
    					if(sbVal == null){
    		        		sbVal = new StringBuffer();
    		        	}
    		        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G13")+"\n\n\r");
    				}
    		}
    		

    		//Datos de valoración
    		if (anioRevisionTotal < 2006)
    		{
    			//Busqueda de la fórmula de validación de la finca
    			if ((formulaCalculo.intValue() == -1)||(formulaCalculo.intValue() == 41)){
    				if(sbVal == null){
    	        		sbVal = new StringBuffer();
    	        	}
    	        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G14")+"\n\n\r");
    			}
    		}else
            {
                //Deducción de la fórmual de valoración
    			if ((formulaCalculo.intValue() == -1)||(formulaCalculo.intValue() == 41)){
    				if(sbVal == null){
    	        		sbVal = new StringBuffer();
    	        	}
    	        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G14")+"\n\n\r");
    			}
    		}
    		
    		//Finca infradificada
    		//Criterios de exclusión.
    		
    		if (!criteriosExclusionInfraedificada(finca) || construccionPorFases(finca))
            {
                finca.getDatosEconomicos().setIndInfraedificabilidad("");
            }
            else
            {
                casos134Infraedificada(conn, finca);
            }
    		

    	
    		
            //Restricción en modificación de planteamiento
    		if (tipoPonencia.equalsIgnoreCase("V") && (CriteriosValidacion.existeUrbanaEnFinca(finca)))
    		{
    			if ((anioNormas < 1989)&&(finca.getDatosEconomicos().getCodigoCalculoValor().intValue() != 11)
    				&&(finca.getDatosEconomicos().getCodigoCalculoValor().intValue() != 31)&&(finca.getDatosEconomicos().getCodigoCalculoValor().intValue() != 42)){
    				if(sbVal == null){
    	        		sbVal = new StringBuffer();
    	        	}
    	        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G15")+"\n\n\r");
    			}
    			
    			if ((anioNormas >= 1989)&&(finca.getDatosEconomicos().getCodigoCalculoValor().intValue() != 11)
        				&&(finca.getDatosEconomicos().getCodigoCalculoValor().intValue() != 32)&&(finca.getDatosEconomicos().getCodigoCalculoValor().intValue() != 42)){
    				if(sbVal == null){
    	        		sbVal = new StringBuffer();
    	        	}
    	        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G15")+"\n\n\r");
    			}
    		}
    		
    		if (CriteriosValidacion.esNecesariaPonencia(finca) && (!tipoPonencia.equalsIgnoreCase("V")))
    		{
    			if (CodigoTipoExpediente.equalsIgnoreCase("ALCN")
    					|| CodigoTipoExpediente.equalsIgnoreCase("ALCM")){
    				if(sbVal == null){
    	        		sbVal = new StringBuffer();
    	        	}
    	        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G16")+"\n\n\r");
    			}
    		}
    	}   	
    	
    	//VALIDACION SUPERFICIE
    	if (finca.getDatosFisicos().getSupFinca() == null || finca.getDatosFisicos().getSupFinca()<=0)
    	{
    		int valor = 0;
    		if (formulaCalculo!= null)
    			valor = formulaCalculo.intValue();
    		if (!(((valor == 30)||(anioRevisionTotal > 2005))&&(sumaSuelos(finca) > 9999999))){
    			if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("ValidacionMensajesError","Error.G55")+"\n\n\r");
    		}
    	}
    		
    	
    	//Sumamos al solar el 20%
    	long PorcentajeSolar = (finca.getDatosFisicos().getSupFinca().longValue()) + (((finca.getDatosFisicos().getSupFinca().longValue()) * 20) / 100);
    	if ((finca.getDatosFisicos().getSupCubierta().longValue()) > PorcentajeSolar){
    		if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G11")+"\n\n\r");
    	}
    	
    	if ((finca.getDatosFisicos().getSupSobreRasante().longValue()) > (finca.getDatosFisicos().getSupTotal().longValue())){
    		if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G12")+"\n\n\r");
    	}
    	
    	if ((finca.getDatosFisicos().getSupSobreRasante().longValue()+finca.getDatosFisicos().getSupBajoRasante()) != finca.getDatosFisicos().getSupTotal().longValue()){
    		if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.M22")+"\n\n\r");
    	}

    	if ( finca.getDatosEconomicos().getAnioAprobacion() != null &&
                (finca.getDatosEconomicos().getAnioAprobacion().longValue()) >= 2005)
        {
    		//Si la superficie construida es mayor que 0, es necesario indicar la superficie cubierta,
    		//la cual no puede ser mayor a la construida.
    		if ((finca.getDatosFisicos().getSupTotal().longValue()) > 0)
    		{
    			if ((finca.getDatosFisicos().getSupCubierta() == null)||((finca.getDatosFisicos().getSupCubierta()!=null
                        && finca.getDatosFisicos().getSupTotal()!=null)&&finca.getDatosFisicos().getSupCubierta().longValue()
                        > finca.getDatosFisicos().getSupTotal().longValue())){
    				if(sbVal == null){
    	        		sbVal = new StringBuffer();
    	        	}
    	        	sbVal.append(I18N.get("ValidacionMensajesError","Error.M3")+"\n\n\r");
    			}

    		}
    	}else if ((finca.getDatosEconomicos().getPoligonoCatastralValor()!=null&&  finca.getDatosEconomicos().getPoligonoCatastralValor().getCodPoligono()!=null&&
                finca.getDatosEconomicos().getPoligonoCatastralValor().getCodPoligono().length() > 0))
    	{
    		if (finca.getDatosFisicos().getSupTotal()==null||finca.getDatosFisicos().getSupTotal().longValue() <=0){
    			if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("ValidacionMensajesError","Error.M4")+"\n\n\r");
    		}
	
    		if (finca.getDatosFisicos().getSupCubierta() == null){
    			if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("ValidacionMensajesError","Error.M5")+"\n\n\r");
    		}
    		
    		else if ((finca.getDatosFisicos().getSupCubierta()!=null&&
                    finca.getDatosFisicos().getSupCubierta().longValue() <= 0) &&
                    (finca.getDatosEconomicos().getCodigoCalculoValor()!=null &&
                            finca.getDatosEconomicos().getCodigoCalculoValor().intValue() != 30)){
    			if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("ValidacionMensajesError","Error.M5")+"\n\n\r");
    		}
    	}
		
		return sbVal;
	}


    /*************************************************  JORGE  *********************************************************/

    public StringBuffer validacionesGlobalesFinca(Connection conn, DatosValoracion datosVal, FincaCatastro finca, boolean hayRustica,
                                            boolean esCorrectaPonencia, String idMunicipio)throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
    	
        float superficieCultivos = calculaSuperficieCultivos(finca);
        float superficieSuelos = calculaSuperficieSuelos(finca);
        float superficieUCs = calculaSuperficieUCs(finca);
        String indicadorValidacionSuperficieCultivos = calculaIndicadorValidacionSuperficieSuelos(conn, finca, datosVal, idMunicipio,superficieSuelos);
        if((indicadorValidacionSuperficieCultivos.equalsIgnoreCase("S")||(hayRustica&&esCorrectaPonencia))||
                (!esCorrectaPonencia&&superficieCultivos!=0))
        {
            if((superficieSuelos+superficieCultivos+superficieUCs)!=finca.getDatosFisicos().getSupFinca().longValue())
            {
                if(!hayToleranciaEspecialCierreRevisiones(finca,superficieCultivos))
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("ValidacionMensajesError","Error.G163")+"\n\n\r");
                }
            }
        }
        if(superficieCultivos>0 &&!(finca.getDatosFisicos().getSupFinca().floatValue()==(superficieCultivos+superficieUCs)))
        {
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G164")+"\n\n\r");
        }
        
        return sbVal;
    }

    private float calculaSuperficieCultivos(FincaCatastro finca)
    {
        float superficie = 0;
        ArrayList listaCultivos = finca.getLstCultivos();
        if(listaCultivos!=null)
        {
            for(int i=0; i<listaCultivos.size();i++)
            {
                Cultivo cultivo = (Cultivo)listaCultivos.get(i);
                if(cultivo.getTipoSuelo()!=null && cultivo.getTipoSuelo().equalsIgnoreCase(rustica))
                {
                    superficie = superficie + cultivo.getSuperficieParcela().longValue();
                }
            }
        }
        return superficie;
    }

    private float calculaSuperficieSuelos(FincaCatastro finca)
    {
    	float superficie=0;
    	ArrayList listaSuelos = finca.getLstSuelos();
    	if (listaSuelos != null){
    		for(int i=0; i<listaSuelos.size();i++)
    		{
    			SueloCatastro suelo = (SueloCatastro)listaSuelos.get(i);
    			superficie = superficie + suelo.getDatosFisicos().getSupOcupada().longValue();
    		}
    	}
    	return superficie;
    }

    private float calculaSuperficieUCs(FincaCatastro finca)
    {
        float superficie=0;
        ArrayList listaUC = finca.getLstUnidadesConstructivas();
        if(listaUC!=null)
        {
            for(int i=0; i<listaUC.size();i++)
            {
                UnidadConstructivaCatastro uc = (UnidadConstructivaCatastro)listaUC.get(i);
                if(uc.getTipoUnidad().equalsIgnoreCase(rustica))
                {
                    superficie = superficie + uc.getDatosFisicos().getSupOcupada().longValue();
                }
            }
        }
        return superficie;
    }

    private String calculaIndicadorValidacionSuperficieSuelos(Connection conn,FincaCatastro finca, DatosValoracion datosVal,
                                                               String idMunicipio, float superficieSuelos)
            throws DataExceptionCatastro
    {
    	int annoRevisionTotal = getAnnoRevisionTotal(conn, idMunicipio, finca.getDatosEconomicos().getAnioAprobacion());

    	if (datosVal != null){
    		Integer annoNormas = datosVal.getAnnoDeLasNormas();
    		if (annoNormas != null){
    			if((finca.getDatosFisicos().getSupFinca().floatValue()==(float)0 &&(datosVal.getFormaDeCalculo().floatValue()==(float)30 ||
    					annoRevisionTotal>2005)&&superficieSuelos>9999999)||((annoNormas.intValue()==1989 ||annoNormas.intValue()==1993)&&annoRevisionTotal<2006
    							&&existeSueloTipoValor2Distinto(finca)))
    			{
    				return "N";
    			}
    			if(annoNormas.intValue()==1982 || annoNormas.intValue()==1986)
    			{
    				return datosVal.getIndicadorValidacionSuperficieTotalSubParcelas();
    			}
    		}
    	}
        return "S";
    }

    private int getAnnoRevisionTotal(Connection conn, String idMunicipio, Integer annoAprobacion)  throws DataExceptionCatastro
    {
        PreparedStatement s = null;
        ResultSet r = null;
        int annoApro = 0;
        if(annoAprobacion!=null)
        {
            annoApro = annoAprobacion.intValue();
        }
        int anno = -1;
        try
        {
            s = conn.prepareStatement("MCobtenerAnnoEfectosTotalPonencia");
            s.setInt(1,annoApro);
            s.setInt(2, Integer.valueOf(idMunicipio).intValue());
            r = s.executeQuery();
            if(r.next())
            {
                anno = r.getInt("anno_efectostotal");
            }
        }
        catch (SQLException ex)
        {
            throw new DataExceptionCatastro(ex);
        }
        finally
        {
            if (s!=null) try{s.close();}catch(Exception e){};
            if (r!= null) try{r.close();}catch(Exception e){};
        }
        return anno;
    }

    private int getAnnoNormasPonencia(Connection conn, String idMunicipio, Integer annoAprobacion) throws DataExceptionCatastro
    {
        int annoNormas = -1;
        PreparedStatement s = null;
        ResultSet r = null;
        int annoApro = 0;
        if(annoAprobacion!=null)
        {
            annoApro = annoAprobacion.intValue();
        }
        try
        {
            s = conn.prepareStatement("MCobtenerAnnoDeNormasPonencia");
            s.setInt(1,annoApro);
            s.setInt(2, Integer.valueOf(idMunicipio).intValue());
            r = s.executeQuery();
            if(r.next())
            {
                annoNormas = r.getInt("anno_normas");
            }
        }
        catch (SQLException ex)
        {
            throw new DataExceptionCatastro(ex);
        }
        finally
        {
            if (s!=null) try{s.close();}catch(Exception e){};
            if (r!= null) try{r.close();}catch(Exception e){};
        }
        return annoNormas;
    }

    private boolean existeSueloTipoValor2Distinto(FincaCatastro finca)
    {
        ArrayList listaSuelo = finca.getLstSuelos();
        boolean haySuelo2Distinto = false;
        if (listaSuelo != null){
        	for(int i = 0;i<listaSuelo.size() && !haySuelo2Distinto;i++)
        	{
        		SueloCatastro suelo = (SueloCatastro)listaSuelo.get(i);
        		if(!suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("2"))
        		{
        			haySuelo2Distinto = true;
        		}
        	}
        }
        return haySuelo2Distinto;
    }

    private boolean hayToleranciaEspecialCierreRevisiones(FincaCatastro finca,float superficieCultivos)
    {
        return superficieCultivos == finca.getDatosFisicos().getSupFinca().floatValue();    
    }

}
