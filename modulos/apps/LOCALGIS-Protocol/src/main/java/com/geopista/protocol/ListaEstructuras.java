/**
 * ListaEstructuras.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import com.geopista.app.utilidades.UtilsDrivers;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.metadatos.Capa;
import com.geopista.protocol.metadatos.OperacionesMetadatos;
import com.geopista.sql.GEOPISTAConnection;
import com.vividsolutions.jump.task.TaskMonitor;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 22-jul-2004
 * Time: 9:02:05
 */
public class ListaEstructuras {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ListaEstructuras.class);
    private final String servletName="/CServletDB";
    public static String  ESTRUCTURAS_DIR="dbfiles";

    Hashtable lista ;
    Hashtable listapatrones;
    String dominioPadre;
	private TaskMonitor taskMonitor;
    public ListaEstructuras() {
        lista = new Hashtable();
        listapatrones= new Hashtable();
    }
    public ListaEstructuras(String dominioPadre){
        this.dominioPadre=dominioPadre;
        lista = new Hashtable();
        listapatrones= new Hashtable();

    }

    public Hashtable getLista() {
        return lista;
    }

    public Vector getListaSorted(String locale) {
        if (lista==null) return null;
        Vector listaSorted= new Vector();
        for (Enumeration e=lista.elements();e.hasMoreElements();){
        	DomainNode dNode=(DomainNode)e.nextElement();
        	dNode.setLocale(locale);
            putSorted(listaSorted,dNode,locale);
        }
        return listaSorted;
    }
    /**
     * Devuelve la lista de dominios ordenada por el valor nï¿½merico
     * del patron
     * @return
     */
    public Vector getListaSortedByPatron() {
        if (lista==null) return null;
        Vector listaSorted= new Vector();
        for (Enumeration e=lista.elements();e.hasMoreElements();){
        	DomainNode dNode=(DomainNode)e.nextElement();
            putSorted(listaSorted,dNode);
        }
        return listaSorted;
    }

    public int size()
    {
        if (lista==null) return 0;
        return lista.size();
    }

    public void setLista(Hashtable lista) {
        this.lista = lista;
    }
    public void setListaPatrones(Hashtable lista)
    {
        try
        {
            listapatrones= new Hashtable();
            for (Enumeration e=lista.elements();e.hasMoreElements();)
            {
                DomainNode auxDomainNode=(DomainNode) e.nextElement();
                if (auxDomainNode.getPatron()!=null)
                    listapatrones.put(auxDomainNode.getPatron().toUpperCase(),auxDomainNode);
            }
        }catch (Exception ex)
        {
            StringWriter sw=new StringWriter();
            PrintWriter pw=new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Error al cargar la lista de patrones:"+sw.toString());
        }
    }

    public void add(DomainNode domainNode)
    {
    	
        if (domainNode==null)return;
        lista.put(domainNode.getIdNode(),domainNode);
        if (domainNode.getPatron()!=null)
        listapatrones.put(domainNode.getPatron().toUpperCase(),domainNode);
    }
    public boolean loadFile()
    {
        String systemPath = System.getProperty("user.dir", ".");
        String sNombreFichero=systemPath+File.separator+ESTRUCTURAS_DIR+File.separator+dominioPadre+".xml";
        try
        {
            File f= new File(sNombreFichero);
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String sLine=reader.readLine();
            StringBuffer sb=new StringBuffer("");
            while (sLine != null)
            {
                sb.append(sLine);
                sLine=reader.readLine();
            }
            if (sb.length() ==0)
            {
                logger.warn("Fichero "+sNombreFichero+ " vacio");
                return false;
            }
             
            Reader localReader = new FileReader(f);
            long startMils;
            long endMils;
            
			
			startMils=Calendar.getInstance().getTimeInMillis();
			//System.out.println("Start Time:"+sNombreFichero);
	        ListaEstructuras aux=(ListaEstructuras) Unmarshaller.unmarshal(ListaEstructuras.class,new StringReader(sb.toString()));
			endMils=Calendar.getInstance().getTimeInMillis();
			//logger.info("End Time:"+(endMils-startMils)/1000+" segundos");
			//System.out.println("End Time:"+(endMils-startMils)/1000+" segundos");

			/*startMils=Calendar.getInstance().getTimeInMillis();
			System.out.println("Start Time:"+sNombreFichero);
            ListaEstructuras aux1=(ListaEstructuras) Unmarshaller.unmarshal(ListaEstructuras.class, localReader);
			endMils=Calendar.getInstance().getTimeInMillis();
			logger.info("End Time:"+(endMils-startMils)/1000+" segundos");
			System.out.println("End Time:"+(endMils-startMils)/1000+" segundos");
			*/
			this.setLista(aux.getLista());
            this.setListaPatrones(aux.getLista());
            reader.close();
            return true;
        }catch(Exception ex)
        {
            logger.error("Error al cargar el fichero: "+ sNombreFichero+". Excepcion: "+ex.toString());
            return false;
        }

    }
    public boolean saveFile()
    {

        String systemPath = System.getProperty("user.dir", ".");
        String sNombreFichero=systemPath+File.separator+ESTRUCTURAS_DIR+File.separator+dominioPadre+".xml";
        try
        {
            try{
            File directorio = new File(systemPath+File.separator+ESTRUCTURAS_DIR);
            directorio.mkdirs();}catch(Exception ex){}
            File f= new File(sNombreFichero);
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            StringWriter sw = new StringWriter();
			Marshaller.marshal(this, sw);
            writer.write(sw.toString());
            writer.flush();
            writer.close();
            logger.debug("Estructura del dominio "+dominioPadre+ " salvada correctamente en el fichero: "+sNombreFichero);
            return true;

        }catch(Exception ex)
        {
            logger.error("Error al salvar el fichero: "+ sNombreFichero+". Excepcion: "+ex.toString());
            return false;
        }

        }
    
    public boolean loadDBCem(String sUrl){
    	return loadDBData(sUrl, true);
    }
    
    public boolean loadDB(String sUrl){
    	return loadDBData(sUrl, false);
    }
    
    private boolean loadDBData(String sUrl, boolean withIdParent) {

        if (!com.geopista.security.SecurityManager.isLogged()) return false;
        String sConn = "jdbc:pista:"+sUrl+servletName;

        try {
        	  //Verificamos en primer lugar si el driver esta registrado
        	  UtilsDrivers.registerDriver("com.geopista.sql.GEOPISTADriver");
        	
              Class.forName("com.geopista.sql.GEOPISTADriver");
              
              //Verificamos en primer lugar 

              Connection conn = DriverManager.getConnection(sConn);
              //LCG_PATCH_002
              ((GEOPISTAConnection)conn).setTaskMonitor(taskMonitor);
              // en vez de la sentencia SQL.
              // 'select domains.id,domainnodes.id, domainnodes.pattern, dictionary.locale, dictionary.traduccion
              //  from domains,domainnodes,dictionary where upper(domains.name)=upper(?) and domainnodes.type=? and domainnodes.id_domain=domains.id and domainnodes.id_description= dictionary.id_vocablo
              //  and domainnodes.id_municipio =? order by domainsnodes.id')

              PreparedStatement ps = conn.prepareStatement("getestructuramunicipio");
              ps.setString(1,dominioPadre);
              ps.setInt(2,com.geopista.feature.Domain.CODEDENTRY);
              //CAST8.4
              //ps.setString(3, com.geopista.security.SecurityManager.getIdEntidad());
              ps.setInt(3, Integer.parseInt(com.geopista.security.SecurityManager.getIdEntidad(),10));
              ResultSet  rs =ps.executeQuery();
              boolean hayDatos=true;
              if (!rs.next())
              { 
                  logger.debug("No existe estructura especifica para el dominio :<"+dominioPadre + "> en la entidad "+ com.geopista.security.SecurityManager.getIdEntidad());
                  logger.debug("Comprobamos si hay genï¿½ricos");
                  ps = conn.prepareStatement("getestructura");
                  
                  if(withIdParent)
                	  ps = conn.prepareStatement("getestructuraWithParent");
                  
                  ps.setString(1,dominioPadre);
                  ps.setInt(2,com.geopista.feature.Domain.CODEDENTRY);
                  rs =ps.executeQuery();
                  if (!rs.next())
                  {
                      hayDatos=false;
                      logger.warn("No existe estructura generica para el dominio <"+dominioPadre+">");
                  }
              }
              if (hayDatos)
              {
                   lista=new Hashtable();
                   listapatrones= new Hashtable();
                   DomainNode oldDomainNode=null;
                   if (com.geopista.security.SecurityManager.getIdEntidad()==null)
                	   logger.error("El identificador de entidad es nulo");
                   do{
                         String newIdDomainNode=rs.getString("id_node");
                         if ((oldDomainNode==null) || (!oldDomainNode.getIdNode().equals(newIdDomainNode)))
                         {
                                 oldDomainNode= new DomainNode(rs.getString("id_node"),rs.getString("id_descripcion"),
                                                                  com.geopista.feature.Domain.CODEDENTRY, withIdParent? rs.getString("parentdomain") : null,
                                                                  new Integer(com.geopista.security.SecurityManager.getIdEntidad()).toString(),
                                                                  rs.getString("id_domain"),rs.getString("pattern"));
                                 if (rs.getString("id_descripcion")!=null)
                                    oldDomainNode.addTerm(rs.getString("locale"), rs.getString("traduccion"));
                                 	add(oldDomainNode);
                         }
                         else
                         {
                                 oldDomainNode.addTerm(rs.getString("locale"), rs.getString("traduccion"));
                         }
                    }while (rs.next());
              }
         	  rs.close();
			  ps.close();
			  conn.close();
              return hayDatos;
       } catch (Exception e) {
            StringWriter sw=new StringWriter();
		    PrintWriter pw=new PrintWriter(sw);
	    	e.printStackTrace(pw);
            logger.error("ERROR al recoger la estructura del dominio "+dominioPadre+" conectando a "+sConn+" :"+sw.toString());
        }
        return false;
    }
    

    /*public boolean loadDB(String sUrl)
    {

        if (!com.geopista.security.SecurityManager.isLogged()) return false;
        String sConn = "jdbc:pista:"+sUrl+servletName;

        try {
        	  //Verificamos en primer lugar si el driver esta registrado
        	  UtilsDrivers.registerDriver("com.geopista.sql.GEOPISTADriver");
        	
              Class.forName("com.geopista.sql.GEOPISTADriver");
              
              //Verificamos en primer lugar 

              Connection conn = DriverManager.getConnection(sConn);
              //LCG_PATCH_002
              ((GEOPISTAConnection)conn).setTaskMonitor(taskMonitor);
              // en vez de la sentencia SQL.
              // 'select domains.id,domainnodes.id, domainnodes.pattern, dictionary.locale, dictionary.traduccion
              //  from domains,domainnodes,dictionary where upper(domains.name)=upper(?) and domainnodes.type=? and domainnodes.id_domain=domains.id and domainnodes.id_description= dictionary.id_vocablo
              //  and domainnodes.id_municipio =? order by domainsnodes.id')

              PreparedStatement ps = conn.prepareStatement("getestructuramunicipio");
              ps.setString(1,dominioPadre);
              ps.setInt(2,com.geopista.feature.Domain.CODEDENTRY);
              ps.setString(3, com.geopista.security.SecurityManager.getIdEntidad());
              ResultSet  rs =ps.executeQuery();
              boolean hayDatos=true;
              if (!rs.next())
              { 
                  logger.debug("No existe estructura especifica para el dominio :<"+dominioPadre + "> en la entidad "+ com.geopista.security.SecurityManager.getIdEntidad());
                  logger.debug("Comprobamos si hay genï¿½ricos");
                  ps = conn.prepareStatement("getestructura");
                  ps.setString(1,dominioPadre);
                  ps.setInt(2,com.geopista.feature.Domain.CODEDENTRY);
                  rs =ps.executeQuery();
                  if (!rs.next())
                  {
                      hayDatos=false;
                      logger.warn("No existe estructura generica para el dominio <"+dominioPadre+">");
                  }
              }
              if (hayDatos)
              {
                   lista=new Hashtable();
                   listapatrones= new Hashtable();
                   DomainNode oldDomainNode=null;
                   if (com.geopista.security.SecurityManager.getIdEntidad()==null)
                	   logger.error("El identificador de entidad es nulo");
                   do{
                         String newIdDomainNode=rs.getString("id_node");
                         if ((oldDomainNode==null) || (!oldDomainNode.getIdNode().equals(newIdDomainNode)))
                         {
                                 oldDomainNode= new DomainNode(rs.getString("id_node"),rs.getString("id_descripcion"),
                                                                  com.geopista.feature.Domain.CODEDENTRY, null,
                                                                  new Integer(com.geopista.security.SecurityManager.getIdEntidad()).toString(),
                                                                  rs.getString("id_domain"),rs.getString("pattern"));
                                 if (rs.getString("id_descripcion")!=null)
                                    oldDomainNode.addTerm(rs.getString("locale"), rs.getString("traduccion"));
                                 	add(oldDomainNode);
                         }
                         else
                         {
                                 oldDomainNode.addTerm(rs.getString("locale"), rs.getString("traduccion"));
                         }
                    }while (rs.next());
              }
         	  rs.close();
			  ps.close();
			  conn.close();
              return hayDatos;
       } catch (Exception e) {
            StringWriter sw=new StringWriter();
		    PrintWriter pw=new PrintWriter(sw);
	    	e.printStackTrace(pw);
            logger.error("ERROR al recoger la estructura del dominio "+dominioPadre+" conectando a "+sConn+" :"+sw.toString());
        }
        return false;
    }*/
    
	public boolean loadDB(ArrayList<DomainNode> domainNodes) {

		lista = new Hashtable();
		listapatrones = new Hashtable();
		Iterator<DomainNode> it = domainNodes.iterator();
		while (it.hasNext()) {
			DomainNode domainNode = it.next();
			add(domainNode);
		}
		return true;
    }
    
    private void putSorted(Vector vectorSorted, DomainNode domainNode, String locale)
    {
           boolean bSorted = false;
           for (int i = 0; i < vectorSorted.size(); i++) {
                DomainNode base = (DomainNode)vectorSorted.elementAt(i);
               if (base.compareTo(domainNode,locale) > 0) {
                   vectorSorted.insertElementAt(domainNode, i);
                   bSorted = true;
                   break;
               }
           }
           if (!bSorted) {
               bSorted = true;
               vectorSorted.addElement(domainNode);
           }
       }
    /**
     * Ordena la lista por el valor numerico del patrï¿½n
     * @param url
     * @return
     */
    private void putSorted(Vector vectorSorted, DomainNode domainNode)
    {
           long valorPatron=0;
           try{
        	   valorPatron=Long.parseLong(domainNode.getPatron());
           }catch (Exception e) {
        	   vectorSorted.insertElementAt(domainNode, 0);
		   }
           for (int i = 0; i < vectorSorted.size(); i++) {
                DomainNode base = (DomainNode)vectorSorted.elementAt(i);
                try{
                	if (Long.parseLong(base.getPatron())> valorPatron) {
                		vectorSorted.insertElementAt(domainNode, i);
                		return;
                	}
                }catch (Exception e) {
                	
				}
           }
           vectorSorted.addElement(domainNode);
           
     }
    public boolean loadCapas(String url)
    {

        try
        {
            CResultadoOperacion result=(new OperacionesMetadatos(url)).getCapas();
            if (result.getResultado()&&result.getVector()!=null)
            {
                for (Enumeration e=result.getVector().elements();e.hasMoreElements();)
                {
                     Capa capa= (Capa)e.nextElement();
                     DomainNode auxDomain= new DomainNode();
                     auxDomain.setIdNode(capa.getId());
                     auxDomain.sethDict(capa.gethDict());
                     auxDomain.setIdDes(capa.getIdDes());
                     logger.debug("Aï¿½adiendo la capa: "+capa.getId()+" Nombre:"+ capa.getFirstTerm());
                     add(auxDomain);
                }

            }
            else
            {
                logger.error("Problemas al obtener la lista de capas:"+result.getDescripcion());
            }
            return result.getResultado();
        }
        catch (Exception e) {
            StringWriter sw=new StringWriter();
		    PrintWriter pw=new PrintWriter(sw);
	    	e.printStackTrace(pw);
            logger.error("ERROR al recoger las capas  conectando:"+sw.toString());
        }
        return false;

    }
    public DomainNode getDomainNode(String patron)
    {
        if (patron==null) return null;
        return (DomainNode)listapatrones.get(patron.toUpperCase());
    }
    public DomainNode getDomainNodeById(String idNode)
    {
        if ((idNode==null)||lista==null) return null;
        return (DomainNode) lista.get(idNode);
    }
    public DomainNode getDomainNodeByTraduccion(String traduccion)
    {
        if (traduccion==null) return null;
        for(Enumeration e=lista.elements();e.hasMoreElements();)
        {
             DomainNode auxDomain= (DomainNode) e.nextElement();
             Hashtable hTable=auxDomain.gethDict();
             if (hTable!=null)
             {
                 for (Enumeration ea=hTable.elements();ea.hasMoreElements();)
                 {
                     String auxTerm=(String)ea.nextElement();
                     if (traduccion.equalsIgnoreCase(auxTerm))
                         return auxDomain;
                 }
             }

        }
        return null;
    }
    public String getDominioPadre(){
    	return dominioPadre;
    }
	public void addTaskMonitor(TaskMonitor taskMonitor) {
		this.taskMonitor=taskMonitor;
		
	}

	
}
