package com.geopista.app.catastro.intercambio.edicion.validacion;

import com.geopista.app.catastro.intercambio.exception.DataException;
import com.geopista.app.catastro.model.beans.BienInmuebleJuridico;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.SueloCatastro;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 07-may-2007
 * Time: 11:10:25
 * To change this template use File | Settings | File Templates.
 */
public class DatosValoracion
{
    Integer annoDeLasNormas;
    Integer formaDeCalculo;
    String codigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo;
    String metodoCalculoValorSuelo;
    String tipoRegistroConstruccionEnFin;
    Integer numeroMinimoSubparcelas;
    String indicadorValidacionSuperficieTotalSubParcelas;
    String indicadorValidacionSuperficieConstruida;
    String tipoRelacionEntreCargosLocales;
    String indicadorElementosComunes;
    Integer coeficientePropiedadCargosDeSuelo;
    Integer coeficientePropiedadCargosConstruccion;
    String metodoRepartoValorDeConstruccion;
    boolean esCorrectaFormaCalculo;
    private static final String rustica = "RU";

    public DatosValoracion()
    {

    }

    public Integer getFormaDeCalculo()
    {
        return formaDeCalculo;
    }

    public void setFormaDeCalculo(Integer formaDeCalculo)
    {
        this.formaDeCalculo = formaDeCalculo;
    }

    public String getTipoRelacionEntreCargosLocales()
    {
        return tipoRelacionEntreCargosLocales;
    }

    public void setTipoRelacionEntreCargosLocales(String tipoRelacionEntreCargosLocales)
    {
        this.tipoRelacionEntreCargosLocales = tipoRelacionEntreCargosLocales;
    }

    public boolean isEsCorrectaFormaCalculo()
    {
        return esCorrectaFormaCalculo;
    }

    public void setEsCorrectaFormaCalculo(boolean esCorrectaFormaCalculo)
    {
        this.esCorrectaFormaCalculo = esCorrectaFormaCalculo;
    }

    public String getIndicadorElementosComunes()
    {
        return indicadorElementosComunes;
    }

    public void setIndicadorElementosComunes(String indicadorElementosComunes)
    {
        this.indicadorElementosComunes = indicadorElementosComunes;
    }

    public String getMetodoCalculoValorSuelo()
    {
        return metodoCalculoValorSuelo;
    }

    public void setMetodoCalculoValorSuelo(String metodoCalculoValorSuelo)
    {
        this.metodoCalculoValorSuelo = metodoCalculoValorSuelo;
    }

    public String getIndicadorValidacionSuperficieConstruida()
    {
        return indicadorValidacionSuperficieConstruida;
    }

    public void setIndicadorValidacionSuperficieConstruida(String indicadorValidacionSuperficieConstruida)
    {
        this.indicadorValidacionSuperficieConstruida = indicadorValidacionSuperficieConstruida;
    }

    public String getCodigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo()
    {
        return codigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo;
    }

    public void setCodigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo(String codigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo)
    {
        this.codigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo = codigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo;
    }

    public Integer getCoeficientePropiedadCargosDeSuelo()
    {
        return coeficientePropiedadCargosDeSuelo;
    }

    public void setCoeficientePropiedadCargosDeSuelo(Integer coeficientePropiedadCargosDeSuelo)
    {
        this.coeficientePropiedadCargosDeSuelo = coeficientePropiedadCargosDeSuelo;
    }

    public Integer getCoeficientePropiedadCargosConstruccion()
    {
        return coeficientePropiedadCargosConstruccion;
    }

    public void setCoeficientePropiedadCargosConstruccion(Integer coeficientePropiedadCargosConstruccion)
    {
        this.coeficientePropiedadCargosConstruccion = coeficientePropiedadCargosConstruccion;
    }

    public String getIndicadorValidacionSuperficieTotalSubParcelas()
    {
        return indicadorValidacionSuperficieTotalSubParcelas;
    }

    public void setIndicadorValidacionSuperficieTotalSubParcelas(String indicadorValidacionSuperficieTotalSubParcelas)
    {
        this.indicadorValidacionSuperficieTotalSubParcelas = indicadorValidacionSuperficieTotalSubParcelas;
    }

    public Integer getAnnoDeLasNormas()
    {
        return annoDeLasNormas;
    }

    public void setAnnoDeLasNormas(Integer annoDeLasNormas)
    {
        this.annoDeLasNormas = annoDeLasNormas;
    }

    public String getTipoRegistroConstruccionEnFin()
    {
        return tipoRegistroConstruccionEnFin;
    }

    public void setTipoRegistroConstruccionEnFin(String tipoRegistroConstruccionEnFin)
    {
        this.tipoRegistroConstruccionEnFin = tipoRegistroConstruccionEnFin;
    }

    public Integer getNumeroMinimoSubparcelas()
    {
        return numeroMinimoSubparcelas;
    }

    public void setNumeroMinimoSubparcelas(Integer numeroMinimoSubparcelas)
    {
        this.numeroMinimoSubparcelas = numeroMinimoSubparcelas;
    }

