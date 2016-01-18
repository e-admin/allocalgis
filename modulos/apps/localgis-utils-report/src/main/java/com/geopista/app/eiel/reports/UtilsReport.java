/**
 * UtilsReport.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.plantillas.ConstantesLocalGISPlantillas;

public class UtilsReport {

	private static final Log logger = LogFactory.getLog(UtilsReport.class);

	@SuppressWarnings("unchecked")
	public static void obtenerSubreports(JasperDesign jasperDesign,
			ArrayList<String> listaSubReports, Object[] subreports,
			boolean serverside) {
		String path = ConstantesLocalGISEIEL.PATH_PLANTILLAS_EIEL
				+ File.separator + "subreports" + File.separator;
		JRChild hijo = null;
		JasperDesign jasperDesignSub = null;
		String nombre = "";

		JRBand[] bands = new JRBand[10];

		JRBand designBand = getDetailBand(jasperDesign);

		bands[0] = designBand;

		bands[1] = jasperDesign.getBackground();
		bands[2] = jasperDesign.getColumnFooter();
		bands[3] = jasperDesign.getColumnHeader();
		bands[4] = jasperDesign.getLastPageFooter();
		bands[5] = jasperDesign.getNoData();
		bands[6] = jasperDesign.getPageFooter();
		bands[7] = jasperDesign.getPageHeader();
		bands[8] = jasperDesign.getSummary();
		bands[9] = jasperDesign.getTitle();

		logger.debug("Procesando informe " + jasperDesign.getName());
		logger.info("Path subreports:"+path);

		for (int x = 0; x < bands.length; x++) {
			if (bands[x] == null) {
				continue;
			}
			ArrayList<JRChild> elementosBanda = (ArrayList<JRChild>) bands[x]
					.getChildren();
			for (int i = 0; i < elementosBanda.size(); i++) {
				hijo = elementosBanda.get(i);
				// Si es un sub-report lo procesamos
				if (hijo instanceof JRDesignSubreport) {
					nombre = getNombreSubreport((JRDesignSubreport) hijo);
					if (nombre != null) {
						// Si no está en la lista lo añadimos y lo procesamos.
						// Los ya procesados no se repiten
						if (!listaSubReports.contains(nombre)) {
							listaSubReports.add(nombre);
							try {
								guardarCompilarSubreport(nombre, subreports,
										serverside);
								jasperDesignSub = JRXmlLoader.load(path
										+ nombre);
								obtenerSubreports(jasperDesignSub,
										listaSubReports, subreports, serverside);
							} catch (Exception e) {
								logger.warn("Error procesando subinforme "
										+ nombre);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Obtiene la banda de detalle. La implementacion ha cambiado a partir de la
	 * version 4
	 * 
	 * @param jasperDesignBuilder
	 * @param subReport
	 * @return
	 * @throws Exception
	 */
	private static JRBand getDetailBand(JasperDesign jasperDesignSub) {
		// Version 2.0
		//return jasperDesignSub.getDetail();

		// Version 4.0
		
		JRSection jrSection = jasperDesignSub.getDetailSection(); for(JRBand
		jrBand : jrSection.getBands()) { if(jrBand instanceof JRDesignBand) {
		return (JRBand)jrBand; } } return null;
		
	}

	private static String getNombreSubreport(JRDesignSubreport subreport) {
		JRDesignExpression expresion = (JRDesignExpression) subreport
				.getExpression();
		String strExpresion = expresion.getText();

		int posComillas = strExpresion.indexOf("\"");
		int posPunto = strExpresion.lastIndexOf(".");

		if (posComillas == -1 || posPunto == -1) {
			return null;
		}

		String nombre = strExpresion.substring(posComillas + 1, posPunto)+ ".jrxml";

		return nombre;
	}

	private static void guardarCompilarSubreport(String subreportname,
			Object[] subreports, boolean serverside) throws Exception {

		// Crear path si es necesario
		String path = ConstantesLocalGISEIEL.PATH_PLANTILLAS_EIEL
				+ File.separator + "subreports" + File.separator;
		if (!new File(path).exists()) {
			new File(path).mkdirs();
		}

		// Si la lista de subreports esta vacia asumimos
		// que el informe en jrxm ya esta guardado en disco. Esto solo vale
		// si el reporte se ejecuta en modo servidor (WebEIELReport)
		if (serverside) {
			String pathCompilacion = (new File(path)).getAbsolutePath()
					+ File.separator + subreportname;
			JasperCompileManager.compileReportToFile(pathCompilacion);
			return;
		}
		if (subreports == null)
			return;
		// Buscar subreport y copiarlo
		String nombre = "";
		for (int i = 0; i < subreports.length; i++) {
			Object[] subreport = (Object[]) subreports[i];
			nombre = (String) subreport[0];
			// Si encontramos el informe...
			if (nombre.equalsIgnoreCase(subreportname)) {
				// y tiene contenido
				if (subreport[1] == null) {
					break;
				}
				FileOutputStream out = new FileOutputStream(path + nombre);
				out.write((byte[]) subreport[1]);
				out.close();

				logger.info("Compilando Report:"+(new File(path)).getAbsolutePath() + File.separator + nombre);				
				JasperCompileManager.compileReportToFile((new File(path))
						.getAbsolutePath() + File.separator + nombre);
				logger.info("Report compilado");
				return;
			}
		}
	}

	public static void getNombresPlantillas(String directorio, String filtro,
			String categoria, String plantillaBusqueda,	ArrayList<Object[]> plantillas) {
		getNombresPlantillas(directorio,filtro,categoria,plantillaBusqueda,plantillas,null);

	}
	
	public static void getNombresPlantillas(String directorio, String filtro,
			String categoria, String plantillaBusqueda,	ArrayList<Object[]> plantillas,String idEntidad) {

		File dir = new File(directorio);
		Object[] plantilla;

		if (dir.isDirectory()) {
			if (plantillaBusqueda == null) {
				FilenameFilter filter = new MatchJRXMLFilter(filtro, categoria);
				File[] childrenPlantillas = dir.listFiles(filter);
				for (int i = 0; i < childrenPlantillas.length; i++) {
					File file = childrenPlantillas[i];
					plantilla = new Object[2];
					if (idEntidad!=null)
						plantilla[0] ="("+idEntidad+") "+ file.getName();
					else
						plantilla[0] = file.getName();
					plantilla[1] = file.getAbsolutePath();
					plantillas.add(plantilla);
				}
			} else {
				String elementoBusqueda=plantillaBusqueda;
				if (idEntidad!=null)
					if (plantillaBusqueda.startsWith("(")){
						//System.out.println("Busqueda:"+plantillaBusqueda);
						int indice=plantillaBusqueda.indexOf(")");
						elementoBusqueda=plantillaBusqueda.substring(indice+2,plantillaBusqueda.length());
					}
				//System.out.println("Plantilla de Busqueda:"+elementoBusqueda);
				FilenameFilter filter = new MatchJRXMLFilter(elementoBusqueda);
				File[] childrenPlantillas = dir.listFiles(filter);
				for (int i = 0; i < childrenPlantillas.length; i++) {
					File file = childrenPlantillas[i];
					plantilla = new Object[2];
					plantilla[0] = file.getName();
					plantilla[1] = file.getAbsolutePath();
					plantillas.add(plantilla);
				}
			}

			// Por cada directorio que exista volvemos a recorrerlo
			File[] childrenDirectorio = dir.listFiles();
			for (int i = 0; i < childrenDirectorio.length; i++) {
				File file = childrenDirectorio[i];
				if (file.isDirectory()) {
					// Entramos en todos los directorios excepto en los que se
					// llamen subreport
					if (!file.getName().equals("subreports")) {
						getNombresPlantillas(file.getAbsolutePath(), filtro,
								categoria, plantillaBusqueda, plantillas,idEntidad);
					}
				}
			}
		}
	}
	
	public static void main(String args[]){
		String plantillaBusqueda="(77) EIEL_equipamientos_EQ.jrxml";
		int indice=plantillaBusqueda.indexOf(")");
		String elementoBusqueda=plantillaBusqueda.substring(indice+2,plantillaBusqueda.length());
		System.out.println("<"+elementoBusqueda+">");
	}

}

class MatchJRXMLFilter implements FilenameFilter {
	private String filter1;
	private String filter2;
	private boolean busquedaExacta = false;

	public MatchJRXMLFilter(String filter1, String filter2) {
		this.filter1 = filter1;
		this.filter2 = filter2;

	}

	public MatchJRXMLFilter(String filter1) {
		this.filter1 = filter1;
		busquedaExacta = true;

	}

	public boolean accept(File dir, String name) {
		if (busquedaExacta) {
			if (name.equals(filter1))
				return true;
			else
				return false;
		} else {
			if (name.contains("CAPAS_") && (filter1!=null && filter1.equals("CA")))
				return false;
			if (name.contains("GROUPED") && (filter1!=null && filter1.equals("ED")) && (!name.contains("saneamiento")))
				return false;
			if (name.endsWith("."
					+ ConstantesLocalGISPlantillas.EXTENSION_JRXML)
					&& ((filter1 != null) && (name.contains(filter1)))
					|| name.endsWith("."
							+ ConstantesLocalGISPlantillas.EXTENSION_JRXML)
					&& ((filter2 != null) && (name.contains(filter2))))
				return true;
			else
				return false;
		}
	}
	
	
}
