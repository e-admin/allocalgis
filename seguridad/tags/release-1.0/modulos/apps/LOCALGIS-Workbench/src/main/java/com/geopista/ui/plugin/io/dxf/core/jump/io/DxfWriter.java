package com.geopista.ui.plugin.io.dxf.core.jump.io;

import com.vividsolutions.jump.io.IllegalParametersException;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.java2xml.Java2XML;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jts.geom.*;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.BufferedReader;
import java.util.Iterator;
import java.util.List;

public class DxfWriter {

	public static final int GRP_START = 0;
	public static final int GRP_VALUE = 1;
	public static final int GRP_NAME = 2;
	public static final int GRP_VARIABLE = 9;
	public static final int GRP_INTEGER = 70;
	public static final int GRP_EXTENDED_APPID = 1001;
	public static final int GRP_EXTENDED_STRING = 1000;
	
	public static final int GPR_NUMBER = 4;
	public static final int GPR_NUMBER2 = 62;
	public static final int GPR_NUMBER3 = 6;

	public static final String STR_SECTION = "SECTION";
	public static final String STR_ENDSEC = "ENDSEC";
	public static final String STR_TABLE = "TABLE";
	public static final String STR_ENDTAB = "ENDTAB";
	public static final String STR_APPID = "APPID";
	public static final String STR_EOF = "EOF";


	public static final String SEC_HEADER = "HEADER";
	public static final String SEC_TABLES = "TABLES";
	public static final String SEC_ENTITIES = "ENTITIES";
	public static final String SEC_BLOCKS = "BLOCKS";

	public static final String VAR_ACADVER = "$ACADVER";

	public static final String TAB_APPID = "APPID";
	public static final String TAB_LAYER = "LAYER";
	public static final String TAB_CONTINUOUS = "CONTINUOUS";

	public static final String APP_GEOPISTA = "GEOPISTA";
    public static final String APP_GEOPISTA_SCHEMA = "GEOPISTA_SCHEMA";
    public static final String APP_GEOPISTA_LAYER = "GEOPISTA_LAYER";

	public static final String VAL_ACADVER = "AC1009";

    /** Evita que varias features de la misma capa que no tienen SystemId Inicializado y que en el
     * fichero dxf aparecerian como (nombreCapa.NoInicilaizado),
     * aparezcan en el dxf como (nombreCapa.NoInicilaizado+contFeatureNoInicializada) y asi
     * evitar que se machaquen. */
    private int contFeatureSinSystemID= 0;

    


	public void write(List lLayers, String sFilename) throws IllegalParametersException, Exception {
		PrintWriter out = new PrintWriter(new FileWriter(sFilename));
		writeHeaderSection(out);
		writeTablesSection(out);
		writeEntitiesSection(out, lLayers);
		writeGroup(out, GRP_START, STR_EOF);
		out.flush();
		out.close();
	}
	
	public void writeAutoCAD(List lLayers, String sFilename) throws IllegalParametersException, Exception {
		PrintWriter out = new PrintWriter(new FileWriter(sFilename));
		writeHeaderSection(out);
		writeTablesSectionAutoCAD(out, lLayers);
		writeEntitiesSectionAutoCAD(out, lLayers);
		writeGroup(out, GRP_START, STR_EOF);
		out.flush();
		out.close();
	}

	private void writeGroup(PrintWriter out, int iGroup, String sText) {
		String sCode = String.valueOf(iGroup);
		int iSpaces = sCode.length() > 2 ? 0 : 3 - sCode.length(); // FORTRAN I3
		for (int i = 0; i < iSpaces; i++)
			out.print(" ");
		out.println(iGroup);
		out.println(sText);
	}

	private void writeInt(PrintWriter out, int iGroup, int iValue) {
		String sValue = String.valueOf(iValue);
		int iSpaces = sValue.length() > 2 ? 0 : 6 - sValue.length();
		StringBuffer sbValue = new StringBuffer();
		for (int i = 0; i < iSpaces; i++)
			sbValue.append(" ");
		sbValue.append(sValue);
		writeGroup(out, iGroup, sbValue.toString());
	}

