
package org.agil.core.util.image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import org.agil.core.util.image.jaicodecs.image.SimpleRenderedImage;

import com.vividsolutions.jts.geom.Envelope;


/**
 *  Clase que representa una RenderedImage georeferenciada.
 *  Ademas de funciones propias de una RenderedImage (acceso al tamaño
 *  de la imagen, al numero de tiles que la forman, etc.) ofrece funciones
 *  de conversion de coordenadas (de pixel a metros, de tile a metros, etc.)
 *
 *  Basicamente, esta formada por una imagen (RenderedImage, o alguna de sus clases,
 *  BufferedImage o SimpleRenderedImage) y por un mer (Rect)
 *
 */

public class GeoReferencedRenderedImage {
    /**
     * Imagen en si.
     */
    private RenderedImage image;
    /**Mer que georreferencia a la imagen*/
    protected Envelope mer;

    /**
     * Constructor. Se proporciona una imagen y su mer.
     * @param image
     * @param mer
     */
    public GeoReferencedRenderedImage(RenderedImage image, Envelope mer) {
        this.mer=mer;
        this.image=image;
    }

    public GeoReferencedRenderedImage(){
    }

    public void setMer(Envelope mer){
        this.mer=mer;
    }

    public void setRenderedImage(RenderedImage image){
        this.image=image;
    }

    public RenderedImage getImage(){
        return image;
    }


    public Envelope getMer(){
        return mer;
    }

/**Devuelve el numero de columnas de tile por que está
     * formada la imagen de la tesela
     */
    public int getNumXTiles(){
        return image.getNumXTiles();
    }

    /**Devuelve el numero de filas de imagen por que está formada
     * la imagen de la tesela.
     */
    public int getNumYTiles(){
        return image.getNumYTiles();
    }

/**
 * Devuelve la resolucion de la imagen (puesto que se puede hablar de resolucion
 * en filas y resolucion en columnas, devuelve la mayor de ambas)
 * @return resolucion expresada en metros/pixels.
 */
   public double getResolution(){
        double resX = (mer.getMaxX()-mer.getMinX())/(double)image.getWidth();
        double resY = (mer.getMaxX()-mer.getMinY())/(double)image.getHeight();
        return (resX>resY)?resX:resY;
    }

    /**Devuelve un tile de la imagen en forma de BufferedImage
     * @param xTile Coordenada x del tile
     * @param yTile Coordenada y del tile
     * @return Tile solicitado en forma de BufferedImage.
     */
    public BufferedImage getTile(int xTile, int yTile){

        java.awt.image.Raster tile = image.getTile(xTile,yTile);
        return ImageLoader.getAsBufferedImage(tile,image.getColorModel());
    }

    /**Devuelve un tile en forma de BufferedImage con el tamaño especificado
     @param xTile coordenada x del tile solicitado
     @param yTile coordenada y del tile solicitado
     @param width ancho que queremos que tenga la imagen devuelta
     @param height alto que queremos que tenga la imagen devuelta
     */
    public BufferedImage getTile(int xTile,int yTile,int width, int height){
        BufferedImage buff = getTile(xTile,yTile);
        BufferedImage buff2 = new BufferedImage(width,height,buff.getType());
        Graphics g=buff2.getGraphics();
        g.drawImage(buff,0,0,width,height,null);
        return buff2;
    }

    /**Utilidades para convertir coordenadas reales en pixeles de la imagen
     * -ojo! No confundir con las funciones de conversion de coordenadas reales
     * a coordenadas del dispositivo, presentes en OnisCanvasGraphics
     */
     public int getXPixel(double x){
        double fx = (double)image.getWidth()/(mer.getMaxX()-mer.getMinX());
        return((int)((x-mer.getMinX())*fx));
     }

     public int getYPixel(double y){
        double fy = (double)image.getHeight()/(mer.getMaxY()-mer.getMinY());
        int dy = (int)((y-mer.getMinY())*fy);
        return (image.getHeight()-dy);
     }




    /**
     * Devuelve la mayor fila de la malla de tiles de la imagen de la Tesela.
     * Si la imagen no admite tiles, devuelve 0.
     * @return Fila maxima de la malla de tiles de la imagen
     */
    public int getMaxTileY(){
//            Puesto que trabajamos con RenderedImage (SimpleRenderedImage y BufferedImage)
//            estas no disponen del metodo getMaxTileY. Por esta razon hay que hacer un cast
//            en el caso de que la imagen tenga tiles (SimpleRenderedImage)
        if(image.getNumYTiles()==1)
            return 0;
        else
            return ((SimpleRenderedImage)image).getMaxTileY();
    }

