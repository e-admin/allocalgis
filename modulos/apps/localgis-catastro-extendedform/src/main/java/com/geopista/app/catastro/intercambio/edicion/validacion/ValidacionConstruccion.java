/**
 * ValidacionConstruccion.java
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.exception.DataExceptionCatastro;

import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.BienInmuebleJuridico;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;
import com.vividsolutions.jump.I18N;

public class ValidacionConstruccion
{

    private static final String rustica = "RU";
    private String idMunicipio;
    private int annoRevisionTotal = -1;
    private boolean esCorrectaPonencia;
    private UnidadConstructivaCatastro uc = null;
    private boolean hayCodigoTipoValorT = false;
    private float superficieSobreRasante;
    private float superficieConstruidaFinca;
    private float superficieTodasConstruciones=0;
    private int annoAprobacionPonencia;

    public ValidacionConstruccion(String idMunicipio, boolean esCorrectaPonencia, float superficieSobreRasante,
                                  float superficieConstruidaFinca, Integer annoAprobacionPonencia)
    {
        this.idMunicipio= idMunicipio;
        this.esCorrectaPonencia = esCorrectaPonencia;
        this.superficieSobreRasante = superficieSobreRasante;
        this.superficieConstruidaFinca = superficieConstruidaFinca;
        if(annoAprobacionPonencia != null)
            this.annoAprobacionPonencia = annoAprobacionPonencia.intValue();
    }

    public StringBuffer validaListaCons(Connection conn, ArrayList listaConst, ArrayList listaUCFinca, DatosValoracion datosVal,
                                  ArrayList listaBI)throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
        if(listaConst!=null&& listaConst.size()>0)
        {
            if(!esBlancoRelacionCargosYLocales(conn,datosVal))
            {
               // String resultado=null;
                for(int i=0; i<listaConst.size() ;i++)
                {
                    ConstruccionCatastro cons = (ConstruccionCatastro)listaConst.get(i);
                    StringBuffer sbValcons = validaCons(conn, cons,listaUCFinca,datosVal, listaBI, listaConst);
                    if(sbValcons != null){
                    	if(sbVal == null){
                    		sbVal = new StringBuffer();
                    	}
                    	sbVal.append(sbValcons.toString());
                    }
                    if(datosVal.isEsCorrectaFormaCalculo())
                    {
                    	if (datosVal.getMetodoCalculoValorSuelo() != null){
	                        if(datosVal.getMetodoCalculoValorSuelo().equalsIgnoreCase("RT"))
	                        {
	                            if(cons.getDatosEconomicos().getCodTipoValor()!=null&&cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("T"))
	                            {
	                                hayCodigoTipoValorT = true;
	                            }
	                        }
                    	}
                        superficieTodasConstruciones = superficieTodasConstruciones +
                                cons.getDatosFisicos().getSupTotal().floatValue();                        
                    }
                }
                if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
                	//solo se realizan estas validaciones cuando es un expediente de situaciones finales
	                if(datosVal.isEsCorrectaFormaCalculo())
	                {
	                    if(datosVal.getMetodoCalculoValorSuelo()!=null&& datosVal.getMetodoCalculoValorSuelo().equalsIgnoreCase("RT"))
	                    {
	                        if(superficieSobreRasante<=0)
	                        {
	                        	if(sbVal == null){
	                        		sbVal = new StringBuffer();
	                        	}
	                        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G106")+"\n\n\r");
	                        }
	                        if(!hayCodigoTipoValorT)
	                        {
	                        	if(sbVal == null){
	                        		sbVal = new StringBuffer();
	                        	}
	                        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G107")+"\n\n\r");
	                        }
	                    }
	                    if((annoRevisionTotal>=2006|| (annoRevisionTotal<2006&&
	                            datosVal.getIndicadorValidacionSuperficieConstruida().equalsIgnoreCase("S")))
	                            &&superficieConstruidaFinca!=superficieTodasConstruciones)
	                    {
	                    	if(sbVal == null){
	                    		sbVal = new StringBuffer();
	                    	}
	                    	sbVal.append(I18N.get("ValidacionMensajesError","Error.G108")+"\n\n\r");                       
	                    }
	                }
                }
            }
            else
            {
            	if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("ValidacionMensajesError","Error.G77")+"\n\n\r");   
            }
        }
        return sbVal;
    }

    private boolean esBlancoRelacionCargosYLocales(Connection conn, DatosValoracion datosVal)
            throws DataExceptionCatastro
    {
        boolean resultado = false;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {
            s = conn.prepareStatement("MCobtenerAnnoEfectosTotalPonencia");
            s.setInt(1, annoAprobacionPonencia);
            s.setString(2, idMunicipio);
            r = s.executeQuery();
            int anno = -1;
            if(r.next())
            {
                anno = r.getInt("anno_efectostotal");
            }
            if(anno<0 || (anno<2006 && (datosVal.getTipoRelacionEntreCargosLocales()==null||
                    datosVal.getTipoRelacionEntreCargosLocales().equalsIgnoreCase(""))))
            {
                resultado = true;
            }
            else
            {
                this.annoRevisionTotal = anno;
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
        return resultado;
    }

    public StringBuffer validaCons(Connection conn, ConstruccionCatastro cons, ArrayList listaUCFinca, DatosValoracion datosVal,
                            ArrayList listaBI, ArrayList lstCons)throws DataExceptionCatastro
    {
       // String resultado = null;
    	StringBuffer sbVal = null;
        try
        {
            if(cons.getDatosFisicos().getCodUnidadConstructiva()==null)
            {
            	if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("ValidacionMensajesError","Error.G80")+"\n\n\r");
            }
            StringBuffer sbValIdentificacion = validaIdentificacion(conn, cons, lstCons);
            StringBuffer sbValDatosFisicos = validaDatosFisicos(conn, cons, listaUCFinca,datosVal, listaBI);
            StringBuffer sbValDatosEconomicos = validaDatosEconomicos(conn,cons, datosVal);
            
            if(sbValIdentificacion != null){
            	if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(sbValIdentificacion.toString());
            }
			if(sbValDatosFisicos != null){
				if(sbVal == null){
            		sbVal = new StringBuffer();
            	}     	
				sbVal.append(sbValDatosFisicos.toString());
			}
			if(sbValDatosEconomicos != null){
				if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
				sbVal.append(sbValDatosEconomicos.toString());
			}
        }
        catch(Exception e)
        {
            throw new DataExceptionCatastro(e);
        }

        return sbVal;
    }

    private StringBuffer validaIdentificacion(Connection conn, ConstruccionCatastro cons, ArrayList lstConstrucciones) throws DataExceptionCatastro
    {//Se añade un segundo parámetro de búsqueda a la consulta: la parcela catastral (ogalende)
    	StringBuffer sbVal = null;
        if(cons.getNumOrdenConstruccion()!=null && !cons.getNumOrdenConstruccion().equalsIgnoreCase("") &&
                !cons.getNumOrdenConstruccion().equalsIgnoreCase("0")
                && cons.getRefParcela().getRefCatastral()!=null 
                && !cons.getRefParcela().getRefCatastral().equalsIgnoreCase(""))
        {
        	
        	for (java.util.Iterator iterLstConstruccciones = (java.util.Iterator) lstConstrucciones.iterator(); iterLstConstruccciones.hasNext();){

        		Object object = iterLstConstruccciones.next();
        		if (object instanceof ConstruccionCatastro){

        			if ( object != cons && ((ConstruccionCatastro)object).getIdConstruccion().equals(cons.getIdConstruccion())){
        				if(sbVal == null){
        	        		sbVal = new StringBuffer();
        	        	}
        	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.construccion") +"- "+
                    			cons.getIdConstruccion()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G78")+"\n\n\r");
        			}
        		}
        	}
        	
        	
//            PreparedStatement s = null;
//            ResultSet r = null;
//            try
//            {
//                s = conn.prepareStatement("MCobtenerDuplicadosConstruccion");
//                s.setString(1,cons.getNumOrdenConstruccion());
//                s.setString(2, cons.getRefParcela().getRefCatastral());
//                r = s.executeQuery();
//                if(r.next())
//                {
//                    resultado = "Error.G78";
////                	return null;
//                }
//            }
//            catch (SQLException ex)
//            {
//                throw new DataException(ex);
//            }
//            finally
//            {
//                if (s!=null) try{s.close();}catch(Exception e){};
//                if (r!= null) try{r.close();}catch(Exception e){};
//            }
        }
        else
        {
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.construccion") +"- "+
        			cons.getIdConstruccion()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G78")+"\n\n\r");
        }

        return sbVal;
    }

    private StringBuffer validaDatosFisicos(Connection conn, ConstruccionCatastro cons, ArrayList listaUCFinca,
                                      DatosValoracion datosVal, ArrayList listaBI) throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
        if(cons.getDatosFisicos().getSupTotal()==null || cons.getDatosFisicos().getSupTotal().floatValue()<=0)
        {
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.construccion") +"- "+
        			cons.getIdConstruccion()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G79")+"\n\n\r");
        }
        else
        {
            uc = validaExistenciaCodUC(cons.getDatosFisicos().getCodUnidadConstructiva(), listaUCFinca);
            if(uc == null)
            {
            	if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.construccion") +"- "+
            			cons.getIdConstruccion()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G80")+"\n\n\r");
            }
            else
            {
            	if (cons.getDatosFisicos().getTipoReforma() != null)  
            		if (cons.getDatosFisicos().getTipoReforma().equals(""))
            			if(cons.getDatosFisicos().getAnioReforma()==null || uc.getDatosFisicos().getAnioConstruccion()==null
            					||(cons.getDatosFisicos().getAnioReforma().intValue()>uc.getDatosFisicos().getAnioConstruccion().intValue()))
            			{
            				if(sbVal == null){
            	        		sbVal = new StringBuffer();
            	        	}
            	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.construccion") +"- "+
                        			cons.getIdConstruccion()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G83")+"\n\n\r");
            			}
            			else
            			{
            				Calendar aux= new GregorianCalendar();
            				aux.setTime(new Date(System.currentTimeMillis()));
            				int anno = aux.get(Calendar.YEAR);
            				if(cons.getDatosFisicos().getAnioReforma()==null || cons.getDatosFisicos().getAnioReforma().intValue()>anno)
            				{
            					if(sbVal == null){
            		        		sbVal = new StringBuffer();
            		        	}
            		        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.construccion") +"- "+
                            			cons.getIdConstruccion()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G84")+"\n\n\r");                       
            				}
            				else
            				{
            					StringBuffer sbValIndAnnoReforma = validaIndicadorYAnnoReforma(conn, cons);
            					StringBuffer sbValCargoReparto = null;
            					if(datosVal.getTipoRelacionEntreCargosLocales()!=null &&
            								datosVal.getTipoRelacionEntreCargosLocales().equalsIgnoreCase("11")){
            						sbValCargoReparto = validaCargoYReparto(conn, cons, listaBI, datosVal);
            					}

            					if(sbValIndAnnoReforma != null){
            						if(sbVal == null){
            			        		sbVal = new StringBuffer();
            			        	}
            						sbVal.append(sbValIndAnnoReforma.toString());
            					}
            					if(sbValCargoReparto != null){
            						if(sbVal == null){
            			        		sbVal = new StringBuffer();
            			        	}
            						sbVal.append(sbValCargoReparto.toString());
            					}
            				}
            			}
            }
        }
        return sbVal;
    }


    private UnidadConstructivaCatastro validaExistenciaCodUC(String codUC,ArrayList listaUCFinca)
    {
    	if (listaUCFinca != null){
    		for(int i= 0; i<listaUCFinca.size();i++)
    		{
    			UnidadConstructivaCatastro uc = (UnidadConstructivaCatastro)listaUCFinca.get(i);
    			if(codUC.equalsIgnoreCase(uc.getCodUnidadConstructiva()))
    			{
    				return uc;
    			}
    		}
    	}
        return null;
    }

    private StringBuffer validaIndicadorYAnnoReforma(Connection conn,ConstruccionCatastro cons) throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {
            s = conn.prepareStatement("MCobtenerAplicacionFormulaPonencia");
            s.setInt(1, annoAprobacionPonencia);
            s.setString(2, idMunicipio);
            r = s.executeQuery();
            String aplicacionFormula = null;
            if(r.next())
            {
                aplicacionFormula = r.getString("aplicacion_formula");
                if(aplicacionFormula!=null && aplicacionFormula.equalsIgnoreCase("S"))
                {
                    if((cons.getDatosFisicos().getAnioReforma()==null && cons.getDatosFisicos().getTipoReforma()!=null)
                            ||(cons.getDatosFisicos().getAnioReforma()!=null && cons.getDatosFisicos().getTipoReforma()==null))
                    {
                    	if(sbVal == null){
                    		sbVal = new StringBuffer();
                    	}
                    	sbVal.append(I18N.get("ValidacionMensajesError","Error.G85")+"\n\n\r");
                    }                   
                    //Se ha corregido esta condición (ogalende)
                    else if(cons.getDatosFisicos().getTipoReforma()!=null &&
                    		!(cons.getDatosFisicos().getTipoReforma().equalsIgnoreCase("") &&
                            cons.getDatosFisicos().getTipoReforma().equalsIgnoreCase("R") &&
                            cons.getDatosFisicos().getTipoReforma().equalsIgnoreCase("O") &&
                            cons.getDatosFisicos().getTipoReforma().equalsIgnoreCase("E") &&
                            cons.getDatosFisicos().getTipoReforma().equalsIgnoreCase("I")))
                    {
                    	if(sbVal == null){
                    		sbVal = new StringBuffer();
                    	}
                    	sbVal.append(I18N.get("ValidacionMensajesError","Error.G82")+"\n\n\r");  
                    }
                }
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
        return sbVal;
    }

    private StringBuffer validaCargoYReparto(Connection conn,ConstruccionCatastro cons,ArrayList listaBI,
                                       DatosValoracion datosVal)throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
    	
        if(cons.getNumOrdenBienInmueble()!=null || cons.getDatosEconomicos().getCodModalidadReparto()!=null)
        {
            if(cons.getNumOrdenBienInmueble()!=null)
            {
                boolean existe = false;
                if (listaBI != null){
	                for(int i = 0; i<listaBI.size() && !existe;i++)
	                {
	                	if (listaBI.get(i) instanceof BienInmuebleJuridico){
	
	                		BienInmuebleJuridico bi = (BienInmuebleJuridico) listaBI.get(i);
	                		if(bi.getBienInmueble().getIdBienInmueble().getNumCargo()!=null && bi.getBienInmueble().getIdBienInmueble().getNumCargo().
	                				equalsIgnoreCase(cons.getNumOrdenBienInmueble()))
	                		{
	                			existe = true;
	                		}
	                	}
	                	else{
	                		BienInmuebleCatastro bi = (BienInmuebleCatastro) listaBI.get(i);
	                		if(bi.getIdBienInmueble().getNumCargo()!=null && bi.getIdBienInmueble().getNumCargo().
	                				equalsIgnoreCase(cons.getNumOrdenBienInmueble()))
	                		{
	                			existe = true;
	                		}
	                	}
	                }
                }
                if(!existe)
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("ValidacionMensajesError","Error.G91")+"\n\n\r");
                }
            }
            if(cons.getDatosEconomicos().getCodModalidadReparto()!=null)
            {
                if(datosVal.isEsCorrectaFormaCalculo() && datosVal.getIndicadorElementosComunes()!=null
                        && datosVal.getIndicadorElementosComunes().equalsIgnoreCase("S"))
                {
                    if(!(cons.getDatosEconomicos().getCodModalidadReparto().equalsIgnoreCase("AC1") ||
                       cons.getDatosEconomicos().getCodModalidadReparto().equalsIgnoreCase("AC2") ||
                       cons.getDatosEconomicos().getCodModalidadReparto().equalsIgnoreCase("AC3") ||
                       cons.getDatosEconomicos().getCodModalidadReparto().equalsIgnoreCase("AC4") ||
                       cons.getDatosEconomicos().getCodModalidadReparto().equalsIgnoreCase("AL1") ||
                       cons.getDatosEconomicos().getCodModalidadReparto().equalsIgnoreCase("AL2") ||
                       cons.getDatosEconomicos().getCodModalidadReparto().equalsIgnoreCase("AL3") ||
                       cons.getDatosEconomicos().getCodModalidadReparto().equalsIgnoreCase("AL4") ||
                       cons.getDatosEconomicos().getCodModalidadReparto().equalsIgnoreCase("TC1") ||
                       cons.getDatosEconomicos().getCodModalidadReparto().equalsIgnoreCase("TC2") ||
                       cons.getDatosEconomicos().getCodModalidadReparto().equalsIgnoreCase("TC3") ||
                       cons.getDatosEconomicos().getCodModalidadReparto().equalsIgnoreCase("TL1") ||
                       cons.getDatosEconomicos().getCodModalidadReparto().equalsIgnoreCase("TL2") ||
                       cons.getDatosEconomicos().getCodModalidadReparto().equalsIgnoreCase("TL3")))
                    {
                    	if(sbVal == null){
                    		sbVal = new StringBuffer();
                    	}
                    	sbVal.append(I18N.get("ValidacionMensajesError","Error.G93")+"\n\n\r");
                    }
                    /*Si es nuevo no tiene por qué existir
                    else if(cons.getDatosEconomicos().getCodModalidadReparto().startsWith("AC") ||
                            cons.getDatosEconomicos().getCodModalidadReparto().startsWith("AL"))
                    {
                        return compruebaExisteReparto(conn, cons);
                    }*/
                }
                else
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("ValidacionMensajesError","Error.G92")+"\n\n\r");
                }
            }
        }
        else
        {
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G90")+"\n\n\r");
        }
        return null;
    }

    private String compruebaExisteReparto(Connection conn, ConstruccionCatastro cons) throws DataExceptionCatastro
    {
        String resultado = null;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {
            s = conn.prepareStatement("MCcompruebaRepartosConstrucciones");
            s.setString(1, idMunicipio);
            r = s.executeQuery();
            String aplicacionFormula = null;
            if(!r.next())
            {
                resultado = "Error.G94";
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
        return resultado;
    }

    private StringBuffer validaDatosEconomicos(Connection conn,ConstruccionCatastro cons, DatosValoracion datosVal)throws DataExceptionCatastro
    {//Se añade también el caracter C a la validación. (ogalende)
    	StringBuffer sbVal = null;
    	StringBuffer sbValPonencia = null;
        if(cons.getDatosEconomicos().getCodCategoriaPredominante()!=null &&
                !(cons.getDatosEconomicos().getCodCategoriaPredominante().equalsIgnoreCase("1") ||
                cons.getDatosEconomicos().getCodCategoriaPredominante().equalsIgnoreCase("2")||
                cons.getDatosEconomicos().getCodCategoriaPredominante().equalsIgnoreCase("3")||
                cons.getDatosEconomicos().getCodCategoriaPredominante().equalsIgnoreCase("4")||
                cons.getDatosEconomicos().getCodCategoriaPredominante().equalsIgnoreCase("5")||
                cons.getDatosEconomicos().getCodCategoriaPredominante().equalsIgnoreCase("6")||
                cons.getDatosEconomicos().getCodCategoriaPredominante().equalsIgnoreCase("7")||
                cons.getDatosEconomicos().getCodCategoriaPredominante().equalsIgnoreCase("8")||
                cons.getDatosEconomicos().getCodCategoriaPredominante().equalsIgnoreCase("9")||
                cons.getDatosEconomicos().getCodCategoriaPredominante().equalsIgnoreCase("A")||
                cons.getDatosEconomicos().getCodCategoriaPredominante().equalsIgnoreCase("B")||
                cons.getDatosEconomicos().getCodCategoriaPredominante().equalsIgnoreCase("C")))
        {
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.construccion") +"- "+
        			cons.getIdConstruccion()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G97")+"\n\n\r"); 
        }
        if(esCorrectaPonencia)
        {
        	sbValPonencia = validaPonencias(conn, cons, datosVal);
        }
        
        if (sbValPonencia != null){
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(sbValPonencia.toString());
        }
        return sbVal;
    }

    private StringBuffer validaPonencias(Connection conn, ConstruccionCatastro cons, DatosValoracion datosVal)throws DataExceptionCatastro
    {
        
        //Todo falta tipologia y tipologia rustica, cuando nos den las tablas.
    	StringBuffer sbVal = validaCorrectores(conn, cons, datosVal);
        return sbVal;
    }

    private StringBuffer validaCorrectores(Connection conn, ConstruccionCatastro cons, DatosValoracion datosVal)throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
       // String resultado = null;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {
            s = conn.prepareStatement("MCobtenerAnnoDeNormasPonencia");
            s.setInt(1, annoAprobacionPonencia);
            s.setString(2, idMunicipio);
            r = s.executeQuery();
            int annoNormas = -1;
            if(r.next())
            {
                annoNormas = r.getInt("anno_normas");
            }
            if(annoNormas!=-1 && annoNormas>1000)
            {
                if(annoNormas==1982 && (cons.getDatosEconomicos().getCorrectorApreciacion()==null||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()!=(float)1))
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.construccion") +"- "+
                			cons.getIdConstruccion()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G45")+"\n\n\r"); 
                }
                else if(annoNormas==1982 && (cons.getDatosEconomicos().getCorrectorApreciacion()==null||
                    !(cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)1.3 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)1.2 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)1.1 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)1 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)0.9 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)0.8 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)0.7)))
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.construccion") +"- "+
                			cons.getIdConstruccion()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G46")+"\n\n\r");
                }
                else if(annoNormas==1989 && (cons.getDatosEconomicos().getCorrectorApreciacion()==null||
                    !(cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)1.8 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)1.7 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)1.6 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)1.5 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)1.4 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)1.3 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)1.2 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)1.1 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)1 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)0.9 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)0.8 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)0.7 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)0.6 ||
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()==(float)0.5)))
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.construccion") +"- "+
                			cons.getIdConstruccion()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G46")+"\n\n\r");

                }
                else if(annoNormas>1989 && (cons.getDatosEconomicos().getCorrectorApreciacion()==null||
                    !(cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()>=(float)0.5 &&
                    cons.getDatosEconomicos().getCorrectorApreciacion().floatValue()<=(float)1.8)))
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.construccion") +"- "+
                			cons.getIdConstruccion()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G46")+"\n\n\r");
                }
