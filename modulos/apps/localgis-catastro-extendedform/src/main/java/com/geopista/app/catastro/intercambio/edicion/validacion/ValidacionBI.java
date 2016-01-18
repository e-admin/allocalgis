/**
 * ValidacionBI.java
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
import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.IdBienInmueble;
import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;
import com.vividsolutions.jump.I18N;

public class ValidacionBI
{
    private static final String rustica = "RU";
    private static final String urbana = "UR";
    private static final String bice = "BI";
    private ArrayList listaUCFinca;
    private ArrayList listaCultivos;
    private ArrayList listaUCAsociadasCargo;
    private boolean hayBiceFinca;
    private boolean hayUrbanaFinca;
    private boolean hayRusticaFinca;
    private String idMunicipio;
    private ArrayList listaConstruccionesFinca;
    private FincaCatastro finca;
    private boolean esCorrectaPonencia;
    private int annoAprobacionPonencia;
    private int annoNormas;

    public ValidacionBI(String idMunicipio, boolean hayBiceFinca, boolean hayUrbanaFinca, boolean hayRusticaFinca
            , FincaCatastro finca, boolean esCorrectaPonencia, Integer annoAprobacionPonencia)
    {
        this.idMunicipio= idMunicipio;
        this.listaUCFinca = finca.getLstUnidadesConstructivas();
        this.hayBiceFinca = hayBiceFinca;
        this.hayUrbanaFinca = hayUrbanaFinca;
        this.hayRusticaFinca = hayRusticaFinca;
        this.listaCultivos = finca.getLstCultivos();
        this.listaConstruccionesFinca = finca.getLstConstrucciones();
        this.finca = finca;
        this.esCorrectaPonencia = esCorrectaPonencia;
        if(annoAprobacionPonencia != null)
            this.annoAprobacionPonencia = annoAprobacionPonencia.intValue();
    }

    public StringBuffer ValidaListaBI(Connection conn, ArrayList listaBI, DireccionLocalizacion dirFinca, DatosValoracion datosVal)
            throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
    	
     //   String resultado = null;
        boolean hayCargoRustica = false;
        boolean hayCargoUrbana = false;
        int numeroBices = 0;
        int numeroNoBices = 0;
        if(listaBI!=null && listaBI.size()>0)
        {
            for(int i = 0; i<listaBI.size(); i++)
            {
                BienInmuebleJuridico bij= (BienInmuebleJuridico)listaBI.get(i);
                BienInmuebleCatastro bi = bij.getBienInmueble();
                asignaCultivosBI(bi, listaCultivos);
                asignaConstruccionesBI(bi);
                asignaUCAsociadasConsCargo(bi);
                StringBuffer sbValClaseBi = determinaClaseBI(bi);
                if(sbValClaseBi != null){
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(sbValClaseBi.toString());
                }
               
                if(bi.getClaseBienInmueble().equalsIgnoreCase(bice))
                {
                    hayCargoUrbana = true;
                    numeroBices++;
                }
                else
                {
                    numeroNoBices++;
                    if(bi.getClaseBienInmueble().equalsIgnoreCase(rustica))
                    {
                        hayCargoRustica = true;
                    }
                    if(bi.getClaseBienInmueble().equalsIgnoreCase(urbana))
                    {
                        hayCargoUrbana = true;
                    }
                }
                StringBuffer sbValBi = ValidaBI(conn, bi, datosVal);
                if( sbValBi != null){
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append( sbValBi.toString());
                }
                
            }
            if(numeroBices>0 && numeroNoBices>0)
            {
            	if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("ValidacionMensajesError","Error.G128")+"\n\n\r");
            }
            if(hayRusticaFinca&&!hayCargoRustica)
            {
            	if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("ValidacionMensajesError","Error.G130")+"\n\n\r");;
            }
            if(hayUrbanaFinca &&!hayCargoUrbana)
            {
            	if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("ValidacionMensajesError","Error.G131")+"\n\n\r");
            }
	           
            if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
	            if(validaDomicilioTributarioDuplicado(listaBI))
	            {
	            	if(sbVal == null){
	            		sbVal = new StringBuffer();
	            	}
	            	sbVal.append(I18N.get("ValidacionMensajesError","Error.G117")+"\n\n\r");
	            }
	            if(hayUrbanaFinca&&!validaDomicilioEnFinca(listaBI, dirFinca))
	            {
	            	if(sbVal == null){
	            		sbVal = new StringBuffer();
	            	}
	            	sbVal.append(I18N.get("ValidacionMensajesError","Error.G132")+"\n\n\r");
	            }
            }  
        
        	if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
        		// solo se realiza esta validación si el expediente es de situaciones finales
        		StringBuffer sbValDepFormulaCalculo = validaDependientesFormulaCalculo(conn ,listaBI, datosVal);
        		if(sbValDepFormulaCalculo != null){
        			if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
        			sbVal.append(sbValDepFormulaCalculo.toString());
        		}
        	}
            
        	StringBuffer sbValUsoBi = validaUsosBI(conn, listaBI, datosVal);
        	if(sbValUsoBi != null){
    			if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
    			sbVal.append(sbValUsoBi.toString());
    		}
        }
        else
        {
        	if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
        		// solo se envia este error en un expediente de situacines finales
        		// en una de variaciones se puede no haber modificado ningún bien, por lo tanto no se
        		// debería lanzar este error.
        		if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("ValidacionMensajesError","Error.G129")+"\n\n\r");
        	}
        }
        return sbVal;
    }

    private StringBuffer determinaClaseBI(BienInmuebleCatastro bi)
    {
    	StringBuffer sbVal = null;
        if(hayBiceFinca)
        {
            bi.setClaseBienInmueble(bice);
            return null;
        }
        if(hayUrbanaFinca && !hayRusticaFinca)
        {
            bi.setClaseBienInmueble(urbana);
            return null;
        }
        //prueba, ogalende
        /*if(bi.getClaseBienInmueble().equals("UR") && !bi.getClaseBienInmueble().equals("RU")){
        	hayUrbanaFinca=true;
        	hayRusticaFinca=false;
        	hayBiceFinca=false;
        	return  null;
        }*/
        if(!hayUrbanaFinca && hayRusticaFinca)
        {
             bi.setClaseBienInmueble(rustica);
            return null;
        }
        if(hayUrbanaFinca && hayRusticaFinca)
        {
            boolean sigueUrbana =true;
            boolean sigueRustica = false;
            for(int i=0;i<bi.getLstCultivos().size() && sigueUrbana && !sigueRustica;i++)
            {
                Cultivo cul = (Cultivo)bi.getLstCultivos().get(i);
                if(cul.getTipoSuelo().equalsIgnoreCase(rustica))
                {
                    sigueRustica = true;
                    sigueUrbana = false;
                }
            }
            if(sigueUrbana&&!sigueRustica)
            {
               for(int i =0;i<listaUCAsociadasCargo.size() && sigueUrbana && !sigueRustica;i++)
               {
                    UnidadConstructivaCatastro uc= (UnidadConstructivaCatastro)listaUCAsociadasCargo.get(i);
                    if(uc.getTipoUnidad().equalsIgnoreCase(rustica))
                    {
                        sigueRustica = true;
                        sigueUrbana = false;
                    }
               }
            }
            if(sigueUrbana)
            {
                bi.setClaseBienInmueble(urbana);
                return null;
            }
            else if(sigueRustica)
            {
                bi.setClaseBienInmueble(rustica);
                return null;
            }
        }
        if(sbVal == null){
    		sbVal = new StringBuffer();
    	}
    	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
				bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J6")+"\n\n\r");
    	
        return sbVal;
    }

    private void asignaUCAsociadasConsCargo(BienInmuebleCatastro bi)
    {
        listaUCAsociadasCargo = new ArrayList();
        for(int i =0;i<bi.getLstConstrucciones().size();i++)
        {
            ConstruccionCatastro cons = (ConstruccionCatastro)bi.getLstConstrucciones().get(i);
            boolean encontrado = false;
            UnidadConstructivaCatastro uc=null;
            if(listaUCFinca != null){
	            for(int j=0; j<listaUCFinca.size() && !encontrado;j++)
	            {
	                uc = (UnidadConstructivaCatastro)listaUCFinca.get(j);
	                if(cons.getDatosFisicos().getCodUnidadConstructiva()!=null && 
	                		cons.getDatosFisicos().getCodUnidadConstructiva().equalsIgnoreCase(uc.getCodUnidadConstructiva()))
	                {
	                    listaUCAsociadasCargo.add(uc);
	                }
	            }
            }
        }
    }

    private void asignaCultivosBI(BienInmuebleCatastro bi, ArrayList listaCultivos)
    {
        ArrayList lCultivosBI = new ArrayList();
        if(listaCultivos!=null)
        {
            for(int i = 0; i<listaCultivos.size();i++)
            {
                Cultivo cultivo = (Cultivo) listaCultivos.get(i);
                if(cultivo.getCodSubparcela()!=null && cultivo.getCodSubparcela().equalsIgnoreCase(bi.getIdBienInmueble().getNumCargo()))
                {
                    lCultivosBI.add(cultivo);
                }
            }
            bi.setLstCultivos(lCultivosBI);
        }
    }

    private void asignaConstruccionesBI(BienInmuebleCatastro bi)
    {
        ArrayList lConsBI = new ArrayList();
        if(listaConstruccionesFinca!=null)
        {
            for(int i = 0; i<listaConstruccionesFinca.size();i++)
            {
                ConstruccionCatastro cons = (ConstruccionCatastro) listaConstruccionesFinca.get(i);
                if(cons.getNumOrdenBienInmueble()!=null && cons.getNumOrdenBienInmueble().equalsIgnoreCase(bi.getIdBienInmueble().getNumCargo()))
                {
                    lConsBI.add(cons);
                }
            }
            bi.setLstConstrucciones(lConsBI);
        }
    }

    private boolean validaDomicilioTributarioDuplicado(ArrayList listaBI)
    {
        for(int i = 0; i<listaBI.size();i++)
        {

            BienInmuebleJuridico bij = (BienInmuebleJuridico)listaBI.get(i);
            BienInmuebleCatastro bi = bij.getBienInmueble();
            for(int j = i+1; j<listaBI.size();j++)
            {
                BienInmuebleJuridico bij2 = (BienInmuebleJuridico)listaBI.get(j);
                BienInmuebleCatastro bi2 = bij2.getBienInmueble();
                boolean iguales = bi.getDomicilioTributario().comparaDireccionesBI(bi2.getDomicilioTributario());
                if(iguales)
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean validaDomicilioEnFinca(ArrayList listaBI, DireccionLocalizacion dirFinca)
    {
        boolean iguales = false;
        for(int i = 0; i<listaBI.size();i++)
        {
            BienInmuebleJuridico bij = (BienInmuebleJuridico)listaBI.get(i); 
            BienInmuebleCatastro bi= bij.getBienInmueble();
            iguales = iguales || dirFinca.comparaDireccionesBI(bi.getDomicilioTributario());
        }
        return iguales;
    }

    public StringBuffer ValidaBI(Connection conn, BienInmuebleCatastro bi, DatosValoracion datosVal) throws DataExceptionCatastro
    {

    	StringBuffer sbVal = null;

        try
        {
        	StringBuffer sbValIdentifica = validaIdentificacion(conn, bi.getIdBienInmueble());
        	StringBuffer sbValClase = validaClase(conn, bi);
        	StringBuffer sbValDomicilio = validaDomicilio(conn, bi);
        	StringBuffer sbValTipoPropiedad = validaTipoPropiedadYCoeficientePropiedad(bi);
        	StringBuffer sbValAdministrativo = validaPrecioAdministrativoVenta(conn, bi);
        	StringBuffer sbValValorBase = validaValorBaseYFormulaCalculo(conn,bi, datosVal);
                    
        	if(sbValIdentifica != null){
        		if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
        		sbVal.append(sbValIdentifica.toString());
        	}
        	if(sbValClase != null){
        		if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
        		sbVal.append(sbValClase.toString());
        	}
        	if(sbValDomicilio != null){
        		if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
        		sbVal.append(sbValDomicilio.toString());
        	}
        	if(sbValTipoPropiedad != null){
        		if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
        		sbVal.append(sbValTipoPropiedad.toString());
        	}
        	if(sbValAdministrativo != null){
        		if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
        		sbVal.append(sbValAdministrativo.toString());
        	}
        	if(sbValValorBase != null){
        		if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
        		sbVal.append(sbValValorBase.toString());
        	}
        }
        catch(Exception e)
        {
            throw new DataExceptionCatastro(e);
        }
        return sbVal;
    }

    private StringBuffer validaIdentificacion(Connection conn, IdBienInmueble id) throws DataExceptionCatastro
    {

    	StringBuffer sbVal = null;
        if(id!=null && id.getIdBienInmueble()!=null && !id.getIdBienInmueble().equalsIgnoreCase(""))
        {
            PreparedStatement s = null;
            ResultSet r = null;
            try
            {
                s = conn.prepareStatement("MCobtenerDuplicadosBI");
                s.setString(1,id.getIdBienInmueble());
                r = s.executeQuery();
                r.next();
                if(r.next())
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
                			id.getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J5")+"\n\n\r");
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
        }
        else
        {
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
        			id.getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J4")+"\n\n\r");
        }
        return sbVal;
    }

    private StringBuffer validaClase(Connection conn, BienInmuebleCatastro bi)throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
        for(int i = 0; i<bi.getLstCultivos().size();i++)
        {
            Cultivo cu = (Cultivo)bi.getLstCultivos().get(i);
            if(!cu.getTipoSuelo().equalsIgnoreCase(bi.getClaseBienInmueble()))
            {
            	if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
            			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G118")+"\n\n\r");
            }
        }
        if (listaUCAsociadasCargo !=null){
	        for(int i=0;i<listaUCAsociadasCargo.size();i++)
	        {
	            UnidadConstructivaCatastro uc = (UnidadConstructivaCatastro) listaUCAsociadasCargo.get(i);
	            if(!uc.getTipoUnidad().equalsIgnoreCase(bi.getClaseBienInmueble()))
	            {
	            	if(sbVal == null){
	            		sbVal = new StringBuffer();
	            	}
	            	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
	            			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G118")+"\n\n\r");
	            }
	        }
        }
        if(bi.getClaseBienInmueble().equalsIgnoreCase(bice))
        {
        	if (listaCultivos != null){
	            for(int i= 0; i<listaCultivos.size();i++)
	            {
	                Cultivo cu = (Cultivo)bi.getLstCultivos().get(i);
	                if(!cu.getTipoSuelo().equalsIgnoreCase(bice))
	                {
	                	if(sbVal == null){
	                		sbVal = new StringBuffer();
	                	}
	                	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
	                			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G118")+"\n\n\r");
	                }
	            }
        	}
        	if (listaUCFinca != null){
	            for(int i=0;i<listaUCFinca.size();i++)
	            {
	                UnidadConstructivaCatastro uc = (UnidadConstructivaCatastro) listaUCFinca.get(i);
	                if(!uc.getTipoUnidad().equalsIgnoreCase(bice))
	                {
	                	if(sbVal == null){
	                		sbVal = new StringBuffer();
	                	}
	                	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
	                			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G118")+"\n\n\r");
	                }
	            }
        	}
        }

        boolean sigue = true;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {
            s = conn.prepareStatement("MCtipoCultivoRepartosCultivosJoinCultivos");
            s.setString(1,bi.getIdBienInmueble().getIdBienInmueble());
            r = s.executeQuery();
            while(r.next() && sigue)
            {
                String tipoSueloRepartido = r.getString("naturaleza_suelo");
                if(!bi.getClaseBienInmueble().equalsIgnoreCase(tipoSueloRepartido))
                {
                    sigue=false;
                }
            }
            if(sigue)
            {
                s = conn.prepareStatement("MCtipoUCRepartosConstrucionesJoinUC");
                s.setString(1,bi.getIdBienInmueble().getIdBienInmueble());
                r = s.executeQuery();
                while(r.next() && sigue)
                {
                    String tipoUC = r.getString("clase_unidad");
                    if(!bi.getClaseBienInmueble().equalsIgnoreCase(tipoUC))
                    {
                        sigue=false;
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
        if(!sigue)
        {
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
        			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G118")+"\n\n\r");
        }
        return sbVal;
    }

    private StringBuffer validaDomicilio(Connection conn, BienInmuebleCatastro bi) throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
        if(bi.getClaseBienInmueble().equalsIgnoreCase(urbana) && (bi.getDomicilioTributario().getCodigoVia()<=0
            || (bi.getDomicilioTributario().getNombreVia()==null|| bi.getDomicilioTributario().getNombreVia().equalsIgnoreCase(""))
            ||(bi.getDomicilioTributario().getTipoVia()==null || bi.getDomicilioTributario().getTipoVia().equalsIgnoreCase(""))))
        {
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
        			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G119")+"\n\n\r");
        }
        if(bi.getClaseBienInmueble().equalsIgnoreCase(rustica) && (bi.getDomicilioTributario().getNombreParaje()==null
        || bi.getDomicilioTributario().getNombreParaje().equalsIgnoreCase("")))
        {
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
        			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G119")+"\n\n\r");
        }
        if(bi.getDomicilioTributario().getCodigoVia()>0
            && (bi.getDomicilioTributario().getNombreVia()!=null&& !bi.getDomicilioTributario().getNombreVia().equalsIgnoreCase(""))
            &&(bi.getDomicilioTributario().getTipoVia()!=null && !bi.getDomicilioTributario().getTipoVia().equalsIgnoreCase("")))
        {
            PreparedStatement s = null;
            ResultSet r = null;
            try
            {
                s = conn.prepareStatement("MCobtenerIDViaCorrecta");
                s.setString(1, bi.getDomicilioTributario().getTipoVia().toUpperCase());
                s.setString(2, bi.getDomicilioTributario().getNombreVia().toUpperCase());
                r = s.executeQuery();
                if(r.next())
                {
                    if(r.getInt("codigocatastro")!=bi.getDomicilioTributario().getCodigoVia())
                    {
                    	if(sbVal == null){
                    		sbVal = new StringBuffer();
                    	}
                    	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
                    			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G6")+"\n\n\r");
                    }
                }
                else
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
                			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G6")+"\n\n\r");
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
        }
        return sbVal;
    }

    private StringBuffer validaTipoPropiedadYCoeficientePropiedad(BienInmuebleCatastro bi)
    {
    	StringBuffer sbVal = null;
    	
        if(bi.getDatosEconomicosBien().getIndTipoPropiedad()!=null 
        		&&!bi.getDatosEconomicosBien().getIndTipoPropiedad().equalsIgnoreCase("")
        		&&!bi.getDatosEconomicosBien().getIndTipoPropiedad().equalsIgnoreCase("T") 
        		&& bi.getDatosEconomicosBien().getIndTipoPropiedad().equalsIgnoreCase("S")
        		&& bi.getDatosEconomicosBien().getIndTipoPropiedad().equalsIgnoreCase("N")
        		&& bi.getDatosEconomicosBien().getIndTipoPropiedad().equalsIgnoreCase("C") 
        		&& bi.getDatosEconomicosBien().getIndTipoPropiedad().equalsIgnoreCase("I")
        		&& bi.getDatosEconomicosBien().getIndTipoPropiedad().equalsIgnoreCase("V") 
        		&& bi.getDatosEconomicosBien().getIndTipoPropiedad().equalsIgnoreCase("B"))
        {
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
        			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G121")+"\n\n\r");
        }
        if((!bi.getClaseBienInmueble().equalsIgnoreCase(rustica) || (bi.getClaseBienInmueble().equalsIgnoreCase(rustica) &&
                compruebaConstruccionRustica(bi)))&&bi.getDatosEconomicosBien().getCoefParticipacion()==null)
        {
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
        			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G122")+"\n\n\r");
        }
        if(bi.getDatosEconomicosBien().getCoefParticipacion()!=null
                && (bi.getDatosEconomicosBien().getCoefParticipacion().floatValue()<(float)0 ||
                bi.getDatosEconomicosBien().getCoefParticipacion().floatValue()>(float)100))
        {
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
        			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G123")+"\n\n\r");
        }
        return sbVal;
    }

    private boolean compruebaConstruccionRustica(BienInmuebleCatastro bi)
    {
        ArrayList listaCons = bi.getLstConstrucciones();
        for(int i = 0; i< listaCons.size();i++ )
        {
            ConstruccionCatastro cons = (ConstruccionCatastro)listaCons.get(i);
            String codUc = cons.getDatosFisicos().getCodUnidadConstructiva();
            if(codUc!=null)
            {
                for(int j = 0; j< listaUCFinca.size(); j++)
                {
                    UnidadConstructivaCatastro uc = (UnidadConstructivaCatastro)listaUCFinca.get(j);
                    if(uc.getCodUnidadConstructiva()!=null && codUc.equalsIgnoreCase(uc.getCodUnidadConstructiva())
                            && uc.getTipoUnidad().equalsIgnoreCase(rustica))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private StringBuffer validaPrecioAdministrativoVenta(Connection conn, BienInmuebleCatastro bi) throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
    	
        Calendar aux= new GregorianCalendar();
        aux.setTime(new Date(System.currentTimeMillis()));
        int anno = aux.get(Calendar.YEAR);
        if(bi.getDatosEconomicosBien().getPrecioVenta()!=null 
                && bi.getDatosEconomicosBien().getPrecioVenta().doubleValue()>0 && (bi.getDatosEconomicosBien().getAnioFinValoracion()==null
                ||bi.getDatosEconomicosBien().getAnioFinValoracion().intValue()<=0))
        {
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
        			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G124")+"\n\n\r");
        }
        if(bi.getClaseBienInmueble().equalsIgnoreCase(bice) && bi.getDatosEconomicosBien().getAnioFinValoracion()!=null
                && bi.getDatosEconomicosBien().getAnioFinValoracion().intValue()>=anno &&
                bi.getDatosEconomicosBien().getPrecioVenta()!=null&& bi.getDatosEconomicosBien().getPrecioVenta().doubleValue()>0)
        {
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
        			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G5")+"\n\n\r");
        }
        if(incompatibilidadPrecioPonencia(conn,bi))
        {
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
        			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J7")+"\n\n\r");
        }
        if(bi.getClaseBienInmueble().equalsIgnoreCase(rustica) && bi.getLstCultivos()!=null && bi.getLstCultivos().size()>0
                &&bi.getDatosEconomicosBien().getPrecioVenta()!=null&& bi.getDatosEconomicosBien().getPrecioVenta().doubleValue()>0
                &&bi.getDatosEconomicosBien().getAnioFinValoracion()!=null
                && bi.getDatosEconomicosBien().getAnioFinValoracion().intValue()>=anno)
        {
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
        			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G126")+"\n\n\r");
        }
        return sbVal;
    }

    private boolean incompatibilidadPrecioPonencia(Connection conn, BienInmuebleCatastro bi) throws DataExceptionCatastro
    {
        boolean resultado = false;
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {
            s = conn.prepareStatement("MCobtenerAnnoDeNormasPonencia");
            s.setInt(1, annoAprobacionPonencia);
            s.setString(2, idMunicipio);
            r = s.executeQuery();
            annoNormas = -1;
            if(r.next())
            {
                annoNormas = r.getInt("anno_normas");
            }
            if(annoNormas<1989&& bi.getDatosEconomicosBien().getPrecioVenta()!=null &&
                    bi.getDatosEconomicosBien().getPrecioVenta().doubleValue()!=0)
            {
                resultado = true;
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

    private StringBuffer validaValorBaseYFormulaCalculo(Connection conn,BienInmuebleCatastro bi, DatosValoracion datosVal) throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
    	
        //Todo si no se rellena antes el obejto hay que ir a bd para coger el valor base y la procedencia de valor base.
        int annoRevisionTotal =getAnnoRevisionTotal(conn);
      /*  if(annoRevisionTotal>2003 &&(bi.getDatosEconomicosBien().getValorBase()!=null && bi.getDatosEconomicosBien().getValorBase().floatValue()!=0))
        {
            return "Error.J15";
        }*/
        if(bi.getDatosBaseLiquidable().getProcedenciaValorBase()!=null&&
                bi.getDatosBaseLiquidable().getProcedenciaValorBase().equalsIgnoreCase("I") &&
                (bi.getDatosBaseLiquidable().getValorBase()==null || bi.getDatosBaseLiquidable().getValorBase().floatValue()==(float)0))
        {
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
        			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J8")+"\n\n\r");
        }
        if(bi.getClaseBienInmueble().equalsIgnoreCase(bice)&&annoRevisionTotal>2003&&(bi.getDatosBaseLiquidable().getValorBase()!=null &&
                bi.getDatosBaseLiquidable().getValorBase().floatValue()>(float)0))
        {
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
        			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J9")+"\n\n\r"); 
        }
        if (datosVal.getTipoRelacionEntreCargosLocales() != null && datosVal.getCodigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo()!= null){
	        if(datosVal.isEsCorrectaFormaCalculo() && datosVal.getTipoRelacionEntreCargosLocales().equalsIgnoreCase("11")&&
	                !(datosVal.getCodigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo().equalsIgnoreCase("S UE LO")&&
	                bi.getDatosEconomicosBien().getIndTipoPropiedad().equalsIgnoreCase("S")) &&
	                !(datosVal.getCodigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo().equalsIgnoreCase("V UE LO")&&
	                bi.getDatosEconomicosBien().getIndTipoPropiedad().equalsIgnoreCase("V")))
	        {
	            if(bi.getLstConstrucciones()==null || bi.getLstConstrucciones().size()==0)
	            {
	            	if(sbVal == null){
	            		sbVal = new StringBuffer();
	            	}
	            	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
	            			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G127")+"\n\n\r");
	            }
	        }
        }
        return sbVal;
    }

    private StringBuffer validaDependientesFormulaCalculo(Connection conn, ArrayList listaBI,DatosValoracion datosVal) throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
        if(datosVal.isEsCorrectaFormaCalculo())
        {
            if(datosVal.getCodigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo()!=null &&
                    datosVal.getCodigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo().equalsIgnoreCase("S UE LO"))
            {
                float sumaCoefPropiedadCargosSuelo = 0;
                float sumaCoefPropietariosConstrucciones = 0;
                boolean hayCargosSuelo = false;
                if (listaBI != null){
	                for(int i =0; i<listaBI.size();i++)
	                {
	                	BienInmuebleJuridico bij = (BienInmuebleJuridico)listaBI.get(i);
	                	BienInmuebleCatastro bi = bij.getBienInmueble();
	                    if(bi.getDatosEconomicosBien().getIndTipoPropiedad().equalsIgnoreCase("S"))
	                    {
	                        sumaCoefPropiedadCargosSuelo = sumaCoefPropiedadCargosSuelo+ bi.getDatosEconomicosBien()
	                                .getCoefParticipacion().floatValue();
	                        hayCargosSuelo = true;
	                    }
	                    if(bi.getLstConstrucciones()!=null && bi.getLstConstrucciones().size()>0)
	                    {
	                        sumaCoefPropietariosConstrucciones = sumaCoefPropietariosConstrucciones+
	                                bi.getDatosEconomicosBien().getCoefParticipacion().floatValue();
	                    }
	                }
                }
                if(!hayCargosSuelo)
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("ValidacionMensajesError","Error.G133")+"\n\n\r");
                }
                else if(datosVal.getCoeficientePropiedadCargosDeSuelo()!=null &&
                        datosVal.getCoeficientePropiedadCargosDeSuelo().floatValue()>0 &&sumaCoefPropiedadCargosSuelo<=0)
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("ValidacionMensajesError","Error.G134")+"\n\n\r");
                }
                else if(datosVal.getCoeficientePropiedadCargosConstruccion()!=null &&
                        datosVal.getCoeficientePropiedadCargosConstruccion().floatValue()>0 && sumaCoefPropietariosConstrucciones<=0)
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("ValidacionMensajesError","Error.G135")+"\n\n\r");
                }
            }
            if(datosVal.getCodigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo()!=null &&
                    datosVal.getCodigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo().equalsIgnoreCase("V UE LO"))
            {
                float sumaCoefPropiedadCargosVuelo = 0;
                float sumaCoefPropietariosConstrucciones = 0;
                boolean hayCargosSuelo = false;
                for(int i =0; i<listaBI.size();i++)
                {
                	BienInmuebleJuridico bij = (BienInmuebleJuridico)listaBI.get(i);
                	BienInmuebleCatastro bi = bij.getBienInmueble();
                    if(bi.getDatosEconomicosBien().getIndTipoPropiedad().equalsIgnoreCase("V"))
                    {
                        sumaCoefPropiedadCargosVuelo = sumaCoefPropiedadCargosVuelo+ bi.getDatosEconomicosBien()
                                .getCoefParticipacion().floatValue();
                        hayCargosSuelo = true;
                    }
                    if(bi.getLstConstrucciones()!=null && bi.getLstConstrucciones().size()>0)
                    {
                        sumaCoefPropietariosConstrucciones = sumaCoefPropietariosConstrucciones+
                                bi.getDatosEconomicosBien().getCoefParticipacion().floatValue();
                    }
                }
                if(!hayCargosSuelo)
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("ValidacionMensajesError","Error.G136")+"\n\n\r");
                }
                else if(datosVal.getCoeficientePropiedadCargosDeSuelo()!=null &&
                        datosVal.getCoeficientePropiedadCargosDeSuelo().floatValue()>0 &&sumaCoefPropiedadCargosVuelo<=0)
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("ValidacionMensajesError","Error.G137")+"\n\n\r");
                }
                else if(datosVal.getCoeficientePropiedadCargosConstruccion()!=null &&
                        datosVal.getCoeficientePropiedadCargosConstruccion().floatValue()>0 && sumaCoefPropietariosConstrucciones<=0)
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("ValidacionMensajesError","Error.G135")+"\n\n\r");
                }
            }
            if(datosVal.getTipoRelacionEntreCargosLocales()==null||datosVal.getTipoRelacionEntreCargosLocales().equalsIgnoreCase("N1") ||
                    datosVal.getTipoRelacionEntreCargosLocales().equalsIgnoreCase(""))
            {
                if(datosVal.getCodigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo()!=null &&
                        datosVal.getCodigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo().equalsIgnoreCase("S UE LO"))
                {
                    int numCargoPropietariosConsSinSuelo=0;
                    for(int i =0; i<listaBI.size();i++)
                    {
	                	BienInmuebleJuridico bij = (BienInmuebleJuridico)listaBI.get(i);
	                	BienInmuebleCatastro bi = bij.getBienInmueble();
                        if(!bi.getDatosEconomicosBien().getIndTipoPropiedad().equalsIgnoreCase("S") &&
                                bi.getLstConstrucciones()!=null && bi.getLstConstrucciones().size()>0)
                        {
                            numCargoPropietariosConsSinSuelo = numCargoPropietariosConsSinSuelo +1;
                        }
                    }
                    if(numCargoPropietariosConsSinSuelo>1)
                    {
                    	if(sbVal == null){
                    		sbVal = new StringBuffer();
                    	}
                    	sbVal.append(I18N.get("ValidacionMensajesError","Error.G138")+"\n\n\r");
                    }
                }
                if(datosVal.getCodigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo()!=null &&
                        datosVal.getCodigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo().equalsIgnoreCase("V UE LO"))
                {
                    int numCargoPropietariosConsSinVuelo=0;
                    for(int i =0; i<listaBI.size();i++)
                    {
	                	BienInmuebleJuridico bij = (BienInmuebleJuridico)listaBI.get(i);
	                	BienInmuebleCatastro bi = bij.getBienInmueble();
                        if(!bi.getDatosEconomicosBien().getIndTipoPropiedad().equalsIgnoreCase("V") &&
                                bi.getLstConstrucciones()!=null && bi.getLstConstrucciones().size()>0)
                        {
                            numCargoPropietariosConsSinVuelo = numCargoPropietariosConsSinVuelo +1;
                        }
                    }
                    if(numCargoPropietariosConsSinVuelo>1)
                    {
                    	if(sbVal == null){
                    		sbVal = new StringBuffer();
                    	}
                    	sbVal.append(I18N.get("ValidacionMensajesError","Error.G138")+"\n\n\r");
                    }
                }
                if(datosVal.getCodigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo()==null ||
                        datosVal.getCodigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo().equalsIgnoreCase(""))
                {
                    int numCargoUrbBice=0;
                    int numCargoRus= 0;
                    for(int i =0; i<listaBI.size();i++)
                    {
                        BienInmuebleJuridico bij = (BienInmuebleJuridico)listaBI.get(i);
                        BienInmuebleCatastro bi = bij.getBienInmueble();
                        if(bi.getClaseBienInmueble().equalsIgnoreCase(rustica))
                        {
                            numCargoRus = numCargoRus+1;
                        }
                        if(bi.getClaseBienInmueble().equalsIgnoreCase(urbana) ||
                                bi.getClaseBienInmueble().equalsIgnoreCase(bice))
                        {
                            numCargoUrbBice = numCargoUrbBice +1;
                        }
                    }
                    if(numCargoRus>1 || numCargoUrbBice>1)
                    {
                    	if(sbVal == null){
                    		sbVal = new StringBuffer();
                    	}
                    	sbVal.append(I18N.get("ValidacionMensajesError","Error.G139")+"\n\n\r");
                    }
                }
            }
            if(datosVal.getCodigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo()==null ||
                    datosVal.getCodigoEscaleraPlantaPuertaQueDiferenciaACargosDeSuelo().equalsIgnoreCase(""))
            {
                float sumaCoefPropiedadCargos = 0;
                for(int i =0; i<listaBI.size();i++)
                {
                    BienInmuebleJuridico bij = (BienInmuebleJuridico)listaBI.get(i);
                    BienInmuebleCatastro bi = bij.getBienInmueble();
                    if( bi.getDatosEconomicosBien().getCoefParticipacion() == null){
                    	 sumaCoefPropiedadCargos = sumaCoefPropiedadCargos + 0;
                    }
                    else{
                    	 sumaCoefPropiedadCargos = sumaCoefPropiedadCargos+ bi.getDatosEconomicosBien()
                         .getCoefParticipacion().floatValue();
                    }

                }
                if(!(sumaCoefPropiedadCargos>=98&&sumaCoefPropiedadCargos<=102))
                {
                    boolean esConsPorFases = false;
                    if(listaConstruccionesFinca != null){
                        for(int i= 0; i<listaConstruccionesFinca.size();i++)
                        {
                            ConstruccionCatastro cons = (ConstruccionCatastro)listaConstruccionesFinca.get(i);
                            if(cons.getDatosEconomicos().getTipoConstruccion().equalsIgnoreCase("00000"))
                            {
                                esConsPorFases = true;
                            }
                        }
                    }

                    int annoRevisionTotal =getAnnoRevisionTotal(conn);

                    if(!esConsPorFases && datosVal.getCoeficientePropiedadCargosConstruccion() !=null && (datosVal.getCoeficientePropiedadCargosConstruccion().floatValue()>0 ||
                            finca.getDatosEconomicos().getIndModalidadReparto().equalsIgnoreCase("3")||
                            (annoRevisionTotal>2005&&(datosVal.getMetodoCalculoValorSuelo().equalsIgnoreCase("")||
                                    datosVal.getMetodoCalculoValorSuelo().equalsIgnoreCase("MX"))&&datosVal.getFormaDeCalculo().floatValue()>=(float)40)
                            || esRepartoPorCoeficientesPropiedad()))
                    {
                    	if(sbVal == null){
                    		sbVal = new StringBuffer();
                    	}
                    	sbVal.append(I18N.get("ValidacionMensajesError","Error.G141")+"\n\n\r");
                    }
                }
            }
        }
        return sbVal;
    }

    private int getAnnoRevisionTotal(Connection conn)  throws DataExceptionCatastro
    {
        PreparedStatement s = null;
        ResultSet r = null;
        int anno = -1;
        try
        {
            s = conn.prepareStatement("MCobtenerAnnoEfectosTotalPonencia");
            s.setInt(1, annoAprobacionPonencia);
            s.setString(2, idMunicipio);
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

    private StringBuffer validaUsosBI(Connection conn,ArrayList listaBI, DatosValoracion datosVal)throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
        for(int i = 0;i< listaBI.size();i++)
        {
            BienInmuebleJuridico bij = (BienInmuebleJuridico) listaBI.get(i);
            BienInmuebleCatastro bi = bij.getBienInmueble();
            if((!esCorrectaPonencia||(bi.getClaseBienInmueble().equalsIgnoreCase(rustica)&&
                   finca.getDatosFisicos().getSupTotal().longValue()<=(finca.getDatosFisicos().getSupFinca().longValue()/4)))&&
                   !bi.getDatosEconomicosBien().getUso().equalsIgnoreCase("Z"))
            {

            	if(sbVal == null){
	        		sbVal = new StringBuffer();
	        	}
	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
            			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J10")+"\n\n\r");
            }
            if(bi.getClaseBienInmueble().equalsIgnoreCase(rustica)&&(bi.getLstConstrucciones()==null ||
                    bi.getLstConstrucciones().size()<=0)&&!bi.getDatosEconomicosBien().getUso().equalsIgnoreCase("Z"))
            {

            	if(sbVal == null){
	        		sbVal = new StringBuffer();
	        	}
	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
            			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J11")+"\n\n\r");

            }
            if(((datosVal.getFormaDeCalculo()!=null 
            		&& datosVal.getFormaDeCalculo().floatValue()>=(float)10 
            		&& datosVal.getFormaDeCalculo().floatValue()<(float)20) 
            		|| esRuinosa())
                    &&!bi.getDatosEconomicosBien().getUso().equalsIgnoreCase("M"))
            {

            	if(sbVal == null){
	        		sbVal = new StringBuffer();
	        	}
	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
            			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J12")+"\n\n\r");
            }
            
            if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
	            if((datosVal.getFormaDeCalculo()!=null 
	            		&& datosVal.getFormaDeCalculo().floatValue()>=(float)30 
	            		&& datosVal.getFormaDeCalculo().floatValue()<(float)40))
	            {
	                String uso = calculaUsoCargoPropiedadVertical(conn,bi);
	                if(uso==null)
	                {

	                	if(sbVal == null){
        	        		sbVal = new StringBuffer();
        	        	}
        	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
                    			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J16")+"\n\n\r");
	                }
	                if(bi.getClaseBienInmueble().equalsIgnoreCase(rustica) && (uso.equalsIgnoreCase("A")||
	                        uso.equalsIgnoreCase("I")))
	                {

	                	if(sbVal == null){
        	        		sbVal = new StringBuffer();
        	        	}
        	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
                    			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J18")+"\n\n\r");
	                }
	                if(bi.getClaseBienInmueble().equalsIgnoreCase(urbana) && (uso.equalsIgnoreCase("B")||
	                        uso.equalsIgnoreCase("J")))
	                {

	                	if(sbVal == null){
        	        		sbVal = new StringBuffer();
        	        	}
        	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
                    			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J19")+"\n\n\r");
	                }
	                bi.getDatosEconomicosBien().setUso(uso);
	            }
	            if((datosVal.getFormaDeCalculo()!=null 
	            		&& datosVal.getFormaDeCalculo().floatValue()>(float)41 
	            		&& datosVal.getFormaDeCalculo().floatValue()<(float)50))
	            {
	                String uso = calculaUsoCargoPropiedadHorizontal(conn,bi);
	                if(uso==null)
	                {
	                	if(sbVal == null){
        	        		sbVal = new StringBuffer();
        	        	}
        	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
                    			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J17")+"\n\n\r");
	                }
	                if(bi.getClaseBienInmueble().equalsIgnoreCase(rustica) && (uso.equalsIgnoreCase("A")||
	                        uso.equalsIgnoreCase("I")))
	                {

	                	if(sbVal == null){
        	        		sbVal = new StringBuffer();
        	        	}
        	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
                    			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J18")+"\n\n\r");
	                }
	                if(bi.getClaseBienInmueble().equalsIgnoreCase(urbana) && (uso.equalsIgnoreCase("B")||
	                        uso.equalsIgnoreCase("J")))
	                {

	                	if(sbVal == null){
        	        		sbVal = new StringBuffer();
        	        	}
        	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
                    			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J19")+"\n\n\r");
	                }
	                bi.getDatosEconomicosBien().setUso(uso);
	            }
            }
            if((datosVal.getFormaDeCalculo()!=null 
            		&& datosVal.getFormaDeCalculo().floatValue()>=(float)50 
            		&& datosVal.getFormaDeCalculo().floatValue()<(float)60) 
            		&& bi.getDatosEconomicosBien().getIndTipoPropiedad().equalsIgnoreCase("S") 
            		&& !bi.getDatosEconomicosBien().getUso().equalsIgnoreCase("M"))
            {

            	if(sbVal == null){
	        		sbVal = new StringBuffer();
	        	}
	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
            			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J13")+"\n\n\r");
            }
            if((datosVal.getFormaDeCalculo()!=null 
            		&& datosVal.getFormaDeCalculo().floatValue()>=(float)50 
            		&& datosVal.getFormaDeCalculo().floatValue()<(float)60) 
            		&& !bi.getDatosEconomicosBien().getIndTipoPropiedad().equalsIgnoreCase("S"))
            {
                String uso = calculaUsoCargoPropiedadHorizontal(conn,bi);
                if(uso==null)
                {
                	if(sbVal == null){
    	        		sbVal = new StringBuffer();
    	        	}
    	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
                			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J17")+"\n\n\r");
                }
                if(bi.getClaseBienInmueble().equalsIgnoreCase(rustica) && (uso.equalsIgnoreCase("A")||
                        uso.equalsIgnoreCase("I")))
                {
                	if(sbVal == null){
    	        		sbVal = new StringBuffer();
    	        	}
    	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
                			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J18")+"\n\n\r");
                }
                if(bi.getClaseBienInmueble().equalsIgnoreCase(urbana) && (uso.equalsIgnoreCase("B")||
                        uso.equalsIgnoreCase("J")))
                {
                	if(sbVal == null){
    	        		sbVal = new StringBuffer();
    	        	}
    	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
                			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J19")+"\n\n\r");
                }
                bi.getDatosEconomicosBien().setUso(uso);
            }
            if((datosVal.getFormaDeCalculo()!=null 
            		&& datosVal.getFormaDeCalculo().floatValue()>=(float)60 
            		&& datosVal.getFormaDeCalculo().floatValue()<(float)70) 
            		&& bi.getDatosEconomicosBien().getIndTipoPropiedad().equalsIgnoreCase("V") 
            		&& !bi.getDatosEconomicosBien().getUso().equalsIgnoreCase("M"))
            {
            	if(sbVal == null){
	        		sbVal = new StringBuffer();
	        	}
	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
            			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J14")+"\n\n\r");
            }
            if((datosVal.getFormaDeCalculo()!=null 
            		&& datosVal.getFormaDeCalculo().floatValue()>=(float)60 
            		&& datosVal.getFormaDeCalculo().floatValue()<(float)70) 
            		&& !bi.getDatosEconomicosBien().getIndTipoPropiedad().equalsIgnoreCase("V"))
            {
                String uso = calculaUsoCargoPropiedadHorizontal(conn,bi);
                if(uso==null)
                {
                	if(sbVal == null){
    	        		sbVal = new StringBuffer();
    	        	}
    	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
                			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J17")+"\n\n\r");
                }
                if(bi.getClaseBienInmueble().equalsIgnoreCase(rustica) && (uso.equalsIgnoreCase("A")||
                        uso.equalsIgnoreCase("I")))
                {
                	if(sbVal == null){
    	        		sbVal = new StringBuffer();
    	        	}
    	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
                			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J18")+"\n\n\r");
                }
                if(bi.getClaseBienInmueble().equalsIgnoreCase(urbana) && (uso.equalsIgnoreCase("B")||
                        uso.equalsIgnoreCase("J")))
                {
                	if(sbVal == null){
    	        		sbVal = new StringBuffer();
    	        	}
    	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.bien") +"- "+
                			bi.getIdBienInmueble().getIdBienInmueble()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J19")+"\n\n\r");
                }                
                bi.getDatosEconomicosBien().setUso(uso);
            }
        }
        return sbVal;
    }

    private boolean esRuinosa()
    {
        float sumaSuperficiesTotalesConst= 0;
        if(listaConstruccionesFinca!=null)
        {
            for(int i =0;i<listaConstruccionesFinca.size();i++)
            {
                ConstruccionCatastro cons = (ConstruccionCatastro)listaConstruccionesFinca.get(i);
                boolean encontrado = false;
                UnidadConstructivaCatastro uc=null;
                for(int j=0; j<listaUCFinca.size() && !encontrado;j++)
                {
                    uc = (UnidadConstructivaCatastro)listaUCFinca.get(j);
                    if(cons.getDatosFisicos().getCodUnidadConstructiva().equalsIgnoreCase(uc.getCodUnidadConstructiva()))
                    {
                        if(!uc.getDatosEconomicos().getCorrectorConservacion().equalsIgnoreCase("O"))
                        {
                            sumaSuperficiesTotalesConst = sumaSuperficiesTotalesConst + cons.getDatosFisicos().getSupTotal().longValue();
                        }
                    }
                }
            }
            return sumaSuperficiesTotalesConst==0;
        }
        return false;
    }

    public void calculaClaseListaBI()
    {
        for(int i = 0; i<finca.getLstBienesInmuebles().size();i++)
        {
            //BienInmuebleCatastro bi = (BienInmuebleCatastro)finca.getLstBienesInmuebles().get(i);
        	BienInmuebleCatastro bi = ((BienInmuebleJuridico)finca.getLstBienesInmuebles().get(i)).getBienInmueble();
            asignaCultivosBI(bi, listaCultivos);
            asignaConstruccionesBI(bi);
            asignaUCAsociadasConsCargo(bi);
            determinaClaseBI(bi);            
        }
    }

    private boolean esRepartoPorCoeficientesPropiedad()
    {
        for(int i =0; i<listaConstruccionesFinca.size();i++)
        {
            ConstruccionCatastro cons = (ConstruccionCatastro)listaConstruccionesFinca.get(i);
            if(cons.getDatosEconomicos().getCodModalidadReparto().endsWith("3"))
            {
                return true;
            }
        }
        return false;
    }

    private String calculaUsoCargoPropiedadVertical(Connection conn,BienInmuebleCatastro bi) throws DataExceptionCatastro
    {
        String uso = usoSegunTipologiaIgual(conn,bi);
        if(uso!=null)
        {
            return uso;
        }
        if(superficieResidencial(conn,bi))
        {
            return "V";
        }
        uso = calculaUsoPorSuperficieMayor(conn,bi);
        if(uso!=null)
        {
            return uso;
        }
        return null;
    }

    private String usoSegunTipologiaIgual(Connection conn, BienInmuebleCatastro bi) throws DataExceptionCatastro
    {
        String usoSegunTipologia = null;
        try
        {
            for(int i =0;i<listaConstruccionesFinca.size();i++)
            {
                String codigoUsoActual= "";
                ConstruccionCatastro cons = (ConstruccionCatastro)listaConstruccionesFinca.get(i);
                if(cons.getDatosEconomicos().getTipoConstruccion()!=null &&!cons.getDatosEconomicos().getTipoConstruccion()
                        .startsWith("1032")&&!cons.getDatosEconomicos().getTipoConstruccion().startsWith("1035")
                        &&!cons.getDatosEconomicos().getTipoConstruccion().equalsIgnoreCase(""))
                {

                    codigoUsoActual = bi.getDatosEconomicosBien().getUso();
                    if(usoSegunTipologia==null)
                    {
                        usoSegunTipologia = calculaUsoSegunTipologiaVertical(codigoUsoActual, cons, bi);
                    }
                    else if(!usoSegunTipologia.equalsIgnoreCase(calculaUsoSegunTipologiaVertical(codigoUsoActual, cons, bi)))
                    {
                        return null;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            throw new DataExceptionCatastro(ex);
        }
        finally
        {
        }
        return usoSegunTipologia;
    }

    private String calculaUsoSegunTipologiaVertical(String codigoUso, ConstruccionCatastro cons, BienInmuebleCatastro bi)
    {
        if(codigoUso==null)
        {
            return bi.getDatosEconomicosBien().getUso();
        }
        if(codigoUso.equalsIgnoreCase("CA"))
        {
            if(cons.getDatosFisicos().getCodDestino().startsWith("C"))
            {
                return "C";
            }
            else
            {
                return "A";
            }
        }
        if(codigoUso.equalsIgnoreCase("AV"))
        {
            return "A";
        }
        if(codigoUso.equalsIgnoreCase("CO"))
        {
            return "O";
        }
        if(codigoUso.equalsIgnoreCase("TC"))
        {
            return "T";
        }
        if(codigoUso.equalsIgnoreCase("GC"))
        {
            return "G";
        }
        else
        {
            return codigoUso;
        }
    }

    private boolean superficieResidencial(Connection conn, BienInmuebleCatastro bi) throws DataExceptionCatastro
    {
        float sumaSuperConstruccionesResidenciales = 0;
        float sumaSuperficiesConstruccionesNoResidenciales = 0;
        float sumaSuperficieTotal = 0;
        float sumaSuperficieGarajes = 0;
        try
        {
            for(int i =0;i<listaConstruccionesFinca.size();i++)
            {
                String codigoUsoActual= "";
                ConstruccionCatastro cons = (ConstruccionCatastro)listaConstruccionesFinca.get(i);
                if(cons.getDatosEconomicos().getTipoConstruccion()!=null &&!cons.getDatosEconomicos().getTipoConstruccion()
                        .startsWith("1032")&&!cons.getDatosEconomicos().getTipoConstruccion().startsWith("1035")
                        &&!cons.getDatosEconomicos().getTipoConstruccion().equalsIgnoreCase(""))
                {
                    codigoUsoActual = bi.getDatosEconomicosBien().getUso();
                    String usoCorrespondienteTipologia = calculaUsoSegunTipologiaVertical(codigoUsoActual, cons, bi);
                    if(usoCorrespondienteTipologia.equalsIgnoreCase("V"))
                    {
                        sumaSuperConstruccionesResidenciales = sumaSuperConstruccionesResidenciales +
                                cons.getDatosFisicos().getSupTotal().longValue();        
                    }
                    else if(!usoCorrespondienteTipologia.equalsIgnoreCase("A"))
                    {
                        sumaSuperficiesConstruccionesNoResidenciales = sumaSuperficiesConstruccionesNoResidenciales +
                                cons.getDatosFisicos().getSupTotal().longValue();
                    }
                    if(cons.getDatosEconomicos().getTipoConstruccion().startsWith("0221") ||
                            cons.getDatosEconomicos().getTipoConstruccion().startsWith("0222"))
                    {
                        sumaSuperficieGarajes = sumaSuperficieGarajes + cons.getDatosFisicos().getSupTotal().longValue();
                    }
                    sumaSuperficieTotal = sumaSuperficieTotal + cons.getDatosFisicos().getSupTotal().longValue();
                }

            }
            if(!(sumaSuperficiesConstruccionesNoResidenciales>(sumaSuperficieTotal/10)))
            {
                sumaSuperficiesConstruccionesNoResidenciales = sumaSuperficiesConstruccionesNoResidenciales +
                        sumaSuperficieGarajes;
            }
            return sumaSuperConstruccionesResidenciales> (0.2*sumaSuperficiesConstruccionesNoResidenciales);
        }
        catch (Exception ex)
        {
            throw new DataExceptionCatastro(ex);
        }
        finally
        {
        }
    }

    private String calculaUsoPorSuperficieMayor(Connection conn, BienInmuebleCatastro bi) throws DataExceptionCatastro
    {
        float superficiesPorUsos[] = new float[0];
        ArrayList usos = new ArrayList();
        if(annoNormas<=0)
        {
            calculaAnnoNormas(conn);
        }
        try
        {
            for(int i =0;i<listaConstruccionesFinca.size();i++)
            {
                String codigoUsoActual= "";
                ConstruccionCatastro cons = (ConstruccionCatastro)listaConstruccionesFinca.get(i);
                if(cons.getDatosEconomicos().getTipoConstruccion()!=null &&!cons.getDatosEconomicos().getTipoConstruccion()
                        .startsWith("1032")&&!cons.getDatosEconomicos().getTipoConstruccion().startsWith("1035")
                        &&!cons.getDatosEconomicos().getTipoConstruccion().equalsIgnoreCase("") &&
                        !cons.getDatosEconomicos().getTipoConstruccion().startsWith("0221") &&
                        !cons.getDatosEconomicos().getTipoConstruccion().startsWith("0222") &&
                        ((annoNormas<1989 &&(!cons.getDatosEconomicos().getTipoConstruccion().startsWith("1033") ||
                        !cons.getDatosEconomicos().getTipoConstruccion().startsWith("1034")))||(annoNormas<1986&&
                        (!!cons.getDatosEconomicos().getTipoConstruccion().startsWith("0133") ||
                        !cons.getDatosEconomicos().getTipoConstruccion().startsWith("0123")))))
                {
                    codigoUsoActual = bi.getDatosEconomicosBien().getUso();
                    String usoCorrespondienteTipologia = calculaUsoSegunTipologiaVertical(codigoUsoActual, cons, bi);
                    boolean continua = true;
                    for(int j=0;j<usos.size() && continua; j++)
                    {
                        if(((String)usos.get(j)).equalsIgnoreCase(usoCorrespondienteTipologia))
                        {
                            superficiesPorUsos[j] = superficiesPorUsos[j] + cons.getDatosFisicos().
                                    getSupTotal().longValue();
                            continua = false;
                        }
                    }
                    //Todo esto probar no se si funcionara.
                    if(continua)
                    {
                        usos.add(usoCorrespondienteTipologia);
                        float temp[]= superficiesPorUsos;
                        superficiesPorUsos = new float[usos.size()];
                        System.arraycopy(temp,0,superficiesPorUsos,0,temp.length);
                        superficiesPorUsos[superficiesPorUsos.length-1]= cons.getDatosFisicos().getSupTotal().longValue();
                    }
                }
            }
            return calculaMayorUso(usos,superficiesPorUsos);
        }
        catch (Exception ex)
        {
            throw new DataExceptionCatastro(ex);
        }
        finally
        {
        }        
    }

    private String calculaMayorUso(ArrayList usos,float[] superficiesPorUsos)
    {
        int posMayor =0;
        float mayor = Float.MIN_VALUE;
        ArrayList ordenUsos = new ArrayList();
        ordenUsos.add("V");
        ordenUsos.add("O");
        ordenUsos.add("C");
        ordenUsos.add("T");
        ordenUsos.add("G");
        ordenUsos.add("I");
        ordenUsos.add("J");
        ordenUsos.add("A");
        ordenUsos.add("B");
        ordenUsos.add("Y");
        ordenUsos.add("Z");
        ordenUsos.add("K");
        ordenUsos.add("E");
        ordenUsos.add("R");
        ordenUsos.add("P");
        ordenUsos.add("M");
        for(int i=0; i<usos.size();i++)
        {
            if(superficiesPorUsos[i]>mayor)
            {
                posMayor = i;
                mayor = superficiesPorUsos[i];
            }
            else if(superficiesPorUsos[i]==mayor)
            {
                int ordenGuardado = ordenUsos.indexOf(usos.get(posMayor));
                int ordenNuevo = ordenUsos.indexOf(usos.get(i));
                if(ordenNuevo!=-1 && ordenNuevo<ordenGuardado)
                {
                    posMayor = i;
                    mayor = superficiesPorUsos[i];
                }
            }
        }
        return (String)usos.get(posMayor);
    }

    private void calculaAnnoNormas(Connection conn) throws DataExceptionCatastro
    {
        PreparedStatement s = null;
        ResultSet r = null;
        try
        {
            s = conn.prepareStatement("MCobtenerAnnoDeNormasPonencia");
            s.setInt(1, annoAprobacionPonencia);
            s.setString(2, idMunicipio);
            r = s.executeQuery();
            annoNormas = -1;
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
    }

    private String calculaUsoCargoPropiedadHorizontal(Connection conn, BienInmuebleCatastro bi) throws DataExceptionCatastro
    {
        ArrayList usos = new ArrayList();
        try
        {
            for(int i =0;i<bi.getLstConstrucciones().size();i++)
            {
                String codigoUsoActual= "";
                ConstruccionCatastro cons = (ConstruccionCatastro)bi.getLstConstrucciones().get(i);
                if(cons.getDatosEconomicos().getTipoConstruccion()!=null &&!cons.getDatosEconomicos().getTipoConstruccion()
                        .startsWith("1032")&&!cons.getDatosEconomicos().getTipoConstruccion().startsWith("1035")
                        &&!cons.getDatosEconomicos().getTipoConstruccion().equalsIgnoreCase("") && !esUCRuinosa(cons))
                {
                    codigoUsoActual = bi.getDatosEconomicosBien().getUso();
                    String usoCorrespondienteTipologia = calculaUsoSegunTipologiaHorizontal(codigoUsoActual, cons, bi);
                    boolean continua = true;
                    for(int j=0;j<usos.size() && continua; j++)
                    {
                        if(((String)usos.get(j)).equalsIgnoreCase(usoCorrespondienteTipologia))
                        {
                            continua = false;
                        }
                    }
                    if(continua)
                    {
                        usos.add(usoCorrespondienteTipologia);
                    }
                }
            }
            return calculaMayorUso(usos);
        }
        catch (Exception ex)
        {
            throw new DataExceptionCatastro(ex);
        }
        finally
        {
        }
    }

    private boolean esUCRuinosa(ConstruccionCatastro cons)
    {
        for(int i = 0;i<listaUCAsociadasCargo.size();i++)
        {
            UnidadConstructivaCatastro uc = (UnidadConstructivaCatastro)listaUCFinca.get(i);
            if(cons.getDatosFisicos().getCodUnidadConstructiva().equalsIgnoreCase(uc.getCodUnidadConstructiva()))
            {
                if(uc.getDatosEconomicos().getCorrectorConservacion().equalsIgnoreCase("O"))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private String calculaUsoSegunTipologiaHorizontal(String codigoUso, ConstruccionCatastro cons, BienInmuebleCatastro bi)
    {
        if(codigoUso==null)
        {
            return bi.getDatosEconomicosBien().getUso();
        }
        if(codigoUso.equalsIgnoreCase("CA"))
        {
            if(cons.getDatosFisicos().getCodDestino().equalsIgnoreCase("AAP") ||
                    cons.getDatosFisicos().getCodDestino().equalsIgnoreCase("AAL"))
            {
                return "A";
            }
            else
            {
                if(bi.getDatosEconomicosBien().getUso().equalsIgnoreCase("C"))
                {
                    return "C";
                }
                if(bi.getDatosEconomicosBien().getUso().equalsIgnoreCase("A"))
                {
                    return "A";
                }
                else
                {
                    if(cons.getDatosFisicos().getCodDestino().startsWith("C"))
                    {
                        return "C";
                    }
                    else
                    {
                        return "A";
                    }
                }
            }
        }
        if(codigoUso.equalsIgnoreCase("AV"))
        {
            return "A";
        }
        if(codigoUso.equalsIgnoreCase("CO") || codigoUso.equalsIgnoreCase("TC") || codigoUso.equalsIgnoreCase("GC"))
        {
            return "C";
        }
        else
        {
            return codigoUso;
        }
    }

    private String calculaMayorUso(ArrayList usos)
    {
        int posMenor =Integer.MAX_VALUE;
        ArrayList ordenUsos = new ArrayList();
        ordenUsos.add("V");
        ordenUsos.add("O");
        ordenUsos.add("C");
        ordenUsos.add("T");
        ordenUsos.add("G");
        ordenUsos.add("I");
        ordenUsos.add("J");
        ordenUsos.add("A");
        ordenUsos.add("B");
        ordenUsos.add("Y");
        ordenUsos.add("Z");
        ordenUsos.add("K");
        ordenUsos.add("E");
        ordenUsos.add("R");
        ordenUsos.add("P");
        ordenUsos.add("M");
        for(int i=0; i<usos.size();i++)
        {
            int ordenNuevo = ordenUsos.indexOf(usos.get(i));
            if(ordenNuevo!=-1 && ordenNuevo<posMenor)
            {
                posMenor = i;
            }
        }
        return (String)usos.get(posMenor);
    }
}
