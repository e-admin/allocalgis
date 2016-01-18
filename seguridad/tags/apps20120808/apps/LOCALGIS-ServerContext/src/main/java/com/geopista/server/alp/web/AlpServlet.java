package com.geopista.server.alp.web;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.exolab.castor.xml.Unmarshaller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.*;
import java.util.*;
import java.util.zip.GZIPOutputStream;

import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.Sesion;
import com.geopista.app.alptolocalgis.beans.ConstantesAlp;

import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.administradorCartografia.*;
import com.geopista.server.database.COperacionesALP;
import com.geopista.util.ServletContextListener;
import com.localgis.server.SessionsContextShared;

import admcarApp.PasarelaAdmcar;


/**
 * Servlet que recibe las peticiones http de los clientes para hacer peticiones a la BBDD. Se envian peticiones
 * multipart, por si el tamaño de las peticiones fuera grande. Se parsea a xml y en esta clase se desparsean y se
 * recoje el elemento ACQuery. Con la accion se discrimina la peticion y se obtienen los parametros en caso de que
 * los haya.
 * */

public class AlpServlet extends LoggerHttpServlet
{
  	private static final long serialVersionUID = -2038982838175392964L;

	private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(AlpServlet.class);

    public int numeroServlet = 0;
    
    public void init(ServletConfig config) throws ServletException{
    super.init(config);

    
    ServletContextListener.numeroCat ++;

       numeroServlet = ServletContextListener.numeroCat;

       System.out.println("Instantiated Cat a new "+config.getServletName()+" object. Total:"+ServletContextListener.numeroCat);


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
    	super.doPost(request);
    	logger.error("[CatastroServlet : doPost ]: Entrando en servlet catastro");
        ObjectOutputStream oos=null;
        try{
            JAASUserPrincipal jassUserPrincipal = (JAASUserPrincipal) request.getUserPrincipal();
            
            PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");    
	    	
            Sesion userSesion = PasarelaAdmcar.listaSesiones.getSesion(jassUserPrincipal.getName());

            COperacionesALP geoConn=null;
            ACQuery query= new ACQuery();
            logger.error("[AlpServlet : doPost ]: dentro try");
            if(!org.apache.commons.fileupload.DiskFileUpload.isMultipartContent(request))
            {
                /* -- PostMethod --  */

            logger.error("[AlpServlet : doPost ]: en if cogiendo la query");
                System.out.println("MESAJE XML ----------"+request.getParameter("mensajeXML"));

                System.out.println(request.getParameter("mensajeXML"));

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
                       else if (item.getFieldName().equalsIgnoreCase(com.geopista.protocol.net.EnviarSeguro.idMunicipio)){
                           String sMunicipio = item.getString("ISO-8859-1");
                           userSesion.setIdMunicipio(sMunicipio);
                        }
                    }
                }
            }

            geoConn= new COperacionesALP();
            response.setHeader("Transfer-encoding","chunked");
            response.setHeader("Content-Encoding", "gzip");
            oos= new ObjectOutputStream(new GZIPOutputStream(response.getOutputStream()));

            switch (query.getAction())
            {
                case ConstantesAlp.ACTION_OBTENER_OPERACIONES:
                {
                	geoConn.getOperaciones(oos, userSesion.getIdMunicipio());
                    break;
                    
                }
                
                case ConstantesAlp.ACTION_OBTENER_OPERACIONES_FILTRO:
                {
                	String filtro = (String)query.getParams().get(ConstantesAlp.FILTRO);
                	geoConn.getOperaciones(oos, filtro, userSesion.getIdMunicipio());
                    break;
                    
                }
                
                case ConstantesAlp.ACTION_ELIMINAR_OPERACIONES:
                {
                	Integer idOperacion = (Integer)query.getParams().get(ConstantesAlp.IDOPERACION);
                	geoConn.removeOperacion(oos, idOperacion);
                    break;
                    
                }
                
                case ConstantesAlp.ACTION_GET_ID_USUARIO:
                {
                    String idSesion = userSesion.getIdUser();
                    oos.writeObject(idSesion);                        
                    break;
                }
                
                case ConstantesAlp.ACTION_OBTENER_IDMAPA:
                {
                	String nombreMapa = (String)query.getParams().get(ConstantesAlp.NOMBREMAPA);
                	geoConn.getIdMapa(oos, nombreMapa, userSesion.getIdEntidad());
                    break;
                }
            }
        }
        catch(PermissionException pe){
            try{
                oos.writeObject(new ACException(pe));
            }
            catch(Exception ex){};
        }
        catch(SystemMapException pe){
            try{oos.writeObject(new ACException(pe));
            }
            catch(Exception ex){};
        }/** MultiPartPost */
        catch (org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException pe){
            try{
                oos.writeObject(new ACException(pe));
            }
            catch(Exception ex){};
        }
        catch(Exception e) {
            e.printStackTrace();
            log(e.getMessage());
        }/** java.lang.OutOfMemoryError */
        catch (java.lang.Error e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            try{
                oos.writeObject(new ACException(e));
            }
            catch(Exception ex){};
        }
        finally{
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
            
            PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");    
	    	            
            sRet=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
        }
        return sRet;
    }

    
    
}
