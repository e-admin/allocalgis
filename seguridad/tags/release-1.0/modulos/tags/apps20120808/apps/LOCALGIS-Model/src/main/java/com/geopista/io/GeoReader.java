package com.geopista.io;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.feature.GeopistaFeature;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.io.IllegalParametersException;
import com.vividsolutions.jump.io.JUMPReader;
import com.vividsolutions.jump.io.ParseException;



public class GeoReader extends DefaultHandler implements JUMPReader {
    static int STATE_GET_COLUMNS = 3;

    /**
     *  STATE   MEANING <br>
     *  0      Init <br>
     *  1      Waiting for Collection tag <br>
     *  2      Waiting for Feature tag <br>
     *  3      Getting jcs columns <br>
     *  4      Parsing geometry (goes back to state 3) <br>
     *  1000   Parsing Multi-geometry, recursion level =1 <br>
     *  1001   Parsing Multi-geometry, recursion level =2 <br>
     */
    static int STATE_INIT = 0;
    static int STATE_PARSE_GEOM_NESTED = 1000;
    static int STATE_PARSE_GEOM_SIMPLE = 4;
    static int STATE_WAIT_COLLECTION_TAG = 1;
    static int STATE_WAIT_FEATURE_TAG = 2;
    GeoGMLInputTemplate GMLinput = null;
    int STATE = STATE_INIT; //list of points
    Point apoint;
    Feature currentFeature;
    int currentGeometryNumb = 1;
    FeatureCollection fc;
    FeatureSchema fcmd; // list of geometries
    Geometry finalGeometry; //list of geometrycollections - list of list of geometry
    ArrayList geometry;
    GeometryFactory geometryFactory = new GeometryFactory(); //list of points
    ArrayList innerBoundaries = new ArrayList();
    Attributes lastStartTag_atts;
    String lastStartTag_name;
    String lastStartTag_qName; //accumulate values inside a tag

    // info about the last start tag encountered
    String lastStartTag_uri;
    LineString lineString;
    LinearRing linearRing; // a LR
    LinearRing outerBoundary; //list of LinearRing
    ArrayList pointList = new ArrayList(); // list of accumulated points (Coordinate)
    Polygon polygon; // polygon

    // higherlevel geomery object
    ArrayList recursivegeometry = new ArrayList();

    // low-level geometry objects
    Coordinate singleCoordinate = new Coordinate();
    String streamName; //result geometry  -
    String tagBody;
    XMLReader xr; //see above

    /**
     *  Constructor - make a SAXParser and have this GMLReader be its
     *  ContentHandler and ErrorHandler.
     */
    public GeoReader() {
        super();
        xr = new org.apache.xerces.parsers.SAXParser();
        xr.setContentHandler(this);
        xr.setErrorHandler(this);
    }

    /**
     *  Attach a GMLInputTemplate information class.
     *
     *@param  template  The new inputTemplate value
     */
    public void setInputTemplate(GeoGMLInputTemplate template) {
        GMLinput = template;
    }

    /**
     *  SAX handler - store and accumulate tag bodies
     *
     *@param  ch                Description of the Parameter
     *@param  start             Description of the Parameter
     *@param  length            Description of the Parameter
     *@exception  SAXException  Description of the Exception
     */
    public void characters(char[] ch, int start, int length)
        throws SAXException {
        try {
            String part;
            part = new String(ch, start, length);
            tagBody = tagBody + part;
        } catch (Exception e) {
            throw new SAXException(e.getMessage());
        }
    }

    /**
     *  SAX HANDLER - move to state 0
     */
    public void endDocument() {
        //System.out.println("End document");
        STATE = STATE_INIT;
    }

