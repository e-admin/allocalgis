/*
 * Creado el 28-mar-2004
 *
 * Este codigo se distribuye bajo licencia GPL
 * de GNU. Para obtener una cópia integra de esta
 * licencia acude a www.gnu.org.
 * 
 * Este software se distribuye "como es". AGIL
 * solo  pretende desarrollar herramientas para
 * la promoción del GIS Libre.
 * AGIL no se responsabiliza de las perdidas económicas o de 
 * información derivadas del uso de este software.
 */
package org.agil.core.coverage;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.agil.core.jump.coverage.TfwFile;
import org.agil.core.util.image.GeoReferencedBufferedImage;
import org.agil.core.util.image.GeoReferencedRenderedImage;
import org.agil.core.util.image.ImageLoader;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

/**
 * Implementación de ImageDataAccesor que es capaz de leer imagenes
 * de las recogidas en el paquete org.agil.util.image
 * 
 * 
 * @author Alvaro Zabala (AGIL)
 *
 */
public class AgilImageDataAccesor implements ImageDataAccesor {
	
	public final String TFW_EXT = ".tfw";
	/**
	 * Rectángulo geográfico que georreferencia la imágen
	 */
	private Envelope imageEnvelope;
	
	/*
	 * Las imágenes gestionadas por el paquete org.agil.util.image admiten la subdivisión
	 * en Tiles (esta forma de trabajar es similar a la de JAI)
	 * 
	 * Estos son los valores maximo y mínimo de coordenadas tile de la imagen
	 */
    private int leftIndex;
    private int rightIndex;
    private int bottomIndex;
    private int topIndex;
    
	/**coordenadas del siguiente tile que se va a devolver*/
	private int actualColumna;
	private int actualFila;
    /**
     * Representación de la imagen
     */
	private GeoReferencedRenderedImage image;
	private String imagePath;
	/**
	 * Clase encargada de generar imágenes a partir de la información
	 * contenida en los archivos de imagen
	 */
	//private PintaRasters pintor;
	/**
	 * Separar el rendering del acceso a datos
	 * (quitar de aqui)
	 */
	//private PintaRasterObserver observer;

	public AgilImageDataAccesor()
	{}
    /**
     * Constructor. Recibe la ruta de la imagen de la que se recuperarán los
     * fragmentos solicitados por el usuario.
     * 
     * @param rutaImagen 
     */
	public AgilImageDataAccesor(String rutaImagen) throws TfwNoAvailableException{
		setImagePath(rutaImagen);
	}
	
	/**
		 * Proporciona al iterador el area de la LocalTesela sobre la que va a iterar.
		 * (area que restringirá los tiles que se van recorriendo)
		 *
		 * @param xmin xmin del area sobre la que se va a iterar.
		 * @param ymin ymin del area sobre la que se va a iterar.
		 * @param xmax xmax del area sobre la que se va a iterar.
		 * @param ymax ymax del area sobre la que se va a iterar.
		 */
		public void setArea(double xmin,double ymin,double xmax,double ymax){
			leftIndex = image.getTileX(xmin);
			rightIndex = image.getTileX(xmax);
			topIndex = image.getTileY(ymin);
			bottomIndex = image.getTileY(ymax);

			//Comprobaciones de rango (para evitar valores numericos que excedan de los limites)
			if(leftIndex<image.getMinTileX())
				leftIndex=image.getMinTileX();
			if(rightIndex>image.getMaxTileX())
				rightIndex=image.getMaxTileX();
			if(topIndex>image.getMaxTileY())
				topIndex=image.getMaxTileY();
			if(bottomIndex<image.getMinTileY())
				bottomIndex=image.getMinTileY();

			actualColumna=leftIndex;
			actualFila=bottomIndex;
		}

		/**
		 * Indica si hay mas tiles que recorrer en el iterador.
		 * @return valor logico que indica si hay mas tiles que recorrer.
		 *
		 */
		public boolean hasNext(){
		  if((actualFila>topIndex)||(actualColumna>rightIndex))
				return false;
			else
				return true;//no comprobamos el indice de las columnas
				//pq se supone que nextTile() lo actualiza
		}

