/**
 * COperacionesMetadatos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.control.SesionUtils_LCGIII;
import com.geopista.protocol.metadatos.CI_Citation;
import com.geopista.protocol.metadatos.CI_Date;
import com.geopista.protocol.metadatos.CI_OnLineResource;
import com.geopista.protocol.metadatos.CI_ResponsibleParty;
import com.geopista.protocol.metadatos.Capa;
import com.geopista.protocol.metadatos.DQ_DataQuality;
import com.geopista.protocol.metadatos.DQ_Element;
import com.geopista.protocol.metadatos.EX_Extent;
import com.geopista.protocol.metadatos.EX_GeographicBoundingBox;
import com.geopista.protocol.metadatos.EX_VerticalExtent;
import com.geopista.protocol.metadatos.LI_Linage;
import com.geopista.protocol.metadatos.MD_DataIdentification;
import com.geopista.protocol.metadatos.MD_Format;
import com.geopista.protocol.metadatos.MD_LegalConstraint;
import com.geopista.protocol.metadatos.MD_Metadata;
import com.geopista.protocol.metadatos.MD_MetadataMini;
import com.geopista.protocol.metadatos.PeticionBusqueda;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.server.metadatos.db.CI_CitationDB;
import com.geopista.server.metadatos.db.CI_OnLineResourceDB;
import com.geopista.server.metadatos.db.CI_ResponsiblePartyDB;
import com.geopista.server.metadatos.db.DQ_DataQualityDB;
import com.geopista.server.metadatos.db.EX_ExtentDB;
import com.geopista.server.metadatos.db.MD_DataIdentificationDB;
import com.geopista.server.metadatos.db.MD_FormatDB;
import com.geopista.server.metadatos.db.MD_LegalConstraintDB;
import com.geopista.server.metadatos.db.MD_MetadataDB;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 23-jul-2004
 * Time: 12:17:24
 */
public class COperacionesMetadatos {
        private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(COperacionesMetadatos.class);

        public static CResultadoOperacion ejecutarListaContactos()
        {
             return ejecutarListaContactos(null);
        }

