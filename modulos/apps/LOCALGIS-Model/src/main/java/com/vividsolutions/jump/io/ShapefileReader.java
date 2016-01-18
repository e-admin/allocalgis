/**
 * ShapefileReader.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * ShapeReader.java
 *
 * Created on June 27, 2002, 2:49 PM
 */
/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */
package com.vividsolutions.jump.io;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import org.geotools.dbffile.DbfFile;
import org.geotools.shapefile.Shapefile;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;


/**
 * ShapefileReader is a {@link JUMPReader} specialized to read Shapefiles.
 *
 * <p>
 *   DataProperties for the JUMPReader load(DataProperties) interface:<br><br>
 * </p>
 *
 * <p>
 *  <table border='1' cellspacing='0' cellpadding='4'>
 *    <tr>
 *      <th>Parameter</th><th>Meaning</th>
 *    </tr>
 *    <tr>
 *      <td>InputFile or DefaultValue</td>
 *      <td>File name for the input .shp file</td>
 *    </tr>
 *    <tr>
 *      <td colspan='2'>
 *         NOTE: The input .dbf is assumed to be 'beside' (in the same
 *         directory) as the .shp file.
 *      </td>
 *    </tr>

 *    <tr>
 *      <td>CompressedFileTemplate</td>
 *      <td>File name (.zip NOT a .gz) with a .shp and .dbf file inside</td>
 *    </tr>
 *
 *    <tr>
 *      <td colspan='2'>
 *         Uses a modified version of geotools to do the .dbf and .shp
 *         file reading.  If you are reading from a .zip file, the dbf
 *         file will be copied to your temp directory and deleted
 *         after being read.
 *      </td>
 *    </tr>
 *  </table>

 */
public class ShapefileReader implements JUMPReader {
    File delete_this_tmp_dbf = null;
	private HashMap filters;

    /** Creates new ShapeReader */
    public ShapefileReader() {
    }

    /**
     * Main method to read a shapefile.  Most of the work is done in the org.geotools.* package.
     *
     *@param dp 'InputFile' or 'DefaultValue' to specify output .shp file.
     *
     */
    public FeatureCollection read(DriverProperties dp)
        throws IllegalParametersException, Exception {
        FeatureCollection result;
        String dbfFileName;
        String shpfileName;
        String path;
        String fname;
        String fname_withoutextention;
        int loc;
        FeatureSchema fs;

        shpfileName = dp.getProperty("File");

        if (shpfileName == null) {
            shpfileName = dp.getProperty("DefaultValue");
        }

        if (shpfileName == null) {
            throw new IllegalParametersException("no File property specified");
        }

        // System.out.println("separator:" + File.separatorChar);
        loc = shpfileName.lastIndexOf(File.separatorChar);

        /*
        if (loc == -1)
        {
              // loc = 0; // no path - ie. "hills.shp"
              // path = "";
              // fname = shpfileName;

              //probably using the wrong path separator character.

              throw new Exception("couldn't find the path separator character '"+ File.separatorChar+"' in your shape file name. This you're probably using the unix (or dos) one.");
        }
        else
        {
         **/
        path = shpfileName.substring(0, loc + 1); // ie. "/data1/hills.shp" -> "/data1/"
        fname = shpfileName.substring(loc + 1); // ie. "/data1/hills.shp" -> "hills.shp"

        loc = fname.lastIndexOf(".");

        if (loc == -1) {
            throw new IllegalParametersException("Filename must end in '.shp'");
        }

        fname_withoutextention = fname.substring(0, loc); // ie. "hills.shp" -> "hills."
        dbfFileName = path + fname_withoutextention + ".dbf";
        File dbf =new File(dbfFileName);
        if (!dbf.exists())
        	dbfFileName = path + fname_withoutextention + ".DBF";


        //okay, have .shp and .dbf file, lets start
        // install Shapefile and DbfFile
        Shapefile myshape = getShapefile(shpfileName,
                dp.getProperty("CompressedFile"));
        DbfFile mydbf = getDbfFile(dbfFileName, dp.getProperty("CompressedFile"));

        GeometryFactory factory = new GeometryFactory();
        GeometryCollection collection = myshape.read(factory);

        fs = new FeatureSchema();

        int numfields = mydbf.getNumFields();

        // fill in schema
        fs.addAttribute("GEOMETRY", AttributeType.GEOMETRY);

        for (int j = 0; j < numfields; j++) {
            fs.addAttribute(mydbf.getFieldName(j), AttributeType.toAttributeType(mydbf.getFieldType(j)));
        }

        result = new FeatureDataset(fs);

        for (int x = 0; x < mydbf.getLastRec(); x++) {
            Feature feature = new BasicFeature(fs);
            Geometry geo = collection.getGeometryN(x);

            //System.out.println(geo.toString());
            StringBuffer s = mydbf.GetDbfRec(x);

            // Vector info = mydbf.ParseRecord( s );
            boolean cumpleFiltro=true;
            for (int y = 0; y < numfields; y++) {
                //feature.setAttribute(y+1, info.get(y) );
                
            	
            	if (mydbf.ParseRecordColumn(s, y) instanceof String){
            		if (!cumpleFiltro(mydbf.getFieldName(y),(String)mydbf.ParseRecordColumn(s, y))){
            			cumpleFiltro=false;
            			break;
            		}
            		feature.setAttribute(y + 1, mydbf.ParseRecordColumn(s, y).toString().trim());
            	}
            	else{
            		if (!cumpleFiltro(mydbf.getFieldName(y),String.valueOf(mydbf.ParseRecordColumn(s, y)))){
            			cumpleFiltro=false;
            		}           		
            		feature.setAttribute(y + 1, mydbf.ParseRecordColumn(s, y));
            	}

                // System.out.println(  mydbf.getFieldName(y)+"="+ info.get(y).toString() );
            }

            feature.setGeometry(geo);

            //System.out.println(s.toString() );
            if (cumpleFiltro)
            	result.add(feature);

            // if ((x % 10000)==0)
            //      System.gc();        // free up memory!
        }

        System.gc();
        mydbf.close(); // close DBF file!
        deleteTmpDbf(); // delete dbf file if it was decompressed

        return result; // Coleccion de Features
    }
    
