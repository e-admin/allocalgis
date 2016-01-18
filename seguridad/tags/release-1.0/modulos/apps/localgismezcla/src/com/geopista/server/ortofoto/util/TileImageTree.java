package com.geopista.server.ortofoto.util;

/*----------------    FILE HEADER  ------------------------------------------

This file is part of deegree.
Copyright (C) 2001 by:
EXSE, Department of Geography, University of Bonn
http://www.giub.uni-bonn.de/exse/
lat/lon Fitzke/Fretter/Poth GbR
http://www.lat-lon.de

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

Contact:

Andreas Poth
lat/lon Fitzke/Fretter/Poth GbR
Meckenheimer Allee 176
53115 Bonn
Germany
E-Mail: poth@lat-lon.de

Jens Fitzke
Department of Geography
University of Bonn
Meckenheimer Allee 166
53115 Bonn
Germany
E-Mail: jens.fitzke@uni-bonn.de

                 
 ---------------------------------------------------------------------------*/
import com.geopista.server.ortofoto.ImportadorOrtofotos;
import com.sun.media.jai.codec.FileSeekableStream;

import java.awt.*;
import java.awt.image.*;


import java.io.*;

import java.util.*;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import org.deegree.graphics.Encoders;
import org.deegree.xml.*;

import org.w3c.dom.*;


/**
 * The program creates a pyramidal structure for a geo-referenced image and splits
 * each level of the 'pyramid' into several tiles. The tiles will be arranged into
 * a Quad-Tree datastructure. The description of this datastructure will be stored
 * into a XML-document that is conform to the deegree coverage descriptor format.
 * (see CVDescriptor.xsd). So the result of the tileing can directly be use with
 * the deegree GridCoverage and Web Coverage Service implementations for fast
 * access to large amounts of raster data.
 * <p> ----------------------------------------------------------------------</p>
 * @author  <a href="mailto:poth@latlon.de">Andreas Poth</a>
 * @version 27.12.2002
 */