//                if(resultado ==null)
//                {
                    if(uc!=null &&((uc.getDatosEconomicos().getCoefCargasSingulares()!=null &&
                            uc.getDatosEconomicos().getCoefCargasSingulares().floatValue()!=(float)1 &&
                            cons.getDatosEconomicos().isCorrectorVivienda()!=null &&
                            cons.getDatosEconomicos().isCorrectorVivienda().booleanValue())||
                            (uc.getDatosEconomicos().isCorrectorSitEspeciales()!=null &&
                            uc.getDatosEconomicos().isCorrectorSitEspeciales().booleanValue()&&
                            cons.getDatosEconomicos().isCorrectorVivienda()!=null &&
                            cons.getDatosEconomicos().isCorrectorVivienda().booleanValue())))
                    {
                    	if(sbVal == null){
                    		sbVal = new StringBuffer();
                    	}
                    	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.construccion") +"- "+
                    			cons.getIdConstruccion()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G99")+"\n\n\r");
                    }
                    else if(uc.getTipoUnidad().equalsIgnoreCase(rustica) && cons.getDatosEconomicos().getCodTipoValor()!=null
                            && !cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase(""))
                    {
                    	if(sbVal == null){
                    		sbVal = new StringBuffer();
                    	}
                    	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.construccion") +"- "+
                    			cons.getIdConstruccion()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G100")+"\n\n\r");
                    }
                    if(datosVal.isEsCorrectaFormaCalculo()&&!uc.getTipoUnidad().equalsIgnoreCase(rustica))
                    {	if (datosVal.getMetodoCalculoValorSuelo() !=null){
	                        if(datosVal.getMetodoCalculoValorSuelo().equalsIgnoreCase("RT")&&
	                        		(cons.getDatosEconomicos().getCodTipoValor()!=null&&
	                                !(cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("T") ||
	                                        cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase(""))))
	                        {
	                        	if(sbVal == null){
	                        		sbVal = new StringBuffer();
	                        	}
	                        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.construccion") +"- "+
	                        			cons.getIdConstruccion()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G101")+"\n\n\r");
	                        }
	                        else if(!datosVal.getMetodoCalculoValorSuelo().equalsIgnoreCase("RT")&&annoRevisionTotal<2006)
	                        {
	                        	if (!cons.getDatosEconomicos().getCodTipoValor().equals("E")&&!cons.getDatosEconomicos().getCodTipoValor().equals("N")
	                        			&&!cons.getDatosEconomicos().getCodTipoValor().equals("T")&&!cons.getDatosEconomicos().getCodTipoValor().equals("V")){
		                            if(annoNormas<1989&& (cons.getDatosEconomicos().getCodTipoValor()==null||cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("2")||
		                                    !(Float.parseFloat(cons.getDatosEconomicos().getCodTipoValor())>=(float)0
		                                    &&Float.parseFloat(cons.getDatosEconomicos().getCodTipoValor())<=(float)7)))
		                            {
		                            	if(sbVal == null){
		                            		sbVal = new StringBuffer();
		                            	}
		                            	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.construccion") +"- "+
		                            			cons.getIdConstruccion()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G102")+"\n\n\r");;
		                            }
		                            else if(annoNormas>=1989&& (cons.getDatosEconomicos().getCodTipoValor()==null||cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("2")||
		                                    cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("3")||
		                                    !(Float.parseFloat(cons.getDatosEconomicos().getCodTipoValor())>=(float)0
		                                    &&Float.parseFloat(cons.getDatosEconomicos().getCodTipoValor())<=(float)9)))
		                            {
		                            	if(sbVal == null){
		                            		sbVal = new StringBuffer();
		                            	}
		                            	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.construccion") +"- "+
		                            			cons.getIdConstruccion()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G103")+"\n\n\r");
		                            }
	                        	}
	                        }
	                        else if(!datosVal.getMetodoCalculoValorSuelo().equalsIgnoreCase("RT")&&annoRevisionTotal>=2006)
	                        {//Se añaden los caracteres T y N 
	                            if(cons.getDatosEconomicos().getCodTipoValor()!=null||!(cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("E") ||
	                            cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("V") ||
	                            cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("T") ||
	                            cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("N") ||
	                            cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("0") ||
	                            cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("1") ||
	                            cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("4") ||
	                            cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("5") ||
	                            cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("6") ||
	                            cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("7") ||
	                            cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("8") ||
	                            cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("9")))
	                            {
	                            	if(sbVal == null){
	                            		sbVal = new StringBuffer();
	                            	}
	                            	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.construccion") +"- "+
	                            			cons.getIdConstruccion()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G104")+"\n\n\r");
	                            }
	                        }
                    	}
                    }
                //}
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
        return sbVal;
    }

}
