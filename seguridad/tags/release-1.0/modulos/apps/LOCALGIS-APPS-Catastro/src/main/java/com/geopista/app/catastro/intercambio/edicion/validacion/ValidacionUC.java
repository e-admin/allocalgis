package com.geopista.app.catastro.intercambio.edicion.validacion;

import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;
import com.geopista.app.catastro.model.beans.SueloCatastro;
import com.geopista.app.catastro.intercambio.exception.DataException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.ArrayList;

public class ValidacionUC
{
    private boolean hayRustica = false;
    private boolean haySuperficieHuella = false;
    private static final String rustica = "RU";
    private static final String bice = "BI";
    private static final String urbana = "UR";
    private String idMunicipio;
    private boolean esCorrectaPonencia;
    private int annoAprobacionPonencia;
    private int annoRevisionTotal = -1;
    private ArrayList listaSuelos;

    public ValidacionUC(String idMunicipio, boolean esCorrectaPonencia, ArrayList listaSuelos, Integer annoAprobacionPonencia)
    {
        this.idMunicipio= idMunicipio;
        this.esCorrectaPonencia = esCorrectaPonencia;
        this.listaSuelos = listaSuelos;
        if(annoAprobacionPonencia != null)
            this.annoAprobacionPonencia = annoAprobacionPonencia.intValue();
    }

    public String ValidaListaUC(Connection conn, ArrayList listaUC, DatosValoracion datosVal)
            throws DataException
    {
        String resultado = null;
        if(listaUC!=null &&listaUC.size()>0)
        {
            if(esBlancoRelacionCargosYLocales(conn, datosVal))
            {
                int numeroBices = 0;
                int numeroNoBices = 0;
                for(int i = 0; i<listaUC.size() &&resultado==null; i++)
                {
                    UnidadConstructivaCatastro uniCo= (UnidadConstructivaCatastro)listaUC.get(i);
                    if(uniCo.getTipoUnidad().equalsIgnoreCase(bice))
                    {
                        numeroBices++;
                    }
                    else
                    {
                        numeroNoBices++;
                    }
                    if(numeroBices>0 && numeroNoBices>0)
                    {
                        return "Error.G74";
                    }
                    resultado = ValidaUC(conn, uniCo, listaUC);
                }
                if(resultado==null && hayRustica && !haySuperficieHuella)
                {
                    return "Error.J3";
                }
            }
            else
            {
                return "Error.G56";
            }
        }

        return resultado;
    }

