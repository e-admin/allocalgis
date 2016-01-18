package com.geopista.server.administradorCartografia;

import com.geopista.server.database.CPoolDatabase;
import com.geopista.protocol.AbstractValidator;
import com.geopista.protocol.administrador.EncriptarPassword;
import com.geopista.protocol.administrador.ListaUsuarios;
import com.geopista.protocol.administrador.Permiso;
import com.geopista.protocol.administrador.Usuario;
import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.control.SesionUtils_LCGIII;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.model.LayerStyleData;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jump.feature.Feature;
import com.geopista.ui.dialogs.beans.ExtractionProject;

import java.sql.*;
import java.sql.Date;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.regex.Pattern;
import java.util.*;


import oracle.sql.STRUCT;
import oracle.sdoapi.OraSpatialManager;
import oracle.sdoapi.adapter.AdapterSDO;
import oracle.sdoapi.geom.GeometryFactory;
import oracle.sdoapi.sref.SRManager;
import oracle.sdoapi.sref.SpatialReference;
import org.geotools.data.oracle.attributeio.AdapterJTS;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.postgis.MultiLineString;
import org.postgis.PGgeometry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



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


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 03-ago-2005
 * Time: 12:33:30
 */

public class OracleConnection implements GeopistaConnection {
     /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(OracleConnection.class);
    private static final int DATABASETYPE=2;
    private static final int UTM_29N_ED50=23029;
    private static final int UTM_30N_ED50=23030;
    private static final int UTM_31N_ED50=23031;
    private static final int UTM_29N_ETRS89=25829;
    private static final int UTM_30N_ETRS89=25830;
    private static final int UTM_31N_ETRS89=25831;

    private NewSRID srid;

    static{
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Connection getConnection(String sUrl, String sUser, String sPass) throws SQLException
    {
        Connection conn=DriverManager.getConnection(sUrl,sUser,sPass);
        return conn;
    }
    private Connection openConnection() throws SQLException{
        Connection conn=CPoolDatabase.getConnection();
        conn.setAutoCommit(true);
        return conn;
    }

    public OracleConnection(){
    }

    /**
     * Inicializa la clase con  un srid
     * @param srid
     */
    public OracleConnection(NewSRID srid){
        this.srid=srid;
    }

    public void setSRID(NewSRID srid){
        this.srid=srid;
    }


    /**
     * Esta funcion carga los dominios del sistema
     * @throws SQLException
     */
    public void loadDomains() throws SQLException{
            String sSQL="select dom.id as id_dom, dom.name as name_dom,"
                       +      " dn.id as id_dn, dn.parentdomain, dn.\"type\" as tipo, dn.id_municipio, dn.pattern, dn.id_description,"
                       +      " dic.locale, dic.traduccion "
                       +"from domainnodes dn left join domains dom on dn.id_domain=dom.id "
                       +                    "left join dictionary dic on dn.id_description=dic.id_vocablo "
                       +"order by id_municipio asc,id_dom asc,id_dn";
            Connection conn=null;
            PreparedStatement ps=null;
            ResultSet rs=null;
            try{
                conn=openConnection();
                ps=conn.prepareStatement(sSQL);
                rs=ps.executeQuery();
                ListaDomain lDomains=null;
                ListaDomainNode lNodes=null;
                DomainNode node=null;
                int iLastMunicipio=-1;
                int iMunicipio=-1;
                int iLastNode=-1;
                int iLastDomain=-1;
                int iNode=0;
                GeopistaDomains.clear();
                while (rs.next()){
                    try{
                        iMunicipio=rs.getInt("id_municipio");
                        if (iMunicipio!=iLastMunicipio){
                            if (lNodes!=null){
                                lNodes.restructurar();
                                GeopistaDomains.addDomains(iLastMunicipio,lDomains);
                            }
                            iLastMunicipio=iMunicipio;
                            lDomains=new ListaDomain();
                            lNodes=new ListaDomainNode();
                        }
                        int idDomain=rs.getInt("id_dom");
                        String sParent=rs.getString("parentdomain");
                        if (iLastDomain!=idDomain){
                            lDomains.add(new Domain(new Integer(idDomain).toString(),rs.getString("name_dom")));
                            iLastDomain=idDomain;
                        }
                        String sIdDescription=rs.getString("id_description");
                        iNode=rs.getInt("id_dn");
                        if (!rs.wasNull()){
                            // Crear Node
                            if (iNode!=iLastNode){
                                iLastNode=iNode;
                                node= new DomainNode(String.valueOf(iNode),
                                        sIdDescription,
                                        rs.getInt("tipo"),
                                        sParent,
                                        String.valueOf(iMunicipio),
                                        new Integer(idDomain).toString(),
                                        rs.getString("pattern"));
                                lNodes.add(node);
                                if (sParent==null)
                                    lDomains.get(new Integer(idDomain).toString()).addNode(node);
                            }
                            if (sIdDescription!=null)
                                node.addTerm(rs.getString("locale"), rs.getString("traduccion"));
                        }
                    }catch (SQLException sqle){
                        throw sqle;
                    }catch (Exception e){
                        logger.error(e.getMessage() + "(domain_node "+iNode+")");
                    }
                }
                lNodes.restructurar();
                GeopistaDomains.addDomains(iLastMunicipio,lDomains);
                if (logger.isDebugEnabled())
                {
                    logger.debug("loadDomains()" + lDomains.print());
                }
                if (logger.isDebugEnabled())
                {
                    logger.debug("loadDomains()" + lNodes.print());
                }
            }finally{
                try{rs.close();}catch(Exception e){};
                try{ps.close();}catch(Exception e){};
                try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
            }
        }

       public static void main(String sArgs[])throws Exception{

           try
           {


               //Para leer
                Class.forName("oracle.jdbc.driver.OracleDriver");
               Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@claudia.malab.satec.es:1521:DBNRED", "geopista", "geopista");
               SRManager manager = OraSpatialManager.getSpatialReferenceManager((oracle.jdbc.OracleConnection)conn);
               SpatialReference sr= manager.retrieve("23045");
               GeometryFactory gFact= OraSpatialManager.getGeometryFactory(sr);

               AdapterSDO adaptersdo= new AdapterSDO(gFact,(oracle.jdbc.OracleConnection) conn);
               AdapterJTS adapterJTS= new AdapterJTS(gFact);

               Statement s = conn.createStatement();
               ResultSet r = s.executeQuery("select geometry from zonas_arboladas");
               r.next();
               com.vividsolutions.jts.geom.Geometry jts  =(com.vividsolutions.jts.geom.Geometry) adapterJTS.exportGeometry(com.vividsolutions.jts.geom.Geometry.class,
                       adaptersdo.importGeometry(r.getObject(1)));
                  //Para insertar
               PreparedStatement ps= conn.prepareStatement("update municipios set geometry=? where id=34083");
               WKTReader wktReader = new WKTReader();
               oracle.sdoapi.geom.Geometry geometry =
               adapterJTS.importGeometry(wktReader.read("MULTIPOLYGON(((389473 4722889,389419 4722794,389358 4722687,389375 4722626,389575 4722549,389700 4722589,389867 4722453,390072 4722653,390084 4722708,390142 4722738,390406 4722802,390679 4722983,390878 4722958,391191 4722943,391249 4723001,391479 4723004,391847 4722753,391316 4722162,391152 4721692,391570 4721399,391696 4720685,391669 4720390,392156 4719760,392407 4719071,392917 4718494,393129 4718450,393629 4718207,393731 4718115,394079 4718076,394024 4718045,393950 4717935,393921 4717742,394116 4717523,394170 4717376,394382 4717233,394440 4716663,394652 4716445,394580 4716218,394880 4715923,394626 4715710,394549 4715528,394721 4715332,395064 4715346,395478 4715519,395825 4715320,395753 4715300,395599 4715297,395331 4715150,394851 4714724,393927 4714554,393771 4714459,393511 4714156,393426 4714127,393111 4714216,392973 4714188,393067 4714094,393074 4713984,393588 4713020,393461 4712900,393297 4712846,393137 4712881,393112 4712470,393398 4712149,394001 4711844,394120 4711758,394415 4711678,394960 4711379,395199 4711253,395153 4711145,395093 4711041,395206 4710223,395023 4710049,395189 4709604,395364 4709481,395613 4708503,395476 4708350,395481 4708106,395234 4707900,395048 4707907,394513 4707712,394467 4707669,394445 4707631,394634 4707504,395521 4707440,395729 4707542,396380 4707587,396518 4707409,396632 4707381,397105 4706708,397217 4706617,397299 4706278,397391 4706014,397634 4705667,397707 4705468,397707 4705408,397584 4705027,397454 4704776,397319 4704354,397312 4703963,397587 4703390,397748 4703261,397759 4703120,397319 4703081,397075 4702965,396985 4702759,397046 4702552,397160 4702482,397455 4702651,397554 4702605,397701 4702123,397567 4701842,397627 4701673,397850 4701688,397937 4701389,397210 4701444,396419 4701317,396166 4701161,395774 4701116,395193 4701316,394665 4701326,394530 4701154,394464 4701135,394104 4701254,393647 4701258,393545 4701318,392662 4701342,392634 4701148,392238 4701416,392051 4701650,391816 4701780,391724 4701843,391019 4702153,390961 4702292,390789 4703339,390841 4703328,391104 4703658,391057 4703876,390945 4704083,391074 4704089,391469 4704084,391810 4704265,392293 4704505,392533 4704582,392678 4704803,393016 4705479,393164 4706344,393205 4706605,393527 4706690,393891 4706980,394110 4707618,394127 4707794,393959 4707804,393856 4707922,393167 4707728,392854 4707847,392751 4707763,392631 4707742,392444 4707791,392454 4707712,392332 4707590,392251 4707586,392109 4707779,391967 4708122,391837 4708287,391464 4708265,391184 4708680,390750 4708663,390624 4708884,390551 4708909,390188 4709010,390178 4709124,390446 4709450,390405 4709785,390503 4710276,390406 4710602,390415 4710791,390497 4710956,390396 4711345,390483 4711414,390453 4711541,390566 4711579,390664 4711711,391075 4712055,391015 4712171,390211 4712738,390088 4713101,389564 4713125,389459 4713129,389450 4713261,389509 4713399,389651 4714010,389416 4714082,389388 4714248,389088 4714287,389336 4715572,389342 4715685,389661 4715866,389945 4716225,390052 4716420,390207 4716798,390103 4717588,390228 4718160,390128 4718339,389996 4718417,389524 4718067,389377 4718085,389282 4718416,388798 4718714,388606 4718629,388465 4718733,388088 4718597,387479 4718267,387211 4718285,387200 4718343,387055 4718532,386844 4718537,386717 4718800,386382 4719009,386190 4719052,385940 4719071,385666 4719350,385586 4719435,385954 4719161,386400 4719515,386621 4719834,386691 4720270,386509 4720786,386527 4720872,386415 4721232,386388 4721461,386423 4721636,386484 4721634,386917 4721563,387631 4721880,387739 4721969,387865 4721932,388040 4722061,388349 4722184,388835 4722666,389161 4722690,389473 4722889)))"));
               Object exportedStruct = adaptersdo.exportGeometry(STRUCT.class,geometry);
               ps.setObject(1, exportedStruct);
               ps.executeUpdate();
               conn.close();

           }catch (Exception e)
           {
               e.printStackTrace();
           }

      }

       /** Devuelve todos los Features con los IDs en featureIDs[]
        * @param acl */
       private int returnAllFeatures (int iMunicipio, String sTable, ACLayer layer, int[] featureIDs, ObjectOutputStream oos, Connection connection,ISesion sesion, GeopistaAcl acl)throws Exception{
           int iRet=-1;
           Connection conn=null;
           PreparedStatement ps=null;
           ResultSet rs=null;
           boolean bCerrarConexion=true;
           try{

           	/*if (!checkPermission(sesion,layer.getACL(),Const.PERM_LAYER_READ))
                   throw new PermissionException(Const.PERM_LAYER_READ);*/

   	        if (connection!=null)
   	        {
   	            conn=connection;
   	            bCerrarConexion=false;
   	        }
   	        else
   	        {
   	            conn=openConnection();
   	            bCerrarConexion=true;
   	        }


   	   	if (acl==null){
       		if (!checkPermission(sesion,layer.getACL(),Const.PERM_LAYER_READ,conn))
                throw new PermissionException(Const.PERM_LAYER_READ);
       	}
       	else{
            boolean permisoLectura=acl.checkPermission(new GeopistaPermission(Const.PERM_LAYER_READ));
            if (!permisoLectura)
            	throw new PermissionException(Const.PERM_LAYER_READ);
       	}


               //REFACTORIZACION ORACLE ps=new SQLParser(conn,srid).newSelect(iMunicipio,sSQL,layer,geom,fn);
   	        StringBuffer sbSQL = new StringBuffer(0);
              	sbSQL.append("select ");
              	sbSQL.append(sTable.toUpperCase());
              	sbSQL.append(".* from ");
              	sbSQL.append(sTable.toUpperCase());
              	sbSQL.append(" where (");
              	boolean bFirstValueEntered = false;
           	for (int i=0; i<featureIDs.length; i++){
           		int iFeatureID = featureIDs[i];
           		if (iFeatureID!=-1){
           			if (!bFirstValueEntered){
           				sbSQL.append("ID=");
           				sbSQL.append(iFeatureID);
           				bFirstValueEntered = true;
           			}else{
           				sbSQL.append(" OR ID=");
           				sbSQL.append(iFeatureID);
           			}
           		}
           	}

           	sbSQL.append(")");

       		ps=conn.prepareStatement(sbSQL.toString());

       	 SRManager manager = OraSpatialManager.getSpatialReferenceManager((oracle.jdbc.OracleConnection)((org.enhydra.jdbc.core.CoreConnection)conn).con);
         SpatialReference sr= manager.retrieve(srid.getSRID(iMunicipio));
         GeometryFactory gFact= OraSpatialManager.getGeometryFactory(sr);

         AdapterSDO adaptersdo= new AdapterSDO(gFact,(oracle.jdbc.OracleConnection) ((org.enhydra.jdbc.core.CoreConnection)conn).con);
         AdapterJTS adapterJTS= new AdapterJTS(gFact);

              	rs=ps.executeQuery();
               ACFeature acFeature=null;
               while (rs.next()){
                   acFeature=new ACFeature();
                   ACAttribute att=null;
                   Object oAttValue=null;
                   for (Iterator it=layer.getAttributes().values().iterator();it.hasNext();){
                       att=(ACAttribute)it.next();
                       switch(att.getColumn().getType()){
                          case ACLayer.TYPE_GEOMETRY:
                        	  com.vividsolutions.jts.geom.Geometry jts  =(com.vividsolutions.jts.geom.Geometry) adapterJTS.exportGeometry(com.vividsolutions.jts.geom.Geometry.class,
                              adaptersdo.importGeometry(rs.getObject("GEOMETRY")));

                              if (jts==null) continue;
                              acFeature.setGeometry(jts.toText());
                               break;
                           case ACLayer.TYPE_NUMERIC:
                           case ACLayer.TYPE_STRING:
                           case ACLayer.TYPE_DATE:
                           case ACLayer.TYPE_BOOLEAN:
                           default:
                           try
                           {
                              oAttValue=rs.getObject(att.getColumn().getName());
                              acFeature.setAttribute(att.getName(),(Serializable)oAttValue);
                           }catch(Exception ex)
                           {
                               logger.error("No se puede cargar la columna: "+att.getColumn().getName(),ex);
                           }
                      }
                   }
                    oos.writeObject(acFeature);
                    oos.flush();
               }

           }catch (Exception e){
               logger.error("returnAllFeatures: " + e.getMessage());
               oos.writeObject(new ACException(e));
               oos.flush();
               throw e;
           }finally{
               try{rs.close();}catch(Exception e){};
               try{ps.close();}catch(Exception e){};
               try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e){};
           }
           return iRet;
       }



       public void returnLayer(int iMunicipio,String sLayer,String sLocale, Geometry geom,FilterNode fn,ObjectOutputStream oos,ISesion sesion,List listMunicipios){
           returnLayer(iMunicipio,sLayer,sLocale,geom,fn,oos,sesion,null);
       }

       public void returnLayer(int iMunicipio,String sLayer,String sLocale, Geometry geom,FilterNode fn,ObjectOutputStream oos,ISesion sesion,boolean bValidateData,List listMunicipios){
           returnLayer(iMunicipio,sLayer,sLocale,geom,fn,oos,sesion,bValidateData,null);
       }


       public void returnLayer(int iMunicipio,String sLayer,String sLocale, Geometry geom,FilterNode fn,ObjectOutputStream oos,ISesion sesion,boolean bValidateData,List listMunicipios, Integer srid_destino){
           returnLayer(iMunicipio,sLayer,sLocale,geom,fn,oos,sesion,bValidateData,null, srid_destino);
       }

       public void returnLayer(int iMunicipio,String sLayer,String sLocale, Geometry geom,FilterNode fn,ObjectOutputStream oos,ISesion sesion)throws IOException,SQLException, Exception
       {



//       public void returnLayer(int iMunicipio,String sLayer,String sLocale, Geometry geom,FilterNode fn,ObjectOutputStream oos,ISesion sesion,List listMunicipios) throws IOException,SQLException, Exception {
//       		//returnLayer(iMunicipio,sLayer,sLocale,geom,fn,oos,sesion,null, true, listMunicipios);
//       		returnLayer(iMunicipio,sLayer,sLocale,geom,fn,oos,sesion,true,listMunicipios);
//		}
//
////		public void returnLayer(int iMunicipio,String sLayer,String sLocale, Geometry geom,FilterNode fn,ObjectOutputStream oos,ISesion sesion,boolean bValidateData,List listMunicipios){
////			returnLayer(iMunicipio,sLayer,sLocale,geom,fn,oos,sesion,bValidateData,null);
////		}
//
//	    public void returnLayer(int iEntidad,String sLayer,String sLocale, Geometry geom,FilterNode fn,ObjectOutputStream oos,ISesion sesion, boolean bValidateData,List lMunicipalities)throws IOException,SQLException, Exception {
//	        returnLayer(iEntidad, sLayer, sLocale, geom, fn, oos, sesion, bValidateData, lMunicipalities, null);
//	    }
//
//
//        public void returnLayer(int iEntidad,String sLayer,String sLocale, Geometry geom,FilterNode fn,ObjectOutputStream oos,ISesion sesion, boolean bValidateData,List lMunicipalities, Integer srid)throws IOException,SQLException, Exception
//        {
               logger.info("Cargando el layer: "+sLayer);
               ACLayer lyRet=null;
               /*
               String sSQL="select q.id,q.selectquery,q.updatequery,q.insertquery,q.deletequery,"
                          +"l.name,l.acl,l.id_layer,d.traduccion,l.id_styles,l.extended_form,s.xml "
                   +"from queries q,layers l, dictionary d, styles s "
                   +"where q.databasetype="+DATABASETYPE+" and l.name=? and "
                         +"q.id_layer=l.id_layer and  "
                         +"l.id_name=d.id_vocablo and "
                         +"l.id_styles=s.id_style and "
                         +"d.locale=?";
               */
            String sSQL="select q.id,q.selectquery,q.updatequery,q.insertquery,q.deletequery,"
                       +"l.name,l.acl,l.id_layer,d.traduccion,l.id_styles,l.extended_form,s.xml,d.locale "
                +"from queries q,layers l, dictionary d, styles s "
                +"where q.databasetype="+DATABASETYPE+" and l.name=? and "
                      +"q.id_layer=l.id_layer and  "
                      +"l.id_name=d.id_vocablo and "
                      +"l.id_styles=s.id_style "; //and ";
                      //+"(d.locale=? or d.locale='es_ES' or d.locale='ca_ES' or d.locale='va_ES' or d.locale='gl_ES' or d.locale='eu_ES')";

               Connection conn=null;
               PreparedStatement ps=null;
               ResultSet rs=null;
               try{
                    conn=openConnection();
                    ps=conn.prepareStatement(sSQL);
                    ps.setString(1,sLayer);
                    //ps.setString(2,sLocale);
                    rs=ps.executeQuery();

                    if (rs.next()){
                         //aso para evitar que el resultSet no se posicione si el idioma es el último
                        String selectQuery=rs.getString("selectquery");
                        /* Comentado por Incidencia [365] */
                        /*
                        lyRet=new ACLayer(rs.getInt("id_layer"),rs.getString("traduccion"),rs.getString("name"),replaceSRID(rs.getString("selectquery"),iMunicipio));
                        lyRet.setACL(rs.getLong("acl"));
                        lyRet.setStyleId(String.valueOf(rs.getInt("id_styles")));
                        lyRet.setStyleXML(rs.getString("xml"));
                        lyRet.setExtendedForm(rs.getString("extended_form"));
                        */

                        /** Incidencia[365] El administrador de cartografia no funciona para otros idiomas  */
                        int iCurrentLayerLocale= -1;
                        /** Entra en el bucle */
                        int iLastLayerLocale= 4;
                       // boolean mejorOpcion= false;

                        do{
                            iCurrentLayerLocale= getOpcionLocale(rs.getString("locale"), sLocale);
                            /** Priorizamos los idiomas: preferencia de usuario, es_ES, resto de idiomas */
                            if (iCurrentLayerLocale < iLastLayerLocale){
                                lyRet=new ACLayer(rs.getInt("id_layer"),rs.getString("traduccion"),rs.getString("name"),replaceSRID(rs.getString("selectquery"),iMunicipio));
                                lyRet.setACL(rs.getLong("acl"));
                                lyRet.setStyleId(String.valueOf(rs.getInt("id_styles")));
                                lyRet.setStyleXML(rs.getString("xml"));
                                lyRet.setExtendedForm(rs.getString("extended_form"));
                                lyRet.setActive(true);

                                iLastLayerLocale= iCurrentLayerLocale;
                                if (iCurrentLayerLocale == 1) break;
                            }
                        }while ((rs.next()));
                        /**/

                        if (logger.isDebugEnabled())
                        {
                            logger.debug("returnLayer(int, String, String, Geometry, FilterNode, ObjectOutputStream, Sesion) - buscando acl"
                                    + lyRet.getACL());
                        }

                        GeopistaAcl acl=getPermission(sesion,lyRet.getACL(),conn);
                        checkReadPermLayer(sesion,acl);

                       /* if (!checkPermission(sesion,lyRet.getACL(),Const.PERM_LAYER_READ))
                        {
                            logger.error("No tiene permiso para leer el layer");
                            throw new PermissionException("PermissionException: " + Const.PERM_LAYER_READ);
                        }*/
                        readNewSchema(lyRet,sLocale, conn, sesion,acl);
                        oos.writeObject(lyRet);
                        returnFeatures(iMunicipio,replaceSRID(selectQuery,iMunicipio),lyRet,geom,fn,oos,conn,sesion,acl,sLocale);
                    } else
                        throw new ObjectNotFoundException("No se encuentra layer: "+sLayer+ " ("+sLocale+")");

                    oos.flush();
                   logger.info("Fin de cargar el layer: "+sLayer);
            }catch(Exception e){
                logger.error("Escritura de layer: "+e.getMessage());
                oos.writeObject(new ACException(e));
                throw e;
            }finally{
                try{rs.close();}catch(Exception e){};
                try{ps.close();}catch(Exception e){};
                try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
            }
        }
        public Object getGeoObject(int iMunicipio, Geometry geom, Connection conn )  throws Exception
        {
              if (geom == null) return null;
              SRManager manager = OraSpatialManager.getSpatialReferenceManager((oracle.jdbc.OracleConnection)((org.enhydra.jdbc.core.CoreConnection)conn).con);
              SpatialReference sr= manager.retrieve(srid.getSRID(iMunicipio));
              GeometryFactory gFact= OraSpatialManager.getGeometryFactory(sr);

              AdapterSDO adaptersdo= new AdapterSDO(gFact,(oracle.jdbc.OracleConnection) ((org.enhydra.jdbc.core.CoreConnection)conn).con);
              AdapterJTS adapterJTS= new AdapterJTS(gFact);

              WKTReader wktReader = new WKTReader();
              oracle.sdoapi.geom.Geometry geometry =
              adapterJTS.importGeometry(wktReader.read(geom.toText()));
              Object exportedStruct = adaptersdo.exportGeometry(STRUCT.class,geometry);
              return exportedStruct;
        }

