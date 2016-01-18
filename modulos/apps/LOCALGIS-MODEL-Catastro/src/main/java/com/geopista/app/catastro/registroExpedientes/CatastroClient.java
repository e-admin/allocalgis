/**
 * CatastroClient.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.registroExpedientes;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.parsers.SAXParser;
import org.exolab.castor.xml.Marshaller;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.importacion.xml.handlers.ImagenXMLHandler;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.EstadoSiguiente;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FX_CC;
import com.geopista.app.catastro.model.beans.Fichero;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.ImagenCatastro;
import com.geopista.app.catastro.model.beans.Persona;
import com.geopista.app.catastro.model.beans.TipoExpediente;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaPoligono;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaTramos;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaUrbanistica;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaZonaValor;
import com.geopista.app.catastro.model.intercambio.importacion.xml.contents.UnidadDatosIntercambio;
import com.geopista.app.catastro.model.intercambio.importacion.xml.handlers.FxccXMLHandler;
import com.geopista.app.catastro.servicioWebCatastro.beans.IdentificadorDialogo;
import com.geopista.security.SecurityManager;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;
import com.geopista.server.catastro.servicioWebCatastro.FicheroExportacion;
import com.geopista.server.catastro.servicioWebCatastro.FxccExportacion;


/**
 * Clase que implementa en la parte cliente la conexion con la parte servidora. Los diferentes metodos son para las
 * diferentes acciones en base de datos. Se crear un ACQuery para cada caso, se introduce una constante indicando la
 * accion que se quiere realizar y se añaden los paramtros a la hash params que seran recogidos en el servlet segun
 * la accion indicada, y se hara la peticion a base de datos. Los objetos enviados deben ser serializables.
 * */

public class CatastroClient {

    private static final Log logger= LogFactory.getLog(CatastroClient.class);
    private String sUrl=null;
    public static final String	mensajeXML	= "mensajeXML";
    public static final String	idMunicipio	= "idMunicipio";
    public static final String	IdApp		= "IdApp";

    /**
     * Inicializa la conexion al servidor
     * @param sUrl del servidor
     */
    public CatastroClient(String sUrl)
    {
        this.sUrl=sUrl;
    }


    public Object existeBIEnBD(String idBI) throws Exception
    {
        Object parcela = null;
    	ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_EXISTE_BI_BD);
        params.put(ConstantesRegExp.STRING_ID_BI, idBI);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            parcela= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("existeParcelaEnBD(String refCatastral)" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return parcela;
    }

