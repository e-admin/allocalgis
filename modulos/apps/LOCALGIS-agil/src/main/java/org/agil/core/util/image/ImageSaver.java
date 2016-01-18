/**
 * ImageSaver.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */


package org.agil.core.util.image;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.agil.core.util.image.encoders.GifEncoder;
import org.agil.core.util.image.jaicodecs.imageformats.bmp.BMPEncodeParam;
import org.agil.core.util.image.jaicodecs.imageformats.bmp.BMPImageEncoder;
import org.agil.core.util.image.jaicodecs.imageformats.jpg.JPEGEncodeParam;
import org.agil.core.util.image.jaicodecs.imageformats.jpg.JPEGImageEncoder;
import org.agil.core.util.image.jaicodecs.imageformats.png.PNGEncodeParam;
import org.agil.core.util.image.jaicodecs.imageformats.png.PNGImageEncoder;
import org.agil.core.util.image.jaicodecs.imageformats.tif.TIFFEncodeParam;
import org.agil.core.util.image.jaicodecs.imageformats.tif.TIFFImageEncoder;


/**Clase que permite
 * guardar imagenes segun diferentes formatos:
 * BMP, TIF, GIF, PNG, JPG,
 *
 * El formato GIF no es de dominio publico, y tiene
 * licencia.
 */

public class ImageSaver extends ImageFormats {

  private ImageSaver() {
  }

  public static void saveImage(BufferedImage image,String path)throws IOException{

    File file = new File(path);
    String ext = getExtension(file);

    if(ext==null)
            throw new IOException(path+" no es una imagen valida, carece de extension");


    if(ext.equalsIgnoreCase(TIF) || ext.equalsIgnoreCase(TIFF)){
      guardarTIFF(image,path);
    }else if(ext.equalsIgnoreCase(PNG)){
      guardarPNG(image,path);
    }else if(ext.equalsIgnoreCase(GIF)){
      guardarGIF(image,path);
    }else if(ext.equalsIgnoreCase(JPG)){
      guardarJPG(image,path);
    }else if(ext.equalsIgnoreCase(BMP)){
      guardarBMP(image,path);
    }else{
      throw new IOException("\""+ext+"\""+"es una extension no contemplada");
    }

  }

  public static void guardarTIFF(BufferedImage image,String path){
      try{
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
        TIFFImageEncoder encoder = new TIFFImageEncoder(out,new TIFFEncodeParam());
        encoder.encode(image);
        out.close();
      }catch(Exception e){
        e.printStackTrace();
      }
  }

  public static void guardarGIF(BufferedImage image,String path){
      try{
       BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
       GifEncoder encoder = new GifEncoder(image,out);
       encoder.encode();
       out.close();
      }catch(Exception e){
        e.printStackTrace();
      }
  }

  public static void guardarJPG(BufferedImage image,String path){
    try{
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
        JPEGImageEncoder encoder = new JPEGImageEncoder(out,new JPEGEncodeParam());
        encoder.encode(image);
        out.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public static void guardarBMP(BufferedImage image,String path){
    try{
         BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
         BMPImageEncoder encoder = new BMPImageEncoder(out,new BMPEncodeParam());
         encoder.encode(image);
         out.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public static void guardarPNG(BufferedImage image,String path){
    try{
          BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
          PNGImageEncoder encoder = new PNGImageEncoder(out,PNGEncodeParam.getDefaultEncodeParam(image));
          encoder.encode(image);
          out.close();
    }catch(Exception e){
        e.printStackTrace();
    }

  }
//  public static void main(String[]args){
//
//        try{
//            RenderedImage rend = ImageLoader.cargarGIF(new File("C:/Dibujo5.gif"));
//
//            Raster raster = rend.getTile(0,0);
//            BufferedImage buff = ImageLoader.getAsBufferedImage(raster,rend.getColorModel());
//
//           guardarBMP(buff,"c:/Dibujo5.bmp");
//        }catch(Exception e){
//        e.printStackTrace();
//       }
//
//  }


}