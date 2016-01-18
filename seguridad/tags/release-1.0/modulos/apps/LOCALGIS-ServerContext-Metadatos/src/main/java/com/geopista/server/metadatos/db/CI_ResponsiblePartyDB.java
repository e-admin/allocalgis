package com.geopista.server.metadatos.db;

import com.geopista.protocol.metadatos.CI_ResponsibleParty;
import com.geopista.protocol.metadatos.CI_OnLineResource;
import com.geopista.server.database.CPoolDatabase;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;

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
 * Time: 10:51:39
 */

public class CI_ResponsiblePartyDB {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CI_ResponsiblePartyDB.class);
    CI_ResponsibleParty responsibleParty;
    public CI_ResponsiblePartyDB(CI_ResponsibleParty este)
    {
        responsibleParty=este;
    }

    public CI_ResponsiblePartyDB()
    {
        this(null);
    }


    public String getTable()
    {
        return "CI_RESPONSIBLEPARTY";
    }

    public CI_ResponsibleParty getResponsibleParty()
    {
        return responsibleParty;
    }

    public static Object load(ResultSet rs, Connection conn)
    {
        CI_ResponsibleParty result = new CI_ResponsibleParty();
        try
        {
            if (CPoolDatabase.isPostgres(conn))
            {
                result.setId(String.valueOf(rs.getLong("responsibleparty_id")) );
                result.setIndividualName(rs.getString("individualName"));
                result.setOrganisationName(rs.getString("organisationName"));
                result.setPositionName(rs.getString("positionName"));
                result.setAddressCity(rs.getString("ci_contact_ci_address_city"));
                result.setAddressAdministrativeArea(rs.getString("ci_contact_ci_address_administrativeArea"));
                result.setAddressPostalCode(rs.getString("ci_contact_ci_address_postalCode"));
                result.setAddressCountry(rs.getString("ci_contact_ci_address_country"));
                result.setHoursOfService(rs.getString("ci_contact_hoursOfService"));
                result.setContactInstructions(rs.getString("ci_contact_contactInstructions"));
            } else
            {
                result.setId(rs.getString("responsibleparty_id") );
                result.setIndividualName(rs.getString("individualName"));
                result.setOrganisationName(rs.getString("organisationName"));
                result.setPositionName(rs.getString("positionName"));
                result.setAddressCity(rs.getString("ci_contact_ci_addr_city"));
                result.setAddressAdministrativeArea(rs.getString("ci_contact_ci_addr_adminArea"));
                result.setAddressPostalCode(rs.getString("ci_contact_ci_addr_postalCode"));
                result.setAddressCountry(rs.getString("ci_contact_ci_addr_country"));
                result.setHoursOfService(rs.getString("ci_contact_hoursOfService"));
                result.setContactInstructions(rs.getString("ci_contact_contactInstructions"));
            }
            CI_OnLineResource auxOnLineResource=(CI_OnLineResource)CI_OnLineResourceDB.load(rs);
            result.setOnLineResource(auxOnLineResource);
        }catch(Exception e)
        {
             java.io.StringWriter sw=new java.io.StringWriter();
		     java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	 e.printStackTrace(pw);
             logger.error("ERROR al recoger el CI_OnLineResource:"+sw.toString());
             return null;
        }
        return result;
    }
    public static boolean save(CI_ResponsibleParty contact, String sIdMunicipio,PreparedStatement ps)
    {
        try
        {
            if  (ps==null) return false;
            ps.setLong(1,Long.parseLong(contact.getId()));
            ps.setString(2,contact.getIndividualName());
            ps.setString(3,contact.getOrganisationName());
            ps.setString(4,contact.getPositionName());
            ps.setString(5,contact.getAddressCity());
            ps.setString(6,contact.getAddressAdministrativeArea());
            ps.setString(7,contact.getAddressPostalCode());
            ps.setString(8,contact.getAddressCountry());
            ps.setString(9,contact.getHoursOfService());
            ps.setString(10,contact.getContactInstructions());
            if (contact.getOnLineResource() == null)
            	ps.setNull(11, java.sql.Types.NUMERIC);
            else
            	ps.setLong(11,Long.parseLong(contact.getOnLineResource().getId()));
            ps.setLong(12,Long.parseLong(sIdMunicipio));
        }catch(Exception e)
        {
             java.io.StringWriter sw=new java.io.StringWriter();
             java.io.PrintWriter pw=new java.io.PrintWriter(sw);
             e.printStackTrace(pw);
             logger.error("ERROR al introducir los datos del nuevo contacto:"+sw.toString());
            return false;
        }
        return true;

    }
    public static boolean saveUpdate(CI_ResponsibleParty contact, PreparedStatement ps)
       {
           try
           {
               if  (ps==null) return false;
               ps.setString(1,contact.getIndividualName());
               ps.setString(2,contact.getOrganisationName());
               ps.setString(3,contact.getPositionName());
               ps.setString(4,contact.getAddressCity());
               ps.setString(5,contact.getAddressAdministrativeArea());
               ps.setString(6,contact.getAddressPostalCode());
               ps.setString(7,contact.getAddressCountry());
               ps.setString(8,contact.getHoursOfService());
               ps.setString(9,contact.getContactInstructions());
               ps.setString(10,contact.getOnLineResource()!=null?contact.getOnLineResource().getId():null);
               ps.setString(11,contact.getId());

           }catch(Exception e)
           {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("ERROR al introducir los datos del nuevo contacto:"+sw.toString());
               return false;
           }
           return true;

       }

    public static String getSelect(Connection conn)
    {
       if (CPoolDatabase.isPostgres(conn))
       {
            return "Select responsibleparty_id, individualname, organisationname, positionname, ci_contact_ci_address_city "+
                ",ci_contact_ci_address_administrativearea,ci_contact_ci_address_postalcode,ci_contact_ci_address_country "+
                ", ci_contact_hoursofservice, ci_contact_hoursofservice, ci_contact_contactinstructions, "+
                "onlineresource_id, linkage, onlinefunctioncode_id from CI_RESPONSIBLEPARTY LEFT OUTER JOIN CI_ONLINERESOURCE on "+
                "(ci_contact_onlineresource_id=onlineresource_id)";
       } else
       {
           return "Select responsibleparty_id, individualname, organisationname, positionname, ci_contact_ci_addr_city " +
                "              ,ci_contact_ci_addr_adminarea,ci_contact_ci_addr_postalcode,ci_contact_ci_addr_country " +
                "              , ci_contact_hoursofservice, ci_contact_hoursofservice, ci_contact_contactinstructions, " +
                "              onlineresource_id, linkage, onlinefunctioncode_id from CI_RESPONSIBLEPARTY LEFT OUTER JOIN CI_ONLINERESOURCE on " +
                "              (ci_contact_onlineresource_id=onlineresource_id)";
       }
    }

    public static String getSelect(String sResponsiblePartyId, Connection conn)
    {
        return getSelect(conn) + " where responsibleparty_id='"+sResponsiblePartyId+"'";
    }

    public static String getSelectTelefonos()
    {
        return "select voice from ci_telephone_voice where responsibleparty_id=?";
    }

    public static String getSelectMails()
    {
        return "select electronicmailaddress from electronicmailaddress where responsibleparty_id=?";
    }

    public static String getSelectDeliveryPoint()
    {
            return "select deliverypoint from deliverypoint where responsibleparty_id=?";
    }
    public static String getSelectFax()
    {
            return "select Facsimile from ci_telephone_facsimile where responsibleparty_id=?";
    }

    public static String getInsert(Connection conn)
    {
        if (CPoolDatabase.isPostgres(conn))
        {
            return "Insert into CI_RESPONSIBLEPARTY (responsibleparty_id, individualname, organisationname, positionname, " +
                    "ci_contact_ci_address_city ,ci_contact_ci_address_administrativearea,ci_contact_ci_address_postalcode, " +
                    "ci_contact_ci_address_country, ci_contact_hoursofservice, ci_contact_contactinstructions, "+
                   " ci_contact_onlineresource_id, id_municipio) values (?,?,?,?,?,?,?,?,?,?,?,?)";
        } else
        {
            return "Insert into CI_RESPONSIBLEPARTY (responsibleparty_id, individualname, organisationname, " +
                    "positionname,ci_contact_ci_addr_city ,ci_contact_ci_addr_adminarea,ci_contact_ci_addr_postalcode," +
                    "ci_contact_ci_addr_country, ci_contact_hoursofservice, ci_contact_contactinstructions," +
                    "ci_contact_onlineresource_id, id_municipio) values (?,?,?,?,?,?,?,?,?,?,?,?)";
        }
    }
    public static String getUpdate(Connection conn)
   {
        if (CPoolDatabase.isPostgres(conn))
        {
            return "Update CI_RESPONSIBLEPARTY set "+
                    " individualname=?, organisationname=?, positionname=?, " +
                    " ci_contact_ci_address_city=?, ci_contact_ci_address_administrativearea=?, "+
                    " ci_contact_ci_address_postalcode=?, " +
                    "ci_contact_ci_address_country=?, ci_contact_hoursofservice=?, ci_contact_contactinstructions=?, "+
                   " ci_contact_onlineresource_id=? where responsibleparty_id=?";
        } else
        {
            return "Update CI_RESPONSIBLEPARTY set "+
                    " individualname=?, organisationname=?, positionname=?, " +
                    " ci_contact_ci_addr_city=?, ci_contact_ci_addr_adminarea=?, "+
                    " ci_contact_ci_addr_postalcode=?, " +
                    "ci_contact_ci_addr_country=?, ci_contact_hoursofservice=?, ci_contact_contactinstructions=?, "+
                   " ci_contact_onlineresource_id=? where responsibleparty_id=?";
        }
   }

    public static String getDelete()
    {
        return "Delete from CI_RESPONSIBLEPARTY where responsibleparty_id=?";
    }
    public static String getInsertMails()
    {
        return "Insert into electronicmailaddress (electronicmailaddress_id, responsibleparty_id,electronicmailaddress) values (?,?,?)";
    }
    public static String getDeleteMails()
    {
           return "delete from electronicmailaddress where responsibleparty_id=?";
    }

    public static String getInsertPoints()
    {
       return "Insert into deliverypoint (deliverypoint_id, responsibleparty_id,deliverypoint) values (?,?,?)";
    }
    public static String getDeletePoints()
    {
          return "delete from deliverypoint where responsibleparty_id=?";
    }

    public static String getInsertTelephones()
    {
           return "Insert into ci_telephone_voice (telephone_voice_id, responsibleparty_id,voice) values (?,?,?)";
    }
    public static String getDeleteTelephones()
    {
              return "delete from ci_telephone_voice where responsibleparty_id=?";
    }

    public static String getInsertFaxs()
    {
          return "Insert into ci_telephone_facsimile (telephone_facsimile_id, responsibleparty_id,facsimile) values (?,?,?)";
    }

    public static String getDeleteFaxs()
    {
        return "delete from ci_telephone_facsimile where responsibleparty_id=?";
    }


    public static boolean saveDetalle(String idDetalle, String idContact, String detalle, PreparedStatement ps)
    {
        try
        {
            if (ps==null) return false;
            ps.setString(1,idDetalle);
            ps.setString(2,idContact);
            ps.setString(3,detalle);
        }catch(Exception e)
        {
             java.io.StringWriter sw=new java.io.StringWriter();
             java.io.PrintWriter pw=new java.io.PrintWriter(sw);
             e.printStackTrace(pw);
             logger.error("ERROR al introducir datos de detalle:"+sw.toString());
            return false;
        }
        return true;
    }

     public static boolean exists(String responsibleparty_id,Connection connection) throws Exception
    {
         PreparedStatement pS=null;
         ResultSet rsSQL=null;
         if (responsibleparty_id==null) return false;
         try
         {
             pS=connection.prepareStatement("select responsibleparty_id from CI_RESPONSIBLEPARTY where responsibleparty_id=?");
             pS.setLong(1, Long.valueOf(responsibleparty_id));
             rsSQL=pS.executeQuery();
             if (!rsSQL.next())
             {
                 try {rsSQL.close();}catch (Exception ex){}
                 try {pS.close();}catch (Exception ex){}
                 return false;
             }
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
                logger.error("ERROR al BORRAR la identificación:"+sw.toString());
                throw (e);
           }
      }

  /*  public void save(XMLTranslator traductor)
    {
        if(_este == null)
        {
            _este = CI_ResponsibleParty.getDefaultContact();
           // traductor.addWarning("We use the default contact");
        }
        traductor.insertar("individualName", _este.getIndividualName(), null);
        traductor.insertar("organisationName", _este.getOrganisationName(), null);
        traductor.insertar("positionName", _este.getPositionName(), null);
        traductor.insertar_tag_begin("contactInfo");
        traductor.insertar_tag_begin("CI_Contact");
        traductor.insertar_tag_begin("phone");
        traductor.insertar_tag_begin("CI_Telephone");
        traductor.insertar_v("voice", _este.getCi_telephone_voice());
        traductor.insertar_v("facsimile", _este.getCi_telephone_facsimile());
        traductor.insertar_tag_end("CI_Telephone");
        traductor.insertar_tag_end("phone");
        traductor.insertar_tag_begin("address");
        traductor.insertar_tag_begin("CI_Address");
        traductor.insertar_v("deliveryPoint", _este.getDeliveryPoint());
        traductor.insertar("city", _este.getAddressCity(), null);
        traductor.insertar("administrativeArea", _este.getAddressAdministrativeArea(), null);
        traductor.insertar("postalCode", _este.getAddressPostalCode(), null);
        traductor.insertar("country", _este.getAddressCountry(), null);
        traductor.insertar_v("electronicMailAddress", _este.getElectronicMailAdress());
        traductor.insertar_tag_end("CI_Address");
        traductor.insertar_tag_end("address");
        traductor.insertar_tag_begin("onlineResource");
        /*traductor.insertar_tag_begin("CI_OnlineResource");
        traductor.insertar("linkage", _este.getLinkage(), null);
        traductor.insertar("functionCode", _este
        traductor.insertar_tag_end("CI_OnlineResource");*/
    /*    traductor.insertar_tag_end("onlineResource");
        traductor.insertar("hoursOfService", _este.getHoursOfService(), null);
        traductor.insertar("contactInstructions", _este.getContactInstructions(), null);
        traductor.insertar_tag_end("CI_Contact");
        traductor.insertar_tag_end("contactInfo");
        traductor.insertar("role", _este.getRole(), null);
    } */



}
