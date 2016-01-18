package com.geopista.server.catastro.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.exolab.castor.xml.Unmarshaller;

import admcarApp.PasarelaAdmcar;

import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.BienInmuebleJuridico;
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.model.beans.EntidadGeneradora;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.Fichero;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.intercambio.importacion.xml.contents.UnidadDatosIntercambio;
import com.geopista.app.catastro.servicioWebCatastro.beans.IdentificadorDialogo;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.server.administradorCartografia.SystemMapException;
import com.geopista.server.catastro.servicioWebCatastro.CabeceraFinEntrada;
import com.geopista.server.catastro.servicioWebCatastro.CabeceraVARPAD;
import com.geopista.server.catastro.servicioWebCatastro.FicheroExportacion;
import com.geopista.server.catastro.servicioWebCatastro.PeticionSWCatastro;
import com.geopista.server.catastro.servicioWebCatastro.UnidadRegistroBI;
import com.geopista.server.catastro.servicioWebCatastro.UnidadRegistroFinca;
import com.geopista.server.database.COperacionesServerCatastroDGC;
import com.geopista.server.database.CPoolDatabase;
import com.geopista.util.ServletContextListener;



/**
 * Servlet que recibe las peticiones http de los clientes para hacer peticiones a la BBDD. Se envian peticiones
 * multipart, por si el tamaño de las peticiones fuera grande. Se parsea a xml y en esta clase se desparsean y se
 * recoje el elemento ACQuery. Con la accion se discrimina la peticion y se obtienen los parametros en caso de que
 * los haya.
 * */



public class CatastroServlet extends HttpServlet
{
    private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(CatastroServlet.class);

    public int numeroServlet = 0;
    
    private static boolean dbOracle = false;
    
