package com.geopista.server.database;


import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.exception.DataException;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils_LCGIII;
import com.geopista.app.catastro.intercambio.importacion.xml.contents.BienInmuebleJuridico;
import com.geopista.app.catastro.intercambio.importacion.xml.contents.UnidadDatosIntercambio;
import com.geopista.app.catastro.intercambio.importacion.xml.contents.UnidadDatosRetorno;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.ComunidadBienes;
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.model.beans.Derecho;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.ElementoReparto;
import com.geopista.app.catastro.model.beans.EntidadGeneradora;
import com.geopista.app.catastro.model.beans.EstadoSiguiente;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FX_CC;
import com.geopista.app.catastro.model.beans.Fichero;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.IdBienInmueble;
import com.geopista.app.catastro.model.beans.IdCultivo;
import com.geopista.app.catastro.model.beans.ImagenCatastro;
import com.geopista.app.catastro.model.beans.NumeroFincaRegistral;
import com.geopista.app.catastro.model.beans.Persona;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.app.catastro.model.beans.RepartoCatastro;
import com.geopista.app.catastro.model.beans.SueloCatastro;
import com.geopista.app.catastro.model.beans.TipoExpediente;
import com.geopista.app.catastro.model.beans.Titular;
import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;
import com.geopista.app.catastro.model.datos.economicos.DatosBaseLiquidableBien;
import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosBien;
import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosConstruccion;
import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosFinca;
import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosSuelo;
import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosUC;
import com.geopista.app.catastro.model.datos.fisicos.DatosFisicosConstruccion;
import com.geopista.app.catastro.model.datos.fisicos.DatosFisicosFinca;
import com.geopista.app.catastro.model.datos.fisicos.DatosFisicosSuelo;
import com.geopista.app.catastro.model.datos.fisicos.DatosFisicosUC;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaPoligono;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaTramos;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaUrbanistica;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaZonaValor;
import com.geopista.app.catastro.model.datos.ponencia.TipoValor;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.servicioWebCatastro.beans.CabeceraWSCatastro;
import com.geopista.app.catastro.servicioWebCatastro.beans.IdentificadorDialogo;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.catastro.servicioWebCatastro.CabeceraFinEntrada;
import com.geopista.server.catastro.servicioWebCatastro.CabeceraVARPAD;
import com.geopista.server.catastro.servicioWebCatastro.FxccExportacion;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.TypeUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.java2xml.Java2XMLCatastro;


/**
 * Clase de acceso a base de datos. Esta clase se divide en dos partes. Los metodos publicos que da acceso a los modulos
 * externos, y que escriben los objetos outputStream con los resultados, para que sean devueltos por los servlet. Los
 * metodos privados que son los que acceden a base de datos y tratan con las bean del modulo. Los metodos publicos
 * pueden encapsular llamadas a varios metodos privados.
 */

public class COperacionesDGC
{
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(COperacionesDGC.class);

	private static final String DEFAULT_CODIGO_MUNICIPIO_DGC = "000";
	/**
	 * Metodo que envuelve la creacion de expediente en BD, ya que consta de insertar una direccion, coger la entidad
	 * generadora pasada como parametro, insertar en la tabla expediente_finca_catastro con las referencias catastrales
	 * del expediente y crear el expediente. Desde esta clase se realizan todas esas acciones. El objeto
	 * oos se utiliza para escribir el resultado que se devolvera en el response del servlet. Antes de insertar se
	 * comprueba que no haya ningun expediente con el mismo numero de expediente.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @param nomEntGen Nombre de la entidad Generadora
	 * @param exp Expediente a guardar
	 * @param idUser Id del usuario
	 * @throws Exception Las Excepciones registradas
	 * */
	public void crearExpediente(ObjectOutputStream oos, Expediente exp, String nomEntGen, String idUser)throws Exception
	{
		try
		{    	   
			if(!existeExpediente(exp.getNumeroExpediente()))
			{
				exp.setIdTecnicoCatastro(idUser);
				insertarDireccionDB(exp.getDireccionPresentador(), exp.getIdMunicipio());
				getEntidadGeneradoraBD(exp, nomEntGen, false,-1);
				Object o2= crearExpedienteDB(exp);
				if(exp.getListaReferencias()!=null && exp.getListaReferencias().size()>0)
				{
					for(int i = 0;i<exp.getListaReferencias().size();i++)
					{
						if(exp.getListaReferencias().get(i) instanceof FincaCatastro)
						{
							insertaFincaCatastroExpedienteDB(exp, i);
						}
						else if(exp.getListaReferencias().get(i) instanceof BienInmuebleCatastro)
						{
							insertarBienInmuebleCatastroExpedienteBD(exp, i);
						}
					}
				}
				oos.writeObject(o2);
			}
		}
		catch(Exception e)
		{
			logger.error("crearExpediente: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	public void crearExpediente(ObjectOutputStream oos, Expediente exp, String idUser) throws Exception{
		try{
			if(!existeExpediente(exp.getNumeroExpediente())){

				exp.setIdTecnicoCatastro(idUser);
				insertarDireccionDB(exp.getDireccionPresentador(), exp.getIdMunicipio());
				getEntidadGeneradoraBD(exp, null, true,1);

				Object o2= crearExpedienteDB(exp);

				if(exp.getListaReferencias()!=null && exp.getListaReferencias().size()>0){
					for(int i = 0;i<exp.getListaReferencias().size();i++){

						if(exp.getListaReferencias().get(i) instanceof FincaCatastro)
							insertaFincaCatastroExpedienteDB(exp, i);
						else if(exp.getListaReferencias().get(i) instanceof BienInmuebleCatastro)
							insertarBienInmuebleCatastroExpedienteBD(exp, i);
					}
				}
				oos.writeObject(o2);
			}
		}
		catch(Exception e){
			logger.error("crearExpediente: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Elimina las referncias asociadas al expediente que llega por parámetro de la tabla
	 * de catastro temporal
	 * @param exp el Expediente
	 * @param refCatastral La ref Catastral
	 * @throws Exception Las Excepciones registradas
	 */
	private void removeRefenciasCat_Temporal(Expediente exp, String refCatastral) throws Exception
	{
		String SQL = "DELETE FROM catastro_temporal WHERE id_expediente=? AND referencia=?";
		PreparedStatement ps= null;
		Connection conn= null;

		try
		{
			conn= CPoolDatabase.getConnection();

			ps= conn.prepareStatement(SQL);
			ps.setLong(1, exp.getIdExpediente());
			ps.setString(2, refCatastral);

			ps.execute();
			conn.commit();

		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}


	/**
	 * Elimina las referncias asociadas al expediente que llega por parámetro de la tabla
	 * de exp_finca_catastro
	 * @param exp  El Expediente
	 * @param refCatastral La Ref Catastral
	 * @throws Exception  Las Excepciones registradas
	 */
	private void removeReferenciasExp_Finca_Cat(Expediente exp, String refCatastral) throws Exception
	{
		String SQL = "DELETE FROM expediente_finca_catastro WHERE id_expediente=? AND ref_catastral=?";
		PreparedStatement ps= null;
		Connection conn= null;

		try
		{
			conn= CPoolDatabase.getConnection();

			ps= conn.prepareStatement(SQL);
			ps.setLong(1, exp.getIdExpediente());
			ps.setString(2, refCatastral);

			ps.execute();
			conn.commit();

		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}


	public void modificarExpediente(ObjectOutputStream oos, Expediente exp, String nomEntGen, String idUser)throws Exception
	{
		try
		{
			if(exp.getListaReferencias()!=null)
			{

				for(int i = 0;i<exp.getListaReferencias().size();i++)
				{
					if(exp.getListaReferencias().get(i) instanceof FincaCatastro)
					{
						FincaCatastro finca = (FincaCatastro) exp.getListaReferencias().get(i);

						modificaFincaExpedienteDB(exp, i);       
					}
					else if(exp.getListaReferencias().get(i) instanceof BienInmuebleJuridico)
					{
						BienInmuebleJuridico bij = (BienInmuebleJuridico)exp.getListaReferencias().get(i);
						if (bij.isDelete())
						{
							//El bien inmueble se ha eliminado
							removeRefenciasCat_Temporal(exp, bij.getBienInmueble().getIdBienInmueble().getParcelaCatastral().getRefCatastral());
							removeReferenciasExp_Finca_Cat(exp, bij.getBienInmueble().getIdBienInmueble().getParcelaCatastral().getRefCatastral());
						}
						else
							modificaBienInmuebleExpedienteBD(exp, i);
					}
				}
			}
			oos.writeObject(exp);
		}
		catch(Exception e)
		{
			logger.error("modificar expediente: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}		
	}

	/**
	 * Metodo que devuelve todos los expedientes no cerrados de un usario pasado por parametro, en caso de que sea la
	 * secuencia "admin", se mostraran todos los expedientes no cerrados. La solicitud por defecto es de todos los
	 * expedientes menos los cerrados, si se desean obtener estos el parametro expCerrado debe contener "true"
	 * y devolvera solo los expedientes cerrados. Se recogen los expedientes con todos sus objetos internos (Direccion,
	 * entidad generadora, etc).El objeto oos se utiliza para escribir el resultado que se devolvera en el response
	 *  del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @param idUsuario Id del usuario
	 * @param expCerrado Objeto que si contiene true, busca solo los expedientes cerrados, y si es false el resto
	 * @param convenio El convenio con el que se esta trabajando.
	 * @param idMunicipio El municipio con el que se esta trabajando.
	 * @throws Exception Las Excepciones registradas
	 * */
	public void getExpedientesUsuario(ObjectOutputStream oos, String idUsuario, String expCerrado, String convenio,
			String idMunicipio, boolean isAcoplado)throws Exception
			{
		try
		{
			for(Iterator it= getExpedientesUsuarioDB(idUsuario, expCerrado, convenio, idMunicipio, isAcoplado).iterator();it.hasNext();)
			{
				oos.writeObject((Expediente)it.next());
			}
		}
		catch(Exception e)
		{
			logger.error("getExpedientesUsuario: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
			}


	/**
	 * Metodo que devuelve el expediente con las parcelas o bienes inmuebles asociados al mismo.El objeto oos se
	 * utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @param convenio El convenio con el que se esta trabajando.
	 * @param idMunicipio El municipio con el que se esta trabajando.
	 * @throws Exception  Las Excepciones registradas
	 * */
	public void getParcelasExpediente(ObjectOutputStream oos,Expediente exp, String convenio, String idMunicipio)throws Exception
	{
		try
		{
			if(convenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
			{
				getBienInmuebleCatastroExpedienteDB(exp, idMunicipio);
			}
			else if(convenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO) &&
					exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
			{
				getFincaCatastroExpedienteDB(exp, idMunicipio);
			}
			else if(convenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO) &&
					exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
			{
				getBienInmuebleCatastroExpedienteDB(exp, idMunicipio);
			}
			oos.writeObject(exp);           
		}
		catch(Exception e)
		{
			logger.error("getParcelasExpediente: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Devuelve una hash con el id de los usuarios como key y el nombre como valor de los usuarios que tengan expedientes
	 * en la tabla expediente. El objeto oos se utiliza para escribir el resultado que se devolvera en el response
	 *  del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @throws Exception  Las Excepciones registradas
	 * */
	public void getUsuariosConExpediente(ObjectOutputStream oos)throws Exception
	{
		try
		{
			Hashtable hashDatos= getUsuariosConExpedienteBD();
			oos.writeObject(hashDatos);
		}
		catch(Exception e)
		{
			logger.error("getUsuarios: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Funcion que devuelve un arrayList con los codigos de las entidades generadoras que hay en BD. El objeto oos se
	 * utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @throws Exception  Las Excepciones registradas
	 * */
	public void getCodigoEntidadGeneradora(ObjectOutputStream oos)throws Exception
	{
		try
		{
			ArrayList listaDatos= (ArrayList)getCodigoEntidadGeneradoraBD();
			oos.writeObject(listaDatos);
		}
		catch(Exception e)
		{
			logger.error("getCodigoEntidadGeneradora: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Funcion que devuelve todos los codigos de los estados que hay en BD. El objeto oos se utiliza para escribir el
	 *  resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @throws Exception  Las Excepciones registradas
	 * */
	public void getCodigoEstados(ObjectOutputStream oos)throws Exception
	{
		try
		{
			ArrayList listaDatos= (ArrayList)getCodigoEstadosBD();
			oos.writeObject(listaDatos);
		}
		catch(Exception e)
		{
			logger.error("getCodigoEstados: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Funcion que devuelve un arrayList con objetos FincaCatastro para recoger la informacion
	 * de la localizacion de las parcelas de las referencias catastrales de un expediente. El objeto oos se utiliza
	 * para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado.
	 * @param refCatastrales Las referencias catastrales de la Finca.
	 * @param idMunicipio El id del municipio con el que se trabaja.
	 * @throws Exception Las Excepciones registradas
	 * */
	public void getFincaCatastroPorRefCatastral(ObjectOutputStream oos, ArrayList refCatastrales, String idMunicipio)throws Exception
	{
		try
		{
			for(Iterator it= getFincaCatastroPorRefCatastralDB(refCatastrales, idMunicipio).iterator();it.hasNext();)
			{
				oos.writeObject((FincaCatastro)it.next());
			}
		}
		catch(Exception e)
		{
			logger.error("getFincaCatastroPorRefCatastral: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que busca en la tabla parcelas las referencias catastrales que contengan el patron pasado por parametro,
	 * devolviendo un arrayList con los objetos FincaCatastro que cumplan la condicion. El objeto oos se
	 * utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @param idMunicipio El id del municipio con el que se trabaja.
	 * @param patron El patron de referencia catastral por el que buscar.
	 * @throws Exception  Las Excepciones registradas
	 */
	public void getFincaCatastroBuscadas(ObjectOutputStream oos, String idMunicipio, String patron) throws Exception
	{
		try
		{
			for(Iterator it= getFincaCatastroBuscadasDB(idMunicipio, patron).iterator();it.hasNext();)
			{
				oos.writeObject((FincaCatastro)it.next());
			}
		}
		catch(Exception e)
		{
			logger.error("getDireccionRefCatastralBuscadas: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que busca en la tabla bien_inmueble los bienes inmuebles que contengan el patron pasado por parametro,
	 * devolviendo un arrayList con los objetos bienInmueble que cumplan la condicion. El objeto oos se
	 * utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @param idMunicipio El id del municipio con el que se trabaja.
	 * @param patron El patron de referencia catastral por el que buscar.
	 * @throws Exception Las Excepciones registradas
	 */
	public void getBienInmuebleCatastroBuscados(ObjectOutputStream oos, String idMunicipio, String patron) throws Exception
	{
		try
		{
			for(Iterator it= getBienInmuebleCatastroBuscadosBD(idMunicipio, patron).iterator();it.hasNext();)
			{
				oos.writeObject((BienInmuebleCatastro)it.next());
			}
		}
		catch(Exception e)
		{
			logger.error("getBienInmuebleTitularidadBuscados: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que busca en la tabla parcelas las referencias catastrales que contengan el patronNombreVia y el
	 * patronTipoVia en los campos nombreVia y tipoVia de la tabla parcelas referenciada por id_via en parcelas,
	 * devolviendo un arrayList con los objetos FincaCatastro que cumplan la condicion. El objeto oos se
	 * utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @param idMunicipio El id del municipio con el que se esta trabajando.
	 * @param patronNombreVia El patron de busqueda del nombre de la via.
	 * @param patronTipoVia El patron de busqueda del tipo de la via.
	 * @throws Exception  Las Excepciones registradas
	 */
	public void getFincaCatastroBuscadasPorDir(ObjectOutputStream oos, String idMunicipio, String patronNombreVia,
			String patronTipoVia) throws Exception
			{
		try
		{
			for(Iterator it= getFincaCatastroBuscadasPorDirDB(idMunicipio, patronNombreVia, patronTipoVia).iterator();it.hasNext();)
			{
				oos.writeObject((FincaCatastro)it.next());
			}
		}
		catch(Exception e)
		{
			logger.error("getDireccionRefCatastralBuscadasPorDir: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
			}
	
	public void obtenerPonenciaZonaValorBD(ObjectOutputStream oos, String codZonaValor, String idMunicipio) throws Exception
	{
		try
		{
			PonenciaZonaValor ponenciaZonaValor= obtenerPonenciaZonaValor(codZonaValor, idMunicipio);			
			oos.writeObject((PonenciaZonaValor)ponenciaZonaValor);
		}
		catch(Exception e)
		{
			logger.error("obtenerPonenciaZonaValorBD: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}
	
	public void obtenerPonenciaUrbanisticaBD(ObjectOutputStream oos, String codPonenciaUrbanistica, String idMunicipio) throws Exception
	{
		try
		{
			PonenciaUrbanistica ponenciaUrbanistica= obtenerPonenciaUrbanistica(codPonenciaUrbanistica, idMunicipio);			
			oos.writeObject((PonenciaUrbanistica)ponenciaUrbanistica);
		}
		catch(Exception e)
		{
			logger.error("obtenerPonenciaUrbanisticaBD: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}
	
	public void obtenerPonenciaPoligonoBD(ObjectOutputStream oos, String codPonenciaPoligono, String idMunicipio) throws Exception
	{
		try
		{
			PonenciaPoligono ponenciaPoligono = obtenerPonenciaPoligono(codPonenciaPoligono, idMunicipio);			
			oos.writeObject((PonenciaPoligono)ponenciaPoligono);
		}
		catch(Exception e)
		{
			logger.error("obtenerPonenciaPoligonoBD: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}
	
	public void obtenerPonenciaTramosBD(ObjectOutputStream oos, String codTramoPonencia, String idMunicipio) throws Exception
	{
		try
		{
			PonenciaTramos ponenciaTramos = obtenerPonenciaTramo(codTramoPonencia, idMunicipio);			
			oos.writeObject((PonenciaTramos)ponenciaTramos);
		}
		catch(Exception e)
		{
			logger.error("obtenerPonenciaPoligonoBD: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que busca en la tabla parcelas las referencias catastrales que contengan el patronPoligono y el
	 * patronParcela en los campos codigoPoligono y codigoParcela de la tabla parcelas,
	 * devolviendo un arrayList con los objetos FincaCatastro que cumplan la condicion. El objeto oos se
	 * utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @param idMunicipio El id del municipio con el que se esta trabajando.
	 * @param patronPoligono El patron de busqueda del nombre de la via.
	 * @param patronParcela El patron de busqueda del tipo de la via.
	 * @throws Exception  Las Excepciones registradas
	 */
	public void getFincaCatastroRusticasBuscadasPorPoligono(ObjectOutputStream oos, String idMunicipio, String patronPoligono,
			String patronParcela) throws Exception
			{
		try
		{
			for(Iterator it= getFincaCatastroRusticasBuscadasPorPoligonoDB(idMunicipio, patronPoligono, patronParcela).iterator();it.hasNext();)
			{
				oos.writeObject((FincaCatastro)it.next());
			}
		}
		catch(Exception e)
		{
			logger.error("getFincaCatastroRusticasBuscadasPorPoligono: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
			}

	/**
	 * Metodo que busca en la tabla bien_inmueble las referencias catastrales que contengan el patronNombreVia y el
	 * patronTipoVia en los campos nombreVia y tipoVia de la tabla parcelas referenciada por id_via en parcelas,
	 * devolviendo un arrayList con los objetos BienInmueble que cumplan la condicion. El objeto oos se
	 * utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @param idMunicipio El id del municipio con el que se esta trabajando.
	 * @param patronNombreVia El patron de busqueda del nombre de la via.
	 * @param patronTipoVia El patron de busqueda del tipo de la via.
	 * @throws Exception  Las Excepciones registradas
	 */    
	public void getBienInmuebleCatastroBuscadosPorDir(ObjectOutputStream oos, String idMunicipio, String patronNombreVia,
			String patronTipoVia) throws Exception
			{
		try
		{
			for(Iterator it= getBienInmuebleCatastroBuscadosPorDirDB(idMunicipio, patronNombreVia, patronTipoVia).iterator();it.hasNext();)
			{
				oos.writeObject((BienInmuebleCatastro)it.next());
			}
		}
		catch(Exception e)
		{
			logger.error("getBienInmuebleTitularidadBuscadosPorDir: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
			}

	/**
	 * Metodo que busca en la tabla bien_inmueble las referencias catastrales que contengan el patronPoligono y el
	 * patronParcela en los campos poligono_rustico y parcela de la tabla bien_inmueble, referenciada con parcelas
	 * para la referencia catastral, devolviendo un arrayList con los objetos BienInmueble que cumplan la condicion.
	 * El objeto oos se utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @param idMunicipio El id del municipio con el que se esta trabajando.
	 * @param patronPoligono El patron del poligono.
	 * @param patronParcela El patron de la parcela.
	 * @throws Exception Las Excepciones registradas
	 */
	public void getBienInmuebleCatastroRusticoBuscadosPorPoligono(ObjectOutputStream oos, String idMunicipio,
			String patronPoligono, String patronParcela) throws Exception
			{
		try
		{                                
			for(Iterator it= getBienInmuebleCatastroRusticoBuscadosPorPoligonoDB(idMunicipio, patronPoligono, patronParcela).iterator();it.hasNext();)
			{
				oos.writeObject((BienInmuebleCatastro)it.next());
			}
		}
		catch(Exception e)
		{
			logger.error("getBienInmuebleCatastroRusticoBuscadosPorPoligono: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
			}

	/**
	 * Metodo que devuelve los valores de la tabla estado_siguiente, almacenandolos en el bean estadoSiguiente. El
	 * objeto oos se utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @throws Exception Las Excepciones registradas
	 */
	public void getEstadoSiguiente(ObjectOutputStream oos) throws Exception
	{
		try
		{
			for(Iterator it= getEstadoSiguienteDB().iterator();it.hasNext();)
			{
				oos.writeObject((EstadoSiguiente)it.next());
			}
		}
		catch(Exception e)
		{
			logger.error("getEstadoSiguiente: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que actualiza un expediente y actualiza las referencias a fincas o bienes inmuebles borrando antes las
	 * referencias que existieran asociadas con ese expediente en las tablas expediente_finca_catastro y
	 * expediente_bieninmueble, y añadiendo las nuesvas. No se devuelve valor, el expediente ya esta actualizado
	 * en la aplicacion. El objeto oos se utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @param exp El expediente a actualizar
	 * @throws Exception Las Excepciones registradas
	 */
	public void updateExpediente(ObjectOutputStream oos, Expediente exp)throws Exception{
		try{
			if(exp.getListaReferencias()!=null && exp.getListaReferencias().size()>0)
				updateReferenciasExpedienteBD(exp);

			updateDireccionDB(exp.getDireccionPresentador(), exp.getIdMunicipio());
			Object o= updateExpedienteBD(exp);
			oos.writeObject(o);
		}
		catch(Exception e)
		{
			logger.error("updateExpediente: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que devuelve una lista con los ficheros almacenados en base de datos para mostrar el historicos de las
	 * consultas con catastro. El objeto oos se utiliza para escribir el resultado que se devolvera en el response
	 * del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @throws Exception Las Excepciones registradas
	 */
	public void consultaHistoricoFicheros(ObjectOutputStream oos, String idMunicipio) throws Exception
	{
		try
		{
			for(Iterator it= consultaHistoricoFicherosBD(idMunicipio).iterator();it.hasNext();)
			{
				oos.writeObject((Fichero)it.next());
			}
		}
		catch(Exception e)
		{
			logger.error("consultaHistoricoFicheros: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que devuelve una via buscandola por el patron del nombre pasado. El objeto oos se
	 * utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @throws Exception  Las Excepciones registradas
	 */
	public void getViaPorNombre(ObjectOutputStream oos, String nombre, String idMunicipio)throws Exception
	{
		try
		{
			DireccionLocalizacion dir= (DireccionLocalizacion)getViaPorNombreBD(nombre, idMunicipio);
			oos.writeObject(dir);
		}
		catch(Exception e)
		{
			logger.error("getTiposVias: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que comprueba si hay que avisar para que el usuario haga una actualizacion o un envio de informacion con
	 * catastro. Si hay que realizar alguna de estas tareas se lanza una excepcion que sera capturada en la parte cliente
	 * y mostrara al usuario el mensaje. El objeto oos se utiliza para escribir el resultado que se devolvera en el
	 * response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @throws Exception Las Excepciones registradas
	 */
	public void comprobarActualizacionYEnvios(ObjectOutputStream oos, String idMunicipio)throws Exception {
		try{
			comprobarActualizacionYEnviosBD(idMunicipio);
		}
		catch(Exception e){
			if(e instanceof ACException)
				oos.writeObject(new ACException(e));
			else{
				logger.error("comprobarActualizacionYEnvios: "+ e.getMessage());
				oos.writeObject(new ACException(e));
				throw e;
			}
		}
	}

	/**
	 * Metodo que devuelve una hash con dos listas, una el nombre de las provincias y otro el codigo de las mismas. Los
	 * dos arrays se corresponden en posiciones. Las keys de la hash son "nombres" y "codigos".
	 *  El objeto oos se utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @throws Exception  Las Excepciones registradas
	 */
	public void getCodigoNombreProvincia(ObjectOutputStream oos)throws Exception
	{
		try
		{
			Hashtable hashDatos= getCodigoNombreProvinciaBD();
			oos.writeObject(hashDatos);
		}
		catch(Exception e)
		{
			logger.error("getCodigoNombreProvincia: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que devuelve una hash con dos listas, una el nombre de los municipios y otro el codigo de los mismos. Los
	 * dos arrays se corresponden en posiciones. Las keys de la hash son "nombres" y "codigos".
	 *  El objeto oos se utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @throws Exception Las Excepciones registradas
	 */
	public void getCodigoNombreMunicipio(ObjectOutputStream oos, String codigoProvincia)throws Exception
	{
		try
		{
			Hashtable hashDatos= getCodigoNombreMunicipioBD(codigoProvincia);
			oos.writeObject(hashDatos);
		}
		catch(Exception e)
		{
			logger.error("getCodigoNombreMunicipio: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}
	
	public void getCodigoDGCMunicipio(ObjectOutputStream oos, String codigoMunicipioINE, String codigoProvincia)throws Exception
	{
		try
		{
			String codigoMunicipioDGC= getCodigoDGCMunicipioBD(codigoMunicipioINE, codigoProvincia);
			oos.writeObject(codigoMunicipioDGC);
		}
		catch(Exception e)
		{
			logger.error("getCodigoDGCeMunicipio: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que devuelve una hash con dos listas, una el nombre de las provincias y otro el codigo de las mismas. Los
	 * dos arrays se corresponden en posiciones. Las keys de la hash son "nombres" y "codigos".
	 *  El objeto oos se utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @param datos Los datos de configuracion.
	 * @throws Exception
	 */     
	public void guardarParametroConfiguracion(ObjectOutputStream oos, DatosConfiguracion datos, String idMunicipio)throws Exception
	{
		try
		{
			guardarParametroConfiguracionBD(datos, false, null, null, idMunicipio);
		}
		catch(Exception e)
		{
			logger.error("guardarParametroConfiguracion: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que devuelve el objeto de DatosConfiguracion, con la configuracion que hay guardada en base de datos.
	 *  El objeto oos se utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @throws Exception
	 */
	public void getParametrosConfiguracion(ObjectOutputStream oos, String idMunicipio)throws Exception
	{
		try
		{
			Object datos= getParametrosConfiguracionBD(idMunicipio);
			oos.writeObject(datos);
		}
		catch(Exception e)
		{
			logger.error("getParametrosConfiguracion: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que devuelve una lista con los tipos de expediente que se pueden trabajar con el convenio que se esta
	 * trabajando y que se recibe por parametro. El objeto oos se utiliza para escribir el resultado que se devolvera
	 * en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado.
	 * @param convenio El convenio con el que se esta trabajando.
	 * @throws Exception
	 */
	public void getTiposExpedientes(ObjectOutputStream oos, String convenio) throws Exception
	{
		try
		{
			for(Iterator it= getTiposExpedientesBD(convenio,-1).iterator();it.hasNext();)
			{
				oos.writeObject((TipoExpediente)it.next());
			}
		}
		catch(Exception e)
		{
			logger.error("getTiposExpedientes: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que busca en la tabla bien_inmueble y personas las referencias catastrales que contengan el patronNif
	 * asociado. Se devuelve los objetos persona que contienen los bienes inmuebles. El objeto oos se utiliza para
	 * escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @param patronNif El patron del nif del titular que se busca.
	 * @param idMunicipio El id del municipio con el que se esta trabajando.
	 * @throws Exception
	 */
	public void getBienesInmueblesBuscadasPorTitular(ObjectOutputStream oos,String patronNif, String idMunicipio) throws Exception
	{
		try
		{
			for(Iterator it= getBienesInmueblesBuscadasPorTitularBD(patronNif, idMunicipio).iterator();it.hasNext();)
			{
				oos.writeObject((Persona)it.next());
			}
		}
		catch(Exception e)
		{
			logger.error("getBienesInmueblesBuscadasPorTitular: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo inserta un expediente en la tabla expediente. Este metodo se diferencia de crearExpediente en que estos
	 * expedientes insertados son los recibidos por catastro, por lo que ya tienen rellenos todos los datos.
	 * El objeto oos se utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado.
	 * @param exp El expediente a insertar.
	 * @param idUser El id del usuarios que ha importado.
	 * @throws Exception
	 */
	public void crearExpedienteCatastro(ObjectOutputStream oos, Expediente exp,String idUser)throws Exception
	{
		try
		{
			exp.setIdTecnicoCatastro(idUser);
			insertarDireccionDB(exp.getDireccionPresentador(), exp.getIdMunicipio());
			crearExpedienteDB(exp);
			if(exp.getListaReferencias()!=null && exp.getListaReferencias().size()>0)
			{
				for(int i = 0;i<exp.getListaReferencias().size();i++)
				{
					if(exp.getListaReferencias().get(i) instanceof ReferenciaCatastral)
					{
						insertaFincaCatastroExpedienteDB(exp, i);
					}
					else if(exp.getListaReferencias().get(i) instanceof IdBienInmueble)
					{
						insertarBienInmuebleCatastroExpedienteBD(exp, i);
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error("crearExpedienteCatastro: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que inserta un fichero pasado como parametro y que asocia los expedientes, involucrados en ese fichero,
	 * mediante la tabla expediente_fichero. Ademas actualiza las fechas de envio y actualizacion periodicas.
	 * El objeto oos se utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @param listaNumerosExp Lista de los numeros de expedientes asociados al fichero
	 * @param fichero Fichero a insertar en BBDD.
	 * @throws Exception
	 */
	public void crearFichero(ObjectOutputStream oos, ArrayList listaNumerosExp, Fichero fichero, String idMunicipio) throws Exception
	{
		try
		{
			crearFicheroBD(listaNumerosExp,fichero, idMunicipio);
		}
		catch(Exception e)
		{
			logger.error("crearFichero: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que devuelve una lista con los expedientes que se tienen que exportar en la exportacion masiva. Para ello
	 * se comprueba que esten dentro del convenio y que esten en el municipio con el que se esta trabajando.
	 *
	 * @param oos Objeto donde se escriben las excepciones que seran propagadas.
	 * @param idMunicipio El id del municipio con el que se esta trabajando.
	 * @param convenio Convenio con el que se esta trabajando.
	 * @return ArrayList
	 * @throws Exception
	 */
	public ArrayList getExpedientesExportacionMasiva(ObjectOutputStream oos, String idMunicipio, String convenio) throws Exception
	{
		try
		{
			return getExpedientesExportacionMasivaBD(idMunicipio, convenio);
		}
		catch(Exception e)
		{
			logger.error("getExpedientesExportacionMasiva: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que devuelve una lista  con dos listas, una que contiene los xml generados para sus parcelas o bienes
	 *  inmuebles y  otra con sus fx_cc asociados.
	 *
	 * @param oos Objeto donde se escriben las excepciones que seran propagadas.
	 * @param expediente Expediente sobre el que se busca sus xml´s en catastro temporal.
	 * @return ArrayList Lista con dos listas una para los xml de las parcelas y otra para la fx_cc asociada
	 * @throws Exception
	 */
	public ArrayList getXmlParcelasFX_CCExp(ObjectOutputStream oos, Expediente expediente) throws Exception
	{
		try
		{
			return getXmlParcelasFX_CCExpBD(expediente);
		}
		catch(Exception e)
		{
			logger.error("getXmlParcelasFX_CCExp: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}
	
	
	/**
	 * Metodo que devuelve una lista  con dos listas, una que contiene los xml generados para sus parcelas o bienes
	 *  inmuebles y  otra con sus fx_cc asociados.
	 *
	 * @param oos Objeto donde se escriben las excepciones que seran propagadas.
	 * @param expediente Expediente sobre el que se busca sus xml´s en catastro temporal.
	 * @return ArrayList Lista con dos listas una para los xml de las parcelas y otra para la fx_cc asociada
	 * @throws Exception
	 */
	public ArrayList getXmlParcelasFX_CCExp( Expediente expediente) throws Exception
	{
		try
		{
			return getXmlParcelasFX_CCExpBD(expediente);
		}
		catch(Exception e)
		{
			logger.error("getXmlParcelasFX_CCExp: "+ e.getMessage());
			throw e;
		}
	}
	
	
	public ArrayList getFxccParcelasExp(ObjectOutputStream oos, Expediente expediente) throws Exception
	{
		try
		{
			return getFxccParcelasExpBD(expediente);
		}
		catch(Exception e)
		{
			logger.error("getXmlParcelasFX_CCExp: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que crea y devuelve una cabecera de archivo de exportacion masiva.
	 *
	 * @param oos Objeto donde se escriben las excepciones que seran propagadas.
	 * @param expedientes Los expedientes que se van a exportar.
	 * @param modoTrabajo El modo de trabajo, acoplado o desacoplado.
	 * @return CabeceraFinEntrada La cabecera creada.
	 * @throws Exception
	 */
	public CabeceraFinEntrada creaCabeceraFinEntradaMasivo(ObjectOutputStream oos, ArrayList expedientes, String modoTrabajo) throws Exception
	{
		try
		{
			return creaCabeceraFinEntradaMasivoBD(expedientes, modoTrabajo);
		}
		catch(Exception e)
		{
			logger.error("creaCabeceraFinEntradaMasivo: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	public CabeceraVARPAD creaCabeceraVARPADMasivo(ObjectOutputStream oos, ArrayList expedientes, String modoTrabajo) throws Exception
	{
		try
		{
			return creaCabeceraVARPADMasivoBD(expedientes, modoTrabajo);
		}
		catch(Exception e)
		{
			logger.error("creaCabeceraFinEntradaMasivo: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que crea y devuelve una cabecera de la comunicacion mediante WS con la DGC
	 *
	 * @param oos Objeto donde se escriben las excepciones que seran propagadas.
	 * @param expedientes Los expedientes que se van a exportar.
	 * @return CabeceraWSCatastro La cabecera creada.
	 * @throws Exception
	 */
	public CabeceraWSCatastro creaCabeceraWS(Expediente expediente, String tipo, String nombreSolicitante, String nifSolicitante) throws Exception
	{
		try
		{
			return creaCabeceraWSBD(expediente, tipo, nombreSolicitante, nifSolicitante);
		}
		catch(Exception e)
		{
			logger.error("creaCabeceraWS: "+ e.getMessage());
			throw e;
		}
	}
	/**
	 * Metodo que elimina las asociaciones de catastro_temporal que tenga el expediente pasado por parametro.
	 *  El objeto oos se utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escriben las excepciones que seran propagadas.
	 * @param expediente Expediente del que se van a eliminar las asocianes en catastro temporal, tabla catastro_temporal
	 * @throws Exception
	 */
	public void eliminaCatastroTemporalExp(ObjectOutputStream oos, Expediente expediente) throws Exception
	{
		try
		{
			eliminaCatastroTemporalExpBD(expediente);
		}
		catch(Exception e)
		{
			logger.error("eliminaCatastroTemporalExp: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que devuelve la fecha del ultimo envio masivo.
	 * El objeto oos se utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @throws Exception
	 */
	public void getFechaInicioPeriodoExp(ObjectOutputStream oos, String idMunicipio) throws Exception
	{
		try
		{
			Date fecha = getFechaInicioPeriodoExpBD(idMunicipio);
			oos.writeObject(fecha);
		}
		catch(Exception e)
		{
			logger.error("getFechaInicioPeriodoExp: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que actualiza el estado de los expedientes en estado finalizado despues de la exportacion masiva.
	 * El objeto oos se utiliza para escribir el resultado que se devolvera en el response del servlet.
	 *
	 * @param oos Objeto donde se escriben las excepciones que seran propagadas.
	 * @param expedientes Los expedientes exportados de los cuales se mirar cuales se tienen que actualizar.
	 * @throws Exception
	 */
	public void actualizaExpFinalizadosDespuesExportacion(ObjectOutputStream oos, ArrayList expedientes, String modoTrabajo) throws Exception
	{
		try
		{
			actualizaExpFinalizadosDespuesExportacionBD(expedientes, modoTrabajo);
		}
		catch(Exception e)
		{
			logger.error("actualizaExpFinalizadosDespuesExportacion: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/**
	 * Metodo que devuelve una hash con todos los usuarios de los programas geopista. La hash se genera con el nombre
	 * de usuario como clave y el id como value. El objeto oos se utiliza para escribir el resultado que se devolvera
	 * en el response del servlet.
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @throws Exception
	 */
	public void getTodosLosUsuarios(ObjectOutputStream oos)throws Exception
	{
		try
		{
			Hashtable hashDatos= getTodosLosUsuariosBD();
			oos.writeObject(hashDatos);
		}
		catch(Exception e)
		{
			logger.error("getTodosLosUsuarios: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}
	
	
	

	/**
	  * Actualiza el estado del bien en la tabla de expediente_finca_catastro para indicar que la finca esta 
	 * actualizada con la OVC
	 * @param idExp
	 * @param finca
	 * @param actualizado
	 * @throws Exception
	 */
	public void actualizarExpedienteFincaCatastroOVC(long idExp ,FincaCatastro finca, boolean actualizado) throws Exception{
		
		String idEntidadGeneradora = null;
		String sSQL= "UPDATE expediente_finca_catastro SET actualizado=? WHERE id_expediente = ? AND ref_catastral=?";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			
			ps.setBoolean(1,actualizado);
			ps.setLong(2,idExp);
			ps.setString(3,finca.getRefFinca().getRefCatastral());

			ps.execute();
			conn.commit();
		}
		catch (Exception e){
			throw e;
		}
		finally{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}
	
	/**
	 * Actualiza el estado del bien en la tabla de expediente_bieninmueble para indicar que el bien esta 
	 * actualizado con la OVC
	 * @param idExp
	 * @param bien
	 * @param actualizado
	 * @throws Exception
	 */
	public void actualizarExpedienteBienInmuebleOVC(long idExp ,BienInmuebleCatastro bien, boolean actualizado) throws Exception{
		
		String idEntidadGeneradora = null;
		String sSQL= "UPDATE expediente_bieninmueble SET actualizado=? WHERE id_expediente = ? AND id_bieninmueble=?";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			
			ps.setBoolean(1,actualizado);
			ps.setLong(2,idExp);
			ps.setString(3,bien.getIdBienInmueble().getIdBienInmueble());

			ps.execute();
			conn.commit();
		}
		catch (Exception e){
			throw e;
		}
		finally{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}
	
	
	/**
	 * Completa la información de las referencias del expediente que llega por parámetro,
	 * para poder guardarlo en catastro temporal.
	 * @param exp Expediente a completar.
	 * @param convenio El convenio con el que se esta trabajando.
	 * @throws Exception
	 */
	public void completaReferenciasToXMLBD(Expediente exp, String convenio, String idMunicipio) throws Exception{

		try {
			if(convenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
				exp = completarBienInmuebleCatastroExpedienteDB(exp, idMunicipio);

			else if(convenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO) &&
					exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
				exp = completarFincaCatastroExpedienteDB(exp, idMunicipio);

			else if(convenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO) &&
					exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))

				exp = completarBienInmuebleCatastroExpedienteDB(exp, idMunicipio);

		}
		catch (Exception e) {
			throw e;
		}

		for(int i = 0;i<exp.getListaReferencias().size();i++)
		{
			if(exp.getListaReferencias().get(i) instanceof FincaCatastro)
				modificaFincaExpedienteDB(exp, i);

			else if(exp.getListaReferencias().get(i) instanceof BienInmuebleJuridico)
				modificaBienInmuebleExpedienteBD(exp, i);

		}
	}
	
	
	public void existeParcelaEn(ObjectOutputStream oos, String refCatastral)throws Exception
	{
		try
		{
			FincaCatastro finca= existeParcelaEnBD(refCatastral);
			oos.writeObject(finca);
		}
		catch(Exception e)
		{
			logger.error("existeParcelaEn: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}
	
	public boolean existenReferenciasCatastro_temporal(ObjectOutputStream oos, Expediente exp)throws Exception
	{
	
		boolean existeRefCatastTemp = false;
		int numRefCatastroTemporal=0;
		String referencia= "";
		StringBuffer sSQL= new StringBuffer();
		sSQL.append("SELECT COUNT(*) AS total FROM catastro_temporal WHERE id_expediente=? AND referencia IN ( ");
		for(int i = 0;i<exp.getListaReferencias().size();i++)
		{
			
			if(exp.getListaReferencias().get(i) instanceof FincaCatastro){
				referencia = ((FincaCatastro)exp.getListaReferencias().get(i)).getRefFinca().getRefCatastral();

			}
			else if(exp.getListaReferencias().get(i) instanceof BienInmuebleJuridico){
				referencia = ((BienInmuebleJuridico)exp.getListaReferencias().get(i)).getBienInmueble().getIdBienInmueble().getIdBienInmueble();
			}
			else if(exp.getListaReferencias().get(i) instanceof BienInmuebleCatastro){
				referencia = ((BienInmuebleCatastro)exp.getListaReferencias().get(i)).getIdBienInmueble().getIdBienInmueble();
			}
			sSQL.append("'"+referencia+"'");
			if(i != (exp.getListaReferencias().size()-1)){
				sSQL.append(",");
			}
			
		}
			sSQL.append(")");
		
			
	
			
			PreparedStatement ps= null;
			Connection conn= null;
			ResultSet rs = null;
			try
			{
				conn= CPoolDatabase.getConnection();
				ps= conn.prepareStatement(sSQL.toString());
				//ps.setString(1,referencia);
				ps.setLong(1,exp.getIdExpediente());
	
				rs= ps.executeQuery();
				if(rs.next())
				{
					numRefCatastroTemporal = rs.getInt("total");
				}
				
				if(numRefCatastroTemporal == 0){
					existeRefCatastTemp = false;
				}
				else{
				
					existeRefCatastTemp = true;
				}
				oos.writeObject(existeRefCatastTemp);
			}
			catch (Exception e)
			{
				throw e;
			}
			finally
			{
				try{ps.close();}catch(Exception e){};
				try{rs.close();}catch(Exception e){};
				try{conn.close();}catch(Exception e){};
			}
		
		return existeRefCatastTemp;
	
	}

	public void existeBIEn(ObjectOutputStream oos, String idBI)throws Exception
	{
		try
		{
			BienInmuebleCatastro bi= existeBIEnBD(idBI);
			oos.writeObject(bi);
		}
		catch(Exception e)
		{
			logger.error("existeBIEn: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}

	/*------------------------------------------------ Parte privada -----------------------------------------------------*/

	
	private void actualizaExpedienteAsociacionGerencia(Expediente exp) throws Exception {
		
		String sSQL= "UPDATE EXPEDIENTE SET anio_expediente_gerencia = ? ,referencia_expediente_gerencia = ?, " +
				"codigo_entidad_registro_dgc_origen_alteracion = ?, anio_expediente_admin_origen_alteracion = ?, " +
				"referencia_expediente_admin_origen = ?, codigo_descriptivo_alteracion = ?";
		
		PreparedStatement ps= null;
		Connection conn= null;

		try{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			
			ps.setInt(1,exp.getAnnoExpedienteGerencia());
			ps.setString(2,exp.getReferenciaExpedienteGerencia());
			ps.setInt(3,exp.getCodigoEntidadRegistroDGCOrigenAlteracion());
			ps.setInt(4,exp.getAnnoExpedienteAdminOrigenAlteracion());
			ps.setString(5,exp.getReferenciaExpedienteAdminOrigen());                
			ps.setString(6, exp.getCodigoDescriptivoAlteracion());


			ps.execute();
			conn.commit();
		}
		catch (Exception e){
			throw e;
		}
		finally{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}
	/**
	 * Funcion que crea el expediente en BD de datos. El identificador del expediente se realiza con la secuencia
	 * seq_expediente. Los objetos direccion, entidad generadora, etc ya han sido creados.
	 *
	 * @param exp Expediente a insertar
	 * @return Expediente El expediente insertado y con datos actualizados como su id, etc.
	 * @throws Exception
	 * */
	private Expediente crearExpedienteDB(Expediente exp) throws Exception {
		if(exp==null)
			return null;

		String sSQL= "INSERT INTO EXPEDIENTE (ID_Expediente, ID_Entidad_Generadora, ID_Estado, ID_Tecnico_Catastro," +
		" ID_Localizacion, ID_Municipio, Numero_Expediente, ID_Tipo_Expediente, Fecha_Alteracion, " +
		" Anio_Expediente_Gerencia, Referencia_Expediente_Gerencia, Codigo_Entidad_Registro_DGC_Origen_Alteracion," +
		" Anio_Expediente_Admin_Origen_Alteracion, Referencia_Expediente_Admin_Origen, Fecha_Registro, " +
		" Tipo_Documento_Origen_Alteracion, Info_Documento_Origen_Alteracion," +

		" Codigo_Descriptivo_Alteracion, Descripcion_Alteracion, NIF_Presentador, Nombre_Completo_Presentador, " +
		" Num_BienesInmuebles_Urb, Num_BienesInmuebles_Rus, Num_BienesInmuebles_Esp, Cod_Provincia_Notaria, " +

		" Cod_Poblacion_Notaria, Cod_Notaria, Anio_protocolo_Notarial, Protocolo_notarial, modo_acoplado) " +

		" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		String sqlOracle = "INSERT INTO EXPEDIENTE (ID_Expediente, ID_Entidad_Generadora, ID_Estado, ID_Tecnico_Catastro," +
		" ID_Localizacion, ID_Municipio, Numero_Expediente, ID_Tipo_Expediente, Fecha_Alteracion, " +
		" Anio_Expediente_Gerencia, Referencia_Expediente_Gerencia, codent_reg_dgc_orig_alteracion," +
		" anio_exp_admin_orig_alteracion, referencia_exp_admin_origen, Fecha_Registro, " +
		" tipo_documento_orig_alteracion, info_documento_orig_alteracion," +

		" Codigo_Descriptivo_Alteracion, Descripcion_Alteracion, NIF_Presentador, Nombre_Completo_Presentador, " +
		" Num_BienesInmuebles_Urb, Num_BienesInmuebles_Rus, Num_BienesInmuebles_Esp, Cod_Provincia_Notaria, " +

		" Cod_Poblacion_Notaria, Cod_Notaria, Anio_protocolo_Notarial, Protocolo_notarial, modo_acoplado) " +

		" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		PreparedStatement ps= null;
		Connection conn= null;

		try{
			conn= CPoolDatabase.getConnection();

			if(CPoolDatabase.isPostgres(conn))
				ps= conn.prepareStatement(sSQL);
			else
				ps= conn.prepareStatement(sqlOracle);

			if(CPoolDatabase.isPostgres(conn))
				exp.setIdExpediente(getIdDB("select nextval('seq_expediente') as id_expediente","id_expediente"));
			else
				exp.setIdExpediente(getIdDB("select seq_expediente.nextval as id_expediente from dual","id_expediente"));

			ps.setLong(1,exp.getIdExpediente());
			ps.setLong(2,exp.getEntidadGeneradora().getIdEntidadGeneradora());
			ps.setLong(3,exp.getIdEstado());
			ps.setString(4,exp.getIdTecnicoCatastro());
			ps.setLong(5,exp.getDireccionPresentador().getIdLocalizacion());
			ps.setLong(6,exp.getIdMunicipio());
			ps.setString(7,exp.getNumeroExpediente());
			int idTipoExpediente = getIdTipoExpediente(exp.getTipoExpediente().getCodigoTipoExpediente());
			ps.setInt(8,idTipoExpediente);

			if(exp.getFechaAlteracion()!=null)
				ps.setDate(9,new java.sql.Date(exp.getFechaAlteracion().getTime()));
			else
				ps.setDate(9,null);

			ps.setObject(10, exp.getAnnoExpedienteGerencia());

			ps.setString(11, exp.getReferenciaExpedienteGerencia());

			ps.setObject(12, exp.getCodigoEntidadRegistroDGCOrigenAlteracion());

			Calendar aux= new GregorianCalendar();
			aux.setTime(new Date(System.currentTimeMillis()));
			int anno = aux.get(Calendar.YEAR);
			exp.setAnnoExpedienteAdminOrigenAlteracion(anno);
			ps.setInt(13, exp.getAnnoExpedienteAdminOrigenAlteracion());
			ps.setString(14, exp.getReferenciaExpedienteAdminOrigen());
			ps.setDate(15, new java.sql.Date(exp.getFechaRegistro().getTime()));
			ps.setString(16, exp.getTipoDocumentoOrigenAlteracion());
			ps.setString(17, exp.getInfoDocumentoOrigenAlteracion());
			ps.setString(18, exp.getCodigoDescriptivoAlteracion());
			ps.setString(19, exp.getDescripcionAlteracion());
			ps.setString(20, exp.getNifPresentador());
			ps.setString(21,exp.getNombreCompletoPresentador());
			ps.setLong(22, exp.getNumBienesInmueblesUrbanos());
			ps.setLong(23, exp.getNumBienesInmueblesRusticos());
			ps.setLong(24, exp.getNumBienesInmueblesCaractEsp());
			ps.setString(25, exp.getCodProvinciaNotaria());
			ps.setString(26, exp.getCodPoblacionNotaria());
			ps.setString(27, exp.getCodNotaria());
			ps.setObject(28, exp.getAnnoProtocoloNotarial());
			ps.setString(29, exp.getProtocoloNotarial());
			ps.setBoolean(30, exp.isModoAcoplado());
			ps.execute();
			conn.commit();            
		}
		catch (Exception e){
			throw e;
		}
		finally{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return exp;
	}

	/**
	 * Funcion que inserta en BD la direccion de un expediente. El identificador de la direccion se realiza con la secuencia
	 * seq_domicilio_localizacion.
	 *
	 * @param dir La direccion a insertar.
	 * @param idMunicipio El municipio sobre el que se esta trabajando.
	 * @throws Exception
	 * */
	private void insertarDireccionDB(DireccionLocalizacion dir, long idMunicipio) throws Exception{
		if(dir!=null) {

			String sSQL= "INSERT INTO Domicilio_Localizacion (ID_Localizacion, Provincia_INE, Municipio_INE, Codigo_MunicipioDGC," +
			" Nombre_Entidad_Menor, Codigo_Via, Primer_Numero, Primera_Letra, Segundo_Numero, " +
			" Segunda_Letra, Kilometro, Bloque, Escalera, Planta, Puerta, Direccion_No_Estructurada," +
			" Codigo_Postal, Nombre_Via, Apartado_Correos, Tipo_Via, Nombre_Provincia, Nombre_Municipio)" +
			" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			PreparedStatement ps= null;
			Connection conn= null;

			try{
				conn= CPoolDatabase.getConnection();
				ps= conn.prepareStatement(sSQL);

				if(CPoolDatabase.isPostgres(conn))
					dir.setIdLocalizacion(getIdDB("select nextval('seq_domicilio_localizacion') as id_domicilio_localizacion","id_domicilio_localizacion"));
				else
					dir.setIdLocalizacion(getIdDB("select seq_domicilio_localizacion.nextval as id_domicilio_localizacion from dual","id_domicilio_localizacion"));


				dir.setCodigoVia(getIdViaBD(dir.getTipoVia(),dir.getNombreVia(),idMunicipio));

				String codMunicipioDGC = findCodigoMunicipioDGCBD(dir);

				ps.setLong(1,dir.getIdLocalizacion());
				ps.setString(2,dir.getProvinciaINE());
				ps.setString(3,dir.getMunicipioINE());
				ps.setString(4,codMunicipioDGC);
				ps.setString(5,dir.getNombreEntidadMenor());

				ps.setLong(6,dir.getCodigoVia());
				ps.setLong(7, dir.getPrimerNumero());
				ps.setString(8, dir.getPrimeraLetra());
				ps.setLong(9,dir.getSegundoNumero());
				ps.setString(10, dir.getSegundaLetra());
				ps.setDouble(11, dir.getKilometro());
				ps.setString(12, dir.getBloque());
				ps.setString(13, dir.getEscalera());
				ps.setString(14, dir.getPlanta());
				ps.setString(15, dir.getPuerta());
				ps.setString(16, dir.getDireccionNoEstructurada());
				ps.setInt(17, Integer.valueOf(dir.getCodigoPostal()));
				ps.setString(18, dir.getNombreVia());
				ps.setLong(19, dir.getApartadoCorreos());
				ps.setString(20, dir.getTipoVia());
				ps.setString(21, dir.getNombreProvincia());
				ps.setString(22, dir.getNombreMunicipio());
				ps.execute();
				conn.commit();
			}
			catch (Exception e){
				throw e;
			}
			finally{
				try{ps.close();}catch(Exception e){};
				try{conn.close();}catch(Exception e){};
			}
		}
	}

	/**
	 * Funcion que rellena el atributo de entidad generadora del expediente pasado por parametro, con la entidad que
	 * busca en base de datos a partir del nombre o del id . El parametro porId, indica si la busqueda se realiza por el id, en
	 * caso de que no se quiera buscar por nombre. El parametro idEntGen es el identificador en caso de la busqueda por
	 * id.
	 *
	 * @param exp Expediente donde se inserta la entidad generadora buscada.
	 * @param nomEntGen Nombre de la entidad generadora buscada.
	 * @param porId indica si la busqueda es por id.
	 * @param idEntGen Id de la entidad generadora, por si la busqueda es por id.
	 * @throws Exception
	 */
	private void getEntidadGeneradoraBD(Expediente exp, String nomEntGen, boolean porId, int idEntGen) throws Exception {
		if((exp!=null) && ((nomEntGen!=null) && !nomEntGen.equals("")|| porId))
		{
			String sSQL;
			if(porId)
			{
				sSQL= "select * from Entidad_Generadora where id_entidad_generadora=" + idEntGen;
			}
			else
			{
				sSQL= "select * from Entidad_Generadora where nombre='" + nomEntGen + "'";
			}
			PreparedStatement ps= null;
			Connection conn= null;
			ResultSet rs= null;
			try
			{
				conn= CPoolDatabase.getConnection();
				ps= conn.prepareStatement(sSQL);
				rs= ps.executeQuery();
				if (rs.next())
				{
					EntidadGeneradora entGen= new EntidadGeneradora();
					entGen.setCodigo(TypeUtil.getSimpleInteger(rs, "Codigo"));
					entGen.setTipo(rs.getString("tipo"));
					entGen.setDescripcion(rs.getString("descripcion"));
					entGen.setNombre(rs.getString("nombre"));
					entGen.setIdEntidadGeneradora(TypeUtil.getSimpleLong(rs,"id_entidad_generadora"));
					exp.setEntidadGeneradora(entGen);
				}
			}
			catch (Exception e)
			{
				throw e;
			}
			finally
			{
				try{ps.close();}catch(Exception e){};
				try{rs.close();}catch(Exception e){};
				try{conn.close();}catch(Exception e){};
			}
		}
	}

	/**
	 * Inserta las referencias catastrales del expediente en la base de datos. Para ello inserta en la tabla
	 * expediente_finca_catastro el par idexpediente y refcatastral. Un par por cada referencia catastral.
	 *
	 * @param exp Expediente al que se le asocian las fincas.
	 * @param i La posicion en el arryList del objeto a insertar.
	 * @throws Exception
	 */
	private void insertaFincaCatastroExpedienteDB(Expediente exp, int i)throws Exception {
		if(exp!=null){
			String refFinca = null;
			String id_dialogo = "";
			boolean actualizado= false;

			if(exp.getListaReferencias().get(i) instanceof FincaCatastro){
				FincaCatastro finca = (FincaCatastro)exp.getListaReferencias().get(i);
				refFinca = finca.getRefFinca().getRefCatastral();
				id_dialogo = finca.getIdentificadorDialogo();
				actualizado = finca.isActualizadoOVC();
			}
			else if(exp.getListaReferencias().get(i) instanceof ReferenciaCatastral) {
				ReferenciaCatastral finca = (ReferenciaCatastral)exp.getListaReferencias().get(i);
				refFinca = finca.getRefCatastral();
			}

			FincaCatastro existeF = existeParcelaEnBD(refFinca);
			if(existeF!=null){
				String sSQL= "INSERT INTO expediente_finca_catastro (id_expediente,ref_catastral,id_dialogo, actualizado) VALUES (?,?,?,?)";
				PreparedStatement ps= null;
				Connection conn= null;

				try{
					conn= CPoolDatabase.getConnection();
					ps= conn.prepareStatement(sSQL);
					ps.setLong(1,exp.getIdExpediente());
					ps.setString(2,refFinca);
					ps.setString(3,id_dialogo);
					ps.setBoolean(4, actualizado);
					ps.execute();
					conn.commit();
				}
				catch (Exception e) {
					throw e;
				}
				finally{
					try{ps.close();}catch(Exception e){};
					try{conn.close();}catch(Exception e){};
				}
			}
		}
	}

	/**
	 * Inserta los id bien inmueble del expediente en la base de datos. Para ello inserta en la tabla
	 * expediente_bieninmueble el par idexpediente y id_bieninmueble. Un par por cada id bien inmueble.
	 *
	 * @param exp Expediente al que se le asocian las fincas.
	 * @param i La posicion en el arryList del objeto a insertar.
	 * @throws Exception
	 */    
	private void insertarBienInmuebleCatastroExpedienteBD(Expediente exp, int i) throws Exception {
		if(exp!=null){
			String idBI = null;
			String idDialogo = "";
			boolean actualizado = false;
			if(exp.getListaReferencias().get(i) instanceof BienInmuebleCatastro){
				BienInmuebleCatastro bien = (BienInmuebleCatastro)exp.getListaReferencias().get(i);
				idBI = bien.getIdBienInmueble().getIdBienInmueble();
				idDialogo= bien.getIdentificadorDialogo();
				actualizado = bien.isActualizadoOVC();
			}
			else if(exp.getListaReferencias().get(i) instanceof IdBienInmueble){
				IdBienInmueble bien = (IdBienInmueble)exp.getListaReferencias().get(i);
				idBI = bien.getIdBienInmueble();
			}
			else if(exp.getListaReferencias().get(i) instanceof BienInmuebleJuridico) {
				BienInmuebleJuridico bij = (BienInmuebleJuridico)exp.getListaReferencias().get(i);
				BienInmuebleCatastro bien = bij.getBienInmueble();
				idBI = bien.getIdBienInmueble().getIdBienInmueble();
				idDialogo= bien.getIdentificadorDialogo();
				actualizado = bien.isActualizadoOVC();
			}

			BienInmuebleCatastro bi = existeBIEnBD(idBI);
			if(bi!=null) {
				String sSQL= "INSERT INTO Expediente_BienInmueble(id_expediente,ID_BienInmueble,id_dialogo,actualizado) VALUES (?,?,?,?)";
				PreparedStatement ps= null;
				Connection conn= null;
				try {
					conn= CPoolDatabase.getConnection();
					ps= conn.prepareStatement(sSQL);
					ps.setLong(1,exp.getIdExpediente());
					ps.setString(2,idBI);
					ps.setString(3,idDialogo);
					ps.setBoolean(4,actualizado);
					ps.execute();
					conn.commit();
				}
				catch (Exception e) {
					throw e;
				}
				finally {
					try{ps.close();}catch(Exception e){};
					try{conn.close();}catch(Exception e){};
				}
			}
		}
	}

	/**
	 * Devuelve los expedientes no cerrados en base de datos de un usario. En caso de que se deseen los expedientes
	 * cerrados el valor del parametro expCerrado debe ser "true". En ese caso solo se devolveran los expedientes
	 * del usuario que ya hayan sido cerrados. El parametro idUsuario indica el id del usuario o la secuencia "admin",
	 * la cual indica que es un administrador y tiene permiso para ver todos los expediente, por lo que se devolveran
	 * todos los expediente no cerrados de la base de datos, o todos los cerrados, segun el valor del parametro expCerrado.
	 * Este metodo se encarga de llamar a los diferentes metodos para recoger los valores de los atributos de expediente,
	 * como direccion, entidad generadora, etc.
	 *
	 * @param idUsuario El id de Usuario que ha hecho la peticion.
	 * @param expCerrado Busca expedientes cerrados si el valor es true.
	 * @param convenio El convenio con el que se esta trabajando.
	 * @param idMunicipio El id del municipio con el que se esta trabajando.
	 * @return Collection ArrayList con los expedientes encontrados.
	 * @throws Exception
	 */
	private Collection getExpedientesUsuarioDB(String idUsuario, String expCerrado, String convenio, String idMunicipio, boolean isAcoplado) throws Exception {
		ArrayList expUsu =  null;
		Connection conn= CPoolDatabase.getConnection();

		if((idUsuario!=null)&&(!idUsuario.equals(""))){

			expUsu = new ArrayList();
			String sSQL = "select * from expediente where id_municipio = " +idMunicipio + " and modo_acoplado = " +isAcoplado + " and ";

			if(expCerrado.equalsIgnoreCase("true")){
				if(CPoolDatabase.isPostgres(conn))
					sSQL= sSQL + "(fecha_de_cierre is not null or fecha_de_cierre<"+ new java.sql.Date(System.currentTimeMillis()) + ")";
				else
					sSQL= sSQL + "(fecha_de_cierre is not null or to_char(fecha_de_cierre,'yyyy-mm-dd')<'"+ new java.sql.Date(System.currentTimeMillis()) + "')";
			}
			else{
				if(CPoolDatabase.isPostgres(conn))
					sSQL= sSQL + "(fecha_de_cierre is null or fecha_de_cierre>="+ new java.sql.Date(System.currentTimeMillis()) + ")";
				else
					sSQL= sSQL + "(fecha_de_cierre is null or to_char(fecha_de_cierre,'yyyy-mm-dd')>='"+ new java.sql.Date(System.currentTimeMillis()) + "')";
			}

			if(!idUsuario.equalsIgnoreCase("admin")){
				sSQL= sSQL +" and id_tecnico_catastro=" + idUsuario;
			}
			sSQL = sSQL + " order by numero_expediente";

			PreparedStatement ps= null;
			ResultSet rs= null;
			try
			{

				ps= conn.prepareStatement(sSQL);
				rs= ps.executeQuery();
				while (rs.next())
				{
					Expediente exp= new Expediente();
					exp.setIdExpediente(rs.getLong("id_expediente"));
					int idEntidadGeneradora =rs.getInt("id_entidad_generadora");
					getEntidadGeneradoraBD(exp,null, true,idEntidadGeneradora);
					exp.setIdEstado(rs.getLong("id_estado"));
					exp.setIdTecnicoCatastro(rs.getString("id_tecnico_catastro"));
					int idLocalizacion=rs.getInt("id_localizacion");
					getDireccionExpUsuarioBD(exp,idLocalizacion);
					exp.setIdMunicipio(rs.getInt("id_municipio"));
					exp.setNumeroExpediente(rs.getString("numero_expediente"));
					exp.setTipoExpediente((TipoExpediente)((ArrayList)getTiposExpedientesBD(null,rs.getInt("Id_Tipo_expediente"))).get(0));
					exp.setFechaAlteracion((java.util.Date)rs.getDate("fecha_alteracion"));

					int anoExpGerencia = TypeUtil.getSimpleInteger(rs, "anio_expediente_gerencia");
					exp.setAnnoExpedienteGerencia( anoExpGerencia != -1? new Integer(anoExpGerencia): null);

					exp.setReferenciaExpedienteGerencia(rs.getString("referencia_expediente_gerencia"));

					if(CPoolDatabase.isPostgres(conn)){
						int codEntidadRegDGCOrigAlteracion = TypeUtil.getSimpleInteger(rs,"codigo_entidad_registro_dgc_origen_alteracion");
						exp.setCodigoEntidadRegistroDGCOrigenAlteracion( codEntidadRegDGCOrigAlteracion!= -1? new Integer(codEntidadRegDGCOrigAlteracion) : null);
						exp.setAnnoExpedienteAdminOrigenAlteracion(TypeUtil.getSimpleInteger(rs,"anio_expediente_admin_origen_alteracion"));
						exp.setReferenciaExpedienteAdminOrigen(rs.getString("referencia_expediente_admin_origen"));
						exp.setTipoDocumentoOrigenAlteracion(rs.getString("tipo_documento_origen_alteracion"));
						exp.setInfoDocumentoOrigenAlteracion(rs.getString("info_documento_origen_alteracion"));
					}
					else{
						int codEntidadRegDGCOrigAlteracion = TypeUtil.getSimpleInteger(rs,"codent_reg_dgc_orig_alteracion");
						exp.setCodigoEntidadRegistroDGCOrigenAlteracion( codEntidadRegDGCOrigAlteracion!= -1? new Integer(codEntidadRegDGCOrigAlteracion) : null);
						exp.setAnnoExpedienteAdminOrigenAlteracion(TypeUtil.getSimpleInteger(rs,"anio_exp_admin_orig_alteracion"));
						exp.setReferenciaExpedienteAdminOrigen(rs.getString("referencia_exp_admin_origen"));
						exp.setTipoDocumentoOrigenAlteracion(rs.getString("tipo_documento_orig_alteracion"));
						exp.setInfoDocumentoOrigenAlteracion(rs.getString("info_documento_orig_alteracion"));
					}

					exp.setFechaRegistro((java.util.Date)rs.getDate("fecha_registro"));
					exp.setFechaMovimiento((java.util.Date)rs.getDate("fecha_movimiento"));
					Time horaMov = rs.getTime("hora_movimiento");
					String horaGene=null;
					if(horaMov!=null)
					{
						SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss");
						horaGene = horaFormat.format(horaMov);
					}
					exp.setHoraMovimiento(horaGene);
					exp.setFechaDeCierre(rs.getDate("fecha_de_cierre"));
					exp.setCodigoDescriptivoAlteracion(rs.getString("codigo_descriptivo_alteracion"));
					exp.setDescripcionAlteracion(rs.getString("descripcion_alteracion"));
					exp.setNifPresentador(rs.getString("nif_presentador"));
					exp.setNombreCompletoPresentador(rs.getString("nombre_completo_presentador"));
					exp.setNumBienesInmueblesUrbanos(TypeUtil.getSimpleInteger(rs,"num_bienesinmuebles_urb"));
					exp.setNumBienesInmueblesRusticos(TypeUtil.getSimpleInteger(rs,"num_bienesinmuebles_rus"));
					exp.setNumBienesInmueblesCaractEsp(TypeUtil.getSimpleInteger(rs,"num_bienesinmuebles_esp"));
					exp.setCodProvinciaNotaria(rs.getString("Cod_Provincia_Notaria"));
					exp.setCodPoblacionNotaria(rs.getString("Cod_Poblacion_Notaria"));
					exp.setCodNotaria(rs.getString("Cod_Notaria"));

					exp.setAnnoProtocoloNotarial(rs.getString("Anio_protocolo_Notarial"));

					exp.setProtocoloNotarial(rs.getString("Protocolo_notarial"));
					if(convenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
					{
						getBienInmuebleCatastroExpedienteDB(exp, idMunicipio);
					}
					else if(convenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO) &&
							exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
					{
						getFincaCatastroExpedienteDB(exp, idMunicipio);
					}
					else if(convenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO) &&
							exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
					{
						getBienInmuebleCatastroExpedienteDB(exp, idMunicipio);
					}
					expUsu.add(exp);
				}
			}
			catch (Exception e)
			{
				throw e;
			}
			finally
			{
				try{ps.close();}catch(Exception e){};
				try{rs.close();}catch(Exception e){};
				try{conn.close();}catch(Exception e){};
			}
		}
		return expUsu;
	}

	/**
	 * Este metedo accede a base de datos y recoge la direccion que tenga el identificador idLocalizacion pasado por
	 * parametro. El objeto Direccion creado se asigna al atributo Direccion del expediente pasado por parametro.
	 *
	 * @param exp Expediente donde se introduce la direccion buscada.
	 * @param idLocalizacion El id de la direccion buscada.
	 * @throws Exception
	 */
	private void getDireccionExpUsuarioBD(Expediente exp, int idLocalizacion) throws Exception
	{
		String sSQL= "select * from domicilio_localizacion where id_localizacion=" + idLocalizacion;
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			if(rs.next())
			{
				DireccionLocalizacion dir= new DireccionLocalizacion();
				dir.setIdLocalizacion(TypeUtil.getSimpleLong(rs, "id_localizacion"));
				dir.setProvinciaINE(rs.getString("provincia_ine"));
				dir.setMunicipioINE(rs.getString("municipio_ine"));
				dir.setCodigoMunicipioDGC(rs.getString("codigo_municipiodgc"));
				dir.setNombreEntidadMenor(rs.getString("nombre_entidad_menor"));
				dir.setCodigoVia(TypeUtil.getSimpleInteger(rs,"codigo_via"));
				dir.setPrimerNumero(TypeUtil.getSimpleInteger(rs,"primer_numero"));
				dir.setPrimeraLetra(rs.getString("primera_letra"));
				dir.setSegundoNumero(TypeUtil.getSimpleInteger(rs,"segundo_numero"));
				dir.setSegundaLetra(rs.getString("segunda_letra"));
				dir.setKilometro(TypeUtil.getSimpleDouble(rs, "kilometro"));
				dir.setBloque(rs.getString("bloque"));
				dir.setEscalera(rs.getString("escalera"));
				dir.setPlanta(rs.getString("planta"));
				dir.setPuerta(rs.getString("puerta"));
				dir.setDireccionNoEstructurada(rs.getString("direccion_no_estructurada"));


				int valorCodigoPostal=TypeUtil.getSimpleInteger(rs,"codigo_postal");
				if(valorCodigoPostal!=-1)
					dir.setCodigoPostal(String.valueOf(valorCodigoPostal));

				dir.setNombreVia(rs.getString("nombre_via"));

				String apartadoCorreos=rs.getString("apartado_correos");
				if(apartadoCorreos==null)
					dir.setApartadoCorreos(-1);
				else
					dir.setApartadoCorreos(Integer.parseInt(apartadoCorreos));

				dir.setTipoVia(rs.getString("tipo_via"));
				dir.setNombreProvincia(rs.getString("nombre_provincia"));
				dir.setNombreMunicipio(rs.getString("nombre_municipio"));
				exp.setDireccionPresentador(dir);
			}      
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	/**
	 *  Metodo que accede a base de datos para recoger los identificadores y los nombres de los usuarios que tienen
	 * expedientes. Devuelve una hash con el identificador como clave y el nombre del usuario como valor.
	 *
	 * @return Hashtable La tabla con los usuarios, la key es el id y el value es el name.
	 * @throws Exception
	 */
	private Hashtable getUsuariosConExpedienteBD() throws Exception
	{
		Hashtable aux= new Hashtable();
		String sSQL= "select e.id_tecnico_catastro,iu.name from expediente e, iuseruserhdr iu where " +
		" e.id_tecnico_catastro= iu.id group by name, id_tecnico_catastro";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				aux.put(rs.getString("id_tecnico_catastro"), rs.getString("name"));
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return aux;
	}

	/**
	 * Metodo que devuelve un ArrayList con los codigos de todas las entidades generadoras.
	 *
	 * @return Collection ArrayList con los codigos de las entidades generadoras.
	 * @throws Exception
	 */
	private Collection getCodigoEntidadGeneradoraBD() throws Exception
	{
		ArrayList aux= new ArrayList();
		String sSQL= "select codigo from entidad_generadora";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				aux.add(String.valueOf(rs.getInt("codigo")));
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return aux;
	}

	/**
	 * Metodo que devuelve un ArrayList con los codigos de todos los estados.
	 *
	 * @return Collection ArrayList con los codigos de los estados.
	 * @throws Exception
	 */
	private Collection getCodigoEstadosBD() throws Exception
	{
		ArrayList aux= new ArrayList();
		String sSQL= "select id_estado from estado_expediente";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				aux.add(String.valueOf(rs.getInt("id_estado")));
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return aux;
	}

	/**
	 * Metodo que obtiene de base de datos las referencias a parcelas de un expediente pasado por parametro y las
	 * asigna al atributo listaReferencias del expediente.
	 *
	 * @param exp El expediente al que se va a asociar
	 * @param idMunicipio El id del municipio con el que se esta trabajando.
	 * @throws Exception
	 * */
	private void getFincaCatastroExpedienteDB(Expediente exp, String idMunicipio)throws Exception
	{
		ArrayList aux= new ArrayList();
		
		String sSQL = "select e.ref_catastral ,e.id_dialogo,e.actualizado, p.codigopoligono, p.codigoparcela, p.id_via,p.primer_numero," +
		" p.codigo_municipioDGC, p.codigodelegacionmeh from expediente_finca_catastro e left join parcelas p " +
				"on p.referencia_catastral=e.ref_catastral and p.fecha_baja is null where e.id_expediente=" 
				+ exp.getIdExpediente() + " and p.id_municipio='" + idMunicipio + "'";
		
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				FincaCatastro finca = new FincaCatastro();
				ReferenciaCatastral refCatas = new ReferenciaCatastral(rs.getString("ref_catastral"));
				if(rs.getString("id_dialogo") == null ){
					finca.setIdentificadorDialogo("");
				}
				else{
					finca.setIdentificadorDialogo(rs.getString("id_dialogo"));
				}
				finca.setActualizadoOVC(rs.getBoolean("actualizado"));
				
				finca.setRefFinca(refCatas);
				DireccionLocalizacion dir = new DireccionLocalizacion();
				dir.setPrimerNumero(TypeUtil.getSimpleInteger(rs, "primer_numero"));
				dir.setCodPoligono(rs.getString("codigopoligono"));
				dir.setCodParcela(rs.getString("codigoparcela"));

				String codMunicipioDGC = rs.getString("codigo_municipioDGC");
				if(codMunicipioDGC != null && !codMunicipioDGC.equalsIgnoreCase(""))
					finca.setCodMunicipioDGC(codMunicipioDGC);
				else
					finca.setCodMunicipioDGC(DEFAULT_CODIGO_MUNICIPIO_DGC);

				String codDelegacionMEH = rs.getString("codigodelegacionmeh");
				if(codDelegacionMEH == null || codDelegacionMEH.equalsIgnoreCase(""))
					codDelegacionMEH = String.valueOf(exp.getEntidadGeneradora().getCodigo());

				finca.setCodDelegacionMEH(codDelegacionMEH);

				int idVia = -1;
				idVia = TypeUtil.getSimpleInteger(rs,"id_via");
				ResultSet rsVia= null;
				try
				{
					if(idVia!=-1){
						sSQL = "select vias.tipovianormalizadocatastro, vias.nombrecatastro from vias where codigocatastro=" + idVia
						+ " and id_municipio="+Integer.parseInt(idMunicipio);;
						ps= conn.prepareStatement(sSQL);
						rsVia= ps.executeQuery();
						if(rsVia.next())
						{
							dir.setNombreVia(rsVia.getString("nombrecatastro"));
							dir.setTipoVia(rsVia.getString("tipovianormalizadocatastro"));
						}
					}
				}
				catch (Exception e)
				{
					throw e;
				}
				finally
				{
					try{rsVia.close();}catch(Exception e){};
				}
				finca.setDirParcela(dir);
				aux.add(finca);
			}

			ResultSet rsParcelTemp= null;
			try {

				String SQLParcelasTemp = "select referencia from catastro_temporal where id_expediente =" + exp.getIdExpediente() + " and referencia not in " +
				"(select e.ref_catastral from expediente_finca_catastro e left join parcelas p " +
				"on p.referencia_catastral=e.ref_catastral and p.fecha_baja is null where e.id_expediente=" 
				+ exp.getIdExpediente() + " and p.id_municipio='" + idMunicipio + "')";
				
				ps= conn.prepareStatement(SQLParcelasTemp);
				rsParcelTemp= ps.executeQuery();
				if(rsParcelTemp.next()) {
					FincaCatastro finca = new FincaCatastro();
					ReferenciaCatastral refCatas = new ReferenciaCatastral(rsParcelTemp.getString("referencia"));
					finca.setRefFinca(refCatas);
					
					DireccionLocalizacion dir = new DireccionLocalizacion();
					finca.setDirParcela(dir);
					exp.getDireccionPresentador().setCodigoMunicipioDGC(finca.getCodMunicipioDGC());
					aux.add(finca);
				}
			}
			catch (Exception e){
				throw e;
			}
			finally{
				try{rsParcelTemp.close();}catch(Exception e){};
			}

			exp.setListaReferencias(aux);            
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	/**
	 * Metodo que obtiene de base de datos las referencias a bienes inmuebles de un expediente pasado por parametro y las
	 * asigna al atributo listaReferencias del expediente.
	 *
	 * @param exp El expediente al que se va a asociar
	 * @param idMunicipio El id del municipio con el que se esta trabajando.
	 * @throws Exception
	 * */
	private void getBienInmuebleCatastroExpedienteDB(Expediente exp, String idMunicipio)throws Exception
	{
		ArrayList aux= new ArrayList();
		String sSQL= "select e.id_bieninmueble, e.id_dialogo,e.actualizado, b.poligono_rustico, b.parcela, b.id_via,b.primer_numero, " +
		" b.codigo_municipiodgc, b.clase_bieninmueble, p.codigodelegacionmeh from " +
		" Expediente_BienInmueble e,Bien_Inmueble b, parcelas p where e.id_expediente=" +exp.getIdExpediente()+
		" and e.id_BienInmueble=b.IDentificador and p.referencia_catastral=b.parcela_catastral and" +
		" p.id_municipio=" +
		idMunicipio;
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				BienInmuebleCatastro bien = new BienInmuebleCatastro();
				IdBienInmueble idBI = new IdBienInmueble();
				idBI.setIdBienInmueble(rs.getString("id_bieninmueble"));
				bien.setIdBienInmueble(idBI);

				DireccionLocalizacion dir = new DireccionLocalizacion();
				dir.setPrimerNumero(TypeUtil.getSimpleInteger(rs, "primer_numero"));
				
				String codDelegacionMEH = rs.getString("codigodelegacionmeh");
				if(codDelegacionMEH == null || codDelegacionMEH.equalsIgnoreCase(""))
					codDelegacionMEH = String.valueOf(exp.getEntidadGeneradora().getCodigo());

				dir.setProvinciaINE(codDelegacionMEH);
				
				dir.setCodPoligono(rs.getString("poligono_rustico"));
				dir.setCodParcela(rs.getString("parcela"));
				bien.setCodMunicipioDGC(rs.getString("codigo_municipiodgc"));
				bien.setClaseBienInmueble(rs.getString("clase_bieninmueble"));
				if(rs.getString("id_dialogo") == null ){
					bien.setIdentificadorDialogo("");
				}
				else{
					bien.setIdentificadorDialogo(rs.getString("id_dialogo"));
				}
				bien.setActualizadoOVC(rs.getBoolean("actualizado"));
				int idVia = -1;
				idVia = TypeUtil.getSimpleInteger(rs,"id_via");
				ResultSet rsVia= null;
				try
				{
					if(idVia!=-1){
						sSQL = "select vias.tipovianormalizadocatastro, vias.nombrecatastro from vias where codigocatastro=" + idVia
						+ " and id_municipio="+Integer.parseInt(idMunicipio);;
						ps= conn.prepareStatement(sSQL);
						rsVia= ps.executeQuery();
						if(rsVia.next())
						{
							dir.setNombreVia(rsVia.getString("nombrecatastro"));
							dir.setTipoVia(rsVia.getString("tipovianormalizadocatastro"));
						}
					}
				}catch (Exception e)
				{
					throw e;
				}
				finally
				{
					try{rsVia.close();}catch(Exception e){};
				}
				bien.setDomicilioTributario(dir);
				aux.add(bien);
			}
			exp.setListaReferencias(aux);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	/**
	 * Metodo que devuelve un ArrayList con objetos FincaCatastro correspondientes a las referencias catastrales pasadas
	 * por parametro en el arryaList refCatastrales.
	 *
	 * @param refCatastrales Las referencias catastrales de las parcelas.
	 * @param idMunicipio El id del municipio con el que se esta trabajando.
	 * @return Collection El ArrayList resultado.
	 * @throws Exception
	 * */
	private Collection getFincaCatastroPorRefCatastralDB(ArrayList refCatastrales, String idMunicipio) throws Exception
	{
		ArrayList aux= new ArrayList();
		String sSQLBase= "select p.id_via, p.primer_numero,p.codigopoligono, p.codigoparcela from parcelas p" +
		" where p.id_municipio="+ idMunicipio+" and p.referencia_catastral='";

		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			for(int i= 0;i<refCatastrales.size();i++)
			{
				String sSQL = sSQLBase + refCatastrales.get(i) + "'";
				ps= conn.prepareStatement(sSQL);
				rs= ps.executeQuery();
				while(rs.next())
				{
					FincaCatastro finca = new FincaCatastro();
					ReferenciaCatastral refCatas = new ReferenciaCatastral((String)refCatastrales.get(i));
					finca.setRefFinca(refCatas);
					DireccionLocalizacion dir = new DireccionLocalizacion();
					dir.setCodPoligono(rs.getString("codigopoligono"));
					dir.setCodParcela(rs.getString("codigoparcela"));
					dir.setPrimerNumero(TypeUtil.getSimpleInteger(rs, "primer_numero"));
					int idVia = -1;
					idVia = TypeUtil.getSimpleInteger(rs,"id_via");
					ResultSet rsVia= null;
					try
					{
						if(idVia!=-1){
							sSQL = "select vias.tipovianormalizadocatastro, vias.nombrecatastro from vias where codigocatastro=" + idVia
							+ " and id_municipio="+Integer.parseInt(idMunicipio);;
							ps= conn.prepareStatement(sSQL);
							rsVia= ps.executeQuery();
							if(rsVia.next())
							{
								dir.setNombreVia(rsVia.getString("nombrecatastro"));
								dir.setTipoVia(rsVia.getString("tipovianormalizadocatastro"));
							}
						}
					}
					catch (Exception e)
					{
						throw e;
					}
					finally
					{
						try{rsVia.close();}catch(Exception e){};
					}
					finca.setDirParcela(dir);
					aux.add(finca);
				}
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return aux;
	}

	/**
	 * Metodo que busca todas las referencias catastrales en parcelas que coincidan con el patron pasado por parametro.
	 *
	 * @param idMunicipio El id del municipio con el que se esta trabajando.
	 * @param patron El patron de busca de la referencia catastral.
	 * @return Collection ArrayList con objetos FincaCatastro resultado.
	 * @throws Exception
	 * */
	private Collection getFincaCatastroBuscadasDB(String idMunicipio,String patron) throws Exception
	{
		ArrayList aux= new ArrayList();
		String sSQL= "select distinct parcelas.referencia_catastral, parcelas.primer_numero, parcelas.codigopoligono," +
		" parcelas.codigoparcela, parcelas.id_via from parcelas where" +
		" parcelas.referencia_catastral IS NOT NULL and parcelas.id_municipio= '"+ idMunicipio+"' "
		+" and ((parcelas.referencia_catastral like upper('%" + patron + "%')) or ( parcelas.referencia_catastral is null)) "
		+ " order by parcelas.referencia_catastral asc";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				FincaCatastro finca = new FincaCatastro();
				ReferenciaCatastral refCatas = new ReferenciaCatastral(rs.getString("referencia_catastral"));
				finca.setRefFinca(refCatas);
				DireccionLocalizacion dir = new DireccionLocalizacion();
				dir.setCodParcela(rs.getString("codigoparcela"));
				dir.setCodPoligono(rs.getString("codigopoligono"));
				dir.setPrimerNumero(TypeUtil.getSimpleInteger(rs,"primer_numero"));
				int idVia = -1;
				idVia = TypeUtil.getSimpleInteger(rs,"id_via");
				ResultSet rsVia= null;                
				try
				{
					if(idVia!=-1){
						sSQL = "select vias.tipovianormalizadocatastro, vias.nombrecatastro from vias where codigocatastro=" + idVia
						+ " and id_municipio="+Integer.parseInt(idMunicipio);
						ps= conn.prepareStatement(sSQL);
						rsVia= ps.executeQuery();
						if(rsVia.next())
						{
							dir.setNombreVia(rsVia.getString("nombrecatastro"));
							dir.setTipoVia(rsVia.getString("tipovianormalizadocatastro"));
						}
					}
				}
				catch (Exception e)
				{
					throw e;
				}
				finally
				{
					try{rsVia.close();}catch(Exception e){};
				}
				finca.setDirParcela(dir);
				aux.add(finca);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return aux;
	}

	/**
	 * Metodo que busca todas las referencias catastrales en bien_inmueble relacionado con parcelas que coincidan
	 * con el patron pasado por parametro.
	 *
	 * @param idMunicipio El id del municipio con el que se esta trabajando.
	 * @param patron El patron de busca de la referencia catastral.
	 * @return Collection ArrayList con objetos BienInmueble resultado.
	 * @throws Exception
	 * */
	private Collection getBienInmuebleCatastroBuscadosBD(String idMunicipio,String patron) throws Exception
	{
		ArrayList aux= new ArrayList();
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			String sSQL;
			conn= CPoolDatabase.getConnection();
			if(CPoolDatabase.isPostgres(conn))
			{
				sSQL ="select distinct bien_inmueble.identificador, bien_inmueble.poligono_rustico, bien_inmueble.parcela, bien_inmueble.primer_numero, "+
				" bien_inmueble.id_via , bien_inmueble.clase_bieninmueble"+
				" from bien_inmueble, parcelas where parcelas.referencia_catastral "+
				" IS NOT NULL and parcelas.id_municipio='"+ idMunicipio+"' " +
				" and ((parcelas.referencia_catastral like upper('%" + patron + "%')) or ( parcelas.referencia_catastral is null)) " +
				" and bien_inmueble.identificador like  parcelas.referencia_catastral || '%' " +
				" order by bien_inmueble.identificador asc";
			}
			else
			{
				sSQL ="select distinct bien_inmueble.identificador, bien_inmueble.poligono_rustico, bien_inmueble.parcela, bien_inmueble.primer_numero, "+
				" bien_inmueble.id_via,  bien_inmueble.clase_bieninmueble"+
				" from bien_inmueble, parcelas where parcelas.referencia_catastral "+
				" IS NOT NULL and parcelas.id_municipio='"+ idMunicipio+"' " +
				" and ((parcelas.referencia_catastral like upper('%" + patron + "%')) or ( parcelas.referencia_catastral is null)) " +
				" and bien_inmueble.identificador like concat(parcelas.referencia_catastral,'%') " +
				" order by bien_inmueble.identificador asc";
			}
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				BienInmuebleCatastro bien = new BienInmuebleCatastro();
				IdBienInmueble idBI = new IdBienInmueble();
				idBI.setIdBienInmueble(rs.getString("identificador"));
				bien.setIdBienInmueble(idBI);
				bien.setClaseBienInmueble(rs.getString("clase_bieninmueble"));

				DireccionLocalizacion dir = new DireccionLocalizacion();
				dir.setCodParcela(rs.getString("parcela"));
				dir.setCodPoligono(rs.getString("poligono_rustico"));
				dir.setPrimerNumero(TypeUtil.getSimpleInteger(rs, "primer_numero"));
				int idVia = -1;
				idVia = TypeUtil.getSimpleInteger(rs,"id_via");
				ResultSet rsVia= null;
				try
				{
					if(idVia!=-1){	
						sSQL = "select vias.tipovianormalizadocatastro, vias.nombrecatastro from vias where codigocatastro=" + idVia
						+ " and id_municipio="+Integer.parseInt(idMunicipio);;
						ps= conn.prepareStatement(sSQL);
						rsVia= ps.executeQuery();
						if(rsVia.next())
						{
							dir.setNombreVia(rsVia.getString("nombrecatastro"));
							dir.setTipoVia(rsVia.getString("tipovianormalizadocatastro"));
						}
					}

				}catch (Exception e)
				{
					throw e;
				}
				finally
				{
					try{rsVia.close();}catch(Exception e){};
				}
				bien.setDomicilioTributario(dir);
				aux.add(bien);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return aux;
	}

	/**
	 * Metodo que busca todas las referencias catastrales en parcelas que coincidan en la via y el tipo de via
	 * con el patron de via y tipo via pasado por parametro.
	 *
	 * @param idMunicipio El id del municipio con el que se esta trabajando.
	 * @param patronNombreVia El patron del nombre de la via.
	 * @param patronTipoVia El patron del tipo de la via.
	 * @return Collection ArrayList con objetos FincaCatastro resultado.
	 * @throws Exception
	 * */
	private Collection getFincaCatastroBuscadasPorDirDB(String idMunicipio,String patronNombreVia, String patronTipoVia) throws Exception
	{
		ArrayList aux= new ArrayList();
		String sSQL= "select distinct parcelas.referencia_catastral as ID, vias.tipovianormalizadocatastro, vias.nombrecatastro, " +
		" parcelas.primer_numero, parcelas.codigopoligono, parcelas.codigoparcela from parcelas LEFT JOIN " +
		" vias ON parcelas.id_via=vias.codigocatastro where" +
		" parcelas.referencia_catastral IS NOT NULL and parcelas.id_municipio= '"+ idMunicipio+"' "
		+ " and ((vias.nombrecatastro like upper('%" + patronNombreVia + "%')) "
		+ " and (vias.tipovianormalizadocatastro like upper('%" + patronTipoVia + "%'))) "
		+ " and vias.id_municipio='"+idMunicipio+"'"  
		+ " order by vias.nombrecatastro asc";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				FincaCatastro finca = new FincaCatastro();
				ReferenciaCatastral refCatas = new ReferenciaCatastral(rs.getString("id"));
				finca.setRefFinca(refCatas);
				DireccionLocalizacion dir = new DireccionLocalizacion();
				dir.setNombreVia(rs.getString("nombrecatastro"));
				dir.setTipoVia(rs.getString("tipovianormalizadocatastro"));
				dir.setPrimerNumero(TypeUtil.getSimpleInteger(rs, "primer_numero"));
				dir.setCodParcela(rs.getString("codigoparcela"));
				dir.setCodPoligono(rs.getString("codigopoligono"));
				finca.setDirParcela(dir);
				aux.add(finca);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return aux;
	}

	/**
	 * Metodo que busca todas las referencias catastrales en bien_inmueble que coincidan en la via y el tipo de via
	 * con el patron de via y tipo via pasado por parametro.
	 *
	 * @param idMunicipio El id del municipio con el que se esta trabajando.
	 * @param patronNombreVia El patron del nombre de la via.
	 * @param patronTipoVia El patron del tipo de la via.
	 * @return Collection ArrayList con objetos BienInmueble resultado.
	 * @throws Exception
	 * */
	private Collection getBienInmuebleCatastroBuscadosPorDirDB(String idMunicipio,String patronNombreVia, String patronTipoVia) throws Exception
	{
		ArrayList aux= new ArrayList();
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			String sSQL;
			conn= CPoolDatabase.getConnection();
			if(CPoolDatabase.isPostgres(conn))
			{
				sSQL =" select distinct bien_inmueble.identificador, vias.tipovianormalizadocatastro, vias.nombrecatastro, bien_inmueble.primer_numero, " +
				" bien_inmueble.parcela, bien_inmueble.poligono_rustico  " +
				" from bien_inmueble,vias, parcelas where bien_inmueble.id_via=vias.codigocatastro and parcelas.referencia_catastral " +
				" IS NOT NULL and parcelas.id_municipio='"+ idMunicipio+"' " +
				" and ((vias.nombrecatastro like upper('%" + patronNombreVia + "%')) " +
				" and (vias.tipovianormalizadocatastro like upper('%" + patronTipoVia + "%'))) " +
				" and vias.id_municipio='"+idMunicipio+"'" +
				" and bien_inmueble.identificador like  parcelas.referencia_catastral || '%' " +
				" order by vias.nombrecatastro asc";
			}
			else
			{
				sSQL =" select distinct bien_inmueble.identificador, vias.tipovianormalizadocatastro, vias.nombrecatastro, bien_inmueble.primer_numero, " +
				" bien_inmueble.parcela, bien_inmueble.poligono_rustico  " +
				" from bien_inmueble,vias, parcelas where bien_inmueble.id_via=vias.codigocatastro and parcelas.referencia_catastral " +
				" IS NOT NULL and parcelas.id_municipio='"+ idMunicipio+"' " +
				" and ((vias.nombrecatastro like upper('%" + patronNombreVia + "%')) " +
				" and (vias.tipovianormalizadocatastro like upper('%" + patronTipoVia + "%'))) " +
				" and vias.id_municipio='"+idMunicipio+"'" +                        
				" and bien_inmueble.identificador like concat(parcelas.referencia_catastral,'%') " +
				" order by vias.nombrecatastro asc";
			}
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				BienInmuebleCatastro bien = new BienInmuebleCatastro();
				IdBienInmueble idBI = new IdBienInmueble();
				idBI.setIdBienInmueble(rs.getString("identificador"));
				bien.setIdBienInmueble(idBI);

				DireccionLocalizacion dir = new DireccionLocalizacion();
				dir.setTipoVia(rs.getString("tipovianormalizadocatastro"));
				dir.setNombreVia(rs.getString("nombrecatastro"));
				dir.setPrimerNumero(TypeUtil.getSimpleInteger(rs, "primer_numero"));
				dir.setCodParcela(rs.getString("parcela"));
				dir.setCodPoligono(rs.getString("poligono_rustico"));
				bien.setDomicilioTributario(dir);
				aux.add(bien);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return aux;
	}

	/**
	 * Metodo que devuelve un array de objetos EstadoSiguiente obtenidos de la base de datos, para poder hacer las
	 * transiciones entre estados segun las acciones.
	 *
	 * @return Collection El ArrayList con los estados siguientes.
	 * @throws Exception
	 * */
	private Collection getEstadoSiguienteDB() throws Exception
	{
		ArrayList aux= new ArrayList();
		String sSQL= "select * from estado_siguiente";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				EstadoSiguiente auxEst = new EstadoSiguiente();
				auxEst.setEstadoActual(TypeUtil.getSimpleLong(rs, "id_estado"));
				auxEst.setEstadoSiguiente(TypeUtil.getSimpleLong(rs,"id_estado_siguiente"));
				auxEst.setModo(rs.getString("modo"));
				aux.add(auxEst);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return aux;
	}

	/**
	 * Metodo que devuelve un id. Se pasa por parametro la sentencia sql de la secuencia y el objeto a recibir y
	 * devuelve el id.
	 *
	 * @param idSql La sentencia sql de la secuencia.
	 * @param idReturn El atributo a obtener.
	 * @return long El id generado.
	 * @throws Exception
	 * */
	private long getIdDB(String idSql, String idReturn)throws Exception
	{
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		long id=-1;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps=conn.prepareStatement(idSql);
			rs=ps.executeQuery();
			if (rs.next())
			{
				id= rs.getLong(idReturn);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return id;
	}

	/**
	 * Metodo que devuelve el id de una via, buscada en la tabla vias. Para seleccionar la via se busca comparando con
	 * el municipio, el nombre de la via y el tipo de la via.
	 *
	 * @param tipoVia El tipo de la via a comparar.
	 * @param nombreVia El nombre de la via a comparar.
	 * @param idMunicipio El id del municipio con el que se esta trabajando.
	 * @return int El id de la via, si es -1 es que no se ha encontrado.
	 * @throws Exception
	 * */
	private int getIdViaBD(String tipoVia, String nombreVia, long idMunicipio)throws Exception{

		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;

		String query = "select codigoine from vias where tipovianormalizadocatastro='"+tipoVia.toUpperCase() +
		"' and nombreviaine='"+ nombreVia.toUpperCase()+ "' and id_municipio="+ idMunicipio;

		try {
			conn= CPoolDatabase.getConnection();
			ps=conn.prepareStatement(query);
			rs=ps.executeQuery();

			if (rs.next())
				return TypeUtil.getSimpleInteger(rs, "codigoine");
			else
				return -1;
			//throw new ACException("El nombre de la Via y el tipo no se encuentran en base de datos");

		}
		catch (Exception e){
			throw e;
		}
		finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	/**
	 * Metodo que comprueba si ya existe un expediente con ese numero en base de datos.
	 *
	 * @param numeroExp Numero de expediente a comparar.
	 * @return boolean Boolean indicando si existe ya un expediente con ese numero o no.
	 * @throws Exception
	 * */
	private boolean existeExpediente(String numeroExp) throws Exception
	{
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		boolean existe = false;
		String query = "select numero_expediente from expediente where numero_expediente='"+numeroExp + "'";
		try
		{
			conn= CPoolDatabase.getConnection();
			ps=conn.prepareStatement(query);
			rs=ps.executeQuery();
			if (rs.next())
			{
				existe=true;
				throw new ACException("Catastro.RegistroExpedientes.MainCatastro.msgExpedienteDuplicado");
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return existe;
	}

	/**
	 * Metodo que elimina todas las referencias de finca o bien inmueble de las tablas expediente_finca_catastro y
	 * expediente_bieninmueble del expediente pasado por parametro e introduce las nuevas referencias que el expediente
	 * tenga asociadas.
	 *
	 * @param exp Expediente a actualizar.
	 * @throws Exception
	 * */
	private void updateReferenciasExpedienteBD(Expediente exp) throws Exception {
		PreparedStatement ps= null;
		Connection conn= null;

		try {

			String queryUpdate= null;
			if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
				queryUpdate = "delete from expediente_finca_catastro where id_expediente="+exp.getIdExpediente();
			else if(exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
				queryUpdate = "delete from expediente_bienInmueble where id_expediente="+exp.getIdExpediente();

			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(queryUpdate);
			ps.execute();
			conn.commit();

			for(int i = 0; i<exp.getListaReferencias().size();i++) {

				if(exp.getListaReferencias().get(i) instanceof FincaCatastro)
					insertaFincaCatastroExpedienteDB(exp, i);
				else if(exp.getListaReferencias().get(i) instanceof BienInmuebleCatastro)
					insertarBienInmuebleCatastroExpedienteBD(exp, i);
				else if(exp.getListaReferencias().get(i) instanceof BienInmuebleJuridico)
					insertarBienInmuebleCatastroExpedienteBD(exp, i);
			}

		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	/**
	 * Metod que actualiza una direccion en base de datos.
	 *
	 * @param dir La direccion a actualizar.
	 * @param idMunicipio El id del municipio de la direccion.
	 * @throws Exception
	 * */
	private void updateDireccionDB(DireccionLocalizacion dir, long idMunicipio) throws Exception{
		if(dir!=null){

			String sSQL= "update Domicilio_Localizacion set Provincia_INE=?, Municipio_INE=?, Codigo_MunicipioDGC=?," +
			" Nombre_Entidad_Menor=?, Codigo_Via=?, Primer_Numero=?, Primera_Letra=?, Segundo_Numero=?, " +
			" Segunda_Letra=?, Kilometro=?, Bloque=?, Escalera=?, Planta=?, Puerta=?, Direccion_No_Estructurada=?," +
			" Codigo_Postal=?, Nombre_Via=?, Apartado_Correos=?, Tipo_Via=?, Nombre_Provincia=?, Nombre_Municipio=?" +
			" where id_localizacion="+ dir.getIdLocalizacion();

			PreparedStatement ps= null;
			Connection conn= null;

			try{
				conn= CPoolDatabase.getConnection();
				ps= conn.prepareStatement(sSQL);

				dir.setCodigoVia(getIdViaBD(dir.getTipoVia(),dir.getNombreVia(),idMunicipio));

				String codMunicipioDGC = findCodigoMunicipioDGCBD(dir);

				ps.setString(1,dir.getProvinciaINE());
				ps.setString(2,dir.getMunicipioINE());
				ps.setString(3,codMunicipioDGC);
				ps.setString(4,dir.getNombreEntidadMenor());

				ps.setLong(5,dir.getCodigoVia());                
				ps.setLong(6, dir.getPrimerNumero());
				ps.setString(7, dir.getPrimeraLetra());
				ps.setLong(8,dir.getSegundoNumero());
				ps.setString(9, dir.getSegundaLetra());
				ps.setDouble(10, dir.getKilometro());
				ps.setString(11, dir.getBloque());
				ps.setString(12, dir.getEscalera());
				ps.setString(13, dir.getPlanta());
				ps.setString(14, dir.getPuerta());
				ps.setString(15, dir.getDireccionNoEstructurada());
				ps.setInt(16, Integer.valueOf(dir.getCodigoPostal()));
				ps.setString(17, dir.getNombreVia());
				ps.setLong(18, dir.getApartadoCorreos());
				ps.setString(19, dir.getTipoVia());
				ps.setString(20, dir.getNombreProvincia());
				ps.setString(21, dir.getNombreMunicipio());

				ps.execute();
				conn.commit();
			}
			catch (Exception e){
				throw e;
			}
			finally{
				try{ps.close();}catch(Exception e){};
				try{conn.close();}catch(Exception e){};
			}
		}
	}

	/**
	 * Metodo que actualiza un expediente en base de datos.
	 *
	 * @param exp El expediente a actualizar.
	 * @return Expediente El expediente actualizado.
	 * @throws Exception
	 * */
	private Expediente updateExpedienteBD(Expediente exp) throws Exception
	{
		PreparedStatement ps= null;
		Connection conn= null;

		try
		{
			String queryUpdate ="update expediente set  ID_Entidad_Generadora=?, ID_Estado=?," +
			" ID_Tecnico_Catastro=?, ID_Localizacion=?,"+
			" ID_Municipio=?, Numero_Expediente=?, ID_Tipo_Expediente=?, Fecha_Alteracion=?, Anio_Expediente_Gerencia=?, Referencia_Expediente_Gerencia=?,"+
			" Codigo_Entidad_Registro_DGC_Origen_Alteracion=?, Anio_Expediente_Admin_Origen_Alteracion=?,"+
			" Referencia_Expediente_Admin_Origen=?, Fecha_Registro=?, Tipo_Documento_Origen_Alteracion=?,"+
			" Info_Documento_Origen_Alteracion=?, Codigo_Descriptivo_Alteracion=?, Descripcion_Alteracion=?,"+
			" NIF_Presentador=?, Nombre_Completo_Presentador=?, Num_BienesInmuebles_Urb=?, Num_BienesInmuebles_Rus=?,"+
			" Num_BienesInmuebles_Esp=?, Cod_Provincia_Notaria=?, Cod_Poblacion_Notaria=?, Cod_Notaria=?, Anio_protocolo_Notarial=?, Protocolo_notarial=?"+
			", fecha_movimiento=?, hora_movimiento=? where id_expediente="+exp.getIdExpediente();

			String oracleQueryUpdate ="update expediente set  ID_Entidad_Generadora=?, ID_Estado=?," +
			" ID_Tecnico_Catastro=?, ID_Localizacion=?,"+
			" ID_Municipio=?, Numero_Expediente=?, ID_Tipo_Expediente=?, Fecha_Alteracion=?, Anio_Expediente_Gerencia=?, Referencia_Expediente_Gerencia=?,"+
			" codent_reg_dgc_orig_alteracion=?, anio_exp_admin_orig_alteracion=?,"+
			" referencia_exp_admin_origen=?, Fecha_Registro=?, tipo_documento_orig_alteracion=?,"+
			" info_documento_orig_alteracion=?, Codigo_Descriptivo_Alteracion=?, Descripcion_Alteracion=?,"+
			" NIF_Presentador=?, Nombre_Completo_Presentador=?, Num_BienesInmuebles_Urb=?, Num_BienesInmuebles_Rus=?,"+
			" Num_BienesInmuebles_Esp=?, Cod_Provincia_Notaria=?, Cod_Poblacion_Notaria=?, Cod_Notaria=?, Anio_protocolo_Notarial=?, Protocolo_notarial=?"+
			", fecha_movimiento=?, hora_movimiento=? where id_expediente="+exp.getIdExpediente();


			conn= CPoolDatabase.getConnection();
			if(CPoolDatabase.isPostgres(conn))
				ps= conn.prepareStatement(queryUpdate);
			else
				ps= conn.prepareStatement(oracleQueryUpdate);

			ps.setLong(1,exp.getEntidadGeneradora().getIdEntidadGeneradora());
			ps.setLong(2,exp.getIdEstado());
			ps.setString(3,exp.getIdTecnicoCatastro());
			ps.setLong(4,exp.getDireccionPresentador().getIdLocalizacion());
			ps.setLong(5,exp.getIdMunicipio());
			ps.setString(6,exp.getNumeroExpediente());
			int idTipoExpediente = getIdTipoExpediente(exp.getTipoExpediente().getCodigoTipoExpediente());
			ps.setInt(7,idTipoExpediente);
			if(exp.getFechaAlteracion()!=null)
			{
				ps.setDate(8,new java.sql.Date(exp.getFechaAlteracion().getTime()));
			}
			else
			{
				ps.setDate(8,null);
			}

			ps.setObject(9, exp.getAnnoExpedienteGerencia());
			ps.setString(10, exp.getReferenciaExpedienteGerencia());
			ps.setObject(11, exp.getCodigoEntidadRegistroDGCOrigenAlteracion());
			ps.setInt(12, exp.getAnnoExpedienteAdminOrigenAlteracion());
			ps.setString(13, exp.getReferenciaExpedienteAdminOrigen());
			ps.setDate(14, new java.sql.Date(exp.getFechaRegistro().getTime()));
			ps.setString(15, exp.getTipoDocumentoOrigenAlteracion());
			ps.setString(16, exp.getInfoDocumentoOrigenAlteracion());
			ps.setString(17, exp.getCodigoDescriptivoAlteracion());
			ps.setString(18, exp.getDescripcionAlteracion());
			ps.setString(19, exp.getNifPresentador());
			ps.setString(20,exp.getNombreCompletoPresentador());
			ps.setLong(21, exp.getNumBienesInmueblesUrbanos());
			ps.setLong(22, exp.getNumBienesInmueblesRusticos());
			ps.setLong(23, exp.getNumBienesInmueblesCaractEsp());
			ps.setString(24, exp.getCodProvinciaNotaria());
			ps.setString(25, exp.getCodPoblacionNotaria());
			ps.setString(26, exp.getCodNotaria());
			ps.setObject(27, exp.getAnnoProtocoloNotarial());
			ps.setString(28, exp.getProtocoloNotarial());
			if(exp.getFechaMovimiento()!=null)
			{
				ps.setDate(29, new java.sql.Date(exp.getFechaMovimiento().getTime()));
			}
			else
			{
				ps.setDate(29, null);
			}
			if(exp.getHoraMovimiento()!=null)
			{
				SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss");
				ps.setTime(30, new Time(horaFormat.parse(exp.getHoraMovimiento()).getTime()));
			}
			else
			{
				ps.setTime(30, null);                
			}

			ps.execute();
			conn.commit();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return exp;
	}

	/**
	 * Metodo que devuelve los tipos de vias que hay almacenados en la base de datos.
	 *
	 * @return Collection ArrayList con los tipos de vias (String).
	 * @throws Exception
	 * */
	private DireccionLocalizacion getViaPorNombreBD(String nombre, String idMunicipio) throws Exception
	{
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		DireccionLocalizacion resultado= null;
		try
		{
			String query = "select * from vias where id_Municipio=" + Integer.valueOf(idMunicipio) + " and nombrecatastro " +
			"like '%" + nombre.toUpperCase() + "%'";
			conn= CPoolDatabase.getConnection();
			ps=conn.prepareStatement(query);
			rs=ps.executeQuery();
			resultado = new DireccionLocalizacion();
			if(rs.next())
			{
				resultado.setTipoVia(rs.getString("tipovianormalizadocatastro"));
				resultado.setNombreVia(rs.getString("nombrecatastro"));
				resultado.setCodigoVia(TypeUtil.getSimpleInteger(rs, "codigocatastro"));
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return resultado;
	}    

	/**
	 * Metodo que devuelve un ArrayList con los ficheros generados por la aplicacion hasta la fecha actual.
	 *
	 * @return Collection ArrayList con los objetos fichero obtenidos.
	 * @throws Exception
	 * */
	private Collection consultaHistoricoFicherosBD(String idMunicipio) throws Exception{
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		ArrayList resultados= null;

		try {

			String query =  "select * from ficheros where id_fichero in ( select distinct(f.id_fichero)" +
			" from ficheros f, expediente_fichero ef, expediente e " +
			" where e.id_expediente = ef.id_expediente and ef.id_fichero = f.id_fichero and " +
			" e.id_municipio = "+idMunicipio+" )";


			conn= CPoolDatabase.getConnection();
			ps=conn.prepareStatement(query);
			rs=ps.executeQuery();
			resultados = new ArrayList();

			while(rs.next()){
				Fichero fich= new Fichero();
				fich.setIdFichero(TypeUtil.getSimpleInteger(rs, "id_fichero"));
				fich.setIdTipoFichero(TypeUtil.getSimpleInteger(rs,"id_tipo_fichero"));
				fich.setNombre(rs.getString("nombre"));
				fich.setDescripcion(rs.getString("descripcion"));
				fich.setFechaGeneracion(rs.getDate("fecha_generacion"));
				fich.setFechaIntercambio(rs.getDate("fecha_intercambio"));
				fich.setContenido(rs.getString("contenido"));
				fich.setUrl(rs.getString("url"));
				fich.setFechaInicioPeriodo(rs.getDate("fecha_inicio_periodo"));
				fich.setFechaFinPeriodo(rs.getDate("fecha_fin_periodo"));
				fich.setCodigoEntidadDestinataria(TypeUtil.getSimpleInteger(rs,"codigo_entidad_destinataria"));
				fich.setCodigoEntidadGeneradora(TypeUtil.getSimpleInteger(rs,"codigo_entidad_generadora"));
				fich.setCodigoEnvio(rs.getString("codigo_envio"));
				resultados.add(fich);
			}
		}
		catch (Exception e){
			throw e;
		}
		finally {
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return resultados;
	}

	/**
	 * Metodo que inserta el fichero pasado por parametro en la tabla fichero y asocia los expedientes, pasados en el
	 * array al fichero. Ademas actualiza las fechas de envio y actualizacion periodica.
	 *
	 * @param listaNumExp Lista con los numeros de expediente.
	 * @param fich Fichero para insertar.
	 * @throws Exception
	 * */
	private void crearFicheroBD(ArrayList listaNumExp, Fichero fich, String idMunicipio) throws Exception {

		if(listaNumExp==null || listaNumExp.size()==0)
			return;

		String  sSQL= "INSERT INTO ficheros(Id_fichero, ID_Tipo_Fichero, nombre, Descripcion, fecha_generacion, " +
		" fecha_intercambio, url, Fecha_Inicio_Periodo, Fecha_Fin_Periodo," +
		" codigo_entidad_destinataria, codigo_entidad_generadora, Codigo_Envio) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

		PreparedStatement ps= null;
		Connection conn= CPoolDatabase.getConnection();

		try{
			long idFichero;

			if(CPoolDatabase.isPostgres(conn))
				idFichero = getIdDB("select nextval('seq_ficheros') as id_fichero","id_fichero");
			else
				idFichero = getIdDB("select seq_ficheros.nextval as id_fichero from dual","id_fichero");

			fich.setIdFichero((int)idFichero);

			ps= conn.prepareStatement(sSQL);
			ps.setLong(1,fich.getIdFichero());
			ps.setLong(2,fich.getIdTipoFichero());
			ps.setString(3,fich.getNombre());
			ps.setString(4,fich.getDescripcion());
			ps.setDate(5,new java.sql.Date(fich.getFechaGeneracion().getTime()));
			ps.setDate(6,new java.sql.Date(fich.getFechaIntercambio().getTime()));
			ps.setString(7,fich.getUrl());
			ps.setDate(8,new java.sql.Date(fich.getFechaInicioPeriodo().getTime()));
			ps.setDate(9,new java.sql.Date(fich.getFechaFinPeriodo().getTime()));
			ps.setInt(10, fich.getCodigoEntidadDestinataria());
			ps.setInt(11,fich.getCodigoEntidadGeneradora());
			ps.setObject(12,fich.getCodigoEnvio());

			ps.execute();
			conn.commit();

			for(int i = 0 ; i< listaNumExp.size();i++){
				String numExp = (String)listaNumExp.get(i);
				long idExpediente = getIDExpPorNumExp(numExp);

				try{

					sSQL = "select id_expediente, id_fichero from expediente_fichero " +
					"where id_expediente=" + idExpediente + " and id_fichero=" + 
					fich.getIdFichero();
					ps=conn.prepareStatement(sSQL);
					ResultSet rs = ps.executeQuery();

					if (!rs.next()){

						sSQL= "INSERT INTO EXPEDIENTE_fichero(Id_expediente, id_fichero) VALUES(?,?)";
						ps= conn.prepareStatement(sSQL);
						ps.setLong(1,idExpediente);
						ps.setLong(2,fich.getIdFichero());
						ps.execute();
						conn.commit();
					}
				}
				catch(Exception e){
					throw e;
				}//fin del catch
				finally{//añadimos este finally, porque sino el número máximo de cursores abiertos se excede en oracle
					try{ps.close();}catch(Exception e){};
				}
			}//fin del for
		}
		catch (Exception e){
			throw e;
		}
		finally{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}

		if(fich.getIdTipoFichero()==Fichero.FIN_ENTRADA || fich.getIdTipoFichero() == Fichero.VARPAD)
			guardarParametroConfiguracionBD(null,true,"Ultima_Fecha_Envio",new Date(System.currentTimeMillis()), idMunicipio);
		else if(fich.getIdTipoFichero()==Fichero.FIN_RETORNO)
			guardarParametroConfiguracionBD(null, true,"Ultima_Fecha_Actualizacion",new Date(System.currentTimeMillis()), idMunicipio);

	}

	/**
	 * Devuelve el id de un expediente buscado por el numero de expediente.
	 *
	 * @param numExp Numero de expediente.
	 * @return long El id del expediente.
	 * @throws Exception
	 * */
	private long getIDExpPorNumExp(String numExp) throws Exception
	{
		long id= -1;
		String sSQL= "select id_expediente from expediente where numero_expediente ='" + numExp + "'";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			if(rs.next())
			{
				id= rs.getLong("id_expediente");

			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return id;
	}

	/**
	 * Metodo que comprueba si es necesario avisar al usuario de la aplicacion que tiene que realizar un envio o
	 * actualizacion periodica. Los mensajes se envian en excepciones.
	 *
	 * @throws Exception
	 * */
	private void comprobarActualizacionYEnviosBD(String idMunicipio) throws Exception{
		String sSQL= "select * from configuracion where id_municipio = 0 or " +
		"id_municipio = " +idMunicipio+" order by id_municipio desc" ;
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;

		try{
			conn= CPoolDatabase.getConnection();
			ps=conn.prepareStatement(sSQL);
			rs=ps.executeQuery();
			int frecuenciaActualizacion = -1;
			int frecuenciaEnvio = -1;
			Date ultimaFechaAct = null;
			Date ultimaFechaEnv = null;
			int mostrarAvisoAct = -1;
			int mostrarAvisoEnv = -1;

			String msg = "";
			if(rs.next()){
				frecuenciaActualizacion = rs.getInt("Frecuencia_Actualizacion");
				frecuenciaEnvio = rs.getInt("Frecuencia_Envio");
				ultimaFechaAct = rs.getDate("ultima_Fecha_Actualizacion");
				ultimaFechaEnv = rs.getDate("ultima_Fecha_Envio");
				mostrarAvisoAct = rs.getInt("Mostrar_Aviso_Act");
				mostrarAvisoEnv = rs.getInt("Mostrar_Aviso_Envio");
			}

			if(frecuenciaActualizacion != -1 && mostrarAvisoAct==1){
				Calendar aux= new GregorianCalendar();
				aux.setTime(new Date(System.currentTimeMillis()));
				aux.add(Calendar.DATE,-frecuenciaActualizacion);
				if(ultimaFechaAct!=null) {
					if(aux.getTimeInMillis()>=ultimaFechaAct.getTime())
						msg = msg + "Catastro.RegistroExpedientes.MainCatastro.debeActualizar;";
				}
				else
					msg = msg + "Catastro.RegistroExpedientes.MainCatastro.noActualizacionCatastro;";

			}
			if(frecuenciaEnvio != -1 && mostrarAvisoEnv==1){
				Calendar aux= new GregorianCalendar();
				aux.setTime(new Date(System.currentTimeMillis()));
				aux.add(Calendar.DATE,-frecuenciaEnvio);
				if(ultimaFechaEnv!=null) {
					if(aux.getTimeInMillis()>=ultimaFechaEnv.getTime())
						msg = msg + "Catastro.RegistroExpedientes.MainCatastro.debeEnviar;";
				}
				else
					msg = msg + "Catastro.RegistroExpedientes.MainRegistroExp.noEnvioCatastro";

			}
			if(!msg.equalsIgnoreCase("")){
				throw new ACException(msg);
			}
		}
		catch (Exception e){
			throw e;
		}
		finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	/**
	 * Metodo que devuelve una Hashtable con dos ArrayList que contienen los codigos y nombres de las provincias,
	 * correspondiendose en la posicion. Las keys son "nombres" y "codigos".
	 *
	 * @return Hashtable Contiene dos listas con keys "nombres" y "codigos" con los nombres y codigos de las provincias
	 * @throws Exception
	 * */
	private Hashtable getCodigoNombreProvinciaBD() throws Exception
	{
		Hashtable aux= new Hashtable();
		ArrayList codigos = new ArrayList();
		ArrayList nombres = new ArrayList();
		String sSQL= "select id, nombreoficial from provincias order by translate(nombreoficial, 'ÁÉÍÓÚáéíóú','AEIOUaeiou')";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				codigos.add(rs.getString("id"));
				nombres.add(rs.getString("nombreoficial"));
			}
			//TODO esto mirar y mejorar
			aux.put("codigos",codigos);
			aux.put("nombres",nombres);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return aux;
	}

	/**
	 * Metodo que devuelve una Hashtable con dos ArrayList que contienen los codigos y nombres de los municipios,
	 * correspondiendose en la posicion. Las keys son "nombres" y "codigos".
	 *
	 * @param codigoProvincia El codigo de la provincia
	 * @return Hashtable Contiene dos listas con keys "nombres" y "codigos" con los nombres y codigos de los municipios
	 * @throws Exception
	 * */
	private Hashtable getCodigoNombreMunicipioBD(String codigoProvincia) throws Exception
	{
		Hashtable aux= new Hashtable();
		ArrayList codigos = new ArrayList();
		ArrayList nombres = new ArrayList();
		String sSQL= "select id_ine, nombreoficial from municipios where id_provincia='"+codigoProvincia +
		"' order by translate(nombreoficial, 'ÁÉÍÓÚáéíóú','AEIOUaeiou')";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				codigos.add(rs.getString("id_ine"));
				nombres.add(rs.getString("nombreoficial"));
			}
			aux.put("codigos",codigos);
			aux.put("nombres",nombres);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return aux;
	}
	
	private String getCodigoDGCMunicipioBD(String codigoMunicipioINE, String codigoProvincia) throws Exception
	{
		String aux= null;
		
		String sSQL= "select id_catastro from municipios where id_ine='"+ codigoMunicipioINE + 
		"' and id_provincia='" + codigoProvincia +"'";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				aux = rs.getString("id_catastro");
				
			}
			
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return aux;
	}

	/**
	 * Metodo que busca el codigo de municipio dgc para la direccion pasada por parametro y se la asigna.
	 *
	 * @param dir La direccion a asignar.
	 * @throws Exception
	 * */


	private String findCodigoMunicipioDGCBD(DireccionLocalizacion dir)throws Exception{
		String sSQL= "select id_catastro from municipios where id_provincia='"+dir.getProvinciaINE() +
		"' and id_ine='" + dir.getMunicipioINE() + "'";

		String codMunicipioDGC = null;

		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;

		try{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			if(rs.next())
				codMunicipioDGC = rs.getString("id_catastro");

			if(codMunicipioDGC != null && !codMunicipioDGC.equalsIgnoreCase(""))
				return codMunicipioDGC;
			else
				return DEFAULT_CODIGO_MUNICIPIO_DGC;
		}
		catch (Exception e){
			throw e;
		}
		finally{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}

	}

	private String findCodigoMunicipioDGCBDFromBI(DireccionLocalizacion dir)throws Exception{
		String sSQL= "select codigo_municipiodgc from bien_inmueble where " +
		"parcela_catastral = ";

		String defaultCodigoMunicipioDGC = "000";

		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;

		try{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			if(rs.next())
				return rs.getString("id_catastro");

			return defaultCodigoMunicipioDGC;
		}
		catch (Exception e){
			throw e;
		}
		finally{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}

	}


	/**
	 * Metodo que guarda los parametros de configuracion pasados por parametro en la tabla configuracion. Tambien
	 * actualiza las fechas de actualizacion y envio periodicos.
	 *
	 * @param datos Los datos de configuracion a guardar.
	 * @param actualizarFecha Boolean que indica si hay que actualizar fechas en vez de los datos.
	 * @param columna String que indica la columna a actualizar en caso de que actualicen las fechas.
	 * @param fecha La nueva fecha.
	 * @throws Exception
	 * */
	private void guardarParametroConfiguracionBD(DatosConfiguracion datos, boolean actualizarFecha,String columna, Date fecha, String idMunicipio)throws Exception {
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs = null;

		try{
			conn= CPoolDatabase.getConnection();

			String querySelect = "select * from configuracion where id_municipio = "+idMunicipio;
			ps= conn.prepareStatement(querySelect);
			rs= ps.executeQuery();
			if(!rs.next()){

				String queryInsert = "insert into configuracion  " +
				"(select frecuencia_actualizacion, frecuencia_envio, convenio," +
				" modo_trabajo, tipo_convenio, mostrar_aviso_act, mostrar_aviso_envio, " +
				"ultima_fecha_actualizacion, ultima_fecha_envio," + idMunicipio +
				" ,modo_generacion from configuracion where id_municipio = 0)";

				ps= conn.prepareStatement(queryInsert);
				ps.executeUpdate();
				conn.commit();
			}

			if(datos!=null && datos.getFrecuenciaActualizacion()!=-1 && datos.getFrecuenciaEnvio()!=-1 &&
					datos.getMostrarAvisoAct()!=-1 && datos.getMostrarAvisoEnvio()!=-1){

				String queryUpdate = "update configuracion set Frecuencia_Actualizacion=?, Frecuencia_Envio=?," +
				" Mostrar_Aviso_Act=?, Mostrar_Aviso_Envio=? modo_generacion=? where id_municipio = ?";

				ps= conn.prepareStatement(queryUpdate);
				ps.setInt(1,datos.getFrecuenciaActualizacion());
				ps.setInt(2,datos.getFrecuenciaEnvio());
				ps.setInt(3,datos.getMostrarAvisoAct());
				ps.setInt(4,datos.getMostrarAvisoEnvio());
				ps.setString(5,datos.getModoGenerarFXCC());
				ps.setString(6, idMunicipio);
				ps.execute();
				conn.commit();
			}
			else if(datos!=null && datos.getTipoConvenio()!=null && !datos.getTipoConvenio().equalsIgnoreCase("")
					&& datos.getModoTrabajo()!=null && !datos.getModoTrabajo().equalsIgnoreCase("")
					&& datos.getFormaConvenio()!=null && !datos.getFormaConvenio().equalsIgnoreCase("")
					&& datos.getModoGenerarFXCC()!=null && !datos.getModoGenerarFXCC().equalsIgnoreCase("")) {

				String queryUpdate = "update configuracion set Convenio=?, Modo_Trabajo=?, Tipo_Convenio=?, modo_generacion=? where id_municipio = ?";

				ps= conn.prepareStatement(queryUpdate);
				ps.setString(1,datos.getTipoConvenio());
				ps.setString(2,datos.getModoTrabajo());
				ps.setString(3,datos.getFormaConvenio());
				ps.setString(4,datos.getModoGenerarFXCC());
				ps.setString(5, idMunicipio);
				ps.execute();
				conn.commit();
			}
			else if(actualizarFecha) {
				String queryUpdate = "update configuracion set "+ columna+"=? where id_municipio = ?";
				ps= conn.prepareStatement(queryUpdate);
				ps.setDate(1,new java.sql.Date(fecha.getTime()));
				ps.setString(2, idMunicipio);
				ps.execute();
				conn.commit();
			}
		}
		catch (Exception e){
			throw e;
		}
		finally{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	/**
	 * Metodo que devuelve los datos de configuracion almacenados en la base de datos.
	 *
	 * @return DatosConfiguracion Los datos de configuracion de la aplicacion.
	 * @throws Exception
	 * */
	private DatosConfiguracion getParametrosConfiguracionBD(String idMunicipio)throws Exception{
		String sSQL= "select * from configuracion where id_municipio = 0 or " +
		"id_municipio = "+idMunicipio+" order by id_municipio desc";

		DatosConfiguracion datos = null;
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;

		try {
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();

			if(rs.next()){
				datos = new DatosConfiguracion();
				datos.setFrecuenciaActualizacion(rs.getInt("Frecuencia_Actualizacion"));//not NULL en la BD
				datos.setFrecuenciaEnvio(rs.getInt("Frecuencia_Envio"));//not NULL en la BD
				datos.setMostrarAvisoAct(TypeUtil.getSimpleInteger(rs, "Mostrar_Aviso_Act"));
				datos.setMostrarAvisoEnvio(TypeUtil.getSimpleInteger(rs, "Mostrar_Aviso_Envio"));
				datos.setTipoConvenio(rs.getString("Convenio"));
				datos.setFormaConvenio(rs.getString("Tipo_Convenio"));
				datos.setModoTrabajo(rs.getString("Modo_Trabajo"));
				datos.setModoGenerarFXCC(rs.getString("modo_generacion"));
			}
		}
		catch (Exception e){
			throw e;
		}
		finally{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return datos;
	}

	/**
	 * Metodo que devuelve los tipos de expedientes dependiendo del convenio en el que se este trabajando. Tambien
	 * devuelve un tipo de expediente si se le pasa el id de este.
	 *
	 * @param convenio Tipo de convenio con el que se esta trabajando.
	 * @param id Identificador del tipo de expediente a buscar.
	 * @return Collection Los tipos de expediente obtenidos.
	 * @throws Exception
	 * */
	private Collection getTiposExpedientesBD(String convenio, int id)throws Exception
	{
		String sSQL = null;
		if(convenio!=null &&id==-1)
		{
			if(convenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
			{
				sSQL= "select codigo_tipo_expediente,convenio from tipo_expediente";
			}
			else
			{
				sSQL= "select codigo_tipo_expediente,convenio from tipo_expediente where convenio='" + convenio + "'";
			}
		}
		else if(convenio==null & id!=-1)
		{
			sSQL= "select codigo_tipo_expediente,convenio from tipo_expediente where id_tipo_expediente=" + id;
		}
		ArrayList tiposExp = new ArrayList();
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				TipoExpediente tipoExp = new TipoExpediente();
				tipoExp.setCodigoTipoExpediente(rs.getString("codigo_tipo_expediente"));
				tipoExp.setConvenio(rs.getString("convenio"));
				tiposExp.add(tipoExp);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return tiposExp;
	}

	/**
	 *  Metodo que comprueba si ya hay una entrada en catastro temporal con ese expediente y esa referencia.
	 *
	 * @param exp El expediente que se busca.
	 * @param referencia La referencia que se busca.
	 * @return boolean Boolean que indica si se ha encontrado o no.
	 * @throws Exception
	 * */
	private boolean existExpediente(Expediente exp, String referencia) throws Exception
	{
		String SQL = "SELECT * FROM catastro_temporal WHERE id_expediente=? AND referencia=?";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs = null;

		try
		{
			System.out.println("Va a crear la conexion con la BBDD");
			conn= CPoolDatabase.getConnection();

			ps= conn.prepareStatement(SQL);
			ps.setLong(1, exp.getIdExpediente());
			ps.setString(2, referencia);

			rs = ps.executeQuery();

			if (rs.next())
				return true;

		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}

		return false;
	}



	private void modificaFincaExpedienteDB(Expediente exp, int i)throws Exception {

		if(exp!=null){
			String sSQL = "";
			PreparedStatement ps= null;
			Connection conn= null;

			try{
				conn= CPoolDatabase.getConnection();
				FincaCatastro finca = (FincaCatastro)exp.getListaReferencias().get(i);
				//Paso la finca a XML
				Java2XMLCatastro aux2 = new Java2XMLCatastro();
				String fincaXML =aux2.write(finca,"elemf");

				if (existExpediente(exp,finca.getRefFinca().getRefCatastral())){

					if (finca.getFxcc()!=null && finca.getFxcc().getDXF()!=null){

						String fxccXML = aux2.write(finca.getFxcc(), "fxcc");

						if (finca.getLstImagenes() != null && finca.getLstImagenes().size()>0){

							ImportarUtils oper = new ImportarUtils();
							String lstImagenes = oper.getImagenCatastroXML(finca.getLstImagenes());

							sSQL = "UPDATE catastro_temporal SET fxcc=?, limg=? WHERE id_expediente=? AND referencia=?";

							ps= conn.prepareStatement(sSQL);
							ps.setString(1, fxccXML);
							ps.setString(2, lstImagenes);
							ps.setLong(3,exp.getIdExpediente());
							ps.setString(4, finca.getRefFinca().getRefCatastral());
							ps.executeUpdate();

						}
						else{

							sSQL = "UPDATE catastro_temporal SET fxcc=? WHERE id_expediente=? AND referencia=?";

							ps= conn.prepareStatement(sSQL);
							ps.setString(1, fxccXML);
							ps.setLong(2,exp.getIdExpediente());
							ps.setString(3, finca.getRefFinca().getRefCatastral());
							ps.executeUpdate();
						}
					}
					else{

						if (finca.getLstImagenes() != null && finca.getLstImagenes().size()>0){

							ImportarUtils oper = new ImportarUtils();
							String lstImagenes = oper.getImagenCatastroXML(finca.getLstImagenes());

							sSQL = "UPDATE catastro_temporal SET xml=?, limg=? WHERE id_expediente=? AND referencia=?";

							ps= conn.prepareStatement(sSQL);
							ps.setString(1, fincaXML);
							ps.setString(2, lstImagenes);
							ps.setLong(3,exp.getIdExpediente());
							ps.setString(4, finca.getRefFinca().getRefCatastral());
							ps.executeUpdate();

						}
						else{

							sSQL = "UPDATE catastro_temporal SET xml=? WHERE id_expediente=? AND referencia=?";

							ps= conn.prepareStatement(sSQL);
							ps.setString(1, fincaXML);
							ps.setLong(2,exp.getIdExpediente());
							ps.setString(3, finca.getRefFinca().getRefCatastral());
							ps.executeUpdate();
						}
					}
				}
				else{

					if (finca.getFxcc()!=null){

						String fxccXML = aux2.write(finca.getFxcc(), "fxcc");

						if (finca.getLstImagenes() != null && finca.getLstImagenes().size()>0){

							ImportarUtils oper = new ImportarUtils();
							String lstImagenes = oper.getImagenCatastroXML(finca.getLstImagenes());

							sSQL = "INSERT INTO catastro_temporal (id_expediente, referencia, xml, fxcc, limg)" +
							" VALUES (?,?,?,?,?)";

							ps= conn.prepareStatement(sSQL);
							ps.setLong(1, exp.getIdExpediente());
							ps.setString(2,finca.getRefFinca().getRefCatastral());
							ps.setString(3, fincaXML);
							ps.setString(4, fxccXML);
							ps.setString(5, lstImagenes);
							ps.execute();

						}
						else{

							sSQL = "INSERT INTO catastro_temporal (id_expediente, referencia, xml, fxcc)" +
							" VALUES (?,?,?,?)";

							ps= conn.prepareStatement(sSQL);
							ps.setLong(1, exp.getIdExpediente());
							ps.setString(2,finca.getRefFinca().getRefCatastral());
							ps.setString(3, fincaXML);
							ps.setString(4, fxccXML);
							ps.execute();
						}
					}
					else{

						if (finca.getLstImagenes() != null && finca.getLstImagenes().size()>0){

							ImportarUtils oper = new ImportarUtils();
							String lstImagenes = oper.getImagenCatastroXML(finca.getLstImagenes());

							sSQL = "INSERT INTO catastro_temporal (id_expediente, referencia, xml, limg)" +
							" VALUES (?,?,?,?)";

							ps= conn.prepareStatement(sSQL);
							ps.setLong(1, exp.getIdExpediente());
							ps.setString(2,finca.getRefFinca().getRefCatastral());
							ps.setString(3, fincaXML);
							ps.setString(4, lstImagenes);
							ps.execute();

						}
						else{

							sSQL= "INSERT INTO catastro_temporal (id_expediente, referencia, xml)" +
							" VALUES (?,?,?)";

							ps= conn.prepareStatement(sSQL);
							ps.setLong(1, exp.getIdExpediente());
							ps.setString(2,finca.getRefFinca().getRefCatastral());
							ps.setString(3, fincaXML);
							ps.execute();

						}
					}
				} 

				conn.commit();
			}
			catch (Exception e)
			{
				throw e;
			}

			finally
			{
				try{ps.close();}catch(Exception e){};
				try{conn.close();}catch(Exception e){};
			}
		}
	}


	private void modificaBienInmuebleExpedienteBD(Expediente exp, int i) throws Exception{
		if(exp!=null){
			String sSQL = "";
			PreparedStatement ps= null;
			Connection conn= null;

			try{
				conn= CPoolDatabase.getConnection();
				BienInmuebleJuridico bien = (BienInmuebleJuridico)exp.getListaReferencias().get(i);

				//Tenemos que añadir el elemv porque es un tipo de expediente de titularidades
				Java2XMLCatastro aux2 = new Java2XMLCatastro();
				String bienXML =aux2.write(bien,"bij");
				bienXML = "<elemv>\n<lbicenv>\n" + bienXML + "</lbicenv>\n</elemv>\n";

				int inicioInfoTributaria = bienXML.lastIndexOf("<inft>");
				if(inicioInfoTributaria != -1){
					int finInfoTributaria =   bienXML.lastIndexOf("</inft>") + "</inft>".length();
					bienXML = bienXML.substring(0, inicioInfoTributaria) + bienXML.substring(finInfoTributaria, bienXML.length());  
				}

				//Se eliminana los datos econ. del BI en la VARPAD son opcionales 
				int inicioDatosEconBien = bienXML.lastIndexOf("<debi>");
				if(inicioDatosEconBien != -1){
					int finDatosEconBien =   bienXML.lastIndexOf("</debi>") + "</debi>".length();
					bienXML = bienXML.substring(0, inicioDatosEconBien) + bienXML.substring(finDatosEconBien, bienXML.length());
				}

				if (existExpediente(exp,bien.getBienInmueble().getIdBienInmueble().getIdBienInmueble())){
					sSQL = "UPDATE catastro_temporal SET xml=? WHERE id_expediente=? AND referencia=?";

					ps= conn.prepareStatement(sSQL);
					ps.setString(1, bienXML);
					ps.setLong(2,exp.getIdExpediente());
					ps.setString(3, bien.getBienInmueble().getIdBienInmueble().getIdBienInmueble());
					ps.executeUpdate();
				}
				else{
					sSQL= "INSERT INTO catastro_temporal(id_expediente,referencia, xml) VALUES (?,?,?)";

					ps= conn.prepareStatement(sSQL);
					ps.setLong(1, exp.getIdExpediente());
					ps.setString(2,bien.getBienInmueble().getIdBienInmueble().getIdBienInmueble());
					ps.setString(3, bienXML);
					ps.execute();
				}

				conn.commit();
			}
			catch (Exception e) {
				throw e;
			}
			finally
			{
				try{ps.close();}catch(Exception e){};
				try{conn.close();}catch(Exception e){};
			}
		}
	}


	private int getIdTipoExpediente(String tipoExpediente) throws Exception
	{
		String sSQL= "select id_tipo_expediente from tipo_expediente where codigo_tipo_expediente='" + tipoExpediente + "'";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		int idTipoExp= -1;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			if(rs.next())
			{
				idTipoExp = rs.getInt("id_tipo_expediente");
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return idTipoExp;
	}

	/**
	 * Metodo que devuelve una lista con personas que tienen el nif o parte del nif pasado
	 * por parametro. El objeto persona contiene el bien inmueble buscado.
	 *
	 * @param patronNif El patron de nif a buscar.
	 * @param idMunicipio EL id del municipio con el que se esta trabajando.
	 * @return Collection El resultado.
	 * @throws Exception
	 * */
	private Collection getBienesInmueblesBuscadasPorTitularBD(String patronNif, String idMunicipio) throws Exception
	{
		ArrayList aux= new ArrayList();
		String sSQL =" select distinct persona.nif , persona.parcela_catastral, persona.numero_cargo, persona.digito_control1," +
		" persona.digito_control2,persona.id_via, persona.primer_numero " +
		" from persona, parcelas where persona.parcela_catastral " +
		" IS NOT NULL and parcelas.referencia_catastral=persona.parcela_catastral and parcelas.id_municipio='"+ idMunicipio+"' " +
		" and ((persona.nif like upper('%" + patronNif + "%')) or ( persona.nif is null)) " +
		" order by persona.nif asc";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				Persona pers = new Persona();
				pers.setNif(rs.getString("nif"));
				String identBI = rs.getString("parcela_catastral") + rs.getString("numero_cargo") +
				rs.getString("digito_control1") + rs.getString("digito_control2");
				BienInmuebleCatastro bien = new BienInmuebleCatastro();
				IdBienInmueble idBI = new IdBienInmueble();
				idBI.setIdBienInmueble(identBI);
				bien.setIdBienInmueble(idBI);

				DireccionLocalizacion dir = new DireccionLocalizacion();
				dir.setPrimerNumero(TypeUtil.getSimpleInteger(rs, "primer_numero"));                
				int idVia = -1;
				idVia = TypeUtil.getSimpleInteger(rs,"id_via");
				ResultSet rsVia= null;
				try
				{
					if(idVia!=-1){
						sSQL = "select vias.tipovianormalizadocatastro, vias.nombrecatastro from vias where codigocatastro=" + idVia
						+ " and id_municipio="+Integer.parseInt(idMunicipio);
						ps= conn.prepareStatement(sSQL);
						rsVia= ps.executeQuery();
						if(rsVia.next())
						{
							dir.setNombreVia(rsVia.getString("nombrecatastro"));
							dir.setTipoVia(rsVia.getString("tipovianormalizadocatastro"));
						}//fin del if
					}//fin if
				}catch (Exception e)
				{
					throw e;
				}
				finally
				{
					try{rsVia.close();}catch(Exception e){};
				}
				bien.setDomicilioTributario(dir);
				pers.setBienInmueble(bien);
				aux.add(pers);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return aux;
	}

	/**
	 * Metodo que devuelve los expedientes que se tienen que exportar masivamente. Son todos aquellos que tienen estado
	 * finalizado o que no tienen una entrada asociada en expediente_fichero, por lo que nunca se les ha generado un
	 * fin entrada.
	 *
	 * @param idMunicipio El municipio con el que se esta trabajando.
	 * @param convenio El convenio con el que se esta trabajando.
	 * @return ArrayList Los expediente a exportar.
	 * @throws Exception
	 * */
	private ArrayList getExpedientesExportacionMasivaBD(String idMunicipio, String convenio) throws Exception {
		ArrayList expedientes = new ArrayList();

		String sSQL ="select * from expediente where id_municipio = "+idMunicipio +
		" and modo_acoplado=false" +
		" and id_estado="+ConstantesRegExp.ESTADO_FINALIZADO + " union " +
		" select * from expediente where id_municipio = " +idMunicipio +
		" and id_expediente not in (select id_expediente from expediente_fichero)";

		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;

		try{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();

			while(rs.next()) {

				Expediente exp= new Expediente();
				exp.setIdExpediente(TypeUtil.getSimpleLong(rs, "id_expediente"));
				int idEntidadGeneradora =rs.getInt("id_entidad_generadora");
				getEntidadGeneradoraBD(exp,null, true,idEntidadGeneradora);
				exp.setIdEstado(TypeUtil.getSimpleLong(rs,"id_estado"));
				exp.setIdTecnicoCatastro(rs.getString("id_tecnico_catastro"));
				int idLocalizacion=rs.getInt("id_localizacion");
				getDireccionExpUsuarioBD(exp,idLocalizacion);
				exp.setIdMunicipio(rs.getInt("id_municipio"));
				exp.setNumeroExpediente(rs.getString("numero_expediente"));
				exp.setTipoExpediente((TipoExpediente)((ArrayList)getTiposExpedientesBD(null,rs.getInt("Id_Tipo_expediente"))).get(0));
				exp.setFechaAlteracion((java.util.Date)rs.getDate("fecha_alteracion"));

				int anoExpGerencia = TypeUtil.getSimpleInteger(rs,"anio_expediente_gerencia");
				exp.setAnnoExpedienteGerencia( anoExpGerencia != -1 ? new Integer(anoExpGerencia) : null);

				exp.setReferenciaExpedienteGerencia(rs.getString("referencia_expediente_gerencia"));

				if(CPoolDatabase.isPostgres(conn)){
					int codEntidadRegDGCOrigAlteracion = TypeUtil.getSimpleInteger(rs,"codigo_entidad_registro_dgc_origen_alteracion");
					exp.setCodigoEntidadRegistroDGCOrigenAlteracion( codEntidadRegDGCOrigAlteracion!=-1? new Integer(codEntidadRegDGCOrigAlteracion) :null);
					exp.setAnnoExpedienteAdminOrigenAlteracion(TypeUtil.getSimpleInteger(rs,"anio_expediente_admin_origen_alteracion"));
					exp.setReferenciaExpedienteAdminOrigen(rs.getString("referencia_expediente_admin_origen"));
					exp.setTipoDocumentoOrigenAlteracion(rs.getString("tipo_documento_origen_alteracion"));
					exp.setInfoDocumentoOrigenAlteracion(rs.getString("info_documento_origen_alteracion"));
				}
				else{
					int codEntidadRegDGCOrigAlteracion = TypeUtil.getSimpleInteger(rs,"codent_reg_dgc_orig_alteracion");
					exp.setCodigoEntidadRegistroDGCOrigenAlteracion( codEntidadRegDGCOrigAlteracion!=-1? new Integer(codEntidadRegDGCOrigAlteracion) :null);
					exp.setAnnoExpedienteAdminOrigenAlteracion(TypeUtil.getSimpleInteger(rs,"anio_exp_admin_orig_alteracion"));
					exp.setReferenciaExpedienteAdminOrigen(rs.getString("referencia_exp_admin_origen"));
					exp.setTipoDocumentoOrigenAlteracion(rs.getString("tipo_documento_orig_alteracion"));
					exp.setInfoDocumentoOrigenAlteracion(rs.getString("info_documento_orig_alteracion"));
				}

				exp.setFechaRegistro((java.util.Date)rs.getDate("fecha_registro"));
				exp.setFechaMovimiento((java.util.Date)rs.getDate("fecha_movimiento"));

				Time horaMov = rs.getTime("hora_movimiento");
				String horaGene = null;
				if(horaMov!=null)  {
					SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss");
					horaGene = horaFormat.format(horaMov);
				}

				exp.setHoraMovimiento(horaGene);
				exp.setFechaDeCierre(rs.getDate("fecha_de_cierre"));
				exp.setCodigoDescriptivoAlteracion(rs.getString("codigo_descriptivo_alteracion"));
				exp.setDescripcionAlteracion(rs.getString("descripcion_alteracion"));
				exp.setNifPresentador(rs.getString("nif_presentador"));
				exp.setNombreCompletoPresentador(rs.getString("nombre_completo_presentador"));
				exp.setNumBienesInmueblesUrbanos(TypeUtil.getSimpleInteger(rs,"num_bienesinmuebles_urb"));
				exp.setNumBienesInmueblesRusticos(TypeUtil.getSimpleInteger(rs,"num_bienesinmuebles_rus"));
				exp.setNumBienesInmueblesCaractEsp(TypeUtil.getSimpleInteger(rs,"num_bienesinmuebles_esp"));
				exp.setCodProvinciaNotaria(rs.getString("Cod_Provincia_Notaria"));
				exp.setCodPoblacionNotaria(rs.getString("Cod_Poblacion_Notaria"));
				exp.setCodNotaria(rs.getString("Cod_Notaria"));

				exp.setAnnoProtocoloNotarial(rs.getString("Anio_protocolo_Notarial"));

				exp.setProtocoloNotarial(rs.getString("Protocolo_notarial"));

				if(convenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)) {
					getBienInmuebleCatastroExpedienteDB(exp, idMunicipio);
				}
				else if(convenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO) &&
						exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)){
					getFincaCatastroExpedienteDB(exp, idMunicipio);
				}
				else if(convenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO) &&
						exp.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)){
					getBienInmuebleCatastroExpedienteDB(exp, idMunicipio);
				}

				expedientes.add(exp);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return expedientes;
	}

	/**
	 * Metodo que devuelve la informacion de catastro temporal para un expediente pasado por parametro. Se devuelve una
	 * lista con dos lista, una la informacion de elemf y otra la informacion grafica si la hay.
	 *
	 * @param expediente El expediente sobre el que se va a buscar la informacion grafica.
	 * @return El resultado.
	 * @throws Exception
	 * */
	private ArrayList getXmlParcelasFX_CCExpBD(Expediente expediente) throws Exception {

		ArrayList xmlParcelasFX_CCExp = new ArrayList();

		if(expediente!=null) {
			String sSQL = "select xml,fxcc,limg from catastro_temporal where id_expediente=" + expediente.getIdExpediente();

			PreparedStatement ps= null;
			Connection conn= null;
			ResultSet rs= null;

			try{
				conn= CPoolDatabase.getConnection();
				ps= conn.prepareStatement(sSQL);
				rs= ps.executeQuery();

				ArrayList xmlParcela = new ArrayList();
				ArrayList xmlFX_CC = new ArrayList();
				ArrayList xmlIMG = new ArrayList();
				int i=0;

				while(rs.next()){
					xmlParcela.add(i,rs.getString("xml"));
					xmlFX_CC.add(i, rs.getString("fxcc"));
					xmlIMG.add(i, rs.getString("limg"));
					i++;
				}

				xmlParcelasFX_CCExp.add(0,xmlParcela);
				xmlParcelasFX_CCExp.add(1,xmlFX_CC);
				xmlParcelasFX_CCExp.add(2,xmlIMG);
			}
			catch (Exception e){ throw e; }
			finally{
				try{
					if(ps!=null) ps.close();
					if(rs!=null)rs.close();
					if(conn!=null)conn.close();
				}
				catch(Exception e){}
			}
		}

		return xmlParcelasFX_CCExp;
	}
	
	private ArrayList getFxccParcelasExpBD(Expediente expediente) throws Exception {

		ArrayList fxccParcelasExp = new ArrayList();

		if(expediente!=null) {
			String sSQL = "select referencia,fxcc,limg from catastro_temporal where id_expediente=" + expediente.getIdExpediente();

			PreparedStatement ps= null;
			Connection conn= null;
			ResultSet rs= null;

			try{
				conn= CPoolDatabase.getConnection();
				ps= conn.prepareStatement(sSQL);
				rs= ps.executeQuery();

				while(rs.next()){
					
					FxccExportacion fxcc = new FxccExportacion();

					fxcc.setRefCatastral(rs.getString("referencia"));
					fxcc.setFxcc(rs.getString("fxcc"));
					fxcc.setLstImg(rs.getString("limg"));
					
					fxccParcelasExp.add(fxcc);
				}

			}
			catch (Exception e){ throw e; }
			finally{
				try{
					if(ps!=null) ps.close();
					if(rs!=null)rs.close();
					if(conn!=null)conn.close();
				}
				catch(Exception e){}
			}
		}

		return fxccParcelasExp;
	}
	
	/**
	 * Metodo que crea una cabecera para la comunicacion con la DGC mediante WS. Recopila los datos y crea el objeto
	 * que sera parseado a xml por el metodo que lo haya llamado.
	 *
	 * @param expedientes Los expediente a exportar.
	 * @return CabeceraWSCatastro El resultado.
	 * @throws Exception
	 * */
	private CabeceraWSCatastro creaCabeceraWSBD(Expediente exp, String tipo, 
			String nombreSolicitante, String nifSolicitante) throws Exception{
		Date fechaIniPe = null;
		CabeceraWSCatastro cabecera= new CabeceraWSCatastro();


		cabecera.setTipoEntidadGeneradora(exp.getEntidadGeneradora().getTipo());
		cabecera.setCodigoDelegacion(exp.getEntidadGeneradora().getCodigo());
		cabecera.setMunicipioODiputacion(exp.getEntidadGeneradora().getDescripcion());
		cabecera.setNombreEntidadGeneradora(exp.getEntidadGeneradora().getNombre());

		cabecera.setFechaGeneracionFichero(new Date(System.currentTimeMillis()));
		SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss");
		String horaGene = horaFormat.format(new Time(System.currentTimeMillis()));
		cabecera.setHoraGeneracionFichero(horaGene);
		
		if(tipo.equals(ConstantesRegistroExp.TAG_CREACION_EXPEDIENTE_REQUEST)){
			cabecera.setTipoFichero(CabeceraWSCatastro.TIPO_XML_CREACION_EXPEDIENTE);
			cabecera.setActualizaCatastro(false);
		}
		else if(tipo.equals(ConstantesRegistroExp.TAG_CONSULTA_CATASTRO_REQUEST)){
			cabecera.setTipoFichero(CabeceraWSCatastro.TIPO_XML_CONSULTA_CATASTRO);
			cabecera.setActualizaCatastro(false);
		}
		else if(tipo.equals(ConstantesRegistroExp.TAG_ACTUALIZA_CATASTRO_REQUEST)){
			cabecera.setTipoFichero(CabeceraWSCatastro.TIPO_XML_ACTUALIZA_CATASTRO);
			cabecera.setActualizaCatastro(true);
			
			ArrayList lstIdentificadorDialogo = new ArrayList();
			for (int i=0; i<exp.getListaReferencias().size(); i++){
				IdentificadorDialogo identificadorDialogo = new IdentificadorDialogo();
				Object obj = exp.getListaReferencias().get(i);
				if(obj instanceof FincaCatastro){
					FincaCatastro finca = (FincaCatastro) obj;
					identificadorDialogo.setFincaBien(finca);
					identificadorDialogo.setIdentificadorDialogo(finca.getIdentificadorDialogo());
					identificadorDialogo.setPc1(finca.getRefFinca().getRefCatastral1());
					identificadorDialogo.setPc2(finca.getRefFinca().getRefCatastral2());
					identificadorDialogo.setCodigoDelegacion(finca.getCodDelegacionMEH());
					identificadorDialogo.setCodigoMunicipioDGC(finca.getCodMunicipioDGC());
					identificadorDialogo.setBICE(finca.getBICE());
				}
				else if(obj instanceof BienInmuebleCatastro){
					BienInmuebleCatastro bien = (BienInmuebleCatastro)obj;
					identificadorDialogo.setFincaBien(bien);
					identificadorDialogo.setIdentificadorDialogo(((BienInmuebleCatastro) obj).getIdentificadorDialogo());
					
					identificadorDialogo.setIdBienInmueble(bien.getIdBienInmueble());

					
					identificadorDialogo.setCodigoDelegacion(bien.getDomicilioTributario().getProvinciaINE());
					identificadorDialogo.setCodigoMunicipioDGC(bien.getCodMunicipioDGC());
					identificadorDialogo.setClaseBienInmueble(bien.getClaseBienInmueble());
				}
				lstIdentificadorDialogo.add(identificadorDialogo);
			}
			cabecera.setLstIdentificadorDialogo(lstIdentificadorDialogo);

		}
		else if(tipo.equals(ConstantesRegistroExp.TAG_CONSULTA_ESTADO_EXPEDIENTE_REQUEST)){
			cabecera.setTipoFichero(CabeceraWSCatastro.TIPO_XML_CONSULTA_ESTADO_EXPEDIENTE);
			cabecera.setActualizaCatastro(false);
		}

		cabecera.setNifPersona(nifSolicitante);
		cabecera.setNombrePersona(nombreSolicitante);
	
		return cabecera;
	}

	/**
	 * Metodo que crea una cabecera para el archivo fin entrada. Recopila los datos de base de datos y crea el objeto
	 * que sera parseado a xml por el metodo que lo haya llamado.
	 *
	 * @param expedientes Los expediente a exportar.
	 * @param modoTrabajo El modo de trabajo, acoplado o desacoplado.
	 * @return CabeceraFinEntrada El resultado.
	 * @throws Exception
	 * */
	private CabeceraFinEntrada creaCabeceraFinEntradaMasivoBD(ArrayList expedientes, String modoTrabajo) throws Exception{
		Date fechaIniPe = null;
		CabeceraFinEntrada cabecera= new CabeceraFinEntrada();

		if(expedientes!=null && expedientes.size()>0){
			Expediente exp = (Expediente)expedientes.get(0);

			cabecera.setTipoEntidadGeneradora(exp.getEntidadGeneradora().getTipo());
			cabecera.setCodigoDelegacion(exp.getEntidadGeneradora().getCodigo());
			cabecera.setMunicipioODiputacion(exp.getEntidadGeneradora().getDescripcion());
			cabecera.setNombreEntidadGeneradora(exp.getEntidadGeneradora().getNombre());

			cabecera.setCodigoEntidadDestinataria(EntidadGeneradora.CODIGO_CATASTRO);
			cabecera.setFechaGeneracionFichero(new Date(System.currentTimeMillis()));
			SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss");
			String horaGene = horaFormat.format(new Time(System.currentTimeMillis()));
			cabecera.setHoraGeneracionFichero(horaGene);
			cabecera.setTipoFichero(CabeceraFinEntrada.TIPO_FICHERO_FIN_ENTRADA);

			String sSQL= "select ultima_fecha_envio from configuracion";
			PreparedStatement ps= null;
			Connection conn= null;
			ResultSet rs= null;

			int numeroExp = expedientes.size();
			int numeroFincas = 0;
			int numeroBienesInmuebles = 0;
			int numeroTitulares= 0;
			try {
				conn= CPoolDatabase.getConnection();
				ps= conn.prepareStatement(sSQL);
				rs= ps.executeQuery();

				if(rs.next())
					fechaIniPe = rs.getDate("ultima_fecha_envio");

				if(modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO)){

					sSQL = "select count(*) as numeroFincas from catastro_temporal where id_expediente=" + ((Expediente)expedientes.get(0)).getIdExpediente();
					ps= conn.prepareStatement(sSQL);
					rs= ps.executeQuery();

					if(rs.next())
						numeroFincas = rs.getInt("numeroFincas");
					
					sSQL ="select count(*) as numeroBienesInmuebles from bien_inmueble b where parcela_catastral in " + 
						"(select e.ref_catastral from expediente_finca_catastro e left join parcelas p " +
					"on p.referencia_catastral=e.ref_catastral and p.fecha_baja is null where e.id_expediente=" 
					+ ((Expediente)expedientes.get(0)).getIdExpediente() + " and p.id_municipio='" + new Long(((Expediente)expedientes.get(0)).getIdMunicipio()).toString() + "')";
					
					
					ps= conn.prepareStatement(sSQL);
					rs= ps.executeQuery();

					if(rs.next())
						numeroBienesInmuebles=  rs.getInt("numeroBienesInmuebles");

					if(CPoolDatabase.isPostgres(conn))
				
						sSQL = "select count(*) as numeroTitulares from persona where (parcela_catastral || numero_cargo || digito_control1 || " +
						" digito_control2) in (select b.identificador from bien_inmueble b where parcela_catastral in " +
						"(select e.ref_catastral from expediente_finca_catastro e left join parcelas p " +
						"on p.referencia_catastral=e.ref_catastral and p.fecha_baja is null where e.id_expediente=" 
						+ ((Expediente)expedientes.get(0)).getIdExpediente() + " and p.id_municipio='" + new Long(((Expediente)expedientes.get(0)).getIdMunicipio()).toString() + "'))";
												
					else
			
						sSQL = "select count(*) as numeroTitulares from persona where (concat(parcela_catastral, concat(numero_cargo, concat(digito_control1, " +
						" digito_control2)))) in (select b.identificador from bien_inmueble b where parcela_catastral in " +
						"(select e.ref_catastral from expediente_finca_catastro e left join parcelas p " +
						"on p.referencia_catastral=e.ref_catastral and p.fecha_baja is null where e.id_expediente=" 
						+ ((Expediente)expedientes.get(0)).getIdExpediente() + " and p.id_municipio='" + new Long(((Expediente)expedientes.get(0)).getIdMunicipio()).toString() + "'";

					ps= conn.prepareStatement(sSQL);
					rs= ps.executeQuery();
					if(rs.next())
						numeroTitulares=  rs.getInt("numeroTitulares");

					ps= conn.prepareStatement(sSQL);
					rs= ps.executeQuery();
					if(rs.next())
						numeroTitulares= numeroTitulares +  rs.getInt("numeroTitulares");

				}
				else{
					
					sSQL = "select count(*) as numeroFincas from parcelas p, expediente_finca_catastro ep where p.referencia_catastral=ep.ref_catastral and p.fecha_baja is null and"
						+" ep.id_expediente in(select id_expediente from expediente where id_estado="+ConstantesRegExp.ESTADO_FINALIZADO +
						" union select id_expediente from expediente where id_expediente " +
						" not in (select id_expediente from expediente_fichero))";
					
					ps= conn.prepareStatement(sSQL);
					rs= ps.executeQuery();

					if(rs.next())
						numeroFincas = rs.getInt("numeroFincas");

					sSQL ="select count(*) as numeroBienesInmuebles from bien_inmueble b where parcela_catastral in " +
					" (select p.referencia_catastral from parcelas p, expediente_finca_catastro ep where" +
					" p.referencia_catastral=ep.ref_catastral and p.fecha_baja is null and ep.id_expediente " +
					" in(select id_expediente from expediente where id_estado="+ConstantesRegExp.ESTADO_FINALIZADO + " union " +
					" select id_expediente from expediente where id_expediente " +
					" not in (select id_expediente from expediente_fichero)))";
					ps= conn.prepareStatement(sSQL);
					rs= ps.executeQuery();

					if(rs.next())
						numeroBienesInmuebles=  rs.getInt("numeroBienesInmuebles");

					if(CPoolDatabase.isPostgres(conn))
						sSQL = "select count(*) as numeroTitulares from persona where (parcela_catastral || numero_cargo || digito_control1 || " +
						" digito_control2) in (select b.identificador from bien_inmueble b where parcela_catastral in " +
						" (select p.referencia_catastral from parcelas p, expediente_finca_catastro ep where " +
						" p.referencia_catastral=ep.ref_catastral and p.fecha_baja is null and ep.id_expediente " +
						" in(select id_expediente from expediente where id_estado="+ConstantesRegExp.ESTADO_FINALIZADO +
						" union select id_expediente from expediente where id_expediente " +
						" not in (select id_expediente from expediente_fichero))))";
					else
						sSQL = "select count(*) as numeroTitulares from persona where (concat(parcela_catastral, concat(numero_cargo, concat(digito_control1," +
						" digito_control2)))) in (select b.identificador from bien_inmueble b where parcela_catastral in " +
						" (select p.referencia_catastral from parcelas p, expediente_finca_catastro ep where " +
						" p.referencia_catastral=ep.ref_catastral and p.fecha_baja is null and ep.id_expediente " +
						" in(select id_expediente from expediente where id_estado="+ConstantesRegExp.ESTADO_FINALIZADO +
						" union select id_expediente from expediente where id_expediente " +
						" not in (select id_expediente from expediente_fichero))))";

					ps= conn.prepareStatement(sSQL);
					rs= ps.executeQuery();
					if(rs.next())
						numeroTitulares=  rs.getInt("numeroTitulares");

				}
				cabecera.setFechaInicioPeriodo(fechaIniPe);
				cabecera.setFechaFinalizacionPeriodo(new Date(System.currentTimeMillis()));
				cabecera.setNumeroExpedientes(numeroExp);
				cabecera.setNumeroFincas(numeroFincas);
				cabecera.setNumeroBienesInmuebles(numeroBienesInmuebles);
				cabecera.setNumeroTitulares(numeroTitulares);
			}
			catch (Exception e){
				throw e;
			}
			finally{
				try{ps.close();}catch(Exception e){};
				try{rs.close();}catch(Exception e){};
				try{conn.close();}catch(Exception e){};
			}
		}
		return cabecera;
	}

	private CabeceraVARPAD creaCabeceraVARPADMasivoBD(ArrayList expedientes, String modoTrabajo) throws Exception{
		Date fechaIniPe = null;
		CabeceraVARPAD cabecera= new CabeceraVARPAD();

		if(expedientes!=null && expedientes.size()>0){
			Expediente exp = (Expediente)expedientes.get(0);

			cabecera.setTipoEntidadGeneradora(exp.getEntidadGeneradora().getTipo());
			cabecera.setCodigoDelegacion(exp.getEntidadGeneradora().getCodigo());
			cabecera.setMunicipioODiputacion(exp.getEntidadGeneradora().getDescripcion());
			cabecera.setNombreEntidadGeneradora(exp.getEntidadGeneradora().getNombre());

			cabecera.setCodigoEntidadDestinataria(EntidadGeneradora.CODIGO_CATASTRO);
			cabecera.setFechaGeneracionFichero(new Date(System.currentTimeMillis()));
			SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss");
			String horaGene = horaFormat.format(new Time(System.currentTimeMillis()));
			cabecera.setHoraGeneracionFichero(horaGene);
			cabecera.setTipoFichero(CabeceraFinEntrada.TIPO_FICHERO_VARPAD);

			String sSQL= "select ultima_fecha_envio from configuracion";
			PreparedStatement ps= null;
			Connection conn= null;
			ResultSet rs= null;

			int numeroExp = expedientes.size();
			int numeroFincas = 0;
			int numeroBienesInmuebles = 0;
			int numeroTitulares= 0;
			try {
				conn= CPoolDatabase.getConnection();
				ps= conn.prepareStatement(sSQL);
				rs= ps.executeQuery();

				if(rs.next())
					fechaIniPe = rs.getDate("ultima_fecha_envio");

				if(modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO)){

					sSQL = "select count(*) as numeroBienesInmuebles from catastro_temporal where id_expediente=" + ((Expediente)expedientes.get(0)).getIdExpediente();
					ps= conn.prepareStatement(sSQL);
					rs= ps.executeQuery();

					if(rs.next())
						numeroBienesInmuebles= rs.getInt("numeroBienesInmuebles");

					if(CPoolDatabase.isPostgres(conn))
						sSQL = "select count(*)as numeroTitulares from persona where (parcela_catastral || numero_cargo || digito_control1 || digito_control2) " +
						"in (select id_bieninmueble from expediente_bieninmueble where id_expediente="+((Expediente)expedientes.get(0)).getIdExpediente()+")";
					else
						sSQL = "select count(*) as numeroTitulares from persona where (concat(parcela_catastral, concat(numero_cargo, concat(digito_control1, " +
						" digito_control2)))) in (select id_bieninmueble from expediente_bieninmueble " +
						"here id_expediente="+((Expediente)expedientes.get(0)).getIdExpediente()+")";

					ps= conn.prepareStatement(sSQL);
					rs= ps.executeQuery();
					if(rs.next())
						numeroTitulares= rs.getInt("numeroTitulares");

				}
				else{
					sSQL = "select count(*) as numeroBienesInmuebles from bien_inmueble b, expediente_bieninmueble eb where b.identificador=eb.id_bieninmueble and"
						+" eb.id_expediente in(select id_expediente from expediente where id_estado="+ConstantesRegExp.ESTADO_FINALIZADO +
						" union select id_expediente from expediente where id_expediente " +
						" not in (select id_expediente from expediente_fichero))";
					ps= conn.prepareStatement(sSQL);
					rs= ps.executeQuery();

					if(rs.next())
						numeroBienesInmuebles= rs.getInt("numeroBienesInmuebles");

					if(CPoolDatabase.isPostgres(conn))
						sSQL = "select count(*) as numeroTitulares from persona where (parcela_catastral || numero_cargo || digito_control1 || digito_control2) in" +
						" (select b.identificador from bien_inmueble b, expediente_bieninmueble eb where b.identificador=eb.id_bieninmueble and"
						+" eb.id_expediente " +
						" in(select id_expediente from expediente where id_estado="+ConstantesRegExp.ESTADO_FINALIZADO +
						" union select id_expediente from expediente where id_expediente " +
						" not in (select id_expediente from expediente_fichero)))";
					else
						sSQL = "select count(*) as numeroTitulares from persona where (concat(parcela_catastral, concat(numero_cargo, concat(digito_control1, digito_control2)))) in" +
						" (select b.identificador from bien_inmueble b, expediente_bieninmueble eb where b.identificador=eb.id_bieninmueble and"
						+" eb.id_expediente " +
						" in(select id_expediente from expediente where id_estado="+ConstantesRegExp.ESTADO_FINALIZADO +
						" union select id_expediente from expediente where id_expediente " +
						" not in (select id_expediente from expediente_fichero)))";

					ps= conn.prepareStatement(sSQL);
					rs= ps.executeQuery();

					if(rs.next())
						numeroTitulares=rs.getInt("numeroTitulares");


				}
				cabecera.setFechaInicioPeriodo(fechaIniPe);
				cabecera.setFechaFinalizacionPeriodo(new Date(System.currentTimeMillis()));
				cabecera.setNumeroExpedientes(numeroExp);         
				cabecera.setNumeroBienesInmuebles(numeroBienesInmuebles);
				cabecera.setNumeroTitulares(numeroTitulares);
			}
			catch (Exception e){
				throw e;
			}
			finally{
				try{ps.close();}catch(Exception e){};
				try{rs.close();}catch(Exception e){};
				try{conn.close();}catch(Exception e){};
			}
		}
		return cabecera;
	}

	/**
	 * Metodo que devuelve el nombre de una provincia a partir de su id.
	 *
	 * @param idProvincia El identificador de la provincia.
	 * @return String El nombre.
	 * @throws SQLException
	 * */
	private String getNombreProvinciaById(String idProvincia) throws SQLException
	{
		String provincia = null;
		String sSQL = "select nombreoficial from provincias where id="+idProvincia;
		PreparedStatement ps = null;
		Connection conn = null;
		ResultSet rs = null;

		try {

			conn = CPoolDatabase.getConnection();
			ps = conn.prepareStatement(sSQL);
			rs = ps.executeQuery();

			while (rs.next())
			{
				provincia = rs.getString("nombreoficial");
			}
			return provincia;

		} catch (SQLException e) {
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	/**
	 * Metodo que devuelve un objeto persona buscado a traves del nif de su representante y el id del bien inmueble.
	 *
	 * @param nifRepresentante El nif del representante.
	 * @param id_bienInmueble El id del bien inmueble.
	 * @return Persona El objeto devuelto.
	 * @throws SQLException
	 * */
	private Persona getRepresentante(String nifRepresentante, String id_bienInmueble) throws SQLException
	{
		Persona representante = new Persona();
		String sSQL = "select * from representante r where r.nifrepresentante='"+nifRepresentante+
		"' AND r.id_bieninmueble='"+id_bienInmueble+"'";

		PreparedStatement ps = null;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement psVia= null;
		ResultSet rsVia= null;
		PreparedStatement psMunicipio= null;
		ResultSet rsMunicipio= null;

		try {

			conn = CPoolDatabase.getConnection();
			ps = conn.prepareStatement(sSQL);
			rs = ps.executeQuery();

			while (rs.next())
			{
				//Ya no existe: representante.setAusenciaNIF(rs.getString("ausencia_nif"));
				representante.setCodEntidadMenor(rs.getString("entidad_menor"));

				DireccionLocalizacion dirRepresentante = new DireccionLocalizacion();

				int idVia = -1;
				idVia = TypeUtil.getSimpleInteger(rs, "id_via");
				if(idVia!=-1){
					sSQL = "select v.tipovianormalizadocatastro, v.nombrecatastro, v.codigocatastro from vias v where id=" + idVia;
					psVia= conn.prepareStatement(sSQL);
					rsVia= psVia.executeQuery();
					if(rsVia.next()){
						dirRepresentante.setCodigoVia(TypeUtil.getSimpleInteger(rs,"codigocatastro"));
						dirRepresentante.setTipoVia(rsVia.getString("tipovianormalizadocatastro"));
						dirRepresentante.setNombreVia(rsVia.getString("nombrecatastro"));
					}
				}

				dirRepresentante.setPrimerNumero(TypeUtil.getSimpleInteger(rs,"primer_numero"));
				dirRepresentante.setBloque(rs.getString("bloque"));
				dirRepresentante.setCodigoMunicipioDGC(rs.getString("codigo_municipio_dgc"));
				dirRepresentante.setCodigoPostal(rs.getString("codigo_postal"));
				dirRepresentante.setDireccionNoEstructurada(rs.getString("direccion_no_estructurada"));
				dirRepresentante.setEscalera(rs.getString("escalera"));
				dirRepresentante.setKilometro(TypeUtil.getSimpleDouble(rs,"kilometro"));
				dirRepresentante.setNombreEntidadMenor(rs.getString("entidad_menor"));

				String idProv="00";
				long codProv=TypeUtil.getSimpleLong(rs,"codigo_provincia_ine");
				if(codProv != -1)
					idProv = String.valueOf(codProv);

				String idMun= DEFAULT_CODIGO_MUNICIPIO_DGC;
				long codMun=TypeUtil.getSimpleLong(rs,"codigo_municipio_ine");
				if(codMun!=-1)
					idMun = String.valueOf(codMun);

				idProv = GeopistaFunctionUtils.completarConCeros(idProv, 2);

				idMun = GeopistaFunctionUtils.completarConCeros(idMun, 3);

				String idMunicipio = idProv + idMun;

				sSQL = "select nombreoficial, id_provincia from municipios where id=" + idMunicipio;
				psMunicipio= conn.prepareStatement(sSQL);
				rsMunicipio= psMunicipio.executeQuery();
				if(rsMunicipio.next()){
					//Sale de la tabla de municipios
					dirRepresentante.setNombreMunicipio(rsMunicipio.getString("nombreoficial"));
					dirRepresentante.setProvinciaINE(rsMunicipio.getString("id_provincia"));
					dirRepresentante.setNombreProvincia(getNombreProvinciaById(rsMunicipio.getString("id_provincia")));
				}

				dirRepresentante.setMunicipioINE(idMun);
				if(dirRepresentante.getCodigoMunicipioDGC() == null || dirRepresentante.getCodigoMunicipioDGC().equals(""))
					dirRepresentante.setCodigoMunicipioDGC(idMun);


				dirRepresentante.setPlanta(rs.getString("planta"));
				dirRepresentante.setPrimeraLetra(rs.getString("primera_letra"));
				dirRepresentante.setPuerta(rs.getString("puerta"));
				dirRepresentante.setSegundaLetra(rs.getString("segunda_letra"));
				dirRepresentante.setSegundoNumero(TypeUtil.getSimpleInteger(rs,"segundo_numero"));

				representante.setDomicilio(dirRepresentante);
				representante.setNif(rs.getString("nifrepresentante"));
				representante.setRazonSocial(rs.getString("razonsocial_representante"));

			}
			return representante;

		} catch (SQLException e) {
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{psMunicipio.close();}catch(Exception e){};
			try{rsMunicipio.close();}catch(Exception e){};
			try{psVia.close();}catch(Exception e){};
			try{rsVia.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}

	}

	/**
	 * Metodo que devuelve una lista de titulares que tienen asociado la referencia de expediente pasada por parametro
	 * y el bien inmueble.
	 *
	 * @param referenciaExpediente La referencia del expediente.
	 * @param id_bienInmueble El identificador del bien inmueble buscado.
	 * @return ArrayList La lista con los resultados.
	 * @throws SQLException
	 * */
	private ArrayList getTitular(String referenciaExpediente, String id_bienInmueble) throws SQLException
	{
		ArrayList lstTitulares = new ArrayList();

		if (id_bienInmueble.length()<20){
			GeopistaFunctionUtils.completarConCeros(id_bienInmueble, 20);
		}
		String sSQL = "select * from persona p,derechos d where d.id_bieninmueble='" + id_bienInmueble + "'" +
		" and d.niftitular=p.nif and p.parcela_catastral='" + id_bienInmueble.substring(0, 14)+ "'" +
		" and p.numero_cargo='" + id_bienInmueble.substring(14,18) + "'" +
		" and p.digito_control1='" + id_bienInmueble.substring(18,19) + "'" +
		" and p.digito_control2='" + id_bienInmueble.substring(19,20) + "'";

		PreparedStatement ps = null;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement psVia= null;
		ResultSet rsVia= null;
		PreparedStatement psMunicipio= null;
		ResultSet rsMunicipio= null;      


		try{
			conn = CPoolDatabase.getConnection();
			ps = conn.prepareStatement(sSQL);
			rs = ps.executeQuery();

			while (rs.next())
			{
				String nif = rs.getString("nif");
				String nifcb = rs.getString("nif_cb");

				if (!nif.startsWith("E") ||
						(nif.startsWith("E") && nifcb != null && 
								nif.equalsIgnoreCase(nifcb))){

					Titular titular = new Titular();

					titular.setAusenciaNIF(rs.getString("ausencia_nif"));
					titular.setNif(nif);
					titular.setNifCb(nifcb);

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String s_fecha_alteracion = null;
					if (rs.getDate("fecha_alteracion")!=null){
						s_fecha_alteracion = sdf.format(rs.getDate("fecha_alteracion"));
					}                
					titular.setFechaAlteracion(s_fecha_alteracion);

					Derecho derecho =  new Derecho();
					derecho.setCodDerecho(rs.getString("codigo_derecho"));

					IdBienInmueble idBI = new IdBienInmueble();
					idBI.setIdBienInmueble(rs.getString("id_bieninmueble"));
					derecho.setIdBienInmueble(idBI);

					derecho.setOrdinalDerecho(TypeUtil.getSimpleInteger(rs, "ordinal_derecho"));
					derecho.setPorcentajeDerecho(TypeUtil.getSimpleFloat(rs,"porcentaje_derecho"));
					titular.setDerecho(derecho);

					titular.setNifConyuge(rs.getString("nif_conyuge"));
					titular.setComplementoTitularidad(rs.getString("complemento_titularidad"));
					titular.setRazonSocial(rs.getString("razon_social"));

					DireccionLocalizacion direccionTitular = new DireccionLocalizacion();

					int idVia = -1;
					idVia = TypeUtil.getSimpleInteger(rs,"id_via");
					if(idVia!=-1){

						sSQL = "select v.tipovianormalizadocatastro, v.nombrecatastro, v.codigocatastro from vias v where id="
							+ idVia;
						psVia= conn.prepareStatement(sSQL);
						rsVia= psVia.executeQuery();
						if(rsVia.next()){
							direccionTitular.setCodigoVia(TypeUtil.getSimpleInteger(rs,"codigocatastro"));
							direccionTitular.setTipoVia(rsVia.getString("tipovianormalizadocatastro"));
							direccionTitular.setNombreVia(rsVia.getString("nombrecatastro"));
						}
					}


					String idProv="00";
					long codProv=TypeUtil.getSimpleLong(rs,"codigo_provincia_ine");
					if(codProv!=-1)
						idProv=String.valueOf(codProv);

					String idMun=DEFAULT_CODIGO_MUNICIPIO_DGC;
					long codMun=TypeUtil.getSimpleLong(rs,"codigo_municipio_ine");
					if(codMun!=-1)
						idMun=String.valueOf(codMun);

					GeopistaFunctionUtils.completarConCeros(idMun, 3);


					idProv = GeopistaFunctionUtils.completarConCeros(idProv.trim(), 2);

					idMun = GeopistaFunctionUtils.completarConCeros(idMun.trim(), 3);					

					String idMunicipio = idProv + idMun;

					sSQL = "select nombreoficial, id_provincia from municipios where id=" + idMunicipio;
					psMunicipio= conn.prepareStatement(sSQL);
					rsMunicipio= psMunicipio.executeQuery();

					if(rsMunicipio.next()){
						direccionTitular.setNombreMunicipio(rsMunicipio.getString("nombreoficial"));
						direccionTitular.setProvinciaINE(rsMunicipio.getString("id_provincia"));
						direccionTitular.setNombreProvincia(getNombreProvinciaById(rsMunicipio.getString("id_provincia")));
					}

					direccionTitular.setMunicipioINE(idMun);

					direccionTitular.setPrimerNumero(TypeUtil.getSimpleInteger(rs,"primer_numero"));
					direccionTitular.setApartadoCorreos(TypeUtil.getSimpleInteger(rs,"apartado_correos"));
					direccionTitular.setBloque(rs.getString("bloque"));

					long codMunDgc=TypeUtil.getSimpleLong(rs,"codigo_municipio_dgc");
					if(codMunDgc != -1)
						direccionTitular.setCodigoMunicipioDGC(String.valueOf(codMunDgc));
					else
						direccionTitular.setCodigoMunicipioDGC(idMun);

					direccionTitular.setCodigoPostal(rs.getString("codigo_postal"));
					direccionTitular.setDireccionNoEstructurada(rs.getString("direccion_no_estructurada"));
					direccionTitular.setEscalera(rs.getString("escalera"));
					direccionTitular.setKilometro(TypeUtil.getSimpleDouble(rs,"kilometro"));

					direccionTitular.setNombreEntidadMenor(rs.getString("entidad_menor"));
					direccionTitular.setPlanta(rs.getString("planta"));
					direccionTitular.setPrimeraLetra(rs.getString("primera_letra"));
					direccionTitular.setPuerta(rs.getString("puerta"));
					direccionTitular.setSegundaLetra(rs.getString("segunda_letra"));
					direccionTitular.setSegundoNumero(TypeUtil.getSimpleInteger(rs,"segundo_numero"));

					titular.setDomicilio(direccionTitular);

					lstTitulares.add(titular);
				}
			}
			return lstTitulares;

		}
		catch (SQLException e) {
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{psMunicipio.close();}catch(Exception e){};
			try{rsMunicipio.close();}catch(Exception e){};
			try{psVia.close();}catch(Exception e){};
			try{rsVia.close();}catch(Exception e){};                
			try{conn.close();}catch(Exception e){};
		}

	}

	/**
	 * Metodo que devuelve una lista de Comunidades de Bienes que tienen asociado la referencia de expediente pasada por parametro
	 * y el bien inmueble.
	 *
	 * @param referenciaExpediente La referencia del expediente.
	 * @param id_bienInmueble El identificador del bien inmueble buscado.
	 * @return ArrayList La lista con los resultados.
	 * @throws SQLException
	 * */
	private ArrayList getComunidadBienes(String referenciaExpediente, String id_bienInmueble) throws SQLException
	{
		ArrayList lstComunidadBienes = null;

		if (id_bienInmueble.length()<20)
			GeopistaFunctionUtils.completarConCeros(id_bienInmueble, 20);

		String sSQL = "select * from persona p where p.parcela_catastral='" 
			+ id_bienInmueble.substring(0, 14)+ "'" +
			" and p.numero_cargo='" + id_bienInmueble.substring(14,18) + "'" +
			" and p.digito_control1='" + id_bienInmueble.substring(18,19) + "'" +
			" and p.digito_control2='" + id_bienInmueble.substring(19,20) + "'";

		PreparedStatement ps = null;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement psVia= null;
		ResultSet rsVia= null;
		PreparedStatement psMunicipio= null;
		ResultSet rsMunicipio= null;      


		try{
			conn = CPoolDatabase.getConnection();
			ps = conn.prepareStatement(sSQL);
			rs = ps.executeQuery();

			while (rs.next())
			{
				String nif = rs.getString("nif");

				if (nif != null && nif.startsWith("E")){

					ComunidadBienes comunidadBienes = new ComunidadBienes();
					comunidadBienes.setAusenciaNIF(rs.getString("ausencia_nif"));
					comunidadBienes.setNif(rs.getString("nif"));

					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					String s_fecha_alteracion = null;
					if (rs.getDate("fecha_alteracion")!=null)
						s_fecha_alteracion = sdf.format(rs.getDate("fecha_alteracion"));

					comunidadBienes.setFechaAlteracion(s_fecha_alteracion);					

					comunidadBienes.setRazonSocial(rs.getString("razon_social"));

					DireccionLocalizacion direccionCB = new DireccionLocalizacion();

					int idVia = -1;
					idVia = TypeUtil.getSimpleInteger(rs, "id_via");
					if(idVia!=-1){
						sSQL = "select v.tipovianormalizadocatastro, v.nombrecatastro, v.codigocatastro from vias v where codigocatastro="
							+ idVia;
						psVia= conn.prepareStatement(sSQL);
						rsVia= psVia.executeQuery();
						if(rsVia.next()){
							direccionCB.setCodigoVia(TypeUtil.getSimpleInteger(rs,"codigocatastro"));
							direccionCB.setTipoVia(rsVia.getString("tipovianormalizadocatastro"));
							direccionCB.setNombreVia(rsVia.getString("nombrecatastro"));
						}
					}

					String idProv = rs.getString("codigo_provincia_ine");
					String idMun= rs.getString("codigo_municipio_ine");

					while(idMun.length()<3)
						idMun= "0" + idMun;

					if(idProv == null)
						idProv = "00";

					String idMunicipio = idProv + idMun;
					sSQL = "select nombreoficial, id_provincia from municipios where id=" + idMunicipio;
					psMunicipio= conn.prepareStatement(sSQL);
					rsMunicipio= psMunicipio.executeQuery();
					if(rsMunicipio.next()){
						direccionCB.setNombreMunicipio(rsMunicipio.getString("nombreoficial"));
						direccionCB.setProvinciaINE(rsMunicipio.getString("id_provincia"));
						direccionCB.setNombreProvincia(getNombreProvinciaById(rsMunicipio.getString("id_provincia")));
					}

					direccionCB.setMunicipioINE(idMun);

					direccionCB.setPrimerNumero(TypeUtil.getSimpleInteger(rs,"primer_numero"));
					direccionCB.setApartadoCorreos(TypeUtil.getSimpleInteger(rs, "apartado_correos"));
					direccionCB.setBloque(rs.getString("bloque"));

					direccionCB.setCodigoMunicipioDGC(rs.getString("codigo_municipio_dgc"));
					if(direccionCB.getCodigoMunicipioDGC() == null || direccionCB.getCodigoMunicipioDGC().equals(""))
						direccionCB.setCodigoMunicipioDGC(idMun);

					direccionCB.setCodigoPostal(rs.getString("codigo_postal"));

					direccionCB.setDireccionNoEstructurada(rs.getString("direccion_no_estructurada"));
					direccionCB.setEscalera(rs.getString("escalera"));
					direccionCB.setKilometro(TypeUtil.getSimpleDouble(rs, "kilometro"));


					direccionCB.setNombreEntidadMenor(rs.getString("entidad_menor"));
					direccionCB.setPlanta(rs.getString("planta"));
					direccionCB.setPrimeraLetra(rs.getString("primera_letra"));
					direccionCB.setPuerta(rs.getString("puerta"));
					direccionCB.setSegundaLetra(rs.getString("segunda_letra"));
					direccionCB.setSegundoNumero(TypeUtil.getSimpleInteger(rs, "segundo_numero"));

					comunidadBienes.setDomicilio(direccionCB);

					comunidadBienes.setComplementoTitularidad(rs.getString("complemento_titularidad"));
					comunidadBienes.setBienInmueble(new BienInmuebleCatastro());
					comunidadBienes.getBienInmueble().getIdBienInmueble().setIdBienInmueble(id_bienInmueble);

					if(lstComunidadBienes == null)
						lstComunidadBienes = new ArrayList();

					lstComunidadBienes.add(comunidadBienes);
				}
			}
			return lstComunidadBienes;

		}
		catch (SQLException e) {
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{psMunicipio.close();}catch(Exception e){};
			try{rsMunicipio.close();}catch(Exception e){};
			try{psVia.close();}catch(Exception e){};
			try{rsVia.close();}catch(Exception e){};                
			try{conn.close();}catch(Exception e){};
		}

	}

	/**
	 * Metodo que devuelve el codigo de la entidad generadora buscada por id.
	 *
	 * @param idEntidadGeneradora El identificador de la entidad generadora.
	 * @return int El codigo de la entidad.
	 * @throws SQLException
	 * */
	private int getCodigoEntidadGeneradora(String idEntidadGeneradora) throws SQLException
	{
		int codigo = 0;
		String sSQL= "select codigo from entidad_generadora where id_entidad_generadora="+idEntidadGeneradora;
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				codigo = rs.getInt("codigo");
			}
			return codigo;
		}
		catch (SQLException e) {
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	/**
	 * Metodo que que devuelve el codigo de delegacion de un expediente.
	 *
	 * @param refExpediente El identificador del expediente.
	 * @return int El codigo de delegacion devuelto.
	 * @throws SQLException
	 * */
	private int getCodDelegacionDGC(String refExpediente) throws SQLException
	{
		String idEntidadGeneradora = null;
		String sSQL= "select id_entidad_generadora from expediente where referencia_expediente_admin_origen='"+refExpediente+"'";
//		String sSQL= "select id_entidad_generadora from expediente where id_expediente="+refExpediente;
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				idEntidadGeneradora = rs.getString("id_entidad_generadora");
			}
			return getCodigoEntidadGeneradora(idEntidadGeneradora);
		}
		catch (SQLException e) {
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	/**
	 * Completa el BI con toda las información para poder generar en XML y guardarlo en la BBDD.
	 * @param exp Expediente.
	 * @return Expediente El expediente devuelto.
	 * @throws Exception 
	 */
	private Expediente completarBienInmuebleCatastroExpedienteDB(Expediente exp, String idMunicipio) throws Exception
	{
		ArrayList lstBienesInmuebles= new ArrayList();

		String sSQL= "select b.*, m.* , e.id_dialogo from " +
		"Expediente_BienInmueble e,Bien_Inmueble b, parcelas p , municipios m where e.id_expediente="
		+exp.getIdExpediente()+" and e.id_BienInmueble=b.identificador" +
		" AND b.parcela_catastral=p.referencia_catastral and p.id_municipio=m.id";

		PreparedStatement ps= null;
		PreparedStatement psVia= null;
		Connection conn= null;
		ResultSet rs= null;
		ResultSet rsVia= null;

		try {
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();

			while(rs.next()){

				BienInmuebleCatastro bien = new BienInmuebleCatastro();
				IdBienInmueble idBI = new IdBienInmueble();
				idBI.setIdBienInmueble(rs.getString("identificador"));
				bien.setIdBienInmueble(idBI);
				bien.setClaseBienInmueble(rs.getString("clase_bieninmueble"));

				Expediente expedienteBI = new Expediente();
				EntidadGeneradora entidadGeneradora = new EntidadGeneradora();
				entidadGeneradora.setCodigo(getCodDelegacionDGC(rs.getString("referencia_expediente")));
				expedienteBI.setEntidadGeneradora(entidadGeneradora);
				bien.setDatosExpediente(expedienteBI);
				
				if(rs.getString("id_dialogo") == null ){
					bien.setIdentificadorDialogo("");
				}
				else{
					bien.setIdentificadorDialogo(rs.getString("id_dialogo"));
				}

				NumeroFincaRegistral numFincaRegistral = new NumeroFincaRegistral(rs.getString("numero_fincaregistral"));
				bien.setNumFincaRegistral(numFincaRegistral);

				DatosEconomicosBien datosEconomicosBien =  new DatosEconomicosBien();
				datosEconomicosBien.setCoefParticipacion(TypeUtil.getFloat(rs,"coeficiente_propiedad"));
				datosEconomicosBien.setIndTipoPropiedad(rs.getString("tipo_propiedad"));
				datosEconomicosBien.setNumOrdenHorizontal(rs.getString("numero_orden_horizontal"));
				datosEconomicosBien.setOrigenPrecioDeclarado(rs.getString("origen_precio"));
				datosEconomicosBien.setPrecioDeclarado(TypeUtil.getDouble(rs,"precio_declarado"));

				datosEconomicosBien.setPrecioVenta(TypeUtil.getDouble(rs,"precio_venta"));
				datosEconomicosBien.setUso(rs.getString("clave_uso_dgc"));
				bien.setDatosEconomicosBien(datosEconomicosBien);

				DatosBaseLiquidableBien datosBLB = new DatosBaseLiquidableBien();
				datosBLB.setValorBase(TypeUtil.getDouble(rs,"valor_base"));

				datosBLB.setProcedenciaValorBase(rs.getString("procedencia_valorbase"));
				bien.setDatosBaseLiquidable(datosBLB);

				DireccionLocalizacion dir = new DireccionLocalizacion();

				int idVia = -1;
				idVia = TypeUtil.getSimpleInteger(rs,"id_via");
				if(idVia!=-1){
					sSQL = "select v.tipovianormalizadocatastro, v.nombrecatastro, v.codigocatastro from vias v where id="
						+ idVia+ " and id_municipio="+idMunicipio;
					psVia= conn.prepareStatement(sSQL);
					rsVia= psVia.executeQuery();

					if(rsVia.next()){
						dir.setCodigoVia(TypeUtil.getSimpleInteger(rsVia,"codigocatastro"));
						dir.setTipoVia(rsVia.getString("tipovianormalizadocatastro"));
						dir.setNombreVia(rsVia.getString("nombrecatastro"));
					}
				}

				dir.setBloque(rs.getString("bloque"));

				int valorCodigoPostal=TypeUtil.getSimpleInteger(rs,"codigo_postal");
				if(valorCodigoPostal!=-1)
					dir.setCodigoPostal(String.valueOf(valorCodigoPostal));


				dir.setCodMunOrigenAgregacion(rs.getString("municipio_origen_agregacion"));
				dir.setCodParaje(rs.getString("paraje"));
				dir.setCodParcela(rs.getString("parcela"));
				dir.setCodPoligono(rs.getString("poligono_rustico"));
				dir.setCodZonaConcentracion(rs.getString("zona_concentracion"));
				dir.setDireccionNoEstructurada(rs.getString("direccion_no_estructurada"));
				dir.setDistrito(rs.getString("distrito"));
				dir.setEscalera(rs.getString("escalera"));
				dir.setIdVia(TypeUtil.getSimpleInteger(rs,"id_via"));
				dir.setKilometro(TypeUtil.getSimpleDouble(rs,"kilometro"));
				dir.setNombreEntidadMenor(rs.getString("nombre_entidad_menor"));

				//Se saca de la tabla municipios
				dir.setProvinciaINE(rs.getString("id_provincia"));
				dir.setMunicipioINE(rs.getString("id_ine"));
				dir.setNombreMunicipio(rs.getString("nombreoficial"));

				bien.setCodMunicipioDGC(rs.getString("codigo_municipiodgc"));
				if(bien.getCodMunicipioDGC() == null || bien.getCodMunicipioDGC().equals(""))
					bien.setCodMunicipioDGC(rs.getString("id_catastro"));
				if(bien.getCodMunicipioDGC() == null || bien.getCodMunicipioDGC().equals(""))
					bien.setCodMunicipioDGC(dir.getMunicipioINE());
				if(bien.getCodMunicipioDGC() == null || bien.getCodMunicipioDGC().equals(""))
					bien.setCodMunicipioDGC(DEFAULT_CODIGO_MUNICIPIO_DGC);

				dir.setCodigoMunicipioDGC(bien.getCodMunicipioDGC());

				dir.setNombreParaje(rs.getString("nombre_paraje"));
				dir.setNombreProvincia(getNombreProvinciaById(rs.getString("id_provincia")));
				dir.setPlanta(rs.getString("planta"));
				dir.setPrimeraLetra(rs.getString("primera_letra"));
				dir.setPuerta(rs.getString("puerta"));
				dir.setSegundaLetra(rs.getString("segunda_letra"));
				dir.setSegundoNumero(TypeUtil.getSimpleInteger(rs,"segundo_numero"));
				bien.setDomicilioTributario(dir);

				bien.setNombreEntidadMenor(rs.getString("nombre_entidad_menor"));
				bien.setNumFijoInmueble(TypeUtil.getInteger(rs,"numero_fijo_inmueble"));
				NumeroFincaRegistral numeroFincaRegistral = new NumeroFincaRegistral(rs.getString("numero_fincaregistral"));
				bien.setNumFincaRegistral(numeroFincaRegistral);

				Persona representante = getRepresentante(rs.getString("id_representante"), rs.getString("identificador"));
				bien.setRepresentante(representante);

				bien.setTipoMovimiento(BienInmuebleCatastro.TIPO_MOVIMIENTO_VARIACIONES_TITULARIDAD);

				ArrayList lstTitulares = getTitular(rs.getString("referencia_expediente"),rs.getString("identificador"));

				ArrayList lstComunidadBienes = getComunidadBienes(rs.getString("referencia_expediente"),rs.getString("identificador"));

				BienInmuebleJuridico bij = new BienInmuebleJuridico();
				bij.setBienInmueble(bien);             
				bij.setLstTitulares(lstTitulares);
				bij.setLstComBienes(lstComunidadBienes);
				lstBienesInmuebles.add(bij);
			}
			exp.setListaReferencias(lstBienesInmuebles);
			return exp;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{psVia.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{rsVia.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	private ArrayList getLstSuelos(FincaCatastro finca, String idMunicipio) throws Exception {
		ArrayList lstSuelos = new ArrayList();
		String sSQL= "select * from suelo where parcela_catastral='"+finca.getRefFinca().getRefCatastral()+"'";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;

		try {
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next()){

				SueloCatastro suelo = new SueloCatastro();

				suelo.setRefParcela(finca.getRefFinca());
				suelo.setNumOrden(rs.getString("numero_orden"));

				suelo.setCodDelegacion(finca.getCodDelegacionMEH());
				suelo.setCodMunicipioDGC(finca.getCodMunicipioDGC());

				DatosFisicosSuelo datosSuelo = new DatosFisicosSuelo();
				datosSuelo.setLongFachada(TypeUtil.getFloat(rs, "longitud_fachada"));
				datosSuelo.setTipoFachada(rs.getString("tipo_fachada"));
				datosSuelo.setSupOcupada(TypeUtil.getLong(rs,"superficie_elemento_suelo"));
				suelo.setDatosFisicos(datosSuelo);

				DatosEconomicosSuelo datosEconomicosSuelo = new DatosEconomicosSuelo();
				datosEconomicosSuelo.setCodViaPonencia(rs.getString("codigo_via_ponencia"));
				datosEconomicosSuelo.setCodTramoPonencia(rs.getString("codigo_tramo_ponencia"));

				if (datosEconomicosSuelo.getTramos() != null && datosEconomicosSuelo.getTramos().getCodTramo()!=null && !datosEconomicosSuelo.getTramos().getCodTramo().equals("")){
					
					//Obtener PonenciaTramos
					PonenciaTramos tramo = null;
					String codTramoPonencia = rs.getString("codigo_tramo_ponencia");
					tramo = obtenerPonenciaTramo(codTramoPonencia, idMunicipio);
					if (tramo != null){
						datosEconomicosSuelo.setTramos(tramo);
					}
				}
				
				if (rs.getString("zona_valor")!=null && !rs.getString("zona_valor").equals("")){

					//Obtener la PonenciaZonaValor
					PonenciaZonaValor zonaValor = null;
					String codZonaValor = rs.getString("zona_valor");
					zonaValor = obtenerPonenciaZonaValor(codZonaValor, idMunicipio);
					if (zonaValor != null){
						datosEconomicosSuelo.setZonaValor(zonaValor);
					}
				}

				if (rs.getString("zona_urbanistica")!=null && !rs.getString("zona_urbanistica").equals("")){
					
					//Obtener la PonenciaUrbanistica
					PonenciaUrbanistica ponenciaUrbanistica = null;
					String codZonaUrbanistica = rs.getString("zona_urbanistica");
					ponenciaUrbanistica = obtenerPonenciaUrbanistica(codZonaUrbanistica, idMunicipio);
					if (ponenciaUrbanistica != null){
						datosEconomicosSuelo.setZonaUrbanistica(ponenciaUrbanistica);
					}
				}
				

				datosEconomicosSuelo.setCodTipoValor(rs.getString("codigo_tipo_valor"));
				datosEconomicosSuelo.setNumFachadas(rs.getString("numero_fachadas"));
				datosEconomicosSuelo.setCorrectorLongFachada(new Boolean(rs.getBoolean("corrector_longitud_fachada")));
				datosEconomicosSuelo.setCorrectorFormaIrregular(new Boolean(rs.getBoolean("corrector_forma_irregular")));
				datosEconomicosSuelo.setCorrectorDesmonte(new Boolean(rs.getBoolean("corrector_desmonte_excesivo")));
				datosEconomicosSuelo.setCorrectorSupDistinta(new Boolean(rs.getString("corrector_superficie_distinta")));
				datosEconomicosSuelo.setCorrectorInedificabilidad(new Boolean(rs.getBoolean("corrector_inedificabilidad")));
				datosEconomicosSuelo.setCorrectorVPO(new Boolean(rs.getBoolean("corrector_vpo")));

				if(CPoolDatabase.isPostgres(conn)){
					datosEconomicosSuelo.setCorrectorDeprecFuncional(new Boolean(rs.getBoolean("corrector_depreciacion_funcional")));
					datosEconomicosSuelo.setCorrectorSitEspeciales(new Boolean(rs.getBoolean("corrector_situaciones_especiales")));
					datosEconomicosSuelo.setCorrectorAprecDeprec(TypeUtil.getFloat(rs,"corrector_apreciacion_depreciacion"));

				}
				else{
					datosEconomicosSuelo.setCorrectorDeprecFuncional(new Boolean(rs.getBoolean("corrector_depreciacion_func")));
					datosEconomicosSuelo.setCorrectorSitEspeciales(new Boolean(rs.getBoolean("corrector_situaciones_espec")));
					datosEconomicosSuelo.setCorrectorAprecDeprec(TypeUtil.getFloat(rs,"corrector_apreciacion_deprec"));                    
				}

				datosEconomicosSuelo.setCorrectorCargasSingulares(TypeUtil.getFloat(rs,"corrector_cargas_singulares"));
				datosEconomicosSuelo.setCorrectorNoLucrativo(new Boolean(rs.getBoolean("corrector_uso_no_lucrativo")));
				suelo.setDatosEconomicos(datosEconomicosSuelo);

				if (finca.getCodMunicipioDGC()!=null && !finca.getCodMunicipioDGC().equals("")){
					suelo.setCodMunicipioDGC(finca.getCodMunicipioDGC());
				}
				if (finca.getCodDelegacionMEH()!=null && !finca.getCodDelegacionMEH().equals("")){
					suelo.setCodDelegacion(finca.getCodDelegacionMEH());
				}

				lstSuelos.add(suelo);
			}
			return lstSuelos;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}

	}

	private ArrayList getUnidadConstructiva(FincaCatastro finca, String idMunicipio) throws Exception
	{
		ArrayList lstUnidadesConstructivas = new ArrayList();
		String sSQL= "select u.*, m.* from unidad_constructiva u, parcelas p, municipios m where u.parcela_catastral='"
			+finca.getRefFinca().getRefCatastral()+"' and u.parcela_catastral=p.referencia_catastral and p.id_municipio=m.id";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		PreparedStatement psVia= null;
		ResultSet rsVia= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				UnidadConstructivaCatastro unidadConstructiva = new UnidadConstructivaCatastro();
				unidadConstructiva.setRefParcela(finca.getRefFinca());
				unidadConstructiva.setCodDelegacionMEH(finca.getCodDelegacionMEH());

				unidadConstructiva.setCodUnidadConstructiva(rs.getString("codigo_unidad_constructiva"));
				unidadConstructiva.setTipoUnidad(rs.getString("clase_unidad"));

				unidadConstructiva.setCodMunicipioDGC(rs.getString("codigo_municipio_dgc"));
				if(unidadConstructiva.getCodMunicipioDGC() == null || unidadConstructiva.getCodMunicipioDGC().equals(""))
					unidadConstructiva.setCodMunicipioDGC(finca.getCodMunicipioDGC());


				DireccionLocalizacion dir = new DireccionLocalizacion();

				dir.setProvinciaINE(rs.getString("id_provincia"));
				dir.setMunicipioINE(rs.getString("codigo_municipio_ine"));
				dir.setCodigoMunicipioDGC(rs.getString("codigo_municipio_dgc"));
				if(dir.getCodigoMunicipioDGC() == null || dir.getCodigoMunicipioDGC().equals(""))
					dir.setCodigoMunicipioDGC(finca.getCodMunicipioDGC());

				dir.setNombreProvincia(getNombreProvinciaById(rs.getString("id_provincia")));
				dir.setNombreMunicipio(rs.getString("nombreoficial"));
				dir.setNombreEntidadMenor(rs.getString("nombre_entidad_menor"));

				int idVia = -1;
				idVia = TypeUtil.getSimpleInteger(rs, "id_via");
				if(idVia!=-1){

					sSQL = "select v.tipovianormalizadocatastro, v.nombrecatastro, v.codigocatastro from vias v where id="
						+ idVia+ " and id_municipio="+idMunicipio;
					psVia= conn.prepareStatement(sSQL);
					rsVia= psVia.executeQuery();
					if(rsVia.next()){
						dir.setCodigoVia(TypeUtil.getSimpleInteger(rs,"codigocatastro"));
						dir.setTipoVia(rsVia.getString("tipovianormalizadocatastro"));
						dir.setNombreVia(rsVia.getString("nombrecatastro"));
					}
				}

				dir.setPrimerNumero(TypeUtil.getSimpleInteger(rs,"primer_numero"));
				dir.setPrimeraLetra(rs.getString("primera_letra"));
				dir.setSegundaLetra(rs.getString("segunda_letra"));

				dir.setSegundoNumero(TypeUtil.getSimpleInteger(rs,"segundo_numero"));
				dir.setKilometro(TypeUtil.getSimpleDouble(rs,"kilometro"));

				dir.setDireccionNoEstructurada(rs.getString("direccion_no_estructurada"));
				unidadConstructiva.setDirUnidadConstructiva(dir);

				DatosFisicosUC datosFisicosUC = new DatosFisicosUC();
				datosFisicosUC.setSupOcupada( TypeUtil.getLong(rs,"superficie_ocupada"));
				datosFisicosUC.setLongFachada(TypeUtil.getFloat(rs,"longitud_fachada"));
				datosFisicosUC.setAnioConstruccion(TypeUtil.getInteger(rs,"anio_construccion"));
				datosFisicosUC.setIndExactitud(rs.getString("indicador_exactitud"));
				unidadConstructiva.setDatosFisicos(datosFisicosUC);

				DatosEconomicosUC datosEconomicosUC = new DatosEconomicosUC();
				datosEconomicosUC.setCodViaPonencia(rs.getString("codigo_via_ponencia"));
				
				if (rs.getString("codigo_tramo_ponencia")!=null && !rs.getString("codigo_tramo_ponencia").equals("")){
					
					//Obtener PonenciaTramos
					PonenciaTramos tramo = null;
					String codTramoPonencia = rs.getString("codigo_tramo_ponencia");
					tramo = obtenerPonenciaTramo(codTramoPonencia, idMunicipio);
					if (tramo != null){
						datosEconomicosUC.setTramoPonencia(tramo);
					}
				}
				
				if (rs.getString("zona_valor")!=null && !rs.getString("zona_valor").equals("")){

					//Obtener la PonenciaZonaValor
					PonenciaZonaValor zonaValor = null;
					String codZonaValor = rs.getString("zona_valor");
					zonaValor = obtenerPonenciaZonaValor(codZonaValor, idMunicipio);
					if (zonaValor != null){
						datosEconomicosUC.setZonaValor(zonaValor);
					}
				}
				
				datosEconomicosUC.setNumFachadas(rs.getString("numero_fachadas"));
				datosEconomicosUC.setCorrectorLongFachada(new Boolean(rs.getBoolean("corrector_longitud_fachada")));
				datosEconomicosUC.setCorrectorConservacion(rs.getString("corrector_estado_conservacion"));

				if(CPoolDatabase.isPostgres(conn)){
					datosEconomicosUC.setCorrectorDepreciacion(new Boolean(rs.getBoolean("corrector_depreciacion_funcional")));
					datosEconomicosUC.setCorrectorSitEspeciales(new Boolean(rs.getBoolean("corrector_situaciones_especiales")));
				}
				else{
					datosEconomicosUC.setCorrectorDepreciacion(new Boolean(rs.getBoolean("corrector_depreciacion_func")));
					datosEconomicosUC.setCorrectorSitEspeciales(new Boolean(rs.getBoolean("corrector_situaciones_espec")));                    
				}

				datosEconomicosUC.setCoefCargasSingulares(TypeUtil.getFloat(rs,"corrector_cargas_singulares"));
				datosEconomicosUC.setCorrectorNoLucrativo(new Boolean(rs.getBoolean("corrector_uso_no_lucrativo")));
				datosEconomicosUC.setCorrectorSitEspeciales(new Boolean(rs.getBoolean("corrector_situaciones_especiales")));
				unidadConstructiva.setDatosEconomicos(datosEconomicosUC);

				if (finca.getCodMunicipioDGC()!=null && !finca.getCodMunicipioDGC().equals("")){
					unidadConstructiva.setCodMunicipioDGC(finca.getCodMunicipioDGC());
				}
				if (finca.getCodDelegacionMEH()!=null && !finca.getCodDelegacionMEH().equals("")){
					unidadConstructiva.setCodDelegacionMEH(finca.getCodDelegacionMEH());
				}

				lstUnidadesConstructivas.add(unidadConstructiva);
			}
			return lstUnidadesConstructivas;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{psVia.close();}catch(Exception e){};
			try{rsVia.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	private ArrayList getConstrucciones(FincaCatastro finca) throws Exception
	{
		ArrayList lstConstrucciones = new ArrayList();
		String sSQL= "select * from construccion where parcela_catastral='"+finca.getRefFinca().getRefCatastral()+"'";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				ConstruccionCatastro construccionCatastro = new ConstruccionCatastro();
				construccionCatastro.setRefParcela(finca.getRefFinca());
				
				construccionCatastro.setNumOrdenConstruccion(GeopistaFunctionUtils.completarConCeros(rs.getString("numero_orden_construccion"), 4));

				construccionCatastro.setCodDelegacionMEH(finca.getCodDelegacionMEH());

				construccionCatastro.setNumOrdenBienInmueble(rs.getString("numero_orden_bieninmueble"));
				construccionCatastro.setIdConstruccion(rs.getString("identificador"));

				construccionCatastro.setCodMunicipio(finca.getCodMunicipioDGC());

				DireccionLocalizacion dir = new DireccionLocalizacion();
				dir.setBloque(rs.getString("bloque"));
				dir.setEscalera(rs.getString("escalera"));
				dir.setPlanta(rs.getString("planta"));
				dir.setPuerta(rs.getString("puerta"));
				construccionCatastro.setDomicilioTributario(dir);

				DatosFisicosConstruccion datosFisicosConstruccion = new DatosFisicosConstruccion();
				datosFisicosConstruccion.setAnioAntiguedad(TypeUtil.getInteger(rs,"anno_antiguedad"));
				datosFisicosConstruccion.setCodUnidadConstructiva(rs.getString("codigo_unidadconstructiva"));
				datosFisicosConstruccion.setCodDestino(rs.getString("codigo_destino_dgc"));
				datosFisicosConstruccion.setAnioReforma(TypeUtil.getInteger(rs, "anno_reforma"));
				datosFisicosConstruccion.setTipoReforma(rs.getString("indicador_tipo_reforma"));
				datosFisicosConstruccion.setLocalInterior(new Boolean(rs.getBoolean("indicador_local_interior")));
				datosFisicosConstruccion.setSupTotal(TypeUtil.getLong(rs,"superficie_total_local"));
				datosFisicosConstruccion.setSupTerrazasLocal(TypeUtil.getLong(rs,"superficie_terrazas_local"));
				datosFisicosConstruccion.setSupImputableLocal(TypeUtil.getLong(rs,"superficie_imputable_local"));
				construccionCatastro.setDatosFisicos(datosFisicosConstruccion);

				DatosEconomicosConstruccion datosEconomicosConstruccion =  new DatosEconomicosConstruccion();
				datosEconomicosConstruccion.setCodCategoriaPredominante(rs.getString("codigo_categoria_predominante"));
				datosEconomicosConstruccion.setCodModalidadReparto(rs.getString("codigo_modalidad_reparto"));
				datosEconomicosConstruccion.setCodTipoValor(rs.getString("codigo_tipo_valor"));
				datosEconomicosConstruccion.setCodUsoPredominante(rs.getString("codigo_uso_predominante"));

				if(CPoolDatabase.isPostgres(conn))
					datosEconomicosConstruccion.setCorrectorApreciación(TypeUtil.getFloat(rs,"corrector_apreciacion_economica"));
				else
					datosEconomicosConstruccion.setCorrectorApreciación(TypeUtil.getFloat(rs,"corrector_apreciacion_econom"));                    

				datosEconomicosConstruccion.setCorrectorVivienda(new Boolean(rs.getBoolean("corrector_vivienda")));
				datosEconomicosConstruccion.setTipoConstruccion(rs.getString("tipologia_constructiva"));
				construccionCatastro.setDatosEconomicos(datosEconomicosConstruccion);

				construccionCatastro.setTipoMovimiento(BienInmuebleCatastro.TIPO_MOVIMIENTO_FINAL);

				lstConstrucciones.add(construccionCatastro);
			}
			return lstConstrucciones;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	private ArrayList getCultivo(FincaCatastro finca) throws Exception
	{
		ArrayList lstCultivos = new ArrayList();
		String sSQL= "select * from cultivos where parcela_catastral='"+finca.getRefFinca().getRefCatastral()+"'";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				Cultivo cultivo = new Cultivo();

				IdCultivo idCultivo = new IdCultivo();
				idCultivo.setParcelaCatastral(finca.getRefFinca().getRefCatastral());
				idCultivo.setNumOrden(rs.getString("numero_orden"));
				idCultivo.setCalifCultivo(rs.getString("calificacion_cultivo"));
				cultivo.setIdCultivo(idCultivo);

				cultivo.setTipoSuelo(rs.getString("naturaleza_suelo"));
				cultivo.setCodDelegacionMEH(finca.getCodDelegacionMEH());
				cultivo.setCodMunicipioDGC(finca.getCodMunicipioDGC());

				cultivo.setCodBonificacion(rs.getString("bonificacion_subparcela"));
				cultivo.setCodModalidadReparto(rs.getString("modalidad_reparto"));
				cultivo.setCodSubparcela(rs.getString("codigo_subparcela"));   
				cultivo.setEjercicioFinBonificacion(TypeUtil.getInteger(rs, "ejercicio_finalizacion"));   
				cultivo.setIntensidadProductiva(TypeUtil.getInteger(rs,"intensidad_productiva"));
				cultivo.setSuperficieParcela(TypeUtil.getLong(rs,"superficie_subparcela"));
				cultivo.setTipoSubparcela(rs.getString("tipo_subparcela"));   

				lstCultivos.add(cultivo);   
			}
			return lstCultivos;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}

	}

	private ArrayList getImagenes(FincaCatastro finca) throws Exception{

		ArrayList lstimagenes = new ArrayList();

		String sSQL= "select * from imagenes where id_finca=" + finca.getIdFinca();
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;

		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{			
				ImagenCatastro imagen= new ImagenCatastro();
				imagen.setExtension(rs.getString("formato"));
				imagen.setNombre(rs.getString("nombre"));
				imagen.setTipoDocumento(rs.getString("tipo_documento"));
				imagen.setFoto(rs.getString("foto"));

				lstimagenes.add(imagen);
			}
			return lstimagenes;

		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	private FX_CC getFXCC(int idFinca) throws Exception
	{
		String sSQL= "select * from fxcc where id_finca="+idFinca;
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		FX_CC fxcc = null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{			
				fxcc = new FX_CC();
				fxcc.setASC(rs.getString("ASC"));
				fxcc.setDXF(rs.getString("DXF"));
			}
			return fxcc;

		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}


	private RepartoCatastro getRepartosLocales(String idConsOrigen, boolean esConstElemRep) throws Exception
	{
		String sSQL= "select * from repartosconstrucciones where id_construccionorigen='" + idConsOrigen + "'";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		RepartoCatastro reparto = null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			if(rs.next())
			{
				reparto = new RepartoCatastro();
				reparto.setIdOrigen(new ReferenciaCatastral(idConsOrigen.substring(0,14)));
				reparto.setNumOrdenConsRepartir(idConsOrigen.substring(14,idConsOrigen.length()));
				reparto.setTIPO_MOVIMIENTO(rs.getString("tipo_movimiento"));

				ElementoReparto elemRep = new ElementoReparto();
				elemRep.setId(rs.getString("id_construcciondestino"));
				elemRep.setEsConstruccion(esConstElemRep);
				if (elemRep.getExpediente()==null){
					elemRep.setExpediente(new Expediente());
					elemRep.getExpediente().setEntidadGeneradora(new EntidadGeneradora());
				}
				else if (elemRep.getExpediente().getEntidadGeneradora() == null){
					elemRep.getExpediente().setEntidadGeneradora(new EntidadGeneradora());
				}
				elemRep.getExpediente().getEntidadGeneradora().setCodigo(TypeUtil.getSimpleInteger(rs, "codigo_entidad_colaboradora"));
				elemRep.getExpediente().setReferenciaExpedienteAdminOrigen(rs.getString("referencia_expediente"));
				elemRep.getExpediente().setAnnoExpedienteAdminOrigenAlteracion(TypeUtil.getSimpleInteger(rs,"anno_expediente"));
				elemRep.setPorcentajeReparto(TypeUtil.getSimpleFloat(rs,"porcentaje_reparto"));
				if (elemRep.getId() != null && elemRep.getId().length()<18){
					elemRep.setId(GeopistaFunctionUtils.completarConCeros(elemRep.getId(), 18));
				}
				if (elemRep.getId()!=null){
					elemRep.setNumCargo(elemRep.getId().substring(14,18));
				}

				ArrayList lstLocales = new ArrayList();
				lstLocales.add(elemRep);
				reparto.setLstLocales(lstLocales);
			}
			while(rs.next())
			{                
				ElementoReparto elemRep = new ElementoReparto();
				elemRep.setId(rs.getString("id_construcciondestino"));
				elemRep.setEsConstruccion(esConstElemRep);
				if (elemRep.getExpediente()==null){
					elemRep.setExpediente(new Expediente());
					elemRep.getExpediente().setEntidadGeneradora(new EntidadGeneradora());
				}
				else if (elemRep.getExpediente().getEntidadGeneradora() == null){
					elemRep.getExpediente().setEntidadGeneradora(new EntidadGeneradora());
				}
				elemRep.getExpediente().getEntidadGeneradora().setCodigo(TypeUtil.getSimpleInteger(rs,"codigo_entidad_colaboradora"));
				elemRep.getExpediente().setReferenciaExpedienteAdminOrigen(rs.getString("referencia_expediente"));
				elemRep.getExpediente().setAnnoExpedienteAdminOrigenAlteracion(TypeUtil.getSimpleInteger(rs,"anno_expediente"));
				elemRep.setPorcentajeReparto(TypeUtil.getSimpleFloat(rs,"porcentaje_reparto"));
				if (elemRep.getId() != null && elemRep.getId().length()<18){
					elemRep.setId(GeopistaFunctionUtils.completarConCeros(elemRep.getId(), 18));
				}
				if (elemRep.getId()!=null){
					elemRep.setNumCargo(elemRep.getId().substring(14,18));
				}


				reparto.getLstLocales().add(elemRep);
			}

		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return reparto;
	}

	private RepartoCatastro getRepartosLocalBI(String idConsOrigen, boolean esConstElemRep) throws Exception
	{
		String sSQL= "select * from repartosconsbi where idconstruccion_origen='"+idConsOrigen + "'";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		RepartoCatastro reparto = null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			if(rs.next())
			{
				reparto = new RepartoCatastro();
				reparto.setIdOrigen(new ReferenciaCatastral(idConsOrigen.substring(0,14)));
				reparto.setNumOrdenConsRepartir(idConsOrigen.substring(14,idConsOrigen.length()));
				reparto.setTIPO_MOVIMIENTO(rs.getString("tipo_movimiento"));

				ElementoReparto elemRep = new ElementoReparto();
				elemRep.setId(rs.getString("id_bieninmuebledestino"));
				elemRep.setEsConstruccion(esConstElemRep);
				if (elemRep.getExpediente()==null){
					elemRep.setExpediente(new Expediente());
					elemRep.getExpediente().setEntidadGeneradora(new EntidadGeneradora());
				}
				else if (elemRep.getExpediente().getEntidadGeneradora() == null){
					elemRep.getExpediente().setEntidadGeneradora(new EntidadGeneradora());
				}
				elemRep.getExpediente().getEntidadGeneradora().setCodigo(TypeUtil.getSimpleInteger(rs, "codigo_entidad_colaboradora"));
				elemRep.getExpediente().setReferenciaExpedienteAdminOrigen(rs.getString("referencia_expediente"));
				elemRep.getExpediente().setAnnoExpedienteAdminOrigenAlteracion(TypeUtil.getSimpleInteger(rs,"anno_expediente"));
				elemRep.setPorcentajeReparto(TypeUtil.getSimpleFloat(rs,"porcentaje_reparto"));
				if (elemRep.getId() != null && elemRep.getId().length()<18){
					elemRep.setId(GeopistaFunctionUtils.completarConCeros(elemRep.getId(), 18));
				}
				if (elemRep.getId()!=null){
					elemRep.setNumCargo(elemRep.getId().substring(14,18));
				}

				ArrayList lstBienes = new ArrayList();
				lstBienes.add(elemRep);
				reparto.setLstBienes(lstBienes);
			}
			while(rs.next())
			{
				ElementoReparto elemRep = new ElementoReparto();
				elemRep.setId(rs.getString("id_bieninmuebledestino"));
				elemRep.setEsConstruccion(esConstElemRep);
				if (elemRep.getExpediente()==null){
					elemRep.setExpediente(new Expediente());
					elemRep.getExpediente().setEntidadGeneradora(new EntidadGeneradora());
				}
				else if (elemRep.getExpediente().getEntidadGeneradora() == null){
					elemRep.getExpediente().setEntidadGeneradora(new EntidadGeneradora());
				}
				elemRep.getExpediente().getEntidadGeneradora().setCodigo(TypeUtil.getSimpleInteger(rs, "codigo_entidad_colaboradora"));
				elemRep.getExpediente().setReferenciaExpedienteAdminOrigen(rs.getString("referencia_expediente"));
				elemRep.getExpediente().setAnnoExpedienteAdminOrigenAlteracion(TypeUtil.getSimpleInteger(rs,"anno_expediente"));
				elemRep.setPorcentajeReparto(TypeUtil.getSimpleFloat(rs,"porcentaje_reparto"));
				if (elemRep.getId() != null && elemRep.getId().length()<18){
					elemRep.setId(GeopistaFunctionUtils.completarConCeros(elemRep.getId(), 18));
				}
				if (elemRep.getId()!=null){
					elemRep.setNumCargo(elemRep.getId().substring(14,18));
				}

				reparto.getLstBienes().add(elemRep);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return reparto;
	}


	private RepartoCatastro getRepartoCultivo(IdCultivo idCultivo, boolean esConstElemRep) throws Exception
	{
		String sSQL= "select * from repartoscultivos where id_cultivoorigen='" + idCultivo.getIdCultivo() + "'";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		RepartoCatastro reparto = null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			if(rs.next())
			{			
				reparto = new RepartoCatastro();
				reparto.setIdOrigen(idCultivo.getParcelaCatastral());
				reparto.setCalifCatastralElementoRepartir(idCultivo.getCalifCultivo());
				reparto.setTIPO_MOVIMIENTO(rs.getString("tipo_movimiento"));

				ElementoReparto elemRep = new ElementoReparto();
				elemRep.setId(rs.getString("id_bieninmuebledestino"));
				elemRep.setEsConstruccion(esConstElemRep);
				if (elemRep.getExpediente()==null){
					elemRep.setExpediente(new Expediente());
					elemRep.getExpediente().setEntidadGeneradora(new EntidadGeneradora());
				}
				else if (elemRep.getExpediente().getEntidadGeneradora() == null){
					elemRep.getExpediente().setEntidadGeneradora(new EntidadGeneradora());
				}
				elemRep.getExpediente().getEntidadGeneradora().setCodigo(TypeUtil.getSimpleInteger(rs, "codigo_entidad_colaboradora"));
				elemRep.getExpediente().setReferenciaExpedienteAdminOrigen(rs.getString("referencia_expediente"));
				elemRep.getExpediente().setAnnoExpedienteAdminOrigenAlteracion(TypeUtil.getSimpleInteger(rs,"anno_expediente"));
				elemRep.setPorcentajeReparto(TypeUtil.getSimpleFloat(rs,"porcentaje_reparto"));
				if (elemRep.getId() != null && elemRep.getId().length()<18){
					elemRep.setId(GeopistaFunctionUtils.completarConCeros(elemRep.getId(), 18));
				}
				if (elemRep.getId()!=null){
					elemRep.setNumCargo(elemRep.getId().substring(14,18));
				}

				ArrayList lstBienes = new ArrayList();
				lstBienes.add(elemRep);
				reparto.setLstBienes(lstBienes);
			}
			while(rs.next())
			{
				ElementoReparto elemRep = new ElementoReparto();
				elemRep.setId(rs.getString("id_bieninmuebledestino"));
				elemRep.setEsConstruccion(esConstElemRep);
				if (elemRep.getExpediente()==null){
					elemRep.setExpediente(new Expediente());
					elemRep.getExpediente().setEntidadGeneradora(new EntidadGeneradora());
				}
				else if (elemRep.getExpediente().getEntidadGeneradora() == null){
					elemRep.getExpediente().setEntidadGeneradora(new EntidadGeneradora());
				}
				elemRep.getExpediente().getEntidadGeneradora().setCodigo(TypeUtil.getSimpleInteger(rs,"codigo_entidad_colaboradora"));
				elemRep.getExpediente().setReferenciaExpedienteAdminOrigen(rs.getString("referencia_expediente"));
				elemRep.getExpediente().setAnnoExpedienteAdminOrigenAlteracion(TypeUtil.getSimpleInteger(rs,"anno_expediente"));
				elemRep.setPorcentajeReparto(TypeUtil.getSimpleFloat(rs,"porcentaje_reparto"));
				if (elemRep.getId() != null && elemRep.getId().length()<18){
					elemRep.setId(GeopistaFunctionUtils.completarConCeros(elemRep.getId(), 18));
				}
				if (elemRep.getId()!=null){
					elemRep.setNumCargo(elemRep.getId().substring(14,18));
				}

				reparto.getLstBienes().add(elemRep);
			}


		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return reparto;
	}


	private ArrayList getRepartos(FincaCatastro finca) throws Exception
	{
		ArrayList repartos = new ArrayList();

		//Repartos de la consturcciones
		if (finca.getLstConstrucciones() != null)
		{
			ArrayList lstConstrucciones = finca.getLstConstrucciones();
			for (int i=0;i<lstConstrucciones.size();i++)
			{
				ConstruccionCatastro construccion = (ConstruccionCatastro) lstConstrucciones.get(i);
				if (construccion.getDatosEconomicos().getCodModalidadReparto() != null)
				{
					boolean esConstElemRep = (construccion.getDatosEconomicos().getCodModalidadReparto().startsWith("AL")
							|| construccion.getDatosEconomicos().getCodModalidadReparto().startsWith("TL")
							&&!(construccion.getDatosEconomicos().getCodModalidadReparto().startsWith("AC")
									|| construccion.getDatosEconomicos().getCodModalidadReparto().startsWith("TC")));
					//La construccion es común, se reparte
					if (construccion.getDatosEconomicos().getCodModalidadReparto().startsWith("AC")
							||construccion.getDatosEconomicos().getCodModalidadReparto().startsWith("TC"))
					{
						//Se reparte entre cargo
						RepartoCatastro reparto = getRepartosLocalBI(construccion.getIdConstruccion(), esConstElemRep);
						if (reparto != null){
							reparto.setTipoReparto(construccion.getDatosEconomicos().getCodModalidadReparto().substring(0,2));
							repartos.add(reparto);
						}
					}
					if (construccion.getDatosEconomicos().getCodModalidadReparto().startsWith("AL") ||
							construccion.getDatosEconomicos().getCodModalidadReparto().startsWith("TL"))
					{
						//Se reparte entre locales
						RepartoCatastro reparto = getRepartosLocales(construccion.getIdConstruccion(), esConstElemRep);
						if (reparto!=null){
							reparto.setTipoReparto(construccion.getDatosEconomicos().getCodModalidadReparto().substring(0,2));                        
							repartos.add(reparto);
						}
					}
				}
			}
		}

		//Repartos de los cultivos
		if (finca.getLstCultivos() != null)
		{
			ArrayList lstCultivos = finca.getLstCultivos();
			for (int i=0;i<lstCultivos.size();i++)
			{
				Cultivo cultivo = (Cultivo) lstCultivos.get(i);
				if (cultivo.getCodModalidadReparto() != null)
				{
					//El cultivo se reparte
					boolean esConstElemRep = (cultivo.getCodModalidadReparto().startsWith("AL") || cultivo.getCodModalidadReparto().startsWith("TL")
							&&!(cultivo.getCodModalidadReparto().startsWith("AC") || cultivo.getCodModalidadReparto().startsWith("TC")));
					RepartoCatastro reparto = getRepartoCultivo(cultivo.getIdCultivo(), esConstElemRep);
					if (reparto != null){
						reparto.setCodSubparcelaElementoRepartir(cultivo.getCodSubparcela());
						reparto.setTipoReparto(cultivo.getCodModalidadReparto().substring(0,2));
						repartos.add(reparto);
					}
				}
			}
		}

		return repartos;

	}

	private ArrayList getLstBIs(FincaCatastro finca, String idMunicipio)  throws Exception {
		ArrayList lstBIs = new ArrayList();
		String sSQL;
		PreparedStatement ps= null;
		PreparedStatement psVia= null;
		Connection conn= null;
		ResultSet rs= null;
		ResultSet rsVia= null;

		try{
			conn= CPoolDatabase.getConnection();
			if(CPoolDatabase.isPostgres(conn))
				sSQL ="select b.*, m.* "+
				" from bien_inmueble b, parcelas p, municipios m where"+
				" b.identificador like  ('"+finca.getRefFinca().getRefCatastral()+"' || '%') " +
				" AND b.parcela_catastral=p.referencia_catastral and p.id_municipio=m.id"+
				" order by b.identificador asc";
			else
				sSQL ="select b.*, m.*  "+
				" from bien_inmueble b, parcelas p, municipios m where b.identificador like " +
				" concat('"+finca.getRefFinca().getRefCatastral()+"','%') " +
				" AND b.parcela_catastral=p.referencia_catastral and p.id_municipio=m.id"+
				" order by b.identificador asc";

			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next()){

				BienInmuebleCatastro bien = new BienInmuebleCatastro();
				IdBienInmueble idBI = new IdBienInmueble();
				idBI.setIdBienInmueble(rs.getString("identificador"));
				bien.setIdBienInmueble(idBI);

				bien.setClaseBienInmueble(rs.getString("clase_bieninmueble"));
				Expediente expedienteBI = new Expediente();
				EntidadGeneradora entidadGeneradora = new EntidadGeneradora();
				entidadGeneradora.setCodigo(getCodDelegacionDGC(rs.getString("referencia_expediente")));
				expedienteBI.setEntidadGeneradora(entidadGeneradora);
				bien.setDatosExpediente(expedienteBI);

				bien.setCodMunicipioDGC(rs.getString("codigo_municipiodgc"));
				if(bien.getCodMunicipioDGC() == null || bien.getCodMunicipioDGC().equals(""))
					bien.setCodMunicipioDGC(finca.getCodMunicipioDGC());

				bien.setIdAyuntamientoBienInmueble(rs.getString("identificacion_bieninmueble"));

				NumeroFincaRegistral numFincaRegistral = new NumeroFincaRegistral(rs.getString("numero_fincaregistral"));
				bien.setNumFincaRegistral(numFincaRegistral);

				DatosEconomicosBien datosEconomicosBien =  new DatosEconomicosBien();
				datosEconomicosBien.setCoefParticipacion(TypeUtil.getFloat(rs,"coeficiente_propiedad"));
				datosEconomicosBien.setIndTipoPropiedad(rs.getString("tipo_propiedad"));
				datosEconomicosBien.setNumOrdenHorizontal(rs.getString("numero_orden_horizontal"));
				datosEconomicosBien.setOrigenPrecioDeclarado(rs.getString("origen_precio"));
				datosEconomicosBien.setPrecioDeclarado(TypeUtil.getDouble(rs,"precio_declarado"));
				datosEconomicosBien.setPrecioVenta(TypeUtil.getDouble(rs,"precio_venta"));
				datosEconomicosBien.setAnioFinValoracion(TypeUtil.getInteger(rs,"anno_finalizacion_valoracion"));
				datosEconomicosBien.setUso(rs.getString("clave_uso_dgc"));
				bien.setDatosEconomicosBien(datosEconomicosBien);

				DatosBaseLiquidableBien datosBLB = new DatosBaseLiquidableBien();
				datosBLB.setValorBase(TypeUtil.getDouble(rs,"valor_base"));
				datosBLB.setProcedenciaValorBase(rs.getString("procedencia_valorbase"));
				bien.setDatosBaseLiquidable(datosBLB);

				DireccionLocalizacion dir = new DireccionLocalizacion();

				int idVia = -1;
				idVia = TypeUtil.getSimpleInteger(rs,"id_via");
				if(idVia!=-1){
					sSQL = "select v.tipovianormalizadocatastro, v.nombrecatastro, v.codigocatastro from vias v where id="
						+ idVia+ " and id_municipio="+idMunicipio;
					psVia= conn.prepareStatement(sSQL);
					rsVia= psVia.executeQuery();
					if(rsVia.next()){
						dir.setCodigoVia(TypeUtil.getSimpleInteger(rsVia,"codigocatastro"));
						dir.setTipoVia(rsVia.getString("tipovianormalizadocatastro"));
						dir.setNombreVia(rsVia.getString("nombrecatastro"));
					}
				}

				dir.setBloque(rs.getString("bloque"));

				dir.setCodigoMunicipioDGC(rs.getString("codigo_municipiodgc"));
				if(dir.getCodigoMunicipioDGC() == null || dir.getCodigoMunicipioDGC().equals(""))
					dir.setCodigoMunicipioDGC(bien.getCodMunicipioDGC());

				int valorCodigoPostal=TypeUtil.getSimpleInteger(rs,"codigo_postal");
				if(valorCodigoPostal!=-1)
					dir.setCodigoPostal(String.valueOf(valorCodigoPostal));

				dir.setCodMunOrigenAgregacion(rs.getString("municipio_origen_agregacion"));
				dir.setCodParaje(rs.getString("paraje"));
				dir.setCodParcela(rs.getString("parcela"));
				dir.setCodPoligono(rs.getString("poligono_rustico"));
				dir.setCodZonaConcentracion(rs.getString("zona_concentracion"));
				dir.setDireccionNoEstructurada(rs.getString("direccion_no_estructurada"));
				dir.setDistrito(rs.getString("distrito"));
				dir.setEscalera(rs.getString("escalera"));
				dir.setIdVia(TypeUtil.getSimpleInteger(rs,"id_via"));
				dir.setKilometro(TypeUtil.getSimpleDouble(rs,"kilometro"));
				dir.setNombreEntidadMenor(rs.getString("nombre_entidad_menor"));

				//Se recogen de la tabla municipios
				dir.setProvinciaINE(rs.getString("id_provincia"));
				dir.setMunicipioINE(rs.getString("id_ine"));
				dir.setNombreMunicipio(rs.getString("nombreoficial"));

				dir.setNombreParaje(rs.getString("nombre_paraje"));
				dir.setNombreProvincia(getNombreProvinciaById(rs.getString("id_provincia")));
				dir.setPlanta(rs.getString("planta"));
				dir.setPrimeraLetra(rs.getString("primera_letra"));
				dir.setPuerta(rs.getString("puerta"));
				dir.setSegundaLetra(rs.getString("segunda_letra"));
				dir.setSegundoNumero(TypeUtil.getSimpleInteger(rs,"segundo_numero"));

				bien.setDomicilioTributario(dir);

				bien.setNombreEntidadMenor(rs.getString("nombre_entidad_menor"));
				bien.setNumFijoInmueble(TypeUtil.getInteger(rs,"numero_fijo_inmueble"));
				NumeroFincaRegistral numeroFincaRegistral = new NumeroFincaRegistral(rs.getString("numero_fincaregistral"));
				bien.setNumFincaRegistral(numeroFincaRegistral);

				Persona representante = getRepresentante(rs.getString("id_representante"), rs.getString("identificador"));
				bien.setRepresentante(representante);

				bien.setTipoMovimiento(BienInmuebleCatastro.TIPO_MOVIMIENTO_FINAL);

				ArrayList lstTitulares = getTitular(rs.getString("referencia_expediente"),rs.getString("identificador"));

				ArrayList lstComunidadBienes = getComunidadBienes(rs.getString("referencia_expediente"),rs.getString("identificador"));

				BienInmuebleJuridico bij = new BienInmuebleJuridico();
				bij.setBienInmueble(bien);
				bij.setLstTitulares(lstTitulares);
				bij.setLstComBienes(lstComunidadBienes);
				lstBIs.add(bij);
			}

		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{psVia.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{rsVia.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return lstBIs;
	}

	/**
	 * Metodo que actualiza los identificadores de dialogo en la tabla expediente_finca_catastro o expediente_bieninmueble
	 *
	 * @param oos Objeto donde se escribe el resultado
	 * @param exp El expediente a actualizar
	 * @throws Exception Las Excepciones registradas
	 */
	public void updateIdentificadoresDialogo(ObjectOutputStream oos, ArrayList lstIdentificadorDialogo, Expediente exp)throws Exception{
		try{
			if(lstIdentificadorDialogo!=null && !lstIdentificadorDialogo.isEmpty()){
				for (int i = 0; i < lstIdentificadorDialogo.size(); i++) {
					if(((IdentificadorDialogo)lstIdentificadorDialogo.get(i)).getFincaBien() instanceof FincaCatastro){
						updateIdentificadorDialogoExpedienteFincaCatastroBD((IdentificadorDialogo)lstIdentificadorDialogo.get(i), exp, false);
					}
					else if(((IdentificadorDialogo)lstIdentificadorDialogo.get(i)).getFincaBien() instanceof BienInmuebleCatastro){
						updateIdentificadorDialogoExpedienteBienInmuebleBD((IdentificadorDialogo)lstIdentificadorDialogo.get(i), exp, false);
					}
					
				}
				
			}

		}
		catch(Exception e)
		{
			logger.error("updateExpediente: "+ e.getMessage());
			oos.writeObject(new ACException(e));
			throw e;
		}
	}
	
	private Expediente completarFincaCatastroExpedienteDB(Expediente exp, String idMunicipioFinca) throws Exception{

		ArrayList lstFincas = new ArrayList();
		
		String sSQL = "select * from expediente_finca_catastro e left join parcelas p " +
		"on p.referencia_catastral=e.ref_catastral and p.fecha_baja is null where e.id_expediente=" 
		+ exp.getIdExpediente() + " and p.id_municipio='" + idMunicipioFinca + "'";

		PreparedStatement ps= null;
		PreparedStatement psVia= null;
		PreparedStatement psMunicipio= null;
		PreparedStatement psProvincia= null;
		Connection conn= null;
		ResultSet rs= null;
		ResultSet rsVia= null;
		ResultSet rsMunicipio= null;
		ResultSet rsProvincia= null;

		try {
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next()) {

				FincaCatastro finca = new FincaCatastro();
				finca.setBICE(rs.getString("bice"));
				ReferenciaCatastral refFinca = new ReferenciaCatastral(rs.getString("referencia_catastral"));
				finca.setRefFinca(refFinca);
				finca.setIdFinca(TypeUtil.getSimpleInteger(rs,"id"));
				if(rs.getString("id_dialogo") == null ){
					finca.setIdentificadorDialogo("");
				}
				else{
					finca.setIdentificadorDialogo(rs.getString("id_dialogo"));
				}

				DireccionLocalizacion dir = new DireccionLocalizacion();

				//Si no hay via, en BD se tiene un 0 (causa: importador de parcelas)
				int idVia = rs.getInt("id_via");
				if(idVia != 0){

					sSQL = "select v.tipovianormalizadocatastro, v.nombrecatastro, v.codigocatastro from vias v where codigocatastro="
						+ idVia+ " and id_municipio="+idMunicipioFinca;

					psVia= conn.prepareStatement(sSQL);
					rsVia= psVia.executeQuery();

					if(rsVia.next()) {
						dir.setCodigoVia(TypeUtil.getSimpleInteger(rsVia,"codigocatastro"));
						dir.setTipoVia(rsVia.getString("tipovianormalizadocatastro"));
						dir.setNombreVia(rsVia.getString("nombrecatastro"));
					}
				}

				int idMunicipio = rs.getInt("id_municipio");//es NOT NULL en la BD
				String idM = GeopistaFunctionUtils.completarConCeros(String.valueOf(idMunicipio), 5);               

				sSQL = "select nombreoficial from provincias where id="+ idM.substring(0,2);
				psProvincia= conn.prepareStatement(sSQL);
				rsProvincia= psProvincia.executeQuery();
				if(rsProvincia.next())
					dir.setNombreProvincia(rsProvincia.getString("nombreoficial"));

				//Si en la tabla de parcelas no tenemos el codigo, se le asigna el correspondiente al INE.
				finca.setCodDelegacionMEH(rs.getString("codigodelegacionmeh"));
				if(finca.getCodDelegacionMEH() == null || finca.getCodDelegacionMEH().equals(""))
					finca.setCodDelegacionMEH(idM.substring(0,2));

				sSQL = "select nombreoficial, id_provincia, id_ine, id_catastro from municipios where id="+ idMunicipio;
				psMunicipio= conn.prepareStatement(sSQL);
				rsMunicipio= psMunicipio.executeQuery();
				if(rsMunicipio.next()){
					dir.setNombreMunicipio(rsMunicipio.getString("nombreoficial"));
					dir.setProvinciaINE(rsMunicipio.getString("id_provincia"));
					dir.setMunicipioINE(rsMunicipio.getString("id_ine"));

					finca.setCodMunicipioDGC(rs.getString("codigo_municipiodgc"));
					if(finca.getCodMunicipioDGC() == null || finca.getCodMunicipioDGC().equals(""))
						finca.setCodMunicipioDGC(rsMunicipio.getString("id_catastro"));
					if(finca.getCodMunicipioDGC() == null || finca.getCodMunicipioDGC().equals(""))
						finca.setCodMunicipioDGC(dir.getMunicipioINE());
					if(finca.getCodMunicipioDGC() == null || finca.getCodMunicipioDGC().equals(""))
						finca.setCodMunicipioDGC(DEFAULT_CODIGO_MUNICIPIO_DGC);

					dir.setCodigoMunicipioDGC(finca.getCodMunicipioDGC());
					if (exp.getDireccionPresentador().getCodigoMunicipioDGC() == null || exp.getDireccionPresentador().getCodigoMunicipioDGC().equals("")){
						exp.getDireccionPresentador().setCodigoMunicipioDGC(finca.getCodMunicipioDGC());
					}
				}

				dir.setNombreEntidadMenor(rs.getString("nombreentidadmenor"));
				dir.setPrimerNumero(TypeUtil.getSimpleInteger(rs,"primer_numero"));
				dir.setPrimeraLetra(rs.getString("primera_letra"));
				dir.setSegundoNumero(TypeUtil.getSimpleInteger(rs,"segundo_numero"));
				dir.setSegundaLetra(rs.getString("segunda_letra"));
				dir.setKilometro(TypeUtil.getSimpleDouble(rs, "kilometro"));
				dir.setDireccionNoEstructurada(rs.getString("direccion_no_estructurada"));
				dir.setBloque(rs.getString("bloque"));

				int valorCodigoPostal=TypeUtil.getSimpleInteger(rs,"codigo_postal");
				if(valorCodigoPostal!=-1)
					dir.setCodigoPostal(String.valueOf(valorCodigoPostal));

				dir.setDistrito(rs.getString("id_distrito"));
				dir.setCodMunOrigenAgregacion(rs.getString("codigo_municipio_origendgc"));
				dir.setCodZonaConcentracion(rs.getString("codigozonaconcentracion"));
				dir.setCodPoligono(rs.getString("codigopoligono"));
				dir.setCodParcela(rs.getString("codigoparcela"));
				dir.setNombreParaje(rs.getString("nombreparaje"));
				dir.setCodParaje(rs.getString("codigo_paraje"));

				finca.setDirParcela(dir);

				DatosFisicosFinca datosFisicos = new DatosFisicosFinca();
				datosFisicos.setSupFinca(TypeUtil.getLong(rs, "superficie_finca"));
				datosFisicos.setSupTotal(TypeUtil.getLong(rs,"superficie_construida_total"));
				datosFisicos.setSupSobreRasante(TypeUtil.getLong(rs,"superficie_const_sobrerasante"));
				datosFisicos.setSupBajoRasante(TypeUtil.getLong(rs,"superficie_const_bajorasante"));
				datosFisicos.setSupCubierta(TypeUtil.getLong(rs,"superficie_cubierta"));
				datosFisicos.setXCoord(TypeUtil.getFloat(rs,"coordenada_x"));
				datosFisicos.setYCoord(TypeUtil.getFloat(rs,"coordenada_y"));

				String geometry = rs.getString("GEOMETRY");
				if(geometry != null && !geometry.equalsIgnoreCase("") &&
						( datosFisicos.getXCoord() != null || datosFisicos.getYCoord() != null ))
					datosFisicos.setSRS(geometry.substring(5, 10));

				finca.setDatosFisicos(datosFisicos);

				//Datos economicos
				DatosEconomicosFinca datosEconomicosFinca = new DatosEconomicosFinca();
				datosEconomicosFinca.setAnioAprobacion(TypeUtil.getInteger(rs,"anio_aprobacion"));
				datosEconomicosFinca.setCodigoCalculoValor(TypeUtil.getInteger(rs,"codigo_calculo_valor"));
				datosEconomicosFinca.setIndInfraedificabilidad(rs.getString("indicador_infraedificabilidad"));
				datosEconomicosFinca.setIndModalidadReparto(rs.getString("modalidad_reparto"));

				//Ponencia de poligono
				PonenciaPoligono poligonoCatastralValor = new PonenciaPoligono();
				poligonoCatastralValor.setCodPoligono(rs.getString("poligono_catastral_valoracion"));
				datosEconomicosFinca.setPoligonoCatastralValor(poligonoCatastralValor);
				finca.setDatosEconomicos(datosEconomicosFinca);

				ArrayList lstBIs = getLstBIs(finca, idMunicipioFinca);
				finca.setLstBienesInmuebles(lstBIs);

				ArrayList lstSuelos = getLstSuelos(finca, idMunicipioFinca);
				finca.setLstSuelos(lstSuelos);

				ArrayList lstUnidadesConstructivas = getUnidadConstructiva(finca, idMunicipioFinca);
				finca.setLstUnidadesConstructivas(lstUnidadesConstructivas);

				ArrayList lstConstrucciones = getConstrucciones(finca);
				finca.setLstConstrucciones(lstConstrucciones);

				ArrayList lstCultivos = getCultivo(finca);
				finca.setLstCultivos(lstCultivos);

				ArrayList lstRepartos = getRepartos(finca);
				finca.setLstReparto(lstRepartos);

				finca.setFxcc(getFXCC(rs.getInt("id")));//el id es NOT NULL en la BD

				ArrayList lstImagenes = getImagenes(finca);
				finca.setLstImagenes(lstImagenes);

				lstFincas.add(finca);
			}
			exp.setListaReferencias(lstFincas);
			return exp;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{psVia.close();}catch(Exception e){};
			try{psProvincia.close();}catch(Exception e){};
			try{psMunicipio.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{rsVia.close();}catch(Exception e){};
			try{rsMunicipio.close();}catch(Exception e){};
			try{rsProvincia.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}

	}

	/**
	 * Metodo que elimina las entradas de catastro temporal del expediente pasado por parametro.
	 *
	 * @param expediente El expediente.
	 * @throws Exception
	 * */
	private void eliminaCatastroTemporalExpBD(Expediente expediente) throws Exception
	{
		PreparedStatement ps= null;
		Connection conn= null;

		try
		{
			String queryUpdate= "delete from catastro_temporal where id_expediente=" + expediente.getIdExpediente();
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(queryUpdate);
			ps.execute();
			conn.commit();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	/**
	 * Metodo que devuelve la fecha del ultimo envio periodico a catastro.
	 *
	 * @return Date El resultado.
	 * @throws Exception
	 * */
	private Date getFechaInicioPeriodoExpBD(String idMunicipio) throws Exception {

		String sSQL= "select ultima_fecha_envio from configuracion " +
		"where id_municipio = 0 or id_municipio = " +idMunicipio+" order by id_municipio desc";

		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		Date fechaIniPe=null;
		try{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();

			if(rs.next())
				fechaIniPe = rs.getDate("ultima_fecha_envio");

		}
		catch (Exception e){
			throw e;
		}
		finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return fechaIniPe;
	}

	/**
	 * Metodo que actualiza el estado de los expedientes finalizados despues de la exportacion masiva.
	 *
	 * @param expedientes Los expedientes entre los que se mirar cual tiene que ser actualizado.
	 * @throws Exception
	 * */
	private void actualizaExpFinalizadosDespuesExportacionBD(ArrayList expedientes, String modoTrabajo) throws Exception {
		PreparedStatement ps= null;
		Connection conn= null;

		try{
			String queryUpdate="update expediente set id_estado="+ConstantesRegExp.ESTADO_GENERADO
			+" where id_estado="+ ConstantesRegExp.ESTADO_FINALIZADO
			+ " and id_expediente=?";
			conn= CPoolDatabase.getConnection();

			for(int i=0;i<expedientes.size();i++){

				if(!modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO) && ((Expediente)expedientes.get(i)).getIdEstado()==ConstantesRegExp.ESTADO_FINALIZADO){
					ps= conn.prepareStatement(queryUpdate);
					ps.setLong(1,((Expediente)expedientes.get(i)).getIdExpediente());
					ps.execute();
					conn.commit();
				}
			}

		}
		catch (Exception e){
			throw e;
		}
		finally{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	/**
	 * Metodo que devuelve todos los usuarios de las aplicaciones geopista.
	 *
	 * @return Hashtable Hash con el resultado, la key es el nombre y el value es el id de usuario.
	 * @throws Exception
	 * */
	private Hashtable getTodosLosUsuariosBD() throws Exception
	{
		Hashtable aux= new Hashtable();
		String sSQL= "select iu.id,iu.name from iuseruserhdr iu group by iu.name,iu.id";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				aux.put(String.valueOf(rs.getString("name")), String.valueOf(rs.getInt("id")));
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return aux;
	}

	/**
	 * Metodo que busca todas las referencias catastrales rusticas en parcelas que coincidan con el
	 * patron de codigo de poligo y codigo de parcela pasado por parametro.
	 *
	 * @param idMunicipio El id del municipio con el que se esta trabajando.
	 * @param patronPoligono El patron del poligono.
	 * @param patronParcela El patron de la parcela.
	 * @return Collection ArrayList con objetos FincaCatastro resultado.
	 * @throws Exception
	 * */
	private Collection getFincaCatastroRusticasBuscadasPorPoligonoDB(String idMunicipio,String patronPoligono,
			String patronParcela) throws Exception
			{
		ArrayList aux= new ArrayList();
		String sSQL= "select distinct parcelas.id_via, parcelas.primer_numero, parcelas.referencia_catastral," +
		" parcelas.codigopoligono, parcelas.codigoparcela" +
		" from parcelas where parcelas.referencia_catastral IS NOT NULL and parcelas.id_municipio= '"+ idMunicipio+"'"+
		" and (parcelas.codigopoligono is not null and (parcelas.codigopoligono like upper('%"+patronPoligono+"%')))"+
		" and (parcelas.codigoparcela is not null and (parcelas.codigoparcela like upper('%"+patronParcela+"%')))" +
		" order by parcelas.codigopoligono asc";

		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				FincaCatastro finca = new FincaCatastro();
				ReferenciaCatastral refCatas = new ReferenciaCatastral(rs.getString("referencia_catastral"));
				finca.setRefFinca(refCatas);
				DireccionLocalizacion dir = new DireccionLocalizacion();
				dir.setCodPoligono(rs.getString("codigopoligono"));
				dir.setCodParcela(rs.getString("codigoparcela"));
				dir.setPrimerNumero(TypeUtil.getSimpleInteger(rs,"primer_numero"));
				int idVia = -1;
				idVia = TypeUtil.getSimpleInteger(rs,"id_via");
				ResultSet rsVia= null;
				try
				{
					if(idVia!=-1){
						sSQL = "select vias.tipovianormalizadocatastro, vias.nombrecatastro from vias where codigocatastro=" + idVia
						+ " and id_municipio="+Integer.parseInt(idMunicipio);;
						ps= conn.prepareStatement(sSQL);
						rsVia= ps.executeQuery();
						if(rsVia.next())
						{
							dir.setNombreVia(rsVia.getString("nombrecatastro"));
							dir.setTipoVia(rsVia.getString("tipovianormalizadocatastro"));
						}//fin if
					}//fin if
				}
				catch (Exception e)
				{
					throw e;
				}
				finally
				{
					try{rsVia.close();}catch(Exception e){};
				}
				finca.setDirParcela(dir);
				aux.add(finca);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return aux;
			}

	/**
	 * Metodo que busca todas las referencias catastrales en bien_inmueble que coincidan en la via y el tipo de via
	 * con el patron de via y tipo via pasado por parametro.
	 *
	 * @param idMunicipio El id del municipio con el que se esta trabajando.
	 * @param patronPoligono El patron del poligono rustico.
	 * @param patronParcela El patron de la parcela.
	 * @return Collection ArrayList con objetos BienInmueble resultado.
	 * @throws Exception
	 * */
	private Collection getBienInmuebleCatastroRusticoBuscadosPorPoligonoDB(String idMunicipio,String patronPoligono,
			String patronParcela) throws Exception
			{
		ArrayList aux= new ArrayList();
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		try
		{
			String sSQL;
			conn= CPoolDatabase.getConnection();
			if(CPoolDatabase.isPostgres(conn))
			{
				sSQL ="select distinct bien_inmueble.identificador, bien_inmueble.poligono_rustico, bien_inmueble.parcela,"+
				" bien_inmueble.id_via, bien_inmueble.primer_numero"+
				" from bien_inmueble, parcelas where parcelas.referencia_catastral"+
				" IS NOT NULL and parcelas.id_municipio='"+ idMunicipio+"'"+
				" and (bien_inmueble.poligono_rustico  is not null and bien_inmueble.poligono_rustico" +
				" like upper('%" + patronPoligono + "%'))"+
				" and (bien_inmueble.parcela is not null and bien_inmueble.parcela" +
				" like upper('%" + patronParcela + "%'))"+
				" and bien_inmueble.identificador like  parcelas.referencia_catastral || '%'"+
				" order by bien_inmueble.poligono_rustico asc";
			}
			else
			{
				sSQL ="select distinct bien_inmueble.identificador, bien_inmueble.poligono_rustico, bien_inmueble.parcela"+
				" from bien_inmueble, parcelas where parcelas.referencia_catastral"+
				" IS NOT NULL and parcelas.id_municipio='"+ idMunicipio+"'"+
				" and (bien_inmueble.poligono_rustico  is not null and bien_inmueble.poligono_rustico" +
				" like upper('%" + patronPoligono + "%'))"+
				" and (bien_inmueble.parcela is not null and bien_inmueble.parcela" +
				" like upper('%" + patronParcela + "%'))"+
				" and bien_inmueble.identificador like concat(parcelas.referencia_catastral,'%')" +
				" order by bien_inmueble.poligono_rustico asc";
			}
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next())
			{
				BienInmuebleCatastro bien = new BienInmuebleCatastro();
				IdBienInmueble idBI = new IdBienInmueble();
				idBI.setIdBienInmueble(rs.getString("identificador"));
				bien.setIdBienInmueble(idBI);

				DireccionLocalizacion dir = new DireccionLocalizacion();
				dir.setCodPoligono(rs.getString("poligono_rustico"));
				dir.setCodParcela(rs.getString("parcela"));
				dir.setPrimerNumero(TypeUtil.getSimpleInteger(rs, "primer_numero"));
				int idVia = -1;
				idVia = TypeUtil.getSimpleInteger(rs,"id_via");
				ResultSet rsVia= null;
				try
				{
					if(idVia!=-1){
						sSQL = "select vias.tipovianormalizadocatastro, vias.nombrecatastro from vias where codigocatastro=" + idVia
						+ " and id_municipio="+Integer.parseInt(idMunicipio);;
						ps= conn.prepareStatement(sSQL);
						rsVia= ps.executeQuery();
						if(rsVia.next())
						{
							dir.setNombreVia(rsVia.getString("nombrecatastro"));
							dir.setTipoVia(rsVia.getString("tipovianormalizadocatastro"));
						}//fin if
					}//fin if
				}
				catch (Exception e)
				{
					throw e;
				}
				finally
				{
					try{rsVia.close();}catch(Exception e){};
				}
				bien.setDomicilioTributario(dir);
				aux.add(bien);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return aux;
			}


	public FincaCatastro existeParcelaEnBD(String refCatastral)throws Exception
	{
		String sSQL= "SELECT id FROM parcelas WHERE referencia_catastral=?";

		FincaCatastro finca = null;
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs = null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			ps.setString(1,refCatastral);

			rs= ps.executeQuery();
			if(rs.next())
			{
				finca = new FincaCatastro();
				finca.setIdFinca(rs.getInt("id"));
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return finca;
	}

	public BienInmuebleCatastro existeBIEnBD(String idBI)throws Exception
	{
		String sSQL= "SELECT identificador FROM bien_inmueble WHERE identificador=?";

		BienInmuebleCatastro bi = null;
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs = null;
		try
		{
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			ps.setString(1,idBI);

			rs= ps.executeQuery();
			if(rs.next())
			{
				bi = new BienInmuebleCatastro();
				IdBienInmueble id = new IdBienInmueble();
				id.setIdBienInmueble(rs.getString(1));
				bi.setIdBienInmueble(id);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return bi;
	}


	//Operaciones para realizar la importación del fichero fin de salida
	///////////////////////////////////////////////////////////////////////

	public static Set familiasModificadas = new HashSet(); 

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

	private ArrayList lstValores = new ArrayList();
	private ArrayList lstTipos = new ArrayList();

	private static Connection connection = null;

	private Connection getDBConnection(){

		if (connection == null){
			connection = CPoolDatabase.getConnection();
		}
		return connection;
	}
	
	private void releaseConnection() throws SQLException{

		if (connection != null){
			connection.close();
			connection = null;
		}
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

				} catch (DataException e)
				{    
					throw new DataException(e);

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

				s = conn.prepareStatement("select nifrepresentante from representante where id_bieninmueble=?");            
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
						s = conn.prepareStatement("update representante set nifrepresentante=?, anno_expediente=?, " +
								"referencia_expediente=?, codigo_entidad_colaboradora=?, razonsocial_representante=?, " +
								"codigo_provincia_ine=?,codigo_municipio_dgc=?, codigo_municipio_ine=?, entidad_menor=?," +
								" id_via=?, primer_numero=?, primera_letra=?, segundo_numero=?, segunda_letra=?, kilometro=?, " +
								"bloque=?, escalera=?, planta=?, puerta=?, direccion_no_estructurada=?, codigo_postal=?, " +
						"apartado_correos=? where id_bieninmueble=? and nifrepresentante=?");

						lstValores.add(bij.getBienInmueble().getRepresentante().getNif());
						lstTipos.add(TYPE_VARCHAR);
					}
					else
						s = conn.prepareStatement("insert into representante (anno_expediente, referencia_expediente, " +
								"codigo_entidad_colaboradora, razonsocial_representante, codigo_provincia_ine, " +
								"codigo_municipio_dgc, codigo_municipio_ine, entidad_menor, id_via, primer_numero, " +
								"primera_letra, segundo_numero, segunda_letra, kilometro, bloque, escalera, planta, " +
								"puerta, direccion_no_estructurada, codigo_postal, apartado_correos, id_bieninmueble, " +
						"nifrepresentante) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

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
				s = conn.prepareStatement("select identificador from bien_inmueble where identificador=?");
				s.setString(1,bij.getBienInmueble().getIdBienInmueble().getIdBienInmueble());
				r = s.executeQuery();
				if (r.next())
					idBI = r.getString(1);

				r=null;
				s=null;    

				if(idBI!=null)
					s = conn.prepareStatement("update bien_inmueble set anno_expediente=?, referencia_expediente=?, " +
							"codigo_entidad_generadora=?,tipo_movimiento=?, clase_bieninmueble=?, parcela_catastral=?, " +
							"numero_cargo=?, primer_caracter_control=?, segundo_caracter_control=?, numero_fijo_inmueble=?, " +
							"identificacion_bieninmueble=?, numero_fincaregistral=?, codigo_municipiodgc=?, nombre_entidad_menor=?, " +
							"id_via=?, primer_numero=?, primera_letra=?, segundo_numero=?, segunda_letra=?, kilometro=?, bloque=?, " +
							"escalera=?, planta=?, puerta=?, direccion_no_estructurada=?, codigo_postal=?, distrito=?," +
							" municipio_origen_agregacion=?, zona_concentracion=?, poligono_rustico=?, parcela=?, paraje=?, " +
							"nombre_paraje=?, precio_declarado=?, origen_precio=?, precio_venta=?, anno_finalizacion_valoracion=?, " +
							"tipo_propiedad=?, numero_orden_horizontal=?, anno_antiguedad=?, anio_valor_catastral=?, valor_catastral=?, " +
							"valor_catastral_suelo=?, valor_catastral_construccion=?, base_liquidable=?, clave_uso_dgc=?, importe_bonificacion=?, " +
							"clave_bonificacion=?, superficie_elementos_constructivos=?, superficie_finca=?, coeficiente_propiedad=?, " +
							"valor_base=?, procedencia_valorbase=?, ejercicio_ibi=?, valor_catastral_ibi=?, ejercicio_revision=?, " +
					"ejercicio_revision_parcial=?, periodo_total=?, id_representante=? where identificador=?");

				else
					s = conn.prepareStatement("insert into bien_inmueble(anno_expediente, referencia_expediente, " +
							"codigo_entidad_generadora,tipo_movimiento, clase_bieninmueble, parcela_catastral, " +
							"numero_cargo, primer_caracter_control, segundo_caracter_control, numero_fijo_inmueble, " +
							"identificacion_bieninmueble, numero_fincaregistral, codigo_municipiodgc, " +
							"nombre_entidad_menor, id_via, primer_numero, primera_letra, segundo_numero, segunda_letra, " +
							"kilometro, bloque, escalera, planta, puerta, direccion_no_estructurada, codigo_postal, " +
							"distrito, municipio_origen_agregacion, zona_concentracion, poligono_rustico, parcela, " +
							"paraje, nombre_paraje, precio_declarado, origen_precio, precio_venta, anno_finalizacion_valoracion, " +
							"tipo_propiedad, numero_orden_horizontal, anno_antiguedad, anio_valor_catastral, valor_catastral, " +
							"valor_catastral_suelo, valor_catastral_construccion, base_liquidable, clave_uso_dgc, " +
							"importe_bonificacion, clave_bonificacion, superficie_elementos_constructivos, superficie_finca, " +
							"coeficiente_propiedad, valor_base, procedencia_valorbase, ejercicio_ibi, valor_catastral_ibi, " +
							"ejercicio_revision, ejercicio_revision_parcial, periodo_total, id_representante, identificador) " +
					"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); 


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
				lstTipos.add(TYPE_DOUBLE); 

				lstValores.add(bij.getBienInmueble().getDatosBaseLiquidable().getProcedenciaValorBase());            
				lstTipos.add(TYPE_VARCHAR); 

				lstValores.add(bij.getBienInmueble().getDatosBaseLiquidable().getEjercicioIBI());            
				lstTipos.add(TYPE_NUMERIC); 

				lstValores.add(bij.getBienInmueble().getDatosBaseLiquidable().getValorCatastralIBI());            
				lstTipos.add(TYPE_DOUBLE); 



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
			s = conn.prepareStatement("delete from persona where parcela_catastral=? and numero_cargo=? and digito_control1=? and digito_control2=?");
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
			s = conn.prepareStatement("delete from derechos where id_bieninmueble=?");
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
				s = conn.prepareStatement("select nif from persona where nif=? and parcela_catastral=? and numero_cargo=? and digito_control1=? and digito_control2=?");
				s.setString(1, pers.getNif());
				s.setString(2,idBi.getParcelaCatastral().getRefCatastral());
				s.setString(3,idBi.getNumCargo());
				s.setString(4,idBi.getDigControl1());
				s.setString(5,idBi.getDigControl2());

				r = s.executeQuery();
				if (r.next())
				{
					s=null;
					s = conn.prepareStatement("update persona set anno_expediente=?, referencia_expediente=?, codigo_entidad_colaboradora=?," +
							" razon_social=?, ausencia_nif=?, codigo_provincia_ine=?, codigo_municipio_dgc=?, codigo_municipio_ine=?, " +
							"entidad_menor=?, id_via=?, primer_numero=?, primera_letra=?, segundo_numero=?, segunda_letra=?, escalera=?, " +
							"kilometro=?, bloque=?, planta=?, puerta=?, direccion_no_estructurada=?, codigo_postal=?, apartado_correos=?, " +
							"nif_conyuge=?, nif_cb=?, complemento_titularidad=?, fecha_alteracion=? where nif=? and parcela_catastral=? " +
					"and numero_cargo=? and digito_control1=? and digito_control2=?"); 
				}
				else
				{
					s=null;
					if (pers.getAusenciaNIF()==null){
						s = conn.prepareStatement("insert into persona (anno_expediente, referencia_expediente, codigo_entidad_colaboradora, " +
								"razon_social, ausencia_nif, codigo_provincia_ine, codigo_municipio_dgc, codigo_municipio_ine, entidad_menor, " +
								"id_via, primer_numero, primera_letra, segundo_numero, segunda_letra, escalera, kilometro, bloque, planta, " +
								"puerta, direccion_no_estructurada, codigo_postal, apartado_correos, nif_conyuge, nif_cb, " +
								"complemento_titularidad, fecha_alteracion, nif, parcela_catastral, numero_cargo, digito_control1, " +
						"digito_control2) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					}
					else{
						Long nifAuxiliar = null;
						s = conn.prepareStatement("select nextval('seq_ausencia_nif')");
						r = s.executeQuery();
						if (r.next()){
							nifAuxiliar = new Long(r.getLong(1));    
							pers.setNif(nifAuxiliar.toString());
						}
						s=null;
						r=null;
						s = conn.prepareStatement("insert into persona (anno_expediente, referencia_expediente, codigo_entidad_colaboradora, " +
								"razon_social, ausencia_nif, codigo_provincia_ine, codigo_municipio_dgc, codigo_municipio_ine, entidad_menor, " +
								"id_via, primer_numero, primera_letra, segundo_numero, segunda_letra, escalera, kilometro, bloque, planta, " +
								"puerta, direccion_no_estructurada, codigo_postal, apartado_correos, nif_conyuge, nif_cb, " +
								"complemento_titularidad, fecha_alteracion, nif, parcela_catastral, numero_cargo, digito_control1, " +
						"digito_control2) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");  
					}
				}
			}
			else
			{
				Connection conn = getDBConnection();
				s = conn.prepareStatement("select nif from persona where nif=? and parcela_catastral=? and numero_cargo=? and digito_control1=? and digito_control2=?");
				s.setString(1, pers.getNif());
				s.setString(2,idBi.getParcelaCatastral().getRefCatastral());
				s.setString(3,idBi.getNumCargo());
				s.setString(4,idBi.getDigControl1());
				s.setString(5,idBi.getDigControl2());

				r = s.executeQuery();
				if (r.next())
				{
					s=null;
					s = conn.prepareStatement("update persona set anno_expediente=?, referencia_expediente=?, codigo_entidad_colaboradora=?," +
							" razon_social=?, ausencia_nif=?, codigo_provincia_ine=?, codigo_municipio_dgc=?, codigo_municipio_ine=?, " +
							"entidad_menor=?, id_via=?, primer_numero=?, primera_letra=?, segundo_numero=?, segunda_letra=?, escalera=?, " +
							"kilometro=?, bloque=?, planta=?, puerta=?, direccion_no_estructurada=?, codigo_postal=?, apartado_correos=?, " +
							"nif_conyuge=?, nif_cb=?, complemento_titularidad=?, fecha_alteracion=? where nif=? and parcela_catastral=? " +
					"and numero_cargo=? and digito_control1=? and digito_control2=?"); 
				}
				else
				{

					s = conn.prepareStatement("insert into persona (anno_expediente, referencia_expediente, codigo_entidad_colaboradora, " +
							"razon_social, ausencia_nif, codigo_provincia_ine, codigo_municipio_dgc, codigo_municipio_ine, entidad_menor, " +
							"id_via, primer_numero, primera_letra, segundo_numero, segunda_letra, escalera, kilometro, bloque, planta, " +
							"puerta, direccion_no_estructurada, codigo_postal, apartado_correos, nif_conyuge, nif_cb, " +
							"complemento_titularidad, fecha_alteracion, nif, parcela_catastral, numero_cargo, digito_control1, " +
					"digito_control2) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

					if (pers.getAusenciaNIF()!=null){
						Long nifAuxiliar = null;
						s = conn.prepareStatement("select nextval('seq_ausencia_nif')");
						r = s.executeQuery();
						if (r.next()){
							nifAuxiliar = new Long(r.getLong(1));    
							pers.setNif(nifAuxiliar.toString());
						}
						s=null;
						r=null;
						s = conn.prepareStatement("insert into persona (anno_expediente, referencia_expediente, codigo_entidad_colaboradora, " +
								"razon_social, ausencia_nif, codigo_provincia_ine, codigo_municipio_dgc, codigo_municipio_ine, entidad_menor, " +
								"id_via, primer_numero, primera_letra, segundo_numero, segunda_letra, escalera, kilometro, bloque, planta, " +
								"puerta, direccion_no_estructurada, codigo_postal, apartado_correos, nif_conyuge, nif_cb, " +
								"complemento_titularidad, fecha_alteracion, nif, parcela_catastral, numero_cargo, digito_control1, " +
						"digito_control2) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

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

						s = conn.prepareStatement("select niftitular from derechos where niftitular=? and id_bieninmueble=?");
						s.setString(1, pers.getNif());
						s.setString(2,idBi.getIdBienInmueble());                    

						r = s.executeQuery();
						if (r.next())
						{
							s=null;
							s = conn.prepareStatement("update derechos set codigo_derecho=?, porcentaje_derecho=?, ordinal_derecho=? where niftitular=? and id_bieninmueble=?"); 
						}
						else
						{
							s=null;
							s = conn.prepareStatement("insert into derechos (codigo_derecho, porcentaje_derecho, ordinal_derecho, niftitular, id_bieninmueble) values (?,?,?,?,?)");
						}

					}
					else
					{                	
						s = conn.prepareStatement("insert into derechos (codigo_derecho, porcentaje_derecho, ordinal_derecho, niftitular, id_bieninmueble) values (?,?,?,?,?)");
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
			ex.printStackTrace();
			throw new DataException(I18N.get("Importacion","importar.error.personas"), ex);

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

		if (insertarExpediente)
			insertarDatosExpediente (exp);


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
	private void insertarDatosRepartoCultivos(RepartoCatastro reparto, ElementoReparto elemReparto) //throws DataException
	throws Exception
	{   
		PreparedStatement s = null;
		ResultSet r = null;
		Integer idReparto = null;

		//Comprobar si existe el reparto para ver si es inserción o actualización
		Connection conn = getDBConnection();
		s = conn.prepareStatement("select id_reparto from repartoscultivos where id_cultivoorigen=(select identificador from " +
				"cultivos where parcela_catastral=? and codigo_subparcela=? and calificacion_cultivo=?) and " +
				"id_bieninmuebledestino=(select identificador from bien_inmueble where parcela_catastral=? " +
		"and numero_cargo=?)");

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
			s = conn.prepareStatement("update repartoscultivos set anno_expediente=?, referencia_expediente=?, " +
					"codigo_entidad_colaboradora=?, tipo_movimiento=?, id_cultivoorigen=(select identificador from cultivos " +
					"where parcela_catastral=? and codigo_subparcela=? and calificacion_cultivo=?), porcentaje_reparto=?, " +
					"id_bieninmuebledestino=(select identificador from bien_inmueble where parcela_catastral=? and numero_cargo=?) " +
			"where id_reparto=?");
		else
			s = conn.prepareStatement("insert into repartoscultivos (anno_expediente, referencia_expediente, " +
					"codigo_entidad_colaboradora, tipo_movimiento, id_cultivoorigen, porcentaje_reparto, id_bieninmuebledestino, " +
					"id_reparto) values (?,?,?,?,select identificador from cultivos where parcela_catastral=? " +
					"and codigo_subparcela=? and calificacion_cultivo=?,?,select identificador from bien_inmueble where " +
			"parcela_catastral=? and numero_cargo=?, nextval('seq_reparto'))");

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
	private void insertarDatosRepartoConstrucciones(RepartoCatastro reparto, ElementoReparto elemreparto) //throws DataException
	throws Exception
	{           

		PreparedStatement s = null;
		ResultSet r = null;
		Integer idReparto = null;

		Connection conn = getDBConnection();
		//Comprobar si existe el reparto para ver si es inserción o actualización
		s = conn.prepareStatement("select id_reparto from repartosconstrucciones where id_construccionorigen=(select " +
				"identificador from construccion where parcela_catastral=? and numero_orden_construccion=?) and " +
				"id_construcciondestino=(select identificador from construccion where parcela_catastral=? and " +
		"numero_orden_construccion=?)");

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
			s = conn.prepareStatement("update repartosconstrucciones set anno_expediente=?, referencia_expediente=?, " +
					"codigo_entidad_colaboradora=?, tipo_movimiento=?, id_construccionorigen=(select identificador " +
					"from construccion where parcela_catastral=? and numero_orden_construccion=?), porcentaje_reparto=?, " +
					"id_construcciondestino=(select identificador from construccion where parcela_catastral=? " +
			"and numero_orden_construccion=?) where id_reparto=?");
		else
			s = conn.prepareStatement("insert into repartosconstrucciones (anno_expediente, referencia_expediente, " +
					"codigo_entidad_colaboradora, tipo_movimiento, id_construccionorigen, porcentaje_reparto, " +
					"id_construcciondestino, id_reparto) values (?,?,?,?,(select identificador from construccion where " +
					"parcela_catastral=? and numero_orden_construccion=?),?, (select identificador from construccion where " +
			"parcela_catastral=? and numero_orden_construccion=?), nextval('seq_reparto'))");

		lstValores.clear();
		lstTipos.clear();

		if (elemreparto.getExpediente()!= null){
			lstValores.add(new Integer(elemreparto.getExpediente().getAnnoExpedienteAdminOrigenAlteracion()));
		}
		else{
			lstValores.add(null);
		}
		lstTipos.add(TYPE_NUMERIC);   
		if (elemreparto.getExpediente()!= null){
			lstValores.add(elemreparto.getExpediente().getReferenciaExpedienteAdminOrigen());
		}
		else{
			lstValores.add(null);
		}
		lstTipos.add(TYPE_VARCHAR); 
		if (elemreparto.getExpediente() != null && elemreparto.getExpediente().getEntidadGeneradora()!=null)
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
	private void insertarDatosRepartoConsBi(RepartoCatastro reparto, ElementoReparto elemReparto) //throws DataException
	throws Exception
	{           
		PreparedStatement s = null;
		ResultSet r = null;
		Integer idReparto = null;

		Connection conn = getDBConnection();
		//Comprobar si existe el reparto para ver si es inserción o actualización
		s = conn.prepareStatement("select id_reparto from repartosconsbi where idconstruccion_origen=(select identificador " +
				"from construccion where parcela_catastral=? and numero_orden_construccion=?) and " +
		"id_bieninmuebledestino=(select identificador from bien_inmueble where parcela_catastral=? and numero_cargo=?)");

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
			s = conn.prepareStatement("update repartosconsbi set anno_expediente=?, referencia_expediente=?, " +
					"codigo_entidad_colaboradora=?, tipo_movimiento=?, idconstruccion_origen=(select identificador from " +
					"construccion where parcela_catastral=? and numero_orden_construccion=?), porcentaje_reparto=?, " +
					"id_bieninmuebledestino=(select identificador from bien_inmueble where parcela_catastral=? and numero_cargo=?) " +
			"where id_reparto=?");
		else
			s = conn.prepareStatement("insert into repartosconsbi (anno_expediente, referencia_expediente, " +
					"codigo_entidad_colaboradora, tipo_movimiento, idconstruccion_origen, porcentaje_reparto, " +
					"id_bieninmuebledestino, id_reparto) values (?,?,?,?,(select identificador from construccion where " +
					"parcela_catastral=? and numero_orden_construccion=?),?,(select identificador from bien_inmueble where " +
			"parcela_catastral=? and numero_cargo=?), nextval('seq_reparto'))");

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
			s = conn.prepareStatement("select identificador from suelo where identificador=?");
			s.setString(1,sc.getIdSuelo());
			r = s.executeQuery();
			if (r.next())
				idSuelo = r.getString(1);

			s=null;
			r=null;

			if(idSuelo!=null)
				s = conn.prepareStatement("update suelo set anno_expediente=?,referencia_expediente=?,codigo_entidad_colaboradora=?," +
						"tipo_movimiento=?, parcela_catastral=?,numero_orden=?,longitud_fachada=?,tipo_fachada=?," +
						"superficie_elemento_suelo=?, fondo_elemento_suelo=?,codigo_via_ponencia=?,codigo_tramo_ponencia=?, " +
						"zona_valor=?, zona_urbanistica=?, codigo_tipo_valor=?, numero_fachadas=?, corrector_longitud_fachada=?, " +
						"corrector_forma_irregular=?, corrector_desmonte_excesivo=?, corrector_superficie_distinta=?, " +
						"corrector_inedificabilidad=?, corrector_vpo=?, corrector_apreciacion_depreciacion=?, " +
						"corrector_depreciacion_funcional=?, corrector_cargas_singulares=?, corrector_situaciones_especiales=?, " +
				"corrector_uso_no_lucrativo=? where identificador=?");
			else
				s = conn.prepareStatement("insert into suelo (anno_expediente,referencia_expediente,codigo_entidad_colaboradora," +
						"tipo_movimiento,parcela_catastral, numero_orden,longitud_fachada,tipo_fachada,superficie_elemento_suelo," +
						"fondo_elemento_suelo, codigo_via_ponencia,codigo_tramo_ponencia, zona_valor, zona_urbanistica," +
						"codigo_tipo_valor, numero_fachadas, corrector_longitud_fachada, corrector_forma_irregular, " +
						"corrector_desmonte_excesivo,corrector_superficie_distinta, corrector_inedificabilidad, corrector_vpo, " +
						"corrector_apreciacion_depreciacion, corrector_depreciacion_funcional, corrector_cargas_singulares, " +
						"corrector_situaciones_especiales, corrector_uso_no_lucrativo, identificador) " +
				"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");


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
		catch (DataException ex)
		{           
			throw new DataException(I18N.get("Importacion","importar.error.suelos"), ex);

		}        
	}


	private void insertarDatosUC(UnidadConstructivaCatastro uc, boolean insertarExpediente, Expediente exp)

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
			s = conn.prepareStatement("select identificador from unidad_constructiva where identificador=?");
			s.setString(1,uc.getIdUnidadConstructiva());
			r = s.executeQuery();
			if (r.next())
				idUC = r.getString(1);

			s=null;
			r=null;

			if(idUC!=null)
				s = conn.prepareStatement("update unidad_constructiva set anno_expediente=?, referencia_expediente=?, " +
						"codigo_entidad_colaboradora=?, tipomovimiento=?, parcela_catastral=?, " +
						"codigo_unidad_constructiva=?, clase_unidad=?, codigo_municipio_ine=?, codigo_municipio_dgc=?, " +
						"nombre_entidad_menor=?, primer_numero=?, primera_letra=?, segundo_numero=?, segunda_letra=?, " +
						"kilometro=?, bloque=?, direccion_no_estructurada=?, anio_construccion=?, indicador_exactitud=?, " +
						"superficie_ocupada=?, longitud_fachada=?, codigo_via_ponencia=?, codigo_tramo_ponencia=?, " +
						"zona_valor=?, numero_fachadas=?, corrector_longitud_fachada=?, corrector_estado_conservacion=?," +
						"corrector_depreciacion_funcional=?, corrector_cargas_singulares=?, " +
						"corrector_situaciones_especiales=?, corrector_uso_no_lucrativo=?, id_via=? " +
				"where identificador=?"); 
			else
				s = conn.prepareStatement("insert into unidad_constructiva (anno_expediente, referencia_expediente, " +
						"codigo_entidad_colaboradora, tipomovimiento, parcela_catastral, codigo_unidad_constructiva, clase_unidad, " +
						"codigo_municipio_ine, codigo_municipio_dgc, nombre_entidad_menor, primer_numero, primera_letra, " +
						"segundo_numero, segunda_letra, kilometro, bloque, direccion_no_estructurada, anio_construccion, " +
						"indicador_exactitud, superficie_ocupada, longitud_fachada, codigo_via_ponencia, codigo_tramo_ponencia, " +
						"zona_valor, numero_fachadas, corrector_longitud_fachada, corrector_estado_conservacion," +
						"corrector_depreciacion_funcional, corrector_cargas_singulares, corrector_situaciones_especiales, " +
						"corrector_uso_no_lucrativo, id_via, identificador) values" +
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

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
		catch (DataException ex)
		{           
			throw new DataException(I18N.get("Importacion","importar.error.uc"), ex);

		}       

	}


	private void insertarDatosConstruccion(ConstruccionCatastro construc, boolean insertarExpediente, Expediente exp)

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
			s = conn.prepareStatement("select identificador from construccion where identificador=?");
			s.setString(1,construc.getIdConstruccion());
			r = s.executeQuery();
			if (r.next())
				idConst = r.getString(1);

			r=null;
			s=null;

			if(idConst!=null)
				s = conn.prepareStatement("update construccion set anno_expediente=?, referencia_expediente=?, " +
						"codigo_entidad_colaboradora=?, tipo_movimiento=?, parcela_catastral=?, numero_orden_construccion=?, " +
						"numero_orden_bieninmueble=?, codigo_unidadconstructiva=?, bloque=?, escalera=?, planta=?, puerta=?, " +
						"codigo_destino_dgc=?, indicador_tipo_reforma=?, anno_reforma=?, anno_antiguedad=?, indicador_local_interior=?," +
						" superficie_total_local=?, superficie_terrazas_local=?, superficie_imputable_local=?, tipologia_constructiva=?," +
						" codigo_uso_predominante=?, codigo_categoria_predominante=?, codigo_modalidad_reparto=?, codigo_tipo_valor=?, " +
				"corrector_apreciacion_economica=?, corrector_vivienda=? where identificador=?");
			else
				s = conn.prepareStatement("insert into construccion (anno_expediente, referencia_expediente, " +
						"codigo_entidad_colaboradora, tipo_movimiento, parcela_catastral, numero_orden_construccion, " +
						"numero_orden_bieninmueble, codigo_unidadconstructiva, bloque, escalera, planta, puerta, codigo_destino_dgc, " +
						"indicador_tipo_reforma, anno_reforma, anno_antiguedad, indicador_local_interior, superficie_total_local, " +
						"superficie_terrazas_local, superficie_imputable_local, tipologia_constructiva, codigo_uso_predominante, " +
						"codigo_categoria_predominante, codigo_modalidad_reparto, codigo_tipo_valor, corrector_apreciacion_economica, " +
				"corrector_vivienda, identificador) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); 


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
		catch (DataException ex)
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
			s = conn.prepareStatement("select identificador from cultivos where identificador=?");
			s.setString(1,cultivo.getIdCultivo().getIdCultivo());
			r = s.executeQuery();
			if (r.next())
				idCultivo = r.getString(1);

			s=null;
			r=null;

			if(idCultivo!=null)
				s = conn.prepareStatement("update cultivos set anno_expediente=?, referencia_expediente=?, codigo_entidad_colaboradora=?, " +
						"tipo_movimiento=?, naturaleza_suelo=?, parcela_catastral=?, codigo_subparcela=?, numero_orden=?, " +
						"tipo_subparcela=?, superficie_subparcela=?, calificacion_cultivo=?, denominacion_cultivo=?, " +
						"intensidad_productiva=?, bonificacion_subparcela=?, ejercicio_finalizacion=?, valor_catastral=?, " +
				"modalidad_reparto=? where identificador=?");
			else
				s = conn.prepareStatement("insert into cultivos (anno_expediente, referencia_expediente, codigo_entidad_colaboradora, " +
						"tipo_movimiento, naturaleza_suelo, parcela_catastral, codigo_subparcela, numero_orden, tipo_subparcela, " +
						"superficie_subparcela, calificacion_cultivo, denominacion_cultivo, intensidad_productiva, " +
						"bonificacion_subparcela, ejercicio_finalizacion, valor_catastral, modalidad_reparto, identificador) " +
				"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");


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
		catch (DataException ex)
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
		s = conn.prepareStatement("select id from parcelas where referencia_catastral=? and fecha_baja is null");
		s.setString(1, fc.getRefFinca().getRefCatastral());     
		r = s.executeQuery();
		if (r.next())
			idFinca = r.getString(1);

		s=null;
		r=null;
		if (idFinca!=null)
		{

			//Comprobar si existe el fxcc para ver si es inserción o actualización
			s = conn.prepareStatement("select id_finca from fxcc where id_finca=?");
			s.setString(1,idFinca);
			r = s.executeQuery();

			String idFxcc=null;
			if (r.next())
				idFxcc = r.getString(1);                

			s=null;
			r=null;

			if(idFxcc!=null)
				s = conn.prepareStatement("update fxcc set \"DXF\"=?, \"ASC\"=? where id_finca=?");
			else
				s = conn.prepareStatement("insert into fxcc (\"DXF\", \"ASC\", id_finca) values (?, ?, ?)");

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
		s = conn.prepareStatement("select id from parcelas where referencia_catastral=? and fecha_baja is null");
		s.setString(1, fc.getRefFinca().getRefCatastral());     
		r = s.executeQuery();
		if (r.next())
			idFinca = r.getString(1);

		s=null;
		r=null;
		if (idFinca!=null)
		{

			//Comprobar si existe la imagen para ver si es inserción o actualización
			s = conn.prepareStatement("select id_finca from imagenes where id_finca=?");
			s.setString(1,idFinca);
			r = s.executeQuery();

			String idImagen = null;
			if (r.next())
				idImagen = r.getString(1);                

			s=null;
			r=null;

			if(idImagen!=null)
				s = conn.prepareStatement("update imagenes set nombre=?, formato=?, tipo_documento=?, foto=? where id_finca=?");
			else
				s = conn.prepareStatement("insert into imagenes (nombre, formato, tipo_documento, foto, id_finca) values (?, ?, ?, ?, ?)");

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

			s = conn.prepareStatement("update catastro_temporal set fxcc=? where id_expediente=? and referencia=?");

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
		s = conn.prepareStatement("select id from municipios where id=?");

		s.setInt(1, identificadorMunicipio.intValue());

		r = s.executeQuery();
		if (r.next())
		{
			s=null;

			if (dir.getCodigoVia()!=0){

				s = conn.prepareStatement("select id from vias where codigocatastro=? and id_municipio=?");

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

				s = conn.prepareStatement("select id from vias where tipovianormalizadocatastro=? and nombrecatastro=? and id_municipio=?");

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
				s = conn.prepareStatement("select nextval('seq_vias')");
				r = null;
				r = s.executeQuery();
				if (r.next())
					idVia = r.getInt(1);

				if (idVia!=0)
				{

					//Inserción de la vía
					s = conn.prepareStatement("insert into vias (id, codigocatastro, tipovianormalizadocatastro, nombrecatastro, id_municipio) values (?,?,?,?,?)");

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
		s = conn.prepareStatement("select id from municipios where id=?");

		s.setInt(1, identificadorMunicipio.intValue());

		r = s.executeQuery();
		if (r.next())
		{
			s=null;

			s = conn.prepareStatement("select id from vias where codigocatastro=? and id_municipio=?");

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
				s = conn.prepareStatement("select nextval('seq_vias')");
				r = null;
				r = s.executeQuery();
				if (r.next())
					idVia = r.getInt(1);

				if (idVia!=0 && dir.getNombreVia()!=null)
				{

					//Inserción de la vía
					s = conn.prepareStatement("insert into vias (id, codigocatastro, tipovianormalizadocatastro, nombrecatastro, id_municipio) values (?,?,?,?,?)");

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
				s = conn.prepareStatement("select id_paraje from paraje where id_paraje=?");
				s.setInt(1, identificador);

				r = s.executeQuery();
				if (!r.next())
				{			
					r=null;
					s=null;

					//Inserción del paraje
					s = conn.prepareStatement("insert into paraje (id_paraje, codigoparaje, denominacionparaje) values (?,?,?)");

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
			s = conn.prepareStatement("select id from parcelas where referencia_catastral=? and fecha_baja is null");
			s.setString(1,fc.getRefFinca().getRefCatastral());
			r = s.executeQuery();
			if (r.next())
				idFinca = new Integer(r.getInt(1));

			String codProvincia = EdicionUtils.paddingString(String.valueOf(fc.getDirParcela().getProvinciaINE()),
					TAM_CODPRO,'0',true);
			String codMunicipio = EdicionUtils.paddingString(String.valueOf(fc.getDirParcela().getMunicipioINE()),
					TAM_CODMUNI,'0',true);
			String codCompletoMunicipio = codProvincia + codMunicipio;

			s=null;
			r=null;
			if(idFinca!=null)
				s = conn.prepareStatement("update parcelas set referencia_catastral=?, id_municipio=?, primer_numero=?, primera_letra=?," +
						" segundo_numero=?, segunda_letra=?, kilometro=?, bloque=?, direccion_no_estructurada=?, codigo_postal=?, " +
						"fecha_alta=?, fecha_baja=?, codigodelegacionmeh=?, nombreentidadmenor=?,id_distrito=?, " +
						"codigozonaconcentracion=?,codigopoligono=?,codigoparcela=?,nombreparaje=?,id_via=?, anno_expediente=?," +
						"referencia_expediente=?,codigo_entidad_colaboradora=?,tipo_movimiento=?,codigo_municipioDGC=?, BICE=?," +
						"codigo_provinciaINE=?, codigo_municipio_localizacionDGC=?, codigo_municipio_localizacionINE=?, " +
						"codigo_municipio_origenDGC=?, codigo_paraje=?,superficie_finca=?, superficie_construida_total=?," +
						"superficie_const_sobrerasante=?,superficie_const_bajorasante=?,superficie_cubierta=?, coordenada_x=?, coordenada_y=?, anio_aprobacion=?, " +
						"codigo_calculo_valor=?, poligono_catastral_valoracion=?, modalidad_reparto=?, indicador_infraedificabilidad=?," +
				" movimiento_baja=? where id=?");
			else
			{
				s = conn.prepareStatement("select nextval('seq_parcelas')");
				r = s.executeQuery();
				if (r.next())
					idFinca = new Integer(r.getInt(1));

				r=null;
				s=null;
				s = conn.prepareStatement("insert into parcelas (referencia_catastral, id_municipio, primer_numero, primera_letra, " +
						"segundo_numero, segunda_letra, kilometro, bloque, direccion_no_estructurada, codigo_postal, fecha_alta, " +
						"fecha_baja, codigodelegacionmeh, nombreentidadmenor,id_distrito, codigozonaconcentracion,codigopoligono," +
						"codigoparcela,nombreparaje,id_via, anno_expediente,referencia_expediente,codigo_entidad_colaboradora," +
						"tipo_movimiento,codigo_municipioDGC, BICE,codigo_provinciaINE, codigo_municipio_localizacionDGC, " +
						"codigo_municipio_localizacionINE, codigo_municipio_origenDGC, codigo_paraje,superficie_finca, " +
						"superficie_construida_total,superficie_const_sobrerasante,superficie_const_bajorasante,superficie_cubierta, coordenada_x, coordenada_y, " +
						"anio_aprobacion, codigo_calculo_valor, poligono_catastral_valoracion, modalidad_reparto, " +
						"indicador_infraedificabilidad, movimiento_baja, id) values" +
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

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

			lstValores.add(fc.getDirParcela().getProvinciaINE());
			lstTipos.add(TYPE_VARCHAR); 

			lstValores.add(fc.getDirParcela().getCodigoMunicipioDGC());
			lstTipos.add(TYPE_VARCHAR); 

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
			
			lstValores.add(fc.getDatosFisicos().getSupBajoRasante());            
			lstTipos.add(TYPE_NUMERIC);  

			lstValores.add(fc.getDatosFisicos().getSupCubierta());            
			lstTipos.add(TYPE_NUMERIC);  

			lstValores.add(fc.getDatosFisicos().getXCoord());  
			lstTipos.add(TYPE_FLOAT);

			lstValores.add(fc.getDatosFisicos().getYCoord()); 
			lstTipos.add(TYPE_FLOAT);

			lstValores.add(fc.getDatosEconomicos().getAnioAprobacion());            
			lstTipos.add(TYPE_NUMERIC);  

			lstValores.add(fc.getDatosEconomicos().getCodigoCalculoValor());            
			lstTipos.add(TYPE_NUMERIC);  

			lstValores.add(fc.getDatosEconomicos().getPoligonoCatastralValor().getCodPoligono());            
			lstTipos.add(TYPE_VARCHAR); 

			lstValores.add(fc.getDatosEconomicos().getIndModalidadReparto());
			lstTipos.add(TYPE_VARCHAR); 

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


		}
		catch (DataException ex)
		{                       
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
			s = conn.prepareStatement("select id_entidad_generadora from entidad_generadora where codigo=?");
			s.setInt(1,datosExpediente.getEntidadGeneradora().getCodigo());
			r = s.executeQuery();
			if (r.next())
				idEntidadGeneradora = new Long(r.getLong(1));       

			s=null;
			r=null;
			if(idEntidadGeneradora!=null)
				s = conn.prepareStatement("update entidad_generadora set codigo=?, tipo=?, descripcion=?, nombre=? where id_entidad_generadora=?");
			else
			{
				s = conn.prepareStatement("select nextval('seq_entidadgeneradora')");
				r = s.executeQuery();
				if (r.next())
					idEntidadGeneradora = new Long(r.getLong(1));     
				s=null;
				r=null;
				s = conn.prepareStatement("insert into entidad_generadora(codigo, tipo, descripcion, nombre, id_entidad_generadora) values(?,?,?,?,?)");
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
			s = conn.prepareStatement("select nextval('seq_domicilio_localizacion')");
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

				s = conn.prepareStatement("insert into domicilio_localizacion (id_localizacion, provincia_ine, municipio_ine, " +
						"codigo_municipiodgc, nombre_entidad_menor, codigo_via, primer_numero, primera_letra, segundo_numero, " +
						"segunda_letra, kilometro, bloque, escalera, planta, puerta, direccion_no_estructurada, codigo_postal, " +
						"nombre_via, apartado_correos, tipo_via, nombre_provincia, nombre_municipio) values " +
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

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
		s = conn.prepareStatement("select id_expediente from expediente where numero_expediente=?");
		s.setString(1,datosExpediente.getNumeroExpediente());
		r = s.executeQuery();
		if (r.next())
			idExpediente = new Long(r.getLong(1));

		s=null;
		r=null;
		if(idExpediente!=null)
			s = conn.prepareStatement("update expediente set id_entidad_generadora=?, id_estado=?, id_tecnico_catastro=?," +
					"id_localizacion=?, id_municipio=?, numero_expediente=?, id_tipo_expediente=(select id_tipo_expediente from " +
					"tipo_expediente where codigo_tipo_expediente=?), fecha_alteracion=?, anio_expediente_gerencia=?, " +
					"referencia_expediente_gerencia=?, codigo_entidad_registro_dgc_origen_alteracion=?, " +
					"anio_expediente_admin_origen_alteracion=?, referencia_expediente_admin_origen=?, fecha_registro=?, " +
					"fecha_movimiento=?, hora_movimiento=?, tipo_documento_origen_alteracion=?, info_documento_origen_alteracion=?, " +
					"codigo_descriptivo_alteracion=?, descripcion_alteracion=?, nif_presentador=?, nombre_completo_presentador=?, " +
					"num_bienesinmuebles_urb=?, num_bienesinmuebles_rus=?, num_bienesinmuebles_esp=?, fecha_de_cierre=?, " +
					"cod_provincia_notaria=?, cod_poblacion_notaria=?, cod_notaria=?, anio_protocolo_notarial=?, " +
			"protocolo_notarial=? where id_expediente=?");
		else
		{
			s = conn.prepareStatement("select nextval('seq_expediente')");
			r = s.executeQuery();
			if (r.next())
				idExpediente = new Long(r.getLong(1));

			r=null;
			s=null;
			s = conn.prepareStatement("insert into expediente (id_entidad_generadora, id_estado, id_tecnico_catastro," +
					"id_localizacion, id_municipio, numero_expediente, id_tipo_expediente, fecha_alteracion, " +
					"anio_expediente_gerencia, referencia_expediente_gerencia, codigo_entidad_registro_dgc_origen_alteracion, " +
					"anio_expediente_admin_origen_alteracion, referencia_expediente_admin_origen, fecha_registro, fecha_movimiento, " +
					"hora_movimiento, tipo_documento_origen_alteracion, info_documento_origen_alteracion, " +
					"codigo_descriptivo_alteracion, descripcion_alteracion, nif_presentador, nombre_completo_presentador, " +
					"num_bienesinmuebles_urb, num_bienesinmuebles_rus, num_bienesinmuebles_esp, fecha_de_cierre, " +
					"cod_provincia_notaria, cod_poblacion_notaria, cod_notaria, anio_protocolo_notarial, protocolo_notarial, " +
					"id_expediente) values (?,?,?,?,?,?,(select id_tipo_expediente from tipo_expediente where " +
			"codigo_tipo_expediente=?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");                
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

		lstValores.add(datosExpediente.getAnnoExpedienteGerencia());
		lstTipos.add(TYPE_NUMERIC);

		lstValores.add(datosExpediente.getReferenciaExpedienteGerencia());
		lstTipos.add(TYPE_VARCHAR);

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
			case Types.DATE:
				if (o == null)
					s.setNull(i+1, Types.DATE);
				else
					s.setDate(i+1, new java.sql.Date(((Date)o).getTime()));
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
	 * 
	 * @param udsa
	 * @throws Exception 
	 * @throws DataException
	 */
	public void insertarDatosSalida (ObjectOutputStream oos,UnidadDatosIntercambio udsa, boolean insertarExpediente, 
			Expediente exp) throws Exception //throws DataException

			{  
		try {

			Geometry geometriaParcela = null;
			if (udsa.getFincaCatastro()!=null){
				if(udsa.getFxcc()!=null){
					if(udsa.getFxcc().getDXF()!=null){

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

		} catch (Exception e) {
			e.printStackTrace();
			oos.writeObject(new ACException(e));
			releaseConnection();
		}


			}

	/**
	 * 
	 * @param udretorno
	 * @throws DataException
	 */
	public void insertarDatosRetorno(ObjectOutputStream oos, UnidadDatosRetorno udretorno) //throws DataException
	throws Exception
	{   
		insertarDatosExpediente(udretorno.getExpediente());

		Iterator itUDSalida = udretorno.getLstSituaciones().iterator();
		while (itUDSalida.hasNext())
		{
			UnidadDatosIntercambio udsa = (UnidadDatosIntercambio)itUDSalida.next();
			insertarDatosSalida(oos, udsa, false, null);            
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

		pers.getExpediente().setCodigoEntidadRegistroDGCOrigenAlteracion(Integer.valueOf(linea.substring(431, 434)));


		insertarDatosPersona(idBi, pers, true);
	}

	public void insertarDatosFinca (Geometry geometriaFinca, FincaCatastro fc, boolean insertarExpediente, Expediente exp)

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
			s = conn.prepareStatement("select id from parcelas where referencia_catastral=? and fecha_baja is null");
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
				s = conn.prepareStatement("update parcelas set referencia_catastral=?, id_municipio=?, primer_numero=?, " +
						"primera_letra=?, segundo_numero=?, segunda_letra=?, kilometro=?, bloque=?, direccion_no_estructurada=?, " +
						"codigo_postal=?, fecha_alta=?, fecha_baja=?, codigodelegacionmeh=?, nombreentidadmenor=?,id_distrito=?, " +
						"codigozonaconcentracion=?,codigopoligono=?,codigoparcela=?,nombreparaje=?,id_via=?, anno_expediente=?," +
						"referencia_expediente=?,codigo_entidad_colaboradora=?,tipo_movimiento=?,codigo_municipioDGC=?, BICE=?," +
						"codigo_provinciaINE=?, codigo_municipio_localizacionDGC=?, codigo_municipio_localizacionINE=?, " +
						"codigo_municipio_origenDGC=?, codigo_paraje=?,superficie_finca=?, superficie_construida_total=?," +
						"superficie_const_sobrerasante=?,superficie_cubierta=?, coordenada_x=?, coordenada_y=?, anio_aprobacion=?, " +
						"codigo_calculo_valor=?, poligono_catastral_valoracion=?, modalidad_reparto=?, indicador_infraedificabilidad=?, " +
				"movimiento_baja=?, \"GEOMETRY\"=? where id=?");
			else
			{
				s = conn.prepareStatement("select nextval('seq_parcelas')");
				r = s.executeQuery();
				if (r.next())
					idFinca = new Integer(r.getInt(1));

				r=null;
				s=null;
				s = conn.prepareStatement("insert into parcelas (referencia_catastral, id_municipio, primer_numero, " +
						"primera_letra, segundo_numero, segunda_letra, kilometro, bloque, direccion_no_estructurada, " +
						"codigo_postal, fecha_alta, fecha_baja, codigodelegacionmeh, nombreentidadmenor,id_distrito, " +
						"codigozonaconcentracion,codigopoligono,codigoparcela,nombreparaje,id_via, anno_expediente," +
						"referencia_expediente,codigo_entidad_colaboradora,tipo_movimiento,codigo_municipioDGC, BICE," +
						"codigo_provinciaINE, codigo_municipio_localizacionDGC, codigo_municipio_localizacionINE, " +
						"codigo_municipio_origenDGC, codigo_paraje,superficie_finca, superficie_construida_total," +
						"superficie_const_sobrerasante,superficie_cubierta, coordenada_x, coordenada_y, anio_aprobacion, " +
						"codigo_calculo_valor, poligono_catastral_valoracion, modalidad_reparto, " +
						"indicador_infraedificabilidad, movimiento_baja, \"GEOMETRY\", id) values" +
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

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

		}

	}
	
	private PonenciaTramos obtenerPonenciaTramo(String codTramoPonencia, String idMunicipio) throws Exception{
		
		PonenciaTramos ponenciaTramos = null;
		
		String sSQL= "select * from ponencia_tramos where id_municipio= " + idMunicipio + " and trim(codigo_tramo)='" + codTramoPonencia.trim() + "'";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;

		try{
			conn= CPoolDatabase.getConnection();
			ps=conn.prepareStatement(sSQL);
			rs=ps.executeQuery();			
			
			if(rs.next()){
				
				ponenciaTramos = new PonenciaTramos();
				ponenciaTramos.setAgua(rs.getString("agua"));
				ponenciaTramos.setAlcantarillado(rs.getString("alcantarillado"));
				ponenciaTramos.setAlumbrado(rs.getString("alcantarillado"));
				ponenciaTramos.setAnioAprobacion(new Integer(rs.getInt("anno_aprobacion")));
				
				TipoValor tipoValor = new TipoValor();
				tipoValor.setOtrosUsos1(new Float(rs.getFloat("banda_otrosusos1")));
				tipoValor.setOtrosUsos2(new Float(rs.getFloat("banda_otrosusos2")));
				tipoValor.setOtrosUsos3(new Float(rs.getFloat("banda_otrosusos3")));
				tipoValor.setUsoComercial(new Float(rs.getFloat("banda_usocomercial")));
				tipoValor.setUsoIndustrial(new Float(rs.getFloat("banda_usoindustrial")));
				tipoValor.setUsoOficinas(new Float(rs.getFloat("banda_usooficinas")));
				tipoValor.setUsoResidencial(new Float(rs.getFloat("banda_usoresidencial")));
				tipoValor.setUsoTuristico(new Float(rs.getFloat("banda_usoturistico")));
				
				ponenciaTramos.setBanda(tipoValor);
				
				ponenciaTramos.setCMaxImpar(rs.getString("caracter_maximoimpar"));
				ponenciaTramos.setCMaxPar(rs.getString("caracter_maximopar"));
				ponenciaTramos.setCMinImpar(rs.getString("caracter_minimoimpar"));
				ponenciaTramos.setCMinPar(rs.getString("caracter_minimopar"));
				ponenciaTramos.setCodDelegacionMEH(rs.getString("codigo_delegacionmeh"));
				ponenciaTramos.setCodMunicipioINE(rs.getString("codigo_municipio_meh"));
				ponenciaTramos.setCodMunicipioINE(rs.getString("codigo_municipio_ine"));
				ponenciaTramos.setCodProvinciaINE(rs.getString("codigo_provinciaine"));
				ponenciaTramos.setCodTramo(rs.getString("codigo_tramo"));
				ponenciaTramos.setCodVia(rs.getString("codigo_via"));
				ponenciaTramos.setCorrApDepConst(new Float(rs.getFloat("corrector_apdpsuecons")));
				ponenciaTramos.setCorrApDepSuelo(new Float(rs.getFloat("corrector_apdpsuelo")));
				ponenciaTramos.setCostesUrbanizacion(new Double(rs.getString("costes_urbanizacion")));
				ponenciaTramos.setDenominacion(rs.getString("denominacion"));
				ponenciaTramos.setDesmonte(rs.getString("desmonte"));
				ponenciaTramos.setElectricidad(rs.getString("electricidad"));
				ponenciaTramos.setIdMunicipio(rs.getString("id_municipio"));
				ponenciaTramos.setMaxImpar(new Integer(rs.getInt("maximo_impar")));
				ponenciaTramos.setMaxPar(new Integer(rs.getString("maximo_par")));
				ponenciaTramos.setMinImpar(new Integer(rs.getInt("minimo_impar")));
				ponenciaTramos.setMinPar(new Integer(rs.getInt("minimo_par")));
				ponenciaTramos.setPavimentacion(rs.getString("pavimentacion"));
				
				if (rs.getString("codigo_poligono")!= null && !rs.getString("codigo_poligono").equals("")){
					
					PonenciaPoligono ponenciaPoligono = null;
					String codPoligono = rs.getString("codigo_poligono");
					ponenciaPoligono = obtenerPonenciaPoligono(codPoligono, idMunicipio);
					ponenciaTramos.setPonPoligono(ponenciaPoligono);
				}
				
				ponenciaTramos.setSituacion(rs.getString("situacion"));
				
				if (rs.getString("codigo_urbanistica")!= null && !rs.getString("codigo_urbanistica").equals("")){
					
					PonenciaUrbanistica ponenciaUrbanistica = null;
					String codUrbanistica = rs.getString("codigo_urbanistica");
					ponenciaUrbanistica = obtenerPonenciaUrbanistica(codUrbanistica, idMunicipio);
					ponenciaTramos.setPonUrbanistica(ponenciaUrbanistica);
				}				
				
				ponenciaTramos.setValorSuelo(rs.getString("valor_vuelo"));
				ponenciaTramos.setValorUnitario(new Double(rs.getString("valor_unitario")));
								
			}
			
		}
		catch (Exception e){
			throw e;
		}
		finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		
		return ponenciaTramos;
	}
	
	private PonenciaPoligono obtenerPonenciaPoligono(String codPonenciaPoligono, String idMunicipio) throws Exception{
		
		PonenciaPoligono ponenciaPoligono = null;
		
		String sSQL= "select * from ponencia_poligono where id_municipio= " + idMunicipio + " and codigo_poligono='" + codPonenciaPoligono + "'";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;

		try{
			
			conn= CPoolDatabase.getConnection();
			ps=conn.prepareStatement(sSQL);
			rs=ps.executeQuery();			
			
			if(rs.next()){
				
				ponenciaPoligono = new PonenciaPoligono();
				
				ponenciaPoligono.setAnioNormas(new Integer(rs.getInt("aanormas")));
				ponenciaPoligono.setAnioRevision(new Integer(rs.getInt("aarevision")));
				ponenciaPoligono.setAnioAprobacion(new Integer(rs.getInt("anno_aprobacion")));
				ponenciaPoligono.setCodDelegacionMEH(rs.getString("codigo_delegacionmeh"));
				ponenciaPoligono.setCodMBCPlan(new Integer(rs.getInt("codmbc_plan")));
				ponenciaPoligono.setCodMunicipioINE(rs.getString("codigo_municipio_ine"));
				ponenciaPoligono.setCodMunicipioMEH(rs.getString("codigo_municipio_meh"));
				ponenciaPoligono.setCodPoligono(rs.getString("codigo_poligono"));
				ponenciaPoligono.setCodProvinciaINE(rs.getString("codigo_provinciaine"));
				ponenciaPoligono.setCoefCoordPlan(new Float(rs.getFloat("coef_coordplan")));
				ponenciaPoligono.setDiseminado(rs.getString("diseminado"));
				ponenciaPoligono.setEdifPlan(new Float(rs.getFloat("edif_plan")));
				ponenciaPoligono.setFlGB(new Float(rs.getFloat("fl_gb")));
				ponenciaPoligono.setFlGBUni(new Float(rs.getFloat("fl_gb_uni")));
				ponenciaPoligono.setGrupoPlan(new Integer(rs.getInt("grupo_plan")));
				ponenciaPoligono.setIdMunicipio(rs.getString("id_municipio"));
				ponenciaPoligono.setImporteMBC(new Double(rs.getDouble("mbc")));
				ponenciaPoligono.setImporteMBR(new Double(rs.getDouble("mbr")));
				ponenciaPoligono.setModPlan(new Float(rs.getFloat("modulo_plan")));
				ponenciaPoligono.setUsoPlan(rs.getString("uso_plan"));
				ponenciaPoligono.setVrb(new Double(rs.getDouble("vrb")));
				ponenciaPoligono.setZonaVRB(rs.getString("zona_vrb"));
				ponenciaPoligono.setZonaVUB(rs.getString("zona_vub"));
												
			}
			
		}
		catch (Exception e){
			throw e;
		}
		finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		
		return ponenciaPoligono;
	}
	
	private PonenciaUrbanistica obtenerPonenciaUrbanistica(String codPonenciaUrbanistica, String idMunicipio) throws Exception{
		
		PonenciaUrbanistica ponenciaUrbanistica = null;
		
		String sSQL= "select * from ponencia_urbanistica where id_municipio= " + idMunicipio + " and trim(codigo_zona)='" + codPonenciaUrbanistica.trim() + "'";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;

		try{
			
			conn= CPoolDatabase.getConnection();
			ps=conn.prepareStatement(sSQL);
			rs=ps.executeQuery();			
			
			if(rs.next()){
				
				ponenciaUrbanistica = new PonenciaUrbanistica();
				
				ponenciaUrbanistica.setAnioAprobacion(new Integer(rs.getInt("anno_aprobacion")));
				ponenciaUrbanistica.setCodCalificacion(rs.getString("codigo_calificacion"));
				ponenciaUrbanistica.setCodDelegacionMEH(rs.getString("codigo_delegacionmeh"));
				ponenciaUrbanistica.setCodMunicipioINE(rs.getString("codigo_municipio_ine"));
				ponenciaUrbanistica.setCodMunicipioMEH(rs.getString("codigo_municipio_meh"));
				ponenciaUrbanistica.setCodOcupacion(rs.getString("codigo_ocupacion"));
				ponenciaUrbanistica.setCodOrdenacion(rs.getString("codigo_ordenacion"));
				ponenciaUrbanistica.setCodProvinciaINE(rs.getString("codigo_provinciaine"));
				ponenciaUrbanistica.setCodZona(rs.getString("codigo_zona"));
				ponenciaUrbanistica.setCodZonificacion(rs.getString("codigo_zonificacion"));
				ponenciaUrbanistica.setDenominacion(rs.getString("denominacion"));
				
				TipoValor edificabilidad = new TipoValor();
				edificabilidad.setEquipamientos(new Float(rs.getFloat("edificabilidad_equipamientos")));
				edificabilidad.setOtrosUsos1(new Float(rs.getFloat("edificabilidad_otrosusos1")));
				edificabilidad.setOtrosUsos2(new Float(rs.getFloat("edificabilidad_otrosusos2")));
				edificabilidad.setOtrosUsos3(new Float(rs.getFloat("edificabilidad_otrosusos3")));
				edificabilidad.setUsoComercial(new Float(rs.getFloat("edificabilidad_usocomercial")));
				edificabilidad.setUsoIndustrial(new Float(rs.getFloat("edificabilidad_usoindustrial")));
				edificabilidad.setUsoOficinas(new Float(rs.getFloat("edificabilidad_usooficinas")));
				edificabilidad.setUsoResidencial(new Float(rs.getFloat("edificabilidad_usoresidencial")));
				edificabilidad.setUsoTuristico(new Float(rs.getFloat("edificabilidad_usoturistico")));
				edificabilidad.setZonaVerde(new Float(rs.getFloat("edificabilidad_zonaverde")));
				ponenciaUrbanistica.setEdificabilidad(edificabilidad);
				
				ponenciaUrbanistica.setFondo(new Integer(rs.getInt("fondo")));
				ponenciaUrbanistica.setIdMunicipio(rs.getString("id_municipio"));
				ponenciaUrbanistica.setLongitud(new Integer(rs.getInt("longitud")));
				ponenciaUrbanistica.setNumPlantas(new Float(rs.getFloat("numero_plantas")));
				ponenciaUrbanistica.setNumPlantasSolatInt(new Float(rs.getFloat("numeroplantas_solarinterior")));
				ponenciaUrbanistica.setSupMinima(new Integer(rs.getInt("superficie_minima")));
												
			}
			
		}
		catch (Exception e){
			throw e;
		}
		finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		
		return ponenciaUrbanistica;
	}
	
	private PonenciaZonaValor obtenerPonenciaZonaValor(String codPonenciaZonaValor, String idMunicipio) throws Exception{
		
		PonenciaZonaValor ponenciaZonaValor = null;
		
		String sSQL= "select * from ponencia_zonavalor where id_municipio= " + idMunicipio + " and codigo_zonavalor='" + codPonenciaZonaValor + "'";
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;

		try{
			
			conn= CPoolDatabase.getConnection();
			ps=conn.prepareStatement(sSQL);
			rs=ps.executeQuery();			
			
			if(rs.next()){
				
				ponenciaZonaValor = new PonenciaZonaValor();
				ponenciaZonaValor.setAnioAprobacion(new Integer(rs.getInt("anno_aprobacion")));
				ponenciaZonaValor.setCodDelegacionMEH(rs.getString("codigo_delegacionmeh"));
				ponenciaZonaValor.setCodMunicipioINE(rs.getString("codigo_municipio_ine"));
				ponenciaZonaValor.setCodMunicipioMEH(rs.getString("codigo_municipio_meh"));
				ponenciaZonaValor.setCodProvinciaINE(rs.getString("codigo_provinciaine"));
				ponenciaZonaValor.setCodZonaValor(rs.getString("codigo_zonavalor"));
				ponenciaZonaValor.setIdMunicipio(rs.getString("id_municipio"));
				
				TipoValor importesZonaValor = new TipoValor();
				importesZonaValor.setEquipamientos(new Float(rs.getFloat("importe_equipamientos")));
				importesZonaValor.setOtrosUsos1(new Float(rs.getFloat("importe_otrosusos1")));
				importesZonaValor.setOtrosUsos2(new Float(rs.getFloat("importe_otrosusos2")));
				importesZonaValor.setOtrosUsos3(new Float(rs.getFloat("importe_otrosusos3")));
				importesZonaValor.setUsoComercial(new Float(rs.getFloat("importe_usocomercial")));
				importesZonaValor.setUsoIndustrial(new Float(rs.getFloat("importe_usoindustrial")));
				importesZonaValor.setUsoOficinas(new Float(rs.getFloat("importe_usooficinas")));
				importesZonaValor.setUsoResidencial(new Float(rs.getFloat("importe_usoresidencial")));
				importesZonaValor.setUsoTuristico(new Float(rs.getFloat("importe_usoturistico")));
				importesZonaValor.setZonaVerde(new Float(rs.getFloat("importe_zonaverde")));
				ponenciaZonaValor.setImportesZonaValor(importesZonaValor);
				
				ponenciaZonaValor.setValorEquipamientos(new Float(rs.getFloat("valor_equipamientos")));
				ponenciaZonaValor.setValorSinDesarrollar(new Float(rs.getFloat("valor_suelosindesarrollar")));
				ponenciaZonaValor.setValorUnitario(new Float(rs.getFloat("valor_unitario")));
				ponenciaZonaValor.setValorZonaVerde(new Float(rs.getFloat("valor_zonaverde")));
																		
			}
			
		}
		catch (Exception e){
			throw e;
		}
		finally{
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		
		return ponenciaZonaValor;
	}
	
	/**
	 * Metodo que actualiza en la BD en la tabla expediente_finca_catastro el id_dialogo
	 * tenga asociadas.
	 *
	 * @param exp Expediente a actualizar.
	 * @throws Exception
	 * */
	
	
	private void updateIdentificadorDialogoExpedienteFincaCatastroBD(IdentificadorDialogo identificadorDialogo, Expediente exp, boolean actualizado) throws Exception {
		PreparedStatement ps= null;
		Connection conn= null;

		try {
//			for(int i=0; i<identificadorDialogo.getLstExpedientes().size(); i++){
//				Expediente exp = (Expediente)identificadorDialogo.getLstExpedientes().get(i);
				String queryUpdate = "UPDATE expediente_finca_catastro SET id_dialogo = '"+ identificadorDialogo.getIdentificadorDialogo() + "' , " +
						"actualizado = " +actualizado+
				"  WHERE id_expediente = " + exp.getIdExpediente() + " AND ref_catastral = '" +
					((FincaCatastro)identificadorDialogo.getFincaBien()).getRefFinca().getRefCatastral()+ "'";
				
				conn= CPoolDatabase.getConnection();
				ps= conn.prepareStatement(queryUpdate);
				ps.execute();
//			}	
			
			conn.commit();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}


	/**
	  * Metodo que actualiza en la BD en la tabla expediente_bieninmueble el id_dialogo
	 *
	 * @param exp Expediente a actualizar.
	 * @throws Exception
	 * */
	private void updateIdentificadorDialogoExpedienteBienInmuebleBD(IdentificadorDialogo identificadorDialogo, Expediente exp, boolean actualizado) throws Exception {
		PreparedStatement ps= null;
		Connection conn= null;

		try {
//			for(int i=0; i<identificadorDialogo.getLstExpedientes().size(); i++){
//				Expediente exp = (Expediente)identificadorDialogo.getLstExpedientes().get(i);
				String queryUpdate = "UPDATE expediente_bienInmueble SET id_dialogo ='"+ identificadorDialogo.getIdentificadorDialogo() +"' , " +
						"actualizado = "+actualizado+ 
				"  WHERE id_expediente = " + exp.getIdExpediente() + " AND id_bieninmueble = '" +
					((BienInmuebleCatastro)identificadorDialogo.getFincaBien()).getIdBienInmueble().getParcelaCatastral().getRefCatastral()+
					((BienInmuebleCatastro)identificadorDialogo.getFincaBien()).getIdBienInmueble().getNumCargo()+
					((BienInmuebleCatastro)identificadorDialogo.getFincaBien()).getIdBienInmueble().getDigControl1()+
					((BienInmuebleCatastro)identificadorDialogo.getFincaBien()).getIdBienInmueble().getDigControl2()+"'";
				
				conn= CPoolDatabase.getConnection();
				ps= conn.prepareStatement(queryUpdate);
				ps.execute();
//			}	
			
			conn.commit();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}
	
	
	
	/**
	  * Metodo que obtiene de la tabla expediente_bieninmueble el id_dialogo
	 *
	 * @param exp Expediente a actualizar.
	 * @throws Exception
	 * */
	public String obtenerIdentificadorDialogoExpedienteBienInmuebleBD(Expediente exp, IdentificadorDialogo  identificadorDialogo) throws Exception {
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		String idDialogo = null;
		try {
				
			String query = "SELECT id_dialogo FROM expediente_bienInmueble " +
			"  WHERE id_expediente = " + exp.getIdExpediente() + " AND id_bieninmueble = " +
				((BienInmuebleCatastro)identificadorDialogo.getFincaBien()).getIdBienInmueble().getParcelaCatastral().getRefCatastral();
			
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(query);
			rs = ps.executeQuery();

			if(rs.next()){
				idDialogo = rs.getString("id_dialogo");
				}	
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return idDialogo;
	}
	
	/**
	  * Metodo que obtiene de la tabla expediente_bieninmueble el id_dialogo
	 *
	 * @param exp Expediente a actualizar.
	 * @throws Exception
	 * */
	public String obtenerIdentificadorDialogoExpedienteFincaCatastroBD(Expediente exp, IdentificadorDialogo  identificadorDialogo) throws Exception {
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		String idDialogoFinca  = null;
		try {

				String query = "SELECT id_dialogo FROM expediente_finca_catastro " +
			"  WHERE id_expediente = " + exp.getIdExpediente() + " AND ref_catastral = " +
				((FincaCatastro)identificadorDialogo.getFincaBien()).getRefFinca().getRefCatastral();
			
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(query);
			rs = ps.executeQuery();

			if(rs.next()){
				idDialogoFinca = rs.getString("id_dialogo");
			}

		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return idDialogoFinca;
	}
	
	/**
	 * * consulta si la finca esta actualizado con la OVC
	 * @param exp
	 * @param referencia
	 * @return
	 * @throws Exception
	 */
	public boolean consultaEstadoFincaOVC(Expediente exp, String  referencia) throws Exception {
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		boolean actualizado  = false;
		try {

			String query = "SELECT actualizado FROM expediente_finca_catastro " +
				"  WHERE id_expediente = " + exp.getIdExpediente() + " AND ref_catastral = '" +referencia+"'";
			
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(query);
			rs =ps.executeQuery();

			if(rs.next()){
				actualizado = rs.getBoolean("actualizado");
			}

		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return actualizado;
	}
	
	/**
	 * consulta si el bien inmubeble esta actualizado con la OVC
	 * @param exp
	 * @param id_bieninmueble
	 * @return
	 * @throws Exception
	 */
	public boolean consultaEstadoBienInmuebleOVC(Expediente exp, String  id_bieninmueble) throws Exception {
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		boolean actualizado  = false;
		try {

			String query = "SELECT actualizado FROM expediente_bieninmueble " +
				"  WHERE id_expediente = " + exp.getIdExpediente() + " AND id_bieninmueble = '" +id_bieninmueble+"'";
			
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(query);
			rs = ps.executeQuery();

			if(rs.next()){
				actualizado = rs.getBoolean("actualizado");
			}

		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return actualizado;
	}

	/**
	 * Completa la información de las referencias del expediente que llega por parámetro,
	 * para poder guardarlo en catastro temporal.
	 * @param exp Expediente a completar.
	 * @param convenio El convenio con el que se esta trabajando.
	 * @throws Exception
	 */
	public void completarInfoCatastral(ObjectOutputStream oos,Integer idParcela, String convenio, String idMunicipio) throws Exception{

		try {
			
			if(convenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)){

				oos.writeObject(completarFincaCatastroDB(idParcela,idMunicipio));
			}

			else if(convenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)){
				

				oos.writeObject(completarFincaCatastroDB(idParcela,idMunicipio));

			}
		}
		catch (Exception e) {
			throw e;
		}

	}
	
	private FincaCatastro completarFincaCatastroDB(Integer idParcela, String idMunicipioFinca) throws Exception{

		FincaCatastro finca = new FincaCatastro();
		
		String sSQL = "select * from parcelas p where id="+ idParcela 
						+ " and p.id_municipio=" + idMunicipioFinca;

		PreparedStatement ps= null;
		PreparedStatement psVia= null;
		PreparedStatement psMunicipio= null;
		PreparedStatement psProvincia= null;
		Connection conn= null;
		ResultSet rs= null;
		ResultSet rsVia= null;
		ResultSet rsMunicipio= null;
		ResultSet rsProvincia= null;

		try {
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(sSQL);
			rs= ps.executeQuery();
			while(rs.next()) {

				//FincaCatastro finca = new FincaCatastro();
				finca.setBICE(rs.getString("bice"));
				ReferenciaCatastral refFinca = new ReferenciaCatastral(rs.getString("referencia_catastral"));
				finca.setRefFinca(refFinca);
				finca.setIdFinca(TypeUtil.getSimpleInteger(rs,"id"));

				DireccionLocalizacion dir = new DireccionLocalizacion();

				//Si no hay via, en BD se tiene un 0 (causa: importador de parcelas)
				int idVia = rs.getInt("id_via");
				if(idVia != 0){

					sSQL = "select v.tipovianormalizadocatastro, v.nombrecatastro, v.codigocatastro from vias v where codigocatastro="
						+ idVia+ " and id_municipio="+idMunicipioFinca;

					psVia= conn.prepareStatement(sSQL);
					rsVia= psVia.executeQuery();

					if(rsVia.next()) {
						dir.setCodigoVia(TypeUtil.getSimpleInteger(rsVia,"codigocatastro"));
						dir.setTipoVia(rsVia.getString("tipovianormalizadocatastro"));
						dir.setNombreVia(rsVia.getString("nombrecatastro"));
					}
				}

				int idMunicipio = rs.getInt("id_municipio");//es NOT NULL en la BD
				String idM = GeopistaFunctionUtils.completarConCeros(String.valueOf(idMunicipio), 5);               

				sSQL = "select nombreoficial from provincias where id="+ idM.substring(0,2);
				psProvincia= conn.prepareStatement(sSQL);
				rsProvincia= psProvincia.executeQuery();
				if(rsProvincia.next())
					dir.setNombreProvincia(rsProvincia.getString("nombreoficial"));

				//Si en la tabla de parcelas no tenemos el codigo, se le asigna el correspondiente al INE.
				finca.setCodDelegacionMEH(rs.getString("codigodelegacionmeh"));
				if(finca.getCodDelegacionMEH() == null || finca.getCodDelegacionMEH().equals(""))
					finca.setCodDelegacionMEH(idM.substring(0,2));

				sSQL = "select nombreoficial, id_provincia, id_ine, id_catastro from municipios where id="+ idMunicipio;
				psMunicipio= conn.prepareStatement(sSQL);
				rsMunicipio= psMunicipio.executeQuery();
				if(rsMunicipio.next()){
					dir.setNombreMunicipio(rsMunicipio.getString("nombreoficial"));
					dir.setProvinciaINE(rsMunicipio.getString("id_provincia"));
					dir.setMunicipioINE(rsMunicipio.getString("id_ine"));

					finca.setCodMunicipioDGC(rs.getString("codigo_municipiodgc"));
					if(finca.getCodMunicipioDGC() == null || finca.getCodMunicipioDGC().equals(""))
						finca.setCodMunicipioDGC(rsMunicipio.getString("id_catastro"));
					if(finca.getCodMunicipioDGC() == null || finca.getCodMunicipioDGC().equals(""))
						finca.setCodMunicipioDGC(dir.getMunicipioINE());
					if(finca.getCodMunicipioDGC() == null || finca.getCodMunicipioDGC().equals(""))
						finca.setCodMunicipioDGC(DEFAULT_CODIGO_MUNICIPIO_DGC);

					dir.setCodigoMunicipioDGC(finca.getCodMunicipioDGC());
					/*if (exp.getDireccionPresentador().getCodigoMunicipioDGC() == null || exp.getDireccionPresentador().getCodigoMunicipioDGC().equals("")){
						exp.getDireccionPresentador().setCodigoMunicipioDGC(finca.getCodMunicipioDGC());
					}*/
				}

				dir.setNombreEntidadMenor(rs.getString("nombreentidadmenor"));
				dir.setPrimerNumero(TypeUtil.getSimpleInteger(rs,"primer_numero"));
				dir.setPrimeraLetra(rs.getString("primera_letra"));
				dir.setSegundoNumero(TypeUtil.getSimpleInteger(rs,"segundo_numero"));
				dir.setSegundaLetra(rs.getString("segunda_letra"));
				dir.setKilometro(TypeUtil.getSimpleDouble(rs, "kilometro"));
				dir.setDireccionNoEstructurada(rs.getString("direccion_no_estructurada"));
				dir.setBloque(rs.getString("bloque"));

				int valorCodigoPostal=TypeUtil.getSimpleInteger(rs,"codigo_postal");
				if(valorCodigoPostal!=-1)
					dir.setCodigoPostal(String.valueOf(valorCodigoPostal));

				dir.setDistrito(rs.getString("id_distrito"));
				dir.setCodMunOrigenAgregacion(rs.getString("codigo_municipio_origendgc"));
				dir.setCodZonaConcentracion(rs.getString("codigozonaconcentracion"));
				dir.setCodPoligono(rs.getString("codigopoligono"));
				dir.setCodParcela(rs.getString("codigoparcela"));
				dir.setNombreParaje(rs.getString("nombreparaje"));
				dir.setCodParaje(rs.getString("codigo_paraje"));

				finca.setDirParcela(dir);

				DatosFisicosFinca datosFisicos = new DatosFisicosFinca();
				datosFisicos.setSupFinca(TypeUtil.getLong(rs, "superficie_finca"));
				datosFisicos.setSupTotal(TypeUtil.getLong(rs,"superficie_construida_total"));
				datosFisicos.setSupSobreRasante(TypeUtil.getLong(rs,"superficie_const_sobrerasante"));
				datosFisicos.setSupBajoRasante(TypeUtil.getLong(rs,"superficie_const_bajorasante"));
				datosFisicos.setSupCubierta(TypeUtil.getLong(rs,"superficie_cubierta"));
				datosFisicos.setXCoord(TypeUtil.getFloat(rs,"coordenada_x"));
				datosFisicos.setYCoord(TypeUtil.getFloat(rs,"coordenada_y"));

				String geometry = rs.getString("GEOMETRY");
				if(geometry != null && !geometry.equalsIgnoreCase("") &&
						( datosFisicos.getXCoord() != null || datosFisicos.getYCoord() != null ))
					datosFisicos.setSRS(geometry.substring(5, 10));

				finca.setDatosFisicos(datosFisicos);

				//Datos economicos
				DatosEconomicosFinca datosEconomicosFinca = new DatosEconomicosFinca();
				datosEconomicosFinca.setAnioAprobacion(TypeUtil.getInteger(rs,"anio_aprobacion"));
				datosEconomicosFinca.setCodigoCalculoValor(TypeUtil.getInteger(rs,"codigo_calculo_valor"));
				datosEconomicosFinca.setIndInfraedificabilidad(rs.getString("indicador_infraedificabilidad"));
				datosEconomicosFinca.setIndModalidadReparto(rs.getString("modalidad_reparto"));

				//Ponencia de poligono
				PonenciaPoligono poligonoCatastralValor = new PonenciaPoligono();
				poligonoCatastralValor.setCodPoligono(rs.getString("poligono_catastral_valoracion"));
				datosEconomicosFinca.setPoligonoCatastralValor(poligonoCatastralValor);
				finca.setDatosEconomicos(datosEconomicosFinca);

				ArrayList lstBIs = getLstBIs(finca, idMunicipioFinca);
				finca.setLstBienesInmuebles(lstBIs);

				ArrayList lstSuelos = getLstSuelos(finca, idMunicipioFinca);
				finca.setLstSuelos(lstSuelos);

				ArrayList lstUnidadesConstructivas = getUnidadConstructiva(finca, idMunicipioFinca);
				finca.setLstUnidadesConstructivas(lstUnidadesConstructivas);

				ArrayList lstConstrucciones = getConstrucciones(finca);
				finca.setLstConstrucciones(lstConstrucciones);

				ArrayList lstCultivos = getCultivo(finca);
				finca.setLstCultivos(lstCultivos);

				ArrayList lstRepartos = getRepartos(finca);
				finca.setLstReparto(lstRepartos);

				finca.setFxcc(getFXCC(rs.getInt("id")));//el id es NOT NULL en la BD

				ArrayList lstImagenes = getImagenes(finca);
				finca.setLstImagenes(lstImagenes);

				//lstFincas.add(finca);
			}
			//exp.setListaReferencias(lstFincas);
			return finca;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{psVia.close();}catch(Exception e){};
			try{psProvincia.close();}catch(Exception e){};
			try{psMunicipio.close();}catch(Exception e){};
			try{rs.close();}catch(Exception e){};
			try{rsVia.close();}catch(Exception e){};
			try{rsMunicipio.close();}catch(Exception e){};
			try{rsProvincia.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}

	}

	public int getIdFincaCatastro(ObjectOutputStream oos, String idMunicipio, String refCatastral) throws Exception {
		PreparedStatement ps= null;
		Connection conn= null;
		ResultSet rs= null;
		int idFinca  = -1;
		try {

			String query = "SELECT id FROM parcelas WHERE referencia_catastral = '" + refCatastral + "' " +
					" AND id_municipio = '" +idMunicipio+"'";
			
			conn= CPoolDatabase.getConnection();
			ps= conn.prepareStatement(query);
			rs =ps.executeQuery();

			if(rs.next()){
				idFinca = rs.getInt("id");
			}

		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return idFinca;
	}
	
//	public int getIdBien(ObjectOutputStream oos, String idMunicipio, String idBien) throws Exception {
//		PreparedStatement ps= null;
//		Connection conn= null;
//		ResultSet rs= null;
//		int idBien = -1;
//		try {
//
//			String query = "SELECT id FROM parcelas WHERE referencia_catastral = " + idBien + " " +
//					" AND id_municipio = '" +idMunicipio+"'";
//			
//			conn= CPoolDatabase.getConnection();
//			ps= conn.prepareStatement(query);
//			rs =ps.executeQuery();
//
//			if(rs.next()){
//				idBien = rs.getInt("id");
//			}
//
//		}
//		catch (Exception e)
//		{
//			throw e;
//		}
//		finally
//		{
//			try{ps.close();}catch(Exception e){};
//			try{conn.close();}catch(Exception e){};
//		}
//		return idBien;
//	}
	
	
}