		/**
		 * Devuelve el siguiente tile del iterador.
		 * @return siguiente tile, en forma de GeoReferencedBufferedImage
		 */
		public GeoReferencedBufferedImage nextTile(){

			//primero obtenemos su imagen
			//hay que añadir comprobaciones de rango para devolver null
			//si los valores de actualColumna o actualFila no son consecuentes
			//o por si no se ha chequeado el metodo hasNext()

			BufferedImage buff = null;
			//String key = null;
			try{
//				System.out.println("Pedimos el tile ("+actualColumna+","+actualFila);
			  	buff=image.getTile(actualColumna,actualFila);
			  	if(false)
			  		debug(buff);
			}catch(Exception e){
			  System.out.println("imagen fuera de rango");
			  return null;
			}
				
			//obtenemos las coordenadas reales del tile, para su georeferenciacion
			double xMin=image.tileXtoRealX(actualColumna);//xmin
			double yMax=image.tileYtoRealY(actualFila);//ymax

			double xMax = xMin+image.getRealTileWidht();
			double yMin = yMax-image.getRealTileHeight();

			GeoReferencedBufferedImage solucion = new
				GeoReferencedBufferedImage(buff,new Envelope(xMin,xMax,yMin,yMax));

			//actualizamos los 'cursores' que apuntan al siguiente tile a devolver
			actualColumna++;
			if(actualColumna>rightIndex){//Pasamos a la siguiente fila, iniciando el recorrido
				//desde la primera columna
				actualColumna=leftIndex;
				actualFila++;
			}//if
			return solucion;
		}

	/**
	 * Devuelve una imagen de la zona especificada con el tamaño solicitado
	 */
	public BufferedImage getImagen(Envelope envelope, int width, int height) {
		//creamos la imagen del tamaño especificado
		 //los codecs JPEG solo admiten TYPE_BYTE
		 BufferedImage buff = new BufferedImage(width,height,
				BufferedImage.TYPE_INT_RGB);

		 Graphics2D gB = (Graphics2D)buff.getGraphics();

		 //determinacion del factor de escala para pasar de metros a pixels del dispositivo
		/*
		 double fx = (envelope.getWidth())/width;
		 double fy = (envelope.getHeight())/height;
		 double f= (fx>fy)?fx:fy;
		  if(pintor==null)
				pintor = new PintaRasters();
		  pintor.pinta(gB,null,envelope,f,0,0);
		 */
		 
		GISGraphics gisGraphics = new GISGraphics(envelope, height, width, gB);
		setArea(envelope.getMinX(), envelope.getMinY(), envelope.getMaxX(), envelope.getMaxY());
		int numTiles=0;
		while(hasNext()){
			//obtenemos el tile TODO Aquí podríamos usar una caché de tiles
			GeoReferencedBufferedImage geoImage = nextTile();
			double xminIm = geoImage.getMer().getMinX();
			double ymaxIm = geoImage.getMer().getMaxY();
			double xmaxIm = geoImage.getMer().getMaxX();
			double yminIm = geoImage.getMer().getMinY();
			BufferedImage buffer = (BufferedImage) geoImage.getImage();
			
			if(false)//esto el compilador lo desecha. El tema esta en generar imagenes 
				debug(buffer);
			
			//NO DIBUJA BIEN , PERO LAS TILES SE GENERAN BIEN
			gisGraphics.drawImagen(buffer,xminIm,xmaxIm,yminIm,ymaxIm);	
			numTiles++;
			/*
			 * if((numTiles%10 ==0)&& (observer!=null))
				observer.actualizate();
			*/
//			System.out.println("Procesadas:"+numTiles);
		}//while
		//debug(buff);  
		return buff;
	}