	private void startSection(PrintWriter out, String sName) {
		writeGroup(out, GRP_START, STR_SECTION);
		writeGroup(out, GRP_NAME, sName);
	}


	private void endSection(PrintWriter out) {
		writeGroup(out, GRP_START, STR_ENDSEC);
	}


	private void writeHeaderSection(PrintWriter out) {
		startSection(out, SEC_HEADER);
		writeGroup(out, GRP_VARIABLE, VAR_ACADVER);
		writeGroup(out, GRP_VALUE, VAL_ACADVER);
		endSection(out);
	}

	private void writeTablesSection(PrintWriter out) {
		startSection(out, SEC_TABLES);
		writeAppIDTable(out);
		endSection(out);
	}
	
	private void writeTablesSectionAutoCAD(PrintWriter out, List lLayers) {
		startSection(out, SEC_TABLES);
		writeAppIDTable(out);
		writeLayers(out,lLayers);
		endSection(out);
	}


	private void startTable(PrintWriter out, String sName) {
		writeGroup(out, GRP_START, STR_TABLE);
		writeGroup(out, GRP_NAME, sName);
	}

	private void endTable(PrintWriter out) {
		writeGroup(out, GRP_START, STR_ENDTAB);
	}

	private void writeAppIDTable(PrintWriter out) {
		startTable(out, TAB_APPID);
		writeInt(out, GRP_INTEGER, 1);
		writeGroup(out, GRP_START, STR_APPID);
		writeGroup(out, GRP_NAME, APP_GEOPISTA);
		writeInt(out, GRP_INTEGER, 0);
        writeGroup(out, GRP_START, STR_APPID);
		writeGroup(out, GRP_NAME, APP_GEOPISTA_SCHEMA);
		writeInt(out, GRP_INTEGER, 0);
        writeGroup(out, GRP_START, STR_APPID);
		writeGroup(out, GRP_NAME, APP_GEOPISTA_LAYER);
		writeInt(out, GRP_INTEGER, 0);
		endTable(out);
	}
	
	private void writeLayers(PrintWriter out, List lLayers){
		startTable(out, TAB_LAYER);
		writeInt(out, GRP_INTEGER, 4);
		writeGroup(out, GRP_START, TAB_LAYER);
		writeGroup(out, GRP_NAME, "0");
		writeInt(out, GRP_INTEGER, 0);
		writeInt(out, GPR_NUMBER2, 7);
		writeGroup(out, GPR_NUMBER3, TAB_CONTINUOUS);
		for (Iterator iterLayers = lLayers.iterator();iterLayers.hasNext();){
			Layer layer =(Layer)iterLayers.next();
			writeLayer(out, layer);			
		}
		endTable(out);
	}

	private void writePoint(PrintWriter out, Geometry geom, Feature f, int iGeomNum,String sLayer) {
		writeGroup(out, GRP_START, "POINT");
		writeGroup(out, 8, sLayer);
		Coordinate[] aCoords = geom.getCoordinates();
		writeGroup(out, 10, String.valueOf(aCoords[0].x));
		writeGroup(out, 20, String.valueOf(aCoords[0].y));
		writeGroup(out, 30, "0.0");
		writeExtendedEntityData(out,  f,iGeomNum,0,sLayer);
	}

	private void writeLineString(PrintWriter out, Geometry geom, Feature f, int iGeomNum,String sLayer) {
		writeGroup(out, GRP_START, "POLYLINE");
		writeGroup(out, 8, sLayer);
		writeInt(out, 66, 1); //vertex follow
		writeGroup(out, 10, "0.0");
		writeGroup(out, 20, "0.0");
		writeGroup(out, 30, "0.0");
		writeExtendedEntityData(out,f,iGeomNum,0,sLayer);
		Coordinate[] aCoords = geom.getCoordinates();
		for (int i = 0; i < aCoords.length; i++) {
			writeGroup(out, 0, "VERTEX");
			writeGroup(out, 8, sLayer);
			writeGroup(out, 10, String.valueOf(aCoords[i].x));
			writeGroup(out, 20, String.valueOf(aCoords[i].y));
			writeGroup(out, 30, "0.0");
		}
		writeGroup(out, GRP_START, "SEQEND");
	}