        public int lockLayer(int iMunicipio, String sLayer, int iUser, Geometry geom) throws Exception
        {
             // Geometry del Municipio si geom==null
            int iRet=AdministradorCartografiaClient.LOCK_LAYER_LOCKED;
            String sSQL="";
            if (geom!=null)
                sSQL="insert into locks_layer (municipio,layer,user_id,ts,GEOMETRY) "
                       +"values (?,?,?,?,?)";
            else
                sSQL="insert into locks_layer (municipio,layer,user_id,ts) "
                       +"values (?,?,?,?)";
            Connection conn=null;
            PreparedStatement ps=null;
            try{
                iRet=canLockLayer(iMunicipio,sLayer,geom,iUser);
                if (iRet==0 ){
                    conn=openConnection();
                    ps=conn.prepareStatement(sSQL);
                    ps.setInt(1,iMunicipio);
                    ps.setString(2,sLayer);
                    ps.setInt(3,iUser);
                    ps.setTimestamp(4,new java.sql.Timestamp(System.currentTimeMillis()));
                    if (geom!=null)
                        ps.setObject(5,getGeoObject(iMunicipio, geom, conn));

                    ps.execute();
                }
            }catch(SQLException se){
                iRet=AdministradorCartografiaClient.LOCK_LAYER_ERROR;
                logger.error("lockLayer: " +se.getMessage());
                logger.error("lockLayer(int, String, int, Geometry)", se);
            }finally{
                try{ps.close();}catch(Exception e){};
                try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
            }
            return iRet;
        }
             public int canLockLayer(int iMunicipio, String sLayer, Geometry geom, int iUser) throws SQLException, ACException{
               // Comparar bloqueos de layer y geometrias de features...
               int iRet=0;
               // Comparar ids de Feature y bloqueos de layer con la geometria de iFeature...
               int iLayerLock=layerLocked(iMunicipio,sLayer,geom);
               if(iLayerLock!=-1)
                   return (iLayerLock==iUser?AdministradorCartografiaClient.LOCK_LAYER_OWN
                                            :AdministradorCartografiaClient.LOCK_LAYER_OTHER);
               //Obtener la tabla donde esta la geometria...
               String sSQLTable=null;
               Connection conn=null;
               PreparedStatement ps=null;
               ResultSet rs=null;
               try{
                   conn=openConnection();
                   sSQLTable = "select t.name as \"table\",l.id_layer from tables t,columns c,attributes a,layers l " +
                                 "where a.id_layer=l.id_layer and l.name=? and c.id_table=t.id_table and c.name='GEOMETRY' and a.id_column=c.id";
                   ps=conn.prepareStatement(sSQLTable);
                   ps.setString(1,sLayer);
                   rs=ps.executeQuery();
                   String sTable=null;
                   if (rs.next()){
                       sTable=rs.getString("table");
                   }
                   rs.close();
                   ps.close();
                   String sSQLGeom="select ll.user_id from locks_feature ll,"+sTable+" t " +
                                   "where ll.municipio=? and ll.layer=? and t.id=ll.feature_id " +
                                   " and t.id_municipio=? ";
                   if (geom!=null)
                       sSQLGeom+=" and (sdo_relate(t.geometry,?, 'mask=anyinteract querytype=window') = 'TRUE')";
                   ps=conn.prepareStatement(sSQLGeom);
                   ps.setInt(1,iMunicipio);
                   ps.setString(2,sLayer);
                   ps.setInt(3,iMunicipio);
                   if (geom!=null){
                     ps.setObject(4,getGeoObject(iMunicipio,geom,conn) );
                   }
                   rs=ps.executeQuery();
                   if (rs.next() && rs.getInt("user_id")!=iUser)
                       iRet=AdministradorCartografiaClient.LOCK_FEATURE_OTHER;
               }
               catch(Exception se){
                iRet=AdministradorCartografiaClient.LOCK_LAYER_ERROR;
                logger.error("canLockLayer: " +se.getMessage());
                logger.error("canLockLayerr(int, String, int, Geometry)", se);
               }finally{
                   try{rs.close();}catch(Exception e){};
                   try{ps.close();}catch(Exception e){};
                   try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
               }
               return iRet;
       }
    /** devuelve -1 si no bloqueado, id_usuario si hay bloqueo */
     private int layerLocked(int iMunicipio, String sLayer, Geometry geom) {
         int iRet=-1;
         String sSQL="select user_id,ts from locks_layer " +
                     "where " +
                         "municipio=? and " +
                         "layer=? " +
                         (geom!=null? "and " +
                   " (sdo_relate(geometry,?, 'mask=anyinteract querytype=window') = 'TRUE')"
                                    : "");
         Connection conn=null;
         PreparedStatement ps=null;
         ResultSet rs=null;
         try{
             conn=openConnection();
             ps=conn.prepareStatement(sSQL);
             ps.setInt(1,iMunicipio);
             ps.setString(2,sLayer);
             if (geom!=null){
                 ps.setObject(3, getGeoObject(iMunicipio,geom,conn));
             }

             rs=ps.executeQuery();
             if (rs.next()){
                 iRet=rs.getInt("user_id");
             }
         }catch(Exception se){
                iRet=AdministradorCartografiaClient.LOCK_LAYER_ERROR;
                logger.error("lockLayer: " +se.getMessage());
                logger.error("lockLayer(int, String, int, Geometry)", se);

         }finally{
             try{rs.close();}catch(Exception e){};
             try{ps.close();}catch(Exception e){};
             try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
         }
         return iRet;
     }


