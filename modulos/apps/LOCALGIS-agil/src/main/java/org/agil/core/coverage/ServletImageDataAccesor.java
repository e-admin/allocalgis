/**
 * ServletImageDataAccesor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.agil.core.coverage;

import java.awt.image.BufferedImage;
import java.net.URL;

import com.vividsolutions.jts.geom.Envelope;

/**
 *  <p>
 *
 *  Permite recuperar imagenes de un Servlet servidor de imagenes </p>
 *
 *@author     alvaro zabala
 *@version    1.1
 */
public class ServletImageDataAccesor
		 extends TCPImageDataAccesor {
	/*
	 *  url del servlet que nos va a proporcionar imagenes
	 *
	 */
	private String urlServlet;
	/**
	 *  peticion que se hace llegar al servlet
	 */
	private String peticion = "?idCoverage=" + idCoverage + "&accion=";


	/**
	 *@param  urlServlet  Description of Parameter
	 *@param  idCoverage  Description of Parameter
	 *@roseuid            3F703C5403C8
	 */
	public ServletImageDataAccesor(String urlServlet, long idCoverage) {
		this.urlServlet = urlServlet;
		this.idCoverage = idCoverage;
	}


	/**
	 *  Lee el fragmento de imagen de la cobertura asociada de un servlet
	 *  servidor de imagenes (en un futuro habrá que integrar esto con servidores
	 *  WMS)
	 *
	 *@param  envelope
	 *@param  width
	 *@param  height
	 *@return           java.awt.image.BufferedImage
	 *@author           alvaro zabala 23-sep-2003
	 *@roseuid          3F703C550000
	 */
	public BufferedImage getImagen(Envelope envelope, int width, int height) {
		BufferedImage solucion = null;
		try {
			StringBuffer request = new StringBuffer(urlServlet).
					append(peticion).
					append("getImage").
					append("&xmin=").
					append(envelope.getMinX()).
					append("&xmax=").
					append(envelope.getMaxX()).
					append("&ymin=").
					append(envelope.getMinY()).
					append("&ymax=").
					append(envelope.getMaxY());
			URL url = new URL(request.toString());
			//in = new DataInputStream(new BufferedInputStream(url.openStream()));
			//creo que lo que le llega del Servlet es ya una imagen JPEG
			//Si quisiesemos leer content-types y tal, llamariamos a url.openConnection
			//solucion = readImageAsJPG();

		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return solucion;
	}
	//getImagen


	/*
	 *  (non-Javadoc)
	 *  @see org.agil.kernel.jump.coverage.ImageDataAccesor#getEnvelope()
	 */
			/**
	 *  Gets the Envelope attribute of the ServletImageDataAccesor object
	 *
	 *@return    The Envelope value
	 */
	public Envelope getEnvelope() {
		Envelope solucion = null;
		try {
			StringBuffer request = new StringBuffer(urlServlet).
					append(peticion).
					append("getEnvelope");
			URL url = new URL(request.toString());
			//in = new DataInputStream(new BufferedInputStream(url.openStream()));
			//TODO ahora hay que leer el html enviado por el Servlet, y construir el Envelope

		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return solucion;
	}

}
