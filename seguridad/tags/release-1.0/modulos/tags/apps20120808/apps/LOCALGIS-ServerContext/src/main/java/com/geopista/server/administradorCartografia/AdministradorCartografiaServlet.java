package com.geopista.server.administradorCartografia;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.exolab.castor.xml.Unmarshaller;
//import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;

import admcarApp.PasarelaAdmcar;

import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.protocol.Version;
import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.CPoolDatabase;
import com.geopista.ui.dialogs.beans.ExtractionProject;
import com.localgis.server.SessionsContextShared;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.io.WKTReader;


public class AdministradorCartografiaServlet extends LoggerHttpServlet {
  	private static final long serialVersionUID = -4142109008606570489L;
	//private static SRID srid=null;
    private static NewSRID srid = null;
    private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AdministradorCartografiaServlet.class);
    
    GeopistaConnection geoConn;
    private static boolean dbOracle=false;

    public void init(){
        try{
            if (srid==null)
//                srid=new SRID(Const.SRID_PROPERTIES);
                srid = new NewSRID();
            Connection conn=CPoolDatabase.getConnection();
            if (conn!=null){
	            if (((org.postgresql.jdbc3.Jdbc3Connection)conn) instanceof org.postgresql.PGConnection)
	                dbOracle=false;
	            else
	                dbOracle=true;
	            conn.close();
	            CPoolDatabase.releaseConexion();
	            logger.info("Cargando dominios.....");
	            if(GeopistaDomains.hmMunicipios.size()==0)
	                reloadDomains();
	            logger.info("Dominios cargados");
            }
            else{
            	logger.info("Conexion contra la BD no disponible");
            }
        } catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    //REFACTORIZACION ORACLE public static synchronized void reloadDomains()throws SQLException{
    public static synchronized void reloadDomains()throws Exception{
        
        /** REFACTORIZACION ORACLE */
        GeopistaConnectionFactory.getInstance(srid, dbOracle).loadDomains();
    }

    private ISesion sesion(HttpServletRequest request){
        ISesion sRet=null;
        JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
        if (userPrincipal!=null){
            String  sIdSesion= userPrincipal.getName();
            
            PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");    
        	
            sRet=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
        }
        return sRet;
    }

    private int idMunicipio(ISesion sSesion, HttpServletRequest request, Hashtable params){
        int iRet=-1;
        try{
        	if (params.containsKey(Const.KEY_MUNICIPIO))
            	iRet=Integer.parseInt((String)params.get(Const.KEY_MUNICIPIO));
            else if (request.getParameter(EnviarSeguro.idMunicipio)!=null)
            	iRet = Integer.parseInt(request.getParameter(EnviarSeguro.idMunicipio));
            if (iRet!=-1 && sSesion!=null){
        		sSesion.setIdMunicipio(new Integer(iRet).toString());      
        		PasarelaAdmcar.listaSesiones.getSesion(sSesion.getIdSesion()).setIdMunicipio(new Integer(iRet).toString());
        		SessionsContextShared.getContextShared().setSharedAttribute(this.getServletContext(), "UserSessions", PasarelaAdmcar.listaSesiones);    
            }
            if (iRet==-1 && sSesion!=null && sSesion.getIdMunicipio()!=null ){
            	return new Integer(sSesion.getIdMunicipio()).intValue();
            }
            	
 
        }catch (Exception e){
            log(e.getMessage());
        }
        return iRet;
    }
    
    private String getMunicipalities(List lMunicipalities){
    	if (lMunicipalities!=null){
	    	Iterator itMunicipios = lMunicipalities.iterator();
			String sMunicipios = "";
			while (itMunicipios.hasNext()){
				Municipio municipio = (Municipio)itMunicipios.next();
				if (!sMunicipios.equals(""))
					sMunicipios += ",";
				sMunicipios += String.valueOf(municipio.getId());
			}
			return sMunicipios;
    	}
    	return null;
    }

    //Devuelve la entidad supramunicipal relacionada con la sesión
    private int idEntidadSupramunicipal(HttpServletRequest request){
        int iRet=-1;
        try{
        	iRet=Integer.parseInt(sesion(request).getIdEntidad());            
        }catch (Exception e){
        }
        return iRet;
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	super.doPost(request);
        ObjectOutputStream oos=null;
        try{
        	
            logger.info("Entrando en doPost de AdministradorCartografiaServlet");

            GeopistaConnection geoConn=null;
            ACQuery query= new ACQuery();
            if(!org.apache.commons.fileupload.DiskFileUpload.isMultipartContent(request)){
 
            	//iMunicipio = Integer.parseInt(request.getParameter(EnviarSeguro.idMunicipio));
                ACMessage abQuery=(ACMessage)Unmarshaller.unmarshal(ACMessage.class,new StringReader(request.getParameter("mensajeXML")));
                ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(abQuery.getQuery()));
                //ACQuery query=(ACQuery)ois.readObject();
                query=(ACQuery)ois.readObject();
              
            }else{
                /** -- MultiPartPost -- */

                // Parse the request

                // Create a new file upload handler
                org.apache.commons.fileupload.DiskFileUpload upload = new org.apache.commons.fileupload.DiskFileUpload();

                List items = upload.parseRequest(request);

                // Process the uploaded items
                Iterator iter = items.iterator();

                while (iter.hasNext()) {
                    org.apache.commons.fileupload.FileItem item = (org.apache.commons.fileupload.FileItem) iter.next();

                    if (item.isFormField()) {
  /*                     if (item.getFieldName().equalsIgnoreCase(EnviarSeguro.idMunicipio)){
                        	iMunicipio = Integer.parseInt(item.getString("ISO-8859-1"));
                        }*/
                       if (item.getFieldName().equalsIgnoreCase("mensajeXML")){
                           ACMessage abQuery=(ACMessage)Unmarshaller.unmarshal(ACMessage.class,new StringReader(item.getString("ISO-8859-1")));
                           ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(abQuery.getQuery()));
                           query=(ACQuery)ois.readObject();

                       }
                    }
                }

                /**/
            }

            
            /** REFACTORIZACION ORACLE*/
            geoConn= GeopistaConnectionFactory.getInstance(srid, dbOracle);

            int iEntidad=idEntidadSupramunicipal(request);
            response.setHeader("Transfer-encoding","chunked");
            response.setHeader("Content-Encoding", "gzip");
            oos=new ObjectOutputStream(new GZIPOutputStream(response.getOutputStream()));
            
            Hashtable params=query.getParams();
            if (params==null) return;
            
            String sLocale=(String)params.get(Const.KEY_LOCALE);
            Geometry g=null;
            ISesion sesion=sesion(request);
            
            int iMunicipio=idMunicipio(sesion, request, params); 
            logger.info("Id de Municipio Entrada:"+iMunicipio+" Municipalities:"+getMunicipalities((List)params.get(Const.KEY_MUNICIPALITIES))+" Action:"+query.getAction());
            int iUserID=Integer.parseInt(sesion.getIdUser());
            
            if (params.containsKey(Const.KEY_ID_ENTIDAD)){
            	iEntidad=(Integer)params.get(Const.KEY_ID_ENTIDAD);
            }
            if (params.containsKey(Const.KEY_GEOMETRY)){
                g=getGeometry((String)params.get(Const.KEY_GEOMETRY));
            }
            Integer srid = (Integer) params.get(Const.KEY_SRID);
            if (params.get(Const.KEY_VERSION) != null){
                Version version = (Version) params.get(Const.KEY_VERSION);
                version.setIdUsuario(Integer.parseInt(sesion.getIdUser()));
                sesion.setVersion(version);
            }else{
            	sesion.setVersion(null);
            }
            boolean bValidateData=true;
            switch (query.getAction()){
	            case Const.ACTION_SRID_DEFECTO:
	            	oos.writeObject(geoConn.getSRIDDefecto(true,iEntidad));
	            	break;
	            case Const.ACTION_SRID_INICIAL:
	            	oos.writeObject(geoConn.getSRIDDefecto(false,iEntidad));
	            	break;
                case Const.ACTION_LAYER:
                	
                	long startMils=Calendar.getInstance().getTimeInMillis();
                    String sLayer=(String)params.get(Const.KEY_LAYER);
                    
                	if (params.get(Const.KEY_LOAD_FEATURE_LAYER)!=null){
                		sesion.setLoadFeatureLayer((Integer)params.get(Const.KEY_LOAD_FEATURE_LAYER));
                	}
                	else{
                		sesion.setLoadFeatureLayer(1);
                	}
                	
                    try {bValidateData=((Boolean)params.get(Const.KEY_VALIDATE_DATA)).booleanValue();}catch(Exception e){}
                    geoConn.returnLayer(iEntidad,sLayer,sLocale,g,
                                                         (FilterNode)params.get(Const.KEY_FILTER),
                                                         oos,sesion,bValidateData,(List)params.get(Const.KEY_MUNICIPALITIES), srid);
                    long endMils=Calendar.getInstance().getTimeInMillis();
        			logger.info("Tiempo Total carga:"+sLayer+" :"+(endMils-startMils)+" mils"+" IdMunicipio:"+sesion.getIdMunicipio());
                    break;
                case Const.ACTION_LAYER_LOCK:
                    sLayer=(String)params.get(Const.KEY_LAYER);
                    oos.writeInt(geoConn.lockLayer(iMunicipio,sLayer,iUserID,
                                                          g!=null? g
                                                                 : geoConn.municipioGeometry(iMunicipio)));
                    break;
                case Const.ACTION_LAYER_UNLOCK:
                    sLayer=(String)params.get(Const.KEY_LAYER);
                    oos.writeInt(geoConn.unlockLayer(iMunicipio,sLayer,iUserID));
                    break;                
                case Const.ACTION_FEATURE_LOCK:
                    List layers =(List)params.get(Const.KEY_LAYER);
                    List featureIds = (List)params.get(Const.KEY_FEATURE);
                    oos.writeObject(geoConn.lockFeature(iMunicipio,layers,featureIds,iUserID));
                    break;
                case Const.ACTION_FEATURE_UNLOCK:
                	layers =(List)params.get(Const.KEY_LAYER);
                    featureIds = (List)params.get(Const.KEY_FEATURE);
                    oos.writeObject(geoConn.unlockFeature(iMunicipio,layers,featureIds,iUserID));
                    break;
                case Const.ACTION_MAP:
                    int iMap=Integer.parseInt((String)params.get(Const.KEY_MAP));
                    geoConn.returnMap(iEntidad,iMap,sLocale,oos,sesion);
                    break;
                case Const.ACTION_MAPS:
                	geoConn.returnMaps(sLocale,sesion,oos);                 	              		               	                 
                    break;
                case Const.ACTION_FEATURE:
                    sLayer=(String)params.get(Const.KEY_LAYER);
                    try {bValidateData=((Boolean)params.get(Const.KEY_VALIDATE_DATA)).booleanValue();}catch(Exception e){}
                    geoConn.loadFeatures(iEntidad,sLayer,sLocale,g,
                                         (FilterNode)params.get(Const.KEY_FILTER),
                                         oos,sesion,bValidateData,(List)params.get(Const.KEY_MUNICIPALITIES), srid);
                    break;
                case Const.ACTION_FEATURE_UPLOAD:
                    sLayer=(String)params.get(Const.KEY_LAYER);
                	iMunicipio = Integer.parseInt((String)params.get(Const.KEY_MUNICIPIO));
                	List listMunicipios = (List)params.get(Const.KEY_MUNICIPALITIES);
                	
                	
                	if (params.get(Const.KEY_ESTADO_VALIDACION)!=null){
                		sesion.setEstadoValidacion((Integer)params.get(Const.KEY_ESTADO_VALIDACION));
                	}

                	
                	sesion.setAlMunicipios(listMunicipios);
                    boolean bLoadData=true;
                    try {bLoadData=((Boolean)params.get(Const.KEY_LOAD_DATA)).booleanValue();}catch(Exception e){}
                    try {bValidateData=((Boolean)params.get(Const.KEY_VALIDATE_DATA)).booleanValue();}catch(Exception e){}
                    ACFeatureUpload[] aUpload=(ACFeatureUpload[])params.get(Const.KEY_UPLOAD);
                    int iUpdatedFeature=0;
                    if (aUpload.length==1) //si el numero de features a actualizar es igual a 1 lo dejamos como esa
                    {
                        if (aUpload[0].isNew()){ // Insert
//                            iUpdatedFeature=geoConn.insertFeature(iMunicipio,sLayer,sLocale,aUpload[0],oos,sesion,bValidateData);
                            iUpdatedFeature=geoConn.insertFeature(iMunicipio,sLayer,sLocale,aUpload[0],oos,sesion,bValidateData, srid);
                            if (iUpdatedFeature!=-1)
                                geoConn.log(iMunicipio,sLayer,iUserID,iUpdatedFeature,Const.HISTORY_ACTION_INSERT);
                        } else if (aUpload[0].isDirty()||aUpload[0].isDeleted()){ // Update o delete
                            iUpdatedFeature=geoConn.updateFeature(iMunicipio,sLayer,sLocale,aUpload[0],aUpload[0].isDeleted(),oos,sesion,bValidateData, srid);
                            geoConn.log(iMunicipio,sLayer,iUserID,iUpdatedFeature,aUpload[0].isDeleted()?Const.HISTORY_ACTION_DELETE:Const.HISTORY_ACTION_UPDATE);
                        }

                    }
                    else //procesamiento por lotes
                    {
                    	if (params.get(Const.KEY_IMPORTACIONES) != null)
                    		geoConn.procesarLoteActualizacion(iMunicipio,sLayer,sLocale,aUpload,oos,sesion, iUserID, bLoadData,true,srid,params);
                    	else
                    		geoConn.procesarLoteActualizacion(iMunicipio,sLayer,sLocale,aUpload,oos,sesion, iUserID, bLoadData,true,srid);
                    }
                    break;
                case Const.ACTION_STYLE_UPLOAD:
                    sLayer=(String)params.get(Const.KEY_LAYER);
                    String sXML=(String)params.get(Const.KEY_STYLE);
                    geoConn.updateStyle(sLayer, sXML,oos,sesion);
                    break;
                case Const.ACTION_MODIFIED_FEATURES:
                    sLayer=(String)params.get(Const.KEY_LAYER);
                    long lDate=Long.parseLong((String)params.get(Const.KEY_DATE));
                    geoConn.returnModifiedFeatureIDs(iMunicipio,sLayer,lDate,sLocale,oos);
                    break;
                case Const.ACTION_LAYERFAMILY_IDS_BYMAP:
                    iMap=Integer.parseInt((String)params.get(Const.KEY_MAP));
                    geoConn.returnLayerFamilyIDsByMap(iMap,oos);
                    break;
                case Const.ACTION_LAYERFAMILY_IDS:
                    geoConn.returnLayerFamilyIDs(oos);
                    break;
                case Const.ACTION_LAYER_IDS_BYMAP:
                    iMap=Integer.parseInt((String)params.get(Const.KEY_MAP));
                    int iPos=Integer.parseInt(sLayer=(String)params.get(Const.KEY_POSITION));
                    geoConn.returnLayerIDsByMap(iMap,iPos,oos,sesion);
                    break;
                case Const.ACTION_LAYER_IDS_BYLAYERFAMILY:
                    int iFamily=Integer.parseInt((String)params.get(Const.KEY_LAYERFAMILY));
                    geoConn.returnLayerIDsByFamily(iFamily,oos,sesion);
                    break;
                case Const.ACTION_MAP_UPLOAD:
                    ACMap acMap=(ACMap)params.get(Const.KEY_MAP_UPLOAD);       
                                      
                    //Si se pide que se guarde para la entidad 0 es que queremos actualizar el mapa base y no
                    //crear uno nuevo para la entidad. Para que esto suceda el usuario tiene que tener el permiso de
                    //Geopista.Map.CrearMapaGlobal
                    if ((acMap.getId()==null||acMap.getId().equals("")) && acMap.getIdEntidadSeleccionada().equals("0")){
                    	geoConn.insertMap(sesion,"0",acMap,sLocale,oos);
                    }
                    else if (acMap.getId()!=null && acMap.getIdEntidadSeleccionada().equals("0")){
                    	geoConn.updateMap(sesion,"0",acMap,sLocale,oos);
                    }
                    
                    //Distinguimos entre los mapas de sistema y los mapas que no lo son.
                    else if ((acMap.getId()==null||acMap.getId().equals("")) && acMap.idEntidad!=0){
                    	geoConn.insertMap(sesion,sesion.getIdEntidad(),acMap,sLocale,oos);
                    }                       
                    else if((acMap.getId()==null||acMap.getId().equals("")) && acMap.idEntidad==0){

                    	geoConn.insertMap(sesion,"0",acMap,sLocale,oos);                    	
                    }
                    else{
                    	if (acMap.idEntidad!=0){
                    		geoConn.updateMap(sesion,sesion.getIdEntidad(),acMap,sLocale,oos);
                    	}else if(acMap.idEntidad==0){
                    		geoConn.updateMap(sesion,"0",acMap,sLocale,oos);
                    	}                    	
                    }                        
                    break;
                    
                    
                    /** MODIFICACION DE SATEC (NO FUNCIONA)
                    //Distinguimos entre los mapas de sistema y los mapas que no lo son.
                    if ((acMap.getId()==null||acMap.getId().equals("")) && acMap.getName().contains("_SYSTEM")  ){
                    	geoConn.insertMap(sesion,"0",acMap,sLocale,oos);
                    }
                    else if((acMap.getId()==null||acMap.getId().equals("")) && acMap.idEntidad!=0){
                    	geoConn.insertMap(sesion,sesion.getIdEntidad(),acMap,sLocale,oos);
                    }                       
                    else if((acMap.getId()==null||acMap.getId().equals("")) && acMap.idEntidad==0){
         
                    	geoConn.insertMap(sesion,"0",acMap,sLocale,oos);                    	
                    }
                    else{
                    	//Recuperar la entidad a la que estaba asignado el mapa en BD antes de abrirlo en el editor
                        int sIdEntidad=-1;
                        String sXml="";
                        String sOldName="";
                    	try{
                        	Connection conn=null;
                        	PostGISConnection pgc=new PostGISConnection();
                        	conn=pgc.openConnection();    
                        	String sSQLIdEntidad="select id_entidad,xml from maps where id_map=?";
                        	
                        	PreparedStatement ps=conn.prepareStatement(sSQLIdEntidad);
                        	ps.setInt(1, Integer.parseInt(acMap.getId()));
                            ResultSet rs=ps.executeQuery();
                            if (rs.next()){
                            	sIdEntidad=rs.getInt("id_entidad");
                            	sXml=rs.getString("xml");
                            	sOldName=sXml.substring(sXml.indexOf("<mapName>"), sXml.indexOf("</mapName>"));
                            }
                    	}catch (Exception e) {
                    		logger.error("Error al actualiza mapa intentando recuperar idEntidad original del mapa ID "+acMap.getId());
                    	}
                    	
                    	//if (acMap.idEntidad!=0 && acMap.getName().contains("_SYSTEM") ) {
                    	if ( sOldName.contains("_SYSTEM") || !acMap.getName().contains("_SYSTEM") ){
                    		//Si el mapa actual (bd) contiene el sufijo SYSTEM y el nuevo NO, entonces crear nuevo mapa
                    		//para la entidad seleccionada
                    		geoConn.insertMap(sesion,sesion.getIdEntidad(),acMap,sLocale,oos);
                        }else if ( sOldName.contains("_SYSTEM") || acMap.getName().contains("_SYSTEM") ) {
                    		geoConn.updateMap(sesion,"0",acMap,sLocale,oos);
                    	}else if (acMap.idEntidad!=0){
                    		geoConn.updateMap(sesion,sesion.getIdEntidad(),acMap,sLocale,oos);
                    	}else if(acMap.idEntidad==0){
                    		geoConn.updateMap(sesion,"0",acMap,sLocale,oos);
                    	}        
                    	else {
                    		geoConn.updateMap(sesion,sesion.getIdEntidad(),acMap,sLocale,oos);
                    	}
                    }                        
                    break;
                    */
                case Const.ACTION_MAP_UPLOAD_MUNI:
                    acMap=(ACMap)params.get(Const.KEY_MAP_UPLOAD);
                    if (acMap.getId()==null||acMap.getId().equals(""))
                        geoConn.insertMap(sesion,sesion.getIdEntidad(),acMap,sLocale,oos);
                    else
                        geoConn.updateMap(sesion,sesion.getIdEntidad(),acMap,sLocale,oos);
                    break;
                case Const.ACTION_MAP_DELETE:
                    iMap=Integer.parseInt((String)params.get(Const.KEY_MAP));
                    geoConn.deleteMap(sesion,iMap,oos,sLocale);
                    break;
                case Const.ACTION_LAYERFAMILIES:
                    /** Incidencia[365] El administrador de cartografia no funciona para otros idiomas  */
                    //geoConn.returnLayerFamilies(oos);
                    geoConn.returnLayerFamilies(oos, sLocale);
                    break;
                case Const.ACTION_GEOREFADD:
                    //Devuelve un punto con la georeferenciación de un dirección;
                    String tipoVia=(String)params.get(Const.KEY_TIPO_VIA);
                    String via=(String)params.get(Const.KEY_VIA);
                    String numPoli=(String)params.get(Const.KEY_NUM_POLI);
                    try {bValidateData=((Boolean)params.get(Const.KEY_VALIDATE_DATA)).booleanValue();}catch(Exception e){}
                    geoConn.returnGeoRef(oos, tipoVia, via, numPoli,sLocale, sesion,bValidateData);
                    break;
                case Const.ACTION_GETUSERS:
//                    int idMunicipio=(Integer)params.get(Const.KEY_MUNICIPIO);
                    oos.writeObject(geoConn.getUsuarios(iEntidad));
                    break;
                case Const.ACTION_GETUSERSPERMLAYERS:
//                    idMunicipio=(Integer)params.get(Const.KEY_MUNICIPIO);
                    List<Integer> listaCapas=(List<Integer>)params.get(Const.KEY_ARRAY_CAPAS);
					oos.writeObject(geoConn.getUsuariosPermisosCapas(listaCapas, iEntidad));
                    break;
                case Const.ACTION_CREATEEXTRACTPROJECT:
                	ExtractionProject eProyect=(ExtractionProject)params.get(Const.KEY_EXTRACT_PROJECT);
					geoConn.crearProyectoExtraccion(eProyect, iEntidad);
                    break;
                case Const.ACTION_DELETEEXTRACTPROJECT:
                	ExtractionProject eDeleteProyect=(ExtractionProject)params.get(Const.KEY_EXTRACT_PROJECT);
					geoConn.deleteProyectoExtraccion(eDeleteProyect, iEntidad);
                    break;
                case Const.ACTION_GETEXTRACTPROJECTS:
                	Integer idMapa=(Integer)params.get(Const.KEY_MAP);
//                	idMunicipio=(Integer)params.get(Const.KEY_MUNICIPIO);
                	oos.writeObject(geoConn.getProyectosExtraccion(idMapa, iEntidad));
                    break;
                case Const.ACTION_SET_ASSIGNCELLSEXTRACTPROJECT:
                	String idProyectoExtract=(String)params.get(Const.KEY_ID_PROJECT);
                	HashMap<String, String> celdasUsuarios=(HashMap<String, String>)params.get(Const.KEY_CELLS_USER);
					geoConn.asignarCeldasProyectoExtraccion(idProyectoExtract, celdasUsuarios);
                    break;
                case Const.ACTION_GET_ASSIGNCELLSEXTRACTPROJECT:
                	idProyectoExtract=(String)params.get(Const.KEY_ID_PROJECT);
                	oos.writeObject(geoConn.obtenerCeldasProyectoExtraccion(idProyectoExtract));
                    break;
                case Const.ACTION_SEARCH_ATTRIBUTE: 
                    sLayer=(String)params.get(Const.KEY_LAYER);
                    String attributeName = (String) params.get(Const.KEY_ATTRIBUTE_NAME);
                    String attributeValue = (String) params.get(Const.KEY_ATTRIBUTE_VALUE);
                    geoConn.searchByAttribute(String.valueOf(iEntidad), iMunicipio, sLayer, sLocale, (FilterNode)params.get(Const.KEY_FILTER), oos, sesion, attributeName, attributeValue, bValidateData, srid);
                    break;
                case Const.ACTION_MUNICIPIO_GEOMETRY:
                    oos.writeObject(geoConn.municipioGeometry((Integer)params.get(Const.KEY_MUNICIPIO_GEOMETRY)));
                    break;
            }
        }catch(PermissionException pe){
            try{oos.writeObject(new ACException(pe));}catch(Exception ex){};
        }catch(SystemMapException pe){
            try{oos.writeObject(new ACException(pe));}catch(Exception ex){};
        }catch(PublishedMapException pe){
            try{oos.writeObject(new ACException(pe));}catch(Exception ex){};
        }/** MultiPartPost */catch (org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException pe){
            try{oos.writeObject(new ACException(pe));}catch(Exception ex){};
        }catch(Exception e){
            e.printStackTrace();
            log(e.getMessage());
        }finally{
            //oos.flush();
			//Modificado para reestablecer el BufferedOutputStream
			oos.reset();

            try{
            	oos.close();
            }catch(Exception e){
            	e.printStackTrace();
            };
        }

    }
    
    /**
     * A partir de un texto de definición de geometría con srid y geometría, lo descompone
     * @param stGeometry
     */
    private Geometry getGeometry(String stGeometry){
    	try{
	    	StringTokenizer stGeom = new StringTokenizer(stGeometry, ";");
	    	String geometria = "";
	    	String sridToken = "";
	    	while (stGeom.countTokens() == 2){
	    		StringTokenizer stSrid = new StringTokenizer(stGeom.nextToken(),"=");
	    		
	    		while (stSrid.countTokens() == 2){
	    			stSrid.nextToken();
	    			sridToken = stSrid.nextToken();
	    		}
	    		geometria = stGeom.nextToken();
	    	}
	        Feature f=(Feature)new WKTReader().read(new StringReader(geometria)).getFeatures().get(0);
	        Geometry g=f.getGeometry();
	        g.setSRID(Integer.parseInt(sridToken));
	        return g;
    	}catch(Exception e){
    		e.printStackTrace();
            log(e.getMessage());
    		return null;
    	}
    }
}
