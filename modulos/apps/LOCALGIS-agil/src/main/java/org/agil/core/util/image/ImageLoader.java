/**
 * ImageLoader.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Creado el 26-mar-2004
 *
 * Este codigo se distribuye bajo licencia GPL
 * de GNU. Para obtener una cópia integra de esta
 * licencia acude a www.gnu.org.
 * 
 * Este software se distribuye "como es". AGIL
 * solo  pretende desarrollar herramientas para
 * la promoción del GIS Libre.
 * AGIL no se responsabiliza de las perdidas derivadas
 * del uso de este software.
 */
package org.agil.core.util.image;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.agil.core.util.image.jaicodecs.imageformats.bmp.BMPEncodeParam;
import org.agil.core.util.image.jaicodecs.imageformats.bmp.BMPImageDecoder;
import org.agil.core.util.image.jaicodecs.imageformats.fpx.FPXDecodeParam;
import org.agil.core.util.image.jaicodecs.imageformats.fpx.FPXImageDecoder;
import org.agil.core.util.image.jaicodecs.imageformats.gif.GIFImageDecoder;
import org.agil.core.util.image.jaicodecs.imageformats.jpg.JPEGImageDecoder;
import org.agil.core.util.image.jaicodecs.imageformats.png.PNGDecodeParam;
import org.agil.core.util.image.jaicodecs.imageformats.png.PNGImageDecoder;
import org.agil.core.util.image.jaicodecs.imageformats.tif.TIFFDecodeParam;
import org.agil.core.util.image.jaicodecs.imageformats.tif.TIFFImageDecoder;
import org.agil.core.util.image.jaicodecs.stream.FileSeekableStream;


/**Clase encargada de cargar imagenes 
 * 
 * Actualmente soporta los siguientes formatos:
 * 
 * a) GIF   No soporta tiles (tile 0,0 )
 * 
 * b) TIFF  Soporta tiles
 * 
 * c) PNG   No soporta tiles
 * 
 * d) BMP   No soporta tiles
 * 
 * e) JPG   No soporta tiles
 * 
 * f) FPX   Soporta tiles
 * 
 *
 * Si la ruta especificada no se corresponde con alguno de los anteriores formatos,
 * lanza una excepcion del tipo IOException.
 *
 * El stream de lectura de cada imagen dependera del tipo de formato
 * (las imagenes Tiff requieren un stream del tipo SeekableStream, mientras que las
 * Gif, Jpg,etc admiten cualquier InputStream)
 *
 * Las imagenes que admiten Tiles son devueltas como SimpleRenderedImage, mientras
 * que las que no los admiten son devueltas como BufferedImage (ambas clases
 * son implementaciones de la interfaz RenderedImage)
 *
 */
public class ImageLoader extends ImageFormats{


    /**Constructor privado, de acuerdo al patron Singleton*/
	private ImageLoader() {
	}

    /**Permite crear una BufferedImage a partir de un Raster.
     * Metodo de utilidad para dibujar tiles aislados.
     *
     * @param raster Raster que queremos convertir.
     * @return BufferedImage construida a partir del raster proporcionado
     */
    public static BufferedImage getAsBufferedImage(java.awt.image.Raster raster,
    													ColorModel colorModel){

        BufferedImage buff=null;
        DataBuffer dataBuffer = raster.getDataBuffer();
        Point origin = new Point(0, 0);
        SampleModel sampleModel = raster.getSampleModel();
//        ColorModel colorModel = raster.getColorModel();
        WritableRaster wr =Raster.createWritableRaster(sampleModel,
                                                        dataBuffer,
                                                        origin);
        buff = new BufferedImage(colorModel,
                                        wr,
                                        colorModel.isAlphaPremultiplied(),
                                        null);

        return buff;

    }

    /**Devuelve una imagen como RenderedImage (BufferedImage, SimpleRenderedImage o
     * cualquier otra implementación de la interfaz) a partir de su ruta.
     * @param path Ruta de la imagen
     * @return representacion de la imagen como RenderedImage
     */
	public static RenderedImage load(String path) throws IOException{

		File file = new File(path);
		String ext = getExtension(file);

		if(ext==null)
			throw new IOException(path+" no es una imagen valida, carece de extension");

		RenderedImage solucion=null;

		if(ext.equals(TIFF)||(ext.equals(TIF))){
			solucion=cargarTIFF(file);
		}
		else if(ext.equals(PNG)){
			solucion=cargarPNG(file);
		}
		else if(ext.equals(BMP)){
			solucion=cargarBMP(file);
		}
		else if(ext.equals(JPG)){
			solucion=cargarJPG(file);
		}
		else if(ext.equals(FPX)){
			solucion=cargarFPX(file);
		}
		else if(ext.equals(GIF)){
			solucion=cargarGIF(file);
		}else{
			throw new IOException("\""+ext+"\""+"es una extension no contemplada");
		}

		return solucion;

	}