    public void init(ServletConfig config) throws ServletException{
    	
    	super.init(config);

    	try{

//    		Connection conn=CPoolDatabase.getConnection();
//    		if (conn!=null){
//    			if (((org.enhydra.jdbc.core.CoreConnection)conn).con instanceof org.postgresql.PGConnection)
//    				dbOracle=false;
//    			else
//    				dbOracle=true;
//    			conn.close();
//    			CPoolDatabase.releaseConexion();
//
//    		}
//    		else{
//    			logger.info("Conexion contra la BD no disponible");
//    		}

    		ServletContextListener.numeroCat ++;

    		numeroServlet = ServletContextListener.numeroCat;

    		System.out.println("Instantiated Cat a new "+config.getServletName()+" object. Total:"+ServletContextListener.numeroCat);

    	} catch (Exception e){
    		e.printStackTrace();
    		logger.error(e.getMessage());
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
    	    	
    	//System.out.println("[CatastroServlet : doPost ]: Entrando en servlet catastro");
        logger.error("[CatastroServlet : doPost ]: Entrando en servlet catastro");
        ObjectOutputStream oos=null;
        try{
            JAASUserPrincipal jassUserPrincipal = (JAASUserPrincipal) request.getUserPrincipal();
            Sesion userSesion = PasarelaAdmcar.listaSesiones.getSesion(jassUserPrincipal.getName());

            COperacionesServerCatastroDGC geoConn=null;
            ACQuery query= new ACQuery();
            Vector docsFiles= new Vector();
            logger.error("[CatastroServlet : doPost ]: dentro try");
            if(!org.apache.commons.fileupload.DiskFileUpload.isMultipartContent(request))
            {
                /* -- PostMethod --  */

            logger.debug("[CatastroServlet : doPost ]: en if cogiendo la query");
            logger.debug("MENSAJE XML ----------"+request.getParameter("mensajeXML"));

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

            if (!dbOracle){
            	geoConn= new COperacionesServerCatastroDGC();
            }            
            
            response.setHeader("Transfer-encoding","chunked");
            response.setHeader("Content-Encoding", "gzip");
            oos= new ObjectOutputStream(new GZIPOutputStream(response.getOutputStream()));

            Sesion sesion= sesion(request);
            switch (query.getAction())
            {
                case ConstantesRegExp.ACTION_CREAR_EXPEDIENTE:
                {
                    Expediente exp= (Expediente)query.getParams().get(ConstantesRegExp.OBJETO_EXPEDIENTE);
                    geoConn.crearExpediente(oos,exp, userSesion.getIdUser() );

                    //PRUEBAS SERVICIOS WEB PeticionSWCatastro.peticionCrearExpSinParcelas(exp, userSesion.getUserPrincipal().getName());
                    break;
                }
                case ConstantesRegExp.ACTION_GET_EXPEDIENTES_USUARIO:
                {
                    String idUsuario = userSesion.getIdUser();
                    String expCerrado = (String)query.getParams().get(ConstantesRegExp.BOOLEAN_EXP_CERRADO);
                    String convenio = (String) query.getParams().get(ConstantesRegExp.STRING_CONVENIO);
                    String modoTrabajo = (String) query.getParams().get(ConstantesRegExp.STRING_MODO_TRABAJO);
                    
                    boolean isAcoplado = false;
                    if(modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                    {
                    	isAcoplado = true;
                    }
                  	
                    Vector aux= new Vector();
                    aux.add("Catastro.RegistroExpediente.Admin");
                    if((userSesion.getConjuncion(aux)).size()>0)
                    {
                        idUsuario= "admin";
                    }
                    geoConn.getExpedientesUsuario(oos,idUsuario, expCerrado, convenio, userSesion.getIdMunicipio(), isAcoplado);
                    break;
                }
                case ConstantesRegExp.ACTION_GET_USUARIOS_CON_EXPEDIENTE:
                {
                    Vector aux= new Vector();
                    aux.add("Catastro.RegistroExpediente.Admin");
                    if((userSesion.getConjuncion(aux)).size()>0)
                    {
                        geoConn.getUsuariosConExpediente(oos);
                    }
                    else
                    {
                        Hashtable usr= new Hashtable();
                        usr.put(userSesion.getIdUser(),userSesion.getUserPrincipal().getName());
                        oos.writeObject(usr);
                    }
                    break;
                }
                case ConstantesRegExp.ACTION_GET_ENTIDADES_GENERADORAS:
                {
                    geoConn.getCodigoEntidadGeneradora(oos);
                    break;
                }
                case ConstantesRegExp.ACTION_GET_CODIGOS_ESTADOS:
                {
                    geoConn.getCodigoEstados(oos);
                    break;
                }
                case ConstantesRegExp.ACTION_GET_FINCA_CATASTRO_POR_REFERENCIA_CATASTRAL:
                {
                    ArrayList refCatas = (ArrayList)query.getParams().get(ConstantesRegExp.OBJETO_LISTA_REFERENCIAS_CATASTRALES);
                    geoConn.getFincaCatastroPorRefCatastral(oos, refCatas, userSesion.getIdMunicipio());
                    break;
                }
                case ConstantesRegExp.ACTION_GET_FINCA_CATASTRO_BUSCADAS:
                {
                    String patron= (String)query.getParams().get(ConstantesRegExp.STRING_PATRON_REF_CATASTRAL);
                    geoConn.getFincaCatastroBuscadas(oos, userSesion.getIdMunicipio(), patron);
                    break;
                }
                case ConstantesRegExp.ACTION_GET_FINCA_CATASTRO_RUSTICA_BUSCADAS_POR_POLIGONO:
                {
                    String patronPoligono= (String)query.getParams().get(ConstantesRegExp.STRING_PATRON_POLIGONO);
                    String patronParcela= (String)query.getParams().get(ConstantesRegExp.STRING_PATRON_PARCELA);
                    geoConn.getFincaCatastroRusticasBuscadasPorPoligono(oos, userSesion.getIdMunicipio(), patronPoligono, patronParcela);
                    break;
                }
                case ConstantesRegExp.ACTION_GET_FINCA_CATASTRO_BUSCADAS_POR_DIR:
                {
                    String patronNombreVia= (String)query.getParams().get(ConstantesRegExp.STRING_PATRON_NOMBRE_VIA);
                    String patronTipoVia= (String)query.getParams().get(ConstantesRegExp.STRING_PATRON_TIPO_VIA);
                    geoConn.getFincaCatastroBuscadasPorDir(oos, userSesion.getIdMunicipio(), patronNombreVia,patronTipoVia);
                    break;
                }
                case ConstantesRegExp.ACTION_GET_PONENCIA_ZONA_VALOR:
                {
                    String codZonaValor= (String)query.getParams().get(ConstantesRegExp.STRING_PONENCIA_ZONA_VALOR);
                    geoConn.obtenerPonenciaZonaValorBD(oos, codZonaValor, userSesion.getIdMunicipio());
                    break;
                }
                case ConstantesRegExp.ACTION_GET_PONENCIA_URBANISTICA:
                {
                    String codZonaValor= (String)query.getParams().get(ConstantesRegExp.STRING_PONENCIA_URBANISTICA);
                    geoConn.obtenerPonenciaUrbanisticaBD(oos, codZonaValor, userSesion.getIdMunicipio());
                    break;
                }
                case ConstantesRegExp.ACTION_GET_PONENCIA_POLIGONO:
                {
                    String codPonenciaPoligono = (String)query.getParams().get(ConstantesRegExp.STRING_PONENCIA_POLIGONO);
                    geoConn.obtenerPonenciaPoligonoBD(oos, codPonenciaPoligono, userSesion.getIdMunicipio());
                    break;
                }
                case ConstantesRegExp.ACTION_GET_PONENCIA_TRAMOS:
                {
                    String codTramoPonencia = (String)query.getParams().get(ConstantesRegExp.STRING_PONENCIA_TRAMOS);
                    geoConn.obtenerPonenciaTramosBD(oos, codTramoPonencia, userSesion.getIdMunicipio());
                    break;
                }
                case ConstantesRegExp.ACTION_GET_BIEN_INMUEBLE_BUSCADAS:
                {
                    String patron= (String)query.getParams().get(ConstantesRegExp.STRING_PATRON_REF_CATASTRAL);
                    geoConn.getBienInmuebleCatastroBuscados(oos, userSesion.getIdMunicipio(), patron);
                    break;
                }
                case ConstantesRegExp.ACTION_GET_BIEN_INMUEBLE_BUSCADAS_POR_DIR:
                {
                    String patronNombreVia= (String)query.getParams().get(ConstantesRegExp.STRING_PATRON_NOMBRE_VIA);
                    String patronTipoVia= (String)query.getParams().get(ConstantesRegExp.STRING_PATRON_TIPO_VIA);
                    geoConn.getBienInmuebleCatastroBuscadosPorDir(oos, userSesion.getIdMunicipio(), patronNombreVia,patronTipoVia);
                    break;
                }
                case ConstantesRegExp.ACTION_GET_BIEN_INMUEBLE_RUSTICO_BUSCADAS_POR_POLIGONO:
                {
                    String patronPoligono= (String)query.getParams().get(ConstantesRegExp.STRING_PATRON_POLIGONO);
                    String patronParcela= (String)query.getParams().get(ConstantesRegExp.STRING_PATRON_PARCELA);
                    geoConn.getBienInmuebleCatastroRusticoBuscadosPorPoligono(oos, userSesion.getIdMunicipio(), patronPoligono, patronParcela);
                    break;
                }
                case ConstantesRegExp.ACTION_GET_ESTADO_SIGUIENTE:
                {
                    geoConn.getEstadoSiguiente(oos);
                    break;
                }
                case ConstantesRegExp.ACTION_UPDATE_EXPEDIENTE:
                {
                    Expediente exp = (Expediente)query.getParams().get(ConstantesRegExp.OBJETO_EXPEDIENTE);
                    geoConn.updateExpediente(oos, exp);
                    break;
                }
                case ConstantesRegExp.ACTION_CONSULTA_HISTORICO_FICHEROS:
                {
                    geoConn.consultaHistoricoFicheros(oos, userSesion.getIdMunicipio());
                    break;
                }
                case ConstantesRegExp.ACTION_GET_VIA_POR_NOMBRE:
                {
                    String nombre = ((String)query.getParams().get(ConstantesRegExp.STRING_NOMBRE_VIA));
                    geoConn.getViaPorNombre(oos, nombre,userSesion.getIdMunicipio());
                    break;
                }
                case ConstantesRegExp.ACTION_COMPROBAR_ACTUALIZACIONES_ENVIOS:
                {
                    geoConn.comprobarActualizacionYEnvios(oos, userSesion.getIdMunicipio());
                    break;
                }
                case ConstantesRegExp.ACTION_GET_CODIGO_NOMBRE_PROVINCIA:
                {
                    geoConn.getCodigoNombreProvincia(oos);
                    break;
                }
                case ConstantesRegExp.ACTION_GET_CODIGO_NOMBRE_MUNICIPIO:
                {
                    String codigoProvincia = ((String)query.getParams().get(ConstantesRegExp.CODIGO_PROVINCIA));
                    geoConn.getCodigoNombreMunicipio(oos, codigoProvincia);
                    break;
                }
                case ConstantesRegExp.ACTION_GET_CODIGO_DGC_MUNICIPIO:
                {
                    String codigoMunicipioINE = ((String)query.getParams().get(ConstantesRegExp.CODIGO_MUNICIPIO_INE));
                    String codigoProvincia = ((String)query.getParams().get(ConstantesRegExp.CODIGO_PROVINCIA));
                    geoConn.getCodigoDGCMunicipio(oos, codigoMunicipioINE, codigoProvincia);
                    break;
                }
                case ConstantesRegExp.ACTION_GUARDAR_CONFIGURACION:
                {
                    DatosConfiguracion datos = ((DatosConfiguracion)query.getParams().get(ConstantesRegExp.PARAM_CONFIGURACION));
                    geoConn.guardarParametroConfiguracion(oos, datos, userSesion.getIdMunicipio());
                    break;
                }
                case ConstantesRegExp.ACTION_GET_CONFIGURACION:
                {
                    geoConn.getParametrosConfiguracion(oos, userSesion.getIdMunicipio());
                    break;
                }
                case ConstantesRegExp.ACTION_GET_TIPOS_EXPEDIENTES:
                {
                    String convenio = (String)query.getParams().get(ConstantesRegExp.STRING_CONVENIO);
                    geoConn.getTiposExpedientes(oos, convenio);
                    break;
                }

                case ConstantesRegExp.ACTION_MODIFICAR_EXPEDIENTE:
                {
                	System.out.println("SERVLET");
                	Expediente exp= (Expediente)query.getParams().get(ConstantesRegExp.OBJETO_EXPEDIENTE);
                    geoConn.modificarExpediente(oos,exp, "satec", userSesion.getIdUser() );
                    
                	break;
                }

                case ConstantesRegExp.ACTION_GET_BIEN_INMUEBLE_BUSCADOS_POR_TITULAR:
                {
                    String patronNif= (String)query.getParams().get(ConstantesRegExp.STRING_PATRON_NIF);
                    geoConn.getBienesInmueblesBuscadasPorTitular(oos, patronNif, userSesion.getIdMunicipio());
                    break;
                }
                case ConstantesRegExp.ACTION_CREAR_EXPEDIENTE_CATASTRO:
                {
                    Expediente exp= (Expediente)query.getParams().get(ConstantesRegExp.OBJETO_EXPEDIENTE);
                    geoConn.crearExpedienteCatastro(oos, exp,userSesion.getIdUser());
                    break;
                }
                case ConstantesRegExp.ACTION_CREAR_FICHERO:
                {
                    Fichero fich= (Fichero)query.getParams().get(ConstantesRegExp.OBJETO_FICHERO);
                    ArrayList listaNumExp = (ArrayList)query.getParams().get(ConstantesRegExp.ARRAY_LISTA_NUM_EXP);
                    geoConn.crearFichero(oos, listaNumExp,fich, userSesion.getIdMunicipio());
                    break;
                }
                case ConstantesRegExp.ACTION_EXPORTACION_MASIVA:{
                    String modoTrabajo = (String)query.getParams().get(ConstantesRegExp.STRING_MODO_TRABAJO);
                    String convenio = (String)query.getParams().get(ConstantesRegExp.STRING_CONVENIO);

                    String directorio =null;
                    ArrayList expedientes = null;
                    
                    if(modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))  {
                     	
                    	directorio = (String)query.getParams().get(ConstantesRegExp.STRING_DIRECTORIO_EXPORTACION);
                        String nombreFicheroFinEntrada = (String)query.getParams().get(ConstantesRegExp.STRING_NOMBRE_FICHERO_FIN_ENTRADA_EXPORTACION);
                        String nombreFicheroVARPAD = (String)query.getParams().get(ConstantesRegExp.STRING_NOMBRE_FICHERO_VARPAD_EXPORTACION);

                        Expediente expObj = (Expediente)query.getParams().get(ConstantesRegExp.OBJETO_EXPEDIENTE);
                        expedientes  = new ArrayList();
                        expedientes.add(expObj);
                       // expedientes = geoConn.getExpedientesExportacionMasiva(oos, userSesion.getIdMunicipio(),convenio);

                        if(expedientes.isEmpty()) 
                            oos.writeObject(new ACException("No hay expedientes para exportar."));
                        else{
                        	
                        	FicheroExportacion ficheroExportacion = new FicheroExportacion();

                        	if(convenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD) &&
                        			expObj.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)){
                            	buildVARPADRemote(oos, expedientes, directorio, nombreFicheroVARPAD, modoTrabajo, geoConn, userSesion.getIdMunicipio(),ficheroExportacion);

                            }
                        	 else if (convenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO) &&
                                     expObj.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)){
                        			buildVARPADRemote(oos, expedientes, directorio, nombreFicheroVARPAD, modoTrabajo, geoConn, userSesion.getIdMunicipio(),ficheroExportacion);
                        	 }
                            else{
                                ArrayList listaExpTitularidad = new ArrayList();
                                ArrayList listaExpFiscEconomic = new ArrayList();

                                for(int i= 0;i<expedientes.size();i++) {
                                    Expediente exp = (Expediente) expedientes.get(i);

                                    if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                                        listaExpTitularidad.add(exp);
                                    else if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)) 
                                        listaExpFiscEconomic.add(exp);
                                }

                                if(!listaExpTitularidad.isEmpty())
                                	buildVARPADRemote(oos, listaExpTitularidad, directorio, nombreFicheroVARPAD, modoTrabajo, geoConn, userSesion.getIdMunicipio(),ficheroExportacion);

                                if(!listaExpFiscEconomic.isEmpty()){
                                	
                                	buildFINEntradaRemote(oos, listaExpFiscEconomic, directorio, nombreFicheroFinEntrada, modoTrabajo, geoConn, userSesion.getIdMunicipio(),ficheroExportacion);
                                	
                                	// Descargar los ficheros DXF y ASC en el directorio destino dentro de la carpeta con la ref catastral de la parcela
                                	
                                }
                            }
                            
                            oos.writeObject(ficheroExportacion);
                        }
                    }
                    else{
                    	
                        directorio = (String)query.getParams().get(ConstantesRegExp.STRING_DIRECTORIO_EXPORTACION);
                        String nombreFicheroFinEntrada = (String)query.getParams().get(ConstantesRegExp.STRING_NOMBRE_FICHERO_FIN_ENTRADA_EXPORTACION);
                        String nombreFicheroVARPAD = (String)query.getParams().get(ConstantesRegExp.STRING_NOMBRE_FICHERO_VARPAD_EXPORTACION);

                        expedientes = geoConn.getExpedientesExportacionMasiva(oos, userSesion.getIdMunicipio(),convenio);
                      
                        
                        expedientes = geoConn.getExpedientesExportacionMasiva(oos, userSesion.getIdMunicipio(),convenio);

                        if(expedientes.isEmpty()) 
                            oos.writeObject(new ACException("No hay expedientes para exportar."));
                        else{
                        	
                        	
                        	FicheroExportacion ficheroExportacion = new FicheroExportacion();

                            if(convenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)){
                            	buildVARPADRemote(oos, expedientes, directorio, nombreFicheroVARPAD, modoTrabajo, geoConn, userSesion.getIdMunicipio(),ficheroExportacion);

                            }
                            else{
                                ArrayList listaExpTitularidad = new ArrayList();
                                ArrayList listaExpFiscEconomic = new ArrayList();

                                for(int i= 0;i<expedientes.size();i++) {
                                    Expediente exp = (Expediente) expedientes.get(i);

                                    if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                                        listaExpTitularidad.add(exp);
                                    else if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)) 
                                        listaExpFiscEconomic.add(exp);
                                }

