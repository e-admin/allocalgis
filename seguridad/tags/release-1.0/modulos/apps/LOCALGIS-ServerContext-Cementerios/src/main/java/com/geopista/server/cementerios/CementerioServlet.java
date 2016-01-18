package com.geopista.server.cementerios;

import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.administradorCartografia.*;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.cementerios.Const;
import com.geopista.protocol.document.DocumentBean;
import com.localgis.server.SessionsContextShared;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import java.io.*;
import java.util.*;
import java.util.zip.GZIPOutputStream;
import java.net.URLDecoder;
import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.exolab.castor.xml.Unmarshaller;
import admcarApp.PasarelaAdmcar;


public class CementerioServlet  extends LoggerHttpServlet{
    private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(CementerioServlet.class);
    GeopistaConnection geoConn;
    private static NewSRID srid=null;

    public void init(){
        try{
            if (srid==null)
                srid = new NewSRID();
        } catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.getWriter().append("Authentication Successful!");
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	super.doPost(request);
        ObjectOutputStream oos=null;
        try{
        	JAASUserPrincipal jassUserPrincipal = (JAASUserPrincipal) request.getUserPrincipal();
            PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");    
        	Sesion userSesion = PasarelaAdmcar.listaSesiones.getSesion(jassUserPrincipal.getName());

            CementerioConnection geoConn=null;
            ACQuery query= new ACQuery();
            Vector docsFiles= new Vector();
            DocumentBean documentFile= null;

            if(!org.apache.commons.fileupload.DiskFileUpload.isMultipartContent(request)){
                /* -- PostMethod --  */
                System.out.println(request.getParameter("mensajeXML"));
                ACMessage abQuery= (ACMessage)Unmarshaller.unmarshal(ACMessage.class,new StringReader(request.getParameter("mensajeXML")));
                ObjectInputStream ois= new ObjectInputStream(new ByteArrayInputStream(abQuery.getQuery()));
                query= (ACQuery)ois.readObject();
            }else{
                /** -- MultiPartPost -- */
               // Create a new file upload handler
                org.apache.commons.fileupload.DiskFileUpload upload= new org.apache.commons.fileupload.DiskFileUpload();
                List items= upload.parseRequest(request);

                // Process the uploaded items
                Iterator iter= items.iterator();

                while (iter.hasNext()) {
                    org.apache.commons.fileupload.FileItem item= (org.apache.commons.fileupload.FileItem) iter.next();

                    if (item.isFormField()) {
                       if (item.getFieldName().equalsIgnoreCase("mensajeXML")){
                           ACMessage abQuery=(ACMessage)Unmarshaller.unmarshal(ACMessage.class,new StringReader(item.getString("ISO-8859-1")));
                           ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(abQuery.getQuery()));
                           query=(ACQuery)ois.readObject();

                           logger.debug("MENSAJE XML:"+item.getString("ISO-8859-1"));
                       }
                    }else {
                        /** Leemos el fichero que se envia. Generamos un DocumentBean auxiliar para guardar la informacion */
                        documentFile= new DocumentBean();
                        /** Debido a que el nombre del fichero puede contener acentos. */
                        documentFile.setFileName(URLDecoder.decode(item.getFieldName(),"ISO-8859-1"));
                        documentFile.setContent(item.get());
                        documentFile.setSize(item.getSize());

                        /** Inventario. Recogemos los files. */
                        int index= item.getFieldName().indexOf("FILE", 0);
                        if (index != -1){
                            index= Integer.parseInt(item.getFieldName().substring(0, index));
                            if (docsFiles.size() < index){
                                for (int i=docsFiles.size(); i<index; i++){
                                    docsFiles.add(i, null);
                                }
                            }
                            docsFiles.add(index, documentFile);
                        }
                   }

                }
            }

            //De aquí hay que obtener los parametros necesarios
            Hashtable params= query.getParams();
            if (params.get(Const.KEY_FECHA_VERSION) != null && !params.get(Const.KEY_FECHA_VERSION).equals(""))
            	Const.fechaVersion = (String)params.get(Const.KEY_FECHA_VERSION);

            //ASO Elimina porque no es coherente
            //if (params.get(Const.KEY_ID_MUNICIPIO) != null && !params.get(Const.KEY_ID_MUNICIPIO).equals(""))
            //	userSesion.setIdMunicipio(((Integer)params.get(Const.KEY_ID_MUNICIPIO)).toString());
            String municipio=params.get(Const.KEY_ID_MUNICIPIO).toString();
            geoConn= new CementerioConnection(srid, municipio);

            response.setHeader("Transfer-encoding","chunked");
            response.setHeader("Content-Encoding", "gzip");
            oos= new ObjectOutputStream(new GZIPOutputStream(response.getOutputStream()));
            Sesion sesion= sesion(request);
            //int iUserID= Integer.parseInt(sesion.getIdUser());
            Object obj= null;

            switch (query.getAction()){
            	case Const.ACTION_GET_ALL:
            		geoConn.returnAllElemsByType(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), 
            				((String)params.get(Const.KEY_CADENA_BUSQUEDA)), ((Integer)params.get(Const.KEY_IDCEMENTERIO)), userSesion);
            		break;
            	case Const.ACTION_GET_ELEM:
            		geoConn.returnElemsByTypeAndFeature(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), 
            				((String)params.get(Const.KEY_CADENA_BUSQUEDA)), (Object[])params.get(Const.KEY_FILTRO_BUSQUEDA), 
            				(Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES),
            				((Integer)params.get(Const.KEY_IDCEMENTERIO)),userSesion);
            		break;           		

