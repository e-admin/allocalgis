package com.geopista.server.inventario;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exolab.castor.xml.Unmarshaller;
import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;

import admcarApp.PasarelaAdmcar;

import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.Documentable;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.ConfigParameters;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.InventarioEIELBean;
import com.geopista.protocol.inventario.Lote;
import com.geopista.protocol.inventario.MuebleBean;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;
import com.geopista.server.administradorCartografia.NewSRID;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.server.administradorCartografia.SystemMapException;
import com.localgis.server.SessionsContextShared;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 17-jul-2006
 * Time: 9:29:47
 * To change this template use File | Settings | File Templates.
 */
public class InventarioServlet  extends LoggerHttpServlet{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(InventarioServlet.class);
    GeopistaConnection geoConn;
//    private static SRID srid=null;
    private static NewSRID srid=null;

    public void init(){
        try{
            if (srid==null)
//                srid=new SRID(com.geopista.server.administradorCartografia.Const.SRID_PROPERTIES);
                srid = new NewSRID();
        } catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	super.doPost(request);
    	ObjectOutputStream oos=null;
        try{
            JAASUserPrincipal jassUserPrincipal = (JAASUserPrincipal) request.getUserPrincipal();
            
            PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");    
	    	
            Sesion userSesion = PasarelaAdmcar.listaSesiones.getSesion(jassUserPrincipal.getName());
            
            InventarioConnection geoConn=null;
            ACQuery query= new ACQuery();
            Vector docsFiles= new Vector();
            DocumentBean documentFile= null;

            if(!org.apache.commons.fileupload.DiskFileUpload.isMultipartContent(request)){
                /* -- PostMethod --  */
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
            String fechaVersion="";
            if (params.get(Const.KEY_FECHA_VERSION) != null && !params.get(Const.KEY_FECHA_VERSION).equals(""))
            	 fechaVersion= (String)params.get(Const.KEY_FECHA_VERSION);
 
            //ASO Elimina porque no es coherente
            //if (params.get(Const.KEY_ID_MUNICIPIO) != null && !params.get(Const.KEY_ID_MUNICIPIO).equals(""))
            //	userSesion.setIdMunicipio(((Integer)params.get(Const.KEY_ID_MUNICIPIO)).toString());
            String municipio=params.get(Const.KEY_ID_MUNICIPIO).toString();
            geoConn= new InventarioConnection(srid, municipio, fechaVersion);
            
            response.setHeader("Transfer-encoding","chunked");
            response.setHeader("Content-Encoding", "gzip");
            oos= new ObjectOutputStream(new GZIPOutputStream(response.getOutputStream()));
            Sesion sesion= sesion(request);
            int iUserID= Integer.parseInt(sesion.getIdUser());
            
            //Parametros adicionales que vienen en la peticion para personalizar las
            //queries
            ConfigParameters configParameters=new ConfigParameters();
            if ((params.get("LIMIT") != null) && (!params.get("LIMIT").equals("-1")))
            	configParameters.setLimit(Integer.parseInt(params.get("LIMIT").toString(),10));
            if ((params.get("OFFSET") != null) && (!params.get("OFFSET").equals("0")))
            	configParameters.setOffset(Integer.parseInt(params.get("OFFSET").toString(),10));
            
            
            Object obj= null;
            switch (query.getAction()){

                case Const.ACTION_GET_INMUEBLES:
                    geoConn.returnInmuebles(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), ((String)params.get(Const.KEY_CADENA_BUSQUEDA)), 
                    		(Object[])params.get(Const.KEY_FILTRO_BUSQUEDA), (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES),
                    		 userSesion,configParameters);
                    break;
                case Const.ACTION_GET_INMUEBLE:
                    geoConn.returnGenericBien(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), ((Long)params.get(Const.KEY_IDINMUEBLE)), ((Long)params.get(Const.KEY_IDREVISION)),((Long)params.get(Const.KEY_IDREVISIONEXPIRADA)),userSesion,configParameters);
                    
                    /*geoConn.returnInmueble(oos, ((String)params.get(Const.KEY_PATRON)), 
                    		((Long)params.get(Const.KEY_IDINMUEBLE)), userSesion);*/
                    break;
                case Const.ACTION_INSERT_INVENTARIO:
                    obj= params.get(Const.KEY_INVENTARIO);
                    /** Recogemos los documentos */
                    recogerDocumentos(obj, docsFiles);
                    if (obj instanceof MuebleBean && ((MuebleBean)obj).getLote()!=null){
                    	recogerDocumentos(((MuebleBean)obj).getLote(),docsFiles);
                    }
                    geoConn.returnInsertInventario(oos, (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES), obj, userSesion);
                    break;
                case Const.ACTION_UPDATE_LAYER_FEATURE_BIENES:
                	obj= params.get(Const.KEY_INVENTARIO);
                	geoConn.returnUpdateBienFeatureInventario(oos,obj,userSesion);
                	break;                    
                case Const.ACTION_UPDATE_INVENTARIO:
                    obj= params.get(Const.KEY_INVENTARIO);
                    /** Recogemos los documentos */
                    if (obj instanceof BienBean){
                    	recogerDocumentos(obj, docsFiles);
                    	geoConn.returnUpdateInventario(oos, obj, userSesion);
                    }else if (obj instanceof BienRevertible){
                    	recogerDocumentos(obj, docsFiles);
                    	geoConn.returnUpdateBienRevertible(oos, (BienRevertible)obj, userSesion);
                    }else if (obj instanceof Lote){
                    	recogerDocumentos(obj, docsFiles);
                    	geoConn.returnUpdateLote(oos, (Lote)obj, userSesion);
                    }
                    break;
                case Const.ACTION_GET_CUENTAS_CONTABLES:
                    geoConn.returnCuentasContables(oos);
                    break;
                case Const.ACTION_GET_CUENTAS_AMORTIZACION:
                    geoConn.returnCuentasAmortizacion(oos);
                    break;
                case Const.ACTION_GET_COMPANNIAS_SEGUROS:
                    geoConn.returnCompanniasSeguros(oos);
                    break;
                case Const.ACTION_BORRAR_INVENTARIO:
                    obj= params.get(Const.KEY_INVENTARIO);
                    geoConn.returnBorrarInventario(oos, obj, userSesion);
                    break;
                case Const.ACTION_ELIMINAR_INVENTARIO:
                    obj= params.get(Const.KEY_INVENTARIO);
                    if (obj instanceof BienBean)
                    	geoConn.returnEliminarInventario(oos, obj, (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES), userSesion);
                    if (obj instanceof BienRevertible)
                    	geoConn.returnEliminarBienRevertible(oos, (BienRevertible)obj,  userSesion);
                    if (obj instanceof Lote)
                    	geoConn.returnEliminarLote(oos, (Lote)obj,  userSesion);
                    break;
                case Const.ACTION_ELIMINAR_NORECOVER_INVENTARIO:
                    obj= params.get(Const.KEY_INVENTARIO);
                    geoConn.returnEliminarTodoInventario(null,obj,oos);
                    break;
                case Const.ACTION_GET_MUEBLES:
                    geoConn.returnMuebles(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), ((String)params.get(Const.KEY_CADENA_BUSQUEDA)), (Object[])params.get(Const.KEY_FILTRO_BUSQUEDA), (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS),
                    		(Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES), userSesion,configParameters);
                    break;
                case Const.ACTION_GET_MUEBLE:
                    geoConn.returnGenericBien(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), ((Long)params.get(Const.KEY_IDINMUEBLE)), ((Long)params.get(Const.KEY_IDREVISION)),((Long)params.get(Const.KEY_IDREVISIONEXPIRADA)),userSesion,configParameters);
                    break;
                case Const.ACTION_GET_DERECHOS_REALES:
                     geoConn.returnDerechosReales(oos, ((String)params.get(Const.KEY_SUPERPATRON)),((String)params.get(Const.KEY_PATRON)), ((String)params.get(Const.KEY_CADENA_BUSQUEDA)), (Object[])params.get(Const.KEY_FILTRO_BUSQUEDA), (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), 
                    		 (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES), userSesion,configParameters);
                    break;
                case Const.ACTION_GET_DERECHO_REAL:
                    geoConn.returnGenericBien(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), ((Long)params.get(Const.KEY_IDINMUEBLE)), ((Long)params.get(Const.KEY_IDREVISION)),((Long)params.get(Const.KEY_IDREVISIONEXPIRADA)),userSesion,configParameters);
                    break;
                case Const.ACTION_BLOQUEAR_INVENTARIO:
                    obj= params.get(Const.KEY_INVENTARIO);
                    geoConn.returnBloquearInventario(oos, obj, ((Boolean)params.get(Const.KEY_VALOR_BLOQUEO_INVENTARIO)).booleanValue(), userSesion);
                    break;
                case Const.ACTION_GET_BLOQUEADO_INVENTARIO:
                    geoConn.returnBloqueado(oos, params.get(Const.KEY_INVENTARIO), userSesion);
                    break;
                case Const.ACTION_GET_VALORES_MOBILIARIOS:
                    geoConn.returnValoresMobiliarios(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), ((String)params.get(Const.KEY_CADENA_BUSQUEDA)), (Object[])params.get(Const.KEY_FILTRO_BUSQUEDA), (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS),
                    		(Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES), userSesion,configParameters);
                    break;
                case Const.ACTION_GET_VALOR_MOBILIARIO:
                    geoConn.returnGenericBien(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), ((Long)params.get(Const.KEY_IDINMUEBLE)), ((Long)params.get(Const.KEY_IDREVISION)),((Long)params.get(Const.KEY_IDREVISIONEXPIRADA)),userSesion,configParameters);
                    break;
                case Const.ACTION_GET_CREDITOS_DERECHOS:
                    geoConn.returnCreditosDerechos(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), ((String)params.get(Const.KEY_CADENA_BUSQUEDA)), (Object[])params.get(Const.KEY_FILTRO_BUSQUEDA), (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS),
                    		(Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES), userSesion,configParameters);
                    break;
                case Const.ACTION_GET_CREDITO_DERECHO:
                    geoConn.returnGenericBien(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), ((Long)params.get(Const.KEY_IDINMUEBLE)), ((Long)params.get(Const.KEY_IDREVISION)),((Long)params.get(Const.KEY_IDREVISIONEXPIRADA)),userSesion,configParameters);
                    break;
                case Const.ACTION_GET_SEMOVIENTES:
                    geoConn.returnSemovientes(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), ((String)params.get(Const.KEY_CADENA_BUSQUEDA)), (Object[])params.get(Const.KEY_FILTRO_BUSQUEDA), (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS),
                    		(Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES), userSesion,configParameters);
                    break;
                case Const.ACTION_GET_SEMOVIENTE:
                    geoConn.returnGenericBien(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), ((Long)params.get(Const.KEY_IDINMUEBLE)), ((Long)params.get(Const.KEY_IDREVISION)),((Long)params.get(Const.KEY_IDREVISIONEXPIRADA)),userSesion,configParameters);
                    break;
                case Const.ACTION_GET_VIAS:
                    geoConn.returnVias(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), ((String)params.get(Const.KEY_CADENA_BUSQUEDA)), (Object[])params.get(Const.KEY_FILTRO_BUSQUEDA), (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS),
                    		(Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES), userSesion,configParameters);
                    break;
                case Const.ACTION_GET_VIA:
                    geoConn.returnGenericBien(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), ((Long)params.get(Const.KEY_IDINMUEBLE)), ((Long)params.get(Const.KEY_IDREVISION)),((Long)params.get(Const.KEY_IDREVISIONEXPIRADA)),userSesion,configParameters);
                    break;                                   
                case Const.ACTION_GET_VEHICULOS:
                    geoConn.returnVehiculos(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), ((String)params.get(Const.KEY_CADENA_BUSQUEDA)), (Object[])params.get(Const.KEY_FILTRO_BUSQUEDA), (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS),
                    		(Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES), userSesion,configParameters);
                    break;
                case Const.ACTION_GET_VEHICULO:
                    geoConn.returnGenericBien(oos, ((String)params.get(Const.KEY_SUPERPATRON)), ((String)params.get(Const.KEY_PATRON)), ((Long)params.get(Const.KEY_IDINMUEBLE)), ((Long)params.get(Const.KEY_IDREVISION)),((Long)params.get(Const.KEY_IDREVISIONEXPIRADA)),userSesion,configParameters);
                    break;
                case Const.ACTION_GET_PLANTILLAS:
                    geoConn.returnPlantillas(oos, ((String)params.get(Const.KEY_PATH_PLANTILLAS)));
                    break;
                case Const.ACTION_GET_REFERENCIAS_CATASTRALES:
                	String patron= (String)query.getParams().get(Const.KEY_PATRON);
                    geoConn.returnReferanciasCatastrales(oos, patron);
                    break;
                case Const.ACTION_RECUPERAR_INVENTARIO:
                    obj= params.get(Const.KEY_INVENTARIO);
                    geoConn.recuperarInventario(oos, obj, userSesion);
                    break;                    
                case Const.ACTION_GET_HORA:
                    obj= params.get(Const.ACTION_GET_HORA);
                    geoConn.getHora(oos);
                    break;
                case Const.ACTION_GET_BIENES_REVERTIBLES:
                    geoConn.returnBienesRevertibles(oos, ((String)params.get(Const.KEY_PATRON)), ((String)params.get(Const.KEY_CADENA_BUSQUEDA)), (Object[])params.get(Const.KEY_FILTRO_BUSQUEDA), (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES), userSesion,configParameters);
                    break;
                case Const.ACTION_GET_BIENES:
                    geoConn.returnBienes(oos,  ((String)params.get(Const.KEY_SUPERPATRON)),((String)params.get(Const.KEY_CADENA_BUSQUEDA)), (Object[])params.get(Const.KEY_FILTRO_BUSQUEDA),(Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES));
                    break;
                case Const.ACTION_GET_LOTES:
                    geoConn.returnLotes(oos, ((String)params.get(Const.KEY_PATRON)), ((String)params.get(Const.KEY_CADENA_BUSQUEDA)), (Object[])params.get(Const.KEY_FILTRO_BUSQUEDA), (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES), userSesion,configParameters);
                	break;
                case Const.ACTION_ELIMINAR_TODO_INVENTARIO:
                    geoConn.returnEliminarTodoInventario(null,null,oos);
                	break;	
                case Const.ACTION_GET_DATE:
//                    obj= params.get(Const.ACTION_GET_DATE);
                    geoConn.getDate(oos);
                    break; 
                case Const.ACTION_GET_BIENES_PREALTA:
                    geoConn.returnBienesPreAlata(oos);
                    break;               
                case Const.ACTION_INSERT_BIEN_PREALTA:
                    obj= params.get(Const.KEY_INVENTARIO);
                    /** Recogemos los documentos */
                    recogerDocumentos(obj, docsFiles);
                    if (obj instanceof MuebleBean && ((MuebleBean)obj).getLote()!=null){
                    	recogerDocumentos(((MuebleBean)obj).getLote(),docsFiles);
                    }
                    Object bienPA = params.get(Const.KEY_PREALTA);
                    geoConn.returnInsertBienPreALta(oos, (Object[])params.get(Const.KEY_ARRAYLIST_IDLAYERS), (Object[])params.get(Const.KEY_ARRAYLIST_IDFEATURES), obj,bienPA, userSesion);
                    break; 
                case Const.ACTION_UPDATE_DATOS_VALORACION:
                  geoConn.updateDatosValoracion(oos,((Double)params.get(Const.KEY_VALOR_URBANO)),((Double)params.get(Const.KEY_VALOR_RUSTICO)),((Integer)params.get(Const.KEY_ID_ENTIDAD)));
                  break;
                case Const.ACTION_GET_VALOR_METRO:
                    geoConn.returnValorMetro(oos,((String)params.get(Const.KEY_PATRON)),((Integer)params.get(Const.KEY_ID_ENTIDAD)));
                    break;