    /**
     *  SAX handler - handle state information and transitions based on ending
     *  elements Most of the work of the parser is done here.
     *
     *@param  uri               Description of the Parameter
     *@param  name              Description of the Parameter
     *@param  qName             Description of the Parameter
     *@exception  SAXException  Description of the Exception
     */
    public void endElement(String uri, String name, String qName)
        throws SAXException {
        try {
            int index;

            // System.out.println("End element: " + qName);
            if (STATE == STATE_INIT) {
                tagBody = "";

                return; //something wrong
            }

            if (STATE > STATE_GET_COLUMNS) {
                if (isMultiGeometryTag(qName)) {
                    if (STATE == STATE_PARSE_GEOM_NESTED) {
                        STATE = STATE_PARSE_GEOM_SIMPLE; //finished - no action.  geometry is correct
                    } else {
                        //build the geometry that was in that collection
                        Geometry g;

                        g = geometryFactory.buildGeometry(geometry);
                        geometry = (ArrayList) recursivegeometry.get(STATE -
                                STATE_PARSE_GEOM_NESTED - 1);
                        geometry.add(g);
                        recursivegeometry.remove(STATE -
                            STATE_PARSE_GEOM_NESTED);
                        g = null;

                        STATE--;
                    }
                }

                if (GMLinput.isGeometryElement(qName)) {
                    tagBody = "";
                    STATE = STATE_GET_COLUMNS;

                    finalGeometry = geometryFactory.buildGeometry(geometry);

                    //System.out.println("end geom: "+finalGeometry.toString() );
                    currentFeature.setGeometry(finalGeometry);
                    currentGeometryNumb++;

                    return;
                }

                //these correspond to <coord><X>0.0</X><Y>0.0</Y></coord>
                if ((qName.compareToIgnoreCase("X") == 0) ||
                        (qName.compareToIgnoreCase("gml:X") == 0)) {
                    singleCoordinate.x = (new Double(tagBody)).doubleValue();
                } else if ((qName.compareToIgnoreCase("Y") == 0) ||
                        (qName.compareToIgnoreCase("gml:y") == 0)) {
                    singleCoordinate.y = (new Double(tagBody)).doubleValue();
                } else if ((qName.compareToIgnoreCase("Z") == 0) ||
                        (qName.compareToIgnoreCase("gml:z") == 0)) {
                    singleCoordinate.z = (new Double(tagBody)).doubleValue();
                } else if ((qName.compareToIgnoreCase("COORD") == 0) ||
                        (qName.compareToIgnoreCase("gml:coord") == 0)) {
                    pointList.add(new Coordinate(singleCoordinate)); //remember it
                }
                // this corresponds to <gml:coordinates>1195156.78946687,382069.533723461</gml:coordinates>
                else if ((qName.compareToIgnoreCase("COORDINATES") == 0) ||
                        (qName.compareToIgnoreCase("gml:coordinates") == 0)) {
                    //tagBody has a wack-load of points in it - we need
                    // to parse them into the pointList list.
                    // assume that the x,y,z coordinate are "," separated, and the points are " " separated
                    parsePoints(tagBody, geometryFactory);
                } else if ((qName.compareToIgnoreCase("linearring") == 0) ||
                        (qName.compareToIgnoreCase("gml:linearring") == 0)) {
                    Coordinate[] c = new Coordinate[0];

                    c = (Coordinate[]) pointList.toArray(c);

                    //c= (Coordinate[])l;
                    linearRing = geometryFactory.createLinearRing(c);
                } else if ((qName.compareToIgnoreCase("outerBoundaryIs") == 0) ||
                        (qName.compareToIgnoreCase("gml:outerBoundaryIs") == 0)) {
                    outerBoundary = linearRing;
                } else if ((qName.compareToIgnoreCase("innerBoundaryIs") == 0) ||
                        (qName.compareToIgnoreCase("gml:innerBoundaryIs") == 0)) {
                    innerBoundaries.add(linearRing);
                } else if ((qName.compareToIgnoreCase("polygon") == 0) ||
                        (qName.compareToIgnoreCase("gml:polygon") == 0)) {
                    //LinearRing[] lrs = new LinearRing[1];
                    LinearRing[] lrs = new LinearRing[0];

                    lrs = (LinearRing[]) innerBoundaries.toArray(lrs);
                    polygon = geometryFactory.createPolygon(outerBoundary, lrs);
                    geometry.add(polygon);
                } else if ((qName.compareToIgnoreCase("linestring") == 0) ||
                        (qName.compareToIgnoreCase("gml:linestring") == 0)) {
                    Coordinate[] c = new Coordinate[0];

                    c = (Coordinate[]) pointList.toArray(c);

                    lineString = geometryFactory.createLineString(c);
                    geometry.add(lineString);
                } else if ((qName.compareToIgnoreCase("point") == 0) ||
                        (qName.compareToIgnoreCase("gml:point") == 0)) {
                    apoint = geometryFactory.createPoint((Coordinate) pointList.get(
                                0));
                    geometry.add(apoint);
                }
            } else if (STATE == STATE_GET_COLUMNS) {
                if (qName.compareToIgnoreCase(GMLinput.featureTag) == 0) {
                    tagBody = "";
                    STATE = STATE_WAIT_FEATURE_TAG;

                    //System.out.println("end feature");
                    //create a feature and put it inside the featurecollection
                    if (currentFeature.getGeometry() == null) {
                        Geometry g = currentFeature.getGeometry();

                        if (g != null) {
                            System.out.println(g.toString());
                        }

                        throw new ParseException(
                            "no geometry specified in feature");
                    }

                    fc.add(currentFeature);
                    currentFeature = null;

                    return;
                } else {
                    //check to see if this was a tag we want to store as a column
                    try {
                        if ((index = GMLinput.match(lastStartTag_qName,
                                        lastStartTag_atts)) > -1) {
                            // System.out.println("value of " + GMLinput.columnName(index)+" : " +  GMLinput.getColumnValue(index,tagBody, lastStartTag_atts) );
                            currentFeature.setAttribute(GMLinput.columnName(
                                    index),
                                GMLinput.getColumnValue(index, tagBody,
                                    lastStartTag_atts));
                        }
                    } catch (Exception e) {
                        //dont actually do anything with the parse problem - just ignore it,
                        // we cannot send it back because the function its overiding doesnt allow
                        e.printStackTrace();
                    }

                    tagBody = "";
                }
            } else if (STATE == STATE_WAIT_FEATURE_TAG) {
                if (qName.compareToIgnoreCase(GMLinput.collectionTag) == 0) {
                    STATE = STATE_INIT; //finish

                    //System.out.println("DONE!");
                    tagBody = "";

                    return;
                }
            } else if (STATE == STATE_WAIT_COLLECTION_TAG) {
                tagBody = "";

                return; //still look for start collection tag
            }
        } catch (Exception e) {
            throw new SAXException(e.getMessage());
        }
    }

    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public void fatalError(SAXParseException exception)
        throws SAXException {
        throw exception;
    }

