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

import java.io.File;
/**
   Clase abstracta que proporciona propiedades y metodos
   basicos tanto para cargar como guardar imagenes en diferentes
   formatos graficos.
 */

public abstract class ImageFormats {

	/**
	 * Conjunto de extensiones
	 * reconocidas como formatos de imágenes.
	 */
    public final static String  GIF = "gif";
	public final static String  TIF = "tif";
	public final static String TIFF = "tiff";
	public final static String  PNG = "png";
	public final static String  BMP = "bmp";
	public final static String  JPG = "jpg";
	public final static String  FPX = "fpx";
	/**
	 * Devuelve la extensión del fichero proporcionado
	 * @param file
	 * @return
	 */
    public static String getExtension(File file){
         String s = file.getName();
         int i = s.lastIndexOf(".");

         String ext = null;
         if (i > 0 && i < s.length() - 1) {
            //El archivo tiene extension
             ext = s.substring(i+1).toLowerCase();
         }
         return ext;
	}//getExtension
}