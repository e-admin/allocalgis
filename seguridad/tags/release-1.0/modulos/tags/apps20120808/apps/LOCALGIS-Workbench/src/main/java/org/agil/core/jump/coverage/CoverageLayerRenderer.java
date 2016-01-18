package org.agil.core.jump.coverage;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.agil.core.coverage.AgilImageDataAccesor;
import org.agil.core.coverage.Coverage;
import org.agil.core.coverage.EarthImage;
import org.agil.core.coverage.GISGraphics;
import org.agil.core.coverage.GridCoverage;
import org.agil.core.coverage.GridCoverageCollection;
import org.agil.core.coverage.ImageDataAccesor;
import org.agil.core.util.image.GeoReferencedBufferedImage;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.Drawer;
import com.vividsolutions.jump.workbench.ui.renderer.ImageCachingRenderer;
import com.vividsolutions.jump.workbench.ui.renderer.ThreadSafeImage;

/**
 *  Implementacion de AbstractRenderer que se encarga de dibujar layers del
 *  tipo CoverageLayer en el dispositivo del mapa (LayerViewPanel)
 *
 *@author    alvaro zabala 29-sep-2003
 */
public class CoverageLayerRenderer extends ImageCachingRenderer {

	/**
	 *  Constructor
	 *
	 *@param  layer  coverageLayer que se encargara de dibujar este renderer
	 *@param  panel  componente de dibujo
	 */
	public CoverageLayerRenderer(CoverageLayer layer, LayerViewPanel panel) {
		super(layer, panel);
	}


	/**
	 *  Devuelve una imagen sobre la que se dibujara el contenido de la
	 *  coverageLayer TODO ver si se puede crear una NonThreadSafeImage que no
	 *  sincronice los metodos de dibujo, para ver si va mas rapido
	 *
	 *@return    imagen cuyos metodos de dibujo son sincronizados
	 */
	public ThreadSafeImage getImage() {
		if (!getLayer().isVisible()) {
			return new ThreadSafeImage(panel);
		}
		return super.getImage();
	}


	/**
	 *  Crea el proceso encargado de dibujar los datos del layer asociado en un
	 *  thread dedicado (si el layer no esta visible, no se crea thread)
	 *
	 *@return    implementacion de runnable para ser ejecutada en un thread
	 *      propio (o bien null si el layer no es visible)
	 */
	public Runnable createRunnable() {
		if (!getLayer().isVisible()) {
			//If the cached image is null, leave it alone. [Jon Aquino]
			return null;
		}
		return super.createRunnable();
	}