    /**
     *  Main Entry - load in a GML file
     *
     *@param  dp                              Description of the Parameter
     *@return                                 Description of the Return Value
     *@exception  IllegalParametersException  Description of the Exception
     *@exception  Exception                   Description of the Exception
     */
    public FeatureCollection read(DriverProperties dp)
        throws IllegalParametersException, Exception {
        FeatureCollection fc;
        GeoGMLInputTemplate gmlTemplate;
        String inputFname;
        boolean isCompressed;
        boolean isCompressed_template;

        isCompressed_template = (dp.getProperty("CompressedFileTemplate") != null);

        isCompressed = (dp.getProperty("CompressedFile") != null);

        inputFname = dp.getProperty("File");

        if (inputFname == null) {
            inputFname = dp.getProperty("DefaultValue");
        }

        if (inputFname == null) {
            throw new IllegalParametersException(
                "call to GMLReader.read() has DataProperties w/o a InputFile specified");
        }

//        inputFname = parseFromUtf8ToLatin(inputFname); //parche acentos
        if (dp.getProperty("TemplateFile") == null) {
            // load from .gml file
            if (isCompressed) {
                InputStream in = GeopistaCompressedFile.openFile(inputFname,
                        dp.getProperty("CompressedFile"));
                gmlTemplate = inputTemplateFromFile(in);
                in.close();
            } else {
                gmlTemplate = inputTemplateFromFile(inputFname);
            }
        } else {
            //template file specified
            if (isCompressed_template) {
                InputStream in = GeopistaCompressedFile.openFile(dp.getProperty(
                            "TemplateFile"),
                        dp.getProperty("CompressedFileTemplate"));
                gmlTemplate = inputTemplateFromFile(in);
                in.close();
            } else {
                if (isCompressed) //special case if the .gml file is compressed, and a template file is specified
                 {
                    if (dp.getProperty("CompressedFile").equals(dp.getProperty(
                                    "TemplateFile"))) //the template file is the compressed file
                     {
                        InputStream in = GeopistaCompressedFile.openFile(inputFname,
                                dp.getProperty("CompressedFile"));
                        gmlTemplate = inputTemplateFromFile(in);
                        in.close();
                    } else {
                        gmlTemplate = inputTemplateFromFile(dp.getProperty(
                                    "TemplateFile"));
                    }
                } else {
                    //normal load
                    gmlTemplate = inputTemplateFromFile(dp.getProperty(
                                "TemplateFile"));
                }
            }
        }

        java.io.Reader r;

        this.setInputTemplate(gmlTemplate);

        if (isCompressed) {
            r = new BufferedReader(new InputStreamReader(
                        GeopistaCompressedFile.openFile(inputFname,
                            dp.getProperty("CompressedFile"))));
        } else {
            r = new BufferedReader(new FileReader(inputFname));
        }

        fc = read(r, inputFname);
        r.close();

        return fc;
    }

