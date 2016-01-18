/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */
package com.geopista.util;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.actions.ForcedAcceptPrintService;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.feature.GeopistaFeature;
import com.geopista.ui.plugin.bean.ConfigPrintPlugin;
import com.geopista.ui.plugin.print.PrintLayoutFrame;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

import edu.emory.mathcs.backport.java.util.TreeMap;

//import org.apache.log4j.Logger;

public final class UtilsPrintPlugin {

	//Identificador fichero recursos
	private static final String NAME_RESOURCE_PRINT_PLUGIN 	 = "PrintPluginI18N";	
	//Objeto con config wizard plugin impresion series
	public static final String KEY_CONFIG_SERIE_PRINT_PLUGIN = "ConfigWizardSeriePrintPlugin";  

	//Key parametros configuracion definidos para plugin impresion
	public static String PARAM_CONFIG_CURRENT_PAGE 	= "%PAGINA_ACTUAL%";
	public static String PARAM_CONFIG_PAGE_COUNT 	= "%NUMERO_PAGINAS%";
	public static String PARAM_CONFIG_TITLE 		= "%TITULO%";
	public static String PARAM_CONFIG_DESCRIPTION 	= "%DESCRIPCION%";
	public static String PARAM_CONFIG_NAME_CAMPO_ITERACION 	= "%NOMBRE_CAMPO_ITERACION%";
	public static String PARAM_CONFIG_CAMPO_ITERACION 		= "%CAMPO_ITERACION%";
	public static String PARAM_CONFIG_TEXTO_ITERACION 		= "%TEXTO_ITERACION%";
	public static String PARAM_CONFIG_ID_ESCALA 		= "%ID_ESCALA%";
	public static String PARAM_CONFIG_INFO_ESCALAS		= "%INFO_ESCALAS%";

	public static void inicializarIdiomaPluginImpresion(){
		if (I18N.plugInsResourceBundle.get(NAME_RESOURCE_PRINT_PLUGIN) == null){
			Locale locale =I18N.getLocaleAsObject();    
			ResourceBundle bundle = ResourceBundle.getBundle("language.PrintPluginI18N", locale, UtilsPrintPlugin.class.getClassLoader());
			I18N.plugInsResourceBundle.put(NAME_RESOURCE_PRINT_PLUGIN, bundle);
		}
	}

	public static String getMessageI18N (String keyMenssage) {
		return I18N.get(NAME_RESOURCE_PRINT_PLUGIN, keyMenssage);
	}

	public static String getMessageI18N (String keyMenssage, Object[] params) {
		return MessageFormat.format(getMessageI18N(keyMenssage), params);
	}

	/**
	 * Obtiene clave de mapa asociada a la descripcion indicada
	 * Calve de mapa tipo integer
	 * @param valorSeleccion
	 * @return
	 */
	public static String getClaveMapaPorValor (Map mapaDatos, String valorSeleccion) {
		String clave = "";
		String valor = "";
		boolean encontrado = false;
		if (valorSeleccion != null) {
			Iterator it = mapaDatos.keySet().iterator();
			while (it.hasNext() && !encontrado){
				clave = it.next().toString();
				valor = ((clave != null)? (String) mapaDatos.get(Integer.valueOf(clave)) : null);
				if (valor != null && valor.equals(valorSeleccion))
					encontrado = true;
			}
			if (!encontrado)
				clave = null;
		}

		return clave;
	}


	/**
	 * Procesa configuracion establecida en el wizard generando mapa con parametros
	 * @param config
	 * @return
	 */
	public static Map getParametersPrintPlugin (ConfigPrintPlugin config) {
		Map param = new HashMap ();

		param.put(PARAM_CONFIG_TITLE, config.getTitulo());
		param.put(PARAM_CONFIG_DESCRIPTION, config.getDescripcion());

		param.put(PARAM_CONFIG_ID_ESCALA, config.getIdEscala());
		param.put(PARAM_CONFIG_INFO_ESCALAS, config.getDatosEscalas());

		param.put(PARAM_CONFIG_NAME_CAMPO_ITERACION, config.getNombreCampoIteracion());
		param.put(PARAM_CONFIG_TEXTO_ITERACION, config.getTextoIteracion());

		List lstValuesCamposIteracion = new ArrayList ();
		List lstCuadricula = config.getLstCuadriculas();
		if (lstCuadricula != null) {
			for (int i = 0; i < lstCuadricula.size(); i++)
				lstValuesCamposIteracion.add(String.valueOf(((GeopistaFeature) lstCuadricula.get(i)).getAttribute(config.getNombreCampoIteracion())));
		}
		param.put(PARAM_CONFIG_CAMPO_ITERACION, lstValuesCamposIteracion);


		return param;
	}