        public static CResultadoOperacion ejecutarListaContactos(String sResponsiblePartyId)
        {
           Connection connection = null;
           PreparedStatement preparedStatement = null;
           ResultSet rsSQL=null;
           CResultadoOperacion resultado;
           try
           {
               logger.debug("Inicio de listar los contactos");
               connection = CPoolDatabase.getConnection();
               if (connection == null)
               {
                      logger.warn("No se puede obtener la conexión");
                       return new CResultadoOperacion(false, "No se puede obtener la conexión");
               }
               try{connection.setAutoCommit(false);}catch(Exception e){};
               if(sResponsiblePartyId==null) //Se obtine la lista total
                    preparedStatement = connection.prepareStatement(CI_ResponsiblePartyDB.getSelect(connection));
               else
                    preparedStatement = connection.prepareStatement(
                                            CI_ResponsiblePartyDB.getSelect(sResponsiblePartyId, connection));
               rsSQL=preparedStatement.executeQuery();

               Vector auxVector = new Vector();
               while (rsSQL.next())
               {
                     CI_ResponsibleParty auxResponsible=(CI_ResponsibleParty) CI_ResponsiblePartyDB.load(rsSQL, connection);
                     auxVector.add(auxResponsible);
               }

               for (Enumeration e=auxVector.elements();e.hasMoreElements();)
               {
                   try
                   {
                        CI_ResponsibleParty auxResponsible=(CI_ResponsibleParty)e.nextElement();
                        //DeliveryPoint
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getSelectDeliveryPoint());
                        preparedStatement.setString(1,auxResponsible.getId());
                        rsSQL=preparedStatement.executeQuery();
                        auxResponsible.setDeliveryPoint(cargarLista(rsSQL));

                        //Fax
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getSelectFax());
                        preparedStatement.setString(1,auxResponsible.getId());
                        rsSQL=preparedStatement.executeQuery();
                        auxResponsible.setCi_telephone_facsimile(cargarLista(rsSQL));

                       //Mail
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getSelectMails());
                        preparedStatement.setString(1,auxResponsible.getId());
                        rsSQL=preparedStatement.executeQuery();
                        auxResponsible.setElectronicMailAdress(cargarLista(rsSQL));

                        //Telephone
                         preparedStatement.close();
                         preparedStatement = null;
                         preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getSelectTelefonos());
                         preparedStatement.setString(1,auxResponsible.getId());
                         rsSQL=preparedStatement.executeQuery();
                         auxResponsible.setCi_telephone_voice(cargarLista(rsSQL));


                   }catch (Exception ex)
                   {
                        logger.error("Error al optener los datos adicionales del contacto");
                   }

               }
               resultado=new CResultadoOperacion(true, "Operación ejecutada correctamente");
               resultado.setVector(auxVector);

         }catch(SQLException e)
         {
              java.io.StringWriter sw=new java.io.StringWriter();
              java.io.PrintWriter pw=new java.io.PrintWriter(sw);
              e.printStackTrace(pw);
              logger.error("ERROR al CREAR el Domain Node:"+sw.toString());
              resultado=new CResultadoOperacion(false, e.getMessage());
              try {connection.rollback();} catch (Exception ex2) {}
         }finally{
                   try{rsSQL.close();} catch (Exception ex2) {}
                   try{preparedStatement.close();} catch (Exception ex2) {}
                   try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
               }
              return resultado;
          }
    private static Vector cargarLista(ResultSet rs)
    {
        try
        {
            Vector auxVector= new Vector();
            while (rs.next())
            {
                auxVector.add(rs.getString(1));
            }
            return auxVector;
        }catch(Exception e)
        {
            logger.error("Error al cargar la lista"+e.toString());
        }
        return null;
    }
    public static CResultadoOperacion ejecutarDeleteContact(CI_ResponsibleParty contact)
    {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet rsSQL=null;
            CResultadoOperacion resultado;
            try
            {
                logger.debug("Inicio de borrar el contacto:"+contact.getIndividualName()+"["+contact.getOrganisationName()+"]");
                connection = CPoolDatabase.getConnection();
                if (connection == null)
                {
                       logger.warn("No se puede obtener la conexión");
                       return new CResultadoOperacion(false, "No se puede obtener la conexión");
                }
                try{connection.setAutoCommit(false);}catch(Exception e){};
                //Primero hay que mirar si pertenece a un metadata

                //Borramos los mails
                logger.debug("Borrando mails usuario: "+contact.getId());
                preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getDeleteMails());
                preparedStatement.setString(1,contact.getId());
                preparedStatement.execute();

                //Borramos los delivery point
                logger.debug("Borrando delivery point de usuario: "+contact.getId());
                preparedStatement.close();
                preparedStatement = null;
                preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getDeletePoints());
                preparedStatement.setString(1,contact.getId());
                preparedStatement.execute();

                //Borramos los delivery point
                logger.debug("Borrando telefonos de usuario: "+contact.getId());
                preparedStatement.close();
                preparedStatement = null;
                preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getDeleteTelephones());
                preparedStatement.setString(1,contact.getId());
                preparedStatement.execute();

                //Borramos los fax
                logger.debug("Borrando fax de usuario: "+contact.getId());
                preparedStatement.close();
                preparedStatement = null;
                preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getDeleteFaxs());
                preparedStatement.setString(1,contact.getId());
                preparedStatement.execute();


                //Borramos el contacto
                preparedStatement.close();
                preparedStatement = null;
                preparedStatement = connection.prepareStatement(CI_ResponsiblePartyDB.getDelete() );
                preparedStatement.setString(1,contact.getId());
                preparedStatement.execute();

                //Borramos los recursos
                if (contact.getOnLineResource()!=null && contact.getOnLineResource().getId()!=null)
                {
                    logger.debug("Borramos el recurso en linea:"+contact.getOnLineResource().getLinkage());
                    preparedStatement.close();
                    preparedStatement = null;
                    preparedStatement=connection.prepareStatement(CI_OnLineResourceDB.getDelete());
                    preparedStatement.setString(1,contact.getOnLineResource().getId());
                    preparedStatement.execute();
                }

                //connection.commit();
                logger.debug("Contacto "+contact.getId()+" con name: "+contact.getIndividualName()+" ["+contact.getOrganisationName()+"]");
                resultado=new CResultadoOperacion(true, "Contacto borrado correctamente");

          }catch(Exception e)
          {
               java.io.StringWriter sw=new java.io.StringWriter();
               java.io.PrintWriter pw=new java.io.PrintWriter(sw);
               e.printStackTrace(pw);
               logger.error("ERROR al borrar el contacto:"+sw.toString());
               String error="";
               if (e.toString().toUpperCase().indexOf("FOREIGN")>=0)
                  error="El contacto esta siendo utilizado en el sistema. No se puede borrar";
               else
                  error=e.getMessage();
               resultado=new CResultadoOperacion(false, error);
               try {connection.rollback();} catch (Exception ex2) {}
          }finally{
                    try{rsSQL.close();} catch (Exception ex2) {}
                    try{preparedStatement.close();} catch (Exception ex2) {}
                    try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
                }
               return resultado;
           }


     public static CResultadoOperacion ejecutarNewContact(CI_ResponsibleParty contact, String sIdMunicipio)
       {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet rsSQL=null;
            CResultadoOperacion resultado;
            try
            {
                logger.debug("Inicio añadir contacto:"+contact.getIndividualName()+"["+contact.getOrganisationName()+"]");
                connection = CPoolDatabase.getConnection();
                if (connection == null)
                {
                       logger.warn("No se puede obtener la conexión");
                        return new CResultadoOperacion(false, "No se puede obtener la conexión");
                }
                try{connection.setAutoCommit(false);}catch(Exception e){};

                //Insertamos el recurso
                if (contact.getOnLineResource()!=null && contact.getOnLineResource().getLinkage()!=null && contact.getOnLineResource().getLinkage().length()>0)
                {
                       long lIdOnLine =CPoolDatabase.getNextValue("CI_ONLINERESOURCE","ONLINERESOURCE_ID");
                       logger.debug("añadimos recursos en linea:"+contact.getOnLineResource().getLinkage());
                       logger.debug("El function code que nos llega es:"+contact.getOnLineResource().getIdOnLineFunctionCode());
                       preparedStatement=connection.prepareStatement(CI_OnLineResourceDB.getInsert());
                       contact.getOnLineResource().setId(new Long(lIdOnLine).toString());
                       CI_OnLineResourceDB.save(contact.getOnLineResource(),preparedStatement);
                       preparedStatement.execute();
                }

                //Insertamos el contacto
                long lIdContacto=CPoolDatabase.getNextValue("CI_RESPONSIBLEPARTY","RESPONSIBLEPARTY_ID");
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                preparedStatement = connection.prepareStatement(CI_ResponsiblePartyDB.getInsert(connection) );
                contact.setId(new Long(lIdContacto).toString());
                CI_ResponsiblePartyDB.save(contact,sIdMunicipio,preparedStatement);
                preparedStatement.execute();

                //Borramos los mails
                logger.debug("Borrando mails usuario: "+contact.getId());
                preparedStatement.close();
                preparedStatement = null;
                preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getDeleteMails());
                preparedStatement.setString(1,contact.getId());
                preparedStatement.execute();

                //Borramos los delivery point
                logger.debug("Borrando delivery point de usuario: "+contact.getId());
                preparedStatement.close();
                preparedStatement = null;
                preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getDeletePoints());
                preparedStatement.setString(1,contact.getId());
                preparedStatement.execute();

                //Borramos los delivery point
                logger.debug("Borrando telefonos de usuario: "+contact.getId());
                preparedStatement.close();
                preparedStatement = null;
                preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getDeleteTelephones());
                preparedStatement.setString(1,contact.getId());
                preparedStatement.execute();

                //Borramos los fax
                logger.debug("Borrando fax de usuario: "+contact.getId());
                preparedStatement.close();
                preparedStatement = null;
                preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getDeleteFaxs());
                preparedStatement.setString(1,contact.getId());
                preparedStatement.execute();


                //Insertamos los mails
                if (contact.getElectronicMailAdress()!=null && contact.getElectronicMailAdress().size()>0)
                {
                    logger.debug("añadimos mails");
                    for (Enumeration e=contact.getElectronicMailAdress().elements();e.hasMoreElements();)
                    {
                        long lIdMail =CPoolDatabase.getNextValue("ELECTRONICMAILADDRESS","ELECTRONICMAILADDRESS_ID");
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getInsertMails());
                        CI_ResponsiblePartyDB.saveDetalle(new Long(lIdMail).toString(),contact.getId(),(String)e.nextElement(),preparedStatement);
                        preparedStatement.execute();
                    }
                 }

                //Insertamos points
                if (contact.getDeliveryPoint()!=null && contact.getDeliveryPoint().size()>0)
                {
                    logger.debug("añadimos points");
                    for (Enumeration e=contact.getDeliveryPoint().elements();e.hasMoreElements();)
                    {
                        long lIdPoint =CPoolDatabase.getNextValue("DELIVERYPOINT","DELIVERYPOINT_ID");
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getInsertPoints());
                        CI_ResponsiblePartyDB.saveDetalle(new Long(lIdPoint).toString(),contact.getId(),(String)e.nextElement(),preparedStatement);
                        preparedStatement.execute();
                    }
                 }

                //Insertamos Telephonos
                if (contact.getCi_telephone_voice()!=null && contact.getCi_telephone_voice().size()>0)
                {
                    logger.debug("añadimos Telefonos");
                    for (Enumeration e=contact.getCi_telephone_voice().elements();e.hasMoreElements();)
                    {
                        long lIdTelephone =CPoolDatabase.getNextValue("CI_TELEPHONE_VOICE","TELEPHONE_VOICE_ID");
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getInsertTelephones());
                        CI_ResponsiblePartyDB.saveDetalle(new Long(lIdTelephone).toString(),contact.getId(),(String)e.nextElement(),preparedStatement);
                        preparedStatement.execute();
                    }
                 }

                //Insertamos faxes
                 if (contact.getCi_telephone_facsimile()!=null && contact.getCi_telephone_facsimile().size()>0)
                 {
                     logger.debug("añadimos Faxs");
                     for (Enumeration e=contact.getCi_telephone_facsimile().elements();e.hasMoreElements();)
                     {
                         long lIdFax =CPoolDatabase.getNextValue("CI_TELEPHONE_FACSIMILE","TELEPHONE_FACSIMILE_ID");
                         preparedStatement.close();
                         preparedStatement = null;
                         preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getInsertFaxs());
                         CI_ResponsiblePartyDB.saveDetalle(new Long(lIdFax).toString(),contact.getId(),(String)e.nextElement(),preparedStatement);
                         preparedStatement.execute();
                     }
                  }

                //connection.commit();
                logger.debug("Contacto "+contact.getId()+" con name: "+contact.getIndividualName()+" ["+contact.getOrganisationName()+"]");
                resultado=new CResultadoOperacion(true, new Long(lIdContacto).toString());

          }catch(Exception e)
          {
               java.io.StringWriter sw=new java.io.StringWriter();
               java.io.PrintWriter pw=new java.io.PrintWriter(sw);
               e.printStackTrace(pw);
               logger.error("ERROR al CREAR el contacto:"+sw.toString());
               resultado=new CResultadoOperacion(false, e.getMessage());
               try {connection.rollback();} catch (Exception ex2) {}
          }finally{
                    try{rsSQL.close();} catch (Exception ex2) {}
                    try{preparedStatement.close();} catch (Exception ex2) {}
                    try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
                }
               return resultado;
           }

    public static CResultadoOperacion ejecutarUpdateContact(CI_ResponsibleParty contact)
       {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet rsSQL=null;
            CResultadoOperacion resultado;
            try
            {
                logger.debug("Inicio actualizar contacto:"+contact.getIndividualName()+"["+contact.getOrganisationName()+"]");
                connection = CPoolDatabase.getConnection();
                if (connection == null)
                {
                       logger.warn("No se puede obtener la conexión");
                        return new CResultadoOperacion(false, "No se puede obtener la conexión");
                }
                try{connection.setAutoCommit(false);}catch(Exception e){};

                //Insertamos el recurso
                if (contact.getOnLineResource()!=null && contact.getOnLineResource().getLinkage()!=null && contact.getOnLineResource().getLinkage().length()>0)
                {
                       if (contact.getOnLineResource().getId()==null || contact.getOnLineResource().getId().length()==0)
                       {
                            //Creamos un nuevo recurso
                            long lIdOnLine =CPoolDatabase.getNextValue("CI_ONLINERESOURCE","ONLINERESOURCE_ID");
                            logger.debug("añadimos recursos en linea:"+contact.getOnLineResource().getLinkage());
                            logger.debug("El function code que nos llega es:"+contact.getOnLineResource().getIdOnLineFunctionCode());
                            preparedStatement=connection.prepareStatement(CI_OnLineResourceDB.getInsert());
                            contact.getOnLineResource().setId(new Long(lIdOnLine).toString());
                            CI_OnLineResourceDB.save(contact.getOnLineResource(),preparedStatement);
                       }
                       else
                       {
                            preparedStatement=connection.prepareStatement(CI_OnLineResourceDB.getUpdate());
                            CI_OnLineResourceDB.saveUpdate(contact.getOnLineResource(),preparedStatement);
                       }
                       preparedStatement.execute();
                }

                //Actualizamos el contacto
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                preparedStatement = connection.prepareStatement(CI_ResponsiblePartyDB.getUpdate(connection) );
                CI_ResponsiblePartyDB.saveUpdate(contact,preparedStatement);
                preparedStatement.execute();


                //Insertamos los mails
                if (contact.getElectronicMailAdress()!=null && contact.getElectronicMailAdress().size()>0)
                {
                    logger.debug("añadimos mails");
                    for (Enumeration e=contact.getElectronicMailAdress().elements();e.hasMoreElements();)
                    {
                        long lIdMail =CPoolDatabase.getNextValue("ELECTRONICMAILADDRESS","ELECTRONICMAILADDRESS_ID");
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getInsertMails());
                        CI_ResponsiblePartyDB.saveDetalle(new Long(lIdMail).toString(),contact.getId(),(String)e.nextElement(),preparedStatement);
                        preparedStatement.execute();
                    }
                 }

                //Insertamos points
                if (contact.getDeliveryPoint()!=null && contact.getDeliveryPoint().size()>0)
                {
                    logger.debug("añadimos points");
                    for (Enumeration e=contact.getDeliveryPoint().elements();e.hasMoreElements();)
                    {
                        long lIdPoint =CPoolDatabase.getNextValue("DELIVERYPOINT","DELIVERYPOINT_ID");
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getInsertPoints());
                        CI_ResponsiblePartyDB.saveDetalle(new Long(lIdPoint).toString(),contact.getId(),(String)e.nextElement(),preparedStatement);
                        preparedStatement.execute();
                    }
                 }

                //Insertamos Telephonos
                if (contact.getCi_telephone_voice()!=null && contact.getCi_telephone_voice().size()>0)
                {
                    logger.debug("añadimos Telefonos");
                    for (Enumeration e=contact.getCi_telephone_voice().elements();e.hasMoreElements();)
                    {
                        long lIdTelephone =CPoolDatabase.getNextValue("CI_TELEPHONE_VOICE","TELEPHONE_VOICE_ID");
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getInsertTelephones());
                        CI_ResponsiblePartyDB.saveDetalle(new Long(lIdTelephone).toString(),contact.getId(),(String)e.nextElement(),preparedStatement);
                        preparedStatement.execute();
                    }
                 }

                //Insertamos faxes
                 if (contact.getCi_telephone_facsimile()!=null && contact.getCi_telephone_facsimile().size()>0)
                 {
                     logger.debug("añadimos Faxs");
                     for (Enumeration e=contact.getCi_telephone_facsimile().elements();e.hasMoreElements();)
                     {
                         long lIdFax =CPoolDatabase.getNextValue("CI_TELEPHONE_FACSIMILE","TELEPHONE_FACSIMILE_ID");
                         preparedStatement.close();
                         preparedStatement = null;
                         preparedStatement=connection.prepareStatement(CI_ResponsiblePartyDB.getInsertFaxs());
                         CI_ResponsiblePartyDB.saveDetalle(new Long(lIdFax).toString(),contact.getId(),(String)e.nextElement(),preparedStatement);
                         preparedStatement.execute();
                     }
                  }

                //connection.commit();
                logger.debug("Contacto "+contact.getId()+" con name: "+contact.getIndividualName()+" ["+contact.getOrganisationName()+"]");
                resultado=new CResultadoOperacion(true, "Contacto actualizado correctamente");

          }catch(Exception e)
          {
               java.io.StringWriter sw=new java.io.StringWriter();
               java.io.PrintWriter pw=new java.io.PrintWriter(sw);
               e.printStackTrace(pw);
               logger.error("ERROR al actualizar el contacto:"+sw.toString());
               resultado=new CResultadoOperacion(false, e.getMessage());
               try {connection.rollback();} catch (Exception ex2) {}
          }finally{
                    try{rsSQL.close();} catch (Exception ex2) {}
                    try{preparedStatement.close();} catch (Exception ex2) {}
                    try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
                }
               return resultado;
           }
    ////////////////////////METADATOS//////////////////////////////////
    public static CResultadoOperacion ejecutarNewMetadata(MD_Metadata metadato, String sIdMunicipio)
  {
       Connection connection = null;
       PreparedStatement preparedStatement = null;
       ResultSet rsSQL=null;
       CResultadoOperacion resultado;
       try
       {
           logger.debug("Inicio añadir metadato:"+metadato.getMetadatastandardname());
           connection = CPoolDatabase.getConnection();
           if (connection == null)
           {
                  logger.warn("No se puede obtener la conexión");
                   return new CResultadoOperacion(false, "No se puede obtener la conexión");
           }
           try{connection.setAutoCommit(false);}catch(Exception e){};

           //Insertamos el metadato
           long lIdFileIdentifier =CPoolDatabase.getNextValue("MD_METADATA","FILEIDENTIFIER");
           logger.debug("añadimos el metatado:"+lIdFileIdentifier);
           preparedStatement=connection.prepareStatement(MD_MetadataDB.getInsert());
           metadato.setFileidentifier(new Long(lIdFileIdentifier).toString());
           MD_MetadataDB.save(metadato, sIdMunicipio,preparedStatement);
           preparedStatement.execute();
           //insertamos el contacto
           logger.debug("añadimos el contacto:"+(metadato.getResponsibleParty()!=null?metadato.getResponsibleParty().getId():""));
           //Si no tenemos el identificador del contacto lo tenemos que crear esto ocurre cuando se creo sin conexion
           if (metadato.getResponsibleParty()!=null && metadato.getResponsibleParty().getId()==null)
           {
               ejecutarNewContact(metadato.getResponsibleParty(),sIdMunicipio);
           }
           preparedStatement.close();
           preparedStatement = null;
           preparedStatement=connection.prepareStatement(MD_MetadataDB.getInsertContacto());
           MD_MetadataDB.saveContacto(metadato,preparedStatement);
           preparedStatement.execute();
           //insertamos la identificacion
           nuevaIdentificacion(metadato.getIdentificacion(),metadato.getFileidentifier(),sIdMunicipio,preparedStatement,connection);
           logger.debug("NUEVA IDENTIFICACION:"+metadato.getIdentificacion().getIdentification_id());
           //
           nuevaDistribucion(metadato,preparedStatement,connection);
           nuevaCalidad(metadato,preparedStatement,connection);
           nuevoSistemaReferencia(metadato,preparedStatement,connection);
           //connection.commit();
           logger.debug("Metadato "+metadato.getFileidentifier()+" insertado con exito");
           resultado=new CResultadoOperacion(true, new Long(lIdFileIdentifier).toString());
          // resultado.setVector(new Vector());
          // resultado.getVector().add(metadato);
     }catch(Exception e)
     {
          java.io.StringWriter sw=new java.io.StringWriter();
          java.io.PrintWriter pw=new java.io.PrintWriter(sw);
          e.printStackTrace(pw);
          logger.error("ERROR al CREAR el metadato:"+sw.toString());
          resultado=new CResultadoOperacion(false, e.getMessage());
          try {connection.rollback();} catch (Exception ex2) {}
     }finally{
               try{rsSQL.close();} catch (Exception ex2) {}
               try{preparedStatement.close();} catch (Exception ex2) {}
               try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
           }
          return resultado;
      }

    ////////////////////////METADATOS//////////////////////////////////
    public static CResultadoOperacion ejecutarUpdateMetadata(MD_Metadata metadato, String sIdMunicipio)
  {
       Connection connection = null;
       PreparedStatement preparedStatement = null;
       ResultSet rsSQL=null;
       CResultadoOperacion resultado;
       try
       {
           logger.debug("Inicio actualizar metadato:"+metadato.getMetadatastandardname());
           connection = CPoolDatabase.getConnection();
           if (connection == null)
           {
                  logger.warn("No se puede obtener la conexión para actualizar el metadato");
                   return new CResultadoOperacion(false, "No se puede obtener la conexión");
           }
           if (!MD_MetadataDB.exists(metadato.getFileidentifier(),connection))
           {
               try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
               return ejecutarNewMetadata(metadato, sIdMunicipio);
           }
           try{connection.setAutoCommit(false);}catch(Exception e){};

           //actualizamos el metadato
           logger.debug("actualizamos el metatado:"+metadato.getFileidentifier());
           preparedStatement=connection.prepareStatement(MD_MetadataDB.getUpdate());
           MD_MetadataDB.saveUpdate(metadato,preparedStatement);
           preparedStatement.execute();
           //insertamos el contacto
           logger.debug("añadimos el contacto:"+(metadato.getResponsibleParty()!=null?metadato.getResponsibleParty().getId():""));
           //Si no tenemos el identificador del contacto lo tenemos que crear esto ocurre cuando se creo sin conexion
           //primero lo borramos y luego lo añadimos
           if (metadato.getResponsibleParty()!=null && (metadato.getResponsibleParty().getId()==null || !CI_ResponsiblePartyDB.exists(metadato.getResponsibleParty().getId(),connection)))
           {
               ejecutarNewContact(metadato.getResponsibleParty(),sIdMunicipio);
           }
           //Primero borro  los contactos del metadato
           preparedStatement.close();
           preparedStatement = null;
           preparedStatement=connection.prepareStatement(MD_MetadataDB.getDeleteContacto());
           MD_MetadataDB.saveId(metadato.getFileidentifier(),preparedStatement);
           preparedStatement.execute();

           //Ahora los creo
           preparedStatement.close();
           preparedStatement = null;
           preparedStatement=connection.prepareStatement(MD_MetadataDB.getInsertContacto());
           MD_MetadataDB.saveContacto(metadato,preparedStatement);
           preparedStatement.execute();
           //

           logger.debug("ID del MD_DataIdentifier recibido:"+(metadato.getIdentificacion()==null?"MD_DataIdentifier nulo":metadato.getIdentificacion().getIdentification_id()));
           //insertamos la identificacion
           if (metadato.getIdentificacion()!=null&&(metadato.getIdentificacion().getIdentification_id()==null||!MD_DataIdentificationDB.exists(metadato.getIdentificacion().getIdentification_id(),connection)))
           {
               logger.info("Se ha recibido una identificacion sin identificador, esto utilizando la aplicacion cliente no es posible");
               nuevaIdentificacion(metadato.getIdentificacion(),metadato.getFileidentifier(),sIdMunicipio,preparedStatement,connection);
           }
           else
           {
               updateIdentificacion(metadato.getIdentificacion(),sIdMunicipio,preparedStatement, connection);
           }
           updateDistribucion(metadato,preparedStatement,connection);
           updateCalidad(metadato,preparedStatement,connection);

           deleteSistemaReferencia(metadato.getFileidentifier(),connection);
           nuevoSistemaReferencia(metadato,preparedStatement,connection);

           //connection.commit();
           logger.debug("Metadato "+metadato.getFileidentifier()+" insertado con exito");
           resultado=new CResultadoOperacion(true, "Metadato insertado con exito");
           //resultado.setVector(new Vector());
           //resultado.getVector().add(metadato);
     }catch(Exception e)
     {
          java.io.StringWriter sw=new java.io.StringWriter();
          java.io.PrintWriter pw=new java.io.PrintWriter(sw);
          e.printStackTrace(pw);
          logger.error("ERROR al ACTUALIZAR el metadato:"+sw.toString());
          resultado=new CResultadoOperacion(false, e.getMessage());
          try {connection.rollback();} catch (Exception ex2) {}
     }finally{
               try{rsSQL.close();} catch (Exception ex2) {}
               try{preparedStatement.close();} catch (Exception ex2) {}
               try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
           }
          return resultado;
      }

    private static void nuevaIdentificacion(MD_DataIdentification identificacion,String fileIdentifier,String sIdMunicipio, PreparedStatement preparedStatement, Connection connection) throws Exception
     {
         if (identificacion==null)
         {
             logger.error("Error al entrar la IDENTIFICACION, los valores son nulos");
             return;
         }
         try
         {
               nuevaCitacion(identificacion.getCitacion(),preparedStatement,connection);
               long lIdIdentification_id =CPoolDatabase.getNextValue("MD_DATAIDENTIFICATION","IDENTIFICATION_ID");
               logger.debug("añadimos la identificacion:"+lIdIdentification_id);
               preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getInsert());
               identificacion.setIdentification_id(new Long(lIdIdentification_id).toString());
               MD_DataIdentificationDB.save(identificacion,fileIdentifier,preparedStatement);
               preparedStatement.execute();
               //insertamos el contacto
               if (identificacion.getResponsibleParty()!=null)
               {
                   if (identificacion.getResponsibleParty().getId()==null)
                      ejecutarNewContact(identificacion.getResponsibleParty(),sIdMunicipio);

                   logger.debug("añadimos el contacto:"+identificacion.getResponsibleParty().getId());
                   preparedStatement.close();
                   preparedStatement = null;
                   preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getInsertContacto(connection));
                   MD_DataIdentificationDB.saveContacto(identificacion,preparedStatement);
                   preparedStatement.execute();
               }
               //Metemos los idiomas
               if (identificacion.getIdiomas()!=null)
               {
                    for (Enumeration e=identificacion.getIdiomas().elements();e.hasMoreElements();)
                    {
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getInsertIdioma());
                        MD_DataIdentificationDB.saveDescriptor(identificacion,(String)e.nextElement(),preparedStatement);
                        preparedStatement.execute();
                    }
               }
               //Metemos la representacion espacial
               if (identificacion.getrEspacial()!=null)
               {
                    for (Enumeration e=identificacion.getrEspacial().elements();e.hasMoreElements();)
                    {
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getInsertRespresentacionEspacial(connection));
                        MD_DataIdentificationDB.saveDescriptor(identificacion,(String)e.nextElement(),preparedStatement);
                        preparedStatement.execute();
                    }
               }
               //Metemos las categorias
               if (identificacion.getCategorias()!=null)
               {
                    for (Enumeration e=identificacion.getCategorias().elements();e.hasMoreElements();)
                    {
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getInsertCategoria(connection));
                        MD_DataIdentificationDB.saveDescriptor(identificacion,(String)e.nextElement(),preparedStatement);
                        preparedStatement.execute();
                    }
               }
             //Metemos los graficos
             if (identificacion.getGraficos()!=null && identificacion.getGraficos().size()>0)
             {
                  logger.debug("añadimos Graficos");
                  for (Enumeration e=identificacion.getGraficos().elements();e.hasMoreElements();)
                  {
                       long lIdGrafico =CPoolDatabase.getNextValue("MD_BROWSEGRAPHIC","BROWSEGRAPHIC_ID");
                       preparedStatement.close();
                       preparedStatement = null;
                       preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getInsertGrafico());
                       MD_DataIdentificationDB.saveGrafico(new Long(lIdGrafico).toString(),identificacion,(String)e.nextElement(),preparedStatement);
                       preparedStatement.execute();
                  }
              }
              //Metemos la Resolucion
              if (identificacion.getResolucion()!=null)
              {
                  logger.debug("añadimos la resolución: "+identificacion.getResolucion().longValue());
                  long lIdGrafico =CPoolDatabase.getNextValue("MD_RESOLUTION","RESOLUTION_ID");
                  preparedStatement.close();
                  preparedStatement = null;
                  preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getInsertResolucion());
                  MD_DataIdentificationDB.saveResolucion(new Long(lIdGrafico).toString(),identificacion,preparedStatement);
                  preparedStatement.execute();
              }

              nuevaExtension(identificacion.getExtent(),identificacion.getIdentification_id(),preparedStatement,connection);
              nuevaConstraint(identificacion.getConstraint(),identificacion.getIdentification_id(), preparedStatement, connection);
         }catch(Exception e)
          {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("ERROR al CREAR la identificación:"+sw.toString());
                throw (e);
           }
      }

      private static void updateIdentificacion(MD_DataIdentification identificacion,String sIdMunicipio,PreparedStatement preparedStatement, Connection connection) throws Exception
     {
         if (identificacion==null || identificacion.getIdentification_id()==null)
         {
             logger.error("Error al entrar la IDENTIFICACION, los valores son nulos");
             return;
         }
         try
         {
               if (identificacion.getCitacion()!=null&&(identificacion.getCitacion().getCitation_id()==null||!CI_CitationDB.exists(identificacion.getCitacion().getCitation_id(),connection)))
                   nuevaCitacion(identificacion.getCitacion(),preparedStatement,connection);
               else
                   updateCitacion(identificacion.getCitacion(),preparedStatement,connection);

               preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getUpdate());
               MD_DataIdentificationDB.saveUpdate(identificacion,preparedStatement);
               preparedStatement.execute();
               //Primero borramos el contacto
               preparedStatement.close();
               preparedStatement = null;
               preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getDeleteContact(identificacion.getIdentification_id(), connection));
               preparedStatement.execute();
               //insertamos el contacto
               if (identificacion.getResponsibleParty()!=null)
               {
                   if (identificacion.getResponsibleParty().getId()==null||!CI_ResponsiblePartyDB.exists(identificacion.getResponsibleParty().getId(),connection))
                      ejecutarNewContact(identificacion.getResponsibleParty(),sIdMunicipio);

                   logger.debug("añadimos el contacto:"+identificacion.getResponsibleParty().getId());
                   preparedStatement.close();
                   preparedStatement = null;
                   preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getInsertContacto(connection));
                   MD_DataIdentificationDB.saveContacto(identificacion,preparedStatement);
                   preparedStatement.execute();
               }
               //Borramos los idiomas
               preparedStatement.close();
               preparedStatement = null;
               preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getDeleteIdioma(identificacion.getIdentification_id()));
               preparedStatement.execute();
               //Metemos los idiomas
               if (identificacion.getIdiomas()!=null)
               {
                    for (Enumeration e=identificacion.getIdiomas().elements();e.hasMoreElements();)
                    {
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getInsertIdioma());
                        MD_DataIdentificationDB.saveDescriptor(identificacion,(String)e.nextElement(),preparedStatement);
                        preparedStatement.execute();
                    }
               }
               //Borramos la respresentacion espacial
               preparedStatement.close();
               preparedStatement = null;
               preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getDeleteRespresentacionEspacial(identificacion.getIdentification_id(), connection));
               preparedStatement.execute();
               //Metemos la representacion espacial
               if (identificacion.getrEspacial()!=null)
               {
                    for (Enumeration e=identificacion.getrEspacial().elements();e.hasMoreElements();)
                    {
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getInsertRespresentacionEspacial(connection));
                        MD_DataIdentificationDB.saveDescriptor(identificacion,(String)e.nextElement(),preparedStatement);
                        preparedStatement.execute();
                    }
               }
               //Borramos las categorias
               preparedStatement.close();
               preparedStatement = null;
               preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getDeleteCategoria(identificacion.getIdentification_id(), connection));
               preparedStatement.execute();
               //Metemos las categorias
               if (identificacion.getCategorias()!=null)
               {
                    for (Enumeration e=identificacion.getCategorias().elements();e.hasMoreElements();)
                    {
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getInsertCategoria(connection));
                        MD_DataIdentificationDB.saveDescriptor(identificacion,(String)e.nextElement(),preparedStatement);
                        preparedStatement.execute();
                    }
               }
             //Borramos los graficos
             preparedStatement.close();
             preparedStatement = null;
             preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getDeleteGrafico(identificacion.getIdentification_id()));
             preparedStatement.execute();

             //Metemos los graficos
             if (identificacion.getGraficos()!=null && identificacion.getGraficos().size()>0)
             {
                  logger.debug("añadimos Graficos");
                  for (Enumeration e=identificacion.getGraficos().elements();e.hasMoreElements();)
                  {
                       long lIdGrafico =CPoolDatabase.getNextValue("MD_BROWSEGRAPHIC","BROWSEGRAPHIC_ID");
                       preparedStatement.close();
                       preparedStatement = null;
                       preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getInsertGrafico());
                       MD_DataIdentificationDB.saveGrafico(new Long(lIdGrafico).toString(),identificacion,(String)e.nextElement(),preparedStatement);
                       preparedStatement.execute();
                  }
              }
              //Borramos la resolucion
              preparedStatement.close();
              preparedStatement = null;
              preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getDeleteResolucion(identificacion.getIdentification_id()));
              preparedStatement.execute();
              //Metemos la Resolucion
              if (identificacion.getResolucion()!=null)
              {
                  logger.debug("añadimos la resolución: "+identificacion.getResolucion().longValue());
                  long lIdGrafico =CPoolDatabase.getNextValue("MD_RESOLUTION","RESOLUTION_ID");
                  preparedStatement.close();
                  preparedStatement = null;
                  preparedStatement=connection.prepareStatement(MD_DataIdentificationDB.getInsertResolucion());
                  MD_DataIdentificationDB.saveResolucion(new Long(lIdGrafico).toString(),identificacion,preparedStatement);
                  preparedStatement.execute();
              }
              if (identificacion.getExtent()!=null&&(identificacion.getExtent().getExtent_id()==null||!EX_ExtentDB.exists(identificacion.getExtent().getExtent_id(),connection)))
                nuevaExtension(identificacion.getExtent(),identificacion.getIdentification_id(),preparedStatement,connection);
              else
                updateExtension(identificacion.getExtent(),identificacion.getIdentification_id(),preparedStatement,connection);

              if (identificacion.getConstraint()!=null&& (identificacion.getConstraint().getId()==null||!MD_LegalConstraintDB.exists(identificacion.getConstraint().getId(),connection)))
                nuevaConstraint(identificacion.getConstraint(),identificacion.getIdentification_id(), preparedStatement, connection);
              else
                updateConstraint(identificacion.getConstraint(),identificacion.getIdentification_id(), preparedStatement, connection);
         }catch(Exception e)
          {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("ERROR al CREAR la identificación:"+sw.toString());
                throw (e);
           }
      }

      private static void nuevaCitacion(CI_Citation citacion,PreparedStatement preparedStatement, Connection connection) throws Exception
      {
          try
          {
                if (citacion==null) return;
                //Primero hay que añadir la citacion
                long lIdCitation_id =CPoolDatabase.getNextValue("CI_CITATION","CITATION_ID");
                logger.debug("añadimos la citacion:"+lIdCitation_id);
                preparedStatement=connection.prepareStatement(CI_CitationDB.getInsert());
                citacion.setCitation_id(new Long(lIdCitation_id).toString());
                CI_CitationDB.save(citacion,preparedStatement);
                preparedStatement.execute();
                if (citacion.getCI_Dates()!=null)
                {
                    for (Enumeration e=citacion.getCI_Dates().elements();e.hasMoreElements();)
                    {
                        long lIdDate_id =CPoolDatabase.getNextValue("CI_DATE","DATE_ID");
                        logger.debug("añadimos la fecha a la citacion:"+lIdDate_id);
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(CI_CitationDB.getInsertDate(connection));
                        CI_Date auxDate=(CI_Date)e.nextElement();
                        auxDate.setDate_id(new Long(lIdDate_id).toString());
                        CI_CitationDB.saveDate(auxDate,citacion.getCitation_id(),preparedStatement);
                        preparedStatement.execute();
                    }
                }
            }catch(Exception e)
           {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("ERROR al CREAR la citacion:"+sw.toString());
                throw e;
           }
      }
     private static void updateCitacion(CI_Citation citacion,PreparedStatement preparedStatement, Connection connection) throws Exception
      {
          try
          {
                if ((citacion==null)||(citacion.getCitation_id()==null)) return;
                //Primero hay actualizamos la citacion
                 preparedStatement=connection.prepareStatement(CI_CitationDB.getUpdate());
                 CI_CitationDB.saveUpdate(citacion,preparedStatement);
                 preparedStatement.execute();
                 //Borramos las citaciones
                 preparedStatement.close();
                 preparedStatement = null;
                 preparedStatement=connection.prepareStatement(CI_CitationDB.getDeleteDate(citacion.getCitation_id()));
                 preparedStatement.execute();
                 //Insertamos las citacion
                 if (citacion.getCI_Dates()!=null)
                 {
                    for (Enumeration e=citacion.getCI_Dates().elements();e.hasMoreElements();)
                    {
                        long lIdDate_id =CPoolDatabase.getNextValue("CI_DATE","DATE_ID");
                        logger.debug("añadimos la fecha a la citacion:"+lIdDate_id);
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(CI_CitationDB.getInsertDate(connection));
                        CI_Date auxDate=(CI_Date)e.nextElement();
                        auxDate.setDate_id(new Long(lIdDate_id).toString());
                        CI_CitationDB.saveDate(auxDate,citacion.getCitation_id(),preparedStatement);
                        preparedStatement.execute();
                    }
                }
            }catch(Exception e)
           {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("ERROR al CREAR la citacion:"+sw.toString());
                throw e;
           }
      }

    private static void nuevaExtension(EX_Extent extent,String identification_id,PreparedStatement preparedStatement, Connection connection) throws Exception
         {
             try
             {
                 if (extent==null) return;
                 //Primero hay que añadir la extension
                 long lIdExtent_id =CPoolDatabase.getNextValue("EX_EXTENT","EXTENT_ID");
                 logger.debug("añadimos la extent:"+lIdExtent_id);
                 preparedStatement=connection.prepareStatement(EX_ExtentDB.getInsert());
                 extent.setExtent_id(new Long(lIdExtent_id).toString());
                 EX_ExtentDB.save(extent,identification_id,preparedStatement);
                 preparedStatement.execute();
                 //Añadimos el vertical extent
                 if (extent.getVertical()!=null)
                 {
                      long lIdVertical_id =CPoolDatabase.getNextValue("EX_VERTICALEXTENT","VERTICALEXTENT_ID");
                      preparedStatement.close();
                      preparedStatement = null;
                      preparedStatement=connection.prepareStatement(EX_ExtentDB.getInsertVertical());
                      extent.getVertical().setId(new Long(lIdVertical_id).toString());
                      EX_ExtentDB.saveVertical(extent.getVertical(),extent.getExtent_id(),preparedStatement);
                      preparedStatement.execute();
                  }
                 //Añadimos el box
                 if (extent.getBox()!=null)
                 {
                      long lIdBox_id =CPoolDatabase.getNextValue("EX_GEOGRAPHICBOUNDINGBOX","GEOGRAPHICBOUNDINGBOX_ID");
                      preparedStatement.close();
                      preparedStatement = null;
                      preparedStatement=connection.prepareStatement(EX_ExtentDB.getInsertBox());
                      extent.getBox().setId(new Long(lIdBox_id).toString());
                      EX_ExtentDB.saveBox(extent.getBox(),extent.getExtent_id(),preparedStatement);
                      preparedStatement.execute();
                  }
             }catch(Exception e)
             {
                   java.io.StringWriter sw=new java.io.StringWriter();
                   java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                   e.printStackTrace(pw);
                   logger.error("ERROR al CREAR la extension:"+sw.toString());
                   throw e;
              }
         }
        private static void updateExtension(EX_Extent extent,String identification_id,PreparedStatement preparedStatement, Connection connection) throws Exception
         {
              try
              {
                  if (extent==null||extent.getExtent_id()==null)
                  {
                      deleteExtent(identification_id,connection);
                      return;
                  }
                  //Actualizamos la extension
                  logger.debug("Actualizo la extensión. Descripción="+extent.getDescription());
                  preparedStatement=connection.prepareStatement(EX_ExtentDB.getUpdate());
                  EX_ExtentDB.saveUpdate(extent,preparedStatement);
                  preparedStatement.execute();
                  //Borramos el vertical extent
                  preparedStatement.close();
                  preparedStatement = null;
                  preparedStatement=connection.prepareStatement(EX_ExtentDB.getDeleteVertical(extent.getExtent_id()));
                  preparedStatement.execute();
                  //Añadimos el vertical extent
                  if (extent.getVertical()!=null)
                  {
                       if (extent.getVertical().getId()==null)
                       {
                            long lIdVertical_id =CPoolDatabase.getNextValue("EX_VERTICALEXTENT","VERTICALEXTENT_ID");
                            extent.getVertical().setId(new Long(lIdVertical_id).toString());
                       }
                       preparedStatement.close();
                       preparedStatement = null;
                       preparedStatement=connection.prepareStatement(EX_ExtentDB.getInsertVertical());
                       EX_ExtentDB.saveVertical(extent.getVertical(),extent.getExtent_id(),preparedStatement);
                       preparedStatement.execute();
                   }
                  //Borramos el Box
                  preparedStatement.close();
                  preparedStatement = null;
                  preparedStatement=connection.prepareStatement(EX_ExtentDB.getDeleteBox(extent.getExtent_id()));
                  preparedStatement.execute();
                  //Añadimos el box
                  if (extent.getBox()!=null)
                  {
                       if (extent.getBox().getId()==null)
                       {
                           long lIdBox_id =CPoolDatabase.getNextValue("EX_GEOGRAPHICBOUNDINGBOX","GEOGRAPHICBOUNDINGBOX_ID");
                           extent.getBox().setId(new Long(lIdBox_id).toString());
                       }
                       preparedStatement.close();
                       preparedStatement = null;
                       preparedStatement=connection.prepareStatement(EX_ExtentDB.getInsertBox());
                       EX_ExtentDB.saveBox(extent.getBox(),extent.getExtent_id(),preparedStatement);
                       preparedStatement.execute();
                   }
              }catch(Exception e)
              {
                    java.io.StringWriter sw=new java.io.StringWriter();
                    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                    e.printStackTrace(pw);
                    logger.error("ERROR al ACTUALIZAR la extension:"+sw.toString());
                    throw e;
               }
         }

      private static void nuevaConstraint(MD_LegalConstraint constraint,String identification_id,PreparedStatement preparedStatement, Connection connection) throws Exception
      {
          try
          {
              if (constraint==null) return;
              //Primero hay que añadir la constraint
              long lIdConstraint_id =CPoolDatabase.getNextValue("MD_LEGALCONSTRAINT","LEGALCONSTRAINT_ID");
              logger.debug("actualizamos la constraint:"+lIdConstraint_id);
              preparedStatement=connection.prepareStatement(MD_LegalConstraintDB.getInsert());
              constraint.setId(new Long(lIdConstraint_id).toString());
              MD_LegalConstraintDB.save(constraint,identification_id,preparedStatement);
              preparedStatement.execute();
              //Añadimos las restricciones de acceso
              if (constraint.getAccess()!=null)
              {
                   for (Enumeration e=constraint.getAccess().elements();e.hasMoreElements();)
                   {
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(MD_LegalConstraintDB.getInsertAccess());
                        MD_LegalConstraintDB.saveTipos(constraint,(String)e.nextElement(),preparedStatement);
                        preparedStatement.execute();
                   }
               }
               //Añadimos las restricciones de uso
               if (constraint.getUse()!=null)
               {
                    for (Enumeration e=constraint.getUse().elements();e.hasMoreElements();)
                    {
                         preparedStatement.close();
                         preparedStatement = null;
                         preparedStatement=connection.prepareStatement(MD_LegalConstraintDB.getInsertUse());
                         MD_LegalConstraintDB.saveTipos(constraint,(String)e.nextElement(),preparedStatement);
                         preparedStatement.execute();
                    }
               }

          }catch(Exception e)
          {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("ERROR al CREAR la constraint:"+sw.toString());
                throw e;
           }
      }

    private static void updateConstraint(MD_LegalConstraint constraint,String identification_id,PreparedStatement preparedStatement, Connection connection) throws Exception
          {
              try
              {
                  if (constraint==null||constraint.getId()==null)
                  {
                     deleteConstraint(identification_id,connection);
                     return;
                  }
                  //Primero actualizo la constraint
                  preparedStatement=connection.prepareStatement(MD_LegalConstraintDB.getUpdate());
                  MD_LegalConstraintDB.saveUpdate(constraint,preparedStatement);
                  preparedStatement.execute();
                  //Borramoso las restricciones de acceso
                  preparedStatement.close();
                  preparedStatement = null;
                  preparedStatement=connection.prepareStatement(MD_LegalConstraintDB.getDeleteAccess(constraint.getId()));
                  preparedStatement.execute();
                  //Añadimos las restricciones de acceso
                  if (constraint.getAccess()!=null)
                  {
                       for (Enumeration e=constraint.getAccess().elements();e.hasMoreElements();)
                       {
                            preparedStatement.close();
                            preparedStatement = null;
                            preparedStatement=connection.prepareStatement(MD_LegalConstraintDB.getInsertAccess());
                            MD_LegalConstraintDB.saveTipos(constraint,(String)e.nextElement(),preparedStatement);
                            preparedStatement.execute();
                       }
                   }
                   //Borramoso las restricciones de acceso
                   preparedStatement.close();
                   preparedStatement = null;
                   preparedStatement=connection.prepareStatement(MD_LegalConstraintDB.getDeleteUse(constraint.getId()));
                   preparedStatement.execute();
                   //Añadimos las restricciones de uso
                   if (constraint.getUse()!=null)
                   {
                        for (Enumeration e=constraint.getUse().elements();e.hasMoreElements();)
                        {
                             preparedStatement.close();
                             preparedStatement = null;
                             preparedStatement=connection.prepareStatement(MD_LegalConstraintDB.getInsertUse());
                             MD_LegalConstraintDB.saveTipos(constraint,(String)e.nextElement(),preparedStatement);
                             preparedStatement.execute();
                        }
                   }

              }catch(Exception e)
              {
                    java.io.StringWriter sw=new java.io.StringWriter();
                    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                    e.printStackTrace(pw);
                    logger.error("ERROR al ACTUALIZAR la constraint:"+sw.toString());
                    throw e;
               }
          }

      private static void nuevaDistribucion(MD_Metadata metadato,PreparedStatement preparedStatement, Connection connection) throws Exception
     {
         try
         {
             if (metadato==null) return;
             //Añadimos los formatos
             if (metadato.getFormatos()!=null&&metadato.getFormatos().size()>0)
             {
                 for (Enumeration e=metadato.getFormatos().elements();e.hasMoreElements();)
                 {
                       long lIdFormato_id =CPoolDatabase.getNextValue("MD_FORMAT","FORMAT_ID");
                       logger.debug("añadimos la formato:"+lIdFormato_id);
                       preparedStatement.close();
                       preparedStatement = null;
                       preparedStatement=connection.prepareStatement(MD_FormatDB.getInsert());
                       MD_Format auxFormato=(MD_Format)e.nextElement();
                       auxFormato.setId(new Long(lIdFormato_id).toString());
                       MD_FormatDB.save(auxFormato,metadato.getFileidentifier(),preparedStatement);
                       preparedStatement.execute();
                 }
             }
             //Añadimos los recursos
             if (metadato.getOnlineresources()!=null&&metadato.getOnlineresources().size()>0)
             {
                 for (Enumeration e=metadato.getOnlineresources().elements();e.hasMoreElements();)
                 {
                     long lIdOnLine =CPoolDatabase.getNextValue("CI_ONLINERESOURCE","ONLINERESOURCE_ID");
                     if (preparedStatement != null) {
                         preparedStatement.close();
                         preparedStatement = null;
                     }
                     preparedStatement=connection.prepareStatement(CI_OnLineResourceDB.getInsert());
                     CI_OnLineResource auxRecurso=(CI_OnLineResource)e.nextElement();
                     auxRecurso.setId(new Long(lIdOnLine).toString());
                     CI_OnLineResourceDB.save(auxRecurso,preparedStatement);
                     preparedStatement.execute();
                     preparedStatement.close();
                     preparedStatement = null;
                     preparedStatement=connection.prepareStatement(CI_OnLineResourceDB.getInsertRecursoMetadata());
                     CI_OnLineResourceDB.saveRecursoMetadata(metadato.getFileidentifier(), auxRecurso,preparedStatement);
                     preparedStatement.execute();
                 }
             }

         }catch(Exception e)
         {
               java.io.StringWriter sw=new java.io.StringWriter();
               java.io.PrintWriter pw=new java.io.PrintWriter(sw);
               e.printStackTrace(pw);
               logger.error("ERROR al CREAR la extension:"+sw.toString());
               throw e;
          }
     }
    private static void nuevoSistemaReferencia(MD_Metadata metadato,PreparedStatement preparedStatement, Connection connection) throws Exception
    {
        try
        {
            if (metadato==null) return;
            //Añadimos el sistema de referencia
            if (metadato.getReference()!=null&&metadato.getReference().size()>0)
            {
                for (Enumeration e=metadato.getReference().elements();e.hasMoreElements();)
                {
                      preparedStatement.close();
                      preparedStatement = null;
                      preparedStatement=connection.prepareStatement(MD_MetadataDB.getInsertReference());
                      MD_MetadataDB.saveReference(metadato,(String)e.nextElement(),preparedStatement);
                      preparedStatement.execute();
                }
            }

        }catch(Exception e)
        {
              java.io.StringWriter sw=new java.io.StringWriter();
              java.io.PrintWriter pw=new java.io.PrintWriter(sw);
              e.printStackTrace(pw);
              logger.error("ERROR al CREAR la extension:"+sw.toString());
              throw e;
         }
    }

      private static void updateDistribucion(MD_Metadata metadato,PreparedStatement preparedStatement, Connection connection) throws Exception
        {
            try
            {
                if (metadato==null) return;
                //Borramos los formatos
                preparedStatement=connection.prepareStatement(MD_FormatDB.getDelete(metadato.getFileidentifier()));
                preparedStatement.execute();
                //Añadimos los formatos
                if (metadato.getFormatos()!=null&&metadato.getFormatos().size()>0)
                {
                    for (Enumeration e=metadato.getFormatos().elements();e.hasMoreElements();)
                    {
                          MD_Format auxFormato=(MD_Format)e.nextElement();
                          if (auxFormato.getId()==null)
                          {
                                long lIdFormato_id =CPoolDatabase.getNextValue("MD_FORMAT","FORMAT_ID");
                                logger.debug("añadimos la formato:"+lIdFormato_id);
                                auxFormato.setId(new Long(lIdFormato_id).toString());
                          }
                          preparedStatement.close();
                          preparedStatement = null;
                          preparedStatement=connection.prepareStatement(MD_FormatDB.getInsert());
                          MD_FormatDB.save(auxFormato,metadato.getFileidentifier(),preparedStatement);
                          preparedStatement.execute();
                    }
                }
                //Borramos los recursos
                preparedStatement.close();
                preparedStatement = null;
                preparedStatement=connection.prepareStatement(CI_OnLineResourceDB.getDeleteRecursoMetadata(metadato.getFileidentifier()));
                preparedStatement.execute();
                //Añadimos los recursos
                if (metadato.getOnlineresources()!=null&&metadato.getOnlineresources().size()>0)
                {
                    for (Enumeration e=metadato.getOnlineresources().elements();e.hasMoreElements();)
                    {
                        CI_OnLineResource auxRecurso=(CI_OnLineResource)e.nextElement();
                        if ((auxRecurso.getId()==null)||!CI_OnLineResourceDB.exists(auxRecurso.getId(),connection))
                        {
                            long lIdOnLine =CPoolDatabase.getNextValue("CI_ONLINERESOURCE","ONLINERESOURCE_ID");
                            auxRecurso.setId(new Long(lIdOnLine).toString());
                            preparedStatement.close();
                            preparedStatement = null;
                            preparedStatement=connection.prepareStatement(CI_OnLineResourceDB.getInsert());
                            CI_OnLineResourceDB.save(auxRecurso,preparedStatement);
                        }
                        else
                        {
                            preparedStatement.close();
                            preparedStatement = null;
                            preparedStatement=connection.prepareStatement(CI_OnLineResourceDB.getUpdate());
                            CI_OnLineResourceDB.saveUpdate(auxRecurso,preparedStatement);
                        }
                        preparedStatement.execute();
                        preparedStatement.close();
                        preparedStatement = null;
                        preparedStatement=connection.prepareStatement(CI_OnLineResourceDB.getInsertRecursoMetadata());
                        CI_OnLineResourceDB.saveRecursoMetadata(metadato.getFileidentifier(), auxRecurso,preparedStatement);
                        preparedStatement.execute();
                    }
                }

            }catch(Exception e)
            {
                  java.io.StringWriter sw=new java.io.StringWriter();
                  java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                  e.printStackTrace(pw);
                  logger.error("ERROR al CREAR la extension:"+sw.toString());
                  throw e;
             }
        }


       private static void nuevaCalidad(MD_Metadata metadato,PreparedStatement preparedStatement, Connection connection) throws Exception
     {
         try
         {
             if ((metadato==null) || (metadato.getCalidad()==null))return;
             //Añadimos los formatos
             if (metadato.getCalidad().getLinage()!=null)
             {
                 long lIdLinage_id =CPoolDatabase.getNextValue("LI_LINAGE","LINAGE_ID");
                 metadato.getCalidad().getLinage().setId(new Long(lIdLinage_id).toString());
                 preparedStatement=connection.prepareStatement(DQ_DataQualityDB.getInsertLinage(connection));
                 DQ_DataQualityDB.saveLinage(metadato.getCalidad().getLinage(),preparedStatement);
                 preparedStatement.execute();

                 if(metadato.getCalidad().getLinage().getSources()!=null)
                 {
                     for (Enumeration e=metadato.getCalidad().getLinage().getSources().elements();e.hasMoreElements();)
                     {
                           long lIdSource_id =CPoolDatabase.getNextValue("LI_SOURCE","SOURCE_ID");
                           preparedStatement.close();
                           preparedStatement = null;
                           preparedStatement=connection.prepareStatement(DQ_DataQualityDB.getInsertSource());
                           DQ_DataQualityDB.saveLinageDescription(new Long(lIdSource_id).toString(),metadato.getCalidad().getLinage(),
                                            (String)e.nextElement(),preparedStatement);
                           preparedStatement.execute();
                     }
                 }
                 if (metadato.getCalidad().getLinage().getSteps()!=null)
                 {
                     for (Enumeration e=metadato.getCalidad().getLinage().getSteps().elements();e.hasMoreElements();)
                     {
                           long lIdStep_id =CPoolDatabase.getNextValue("LI_PROCESSSTEP","PROCESSSTEP_ID");
                           preparedStatement.close();
                           preparedStatement = null;
                           preparedStatement=connection.prepareStatement(DQ_DataQualityDB.getInsertStep());
                           DQ_DataQualityDB.saveLinageDescription(new Long(lIdStep_id).toString(),metadato.getCalidad().getLinage(),
                                            (String)e.nextElement(),preparedStatement);
                           preparedStatement.execute();
                     }
                 }
             }
             //Añadimos la calidad
             long lIdQuality_id =CPoolDatabase.getNextValue("DQ_DATAQUALITY","DATAQUALITY_ID");
             if (preparedStatement != null) {
                preparedStatement.close();
                preparedStatement = null;
             }
             preparedStatement=connection.prepareStatement(DQ_DataQualityDB.getInsert());
             metadato.getCalidad().setId(new Long(lIdQuality_id).toString());
             DQ_DataQualityDB.save(metadato.getCalidad(),metadato.getFileidentifier(),preparedStatement);
             preparedStatement.execute();

             //Añadimos los informes
             if (metadato.getCalidad().getElements()!=null)
             {
                 for (Enumeration e=metadato.getCalidad().getElements().elements();e.hasMoreElements();)
                 {
                      long lIdElement_id =CPoolDatabase.getNextValue("DQ_ELEMENT","ELEMENT_ID");
                       preparedStatement.close();
                       preparedStatement = null;
                       preparedStatement=connection.prepareStatement(DQ_DataQualityDB.getInsertElement());
                       DQ_Element auxElement=(DQ_Element)e.nextElement();
                       auxElement.setId(new Long(lIdElement_id).toString());
                       if (auxElement.getCitation()!=null)
                           nuevaCitacion(auxElement.getCitation(),preparedStatement,  connection);
                       DQ_DataQualityDB.saveElement(auxElement,metadato.getCalidad(),preparedStatement);
                       preparedStatement.execute();
                 }
             }

         }catch(Exception e)
         {
               java.io.StringWriter sw=new java.io.StringWriter();
               java.io.PrintWriter pw=new java.io.PrintWriter(sw);
               e.printStackTrace(pw);
               logger.error("ERROR al CREAR la extension:"+sw.toString());
               throw e;
          }
     }
    private static void updateCalidad(MD_Metadata metadato,PreparedStatement preparedStatement, Connection connection) throws Exception
    {
        try
        {
            //Borramos la calidad
            deleteCalidad(metadato.getFileidentifier(),connection);
            if ((metadato==null) || (metadato.getCalidad()==null))
            {   return;
            }
            //Añadimos los formatos
            if (metadato.getCalidad().getLinage()!=null)
            {
                if (metadato.getCalidad().getLinage().getId()==null)
                {
                    long lIdLinage_id =CPoolDatabase.getNextValue("LI_LINAGE","LINAGE_ID");
                    metadato.getCalidad().getLinage().setId(new Long(lIdLinage_id).toString());
                }
                preparedStatement=connection.prepareStatement(DQ_DataQualityDB.getInsertLinage(connection));
                DQ_DataQualityDB.saveLinage(metadato.getCalidad().getLinage(),preparedStatement);
                preparedStatement.execute();

                if(metadato.getCalidad().getLinage().getSources()!=null)
                {
                    for (Enumeration e=metadato.getCalidad().getLinage().getSources().elements();e.hasMoreElements();)
                    {
                          long lIdSource_id =CPoolDatabase.getNextValue("LI_SOURCE","SOURCE_ID");
                          preparedStatement.close();
                          preparedStatement = null;
                          preparedStatement=connection.prepareStatement(DQ_DataQualityDB.getInsertSource());
                          DQ_DataQualityDB.saveLinageDescription(new Long(lIdSource_id).toString(),metadato.getCalidad().getLinage(),
                                           (String)e.nextElement(),preparedStatement);
                          preparedStatement.execute();
                    }
                }
                if (metadato.getCalidad().getLinage().getSteps()!=null)
                {
                    for (Enumeration e=metadato.getCalidad().getLinage().getSteps().elements();e.hasMoreElements();)
                    {
                          long lIdStep_id =CPoolDatabase.getNextValue("LI_PROCESSSTEP","PROCESSSTEP_ID");
                          preparedStatement.close();
                          preparedStatement = null;
                          preparedStatement=connection.prepareStatement(DQ_DataQualityDB.getInsertStep());
                          DQ_DataQualityDB.saveLinageDescription(new Long(lIdStep_id).toString(),metadato.getCalidad().getLinage(),
                                           (String)e.nextElement(),preparedStatement);
                          preparedStatement.execute();
                    }
                }
            }
            //Añadimos la calidad
            if (metadato.getCalidad().getId()==null)
            {
                long lIdQuality_id =CPoolDatabase.getNextValue("DQ_DATAQUALITY","DATAQUALITY_ID");
                metadato.getCalidad().setId(new Long(lIdQuality_id).toString());
            }
            if (preparedStatement != null) {
                preparedStatement.close();
                preparedStatement = null;
            }
            preparedStatement=connection.prepareStatement(DQ_DataQualityDB.getInsert());
            DQ_DataQualityDB.save(metadato.getCalidad(),metadato.getFileidentifier(),preparedStatement);
            preparedStatement.execute();

            //Añadimos los informes
            if (metadato.getCalidad().getElements()!=null)
            {
                for (Enumeration e=metadato.getCalidad().getElements().elements();e.hasMoreElements();)
                {
                     DQ_Element auxElement=(DQ_Element)e.nextElement();
                     if (auxElement.getId()==null)
                     {
                        long lIdElement_id =CPoolDatabase.getNextValue("DQ_ELEMENT","ELEMENT_ID");
                        auxElement.setId(new Long(lIdElement_id).toString());
                     }
                     preparedStatement.close();
                     preparedStatement = null;
                     preparedStatement=connection.prepareStatement(DQ_DataQualityDB.getInsertElement());

                    if (auxElement.getCitation()!=null)
                          nuevaCitacion(auxElement.getCitation(),preparedStatement,  connection);
                    DQ_DataQualityDB.saveElement(auxElement,metadato.getCalidad(),preparedStatement);
                     preparedStatement.execute();
                }
            }

        }catch(Exception e)
        {
              java.io.StringWriter sw=new java.io.StringWriter();
              java.io.PrintWriter pw=new java.io.PrintWriter(sw);
              e.printStackTrace(pw);
              logger.error("ERROR al CREAR la extension:"+sw.toString());
              throw e;
         }
    }

      public static CResultadoOperacion ejecutarListaMetadatosParcial(PeticionBusqueda peticion)
      {
           Connection connection = null;
           PreparedStatement preparedStatement = null;
           ResultSet rsSQL=null;
           CResultadoOperacion resultado;
           try
           {
               logger.debug("Inicio de listar metadatos");
               connection = CPoolDatabase.getConnection();
               if (connection == null)
               {
                      logger.warn("No se puede obtener la conexión");
                       return new CResultadoOperacion(false, "No se puede obtener la conexión");
               }
               try{connection.setAutoCommit(false);}catch(Exception e){};

               preparedStatement = MD_MetadataDB.getSelectParcial(peticion,connection);
               rsSQL=preparedStatement.executeQuery();

               Vector auxVector = new Vector();
               while (rsSQL.next())
               {
                     MD_MetadataMini auxMetadataMini=(MD_MetadataMini) MD_MetadataDB.loadParcial(rsSQL);
                     auxVector.add(auxMetadataMini);
               }

               resultado=new CResultadoOperacion(true, "Operación ejecutada correctamente");
               resultado.setVector(auxVector);

         }catch(SQLException e)
         {
              java.io.StringWriter sw=new java.io.StringWriter();
              java.io.PrintWriter pw=new java.io.PrintWriter(sw);
              e.printStackTrace(pw);
              logger.error("ERROR al listar parcialmente los metadatos:"+sw.toString());
              resultado=new CResultadoOperacion(false, e.getMessage());
              try {connection.rollback();} catch (Exception ex2) {}
         }finally{
                   try{rsSQL.close();} catch (Exception ex2) {}
                   try{preparedStatement.close();} catch (Exception ex2) {}
                   try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
               }
              return resultado;
          }

        public static CResultadoOperacion ejecutarGetMetadata(String sFileIdentifier)
       {
               Connection connection = null;
               PreparedStatement preparedStatement = null;
               ResultSet rsSQL=null;
               CResultadoOperacion resultado;
               try
               {
                   logger.debug("Inicio de optener el metadato: "+sFileIdentifier);
                   connection = CPoolDatabase.getConnection();
                   if (connection == null)
                   {
                          logger.warn("No se puede obtener la conexión");
                          return new CResultadoOperacion(false, "No se puede obtener la conexión");
                   }
                   try {connection.setAutoCommit(false);}catch(Exception e){};
                   //Primero hay que mirar si pertenece a un metadata
                   logger.debug("Primero obtenermos los datos simples del metadato");

                   preparedStatement=connection.prepareStatement(MD_MetadataDB.getSelect());
                   MD_MetadataDB.saveId(sFileIdentifier,preparedStatement);
                   rsSQL=preparedStatement.executeQuery();

                   if (!rsSQL.next())
                   {
                       resultado = new CResultadoOperacion(false,"Metadato no encontrado en el sistema");
                   }
                   else
                   {
                       MD_Metadata metadato= (MD_Metadata) MD_MetadataDB.load(rsSQL);
                       if (metadato==null)
                       {
                            resultado = new CResultadoOperacion(false,"Metadato no encontrado en el sistema o valor nulo");
                       }
                       else
                       {
                           //Conseguimos el identificador del contacto
                           preparedStatement.close();
                           preparedStatement = null;
                           preparedStatement=connection.prepareStatement(MD_MetadataDB.getSelectContacto());
                           MD_MetadataDB.saveId(sFileIdentifier,preparedStatement);
                           rsSQL=preparedStatement.executeQuery();
                           if (rsSQL.next())
                           {
                               String sContactId=String.valueOf(rsSQL.getLong("responsibleparty_id"));
                               String sRoleCodeId = String.valueOf(rsSQL.getLong("rolecode_id"));
                               CResultadoOperacion rContacto=ejecutarListaContactos(sContactId);
                               if (rContacto.getResultado())
                               {
                                   if (rContacto.getVector()!=null&&rContacto.getVector().size()>0)
                                   {
                                       metadato.setResponsibleParty((CI_ResponsibleParty)rContacto.getVector().elementAt(0));
                                       metadato.setRolecode_id(sRoleCodeId);
                                   }
                               }
                           }
                           //Conseguimos la identificacion
                           metadato.setIdentificacion(getIdentificacion(metadato.getFileidentifier(),connection));
                           //Metemos la calidad
                           metadato.setCalidad(getCalidad(metadato.getFileidentifier(),connection));
                           //Metemos la distribucion()
                           metadato.setFormatos(getFormatos(metadato.getFileidentifier(),connection));
                           metadato.setOnlineresources(getOnlineresources(metadato.getFileidentifier(),connection));
                           //Metemos la referencia
                           metadato.setReference(getSistemaReferencia(metadato.getFileidentifier(),connection));
                       }

                       resultado=new CResultadoOperacion(true, "Metadato cargado correctamente");
                       Vector auxVector=new Vector();
                       auxVector.add(metadato);
                       resultado.setVector(auxVector);
                   }


             }catch(Exception e)
             {
                  java.io.StringWriter sw=new java.io.StringWriter();
                  java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                  e.printStackTrace(pw);
                  logger.error("ERROR al obtener el metadato "+sFileIdentifier+". Error:"+sw.toString());
                  resultado=new CResultadoOperacion(false, e.getMessage());
                  try {connection.rollback();} catch (Exception ex2) {}
             }finally{
                       try{rsSQL.close();} catch (Exception ex2) {}
                       try{preparedStatement.close();} catch (Exception ex2) {}
                       try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
                   }
                  return resultado;
       }
       private static MD_DataIdentification getIdentificacion(String fileIdentifier, Connection connection) throws Exception
     {

         PreparedStatement pS=null;
         ResultSet rsSQL=null;
         if (fileIdentifier==null)
         {
             logger.error("Error al obtener la IDENTIFICACION, fileIdentifier nulo");
             return null;
         }
         try
         {
             pS=connection.prepareStatement(MD_DataIdentificationDB.getSelect(fileIdentifier));
             rsSQL=pS.executeQuery();
             if (!rsSQL.next())
             {
                 try {rsSQL.close();}catch (Exception ex){}
                 try {pS.close();}catch (Exception ex){}
                 return null;
             }
             MD_DataIdentification identificacion= (MD_DataIdentification) MD_DataIdentificationDB.load(rsSQL);
             if (identificacion.getCitacion()!=null)
                 identificacion.setCitacion(getCitacion(identificacion.getCitacion().getCitation_id(),connection));
             else
                 logger.debug("La identificacion para el fileIdentifier:"+fileIdentifier+" no tiene citacion");
             //Conseguimos el identificador del contacto
             pS.close();
             pS = null;
             pS=connection.prepareStatement(MD_DataIdentificationDB.getSelectContacto(identificacion.getIdentification_id(), connection));
             rsSQL=pS.executeQuery();
             if (rsSQL.next())
             {
                 String sContactId=String.valueOf(rsSQL.getLong("responsibleparty_id"));
                 String sRoleCodeId = String.valueOf(rsSQL.getLong("rolecode_id"));
                 CResultadoOperacion rContacto=ejecutarListaContactos(sContactId);
                 if (rContacto.getResultado())
                 {
                     if (rContacto.getVector()!=null&&rContacto.getVector().size()>0)
                     {
                         identificacion.setResponsibleParty((CI_ResponsibleParty)rContacto.getVector().elementAt(0));
                         identificacion.setRolecode_id(sRoleCodeId);
                     }
                 }
             }
             //Cogemos los idiomas
             pS.close();
             pS = null;
             pS=connection.prepareStatement(MD_DataIdentificationDB.getSelectIdioma(identificacion.getIdentification_id()));
             rsSQL=pS.executeQuery();
             while (rsSQL.next())
             { identificacion.addIdioma(rsSQL.getString(1)); }
             //Cogemos la representación espacil
             pS.close();
             pS = null;
             pS=connection.prepareStatement(MD_DataIdentificationDB.getSelectRespresentacionEspacial(identificacion.getIdentification_id(), connection));
             rsSQL=pS.executeQuery();
             while (rsSQL.next())
             { identificacion.addrEspacial(rsSQL.getString(1)); }
             //Cogemos las categorias
             pS.close();
             pS = null;
             pS=connection.prepareStatement(MD_DataIdentificationDB.getSelectCategoria(identificacion.getIdentification_id(), connection));
             rsSQL=pS.executeQuery();
             while (rsSQL.next())
             { identificacion.addCategoria(rsSQL.getString(1)); }
             //Cogemos los graficos
             pS.close();
             pS = null;
             pS=connection.prepareStatement(MD_DataIdentificationDB.getSelectGrafico(identificacion.getIdentification_id()));
             rsSQL=pS.executeQuery();
             while (rsSQL.next())
             { identificacion.addGrafico(rsSQL.getString(1)); }
             //Obtenermos la resolucion
             pS.close();
             pS = null;
             pS=connection.prepareStatement(MD_DataIdentificationDB.getSelectResolucion(identificacion.getIdentification_id()));
             rsSQL=pS.executeQuery();
             if (rsSQL.next()) identificacion.setResolucion(new Long(rsSQL.getLong(1)));
             //Ponemos el resto
              identificacion.setExtent(getExtension(identificacion.getIdentification_id(),connection));
              identificacion.setConstraint(getConstraint(identificacion.getIdentification_id(), connection));

             try {rsSQL.close();}catch (Exception ex){}
             try {pS.close();}catch (Exception ex){}
              return identificacion;
         }catch(Exception e)
          {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                try {rsSQL.close();}catch (Exception ex){}
                try {pS.close();}catch (Exception ex){}
                logger.error("ERROR al CREAR la identificación:"+sw.toString());
                throw (e);
           }
      }
      private static CI_Citation getCitacion(String citation_id, Connection connection) throws Exception
     {
          PreparedStatement pS=null;
          ResultSet rsSQL=null;
          logger.debug("Recuperando la citacion:"+citation_id);
          if (citation_id==null)
          {
              logger.error("Error al obtener la CITACION, citation_id nula");
              return null;
          }
          try
          {
              pS=connection.prepareStatement(CI_CitationDB.getSelect(citation_id, connection));
              rsSQL=pS.executeQuery();
              if (!rsSQL.next())
              {
                  try {rsSQL.close();}catch (Exception ex){}
                  try {pS.close();}catch (Exception ex){}
                  return null;}
              CI_Citation citacion= (CI_Citation) CI_CitationDB.load(rsSQL);
              try {rsSQL.close();}catch (Exception ex){}
              try {pS.close();}catch (Exception ex){}
              return citacion;
         }catch(Exception e)
          {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                try {rsSQL.close();}catch (Exception ex){}
                try {pS.close();}catch (Exception ex){}
                logger.error("ERROR al CREAR la identificación:"+sw.toString());
                throw (e);
           }
      }
      private static EX_Extent getExtension(String identification_id, Connection connection) throws Exception
     {
          PreparedStatement pS=null;
          ResultSet rsSQL=null;
          logger.debug("Recuperando la extensión de la identificacin:"+identification_id);
          if (identification_id==null)
          {
              logger.error("Error al obtener la EXTENSION, identification_id nula");
              return null;
          }
          try
          {
                pS=connection.prepareStatement(EX_ExtentDB.getSelect(identification_id));
                rsSQL=pS.executeQuery();
                if (!rsSQL.next()) {
                    try {rsSQL.close();}catch (Exception ex){}
                    try {pS.close();}catch (Exception ex){}
                    return null;
                }
                EX_Extent extension= (EX_Extent) EX_ExtentDB.load(rsSQL);

                pS.close();
                pS = null;
                pS=connection.prepareStatement(EX_ExtentDB.getSelectVertical(extension.getExtent_id()));
                rsSQL=pS.executeQuery();
                if (rsSQL.next())
                {
                    extension.setVertical((EX_VerticalExtent) EX_ExtentDB.loadVertical(rsSQL));
                }

                pS.close();
                pS = null;
                pS=connection.prepareStatement(EX_ExtentDB.getSelectBox(extension.getExtent_id()));
                rsSQL=pS.executeQuery();
                if (rsSQL.next())
                {
                    extension.setBox((EX_GeographicBoundingBox) EX_ExtentDB.loadBox(rsSQL));
                }
              try {rsSQL.close();}catch (Exception ex){}
              try {pS.close();}catch (Exception ex){}
              return extension;
         }catch(Exception e)
          {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                try {rsSQL.close();}catch (Exception ex){}
                try {pS.close();}catch (Exception ex){}
                logger.error("ERROR al recoger la extension:"+sw.toString());
                throw (e);
           }
      }
     private static MD_LegalConstraint getConstraint(String identification_id, Connection connection) throws Exception
     {
          PreparedStatement pS=null;
          ResultSet rsSQL=null;
          logger.debug("Recuperando las constraint para la identificacin:"+identification_id);
          if (identification_id==null)
          {
              logger.error("Error al obtener la CONSTRAITN, identification_id nula");
              return null;
          }
          try
          {
                pS=connection.prepareStatement(MD_LegalConstraintDB.getSelect(identification_id));
                rsSQL=pS.executeQuery();
                if (!rsSQL.next()) {
                    try {rsSQL.close();}catch (Exception ex){}
                    try {pS.close();}catch (Exception ex){}
                    return null;
                }
                MD_LegalConstraint constraint= (MD_LegalConstraint) MD_LegalConstraintDB.load(rsSQL);

                pS.close();
                pS = null;
                pS=connection.prepareStatement(MD_LegalConstraintDB.getSelectAccess(constraint.getId()));
                rsSQL=pS.executeQuery();
                while (rsSQL.next())
                {
                    constraint.addAccess(rsSQL.getString(1));
                }

               pS.close();
               pS = null;
               pS=connection.prepareStatement(MD_LegalConstraintDB.getSelectUse(constraint.getId()));
               rsSQL=pS.executeQuery();
               while (rsSQL.next())
               {
                  constraint.addUse(rsSQL.getString(1));
               }


               try {rsSQL.close();}catch (Exception ex){}
               try {pS.close();}catch (Exception ex){}

              return constraint;
         }catch(Exception e)
          {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                try {rsSQL.close();}catch (Exception ex){}
                try {pS.close();}catch (Exception ex){}
                logger.error("ERROR al recoger la extension:"+sw.toString());
                throw (e);
           }
      }

     private static DQ_DataQuality getCalidad(String fileidentifier, Connection connection) throws Exception
     {
         PreparedStatement pS=null;
         ResultSet rsSQL=null;
         logger.debug("Recuperando la calidad para el metadato:"+fileidentifier);
         if (fileidentifier==null)
         {
                logger.error("Error al obtener la CALIDAD, identification_id nula");
                return null;
         }
         try
         {
               pS=connection.prepareStatement(DQ_DataQualityDB.getSelect(fileidentifier));
               rsSQL=pS.executeQuery();
               if (!rsSQL.next()) {
                  try {rsSQL.close();}catch (Exception ex){}
                  try {pS.close();}catch (Exception ex){}
                  return null;
              }
              DQ_DataQuality calidad= (DQ_DataQuality) DQ_DataQualityDB.load(rsSQL);
              //Obtenemos el linage
              if (calidad.getLinage()!=null)
              {
                   logger.debug("Intentando encontrar el linage:"+calidad.getLinage().getId());
                   pS.close();
                   pS = null;
                   pS=connection.prepareStatement(DQ_DataQualityDB.getSelectLinage(calidad.getLinage().getId(), connection));
                   rsSQL=pS.executeQuery();
                   if (rsSQL.next())
                   {
                       LI_Linage linage = (LI_Linage)DQ_DataQualityDB.loadLinage(rsSQL);
                       //Obtenemos las fuentes
                       pS.close();
                       pS = null;
                       pS=connection.prepareStatement(DQ_DataQualityDB.getSelectSource(linage.getId()));
                       rsSQL=pS.executeQuery();
                       while (rsSQL.next())
                       {linage.addSource(rsSQL.getString(1)); }

                       //Obtenemos los pasos
                       pS.close();
                       pS = null;
                       pS=connection.prepareStatement(DQ_DataQualityDB.getSelectStep(linage.getId()));
                       rsSQL=pS.executeQuery();
                       while (rsSQL.next())
                       {linage.addStep(rsSQL.getString(1)); }
                       calidad.setLinage(linage);
                   }else
                   {
                        logger.error("No se ha encontrado el linage"+calidad.getLinage().getId()+". Posible no concordancia de los datos");
                        calidad.setLinage(null);
                   }
              }

              //Obtenemos los elementos
              pS.close();
              pS = null;
              pS=connection.prepareStatement(DQ_DataQualityDB.getSelectElement(calidad.getId()));
              rsSQL=pS.executeQuery();
              while (rsSQL.next())
              {
                  DQ_Element auxElement = (DQ_Element)DQ_DataQualityDB.loadElement(rsSQL);
                  if (auxElement.getCitation()!=null)
                  {
                      auxElement.setCitation(getCitacion(auxElement.getCitation().getCitation_id(),connection));
                  }
                  calidad.addElement(auxElement);
              }


               try {rsSQL.close();}catch (Exception ex){}
               try {pS.close();}catch (Exception ex){}

              return calidad;
         }catch(Exception e)
          {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                try {rsSQL.close();}catch (Exception ex){}
                try {pS.close();}catch (Exception ex){}
                logger.error("ERROR al recoger la calidad:"+sw.toString());
                throw (e);
           }

     }

     private static Vector getFormatos(String fileidentifier, Connection connection) throws Exception
     {
         PreparedStatement pS=null;
         ResultSet rsSQL=null;
         logger.debug("Recuperando los formatos de distribucion para el metadato:"+fileidentifier);
         if (fileidentifier==null)
         {
                logger.error("Error al obtener los MD_FORMAT, identification_id nula");
                return null;
         }
         try
         {
               pS=connection.prepareStatement(MD_FormatDB.getSelect(fileidentifier));
               rsSQL=pS.executeQuery();
               if (!rsSQL.next()) {
                  try {rsSQL.close();}catch (Exception ex){}
                  try {pS.close();}catch (Exception ex){}
                  return null;
              }
              Vector auxVector= new Vector();
              do
              {
                  auxVector.add(MD_FormatDB.load(rsSQL));
              }while (rsSQL.next());
               try {rsSQL.close();}catch (Exception ex){}
               try {pS.close();}catch (Exception ex){}
               return auxVector;
         }catch(Exception e)
          {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                try {rsSQL.close();}catch (Exception ex){}
                try {pS.close();}catch (Exception ex){}
                logger.error("ERROR al recoger los formatos del metadato:"+sw.toString());
                throw (e);
           }

     }
     private static Vector getSistemaReferencia(String fileidentifier, Connection connection) throws Exception
     {
         PreparedStatement pS=null;
         ResultSet rsSQL=null;
         logger.debug("Recuperando los sistemad de referencia:"+fileidentifier);
         if (fileidentifier==null)
         {
                logger.error("Error al obtener los R_METADATAREFERENCESYSTEM, identification_id nula");
                return null;
         }
         try
         {
               pS=connection.prepareStatement(MD_MetadataDB.getSelectReference(fileidentifier));
               rsSQL=pS.executeQuery();
               if (!rsSQL.next()) {
                  try {rsSQL.close();}catch (Exception ex){}
                  try {pS.close();}catch (Exception ex){}
                  return null;
              }
              Vector auxVector= new Vector();
              do
              {
                  auxVector.add(rsSQL.getString(1));
              }while (rsSQL.next());
               try {rsSQL.close();}catch (Exception ex){}
               try {pS.close();}catch (Exception ex){}
               return auxVector;
         }catch(Exception e)
          {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                try {rsSQL.close();}catch (Exception ex){}
                try {pS.close();}catch (Exception ex){}
                logger.error("ERROR al recoger los Sistemas de referencia metadato:"+sw.toString());
                throw (e);
           }

     }

     private static Vector getOnlineresources(String fileidentifier, Connection connection) throws Exception
     {
         PreparedStatement pS=null;
         ResultSet rsSQL=null;
         logger.debug("Recuperando los recursos en linea de la distribucion para el metadato:"+fileidentifier);
         if (fileidentifier==null)
         {
                logger.error("Error al obtener los MD_DIGITALTRANSFEROPTIONS, identification_id nula");
                return null;
         }
         try
         {
               pS=connection.prepareStatement(CI_OnLineResourceDB.getSelect(fileidentifier));
               rsSQL=pS.executeQuery();
               if (!rsSQL.next()) {
                  try {rsSQL.close();}catch (Exception ex){}
                  try {pS.close();}catch (Exception ex){}
                  return null;
              }
              Vector auxVector= new Vector();
              do
              {
                  logger.debug("Añadiendo objeto de formato");
                  auxVector.add(CI_OnLineResourceDB.load(rsSQL));
              }while(rsSQL.next());
               try {rsSQL.close();}catch (Exception ex){}
               try {pS.close();}catch (Exception ex){}
               return auxVector;
         }catch(Exception e)
          {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                try {rsSQL.close();}catch (Exception ex){}
                try {pS.close();}catch (Exception ex){}
                logger.error("ERROR al recoger los recursos en linea del metadato:"+sw.toString());
                throw (e);
           }

     }
     public static CResultadoOperacion ejecutarDeleteMetadata(String sFileIdentifier)
     {
          Connection connection = null;
          PreparedStatement preparedStatement = null;
          ResultSet rsSQL=null;
          CResultadoOperacion resultado;
          try
          {
              if (sFileIdentifier == null)
              {
                     logger.warn("El identificador del metadato es nulo");
                     return new CResultadoOperacion(false, "Identificador del metadato nulo");
              }
              logger.debug("Inicio de optener el metadato: "+sFileIdentifier);
              connection = CPoolDatabase.getConnection();
              if (connection == null)
              {
                     logger.warn("No se puede obtener la conexión");
                     return new CResultadoOperacion(false, "No se puede obtener la conexión");
              }

              try{connection.setAutoCommit(false);}catch(Exception e){};

              logger.debug("Borramos los datos del metadato");

              //Borramos los contactos
              preparedStatement=connection.prepareStatement(MD_MetadataDB.getDeleteContacto());
              MD_MetadataDB.saveId(sFileIdentifier,preparedStatement);
              preparedStatement.execute();

              //borramos la identificacion identificacion
              deleteIdentificacion(sFileIdentifier,connection);
              deleteCalidad(sFileIdentifier,connection);
              deleteDistribucion(sFileIdentifier,connection);
              deleteSistemaReferencia(sFileIdentifier,connection);

              preparedStatement.close();
              preparedStatement = null;
              preparedStatement=connection.prepareStatement(MD_MetadataDB.getDelete());
              MD_MetadataDB.saveId(sFileIdentifier,preparedStatement);
              preparedStatement.execute();

              //connection.commit();
              resultado=new CResultadoOperacion(true, "Metadato borrardo correctamente");
        }catch(Exception e)
        {
             java.io.StringWriter sw=new java.io.StringWriter();
             java.io.PrintWriter pw=new java.io.PrintWriter(sw);
             e.printStackTrace(pw);
             logger.error("ERROR al BORRAR el metadato "+sFileIdentifier+". Error:"+sw.toString());
             resultado=new CResultadoOperacion(false, e.getMessage());
             try {connection.rollback();} catch (Exception ex2) {}
        }finally{
                  try{rsSQL.close();} catch (Exception ex2) {}
                  try{preparedStatement.close();} catch (Exception ex2) {}
                  try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
              }
             return resultado;
      }

      private static boolean  deleteIdentificacion(String fileIdentifier, Connection connection) throws Exception
     {

         PreparedStatement pSpcpal=null;
         PreparedStatement pS=null;
         ResultSet rsSQL=null;
         if (fileIdentifier==null)
         {
             logger.error("Error al borrar la IDENTIFICACION, fileIdentifier nulo");
             return false;
         }
         try
         {
             pSpcpal=connection.prepareStatement(MD_DataIdentificationDB.getSelect(fileIdentifier));
             rsSQL=pSpcpal.executeQuery();
             if (!rsSQL.next())
             {
                 try {rsSQL.close();}catch (Exception ex){}
                 try {pSpcpal.close();}catch (Exception ex){}
                 return true;
             }
             do
             {
                 MD_DataIdentification auxdata= (MD_DataIdentification)MD_DataIdentificationDB.load(rsSQL);
                 //Borramos el contacto
                 pS=connection.prepareStatement(MD_DataIdentificationDB.getDeleteContact(auxdata.getIdentification_id(), connection));
                 pS.execute();
                 //Borramos los idiomas
                 pS.close();

                 pS=connection.prepareStatement(MD_DataIdentificationDB.getDeleteIdioma(auxdata.getIdentification_id()));
                 pS.execute();
                 //Borramos la representación espacil
                 pS.close();
                 pS=connection.prepareStatement(MD_DataIdentificationDB.getDeleteRespresentacionEspacial(auxdata.getIdentification_id(), connection));
                 pS.execute();
                 //Borramos las categorias
                 pS.close();
                 pS=connection.prepareStatement(MD_DataIdentificationDB.getDeleteCategoria(auxdata.getIdentification_id(), connection));
                 pS.execute();
                //Borramos los graficos
                 pS.close();
                 pS=connection.prepareStatement(MD_DataIdentificationDB.getDeleteGrafico(auxdata.getIdentification_id()));
                 pS.execute();
                 //Borramos la resolucion
                 pS.close();
                 pS=connection.prepareStatement(MD_DataIdentificationDB.getDeleteResolucion(auxdata.getIdentification_id()));
                 pS.execute();
                  //Borramos el resto
                 deleteExtent(auxdata.getIdentification_id(),connection);
                 deleteConstraint(auxdata.getIdentification_id(), connection);
                 //Borramos la identificacion
                 pS.close();
                 pS=connection.prepareStatement(MD_DataIdentificationDB.getDelete(auxdata.getIdentification_id()));
                 pS.execute();
                 //Borramos las citacion
                 if (auxdata.getCitacion()!=null)
                       deleteCitation(auxdata.getCitacion().getCitation_id(),connection);
             }while(rsSQL.next());
             try {rsSQL.close();}catch (Exception ex){}
             try {pSpcpal.close();}catch (Exception ex){}
             try {pS.close();}catch (Exception ex){}
             return true;
         }catch(Exception e)
          {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                try {rsSQL.close();}catch (Exception ex){}
                try {pS.close();}catch (Exception ex){}
                logger.error("ERROR al BORRAR la identificación:"+sw.toString());
                throw (e);
           }
      }

      private static boolean  deleteCitation(String citation_id, Connection connection) throws Exception
      {
            PreparedStatement pS=null;
            if (citation_id==null)
            {
                logger.error("Error al borrar la CITACION, citation_id nulo");
                return false;
            }
            try
            {
                //Primero borrarmos las fechas
                pS=connection.prepareStatement(CI_CitationDB.getDeleteDate(citation_id));
                pS.execute();

                //Borrarmos la citacion
                pS.close();
                pS = null;
                pS=connection.prepareStatement(CI_CitationDB.getDelete(citation_id));
                pS.execute();

                try {pS.close();}catch (Exception ex){}
                return true;
             }catch(Exception e)
              {
                    java.io.StringWriter sw=new java.io.StringWriter();
                    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                    e.printStackTrace(pw);
                    try {pS.close();}catch (Exception ex){}
                    logger.error("ERROR al BORRAR la citacion:"+sw.toString());
                    throw (e);
               }
        }

        private static boolean  deleteExtent(String identification_id, Connection connection) throws Exception
        {
              PreparedStatement pS=null;
              ResultSet rsSQL=null;
              if (identification_id==null)
              {
                  logger.error("Error al borrar la EXTENSION, identification_id nulo");
                  return false;
              }
              try
              {
                  //Primero obtenemos los extent
                  pS=connection.prepareStatement(EX_ExtentDB.getSelect(identification_id));
                  rsSQL=pS.executeQuery();
                  if (!rsSQL.next())
                  {
                         try {rsSQL.close();}catch (Exception ex){}
                         try {pS.close();}catch (Exception ex){}
                         return true;
                  }
                  do
                  {
                      EX_Extent auxextent= (EX_Extent)EX_ExtentDB.load(rsSQL);

                      //Primero borramos los vertical
                      pS.close();
                      pS = null;
                      pS=connection.prepareStatement(EX_ExtentDB.getDeleteVertical(auxextent.getExtent_id()));
                      pS.execute();
                      //Borrarmos los box
                      pS.close();
                      pS = null;
                      pS=connection.prepareStatement(EX_ExtentDB.getDeleteBox(auxextent.getExtent_id()));
                      pS.execute();
                      //Borramos la extension
                      pS.close();
                      pS = null;
                      pS=connection.prepareStatement(EX_ExtentDB.getDelete(auxextent.getExtent_id()));
                      pS.execute();
                  }while(rsSQL.next());

                  try {rsSQL.close();}catch (Exception ex){}
                  try {pS.close();}catch (Exception ex){}
                  return true;
               }catch(Exception e)
                {
                      java.io.StringWriter sw=new java.io.StringWriter();
                      java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                      e.printStackTrace(pw);
                      try {rsSQL.close();}catch (Exception ex){}
                      try {pS.close();}catch (Exception ex){}
                      logger.error("ERROR al BORRAR la extension:"+sw.toString());
                      throw (e);
                 }
          }

            private static boolean  deleteConstraint(String identification_id, Connection connection) throws Exception
           {
                 PreparedStatement pS=null;
                 ResultSet rsSQL=null;
                 if (identification_id==null)
                 {
                     logger.error("Error al borrar la CONSTRAINT, identification_id nulo");
                     return false;
                 }
                 try
                 {
                     //Primero obtenemos los extent
                     pS=connection.prepareStatement(MD_LegalConstraintDB.getSelect(identification_id));
                     rsSQL=pS.executeQuery();
                     if (!rsSQL.next())
                     {
                            try {rsSQL.close();}catch (Exception ex){}
                            try {pS.close();}catch (Exception ex){}
                            return true;
                     }
                     do
                     {
                         MD_LegalConstraint auxconstraint= (MD_LegalConstraint)MD_LegalConstraintDB.load(rsSQL);
                         //Primero borramos los acceso
                         pS.close();
                         pS = null;
                         pS=connection.prepareStatement(MD_LegalConstraintDB.getDeleteAccess(auxconstraint.getId()));
                         pS.execute();
                         //Borramos el uso
                         pS.close();
                         pS = null;
                         pS=connection.prepareStatement(MD_LegalConstraintDB.getDeleteUse(auxconstraint.getId()));
                         pS.execute();
                         //Borramos la constraint
                         pS.close();
                         pS = null;
                         pS=connection.prepareStatement(MD_LegalConstraintDB.getDelete(auxconstraint.getId()));
                         pS.execute();

                     }while(rsSQL.next());

                     try {rsSQL.close();}catch (Exception ex){}
                     try {pS.close();}catch (Exception ex){}
                     return true;
                  }catch(Exception e)
                   {
                         java.io.StringWriter sw=new java.io.StringWriter();
                         java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                         e.printStackTrace(pw);
                         try {rsSQL.close();}catch (Exception ex){}
                         try {pS.close();}catch (Exception ex){}
                         logger.error("ERROR al BORRAR la extension:"+sw.toString());
                         throw (e);
                    }
             }
       private static boolean  deleteCalidad(String sFileIdentifier, Connection connection) throws Exception
       {
             PreparedStatement pS=null;
             ResultSet rsSQL=null;
             if (sFileIdentifier==null)
             {
                 logger.error("Error al borrar la  CALIDAD, fileidentifier nulo");
                 return false;
             }
             try
             {
                 //Primero obtenemos la calidad
                 pS=connection.prepareStatement(DQ_DataQualityDB.getSelect(sFileIdentifier));
                 rsSQL=pS.executeQuery();
                 if (!rsSQL.next())
                 {
                        try {rsSQL.close();}catch (Exception ex){}
                        try {pS.close();}catch (Exception ex){}
                        return true;
                 }
                 do
                 {
                     DQ_DataQuality auxquality= (DQ_DataQuality)DQ_DataQualityDB.load(rsSQL);
                     //Primero borramos los informes
                     pS.close();
                     pS = null;
                     pS=connection.prepareStatement(DQ_DataQualityDB.getDeleteElement(auxquality.getId()));
                     pS.execute();
                     //Borramos la calidad
                      pS.close();
                      pS = null;
                      pS=connection.prepareStatement(DQ_DataQualityDB.getDelete(auxquality.getId()));
                      pS.execute();
                     //Borramos el linage
                     if (auxquality.getLinage()!=null)
                     {
                          pS.close();
                          pS = null;
                          pS=connection.prepareStatement(DQ_DataQualityDB.getDeleteSource(auxquality.getLinage().getId()));
                          pS.execute();
                          pS.close();
                          pS = null;
                          pS=connection.prepareStatement(DQ_DataQualityDB.getDeleteStep(auxquality.getLinage().getId()));
                          pS.execute();
                          pS.close();
                          pS = null;
                          pS=connection.prepareStatement(DQ_DataQualityDB.getDeleteLinage(auxquality.getLinage().getId()));
                          pS.execute();
                     }

                 }while(rsSQL.next());
                 try {rsSQL.close();}catch (Exception ex){}
                 try {pS.close();}catch (Exception ex){}
                 return true;
              }catch(Exception e)
               {
                     java.io.StringWriter sw=new java.io.StringWriter();
                     java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                     e.printStackTrace(pw);
                     try {rsSQL.close();}catch (Exception ex){}
                     try {pS.close();}catch (Exception ex){}
                     logger.error("ERROR al BORRAR la extension:"+sw.toString());
                     throw (e);
                }
         }
         private static boolean  deleteSistemaReferencia(String sFileIdentifier,Connection connection) throws Exception
        {
                 PreparedStatement pS=null;
                 ResultSet rsSQL=null;
                 if (sFileIdentifier==null)
                 {
                     logger.error("Error al borrar el Sistema de referencia, fileidentifier nulo");
                     return false;
                 }
                 try
                 {
                     pS=connection.prepareStatement(MD_MetadataDB.getDeleteReference(sFileIdentifier));
                     pS.execute();
                     try {rsSQL.close();}catch (Exception ex){}
                     try {pS.close();}catch (Exception ex){}
                     return true;
                  }catch(Exception e)
                   {
                         java.io.StringWriter sw=new java.io.StringWriter();
                         java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                         e.printStackTrace(pw);
                         try {rsSQL.close();}catch (Exception ex){}
                         try {pS.close();}catch (Exception ex){}
                         logger.error("ERROR al BORRAR la distribucion:"+sw.toString());
                         throw (e);
                    }
             }

          private static boolean  deleteDistribucion(String sFileIdentifier, Connection connection) throws Exception
       {
             PreparedStatement pS=null;
             ResultSet rsSQL=null;
             if (sFileIdentifier==null)
             {
                 logger.error("Error al borrar la  DISTRIBUCION, fileidentifier nulo");
                 return false;
             }
             try
             {
                 //Primero borramos MD_DIGITAL_TRANSFEROPTIONS
                 pS=connection.prepareStatement(CI_OnLineResourceDB.getDeleteRecursoMetadata(sFileIdentifier));
                 pS.execute();
                 //Borramos los formatos
                 pS.close();
                 pS = null;
                 pS=connection.prepareStatement(MD_FormatDB.getDelete(sFileIdentifier));
                 pS.execute();

                 try {rsSQL.close();}catch (Exception ex){}
                 try {pS.close();}catch (Exception ex){}
                 return true;
              }catch(Exception e)
               {
                     java.io.StringWriter sw=new java.io.StringWriter();
                     java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                     e.printStackTrace(pw);
                     try {rsSQL.close();}catch (Exception ex){}
                     try {pS.close();}catch (Exception ex){}
                     logger.error("ERROR al BORRAR la distribucion:"+sw.toString());
                     throw (e);
                }
         }
         public static String getIdAclCapa(String id_capa)
         {
             Connection connection = null;
             PreparedStatement preparedStatement = null;
             ResultSet rsSQL=null;
             String resultado=null;
             try
             {
                logger.debug("Recogiendo el Acl para la capa: "+id_capa);
                connection = CPoolDatabase.getConnection();
                if (connection == null)
                {
                    logger.warn("No se puede obtener la conexión");
                    return null;
                }
                preparedStatement = connection.prepareStatement("select acl from layers where id_layer='"+id_capa+"'");
                rsSQL=preparedStatement.executeQuery();
                if (rsSQL.next())
                {
                    resultado=rsSQL.getString(1);
                    logger.debug("Acl obtenido: "+resultado+" para la capa "+id_capa);
                }
                else
                    logger.debug("La capa "+id_capa+" no tiene ningún acl asignado");



         }catch(SQLException e)
         {
              java.io.StringWriter sw=new java.io.StringWriter();
              java.io.PrintWriter pw=new java.io.PrintWriter(sw);
              e.printStackTrace(pw);
              logger.error("ERROR al CREAR el Domain Node:"+sw.toString());
              return null;
         }finally{
                   try{rsSQL.close();} catch (Exception ex2) {}
                   try{preparedStatement.close();} catch (Exception ex2) {}
                   try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
               }
              return resultado;
         }
         public static CResultadoOperacion getCapas(Sesion sSesion)
         {
             Connection connection = null;
             PreparedStatement preparedStatement = null;
             ResultSet rsSQL=null;
             CResultadoOperacion resultado;
             try
             {
               logger.debug("Recojo la lista de mapas");
               connection = CPoolDatabase.getConnection();
               if (connection == null)
               {
                      logger.warn("No se puede obtener la conexión");
                       return new CResultadoOperacion(false, "No se puede obtener la conexión");
               }
               try{connection.setAutoCommit(false);}catch(Exception e){};

               preparedStatement = connection.prepareStatement(
                            "select layers.id_layer as id , layers.id_name as id_name, layers.acl as acl,dictionary.locale as locale,"+
                            "  dictionary.traduccion as traduccion from layers, dictionary where layers.id_name=dictionary.id_vocablo " +
                            "  order by layers.id_layer");

               rsSQL=preparedStatement.executeQuery();
               Vector auxVector = new Vector();

               Capa  oldCapa=null;
               while (rsSQL.next())
               {
                     boolean noAcl=false;
                     String newId=rsSQL.getString("id");
                     if ((oldCapa==null) || (!oldCapa.getId().equals(newId)))
                     {
                                 oldCapa= new Capa(rsSQL.getString("id"),rsSQL.getString("id_name"));
                                 oldCapa.setIdAcl(rsSQL.getString("acl"));
                                 oldCapa.addTerm(rsSQL.getString("locale"), rsSQL.getString("traduccion"));
                                 if (oldCapa.getIdAcl()!=null)
                                 {
                                     try
                                     {
                                        GeopistaAcl acl = SesionUtils_LCGIII.getPerfil(sSesion,new Long(oldCapa.getIdAcl()).longValue());
                                        if (!acl.checkPermission(new GeopistaPermission(GeopistaPermission.LEER_CAPA)))
                                        {
                                           logger.warn("El usuario "+sSesion.getUserPrincipal().getName()+" NO tiene permiso para leer en la capa "+oldCapa.getId());
                                           noAcl=true;
                                        }
                                     }catch (java.security.acl.AclNotFoundException ex)
                                     {}
                                      catch (Exception e)
                                     {
                                         java.io.StringWriter sw=new java.io.StringWriter();
                                         java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                                         e.printStackTrace(pw);
                                         logger.error("ERROR al obtener el ACL:"+sw.toString());
                                     }
                                 }
                                 if (!noAcl)
                                    auxVector.add(oldCapa);
                     }
                     else
                     {
                                 oldCapa.addTerm(rsSQL.getString("locale"), rsSQL.getString("traduccion"));
                     }
                }

               resultado=new CResultadoOperacion(true, "Operación ejecutada correctamente");
               resultado.setVector(auxVector);

         }catch(SQLException e)
         {
              java.io.StringWriter sw=new java.io.StringWriter();
              java.io.PrintWriter pw=new java.io.PrintWriter(sw);
              e.printStackTrace(pw);
              logger.error("ERROR al CREAR el Domain Node:"+sw.toString());
              resultado=new CResultadoOperacion(false, e.getMessage());
              try {connection.rollback();} catch (Exception ex2) {}
         }finally{
                   try{rsSQL.close();} catch (Exception ex2) {}
                   try{preparedStatement.close();} catch (Exception ex2) {}
                   try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
               }
              return resultado;
          }

      public static CResultadoOperacion ejecutarUpdateAll(String sCampo, String sValor, String sMunicipio)
      {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            CResultadoOperacion resultado;
            try
            {
                if (sCampo == null || sValor==null || sMunicipio==null)
                {
                       logger.warn("Se han recibido valores nulos");
                       return new CResultadoOperacion(false, "Se han recibido valores nulos");
                }
                logger.debug("Inico de actualizar todos los metadatos sCampo: "+sCampo+", sValor:"+sValor+", sMunicipio:"+sMunicipio);
                connection = CPoolDatabase.getConnection();
                if (connection == null)
                {
                       logger.warn("No se puede obtener la conexión");
                       return new CResultadoOperacion(false, "No se puede obtener la conexión");
                }
                connection.setAutoCommit(true);
                preparedStatement = connection.prepareStatement(
                                        "update md_metadata set "+sCampo +" = ? where id_municipio =?");
                preparedStatement.setString(1,sValor);
                preparedStatement.setString(2,sMunicipio);
                int i=preparedStatement.executeUpdate();
                resultado = new CResultadoOperacion(true, "Actualización ejecutada correctamente. Metadatos actualizados:"+i);
            }
            catch (Exception e)
            {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("ERROR al CREAR el Domain Node:"+sw.toString());
                resultado=new CResultadoOperacion(false, e.getMessage());
                try {connection.rollback();} catch (Exception ex2) {}
            }finally{
                   try{preparedStatement.close();} catch (Exception ex2) {}
                   try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
               }
              return resultado;
          }


    }