    /**
     *  Helper function - calls read(java.io.Reader r,String readerName) with the
     *  readerName "Unknown Stream". You should have already called
     *  setInputTempalate().
     *
     *@param  r              reader to read the GML File from
     *@return                Description of the Return Value
     *@exception  Exception  Description of the Exception
     */
    public FeatureCollection read(java.io.Reader r) throws Exception {
        return read(r, "Unknown Stream");
    }

    /**
     *  Main function to read a GML file. You should have already called
     *  setInputTempalate().
     *
     *@param  r              reader to read the GML File from
     *@param  readerName     what to call the reader for error reporting
     *@return                Description of the Return Value
     *@exception  Exception  Description of the Exception
     */
    public FeatureCollection read(java.io.Reader r, String readerName)
        throws Exception {
        LineNumberReader myReader = new LineNumberReader(r);

        if (GMLinput == null) {
            throw new ParseException(
                "you must set the GMLinput template first!");
        }

        streamName = readerName;
        fcmd = GMLinput.toFeatureSchema(readerName);
        fc = new FeatureDataset(fcmd);

        try {
            xr.parse(new InputSource(myReader));
        } catch (SAXParseException e) {
            throw new ParseException(e.getMessage() + "  Last Opened Tag: " +
                lastStartTag_qName + ".  Reader reports last line read as " +
                myReader.getLineNumber(),
                streamName + " - " + e.getPublicId() + " (" + e.getSystemId() +
                ") ", e.getLineNumber(), e.getColumnNumber());
        } catch (SAXException e) {
            throw new ParseException(e.getMessage() + "  Last Opened Tag: " +
                lastStartTag_qName, streamName, myReader.getLineNumber(), 0);
        }

        //Bloqueamos las features para que se apliquen
        Collection lookFeatures = fc.getFeatures();
        Iterator lookFeaturesIter = lookFeatures.iterator();
        while (lookFeaturesIter.hasNext())
        {
          GeopistaFeature actualFeature = (GeopistaFeature) lookFeaturesIter.next();
          actualFeature.setLockedFeature(true);
        }
        
        return fc;
    }

    ////////////////////////////////////////////////////////////////////
    // Event handlers.
    ////////////////////////////////////////////////////////////////////