	/**
	 * Obtiene datos de escalas disponibles (id/desc) 
	 * Usamos treemap con id integer para mostrar ordenado, si no, crear metodo de ordenacion
	 * @return
	 */
	public static Map<Integer, String> getDatosEscalasDisponibles () {
		Map<Integer, String> lstEscalas = new  TreeMap();

		lstEscalas.put(-2, UtilsPrintPlugin.getMessageI18N("SeriePrintPlugIn.EscalaVistaActual"));
		lstEscalas.put(-1, UtilsPrintPlugin.getMessageI18N("SeriePrintPlugIn.EscalaTodoElMapa"));

		lstEscalas.put(500,  "1:500");
		lstEscalas.put(1000, "1:1000");
		lstEscalas.put(2000, "1:2000");
		lstEscalas.put(3000, "1:3000");
		lstEscalas.put(4000, "1:4000");
		lstEscalas.put(5000, "1:5000");
		lstEscalas.put(10000, "1:10000");

		return lstEscalas;
	}



	/**
	 * Called when the user presses Next on this panel
	 */
	public static void realizarAccionPlugin (final PlugInContext pluginContext, final JPanel parent, final ConfigPrintPlugin config) throws Exception
	{
		AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);

		progressDialog.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				new Thread(new Runnable() {
					public void run() {
						try {
							//Esetablecer panel espera
							PrintLayoutFrame frame = config.getPrintFrame();
							frame.setProgressDialog(progressDialog);

							//Accion es crear plantilla: mostrar plantilla vacia
							if (config.getEsAccionCrearPlantilla()) {
								progressDialog.setTextTitle(UtilsPrintPlugin.getMessageI18N("PrintPlugin.tituloAccion.crearPlantilla"));
								progressDialog.report(UtilsPrintPlugin.getMessageI18N("PrintPlugin.descAccion.buscarPlantillas")); 
								frame.setVisible(true);
								JOptionPane.showMessageDialog(parent, "Plantilla abierta");
							} 
							else if (config.getEsAccionModificarPlantilla()) {
								progressDialog.setTextTitle(UtilsPrintPlugin.getMessageI18N("PrintPlugin.tituloAccion.modificarPlantilla"));
								//Accion abrir plantilla para modificar: cargar plantilla almacenada
								//Seleccionar y cargar plantilla
								frame.forceOpen();
								//Mostrar
								frame.setVisible(true);
								frame.toBack();
								frame.show();
								JOptionPane.showMessageDialog(parent, "Plantilla cargada");
							} 
							else {
								progressDialog.setTextTitle(UtilsPrintPlugin.getMessageI18N("PrintPlugin.tituloAccion.imprimirSerieMapas"));

								Layer selectedLayer = config.getCapaIteracion();
								List lstCuadriculaOriginal = config.getLstCuadriculas();

								//Comprobar si tenemos datos para generar resultados: Para impresion de series tenemos que tener cuadricula
								boolean hayDatos = (config.getEsAccionImprimir())? true: false;
								if ((config.getEsAccionImprimirSerie()) && (selectedLayer != null && lstCuadriculaOriginal.size() > 0))
									hayDatos = true;

								//Si tenemos datos: generar resultados
								if (hayDatos) 
									UtilsPrintPlugin.generarResultadosImpresionMapas(pluginContext, parent, config);
								else
									JOptionPane.showMessageDialog(parent, "Faltan datos para impresion");
							}

						} catch (Exception e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						} finally {
							progressDialog.setVisible(false);
							progressDialog.dispose();
						}
					}
				}).start();
			}
		});
		GUIUtil.centreOnWindow(progressDialog);

		progressDialog.allowCancellationRequests();
		progressDialog.setVisible(true);
		progressDialog.toFront();
	}




	/**
	 * Genera resultados del plugin de impresion Series de mapas segun la configuracion indicada
	 * @param pluginContext
	 * @param parent
	 * @param config
	 * @throws Exception 
	 */
	private static void generarResultadosImpresionMapas (PlugInContext pluginContext, JPanel parent, ConfigPrintPlugin config) throws Exception 
	{
		//Generar resultados
		if (config.isResultadoEnFichero())
			generarResultadosEnFichero (parent, config);
		else
			generarResultadosEnPlantilla (pluginContext, parent, config);
	}

	/**
	 * Abre y muestra plantilla con resultados de la seleccion realizada para que el usuario pueda actuar sobre la plantilla
	 * @param pluginContext
	 * @param parent
	 * @param config
	 */
	private static void generarResultadosEnPlantilla (PlugInContext pluginContext, JPanel parent, ConfigPrintPlugin config) {
		PrintLayoutFrame frame = config.getPrintFrame();
		frame.setVisible(true);
		frame.show();
		//Establecer datos
		actualizarDatosPlantilla(frame, config.getLstCuadriculas(), UtilsPrintPlugin.getParametersPrintPlugin(config));
		//Seleccionar plantilla y abrir
		frame.forceOpen();
		frame.toBack();
		//Mostrar confirmacion
		JOptionPane.showMessageDialog(parent, "Plantilla cargada");
	}

	/**
	 * Realiza la impresion generando los resultados directamente en uno o varios ficheros sin que el usuario pueda actuar sobre la plantilla seleccionada
	 * @throws Exception 
	 */
	private static void generarResultadosEnFichero (JPanel parent, ConfigPrintPlugin config) throws Exception {
		JFileChooser fileChooser = null;

		boolean multiplesFicheros = config.isUnFicheroPorHoja();
		List lstCuadriculaOriginal = config.getLstCuadriculas();

		//Obtener frame seleccion para cargar configuracion establecida 
		PrintLayoutFrame frame = config.getPrintFrame();
		frame.setVisible(true);
		frame.toBack();
		frame.show();

		//Establecer configuracion para impresion PDF
		establecerConfiguracionImpresionPDF(config);

		//Generar multiples ficheros: un fichero con cada una de las cuadriculas seleccionadas
		if (multiplesFicheros) {
			for (int i = 0; i < lstCuadriculaOriginal.size(); i++) 
				fileChooser = imprimirFichero(frame, fileChooser, config, new ArrayList(lstCuadriculaOriginal.subList(i, i+1)), new Integer (i+1));
		}
		else	//Generar fichero con todas las cuadriculas
			imprimirFichero(frame, fileChooser, config, lstCuadriculaOriginal, null);

		//Cerrar frame despues de impresion
		frame.setVisible(false);
		frame.dispose();

		//Mostrar confirmacion
		JOptionPane.showMessageDialog(parent, "Impresion realizada");
	}

	private static void establecerConfiguracionImpresionPDF (ConfigPrintPlugin config) throws Exception {
		try {
			//Si tenemos impresora PDFCreator ejecutamos configuracion
			if (config.getServicioImpresion() != null && config.getServicioImpresion().getName().equals("PDFCreator")) 
			{
				String pathScriptOrigen = "";
				String pathScriptDestino = "";
				String nameScript = "SetConfigPDFCreator.vbs";

				//Estblecer rutas para descarga de script
				pathScriptOrigen = "/scripts/";
				File dirBase = new File (UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY, null,false));
				pathScriptDestino = dirBase.getPath() + dirBase.separator + "scripts" + dirBase.separator;

				//Descargamos script a ejecutar con configuracion para PDFCreator
				decargarFichero(pathScriptOrigen, pathScriptDestino, nameScript);

				//Ejecutar script con parametros correspondientes
				int j = 0;
				String[]cmd = new String[6];
				cmd[j++] = "cmd.exe" ;
				cmd[j++] = "/C" ;

				//Script a ejecutar
				cmd[j++] = pathScriptDestino  + nameScript;
				//Carpeta destino resultados
				cmd[j++] = config.getRutaDestino();//"<MyFiles>\\ejemplo";
				//Nombre fichero: titulo establecido
				cmd[j++] = "<Title>";
				cmd[j++] = "1";
				Runtime rt = Runtime.getRuntime();
				Process proc;

				proc = rt.exec(cmd);
				int exitVal = proc.waitFor();
				System.out.println("Valor de salida: " + exitVal);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Descargar a local fichero para ejecutar configuracion impresion PDF
	 * @param pathOrigen
	 * @param pathDestino
	 * @param nombreFichero
	 * @throws Exception 
	 */
	private static void decargarFichero (String pathOrigen, String pathDestino, String nombreFichero) throws Exception {
		InputStream entrada = null;
		FileOutputStream salida = null;
		try{
			//Si no existe ruta la creamos
			File destino = new File(pathDestino);
			if (destino != null && !destino.exists()) 
				destino.mkdir();
			//Si no esta descargado: descargamos
			File fileDestino = new File(destino, nombreFichero);
			if (fileDestino != null && !fileDestino.exists()) {
				int len;
				byte[] buf =new byte[1024];
				entrada = UtilsPrintPlugin.class.getResourceAsStream(pathOrigen + nombreFichero);
				salida = new FileOutputStream(fileDestino);
				while((len=entrada.read(buf))>0)
					salida.write(buf, 0, len);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				if (salida != null)
					salida.close();
				if (entrada != null)
					entrada.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Genera fichero de resultados segun datos enviados
	 * @param frame
	 * @param fileChooser
	 * @param config
	 * @param lstCuadricula
	 * @return Retorna plantilla seleccionada para generar resultados
	 */
	private static JFileChooser imprimirFichero (PrintLayoutFrame frame, JFileChooser fileChooser, ConfigPrintPlugin config, List lstCuadricula, Integer contador) {
		try {
			//Establecer configuracion personalizada o usar la de la plantilla segun seleccion
			PageFormat pageFormatPrint = (config.isConfigPersonalizada())? frame.getPageFormat() : null;
			//Borrar elementos graficos de la pagina
			frame.resetPage();
			//Establecer datos
			config.setLstCuadriculas(lstCuadricula);
			Map parametros = UtilsPrintPlugin.getParametersPrintPlugin(config);
			//Carga (o seleccion inicial) de plantilla con datos actualizados
			actualizarDatosPlantilla(frame, config.getLstCuadriculas(), parametros);
			fileChooser = frame.forceOpen(fileChooser, pageFormatPrint);
			//Imprimir
			frame.getPrinterJob().setJobName(getNombreFicheroResultados (config, parametros, contador));
			frame.getPrinterJob().setPrintable((Printable) frame.getPrintLayoutPreviewPanel().getPreviewPanel().getPage().getPageForPrint(), frame.getPageFormat());
			frame.getPrinterJob().print();
		} catch (PrinterException exception) {
			if (exception.getMessage().indexOf("accepting job") != -1) {
				// recommend prompting the user at this point if they want to force it
				// so they'll know there may be a problem.
				try {
					// try printing again but ignore the not-accepting-jobs attribute
					PrintService printer = frame.getPrinterJob().getPrintService();
					DocPrintJob job = printer.createPrintJob();

					DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
					SimpleDoc doc = new SimpleDoc((Printable) frame.getPrintLayoutPreviewPanel().getPreviewPanel().getPage().getPageForPrint(), flavor, null);
					ForcedAcceptPrintService.setupPrintJob(job); // add secret ingredient
					job.print(doc,null);
				} catch (PrintException e) {
					// ok, you really can't print
					JOptionPane.showMessageDialog(frame, e);
				}
			}
			else
				JOptionPane.showMessageDialog(frame, exception);
		}
		return fileChooser;
	}

	/**
	 * Actualiza informacion de plantilla
	 * @param frame
	 * @param lstCuadricula
	 * @param parameters
	 */
	private static void actualizarDatosPlantilla (PrintLayoutFrame frame, List lstCuadricula, Map parameters) {
		//Establecer datos
		frame.getExtentManager().setExtentsFromFeatures(lstCuadricula);
		frame.getExtentManager().setParameters(parameters);
		frame.getExtentManager().setCurrentExtent(0);
		frame.setMapsExtents();
	}

	/**
	 * Obtiene nombre para generacion de fichero de resultados
	 * @param config
	 * @param parametros
	 * @param contador
	 * @return
	 */
	private static String getNombreFicheroResultados (ConfigPrintPlugin config, Map parametros, Integer contador) {
		String descCampo = "";
		List lstCampos =  (List) parametros.get(UtilsPrintPlugin.PARAM_CONFIG_CAMPO_ITERACION);
		//Si tenemos un unico valor de campo establecer para formar parte del nombre (si no no se incluye nada)
		if ((lstCampos != null)  && (lstCampos.size() == 1)) {
			descCampo = (String) lstCampos.get(0);
			if (descCampo == null || descCampo.equals("") || descCampo.equals("null"))
				descCampo = "SinDescripcion";
			//TODO: Limitar longitud ????
			if (descCampo.length() > 100)
				descCampo = descCampo.substring(0, 100);
			descCampo = ("_" + descCampo);
		}

		//Nombre fichero: prefijoImpresion_DescCampoIteracion_Contador<ConfiguracionPDFCreator>
		String nombre = (config.getPrefijoImpresion() + descCampo + ((contador != null)? ("_" + contador) : ""));

		return nombre;
	}

	public static String formatTextStyleUnderline(String text, boolean isUnderline) {
		String result = (text != null)? text : "";
		//Eliminar subrayado si lo tenia
		result = result.replaceAll("<html><u>", "");
		result = result.replaceAll("</u></html>", "");
		//establecer si corresponde
		if (isUnderline) 
			result = "<html><u>" + result + "</u></html>";
		return result;
	}

}
