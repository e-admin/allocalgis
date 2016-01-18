package org.agil.core.coverage;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
 
/**
 *  Título: SpatialServerServlet Descripcion: Servlet encargado de enviar
 *  mapas con las capas solicitadas por el Usuario, para la ventana geografica
 *  x,y,X,Y (ViewBox) solicitada por éste. Este Servlet pretende soportar la
 *  funcionalidad ofrecida por ArcIMS. Al trabajar con una imagen en memoria,
 *  para entornos Unix hay que tener en cuenta las HeadLess exceptions.
 *  Copyright: Copyright (c) 2002 Empresa: Asociacion Para la Promoción del GIS Libre
 *
 *@author     Alvaro Zabala Ordoñez
 *@created    12 de marzo de 2003
 *@version    1.0
 */

public class SpatialServerServlet extends HttpServlet {
	private GISGraphics gisGraphics;

	private static final String CONTENT_TYPE = "image/jpeg";



	/**
	 *  Description of the Method
	 *
	 *@exception  ServletException  Description of the Exception
	 */
	public void init() throws ServletException {
		gisGraphics = new GISGraphics();

		//EL PROBLEMA VIENE CON LA APERTURA DE IMAGENES (TARDAN MUCHO EN CREARSE)
		//POR ESTO, TODAS LAS IMAGENES DISPONIBLES SE DEBEN PODER CREAR ANTES
		//(BIEN EN UN SERVIDOR DEDICADO, BIEN EN EL METODO INIT)



	}


	/**
	 *  Procesar una petición HTTP Post
	 *
	 *@param  request               Description of the Parameter
	 *@param  response              Description of the Parameter
	 *@exception  ServletException  Description of the Exception
	 *@exception  IOException       Description of the Exception
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType(CONTENT_TYPE);
		//Usuario usuario = null;
		HttpSession sesion = null;

		//Primeramente comprobamos que existe una Sesion, y que esta tiene un Usuario
		try {
			if (request.isRequestedSessionIdValid()) {
				//si la sesion es valida

				sesion = request.getSession(false);

				if (sesion.getAttribute("USUARIO") == null) {
					//pero no tiene un usuario asociado
					request.setAttribute("MOSTRAR_ERROR", "Usuario no válido");
					getServletContext().getRequestDispatcher("/jsp/errores/error.jsp").forward(request, response);
				}
			}
			else {
				request.setAttribute("MOSTRAR_ERROR", "La sesión ha caducado");
				getServletContext().getRequestDispatcher("/jsp/errores/error.jsp").forward(request, response);
				return;
			}

			//usuario = (Usuario) sesion.getAttribute("USUARIO");
/*
			int width = usuario.getGisWorkSpace().getVisor().getAncho();
			int height = usuario.getGisWorkSpace().getVisor().getAlto();

			double xMin = usuario.getGisWorkSpace().getVisor().getXMin();
			double xMax = usuario.getGisWorkSpace().getVisor().getXMax();
			double yMin = usuario.getGisWorkSpace().getVisor().getYMin();
			double yMax = usuario.getGisWorkSpace().getVisor().getYMax();
*/
                        int width = 0, height = 0;
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();
			g.setColor(java.awt.Color.white);
			g.fillRect(0, 0, width, height);

			gisGraphics.setGraphics(g);
			gisGraphics.setAlto(height);
			gisGraphics.setAncho(width);
			//gisGraphics.setArea(xMin, yMin, xMax, yMax);
//            org.agil.kernel.jump.coverage.MapModel mapModel = usuario.getGisWorkSpace().getMapModel();
			System.out.println("Antes de dibujar el Mapa");
//            mapModel.pintar(gisGraphics);
			System.out.println("Despues de dibujar el Mapa");
			g.dispose();

			System.out.println("Vamos a enviar la imagen!!!!!!!!!!");
			ServletOutputStream sos = response.getOutputStream();
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(sos);
			encoder.encode(image);
			sos.close();
			System.out.println("Imagen enviada");

			//ahora la guardamos a fichero, para comprobar
//      java.io.FileOutputStream file = new java.io.FileOutputStream(new File("c:/imagen.jpg"));
//      JPEGImageEncoder encoder2 = JPEGCodec.createJPEGEncoder(file);
//      encoder2.encode(image);
//      file.close();
//      System.out.println("Generada imagen temporal");



		}
		catch (Exception e) {
			System.out.println(e);
			System.out.println(e.getMessage());
			request.setAttribute("MOSTRAR_ERROR", "En estos momentos el servidor de mapas no está disponible.");
			getServletContext().getRequestDispatcher("/jsp/errores/error.jsp").forward(request, response);
			return;
		}

	}


	/**
	 *  Description of the Method
	 *
	 *@param  request               Description of the Parameter
	 *@param  response              Description of the Parameter
	 *@exception  ServletException  Description of the Exception
	 *@exception  IOException       Description of the Exception
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doPost(request, response);
	}



	/**
	 *  Limpiar recursos
	 */
	public void destroy() {
	}
}