    /**
     *  SAX handler - move to state 1
     */
    public void startDocument() {
        //System.out.println("Start document");
        tagBody = "";
        STATE = STATE_WAIT_COLLECTION_TAG;
    }

    /**
     *  SAX handler. Handle state and state transitions based on an element
     *  starting
     *
     *@param  uri               Description of the Parameter
     *@param  name              Description of the Parameter
     *@param  qName             Description of the Parameter
     *@param  atts              Description of the Parameter
     *@exception  SAXException  Description of the Exception
     */
    public void startElement(String uri, String name, String qName,
        Attributes atts) throws SAXException {
        try {
            //System.out.println("Start element: " + qName);
            tagBody = "";
            lastStartTag_uri = uri;
            lastStartTag_name = name;
            lastStartTag_qName = qName;
            lastStartTag_atts = atts;

            if (STATE == STATE_INIT) {
                return; //something wrong
            }

            if ((STATE == STATE_WAIT_COLLECTION_TAG) &&
                    (qName.compareToIgnoreCase(GMLinput.collectionTag) == 0)) {
                //found the collection tag
                // System.out.println("found collection");
                STATE = STATE_WAIT_FEATURE_TAG;

                return;
            }

            if ((STATE == STATE_WAIT_FEATURE_TAG) &&
                    (qName.compareToIgnoreCase(GMLinput.featureTag) == 0)) {
                //found the feature tag
                //System.out.println("found feature");
                currentFeature = new GeopistaFeature(fcmd);
                STATE = STATE_GET_COLUMNS;

                return;
            }

            if ((STATE == STATE_GET_COLUMNS) &&
                    GMLinput.isGeometryElement(qName)) {
                //found the geom tag
                // System.out.println("found geom #"+currentGeometryNumb );
                recursivegeometry = new ArrayList();
                geometry = new ArrayList();
                recursivegeometry.add(geometry);

                // recursivegeometry[0] = geometry
                finalGeometry = null;
                STATE = STATE_PARSE_GEOM_SIMPLE;

                return;
            }

            if ((STATE >= STATE_PARSE_GEOM_SIMPLE) &&
                    ((qName.compareToIgnoreCase("coord") == 0) ||
                    (qName.compareToIgnoreCase("gml:coord") == 0))) {
                singleCoordinate.x = Double.NaN;
                singleCoordinate.y = Double.NaN;
                singleCoordinate.z = Double.NaN;
            }

            if ((STATE >= STATE_PARSE_GEOM_SIMPLE) &&
                    (!((qName.compareToIgnoreCase("X") == 0) ||
                    (qName.compareToIgnoreCase("gml:x") == 0) ||
                    (qName.compareToIgnoreCase("y") == 0) ||
                    (qName.compareToIgnoreCase("gml:y") == 0) ||
                    (qName.compareToIgnoreCase("z") == 0) ||
                    (qName.compareToIgnoreCase("gml:z") == 0) ||
                    (qName.compareToIgnoreCase("coord") == 0) ||
                    (qName.compareToIgnoreCase("gml:coord") == 0)))) {
                pointList.clear(); //clear out any accumulated points
            }

            if ((STATE >= STATE_PARSE_GEOM_SIMPLE) &&
                    ((qName.compareToIgnoreCase("polygon") == 0) ||
                    (qName.compareToIgnoreCase("gml:polygon") == 0))) {
                innerBoundaries.clear(); //polygon just started - clear out the last one
            }

            if ((STATE > STATE_GET_COLUMNS) && (isMultiGeometryTag(qName))) {
                //in state 4 or a 1000 state and found a start GC (or Multi-geom) event
                if (STATE == STATE_PARSE_GEOM_SIMPLE) {
                    // geometry already = recursivegeometry[0]
                    STATE = STATE_PARSE_GEOM_NESTED;
                } else {
                    STATE++;
                    geometry = new ArrayList();
                    recursivegeometry.add(geometry);
                }
            }
        } catch (Exception e) {
            throw new SAXException(e.getMessage());
        }
    }