    private void writePolygon(PrintWriter out, Geometry geom, Feature f, int iGeomNum,String sLayer){
        Polygon polygon=(Polygon)geom;
        writeLinearRing(out,polygon.getExteriorRing(),f,iGeomNum,0,sLayer);
        for (int i=0;i<polygon.getNumInteriorRing();i++)
            writeLinearRing(out,polygon.getInteriorRingN(i),f,iGeomNum,i+1,sLayer);
    }

    private void writeLinearRing(PrintWriter out, Geometry geom, Feature f, int iGeomNum, int iRingNum,String sLayer) {
        writeGroup(out, GRP_START, "POLYLINE");
		writeGroup(out, 8, sLayer);
		writeInt(out, 70, 1); //closed polyline
		writeInt(out, 66, 1); //vertex follow
		writeGroup(out, 10, "0.0");
		writeGroup(out, 20, "0.0");
		writeGroup(out, 30, "0.0");
		writeExtendedEntityData(out, f, iGeomNum,iRingNum,sLayer);
		Coordinate[] aCoords = geom.getCoordinates();
		for (int i = 0; i < aCoords.length - 1; i++) {
			writeGroup(out, 0, "VERTEX");
			writeGroup(out, 8, sLayer);
			writeGroup(out, 10, String.valueOf(aCoords[i].x));
			writeGroup(out, 20, String.valueOf(aCoords[i].y));
			writeGroup(out, 30, "0.0");
		}
		writeGroup(out, GRP_START, "SEQEND");
	}

	private void writeEntity(PrintWriter out, Geometry geom, Feature feature, int iGeomNum,String sLayer) {
		String s = geom.getGeometryType();
		
		if (geom.getCoordinates().length > 0){		

			if (s.equals("LineString")) {
				writeLineString(out, geom, feature, iGeomNum,sLayer);
			} else if (s.equals("Point")) {
				writePoint(out, (Point) geom, feature, iGeomNum,sLayer);
			} else if (s.equals("Polygon")) {
				writePolygon(out, (Polygon) geom, feature, iGeomNum,sLayer);
			}
		}
	}



	private void writeEntitiesSection(PrintWriter out, List lLayers) {
        startSection(out, SEC_ENTITIES);
        for (Iterator itLayers=lLayers.iterator();itLayers.hasNext();){
            GeopistaLayer layer=(GeopistaLayer)itLayers.next();
            FeatureCollection featureCollection=layer.getFeatureCollectionWrapper().getWrappee();
            /** NOTA: esta linea ya estaba comentada
            //writeLayer(out,layer);
            */

            /* NOTA: SHP2DXF al guardar el fichero como dxf excepcion al realizar el casting a GeopistaSchema,
                     ya que recibimos un FeatureSchema. Incidencia [0000080]
            */
            /*
            writeSchema(out,(GeopistaSchema)featureCollection.getFeatureSchema(),layer.getSystemId());
            for (Iterator it = featureCollection.getFeatures().iterator(); it.hasNext();) {
                Feature f = (Feature) it.next();
                if (!(f.getGeometry() instanceof GeometryCollection)) {
                    writeEntity(out, f.getGeometry(), f,0,layer.getSystemId());
                } else {
                    GeometryCollection gc = (GeometryCollection) f.getGeometry();
                    for (int i = 0; i < gc.getNumGeometries(); i++) {
                        writeEntity(out, gc.getGeometryN(i), f, i,layer.getSystemId());
                    }
                }
            }
            */
            GeopistaSchema geopistaSchema= new GeopistaSchema();

            if (featureCollection.getFeatureSchema() instanceof GeopistaSchema){
               geopistaSchema= (GeopistaSchema)featureCollection.getFeatureSchema();
            }else{
                /** Leemos el schema de la primera feature. */
                if (featureCollection.getFeatures().size() > 0){
                   GeopistaFeature gf = GeopistaSchema.vampiriceSchema((Feature)featureCollection.getFeatures().get(0));
                   geopistaSchema= (GeopistaSchema)gf.getSchema();
                }
            }
            writeSchema(out,geopistaSchema,layer.getSystemId());
            for (Iterator it = featureCollection.getFeatures().iterator(); it.hasNext();) {
                Feature f = (Feature) it.next();
                if (!(f.getGeometry() instanceof GeometryCollection)) {
                    writeEntity(out, f.getGeometry(), f,0,layer.getSystemId());
                } else {
                    GeometryCollection gc = (GeometryCollection) f.getGeometry();
                    for (int i = 0; i < gc.getNumGeometries(); i++) {
                        writeEntity(out, gc.getGeometryN(i), f, i,layer.getSystemId());
                    }
                }
            }

        }
		endSection(out);
	}
	