public class TileImageTree {
    // main image
    private BufferedImage image = null;
    private ProgressObserver progressObserver = null;
    // layerID and title of the CV layer
    private String imageName = null;
    private String targetDir = null;
    private String targetFormat = null;
    private double[] targetResolutions = null;
    // main image resolution
    private double resx = 0;
    private double resy = 0;
    private double xmax = 0;
    // main image bounding box
    private double xmin = 0;
    private double ymax = 0;
    private double ymin = 0;
    private float quality = 0;
    private int count = 0;
    private int startIndex = 0;
    private String crs = null;
    
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TileImageTree.class);

    /** Creates a new instance of TileImageTree */
    public TileImageTree( String imageSource, String targetDir, String targetFormat, 
                          double[] targetResolutions, int startIndex, float quality,
                          String crs)
                  throws Exception {
        this.targetDir = targetDir;
        this.targetFormat = targetFormat.toLowerCase();
        this.quality = quality;
        this.targetResolutions = targetResolutions;
        this.progressObserver = new ProgressObserver();

        int pos = imageSource.lastIndexOf( '/' );
        this.imageName = imageSource.substring( pos + 1, imageSource.length() );
        this.startIndex = startIndex;
        this.crs = crs;

        // load the main image
        image = loadImage( imageSource );

        // get the bounding box of the source image by evaluating its world file
        readWorldFile( imageSource );
    }

    /**
     * sets a user defined observer for controling the progress of the tileing
     */
    public void setProgressObserver( ProgressObserver progressObserver ) {
        this.progressObserver = progressObserver;
    }

    /**
     * loads the base image
     */
    private BufferedImage loadImage( String imageSource ) throws Exception {
    	
    	logger.debug("Inicio TileImageTree.loadImage()...");
    	logger.warn("Reading source image...");
        System.out.println( "reading source image ..." );

        BufferedImage bi = null;
        FileSeekableStream fss = new FileSeekableStream( imageSource );
        RenderedOp rop = JAI.create( "stream", fss );
        bi = rop.getAsBufferedImage();
        System.gc();
        logger.warn("Reading source image finished");
        System.out.println( "finished" );
        return bi;
    }

    /**
     * starts the creation of the tiles
     */
    public void createTileImageTree() throws Exception {
    	
    	try {
	    	logger.debug("Inicio TileImageTree.createTileImageTree()...");
	    	logger.warn("Creating tile image tree...");
	        Document doc = XMLTools.create();
	        Element root = doc.createElement( "CVDescriptor" );
	        root.setAttribute( "xmlns", "http://www.deegree.org/gc" );
	        root.setAttribute( "xmlns:wcs", "http://www.opengis.net/wcs" );
	        doc.appendChild( root );
	        int pos = imageName.lastIndexOf('.');
	        String tmp = imageName.substring( 0, pos );
	        addGridCoverageLayer( root, tmp, tmp, crs, xmin, ymin, xmax, ymax, xmin, 
	                              ymin, xmax, ymax, image.getWidth() - 1, image.getHeight() - 1 );
	
	        System.out.println( "creating tiles ..." );
	        logger.warn("Creating tiles...");
	        tile( image, xmin, ymin, xmax, ymax, 0, root );
	        logger.warn("100%");
	        logger.warn("Creating tiles finished");
	        System.out.println( "100%" );
	        System.out.println( "finished" );
	
	        logger.warn("Writing descriptor...");
	        FileOutputStream fos = new FileOutputStream( targetDir + "/gvDesc.xml" );
	        fos.write( DOMPrinter.nodeToString( doc, "ISO-8859-1" ).getBytes() );
	        fos.close();
	        logger.warn("Creating tile image tree finished");
    	} catch (Throwable e) {
    		logger.error("ERROR in TileImageTree.createTileImageTree()");
    		logger.debug(e.getMessage());
    		e.printStackTrace();
    		throw new Exception(e.getMessage());
    	}
    }

    /**
     * the method performes the creation of the tiles and the filling of the quadtree
     * XML-document. the method will be call in a recursion for each defined level
     * (scale).
     */
    private void tile( BufferedImage img, double xmin, double ymin, double xmax, double ymax, 
                       int res, Element parent ) throws Exception {
        // break condition
        if ( res >= targetResolutions.length ) {
            return;
        }
  
        // create level node 
        Element level = parent.getOwnerDocument().createElement( "Level" );

        if ( res == 0 ) {
            level.setAttribute( "maxScale", "99999999" );
        } else {
            level.setAttribute( "maxScale", "" + targetResolutions[res - 1] );
        }

        if ( res == ( targetResolutions.length - 1 ) ) {
            level.setAttribute( "minScale", "0" );
        } else {
            level.setAttribute( "minScale", "" + targetResolutions[res] );
        }

        parent.appendChild( level );

        BufferedImage im = null;
        double xmin_ = 0;
        double ymin_ = 0;
        double xmax_ = 0;
        double ymax_ = 0;
        // calculate half of tile width and height to get tile (quarter) coordinates
        double x2 = ( xmax - xmin ) / 2d;
        double y2 = ( ymax - ymin ) / 2d;

        // create the four quarters (tiles) for the submitted image and call this method
        // in a recursion to create the next resolution level
        for ( int i = 0; i < 4; i++ ) {
            switch ( i ) {
                case 0:
                {
                    // tile bounding box      
                    xmin_ = xmin;
                    ymin_ = ymin;
                    xmax_ = xmin + x2;
                    ymax_ = ymin + y2;
                    im = img.getSubimage( 0, img.getHeight() / 2, img.getWidth() / 2, 
                                          img.getHeight() / 2 );
                    break;
                }
                case 1:
                {
                    // tile bounding box
                    xmin_ = xmin + x2;
                    ymin_ = ymin;
                    xmax_ = xmax;
                    ymax_ = ymin + y2;
                    im = img.getSubimage( img.getWidth() / 2, img.getHeight() / 2, 
                                          img.getWidth() / 2, img.getHeight() / 2 );
                    break;
                }
                case 2:
                {
                    // tile bounding box
                    xmin_ = xmin;
                    ymin_ = ymin + y2;
                    xmax_ = xmin + x2;
                    ymax_ = ymax;
                    im = img.getSubimage( 0, 0, img.getWidth() / 2, img.getHeight() / 2 );
                    break;
                }
                case 3:
                {
                    // tile bounding box
                    xmin_ = xmin + x2;
                    ymin_ = ymin + y2;
                    xmax_ = xmax;
                    ymax_ = ymax;
                    im = img.getSubimage( img.getWidth() / 2, 0, img.getWidth() / 2, 
                                          img.getHeight() / 2 );
                    break;
                }
            }

            // calculate the tiles width and height for the current resolution
            int tilex = (int)Math.round( ( xmax_ - xmin_ ) / targetResolutions[res] );
            int tiley = (int)Math.round( ( ymax_ - ymin_ ) / targetResolutions[res] );

            // create new image (tile)
            BufferedImage tmp = new BufferedImage( tilex, tiley, BufferedImage.TYPE_INT_RGB );
            Graphics g = tmp.getGraphics();
            g.drawImage( im, 0, 0, tilex, tiley, null );
            g.dispose();

            // observer output
            progressObserver.write( new Integer( count ) );

            // save tile to the filesystem
            String file = saveTile( tmp );
            tmp = null;
            System.gc();

            // add a new Tile-node to the quad-tree
            Element tile = addNode( xmin_, ymin_, xmax_, ymax_, file, level );

            // recursion !
            tile( im, xmin_, ymin_, xmax_, ymax_, res + 1, tile );
            im = null;
            System.gc();
        }
    }

    /**
     * stores one image (tile) in the desired format to the desired target 
     * directory. 
     */
    private String saveTile( BufferedImage img ) throws Exception {
        count++;

        String file = targetDir + "/tile_" + ( startIndex++ ) + "." + targetFormat;
        FileOutputStream fos = new FileOutputStream( file );

        if ( targetFormat.equalsIgnoreCase( "bmp" ) ) {
            Encoders.encodeBmp( fos, img );
        } else if ( targetFormat.equalsIgnoreCase( "gif" ) ) {
            Encoders.encodeGif( fos, img );
        } else if ( targetFormat.equalsIgnoreCase( "png" ) ) {
            Encoders.encodePng( fos, img );
        } else if ( targetFormat.equalsIgnoreCase( "tiff" ) || 
                        targetFormat.equalsIgnoreCase( "tif" ) ) {
            Encoders.encodeTiff( fos, img );
        } else if ( targetFormat.equalsIgnoreCase( "jpg" ) || 
                        targetFormat.equalsIgnoreCase( "jpeg" ) ) {
            Encoders.encodeJpeg( fos, img, quality );
        }

        fos.close();
        return file;
    }

    /**
     * adds a new Tile-node to the GVDescriptor XML-document
     */
    private Element addNode( double xmin, double ymin, double xmax, double ymax, String file, 
                             Element parent ) throws Exception {
        // create Tile node 
        java.text.DecimalFormat format = new java.text.DecimalFormat( "####.###");
        Element tile = parent.getOwnerDocument().createElement( "Tile" );
        tile.setAttribute( "minx", "" + format.format(xmin).replace(',','.') );
        tile.setAttribute( "miny", "" + format.format(ymin).replace(',','.') );
        tile.setAttribute( "maxx", "" + format.format(xmax).replace(',','.') );
        tile.setAttribute( "maxy", "" + format.format(ymax).replace(',','.') );
        tile.setAttribute( "resource", "file:///" + file );
        parent.appendChild( tile );
        return tile;
    }

    /**
     * Gets the latitude and longitude coordinates (xmin, ymin, xmax and ymax) 
     * of the image.
     */
    private void readWorldFile( String filename ) throws Exception {
    	
    	logger.debug("Inicio TileImageTree.readWorldFile()...");
    	logger.warn("Reading worldfile...");
        try {
            // Gets the substring beginning at the specified beginIndex (0) - the
            // beginning index, inclusive - and extends to the character at
            //	index endIndex (position of '.') - the ending index, exclusive.
            String fname = null;
            int pos = filename.lastIndexOf( "." );
            filename = filename.substring( 0, pos );

            //Looks for corresponding worldfiles.       
            if ( ( new File( filename + ".tfw" ) ).exists() ) {
                fname = filename + ".tfw";
            } else if ( ( new File( filename + ".wld" ) ).exists() ) {
                fname = filename + ".wld";
            } else if ( ( new File( filename + ".jgw" ) ).exists() ) {
                fname = filename + ".jgw";
            } else if ( ( new File( filename + ".jpgw" ) ).exists() ) {
                fname = filename + ".jpgw";
            } else if ( ( new File( filename + ".gfw" ) ).exists() ) {
                fname = filename + ".gfw";
            } else if ( ( new File( filename + ".gifw" ) ).exists() ) {
                fname = filename + ".gifw";
            } else if ( ( new File( filename + ".pgw" ) ).exists() ) {
                fname = filename + ".pgw";
            } else if ( ( new File( filename + ".pngw" ) ).exists() ) {
                fname = filename + ".pngw";
            } else {
                throw new Exception( "Not a world file for: " + filename );
            }

            // Reads character files.
            // The constructors of this class (FileReader) assume that the default character
            //	 encoding and the default byte-buffer size are appropriate.
            // The BufferedReader reads text from a character-input stream, buffering characters so as
            //	to provide for the efficient reading of characters
            BufferedReader br = new BufferedReader( new FileReader( fname ) );

            String s = null;

            int cnt = 0;
            double d1 = 0;
            double d2 = 0;
            double d3 = 0;
            double d4 = 0;

            while ( ( s = br.readLine() ) != null ) {
                cnt++;
                s = s.trim();

                switch ( cnt ) {
                    case 1:
                        d1 = Double.parseDouble( s );
                        break;
                    case 4:
                        d2 = Double.parseDouble( s );
                        break;
                    case 5:
                        d3 = Double.parseDouble( s );
                        break;
                    case 6:
                        d4 = Double.parseDouble( s );
                        break;
                }
            }

            br.close();

            double d5 = d3 + ( image.getWidth() * d1 );
            double d6 = d4 + ( image.getHeight() * d2 );

            ymax = d4;
            ymin = d6;
            xmax = d5;
            xmin = d3;

            resx = ( xmax - xmin ) / (double)image.getWidth();
            resy = ( ymax - ymin ) / (double)image.getHeight();
            logger.warn("Reading worldfile finished");
        } catch ( Exception ex ) {
        	logger.error("ERROR leyendo worldfile");
            System.out.println( ex );
        }
    }

    /**
     * adds a new grid coverage layer to a WCS
     */
    private void addGridCoverageLayer( Node node, String layerID, String title, String crs, 
                                       double llminx, double llminy, double llmaxx, double llmaxy, 
                                       double minx, double miny, double maxx, double maxy, 
                                       double width, int height ) throws Exception {
    	
    	logger.debug("Inicio TileImageTree.addGridCoverageLayer()...");
    	logger.warn("Adding grid coverage layer...");
        StringBuffer sb = new StringBuffer( 1000 );        
        sb.append( "<wcs:GridCoverageLayer xmlns:wcs=\"http://www.opengis.net/wcs\">" );
        sb.append( "<wcs:LayerID>" + layerID + "</wcs:LayerID>" );
        sb.append( "<wcs:Title>" + title + "</wcs:Title>" );
        sb.append( "<wcs:LatLonBoundingBox minx=\"" + llminx + "\" " );
        sb.append( "miny=\"" + llminy + "\" maxx=\"" + llmaxx + "\" maxy=\"" + llmaxy + "\"/>" );
        sb.append( "<wcs:SRS>" + crs + "</wcs:SRS>" );
        sb.append( "<wcs:GridExtentDescription>" );
        sb.append( "<wcs:SpatialExtent srsName=\"" + crs + "\">" );
        sb.append( "<wcs:XExtent>" );
        sb.append( "<wcs:min>" + minx + "</wcs:min>" );
        sb.append( "<wcs:max>" + maxx + "</wcs:max>" );
        sb.append( "</wcs:XExtent><wcs:YExtent>" );
        sb.append( "<wcs:min>" + miny + "</wcs:min>" );
        sb.append( "<wcs:max>" + maxy + "</wcs:max>" );
        sb.append( "</wcs:YExtent></wcs:SpatialExtent>" );
        sb.append( "<wcs:GridAxisDescription><wcs:GridAxis>" );
        sb.append( "<wcs:Name>X</wcs:Name>" );
        sb.append( "<wcs:orientation>right</wcs:orientation>" );
        sb.append( "</wcs:GridAxis><wcs:GridAxis>" );
        sb.append( "<wcs:Name>Y</wcs:Name>" );
        sb.append( "<wcs:orientation>up</wcs:orientation>" );
        sb.append( "</wcs:GridAxis></wcs:GridAxisDescription>" );
        sb.append( "<wcs:Grid dimension=\"2\" type=\"post\">" );
        sb.append( "<wcs:GridRange><wcs:low>" );
        sb.append( "<wcs:ordinate>0</wcs:ordinate>" );
        sb.append( "<wcs:ordinate>0</wcs:ordinate>" );
        sb.append( "</wcs:low><wcs:high>" );
        sb.append( "<wcs:ordinate>" + width + "</wcs:ordinate>" );
        sb.append( "<wcs:ordinate>" + height + "</wcs:ordinate>" );
        sb.append( "</wcs:high></wcs:GridRange></wcs:Grid>" );
        sb.append( "</wcs:GridExtentDescription>" );
        sb.append( "<wcs:RangeSetDescription>" );
        sb.append( "<wcs:GridRangeDescription>" );
        sb.append( "<wcs:RangeID>" + layerID + "</wcs:RangeID>" );
        sb.append( "<wcs:title>" + title + "</wcs:title>" );
        sb.append( "</wcs:GridRangeDescription>" );
        sb.append( "</wcs:RangeSetDescription></wcs:GridCoverageLayer>" );

        // parse new GridCoverage Layer as dom document and get the root element
        StringReader sr = new StringReader( sb.toString() );
        Document doc = XMLTools.parse( sr );
        Node root = doc.getDocumentElement();

        // insert the new GridCoverage layer into the existing CoverageLayerList
        XMLTools.insertNodeInto( root, node );
        
        logger.warn("Adding grid coverage layer finished");
    }

    /**
     *
     */
    private static void printHelp() {
        System.out.println( "ERROR: List of submitted parameters isn't complete." );
        System.out.println();
        System.out.println( "TileImageTree parameters: " );
        System.out.println( "-i: input file name (mandatory)" );
        System.out.println( "-o: output directory path name (mandatory)" );
        System.out.print( "-f: output format (gif, bmp, jpg, png, tif)" );
        System.out.println( " default = jpg; " );
        System.out.println( "    Consider that the target format must have the" );
        System.out.println( "    same or a higher color depht then the input format" );
        System.out.println( "-r comma sperated list of resolutions; e.g. 1.0,0.5,0.25" );
        System.out.println( "    The length of the list is equal to the number of levels" );
        System.out.println( "    that will be generated. The number of level determines the" );
        System.out.println( "    size of the generated tiles because for each level the tiles" );
        System.out.println( "    of the former level will be devided into four quarters." );
        System.out.println( "    The first level will have the first resolution, the second." );
        System.out.println( "    level the second one etc.." );
        System.out.println( "-s: index where the nameing of the tiles start" );
        System.out.println( "     (optional, default = 0)" );
        System.out.println( "-q: qualitiy of the produced tiles (just if output format = jpg)" );
        System.out.println( "     (optional, default = 1 (best))" );
        System.out.println( "-k: coordinate reference system of the map to be tiled" );
        System.out.println( "     (optional, default = EPSG:4326)" );
    }

    /**
     *
     *
     * @param map 
     *
     * @return 
     */
    private static boolean validate( HashMap map ) {
        boolean valid = true;

        if ( ( map.get( "-i" ) == null ) || ( map.get( "-o" ) == null ) || 
             ( map.get( "-r" ) == null ) || ( map.get( "-f" ) == null ) || 
             ( map.get( "-h" ) != null ) ) {
            valid = false;
        }

        return valid;
    }

    /**
     * @param args the command line arguments
     * @throws Exception 
     */
    public static void main( String[] args ) throws Exception {
        HashMap map = new HashMap();

        for ( int i = 0; i < args.length; i += 2 ) {
            map.put( args[i], args[i + 1] );
        }

        if ( !validate( map ) ) {
            printHelp();
            System.exit( 1 );
        }

        String inFile = (String)map.get( "-i" );
        String outDir = (String)map.get( "-o" );
        String format = ( (String)map.get( "-f" ) ).toUpperCase();

        double[] targetResolutions = null;

        try {
            StringTokenizer st = new StringTokenizer( (String)map.get( "-r" ), ",;" );
            targetResolutions = new double[st.countTokens()];

            for ( int i = 0; i < targetResolutions.length; i++ ) {
                double v = Double.parseDouble( st.nextToken() );
                targetResolutions[i] = v;
            }
        } catch ( Exception e ) {
            System.out.println( "Cant't parse target resolutions!" + e );
            printHelp();
            System.exit( 1 );
        }

        int startIndex = 0;

        try {
            startIndex = Integer.parseInt( (String)map.get( "-s" ) );
        } catch ( Exception ex ) {
        }

        float quality = 1.0f;

        try {
            quality = Float.parseFloat( (String)map.get( "-q" ) );

            if ( quality > 1 ) {
                quality = 1.0f;
            } else if ( quality < 0.1 ) {
                quality = 0.1f;
            }
        } catch ( Exception ex ) {
        }
        
        String crs = (String)map.get( "-k" );
        if ( crs == null ) {
            crs = "EPSG:4326";
        }

        try {
            TileImageTree tit = new TileImageTree( inFile, outDir, format, targetResolutions, 
                                                   startIndex, quality, crs );
            tit.createTileImageTree();
        } catch ( Exception e ) {
            System.out.println( e.getMessage() );
            e.printStackTrace();
            throw e;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    //                        inner classes                                  //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * default progress observer class. write the progress in % to the console
     */
    public class ProgressObserver {
        private int max = 0;

        /**
         * Creates a new ProgressObserver object.
         */
        public ProgressObserver() {
            for ( int i = 0; i < targetResolutions.length; i++ ) {
                max += (int)Math.pow( 4, ( i + 1 ) );
            }

            this.max = max;
        }

        /**
         *
         *
         * @param object 
         */
        public void write( Object object ) {
            double v = ( (Integer)object ).intValue();

            if ( ( v % 20 ) == 0 ) {
                v = (int)Math.round( v / (double)max * 10000d );
                System.out.println( ( v / 100d ) + "%" );
            }
        }
    }
}