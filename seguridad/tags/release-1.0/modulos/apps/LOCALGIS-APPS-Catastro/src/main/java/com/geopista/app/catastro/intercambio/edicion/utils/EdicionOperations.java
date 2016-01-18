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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 *
 */


package com.geopista.app.catastro.intercambio.edicion.utils;


/**
 * Definición de métodos para la realización de operaciones sobre base de 
 * datos
 * 
 * @author cotesa
 *
 */

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.geopista.app.AppConstants;
import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.exception.DataException;
import com.geopista.app.catastro.model.beans.BienInmuebleJuridico;
import com.geopista.app.catastro.model.intercambio.importacion.xml.contents.UnidadDatosIntercambio;
import com.geopista.app.catastro.intercambio.importacion.xml.handlers.ImagenXMLHandler;
import com.geopista.app.catastro.intercambio.importacion.xml.handlers.SituacionXMLHandler;
import com.geopista.app.catastro.model.beans.CategoriaCultivo;
import com.geopista.app.catastro.model.beans.Escalera;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.ImagenCatastro;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Paraje;
import com.geopista.app.catastro.model.beans.Planta;
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.catastro.model.beans.Puerta;
import com.geopista.app.catastro.model.beans.TipoDestino;
import com.geopista.app.catastro.model.beans.TipologiaLocal;
import com.geopista.app.catastro.model.beans.Tramo;
import com.geopista.app.catastro.model.beans.ZonaUrbanistica;
import com.geopista.protocol.catastro.Via;
import com.geopista.protocol.datosPersonales.DatosPersonales;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class EdicionOperations
{
    /**
     * Conexión a base de datos
     */
    public Connection conn = null;
    /**
     * Conexión a base de datos sin pasar por el administrador de cartografía
     */
    public static Connection directConn = null;
    
    /**
     * Contexto de la aplicación
     */
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    /**
     * Locale que identifica el idioma del usuario
     */
    private String locale = aplicacion.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY, AppConstants.DEFAULT_LOCALE, false);
    public static Set familiasModificadas = new HashSet(); 

    
    /**
     * Constructor por defecto
     *
     */
    public EdicionOperations()
    {        
        try
        {
            conn = getDBConnection();
        }
        catch(Exception e)
        { 
            e.printStackTrace();
        }        
    }    
  
    
    /**
     * Obtiene una conexión a la base de datos
     * @return Conexión a la base de datos
     * @throws SQLException
     */
    private static Connection getDBConnection () throws SQLException
    {        
        Connection con=  aplicacion.getConnection();
        con.setAutoCommit(false);
        return con;
    }  
    
    
    /**
     * Obtiene un arraylist de objetos de tipo Via con todas las vías de catastro
     * del sistema que comienzan con nomVia
     * 
     * @param String con el nombre de la vía o los caracteres con los que comienza su nombre
     * @return ArrayList de objetos Via
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public ArrayList obtenerViasCatastro (String nomVia) throws DataException
    {        
        ArrayList lstVias = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerViasCatastro");
            
            s.setString(1, nomVia+"%");
            s.setInt(2,AppContext.getIdMunicipio());
            
            r = s.executeQuery();  
            while (r.next())
            {
                Via via = new Via();
                via.setId(r.getString(1));
                via.setTipoViaNormalizadoCatastro(r.getString(2));
                via.setNombreCatastro(r.getString(3));
                via.setCodigoCatastro(r.getString(4));
                via.setMunicipio(r.getString(5));
                Integer codIne = r.getInt(6);
                via.setCodigoIne(codIne.toString());
                via.setIdMunicipio(r.getInt(7));
                lstVias.add(via); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            Comparator viasComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    Via v1 = (Via) o1;
                    Via v2 = (Via) o2;
                    
                    String desc1 = v1.getNombreCatastro();
                    String desc2 = v2.getNombreCatastro();                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);                    
                }
            };
            
           
            Collections.sort(lstVias, viasComparator);
            
            
        }
        catch (SQLException ex)
        {           
            throw new DataException(ex);
        }
        
        return lstVias;
    }
    
    public ArrayList obtenerViasINE (String nomVia) throws DataException
    {        
        ArrayList lstVias = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerViasINE");
            s.setInt(1,AppContext.getIdMunicipio());
            s.setString(2, nomVia+"%");
            
            r = s.executeQuery();  
            while (r.next())
            {
                Via via = new Via();
                via.setId(r.getString(1));
                via.setTipoViaNormalizadoCatastro(r.getString(2));
                via.setNombreCatastro(r.getString(3));
                via.setMunicipio(r.getString(4));
                lstVias.add(via); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            Comparator viasComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    Via v1 = (Via) o1;
                    Via v2 = (Via) o2;
                    
                    String desc1 = v1.getNombreCatastro();
                    String desc2 = v2.getNombreCatastro();                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);                    
                }
            };
            
           
            Collections.sort(lstVias, viasComparator);
            
            
        }
        catch (SQLException ex)
        {           
            throw new DataException(ex);
        }
        
        return lstVias;
    }
    
    /**
     * Obtiene un arraylist de objetos de tipo Via con todas las vías de catastro
     * del sistema que comienzan con nomVia y para un tipoVia determinado
     * 
     * @param String con el nombre de la vía o los caracteres con los que comienza su nombre
     * @param String con el nombre del tipo de la vía o los caracteres con los que comienza su nombre
     * @return ArrayList de objetos Via
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public ArrayList obtenerViasCatastro (String nomVia, String tipoVia) throws DataException
    {        
        ArrayList lstVias = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerViasPorTipoCatastro");
            s.setString(1, nomVia+"%");
            s.setString(2, tipoVia);
            s.setInt(3,AppContext.getIdMunicipio());
            
            r = s.executeQuery();  
            while (r.next())
            {
                Via via = new Via();
                via.setId(r.getString(1));
                via.setTipoViaNormalizadoCatastro(r.getString(2));
                via.setNombreCatastro(r.getString(3));
                via.setCodigoCatastro(r.getString(4));
                via.setMunicipio(r.getString(5));
                Integer codIne = r.getInt(6);
                via.setCodigoIne(codIne.toString());
                via.setIdMunicipio(r.getInt(7));
                lstVias.add(via); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            Comparator viasComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    Via v1 = (Via) o1;
                    Via v2 = (Via) o2;
                    
                    String desc1 = v1.getNombreCatastro();
                    String desc2 = v2.getNombreCatastro();                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);                    
                }
            };
            
           
            Collections.sort(lstVias, viasComparator);
            
            
        }
        catch (SQLException ex)
        {           
            throw new DataException(ex);
        }
        
        return lstVias;
    }
    
    public ArrayList obtenerViasCatastroMunic (String nomVia, String idMunicipio) throws DataException
    {        
        ArrayList lstVias = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerViasPorMunicipio");
//            s.setInt(1,AppContext.getIdMunicipio());
            s.setString(1, nomVia+"%");
            s.setString(2, idMunicipio);
            
            r = s.executeQuery();  
            while (r.next())
            {
                Via via = new Via();
                via.setId(r.getString(1));
                via.setTipoViaNormalizadoCatastro(r.getString(2));
                via.setNombreCatastro(r.getString(3));
                via.setCodigoCatastro(r.getString(4));
                via.setMunicipio(r.getString(5));
                lstVias.add(via); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            Comparator viasComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    Via v1 = (Via) o1;
                    Via v2 = (Via) o2;
                    
                    String desc1 = v1.getNombreCatastro();
                    String desc2 = v2.getNombreCatastro();                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);                    
                }
            };
            
           
            Collections.sort(lstVias, viasComparator);
            
            
        }
        catch (SQLException ex)
        {           
            throw new DataException(ex);
        }
        
        return lstVias;
    }
    
    public ArrayList obtenerViasCatastroCodigoVia (String nomVia, String tipoVia, int codigoVia) throws DataException
    { 
    	ArrayList lstVias = new ArrayList();
    	 try
         { 
	    	 PreparedStatement s = null;
	         ResultSet r = null;
	         
	         s = conn.prepareStatement("MCobtenerViasPorCodigoVia");
	         s.setString(1, nomVia+"%");
	         s.setString(2, tipoVia+"%");
	         Integer codiVia = new Integer(codigoVia);
	         s.setString(3, codiVia.toString()+"%");
	        
	         r = s.executeQuery();  
	         while (r.next())
	         {
	             Via via = new Via();
	             via.setId(r.getString(1));
	             Integer codIne = r.getInt(2);
	             via.setCodigoIne(codIne.toString());
	             via.setTipoViaNormalizadoCatastro(r.getString(3));
	             via.setNombreCatastro(r.getString(4));
	             via.setMunicipio(r.getString(5));
	             via.setIdMunicipio(r.getInt(6));
	             Integer codCatastro = r.getInt(7);
	             via.setCodigoCatastro(codCatastro.toString());
	             lstVias.add(via); 
	         }        
	         if (s!=null) s.close();
	         if (r!= null) r.close(); 
	         conn.close();
	         
	         Comparator viasComparator = new Comparator(){
	             public int compare(Object o1, Object o2) {
	                 Via v1 = (Via) o1;
	                 Via v2 = (Via) o2;
	                 
	                 String desc1 = v1.getNombreCatastro();
	                 String desc2 = v2.getNombreCatastro();                    
	                 
	                 Collator myCollator=Collator.getInstance(new Locale(locale));
	                 myCollator.setStrength(Collator.PRIMARY);
	                 return myCollator.compare(desc1, desc2);                    
	             }
	         };
         
        
         Collections.sort(lstVias, viasComparator);
         
         }
         catch (SQLException ex)
         {           
             throw new DataException(ex);
         }
    	return lstVias;
    }
    
    
    public Municipio obtenerMunicipioPorId (Integer idMunicipio) throws DataException
    { 
    	Municipio municipio = new Municipio();
		try
		{            
			PreparedStatement s = null;
	        ResultSet r = null;
	            
	        s = conn.prepareStatement("MCobtenerIdProvinciaPorIdMunicipio");
	        s.setInt(1,idMunicipio.intValue());
	        r = s.executeQuery(); 
	        while (r.next())
            {
	        	
	        	Provincia provincia = new Provincia();     
	        	provincia.setIdProvincia(r.getString(1));
	        	municipio.setProvincia(provincia);
	        	
            }
	        
	        if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
	        
		}
		catch (SQLException ex)
		{           
			throw new DataException(ex);
		}
        return municipio;
    }
    
    public ArrayList obtenerViasCatastro (String nomVia, String tipoVia, String idMunicipio) throws DataException
    {        
        ArrayList lstVias = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerViasPorTipoCatastroMunic");
            s.setString(1,idMunicipio);
            s.setString(2, nomVia+"%");
            s.setString(3, tipoVia);
            
            byte[] a;
            
            r = s.executeQuery();  
            while (r.next())
            {
                Via via = new Via();
                via.setId(r.getString(1));
                via.setTipoViaNormalizadoCatastro(r.getString(2));
                via.setNombreCatastro(r.getString(3));
                via.setCodigoCatastro(r.getString(4));
                via.setMunicipio(r.getString(5));
                lstVias.add(via); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            Comparator viasComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    Via v1 = (Via) o1;
                    Via v2 = (Via) o2;
                    
                    String desc1 = v1.getNombreCatastro();
                    String desc2 = v2.getNombreCatastro();                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);                    
                }
            };
            
           
            Collections.sort(lstVias, viasComparator);
            
            
        }
        catch (SQLException ex)
        {           
            throw new DataException(ex);
        }
        
        return lstVias;
    }
    
    public ArrayList obtenerViasINE (String nomVia, String tipoVia) throws DataException
    {        
        ArrayList lstVias = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerViasPorTipoINE");
            s.setInt(1,AppContext.getIdMunicipio());
            s.setString(2, nomVia+"%");
            s.setString(3, tipoVia);
            
            r = s.executeQuery();  
            while (r.next())
            {
                Via via = new Via();
                via.setId(r.getString(1));
                via.setTipoViaNormalizadoCatastro(r.getString(2));
                via.setNombreCatastro(r.getString(3));
                lstVias.add(via); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            Comparator viasComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    Via v1 = (Via) o1;
                    Via v2 = (Via) o2;
                    
                    String desc1 = v1.getNombreCatastro();
                    String desc2 = v2.getNombreCatastro();                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);                    
                }
            };
            
           
            Collections.sort(lstVias, viasComparator);
            
            
        }
        catch (SQLException ex)
        {           
            throw new DataException(ex);
        }
        
        return lstVias;
    }
    
    /**
     * Obtiene un arraylist de objetos de tipo Via con todas las vías de catastro
     * del sistema que comienzan con nomVia
     * 
     * @param String con el nombre de la vía o los caracteres con los que comienza su nombre
     * @return ArrayList de objetos Via
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public ArrayList obtenerParajesCatastro (String nomParaje) throws DataException
    {        
        ArrayList lstParajes = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerParajesCatastro");
            s.setInt(1,AppContext.getIdMunicipio());
            s.setString(2, nomParaje+"%");
            
            r = s.executeQuery();  
            while (r.next())
            {   
            	Paraje paraje = new Paraje();
            	paraje.setNombre(r.getString(1));
            	paraje.setCodigo(r.getString(2));
                //lstParajes.add(r.getString(1));
            	lstParajes.add(paraje);
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            Comparator parajesComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    Paraje p1 = (Paraje) o1;
                    Paraje p2 = (Paraje) o2;
                    
                    String cod1 = p1.getCodigo();
                    String cod2 = p2.getCodigo();
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(cod1, cod2);                    
                }
            };
            
           
            Collections.sort(lstParajes, parajesComparator);
            
            
        }
        catch (SQLException ex)
        {           
            throw new DataException(ex);
        }
        
        return lstParajes;
    }
    
    public String obtenerDxf (long idExpediente, String refCatastral) throws DataException
    {        
        String dxf  = null;
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCselectDXFBase64");
            s.setLong(1,idExpediente);
            s.setString(2, refCatastral);
            
            r = s.executeQuery();  
            while (r.next())
            {   
                dxf = r.getString(1); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
        }
        catch (SQLException ex)
        {           
            throw new DataException(ex);
        }
        
        return dxf;
    }
    
    
    public ArrayList buscarImagenesFinca(String refCatastral) 	throws Exception
    {
    	ArrayList lstImagenes = new ArrayList();
    	
    	PreparedStatement s = null;
		ResultSet r = null;
		String idFinca = null;
		
		Connection conn = getDBConnection();
		
		//Antes de nada, se comprueba si la finca a la que se quiere asociar informacion
		//gráfica está dada de alta en catastro real
		s = conn.prepareStatement("MCexisteFinca");
		s.setString(1, refCatastral);     
		r = s.executeQuery();
		if (r.next())
		    idFinca = r.getString(1);
		
		s=null;
		r=null;
		
		if (idFinca!=null)
		{
			
			s = conn.prepareStatement("MCbuscarImagenesFinca");
		    s.setString(1,idFinca);
		    r = s.executeQuery();
		    
		    while (r.next()){
		    	
		    	ImagenCatastro imagen = new ImagenCatastro();
		    	imagen.setNombre( r.getString(1));
		    	imagen.setExtension( r.getString(2));
		    	imagen.setTipoDocumento( r.getString(3));
		    	imagen.setFoto( r.getString(4));

		    	lstImagenes.add(imagen);
		    	
		    }

		}
		
		if (s!=null) s.close();
		if (r!= null) r.close(); 
		return lstImagenes;
    }
    
    
    public ArrayList obtenerLstImagenes (long idExpediente, String refCatastral) throws DataException
    {        
        String imagenes  = null;
        ArrayList instancias = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCselectImagenes");
            s.setLong(1,idExpediente);
            s.setString(2, refCatastral);
            
            r = s.executeQuery();  
            while (r.next())
            {   
                imagenes = r.getString(1); 
            }  
            
            if (imagenes!=null)
			{

            	try{

            		XMLReader parser = new SAXParser();

            		parser.setFeature("http://apache.org/xml/features/validation/schema", true);
            		parser.setFeature("http://xml.org/sax/features/validation", true);

            		parser.setContentHandler(new ImagenXMLHandler(parser, instancias, "elemf"));
            		parser.parse(new InputSource(new ByteArrayInputStream(imagenes.getBytes())));
            		
            		
            	}
            	catch (Exception e) {
            		System.out.println ("Error al procesar el fichero de imagenes: " 
							+ e.getMessage());
					e.printStackTrace();
            	}            
				  
			}
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
        }
        catch (SQLException ex)
        {           
            throw new DataException(ex);
        }
        
        return instancias;
    }
    
    public String obtenerAsc(long idExpediente, String refCatastral) throws DataException
    {        
        String dxf  = null;
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCselectASCBase64");
            s.setLong(1,idExpediente);
            s.setString(2, refCatastral);
            
            r = s.executeQuery();  
            while (r.next())
            {   
                dxf = r.getString(1); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
        }
        catch (SQLException ex)
        {           
            throw new DataException(ex);
        }
        
        return dxf;
    }
    
    /**
     * Obtiene un arraylist de objetos de tipo Provincia con todas las provincias
     * del sistema
     * 
     * @return ArrayList de objetos Provincia
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public ArrayList obtenerProvincias () 
    {        
        ArrayList lstProvincias = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerProvincias");
            r = s.executeQuery();  
            Provincia provincia = new Provincia();
            lstProvincias.add(provincia);

            while (r.next())
            {
                provincia = new Provincia();
                provincia.setIdProvincia(r.getString(1));
                provincia.setNombreOficial(r.getString(2));
                lstProvincias.add(provincia); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            Comparator provinciasComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    Provincia p1 = (Provincia) o1;
                    Provincia p2 = (Provincia) o2;
                    
                    String desc1 = p1.getNombreOficial();
                    String desc2 = p2.getNombreOficial();                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);                    
                }
            };
                       
            Collections.sort(lstProvincias, provinciasComparator);            
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstProvincias;
    }
    
    public ArrayList obtenerNumerosCargo (String parcelaCatastral) 
    {        
        ArrayList lstNumCargo = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerNumerosCargo");
            s.setString(1, parcelaCatastral);
            r = s.executeQuery();  
            String numCargo = null;
            
            while (r.next())
            {                
            	numCargo = r.getString(1);
            	lstNumCargo.add(numCargo);
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstNumCargo;
    }
    
    public ArrayList obtenerBienes (String parcelaCatastral, String nif) 
    {        
        ArrayList lstBienes = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerBienes");
            s.setString(1, parcelaCatastral);
            s.setString(2,nif);
            r = s.executeQuery();  
            String numCargo = null;
            String digito1 = null;
            String digito2 = null;
            lstBienes.add("");
          
            while (r.next())
            {                
            	numCargo = r.getString("numero_cargo");
            	digito1 = r.getString("digito_control1");
            	digito2 = r.getString("digito_control2");
            	if(!numCargo.equals("") && !digito1.equals("") && !digito2.equals("")){
            		String bien = parcelaCatastral + numCargo + digito1 + digito2;
            		lstBienes.add(bien);
            	}
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstBienes;
    }
    
    /**
     * Obtiene un arraylist de objetos de tipo Municipios con todos los municipios
     * del sistema
     * 
     * @param Provincia Provincia de la que se quieren obtener los municipios
     * @return ArrayList de objetos Municipio
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public ArrayList obtenerMunicipios (Provincia provincia) 
    {        
        ArrayList lstMunicipios = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerMunicipios");
            s.setString(1, provincia.getIdProvincia().substring(0,2));
            r = s.executeQuery();  
            Municipio municipio = new Municipio();
            lstMunicipios.add(municipio); 
            while (r.next())
            {
                municipio = new Municipio();
                municipio.setId(r.getInt(1));
                municipio.getProvincia().setIdProvincia(r.getString(2));
                municipio.setIdCatastro(r.getString(3));
                municipio.setIdIne(r.getString(4));
                municipio.setNombreOficial(r.getString(5));
                municipio.setProvincia(provincia);
                lstMunicipios.add(municipio); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            Comparator municipiosComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    Municipio m1 = (Municipio) o1;
                    Municipio m2 = (Municipio) o2;
                    
                    String desc1 = m1.getNombreOficial();
                    String desc2 = m2.getNombreOficial();                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);                    
                }
            };
                       
            Collections.sort(lstMunicipios, municipiosComparator);            
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstMunicipios;
    }
    
    /**
     * Obtiene un arraylist de objetos de tipo Municipio con todos los municipios
     * del sistema
     * 
     * @return ArrayList de objetos Municipio
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public ArrayList obtenerMunicipios () 
    {        
        ArrayList lstMunicipios = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerMunicipiosTotal");
            r = s.executeQuery();  
            Municipio municipio = new Municipio();
            lstMunicipios.add(municipio); 
            while (r.next())
            {
                municipio = new Municipio();
                municipio.setId(r.getInt(1));
                municipio.getProvincia().setIdProvincia(r.getString(2));
                municipio.setIdCatastro(r.getString(3));
                municipio.setIdIne(r.getString(4));
                municipio.setNombreOficial(r.getString(5));
                lstMunicipios.add(municipio); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            Comparator municipiosComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    Municipio m1 = (Municipio) o1;
                    Municipio m2 = (Municipio) o2;
                    
                    String desc1 = m1.getNombreOficial();
                    String desc2 = m2.getNombreOficial();                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);                    
                }
            };
                       
            Collections.sort(lstMunicipios, municipiosComparator);            
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstMunicipios;
    }


    /**
     * Obtiene un arraylist de objetos de tipo TipoDestino con todos los tipos de destino
     * del sistema
     * 
     * @return ArrayList de objetos TipoDestino
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public ArrayList obtenerTiposDestino () 
    {        
        ArrayList lstTiposDestino = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerTipoDestino");
            r = s.executeQuery();  
            TipoDestino tipoDestino = new TipoDestino();
            lstTiposDestino.add(tipoDestino);

            while (r.next())
            {
                tipoDestino = new TipoDestino();
                tipoDestino.setId(r.getInt(1));
                tipoDestino.setPatron(r.getString(2));
                tipoDestino.setDescripcion(r.getString(3));
                lstTiposDestino.add(tipoDestino); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            Comparator tiposComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    TipoDestino t1 = (TipoDestino) o1;
                    TipoDestino t2 = (TipoDestino) o2;
                    
                    String desc1 = t1.getPatron();
                    String desc2 = t2.getPatron();                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);                    
                }
            };
                       
            Collections.sort(lstTiposDestino, tiposComparator);            
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstTiposDestino;
    }
    
    

    /**
     * Obtiene un arraylist de objetos de tipo TipologiaLocal con todos los tipos de locales
     * del sistema
     * 
     * @return ArrayList de objetos TipologiaLocal
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public ArrayList obtenerTipologiasLocales () 
    {        
        ArrayList lstTipos = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerTipologiaLocal");
            r = s.executeQuery();  
            TipologiaLocal tipoLocal = new TipologiaLocal();
            lstTipos.add(tipoLocal);

            while (r.next())
            {
                tipoLocal = new TipologiaLocal();
                tipoLocal.setId(r.getInt(1));
                tipoLocal.setAnioNormas(r.getInt(2));
                tipoLocal.setPatron(r.getString(3));
                tipoLocal.setDescripcion(r.getString(4));
                lstTipos.add(tipoLocal); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            Comparator tiposComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    TipologiaLocal t1 = (TipologiaLocal) o1;
                    TipologiaLocal t2 = (TipologiaLocal) o2;
                    
                    String desc1 = t1.getPatron();
                    String desc2 = t2.getPatron();                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);                    
                }
            };
                       
            Collections.sort(lstTipos, tiposComparator);            
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstTipos;
    }
    
    public ArrayList obtenerCategoriaCultivo () 
    {        
        ArrayList lstCalifCultivo = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerCategoriaCultivo");
            r = s.executeQuery();  
            CategoriaCultivo califCultivo = new CategoriaCultivo();
            lstCalifCultivo.add(califCultivo);

            while (r.next())
            {
            	califCultivo = new CategoriaCultivo();
            	califCultivo.setId(r.getInt(1));
            	califCultivo.setPatron(r.getString(2));
            	califCultivo.setDescripcion(r.getString(3));
                lstCalifCultivo.add(califCultivo); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            Comparator tiposComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    CategoriaCultivo t1 = (CategoriaCultivo) o1;
                    CategoriaCultivo t2 = (CategoriaCultivo) o2;
                    
                    String desc1 = t1.getPatron();
                    String desc2 = t2.getPatron();                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);                    
                }
            };
                       
            Collections.sort(lstCalifCultivo, tiposComparator);            
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstCalifCultivo;
    }
    
    public ArrayList obtenerEscalera () 
    {        
        ArrayList lstEscalera = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerEscalera");
            r = s.executeQuery();  
            Escalera escalera = new Escalera();
            lstEscalera.add(escalera);

            while (r.next())
            {
            	escalera = new Escalera();
            	escalera.setId(r.getInt(1));
            	escalera.setPatron(r.getString(2));
            	escalera.setDescripcion(r.getString(3));
            	lstEscalera.add(escalera); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            Comparator tiposComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                    Escalera t1 = (Escalera) o1;
                    Escalera t2 = (Escalera) o2;
                    
                    String desc1 = t1.getPatron();
                    String desc2 = t2.getPatron();                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);                    
                }
            };
                       
            Collections.sort(lstEscalera, tiposComparator);            
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstEscalera;
    }
    
    public ArrayList obtenerPlanta () 
    {        
        ArrayList lstPlanta = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerPlanta");
            r = s.executeQuery();  
            Planta planta = new Planta();
            lstPlanta.add(planta);

            while (r.next())
            {
            	planta = new Planta();
            	planta.setId(r.getInt(1));
            	planta.setPatron(r.getString(2));
            	planta.setDescripcion(r.getString(3));
            	lstPlanta.add(planta); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            Comparator tiposComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                	Planta t1 = (Planta) o1;
                	Planta t2 = (Planta) o2;
                    
                    String desc1 = t1.getPatron();
                    String desc2 = t2.getPatron();                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);                    
                }
            };
                       
            Collections.sort(lstPlanta, tiposComparator);            
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstPlanta;
    }
    
    public ArrayList obtenerPuerta () 
    {        
        ArrayList lstPuerta = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerPuerta");
            r = s.executeQuery();  
            Puerta puerta = new Puerta();
            lstPuerta.add(puerta);

            while (r.next())
            {
            	puerta = new Puerta();
            	puerta.setId(r.getInt(1));
            	puerta.setPatron(r.getString(2));
            	puerta.setDescripcion(r.getString(3));
            	lstPuerta.add(puerta); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            Comparator tiposComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                	Puerta t1 = (Puerta) o1;
                	Puerta t2 = (Puerta) o2;
                    
                    String desc1 = t1.getPatron();
                    String desc2 = t2.getPatron();                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);                    
                }
            };
                       
            Collections.sort(lstPuerta, tiposComparator);            
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstPuerta;
    }
    
    public ArrayList obtenerUso () 
    {        
        ArrayList lstUso = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerUso");
            r = s.executeQuery();  
            Tramo uso = new Tramo();
            lstUso.add(uso);

            while (r.next())
            {
            	uso = new Tramo();
            	uso.setId(r.getInt(1));
            	uso.setPatron(r.getString(2));
            	uso.setDescripcion(r.getString(3));
            	lstUso.add(uso); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            Comparator tiposComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                	Tramo t1 = (Tramo) o1;
                	Tramo t2 = (Tramo) o2;
                    
                    String desc1 = t1.getPatron();
                    String desc2 = t2.getPatron();                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);                    
                }
            };
                       
            Collections.sort(lstUso, tiposComparator);            
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstUso;
    }
    
    public ArrayList obtenerAnioPonencia () 
    {        
        ArrayList lstAnioPonencia = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerAnioPonencia");
            r = s.executeQuery();  
            String anioPonencia = new String();
            lstAnioPonencia.add(anioPonencia);

            while (r.next())
            {   
            	anioPonencia = r.getString(1);
                lstAnioPonencia.add(anioPonencia); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstAnioPonencia;
    }

    public ArrayList obtenerPoligonos () 
    {        
        ArrayList lstPoligonos = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerListaPoligonos");
            r = s.executeQuery(); 
            String codigoPoligono = new String();
            lstPoligonos.add(codigoPoligono);

            while (r.next())
            {   
            	codigoPoligono = r.getString(1);
            	lstPoligonos.add(codigoPoligono); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstPoligonos;
    }
    
    public ArrayList obtenerCodigoVias () 
    {        
        ArrayList lstCodigoVias = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerListaCodigoVias");
            r = s.executeQuery();  
            String codigoVia;
            
            while (r.next())
            {   
            	codigoVia = r.getString(1);
            	lstCodigoVias.add(codigoVia); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstCodigoVias;
    }
    
    public ArrayList obtenerCodigoRegistro (String codigoProvincia) 
    {        
        ArrayList lstCodigoRegistro = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerListaCodigoRegistro");
            s.setString(1, codigoProvincia);
            r = s.executeQuery();  
            String codigoRegistro;
            
            while (r.next())
            {   
            	codigoRegistro = r.getString(1);
            	lstCodigoRegistro.add(codigoRegistro); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstCodigoRegistro;
    }
    
    public ArrayList obtenerZonaValor () 
    {        
        ArrayList lstZonaValor = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerListaZonaValor");
            r = s.executeQuery();  
            String codigoZonaValor = new String("");
            lstZonaValor.add(codigoZonaValor);

            while (r.next())
            {   
            	codigoZonaValor = r.getString(1);
            	lstZonaValor.add(codigoZonaValor); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstZonaValor;
    }
    
    public ArrayList obtenerCodigoTramo () 
    {        
        ArrayList lstCodigoTramo = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerCodigoTramo");
            r = s.executeQuery();  
//            String codigoTramo = new String("");
//            String denominacion = null;
            Tramo tramoAux = new Tramo();
            lstCodigoTramo.add(tramoAux);
            

            while (r.next())
            {   
            	Tramo tramo = new Tramo();
            	tramo.setPatron(r.getString("codigo_tramo"));
            	tramo.setDescripcion(r.getString("denominacion"));
            	lstCodigoTramo.add(tramo); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstCodigoTramo;
    }

    
    public ArrayList obtenerZonaUrbanistica () 
    {        
        ArrayList lstZonaUrbanistica = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerZonaUrbanistica");
            r = s.executeQuery();  
            
            ZonaUrbanistica zonaUrbAux = new ZonaUrbanistica();
            
            lstZonaUrbanistica.add(zonaUrbAux);

            while (r.next())
            {   
            	ZonaUrbanistica zonaUrbanistica = new ZonaUrbanistica();
            	zonaUrbanistica.setPatron(r.getString("codigo_zona"));
            	zonaUrbanistica.setDescripcion(r.getString("denominacion"));
            	lstZonaUrbanistica.add(zonaUrbanistica); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();            
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstZonaUrbanistica;
    }

    public ArrayList obtenerPoligonoValoracion () 
    {        
        ArrayList lstPoligonoValoracion = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerListaPoligonoValoracion");
            r = s.executeQuery();
            String codigoPoligonoValoracion = new String();
            lstPoligonoValoracion.add(codigoPoligonoValoracion);

            while (r.next())
            {   
            	codigoPoligonoValoracion = r.getString(1);
            	lstPoligonoValoracion.add(codigoPoligonoValoracion); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        return lstPoligonoValoracion;
    }

    public FincaCatastro obtenerInfoFinca(FincaCatastro fc, long idExpediente)
    {    
    	
    	final ArrayList instancias = new ArrayList();

    	final long idExp = idExpediente;
    	final String refCatastral = fc.getRefFinca().getRefCatastral();

    	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
    	progressDialog.setTitle(I18N.get("Expedientes","finca.panel.CargandoParcela"));
    	progressDialog.report(I18N.get("Expedientes","finca.panel.CargandoInfoParcela"));
    	progressDialog.addComponentListener(new ComponentAdapter()
    	{
    		public void componentShown(ComponentEvent e)
    		{   
    			new Thread(new Runnable()
    			{
    				public void run()
    				{
    					try
    					{
    						try
    						{
    							String strElemf = null;
    							try
    							{            

    								PreparedStatement s = null;
    								ResultSet r = null;

    								s = conn.prepareStatement("MCobtenerElemfTemporal");
    								
    								s.setString(1,refCatastral);
    								s.setLong(2, idExp);
    								r = s.executeQuery();  
    								if (r.next())
    								{
    									strElemf = r.getString(1);
    								}        

    								if (s!=null) s.close();
    								if (r!= null) r.close(); 
    								conn.close();

    							}
    							catch (SQLException ex)
    							{           
    								new DataException(ex);
    							}

    							if (strElemf!=null)
    							{

    								XMLReader parser = new SAXParser();

    								parser.setFeature("http://apache.org/xml/features/validation/schema", true);
    								parser.setFeature("http://xml.org/sax/features/validation", true);

    								parser.setContentHandler(new SituacionXMLHandler(parser, instancias, "elemf"));
    								parser.parse(new InputSource(new ByteArrayInputStream(strElemf.getBytes("UTF-8"))));
    								  
    							}
    						}
    						catch (Exception e)
    						{
    							System.out.println ("Error al procesar el fichero de catastro: " 
    									+ e.getMessage());
    							e.printStackTrace();
    						}

    					} 
    					catch (Exception e)
    					{
    						e.printStackTrace();
    					} 
    					finally
    					{
    						progressDialog.setVisible(false);
    					}
    				}
    			}).start();
    		}
    	});
    	GUIUtil.centreOnWindow(progressDialog);
    	progressDialog.setVisible(true);

    	if (instancias.size()!=0)
    		fc = ((UnidadDatosIntercambio)(instancias.get(0))).getFincaCatastro();

    	return fc;
    }
    
    public BienInmuebleJuridico obtenerInfoBien(BienInmuebleJuridico bij, long idExpediente)
    {         
        String strElemf = null;
        
                
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerElemfTemporal");
            s.setString(1,bij.getBienInmueble().getIdBienInmueble().getIdBienInmueble());
            s.setLong(2, idExpediente);
            r = s.executeQuery();  
            if (r.next())
            {
                strElemf = r.getString(1);
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
        }
        catch (SQLException ex)
        {           
            new DataException(ex);
        }
        
        ArrayList instancias = new ArrayList();
       
        if (strElemf!=null)
        {
            try
            {
                XMLReader parser = new SAXParser();
                
                parser.setFeature("http://apache.org/xml/features/validation/schema", true);
                parser.setFeature("http://xml.org/sax/features/validation", true);
                
                parser.setContentHandler(new SituacionXMLHandler(parser, instancias, "elemf"));
                parser.parse(new InputSource(new ByteArrayInputStream(strElemf.getBytes("UTF-8"))));            
            }
            catch (Exception e)
            {
                System.out.println ("Error al procesar el fichero de catastro: " 
                        + e.getMessage());
                e.printStackTrace();
            }
        }
        
        if (instancias.size()!=0)
        {
            UnidadDatosIntercambio udi=((UnidadDatosIntercambio)(instancias.get(0)));
            
            if ((udi.getLstBienesInmuebles() != null)&&(udi.getLstBienesInmuebles().size()!=0))
            {
                //Sólo habrá información de un bien inmueble
                bij= (BienInmuebleJuridico)udi.getLstBienesInmuebles().get(0);                
            }
        }
        
        return bij;
    }

    public ArrayList obtenerParcelasExpediente(Expediente expedienteSeleccionado)
    {
        ArrayList lstParcelas  = new ArrayList();
        
        if (expedienteSeleccionado!=null && expedienteSeleccionado.getIdExpediente()!=0)
        {
            
        }
        
        return lstParcelas;
    }
    
    /**
     * Obtiene un arraylist de objetos de tipo Persona con todas las titulares
     * del sistema que comienzan con el nif o el nombre introducido
     * 
     * @param String con el nif del titular o los caracteres con los que comienza su nombre
     * @param String con el nombreApellidos del titular o los caracteres con los que comienza su nombre
     * @return ArrayList de objetos DatosPersonales
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public ArrayList obtenerTitulares (String nif, String nombreApellidos) throws DataException
    {        

        ArrayList lstDatosPersonales = new ArrayList();
        
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            s = conn.prepareStatement("MCobtenerTitulares");
            s.setString(1, nif+"%");
            s.setString(2, nombreApellidos+"%");
            
            r = s.executeQuery();  
            while (r.next())
            {
                DatosPersonales datosPersonales = new DatosPersonales();
                datosPersonales.setNif(r.getString(1));
                datosPersonales.setNombreApellidos(r.getString(2));
                datosPersonales.setCodigoProvincia(r.getInt(3));
                datosPersonales.setCodigoMunicipio(r.getInt(4));
                datosPersonales.setEntidadMenor(r.getString(5));
                datosPersonales.setCodigoVia(r.getInt(6));
                datosPersonales.setPrimerNumero(r.getString(7));
                datosPersonales.setPrimeraLetra(r.getString(8));
                datosPersonales.setSegundoNumero(r.getString(9));
                datosPersonales.setSegundoLetra(r.getString(10));
                datosPersonales.setEscalera(r.getString(11));
                datosPersonales.setKilometro(r.getInt(12));
                datosPersonales.setBloque(r.getString(13));
                datosPersonales.setPlanta(r.getString(14));
                datosPersonales.setPuerta(r.getString(15));
                datosPersonales.setDireccionNoEstrucutrada(r.getString(16));
                datosPersonales.setCodigo_postal(r.getString(17));
                datosPersonales.setApartado_correos(r.getInt(18));
                
                lstDatosPersonales.add(datosPersonales); 
            }        
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            conn.close();
            
            Comparator datosPersonalesComparator = new Comparator(){
                public int compare(Object o1, Object o2) {
                	DatosPersonales v1 = (DatosPersonales) o1;
                	DatosPersonales v2 = (DatosPersonales) o2;
                    
                    String desc1 = v1.getNombreApellidos();
                    String desc2 = v2.getNombreApellidos();                    
                    
                    Collator myCollator=Collator.getInstance(new Locale(locale));
                    myCollator.setStrength(Collator.PRIMARY);
                    return myCollator.compare(desc1, desc2);       

                }
            };
            
           
            Collections.sort(lstDatosPersonales, datosPersonalesComparator);
            
            
        }
        catch (SQLException ex)
        {           
            throw new DataException(ex);
        }
        
        return lstDatosPersonales;
    }
}


