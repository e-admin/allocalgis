package com.geopista.app.catastro.intercambio.edicion;

import com.vividsolutions.jump.io.IllegalParametersException;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jts.geom.*;
import com.geopista.model.GeopistaLayer;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.GeopistaFeature;

import java.util.List;
import java.util.Iterator;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 09-jul-2007
 * Time: 12:14:48
 * To change this template use File | Settings | File Templates.
 */
public class DxfWriterCatastro
{


	public static final int GRP_START = 0;
	public static final int GRP_VALUE = 1;
	public static final int GRP_NAME = 2;
	public static final int GRP_VARIABLE = 9;
	public static final int GRP_INTEGER = 70;
	public static final int GRP_EXTENDED_APPID = 1001;
	public static final int GRP_EXTENDED_STRING = 1000;
	public static final int GRP_END = 8;

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

	public static final String APP_GEOPISTA = "GEOPISTA";
    public static final String APP_GEOPISTA_SCHEMA = "GEOPISTA_SCHEMA";
    public static final String APP_GEOPISTA_LAYER = "GEOPISTA_LAYER";

	public static final String VAL_ACADVER = "AC1009";

    /** Evita que varias features de la misma capa que no tienen SystemId Inicializado y que en el
     * fichero dxf aparecerian como (nombreCapa.NoInicilaizado),
     * aparezcan en el dxf como (nombreCapa.NoInicilaizado+contFeatureNoInicializada) y asi
     * evitar que se machaquen. */
    private int contFeatureSinSystemID= 0;




	public String write(List lLayers) throws IllegalParametersException, Exception {
        StringWriter strWriter = new StringWriter();
        PrintWriter out = new PrintWriter(strWriter);
//		writeHeaderSection(out);
//		writeTablesSection(out);
		writeEntitiesSection(out, lLayers);
		writeGroup(out, GRP_START, STR_EOF);
        return strWriter.toString();
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
//		writeGroup(out, GRP_VARIABLE, VAR_ACADVER);
//		writeGroup(out, GRP_VALUE, VAL_ACADVER);
		endSection(out);
	}

