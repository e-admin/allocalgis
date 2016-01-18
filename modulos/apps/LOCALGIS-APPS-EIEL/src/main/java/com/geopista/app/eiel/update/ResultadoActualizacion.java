/**
 * ResultadoActualizacion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.update;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultadoActualizacion {

	
	private ArrayList elementosInsertar;
	private ArrayList elementosInsertarManual;
	private ArrayList elementosBaja;
	private ArrayList elementosBajaManual;
	private ArrayList elementosUpdate;
	
	private StringBuffer sb;
	private HashMap indexData;
	
	private EntidadesAgrupadas entidadesAgrupadas;
	
	
	private HashMap totalPoblacion;
	private HashMap totalViviendas;
	private HashMap totalPoblacionEncuestada;
	private HashMap totalViviendasEncuestadas;
	
	private HashMap saneamientosConectados;
	private HashMap abastecimientosConectados;
	
	public ResultadoActualizacion(){
		elementosInsertar=new ArrayList();
		elementosInsertarManual=new ArrayList();
		elementosUpdate=new ArrayList();
		elementosBaja=new ArrayList();
		elementosBajaManual=new ArrayList();
		indexData=new HashMap();
		sb=new StringBuffer();
		entidadesAgrupadas=new EntidadesAgrupadas();
		
		indexData=new HashMap();
		
		totalPoblacion=new HashMap();
		totalViviendas=new HashMap();

		
		totalPoblacionEncuestada=new HashMap();
		totalViviendasEncuestadas=new HashMap();

		saneamientosConectados=new HashMap();
		abastecimientosConectados=new HashMap();
		
	}
	
	public void addElementAlta(Object obj){
		elementosInsertar.add(obj);
	}
	
	public ArrayList getElementsAlta(){
		return elementosInsertar;
	}
	
	public void addElementAltaManual(Object obj){
		elementosInsertarManual.add(obj);
	}
	
	public ArrayList getElementsAltaManual(){
		return elementosInsertarManual;
	}

	public void addElementUpdate(Object obj){
		elementosUpdate.add(obj);
	}
	
	public ArrayList getElementsUpdate(){
		return elementosUpdate;
	}

	public ArrayList getElementsBaja(){
		return elementosBaja;
	}
	
	public void addElementBaja(Object obj){
		elementosBaja.add(obj);
	}
	
	public ArrayList getElementsBajaManual(){
		return elementosBajaManual;
	}
	
	public void addElementBajaManual(Object obj){
		elementosBajaManual.add(obj);
	}
	
	public void append(String cadena){
		sb.append(cadena);
	}
	
	public String print(){
		return sb.toString();
	}

	public void addEntidadesAgrupadas(String codMunicipio,HashMap indexData) {
		this.indexData.put(codMunicipio,indexData);	
	}
	
	public HashMap getEntidadesAgrupadas(String codMunicipio){
		return (HashMap)this.indexData.get(codMunicipio);
	}

	public void addTotalInfoAgrupada(String claveEntidadAgrupada,
			float totalPop, float totalViv) {
		entidadesAgrupadas.addTotalInfo(claveEntidadAgrupada, totalPop,totalViv);				
	}
	
	
	public EntidadAgrupada getTotalInfoAgrupada(String claveEntidadAgrupada){
		return entidadesAgrupadas.getEntidadAgrupada(claveEntidadAgrupada);
	}
	
	public void addSaneamientoConectado(String codSaneamiento){
		saneamientosConectados.put(codSaneamiento, codSaneamiento);
	}
	
	public boolean isSaneamientoConectado(String codSaneamiento){
		return saneamientosConectados.containsKey(codSaneamiento);
	}
	
	public void addAbastecimientoConectado(String codAbastecimiento){
		abastecimientosConectados.put(codAbastecimiento, codAbastecimiento);
	}
	
	public boolean isAbastecimientoConectado(String codAbastecimiento){
		return abastecimientosConectados.containsKey(codAbastecimiento);
	}
	
	
	public void addTotalPoblacion(String codMunicipio,Integer totalPoblacion){
		this.totalPoblacion.put(codMunicipio, totalPoblacion);
	}
	public void addTotalPoblacionEncuestada(String codMunicipio,Integer totalPoblacionEncuestada){
		
		Integer totalTemp=(Integer)this.totalPoblacionEncuestada.get(codMunicipio);
		if (totalTemp!=null)
			totalTemp+=totalPoblacionEncuestada;
		else
			totalTemp=totalPoblacionEncuestada;
		
		this.totalPoblacionEncuestada.put(codMunicipio, totalTemp);
	}
	
	public Integer getTotalPoblacion(String codMunicipio){
		return (Integer)totalPoblacion.get(codMunicipio);
	}
	public Integer getTotalPoblacionEncuestada(String codMunicipio){
		return (Integer)totalPoblacionEncuestada.get(codMunicipio);
	}
	
	public void addTotalViviendas(String codMunicipio,Integer totalViviendas){
		this.totalViviendas.put(codMunicipio, totalViviendas);
	}
	public void addTotalViviendasEncuestadas(String codMunicipio,Integer totalViviendasEncuestadas){
		
		Integer totalTemp=(Integer)this.totalViviendasEncuestadas.get(codMunicipio);
		if (totalTemp!=null)
			totalTemp+=totalViviendasEncuestadas;
		else
			totalTemp=totalViviendasEncuestadas;
		
		this.totalViviendasEncuestadas.put(codMunicipio, totalTemp);
	}
	
	public Integer getTotalViviendas(String codMunicipio){
		return (Integer)totalViviendas.get(codMunicipio);
	}
	public Integer getTotalViviendasEncuestadas(String codMunicipio){
		return (Integer)totalViviendasEncuestadas.get(codMunicipio);
	}
	

	
}