//                case Const.ACTION_GET_FEATURES_COMPARE_GEOMETRY:
//                	
//                	//Recuperamos los parametros
//                	int idLayer= (Integer) query.getParams().get(Const.KEY_ID_LAYER);
//                	String superf= (String)query.getParams().get(Const.KEY_SUPERF);
//                	double tol = (Double)query.getParams().get(Const.KEY_TOLERANCIA);
//                	//Obtenemos las features
//                	geoConn.getfeatComparar(oos, idLayer, superf, tol);
//                	break;
//                case Const.ACTION_GET_REVISION_ACTUAL:
//                	String tabla= (String ) query.getParams().get(Const.KEY_TABLA);
//                	geoConn.getRevision(oos, tabla);
//                	break;
                case Const.ACTION_GET_EIEL_SIN_ASOCIAR:
                    geoConn.returnDatosEIELSinAsociar(oos);
                    break;
                case Const.ACTION_GET_NUMEROS_INVENTARIO:
                    geoConn.returnNumerosInventario(oos, ((Integer)params.get(Const.KEY_EPIG_INVENTARIO)));
                    break;
                case Const.ACTION_INSERT_INTEG_EIEL_INVENTARIO:
                	InventarioEIELBean inventarioEIEL = (InventarioEIELBean) params.get(Const.KEY_INVENTARIO_EIEL);
                    geoConn.returnInsertIntegEIELInventario(oos, inventarioEIEL);
                    break;
                case Const.ACTION_GET_EIEL_ASOCIADOS:
                	long idBien = (Long) params.get(Const.KEY_IDBIEN);
                    geoConn.returnDatosEIELBien(oos, idBien);
                    break;
                case Const.ACTION_GET_COMPROBAR_INTEG_EIEL:
                	BienBean bien = (BienBean) params.get(Const.KEY_BIEN);
                    geoConn.returnComprobarIntegEIELInventario(oos, bien);
                    break;
                case Const.ACTION_GET_ELEMENTOS_COMPROBAR_INTEG_EIEL:
                	idBien = (Long) params.get(Const.KEY_IDBIEN);
                    geoConn.returnElementosComprobarIntegEIELInventario(oos, idBien);
                    break;  
                case Const.ACTION_INSERT_BIENES_AMORTIZABLES:
                	Integer anio = (Integer) params.get(Const.KEY_ANIO_AMORTIZACION);
                    geoConn.insertBienesAmortizados(oos,anio, userSesion);
                	break;
                case Const.ACTION_GET_BIENES_AMORTIZADOS:
                	idBien = (Long) params.get(Const.KEY_IDBIEN);
                    geoConn.returnHistoricoBienAmortizado(oos,idBien,((String)params.get(Const.KEY_PATRON)));
                	break;
                 
             }
        }catch(PermissionException pe){
            logger.error("Error en los permisos",pe);           
            try{oos.writeObject(new ACException(pe));}catch(Exception ex){};
        }catch(SystemMapException pe){
            logger.error("Error systemMap",pe);           
            try{oos.writeObject(new ACException(pe));}catch(Exception ex){};
        }/** MultiPartPost */catch (org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException pe){
            try{oos.writeObject(new ACException(pe));}catch(Exception ex){};
        }catch(Exception e){
         	logger.error("Se ha producido un ",e);
            try{oos.writeObject(new ACException(e));}catch(Exception ex){};
        }catch (java.lang.Error e){
        	logger.error("Se ha producido un ",e);
            try{oos.writeObject(new ACException(e));}catch(Exception ex){};
        }catch(Throwable e){
          	logger.error("Se ha producido un ",e);
            try{oos.writeObject(new ACException(e));}catch(Exception ex){};
        } finally{
            try{oos.close();}catch(Exception e){};
        }
        

    }
    private Sesion sesion(HttpServletRequest request){
        Sesion sRet=null;
        JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
        if (userPrincipal!=null){
            String  sIdSesion= userPrincipal.getName();
            
            PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");    
	    	
            sRet=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
        }
        return sRet;
    }

    private Object recogerDocumentos(Object obj, Vector docsFiles) throws Exception {
        try{
            /** Recogemos los documentos */
            if (obj != null){
                com.geopista.server.document.DocumentConnection docConn= new com.geopista.server.document.DocumentConnection(srid);
                if (((Documentable)obj).getDocumentos() != null){
                    Object[] documentos= ((Documentable)obj).getDocumentos().toArray();
                    for (int i=0; i<documentos.length; i++){
                        DocumentBean document= (DocumentBean)documentos[i];
                        if (docsFiles != null && !docsFiles.isEmpty()){
                            try{
                            	for (int j=0; j<docsFiles.size();j++){
                            		if (docsFiles.get(j) != null){
                            		   DocumentBean documentFile= (DocumentBean)docsFiles.get(j);
                                       int index= documentFile.getFileName().indexOf("FILE", 0);
                                       if (index <0 ) continue;
                                       String nombreFichero=documentFile.getFileName().substring(index+4, documentFile.getFileName().length());
                                       if (document.getFileName().indexOf(nombreFichero)<0) continue;
                                       document.setFileName(nombreFichero);
                                       document.setSize(documentFile.getSize());
                                       document.setContent(documentFile.getContent());
                                       if (docConn.isImagen(document)) document.setIsImagen();
                                       if (document.isImagen())
                                       document.setThumbnail(com.geopista.protocol.document.Thumbnail.createThumbnail(documentFile.getContent()));
                                       break;
                            		}
                            	}
                            }catch(java.lang.IndexOutOfBoundsException e){/** No existe el file para el documento con indice i */}
                        }else{
                        	//TODO aqui se puede comprobar si se ha cargado o no
                        	if (document.getServerPath()!=null && document.getServerPath().length()>0){
                           		 document=docConn.cargarDocumentServer(document); 
                           	}
                        }
                    }
                }
            }
        }catch (Exception e){
            throw e;
        }

        return obj;

    }

}
