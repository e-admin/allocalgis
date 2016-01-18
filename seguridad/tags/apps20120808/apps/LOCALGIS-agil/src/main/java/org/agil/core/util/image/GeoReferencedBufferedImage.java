

package org.agil.core.util.image;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Clase que representa a una BufferedImage GeoReferenciada.
 * Hereda de la clase GeoReferencedRenderedImage, siendo a su
 * vez más concreta que esta.
 */

public class GeoReferencedBufferedImage extends GeoReferencedRenderedImage {

    private BufferedImage imagen;


    public GeoReferencedBufferedImage(BufferedImage imagen,Envelope rect) {
		this.imagen=imagen;
        mer=rect;
    }

    public RenderedImage getImage(){
        return imagen;
    }
}