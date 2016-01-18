package com.geopista.protocol.metadatos;

import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.security.SecurityManager;
import com.geopista.app.AppContext;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.Vector;

import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.Marshaller;

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
 * Date: 23-jul-2004
 * Time: 10:30:17
 */
public class OperacionesMetadatos {
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(OperacionesMetadatos.class);
    private String url;
    private final String servletNameGetContactos="/GetContactos";
    private final String servletNameNuevoContacto="/NewContact";
    private final String servletNameActualizarContacto="/UpdateContact";
    private final String servletNameEliminarContacto="/DeleteContact";
    private final String servletNameSaveMetadato="/SaveMetadata";
    private final String servletNameMetadataParcial="/GetMetadataParcial";
    private final String servletNameGetMetadata="/GetMetadata";
    private final String servletNameDeleteMetadata="/DeleteMetadata";
    private final String servletNameGetCapas="/GetLayers";
    private final String servletNameUpdateAll="/UpdateAll";

    public OperacionesMetadatos(String sUrl)
    {
        url=sUrl;
        ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY);
        SecurityManager.setIdMunicipio(iSesion.getIdMunicipio());
    }
	public ListaContactos getContactos() throws Exception
    {
          try {

                  StringReader sr=EnviarSeguro.enviarPlano(url+servletNameGetContactos,"");

                   CResultadoOperacion resultado=(CResultadoOperacion)Unmarshaller.unmarshal(CResultadoOperacion.class,sr);
                   if (resultado.getResultado())
                   {
                        ListaContactos listaContactos = new ListaContactos();
                        listaContactos.set(resultado.getVector());
                        return listaContactos;
                   }else
                   {
                      logger.error("Error al obtener la lista de contactos:"+ resultado.getDescripcion());
                       return null;
                   }
               }
               catch (Exception ex) {
                   StringWriter sw = new StringWriter();
                   PrintWriter pw = new PrintWriter(sw);
                   ex.printStackTrace(pw);
                   logger.error("Exception al obtener la lista de contactos: " + sw.toString());
                   throw ex;
               }
    }
    public CResultadoOperacion eliminarContacto(CI_ResponsibleParty contacto) throws Exception
   {
        try {
               /*
               StringWriter sw = new StringWriter();
               Marshaller.marshal(contacto, sw);
               */
               StringWriter sw = new StringWriter();
               Marshaller marshaller = new Marshaller(sw);
               marshaller.setEncoding("ISO-8859-1");
               marshaller.marshal(contacto);

               CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameEliminarContacto, sw.toString() );
               return resultado;
        }
        catch (Exception ex) {
           StringWriter sw = new StringWriter();
           PrintWriter pw = new PrintWriter(sw);
           ex.printStackTrace(pw);
           logger.error("Exception al eliminar el contacto: " + sw.toString());
           throw ex;
       }
   }

    public CResultadoOperacion nuevoContacto(CI_ResponsibleParty contacto) throws Exception
           {
                try {
                   /*
                   StringWriter sw = new StringWriter();
                   Marshaller.marshal(contacto, sw);
                   */
                    StringWriter sw = new StringWriter();
                    Marshaller marshaller = new Marshaller(sw);
                    marshaller.setEncoding("ISO-8859-1");
                    marshaller.marshal(contacto);

                   CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameNuevoContacto, sw.toString() );
                   if (resultado.getResultado())
                       contacto.setId(resultado.getDescripcion());
                   return resultado;
               }
               catch (Exception ex) {
                   StringWriter sw = new StringWriter();
                   PrintWriter pw = new PrintWriter(sw);
                   ex.printStackTrace(pw);
                   logger.error("Exception al crear el contacto: " + sw.toString());
                   throw ex;
               }
           }

     public CResultadoOperacion actualizarContacto(CI_ResponsibleParty contacto) throws Exception
     {
           try {
                   /*
                   StringWriter sw = new StringWriter();
                   Marshaller.marshal(contacto, sw);
                   */
                   StringWriter sw = new StringWriter();
                   Marshaller marshaller = new Marshaller(sw);
                   marshaller.setEncoding("ISO-8859-1");
                   marshaller.marshal(contacto);

                   CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameActualizarContacto, sw.toString() );
                   return resultado;
               }
               catch (Exception ex) {
                   StringWriter sw = new StringWriter();
                   PrintWriter pw = new PrintWriter(sw);
                   ex.printStackTrace(pw);
                   logger.error("Exception al actualizar el contacto: " + sw.toString());
                   throw ex;
               }
     }
     public CResultadoOperacion salvarMetadato(MD_Metadata metadato) throws Exception
     {
            try {
               /*
               StringWriter sw = new StringWriter();
               Marshaller.marshal(metadato, sw);
               */
                StringWriter sw = new StringWriter();
                Marshaller marshaller = new Marshaller(sw);
                marshaller.setEncoding("ISO-8859-1");
                marshaller.marshal(metadato);

               CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameSaveMetadato, sw.toString() );
               return resultado;
           }
           catch (Exception ex) {
               StringWriter sw = new StringWriter();
               PrintWriter pw = new PrintWriter(sw);
               ex.printStackTrace(pw);
               logger.error("Exception al crear el metadato: " + sw.toString());
               throw ex;
           }
     }
     public Vector getMetadatosParcial(PeticionBusqueda peticion) throws Exception
     {
           try {
                       if (peticion==null) return null;
                       /*
                       StringWriter sw = new StringWriter();
                       Marshaller.marshal(peticion, sw);
                       */
                       StringWriter sw = new StringWriter();
                       Marshaller marshaller = new Marshaller(sw);
                       marshaller.setEncoding("ISO-8859-1");
                       marshaller.marshal(peticion);

                       StringReader sr=EnviarSeguro.enviarPlano(url+servletNameMetadataParcial,sw.toString());
                       CResultadoOperacion resultado=(CResultadoOperacion)Unmarshaller.unmarshal(CResultadoOperacion.class,sr);
                       if (resultado.getResultado())
                       {
                            return resultado.getVector();
                       }else
                       {
                           logger.error("Error al obtener la lista parcial metadatos:"+ resultado.getDescripcion());
                           return null;
                       }
                   }
                   catch (Exception ex) {
                       StringWriter sw = new StringWriter();
                       PrintWriter pw = new PrintWriter(sw);
                       ex.printStackTrace(pw);
                       logger.error("Exception al obtener la lista parcial metadatos: " + sw.toString());
                       throw ex;
                   }

        }
        public CResultadoOperacion getMetadato(String sFileIdentifier) throws Exception
        {
               try {


                   CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameGetMetadata, sFileIdentifier );

                   return resultado;
              }
              catch (Exception ex) {
                  StringWriter sw = new StringWriter();
                  PrintWriter pw = new PrintWriter(sw);
                  ex.printStackTrace(pw);
                  logger.error("Exception al crear el metadato: " + sw.toString());
                  throw ex;
              }
        }
        public CResultadoOperacion deleteMetadata(String sFileIdentifier) throws Exception
        {
               try {

                   CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameDeleteMetadata, sFileIdentifier );

                  return resultado;
              }
              catch (Exception ex) {
                  StringWriter sw = new StringWriter();
                  PrintWriter pw = new PrintWriter(sw);
                  ex.printStackTrace(pw);
                  logger.error("Exception al crear el metadato: " + sw.toString());
                  throw ex;
              }
        }

        public CResultadoOperacion updateAll(String sCampo, String sValor) throws Exception
        {
               try {
                  Vector pasar = new Vector();
                  pasar.add(sCampo);
                  pasar.add(sValor);
                  /*
                  StringWriter sw = new StringWriter();
                  Marshaller.marshal(pasar, sw);
                  */
                   StringWriter sw = new StringWriter();
                   Marshaller marshaller = new Marshaller(sw);
                   marshaller.setEncoding("ISO-8859-1");
                   marshaller.marshal(pasar);

                  CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameUpdateAll,sw.toString() );
                  return resultado;
              }
              catch (Exception ex) {
                  StringWriter sw = new StringWriter();
                  PrintWriter pw = new PrintWriter(sw);
                  ex.printStackTrace(pw);
                  logger.error("Exception al crear el metadato: " + sw.toString());
                  throw ex;
              }
        }

        public CResultadoOperacion getCapas() throws Exception
        {
               try {

                   CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameGetCapas,"");

                  return resultado;
              }
              catch (Exception ex) {
                  StringWriter sw = new StringWriter();
                  PrintWriter pw = new PrintWriter(sw);
                  ex.printStackTrace(pw);
                  logger.error("Exception al pedir: " + sw.toString());
                  throw ex;
              }
        }



}