package com.geopista.app.catastro.servicioWebCatastro.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.exception.DataException;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportacionOperations;
import com.geopista.app.catastro.model.beans.BienInmuebleJuridico;
import com.geopista.app.catastro.model.intercambio.importacion.xml.contents.UnidadDatosIntercambio;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.ComunidadBienes;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.ElementoReparto;
import com.geopista.app.catastro.model.beans.FX_CC;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.IdBienInmueble;
import com.geopista.app.catastro.model.beans.Persona;
import com.geopista.app.catastro.model.beans.RepartoCatastro;
import com.geopista.app.catastro.model.beans.SueloCatastro;
import com.geopista.app.catastro.model.beans.Titular;
import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.I18N;

public class WSOperations {

	/**
     * Contexto de la aplicación
     */
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
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
    
    private ArrayList lstValores = new ArrayList();
    private ArrayList lstTipos = new ArrayList();

	private Connection con=null;
	
	private static final Log logger = LogFactory.getLog(ImportacionOperations.class);
    
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
    
    public void insertarDatosCatastrales (UnidadDatosIntercambio unidadDatosIntercambio)   throws Exception{
    	
    	if(unidadDatosIntercambio.getFincaCatastro() != null){
    		insertarDatosFinca(unidadDatosIntercambio.getFincaCatastro());
    	}
    	if(unidadDatosIntercambio.getLstSuelos() != null && !unidadDatosIntercambio.getLstSuelos().isEmpty()){
    		insertarListaDatos(unidadDatosIntercambio.getLstSuelos());
    	}
    	if(unidadDatosIntercambio.getLstUCs() != null && !unidadDatosIntercambio.getLstUCs().isEmpty()){
    		insertarListaDatos(unidadDatosIntercambio.getLstUCs());     
    	}
        if(unidadDatosIntercambio.getLstBienesInmuebles() != null && !unidadDatosIntercambio.getLstBienesInmuebles().isEmpty()){
        	insertarListaDatos(unidadDatosIntercambio.getLstBienesInmuebles()); 
        }
        if(unidadDatosIntercambio.getLstCultivos() != null && !unidadDatosIntercambio.getLstCultivos().isEmpty()){
        	insertarListaDatos(unidadDatosIntercambio.getLstCultivos());
        }
        if(unidadDatosIntercambio.getLstConstrucciones() != null && !unidadDatosIntercambio.getLstConstrucciones().isEmpty()){
        	insertarListaDatos(unidadDatosIntercambio.getLstConstrucciones());
        }
        if(unidadDatosIntercambio.getLstRepartos() != null && !unidadDatosIntercambio.getLstRepartos().isEmpty()){
        	insertarListaDatos(unidadDatosIntercambio.getLstRepartos());   
        }
        if (unidadDatosIntercambio.getFincaCatastro()!=null && unidadDatosIntercambio.getFxcc()!=null) {
            insertarDatosGraficos(unidadDatosIntercambio.getFincaCatastro(), unidadDatosIntercambio.getFxcc());
        }
   	 
   	 
    	
    }
    
