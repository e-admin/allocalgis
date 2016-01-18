package com.geopista.server.database;

/**
 * Definición de métodos para la realización de operaciones sobre base de 
 * datos
 * 
 * @author cotesa
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.exception.DataException;
import com.geopista.app.catastro.intercambio.importacion.utils.ParcelaAfectada;
import com.geopista.app.catastro.intercambio.importacion.xml.contents.BienInmuebleJuridico;
import com.geopista.app.catastro.intercambio.importacion.xml.contents.UnidadDatosIntercambio;
import com.geopista.app.catastro.intercambio.importacion.xml.contents.UnidadDatosRetorno;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.ComunidadBienes;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.ElementoReparto;
import com.geopista.app.catastro.model.beans.EntidadGeneradora;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FX_CC;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.IdBienInmueble;
import com.geopista.app.catastro.model.beans.ImagenCatastro;
import com.geopista.app.catastro.model.beans.Persona;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.app.catastro.model.beans.RepartoCatastro;
import com.geopista.app.catastro.model.beans.SueloCatastro;
import com.geopista.app.catastro.model.beans.TipoExpediente;
import com.geopista.app.catastro.model.beans.Titular;
import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;
import com.geopista.app.layerutil.GestorCapas;
import com.geopista.protocol.catastro.Via;
import com.geopista.server.database.CPoolDatabase;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.I18N;


public class ImportacionOperations
{    
    /**
     * Conexión a base de datos
     */
    //public Connection conn = null;
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
    private String locale = aplicacion.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY, GestorCapas.DEFAULT_LOCALE, false);
    public static Set familiasModificadas = new HashSet(); 
    //private static final String pass = aplicacion.getUserPreference("conexion.pass","",false);
    
    private static final int NO_RESULTS  = -1;
    private static final int TAM_CODPRO  =  2;
    private static final int TAM_CODMUNI =  3;
    
    private static final Integer TYPE_VARCHAR = new Integer(Types.VARCHAR);
    private static final Integer TYPE_NUMERIC = new Integer(Types.NUMERIC);
    private static final Integer TYPE_INTEGER = new Integer(Types.INTEGER);
    private static final Integer TYPE_FLOAT = new Integer(Types.FLOAT);
    private static final Integer TYPE_DOUBLE = new Integer(Types.DOUBLE);
    private static final Integer TYPE_BOOLEAN = new Integer(Types.BOOLEAN);
    private static final Integer TYPE_CLOB = new Integer(Types.CLOB);
    private static final Integer TYPE_OTHER = new Integer(Types.OTHER);
    private static final Integer TYPE_LONGVARBINARY = new Integer(Types.LONGVARBINARY);
        
    private static final Log logger = LogFactory.getLog(ImportacionOperations.class);
    
    
    private ArrayList lstValores = new ArrayList();
    private ArrayList lstTipos = new ArrayList();

	private Connection con=null;
    
    
    /**
     * Constructor por defecto
     *
     */
    public ImportacionOperations()
    {        
        try
        {
            //conn = getDBConnection();
        }
        catch(Exception e)
        { 
            logger.error("Error ", e);
        }        
    }
    
    
    /**
     * Obtiene una conexión a la base de datos
     * @return Conexión a la base de datos
     * @throws SQLException
     */
    /*private static Connection getDBConnection () throws SQLException
    {     
    	
        Connection con=  aplicacion.getConnection();
        con.setAutoCommit(false);
        return con;
    }*/  
    
    private Connection getDBConnection () throws SQLException
    {     
    	if(!aplicacion.isLogged()) 
    		throw new SQLException("No Connection");
    	if (con==null)
    	{
    		con=  aplicacion.getConnection();
            con.setAutoCommit(false);
    	}
         
        return con;
    }  
    
   
    /**
     * 
     * @param bienInmuebleNoFin
     * @param esFinalizado
     * @return
     */
    private void insertarDatosBI(BienInmuebleCatastro bienInmuebleNoFin, boolean esFinalizado)
    {     
        //TODO revisar cuando se modifique el modelo de datos (referente a titular, derecho y bienes inmuebles)
        //no se pueden guardar datos en bien_inmueble porque necesita titular
        
    }

    /**
     * 
     * @param lstDatos
     * @return
     * @throws DataException 
     */
    private void insertarListaDatos(ArrayList lstDatos, boolean insertarExpediente, Expediente exp) //throws DataException
    throws Exception
    {
               
        if (lstDatos!=null)
        {
            Iterator it = lstDatos.iterator();
            
            while (it.hasNext())
            {            
                try
                {
                    Object o = it.next();
                                        
                    if (o instanceof SueloCatastro)
                    {
                        insertarDatosSuelo((SueloCatastro)o, insertarExpediente, exp);
                    }
                    else if (o instanceof UnidadConstructivaCatastro)
                    {
                        insertarDatosUC ((UnidadConstructivaCatastro)o, insertarExpediente, exp);
                    }
                    else if (o instanceof BienInmuebleJuridico)
                    {
                        insertarDatosBIJ ((BienInmuebleJuridico)o, insertarExpediente, exp);
                    }
                    else if (o instanceof ConstruccionCatastro)
                    {
                        insertarDatosConstruccion ((ConstruccionCatastro)o, insertarExpediente, exp);
                    }
                    else if (o instanceof Cultivo)
                    {
                        insertarDatosCultivo ((Cultivo)o, insertarExpediente, exp);
                    }
                    else if (o instanceof RepartoCatastro)
                    {
                        insertarDatosReparto ((RepartoCatastro)o, insertarExpediente, exp);
                    }
//                    else if (o instanceof ImagenCatastro)
//                    {
//                    	insertarDatosImagen ((ImagenCatastro)o, insertarExpediente, exp);
//                    }
                    
                } catch (DataException e)
                {    
                    throw new DataException(e);
                	//logger.error("Error ", e);
                   
                }            
            }
        }
        
    }
    
    
    private void insertarDatosBIJ(BienInmuebleJuridico bij, boolean insertarExpediente, Expediente exp) //throws DataException
    throws Exception
    {   
        try
        {   
        	if (exp!=null)
        		bij.setDatosExpediente(exp);

        	if (insertarExpediente && exp!=null)
        		insertarDatosExpediente (exp);  

        	lstValores.clear();
        	lstTipos.clear();

        	PreparedStatement s = null;
        	ResultSet r = null;                  

        	//REPRESENTANTE
        	//Comprobar si existe el representante para ver si es inserción o actualización
        	String idRepresentante = null;
        	Connection conn = getDBConnection();

        	if(bij.getBienInmueble()!=null && 
        			bij.getBienInmueble().getIdBienInmueble()!=null && 
        			bij.getBienInmueble().getIdBienInmueble().getIdBienInmueble()!=null){

        		s = conn.prepareStatement("MCexisteRepresentante");            
        		s.setString(1,bij.getBienInmueble().getIdBienInmueble().getIdBienInmueble());
        		r = s.executeQuery();
        		if (r.next())
        			idRepresentante = r.getString(1);            
        		r=null;
        		s=null;

        		if (bij.getBienInmueble().getRepresentante().getNif()!=null)
        		{
        			int idVia = obtenerIdVia(bij.getBienInmueble().getRepresentante().getDomicilio());
        			if (idVia!=0)
        				bij.getBienInmueble().getRepresentante().getDomicilio().setIdVia(idVia);
        			else
        				bij.getBienInmueble().getRepresentante().getDomicilio().setIdVia(
        						bij.getBienInmueble().getRepresentante().getDomicilio().getCodigoVia());

        			//Se inserta/actualiza la información del representante
        			lstValores.clear();
        			lstTipos.clear();

        			if(idRepresentante!=null)
        			{
        				s = conn.prepareStatement("MCactualizarRepresentante");
        				lstValores.add(bij.getBienInmueble().getRepresentante().getNif());
        				lstTipos.add(TYPE_VARCHAR);
        			}
        			else
        				s = conn.prepareStatement("MCinsertarRepresentante");

        			lstValores.add(new Integer(bij.getBienInmueble().getRepresentante().getExpediente().
        					getAnnoExpedienteAdminOrigenAlteracion()));
        			lstTipos.add(TYPE_NUMERIC);

        			lstValores.add(bij.getBienInmueble().getRepresentante().getExpediente().
        					getReferenciaExpedienteAdminOrigen());
        			lstTipos.add(TYPE_VARCHAR);

        			if(bij.getBienInmueble().getRepresentante().getExpediente().getEntidadGeneradora()!=null)
        				lstValores.add(new Integer(bij.getBienInmueble().getRepresentante().getExpediente().
        						getEntidadGeneradora().getCodigo()));
        			else
        				lstValores.add(null);
        			lstTipos.add(TYPE_VARCHAR);

        			lstValores.add(bij.getBienInmueble().getRepresentante().getRazonSocial());
        			lstTipos.add(TYPE_VARCHAR);

        			lstValores.add(new Integer(bij.getBienInmueble().getRepresentante().
        					getDomicilio().getProvinciaINE()));
        			lstTipos.add(TYPE_NUMERIC);

        			lstValores.add(new Integer(bij.getBienInmueble().getRepresentante().
        					getDomicilio().getCodigoMunicipioDGC()));
        			lstTipos.add(TYPE_NUMERIC);

        			lstValores.add(new Integer(bij.getBienInmueble().getRepresentante().
        					getDomicilio().getMunicipioINE()));
        			lstTipos.add(TYPE_NUMERIC);

        			if (bij.getBienInmueble().getRepresentante().
        					getDomicilio().getNombreEntidadMenor()!=null)                
        				lstValores.add(new Integer(bij.getBienInmueble().getRepresentante().
        						getDomicilio().getNombreEntidadMenor()));
        			else 
        				lstValores.add(null);

        			lstTipos.add(TYPE_VARCHAR);

        			lstValores.add(new Integer(bij.getBienInmueble().getRepresentante().
        					getDomicilio().getIdVia()));
        			lstTipos.add(TYPE_NUMERIC);

        			if (bij.getBienInmueble().getRepresentante().
        					getDomicilio().getPrimerNumero() == -1){
        				lstValores.add(null);
        			}
        			else{
        				lstValores.add(new Integer(bij.getBienInmueble().getRepresentante().
        						getDomicilio().getPrimerNumero()));
        			}
        			lstTipos.add(TYPE_NUMERIC);

        			lstValores.add(bij.getBienInmueble().getRepresentante().
        					getDomicilio().getPrimeraLetra());
        			lstTipos.add(TYPE_VARCHAR);

        			if (bij.getBienInmueble().getRepresentante().
        					getDomicilio().getSegundoNumero() == -1){
        				lstValores.add(null);
        			}
        			else{
	        			lstValores.add(new Integer(bij.getBienInmueble().getRepresentante().
	        					getDomicilio().getSegundoNumero()));
        			}
        			lstTipos.add(TYPE_NUMERIC);

        			lstValores.add(bij.getBienInmueble().getRepresentante().
        					getDomicilio().getSegundaLetra());
        			lstTipos.add(TYPE_VARCHAR);

        			if (bij.getBienInmueble().getRepresentante().
        					getDomicilio().getKilometro() == -1){
        				lstValores.add(null);
        			}
        			else{
        				lstValores.add(new Double(bij.getBienInmueble().getRepresentante().
        						getDomicilio().getKilometro()));
        			}
        			lstTipos.add(TYPE_DOUBLE);

        			lstValores.add(bij.getBienInmueble().getRepresentante().
        					getDomicilio().getBloque());
        			lstTipos.add(TYPE_VARCHAR);

        			lstValores.add(bij.getBienInmueble().getRepresentante().
        					getDomicilio().getEscalera());
        			lstTipos.add(TYPE_VARCHAR);

        			lstValores.add(bij.getBienInmueble().getRepresentante().
        					getDomicilio().getPlanta());
        			lstTipos.add(TYPE_VARCHAR);

        			lstValores.add(bij.getBienInmueble().getRepresentante().
        					getDomicilio().getPuerta());
        			lstTipos.add(TYPE_VARCHAR);

        			lstValores.add(bij.getBienInmueble().getRepresentante().
        					getDomicilio().getDireccionNoEstructurada());
        			lstTipos.add(TYPE_VARCHAR);

        			/* JAVIER Codigo para codigo_postal como tipo int
        			 if (bij.getBienInmueble().getRepresentante().
        					getDomicilio().getCodigoPostal() == -1){
        				lstValores.add(null);
        			}
        			else{
        				lstValores.add(String.valueOf(bij.getBienInmueble().getRepresentante().
        						getDomicilio().getCodigoPostal()));
        			}
        			lstTipos.add(TYPE_VARCHAR);
        			*/
                    
                    lstValores.add(bij.getBienInmueble().getRepresentante().
        						getDomicilio().getCodigoPostal());
        			lstTipos.add(TYPE_VARCHAR);

                    lstValores.add(new Integer(bij.getBienInmueble().getRepresentante().
        					getDomicilio().getApartadoCorreos()));
        			lstTipos.add(TYPE_NUMERIC);

        			lstValores.add(bij.getBienInmueble().getIdBienInmueble().getIdBienInmueble());
        			lstTipos.add(TYPE_VARCHAR);

        			if (idRepresentante!=null)
        				lstValores.add(idRepresentante);
        			else
        				lstValores.add(bij.getBienInmueble().getRepresentante().getNif());

        			lstTipos.add(TYPE_VARCHAR);


        			createStatement(s, lstValores, lstTipos);
        			s.execute();

        			r=null;
        			s=null; 

        		}
        		String idBI = null;

        		//Comprobar si existe el bien inmueble para ver si es inserción o actualización
        		s = conn.prepareStatement("MCexisteBI");
        		s.setString(1,bij.getBienInmueble().getIdBienInmueble().getIdBienInmueble());
        		r = s.executeQuery();
        		if (r.next())
        			idBI = r.getString(1);

        		r=null;
        		s=null;    

        		if(idBI!=null)
        			s = conn.prepareStatement("MCactualizarBI");
        		else
        			s = conn.prepareStatement("MCinsertarBI");


        		//Se busca el identificador de vía
        		int idVia = obtenerIdVia(bij.getBienInmueble().getDomicilioTributario());
        		if (idVia!=0)
        			bij.getBienInmueble().getDomicilioTributario().setIdVia(idVia);


        		//BIEN INMUEBLE
        		//Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
        		//si vienen o no valores nulos de forma más cómoda 
        		lstValores.clear();
        		lstTipos.clear();
        		
        		if (bij.getBienInmueble().getDomicilioTributario() != null){
        			obtenerCodigoParaje(bij.getBienInmueble().getDomicilioTributario());

            		lstValores.clear();
            		lstTipos.clear();
        		}        		

        		lstValores.add(new Integer(bij.getDatosExpediente().getAnnoExpedienteAdminOrigenAlteracion()));
        		lstTipos.add(TYPE_NUMERIC);

        		lstValores.add(bij.getDatosExpediente().getReferenciaExpedienteAdminOrigen());            
        		lstTipos.add(TYPE_VARCHAR);            

        		if(bij.getDatosExpediente().getEntidadGeneradora()!=null)
        			lstValores.add(new Integer(bij.getDatosExpediente().getEntidadGeneradora().getCodigo()));
        		else
        			lstValores.add(null);
        		lstTipos.add(TYPE_VARCHAR);

        		//Tipo movimiento
        		lstValores.add(bij.getBienInmueble().getTipoMovimiento());            
        		lstTipos.add(TYPE_VARCHAR);            

        		lstValores.add(bij.getBienInmueble().getClaseBienInmueble());
        		lstTipos.add(TYPE_VARCHAR);

        		lstValores.add(bij.getBienInmueble().getIdBienInmueble().
        				getParcelaCatastral().getRefCatastral());            
        		lstTipos.add(TYPE_VARCHAR);       

        		lstValores.add(bij.getBienInmueble().getIdBienInmueble().getNumCargo());            
        		lstTipos.add(TYPE_VARCHAR);       

        		lstValores.add(bij.getBienInmueble().getIdBienInmueble().getDigControl1());            
        		lstTipos.add(TYPE_VARCHAR);      

        		lstValores.add(bij.getBienInmueble().getIdBienInmueble().getDigControl2());            
        		lstTipos.add(TYPE_VARCHAR);  

        		lstValores.add(bij.getBienInmueble().getNumFijoInmueble());            
        		lstTipos.add(TYPE_NUMERIC);  



        		lstValores.add(bij.getBienInmueble().getIdAyuntamientoBienInmueble());            
        		lstTipos.add(TYPE_VARCHAR);  

        		if (bij.getBienInmueble().getNumFincaRegistral()!=null)
        			lstValores.add(bij.getBienInmueble().getNumFincaRegistral().getNumFincaRegistral());            
        		else
        			lstValores.add(null);
        		lstTipos.add(TYPE_VARCHAR);  

        		lstValores.add(bij.getBienInmueble().getCodMunicipioDGC());            
        		lstTipos.add(TYPE_VARCHAR);  

        		lstValores.add(bij.getBienInmueble().getNombreEntidadMenor());            
        		lstTipos.add(TYPE_VARCHAR);  

        		lstValores.add(new Integer(idVia));            
        		lstTipos.add(TYPE_NUMERIC);  

        		if (bij.getBienInmueble().getDomicilioTributario().getPrimerNumero() == -1){
        			lstValores.add(null);
        		}
        		else{
        			lstValores.add(new Integer(bij.getBienInmueble().getDomicilioTributario().getPrimerNumero()));
        		}
        		lstTipos.add(TYPE_NUMERIC);  

        		lstValores.add(bij.getBienInmueble().getDomicilioTributario().getPrimeraLetra());            
        		lstTipos.add(TYPE_VARCHAR);  

        		if (bij.getBienInmueble().getDomicilioTributario().getSegundoNumero() == -1){
        			lstValores.add(null);
        		}
        		else{
        			lstValores.add(new Integer(bij.getBienInmueble().getDomicilioTributario().getSegundoNumero()));
        		}
        		lstTipos.add(TYPE_NUMERIC);  

        		lstValores.add(bij.getBienInmueble().getDomicilioTributario().getSegundaLetra());            
        		lstTipos.add(TYPE_VARCHAR);  

        		if (bij.getBienInmueble().getDomicilioTributario().getKilometro() == -1){
        			lstValores.add(null);
        		}
        		else{
        			lstValores.add(new Double(bij.getBienInmueble().getDomicilioTributario().getKilometro()));
        		}
        		lstTipos.add(TYPE_DOUBLE);  



        		lstValores.add(bij.getBienInmueble().getDomicilioTributario().getBloque());            
        		lstTipos.add(TYPE_VARCHAR);  

        		lstValores.add(bij.getBienInmueble().getDomicilioTributario().getEscalera());            
        		lstTipos.add(TYPE_VARCHAR);  

        		lstValores.add(bij.getBienInmueble().getDomicilioTributario().getPlanta());            
        		lstTipos.add(TYPE_VARCHAR); 

        		lstValores.add(bij.getBienInmueble().getDomicilioTributario().getPuerta());            
        		lstTipos.add(TYPE_VARCHAR); 

        		lstValores.add(bij.getBienInmueble().getDomicilioTributario().getDireccionNoEstructurada());            
        		lstTipos.add(TYPE_VARCHAR); 

                /* JAVIER si codigo postal es tipo int
        		if (bij.getBienInmueble().getDomicilioTributario().getCodigoPostal() == -1){
        			lstValores.add(null);
        		}
        		else{
        			lstValores.add(new Integer(bij.getBienInmueble().getDomicilioTributario().getCodigoPostal()));
        		}
        		lstTipos.add(TYPE_NUMERIC); 
                */

                lstValores.add(bij.getBienInmueble().getDomicilioTributario().getCodigoPostal());
                lstTipos.add(TYPE_VARCHAR);

                lstValores.add(bij.getBienInmueble().getDomicilioTributario().getDistrito());
        		lstTipos.add(TYPE_VARCHAR); 

        		lstValores.add(bij.getBienInmueble().getDomicilioTributario().getCodMunOrigenAgregacion());            
        		lstTipos.add(TYPE_VARCHAR); 

        		lstValores.add(bij.getBienInmueble().getDomicilioTributario().getCodZonaConcentracion());            
        		lstTipos.add(TYPE_VARCHAR); 

        		lstValores.add(bij.getBienInmueble().getDomicilioTributario().getCodPoligono());            
        		lstTipos.add(TYPE_VARCHAR); 

        		lstValores.add(bij.getBienInmueble().getDomicilioTributario().getCodParcela());            
        		lstTipos.add(TYPE_VARCHAR);  

        		lstValores.add(bij.getBienInmueble().getDomicilioTributario().getCodParaje());            
        		lstTipos.add(TYPE_VARCHAR);      

        		lstValores.add(bij.getBienInmueble().getDomicilioTributario().getNombreParaje());
        		lstTipos.add(TYPE_VARCHAR);

        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getPrecioDeclarado());            
        		lstTipos.add(TYPE_DOUBLE); 

        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getOrigenPrecioDeclarado());            
        		lstTipos.add(TYPE_VARCHAR); 

        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getPrecioVenta());            
        		lstTipos.add(TYPE_DOUBLE); 

        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getAnioFinValoracion());            
        		lstTipos.add(TYPE_NUMERIC); 

        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getIndTipoPropiedad());            
        		lstTipos.add(TYPE_VARCHAR); 

        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getNumOrdenHorizontal());            
        		lstTipos.add(TYPE_VARCHAR); 


        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getAnioAntiguedad());            
        		lstTipos.add(TYPE_NUMERIC); 




        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getAnioValorCat());            
        		lstTipos.add(TYPE_NUMERIC); 

        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getValorCatastral());            
        		lstTipos.add(TYPE_NUMERIC); 

        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getValorCatastralSuelo());            
        		lstTipos.add(TYPE_NUMERIC); 

        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getValorCatastralConstruccion());            
        		lstTipos.add(TYPE_NUMERIC); 

        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getBaseLiquidable());            
        		lstTipos.add(TYPE_INTEGER); 

        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getUso());            
        		lstTipos.add(TYPE_VARCHAR); 

        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getImporteBonificacionRustica());            
        		lstTipos.add(TYPE_NUMERIC); 

        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getClaveBonificacionRustica());            
        		lstTipos.add(TYPE_VARCHAR); 

        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getSuperficieCargoFincaConstruida());            
        		lstTipos.add(TYPE_NUMERIC); 

        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getSuperficieCargoFincaRustica());            
        		lstTipos.add(TYPE_NUMERIC); 




        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getCoefParticipacion());            
        		lstTipos.add(TYPE_FLOAT); 

        		lstValores.add(bij.getBienInmueble().getDatosBaseLiquidable().getValorBase());            
        		lstTipos.add(TYPE_NUMERIC); 

        		lstValores.add(bij.getBienInmueble().getDatosBaseLiquidable().getProcedenciaValorBase());            
        		lstTipos.add(TYPE_VARCHAR); 

        		lstValores.add(bij.getBienInmueble().getDatosBaseLiquidable().getEjercicioIBI());            
        		lstTipos.add(TYPE_NUMERIC); 

        		lstValores.add(bij.getBienInmueble().getDatosBaseLiquidable().getValorCatastralIBI());            
        		lstTipos.add(TYPE_NUMERIC); 



        		lstValores.add(bij.getBienInmueble().getDatosBaseLiquidable().getEjercicioRevTotal());            
        		lstTipos.add(TYPE_NUMERIC); 

        		lstValores.add(bij.getBienInmueble().getDatosBaseLiquidable().getEjercicioRevParcial());            
        		lstTipos.add(TYPE_NUMERIC); 

        		lstValores.add(bij.getBienInmueble().getDatosBaseLiquidable().getPeriodoTotal());            
        		lstTipos.add(TYPE_NUMERIC); 


        		//Id representante
        		lstValores.add(bij.getBienInmueble().getRepresentante()!=null?
        				bij.getBienInmueble().getRepresentante().getNif():null);            
        		lstTipos.add(TYPE_VARCHAR); 

        		lstValores.add(bij.getBienInmueble().getIdBienInmueble().getIdBienInmueble());            
        		lstTipos.add(TYPE_VARCHAR); 

        		createStatement(s, lstValores, lstTipos);

        		s.execute();


        		//TITULARES
        		//Se comprueba si existen titulares y se actualiza su información
        		insertarDatosTitulares (
        				bij.getBienInmueble(),
        				bij.getLstTitulares());


        		//COMUNIDADES DE BIENES
        		//Se comprueba si existen comunidades de bienes y se actualiza su información
        		insertarDatosComunidadesBienes (
        				bij.getBienInmueble(),
        				bij.getLstComBienes());

        	}

        	if (s!=null) s.close();
        	if (r!= null) r.close(); 
        	conn.close();

        }
        catch (DataException ex)
        {           
        	throw new DataException(I18N.get("Importacion","importar.error.bienes"), ex);
        	//logger.error("Error ", ex);
        }        
    }

    
    /**
     * Inserta o actualiza la lista de comunidades de bien existenes en
     * la titularidad de determinado bien inmueble
     * 
     * @param idBi Identificador del bien inmueble del que son titulares los 
     * integrantes de la lista de titulares
     * @param lstCBs ArrayList con la lista de comunidades de bienes
     * del bien inmueble
     * 
     * @throws DataException 
     */
    private void insertarDatosComunidadesBienes(BienInmuebleCatastro bic, ArrayList lstCBs)
    //throws DataException
    //throws DataException
    throws Exception
    {
        if(lstCBs != null){
            Iterator it = lstCBs.iterator();
            while (it.hasNext())
            {
                Object persona = it.next();
                if (persona instanceof ComunidadBienes)
                    insertarDatosPersona (bic.getIdBienInmueble(), (ComunidadBienes)persona, true);
            }
        }
    }
    
    
    /**
     * Inserta o actualiza la lista de titulares de determinado bien inmueble
     * 
     * @param idBi Identificador del bien inmueble del que son titulares los 
     * integrantes de la lista de titulares
     * @param lstTitulares ArrayList con la lista de titulares del bien inmueble
     * 
     * @throws DataException 
     */
    private void insertarDatosTitulares(BienInmuebleCatastro bic, ArrayList lstTitulares)
    //throws DataException
    throws Exception
    {
        //Eliminar los antiguos titulares del bien inmueble (conformarían una situación
        //inicial de titularidad) e insertar los nuevos titulares (situación final)
        if (lstTitulares.size()!=0)
        {
            eliminarDatosTitulares(bic.getIdBienInmueble());
        }        
        
        Iterator it = lstTitulares.iterator();        
        while (it.hasNext())
        {
            Object persona = it.next();
            if (persona instanceof Persona)
                insertarDatosPersona (bic.getIdBienInmueble(), (Persona)persona, false);
        }                
      
    }
    
    /**
     * Elimina la situación inicial de titularidad de un bien inmueble
     * @param idBi Identificador del bien inmueble    
     * @throws DataException
     */
    private void eliminarDatosTitulares(IdBienInmueble idBi) throws DataException
    {
        try
        {            
            PreparedStatement s = null;
            Connection conn = getDBConnection();
            s = conn.prepareStatement("MCborrarSituacionInicialTitularidad");
            lstValores.clear();
        	lstTipos.clear();
            
            lstValores.add(idBi.getParcelaCatastral().getRefCatastral());
            lstTipos.add(TYPE_VARCHAR);
            lstValores.add(idBi.getNumCargo());
            lstTipos.add(TYPE_VARCHAR);
            lstValores.add(idBi.getDigControl1());
            lstTipos.add(TYPE_VARCHAR);
            lstValores.add(idBi.getDigControl2());
            lstTipos.add(TYPE_VARCHAR);
            createStatement(s, lstValores, lstTipos);           
            s.executeUpdate();            
            
            s=null;
            s = conn.prepareStatement("MCborrarDerechoBI");
            lstValores = new ArrayList();
            lstTipos = new ArrayList();
            lstValores.add(idBi.getIdBienInmueble());
            lstTipos.add(TYPE_VARCHAR);
            createStatement(s, lstValores, lstTipos);
            s.executeUpdate();            
            
            if (s!=null) s.close();
            // conn.close();             
        }
        catch (Exception ex)
        {           
            throw new DataException(I18N.get("Importacion","importar.error.eliminar.titulares"), ex);
        	//logger.error("Error ", ex);
        }
        
        
    }

    private void insertarDatosPersona(IdBienInmueble idBi, Persona pers, boolean canUpdate) //throws DataException
    throws Exception
    {
        //canUpdate=true;
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            
            
            //Será true cuando se puedan realizar actualizaciones de personas ya 
            //insertadas, por ejemplo, desde padrón catastral
            if (canUpdate)
            {
                //Comprueba si existe la persona
            	Connection conn = getDBConnection();
                s = conn.prepareStatement("MCexistePersona");
                s.setString(1, pers.getNif());
                s.setString(2,idBi.getParcelaCatastral().getRefCatastral());
                s.setString(3,idBi.getNumCargo());
                s.setString(4,idBi.getDigControl1());
                s.setString(5,idBi.getDigControl2());
                
                r = s.executeQuery();
                if (r.next())
                {
                    s=null;
                    s = conn.prepareStatement("MCactualizarPersona"); 
                }
                else
                {
                    s=null;
                    if (pers.getAusenciaNIF()==null){
                    	s = conn.prepareStatement("MCinsertarPersona");
                    }
                    else{
                    	Long nifAuxiliar = null;
                    	s = conn.prepareStatement("MCnextSecuenciaNifPersona");
                    	r = s.executeQuery();
                    	if (r.next()){
                    		nifAuxiliar = new Long(r.getLong(1));    
                    		pers.setNif(nifAuxiliar.toString());
                    	}
                    	s=null;
                    	r=null;
                    	s = conn.prepareStatement("MCinsertarPersona");  
                    }
                }
            }
            else
            {
            	Connection conn = getDBConnection();
            	s = conn.prepareStatement("MCexistePersona");
            	s.setString(1, pers.getNif());
            	s.setString(2,idBi.getParcelaCatastral().getRefCatastral());
            	s.setString(3,idBi.getNumCargo());
            	s.setString(4,idBi.getDigControl1());
            	s.setString(5,idBi.getDigControl2());

            	r = s.executeQuery();
            	if (r.next())
            	{
            		s=null;
            		s = conn.prepareStatement("MCactualizarPersona"); 
            	}
            	else
            	{
//          		Connection conn = getDBConnection();
            		s = conn.prepareStatement("MCinsertarPersona");
            		if (pers.getAusenciaNIF()!=null){
            			Long nifAuxiliar = null;
            			s = conn.prepareStatement("MCnextSecuenciaNifPersona");
            			r = s.executeQuery();
            			if (r.next()){
            				nifAuxiliar = new Long(r.getLong(1));    
            				pers.setNif(nifAuxiliar.toString());
            			}
            			s=null;
            			r=null;
            			s = conn.prepareStatement("MCinsertarPersona");                    
            		}
            	}
                          
            }
            r=null;
            
            //Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
            //si vienen o no valores nulos de forma más cómoda 
            
            int idVia = obtenerIdVia(pers.getDomicilio());
            
            lstValores.clear();
        	lstTipos.clear();

        	            
            lstValores.add(new Integer(pers.getExpediente().getAnnoExpedienteAdminOrigenAlteracion()));
            lstTipos.add(TYPE_NUMERIC);
            
            lstValores.add(pers.getExpediente().getReferenciaExpedienteAdminOrigen());            
            lstTipos.add(TYPE_VARCHAR);            
            
            if (pers.getExpediente().getEntidadGeneradora()!=null)
                lstValores.add (new Integer(pers.getExpediente().getEntidadGeneradora().getCodigo()));
            else
                lstValores.add (null);
            lstTipos.add(TYPE_NUMERIC);
            
            lstValores.add(pers.getRazonSocial());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(pers.getAusenciaNIF());            
            lstTipos.add(TYPE_VARCHAR);  
            
            if (pers.getDomicilio().getProvinciaINE()==null){
            	lstValores.add(null);
            }
            else{
            	lstValores.add(new Integer(pers.getDomicilio().getProvinciaINE()));
            }
            lstTipos.add(TYPE_NUMERIC);  
            
            if (pers.getDomicilio().getCodigoMunicipioDGC()==null){
            	lstValores.add(null);
            }
            else{
            	lstValores.add(new Integer(pers.getDomicilio().getCodigoMunicipioDGC()));
            }
            lstTipos.add(TYPE_NUMERIC);  
            
            if (pers.getDomicilio().getMunicipioINE()==null){
            	lstValores.add(null);
            }
            else{
            	lstValores.add(new Integer(pers.getDomicilio().getMunicipioINE()));
            }
            lstTipos.add(TYPE_NUMERIC);  
            
            lstValores.add(pers.getCodEntidadMenor());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(new Integer(idVia));            
            lstTipos.add(TYPE_NUMERIC);  
            
            if (pers.getDomicilio().getPrimerNumero() == -1){
            	lstValores.add(null);
            }
            else{
            	lstValores.add(new Integer(pers.getDomicilio().getPrimerNumero()));
            }
            lstTipos.add(TYPE_NUMERIC);  
            
            lstValores.add(pers.getDomicilio().getPrimeraLetra());            
            lstTipos.add(TYPE_VARCHAR); 
            
            if (pers.getDomicilio().getSegundoNumero() == -1){
            	lstValores.add(null);
            }
            else{
            	lstValores.add(new Integer(pers.getDomicilio().getSegundoNumero()));
            }
            lstTipos.add(TYPE_NUMERIC); 
            
            lstValores.add(pers.getDomicilio().getSegundaLetra());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(pers.getDomicilio().getEscalera());            
            lstTipos.add(TYPE_VARCHAR); 
            
            if (pers.getDomicilio().getKilometro() == -1){
            	lstValores.add(null);
            }
            else{
            	lstValores.add(new Double(pers.getDomicilio().getKilometro()));
            }
            lstTipos.add(TYPE_DOUBLE); 
            
            lstValores.add(pers.getDomicilio().getBloque());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(pers.getDomicilio().getPlanta());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(pers.getDomicilio().getPuerta());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(pers.getDomicilio().getDireccionNoEstructurada());            
            lstTipos.add(TYPE_VARCHAR);  

            /* JAVIER si codigo_postal es tipo int
            if (pers.getDomicilio().getCodigoPostal() == -1){
            	lstValores.add(null);
            }
            else{
            	lstValores.add(String.valueOf(pers.getDomicilio().getCodigoPostal()));
            }
            lstTipos.add(TYPE_VARCHAR);      
            */

            lstValores.add(pers.getDomicilio().getCodigoPostal());
            lstTipos.add(TYPE_VARCHAR);

            lstValores.add(new Integer(pers.getDomicilio().getApartadoCorreos()));
            lstTipos.add(TYPE_NUMERIC);      
            
            if (pers instanceof Titular)
                lstValores.add(((Titular)pers).getNifConyuge());
            else
                lstValores.add(null);
            lstTipos.add(TYPE_VARCHAR);      
            
            if (pers instanceof Titular)
                lstValores.add(((Titular)pers).getNifCb());
//            else if (pers instanceof ComunidadBienes)
//                lstValores.add(((ComunidadBienes)pers).getNif());
            else
                lstValores.add(null);                
            lstTipos.add(TYPE_VARCHAR);
            
            if (pers instanceof Titular)
                lstValores.add(((Titular)pers).getComplementoTitularidad());
            else
                lstValores.add(null);
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(pers.getExpediente().getFechaAlteracion());            
            lstTipos.add(new Integer(Types.DATE));  
            
            if (pers.getNif()!=null){
    			lstValores.add(pers.getNif());
    		}
    		else{
    			lstValores.add("000000000");
    		}
    		lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(idBi.getParcelaCatastral().getRefCatastral());
            lstTipos.add(TYPE_VARCHAR);
            
            lstValores.add(idBi.getNumCargo());            
            lstTipos.add(TYPE_VARCHAR);       
            
            lstValores.add(idBi.getDigControl1());            
            lstTipos.add(TYPE_VARCHAR);       
            
            lstValores.add(idBi.getDigControl2());            
            lstTipos.add(TYPE_VARCHAR);      
            
            
            createStatement(s, lstValores, lstTipos);
            
            s.execute();
            
            Connection conn = getDBConnection();
            if (pers instanceof Titular){

            	if(idBi.getIdBienInmueble()!=null && pers.getNif()!=null){
            		r=null;
            		s=null;

            		if (canUpdate)
            		{
            			//Comprueba si existe la persona

            			s = conn.prepareStatement("MCexisteDerecho");
            			s.setString(1, pers.getNif());
            			s.setString(2,idBi.getIdBienInmueble());                    

            			r = s.executeQuery();
            			if (r.next())
            			{
            				s=null;
            				s = conn.prepareStatement("MCactualizarDerecho"); 
            			}
            			else
            			{
            				s=null;
            				s = conn.prepareStatement("MCinsertarDerecho");
            			}

            		}
            		else
            		{                	
            			s = conn.prepareStatement("MCinsertarDerecho");
            		}

            		lstValores.clear();
            		lstTipos.clear();   

            		lstValores.add(((Titular)pers).getDerecho().getCodDerecho());
            		lstTipos.add(TYPE_VARCHAR);

            		lstValores.add(new Float(((Titular)pers).getDerecho().getPorcentajeDerecho()));
            		lstTipos.add(TYPE_FLOAT);

            		lstValores.add(new Integer(((Titular)pers).getDerecho().getOrdinalDerecho()));
            		lstTipos.add(TYPE_NUMERIC);

            		lstValores.add(pers.getNif());
            		lstTipos.add(TYPE_VARCHAR);

            		lstValores.add(idBi.getIdBienInmueble());
            		lstTipos.add(TYPE_VARCHAR);


            		createStatement(s, lstValores, lstTipos);
            		s.execute();
            	}
            }
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            // conn.close();             
        }
        catch (DataException ex)
        {           
            throw new DataException(I18N.get("Importacion","importar.error.personas"), ex);
        	//logger.error("Error ", ex);
        }          
    }
    
    /**
     * 
     * @param reparto
     * @return
     * @throws DataException 
     */
    private void insertarDatosReparto(RepartoCatastro reparto, boolean insertarExpediente, Expediente exp) 
    //throws DataException
    throws Exception
    {        
        //TODO: MODIFICACION DEL XSD DE CATASTRO: TODOS LOS REPARTOS LLEVAN EXPEDIENTE ASOCIADO
        //Inserta en todo caso los datos del expediente
        //if (exp!=null)
        //    reparto.setDatosExpediente(exp);
        if (insertarExpediente)
            insertarDatosExpediente (exp);
        
        
        //Comprueba de qué tipo de reparto se trata
        
        //reparto de cultivo en cultivos
        if (reparto.getCodSubparcelaElementoRepartir()!=null
                && reparto.getCalifCatastralElementoRepartir()!=null
                //&& reparto.getNumCargoDestino()!=null)
                && reparto.getLstBienes()!=null && reparto.getLstBienes().size()>0)
        {        
        	for(Iterator elemRepartos = reparto.getLstBienes().iterator();elemRepartos.hasNext();){
        		
        		ElementoReparto elemReparto = (ElementoReparto)elemRepartos.next();
        		
        		insertarDatosRepartoCultivos(reparto, elemReparto);  
        	}
                      
        }
        else if (reparto.getNumOrdenConsRepartir()!=null)
        {
            //reparto de construccion
            //if (reparto.getNumOrdenRepartido()!=null)
        	if (reparto.getLstLocales()!=null && reparto.getLstLocales().size()>0)
            {
        		for(Iterator elemRepartos = reparto.getLstLocales().iterator();elemRepartos.hasNext();){

        			ElementoReparto elemReparto = (ElementoReparto)elemRepartos.next();
        			//reparto de construccion en construcciones
        			insertarDatosRepartoConstrucciones(reparto, elemReparto);
        		}
            }
            //else if (reparto.getNumCargoDestino()!=null)
        	else if (reparto.getLstBienes()!=null && reparto.getLstBienes().size()>0)
            {
        		for(Iterator elemRepartos = reparto.getLstBienes().iterator();elemRepartos.hasNext();){

        			ElementoReparto elemReparto = (ElementoReparto)elemRepartos.next();
        			//reparto de construcción en bienes inmuebles
        			insertarDatosRepartoConsBi(reparto, elemReparto);
        		}
            }
        }
    }
    
    /**
     * 
     * @param imagen
     * @return
     * @throws DataException 
     */
