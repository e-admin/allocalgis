package com.geopista.server.eiel.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exolab.castor.xml.Unmarshaller;
import org.mortbay.jaas.JAASUserPrincipal;

import admcarApp.PasarelaAdmcar;

import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.server.administradorCartografia.SRID;
import com.geopista.server.administradorCartografia.SystemMapException;
import com.geopista.server.database.COperacionesEIEL;
import com.geopista.util.ServletContextListener;


/**
 * Servlet que recibe las peticiones http de los clientes para hacer peticiones a la BBDD. Se envian peticiones
 * multipart, por si el tamaño de las peticiones fuera grande. Se parsea a xml y en esta clase se desparsean y se
 * recoje el elemento ACQuery. Con la accion se discrimina la peticion y se obtienen los parametros en caso de que
 * los haya.
 * */

public class EIELServlet extends HttpServlet
{
    private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(EIELServlet.class);

    public int numeroServlet = 0;
    
    private static SRID srid = null;
    
    public void init(ServletConfig config) throws ServletException{
    	super.init(config);


    	ServletContextListener.numeroCat ++;

    	numeroServlet = ServletContextListener.numeroCat;

    	System.out.println("Instantiated Cat a new "+config.getServletName()+" object. Total:"+ServletContextListener.numeroCat);
    	
    	if (srid==null)
			try {
				srid=new SRID(com.geopista.server.administradorCartografia.Const.SRID_PROPERTIES);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request, response);
    }

    /**
     * Metodo que mediante el request lee el ACQuery y analiza que accion se desea realiza para llamar a la clase
     * COperacionesDGC que es la que conecta con BBDD. El resultado se escribe en el objeto oos y se vuelve a enviar.
     *
     * @param request Los datos e la peticion.
     * @param response Los datos de la respuesta
     * @throws ServletException
     * @throws IOException
     * */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    	//logger.info("DO POST EIELSERVLET");
        ObjectOutputStream oos=null;
        try{
            JAASUserPrincipal jassUserPrincipal = (JAASUserPrincipal) request.getUserPrincipal();
            Sesion userSesion = PasarelaAdmcar.listaSesiones.getSesion(jassUserPrincipal.getName());

            COperacionesEIEL geoConn=null;
            ACQuery query= new ACQuery();
            
            
            if(!org.apache.commons.fileupload.DiskFileUpload.isMultipartContent(request))
            {
                /* -- PostMethod --  */

                ACMessage abQuery= (ACMessage) Unmarshaller.unmarshal(ACMessage.class,new StringReader(request.getParameter("mensajeXML")));
                ObjectInputStream ois= new ObjectInputStream(new ByteArrayInputStream(abQuery.getQuery()));
                query= (ACQuery)ois.readObject();
            }
            else
            {
                /** -- MultiPartPost -- */
               // Create a new file upload handler
                org.apache.commons.fileupload.DiskFileUpload upload= new org.apache.commons.fileupload.DiskFileUpload();
                List items= upload.parseRequest(request);

                // Process the uploaded items
                Iterator iter= items.iterator();

                while (iter.hasNext())
                {
                    org.apache.commons.fileupload.FileItem item= (org.apache.commons.fileupload.FileItem) iter.next();

                    if (item.isFormField())
                    {
                       if (item.getFieldName().equalsIgnoreCase("mensajeXML"))
                       {
                           ACMessage abQuery=(ACMessage)Unmarshaller.unmarshal(ACMessage.class,new StringReader(item.getString("ISO-8859-1")));
                           ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(abQuery.getQuery()));
                           query=(ACQuery)ois.readObject();

                           logger.debug("MENSAJE XML:"+item.getString("ISO-8859-1"));
                       }
                    }
                }
            }

            geoConn= new COperacionesEIEL(srid);
            response.setHeader("Transfer-encoding","chunked");
            response.setHeader("Content-Encoding", "gzip");
            oos= new ObjectOutputStream(new GZIPOutputStream(response.getOutputStream()));

