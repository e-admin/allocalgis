/**
 * COperacionesDGCUtil.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.util;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.servicioWebCatastro.beans.CabeceraWSCatastro;
import com.geopista.app.catastro.servicioWebCatastro.beans.IdentificadorDialogo;
import com.geopista.app.catastro.utils.ConstantesCatastro;


public class COperacionesDGCUtil {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(COperacionesDGCUtil.class);
	/**
	 * Metodo que crea una cabecera para la comunicacion con la DGC mediante WS. Recopila los datos y crea el objeto
	 * que sera parseado a xml por el metodo que lo haya llamado.
	 *
	 * @param expedientes Los expediente a exportar.
	 * @return CabeceraWSCatastro El resultado.
	 * @throws Exception
	 * */
	private static CabeceraWSCatastro creaCabeceraWSBD(Expediente exp, String tipo, 
			String nombreSolicitante, String nifSolicitante) throws Exception{
		Date fechaIniPe = null;
		CabeceraWSCatastro cabecera= new CabeceraWSCatastro();


		cabecera.setTipoEntidadGeneradora(exp.getEntidadGeneradora().getTipo());
		cabecera.setCodigoDelegacion(exp.getEntidadGeneradora().getCodigo());
		cabecera.setMunicipioODiputacion(exp.getEntidadGeneradora().getDescripcion());
		cabecera.setNombreEntidadGeneradora(exp.getEntidadGeneradora().getNombre());

		cabecera.setFechaGeneracionFichero(new Date(System.currentTimeMillis()));
		SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss");
		String horaGene = horaFormat.format(new Time(System.currentTimeMillis()));
		cabecera.setHoraGeneracionFichero(horaGene);
		
		if(tipo.equals(ConstantesCatastro.TAG_CREACION_EXPEDIENTE_REQUEST)){
			cabecera.setTipoFichero(CabeceraWSCatastro.TIPO_XML_CREACION_EXPEDIENTE);
			cabecera.setActualizaCatastro(false);
		}
		else if(tipo.equals(ConstantesCatastro.TAG_CONSULTA_CATASTRO_REQUEST)){
			cabecera.setTipoFichero(CabeceraWSCatastro.TIPO_XML_CONSULTA_CATASTRO);
			cabecera.setActualizaCatastro(false);
		}
		else if(tipo.equals(ConstantesCatastro.TAG_ACTUALIZA_CATASTRO_REQUEST)){
			cabecera.setTipoFichero(CabeceraWSCatastro.TIPO_XML_ACTUALIZA_CATASTRO);
			cabecera.setActualizaCatastro(true);
			
			ArrayList lstIdentificadorDialogo = new ArrayList();
			for (int i=0; i<exp.getListaReferencias().size(); i++){
				IdentificadorDialogo identificadorDialogo = new IdentificadorDialogo();
				Object obj = exp.getListaReferencias().get(i);
				if(obj instanceof FincaCatastro){
					FincaCatastro finca = (FincaCatastro) obj;
					identificadorDialogo.setFincaBien(finca);
					identificadorDialogo.setIdentificadorDialogo(finca.getIdentificadorDialogo());
					identificadorDialogo.setPc1(finca.getRefFinca().getRefCatastral1());
					identificadorDialogo.setPc2(finca.getRefFinca().getRefCatastral2());
					identificadorDialogo.setCodigoDelegacion(finca.getCodDelegacionMEH());
					identificadorDialogo.setCodigoMunicipioDGC(finca.getCodMunicipioDGC());
					identificadorDialogo.setBICE(finca.getBICE());
					identificadorDialogo.setBienCatastro(false);
					identificadorDialogo.setFincaCatastro(true);
				}
				else if(obj instanceof BienInmuebleCatastro){
					BienInmuebleCatastro bien = (BienInmuebleCatastro)obj;
					identificadorDialogo.setFincaBien(bien);
					identificadorDialogo.setIdentificadorDialogo(((BienInmuebleCatastro) obj).getIdentificadorDialogo());
					
					identificadorDialogo.setIdBienInmueble(bien.getIdBienInmueble());

					
					identificadorDialogo.setCodigoDelegacion(bien.getDomicilioTributario().getProvinciaINE());
					identificadorDialogo.setCodigoMunicipioDGC("003");
					//identificadorDialogo.setCodigoMunicipioDGC(bien.getCodMunicipioDGC());
					identificadorDialogo.setClaseBienInmueble(bien.getClaseBienInmueble());
					
					identificadorDialogo.setBienCatastro(true);
					identificadorDialogo.setFincaCatastro(false);
				}
				lstIdentificadorDialogo.add(identificadorDialogo);
			}
			cabecera.setLstIdentificadorDialogo(lstIdentificadorDialogo);

		}
		else if(tipo.equals(ConstantesCatastro.TAG_CONSULTA_ESTADO_EXPEDIENTE_REQUEST)){
			cabecera.setTipoFichero(CabeceraWSCatastro.TIPO_XML_CONSULTA_ESTADO_EXPEDIENTE);
			cabecera.setActualizaCatastro(false);
		}

		cabecera.setNifPersona(nifSolicitante);
		cabecera.setNombrePersona(nombreSolicitante);
	
		return cabecera;
	}

	/**
	 * Metodo que crea y devuelve una cabecera de la comunicacion mediante WS con la DGC
	 *
	 * @param oos Objeto donde se escriben las excepciones que seran propagadas.
	 * @param expedientes Los expedientes que se van a exportar.
	 * @return CabeceraWSCatastro La cabecera creada.
	 * @throws Exception
	 */
	public static CabeceraWSCatastro creaCabeceraWS(Expediente expediente, String tipo, String nombreSolicitante, String nifSolicitante) throws Exception
	{
		try
		{
			return creaCabeceraWSBD(expediente, tipo, nombreSolicitante, nifSolicitante);
		}
		catch(Exception e)
		{
			logger.error("creaCabeceraWS: "+ e.getMessage());
			throw e;
		}
	}
}
