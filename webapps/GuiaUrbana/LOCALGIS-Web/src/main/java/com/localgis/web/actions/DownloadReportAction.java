/**
 * DownloadReportAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.actions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.localgis.web.actionsforms.DownloadReportActionForm;

public class DownloadReportAction extends Action {
	
	
	private static Logger logger = Logger.getLogger(DownloadReportAction.class);
    @SuppressWarnings("unused")
	private static String ERROR_PAGE = "error";

    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DownloadReportActionForm formBean = (DownloadReportActionForm)form;
        String reportName = formBean.getReportName();
        byte[] output=null;
        
        
        try {
									
			output=readBinaryFile(reportName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        File f=new File(reportName);
        String shortName=f.getName();
       
		//Escribir
        //response.setContentLength(gpxOut.size());
        response.setContentType("application/x-file-download");
        String filename = shortName;
        filename = filename.replace(" ", "_");
        response.setHeader("Content-disposition", "attachment; filename=" + filename);
        
        ServletOutputStream outStream = response.getOutputStream();
        outStream.write(output);
        outStream.flush();
        
        return null;
    }
    
    private byte[] readBinaryFile(String fileName) throws IOException {
        //InputStream input = getClass().getResourceAsStream(fileName);
        InputStream input = new FileInputStream(fileName);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
 
        for (int read = input.read(); read >= 0; read = input.read())
	        output.write(read);
 
        byte[] buffer = output.toByteArray();
 
        input.close ();
        output.close();
 
        return buffer;
}
    
   
    
    
     
}
