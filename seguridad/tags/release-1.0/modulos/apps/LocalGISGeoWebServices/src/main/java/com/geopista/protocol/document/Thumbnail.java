package com.geopista.protocol.document;
import com.sun.image.codec.jpeg.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 03-may-2006
 * Time: 14:26:35
 */


/**
 * Thumbnail.java (requires Java 1.2+)
 * Load an image, scale it down and save it as a JPEG file.
 * @author Marco Schmidt
 */
public class Thumbnail {
    public Thumbnail() {
    }
    /** 
     * Ejemplo de como hacer un thumbnail. Esta función debería devolver el
     * chorro con el thumbnail para que se crease en base de datos
     * @param ficheroOrigen
     * @param ficheroDestino
     * @throws Exception
     */

    public static void createThumbnail(String ficheroOrigen, String ficheroDestino) throws Exception
    {

    // load image from INFILE
    Image image = Toolkit.getDefaultToolkit().getImage(ficheroOrigen);
    MediaTracker mediaTracker = new MediaTracker(new Container());
    mediaTracker.addImage(image, 0);
    mediaTracker.waitForID(0);
    // determine thumbnail size from WIDTH and HEIGHT
    //ancho y largo esto se puede sacar de algun fichero de configuracion
    int thumbWidth =100;
    int thumbHeight = 100;
    double thumbRatio = (double)thumbWidth / (double)thumbHeight;
    int imageWidth = image.getWidth(null);
    int imageHeight = image.getHeight(null);
    double imageRatio = (double)imageWidth / (double)imageHeight;
    if (thumbRatio < imageRatio) {
      thumbHeight = (int)(thumbWidth / imageRatio);
    } else {
      thumbWidth = (int)(thumbHeight * imageRatio);
    }
    // draw original image to thumbnail image object and
    // scale it to the new size on-the-fly
    BufferedImage thumbImage = new BufferedImage(thumbWidth,
      thumbHeight, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics2D = thumbImage.createGraphics();
    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
      RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
    // save thumbnail image to OUTFILE
    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(ficheroDestino));
    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
    JPEGEncodeParam param = encoder.
      getDefaultJPEGEncodeParam(thumbImage);
    //LA cualidad puede ir de 0 a 100 tal vez esto tambien debe ir en el parámetro de configuración
    int quality = 50;
    quality = Math.max(0, Math.min(quality, 100));
    param.setQuality((float)quality / 100.0f, false);
    encoder.setJPEGEncodeParam(param);
    encoder.encode(thumbImage);
    out.close();
  }

public static byte[] createThumbnail(byte[] originalImage, int width, int height) throws Exception{
    // load image from INFILE
    Image image = Toolkit.getDefaultToolkit().createImage(originalImage);
    MediaTracker mediaTracker = new MediaTracker(new Container());
    mediaTracker.addImage(image, 0);
    mediaTracker.waitForID(0);
    // determine thumbnail size from WIDTH and HEIGHT
    //ancho y largo esto se puede sacar de algun fichero de configuracion
    int thumbWidth =width;
    int thumbHeight = height;
    double thumbRatio = (double)thumbWidth / (double)thumbHeight;
    int imageWidth = image.getWidth(null);
    int imageHeight = image.getHeight(null);
    double imageRatio = (double)imageWidth / (double)imageHeight;
    if (thumbRatio < imageRatio) {
      thumbHeight = (int)(thumbWidth / imageRatio);
    } else {
      thumbWidth = (int)(thumbHeight * imageRatio);
    }
    // draw original image to thumbnail image object and
    // scale it to the new size on-the-fly
    BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics2D = thumbImage.createGraphics();
    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);

    ByteArrayOutputStream baos= new ByteArrayOutputStream();
    ImageIO.write(thumbImage, "jpeg" /* "png" "jpeg" format desired, no "gif" yet. */, baos );
    baos.flush();
    byte[] thumbImageAsRawBytes= baos.toByteArray();
    baos.close();
    return thumbImageAsRawBytes;

}

    public static byte[] createThumbnail(byte[] originalImage) throws Exception{
        return createThumbnail(originalImage, 100, 100);
  }


    public static byte[] createThumbnail(String ficheroOrigen, int thumbW, int thumbH){
        try{
            Image img= Toolkit.getDefaultToolkit().getImage(ficheroOrigen);
            MediaTracker mediaTracker = new MediaTracker(new Container());
            mediaTracker.addImage(img, 0);
            mediaTracker.waitForID(0);
            double thumbRatio = (double)thumbW / (double)thumbH;
            int imageWidth = img.getWidth(null);
            int imageHeight = img.getHeight(null);
            double imageRatio = (double)imageWidth / (double)imageHeight;
            if (thumbRatio < imageRatio) {
              thumbH = (int)(thumbW / imageRatio);
            } else {
              thumbW = (int)(thumbH * imageRatio);
            }
            BufferedImage thumbImage = new BufferedImage(thumbW, thumbH, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = thumbImage.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.drawImage(img, 0, 0, thumbW, thumbH, null);

            ByteArrayOutputStream baos= new ByteArrayOutputStream();
            ImageIO.write(thumbImage, "jpeg" /* "png" "jpeg" format desired, no "gif" yet. */, baos );
            baos.flush();
            byte[] thumbImageAsRawBytes= baos.toByteArray();
            baos.close();

            return thumbImageAsRawBytes;
        }catch(Exception e){}

        return null;
    }

    public static BufferedImage escalarImagen(byte[] thumbnail, int thumbW, int thumbH){
        try{
            if (thumbnail != null){
                // escalamos la imagen
                Image img= Toolkit.getDefaultToolkit().createImage(thumbnail);
                MediaTracker mediaTracker = new MediaTracker(new Container());
                mediaTracker.addImage(img, 0);
                mediaTracker.waitForID(0);
                double thumbRatio = (double)thumbW / (double)thumbH;
                int imageWidth = img.getWidth(null);
                int imageHeight = img.getHeight(null);
                double imageRatio = (double)imageWidth / (double)imageHeight;
                if (thumbRatio < imageRatio) {
                  thumbH = (int)(thumbW / imageRatio);
                } else {
                  thumbW = (int)(thumbH * imageRatio);
                }
                BufferedImage thumbImage = new BufferedImage(thumbW, thumbH, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics2D = thumbImage.createGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                graphics2D.drawImage(img, 0, 0, thumbW, thumbH, null);

                return thumbImage;
            }
        }catch(Exception e){}

        return null;
    }




}
