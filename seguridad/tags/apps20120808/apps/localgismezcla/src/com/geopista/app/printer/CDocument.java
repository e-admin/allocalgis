package com.geopista.app.printer;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.net.URL;
import java.net.MalformedURLException;

/**
 *  This class is the painter for the document content.
 */
public class CDocument extends Component implements Printable {
	private final static int POINTS_PER_INCH = 72;
	private static String _nombreFichero = "";
    private static java.awt.Image image;

	public CDocument(java.awt.Image image){
		super();
		this.image=image;
		//_nombreFichero=nombreFichero;

	}
	/**
	 * Method: print <p>
	 *
	 * @param g a value of type Graphics
	 * @param pageFormat a value of type PageFormat
	 * @param page a value of type int
	 * @return a value of type int
	 */
	public int print(Graphics g, PageFormat pageFormat, int page) {

        System.out.println("page: "+page);
		try {

			Graphics2D g2d = (Graphics2D) g;
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
			g2d.setPaint(Color.black);

			/*
			MediaTracker mediaTracker = new MediaTracker(this);
			URL imageURL1 = new URL(_nombreFichero);
			Image image = Toolkit.getDefaultToolkit().getImage(imageURL1);
			mediaTracker.addImage(image, 0);
			mediaTracker.waitForID(0);
            */
			System.out.println("[Document.print()] pageFormat.getWidth(): "+pageFormat.getWidth());
			System.out.println("[Document.print()] pageFormat.getHeight(): "+pageFormat.getHeight());

			CImagen imagen=new CImagen(image.getWidth(this),image.getHeight(this));

			imagen.escalarImagen((float)pageFormat.getWidth(),(float)pageFormat.getHeight());
			imagen.centrarImagen((float)pageFormat.getWidth(),(float)pageFormat.getHeight());

			g2d.drawImage(image, Math.round(imagen.getCoordenadaOrigenX()), Math.round(imagen.getCoordenadaOrigenY()), Math.round(imagen.getImageWidth()) ,Math.round(imagen.getImageHeight())  , this);


		} catch (Exception ex) {
			ex.printStackTrace();
		}

		//--- Validate the page
		return (PAGE_EXISTS);

	}
}
