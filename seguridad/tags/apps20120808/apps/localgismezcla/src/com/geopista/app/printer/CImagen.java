package com.geopista.app.printer;



/**
 * @author  SATEC
 * @version $Revision: 1.1 $
 *
 * Autor:$Author: miriamperez $
 * Fecha Ultima Modificacion:$Date: 2009/07/03 12:31:42 $
 * $Name:  $
 * $RCSfile: CImagen.java,v $
 * $Revision: 1.1 $
 * $Locker:  $
 * $State: Exp $
 *
 * To change this template use Options | File Templates.
 */
public class CImagen {

	private float _coordenadaOrigenX = 0;
	private float _coordenadaOrigenY = 0;

	private float _imageWidth = 0;
	private float _imageHeight = 0;

	public CImagen(float imageWidth, float imageHeight) {
		_imageWidth = imageWidth;
		_imageHeight = imageHeight;

	}

	public float getCoordenadaOrigenX() {
		return _coordenadaOrigenX;
	}

	public float getCoordenadaOrigenY() {
		return _coordenadaOrigenY;
	}

	public float getImageWidth() {
		return _imageWidth;
	}

	public float getImageHeight() {
		return _imageHeight;
	}

	public float getAspectRatio() {
		return (_imageWidth / _imageHeight);
	}

	public boolean centrarImagen(float pageWidth, float pageHeight) {

		try {

			if ((_imageWidth == 0) || (_imageHeight == 0)) {
				System.out.println("[CImagen.centrarImagen()] ERROR. El ancho o largo de la imagen es cero.");
				return false;
			}


			//****************************************************
			//** Calculamos la _coordenadaOrigenX para una imagen centrada horizontalmente en la página
			//***************************************************
			_coordenadaOrigenX = (pageWidth / 2) - (_imageWidth / 2);

			//****************************************************
			//** Calculamos la _coordenadaOrigenY para una imagen centrada verticalmente en la página
			//***************************************************
			_coordenadaOrigenY = (pageHeight / 2) - (_imageHeight / 2);

			System.out.println("[CImagen.centrarImagen()] Imagen centrada. (_coordenadaOrigenX,_coordenadaOrigenY): (" + _coordenadaOrigenX+","+_coordenadaOrigenY+")");

			return true;

		} catch (Exception ex) {
			System.out.println("[CImagen.centrarImagen()] Exception: " + ex.toString());
			return false;
		}
	}

	public boolean escalarImagen(float pageWidth, float pageHeight) {


		try {

			if ((_imageWidth == 0) || (_imageHeight == 0)) {
				System.out.println("[CImagen.centrarImagen()] ERROR. El ancho o largo de la imagen es cero.");
				return false;
			}

			//****************************************************
			//** Averiguamos si el limite escalar es horizontal o vertical
			//***************************************************
			float pageAspectRatio = (pageWidth / pageHeight);
			float imageAspectRatio = getAspectRatio();

			boolean LIMITE_ESCALAR_VERTICAL = (imageAspectRatio < pageAspectRatio);

			if (LIMITE_ESCALAR_VERTICAL) {

				System.out.println("[CImagen.escalarImagen()] Limite escalar vertical. Altura asignada. Anchura recalculada.");
				_imageHeight = pageHeight;
				_imageWidth = _imageHeight * imageAspectRatio;


			} else {

				System.out.println("[CImagen.escalarImagen()] Limite escalar horizontal. Anchura asignada. Altura recalculada.");
				_imageWidth = pageWidth;
				_imageHeight = _imageWidth * imageAspectRatio;


			}

			System.out.println("[CImagen.centrarImagen()] Imagen escalada. (_imageWidth,_imageHeight): (" + _imageWidth+","+_imageHeight+")");

			return true;

		} catch (Exception ex) {
			System.out.println("[CImagen.escalarImagen()] Exception: " + ex.toString());
			return false;
		}


	}

}
