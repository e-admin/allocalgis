/**
 * ValidacionRepartos.java
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
import java.util.List;

import com.geopista.app.catastro.intercambio.exception.DataExceptionCatastro;
import com.geopista.app.catastro.model.beans.*;
import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosConstruccion;
import com.geopista.app.catastro.model.datos.fisicos.DatosFisicosConstruccion;
import com.geopista.util.TypeUtil;
import com.geopista.server.database.CPoolDatabase;
import com.vividsolutions.jump.I18N;

public class ValidacionRepartos {
	
	/**
	 * Método que comprueba si el elemento que se quiere repartir existe, tanto si es un cultivo como
	 * una construccion.
	 * 
	 * @param conn Conexión a la BBDD
	 * @return
	 * @throws Exception
	 */
	private boolean existeElementoACompartir(Connection conn, String refCatas, String numOrden, boolean esConstruccion,
                                             String codSubParcela, String califCultivo) throws Exception
	{
		String sSQL= null;
		//String id = null;
		if (esConstruccion)
		{
			sSQL = "MCgetLocalACompartir";
			//id = ((ConstruccionCatastro)elemRepartido).getIdConstruccion();
		    //String sSQL= "select * from construccion where identificador=?;
		}
		else
		{
			sSQL = "MCgetCultivoACompartir";
			//id = ((Cultivo)elemRepartido).getIdCultivo().getIdCultivo();
			//String sSQL= "select * from cultivos where identificador=?;
		}
	     PreparedStatement ps= null;
	     ResultSet rs= null;

	     try
	        {
	            ps= conn.prepareStatement(sSQL);
                if(esConstruccion)
                {
                    ps.setString(1, refCatas);
                    ps.setString(2, numOrden);
                }
                else
                {
                    ps.setString(1, refCatas);
                    ps.setString(2, codSubParcela);
                    ps.setString(3, califCultivo);
                }
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
	 * Se comprueba que la construcción sobre la que va a recaer el reparto exista.
	 * @param conn Conexión a la BBDD.
	 * @param idConstruccionDestino Identificador de la construcción sobre la que recae el reparto.
	 * @return
	 * @throws Exception
	 */
	private ConstruccionCatastro existeConstruccionReparto(Connection conn, String refCatas, String numOrden,
                                                                  boolean esDestino, String idConstruccionDestino) throws Exception
	{
		String sSQL= null;
        if(!esDestino)
        {
            sSQL = "MCgetLocalACompartir";
        }
        else
        {
            sSQL = "MCgetLocalDestinoReparto";
        }
        //String sSQL= "select * from construccion where identificador=?;
		ConstruccionCatastro construccionDestino = null;
		
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
                if(!esDestino)
                {
                    ps.setString(1, refCatas);
                    ps.setString(2, numOrden);
                }
                else
                {
                    ps.setString(1, idConstruccionDestino);
                }

	            rs= ps.executeQuery();
	            if(rs.next())
	            {
	            	construccionDestino = new ConstruccionCatastro();
	            	construccionDestino.setNumOrdenConstruccion(rs.getString("numero_orden_construccion"));
		    	 	//PONER EL DE LA FINCA  construccionDestino.setCodDelegacionMEH();

		            DireccionLocalizacion dir = new DireccionLocalizacion();
		            dir.setBloque(rs.getString("bloque"));
		            dir.setEscalera(rs.getString("escalera"));
		            dir.setPlanta(rs.getString("planta"));
		            dir.setPuerta(rs.getString("puerta"));
		            construccionDestino.setDomicilioTributario(dir);
		            
		            DatosFisicosConstruccion datosFisicosConstruccion = new DatosFisicosConstruccion();
		            datosFisicosConstruccion.setAnioAntiguedad(TypeUtil.getInteger(rs, "anno_antiguedad"));
		            datosFisicosConstruccion.setCodUnidadConstructiva(rs.getString("codigo_unidadconstructiva"));
		            datosFisicosConstruccion.setCodDestino(rs.getString("codigo_destino_dgc"));
		            datosFisicosConstruccion.setAnioReforma(TypeUtil.getInteger(rs,"anno_reforma"));
		            datosFisicosConstruccion.setTipoReforma(rs.getString("indicador_tipo_reforma"));
		            datosFisicosConstruccion.setLocalInterior(new Boolean(rs.getBoolean("indicador_local_interior")));
		            datosFisicosConstruccion.setSupTotal(TypeUtil.getLong(rs, "superficie_total_local"));
		            datosFisicosConstruccion.setSupTerrazasLocal(TypeUtil.getLong(rs,"superficie_terrazas_local"));
		            datosFisicosConstruccion.setSupImputableLocal(TypeUtil.getLong(rs,"superficie_imputable_local"));
		            construccionDestino.setDatosFisicos(datosFisicosConstruccion);
		            
		            DatosEconomicosConstruccion datosEconomicosConstruccion =  new DatosEconomicosConstruccion();
		            datosEconomicosConstruccion.setCodCategoriaPredominante(rs.getString("codigo_categoria_predominante"));
		            datosEconomicosConstruccion.setCodModalidadReparto(rs.getString("codigo_modalidad_reparto"));
		            datosEconomicosConstruccion.setCodTipoValor(rs.getString("codigo_tipo_valor"));
		            datosEconomicosConstruccion.setCodUsoPredominante(rs.getString("codigo_uso_predominante"));

                    if(CPoolDatabase.isPostgres(conn))
                        datosEconomicosConstruccion.setCorrectorApreciacion(TypeUtil.getFloat(rs,"corrector_apreciacion_economica"));
                    else
                        datosEconomicosConstruccion.setCorrectorApreciacion(TypeUtil.getFloat(rs,"corrector_apreciacion_econom"));

                    datosEconomicosConstruccion.setCorrectorVivienda(new Boolean(rs.getBoolean("corrector_vivienda")));
		            datosEconomicosConstruccion.setTipoConstruccion(rs.getString("tipologia_constructiva"));
		            construccionDestino.setDatosEconomicos(datosEconomicosConstruccion);
		     		            
		            //construccionDestino.setTipoMovimiento(rs.getString("tipo_movimiento"));
	            	
	            	construccionDestino.setIdConstruccion(rs.getString("identificador"));

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
		return construccionDestino;
	}
	
	
	/**
	 * Se comprueba que exista el BienInmueble sobre el que va a recaer el reparto.
	 * @param conn Conexión a la BBDD.
	 * @param idCargoDestino Identificador del BienInmueble sobre el que recae le reparto.
	 * @return
	 * @throws Exception
	 */
	private boolean existeCargoRepartoDestino(Connection conn, String idCargoDestino) throws Exception
	{
		String sSQL= "MCgetCargoDestino";
		//String sSQL= "select * from bien_inmueble where identificador=?;
		
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setString(1, idCargoDestino);
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
	 * Se comprueba que la construcción sobre la que recae el reparto no sea de tipo común y que 
	 * la clase de la unidad constructiva a la que pertenece el local destino tiene que ser la misma 
	 * que la de la unidad constructiva a la que pertenece el local común (a repartir). 
	 * @param conn Conexión a la BBDD
	 * @return
	 * @throws Exception
	 */
	private StringBuffer construccionDestinoComun(Connection conn, RepartoCatastro reparto, FincaCatastro finca) throws Exception
	{
		StringBuffer sbVal = null;
		
		 List lstLocales = finca.getLstConstrucciones();
		 List lstUC = finca.getLstUnidadesConstructivas();
		 List lstLocalesReparto = reparto.getLstLocales();
		 String codUnidadConstructivaReparto = "";
		 String codUnidadConstructiva = "";
		 String clUnidadConstructiva = "";
		 String clUnidadConstructivaReparto = "";
		 int m = lstLocalesReparto.size();
	     for(int i=0;i<m; i++)
         {
             String idLocalReparto = ((ElementoReparto)lstLocalesReparto.get(i)).getNumCargo();
             if (idLocalReparto == null){
            	 if (((ElementoReparto)lstLocalesReparto.get(i)).getId() != null){
            		 idLocalReparto = ((ElementoReparto)lstLocalesReparto.get(i)).getId().substring(((ElementoReparto)lstLocalesReparto.get(i)).getId().length()-4, ((ElementoReparto)lstLocalesReparto.get(i)).getId().length());
            	 }
             }
    		 int n = lstLocales.size();
    	     for(int j=0;j< n;j++)
             {
    	    	 ConstruccionCatastro construccion = (ConstruccionCatastro)lstLocales.get(j);
                 String idConstruccion = construccion.getNumOrdenConstruccion();
                 if (idLocalReparto != null && idLocalReparto.equals(idConstruccion)){
                	 if (codUnidadConstructivaReparto.equals(""))
                		 codUnidadConstructivaReparto = construccion.getDatosFisicos().getCodUnidadConstructiva();
                	 if (construccion.getLstRepartos().size()>0){
                		 if(sbVal == null){
                     		sbVal = new StringBuffer();
                     	}
                     	sbVal.append(I18N.get("ValidacionMensajesError","Error.G153")+"\n\n\r");
                	 }
                 }
                 if (idConstruccion.equals(reparto.getNumOrdenConsRepartir())){
                	 if (codUnidadConstructiva.equals(""))
                		 codUnidadConstructiva = construccion.getDatosFisicos().getCodUnidadConstructiva();
                 }
             }
    		 n = lstUC.size();
    	     for(int j=0;j<n;j++)
             {    	
    	    	 UnidadConstructivaCatastro uc = (UnidadConstructivaCatastro)lstUC.get(j);
                 if (codUnidadConstructivaReparto.equals(uc.getCodUnidadConstructiva()))
                	 if (clUnidadConstructivaReparto.equals(""))
                		 clUnidadConstructivaReparto = uc.getTipoUnidad();
                 if (codUnidadConstructiva.equals(uc.getCodUnidadConstructiva()))
                	 if (clUnidadConstructiva.equals(""))
                		 clUnidadConstructiva = uc.getTipoUnidad();
             }
        	 if (!clUnidadConstructivaReparto.equals(clUnidadConstructiva)){
        		 if(sbVal == null){
             		sbVal = new StringBuffer();
             	}
             	sbVal.append(I18N.get("ValidacionMensajesError","Error.G154")+"\n\n\r");
        	 }
         }
         return sbVal;
	}
	
	
	/**
	 * Método que comprueba que la construcción a repartir no se reparta más de una vez al mismo cargo.
	 * @param lstRepartos Lista con los repartos.
	 * @param construccionARepartir Construcción a repartir.
	 * @param idDestino identificador del cargo sobre el que recae el local.
	 * @return
	 */
/*	private boolean existeMismoCargoDestinoLocal(Connection conn,ArrayList lstRepartos, ConstruccionCatastro construccionARepartir, String idDestino)
	{
		int contadorCargos = 0;
		for (int i=0;i<lstRepartos.size();i++)
		{
			RepartoCatastro reparto = (RepartoCatastro) lstRepartos.get(i);
			if (reparto.getIdOrigen()!=null && reparto.getNumOrdenConsRepartir()!=null)
			{
				ConstruccionCatastro construccion = null;
                try
                {
                    construccion = (ConstruccionCatastro) existeConstruccionReparto(conn,
                            reparto.getIdOrigen().getRefCatastral(), reparto.getNumOrdenConsRepartir(), false, null);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
				if (construccion !=null &&construccion.getIdConstruccion().equalsIgnoreCase(construccionARepartir.getIdConstruccion()))
				{
					//El origen es el mismo
					//Comprobamos el destino
                    if(reparto.getLstLocales()!=null)
                    {
                        for(int j=0;j<reparto.getLstLocales().size();j++)
                        {
                            ElementoReparto elemRep =(ElementoReparto) reparto.getLstLocales().get(j);
                            if (elemRep.getId().equalsIgnoreCase(idDestino))
                                contadorCargos += 1;
                        }
                    }
                }
			}
		}
		if (contadorCargos > 1)
			return true;
		
		return false;
	}*/
	
	
	/**
	 * Se comprueba que el destino del reparto, no sea el mismo que el destino de otro reparto del mismo local.
	 * @param lstRepartos Lista con los repartos.
	 * @param construccionARepartir Construcción a repartir
	 * @param idDestino
	 * @return
	 */
/*	private boolean existeMismoCargoDestinoCargo(Connection conn,ArrayList lstRepartos, ConstruccionCatastro construccionARepartir, String idDestino)
	{
		int contadorCargos = 0;
		for (int i=0;i<lstRepartos.size();i++)
		{
			RepartoCatastro reparto = (RepartoCatastro) lstRepartos.get(i);
			if (reparto.getIdOrigen()!=null && reparto.getNumOrdenConsRepartir()!=null)
			{
				ConstruccionCatastro construccion = null;
                try
                {
                    construccion = (ConstruccionCatastro) existeConstruccionReparto(conn,
                            reparto.getIdOrigen().getRefCatastral(), reparto.getNumOrdenConsRepartir(), false, null);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
				if (construccion!=null &&construccion.getIdConstruccion()
                        .equalsIgnoreCase(construccionARepartir.getIdConstruccion()))
				{
					//El origen es el mismo
					//Comprobamos el destino
                    if(reparto.getLstBienes()!=null)
                    {
                        for(int j=0; j<reparto.getLstBienes().size();j++)
                        {
                            ElementoReparto elemRep = (ElementoReparto)reparto.getLstBienes().get(j);
                            if (elemRep.getId().equalsIgnoreCase(idDestino))
                                contadorCargos += 1;
                        }
                    }
				}
			}
		}
		if (contadorCargos > 1)
			return true;
		
		return false;
	}
	*/
	
	/**
	 * Método que se encarga de las validaciones de los repartos de construcciones.
	 * @param conn Conexión a la BBDD.
	 * @param finca Finca que contiene los repartos.
	 * @param reparto reparto a validar.
	 * @return
	 * @throws DataExceptionCatastro 
	 */
	private StringBuffer validacionRepartoConstrucciones(Connection conn, FincaCatastro finca, RepartoCatastro reparto) throws DataExceptionCatastro
	{
		StringBuffer sbVal = null;
		
		if ((reparto.getTipoReparto() == null)||(reparto.getTipoReparto().length() < 1)){
			if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G149")+"\n\n\r");
		}
		if (reparto.getTipoReparto().startsWith("TC") || (reparto.getTipoReparto().startsWith("TL"))){
			if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G151")+"\n\n\r");
		}

		//Destino
		if ((reparto.getLstBienes()!=null)&&(reparto.getLstBienes().size()>0)
				&&(reparto.getLstLocales()!=null)&&(reparto.getLstLocales().size()>0)){
			if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.M20")+"\n\n\r");
		}
        if(reparto.getTipoReparto().equalsIgnoreCase("AL"))
		{
			//debe indicarse la construcción destino (y no el cargo destino)
			if ((reparto.getLstLocales()==null)||(reparto.getLstLocales().size() < 1)){
				if(sbVal == null){
	        		sbVal = new StringBuffer();
	        	}
	        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G152")+"\n\n\r");
			}
			if ((reparto.getLstBienes()!=null)&&(reparto.getLstBienes().size() > 0)){
				if(sbVal == null){
	        		sbVal = new StringBuffer();
	        	}
	        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G152")+"\n\n\r");
			}

			try
            {
				//Mira si la construcción destino es común y si la clase de la unidad constructiva a la que pertenece el local destino tiene 
	            //que ser la misma que la de la unidad constructiva a la que pertenece el local común 

				StringBuffer sbValConsDestinComun = construccionDestinoComun(conn, reparto,finca);
				if (sbValConsDestinComun != null){
					if(sbVal == null){
		        		sbVal = new StringBuffer();
		        	}
					sbVal.append(sbValConsDestinComun.toString());
				}
			}
            catch (Exception e)
            {
				throw new DataExceptionCatastro(e);
			}
            for(int i=0;i<reparto.getLstLocales().size();i++)
            {
                ElementoReparto elemRep = (ElementoReparto)reparto.getLstLocales().get(i);
                if (existeMismoLocalDestino(conn, elemRep, reparto)){
                	if(sbVal == null){
    	        		sbVal = new StringBuffer();
    	        	}
    	        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G156")+"\n\n\r");
                }
            }
        }

		if (reparto.getTipoReparto().equalsIgnoreCase("AC"))
		{
			//debe indicarse el cargo destino (y no la construcción destino
			if ((reparto.getLstBienes()==null)||(reparto.getLstBienes().size() < 1)){
				
				if(sbVal == null){
	        		sbVal = new StringBuffer();
	        	}
	        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G157")+"\n\n\r");
			}
			if ((reparto.getLstLocales()!=null)&&(reparto.getLstLocales().size() > 0)){
				if(sbVal == null){
	        		sbVal = new StringBuffer();
	        	}
	        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G157")+"\n\n\r");
			}
            try
            {
               //Si es nuevo no tiene por qué existir
            	/*
               for(int i=0;i< reparto.getLstBienes().size();i++)
                {
                    String id = ((ElementoReparto)reparto.getLstBienes().get(i)).getId();
                    if (!existeCargoRepartoDestino(conn, id))
                        return "Error.G159";
                }*/
            }
            catch (Exception e)
            {
                throw new DataExceptionCatastro(e);
            }
        }
		//Coeficiente
		if ((reparto.getTipoReparto().endsWith("AC")) && ((reparto.getPorcentajeReparto() < 0) || (reparto.getPorcentajeReparto() > 100))){
			if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G162")+"\n\n\r");
		}
				
		return sbVal;
	}
	
	
	/**
	 * Comprueba que el cargo destino del reparto no sea el mismo que el destino de otro reparto del mismo cultivo.
	 * @param lstRepartos Lista con los repartos.
	 * @param cultivoARepartir Cultivo a repartir.
	 * @param idDestino identicador del cargo sobre el que recae el reparto.
	 * @return
	 */
	private boolean existeMismoCargoDestino(Connection conn,ElementoReparto destino, RepartoCatastro reparto)
	{
		int contadorCargos = 0;
		int n = reparto.getLstBienes().size();
		for (int i=0;i<n;i++)
		{
	        ElementoReparto elemRep = (ElementoReparto)reparto.getLstBienes().get(i);
            if (elemRep.getId().equalsIgnoreCase(destino.getId()))
            	contadorCargos += 1;
		}
		if (contadorCargos > 1)
			return true;
		
		return false;
	}
	
	/**
	 * Comprueba que el local destino del reparto no sea el mismo que el destino de otro reparto de la misma construcción.
	 */
	private boolean existeMismoLocalDestino(Connection conn,ElementoReparto destino, RepartoCatastro reparto)
	{
		int contadorCargos = 0;
		int n = reparto.getLstLocales().size();
		for (int i=0;i<n;i++)
		{
	        ElementoReparto elemRep = (ElementoReparto)reparto.getLstLocales().get(i);
            if (elemRep.getId().equalsIgnoreCase(destino.getId()))
            	contadorCargos += 1;
		}
		if (contadorCargos > 1)
			return true;
		
		return false;
	}
	
	/**
	 * Método que se encarga de la validación de los repartos de los cultivos.
	 * @param conn Conexión a la BBDD.
	 * @param finca Finca que contiene los repartos y el cultivo.
	 * @param reparto Reparto a validar.
	 * @return
	 * @throws DataExceptionCatastro 
	 */
	private StringBuffer validacionRepartoCultivos(Connection conn, FincaCatastro finca, RepartoCatastro reparto) throws DataExceptionCatastro
	{
		StringBuffer sbVal = null;
		
		//Si lo metemos de nuevas no tiene por qué existir
		/*Cultivo cultivoARepartir = null;
        try
        {
            cultivoARepartir = getCultivoOrigen(conn, reparto.getIdOrigen().getRefCatastral(),
                    reparto.getCodSubparcelaElementoRepartir(), reparto.getCalifCatastralElementoRepartir());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try
        {
			if (!existeElementoACompartir(conn, reparto.getIdOrigen().getRefCatastral(), null, false, reparto.getCodSubparcelaElementoRepartir(), reparto.getCalifCatastralElementoRepartir()))
				return "Error.G160";
		}
        catch (Exception e)
        {
			throw new DataException(e);
		}*/
		if ((reparto.getTipoReparto() == null)||(reparto.getTipoReparto().length() < 1)){
			if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G161")+"\n\n\r");
		}
		if (reparto.getTipoReparto().equalsIgnoreCase("TC")){
			if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G151")+"\n\n\r");
		}
		if (!reparto.getTipoReparto().equalsIgnoreCase("AC")){
			if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G157")+"\n\n\r");
		}
		
		//Destino
		/*try
        {
			//El bien inmueble destino del reparto puede no existir porque se esté dando de alta en el momento
			if (reparto.getLstBienes() == null || reparto.getLstBienes().size()<1)
				return "Error.G159";
            for(int i= 0; i<reparto.getLstBienes().size();i++)
            {
                ElementoReparto elemRep = (ElementoReparto)reparto.getLstBienes().get(i);
                if((!existeCargoRepartoDestino(conn, elemRep.getId())))
                {
                    return "Error.G159";
                }
            }
        }
        catch (Exception e)
        {
			throw new DataException(e);
		}*/

        for(int i=0;i<reparto.getLstBienes().size();i++)
        {
            ElementoReparto elemRep = (ElementoReparto)reparto.getLstBienes().get(i);
            if (existeMismoCargoDestino(conn, elemRep, reparto)){
            	if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("ValidacionMensajesError","Error.G158")+"\n\n\r");
            }
        }

        if ((reparto.getLstLocales() != null)&&(reparto.getLstLocales().size() > 0)){
        	if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G157")+"\n\n\r");
        }
		
		//Coeficiente
		if (reparto.getTipoReparto().endsWith("AC") && (reparto.getPorcentajeReparto() < 0 || reparto.getPorcentajeReparto() > 100)){
			if(sbVal == null){
        		sbVal = new StringBuffer();
        	}
        	sbVal.append(I18N.get("ValidacionMensajesError","Error.G162")+"\n\n\r");
		}
		
		return sbVal;
	}

	private Cultivo getCultivoOrigen(Connection conn, String refCatas, String codSubParcela, String califCultivo) throws Exception
	{
        String sSQL= "MCgetLocalACompartir";
        //String sSQL= "select * from construccion where identificador=?;
        Cultivo cultivo = null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        try
        {
            ps= conn.prepareStatement(sSQL);
            ps.setString(1, refCatas);
            ps.setString(2, codSubParcela);
            ps.setString(3, califCultivo);
            rs= ps.executeQuery();
            if(rs.next())
            {
                cultivo = new Cultivo();
                IdCultivo idCultivo = new IdCultivo();
                idCultivo.setParcelaCatastral(refCatas);
                idCultivo.setNumOrden(rs.getString("numero_orden"));
                idCultivo.setCalifCultivo(rs.getString("calificacion_cultivo"));
                cultivo.setIdCultivo(idCultivo);
                cultivo.setTipoSuelo(rs.getString("naturaleza_suelo"));
                cultivo.setCodBonificacion(rs.getString("bonificacion_subparcela"));
                cultivo.setCodModalidadReparto(rs.getString("modalidad_reparto"));
                cultivo.setCodSubparcela(rs.getString("codigo_subparcela"));
                cultivo.setEjercicioFinBonificacion(new Integer(rs.getInt("ejercicio_finalizacion")));
                cultivo.setIntensidadProductiva(new Integer(rs.getInt("intensidad_productiva")));
                cultivo.setSuperficieParcela(new Long(rs.getLong("superficie_subparcela")));
                cultivo.setTipoSubparcela(rs.getString("tipo_subparcela"));
                cultivo.setTIPO_MOVIMIENTO(rs.getString("tipo_movimiento"));
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
		return cultivo;
	}


    /**
	 * Método que se encarga de la validación de los repartos, tanto de cultivos como de locales.
	 * @param conn Conexión a la BBDD
	 * @param finca Finca que contiene los repartos.
	 * @return
	 * @throws DataExceptionCatastro 
	 */
	public StringBuffer validacionRepartos(Connection conn, FincaCatastro finca, DatosValoracion datosVal) throws DataExceptionCatastro
	{
		StringBuffer sbVal = null;
		ArrayList lstRepartos = finca.getLstReparto();

        if(lstRepartos!=null && lstRepartos.size()>0)
        {
        	if (CriteriosValidacion.existeUrbanaEnFinca(finca) && !datosVal.getIndicadorElementosComunes().equals("S")){
        		if(sbVal == null){
            		sbVal = new StringBuffer();
            	}
            	sbVal.append(I18N.get("ValidacionMensajesError","Error.G148")+"\n\n\r");
        	}
            for (int i=0;i<lstRepartos.size();i++)
            {
                //Para cada reparto
                RepartoCatastro reparto = (RepartoCatastro) lstRepartos.get(i);
                if ((reparto.getNumOrdenConsRepartir()!=null)&&(reparto.getNumOrdenConsRepartir().length() > 0))
                {
                    //El reparto es de construcciones
                	StringBuffer sbValRepartoConst = validacionRepartoConstrucciones(conn, finca, reparto);
                	if(sbValRepartoConst == null){
                		if(sbVal == null){
                    		sbVal = new StringBuffer();
                    	}
                		sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.reparto") +"- "+
                				reparto.getNumOrdenConsRepartir()+":"+ "\r\n" +sbValRepartoConst.toString());
                	}
                }
                if ((reparto.getCodSubparcelaElementoRepartir()!=null)&&(reparto.getCodSubparcelaElementoRepartir().length() > 0)
                        &&(reparto.getCalifCatastralElementoRepartir()!=null) && (reparto.getCalifCatastralElementoRepartir().length() > 0))
                {
                    //El reparto es de cultivos
                	StringBuffer sbValRepartoCultivos = validacionRepartoCultivos(conn, finca, reparto);
                	if(sbValRepartoCultivos == null){
                		if(sbVal == null){
                    		sbVal = new StringBuffer();
                    	}
                		sbVal.append(I18N.get("Expedientes","Catastro.gestionexpediente.validaciones.reparto") +"- "+
                				reparto.getNumOrdenConsRepartir()+":"+ "\r\n" +sbValRepartoCultivos.toString());
                	}
                }
            }
        }
        return sbVal;
	}

}