	/**
	 * @param buffer
	 * @param width
	 * @param height
	 */
	private void debug(BufferedImage buffer) {
		File file = new File("c:/"+this.actualColumna+","+this.actualFila+".jpg");
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(buffer);
		param.setQuality(1.0f, false);
		encoder.setJPEGEncodeParam(param);
		try {
			encoder.encode(buffer);
		} catch (ImageFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	/**
	 * Devuelve los datos de georreferenciación de la imagen
	 */
	public Envelope getEnvelope() {
		return imageEnvelope;
	}
	
	class PintaRasters{
			public PintaRasters(){
			}
			
			/**
			 * ESTO ESTÁ FALLANDO. CONFRONTAR CON ECWIMAGEDATAACCESOR (IGUAL SE PUEDE USAR GISGRAPHICS)
			 * @param g
			 * @param observer
			 * @param envelope
			 * @param factor
			 * @param ox
			 * @param oy
			 */
			public void pinta(Graphics g,PintaRasterObserver observer,Envelope envelope
										,double factor,int ox,int oy){
	
	
	  		//delimitamos los tiles sobre los que vamos a iterar.
	  		setArea(envelope.getMinX(), envelope.getMinY(), envelope.getMaxX(), envelope.getMaxY());
	  		int numTiles=0;//contador de tiles dibujados (cada cierto numero
	  		//se refrescara el canvas -observer.actualizate()-)
	
	 	    while(hasNext()){
				//Comprobamos si se ha interrumpido el thread de repintado
				if(Thread.currentThread().isInterrupted()) return;
	
				//obtenemos el tile TODO Aquí podríamos usar una caché de tiles
				GeoReferencedBufferedImage image = nextTile();
				//coordenadas dispositivo del tile (usamos AffineTransform de Java2D??)
				int x0=transf_x(image.getMer().getMinX(),envelope.getMinX(),factor,ox);
				int y0=transf_y(image.getMer().getMaxY(),envelope.getMaxY(),factor,oy);
				
				int x1=transf_x(image.getMer().getMaxX(),envelope.getMinX(),factor,ox);
				int y1=transf_y(image.getMer().getMinY(),envelope.getMaxY(),factor,oy);
				g.drawImage((BufferedImage)image.getImage(),x0,y0,(x1-x0),(y1-y0),(ImageObserver) observer);
				numTiles++;
				if((numTiles%10 ==0)&& (observer!=null))
		  			observer.actualizate();
//		  		System.out.println("Procesadas:"+numTiles);
	  		}//while
	  		System.out.println("Terminado el rendering de JAI");
		}//pinta
		
		public  int transf_x(double x,double xmin,double f,int ox) {
			   return (int)Math.round((((x-xmin)/f) + ox));
		}

		public  int transf_y(double y,double ymax,double f,int oy) {
			   return (int)Math.round((((ymax-y)/f) + oy));
		}
	}//PintaRasters
	
	public static void main(String[] args){
		
	}
	
	public interface PintaRasterObserver extends ImageObserver{
		public void actualizate();
	}

	public String getImagePath()
	{
		return imagePath;
	}
	public void setImagePath(String rutaImagen) throws TfwNoAvailableException
	{
		String rutaImagenWithoutExt = GUIUtil.nameWithoutExtension(rutaImagen);

		this.imagePath = rutaImagen;
		String ext=TFW_EXT;
		// obtiene la extensión
		String imgExt= rutaImagen.substring(rutaImagen.lastIndexOf("."));
		if ("jpg".equalsIgnoreCase(imgExt) || "jpeg".equalsIgnoreCase(imgExt))
			ext=".jgw";
		try{
			RenderedImage rendered = ImageLoader.load(rutaImagen);
			TfwFile tfwFile = new TfwFile(rutaImagenWithoutExt + ext);
			double xmin = tfwFile.getXReal(rendered.getMinX());
			double xmax = tfwFile.getXReal(rendered.getMinX() + rendered.getWidth());
			double ymin = tfwFile.getYReal(rendered.getMinY() + rendered.getHeight());
			double ymax = tfwFile.getYReal(rendered.getMinY());
			
			imageEnvelope = new Envelope(xmin, xmax, ymin, ymax);
			
			image = new GeoReferencedRenderedImage(rendered,imageEnvelope);
			
			leftIndex=image.getMinTileX();
			rightIndex=image.getMaxTileX();
			bottomIndex=image.getMinTileY();
			topIndex=image.getMaxTileY();

		} catch(FileNotFoundException e){
			e.printStackTrace();
			throw new TfwNoAvailableException(e.getMessage());
		}catch (IOException ex) {
			ex.printStackTrace();
				
		}
		
	}
}