     public int unlockLayer(int iMunicipio, String sLayer, int iUser){
        int iRet=AdministradorCartografiaClient.UNLOCK_LAYER_UNLOCKED;
        String sSQL="delete from locks_layer where " +
                                            "municipio=? and " +
                                            "layer=? and " +
                                            "user_id=?";
        Connection conn=null;
        PreparedStatement ps=null;
        try{
            //int iLocked=layerLocked(iMunicipio,iLayer,null);
            //if (iLocked==-1){
            //    iRet=AdministradorCartografiaClient.UNLOCK_LAYER_NOTLOCKED;
            //}else{
                conn=openConnection();
                ps=conn.prepareStatement(sSQL);
                ps.setInt(1,iMunicipio);
                ps.setString(2,sLayer);
                ps.setInt(3,iUser);
                iRet=ps.executeUpdate();
            //}
        }catch(SQLException se){
            iRet=AdministradorCartografiaClient.UNLOCK_LAYER_ERROR;
            logger.error("unlockLayer: " +se.getMessage());
            logger.error("unlockLayer(int, String, int)", se);
        }finally{
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return iRet;
    }
//       public int lockFeature(int iMunicipio, String sLayer, int iFeatureId, int iUserId) throws Exception{
//        int iRet=AdministradorCartografiaClient.LOCK_FEATURE_LOCKED;
//        String sSQL="insert into locks_feature (municipio,layer,feature_id,user_id,ts) "
//                   +"values (?,?,?,?,?)";
//        Connection conn=null;
//        PreparedStatement ps=null;
//        try{
//            iRet=canLockFeature(iMunicipio,sLayer,iFeatureId,iUserId);
//            if (iRet==0){
//                conn=openConnection();
//                ps=conn.prepareStatement(sSQL);
//                ps.setInt(1,iMunicipio);
//                ps.setString(2,sLayer);
//                ps.setInt(3,iFeatureId);
//                ps.setInt(4,iUserId);
//                ps.setTimestamp(5,new Timestamp(System.currentTimeMillis()));
//                ps.executeUpdate();
//            }
//        }catch(SQLException se){
//            iRet=AdministradorCartografiaClient.LOCK_FEATURE_ERROR;
//            logger.error("lockFeature: " +se.getMessage());
//            logger.error("lockFeature(int, String, int, int)", se);
//        }finally{
//            try{ps.close();}catch(Exception e){};
//            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
//        }
//        return iRet;
//    }
//         public int unlockFeature(int iMunicipio, String sLayer, int iFeature, int iUser){
//        int iRet=AdministradorCartografiaClient.UNLOCK_FEATURE_UNLOCKED;
//        String sSQL="delete from locks_feature where " +
//                                            "municipio=? and " +
//                                            "layer=? and " +
//                                            "user_id=? and " +
//                                            "feature_id=?";
//        Connection conn=null;
//        PreparedStatement ps=null;
//        try{
//           int iLocked=featureLocked(iMunicipio,sLayer,iFeature);
//           if (iLocked==-1){
//               iRet=AdministradorCartografiaClient.UNLOCK_FEATURE_NOTLOCKED;
//           }else{
//                conn=openConnection();
//                ps=conn.prepareStatement(sSQL);
//                ps.setInt(1,iMunicipio);
//                ps.setString(2,sLayer);
//                ps.setInt(3,iUser);
//                ps.setInt(4,iFeature);
//                iRet=ps.executeUpdate();
//           }
//        }catch(SQLException se){
//            iRet=AdministradorCartografiaClient.UNLOCK_FEATURE_ERROR;
//            logger.error("unlockFeature: " +se.getMessage());
//            logger.error("unlockFeature(int, String, int, int)", se);
//        }finally{
//            try{ps.close();}catch(Exception e){};
//            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
//        }
//        return iRet;
//    }
        public Geometry municipioGeometry(int iMunicipio)
        {
            Geometry gRet=null;
            String sSQL="select GEOMETRY from municipios where id=? and GEOMETRY is not null";
            Connection conn=null;
            PreparedStatement ps=null;
            ResultSet rs=null;
            try{

                conn=openConnection();
                ps=conn.prepareStatement(sSQL);
                SRManager manager = OraSpatialManager.getSpatialReferenceManager((oracle.jdbc.OracleConnection)((org.enhydra.jdbc.core.CoreConnection)conn).con);
                SpatialReference sr= manager.retrieve(srid.getSRID(iMunicipio));
                GeometryFactory gFact= OraSpatialManager.getGeometryFactory(sr);

                AdapterSDO adaptersdo= new AdapterSDO(gFact,(oracle.jdbc.OracleConnection) ((org.enhydra.jdbc.core.CoreConnection)conn).con);
                AdapterJTS adapterJTS= new AdapterJTS(gFact);

                ps.setInt(1,iMunicipio);
                rs=ps.executeQuery();
                if (rs.next() && rs.getObject("GEOMETRY")!=null){
                    com.vividsolutions.jts.geom.Geometry jts  =(com.vividsolutions.jts.geom.Geometry) adapterJTS.exportGeometry(com.vividsolutions.jts.geom.Geometry.class,
                    adaptersdo.importGeometry(rs.getObject("GEOMETRY")));
                    String sGeom=jts.toText();
                    if (logger.isDebugEnabled())
                    {
                         logger.debug("municipioGeometry(int)" + sGeom);
                    }
                    try{
                        Feature f=(Feature)new com.vividsolutions.jump.io.WKTReader().read(new StringReader(sGeom)).getFeatures().get(0);
                        gRet=f.getGeometry();
                    }catch (Exception e){
                       logger.error("municipioGeometry(int)", e);
                    }
                }
        } catch(SQLException se){
            logger.error("municipioGeometry: " +se.getMessage());
            logger.error("municipioGeometry(int)", se);
        }catch(Exception ex){
            logger.error("municipioGeometry: " +ex.getMessage());
            logger.error("municipioGeometry(int)", ex);
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return gRet;
        }
        public void returnMap(int iMunicipio,int iID, String sLocale, ObjectOutputStream oos,ISesion sesion)throws IOException,SQLException,Exception{
        try{
            oos.writeObject(loadMap(iID,sLocale,sesion,false,null));
        }catch(PermissionException pe)
        {
            logger.error("Error en permisos", pe);
            throw pe;
        }catch(Exception e){
            logger.error("returnMap: " + e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
    }
        public void returnMaps(String sLocale,ISesion sesion, ObjectOutputStream oos)throws IOException,SQLException,Exception{
        try{
            for (Iterator it=loadMaps(sLocale, sesion).iterator();it.hasNext();){
                oos.writeObject((ACMap)it.next());
            }
        }catch(Exception e){
            logger.error("Carga de mapas: "+ e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }
        }
         public void returnLayerFamilyIDsByMap(int iMap, ObjectOutputStream oos)throws IOException,SQLException,Exception{
        String sSQL="select id_layerfamily from maps_layerfamilies_relations where id_map=? order by \"position\"";
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            conn=openConnection();
            ps=conn.prepareStatement(sSQL);
            ps.setInt(1,iMap);
            rs=ps.executeQuery();
            ArrayList alIDs=new ArrayList();
            for (;rs.next();)
                alIDs.add(new Integer(rs.getInt("id_layerfamily")));
            int[] aiIDs=new int[alIDs.size()];
            for (int i=0;i<aiIDs.length;i++)
                aiIDs[i]=((Integer)alIDs.get(i)).intValue();
            oos.writeObject(aiIDs);
        }catch(Exception e){
            logger.error("returnLayerFamilyIDsByMap(int, ObjectOutputStream)", e);
            logger.error("returnLayerFamilyIDsByMap: "+e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
        }
    }

    public int insertFeature(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload upload, ObjectOutputStream oos,ISesion sesion) throws Exception{
        int iRet=-1;
        ACLayer acLayer=null;
        /** Comentado por Incidencia [365] */
        /*
        String sSQL="select q.id,q.selectquery,q.updatequery,q.insertquery,q.deletequery,"
                          +"l.name,l.acl,l.id_layer,d.traduccion "
                   +"from queries q,layers l, dictionary d "
                   +"where q.databasetype="+DATABASETYPE+" and l.name=? and "
                         +"q.id_layer=l.id_layer and  "
                         +"l.id_name=d.id_vocablo and "
                         +"d.locale=?";
        */
        /** Incidencia [365] El administrador de cartografia no funciona para otros idiomas */
        String sSQL="select q.id,q.selectquery,q.updatequery,q.insertquery,q.deletequery,"
                          +"l.name,l.acl,l.id_layer,d.traduccion,d.locale "
                   +"from queries q,layers l, dictionary d "
                   +"where q.databasetype="+DATABASETYPE+" and l.name=? and "
                         +"q.id_layer=l.id_layer and  "
                         +"l.id_name=d.id_vocablo ";// and ";
                         //+"(d.locale=? or d.locale='es_ES' or d.locale='ca_ES' or d.locale='va_ES' or d.locale='gl_ES' or d.locale='eu_ES')";

        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            conn=openConnection();
            ps=conn.prepareStatement(sSQL);
            ps.setString(1,sLayer);
            //ps.setString(2,sLocale);
            rs=ps.executeQuery();

            if (rs.next()){
                /** Comentado por Incidencia [365] */
                /*
                acLayer=new ACLayer(rs.getInt("id_layer"),rs.getString("traduccion"),rs.getString("name"),replaceSRID(rs.getString("selectquery"),iMunicipio));
                String sInsertSQL=rs.getString("insertquery");
                String sSelectSQL=rs.getString("selectquery");
                acLayer.setACL(rs.getLong("acl"));
                */
                /** Incidencia [365] */
                String sInsertSQL= "";
                String sSelectSQL= "";
                int iCurrentLayerLocale= -1;
                /** aseguramos que entra la primera vez */
                int iLastLayerLocale=4;
                boolean mejorOpcion= false;

                do{
                    iCurrentLayerLocale= getOpcionLocale(rs.getString("locale"), sLocale);
                    /** Priorizamos los idiomas: preferencia de usuario, es_ES, resto de idiomas */
                    if (iCurrentLayerLocale < iLastLayerLocale){
                        acLayer=new ACLayer(rs.getInt("id_layer"),rs.getString("traduccion"),rs.getString("name"),replaceSRID(rs.getString("selectquery"),iMunicipio));
                        sInsertSQL=rs.getString("insertquery");
                        sSelectSQL=rs.getString("selectquery");
                        acLayer.setACL(rs.getLong("acl"));

                        iLastLayerLocale= iCurrentLayerLocale;
                        if (iCurrentLayerLocale == 1) mejorOpcion= true;
                    }
                }while ((rs.next()) && (!mejorOpcion));


              //Comprobamos si tiene permiso para poder realizar la operacion
    			GeopistaAcl acl=getPermission(sesion,acLayer.getACL(),conn);

                /**/
    			readNewSchema(acLayer,sLocale, conn, sesion,acl);
                rs.close();
                ps.close();

            	//Comprobamos si tiene permiso para poder realizar la operacion
    			if (!checkPerm(sesion,acl,Const.PERM_LAYER_ADD))
                    throw new PermissionException(Const.PERM_LAYER_ADD);



    			/*Bloque que se añadiría si tuviera parámetro booleano bValidateData como en postgis
    			 * ArrayList lstValidateFeatures = null;
                if (upload!= null && bValidateData)
                {
               		lstValidateFeatures = getValidateFeatures(acLayer,sLocale);
                }

                if (lstValidateFeatures != null){
               		for (Iterator iterValidateFeatures = lstValidateFeatures.iterator();iterValidateFeatures.hasNext();){
               			AbstractValidator validateFeatures = (AbstractValidator)iterValidateFeatures.next();
               			validateFeatures.beforeInsert(upload,acLayer);
               		}
               	}*/






                //int iLock=canLockFeature(iMunicipio,sLayer,(String)upload.getAttValues()[upload.getGeometryIndex()],Integer.parseInt(sesion.getIdUser()));
                //if (iLock!=AdministradorCartografiaClient.LOCK_LAYER_OWN)
                //    throw new LockException("Lock_layer: municipio: "+iMunicipio + " layer: "+sLayer);

                //REFACTORIZACION ORACLE SQLParser sqlParser=new SQLParser(conn,srid);
                SQLParserOracle sqlParser=new SQLParserOracle(conn,srid);
                conn.setAutoCommit(false);

                String sTable=acLayer.findPrimaryTable();

                //Si la feature que me llega tiene el SistemID se lo pongo como clave primaria sino
                //le pongo nextval
                String sSelectSQLId=null;
                if (sInsertSQL.indexOf("?PK")>=0)
                {
                      try{
                            new Long(upload.getId());
                            sInsertSQL=SQLParser.replaceString(sInsertSQL,"?PK"," '"+upload.getId()+"' ");
                            sSelectSQLId = upload.getId();
                      }catch (Exception e)
                      {
                              sInsertSQL = SQLParser.replaceString(sInsertSQL,"?PK"," SEQ_"+sTable.toUpperCase() +".nextval ");

                      }
                }
                ///
                PreparedStatement[] apsUpdate=sqlParser.newUpdateOracle(iMunicipio,sInsertSQL,acLayer,upload);
                for(int i=0;i<apsUpdate.length;i++){
                    apsUpdate[i].executeUpdate();
                    apsUpdate[i].close();
                }
                conn.commit();
                if (sSelectSQLId == null)
                {
                    PreparedStatement psCurrVal=conn.prepareStatement("select SEQ_"+sTable.toUpperCase() +".currval from dual");
                    ResultSet rsCurrVal= psCurrVal.executeQuery();
                    if (rsCurrVal.next())
                        sSelectSQLId=rsCurrVal.getString(1);
                }
                iRet=returnFeatures(iMunicipio,sSelectSQL
                                  + " and "+sTable+".ID="+sSelectSQLId,acLayer,null,null,oos,conn,sesion, acl);
            } else
                throw new ObjectNotFoundException("layer: "+sLayer+" ("+sLocale+")");
            oos.flush();
        }catch(Exception e){
            try{conn.rollback();}catch(Exception ex){};
            logger
                    .error(
                            "insertFeature(int, String, String, ACFeatureUpload, ObjectOutputStream, Sesion)",
                            e);
            logger.error("insertFeature: "+e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return iRet;
    }


    /**
     * Comprueba si un mapa esta publicado en la guia urbana antigua o en la nueva.
     * @param id
     * @return
     * @throws SQLException
     */
    private boolean isPublishedMap(int id,Connection connection) throws SQLException{

    	String sec1 = "select mapid from localgisguiaurbana.map where mapidgeopista=?";
    	Connection conn=null;
        PreparedStatement preparedStmt=null;
        ResultSet rSet=null;
        boolean bCerrarConexion=true;
        try {
        	if (connection!=null)
            {
                conn=connection;
                bCerrarConexion=false;
            }
            else
            {
            	conn=openConnection();
                bCerrarConexion=true;
            }
        	//Connection conn = openConnection();
        	preparedStmt=conn.prepareStatement(sec1);
        	preparedStmt.setInt(1,id);
	        rSet = preparedStmt.executeQuery();
	        if (rSet.next())
	        	return true;
        } catch (Exception e) {
        	logger.error("isPublishedMap: "+ e.getMessage());
        } finally {
	        try{rSet.close();}catch(Exception e){};
	        try{preparedStmt.close();}catch(Exception e){};
	        //try{conn.close();}catch(Exception e){};
	        try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e1){};
	    }
        return false;
    }



         public void log(int iMunicipio, String sLayer, int iUserID, int iFeature, int iAction)throws SQLException{
        String sSQL="insert into history (municipio,layer,feature,user_id,ts,\"action\") " +
                    "values (?,?,?,?,?,?)";
        Connection conn=null;
        PreparedStatement ps=null;
        try{
            conn=openConnection();
            ps=conn.prepareStatement(sSQL);
            ps.setInt(1,iMunicipio);
            ps.setString(2,sLayer);
            ps.setInt(3,iFeature);
            ps.setInt(4,iUserID);
            ps.setTimestamp(5,new Timestamp(System.currentTimeMillis()));
            ps.setInt(6,iAction);
            ps.executeUpdate();
        }catch(SQLException e){
            logger.error("log(int, String, int, int, int)", e);
            logger.error("log: "+e.getMessage());
            //try{oos.writeObject(new ACException(e));}catch(Exception ex){};
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
    }

    public int updateFeature(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload upload, boolean bDelete, ObjectOutputStream oos, ISesion sesion)throws Exception{
        int iRet=-1;
        ACLayer acLayer=null;
        /** Comentado por Incidencia [365] */
        /*
        String sSQL="select q.id,q.selectquery,q.updatequery,q.insertquery,q.deletequery,"
                          +"l.id_layer,l.name,l.acl,d.traduccion "
                   +"from queries q,layers l, dictionary d "
                   +"where q.databasetype="+DATABASETYPE+" and l.name=? and "
                         +"q.id_layer=l.id_layer and  "
                         +"l.id_name=d.id_vocablo and "
                         +"d.locale=?";
        */

        String sSQL="select q.id,q.selectquery,q.updatequery,q.insertquery,q.deletequery,"
                          +"l.id_layer,l.name,l.acl,d.traduccion,d.locale "
                   +"from queries q,layers l, dictionary d "
                   +"where q.databasetype="+DATABASETYPE+" and l.name=? and "
                         +"q.id_layer=l.id_layer and  "
                         +"l.id_name=d.id_vocablo ";// and ";
                        // +"(d.locale=? or d.locale='es_ES' or d.locale='ca_ES' or d.locale='va_ES' or d.locale='gl_ES' or d.locale='eu_ES')";

        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            conn=openConnection();
            ps=conn.prepareStatement(sSQL);
            ps.setString(1,sLayer);
            //ps.setString(2,sLocale);
            rs=ps.executeQuery();

            if (rs.next()){
                /** Comentado por Incidencia [365] */
                /*
                acLayer=new ACLayer(rs.getInt("id_layer"),rs.getString("traduccion"),rs.getString("name"),replaceSRID(rs.getString("selectquery"),iMunicipio));
                acLayer.setACL(rs.getLong("acl"));
                sSQL=rs.getString(bDelete?"deletequery":"updatequery");
                */

                /** Incidencia [365] */
                int iCurrentLayerLocale= -1;
                /** aseguramos que entra la primera vez */
                int iLastLayerLocale=4;
                boolean mejorOpcion= false;

                do{
                    iCurrentLayerLocale= getOpcionLocale(rs.getString("locale"), sLocale);
                    /** Priorizamos los idiomas: preferencia de usuario, es_ES, resto de idiomas */
                    if (iCurrentLayerLocale < iLastLayerLocale){
                        acLayer=new ACLayer(rs.getInt("id_layer"),rs.getString("traduccion"),rs.getString("name"),replaceSRID(rs.getString("selectquery"),iMunicipio));
                        acLayer.setACL(rs.getLong("acl"));
                        sSQL=rs.getString(bDelete?"deletequery":"updatequery");

                        iLastLayerLocale= iCurrentLayerLocale;
                        if (iCurrentLayerLocale == 1) mejorOpcion= true;
                    }
                }while ((rs.next()) && (!mejorOpcion));
                /**/

                rs.close();
                ps.close();
                //readSchema(acLayer,sLocale,sesion);

            	GeopistaAcl acl=getPermission(sesion,acLayer.getACL(),conn);

                readNewSchema(acLayer,sLocale, conn, sesion,acl);

            	//Comprobamos si tiene permiso para poder realizar la operacion
    			if (!checkPerm(sesion,acl,Const.PERM_LAYER_WRITE))
    				throw new PermissionException(Const.PERM_LAYER_WRITE);



    			/*Pondríamos esto si tuviéramos el parámetro bValidateData en el método (como tenemos en el caso de postgis)...
    			 *
    			ArrayList lstValidateFeatures = null;
                if (upload!= null && bValidateData )
                {
               		lstValidateFeatures = getValidateFeatures(acLayer,sLocale);
                }

                if (lstValidateFeatures != null){
               		for (Iterator iterValidateFeatures = lstValidateFeatures.iterator();iterValidateFeatures.hasNext();){
               			AbstractValidator validateFeatures = (AbstractValidator)iterValidateFeatures.next();
               			if (bDelete){
               				validateFeatures.beforeDelete(upload, acLayer);
               			}
               			else{
               				validateFeatures.beforeUpdate(upload, acLayer);
               			}
               		}
               	}
               	*/



                /*if (!checkPermission(sesion,acLayer.getACL(),Const.PERM_LAYER_WRITE))
                    throw new PermissionException(Const.PERM_LAYER_WRITE);*/

                int iLock=canLockFeature(iMunicipio,sLayer,Integer.parseInt(upload.getId()),Integer.parseInt(sesion.getIdUser()),conn);
                if (!(iLock==AdministradorCartografiaClient.LOCK_FEATURE_OWN ||
                      iLock==AdministradorCartografiaClient.LOCK_LAYER_OWN   ||
                      iLock==AdministradorCartografiaClient.LOCK_LAYER_LOCKED)){
                    String sMsg=null;
                    switch (iLock){
                        case AdministradorCartografiaClient.LOCK_LAYER_OTHER:
                            sMsg="locked: layer "+sLayer;
                            break;
                        case AdministradorCartografiaClient.LOCK_FEATURE_OTHER:
                            sMsg="locked: feature";
                            break;
                        default:
                            sMsg="Lock error";
                    }
                    throw new LockException(sMsg+" ("+iLock+")");
                }
                conn.setAutoCommit(false);
                //REFACTORIZACION ORACLE PreparedStatement[] apsUpdate=new SQLParser(conn,srid).newUpdateOracle(iMunicipio,sSQL,acLayer,upload);
                PreparedStatement[] apsUpdate=new SQLParserOracle(conn,srid).newUpdateOracle(iMunicipio,sSQL,acLayer,upload);

                for(int i=0;i<apsUpdate.length;i++){
                    apsUpdate[i].executeUpdate();
                    apsUpdate[i].close();
                }
                conn.commit();
                String sTable=acLayer.findPrimaryTable();
                if (!bDelete)
                   iRet=returnFeatures(iMunicipio,acLayer.getSelectQuery()
                                     + " and "+sTable+".ID="+upload.getAttValues()[acLayer.findID().getPosition()-1],acLayer,null,null,oos,conn,sesion,null);



                /* Pondríamos esto si tuviéramos el parámetro bValidateData en el método (como tenemos en el caso de postgis)...
                 * if (lstValidateFeatures != null){
            		for (Iterator iterValidateFeatures = lstValidateFeatures.iterator();iterValidateFeatures.hasNext();){
            			AbstractValidator validateFeatures = (AbstractValidator)iterValidateFeatures.next();
            			if (bDelete){
            				validateFeatures.afterDelete(upload,acLayer);
            			}
            			else{
            				validateFeatures.afterUpdate(upload,acLayer);
            			}
            		}
            	}
                */



            } else
                throw new ObjectNotFoundException("layer: "+sLayer+" ("+sLocale+")");
            //oos.writeInt(iRet);  angeles lo quita porque cree que va a funcionar mejor
            oos.flush();
            if(upload.getAttValues()[acLayer.findID().getPosition()-1] instanceof String)
            {
                String iRetString =  (String) upload.getAttValues()[acLayer.findID().getPosition()-1];
                iRet = Integer.parseInt(iRetString);
            }
            else
            {
                iRet=((Number)upload.getAttValues()[acLayer.findID().getPosition()-1]).intValue();
            }
        }catch(Exception e){
            try{conn.rollback();}catch(Exception ex){};
            logger.error("insertFeature: "+e.getMessage());
            logger.error("updateFeature(int, String, String, ACFeatureUpload, boolean, ObjectOutputStream, Sesion)",
                            e);
            try{oos.writeObject(new ACException(e));}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return iRet;
    }
    public int procesarLoteActualizacion(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload[] aUpload, ObjectOutputStream oos, ISesion sesion, int iUserID, boolean bLoadData)throws Exception{
                int iRet=-1;
                boolean nextvalCalled=false;

                ACLayer acLayer=null;
                //Primero obtenemos las querys de la base de datos
                /* Comentado por Incidencia [365] */
                /*
                String sSQL="select q.id,q.selectquery,q.updatequery,q.insertquery,q.deletequery,"
                                  +"l.id_layer,l.name,l.acl,d.traduccion "
                           +"from queries q,layers l, dictionary d "
                           +"where q.databasetype="+DATABASETYPE+" and l.name=? and "
                                 +"q.id_layer=l.id_layer and  "
                                 +"l.id_name=d.id_vocablo and "
                                 +"d.locale=?";
                */

                String sSQL="select q.id,q.selectquery,q.updatequery,q.insertquery,q.deletequery,"
                                  +"l.id_layer,l.name,l.acl,d.traduccion,d.locale "
                           +"from queries q,layers l, dictionary d "
                           +"where q.databasetype="+DATABASETYPE+" and l.name=? and "
                                 +"q.id_layer=l.id_layer and  "
                                 +"l.id_name=d.id_vocablo ";// and ";
                                // +"(d.locale=?  or d.locale='es_ES' or d.locale='ca_ES' or d.locale='va_ES' or d.locale='gl_ES' or d.locale='eu_ES')";

                Connection conn=null;
                PreparedStatement ps=null;
                ResultSet rs=null;
                try{
                    conn=openConnection();
                    ps=conn.prepareStatement(sSQL);
                    ps.setString(1,sLayer);
                   // ps.setString(2,sLocale);
                    rs=ps.executeQuery();

                    if (rs.next()){
                        /** Comentado por Incidencia [365] */
                        /*
                        acLayer=new ACLayer(rs.getInt("id_layer"),rs.getString("traduccion"),rs.getString("name"),replaceSRID(rs.getString("selectquery"),iMunicipio));
                        acLayer.setACL(rs.getLong("acl"));
                        String sSQLDelete=rs.getString("deletequery");
                        String sSQLUpdate=rs.getString("updatequery");
                        String sSQLInsert=rs.getString("insertquery");
                        */

                        /** Incidencia [365] El administrador de cartografia no funciona para otros idiomas */
                        String sSQLDelete= "";
                        String sSQLUpdate= "";
                        String sSQLInsert= "";

                        int iCurrentLayerLocale= -1;
                        /** aseguramos que entra la primera vez */
                        int iLastLayerLocale=4;
                        boolean mejorOpcion= false;

                        do{
                            iCurrentLayerLocale= getOpcionLocale(rs.getString("locale"), sLocale);
                            /** Priorizamos los idiomas: preferencia de usuario, es_ES, resto de idiomas */
                            if (iCurrentLayerLocale < iLastLayerLocale){
                                acLayer=new ACLayer(rs.getInt("id_layer"),rs.getString("traduccion"),rs.getString("name"),replaceSRID(rs.getString("selectquery"),iMunicipio));
                                acLayer.setACL(rs.getLong("acl"));
                                sSQLDelete=rs.getString("deletequery");
                                sSQLUpdate=rs.getString("updatequery");
                                sSQLInsert=rs.getString("insertquery");

                                iLastLayerLocale= iCurrentLayerLocale;
                                if (iCurrentLayerLocale == 1) mejorOpcion= true;
                            }
                        }while ((rs.next()) && (!mejorOpcion));
                        /**/
                        rs.close();
                        ps.close();
                       // readSchema(acLayer,sLocale,sesion);
                        //PreparedStatement[] apsUpdate=null;
                        GeopistaAcl acl=SesionUtils_LCGIII.getPerfil(sesion,acLayer.getACL(),conn);
                        readNewSchema(acLayer,sLocale, conn, sesion,acl);
                        int[] arlFeatureIDs = new int[aUpload.length];


                        /* Bloque que se añadiría si recibiéramos la variable booleana bValidateData como en postgres
                         * ArrayList lstValidateFeatures = null;

                    if (bValidateData)
                    {
                   		lstValidateFeatures = getValidateFeatures(acLayer,sLocale);
                    }
                    */



                        for (int i=0;i<aUpload.length;i++){
                            if (aUpload[i].isNew())
                            {
                                 if (!checkPermission(sesion,acl,Const.PERM_LAYER_ADD))
                                throw new PermissionException(Const.PERM_LAYER_ADD);
                            }else
                            {
                                if (!checkPermission(sesion,acl,Const.PERM_LAYER_WRITE))
                                throw new PermissionException(Const.PERM_LAYER_WRITE);
                                int iLock=canLockFeature(iMunicipio,sLayer,Integer.parseInt(aUpload[i].getId()),Integer.parseInt(sesion.getIdUser()),conn);
                                if (!(iLock==AdministradorCartografiaClient.LOCK_FEATURE_OWN ||
                                      iLock==AdministradorCartografiaClient.LOCK_LAYER_OWN   ||
                                      iLock==AdministradorCartografiaClient.LOCK_LAYER_LOCKED)){
                                           String sMsg=null;
                                    switch (iLock){
                                        case AdministradorCartografiaClient.LOCK_LAYER_OTHER:
                                            sMsg="locked: layer "+sLayer;
                                            break;
                                        case AdministradorCartografiaClient.LOCK_FEATURE_OTHER:
                                            sMsg="locked: feature";
                                            break;
                                        default:
                                            sMsg="Lock error";
                                    }
                                    throw new LockException(sMsg+" ("+iLock+")");
                               }
                            }





                            /* Bloque que se añadiría si recibiéramos la variable booleana bValidateData como en postgres
                            if (lstValidateFeatures != null){
                           		for (Iterator iterValidateFeatures = lstValidateFeatures.iterator();iterValidateFeatures.hasNext();){
                           			AbstractValidator validateFeatures = (AbstractValidator)iterValidateFeatures.next();
                           			validateFeatures.beforeUpdate(aUpload[i],acLayer);
                           		}
                           	}
                           	*/





                            conn.setAutoCommit(false);
                            sSQL=(aUpload[i].isNew()?sSQLInsert:aUpload[i].isDeleted()?sSQLDelete:sSQLUpdate);
                            String sTable=acLayer.findPrimaryTable();
                            String sSelectSQLId = " and "+sTable+".ID=currval('SEQ_"+sTable.toUpperCase() +"')";
                            if (sSQL.indexOf("?PK")>=0)
                            {
                                try
                                {
                                    new Long(aUpload[i].getId());
                                    sSQL=SQLParser.replaceString(sSQL,"?PK"," '"+aUpload[i].getId()+"' ");
                                    sSelectSQLId = " and "+sTable+".ID="+aUpload[i].getId();
                                }catch (Exception e)
                                {
                                    sSQL=SQLParser.replaceString(sSQL,"?PK"," SEQ_"+sTable.toUpperCase() +".nextval ");
                                    nextvalCalled=true;
                                }
                            }



                            //REFACTORIZACION ORACLE PreparedStatement[] apsUpdate=new SQLParser(conn,srid).newUpdateOracle(iMunicipio,sSQL,acLayer,aUpload[i]);
                            PreparedStatement[] apsUpdate=new SQLParserOracle(conn,srid).newUpdateOracle(iMunicipio,sSQL,acLayer,aUpload[i]);

                            //apsUpdate=new SQLParser(conn,srid).newUpdate(iMunicipio,sSQL,acLayer,aUpload[i],apsUpdate);

                            for(int j=0;j<apsUpdate.length;j++){
                                apsUpdate[j].executeUpdate();
                                apsUpdate[j].close();
                                // apsUpdate[j].addBatch();
                            }
                            conn.commit();
                            //String sTable=acLayer.findPrimaryTable();
                            if (aUpload[i].isNew()&& bLoadData)
                            {
                            	if(!nextvalCalled){
                            		PreparedStatement psNextVal=conn.prepareStatement("select SEQ_"+sTable.toUpperCase() +".nextval from dual");
                            		psNextVal.executeQuery();
                            	}//fin if


                            	PreparedStatement psCurrVal=conn.prepareStatement("select SEQ_"+sTable.toUpperCase() +".currval as ID from dual");
                                    ResultSet rsCurrVal= psCurrVal.executeQuery();

                                    if (rsCurrVal.next()){
                                        iRet = ((Number)rsCurrVal.getObject("ID")).intValue();
                                    }

                                    rsCurrVal.close();
                                    psCurrVal.close();

                                    arlFeatureIDs[i] = iRet;

                                    if (i>=(aUpload.length-1)){
                                		iRet = returnAllFeatures(iMunicipio,sTable, acLayer, arlFeatureIDs, oos,conn,sesion,null);
                                	}
                               }
                               else if (!aUpload[i].isDeleted() && bLoadData)
                               {
                                iRet=returnFeatures(iMunicipio,acLayer.getSelectQuery()
                                             + " and "+sTable+".ID="+aUpload[i].getAttValues()[acLayer.findID().getPosition()-1],acLayer,null,null,oos,conn,sesion,null);
                                iRet=((Number)aUpload[i].getAttValues()[acLayer.findID().getPosition()-1]).intValue();
                            }
                            oos.flush();
                            if (iRet!=-1)
                                log(iMunicipio,sLayer,iUserID,iRet,aUpload[i].isNew()?Const.HISTORY_ACTION_INSERT:aUpload[i].isDeleted()?Const.HISTORY_ACTION_DELETE:Const.HISTORY_ACTION_UPDATE);
                        }
                        //for(int j=0;j<apsUpdate.length;j++){
                        //        apsUpdate[j].executeBatch();
                        //        apsUpdate[j].close();
                        //}
                        //conn.commit();


                        /* Bloque que se añadiría si recibiéramos la variable booleana bValidateData como en postgres
                        if (lstValidateFeatures != null){
                       		for (Iterator iterValidateFeatures = lstValidateFeatures.iterator();iterValidateFeatures.hasNext();){
                       			AbstractValidator validateFeatures = (AbstractValidator)iterValidateFeatures.next();
                       			validateFeatures.afterUpdate(aUpload[i],acLayer);
                       		}
                       	}
                       	*/


                   } else
                        throw new ObjectNotFoundException("layer: "+sLayer+" ("+sLocale+")");

                }catch(Exception e){
                    try{conn.rollback();}catch(Exception ex){};
                    logger.error("insertFeature: "+e.getMessage());
                    logger.error("updateFeature(int, String, String, ACFeatureUpload, boolean, ObjectOutputStream, Sesion)",
                                    e);
                    try{oos.writeObject(new ACException(e));}catch(Exception ex){};
                    throw e;
                }finally{
                    try{rs.close();}catch(Exception e){};
                    try{ps.close();}catch(Exception e){};
                    try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
                }
                return iRet;
            }

         public void updateStyle(String sLayer, String sXML, ObjectOutputStream oos, ISesion sesion) throws Exception{
        String sSQL="select ls.id_style , l.acl "
                  + "from layers_styles ls, layers l  "
                  + "where l.name=? and ls.id_layer= l.id_layer";
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            conn=openConnection();
            ps=conn.prepareStatement(sSQL);
            ps.setString(1,sLayer);
            rs=ps.executeQuery();
            if (rs.next()){
                int iStyleID=rs.getInt("id_style");
                long lACL=rs.getLong("acl");
                rs.close();
                ps.close();
                if (!checkPermission(sesion,lACL,Const.PERM_WRITE_SLD,conn))
                    throw new PermissionException(Const.PERM_WRITE_SLD);
                ps=conn.prepareStatement("update styles set xml=? where id_style=?");
                ps.setString(1,sXML);
                ps.setInt(2,iStyleID);
                ps.executeUpdate();
            }  else
                throw new ObjectNotFoundException("layer: "+sLayer);
        }catch(SQLException e){
            logger.error("updateStyle(String, String, ObjectOutputStream, Sesion)", e);
            logger.error("updateStyle: "+e.getMessage());
            try{oos.writeObject(new ACException(e));}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
    }
       public void returnModifiedFeatureIDs(int iMunicipio,String sLayer, long lDate, String sLocale, ObjectOutputStream oos)throws IOException,SQLException,Exception{
        String sSQL="select feature from history " +
                "where municipio=? and " +
                "layer=? and " +
                "ts>=?";
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            conn=openConnection();
            ps=conn.prepareStatement(sSQL);
            ps.setInt(1,iMunicipio);
            ps.setString(2,sLayer);
            ps.setTimestamp(3,new Timestamp(lDate));
            rs=ps.executeQuery();
            ArrayList alFeatures=new ArrayList();
            for (;rs.next();)
                alFeatures.add(new Integer(rs.getInt("feature")));
            int[] aiFeatures=new int[alFeatures.size()];
            for (int i=0;i<aiFeatures.length;i++)
                aiFeatures[i]=((Integer)alFeatures.get(i)).intValue();
            oos.writeObject(aiFeatures);
        }catch(Exception e){
            logger
                    .error(
                            "returnModifiedFeatureIDs(int, String, long, String, ObjectOutputStream)",
                            e);
            logger.error("returnModifiedFeatureIDs: "+e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
        }
    }
    public void returnLayerFamilyIDs(ObjectOutputStream oos)throws IOException,SQLException,Exception{
                String sSQL="select id_layerfamily from layerfamilies";
                Connection conn=null;
                PreparedStatement ps=null;
                ResultSet rs=null;
                try{
                    conn=openConnection();
                    ps=conn.prepareStatement(sSQL);
                    rs=ps.executeQuery();
                    ArrayList alIDs=new ArrayList();
                    for (;rs.next();)
                        alIDs.add(new Integer(rs.getInt("id_layerfamily")));
                    int[] aiIDs=new int[alIDs.size()];
                    for (int i=0;i<aiIDs.length;i++)
                        aiIDs[i]=((Integer)alIDs.get(i)).intValue();
                    oos.writeObject(aiIDs);
                }catch(Exception e){
                    logger.error("returnLayerFamilyIDs(ObjectOutputStream)", e);
                    logger.error("returnLayerFamilyIDs: "+e.getMessage());
                    oos.writeObject(new ACException(e));
                    throw e;
                }finally{
                    try{rs.close();}catch(Exception e){};
                    try{ps.close();}catch(Exception e){};
                    try{conn.close();}catch(Exception e){};
                }
         }

        public void returnLayerIDsByMap(int iMap,int iPosition,ObjectOutputStream oos,ISesion sesion)throws IOException,SQLException,Exception{
        String sSQL="select l.name,l.acl from maps m, maps_layerfamilies_relations mlr, layerfamilies_layers_relations llr, layers l " +
                    "where " +
                        "m.id_map=mlr.id_map and " +
                        "mlr.id_layerfamily=llr.id_layerfamily and " +
                        "llr.id_layer=l.id_layer and " +
                        "m.id_map=? and " +
                        "mlr.\"position\"=? " +
                    "order by llr.\"position\"";
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            conn=openConnection();
            ps=conn.prepareStatement(sSQL);
            ps.setInt(1,iMap);
            ps.setInt(2,iPosition);
            rs=ps.executeQuery();
            ArrayList alIDs=new ArrayList();
            for (;rs.next();)
                if (checkPermission(sesion,rs.getLong("acl"),Const.PERM_LAYER_READ)){
                    String sName=rs.getString("name");
                    if (sName!=null)
                        alIDs.add(sName);
                }
            String[] asIDs=new String[alIDs.size()];
            for (int i=0;i<asIDs.length;i++)
                asIDs[i]=((String)alIDs.get(i));
            oos.writeObject(asIDs);
        }catch(Exception e){
            logger.error("returnLayerIDsByMap(int, int, ObjectOutputStream, Sesion)", e);
            logger.error("returnLayerFamilyIDsByMap: "+e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();}catch(Exception e){};
        }
    }
    public void returnLayerIDsByFamily(int iLayerFamily,ObjectOutputStream oos,ISesion sesion) throws IOException,SQLException,Exception{
            String sSQL="select l.name,l.acl from layerfamilies_layers_relations llr, layers l " +
                        "where " +
                            "llr.id_layer=l.id_layer and " +
                            "llr.id_layerfamily=? " +
                        "order by llr.\"position\"";
            Connection conn=null;
            PreparedStatement ps=null;
            ResultSet rs=null;
            try{
                conn=openConnection();
                ps=conn.prepareStatement(sSQL);
                ps.setInt(1,iLayerFamily);
                rs=ps.executeQuery();
                ArrayList alIDs=new ArrayList();
                for (;rs.next();)
                    if (checkPermission(sesion,rs.getLong("acl"),Const.PERM_LAYER_READ, conn)){
                        String sName=rs.getString("name");
                        if (sName!=null)
                            alIDs.add(sName);
                    }
                String[] asIDs=new String[alIDs.size()];
                for (int i=0;i<asIDs.length;i++)
                    asIDs[i]=((String)alIDs.get(i));
                oos.writeObject(asIDs);
            }catch(Exception e){
                logger.error("returnLayerIDsByFamily(int, ObjectOutputStream, Sesion)", e);
                logger.error("returnLayerFamilyIDsByFamily: "+e.getMessage());
                oos.writeObject(new ACException(e));
                throw e;
            }finally{
                try{rs.close();}catch(Exception e){};
                try{ps.close();}catch(Exception e){};
                try{conn.close();}catch(Exception e){};
            }
        }

       public void insertMap(ISesion sesion, String sIdMunicipio, ACMap acMap,String sLocale,ObjectOutputStream oos, int id_map,Connection connection,GeopistaAcl acl) throws Exception{

    	 boolean bCerrarConexion=true;

    	String sSQLDict="insert into dictionary (id_vocablo,locale,traduccion) values (?,?,?)";
        String sSQLMaps="insert into maps (id_map,id_name,xml,image, id_municipio) "
                       +"values (?,?,?,?,?)";


        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{

         	if (connection!=null)
            {
                conn=connection;
                bCerrarConexion=false;
            }
            else
            {
            	conn=openConnection();
                bCerrarConexion=true;
            }


        	//conn=openConnection();

            conn.setAutoCommit(false);


            if (acl!=null){
            	if (!checkPerm(sesion,acl,Const.PERM_MAP_CREATE)){
            		throw new PermissionException("PermissionException: " + Const.PERM_MAP_CREATE);
            	}
            }
            else{
            	if (!checkPermission(sesion,Const.ACL_MAP,Const.PERM_MAP_CREATE))
            		throw new PermissionException("PermissionException: " + Const.PERM_MAP_CREATE);
            }

            long idDictionary = CPoolDatabase.getNextValue("dictionary","id_vocablo","SEQ_DICTIONARY",conn);

            ps=conn.prepareStatement(sSQLDict);
            ps.setString(1,new Long(idDictionary).toString());
            ps.setString(2,sLocale);
            ps.setString(3,acMap.getName());
            ps.executeUpdate();
            ps.close();


          //Solucion provisional para el almacenamiento de mapas en idiomas!= es_ES
            if (!sLocale.equals("es_ES"))
            {
                ps=conn.prepareStatement(sSQLDict);
                ps.setString(1,new Long(idDictionary).toString());
                ps.setString(2,"es_ES");
                ps.setString(3,acMap.getName());
                ps.executeUpdate();
                ps.close();
            }
            //

            ps=conn.prepareStatement(sSQLMaps);
            ps.setInt(1,id_map);
            ps.setString(2,new Long(idDictionary).toString());
            ps.setString(3,acMap.getXml());
            ps.setObject(4,acMap.getImage());
            ps.setString(5,sIdMunicipio);
            ps.executeUpdate();

            String sFamiliesSQL="insert into maps_layerfamilies_relations (id_map,id_layerfamily, \"position\", id_municipio) values (?,?,?,?)";
            Integer iPosition=null;
            for (Enumeration e=acMap.getLayerFamilies().keys(); e.hasMoreElements();){
                iPosition=(Integer)e.nextElement();
                ACLayerFamily lf=(ACLayerFamily)acMap.getLayerFamilies().get(iPosition);
                ps=conn.prepareStatement(sFamiliesSQL);
                ps.setInt(1,id_map);
                ps.setInt(2,lf.getId());
                ps.setInt(3,iPosition.intValue());
                ps.setInt(4,Integer.parseInt(sIdMunicipio));
                ps.executeUpdate();
                ps.close();
            }

            String sStylesSQL="insert into layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename," +
                    " \"position\", id_municipio) values (?,?,(select id_layer from layers where name=?),?,?,?,?)";
            for (Iterator it=acMap.getLayerStyles().iterator(); it.hasNext();){
                LayerStyleData lsd=(LayerStyleData)it.next();
                if (lsd.getIdStyle()!=null){
                    ps=conn.prepareStatement(sStylesSQL);
                    ps.setInt(1,id_map);
                    ps.setInt(2,Integer.parseInt(lsd.getIdLayerFamily()));
                    ps.setString(3,lsd.getIdLayer());
                    String sIdStyle=lsd.getIdStyle();
                    if (sIdStyle!=null)
                        ps.setInt(4,Integer.parseInt(sIdStyle));
                    else
                        ps.setNull(4,java.sql.Types.INTEGER);
                    ps.setString(5,lsd.getStyleName());
                    String sPos=lsd.getLayerPosition();
                    if (sPos!=null)
                        ps.setInt(6,Integer.parseInt(sPos));
                    else
                        ps.setNull(6,java.sql.Types.INTEGER);
                    ps.setInt(7,Integer.parseInt(sIdMunicipio));
                    ps.executeUpdate();
                    ps.close();
                }
            }
            //Adaptacion de WMS a Geopista -->
            acMap.setId(Integer.toString(id_map));
            updateMapServerLayer(acMap, sIdMunicipio, conn);
            // <-- Adaptacion de WMS a Geopista

            oos.writeObject(new Integer(id_map));

            conn.commit();
        }catch(SQLException e){
            try{conn.rollback();}catch(Exception ex){};
            logger.error("insertMap(Sesion, ACMap, String, ObjectOutputStream)", e);
            logger.error("insertMap: "+e.getMessage());
            //try{oos.writeObject(new ACException(e));}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e1){};
            //try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
    }



    public void insertMap(ISesion sesion, String sIdMunicipio, ACMap acMap,String sLocale,ObjectOutputStream oos) throws Exception{

       /* if (!checkPermission(sesion,Const.ACL_MAP,Const.PERM_MAP_CREATE))
            throw new PermissionException("PermissionException: " + Const.PERM_MAP_CREATE);*/
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            conn=openConnection();

          //Comprobamos si tiene permiso para poder realizar la operacion
			GeopistaAcl acl=getPermission(sesion,Const.ACL_MAP,conn);
			if (!checkPerm(sesion,acl,Const.PERM_MAP_CREATE))
				throw new PermissionException("PermissionException: " + Const.PERM_MAP_CREATE);

           /* CÓDIGO ANTIGUO SUSTITUÍDO POR EL QUE APARECE A CONTINUACIÓN
            * String sIdSQL="select SEQ_MAPS.nextval as map_id from dual";
            ps=conn.prepareStatement(sIdSQL);
            rs=ps.executeQuery();
            if (rs.next()){
                int iId=rs.getInt("map_id");
                insertMap(sesion,sIdMunicipio,acMap,sLocale,oos,iId);
            }*/



			/*COMIENZO DEL NUEVO BLOQUE DE CÓDIGO
			 */
			long iId=CPoolDatabase.getNextValue("MAPS","id_map","SEQ_MAPS",conn);
			insertMap(sesion,sIdMunicipio,acMap,sLocale,oos,new Long(iId).intValue(),conn,acl);
			/*FIN DEL NUEVO BLOQUE DE CÓDIGO*/

        }catch(SQLException e){
            try{conn.rollback();}catch(Exception ex){};
            logger.error("insertMap(Sesion, ACMap, String, ObjectOutputStream)", e);
            logger.error("insertMap: "+e.getMessage());
            //try{oos.writeObject(new ACException(e));}catch(Exception ex){};
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
    }
    public void updateMap(ISesion sesion, String sIdMunicipio, ACMap acMap,String sLocale,ObjectOutputStream oos) throws Exception{
           /* if (!checkPermission(sesion,Const.ACL_MAP,Const.PERM_MAP_EDIT))
                throw new PermissionException("PermissionException: " + Const.PERM_MAP_EDIT);*/

             /*ACMap tempMap = loadMap(Integer.parseInt(acMap.getId()),sLocale,sesion,false);

             if(tempMap.getIdMunicipio()==0)
             {
                 insertMap(sesion,sIdMunicipio,acMap,sLocale,oos,Integer.parseInt(tempMap.getId()));
                 return;
             }*/
    	  try{
            String sSQL="update maps set xml=?,image=?, id_municipio=? where id_map=?";
            Connection conn=null;
            PreparedStatement ps=null;
            try{
                conn=openConnection();

              //Comprobamos si tiene permiso para poder realizar la operacion
    			GeopistaAcl acl=getPermission(sesion,Const.ACL_MAP,conn);
    			if (!checkPerm(sesion,acl,Const.PERM_MAP_EDIT))
    				throw new PermissionException("PermissionException: " + Const.PERM_MAP_EDIT);

                ACMap tempMap = loadMap(Integer.parseInt(acMap.getId()),sLocale,sesion,false,conn);

                if(tempMap.getIdMunicipio()==0)
                {
                    insertMap(sesion,sIdMunicipio,acMap,sLocale,oos,Integer.parseInt(tempMap.getId()),conn,acl);
                    return;
                }



                conn.setAutoCommit(false);
                ps=conn.prepareStatement(sSQL);
                ps.setString(1,acMap.getXml());
                ps.setObject(2,acMap.getImage());
                ps.setString(3,sIdMunicipio);
                ps.setInt(4,Integer.parseInt(acMap.getId()));
                ps.executeUpdate();
                String[] asSql=new String[]{"delete from maps_layerfamilies_relations where id_map=? and id_municipio = ?"
                                                     ,"delete from layers_styles where id_map=? and id_municipio = ?"
                                                     ,"delete from maps_wms_relations where id_map=? and id_municipio=?"};
                int iId=Integer.parseInt(acMap.getId());
                for (int i=0;i<asSql.length;i++){
                    ps=conn.prepareStatement(asSql[i]);
                    ps.setInt(1,iId);
                    ps.setInt(2,Integer.parseInt(sIdMunicipio));
                    ps.executeUpdate();
                    ps.close();
                }
                String updateDictionary = "Update dictionary set traduccion = ? where dictionary.id_vocablo = (select maps.id_name from maps where maps.id_map = ? and maps.id_municipio = ?) and dictionary.locale = ?";
                ps=conn.prepareStatement(updateDictionary);
                ps.setString(1,acMap.getName());
                ps.setInt(2,iId);
                ps.setInt(3,Integer.parseInt(sIdMunicipio));
                ps.setString(4,sLocale);
                int rows= ps.executeUpdate(); // Incidencia [365] - annadimos rows afectadas en la actualizacion
                ps.close();

                /** Si no se actualiza para locale sLocale, es que no existe la traduccion en el dictionary para ese locale -> hacemos la insercion */
                if (rows == 0){
                    /** No existen traducciones para sLocale - Hacemos la insercion de la traduccion para sLocale. */
                    String insertDictionary="insert into dictionary (traduccion, id_vocablo, locale) values (?,(select maps.id_name from maps where maps.id_map=? and maps.id_municipio=?),?)";
                    ps=conn.prepareStatement(insertDictionary);
                    ps.setString(1,acMap.getName());
                    ps.setInt(2,iId);
                    ps.setInt(3,Integer.parseInt(sIdMunicipio));
                    ps.setString(4,sLocale);
                    ps.executeUpdate();
                    ps.close();
                }

                String sFamiliesSQL="insert into maps_layerfamilies_relations (id_map,id_layerfamily,\"position\", id_municipio) values (?,?,?,?)";
                Integer iPosition=null;
                for (Enumeration e=acMap.getLayerFamilies().keys(); e.hasMoreElements();){
                    iPosition=(Integer)e.nextElement();
                    ACLayerFamily lf=(ACLayerFamily)acMap.getLayerFamilies().get(iPosition);
                    ps=conn.prepareStatement(sFamiliesSQL);
                    ps.setInt(1,iId);
                    ps.setInt(2,lf.getId());
                    ps.setInt(3,iPosition.intValue());
                    ps.setInt(4,Integer.parseInt(sIdMunicipio));
                    ps.executeUpdate();
                    ps.close();
                }
                String sStylesSQL="insert into layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,\"position\",id_municipio) values (?,?,(select id_layer from layers where name=?),?,?,?,?)";
                for (Iterator it=acMap.getLayerStyles().iterator(); it.hasNext();){
                    LayerStyleData lsd=(LayerStyleData)it.next();
                    if (lsd.getIdStyle()!=null){
                        ps=conn.prepareStatement(sStylesSQL);
                        ps.setInt(1,iId);
                        ps.setInt(2,Integer.parseInt(lsd.getIdLayerFamily()));
                        ps.setString(3,lsd.getIdLayer());
                        String sIdStyle=lsd.getIdStyle();
                        if (sIdStyle!=null)
                            ps.setInt(4,Integer.parseInt(sIdStyle));
                        else
                            ps.setNull(4,java.sql.Types.INTEGER);
                        ps.setString(5,lsd.getStyleName());
                        String sPos=lsd.getLayerPosition();
                        if (sPos!=null)
                            ps.setInt(6,Integer.parseInt(sPos));
                        else
                            ps.setNull(6,java.sql.Types.INTEGER);
                        ps.setInt(7,Integer.parseInt(sIdMunicipio));
                        ps.executeUpdate();
                        ps.close();
                    }
                }
                //Adaptacion de WMS a Geopista -->
                updateMapServerLayer(acMap, sIdMunicipio, conn);
                // <-- Adaptacion de WMS a Geopista

                conn.commit();
            }catch(SQLException e){
                try{conn.rollback();}catch(Exception ex){};
                logger.error("updateMap(Sesion, ACMap, String, ObjectOutputStream)", e);
                logger.error("updateMap: "+e.getMessage());
                //try{oos.writeObject(new ACException(e));}catch(Exception ex){};
                throw e;
            }finally{
                try{ps.close();}catch(Exception e){};
                try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
            }
    	  }catch(ObjectNotFoundException e)
          {
              insertMap(sesion,sIdMunicipio,acMap,sLocale,oos);
              return;
          }
        }

    private void updateMapServerLayer(ACMap acMap, String sIdMunicipio, Connection conn) throws Exception{
        PreparedStatement ps=null;
        try{
            if(acMap.getMapServerLayers() != null && !acMap.getMapServerLayers().isEmpty()){

                String sWMSLayersSQL="insert into maps_wms_relations (id_map,id_mapserver_layer,\"position\",id_municipio) values (?,?,?,?)";
                for (Iterator it=acMap.getMapServerLayers().iterator(); it.hasNext();){

                    ACWMSLayer layer = (ACWMSLayer)it.next();
                    if(layer.getId() == null){
                        layer.setId(new Integer(insertMapServerLayer(layer, conn)));
                    }

                    else{//en caso contrario la actualizamos
                    	String updateWMSLayer="update map_server_layers set params=?, srs=?, format=?, activa=?, visible=?, styles=? where id=?";
                    	ps=conn.prepareStatement(updateWMSLayer);
                        ps.setString(1,layer.getCommaSeparatedLayerNamesList());
                        ps.setString(2,layer.getSrs());
                        ps.setString(3,layer.getFormat());
                        ps.setInt(4, layer.isActiva()? 1 : 0);
                        ps.setInt(5, layer.isVisible()? 1 : 0);
                        ps.setString(6, layer.getCommaSeparatedStylesList());
                        ps.setInt(7,layer.getId());
                    	ps.executeUpdate();
                        ps.close();
                    }

                    int iId=Integer.parseInt(acMap.getId());

                    ps=conn.prepareStatement(sWMSLayersSQL);
                    ps.setInt(1,iId);
                    ps.setInt(2,layer.getId().intValue());
                    ps.setInt(3,layer.getPosition());
                    ps.setInt(4,Integer.parseInt(sIdMunicipio));
                    ps.executeUpdate();
                    ps.close();
                }
            }
        }catch(SQLException e){
            logger.error("updateMapServerLayer( ACMap, sIdMunicipio, Connection)", e);
            logger.error("updateMapServerLayer: "+e.getMessage());
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
        }
    }

    private int insertMapServerLayer(ACWMSLayer layer, Connection conn) throws Exception{
        PreparedStatement ps=null;
        Statement st = null;
        ResultSet rs = null;
        int id = 0;

        try{

            String seq = "select nextval('seq_map_server_layers') as id";
            st = conn.createStatement();
            rs = st.executeQuery(seq);

            if(!rs.next())
                throw new Exception("No se pudo obtener el ID de la secuencia de Map_Server_Layers");

            id = rs.getInt("id");

            st.close();
            rs.close();

            String sWMSLayerSQL="insert into map_server_layers (id,service,url,"+
                    "params,srs,format,version,activa,visible,styles) values (?,?,?,?,?,?,?,?,?,?)";

            ps=conn.prepareStatement(sWMSLayerSQL);
            ps.setInt(1,id);
            ps.setString(2,layer.getService());
            ps.setString(3,layer.getUrl());
            ps.setString(4,layer.getCommaSeparatedLayerNamesList());
            ps.setString(5,layer.getSrs());
            ps.setString(6,layer.getFormat());
            ps.setString(7,layer.getVersion());
            ps.setInt(8, layer.isActiva()? 1 : 0);
            ps.setInt(9, layer.isVisible()? 1 : 0);
            ps.setString(10,layer.getCommaSeparatedStylesList());
            ps.executeUpdate();
            ps.close();

        }
        catch(SQLException e){
            logger.error("insertMapServerLayer(ACWMSLayer)", e);
            logger.error("insertMapServerLayer: "+e.getMessage());
            throw e;
        }
        finally{
            try{rs.close();}catch(Exception e){};
            try{st.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
        }

        return id;
    }

        public void deleteMap(ISesion sesion, int iId,ObjectOutputStream oos,String sLocale) throws Exception{
            /*if (!checkPermission(sesion,Const.ACL_MAP,Const.PERM_MAP_EDIT))
                throw new PermissionException("PermissionException: " + Const.PERM_MAP_EDIT);*/


            Connection conn=null;
            PreparedStatement ps=null;
            try{
                conn=openConnection();

              //Comprobamos si tiene permiso para poder realizar la operacion
    			GeopistaAcl acl=getPermission(sesion,Const.ACL_MAP,conn);
    			if (!checkPerm(sesion,acl,Const.PERM_MAP_EDIT))
    	            throw new PermissionException("PermissionException: " + Const.PERM_MAP_EDIT);

    			  ACMap tempMap = loadMap(iId,sLocale,sesion,false,conn);
    		        if(tempMap.getIdMunicipio()==0) throw new SystemMapException("SystemMap");


    			 if (isPublishedMap(iId,conn))
    		        	throw new PublishedMapException();

                conn.setAutoCommit(false);
                String[] asSql=new String[]{"delete from dictionary where id_vocablo=(select id_name from maps where id_map=? and id_municipio = ?))"
                                           ,"delete from maps_layerfamilies_relations where id_map=? and id_municipio = ?"
                                           ,"delete from maps_wms_relations where id_map=? and id_municipio = ?"
                                           ,"delete from layers_styles where id_map=? and id_municipio = ?"
                                           ,"delete from maps where id_map=? and id_municipio = ?"};
                for (int i=0;i<asSql.length;i++){
                    ps=conn.prepareStatement(asSql[i]);
                    ps.setInt(1,iId);
                    ps.setInt(2,Integer.parseInt(sesion.getIdMunicipio()));
                    ps.executeUpdate();
                    ps.close();
                }
                conn.commit();
            }catch(SQLException e){
                try{conn.rollback();}catch(Exception ex){};
                logger.error("deleteMap(Sesion, int, ObjectOutputStream)", e);
                logger.error("deleteMap: "+e.getMessage());
                oos.writeObject(new ACException(e));
                throw e;
            }finally{
                try{ps.close();}catch(Exception e){};
                try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
            }
        }

    /** Comentado Por Incidencia [365] */
    //public void returnLayerFamilies(ObjectOutputStream oos)throws IOException,SQLException,Exception{
    public void returnLayerFamilies(ObjectOutputStream oos, String sLocale)throws IOException,SQLException,Exception{
        /** Comentado por Incidencia [365] */
        //String sSQL="select id_layerfamily,traduccion from layerfamilies, dictionary where id_name=id_vocablo";
        String sSQL="select id_layerfamily,traduccion,locale from layerfamilies, dictionary where id_name=id_vocablo "// and " +
               // "(locale=? or locale='es_ES' or locale='ca_ES' or locale='va_ES' or locale='gl_ES' or locale='eu_ES')";
        		+" order by id_layerfamily";
                Connection conn=null;
                PreparedStatement ps=null;
                ResultSet rs=null;
                try{
                    conn=openConnection();
                    ps=conn.prepareStatement(sSQL);
                    //ps.setString(1,sLocale);
                    rs=ps.executeQuery();
                    //ArrayList alFamilies=new ArrayList(); // Comentado por Incidencia [365]

                    /** Incidencia [365] */
                    int iCurrentLayerFamilyID= -1;
                    int iLastLayerFamilyID= -1;
                    int iCurrentLayerFamilyLocale= -1;
                    int iLastLayerFamilyLocale= -1;
                    Hashtable ht_acLayerFamily= new Hashtable();
                    /**/

                    for (;rs.next();){
                        ACLayerFamily acFamily=new ACLayerFamily();
                        acFamily.setId(rs.getInt("id_layerfamily"));
                        acFamily.setName(rs.getString("traduccion"));
                        //alFamilies.add(acFamily); // Comentado por Incidencia [365]

                        /** Incidencia [365] */
                        iCurrentLayerFamilyID= rs.getInt("id_layerfamily");
                        /** Comprobamos el locale encontrado */
                        iCurrentLayerFamilyLocale= getOpcionLocale(rs.getString("locale"), sLocale);
                        if ((iCurrentLayerFamilyID != iLastLayerFamilyID) ||
                             ((iCurrentLayerFamilyID == iLastLayerFamilyID) && (iCurrentLayerFamilyLocale < iLastLayerFamilyLocale))){
                            ht_acLayerFamily.put(new Integer(iCurrentLayerFamilyID), acFamily);
                            iLastLayerFamilyLocale= iCurrentLayerFamilyLocale;
                            iLastLayerFamilyID= iCurrentLayerFamilyID;
                        }
                        /**/
                    }

                    /** Comentado por Incidencia [365] */
                    /*
                    ACLayerFamily[] aFamilies=new ACLayerFamily[alFamilies.size()];
                    for (int i=0;i<aFamilies.length;i++)
                        aFamilies[i]=(ACLayerFamily)(alFamilies.get(i));
                    */
                    /** Inidencia [365] */
                    ACLayerFamily[] aFamilies=new ACLayerFamily[ht_acLayerFamily.size()];
                    Enumeration e= ht_acLayerFamily.elements();
                    for (int i=0; e.hasMoreElements(); i++){
                        aFamilies[i]= (ACLayerFamily)e.nextElement();
                    }
                    /**/

                    oos.writeObject(aFamilies);
                }catch(Exception e){
                    logger.error("returnLayerFamilies(ObjectOutputStream)", e);
                    logger.error("returnLayerFamilies: "+e.getMessage());
                    oos.writeObject(new ACException(e));
                    throw e;
                }finally{
                    try{rs.close();}catch(Exception e){};
                    try{ps.close();}catch(Exception e){};
                    try{conn.close();}catch(Exception e){};
                }
         }

    public void returnGeoRef(ObjectOutputStream oos, String tipoVia, String via, String numPoli, String sLocale,ISesion sesion) throws Exception {
                String sSQL="select numeros_policia.ID,numeros_policia.\"GEOMETRY\"" +
                       " from vias , numeros_policia where vias.id_municipio=? and numeros_policia.id_municipio=? and " +
                                                         " UPPER(vias.tipoviaine) like ? and " +
                                                         " UPPER(vias.nombreviaine) like ? and " +
                                                          " UPPER(numeros_policia.rotulo) like ? and numeros_policia.id_via=vias.id;";
               Connection conn=null;
               PreparedStatement ps=null;
               ResultSet rs=null;
               Geometry geo=null;
               try{
                  conn=openConnection();
                  ps=conn.prepareStatement(sSQL);
                  ps.setString(1,sesion.getIdMunicipio());
                  ps.setString(2,sesion.getIdMunicipio());
                  ps.setString(3,(tipoVia!=null?tipoVia.toUpperCase():"%"));
                  ps.setString(4,(via!=null?via.toUpperCase():"%"));
                  if (via!=null) via=replace(via);
                  ps.setString(5,(numPoli!=null?numPoli.toUpperCase():"%"));
                  rs=ps.executeQuery();
                  /*SRManager manager = OraSpatialManager.getSpatialReferenceManager((oracle.jdbc.OracleConnection)((org.enhydra.jdbc.core.CoreConnection)conn).con);
                  SpatialReference sr= manager.retrieve(srid.getSRID(Integer.parseInt(sesion.getIdMunicipio())));
                  GeometryFactory gFact= OraSpatialManager.getGeometryFactory(sr);

                 AdapterSDO adaptersdo= new AdapterSDO(gFact,(oracle.jdbc.OracleConnection) ((org.enhydra.jdbc.core.CoreConnection)conn).con);
                 AdapterJTS adapterJTS= new AdapterJTS(gFact);*/
                  if (rs.next())
                  {
                      /* if (rs.getObject("GEOMETRY")!=null)
                       {

                            com.vividsolutions.jts.geom.Geometry jts  =(com.vividsolutions.jts.geom.Geometry) adapterJTS.exportGeometry(com.vividsolutions.jts.geom.Geometry.class,
                            adaptersdo.importGeometry(rs.getObject("GEOMETRY")));
                            Feature f=(Feature)new com.vividsolutions.jump.io.WKTReader().read(new StringReader(jts.toText())).getFeatures().get(0);
                            geo=f.getGeometry();
                            oos.writeObject(geo);
                       } */
                      FilterNode fn=  FilterLeaf.equal("numeros_policia.ID",rs.getString("ID"));
                      while(rs.next())
                      {
                          FilterNode fnAux=  FilterLeaf.equal("numeros_policia.ID",rs.getString("ID"));
                          FilterNode fnDoble= FilterOpBinary.or(fn,fnAux);
                          fn=fnDoble;
                      };
                      returnLayer(Integer.parseInt(sesion.getIdMunicipio()), "numeros_policia",sLocale,null,fn,oos,sesion, true, null, null);

                 }else
                       throw new ObjectNotFoundException("Georeferenciación no encontrada");

                  oos.flush();
              }catch(Exception e){
                   logger.error("returnLayerFamilies(ObjectOutputStream)", e);
                   logger.error("returnLayerFamilies: "+e.getMessage());
                   oos.writeObject(new ACException(e));
                   throw e;
               }finally{
                      try{rs.close();}catch(Exception e){};
                      try{ps.close();}catch(Exception e){};
                      try{conn.close();}catch(Exception e){};
               }

    }


    private String replace(String valor)
    {
        String valorFinal=valor.replace(' ','%');
        valorFinal=valorFinal.replace('(','%');
        valorFinal=valorFinal.replace(')','%');
        valorFinal=valorFinal.replace(',','%');
        String[] remplazables = {"A","DE","LOS","EL","LA","EN"};
        for (int i=0; i<remplazables.length;i++)
        {
                valorFinal=replace(valorFinal,remplazables[i],'%');
        }
        return valorFinal;
    }


    private String replace(String valor, String remplazable, char espacio)
    {
        String origen=valor.toUpperCase();
        remplazable=remplazable.toUpperCase();
        String remplazado=valor;
        if (origen.indexOf(espacio+remplazable+espacio)>=0)
        {
            remplazado=origen.substring(0,origen.indexOf(espacio+remplazable+espacio))+"%"+
                       origen.substring(origen.indexOf(espacio+remplazable+espacio)+(espacio+remplazable+espacio).length());
            logger.debug("REMPLAZADO="+remplazado);
            origen=remplazado;
        }
        if (origen.indexOf(remplazable+espacio)==0)
        {
            remplazado="%"+origen.substring((remplazable+espacio).length());
            origen=remplazado;
        }
        if (origen.lastIndexOf(espacio+remplazable)>=0)
        {
            if (origen.lastIndexOf(espacio+remplazable)==origen.length()-(espacio+remplazable).length())
            {
                remplazado=origen.substring(0,origen.length()-(espacio+remplazable).length())+"%";
            }
        }
        return remplazado;
    }


    private String replaceSRID(String sQuery,int iMunicipio) throws ACException{
        String sRet=sQuery;
        if (sQuery!=null){
            Pattern pattern=Pattern.compile("\\?S");
            sRet= pattern.matcher(sQuery).replaceAll(String.valueOf(srid.getSRID(iMunicipio)));
        }
        return sRet;
    }


    private boolean checkPerm(ISesion sesion, GeopistaAcl acl,String perm) throws Exception{
        boolean permiso=acl.checkPermission(new GeopistaPermission(perm));
        if (!permiso){
            logger.error("No tiene permiso para realizar la operacion");
            return false;
        }
        return true;
    }


    private boolean checkPermission(ISesion sSesion,long lACL,String sPerm) throws Exception{
        GeopistaAcl acl=SesionUtils_LCGIII.getPerfil(sSesion,lACL,null);
        if (acl==null) return false;
        return acl.checkPermission(new GeopistaPermission(sPerm));
    }
    private boolean checkPermission(ISesion sSesion,GeopistaAcl acl,String sPerm) throws Exception{
        return acl.checkPermission(new GeopistaPermission(sPerm));
    }

    private boolean checkPermission(ISesion sSesion,long lACL,String sPerm,Connection con) throws Exception{
        logger.debug("Buscando el acl:"+lACL);
        GeopistaAcl acl=SesionUtils_LCGIII.getPerfil(sSesion,lACL,con);
        if (acl==null) return false;
        return acl.checkPermission(new GeopistaPermission(sPerm));
    }


    /**
     * Comprobamos si tiene permiso de lectura.
     * @param sesion
     * @param acl
     * @throws Exception
     */
    private void checkReadPerm(ISesion sesion, GeopistaAcl acl) throws Exception{
        boolean permisoLectura=acl.checkPermission(new GeopistaPermission(Const.PERM_LAYER_READ));
        if (!permisoLectura){
            throw new PermissionException("El usuario "+sesion.getUserPrincipal().getName()+ " no tiene" +
            " permisos para ver todas las capas del mapa.");
        }

    }
    private void checkReadPermLayer(ISesion sesion, GeopistaAcl acl) throws Exception{
        boolean permisoLectura=acl.checkPermission(new GeopistaPermission(Const.PERM_LAYER_READ));
        if (!permisoLectura){
            logger.error("No tiene permiso para leer el layer");
            throw new PermissionException("PermissionException: " + Const.PERM_LAYER_READ);
        }
    }



    /**
     * Este metdo sustitute al checkPermission para obtener la informacion de la Base
     * de datos solo una vez.
     * @param sSesion
     * @param lACL
     * @param conn
     * @return
     * @throws Exception
     */
    private GeopistaAcl getPermission(ISesion sSesion,long lACL,Connection conn) throws Exception{
        logger.debug("Buscando el acl:"+lACL);
        GeopistaAcl acl=SesionUtils_LCGIII.getPerfil(sSesion,lACL,conn);
        if (acl==null){
            throw new PermissionException("El usuario "+sSesion.getUserPrincipal().getName()+ " no tiene" +
            " permisos para ver todas las capas del mapa.");
        }
        return acl;
    }






    /** Carga el esquema de una capa en el objeto ACLayer */
        private void readSchema(ACLayer layer, String sLocale,ISesion sesion)throws Exception{
            int iMunicipio=Integer.parseInt(sesion.getIdMunicipio());
            /*
            String sSQL="select t.id_table,c.id as id_column,c.name as c_name,c.\"Length\" as c_length,c.\"Precision\" as c_precision,c.\"Scale\" as c_scale,c.\"Type\" as c_type,cd.id_domain as cd_id_domain, cd.\"level\" as cd_level,t.name as t_name,a.\"position\" as a_position,a.editable,d.traduccion,t.geometrytype "+
                        "from attributes a inner join columns c on (a.id_column=c.id) "+
                            "left join columns_domains cd on (c.id=cd.id_column) "+
                            "inner join tables t on (c.id_table=t.id_table) "+
                            "inner join dictionary d on (a.id_alias=d.id_vocablo) "+
                        "where a.id_layer=? "+
                            "and d.locale=? "+
                        "order by a.\"position\",t.id_table,c.id";
            */
            String sSQL="select t.id_table,c.id as id_column,c.name as c_name,c.\"Length\" as c_length,c.\"Precision\" as c_precision,c.\"Scale\" as c_scale,c.\"Type\" as c_type,cd.id_domain as cd_id_domain, cd.\"level\" as cd_level,t.name as t_name,a.\"position\" as a_position,a.editable,d.traduccion,t.geometrytype,d.locale "+
                        "from attributes a inner join columns c on (a.id_column=c.id) "+
                            "left join columns_domains cd on (c.id=cd.id_column) "+
                            "inner join tables t on (c.id_table=t.id_table) "+
                            "inner join dictionary d on (a.id_alias=d.id_vocablo) "+
                        "where a.id_layer=? "+
                            //"and (d.locale=? or d.locale='es_ES' or d.locale='ca_ES' or d.locale='va_ES' or d.locale='gl_ES' or d.locale='eu_ES') "+
                        "order by a.\"position\",t.id_table,c.id";

            Connection conn=null;
            PreparedStatement ps=null;
            ResultSet rs=null;
            try{
                boolean bEditable=(checkPermission(sesion,layer.getACL(),Const.PERM_LAYER_WRITE));
                conn=openConnection();
                ps=conn.prepareStatement(sSQL);
                ps.setInt(1,layer.getId_layer());
                ps.setString(2,sLocale);
                rs=ps.executeQuery();
                int iCurrentTable=-1;
                int iCurrentColumn=-1;
                int iTable=0;
                int iColumn=0;
                ACTable table=null;
                ACColumn column=null;
                ACAttribute attribute=null;

                /** Incidencia [365] El administrador de cartografia no funciona para otros idiomas */
                int iCurrentAttributePosition= -1;
                int iLastAttributePosition= -1;
                int iCurrentAttributeLocale= -1;
                int iLastAttributeLocale= -1;
                /**/

                while (rs.next()){
                    iTable=rs.getInt("id_table");
                    iColumn=rs.getInt("id_column");
                    if (iTable!=iCurrentTable){
                        int iGeomType=rs.getInt("geometrytype");
                        if (rs.wasNull())
                            iGeomType=-1;
                        table=new ACTable(rs.getInt("id_table"),rs.getString("t_name"),iGeomType);
                        iCurrentTable=iTable;
                    }
                    if (iColumn!=iCurrentColumn){
                        column=new ACColumn(iColumn,rs.getString("c_name"),table,null, rs.getInt("cd_level"),
                                rs.getInt("c_length"),rs.getInt("c_precision"),rs.getInt("c_scale"),rs.getInt("c_type"));
                        iCurrentTable=iTable;
                    }
                    attribute=new ACAttribute();
                    attribute.setColumn(column);
                    attribute.setName(rs.getString("traduccion"));
                    attribute.setPosition(rs.getInt("a_position"));
                    attribute.setEditable(rs.getBoolean("editable")&&bEditable);
                    ACDomain domain=GeopistaDomains.getDomain(iMunicipio,rs.getInt("cd_id_domain"));
                    column.setDomain(domain);
                    /** Comentado por Incidencia [365] */
                    /*
                    layer.addAttribute(attribute);
                    if (column.getName().equals("GEOMETRY"))
                        layer.setGeometryAttribute(attribute.getName());
                    */

                    /** Incidencia [365] El atributo en sLocale, si no es_ES, si no el idioma que exista. */
                    iCurrentAttributePosition= rs.getInt("a_position");
                    /** Opcion de idioma encontrada en el atributo */
                    iCurrentAttributeLocale= getOpcionLocale(rs.getString("locale"), sLocale);
                    /** Confirmado por JPablo: un schema no puede tener 2 atributos distintos en la misma posicion */
                    if ((iCurrentAttributePosition != iLastAttributePosition) ||
                        ((iCurrentAttributePosition == iLastAttributePosition) && (iCurrentAttributeLocale < iLastAttributeLocale))){
                            layer.addAttribute(attribute);
                            if (column.getName().equals("GEOMETRY"))
                                layer.setGeometryAttribute(attribute.getName());

                            iLastAttributeLocale= iCurrentAttributeLocale;
                            iLastAttributePosition= iCurrentAttributePosition;
                    }
                    /**/
                }
            }finally{
                try{rs.close();}catch(Exception e){};
                try{ps.close();}catch(Exception e){};
                try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
            }
        }






        /** Carga el esquema de una capa en el objeto ACLayer */
        private void readNewSchema(ACLayer layer, String sLocale, Connection connection, ISesion sesion, GeopistaAcl acl)throws Exception{
        	boolean bCerrarConexion=true;
        	int iMunicipio=Integer.parseInt(sesion.getIdMunicipio());
            /*
            String sSQL="select t.id_table,c.id as id_column,c.name as c_name,c.\"Length\" as c_length,c.\"Precision\" as c_precision,c.\"Scale\" as c_scale,c.\"Type\" as c_type,cd.id_domain as cd_id_domain, cd.\"level\" as cd_level,t.name as t_name,a.\"position\" as a_position,a.editable,d.traduccion,t.geometrytype "+
                        "from attributes a inner join columns c on (a.id_column=c.id) "+
                            "left join columns_domains cd on (c.id=cd.id_column) "+
                            "inner join tables t on (c.id_table=t.id_table) "+
                            "inner join dictionary d on (a.id_alias=d.id_vocablo) "+
                        "where a.id_layer=? "+
                            "and d.locale=? "+
                        "order by a.\"position\",t.id_table,c.id";
            */
            String sSQL="select t.id_table,c.id as id_column,c.name as c_name,c.\"Length\" as c_length,c.\"Precision\" as c_precision,c.\"Scale\" as c_scale,c.\"Type\" as c_type,cd.id_domain as cd_id_domain, cd.\"level\" as cd_level,t.name as t_name,a.\"position\" as a_position,a.editable,d.traduccion,t.geometrytype,d.locale "+
                        "from attributes a inner join columns c on (a.id_column=c.id) "+
                            "left join columns_domains cd on (c.id=cd.id_column) "+
                            "inner join tables t on (c.id_table=t.id_table) "+
                            "inner join dictionary d on (a.id_alias=d.id_vocablo) "+
                        "where a.id_layer=? "+
                            //"and (d.locale=? or d.locale='es_ES' or d.locale='ca_ES' or d.locale='va_ES' or d.locale='gl_ES' or d.locale='eu_ES') "+
                        "order by a.\"position\",t.id_table,c.id";

            Connection conn=null;
            PreparedStatement ps=null;
            ResultSet rs=null;
            try{

            	if (connection!=null)
                {
                    conn=connection;
                    bCerrarConexion=false;
                }
                else
                {
                    conn=openConnection();
                    bCerrarConexion=true;
                }


            	boolean bEditable=true;
            	if (acl==null)
            		bEditable=checkPermission(sesion,layer.getACL(),Const.PERM_LAYER_WRITE,conn);
            	else
            		bEditable=acl.checkPermission(new GeopistaPermission(Const.PERM_LAYER_WRITE));



                //conn=openConnection();
                ps=conn.prepareStatement(sSQL);
                ps.setInt(1,layer.getId_layer());
                //ps.setString(2,sLocale);
                rs=ps.executeQuery();
                int iCurrentTable=-1;
                int iCurrentColumn=-1;
                int iTable=0;
                int iColumn=0;
                ACTable table=null;
                ACColumn column=null;
                ACAttribute attribute=null;

                /** Incidencia [365] El administrador de cartografia no funciona para otros idiomas */
                int iCurrentAttributePosition= -1;
                int iLastAttributePosition= -1;
                int iCurrentAttributeLocale= -1;
                int iLastAttributeLocale= -1;
                /**/

                while (rs.next()){
                    iTable=rs.getInt("id_table");
                    iColumn=rs.getInt("id_column");
                    if (iTable!=iCurrentTable){
                        int iGeomType=rs.getInt("geometrytype");
                        if (rs.wasNull())
                            iGeomType=-1;
                        table=new ACTable(rs.getInt("id_table"),rs.getString("t_name"),iGeomType);
                        iCurrentTable=iTable;
                    }
                    if (iColumn!=iCurrentColumn){
                        column=new ACColumn(iColumn,rs.getString("c_name"),table,null, rs.getInt("cd_level"),
                                rs.getInt("c_length"),rs.getInt("c_precision"),rs.getInt("c_scale"),rs.getInt("c_type"));
                        iCurrentTable=iTable;
                    }
                    attribute=new ACAttribute();
                    attribute.setColumn(column);
                    attribute.setName(rs.getString("traduccion"));
                    attribute.setPosition(rs.getInt("a_position"));
                    attribute.setEditable(rs.getBoolean("editable")&&bEditable);
                    ACDomain domain=GeopistaDomains.getDomain(iMunicipio,rs.getInt("cd_id_domain"));
                    column.setDomain(domain);
                    /** Comentado por Incidencia [365] */
                    /*
                    layer.addAttribute(attribute);
                    if (column.getName().equals("GEOMETRY"))
                        layer.setGeometryAttribute(attribute.getName());
                    */

                    /** Incidencia [365] El atributo en sLocale, si no es_ES, si no el idioma que exista. */
                    iCurrentAttributePosition= rs.getInt("a_position");
                    /** Opcion de idioma encontrada en el atributo */
                    iCurrentAttributeLocale= getOpcionLocale(rs.getString("locale"), sLocale);
                    /** Confirmado por JPablo: un schema no puede tener 2 atributos distintos en la misma posicion */
                    if ((iCurrentAttributePosition != iLastAttributePosition) ||
                        ((iCurrentAttributePosition == iLastAttributePosition) && (iCurrentAttributeLocale < iLastAttributeLocale))){
                            layer.addAttribute(attribute);
                            if (column.getName().equals("GEOMETRY"))
                                layer.setGeometryAttribute(attribute.getName());

                            iLastAttributeLocale= iCurrentAttributeLocale;
                            iLastAttributePosition= iCurrentAttributePosition;
                    }
                    /**/
                }
            }finally{
                try{rs.close();}catch(Exception e){};
                try{ps.close();}catch(Exception e){};
               // try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
                try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e){};
            }
        }



        private int returnFeatures (int iMunicipio,String sSQL, ACLayer layer, Geometry geom,FilterNode fn, ObjectOutputStream oos, Connection connection,ISesion sesion, GeopistaAcl acl)throws Exception{
        	return returnFeatures(iMunicipio,sSQL,layer,geom,fn,oos,connection,sesion,acl,null,true);
        }

        private int returnFeatures (int iMunicipio,String sSQL, ACLayer layer, Geometry geom,FilterNode fn, ObjectOutputStream oos, Connection connection,ISesion sesion, GeopistaAcl acl,String sLocale)throws Exception{
        	return returnFeatures(iMunicipio,sSQL,layer,geom,fn,oos,connection,sesion,acl,sLocale,true);
        }




        private int returnFeatures (int iMunicipio,String sSQL, ACLayer layer, Geometry geom,FilterNode fn, ObjectOutputStream oos, Connection connection,ISesion sesion, GeopistaAcl acl, String sLocale, boolean bValidateData)throws Exception{
        int iRet=-1;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        boolean bCerrarConexion=true;
        try{


            if (connection!=null)
            {
                conn=connection;
                bCerrarConexion=false;
            }
            else
            {
                conn=openConnection();
                bCerrarConexion=true;
            }


            if (acl==null){
           		if (!checkPermission(sesion,layer.getACL(),Const.PERM_LAYER_READ,conn))
                    throw new PermissionException(Const.PERM_LAYER_READ);
           	}
           	else{
                boolean permisoLectura=acl.checkPermission(new GeopistaPermission(Const.PERM_LAYER_READ));
                if (!permisoLectura)
                	throw new PermissionException(Const.PERM_LAYER_READ);
           	}

            /*	Bloque que se añadiría si recibiéramos la variable booleana bValidateData (como en postgres)

        	ArrayList lstValidateFeatures = null;
            if (bValidateData)
            {
           		lstValidateFeatures = getValidateFeatures(layer,sLocale);
            }

            if (lstValidateFeatures != null){
           		for (Iterator iterValidateFeatures = lstValidateFeatures.iterator();iterValidateFeatures.hasNext();){
           			AbstractValidator validateFeatures = (AbstractValidator)iterValidateFeatures.next();
           			validateFeatures.beforeRead(layer);
           		}
           	}*/



            //REFACTORIZACION ORACLE ps=new SQLParser(conn,srid).newSelectOracle(iMunicipio,sSQL,layer,geom,fn);
            ps=new SQLParserOracle(conn,srid).newSelectOracle(iMunicipio,sSQL,layer,geom,fn);
            SRManager manager = OraSpatialManager.getSpatialReferenceManager((oracle.jdbc.OracleConnection)((org.enhydra.jdbc.core.CoreConnection)conn).con);
            SpatialReference sr= manager.retrieve(srid.getSRID(iMunicipio));
            GeometryFactory gFact= OraSpatialManager.getGeometryFactory(sr);

            AdapterSDO adaptersdo= new AdapterSDO(gFact,(oracle.jdbc.OracleConnection) ((org.enhydra.jdbc.core.CoreConnection)conn).con);
            AdapterJTS adapterJTS= new AdapterJTS(gFact);

            rs=ps.executeQuery();
            ACFeature acFeature=null;
            while (rs.next()){
                acFeature=new ACFeature();
                ACAttribute att=null;
                Object oAttValue=null;
                for (Iterator it=layer.getAttributes().values().iterator();it.hasNext();){
                	layer.getAttributes();
                    att=(ACAttribute)it.next();
                    switch(att.getColumn().getType()){
                       case ACLayer.TYPE_GEOMETRY:
                            com.vividsolutions.jts.geom.Geometry jts  =(com.vividsolutions.jts.geom.Geometry) adapterJTS.exportGeometry(com.vividsolutions.jts.geom.Geometry.class,
                                    adaptersdo.importGeometry(rs.getObject("GEOMETRY")));
                            if (jts instanceof com.vividsolutions.jts.geom.MultiPolygon){
                            	if (((com.vividsolutions.jts.geom.MultiPolygon)jts).getNumGeometries()==1)
                            		jts = ((com.vividsolutions.jts.geom.MultiPolygon)jts).getGeometryN(0);
                            }
                            else if (jts instanceof com.vividsolutions.jts.geom.MultiLineString){
                            	if (((com.vividsolutions.jts.geom.MultiLineString)jts).getNumGeometries()==1)
                            		jts = ((com.vividsolutions.jts.geom.MultiLineString)jts).getGeometryN(0);
                            }
                            else if (jts instanceof com.vividsolutions.jts.geom.MultiPoint){
                            	if (((com.vividsolutions.jts.geom.MultiPoint)jts).getNumGeometries()==1)
                            		jts = ((com.vividsolutions.jts.geom.MultiPoint)jts).getGeometryN(0);
                            }

                            if (jts==null) continue;
                            acFeature.setGeometry(jts.toText());
                            break;
                        case ACLayer.TYPE_NUMERIC:
                        case ACLayer.TYPE_STRING:
                        case ACLayer.TYPE_DATE:
                        case ACLayer.TYPE_BOOLEAN:
                        default:
                        try
                        {
                           oAttValue=rs.getObject(att.getColumn().getName());
                           acFeature.setAttribute(att.getName(),(Serializable)oAttValue);
                        }catch(Exception ex)
                        {
                            logger.error("No se puede cargar la columna: "+att.getColumn().getName(),ex);
                        }
                   }
                }
                 oos.writeObject(acFeature);
            }
            if (acFeature!=null)
            {
                iRet=acFeature.findID(layer);
            }


            /* Bloque que se añadiría si recibiéramos la variable booleana bValidateData (como en postgres)
             *  if (lstValidateFeatures != null){
               		for (Iterator iterValidateFeatures = lstValidateFeatures.iterator();iterValidateFeatures.hasNext();){
               			AbstractValidator validateFeatures = (AbstractValidator)iterValidateFeatures.next();
               			validateFeatures.afterRead(acFeature, layer);
               		}
               	}*/



        }catch (Exception e){
            logger.error("returnFeatures: " + e.getMessage());
            oos.writeObject(new ACException(e));
            throw e;
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e){};
        }
        return iRet;
    }
    public Collection loadMaps(String sLocale, ISesion sesion) throws SQLException,Exception{
            HashMap alRet=new HashMap();
           /** Comentado por Incidencia [365] */
           /*
           String sSQL="select * from maps m, dictionary d where (id_municipio=0  or id_municipio='"+sesion.getIdMunicipio()+"') "+
                   "and m.id_name=d.id_vocablo and d.locale=? order by id_municipio";
           */
            String sSQL="select * from maps m, dictionary d where (id_municipio=0  or id_municipio='"+sesion.getIdMunicipio()+"') "+
                    "and m.id_name=d.id_vocablo "+
                    //"(d.locale=? or d.locale='es_ES' or d.locale='ca_ES' or d.locale='va_ES' or d.locale='gl_ES' or d.locale='eu_ES') " +
                    "order by id_municipio, id_map";

           Connection conn=null;
           PreparedStatement ps=null;
           ResultSet rs=null;
           try{
               conn=openConnection();
               ps=conn.prepareStatement(sSQL);
               //ps.setString(1,sLocale);
               rs=ps.executeQuery();
               ACMap map=null;
               /** Incidencia [365] */
               /** Locale del mapa */
               int iCurrentMapLocale= -1;
               int iLastMapLocale= -1;
               /** Indentificador del mapa */
               int iCurrentMapId= -1;
               int iLastMapId= -1;
               /** municipio del mapa */
               int iLastMunicipio= -1;
               int iCurrentMunicipio= -1;
               /**/
               while (rs.next()){
                   map=new ACMap();
                   map.setName(rs.getString("traduccion"));
                   java.sql.Blob b = rs.getBlob("image");
                   if (b!=null)
                   {
                       map.setImage((byte[])b.getBytes(1,new Long(b.length()).intValue()));
                   }
                   map.setId(String.valueOf(rs.getInt("id_map")));
                   /** Comentado por Incidencia [365] */
                   //alRet.put(map.getId(),map); // La query ordena los resultados con los idmuni NULL primero por lo que en el hashtable sobreviven los no NULL en caso de duplicidad.

                   /* Incidencia [365]: El administrador de cartografia no funciona para otros idiomas.
                      Si no existe para sLocale, se mostrara es_ES, si no, en ultima instancia se mostrará en el idioma en el que exista. */

                   iCurrentMapId= rs.getInt("id_map");
                   iCurrentMunicipio= rs.getInt("id_municipio");
                   /** La query ordena los resultados con los idmuni NULL primero por lo que en el hashtable sobreviven los no NULL en caso de duplicidad. */
                   iCurrentMapLocale= getOpcionLocale(rs.getString("locale"), sLocale);
                   if ((iCurrentMunicipio != iLastMunicipio) ||
                       ((iCurrentMunicipio == iLastMunicipio) && (iCurrentMapId != iLastMapId)) ||
                       ((iCurrentMunicipio == iLastMunicipio) && (iCurrentMapId == iLastMapId) && (iCurrentMapLocale < iLastMapLocale))){
                           alRet.put(map.getId(),map);
                           iLastMunicipio= iCurrentMunicipio;
                           iLastMapLocale= iCurrentMapLocale;
                           iLastMapId= iCurrentMapId;
                   }
                   /**/
               }
           }finally{
               try{rs.close();}catch(Exception e){};
               try{ps.close();}catch(Exception e){};
               try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
           }
           return alRet.values();
       }

       /*
       public ACMap loadMap(int iID, String sLocale, ISesion sesion, boolean bLoadSchema) throws Exception{
        ACMap acMap=null;
        int realIdMunicipio = 0;
           String sSQLMap="select m.id_map,m.image,m.xml,d.traduccion, m.id_municipio " +
                                 " from maps m, dictionary d " +
                                 " where m.id_map=? and m.id_name=d.id_vocablo and d.locale=? and (id_municipio = 0  or id_municipio=?) order by id_municipio desc";

        String sSQLLayers="select mlr.\"position\" as mlr_position, " +
                            "d1.traduccion as nombre_categoria, d2.traduccion as nombre_layer,lf.id_layerfamily as lf_id_layerfamily," +
                            "l.id_layer as l_id_layer," +
                            "l.name as l_name," +
                            "l.acl as l_acl," +
                            "llr.\"position\" as llr_position," +
                            "ls.stylename as ls_stylename," +
                            "ls.\"position\" as ls_position," +
                            "s.xml as s_xml," +
                            "s.id_style as s_id_style," +
                            "q.selectquery as q_selectquery " +
                    "from maps m inner join maps_layerfamilies_relations mlr on m.id_map=mlr.id_map " +
                            "inner join layerfamilies lf on mlr.id_layerfamily=lf.id_layerfamily " +
                            "inner join layerfamilies_layers_relations llr on lf.id_layerfamily=llr.id_layerfamily " +
                            "inner join layers l on llr.id_layer=l.id_layer " +
                            "inner join queries q on q.id_layer=l.id_layer " +
                            "left join layers_styles ls on m.id_map=ls.id_map and lf.id_layerfamily=ls.id_layerfamily and ls.id_layer=l.id_layer " +
                            "left join styles s on s.id_style=ls.id_style " +
                           // "inner join dictionary d on (lf.id_name=d.id_vocablo  or l.id_name=d.id_vocablo) " + ASO cambia esta mal
                           "inner join dictionary d1 on lf.id_name=d1.id_vocablo " +
                           "inner join dictionary d2 on l.id_name=d2.id_vocablo " +
                    "where q.databasetype="+DATABASETYPE+"  and m.id_map=? and d1.locale=? and d2.locale=?" +
                   " and s.id_style is not null and m.id_municipio=? and mlr.id_municipio=? "+
                    "and ls.id_municipio=?" + //Añadido por ASO
                    "order by mlr.\"position\",llr.\"position\",l.id_layer";
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            conn=openConnection();
            ps=conn.prepareStatement(sSQLMap);
            ps.setInt(1,iID);
            ps.setString(2,sLocale);
            ps.setInt(3,Integer.parseInt(sesion.getIdMunicipio()));
            rs=ps.executeQuery();
            acMap=new ACMap();
            if (rs.next()){
                acMap.setName(rs.getString("traduccion"));
                //acMap.setImage((byte[])rs.getObject("image"));
                java.sql.Blob b = rs.getBlob("image");
                if (b!=null)
                {
                       acMap.setImage((byte[])b.getBytes(1,new Long(b.length()).intValue()));
                }
                acMap.setId(String.valueOf(rs.getInt("id_map")));
                 realIdMunicipio = rs.getInt("id_municipio");
                acMap.setIdMunicipio(realIdMunicipio);
            } else
                throw new ObjectNotFoundException("ObjectNotFoundException: map "+iID + " ("+sLocale+")");
            rs.close();
            ps.close();
            ps=conn.prepareStatement(sSQLLayers);
            ps.setInt(1,iID);
            ps.setString(2,sLocale);
             ps.setString(3,sLocale);
             ps.setInt(4,realIdMunicipio);
             ps.setInt(5,realIdMunicipio);
             ps.setInt(6,realIdMunicipio);
            rs=ps.executeQuery();
            Hashtable htFamilies=new Hashtable();
            Hashtable htLayers=null;
            acMap.setLayerFamilies(htFamilies);
            int iCurrentCategoryPosition=0;
            int iLastCategoryPosition=-1;
            String sCategory=null;
            ACLayerFamily acFamily=null;
            // Lo de abajo deja de ser cierto fallaba cuando el nombre de la categoria y el layer eran
            // los mismos, además no se podía concretar que el orden fuera correcto
            // Los resultados salen en filas de 2 en 2, la primera con el nombre de la categoria
            // y la segunda con el nombre del layer
            while (rs.next()){
                iCurrentCategoryPosition=rs.getInt("mlr_position");
                if (iCurrentCategoryPosition!=iLastCategoryPosition){ // Nueva categoria?
                    htLayers=new Hashtable();
                    sCategory=rs.getString("nombre_categoria");
                    acFamily=new ACLayerFamily();
                    acFamily.setName(sCategory);
                    acFamily.setId(rs.getInt("lf_id_layerfamily"));
                    acFamily.setLayers(htLayers);
                    htFamilies.put(new Integer(iCurrentCategoryPosition),acFamily);
                    iLastCategoryPosition=iCurrentCategoryPosition;
                }
                //rs.next(); ASO cambia
                ACLayer acLayer=new ACLayer(rs.getInt("l_id_layer"),rs.getString("nombre_layer"),rs.getString("l_name"),replaceSRID(rs.getString("q_selectquery"),Integer.parseInt(sesion.getIdMunicipio())));
                String sStyleName=rs.getString("ls_stylename");
                String sStyleXML=rs.getString("s_xml");
                String sStyleId=String.valueOf(rs.getInt("s_id_style"));
                if (rs.getString("ls_position")!=null)
                {
                    int iPosOnMap=rs.getInt("ls_position");
                    acLayer.setPositionOnMap(iPosOnMap);
                }
                if (sStyleName!=null && sStyleXML!=null){
                    acLayer.setStyleName(sStyleName);
                    acLayer.setStyleXML(sStyleXML);
                    acLayer.setStyleId(sStyleId);
                }
                //ASO AÑADE
                if (!checkPermission(sesion,rs.getLong("l_acl"),Const.PERM_LAYER_READ))
                {
                    throw new PermissionException("El usuario "+sesion.getUserPrincipal().getName()+ " no tiene" +
                            " permisos para ver todas las capas del mapa.");
                }
                acLayer.setActive(true);//checkPermission(sesion,rs.getLong("l_acl"),Const.PERM_LAYER_READ));
                acLayer.setEditable(checkPermission(sesion,rs.getLong("l_acl"),Const.PERM_LAYER_WRITE));
                htLayers.put(new Integer(rs.getInt("llr_position")),acLayer);
                if (bLoadSchema)
                    readSchema(acLayer,sLocale,sesion);

            }
            acMap.setLayerFamilies(htFamilies);
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return acMap;
    }
    */

    public ACMap loadMap(int iID, String sLocale, ISesion sesion, boolean bLoadSchema,Connection connection) throws Exception{
     ACMap acMap=null;
     int realIdMunicipio = 0;
     String sSQLMap="select m.id_map,m.image,m.xml,d.traduccion, m.id_municipio, d.locale " +
                              " from maps m, dictionary d " +
                              " where m.id_map=? and m.id_name=d.id_vocablo "+
                              //"(d.locale=? or d.locale='es_ES' or d.locale='ca_ES' or d.locale='va_ES' or d.locale='gl_ES' or d.locale='eu_ES') " +
                              "and (id_municipio = 0  or id_municipio=?) order by id_municipio";

     String sSQLLayers="select mlr.\"position\" as mlr_position, " +
                         "d1.traduccion as nombre_categoria, d2.traduccion as nombre_layer,lf.id_layerfamily as lf_id_layerfamily," +
                         "l.id_layer as l_id_layer," +
                         "l.name as l_name," +
                         "l.acl as l_acl," +
                         "llr.\"position\" as llr_position," +
                         "ls.stylename as ls_stylename," +
                         "ls.\"position\" as ls_position," +
                         "ls.isactive as ls_isactive, "+
                         "s.xml as s_xml," +
                         "s.id_style as s_id_style," +
                         "d1.locale as lf_locale, d2.locale as l_locale," +
                         "q.selectquery as q_selectquery " +
                 "from maps m inner join maps_layerfamilies_relations mlr on m.id_map=mlr.id_map " +
                         "inner join layerfamilies lf on mlr.id_layerfamily=lf.id_layerfamily " +
                         "inner join layerfamilies_layers_relations llr on lf.id_layerfamily=llr.id_layerfamily " +
                         "inner join layers l on llr.id_layer=l.id_layer " +
                         "inner join queries q on q.id_layer=l.id_layer " +
                         "left join layers_styles ls on m.id_map=ls.id_map and lf.id_layerfamily=ls.id_layerfamily and ls.id_layer=l.id_layer " +
                         "left join styles s on s.id_style=ls.id_style " +
                        // "inner join dictionary d on (lf.id_name=d.id_vocablo  or l.id_name=d.id_vocablo) " + ASO cambia esta mal
                        "inner join dictionary d1 on lf.id_name=d1.id_vocablo " +
                        "inner join dictionary d2 on l.id_name=d2.id_vocablo " +
                 "where q.databasetype="+DATABASETYPE+"  and m.id_map=?  and d1.locale=d2.locale " +//and " +
                // "(d1.locale=? or d1.locale='es_ES' or d1.locale='ca_ES' or d1.locale='va_ES' or d1.locale='gl_ES' or d1.locale='eu_ES') and " +
                 //"(d2.locale=? or d2.locale='es_ES' or d2.locale='ca_ES' or d2.locale='va_ES' or d2.locale='gl_ES' or d2.locale='eu_ES') " +
                 "and s.id_style is not null and m.id_municipio=? and mlr.id_municipio=? "+
                 "and ls.id_municipio=?" + //Añadido por ASO
                 "order by mlr.\"position\",llr.\"position\",l.id_layer";
     Connection conn=null;
     PreparedStatement ps=null;
     ResultSet rs=null;
     boolean bCerrarConexion=true;
     try{

    	 if (connection!=null)
         {
             conn=connection;
             bCerrarConexion=false;
         }
         else
         {
         	conn=openConnection();
             bCerrarConexion=true;
         }

         //conn=openConnection();
         ps=conn.prepareStatement(sSQLMap);
         ps.setInt(1,iID);
        //ps.setString(2,sLocale);
         ps.setInt(2,Integer.parseInt(sesion.getIdMunicipio()));
         rs=ps.executeQuery();
         acMap=new ACMap();

         if (rs.next()){
             /*
             acMap.setName(rs.getString("traduccion"));
             //acMap.setImage((byte[])rs.getObject("image"));
             java.sql.Blob b = rs.getBlob("image");
             if (b!=null)
             {
                    acMap.setImage((byte[])b.getBytes(1,new Long(b.length()).intValue()));
             }
             acMap.setId(String.valueOf(rs.getInt("id_map")));
              realIdMunicipio = rs.getInt("id_municipio");
             acMap.setIdMunicipio(realIdMunicipio);
             */

             /* Incidencia [365]: El administrador de cartografia no funciona para otros idiomas.
                Si no existe para sLocale, se mostrará en el idioma en el que exista. */
             int iCurrentMunicipio= -1;
             int iLastMunicipio= -1;
             int iCurrentMapLocale= -1;
             int iLastMapLocale= -1;

             do{
                 // Priorizamos los idiomas: preferencia de usuario, es_ES, resto de idiomas
                 iCurrentMunicipio= rs.getInt("id_municipio");
                 iCurrentMapLocale= getOpcionLocale(rs.getString("locale"), sLocale);
                 /** ordenado por id_municipio: los registros con id_municicpio=0 los primeros */
                 if ((iCurrentMunicipio != iLastMunicipio) ||
                     ((iCurrentMunicipio == iLastMunicipio) && (iCurrentMapLocale < iLastMapLocale))){
                     acMap.setName(rs.getString("traduccion"));
                     //acMap.setImage((byte[])rs.getObject("image"));
                     java.sql.Blob b = rs.getBlob("image");
                     if (b!=null){
                         acMap.setImage((byte[])b.getBytes(1,new Long(b.length()).intValue()));
                     }
                     acMap.setId(String.valueOf(rs.getInt("id_map")));
                     acMap.setXml(updateProjection(rs.getCharacterStream("xml"),srid.getSRID(Integer.parseInt(sesion.getIdMunicipio()))));
                     realIdMunicipio = rs.getInt("id_municipio");
                     acMap.setIdMunicipio(realIdMunicipio);

                     iLastMunicipio= iCurrentMunicipio;
                     iLastMapLocale= iCurrentMapLocale;
                 }
             }while (rs.next());
             /**/
         } else
             throw new ObjectNotFoundException("ObjectNotFoundException: map "+iID + " ("+sLocale+")");
         rs.close();
         ps.close();
         ps=conn.prepareStatement(sSQLLayers);
         ps.setInt(1,iID);
        // ps.setString(2,sLocale);
         //ps.setString(3,sLocale);
         ps.setInt(2,realIdMunicipio);
         ps.setInt(3,realIdMunicipio);
         ps.setInt(4,realIdMunicipio);
         rs=ps.executeQuery();
         Hashtable htFamilies=new Hashtable();
         Hashtable htLayers=null;
         acMap.setLayerFamilies(htFamilies);
         int iCurrentCategoryPosition=0;
         int iLastCategoryPosition=-1;
         String sCategory=null;
         ACLayerFamily acFamily=null;
         // Lo de abajo deja de ser cierto fallaba cuando el nombre de la categoria y el layer eran
         // los mismos, además no se podía concretar que el orden fuera correcto
         // Los resultados salen en filas de 2 en 2, la primera con el nombre de la categoria
         // y la segunda con el nombre del layer

         /** Incidencia [365] */
         int iLastLayerPosition= -1;
         int iCurrentLayerPosition= -1;
         /** opcion idioma layer */
         int iLastLayerLocale= -1;
         int iCurrentLayerLocale= -1;
         /** opcion idioma categoria */
         int iLastCategoriaLocale= -1;
         int iCurrentCategoriaLocale= -1;
         /**/

         while (rs.next()){
             /** Opcion de idioma encontrada en la categoria */
             iCurrentCategoriaLocale= getOpcionLocale(rs.getString("lf_locale"), sLocale);
             /**/
             iCurrentCategoryPosition=rs.getInt("mlr_position");
             if (iCurrentCategoryPosition!=iLastCategoryPosition){ // Nueva categoria?
                 htLayers=new Hashtable();
                 sCategory=rs.getString("nombre_categoria");
                 acFamily=new ACLayerFamily();
                 acFamily.setName(sCategory);
                 acFamily.setId(rs.getInt("lf_id_layerfamily"));
                 acFamily.setLayers(htLayers);
                 htFamilies.put(new Integer(iCurrentCategoryPosition),acFamily);
                 iLastCategoryPosition=iCurrentCategoryPosition;

                 /** Añadido por Incidencia [365] */
                 iLastLayerPosition= -1;
                 iLastCategoriaLocale= iCurrentCategoriaLocale;
             }else if (iCurrentCategoriaLocale < iLastCategoriaLocale){
                /** Locale actual mejor que el anterior */
                if (htFamilies.get(new Integer(iCurrentCategoryPosition)) != null){
                    acFamily= (ACLayerFamily)htFamilies.get(new Integer(iCurrentCategoryPosition));
                    acFamily.setName(rs.getString("nombre_categoria"));
                }
                iLastCategoriaLocale= iCurrentCategoriaLocale;
             }
             /**/
             //rs.next(); ASO cambia
             ACLayer acLayer=new ACLayer(rs.getInt("l_id_layer"),rs.getString("nombre_layer"),rs.getString("l_name"),replaceSRID(rs.getString("q_selectquery"),Integer.parseInt(sesion.getIdMunicipio())));
             String sStyleName=rs.getString("ls_stylename");
             String sStyleXML=rs.getString("s_xml");
             String sStyleId=String.valueOf(rs.getInt("s_id_style"));
             if (rs.getString("ls_position")!=null)
             {
                 int iPosOnMap=rs.getInt("ls_position");
                 acLayer.setPositionOnMap(iPosOnMap);
             }
             if (sStyleName!=null && sStyleXML!=null){
                 acLayer.setStyleName(sStyleName);
                 acLayer.setStyleXML(sStyleXML);
                 acLayer.setStyleId(sStyleId);
             }
             //ASO AÑADE

             acLayer.setActive(rs.getBoolean("ls_isactive"));//checkPermission(sesion,rs.getLong("l_acl"),Const.PERM_LAYER_READ));


             GeopistaAcl acl=getPermission(sesion,rs.getLong("l_acl"),conn);
             checkReadPerm(sesion,acl);
             acLayer.setEditable(acl.checkPermission(new GeopistaPermission(Const.PERM_LAYER_WRITE)));


             /** Comentado por Incidencia [365] */
             /*
             htLayers.put(new Integer(rs.getInt("llr_position")),acLayer);
             if (bLoadSchema)
                 readSchema(acLayer,sLocale,sesion);
             */

             /** Incidencia [365] . Leemos la opcion de idioma.*/
             iCurrentLayerLocale= getOpcionLocale(rs.getString("l_locale"), sLocale);
             iCurrentLayerPosition= rs.getInt("llr_position");
             if ((iCurrentLayerPosition != iLastLayerPosition) ||
                  ((iCurrentLayerPosition == iLastLayerPosition) && (iCurrentLayerLocale < iLastLayerLocale))){
                 /** La opcion locale actual es mejor que la anterior */
                 htLayers.put(new Integer(rs.getInt("llr_position")),acLayer);
                 if (bLoadSchema)
                	 readNewSchema(acLayer,sLocale, conn, sesion,acl);
                 iLastLayerLocale= iCurrentLayerLocale;
                 iLastLayerPosition= iCurrentLayerPosition;
             }
             /**/
         }
         acMap.setLayerFamilies(htFamilies);
         acMap.setMapServerLayers(loadMapServerLayers(iID, sesion,conn));
     }finally{
         try{rs.close();}catch(Exception e){};
         try{ps.close();}catch(Exception e){};
         try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e1){};
         //try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
     }
     return acMap;
 }






    public ArrayList loadMapServerLayers(int idMap, ISesion sesion,Connection connection){
        ArrayList wmsLayers = new ArrayList();
        ACWMSLayer layer = null;
        boolean bCerrarConexion=true;

        String query = "select MSL.id, MSL.service, MSL.url, MSL.params, " +
                "MSL.srs, MSL.format, MSL.version, MSL.activa, MSL.visible,MSL.styles, R.\"position\" " +
                "from map_server_layers MSL, maps_wms_relations R " +
                "where MSL.id = R.id_mapserver_layer and R.id_map = ? and R.id_municipio = ?";

        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
           // conn=openConnection();

        	if (connection!=null)
            {
                conn=connection;
                bCerrarConexion=false;
            }
            else
            {
                conn=openConnection();
                bCerrarConexion=true;
            }

            ps=conn.prepareStatement(query);
            ps.setInt(1,idMap);
            ps.setInt(2,Integer.parseInt(sesion.getIdMunicipio()));
            rs=ps.executeQuery();

            while (rs.next()){
                int idWMSLayer = rs.getInt("id");
                String service = rs.getString("service");
                String url = rs.getString("url");
                String params = rs.getString("params");
                String srs = rs.getString("srs");
                String format = rs.getString("format");
                String version = rs.getString("version");
                boolean activa = rs.getInt("activa") == 1 ? true : false;
                boolean visible = rs.getInt("visible") == 1 ? true : false;
                int position = rs.getInt("position");
                String styles=rs.getString("styles");
                String name=rs.getString("name");

                layer = new ACWMSLayer(idWMSLayer, service, url, params, srs, format, version, activa, visible,
                		position,styles, name);
                wmsLayers.add(layer);
            }

        }
        catch(Exception e){
            logger.error("loadMapServerLayers: " +e, e);
        }
        finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            //try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
            try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e){};
        }

        return wmsLayers;
    }

    public int canLockFeature(int iMunicipio, String sLayer, int iFeature, int iUser, Connection connection) throws SQLException, ACException{
            int iRet=0;
            boolean bCerrarConexion=true;

            // Comparar ids de Feature y bloqueos de layer con la geometria de iFeature...
            int iFeatureLock=featureLocked(iMunicipio,sLayer,iFeature, connection);
            if(iFeatureLock!=-1)
                return (iFeatureLock==iUser?AdministradorCartografiaClient.LOCK_FEATURE_OWN
                                           :AdministradorCartografiaClient.LOCK_FEATURE_OTHER);
            //Obtener la tabla donde esta la geometria...
            String sSQLTable="select t.name as tabla,l.id_layer from tables t,columns c,attributes a,layers l " +
                             "where a.id_layer=l.id_layer and l.name=? and c.id_table=t.id_table and c.name='GEOMETRY' and a.id_column=c.id";
            Connection conn=null;
            PreparedStatement ps=null;
            ResultSet rs=null;
            try{
               	if (connection!=null)
                {
                    conn=connection;
                    bCerrarConexion=false;
                }
                else
                {
                	conn=openConnection();
                    bCerrarConexion=true;
                }


                //conn=openConnection();
                ps=conn.prepareStatement(sSQLTable);
                ps.setString(1,sLayer);
                rs=ps.executeQuery();
                String sTable=null;
                if (rs.next()){
                    sTable=rs.getString("tabla");
                }
                rs.close();
                ps.close();
                String sSQLGeom="select ll.user_id from locks_layer ll,"+sTable+" t " +
                                "where t.id=? and ll.municipio=? and ll.layer=? " +
                                //" and t.\"GEOMETRY\" && setsrid(locks_layer.\"GEOMETRY\","+srid.getSRID(iMunicipio)+");";
                " and sdo_relate(t.geometry,ll.geometry, 'mask=anyinteract querytype=window') = 'TRUE'";


                ps=conn.prepareStatement(sSQLGeom);
                ps.setInt(1,iFeature);
                ps.setInt(2,iMunicipio);
                ps.setString(3,sLayer);
                rs=ps.executeQuery();
                if (rs.next())
                    iRet=(rs.getInt("user_id")!=iUser)? AdministradorCartografiaClient.LOCK_LAYER_OTHER
                                                      : AdministradorCartografiaClient.LOCK_LAYER_OWN;
            }finally{
                try{rs.close();}catch(Exception e){};
                try{ps.close();}catch(Exception e){};
                //try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
                try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e1){};
            }
            return iRet;
        }



