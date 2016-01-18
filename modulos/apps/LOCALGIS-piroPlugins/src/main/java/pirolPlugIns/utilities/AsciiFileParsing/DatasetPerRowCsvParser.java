/**
 * DatasetPerRowCsvParser.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 27.07.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: DatasetPerRowCsvParser.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:06 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/AsciiFileParsing/DatasetPerRowCsvParser.java,v $
 */
package pirolPlugIns.utilities.AsciiFileParsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import pirolPlugIns.utilities.AttributeInfo;
import pirolPlugIns.utilities.FeatureCollection.PirolFeatureCollection;
import pirolPlugIns.utilities.FeatureCollection.PirolFeatureCollectionRole;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;

/**
 * Generic class to parse an ASCII file with one dataset per row.
 * Use specialized parser for the file headers, but this one for the data part.<br><br>
 * Note: Does not open/close the given BufferedReader.
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
public class DatasetPerRowCsvParser {
    
    public static final String[] validSepatators = { "\t", " ", ";", "," };
    public static final String[] validSepatatorNames = { "tabulator", "space", "semicolon", "komma" };
    
    protected int xColumn, yColumn, zColumn;
    
    private BufferedReader buffReader = null;
    
    protected String separator = null;
    
    private boolean dataRead = false;
    
    protected String skipLineIfNotEqualTo = null;
    
    /**
     * toogle, if the dataset is skipped or a given null-value will be used, if an empty
     * field is found. <br>default: skip dataset
     */
    protected boolean useDefValIfNeccessary = false;
    
    protected TreeSet featureList = null;
    protected CsvRawPointDataset[] featureArray = null; 
    protected PirolFeatureCollection fc = null;
    
    protected PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);
    
    /**
     * Array that holds information about the columns - need to determine e.g. the
     * datatype to parse the ASCII stuff to...<br>
     * Note: the coordinates are also specified in this array (simply as double-type attributes), plus they have to be indicated by <code>xColumn, yColumn, zColumn</code>
     */
    protected AttributeInfo[] cols = null;
    protected boolean complainOnEmptyLine = true;
    
    /**
     * 
     *@param cols array with information about the columns to parse
     *@param isReader an open buffered input reader
     *@throws InvalidParameterException
     *@throws NoSuchElementException
     *@throws IOException
     */
    public DatasetPerRowCsvParser( AttributeInfo[] cols, BufferedReader isReader ) throws InvalidParameterException, NoSuchElementException, IOException{
        this.buffReader = isReader;
        this.buffReader.mark(2048);
        
        this.cols = cols;
        this.featureList = new TreeSet();
        
        xColumn = 1;
        yColumn = 0;
        zColumn = -1;
    }
    
    /**
     * 
     *@param cols array with information about the columns to parse
     *@param isReader an open buffered input reader
     *@param readAheadLimit Limit on the number of characters that may be read while still preserving the mark
     *@throws InvalidParameterException
     *@throws NoSuchElementException
     *@throws IOException
     *@see BufferedReader#mark(int)
     */
    public DatasetPerRowCsvParser( AttributeInfo[] cols, BufferedReader isReader, int readAheadLimit ) throws InvalidParameterException, NoSuchElementException, IOException{
        this.buffReader = isReader;
        this.buffReader.mark(readAheadLimit);
        
        this.cols = cols;
        this.featureList = new TreeSet();
        
        xColumn = 1;
        yColumn = 0;
        zColumn = -1;
    }
    

    public String getSeparator() {
        return separator;
    }
 
    public void setSeparator(String separator) {
        if ( separator != null)
            this.separator = separator;
    }

    public void setXColumn(int column) {
        this.xColumn = column;
    }

    public void setYColumn(int column) {
        this.yColumn = column;
    }
    
    public AttributeInfo getColumn( int nr ) throws InvalidParameterException, NoSuchElementException, IOException{
        if ( cols.length > nr )
            return cols[nr];
        
        return null;
    }
    
    /**
     *@return array of the parsed datasets
     *@throws InvalidParameterException
     *@throws NoSuchElementException
     *@throws IOException
     */
    public CsvRawPointDataset[] getFeaturesArray() throws InvalidParameterException, NoSuchElementException, IOException{
        if ( !this.dataRead) this.parse();
        if (this.featureArray == null)
            this.featureArray = (CsvRawPointDataset[])this.featureList.toArray(new CsvRawPointDataset[0]);
        return this.featureArray;
    }
    
    /**
     *@return List of CSVDataset objects
     *@throws InvalidParameterException
     *@throws NoSuchElementException
     *@throws IOException
     *@see CsvRawPointDataset
     */
    public List getFeaturesList() throws InvalidParameterException, NoSuchElementException, IOException{
        ArrayList list = new ArrayList();
        list.add(this.featureList);
        
        return list;
    }
    
    public boolean isEmpty() throws InvalidParameterException, NoSuchElementException, IOException{
        if ( !this.dataRead ) this.parse();
        return this.featureList.isEmpty();
    }
    
    public CsvRawPointDataset getFeature( int nr ) throws InvalidParameterException, NoSuchElementException, IOException{
        if ( this.getNumOfDatasets() > nr )
            return this.featureArray[nr];
        
        return null;
    }
    
    public int getNumOfDatasets() throws InvalidParameterException, NoSuchElementException, IOException{
        if ( !this.dataRead ) this.parse();
        return this.featureArray.length;
    }
    
    public void parse() throws InvalidParameterException, NoSuchElementException, IOException {
        if ( !this.dataRead ){
            dataRead = this.readData();
        }
    }
    
    /**
     * the actual parsing of the file...
     *@return true
     *@throws IOException
     *@throws InvalidParameterException
     */
    public boolean readData() throws IOException, InvalidParameterException {
        String line;
        String[] fields;
        CsvRawPointDataset csvdata = null;
        AttributeInfo col;
        
        int numCols = cols.length;
        
        boolean lineOk = true;
        
        for ( int i = 0; (line=this.buffReader.readLine())!=null; i++ ){
            if (line.length() == 0){
                // leere Zeile
                continue;
            } else if (this.skipLineIfNotEqualTo!=null){
                if (!line.startsWith(this.skipLineIfNotEqualTo)) continue;
            }
            
            if (this.separator == null){
                this.separator = DatasetPerRowCsvParser.guessSepatator(line);
            }
            
            fields = line.split(this.separator);
            
            csvdata = new CsvRawPointDataset(i);
            
            lineOk = true;
            
            if ( numCols != fields.length && !this.useDefValIfNeccessary){
                logger.printMinorError("skipped invalid dataset: unequal numbers of fields in line "+(i+4)+" and in header: " + fields.length  + " vs. "  + numCols);
                continue;
            } else if (fields.length == 0) {
                logger.printWarning("skipped empty line "+(i+4)+ ".", this.complainOnEmptyLine );
                continue;
            }
            
            
            for ( int j=0; j<numCols && j<fields.length && lineOk; j++ ){
                col = cols[j];
                
                //this.logger.printDebug("fields.length: " + fields.length + " (" + numCols + ")" );
                
                fields[j] = fields[j].trim();
                
                if (fields[j].length() == 0 && col.getNullValue() != null){
                    csvdata.put( col.getAttributeName(), col.getNullValue() );
                } else { 

                    if ( col.getAttributeType().equals(AttributeType.DOUBLE) ){
                        
                        if (fields[j].indexOf(",")>-1){
                            fields[j] = fields[j].replace(',', '.');
                        }
                        
                        try {
                            csvdata.put( col.getAttributeName(), Double.valueOf(fields[j]) );
                        } catch ( NumberFormatException nex ){
                            logger.printError(nex.getMessage() + " String: >" + fields[j] + "<, Line: " + i + ", column: " + j);
                            lineOk = false;
                        }
                        
                    } else if ( col.getAttributeType().equals( AttributeType.INTEGER ) )
                        csvdata.put( col.getAttributeName(), Integer.valueOf(fields[j]) );
                    else
                        csvdata.put( col.getAttributeName(), fields[j] );
                    
                    if ( j == this.xColumn || j == this.yColumn || j == this.zColumn){
                        // it's a coordinate!
                        
                        double coordinate = -1;
                        String coordinateStr = fields[j];
                        
                        if ( col.getAttributeType().equals(AttributeType.STRING) ){
                            if (coordinateStr.startsWith("N") || coordinateStr.startsWith("E") || coordinateStr.startsWith("S") || coordinateStr.startsWith("W"))
                                coordinateStr = coordinateStr.substring(1);
                            if (coordinateStr.indexOf(",")>-1){
                                coordinateStr = coordinateStr.replace(',', '.');
                            }
                        }
                            
                        try {
                            coordinate = Double.valueOf(coordinateStr).doubleValue();
                        } catch ( NumberFormatException nex ){
                            logger.printError(nex.getMessage() + ", coordinate: >" + fields[j] + "<, Line: " + i + ", column: " + j);
                            lineOk = false;
                        }

                        if (lineOk){
                            if ( j == this.xColumn ){
                                csvdata.setX(coordinate);
                            }
                            if ( j == this.yColumn ){
                                csvdata.setY(coordinate);
                            }
                            if ( j == this.zColumn ){
                                csvdata.setZ(coordinate);
                            }
                        }
                    }
                }
                
            }
            
            if (lineOk)
                this.featureList.add(csvdata);
        }
        this.dataRead = true;
        
        this.featureArray = this.getFeaturesArray();
        
        return true;
    }
    
    /**
     * creates a Jump FeatureCollection out of the given information.
     *@return FeatureCollection containing attribute and feature information 
     *@throws Exception
     */
    public PirolFeatureCollection createFeatureCollection(PirolFeatureCollectionRole role) throws Exception {
        if ( !this.dataRead ) this.parse();
        
        if (this.fc == null){
            FeatureSchema schema = this.createFeatureSchema();
            this.fc = new PirolFeatureCollection(this.createFeatureDataset(schema), role);
        }
        
        return this.fc;
    }
    
    public FeatureSchema createFeatureSchema() {
        FeatureSchema schema = new FeatureSchema();

        schema.addAttribute( "Geometrie", AttributeType.GEOMETRY );
        
        for ( int i=0; i<cols.length; i++){
            if (i != this.xColumn && i != this.yColumn && i != this.zColumn)
                schema.addAttribute( cols[i].getAttributeName(), cols[i].getAttributeType() );
        }
        
        return schema;
    }
    
    protected FeatureDataset createFeatureDataset(FeatureSchema fs) throws InvalidParameterException, NoSuchElementException, IOException {
        FeatureDataset fc = new FeatureDataset(fs);
            
        CsvRawPointDataset[] tmp = this.getFeaturesArray();
        
        BasicFeature feat;

        GeometryFactory geoFact = new GeometryFactory();
        Coordinate coord;
        Geometry geom;
        
        int numCols = this.cols.length;
        int numFeats = tmp.length;
        
        if (tmp == null) return null;

        for (int i = 0; i < numFeats; i++) {
            feat = new BasicFeature(fs);

            for (int j = 0; j < numCols; j++) {
                if (j != this.xColumn && j != this.yColumn && j != this.zColumn)
                    feat.setAttribute(cols[j].getAttributeName(), tmp[i].get(cols[j].getAttributeName()));
            }
            
            if (tmp[i].getZ()!=CsvRawPointDataset.INVALID_COORD){
                coord = new Coordinate(tmp[i].getX(), tmp[i].getY(), tmp[i].getZ() );
            } else {
                coord = new Coordinate(tmp[i].getX(), tmp[i].getY() );
            }
            
            geom = geoFact.createPoint( coord );
                            
            feat.setGeometry(geom);

            fc.add(feat);
        }

        return fc;
    }
    
    public static String guessSepatator( String line ){
        String separator = validSepatators[0];
        String dominatingSeparator = separator;
        int maxAppearance = 0;
        int appearance;
        
        for ( int i=0; i<validSepatators.length; i++ ){
            separator = validSepatators[i];
            appearance = line.split(separator).length;
            
            if ( appearance > maxAppearance ){
                maxAppearance = appearance;
                dominatingSeparator = separator;
            }
        }
        return dominatingSeparator;
    }


    /**
     *@return true if data from the file was parsed, false if not... 
     */
    public boolean isDataRead() {
        return dataRead;
    }



    public boolean isUseDefValIfNeccessary() {
        return useDefValIfNeccessary;
    }



    public void setUseDefValIfNeccessary(boolean useDefValIfNeccessary) {
        this.useDefValIfNeccessary = useDefValIfNeccessary;
    }



    public boolean isComplainOnEmptyLine() {
        return complainOnEmptyLine;
    }



    public void setComplainOnEmptyLine(boolean complainOnEmptyLine) {
        this.complainOnEmptyLine = complainOnEmptyLine;
    }



    public String getSkipLineIfNotEqualTo() {
        return skipLineIfNotEqualTo;
    }



    public void setSkipLineIfNotEqualTo(String skipLineIfNotEqualTo) {
        this.skipLineIfNotEqualTo = skipLineIfNotEqualTo;
    }



    public AttributeInfo[] getColumns() {
        return cols;
    }
    
    

}