	/**Carga una imagen JPG
     *
     * @param file Objeto File que representa al archivo de imagen
     * @return representacion de la imagen como RenderedImage
     * */
	public static RenderedImage cargarJPG(File file)throws FileNotFoundException,IOException{

		//Podria ser seekable pero no es necesario un random access files
		//(que es lo que añade seekable a un input stream)
		FileInputStream stream = new FileInputStream(file);

		JPEGImageDecoder decoder = new JPEGImageDecoder(stream,null);
		RenderedImage rendered = decoder.decodeAsRenderedImage(0);
        java.awt.image.Raster raster = rendered.getTile(0,0);
        return getAsBufferedImage(raster,rendered.getColorModel());
	}


	/**Carga una imagen GIF
     *
     * @param file Objeto File que representa al archivo de imagen
     * @return representacion de la imagen como RenderedImage
     * */
	public static RenderedImage cargarGIF(File file)throws FileNotFoundException,IOException{
		 FileInputStream stream = new FileInputStream(file);
		 GIFImageDecoder decoder = new GIFImageDecoder(stream,null);
         RenderedImage rendered = decoder.decodeAsRenderedImage(0);
         java.awt.image.Raster raster = rendered.getTile(0,0);
		 return getAsBufferedImage(raster,rendered.getColorModel());
	}

     /**Carga una imagen TIF
     *
     * @param file Objeto File que representa al archivo de imagen
     * @return representacion de la imagen como RenderedImage
     * */
	public static RenderedImage cargarTIFF(File file)throws FileNotFoundException,IOException{

		//Formatos como el tiff, fpx, etc. que necesitan varias 'pasadas' del fichero,
		//requieron de SeekableStreams.

		//En este caso utilizamos FileSeekableStream, y no FileCacheSeekableStream,
        //ForwardedSeekableStream o MemorySeekableStream por ser el que
		//menos problemas ha dado.
		FileSeekableStream stream = new FileSeekableStream(file);
		TIFFImageDecoder decoder=new TIFFImageDecoder(stream,new TIFFDecodeParam());
		return decoder.decodeAsRenderedImage(0);
	}

	/**Carga una imagen FPX
     * Se trata de un formato especial que admite tiles.
     *
     * @param file Objeto File que representa al archivo de imagen
     * @return representacion de la imagen como RenderedImage
     * */
	public static RenderedImage cargarFPX(File file)throws FileNotFoundException,IOException{
		FileSeekableStream stream = new FileSeekableStream(file);
		FPXImageDecoder decoder = new FPXImageDecoder(stream,new FPXDecodeParam());
		return decoder.decodeAsRenderedImage(0);


	}

    /**Carga una imagen BMP
     *
     * @param file Objeto File que representa al archivo de imagen
     * @return representacion de la imagen como RenderedImage
     * */
	public static RenderedImage cargarBMP(File file)throws FileNotFoundException,IOException{
		FileInputStream stream = new FileInputStream(file);
		BMPImageDecoder decoder = new BMPImageDecoder(stream,new BMPEncodeParam());
        RenderedImage rendered = decoder.decodeAsRenderedImage(0);
        java.awt.image.Raster raster = rendered.getTile(0,0);
		return getAsBufferedImage(raster,rendered.getColorModel());
	}
	/**Carga una imagen PNG
     *
     * @param file Objeto File que representa al archivo de imagen
     * @return representacion de la imagen como RenderedImage
     * */
	public static RenderedImage cargarPNG(File file)throws FileNotFoundException,IOException{

		FileInputStream stream = new FileInputStream(file);
		PNGImageDecoder decoder = new PNGImageDecoder(stream,new PNGDecodeParam());
        RenderedImage rendered = decoder.decodeAsRenderedImage(0);
        java.awt.image.Raster raster = rendered.getTile(0,0);
		return getAsBufferedImage(raster,rendered.getColorModel());
	}
}