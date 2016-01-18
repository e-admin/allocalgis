/**
 * ConfigPrintPlugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.bean;

import java.util.List;
import java.util.Map;

import javax.print.PrintService;

import com.geopista.ui.plugin.print.PrintLayoutFrame;
import com.vividsolutions.jump.workbench.model.Layer;

public class ConfigPrintPlugin {

	private static final int ACCION_PLANTILLA_CREAR 	= 0;
	private static final int ACCION_PLANTILLA_MODIFICAR = 1;
	private static final int ACCION_IMPRIMIR_SERIE 	 	= 2;
	private static final int ACCION_IMPRIMIR 	 		= 3;
	
	//Accion plugion: crear/modificar plantilla, imprimir serie, ...
	private Integer accionPlugin;
	
	//Configuracion general
	private String titulo;
	private String descripcion;
	//Escala
	private String idEscala;
	private Map datosEscalas;
	//Leyenda en vista preliminar
	private boolean mostrarLeyenda;
	//Datos iteracion
	private Layer capaIteracion;
	private String nombreCampoIteracion;
	private String textoIteracion;
	//Seleccion de elementos para imprimir
	private List lstCuadriculas;
	//Configuracion impresion documentos
	private String rutaDestino;
	private String prefijoImpresion;
	private PrintService servicioImpresion;
	private boolean configPersonalizada;
	private PrintLayoutFrame printFrame;
	//Formato resultados
	private boolean resultadoEnFichero;
	private boolean unFicheroPorHoja;
	
	

	public ConfigPrintPlugin() {
	}
	
	public ConfigPrintPlugin(PrintLayoutFrame pFrame) {
		printFrame = pFrame;
		printFrame.setVisible(false);
		printFrame.toBack();
	}
	
	
	
	public PrintService getServicioImpresion() {
		return servicioImpresion;
	}
	public void setServicioImpresion(PrintService servicioImpresion) {
		this.servicioImpresion = servicioImpresion;
	}
	public boolean isConfigPersonalizada() {
		return configPersonalizada;
	}
	public void setConfigPersonalizada(boolean configPersonalizada) {
		this.configPersonalizada = configPersonalizada;
	}
	public Integer getAccionPlugin() {
		return accionPlugin;
	}
	public void setAccionPlugin(Integer accionPlugin) {
		this.accionPlugin = accionPlugin;
	}
	public PrintLayoutFrame getPrintFrame() {
		return printFrame;
	}
	public void setPrintFrame(PrintLayoutFrame printFrame) {
		this.printFrame = printFrame;
	}
	public Map getDatosEscalas() {
		return datosEscalas;
	}
	public void setDatosEscalas(Map datosEscalas) {
		this.datosEscalas = datosEscalas;
	}
	public String getRutaDestino() {
		return rutaDestino;
	}
	public void setRutaDestino(String rutaDestino) {
		this.rutaDestino = rutaDestino;
	}
	public String getPrefijoImpresion() {
		return prefijoImpresion;
	}
	public void setPrefijoImpresion(String prefijoImpresion) {
		this.prefijoImpresion = prefijoImpresion;
	}
	public Layer getCapaIteracion() {
		return capaIteracion;
	}
	public void setCapaIteracion(Layer capaIteracion) {
		this.capaIteracion = capaIteracion;
	}
	public String getNombreCampoIteracion() {
		return nombreCampoIteracion;
	}
	public void setNombreCampoIteracion(String nombreCampoIteracion) {
		this.nombreCampoIteracion = nombreCampoIteracion;
	}
	public String getTextoIteracion() {
		return textoIteracion;
	}
	public void setTextoIteracion(String textoIteracion) {
		this.textoIteracion = textoIteracion;
	}
	public List getLstCuadriculas() {
		return lstCuadriculas;
	}
	public void setLstCuadriculas(List lstCuadriculas) {
		this.lstCuadriculas = lstCuadriculas;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getIdEscala() {
		return idEscala;
	}
	public void setIdEscala(String idEscala) {
		this.idEscala = idEscala;
	}
	public boolean isMostrarLeyenda() {
		return mostrarLeyenda;
	}
	public void setMostrarLeyenda(boolean mostrarLeyenda) {
		this.mostrarLeyenda = mostrarLeyenda;
	}
	public boolean isResultadoEnFichero() {
		return resultadoEnFichero;
	}
	public void setResultadoEnFichero(boolean resultadoEnFichero) {
		this.resultadoEnFichero = resultadoEnFichero;
	}
	public boolean isUnFicheroPorHoja() {
		return unFicheroPorHoja;
	}
	public void setUnFicheroPorHoja(boolean unFicheroPorHoja) {
		this.unFicheroPorHoja = unFicheroPorHoja;
	}

	
	public void establecerAccionPlugin (boolean crear, boolean modificar, boolean imprimir, boolean imprimirSerie) {
		if (crear)
			accionPlugin = new Integer(ACCION_PLANTILLA_CREAR);
		else if (modificar)
			accionPlugin = new Integer(ACCION_PLANTILLA_MODIFICAR);
		else if (imprimir)
			accionPlugin = new Integer(ACCION_IMPRIMIR);
		else if (imprimirSerie)
			accionPlugin = new Integer(ACCION_IMPRIMIR_SERIE);
	}
	public boolean getEsAccionCrearPlantilla () {
		return (accionPlugin != null && accionPlugin.intValue() == ACCION_PLANTILLA_CREAR);
	}
	public boolean getEsAccionModificarPlantilla () {
		return (accionPlugin != null && accionPlugin.intValue() == ACCION_PLANTILLA_MODIFICAR);
	}
	public boolean getEsAccionImprimir () {
		return (accionPlugin != null && accionPlugin.intValue() == ACCION_IMPRIMIR);
	}
	public boolean getEsAccionImprimirSerie () {
		return (accionPlugin != null && accionPlugin.intValue() == ACCION_IMPRIMIR_SERIE);
	}
}