    private boolean esBlancoRelacionCargosYLocales(Connection conn, DatosValoracion datosVal)
            throws DataException
    {
        boolean resultado = true;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {
            s = conn.prepareStatement("MCobtenerAnnoEfectosTotalPonencia");
            s.setInt(1,annoAprobacionPonencia);
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
                resultado = false;
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

    public String ValidaUC(Connection conn, UnidadConstructivaCatastro unidadCons, ArrayList lstUC) throws DataException
    {
    	String resultado = null;
        try
        {
            resultado = validaDatosIdentificacion(conn, unidadCons, lstUC);
            if(resultado==null)
            {
                if(!unidadCons.getTipoUnidad().equalsIgnoreCase(rustica))
                {
                    resultado = validaDomicilio(conn, unidadCons);
                }
                if(resultado==null)
                {
                    resultado = validaDatosFisicos(conn, unidadCons);
                    if(resultado==null)
                    {
                        resultado = validaDatosEconomicos(conn, unidadCons);
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new DataException(e);
        }

        return resultado;
    }
    
    public String ValidaUC(Connection conn, UnidadConstructivaCatastro unidadCons) throws DataException
    {
        String resultado = null;
        try
        {
//            resultado = validaDatosIdentificacion(conn, unidadCons);
//            if(resultado==null)
//            {
                if(!unidadCons.getTipoUnidad().equalsIgnoreCase(rustica))
                {
                    resultado = validaDomicilio(conn, unidadCons);
                }
                if(resultado==null)
                {
                    resultado = validaDatosFisicos(conn, unidadCons);
                    if(resultado==null)
                    {
                        resultado = validaDatosEconomicos(conn, unidadCons);
                    }
                }
//            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new DataException(e);
        }

        return resultado;
    }

    private String validaDatosIdentificacion(Connection conn, UnidadConstructivaCatastro unidadCons, ArrayList lstUC) throws DataException
    {//Añadimos un segundo parámetro a la consulta, la parcela catastral.
        String resultado = null;
        if(unidadCons.getCodUnidadConstructiva()!=null && !unidadCons.getCodUnidadConstructiva().equalsIgnoreCase("")
                && !unidadCons.getCodUnidadConstructiva().equalsIgnoreCase("0")
                && unidadCons.getRefParcela().getRefCatastral()!=null 
                && !unidadCons.getRefParcela().getRefCatastral().equalsIgnoreCase(""))
        {
        	if (!unidadCons.getTipoUnidad().equals(urbana)&&!unidadCons.getTipoUnidad().equals(rustica)&&!unidadCons.getTipoUnidad().equals(bice))
        		return "Error.G57";
        	
        	for (java.util.Iterator iterLstUC = (java.util.Iterator) lstUC.iterator(); iterLstUC.hasNext();){
        		
        		Object object = iterLstUC.next();
        		if (object instanceof UnidadConstructivaCatastro){
        			
        			if ( object != unidadCons && ((UnidadConstructivaCatastro)object).getCodUnidadConstructiva().equals(unidadCons.getCodUnidadConstructiva())){
        				resultado = "Error.J1";
        			}
        		}
        	}
//            PreparedStatement s = null;
//            ResultSet r = null;
//            try
//            {
//                s = conn.prepareStatement("MCexisteDuplicadosUC");
//                s.setString(1,unidadCons.getCodUnidadConstructiva());
//                s.setString(2, unidadCons.getRefParcela().getRefCatastral());
//                r = s.executeQuery();
//                if(r.next())
//                {
//                	resultado = "Error.J1";
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
            resultado = "Error.J2";
        }

        return resultado;
    }

    private String validaDomicilio(Connection conn, UnidadConstructivaCatastro unidadCons) throws DataException
    {
        String resultado = null;
        if(unidadCons.getDirUnidadConstructiva()!=null && unidadCons.getDirUnidadConstructiva().getTipoVia()!=null
                && !unidadCons.getDirUnidadConstructiva().getTipoVia().equalsIgnoreCase("")
                && unidadCons.getDirUnidadConstructiva().getNombreVia()!=null
                && !unidadCons.getDirUnidadConstructiva().getNombreVia().equalsIgnoreCase(""))
        {
            PreparedStatement s = null;
            ResultSet r = null;
            try
            {
                s = conn.prepareStatement("MCobtenerIDViaCorrecta");
                s.setString(1,unidadCons.getDirUnidadConstructiva().getTipoVia().toUpperCase());
                s.setString(2,unidadCons.getDirUnidadConstructiva().getNombreVia().toUpperCase());
                r = s.executeQuery();
                if(r.next())
                {
                    if(r.getInt("codigocatastro")!=unidadCons.getDirUnidadConstructiva().getCodigoVia())
                    {
                        resultado = "Error.G6";                           
                    }
                }
                else
                {
                    resultado = "Error.G59";
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
        }
        else
        {
            resultado = "Error.G59";
        }

        return resultado;
    }

    private String validaDatosFisicos(Connection conn, UnidadConstructivaCatastro unidadCons) throws DataException
    {
        try
        {
	        if(unidadCons.getTipoUnidad().equalsIgnoreCase(rustica))
	        {
	            hayRustica= true;
	            if(unidadCons.getDatosFisicos().getSupOcupada()!=null &&
	                    unidadCons.getDatosFisicos().getSupOcupada().floatValue()>(float)0)
	            {
	                haySuperficieHuella= true;
	            }
	        }
	        if(unidadCons.getDatosFisicos().getIndExactitud()!=null &&
	                !unidadCons.getDatosFisicos().getIndExactitud().equalsIgnoreCase("") &&
	                !unidadCons.getDatosFisicos().getIndExactitud().equalsIgnoreCase("+") &&
	                !unidadCons.getDatosFisicos().getIndExactitud().equalsIgnoreCase("-") &&
	                !unidadCons.getDatosFisicos().getIndExactitud().equalsIgnoreCase("E") &&
	                !unidadCons.getDatosFisicos().getIndExactitud().equalsIgnoreCase("C") &&
	                !unidadCons.getDatosFisicos().getIndExactitud().equalsIgnoreCase("D"))
	        {
	            return "Error.G63";
	        }
	        if (unidadCons.getDatosFisicos().getAnioConstruccion()!=null && unidadCons.getDatosFisicos().getAnioConstruccion().intValue()!=0){
	        	if (unidadCons.getDatosFisicos().getIndExactitud()==null || unidadCons.getDatosFisicos().getIndExactitud().equalsIgnoreCase(""))
	                return "Error.G64";
	        }
	        if(!checkeaNecesarioAnnoConstruccion(conn, unidadCons))
	        {
	            return "Error.G65";
	        }
	        if (unidadCons.getDatosFisicos().getAnioConstruccion()!=null && unidadCons.getDatosFisicos().getAnioConstruccion().intValue()!=0){
	        	Calendar aux= new GregorianCalendar();
	            aux.setTime(new Date(System.currentTimeMillis()));
	            int anno = aux.get(Calendar.YEAR);
	            if(unidadCons.getDatosFisicos().getAnioConstruccion().intValue()>anno)
	            {
	            	return "Error.G66";
	            }
	        }
        }catch(Exception e)
        {
        	throw new DataException(e);                    
        }

        return null;
    }

    private boolean checkeaNecesarioAnnoConstruccion(Connection conn, UnidadConstructivaCatastro unidadCons) throws DataException
    {
        boolean resultado = true;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {
            s = conn.prepareStatement("MCobtenerAplicacionFormulaPonencia");
            s.setInt(1, annoAprobacionPonencia);
            s.setString(2, idMunicipio);
            r = s.executeQuery();
            String indicadorFiabilidadFechaConstruccion = null;
            if(r.next())
            {
                indicadorFiabilidadFechaConstruccion = r.getString("aplicacion_formula");
            }
            if(((indicadorFiabilidadFechaConstruccion!=null && indicadorFiabilidadFechaConstruccion.equalsIgnoreCase("S"))
                    ||(unidadCons.getDatosFisicos().getIndExactitud()!=null &&
                    !unidadCons.getDatosFisicos().getIndExactitud().equalsIgnoreCase("")))
                    &&(unidadCons.getDatosFisicos().getAnioConstruccion()==null ||
                    unidadCons.getDatosFisicos().getAnioConstruccion().intValue()<1000))
            {
                resultado = false;
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

    private String validaDatosEconomicos(Connection conn , UnidadConstructivaCatastro unidadCons) throws DataException
    {
        if(unidadCons.getTipoUnidad().equalsIgnoreCase(rustica))
        {
            if(!(unidadCons.getDatosEconomicos().getNumFachadas()==null ||
                    unidadCons.getDatosEconomicos().getNumFachadas().equalsIgnoreCase("N")||
                    unidadCons.getDatosEconomicos().getNumFachadas().equalsIgnoreCase(""))||
                    unidadCons.getDatosEconomicos().isCorrectorLongFachada().booleanValue())
            {
                return "Error.G60";
            }
        }
        else
        {
            if(unidadCons.getDatosEconomicos().getNumFachadas()==null ||
                    (!unidadCons.getDatosEconomicos().getNumFachadas().equalsIgnoreCase("") &&
                    !unidadCons.getDatosEconomicos().getNumFachadas().equalsIgnoreCase("2") &&
                    !unidadCons.getDatosEconomicos().getNumFachadas().equalsIgnoreCase("3") &&
                    !unidadCons.getDatosEconomicos().getNumFachadas().equalsIgnoreCase("N"))
                    )
            {
                return "Error.G61";
            }
            if(unidadCons.getDatosEconomicos().isCorrectorLongFachada().booleanValue() &&
                    (unidadCons.getDatosEconomicos().getNumFachadas()==null ||
                    unidadCons.getDatosEconomicos().getNumFachadas().equalsIgnoreCase("") ||
                    unidadCons.getDatosEconomicos().getNumFachadas().equalsIgnoreCase("N") ||
                    Integer.parseInt(unidadCons.getDatosEconomicos().getNumFachadas())<=0))
            {
                return  "Error.G62";
            }
        }
        if(esCorrectaPonencia)
        {
            try
            {
                String resultado = null;
                if(!unidadCons.getTipoUnidad().equalsIgnoreCase(rustica)&&annoRevisionTotal<2006)
                {
                    resultado = checkeaViaPonencia(conn, unidadCons);
                    if(resultado!=null)
                    {
                        return resultado;
                    }
                }
                if(!unidadCons.getTipoUnidad().equalsIgnoreCase(rustica)&&annoRevisionTotal>=2006)
                {
                    resultado = checkeZonaValor(conn, unidadCons);
                    if(resultado!=null)
                    {
                        return resultado;
                    }
                }
                resultado = checkeaCorrectores(conn, unidadCons);
                return resultado;
            }
            catch(Exception e)
            {
                throw new DataException(e);
            }
        }
        return null;
    }

    private String checkeaViaPonencia(Connection conn,UnidadConstructivaCatastro unidadCons) throws DataException
    {
        String resultado = null;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {
            s = conn.prepareStatement("MCobtenerCodViaTramoPonenciaUC");
            s.setString(1,unidadCons.getDatosEconomicos().getTramoPonencia().getCodTramo());
            s.setInt(2, annoAprobacionPonencia);
            s.setString(3, idMunicipio);
            r = s.executeQuery();
            String codVia = null;
            if(r.next())
            {
                codVia = r.getString("codigo_via");
                if(!codVia.equalsIgnoreCase(unidadCons.getDatosEconomicos().getTramoPonencia().getCodVia())
                &&Integer.parseInt(codVia)!=unidadCons.getDirUnidadConstructiva().getCodigoVia())
                {
                    s = conn.prepareStatement("MCcompruebaCodigoVia");
                    s.setInt(1,Integer.parseInt(codVia));
                    r = s.executeQuery();
                    if(!r.next())
                    {
                        resultado = "Error.G67";    
                    }
                }
            }
            else
            {
                resultado = "Error.G68";
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

    private String checkeZonaValor(Connection conn,UnidadConstructivaCatastro unidadCons)throws DataException
    {
    	if (unidadCons.getDatosEconomicos().getZonaValor().getCodZonaValor() == null)
    		return "Error.G69";
        String resultado = null;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {
            s = conn.prepareStatement("MCvalidarZonaValorUC");
            s.setString(1,unidadCons.getDatosEconomicos().getZonaValor().getCodZonaValor());
            s.setInt(2, annoAprobacionPonencia);
            s.setString(3, idMunicipio);
            r = s.executeQuery();
            if(r.next())
            {
                s = conn.prepareStatement("MCexisteZona_VBRPoligonIgualZonaValorUC");
                s.setString(1,unidadCons.getDatosEconomicos().getZonaValor().getCodZonaValor());
                s.setInt(2, annoAprobacionPonencia);
                s.setString(3, idMunicipio);
                r = s.executeQuery();
                if(!r.next()|| !haySueloUnitario(conn))
                {
                    boolean haySueloConZonaValor = false;
                    if (listaSuelos != null){
	                    for(int i=0; i< listaSuelos.size() && !haySueloConZonaValor;i++)
	                    {
	                        SueloCatastro suelo = (SueloCatastro)listaSuelos.get(i);
	                    	if (suelo.getDatosEconomicos().getZonaValor().getCodZonaValor() == null)
	                    		return "Error.G21";
	                        if(suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().equalsIgnoreCase(
	                                unidadCons.getDatosEconomicos().getZonaValor().getCodZonaValor()))
	                        {
	                            haySueloConZonaValor= true;
	                        }
	                    }
                    }
                    if(!haySueloConZonaValor)
                    {
                        resultado = "Error.G70";
                    }
                }
            }
            else
            {
                resultado = "Error.G69";
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

    private boolean haySueloUnitario(Connection conn) throws DataException
    {
        if(listaSuelos!=null)
        {
            for(int i = 0; i<listaSuelos.size();i++)
            {
                SueloCatastro suelo = (SueloCatastro)listaSuelos.get(i);
                if(suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("U"))
                {
                    return true;
                }
                else
                {
                    boolean hayUnitaria = false;
                    PreparedStatement s = null;
                    ResultSet r = null;
                    try
                    {
                        s = conn.prepareStatement("MCgetCodigoCalificacionSuelo");
                        s.setString(1,suelo.getDatosEconomicos().getZonaUrbanistica().getCodZona());
                        s.setInt(2, annoAprobacionPonencia);
                        s.setString(3, idMunicipio);
                        r = s.executeQuery();
                        if(r.next())
                        {
                            String codigoCalificacion = r.getString("codigo_calificacion");
                            if(suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().equals("U"))
                            	hayUnitaria = true;
                            
                            if(suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("PU")&&
                                    (!codigoCalificacion.startsWith("PP") && !codigoCalificacion.startsWith("UG")))
                            {
                                hayUnitaria = true;
                            }
                            else if(suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("P")&&
                                    (codigoCalificacion.startsWith("PP") || codigoCalificacion.startsWith("UG")))
                            {
                                hayUnitaria = false;
                            }
                        }
                        //TODO no se considera que pueda pasar un else si los datos estan bien introducidos.
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
                    if(hayUnitaria)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String checkeaCorrectores(Connection conn,UnidadConstructivaCatastro unidadCons)throws DataException
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
                if(!conservacionCorrecta(conn, unidadCons, annoNormas))
                {
                    resultado= "Error.G71";
                }
                else if(annoNormas<1986&&(unidadCons.getDatosEconomicos().isCorrectorNoLucrativo()!=null&&
                        unidadCons.getDatosEconomicos().isCorrectorNoLucrativo().booleanValue()))
                {
                    resultado = "Error.G72";
                }
                else if(annoNormas<1989 &&(unidadCons.getDatosEconomicos().getCoefCargasSingulares()!=null
                &&!(unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()==(float)0.7 ||
                unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()==(float)1)))
                {
                    resultado = "Error.G42";
                }
                else if(annoNormas==1989 &&(unidadCons.getDatosEconomicos().getCoefCargasSingulares()!=null
                &&!(unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()>=(float)0.7 &&
                unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()<=(float)1)))
                {
                    resultado = "Error.G43";
                }
                else if(annoNormas>1989 &&(unidadCons.getDatosEconomicos().getCoefCargasSingulares()!=null
                &&!(unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()==(float)0.7 ||
                unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()==(float)0.8 ||
                unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()==(float)0.9 ||
                unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()==(float)1)))
                {
                    resultado = "Error.G44";
                }
                else if((unidadCons.getDatosEconomicos().getCoefCargasSingulares()!=null &&
                        unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()!=(float)1
                        && unidadCons.getDatosEconomicos().isCorrectorSitEspeciales()!=null
                        && unidadCons.getDatosEconomicos().isCorrectorSitEspeciales().booleanValue()))
                {
                	resultado = "Error.G73";
                }
                if((unidadCons.getDatosEconomicos().getCoefCargasSingulares()!=null &&
                        unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()!=(float)1
                        && unidadCons.getDatosEconomicos().isCorrectorSitEspeciales()!=null
                        && unidadCons.getDatosEconomicos().isCorrectorSitEspeciales().booleanValue()))
                {
                    resultado = "Error.G73";
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

    private boolean conservacionCorrecta(Connection conn,UnidadConstructivaCatastro unidadCons, int annoNormas)throws DataException
    {
        boolean resultado = false;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {
            s = conn.prepareStatement("MCobtenerCoefCorrectorEstadoConservacion");
            s.setInt(1, annoNormas);
            r = s.executeQuery();
            String coefCorrectorConservacion = "";
            while(r.next() && !resultado)
            {
                coefCorrectorConservacion = r.getString("conservacion");
                if(unidadCons.getDatosEconomicos().getCorrectorConservacion()!=null &&
                   !unidadCons.getDatosEconomicos().getCorrectorConservacion().equalsIgnoreCase("")&&
                   coefCorrectorConservacion.equalsIgnoreCase(unidadCons.getDatosEconomicos().getCorrectorConservacion()))
                {
                    resultado = true;
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
