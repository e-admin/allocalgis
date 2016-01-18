package com.geopista.app.catastro.intercambio.edicion.validacion;

import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.intercambio.exception.DataException;
import com.geopista.app.catastro.intercambio.importacion.xml.contents.BienInmuebleJuridico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;

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

    public String validaListaCons(Connection conn, ArrayList listaConst, ArrayList listaUCFinca, DatosValoracion datosVal,
                                  ArrayList listaBI)throws DataException
    {
        if(listaConst!=null&& listaConst.size()>0)
        {
            if(!esBlancoRelacionCargosYLocales(conn,datosVal))
            {
                String resultado=null;
                for(int i=0; i<listaConst.size() && resultado==null;i++)
                {
                    ConstruccionCatastro cons = (ConstruccionCatastro)listaConst.get(i);
                    resultado = validaCons(conn, cons,listaUCFinca,datosVal, listaBI, listaConst);
                    if(resultado ==null && datosVal.isEsCorrectaFormaCalculo())
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
                if(resultado ==null &&datosVal.isEsCorrectaFormaCalculo())
                {
                    if(datosVal.getMetodoCalculoValorSuelo()!=null&& datosVal.getMetodoCalculoValorSuelo().equalsIgnoreCase("RT"))
                    {
                        if(superficieSobreRasante<=0)
                        {
                            return "Error.G106";
                        }
                        if(!hayCodigoTipoValorT)
                        {
                            return "Error.G107";
                        }
                    }
                    if((annoRevisionTotal>=2006|| (annoRevisionTotal<2006&&
                            datosVal.getIndicadorValidacionSuperficieConstruida().equalsIgnoreCase("S")))
                            &&superficieConstruidaFinca!=superficieTodasConstruciones)
                    {
                        return "Error.G108";                        
                    }
                }
                else if(resultado!=null)
                {
                    return resultado;
                }
            }
            else
            {
                return "Error.G77";    
            }
        }
        return null;
    }

    private boolean esBlancoRelacionCargosYLocales(Connection conn, DatosValoracion datosVal)
            throws DataException
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
            throw new DataException(ex);
        }
        finally
        {
            if (s!=null) try{s.close();}catch(Exception e){};
            if (r!= null) try{r.close();}catch(Exception e){};
        }
        return resultado;
    }

    public String validaCons(Connection conn, ConstruccionCatastro cons, ArrayList listaUCFinca, DatosValoracion datosVal,
                            ArrayList listaBI, ArrayList lstCons)throws DataException
    {
        String resultado = null;
        try
        {
            if(cons.getDatosFisicos().getCodUnidadConstructiva()==null)
            {
                return "Error.G80";
            }
            resultado = validaIdentificacion(conn, cons, lstCons);
            if(resultado == null)
            {
                resultado = validaDatosFisicos(conn, cons, listaUCFinca,datosVal, listaBI);
                if(resultado == null)
                {
                    resultado = validaDatosEconomicos(conn,cons, datosVal);
                }
            }
        }
        catch(Exception e)
        {
            throw new DataException(e);
        }

        return resultado;
    }

    private String validaIdentificacion(Connection conn, ConstruccionCatastro cons, ArrayList lstConstrucciones) throws DataException
    {//Se añade un segundo parámetro de búsqueda a la consulta: la parcela catastral (ogalende)
        String resultado = null;
        if(cons.getNumOrdenConstruccion()!=null && !cons.getNumOrdenConstruccion().equalsIgnoreCase("") &&
                !cons.getNumOrdenConstruccion().equalsIgnoreCase("0")
                && cons.getRefParcela().getRefCatastral()!=null 
                && !cons.getRefParcela().getRefCatastral().equalsIgnoreCase(""))
        {
        	
        	for (java.util.Iterator iterLstConstruccciones = (java.util.Iterator) lstConstrucciones.iterator(); iterLstConstruccciones.hasNext();){

        		Object object = iterLstConstruccciones.next();
        		if (object instanceof ConstruccionCatastro){

        			if ( object != cons && ((ConstruccionCatastro)object).getIdConstruccion().equals(cons.getIdConstruccion())){
        				resultado = "Error.G78";
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
            return "Error.G78";
        }

        return resultado;
    }

    private String validaDatosFisicos(Connection conn, ConstruccionCatastro cons, ArrayList listaUCFinca,
                                      DatosValoracion datosVal, ArrayList listaBI) throws DataException
    {
        if(cons.getDatosFisicos().getSupTotal()==null || cons.getDatosFisicos().getSupTotal().floatValue()<=0)
        {
            return "Error.G79";
        }
        else
        {
            uc = validaExistenciaCodUC(cons.getDatosFisicos().getCodUnidadConstructiva(), listaUCFinca);
            if(uc == null)
            {
                return "Error.G80";
            }
            else
            {
            	if (cons.getDatosFisicos().getTipoReforma() != null)  
            		if (cons.getDatosFisicos().getTipoReforma().equals(""))
            			if(cons.getDatosFisicos().getAnioReforma()==null || uc.getDatosFisicos().getAnioConstruccion()==null
            					||(cons.getDatosFisicos().getAnioReforma().intValue()>uc.getDatosFisicos().getAnioConstruccion().intValue()))
            			{
            				return "Error.G83";
            			}
            			else
            			{
            				Calendar aux= new GregorianCalendar();
            				aux.setTime(new Date(System.currentTimeMillis()));
            				int anno = aux.get(Calendar.YEAR);
            				if(cons.getDatosFisicos().getAnioReforma()==null || cons.getDatosFisicos().getAnioReforma().intValue()>anno)
            				{
            					return "Error.G84";                        
            				}
            				else
            				{
            					String resultado= validaIndicadorYAnnoReforma(conn, cons);
            					if(resultado==null)
            					{
            						if(datosVal.getTipoRelacionEntreCargosLocales()!=null &&
            								datosVal.getTipoRelacionEntreCargosLocales().equalsIgnoreCase("11"))
            						{
            							resultado = validaCargoYReparto(conn, cons, listaBI, datosVal);
            						}
            					}
            					return resultado;
            				}
            			}
            		else 
            			return null;
            	else 
            		return null;
            }
        }
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

    private String validaIndicadorYAnnoReforma(Connection conn,ConstruccionCatastro cons) throws DataException
    {
        String resultado = null;
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
                        resultado = "Error.G85";
                    }                   
                    //Se ha corregido esta condición (ogalende)
                    else if(cons.getDatosFisicos().getTipoReforma()!=null &&
                    		!(cons.getDatosFisicos().getTipoReforma().equalsIgnoreCase("") &&
                            cons.getDatosFisicos().getTipoReforma().equalsIgnoreCase("R") &&
                            cons.getDatosFisicos().getTipoReforma().equalsIgnoreCase("O") &&
                            cons.getDatosFisicos().getTipoReforma().equalsIgnoreCase("E") &&
                            cons.getDatosFisicos().getTipoReforma().equalsIgnoreCase("I")))
                    {
                        resultado = "Error.G82";       
                    }
                }
            }
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }
        finally
        {
            if (s!=null) try{s.close();}catch(Exception e){};
            if (r!= null) try{r.close();}catch(Exception e){};
        }
        return resultado;
    }

    private String validaCargoYReparto(Connection conn,ConstruccionCatastro cons,ArrayList listaBI,
                                       DatosValoracion datosVal)throws DataException
    {
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
                    return "Error.G91";
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
                        return "Error.G93";
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
                    return "Error.G92";
                }
            }
        }
        else
        {
            return "Error.G90";
        }
        return null;
    }

    private String compruebaExisteReparto(Connection conn, ConstruccionCatastro cons) throws DataException
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
            throw new DataException(ex);
        }
        finally
        {
            if (s!=null) try{s.close();}catch(Exception e){};
            if (r!= null) try{r.close();}catch(Exception e){};
        }
        return resultado;
    }

    private String validaDatosEconomicos(Connection conn,ConstruccionCatastro cons, DatosValoracion datosVal)throws DataException
    {//Se añade también el caracter C a la validación. (ogalende)
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
            return "Error.G97";    
        }
        if(esCorrectaPonencia)
        {
            return validaPonencias(conn, cons, datosVal);
        }
        return null;
    }

    private String validaPonencias(Connection conn, ConstruccionCatastro cons, DatosValoracion datosVal)throws DataException
    {
        String resultado = null;
        //Todo falta tipologia y tipologia rustica, cuando nos den las tablas.
        resultado = validaCorrectores(conn, cons, datosVal);
        return resultado;
    }

    private String validaCorrectores(Connection conn, ConstruccionCatastro cons, DatosValoracion datosVal)throws DataException
    {
        String resultado = null;
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
                if(annoNormas==1982 && (cons.getDatosEconomicos().getCorrectorApreciación()==null||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()!=(float)1))
                {
                    resultado = "Error.G45";    
                }
                else if(annoNormas==1982 && (cons.getDatosEconomicos().getCorrectorApreciación()==null||
                    !(cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)1.3 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)1.2 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)1.1 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)1 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)0.9 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)0.8 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)0.7)))
                {
                    resultado = "Error.G46";
                }
                else if(annoNormas==1989 && (cons.getDatosEconomicos().getCorrectorApreciación()==null||
                    !(cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)1.8 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)1.7 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)1.6 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)1.5 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)1.4 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)1.3 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)1.2 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)1.1 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)1 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)0.9 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)0.8 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)0.7 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)0.6 ||
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()==(float)0.5)))
                {
                    resultado = "Error.G46";
                }
                else if(annoNormas>1989 && (cons.getDatosEconomicos().getCorrectorApreciación()==null||
                    !(cons.getDatosEconomicos().getCorrectorApreciación().floatValue()>=(float)0.5 &&
                    cons.getDatosEconomicos().getCorrectorApreciación().floatValue()<=(float)1.8)))
                {
                    resultado = "Error.G46";
                }
                if(resultado ==null)
                {
                    if(uc!=null &&((uc.getDatosEconomicos().getCoefCargasSingulares()!=null &&
                            uc.getDatosEconomicos().getCoefCargasSingulares().floatValue()!=(float)1 &&
                            cons.getDatosEconomicos().isCorrectorVivienda()!=null &&
                            cons.getDatosEconomicos().isCorrectorVivienda().booleanValue())||
                            (uc.getDatosEconomicos().isCorrectorSitEspeciales()!=null &&
                            uc.getDatosEconomicos().isCorrectorSitEspeciales().booleanValue()&&
                            cons.getDatosEconomicos().isCorrectorVivienda()!=null &&
                            cons.getDatosEconomicos().isCorrectorVivienda().booleanValue())))
                    {
                        resultado = "Error.G99";
                    }
                    else if(uc.getTipoUnidad().equalsIgnoreCase(rustica) && cons.getDatosEconomicos().getCodTipoValor()!=null
                            && !cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase(""))
                    {
                        resultado = "Error.G100";
                    }
                    if(resultado==null && datosVal.isEsCorrectaFormaCalculo()&&!uc.getTipoUnidad().equalsIgnoreCase(rustica))
                    {	if (datosVal.getMetodoCalculoValorSuelo() !=null){
	                        if(datosVal.getMetodoCalculoValorSuelo().equalsIgnoreCase("RT")&&
	                        		(cons.getDatosEconomicos().getCodTipoValor()!=null&&
	                                !(cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("T") ||
	                                        cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase(""))))
	                        {
	                            resultado = "Error.G101";
	                        }
	                        else if(!datosVal.getMetodoCalculoValorSuelo().equalsIgnoreCase("RT")&&annoRevisionTotal<2006)
	                        {
	                        	if (!cons.getDatosEconomicos().getCodTipoValor().equals("E")&&!cons.getDatosEconomicos().getCodTipoValor().equals("N")
	                        			&&!cons.getDatosEconomicos().getCodTipoValor().equals("T")&&!cons.getDatosEconomicos().getCodTipoValor().equals("V")){
		                            if(annoNormas<1989&& (cons.getDatosEconomicos().getCodTipoValor()==null||cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("2")||
		                                    !(Float.parseFloat(cons.getDatosEconomicos().getCodTipoValor())>=(float)0
		                                    &&Float.parseFloat(cons.getDatosEconomicos().getCodTipoValor())<=(float)7)))
		                            {
		                                resultado = "Error.G102";
		                            }
		                            else if(annoNormas>=1989&& (cons.getDatosEconomicos().getCodTipoValor()==null||cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("2")||
		                                    cons.getDatosEconomicos().getCodTipoValor().equalsIgnoreCase("3")||
		                                    !(Float.parseFloat(cons.getDatosEconomicos().getCodTipoValor())>=(float)0
		                                    &&Float.parseFloat(cons.getDatosEconomicos().getCodTipoValor())<=(float)9)))
		                            {
		                                resultado = "Error.G103";
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
	                                resultado = "Error.G104";   
	                            }
	                        }
                    }
                    }
                }
            }
        }
        catch (SQLException ex)
        {
            throw new DataException(ex);
        }
        finally
        {
            if (s!=null) try{s.close();}catch(Exception e){};
            if (r!= null) try{r.close();}catch(Exception e){};
        }
        return resultado;
    }

}