    /** devuelve -1 si no bloqueado, id_usuario si hay bloqueo */
    private int featureLocked(int iMunicipio, String sLayer, int iFeature,Connection connection) throws SQLException{
        int iRet=-1;
        boolean bCerrarConexion=true;
        String sSQL="select user_id,ts from locks_feature " +
                    "where " +
                        "municipio=? and " +
                        "layer=? and " +
                        "feature_id=?";
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{

        	if (connection!=null)
            {
                conn=connection;
                bCerrarConexion=false;
            }
            else
            {
            	conn=openConnection();
                bCerrarConexion=true;
            }

           // conn=openConnection();
            ps=conn.prepareStatement(sSQL);
            ps.setInt(1,iMunicipio);
            ps.setString(2,sLayer);
            ps.setInt(3,iFeature);
            rs=ps.executeQuery();
            if (rs.next()){
                iRet=rs.getInt("user_id");
            }
        }finally{
            try{rs.close();}catch(Exception e){};
            try{ps.close();}catch(Exception e){};
            try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e1){};
            //try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }
        return iRet;
    }

    /** REFACTORIZACION ORACLE */
    public static com.geopista.protocol.CResultadoOperacion getDireccionMasCercanaOracle(Connection connection,String geometria, String idMunicipio, int ssrid)
        {
                PreparedStatement preparedStatement = null;
                ResultSet rsSQL = null;
                com.geopista.protocol.CResultadoOperacion resultado;
                try {
                    logger.debug("Inicio de conseguir la dirección mas cercana Oracle:" + geometria);
                    if (connection == null) {
                        logger.warn("No se puede obtener la conexión");
                        return new com.geopista.protocol.CResultadoOperacion(false, "No se puede obtener la conexión");
                    }

                    connection.setAutoCommit(false);

                    String sql="select np.id as id, np.id_via as id_via, np.rotulo as rotulo from "+
                    "numeros_policia np where np.id_municipio =? "+
                    "and (MDSYS.SDO_NN(geometry,?, 'sdo_num_res=1')= 'TRUE')";

                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, idMunicipio);
                    //añadimos la geometria
                    SRManager manager = OraSpatialManager.getSpatialReferenceManager((oracle.jdbc.OracleConnection)((org.enhydra.jdbc.core.CoreConnection)connection).con);
                    SpatialReference sr= manager.retrieve(ssrid);
                    GeometryFactory gFact= OraSpatialManager.getGeometryFactory(sr);
                    AdapterSDO adaptersdo= new AdapterSDO(gFact,(oracle.jdbc.OracleConnection) ((org.enhydra.jdbc.core.CoreConnection)connection).con);
                    AdapterJTS adapterJTS= new AdapterJTS(gFact);
                    ///
                    WKTReader wktReader = new WKTReader();
                    oracle.sdoapi.geom.Geometry geometry =  adapterJTS.importGeometry(wktReader.read(geometria));
                    Object exportedStruct = adaptersdo.exportGeometry(STRUCT.class,geometry);
                    preparedStatement.setObject(2,exportedStruct );
                    rsSQL=preparedStatement.executeQuery();
                    if (!rsSQL.next())
                    {
                            resultado= new com.geopista.protocol.CResultadoOperacion(false, "No existe ningún número de policia cercano");
                            logger.info("No existe ningún número de policia cercano ");
                    }
                    else
                    {
                        String rotulo=rsSQL.getString("rotulo");
                        String id=rsSQL.getString("id");
                        String idVia =rsSQL.getString("id_via");
                        //com.geopista.server.database.COperacionesDatabaseOcupaciones.safeClose(null, null, preparedStatement, null);
                        preparedStatement = connection.prepareStatement("select tipovianormalizadocatastro, nombrecatastro from vias where ID=?");
                        preparedStatement.setString(1, idVia);
                        rsSQL=preparedStatement.executeQuery();
                        if (!rsSQL.next())
                        {
                                resultado= new com.geopista.protocol.CResultadoOperacion(false, "No se ha encontrado los datos de la vía");
                                logger.info("no se ha encontrado datos para el id_via "+idVia);
                        }
                        else
                        {
                            com.geopista.protocol.contaminantes.NumeroPolicia numeroPolicia = new com.geopista.protocol.contaminantes.NumeroPolicia();
                            numeroPolicia.setId(id);
                            numeroPolicia.setId_via(idVia);
                            numeroPolicia.setNombrevia(rsSQL.getString("nombrecatastro"));
                            numeroPolicia.setRotulo(rotulo);
                            numeroPolicia.setTipovia(rsSQL.getString("tipovianormalizadocatastro"));
                            resultado=new com.geopista.protocol.CResultadoOperacion(true, "Operación ejecutada correctamente");
                            Vector aux= new Vector();
                            aux.add(numeroPolicia);
                            resultado.setVector(aux);
                        }

                     }
                    connection.commit();

                } catch (Exception e) {
                    java.io.StringWriter sw = new java.io.StringWriter();
                    java.io.PrintWriter pw = new java.io.PrintWriter(sw);
                    e.printStackTrace(pw);
                    logger.error("ERROR al obtener la direccion mas cercana:" + sw.toString());
                    resultado = new com.geopista.protocol.CResultadoOperacion(false, e.getMessage());
                    try {
                        connection.rollback();
                    } catch (Exception ex2) {
                    }
                } finally {
                    //com.geopista.server.database.COperacionesDatabaseOcupaciones.safeClose(rsSQL, preparedStatement, connection);
                }
                return resultado;
            }

    public static com.geopista.protocol.CResultadoOperacion getDireccionMasCercanaContaminantesOracle(Connection connection, String idContaminante, int idMunicipio, int ssrid)
     {
		PreparedStatement preparedStatement = null;
		ResultSet rsSQL = null;
		com.geopista.protocol.CResultadoOperacion resultado;
		try {
			logger.debug("Inicio de conseguir la dirección mas cercana Oracle:" + idContaminante);
			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
				return new com.geopista.protocol.CResultadoOperacion(false, "No se puede obtener la conexión");
			}
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement("select GEOMETRY from actividad_contaminante where id=?");
            ////
            SRManager manager = OraSpatialManager.getSpatialReferenceManager((oracle.jdbc.OracleConnection)((org.enhydra.jdbc.core.CoreConnection)connection).con);
            SpatialReference sr= manager.retrieve(ssrid);
            GeometryFactory gFact= OraSpatialManager.getGeometryFactory(sr);
            AdapterSDO adaptersdo= new AdapterSDO(gFact,(oracle.jdbc.OracleConnection) ((org.enhydra.jdbc.core.CoreConnection)connection).con);
            AdapterJTS adapterJTS= new AdapterJTS(gFact);
            ///
			preparedStatement.setString(1, idContaminante);
            rsSQL=preparedStatement.executeQuery();
            if (!rsSQL.next())
            {
                resultado= new com.geopista.protocol.CResultadoOperacion(false, "No se ha encontrado la geometria");
            }
            else
            {
                com.vividsolutions.jts.geom.Geometry jts  =(com.vividsolutions.jts.geom.Geometry) adapterJTS.exportGeometry(com.vividsolutions.jts.geom.Geometry.class,
                adaptersdo.importGeometry(rsSQL.getObject("GEOMETRY")));
                preparedStatement.close();

                String sql="select np.id as id, np.id_via as id_via, np.rotulo as rotulo from "+
                "numeros_policia np where np.id_municipio =? "+
                "and (MDSYS.SDO_NN(geometry,?, 'sdo_num_res=1')= 'TRUE')";
                //com.geopista.server.database.COperacionesContaminantes.safeClose(null, preparedStatement, null);
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, idMunicipio);
                preparedStatement.setObject(2, new OracleConnection().getGeoObject(idMunicipio,jts, connection ));
                rsSQL=preparedStatement.executeQuery();
                if (!rsSQL.next())
                {
                        resultado= new com.geopista.protocol.CResultadoOperacion(false, "No existe ningún número de policia cercano");
                        logger.info("No existe ningún número de policia cercano ");
                }
                else
                {
                    String rotulo=rsSQL.getString("rotulo");
                    String id=rsSQL.getString("id");
                    String idVia =rsSQL.getString("id_via");
                    preparedStatement.close();
                    //com.geopista.server.database.COperacionesContaminantes.safeClose(null, preparedStatement, null);
                    preparedStatement = connection.prepareStatement("select tipovianormalizadocatastro, nombrecatastro from vias where ID=?");
                    preparedStatement.setString(1, idVia);
                    rsSQL=preparedStatement.executeQuery();
                    if (!rsSQL.next())
                    {
                            resultado= new com.geopista.protocol.CResultadoOperacion(false, "No se ha encontrado la geometria");
                            logger.info("no se ha encontrado datos para el id_via "+idVia);
                    }
                    else
                    {
                        com.geopista.protocol.contaminantes.NumeroPolicia numeroPolicia = new com.geopista.protocol.contaminantes.NumeroPolicia();
                        numeroPolicia.setId(id);
                        numeroPolicia.setId_via(idVia);
                        numeroPolicia.setNombrevia(rsSQL.getString("nombrecatastro"));
                        numeroPolicia.setRotulo(rotulo);
                        numeroPolicia.setTipovia(rsSQL.getString("tipovianormalizadocatastro"));
                        resultado=new com.geopista.protocol.CResultadoOperacion(true, "Operación ejecutada correctamente");
                        Vector aux= new Vector();
                        aux.add(numeroPolicia);
                        resultado.setVector(aux);
                    }

                 }
            }
			connection.commit();

		} catch (Exception e) {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("ERROR al obtener la direccion mas cercana:" + sw.toString());
			resultado = new com.geopista.protocol.CResultadoOperacion(false, e.getMessage());
			try {
				connection.rollback();
			} catch (Exception ex2) {
			}
		} finally {
			//com.geopista.server.database.COperacionesContaminantes.safeClose(rsSQL, preparedStatement, connection);
		}
		return resultado;
	}

    /** Incidecncia [365] */
    /** Retorna 1 - si la traduccion encontrada corresponde a la preferencia de idioma del usuario
     *          2 - si la traduccion encontrada corresponde al locale es_ES (segunda mejor opcion)
     *          3 - si es cualquier otro idioma (x_ES)
     */
    public static int getOpcionLocale(String locale, String sLocale){
        if (locale.equals(sLocale)){
            /** mejor opcion, sLocale */
            return 1;
        }else if (locale.equals("es_ES")){
            /** segunda mejor opcion, es_ES */
            return 2;
        }else{
            /** peor opcion, x_ES */
            return 3;
        }
    }


 /**
     * Inserta una nueva coverage layer en la tabla de coverage layers
     *
     * @param idMunicipio
     * @param idName
     * @param desc_path
     * @param srs
     * @param extension
     * @throws Exception
     */
    public void insertCoverageLayer(int idMunicipio, int idName, String desc_path, String srs, String extension)
    	throws Exception {

    	String sSQLInsertCoverageLayer = "insert into coverage_layers (id_municipio, id_name, desc_path, srs, extension) values (?,?,?,?,?)";
    	String sSQLDeleteCoverageLayer = "delete from coverage_layers where id_municipio=?";
    	Connection conn = null;
		conn=openConnection();
        PreparedStatement preparedStmt=null;
        ResultSet rSet=null;

        try {
        	preparedStmt=conn.prepareStatement(sSQLDeleteCoverageLayer);
	        preparedStmt.setInt(1, idMunicipio);
	        preparedStmt.executeUpdate();
	        preparedStmt = null;

	        preparedStmt=conn.prepareStatement(sSQLInsertCoverageLayer);
	        preparedStmt.setInt(1, idMunicipio);
	        preparedStmt.setInt(2, idName);
	        preparedStmt.setString(3, desc_path);
            preparedStmt.setString(4, srs);
            preparedStmt.setString(5, extension);
	        preparedStmt.executeUpdate();
	        conn.commit();
        } catch (Exception e) {
        	logger.error("insertCoverageLayer: "+ e.getMessage());
        	throw e;
        } finally {
	        try{rSet.close();}catch(Exception e){};
	        try{preparedStmt.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }
    }

    public int getNextDictionaryId() throws Exception {

    	String sec = "select nextval('seq_dictionary') as next";
    	Connection conn = null;
		conn=openConnection();
        PreparedStatement preparedStmt=null;
        ResultSet rSet=null;
        try {
        	preparedStmt=conn.prepareStatement(sec);
	        rSet = preparedStmt.executeQuery();
	        rSet.next();
	        int res = rSet.getInt("next");

	        return res;
        } catch (Exception e) {
        	logger.error("getNextDictionaryId: "+ e.getMessage());
        	throw e;
        } finally {
	        try{rSet.close();}catch(Exception e){};
	        try{preparedStmt.close();}catch(Exception e){};
	        try{conn.close();}catch(Exception e){};
	    }
    }

    public void insertVocablo(int idVocablo, String locale, String traduccion)
	throws Exception {

	String sSQLInsertVocablo = "insert into dictionary values (?,?,?)";
	Connection conn = null;
	conn=openConnection();
    PreparedStatement preparedStmt=null;
    ResultSet rSet=null;

    try {
    	preparedStmt=conn.prepareStatement(sSQLInsertVocablo);
        preparedStmt.setInt(1, idVocablo);
        preparedStmt.setString(2, locale);
        preparedStmt.setString(3, traduccion);
        preparedStmt.executeUpdate();
        conn.commit();
    } catch (Exception e) {
    	logger.error("insertVocablo: "+ e.getMessage());
    	throw e;
    } finally {
        try{rSet.close();}catch(Exception e){};
        try{preparedStmt.close();}catch(Exception e){};
        try{conn.close();}catch(Exception e){};
    }
}
    public FeatureLockResult lockFeature(int iMunicipio, List layers, List featuresIds, int iUserId) throws Exception{
    	return new FeatureLocker().lock(iMunicipio, layers, featuresIds, iUserId);
    }

    public List unlockFeature(int iMunicipio, List layers, List featuresIds, int iUser){
    	ArrayList unlockResultList = new ArrayList();

        int unlockResult = AdministradorCartografiaClient.UNLOCK_FEATURE_UNLOCKED;

        String sqlDeleteFeatureLock="delete from locks_feature where " +
        	"municipio=? and " +
        	"layer=? and " +
        	"user_id=? and " +
        	"feature_id=?";

        //Comprobacion de si el feature esta cerrado
        String sqlCheckFeatureLock="select user_id,ts from locks_feature " +
        	"where " +
        	"municipio=? and " +
        	"layer=? and " +
        	"feature_id=?";

        PreparedStatement psDeleteFeatureLock = null;
        PreparedStatement psCheckFeatureLock = null;
        ResultSet rs = null;
        Connection conn=null;

        try{
        	conn = openConnection();
        	psDeleteFeatureLock = conn.prepareStatement(sqlDeleteFeatureLock);
        	psCheckFeatureLock = conn.prepareStatement(sqlCheckFeatureLock);

        	for (int i = 0; i < featuresIds.size(); i++){
        		String layer = (String) layers.get(i);
        		int feature = ((Integer) featuresIds.get(i)).intValue();

        		int lockingUserId = -1;

        		psCheckFeatureLock.setInt(1,iMunicipio);
            	psCheckFeatureLock.setString(2,layer);
            	psCheckFeatureLock.setInt(3,feature);

                rs = psCheckFeatureLock.executeQuery();
                if (rs.next()){
                	lockingUserId=rs.getInt("user_id");
                }
                rs.close();

        		if (lockingUserId==-1){
        			unlockResult = AdministradorCartografiaClient.UNLOCK_FEATURE_NOTLOCKED;
        		}else{
        			psDeleteFeatureLock.setInt(1,iMunicipio);
        			psDeleteFeatureLock.setString(2,layer);
        			psDeleteFeatureLock.setInt(3,iUser);
        			psDeleteFeatureLock.setInt(4,feature);

        			unlockResult = psDeleteFeatureLock.executeUpdate();
        		}
        		unlockResultList.add(new Integer(unlockResult ));
        	}
        } catch(SQLException se){
        	unlockResult = AdministradorCartografiaClient.UNLOCK_FEATURE_ERROR;
        	unlockResultList.add(new Integer(unlockResult ));
        	logger.error("unlockFeature: " +se.getMessage());
        	logger.error("unlockFeature(int, String, int, int)", se);
        } finally {
        	try{psDeleteFeatureLock.close();}catch(Exception e){};
        	try{psCheckFeatureLock.close();}catch(Exception e){};
        	try{rs.close();}catch(Exception e){};
        	try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
        }

        return unlockResultList;
    }

    // Esta es una clase auxiliar. No es thread safe. Para utilizar, instanciar un objeto
    // y realizar el lock
    private class FeatureLocker {

    	// Insercion de un lock para la feature
        private String sqlInsertFeatureLock = "insert into locks_feature (municipio,layer,feature_id,user_id,ts) "
            +"values (?,?,?,?,?)";
        //Obtener la tabla donde esta la geometria...
        private String sqlFindGeometryTable = "select t.name as tabla,l.id_layer from tables t,columns c,attributes a,layers l " +
    		"where a.id_layer=l.id_layer and l.name=? and c.id_table=t.id_table and c.name='GEOMETRY' and a.id_column=c.id";

        //Comprobacion de si el feature esta cerrado
        private  String sqlCheckFeatureLock = "select user_id,ts from locks_feature " +
    		"where " +
    		"municipio=? and " +
    		"layer=? and " +
    		"feature_id=?";

        int lockingUserId = -1;

    	Connection conn;

    	Thread[] insertLockThreads;

    	FeatureLockResult featureLockResult;

    	public FeatureLockResult lock(int iMunicipio, List layers, List featuresIds, int iUserId) {
        	ArrayList featuresToLock = new ArrayList();
            int lockStatus = AdministradorCartografiaClient.LOCK_FEATURE_LOCKED;

            featureLockResult = null;
            PreparedStatement psFindGeometryTable = null;
            PreparedStatement psInsertFeatureLock = null;
            PreparedStatement psCheckFeatureLock = null;

            try {
            	try{
            		conn = openConnection();
            		psFindGeometryTable = conn.prepareStatement(sqlFindGeometryTable);
            		psInsertFeatureLock = conn.prepareStatement(sqlInsertFeatureLock);
            		psCheckFeatureLock = conn.prepareStatement(sqlCheckFeatureLock);

            		for(int i = 0; i < featuresIds.size(); i++){
            			String layer = (String) layers.get(i);
            			int featureId = ((Integer) featuresIds.get(i)).intValue();

            			lockStatus = checkFeatureLockStatus(psFindGeometryTable,psCheckFeatureLock,
            					iMunicipio,layer,featureId,iUserId);

            			if (lockStatus == 0){
            				featuresToLock.add(new Integer(i));
            			}

            			if (lockStatus != AdministradorCartografiaClient.LOCK_FEATURE_LOCKED
            					&& lockStatus != AdministradorCartografiaClient.LOCK_FEATURE_OWN
            					&& lockStatus != AdministradorCartografiaClient.LOCK_LAYER_OWN) {

            				featureLockResult = new FeatureLockResult(lockStatus, layer, featureId);

            				if (lockStatus == AdministradorCartografiaClient.LOCK_FEATURE_OTHER){
            					addLockOwnerUserInfo(featureLockResult, lockingUserId);
            				}

            				return featureLockResult;
            			}
            		}
            	} catch (ACException e) {
            		logger.error("lockFeature: " +e.getMessage());
            		logger.error("lockFeature(int, String, int, int)", e);

            		featureLockResult = new FeatureLockResult(AdministradorCartografiaClient.LOCK_FEATURE_ERROR);
            		return featureLockResult;
                } catch(SQLException se) {
            		logger.error("lockFeature: " +se.getMessage());
            		logger.error("lockFeature(int, String, int, int)", se);

            		featureLockResult = new FeatureLockResult(AdministradorCartografiaClient.LOCK_FEATURE_ERROR);
            		return featureLockResult;
            	} finally{
            		try{psFindGeometryTable.close();}catch(Exception e){};
            		try{psInsertFeatureLock.close();}catch(Exception e){};
            		try{psCheckFeatureLock.close();}catch(Exception e){};
            	}

            	try {
            		int numberOfThreads = 4;
            		int numberOfFeaturesPerThread = featuresToLock.size()/ numberOfThreads;

            		conn.setAutoCommit(false);

            		insertLockThreads = new InsertLockThread[numberOfThreads];

            		for (int i = 0; i < insertLockThreads.length; i++){
            			int first = 0 + i*(numberOfFeaturesPerThread);
            			int last = i*(numberOfFeaturesPerThread) + (numberOfFeaturesPerThread);

            			if (i == insertLockThreads.length - 1){
            				last = featuresToLock.size();
            			}

            			insertLockThreads[i] = new InsertLockThread(conn, featuresToLock, featuresIds,
            					layers, iMunicipio,	iUserId, first, last);

            			insertLockThreads[i].start();
            		}

            		// Espero a que finalicen los hilos
            		for (int i = 0; i < insertLockThreads.length; i++){
            			insertLockThreads[i].join();
            		}

            		if (featureLockResult == null){
            			conn.commit();
            			return new FeatureLockResult(AdministradorCartografiaClient.LOCK_FEATURE_LOCKED);
            		}
            		else {
            			// Retornamos el resultado de error devuelto por uno de los hilos
            			try{conn.rollback();} catch (SQLException e){}
            			return featureLockResult;
            		}
            	} catch (SQLException e){
            		try{conn.rollback();} catch (SQLException se){}
            		logger.error("lockFeature: " +e.getMessage());
            		logger.error("lockFeature(int, String, int, int)", e);

            		featureLockResult = new FeatureLockResult(AdministradorCartografiaClient.LOCK_FEATURE_ERROR);
            		return featureLockResult;
            	} catch (InterruptedException e) {
            		try{conn.rollback();} catch (SQLException se){}
            		logger.error("lockFeature: " +e.getMessage());
            		logger.error("lockFeature(int, String, int, int)", e);

            		featureLockResult = new FeatureLockResult(AdministradorCartografiaClient.LOCK_FEATURE_ERROR);
            		return featureLockResult;
				}
            } finally {
            	try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
            }
    	}

        private int checkFeatureLockStatus(PreparedStatement psFindGeometryTable,
        		PreparedStatement psCheckFeatureLock,
        		int iMunicipio, String layer, int feature, int userId)
        		throws SQLException, ACException{

        	int lockStatus=0;
            PreparedStatement psGeom = null;
            ResultSet rs=null;
            try{
            	// Comparar ids de Feature y bloqueos de layer con la geometria de iFeature...
        		lockingUserId = -1;

        		psCheckFeatureLock.setInt(1,iMunicipio);
            	psCheckFeatureLock.setString(2,layer);
            	psCheckFeatureLock.setInt(3,feature);

                rs = psCheckFeatureLock.executeQuery();
                if (rs.next()){
                	lockingUserId=rs.getInt("user_id");
                }
                rs.close();

                if(lockingUserId != -1){
                    return (lockingUserId==userId?AdministradorCartografiaClient.LOCK_FEATURE_OWN
                                               :AdministradorCartografiaClient.LOCK_FEATURE_OTHER);
                }

                psFindGeometryTable.setString(1,layer);
                rs = psFindGeometryTable.executeQuery();
                String geometryTable = null;

                if (rs.next()){
                    geometryTable = rs.getString("table");
                }

                rs.close();

                String sqlGeom = "select ll.user_id from locks_layer ll,"+geometryTable+" t " +
            		"where t.id=? and ll.municipio=? and ll.layer=? " +
            		" and sdo_relate(t.geometry,ll.geometry, 'mask=anyinteract querytype=window') = 'TRUE'";

                psGeom=conn.prepareStatement(sqlGeom);
                psGeom.setInt(1,feature);
                psGeom.setInt(2,iMunicipio);
                psGeom.setString(3,layer);

                rs = psGeom.executeQuery();

                if (rs.next()){
                    lockStatus=(rs.getInt("user_id")!=userId)? AdministradorCartografiaClient.LOCK_LAYER_OTHER
                                                      : AdministradorCartografiaClient.LOCK_LAYER_OWN;
                }

                rs.close();
            }finally{
                try{rs.close();}catch(Exception e){};
                try{psGeom.close();}catch(Exception e){};
            }
            return lockStatus;
        }

        private class InsertLockThread extends Thread{
        	List featuresIds = null;
        	List layers = null;
        	List featuresToLock = null;
        	int municipio;
        	int userId;
        	int first;
        	int last;

        	public InsertLockThread(Connection conn, List featuresToLock, List featuresIds,
        			List layers, int municipio, int userId, int first, int last){
        		super();
        		this.featuresIds = featuresIds;
        		this.layers = layers;
        		this.featuresToLock = featuresToLock;
        		this.municipio = municipio;
        		this.userId = userId;
        		this.first = first;
        		this.last = last;
        	}

        	public void run() {
        		PreparedStatement psInsertFeatureLock = null;
        		String layer = null;
        		int featureId = -1;

        		try {

        			psInsertFeatureLock =
        				conn.prepareStatement(sqlInsertFeatureLock);

        			for (int i = first; i < last; i++){
        				int index = ((Integer) featuresToLock.get(i)).intValue();
        				layer = (String) layers.get(index);
        				featureId = ((Integer) featuresIds.get(index)).intValue();

        				psInsertFeatureLock.setInt(1,municipio);
        				psInsertFeatureLock.setString(2,layer);
        				psInsertFeatureLock.setInt(3,featureId);
        				psInsertFeatureLock.setInt(4,userId);
        				psInsertFeatureLock.setTimestamp(5,new Timestamp(System.currentTimeMillis()));
        				psInsertFeatureLock.executeUpdate();

        				if (Thread.interrupted()){
        					return;
        				}
        			}

        		} catch (SQLException e){
        			int errorCode = AdministradorCartografiaClient.LOCK_FEATURE_ERROR;
        			featureLockErrorListener(this, errorCode, layer, featureId);
        		} finally {
        			try{psInsertFeatureLock.close();}catch(Exception e){};
        		}
        	}
        }

        private synchronized void featureLockErrorListener(Thread failingThread,
        		int errorCode, String layer, int featureId){
        	// Si otro thread tambien obtuvo un error, solo se considerara
        	// el error del primer thread que invoque a esta funcion
        	if (Thread.interrupted()){
        		return;
        	}

        	for (int i = 0; i < insertLockThreads.length; i++){
        		if (insertLockThreads[i] != failingThread){
        			insertLockThreads[i].interrupt();
        		}
        	}

        	featureLockResult = new FeatureLockResult(errorCode, layer, featureId);

        	return;
        }

        private void addLockOwnerUserInfo(FeatureLockResult lockResult, int lockOwnerUserId){
        	String sqlFindUserName = "SELECT u.name FROM iuseruserhdr u WHERE u.id=?";
        	PreparedStatement psFindUserName = null;
        	ResultSet rs = null;
        	try {

        		psFindUserName = conn.prepareStatement(sqlFindUserName);
        		psFindUserName.setInt(1, lockOwnerUserId);
        		rs = psFindUserName.executeQuery();

        		String lockOwnerUserName = null;
                if (rs.next()){
                	lockOwnerUserName = rs.getString("name");
                }

                lockResult.setLockOwnerUserName(lockOwnerUserName);
                lockResult.setLockOwnerUserId(new Integer(lockOwnerUserId));

        	} catch (SQLException e) {
				logger.error("Error al obtener el nombre del usuario que esta bloqueando el feature", e);
				e.printStackTrace();
			}
        	finally {
        		try{rs.close();}catch(Exception e){};
                try{psFindUserName.close();}catch(Exception e){};
        	}
        }
    }

	private ArrayList getValidateFeatures(ACLayer acLayer, String sLocale) throws Exception {
		return getValidateFeatures(acLayer,sLocale,null);
	}

    private ArrayList getValidateFeatures(ACLayer acLayer, String sLocale,Connection connection) throws Exception {

		ArrayList lstValidates = null;

		String sSQL = "select l.validator, d.locale "
			+ "from layers l, dictionary d "
			+ "where l.name=? and l.id_name=d.id_vocablo ";

		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		boolean bCerrarConexion=true;
		try{
        	if (connection!=null)
            {
                conn=connection;
                bCerrarConexion=false;
            }
            else
            {
            	conn=openConnection();
                bCerrarConexion=true;
            }
			//conn=openConnection();
			ps=conn.prepareStatement(sSQL);
            ps.setString(1,acLayer.getSystemName());
            rs=ps.executeQuery();

            if (rs.next()){

                int iCurrentLayerLocale= -1;
                int iLastLayerLocale=4;
                boolean mejorOpcion= false;

                AbstractValidator abstractValidator = null;
                String sValidator = null;

                do{
                    iCurrentLayerLocale= getOpcionLocale(rs.getString("locale"), sLocale);
                    /** Priorizamos los idiomas: preferencia de usuario, es_ES, resto de idiomas */
                    if (iCurrentLayerLocale < iLastLayerLocale){

                    	sValidator = rs.getString("validator");

                        iLastLayerLocale= iCurrentLayerLocale;
                        if (iCurrentLayerLocale == 1) mejorOpcion= true;
                    }
                }while ((rs.next()) && (!mejorOpcion));

                String sValidates[] = null;

                if (sValidator != null && !sValidator.equals("")){
                	lstValidates = new ArrayList();
                	sValidates = sValidator.split(";");
                	for(int i=0;i<sValidates.length;i++){
                		abstractValidator = (AbstractValidator) Class
        				.forName(sValidates[i]).newInstance();
                		lstValidates.add(abstractValidator);
                	}
                }
            }
            return lstValidates;

		}catch(Exception e){
			try{conn.rollback();}catch(Exception ex){};
			logger.error("insertFeature: "+e.getMessage());
			logger.error("updateFeature(int, String, String, ACFeatureUpload, boolean, ObjectOutputStream, Sesion)",
					e);
			throw e;
		}finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{if (bCerrarConexion){conn.close();CPoolDatabase.releaseConexion();}}catch(Exception e1){};
			/*try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};*/

		}

	}


    /**
     * Se coge de la base de datos el campo maps.xml. Si el nodo mapProjection es "Unspecified",
     * se sustituye éste por el sistema de coordenadas correspondiente
     * @param sArgs
     * @throws Exception
     */
    private String updateProjection(Reader xml, int idSrid) throws Exception{
    	SAXBuilder builder = new SAXBuilder(false);
        Document docNew = builder.build(xml);
        Element rootElement = docNew.getRootElement();
        Element elemento = (Element)rootElement.getChild("mapProjection");
        if (elemento.getName().equals("mapProjection") && (elemento.getText().equals("")||elemento.getText().equals("Unspecified"))){
	        	if (idSrid == UTM_30N_ED50)
	        		elemento.setText("UTM 30N ED50");
	        	else if (idSrid==UTM_29N_ED50)
	        		elemento.setText("UTM 29N ED50");
	        	else if (idSrid ==UTM_31N_ED50)
	        		elemento.setText("UTM 31N ED50");
	        	else if (idSrid==UTM_29N_ETRS89)
	        		elemento.setText("UTM 29N ETRS89");
	        	else if (idSrid ==UTM_31N_ETRS89)
	        		elemento.setText("UTM 31N ETRS89");
	        	else if (idSrid==UTM_30N_ETRS89)
	        		elemento.setText("UTM 30N ETRS89");
	        }
            XMLOutputter outp = new XMLOutputter();
	        return outp.outputString(docNew);
    }




	public int insertFeature(int municipio, String layer, String locale,
			ACFeatureUpload upload, ObjectOutputStream oos, ISesion sesion,
			boolean validateData) throws Exception {
        /*
         * TODO De momento llamamos a la funcion original aunque habría que ver
         * si hay que hacer una nueva implementacion
         */
		return insertFeature(municipio, layer, locale, upload, oos, sesion);
	}



    public int insertFeature(int iMunicipio, String sLayer, String sLocale, ACFeatureUpload upload,
            ObjectOutputStream oos, ISesion sesion, boolean bValidateData, Integer sridDestino)
            throws Exception {
        /*
         * TODO De momento llamamos a la funcion original aunque habría que ver
         * si hay que hacer una nueva implementacion
         */
        return insertFeature(iMunicipio, sLayer, sLocale, upload, oos, sesion);
    }

	public int procesarLoteActualizacion(int municipio, String layer,
			String locale, ACFeatureUpload[] upload, ObjectOutputStream oos,
			ISesion sesion, int userID, boolean loadData, boolean validateData)
			throws Exception {
        /*
         * TODO De momento llamamos a la funcion original aunque habría que ver
         * si hay que hacer una nueva implementacion
         */
		return procesarLoteActualizacion(municipio, layer, locale, upload, oos, sesion, userID, loadData);
	}

	   public int procesarLoteActualizacion(int municipio, String layer,
	            String locale, ACFeatureUpload[] upload, ObjectOutputStream oos,
	            ISesion sesion, int userID, boolean loadData, boolean validateData, Integer srid_destino)
	            throws Exception {
	        /*
	         * TODO De momento llamamos a la funcion original aunque habría que ver
	         * si hay que hacer una nueva implementacion
	         */
	        return procesarLoteActualizacion(municipio, layer, locale, upload, oos, sesion, userID, loadData);
	    }

	   public int procesarLoteActualizacion(int municipio, String layer,
	            String locale, ACFeatureUpload[] upload, ObjectOutputStream oos,
	            ISesion sesion, int userID, boolean loadData, boolean validateData, Integer srid_destino, boolean bImportacion)
	            throws Exception {
	        /*
	         * TODO De momento llamamos a la funcion original aunque habría que ver
	         * si hay que hacer una nueva implementacion
	         */
	        return procesarLoteActualizacion(municipio, layer, locale, upload, oos, sesion, userID, loadData);
	    }

	   public int updateFeature(int municipio, String layer, String locale,
			ACFeatureUpload upload, boolean delete, ObjectOutputStream oos,
			ISesion sesion, boolean validateData) throws Exception {
        /*
         * TODO De momento llamamos a la funcion original aunque habría que ver
         * si hay que hacer una nueva implementacion
         */
		return updateFeature(municipio, layer, locale, upload, delete, oos, sesion);
	}


	   public int updateFeature(int municipio, String layer, String locale,
	            ACFeatureUpload upload, boolean delete, ObjectOutputStream oos,
	            ISesion sesion, boolean validateData, Integer srid_destino) throws Exception {
	        /*
	         * TODO De momento llamamos a la funcion original aunque habría que ver
	         * si hay que hacer una nueva implementacion
	         */
	        return updateFeature(municipio, layer, locale, upload, delete, oos, sesion);
	    }

	public void returnLayer(int municipio, String layer, String locale,
			Geometry geom, FilterNode fn, ObjectOutputStream oos,
			ISesion sesion, boolean validateData) throws IOException,
			SQLException, Exception {
        /*
         * TODO De momento llamamos a la funcion original aunque habría que ver
         * si hay que hacer una nueva implementacion
         */
	    returnLayer(municipio, layer, locale, geom, fn, oos, sesion, true, null, null);

	}
	public void returnGeoRef(ObjectOutputStream oos, String tipoVia,
			String via, String numPoli, String locale, ISesion sesion,
			boolean validateData) throws Exception {
        /*
         * TODO De momento llamamos a la funcion original aunque habría que ver
         * si hay que hacer una nueva implementacion
         */
	    returnGeoRef(oos, tipoVia, via, numPoli, locale, sesion);
	}
	public ListaUsuarios getUsuarios(int iEntidad) throws SQLException {
        /*
         * TODO De momento llamamos a la funcion original aunque habría que ver
         * si hay que hacer una nueva implementacion
         */
		return getUsuarios(iEntidad);
	}

    /**
     * Devuelve una lista con todos los usuarios con permiso de lectura sobre las capas indicadas
     * @param idMunicipio
     * @param listaCapas
     * @return
     * @throws SQLException
     */
    public ListaUsuarios getUsuariosPermisosCapas(List<Integer> listaCapas, int iEntidad)throws SQLException{
        /*
         * TODO De momento llamamos a la funcion original aunque habría que ver
         * si hay que hacer una nueva implementacion
         */
		return getUsuariosPermisosCapas(listaCapas, iEntidad);
   }

    /**
     * Crea un nuevo proyecto de extracción en BBDD
     * @param eProject
     * @throws SQLException
     */
    public void crearProyectoExtraccion(ExtractionProject eProject, int idEntidad)throws SQLException{
    	/*
         * TODO De momento llamamos a la funcion original aunque habría que ver
         * si hay que hacer una nueva implementacion
         */
		crearProyectoExtraccion(eProject, idEntidad);
    }

    public void deleteProyectoExtraccion(ExtractionProject eProject, int idEntidad)throws SQLException{
    	/*
         * TODO De momento llamamos a la funcion original aunque habría que ver
         * si hay que hacer una nueva implementacion
         */
    	deleteProyectoExtraccion(eProject, idEntidad);
    }

    /**
     * Devuelve una lista con los proyectos de desconexión asociados a un mapa
     */
    public List<ExtractionProject> getProyectosExtraccion(int idMapa, int idEntidad)throws SQLException{
    	/*
         * TODO De momento llamamos a la funcion original aunque habría que ver
         * si hay que hacer una nueva implementacion
         */
    	return getProyectosExtraccion(idMapa, idEntidad);
   }

    /**
     * Asigna los usuarios a las celdas del proyecto de extracción
     */
	public void asignarCeldasProyectoExtraccion(String idProyectoExtract, HashMap<String, String> celdasUsuarios) throws SQLException {
    	/*
         * TODO De momento llamamos a la funcion original aunque habría que ver
         * si hay que hacer una nueva implementacion
         */
    	asignarCeldasProyectoExtraccion(idProyectoExtract, celdasUsuarios);
	}

	/**
	 * Obtiene las celdas asignadas de un proyecto de extracción
	 */
	public HashMap<String, String> obtenerCeldasProyectoExtraccion(String idProyectoExtract)throws SQLException {
    	/*
         * TODO De momento llamamos a la funcion original aunque habría que ver
         * si hay que hacer una nueva implementacion
         */
    	return obtenerCeldasProyectoExtraccion(idProyectoExtract);
	}

	public String getSRIDDefecto(boolean defecto, int idEntidad) throws FileNotFoundException,IOException,ACException,SQLException{
		return getSRIDDefecto(defecto, idEntidad);

	}

	public int getSRIDDefecto(boolean defecto, Connection conn, String idEntidad) throws FileNotFoundException,IOException,ACException{
		return getSRIDDefecto(defecto, conn, idEntidad);
	}


    public void searchByAttribute(String idEntidad, int iMunicipio, String sLayer, String sLocale,
            FilterNode fn, ObjectOutputStream oos, ISesion sesion, String attributeName, String attributeValue,
            boolean bValidateData, Integer sridDestino) throws IOException, SQLException, Exception {
        searchByAttribute(idEntidad, iMunicipio, sLayer, sLocale, fn, oos, sesion, attributeName,
                attributeValue, bValidateData, sridDestino);
    }

    public void loadFeatures(int iMunicipio,String sLayer,String sLocale, Geometry geom,FilterNode fn,ObjectOutputStream oos,ISesion sesion,boolean bValidateData,List lMunicipalities, Integer srid)
    	throws IOException,SQLException, Exception{}
	public int procesarLoteActualizacion(int iMunicipio, String sLayer,
			String sLocale, ACFeatureUpload[] aUpload, ObjectOutputStream oos,
			ISesion sesion, int iUserID, boolean bLoadData,
			boolean bValidateData, Integer srid_destino, Hashtable params)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