    public String getMetodoRepartoValorDeConstruccion()
    {
        return metodoRepartoValorDeConstruccion;
    }

    public void setMetodoRepartoValorDeConstruccion(String metodoRepartoValorDeConstruccion)
    {
        this.metodoRepartoValorDeConstruccion = metodoRepartoValorDeConstruccion;
    }

    public void calculaDatosValoracion(Connection conn, Integer formaCalculo, Integer annoAprobacion,
                                       FincaCatastro finca, String idMunicipio)
            throws DataException
    {
        if(annoAprobacion!=null && annoAprobacion.intValue()!=0)
        {
            this.annoDeLasNormas = calculaAnnoNormas(conn,annoAprobacion, idMunicipio);
        }
        else
        {
            this.annoDeLasNormas = null;
        }
        this.formaDeCalculo = formaCalculo;
        if(this.annoDeLasNormas!=null&&this.formaDeCalculo!=null &&annoAprobacion!=null && annoAprobacion.intValue()<2005)
        {
            if(!recopilaDatosBD(conn))
            {
                datosPonenciaIncorrecta();
            }
        }
        else if(annoAprobacion!=null && annoAprobacion.intValue()>=2005)
        {
            generaDatosValoracion(conn, finca, annoAprobacion.intValue(), idMunicipio);
        }
        else
        {
            datosPonenciaIncorrecta();
        }
    }

    private void datosPonenciaIncorrecta()
    {
        formaDeCalculo = null;
        codigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo = "";
        metodoCalculoValorSuelo = "";
        tipoRegistroConstruccionEnFin = "";
        numeroMinimoSubparcelas = new Integer(1);
        indicadorValidacionSuperficieTotalSubParcelas = "S";
        indicadorValidacionSuperficieConstruida = "";
        tipoRelacionEntreCargosLocales = "";
        indicadorElementosComunes= "";
        coeficientePropiedadCargosDeSuelo = null;
        coeficientePropiedadCargosConstruccion = null;
        metodoRepartoValorDeConstruccion = "";
        esCorrectaFormaCalculo = false;
    }