    /**
     * 
     * @param lstDatos
     * @return
     * @throws DataException 
     */
    private void insertarListaDatos(ArrayList lstDatos) //throws DataException
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
                        insertarDatosSuelo((SueloCatastro)o);
                    }
                    else if (o instanceof UnidadConstructivaCatastro)
                    {
                        insertarDatosUC ((UnidadConstructivaCatastro)o);
                    }
                    else if (o instanceof BienInmuebleJuridico)
                    {
                        insertarDatosBIJ ((BienInmuebleJuridico)o);
                    }
                    else if (o instanceof ConstruccionCatastro)
                    {
                        insertarDatosConstruccion ((ConstruccionCatastro)o);
                    }
                    else if (o instanceof Cultivo)
                    {
                        insertarDatosCultivo ((Cultivo)o);
                    }
                    else if (o instanceof RepartoCatastro)
                    {
                        insertarDatosReparto ((RepartoCatastro)o);
                    }

                    
                } catch (DataException e)
                {    
                    throw new DataException(e);
                   
                }            
            }
        }
        
    }
    
    private void insertarDatosUC(UnidadConstructivaCatastro uc)
    throws Exception
    {        
        try
        {      
         
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
        }
        catch (Exception ex)
        {           
            throw new DataException(I18N.get("Importacion","importar.error.uc"), ex);
        }       
        
    }
    
	private void insertarDatosFinca (FincaCatastro fc)
    //throws DataException
    throws Exception
    {       
        try
        {                             
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
	
	 private void insertarDatosSuelo(SueloCatastro sc) throws Exception {
	    	
        try
        {      

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
            
        }
        catch (Exception ex)
        {           
            throw new DataException(I18N.get("Importacion","importar.error.suelos"), ex);
        }        
    }
	 
	 private void insertarDatosBIJ(BienInmuebleJuridico bij)  throws Exception
    {   
        try
        {    

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
        		lstTipos.add(TYPE_DOUBLE); 

        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getValorCatastralSuelo());            
        		lstTipos.add(TYPE_DOUBLE); 

        		lstValores.add(bij.getBienInmueble().getDatosEconomicosBien().getValorCatastralConstruccion());            
        		lstTipos.add(TYPE_DOUBLE); 

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
	 
	 private void insertarDatosPersona(IdBienInmueble idBi, Persona pers, boolean canUpdate) //throws DataException
	    throws Exception
	    {
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
	        }
	        catch (DataException ex)
	        {           
	            throw new DataException(I18N.get("Importacion","importar.error.personas"), ex);
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
        }
        catch (Exception ex)
        {           
            throw new DataException(I18N.get("Importacion","importar.error.eliminar.titulares"), ex);
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

    	}
    	else{
    		
    	}

    	return idVia;
    }
    
    /**
     * 
     * @param reparto
     * @return
     * @throws DataException 
     */
    private void insertarDatosReparto(RepartoCatastro reparto) 
    throws Exception
    {        

        //Comprueba de qué tipo de reparto se trata
        
        //reparto de cultivo en cultivos
        if (reparto.getCodSubparcelaElementoRepartir()!=null
                && reparto.getCalifCatastralElementoRepartir()!=null
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
        	if (reparto.getLstLocales()!=null && reparto.getLstLocales().size()>0)
            {
        		for(Iterator elemRepartos = reparto.getLstLocales().iterator();elemRepartos.hasNext();){

        			ElementoReparto elemReparto = (ElementoReparto)elemRepartos.next();
        			//reparto de construccion en construcciones
        			insertarDatosRepartoConstrucciones(reparto, elemReparto);
        		}
            }
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
		
		lstValores.add(new Integer(elemreparto.getExpediente().getAnnoExpedienteAdminOrigenAlteracion()));
		lstTipos.add(TYPE_NUMERIC);   
		lstValores.add(elemreparto.getExpediente().getReferenciaExpedienteAdminOrigen());
		lstTipos.add(TYPE_VARCHAR);   
		if (elemreparto.getExpediente().getEntidadGeneradora()!=null)
		    lstValores.add(new Integer(elemreparto.getExpediente().getEntidadGeneradora().getCodigo()));
		else
		    lstValores.add(null);            
		lstTipos.add(TYPE_VARCHAR);
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
    }
    
    
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
		
		lstValores.add(new Integer(elemReparto.getExpediente().getAnnoExpedienteAdminOrigenAlteracion()));
		lstTipos.add(TYPE_NUMERIC);   
		lstValores.add(elemReparto.getExpediente().getReferenciaExpedienteAdminOrigen());
		lstTipos.add(TYPE_VARCHAR);   
		if (elemReparto.getExpediente().getEntidadGeneradora()!=null)
		    lstValores.add(new Integer(elemReparto.getExpediente().getEntidadGeneradora().getCodigo()));
		else
		    lstValores.add(null);            
		lstTipos.add(TYPE_VARCHAR);        
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
		lstValores.add(new Float(elemReparto.getPorcentajeReparto()));
		lstTipos.add(TYPE_FLOAT);  
		
		//datos para hallar el id_bieninmueble
		lstValores.add(reparto.getIdOrigen().getRefCatastral());            
		lstTipos.add(TYPE_VARCHAR);  
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
    }
    
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
		
		lstValores.add(new Integer(elemReparto.getExpediente().getAnnoExpedienteAdminOrigenAlteracion()));
		lstTipos.add(TYPE_NUMERIC); 
		lstValores.add(elemReparto.getExpediente().getReferenciaExpedienteAdminOrigen());
		lstTipos.add(TYPE_VARCHAR);   
		if (elemReparto.getExpediente().getEntidadGeneradora()!=null)
			lstValores.add(new Integer(elemReparto.getExpediente().getEntidadGeneradora().getCodigo()));
		else
		    lstValores.add(null);            
		lstTipos.add(TYPE_VARCHAR); 
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
    }
    
    private void insertarDatosConstruccion(ConstruccionCatastro construc)
    throws Exception
    {
        try
        {      

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
            
            
            
        }
        catch (Exception ex)
        {           
            throw new DataException(I18N.get("Importacion","importar.error.construcciones"), ex);
        }       
        
    }
    
    /**
     * 
     * @param cultivo
     * @return
     * @throws DataException
     */
    private void insertarDatosCultivo(Cultivo cultivo) //throws DataException
    throws Exception
    {       
        try
        {     
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
            
        }
        catch (Exception ex)
        {           
            throw new DataException(I18N.get("Importacion","importar.error.cultivos"), ex);
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
		    
		    lstValores.add(fxcc.getDXF());
		    lstTipos.add(TYPE_CLOB);
		    
		    lstValores.add(fxcc.getASC()); 
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
    
}