	/**
	 *  Metodo principal de toda implementacion de renderer. Se encarga de
	 *  dibujar el layer sobre el dispositivo de dibujo (threadSafeImage)
	 *
	 *@param  image          imagen del dispositivo sobre el que se dibujara el
	 *      renderer. ThreadSafeImage es el equivalente, para JUMP, al
	 *      GISGraphics nuestro. TODO (REVISAR POSIBLES HERENCIAS)
	 *@exception  Exception  Description of Exception
	 *@see                   com.vividsolutions.jump.workbench.ui.renderer.AbstractRenderer#renderHook(com.vividsolutions.jump.workbench.ui.renderer.ThreadSafeImage)
	 */
	protected void renderHook(ThreadSafeImage image) throws Exception {
		// TODO Auto-generated method stub
		if (!getLayer().isVisible()) {
			return;
		}

		//Si queremos optimizar el Renderer, para que las imagenes se puedan dibujar
		//con cancelaciones (en el caso de que trabajen con TILES) y actualizando la pantalla
		//cada N tiles, debemos eliminar este metodo y REDISEÑAR Coverage.
		//El tema está que en imagenes tipo ECW si queremos llamar a getBufferedImage,
		//mientras que en imagenes basadas en JAI podriamos llamar a getImageDataAccesor
		CoverageLayer coverageLayer = (CoverageLayer) getLayer();
		Coverage coverage = coverageLayer.getCoverage();
		//Prueba para usar tiles progresivos
		if(coverage instanceof GridCoverage && ! (coverage instanceof GridCoverageCollection)){
			EarthImage earthImage = (EarthImage) ((GridCoverage)coverage).getFunction();
			ImageDataAccesor dataAccesor = earthImage.getDataAccesor();
			if(dataAccesor instanceof AgilImageDataAccesor){
				AgilImageDataAccesor dao = (AgilImageDataAccesor)dataAccesor;
				Envelope envelope = panel.getViewport().getEnvelopeInModelCoordinates();
				int width = Math.max(panel.getWidth(),1);
				int height = Math.max(panel.getHeight(),1);
				final BufferedImage solucion = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_ARGB); 
				Graphics gB = solucion.getGraphics();
				GISGraphics gisGraphics = new GISGraphics(envelope, height, width, gB);
				dao.setArea(envelope.getMinX(), envelope.getMinY(), envelope.getMaxX(), envelope.getMaxY());
				int numTiles=0;
				while(dao.hasNext()){
					if(this.cancelled)
						break;
					//obtenemos el tile TODO Aquí podríamos usar una caché de tiles
					GeoReferencedBufferedImage geoImage = dao.nextTile();
					double xminIm = geoImage.getMer().getMinX();
					double ymaxIm = geoImage.getMer().getMaxY();
					double xmaxIm = geoImage.getMer().getMaxX();
					double yminIm = geoImage.getMer().getMinY();
					BufferedImage buffer = (BufferedImage) geoImage.getImage();
					gisGraphics.drawImagen(buffer,xminIm,xmaxIm,yminIm,ymaxIm);	
					
					/*
					int x0 = xworld2xpix(areaOfInterest.getMinX());
					int x1 = xworld2xpix(areaOfInterest.getMaxX());
					int y0 = yworld2ypix(areaOfInterest.getMaxY());
					int y1 = yworld2ypix(areaOfInterest.getMinY());
					int width = x1 - x0;
					int height = y1 - y0;
					BufferedImage imagen = coverage.getBufferedImage(width, height, areaOfInterest);
					graphics.drawImage(imagen, x0, y0, x1 - x0, y1 - y0, null);
					*/
					
					
					
					
					image.draw(new Drawer() {
						public void draw(Graphics2D g) throws Exception {
							/*
							g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
									getLayer().getAlpha() / 255f));
							*/
							RenderingHints hints = getLayer().getRenderingHints();
							g.setRenderingHints(hints);
							g.drawImage(solucion, 0, 0, null);
						}
						//draw
					});
					//Thread.currentThread().sleep(2000);
					numTiles++;
					/*
					 * if((numTiles%10 ==0)&& (observer!=null))
						observer.actualizate();
					*/
					//System.out.println("Procesadas:"+numTiles);
				}//while
				return;
			}//AgilImageDataAccesor
		}//gridCoverage
		//Llegados a este punto no se ha renderizado
		final Image sourceImage = getLayer().createImage(panel);

		if (sourceImage == null) {
			return;
		}

		//Drawing can take a long time. If the renderer is cancelled during this
		//time, don't draw when the request returns. [Jon Aquino]
		if (cancelled) {
			return;
		}
		//TODO revisar la forma de dibujar de la clase ThreadSafeImage para integrarla con GISGraphics
		image.draw(
					new Drawer() {
						public void draw(Graphics2D g) throws Exception {
							g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
									getLayer().getAlpha() / 255f));
							RenderingHints hints = getLayer().getRenderingHints();
							g.setRenderingHints(hints);
							g.drawImage(sourceImage, 0, 0, null);
						}
						//draw
					});
	}


	/**
	 *  Devuelve la Layer (del subtipo CoverageLayer) asociada al Renderer.
	 *
	 *@return    layer del subtipo CoverageLayer
	 */
	private CoverageLayer getLayer() {
		return (CoverageLayer) getContentID();
	}

}
