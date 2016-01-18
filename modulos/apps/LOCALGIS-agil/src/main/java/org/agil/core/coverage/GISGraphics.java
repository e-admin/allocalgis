/**
 * GISGraphics.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.agil.core.coverage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.workbench.ui.renderer.Drawer;
//Importamos una clase interna

/**
 *  Esta clase representa un contexto de dispositivo, con una ventana
 *  geografica asociada (puerto de vision en coordenadas reales) con el que se
 *  pueden realizar operaciones de dibujo en coordenadas geograficas/reales.
 *  Se utiliza para dibujar graficos en memoria, no asociados a dispositivos
 *  en pantalla. Esto puede ser de utilidad para implementar la tecnica de
 *  Doble Buffer (que evita el efecto de flicker o parpadeo en pantalla) y
 *  para crear servidores de imagenes. TODO JUMP tiene su propio esquema de
 *  renderizado (RenderingManager) pero esta fuertemente acoplado con la
 *  utilizacion de componentes en pantalla (LayerViewManager).
 *
 *
 */
public class GISGraphics {

	/**
	 *  ancho del dispositivo en ud. específicas del propio dispositivo (pixels)
	 */
	private int ancho;

	/**
	 *  alto del dispositivo en unidades específicas del mismo dispositivo
	 *  (pixels)
	 */
	private int alto;

	/**
	 *  offset en x del dispositivo. de utilidad por si no se desea empezar a
	 *  dibujar en el mismo origen (0,0) del dispositivo
	 */
	private int offsetX;

	/**
	 *  offset en y del dispositivo. util por si no se desea empezar a dibujar en
	 *  el mismo origen y del dispositivo
	 */
	private int offsetY;

	/**
	 *  manejador del dispositivo sobre el que se pretende dibujar. Puede ser una
	 *  impresora, un componente grafico, etc. etc.
	 */
	private Graphics graphics;

	/**
	 *  coordenadas reales -puerto de vision- asociadas al dispositivo. TODO Ver
	 *  si se puede cambiar por el Viewport de JUMP
	 */
	private Envelope rect;


	/**
	 *  Constructor for the GISGraphics object
	 *
	 *@param  mer    Description of the Parameter
	 *@param  alto   Description of Parameter
	 *@param  ancho  Description of Parameter
	 *@param  g      Description of Parameter
	 */
	public GISGraphics(Envelope mer, int alto, int ancho, Graphics g) {
		this.rect = mer;
		this.ancho = ancho;
		this.alto = alto;
		this.graphics = g;
	}


	/**
	 *  Constructor por defecto
	 */
	public GISGraphics() {
		this.rect = new com.vividsolutions.jts.geom.Envelope();
	}


	/**
	 *  Establece el alto en pixeles del dispositivo
	 *
	 *@param  alto  alto en unidades dispositivo
	 */
	public void setAlto(int alto) {
		this.alto = alto;
	}


	/**
	 *  Establece el ancho en unidades dispositivo
	 *
	 *@param  ancho  ancho en pixeles
	 */
	public void setAncho(int ancho) {
		this.ancho = ancho;
	}


	/**
	 *  Establece el manejador de dispositivo con el que estamos trabajando
	 *
	 *@param  graphicsValue  The new graphics value
	 */
	public void setGraphics(Graphics graphicsValue) {
		this.graphics = graphicsValue;
	}


	/**
	 *  Establece el puerto de vision (coordenadas reales de las esquinas del
	 *  dispositivo de dibujo)
	 *
	 *@param  rectValue  nuevo puerto de vision
	 */
	public void setRect(Envelope rectValue) {
		this.rect = rectValue;
	}


	/**
	 *  Devuelve el alto en pixeles
	 *
	 *@return    alto en pixeles
	 */
	public int getAlto() {
		return this.alto;
	}


	/**
	 *  devuelve el ancho en pixeles
	 *
	 *@return    ancho en pixeles
	 */
	public int getAncho() {
		return this.ancho;
	}


	/**
	 *  devuelve el puerto de vision
	 *
	 *@return    puerto de vision
	 */
	public Envelope getRect() {
		return rect;
	}


	/**
	 *  este acceso se debe quitar, y CENTRAR LA LOGICA DE DIBUJO EN ESTA CLASE
	 *  (y no en la clase GeoReferencedImage)
	 *
	 *@return    The graphics value
	 */
	public Graphics getGraphics() {
		return graphics;
	}


	/**
	 *  devuelve la escala de trabajo del contexto de dispositivo. ahora mismo se
	 *  esta trabajando con el ancho real en metros, aunque se pueden utilizar
	 *  otras magnitudes independientes del dispositivo: metros/pixel etc.
	 *
	 *@return    The factorEscala value
	 */
	public double getFactorEscala() {

		return rect.getWidth();
	}


	/**
	 *  Establece el color de fondo
	 *
	 *@param  color  Description of Parameter
	 */
	public void fill(Color color) {
		Graphics2D g = (Graphics2D) graphics;
		//Seguramente graphics pase a ser Graphics2D
		g.setColor(color);
		g.fillRect(0, 0, this.alto, this.ancho);
	}