	private void writeEntitiesSectionAutoCAD(PrintWriter out, List lLayers) {
        startSection(out, SEC_ENTITIES);
        for (Iterator itLayers=lLayers.iterator();itLayers.hasNext();){
            GeopistaLayer layer=(GeopistaLayer)itLayers.next();
            FeatureCollection featureCollection=layer.getFeatureCollectionWrapper().getWrappee();
            /** NOTA: esta linea ya estaba comentada
            //writeLayer(out,layer);
            */

            //writeLayer(out,layer);
            /* NOTA: SHP2DXF al guardar el fichero como dxf excepcion al realizar el casting a GeopistaSchema,
                     ya que recibimos un FeatureSchema. Incidencia [0000080]
            */
            /*
            writeSchema(out,(GeopistaSchema)featureCollection.getFeatureSchema(),layer.getSystemId());
            for (Iterator it = featureCollection.getFeatures().iterator(); it.hasNext();) {
                Feature f = (Feature) it.next();
                if (!(f.getGeometry() instanceof GeometryCollection)) {
                    writeEntity(out, f.getGeometry(), f,0,layer.getSystemId());
                } else {
                    GeometryCollection gc = (GeometryCollection) f.getGeometry();
                    for (int i = 0; i < gc.getNumGeometries(); i++) {
                        writeEntity(out, gc.getGeometryN(i), f, i,layer.getSystemId());
                    }
                }
            }
            */
//            GeopistaSchema geopistaSchema= new GeopistaSchema();
//
//            if (featureCollection.getFeatureSchema() instanceof GeopistaSchema){
//               geopistaSchema= (GeopistaSchema)featureCollection.getFeatureSchema();
//            }else{
//                /** Leemos el schema de la primera feature. */
//                if (featureCollection.getFeatures().size() > 0){
//                   GeopistaFeature gf = GeopistaSchema.vampiriceSchema((Feature)featureCollection.getFeatures().get(0));
//                   geopistaSchema= (GeopistaSchema)gf.getSchema();
//                }
//            }
//            writeSchema(out,geopistaSchema,layer.getSystemId());
            for (Iterator it = featureCollection.getFeatures().iterator(); it.hasNext();) {
                Feature f = (Feature) it.next();
                if (!(f.getGeometry() instanceof GeometryCollection)) {
                    writeEntity(out, f.getGeometry(), f,0,layer.getSystemId());
                } else {
                    GeometryCollection gc = (GeometryCollection) f.getGeometry();
                    for (int i = 0; i < gc.getNumGeometries(); i++) {
                        writeEntity(out, gc.getGeometryN(i), f, i,layer.getSystemId());
                    }
                }
            }

        }
		endSection(out);
	}

    /** Escribe el esquema (XML) en el XDATA de un punto con
     * APPID=GEOPISTA_SCHEMA. */
    private void writeSchema(PrintWriter out,GeopistaSchema schema,String sLayer){    
        try{
            String sXml=new Java2XML().write(schema,APP_GEOPISTA_SCHEMA);
            writeGroup(out, GRP_START, "POINT");
            writeGroup(out, 8, sLayer);
            writeGroup(out, 10, "0.0");
            writeGroup(out, 20, "0.0");
            writeGroup(out, 30, "0.0");
            writeGroup(out, GRP_EXTENDED_APPID, APP_GEOPISTA_SCHEMA);
            writeGroup(out, 1002, "{");
            BufferedReader brXml=new BufferedReader(new StringReader(sXml));
            String sLine=null;
            while((sLine=brXml.readLine())!=null){
                int iIndex=0;
                while (iIndex<sLine.length()){
                    int iNext=iIndex+250;
                    if (iNext>sLine.length())
                        iNext=sLine.length();
                    String sSubstring=sLine.substring(iIndex,iNext);
                    writeGroup(out,1000,"$"+sSubstring+"$");
                    iIndex=iNext;
                }
            }
            writeGroup(out, 1002, "}");
        }catch(Exception e){
            e.printStackTrace();
            writeGroup(out,999,"Schema Error: "+e.getMessage());
        }
    }

