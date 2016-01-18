/**
 * CServletPrueba.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.licencias.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.geopista.server.LoggerHttpServlet;


public class CServletPrueba extends LoggerHttpServlet {
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CServletPrueba.class);


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		logger.debug("Inicio.");
		doPost(request, response);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		super.doPost(request);
		logger.debug("Inicio.");
		PrintWriter out = response.getWriter();


		try {
			String stream = "";

			String aux = null;
			InputStream is = request.getInputStream();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
			while ((aux = buffer.readLine()) != null) {
				stream += aux;
			}
			logger.info("stream: " + stream);


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}


	}
}
