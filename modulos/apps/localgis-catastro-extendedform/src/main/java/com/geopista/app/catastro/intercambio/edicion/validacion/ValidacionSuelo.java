/**
 * ValidacionSuelo.java
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

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.exception.DataExceptionCatastro;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.SueloCatastro;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaCNT;
import com.vividsolutions.jump.I18N;

public class ValidacionSuelo
{
	private String metodoCalculoValor;
	private String tipoRelacionCargos;
	private Integer numMinSubparcelas;
	private boolean esCorrectaFormulaCalculo;
	private boolean esCorrectaPonencia;
	private int anioNormas;
	private int anioRevisionTotal;
	private PonenciaCNT ponencia;
	
	
	public ValidacionSuelo(DatosValoracion datosValoracion)
	{
		this.metodoCalculoValor = datosValoracion.getMetodoCalculoValorSuelo();
		this.tipoRelacionCargos = datosValoracion.getTipoRelacionEntreCargosLocales();
		this.numMinSubparcelas = datosValoracion.getNumeroMinimoSubparcelas();
		this.esCorrectaFormulaCalculo = datosValoracion.isEsCorrectaFormaCalculo();
		this.esCorrectaPonencia = datosValoracion.isEsCorrectaFormaCalculo();
		
	}
	
	/**
	 * Método que comprueba que el tramo exista.
	 * @param conn Conexión a la BBDD
	 * @param tramo Tramo a validar
	 * @return
	 * @throws Exception
	 */
	private boolean existeTramo(Connection conn, String tramo) throws Exception
	{
		String sSQL = "MCobtenerCodViaTramo"; 
		 //String sSQL= "select * from vias where nombreviaine="+codigoTipoVia;
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setString(1, tramo);
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
	 * Comprueba que la vía exista.
	 * @param conn Conexión a la BBDD.
	 * @param codVia Codigo de la vía a validar.
	 * @return
	 * @throws Exception
	 */
	private boolean existeVia(Connection conn, String codVia) throws Exception
	{
		String sSQL = "MCcompruebaCodVia"; 
		 //String sSQL= "select * from vias where nombreviaine="+codigoTipoVia;
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setString(1, codVia);
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
	
	
	private boolean existeZonaValor(Connection conn,String codZonaValor, Integer anioAprobacion, String codMunicipio,
                                    String codDelegacion) throws Exception {
		String sSQL = "MCgetZonaValor"; 
		 //String sSQL= "select codigo_zonavalor from ponencia_zonavalor where anno_aprobacion="+anioAprobacion
		//				AND	codigo_municipio_meh=codMunicipio AND codigo_delegacionmeh= codDelegacion;
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setInt(1, anioAprobacion.intValue());
	            ps.setString(2, codMunicipio);
	            ps.setString(3, codDelegacion);        
                rs= ps.executeQuery();
	            if(rs.next())
	            {
	            	if (codZonaValor.equalsIgnoreCase(rs.getString("codigo_zonavalor")))
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
	
	private boolean existeZonaUrbanistica(Connection conn,String codZonaValor, Integer anioAprobacion, String codMunicipio, String codDelegacion) throws Exception {
		String sSQL = "MCgetZonaValorUrbanistica";
		 //String sSQL= "select codigo_zona from ponencia_urbanistica where anno_aprobacion="+anioAprobacion
		//				AND	codigo_municipio_meh=codMunicipio AND codigo_delegacionmeh= codDelegacion;
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setInt(1, anioAprobacion.intValue());
	            ps.setString(2, codMunicipio);
	            ps.setString(3, codDelegacion);
                rs= ps.executeQuery();
	            if(rs.next())
	            {
	            	if (codZonaValor.equalsIgnoreCase(rs.getString("codigo_zona")))
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
	
	
	private boolean codigoPoligonoIgualTramo(Connection conn,String codPoligono, Integer anioAprobacion) throws Exception {
		String sSQL = "MCgetPoligonoTramo"; 
		 //String sSQL= "select codigo_poligono from ponencia_tramos where anno_aprobacion="+anioAprobacion
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setInt(1, anioAprobacion.intValue());
	            rs= ps.executeQuery();
	            if(rs.next())
	            {
	            	if (codPoligono.equalsIgnoreCase(rs.getString("codigo_poligono")))
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
	
	private int getMBRInicio(Connection conn,int anioRevisionTotal, String codZonaValor) throws Exception {
		String sSQL = "MCgetMbrInicio"; 
		 //String sSQL= "select MBR_INICIO from zona_valor_aa where ejercicio="+anioRevisionTotal
						// "AND zona_valor_aa="+codZonaValor;
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setInt(1, anioRevisionTotal);
	            ps.setString(2, codZonaValor);
	            rs= ps.executeQuery();
	            if(rs.next())
	               	return rs.getInt("MBR_INICIO");
 
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
		return -1;
	}
	
	private int getMBRFin(Connection conn,int anioRevisionTotal, String codZonaValor) throws Exception {
		String sSQL = "MCgetMbrFin"; 
		 //String sSQL= "select MBR_FIN from zona_valor_aa where ejercicio="+anioRevisionTotal
						// "AND zona_valor_aa="+codZonaValor;
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setInt(1, anioRevisionTotal);
	            ps.setString(2, codZonaValor);
	            rs= ps.executeQuery();
	            if(rs.next())
	               	return rs.getInt("MBR_FIN");
 
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
		return -1;
	}
	
	private double recuperarImporteMBR(Connection conn,String codPoligono) throws Exception {
		String sSQL = "MCrecuperarImporteMBR"; 
		 //String sSQL= "select MBR from poligono where POLIGONO="+codPoligono
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setString(1, codPoligono);
	            rs= ps.executeQuery();
	            if(rs.next())
	               	return rs.getDouble("MBR");
 
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
		return -1;
	}
	
	private int getMBR(Connection conn,double valor) throws Exception {
		String sSQL = "MCgetMBR"; 
		 //String sSQL= "select MBR from mbr where VALOR="+valor
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setDouble(1, valor);
	            rs= ps.executeQuery();
	            if(rs.next())
	               	return rs.getInt("MBR");
 
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
		return -1;
	}

	private boolean zonaValorFueraLimites(Connection conn, FincaCatastro finca, String codZonaValor) throws DataExceptionCatastro {
		try {
			int anioRevisionTotal = ponencia.getAnioEfectosTotal().intValue();
			
				
			int mbr_inicio = getMBRInicio(conn, anioRevisionTotal,codZonaValor);
			int mbr_fin = getMBRFin(conn, anioRevisionTotal,codZonaValor);
			
			
			double importeMBR = recuperarImporteMBR(conn, finca.getDirParcela().getCodPoligono());
			int mbr = getMBR(conn, importeMBR);
			
			if ((mbr < mbr_inicio) || (mbr > mbr_fin))
				return true;
		} catch (Exception e) {
			throw new DataExceptionCatastro(e);
		}
		return false;
	}

	
	
	/**
	 * Método que lleva a cabo la validación general de los suelos.
	 * @param lstSuelos Lista de los suelos a validar.
	 * @return
	 */
	private StringBuffer validacionGeneralSuelos(ArrayList lstSuelos)
	{
		String numOrden = null;
		boolean tipoFachadaFA = false;
		StringBuffer sbVal = null;
		for (int i=0;i<lstSuelos.size();i++)
		{
			SueloCatastro suelo = (SueloCatastro)lstSuelos.get(i);
			//Comprobar la naturaleza del suelo
			
			if (numOrden!= null)
			{
				if (suelo.getNumOrden().equalsIgnoreCase(numOrden)){
					if(sbVal == null){
		        		sbVal = new StringBuffer();
		        	}
		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.M19")+"\n\n\r");
				}
			}
			numOrden = suelo.getNumOrden();
			
			if (suelo.getDatosFisicos().getTipoFachada()!=null &&suelo.getDatosFisicos().getTipoFachada().equalsIgnoreCase("FA"))
				tipoFachadaFA = true;
		}
		
		if (!tipoFachadaFA){
			if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G53")+"\n\n\r");
		}
		
		return sbVal;
	}
	
	/**
	 * Método que comprueba que no exista ningún suelo con el numero de orden duplicado.
	 * @param suelo Suelo que contiene el numero de orden a comprobar si está duplicado.
	 * @param finca Finca que contiene la lista de suelos donde buscar repetidos.
	 * @return
	 */
	private boolean duplicadoNumOrden(SueloCatastro suelo, FincaCatastro finca)
	{
		ArrayList lstSuelos = finca.getLstSuelos();
		int contadorRepetidos = 0;
		
		if (lstSuelos != null)
		{
			for (int i=0;i<lstSuelos.size();i++)
			{
				SueloCatastro s = (SueloCatastro) lstSuelos.get(i);
				
				if (s.getNumOrden().equalsIgnoreCase(suelo.getNumOrden()))
					contadorRepetidos += 1;
			}
		}
		
		if (contadorRepetidos > 1)
			return true;
		
		return false;
	}
	
	
	/**
	 * Método que realiza la validación particular de cada suelo.
	 * @param suelo Suelo a validar.
	 * @param finca Finca que lo contiene.
	 * @return
	 * @throws DataExceptionCatastro 
	 */
	public StringBuffer validacionParcialSuelo(Connection conn, SueloCatastro suelo, FincaCatastro finca, String idMunicipio) throws DataExceptionCatastro
	{
		StringBuffer sbVal = null;
		try {
			if (!suelo.getDatosEconomicos().getZonaValor().equals("RUSTI")){
				if (suelo.getNumOrden().equalsIgnoreCase("0") || duplicadoNumOrden(suelo, finca)){
					if(sbVal == null){
		        		sbVal = new StringBuffer();
		        	}
		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.M6")+"\n\n\r");
				}

				 if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
					 //expediente  de situaciones finales
		        	if (((finca.getDatosFisicos().getSupFinca().longValue()) > 0)&&(((suelo.getDatosEconomicos().getCodTipoValor()!=null&&suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("2")) || (finca.getDatosEconomicos().getAnioAprobacion() != null && finca.getDatosEconomicos().getAnioAprobacion().intValue() > 2005))))
		        	{
			        	
		        		if (suelo.getDatosFisicos().getSupOcupada() != null){
		        			//Sup suelo mayor que la superficie del solar
		        			if (suelo.getDatosFisicos().getSupOcupada().longValue() > finca.getDatosFisicos().getSupFinca().longValue()){
		        				if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G18")+"\n\n\r");
		        			}
		        		}
		        		else{
		        			if(sbVal == null){
	    		        		sbVal = new StringBuffer();
	    		        	}
	    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
	    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G170")+"\n\n\r");
		        		}
			        }
				 }
		        
		        if (!suelo.getDatosFisicos().getTipoFachada().equalsIgnoreCase("FA")&& suelo.getDatosFisicos().getSupOcupada().longValue() == 0){
		        	if(sbVal == null){
		        		sbVal = new StringBuffer();
		        	}
		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G18")+"\n\n\r");
		        }

		        if (suelo.getDatosEconomicos().getNumFachadas()==null||(!suelo.getDatosEconomicos().getNumFachadas().equalsIgnoreCase("N") && !suelo.getDatosEconomicos().getNumFachadas().equalsIgnoreCase("2") && !suelo.getDatosEconomicos().getNumFachadas().equalsIgnoreCase("3"))){
		        	if(sbVal == null){
		        		sbVal = new StringBuffer();
		        	}
		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G19")+"\n\n\r");
		        }
	        
		        if (suelo.getDatosEconomicos().isCorrectorLongFachada().booleanValue() && (suelo.getDatosFisicos().getLongFachada()==null||suelo.getDatosFisicos().getLongFachada().floatValue()<=0)){
		        	if(sbVal == null){
		        		sbVal = new StringBuffer();
		        	}
		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G20")+"\n\n\r");
		        }
 
		       
		        if (esCorrectaPonencia)
		        {                               
	                //Ponencia posterior 2005
	                ponencia = Ponencia.recopilarDatos(conn, finca.getDatosEconomicos().getAnioAprobacion());
	                anioNormas = ponencia.getAnioNormas().intValue();
	                anioRevisionTotal = ponencia.getAnioEfectosTotal().intValue();
	                if (finca.getDatosEconomicos().getAnioAprobacion().intValue() >= 2005)
	                {
	
	                	//Comprobación Zona Valor
	                	if (!existeZonaValor(conn, suelo.getDatosEconomicos().getZonaValor().getCodZonaValor(), finca.getDatosEconomicos().getAnioAprobacion(), finca.getCodMunicipioDGC(), finca.getCodDelegacionMEH())){
	                		if(sbVal == null){
	    		        		sbVal = new StringBuffer();
	    		        	}
	    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
	    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G21")+"\n\n\r");
	                	}
	
	                	if (suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("R")||suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("PR"))
	                	{
	                		if (zonaValorFueraLimites(conn, finca, suelo.getDatosEconomicos().getZonaValor().getCodZonaValor())){
	                			if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G168")+"\n\n\r");
	                		}
	                	}
	
	                    //Comprobación Zona Urbanística
	                    if (!existeZonaUrbanistica(conn, suelo.getDatosEconomicos().getZonaUrbanistica().getCodZona(), finca.getDatosEconomicos().getAnioAprobacion(), finca.getCodMunicipioDGC(), finca.getCodDelegacionMEH())){
	                    	if(sbVal == null){
	    		        		sbVal = new StringBuffer();
	    		        	}
	    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
	    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G24")+"\n\n\r");
	                    }
	
	                    if (!suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("P")&&(suelo.getDatosEconomicos().getZonaUrbanistica().getCodZona().equalsIgnoreCase("PP") || suelo.getDatosEconomicos().getZonaUrbanistica().getCodZona().equalsIgnoreCase("UG"))){
	                    	if(sbVal == null){
	    		        		sbVal = new StringBuffer();
	    		        	}
	    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
	    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G22")+"\n\n\r");
	                    }

	                    if ((suelo.getDatosEconomicos().getZonaUrbanistica().getCodZona().equalsIgnoreCase("PP") && suelo.getDatosEconomicos().getZonaUrbanistica().getCodZona().equalsIgnoreCase("UG"))&&(!suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("P"))){
	                    	if(sbVal == null){
	    		        		sbVal = new StringBuffer();
	    		        	}
	    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
	    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G22")+"\n\n\r");
	                    }

	                    //Comprobamos que el estado de la ponencia no sea null (ogalende)
	                    if(ponencia.getEstadoPonencia()!=null){
		                    if (ponencia.getEstadoPonencia().equalsIgnoreCase("P") || ponencia.getEstadoPonencia().equalsIgnoreCase("V"))
		                    {
		                        if (!suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("PP") || !suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("UG") || !suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("UP")){
		                        	if(sbVal == null){
		                        		sbVal = new StringBuffer();
		                        	}
		                        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		        							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.M7")+"\n\n\r");
		                        }
		                    }
	                    }

	                    //Zonas de unitario
	                    if ((suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("U")||suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("PU"))&&(!suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("PP")&&(!suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("UG"))))
	                    {
	                        if ((suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("VP")|| suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("VL"))&& (suelo.getDatosEconomicos().getZonaValor().getValorZonaVerde().floatValue() == (float)0)){
	                        	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G27")+"\n\n\r");
	                        }
	
	                        if (suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("EQ")||suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("CV")||suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("DP")||suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("EN")||suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("RL")||suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("SC")||suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("SN")||suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("TN"))
	                        {
	                            if (suelo.getDatosEconomicos().getZonaValor().getValorEquipamientos().floatValue() == (float)0){
	                            	if(sbVal == null){
	    	    		        		sbVal = new StringBuffer();
	    	    		        	}
	    	    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
	    	    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G28")+"\n\n\r");
	                            }
	                        }
	                    }
	
	                    //Zonas de repercusión
	                    if (((metodoCalculoValor != null) && (metodoCalculoValor.length() <1))
	                            &&(suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("R")||suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("PR"))&&(!suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("PP") && suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("UG")))
	                    {
	                        if (suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("VP") || suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("VL"))
	                        {
	                            if ((suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().getZonaVerde().longValue() == 0) || (suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor().getZonaVerde().longValue() == 0)){
	                            	if(sbVal == null){
	    	    		        		sbVal = new StringBuffer();
	    	    		        	}
	    	    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
	    	    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G29")+"\n\n\r");
	                            }
	                        }
	
	                        if (suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("EQ")||suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("CV")||suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("DP")||suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("EN")||suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("RL")||suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("SC")||suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("SN")||suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("TN"))
	                        {
	                            if ((suelo.getDatosEconomicos().getZonaUrbanistica().getEdificabilidad().getEquipamientos().floatValue() == (float)0)||(suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor().getEquipamientos().floatValue() == (float)0)){
	                            	if(sbVal == null){
	    	    		        		sbVal = new StringBuffer();
	    	    		        	}
	    	    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
	    	    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G30")+"\n\n\r");
	                            }
	                        }
	                    }
	
	                    //Zonas pendientes de desarrollar
	                    if ((suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("P"))&&(suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("PP") || suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("UG")))
	                    {
	                        if (suelo.getDatosEconomicos().getZonaValor().getValorSinDesarrollar().floatValue() < (float)1){
	                        	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G31")+"\n\n\r");
	                        }
	                    }
	
	                }else{
	
		                //Ponencia Anterior
		                //Comprobamos también que codViaPonencia y CodTramoPonencia no sean "" (ogalende)
		                if ((suelo.getDatosEconomicos().getCodViaPonencia()!=null && !suelo.getDatosEconomicos().getCodViaPonencia().equals("")) 
		                		|| (suelo.getDatosEconomicos().getTramos() != null && suelo.getDatosEconomicos().getTramos().getCodTramo()!=null && !suelo.getDatosEconomicos().getTramos().getCodTramo().equals("")))
		                {
		                    if (!existeTramo(conn, suelo.getDatosEconomicos().getTramos().getCodTramo()) || !existeVia(conn, suelo.getDatosEconomicos().getCodViaPonencia())){
		                    	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G25")+"\n\n\r");
		                    }
		
		                    if (suelo.getDatosFisicos().getTipoFachada().equalsIgnoreCase("FA") && finca.getDirParcela().getCodPoligono()!=null && !finca.getDirParcela().getCodPoligono().equals(""))
		                        //Si el poligono de la finca no coincide con el del tramo
		                        {
		                            if (!codigoPoligonoIgualTramo(conn, finca.getDirParcela().getCodPoligono(), finca.getDatosEconomicos().getAnioAprobacion())){
		                            	if(sbVal == null){
		    	    		        		sbVal = new StringBuffer();
		    	    		        	}
		    	    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    	    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G26")+"\n\n\r");
		                            }
		                        }
		                	}

	                	}
			        if ((anioNormas < 1989)&& suelo.getDatosEconomicos().getCodTipoValor()==null){
			        	if(sbVal == null){
    		        		sbVal = new StringBuffer();
    		        	}
    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G32")+"\n\n\r");
			        }
			        if ((anioNormas < 1989)&&(suelo.getDatosEconomicos().getCodTipoValor()!=null&&(Integer.parseInt(suelo.getDatosEconomicos().getCodTipoValor()) < 0 || Integer.parseInt(suelo.getDatosEconomicos().getCodTipoValor()) > 7))){
			        	if(sbVal == null){
    		        		sbVal = new StringBuffer();
    		        	}
    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G32")+"\n\n\r");
			        }

			        if ((anioNormas >= 1989) && (anioRevisionTotal < 2006))
			        {
			            if (suelo.getDatosFisicos().getTipoFachada().equalsIgnoreCase("SI") && (suelo.getDatosEconomicos().getCodTipoValor() ==null||!suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("2"))){
			            	if(sbVal == null){
	    		        		sbVal = new StringBuffer();
	    		        	}
	    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
	    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G34")+"\n\n\r");
			            }
			            if (!suelo.getDatosFisicos().getTipoFachada().equalsIgnoreCase("SI") && suelo.getDatosEconomicos().getCodTipoValor()==null){
			            	if(sbVal == null){
	    		        		sbVal = new StringBuffer();
	    		        	}
	    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
	    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G35")+"\n\n\r");
			            }
			            if (!suelo.getDatosFisicos().getTipoFachada().equalsIgnoreCase("SI") && (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("3") || (Integer.parseInt(suelo.getDatosEconomicos().getCodTipoValor())<0) || (Integer.parseInt(suelo.getDatosEconomicos().getCodTipoValor())>9))){
			            	if(sbVal == null){
	    		        		sbVal = new StringBuffer();
	    		        	}
	    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
	    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G35")+"\n\n\r");
			            }
			        }

//			        if (anioNormas < 1989)
//			        {
//			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("0"))
//			            {
//			                if (suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor()==null || suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor().getUsoComercial().floatValue() == (float)0)
//			                    return "Error.G33";
//			            }
//			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("1"))
//			            {
//			                if (suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor()==null || suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor().getUsoResidencial().floatValue() == (float)0)
//			                    return "Error.G33";
//			            }
//			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("2"))
//			            {
//			                if (suelo.getDatosEconomicos().getZonaValor().getValorUnitario()==null||suelo.getDatosEconomicos().getZonaValor().getValorUnitario().floatValue() == (float)0)
//			                    return "Error.G33";
//			            }
//			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("4"))
//			            {
//			                if (suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor()==null||suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor().getUsoOficinas().floatValue() == (float)0)
//			                    return "Error.G33";
//			            }
//			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("5"))
//			            {
//			                if (suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor()==null||suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor().getUsoIndustrial().floatValue() == (float)0)
//			                    return "Error.G33";
//			            }
//			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("6"))
//			            {
//			                if (suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor()==null||suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor().getOtrosUsos1().floatValue() == (float)0)
//			                    return "Error.G33";
//			            }
//			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("7"))
//			            {
//			                if (suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor()==null||suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor().getOtrosUsos2().floatValue() == (float)0)
//			                    return "Error.G33";
//			            }
//			        }
//			
//			        if (anioNormas >= 1989 && anioRevisionTotal < 2006 )
//			        {
//			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("0"))
//			            {
//			                if (suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor()==null||suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor().getUsoComercial().floatValue() == (float)0)
//			                    return "Error.G33";
//			            }
//			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("1"))
//			            {
//			                if (suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor()==null||suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor().getUsoResidencial().floatValue() == (float)0)
//			                    return "Error.G33";
//			            }
//			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("2"))
//			            {
//			            	//comprobamos que el valor unitario no sea valor nulo (ogalende)
//			                if (suelo.getDatosEconomicos().getZonaValor().getValorUnitario()!=null && 
//			                    suelo.getDatosEconomicos().getZonaValor().getValorUnitario().floatValue() == (float)0)
//			                    return "Error.G33";
//			            }
//			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("4"))
//			            {
//			                if (suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor()==null||suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor().getUsoOficinas().floatValue() == (float)0)
//			                    return "Error.G33";
//			            }
//			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("5"))
//			            {
//			                if (suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor()==null||suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor().getUsoIndustrial().floatValue() == (float)0)
//			                    return "Error.G33";
//			            }
//			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("6"))
//			            {
//			                if (suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor()==null||suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor().getUsoTuristico().floatValue() == (float)0)
//			                    return "Error.G33";
//			            }
//			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("7"))
//			            {
//			                if (suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor()==null||suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor().getOtrosUsos1().floatValue() == (float)0)
//			                    return "Error.G33";
//			            }
//			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("8"))
//			            {
//			                if (suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor()==null||suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor().getOtrosUsos2().floatValue() == (float)0)
//			                    return "Error.G33";
//			            }
//			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("9"))
//			            {
//			                if (suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor()==null||suelo.getDatosEconomicos().getZonaValor().getImportesZonaValor().getOtrosUsos3().floatValue() == (float)0)
//			                    return "Error.G33";
//			            }
//			        }

			        if (anioNormas < 1989)
			        {
			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("0"))
			            {
			                if (suelo.getDatosEconomicos().getTramos().getBanda()==null || 
			                		suelo.getDatosEconomicos().getTramos().getBanda().getUsoComercial()==null ||
			                		suelo.getDatosEconomicos().getTramos().getBanda().getUsoComercial().floatValue() == (float)0){
			                	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G33")+"\n\n\r");
			                }
			            }
			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("1"))
			            {
			                if (suelo.getDatosEconomicos().getTramos().getBanda()==null || 
			                		suelo.getDatosEconomicos().getTramos().getBanda().getUsoResidencial() == null ||
			                		suelo.getDatosEconomicos().getTramos().getBanda().getUsoResidencial().floatValue() == (float)0){
			                	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G33")+"\n\n\r");
			                }
			            }
			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("2"))
			            {
			                if (suelo.getDatosEconomicos().getTramos()==null ||
			                		suelo.getDatosEconomicos().getTramos().getValorUnitario() == null || 
			                		suelo.getDatosEconomicos().getTramos().getValorUnitario().floatValue() == (float)0){
			                	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G33")+"\n\n\r");
			                }
			            }
			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("4"))
			            {
			                if (suelo.getDatosEconomicos().getTramos().getBanda()==null || 
			                		suelo.getDatosEconomicos().getTramos().getBanda().getUsoOficinas() == null || 
			                		suelo.getDatosEconomicos().getTramos().getBanda().getUsoOficinas().floatValue() == (float)0){
			                	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G33")+"\n\n\r");
			                }
			            }
			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("5"))
			            {
			                if (suelo.getDatosEconomicos().getTramos().getBanda()==null || 
			                		suelo.getDatosEconomicos().getTramos().getBanda().getUsoIndustrial() == null ||
			                		suelo.getDatosEconomicos().getTramos().getBanda().getUsoIndustrial().floatValue() == (float)0){
			                	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G33")+"\n\n\r");
			                }
			            }
			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("6"))
			            {
			                if (suelo.getDatosEconomicos().getTramos().getBanda()==null || 
			                		suelo.getDatosEconomicos().getTramos().getBanda().getOtrosUsos1() == null ||
			                		suelo.getDatosEconomicos().getTramos().getBanda().getOtrosUsos1().floatValue() == (float)0){
			                	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G33")+"\n\n\r");
			                }
			            }
			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("7"))
			            {
			                if (suelo.getDatosEconomicos().getTramos().getBanda()==null || 
			                		suelo.getDatosEconomicos().getTramos().getBanda().getOtrosUsos2() == null ||
			                		suelo.getDatosEconomicos().getTramos().getBanda().getOtrosUsos2().floatValue() == (float)0){
			                	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G33")+"\n\n\r");
			                }
			            }
			        }
			
			        if (anioNormas >= 1989 && anioRevisionTotal < 2006 )
			        {
			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("0"))
			            {
			                if (suelo.getDatosEconomicos().getTramos().getBanda()==null || 
			                		suelo.getDatosEconomicos().getTramos().getBanda().getUsoComercial() == null ||
			                		suelo.getDatosEconomicos().getTramos().getBanda().getUsoComercial().floatValue() == (float)0){
			                	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G33")+"\n\n\r");
			                }
			            }
			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("1"))
			            {
			                if (suelo.getDatosEconomicos().getTramos().getBanda()==null || 
			                		suelo.getDatosEconomicos().getTramos().getBanda().getUsoResidencial() == null ||
			                		suelo.getDatosEconomicos().getTramos().getBanda().getUsoResidencial().floatValue() == (float)0){
			                	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G33")+"\n\n\r");
			                }
			            }
			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("2"))
			            {
			            	//comprobamos que el valor unitario no sea valor nulo (ogalende)
			                if (suelo.getDatosEconomicos().getTramos().getValorUnitario()==null || 
			                		suelo.getDatosEconomicos().getTramos().getValorUnitario().floatValue() == (float)0){
			                	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G33")+"\n\n\r");
			                }
			            }
			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("4"))
			            {
			                if (suelo.getDatosEconomicos().getTramos().getBanda() == null || 
			                		suelo.getDatosEconomicos().getTramos().getBanda().getUsoOficinas() == null ||
			                		suelo.getDatosEconomicos().getTramos().getBanda().getUsoOficinas().floatValue() == (float)0){
			                	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G33")+"\n\n\r");
			                }
			            }
			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("5"))
			            {
			                if (suelo.getDatosEconomicos().getTramos().getBanda() == null || 
			                		suelo.getDatosEconomicos().getTramos().getBanda().getUsoIndustrial() == null ||
			                		suelo.getDatosEconomicos().getTramos().getBanda().getUsoIndustrial().floatValue() == (float)0){
			                	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G33")+"\n\n\r");
			                }
			            }
			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("6"))
			            {
			                if (suelo.getDatosEconomicos().getTramos().getBanda() == null || 
			                		suelo.getDatosEconomicos().getTramos().getBanda().getUsoTuristico() == null ||
			                		suelo.getDatosEconomicos().getTramos().getBanda().getUsoTuristico().floatValue() == (float)0){
			                	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G33")+"\n\n\r");
			                }
			            }
			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("7"))
			            {
			                if (suelo.getDatosEconomicos().getTramos().getBanda() == null || 
			                		suelo.getDatosEconomicos().getTramos().getBanda().getOtrosUsos1() == null ||
			                		suelo.getDatosEconomicos().getTramos().getBanda().getOtrosUsos1().floatValue() == (float)0){
			                	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G33")+"\n\n\r");
			                }
			            }
			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("8"))
			            {
			                if (suelo.getDatosEconomicos().getTramos().getBanda()==null || 
			                		suelo.getDatosEconomicos().getTramos().getBanda().getOtrosUsos2() == null ||
			                		suelo.getDatosEconomicos().getTramos().getBanda().getOtrosUsos2().floatValue() == (float)0){
			                	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G33")+"\n\n\r");
			                }
			            }
			            if (suelo.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("9"))
			            {
			                if (suelo.getDatosEconomicos().getTramos().getBanda()==null || 
			                		suelo.getDatosEconomicos().getTramos().getBanda().getOtrosUsos3() == null ||
			                		suelo.getDatosEconomicos().getTramos().getBanda().getOtrosUsos3().floatValue() == (float)0){
			                	if(sbVal == null){
		    		        		sbVal = new StringBuffer();
		    		        	}
		    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
		    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G33")+"\n\n\r");
			                }
			            }
			        }

			        //COEFICIENTES CORRECTORES
			        if (((anioNormas != 1993)||((tipoRelacionCargos != null) && (tipoRelacionCargos.length() > 0)))
			             &&(suelo.getDatosEconomicos().isCorrectorVPO().booleanValue())){
			        	if(sbVal == null){
    		        		sbVal = new StringBuffer();
    		        	}
    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G36")+"\n\n\r");
			        }

		            if ((anioNormas > 1982)&& (suelo.getDatosEconomicos().isCorrectorDesmonte().booleanValue())){
		            	if(sbVal == null){
    		        		sbVal = new StringBuffer();
    		        	}
    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G37")+"\n\n\r");
		            }
		
		            if ((anioNormas < 1986)&& (suelo.getDatosEconomicos().isCorrectorNoLucrativo().booleanValue())){
		            	if(sbVal == null){
    		        		sbVal = new StringBuffer();
    		        	}
    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G41")+"\n\n\r");
		            }

		            if (anioNormas < 1989)
		            {
		                if ((suelo.getDatosEconomicos().getCorrectorCargasSingulares().floatValue() != (float)0.7)&&(suelo.getDatosEconomicos().getCorrectorCargasSingulares().floatValue() != (float)1)){
		                	if(sbVal == null){
	    		        		sbVal = new StringBuffer();
	    		        	}
	    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
	    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G42")+"\n\n\r");
		                }
		            }
		            if (anioNormas == 1989)
		            {
		                if ((suelo.getDatosEconomicos().getCorrectorCargasSingulares().floatValue() < (float)0.7)||(suelo.getDatosEconomicos().getCorrectorCargasSingulares().floatValue() > (float)1)){
		                	if(sbVal == null){
	    		        		sbVal = new StringBuffer();
	    		        	}
	    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
	    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G43")+"\n\n\r");
		                }
		            }
		            if (anioNormas > 1989)
		            {
		                if ((suelo.getDatosEconomicos().getCorrectorCargasSingulares().floatValue() != (float)0.7)&&(suelo.getDatosEconomicos().getCorrectorCargasSingulares().floatValue() != (float)0.8)&&(suelo.getDatosEconomicos().getCorrectorCargasSingulares().floatValue() != (float)0.9)&&(suelo.getDatosEconomicos().getCorrectorCargasSingulares().floatValue() != (float)1)){
		                	if(sbVal == null){
	    		        		sbVal = new StringBuffer();
	    		        	}
	    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
	    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G44")+"\n\n\r");
		                }
		            }
		
		            if ((anioNormas == 1982)&&(suelo.getDatosEconomicos().getCorrectorAprecDeprec().floatValue()!= (float)1)){
		            	if(sbVal == null){
    		        		sbVal = new StringBuffer();
    		        	}
    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G45")+"\n\n\r");
		            }
		
		            if ((anioNormas == 1986)&&(suelo.getDatosEconomicos().getCorrectorAprecDeprec().floatValue() != (float)1.3)&&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 1.2)
		                        &&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 1.1)&&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 1)
		                        &&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 0.9)&&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 0.8)
		                        &&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 0.7)){
		            	if(sbVal == null){
    		        		sbVal = new StringBuffer();
    		        	}
    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G46")+"\n\n\r");
		            }
		
		            if ((anioNormas == 1989)&&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 1.8)&&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 1.7)
		                        &&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 1.6)&&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 1.5)
		                        &&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 1.4)&&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 1.3)&&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 1.2)
		                        &&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 1.1)&&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 1)
		                        &&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 0.9)&&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 0.8)
		                        &&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 0.7)&&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 0.6)
		                        &&(suelo.getDatosEconomicos().getCoefAprecDeprec().intValue() != 0.5)){
		            	if(sbVal == null){
    		        		sbVal = new StringBuffer();
    		        	}
    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G46")+"\n\n\r");
		            }
		
		            if ((anioNormas > 1989)&&((suelo.getDatosEconomicos().getCorrectorAprecDeprec() == null) ||(suelo.getDatosEconomicos().getCorrectorAprecDeprec().floatValue() < (float)0.5) || (suelo.getDatosEconomicos().getCorrectorAprecDeprec().floatValue() > (float)1.8))){
		            	if(sbVal == null){
    		        		sbVal = new StringBuffer();
    		        	}
    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G46")+"\n\n\r");
		            }
		
		            if ((anioNormas > 1986)&&(metodoCalculoValor != null)&&(metodoCalculoValor.length() > 0) && suelo.getDatosEconomicos().isCorrectorDeprecFuncional().booleanValue()){
		            	if(sbVal == null){
    		        		sbVal = new StringBuffer();
    		        	}
    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.M8")+"\n\n\r");
		            }
		
		            if ((anioNormas > 1986)&&(metodoCalculoValor != null)&&(metodoCalculoValor.length() > 0)&& suelo.getDatosEconomicos().isCorrectorSitEspeciales().booleanValue()){
		            	if(sbVal == null){
    		        		sbVal = new StringBuffer();
    		        	}
    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.M9")+"\n\n\r");
		            }

		            //INCOMPATIBILIDADES
		            if ((anioNormas > 1986) && (tipoRelacionCargos != null) && (tipoRelacionCargos.length() > 0))
		            {
		                //incompatibilidades
		                if (suelo.getDatosEconomicos().isCorrectorDeprecFuncional().booleanValue() || suelo.getDatosEconomicos().isCorrectorSitEspeciales().booleanValue() || suelo.getDatosEconomicos().isCorrectorNoLucrativo().booleanValue()){
		                	if(sbVal == null){
	    		        		sbVal = new StringBuffer();
	    		        	}
	    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
	    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G39")+"\n\n\r");
		                }
		
		                if ((suelo.getDatosEconomicos().getCorrectorCargasSingulares().floatValue() != (float)1)||(suelo.getDatosEconomicos().getCorrectorAprecDeprec().floatValue() != (float)1)){
		                	if(sbVal == null){
	    		        		sbVal = new StringBuffer();
	    		        	}
	    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
	    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G39")+"\n\n\r");
		                }
		            }
		            if (suelo.getDatosEconomicos().isCorrectorInedificabilidad().booleanValue() && suelo.getDatosEconomicos().isCorrectorSitEspeciales().booleanValue()){
		            	if(sbVal == null){
    		        		sbVal = new StringBuffer();
    		        	}
    		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.suelo") +"- "+
    							suelo.getIdSuelo()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G40")+"\n\n\r");
		            }
		        }
	        
	        }//FIN DE LAS PONENCIAS
        
		} catch (Exception e) {
			throw new DataExceptionCatastro(e);
		}
        //FACHADAS
		return sbVal;
	}
	
	

	/**
	 * Método que realiza la validación del suelo. Tanto particular como generalmente.
	 * @param conn Conexión a la BBDD
	 * @param finca Finca que contiene los suelos a validar.
	 * @return
	 * @throws DataExceptionCatastro 
	 */
	public StringBuffer validacionSuelo(Connection conn, FincaCatastro finca, String idMunicipio) throws DataExceptionCatastro
	{
		ArrayList lstSuelos = finca.getLstSuelos();
		StringBuffer sbVal = null;
		if ((lstSuelos != null) && (lstSuelos.size() > 0))
		{
			sbVal = validacionGeneralSuelos(lstSuelos);
//			if (sbVal == null)
//			{
				//Validamos cada uno de los suelos
				for (int i=0;i<lstSuelos.size();i++)
				{
					SueloCatastro suelo = (SueloCatastro) lstSuelos.get(i);
					try {
						StringBuffer sbValParcialSuelo = validacionParcialSuelo(conn, suelo, finca, idMunicipio);
						if(sbValParcialSuelo !=null){
							if(sbVal ==null){
								sbVal = new StringBuffer();
							}

							sbVal.append(sbValParcialSuelo.toString());
						}
					} catch (DataExceptionCatastro e) {
						throw new DataExceptionCatastro(e);
					}					
				}
			//}
		}
		else
		{
			
			if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
		//si el expediente es de situaciones finales
				if (esCorrectaPonencia && esCorrectaFormulaCalculo)
				{
					if (numMinSubparcelas.intValue() > 0){
						sbVal = new StringBuffer();
						sbVal.append(I18N.get("ValidacionMensajesError","Error.G54")+"\n\n\r");
					}
				}
			}
		}
		        	
		return sbVal;
	}

}