    ////////////////////////////////////////////////////////////////////
    // Error handlers.
    ////////////////////////////////////////////////////////////////////
    public void warning(SAXParseException exception) throws SAXException {
        throw exception;
    }

    /**
     *  returns true if the the string represents a multi* geometry type
     *
     *@param  s  Description of the Parameter
     *@return    The multiGeometryTag value
     */
    private boolean isMultiGeometryTag(String s) {
        //remove the "gml:" if its there
        if ((s.length() > 5) &&
                (s.substring(0, 5).compareToIgnoreCase("gml:") == 0)) {
            s = s.substring(5);
        }

        if ((s.compareToIgnoreCase("multigeometry") == 0) ||
                (s.compareToIgnoreCase("multipoint") == 0) ||
                (s.compareToIgnoreCase("multilinestring") == 0) ||
                (s.compareToIgnoreCase("multipolygon") == 0)) {
            return true;
        }

        return false;
    }

    private GeoGMLInputTemplate inputTemplateFromFile(InputStream in)
        throws ParseException, FileNotFoundException, IOException {
        GeoGMLInputTemplate result;
        java.io.Reader r = new BufferedReader(new InputStreamReader(in));
        result = inputTemplate(r);
        r.close();

        return result;
    }

    private GeoGMLInputTemplate inputTemplateFromFile(String filename)
        throws ParseException, FileNotFoundException, IOException {
        GeoGMLInputTemplate result;
        java.io.Reader r = new BufferedReader(new FileReader(filename));
        result = inputTemplate(r);
        r.close();

        return result;
    }

    /**
     *  Parse a bunch of points - stick them in pointList. Handles 2d and 3d.
     *
     *@param  ptString         string containing a bunch of coordinates
     *@param  geometryFactory  JTS point/coordinate factory
     */
    private void parsePoints(String ptString, GeometryFactory geometryFactory) {
        String aPoint;
        StringTokenizer stokenizerPoint;
        Coordinate coord = new Coordinate();
        int dim;
        String numb;
        StringBuffer sb;
        int t;
        char ch;

        //remove \n and \r and replace with spaces
        sb = new StringBuffer(ptString);

        for (t = 0; t < sb.length(); t++) {
            ch = sb.charAt(t);

            if ((ch == '\n') || (ch == '\r')) {
                sb.setCharAt(t, ' ');
            }
        }

        StringTokenizer stokenizer = new StringTokenizer(new String(sb), " ",
                false);

        while (stokenizer.hasMoreElements()) {
            //have a point in memory - handle the single point
            aPoint = stokenizer.nextToken();
            stokenizerPoint = new StringTokenizer(aPoint, ",", false);
            coord.x = coord.y = coord.z = Double.NaN;
            dim = 0;

            while (stokenizerPoint.hasMoreElements()) {
                numb = stokenizerPoint.nextToken();

                if (dim == 0) {
                    coord.x = Double.parseDouble(numb);
                } else if (dim == 1) {
                    coord.y = Double.parseDouble(numb);
                } else if (dim == 3) {
                    coord.z = Double.parseDouble(numb);
                }

                dim++;
            }

            pointList.add(coord); //remember it
            coord = new Coordinate();
            stokenizerPoint = null;
        }
    }

    private GeoGMLInputTemplate inputTemplate(java.io.Reader r)
        throws IOException, ParseException {
        GeoGMLInputTemplate gmlTemplate = new GeoGMLInputTemplate();
        gmlTemplate.load(r);
        r.close();

        if (!(gmlTemplate.loaded)) {
            throw new ParseException("Failed to load GML input template");
        }

        return gmlTemplate;
    }
    //para solucionar el problema de los acentos en las capas
    public static String parseFromUtf8ToLatin(String src) {
    	String out = null;
    	try {
    		out = new String(src.getBytes("8859_1"), "utf-8");
    	}catch (Exception e) {
    		System.err.println("Problema al realizar la transformacion de UTF8 a Latin de la cadena: " + src + " " + e.getMessage());
		}
    	return out;
    }
    
}