            	case Const.ACTION_FILTER_ELEM:
        		geoConn.returnFilterElem(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), 
        				((String)params.get(Const.KEY_CADENA_BUSQUEDA)), (Object[])params.get(Const.KEY_FILTRO_BUSQUEDA), 
        				(Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES),
        				((Integer)params.get(Const.KEY_IDCEMENTERIO)),userSesion);            		
        		break;
            	case Const.ACTION_FIND_ELEM:
            		geoConn.returnFindElem(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), 
            				((String)params.get(Const.KEY_CADENA_BUSQUEDA)), (Object[])params.get(Const.KEY_FILTRO_BUSQUEDA), 
            				(Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES),
            				((Integer)params.get(Const.KEY_IDCEMENTERIO)),userSesion);            		
            		break;
            	case Const.ACTION_INSERT:
            		  obj= params.get(Const.KEY_IDELEMENTOCEMENTERIO);
            		geoConn.returnInsertElemCementerio (oos, (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES), 
            				obj,((Integer)params.get(Const.KEY_IDCEMENTERIO)), userSesion);
        		break;
            	case Const.ACTION_GET_UNIDAD_ENTE:
            		geoConn.returnUnidadEnterramiento (oos, (String)params.get(Const.KEY_PATRON), ((Long)params.get(Const.KEY_IDELEMENTOCEMENTERIO)), userSesion);
            		break;
                case Const.ACTION_UPDATE:
                	  obj= params.get(Const.KEY_IDELEMENTOCEMENTERIO);
                	  geoConn.returnUpdateElemCementerio(oos, obj, userSesion);
                    break;	
                case Const.ACTION_DELETE:
                    obj= params.get(Const.KEY_IDELEMENTOCEMENTERIO);
                    geoConn.returnEliminarElemCementerio(oos, (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES),
                    		obj,((Integer)params.get(Const.KEY_IDCEMENTERIO)), userSesion);
                    break;
                case Const.ACTION_GET_HORA:
                    obj= params.get(Const.ACTION_GET_HORA);
                    geoConn.getHora(oos);
                    break;
            	case Const.ACTION_GET_CONCESIONES:
            		geoConn.returnConcesion(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), ((String)params.get(Const.KEY_CADENA_BUSQUEDA)), 
            				(Object[])params.get(Const.KEY_FILTRO_BUSQUEDA), (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES), 
            				((Integer)params.get(Const.KEY_IDCEMENTERIO)),userSesion);
        		break;
            	case Const.ACTION_GET_INVENCIONES_PUN:
            		geoConn.returnConcesion(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), ((String)params.get(Const.KEY_CADENA_BUSQUEDA)),
            				(Object[])params.get(Const.KEY_FILTRO_BUSQUEDA), (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES), 
            				((Integer)params.get(Const.KEY_IDCEMENTERIO)),userSesion);
        		break;
            	case Const.ACTION_GET_PLAZA:       
            		geoConn.returnPlaza(oos,(Long) params.get(Const.KEY_IDELEMENTOCEMENTERIO), userSesion);
        		break;
            	case Const.ACTION_GET_CAMPOS_ELEM:
            		geoConn.returnAllCamposElems(oos,((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), userSesion);
        		break;
            	case Const.ACTION_GET_HISTORICO:
            		obj= params.get(Const.KEY_ID_DIFUNTO);
            		geoConn.returnHistorico(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), 
            				((String)params.get(Const.KEY_CADENA_BUSQUEDA)), (Object[])params.get(Const.KEY_FILTRO_BUSQUEDA), 
            				(Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES),
            				((Integer)params.get(Const.KEY_IDCEMENTERIO)), ((Object)params.get(Const.KEY_ID_DIFUNTO)) ,userSesion);
        		break;
            	case Const.ACTION_GET_CEMENTERIO:            	
            		geoConn.returnCementerio(oos, (Integer) params.get(Const.KEY_IDELEMENTOCEMENTERIO) , userSesion);
        		break;
        		
        		
        		
            }
        }catch(PermissionException pe){
            try{oos.writeObject(new ACException(pe));}catch(Exception ex){};
        }catch(SystemMapException pe){
            try{oos.writeObject(new ACException(pe));}catch(Exception ex){};
        }/** MultiPartPost */catch (org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException pe){
            try{oos.writeObject(new ACException(pe));}catch(Exception ex){};
        }catch(Exception e){
            e.printStackTrace();
            log(e.getMessage());
        }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            try{oos.writeObject(new ACException(e));}catch(Exception ex){};
        }finally{
            try{oos.close();}catch(Exception e){};
        }

    }
    private Sesion sesion(HttpServletRequest request){
        Sesion sRet=null;
        JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
        if (userPrincipal!=null){
            String  sIdSesion= userPrincipal.getName();
            sRet=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
        }
        return sRet;
    }



}