    /** Escribe el layer (XML) en el XDATA de un punto con
    private void writeLayer(PrintWriter out,GeopistaLayer layer){
        try{
            String sXml=new Java2XML().write(layer,APP_GEOPISTA_LAYER);
            writeGroup(out, GRP_START, "POINT");
            writeGroup(out, 8, layer.getSystemId());
            writeGroup(out, 10, "0.0");
            writeGroup(out, 20, "0.0");
            writeGroup(out, 30, "0.0");
            writeGroup(out, GRP_EXTENDED_APPID, APP_GEOPISTA_LAYER);
            writeGroup(out, 1002, "{");
            BufferedReader brXml=new BufferedReader(new StringReader(sXml));
            String sLine=null;
            while((sLine=brXml.readLine())!=null){
                int iIndex=0;
                while (iIndex<sLine.length()){
                    int iNext=iIndex+250;
                    if (iNext>sLine.length())
                        iNext=sLine.length();
                    String sSubstring=sLine.substring(iIndex,iNext);
                    writeGroup(out,1000,"$"+sSubstring+"$");
                    iIndex=iNext;
                }
            }
            writeGroup(out, 1002, "}");
        }catch(Exception e){
            e.printStackTrace();
            writeGroup(out,999,"Layer Error: "+e.getMessage());
        }
    }*/

    private void writeLayer(PrintWriter out,Layer layer){
    	writeGroup(out, GRP_START, TAB_LAYER);
    	if(layer instanceof GeopistaLayer){
    		writeGroup(out, GRP_NAME, ((GeopistaLayer)layer).getSystemId());
    	}
    	else{
    		writeGroup(out, GRP_NAME, layer.getName());
    	}
    	writeInt(out, GRP_INTEGER, 0);
    	writeInt(out, GPR_NUMBER2, 7);
    	writeGroup(out, GPR_NUMBER3, TAB_CONTINUOUS);
    }


	private void writeExtendedEntityData(PrintWriter out, Feature f,int iGeomNum,int iRingNum,String sLayer){
		writeGroup(out, GRP_EXTENDED_APPID, APP_GEOPISTA);
		writeGroup(out, 1002, "{");
        //writeGroup(out, 1000, sLayer+"."+((GeopistaFeature) f).getSystemId());
        if (!((GeopistaFeature) f).isTempID()){
            writeGroup(out, 1000, sLayer+"."+((GeopistaFeature) f).getSystemId());
        }else{
		    writeGroup(out, 1000, sLayer+"."+((GeopistaFeature) f).getSystemId()+contFeatureSinSystemID);
            contFeatureSinSystemID++;
        }

		writeGroup(out, 1000, (f.getGeometry().getClass().getName()));
        writeGroup(out, 1000, String.valueOf(iGeomNum));
        writeGroup(out, 1000, String.valueOf(iRingNum));
		Object[] oAtts = f.getAttributes();
		for (int i = 0; i < oAtts.length; i++) {
			writeGroup(out, 1002, "{");
			writeGroup(out, 1000, String.valueOf(i));
			String sType = oAtts[i] == null ? null : oAtts[i].getClass().getName();
			String sValue = oAtts[i] == null ? null
					: oAtts[i] instanceof Geometry ? null : oAtts[i].toString();
			writeGroup(out, 1000, sType);
			writeGroup(out, 1000, sValue);
			writeGroup(out, 1002, "}");
		}
		writeGroup(out, 1002, "}");
	}
}
