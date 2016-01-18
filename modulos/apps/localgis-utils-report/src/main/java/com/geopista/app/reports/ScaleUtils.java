/**
 * ScaleUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports;

import java.awt.Toolkit;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.geom.EnvelopeUtil;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.IViewport;

public class ScaleUtils {
	 
	// Factores de conversion de unidades para informes
	private static final double PIXEL = 1.0;
	private static final double INCHES = 72.0;
    private static final double PAPER_PIXELS_PER_CENTIMETER = 28.3464;
    
    private static final double INCH_TO_CM = 2.54;
    private static double SCREEN_PIXELS_PER_INCH= 96.0;
    static{
    	try{
    		SCREEN_PIXELS_PER_INCH = Toolkit.getDefaultToolkit().getScreenResolution(); //72 dpi or 96 dp1i or ..
    	}
    	catch (Exception e){
    		
    	}
    }
    private static final double SERVER_IMAGE_PIXELS_PER_INCH = 96.0;
        
	public static int convertPaperPixelsToScreenPixels(int paperPixels){
		double paperInches = convertPaperPixelsToCentimeters(paperPixels) / INCH_TO_CM;
		double screenPixels = paperInches * SCREEN_PIXELS_PER_INCH;
		
		return new Double(screenPixels).intValue();
	}
	
	public static int convertScreenPixelsToPaperPixels(int screenPixels){
		double screenCentimeters = convertScreenPixelsToCentimeters(screenPixels);
		double paperPixels = screenCentimeters * PAPER_PIXELS_PER_CENTIMETER;
		
		return new Double(paperPixels).intValue();
	} 
	
	public static int convertPaperPixelsToServerImagePixels(int paperPixels){
		double paperInches = convertPaperPixelsToCentimeters(paperPixels) / INCH_TO_CM;
		double serverImagePixels = paperInches * SERVER_IMAGE_PIXELS_PER_INCH;
		
		return new Double(serverImagePixels).intValue();
	}

	public static double convertPaperPixelsToCentimeters(double pixels){
		double centimeters = pixels / PAPER_PIXELS_PER_CENTIMETER;
		
		return centimeters;
	}
	
	public static double convertScreenPixelsToCentimeters(double pixels){
		double centimeters = (pixels / SCREEN_PIXELS_PER_INCH) * INCH_TO_CM;
		
		return centimeters;
	}
	
	public static double convertServerImagePixelsToCentimeters(double pixels){
		double centimeters = (pixels / SERVER_IMAGE_PIXELS_PER_INCH) * INCH_TO_CM;
		
		return centimeters;
	}
	
	public static double convertPaperScaleToServerImageScale(double originalScale){
		double serverImagePixelsPerCentimeter = SERVER_IMAGE_PIXELS_PER_INCH * INCH_TO_CM ;
		double conversionRatio = serverImagePixelsPerCentimeter / PAPER_PIXELS_PER_CENTIMETER;
		
		return originalScale * conversionRatio;
	}
	
	public static int getScaleNumerator(String scale){
		return Integer.parseInt(scale.substring(0, scale.indexOf(":")));
	}
	
	public static int getScaleDenominator(String scale){
		return Integer.parseInt(scale.substring(scale.indexOf(":")+1));	
	}
	
	public static String convertFormatedPaperScaleToServerImageScale(String originalFormatedScale){
		int numerator = getScaleNumerator(originalFormatedScale);
		int denominator = getScaleDenominator(originalFormatedScale);
		double scale = numerator/((double) denominator);
		
		double newScale = convertPaperScaleToServerImageScale(scale);
		
		return "1:" + new Double(1/newScale).intValue();
	}

	public static Envelope getReportPrintEnvelope(ILayerViewPanel layerViewPanel, 
			int imageHeight, int imageWidth){
		IViewport viewPort = layerViewPanel.getViewport();
		
		double printAreaHeightInMeters = 
			convertScreenPixelsToCentimeters(imageHeight)/100;
		double printAreaWidthInMeters =
			convertScreenPixelsToCentimeters(imageWidth)/100;
				
		// Las coordenadas del envelope estan especificadas en metros
		Envelope viewPortEnvelope = viewPort.getEnvelopeInModelCoordinates();
		double modelViewWidthInMeters = viewPortEnvelope.getWidth();
		double modelViewHeightInMeters = viewPortEnvelope.getHeight();
		
		double horizontalScale = modelViewWidthInMeters / printAreaWidthInMeters;
		double verticalScale = modelViewHeightInMeters / printAreaHeightInMeters;
	
		double scaledPrintAreaHeightInMeters;
		double scaledPrintAreaWidthInMeters;		
		
		if (printAreaWidthInMeters * verticalScale < modelViewWidthInMeters ){					
			scaledPrintAreaHeightInMeters = printAreaHeightInMeters * verticalScale;
			scaledPrintAreaWidthInMeters = printAreaWidthInMeters * verticalScale;
		}
		else {
			scaledPrintAreaHeightInMeters = printAreaHeightInMeters * horizontalScale;
			scaledPrintAreaWidthInMeters = printAreaWidthInMeters * horizontalScale;
		}
	
		// Aplico una reduccion del 5% del area de impresion para una mejor
		// previsualizacion en pantalla
		scaledPrintAreaHeightInMeters = scaledPrintAreaHeightInMeters - scaledPrintAreaHeightInMeters * 0.05;
		scaledPrintAreaWidthInMeters = scaledPrintAreaWidthInMeters - scaledPrintAreaWidthInMeters * 0.05;
		
		Coordinate modelCentre = EnvelopeUtil.centre(viewPortEnvelope);				
		Envelope printEnvelopeInModelCoordinates = new Envelope(
				modelCentre.x - (scaledPrintAreaWidthInMeters / 2.0),
				modelCentre.x + (scaledPrintAreaWidthInMeters / 2.0),
				modelCentre.y - (scaledPrintAreaHeightInMeters / 2.0),
				modelCentre.y + (scaledPrintAreaHeightInMeters / 2.0));
		
		return printEnvelopeInModelCoordinates;
	}

	public static double getReportPrintEnvelopeScale(ILayerViewPanel layerViewPanel, 
			int imageHeight, int imageWidth) {
		Envelope printEnvelope = getReportPrintEnvelope(layerViewPanel, imageHeight, imageWidth);
		
		double printAreaWidthInMeters =
			convertPaperPixelsToCentimeters(imageWidth)/100;
		double realAreaWidthInMeters = printEnvelope.getWidth();
		
		double horizontalScale = realAreaWidthInMeters/printAreaWidthInMeters;
		
		return horizontalScale;
	}
}