	private void writeTablesSection(PrintWriter out) {
		startSection(out, SEC_TABLES);
		writeAppIDTable(out);
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

	private void writePoint(PrintWriter out, Geometry geom, Feature f, int iGeomNum,String sLayer) {
		writeGroup(out, GRP_START, "POINT");
		writeGroup(out, 8, sLayer);
		Coordinate[] aCoords = geom.getCoordinates();
		writeGroup(out, 10, String.valueOf(aCoords[0].x));
		writeGroup(out, 20, String.valueOf(aCoords[0].y));
		writeGroup(out, 30, "0.0");
		//writeExtendedEntityData(out,  f,iGeomNum,0,sLayer);
	}

	private void writeLineString(PrintWriter out, Geometry geom, Feature f, int iGeomNum,String sLayer) {
		writeGroup(out, GRP_START, "POLYLINE");
		writeGroup(out, 8, sLayer);
		writeInt(out, 66, 1); //vertex follow
		writeGroup(out, 10, "0.0");
		writeGroup(out, 20, "0.0");
		writeGroup(out, 30, "0.0");
		//writeExtendedEntityData(out,f,iGeomNum,0,sLayer);
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
//		writeInt(out, 70, 1); //closed polyline
		writeInt(out, 66, 1); //vertex follow
//		writeGroup(out, 10, "0.0");
//		writeGroup(out, 20, "0.0");
//		writeGroup(out, 30, "0.0");
		//writeExtendedEntityData(out, f, iGeomNum,iRingNum,sLayer);
		Coordinate[] aCoords = geom.getCoordinates();
		for (int i = 0; i < aCoords.length - 1; i++) {
			writeGroup(out, 0, "VERTEX");
			writeGroup(out, 8, sLayer);
			writeGroup(out, 10, String.valueOf(aCoords[i].x));
			writeGroup(out, 20, String.valueOf(aCoords[i].y));
			writeGroup(out, 30, "0.0");
		}
		if (aCoords.length>0){
			writeGroup(out, 0, "VERTEX");
			writeGroup(out, 8, sLayer);
			writeGroup(out, 10, String.valueOf(aCoords[0].x));
			writeGroup(out, 20, String.valueOf(aCoords[0].y));
			writeGroup(out, 30, "0.0");
		}
		writeGroup(out, GRP_START, "SEQEND");
	}

	private void writeEntity(PrintWriter out, Geometry geom, Feature feature, int iGeomNum,String sLayer) {
		
		String s = geom.getGeometryType();

		if (geom != null && !isReferenceGeom(geom)){

			if (s.equals("LineString")) {
				writeLineString(out, geom, feature, iGeomNum,sLayer);
			} else if (s.equals("Point"))
			{
				if(isTextLayer(feature))
				{
					writeText(out, sLayer, geom, feature);
				}
				else
				{
					writePoint(out, (Point) geom, feature, iGeomNum,sLayer);
				}

			} else if (s.equals("Polygon")) {
				writePolygon(out, (Polygon) geom, feature, iGeomNum,sLayer);
			}
		}
	}
	
	private boolean isReferenceGeom(Geometry geom){
		
		if (geom instanceof Point){
			Point point = (Point)geom;
			if (point.getCoordinate().x == 0 && point.getCoordinate().y == 0){
				return true;
			}
		}
		else if (geom instanceof LineString){
			LineString line = (LineString)geom;
			int numPoints = line.getCoordinateSequence().toCoordinateArray().length;
			Coordinate[] coordinates = line.getCoordinateSequence().toCoordinateArray();
			for (int i = 0; i < numPoints; i++){
				Coordinate coord = coordinates[i];
				if (coord.x != 0 || coord.y != 0){
					return false;
				}
			}
			return true;
			
		}		
		
		return false;
	}

	private boolean isTextLayer(Feature feature){
		
		int numAttributes = feature.getSchema().getAttributeCount();
		for (int count = 0; count < numAttributes; count++){
			String nameAttribute = feature.getSchema().getAttributeName(count);
			if (nameAttribute.equals("TEXTO")){
				return true;
			}
		}
		return false;
		
	}

	private void writeEntitiesSection(PrintWriter out, List lLayers) {
        startSection(out, SEC_ENTITIES);
        for (Iterator itLayers=lLayers.iterator();itLayers.hasNext();){
            GeopistaLayer layer=(GeopistaLayer)itLayers.next();
            FeatureCollection featureCollection=layer.getFeatureCollectionWrapper().getWrappee();
            
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
        writeGroup(out, GRP_END, new Integer(GRP_START).toString());
		endSection(out);
	}

    /** Escribe el esquema (XML) en el XDATA de un punto con
     * APPID=GEOPISTA_SCHEMA. */
    /*private void writeSchema(PrintWriter out,GeopistaSchema schema,String sLayer){
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
    }*/

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

    private void writeText(PrintWriter out, String layerName, Geometry geom, Feature feature)
    {
		writeGroup(out, 0, "TEXT");
	    writeGroup(out, 8, layerName);
        //TODO poner los punto originales
        writeGroup(out, 10, String.valueOf(geom.getCoordinate().x));
		writeGroup(out, 20, String.valueOf(geom.getCoordinate().y));

        String alturaTexto = "0.0";
        if(feature.getAttribute("ALTURA_TEXTO") != null && !feature.getAttribute("ALTURA_TEXTO").equals(""))
           alturaTexto = String.valueOf((Double)feature.getAttribute("ALTURA_TEXTO"));

        String angulo = "0.0";
        if(feature.getAttribute("ANGULO") != null && !feature.getAttribute("ANGULO").equals(""))
            angulo = String.valueOf((Double)feature.getAttribute("ANGULO"));

        String texto = "";
        if(feature.getAttribute("TEXTO") != null)
            texto = (String)feature.getAttribute("TEXTO");

        writeGroup(out, 40, alturaTexto);
        writeGroup(out, 50, angulo);
        writeGroup(out, 1, texto);
        writeGroup(out, 72,"1"); //TODO justificacion horizontal.
        writeGroup(out, 73,"3"); //TODO justificacion vertical.
        writeGroup(out, 11, String.valueOf(geom.getCoordinate().x)); //TODO puntos de alineacion, suelen ser los mismo que las coordenadas. OPCIONALES
        writeGroup(out, 21, String.valueOf(geom.getCoordinate().y)); //TODO puntos de alineacion, suelen ser los mismo que las coordenadas. OPCIONALES
    }
}
