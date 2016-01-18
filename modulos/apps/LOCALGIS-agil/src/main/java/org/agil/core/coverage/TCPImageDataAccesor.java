/**
 * TCPImageDataAccesor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.agil.core.coverage;

import java.awt.image.BufferedImage;

import org.agil.vectorialserver.SpatialEngineConnection;

import com.vividsolutions.jts.geom.Envelope;

/**
 *  <p>
 *
 *  Permite recuperar imagenes de un servidor de imagenes TCP/IP dedicado </p>
 *
 *@author     alvaro zabala
 *@todo       QUITAR LA LOGICA DE CONEXION DE ESTA CLASE Y LLEVARLA A
 *      SpatialEngineConnection
 *@version    1.1
 */
public class TCPImageDataAccesor
		 implements ImageDataAccesor {
	/**
	 *  Identificador de la cobertura cuyos datos se estan accediendo. Un
	 *  servidor de imagenes puede barajar multiples rasters, y de esta forma se
	 *  identifica la que se está solicitando
	 */
	protected long idCoverage;

	/**
	 *  Envelope de los datos raster accedidos. Solo se solicitan una vez al
	 *  servidor, el resto de veces se mantienen en local.
	 */
	protected Envelope envelope;

	/**
	 *  Conexion logica al servidor de datos espaciales encargado de
	 *  suministrarnos las imagenes
	 */
	protected SpatialEngineConnection conexionServidorEspacial;


	/**
	 *  Constructor. Recibe un idenficador y la conexion al servidor espacial
	 *
	 *@param  idCoverage                Description of Parameter
	 *@param  conexionServidorEspacial  Description of Parameter
	 *@roseuid                          3F703C5501A5
	 */
	public TCPImageDataAccesor(long idCoverage, SpatialEngineConnection conexionServidorEspacial) {
		this.conexionServidorEspacial = conexionServidorEspacial;
		this.idCoverage = idCoverage;
	}


	/**
	 *  Constructor por defecto
	 */
	public TCPImageDataAccesor() {
	}


	/**
	 *  Setea la conexion espacial
	 *
	 *@param  conexion  conexion con un servidor espacial
	 */
	public void setSpatialConnection(SpatialEngineConnection conexion) {
		this.conexionServidorEspacial = conexion;
	}


	/**
	 *@param  envelope
	 *@param  width
	 *@param  height
	 *@return           java.awt.image.BufferedImage
	 *@roseuid          3F703C5501C5
	 */
	public BufferedImage getImagen(Envelope envelope, int width, int height) {
		BufferedImage solucion = null;
		solucion = conexionServidorEspacial.getCoverageImage(this.idCoverage, envelope, width, height);
		return solucion;
	}

	//getImage


	/**
	 *  Lee del canal de entrada una imagen serializada segun un formato propio.
	 *
	 *@return                       imagen leida del canal de datos.
	 *@throws  java.io.IOException  excepcion causada por problemas en la
	 *      conexion
	 *@roseuid                      3F687B3C0213
	 */
	/*
	 *  protected BufferedImage readNormalImage() throws java.io.IOException {
	 *  BufferedImage solucion = null;
	 *  //En primer lugar el DataBuffer de la imagen remota
	 *  DataBuffer dataBuffer = null;
	 *  //Informacion sobre el tipo de dato con el que estan almacenados los pixels
	 *  int dataTypeDB = in.readInt();
	 *  //Creamos el DATABUFFER apropiado a este tipo de datos
	 *  switch (dataTypeDB) {
	 *  case DataBuffer.TYPE_BYTE: {
	 *  int length = in.readInt();
	 *  byte[] pixels = new byte[length];
	 *  in.read(pixels);
	 *  dataBuffer = new DataBufferByte(pixels, pixels.length);
	 *  }
	 *  break;
	 *  case DataBuffer.TYPE_INT:
	 *  case DataBuffer.TYPE_SHORT: {
	 *  int pixelsL = in.readInt();
	 *  System.out.println("pixelsL:" + pixelsL);
	 *  int[] pixels = new int[pixelsL];
	 *  for (int i = 0; i < pixelsL; i++) {
	 *  pixels[i] = in.readInt();
	 *  } //for
	 *  dataBuffer = new DataBufferInt(pixels, pixelsL);
	 *  } //case
	 *  break;
	 *  } //switch
	 *  //AHORA LEEMOS LOS DATOS DE LA IMAGEN: w, h, numBandas, pixelStride, scanLineStride
	 *  int width = in.readInt();
	 *  int height = in.readInt();
	 *  int numBands = in.readInt();
	 *  //ahora leemos el tipo de SampleModel
	 *  int sampleModelType = in.readInt();
	 *  switch (sampleModelType) {
	 *  case org.agil.kernel.jump.coverage.TCPImageServer.COMPONENT_SAMPLE_MODEL: {
	 *  int pixelStride = in.readInt();
	 *  int scanlineStride = in.readInt();
	 *  int lbO = in.readInt();
	 *  int[] bandsOffset = new int[lbO];
	 *  for (int i = 0; i < bandsOffset.length; i++) {
	 *  bandsOffset[i] = in.readInt();
	 *  }
	 *  int existen = in.readInt();
	 *  if (existen == 0) {
	 *  System.out.println("la imagen no tiene banco de indices");
	 *  }
	 *  else if (existen == 1) {
	 *  int lbI = in.readInt();
	 *  int[] bankIndices = new int[lbI];
	 *  for (int i = 0; i < bankIndices.length; i++) {
	 *  bankIndices[i] = in.readInt();
	 *  } //for
	 *  }
	 *  //Esto, en vez de generarse así, se leera del Servidor Remoto
	 *  //(podemos encontrarnos con imagenes de MAS o MENOS de 8 bits por banda
	 *  int[] bits = new int[numBands];
	 *  for (int i = 0; i < numBands; i++) {
	 *  bits[i] = 8;
	 *  }
	 *  //Esto se leera tambien
	 *  int transparency;
	 *  boolean hasAlpha;
	 *  if (numBands <= 3) {
	 *  transparency = Transparency.OPAQUE;
	 *  hasAlpha = false;
	 *  }
	 *  else {
	 *  transparency = Transparency.TRANSLUCENT;
	 *  hasAlpha = false;
	 *  }
	 *  //El tipo de ColorSpace se leera tb, por si aparecen otros distintos
	 *  //de momento se considera solo RGB
	 *  ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	 *  ColorModel cm = new ComponentColorModel(cs,
	 *  bits,
	 *  hasAlpha,
	 *  hasAlpha,
	 *  transparency,
	 *  DataBuffer.TYPE_BYTE);
	 *  WritableRaster wr = WritableRaster.createInterleavedRaster(dataBuffer,
	 *  width, height,
	 *  scanlineStride, pixelStride,
	 *  bandsOffset, new Point(0, 0));
	 *  solucion = new BufferedImage(cm, wr, cm.isAlphaPremultiplied(), null);
	 *  }
	 *  break;
	 *  case org.agil.kernel.jump.coverage.TCPImageServer.
	 *  SINGLE_PIXEL_SAMPLE_MODEL: {
	 *  int dataType = in.readInt();
	 *  int scanLineStride = in.readInt();
	 *  int lengthBitMask = in.readInt();
	 *  int[] bitMasks = new int[lengthBitMask];
	 *  for (int i = 0; i <= lengthBitMask; i++) {
	 *  bitMasks[i] = in.readInt();
	 *  }
	 *  SinglePixelPackedSampleModel sampleModel = new
	 *  SinglePixelPackedSampleModel(dataType,
	 *  width,
	 *  height,
	 *  scanLineStride,
	 *  bitMasks);
	 *  ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	 *  ColorModel cm = new ComponentColorModel(cs,
	 *  bitMasks,
	 *  false,
	 *  false,
	 *  Transparency.OPAQUE,
	 *  DataBuffer.TYPE_BYTE);
	 *  WritableRaster wRaster = Raster.createWritableRaster(sampleModel,
	 *  dataBuffer,
	 *  new Point(0, 0));
	 *  solucion = new BufferedImage(cm, wRaster, false, null);
	 *  }
	 *  break;
	 *  default: {
	 *  System.out.println("Error:!!SampleModel no contemplado.");
	 *  }
	 *  break;
	 *  } //switch
	 *  return solucion;
	 *  }
	 */
	/*
	 *  Devuelve el envelope de los datos accedidos.
	 *  @see org.agil.kernel.jump.coverage.ImageDataAccesor#getEnvelope()
	 */
	public Envelope getEnvelope() {
		if (envelope == null) {
			envelope = this.conexionServidorEspacial.getCoverageEnvelope(this.idCoverage);
		}
		return envelope;
	}
	//getEnvelope
}
