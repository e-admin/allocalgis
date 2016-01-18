package es.satec.svgviewer.localgis.ecw;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Display;

import com.ermapper.ecw.JNCSException;
import com.ermapper.ecw.JNCSFile;

public class EcwReader {

	private static Logger logger = (Logger) Logger.getInstance(EcwReader.class);

	private Vector loadedFiles;
	
	private Image offscreen;
	
	public EcwReader() {
		loadedFiles = new Vector();
		offscreen = null;
	}
	
	public Vector getLoadedFiles() {
		return loadedFiles;
	}

	public JNCSFile loadEcw(String ecwFilePath) throws JNCSException {
		if (ecwFilePath == null) throw new IllegalArgumentException();

		File f = new File(ecwFilePath);
		if (!f.exists()) throw new JNCSException("El fichero no existe");

		return readEcw(ecwFilePath);
	}
	
	public void unloadEcw(int index) {
		if (index >= 0 && index < loadedFiles.size()) {
			loadedFiles.removeElementAt(index);
		}
	}
	
	public void setVisible(int index, boolean visible) {
		if (index >= 0 && index < loadedFiles.size()) {
			JNCSFile file = (JNCSFile) loadedFiles.elementAt(index);
			file.setVisible(visible);
		}
		
	}

	public boolean somethingToDisplay() {
		if (loadedFiles != null && !loadedFiles.isEmpty()) {
			for (int i=0; i<loadedFiles.size(); i++) {
				JNCSFile file = (JNCSFile) loadedFiles.elementAt(i);
				if (file.isVisible()) return true;
			}
		}
		return false;
	}

	private JNCSFile readEcw(String ecwFilePath) throws JNCSException {
		JNCSFile file = new JNCSFile(ecwFilePath, false);
		loadedFiles.addElement(file);
		return file;
	}

	public Image paint(double xmin, double ymin, double xmax, double ymax, int width, int height) {
		logger.debug("Dibuja ecw en (" + xmin + ", " + ymin + ") - (" + xmax + ", " + ymax + ") Tamaño: " + width + "x" + height);

		if (offscreen != null && !offscreen.isDisposed()) {
			offscreen.dispose();
		}

		offscreen = new Image(Display.getCurrent(), width, height);
		GC gc = new GC(offscreen);
		
		if (loadedFiles != null && !loadedFiles.isEmpty()) {
			
			double scrRatioX = width/(xmax-xmin);
			double scrRatioY = height/(ymax-ymin);
			
			Enumeration en = loadedFiles.elements();
			while (en.hasMoreElements()) {
				Image fileImage = null;
				try {
					JNCSFile file = (JNCSFile) en.nextElement();
					
					if (!file.isVisible()) continue;
					
					double ecwxmin = file.originX;
					double ecwymin = file.originY+file.height*file.cellIncrementY;
					double ecwxmax = file.originX+file.width*file.cellIncrementX;
					double ecwymax = file.originY;
					
					if (ecwxmax < xmin || ecwymin > ymax || ecwxmin > xmax || ecwymax < ymin) continue;
					
					int bandlist[] = new int[file.numBands];
					for (int i=0; i< file.numBands; i++) {
						bandlist[i] = i;
					}
	
					// Coordenadas del fichero a mostrar en pantalla
					int tlx = (int) ((xmin-file.originX)/file.cellIncrementX);
					if (tlx<0) tlx = 0;
					int tly = (int) ((ymax-file.originY)/file.cellIncrementY);
					if (tly<0) tly = 0;
					int brx = (int) ((xmax-file.originX)/file.cellIncrementX);
					if (brx>=file.width) brx = file.width-1;
					int bry = (int) ((ymin-file.originY)/file.cellIncrementY);
					if (bry>=file.height) bry = file.height-1;
					logger.debug("Coordenadas Dataset: (" + tlx + ", " + tly + ") - (" + brx + ", " + bry + ")");
	
					// Coordenadas del mundo real a mostrar en pantalla
					double tlxWorld = file.originX+tlx*file.cellIncrementX;
					double tlyWorld = file.originY+tly*file.cellIncrementY;
					double brxWorld = file.originX+brx*file.cellIncrementX;
					double bryWorld = file.originY+bry*file.cellIncrementY;
					logger.debug("Coordenadas world: (" + tlxWorld + ", " + tlyWorld + ") - (" + brxWorld + ", " + bryWorld + ")");
					
					// Coordenadas de pantalla
					int scrx = (int) ((tlxWorld-xmin)*scrRatioX);
					if (scrx < 0) scrx = 0;
					int scry = (int) ((ymax-tlyWorld)*scrRatioY);
					if (scry < 0) scry = 0;
					int scrwidth = (int) ((brxWorld-tlxWorld)*scrRatioX);
					if (scrwidth >= width) scrwidth = width-1;
					int scrheight = (int) ((tlyWorld-bryWorld)*scrRatioY);
					if (scrheight >= height) scrheight = height-1;
					logger.debug("Coordenadas pantalla: (" + scrx + ", " + scry + ") width=" + scrwidth + ", height=" + scrheight);
					
					// Calculo del tamaño de la imagen a generar
					int imgwidth = scrwidth;
					int imgheight = scrheight;
					if ((scrwidth > (brx-tlx)) || (scrheight > (bry-tly))) {
						double ratiox = (brx-tlx)/(scrwidth*1.0);
						double ratioy = (bry-tly)/(scrheight*1.0);
						if (ratiox < ratioy) {
							imgwidth = brx-tlx;
							imgheight = (int) (imgheight*ratiox);
							if (imgheight > (bry-tly))
								imgheight = bry-tly;
						}
						else {
							imgheight = bry-tly;
							imgwidth = (int) (imgwidth*ratioy);
							if (imgwidth > (brx-tlx))
								imgwidth = brx-tlx;
						}
					}
					logger.debug("Tamaño imagen: " + imgwidth + "x" + imgheight);

					// Generacion y dibujado de la imagen
					file.setView(file.numBands, bandlist, tlx, tly, brx, bry, imgwidth, imgheight);
					
					int pRGBArray[] = new int[imgwidth*imgheight];
					file.readImageRGBA(pRGBArray, imgwidth, imgheight);
					
		            ImageData imageData = new ImageData(imgwidth, imgheight, 32, new PaletteData(0xFF0000, 0xFF00, 0xFF));
		            imageData.setPixels(0, 0, imgwidth*imgheight, pRGBArray, 0);
		            fileImage = new Image(Display.getCurrent(), imageData);

		            if (imgwidth==scrwidth && imgheight==scrheight) {
		            	gc.drawImage(fileImage, scrx, scry);
		            }
		            else {
		            	gc.drawImage(fileImage, 0, 0, imgwidth, imgheight, scrx, scry, scrwidth, scrheight);
		            }
				} catch (Exception e) {
					logger.error("Error al dibujar ECW", e);
				} finally {
					if (fileImage != null && !fileImage.isDisposed()) {
						fileImage.dispose();
					}
				}
			}
		}
		
		gc.dispose();
		
		return offscreen;
	}
}
