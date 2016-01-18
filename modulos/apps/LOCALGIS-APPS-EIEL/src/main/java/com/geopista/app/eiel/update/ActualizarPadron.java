/**
 * ActualizarPadron.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.update;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.InitEIEL;
import com.geopista.app.eiel.beans.AbastecimientoAutonomoEIEL;
import com.geopista.app.eiel.beans.DiseminadosEIEL;
import com.geopista.app.eiel.beans.Encuestados1EIEL;
import com.geopista.app.eiel.beans.Encuestados2EIEL;
import com.geopista.app.eiel.beans.EntidadesAgrupadasEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.InfoPadronEIEL;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.beans.PadronMunicipiosEIEL;
import com.geopista.app.eiel.beans.PoblamientoEIEL;
import com.geopista.app.eiel.beans.RecogidaBasurasEIEL;
import com.geopista.app.eiel.beans.SaneamientoAutonomoEIEL;
import com.geopista.app.eiel.beans.ServiciosAbastecimientosEIEL;
import com.geopista.app.eiel.beans.ServiciosRecogidaBasuraEIEL;
import com.geopista.app.eiel.beans.ServiciosSaneamientoEIEL;
import com.geopista.app.eiel.beans.filter.LCGMunicipioEIEL;
import com.geopista.app.eiel.beans.filter.LCGNucleoEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


public class ActualizarPadron {

	private AppContext aplicacion;
    private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(ActualizarPadron.class);

    private String fixedCodProvincia="33";
    private float PORCENTAJE_POBLACION_ESTACIONAL=(float) 1.1;
    private float PORCENTAJE_CONSUMO_INVIERNO=(float) 0.15;
    private float PORCENTAJE_CONSUMO_VERANO=(float) 0.18;
    private float PORCENTAJE_DEFICITARIAS=(float) 25;
    
    private String añoPadron;
    
    private TaskMonitorDialog progressDialog;
	private boolean actualizar;
	private String path;
	private LCGMunicipioEIEL municipioSeleccionado;

	private HashMap elementosNucleosEncuestados2=new HashMap();
    
	public ActualizarPadron(String añoPadron, LCGMunicipioEIEL municipioSeleccionado,boolean actualizar, String path,TaskMonitorDialog progressDialog){
		
		this.añoPadron=añoPadron;
		this.municipioSeleccionado=municipioSeleccionado;
        this.aplicacion= (AppContext) AppContext.getApplicationContext();
        this.progressDialog=progressDialog;
        progressDialog.setResizable(false);
        this.actualizar=actualizar;
        this.path=path;
        
	}
        
	
	public ResultadoActualizacion actualizar(){
        ResultadoActualizacion resultadoActualizacion;

		LocalGISEIELUtils.storeNucleosAllMunicipios();
		        
		resultadoActualizacion = new ResultadoActualizacion();
		

			
		try {
			String filtroMunicipio="";
			if (municipioSeleccionado.getIdMunicipio()!=0)
				filtroMunicipio=" and (\"CON3\"='"+String.valueOf(municipioSeleccionado.getIdMunicipio()).substring(2,5)+"')";
						
			//044->Oviedo 024->Gijon 004->Aviles
			String filtroMunicipios=filtroMunicipio+" and (\"CON3\"!='044' and \"CON3\"!='024' and \"CON3\"!='004')";
			//String filtroMunicipios=" and (\"CON3\"='001' and \"P\"='09' and \"N/D\"='01' and (\"ES\"='03' or (\"ES\"='08') or (\"ES\"='10')) )";

			actualizar_tabla(actualizar,"(\"Categoría\"='CONCEJO')"+filtroMunicipio,ConstantesLocalGISEIEL.PADRON_MUNICIPIOS,resultadoActualizacion);
			actualizar_tabla(actualizar,"(\"N/D\"!='00')"+filtroMunicipio,ConstantesLocalGISEIEL.POBLAMIENTO,resultadoActualizacion);
			actualizar_tabla(actualizar,"(\"N/D\"!='00')"+filtroMunicipio,ConstantesLocalGISEIEL.ENTIDADES_SINGULARES,resultadoActualizacion);			
			actualizar_tabla(actualizar,"(\"N/D\"!='00')"+filtroMunicipio,ConstantesLocalGISEIEL.C_NUCLEOS_POBLACION,resultadoActualizacion);
						
			actualizar_tabla(actualizar,"(\"N/D\"!='00')"+filtroMunicipio,ConstantesLocalGISEIEL.AGRUPACIONES6000,resultadoActualizacion);
			actualizar_tabla(actualizar,"(\"N/D\"!='00') "+filtroMunicipios,ConstantesLocalGISEIEL.ENCUESTADOS1,resultadoActualizacion);
			
			//Los encuestados2 no se actualizan solo se utilizan para posteriormente actualizar los datos de servicio de abastecimiento autonomo
			actualizar_tabla(actualizar,"(\"N/D\"!='00') "+filtroMunicipios,ConstantesLocalGISEIEL.ENCUESTADOS2,resultadoActualizacion);
	
			
			actualizar_tabla(actualizar,"(\"N/D\"!='00') "+filtroMunicipios,ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS,resultadoActualizacion);				
			actualizar_tabla(actualizar,"(\"N/D\"!='00') "+filtroMunicipios,ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO,resultadoActualizacion);
			
			actualizar_tabla(actualizar,"(\"N/D\"!='00') "+filtroMunicipios,ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO,resultadoActualizacion); 
			actualizar_tabla(actualizar,"(\"N/D\"!='00') "+filtroMunicipios,ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO,resultadoActualizacion);			
			
			actualizar_tabla(actualizar,"(\"N/D\"!='00') "+filtroMunicipios,ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA,resultadoActualizacion);
			
			actualizar_tabla(actualizar,"(\"Categoría\"='CONCEJO')"+filtroMunicipios,ConstantesLocalGISEIEL.DISEMINADOS,resultadoActualizacion);
			
			
			
			showResultados(resultadoActualizacion);
		} catch (PadronException e) {
			resultadoActualizacion.append(getStackTrace(e));
			logger.error("Error al obtener la informacion del Padron",e);
			showResultados(resultadoActualizacion);
			resultadoActualizacion=null;
		}
		catch (Exception e){
			resultadoActualizacion.append(getStackTrace(e));
			logger.error("Error al obtener la informacion del Padron",e);
			showResultados(resultadoActualizacion);
			resultadoActualizacion=null;
			
		}
		
		
	
      
		return resultadoActualizacion;
        
	}

	public static String getStackTrace(Throwable aThrowable) {
	    final Writer result = new StringWriter();
	    final PrintWriter printWriter = new PrintWriter(result);
	    aThrowable.printStackTrace(printWriter);
	    return result.toString();
	  }
	
	
	
	
	/**
	 * Actualizacion de la tabla
	 * @param actualizar
	 * @param filtro
	 * @param tipo
	 * @param resultadoActualizacion
	 * @return
	 */
	private boolean actualizar_tabla(boolean actualizar,String filtro,String tipo,ResultadoActualizacion resultadoActualizacion) throws PadronException{
		
		HashMap hElementosActualizar=new HashMap();
		HashMap hElementosActualizarIndexados=new HashMap();
		HashMap hElementosSobrantesIndexados=new HashMap();
		
		HashMap indexData=null;
		

		//Obtenemos los elementos de padron a actualizar.
		String tabla="padron.nomenclator_"+añoPadron;
		ArrayList elementosPadron=getListPadron(tipo,filtro,tabla,resultadoActualizacion);
		if (elementosPadron==null)
			return false;
		
		String cadena="Actualizando:"+tipo;
		logger.info(cadena);
		resultadoActualizacion.append(cadena+"\n");
		resultadoActualizacion.append("----------------\n");
		if (progressDialog!=null)
			progressDialog.report(I18N.get("LocalGISUpdatePadron", "update.padron.actualizando")+":"+tipo);  
	
		HashMap elementos_eiel_t_rb=null;
		if (tipo==ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA)
			elementos_eiel_t_rb=getList_eiel_t_rb(filtro);
		
		
		for (int i=0;i<elementosPadron.size();i++){
			
			if (i % 1000==0){
				logger.info ("Tipo:"+tipo+" Actualizando elemento:"+i+"/"+elementosPadron.size());
			}
			
			InfoPadronEIEL infoPadron=(InfoPadronEIEL)elementosPadron.get(i);
						
			String codMunicipio=fixedCodProvincia+infoPadron.getCON3();
			String codEntidad=infoPadron.getP()+infoPadron.getES();
			String codPoblamiento=infoPadron.getND();
    		if (codPoblamiento.equals("99"))
    			codPoblamiento="70";
    		
    		//Clave de busqueda.
    		String claveBusqueda=codEntidad+codPoblamiento;    		
    		if (tipo.equals(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS))
    			claveBusqueda=codMunicipio;
    		else if (tipo.equals(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES))
    			claveBusqueda=codEntidad;
    		else if (tipo.equals(ConstantesLocalGISEIEL.DISEMINADOS))
    			claveBusqueda=codMunicipio;
			
	    	try {
	    		
	    		//Obtenemos los elementos a actualizar.
	    		indexData=getElementosActualizar(hElementosActualizar,hElementosActualizarIndexados,hElementosSobrantesIndexados,
	    											tipo,fixedCodProvincia,codMunicipio);
	    		
	    		//Para las Agrupaciones 6000 guardamos los elementos que hay actualmente
	    		//para luego descontarlos de la tabla eiel_t_nucl_encuest_1
	    		if (tipo.equals(ConstantesLocalGISEIEL.AGRUPACIONES6000)){
    				resultadoActualizacion.addEntidadesAgrupadas(codMunicipio,indexData);
    			}
	    		
	    		
	    		//Buscamos si el elemento está entre los elementos a actualizar , si esta lo eliminamos
	    		//marcandolo como procesado.
	    		Object obj=null;
	    		if (indexData.get(claveBusqueda)!=null){	    			
	    			obj = indexData.get(claveBusqueda);
	    			
	    			HashMap indexDataBorrar=(HashMap)hElementosSobrantesIndexados.get(codMunicipio);
	    			if (indexDataBorrar!=null)
	    				indexDataBorrar.remove(claveBusqueda);
	    		}
	
    			if (tipo.equals(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS)){
    				actualizar_eiel_t_padron_ttmm(actualizar,tipo,infoPadron,obj,resultadoActualizacion);
    			}
    			else if (tipo.equals(ConstantesLocalGISEIEL.POBLAMIENTO)){
    				actualizar_eiel_t_poblamiento(actualizar,tipo, infoPadron, obj, resultadoActualizacion);
    			}
    			else if (tipo.equals(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES)){
    				actualizar_eiel_t_entidad_singular(actualizar,tipo, infoPadron, obj, resultadoActualizacion);
    			}
    			else if (tipo.equals(ConstantesLocalGISEIEL.C_NUCLEOS_POBLACION)){
    				actualizar_eiel_c_nucleo_poblacion(actualizar,tipo, infoPadron, obj, resultadoActualizacion);
    			}	    
    			else if (tipo.equals(ConstantesLocalGISEIEL.AGRUPACIONES6000)){
    				actualizar_eiel_t_entidades_agrupadas(actualizar,tipo, infoPadron, obj,indexData,claveBusqueda,resultadoActualizacion);
    			}	
    			else if (tipo.equals(ConstantesLocalGISEIEL.ENCUESTADOS1)){
    				actualizar_eiel_t_nucl_encuest_1(actualizar,tipo, infoPadron, obj, resultadoActualizacion);
    			}
    			else if (tipo.equals(ConstantesLocalGISEIEL.ENCUESTADOS2)){
    				//Guardamos los datos de Nucleos Encuestados2 que
    				//se utilizan para el abastecimiento autonomo. Incidencia 193
    				elementosNucleosEncuestados2.put(codMunicipio+codEntidad+codPoblamiento,obj);	
    			}
    			else if (tipo.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS)){
    				actualizar_eiel_t_abast_serv(actualizar,tipo, infoPadron, obj, resultadoActualizacion);
    			}
    			else if (tipo.equals(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO)){
    				actualizar_eiel_t_abast_au(actualizar,tipo, infoPadron, obj, resultadoActualizacion);
    			}
    			else if (tipo.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO)){
    				actualizar_eiel_t_saneam_serv(actualizar,tipo, infoPadron, obj, resultadoActualizacion);
    			}
    			else if (tipo.equals(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO)){
    				actualizar_eiel_t_saneam_au(actualizar,tipo, infoPadron, obj, resultadoActualizacion);
    			}
    			else if (tipo.equals(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA)){
    				actualizar_eiel_t_rb_serv(actualizar,tipo, infoPadron, obj,elementos_eiel_t_rb, resultadoActualizacion);
    			}	
    			else if (tipo.equals(ConstantesLocalGISEIEL.DISEMINADOS)){
    				actualizar_eiel_t_mun_diseminados(actualizar,tipo, infoPadron, obj, resultadoActualizacion);
    			}	
    			
									
	    	}
	    	catch (Exception e){
	    		logger.error("Error",e);
	    		return false;
	    	}			
		}
		
		
		//Revisamos los elementos que quedan pendientes. de procesar porque estaban en LOCALGIS pero no
		//en la Base de Datos del Nomenclator. En principio el numero de elementos debe de ser pequeño.		
		int elementos=0;
		Iterator it = hElementosSobrantesIndexados.entrySet().iterator();	
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();			
			HashMap hm=(HashMap)e.getValue();
			if (hm.size()>0)
				elementos+=hm.size();
			Iterator it2 = hm.entrySet().iterator();	
			while (it2.hasNext()) {
				Map.Entry e1 = (Map.Entry)it2.next();
				Object obj=(Object)e1.getValue();
				//logger.info("Valor de obj:"+obj);			
				
				String claveBusqueda=null;    		
				
				if (tipo.equals(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS)){
    				actualizar_eiel_t_padron_ttmm(actualizar,tipo,null,obj,resultadoActualizacion);
    			}
    			else if (tipo.equals(ConstantesLocalGISEIEL.POBLAMIENTO)){
    				actualizar_eiel_t_poblamiento(actualizar,tipo, null, obj, resultadoActualizacion);
    			}
    			else if (tipo.equals(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES)){
    				actualizar_eiel_t_entidad_singular(actualizar,tipo, null, obj, resultadoActualizacion);
    			}
    			else if (tipo.equals(ConstantesLocalGISEIEL.C_NUCLEOS_POBLACION)){
    				actualizar_eiel_c_nucleo_poblacion(actualizar,tipo, null, obj, resultadoActualizacion);
    			}	    
    			else if (tipo.equals(ConstantesLocalGISEIEL.AGRUPACIONES6000)){
    				actualizar_eiel_t_entidades_agrupadas(actualizar,tipo, null, obj,indexData,claveBusqueda, resultadoActualizacion);
    			}	
    			else if (tipo.equals(ConstantesLocalGISEIEL.ENCUESTADOS1)){
    				actualizar_eiel_t_nucl_encuest_1(actualizar,tipo, null, obj, resultadoActualizacion);
    			}
    			else if (tipo.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS)){
    				actualizar_eiel_t_abast_serv(actualizar,tipo, null, obj, resultadoActualizacion);
    			}
    			else if (tipo.equals(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO)){
    				actualizar_eiel_t_abast_au(actualizar,tipo, null, obj, resultadoActualizacion);
    			}
    			else if (tipo.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO)){
    				actualizar_eiel_t_saneam_serv(actualizar,tipo, null, obj, resultadoActualizacion);
    			}
    			else if (tipo.equals(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO)){
    				actualizar_eiel_t_saneam_au(actualizar,tipo, null, obj, resultadoActualizacion);
    			}
    			else if (tipo.equals(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA)){
    				actualizar_eiel_t_rb_serv(actualizar,tipo, null, obj, elementos_eiel_t_rb,resultadoActualizacion);
    			}  		
    			else if (tipo.equals(ConstantesLocalGISEIEL.DISEMINADOS)){
    				actualizar_eiel_t_mun_diseminados(actualizar,tipo, null, obj, resultadoActualizacion);
    			} 
						
			}
		}
		
		/*if (elementos==0)
			logger.info("No existen elementos para gestionar por eliminacion de tipo:"+tipo);
		else
			logger.info("Hay que gestionar:"+elementos+" elementos por eliminacion de tipo:"+tipo);*/
		
		
    	return true;
	}
	
	
	/**
	 * Actualizacion de la tabla eiel_t_padron_ttmm
	 * @return
	 */
	private boolean actualizar_eiel_t_padron_ttmm(boolean actualizar,String tipo,InfoPadronEIEL infoPadron,Object obj,
													ResultadoActualizacion resultadoActualizacion){
		
		if (infoPadron==null){
			resultadoActualizacion.addElementBajaManual(obj);
			return true;
		}
		
		if (infoPadron.getP().equals("00")){
	    	try {


	    		resultadoActualizacion.addTotalPoblacion(fixedCodProvincia+infoPadron.getCON3(), (int)infoPadron.getTotalPop());
	    		resultadoActualizacion.addTotalViviendas(fixedCodProvincia+infoPadron.getCON3(), (int)infoPadron.getTotalViv());
	    		
	    		if (obj!=null){
		    		PadronMunicipiosEIEL pm = (PadronMunicipiosEIEL)obj;    		
					//resultadoActualizacion.append("Se actualiza la informacion de padron para el municipio:"+infoPadron.getCON3()+"\n");
						boolean actualizarRegistro=false;
						if (pm.getTotPobl_a1()!=infoPadron.getTotalPop()){
							String cadena="Actualizando padron habitantes en el municipio:"+infoPadron.getCON3()+" pm.getTotPobl_a1():"+pm.getTotPobl_a1()+"->"+(int)infoPadron.getTotalPop();
							resultadoActualizacion.append(cadena+"\n");	
							actualizarRegistro=true;
						}
						
						pm.setHombres_a1((int) infoPadron.getHombres());
						pm.setMujeres_a1((int)infoPadron.getMujeres());
						pm.setTotPobl_a1((int)infoPadron.getTotalPop());
						pm.setHombres_a2(null);
						pm.setMujeres_a2(null);
						pm.setTotPobl_a2(null);
						pm.setFecha_a1(new Integer(añoPadron));
						pm.setFecha_a2(null);
						if (actualizarRegistro){
							if (actualizar)
								InitEIEL.clienteLocalGISEIEL.insertarElemento(pm, null, tipo);
							
							
							resultadoActualizacion.addElementUpdate(pm);
						}
						else{
							String cadena="Poblacion en tabla padron se mantiene y no se actualiza:"+infoPadron.getCON3();
							resultadoActualizacion.append(cadena+"\n");	
						}
							
					
	    		}
	    		else{
					resultadoActualizacion.append("Se inserta la informacion de padron para el municipio:"+infoPadron.getCON3()+"\n");
					PadronMunicipiosEIEL pm = new PadronMunicipiosEIEL();
					
					pm.setCodINEProvincia(fixedCodProvincia);
					pm.setCodINEMunicipio(infoPadron.getCON3());
					
					pm.setHombres_a1((int) infoPadron.getHombres());
					pm.setMujeres_a1((int)infoPadron.getMujeres());
					pm.setTotPobl_a1((int)infoPadron.getTotalPop());
					pm.setHombres_a2(null);
					pm.setMujeres_a2(null);
					pm.setTotPobl_a2(null);
					pm.setFecha_a1(new Integer(añoPadron));
					pm.setFecha_a2(null);
	    			if (actualizar){	    				
	    				
						InitEIEL.clienteLocalGISEIEL.insertarElemento(pm, null, tipo);						
	    			}
	    			resultadoActualizacion.addElementAlta(pm);
	    		}
			
	    	}
	    	catch (Exception e){
	    		logger.error("Error",e);
	    		return false;
	    	}
		}
		return true;
	}
		
	/**
	 * Actualizacion de la tabla eiel_t_padron_ttmm
	 * @return
	 */
	private boolean actualizar_eiel_t_poblamiento(boolean actualizar,String tipo,InfoPadronEIEL infoPadron,Object obj,
													ResultadoActualizacion resultadoActualizacion){
						
	    	try {
				if (infoPadron==null){
	    			resultadoActualizacion.addElementBajaManual(obj);
	    			return true;
				}

				String codEntidad=infoPadron.getP()+infoPadron.getES();
				String codPoblamiento=infoPadron.getND();
	    		
				
				if (obj!=null){
		    		PoblamientoEIEL pm = (PoblamientoEIEL)obj; 
					//String cadena="Nucleo se mantiene:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
					//resultadoActualizacion.append(cadena+"\n");					
	    		}						
				else{
					String cadena="Poblacion no existe. Insertando Fila:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
					resultadoActualizacion.append(cadena+"\n");
					
					PoblamientoEIEL pm=new PoblamientoEIEL();
					pm.setCodINEProvincia(fixedCodProvincia);
					pm.setCodINEMunicipio(infoPadron.getCON3());
					pm.setCodINEEntidad(codEntidad);
					if (codPoblamiento.equals("99"))
		    			codPoblamiento="70";
					pm.setCodINEPoblamiento(codPoblamiento);
					if (actualizar){
						
						InitEIEL.clienteLocalGISEIEL.insertarElemento(pm, null, tipo);
					}
					resultadoActualizacion.addElementAlta(pm);
					
				}
	    	}
	    	catch (Exception e){
	    		logger.error("Error",e);
	    		return false;
	    	}
		
    	return true;
	}
	
	/**
	 * Actualizacion de la tabla eiel_t_padron_ttmm
	 * @return
	 */
	private boolean actualizar_eiel_t_entidad_singular(boolean actualizar,String tipo,InfoPadronEIEL infoPadron,Object obj,
														ResultadoActualizacion resultadoActualizacion){
						
	    	try {
	    		if (infoPadron==null){
	    			resultadoActualizacion.addElementBajaManual(obj);
	    			return true;
				}
	    		
	    		String codEntidad=infoPadron.getP()+infoPadron.getES();
				String codPoblamiento=infoPadron.getND();
	    		
				if (obj!=null){
		    		EntidadesSingularesEIEL pm = (EntidadesSingularesEIEL)obj; 
					//String cadena="Entidad Singular se mantiene:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
					//resultadoActualizacion.append(cadena+"\n");					
	    		}						
				else{
					String cadena="Entidad Singular no existe. Insertando Fila:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
					resultadoActualizacion.append(cadena+"\n");
					EntidadesSingularesEIEL pm=new EntidadesSingularesEIEL();
					pm.setCodINEProvincia(fixedCodProvincia);
					pm.setCodINEMunicipio(infoPadron.getCON3());
					pm.setCodINEEntidad(codEntidad);
					pm.setDenominacion(infoPadron.getOficial());
					if (actualizar){		
						
						
						InitEIEL.clienteLocalGISEIEL.insertarElemento(pm, null, tipo);
						
					}
					resultadoActualizacion.addElementAlta(pm);
				}
	    	}
	    	catch (Exception e){
	    		logger.error("Error",e);
	    		return false;
	    	}
		
    	return true;
	}
	
	/**
	 * Actualizacion de la tabla eiel_t_padron_ttmm
	 * @param claveBusqueda 
	 * @param indexData 
	 * @param indexData 
	 * @param claveBusqueda 
	 * @return
	 */
	private boolean actualizar_eiel_t_entidades_agrupadas(boolean actualizar,String tipo,InfoPadronEIEL infoPadron,Object obj,
														HashMap indexData, String claveBusqueda, ResultadoActualizacion resultadoActualizacion){
						
	    	try {
	    		
	    		if (infoPadron==null){
	    			resultadoActualizacion.addElementBajaManual(obj);
	    			return true;
				}
	    		
	    		String codEntidad=infoPadron.getP()+infoPadron.getES();
				String codPoblamiento=infoPadron.getND();
	    		
				if (obj!=null){
	    			EntidadesAgrupadasEIEL pm = (EntidadesAgrupadasEIEL)obj; 
	    			
	    			String claveEntidadAgrupada=pm.getCodINEMunicipio()+pm.getCodEntidad_agrupada()+pm.getCodNucleo_agrupado();
		    		
					if (infoPadron.getPrincipales()>=5){
						String cadena="Numero de Viviendas Principales>=5. Agrupacion se mantiene:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						resultadoActualizacion.append(cadena+"\n");
		    			resultadoActualizacion.addTotalInfoAgrupada(claveEntidadAgrupada,infoPadron.getTotalPop(),infoPadron.getTotalViv());
					}
					else{
						String cadena="Numero de Viviendas Principales<5. Eliminando Agrupacion:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						//logger.info(cadena);
						resultadoActualizacion.append(cadena+"\n");
						if (actualizar){
							InitEIEL.clienteLocalGISEIEL.eliminarElemento(pm, null, null, tipo);							
						}
						
						//Lo eliminamos del listado de agrupadas
						//Si el elemento a eliminar es un elemento en el que estan agrupados otros elementos es preciso que los eliminemos
						//de la tabla de entidades agrupadas y pasen como independientes a la tabla de nucleos encuestados1
						ArrayList clavesABorrar=new ArrayList();
						if (claveBusqueda!=null){
							if (pm.getCodEntidad().equals(pm.getCodEntidad_agrupada())){							
								indexData.remove(claveBusqueda);
								Iterator it = indexData.entrySet().iterator();	
								while (it.hasNext()) {
									Map.Entry e = (Map.Entry)it.next();		
									EntidadesAgrupadasEIEL entidadadAgrupada=(EntidadesAgrupadasEIEL)e.getValue();
									String claveElemento=entidadadAgrupada.getCodEntidad_agrupada()+entidadadAgrupada.getCodNucleo_agrupado();
									if (claveElemento.equals(claveBusqueda)){
										//logger.info("Hay que eliminar este elemento");
										
										cadena="Eliminando Agrupacion por eliminacion del padre:"+getNombreNucleo(infoPadron.getCON3(),entidadadAgrupada.getCodEntidad(),entidadadAgrupada.getCodNucleo());
										//logger.info(cadena);
										resultadoActualizacion.append(cadena+"\n");
										if (actualizar){
											InitEIEL.clienteLocalGISEIEL.eliminarElemento(entidadadAgrupada, null, null, tipo);							
										}
										String claveBorrado=entidadadAgrupada.getCodEntidad()+entidadadAgrupada.getCodNucleo();
										clavesABorrar.add(claveBorrado);
									}
								}
								Iterator itClaves=clavesABorrar.iterator();
								while (itClaves.hasNext()){
									String clave=(String)itClaves.next();
									indexData.remove(clave);
								}
							}
						}
						
						resultadoActualizacion.addElementBaja(pm);
	
					}
	    		}						
				else{
					//No hacemos nada porque no se pueden insertar entidades agrupadas automaticamente
	
				}
	    	}
	    	catch (Exception e){
	    		logger.error("Error",e);
	    		return false;
	    	}
		
    	return true;
	}
	
	
	
	
	
	
	
	/**
	 * Actualizacion de la tabla eiel_t_padron_ttmm
	 * @return
	 */
	private boolean actualizar_eiel_c_nucleo_poblacion(boolean actualizar,String tipo,InfoPadronEIEL infoPadron,Object obj,
														ResultadoActualizacion resultadoActualizacion){
						
	    	try {
	    		
	    		if (infoPadron==null){
	    			resultadoActualizacion.addElementBajaManual(obj);
	    			return true;
				}
	    		
	    		
	    		String codEntidad=infoPadron.getP()+infoPadron.getES();
				String codPoblamiento=infoPadron.getND();
	    		
				if (obj!=null){
		    		NucleosPoblacionEIEL pm = (NucleosPoblacionEIEL)obj; 
					
		    		
					if (infoPadron.getPrincipales()>=5){
						String cadena="Numero de Viviendas Principales>=5. Nucleo se mantiene:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->"+infoPadron.getOficial();
						//resultadoActualizacion.append(cadena+"\n");
					}
					else{
						String cadena="Numero de Viviendas Principales<5. Eliminando Nucleo:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->"+infoPadron.getOficial();
						//logger.info(cadena);
						resultadoActualizacion.append(cadena+"\n");
						if (actualizar){
							InitEIEL.clienteLocalGISEIEL.eliminarElemento(pm, null, null, tipo);							
						}
						resultadoActualizacion.addElementBaja(pm);
	
					}
	    		}						
				else{
					if (infoPadron.getPrincipales()>=5){
						String cadena="Numero de Viviendas Principales>=5 ("+(int)infoPadron.getPrincipales()+") Insertando Nucleo:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->"+infoPadron.getOficial();
						//logger.info(cadena);
						resultadoActualizacion.append(cadena+"\n");
						NucleosPoblacionEIEL pm=new NucleosPoblacionEIEL();
						if (actualizar){							
							
							pm.setIdMunicipio(Integer.parseInt(fixedCodProvincia+infoPadron.getCON3()));
							pm.setCodINEProvincia(fixedCodProvincia);
							pm.setCodINEMunicipio(infoPadron.getCON3());
							pm.setCodINEEntidad(codEntidad);
							if (codPoblamiento.equals("99"))
				    			codPoblamiento="70";
							pm.setCodINEPoblamiento(codPoblamiento);
							pm.setNombreOficial(infoPadron.getOficial());
							InitEIEL.clienteLocalGISEIEL.insertarElemento(pm, null, tipo);
						}
						resultadoActualizacion.addElementAlta(pm);
					}
					else{
						String cadena="Numero de Viviendas Principales <5. Nucleo no existia:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->"+infoPadron.getOficial();
						//logger.info(cadena);
						//resultadoActualizacion.append(cadena+"\n");
					}
				}
	    	}
	    	catch (Exception e){
	    		logger.error("Error",e);
	    		return false;
	    	}
		
    	return true;
	}
	
	
	
	/**
	 * Actualizacion de la tabla eiel_t_padron_ttmm
	 * @return
	 */
	private boolean actualizar_eiel_t_nucl_encuest_1(boolean actualizar,String tipo,InfoPadronEIEL infoPadron,Object obj,
														ResultadoActualizacion resultadoActualizacion){
						
	    	try {
	    		
	     		
	    		if (infoPadron==null){
	    			resultadoActualizacion.addElementBajaManual(obj);
	    			return true;
				}
	    		
	    		String codEntidad=infoPadron.getP()+infoPadron.getES();
				String codPoblamiento=infoPadron.getND();
				String codMunicipio=fixedCodProvincia+infoPadron.getCON3();
	    		
				if (codPoblamiento.equals("99"))
	    			codPoblamiento="70";
	    		
	    		//Clave de busqueda
	    		String claveBusqueda=codEntidad+codPoblamiento;   
	

	    		EntidadAgrupada entidadAgrupada=null;
	    		
	    		if (resultadoActualizacion.getEntidadesAgrupadas(codMunicipio)!=null){
	    			HashMap indexData=resultadoActualizacion.getEntidadesAgrupadas(codMunicipio);
	    			if (indexData.get(claveBusqueda)!=null){
	    				//logger.info("Registro esta en la tabla de agrupadas");
	    				String claveElementoBusqueda=infoPadron.getCON3()+claveBusqueda;
	    				entidadAgrupada=resultadoActualizacion.getTotalInfoAgrupada(claveElementoBusqueda);
	    				//Si el registro no es una entidad agrupada no lo insertamos en la tabla de eiel_t_nucleo_encuestado_1
	    				if (entidadAgrupada==null)
	    					return false;	    			
	    			}
	    		}
	    		
	    		int totalPoblacion;
				int totalViviendas;
				
				boolean bentidadAgrupada=false;
				if (entidadAgrupada!=null){
					totalPoblacion=(int)entidadAgrupada.getTotalPoblacion();
					totalViviendas=(int)entidadAgrupada.getTotalViviendas();
					bentidadAgrupada=true;
				}
				else{
					totalPoblacion=(int)infoPadron.getTotalPop();
					totalViviendas=(int)infoPadron.getTotalViv();
					bentidadAgrupada=false;
				}
				
					
	    		if (obj!=null){
		    		Encuestados1EIEL pm = (Encuestados1EIEL)obj; 
							    		
					if (infoPadron.getPrincipales()>=5){
						
						resultadoActualizacion.addTotalPoblacionEncuestada(codMunicipio,totalPoblacion);
			    		resultadoActualizacion.addTotalViviendasEncuestadas(codMunicipio,totalViviendas);
			    						
			    		
						String cadena="Numero de Viviendas Principales>=5. Encuestado1 se mantiene:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						//resultadoActualizacion.append(cadena+"\n");
						//Actualizamos su informacion
						
						
						boolean actualizarRegistro=false;
						
						if (totalPoblacion!=pm.getPadron()){
							cadena="Actualizando NucleoEncuestado1. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Total Poblacion:"+pm.getPadron()+"->"+totalPoblacion+ " Agrupado:"+bentidadAgrupada;
							resultadoActualizacion.append(cadena+"\n");

							pm.setPadron(totalPoblacion);
							actualizarRegistro=true;
						}
						
						int poblacionEstacional=(int)((Math.ceil(totalPoblacion*PORCENTAJE_POBLACION_ESTACIONAL)));
						if (poblacionEstacional!=pm.getPoblacionEstacional()){		
							cadena="Actualizando NucleoEncuestado1. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Total Poblacion estacional:"+pm.getPoblacionEstacional()+"->"+poblacionEstacional+ " Agrupado:"+bentidadAgrupada;
							resultadoActualizacion.append(cadena+"\n");
							pm.setPoblacionEstacional(poblacionEstacional);
							actualizarRegistro=true;
						}
						
						if (totalViviendas!=pm.getViviendasTotales()){
							cadena="Actualizando NucleoEncuestado1. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Total Viviendas:"+pm.getViviendasTotales()+"->"+totalViviendas;
							resultadoActualizacion.append(cadena+"\n");
							pm.setViviendasTotales(totalViviendas);
							actualizarRegistro=true;
						}
						
						if (actualizarRegistro){
							if (actualizar){
								InitEIEL.clienteLocalGISEIEL.insertarElemento(pm, null, tipo);
							}
							resultadoActualizacion.addElementUpdate(pm);	
						}
						else{
							cadena="Numero de Viviendas Principales>=5. Encuestado1 se mantiene y no se actualiza:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
							resultadoActualizacion.append(cadena+"\n");							
						}
					
											
			
					}
					else{
						String cadena="Numero de Viviendas Principales<5. Eliminando Encuestado1:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						//logger.info(cadena);
						resultadoActualizacion.append(cadena+"\n");
						if (actualizar){
							
							InitEIEL.clienteLocalGISEIEL.eliminarElemento(pm, null, null, tipo);							
						}
						resultadoActualizacion.addElementBaja(pm);
	
					}
	    		}						
				else{
					if (infoPadron.getPrincipales()>=5){
						
						resultadoActualizacion.addTotalPoblacionEncuestada(codMunicipio,totalPoblacion);
			    		resultadoActualizacion.addTotalViviendasEncuestadas(codMunicipio,totalViviendas);
			    	
					
						String cadena="Numero de Viviendas Principales>=5 ("+(int)infoPadron.getPrincipales()+") Insertando Encuestado1:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						//logger.info(cadena);
						resultadoActualizacion.append(cadena+"\n");
						
						Encuestados1EIEL pm=new Encuestados1EIEL();
						pm.setIdMunicipio(Integer.parseInt(fixedCodProvincia+infoPadron.getCON3()));
						pm.setCodINEProvincia(fixedCodProvincia);
						pm.setCodINEMunicipio(infoPadron.getCON3());
						pm.setCodINEEntidad(codEntidad);
						if (codPoblamiento.equals("99"))
			    			codPoblamiento="70";
						pm.setCodINEPoblamiento(codPoblamiento);
						//Datos de Totales
						pm.setPadron(totalPoblacion);
						pm.setPoblacionEstacional((int)(Math.ceil(totalPoblacion*PORCENTAJE_POBLACION_ESTACIONAL)));
						pm.setViviendasTotales(totalViviendas);
						if (actualizar){


							InitEIEL.clienteLocalGISEIEL.insertarElemento(pm, null, tipo);
						}
						resultadoActualizacion.addElementAlta(pm);
					}
					else{
						String cadena="Numero de Viviendas Principales <5. Encuestado1 no existia:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						//logger.info(cadena);
						//resultadoActualizacion.append(cadena+"\n");
					}
				}
	    	}
	    	catch (Exception e){
	    		logger.error("Error",e);
	    		return false;
	    	}
		
    	return true;
	}
	
	
	/**
	 * Actualizacion de la tabla eiel_t_abast_serv
	 * @return
	 */
	private boolean actualizar_eiel_t_abast_serv(boolean actualizar,String tipo,InfoPadronEIEL infoPadron,Object obj,
														ResultadoActualizacion resultadoActualizacion){
						
	    	try {
	    		
	    		if (infoPadron==null){
	    			resultadoActualizacion.addElementBajaManual(obj);
	    			return true;
	    		}
	    		
	    		String codEntidad=infoPadron.getP()+infoPadron.getES();
				String codPoblamiento=infoPadron.getND();
				String codMunicipio=fixedCodProvincia+infoPadron.getCON3();
	    		
				if (codPoblamiento.equals("99"))
	    			codPoblamiento="70";
	    		
	    		//Clave de busqueda
	    		String claveBusqueda=codEntidad+codPoblamiento;   
	
				
	    	
	    		
	    		EntidadAgrupada entidadAgrupada=null;
	    		
	    		if (resultadoActualizacion.getEntidadesAgrupadas(codMunicipio)!=null){
	    			HashMap indexData=resultadoActualizacion.getEntidadesAgrupadas(codMunicipio);
	    			if (indexData.get(claveBusqueda)!=null){
	    				//logger.info("Registro esta en la tabla de agrupadas");
	    				String claveElementoBusqueda=infoPadron.getCON3()+claveBusqueda;
	    				entidadAgrupada=resultadoActualizacion.getTotalInfoAgrupada(claveElementoBusqueda);
	    				//Si el registro no es una entidad agrupada no lo insertamos en la tabla de eiel_t_nucleo_encuestado_1
	    				if (entidadAgrupada==null)
	    					return false;	    			
	    			}
	    		}
	    		
	    		int totalPoblacion;
				int totalViviendas;				
				boolean bentidadAgrupada=false;
				if (entidadAgrupada!=null){
					totalPoblacion=(int)entidadAgrupada.getTotalPoblacion();
					totalViviendas=(int)entidadAgrupada.getTotalViviendas();
					bentidadAgrupada=true;
				}
				else{
					totalPoblacion=(int)infoPadron.getTotalPop();
					totalViviendas=(int)infoPadron.getTotalViv();
					bentidadAgrupada=false;
				}
				
	    		if (obj!=null){
	    			ServiciosAbastecimientosEIEL pm = (ServiciosAbastecimientosEIEL)obj; 
							    		
					if (infoPadron.getPrincipales()>=5){
						String cadena="Numero de Viviendas Principales>=5. ServicioAbastecimiento se mantiene:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						//resultadoActualizacion.append(cadena+"\n");
						//Actualizamos su informacion
	
						//Si eiel_t_abast_serv.viviendas_c_conex = 0:
						//	   eiel_t_abast_serv.viv_deficitarias = NOMENCLATOR.TOTAL_VIV.
						//	o	Si no: eiel_t_abast_serv. viviendas_c_conex = NOMENCLATOR.TOTAL_VIV

						
						boolean actualizarRegistro=false;
						
						int viviendasDeficitarias=0;
						int viviendasConectadas=0;
						if (pm.getViviendasConectadas()==0){
							viviendasDeficitarias=totalViviendas;
							if (viviendasDeficitarias!=pm.getViviendasDeficitarias()){
								cadena="Actualizando ServicioAbastecimiento. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Viviendas Deficitarias:"+pm.getViviendasDeficitarias()+"->"+viviendasDeficitarias;
								resultadoActualizacion.append(cadena+"\n");
								pm.setViviendasDeficitarias(viviendasDeficitarias);
								actualizarRegistro=true;
							}
						}
						else{
							
							//Almacenamos el numero de viviendas conectadas que tiene el servicio de abastecimento para luego actualizar
							//o no el abastecimiento autonomo. Solo se incorpora si el numero de viviendas conectadas es >0
							resultadoActualizacion.addAbastecimientoConectado(codMunicipio+claveBusqueda);
							
							viviendasConectadas=totalViviendas;
							if (pm.getViviendasNoConectadas() != 0){
								if (totalViviendas != (pm.getViviendasConectadas()+pm.getViviendasNoConectadas())){
									cadena="Revise manualmente ServicioAbastecimiento. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Total Viviendas ="+totalViviendas+" - Viviendas conectadas = "+pm.getViviendasConectadas()+" - Viviendas no conectadas = "+pm.getViviendasNoConectadas();
									resultadoActualizacion.append(cadena+"\n");
								}
										
							}
							else{
								if (viviendasConectadas!=pm.getViviendasConectadas()){
									cadena="Actualizando ServicioAbastecimiento. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Viviendas Conectadas:"+pm.getViviendasConectadas()+"->"+viviendasConectadas;
									resultadoActualizacion.append(cadena+"\n");								
									pm.setViviendasConectadas(viviendasConectadas);
									actualizarRegistro=true;
								}
							}
						}
						
						int valorInviernoOriginal=(int)(Math.ceil(totalPoblacion*PORCENTAJE_CONSUMO_INVIERNO));
						int valorInvierno;
						double consumI = (double)(totalPoblacion *ConstantesLocalGISEIEL.MULTIPLO_CONSUMO_INVIERNO) /1000;
						if (consumI > 0 && consumI < 1)
							valorInvierno = 1;
						else
							valorInvierno = (int)Math.floor(consumI + 0.5d);
						
						
						
						if (valorInvierno!=pm.getConsumoInvierno() && pm.getViviendasConectadas()!=0){
							cadena="Actualizando ServicioAbastecimiento. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Consumo Invierno:"+pm.getConsumoInvierno()+"->"+valorInvierno+" Poblacion:"+totalPoblacion;
							resultadoActualizacion.append(cadena+"\n");
							pm.setConsumoInvierno(valorInvierno);
							actualizarRegistro=true;
						}

						int valorVeranoOriginal=(int)(Math.ceil(totalPoblacion*PORCENTAJE_POBLACION_ESTACIONAL*PORCENTAJE_CONSUMO_VERANO));						
						int valorVerano;
						int totalPoblacionEstacional=(int)(Math.ceil(totalPoblacion*PORCENTAJE_POBLACION_ESTACIONAL));
						double valorVeranoDouble = (double)(totalPoblacionEstacional*ConstantesLocalGISEIEL.MULTIPLO_CONSUMO_VERANO) /1000;
						if (valorVeranoDouble > 0 && valorVeranoDouble < 1)
							valorVerano = 1;
						else
							valorVerano = (int)Math.floor(valorVeranoDouble+ 0.5d);
						
						if(valorVerano == valorInvierno)
							valorVerano=valorVerano+1;
						
						if (valorVerano!=pm.getConsumoVerano() && pm.getViviendasConectadas()!=0){
							cadena="Actualizando ServicioAbastecimiento. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Consumo Verano:"+pm.getConsumoVerano()+"->"+valorVerano+" Poblacion:"+totalPoblacion;
							resultadoActualizacion.append(cadena+"\n");
							pm.setConsumoVerano(valorVerano);
							actualizarRegistro=true;
						}
						
						int deficit=(int)(viviendasDeficitarias*PORCENTAJE_DEFICITARIAS);
						if (deficit!=pm.getLogitudDeficitaria()){
							cadena="Actualizando ServicioAbastecimiento. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Longitud Deficitaria:"+pm.getLogitudDeficitaria()+"->"+deficit;
							resultadoActualizacion.append(cadena+"\n");
							pm.setLongitudDeficitaria(deficit);
							actualizarRegistro=true;
						}
						
						if (pm.getViviendasDeficitarias()!=0){
							int poblacionResidenteDefitaria=(int)(totalPoblacion);
							if (poblacionResidenteDefitaria!=pm.getPoblacionResidenteDeficitaria()){
								cadena="Actualizando ServicioAbastecimiento. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Poblacion Residente Deficitaria:"+pm.getPoblacionResidenteDeficitaria()+"->"+totalPoblacion;
								resultadoActualizacion.append(cadena+"\n");
								pm.setPoblacionResidenteDeficitaria(poblacionResidenteDefitaria);
								actualizarRegistro=true;
							}
						}
						if (pm.getViviendasDeficitarias()!=0){
							int poblacionEstacionalDefitaria=(int)(Math.ceil(totalPoblacion*PORCENTAJE_POBLACION_ESTACIONAL));
							if (poblacionEstacionalDefitaria!=pm.getPoblacionEstacionalDeficitaria()){
								cadena="Actualizando ServicioAbastecimiento. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Poblacion Estacional Deficitaria:"+pm.getPoblacionEstacionalDeficitaria()+"->"+poblacionEstacionalDefitaria;
								resultadoActualizacion.append(cadena+"\n");
								pm.setPoblacionEstacionalDeficitaria(poblacionEstacionalDefitaria);
								actualizarRegistro=true;
							}
						}
						
						if (actualizarRegistro){
							if (actualizar){
								InitEIEL.clienteLocalGISEIEL.insertarElemento(pm, null, tipo);
							}
							resultadoActualizacion.addElementUpdate(pm);	
						}
						else{
							cadena="Numero de Viviendas Principales>=5. ServicioAbastecimiento se mantiene y no se actualiza:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
							resultadoActualizacion.append(cadena+"\n");							
						}
									
						
			
					}
					else{

						//Descomentamos la eliminacion del servicio de abastecimiento
						String cadena="Numero de Viviendas Principales<5. Desactivando la eliminacion del servicio ServicioAbastecimiento:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						logger.info(cadena);
						resultadoActualizacion.append(cadena+"\n");
						/*String cadena="Numero de Viviendas Principales<5. Eliminando ServicioAbastecimiento:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						//logger.info(cadena);
						resultadoActualizacion.append(cadena+"\n");
						if (actualizar){
							
							InitEIEL.clienteLocalGISEIEL.eliminarElemento(pm, null, null, tipo);							
						}
						resultadoActualizacion.addElementBaja(pm);*/
	
					}
	    		}						
				else{
					
					if (infoPadron.getPrincipales()>=5){

						String cadena="Numero de Viviendas Principales>=5 ("+(int)infoPadron.getPrincipales()+") Es preciso insertar a mano ServicioAbastecimiento:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						//logger.info(cadena);
						resultadoActualizacion.append(cadena+"\n");
						ServiciosAbastecimientosEIEL pm=new ServiciosAbastecimientosEIEL();
						pm.setIdMunicipio(Integer.parseInt(fixedCodProvincia+infoPadron.getCON3()));
						pm.setCodINEProvincia(fixedCodProvincia);
						pm.setCodINEMunicipio(infoPadron.getCON3());
						pm.setCodINEEntidad(codEntidad);
						if (codPoblamiento.equals("99"))
			    			codPoblamiento="70";
						pm.setCodINEPoblamiento(codPoblamiento);
						
						resultadoActualizacion.addElementAltaManual(pm);
					}
				}
	    	}
	    	catch (Exception e){
	    		logger.error("Error",e);
	    		return false;
	    	}
		
    	return true;
	}
	
	
	/**
	 * Actualizacion de la tabla eiel_t_abast_au
	 * @return
	 */
	private boolean actualizar_eiel_t_abast_au(boolean actualizar,String tipo,InfoPadronEIEL infoPadron,Object obj,
														ResultadoActualizacion resultadoActualizacion){
						
	    	try {
	    		
	    		if (infoPadron==null){
	    			resultadoActualizacion.addElementBajaManual(obj);
	    			return true;
	    		}
	    		
	    		String codEntidad=infoPadron.getP()+infoPadron.getES();
				String codPoblamiento=infoPadron.getND();
				String codMunicipio=fixedCodProvincia+infoPadron.getCON3();
	    		
				if (codPoblamiento.equals("99"))
	    			codPoblamiento="70";
	    		
	    		//Clave de busqueda
	    		String claveBusqueda=codEntidad+codPoblamiento;   
	
		
	    		
	    		EntidadAgrupada entidadAgrupada=null;
	    		
	    		if (resultadoActualizacion.getEntidadesAgrupadas(codMunicipio)!=null){
	    			HashMap indexData=resultadoActualizacion.getEntidadesAgrupadas(codMunicipio);
	    			if (indexData.get(claveBusqueda)!=null){
	    				//logger.info("Registro esta en la tabla de agrupadas");
	    				String claveElementoBusqueda=infoPadron.getCON3()+claveBusqueda;
	    				entidadAgrupada=resultadoActualizacion.getTotalInfoAgrupada(claveElementoBusqueda);
	    				//Si el registro no es una entidad agrupada no lo insertamos en la tabla de eiel_t_nucleo_encuestado_1
	    				if (entidadAgrupada==null)
	    					return false;	    			
	    			}
	    		}
	    		
	    		int totalPoblacion;
				int totalViviendas;				
				boolean bentidadAgrupada=false;
				if (entidadAgrupada!=null){
					totalPoblacion=(int)entidadAgrupada.getTotalPoblacion();
					totalViviendas=(int)entidadAgrupada.getTotalViviendas();
					bentidadAgrupada=true;
				}
				else{
					totalPoblacion=(int)infoPadron.getTotalPop();
					totalViviendas=(int)infoPadron.getTotalViv();
					bentidadAgrupada=false;
				}
				
	    		if (obj!=null){
	    			AbastecimientoAutonomoEIEL pm = (AbastecimientoAutonomoEIEL)obj; 
							    		
					if (infoPadron.getPrincipales()>=5){
						String cadena="Numero de Viviendas Principales>=5. AbastecimientoAutonomoEIEL se mantiene:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						//resultadoActualizacion.append(cadena+"\n");
						//Actualizamos su informacion
						
						boolean actualizarRegistro=false;
					
						//CAMBIAR OPERATIVA
						Encuestados2EIEL encuestados2EIEL=null;
						if (elementosNucleosEncuestados2.get(codMunicipio+codEntidad+codPoblamiento)!=null)
							encuestados2EIEL=(Encuestados2EIEL)elementosNucleosEncuestados2.get(codMunicipio+codEntidad+codPoblamiento);
							
						if (encuestados2EIEL!=null && encuestados2EIEL.getDisponibilidadCaudal().equals("NO")){
						//if (pm.getSuficienciaCaudal().equals("SF")){
							if (totalPoblacion!=pm.getPoblacionResidente()){
								cadena="Actualizando AbastecimientoAutonomoEIEL. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Total Poblacion:"+pm.getPoblacionResidente()+"->"+totalPoblacion+ " Agrupado:"+bentidadAgrupada;
								resultadoActualizacion.append(cadena+"\n");
								pm.setPoblacionResidente(totalPoblacion);
								actualizarRegistro=true;
							}
							
							int poblacionEstacional=(int)(Math.ceil(totalPoblacion*PORCENTAJE_POBLACION_ESTACIONAL));
							if (poblacionEstacional!=pm.getPoblacionEstacional()){		
								pm.setPoblacionEstacional(poblacionEstacional);
								actualizarRegistro=true;
							}
							
							if (totalViviendas!=pm.getViviendas()){
								cadena="Actualizando AbastecimientoAutonomoEIEL. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Total Viviendas:"+pm.getViviendas()+"->"+totalViviendas+ " Agrupado:"+bentidadAgrupada;
								resultadoActualizacion.append(cadena+"\n");
	
								pm.setViviendas(totalViviendas);
								actualizarRegistro=true;
							}
							
							if (actualizarRegistro){							
								if (actualizar){
									InitEIEL.clienteLocalGISEIEL.insertarElemento(pm, null, tipo);	
								}
								resultadoActualizacion.addElementUpdate(pm);
							}
							else{
								cadena="Numero de Viviendas Principales>=5. AbastecimientoAutonomoEIEL se mantiene y no se actualiza:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
								resultadoActualizacion.append(cadena+"\n");
							}
							
						}
						else if (encuestados2EIEL!=null && !encuestados2EIEL.getDisponibilidadCaudal().equals("NO")){
							cadena="Actualizando AbastecimientoAutonomoEIEL. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"-> Fijamos a 0 por ser el caudal SF o IN";
							resultadoActualizacion.append(cadena+"\n");
							pm.setPoblacionResidente(0);
							pm.setPoblacionEstacional(0);							
							pm.setViviendas(0);
							actualizarRegistro=true;						
							if (actualizar){
								InitEIEL.clienteLocalGISEIEL.insertarElemento(pm, null, tipo);	
							}
							resultadoActualizacion.addElementUpdate(pm);
						}		
						else{
							cadena="Valor de Caudal para el municipio. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+" no esta dentro de los soportados";
							resultadoActualizacion.append(cadena+"\n");
						}
		
					}
					else{
						/*String cadena="Numero de Viviendas Principales<5. Desactivando la eliminacion del servicio ServicioAbastecimiento:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						logger.info(cadena);
						resultadoActualizacion.append(cadena+"\n");*/

						
						String cadena="Numero de Viviendas Principales<5. Eliminando ServicioAbastecimiento:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						//logger.info(cadena);
						resultadoActualizacion.append(cadena+"\n");
						if (actualizar){
							
							InitEIEL.clienteLocalGISEIEL.eliminarElemento(pm, null, null, tipo);							
						}
						resultadoActualizacion.addElementBaja(pm);
	
					}
	    		}						
				else{
					if (infoPadron.getPrincipales()>=5){
						
						if (!resultadoActualizacion.isAbastecimientoConectado(codMunicipio+claveBusqueda)){
							String cadena="Numero de Viviendas Principales>=5 ("+(int)infoPadron.getPrincipales()+") Es preciso insertar a mano AbastecimientoAutonomoEIEL:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
							//logger.info(cadena);
							resultadoActualizacion.append(cadena+"\n");
							
							AbastecimientoAutonomoEIEL pm=new AbastecimientoAutonomoEIEL();
							pm.setIdMunicipio(Integer.parseInt(fixedCodProvincia+infoPadron.getCON3()));
							pm.setCodINEProvincia(fixedCodProvincia);
							pm.setCodINEMunicipio(infoPadron.getCON3());
							pm.setCodINEEntidad(codEntidad);
							if (codPoblamiento.equals("99"))
				    			codPoblamiento="70";
							pm.setCodINENucleo(codPoblamiento);
							
							resultadoActualizacion.addElementAltaManual(pm);
						}
						else{							
							String cadena="Numero de Viviendas Principales>=5 ("+(int)infoPadron.getPrincipales()+") El nucleo no tiene que estar en la tabla de AbastecimientoAutonomoEIEL:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
							//logger.info(cadena);
							resultadoActualizacion.append(cadena+"\n");
						}
					}
				}
	    	}
	    	catch (Exception e){
	    		logger.error("Error",e);
	    		return false;
	    	}
		
    	return true;
	}
	
	
	/**
	 * Actualizacion de la tabla eiel_t_saneam_serv
	 * @return
	 */
	private boolean actualizar_eiel_t_saneam_serv(boolean actualizar,String tipo,InfoPadronEIEL infoPadron,Object obj,
														ResultadoActualizacion resultadoActualizacion){
						
	    	try {
	    		
	    		if (infoPadron==null){
	    			resultadoActualizacion.addElementBajaManual(obj);
	    			return true;
	    		}
	    		
	    		String codEntidad=infoPadron.getP()+infoPadron.getES();
				String codPoblamiento=infoPadron.getND();
				String codMunicipio=fixedCodProvincia+infoPadron.getCON3();
	    		
				if (codPoblamiento.equals("99"))
	    			codPoblamiento="70";
	    		
	    		//Clave de busqueda
	    		String claveBusqueda=codEntidad+codPoblamiento;   
	
				
	    	
	    		
	    		EntidadAgrupada entidadAgrupada=null;
	    		
	    		if (resultadoActualizacion.getEntidadesAgrupadas(codMunicipio)!=null){
	    			HashMap indexData=resultadoActualizacion.getEntidadesAgrupadas(codMunicipio);
	    			if (indexData.get(claveBusqueda)!=null){
	    				//logger.info("Registro esta en la tabla de agrupadas");
	    				String claveElementoBusqueda=infoPadron.getCON3()+claveBusqueda;
	    				entidadAgrupada=resultadoActualizacion.getTotalInfoAgrupada(claveElementoBusqueda);
	    				//Si el registro no es una entidad agrupada no lo insertamos en la tabla de eiel_t_nucleo_encuestado_1
	    				if (entidadAgrupada==null)
	    					return false;	    			
	    			}
	    		}
	    		
	    		int totalPoblacion;
				int totalViviendas;				
				boolean bentidadAgrupada=false;
				if (entidadAgrupada!=null){
					totalPoblacion=(int)entidadAgrupada.getTotalPoblacion();
					totalViviendas=(int)entidadAgrupada.getTotalViviendas();
					bentidadAgrupada=true;
				}
				else{
					totalPoblacion=(int)infoPadron.getTotalPop();
					totalViviendas=(int)infoPadron.getTotalViv();
					bentidadAgrupada=false;
				}
				
	    		if (obj!=null){
	    			ServiciosSaneamientoEIEL pm = (ServiciosSaneamientoEIEL)obj; 
							    		
					if (infoPadron.getPrincipales()>=5){
						String cadena="Numero de Viviendas Principales>=5. ServicioSaneamiento se mantiene:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						//resultadoActualizacion.append(cadena+"\n");
						//Actualizamos su informacion
	
						//Si eiel_t_saneam_serv.viviendas_c_conex = 0:
						//	   eiel_t_saneam_serv.viv_deficitarias = NOMENCLATOR.TOTAL_VIV.
						//	o	Si no: eiel_t_saneam_serv. viviendas_c_conex = NOMENCLATOR.TOTAL_VIV

						
						boolean actualizarRegistro=false;
						
						
						
						
						int viviendasDeficitarias=0;
						int viviendasConectadas=0;
						if (pm.getVivConectadas()==0){
							viviendasDeficitarias=totalViviendas;
							if (viviendasDeficitarias!=pm.getVivDeficitarias()){
								cadena="Actualizando ServicioSaneamiento. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Viviendas Deficitarias:"+pm.getVivDeficitarias()+"->"+viviendasDeficitarias;
								resultadoActualizacion.append(cadena+"\n");
								pm.setVivDeficitarias(viviendasDeficitarias);
								actualizarRegistro=true;
							}
						}
						else{
							
							//Almacenamos el numero de viviendas conectadas que tiene el servicio de saneamiento para luego actualizar
							//o no el saneamiento autonomo. Solo se incorpora si el numero de viviendas conectadas es >0
							resultadoActualizacion.addSaneamientoConectado(codMunicipio+claveBusqueda);
							
							
							viviendasConectadas=totalViviendas;
							if (pm.getVivNoConectadas() != 0){
								if (totalViviendas != (pm.getVivConectadas()+pm.getVivNoConectadas())){
									cadena="Revise manualmente ServicioSaneamiento. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Total Viviendas ="+totalViviendas+" - Viviendas conectadas = "+pm.getVivConectadas()+" - Viviendas no conectadas = "+pm.getVivNoConectadas();
									resultadoActualizacion.append(cadena+"\n");
								}
										
							}
							else{
								if (viviendasConectadas!=pm.getVivConectadas()){
									cadena="Actualizando ServicioSaneamiento. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Viviendas Conectadas:"+pm.getVivConectadas()+"->"+viviendasConectadas;
									resultadoActualizacion.append(cadena+"\n");								
									pm.setVivConectadas(viviendasConectadas);
									actualizarRegistro=true;
								}
							}
						}
						
						
						
						int deficit=(int)(viviendasDeficitarias*PORCENTAJE_DEFICITARIAS);
						if (deficit!=pm.getLongDeficitaria()){
							cadena="Actualizando ServicioSaneamiento. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Longitud Deficitaria:"+pm.getLongDeficitaria()+"->"+deficit;
							resultadoActualizacion.append(cadena+"\n");
							pm.setLongDeficitaria(deficit);
							actualizarRegistro=true;
						}
						
						if (pm.getVivDeficitarias()!=0){
							int poblacionResidenteDefitaria=(int)(totalPoblacion);
							if (poblacionResidenteDefitaria!=pm.getPoblResDeficitaria()){
								cadena="Actualizando ServicioSaneamiento. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Poblacion Residente Deficitaria:"+pm.getPoblResDeficitaria()+"->"+totalPoblacion;
								resultadoActualizacion.append(cadena+"\n");
								pm.setPoblResDeficitaria(poblacionResidenteDefitaria);
								actualizarRegistro=true;
							}
						}
						if (pm.getVivDeficitarias()!=0){
							int poblacionEstacionalDefitaria=(int)(Math.ceil(totalPoblacion*PORCENTAJE_POBLACION_ESTACIONAL));
							if (poblacionEstacionalDefitaria!=pm.getPoblEstDeficitaria()){
								cadena="Actualizando ServicioSaneamiento. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Poblacion Estacional Deficitaria:"+pm.getPoblEstDeficitaria()+"->"+poblacionEstacionalDefitaria;
								resultadoActualizacion.append(cadena+"\n");
								pm.setPoblEstDeficitaria(poblacionEstacionalDefitaria);
								actualizarRegistro=true;
							}
						}
								
						if (actualizarRegistro){
							if (actualizar){
								InitEIEL.clienteLocalGISEIEL.insertarElemento(pm, null, tipo);
							}
							resultadoActualizacion.addElementUpdate(pm);	
						}
						else{
							cadena="Numero de Viviendas Principales>=5. ServicioSaneamiento se mantiene y no se actualiza:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
							resultadoActualizacion.append(cadena+"\n");							
						}
						
						
			
					}
					else{
						String cadena="Numero de Viviendas Principales<5. Eliminando ServicioSaneamiento:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						//logger.info(cadena);
						resultadoActualizacion.append(cadena+"\n");
						if (actualizar){
							
							InitEIEL.clienteLocalGISEIEL.eliminarElemento(pm, null, null, tipo);							
						}
						resultadoActualizacion.addElementBaja(pm);
	
					}
	    		}						
				else{
					
					if (infoPadron.getPrincipales()>=5){
						String cadena="Numero de Viviendas Principales>=5 ("+(int)infoPadron.getPrincipales()+") Es preciso insertar a mano ServicioSaneamiento:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						//logger.info(cadena);
						resultadoActualizacion.append(cadena+"\n");
						ServiciosSaneamientoEIEL pm=new ServiciosSaneamientoEIEL();
						pm.setIdMunicipio(Integer.parseInt(fixedCodProvincia+infoPadron.getCON3()));
						pm.setCodINEProvincia(fixedCodProvincia);
						pm.setCodINEMunicipio(infoPadron.getCON3());
						pm.setCodINEEntidad(codEntidad);
						if (codPoblamiento.equals("99"))
			    			codPoblamiento="70";
						pm.setCodINEPoblamiento(codPoblamiento);
						
						resultadoActualizacion.addElementAltaManual(pm);
					}
				}
	    	}
	    	catch (Exception e){
	    		logger.error("Error",e);
	    		return false;
	    	}
		
    	return true;
	}
	
	/**
	 * Actualizacion de la tabla eiel_t_saneam_au
	 * @return
	 */
	private boolean actualizar_eiel_t_saneam_au(boolean actualizar,String tipo,InfoPadronEIEL infoPadron,Object obj,
														ResultadoActualizacion resultadoActualizacion){
						
	    	try {
	    		
	    		if (infoPadron==null){
	    			resultadoActualizacion.addElementBajaManual(obj);
	    			return true;
	    		}
	    		
	    		String codEntidad=infoPadron.getP()+infoPadron.getES();
				String codPoblamiento=infoPadron.getND();
				String codMunicipio=fixedCodProvincia+infoPadron.getCON3();
	    		
				if (codPoblamiento.equals("99"))
	    			codPoblamiento="70";
	    		
	    		//Clave de busqueda
	    		String claveBusqueda=codEntidad+codPoblamiento;   
	
		
	    		
	    		EntidadAgrupada entidadAgrupada=null;
	    		
	    		if (resultadoActualizacion.getEntidadesAgrupadas(codMunicipio)!=null){
	    			HashMap indexData=resultadoActualizacion.getEntidadesAgrupadas(codMunicipio);
	    			if (indexData.get(claveBusqueda)!=null){
	    				//logger.info("Registro esta en la tabla de agrupadas");
	    				String claveElementoBusqueda=infoPadron.getCON3()+claveBusqueda;
	    				entidadAgrupada=resultadoActualizacion.getTotalInfoAgrupada(claveElementoBusqueda);
	    				//Si el registro no es una entidad agrupada no lo insertamos en la tabla de eiel_t_nucleo_encuestado_1
	    				if (entidadAgrupada==null)
	    					return false;	    			
	    			}
	    		}
	    		
	    		int totalPoblacion;
				int totalViviendas;				
				boolean bentidadAgrupada=false;
				if (entidadAgrupada!=null){
					totalPoblacion=(int)entidadAgrupada.getTotalPoblacion();
					totalViviendas=(int)entidadAgrupada.getTotalViviendas();
					bentidadAgrupada=true;
				}
				else{
					totalPoblacion=(int)infoPadron.getTotalPop();
					totalViviendas=(int)infoPadron.getTotalViv();
					bentidadAgrupada=false;
				}
				
	    		if (obj!=null){
	    			SaneamientoAutonomoEIEL pm = (SaneamientoAutonomoEIEL)obj; 
							    		
					if (infoPadron.getPrincipales()>=5){
						String cadena="Numero de Viviendas Principales>=5. SaneamientoAutonomoEIEL se mantiene:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						//resultadoActualizacion.append(cadena+"\n");
						//Actualizamos su informacion
						
					
						boolean actualizarRegistro=false;
						
						
						if (totalPoblacion!=pm.getPoblResidente()){
							cadena="Actualizando SaneamientoAutonomoEIEL. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Total Poblacion:"+pm.getPoblResidente()+"->"+totalPoblacion+ " Agrupado:"+bentidadAgrupada;
							resultadoActualizacion.append(cadena+"\n");
							pm.setPoblResidente(totalPoblacion);
							actualizarRegistro=true;
						}
						
						int poblacionEstacional=(int)(Math.ceil(totalPoblacion*PORCENTAJE_POBLACION_ESTACIONAL));
						if (poblacionEstacional!=pm.getPoblEstacional()){		
							pm.setPoblEstacional(poblacionEstacional);
							actualizarRegistro=true;
						}
						
						if (totalViviendas!=pm.getViviendas()){
							cadena="Actualizando SaneamientoAutonomoEIEL. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Total Viviendas:"+pm.getViviendas()+"->"+totalViviendas+ " Agrupado:"+bentidadAgrupada;
							resultadoActualizacion.append(cadena+"\n");

							pm.setViviendas(totalViviendas);
							actualizarRegistro=true;
						}
						
						if (actualizarRegistro){
							if (actualizar){
								InitEIEL.clienteLocalGISEIEL.insertarElemento(pm, null, tipo);
							}
							resultadoActualizacion.addElementUpdate(pm);	
						}
						else{
							cadena="Numero de Viviendas Principales>=5. SaneamientoAutonomoEIEL se mantiene y no se actualiza:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
							resultadoActualizacion.append(cadena+"\n");							
						}
						
		
					}
					else{
						String cadena="Numero de Viviendas Principales<5. Eliminando SaneamientoAutonomoEIEL:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						//logger.info(cadena);
						resultadoActualizacion.append(cadena+"\n");
						if (actualizar){
							
							InitEIEL.clienteLocalGISEIEL.eliminarElemento(pm, null, null, tipo);							
						}
						resultadoActualizacion.addElementBaja(pm);
	
					}
	    		}						
				else{
					
					if (infoPadron.getPrincipales()>=5){
						
						if (!resultadoActualizacion.isSaneamientoConectado(codMunicipio+claveBusqueda)){						
						
							String cadena="Numero de Viviendas Principales>=5 ("+(int)infoPadron.getPrincipales()+") Es preciso insertar a mano SaneamientoAutonomoEIEL:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
							//logger.info(cadena);
							resultadoActualizacion.append(cadena+"\n");
							
							SaneamientoAutonomoEIEL pm=new SaneamientoAutonomoEIEL();
							pm.setIdMunicipio(Integer.parseInt(fixedCodProvincia+infoPadron.getCON3()));
							pm.setCodINEProvincia(fixedCodProvincia);
							pm.setCodINEMunicipio(infoPadron.getCON3());
							pm.setCodINEEntidad(codEntidad);
							if (codPoblamiento.equals("99"))
				    			codPoblamiento="70";
							pm.setCodINENucleo(codPoblamiento);
							
							resultadoActualizacion.addElementAltaManual(pm);
						}
						else{
							String cadena="Numero de Viviendas Principales>=5 ("+(int)infoPadron.getPrincipales()+") El nucleo no tiene que estar en la tabla de SaneamientoAutonomoEIEL:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
							//logger.info(cadena);
							resultadoActualizacion.append(cadena+"\n");
						}
					}
				}
	    	}
	    	catch (Exception e){
	    		logger.error("Error",e);
	    		return false;
	    	}
		
    	return true;
	}
	
	/**
	 * Actualizacion de la tabla eiel_t_rb_serv
	 * @return
	 */
	private boolean actualizar_eiel_t_rb_serv(boolean actualizar,String tipo,InfoPadronEIEL infoPadron,Object obj,HashMap elementos_eiel_t_rb,
														ResultadoActualizacion resultadoActualizacion){
						
	    	try {
	    		
	    		if (infoPadron==null){
	    			resultadoActualizacion.addElementBajaManual(obj);
	    			return true;
	    		}
	    		
	    		String codEntidad=infoPadron.getP()+infoPadron.getES();
				String codPoblamiento=infoPadron.getND();
				String codMunicipio=fixedCodProvincia+infoPadron.getCON3();
	    		
				if (codPoblamiento.equals("99"))
	    			codPoblamiento="70";
	    		
	    		//Clave de busqueda
	    		String claveBusqueda=codEntidad+codPoblamiento;   
	
		
	    		
	    		EntidadAgrupada entidadAgrupada=null;
	    		
	    		if (resultadoActualizacion.getEntidadesAgrupadas(codMunicipio)!=null){
	    			HashMap indexData=resultadoActualizacion.getEntidadesAgrupadas(codMunicipio);
	    			if (indexData.get(claveBusqueda)!=null){
	    				//logger.info("Registro esta en la tabla de agrupadas");
	    				String claveElementoBusqueda=infoPadron.getCON3()+claveBusqueda;
	    				entidadAgrupada=resultadoActualizacion.getTotalInfoAgrupada(claveElementoBusqueda);
	    				//Si el registro no es una entidad agrupada no lo insertamos en la tabla de eiel_t_nucleo_encuestado_1
	    				if (entidadAgrupada==null)
	    					return false;	    			
	    			}
	    		}
	    		
	    		int totalPoblacion;
				int totalViviendas;				
				boolean bentidadAgrupada=false;
				if (entidadAgrupada!=null){
					totalPoblacion=(int)entidadAgrupada.getTotalPoblacion();
					totalViviendas=(int)entidadAgrupada.getTotalViviendas();
					bentidadAgrupada=true;
				}
				else{
					totalPoblacion=(int)infoPadron.getTotalPop();
					totalViviendas=(int)infoPadron.getTotalViv();
					bentidadAgrupada=false;
				}
				
	    		if (obj!=null){
	    			ServiciosRecogidaBasuraEIEL pm = (ServiciosRecogidaBasuraEIEL)obj; 
							    		
					if (infoPadron.getPrincipales()>=5){
						String cadena="Numero de Viviendas Principales>=5. ServiciosRecogidaBasuraEIEL se mantiene:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						//resultadoActualizacion.append(cadena+"\n");
						//Actualizamos su informacion
						
						//Incidencia 193
						/*En tabla “eiel_t_rb”, se recogen únicamente los núcleos que tienen recogida de basura.

						En la tabla “eiel_t_rb_serv”, se recogen todos los núcleos. Los que tienen recogida de basura, que existen por tanto en la tabla “eiel_t_rb”, los campos srb_viviendas_afec, srb_pob_res_afect y srb_pob_est_afect están a 0. 

						•	La actualización de padrón no añade núcleos, ni quita núcleos de nunguna de las dos tablas. 
						Únicamente actúa sobre la tabla “eiel_t_rb_serv”, de la siguiente manera:
						-	Si la tabla “eiel_t_rb” contiene el núcleo, en la tabla “eiel_t_rb_serv” no hace nada; 
						-	Si la tabla “eiel_t_rb” no contiene el núcleo, en la tabla “eiel_t_rb_serv” actualiza los datos de población y vivienda.
						Este comportamiento debe estar reflejado en la opción “test”, previo a la actualización del padrón
						*/
					
						boolean actualizarRegistro=false;
						
						/*if (pm.getPoblEstSinServicio()==0 && pm.getVivSinServicio()==0 && pm.getPoblResSinServicio()==0){
							//No hacer nada
						}
						else{*/
						String clave=fixedCodProvincia+"_"+infoPadron.getCON3()+"_"+codEntidad+"_"+codPoblamiento;

						if (elementos_eiel_t_rb.containsKey(clave)){
							cadena="Actualizando ServiciosRecogidaBasuraEIEL. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->El elemento existe en la tabla eiel_t_rb. No actualizamos la información";
							resultadoActualizacion.append(cadena+"\n");
							actualizarRegistro=false;
						}
						else{
							if (totalViviendas!=pm.getVivSinServicio()){
								cadena="Actualizando ServiciosRecogidaBasuraEIEL. "+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento)+"->Total Viviendas:"+pm.getVivSinServicio()+"->"+totalViviendas+ " Agrupado:"+bentidadAgrupada;
								resultadoActualizacion.append(cadena+"\n");
								pm.setVivSinServicio(totalViviendas);
								actualizarRegistro=true;
							}
									
							
							if (totalPoblacion!=pm.getPoblResSinServicio()){		
								pm.setPoblResSinServicio(totalPoblacion);
								actualizarRegistro=true;
							}
							
							int poblacionEstacional=(int)(Math.ceil(totalPoblacion*PORCENTAJE_POBLACION_ESTACIONAL));
							if (totalPoblacion!=pm.getPoblEstSinServicio()){		
								pm.setPoblEstSinServicio(poblacionEstacional);
								actualizarRegistro=true;
							}
							
							
							
							if (actualizarRegistro){
								if (actualizar){
									InitEIEL.clienteLocalGISEIEL.insertarElemento(pm, null, tipo);
								}
								resultadoActualizacion.addElementUpdate(pm);	
							}
							else{
								cadena="Numero de Viviendas Principales>=5. ServiciosRecogidaBasuraEIEL se mantiene y no se actualiza:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
								resultadoActualizacion.append(cadena+"\n");							
							}
							
						//}
						}
						
		
					}
					else{
						String cadena="Numero de Viviendas Principales<5. Desactivando la eliminacion del servicio ServiciosRecogidaBasuraEIEL:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						logger.info(cadena);
						resultadoActualizacion.append(cadena+"\n");

						/*String cadena="Numero de Viviendas Principales<5. Eliminando ServiciosRecogidaBasuraEIEL:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						//logger.info(cadena);
						resultadoActualizacion.append(cadena+"\n");
						if (actualizar){
							
							InitEIEL.clienteLocalGISEIEL.eliminarElemento(pm, null, null, tipo);							
						}
						resultadoActualizacion.addElementBaja(pm);*/
	
					}
	    		}						
				else{
					
					if (infoPadron.getPrincipales()>=5){
						String cadena="Numero de Viviendas Principales>=5 ("+(int)infoPadron.getPrincipales()+") Es preciso insertar a mano ServiciosRecogidaBasuraEIEL:"+getNombreNucleo(infoPadron.getCON3(),codEntidad,codPoblamiento);
						//logger.info(cadena);
						resultadoActualizacion.append(cadena+"\n");
						
						ServiciosRecogidaBasuraEIEL pm=new ServiciosRecogidaBasuraEIEL();
						pm.setIdMunicipio(Integer.parseInt(fixedCodProvincia+infoPadron.getCON3()));
						pm.setCodINEProvincia(fixedCodProvincia);
						pm.setCodINEMunicipio(infoPadron.getCON3());
						pm.setCodINEEntidad(codEntidad);
						if (codPoblamiento.equals("99"))
			    			codPoblamiento="70";
						pm.setCodINEPoblamiento(codPoblamiento);
						
						resultadoActualizacion.addElementAltaManual(pm);
					}
				}
	    	}
	    	catch (Exception e){
	    		logger.error("Error",e);
	    		return false;
	    	}
		
    	return true;
	}
	
	/**
	 * Actualizacion de la tabla eiel_t_mun_diseminados
	 * @return
	 */
	private boolean actualizar_eiel_t_mun_diseminados(boolean actualizar,String tipo,InfoPadronEIEL infoPadron,Object obj,
														ResultadoActualizacion resultadoActualizacion){
						
	    	try {
	    		
	     		
	    		if (infoPadron==null){
	    			resultadoActualizacion.addElementBajaManual(obj);
	    			return true;
				}
	    			    		
				String cadena;
				
				DiseminadosEIEL pm=null;
				if (obj!=null){
					pm = (DiseminadosEIEL)obj; 
					//cadena="Diseminado se mantiene:"+infoPadron.getCON3();
					//resultadoActualizacion.append(cadena+"\n");
					
	    		}						
				else{
					cadena="Diseminado no existe. Insertando Fila:"+infoPadron.getCON3();
					resultadoActualizacion.append(cadena+"\n");
					
					pm=new DiseminadosEIEL();
					pm.setCodINEProvincia(fixedCodProvincia);
					pm.setCodINEMunicipio(infoPadron.getCON3());
					
				}
				
				boolean actualizarRegistro=false;
					
				String codigoMunicipio=fixedCodProvincia+infoPadron.getCON3();
				int totalPoblacion=resultadoActualizacion.getTotalPoblacion(codigoMunicipio);
				int totalViviendas=resultadoActualizacion.getTotalViviendas(codigoMunicipio);
				
				int totalPoblacionEncuestada=resultadoActualizacion.getTotalPoblacionEncuestada(codigoMunicipio);
				int totalViviendasEncuestadas=resultadoActualizacion.getTotalViviendasEncuestadas(codigoMunicipio);
				
	    		
				int totalViviendasDiseminado=totalViviendas-totalViviendasEncuestadas;
				int totalPoblacionDiseminado=totalPoblacion-totalPoblacionEncuestada;
				
				if (pm.getPadron()!=totalPoblacionDiseminado){
					cadena="Actualizando Diseminado. "+infoPadron.getCON3()+"->Padron:"+pm.getPadron()+"->"+totalPoblacionDiseminado;
					resultadoActualizacion.append(cadena+"\n");
					pm.setPadron(totalPoblacionDiseminado); //padron_dis
					actualizarRegistro=true;
				}

				
				int totalPoblacionEstacional=(int)(Math.ceil(totalPoblacionDiseminado*PORCENTAJE_POBLACION_ESTACIONAL));
				if (pm.getPoblacionEstacional()!=totalPoblacionEstacional){
					cadena="Actualizando Diseminado. "+infoPadron.getCON3()+"->setPoblacionEstacional:"+pm.getPoblacionEstacional()+"->"+totalPoblacionEstacional;				
					resultadoActualizacion.append(cadena+"\n");
					pm.setPoblacionEstacional(totalPoblacionEstacional); //pob_estaci
					actualizarRegistro=true;
				}

				if (pm.getViviendasTotales()!=totalViviendasDiseminado){
					cadena="Actualizando Diseminado. "+infoPadron.getCON3()+"->setViviendasTotales:"+pm.getViviendasTotales()+"->"+totalViviendasDiseminado;				
					resultadoActualizacion.append(cadena+"\n");
					pm.setViviendasTotales(totalViviendasDiseminado); //viv_total
					actualizarRegistro=true;
				}
				
				int longDeficitarias=(int)(totalViviendasDiseminado*PORCENTAJE_DEFICITARIAS);
				pm.setLongDeficitariaAbast(longDeficitarias);//aag_l_defi
				
				pm.setViviendasDeficitAbast(totalViviendasDiseminado); //aag_v_defi
				pm.setPoblacionResidenteDefAbast(totalPoblacionDiseminado); //aag_pr_def				
				pm.setPoblacionEstacionalDefAbast(totalPoblacionEstacional); //aag_pe_def				
				pm.setPoblacionResidenteAbastAuto(totalPoblacionDiseminado);    //aau_pob_re
				pm.setPoblacionEstacionalAbastAuto(totalPoblacionEstacional);    //aau_pob_es
				pm.setViviendasDefAbastAuto(totalViviendasDiseminado); //aau_def_vi
				pm.setPoblacionResidenteDefAbastAuto(totalPoblacionDiseminado); //aau_def_re
				
				pm.setPoblacionEstacionalDefAbastAuto(totalPoblacionEstacional); //aau_def_es
				
				
				pm.setViviendasDefSaneam(totalViviendasDiseminado); //syd_v_defi
				pm.setPoblacionResidenteDefSaneam(totalPoblacionDiseminado); //syd_pr_def
				pm.setPoblacionEstacionalDefSaneam(totalPoblacionEstacional); //syd_pe_def
				
				pm.setViviendasSaneamientoAuto(totalViviendasDiseminado);//sau_vivien 
				pm.setPoblacionEstacionalSaneamAuto(totalPoblacionEstacional);//sau_pob_es
				
				pm.setVivendasAbastecimientoAuto(totalViviendasDiseminado); //aau_vivien
				
				pm.setPoblacionResidenteSaneamAuto(totalPoblacionDiseminado); //sau_pob_re
				
				if (actualizarRegistro){
					if (actualizar){
						InitEIEL.clienteLocalGISEIEL.insertarElemento(pm, null, tipo);
					}
					if (obj!=null)					
						resultadoActualizacion.addElementUpdate(pm);
					else
						resultadoActualizacion.addElementAlta(pm);
						
				}
				else{
					cadena="Diseminado se mantiene y no se actualiza:"+infoPadron.getCON3();
					resultadoActualizacion.append(cadena+"\n");							
				}
				
	    						
	    	}
	    	catch (Exception e){
	    		logger.error("Error",e);
	    		return false;
	    	}
		
    	return true;
	}
	
	
	/**
	 * Obtiene los elementos a actualizar.
	 * @param infoPoblamiento
	 * @param nombreTabla
	 * @param codProvincia
	 * @param codMunicipio
	 * @return
	 */
	private HashMap getElementosActualizar(HashMap hElementosActualizar,HashMap hElementosActualizarIndexados,
											HashMap hElementosSobrantesIndexados,String tipo,String codProvincia,String codMunicipio){
		
		ArrayList lstElements=new ArrayList();
		
		//El nombre de la tabla puede provocar algun problema.
		String nombreTabla=InitEIEL.tablasAlfanumericas.get(tipo);   
		nombreTabla=null;
		
		try {
			if (hElementosActualizar.get(codMunicipio)==null){
				
				String filter="";
				filter=getFilter(tipo,nombreTabla,codProvincia,codMunicipio);
				//Recuperamos todos y filtramos en memoria para que vaya mas rapido
				lstElements = InitEIEL.clienteLocalGISEIEL.getLstElementos(filter, tipo,
						false,0,nombreTabla,null,codMunicipio,null);
				hElementosActualizar.put(codMunicipio,lstElements);
			}
			else{
				lstElements=(ArrayList)hElementosActualizar.get(codMunicipio);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		
		HashMap indexData=null;
		if (hElementosActualizarIndexados.get(codMunicipio)==null){
			indexData=new HashMap();
			//Los almacenamos en una tabla hash para hacer la busqueda mas rapida			
			for (int j=0;j<lstElements.size();j++){
				
				String clave=null;
				Object data=null;
				if (tipo.equals(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS)){
					 data = (PadronMunicipiosEIEL)lstElements.get(j);
					 clave=codMunicipio;				 
				}
				else if (tipo.equals(ConstantesLocalGISEIEL.POBLAMIENTO)){
					 data = (PoblamientoEIEL)lstElements.get(j);
					 clave=((PoblamientoEIEL)data).getCodINEEntidad()+((PoblamientoEIEL)data).getCodINEPoblamiento();				 			
				}
				else if (tipo.equals(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES)){
					 data = (EntidadesSingularesEIEL)lstElements.get(j);
					 clave=((EntidadesSingularesEIEL)data).getCodINEEntidad();				 			
				}
				else if (tipo.equals(ConstantesLocalGISEIEL.C_NUCLEOS_POBLACION)){
					 data = (NucleosPoblacionEIEL)lstElements.get(j);
					 clave=((NucleosPoblacionEIEL)data).getCodINEEntidad()+((NucleosPoblacionEIEL)data).getCodINEPoblamiento();				 			
				}
				else if (tipo.equals(ConstantesLocalGISEIEL.AGRUPACIONES6000)){
					 data = (EntidadesAgrupadasEIEL)lstElements.get(j);
					 clave=((EntidadesAgrupadasEIEL)data).getCodEntidad()+((EntidadesAgrupadasEIEL)data).getCodNucleo();				 			
				}
				else if (tipo.equals(ConstantesLocalGISEIEL.ENCUESTADOS1)){
					 data = (Encuestados1EIEL)lstElements.get(j);
					 clave=((Encuestados1EIEL)data).getCodINEEntidad()+((Encuestados1EIEL)data).getCodINEPoblamiento();				 			
				}
				else if (tipo.equals(ConstantesLocalGISEIEL.ENCUESTADOS2)){
					 data = (Encuestados2EIEL)lstElements.get(j);
					 clave=((Encuestados2EIEL)data).getCodINEEntidad()+((Encuestados2EIEL)data).getCodINEPoblamiento();				 			
				}
				else if (tipo.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS)){
					 data = (ServiciosAbastecimientosEIEL)lstElements.get(j);
					 clave=((ServiciosAbastecimientosEIEL)data).getCodINEEntidad()+((ServiciosAbastecimientosEIEL)data).getCodINEPoblamiento();				 			
				}
				else if (tipo.equals(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO)){
					 data = (AbastecimientoAutonomoEIEL)lstElements.get(j);
					 clave=((AbastecimientoAutonomoEIEL)data).getCodINEEntidad()+((AbastecimientoAutonomoEIEL)data).getCodINENucleo();				 			
				}
				else if (tipo.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO)){
					 data = (ServiciosSaneamientoEIEL)lstElements.get(j);
					 clave=((ServiciosSaneamientoEIEL)data).getCodINEEntidad()+((ServiciosSaneamientoEIEL)data).getCodINEPoblamiento();				 			
				}
				else if (tipo.equals(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO)){
					 data = (SaneamientoAutonomoEIEL)lstElements.get(j);
					 clave=((SaneamientoAutonomoEIEL)data).getCodINEEntidad()+((SaneamientoAutonomoEIEL)data).getCodINENucleo();				 			
				}
				else if (tipo.equals(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA)){
					 data = (ServiciosRecogidaBasuraEIEL)lstElements.get(j);
					 clave=((ServiciosRecogidaBasuraEIEL)data).getCodINEEntidad()+((ServiciosRecogidaBasuraEIEL)data).getCodINEPoblamiento();				 			
				}
				else if (tipo.equals(ConstantesLocalGISEIEL.DISEMINADOS)){
					 data = (DiseminadosEIEL)lstElements.get(j);
					 clave=codMunicipio;	
				}

				indexData.put(clave,data);
			}
			hElementosActualizarIndexados.put(codMunicipio,indexData);
			
			HashMap indexdataClone=(HashMap)indexData.clone();
			hElementosSobrantesIndexados.put(codMunicipio,indexdataClone);
		}
		else{
			indexData=(HashMap)hElementosActualizarIndexados.get(codMunicipio);
		}
		
		return indexData;
	}
	
	/**
	 * 
	 * @param filterUpdate
	 * @return
	 */
	private ArrayList getListPadron(String tipo,String filterUpdate,String nombreTabla,ResultadoActualizacion resultadoActualizacion) throws PadronException{
		//Obtenemos los datos a actualizar de la Base de Datos
		String tipoUpdate=ConstantesLocalGISEIEL.INFO_PADRON;
		//String nombreTablaUpdate=ConstantesLocalGISEIEL.tablasAlfanumericas.get(tipoUpdate); 				
		ArrayList elementosPadron=new ArrayList();
		try {
			 elementosPadron= InitEIEL.clienteLocalGISEIEL.getLstElementos(filterUpdate, tipoUpdate,
					false,0,nombreTabla,null,null,null);
			 
			String cadena="\n\nTipo:"+tipo+"->Lista de elementos de padron a contemplar:"+elementosPadron.size()+"\n";
			 resultadoActualizacion.append("----------------\n");
			resultadoActualizacion.append(cadena);
		} catch (Exception e1) {
			//logger.error("Error:"+e1);
			elementosPadron=null;
			throw new PadronException(e1);
		}
		return elementosPadron;
	}
	
	/**
	 * 
	 * @param filterUpdate
	 * @return
	 */
	private HashMap getList_eiel_t_rb(String filter) throws PadronException{
		//Obtenemos los datos a actualizar de la Base de Datos
		String tipo=ConstantesLocalGISEIEL.RECOGIDA_BASURAS;
		String nombreTabla="public.eiel_t_rb";
		ArrayList elementos=new ArrayList();
		HashMap elementosHash=new HashMap();
		try {
			 elementos= InitEIEL.clienteLocalGISEIEL.getLstElementos(null, tipo,
					false,0,nombreTabla,null,"TODOS",null);
			 Iterator it=elementos.iterator();
			 while (it.hasNext()){
				 RecogidaBasurasEIEL recogida=(RecogidaBasurasEIEL)it.next();
				 String clave=recogida.getCodINEProvincia()+"_"+recogida.getCodINEMunicipio()+"_"+recogida.getCodINEEntidad()+"_"+recogida.getCodINEPoblamiento();
				 elementosHash.put(clave, recogida);
				 
			 }
		} catch (Exception e1) {
			throw new PadronException(e1);
		}
		return elementosHash;
	}
	
	
	/**
	 * Filtro de busqueda
	 * @param nombreTabla
	 * @param codProvincia
	 * @param codMunicipio
	 * @return
	 */
	private String getFilter(String tipo,String nombreTabla,String codProvincia,String codMunicipio){
		
		String filter="";
		if (nombreTabla!=null){
    		
    		filter = " ("+nombreTabla+".revision_expirada="+ConstantesLocalGISEIEL.REVISION_VALIDA;
    		filter += " or "+nombreTabla+".revision_expirada="+ConstantesLocalGISEIEL.REVISION_TEMPORAL;				
    		filter += " or "+nombreTabla+".revision_expirada="+ConstantesLocalGISEIEL.REVISION_PUBLICABLE;				
    		filter += " or "+nombreTabla+".revision_expirada="+ConstantesLocalGISEIEL.REVISION_PUBLICABLE_MOVILIDAD;
    		filter += " or "+nombreTabla+".revision_expirada="+ConstantesLocalGISEIEL.REVISION_BORRABLE+")";
		}
		else{
			filter = " (revision_expirada="+ConstantesLocalGISEIEL.REVISION_VALIDA;
    		filter += " or revision_expirada="+ConstantesLocalGISEIEL.REVISION_TEMPORAL;				
    		filter += " or revision_expirada="+ConstantesLocalGISEIEL.REVISION_PUBLICABLE;				
    		filter += " or revision_expirada="+ConstantesLocalGISEIEL.REVISION_PUBLICABLE_MOVILIDAD;
    		filter += " or revision_expirada="+ConstantesLocalGISEIEL.REVISION_BORRABLE+")";
		}
		
		if (tipo.equals(ConstantesLocalGISEIEL.AGRUPACIONES6000))
				filter+=" and (codmunicipio='"+codMunicipio.substring(2,5)+"')";
		else
			filter+=" and (codprov='"+codProvincia+"' and codmunic='"+codMunicipio.substring(2,5)+"')";
		return filter;
	}
	
	/**
	 * Obtiene el nombre del nucleo
	 * @param codMunicipio
	 * @param codentidad
	 * @param codnucleo
	 * @return
	 */
	private String getNombreNucleo(String codMunicipio,String codentidad,String codnucleo){
		 
		String claveBusquedaReal="";
		if (codnucleo=="70")
			claveBusquedaReal=codMunicipio+"_"+codentidad+"_"+"99";
		else
			claveBusquedaReal=codMunicipio+"_"+codentidad+"_"+codnucleo;
		
		String nombreNucleo=claveBusquedaReal;
				
		 aplicacion= (AppContext) AppContext.getApplicationContext();

		 HashMap map=(HashMap)aplicacion.getBlackboard().get("HASH_NUCLEOS_ALL_MUNICIPIOS");
		 
		 String claveBusqueda=codMunicipio+"_"+codentidad+"_"+codnucleo;
		 
		 if (map!=null){
			 LCGNucleoEIEL nucleoEIEL= (LCGNucleoEIEL)map.get(claveBusqueda);
			 if (nucleoEIEL!=null){
				 nombreNucleo="("+claveBusquedaReal+") "+nucleoEIEL.getDenominacion() ;
			 }
		 }
		 
		 return nombreNucleo;
	 }
	
	private void showResultados(ResultadoActualizacion resultadoActualizacion){
		
		
		logger.info("---------------------------------------------------------");		
		logger.info("Resultado Actualizacion:\n"+resultadoActualizacion.print());
        logger.info("Resultado Actualizacion Actualizaciones:"+resultadoActualizacion.getElementsUpdate().size());
        logger.info("Resultado Actualizacion Inserciones:"+resultadoActualizacion.getElementsAlta().size());
        for (int i=0;i<resultadoActualizacion.getElementsAlta().size();i++){
        	Object obj=resultadoActualizacion.getElementsAlta().get(i);
        	if (obj instanceof PadronMunicipiosEIEL){logger.info("\tPadronMunicipiosEIEL:->"+(PadronMunicipiosEIEL)obj);}
        	else if (obj instanceof PoblamientoEIEL){logger.info("\tPoblamientoEIEL:->"+(PoblamientoEIEL)obj);}
        	else if (obj instanceof EntidadesSingularesEIEL){logger.info("\tEntidadesSingularesEIEL:->"+(EntidadesSingularesEIEL)obj);}
        	else if (obj instanceof NucleosPoblacionEIEL){logger.info("\tNucleosPoblacionEIEL:->"+(NucleosPoblacionEIEL)obj);}
        	else if (obj instanceof EntidadesAgrupadasEIEL){logger.info("E\tntidadesAgrupadasEIEL:->"+(EntidadesAgrupadasEIEL)obj);}
        	else if (obj instanceof Encuestados1EIEL){logger.info("\tEncuestado1EIEL:->"+(Encuestados1EIEL)obj);}
        	else if (obj instanceof ServiciosAbastecimientosEIEL){logger.info("\tServiciosAbastecimientosEIEL:->"+(ServiciosAbastecimientosEIEL)obj);}
        	else if (obj instanceof AbastecimientoAutonomoEIEL){logger.info("\tAbastecimientoAutonomoEIEL:->"+(AbastecimientoAutonomoEIEL)obj);}
        	else if (obj instanceof ServiciosSaneamientoEIEL){logger.info("\tServiciosSaneamientoEIEL:->"+(ServiciosSaneamientoEIEL)obj);}
        	else if (obj instanceof SaneamientoAutonomoEIEL){logger.info("\tSaneamientoAutonomoEIEL:->"+(AbastecimientoAutonomoEIEL)obj);}
        	else if (obj instanceof ServiciosRecogidaBasuraEIEL){logger.info("\tServiciosRecogidaBasuraEIEL:->"+(ServiciosRecogidaBasuraEIEL)obj);}
        	else if (obj instanceof DiseminadosEIEL){logger.info("\tDiseminadosEIEL:->"+(DiseminadosEIEL)obj);}

        }
        logger.info("Resultado Actualizacion Inserciones Manuales:"+resultadoActualizacion.getElementsAltaManual().size());
        for (int i=0;i<resultadoActualizacion.getElementsAltaManual().size();i++){
        	Object obj=resultadoActualizacion.getElementsAltaManual().get(i);
        	if (obj instanceof PadronMunicipiosEIEL){logger.info("\tPadronMunicipiosEIEL:->"+(PadronMunicipiosEIEL)obj);}
        	else if (obj instanceof PoblamientoEIEL){logger.info("\tPoblamientoEIEL:->"+(PoblamientoEIEL)obj);}
        	else if (obj instanceof EntidadesSingularesEIEL){logger.info("\tEntidadesSingularesEIEL:->"+(EntidadesSingularesEIEL)obj);}
        	else if (obj instanceof NucleosPoblacionEIEL){logger.info("\tNucleosPoblacionEIEL:->"+(NucleosPoblacionEIEL)obj);}
        	else if (obj instanceof EntidadesAgrupadasEIEL){logger.info("\tEntidadesAgrupadasEIEL:->"+(EntidadesAgrupadasEIEL)obj);}
        	else if (obj instanceof Encuestados1EIEL){logger.info("\tEncuestado1EIEL:->"+(Encuestados1EIEL)obj);}
        	else if (obj instanceof ServiciosAbastecimientosEIEL){logger.info("\tServiciosAbastecimientosEIEL:->"+(ServiciosAbastecimientosEIEL)obj);}
        	else if (obj instanceof AbastecimientoAutonomoEIEL){logger.info("\tAbastecimientoAutonomoEIEL:->"+(AbastecimientoAutonomoEIEL)obj);}
        	else if (obj instanceof ServiciosSaneamientoEIEL){logger.info("\tServiciosSaneamientoEIEL:->"+(ServiciosSaneamientoEIEL)obj);}
        	else if (obj instanceof SaneamientoAutonomoEIEL){logger.info("\tSaneamientoAutonomoEIEL:->"+(SaneamientoAutonomoEIEL)obj);}
        	else if (obj instanceof ServiciosRecogidaBasuraEIEL){logger.info("\tServiciosRecogidaBasuraEIEL:->"+(ServiciosRecogidaBasuraEIEL)obj);}
        	else if (obj instanceof DiseminadosEIEL){logger.info("\tDiseminadosEIEL:->"+(DiseminadosEIEL)obj);}

        }
        logger.info("Resultado Actualizacion Bajas:"+resultadoActualizacion.getElementsBaja().size());
        for (int i=0;i<resultadoActualizacion.getElementsBaja().size();i++){
        	Object obj=resultadoActualizacion.getElementsBaja().get(i);
        	if (obj instanceof PadronMunicipiosEIEL){logger.info("\tPadronMunicipiosEIEL:->"+(PadronMunicipiosEIEL)obj);}
        	else if (obj instanceof PoblamientoEIEL){logger.info("\tPoblamientoEIEL:->"+(PoblamientoEIEL)obj);}
        	else if (obj instanceof EntidadesSingularesEIEL){logger.info("\tEntidadesSingularesEIEL:->"+(EntidadesSingularesEIEL)obj);}
        	else if (obj instanceof NucleosPoblacionEIEL){logger.info("\tNucleosPoblacionEIEL:->"+(NucleosPoblacionEIEL)obj);}
        	else if (obj instanceof EntidadesAgrupadasEIEL){logger.info("\tEntidadesAgrupadasEIEL:->"+(EntidadesAgrupadasEIEL)obj);}
        	else if (obj instanceof Encuestados1EIEL){logger.info("\tEncuestado1EIEL:->"+(Encuestados1EIEL)obj);}
        	else if (obj instanceof ServiciosAbastecimientosEIEL){logger.info("\tServiciosAbastecimientosEIEL:->"+(ServiciosAbastecimientosEIEL)obj);}
        	else if (obj instanceof AbastecimientoAutonomoEIEL){logger.info("\tAbastecimientoAutonomoEIEL:->"+(AbastecimientoAutonomoEIEL)obj);}
        	else if (obj instanceof ServiciosSaneamientoEIEL){logger.info("\tServiciosSaneamientoEIEL:->"+(ServiciosSaneamientoEIEL)obj);}
        	else if (obj instanceof SaneamientoAutonomoEIEL){logger.info("\tSaneamientoAutonomoEIEL:->"+(SaneamientoAutonomoEIEL)obj);}
        	else if (obj instanceof ServiciosRecogidaBasuraEIEL){logger.info("\tServiciosRecogidaBasuraEIEL:->"+(ServiciosRecogidaBasuraEIEL)obj);}
        	else if (obj instanceof DiseminadosEIEL){logger.info("\tDiseminadosEIEL:->"+(DiseminadosEIEL)obj);}

        }
        logger.info("Resultado Actualizacion Baja Manual:"+resultadoActualizacion.getElementsBajaManual().size());
        for (int i=0;i<resultadoActualizacion.getElementsBajaManual().size();i++){
        	Object obj=resultadoActualizacion.getElementsBajaManual().get(i);
        	if (obj instanceof PadronMunicipiosEIEL){logger.info("\tPadronMunicipiosEIEL:->"+(PadronMunicipiosEIEL)obj);}
        	else if (obj instanceof PoblamientoEIEL){logger.info("\tPoblamientoEIEL:->"+(PoblamientoEIEL)obj);}
        	else if (obj instanceof EntidadesSingularesEIEL){logger.info("\tEntidadesSingularesEIEL:->"+(EntidadesSingularesEIEL)obj);}
        	else if (obj instanceof NucleosPoblacionEIEL){logger.info("\tNucleosPoblacionEIEL:->"+(NucleosPoblacionEIEL)obj);}
        	else if (obj instanceof EntidadesAgrupadasEIEL){logger.info("\tEntidadesAgrupadasEIEL:->"+(EntidadesAgrupadasEIEL)obj);}
        	else if (obj instanceof Encuestados1EIEL){logger.info("\tEncuestado1EIEL:->"+(Encuestados1EIEL)obj);}
        	else if (obj instanceof ServiciosAbastecimientosEIEL){logger.info("\tServiciosAbastecimientosEIEL:->"+(ServiciosAbastecimientosEIEL)obj);}
        	else if (obj instanceof AbastecimientoAutonomoEIEL){logger.info("\tAbastecimientoAutonomoEIEL:->"+(AbastecimientoAutonomoEIEL)obj);}
        	else if (obj instanceof ServiciosSaneamientoEIEL){logger.info("\tServiciosSaneamientoEIEL:->"+(ServiciosSaneamientoEIEL)obj);}
        	else if (obj instanceof SaneamientoAutonomoEIEL){logger.info("\tSaneamientoAutonomoEIEL:->"+(SaneamientoAutonomoEIEL)obj);}
        	else if (obj instanceof ServiciosRecogidaBasuraEIEL){logger.info("\tServiciosRecogidaBasuraEIEL:->"+(ServiciosRecogidaBasuraEIEL)obj);}
        	else if (obj instanceof DiseminadosEIEL){logger.info("\tDiseminadosEIEL:->"+(DiseminadosEIEL)obj);}

        }
        
        if (path!=null){
	        try {
	        	FileWriter outFile;
	        	if (actualizar)
	        		outFile = new FileWriter(path+File.separator+"Padron_"+añoPadron+".log",false);
	        	else
	        		outFile = new FileWriter(path+File.separator+"PadronTest_"+añoPadron+".log",false);
				PrintWriter out = new PrintWriter(outFile,true);
				out.println("\n\n---------------------------------------------------------\n");
				out.println("\n\nResultado Actualizacion:\n"+resultadoActualizacion.print());
				out.println("Resultado Actualizacion Actualizaciones:"+resultadoActualizacion.getElementsUpdate().size());
				out.println("Resultado Actualizacion Inserciones:"+resultadoActualizacion.getElementsAlta().size());
		        for (int i=0;i<resultadoActualizacion.getElementsAlta().size();i++){
		        	Object obj=resultadoActualizacion.getElementsAlta().get(i);
		        	
		        	if (obj instanceof PadronMunicipiosEIEL){out.println("\tPadronMunicipiosEIEL:->"+(PadronMunicipiosEIEL)obj);}
		        	else if (obj instanceof PoblamientoEIEL){out.println("\tPoblamientoEIEL:->"+(PoblamientoEIEL)obj);}
		        	else if (obj instanceof EntidadesSingularesEIEL){out.println("\tEntidadesSingularesEIEL:->"+(EntidadesSingularesEIEL)obj);}
		        	else if (obj instanceof NucleosPoblacionEIEL){out.println("\tNucleosPoblacionEIEL:->"+(NucleosPoblacionEIEL)obj);}
		        	else if (obj instanceof EntidadesAgrupadasEIEL){out.println("\tEntidadesAgrupadasEIEL:->"+(EntidadesAgrupadasEIEL)obj);}
		        	else if (obj instanceof Encuestados1EIEL){out.println("\tEncuestado1EIEL:->"+(Encuestados1EIEL)obj);}
		        	else if (obj instanceof ServiciosAbastecimientosEIEL){out.println("\tServiciosAbastecimientosEIEL:->"+(ServiciosAbastecimientosEIEL)obj);}
		        	else if (obj instanceof AbastecimientoAutonomoEIEL){out.println("\tAbastecimientoAutonomoEIEL:->"+(AbastecimientoAutonomoEIEL)obj);}
		        	else if (obj instanceof ServiciosSaneamientoEIEL){out.println("\tServiciosSaneamientoEIEL:->"+(ServiciosSaneamientoEIEL)obj);}
		        	else if (obj instanceof SaneamientoAutonomoEIEL){out.println("\tSaneamientoAutonomoEIEL:->"+(SaneamientoAutonomoEIEL)obj);}
		        	else if (obj instanceof ServiciosRecogidaBasuraEIEL){out.println("\tServiciosRecogidaBasuraEIEL:->"+(ServiciosRecogidaBasuraEIEL)obj);}
		        	else if (obj instanceof DiseminadosEIEL){out.println("\tDiseminadosEIEL:->"+(DiseminadosEIEL)obj);}
		        
		        }
		        out.println("Resultado Actualizacion Inserciones Manuales:"+resultadoActualizacion.getElementsAltaManual().size());
		        for (int i=0;i<resultadoActualizacion.getElementsAltaManual().size();i++){
		        	Object obj=resultadoActualizacion.getElementsAltaManual().get(i);
		        	
		        	if (obj instanceof PadronMunicipiosEIEL){out.println("\tPadronMunicipiosEIEL:->"+(PadronMunicipiosEIEL)obj);}
		        	else if (obj instanceof PoblamientoEIEL){out.println("\tPoblamientoEIEL:->"+(PoblamientoEIEL)obj);}
		        	else if (obj instanceof EntidadesSingularesEIEL){out.println("\tEntidadesSingularesEIEL:->"+(EntidadesSingularesEIEL)obj);}
		        	else if (obj instanceof NucleosPoblacionEIEL){out.println("\tNucleosPoblacionEIEL:->"+(NucleosPoblacionEIEL)obj);}
		        	else if (obj instanceof EntidadesAgrupadasEIEL){out.println("\tEntidadesAgrupadasEIEL:->"+(EntidadesAgrupadasEIEL)obj);}
		        	else if (obj instanceof Encuestados1EIEL){out.println("\tEncuestado1EIEL:->"+(Encuestados1EIEL)obj);}
		        	else if (obj instanceof ServiciosAbastecimientosEIEL){out.println("\tServiciosAbastecimientosEIEL:->"+(ServiciosAbastecimientosEIEL)obj);}
		        	else if (obj instanceof AbastecimientoAutonomoEIEL){out.println("\tAbastecimientoAutonomoEIEL:->"+(AbastecimientoAutonomoEIEL)obj);}
		        	else if (obj instanceof ServiciosSaneamientoEIEL){out.println("\tServiciosSaneamientoEIEL:->"+(ServiciosSaneamientoEIEL)obj);}
		        	else if (obj instanceof SaneamientoAutonomoEIEL){out.println("\tSaneamientoAutonomoEIEL:->"+(SaneamientoAutonomoEIEL)obj);}
		        	else if (obj instanceof ServiciosRecogidaBasuraEIEL){out.println("\tServiciosRecogidaBasuraEIEL:->"+(ServiciosRecogidaBasuraEIEL)obj);}
		        	else if (obj instanceof DiseminadosEIEL){out.println("\tDiseminadosEIEL:->"+(DiseminadosEIEL)obj);}
		        
		        }
		        out.println("Resultado Actualizacion Bajas:"+resultadoActualizacion.getElementsBaja().size());
		        for (int i=0;i<resultadoActualizacion.getElementsBaja().size();i++){
		        	Object obj=resultadoActualizacion.getElementsBaja().get(i);
		        	
		        	if (obj instanceof PadronMunicipiosEIEL){out.println("\tPadronMunicipiosEIEL:->"+(PadronMunicipiosEIEL)obj);}
		        	else if (obj instanceof PoblamientoEIEL){out.println("\tPoblamientoEIEL:->"+(PoblamientoEIEL)obj);}
		        	else if (obj instanceof EntidadesSingularesEIEL){out.println("\tEntidadesSingularesEIEL:->"+(EntidadesSingularesEIEL)obj);}
		        	else if (obj instanceof NucleosPoblacionEIEL){out.println("\tNucleosPoblacionEIEL:->"+(NucleosPoblacionEIEL)obj);}
		        	else if (obj instanceof EntidadesAgrupadasEIEL){out.println("\tEntidadesAgrupadasEIEL:->"+(EntidadesAgrupadasEIEL)obj);}
		        	else if (obj instanceof Encuestados1EIEL){out.println("\tEncuestado1EIEL:->"+(Encuestados1EIEL)obj);}
		        	else if (obj instanceof ServiciosAbastecimientosEIEL){out.println("\tServiciosAbastecimientosEIEL:->"+(ServiciosAbastecimientosEIEL)obj);}
		        	else if (obj instanceof AbastecimientoAutonomoEIEL){out.println("\tAbastecimientoAutonomoEIEL:->"+(AbastecimientoAutonomoEIEL)obj);}
		        	else if (obj instanceof ServiciosSaneamientoEIEL){out.println("\tServiciosSaneamientoEIEL:->"+(ServiciosSaneamientoEIEL)obj);}
		        	else if (obj instanceof SaneamientoAutonomoEIEL){out.println("\tSaneamientoAutonomoEIEL:->"+(SaneamientoAutonomoEIEL)obj);}
		        	else if (obj instanceof ServiciosRecogidaBasuraEIEL){out.println("\tServiciosRecogidaBasuraEIEL:->"+(ServiciosRecogidaBasuraEIEL)obj);}
		        	else if (obj instanceof DiseminadosEIEL){out.println("\tDiseminadosEIEL:->"+(DiseminadosEIEL)obj);}
		        
		        }
		        out.println("Resultado Actualizacion Bajas Manual:"+resultadoActualizacion.getElementsBajaManual().size());
		        for (int i=0;i<resultadoActualizacion.getElementsBajaManual().size();i++){
		        	Object obj=resultadoActualizacion.getElementsBajaManual().get(i);
		        	
		        	if (obj instanceof PadronMunicipiosEIEL){out.println("\tPadronMunicipiosEIEL:->"+(PadronMunicipiosEIEL)obj);}
		        	else if (obj instanceof PoblamientoEIEL){out.println("\tPoblamientoEIEL:->"+(PoblamientoEIEL)obj);}
		        	else if (obj instanceof EntidadesSingularesEIEL){out.println("\tEntidadesSingularesEIEL:->"+(EntidadesSingularesEIEL)obj);}
		        	else if (obj instanceof NucleosPoblacionEIEL){out.println("\tNucleosPoblacionEIEL:->"+(NucleosPoblacionEIEL)obj);}
		        	else if (obj instanceof EntidadesAgrupadasEIEL){out.println("\tEntidadesAgrupadasEIEL:->"+(EntidadesAgrupadasEIEL)obj);}
		        	else if (obj instanceof Encuestados1EIEL){out.println("\tEncuestado1EIEL:->"+(Encuestados1EIEL)obj);}
		        	else if (obj instanceof ServiciosAbastecimientosEIEL){out.println("\tServiciosAbastecimientosEIEL:->"+(ServiciosAbastecimientosEIEL)obj);}
		        	else if (obj instanceof AbastecimientoAutonomoEIEL){out.println("\tAbastecimientoAutonomoEIEL:->"+(AbastecimientoAutonomoEIEL)obj);}
		        	else if (obj instanceof ServiciosSaneamientoEIEL){out.println("\tServiciosSaneamientoEIEL:->"+(ServiciosSaneamientoEIEL)obj);}
		        	else if (obj instanceof SaneamientoAutonomoEIEL){out.println("\tSaneamientoAutonomoEIEL:->"+(SaneamientoAutonomoEIEL)obj);}
		        	else if (obj instanceof ServiciosRecogidaBasuraEIEL){out.println("\tServiciosRecogidaBasuraEIEL:->"+(ServiciosRecogidaBasuraEIEL)obj);}
		        	else if (obj instanceof DiseminadosEIEL){out.println("\tDiseminadosEIEL:->"+(DiseminadosEIEL)obj);}
		        
		        }
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	public static void main(String args[]){
		
		double valor=2.7;
		
		int valorInviernoOriginal=(int)(Math.ceil(valor));
		
		double consumI = (double)(valor);
		int valorInvierno = (int)Math.floor(consumI);
		System.out.println("valorInviernoOriginal:"+valorInviernoOriginal);
		System.out.println("valorInviernoOriginal2:"+consumI);
		System.out.println("valorInviernoOriginal2:"+valorInvierno);
	}
}
