package com.geopista.server.contaminantes.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.eclipse.jetty.plus.jaas.JAASPrincipal;
import org.exolab.castor.xml.Unmarshaller;

import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.contaminantes.CAnexo;
import com.geopista.protocol.contaminantes.Contaminante;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.COperacionesContaminantes;

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
 * Date: 25-oct-2004
 * Time: 17:57:39
 */
public class SaveActividad extends LoggerHttpServlet {
       private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SaveActividad.class);
       public void doPost (HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException
            {
    	   		super.doPost(request);
               CResultadoOperacion resultado;
               PrintWriter out = response.getWriter();

               try
               {
                    JAASPrincipal userPrincipal = (JAASPrincipal)request.getUserPrincipal();
                    if (userPrincipal==null)
                    {
                        resultado=new CResultadoOperacion(false,"El usuario no ha sido autenticado por JAAS");
                    }
                    else
                    {

                        /* MultipartPostMethod */
                        String stream=null;
                        /** Recogemos los nuevos ficheros annadidos */
                        Hashtable fileUploads=new Hashtable();
                        // Create a new file upload handler
                        DiskFileUpload upload = new DiskFileUpload();

                        /** Set upload parameters */
                        upload.setSizeThreshold(CConstantesComando.MaxMemorySize);
                        upload.setSizeMax(CConstantesComando.MaxRequestSize);
                        /*
                        String yourTempDirectory= "anexos" + File.separator;
                        upload.setRepositoryPath(yourTempDirectory);
                        */

                        // Parse the request
                        try{
                            List items = upload.parseRequest(request);
                            // Process the uploaded items
                            Iterator iter = items.iterator();

                            while (iter.hasNext()) {
                                FileItem item = (FileItem) iter.next();

                                String fieldName = item.getFieldName();
                                if (item.isFormField()) {
                                   if (fieldName.equalsIgnoreCase(EnviarSeguro.mensajeXML)){
                                      stream  = item.getString("ISO-8859-1");
                                   }
                                } else {
                                    CAnexo anexo= new CAnexo();
                                    /** Debido a que el nombre del fichero puede contener acentos. */
                                    fieldName=URLDecoder.decode(fieldName,"ISO-8859-1");
                                    anexo.setFileName(fieldName);
                                    anexo.setContent(item.get());
                                    fileUploads.put(fieldName,anexo);
                                }
                            }

                            Contaminante actividad =(Contaminante)Unmarshaller.unmarshal(Contaminante.class,new StringReader(stream));
                            resultado=COperacionesContaminantes.actualizarActividad(actividad, fileUploads);

                        }catch (FileUploadBase.SizeLimitExceededException ex){
                            resultado= new CResultadoOperacion(false, "FileUploadBase.SizeLimitExceededException");                            
                            System.out.println("[SaveActividad.doPost]...FileUploadBase.SizeLimitExceededException");
                            logger.warn("************************* FileUploadBase.SizeLimitExceededException");
                        }
                    }
               }catch(Exception e)
               {
                   java.io.StringWriter sw=new java.io.StringWriter();
                   java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                   e.printStackTrace(pw);
                   logger.error("Excepción al actualizar la actividad:"+sw.toString());
                   resultado=new CResultadoOperacion(false, "Excepción al actualizar la actividad:"+e.toString());
               }
                Writer writer = response.getWriter();
                writer.write (resultado.buildResponse());
                writer.flush();
                writer.close();
              }
    }