    /**
     * Los filtros son de este tipo CODIGO_POT=(<>;"0")
     * Que significa que solo se pillan aquellos elementos cuyo CODIGO_POT sea distinto de 0
     * @param campo
     * @param valor
     * @return
     */
    private boolean cumpleFiltro(String campo,String valor){
    	
    	try {
			if (filters!=null){
				String valorFiltro=(String)filters.get(campo);
				if (valorFiltro!=null){
					String[] operadores=valorFiltro.split(";");
					if (operadores[0].equals("<>")){
						if (operadores[1].equals(valor.trim()))
							return false;
					}
					else if (operadores[0].equals("==")){
						if (operadores[1].equals(valor))
							return true;
					}
					return true;
				}
				else
					return true;
			}
			return true;
		} catch (Exception e) {
			return true;
		}
    }
    
    
    public void setFilters(HashMap filters){
    	this.filters=filters;
    }

    protected Shapefile getShapefile(String shpfileName, String compressedFname)
        throws Exception {
        java.io.InputStream in = CompressedFile.openFile(shpfileName,
                compressedFname);

        Shapefile myshape = new Shapefile(in);

        return myshape;
    }

    protected DbfFile getDbfFile(String dbfFileName, String compressedFname)
        throws Exception {
        byte[] b = new byte[16000];
        int len;
        boolean keep_going = true;

        if ((compressedFname != null) && (compressedFname.length() > 0)) {
            // copy the file then use that copy
            File file = java.io.File.createTempFile("dbf", ".dbf");
            FileOutputStream out = new FileOutputStream(file);

            java.io.InputStream in = CompressedFile.openFile(dbfFileName,
                    compressedFname);

            while (keep_going) {
                len = in.read(b);

                if (len > 0) {
                    out.write(b, 0, len);
                }

                keep_going = (len != -1);
            }

            in.close();
            out.close();

            DbfFile mydbf = new DbfFile(file.toString());
            delete_this_tmp_dbf = file; // to be deleted later on

            return mydbf;
        } else {
            DbfFile mydbf = new DbfFile(dbfFileName);

            return mydbf;
        }
    }

    private void deleteTmpDbf() {
        if (delete_this_tmp_dbf != null) {
            delete_this_tmp_dbf.delete();
            delete_this_tmp_dbf = null;
        }
    }
}