                                if(!listaExpTitularidad.isEmpty())
                                	buildVARPADRemote(oos, listaExpTitularidad, directorio, nombreFicheroVARPAD, modoTrabajo, geoConn, userSesion.getIdMunicipio(),ficheroExportacion);

                                if(!listaExpFiscEconomic.isEmpty()){
                                	
                                	buildFINEntradaRemote(oos, listaExpFiscEconomic, directorio, nombreFicheroFinEntrada, modoTrabajo, geoConn, userSesion.getIdMunicipio(),ficheroExportacion);
                                	
                                	// Descargar los ficheros DXF y ASC en el directorio destino dentro de la carpeta con la ref catastral de la parcela
                                	
                                }
                            }
                            
                            oos.writeObject(ficheroExportacion);
                        }

                    }
                    break;
                }
                case ConstantesRegExp.ACTION_GET_ID_USUARIO:
                {
                    String idSesion = userSesion.getIdUser();
                    oos.writeObject(idSesion);                        
                    break;
                }
                case ConstantesRegExp.ACTION_SINCRONIZA_EXPEDIENTE:
                {
                    //TODO
                	throw new NoSuchMethodError("Not implemented: ACTION_SINCRONIZA_EXPEDIENTE");
                }
                case ConstantesRegExp.ACTION_EXISTE_REFERENCIA_CATASTRO_TEMPORAL:
                {
                	
                	Expediente exp = (Expediente)query.getParams().get(ConstantesRegExp.OBJETO_EXPEDIENTE);
                	geoConn.existenReferenciasCatastro_temporal(oos,exp);
                	
                	break;
                }
                case ConstantesRegExp.ACTION_ESCRIBIR_CATASTRO_TEMPORAL:
                {
                	String modoTrabajo = (String)query.getParams().get(ConstantesRegExp.STRING_MODO_TRABAJO);
                    String convenio = (String)query.getParams().get(ConstantesRegExp.STRING_CONVENIO);
                    Expediente exp = (Expediente)query.getParams().get(ConstantesRegExp.OBJETO_EXPEDIENTE);
                    if(modoTrabajo.equals(DatosConfiguracion.MODO_TRABAJO_DESACOPLADO)){
                    	  geoConn.completaReferenciasToXMLBD(exp, convenio, userSesion.getIdMunicipio());
                    }
                    else{
                    	boolean existeRefCatastTemp = geoConn.existenReferenciasCatastro_temporal(oos,exp);
                    	if(!existeRefCatastTemp){
                    
                    		// no existe ninguna referencia para el expediente en catastro_temporal, por lo 
                    		// tanto se generan los xml para introducirlos en catastro_temporal
                    		geoConn.completaReferenciasToXMLBD(exp, convenio, userSesion.getIdMunicipio());
                    		 
                    		for(int i = 0;i<exp.getListaReferencias().size();i++)
                			{
                				if(exp.getListaReferencias().get(i) instanceof FincaCatastro){
                					geoConn.actualizarExpedienteFincaCatastroOVC(exp.getIdExpediente(),(FincaCatastro)exp.getListaReferencias().get(i), true);
                					
                				}
                				else if(exp.getListaReferencias().get(i) instanceof BienInmuebleJuridico){
                					
                					geoConn.actualizarExpedienteBienInmuebleOVC(exp.getIdExpediente(), ((BienInmuebleJuridico)exp.getListaReferencias().get(i)).getBienInmueble(), true);
                					
                				}
                			}
                    	}	
                    }
                  
                    break;
                }
                case ConstantesRegExp.ACTION_ACTUALIZA_CATASTRO_TEMPORAL:
                {
                    String convenio = (String)query.getParams().get(ConstantesRegExp.STRING_CONVENIO);
                    Expediente exp = (Expediente)query.getParams().get(ConstantesRegExp.OBJETO_EXPEDIENTE);
                    
                    for(int i=0; i<exp.getListaReferencias().size(); i++){
                    	if(exp.getListaReferencias().get(i) instanceof FincaCatastro){
                    		geoConn.actualizarExpedienteFincaCatastroOVC(exp.getIdExpediente(), 
                					(FincaCatastro) exp.getListaReferencias().get(i), 
                					((FincaCatastro) exp.getListaReferencias().get(i)).isActualizadoOVC());
                    	}
                    	else if(exp.getListaReferencias().get(i) instanceof BienInmuebleCatastro){
                    		geoConn.actualizarExpedienteBienInmuebleOVC(exp.getIdExpediente(), 
                    					(BienInmuebleCatastro) exp.getListaReferencias().get(i), 
                    					((BienInmuebleCatastro) exp.getListaReferencias().get(i)).isActualizadoOVC());
                    	}
                    }
                    
                    geoConn.completaReferenciasToXMLBD(exp, convenio, userSesion.getIdMunicipio());
                    
                  
                    
                    break;
                }
             
                case ConstantesRegExp.ACCTION_ELIMINAR_CATASTRO_TEMPORAL_EXP:
                {
                    Expediente exp = (Expediente)query.getParams().get(ConstantesRegExp.OBJETO_EXPEDIENTE);
                    geoConn.eliminaCatastroTemporalExp(oos, exp);
                    break;
                }
                case ConstantesRegExp.ACCTION_FECHA_INICIO_PERIODO_EXP:
                {
                    geoConn.getFechaInicioPeriodoExp(oos, userSesion.getIdMunicipio());
                    break;
                }
                case ConstantesRegExp.ACCTION_GET_TODOS_LOS_USUARIOS:
                {
                    geoConn.getTodosLosUsuarios(oos);
                    break;
                }
                case ConstantesRegExp.ACTION_GET_PARCELAS_EXPEDIENTE:
                {
                    Expediente exp = (Expediente)query.getParams().get(ConstantesRegExp.OBJETO_EXPEDIENTE);
                    String convenio = (String)query.getParams().get(ConstantesRegExp.STRING_CONVENIO);
                    geoConn.getParcelasExpediente(oos,exp,convenio,userSesion.getIdMunicipio());
                    break;
                }
                
                case ConstantesRegExp.ACTION_EXISTE_PARCELA_BD:
                {
                	String refCatastral = (String) query.getParams().get(ConstantesRegExp.STRING_REF_CATASTRAL);
                	geoConn.existeParcelaEn(oos, refCatastral);
                	break;
                }
                case ConstantesRegExp.ACTION_EXISTE_BI_BD:
                {
                	String idBI = (String) query.getParams().get(ConstantesRegExp.STRING_ID_BI);
                	geoConn.existeBIEn(oos, idBI);
                	break;
                }
                case ConstantesRegExp.ACTION_INSERTAR_DATOS_SALIDA:
                {
                	UnidadDatosIntercambio udsa = (UnidadDatosIntercambio) query.getParams().get(ConstantesRegExp.OBJETO_UNIDAD_DATOS_INTERCAMBIO);
                	Boolean insertarExpediente = (Boolean) query.getParams().get(ConstantesRegExp.BOOLEAN_INSERTAR_EXPEDIENTE);
                	Expediente exp = (Expediente)query.getParams().get(ConstantesRegExp.OBJETO_EXPEDIENTE);
                	geoConn.insertarDatosSalida(oos, udsa, insertarExpediente, exp);
                	break;
                }
                case ConstantesRegExp.ACTION_UPDATE_IDENTIFICADORES_DIALOGO:
                {
                    ArrayList lstIdentificadoresDialogo = (ArrayList)query.getParams().get(ConstantesRegExp.OBJETO_LST_IDENTIFICADORES_DIALOGO);
                    Expediente exp = (Expediente)query.getParams().get(ConstantesRegExp.OBJETO_EXPEDIENTE);
                    geoConn.updateIdentificadoresDialogo(oos, lstIdentificadoresDialogo, exp);
                    break;
                }
                case ConstantesRegExp.ACTION_GET_IDENTIFICADOR_DIALOGO_BIEN:
                {
                	IdentificadorDialogo  identificadorDialogo = (IdentificadorDialogo)query.getParams().get(ConstantesRegExp.OBJETO_IDENTIFICADOS_DIALOGO);
                    Expediente expediente = (Expediente)query.getParams().get(ConstantesRegExp.OBJETO_EXPEDIENTE);
                    String idDialogoBien = (String)geoConn.obtenerIdentificadorDialogoExpedienteBienInmuebleBD( expediente , identificadorDialogo);
                    oos.writeObject(idDialogoBien);
                    break;
                }
                case ConstantesRegExp.ACTION_GET_IDENTIFICADOR_DIALOGO_FINCA:
                {
                	IdentificadorDialogo  identificadorDialogo = (IdentificadorDialogo)query.getParams().get(ConstantesRegExp.OBJETO_IDENTIFICADOS_DIALOGO);
                	Expediente expediente = (Expediente)query.getParams().get(ConstantesRegExp.OBJETO_EXPEDIENTE);
                	String idDialogoFinca = (String)geoConn.obtenerIdentificadorDialogoExpedienteFincaCatastroBD( expediente ,identificadorDialogo);
                	oos.writeObject(idDialogoFinca);
                    break;
                }
                case ConstantesRegExp.ACTION_GET_INFO_CATASTRAL:
                {
                	Integer idParcela = (Integer)query.getParams().get(ConstantesRegExp.STRING_IDPARCELA);
                    String convenio = (String)query.getParams().get(ConstantesRegExp.STRING_CONVENIO);
                    geoConn.completarInfoCatastral(oos,idParcela, convenio, userSesion.getIdMunicipio());
                    break;
                }
                case ConstantesRegExp.ACTION_CONSULTA_ESTADO_FINCA_OVC:
                {
                    String referencia = (String)query.getParams().get(ConstantesRegExp.STRING_REF_CATASTRAL);
                    Expediente exp = (Expediente)query.getParams().get(ConstantesRegExp.OBJETO_EXPEDIENTE);
                    boolean actualizado = geoConn.consultaEstadoFincaOVC(exp, referencia);
                    oos.writeObject(actualizado);
                    break;
                }
                case ConstantesRegExp.ACTION_CONSULTA_ESTADO_BIEN_OVC:
                {
                    String idBien = (String)query.getParams().get(ConstantesRegExp.STRING_ID_BI);
                    Expediente exp = (Expediente)query.getParams().get(ConstantesRegExp.OBJETO_EXPEDIENTE);
                    boolean actualizado = geoConn.consultaEstadoBienInmuebleOVC(exp, idBien);
                    oos.writeObject(actualizado);
                    break;
                }
                case ConstantesRegExp.ACTION_GET_ID_FINCA_CATASTRO:
                {
                    String refCat= (String)query.getParams().get(ConstantesRegExp.STRING_REF_CATASTRAL);
                    int idFinca = geoConn.getIdFincaCatastro(oos, userSesion.getIdMunicipio(), refCat);
                    oos.writeObject(idFinca);
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
        catch(SystemMapException pe)
        {
            try
            {
            	oos.writeObject(new ACException(pe));
            }
            catch(Exception ex){};
        }/** MultiPartPost */
        catch (org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException pe){
            try{
                oos.writeObject(new ACException(pe));
            }
            catch(Exception ex){};
        }
        /** java.lang.OutOfMemoryError */
        catch (java.lang.Error e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            try{
                oos.writeObject(new ACException(e));
            }
            catch(Exception ex){};
        }
        catch(Exception e) {
//            e.printStackTrace();
            log(e.getMessage());
            throw new RuntimeException(e);
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

    /**
     * Metod que crea unidadesConstructivas para la exportacion masiva de expedientes. Se le pasa el expediente del
     * objeto a exportar.
     *
     * @param exp El expediente que se va a exportar.
     * @return ArrayList Los objetos creados para se parseados a xml.
     * */
    private ArrayList crearUnidadesRegistro(Expediente exp) {
        ArrayList unidadesRegistro = new ArrayList();
        ArrayList ref= exp.getListaReferencias();

        for(int i = 0; i<ref.size();i++){
            if(ref.get(i) instanceof FincaCatastro){
                UnidadRegistroFinca uniR = new UnidadRegistroFinca();
                uniR.setCodigoDelegacion(String.valueOf(exp.getEntidadGeneradora().getCodigo()));
                uniR.setCodigoMunicipio(((FincaCatastro)ref.get(i)).getCodMunicipioDGC());
                uniR.setRefCatas1(((FincaCatastro)ref.get(i)).getRefFinca().getRefCatastral1());
                uniR.setRefCatas2(((FincaCatastro)ref.get(i)).getRefFinca().getRefCatastral2());
                unidadesRegistro.add(uniR);
            }
            else if(ref.get(i) instanceof BienInmuebleCatastro){
                UnidadRegistroBI uniR = new UnidadRegistroBI();
                uniR.setCodigoDelegacion(String.valueOf(exp.getEntidadGeneradora().getCodigo()));
                uniR.setCodigoMunicipio(((BienInmuebleCatastro)ref.get(i)).getCodMunicipioDGC());
                uniR.setRefCatas1(((BienInmuebleCatastro)ref.get(i)).getIdBienInmueble().getParcelaCatastral().getRefCatastral1());
                uniR.setRefCatas2(((BienInmuebleCatastro)ref.get(i)).getIdBienInmueble().getParcelaCatastral().getRefCatastral2());
                uniR.setCargo(((BienInmuebleCatastro)ref.get(i)).getIdBienInmueble().getNumCargo());
                uniR.setDigitoControl1(((BienInmuebleCatastro)ref.get(i)).getIdBienInmueble().getDigControl1());
                uniR.setDigitoControl2(((BienInmuebleCatastro)ref.get(i)).getIdBienInmueble().getDigControl2());
                unidadesRegistro.add(uniR);
            }
        }
        return unidadesRegistro;
    }

    private void buildVARPAD(ObjectOutputStream oos, ArrayList expedientes, String directorio, String nombreFichero, String modoTrabajo, COperacionesServerCatastroDGC geoConn, String idMunicipio ) throws Exception{

    	String datosFichero = null;
        Fichero fichero = new Fichero();
        fichero.setIdTipoFichero(Fichero.VARPAD);
        fichero.setNombre(nombreFichero);
        fichero.setDescripcion(Fichero.DESCRIPCION_VARPAD);

        CabeceraVARPAD cabecera = geoConn.creaCabeceraVARPADMasivo(oos, expedientes, modoTrabajo);
        PeticionSWCatastro.creaCabeceraVARPADMasivo(oos,cabecera, directorio, nombreFichero);
        

        if (datosFichero!=null){
        	for(int i= 0;i<expedientes.size();i++) {

        		Expediente exp =(Expediente)expedientes.get(i);
        		ArrayList xmlBIExp = geoConn.getXmlParcelasFX_CCExp(oos, exp);

        		if(((ArrayList)xmlBIExp.get(0)).size()==0 || exp.getIdEstado() != ConstantesRegExp.ESTADO_FINALIZADO)
        			xmlBIExp = crearUnidadesRegistro((Expediente)expedientes.get(i));

      		PeticionSWCatastro.crearCuerpoVARPADRequest(oos, exp, xmlBIExp, directorio, nombreFichero);
        	
        	}
        }

        if (datosFichero!=null){
        	datosFichero = datosFichero + PeticionSWCatastro.crearCierreVARPADRequest();
        }
        
        oos.writeObject(datosFichero);

        ArrayList listaNumExp = new ArrayList();
        for(int i = 0; i<expedientes.size();i++){
            listaNumExp.add(((Expediente)expedientes.get(i)).getNumeroExpediente());
        }

        fichero.setFechaGeneracion(new Date(System.currentTimeMillis()));
        fichero.setFechaIntercambio(new Date(System.currentTimeMillis()));
        fichero.setUrl(directorio + File.separator + nombreFichero);
        fichero.setFechaInicioPeriodo(cabecera.getFechaInicioPeriodo());
        fichero.setFechaFinPeriodo(cabecera.getFechaFinalizacionPeriodo());
        fichero.setCodigoEntidadDestinataria(EntidadGeneradora.CODIGO_CATASTRO);
        fichero.setCodigoEntidadGeneradora(((Expediente)expedientes.get(0)).getEntidadGeneradora().getCodigo());

        geoConn.crearFichero(oos, listaNumExp, fichero, idMunicipio);
        geoConn.actualizaExpFinalizadosDespuesExportacion(oos,expedientes, modoTrabajo);

    }
    
    private void buildVARPADRemote(ObjectOutputStream oos, ArrayList expedientes, String directorio, String nombreFichero, String modoTrabajo, COperacionesServerCatastroDGC geoConn, String idMunicipio, FicheroExportacion ficheroExportacion) throws Exception{

    	StringBuffer datosFichero = new StringBuffer();
        Fichero fichero = new Fichero();
        fichero.setIdTipoFichero(Fichero.VARPAD);
        fichero.setNombre(nombreFichero);
        fichero.setDescripcion(Fichero.DESCRIPCION_VARPAD);

        CabeceraVARPAD cabecera = geoConn.creaCabeceraVARPADMasivo(oos, expedientes, modoTrabajo);
        datosFichero.append(PeticionSWCatastro.creaCabeceraVARPADMasivo(oos,cabecera));


        for(int i= 0;i<expedientes.size();i++) {

        	Expediente exp =(Expediente)expedientes.get(i);
        	ArrayList xmlBIExp = geoConn.getXmlParcelasFX_CCExp(oos, exp);

        	if(((ArrayList)xmlBIExp.get(0)).size()==0 || exp.getIdEstado() != ConstantesRegExp.ESTADO_FINALIZADO)
        		xmlBIExp = crearUnidadesRegistro((Expediente)expedientes.get(i));

        	datosFichero.append(PeticionSWCatastro.crearCuerpoVARPADRequest(oos, exp, xmlBIExp));
        }

        datosFichero.append(PeticionSWCatastro.crearCierreVARPADRequest());
        ficheroExportacion.setFicheroVarpad(datosFichero.toString());


        ArrayList listaNumExp = new ArrayList();
        for(int i = 0; i<expedientes.size();i++){
            listaNumExp.add(((Expediente)expedientes.get(i)).getNumeroExpediente());
        }

        fichero.setFechaGeneracion(new Date(System.currentTimeMillis()));
        fichero.setFechaIntercambio(new Date(System.currentTimeMillis()));
        fichero.setUrl(directorio + File.separator + nombreFichero);
        fichero.setFechaInicioPeriodo(cabecera.getFechaInicioPeriodo());
        fichero.setFechaFinPeriodo(cabecera.getFechaFinalizacionPeriodo());
        fichero.setCodigoEntidadDestinataria(EntidadGeneradora.CODIGO_CATASTRO);
        fichero.setCodigoEntidadGeneradora(((Expediente)expedientes.get(0)).getEntidadGeneradora().getCodigo());

        geoConn.crearFichero(oos, listaNumExp, fichero, idMunicipio);
        geoConn.actualizaExpFinalizadosDespuesExportacion(oos,expedientes, modoTrabajo);

    }

    private void buildFINEntrda(ObjectOutputStream oos, ArrayList expedientes, String directorio, String nombreFichero, String modoTrabajo, COperacionesServerCatastroDGC geoConn, String idMunicipio ) throws Exception{

        Fichero fichero = new Fichero();
        fichero.setIdTipoFichero(Fichero.FIN_ENTRADA);
        fichero.setNombre(nombreFichero);
        fichero.setDescripcion(Fichero.DESCRIPCION_FIN_ENTRDA);

        CabeceraFinEntrada cabecera = geoConn.creaCabeceraFinEntradaMasivo(oos, expedientes, modoTrabajo);
        PeticionSWCatastro.creaCabeceraFinEntradaMasivo(oos,cabecera, directorio, nombreFichero);

        for(int i= 0;i<expedientes.size();i++) {

            Expediente exp = (Expediente)expedientes.get(i);
            ArrayList xmlParcelasExp = geoConn.getXmlParcelasFX_CCExp(oos, exp);

            if(((ArrayList)xmlParcelasExp.get(0)).size()==0 || exp.getIdEstado() != ConstantesRegExp.ESTADO_FINALIZADO)
                xmlParcelasExp = crearUnidadesRegistro((Expediente)expedientes.get(i));

            PeticionSWCatastro.crearCuerpoFinEntradaRequest(oos, exp, xmlParcelasExp, directorio, nombreFichero);
        }

        PeticionSWCatastro.crearCierreFinEntradaRequest(oos,directorio, nombreFichero);

        ArrayList listaNumExp = new ArrayList();
        for(int i = 0; i<expedientes.size();i++){
            listaNumExp.add(((Expediente)expedientes.get(i)).getNumeroExpediente());
        }

        fichero.setFechaGeneracion(new Date(System.currentTimeMillis()));
        fichero.setFechaIntercambio(new Date(System.currentTimeMillis()));
        fichero.setUrl(directorio + File.separator + nombreFichero);
        fichero.setFechaInicioPeriodo(cabecera.getFechaInicioPeriodo());
        fichero.setFechaFinPeriodo(cabecera.getFechaFinalizacionPeriodo());
        fichero.setCodigoEntidadDestinataria(EntidadGeneradora.CODIGO_CATASTRO);
        fichero.setCodigoEntidadGeneradora(((Expediente)expedientes.get(0)).getEntidadGeneradora().getCodigo());

        geoConn.crearFichero(oos, listaNumExp, fichero, idMunicipio);
        geoConn.actualizaExpFinalizadosDespuesExportacion(oos,expedientes, modoTrabajo);
    }
    
    private void buildFINEntradaRemote(ObjectOutputStream oos, ArrayList expedientes, String directorio, String nombreFichero, String modoTrabajo, COperacionesServerCatastroDGC geoConn, String idMunicipio, FicheroExportacion ficheroExportacion ) throws Exception{

    	StringBuffer datosFichero = new StringBuffer();
        Fichero fichero = new Fichero();
        fichero.setIdTipoFichero(Fichero.FIN_ENTRADA);
        fichero.setNombre(nombreFichero);
        fichero.setDescripcion(Fichero.DESCRIPCION_FIN_ENTRDA);

        CabeceraFinEntrada cabecera = geoConn.creaCabeceraFinEntradaMasivo(oos, expedientes, modoTrabajo);
        datosFichero.append(PeticionSWCatastro.creaCabeceraFinEntradaMasivo(oos,cabecera));

        ArrayList lstFxccExp = new ArrayList();
        
        for(int i= 0;i<expedientes.size();i++) {

            Expediente exp = (Expediente)expedientes.get(i);
            ArrayList xmlParcelasExp = geoConn.getXmlParcelasFX_CCExp(oos, exp);

            if(((ArrayList)xmlParcelasExp.get(0)).size()==0 || exp.getIdEstado() != ConstantesRegExp.ESTADO_FINALIZADO)
                xmlParcelasExp = crearUnidadesRegistro((Expediente)expedientes.get(i));

            datosFichero.append(PeticionSWCatastro.crearCuerpoFinEntradaRequest(oos, exp, xmlParcelasExp));
            
            ArrayList lstTemp = geoConn.getFxccParcelasExp(oos, exp);
            if (lstTemp != null && lstTemp.size()>0){
            	lstFxccExp.add(lstTemp);
            }
            
        }
        
        ficheroExportacion.setLstFxcc(lstFxccExp);

        datosFichero.append(PeticionSWCatastro.crearCierreFinEntradaRequest());
        ficheroExportacion.setFicheroFinEntrada(datosFichero.toString());

        ArrayList listaNumExp = new ArrayList();
        for(int i = 0; i<expedientes.size();i++){
            listaNumExp.add(((Expediente)expedientes.get(i)).getNumeroExpediente());
        }

        fichero.setFechaGeneracion(new Date(System.currentTimeMillis()));
        fichero.setFechaIntercambio(new Date(System.currentTimeMillis()));
        fichero.setUrl(directorio + File.separator + nombreFichero);
        fichero.setFechaInicioPeriodo(cabecera.getFechaInicioPeriodo());
        fichero.setFechaFinPeriodo(cabecera.getFechaFinalizacionPeriodo());
        fichero.setCodigoEntidadDestinataria(EntidadGeneradora.CODIGO_CATASTRO);
        fichero.setCodigoEntidadGeneradora(((Expediente)expedientes.get(0)).getEntidadGeneradora().getCodigo());

        geoConn.crearFichero(oos, listaNumExp, fichero, idMunicipio);
        geoConn.actualizaExpFinalizadosDespuesExportacion(oos,expedientes, modoTrabajo);
    }
}