            //logger.info("ACTION:"+query.getAction());
            switch (query.getAction())
            {                              
                                
                case ConstantesLocalGISEIEL.ACTION_GET_ID_USUARIO:
                {
                    String idSesion = userSesion.getIdUser();
                    oos.writeObject(idSesion);                        
                    break;
                }
                
                case ConstantesLocalGISEIEL.ACTION_OBTENER_IDMAPA:
                {
                	String nombreMapa = (String)query.getParams().get(ConstantesLocalGISEIEL.NOMBREMAPA);
                	geoConn.getIdMapa(oos, nombreMapa, userSesion.getIdMunicipio());
                    break;
                }
                
                case ConstantesLocalGISEIEL.ACTION_OBTENER_MAPAS:
                {
                	String patronNombreMapa = (String)query.getParams().get(ConstantesLocalGISEIEL.NOMBREMAPA);
                	 String locale= ((String)query.getParams().get(ConstantesLocalGISEIEL.Locale));
                	geoConn.getMapas(oos, patronNombreMapa, locale,userSesion.getIdMunicipio());
                    break;
                }
                
                
                case ConstantesLocalGISEIEL.ACTION_OBTENER_NOMBREMAPA:
                {
                	Integer idMapa = (Integer)query.getParams().get(ConstantesLocalGISEIEL.IDMAPA);
                	//geoConn.getNombreMapa(oos, idMapa, userSesion.getIdMunicipio());
                	geoConn.getNombreMapa(oos, idMapa, userSesion.getIdEntidad());
                    break;
                }
                
                case ConstantesLocalGISEIEL.ACTION_OBTENER_DOMINIO:
                {
                	String domainName = ((String) query.getParams().get(ConstantesLocalGISEIEL.ID_DOMAIN));
                	Integer idMunicipio = (Integer)query.getParams().get(ConstantesLocalGISEIEL.KEY_ID_MUNICIPIO);
                    String locale= ((String)query.getParams().get(ConstantesLocalGISEIEL.Locale));
                    
                    //SATEC Le hemos pasado el locale para que solo saques del idioma por defecto para reducir
                    //los tiempos de carga
                	geoConn.getLstDomains(oos, domainName, Integer.toString(idMunicipio),locale);
                    break;
                }
                
                case ConstantesLocalGISEIEL.ACTION_OBTENER_ELEMENTO:
                {
                	String filtro = ((String) query.getParams().get(ConstantesLocalGISEIEL.FILTRO));
                	String tipoElemento = ((String) query.getParams().get(ConstantesLocalGISEIEL.TIPO_ELEMENTO));
                	boolean noGeoReferenciados = ((Boolean) query.getParams().get(ConstantesLocalGISEIEL.OPERACION_FILTRAR_REFERENCIADOS));
                	Integer idMunicipioSeleccionado = (Integer)query.getParams().get(ConstantesLocalGISEIEL.MUNICIPIO_SELECCIONADO);
                	if (idMunicipioSeleccionado==null)
                		idMunicipioSeleccionado=0;
                	
                    String nombreTabla= ((String)query.getParams().get(ConstantesLocalGISEIEL.NOMBRE_TABLA));
                    ArrayList camposEspecificos= ((ArrayList)query.getParams().get(ConstantesLocalGISEIEL.CAMPOS_ESPECIFICOS));
                    

                	geoConn.getLstElementos(oos, tipoElemento, filtro, userSesion.getIdMunicipio(),noGeoReferenciados, false, null,idMunicipioSeleccionado,nombreTabla,camposEspecificos);
                    break;
                }
                case ConstantesLocalGISEIEL.ACTION_OBTENER_ELEMENTOS_VERSIONADOS:
                {
                	String filtro = ((String) query.getParams().get(ConstantesLocalGISEIEL.FILTRO));
                	String tipoElemento = ((String) query.getParams().get(ConstantesLocalGISEIEL.TIPO_ELEMENTO));
                	boolean noGeoReferenciados = ((Boolean) query.getParams().get(ConstantesLocalGISEIEL.OPERACION_FILTRAR_REFERENCIADOS));
                	boolean version = ((Boolean) query.getParams().get(ConstantesLocalGISEIEL.OPERACION_FILTRAR_VERSION));                	
                	Object elemento = ((Object) query.getParams().get(ConstantesLocalGISEIEL.OBJECT));                	
                	geoConn.getLstElementos(oos, tipoElemento, filtro, userSesion.getIdMunicipio(),noGeoReferenciados, version, elemento);
                    break;
                }                  
                
                case ConstantesLocalGISEIEL.ACTION_GET_FEATURES:
                {
                	Object object = ((Object) query.getParams().get(ConstantesLocalGISEIEL.OBJECT));
                	String tipoElemento = (String)query.getParams().get(ConstantesLocalGISEIEL.TIPO_ELEMENTO);
                	  String nombreTabla= ((String)query.getParams().get(ConstantesLocalGISEIEL.NOMBRE_TABLA));
                	geoConn.getFeatures(oos, object, tipoElemento, userSesion.getIdMunicipio(),nombreTabla);
                    break;
                }                
                case ConstantesLocalGISEIEL.ACTION_BLOQUEAR_ELEMENTO:
                {
                	Object object = query.getParams().get(ConstantesLocalGISEIEL.OBJECT);
                	String tipoElemento = (String) query.getParams().get(ConstantesLocalGISEIEL.TIPO_ELEMENTO);
                	Boolean bloqueado = (Boolean) query.getParams().get(ConstantesLocalGISEIEL.KEY_VALOR_BLOQUEO_ELEMENTO);
                	geoConn.returnBloquearElemento(oos, object, bloqueado.booleanValue(), tipoElemento, userSesion);
                    break;
                }
                case ConstantesLocalGISEIEL.ACTION_GET_BLOQUEADO_ELEMENTO:
                {
                	Object object = query.getParams().get(ConstantesLocalGISEIEL.OBJECT);
                	String tipoElemento = (String) query.getParams().get(ConstantesLocalGISEIEL.TIPO_ELEMENTO);
                	geoConn.returnBloqueado(oos, object, tipoElemento);
                    break;
                }
                case ConstantesLocalGISEIEL.ACTION_ELIMINAR_ELEMENTO:
                {
                	Object object = query.getParams().get(ConstantesLocalGISEIEL.OBJECT);
                	Collection lstFeatures = (Collection) query.getParams().get(ConstantesLocalGISEIEL.FEATURES);
                	String idLayer = (String) query.getParams().get(ConstantesLocalGISEIEL.ID_LAYER);
                	String tipoElemento = (String) query.getParams().get(ConstantesLocalGISEIEL.TIPO_ELEMENTO);
                	geoConn.eliminarElemento(oos, object, lstFeatures, idLayer, tipoElemento, userSesion);
                    break;
                }                
                case ConstantesLocalGISEIEL.ACTION_INSERTAR_ELEMENTO:
                {
                	Object object = query.getParams().get(ConstantesLocalGISEIEL.OBJECT);
                	String idLayer = (String) query.getParams().get(ConstantesLocalGISEIEL.ID_LAYER);
                	String tipoElemento = (String) query.getParams().get(ConstantesLocalGISEIEL.TIPO_ELEMENTO);
                	geoConn.insertarElemento(oos, object, idLayer, tipoElemento, userSesion);
                    break;
                }
                case ConstantesLocalGISEIEL.ACTION_OBTENER_USUARIOS_ENTIDAD:
                	String idEntidad = (String) query.getParams().get(ConstantesLocalGISEIEL.ID_ENTIDAD);
                	geoConn.getUsuariosEntidad(oos, idEntidad);
                	break;
                case ConstantesLocalGISEIEL.ACTION_CALCULAR_INDICES_MUNICIPALES:
                {	
                	geoConn.calcularIndicesMunicipales(oos, userSesion);
                    break;
                }
                
                case ConstantesLocalGISEIEL.ACTION_CALCULAR_INDICES_OBRAS:
                {	
                	geoConn.calcularIndicesObras(oos, userSesion);
                    break;
                }
                case ConstantesLocalGISEIEL.ACTION_VALIDACION_MPT:
                {	
                	
                	System.out.println((new StringBuilder(" *********************************** ACTION_VALIDACION_MPT INICIO ")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
                	String fase = (String)query.getParams().get(ConstantesLocalGISEIEL.FASE_MPT);
                	int cuadro = (Integer)query.getParams().get(ConstantesLocalGISEIEL.CUADRO_VALIDACION_MPT);
                	ArrayList lstValCuadros = (ArrayList)query.getParams().get(ConstantesLocalGISEIEL.CUADRO_VALIDACIONES_CUADROS_MPT);
                	
                	Integer idMunicipio = (Integer)query.getParams().get(ConstantesLocalGISEIEL.KEY_ID_MUNICIPIO);
                	geoConn.validacionMPT(oos, userSesion, fase, idMunicipio, cuadro, lstValCuadros);
                	System.out.println((new StringBuilder(" ************************************ ACTION_VALIDACION_MPT FIN ")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
                    break;
                }
                case ConstantesLocalGISEIEL.ACTION_EXPORT_MPT:
                {	
                	String fase = (String) query.getParams().get(ConstantesLocalGISEIEL.FASE_MPT);
                	geoConn.exportMPT(oos,userSesion,fase);
                    break;
                }
                case ConstantesLocalGISEIEL.ACTION_GET_CUADROS_MPT:
                {	
                	geoConn.obtenerCuadrosMPT(oos,userSesion);
                    break;
                }
                case ConstantesLocalGISEIEL.ACTION_GET_VALIDACIONES_MPT:
                {	
                	geoConn.obtenerValidacionesMPT(oos,userSesion);
                    break;
                }
                case ConstantesLocalGISEIEL.ACTION_GET_POBLAMIENTOS_MPT:
                {	
                	ArrayList lstIdMunicipios = (ArrayList) query.getParams().get(ConstantesLocalGISEIEL.LST_ID_MUNICIPIO_CUADROS_MPT);
                	String provincia = (String)query.getParams().get(ConstantesLocalGISEIEL.PROVINCIA_MPT);
                	geoConn.obtenerPoblamientosMPT(oos,userSesion,provincia, lstIdMunicipios);
                	break;
                }                
//                case ConstantesLocalGISEIEL.ACTION_GET_PLANTILLAS:
//                {
//                    String path = ((String)query.getParams().get(ConstantesLocalGISEIEL.KEY_PATH_PLANTILLAS));
//                    String filtro = ((String)query.getParams().get(ConstantesLocalGISEIEL.FILTRO));
//                    String patron = ((String)query.getParams().get(ConstantesLocalGISEIEL.PATRON));
//                    ArrayList patrones = ((ArrayList)query.getParams().get(ConstantesLocalGISEIEL.PATRONES));
//                	geoConn.returnPlantillas(oos, path,filtro,patron,patrones,userSesion.getIdEntidad());
//                    break;
//                }
                case ConstantesLocalGISEIEL.ACTION_GET_PLANTILLAS_CUADROS:
                {
                    String path = ((String)query.getParams().get(ConstantesLocalGISEIEL.KEY_PATH_PLANTILLAS));
                    String filtro = ((String)query.getParams().get(ConstantesLocalGISEIEL.FILTRO));
                	geoConn.returnPlantillasCuadros(oos, path,filtro);
                    break;
                }
                case ConstantesLocalGISEIEL.ACTION_GET_NODOS_EIEL:
                {
                    String nodo = ((String)query.getParams().get(ConstantesLocalGISEIEL.KEY_NODO_EIEL));
                    String locale= ((String)query.getParams().get(ConstantesLocalGISEIEL.Locale));
                	geoConn.returnNodosEIEL(oos, nodo,locale);
                    break;
                }
                case ConstantesLocalGISEIEL.ACTION_GET_CAMPOS_CAPA_EIEL:
                {
                    String nodo = ((String)query.getParams().get(ConstantesLocalGISEIEL.KEY_NODO_EIEL));
                    String locale= ((String)query.getParams().get(ConstantesLocalGISEIEL.Locale));
                	geoConn.returnCamposCapaEIEL(oos, nodo,locale);
                    break;
                }
                case ConstantesLocalGISEIEL.ACTION_GET_NUCLEOS_MUNICIPIO:
                {
                	Integer idMunicipio = (Integer)query.getParams().get(ConstantesLocalGISEIEL.KEY_ID_MUNICIPIO);
                	geoConn.returnNucleosMunicipio(oos, idMunicipio);
                    break;
                }
               
                case ConstantesLocalGISEIEL.ACTION_GET_INDICADORES_EIEL:
                {
                	geoConn.returnIndicadoresEIEL(oos);
                    break;
                }
                case ConstantesLocalGISEIEL.ACTION_GET_MUNICIPIOS_EIEL:
                {
                	Integer idMunicipio = (Integer)query.getParams().get(ConstantesLocalGISEIEL.KEY_ID_MUNICIPIO);
                	geoConn.returnMunicipios(oos,idMunicipio);
                    break;
                }
                
                case ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_CLEANDATA:
                {
                    logger.debug("Limpiando datos. Municipio "+userSesion.getIdMunicipio());
                	geoConn.initIndicadoresCleanData(userSesion.getIdMunicipio());             	
                    break;
                }       
                
                case ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_POBLACION:
                {
                	geoConn.initIndicadoresLoadPoblacionViviendaPlaneamiento(userSesion.getIdMunicipio());               	
                    break;
                }        
                case ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_CICLOAGUA:
                {
                	geoConn.initIndicadoresLoadCicloAgua(userSesion.getIdMunicipio());               	
                    break;
                }    
                case ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_INFRAESTRUCTURAS:
                {
                	geoConn.initIndicadoresLoadInfraestructuras(userSesion.getIdMunicipio());               	
                    break;
                }   
                case ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_RESIDUOSURBANOS:
                {
                	geoConn.initIndicadoresLoadResiduosUrbanos(userSesion.getIdMunicipio());               	
                    break;
                }         
                case ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_EDUCACIONCULTURA:
                {
                	geoConn.initIndicadoresLoadEducacionCultura(userSesion.getIdMunicipio());               	
                    break;
                }                        
                case ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_SANITARIOASISTENCIAL:
                {
                	geoConn.initIndicadoresLoadSanitarioAsistencial(userSesion.getIdMunicipio());               	
                    break;
                }                                                        
                case ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_OTROS:
                {
                	geoConn.initIndicadoresLoadOtros(userSesion.getIdMunicipio());               	
                    break;
                }                                        
                case ConstantesLocalGISEIEL.ACTION_PUBLICAR_ELEMENTO:
                {
                	Object object = query.getParams().get(ConstantesLocalGISEIEL.OBJECT);
                	String tipoElemento = (String) query.getParams().get(ConstantesLocalGISEIEL.TIPO_ELEMENTO);                	
                	geoConn.returnPublicarElemento(oos, object,tipoElemento, userSesion);
                    break;
                }
                case ConstantesLocalGISEIEL.ACTION_VALIDAR_ELEMENTO:
                {
                	Object object = query.getParams().get(ConstantesLocalGISEIEL.OBJECT);
                	String tipoElemento = (String) query.getParams().get(ConstantesLocalGISEIEL.TIPO_ELEMENTO);                	
                	geoConn.returnValidarElemento(oos, object, tipoElemento, userSesion);
                    break;
                }
                case ConstantesLocalGISEIEL.ACTION_GET_NUM_ELEMENTOS_PENDIENTES:
                {
                    String nodo = ((String)query.getParams().get(ConstantesLocalGISEIEL.KEY_NODO_EIEL));        
                    String locale= ((String)query.getParams().get(ConstantesLocalGISEIEL.Locale));
                    Integer idMunicipio = (Integer)query.getParams().get(ConstantesLocalGISEIEL.KEY_ID_MUNICIPIO);
                	geoConn.returnNumElementosPendientes(oos, nodo,idMunicipio,locale);
                    break;
                }               
                
                               
            }
        }
        catch(PermissionException pe){
        	logger.info("Error al ejecutar la operacion",pe);
            try{
                oos.writeObject(new ACException(pe));
            }
            catch(Exception ex){};
        }
        catch(SystemMapException pe){
        	logger.info("Error al ejecutar la operacion",pe);
            try{oos.writeObject(new ACException(pe));
            }
            catch(Exception ex){};
        }/** MultiPartPost */
        catch (org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException pe){
        	logger.info("Error al ejecutar la operacion",pe);

            try{
                oos.writeObject(new ACException(pe));
            }
            catch(Exception ex){};
        }
        catch (java.lang.Error e){
        	logger.info("Error al ejecutar la operacion",e);
        	e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            try{
                oos.writeObject(new ACException(e));
            }
            catch(Exception ex){};
        }
        catch(Throwable e) {
        	logger.info("Error al ejecutar la operacion",e);
            e.printStackTrace();
            log(e.getMessage());
        }
        finally{
        	//logger.info("FINALIZAMOS");
            try{oos.close();}catch(Exception e){};
        }
    }

    /**
     * Metodo que obtiene la sesion del usuario que ha hecho la peticion.
     *
     * @param request Datos de la peticion.
     * */
    private Sesion sesion(HttpServletRequest request)
    {
        Sesion sRet=null;
        JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
        if (userPrincipal!=null)
        {
            String  sIdSesion= userPrincipal.getName();
            sRet=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
        }
        return sRet;
    }

    
    
}