    private boolean recopilaDatosBD(Connection conn) throws DataException
    {
        boolean resultado = false;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {
            s = conn.prepareStatement("MCobtenerDatosValoracionTCalc");
            s.setInt(1, this.formaDeCalculo.intValue());
            s.setInt(2, this.annoDeLasNormas.intValue());
            r = s.executeQuery();
            if(r.next())
            {
                resultado = true;
                this.codigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo = r.getString("TCAL_EPPSP");
                this.metodoCalculoValorSuelo = r.getString("TCAL_SUELO");
                this.tipoRegistroConstruccionEnFin = r.getString("TCAL_REG");
                this.numeroMinimoSubparcelas = new Integer(r.getInt("TCAL_MNSP"));
                this.indicadorValidacionSuperficieTotalSubParcelas = r.getString("TCAL_ASTOT");
                this.indicadorValidacionSuperficieConstruida = r.getString("TCAL_AC");
                this.tipoRelacionEntreCargosLocales = r.getString("TCAL_REL");
                this.indicadorElementosComunes = r.getString("TCAL_COMUN");
                this.coeficientePropiedadCargosDeSuelo = new Integer(r.getInt("TCAL_PJESP"));
                this.coeficientePropiedadCargosConstruccion = new Integer(r.getInt("TCAL_PJEUVC"));
                this.metodoRepartoValorDeConstruccion = r.getString("TCAL_REP2");
                this.esCorrectaFormaCalculo = true;
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

    private void generaDatosValoracion(Connection conn,FincaCatastro finca, int annoAprobacion, String idMunicipio)throws DataException
    {
        this.coeficientePropiedadCargosDeSuelo = null;
        this.coeficientePropiedadCargosConstruccion = null;
        this.codigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo = "";
        this.tipoRegistroConstruccionEnFin = "";
        this.numeroMinimoSubparcelas = new Integer(1);
        if(finca!=null && finca.getLstConstrucciones()!=null && finca.getLstConstrucciones().size()>0)
        {
            this.indicadorValidacionSuperficieTotalSubParcelas = "S";
            this.indicadorValidacionSuperficieConstruida = "S";
            this.tipoRelacionEntreCargosLocales = "11";
            this.indicadorElementosComunes = "S";
            this.metodoRepartoValorDeConstruccion = "VC";
            this.esCorrectaFormaCalculo = true;

            ValidacionBI valBI = new ValidacionBI(idMunicipio,CriteriosValidacion.esFincaBICE(finca),CriteriosValidacion.existeUrbanaEnFinca(finca),
                    CriteriosValidacion.existeRusticaEnFinca(finca), finca, CriteriosValidacion.esNecesariaPonencia(finca), new Integer(annoAprobacion));
            valBI.calculaClaseListaBI();
            if(hayMasDeUnCargoUrbanoORustico(finca.getLstBienesInmuebles()))
            {
                this.formaDeCalculo = new Integer(42);
            }
            else
            {
                this.formaDeCalculo = new Integer(32);
            }
            if(CriteriosValidacion.existeUrbanaEnFinca(finca))
            {
                boolean hayZonaUnitario = calculaZonaUnitario(conn, finca, annoAprobacion, idMunicipio);
                boolean hayZonaRepercusion = calculaZonaRepercusion(conn, finca, annoAprobacion, idMunicipio);
                if(hayZonaUnitario && hayZonaRepercusion)
                {
                    this.metodoCalculoValorSuelo = "MX";
                }
                else if(hayZonaUnitario && !hayZonaRepercusion)
                {
                    this.metodoCalculoValorSuelo = "";
                }
                else
                {
                    this.metodoCalculoValorSuelo = "RP";
                }
            }
            else
            {
                this.metodoCalculoValorSuelo = "";
            }
        }
        else if(finca!=null && (finca.getLstConstrucciones()==null || finca.getLstConstrucciones().size()==0))
        {
            this.formaDeCalculo = new Integer(11);
            this.metodoCalculoValorSuelo = "";
            this.indicadorValidacionSuperficieTotalSubParcelas = "S";
            this.indicadorValidacionSuperficieConstruida = "";
            this.tipoRelacionEntreCargosLocales = "";
            this.indicadorElementosComunes = "";
            this.metodoRepartoValorDeConstruccion = "";
            this.esCorrectaFormaCalculo = true;
        }
    }

    private boolean calculaZonaUnitario(Connection conn, FincaCatastro finca, int annoAprobacion, String idMunicipio)throws DataException
    {
        ArrayList listaSuelos = finca.getLstSuelos();
        if(listaSuelos!=null)
        {
            for(int i=0;i<listaSuelos.size();i++)
            {
                SueloCatastro suelo = (SueloCatastro)listaSuelos.get(i);
                rellenaCodCalificacion(conn, suelo, annoAprobacion, idMunicipio);
                if(suelo.getDatosEconomicos().getZonaValor().getCodZonaValor()!=null &&
                        (suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("U")||
                        suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("PU") ||
                        (suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("PR")&&
                        suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion()!=null &&
                        (suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("PP")||
                        suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("UG")))))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean calculaZonaRepercusion (Connection conn, FincaCatastro finca, int annoAprobacion, String idMunicipio)throws DataException
    {
        ArrayList listaSuelos = finca.getLstSuelos();
        if(listaSuelos!=null)
        {
            for(int i=0;i<listaSuelos.size();i++)
            {
                SueloCatastro suelo = (SueloCatastro)listaSuelos.get(i);
                if(suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion()==null)
                {
                    rellenaCodCalificacion(conn, suelo, annoAprobacion, idMunicipio);
                }
                if(suelo.getDatosEconomicos().getZonaValor().getCodZonaValor()!=null &&
                        (suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("R")||
                        (suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().startsWith("PR")&&
                        suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion()!=null &&
                        (suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("PP")||
                        suelo.getDatosEconomicos().getZonaUrbanistica().getCodCalificacion().equalsIgnoreCase("UG")))))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private void rellenaCodCalificacion(Connection conn, SueloCatastro suelo, int annoAprobacion, String idMunicipio)throws DataException
    {
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {
            s = conn.prepareStatement("MCobtenerCodCalificacionPonenciaUrbana");
            s.setString(1,suelo.getDatosEconomicos().getZonaUrbanistica().getCodZona());
            s.setInt(2, annoAprobacion);
            s.setString(3, idMunicipio);
            r = s.executeQuery();
            if(r.next())
            {
                suelo.getDatosEconomicos().getZonaUrbanistica().setCodCalificacion(r.getString("codigo_calificacion"));
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

    private boolean hayMasDeUnCargoUrbanoORustico(ArrayList listaBI)
    {
        int numUrb =0;
        int numRus =0;
        for(int i =0;i< listaBI.size();i++)
        {
            //BienInmuebleCatastro bi = (BienInmuebleCatastro)listaBI.get(i);
        	BienInmuebleCatastro bi = ((BienInmuebleJuridico)listaBI.get(i)).getBienInmueble();
            if(bi.getClaseBienInmueble().equalsIgnoreCase(rustica))
            {
                numRus = numRus +1;
            }
            else
            {
                numUrb = numUrb +1;
            }
            if(numUrb>2 || numRus>2)
            {
                return true;
            }
        }
        return false;
    }

    private Integer calculaAnnoNormas(Connection conn, Integer annoAprobacionPonencia, String idMunicipio) throws DataException
    {
        Integer annoNormas = null;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {
            s = conn.prepareStatement("MCobtenerAnnoDeNormasPonencia");
            s.setInt(1, annoAprobacionPonencia.intValue());
            s.setString(2, idMunicipio);
            r = s.executeQuery();
            if(r.next())
            {
                annoNormas = new Integer(r.getInt("anno_normas"));
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
        return annoNormas;
    }
}
