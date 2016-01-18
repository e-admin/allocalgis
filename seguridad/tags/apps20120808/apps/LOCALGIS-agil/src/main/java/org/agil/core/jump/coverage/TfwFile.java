/*
 * Creado el 21-abr-2004
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
package org.agil.core.jump.coverage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Alvaro Zabala (AGIL)
 *
 */
public class TfwFile {
	/**
			 * Tamaño del pixel en la direccion X
			 */
			public double xPixelSize;
			/**
			 * Tamaño del pixel en la direccion Y
			 */
			public double yPixelSize;
			/**
			 * Punto conocido de la imagen en coordenadas terreno
			 */
			public double xRealInsertPoint;
			public double yRealInsertPoint;
		
			/**
			 * Punto conocido de la imagen en coordenadas Pixel
			 */
			public double xPixelInsertPoint;
			public double yPixelInsertPoint;
			/**
			 * Ruta del fichero Tfw
			 */
			public String tfwFile;
		
			/**
			 * Constructor. Recibe la ruta de la imágen
			 * @param tfwFile
			 */
			public TfwFile(String tfwFile) throws FileNotFoundException {
			BufferedReader reader=null;
				try {
					reader = new BufferedReader(new FileReader(tfwFile));
				
						xPixelSize = Double.parseDouble(reader.readLine());
						xPixelInsertPoint = Double.parseDouble(reader.readLine());
						yPixelInsertPoint = Double.parseDouble(reader.readLine());
						yPixelSize = Double.parseDouble(reader.readLine());
						xRealInsertPoint = Double.parseDouble(reader.readLine());
						yRealInsertPoint = Double.parseDouble(reader.readLine());
				
				
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					if(e1 instanceof FileNotFoundException)
						throw (FileNotFoundException) e1;
					e1.printStackTrace();
				}
				finally
				{
				try
					{
				    	if(reader!=null) reader.close();
					}
				catch (IOException e)
					{
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
				}
			}
		
			/**
			 * Devuelve la coordenada x real de cualquier pixel de la imagen
			 * @param xPixel
			 * @return
			 */
			public double getXReal(int xPixel){
				return (xRealInsertPoint + ((xPixel - xPixelInsertPoint)*xPixelSize)); 
			}
		
			/**
			 * Devuelve la coordenada y real de cualquier pixel de la imagen a la
			 * que georreferencia
			 * @param yPixel
			 * @return
			 */
			public double getYReal(int yPixel){
				return (yRealInsertPoint + ((yPixel - yPixelInsertPoint)*yPixelSize)); 
			}
			/**
			 * Salva el estado actual del fichero ECW
			 *
			 */
			public void save(String aFile){
				BufferedWriter out = null;
				try {
					out = new BufferedWriter(new FileWriter(aFile));
					out.write(Double.toString(xPixelSize));
					out.newLine();
					out.write(Double.toString(xPixelInsertPoint));
					out.newLine();
					out.write(Double.toString(yPixelInsertPoint));
					out.newLine();
					out.write(Double.toString(yPixelSize));
					out.newLine();
					out.write(Double.toString(xRealInsertPoint));
					out.newLine();
					out.write(Double.toString(yRealInsertPoint));
					out.newLine();
					out.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				} 
			}
}
