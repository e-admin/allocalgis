/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * XMLDataSourceExample.java
 * 
 */

package it.businesslogic.ireport.examples;

import it.businesslogic.ireport.connection.JRXMLDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import net.sf.jasperreports.engine.*;
import java.util.HashMap;


public class XMLDataSourceExample {

    public static void main(String[] args) throws Exception {

      		String reportFileName = "/addressbook.jasper";
		String outFileName = "/addressbook.pdf";
                String xmlFileName = "/addressbook.xml";
                String recordPath = "/addressbook/category/person";
                
                JRXMLDataSource jrxmlds = new JRXMLDataSource(xmlFileName,recordPath);
                
		HashMap hm = new HashMap();
		
		try
		{
			JasperPrint print = JasperFillManager.fillReport(
						reportFileName, 
						hm, 
						jrxmlds);
			
			JRExporter exporter = new net.sf.jasperreports.engine.export.JRPdfExporter();
			
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,outFileName);
                  	exporter.setParameter(JRExporterParameter.JASPER_PRINT,print);
                  	
                  	exporter.exportReport();
                  	System.out.println("Created file: " + outFileName);				
		}
		catch (JRException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}

    }

}


