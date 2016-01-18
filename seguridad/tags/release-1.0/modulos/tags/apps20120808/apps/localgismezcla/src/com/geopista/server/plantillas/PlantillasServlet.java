package com.geopista.server.plantillas;

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

//import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.plantillas.ConstantesLocalGISPlantillas;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.server.administradorCartografia.SRID;
import com.geopista.server.administradorCartografia.SystemMapException;
import com.geopista.server.database.COperacionesPlantillas;
import com.geopista.util.ServletContextListener;


/**
 * Servlet que recibe las peticiones http de los clientes para hacer peticiones a la BBDD. Se envian peticiones
 * multipart, por si el tamaño de las peticiones fuera grande. Se parsea a xml y en esta clase se desparsean y se
 * recoje el elemento ACQuery. Con la accion se discrimina la peticion y se obtienen los parametros en caso de que
 * los haya.
 * */

public class PlantillasServlet extends HttpServlet
{
    private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(PlantillasServlet.class);

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

        ObjectOutputStream oos=null;
        try{
            JAASUserPrincipal jassUserPrincipal = (JAASUserPrincipal) request.getUserPrincipal();
            Sesion userSesion = PasarelaAdmcar.listaSesiones.getSesion(jassUserPrincipal.getName());

            COperacionesPlantillas geoConn=null;
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

            geoConn= new COperacionesPlantillas(srid);
            response.setHeader("Transfer-encoding","chunked");
            response.setHeader("Content-Encoding", "gzip");
            oos= new ObjectOutputStream(new GZIPOutputStream(response.getOutputStream()));

            switch (query.getAction())
            {                              
                                
                case ConstantesLocalGISPlantillas.ACTION_GET_PLANTILLAS:
                {
                    String path = ((String)query.getParams().get(ConstantesLocalGISPlantillas.KEY_PATH_PLANTILLAS));
                    String filtro = ((String)query.getParams().get(ConstantesLocalGISPlantillas.FILTRO));
                    String patron = ((String)query.getParams().get(ConstantesLocalGISPlantillas.PATRON));
                    ArrayList patrones = ((ArrayList)query.getParams().get(ConstantesLocalGISPlantillas.PATRONES));
                	geoConn.returnPlantillas(oos, path,filtro,patron,patrones,userSesion.getIdEntidad(), userSesion.getIdMunicipio());
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
        }
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
            sRet=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
        }
        return sRet;
    }

    
    
}