	/**
	 *  Permite dibujar la imagen de una GridCoverage sobre el dispositivo. En
	 *  este caso, puesto que se dan las 4 coordenadas de la imagen, tenemos 2
	 *  escalas de trabajo (dispositivo e imagen) por lo que la imagen se escala
	 *  para adaptarse a la escala del dispositivo.
	 *
	 *@param  coverage  cobertura que se pretende dibujar en el dispositivo
	 */
	public void drawImagen(GridCoverage coverage) {

//			solo nos interesa la parte que intersecta el puerto de vision
		Envelope areaOfInterest = clip(coverage.getEnvelope());

		int x0 = xworld2xpix(areaOfInterest.getMinX());
		int x1 = xworld2xpix(areaOfInterest.getMaxX());
		int y0 = yworld2ypix(areaOfInterest.getMaxY());
		//se invierten los ejes de coordenadas
		int y1 = yworld2ypix(areaOfInterest.getMinY());

		int width = x1 - x0;
		//ancho en pixels que tendra la imagen a dibujar
		int height = y1 - y0;
		//alto en pixels de la imagen a dibujar

		BufferedImage imagen = coverage.getBufferedImage(width, height, areaOfInterest);
		if (imagen == null) {
			//en datos remotos cuando la zona no vale devuelve null

			System.out.println("imagen nula!");
			return;
		}

		graphics.drawImage(imagen, x0, y0, x1 - x0, y1 - y0, null);
	}
	
	public void drawImagen(BufferedImage image, double xmin, double xmax, double ymin, double ymax){
		Envelope envelope = new Envelope(xmin, xmax , ymin , ymax);
		//Envelope areaOfInterest = clip(envelope);
		Envelope areaOfInterest = envelope;
		int x0p = xworld2xpix(areaOfInterest.getMinX());
		int x1p = xworld2xpix(areaOfInterest.getMaxX());
		int y0p = yworld2ypix(areaOfInterest.getMaxY());//se invierten los ejes de coordenadas
		int y1p = yworld2ypix(areaOfInterest.getMinY());
		int width = x1p - x0p;
		//ancho en pixels que tendra la imagen a dibujar
		int height = y1p - y0p;
		//alto en pixels de la imagen a dibujar
		if (image == null) {
			//en datos remotos cuando la zona no vale devuelve null
			System.out.println("imagen nula!");
			return;
		}
		graphics.drawImage(image, x0p, y0p, x1p - x0p, y1p - y0p, null);
	}


	/**
	 *  Dibuja una imagen con origen en el origen de coordenadas del dispositivo
	 *  (pero conservando su tamaño en pixels) Esto es util para iconos (señales,
	 *  simbolos) que no se deseen escalar
	 *
	 *@param  imagen  imagen a dibujar
	 *@param  xMin    x real donde se desea colocar la esquina superior izquierda
	 *      de la imagen
	 *@param  yMax    y real donde se desea colocar la esquina superior izquierda
	 */
	public void drawImagen(BufferedImage imagen, double xMin, double yMax) {
		int x0 = xworld2xpix(xMin);
		int y0 = yworld2ypix(yMax);
		graphics.drawImage(imagen, x0, y0, null);

	}


	/**
	 *  Este metodo nos ofrece compatibilidad con las clases de dibujo de JUMP
	 *  (StileUtil, BasicStile, etc) TODO De momento, se queda sincronizado,
	 *  aunque veremos si finalmente sera asi
	 *
	 *@param  drawer      implementacion de la interfaz Drawer (clase interna de
	 *      ThreadSafeImage) que permite dibujar sobre el dispositivo grafico. Se
	 *      supone que cada estilo define su propio drawer (esto no es OGC
	 *      compliant)
	 *@throws  Exception
	 */
	public synchronized void draw(Drawer drawer) throws Exception {
 
		drawer.draw((Graphics2D) graphics);
	}


	/**
	 *  clipea un rectangulo(normalmente un bounding box / view box) contra el
	 *  bounding box del puerto de vision. Es de utilidad en el caso de
	 *  Coverages, para solo cargar la porcion de imagen clipeada por el viewport
	 *  del dispositivo.
	 *
	 *@param  boundingBox  rectangulo que se pretende recortar contra el puerto
	 *      de vision del dispositivo
	 *@return              rectangulo clipeado
	 */
	public Envelope clip(Envelope boundingBox) {
		double x;
		double y;
		double X;
		double Y;
		//rectangulo que se va a devolver
		//coordenadas del rectangulo de clip devuelto

		if (!boundingBox.intersects(this.rect)) {
			return null;
		}
		//no intersecan

		if (boundingBox.getMinX() > rect.getMinX()) {
			x = boundingBox.getMinX();
		}
		else {
			x = rect.getMinX();
		}

		if (boundingBox.getMaxX() > rect.getMaxX()) {
			X = rect.getMaxX();
		}
		else {
			X = boundingBox.getMaxX();
		}

		if (boundingBox.getMinY() > rect.getMinY()) {
			y = boundingBox.getMinY();
		}
		else {
			y = rect.getMinY();
		}

		if (boundingBox.getMaxY() > rect.getMaxY()) {
			Y = rect.getMaxY();
		}
		else {
			Y = boundingBox.getMaxY();
		}

		return new Envelope(x, X, y, Y);
	}



	/**
	 *  convierte coordenadas x reales a coordenadas pixels
	 *
	 *@param  xWorld  coordenada x real
	 *@return         coordenada x pixel
	 */
	public int xworld2xpix(double xWorld) {
		double f = rect.getWidth() / (double) this.ancho;
		return (int) ((xWorld - rect.getMinX()) / f) + offsetX;
	}


	/**
	 *  convierte y real a y pixel
	 *
	 *@param  yworld  y real
	 *@return         y pixel
	 */
	public int yworld2ypix(double yworld) {
		double f = rect.getHeight() / (double) this.alto;
		return (int) ((rect.getMaxY() - yworld) / f) + offsetY;
	}
}
