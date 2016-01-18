package com.geopista.server.licencias.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.geopista.server.LoggerHttpServlet;

import java.io.*;


public class CServletPruebaActividad extends LoggerHttpServlet {
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CServletPruebaActividad.class);


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
