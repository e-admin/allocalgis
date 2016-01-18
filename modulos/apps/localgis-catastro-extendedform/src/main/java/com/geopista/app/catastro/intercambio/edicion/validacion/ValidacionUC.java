/**
 * ValidacionUC.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion.validacion;

import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;
import com.geopista.app.catastro.model.beans.SueloCatastro;
import com.geopista.app.catastro.intercambio.exception.DataExceptionCatastro;
import com.vividsolutions.jump.I18N;

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

    public StringBuffer ValidaListaUC(Connection conn, ArrayList listaUC, DatosValoracion datosVal)
            throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
        if(listaUC!=null &&listaUC.size()>0)
        {
            if(esBlancoRelacionCargosYLocales(conn, datosVal))
            {
                int numeroBices = 0;
                int numeroNoBices = 0;
                for(int i = 0; i<listaUC.size(); i++)
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
                   
                    StringBuffer sbValaux =  ValidaUC(conn, uniCo, listaUC);
                    if(sbValaux != null && !sbValaux.equals("")){
                    	sbVal.append(sbValaux.toString());
                    }
                }
                if(numeroBices>0 && numeroNoBices>0)
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("ValidacionMensajesError","Error.G74")+"\n\n\r");
                }
                if(hayRustica && !haySuperficieHuella)
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("ValidacionMensajesError","Error.J3")+"\n\n\r");
                }
            }
            else
            {
            	if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("ValidacionMensajesError","Error.G56")+"\n\n\r");
            }
        }

        return sbVal;
    }

    private boolean esBlancoRelacionCargosYLocales(Connection conn, DatosValoracion datosVal)
            throws DataExceptionCatastro
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
            throw new DataExceptionCatastro(ex);
        }
        finally
        {
            if (s!=null) try{s.close();}catch(Exception e){};
            if (r!= null) try{r.close();}catch(Exception e){};
        }
        return resultado;
    }

    public StringBuffer ValidaUC(Connection conn, UnidadConstructivaCatastro unidadCons, ArrayList lstUC) throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
    	//String resultado = null;
        try
        {
        	StringBuffer sbValIdentificacion = validaDatosIdentificacion(conn, unidadCons, lstUC);
          
            if(!unidadCons.getTipoUnidad().equalsIgnoreCase(rustica))
            {
            	StringBuffer sbValDomicilio = validaDomicilio(conn, unidadCons);
            }
    
            StringBuffer sbValFisicos = validaDatosFisicos(conn, unidadCons);
            StringBuffer sbValEconomicos = validaDatosEconomicos(conn, unidadCons);
           
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new DataExceptionCatastro(e);
        }

        return sbVal;
    }
    
//    public String ValidaUC(Connection conn, UnidadConstructivaCatastro unidadCons) throws DataException
//    {
//        String resultado = null;
//        try
//        {
////            resultado = validaDatosIdentificacion(conn, unidadCons);
////            if(resultado==null)
////            {
//                if(!unidadCons.getTipoUnidad().equalsIgnoreCase(rustica))
//                {
//                    resultado = validaDomicilio(conn, unidadCons);
//                }
//                if(resultado==null)
//                {
//                    resultado = validaDatosFisicos(conn, unidadCons);
//                    if(resultado==null)
//                    {
//                        resultado = validaDatosEconomicos(conn, unidadCons);
//                    }
//                }
////            }
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            throw new DataException(e);
//        }
//
//        return resultado;
//    }

    private StringBuffer validaDatosIdentificacion(Connection conn, UnidadConstructivaCatastro unidadCons, ArrayList lstUC) throws DataExceptionCatastro
    {//Añadimos un segundo parámetro a la consulta, la parcela catastral.
    	StringBuffer sbVal = null;
        String resultado = null;
        if(unidadCons.getCodUnidadConstructiva()!=null && !unidadCons.getCodUnidadConstructiva().equalsIgnoreCase("")
                && !unidadCons.getCodUnidadConstructiva().equalsIgnoreCase("0")
                && unidadCons.getRefParcela().getRefCatastral()!=null 
                && !unidadCons.getRefParcela().getRefCatastral().equalsIgnoreCase(""))
        {
        	
        	if (!unidadCons.getTipoUnidad().equals(urbana)&&!unidadCons.getTipoUnidad().equals(rustica)&&!unidadCons.getTipoUnidad().equals(bice)){
        		if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.uc") +"- "+
            			unidadCons.getCodUnidadConstructiva()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G57")+"\n\n\r");
        	}
        	
        	for (java.util.Iterator iterLstUC = (java.util.Iterator) lstUC.iterator(); iterLstUC.hasNext();){
        		
        		Object object = iterLstUC.next();
        		if (object instanceof UnidadConstructivaCatastro){
        			
        			if ( object != unidadCons && ((UnidadConstructivaCatastro)object).getCodUnidadConstructiva().equals(unidadCons.getCodUnidadConstructiva())){
        				if(sbVal == null){
        	        		sbVal = new StringBuffer();
        	        	}
        	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.uc") +"- "+
                    			unidadCons.getCodUnidadConstructiva()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J1")+"\n\n\r");
        			
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
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.uc") +"- "+
        			unidadCons.getCodUnidadConstructiva()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.J2")+"\n\n\r");
        }

        return sbVal;
    }

    private StringBuffer validaDomicilio(Connection conn, UnidadConstructivaCatastro unidadCons) throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
       // String resultado = null;
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
                    	if(sbVal == null){
                    		sbVal = new StringBuffer();
                    	}
                    	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.uc") +"- "+
                    			unidadCons.getCodUnidadConstructiva()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G6")+"\n\n\r");                         
                    }
                }
                else
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.uc") +"- "+
                			unidadCons.getCodUnidadConstructiva()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G59")+"\n\n\r");
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
        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.uc") +"- "+
        			unidadCons.getCodUnidadConstructiva()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G59")+"\n\n\r");
        }

        return sbVal;
    }

    private StringBuffer validaDatosFisicos(Connection conn, UnidadConstructivaCatastro unidadCons) throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
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
	        	if(sbVal == null){
	        		sbVal = new StringBuffer();
	        	}
	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.uc") +"- "+
            			unidadCons.getCodUnidadConstructiva()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G63")+"\n\n\r");
	        }
	        if (unidadCons.getDatosFisicos().getAnioConstruccion()!=null && unidadCons.getDatosFisicos().getAnioConstruccion().intValue()!=0){
	        	if (unidadCons.getDatosFisicos().getIndExactitud()==null || unidadCons.getDatosFisicos().getIndExactitud().equalsIgnoreCase("")){
	        		if(sbVal == null){
	            		sbVal = new StringBuffer();
	            	}
	            	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.uc") +"- "+
                			unidadCons.getCodUnidadConstructiva()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G64")+"\n\n\r");
	        	}
	        }
	        if(!checkeaNecesarioAnnoConstruccion(conn, unidadCons))
	        {
	        	if(sbVal == null){
	        		sbVal = new StringBuffer();
	        	}
	        	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.uc") +"- "+
            			unidadCons.getCodUnidadConstructiva()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G65")+"\n\n\r");
	        }
	        if (unidadCons.getDatosFisicos().getAnioConstruccion()!=null && unidadCons.getDatosFisicos().getAnioConstruccion().intValue()!=0){
	        	Calendar aux= new GregorianCalendar();
	            aux.setTime(new Date(System.currentTimeMillis()));
	            int anno = aux.get(Calendar.YEAR);
	            if(unidadCons.getDatosFisicos().getAnioConstruccion().intValue()>anno)
	            {
	            	if(sbVal == null){
	            		sbVal = new StringBuffer();
	            	}
	            	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.uc") +"- "+
                			unidadCons.getCodUnidadConstructiva()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G66")+"\n\n\r");
	            }
	        }
        }catch(Exception e)
        {
        	throw new DataExceptionCatastro(e);                    
        }

        return sbVal;
    }

    private boolean checkeaNecesarioAnnoConstruccion(Connection conn, UnidadConstructivaCatastro unidadCons) throws DataExceptionCatastro
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
            throw new DataExceptionCatastro(ex);
        }
        finally
        {
            if (s!=null) try{s.close();}catch(Exception e){};
            if (r!= null) try{r.close();}catch(Exception e){};
        }
        return resultado;
    }

    private StringBuffer validaDatosEconomicos(Connection conn , UnidadConstructivaCatastro unidadCons) throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
    	
        if(unidadCons.getTipoUnidad().equalsIgnoreCase(rustica))
        {
            if(!(unidadCons.getDatosEconomicos().getNumFachadas()==null ||
                    unidadCons.getDatosEconomicos().getNumFachadas().equalsIgnoreCase("N")||
                    unidadCons.getDatosEconomicos().getNumFachadas().equalsIgnoreCase(""))||
                    unidadCons.getDatosEconomicos().isCorrectorLongFachada().booleanValue())
            {
            	if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.uc") +"- "+
            			unidadCons.getCodUnidadConstructiva()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G60")+"\n\n\r");
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
            	if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.uc") +"- "+
            			unidadCons.getCodUnidadConstructiva()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G61")+"\n\n\r");
            }
            if(unidadCons.getDatosEconomicos().isCorrectorLongFachada().booleanValue() &&
                    (unidadCons.getDatosEconomicos().getNumFachadas()==null ||
                    unidadCons.getDatosEconomicos().getNumFachadas().equalsIgnoreCase("") ||
                    unidadCons.getDatosEconomicos().getNumFachadas().equalsIgnoreCase("N") ||
                    Integer.parseInt(unidadCons.getDatosEconomicos().getNumFachadas())<=0))
            {
            	if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.uc") +"- "+
            			unidadCons.getCodUnidadConstructiva()+":"+ "\r\n" +I18N.get("ValidacionMensajesError","Error.G62")+"\n\n\r");
            }
        }
        if(esCorrectaPonencia)
        {
            try
            {
            	StringBuffer sbValPonencia = null;
            	StringBuffer sbValZonaValor = null;
            	StringBuffer sbValCorrectores = null;
                if(!unidadCons.getTipoUnidad().equalsIgnoreCase(rustica)&&annoRevisionTotal<2006)
                {
                	sbValPonencia = checkeaViaPonencia(conn, unidadCons);
                  
                }
                if(!unidadCons.getTipoUnidad().equalsIgnoreCase(rustica)&&annoRevisionTotal>=2006)
                {
                	sbValZonaValor = checkeZonaValor(conn, unidadCons);

                }
                sbValCorrectores = checkeaCorrectores(conn, unidadCons);
                
               
               
                if(sbValPonencia != null){
                	if(sbVal == null){
                 		sbVal = new StringBuffer();
                 	}
                	sbVal.append(sbValPonencia.toString());
                }
                if(sbValZonaValor != null){
                	if(sbVal == null){
                 		sbVal = new StringBuffer();
                 	}
                	sbVal.append(sbValZonaValor.toString());    	
			    }
            	if(sbValCorrectores != null){
            		if(sbVal == null){
                 		sbVal = new StringBuffer();
                 	}
            		sbVal.append(sbValCorrectores.toString());
            	}

            }
            catch(Exception e)
            {
                throw new DataExceptionCatastro(e);
            }
        }
        return sbVal;
    }

    private StringBuffer checkeaViaPonencia(Connection conn,UnidadConstructivaCatastro unidadCons) throws DataExceptionCatastro
    {
       // String resultado = null;
    	StringBuffer sbVal = null;
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
                    	if(sbVal == null){
                    		sbVal = new StringBuffer();
                    	}
                    	sbVal.append(I18N.get("ValidacionMensajesError","Error.G67")+"\n\n\r");
 
                    }
                }
            }
            else
            {
            	if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("ValidacionMensajesError","Error.G68")+"\n\n\r");
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

    private StringBuffer checkeZonaValor(Connection conn,UnidadConstructivaCatastro unidadCons)throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
    	if (unidadCons.getDatosEconomicos().getZonaValor().getCodZonaValor() == null){
    		if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G69")+"\n\n\r");
    	}
    	
       // String resultado = null;
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
	                    	if (suelo.getDatosEconomicos().getZonaValor().getCodZonaValor() == null){
	                    		if(sbVal == null){
	                        		sbVal = new StringBuffer();
	                        	}
	                        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G21")+"\n\n\r");
	                    	}
	                        if(suelo.getDatosEconomicos().getZonaValor().getCodZonaValor().equalsIgnoreCase(
	                                unidadCons.getDatosEconomicos().getZonaValor().getCodZonaValor()))
	                        {
	                            haySueloConZonaValor= true;
	                        }
	                    }
                    }
                    if(!haySueloConZonaValor)
                    {
                    	if(sbVal == null){
                    		sbVal = new StringBuffer();
                    	}
                    	sbVal.append(I18N.get("ValidacionMensajesError","Error.G70")+"\n\n\r");
                    }
                }
            }
            else
            {
            	if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("ValidacionMensajesError","Error.G69")+"\n\n\r");
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

    private boolean haySueloUnitario(Connection conn) throws DataExceptionCatastro
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
                        throw new DataExceptionCatastro(ex);
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

    private StringBuffer checkeaCorrectores(Connection conn,UnidadConstructivaCatastro unidadCons)throws DataExceptionCatastro
    {
      //  String resultado = null;
    	
    	StringBuffer sbVal = null;
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
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("ValidacionMensajesError","Error.G71")+"\n\n\r");
                }
                else if(annoNormas<1986&&(unidadCons.getDatosEconomicos().isCorrectorNoLucrativo()!=null&&
                        unidadCons.getDatosEconomicos().isCorrectorNoLucrativo().booleanValue()))
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("ValidacionMensajesError","Error.G72")+"\n\n\r");
                }
                else if(annoNormas<1989 &&(unidadCons.getDatosEconomicos().getCoefCargasSingulares()!=null
                &&!(unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()==(float)0.7 ||
                unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()==(float)1)))
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("ValidacionMensajesError","Error.G42")+"\n\n\r");
                }
                else if(annoNormas==1989 &&(unidadCons.getDatosEconomicos().getCoefCargasSingulares()!=null
                &&!(unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()>=(float)0.7 &&
                unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()<=(float)1)))
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("ValidacionMensajesError","Error.G43")+"\n\n\r");
                }
                else if(annoNormas>1989 &&(unidadCons.getDatosEconomicos().getCoefCargasSingulares()!=null
                &&!(unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()==(float)0.7 ||
                unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()==(float)0.8 ||
                unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()==(float)0.9 ||
                unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()==(float)1)))
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("ValidacionMensajesError","Error.G44")+"\n\n\r");
                }
                else if((unidadCons.getDatosEconomicos().getCoefCargasSingulares()!=null &&
                        unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()!=(float)1
                        && unidadCons.getDatosEconomicos().isCorrectorSitEspeciales()!=null
                        && unidadCons.getDatosEconomicos().isCorrectorSitEspeciales().booleanValue()))
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("ValidacionMensajesError","Error.G73")+"\n\n\r");
                }
                if((unidadCons.getDatosEconomicos().getCoefCargasSingulares()!=null &&
                        unidadCons.getDatosEconomicos().getCoefCargasSingulares().floatValue()!=(float)1
                        && unidadCons.getDatosEconomicos().isCorrectorSitEspeciales()!=null
                        && unidadCons.getDatosEconomicos().isCorrectorSitEspeciales().booleanValue()))
                {
                	if(sbVal == null){
                		sbVal = new StringBuffer();
                	}
                	sbVal.append(I18N.get("ValidacionMensajesError","Error.G73")+"\n\n\r");
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

    private boolean conservacionCorrecta(Connection conn,UnidadConstructivaCatastro unidadCons, int annoNormas)throws DataExceptionCatastro
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
            throw new DataExceptionCatastro(ex);
        }
        finally
        {
            if (s!=null) try{s.close();}catch(Exception e){};
            if (r!= null) try{r.close();}catch(Exception e){};
        }
        return resultado;
    }
}
