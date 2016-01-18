package es.satec.svgviewer.localgis.sld;

/**
 * This is the basis of all symbolizers. It defines the method <tt>getGeometry</tt> that's common
 * to all symbolizers.
 * <p>
 * ----------------------------------------------------------------------
 * </p>
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */

public interface Symbolizer {

    /**
     * The ScaleDenominator-information is optional and determines whether a rule (and thus a
     * Symbolizer) is a to be applied at a certain scale.
     * 
     * @return the MinScaleDenominator
     */
    double getMinScaleDenominator();

    /**
     * Sets the MinScaleDenominator
     * 
     * @param minScaleDenominator
     *            the MinScaleDenominator
     */
    void setMinScaleDenominator( double minScaleDenominator );

    /**
     * The ScaleDenominator-information is optional and determines whether a rule (and thus a
     * Symbolizer) is a to be applied at a certain scale.
     * 
     * @return the MaxScaleDenominator
     */
    double getMaxScaleDenominator();

    /**
     * Sets the MaxScaleDenominator
     * 
     * @param maxScaleDenominator
     *            the MaxScaleDenominator
     */
    void setMaxScaleDenominator( double maxScaleDenominator );

	void dispose();

}