    /**Devuelve la columna maxima de la malla de tiles de la imagen.
     * Si la imagen no admite tiles, devuelve 0
     * @return Columna maxima de la malla de tiles de la imagen
     */
    public int getMaxTileX(){
//            Puesto que trabajamos con RenderedImage (SimpleRenderedImage y BufferedImage)
//            estas no disponen del metodo getMaxTileX. Por esta razon hay que hacer un cast
//            en el caso de que la imagen tenga tiles (SimpleRenderedImage)
        if(image.getNumXTiles()==1)
            return 0;
        else
            return ((SimpleRenderedImage)image).getMaxTileX();
    }

     /**Devuelve la fila minima de la malla de tiles de la imagen.
     * @return Columna minima de la malla de tiles de la imagen
     */
    public int getMinTileX(){
        return image.getMinTileX();
    }
    /**Devuelve la fila minima de la malla de tiles de la imagen.
     * @return fila minima de la malla de tiles de la imagen
     */
    public int getMinTileY(){
        return image.getMinTileY();
    }


    /**Devuelve el ancho en metros de un tile de la imagen
     * @return ancho en metros de un tile
     * */
    public double getRealTileWidht(){
        double anchoTile = (mer.getMaxX()-mer.getMinX())/(double)getNumXTiles();
        return anchoTile;
    }

    /**Devuelve el alto en metros de un tile de la imagen
     * @return alto en metros de un tile
     * */
    public double getRealTileHeight(){
        double altoTile = (mer.getMaxY()-mer.getMinY())/(double)getNumYTiles();
        //cambiar getNumYTiles() por image.getNumYTiles()
        return altoTile;
    }


    /**Devuelve la columna de tiles a la q pertenece una coordenada x real
     * @param x coordenada x terreno de la que queremos obtener la coordenada tile
     * de la imagen
     * @return columna de la malla de tiles a la que pertenece la x terreno proporcionada.
     * */
    public int getTileX(double x){
        double fX = ((double)image.getNumXTiles())/(mer.getMaxX()-mer.getMinX());
        return (int)((x-mer.getMinX())*fX);
    }

    /**Devuelve la fila de tiles a la que pertenece una coordenada y terreno
     * @param y Coordenada y terreno
     * @return fila de la malla de tiles a la que pertenece la coordenada real proporcionada
     * */
    public int getTileY(double y){
        double fY = ((double)image.getNumYTiles())/(mer.getMaxY()-mer.getMinY());
        return (int)((mer.getMaxY()-y)*fY);
    }
    /**Recibe una columna del grid de tiles y devuelve la coordenada x
     * terreno menor de las comprendidas por el tile
     * @param x columna de la malla de tiles de la imagen
     * @return menor cooordenada x terreno de las abarcadas por el tile.
     */
    public double tileXtoRealX(int x){
    	double realWidth = mer.getWidth();
    	int numXtiles = image.getNumXTiles();
    	double fx = realWidth / (double) numXtiles;
    	
        //double fx = (mer.getMaxX()-mer.getMinX())/(double)image.getNumXTiles();
        return mer.getMinX() + ((double)x * fx);
    }

    /**Recibe una fila del grid de tiles y devuelve su coordenada Y real
     * mayor
     * @param y fila de la malla de tiles de la imagen
     * @return mayor cooordenada y terreno de las abarcadas por el tile.
     */
    public double tileYtoRealY(int y){
    	double realHeight = mer.getHeight();
    	int numYtiles = image.getNumYTiles();
        double fy = realHeight / (double) numYtiles;
        double solucion = mer.getMaxY() - (double) y * fy;
        return solucion;
    }

      /**
       * Devuelve el ancho en metros de la imagen
       * @return ancho en metros
       */
      public double getMetrosWidth(){
        return mer.getWidth();
     }
    /**
     * Devuelve el alto en metros de la imagen
     * @return alto en metros
     */
    public double getMetrosHeight(){
        return mer.getHeight();
    }
    /**
     * Devuelve el ancho en pixeles de la imagen
     * @return ancho en  pixeles
     */
    public int getPixelWidth(){
        return image.getWidth();
    }
    /**
     * Devuelve el alto en pixels de la imagen
     * @return alto en pixels
     */
    public int getPixelHeight(){
        return image.getHeight();
    }
}