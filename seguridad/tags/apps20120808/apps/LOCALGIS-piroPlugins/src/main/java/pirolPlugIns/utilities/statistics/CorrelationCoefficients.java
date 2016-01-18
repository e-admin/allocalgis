/*
 * Created on 18.05.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: CorrelationCoefficients.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:57 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/statistics/CorrelationCoefficients.java,v $
 */
package pirolPlugIns.utilities.statistics;

import java.util.ArrayList;
import java.util.List;

import pirolPlugIns.utilities.FeatureCollectionTools;
import pirolPlugIns.utilities.ObjectComparator;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;


/**
 * Class that calculates various correlation coefficients.
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * 
 */
public class CorrelationCoefficients {
    protected Object[] dataArray = null;
    
    // TODO: use array or list instead of two, three, etc. variables
    protected String attrName1 = null, attrName2 = null;
    
    protected double[] means = null;
    
    protected PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);

    public CorrelationCoefficients(Feature[] features, String attr1, String attr2) {
        super();
        
        this.means = new double[2];
        this.attrName1 = attr1;
        this.attrName2 = attr2;
        
        this.dataArray = this.initializeDataStorage(features);
    }
    
    protected CorrelationDataPair[] initializeDataStorage(Feature[] features){
        List points = new ArrayList();
        
        Feature feature = features[0];
        
        FeatureSchema fs = feature.getSchema();
        double x, y;
        
        int attrIndX = fs.getAttributeIndex(this.attrName1);
        int attrIndY = fs.getAttributeIndex(this.attrName2);
        
        this.means[0] = this.aritmeticMiddle(features, attrIndX);
        this.means[1] = this.aritmeticMiddle(features, attrIndY);
        
        for (int i=0; i<features.length; i++){
            feature = features[i];
            
            x = ObjectComparator.getDoubleValue(feature.getAttribute(attrIndX));
            y = ObjectComparator.getDoubleValue(feature.getAttribute(attrIndY));
            
            points.add(new CorrelationDataPair(new double[]{x,y}, i));
        }
        
        return (CorrelationDataPair[])points.toArray(new CorrelationDataPair[0]);
    }
    
    /**
     * Returns the deviation of the values of the given attribute. Uses a given
     * mean to avoid multiple calculation of the mean. To get the mean take a look 
     * at the FeatureCollectionTools class. This class is also used by aritmeticMiddle().
     *@param features array containing the features we want the deviation for
     *@param attr name of the attribute to calculate the deviation for
     *@param mean the mean for the given features
     *@return the deviation
     *@throws IllegalArgumentException if the attribute is not of a numerical type
     *@see FeatureCollectionTools
     */
    public static double getDeviation(Feature[] features, String attr, double mean){
        PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);
        
        Feature feat = features[0];
        
        int attrIndex = feat.getSchema().getAttributeIndex(attr);
        
        if (!FeatureCollectionTools.isAttributeTypeNumeric(feat.getSchema().getAttributeType(attrIndex)))
            throw new IllegalArgumentException("attribute is not numeric!");
        
        double squareSum = 0;
        
        
        for (int i=0; i<features.length; i++){
            feat = features[i];
            if (feat.getAttribute(attrIndex) != null)
                squareSum += Math.pow( ( ObjectComparator.getDoubleValue(feat.getAttribute(attrIndex)) - mean) ,2);
            else 
                logger.printWarning("skipping value (NULL), when calculating deviation");
                
        }
        
        return Math.sqrt( 1d/(features.length-1)*squareSum );
    }
    
    protected double getVariance(String attr){
        // not needed, yet
        // TODO: implement variance, use deviation (or the other way round)
        return 0.0;
    }
    
    protected double aritmeticMiddle(Feature[] features, int attr){
        return FeatureCollectionTools.getAritmeticMiddleForAttribute(features, attr);
    }
    
    /**
     * Get the aritmetic middle for the nr-th attribut given
     *@param nr index number of attribut to calculate the mean for
     *@return the mean for the attribute or Double.NaN, if errors occured
     */
    public double getMean(int nr){
        return this.means[nr];
    }
    
    /**
     * get Pearson's correlation coefficient (good, dimension-less measure, if there is a linear relation between the attributes)
     * <br>see: <a href="http://www.netzwelt.de/lexikon/Korrelationskoeffizient.html">http://www.netzwelt.de/lexikon/Korrelationskoeffizient.html</a> 
     *@return Pearson's correlation coefficient
     */
    public double getPearsonCoefficient(){
        double coefficient = 0;

        CorrelationDataPair pkt;
        
        double x, y, errorX, errorY;
        double sumErrorProducts = 0;
        double sumXErrorSquares = 0, sumYErrorSquares = 0;
        
        for (int i=0; i<this.dataArray.length; i++){
            pkt = (CorrelationDataPair)this.dataArray[i];
            
            try {
                x = pkt.getX();
                y = pkt.getY();
            } catch (Exception e) {
                logger.printWarning(e.getMessage());
                continue;
            }
            
            errorX = x - this.means[0];
            errorY = y - this.means[1];
            
            // zähler
            sumErrorProducts += (errorX*errorY);
            
            // nenner
            sumXErrorSquares += (errorX*errorX);
            sumYErrorSquares += (errorY*errorY);
        }
        
        coefficient = sumErrorProducts / ( Math.sqrt(sumXErrorSquares) * Math.sqrt(sumYErrorSquares));
        
        return coefficient;
    }
    
    /**
     * "Spearman Rank Order Correlations (or "rho")  and Kendall's Tau-b (or "tau") Correlations are used when the variables are measured as ranks (from highest-to-lowest or lowest-to-highest)"
     * <br><a href="http://www.themeasurementgroup.com/datamining/definitions/correlation.htm">http://www.themeasurementgroup.com/datamining/definitions/correlation.htm</a>
     *@return double array containing: coefficient (a), coefficient (b), number of concordant pairs, number of discordant pairs
     */
    public double[] getKendalsTauRankCoefficient(){
                
        CorrelationDataPair currentPair, referncePair;
        double currX, currY, refX, refY;
        
        int numConcordant = 0, 
        	numDiscordant = 0,
        	numTiesX = 0,
        	numTiesY = 0,
        	numVergleiche = 0;

        logger.printDebug("starting calculation");
        
        for (int i=0; i<this.dataArray.length; i++){
            currentPair = (CorrelationDataPair)this.dataArray[i];
            try {
                currX = currentPair.getCoordinate(0);
                currY = currentPair.getCoordinate(1);
            } catch (Exception e1) {
                logger.printMinorError(e1.getMessage());
                continue;
            }
            
            for (int j=i+1; j<this.dataArray.length; j++, numVergleiche++){
                referncePair = (CorrelationDataPair)this.dataArray[j];
                try {
                    refX = referncePair.getCoordinate(0);
                    refY = referncePair.getCoordinate(1);
                    
                } catch (Exception e2) {
                    logger.printMinorError(e2.getMessage());
                    continue;
                }
                
                if ( (currX < refX && currY < refY) || (currX > refX && currY > refY) ) {
                    numConcordant ++;
                } else if ( (currX < refX && currY > refY) || (currX > refX && currY < refY) ) {
                    numDiscordant ++;
                }
                
                try {
	                if (currX == refX){
	                    numTiesX ++;
	                }
	                if (currY == refY){
	                    numTiesY ++;
	                }
                } catch (Exception e) {
                    logger.printWarning(e.getMessage());
                }
            }
        }
        logger.printDebug("finishing calculation");
        logger.printDebug("Vergleiche: " + numVergleiche);
        logger.printDebug("numConcordant: " + numConcordant);
        logger.printDebug("numDiscordant: " + numDiscordant);
        
        if (numTiesX > 0)
            logger.printWarning("numTiesX: " + numTiesX);
        
        if (numTiesY > 0)
            logger.printWarning("numTiesY: " + numTiesY);
        
        // according to the formula:
        //double kendallsTau_alpha = (double)(numConcordant-numDiscordant) / (((double)this.dataArray.length*(double)(this.dataArray.length-1.0))/2.0);
        
        // less calculation, but equivalent (?) 
        double kendallsTau_alpha = (double)(numConcordant-numDiscordant) / (double)(numVergleiche);
        double kendallsTau_beta = (double)(numConcordant-numDiscordant) / Math.sqrt( (double)(numConcordant+numDiscordant+numTiesX) * (double)(numConcordant+numDiscordant+numTiesY) );
        // third kendalls tau coefficient should be equal to the first one, since we allways have the same number of x and y values
        //double kendallsTau_gamma = 2.0*this.dataArray.length * (numConcordant-numDiscordant) / ((this.dataArray.length-1)*(this.dataArray.length*this.dataArray.length));

        return new double[]{kendallsTau_alpha, kendallsTau_beta, numConcordant, numDiscordant};
    }
    
    private void scaleAllPoints() throws Exception{
	    for (int i=0; i<this.dataArray.length; i++ ){
			((CorrelationDataPair)this.dataArray[i]).scale();
		}
	}
	
	private void unScaleAllPoints() throws Exception{
	    for (int i=0; i<this.dataArray.length; i++ ){
			((CorrelationDataPair)this.dataArray[i]).unScale();
		}
	}
    
 
}
