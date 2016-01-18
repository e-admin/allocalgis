/**
 * JRCementerio.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JRViewer;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Constantes;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 18-oct-2006
 * Time: 12:04:17
 * To change this template use File | Settings | File Templates.
 */
public class JRCementerio {
    public static void generarReport(String jrxmlfile, String pathDestino, String formato, Collection bienes, JFrame desktop, String locale) throws Exception{

        /** Cargamos el JasperDesign desde el XML (jrxml) y lo compilamos para generar un JasperReport */
        JasperDesign jasperDesign = JRXmlLoader.load(jrxmlfile);
        JasperReport jasperReport= JasperCompileManager.compileReport(jasperDesign);

        Map parametros= new HashMap();
        parametros.put("pathSubReportRefCatastrales", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportRefCatastrales.jasper");
        parametros.put("pathSubReportMejoras", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportMejoras.jasper");
        parametros.put("pathSubReportObservaciones", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportObservaciones.jasper");
        parametros.put("pathSubReportUsosFuncionales", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportUsosFuncionales.jasper");
        parametros.put("pathSubReportDocumentos", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportDocumentos.jasper");
        parametros.put("pathSubReportImagenes", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportImagenes.jasper");
        parametros.put("pathPlantillaCreditoDerecho", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportCreditos.jasper");
        parametros.put("locale", locale);
        
     
        /** Rellenamos el report */
        JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport, parametros/*new HashMap()*/, new JRCementerioDataSource(bienes, locale));

        if (formato.equalsIgnoreCase(""+Constantes.formatoPREVIEW)){
            /** Visualizacion del report en un dialogo */
            JDialog viewer= new JDialog(desktop/*new JFrame()*/, true/*modal*/);
            try{viewer.setTitle(((AppContext) AppContext.getApplicationContext()).getI18nString("inventario.informes.tag14"));}catch(Exception e){};
            viewer.setSize(800,600);
            viewer.setLocationRelativeTo(null);

            JRViewer jrv= new JRViewer(jasperPrint);
            viewer.getContentPane().add(jrv);
            viewer.setVisible(true);
            /** Visualizacion del report en el JasperViewer */
            //JasperViewer.viewReport(jasperPrint, false);
        }else if (formato.equalsIgnoreCase(""+Constantes.formatoPDF)){
            // Generamos el fichero PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, pathDestino);
        }/*Implementado pero nunca se ejecuta*/else if (formato.equalsIgnoreCase(""+Constantes.formatoXML)){
            JasperExportManager.exportReportToXmlFile(jasperPrint, pathDestino, /*isEmbeddingImages*/false);
        }/*Implementado pero nunca se ejecuta*/else if (formato.equalsIgnoreCase(""+Constantes.formatoPRINT)){
            /** Imprimimos el report */
            JasperPrintManager.printReport(jasperPrint, /*withPrintDialog*/true);
        }/*Implementado pero nunca se ejecuta*/else if (formato.equalsIgnoreCase(""+Constantes.formatoHTML)){
            /** Or create HTML Report  */
            JasperExportManager.exportReportToHtmlFile(jasperPrint, pathDestino);
        }

    }
}