//    private void insertarDatosImagen(ImagenCatastro imagen, boolean insertarExpediente, Expediente exp) 
//    //throws DataException
//    throws Exception
//    {        
//        //TODO: MODIFICACION DEL XSD DE CATASTRO: TODOS LOS REPARTOS LLEVAN EXPEDIENTE ASOCIADO
//        //Inserta en todo caso los datos del expediente
//        
//        if (insertarExpediente)
//            insertarDatosExpediente (exp);
//        
//        if (imagen != null){
//        	insertarDatosImagenes(fc, lstImagenes)
//        }
//        
//    }
    
    /**
     * 
     * @param reparto
     * @return
     * @throws DataException 
     */
    /*private void insertarDatosRepartoCultivos(RepartoCatastro reparto) throws DataException
    {   
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            Integer idReparto = null;
            
            //Comprobar si existe el reparto para ver si es inserción o actualización
            s = conn.prepareStatement("MCexisteRepartoCultivo");
            
            //Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
            //si vienen o no valores nulos de forma más cómoda 
            ArrayList lstValores = new ArrayList();
            ArrayList lstTipos = new ArrayList();          
            
            lstValores.add(reparto.getIdOrigen().getRefCatastral());
            lstTipos.add(TYPE_VARCHAR);            
            lstValores.add(reparto.getCodSubparcelaElementoRepartir());            
            lstTipos.add(TYPE_VARCHAR);   
            lstValores.add(reparto.getCalifCatastralElementoRepartir());            
            lstTipos.add(TYPE_VARCHAR);   
            lstValores.add(reparto.getIdOrigen().getRefCatastral());            
            lstTipos.add(TYPE_VARCHAR);               
            lstValores.add(reparto.getNumCargoDestino());            
            lstTipos.add(TYPE_VARCHAR);   
            createStatement(s, lstValores, lstTipos);
            r = s.executeQuery();
            if (r.next())
                idReparto = new Integer(r.getInt(1));
            
            s=null;
            r=null;
            
            if(idReparto!=null)
                s = conn.prepareStatement("MCactualizarRepartoCultivo");
            else
                s = conn.prepareStatement("MCinsertarRepartoCultivo");
                       
           
            lstValores = new ArrayList();
            lstTipos = new ArrayList();
            
            lstValores.add(reparto.getTipoReparto());
            lstTipos.add(TYPE_VARCHAR);            
            lstValores.add(new Integer(reparto.getDatosExpediente().getAnnoExpedienteAdminOrigenAlteracion()));            
            lstTipos.add(TYPE_NUMERIC);   
            lstValores.add(reparto.getDatosExpediente().getReferenciaExpedienteAdminOrigen());            
            lstTipos.add(TYPE_VARCHAR);   
            if (reparto.getDatosExpediente().getEntidadGeneradora()!=null)
                lstValores.add(new Integer(reparto.getDatosExpediente().getEntidadGeneradora().getCodigo()));
            else
                lstValores.add(null);            
            lstTipos.add(TYPE_VARCHAR);               
            lstValores.add(reparto.getTipoMovimiento());            
            lstTipos.add(TYPE_VARCHAR);
            
            //datos para hallar el id_cultivo
            lstValores.add(reparto.getIdOrigen().getRefCatastral());            
            lstTipos.add(TYPE_VARCHAR);  
            lstValores.add(reparto.getCodSubparcelaElementoRepartir());            
            lstTipos.add(TYPE_VARCHAR);  
            lstValores.add(reparto.getCalifCatastralElementoRepartir());            
            lstTipos.add(TYPE_VARCHAR);  
            
            //porcentaje de reparto
            lstValores.add(new Float(reparto.getPorcentajeReparto()));            
            lstTipos.add(TYPE_FLOAT);  
            
            //datos para hallar el id_bieninmueble
            lstValores.add(reparto.getIdOrigen().getRefCatastral());            
            lstTipos.add(TYPE_VARCHAR);  
            lstValores.add(reparto.getNumCargoDestino());            
            lstTipos.add(TYPE_VARCHAR);  
            
            if(idReparto!=null)
            {
                lstValores.add(idReparto);         
                lstTipos.add(TYPE_NUMERIC); 
            }             
            
            createStatement(s, lstValores, lstTipos);
            s.execute();
                                        
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            // conn.close(); 
            
        }
        catch (Exception ex)
        {           
            throw new DataException(I18N.get("Importacion","importar.error.repartocultivos"), ex);
        }
    }*/
    
    /**
     * 
     * @param reparto
     * @return
     * @throws DataException 
     */
    private void insertarDatosRepartoCultivos(RepartoCatastro reparto, ElementoReparto elemReparto) //throws DataException
    throws Exception
    {   
        PreparedStatement s = null;
		ResultSet r = null;
		Integer idReparto = null;
		
		//Comprobar si existe el reparto para ver si es inserción o actualización
		Connection conn = getDBConnection();
		s = conn.prepareStatement("MCexisteRepartoCultivo");
		
		//Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
		//si vienen o no valores nulos de forma más cómoda 
		lstValores.clear();
		lstTipos.clear();         
		
		lstValores.add(reparto.getIdOrigen().getRefCatastral());
		lstTipos.add(TYPE_VARCHAR);            
		lstValores.add(reparto.getCodSubparcelaElementoRepartir());            
		lstTipos.add(TYPE_VARCHAR);   
		lstValores.add(reparto.getCalifCatastralElementoRepartir());            
		lstTipos.add(TYPE_VARCHAR);   
		lstValores.add(reparto.getIdOrigen().getRefCatastral());            
		lstTipos.add(TYPE_VARCHAR);               
		//lstValores.add(reparto.getNumCargoDestino());
		lstValores.add(elemReparto.getNumCargo());
		lstTipos.add(TYPE_VARCHAR);   
		createStatement(s, lstValores, lstTipos);
		r = s.executeQuery();
		if (r.next())
		    idReparto = new Integer(r.getInt(1));
		
		s=null;
		r=null;
		
		if(idReparto!=null)
		    s = conn.prepareStatement("MCactualizarRepartoCultivo");
		else
		    s = conn.prepareStatement("MCinsertarRepartoCultivo");
		           
         
		lstValores.clear();
		lstTipos.clear();
		
		//lstValores.add(reparto.getTipoReparto());
		//lstTipos.add(TYPE_VARCHAR);            
		//lstValores.add(new Integer(reparto.getDatosExpediente().getAnnoExpedienteAdminOrigenAlteracion()));
		lstValores.add(new Integer(elemReparto.getExpediente().getAnnoExpedienteAdminOrigenAlteracion()));
		lstTipos.add(TYPE_NUMERIC);   
		//lstValores.add(reparto.getDatosExpediente().getReferenciaExpedienteAdminOrigen());
		lstValores.add(elemReparto.getExpediente().getReferenciaExpedienteAdminOrigen());
		lstTipos.add(TYPE_VARCHAR);   
		//if (reparto.getDatosExpediente().getEntidadGeneradora()!=null)
		if (elemReparto.getExpediente().getEntidadGeneradora()!=null)
		    lstValores.add(new Integer(elemReparto.getExpediente().getEntidadGeneradora().getCodigo()));
		else
		    lstValores.add(null);            
		lstTipos.add(TYPE_VARCHAR);               
		//lstValores.add(reparto.getTipoMovimiento());
		lstValores.add(reparto.getTIPO_MOVIMIENTO());
		lstTipos.add(TYPE_VARCHAR);
		
		//datos para hallar el id_cultivo
		lstValores.add(reparto.getIdOrigen().getRefCatastral());            
		lstTipos.add(TYPE_VARCHAR);  
		lstValores.add(reparto.getCodSubparcelaElementoRepartir());            
		lstTipos.add(TYPE_VARCHAR);  
		lstValores.add(reparto.getCalifCatastralElementoRepartir());            
		lstTipos.add(TYPE_VARCHAR);  
		
		//porcentaje de reparto
		//lstValores.add(new Float(reparto.getPorcentajeReparto()));
		lstValores.add(new Float(elemReparto.getPorcentajeReparto()));
		lstTipos.add(TYPE_FLOAT);  
		
		//datos para hallar el id_bieninmueble
		lstValores.add(reparto.getIdOrigen().getRefCatastral());            
		lstTipos.add(TYPE_VARCHAR);  
		//lstValores.add(reparto.getNumCargoDestino());
		lstValores.add(elemReparto.getNumCargo());
		lstTipos.add(TYPE_VARCHAR);  
		
		if(idReparto!=null)
		{
		    lstValores.add(idReparto);         
		    lstTipos.add(TYPE_NUMERIC); 
		}             
		
		createStatement(s, lstValores, lstTipos);
		s.execute();
		                            
		if (s!=null) s.close();
		if (r!= null) r.close(); 
		// conn.close(); 
    }
    
    /**
     * 
     * @param reparto
     * @return
     * @throws DataException 
     */
    /*private void insertarDatosRepartoConstrucciones(RepartoCatastro reparto) throws DataException
    {           
    	
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            Integer idReparto = null;
            
            
            //Comprobar si existe el reparto para ver si es inserción o actualización
            s = conn.prepareStatement("MCexisteRepartoConstruccion");
            
            //Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
            //si vienen o no valores nulos de forma más cómoda 
            ArrayList lstValores = new ArrayList();
            ArrayList lstTipos = new ArrayList();          
            
            lstValores.add(reparto.getIdOrigen().getRefCatastral());
            lstTipos.add(TYPE_VARCHAR);            
            lstValores.add(reparto.getNumOrdenConsRepartir()); 
            lstTipos.add(TYPE_VARCHAR);  
            lstValores.add(reparto.getIdOrigen().getRefCatastral());
            lstTipos.add(TYPE_VARCHAR);            
            lstValores.add(reparto.getNumOrdenRepartido()); 
            lstTipos.add(TYPE_VARCHAR);   
           
            createStatement(s, lstValores, lstTipos);
            r = s.executeQuery();
            if (r.next())
                idReparto = new Integer(r.getInt(1));
            
            s=null;
            r=null;
            
            if(idReparto!=null)
                s = conn.prepareStatement("MCactualizarRepartoConstruccion");
            else
                s = conn.prepareStatement("MCinsertarRepartoConstruccion");
      
            lstValores = new ArrayList();
            lstTipos = new ArrayList();
            
            lstValores.add(reparto.getTipoReparto());
            lstTipos.add(TYPE_VARCHAR);            
            lstValores.add(new Integer(reparto.getDatosExpediente().getAnnoExpedienteAdminOrigenAlteracion()));            
            lstTipos.add(TYPE_NUMERIC);   
            lstValores.add(reparto.getDatosExpediente().getReferenciaExpedienteAdminOrigen());            
            lstTipos.add(TYPE_VARCHAR);   
            if (reparto.getDatosExpediente().getEntidadGeneradora()!=null)
                lstValores.add(new Integer(reparto.getDatosExpediente().getEntidadGeneradora().getCodigo()));
            else
                lstValores.add(null);            
            lstTipos.add(TYPE_VARCHAR);               
            lstValores.add(reparto.getTipoMovimiento());            
            lstTipos.add(TYPE_VARCHAR);
            
            //datos para hallar el id_construccion_origen
            lstValores.add(reparto.getIdOrigen().getRefCatastral());            
            lstTipos.add(TYPE_VARCHAR);  
            lstValores.add(reparto.getNumOrdenConsRepartir());            
            lstTipos.add(TYPE_VARCHAR);  
                        
            //porcentaje de reparto
            lstValores.add(new Float(reparto.getPorcentajeReparto()));            
            lstTipos.add(TYPE_FLOAT);  
            
            //datos para hallar el id_construccion_origen
            lstValores.add(reparto.getIdOrigen().getRefCatastral());            
            lstTipos.add(TYPE_VARCHAR);  
            lstValores.add(reparto.getNumOrdenRepartido());            
            lstTipos.add(TYPE_VARCHAR);  
            
            if(idReparto!=null)
            {
                lstValores.add(idReparto);         
                lstTipos.add(TYPE_NUMERIC); 
            }             
            
            createStatement(s, lstValores, lstTipos);            
            s.execute();
                                        
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            // conn.close(); 
            
        }
        catch (Exception ex)
        {           
            throw new DataException(I18N.get("Importacion","importar.error.repartosconstrucciones"), ex);
        }             
    }*/
    
    /**
     * 
     * @param reparto
     * @return
     * @throws DataException 
     */
    private void insertarDatosRepartoConstrucciones(RepartoCatastro reparto, ElementoReparto elemreparto) //throws DataException
    throws Exception
    {           
    	
        PreparedStatement s = null;
		ResultSet r = null;
		Integer idReparto = null;
		
		Connection conn = getDBConnection();
		//Comprobar si existe el reparto para ver si es inserción o actualización
		s = conn.prepareStatement("MCexisteRepartoConstruccion");
		
		//Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
		//si vienen o no valores nulos de forma más cómoda 
		lstValores.clear();
		lstTipos.clear();
		
		lstValores.add(reparto.getIdOrigen().getRefCatastral());
		lstTipos.add(TYPE_VARCHAR);            
		lstValores.add(reparto.getNumOrdenConsRepartir()); 
		lstTipos.add(TYPE_VARCHAR);  
		lstValores.add(reparto.getIdOrigen().getRefCatastral());
		lstTipos.add(TYPE_VARCHAR);            
		//lstValores.add(reparto.getNumOrdenRepartido());
		lstValores.add(elemreparto.getNumCargo());
		lstTipos.add(TYPE_VARCHAR);   
         
		createStatement(s, lstValores, lstTipos);
		r = s.executeQuery();
		if (r.next())
		    idReparto = new Integer(r.getInt(1));
		
		s=null;
		r=null;
		
		if(idReparto!=null)
		    s = conn.prepareStatement("MCactualizarRepartoConstruccion");
		else
		    s = conn.prepareStatement("MCinsertarRepartoConstruccion");
     
		lstValores.clear();
		lstTipos.clear();
		
		//lstValores.add(reparto.getTipoReparto());
		//lstTipos.add(TYPE_VARCHAR);            
		//lstValores.add(new Integer(reparto.getDatosExpediente().getAnnoExpedienteAdminOrigenAlteracion()));
		lstValores.add(new Integer(elemreparto.getExpediente().getAnnoExpedienteAdminOrigenAlteracion()));
		lstTipos.add(TYPE_NUMERIC);   
		//lstValores.add(reparto.getDatosExpediente().getReferenciaExpedienteAdminOrigen());
		lstValores.add(elemreparto.getExpediente().getReferenciaExpedienteAdminOrigen());
		lstTipos.add(TYPE_VARCHAR);   
		//if (reparto.getDatosExpediente().getEntidadGeneradora()!=null)
		if (elemreparto.getExpediente().getEntidadGeneradora()!=null)
		    lstValores.add(new Integer(elemreparto.getExpediente().getEntidadGeneradora().getCodigo()));
		else
		    lstValores.add(null);            
		lstTipos.add(TYPE_VARCHAR);               
		//lstValores.add(reparto.getTipoMovimiento());
		lstValores.add(reparto.getTIPO_MOVIMIENTO());
		lstTipos.add(TYPE_VARCHAR);
		
		//datos para hallar el id_construccion_origen
		lstValores.add(reparto.getIdOrigen().getRefCatastral());            
		lstTipos.add(TYPE_VARCHAR);  
		lstValores.add(reparto.getNumOrdenConsRepartir());            
		lstTipos.add(TYPE_VARCHAR);  
		            
		//porcentaje de reparto
		lstValores.add(new Float(elemreparto.getPorcentajeReparto()));            
		lstTipos.add(TYPE_FLOAT);  
		
		//datos para hallar el id_construccion_origen
		lstValores.add(reparto.getIdOrigen().getRefCatastral());            
		lstTipos.add(TYPE_VARCHAR);  
		//lstValores.add(reparto.getNumOrdenRepartido());
		lstValores.add(elemreparto.getNumCargo());
		lstTipos.add(TYPE_VARCHAR);  
		
		if(idReparto!=null)
		{
		    lstValores.add(idReparto);         
		    lstTipos.add(TYPE_NUMERIC); 
		}             
		
		createStatement(s, lstValores, lstTipos);            
		s.execute();
		                            
		if (s!=null) s.close();
		if (r!= null) r.close(); 
		// conn.close();              
    }
    
    
    /**
     * 
     * @param reparto
     * @return
     * @throws DataException 
     */
    /*private void insertarDatosRepartoConsBi(RepartoCatastro reparto) throws DataException
    {           
        try
        {            
            PreparedStatement s = null;
            ResultSet r = null;
            Integer idReparto = null;
            
            
            //Comprobar si existe el reparto para ver si es inserción o actualización
            s = conn.prepareStatement("MCexisteRepartoConsBi");
            
            //Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
            //si vienen o no valores nulos de forma más cómoda 
            ArrayList lstValores = new ArrayList();
            ArrayList lstTipos = new ArrayList();          
            
            lstValores.add(reparto.getIdOrigen().getRefCatastral());
            lstTipos.add(TYPE_VARCHAR);            
            lstValores.add(reparto.getNumOrdenConsRepartir()); 
            lstTipos.add(TYPE_VARCHAR);  
            lstValores.add(reparto.getIdOrigen().getRefCatastral());
            lstTipos.add(TYPE_VARCHAR);            
            lstValores.add(reparto.getNumCargoDestino()); 
            lstTipos.add(TYPE_VARCHAR);   
           
            createStatement(s, lstValores, lstTipos);
            r = s.executeQuery();
            if (r.next())
                idReparto = new Integer(r.getInt(1));
            
            s=null;
            r=null;
            
            if(idReparto!=null)
                s = conn.prepareStatement("MCactualizarRepartoConsBi");
            else
                s = conn.prepareStatement("MCinsertarRepartoConsBi");
      
            lstValores = new ArrayList();
            lstTipos = new ArrayList();
            
            lstValores.add(reparto.getTipoReparto());
            lstTipos.add(TYPE_VARCHAR);            
            lstValores.add(new Integer(reparto.getDatosExpediente().getAnnoExpedienteAdminOrigenAlteracion()));            
            lstTipos.add(TYPE_NUMERIC);   
            lstValores.add(reparto.getDatosExpediente().getReferenciaExpedienteAdminOrigen());            
            lstTipos.add(TYPE_VARCHAR);   
            if (reparto.getDatosExpediente().getEntidadGeneradora()!=null)
                lstValores.add(new Integer(reparto.getDatosExpediente().getEntidadGeneradora().getCodigo()));
            else
                lstValores.add(null);            
            lstTipos.add(TYPE_VARCHAR);               
            lstValores.add(reparto.getTipoMovimiento());            
            lstTipos.add(TYPE_VARCHAR);
            
            //datos para hallar el id_construccion_origen
            lstValores.add(reparto.getIdOrigen().getRefCatastral());            
            lstTipos.add(TYPE_VARCHAR);  
            lstValores.add(reparto.getNumOrdenConsRepartir());            
            lstTipos.add(TYPE_VARCHAR);  
                        
            //porcentaje de reparto
            lstValores.add(new Float(reparto.getPorcentajeReparto()));            
            lstTipos.add(TYPE_FLOAT);  
            
            //datos para hallar el id_bieninmuebledestino
            lstValores.add(reparto.getIdOrigen().getRefCatastral());            
            lstTipos.add(TYPE_VARCHAR);  
            lstValores.add(reparto.getNumCargoDestino());            
            lstTipos.add(TYPE_VARCHAR);  
            
            if(idReparto!=null)
            {
                lstValores.add(idReparto);         
                lstTipos.add(TYPE_NUMERIC); 
            }             
            
            createStatement(s, lstValores, lstTipos);
            s.execute();
                                        
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            // conn.close(); 
            
        }
        catch (Exception ex)
        {           
            throw new DataException(I18N.get("Importacion","importar.error.repartosconsbi"), ex);
        }        
    }*/
    
    /**
     * 
     * @param reparto
     * @return
     * @throws DataException 
     */
    private void insertarDatosRepartoConsBi(RepartoCatastro reparto, ElementoReparto elemReparto) //throws DataException
    throws Exception
    {           
        PreparedStatement s = null;
		ResultSet r = null;
		Integer idReparto = null;
		
		Connection conn = getDBConnection();
		//Comprobar si existe el reparto para ver si es inserción o actualización
		s = conn.prepareStatement("MCexisteRepartoConsBi");
		
		//Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
		//si vienen o no valores nulos de forma más cómoda 
		lstValores.clear();
		lstTipos.clear();
		
		lstValores.add(reparto.getIdOrigen().getRefCatastral());
		lstTipos.add(TYPE_VARCHAR);            
		lstValores.add(reparto.getNumOrdenConsRepartir()); 
		lstTipos.add(TYPE_VARCHAR);  
		lstValores.add(reparto.getIdOrigen().getRefCatastral());
		lstTipos.add(TYPE_VARCHAR);            
		//lstValores.add(reparto.getNumCargoDestino());
		lstValores.add(elemReparto.getNumCargo());
		lstTipos.add(TYPE_VARCHAR);   
         
		createStatement(s, lstValores, lstTipos);
		r = s.executeQuery();
		if (r.next())
		    idReparto = new Integer(r.getInt(1));
		
		s=null;
		r=null;
		
		if(idReparto!=null)
		    s = conn.prepareStatement("MCactualizarRepartoConsBi");
		else
		    s = conn.prepareStatement("MCinsertarRepartoConsBi");
     
		lstValores.clear();
		lstTipos.clear();
		
		//lstValores.add(reparto.getTipoReparto());
		//lstTipos.add(TYPE_VARCHAR);            
		//lstValores.add(new Integer(reparto.getDatosExpediente().getAnnoExpedienteAdminOrigenAlteracion()));
		lstValores.add(new Integer(elemReparto.getExpediente().getAnnoExpedienteAdminOrigenAlteracion()));
		lstTipos.add(TYPE_NUMERIC);   
		//lstValores.add(reparto.getDatosExpediente().getReferenciaExpedienteAdminOrigen());
		lstValores.add(elemReparto.getExpediente().getReferenciaExpedienteAdminOrigen());
		lstTipos.add(TYPE_VARCHAR);   
		//if (reparto.getDatosExpediente().getEntidadGeneradora()!=null)
		if (elemReparto.getExpediente().getEntidadGeneradora()!=null)
		    //lstValores.add(new Integer(reparto.getDatosExpediente().getEntidadGeneradora().getCodigo()));
			lstValores.add(new Integer(elemReparto.getExpediente().getEntidadGeneradora().getCodigo()));
		else
		    lstValores.add(null);            
		lstTipos.add(TYPE_VARCHAR);               
		//lstValores.add(reparto.getTipoMovimiento());
		lstValores.add(reparto.getTIPO_MOVIMIENTO());
		lstTipos.add(TYPE_VARCHAR);
		
		//datos para hallar el id_construccion_origen
		lstValores.add(reparto.getIdOrigen().getRefCatastral());            
		lstTipos.add(TYPE_VARCHAR);  
		lstValores.add(reparto.getNumOrdenConsRepartir());            
		lstTipos.add(TYPE_VARCHAR);  
		            
		//porcentaje de reparto
		lstValores.add(new Float(reparto.getPorcentajeReparto()));            
		lstTipos.add(TYPE_FLOAT);  
		
		//datos para hallar el id_bieninmuebledestino
		lstValores.add(reparto.getIdOrigen().getRefCatastral());            
		lstTipos.add(TYPE_VARCHAR);  
		//lstValores.add(reparto.getNumCargoDestino());
		lstValores.add(elemReparto.getNumCargo());
		lstTipos.add(TYPE_VARCHAR);  
		
		if(idReparto!=null)
		{
		    lstValores.add(idReparto);         
		    lstTipos.add(TYPE_NUMERIC); 
		}             
		
		createStatement(s, lstValores, lstTipos);
		s.execute();
		                            
		if (s!=null) s.close();
		if (r!= null) r.close(); 
		// conn.close();         
    }
    
    
    private void insertarDatosSuelo(SueloCatastro sc, boolean insertarExpediente, Expediente exp) 
    //throws DataException
    throws Exception
    {
    	
        try
        {      
            if (exp!=null)
                sc.setDatosExpediente(exp);
            if (insertarExpediente && exp!=null)
                insertarDatosExpediente (exp);  
            
            PreparedStatement s = null;
            ResultSet r = null;
            String idSuelo = null;
            
            Connection conn = getDBConnection();
            
            //Comprobar si existe el suelo para ver si es inserción o actualización
            s = conn.prepareStatement("MCexisteSuelo");
            s.setString(1,sc.getIdSuelo());
            r = s.executeQuery();
            if (r.next())
                idSuelo = r.getString(1);
            
            s=null;
            r=null;
            
            if(idSuelo!=null)
                s = conn.prepareStatement("MCactualizarSuelo");
            else
                s = conn.prepareStatement("MCinsertarSuelo");
            
            
            
            //Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
            //si vienen o no valores nulos de forma más cómoda 
            lstValores.clear();
        	lstTipos.clear();
           
            lstValores.add(new Integer(sc.getDatosExpediente().getAnnoExpedienteAdminOrigenAlteracion()));
            lstTipos.add(TYPE_NUMERIC);
            
            lstValores.add(sc.getDatosExpediente().getReferenciaExpedienteAdminOrigen());            
            lstTipos.add(TYPE_VARCHAR);            
            
            lstValores.add(String.valueOf(sc.getDatosExpediente().getCodigoEntidadRegistroDGCOrigenAlteracion()));
            lstTipos.add(TYPE_VARCHAR);
            
            //Tipo movimiento
            lstValores.add(null);            
            lstTipos.add(TYPE_VARCHAR);            
            
            lstValores.add(sc.getRefParcela().getRefCatastral());
            lstTipos.add(TYPE_VARCHAR);
            
            lstValores.add(sc.getNumOrden());            
            lstTipos.add(TYPE_VARCHAR);       
            
            lstValores.add(sc.getDatosFisicos().getLongFachada());            
            lstTipos.add(TYPE_FLOAT);       
            
            lstValores.add(sc.getDatosFisicos().getTipoFachada());            
            lstTipos.add(TYPE_VARCHAR);      
            
            lstValores.add(sc.getDatosFisicos().getSupOcupada());            
            lstTipos.add(TYPE_NUMERIC);  
            
            lstValores.add(sc.getDatosFisicos().getFondo());            
            lstTipos.add(TYPE_NUMERIC);  
            
            lstValores.add(sc.getDatosEconomicos().getCodViaPonencia());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(sc.getDatosEconomicos().getTramos()!=null?sc.getDatosEconomicos().getTramos().getCodTramo():"");            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(sc.getDatosEconomicos().getZonaValor().getCodZonaValor());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(sc.getDatosEconomicos().getZonaUrbanistica().getCodZona());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(sc.getDatosEconomicos().getCodTipoValor());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(String.valueOf(sc.getDatosEconomicos().getNumFachadas()));            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(sc.getDatosEconomicos().isCorrectorLongFachada());            
            lstTipos.add(TYPE_BOOLEAN);  
            
            lstValores.add(sc.getDatosEconomicos().isCorrectorFormaIrregular());            
            lstTipos.add(TYPE_BOOLEAN); 
            
            lstValores.add(sc.getDatosEconomicos().isCorrectorDesmonte());            
            lstTipos.add(TYPE_BOOLEAN); 
            
            lstValores.add(sc.getDatosEconomicos().isCorrectorSupDistinta());            
            lstTipos.add(TYPE_BOOLEAN); 
            
            lstValores.add(sc.getDatosEconomicos().isCorrectorInedificabilidad());            
            lstTipos.add(TYPE_BOOLEAN); 
            
            lstValores.add(sc.getDatosEconomicos().isCorrectorVPO());            
            lstTipos.add(TYPE_BOOLEAN); 
            
            lstValores.add(sc.getDatosEconomicos().getCorrectorAprecDeprec());            
            lstTipos.add(TYPE_FLOAT); 
            
            lstValores.add(sc.getDatosEconomicos().isCorrectorDeprecFuncional());            
            lstTipos.add(TYPE_BOOLEAN); 
            
            lstValores.add(sc.getDatosEconomicos().getCorrectorCargasSingulares());            
            lstTipos.add(TYPE_FLOAT); 
            
            lstValores.add(sc.getDatosEconomicos().isCorrectorSitEspeciales());            
            lstTipos.add(TYPE_BOOLEAN);  
            
            lstValores.add(sc.getDatosEconomicos().isCorrectorNoLucrativo());            
            lstTipos.add(TYPE_BOOLEAN);      
           
            lstValores.add(sc.getIdSuelo());
            lstTipos.add(TYPE_VARCHAR);
            
            createStatement(s, lstValores, lstTipos);
            
            s.execute();                 
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            // conn.close(); 
            
        }
        catch (DataException ex)
        {           
            throw new DataException(I18N.get("Importacion","importar.error.suelos"), ex);
        	//logger.error("Error ", ex);
        }        
    }
    
    
    private void insertarDatosUC(UnidadConstructivaCatastro uc, boolean insertarExpediente, Expediente exp)
    //throws DataException
    throws Exception
    {        
        try
        {      
            if (exp!=null)
                uc.setDatosExpediente(exp);
            if (insertarExpediente && exp!=null)
                insertarDatosExpediente (exp);  
            
            PreparedStatement s = null;
            ResultSet r = null;
            String idUC = null;
            
            Connection conn = getDBConnection();
            
            //Comprobar si existe la unidad constructiva para ver si es inserción o actualización
            s = conn.prepareStatement("MCexisteUC");
            s.setString(1,uc.getIdUnidadConstructiva());
            r = s.executeQuery();
            if (r.next())
                idUC = r.getString(1);
            
            s=null;
            r=null;
            
            if(idUC!=null)
                s = conn.prepareStatement("MCactualizarUC");
            else
                s = conn.prepareStatement("MCinsertarUC");
            
            
            
            //Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
            //si vienen o no valores nulos de forma más cómoda 
            lstValores.clear();
        	lstTipos.clear();  
            
            lstValores.add(new Integer(uc.getDatosExpediente().getAnnoExpedienteAdminOrigenAlteracion()));
            lstTipos.add(TYPE_NUMERIC);
            
            lstValores.add(uc.getDatosExpediente().getReferenciaExpedienteAdminOrigen());            
            lstTipos.add(TYPE_VARCHAR);            
            
            if (uc.getDatosExpediente().getEntidadGeneradora()!=null)
                lstValores.add(new Integer(uc.getDatosExpediente().getEntidadGeneradora().getCodigo()));
            else
                lstValores.add(null);
            lstTipos.add(TYPE_VARCHAR);
            
            //Tipo movimiento
            lstValores.add(null);            
            lstTipos.add(TYPE_VARCHAR);            
            
            lstValores.add(uc.getRefParcela().getRefCatastral());
            lstTipos.add(TYPE_VARCHAR);
            
            lstValores.add(uc.getCodUnidadConstructiva());            
            lstTipos.add(TYPE_VARCHAR);       
            
            lstValores.add(uc.getTipoUnidad());            
            lstTipos.add(TYPE_VARCHAR);       
            
            lstValores.add(uc.getDirUnidadConstructiva().getMunicipioINE());            
            lstTipos.add(TYPE_VARCHAR);      
            
            lstValores.add(uc.getDirUnidadConstructiva().getCodigoMunicipioDGC());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(uc.getDirUnidadConstructiva().getNombreEntidadMenor());            
            lstTipos.add(TYPE_VARCHAR);  
            
            if (uc.getDirUnidadConstructiva().getPrimerNumero() == -1){
            	lstValores.add(null);
            }
            else{
            	lstValores.add(new Integer(uc.getDirUnidadConstructiva().getPrimerNumero()));
            }
            lstTipos.add(TYPE_NUMERIC);  
            
            lstValores.add(uc.getDirUnidadConstructiva().getPrimeraLetra());            
            lstTipos.add(TYPE_VARCHAR);  
            
            if (uc.getDirUnidadConstructiva().getSegundoNumero() == -1){
            	lstValores.add(null);
            }
            else{
            	lstValores.add(new Integer(uc.getDirUnidadConstructiva().getSegundoNumero()));
            }
            lstTipos.add(TYPE_NUMERIC);  
            
            lstValores.add(uc.getDirUnidadConstructiva().getSegundaLetra());            
            lstTipos.add(TYPE_VARCHAR);  
            
            if (uc.getDirUnidadConstructiva().getKilometro() == -1){
            	lstValores.add(null);
            }
            else{
            	lstValores.add(new Double(uc.getDirUnidadConstructiva().getKilometro()));
            }
            lstTipos.add(TYPE_DOUBLE);  
            
            lstValores.add(uc.getDirUnidadConstructiva().getBloque());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(uc.getDirUnidadConstructiva().getDireccionNoEstructurada());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(uc.getDatosFisicos().getAnioConstruccion());            
            lstTipos.add(TYPE_NUMERIC); 
            
            lstValores.add(uc.getDatosFisicos().getIndExactitud());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(uc.getDatosFisicos().getSupOcupada());            
            lstTipos.add(TYPE_NUMERIC); 
            
            lstValores.add(uc.getDatosFisicos().getLongFachada());            
            lstTipos.add(TYPE_FLOAT); 
            
            lstValores.add(uc.getDatosEconomicos().getCodViaPonencia());            
            lstTipos.add(TYPE_VARCHAR); 
            
            
            if (uc.getDatosEconomicos().getTramoPonencia()!=null)
                lstValores.add(uc.getDatosEconomicos().getTramoPonencia().getCodTramo());            
            else
                lstValores.add(null);
            lstTipos.add(TYPE_VARCHAR); 
            
            if(uc.getDatosEconomicos().getZonaValor()!=null)
                lstValores.add(uc.getDatosEconomicos().getZonaValor().getCodZonaValor());            
            else
                lstValores.add(null);
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(uc.getDatosEconomicos().getNumFachadas());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(uc.getDatosEconomicos().isCorrectorLongFachada());            
            lstTipos.add(TYPE_BOOLEAN);  
            
            lstValores.add(uc.getDatosEconomicos().getCorrectorConservacion());            
            lstTipos.add(TYPE_VARCHAR);      
            
            lstValores.add(uc.getDatosEconomicos().isCorrectorDepreciacion());            
            lstTipos.add(TYPE_BOOLEAN);      
            
            lstValores.add(uc.getDatosEconomicos().getCoefCargasSingulares());            
            lstTipos.add(TYPE_FLOAT);      
            
            lstValores.add(uc.getDatosEconomicos().isCorrectorSitEspeciales());            
            lstTipos.add(TYPE_BOOLEAN);      
            
            lstValores.add(uc.getDatosEconomicos().isCorrectorNoLucrativo());            
            lstTipos.add(TYPE_BOOLEAN);      
            
            if (uc.getDirUnidadConstructiva()!=null)
                lstValores.add(new Integer(uc.getDirUnidadConstructiva().getIdVia()));
            else
                lstValores.add(null);
            lstTipos.add(TYPE_NUMERIC);
            
            lstValores.add(uc.getIdUnidadConstructiva());
            lstTipos.add(TYPE_VARCHAR);
            
            
            createStatement(s, lstValores, lstTipos);
            
            s.execute();
                   
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            // conn.close();             
        }
        catch (DataException ex)
        {           
            throw new DataException(I18N.get("Importacion","importar.error.uc"), ex);
        	//logger.error("Error ", ex);
        }       
        
    }
    
    
    private void insertarDatosConstruccion(ConstruccionCatastro construc, boolean insertarExpediente, Expediente exp)
    //throws DataException
    throws Exception
    {
        try
        {      
            if (exp!=null)
                construc.setDatosExpediente(exp);
            if (insertarExpediente && exp!=null)
                insertarDatosExpediente (exp);  
            
            PreparedStatement s = null;
            ResultSet r = null;
            String idConst = null;
            
            Connection conn = getDBConnection();
            
            //Comprobar si existe la construcción para ver si es inserción o actualización
            s = conn.prepareStatement("MCexisteConstruccion");
            s.setString(1,construc.getIdConstruccion());
            r = s.executeQuery();
            if (r.next())
                idConst = r.getString(1);
            
            r=null;
            s=null;
            
            if(idConst!=null)
                s = conn.prepareStatement("MCactualizarConstruccion");
            else
                s = conn.prepareStatement("MCinsertarConstruccion");
            
            
            //Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
            //si vienen o no valores nulos de forma más cómoda 
            lstValores.clear();
        	lstTipos.clear();     
            
            lstValores.add(new Integer(construc.getDatosExpediente().getAnnoExpedienteAdminOrigenAlteracion()));
            lstTipos.add(TYPE_NUMERIC);
            
            lstValores.add(construc.getDatosExpediente().getReferenciaExpedienteAdminOrigen());            
            lstTipos.add(TYPE_VARCHAR);            
            
            if (construc.getDatosExpediente().getEntidadGeneradora()!=null)
                lstValores.add(new Integer(construc.getDatosExpediente().getEntidadGeneradora().getCodigo()));
            else
                lstValores.add(null);
            lstTipos.add(TYPE_VARCHAR);
            
            //Tipo movimiento
            lstValores.add(construc.getTipoMovimiento());            
            lstTipos.add(TYPE_VARCHAR);            
            
            lstValores.add(construc.getRefParcela().getRefCatastral());
            lstTipos.add(TYPE_VARCHAR);
            
            
            
            lstValores.add(construc.getNumOrdenConstruccion());            
            lstTipos.add(TYPE_VARCHAR);       
            
            lstValores.add(construc.getNumOrdenBienInmueble());            
            lstTipos.add(TYPE_VARCHAR);       
            
            
            //Código de la uc
//            lstValores.add(null);
            lstValores.add(construc.getDatosFisicos().getCodUnidadConstructiva());
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(construc.getDomicilioTributario().getBloque());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(construc.getDomicilioTributario().getEscalera());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(construc.getDomicilioTributario().getPlanta());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(construc.getDomicilioTributario().getPuerta());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(construc.getDatosFisicos().getCodDestino());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(construc.getDatosFisicos().getTipoReforma());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(construc.getDatosFisicos().getAnioReforma());            
            lstTipos.add(TYPE_NUMERIC); 
            
            lstValores.add(construc.getDatosFisicos().getAnioAntiguedad());            
            lstTipos.add(TYPE_NUMERIC); 
            
            lstValores.add(construc.getDatosFisicos().isLocalInterior());            
            lstTipos.add(TYPE_BOOLEAN); 
            
            lstValores.add(construc.getDatosFisicos().getSupTotal());            
            lstTipos.add(TYPE_NUMERIC); 
            
            lstValores.add(construc.getDatosFisicos().getSupTerrazasLocal());            
            lstTipos.add(TYPE_NUMERIC); 
            
            lstValores.add(construc.getDatosFisicos().getSupImputableLocal());            
            lstTipos.add(TYPE_NUMERIC);  
            
            lstValores.add(construc.getDatosEconomicos().getTipoConstruccion());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(construc.getDatosEconomicos().getCodUsoPredominante());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(construc.getDatosEconomicos().getCodCategoriaPredominante());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(construc.getDatosEconomicos().getCodModalidadReparto());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(construc.getDatosEconomicos().getCodTipoValor());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(construc.getDatosEconomicos().getCorrectorApreciación());            
            lstTipos.add(TYPE_FLOAT);      
            
            lstValores.add(construc.getDatosEconomicos().isCorrectorVivienda());            
            lstTipos.add(TYPE_BOOLEAN);      
            
            lstValores.add(construc.getIdConstruccion());
            lstTipos.add(TYPE_VARCHAR);
            
            createStatement(s, lstValores, lstTipos);
            
            s.execute();                
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            // conn.close(); 
            
            
            
        }
        catch (DataException ex)
        {           
            throw new DataException(I18N.get("Importacion","importar.error.construcciones"), ex);
        	//logger.error("Error ", ex);
        }       
        
    }
    
    /**
     * 
     * @param cultivo
     * @return
     * @throws DataException
     */
    private void insertarDatosCultivo(Cultivo cultivo, boolean insertarExpediente, Expediente exp) //throws DataException
    throws Exception
    {       
        try
        {     
            if (exp!=null)
                cultivo.setDatosExpediente(exp);
            if (insertarExpediente && exp!=null)
                insertarDatosExpediente (exp);  
            
            PreparedStatement s = null;
            ResultSet r = null;
            String idCultivo = null;
            
            Connection conn = getDBConnection();
            
            //Comprobar si existe el cultivo para ver si es inserción o actualización
            s = conn.prepareStatement("MCexisteCultivo");
            s.setString(1,cultivo.getIdCultivo().getIdCultivo());
            r = s.executeQuery();
            if (r.next())
                idCultivo = r.getString(1);
            
            s=null;
            r=null;
            
            if(idCultivo!=null)
                s = conn.prepareStatement("MCactualizarCultivo");
            else
                s = conn.prepareStatement("MCinsertarCultivo");
            
                       
            //Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
            //si vienen o no valores nulos de forma más cómoda 
            lstValores.clear();
        	lstTipos.clear();    
            
            lstValores.add(new Integer(cultivo.getDatosExpediente().getAnnoExpedienteAdminOrigenAlteracion()));
            lstTipos.add(TYPE_NUMERIC);
            
            lstValores.add(cultivo.getDatosExpediente().getReferenciaExpedienteAdminOrigen());            
            lstTipos.add(TYPE_VARCHAR);            
            
            if (cultivo.getDatosExpediente().getEntidadGeneradora()!=null)
                lstValores.add(new Integer(cultivo.getDatosExpediente().getEntidadGeneradora().getCodigo()));
            else
                lstValores.add(null);
            lstTipos.add(TYPE_VARCHAR);
            
            //Tipo movimiento
            lstValores.add(null);            
            lstTipos.add(TYPE_VARCHAR);            
            
            lstValores.add(cultivo.getTipoSuelo());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(cultivo.getIdCultivo().getParcelaCatastral().getRefCatastral());
            lstTipos.add(TYPE_VARCHAR);
            
            lstValores.add(cultivo.getCodSubparcela());
            lstTipos.add(TYPE_VARCHAR);
            
            lstValores.add(cultivo.getIdCultivo().getNumOrden());            
            lstTipos.add(TYPE_VARCHAR);       
            
            lstValores.add(cultivo.getTipoSubparcela());            
            lstTipos.add(TYPE_VARCHAR);       
            
            lstValores.add(cultivo.getSuperficieParcela());            
            lstTipos.add(TYPE_NUMERIC); 
            
            lstValores.add(cultivo.getIdCultivo().getCalifCultivo());            
            lstTipos.add(TYPE_VARCHAR);      
            
            lstValores.add(cultivo.getDenominacionCultivo());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(cultivo.getIntensidadProductiva());            
            lstTipos.add(TYPE_NUMERIC);  
            
            lstValores.add(cultivo.getCodBonificacion());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(cultivo.getEjercicioFinBonificacion());            
            lstTipos.add(TYPE_NUMERIC);  
            
            lstValores.add(cultivo.getValorCatastral());            
            lstTipos.add(TYPE_DOUBLE);  
            
            lstValores.add(cultivo.getCodModalidadReparto());            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(cultivo.getIdCultivo().getIdCultivo());            
            lstTipos.add(TYPE_VARCHAR);  
            
            
            
            createStatement(s, lstValores, lstTipos);
            
            s.execute();
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            // conn.close(); 
            
        }
        catch (DataException ex)
        {           
            throw new DataException(I18N.get("Importacion","importar.error.cultivos"), ex);
        	//logger.error("Error ", ex);
        }
        
    }
    
    /**
     * 
     * @param fc
     * @param fxcc
     * @return
     * @throws DataException
     */
    public void insertarDatosGraficos(FincaCatastro fc, FX_CC fxcc) //throws DataException
    throws Exception
    {
        PreparedStatement s = null;
		ResultSet r = null;
		String idFinca = null;
		
		Connection conn = getDBConnection();
		
		//Antes de nada, se comprueba si la finca a la que se quiere asociar informacion
		//gráfica está dada de alta en catastro real
		s = conn.prepareStatement("MCexisteFinca");
		s.setString(1, fc.getRefFinca().getRefCatastral());     
		r = s.executeQuery();
		if (r.next())
		    idFinca = r.getString(1);
		
		s=null;
		r=null;
		if (idFinca!=null)
		{
		    
		    //Comprobar si existe el fxcc para ver si es inserción o actualización
		    s = conn.prepareStatement("MCexisteFxcc");
		    s.setString(1,idFinca);
		    r = s.executeQuery();
		    
		    String idFxcc=null;
		    if (r.next())
		        idFxcc = r.getString(1);                
		    
		    s=null;
		    r=null;
		    
		    if(idFxcc!=null)
		        s = conn.prepareStatement("MCactualizarFxcc");
		    else
		        s = conn.prepareStatement("MCinsertarFxcc");
		    
		    //Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
		    //si vienen o no valores nulos de forma más cómoda 
		    lstValores.clear();
			lstTipos.clear();
		    
		    lstValores.add(fxcc.getASC());
		    lstTipos.add(TYPE_CLOB);
		    
		    lstValores.add(fxcc.getDXF());            
		    lstTipos.add(TYPE_CLOB);            
		    
		    lstValores.add(idFinca);
		    lstTipos.add(TYPE_VARCHAR);
		    
		    createStatement(s, lstValores, lstTipos);                
		   
		    s.execute();           
		    
		    
		}
		
		if (s!=null) s.close();
		if (r!= null) r.close(); 
		// conn.close();         
    }
    
    public void insertarDatosImagenes(FincaCatastro fc, ArrayList lstImagenes) 
    throws Exception
    {
    	if (lstImagenes != null){
    		
    		Iterator itImagenes = lstImagenes.iterator();
    		
    		while (itImagenes.hasNext()){
    			
    			try
    			{
    				ImagenCatastro imagen = (ImagenCatastro)itImagenes.next();    				
    				insertarDatosImagen(fc, imagen);
    			}
    			catch (DataException e){
    				throw new DataException(e);
    			}
    		}
    	}
        
    }
    
    /**
     * 
     * @param fc
     * @param imagen
     * @return
     * @throws DataException
     */
    public void insertarDatosImagen(FincaCatastro fc, ImagenCatastro imagen) 
    throws Exception
    {
        PreparedStatement s = null;
		ResultSet r = null;
		String idFinca = null;
		
		Connection conn = getDBConnection();
		
		//Antes de nada, se comprueba si la finca a la que se quiere asociar informacion
		//gráfica está dada de alta en catastro real
		s = conn.prepareStatement("MCexisteFinca");
		s.setString(1, fc.getRefFinca().getRefCatastral());     
		r = s.executeQuery();
		if (r.next())
		    idFinca = r.getString(1);
		
		s=null;
		r=null;
		if (idFinca!=null)
		{
		    
		    //Comprobar si existe la imagen para ver si es inserción o actualización
		    s = conn.prepareStatement("MCexisteImagen");
		    s.setString(1,idFinca);
		    r = s.executeQuery();
		    
		    String idImagen = null;
		    if (r.next())
		    	idImagen = r.getString(1);                
		    
		    s=null;
		    r=null;
		    
		    if(idImagen!=null)
		        s = conn.prepareStatement("MCactualizarImagen");
		    else
		        s = conn.prepareStatement("MCinsertarImagen");
		    
		    //Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
		    //si vienen o no valores nulos de forma más cómoda 
		    lstValores.clear();
			lstTipos.clear();
		    
		    lstValores.add(imagen.getNombre());
		    lstTipos.add(TYPE_VARCHAR);
		    
		    lstValores.add(imagen.getExtension()); 
		    lstTipos.add(TYPE_VARCHAR);
		    
		    lstValores.add(imagen.getTipoDocumento()); 
		    lstTipos.add(TYPE_VARCHAR);
		    
		    lstValores.add(imagen.getFoto());
		    lstTipos.add(TYPE_CLOB);            
		    
		    lstValores.add(idFinca);
		    lstTipos.add(TYPE_VARCHAR);
		    
		    createStatement(s, lstValores, lstTipos);                
		   
		    s.execute();
		    
		}
		
		if (s!=null) s.close();
		if (r!= null) r.close(); 
		// conn.close();         
    }
    
    public void asociarDatosGraficosTemporal(long idExpediente, FincaCatastro fc, FX_CC fxcc) throws DataException
    {
    	try
    	{    
    		PreparedStatement s = null;
    		ResultSet r = null;            

    		//Sólo se actualiza catastro temporal si ya hay un xml con datos (al menos tiene
    		//que haber información de finca catastral)

    		Connection conn = getDBConnection();

    		s = conn.prepareStatement("MCactualizarCatastroTemporal");

    		//Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
    		//si vienen o no valores nulos de forma más cómoda 
    		lstValores.clear();
    		lstTipos.clear();

    		//se inserta el nodo fxcc en adelante   
    		lstValores.add(fxcc.toXML());
    		lstTipos.add(TYPE_CLOB);

    		lstValores.add(new Long(idExpediente));
    		lstTipos.add(TYPE_NUMERIC);

    		lstValores.add(fc.getRefFinca().getRefCatastral());            
    		lstTipos.add(TYPE_VARCHAR);            

    		createStatement(s, lstValores, lstTipos);

    		s.execute();

    		if (s!=null) s.close();
    		if (r!= null) r.close(); 
    		// conn.close();    

    	}
    	catch (Exception ex)
    	{           
    		throw new DataException(I18N.get("Importacion","importar.error.graficos"), ex);
    		
    	}        
    }
    
    
    /**
     * 
     * @param dir
     * @return
     * @throws DataException
     */
    public int obtenerIdVia (DireccionLocalizacion dir) //throws DataException
    throws Exception
    {        
    	int idVia = 0;
    	PreparedStatement s = null;
    	ResultSet r = null;
    	String codProvincia = EdicionUtils.paddingString(String.valueOf(dir.getProvinciaINE()),
    			TAM_CODPRO,'0',true);
    	
    	String codMunicipioINE = EdicionUtils.paddingString(String.valueOf(dir.getMunicipioINE()),
    			TAM_CODMUNI,'0',true);

    	Connection conn = getDBConnection();
    	
    	if (codProvincia == null || codProvincia.equals("")){
			
			return idVia;
		}
		
		if (codMunicipioINE == null || codMunicipioINE.equals("")){
			
			return idVia;
		}
		
		String idMunicipio = null;
		idMunicipio = codProvincia.trim() + codMunicipioINE.trim();
		if (idMunicipio.length()>5){
			
			return idVia;
		}
		
		Integer identificadorMunicipio = null;
		try{
			identificadorMunicipio = new Integer(idMunicipio);
		}
		catch(Exception e){
			
			return idVia;
		}

    	//Comprobar si existe el municipio en concreto, 
    	s = conn.prepareStatement("MCexisteMunicipio");

    	s.setInt(1, identificadorMunicipio.intValue());

    	r = s.executeQuery();
    	if (r.next())
    	{
    		s=null;
    		
    		if (dir.getCodigoVia()!=0){

    			s = conn.prepareStatement("MCobtenerIdVia");

    			//Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
    			//si vienen o no valores nulos de forma más cómoda 
    			lstValores.clear();
    			lstTipos.clear();

    			lstValores.add(new Integer(dir.getCodigoVia()));
    			lstTipos.add(TYPE_NUMERIC);

    			lstValores.add(new Integer(codProvincia+codMunicipioINE));
    			lstTipos.add(TYPE_NUMERIC);

    			createStatement(s, lstValores, lstTipos);

    			r = s.executeQuery();  
    			if (r.next())
    			{
    				idVia = r.getInt(1);
    			}
    			
    			r=null;
        		s=null;
    		}
    		else{
    			
    			s = conn.prepareStatement("MCobtenerIdViaPorNombre");
    			
    			lstValores.clear();
    			lstTipos.clear();

    			lstValores.add(dir.getTipoVia());
    			lstTipos.add(TYPE_VARCHAR);
    			
    			lstValores.add(dir.getNombreVia());
    			lstTipos.add(TYPE_VARCHAR);

    			lstValores.add(new Integer(codProvincia+codMunicipioINE));
    			lstTipos.add(TYPE_NUMERIC);

    			createStatement(s, lstValores, lstTipos);

    			r = s.executeQuery();  
    			if (r.next())
    			{
    				idVia = r.getInt(1);
    			}
    			
    			r=null;
        		s=null;
    		}

    		
    		

    		if (idVia==0 && dir.getNombreVia()!=null)
    		{
    			//Siguiente id de vía
    			s = conn.prepareStatement("MCnextSecuenciaVia");
    			r = null;
    			r = s.executeQuery();
    			if (r.next())
    				idVia = r.getInt(1);

    			if (idVia!=0)
    			{

    				//Inserción de la vía
    				s = conn.prepareStatement("MCinsertarVia");

    				//Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
    				//si vienen o no valores nulos de forma más cómoda 
    				lstValores.clear();
    				lstTipos.clear();

    				lstValores.add(new Integer(idVia));
    				lstTipos.add(TYPE_NUMERIC);

    				try{
    					if (dir.getCodigoVia()!=0){
    						lstValores.add(new Integer(dir.getCodigoVia()));
    					}
    					else{
    						lstValores.add(null);
    					}
    				}
    				catch(Exception e)
    				{
    					lstValores.add(null);
    				}

    				lstTipos.add(TYPE_NUMERIC);

    				lstValores.add(dir.getTipoVia());
    				lstTipos.add(TYPE_VARCHAR);

    				lstValores.add(dir.getNombreVia());
    				lstTipos.add(TYPE_VARCHAR);

    				lstValores.add(new Integer(codProvincia+codMunicipioINE));
    				lstTipos.add(TYPE_NUMERIC);

    				createStatement(s, lstValores, lstTipos);

    				s.execute();                    
    			}                
    		}

    		if (s!=null) s.close();
    		if (r!= null) r.close(); 
    		// conn.close(); 

    	}
    	else{
    		
    	}

    	return idVia;
    }
    
    
    
    /**
     * 
     * @param dir
     * @return
     * @throws DataException
     */
    public int obtenerIdViaCatastro (DireccionLocalizacion dir) //throws DataException
    throws Exception
    {     
    	int idViaCatastro =  dir.getCodigoVia(); 
    	int idVia = 0;
    	PreparedStatement s = null;
		ResultSet r = null;
		String codProvincia = EdicionUtils.paddingString(String.valueOf(dir.getProvinciaINE()),
				TAM_CODPRO,'0',true);
		
		String codMunicipioINE = EdicionUtils.paddingString(String.valueOf(dir.getMunicipioINE()),
				TAM_CODMUNI,'0',true);

		Connection conn = getDBConnection();
		
		if (codProvincia == null || codProvincia.equals("")){
			
			return idViaCatastro;
		}
		
		if (codMunicipioINE == null || codMunicipioINE.equals("")){
			
			return idViaCatastro;
		}
		
		String idMunicipio = null;
		idMunicipio = codProvincia.trim() + codMunicipioINE.trim();
		if (idMunicipio.length()>5){
			
			return idViaCatastro;
		}
		
		Integer identificadorMunicipio = null;
		try{
			identificadorMunicipio = new Integer(idMunicipio);
		}
		catch(Exception e){
			
			return idViaCatastro;
		}
		
		
		//Comprobar si existe el municipio en concreto, 
		s = conn.prepareStatement("MCexisteMunicipio");

		s.setInt(1, identificadorMunicipio.intValue());

		r = s.executeQuery();
		if (r.next())
		{
			s=null;
			
			s = conn.prepareStatement("MCobtenerIdVia");

			//Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
			//si vienen o no valores nulos de forma más cómoda 
			lstValores.clear();
			lstTipos.clear();
			
			lstValores.add(new Integer(dir.getCodigoVia()));
			lstTipos.add(TYPE_NUMERIC);

			lstValores.add(new Integer(codProvincia+codMunicipioINE));
			lstTipos.add(TYPE_NUMERIC);

			createStatement(s, lstValores, lstTipos);

			r = s.executeQuery();  
			if (r.next())
			{
				idVia = r.getInt(1);
			}        

			r=null;
			s=null;

			if (idVia==0)
			{
				//Siguiente id de vía
				s = conn.prepareStatement("MCnextSecuenciaVia");
				r = null;
				r = s.executeQuery();
				if (r.next())
					idVia = r.getInt(1);

				if (idVia!=0 && dir.getNombreVia()!=null)
				{

					//Inserción de la vía
					s = conn.prepareStatement("MCinsertarVia");

					//Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
					//si vienen o no valores nulos de forma más cómoda 
					lstValores.clear();
		        	lstTipos.clear();
		        	
					lstValores.add(new Integer(idVia));
					lstTipos.add(TYPE_NUMERIC);

					lstValores.add(new Integer(dir.getCodigoVia()));
					lstTipos.add(TYPE_NUMERIC);

					lstValores.add(dir.getTipoVia());
					lstTipos.add(TYPE_VARCHAR);

					lstValores.add(dir.getNombreVia());
					lstTipos.add(TYPE_VARCHAR);

					lstValores.add(new Integer(codProvincia+codMunicipioINE));
					lstTipos.add(TYPE_NUMERIC);

					createStatement(s, lstValores, lstTipos);

					s.execute();                    
				}                
			}

			if (s!=null) s.close();
			if (r!= null) r.close(); 
			// conn.close(); 
		}
		else{
			
		}
        
        return idViaCatastro;
    }
    
    /**
     * 
     * @param dir
     * @return
     * @throws SQLException 
     * @throws DataException
     */
    public void obtenerCodigoParaje (DireccionLocalizacion dir) throws SQLException //throws DataException

    {     
    	int codigoParajeCatastro = 0;
    	int identificador = 0;


    	if (dir.getCodParaje() != null && !dir.getCodParaje().equals("") 
    			&& dir.getMunicipioINE() != null && !dir.getMunicipioINE().equals("")
    			&& dir.getProvinciaINE() != null && !dir.getProvinciaINE().equals("")){

    		PreparedStatement s = null;
    		ResultSet r = null;

    		Connection conn = getDBConnection();

    		try{
    			
    			String codProvincia = EdicionUtils.paddingString(String.valueOf(dir.getProvinciaINE().trim()), 
    					2,'0', true);
    			
    			String codMunicipio = EdicionUtils.paddingString(String.valueOf(dir.getMunicipioINE().trim()),
        				3,'0',true);

        		String codParaje = EdicionUtils.paddingString(String.valueOf(dir.getCodParaje().trim()),
        				5,'0',true);

        		identificador = new Integer(codProvincia + codMunicipio + codParaje).intValue();
        		
    			//Comprobar si existe el municipio en concreto, 
    			s = conn.prepareStatement("MCexisteParaje");
    			s.setInt(1, identificador);

    			r = s.executeQuery();
    			if (!r.next())
    			{			
    				r=null;
    				s=null;

    				//Inserción del paraje
    				s = conn.prepareStatement("MCinsertarParaje");

    				//Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
    				//si vienen o no valores nulos de forma más cómoda 
    				lstValores.clear();
    				lstTipos.clear();
    				
    				codigoParajeCatastro =  new Integer(dir.getCodParaje()).intValue();

    				lstValores.add(new Integer(identificador));
    				lstTipos.add(TYPE_NUMERIC);
    				
    				lstValores.add(new Integer(codigoParajeCatastro));
    				lstTipos.add(TYPE_NUMERIC);

    				lstValores.add(dir.getNombreParaje());
    				lstTipos.add(TYPE_VARCHAR);    				

    				createStatement(s, lstValores, lstTipos);

    				s.execute();

    				if (s!=null) s.close();
    				if (r!= null) r.close(); 
    			}
    		}
    		catch(Exception e){
    			if (s!=null) s.close();
    			if (r!= null) r.close(); 
    			return;
    		}
    	}
    }

    
    
    
    /**
     * 
     * @param fc
     * @return
     * @throws DataException
     */
    public void insertarDatosFinca (FincaCatastro fc, boolean insertarExpediente, Expediente exp)
    //throws DataException
    throws Exception
    {       
        try
        {         
            if (exp!=null)
                fc.setDatosExpediente(exp);
            if (insertarExpediente && exp!=null)
                insertarDatosExpediente (exp);                
            
            PreparedStatement s = null;
            ResultSet r = null;
            Integer idFinca = null;
            
            Connection conn = getDBConnection();
            
            //Comprobar si existe la finca para ver si es inserción o actualización
            s = conn.prepareStatement("MCexisteFinca");
            s.setString(1,fc.getRefFinca().getRefCatastral());
            r = s.executeQuery();
            if (r.next())
                idFinca = new Integer(r.getInt(1));
            
            String codProvincia = EdicionUtils.paddingString(String.valueOf(fc.getDirParcela().getProvinciaINE()),
                    TAM_CODPRO,'0',true);
            String codMunicipio = EdicionUtils.paddingString(String.valueOf(fc.getDirParcela().getCodigoMunicipioDGC()),
                    TAM_CODMUNI,'0',true);
            String codCompletoMunicipio = codProvincia + codMunicipio;
            
            s=null;
            r=null;
            if(idFinca!=null)
                s = conn.prepareStatement("MCactualizarFinca");
            else
            {
                s = conn.prepareStatement("MCnextSecuenciaFinca");
                r = s.executeQuery();
                if (r.next())
                    idFinca = new Integer(r.getInt(1));
                
                r=null;
                s=null;
                s = conn.prepareStatement("MCinsertarFinca");
                
            }
            fc.setIdFinca(idFinca.intValue());
            
            //TODO Se utiliza como identificador de vía el vias.codigocatastro
            int idVia = obtenerIdViaCatastro(fc.getDirParcela());
            if (idVia!=0)
                fc.getDirParcela().setIdVia(idVia);
            else
                fc.getDirParcela().setIdVia(fc.getDirParcela().getCodigoVia());
            
            //Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
            //si vienen o no valores nulos de forma más cómoda 
            lstValores.clear();
        	lstTipos.clear();
        	
        	if (fc.getDirParcela() != null){
        		obtenerCodigoParaje(fc.getDirParcela());
        		lstValores.clear();
            	lstTipos.clear();
        	}
        	
            lstValores.add(fc.getRefFinca().getRefCatastral());
            lstTipos.add(TYPE_VARCHAR);
            
            lstValores.add((codCompletoMunicipio!=null && !codCompletoMunicipio.trim().equals(""))?  
                    new Integer(codCompletoMunicipio): null);
            lstTipos.add(TYPE_NUMERIC);
            
            if (fc.getDirParcela().getPrimerNumero() == -1){
            	lstValores.add(null);
            }
            else{
            	lstValores.add(new Integer(fc.getDirParcela().getPrimerNumero()));
            }
            lstTipos.add(TYPE_NUMERIC);            
            
            lstValores.add(fc.getDirParcela().getPrimeraLetra());
            lstTipos.add(TYPE_VARCHAR);
            
            if (fc.getDirParcela().getSegundoNumero() == -1){
            	lstValores.add(null);
            }
            else{
            	lstValores.add(new Integer(fc.getDirParcela().getSegundoNumero()));
            }
            lstTipos.add(TYPE_NUMERIC);            
            
            lstValores.add(fc.getDirParcela().getSegundaLetra());
            lstTipos.add(TYPE_VARCHAR);
            
            if (fc.getDirParcela().getKilometro() == -1){
            	lstValores.add(null);
            }
            else{
            	lstValores.add(new Double(fc.getDirParcela().getKilometro()));
            }
            lstTipos.add(TYPE_DOUBLE);       
            
            lstValores.add(fc.getDirParcela().getBloque());            
            lstTipos.add(TYPE_VARCHAR);       
            
            lstValores.add(fc.getDirParcela().getDireccionNoEstructurada());            
            lstTipos.add(TYPE_VARCHAR);      

            /* JAVIER si codigo_postal es int
            if (fc.getDirParcela().getCodigoPostal() == -1){
            	lstValores.add(null);
            }
            else{
            	lstValores.add(new Integer(fc.getDirParcela().getCodigoPostal()));
            }
            lstTipos.add(TYPE_NUMERIC);  
            */
            lstValores.add(fc.getDirParcela().getCodigoPostal());
            lstTipos.add(TYPE_VARCHAR);

            lstValores.add(new Date());
            lstTipos.add(TYPE_VARCHAR);  
            
            //fecha de baja
            lstValores.add(null);            
            lstTipos.add(TYPE_VARCHAR);  
            
            lstValores.add(fc.getCodDelegacionMEH());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(fc.getDirParcela().getNombreEntidadMenor());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(fc.getDirParcela().getDistrito());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(fc.getDirParcela().getCodZonaConcentracion());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(fc.getDirParcela().getCodPoligono());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(fc.getDirParcela().getCodParcela());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(fc.getDirParcela().getNombreParaje());            
            lstTipos.add(TYPE_VARCHAR); 
            
            //TODO: Revisar. Es el codigocatastro de vias
            lstValores.add(new Integer(fc.getDirParcela().getIdVia()));            
            lstTipos.add(TYPE_NUMERIC);  
            
            // JAVIER lstValores.add(new Integer(fc.getDatosExpediente().getAnnoExpedienteGerencia()));
            lstValores.add(fc.getDatosExpediente().getAnnoExpedienteGerencia());            
            lstTipos.add(TYPE_NUMERIC);
            
            lstValores.add(fc.getDatosExpediente().getNumeroExpediente());            
            lstTipos.add(TYPE_VARCHAR); 
            
            if (fc.getDatosExpediente().getEntidadGeneradora()!=null)
                lstValores.add(new Integer(fc.getDatosExpediente().getEntidadGeneradora().getCodigo()));
            else
                lstValores.add(null);
            lstTipos.add(TYPE_VARCHAR); 
            
            //TODO tipo de movimiento
            lstValores.add(null);            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(fc.getCodMunicipioDGC());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(fc.getBICE());            
            lstTipos.add(TYPE_VARCHAR); 
            
            //lstValores.add(String.valueOf(fc.getDirParcela().getProvinciaINE()));
            lstValores.add(fc.getDirParcela().getProvinciaINE());
            lstTipos.add(TYPE_VARCHAR); 
            
            //lstValores.add(String.valueOf(fc.getDirParcela().getCodigoMunicipioDGC()));
            lstValores.add(fc.getDirParcela().getCodigoMunicipioDGC());
            lstTipos.add(TYPE_VARCHAR); 
            
            //lstValores.add(String.valueOf(fc.getDirParcela().getMunicipioINE()));
            lstValores.add(fc.getDirParcela().getMunicipioINE());
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(fc.getDirParcela().getCodMunOrigenAgregacion());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(fc.getDirParcela().getCodParaje());            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(fc.getDatosFisicos().getSupFinca());            
            lstTipos.add(TYPE_NUMERIC);  
            
            lstValores.add(fc.getDatosFisicos().getSupTotal());            
            lstTipos.add(TYPE_NUMERIC);  
            
            lstValores.add(fc.getDatosFisicos().getSupSobreRasante());            
            lstTipos.add(TYPE_NUMERIC);  
            
            lstValores.add(fc.getDatosFisicos().getSupCubierta());            
            lstTipos.add(TYPE_NUMERIC);  
            
            lstValores.add(fc.getDatosFisicos().getXCoord());            
            //lstTipos.add(TYPE_NUMERIC);
            lstTipos.add(TYPE_FLOAT);
            
            lstValores.add(fc.getDatosFisicos().getYCoord());            
            //lstTipos.add(TYPE_NUMERIC);
            lstTipos.add(TYPE_FLOAT);
            
            lstValores.add(fc.getDatosEconomicos().getAnioAprobacion());            
            lstTipos.add(TYPE_NUMERIC);  
            
            lstValores.add(fc.getDatosEconomicos().getCodigoCalculoValor());            
            lstTipos.add(TYPE_NUMERIC);  
            
            lstValores.add(fc.getDatosEconomicos().getPoligonoCatastralValor().getCodPoligono());            
            lstTipos.add(TYPE_VARCHAR); 
            
            //lstValores.add(String.valueOf(fc.getDatosEconomicos().getIndModalidadReparto()));
            lstValores.add(fc.getDatosEconomicos().getIndModalidadReparto());
            lstTipos.add(TYPE_VARCHAR); 
            
            //lstValores.add(String.valueOf(fc.getDatosEconomicos().getIndInfraedificabilidad()));
            lstValores.add(fc.getDatosEconomicos().getIndInfraedificabilidad());
            lstTipos.add(TYPE_VARCHAR); 
            
            //TODO: MOVIMIENTO BAJA
            lstValores.add(null);            
            lstTipos.add(TYPE_VARCHAR); 
            
            lstValores.add(idFinca);            
            lstTipos.add(TYPE_NUMERIC); 
                        
            createStatement(s, lstValores, lstTipos);
            
            s.execute();            
            
            if (s!=null) s.close();
            if (r!= null) r.close(); 
            //// conn.close(); 
            
        }
        catch (DataException ex)
        {           
            //throw new DataException(I18N.get("Importacion","importar.error.fincas"), ex);
        	logger.error("Error ", ex);
        }
        
    }
    
    /**
     * Inserta/actualiza los datos del expediente y de entidad generadora en base de datos
     * @param datosExpediente Objeto Expediente con los datos del expediente
     * @return Identificador del expediente
     * @throws DataException 
     */
    public Long insertarDatosExpediente(Expediente datosExpediente) //throws DataException
    throws Exception
    {       
        Long idExpediente=null;
        
        PreparedStatement s = null;
		ResultSet r = null;
		
		lstValores.clear();
		lstTipos.clear();
		
		Connection conn = getDBConnection();
		
		Long idEntidadGeneradora = null;
		
		//Comprobar si existe la entidad generadora
		if (datosExpediente.getEntidadGeneradora()!=null && 
				datosExpediente.getEntidadGeneradora().getCodigo()>=0)
		{
		    s = conn.prepareStatement("MCexisteEntidadGeneradora");
		    s.setInt(1,datosExpediente.getEntidadGeneradora().getCodigo());
		    r = s.executeQuery();
		    if (r.next())
		        idEntidadGeneradora = new Long(r.getLong(1));       
		    
		    s=null;
		    r=null;
		    if(idEntidadGeneradora!=null)
		        s = conn.prepareStatement("MCactualizarEntidadGeneradora");
		    else
		    {
		        s = conn.prepareStatement("MCnextSecuenciaEntidadGeneradora");
		        r = s.executeQuery();
		        if (r.next())
		            idEntidadGeneradora = new Long(r.getLong(1));     
		        s=null;
		        r=null;
		        s = conn.prepareStatement("MCinsertarEntidadGeneradora");
		    }
		    datosExpediente.getEntidadGeneradora().setIdEntidadGeneradora(idEntidadGeneradora.longValue());
		    
		    lstValores.add(new Integer(datosExpediente.getEntidadGeneradora().getCodigo()));
		    lstTipos.add(TYPE_NUMERIC);
		    
		    lstValores.add(datosExpediente.getEntidadGeneradora().getTipo());
		    lstTipos.add(TYPE_VARCHAR);
		    
		    lstValores.add(datosExpediente.getEntidadGeneradora().getDescripcion());
		    lstTipos.add(TYPE_VARCHAR);
		    
		    lstValores.add(datosExpediente.getEntidadGeneradora().getNombre());
		    lstTipos.add(TYPE_VARCHAR);
		    
		    lstValores.add(idEntidadGeneradora);
		    lstTipos.add(TYPE_NUMERIC);
		    
		    createStatement(s, lstValores, lstTipos);
		    s.execute();                
		}
		Long idDomicilio = null;
		
		//domicilio_localizacion
		if (datosExpediente.getDireccionPresentador()!=null
		        && datosExpediente.getDireccionPresentador().getIdLocalizacion()==0)
		{ 
		    s = conn.prepareStatement("MCnextSecuenciaDomicilioLocalizacion");
		    r = s.executeQuery();
		    if (r.next())
		        idDomicilio = new Long(r.getLong(1));     
		    s=null;
		    r=null;
		    
		    datosExpediente.getDireccionPresentador().setIdLocalizacion(idDomicilio.longValue());
		    if(idDomicilio!=null)
		    {
		    	lstValores.clear();
		    	lstTipos.clear();
		    	                    
		        s = conn.prepareStatement("MCinsertarDomicilioLocalizacion");
		        datosExpediente.getDireccionPresentador().setIdLocalizacion(idDomicilio.longValue());
		        
		        lstValores.add(idDomicilio);
		        lstTipos.add(TYPE_NUMERIC);
		        
		        lstValores.add(new Integer(datosExpediente.getDireccionPresentador().getProvinciaINE()));
		        lstTipos.add(TYPE_NUMERIC);
		        
		        lstValores.add(new Integer(datosExpediente.getDireccionPresentador().getMunicipioINE()));
		        lstTipos.add(TYPE_NUMERIC);
		        
		        lstValores.add(new Integer(datosExpediente.getDireccionPresentador().getCodigoMunicipioDGC()));
		        lstTipos.add(TYPE_NUMERIC);
		        
		        lstValores.add(datosExpediente.getDireccionPresentador().getNombreEntidadMenor());
		        lstTipos.add(TYPE_VARCHAR);
		        
		        
		        
		        lstValores.add(new Integer(datosExpediente.getDireccionPresentador().getCodigoVia()));
		        lstTipos.add(TYPE_NUMERIC);
		        
		        lstValores.add(new Integer(datosExpediente.getDireccionPresentador().getPrimerNumero()));
		        lstTipos.add(TYPE_NUMERIC);
		        
		        lstValores.add(datosExpediente.getDireccionPresentador().getPrimeraLetra());
		        lstTipos.add(TYPE_VARCHAR);
		        
		        lstValores.add(new Integer(datosExpediente.getDireccionPresentador().getSegundoNumero()));
		        lstTipos.add(TYPE_NUMERIC);
		        
		        lstValores.add(datosExpediente.getDireccionPresentador().getSegundaLetra());
		        lstTipos.add(TYPE_VARCHAR);
		        
		        
		        
		        lstValores.add(new Double(datosExpediente.getDireccionPresentador().getKilometro()));
		        lstTipos.add(TYPE_NUMERIC);
		        
		        lstValores.add(datosExpediente.getDireccionPresentador().getBloque());
		        lstTipos.add(TYPE_VARCHAR);
		        
		        lstValores.add(datosExpediente.getDireccionPresentador().getEscalera());
		        lstTipos.add(TYPE_VARCHAR);
		        
		        lstValores.add(datosExpediente.getDireccionPresentador().getPlanta());
		        lstTipos.add(TYPE_VARCHAR);
		        
		        lstValores.add(datosExpediente.getDireccionPresentador().getPuerta());
		        lstTipos.add(TYPE_VARCHAR);
		        
		        
		        
		        lstValores.add(datosExpediente.getDireccionPresentador().getDireccionNoEstructurada());
		        lstTipos.add(TYPE_VARCHAR);
		        
		        lstValores.add(new Integer(datosExpediente.getDireccionPresentador().getCodigoPostal()));
		        lstTipos.add(TYPE_NUMERIC);
		        
		        lstValores.add(datosExpediente.getDireccionPresentador().getNombreVia());
		        lstTipos.add(TYPE_VARCHAR);
		        
		        lstValores.add(String.valueOf(datosExpediente.getDireccionPresentador().getApartadoCorreos()));
		        lstTipos.add(TYPE_VARCHAR);
		        
		        lstValores.add(datosExpediente.getDireccionPresentador().getTipoVia());
		        lstTipos.add(TYPE_VARCHAR);
		        
		        
		        lstValores.add(datosExpediente.getDireccionPresentador().getNombreProvincia());
		        lstTipos.add(TYPE_VARCHAR);
		        
		        lstValores.add(datosExpediente.getDireccionPresentador().getNombreMunicipio());
		        lstTipos.add(TYPE_VARCHAR);
		        
		        
		        createStatement(s, lstValores, lstTipos);
		        s.execute();                     
		        
		    }
		}
		
		
		
		//Comprobar si existe el expediente para ver si es inserción o actualización
		s=null;
		r=null;
		s = conn.prepareStatement("MCexisteExpediente");
		s.setString(1,datosExpediente.getNumeroExpediente());
		r = s.executeQuery();
		if (r.next())
		    idExpediente = new Long(r.getLong(1));
		
		s=null;
		r=null;
		if(idExpediente!=null)
		    s = conn.prepareStatement("MCactualizarExpediente");
		else
		{
		    s = conn.prepareStatement("MCnextSecuenciaExpediente");
		    r = s.executeQuery();
		    if (r.next())
		        idExpediente = new Long(r.getLong(1));
		    
		    r=null;
		    s=null;
		    s = conn.prepareStatement("MCinsertarExpediente");                
		}
		
		datosExpediente.setIdExpediente(idExpediente.longValue());
		lstValores.clear();
		lstTipos.clear();
		            
		if (datosExpediente.getEntidadGeneradora()!=null)
		    lstValores.add(new Long(datosExpediente.getEntidadGeneradora().getIdEntidadGeneradora()));
		else
		    lstValores.add(null);
		lstTipos.add(TYPE_NUMERIC);
		
		//Estado cerrado (tanto en modo acoplado como en desacoplado, se 
		//trata de datos válidos en catastro)
		lstValores.add(new Long(Expediente.CERRADO));
		lstTipos.add(TYPE_NUMERIC);
		
		//TODO: El identificador del técnico de catastro no puede ser nulo            
		if (((AppContext)AppContext.getApplicationContext()).getBlackboard().get("idUserCatastro")!=null)
		    datosExpediente.setIdTecnicoCatastro(aplicacion.getBlackboard().get("idUserCatastro").toString());
		else //si no hay nada, de momento pone como técnico el syssuperuser
		    datosExpediente.setIdTecnicoCatastro("100");
		
		lstValores.add(datosExpediente.getIdTecnicoCatastro()!=null?
		        new Long(datosExpediente.getIdTecnicoCatastro()):null);
		lstTipos.add(TYPE_NUMERIC);
		
		
		if (datosExpediente.getDireccionPresentador()!=null)
		    lstValores.add(new Long(datosExpediente.getDireccionPresentador().getIdLocalizacion()));
		else
		    lstValores.add(null);
		lstTipos.add(TYPE_NUMERIC);
		lstValores.add(new Long(datosExpediente.getIdMunicipio()));
		lstTipos.add(TYPE_NUMERIC);
		lstValores.add(datosExpediente.getNumeroExpediente());
		lstTipos.add(TYPE_VARCHAR);
         
		lstValores.add(datosExpediente.getTipoExpediente()!=null?
		        datosExpediente.getTipoExpediente().getCodigoTipoExpediente():null);
		lstTipos.add(TYPE_VARCHAR);
		lstValores.add(datosExpediente.getFechaAlteracion());
		lstTipos.add(new Integer(Types.DATE));

        // JAVIER lstValores.add(new Long(datosExpediente.getAnnoExpedienteGerencia()));
        lstValores.add(datosExpediente.getAnnoExpedienteGerencia());
        lstTipos.add(TYPE_NUMERIC);

        lstValores.add(datosExpediente.getReferenciaExpedienteGerencia());
		lstTipos.add(TYPE_VARCHAR);

        //JAVIER lstValores.add(new Integer(datosExpediente.getCodigoEntidadRegistroDGCOrigenAlteracion()));
        lstValores.add(datosExpediente.getCodigoEntidadRegistroDGCOrigenAlteracion());
        lstTipos.add(TYPE_NUMERIC);
		
		lstValores.add(new Integer(datosExpediente.getAnnoExpedienteAdminOrigenAlteracion()));
		lstTipos.add(TYPE_NUMERIC);
		lstValores.add(datosExpediente.getReferenciaExpedienteAdminOrigen());
		lstTipos.add(TYPE_VARCHAR);
		lstValores.add(datosExpediente.getFechaRegistro());
		lstTipos.add(new Integer(Types.DATE));
		lstValores.add(datosExpediente.getFechaMovimiento());
		lstTipos.add(new Integer(Types.DATE));
		lstValores.add(datosExpediente.getHoraMovimiento());
		lstTipos.add(TYPE_VARCHAR);
		
		lstValores.add(datosExpediente.getTipoDocumentoOrigenAlteracion());
		lstTipos.add(TYPE_VARCHAR);
		lstValores.add(datosExpediente.getInfoDocumentoOrigenAlteracion());
		lstTipos.add(TYPE_VARCHAR);
		lstValores.add(datosExpediente.getCodigoDescriptivoAlteracion());
		lstTipos.add(TYPE_VARCHAR);
		lstValores.add(datosExpediente.getDescripcionAlteracion());
		lstTipos.add(TYPE_VARCHAR);
		lstValores.add(datosExpediente.getNifPresentador());
		lstTipos.add(TYPE_VARCHAR);
		
		lstValores.add(datosExpediente.getNombreCompletoPresentador());
		lstTipos.add(TYPE_VARCHAR);
		lstValores.add(new Integer(datosExpediente.getNumBienesInmueblesUrbanos()));
		lstTipos.add(TYPE_NUMERIC);
		lstValores.add(new Integer(datosExpediente.getNumBienesInmueblesRusticos()));
		lstTipos.add(TYPE_NUMERIC);
		lstValores.add(new Integer(datosExpediente.getNumBienesInmueblesCaractEsp()));
		lstTipos.add(TYPE_NUMERIC);
		lstValores.add(datosExpediente.getFechaDeCierre());
		lstTipos.add(new Integer(Types.DATE));
		
		lstValores.add(datosExpediente.getCodProvinciaNotaria());
		lstTipos.add(TYPE_VARCHAR);
		lstValores.add(datosExpediente.getCodPoblacionNotaria());
		lstTipos.add(TYPE_VARCHAR);
		lstValores.add(datosExpediente.getCodNotaria());
		lstTipos.add(TYPE_VARCHAR);
		//JAVIER lstValores.add(new Integer (datosExpediente.getAnnoProtocoloNotarial()));
        //JAVIER lstTipos.add(TYPE_NUMERIC);
        lstValores.add(datosExpediente.getAnnoProtocoloNotarial());
        lstTipos.add(TYPE_VARCHAR);
		lstValores.add(datosExpediente.getProtocoloNotarial());
		lstTipos.add(TYPE_VARCHAR);
		
		lstValores.add(new Long(datosExpediente.getIdExpediente()));
		lstTipos.add(TYPE_NUMERIC);
		
		createStatement(s, lstValores, lstTipos);
		
		s.execute();            
		
		if (s!=null) s.close();
		if (r!= null) r.close(); 
		// conn.close(); 
        
        return idExpediente;
    }

    private void createStatement(PreparedStatement s, ArrayList lstValores, ArrayList lstTipos) throws SQLException
    {
        for (int i=0; i< lstTipos.size(); i++)
        {
            Object o = lstValores.get(i);
            
            switch (((Integer)lstTipos.get(i)).intValue())
            {
            case Types.VARCHAR:
                if (o == null)
                    s.setNull(i+1, Types.VARCHAR);
                else
                    s.setString(i+1, String.valueOf(o));
                break;
                
            case Types.INTEGER:
            case Types.NUMERIC:
                if (o == null)
                {
                    s.setNull(i+1, ((Integer)lstTipos.get(i)).intValue());
                }
                else{
                    if (o instanceof Integer)
                        s.setInt(i+1, ((Integer)o).intValue());
                    else if (o instanceof Long)
                        s.setLong(i+1, ((Long)o).longValue());
                }
                
                break;
            case Types.FLOAT:
                if (o == null)
                    s.setNull(i+1, Types.FLOAT);
                else
                    s.setFloat(i+1, ((Float)o).floatValue());
                break;
            case Types.DOUBLE:
                if (o == null)
                    s.setNull(i+1, Types.DOUBLE);
                else
                    s.setDouble(i+1, ((Double)o).doubleValue());
                break;
            case Types.BOOLEAN:
                if (o == null)
                    s.setNull(i+1, Types.BOOLEAN);
                else
                    s.setBoolean(i+1, ((Boolean)o).booleanValue());
                break;
            case Types.LONGVARBINARY:
                if (o == null)
                    s.setNull(i+1, Types.LONGVARBINARY);
                else
                    s.setObject(i+1, (String)(o));
                break;
                
            case Types.CLOB:
                if (o == null)
                    s.setNull(i+1, Types.CLOB);
                else{
                    //ByteArrayInputStream str = new ByteArrayInputStream(((String)o).getBytes());
                    //s.setBinaryStream(i+1, str, ((String)o).length());
                    s.setObject(i+1, (String)(o)); 
                    
                }    
                break;
            case Types.OTHER:
                if (o == null)
                    s.setNull(i+1, Types.OTHER);
                else{                  	
                    s.setString(i+1, "SRID=" + ((Polygon)o).getSRID() + ";" + ((Polygon)(o)).toString()); 
                    
                }            
                break;
            }
        }
    }
    
    /**
     * Obtiene un arraylist de objetos de tipo Via con todas las vías de catastro
     * del sistema
     * 
     * @return ArrayList de objetos Via
     * @throws DataException Si se produce un error de acceso a datos 
     */
    public ArrayList obtenerViasCatastro () //throws DataException
    throws Exception
    {        
        ArrayList lstVias = new ArrayList();
        
        Connection conn = getDBConnection();
		
		PreparedStatement s = null;
		ResultSet r = null;
		
		s = conn.prepareStatement("MCobtenerViasCatastro");
		s.setInt(1,AppContext.getIdMunicipio());
		
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
		// conn.close(); 
		
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
        
        return lstVias;
    }

    /**
     * 
     * @param lstParcelasImportadas
     * @return
     * @throws DataException 
     */
    public ArrayList obtenerParcelasAfectadas(ArrayList lstParcelasImportadas) throws DataException
    //throws Exception
    {    
       
        	
        Set hsParcelas = new HashSet();
        ArrayList lstParcelas = new ArrayList();
        
        PreparedStatement s = null;
		ResultSet r = null;
		
		 try{
		
		Connection conn = getDBConnection();
		
		s = conn.prepareStatement("MCobtenerParcelasTemporal");
		r = s.executeQuery();  
		while (r.next())
		{
		    //Compara la lista de parcelas que se están modificando en 
		    //catastro temporal, con la lista de parcelas importadas.
		    //La lista resultante ha de ser la intersección entre ambas
		    Iterator it = lstParcelasImportadas.iterator();
		    while (it.hasNext())
		    {
		        ReferenciaCatastral rc = (ReferenciaCatastral)it.next();
		        if (rc.getRefCatastral().equals(r.getString("ref_catastral")))
		        {
		            Expediente exp = new Expediente();
		            EntidadGeneradora eg = new EntidadGeneradora();
		            eg.setIdEntidadGeneradora(r.getLong("id_entidad_generadora"));
		            eg.setCodigo(r.getInt("codigo"));
		            eg.setDescripcion(r.getString("descripcion"));
		            eg.setNombre(r.getString("nombre"));
		            eg.setTipo(r.getString("tipo"));
		            exp.setEntidadGeneradora(eg);
		            
		            exp.setIdExpediente(r.getLong("id_expediente"));
		            exp.setIdTecnicoCatastro(r.getString("id_tecnico_catastro"));
		            exp.setIdMunicipio(r.getLong("id_municipio"));
		            exp.setNumeroExpediente(r.getString("numero_expediente"));
		            exp.setFechaAlteracion(r.getDate("fecha_alteracion"));

                    // JAVIER exp.setAnnoExpedienteGerencia(r.getInt("anio_expediente_gerencia"));
                    String anoExpGerencia = r.getString("anio_expediente_gerencia");
                    exp.setAnnoExpedienteGerencia( anoExpGerencia != null? new Integer(anoExpGerencia): null);

                    exp.setReferenciaExpedienteGerencia(r.getString("referencia_expediente_gerencia"));

                    String codEntidadRegDGCOrigAlteracion = null;
                    if(CPoolDatabase.isPostgres(conn)){
                        codEntidadRegDGCOrigAlteracion = r.getString("codigo_entidad_registro_dgc_origen_alteracion");
                        exp.setAnnoExpedienteAdminOrigenAlteracion(r.getInt("anio_expediente_admin_origen_alteracion"));
                        exp.setReferenciaExpedienteAdminOrigen(r.getString("referencia_expediente_admin_origen"));
                        exp.setTipoDocumentoOrigenAlteracion(r.getString("tipo_documento_origen_alteracion"));
                        exp.setInfoDocumentoOrigenAlteracion(r.getString("info_documento_origen_alteracion"));

                    }
                    else {
                        codEntidadRegDGCOrigAlteracion = r.getString("codent_reg_dgc_orig_alteracion");
                        exp.setAnnoExpedienteAdminOrigenAlteracion(r.getInt("anio_exp_admin_orig_alteracion"));
                        exp.setReferenciaExpedienteAdminOrigen(r.getString("referencia_exp_admin_origen"));
                        exp.setTipoDocumentoOrigenAlteracion(r.getString("tipo_documento_orig_alteracion"));
                        exp.setInfoDocumentoOrigenAlteracion(r.getString("info_documento_orig_alteracion"));
                    }

                    exp.setCodigoEntidadRegistroDGCOrigenAlteracion(codEntidadRegDGCOrigAlteracion!=null? new Integer(codEntidadRegDGCOrigAlteracion) : null);

		            exp.setFechaRegistro(r.getDate("fecha_registro"));
		            exp.setFechaMovimiento(r.getDate("fecha_movimiento"));
		            exp.setHoraMovimiento(r.getString("hora_movimiento"));
		            exp.setCodigoDescriptivoAlteracion(r.getString("codigo_descriptivo_alteracion"));
		            exp.setDescripcionAlteracion(r.getString("descripcion_alteracion"));
		            exp.setNifPresentador(r.getString("nif_presentador"));
		            exp.setNombreCompletoPresentador(r.getString("nombre_completo_presentador"));
		            exp.setNumBienesInmueblesUrbanos(r.getInt("num_bienesinmuebles_urb"));
		            exp.setNumBienesInmueblesRusticos(r.getInt("num_bienesinmuebles_rus"));
		            exp.setNumBienesInmueblesCaractEsp(r.getInt("num_bienesinmuebles_esp"));
		            exp.setFechaDeCierre(r.getDate("fecha_de_cierre"));
		            exp.setCodProvinciaNotaria(r.getString("cod_provincia_notaria"));
		            exp.setCodPoblacionNotaria(r.getString("cod_poblacion_notaria"));
		            exp.setCodNotaria(r.getString("cod_notaria"));
		            //JAVIER exp.setAnnoProtocoloNotarial(Integer.parseInt(r.getString("anio_protocolo_notarial")));
                    exp.setAnnoProtocoloNotarial(r.getString("anio_protocolo_notarial"));
                    exp.setProtocoloNotarial(r.getString("protocolo_notarial"));
		              
		            ParcelaAfectada pa = new ParcelaAfectada();
		            pa.setExpediente(exp);
		            pa.setRefParcela(r.getString("ref_catastral"));
		            //pa.setIdFinca(r.getInt("id_parcela"));
		            
		            hsParcelas.add(pa);
		        }
		    }
		}
		
		if (s!=null) s.close();
		if (r!= null) r.close(); 
		// conn.close(); 
		
		Comparator paComparator = new Comparator(){
		    public int compare(Object o1, Object o2) {
		        ParcelaAfectada pa1 = (ParcelaAfectada) o1;
		        ParcelaAfectada pa2 = (ParcelaAfectada) o2;
		        
		        String desc1 = pa1.getExpediente().getNumeroExpediente();
		        String desc2 = pa2.getExpediente().getNumeroExpediente();                    
		        
		        Collator myCollator=Collator.getInstance(new Locale(locale));
		        myCollator.setStrength(Collator.PRIMARY);
		        return myCollator.compare(desc1, desc2);                    
		    }
		};            
		
		lstParcelas.addAll(hsParcelas);
		Collections.sort(lstParcelas, paComparator);
		
	    }
	    catch (Exception ex)
	    {           
	        throw new DataException(I18N.get("Importacion","importar.error.fincas"), ex);
	  	  //logger.error("Error ", ex);
	    }
        
        return lstParcelas;
    }
    
   /**
    * 
    * @return
    * @throws DataException
    */
    public ArrayList obtenerParcelasAfectadas() throws DataException
    //throws Exception
    {        
        ArrayList lstParcelas = new ArrayList();
        
        PreparedStatement s = null;
		ResultSet r = null;
		
		try{
		
		Connection conn = getDBConnection();
		
		s = conn.prepareStatement("MCobtenerParcelasTemporal");
		r = s.executeQuery();  
		while (r.next())
		{
		    
		    
		    Expediente exp = new Expediente();
		    EntidadGeneradora eg = new EntidadGeneradora();
		    eg.setIdEntidadGeneradora(r.getLong("id_entidad_generadora"));
		    eg.setCodigo(r.getInt("codigo"));
		    eg.setDescripcion(r.getString("descripcion"));
		    eg.setNombre(r.getString("nombre"));
		    eg.setTipo(r.getString("tipo"));
		    exp.setEntidadGeneradora(eg);
		    
		    exp.setIdExpediente(r.getLong("id_expediente"));
		    exp.setIdTecnicoCatastro(r.getString("id_tecnico_catastro"));
		    exp.setIdMunicipio(r.getLong("id_municipio"));
		    exp.setNumeroExpediente(r.getString("numero_expediente"));
		    exp.setFechaAlteracion(r.getDate("fecha_alteracion"));

            String anoExpGerencia = r.getString("anio_expediente_gerencia");
            exp.setAnnoExpedienteGerencia( anoExpGerencia!=null? new Integer(anoExpGerencia): null);

            exp.setReferenciaExpedienteGerencia(r.getString("referencia_expediente_gerencia"));

            String codEntidadRegDGCOrigAlteracion = null;
            if(CPoolDatabase.isPostgres(conn)){
                codEntidadRegDGCOrigAlteracion = r.getString("codigo_entidad_registro_dgc_origen_alteracion");
                exp.setAnnoExpedienteAdminOrigenAlteracion(r.getInt("anio_expediente_admin_origen_alteracion"));
                exp.setReferenciaExpedienteAdminOrigen(r.getString("referencia_expediente_admin_origen"));
                exp.setTipoDocumentoOrigenAlteracion(r.getString("tipo_documento_origen_alteracion"));
                exp.setInfoDocumentoOrigenAlteracion(r.getString("info_documento_origen_alteracion"));
            }
            else{
                codEntidadRegDGCOrigAlteracion = r.getString("codent_reg_dgc_orig_alteracion");
                exp.setAnnoExpedienteAdminOrigenAlteracion(r.getInt("anio_exp_admin_orig_alteracion"));
                exp.setReferenciaExpedienteAdminOrigen(r.getString("referencia_exp_admin_origen"));
                exp.setTipoDocumentoOrigenAlteracion(r.getString("tipo_documento_orig_alteracion"));
                exp.setInfoDocumentoOrigenAlteracion(r.getString("info_documento_orig_alteracion"));
            }

            exp.setCodigoEntidadRegistroDGCOrigenAlteracion( codEntidadRegDGCOrigAlteracion!=null? new Integer(codEntidadRegDGCOrigAlteracion) : null);

		    exp.setFechaRegistro(r.getDate("fecha_registro"));
		    exp.setFechaMovimiento(r.getDate("fecha_movimiento"));
		    exp.setHoraMovimiento(r.getString("hora_movimiento"));
		    exp.setCodigoDescriptivoAlteracion(r.getString("codigo_descriptivo_alteracion"));
		    exp.setDescripcionAlteracion(r.getString("descripcion_alteracion"));
		    exp.setNifPresentador(r.getString("nif_presentador"));
		    exp.setNombreCompletoPresentador(r.getString("nombre_completo_presentador"));
		    exp.setNumBienesInmueblesUrbanos(r.getInt("num_bienesinmuebles_urb"));
		    exp.setNumBienesInmueblesRusticos(r.getInt("num_bienesinmuebles_rus"));
		    exp.setNumBienesInmueblesCaractEsp(r.getInt("num_bienesinmuebles_esp"));
		    exp.setFechaDeCierre(r.getDate("fecha_de_cierre"));
		    exp.setCodProvinciaNotaria(r.getString("cod_provincia_notaria"));
		    exp.setCodPoblacionNotaria(r.getString("cod_poblacion_notaria"));
		    exp.setCodNotaria(r.getString("cod_notaria"));
		     //JAVIER exp.setAnnoProtocoloNotarial(Integer.parseInt(r.getString("anio_protocolo_notarial")));
		    exp.setAnnoProtocoloNotarial(r.getString("anio_protocolo_notarial"));
            exp.setProtocoloNotarial(r.getString("protocolo_notarial"));
		    
		    ParcelaAfectada pa = new ParcelaAfectada();
		    pa.setExpediente(exp);
		    pa.setRefParcela(r.getString("ref_catastral"));
		    //pa.setIdFinca(r.getInt("id_parcela"));
		    
		    lstParcelas.add(pa);
		    
		    
		}
		
		if (s!=null) s.close();
		if (r!= null) r.close(); 
//		// conn.close(); 
		
		Comparator paComparator = new Comparator(){
		    public int compare(Object o1, Object o2) {
		        ParcelaAfectada pa1 = (ParcelaAfectada) o1;
		        ParcelaAfectada pa2 = (ParcelaAfectada) o2;
		        
		        String desc1 = pa1.getExpediente().getNumeroExpediente();
		        String desc2 = pa2.getExpediente().getNumeroExpediente();                    
		        
		        Collator myCollator=Collator.getInstance(new Locale(locale));
		        myCollator.setStrength(Collator.PRIMARY);
		        return myCollator.compare(desc1, desc2);                    
		    }
		};            
		
		Collections.sort(lstParcelas, paComparator);
		
    }
    catch (Exception ex)
    {           
        throw new DataException(I18N.get("Importacion","importar.error.fincas"), ex);
  	  //logger.error("Error ", ex);
    }
        
        return lstParcelas;
    }
        
    
    /*
    public Expediente getExpedienteByRef(String refExpediente) throws DataException 
    {
        
        //TODO Consiste en obtener el Expediente completo, a partir de los datos
        //de la base de datos, o del fin de entrada
        UnidadDatosRetorno infoExpediente = null;
        Expediente expediente = null;
        try{
            PreparedStatement s = null;
            ResultSet r = null;                  
            
            //Obtener datos del expediente
            s = conn.prepareStatement("MCseleccionarExpediente");  
            s.setString(1, "12346");
            r = s.executeQuery();
            if (r.next())
            {
                expediente = new Expediente();
                expediente.setIdExpediente(Long.parseLong(refExpediente));
                EntidadGeneradora entidadGen = new EntidadGeneradora();
                entidadGen.setIdEntidadGeneradora(r.getLong(1));
                entidadGen.setCodigo(r.getInt(2));
                entidadGen.setDescripcion(r.getString(3));
                entidadGen.setNombre(r.getString(4));
                entidadGen.setTipo(r.getString(5));
                
                expediente.setEntidadGeneradora(entidadGen);
                expediente.setIdEstado(r.getInt(6));
                expediente.setIdTecnicoCatastro(r.getLong(7));
                DireccionLocalizacion dir = new DireccionLocalizacion();
                dir.setIdLocalizacion(r.getLong(8));
                expediente.setDireccionPresentador(dir);
                expediente.setIdMunicipio(r.getLong(9));
                expediente.setNumeroExpediente(r.getString(10));
                
                expediente.setTipoExpediente(r.getString(11));
                expediente.setFechaAlteracion(r.getDate(9));
                expediente.setAnnoExpedienteGerencia(r.getInt(10));
                expediente.setReferenciaExpedienteGerencia(r.getString(11));
                expediente.setCodigoEntidadRegistroDGCOrigenAlteracion(r.getInt(12));
                
                expediente.setAnnoExpedienteAdminOrigenAlteracion(r.getInt(13));
                expediente.setReferenciaExpedienteAdminOrigen(r.getString(14));
                expediente.setFechaRegistro(r.getDate(15));
                expediente.setFechaMovimiento(r.getDate(16));
                expediente.setHoraMovimiento(r.getTime(17));
                
                expediente.setTipoDocumentoOrigenAlteracion(r.getString(18));
                expediente.setInfoDocumentoOrigenAlteracion(r.getString(19));
                expediente.setCodigoDescriptivoAlteracion(r.getString(20));
                expediente.setDescripcionAlteracion(r.getString(21));
                expediente.setNifPresentador(r.getString(22));
                
                expediente.setNombreCompletoPresentador(r.getString(23));
                expediente.setNumBienesInmueblesUrbanos(r.getInt(24));
                expediente.setNumBienesInmueblesRusticos(r.getInt(25));
                expediente.setNumBienesInmueblesCaractEsp(r.getInt(26));
                expediente.setFechaDeCierre(r.getDate(27));
                
                expediente.setCodProvinciaNotaria(r.getString(28));
                expediente.setCodPoblacionNotaria(r.getString(29));
                expediente.setCodNotaria(r.getString(30));
                expediente.setAnnoProtocoloNotarial(r.getInt(31));
                expediente.setProtocoloNotarial(r.getString(32));
            }
            r=null;
            s=null;
            
//            s = conn.prepareStatement("MCseleccionarFincas");  
//            s.setString(1, refExpediente);
//            r = s.executeQuery();
//            if (r.next())
//            {
//            UnidadDatosIntercambio contenidoExpediente = new UnidadDatosIntercambio();
//            contenidoExpediente.set
//            
            
            
        }
        catch (Exception ex)
        {           
            throw new DataException(ex);
        }        
        
        
        return expediente;
    }
    */

    /**
     * 
     * @param udsa
     * @throws DataException
     */
    public void insertarDatosSalida (UnidadDatosIntercambio udsa, boolean insertarExpediente, 
            Expediente exp) //throws DataException
    throws Exception
    {  
    		
        Geometry geometriaParcela = null;
        if (udsa.getFincaCatastro()!=null){
        	if(udsa.getFxcc()!=null){
        		if(udsa.getFxcc().getDXF()!=null){
        			//String dxf = ImportarUtils.base64ToAscii(udsa.getFxcc().getDXF());
        			String dxf = udsa.getFxcc().getDXF();
        			geometriaParcela = ImportarUtils.obtenerGeometriaParcela(dxf, null);
        			if(geometriaParcela!=null)
        				insertarDatosFinca(geometriaParcela, udsa.getFincaCatastro(), insertarExpediente, exp);
        			else
        				insertarDatosFinca(udsa.getFincaCatastro(), insertarExpediente, exp);
        		}else
        			insertarDatosFinca(udsa.getFincaCatastro(), insertarExpediente, exp);
        	}
        	else{
        		insertarDatosFinca(udsa.getFincaCatastro(), insertarExpediente, exp);
        	}
        }

        udsa.setFincaCatastro(null);
        
        
        insertarListaDatos(udsa.getLstSuelos(), insertarExpediente, exp);
        udsa.setLstSuelos(null);
        insertarListaDatos(udsa.getLstUCs(), insertarExpediente, exp);     
        udsa.setLstUCs(null);
        insertarListaDatos(udsa.getLstBienesInmuebles(), insertarExpediente, exp); 
        udsa.setLstBienesInmuebles(null);
        insertarListaDatos(udsa.getLstCultivos(), insertarExpediente, exp);
        udsa.setLstCultivos(null);
        insertarListaDatos(udsa.getLstConstrucciones(), insertarExpediente, exp);
        udsa.setLstConstrucciones(null);
        insertarListaDatos(udsa.getLstRepartos(), insertarExpediente, exp);   
        udsa.setLstRepartos(null);
        
        if (udsa.getLstImagenes() != null && udsa.getLstImagenes().size() > 0){
        	insertarDatosImagenes(udsa.getFincaCatastro(), udsa.getLstImagenes());
        	udsa.setLstImagenes(null);
        }
        
        if (udsa.getFincaCatastro()!=null && udsa.getFxcc()!=null) {
            insertarDatosGraficos(udsa.getFincaCatastro(), udsa.getFxcc());
            udsa.setFxcc(null);
        }
        
        System.gc();
            	        
    }
    
    /**
     * 
     * @param udretorno
     * @throws DataException
     */
    public void insertarDatosRetorno(UnidadDatosRetorno udretorno) //throws DataException
    throws Exception
    {   
        insertarDatosExpediente(udretorno.getExpediente());
     
        Iterator itUDSalida = udretorno.getLstSituaciones().iterator();
        while (itUDSalida.hasNext())
        {
            UnidadDatosIntercambio udsa = (UnidadDatosIntercambio)itUDSalida.next();
            insertarDatosSalida(udsa, false, null);            
        }
    }
    
  /**
   * Inserta un registro de bien inmueble en las tablas correspondientes
   * @param linea Linea del fichero de datos de padrón en formato TXT (registro 53)
   * 
   * @throws DataException
   */
    public void insertarRegistroBI (String linea) //throws DataException
    throws Exception
    {
        BienInmuebleJuridico bij = new BienInmuebleJuridico();
        BienInmuebleCatastro bi = new BienInmuebleCatastro();
        
        bi.setClaseBienInmueble(linea.substring(7,9));
        
        bi.getIdBienInmueble().setParcelaCatastral(linea.substring(9,23));
        bi.getIdBienInmueble().setNumCargo(linea.substring(23, 27));
        bi.getIdBienInmueble().setDigControl1(linea.substring(27, 28));
        bi.getIdBienInmueble().setDigControl2(linea.substring(28, 29));
        
        bi.setNumFijoInmueble(new Integer(linea.substring(29, 37)));
        bi.setIdAyuntamientoBienInmueble(linea.substring(37, 52));
        
        bi.getDomicilioTributario().setProvinciaINE(linea.substring(52, 54));
        bi.getDomicilioTributario().setNombreProvincia(linea.substring(54, 79));
        bi.getDomicilioTributario().setCodigoMunicipioDGC(linea.substring(79, 82));
        bi.getDomicilioTributario().setMunicipioINE(linea.substring(82, 85));
        bi.getDomicilioTributario().setNombreMunicipio(linea.substring(85,125));
        bi.getDomicilioTributario().setNombreEntidadMenor(linea.substring(125, 155));
        bi.getDomicilioTributario().setCodigoVia(Integer.parseInt(linea.substring(155, 160)));
        //TODO: Transformar: la que viene es la de ine, y se espera la de catastro (2 caracteres)
        //bi.getDomicilioTributario().setTipoVia(linea.substring(160, 165));
        bi.getDomicilioTributario().setNombreVia(linea.substring(165, 190));
        bi.getDomicilioTributario().setPrimerNumero(Integer.parseInt(linea.substring(190, 194)));
        bi.getDomicilioTributario().setPrimeraLetra(linea.substring(194, 195));
        bi.getDomicilioTributario().setSegundoNumero(Integer.parseInt(linea.substring(195, 199)));
        bi.getDomicilioTributario().setSegundaLetra(linea.substring(199, 200));
        bi.getDomicilioTributario().setKilometro(new Double(linea.substring(200, 203)+"."+linea.substring(203, 205)).doubleValue());
        bi.getDomicilioTributario().setBloque(linea.substring(205, 209));
        bi.getDomicilioTributario().setEscalera(linea.substring(209, 211));
        bi.getDomicilioTributario().setPlanta(linea.substring(211, 214));
        bi.getDomicilioTributario().setPuerta(linea.substring(214, 217));
        bi.getDomicilioTributario().setDireccionNoEstructurada(linea.substring(217, 242));
        bi.getDomicilioTributario().setCodigoPostal(linea.substring(242, 247));
        bi.getDomicilioTributario().setDistrito(linea.substring(247, 249));
        bi.getDomicilioTributario().setCodMunOrigenAgregacion(linea.substring(249, 252));
        bi.getDomicilioTributario().setCodZonaConcentracion(linea.substring(252, 254));
        bi.getDomicilioTributario().setCodPoligono(linea.substring(254,257));
        bi.getDomicilioTributario().setCodParcela(linea.substring(257, 262));
        bi.getDomicilioTributario().setNombreParaje(linea.substring(262, 292));
        
        bi.getDatosEconomicosBien().setAnioValorCat(new Integer(linea.substring(292, 296)));
        bi.getDatosEconomicosBien().setValorCatastral(new Double (linea.substring(296, 308)));
        bi.getDatosEconomicosBien().setValorCatastralSuelo(new Double(linea.substring(308, 320)));
        bi.getDatosEconomicosBien().setValorCatastralConstruccion(new Double (linea.substring(320, 332)));
        bi.getDatosEconomicosBien().setBaseLiquidable(new Long (linea.substring(332, 344)));
        bi.getDatosEconomicosBien().setUso(linea.substring(344, 345));
        bi.getDatosEconomicosBien().setImporteBonificacionRustica(new Long(linea.substring(345, 357)));
        bi.getDatosEconomicosBien().setClaveBonificacionRustica(linea.substring(357, 358));
        bi.getDatosEconomicosBien().setSuperficieCargoFincaConstruida(new Long(linea.substring(358, 368)));
        bi.getDatosEconomicosBien().setSuperficieCargoFincaRustica(new Long(linea.substring(368, 378)));
        bi.getDatosEconomicosBien().setCoefParticipacion(new Float(linea.substring(378, 381)+"."+linea.substring(381, 387)));

        bi.getDatosBaseLiquidable().setValorBase(new Double(linea.substring(387, 397)+"."+linea.substring(397, 399)));
        bi.getDatosBaseLiquidable().setProcedenciaValorBase(linea.substring(399, 400));
        bi.getDatosBaseLiquidable().setEjercicioIBI(new Integer(linea.substring(400, 404)));
        bi.getDatosBaseLiquidable().setValorCatastralIBI(new Double(linea.substring(404, 414)+"."+linea.substring(414,416)));
        bi.getDatosBaseLiquidable().setEjercicioRevTotal(new Integer(linea.substring(416, 420)));
        bi.getDatosBaseLiquidable().setEjercicioRevParcial(new Integer(linea.substring(420, 424)));
        bi.getDatosBaseLiquidable().setPeriodoTotal(new Integer(linea.substring(424, 426)));
       
        TipoExpediente tipoExpediente = new TipoExpediente();
        tipoExpediente.setCodigoTipoExpediente(linea.substring(803, 807));
        bij.getDatosExpediente().setTipoExpediente(tipoExpediente);
        bij.getDatosExpediente().setFechaAlteracion(new Date(Integer.parseInt(linea.substring(807, 811)),
                Integer.parseInt(linea.substring(811, 813)), Integer.parseInt(linea.substring(813, 815))));
        bij.getDatosExpediente().setAnnoExpedienteAdminOrigenAlteracion(Integer.parseInt(linea.substring(815, 819)));
        bij.getDatosExpediente().setReferenciaExpedienteAdminOrigen(linea.substring(821, 829));
        bij.getDatosExpediente().setNumeroExpediente(linea.substring(836,849));
        
        EntidadGeneradora eg = new EntidadGeneradora();
        eg.setCodigo(Integer.parseInt(linea.substring(829, 832)));
        bij.getDatosExpediente().setEntidadGeneradora(eg);
        
        bij.setBienInmueble(bi);
        bij.getDatosExpediente().setEntidadGeneradora(eg);
       
        insertarDatosBIJ(bij, false, null);
        
    }
    
    /**
     * Inserta un registro de titular de un bien inmueble en las tablas correspondientes
     * @param linea Linea del fichero de datos de padrón en formato TXT (registro 54)
     * 
     * @throws DataException
     */
      public void insertarRegistroTitular (String linea) //throws DataException
      throws Exception
      {
          IdBienInmueble idBi = new IdBienInmueble();
          Titular pers = new Titular();
          
          idBi.setParcelaCatastral(linea.substring(9, 23));
          idBi.setNumCargo(linea.substring(23, 27));
          idBi.setDigControl1(linea.substring(27, 28));
          idBi.setDigControl2(linea.substring(28, 29));
          
          pers.getDerecho().setCodDerecho(linea.substring(52, 54));
          pers.getDerecho().setPorcentajeDerecho(ImportarUtils.strToFloat(linea.substring(54, 57)+"."+linea.substring(57,59)));
          pers.getDerecho().setIdBienInmueble(idBi);
          
          pers.setNif(linea.substring(59,68));
          pers.setRazonSocial(linea.substring(68, 128));
          if (!linea.substring(128, 136).trim().equals("") && !linea.substring(128, 136).trim().equals("00000000")
        		  && !linea.substring(128, 136).trim().equals("0000000") && !linea.substring(128, 136).trim().equals("000000")
        		  && !linea.substring(128, 136).trim().equals("00000") && !linea.substring(128, 136).trim().equals("0000")
        		  && !linea.substring(128, 136).trim().equals("000") && !linea.substring(128, 136).trim().equals("00") 
        		  && !linea.substring(128, 136).trim().equals("0"))
              pers.setNif(linea.substring(128, 136));
          
          pers.getDomicilio().setProvinciaINE(linea.substring(136, 138));
          pers.getDomicilio().setNombreProvincia(linea.substring(138, 163));
          pers.getDomicilio().setCodigoMunicipioDGC(linea.substring(163, 166));
          pers.getDomicilio().setMunicipioINE(linea.substring(166,169));
          pers.getDomicilio().setNombreMunicipio(linea.substring(169, 209));
          pers.getDomicilio().setNombreEntidadMenor(linea.substring(209, 239));
          pers.getDomicilio().setCodigoVia(Integer.parseInt(linea.substring(239,244)));
          //TODO Transformar: la que viene es la de ine, y se espera la de catastro (2 caracteres)
          //pers.getDomicilio().setTipoVia(linea.substring(244,249));
          pers.getDomicilio().setNombreVia(linea.substring(249, 274));
          pers.getDomicilio().setPrimerNumero(Integer.parseInt(linea.substring(274, 278)));
          pers.getDomicilio().setPrimeraLetra(linea.substring(278,279));
          pers.getDomicilio().setSegundoNumero(Integer.parseInt(linea.substring(279, 283)));
          pers.getDomicilio().setSegundaLetra(linea.substring(283, 284));
          pers.getDomicilio().setKilometro(ImportarUtils.strToDouble(linea.substring(284,287)+"."+linea.substring(287, 289)));
          pers.getDomicilio().setBloque(linea.substring(289, 293));
          pers.getDomicilio().setEscalera(linea.substring(293, 295));
          pers.getDomicilio().setPlanta(linea.substring(295, 298));
          pers.getDomicilio().setPuerta(linea.substring(298, 301));
          pers.getDomicilio().setDireccionNoEstructurada(linea.substring(301, 326));
          pers.getDomicilio().setCodigoPostal(linea.substring(326, 331));
          pers.getDomicilio().setApartadoCorreos(Integer.parseInt(linea.substring(331, 336)));
          
          pers.setNifCb(linea.substring(338, 345));
          pers.setComplementoTitularidad(linea.substring(345, 405));
          
          TipoExpediente tipoExp = new TipoExpediente();
          tipoExp.setCodigoTipoExpediente(linea.substring(405, 409));
          pers.getExpediente().setTipoExpediente(tipoExp);
          pers.getExpediente().setFechaAlteracion(new Date(Integer.parseInt(linea.substring(409, 413)),
                  Integer.parseInt(linea.substring(413, 415)), Integer.parseInt(linea.substring(415, 417))));
          pers.getExpediente().setAnnoExpedienteAdminOrigenAlteracion(Integer.parseInt(linea.substring(417, 421)));
          pers.getExpediente().setReferenciaExpedienteAdminOrigen(linea.substring(423, 431));
          //JAVIER pers.getExpediente().setCodigoEntidadRegistroDGCOrigenAlteracion(Integer.parseInt(linea.substring(431, 434)));
          pers.getExpediente().setCodigoEntidadRegistroDGCOrigenAlteracion(Integer.valueOf(linea.substring(431, 434)));
          
         
          insertarDatosPersona(idBi, pers, true);
      }

      public void insertarDatosFinca (Geometry geometriaFinca, FincaCatastro fc, boolean insertarExpediente, Expediente exp)
      //throws DataException
      throws Exception
      {       
          try
          {         
              if (exp!=null)
                  fc.setDatosExpediente(exp);
              if (insertarExpediente && exp!=null)
                  insertarDatosExpediente (exp);                
              
              PreparedStatement s = null;
              ResultSet r = null;
              Integer idFinca = null;
              
              //Comprobar si existe la finca para ver si es inserción o actualización
              Connection conn = getDBConnection();
              s = conn.prepareStatement("MCexisteFinca");
              s.setString(1,fc.getRefFinca().getRefCatastral());
              r = s.executeQuery();
              if (r.next())
                  idFinca = new Integer(r.getInt(1));
              
              String codProvincia = EdicionUtils.paddingString(String.valueOf(fc.getDirParcela().getProvinciaINE()),
                      TAM_CODPRO,'0',true);
              String codMunicipio = EdicionUtils.paddingString(String.valueOf(fc.getDirParcela().getCodigoMunicipioDGC()),
                      TAM_CODMUNI,'0',true);
              String codCompletoMunicipio = codProvincia + codMunicipio;
              
              s=null;
              r=null;
              if(idFinca!=null)
                  s = conn.prepareStatement("MCactualizarFincaYGeometria");
              else
              {
                  s = conn.prepareStatement("MCnextSecuenciaFinca");
                  r = s.executeQuery();
                  if (r.next())
                      idFinca = new Integer(r.getInt(1));
                  
                  r=null;
                  s=null;
                  s = conn.prepareStatement("MCinsertarFincaYGeometria");
                  
              }
              fc.setIdFinca(idFinca.intValue());
              
              //TODO Se utiliza como identificador de vía el vias.codigocatastro
              int idVia = obtenerIdViaCatastro(fc.getDirParcela());
              if (idVia!=0)
                  fc.getDirParcela().setIdVia(idVia);
              else
                  fc.getDirParcela().setIdVia(fc.getDirParcela().getCodigoVia());
              
              //Se crea una lista con los tipos de valores y otra lista con los valores para comprobar
              //si vienen o no valores nulos de forma más cómoda 
              lstValores.clear();
              lstTipos.clear();

              if (fc.getDirParcela() != null){
            	  obtenerCodigoParaje(fc.getDirParcela());
            	  lstValores.clear();
            	  lstTipos.clear();
              }          	
              
              lstValores.add(fc.getRefFinca().getRefCatastral());
              lstTipos.add(TYPE_VARCHAR);
              
              lstValores.add((codCompletoMunicipio!=null && !codCompletoMunicipio.trim().equals(""))?  
                      new Integer(codCompletoMunicipio): null);
              lstTipos.add(TYPE_NUMERIC);

              if (fc.getDirParcela().getPrimerNumero() == -1){
            	  lstValores.add(null);
              }
              else{
            	  lstValores.add(new Integer(fc.getDirParcela().getPrimerNumero()));
              }
              lstTipos.add(TYPE_NUMERIC);            
              
              lstValores.add(fc.getDirParcela().getPrimeraLetra());
              lstTipos.add(TYPE_VARCHAR);

              if (fc.getDirParcela().getSegundoNumero() == -1){
            	  lstValores.add(null);
              }
              else{
            	  lstValores.add(new Integer(fc.getDirParcela().getSegundoNumero()));
              }
              lstTipos.add(TYPE_NUMERIC);            
              
              lstValores.add(fc.getDirParcela().getSegundaLetra());
              lstTipos.add(TYPE_VARCHAR);

              if (fc.getDirParcela().getKilometro() == -1){
            	  lstValores.add(null);
              }
              else{
            	  lstValores.add(new Double(fc.getDirParcela().getKilometro()));
              }
              lstTipos.add(TYPE_DOUBLE);       
              
              lstValores.add(fc.getDirParcela().getBloque());            
              lstTipos.add(TYPE_VARCHAR);       
              
              lstValores.add(fc.getDirParcela().getDireccionNoEstructurada());            
              lstTipos.add(TYPE_VARCHAR);      

              /* JAVIER si codigo_postal tipo int
              if (fc.getDirParcela().getCodigoPostal() == -1){
            	  lstValores.add(null);
              }
              else{
            	  lstValores.add(new Integer(fc.getDirParcela().getCodigoPostal()));
              }
              lstTipos.add(TYPE_NUMERIC);
              */
              lstValores.add(fc.getDirParcela().getCodigoPostal());
              lstTipos.add(TYPE_VARCHAR);

              lstValores.add(new Date());
              lstTipos.add(TYPE_VARCHAR);  
              
              //fecha de baja
              lstValores.add(null);            
              lstTipos.add(TYPE_VARCHAR);  
              
              lstValores.add(fc.getCodDelegacionMEH());            
              lstTipos.add(TYPE_VARCHAR); 
              
              lstValores.add(fc.getDirParcela().getNombreEntidadMenor());            
              lstTipos.add(TYPE_VARCHAR); 
              
              lstValores.add(fc.getDirParcela().getDistrito());            
              lstTipos.add(TYPE_VARCHAR); 
              
              lstValores.add(fc.getDirParcela().getCodZonaConcentracion());            
              lstTipos.add(TYPE_VARCHAR); 
              
              lstValores.add(fc.getDirParcela().getCodPoligono());            
              lstTipos.add(TYPE_VARCHAR); 
              
              lstValores.add(fc.getDirParcela().getCodParcela());            
              lstTipos.add(TYPE_VARCHAR); 
              
              lstValores.add(fc.getDirParcela().getNombreParaje());            
              lstTipos.add(TYPE_VARCHAR); 
              
              //TODO: Revisar. Es el codigocatastro de vias
              lstValores.add(new Integer(fc.getDirParcela().getIdVia()));            
              lstTipos.add(TYPE_NUMERIC);  
              
              // JAVIER lstValores.add(new Integer(fc.getDatosExpediente().getAnnoExpedienteGerencia()));
              lstValores.add(fc.getDatosExpediente().getAnnoExpedienteGerencia());          
              lstTipos.add(TYPE_NUMERIC);
              
              lstValores.add(fc.getDatosExpediente().getNumeroExpediente());            
              lstTipos.add(TYPE_VARCHAR); 
              
              if (fc.getDatosExpediente().getEntidadGeneradora()!=null)
                  lstValores.add(new Integer(fc.getDatosExpediente().getEntidadGeneradora().getCodigo()));
              else
                  lstValores.add(null);
              lstTipos.add(TYPE_VARCHAR); 
              
              //TODO tipo de movimiento
              lstValores.add(null);            
              lstTipos.add(TYPE_VARCHAR); 
              
              lstValores.add(fc.getCodMunicipioDGC());            
              lstTipos.add(TYPE_VARCHAR); 
              
              lstValores.add(fc.getBICE());            
              lstTipos.add(TYPE_VARCHAR); 
              
              lstValores.add(String.valueOf(fc.getDirParcela().getProvinciaINE()));            
              lstTipos.add(TYPE_VARCHAR); 
              
              lstValores.add(String.valueOf(fc.getDirParcela().getCodigoMunicipioDGC()));            
              lstTipos.add(TYPE_VARCHAR); 
              
              lstValores.add(String.valueOf(fc.getDirParcela().getMunicipioINE()));            
              lstTipos.add(TYPE_VARCHAR); 
              
              lstValores.add(fc.getDirParcela().getCodMunOrigenAgregacion());            
              lstTipos.add(TYPE_VARCHAR); 
              
              lstValores.add(fc.getDirParcela().getCodParaje());            
              lstTipos.add(TYPE_VARCHAR); 
              
              lstValores.add(fc.getDatosFisicos().getSupFinca());            
              lstTipos.add(TYPE_NUMERIC);  
              
              lstValores.add(fc.getDatosFisicos().getSupTotal());            
              lstTipos.add(TYPE_NUMERIC);  
              
              lstValores.add(fc.getDatosFisicos().getSupSobreRasante());            
              lstTipos.add(TYPE_NUMERIC);  
              
              lstValores.add(fc.getDatosFisicos().getSupCubierta());            
              lstTipos.add(TYPE_NUMERIC);  
              
              lstValores.add(fc.getDatosFisicos().getXCoord());            
              lstTipos.add(TYPE_NUMERIC);  
              
              lstValores.add(fc.getDatosFisicos().getYCoord());            
              lstTipos.add(TYPE_NUMERIC);  
              
              lstValores.add(fc.getDatosEconomicos().getAnioAprobacion());            
              lstTipos.add(TYPE_NUMERIC);  
              
              lstValores.add(fc.getDatosEconomicos().getCodigoCalculoValor());            
              lstTipos.add(TYPE_NUMERIC);  
              
              lstValores.add(fc.getDatosEconomicos().getPoligonoCatastralValor().getCodPoligono());            
              lstTipos.add(TYPE_VARCHAR); 
              
              lstValores.add(String.valueOf(fc.getDatosEconomicos().getIndModalidadReparto()));            
              lstTipos.add(TYPE_VARCHAR); 
              
              lstValores.add(String.valueOf(fc.getDatosEconomicos().getIndInfraedificabilidad()));            
              lstTipos.add(TYPE_VARCHAR); 
              
              //TODO: MOVIMIENTO BAJA
              lstValores.add(null);            
              lstTipos.add(TYPE_VARCHAR); 
              
              lstValores.add(geometriaFinca);            
              lstTipos.add(TYPE_OTHER); 
              
              lstValores.add(idFinca);            
              lstTipos.add(TYPE_NUMERIC); 
                          
              createStatement(s, lstValores, lstTipos);
              
              s.execute();            
              
              if (s!=null) s.close();
              if (r!= null) r.close(); 
             
              
          }
          catch (DataException ex)
          {           
              throw new DataException(I18N.get("Importacion","importar.error.fincas"), ex);
        	  //logger.error("Error ", ex);
          }
          
      }


	/*public void releaseResources() {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/

    
}
