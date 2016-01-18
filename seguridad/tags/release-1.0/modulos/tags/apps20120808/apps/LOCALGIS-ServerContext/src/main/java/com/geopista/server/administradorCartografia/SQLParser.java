package com.geopista.server.administradorCartografia;

/* REFACTORIZACION ORACLE
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
*/
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Hashtable;

/* REFACTORIZACION ORACLE
import oracle.sql.STRUCT;
import oracle.sdoapi.sref.SRManager;
import oracle.sdoapi.sref.SpatialReference;
import oracle.sdoapi.OraSpatialManager;
import oracle.sdoapi.adapter.AdapterSDO;
import oracle.sdoapi.geom.GeometryFactory;
import org.geotools.data.oracle.attributeio.AdapterJTS;
*/


public class SQLParser {

    private Connection conn=null;
    private NewSRID srid=null;
    // REFACTORIZACION ORACLE private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SQLParser.class);
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SQLParser.class);

    public SQLParser(Connection conn, NewSRID srid){
        this.conn=conn;
        this.srid=srid;
    }

    /** Sirve para los insert, update y delete. */
    /* REFACTORIZACION ORACLE
    public PreparedStatement[] newUpdate(int iMunicipio,String sSQL, ACLayer schema, ACFeatureUpload featureUpload) throws SQLException, ACException{
        String sQueries[]=sSQL.split(";");
        PreparedStatement[] apsRet=new PreparedStatement[sQueries.length];
        Pattern pattern=Pattern.compile("\\?(M|S|\\d+)");
        ArrayList alParams=new ArrayList();
        ArrayList alTypes=new ArrayList();
        Hashtable htAtts=schema.getAttributes();
        for(int i=0;i<sQueries.length;i++){
            logger.info(sQueries[i]);
            Matcher matcher=pattern.matcher(sQueries[i]);
            alParams.clear();
            while(matcher.find()){
                String sMatch=matcher.group().substring(1);
                if (sMatch.equals("M")){
                    alParams.add(new Integer(iMunicipio));
                    alTypes.add(new Integer(ACLayer.TYPE_NUMERIC));
                }else if (sMatch.equals("S")){
                    alParams.add(new Integer(srid.getSRID(iMunicipio)));
                    alTypes.add(new Integer(ACLayer.TYPE_NUMERIC));
                }
                else{
                    int iIndex = Integer.parseInt(sMatch);
                    if (featureUpload.getAttValues().length<iIndex)
                        throw new ACException("No value specified for parameter: "+iIndex);
                    alParams.add(featureUpload.getAttValues()[iIndex-1]);
                    ACAttribute acAtt=(ACAttribute)htAtts.get(new Integer(iIndex));
                    alTypes.add(new Integer(acAtt.getColumn().getType()));
                }
            }
            apsRet[i]=this.conn.prepareStatement(matcher.replaceAll("?"));
            int iIndex=0;
            for (Iterator it=alParams.iterator();it.hasNext();){
                ++iIndex;
                Object oValue=it.next();
                if ( oValue==null ||
                    (oValue instanceof java.lang.String && oValue.equals(""))){ // ""=No rellenado -> insertamos Null
                    setNullColumn(alTypes,apsRet[i],iIndex);
                    logger.info("param "+iIndex+": NULL");
                } else{
                    apsRet[i].setObject(iIndex,oValue);
                    logger.info("param "+iIndex+": " + oValue.toString());
                }
            }
        }
        return apsRet;
    }
    */

     /** Sirve para los insert, update y delete. */
     /* REFACTORIZACION ORACLE
    public PreparedStatement[] newUpdateOracle(int iMunicipio,String sSQL, ACLayer schema,
                                               ACFeatureUpload featureUpload) throws SQLException, ACException{
        String sQueries[]=sSQL.split(";");
        PreparedStatement[] apsRet=new PreparedStatement[sQueries.length];
        Pattern pattern=Pattern.compile("\\?(M|S|\\d+)");
        ArrayList alParams=new ArrayList();
        ArrayList alTypes=new ArrayList();
        Hashtable htAtts=schema.getAttributes();
        for(int i=0;i<sQueries.length;i++){
            logger.info(sQueries[i]);
            Matcher matcher=pattern.matcher(sQueries[i]);
            alParams.clear();
            while(matcher.find()){
                String sMatch=matcher.group().substring(1);
                if (sMatch.equals("M")){
                    alParams.add(new Integer(iMunicipio));
                    alTypes.add(new Integer(ACLayer.TYPE_NUMERIC));
                }else if (sMatch.equals("S")){
                    alParams.add(new Integer(srid.getSRID(iMunicipio)));
                    alTypes.add(new Integer(ACLayer.TYPE_NUMERIC));
                }else{
                    int iIndex = Integer.parseInt(sMatch);
                    if (featureUpload.getAttValues().length<iIndex)
                        throw new ACException("No value specified for parameter: "+iIndex);
                    ACAttribute att= (ACAttribute)schema.getAttributes().get(new Integer(iIndex));
                    if (att!=null && att.getColumn().getType()==ACLayer.TYPE_GEOMETRY)
                    {
                        try
                        {
                            logger.info("La columna iIndex "+iIndex+" es geometrica");
                            oracle.sdoapi.sref.SRManager manager = oracle.sdoapi.OraSpatialManager.getSpatialReferenceManager((oracle.jdbc.OracleConnection)(((org.enhydra.jdbc.core.CoreConnection)conn).con));
                            oracle.sdoapi.sref.SpatialReference sr= manager.retrieve(srid.getSRID(iMunicipio));
                            oracle.sdoapi.geom.GeometryFactory gFact= oracle.sdoapi.OraSpatialManager.getGeometryFactory(sr);
                            oracle.sdoapi.adapter.AdapterSDO adaptersdo= new oracle.sdoapi.adapter.AdapterSDO(gFact,(oracle.jdbc.OracleConnection) (((org.enhydra.jdbc.core.CoreConnection)conn).con));
                            org.geotools.data.oracle.attributeio.AdapterJTS adapterJTS= new org.geotools.data.oracle.attributeio.AdapterJTS(gFact);
                            WKTReader wktReader = new WKTReader();
                            oracle.sdoapi.geom.Geometry geometry =
                            adapterJTS.importGeometry(wktReader.read((String)featureUpload.getAttValues()[iIndex-1]));
                            Object exportedStruct = adaptersdo.exportGeometry(oracle.sql.STRUCT.class,geometry);
                            alParams.add(exportedStruct);

                        }catch(Exception e)
                        {
                            logger.error("Error al convertir la geometria",e);
                            alParams.add(featureUpload.getAttValues()[iIndex-1]);
                        }
                     } else
                        alParams.add(featureUpload.getAttValues()[iIndex-1]);
                    ACAttribute acAtt=(ACAttribute)htAtts.get(new Integer(iIndex));
                    alTypes.add(new Integer(acAtt.getColumn().getType()));
                }
            }
            apsRet[i]=this.conn.prepareStatement(matcher.replaceAll("?"));
            int iIndex=0;
            for (Iterator it=alParams.iterator();it.hasNext();){
                ++iIndex;
                Object oValue=it.next();
                if ( oValue==null ||
                    (oValue instanceof java.lang.String && oValue.equals(""))){ // ""=No rellenado -> insertamos Null
                    setNullColumn(alTypes,apsRet[i],iIndex);
                    logger.info("param "+iIndex+": NULL");
                } else{

                    apsRet[i].setObject(iIndex,oValue);
                    logger.info("param "+iIndex+": " + oValue.toString());
                }
            }
        }
        return apsRet;
    }
    */

       /** Sirve para hacer las actualizaciones por lotes */
    public PreparedStatement[] newUpdateLotes(int iMunicipio,String sSQL, ACLayer schema, ACFeatureUpload featureUpload, PreparedStatement[] aPs) throws SQLException, ACException{
        String sQueries[]=sSQL.split(";");
        if (aPs==null)
            aPs=new PreparedStatement[sQueries.length];
        Pattern pattern=Pattern.compile("\\?(M|S|\\d+)");
        ArrayList alParams=new ArrayList();
        ArrayList alTypes=new ArrayList();
        Hashtable htAtts=schema.getAttributes();
        for(int i=0;i<sQueries.length;i++)
        {
            logger.info(sQueries[i]);
            Matcher matcher=pattern.matcher(sQueries[i]);
            alParams.clear();
            while(matcher.find())
            {
                String sMatch=matcher.group().substring(1);
                if (sMatch.equals("M")){
                    alParams.add(new Integer(iMunicipio));
                    alTypes.add(new Integer(ACLayer.TYPE_NUMERIC));
                }else if (sMatch.equals("S")){
                    alParams.add(new Integer(srid.getSRID(iMunicipio)));
                    alTypes.add(new Integer(ACLayer.TYPE_NUMERIC));
                }else{
                    int iIndex = Integer.parseInt(sMatch);
                    if (featureUpload.getAttValues().length<iIndex)
                                          throw new ACException("No value specified for parameter: "+iIndex);

                    alParams.add(featureUpload.getAttValues()[iIndex-1]);
                    ACAttribute acAtt=(ACAttribute)htAtts.get(new Integer(iIndex));
                    alTypes.add(new Integer(acAtt.getColumn().getType()));
                }
            }
            if (aPs[i]==null)
                    aPs[i]=this.conn.prepareStatement(matcher.replaceAll("?"));
            int iIndex=0;
            for (Iterator it=alParams.iterator();it.hasNext();){
                ++iIndex;
                Object oValue=it.next();
                if ( oValue==null ||
                    (oValue instanceof java.lang.String && oValue.equals(""))){ // ""=No rellenado -> insertamos Null
                    setNullColumn(alTypes,aPs[i],iIndex);
                    logger.debug("param "+iIndex+": NULL");
                } else{
                    aPs[i].setObject(iIndex,oValue);
                    logger.debug("param "+iIndex+": " + oValue.toString());
                }
            }
        }
        return aPs;
    }

    // REFACTORIZACION ORACLE private void setNullColumn(List lTypes,PreparedStatement ps,int iParamPosition) throws SQLException{
    public static void setNullColumn(List lTypes,PreparedStatement ps,int iParamPosition) throws SQLException{
        int iType=((Integer)lTypes.get(iParamPosition-1)).intValue();
        switch (iType){
            case ACLayer.TYPE_BOOLEAN:
                ps.setNull(iParamPosition,Types.BOOLEAN);
                break;
            case ACLayer.TYPE_DATE:
                ps.setNull(iParamPosition,Types.DATE);
                break;
            case ACLayer.TYPE_NUMERIC:
                ps.setNull(iParamPosition,Types.NUMERIC);
                break;
            case ACLayer.TYPE_STRING:
                ps.setNull(iParamPosition,Types.VARCHAR);
                break;
            case ACLayer.TYPE_GEOMETRY:
                logger.error("GEOMETRY=NULL");
                ps.setNull(iParamPosition, Types.INTEGER);
                break;
//                throw new NullPointerException("Geometry=Null");
            default:
                logger.error("Unknown type "+iType);
        }
    }

   /* REFACTORIZACION ORACLE
    public PreparedStatement newSelect(int iMunicipio,String sSQL, ACLayer schema,Geometry geometry, FilterNode fn)
            throws SQLException, ACException{
        String sPattern="\\?[MG]";
            StringBuffer sbSQL=new StringBuffer(sSQL);
        int iOffset=sSQL.toUpperCase().indexOf(" GROUP BY");
        if (iOffset==-1)
            iOffset=sSQL.length();
        if (fn!=null)
            sbSQL.insert(iOffset," and "+fn.toSQL());
        System.out.println(sbSQL);
        // Hay que ponerlo como texto para usar el operador '&&' en PostGIS
        //"\"GEOMETRY\" && GeometryFromText(?,23030) and";
        List lFilterParams=new ArrayList();
        Pattern pattern=Pattern.compile(sPattern);
        Matcher matcher=pattern.matcher(sSQL);
        while(matcher.find()){
            String sChar=matcher.group().substring(1,2).toUpperCase();
            if (sChar.equals("G"))
                lFilterParams.add(geometry!=null?(Object)geometry.toText():new Boolean(true));
            else if (sChar.equals("M"))
                lFilterParams.add(new Integer(iMunicipio));
        }
        if (fn!=null)
            fn.values(lFilterParams);
        String sGeomFilter=(geometry==null? "?" // Se sustituirá por "true"
                          :schema.findPrimaryTable()+".\"GEOMETRY\" && GeometryFromText(?,"+srid.getSRID(iMunicipio)+")");
        System.out.println(sbSQL.toString().replaceAll("\\?G",sGeomFilter).replaceAll(sPattern,"?"));
        PreparedStatement psRet=this.conn.prepareStatement(sbSQL.toString().replaceAll("\\?G",sGeomFilter).replaceAll(sPattern,"?"));
        int iIndex=0;
        for (Iterator it=lFilterParams.iterator();it.hasNext();)
            psRet.setObject(++iIndex,it.next());
        return psRet;
    }
    */

    /* REFACTORIZACION ORACLE
     //Todo Hay  que cambiar esta función para que funcione con oracle
     public PreparedStatement newSelectOracle(int iMunicipio,String sSQL, ACLayer schema,Geometry geometry, FilterNode fn)
            throws SQLException, ACException{
        String sPattern="\\?[MG]";
            StringBuffer sbSQL=new StringBuffer(sSQL);
        int iOffset=sSQL.toUpperCase().indexOf(" GROUP BY");
        if (iOffset==-1)
            iOffset=sSQL.length();
        if (fn!=null)
            sbSQL.insert(iOffset," and "+fn.toSQL());
        System.out.println(sbSQL);
        // Hay que ponerlo como texto para usar el operador '&&' en PostGIS
        //"\"GEOMETRY\" && GeometryFromText(?,23030) and";
        List lFilterParams=new ArrayList();
        Pattern pattern=Pattern.compile(sPattern);
        Matcher matcher=pattern.matcher(sSQL);
        while(matcher.find()){
            String sChar=matcher.group().substring(1,2).toUpperCase();
            if (sChar.equals("G"))
                lFilterParams.add(geometry!=null?(Object)geometry.toText():new Boolean(true));
            else if (sChar.equals("M"))
                lFilterParams.add(new Integer(iMunicipio));
        }
        if (fn!=null)
            fn.values(lFilterParams);
        String sGeomFilter=(geometry==null? "?" // Se sustituirá por "true"
                          :schema.findPrimaryTable()+".\"GEOMETRY\" && GeometryFromText(?,"+srid.getSRID(iMunicipio)+")");
        System.out.println(sbSQL.toString().replaceAll("\\?G",sGeomFilter).replaceAll(sPattern,"?"));
        PreparedStatement psRet=this.conn.prepareStatement(sbSQL.toString().replaceAll("\\?G",sGeomFilter).replaceAll(sPattern,"?"));
        int iIndex=0;
        for (Iterator it=lFilterParams.iterator();it.hasNext();)
            psRet.setObject(++iIndex,it.next());
        return psRet;
    }
    */

    public static String replaceString(String str, String pattern, String replace)
    {
            int s = 0;
            int e = 0;
            StringBuffer result = new StringBuffer();
            while ((e = str.indexOf(pattern, s)) >= 0) {
                result.append(str.substring(s, e));
                result.append(replace);
                s = e+pattern.length();
            }
            result.append(str.substring(s));
            return result.toString();
        }



}
