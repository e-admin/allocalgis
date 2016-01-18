package com.geopista.ui.plugin.io.dxf.core.jump.io;


import com.geopista.ui.plugin.io.dxf.math.Point3D;
import com.geopista.ui.plugin.io.dxf.reader.*;

import java.util.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import com.vividsolutions.jump.feature.*;
import com.vividsolutions.jts.geom.*;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.Attribute;
import com.geopista.feature.GeopistaFeature;


public class GeopistaDxfConverter extends FeatureCollectionDxfConverter implements DxfConverter{

    Hashtable htAttributes=new Hashtable();
    Hashtable htAttClasses=new Hashtable();
    Hashtable htGeometry=new Hashtable();
    Hashtable htGeomTypes=new Hashtable();
    Hashtable htLayers=new Hashtable();
    private Hashtable htSchemas=new Hashtable();

    GeometryFactory geometryFactory=null;

    public GeopistaDxfConverter(){
        geometryFactory=new GeometryFactory();
    }

    public void convert(String dxfPath) {
		try {
			DxfFile dxfFile = new DxfFile(dxfPath);
			convert(dxfFile);
		} catch (DxfException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public Enumeration getLayers(){
        return htSchemas.keys();
    }
    
    public void setLayer(String key, Object value){
        htSchemas.put(key, value);
        
     }
    public Hashtable getSchemas(){
       return htSchemas;
    }

    public FeatureCollection getFeatureCollection(String sLayer){
        GeopistaSchema schema=(GeopistaSchema)htSchemas.get(sLayer);
        FeatureCollection fcRet=new FeatureDataset(schema);
        for(Enumeration eKeys=htGeometry.keys();eKeys.hasMoreElements();){
            try{
                String sKey=(String)eKeys.nextElement();
                /* NOTA: Relacionado con  Incidencia[0000080]
                Al cargar un fichero shp y guardar el fichero como Geopistadxf, el idLayer
                es el path del fichero (ID_LAYER=path\nomFichero.shp). En un fichero Gepopistadxf,
                el ID_SISTEMA=ID_LAYER.SystemId(=2, p.e.). En el ejemplo, ID_SISTEMA=path\nomFichero.shp.2
                ...
                1001
                GEOPISTA
                1002
                {
                1000
                path\nomFichero.shp.2
                ...

                El resultado de hacer el sKey.split("\\.") al recoger la coleccion de features, seria:
                asKey[0]= path\nomFichero
                asKey[1]= shp

                asKey[0], nunca coincidiria con el idLayer (sLayer) y no recogeriamos
                la coleccion de features.
                */                

                //String[] asKey=sKey.split("\\.");
                String[] asKey= new String[2];
                int index= sKey.lastIndexOf('.');
                if (index != -1){
                    asKey[0]= sKey.substring(0, index);
                    asKey[1]= sKey.substring(index+1, sKey.length());
                }else{
                    asKey[0]= "";
                    asKey[1]= "";
                }


                if (asKey[0].equalsIgnoreCase(sLayer)){
                    GeopistaFeature gf=new GeopistaFeature(schema);
                    gf.setAttributes(convertAttributes(sLayer,(Object[])htAttributes.get(sKey),(Object[])htAttClasses.get(sKey)));
                    gf.setSystemId(asKey[1]);
                    gf.setGeometry(buildGeometry(sKey,(Hashtable)htGeometry.get(sKey)));
                    fcRet.add(gf);
                }
            }catch(ParseException pe){
                pe.printStackTrace();
            }
        }
        return fcRet;
    }

    private Geometry buildGeometry(String sSystemId, Hashtable htGeoms){
        ArrayList alGeoms=new ArrayList();
        for (Enumeration eGeoms=htGeoms.keys();eGeoms.hasMoreElements();){
            Hashtable htRings=(Hashtable) htGeoms.get(eGeoms.nextElement());
            if (!isPolygon((String)htGeomTypes.get(sSystemId))){
                alGeoms.add(htRings.values().iterator().next());
            } else{
                try{
                    /* NOTA: en el conjunto de LR en htRings, debe estar incluido el LR de
                    contorno(LR=0). Si no es asi, el fichero dxf puede que no este bien formado.
                    Si esto ocurre, se lanzara una excepcion del tipo IndexBoundException. */

                    LinearRing lrExternal=null;
                    LinearRing aRings[]=new LinearRing[htRings.size()-1];
                    int i=0;
                    for (Enumeration eRings=htRings.keys();eRings.hasMoreElements();){
                        String sKey=(String)eRings.nextElement();
                        if (sKey.equals("0"))
                            lrExternal=(LinearRing)htRings.get(sKey);
                        else
                            aRings[i++]=(LinearRing)htRings.get(sKey);
                    }
                    /* NOTA: si el LR de contorno (lrExternal) es null y aRings contiene
                    LR, al crear la geometría nos dara una exception. */
                    alGeoms.add(geometryFactory.createPolygon(lrExternal,aRings));
                }catch(Exception e){}
            }
        }
        return geometryFactory.buildGeometry(alGeoms);
    }

    private boolean isGeometryCollection(String sClassName){
        return (sClassName.endsWith("MultiPoint")||
                sClassName.endsWith("MultiLineString")||
                sClassName.endsWith("MultiPolygon")||
                sClassName.endsWith("GeometryCollection"));
    }

    private boolean isPolygon(String sClassName){
        return sClassName.endsWith("Polygon");
    }

    // Deberíamos usar el esquema, pero el XML se exporta mal y no coinciden los
    // indices de los atributos: falta la geometria
    private Object[] convertAttributes(String sLayer,Object[] aStrings,Object[] aClasses) throws ParseException{
        List lSchemaAtts=((GeopistaSchema)htSchemas.get(sLayer)).getAttributes();
        Object[] aoRet=new Object[lSchemaAtts.size()];
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        /** Formato de fecha para los atributos extendidos de tipo java.util.Date */
        /** Ejemplo de formato del valor de un atributo extendido java.util.Date: Sat Dec 30 00:00:00 CET 1899 */
        SimpleDateFormat sdf2= new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.UK);
        
        for (int i=0;i<aStrings.length;i++){
            String sType=((Attribute)lSchemaAtts.get(i)).getType();
            if (aClasses[i].equals("null")||((String)aClasses[i]).startsWith("com.vividsolutions.jts.geom."))
                aoRet[i]=null;
            else if (aClasses[i].equals("java.math.BigDecimal")){
                if(aStrings[i]==null||aStrings[i].equals("null")||((String)aStrings[i]).trim().equals("")||isTempID(aStrings[i].toString())){
                    aoRet[i]=null;
                }else{
                    aoRet[i]=new BigDecimal((String)aStrings[i]);
                }
            } else if(aClasses[i].equals("java.sql.Date")){
                if(aStrings[i]==null||aStrings[i].equals("null")||((String)aStrings[i]).trim().equals("")||isTempID(aStrings[i].toString())){
                    aoRet[i]=null;
                }else{
                    aoRet[i]=new java.sql.Date(sdf.parse((String)aStrings[i]).getTime());
                }
                
            }else if (aClasses[i].equals("java.util.Date")){
                if(aStrings[i]==null||aStrings[i].equals("null")||((String)aStrings[i]).trim().equals("")||isTempID(aStrings[i].toString())){
                    aoRet[i]=null;
                }else{
                    aoRet[i]=new java.util.Date(sdf2.parse((String)aStrings[i]).getTime());
                }
            }
        	else if (aClasses[i].equals("java.lang.Integer")){
        		if(aStrings[i]==null||aStrings[i].equals("null")||((String)aStrings[i]).trim().equals("")||isTempID(aStrings[i].toString())){
                    aoRet[i]=null;
                }else{
                    aoRet[i]=new Integer((String)aStrings[i]);
                }
        	}
        	else if (aClasses[i].equals("java.lang.Long")){
        		if(aStrings[i]==null||aStrings[i].equals("null")||((String)aStrings[i]).trim().equals("")||isTempID(aStrings[i].toString())){
                    aoRet[i]=null;
                }else{
                    aoRet[i]=new Long((String)aStrings[i]);
                }
        	}
        	else if (aClasses[i].equals("java.lang.Float")){
        		if(aStrings[i]==null||aStrings[i].equals("null")||((String)aStrings[i]).trim().equals("")||isTempID(aStrings[i].toString())){
                    aoRet[i]=null;
                }else{
                    aoRet[i]=new Float((String)aStrings[i]);
                }
        	}
        	else if (aClasses[i].equals("java.lang.Double")){
        		if(aStrings[i]==null||aStrings[i].equals("null")||((String)aStrings[i]).trim().equals("")||isTempID(aStrings[i].toString())){
                    aoRet[i]=null;
                }else{
                    aoRet[i]=new Double((String)aStrings[i]);
                }
        	}
        	
            else
                aoRet[i]=aStrings[i];
            // TODO: Booleanos?
        }
        return aoRet;
    }

    public void convert(DxfFile dxfFile) {
        Enumeration set = dxfFile.getEntities().getEntitySet().getEntities();

		while (set.hasMoreElements()) {
			DxfEntity ent = (DxfEntity) set.nextElement();
			ent.convert(this, dxfFile, null);
		}//while
    }

    public void convert(Dxf3DFACE face, DxfFile dxf, Object collector) {

    }

    public void convert(DxfARC arc, DxfFile dxf, Object collector) {

    }

    public void convert(DxfBLOCK block, DxfFile dxf, Object collector) {

    }

    public void convert(DxfCIRCLE circle, DxfFile dxf, Object collector) {

    }

    public void convert(DxfDIMENSION dim, DxfFile dxf, Object collector) {

    }

    public void convert(DxfINSERT insert, DxfFile dxf, Object collector) {

    }

    public void convert(DxfLINE line, DxfFile dxf, Object collector) {
        /* NOTA: Un fichero geopistadxf trabaja con entidades POLYLINE y no con entidades LINE, por lo que
           directamente llamamos a los metodos originales del jump para hacer la conversion.
           Si un fichero dxf contiene entidades LINE, al guardarlo como geopistadxf, estas se transforman en entidades POLYLINE. */
        super.convert(line, dxf, collector);
    }

    public void convert(DxfPOINT point, DxfFile dxf, Object collector) {
        if (point.getXData() != null){
            if (point.getXData().getAppID().equals("GEOPISTA_SCHEMA")){
                try{
                    htSchemas.put(point.getLayerName(),point.getXData().getSchema());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else if (point.getXData().getAppID().equals("GEOPISTA_LAYER")){
                try{
                    htLayers.put(point.getLayerName(),((DxfXDataLayer)point.getXData()).getLayer());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                /** NOTA. A veces, cuando cargamos un fichero GEOPISTA_dxf en AutoCAD
                 * y lo guardamos como AutoCAD R12/LT2 DXF, el fichero pierde el GEOPISTA_SCHEMA.
                 * Por lo tanto tenemos un fichero con atributos extendidos (XData) pero sin el GEOPISTA_SCHEMA,
                 * por lo que no se puede pintar el mapa. Por eso, además de comprobar que la entidad
                 * tenga atributos extendidos, también comprobamos que haya definido un schema
                 * para la capa a la que corresponde la entidad. Si no es así, llamamos a los
                 * metodos originales del jump para hacer la conversion.
                 *
                 */

                /** NOTA: Además comprobamos que el Identificador_Layer del sSystemId (Identificador_Layer.id_Sistema)
                 * de los atributos extendidos (XData) coincida con el identificador de layer (poly.getLayerName()). */

                if ((htSchemas.get(point.getLayerName()) != null) &&
                    (((DxfXDataAttributes)point.getXData()).getSystemId() != null?((DxfXDataAttributes)point.getXData()).getSystemId().startsWith(point.getLayerName()+"."):false)){
                    DxfXDataAttributes xaAtts=(DxfXDataAttributes)point.getXData();
                    String sSystemId=xaAtts.getSystemId();
                    htGeomTypes.put(sSystemId,xaAtts.getGeometryClass());
                    Point3D p = point.getPosition();
                    Coordinate coordinate = new Coordinate(p.x, p.y);
                    Geometry geometry = this.geometryFactory.createPoint(coordinate);
                    loadGeometry(sSystemId,geometry,xaAtts.getGeomNum(),xaAtts.getRingNum());
                    loadAttributes(sSystemId,xaAtts.getAttributes());
                    loadAttributeTypes(sSystemId,xaAtts.getAttributeTypes());
                }else{
                    super.convert(point, dxf, collector);
                }
            }
        }else{
            super.convert(point, dxf, collector);
        }
    }

    public void convert(DxfPOLYLINE poly, DxfFile dxf, Object collector){
        /** NOTA. A veces, cuando cargamos un fichero GEOPISTA_dxf en AutoCAD
         * y lo guardamos como AutoCAD R12/LT2 DXF, el fichero pierde el GEOPISTA_SCHEMA.
         * Por lo tanto tenemos un fichero con atributos extendidos (XData) pero sin el GEOPISTA_SCHEMA,
         * por lo que no se puede pintar el mapa. Por eso, además de comprobar que la entidad
         * tenga atributos extendidos, también comprobamos que haya definido un schema
         * para la capa a la que corresponde la entidad. Si no es así, llamamos a los
         * metodos originales del jump para hacer la conversion.
         *
         */
        /** NOTA: Además comprobamos que el Identificador_Layer del sSystemId (Identificador_Layer.id_Sistema)
         * de los atributos extendidos (XData) coincida con el identificador de layer (poly.getLayerName()). */
        if ((poly.getXData() != null) &&
            (htSchemas.get(poly.getLayerName()) != null) &&
            (((DxfXDataAttributes)poly.getXData()).getSystemId() != null?((DxfXDataAttributes)poly.getXData()).getSystemId().startsWith(poly.getLayerName()+"."):false)){
            DxfXDataAttributes xaAtts=(DxfXDataAttributes)poly.getXData();
            String sSystemId=xaAtts.getSystemId();
            htGeomTypes.put(sSystemId,xaAtts.getGeometryClass());
            DxfEntitySet desVertices=poly.getVertices();
            boolean bClosed=((poly.getType()&DxfPOLYLINE.CLOSED)!=0);
            int iVertices=bClosed?desVertices.getNrOfEntities()+1:desVertices.getNrOfEntities();
            Coordinate[] aCoord=new Coordinate[iVertices];
            Enumeration enumerationElement =desVertices.getEntities();
            /*
            for (int i=0;i<=aCoord.length-1;i++){
                Point3D point=((DxfVERTEX)enumerationElement.nextElement()).getPosition();
                aCoord[i]=new Coordinate(point.x,point.y);
            }
            */
            for (int i=0; enumerationElement.hasMoreElements(); i++){
                Point3D point=((DxfVERTEX)enumerationElement.nextElement()).getPosition();
                aCoord[i]=new Coordinate(point.x,point.y);
            }
            Geometry geometry=null;
            if (bClosed){
                aCoord[aCoord.length-1]=new Coordinate(aCoord[0].x,aCoord[0].y);
                geometry=geometryFactory.createLinearRing(aCoord);
            }else
                geometry=geometryFactory.createLineString(aCoord);

            loadGeometry(sSystemId,geometry,xaAtts.getGeomNum(),xaAtts.getRingNum());
            loadAttributes(sSystemId,xaAtts.getAttributes());
            loadAttributeTypes(sSystemId,xaAtts.getAttributeTypes());
        }else{
            super.convert(poly, dxf, collector);
        }
    }


    public void convert(DxfSOLID solid, DxfFile dxf, Object collector) {

    }

    public void convert(DxfTEXT text, DxfFile dxf, Object collector) {
    	super.convert(text, dxf, collector);
    }

    public void convert(DxfTRACE trace, DxfFile dxf, Object collector) {

    }

    public void convert(DxfEntitySet set, DxfFile dxf, Object collector) {

    }

    public void convert(DxfATTRIB converter, DxfFile dxf, Object collector) {
    	super.convert(converter, dxf, collector);
    }

    private void loadGeometry(String sSystemId, Geometry geometry, int iGeomNum,int iRingNum){
        String sGeomNum=String.valueOf(iGeomNum);
        String sRingNum=String.valueOf(iRingNum);
        Hashtable htGeoms=null;
        Hashtable htRings=null;
        if (!htGeometry.containsKey(sSystemId)){
            htGeoms=new Hashtable();
            htGeometry.put(sSystemId,htGeoms);
        } else{
            htGeoms=(Hashtable)htGeometry.get(sSystemId);
        }
        if (!htGeoms.containsKey(sGeomNum)){
            htRings=new Hashtable();
            htGeoms.put(sGeomNum,htRings);
        }else{
            htRings=(Hashtable)htGeoms.get(sGeomNum);
        }
        htRings.put(sRingNum,geometry);
    }

    private void loadAttributes(String sSystemId, Object[] aoAtts){
        if (!htAttributes.containsKey(sSystemId))
            htAttributes.put(sSystemId,aoAtts);
    }

    private void loadAttributeTypes(String sSystemId, Object[] aoAtts){
        if (!htAttClasses.containsKey(sSystemId))
            htAttClasses.put(sSystemId,aoAtts);
    }

    public boolean isTempID(String id){
        return id.trim().toUpperCase().startsWith("NoInicializado".toUpperCase());
    }


}