    /**
     * Obtiene las parcelas asociadas al expediente.
     * @param exp Expediente nuevo.
     * @return
     * @throws Exception
     */
    public Object getParcelasExpediente(Object exp, String convenio) throws Exception
    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_PARCELAS_EXPEDIENTE);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);
        params.put(ConstantesRegExp.STRING_CONVENIO, convenio);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            bien= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getParcelasExpediente(Object obj)" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return bien;
    }
    
    
    public Object existeParcelaEnBD(String refCatastral) throws Exception
    {
        Object parcela = null;
    	ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_EXISTE_PARCELA_BD);
        params.put(ConstantesRegExp.STRING_REF_CATASTRAL, refCatastral);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            parcela= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("existeParcelaEnBD(String refCatastral)" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return parcela;
    }

    /**
     * Devuelve un listado con todas los bienes inmuebles cuya referencia catastral rustica coincida con los patrones
     * indicados por parámetro
     * @param patronPoligono Patrón del poligono.
     * @param patronParcela Patron de la parcela.
     * @return Collection con objetos FincaCatastro.
     * @throws Exception
     */
    public Collection getBienesInmueblesCatastroRusticoBuscadasPorPoligono(String patronPoligono, String patronParcela)
            throws Exception
    {
        Collection listaExp= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_BIEN_INMUEBLE_RUSTICO_BUSCADAS_POR_POLIGONO);
        params.put(ConstantesRegExp.STRING_PATRON_POLIGONO, patronPoligono);
        params.put(ConstantesRegExp.STRING_PATRON_PARCELA, patronParcela);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        listaExp= new ArrayList();
        try
        {
            for(;;)
            {
                Object bien= readObject(ois);
                if (bien instanceof BienInmuebleCatastro)
                {
                    listaExp.add((BienInmuebleCatastro)bien);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getBienesInmueblesCatastroRusticoBuscadasPorPoligono()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return listaExp;
    }

    /**
     * Devuelve un listado con todas las fincas cuya referencia catastral rustica coincida con los patrones
     * indicados por parámetro
     * @param patronPoligono Patrón del poligono.
     * @param patronParcela Patron de la parcela.
     * @return Collection con objetos FincaCatastro.
     * @throws Exception
     */
    public Collection getFincaCatastroRusticaBuscadasPorPoligono(String patronPoligono, String patronParcela) throws Exception
    {
        Collection listaExp= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_FINCA_CATASTRO_RUSTICA_BUSCADAS_POR_POLIGONO);
        params.put(ConstantesRegExp.STRING_PATRON_POLIGONO, patronPoligono);
        params.put(ConstantesRegExp.STRING_PATRON_PARCELA, patronParcela);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        listaExp= new ArrayList();
        try
        {
            for(;;)
            {
                Object bien= readObject(ois);
                if (bien instanceof FincaCatastro)
                {
                    listaExp.add((FincaCatastro)bien);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getFincaCatastroRusticaBuscadasPorPoligono()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return listaExp;
    }

    /**
     * Devuelve una hashtable con todos los usuarios de las aplicaciones geopista.
     *
     * @return Hashtable El resultado.
     * @throws Exception
     * */
    public Hashtable getTodosLosUsuarios() throws Exception
    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACCTION_GET_TODOS_LOS_USUARIOS);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            bien= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getUsuarios()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return (Hashtable)bien;
    }

    /**
     * Metodo que devuelve la fecha de la ultima sincronizacion con catastro para mostrar el periodo en la exportacion
     * masiva
     *
     * @return Date La fecha de la ultima sincronizacion.
     * @throws Exception
     * */
    public Date getFechaInicioPeriodoExp() throws Exception
    {
        Date fecha=null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACCTION_FECHA_INICIO_PERIODO_EXP);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            fecha = (Date)readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getFechaInicioPeriodoExp()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return fecha;
    }
    
    public PonenciaZonaValor obtenerPonenciaZonaValor(String codZonaValor) throws Exception
    {
    	PonenciaZonaValor ponenciaZonaValor=null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_PONENCIA_ZONA_VALOR);
        query.setParams(params);
        params.put(ConstantesRegExp.STRING_PONENCIA_ZONA_VALOR, codZonaValor);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
        	ponenciaZonaValor = (PonenciaZonaValor)readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("obtenerPonenciaZonaValor()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return ponenciaZonaValor;
    }
    
    public PonenciaUrbanistica obtenerPonenciaUrbanistica(String codPonenciaUrbanistica) throws Exception
    {
    	PonenciaUrbanistica ponenciaUrbanistica = null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_PONENCIA_URBANISTICA);
        query.setParams(params);
        params.put(ConstantesRegExp.STRING_PONENCIA_URBANISTICA, codPonenciaUrbanistica);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
        	ponenciaUrbanistica = (PonenciaUrbanistica)readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("obtenerPonenciaUrbanistica()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return ponenciaUrbanistica;
    }
    
    public PonenciaPoligono obtenerPonenciaPoligono(String codPonenciaPoligono) throws Exception
    {
    	PonenciaPoligono ponenciaPoligono = null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_PONENCIA_POLIGONO);
        query.setParams(params);
        params.put(ConstantesRegExp.STRING_PONENCIA_POLIGONO, codPonenciaPoligono);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
        	ponenciaPoligono = (PonenciaPoligono)readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("obtenerPonenciaPoligono()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return ponenciaPoligono;
    }
    
    public PonenciaTramos obtenerPonenciaTramos(String codPonenciaTramos) throws Exception
    {
    	PonenciaTramos ponenciaTramos = null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_PONENCIA_TRAMOS);
        query.setParams(params);
        params.put(ConstantesRegExp.STRING_PONENCIA_TRAMOS, codPonenciaTramos);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
        	ponenciaTramos = (PonenciaTramos)readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("obtenerPonenciaTramos()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return ponenciaTramos;
    }

    /**
     * Metodo que elimina de la tabla catastro_temporal las entradas para un expediente pasado por parametro.
     *
     * @param exp El expediente del que se quiere eliminar las entradas.
     * @throws Exception
     * */
    public void elimiarCatastroTemporalExp(Expediente exp) throws Exception
    {
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACCTION_ELIMINAR_CATASTRO_TEMPORAL_EXP);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("elimiarCatastroTemporalExp()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
    }

    /**
     * Accion que genera y escribe en catastro temporal las entradas para un expediente pasado por parametro. Es necesario
     * pasar el convenio en que se esta trabajando.
     *
     * @param exp El Expediente del que se generan las entradas.
     * @param convenio El convenio en el que se esta trabajando.
     * @throws Exception
     * */
    public Expediente actualizaCatastroTemporal(Expediente exp, String convenio) throws Exception
    {
    	 Expediente expe = null;
    	 
    	 ACQuery query= new ACQuery();
         Hashtable params= new Hashtable();
         query.setAction(ConstantesRegExp.ACTION_ACTUALIZA_CATASTRO_TEMPORAL);
         params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);
         params.put(ConstantesRegExp.STRING_CONVENIO, convenio);
         query.setParams(params);
         StringWriter swQuery= new StringWriter();
         ByteArrayOutputStream baos= new ByteArrayOutputStream();
         new ObjectOutputStream(baos).writeObject(query);
         Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
         InputStream in= enviarConsulta(sUrl, swQuery.toString());
         ObjectInputStream ois= new ObjectInputStream(in);
         try
         {
             expe = (Expediente)readObject(ois);
         }
         catch(OptionalDataException ode)
         {
             if (ode.eof!=true)
             {
                 logger.error("actualizaCatastroTemporal()" + ode.getMessage(), ode);
             }
         }
         catch (EOFException ee)
         {
         }
         finally
         {
             try{ois.close();}catch(Exception e){};
         }
         return expe;
    }
    
    /**
     * Accion que genera y escribe en catastro temporal las entradas para un expediente pasado por parametro. Es necesario
     * pasar el convenio en que se esta trabajando.
     *
     * @param exp El Expediente del que se generan las entradas.
     * @param convenio El convenio en el que se esta trabajando.
     * @throws Exception
     * */
    public void escribirEnCatastroTemporal(Expediente exp, String convenio, String modoTrabajo) throws Exception
    {
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_ESCRIBIR_CATASTRO_TEMPORAL);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);
        params.put(ConstantesRegExp.STRING_CONVENIO, convenio);
        params.put(ConstantesRegExp.STRING_MODO_TRABAJO,modoTrabajo);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("escribirEnCatastroTemporal()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
    }
    
    /**
     * Accion que genera y escribe en catastro temporal las entradas para un expediente pasado por parametro. Es necesario
     * pasar el convenio en que se esta trabajando.
     *
     * @param exp El Expediente del que se generan las entradas.
     * @param convenio El convenio en el que se esta trabajando.
     * @throws Exception
     * */
    public void escribirEnCatastroTemporalTramiVariacion(Expediente exp, String convenio, 
    		Boolean isCatastroTemporal, Boolean isExpSitFinales, String modoTrabajo) throws Exception
    {
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_ESCRIBIR_CATASTRO_TEMPORAL_VARIACIONES);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);
        params.put(ConstantesRegExp.STRING_CONVENIO, convenio);
        params.put(ConstantesRegExp.STRING_ESCRIBE_CATASTRO_TEMPORAL, isCatastroTemporal);
        params.put(ConstantesRegExp.STRING_IS_EXP_SITUACION_FINAL, isExpSitFinales);
        params.put(ConstantesRegExp.STRING_MODO_TRABAJO,modoTrabajo);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("escribirEnCatastroTemporal()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
    }

    /**
     * Accion que genera y escribe en catastro temporal las entradas para un expediente pasado por parametro. Es necesario
     * pasar el convenio en que se esta trabajando.
     *
     * @param exp El Expediente del que se generan las entradas.
     * @param convenio El convenio en el que se esta trabajando.
     * @throws Exception
     * */
    public FincaCatastro obtenerInfCastatralFinca(Integer idParcela, String convenio) throws Exception
    {
    	FincaCatastro fincaCatastro = null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_INFO_CATASTRAL);
        params.put(ConstantesRegExp.STRING_IDPARCELA, idParcela);
        params.put(ConstantesRegExp.STRING_CONVENIO, convenio);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
        	fincaCatastro = (FincaCatastro)readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("escribirEnCatastroTemporal()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return fincaCatastro;
    }

    /**
     * Accion que consulta en el servidor si el usuario tiene los permisos necesarios
     * para consultar y ver la informacion catastral de fisico economico
     *
     * @param exp El Expediente del que se generan las entradas.
     * @param convenio El convenio en el que se esta trabajando.
     * @throws Exception
     * */
    public Boolean isPermisosConsultarInformacionCatastralFisicoEconomico() throws Exception
    {
    	Boolean isPermisoConsultarInformacionCatastral = null;
    	
    	ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_IS_PERMISOS_CONSULTA_INFO_CATASTRAL_FISICO_ECONOMICO);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
        	isPermisoConsultarInformacionCatastral = (Boolean)readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("isPermisosConsultarInformacionCatastralFisicoEconomico()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
    	
    	return isPermisoConsultarInformacionCatastral;
    }
    
    /**
     * Accion que consulta en el servidor si el usuario tiene los permisos necesarios
     * para consultar y ver la informacion catastral de titularidad
     *
     * @param exp El Expediente del que se generan las entradas.
     * @param convenio El convenio en el que se esta trabajando.
     * @throws Exception
     * */
    public boolean isPermisosConsultarInformacionCatastralTitularidad() throws Exception
    {
    	boolean isPermisoConsultarInformacionCatastral = false;
    	
    	ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_IS_PERMISOS_CONSULTA_INFO_CATASTRAL_TITULARIDAD);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
        	isPermisoConsultarInformacionCatastral = (Boolean)readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("isPermisosConsultarInformacionCatastralTitularidad()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
    	
    	return isPermisoConsultarInformacionCatastral;
    }
    
    
    
    /**
     * Metodo que sincroniza el expediente pasado por parametro con catastro a traves de los servicios web de catastro.
     *
     * @param exp El expediente que se quiere sincronizar.
     * @throws Exception
     * */
    public void sincronizarExpediente(Expediente exp) throws Exception
    {
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_SINCRONIZA_EXPEDIENTE);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("sincronizarExpediente()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
    }

    /**
     * Metodo que devuelve el id del usuario para cargalo posteriormente en la blackboard y que este accesible en la
     * parte cliente.
     *
     * @return String El id del usuario.
     * @throws Exception
     * */
    public String inicializaBlackBoard() throws Exception
    {
        String idUser=null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_ID_USUARIO);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            idUser = (String)readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("inicializaBlackBoard()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return idUser;
    }

    
    public String obtenerCuerpoFinEntradaOVC(Expediente exp, String modoTrabajo, String convenio,
    		ArrayList listaExpedientes)throws Exception{
    	 String cuerpo="";
    	 Object bien= null;
         ACQuery query= new ACQuery();
         Hashtable params= new Hashtable();

         query.setAction(ConstantesRegExp.ACTION_ACTUALIZAR_OVC_ACOPLADO);

         if(exp!=null)
             params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);

         params.put(ConstantesRegExp.STRING_MODO_TRABAJO, modoTrabajo);
         params.put(ConstantesRegExp.STRING_CONVENIO, convenio);
         
         params.put(ConstantesRegExp.OBJETO_EXPEDIENTE, exp);  
         
         query.setParams(params);

         StringWriter swQuery= new StringWriter();
         ByteArrayOutputStream baos= new ByteArrayOutputStream();

         new ObjectOutputStream(baos).writeObject(query);
         Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
         InputStream in= enviarConsulta(sUrl, swQuery.toString());
         ObjectInputStream ois= new ObjectInputStream(in);

         try{
             //bien= readObject(ois);
             cuerpo = (String)readObject(ois);
             
             
         }
         catch(OptionalDataException ode){
             if (ode.eof!=true)
                 logger.error("exportacionMasiva()" + ode.getMessage(), ode);
         }
         catch (EOFException ee) {}

         finally{
             try{ois.close();}catch(Exception e){};
         }
         
         return cuerpo;
    }
    
    /**
     * Metodo que genera el xml del expediente o expedientes (dependiendo del modo en que se este trabajando) y realiza
     * una peticion a los servicios web de catastro, en el modo de trabajo acoplado. En el desacoplado solo genera el xml
     * y lo almacena en el directorio elegido por el usuario. En modo acoplado se envia el expediente que se esta
     * tratando, el modo de trabajo y el convenio. En modo desacoplado se manda el modo de trabajo, el directorio donde
     * se almacenara el xlm generado y el convenio.
     *
     * @param exp El expediente con el que se trabaja en el modo acoplado.
     * @param modoTrabajo El modo de trabajo, acoplado o desacoplado
     * @param directorio El directorio donde se almacena el xml generado en el modo desacoplado.
     * @param convenio String con el convenio en que se esta trabajando.
     * @throws Exception
     * */
    public void exportacionMasiva(Expediente exp, String modoTrabajo, String directorio, String nombreFinEntrada, String nombreVARPAD,
    		String convenio, ArrayList listaExpedientes, boolean todosExpedientes) throws Exception{

        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();

        query.setAction(ConstantesRegExp.ACTION_EXPORTACION_MASIVA);

        if(exp!=null)
            params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);

        params.put(ConstantesRegExp.STRING_MODO_TRABAJO, modoTrabajo);
        params.put(ConstantesRegExp.STRING_CONVENIO, convenio);
        
        params.put(ConstantesRegExp.OBJETO_LISTA_EXP, listaExpedientes);
        params.put(ConstantesRegExp.BOOLEAN_EXPORTAR_TODOS_EXPEDIENTE, todosExpedientes);

        
        System.out.println("CATASTRO CLIENTE: ");
        System.out.println("directorio¨: "+directorio);
        System.out.println("nombreFinEntrada: "+nombreFinEntrada);
        System.out.println("nombreVARPAD: "+nombreVARPAD);
        
        
        if(directorio!=null)
            params.put(ConstantesRegExp.STRING_DIRECTORIO_EXPORTACION, directorio);
        
        if(nombreFinEntrada!=null)
            params.put(ConstantesRegExp.STRING_NOMBRE_FICHERO_FIN_ENTRADA_EXPORTACION, nombreFinEntrada);
        
        if(nombreVARPAD!=null)
            params.put(ConstantesRegExp.STRING_NOMBRE_FICHERO_VARPAD_EXPORTACION, nombreVARPAD);

        query.setParams(params);

        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();

        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);

        try{
            bien= readObject(ois);
            if (bien instanceof FicheroExportacion){
            	
            	FicheroExportacion fichero = (FicheroExportacion)bien;
            	if (fichero.getFicheroVarpad() != null){
            		
            		String filePathName = directorio + File.separator + nombreVARPAD + (nombreVARPAD.indexOf(".xml")==-1 ? ".xml" : "");
                    Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePathName), "UTF8"));
                    out.write(fichero.getFicheroVarpad());
                    out.close();
            	}
            	if (fichero.getFicheroFinEntrada() != null){
            		
            		String filePathName = directorio + File.separator + nombreFinEntrada + (nombreFinEntrada.indexOf(".xml")==-1 ? ".xml" : "");
                    Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePathName), "UTF8"));
                    out.write(fichero.getFicheroFinEntrada());
                    out.close();
            	}
            	if (fichero.getLstFxcc() != null && fichero.getLstFxcc().size() > 0 ){
            		
            		for (Iterator iterFxcc = fichero.getLstFxcc().iterator(); iterFxcc.hasNext();){
            			
            			Object obj = iterFxcc.next();
            			
            			for (Iterator iterFxccImg = ((ArrayList)obj).iterator();iterFxccImg.hasNext();){
            				
            				Object objeto = iterFxccImg.next();

            				if (objeto instanceof FxccExportacion){
            					FxccExportacion fxccFile = (FxccExportacion) objeto;

            					XMLReader parser = new SAXParser();
            					parser.setFeature("http://apache.org/xml/features/validation/schema", true);
            					parser.setFeature("http://xml.org/sax/features/validation", true);

            					if(fxccFile.getFxcc()!=null){

            						FX_CC fxcc = new FX_CC();
            						parser.setContentHandler(new FxccXMLHandler(parser, fxcc, "elemf"));
            						parser.parse(new InputSource(new ByteArrayInputStream(fxccFile.getFxcc().getBytes("UTF-8"))));

            						File dir = new File(directorio + File.separator + fxccFile.getRefCatastral());
            						dir.mkdirs();
            						if (fxcc.getDXF() != null ){

            							String filePathName = directorio + File.separator + fxccFile.getRefCatastral() + File.separator + fxccFile.getRefCatastral() + ".dxf";
            							Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePathName), "UTF8"));
            							out.write(fxcc.getDXF());
            							out.close();
            						}
            						if (fxcc.getASC() != null){

            							String filePathName = directorio + File.separator + fxccFile.getRefCatastral() + File.separator + fxccFile.getRefCatastral() + ".asc";
            							Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePathName), "UTF8"));
            							out.write(fxcc.getASC());
            							out.close();
            						}
            					}

            					if (fxccFile.getLstImg() != null && fxccFile.getLstImg().length() > 0){

            						ArrayList lstImagenes = new ArrayList();		            		

            						parser.setContentHandler(new ImagenXMLHandler(parser, lstImagenes, "elemf"));
            						parser.parse(new InputSource(new ByteArrayInputStream(fxccFile.getLstImg().getBytes())));

            						for (Iterator iterImg = lstImagenes.iterator(); iterImg.hasNext();){
            							ImagenCatastro img = (ImagenCatastro) iterImg.next();

            							File ficheroImg  = new File(directorio + File.separator + fxccFile.getRefCatastral() + File.separator + img.getNombre() + "." + img.getExtension());
            				            ImageIO.write( (BufferedImage) img.getImagen(), img.getExtension() , ficheroImg);
            							            							
            						}
            					}
            				}
            			}

            		}
            	}
            }
        }
        catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("exportacionMasiva()" + ode.getMessage(), ode);
        }
        catch (EOFException ee) {}

        finally{
            try{ois.close();}catch(Exception e){};
        }
    }
    

    public void exportacionMasivaVarpad(Expediente exp, String modoTrabajo, String directorio, String nombreVARPAD,
    		String convenio) throws Exception{

        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();

        query.setAction(ConstantesRegExp.ACTION_EXPORTACION_MASIVA_VARPAD);

        if(exp!=null)
            params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);

        params.put(ConstantesRegExp.STRING_MODO_TRABAJO, modoTrabajo);
        params.put(ConstantesRegExp.STRING_CONVENIO, convenio);
        
        query.setParams(params);

        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();

        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);

        try{
            bien= readObject(ois);
            String filePathName = directorio + File.separator+nombreVARPAD + (nombreVARPAD.indexOf(".xml")==-1 ? ".xml" : "");

            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePathName, true), "UTF8"));
            out.write((String)bien);
            out.close();
        }
        catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("exportacionMasiva()" + ode.getMessage(), ode);
        }
        catch (EOFException ee) {}

        finally{
            try{ois.close();}catch(Exception e){};
        }
    }

    
    public void exportacionMasivaFinEntrada(Expediente exp, String modoTrabajo, String directorio, String nombreFinEntrada,
    		String convenio) throws Exception{

        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();

        query.setAction(ConstantesRegExp.ACTION_EXPORTACION_MASIVA_FINENTRADA);

        if(exp!=null)
            params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);

        params.put(ConstantesRegExp.STRING_MODO_TRABAJO, modoTrabajo);
        params.put(ConstantesRegExp.STRING_CONVENIO, convenio);
        
        query.setParams(params);

        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();

        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);

        try{
            bien= readObject(ois);
            String filePathName = directorio + File.separator+nombreFinEntrada + (nombreFinEntrada.indexOf(".xml")==-1 ? ".xml" : "");

            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePathName, true), "UTF8"));
            out.write((String)bien);
            out.close();
        }
        catch(OptionalDataException ode){
            if (ode.eof!=true)
                logger.error("exportacionMasiva()" + ode.getMessage(), ode);
        }
        catch (EOFException ee) {}

        finally{
            try{ois.close();}catch(Exception e){};
        }
    }

    /**
     * Metodo que almacena un fichero en base de datos asociandolo a los expedientes pasados por parametro
     * en un arrayList.
     *
     * @param listaNumExp La lista de los numeros de expediente a asociar.
     * @param fich Fichero a almacenar.
     * @throws Exception
     * */
    public void crearFichero(Object listaNumExp, Object fich) throws Exception
    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_CREAR_FICHERO);
        params.put(ConstantesRegExp.ARRAY_LISTA_NUM_EXP, listaNumExp);
        params.put(ConstantesRegExp.OBJETO_FICHERO, fich);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            bien= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("crearFichero()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
    }

    /**
     * Crea un expediente que llega por parámetro. Metodo utilizado para crear expedientes que se reciben de catastro.
     * @param exp Expediente que se quiere crear.
     * @throws Exception
     */
    public void crearExpedienteCatastro(Object exp) throws Exception
    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_CREAR_EXPEDIENTE_CATASTRO);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            bien= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("crearExpedienteCatastro(Object obj)" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
    }

    /**
     * Devuelve un ArrayList con un objeto Persona que contiene un bien Inmueble.
     * @param patronNif Nif de la persona por la que se buscará.
     * @return Array List con un objeto Persona.
     * @throws Exception
     */
    public ArrayList getBienInmuebleBuscadosPorTitular(String patronNif) throws Exception
    {
        ArrayList listaExp= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_BIEN_INMUEBLE_BUSCADOS_POR_TITULAR);
        params.put(ConstantesRegExp.STRING_PATRON_NIF, patronNif);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        listaExp= new ArrayList();
        try
        {
            for(;;)
            {
                Object bien= readObject(ois);
                if (bien instanceof Persona)
                {
                    listaExp.add((Persona)bien);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getBienInmuebleBuscadosPorTitular()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return listaExp;
    }

    /**
     * Devuelve un listado con todos los tipos de expedientes que siguen el convenio
     * que se especifica por parámetro.
     * @param convenio Convenio asociado a los expedientes a buscar.
     * @return ArrayList con objetos TipoExpediente
     * @throws Exception
     */
    public ArrayList getTiposExpedientes(String convenio) throws Exception
    {
        ArrayList listaExp= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_TIPOS_EXPEDIENTES);
        params.put(ConstantesRegExp.STRING_CONVENIO, convenio);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        listaExp= new ArrayList();
        try
        {
            for(;;)
            {
                Object bien= readObject(ois);
                if (bien instanceof TipoExpediente)
                {
                    listaExp.add((TipoExpediente)bien);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getTiposExpedientes()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return listaExp;
    }

    /**
     * Devuelve los parámetros de configuración de la aplicación.
     * @return Objeto DatosConfiguracion con la configuración de la aplicación.
     * @throws Exception
     */
    public DatosConfiguracion getParametrosConfiguracion() throws Exception
    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_CONFIGURACION);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            bien= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getParametrosConfiguracion()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return (DatosConfiguracion)bien;
    }

    /**
     * Guarda los nuevos parámetros de configuración que le llegan por parámetro.
     * @param param Nuevos parámetros de configuración.
     * @throws Exception
     */
    public void guardarParametroConfiguracion(DatosConfiguracion param) throws Exception
    {
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GUARDAR_CONFIGURACION);
        params.put(ConstantesRegExp.PARAM_CONFIGURACION,param);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("guardarParametroConfiguracion()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
    }

    /**
     * Devuelve una Hashtable con dos arrays, uno con los codigos de municipios y otro con los nombres de los municipios.
     * Las claves son "codigos" y "nombres", y el codigo y el nombre se corresponden en posiciones.
     * @param codigoProvincia Provincia por la que buscar el municipio.
     * @return Hashtable 2 arrayList, un con los codigo y otro con los nombres.
     * @throws Exception
     */
    public Hashtable getCodigoNombreMunicipio(String codigoProvincia) throws Exception
    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_CODIGO_NOMBRE_MUNICIPIO);
        params.put(ConstantesRegExp.CODIGO_PROVINCIA,codigoProvincia);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            bien= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getCodigoNombreMunicipio()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return (Hashtable)bien;
    }
    
    
    /**
     * Devuelve una Hashtable con dos parametros Integer, uno el nombre de la via y el otro el tipo.
     * Las claves son "nombreVia" y "tipoVia", y el codigo y el nombre se corresponden en posiciones.
     * @param codigoProvincia Provincia por la que buscar el municipio.
     * @return Hashtable 2 arrayList, un con los codigo y otro con los nombres.
     * @throws Exception
     */
    public DireccionLocalizacion getVia(Integer idMunicipio, Integer codigoVia) throws Exception
    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_VIA_POR_CODIGO_VIA);
        params.put(ConstantesRegExp.CODIGO_MUNICIPIO_INE,idMunicipio);
        params.put(ConstantesRegExp.STRING_CODIGO_VIA,codigoVia);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            bien= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getCodigoNombreMunicipio()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return (DireccionLocalizacion)bien;
    }
    
    
    
    public String getCodigoDGCMunicipio(String codigoINE, String codigoProvincia) throws Exception
    {
        Object codigoDGC= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_CODIGO_DGC_MUNICIPIO);
        params.put(ConstantesRegExp.CODIGO_MUNICIPIO_INE, codigoINE);
        params.put(ConstantesRegExp.CODIGO_PROVINCIA, codigoProvincia);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
        	codigoDGC= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getCodigoDGCMunicipio()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return (String)codigoDGC;
    }

    /**
     * Devuelve una ArrayList de objetos EntidadGeneradora.
     * @return Arraylist, objetos EntidadGeneradora.
     * @throws Exception
     */
    public ArrayList getEntidadGeneradora() throws Exception
    {
    	
    	Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_LIST_ENTIDADES_GENERADORAS);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            bien= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getCodigoDigitoControlDni()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return (ArrayList)bien;
    	
    }
    /**
     * Devuelve una Hashtable en la que la key es el resto y el valor es la letra asociada.
     * @return Hashtable, la key es el resto y el valor es la letra asociada.
     * @throws Exception
     */
    public Hashtable getCodigoDigitoControlDni() throws Exception
    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_CODIGO_DIGITO_CONTROL_DNI);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            bien= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getCodigoDigitoControlDni()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return (Hashtable)bien;
    }
    /**
     * Devuelve una Hashtable con con dos arrays, uno con los codigos de provincia y otro con los nombres de las provincias.
     * Las claves son "codigos" y "nombres", y el codigo y el nombre se corresponden en posiciones.
     * @return Hashtable 2 arrayList, un con los codigo y otro con los nombres.
     * @throws Exception
     */
    public Hashtable getCodigoNombreProvincia() throws Exception
    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_CODIGO_NOMBRE_PROVINCIA);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            bien= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getCodigoNombreProvincia()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return (Hashtable)bien;
    }

    /**
     * Comprueba si hay actualizaciones o envíos pendientes. En caso de que haya, se lanza una excepcion que tienes que
     * ser capturada y mostrar en mensaje de la acException.
     * @throws Exception
     */
    public void comprobarActualizacionYEnvios() throws Exception
    {
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_COMPROBAR_ACTUALIZACIONES_ENVIOS);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("comprobarActualizacionYEnvios()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
    }

    /**
     * Devuelve una lista con todos los tipos de vias.
     *
     * @return DireccionLocalizacion con los tipos de vias.
     * @param nombre Nombre de la calle
     * @throws Exception
     */
    public DireccionLocalizacion getViaPorNombre(String nombre) throws Exception
    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_VIA_POR_NOMBRE);
        params.put(ConstantesRegExp.STRING_NOMBRE_VIA, nombre);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            for(;;)
            {
                bien= readObject(ois);

            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getViaPorNombre()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return (DireccionLocalizacion)bien;
    }
    
    public DireccionLocalizacion getViaPorNombreYCodigo(String nombre, int codigovia) throws Exception
    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_VIA_POR_NOMBRE_Y_CODIGO);
        params.put(ConstantesRegExp.STRING_NOMBRE_VIA, nombre);
        params.put(ConstantesRegExp.STRING_CODIGO_VIA, new Integer(codigovia));
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            for(;;)
            {
                bien= readObject(ois);

            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getViaPorNombre()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return (DireccionLocalizacion)bien;
    }

    /**
     * Devuelve un listado con todos los ficheros que se han generado en la entidad.
     * @return ArrayList con objetos Fichero.
     * @throws Exception
     */
    public ArrayList consultaHistoricoFicheros() throws Exception
    {
        ArrayList listaExp= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_CONSULTA_HISTORICO_FICHEROS);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        listaExp= new ArrayList();
        try
        {
            for(;;)
            {
                Object bien= readObject(ois);
                if (bien instanceof Fichero)
                {
                    listaExp.add((Fichero)bien);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("consultaHistoricoFIcheros()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return listaExp;
    }

    /**
     * Modifica el expediente que le llega por parámetro en la BBDD.
     * @param exp expediente que se quiere modificar.
     * @throws Exception
     */
    public void modificarExpediente(Object exp) throws Exception
    {
    	//MENU --> MODIFICAR EXPEDIENTE
        //TODO mirar porque no devuelve nada, seguro que se puede limpiar.
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_MODIFICAR_EXPEDIENTE);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("crearExpediente(Object obj)" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        	System.out.println("Se ha producido un error al enviar el objeto "+ee);
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
    }
    
    /**
     * Modifica el expediente que le llega por parámetro en la BBDD.
     * @param exp expediente que se quiere modificar.
     * @throws Exception
     */
    public void modificarExpedienteVariaciones(Object exp, Boolean isCatastroTemporal, 
    		Boolean isExpSitFinales) throws Exception
    {
    	//MENU --> MODIFICAR EXPEDIENTE
        //TODO mirar porque no devuelve nada, seguro que se puede limpiar.
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_MODIFICAR_EXPEDIENTE);
        params.put(ConstantesRegExp.STRING_ESCRIBE_CATASTRO_TEMPORAL, isCatastroTemporal);
        params.put(ConstantesRegExp.STRING_IS_EXP_SITUACION_FINAL, isExpSitFinales);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("crearExpediente(Object obj)" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        	System.out.println("Se ha producido un error al enviar el objeto "+ee);
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
    }

    /**
     * Modifica el expediente que le llega por parámetro en la BBDD.
     * @param exp expediente que se quiere modificar.
     * @throws Exception
     */
    public void eliminarReferenciaCatastroTemoral(Object exp, ArrayList listaRefElimCatasTemporal) throws Exception
    {
    	//MENU --> MODIFICAR EXPEDIENTE
        //TODO mirar porque no devuelve nada, seguro que se puede limpiar.
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_ELIMINAR_FINCA_CATASTRO_TEMPORAL);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);
        params.put(ConstantesRegExp.OBJETO_LISTA_REFERENCIAS_CATASTRALES,listaRefElimCatasTemporal);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("crearExpediente(Object obj)" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        	System.out.println("Se ha producido un error al enviar el objeto "+ee);
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
    }

    
    /**
     * Actualiza el expediente que llega por parámetro.
     * @param exp Expediente que se va a actualizar.
     * @throws Exception
     */
    public Expediente updateExpediente(Object exp) throws Exception

    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_UPDATE_EXPEDIENTE);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            bien= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("updateExpediente(Object obj)" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return (Expediente)bien;
    }

    /**
     * Devuelve un listado con todos los posibles estados siguientes.
     *
     * @return ArrayList con objetos EstadoSiguiente.
     * @throws Exception
     */
    public ArrayList getEstadoSiguiente() throws Exception
    {
        ArrayList listaExp= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_ESTADO_SIGUIENTE);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        listaExp= new ArrayList();
        try
        {
            for(;;)
            {
                Object bien= readObject(ois);
                if (bien instanceof EstadoSiguiente)
                {
                    listaExp.add((EstadoSiguiente)bien);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getEstadoSiguiente()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return listaExp;
    }

    /**
     * Devuelve un listado con todas las fincas que se encuentren en la direccion que se indica por
     * parámetros.
     * @param patronNombreVia Nombre de la vía.
     * @param patronTipoVia Tipo de la vía.
     * @return Collection con objetos FincaCatastro.
     * @throws Exception
     */
    public Collection getFincasCatastroBuscadasPorDir(String patronNombreVia, String patronTipoVia) throws Exception
    {
        Collection listaExp= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_FINCA_CATASTRO_BUSCADAS_POR_DIR);
        params.put(ConstantesRegExp.STRING_PATRON_NOMBRE_VIA, patronNombreVia);
        params.put(ConstantesRegExp.STRING_PATRON_TIPO_VIA, patronTipoVia);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        listaExp= new ArrayList();
        try
        {
            for(;;)
            {
                Object bien= readObject(ois);
                if (bien instanceof FincaCatastro)
                {
                    listaExp.add((FincaCatastro)bien);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getDireccionReferenciaCatastralBuscadasPorDir()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return listaExp;
    }

    /**
     * Devuelve un listado con todos los bienes Inmuebles localizados en la dirección que se indica por
     * parámetro.
     * @param patronNombreVia Nombre de la vía.
     * @param patronTipoVia Tipo de la vía.
     * @return Collection con objetos BienInmueble.
     * @throws Exception
     */
    public Collection getBienInmuebleCatastroBuscadasPorDir(String patronNombreVia, String patronTipoVia) throws Exception
    {
        Collection listaExp= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_BIEN_INMUEBLE_BUSCADAS_POR_DIR);
        params.put(ConstantesRegExp.STRING_PATRON_NOMBRE_VIA, patronNombreVia);
        params.put(ConstantesRegExp.STRING_PATRON_TIPO_VIA, patronTipoVia);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        listaExp= new ArrayList();
        try
        {
            for(;;)
            {
                Object bien= readObject(ois);
                if (bien instanceof BienInmuebleCatastro)
                {
                    listaExp.add((BienInmuebleCatastro)bien);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getBienInmuebleCatastroBuscadasPorDir()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return listaExp;
    }

    /**
     * Devuelve un listado con todas las fincas cuya referencia catastral coincida con el patron
     * indicada por parámetro
     * @param patron Patrón con la referencia catastral
     * @return Collection con objetos FincaCatastro.
     * @throws Exception
     */
    public Collection getFincaCatastroBuscadas(String patron) throws Exception
    {
        Collection listaExp= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_FINCA_CATASTRO_BUSCADAS);
        params.put(ConstantesRegExp.STRING_PATRON_REF_CATASTRAL,patron);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        listaExp= new ArrayList();
        try
        {
            for(;;)
            {
                Object bien= readObject(ois);
                if (bien instanceof FincaCatastro)
                {
                    listaExp.add((FincaCatastro)bien);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getDireccionReferenciaCatastralBuscadas()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return listaExp;
    }

    /**
     * Devuelve un listado con los bienes inmuebles cuya referencia catrastral coincida con el patrón
     * indicado por parámetro.
     * @param patron Patrón de la referencia catastral.
     * @return Collection con objetos BienInmuebleCatastro
     * @throws Exception
     */
    public Collection getBienesInmueblesCatastroBuscadas(String patron) throws Exception
    {
        Collection listaExp= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_BIEN_INMUEBLE_BUSCADAS);
        params.put(ConstantesRegExp.STRING_PATRON_REF_CATASTRAL,patron);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        listaExp= new ArrayList();
        try
        {
            for(;;)
            {
                Object bien= readObject(ois);
                if (bien instanceof BienInmuebleCatastro)
                {
                    listaExp.add((BienInmuebleCatastro)bien);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getBienesInmueblesCatastroBuscadas()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return listaExp;
    }

    /**
     * Devuelve un listado con todas las fincas cuya referencia catastral encajen con alguna de
     * las que le llegan por parámetro.
     * @param refCatas ArrayList con objetos ReferenciaCatastral
     * @return Collection con objetos FincaCatastro.
     * @throws Exception
     */
    public Collection getFincaCatastroPorReferenciaCatastral(ArrayList refCatas) throws Exception
    {
        Collection listaExp= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_FINCA_CATASTRO_POR_REFERENCIA_CATASTRAL);
        params.put(ConstantesRegExp.OBJETO_LISTA_REFERENCIAS_CATASTRALES,refCatas);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        listaExp= new ArrayList();
        try
        {
            for(;;)
            {
                Object bien= readObject(ois);
                if (bien instanceof FincaCatastro)
                {
                    listaExp.add((FincaCatastro)bien);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getDireccionReferenciaCatastralExpediente()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return listaExp;
    }
    
    
    
    
    /**
     * Devuelve el identificador de la finca
     * @param refCatas
     * @return
     * @throws Exception
     */
    public int getIdFincaCatastro(String refCatas) throws Exception
    {
        Collection listaExp= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_ID_FINCA_CATASTRO);
        params.put(ConstantesRegExp.STRING_REF_CATASTRAL,refCatas);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        int idFinca = -1;
        try
        {
            for(;;)
            {
                Object idFincaObject= readObject(ois);
                if (idFincaObject != null)
                {
                   idFinca = (Integer)idFincaObject;
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getIdFincaCatastro()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return idFinca;
    }

    /**
     * Devuelve un listado con los códigos de estados.
     * @return ArrayList con los códigos de estados.
     * @throws Exception
     */
    public ArrayList getCodigoEstados() throws Exception
    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_CODIGOS_ESTADOS);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            bien= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getCodigoEstados()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return (ArrayList)bien;
    }


    /**
     * Devuelve el listado con todos los códigos de las entidades generadoras.
     * @return ArrayList con los códigos de las entidades generadoras.
     * @throws Exception
     */
    public ArrayList getCodigoEntidadGeneradora() throws Exception
    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_ENTIDADES_GENERADORAS);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            bien= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getCodigoEntidadGeneradora()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return (ArrayList)bien;
    }

    /**
     * Devuelve un listado con los usuarios, para cada uno de ellos se mostrará el identificador y el nombre
     * correspondiente.
     * @return Hashtable (idUser, nameUser)
     * @throws Exception
     */
    public Hashtable getUsuariosConExpediente() throws Exception
    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_USUARIOS_CON_EXPEDIENTE);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            bien= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getUsuarios()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return (Hashtable)bien;
    }
    
    public Collection getExpedientesUsuario(String expCerrado, String convenio) throws Exception{
    	return getExpedientesUsuario(expCerrado, convenio, DatosConfiguracion.MODO_TRABAJO_DESACOPLADO);
	}
    
    /**
     * Devuelve el listado con todos los expedientes cuyo estado y convenio sean los que se le indican
     * por parámetros.
     * @param expCerrado Si el contenido es true, se devuelven solo los expedientes cerrados, si es false los no cerrados
     * @param convenio Convenio.
     * @return Collection con objetos Expediente.
     * @throws Exception
     */
    public Collection getExpedientesUsuarioFinEntradaMasivo(ArrayList estadosArray, String convenio, String modoTrabajo) throws Exception
    {
        Collection listaExp= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_EXPEDIENTES_USUARIO_FIN_ENTADA_MASIVO);
        params.put(ConstantesRegExp.OBJETO_LISTA_ESTADOS_EXP, estadosArray);
        params.put(ConstantesRegExp.STRING_CONVENIO, convenio);
        params.put(ConstantesRegExp.STRING_MODO_TRABAJO, modoTrabajo);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        listaExp= new ArrayList();
        try
        {
            for(;;)
            {
                Object bien= readObject(ois);
                if (bien instanceof Expediente)
                {
                    listaExp.add((Expediente)bien);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getExpedientesUsuario()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return listaExp;
    }
    
    
    /**
     * Devuelve el listado con todos los expedientes cuyo estado y convenio sean los que se le indican
     * por parámetros.
     * @param expCerrado Si el contenido es true, se devuelven solo los expedientes cerrados, si es false los no cerrados
     * @param convenio Convenio.
     * @return Collection con objetos Expediente.
     * @throws Exception
     */
    public Collection getExpedientesUsuario(String expCerrado, String convenio,  String modoTrabajo) throws Exception
    {
        Collection listaExp= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_EXPEDIENTES_USUARIO);
        params.put(ConstantesRegExp.BOOLEAN_EXP_CERRADO, expCerrado);
        params.put(ConstantesRegExp.STRING_CONVENIO, convenio);
        params.put(ConstantesRegExp.STRING_MODO_TRABAJO, modoTrabajo);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        listaExp= new ArrayList();
        try
        {
            for(;;)
            {
                Object bien= readObject(ois);
                if (bien instanceof Expediente)
                {
                    listaExp.add((Expediente)bien);
                }
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getExpedientesUsuario()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return listaExp;
    }

    
    public Expediente getIdExpediente(Expediente exp) throws Exception
    {
        Long idExpediente= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_ID_EXPEDIENTE);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE, exp);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        
        try
        {
            for(;;)
            {
                Object id= readObject(ois);
                if (id instanceof Long)
                {
                    idExpediente = (Long) id;
                    if (idExpediente != null){
                    	exp.setIdExpediente(idExpediente.longValue());
                    }
                }
            }
            
        }        
        
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getExpedientesUsuario()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return exp;
    }

    /**
     * Crea un nuevo expediente.
     * @param exp Expediente nuevo.
     * @return
     * @throws Exception
     */
    public Object crearExpediente(Object exp) throws Exception
    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_CREAR_EXPEDIENTE);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            bien= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("crearExpediente(Object obj)" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return bien;
    }
    
    public Object borrarExpediente(Object exp) throws Exception
    {
        Object borrado= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_BORRAR_EXPEDIENTE);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            borrado= readObject(ois);
        }
        
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("crearExpediente(Object obj)" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return borrado;
    }
    
    /**
     * 
     */
    public void insertarDatosSalida(UnidadDatosIntercambio udsa, boolean insertarExpediente, 
            Expediente exp)
            throws Exception
    {        
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_INSERTAR_DATOS_SALIDA);
        params.put(ConstantesRegExp.OBJETO_UNIDAD_DATOS_INTERCAMBIO, udsa);
        params.put(ConstantesRegExp.BOOLEAN_INSERTAR_EXPEDIENTE, new Boolean(insertarExpediente));
        if (exp != null){
        	params.put(ConstantesRegExp.OBJETO_EXPEDIENTE, exp);
        }
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        
        try
        {
            for(;;)
            {
                Object insertado= readObject(ois);
                
            }
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("insertarDatossalida()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
       
    }

    /**
     * Envía la consulta con el mensaje sMensaje, a la url indicada también por parámetro sUrl.
     * @param sUrl url a la que enviar la consulta.
     * @param sMensaje mensaje a enviar.
     * @return InputStream.
     * @throws Exception
     */
    public static InputStream enviarConsulta(String sUrl, String sMensaje) throws Exception
    {
          return enviar(sUrl, sMensaje, null, null, null);
    }

    /**
     * Envía la consulta al servidor.
     * @param sUrl url a la que enviar la consulta.
     * @param sMensaje mensaje a enviar.
     * @param sUserName nombre del usuario.
     * @param sPassword password del usuario.
     * @param files
     * @return InputStream
     * @throws Exception
     */
    private static InputStream enviar(String sUrl, String sMensaje, String sUserName, String sPassword, Vector files) throws Exception
    {
		Credentials creds = null;
		if (sUserName != null && sPassword != null)
			creds = new UsernamePasswordCredentials(sUserName, sPassword);
        else
        {
			if (com.geopista.security.SecurityManager.getIdSesion() == null){
				if (logger.isDebugEnabled()){
					logger.debug("enviar(String, String, String, String) - Usuario no autienticado");
				}
                creds = new UsernamePasswordCredentials("GUEST", "");
            }else
               creds = new UsernamePasswordCredentials(com.geopista.security.SecurityManager.getIdSesion(), com.geopista.security.SecurityManager.getIdSesion());
		}
		//create a singular HttpClient object		
		org.apache.commons.httpclient.HttpClient client= AppContext.getHttpClient();

		//establish a connection within 5 seconds
		client.setConnectionTimeout(5000);

		//set the default credentials
		if (creds != null)
        {
			client.getState().setCredentials(null, null, creds);
		}
       
        /* -- MultipartPostMethod -- */
        org.apache.commons.httpclient.methods.MultipartPostMethod method= new org.apache.commons.httpclient.methods.MultipartPostMethod(sUrl);

        if (com.geopista.security.SecurityManager.getIdMunicipio()!=null)
        {
             method.addParameter(idMunicipio, SecurityManager.getIdMunicipio());
        }
        if (SecurityManager.getIdApp()!=null)
        {
			 method.addParameter(IdApp, SecurityManager.getIdApp());
        }
        if (sMensaje!=null)
        {
            method.addPart(new org.apache.commons.httpclient.methods.multipart.StringPart(mensajeXML, sMensaje, "ISO-8859-1"));
        }

        /** Necesario para la aplicacion de Inventario. En la aplicacion se pasa un vector files a insertar o modificar */
        //TODO esto mirarlo y quitarlo si no es necesario.
       

        method.setFollowRedirects(false);

		//execute the method
		byte[] responseBody = null;
		try {
			client.executeMethod(method);
			responseBody = method.getResponseBody();
		} catch (HttpException he) {
			throw new Exception("Http error connecting to '" + sUrl + "'" + he.getMessage());
		} catch (IOException ioe) {
			logger.error(
					"enviar(String, String, String, String) - Unable to connect to '"
							+ sUrl + "'", ioe);
			throw new Exception("Unable to connect to '" + sUrl + "'" + ioe.getMessage());
		}
		if (logger.isDebugEnabled()){
			logger
					.debug("enviar(String, String, String, String) - Request Path: "
							+ method.getPath());
		}
		if (logger.isDebugEnabled()){
			logger
					.debug("enviar(String, String, String, String) - Request Query: "
							+ method.getQueryString());
		}
		Header[] requestHeaders = method.getRequestHeaders();
		for (int i = 0; i < requestHeaders.length; i++) {
				if (logger.isDebugEnabled()){
					logger.debug("enviar(String, String, String, String)"
							+ requestHeaders[i]);
				}
		}

		//write out the response headers
		if (logger.isDebugEnabled())
        {
			logger.debug("enviar(String, String, String, String) - *** Response ***");
		}
		if (logger.isDebugEnabled())
        {
			logger.debug("enviar(String, String, String, String) - Status Line: "
							+ method.getStatusLine());
		}
        int iStatusCode = method.getStatusCode();
        String sStatusLine=method.getStatusLine().toString();
		Header[] responseHeaders = method.getResponseHeaders();
		for (int i = 0; i < responseHeaders.length; i++)
        {
			if (logger.isDebugEnabled())
            {
				logger.debug("enviar(String, String, String, String)"
						+ responseHeaders[i]);
			}
		}
		//clean up the connection resources
		method.releaseConnection();
		method.recycle();
        if (iStatusCode==200)
        {
	        return new GZIPInputStream(new ByteArrayInputStream(responseBody));
        }
        else
            throw new Exception(sStatusLine);
	}


    /**
     * Lee la respuesta del servidor.
     * @param ois ObjetInputStream
     * @return Object devuelto por el servidor.
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws ACException
     */
    private Object readObject(ObjectInputStream ois) throws IOException,ClassNotFoundException, ACException
    {
       Object oRet=null;
       oRet=ois.readObject();
       if (oRet instanceof ACException)
       {
           throw (ACException)oRet;
       }
       return oRet;
   }
    

    /**
     * Actualiza el expediente que llega por parámetro.
     * @param exp Expediente que se va a actualizar.
     * @throws Exception
     */
    public void updateIdentificadoresDialogoExpediente(ArrayList lstIdentificadorDialogo, Expediente exp) throws Exception

    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_UPDATE_IDENTIFICADORES_DIALOGO);
        params.put(ConstantesRegExp.OBJETO_LST_IDENTIFICADORES_DIALOGO,lstIdentificadorDialogo);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE, exp );
        
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
           readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("updateIdentificadoresDialogoExpediente(Object obj)" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }

    }
    
    /**
     * Obtiene los identificadores de bienes asociados a un expediente.
     * @param exp Expediente nuevo.
     * @return
     * @throws Exception
     */
    public Object getIdentificadorExpedienteBienes(Object exp, IdentificadorDialogo identificadoresDialogo) throws Exception
    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_IDENTIFICADOR_DIALOGO_BIEN);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);
        params.put(ConstantesRegExp.OBJETO_LST_IDENTIFICADORES_DIALOGO, identificadoresDialogo);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            bien= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getIdentificadoresExpedienteBienes(Object obj,  IdentificadorDialogo identificadoresDialogo)" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return bien;
    }
    /**
     * Obtiene los identificadores de bienes asociados a un expediente.
     * @param exp Expediente nuevo.
     * @return
     * @throws Exception
     */
    public Object getIdentificadorExpedienteFinca(Object exp,  IdentificadorDialogo identificadoresDialogo) throws Exception
    {
        Object bien= null;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_GET_IDENTIFICADOR_DIALOGO_FINCA);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE,exp);
        params.put(ConstantesRegExp.OBJETO_LST_IDENTIFICADORES_DIALOGO, identificadoresDialogo);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            bien= readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("getIdentificadorExpedienteFinca(Object obj,  IdentificadorDialogo identificadoresDialogo)" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return bien;
    }
    
   
    
    public boolean consultaEstadoFincaOVC(Expediente exp, String referencia) throws Exception
    {
    	boolean  actualizado = false;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_CONSULTA_ESTADO_FINCA_OVC);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE, exp);
        params.put(ConstantesRegExp.STRING_REF_CATASTRAL, referencia);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
        	actualizado = (Boolean)readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("consultaEstadoFincaOVC()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return actualizado;
    }
    
    public boolean consultaEstadoBienOVC(Expediente exp, String idBien) throws Exception
    {
    	boolean  actualizado = false;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_CONSULTA_ESTADO_BIEN_OVC);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE, exp);
        params.put(ConstantesRegExp.STRING_ID_BI, idBien);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
        	actualizado = (Boolean)readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("consultaEstadoBienOVC()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return actualizado;
    }
    
    public boolean existeReferenciaCatastroTemporal(Expediente exp) throws Exception
    {
    	boolean  exiteRefCatastroTemporal = false;
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(ConstantesRegExp.ACTION_EXISTE_REFERENCIA_CATASTRO_TEMPORAL);
        params.put(ConstantesRegExp.OBJETO_EXPEDIENTE, exp);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviarConsulta(sUrl, swQuery.toString());
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
        	exiteRefCatastroTemporal = (Boolean)readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
                logger.error("existeReferenciaCatastroTemporal()" + ode.getMessage(), ode);
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
        return exiteRefCatastroTemporal;
    }
}
