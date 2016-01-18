/**
 * LegendServlet.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wm;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.localgis.web.core.model.LocalgisLegend;

/**
 * Servlet implementation class for Servlet: LegendServlet
 *
 */
 public class LegendServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public LegendServlet() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession httpSession = request.getSession();
	    LocalgisLegend localgisLegend = (LocalgisLegend) httpSession.getAttribute("localgisLegend");
	    
	    response.setContentType("image/gif");
	    if (localgisLegend != null) {
	        
	        OutputStream outputStream = response.getOutputStream();
	        outputStream.write(localgisLegend.getImg());
	        outputStream.close();
	        
	    }
	    else {
	        String path = getServletContext().getRealPath("WEB-INF/classes/no_legend.gif");
	        FileInputStream fileInputStream = new FileInputStream(path);
	        OutputStream outputStream = response.getOutputStream();
	        byte[] buffer = new byte[1024];
	        int bytesRead = 0;
	        while ((bytesRead = fileInputStream.read(buffer,0,1024))!=-1) {
	            outputStream.write(buffer,0,bytesRead);
	        }
	        outputStream.close();
	    }
	    response.flushBuffer();
	    httpSession.removeAttribute("localgisLegend");			
	}
}