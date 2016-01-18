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


package com.geopista.app.eiel.utils;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.geopista.app.AppConstants;
import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.AbastecimientoAutonomoEIEL;
import com.geopista.app.eiel.beans.CabildoConsejoEIEL;
import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.CasasConsistorialesEIEL;
import com.geopista.app.eiel.beans.CementeriosEIEL;
import com.geopista.app.eiel.beans.CentrosAsistencialesEIEL;
import com.geopista.app.eiel.beans.CentrosCulturalesEIEL;
import com.geopista.app.eiel.beans.CentrosEnsenianzaEIEL;
import com.geopista.app.eiel.beans.CentrosSanitariosEIEL;
import com.geopista.app.eiel.beans.ColectorEIEL;
import com.geopista.app.eiel.beans.DepositosEIEL;
import com.geopista.app.eiel.beans.Depuradora1EIEL;
import com.geopista.app.eiel.beans.Depuradora2EIEL;
import com.geopista.app.eiel.beans.DiseminadosEIEL;
import com.geopista.app.eiel.beans.EdificiosSinUsoEIEL;
import com.geopista.app.eiel.beans.EmisariosEIEL;
import com.geopista.app.eiel.beans.Encuestados1EIEL;
import com.geopista.app.eiel.beans.Encuestados2EIEL;
import com.geopista.app.eiel.beans.EntidadEIEL;
import com.geopista.app.eiel.beans.EntidadesAgrupadasEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.IncendiosProteccionEIEL;
import com.geopista.app.eiel.beans.InstalacionesDeportivasEIEL;
import com.geopista.app.eiel.beans.LonjasMercadosEIEL;
import com.geopista.app.eiel.beans.MataderosEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.NivelesCentrosEnsenianza;
import com.geopista.app.eiel.beans.NucleoCaptacion;
import com.geopista.app.eiel.beans.NucleoColector;
import com.geopista.app.eiel.beans.NucleoDepositos;
import com.geopista.app.eiel.beans.NucleoDepuradora1;
import com.geopista.app.eiel.beans.NucleoEmisario;
import com.geopista.app.eiel.beans.NucleoEncuestado7EIEL;
import com.geopista.app.eiel.beans.NucleoPuntosVertido;
import com.geopista.app.eiel.beans.NucleoTramosConduccion;
import com.geopista.app.eiel.beans.NucleoTratamientosPotabilizacion;
import com.geopista.app.eiel.beans.NucleoVertedero;
import com.geopista.app.eiel.beans.NucleosAbandonadosEIEL;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.beans.OtrosServMunicipalesEIEL;
import com.geopista.app.eiel.beans.PadronMunicipiosEIEL;
import com.geopista.app.eiel.beans.PadronNucleosEIEL;
import com.geopista.app.eiel.beans.ParquesJardinesEIEL;
import com.geopista.app.eiel.beans.PlaneamientoUrbanoEIEL;
import com.geopista.app.eiel.beans.PoblamientoEIEL;
import com.geopista.app.eiel.beans.PuntoVertidoEmisario;
import com.geopista.app.eiel.beans.PuntosVertidoEIEL;
import com.geopista.app.eiel.beans.RecogidaBasurasEIEL;
import com.geopista.app.eiel.beans.SaneamientoAutonomoEIEL;
import com.geopista.app.eiel.beans.ServiciosAbastecimientosEIEL;
import com.geopista.app.eiel.beans.ServiciosRecogidaBasuraEIEL;
import com.geopista.app.eiel.beans.ServiciosSaneamientoEIEL;
import com.geopista.app.eiel.beans.TanatoriosEIEL;
import com.geopista.app.eiel.beans.TipoDeporte;
import com.geopista.app.eiel.beans.TramosCarreterasEIEL;
import com.geopista.app.eiel.beans.TramosConduccionEIEL;
import com.geopista.app.eiel.beans.TratamientosPotabilizacionEIEL;
import com.geopista.app.eiel.beans.UsosCasasConsistoriales;
import com.geopista.app.eiel.beans.UsosCentrosCulturales;
import com.geopista.app.eiel.beans.VertederosEIEL;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.server.database.CPoolDatabase;

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
	 * Obtiene un arraylist de objetos de tipo Provincia con todas las provincias
	 * del sistema
	 * 
	 * @return ArrayList de objetos Provincia
	 * @throws DataException Si se produce un error de acceso a datos 
	 */
	public ArrayList obtenerProvincias () 
	{        
		ArrayList lstProvincias = new ArrayList();
		PreparedStatement s = null;
		ResultSet r = null;
		
		if (ConstantesLocalGISEIEL.idProvincia == null){
			try
			{        


				s = conn.prepareStatement("MCobtenerProvincias");
				r = s.executeQuery();  
				String provincia = new String();
				lstProvincias.add(provincia);

				while (r.next())
				{
					provincia = r.getString("id");
					lstProvincias.add(provincia); 
				}        

			}
			catch (SQLException ex)
			{           
				new DataException(ex);
			}
			finally{
				safeClose(conn, s, r);
			}
		}
		else{
			lstProvincias.add("");
			lstProvincias.add(ConstantesLocalGISEIEL.idProvincia);
		}

		return lstProvincias;
	}

	
	
	
	public ArrayList obtenerProvinciasConNombre() 
	{        
		ArrayList lstProvincias = new ArrayList();

		PreparedStatement s = null;
		ResultSet r = null;
		if (ConstantesLocalGISEIEL.idProvincia == null){
			try
			{        
				s = conn.prepareStatement("MCobtenerProvincias");
				r = s.executeQuery();  
				String provincia = new String();
				lstProvincias.add(provincia);

				while (r.next())
				{
					provincia = r.getString("id");
					lstProvincias.add(provincia); 
				}        

			}
			catch (SQLException ex)
			{           
				new DataException(ex);
			}
			finally{
				safeClose(conn, s, r);
			}
		}
		else{
			Provincia provincia = null;

			provincia = new Provincia();
			provincia.setIdProvincia("");
			provincia.setNombreOficial("");
			lstProvincias.add(provincia);


			provincia = new Provincia();
			provincia.setIdProvincia(ConstantesLocalGISEIEL.idProvincia);
			provincia.setNombreOficial(ConstantesLocalGISEIEL.Provincia);
			lstProvincias.add(provincia);
		}

		return lstProvincias;
	}

	public ArrayList obtenerTodasProvincias () 
	{        
		ArrayList lstProvincias = new ArrayList();
		Provincia provincia = null;
		PreparedStatement s = null;
		ResultSet r = null;
		
		try
		{        
			s = conn.prepareStatement("MCobtenerProvincias");
			r = s.executeQuery();  
			String id_provincia = new String();
			provincia = new Provincia();
			provincia.setIdProvincia("");
			provincia.setNombreOficial("");
			lstProvincias.add(provincia);

			while (r.next())
			{
				provincia = new Provincia();
				provincia.setNombreOficial(r.getString("nombreoficial"));
				provincia.setIdProvincia(r.getString("id"));
				lstProvincias.add(provincia); 
			}        

		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, s, r);
		}


		return lstProvincias;
	}



	public CaptacionesEIEL getCaptacionEIEL(int idCaptacion){


		CaptacionesEIEL captacion = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{        
			ps = conn.prepareStatement("EIELgetCaptacion");
			ps.setInt(1, idCaptacion);
			rs = ps.executeQuery();  

			while (rs.next())
			{
				captacion = new CaptacionesEIEL();
				captacion.setClave(rs.getString("clave"));
				captacion.setCodINEProvincia(rs.getString("codprov"));
				captacion.setCodINEMunicipio(rs.getString("codmunic"));
				captacion.setCodOrden(rs.getString("orden_ca"));
			}  
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

		return captacion;

	}

	public CaptacionesEIEL getPanelCaptacionEIEL(String clave, String codprov, String codmunic, String orden ) {
		CaptacionesEIEL captacion = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("EIELgetPanelCaptacion");
			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, orden);
			rs = ps.executeQuery();
			while (rs.next()) {
				captacion = new CaptacionesEIEL();
				captacion.setClave(rs.getString("clave"));
				captacion.setCodINEProvincia(rs.getString("codprov"));
				captacion.setCodINEMunicipio(rs.getString("codmunic"));
				captacion.setCodOrden(rs.getString("orden_ca"));
				captacion.setNombre(rs.getString("nombre"));
				captacion.setTipo(rs.getString("tipo"));
				captacion.setTitularidad(rs.getString("titular"));
				captacion.setGestion(rs.getString("gestor"));
				captacion.setSistema(rs.getString("sist_impulsion"));
				captacion.setEstado(rs.getString("estado"));
				captacion.setTipoUso(rs.getString("uso"));
				captacion.setProteccion(rs.getString("proteccion"));
				captacion.setContador(rs.getString("contador"));
				captacion.setObservaciones(rs.getString("observ"));
				captacion.setFechaRevision(rs.getDate("fecha_revision"));
				captacion.setEstadoRevision(rs.getInt("estado_revision"));
				captacion.setFechaInst(rs.getDate("fecha_inst"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

		return captacion;
	}
	
	public EntidadesAgrupadasEIEL getPanelEntidadesAgrupadasEIEL( String codmunicipio) {
		EntidadesAgrupadasEIEL entidadesAgrupadas = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("EIELgetPanelAgrupaciones6000");
			ps.setString(1, codmunicipio);

			rs = ps.executeQuery();
			while (rs.next()) {
				entidadesAgrupadas = new EntidadesAgrupadasEIEL();
				entidadesAgrupadas.setCodINEMunicipio(rs.getString("codmunicipio"));
				entidadesAgrupadas.setCodINEEntidad(rs.getString("codentidad"));
				entidadesAgrupadas.setCodINENucleo(rs.getString("codnucleo"));
				entidadesAgrupadas.setCodINEEntidad_agrupada(rs.getString("codentidad_agrupada"));
				entidadesAgrupadas.setCodINENucleo_agrupado(rs.getString("codnucleo_agrupado"));

			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

		return entidadesAgrupadas;
	}

	public DepositosEIEL getDepositosEIEL(int idDepositos){


		DepositosEIEL depositos = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        
			ps = conn.prepareStatement("EIELgetDepositos");
			ps.setInt(1, idDepositos);
			rs = ps.executeQuery();  

			while (rs.next())
			{
				depositos = new DepositosEIEL();
				depositos.setClave(rs.getString("clave"));
				depositos.setCodINEProvincia(rs.getString("codprov"));
				depositos.setCodINEMunicipio(rs.getString("codmunic"));
				depositos.setOrdenDeposito(rs.getString("orden_de"));
			}  
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

		return depositos;

	}

	public DepositosEIEL getPanelDepositoEIEL(String clave, String codprov, String codmunic, String orden ) {
		DepositosEIEL deposito = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("EIELgetPanelDeposito");
			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, orden);
			rs = ps.executeQuery();
			while (rs.next()) {
				deposito = new DepositosEIEL();
				deposito.setClave(rs.getString("clave"));
				deposito.setCodINEProvincia(rs.getString("codprov"));
				deposito.setCodINEMunicipio(rs.getString("codmunic"));
				deposito.setOrdenDeposito(rs.getString("orden_de"));
				deposito.setUbicacion(rs.getString("ubicacion"));
				deposito.setTitularidad(rs.getString("titular"));
				deposito.setGestor(rs.getString("gestor"));
				deposito.setCapacidad(rs.getInt("capacidad"));
				deposito.setEstado(rs.getString("estado"));
				deposito.setProteccion(rs.getString("proteccion"));
				deposito.setFechaLimpieza(rs.getString("fecha_limpieza"));
				deposito.setContador(rs.getString("contador"));
				deposito.setObservaciones(rs.getString("obsev"));
				deposito.setFechaInstalacion(rs.getDate("fecha_inst"));
				deposito.setFechaRevision(rs.getDate("fecha_revision"));
				deposito.setEstadoRevision(rs.getInt("estado_revision"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return deposito;
	}

	public Depuradora1EIEL getPanelDepuradora1EIEL(String clave, String codprov, String codmunic, String orden ) {
		Depuradora1EIEL depuradora1 = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelDepuradora1");
			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, orden);
			rs = ps.executeQuery();
			while (rs.next()) {
				depuradora1 = new Depuradora1EIEL();
				depuradora1.setClave(rs.getString("clave"));
				depuradora1.setCodINEProvincia(rs.getString("codprov"));
				depuradora1.setCodINEMunicipio(rs.getString("codmunic"));
				depuradora1.setCodOrden(rs.getString("orden_ed"));

				depuradora1.setTratPrimario1(rs.getString("trat_pr_1"));
				depuradora1.setTratPrimario2(rs.getString("trat_pr_2"));
				depuradora1.setTratPrimario3(rs.getString("trat_pr_3"));

				depuradora1.setTratSecundario1(rs.getString("trat_sc_1"));
				depuradora1.setTratSecundario2(rs.getString("trat_sc_2"));
				depuradora1.setTratSecundario3(rs.getString("trat_sc_3"));

				depuradora1.setTratAvanzado1(rs.getString("trat_av_1"));
				depuradora1.setTratAvanzado2(rs.getString("trat_av_2"));
				depuradora1.setTratAvanzado3(rs.getString("trat_av_3"));

				depuradora1.setProcComplementario1(rs.getString("proc_cm_1"));
				depuradora1.setProcComplementario2(rs.getString("proc_cm_2"));
				depuradora1.setProcComplementario3(rs.getString("proc_cm_3"));

				depuradora1.setTratLodos1(rs.getString("trat_ld_1"));
				depuradora1.setTratLodos2(rs.getString("trat_ld_2"));
				depuradora1.setTratLodos3(rs.getString("trat_ld_3"));

				depuradora1.setFechaRevision(rs.getDate("fecha_revision"));
				depuradora1.setEstadoRevision(rs.getInt("estado_revision"));
			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return depuradora1;
	}

	public Depuradora2EIEL getPanelDepuradora2EIEL(String clave, String codprov, String codmunic, String orden ) {
		Depuradora2EIEL depuradora2 = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("EIELgetPanelDepuradora2");
			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, orden);
			rs = ps.executeQuery();
			while (rs.next()) {
				depuradora2 = new Depuradora2EIEL();
				depuradora2.setClave(rs.getString("clave"));
				depuradora2.setCodINEProvincia(rs.getString("codprov"));
				depuradora2.setCodINEMunicipio(rs.getString("codmunic"));
				depuradora2.setCodOrden(rs.getString("orden_ed"));

				depuradora2.setTitular("titular");
				depuradora2.setGestor(rs.getString("gestor"));
				depuradora2.setCapacidad(rs.getInt("capacidad"));

				depuradora2.setProblemas1(rs.getString("problem_1"));
				depuradora2.setProblemas2(rs.getString("problem_2"));
				depuradora2.setProblemas3(rs.getString("problem_3"));

				depuradora2.setLodosVertedero(rs.getInt("lodo_vert"));
				depuradora2.setLodosIncineracion(rs.getInt("lodo_inci"));
				depuradora2.setLodosAgrConCompostaje(rs.getInt("lodo_con_agri"));
				depuradora2.setLodosAgrSinCompostaje(rs.getInt("lodo_sin_agri"));
				depuradora2.setLodosOtroFinal(rs.getInt("lodo_ot"));

				depuradora2.setFechaInstalacion(rs.getDate("fech_inst"));
				depuradora2.setObservaciones(rs.getString("observ"));

				depuradora2.setFechaRevision(rs.getDate("fecha_revision"));
				depuradora2.setEstadoRevision(rs.getInt("estado_revision"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return depuradora2;
	}


	public AbastecimientoAutonomoEIEL getPanelAbastecimientoAutonomoEIEL(String codprov, String codmunic, String entidad, String nucleo ) {
		AbastecimientoAutonomoEIEL abastecimiento = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelAbastecimientoAutonomo");
			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			ps.setString(3, entidad);
			ps.setString(4, nucleo);
			rs = ps.executeQuery();
			while (rs.next()) {
				abastecimiento = new AbastecimientoAutonomoEIEL();
				abastecimiento.setClave(rs.getString("clave"));
				abastecimiento.setCodINEProvincia(rs.getString("codprov"));
				abastecimiento.setCodINEMunicipio(rs.getString("codmunic"));
				abastecimiento.setCodINEEntidad(rs.getString("codentidad"));
				abastecimiento.setCodINENucleo(rs.getString("codpoblamiento"));

				abastecimiento.setViviendas(rs.getInt("aau_vivien"));
				abastecimiento.setPoblacionResidente(rs.getInt("aau_pob_re"));
				abastecimiento.setPoblacionEstacional(rs.getInt("aau_pob_es"));

				abastecimiento.setViviendasDeficitarias(rs.getInt("aau_def_vi"));
				abastecimiento.setPoblacionResidenteDef(rs.getInt("aau_def_re"));
				abastecimiento.setPoblacionEstacionalDef(rs.getInt("aau_def_es"));

				abastecimiento.setFuentesControladas(rs.getInt("aau_fecont"));
				abastecimiento.setFuentesNoControladas(rs.getInt("aau_fencon"));

				abastecimiento.setSuficienciaCaudal(rs.getString("aau_caudal"));

				abastecimiento.setObservaciones(rs.getString("observ"));

				abastecimiento.setFechaRevision(rs.getDate("fecha_revision"));
				abastecimiento.setEstadoRevision(rs.getInt("estado_revision"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}		
		return abastecimiento;
	}



	public ServiciosAbastecimientosEIEL getPanelServiciosAbastecimientosEIEL(String codprov, String codmunic, String entidad, String nucleo ) {
		ServiciosAbastecimientosEIEL serviciosAbastecimientos = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelServiciosAbastecimientos");
			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			ps.setString(3, entidad);
			ps.setString(4, nucleo);
			rs = ps.executeQuery();
			while (rs.next()) {
				serviciosAbastecimientos = new ServiciosAbastecimientosEIEL();
				serviciosAbastecimientos.setCodINEProvincia(rs.getString("codprov"));
				serviciosAbastecimientos.setCodINEMunicipio(rs.getString("codmunic"));
				serviciosAbastecimientos.setCodINEEntidad(rs.getString("codentidad"));
				serviciosAbastecimientos.setCodINEPoblamiento(rs.getString("codpoblamiento"));

				serviciosAbastecimientos.setViviendasConectadas(rs.getInt("viviendas_c_conex"));
				serviciosAbastecimientos.setViviendasNoConectadas(rs.getInt("viviendas_s_conexion"));
				serviciosAbastecimientos.setConsumoInvierno(rs.getInt("consumo_inv"));
				serviciosAbastecimientos.setConsumoVerano(rs.getInt("consumo_verano"));

				serviciosAbastecimientos.setViviendasExcesoPresion(rs.getInt("viv_exceso_pres"));
				serviciosAbastecimientos.setViviendasDeficitPresion(rs.getInt("viv_defic_presion"));

				serviciosAbastecimientos.setPerdidasAgua(rs.getInt("perdidas_agua"));
				serviciosAbastecimientos.setCalidadServicio(rs.getString("calidad_serv"));


				serviciosAbastecimientos.setLongitudDeficitaria(rs.getInt("long_deficit"));
				serviciosAbastecimientos.setViviendasDeficitarias(rs.getInt("viv_deficitarias"));
				serviciosAbastecimientos.setPoblacionResidenteDeficitaria(rs.getInt("plob_res_afect"));
				serviciosAbastecimientos.setPoblacionEstacionalDeficitaria(rs.getInt("plob_est_afect"));


				serviciosAbastecimientos.setObservaciones(rs.getString("observ"));

				serviciosAbastecimientos.setFechaRevision(rs.getDate("fecha_revision"));
				serviciosAbastecimientos.setEstadoRevision(rs.getInt("estado_revision"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}	
		return serviciosAbastecimientos;
	}


	public TratamientosPotabilizacionEIEL getPanelTratamientosPotabilizacionEIEL(String clave, String codprov, String codmunic, String orden ) {
		TratamientosPotabilizacionEIEL tratamientos = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelTratamientosPotabilizacion");
			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, orden);
			rs = ps.executeQuery();
			while (rs.next()) {
				tratamientos = new TratamientosPotabilizacionEIEL();
				tratamientos.setClave(rs.getString("clave"));
				tratamientos.setCodINEProvincia(rs.getString("codprov"));
				tratamientos.setCodINEMunicipio(rs.getString("codmunic"));
				tratamientos.setOrdenPotabilizadora((rs.getString("orden_tp")));

				tratamientos.setTipo(rs.getString("tipo"));

				tratamientos.setUbicacion(rs.getString("ubicacion"));

				tratamientos.setSoloDesinfeccion(rs.getString("s_desinf"));

				tratamientos.setCategoriaA1(rs.getString("categoria_a1"));
				tratamientos.setCategoriaA2(rs.getString("categoria_a2"));
				tratamientos.setCategoriaA3(rs.getString("categoria_a3"));

				tratamientos.setDesaladora(rs.getString("desaladora"));
				tratamientos.setOtros(rs.getString("otros"));

				tratamientos.setMetodoDesinfeccion1(rs.getString("desinf_1"));
				tratamientos.setMetodoDesinfeccion2(rs.getString("desinf_2"));
				tratamientos.setMetodoDesinfeccion3(rs.getString("desinf_3"));

				tratamientos.setPerioricidad(rs.getString("periodicidad"));
				tratamientos.setOrganismoControl(rs.getString("organismo_control"));
				tratamientos.setEstado(rs.getString("estado"));
				tratamientos.setFechaInstalacion(rs.getDate("fecha_inst"));

				tratamientos.setFechaRevision(rs.getDate("fecha_revision"));
				tratamientos.setEstadoRevision(rs.getInt("estado_revision"));
			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}	
		return tratamientos;
	}


	public CentrosAsistencialesEIEL getPanelCentrosAsistencialesEIEL(String clave, String codprov, String codmunic, String entidad, String nucleo, String orden ) {
		CentrosAsistencialesEIEL centrosAsistenciales = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelCentrosAsistenciales");
			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, entidad);
			ps.setString(5, nucleo);
			ps.setString(6, orden);
			rs = ps.executeQuery();

			while (rs.next()) {

				centrosAsistenciales = new CentrosAsistencialesEIEL();
				centrosAsistenciales.setClave(rs.getString("clave"));
				centrosAsistenciales.setCodINEProvincia(rs.getString("codprov"));
				centrosAsistenciales.setCodINEMunicipio(rs.getString("codmunic"));
				centrosAsistenciales.setCodINEEntidad(rs.getString("codentidad"));
				centrosAsistenciales.setCodINEPoblamiento((rs.getString("codpoblamiento")));
				centrosAsistenciales.setOrdenAsistencial(rs.getString("orden_as"));

				centrosAsistenciales.setNombre(rs.getString("nombre"));
				centrosAsistenciales.setTipo(rs.getString("tipo"));

				centrosAsistenciales.setTitularidad(rs.getString("titular"));
				centrosAsistenciales.setGestion(rs.getString("gestor"));
				centrosAsistenciales.setPlazas(rs.getInt("plazas"));

				centrosAsistenciales.setSuperficieCubierta(rs.getInt("s_cubierta"));
				centrosAsistenciales.setSuperficieAireLibre(rs.getInt("s_aire"));
				centrosAsistenciales.setSuperficieSolar(rs.getInt("s_solar"));

				centrosAsistenciales.setEstado(rs.getString("estado"));
				centrosAsistenciales.setFechaIstalacion(rs.getDate("fecha_inst"));
				centrosAsistenciales.setObservaciones(rs.getString("observ"));
				centrosAsistenciales.setFechaRevision(rs.getDate("fecha_revision"));
				centrosAsistenciales.setEstadoRevision(rs.getInt("estado_revision"));
			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}	
		return centrosAsistenciales;
	}


	public TramosCarreterasEIEL getPanelCarreterasEIEL(String codprov, String codcarretera) {
		TramosCarreterasEIEL carretera = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelCarreteras");
			ps.setString(1, codprov);
			ps.setString(2, codcarretera);
			rs = ps.executeQuery();
			while (rs.next()) {
				carretera = new TramosCarreterasEIEL();

				carretera.setCodINEProvincia(rs.getString("codprov"));
				carretera.setCodCarretera(rs.getString("cod_carrt"));
				carretera.setClaseVia(rs.getString("clase_via"));
				carretera.setDenominacion(rs.getString("denominacion"));
				carretera.setTitularidad(rs.getString("titular_via"));
				carretera.setFechaActualizacion(rs.getDate("fecha_act"));
				carretera.setObservaciones(rs.getString("observ"));

			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}	
		return carretera;
	}


	public CabildoConsejoEIEL getPanelCabildoConsejoEIEL(String codprov, String codisla) {
		CabildoConsejoEIEL cabildoConsejo = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelCabildoConsejo");
			ps.setString(1, codprov);
			ps.setString(2, codisla);
			rs = ps.executeQuery();
			while (rs.next()) {
				cabildoConsejo = new CabildoConsejoEIEL();

				cabildoConsejo.setCodINEProvincia(rs.getString("codprov"));
				cabildoConsejo.setCodIsla(rs.getString("cod_isla"));
				cabildoConsejo.setDenominacion(rs.getString("denominacion"));
			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}	
		return cabildoConsejo;
	}




	public CasasConsistorialesEIEL getPanelCasasConsistorialesEIEL(String clave, String codprov, String codmunic, String entidad, String nucleo, String orden ) {

		CasasConsistorialesEIEL casaConsistorial = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelCasasConsistoriales");
			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, entidad);
			ps.setString(5, nucleo);
			ps.setString(6, orden);
			rs = ps.executeQuery();

			while (rs.next()) {

				casaConsistorial = new CasasConsistorialesEIEL();
				casaConsistorial.setClave(rs.getString("clave"));
				casaConsistorial.setCodINEProvincia(rs.getString("codprov"));
				casaConsistorial.setCodINEMunicipio(rs.getString("codmunic"));
				casaConsistorial.setCodINEEntidad(rs.getString("codentidad"));
				casaConsistorial.setCodINEPoblamiento((rs.getString("codpoblamiento")));
				casaConsistorial.setCodOrden(rs.getString("orden_cc"));

				casaConsistorial.setNombre(rs.getString("nombre"));
				casaConsistorial.setTipo(rs.getString("tipo"));
				casaConsistorial.setTitular(rs.getString("titular"));
				casaConsistorial.setTenencia(rs.getString("tenencia"));


				casaConsistorial.setSupCubierta(rs.getInt("s_cubierta"));
				casaConsistorial.setSupAire(rs.getInt("s_aire"));
				casaConsistorial.setSupSolar(rs.getInt("s_solar"));

				casaConsistorial.setEstado(rs.getString("estado"));
				casaConsistorial.setFechaInstalacion(rs.getDate("fecha_inst"));
				casaConsistorial.setObservaciones(rs.getString("observ"));
				casaConsistorial.setFechaRevision(rs.getDate("fecha_revision"));
				casaConsistorial.setEstadoRevision(rs.getInt("estado_revision"));

			}
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();

			ps = conn.prepareStatement("EIELgetPanelUsosCasasConsistoriales");

			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, entidad);
			ps.setString(5, nucleo);
			ps.setString(6, orden);
			rs = ps.executeQuery();

			while (rs.next()) {

				UsosCasasConsistoriales uso = new UsosCasasConsistoriales();

				uso.setCodigoOrdenUso(rs.getString("orden_uso"));
				uso.setUso(rs.getString("uso"));
				uso.setSuperficieUso(new Integer(rs.getInt("s_cubierta")));
				uso.setFechaUso(rs.getDate("fecha_ini"));
				uso.setObservacionesUso(rs.getString("observ"));
				uso.setInstPertenece(rs.getString("inst_pertenece"));

				casaConsistorial.getListaUsos().add(uso);
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return casaConsistorial;
	}


	public CementeriosEIEL getPanelCementeriosEIEL(String clave, String codprov, String codmunic, String entidad, String nucleo, String orden ) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		CementeriosEIEL cementerio = null;

		try {

			ps = conn.prepareStatement("EIELgetPanelCementerios");
			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, entidad);
			ps.setString(5, nucleo);
			ps.setString(6, orden);
			rs = ps.executeQuery();
			while (rs.next()) {
				cementerio = new CementeriosEIEL();
				cementerio.setClave(rs.getString("clave"));
				cementerio.setCodINEProvincia(rs.getString("codprov"));
				cementerio.setCodINEMunicipio(rs.getString("codmunic"));
				cementerio.setCodINEEntidad(rs.getString("codentidad"));
				cementerio.setCodINEPoblamiento((rs.getString("codpoblamiento")));
				cementerio.setOrden(rs.getString("orden_ce"));

				cementerio.setNombre(rs.getString("nombre"));
				cementerio.setTitular(rs.getString("titular"));

				cementerio.setDistancia(rs.getFloat("distancia"));
				cementerio.setAcceso(rs.getString("acceso"));
				cementerio.setCapilla(rs.getString("capilla"));
				cementerio.setDepositoCadaveres(rs.getString("deposito"));
				cementerio.setAmpliacion(rs.getString("ampliacion"));
				cementerio.setSaturacion(rs.getFloat("saturacion"));
				cementerio.setSuperficie(rs.getInt("superficie"));
				cementerio.setCrematorio(rs.getString("crematorio"));



				cementerio.setFechaInstalacion(rs.getDate("fecha_inst"));
				cementerio.setObservaciones(rs.getString("observ"));
				cementerio.setFechaRevision(rs.getDate("fecha_revision"));
				cementerio.setEstadoRevision(rs.getInt("estado_revision"));
			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return cementerio;
	}


	public CentrosCulturalesEIEL getPanelCentrosCulturalesEIEL(String clave, String codprov, String codmunic, String entidad, String nucleo, String orden ) {

		CentrosCulturalesEIEL centroCultural = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			ps = conn.prepareStatement("EIELgetPanelCentrosCulturales");
			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, entidad);
			ps.setString(5, nucleo);
			ps.setString(6, orden);
			rs = ps.executeQuery();

			while (rs.next()) {

				centroCultural = new CentrosCulturalesEIEL();
				centroCultural.setClave(rs.getString("clave"));
				centroCultural.setCodINEProvincia(rs.getString("codprov"));
				centroCultural.setCodINEMunicipio(rs.getString("codmunic"));
				centroCultural.setCodINEEntidad(rs.getString("codentidad"));
				centroCultural.setCodINEPoblamiento((rs.getString("codpoblamiento")));
				centroCultural.setCodOrden(rs.getString("orden_cu"));

				centroCultural.setNombre(rs.getString("nombre"));
				centroCultural.setTipo(rs.getString("tipo"));
				centroCultural.setTitular(rs.getString("titular"));
				centroCultural.setGestor(rs.getString("gestor"));


				centroCultural.setSupCubierta(rs.getInt("s_cubierta"));
				centroCultural.setSupAire(rs.getInt("s_aire"));
				centroCultural.setSupSolar(rs.getInt("s_solar"));

				centroCultural.setEstado(rs.getString("estado"));
				centroCultural.setFechaInstalacion(rs.getDate("fecha_inst"));
				centroCultural.setObservaciones(rs.getString("observ"));
				centroCultural.setFechaRevision(rs.getDate("fecha_revision"));
				centroCultural.setEstadoRevision(rs.getInt("estado_revision"));
				centroCultural.setInstPertenece(rs.getString("inst_pertenece"));
			}
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
			ps = conn.prepareStatement("EIELgetPanelUsosCentrosCulturales");
			ps.setString(1, centroCultural.getClave());
			ps.setString(2, centroCultural.getCodINEProvincia());
			ps.setString(3, centroCultural.getCodINEMunicipio());
			ps.setString(4, centroCultural.getCodINEEntidad());
			ps.setString(5, centroCultural.getCodINEPoblamiento());
			ps.setString(6, centroCultural.getCodOrden());

			rs = ps.executeQuery();

			while (rs.next()) {

				UsosCentrosCulturales uso = new UsosCentrosCulturales();

				uso.setCodigoOrdenUso(rs.getString("orden_uso"));
				uso.setUso(rs.getString("uso"));
				uso.setSuperficieUso(new Integer(rs.getInt("s_cubierta")));
				uso.setFechaUso(rs.getDate("fecha_ini"));
				uso.setObservacionesUso(rs.getString("observ"));
				centroCultural.getListaUsos().add(uso);

			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return centroCultural;
	}


	public TratamientosPotabilizacionEIEL getTratamientosPotabilizacionEIEL(int idTratamientos){


		TratamientosPotabilizacionEIEL tratamientos = null;

		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        


			ps = conn.prepareStatement("EIELgetTratamientosPotabilizacion");
			ps.setInt(1, idTratamientos);
			rs = ps.executeQuery();  

			while (rs.next())
			{
				tratamientos = new TratamientosPotabilizacionEIEL();
				tratamientos.setClave(rs.getString("clave"));
				tratamientos.setCodINEProvincia(rs.getString("codprov"));
				tratamientos.setCodINEMunicipio(rs.getString("codmunic"));
				tratamientos.setOrdenPotabilizadora(rs.getString("orden_tp"));
			}  

		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

		return tratamientos;

	}

	public PuntosVertidoEIEL getPuntosVertidoEIEL(int idVertido){


		PuntosVertidoEIEL vertido = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{       
			ps = conn.prepareStatement("EIELgetPuntosVertido");
			ps.setInt(1, idVertido);
			rs = ps.executeQuery();  

			while (rs.next())
			{
				vertido = new PuntosVertidoEIEL();
				vertido.setClave(rs.getString("clave"));
				vertido.setCodINEProvincia(rs.getString("codprov"));
				vertido.setCodINEMunicipio(rs.getString("codmunic"));
				vertido.setOrden(rs.getString("orden_pv"));
			}  

		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}


		return vertido;

	}

	public Depuradora1EIEL getDepuradora1EIEL(int idDepuradora1){


		Depuradora1EIEL depuradora1 = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        
			ps = conn.prepareStatement("EIELgetDepuradora1");
			ps.setInt(1, idDepuradora1);
			rs = ps.executeQuery();  

			while (rs.next())
			{
				depuradora1 = new Depuradora1EIEL();
				depuradora1.setClave(rs.getString("clave"));
				depuradora1.setCodINEProvincia(rs.getString("codprov"));
				depuradora1.setCodINEMunicipio(rs.getString("codmunic"));
				depuradora1.setCodOrden(rs.getString("orden_ed"));
			}  
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

		return depuradora1;

	}

	public VertederosEIEL getVertederoEIEL(int idVertedero){


		VertederosEIEL vertedero = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        
			ps = conn.prepareStatement("EIELgetVertedero");
			ps.setInt(1, idVertedero);
			rs = ps.executeQuery();  

			while (rs.next())
			{
				vertedero = new VertederosEIEL();
				vertedero.setClave(rs.getString("clave"));
				vertedero.setCodINEProvincia(rs.getString("codprov"));
				vertedero.setCodINEMunicipio(rs.getString("codmunic"));
				vertedero.setCodOrden(rs.getString("orden_vt"));
			}  

		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

		return vertedero;

	}

	public TramosConduccionEIEL getTramosConduccionEIEL(int idConduccion){

		TramosConduccionEIEL conduccion = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        		

			ps = conn.prepareStatement("EIELgetTramosConduccion");
			ps.setInt(1, idConduccion);
			rs = ps.executeQuery();  

			while (rs.next())
			{
				conduccion = new TramosConduccionEIEL();
				conduccion.setClave(rs.getString("clave"));
				conduccion.setCodINEProvincia(rs.getString("codprov"));
				conduccion.setCodINEMunicipio(rs.getString("codmunic"));
				conduccion.setTramo_cn(rs.getString("tramo_cn"));
			}  
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

		return conduccion;

	}

	/**
	 * @author davidcaramazana
	 * Retorna los campos claves del elemento (el resto son null)
	 */
	public ColectorEIEL getColectorEIEL(int idColector){
		ColectorEIEL colector = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{        
			
			ps = conn.prepareStatement("EIELgetColector");
			ps.setInt(1, idColector);
			rs = ps.executeQuery();  
			while (rs.next()) {
				colector = new ColectorEIEL();
				colector.setClave(rs.getString("clave"));
				colector.setCodINEProvincia(rs.getString("codprov"));
				colector.setCodINEMunicipio(rs.getString("codmunic"));
				colector.setCodOrden(rs.getString("orden"));
			}  
		} catch (SQLException ex) {           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return colector;
	}

	/**
	 * @author davidcaramazana
	 * Retorna los campos claves del elemento (el resto son null)
	 */
	public EmisariosEIEL getEmisarioEIEL(int idEmisor){
		EmisariosEIEL Emisor = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{        
			
			ps = conn.prepareStatement("EIELgetEmisor");
			ps.setInt(1, idEmisor);
			rs = ps.executeQuery();  
			while (rs.next()) {
				Emisor = new EmisariosEIEL();
				Emisor.setClave(rs.getString("clave"));
				Emisor.setCodINEProvincia(rs.getString("codprov"));
				Emisor.setCodINEMunicipio(rs.getString("codmunic"));
				Emisor.setCodOrden(rs.getString("orden"));
			}  

		} catch (SQLException ex) {           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return Emisor;
	}

	public void saveNucleoCaptacion(NucleoCaptacion nucleoCaptacion){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        

			boolean actualizar = false;

			if (nucleoCaptacion != null){

				ps = conn.prepareStatement("EIELgetNucleosCaptacion");
				ps.setString(1, nucleoCaptacion.getClaveCaptacion());
				ps.setString(2, nucleoCaptacion.getCodProvCaptacion());
				ps.setString(3, nucleoCaptacion.getCodMunicCaptacion());
				ps.setString(4, nucleoCaptacion.getCodOrdenCaptacion());
				ps.setString(5, nucleoCaptacion.getCodProvNucleo());
				ps.setString(6, nucleoCaptacion.getCodMunicNucleo());
				ps.setString(7, nucleoCaptacion.getCodEntNucleo());
				ps.setString(8, nucleoCaptacion.getCodPoblNucleo());
				rs = ps.executeQuery(); 

				if (rs.next()){
					actualizar = true;
					ps = conn.prepareStatement("EIELupdateNucleosCaptacion");
				}
				else{
					actualizar = false;
					ps = conn.prepareStatement("EIELinsertNucleosCaptacion");
				}

				ps.setString(1, nucleoCaptacion.getClaveCaptacion());
				ps.setString(2, nucleoCaptacion.getCodProvCaptacion());
				ps.setString(3, nucleoCaptacion.getCodMunicCaptacion());
				ps.setString(4, nucleoCaptacion.getCodOrdenCaptacion());

				ps.setString(5, nucleoCaptacion.getCodProvNucleo());
				ps.setString(6, nucleoCaptacion.getCodMunicNucleo());
				ps.setString(7, nucleoCaptacion.getCodEntNucleo());
				ps.setString(8, nucleoCaptacion.getCodPoblNucleo());

				ps.setString(9, nucleoCaptacion.getObservaciones());
				ps.setDate(10, nucleoCaptacion.getFechaInicio());

				ps.setDate(11, nucleoCaptacion.getFechaRevision());
				if (nucleoCaptacion.getEstadoRevision()!=null)
					ps.setInt(12, nucleoCaptacion.getEstadoRevision().intValue());
				else
					ps.setNull(12, java.sql.Types.INTEGER);
				
				if (actualizar){
					ps.setString(13, nucleoCaptacion.getClaveCaptacion());
					ps.setString(14, nucleoCaptacion.getCodProvCaptacion());
					ps.setString(15, nucleoCaptacion.getCodMunicCaptacion());
					ps.setString(16, nucleoCaptacion.getCodOrdenCaptacion());
					ps.setString(17, nucleoCaptacion.getCodProvNucleo());
					ps.setString(18, nucleoCaptacion.getCodMunicNucleo());
					ps.setString(19, nucleoCaptacion.getCodEntNucleo());
					ps.setString(20, nucleoCaptacion.getCodPoblNucleo());
				}

				ps.execute();

			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
	}

	/**
	 * @author davidcaramazana
	 */
	public void saveNucleoColector(NucleoColector nucleoColector) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			boolean actualizar = false;
			if (nucleoColector != null) {
				ps = conn.prepareStatement("EIELgetNucleosColector");
				ps.setString(1, nucleoColector.getClaveColector());
				ps.setString(2, nucleoColector.getCodProvColector());
				ps.setString(3, nucleoColector.getCodMunicColector());
				ps.setString(4, nucleoColector.getCodOrdenColector());
				ps.setString(5, nucleoColector.getCodProvNucleo());
				ps.setString(6, nucleoColector.getCodMunicNucleo());
				ps.setString(7, nucleoColector.getCodEntNucleo());
				ps.setString(8, nucleoColector.getCodPoblNucleo());
				rs = ps.executeQuery();
				if (rs.next()) {
					actualizar = true;
					ps = conn.prepareStatement("EIELupdateNucleosColector");
				} else {
					actualizar = false;
					ps = conn.prepareStatement("EIELinsertNucleosColector");
				}
				ps.setString(1, nucleoColector.getClaveColector());
				ps.setString(2, nucleoColector.getCodProvColector());
				ps.setString(3, nucleoColector.getCodMunicColector());
				ps.setString(4, nucleoColector.getCodOrdenColector());
				ps.setString(5, nucleoColector.getCodProvNucleo());
				ps.setString(6, nucleoColector.getCodMunicNucleo());
				ps.setString(7, nucleoColector.getCodEntNucleo());
				ps.setString(8, nucleoColector.getCodPoblNucleo());
				ps.setFloat(9, nucleoColector.getPmi().floatValue());
				ps.setFloat(10, nucleoColector.getPmf().floatValue());
				ps.setString(11, nucleoColector.getObservaciones());
				ps.setDate(12, nucleoColector.getFechaRevision());
				if(nucleoColector.getEstadoRevision()!=null)
					ps.setInt(13, nucleoColector.getEstadoRevision().intValue());
				else
					ps.setNull(13, java.sql.Types.INTEGER);
				if (actualizar) {
					ps.setString(14, nucleoColector.getClaveColector());
					ps.setString(15, nucleoColector.getCodProvColector());
					ps.setString(16, nucleoColector.getCodMunicColector());
					ps.setString(17, nucleoColector.getCodOrdenColector());
					ps.setString(18, nucleoColector.getCodProvNucleo());
					ps.setString(19, nucleoColector.getCodMunicNucleo());
					ps.setString(20, nucleoColector.getCodEntNucleo());
					ps.setString(21, nucleoColector.getCodPoblNucleo());
				}
				ps.execute();

			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
	}    

	/**
	 * @author davidcaramazana
	 */
	public void savePuntoVertidoEmisario(PuntoVertidoEmisario puntoVertidoEmisario) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			boolean actualizar = false;
			if (puntoVertidoEmisario != null) {
				ps = conn.prepareStatement("EIELgetPuntoVertidoEmisario");
				ps.setString(1, puntoVertidoEmisario.getClaveEmisario());
				ps.setString(2, puntoVertidoEmisario.getCodProvEmisario());
				ps.setString(3, puntoVertidoEmisario.getCodMunicEmisario());
				ps.setString(4, puntoVertidoEmisario.getCodOrdenEmisario());
				ps.setString(5, puntoVertidoEmisario.getCodProvPuntoVertido());
				ps.setString(6, puntoVertidoEmisario.getCodMunicPuntoVertido());
				ps.setString(7, puntoVertidoEmisario.getCodOrdenPuntoVertido());
				rs = ps.executeQuery();
				if (rs.next()) {
					actualizar = true;
					ps = conn.prepareStatement("EIELupdatePuntoVertidoEmisario");
				} else {
					actualizar = false;
					ps = conn.prepareStatement("EIELinsertPuntoVertidoEmisario");
				}
				ps.setString(1, puntoVertidoEmisario.getClaveEmisario());
				ps.setString(2, puntoVertidoEmisario.getCodProvEmisario());
				ps.setString(3, puntoVertidoEmisario.getCodMunicEmisario());
				ps.setString(4, puntoVertidoEmisario.getCodOrdenEmisario());
				ps.setString(5, puntoVertidoEmisario.getCodProvPuntoVertido());
				ps.setString(6, puntoVertidoEmisario.getCodMunicPuntoVertido());
				ps.setString(7, puntoVertidoEmisario.getCodClavePuntoVertido());
				ps.setString(8, puntoVertidoEmisario.getCodOrdenPuntoVertido());
				ps.setFloat(9, puntoVertidoEmisario.getPmi().floatValue());
				ps.setFloat(10, puntoVertidoEmisario.getPmf().floatValue());
				ps.setString(11, puntoVertidoEmisario.getObservaciones());
				ps.setDate(12, puntoVertidoEmisario.getFechaRevision());
				ps.setInt(13, puntoVertidoEmisario.getEstadoRevision().intValue());
				if (actualizar) {
					ps.setString(14, puntoVertidoEmisario.getClaveEmisario());
					ps.setString(15, puntoVertidoEmisario.getCodProvEmisario());
					ps.setString(16, puntoVertidoEmisario.getCodMunicEmisario());
					ps.setString(17, puntoVertidoEmisario.getCodOrdenEmisario());
					ps.setString(18, puntoVertidoEmisario.getCodProvPuntoVertido());
					ps.setString(19, puntoVertidoEmisario.getCodMunicPuntoVertido());
					ps.setString(20, puntoVertidoEmisario.getCodClavePuntoVertido());
					ps.setString(21, puntoVertidoEmisario.getCodOrdenPuntoVertido());
				}
				ps.execute();
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
	}

	public void deleteNucleoCaptacion(NucleoCaptacion nucleoCaptacion){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        

			if (nucleoCaptacion != null){

				ps = conn.prepareStatement("EIELgetNucleosCaptacion");
				ps.setString(1, nucleoCaptacion.getClaveCaptacion());
				ps.setString(2, nucleoCaptacion.getCodProvCaptacion());
				ps.setString(3, nucleoCaptacion.getCodMunicCaptacion());
				ps.setString(4, nucleoCaptacion.getCodOrdenCaptacion());
				ps.setString(5, nucleoCaptacion.getCodProvNucleo());
				ps.setString(6, nucleoCaptacion.getCodMunicNucleo());
				ps.setString(7, nucleoCaptacion.getCodEntNucleo());
				ps.setString(8, nucleoCaptacion.getCodPoblNucleo());
				rs = ps.executeQuery(); 

				if (rs.next()){

					ps = conn.prepareStatement("EIELdeleteNucleosCaptacion");

					ps.setString(1, nucleoCaptacion.getClaveCaptacion());
					ps.setString(2, nucleoCaptacion.getCodProvCaptacion());
					ps.setString(3, nucleoCaptacion.getCodMunicCaptacion());
					ps.setString(4, nucleoCaptacion.getCodOrdenCaptacion());
					ps.setString(5, nucleoCaptacion.getCodProvNucleo());
					ps.setString(6, nucleoCaptacion.getCodMunicNucleo());
					ps.setString(7, nucleoCaptacion.getCodEntNucleo());
					ps.setString(8, nucleoCaptacion.getCodPoblNucleo());

					ps.execute();
				}   
			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

	}

	public void saveNucleoDepositos(NucleoDepositos nucleoDepositos){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        
			boolean actualizar = false;

			if (nucleoDepositos != null){

				ps = conn.prepareStatement("EIELgetNucleosDepositos");
				ps.setString(1, nucleoDepositos.getClaveDepositos());
				ps.setString(2, nucleoDepositos.getCodProvDepositos());
				ps.setString(3, nucleoDepositos.getCodMunicDepositos());
				ps.setString(4, nucleoDepositos.getCodOrdenDepositos());
				ps.setString(5, nucleoDepositos.getCodProvNucleo());
				ps.setString(6, nucleoDepositos.getCodMunicNucleo());
				ps.setString(7, nucleoDepositos.getCodEntNucleo());
				ps.setString(8, nucleoDepositos.getCodPoblNucleo());
				rs = ps.executeQuery(); 

				if (rs.next()){
					actualizar = true;
					ps = conn.prepareStatement("EIELupdateNucleosDepositos");
				}
				else{
					actualizar = false;
					ps = conn.prepareStatement("EIELinsertNucleosDepositos");
				}

				ps.setString(1, nucleoDepositos.getClaveDepositos());
				ps.setString(2, nucleoDepositos.getCodProvDepositos());
				ps.setString(3, nucleoDepositos.getCodMunicDepositos());
				ps.setString(4, nucleoDepositos.getCodOrdenDepositos());

				ps.setString(5, nucleoDepositos.getCodProvNucleo());
				ps.setString(6, nucleoDepositos.getCodMunicNucleo());
				ps.setString(7, nucleoDepositos.getCodEntNucleo());
				ps.setString(8, nucleoDepositos.getCodPoblNucleo());

				if(nucleoDepositos.getObservaciones()!=null)
					ps.setString(9, nucleoDepositos.getObservaciones());
				else
					ps.setNull(9, java.sql.Types.VARCHAR);

				ps.setDate(10, nucleoDepositos.getFechaRevision());
				if(nucleoDepositos.getEstadoRevision()!=null)
					ps.setInt(11, nucleoDepositos.getEstadoRevision().intValue());
				else
					ps.setNull(11, java.sql.Types.INTEGER);

				if (actualizar){
					ps.setString(12, nucleoDepositos.getClaveDepositos());
					ps.setString(13, nucleoDepositos.getCodProvDepositos());
					ps.setString(14, nucleoDepositos.getCodMunicDepositos());
					ps.setString(15, nucleoDepositos.getCodOrdenDepositos());
					ps.setString(16, nucleoDepositos.getCodProvNucleo());
					ps.setString(17, nucleoDepositos.getCodMunicNucleo());
					ps.setString(18, nucleoDepositos.getCodEntNucleo());
					ps.setString(19, nucleoDepositos.getCodPoblNucleo());
				}

				ps.execute();
			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

	}

	public void deleteNucleoDepositos(NucleoDepositos nucleoDepositos){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        

			if (nucleoDepositos != null){

				ps = conn.prepareStatement("EIELgetNucleosDepositos");
				ps.setString(1, nucleoDepositos.getClaveDepositos());
				ps.setString(2, nucleoDepositos.getCodProvDepositos());
				ps.setString(3, nucleoDepositos.getCodMunicDepositos());
				ps.setString(4, nucleoDepositos.getCodOrdenDepositos());
				ps.setString(5, nucleoDepositos.getCodProvNucleo());
				ps.setString(6, nucleoDepositos.getCodMunicNucleo());
				ps.setString(7, nucleoDepositos.getCodEntNucleo());
				ps.setString(8, nucleoDepositos.getCodPoblNucleo());
				rs = ps.executeQuery(); 

				if (rs.next()){

					ps = conn.prepareStatement("EIELdeleteNucleosDepositos");

					ps.setString(1, nucleoDepositos.getClaveDepositos());
					ps.setString(2, nucleoDepositos.getCodProvDepositos());
					ps.setString(3, nucleoDepositos.getCodMunicDepositos());
					ps.setString(4, nucleoDepositos.getCodOrdenDepositos());
					ps.setString(5, nucleoDepositos.getCodProvNucleo());
					ps.setString(6, nucleoDepositos.getCodMunicNucleo());
					ps.setString(7, nucleoDepositos.getCodEntNucleo());
					ps.setString(8, nucleoDepositos.getCodPoblNucleo());

					ps.execute();
				}   

			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

	}

	public void saveNucleoTratamientosPotabilizacion(NucleoTratamientosPotabilizacion nucleoTratamientosPotabilizacion){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        

			boolean actualizar = false;

			if (nucleoTratamientosPotabilizacion != null){

				ps = conn.prepareStatement("EIELgetNucleosTratamientosPotabilizacion");
				ps.setString(1, nucleoTratamientosPotabilizacion.getClaveTratamientosPotabilizacion());
				ps.setString(2, nucleoTratamientosPotabilizacion.getCodProvTratamientosPotabilizacion());
				ps.setString(3, nucleoTratamientosPotabilizacion.getCodMunicTratamientosPotabilizacion());
				ps.setString(4, nucleoTratamientosPotabilizacion.getCodOrdenTratamientosPotabilizacion());
				ps.setString(5, nucleoTratamientosPotabilizacion.getCodProvNucleo());
				ps.setString(6, nucleoTratamientosPotabilizacion.getCodMunicNucleo());
				ps.setString(7, nucleoTratamientosPotabilizacion.getCodEntNucleo());
				ps.setString(8, nucleoTratamientosPotabilizacion.getCodPoblNucleo());
				rs = ps.executeQuery(); 

				if (rs.next()){
					actualizar = true;
					ps = conn.prepareStatement("EIELupdateNucleosTratamientosPotabilizacion");
				}
				else{
					actualizar = false;
					ps = conn.prepareStatement("EIELinsertNucleosTratamientosPotabilizacion");
				}

				ps.setString(1, nucleoTratamientosPotabilizacion.getClaveTratamientosPotabilizacion());
				ps.setString(2, nucleoTratamientosPotabilizacion.getCodProvTratamientosPotabilizacion());
				ps.setString(3, nucleoTratamientosPotabilizacion.getCodMunicTratamientosPotabilizacion());
				ps.setString(4, nucleoTratamientosPotabilizacion.getCodOrdenTratamientosPotabilizacion());

				ps.setString(5, nucleoTratamientosPotabilizacion.getCodProvNucleo());
				ps.setString(6, nucleoTratamientosPotabilizacion.getCodMunicNucleo());
				ps.setString(7, nucleoTratamientosPotabilizacion.getCodEntNucleo());
				ps.setString(8, nucleoTratamientosPotabilizacion.getCodPoblNucleo());

				ps.setString(9, nucleoTratamientosPotabilizacion.getObservaciones());
				ps.setDate(10, nucleoTratamientosPotabilizacion.getFechaRevision());
				if(nucleoTratamientosPotabilizacion.getEstadoRevision()!=null)
					ps.setInt(11, nucleoTratamientosPotabilizacion.getEstadoRevision().intValue());
				else
					ps.setNull(11, java.sql.Types.INTEGER);
				if (actualizar){
					ps.setString(12, nucleoTratamientosPotabilizacion.getClaveTratamientosPotabilizacion());
					ps.setString(13, nucleoTratamientosPotabilizacion.getCodProvTratamientosPotabilizacion());
					ps.setString(14, nucleoTratamientosPotabilizacion.getCodMunicTratamientosPotabilizacion());
					ps.setString(15, nucleoTratamientosPotabilizacion.getCodOrdenTratamientosPotabilizacion());
					ps.setString(16, nucleoTratamientosPotabilizacion.getCodProvNucleo());
					ps.setString(17, nucleoTratamientosPotabilizacion.getCodMunicNucleo());
					ps.setString(18, nucleoTratamientosPotabilizacion.getCodEntNucleo());
					ps.setString(19, nucleoTratamientosPotabilizacion.getCodPoblNucleo());
				}

				ps.execute();

			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

	}

	public void deleteNucleoTratamientosPotabilizacion(NucleoTratamientosPotabilizacion nucleoTratamientosPotabilizacion){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        

			if (nucleoTratamientosPotabilizacion != null){

				ps = conn.prepareStatement("EIELgetNucleosTratamientosPotabilizacion");
				ps.setString(1, nucleoTratamientosPotabilizacion.getClaveTratamientosPotabilizacion());
				ps.setString(2, nucleoTratamientosPotabilizacion.getCodProvTratamientosPotabilizacion());
				ps.setString(3, nucleoTratamientosPotabilizacion.getCodMunicTratamientosPotabilizacion());
				ps.setString(4, nucleoTratamientosPotabilizacion.getCodOrdenTratamientosPotabilizacion());
				ps.setString(5, nucleoTratamientosPotabilizacion.getCodProvNucleo());
				ps.setString(6, nucleoTratamientosPotabilizacion.getCodMunicNucleo());
				ps.setString(7, nucleoTratamientosPotabilizacion.getCodEntNucleo());
				ps.setString(8, nucleoTratamientosPotabilizacion.getCodPoblNucleo());
				rs = ps.executeQuery(); 

				if (rs.next()){

					ps = conn.prepareStatement("EIELdeleteNucleosTratamientosPotabilizacion");

					ps.setString(1, nucleoTratamientosPotabilizacion.getClaveTratamientosPotabilizacion());
					ps.setString(2, nucleoTratamientosPotabilizacion.getCodProvTratamientosPotabilizacion());
					ps.setString(3, nucleoTratamientosPotabilizacion.getCodMunicTratamientosPotabilizacion());
					ps.setString(4, nucleoTratamientosPotabilizacion.getCodOrdenTratamientosPotabilizacion());
					ps.setString(5, nucleoTratamientosPotabilizacion.getCodProvNucleo());
					ps.setString(6, nucleoTratamientosPotabilizacion.getCodMunicNucleo());
					ps.setString(7, nucleoTratamientosPotabilizacion.getCodEntNucleo());
					ps.setString(8, nucleoTratamientosPotabilizacion.getCodPoblNucleo());

					ps.execute();
				}   
			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

	}


	public void saveNucleoPuntosVertido(NucleoPuntosVertido nucleoPuntosVertido){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        
			boolean actualizar = false;

			if (nucleoPuntosVertido != null){

				ps = conn.prepareStatement("EIELgetNucleosPuntosVertido");
				ps.setString(1, nucleoPuntosVertido.getClavePuntosVertido());
				ps.setString(2, nucleoPuntosVertido.getCodProvPuntosVertido());
				ps.setString(3, nucleoPuntosVertido.getCodMunicPuntosVertido());
				ps.setString(4, nucleoPuntosVertido.getCodOrdenPuntosVertido());
				ps.setString(5, nucleoPuntosVertido.getCodProvNucleo());
				ps.setString(6, nucleoPuntosVertido.getCodMunicNucleo());
				ps.setString(7, nucleoPuntosVertido.getCodEntNucleo());
				ps.setString(8, nucleoPuntosVertido.getCodPoblNucleo());
				rs = ps.executeQuery(); 

				if (rs.next()){
					actualizar = true;
					ps = conn.prepareStatement("EIELupdateNucleosPuntosVertido");
				}
				else{
					actualizar = false;
					ps = conn.prepareStatement("EIELinsertNucleosPuntosVertido");
				}

				ps.setString(1, nucleoPuntosVertido.getClavePuntosVertido());
				ps.setString(2, nucleoPuntosVertido.getCodProvPuntosVertido());
				ps.setString(3, nucleoPuntosVertido.getCodMunicPuntosVertido());
				ps.setString(4, nucleoPuntosVertido.getCodOrdenPuntosVertido());

				ps.setString(5, nucleoPuntosVertido.getCodProvNucleo());
				ps.setString(6, nucleoPuntosVertido.getCodMunicNucleo());
				ps.setString(7, nucleoPuntosVertido.getCodEntNucleo());
				ps.setString(8, nucleoPuntosVertido.getCodPoblNucleo());

				ps.setString(9, nucleoPuntosVertido.getObservaciones());
				ps.setDate(10, nucleoPuntosVertido.getFechaRevision());
				if(nucleoPuntosVertido.getEstadoRevision()!=null)
					ps.setInt(11, nucleoPuntosVertido.getEstadoRevision().intValue());
				else
					ps.setNull(11, java.sql.Types.INTEGER);
				if (actualizar){
					ps.setString(12, nucleoPuntosVertido.getClavePuntosVertido());
					ps.setString(13, nucleoPuntosVertido.getCodProvPuntosVertido());
					ps.setString(14, nucleoPuntosVertido.getCodMunicPuntosVertido());
					ps.setString(15, nucleoPuntosVertido.getCodOrdenPuntosVertido());
					ps.setString(16, nucleoPuntosVertido.getCodProvNucleo());
					ps.setString(17, nucleoPuntosVertido.getCodMunicNucleo());
					ps.setString(18, nucleoPuntosVertido.getCodEntNucleo());
					ps.setString(19, nucleoPuntosVertido.getCodPoblNucleo());
				}

				ps.execute();

			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

	}

	public void deleteNucleoPuntosVertido(NucleoPuntosVertido nucleoPuntosVertido){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        

			if (nucleoPuntosVertido != null){

				ps = conn.prepareStatement("EIELgetNucleosPuntosVertido");
				ps.setString(1, nucleoPuntosVertido.getClavePuntosVertido());
				ps.setString(2, nucleoPuntosVertido.getCodProvPuntosVertido());
				ps.setString(3, nucleoPuntosVertido.getCodMunicPuntosVertido());
				ps.setString(4, nucleoPuntosVertido.getCodOrdenPuntosVertido());
				ps.setString(5, nucleoPuntosVertido.getCodProvNucleo());
				ps.setString(6, nucleoPuntosVertido.getCodMunicNucleo());
				ps.setString(7, nucleoPuntosVertido.getCodEntNucleo());
				ps.setString(8, nucleoPuntosVertido.getCodPoblNucleo());
				rs = ps.executeQuery(); 

				if (rs.next()){

					ps = conn.prepareStatement("EIELdeleteNucleosPuntosVertido");

					ps.setString(1, nucleoPuntosVertido.getClavePuntosVertido());
					ps.setString(2, nucleoPuntosVertido.getCodProvPuntosVertido());
					ps.setString(3, nucleoPuntosVertido.getCodMunicPuntosVertido());
					ps.setString(4, nucleoPuntosVertido.getCodOrdenPuntosVertido());
					ps.setString(5, nucleoPuntosVertido.getCodProvNucleo());
					ps.setString(6, nucleoPuntosVertido.getCodMunicNucleo());
					ps.setString(7, nucleoPuntosVertido.getCodEntNucleo());
					ps.setString(8, nucleoPuntosVertido.getCodPoblNucleo());

					ps.execute();
				}   
			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

	}

	public void saveNucleoDepuradora1(NucleoDepuradora1 nucleoDepuradora1){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        

			boolean actualizar = false;

			if (nucleoDepuradora1 != null){

				ps = conn.prepareStatement("EIELgetNucleosDepuradora1");
				ps.setString(1, nucleoDepuradora1.getClaveDepuradora1());
				ps.setString(2, nucleoDepuradora1.getCodProvDepuradora1());
				ps.setString(3, nucleoDepuradora1.getCodMunicDepuradora1());
				ps.setString(4, nucleoDepuradora1.getCodOrdenDepuradora1());
				ps.setString(5, nucleoDepuradora1.getCodProvNucleo());
				ps.setString(6, nucleoDepuradora1.getCodMunicNucleo());
				ps.setString(7, nucleoDepuradora1.getCodEntNucleo());
				ps.setString(8, nucleoDepuradora1.getCodPoblNucleo());
				rs = ps.executeQuery(); 

				if (rs.next()){
					actualizar = true;
					ps = conn.prepareStatement("EIELupdateNucleosDepuradora1");
				}
				else{
					actualizar = false;
					ps = conn.prepareStatement("EIELinsertNucleosDepuradora1");
				}

				ps.setString(1, nucleoDepuradora1.getClaveDepuradora1());
				ps.setString(2, nucleoDepuradora1.getCodProvDepuradora1());
				ps.setString(3, nucleoDepuradora1.getCodMunicDepuradora1());
				ps.setString(4, nucleoDepuradora1.getCodOrdenDepuradora1());

				ps.setString(5, nucleoDepuradora1.getCodProvNucleo());
				ps.setString(6, nucleoDepuradora1.getCodMunicNucleo());
				ps.setString(7, nucleoDepuradora1.getCodEntNucleo());
				ps.setString(8, nucleoDepuradora1.getCodPoblNucleo());

				ps.setString(9, nucleoDepuradora1.getObservaciones());
				ps.setDate(10, nucleoDepuradora1.getFechaRevision());
				if(nucleoDepuradora1.getEstadoRevision()!=null)
					ps.setInt(11, nucleoDepuradora1.getEstadoRevision().intValue());
				else
					ps.setNull(11, java.sql.Types.INTEGER);
				if (actualizar){
					ps.setString(12, nucleoDepuradora1.getClaveDepuradora1());
					ps.setString(13, nucleoDepuradora1.getCodProvDepuradora1());
					ps.setString(14, nucleoDepuradora1.getCodMunicDepuradora1());
					ps.setString(15, nucleoDepuradora1.getCodOrdenDepuradora1());
					ps.setString(16, nucleoDepuradora1.getCodProvNucleo());
					ps.setString(17, nucleoDepuradora1.getCodMunicNucleo());
					ps.setString(18, nucleoDepuradora1.getCodEntNucleo());
					ps.setString(19, nucleoDepuradora1.getCodPoblNucleo());
				}

				ps.execute();

			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

	}


	public void deleteNucleoDepuradora1(NucleoDepuradora1 nucleoDepuradora1){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        

			if (nucleoDepuradora1 != null){

				ps = conn.prepareStatement("EIELgetNucleosDepuradora1");
				ps.setString(1, nucleoDepuradora1.getClaveDepuradora1());
				ps.setString(2, nucleoDepuradora1.getCodProvDepuradora1());
				ps.setString(3, nucleoDepuradora1.getCodMunicDepuradora1());
				ps.setString(4, nucleoDepuradora1.getCodOrdenDepuradora1());
				ps.setString(5, nucleoDepuradora1.getCodProvNucleo());
				ps.setString(6, nucleoDepuradora1.getCodMunicNucleo());
				ps.setString(7, nucleoDepuradora1.getCodEntNucleo());
				ps.setString(8, nucleoDepuradora1.getCodPoblNucleo());
				rs = ps.executeQuery(); 

				if (rs.next()){

					ps = conn.prepareStatement("EIELdeleteNucleosDepuradora1");

					ps.setString(1, nucleoDepuradora1.getClaveDepuradora1());
					ps.setString(2, nucleoDepuradora1.getCodProvDepuradora1());
					ps.setString(3, nucleoDepuradora1.getCodMunicDepuradora1());
					ps.setString(4, nucleoDepuradora1.getCodOrdenDepuradora1());
					ps.setString(5, nucleoDepuradora1.getCodProvNucleo());
					ps.setString(6, nucleoDepuradora1.getCodMunicNucleo());
					ps.setString(7, nucleoDepuradora1.getCodEntNucleo());
					ps.setString(8, nucleoDepuradora1.getCodPoblNucleo());

					ps.execute();
				}   
			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

	}

	public void saveNucleoVertedero(NucleoVertedero nucleoVertedero){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        

			boolean actualizar = false;

			if (nucleoVertedero != null){

				ps = conn.prepareStatement("EIELgetNucleosVertedero");
				ps.setString(1, nucleoVertedero.getClaveVertedero());
				ps.setString(2, nucleoVertedero.getCodProvVertedero());
				ps.setString(3, nucleoVertedero.getCodMunicVertedero());
				ps.setString(4, nucleoVertedero.getCodOrdenVertedero());
				ps.setString(5, nucleoVertedero.getCodProvNucleo());
				ps.setString(6, nucleoVertedero.getCodMunicNucleo());
				ps.setString(7, nucleoVertedero.getCodEntNucleo());
				ps.setString(8, nucleoVertedero.getCodPoblNucleo());
				rs = ps.executeQuery(); 

				if (rs.next()){
					actualizar = true;
					ps = conn.prepareStatement("EIELupdateNucleosVertedero");
				}
				else{
					actualizar = false;
					ps = conn.prepareStatement("EIELinsertNucleosVertedero");
				}

				ps.setString(1, nucleoVertedero.getClaveVertedero());
				ps.setString(2, nucleoVertedero.getCodProvVertedero());
				ps.setString(3, nucleoVertedero.getCodMunicVertedero());
				ps.setString(4, nucleoVertedero.getCodOrdenVertedero());

				ps.setString(5, nucleoVertedero.getCodProvNucleo());
				ps.setString(6, nucleoVertedero.getCodMunicNucleo());
				ps.setString(7, nucleoVertedero.getCodEntNucleo());
				ps.setString(8, nucleoVertedero.getCodPoblNucleo());

				ps.setString(9, nucleoVertedero.getObservaciones());
				ps.setDate(10, nucleoVertedero.getFechaAlta());

				ps.setDate(11, nucleoVertedero.getFechaRevision());
				if(nucleoVertedero.getEstadoRevision()!=null)
					ps.setInt(12, nucleoVertedero.getEstadoRevision().intValue());
				else
					ps.setNull(12, java.sql.Types.INTEGER);
				if (actualizar){
					ps.setString(13, nucleoVertedero.getClaveVertedero());
					ps.setString(14, nucleoVertedero.getCodProvVertedero());
					ps.setString(15, nucleoVertedero.getCodMunicVertedero());
					ps.setString(16, nucleoVertedero.getCodOrdenVertedero());
					ps.setString(17, nucleoVertedero.getCodProvNucleo());
					ps.setString(18, nucleoVertedero.getCodMunicNucleo());
					ps.setString(19, nucleoVertedero.getCodEntNucleo());
					ps.setString(20, nucleoVertedero.getCodPoblNucleo());
				}

				ps.execute();

			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

	}

	public void deleteNucleoVertedero(NucleoVertedero nucleoVertedero){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        

			if (nucleoVertedero != null){

				ps = conn.prepareStatement("EIELgetNucleosVertedero");
				ps.setString(1, nucleoVertedero.getClaveVertedero());
				ps.setString(2, nucleoVertedero.getCodProvVertedero());
				ps.setString(3, nucleoVertedero.getCodMunicVertedero());
				ps.setString(4, nucleoVertedero.getCodOrdenVertedero());
				ps.setString(5, nucleoVertedero.getCodProvNucleo());
				ps.setString(6, nucleoVertedero.getCodMunicNucleo());
				ps.setString(7, nucleoVertedero.getCodEntNucleo());
				ps.setString(8, nucleoVertedero.getCodPoblNucleo());
				rs = ps.executeQuery(); 

				if (rs.next()){

					ps = conn.prepareStatement("EIELdeleteNucleosVertedero");

					ps.setString(1, nucleoVertedero.getClaveVertedero());
					ps.setString(2, nucleoVertedero.getCodProvVertedero());
					ps.setString(3, nucleoVertedero.getCodMunicVertedero());
					ps.setString(4, nucleoVertedero.getCodOrdenVertedero());
					ps.setString(5, nucleoVertedero.getCodProvNucleo());
					ps.setString(6, nucleoVertedero.getCodMunicNucleo());
					ps.setString(7, nucleoVertedero.getCodEntNucleo());
					ps.setString(8, nucleoVertedero.getCodPoblNucleo());

					ps.execute();
				}   

			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

	}

	public void saveNucleoTramosConduccion(NucleoTramosConduccion nucleoTramosConduccion){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        

			
			boolean actualizar = false;

			if (nucleoTramosConduccion != null){

				ps = conn.prepareStatement("EIELgetNucleosTramosConduccion");
				ps.setString(1, nucleoTramosConduccion.getClaveTramosConduccion());
				ps.setString(2, nucleoTramosConduccion.getCodProvTramosConduccion());
				ps.setString(3, nucleoTramosConduccion.getCodMunicTramosConduccion());
				ps.setString(4, nucleoTramosConduccion.getCodOrdenTramosConduccion());
				ps.setString(5, nucleoTramosConduccion.getCodProvNucleo());
				ps.setString(6, nucleoTramosConduccion.getCodMunicNucleo());
				ps.setString(7, nucleoTramosConduccion.getCodEntNucleo());
				ps.setString(8, nucleoTramosConduccion.getCodPoblNucleo());
				rs = ps.executeQuery(); 

				if (rs.next()){
					actualizar = true;
					ps = conn.prepareStatement("EIELupdateNucleosTramosConduccion");
				}
				else{
					actualizar = false;
					ps = conn.prepareStatement("EIELinsertNucleosTramosConduccion");
				}
				ps.setString(1, nucleoTramosConduccion.getClaveTramosConduccion());
				ps.setString(2, nucleoTramosConduccion.getCodProvTramosConduccion());
				ps.setString(3, nucleoTramosConduccion.getCodMunicTramosConduccion());
				ps.setString(4, nucleoTramosConduccion.getCodOrdenTramosConduccion());

				ps.setString(5, nucleoTramosConduccion.getCodProvNucleo());
				ps.setString(6, nucleoTramosConduccion.getCodMunicNucleo());
				ps.setString(7, nucleoTramosConduccion.getCodEntNucleo());
				ps.setString(8, nucleoTramosConduccion.getCodPoblNucleo());

				if(nucleoTramosConduccion.getObservaciones()!=null && !nucleoTramosConduccion.getObservaciones().equals(""))
					ps.setString(9, nucleoTramosConduccion.getObservaciones());
				else
					ps.setNull(9, java.sql.Types.VARCHAR);

				ps.setFloat(10, nucleoTramosConduccion.getPmi());
				ps.setFloat(11, nucleoTramosConduccion.getPmf());

				ps.setDate(12, nucleoTramosConduccion.getFechaRevision());
				if(nucleoTramosConduccion.getEstadoRevision()!=null)
					ps.setInt(13, nucleoTramosConduccion.getEstadoRevision().intValue());
				else
					ps.setNull(13, java.sql.Types.INTEGER);
				if (actualizar){
					ps.setString(14, nucleoTramosConduccion.getCodProvTramosConduccion());
					ps.setString(15, nucleoTramosConduccion.getCodMunicTramosConduccion());
					ps.setString(16, nucleoTramosConduccion.getCodOrdenTramosConduccion());
					ps.setString(17, nucleoTramosConduccion.getCodProvNucleo());
					ps.setString(18, nucleoTramosConduccion.getCodMunicNucleo());
					ps.setString(19, nucleoTramosConduccion.getCodEntNucleo());
					ps.setString(20, nucleoTramosConduccion.getCodPoblNucleo());
				}

				ps.execute();

			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

	}

	public void deleteNucleoTramosConduccion(NucleoTramosConduccion nucleoTramosConduccion){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        

			if (nucleoTramosConduccion != null){

				ps = conn.prepareStatement("EIELgetNucleosTramosConduccion");
				ps.setString(1, nucleoTramosConduccion.getClaveTramosConduccion());
				ps.setString(2, nucleoTramosConduccion.getCodProvTramosConduccion());
				ps.setString(3, nucleoTramosConduccion.getCodMunicTramosConduccion());
				ps.setString(4, nucleoTramosConduccion.getCodOrdenTramosConduccion());
				ps.setString(5, nucleoTramosConduccion.getCodProvNucleo());
				ps.setString(6, nucleoTramosConduccion.getCodMunicNucleo());
				ps.setString(7, nucleoTramosConduccion.getCodEntNucleo());
				ps.setString(8, nucleoTramosConduccion.getCodPoblNucleo());
				rs = ps.executeQuery(); 

				if (rs.next()){

					ps = conn.prepareStatement("EIELdeleteNucleosTramosConduccion");
					ps.setString(1, nucleoTramosConduccion.getClaveTramosConduccion());
					ps.setString(2, nucleoTramosConduccion.getCodProvTramosConduccion());
					ps.setString(3, nucleoTramosConduccion.getCodMunicTramosConduccion());
					ps.setString(4, nucleoTramosConduccion.getCodOrdenTramosConduccion());
					ps.setString(5, nucleoTramosConduccion.getCodProvNucleo());
					ps.setString(6, nucleoTramosConduccion.getCodMunicNucleo());
					ps.setString(7, nucleoTramosConduccion.getCodEntNucleo());
					ps.setString(8, nucleoTramosConduccion.getCodPoblNucleo());

					ps.execute();
				}   

			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

	}


	/**
	 * @author davidcaramazana
	 */
	public void deleteNucleoColector(NucleoColector nucleoColector){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{        
			if (nucleoColector != null){
				ps = conn.prepareStatement("EIELgetNucleosColector");
				ps.setString(1, nucleoColector.getClaveColector());
				ps.setString(2, nucleoColector.getCodProvColector());
				ps.setString(3, nucleoColector.getCodMunicColector());
				ps.setString(4, nucleoColector.getCodOrdenColector());
				ps.setString(5, nucleoColector.getCodProvNucleo());
				ps.setString(6, nucleoColector.getCodMunicNucleo());
				ps.setString(7, nucleoColector.getCodEntNucleo());
				ps.setString(8, nucleoColector.getCodPoblNucleo());
				rs = ps.executeQuery();   			
				if (rs.next()){
					ps = conn.prepareStatement("EIELdeleteNucleosColector");
					ps.setString(1, nucleoColector.getClaveColector());
					ps.setString(2, nucleoColector.getCodProvColector());
					ps.setString(3, nucleoColector.getCodMunicColector());
					ps.setString(4, nucleoColector.getCodOrdenColector());
					ps.setString(5, nucleoColector.getCodProvNucleo());
					ps.setString(6, nucleoColector.getCodMunicNucleo());
					ps.setString(7, nucleoColector.getCodEntNucleo());
					ps.setString(8, nucleoColector.getCodPoblNucleo());
					ps.execute();
				}   
			}
		} catch (SQLException ex) {           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
	}

	/**
	 * @author davidcaramazana
	 */
	public void deletePuntoVertidoEmisario(PuntoVertidoEmisario puntoVertidoEmisario){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{        
			if (puntoVertidoEmisario != null){
				ps = conn.prepareStatement("EIELgetPuntoVertidoEmisario");
				ps.setString(1, puntoVertidoEmisario.getClaveEmisario());
				ps.setString(2, puntoVertidoEmisario.getCodProvEmisario());
				ps.setString(3, puntoVertidoEmisario.getCodMunicEmisario());
				ps.setString(4, puntoVertidoEmisario.getCodOrdenEmisario());
				ps.setString(5, puntoVertidoEmisario.getCodProvPuntoVertido());
				ps.setString(6, puntoVertidoEmisario.getCodMunicPuntoVertido());
				ps.setString(7, puntoVertidoEmisario.getCodOrdenPuntoVertido());
				rs = ps.executeQuery();   			
				if (rs.next()){
					ps = conn.prepareStatement("EIELdeletePuntoVertidoEmisario");
					ps.setString(1, puntoVertidoEmisario.getClaveEmisario());
					ps.setString(2, puntoVertidoEmisario.getCodProvEmisario());
					ps.setString(3, puntoVertidoEmisario.getCodMunicEmisario());
					ps.setString(4, puntoVertidoEmisario.getCodOrdenEmisario());
					ps.setString(5, puntoVertidoEmisario.getCodProvPuntoVertido());
					ps.setString(6, puntoVertidoEmisario.getCodMunicPuntoVertido());
					ps.setString(7, puntoVertidoEmisario.getCodClavePuntoVertido());
					ps.setString(8, puntoVertidoEmisario.getCodOrdenPuntoVertido());
					ps.execute();
				}   
			}
		} catch (SQLException ex) {           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
	}

	public ArrayList<NucleoCaptacion> getLstNucleosCaptacion(String clave, String codigoProvincia, String codigoMunicipio, String codigoOrden){

		ArrayList<NucleoCaptacion> lstNucleos = new ArrayList<NucleoCaptacion>();

		PreparedStatement ps = null;
		ResultSet rs = null;

		if (clave == null){
			clave = "";
		}
		if (codigoProvincia == null){
			codigoProvincia = "";
		}
		if (codigoMunicipio == null){
			codigoMunicipio = "";
		}
		if (codigoOrden == null){
			codigoOrden = "";
		}

		try {
			ps = conn.prepareStatement("EIELgetListaNucleosCaptacion");

			ps.setString(1, clave);
			ps.setString(2, codigoProvincia);
			ps.setString(3, codigoMunicipio);
			ps.setString(4, codigoOrden);
			rs = ps.executeQuery(); 
	
			while (rs.next()){
				NucleoCaptacion nucleoCaptacion = new NucleoCaptacion();
				nucleoCaptacion.setClaveCaptacion(rs.getString("clave_ca"));
				nucleoCaptacion.setCodProvCaptacion(rs.getString("codprov_ca"));
				nucleoCaptacion.setCodMunicCaptacion(rs.getString("codmunic_ca"));
				nucleoCaptacion.setCodOrdenCaptacion(rs.getString("orden_ca"));
				nucleoCaptacion.setCodProvNucleo(rs.getString("codprov_pobl"));
				nucleoCaptacion.setCodMunicNucleo(rs.getString("codmunic_pobl"));
				nucleoCaptacion.setCodEntNucleo(rs.getString("codentidad_pobl"));
				nucleoCaptacion.setCodPoblNucleo(rs.getString("codpoblamiento_pobl"));
				nucleoCaptacion.setObservaciones(rs.getString("observ"));
				nucleoCaptacion.setFechaInicio(rs.getDate("fecha_inicio_serv"));
				nucleoCaptacion.setFechaRevision(rs.getDate("fecha_revision"));
				nucleoCaptacion.setEstadoRevision(new Integer(rs.getInt("estado_revision")));
	
				lstNucleos.add(nucleoCaptacion); 
			}
	
			if (ps!=null) ps.close();
			if (rs!= null) rs.close(); 
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return lstNucleos;
	}

	public ArrayList getLstNucleosCaptacion(int idCaptacion){

		ArrayList lstNucleos = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        			
			CaptacionesEIEL captacion = null;

			captacion = getCaptacionEIEL(idCaptacion);

			if (captacion != null){

				ps = conn.prepareStatement("EIELgetListaNucleosCaptacion");
				ps.setString(1, captacion.getClave());
				ps.setString(2, captacion.getCodINEProvincia());
				ps.setString(3, captacion.getCodINEMunicipio());
				ps.setString(4, captacion.getCodOrden());
				rs = ps.executeQuery(); 

				while (rs.next())
				{
					NucleoCaptacion nucleoCaptacion = new NucleoCaptacion();
					nucleoCaptacion.setClaveCaptacion(rs.getString("clave_ca"));
					nucleoCaptacion.setCodProvCaptacion(rs.getString("codprov_ca"));
					nucleoCaptacion.setCodMunicCaptacion(rs.getString("codmunic_ca"));
					nucleoCaptacion.setCodOrdenCaptacion(rs.getString("orden_ca"));
					nucleoCaptacion.setCodProvNucleo(rs.getString("codprov_pobl"));
					nucleoCaptacion.setCodMunicNucleo(rs.getString("codmunic_pobl"));
					nucleoCaptacion.setCodEntNucleo(rs.getString("codentidad_pobl"));
					nucleoCaptacion.setCodPoblNucleo(rs.getString("codpoblamiento_pobl"));
					nucleoCaptacion.setObservaciones(rs.getString("observ"));
					nucleoCaptacion.setFechaInicio(rs.getDate("fecha_inicio_serv"));
					nucleoCaptacion.setFechaRevision(rs.getDate("fecha_revision"));
					nucleoCaptacion.setEstadoRevision(new Integer(rs.getInt("estado_revision")));

					lstNucleos.add(nucleoCaptacion); 
				}  
			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

		return lstNucleos;
	}

	public ArrayList<NucleoDepositos> getLstNucleosDepositos(String clave, String codigoProvincia, String codigoMunicipio, String codigoOrden){

		ArrayList<NucleoDepositos> lstNucleos = new ArrayList<NucleoDepositos>();

		PreparedStatement ps = null;
		ResultSet rs = null;

		if (clave == null){
			clave = "";
		}
		if (codigoProvincia == null){
			codigoProvincia = "";
		}
		if (codigoMunicipio == null){
			codigoMunicipio = "";
		}
		if (codigoOrden == null){
			codigoOrden = "";
		}

		try {
			ps = conn.prepareStatement("EIELgetListaNucleosDepositos");
		
			ps.setString(1, clave);
			ps.setString(2, codigoProvincia);
			ps.setString(3, codigoMunicipio);
			ps.setString(4, codigoOrden);
			rs = ps.executeQuery(); 
	
			while (rs.next()){
				NucleoDepositos nucleoDepositos = new NucleoDepositos();
				nucleoDepositos.setClaveDepositos(rs.getString("clave_de"));
				nucleoDepositos.setCodProvDepositos(rs.getString("codprov_de"));
				nucleoDepositos.setCodMunicDepositos(rs.getString("codmunic_de"));
				nucleoDepositos.setCodOrdenDepositos(rs.getString("orden_de"));
				nucleoDepositos.setCodProvNucleo(rs.getString("codprov_pobl"));
				nucleoDepositos.setCodMunicNucleo(rs.getString("codmunic_pobl"));
				nucleoDepositos.setCodEntNucleo(rs.getString("codentidad_pobl"));
				nucleoDepositos.setCodPoblNucleo(rs.getString("codpoblamiento_pobl"));
				nucleoDepositos.setObservaciones(rs.getString("observ"));
				nucleoDepositos.setFechaRevision(rs.getDate("fecha_revision"));
				nucleoDepositos.setEstadoRevision(new Integer(rs.getInt("estado_revision")));
	
				lstNucleos.add(nucleoDepositos); 
			}
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return lstNucleos;
	}

	public ArrayList getLstNucleosDepositos(int idDepositos){

		ArrayList lstNucleos = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        
			DepositosEIEL depositos = null;

			depositos = getDepositosEIEL(idDepositos);

			if (depositos != null){

				ps = conn.prepareStatement("EIELgetListaNucleosDepositos");
				ps.setString(1, depositos.getClave());
				ps.setString(2, depositos.getCodINEProvincia());
				ps.setString(3, depositos.getCodINEMunicipio());
				ps.setString(4, depositos.getOrdenDeposito());
				rs = ps.executeQuery(); 

				while (rs.next())
				{
					NucleoDepositos nucleoDepositos = new NucleoDepositos();
					nucleoDepositos.setClaveDepositos(rs.getString("clave_de"));
					nucleoDepositos.setCodProvDepositos(rs.getString("codprov_de"));
					nucleoDepositos.setCodMunicDepositos(rs.getString("codmunic_de"));
					nucleoDepositos.setCodOrdenDepositos(rs.getString("orden_de"));
					nucleoDepositos.setCodProvNucleo(rs.getString("codprov_pobl"));
					nucleoDepositos.setCodMunicNucleo(rs.getString("codmunic_pobl"));
					nucleoDepositos.setCodEntNucleo(rs.getString("codentidad_pobl"));
					nucleoDepositos.setCodPoblNucleo(rs.getString("codpoblamiento_pobl"));
					nucleoDepositos.setObservaciones(rs.getString("observ"));
					nucleoDepositos.setFechaRevision(rs.getDate("fecha_revision"));
					nucleoDepositos.setEstadoRevision(new Integer(rs.getInt("estado_revision")));

					lstNucleos.add(nucleoDepositos); 
				}  
			}

		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

		return lstNucleos;
	}

	public ArrayList<NucleoTratamientosPotabilizacion> getLstNucleosTratamientosPotabilizacion(String clave, String codigoProvincia, String codigoMunicipio, String codigoOrden){

		ArrayList<NucleoTratamientosPotabilizacion> lstNucleos = new ArrayList<NucleoTratamientosPotabilizacion>();

		PreparedStatement ps = null;
		ResultSet rs = null;

		if (clave == null){
			clave = "";
		}
		if (codigoProvincia == null){
			codigoProvincia = "";
		}
		if (codigoMunicipio == null){
			codigoMunicipio = "";
		}
		if (codigoOrden == null){
			codigoOrden = "";
		}


		try {
			ps = conn.prepareStatement("EIELgetListaNucleosTratamientosPotabilizacion");

			ps.setString(1, clave);
			ps.setString(2, codigoProvincia);
			ps.setString(3, codigoMunicipio);
			ps.setString(4, codigoOrden);
			rs = ps.executeQuery(); 
	
			while (rs.next())
			{
				NucleoTratamientosPotabilizacion nucleoTratamientosPotabilizacion = new NucleoTratamientosPotabilizacion();
				nucleoTratamientosPotabilizacion.setClaveTratamientosPotabilizacion(rs.getString("clave_tp"));
				nucleoTratamientosPotabilizacion.setCodProvTratamientosPotabilizacion(rs.getString("codprov_tp"));
				nucleoTratamientosPotabilizacion.setCodMunicTratamientosPotabilizacion(rs.getString("codmunic_tp"));
				nucleoTratamientosPotabilizacion.setCodOrdenTratamientosPotabilizacion(rs.getString("orden_tp"));
				nucleoTratamientosPotabilizacion.setCodProvNucleo(rs.getString("codprov_pobl"));
				nucleoTratamientosPotabilizacion.setCodMunicNucleo(rs.getString("codmunic_pobl"));
				nucleoTratamientosPotabilizacion.setCodEntNucleo(rs.getString("codentidad_pobl"));
				nucleoTratamientosPotabilizacion.setCodPoblNucleo(rs.getString("codpoblamiento_pobl"));
				nucleoTratamientosPotabilizacion.setObservaciones(rs.getString("observ"));
				nucleoTratamientosPotabilizacion.setFechaRevision(rs.getDate("fecha_revision"));
				nucleoTratamientosPotabilizacion.setEstadoRevision(new Integer(rs.getInt("estado_revision")));
	
				lstNucleos.add(nucleoTratamientosPotabilizacion); 
			}  

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return lstNucleos;
	}

	public ArrayList getLstNucleosTratamientosPotabilizacion(int idTratamientos){

		ArrayList lstNucleos = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        
			TratamientosPotabilizacionEIEL tratamientos = null;

			tratamientos = getTratamientosPotabilizacionEIEL(idTratamientos);

			if (tratamientos != null){

				ps = conn.prepareStatement("EIELgetListaNucleosTratamientosPotabilizacion");
				ps.setString(1, tratamientos.getClave());
				ps.setString(2, tratamientos.getCodINEProvincia());
				ps.setString(3, tratamientos.getCodINEMunicipio());
				ps.setString(4, tratamientos.getOrdenPotabilizadora());
				rs = ps.executeQuery(); 

				while (rs.next())
				{
					NucleoTratamientosPotabilizacion nucleoTratamientosPotabilizacion = new NucleoTratamientosPotabilizacion();
					nucleoTratamientosPotabilizacion.setClaveTratamientosPotabilizacion(rs.getString("clave_tp"));
					nucleoTratamientosPotabilizacion.setCodProvTratamientosPotabilizacion(rs.getString("codprov_tp"));
					nucleoTratamientosPotabilizacion.setCodMunicTratamientosPotabilizacion(rs.getString("codmunic_tp"));
					nucleoTratamientosPotabilizacion.setCodOrdenTratamientosPotabilizacion(rs.getString("orden_tp"));
					nucleoTratamientosPotabilizacion.setCodProvNucleo(rs.getString("codprov_pobl"));
					nucleoTratamientosPotabilizacion.setCodMunicNucleo(rs.getString("codmunic_pobl"));
					nucleoTratamientosPotabilizacion.setCodEntNucleo(rs.getString("codentidad_pobl"));
					nucleoTratamientosPotabilizacion.setCodPoblNucleo(rs.getString("codpoblamiento_pobl"));
					nucleoTratamientosPotabilizacion.setObservaciones(rs.getString("observ"));
					nucleoTratamientosPotabilizacion.setFechaRevision(rs.getDate("fecha_revision"));
					nucleoTratamientosPotabilizacion.setEstadoRevision(new Integer(rs.getInt("estado_revision")));

					lstNucleos.add(nucleoTratamientosPotabilizacion); 
				}  
			}

		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

		return lstNucleos;
	}

	public ArrayList<NucleoPuntosVertido> getLstNucleosPuntosVertido(String clave, String codigoProvincia, String codigoMunicipio, String codigoOrden){

		ArrayList<NucleoPuntosVertido> lstNucleos = new ArrayList<NucleoPuntosVertido>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		
		if (clave == null){
			clave = "";
		}
		if (codigoProvincia == null){
			codigoProvincia = "";
		}
		if (codigoMunicipio == null){
			codigoMunicipio = "";
		}
		if (codigoOrden == null){
			codigoOrden = "";
		}

		try {
			ps = conn.prepareStatement("EIELgetListaNucleosPuntosVertido");
		
			ps.setString(1, clave);
			ps.setString(2, codigoProvincia);
			ps.setString(3, codigoMunicipio);
			ps.setString(4, codigoOrden);
			rs = ps.executeQuery(); 
	
			while (rs.next())
			{
				NucleoPuntosVertido nucleoPuntosVertido = new NucleoPuntosVertido();
				nucleoPuntosVertido.setClavePuntosVertido(rs.getString("clave_pv"));
				nucleoPuntosVertido.setCodProvPuntosVertido(rs.getString("codprov_pv"));
				nucleoPuntosVertido.setCodMunicPuntosVertido(rs.getString("codmunic_pv"));
				nucleoPuntosVertido.setCodOrdenPuntosVertido(rs.getString("orden_pv"));
				nucleoPuntosVertido.setCodProvNucleo(rs.getString("codprov_pobl"));
				nucleoPuntosVertido.setCodMunicNucleo(rs.getString("codmunic_pobl"));
				nucleoPuntosVertido.setCodEntNucleo(rs.getString("codentidad_pobl"));
				nucleoPuntosVertido.setCodPoblNucleo(rs.getString("codpoblamiento_pobl"));
				nucleoPuntosVertido.setObservaciones(rs.getString("observ"));
				nucleoPuntosVertido.setFechaRevision(rs.getDate("fecha_revision"));
				nucleoPuntosVertido.setEstadoRevision(new Integer(rs.getInt("estado_revision")));
	
				lstNucleos.add(nucleoPuntosVertido); 
			}  

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return lstNucleos;
	}

	public ArrayList getLstNucleosPuntosVertido(int idVertido){

		ArrayList lstNucleos = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        
			PuntosVertidoEIEL vertido = null;

			vertido = getPuntosVertidoEIEL(idVertido);

			if (vertido != null){

				ps = conn.prepareStatement("EIELgetListaNucleosPuntosVertido");
				ps.setString(1, vertido.getClave());
				ps.setString(2, vertido.getCodINEProvincia());
				ps.setString(3, vertido.getCodINEMunicipio());
				ps.setString(4, vertido.getOrden());
				rs = ps.executeQuery(); 

				while (rs.next())
				{
					NucleoPuntosVertido nucleoPuntosVertido = new NucleoPuntosVertido();
					nucleoPuntosVertido.setClavePuntosVertido(rs.getString("clave_pv"));
					nucleoPuntosVertido.setCodProvPuntosVertido(rs.getString("codprov_pv"));
					nucleoPuntosVertido.setCodMunicPuntosVertido(rs.getString("codmunic_pv"));
					nucleoPuntosVertido.setCodOrdenPuntosVertido(rs.getString("orden_pv"));
					nucleoPuntosVertido.setCodProvNucleo(rs.getString("codprov_pobl"));
					nucleoPuntosVertido.setCodMunicNucleo(rs.getString("codmunic_pobl"));
					nucleoPuntosVertido.setCodEntNucleo(rs.getString("codentidad_pobl"));
					nucleoPuntosVertido.setCodPoblNucleo(rs.getString("codpoblamiento_pobl"));
					nucleoPuntosVertido.setObservaciones(rs.getString("observ"));
					nucleoPuntosVertido.setFechaRevision(rs.getDate("fecha_revision"));
					nucleoPuntosVertido.setEstadoRevision(new Integer(rs.getInt("estado_revision")));

					lstNucleos.add(nucleoPuntosVertido); 
				}  
			}

		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

		return lstNucleos;
	}
	
	public ArrayList<NucleoDepuradora1> getLstNucleosDepuradora1(String clave, String codigoProvincia, String codigoMunicipio, String codigoOrden){

		ArrayList<NucleoDepuradora1> lstNucleos = new ArrayList<NucleoDepuradora1>();

		PreparedStatement ps = null;
		ResultSet rs = null;

		if (clave == null){
			clave = "";
		}
		if (codigoProvincia == null){
			codigoProvincia = "";
		}
		if (codigoMunicipio == null){
			codigoMunicipio = "";
		}
		if (codigoOrden == null){
			codigoOrden = "";
		}



		try {
			ps = conn.prepareStatement("EIELgetListaNucleosDepuradora1");

			ps.setString(1, clave);
			ps.setString(2, codigoProvincia);
			ps.setString(3, codigoMunicipio);
			ps.setString(4, codigoOrden);
			rs = ps.executeQuery(); 
	
			while (rs.next())
			{
				NucleoDepuradora1 nucleoDepuradora1 = new NucleoDepuradora1();
				nucleoDepuradora1.setClaveDepuradora1(rs.getString("clave_ed"));
				nucleoDepuradora1.setCodProvDepuradora1(rs.getString("codprov_ed"));
				nucleoDepuradora1.setCodMunicDepuradora1(rs.getString("codmunic_ed"));
				nucleoDepuradora1.setCodOrdenDepuradora1(rs.getString("orden_ed"));
				nucleoDepuradora1.setCodProvNucleo(rs.getString("codprov_pobl"));
				nucleoDepuradora1.setCodMunicNucleo(rs.getString("codmunic_pobl"));
				nucleoDepuradora1.setCodEntNucleo(rs.getString("codentidad_pobl"));
				nucleoDepuradora1.setCodPoblNucleo(rs.getString("codpoblamiento_pobl"));
				nucleoDepuradora1.setObservaciones(rs.getString("observ"));
				nucleoDepuradora1.setFechaRevision(rs.getDate("fecha_revision"));
				nucleoDepuradora1.setEstadoRevision(new Integer(rs.getInt("estado_revision")));
	
				lstNucleos.add(nucleoDepuradora1); 
			}  

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return lstNucleos;
	}

	public ArrayList getLstNucleosDepuradora1(int idDepuradora1){

		ArrayList lstNucleos = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        	
			Depuradora1EIEL depuradora1 = null;

			depuradora1 = getDepuradora1EIEL(idDepuradora1);

			if (depuradora1 != null){

				ps = conn.prepareStatement("EIELgetListaNucleosDepuradora1");
				ps.setString(1, depuradora1.getClave());
				ps.setString(2, depuradora1.getCodINEProvincia());
				ps.setString(3, depuradora1.getCodINEMunicipio());
				ps.setString(4, depuradora1.getCodOrden());
				rs = ps.executeQuery(); 

				while (rs.next())
				{
					NucleoDepuradora1 nucleoDepuradora1 = new NucleoDepuradora1();
					nucleoDepuradora1.setClaveDepuradora1(rs.getString("clave_ed"));
					nucleoDepuradora1.setCodProvDepuradora1(rs.getString("codprov_ed"));
					nucleoDepuradora1.setCodMunicDepuradora1(rs.getString("codmunic_ed"));
					nucleoDepuradora1.setCodOrdenDepuradora1(rs.getString("orden_ed"));
					nucleoDepuradora1.setCodProvNucleo(rs.getString("codprov_pobl"));
					nucleoDepuradora1.setCodMunicNucleo(rs.getString("codmunic_pobl"));
					nucleoDepuradora1.setCodEntNucleo(rs.getString("codentidad_pobl"));
					nucleoDepuradora1.setCodPoblNucleo(rs.getString("codpoblamiento_pobl"));
					nucleoDepuradora1.setObservaciones(rs.getString("observ"));
					nucleoDepuradora1.setFechaRevision(rs.getDate("fecha_revision"));
					nucleoDepuradora1.setEstadoRevision(new Integer(rs.getInt("estado_revision")));

					lstNucleos.add(nucleoDepuradora1); 
				}  
			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

		return lstNucleos;
	}

	public ArrayList<NucleoVertedero> getLstNucleosVertedero(String clave, String codigoProvincia, String codigoMunicipio, String codigoOrden){

		ArrayList<NucleoVertedero> lstNucleos = new ArrayList<NucleoVertedero>();

		PreparedStatement ps = null;
		ResultSet rs = null;

		if (clave == null){
			clave = "";
		}
		if (codigoProvincia == null){
			codigoProvincia = "";
		}
		if (codigoMunicipio == null){
			codigoMunicipio = "";
		}
		if (codigoOrden == null){
			codigoOrden = "";
		}

		try {
			ps = conn.prepareStatement("EIELgetListaNucleosVertedero");

			ps.setString(1, clave);
			ps.setString(2, codigoProvincia);
			ps.setString(3, codigoMunicipio);
			ps.setString(4, codigoOrden);
			rs = ps.executeQuery(); 
	
			while (rs.next())
			{
				NucleoVertedero nucleoVertedero = new NucleoVertedero();
				nucleoVertedero.setClaveVertedero(rs.getString("clave_vt"));
				nucleoVertedero.setCodProvVertedero(rs.getString("codprov_vt"));
				nucleoVertedero.setCodMunicVertedero(rs.getString("codmunic_vt"));
				nucleoVertedero.setCodOrdenVertedero(rs.getString("orden_vt"));
				nucleoVertedero.setCodProvNucleo(rs.getString("codprov"));
				nucleoVertedero.setCodMunicNucleo(rs.getString("codmunic"));
				nucleoVertedero.setCodEntNucleo(rs.getString("codentidad"));
				nucleoVertedero.setCodPoblNucleo(rs.getString("codpoblamiento"));
				nucleoVertedero.setObservaciones(rs.getString("observ"));
				nucleoVertedero.setFechaAlta(rs.getDate("fecha_alta"));
				nucleoVertedero.setFechaRevision(rs.getDate("fecha_revision"));
				nucleoVertedero.setEstadoRevision(new Integer(rs.getInt("estado_revision")));
	
				lstNucleos.add(nucleoVertedero); 
			}  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return lstNucleos;
	}

	public ArrayList getLstNucleosVertedero(int idVertedero){

		ArrayList lstNucleos = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        
			
			VertederosEIEL vertedero = null;

			vertedero = getVertederoEIEL(idVertedero);

			if (vertedero != null){

				ps = conn.prepareStatement("EIELgetListaNucleosVertedero");
				ps.setString(1, vertedero.getClave());
				ps.setString(2, vertedero.getCodINEProvincia());
				ps.setString(3, vertedero.getCodINEMunicipio());
				ps.setString(4, vertedero.getCodOrden());
				rs = ps.executeQuery(); 

				while (rs.next())
				{
					NucleoVertedero nucleoVertedero = new NucleoVertedero();
					nucleoVertedero.setClaveVertedero(rs.getString("clave_vt"));
					nucleoVertedero.setCodProvVertedero(rs.getString("codprov_vt"));
					nucleoVertedero.setCodMunicVertedero(rs.getString("codmunic_vt"));
					nucleoVertedero.setCodOrdenVertedero(rs.getString("orden_vt"));
					nucleoVertedero.setCodProvNucleo(rs.getString("codprov"));
					nucleoVertedero.setCodMunicNucleo(rs.getString("codmunic"));
					nucleoVertedero.setCodEntNucleo(rs.getString("codentidad"));
					nucleoVertedero.setCodPoblNucleo(rs.getString("codpoblamiento"));
					nucleoVertedero.setObservaciones(rs.getString("observ"));
					nucleoVertedero.setFechaAlta(rs.getDate("fecha_alta"));
					nucleoVertedero.setFechaRevision(rs.getDate("fecha_revision"));
					nucleoVertedero.setEstadoRevision(new Integer(rs.getInt("estado_revision")));

					lstNucleos.add(nucleoVertedero); 
				}  
			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

		return lstNucleos;
	}

	public ArrayList getLstNucleosTramosConduccion(int idConduccion){

		ArrayList lstNucleos = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{        
			TramosConduccionEIEL conduccion = null;

			conduccion = getTramosConduccionEIEL(idConduccion);

			if (conduccion != null){

				ps = conn.prepareStatement("EIELgetListaNucleosTramosConduccion");
				ps.setString(1, conduccion.getCodINEProvincia());
				ps.setString(2, conduccion.getCodINEMunicipio());
				ps.setString(3, conduccion.getTramo_cn());
				rs = ps.executeQuery(); 

				while (rs.next())
				{
					NucleoTramosConduccion nucleoTramosConduccion = new NucleoTramosConduccion();
					nucleoTramosConduccion.setCodProvTramosConduccion(rs.getString("codprov_tcn"));
					nucleoTramosConduccion.setCodMunicTramosConduccion(rs.getString("codmunic_tcn"));
					nucleoTramosConduccion.setCodOrdenTramosConduccion(rs.getString("tramo_cn"));
					nucleoTramosConduccion.setCodProvNucleo(rs.getString("codprov_pobl"));
					nucleoTramosConduccion.setCodMunicNucleo(rs.getString("codmunic_pobl"));
					nucleoTramosConduccion.setCodEntNucleo(rs.getString("codentidad_pobl"));
					nucleoTramosConduccion.setCodPoblNucleo(rs.getString("codpoblamiento_pobl"));
					nucleoTramosConduccion.setObservaciones(rs.getString("observ"));
					nucleoTramosConduccion.setPmi(rs.getFloat("pmi"));
					nucleoTramosConduccion.setPmf(rs.getFloat("pmf"));
					nucleoTramosConduccion.setFechaRevision(rs.getDate("fecha_revision"));
					nucleoTramosConduccion.setEstadoRevision(new Integer(rs.getInt("estado_revision")));
					nucleoTramosConduccion.setClaveTramosConduccion(rs.getString("clave_tcn"));
					lstNucleos.add(nucleoTramosConduccion); 
				}  
			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

		return lstNucleos;
	}
	
	public ArrayList getLstNucleosTramosConduccion(String clave, String codigoProvincia, String codigoMunicipio, String codigoOrden){     

			ArrayList<NucleoTramosConduccion> lstNucleos = new ArrayList<NucleoTramosConduccion>();

			PreparedStatement ps = null;
			ResultSet rs = null;

			if (clave == null){
				clave = "";
			}
			if (codigoProvincia == null){
				codigoProvincia = "";
			}
			if (codigoMunicipio == null){
				codigoMunicipio = "";
			}
			if (codigoOrden == null){
				codigoOrden = "";
			}

			try {
				ps = conn.prepareStatement("EIELgetListaNucleosTramosConduccion");

				ps.setString(1, clave);
				ps.setString(2, codigoProvincia);
				ps.setString(3, codigoMunicipio);
				ps.setString(4, codigoOrden);
				rs = ps.executeQuery(); 
	
				while (rs.next())
				{
						NucleoTramosConduccion nucleoTramosConduccion = new NucleoTramosConduccion();
						nucleoTramosConduccion.setCodProvTramosConduccion(rs.getString("codprov_tcn"));
						nucleoTramosConduccion.setCodMunicTramosConduccion(rs.getString("codmunic_tcn"));
						nucleoTramosConduccion.setCodOrdenTramosConduccion(rs.getString("tramo_tcn"));
						nucleoTramosConduccion.setCodProvNucleo(rs.getString("codprov_pobl"));
						nucleoTramosConduccion.setCodMunicNucleo(rs.getString("codmunic_pobl"));
						nucleoTramosConduccion.setCodEntNucleo(rs.getString("codentidad_pobl"));
						nucleoTramosConduccion.setCodPoblNucleo(rs.getString("codpoblamiento_pobl"));
						nucleoTramosConduccion.setObservaciones(rs.getString("observ"));
						nucleoTramosConduccion.setPmi(rs.getFloat("pmi"));
						nucleoTramosConduccion.setPmf(rs.getFloat("pmf"));
						nucleoTramosConduccion.setFechaRevision(rs.getDate("fecha_revision"));
						nucleoTramosConduccion.setEstadoRevision(new Integer(rs.getInt("estado_revision")));
						nucleoTramosConduccion.setClaveTramosConduccion(rs.getString("clave_tcn"));
						lstNucleos.add(nucleoTramosConduccion); 
				
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				safeClose(conn, ps, rs);
			}

		return lstNucleos;
	}

	/**
	 * @author davidcaramazana
	 * Devuelve todos los Nucleos asociados al elemento
	 */
	public ArrayList getLstNucleosColector(String clave, String codigoProvincia, String codigoMunicipio, String codigoOrden){     
		       
		ArrayList<NucleoColector> lstNucleos = new ArrayList<NucleoColector>();

		PreparedStatement ps = null;
		ResultSet rs = null;

		if (clave == null){
			clave = "";
		}
		if (codigoProvincia == null){
			codigoProvincia = "";
		}
		if (codigoMunicipio == null){
			codigoMunicipio = "";
		}
		if (codigoOrden == null){
			codigoOrden = "";
		}
			
		try {
			ps = conn.prepareStatement("EIELgetListaNucleosColector");

			ps.setString(1, clave);
			ps.setString(2, codigoProvincia);
			ps.setString(3, codigoMunicipio);
			ps.setString(4, codigoOrden);
			rs = ps.executeQuery(); 
			while (rs.next()){
					NucleoColector nucleoColector = new NucleoColector();
					nucleoColector.setClaveColector(rs.getString("clave_tcl"));
					nucleoColector.setCodProvColector(rs.getString("codprov_tcl"));
					nucleoColector.setCodMunicColector(rs.getString("codmunic_tcl"));
					nucleoColector.setCodOrdenColector(rs.getString("tramo_cl"));
					nucleoColector.setPmf(rs.getFloat("pmf"));
					nucleoColector.setPmi(rs.getFloat("pmi"));
					nucleoColector.setCodProvNucleo(rs.getString("codprov_pobl"));
					nucleoColector.setCodMunicNucleo(rs.getString("codmunic_pobl"));
					nucleoColector.setCodEntNucleo(rs.getString("codentidad_pobl"));
					nucleoColector.setCodPoblNucleo(rs.getString("codpoblamiento_pobl"));
					nucleoColector.setObservaciones(rs.getString("observ"));
					nucleoColector.setFechaRevision(rs.getDate("fecha_revision"));
					nucleoColector.setEstadoRevision(new Integer(rs.getInt("estado_revision")));
					nucleoColector.setClave(rs.getString("clave_tcl"));
					lstNucleos.add(nucleoColector);  
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			safeClose(conn, ps, rs);
		}
		
		return lstNucleos;
	}   
	
	/**
	 * @author davidcaramazana
	 * Devuelve todos los Nucleos asociados al elemento
	 */
	public ArrayList getLstNucleosColector(int idColector){
		ArrayList lstNucleos = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{        
			ColectorEIEL colector = null;
			colector = getColectorEIEL(idColector);			
			if (colector != null){				
				ps = conn.prepareStatement("EIELgetListaNucleosColector");
				ps.setString(1, colector.getClave());
				ps.setString(2, colector.getCodINEProvincia());
				ps.setString(3, colector.getCodINEMunicipio());
				ps.setString(4, colector.getCodOrden());
				rs = ps.executeQuery(); 
				while (rs.next()){
					NucleoColector nucleoColector = new NucleoColector();
					nucleoColector.setClaveColector(rs.getString("clave_tcl"));
					nucleoColector.setCodProvColector(rs.getString("codprov_tcl"));
					nucleoColector.setCodMunicColector(rs.getString("codmunic_tcl"));
					nucleoColector.setCodOrdenColector(rs.getString("tramo_cl"));
					nucleoColector.setPmf(rs.getFloat("pmf"));
					nucleoColector.setPmi(rs.getFloat("pmi"));
					nucleoColector.setCodProvNucleo(rs.getString("codprov_pobl"));
					nucleoColector.setCodMunicNucleo(rs.getString("codmunic_pobl"));
					nucleoColector.setCodEntNucleo(rs.getString("codentidad_pobl"));
					nucleoColector.setCodPoblNucleo(rs.getString("codpoblamiento_pobl"));
					nucleoColector.setObservaciones(rs.getString("observ"));
					nucleoColector.setFechaRevision(rs.getDate("fecha_revision"));
					nucleoColector.setEstadoRevision(new Integer(rs.getInt("estado_revision")));
					nucleoColector.setClave(rs.getString("clave_tcl"));
					lstNucleos.add(nucleoColector); 
				}  
			}
		} catch (SQLException ex) {           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return lstNucleos;
	}   

	/**
	 * @author davidcaramazana
	 * Devuelve todos los Nucleos asociados al elemento
	 */
	public ArrayList getLstPuntosVertidoEmisario(String clave, String codigoProvincia, String codigoMunicipio, String codigoOrden){
		ArrayList lstNucleos = new ArrayList();      
			PreparedStatement ps = null;
			ResultSet rs = null;
					
			try {
				ps = conn.prepareStatement("EIELgetListaPuntoVertidoEmisario");

				ps.setString(1, clave);
				ps.setString(2, codigoProvincia);
				ps.setString(3,codigoMunicipio);
				ps.setString(4, codigoOrden);
				rs = ps.executeQuery(); 
				while (rs.next()){
					
						PuntoVertidoEmisario puntoVertido = new PuntoVertidoEmisario();
						puntoVertido.setClaveEmisario(rs.getString("clave_tem"));
						puntoVertido.setCodProvEmisario(rs.getString("codprov_tem"));
						puntoVertido.setCodMunicEmisario(rs.getString("codmunic_tem"));
						puntoVertido.setCodOrdenEmisario(rs.getString("tramo_em"));
						puntoVertido.setPmf(rs.getFloat("pmf"));
						puntoVertido.setPmi(rs.getFloat("pmi"));
						puntoVertido.setCodProvPuntoVertido(rs.getString("codprov_pv"));
						puntoVertido.setCodMunicPuntoVertido(rs.getString("codmunic_pv"));
						puntoVertido.setCodClavePuntoVertido(rs.getString("clave_pv"));
						puntoVertido.setCodOrdenPuntoVertido(rs.getString("orden_pv"));
						puntoVertido.setObservaciones(rs.getString("observ"));
						puntoVertido.setFechaRevision(rs.getDate("fecha_revision"));
						puntoVertido.setEstadoRevision(new Integer(rs.getInt("estado_revision")));					
						lstNucleos.add(puntoVertido); 
					}  
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				safeClose(conn, ps, rs);
			}
		return lstNucleos;
	}
	
	/**
	 * 
	 * Devuelve todos los Nucleos asociados al elemento
	 */
	public ArrayList getLstPuntosVertidoEmisario(int idEmisario){
		ArrayList lstNucleos = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{        

			EmisariosEIEL Emisario = null;
			Emisario = getEmisarioEIEL(idEmisario);			
			if (Emisario != null){				
				ps = conn.prepareStatement("EIELgetListaPuntoVertidoEmisario");
				ps.setString(1, Emisario.getClave());
				ps.setString(2, Emisario.getCodINEProvincia());
				ps.setString(3, Emisario.getCodINEMunicipio());
				ps.setString(4, Emisario.getCodOrden());
				rs = ps.executeQuery(); 
				while (rs.next()){
					PuntoVertidoEmisario puntoVertido = new PuntoVertidoEmisario();
					puntoVertido.setClaveEmisario(rs.getString("clave_tem"));
					puntoVertido.setCodProvEmisario(rs.getString("codprov_tem"));
					puntoVertido.setCodMunicEmisario(rs.getString("codmunic_tem"));
					puntoVertido.setCodOrdenEmisario(rs.getString("tramo_em"));
					puntoVertido.setPmf(rs.getFloat("pmf"));
					puntoVertido.setPmi(rs.getFloat("pmi"));
					puntoVertido.setCodProvPuntoVertido(rs.getString("codprov_pv"));
					puntoVertido.setCodMunicPuntoVertido(rs.getString("codmunic_pv"));
					puntoVertido.setCodClavePuntoVertido("PV");
					puntoVertido.setCodOrdenPuntoVertido(rs.getString("clave_pv"));
					puntoVertido.setObservaciones(rs.getString("observ"));
					puntoVertido.setFechaRevision(rs.getDate("fecha_revision"));
					puntoVertido.setEstadoRevision(new Integer(rs.getInt("estado_revision")));					
					lstNucleos.add(puntoVertido); 
				}  
			}
		} catch (SQLException ex) {           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return lstNucleos;
	}

	/**
	 * Obtiene un arraylist de objetos de tipo Municipios con todos los municipios
	 * del sistema
	 * 
	 * @param Provincia Provincia de la que se quieren obtener los municipios
	 * @return ArrayList de objetos Municipio
	 * @throws DataException Si se produce un error de acceso a datos 
	 */
	public ArrayList obtenerMunicipios (String codProv) 
	{        
		ArrayList lstMunicipios = new ArrayList();
		PreparedStatement s = null;
		ResultSet r = null;
		if (ConstantesLocalGISEIEL.idMunicipio == null){

			try
			{            
				

				s = conn.prepareStatement("MCobtenerMunicipios");
				s.setString(1, codProv);
				r = s.executeQuery();

				String municipio = new String();
				lstMunicipios.add(municipio);

				while (r.next())
				{
					municipio = r.getString("id_ine");
					lstMunicipios.add(municipio); 
				}        

				if (s!=null) s.close();
				if (r!= null) r.close(); 
				conn.close();

				Comparator municipiosComparator = new Comparator(){
					public int compare(Object o1, Object o2) {

						String desc1 = (String) o1;
						if (desc1 == null)
							desc1 = "";
						String desc2 = (String) o2;
						if (desc2 == null)
							desc2 = "";

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
			finally{
				safeClose(conn, s, r);
			}
		}
		else{
			lstMunicipios.add("");
			System.out.println();

			//lstMunicipios.add(ConstantesLocalGISEIEL.Municipio.substring(2, 5));
			lstMunicipios.add(ConstantesLocalGISEIEL.Municipio);
		}

		return lstMunicipios;
	}
	
	public ArrayList obtenerIdMunicipios (String codProv){
		return obtenerIdMunicipios(codProv,null);
	}
	
	public ArrayList obtenerIdMunicipios (String codProv,String idMunicipio) 
	{        
		ArrayList lstMunicipios = new ArrayList();
		PreparedStatement s = null;
		ResultSet r = null;
		if (ConstantesLocalGISEIEL.idMunicipio == null){

			try
			{            
				s = conn.prepareStatement("MCobtenerMunicipios");
				s.setString(1, codProv);
				r = s.executeQuery();

				String municipio = new String();
				lstMunicipios.add(municipio);

				while (r.next())
				{
					municipio = r.getString("id_ine");
					lstMunicipios.add(municipio); 
				}        

				Comparator municipiosComparator = new Comparator(){
					public int compare(Object o1, Object o2) {

						String desc1 = (String) o1;
						if (desc1 == null)
							desc1 = "";
						String desc2 = (String) o2;
						if (desc2 == null)
							desc2 = "";

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
			finally{
				safeClose(conn, s, r);
			}
		}
		else{
			if (idMunicipio!=null){
				lstMunicipios.add("");
				//lstMunicipios.add(ConstantesLocalGISEIEL.Municipio.substring(2, 5));
				lstMunicipios.add(idMunicipio);
			}
			else{
				lstMunicipios.add("");
				//lstMunicipios.add(ConstantesLocalGISEIEL.Municipio.substring(2, 5));
				lstMunicipios.add(ConstantesLocalGISEIEL.idMunicipio.substring(2, 5));
			}
		}

		return lstMunicipios;
	}


	public ArrayList obtenerNumInventario (Integer epigInventario) 
	{        
		ArrayList lstNumInventario = new ArrayList();
		com.geopista.protocol.inventario.Inventario eielInventario = new com.geopista.protocol.inventario.Inventario();
		eielInventario.setId(0);
		eielInventario.setNumInventario("");
		String numInventario = new String();
		int id = 0;
		lstNumInventario.add(eielInventario); 

		if (epigInventario != null){
			PreparedStatement s = null;
			ResultSet r = null;
			try
			{            
				s = conn.prepareStatement("SELECT numinventario, id FROM bienes_inventario WHERE revision_expirada = 9999999999 AND tipo = "+epigInventario+" AND id_municipio = "+Integer.parseInt(ConstantesLocalGISEIEL.idMunicipio)+" ORDER BY numinventario");

				r = s.executeQuery();
				
			//	lstNumInventario.add(numInventario);

				while (r.next())
				{
					numInventario = r.getString("numinventario");
					id = r.getInt("id");
					
					eielInventario = new com.geopista.protocol.inventario.Inventario();
					eielInventario.setId(id);
					eielInventario.setNumInventario(numInventario);
					lstNumInventario.add(eielInventario); 
				}        
			}
			catch (SQLException ex)
			{           
				new DataException(ex);
			}
			finally{
				safeClose(conn, s, r);
			}
		}
		else{
			lstNumInventario.add(eielInventario); 



		}

		return lstNumInventario;
	}
	
	
	public ArrayList obtenerTodosMunicipios (String codProv) 
	{        
		ArrayList lstMunicipios = new ArrayList();
		Municipio municipio = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try
		{            
			s = conn.prepareStatement("MCobtenerMunicipios");
			s.setString(1, codProv);
			r = s.executeQuery();

			municipio = new Municipio();
			lstMunicipios.add(municipio);

			while (r.next())
			{
				municipio = new Municipio();
				if (r.getString("id_ine")!=null)
					municipio.setIdIne(r.getString("id_ine"));
				else
					municipio.setIdIne("");
				municipio.setNombreOficial(r.getString("nombreoficial"));
				lstMunicipios.add(municipio); 
			}        
			Comparator municipiosComparator = new Comparator(){
				public int compare(Object o1, Object o2) {

					String desc1 = ((Municipio) o1).getIdIne();
					if (desc1 == null)
						desc1 = "";
					String desc2 = ((Municipio) o2).getIdIne();
					if (desc2 == null)
						desc2 = "";

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
		finally{
			safeClose(conn, s, r);
		}

		return lstMunicipios;
	}

	public ArrayList obtenerEntidades (MunicipioEIEL municipio) 
	{        
		ArrayList lstEntidades = new ArrayList();

		PreparedStatement s = null;
		ResultSet r = null;
		
		try
		{            

			s = conn.prepareStatement("EIELobtenerEntidades2");
			s.setString(1, municipio.getCodProvincia());
			s.setString(2, municipio.getCodMunicipio());
			r = s.executeQuery();  
			String entidad = new String();
			lstEntidades.add(entidad); 
			while (r.next())
			{
				entidad = r.getString("codentidad");
				lstEntidades.add(entidad); 
			}        
			Comparator entidadesComparator = new Comparator(){
				public int compare(Object o1, Object o2) {

					String desc1 = (String) o1;
					if (desc1 == null)
						desc1 = "";
					String desc2 =(String) o2;
					if (desc2 == null)
						desc2 = "";

					Collator myCollator=Collator.getInstance(new Locale(locale));
					myCollator.setStrength(Collator.PRIMARY);
					return myCollator.compare(desc1, desc2);                    
				}
			};


			Collections.sort(lstEntidades, entidadesComparator);    

		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
				safeClose(conn, s, r);
		}

		return lstEntidades;
	}
	
	
	public ArrayList obtenerEntidadesConNombre (MunicipioEIEL municipio) 
	{        
		ArrayList lstEntidades = new ArrayList();	
		EntidadesSingularesEIEL entidad = null;

		PreparedStatement s = null;
		ResultSet r = null;
		
		try
		{            
			s = conn.prepareStatement("MCEIELobtenerEntidades");
			s.setString(1, municipio.getCodProvincia());
			s.setString(2, municipio.getCodMunicipio());
			r = s.executeQuery();  
			entidad = new EntidadesSingularesEIEL();
			entidad.setCodINEEntidad("");
			entidad.setDenominacion("");
			lstEntidades.add(entidad); 
			while (r.next())
			{
				entidad = new EntidadesSingularesEIEL();
				entidad.setCodINEEntidad(r.getString("codentidad"));
				entidad.setDenominacion(r.getString("denominacion"));
				lstEntidades.add(entidad); 
			}        
			
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, s, r);
		}

		return lstEntidades;
	}
			
	public static boolean safeClose(Connection connection, Statement statement, ResultSet rs) {

		try {
			connection.commit();
		} catch (Exception ex2) {
		}
		try {
			if (rs!=null)
				rs.close();
		} catch (Exception ex2) {
		}
		try {
			if (statement!=null)
				statement.close();
		} catch (Exception ex2) {
		}
		try {
			connection.close();
			CPoolDatabase.releaseConexion();
		} catch (Exception ex2) {
		}

		return true;
	}

	
	public ArrayList obtenerOrdenPuntosVertido (MunicipioEIEL municipio, String clave) 
	{        
		ArrayList lstOrdenesPV = new ArrayList();
		PreparedStatement s = null;
		ResultSet r = null;
		try
		{            
			s = conn.prepareStatement("EIELobtenerOrdenPuntosVertido");
			s.setString(1, clave);
			s.setString(2, municipio.getCodProvincia());
			s.setString(3, municipio.getCodMunicipio());
			r = s.executeQuery();  
			String ordenPV = new String();
			lstOrdenesPV.add(ordenPV); 
			while (r.next())
			{
				ordenPV = r.getString("orden_pv");
				lstOrdenesPV.add(ordenPV); 
			}        

			Comparator entidadesComparator = new Comparator(){
				public int compare(Object o1, Object o2) {

					String desc1 = (String) o1;
					if (desc1 == null)
						desc1 = "";
					String desc2 =(String) o2;
					if (desc2 == null)
						desc2 = "";

					Collator myCollator=Collator.getInstance(new Locale(locale));
					myCollator.setStrength(Collator.PRIMARY);
					return myCollator.compare(desc1, desc2);                    
				}
			};


			Collections.sort(lstOrdenesPV, entidadesComparator);    

		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, s, r);
		}

		return lstOrdenesPV;
	}

	public ArrayList obtenerNucleos (EntidadEIEL entidad) 
	{        
		ArrayList lstNucleos = new ArrayList();
		PreparedStatement s = null;
		ResultSet r = null;
		try
		{            
			s = conn.prepareStatement("EIELobtenerNucleos");
			s.setString(1, entidad.getCodProvincia());
			s.setString(2, entidad.getCodMunicipio());
			s.setString(3, entidad.getCodEntidad());
			r = s.executeQuery();  
			String idNucleo = new String();
			lstNucleos.add(idNucleo); 
			while (r.next())
			{
				idNucleo = r.getString("codpoblamiento");
				String nomOficialNucleo = r.getString("nombre_oficial");
				lstNucleos.add(idNucleo); 
			}        

		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, s, r);
		}

		return lstNucleos;
	}


	public ArrayList obtenerNucleosConNombre (EntidadesSingularesEIEL entidad) 
	{        
		ArrayList lstNucleos = new ArrayList();
		NucleosPoblacionEIEL nucleo = null;
		PreparedStatement s = null;
		ResultSet r = null;
		try
		{            

			int caso = casoConsulta(entidad);

			if (caso == 1){
				s = conn.prepareStatement("EIELobtenerNucleos3");
				s.setString(1, entidad.getCodINEProvincia());
			} else if (caso == 2){
				s = conn.prepareStatement("EIELobtenerNucleos2");
				s.setString(1, entidad.getCodINEProvincia());
				s.setString(2, entidad.getCodINEMunicipio());
			} else{
				s = conn.prepareStatement("EIELobtenerNucleos");
				s.setString(1, entidad.getCodINEProvincia());
				s.setString(2, entidad.getCodINEMunicipio());
				s.setString(3, entidad.getCodINEEntidad());
			}

			r = s.executeQuery();  
			nucleo = new NucleosPoblacionEIEL();
			nucleo.setCodINEEntidad("");
			nucleo.setCodINEPoblamiento("");
			lstNucleos.add(nucleo); 
			while (r.next())
			{
				nucleo = new NucleosPoblacionEIEL();
				nucleo.setCodINEPoblamiento(r.getString("codpoblamiento"));
				nucleo.setNombreOficial(r.getString("nombre_oficial"));
				lstNucleos.add(nucleo); 
			}        

		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, s, r);
		}

		return lstNucleos;
	}

	private int casoConsulta (EntidadesSingularesEIEL entidad){


		if (entidad.getCodINEProvincia() != null && !entidad.getCodINEProvincia().equals("")){
			if (entidad.getCodINEMunicipio() != null && !entidad.getCodINEMunicipio().equals("")){
				if (entidad.getCodINEEntidad() != null && !entidad.getCodINEEntidad().equals("")){
					return 3;
				}
				return 2;
			}
			return 1;
		}

		return -1;
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
		PreparedStatement s = null;
		ResultSet r = null;
		try
		{            
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
		finally{
			safeClose(conn, s, r);
		}

		return lstMunicipios;
	}


	public CentrosEnsenianzaEIEL getPanelCentrosEnsenianzaEIEL(String clave, String codprov, String codmunic, String entidad, String nucleo, String orden ) {
		CentrosEnsenianzaEIEL centrosEnsenianza = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {


			ps = conn.prepareStatement("EIELgetPanelCentrosEnsenianza");
			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, entidad);
			ps.setString(5, nucleo);
			ps.setString(6, orden);
			rs = ps.executeQuery();

			while (rs.next()) {

				centrosEnsenianza = new CentrosEnsenianzaEIEL();
				centrosEnsenianza.setClave(rs.getString("clave"));
				centrosEnsenianza.setCodINEProvincia(rs.getString("codprov"));
				centrosEnsenianza.setCodINEMunicipio(rs.getString("codmunic"));
				centrosEnsenianza.setCodINEEntidad(rs.getString("codentidad"));
				centrosEnsenianza.setCodINEPoblamiento((rs.getString("codpoblamiento")));
				centrosEnsenianza.setCodOrden(rs.getString("orden_en"));

				centrosEnsenianza.setNombre(rs.getString("nombre"));
				centrosEnsenianza.setAmbito(rs.getString("ambito"));


				centrosEnsenianza.setSupCubierta(rs.getInt("s_cubierta"));
				centrosEnsenianza.setSupAire(rs.getInt("s_aire"));
				centrosEnsenianza.setSupSolar(rs.getInt("s_solar"));

				centrosEnsenianza.setEstado(rs.getString("estado"));
				centrosEnsenianza.setFechaInstalacion(rs.getDate("fecha_inst"));
				centrosEnsenianza.setObservaciones(rs.getString("observ"));
				centrosEnsenianza.setFechaRevision(rs.getDate("fecha_revision"));
				centrosEnsenianza.setEstadoRevision(rs.getInt("estado_revision"));
				centrosEnsenianza.setTitular(rs.getString("titular"));
			}
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();

			ps = conn.prepareStatement("EIELgetPanelNivelCentrosEnsenianza");

			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, entidad);
			ps.setString(5, nucleo);
			ps.setString(6, orden);
			rs = ps.executeQuery();

			while (rs.next()) {

				NivelesCentrosEnsenianza nivel = new NivelesCentrosEnsenianza();	

				nivel.setCodigoOrdenNivel(rs.getString("orden_en_nivel"));
				nivel.setNivel(rs.getString("nivel"));				
				nivel.setNumeroPlazas(new Integer(rs.getInt("plazas")));
				nivel.setUnidades(new Integer(rs.getInt("unidades")));
				nivel.setNumeroAlumnos(new Integer(rs.getInt("alumnos")));				
				nivel.setFechaCurso(rs.getDate("fecha_curso"));
				nivel.setFechaRevision(rs.getDate("fecha_revision"));
				nivel.setEstadoRevision(rs.getString("estado_revision"));
				nivel.setObservacionesNivel(rs.getString("observ"));				
				centrosEnsenianza.getListaNiveles().add(nivel);
			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return centrosEnsenianza;
	}



	public CentrosSanitariosEIEL getPanelCentrosSanitariosEIEL(String clave, String codprov, String codmunic, String entidad, String nucleo, String orden ) {
		CentrosSanitariosEIEL centrosSanitarios = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("EIELgetPanelCentrosSanitarios");
			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, entidad);
			ps.setString(5, nucleo);
			ps.setString(6, orden);
			rs = ps.executeQuery();
			while (rs.next()) {
				centrosSanitarios = new CentrosSanitariosEIEL();
				centrosSanitarios.setClave(rs.getString("clave"));
				centrosSanitarios.setCodINEProvincia(rs.getString("codprov"));
				centrosSanitarios.setCodINEMunicipio(rs.getString("codmunic"));
				centrosSanitarios.setCodINEEntidad(rs.getString("codentidad"));
				centrosSanitarios.setCodINEPoblamiento((rs.getString("codpoblamiento")));
				centrosSanitarios.setOrden(rs.getString("orden_sa"));

				centrosSanitarios.setNombre(rs.getString("nombre"));
				centrosSanitarios.setTipo(rs.getString("tipo"));

				centrosSanitarios.setTitularidad(rs.getString("titular"));
				centrosSanitarios.setGestion(rs.getString("gestor"));

				centrosSanitarios.setSupCubierta(rs.getInt("s_cubierta"));
				centrosSanitarios.setSupLibre(rs.getInt("s_aire"));
				centrosSanitarios.setSupSolar(rs.getInt("s_solar"));

				centrosSanitarios.setUci(rs.getString("uci"));
				centrosSanitarios.setNumCamas(rs.getInt("n_camas"));

				centrosSanitarios.setEstado(rs.getString("estado"));
				centrosSanitarios.setFechaInstalacion(rs.getDate("fecha_inst"));
				centrosSanitarios.setObservaciones(rs.getString("observ"));
				centrosSanitarios.setFechaRevision(rs.getDate("fecha_revision"));
				centrosSanitarios.setEstadoRevision(rs.getInt("estado_revision"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return centrosSanitarios;
	}


	public DiseminadosEIEL getPanelDiseminadosEIEL(String codprov, String codmunic) {
		DiseminadosEIEL diseminados = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("EIELgetPanelDiseminados");
			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			rs = ps.executeQuery();
			while (rs.next()) {
				diseminados = new DiseminadosEIEL();
				diseminados.setCodINEProvincia(rs.getString("codprov"));
				diseminados.setCodINEMunicipio(rs.getString("codmunic"));

				diseminados.setPadron(rs.getInt("padron_dis"));
				diseminados.setPoblacionEstacional(rs.getInt("pob_estaci"));
				diseminados.setViviendasTotales(rs.getInt("viv_total"));
				diseminados.setPlazasHoteleras(rs.getInt("hoteles"));
				diseminados.setPlazasCasasRurales(rs.getInt("casas_rural"));

				diseminados.setLongitudAbastecimiento(rs.getInt("longitud"));
				diseminados.setViviendasConAbastecimiento(rs.getInt("aag_v_cone"));
				diseminados.setViviendasSinAbastecimiento(rs.getInt("aag_v_ncon"));
				diseminados.setConsumoInvierno(rs.getInt("aag_c_invi"));
				diseminados.setConsumoVerano(rs.getInt("aag_c_vera"));
				diseminados.setViviendasExcesoPresion(rs.getInt("aag_v_expr"));
				diseminados.setViviendasDefectoPresion(rs.getInt("aag_v_depr"));

				diseminados.setLongDeficitariaAbast(rs.getInt("aag_l_defi"));
				diseminados.setViviendasDeficitAbast(rs.getInt("aag_v_defi"));
				diseminados.setPoblacionResidenteDefAbast(rs.getInt("aag_pr_def"));
				diseminados.setPoblacionEstacionalDefAbast(rs.getInt("aag_pe_def"));

				diseminados.setPoblacionResidenteAbastAuto(rs.getInt("aau_pob_re"));
				diseminados.setPoblacionEstacionalAbastAuto(rs.getInt("aau_pob_es"));
				diseminados.setViviendasDefAbastAuto(rs.getInt("aau_def_vi"));
				diseminados.setPoblacionResidenteDefAbastAuto(rs.getInt("aau_def_re"));
				diseminados.setPoblacionEstacionalDefAbastAuto(rs.getInt("aau_def_es"));
				diseminados.setFuentesNoControladas(rs.getInt("aau_fencon"));

				diseminados.setLongitudSaneamiento(rs.getInt("longi_ramal"));
				diseminados.setViviendasConSaneamiento(rs.getInt("syd_v_cone"));
				diseminados.setViviendasSinSaneamiento(rs.getInt("syd_v_ncon"));
				diseminados.setLongDeficitariaSaneam(rs.getInt("syd_l_defi"));
				diseminados.setViviendasDefSaneam(rs.getInt("syd_v_defi"));
				diseminados.setPoblacionResidenteDefSaneam(rs.getInt("syd_pr_def"));
				diseminados.setPoblacionEstacionalDefSaneam(rs.getInt("syd_pe_def"));
				diseminados.setCaudalDesaguado(rs.getInt("syd_c_desa"));
				diseminados.setCaudalTratado(rs.getInt("syd_c_trat"));

				diseminados.setViviendasSaneamientoAuto(rs.getInt("sau_vivien"));
				diseminados.setPoblacionEstacionalSaneamAuto(rs.getInt("sau_pob_es"));
				diseminados.setViviendasDeficitSaneamAuto(rs.getInt("sau_vi_def"));
				diseminados.setPoblacionResidenteDefSaneamAuto(rs.getInt("sau_pob_re_def"));
				diseminados.setPoblacionEstacionalDefSaneamAuto(rs.getInt("sau_pob_es_def"));

				diseminados.setTmBasura(rs.getFloat("produ_basu"));
				diseminados.setContenedores(rs.getInt("contenedores"));
				diseminados.setViviendasSinBasura(rs.getInt("rba_v_sser"));
				diseminados.setPoblacionResidenteSinBasura(rs.getInt("rba_pr_sse"));
				diseminados.setPoblacionEstacionalSinBasura(rs.getInt("rba_pe_sse"));
				diseminados.setPlantillaLimpieza(rs.getInt("rba_plalim"));

				diseminados.setPuntosLuz(rs.getInt("puntos_luz"));
				diseminados.setViviendasSinAlumbrado(rs.getInt("alu_v_sin"));
				diseminados.setLongDeficitariaAlumbrado(rs.getInt("alu_l_sin"));
				diseminados.setVivendasAbastecimientoAuto(rs.getInt("aau_vivien"));

				diseminados.setFuentesControladas(rs.getInt("aau_fecont"));
				diseminados.setPoblacionResidenteAbastAuto(rs.getInt("sau_pob_re"));

			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return diseminados;
	}


	public EdificiosSinUsoEIEL getPanelEdificiosSinUsoIEL(String clave, String codprov, String codmunic, String entidad, String nucleo, String orden ) {
		EdificiosSinUsoEIEL edificiosSinUso = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelEdificiosSinUso");
			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, entidad);
			ps.setString(5, nucleo);
			ps.setString(6, orden);
			rs = ps.executeQuery();
			while (rs.next()) {
				edificiosSinUso = new EdificiosSinUsoEIEL();
				edificiosSinUso.setClave(rs.getString("clave"));
				edificiosSinUso.setCodINEProvincia(rs.getString("codprov"));
				edificiosSinUso.setCodINEMunicipio(rs.getString("codmunic"));
				edificiosSinUso.setCodINEEntidad(rs.getString("codentidad"));
				edificiosSinUso.setCodINEPoblamiento((rs.getString("codpoblamiento")));
				edificiosSinUso.setCodOrden(rs.getString("orden_su"));

				edificiosSinUso.setNombre(rs.getString("nombre"));

				edificiosSinUso.setTitularidad(rs.getString("titular"));

				edificiosSinUso.setSupCubierta(rs.getInt("s_cubierta"));
				edificiosSinUso.setSupLibre(rs.getInt("s_aire"));
				edificiosSinUso.setSupSolar(rs.getInt("s_solar"));
				edificiosSinUso.setUsoAnterior(rs.getString("uso_anterior"));			
				edificiosSinUso.setEstado(rs.getString("estado"));

				edificiosSinUso.setObservaciones(rs.getString("observ"));
				edificiosSinUso.setFechaRevision(rs.getDate("fecha_revision"));
				edificiosSinUso.setEstadoRevision(rs.getInt("estado_revision"));
				edificiosSinUso.setInst_pertenece(rs.getString("inst_pertenece"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return edificiosSinUso;
	}



	public EntidadesSingularesEIEL getPanelEntidadesSingularesEIEL(String codprov, String codmunic) {
		EntidadesSingularesEIEL endidadesSingulares = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelEntidadesSingulares");
			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			rs = ps.executeQuery();
			while (rs.next()) {
				endidadesSingulares = new EntidadesSingularesEIEL();

				endidadesSingulares.setCodINEProvincia(rs.getString("codprov"));
				endidadesSingulares.setCodINEMunicipio(rs.getString("codmunic"));
				endidadesSingulares.setCodINEEntidad(rs.getString("codentidad"));

				endidadesSingulares.setDenominacion(rs.getString("denominacion"));

				endidadesSingulares.setObservaciones(rs.getString("observ"));
				endidadesSingulares.setFechaRevision(rs.getDate("fecha_revision"));
				endidadesSingulares.setEstadoRevision(rs.getInt("estado_revision"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return endidadesSingulares;
	}

	public ArrayList getPanelLstEntidadesSingularesEIEL(String codprov, String codmunic) {

		ArrayList lstEntidadesSingulares = new ArrayList();
		EntidadesSingularesEIEL endidadesSingulares = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("EIELgetPanelEntidadesSingulares");
			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			rs = ps.executeQuery();

			while (rs.next()) {

				endidadesSingulares = new EntidadesSingularesEIEL();

				endidadesSingulares.setCodINEProvincia(rs.getString("codprov"));
				endidadesSingulares.setCodINEMunicipio(rs.getString("codmunic"));
				endidadesSingulares.setCodINEEntidad(rs.getString("codentidad"));

				endidadesSingulares.setDenominacion(rs.getString("denominacion"));

				endidadesSingulares.setObservaciones(rs.getString("observ"));
				endidadesSingulares.setFechaRevision(rs.getDate("fecha_revision"));
				endidadesSingulares.setEstadoRevision(rs.getInt("estado_revision"));

				lstEntidadesSingulares.add(endidadesSingulares);

			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return lstEntidadesSingulares;
	}



	public NucleoEncuestado7EIEL getPanelInfoTerminosMunicipalesEIEL(String codprov, String codmunic, String entidad, String nucleo ) {
		NucleoEncuestado7EIEL infoTerminosMunicipales = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("EIELgetPanelInfoTerminosMunicipales");
			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			ps.setString(3, entidad);
			ps.setString(4, nucleo);
			rs = ps.executeQuery();
			while (rs.next()) {
				infoTerminosMunicipales = new NucleoEncuestado7EIEL();

				infoTerminosMunicipales.setCodINEProvincia(rs.getString("codprov"));
				infoTerminosMunicipales.setCodINEMunicipio(rs.getString("codmunic"));
				infoTerminosMunicipales.setCodINEEntidad(rs.getString("codentidad"));
				infoTerminosMunicipales.setCodINEPoblamiento((rs.getString("codpoblamiento")));

				infoTerminosMunicipales.setTvAntena(rs.getString("tv_ant"));
				infoTerminosMunicipales.setTvCable(rs.getString("tv_ca"));

				infoTerminosMunicipales.setCalidadGSM(rs.getString("tm_gsm"));
				infoTerminosMunicipales.setCalidadUMTS(rs.getString("tm_umts"));

				infoTerminosMunicipales.setCorreos(rs.getString("correo"));

				infoTerminosMunicipales.setRdsi(rs.getString("ba_rd"));
				infoTerminosMunicipales.setAdsl(rs.getString("ba_xd"));
				infoTerminosMunicipales.setWifi(rs.getString("ba_wi"));
				infoTerminosMunicipales.setInternetTV(rs.getString("ba_ca"));
				infoTerminosMunicipales.setInternetRed(rs.getString("ba_rb"));
				infoTerminosMunicipales.setInternetSatelite(rs.getString("ba_st"));
				infoTerminosMunicipales.setInternetPublico(rs.getString("capi"));

				infoTerminosMunicipales.setCalidadElectricidad(rs.getString("electric"));
				infoTerminosMunicipales.setCalidadGas(rs.getString("gas"));

				infoTerminosMunicipales.setViviendasDeficitariasAlumbrado(rs.getInt("alu_v_sin"));
				infoTerminosMunicipales.setLongitudDeficitariaAlumbrado(rs.getInt("alu_l_sin"));

				infoTerminosMunicipales.setObservaciones(rs.getString("observ"));
				infoTerminosMunicipales.setFechaRevision(rs.getDate("fecha_revision"));
				infoTerminosMunicipales.setEstadoRevision(rs.getInt("estado_revision"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return infoTerminosMunicipales;
	}


	public InstalacionesDeportivasEIEL getPanelInstalacionesDeportivasEIEL(String clave, String codprov, String codmunic, String entidad, String nucleo, String orden ) {

		InstalacionesDeportivasEIEL instalacionesDeportivas = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			ps = conn.prepareStatement("EIELgetPanelInstalacionesDeportivas");
			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, entidad);
			ps.setString(5, nucleo);
			ps.setString(6, orden);
			rs = ps.executeQuery();

			while (rs.next()) {

				instalacionesDeportivas = new InstalacionesDeportivasEIEL();
				instalacionesDeportivas.setClave(rs.getString("clave"));
				instalacionesDeportivas.setCodINEProvincia(rs.getString("codprov"));
				instalacionesDeportivas.setCodINEMunicipio(rs.getString("codmunic"));
				instalacionesDeportivas.setCodINEEntidad(rs.getString("codentidad"));
				instalacionesDeportivas.setCodINEPoblamiento((rs.getString("codpoblamiento")));
				instalacionesDeportivas.setOrdenIdDeportes(rs.getString("orden_id"));

				instalacionesDeportivas.setNombre(rs.getString("nombre"));
				instalacionesDeportivas.setTipo(rs.getString("tipo"));

				instalacionesDeportivas.setTitular(rs.getString("titular"));
				instalacionesDeportivas.setGestor(rs.getString("gestor"));

				instalacionesDeportivas.setSupCubierta(rs.getInt("s_cubierta"));
				instalacionesDeportivas.setSupAire(rs.getInt("s_aire"));
				instalacionesDeportivas.setSupSolar(rs.getInt("s_solar"));

				instalacionesDeportivas.setEstado(rs.getString("estado"));
				instalacionesDeportivas.setFechaInstalacion(rs.getDate("fecha_inst"));
				instalacionesDeportivas.setObservaciones(rs.getString("observ"));
				instalacionesDeportivas.setFechaRevision(rs.getDate("fecha_revision"));
				instalacionesDeportivas.setEstadoRevision(rs.getInt("estado_revision"));
				instalacionesDeportivas.setInst_P(rs.getString("inst_pertenece"));
			}
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();

			ps = conn.prepareStatement("EIELgetPanelTipoInstalacionesDeportivas");

			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, entidad);
			ps.setString(5, nucleo);
			ps.setString(6, orden);

			rs = ps.executeQuery();
			while (rs.next()) {

				TipoDeporte tipoDeporte = new TipoDeporte();
				tipoDeporte.setOrden(rs.getString("orden_id_deportes"));
				tipoDeporte.setObservaciones(rs.getString("observ"));
				tipoDeporte.setTipo(rs.getString("tipo_deporte"));
				tipoDeporte.setFechaInstalacion(rs.getDate("fecha_inst"));		

				instalacionesDeportivas.getListaTipos().add(tipoDeporte);

			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return instalacionesDeportivas;
	}


	public LonjasMercadosEIEL getPanelLonjasMercadosEIEL(String clave, String codprov, String codmunic, String entidad, String nucleo, String orden ) {
		LonjasMercadosEIEL lonjasMercados = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("EIELgetPanelLonjasMercados");
			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, entidad);
			ps.setString(5, nucleo);
			ps.setString(6, orden);
			rs = ps.executeQuery();
			while (rs.next()) {
				lonjasMercados = new LonjasMercadosEIEL();
				lonjasMercados.setClave(rs.getString("clave"));
				lonjasMercados.setCodINEProvincia(rs.getString("codprov"));
				lonjasMercados.setCodINEMunicipio(rs.getString("codmunic"));
				lonjasMercados.setCodINEEntidad(rs.getString("codentidad"));
				lonjasMercados.setCodINEPoblamiento((rs.getString("codpoblamiento")));
				lonjasMercados.setOrden(rs.getString("orden_lm"));

				lonjasMercados.setNombre(rs.getString("nombre"));
				lonjasMercados.setTipo(rs.getString("tipo"));

				lonjasMercados.setTitular(rs.getString("titular"));
				lonjasMercados.setGestion(rs.getString("gestor"));

				lonjasMercados.setSuperficieCubierta(rs.getInt("s_cubierta"));
				lonjasMercados.setSuperficieAireLibre(rs.getInt("s_aire"));
				lonjasMercados.setSuperficieSolar(rs.getInt("s_solar"));

				lonjasMercados.setEstado(rs.getString("estado"));
				lonjasMercados.setFechaInstalacion(rs.getDate("fecha_inst"));
				lonjasMercados.setObservaciones(rs.getString("observ"));
				lonjasMercados.setFechaRevision(rs.getDate("fecha_revision"));
				lonjasMercados.setEstadoRevision(rs.getInt("estado_revision"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return lonjasMercados;
	}


	public MataderosEIEL getPanelMataderosEIEL(String clave, String codprov, String codmunic, String entidad, String nucleo, String orden ) {
		MataderosEIEL mataderos = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			ps = conn.prepareStatement("EIELgetPanelMataderos");

			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, entidad);
			ps.setString(5, nucleo);
			ps.setString(6, orden);
			rs = ps.executeQuery();

			while (rs.next()) {

				mataderos = new MataderosEIEL();
				mataderos.setClave(rs.getString("clave"));
				mataderos.setCodINEProvincia(rs.getString("codprov"));
				mataderos.setCodINEMunicipio(rs.getString("codmunic"));
				mataderos.setCodINEEntidad(rs.getString("codentidad"));
				mataderos.setCodINEPoblamiento((rs.getString("codpoblamiento")));
				mataderos.setOrden(rs.getString("orden_mt"));

				mataderos.setNombre(rs.getString("nombre"));
				mataderos.setClase(rs.getString("clase"));

				mataderos.setTitular(rs.getString("titular"));
				mataderos.setGestion(rs.getString("gestor"));

				mataderos.setSuperficieCubierta(rs.getInt("s_cubierta"));
				mataderos.setSuperficieAireLibre(rs.getInt("s_aire"));
				mataderos.setSuperficieSolar(rs.getInt("s_solar"));

				mataderos.setEstado(rs.getString("estado"));

				mataderos.setCapacidadMax(rs.getInt("capacidad"));
				mataderos.setCapacidadUtilizada(rs.getInt("utilizacion"));

				mataderos.setTunel(rs.getString("tunel"));

				mataderos.setBovino(rs.getString("bovino"));
				mataderos.setOvino(rs.getString("ovino"));
				mataderos.setPorcino(rs.getString("porcino"));
				mataderos.setOtros(rs.getString("otros"));

				mataderos.setFechaInstalacion(rs.getDate("fecha_inst"));
				mataderos.setObservaciones(rs.getString("observ"));
				mataderos.setFechaRevision(rs.getDate("fecha_revision"));
				mataderos.setEstadoRevision(rs.getInt("estado_revision"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return mataderos;
	}


	public NucleosAbandonadosEIEL getPanelNucleosAbandonadosEIEL(String codprov, String codmunic) {
		NucleosAbandonadosEIEL nucleosAbandonados = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelNucleosAbandonados");

			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			rs = ps.executeQuery();

			while (rs.next()) {

				nucleosAbandonados = new NucleosAbandonadosEIEL();
				nucleosAbandonados.setCodINEProvincia(rs.getString("codprov"));
				nucleosAbandonados.setCodINEMunicipio(rs.getString("codmunic"));
				nucleosAbandonados.setCodINEEntidad(rs.getString("codentidad"));
				nucleosAbandonados.setCodINEPoblamiento((rs.getString("codpoblamiento")));

				nucleosAbandonados.setAnnoAbandono(rs.getString("a_abandono"));
				nucleosAbandonados.setCausaAbandono(rs.getString("causa_abandono"));

				nucleosAbandonados.setTitularidad(rs.getString("titular_abandono"));
				nucleosAbandonados.setRehabilitacion(rs.getString("rehabilitacion"));
				nucleosAbandonados.setAcceso(rs.getString("acceso"));

				nucleosAbandonados.setServicioAgua(rs.getString("serv_agua"));
				nucleosAbandonados.setServicioElectricidad(rs.getString("serv_elect"));			

				nucleosAbandonados.setObservaciones(rs.getString("observ"));
				nucleosAbandonados.setFechaRevision(rs.getDate("fecha_revision"));
				nucleosAbandonados.setEstadoRevision(rs.getInt("estado_revision"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return nucleosAbandonados;
	}


	public ArrayList getPanelLstNucleosAbandonadosEIEL(String codprov, String codmunic) {

		ArrayList lstNucleosAbandonados = new ArrayList();		
		NucleosAbandonadosEIEL nucleosAbandonados = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("EIELgetPanelNucleosAbandonados");

			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			rs = ps.executeQuery();

			while (rs.next()) {

				nucleosAbandonados = new NucleosAbandonadosEIEL();
				nucleosAbandonados.setCodINEProvincia(rs.getString("codprov"));
				nucleosAbandonados.setCodINEMunicipio(rs.getString("codmunic"));
				nucleosAbandonados.setCodINEEntidad(rs.getString("codentidad"));
				nucleosAbandonados.setCodINEPoblamiento((rs.getString("codpoblamiento")));

				nucleosAbandonados.setAnnoAbandono(rs.getString("a_abandono"));
				nucleosAbandonados.setCausaAbandono(rs.getString("causa_abandono"));

				nucleosAbandonados.setTitularidad(rs.getString("titular_abandono"));
				nucleosAbandonados.setRehabilitacion(rs.getString("rehabilitacion"));
				nucleosAbandonados.setAcceso(rs.getString("acceso"));

				nucleosAbandonados.setServicioAgua(rs.getString("serv_agua"));
				nucleosAbandonados.setServicioElectricidad(rs.getString("serv_elect"));			

				nucleosAbandonados.setObservaciones(rs.getString("observ"));
				nucleosAbandonados.setFechaRevision(rs.getDate("fecha_revision"));
				nucleosAbandonados.setEstadoRevision(rs.getInt("estado_revision"));

				lstNucleosAbandonados.add(nucleosAbandonados);
			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return lstNucleosAbandonados;
	}



	public NucleosPoblacionEIEL getPanelNucleosPoblacionEIEL(String codprov, String codmunic, String entidad, String nucleo ) {
		NucleosPoblacionEIEL nucleosPoblacion = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelNucleosPoblacion");

			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			ps.setString(3, entidad);
			ps.setString(4, nucleo);
			rs = ps.executeQuery();

			while (rs.next()) {

				nucleosPoblacion = new NucleosPoblacionEIEL();
				nucleosPoblacion.setCodINEProvincia(rs.getString("codprov"));
				nucleosPoblacion.setCodINEMunicipio(rs.getString("codmunic"));
				nucleosPoblacion.setCodINEEntidad(rs.getString("codentidad"));
				nucleosPoblacion.setCodINEPoblamiento((rs.getString("codpoblamiento")));

				nucleosPoblacion.setFechaRevision(rs.getDate("fecha_revision"));
				nucleosPoblacion.setEstadoActualizacion(rs.getInt("estado_actualizacion"));
			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return nucleosPoblacion;
	}



	public OtrosServMunicipalesEIEL getPanelOtrosServiciosMunicipalesEIEL(String codprov, String codmunic) {
		OtrosServMunicipalesEIEL otrosServiciosMunicipales = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			ps = conn.prepareStatement("EIELgetPanelOtrosServiciosMunicipales");

			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			rs = ps.executeQuery();

			while (rs.next()) {

				otrosServiciosMunicipales = new OtrosServMunicipalesEIEL();
				otrosServiciosMunicipales.setCodINEProvincia(rs.getString("codprov"));
				otrosServiciosMunicipales.setCodINEMunicipio(rs.getString("codmunic"));

				otrosServiciosMunicipales.setSwInfGeneral(rs.getString("sw_inf_grl"));
				otrosServiciosMunicipales.setSwInfTuristica(rs.getString("sw_inf_tur"));
				otrosServiciosMunicipales.setSwGbElectronico(rs.getString("sw_gb_elec"));

				otrosServiciosMunicipales.setOrdSoterramiento(rs.getString("ord_soterr"));

				otrosServiciosMunicipales.seteEnEolica(rs.getString("en_eolica"));
				otrosServiciosMunicipales.setKwEolica(rs.getInt("kw_eolica"));

				otrosServiciosMunicipales.setEnSolar(rs.getString("en_solar"));
				otrosServiciosMunicipales.setKwSolar(rs.getInt("kw_solar"));

				otrosServiciosMunicipales.setPlMareomotriz(rs.getString("pl_mareo"));
				otrosServiciosMunicipales.setKwMareomotriz(rs.getInt("kw_mareo"));

				otrosServiciosMunicipales.setOtEnergias(rs.getString("ot_energ"));
				otrosServiciosMunicipales.setKwOtEnergias(rs.getInt("kw_ot_energ"));

				otrosServiciosMunicipales.setObservaciones(rs.getString("observ"));
				otrosServiciosMunicipales.setFechaRevision(rs.getDate("fecha_revision"));
				otrosServiciosMunicipales.setEstadoRevision(rs.getInt("estado_revision"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return otrosServiciosMunicipales;
	}


	public PadronMunicipiosEIEL getPanelPadronMunicipiosEIEL(String codprov, String codmunic) {
		PadronMunicipiosEIEL padronMunicipios = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelPadronMunicipios");

			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			rs = ps.executeQuery();

			while (rs.next()) {

				padronMunicipios = new PadronMunicipiosEIEL();
				padronMunicipios.setCodINEProvincia(rs.getString("codprov"));
				padronMunicipios.setCodINEMunicipio(rs.getString("codmunic"));

				padronMunicipios.setHombres_a1(rs.getInt("n_hombres_a1"));
				padronMunicipios.setMujeres_a1(rs.getInt("n_mujeres_a1"));
				padronMunicipios.setTotPobl_a1(rs.getInt("total_poblacion_a1"));

				padronMunicipios.setHombres_a2(rs.getInt("n_hombres_a2"));
				padronMunicipios.setMujeres_a2(rs.getInt("n_mujeres_a2"));
				padronMunicipios.setTotPobl_a2(rs.getInt("total_poblacion_a2"));

				padronMunicipios.setFecha_a1(rs.getInt("fecha_a1"));
				padronMunicipios.setFecha_a2(rs.getInt("fecha_a2"));

				padronMunicipios.setFechaActualizacion(rs.getDate("fecha_revision"));
				padronMunicipios.setObservaciones(rs.getString("observ"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return padronMunicipios;
	}



	public PadronNucleosEIEL getPanelPadronNucleosEIEL(String codprov, String codmunic, String entidad, String nucleo) {
		PadronNucleosEIEL padronNucleos = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelPadronNucleos");

			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			ps.setString(3, entidad);
			ps.setString(4, nucleo);
			rs = ps.executeQuery();

			while (rs.next()) {

				padronNucleos = new PadronNucleosEIEL();
				padronNucleos.setCodINEProvincia(rs.getString("codprov"));
				padronNucleos.setCodINEMunicipio(rs.getString("codmunic"));
				padronNucleos.setCodINEEntidad(rs.getString("codentidad"));
				padronNucleos.setCodINEPoblamiento((rs.getString("codpoblamiento")));

				padronNucleos.setHombres_a1(rs.getInt("n_hombres_a1"));
				padronNucleos.setMujeres_a1(rs.getInt("n_mujeres_a1"));
				padronNucleos.setTotPobl_a1(rs.getInt("total_poblacion_a1"));

				padronNucleos.setHombres_a2(rs.getInt("n_hombre_a2"));
				padronNucleos.setMujeres_a2(rs.getInt("n_mujeres_a2"));
				padronNucleos.setTotPobl_a2(rs.getInt("total_poblacion_a2"));

				padronNucleos.setFecha_a1(rs.getInt("fecha_a1"));
				padronNucleos.setFecha_a2(rs.getInt("fecha_a2"));

				padronNucleos.setFechaRevision(rs.getDate("fecha_revision"));
				padronNucleos.setObservaciones(rs.getString("observ"));
			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return padronNucleos;
	}



	public ParquesJardinesEIEL getPanelParquesJardinesEIEL(String clave, String codprov, String codmunic, String entidad, String nucleo, String orden ) {
		ParquesJardinesEIEL parquesjardines = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelParquesJardines");

			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, entidad);
			ps.setString(5, nucleo);
			ps.setString(6, orden);
			rs = ps.executeQuery();

			while (rs.next()) {

				parquesjardines = new ParquesJardinesEIEL();
				parquesjardines.setClave(rs.getString("clave"));
				parquesjardines.setCodINEProvincia(rs.getString("codprov"));
				parquesjardines.setCodINEMunicipio(rs.getString("codmunic"));
				parquesjardines.setCodINEEntidad(rs.getString("codentidad"));
				parquesjardines.setCodINEPoblamiento((rs.getString("codpoblamiento")));
				parquesjardines.setCodOrden(rs.getString("orden_pj"));

				parquesjardines.setNombre(rs.getString("nombre"));
				parquesjardines.setTipo(rs.getString("tipo"));

				parquesjardines.setTitularidad(rs.getString("titular"));
				parquesjardines.setGestion(rs.getString("gestor"));

				parquesjardines.setSupCubierta(rs.getInt("s_cubierta"));
				parquesjardines.setSupLibre(rs.getInt("s_aire"));
				parquesjardines.setSupSolar(rs.getInt("s_solar"));

				parquesjardines.setAgua(rs.getString("agua"));
				parquesjardines.setSaneamiento(rs.getString("saneamiento"));
				parquesjardines.setElectricidad(rs.getString("electricidad"));
				parquesjardines.setComedor(rs.getString("comedor"));
				parquesjardines.setJuegosInf(rs.getString("juegos_inf"));
				parquesjardines.setOtros(rs.getString("otras"));


				parquesjardines.setEstado(rs.getString("estado"));
				parquesjardines.setObservaciones(rs.getString("observ"));
				parquesjardines.setFechaRevision(rs.getDate("fecha_revision"));
				parquesjardines.setEstadoRevision(rs.getInt("estado_revision"));
			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return parquesjardines;
	}


	public PlaneamientoUrbanoEIEL getPanelPlaneamientoUrbanosEIEL(String codprov, String codmunic) {
		PlaneamientoUrbanoEIEL planeamientoUrbano = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelPlaneamientoUrbano");

			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			rs = ps.executeQuery();

			while (rs.next()) {

				planeamientoUrbano = new PlaneamientoUrbanoEIEL();
				planeamientoUrbano.setCodINEProvincia(rs.getString("codprov"));
				planeamientoUrbano.setCodINEMunicipio(rs.getString("codmunic"));

				planeamientoUrbano.setTipo(rs.getString("tipo_urba"));
				planeamientoUrbano.setEstado(rs.getString("estado_tramit"));

				planeamientoUrbano.setDenominacion(rs.getString("denominacion"));
				planeamientoUrbano.setSupMunicipal(rs.getFloat("sup_muni"));
				planeamientoUrbano.setFechaPublicacion(rs.getDate("fecha_bo"));

				planeamientoUrbano.setSupUrbano(rs.getFloat("s_urbano"));
				planeamientoUrbano.setSupUrbanizable(rs.getFloat("s_urbanizable"));
				planeamientoUrbano.setSupNoUrbanizable(rs.getFloat("s_no_urbanizable"));
				planeamientoUrbano.setSupNoUrbanizableEsp(rs.getFloat("s_no_urban_especial"));

				planeamientoUrbano.setObservaciones(rs.getString("observ"));
				planeamientoUrbano.setFechaRevision(rs.getDate("fecha_revision"));
				planeamientoUrbano.setEstadoRevision(rs.getInt("estado_revision"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return planeamientoUrbano;
	}


	public PoblamientoEIEL getPanelPoblamientoEIEL(String codprov, String codmunic, String entidad, String nucleo ) {
		PoblamientoEIEL poblamiento = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			ps = conn.prepareStatement("EIELgetPanelPoblamiento");

			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			ps.setString(3, entidad);
			ps.setString(4, nucleo);
			rs = ps.executeQuery();

			while (rs.next()) {

				poblamiento = new PoblamientoEIEL();
				poblamiento.setCodINEProvincia(rs.getString("codprov"));
				poblamiento.setCodINEMunicipio(rs.getString("codmunic"));
				poblamiento.setCodINEEntidad(rs.getString("codentidad"));
				poblamiento.setCodINEPoblamiento((rs.getString("codpoblamiento")));
				poblamiento.setObservaciones(rs.getString("observ"));
				poblamiento.setFechaActualizacion(rs.getDate("fecha_revision"));
			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return poblamiento;
	}


	public Encuestados1EIEL getPanelNucleosEncuestados1EIEL(String codprov, String codmunic, String entidad, String nucleo ) {
		Encuestados1EIEL encuestados1 = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			ps = conn.prepareStatement("EIELgetPanelNucleosEncuestados1");

			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			ps.setString(3, entidad);
			ps.setString(4, nucleo);
			rs = ps.executeQuery();

			while (rs.next()) {

				encuestados1 = new Encuestados1EIEL();
				encuestados1.setCodINEProvincia(rs.getString("codprov"));
				encuestados1.setCodINEMunicipio(rs.getString("codmunic"));
				encuestados1.setCodINEEntidad(rs.getString("codentidad"));
				encuestados1.setCodINEPoblamiento((rs.getString("codpoblamiento")));

				encuestados1.setPadron(rs.getInt("padron"));
				encuestados1.setPoblacionEstacional(rs.getInt("pob_estacional"));
				encuestados1.setAltitud(rs.getInt("altitud"));
				encuestados1.setViviendasTotales(rs.getInt("viviendas_total"));
				encuestados1.setPlazasHoteleras(rs.getInt("hoteles"));
				encuestados1.setPlazasCasasRurales(rs.getInt("casas_rural"));
				encuestados1.setAccesibilidad(rs.getString("accesibilidad"));				

				encuestados1.setObservaciones(rs.getString("observ"));
				encuestados1.setFechaRevision(rs.getDate("fecha_revision"));
				encuestados1.setEstadoRevision(rs.getInt("estado_revision"));
			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return encuestados1;
	}



	public Encuestados2EIEL getPanelNucleosEncuestados2EIEL(String codprov, String codmunic, String entidad, String nucleo ) {
		Encuestados2EIEL encuestados2 = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			ps = conn.prepareStatement("EIELgetPanelNucleosEncuestados2");

			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			ps.setString(3, entidad);
			ps.setString(4, nucleo);
			rs = ps.executeQuery();

			while (rs.next()) {

				encuestados2 = new Encuestados2EIEL();
				encuestados2.setCodINEProvincia(rs.getString("codprov"));
				encuestados2.setCodINEMunicipio(rs.getString("codmunic"));
				encuestados2.setCodINEEntidad(rs.getString("codentidad"));
				encuestados2.setCodINEPoblamiento(rs.getString("codpoblamiento"));

				encuestados2.setDisponibilidadCaudal(rs.getString("aag_caudal"));
				encuestados2.setRestriccionesAgua(rs.getString("aag_restri"));
				encuestados2.setContadores(rs.getString("aag_contad"));
				encuestados2.setTasa(rs.getString("aag_tasa"));
				encuestados2.setAnnoInstalacion(rs.getString("aag_instal"));
				encuestados2.setHidrantes(rs.getString("aag_hidran"));
				encuestados2.setEstadoHidrantes(rs.getString("aag_est_hi"));
				encuestados2.setValvulas(rs.getString("aag_valvul"));
				encuestados2.setEstadoValvulas(rs.getString("aag_est_va"));
				encuestados2.setBocasRiego(rs.getString("aag_bocasr"));
				encuestados2.setEstadoBocasRiego(rs.getString("aag_est_bo"));
				encuestados2.setCisterna(rs.getString("cisterna"));

				encuestados2.setObservaciones(rs.getString("observ"));
				encuestados2.setFechaRevision(rs.getDate("fecha_revision"));
				encuestados2.setEstadoRevision(rs.getInt("estado_revision"));
			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return encuestados2;
	}


	public PuntosVertidoEIEL getPanelPuntosVertidoEIEL(String clave, String codprov, String codmunic, String orden ) {
		PuntosVertidoEIEL puntosVertido = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			ps = conn.prepareStatement("EIELgetPanelPuntosVertido");

			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, orden);
			rs = ps.executeQuery();

			while (rs.next()) {

				puntosVertido = new PuntosVertidoEIEL();
				puntosVertido.setClave(rs.getString("clave"));
				puntosVertido.setCodINEProvincia(rs.getString("codprov"));
				puntosVertido.setCodINEMunicipio(rs.getString("codmunic"));
				puntosVertido.setOrden(rs.getString("orden_pv"));				

				puntosVertido.setTipo(rs.getString("tipo"));
				puntosVertido.setZona(rs.getString("zona"));
				puntosVertido.setDistanciaNucleo(rs.getInt("distancia_nucleo"));
				puntosVertido.setFechaInicio(rs.getDate("fecha_ini_vertido"));

				puntosVertido.setObservaciones(rs.getString("observ"));
				puntosVertido.setFechaRevision(rs.getDate("fecha_revision"));
				puntosVertido.setEstadoRevision(rs.getInt("estado_revision"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return puntosVertido;
	}


	public RecogidaBasurasEIEL getPanelRecogidaBasurasEIEL(String codprov, String codmunic, String entidad, String nucleo ) {
		RecogidaBasurasEIEL recogidaBasuras = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			ps = conn.prepareStatement("EIELgetPanelRecogidaBasuras");
			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			ps.setString(3, entidad);
			ps.setString(4, nucleo);
			rs = ps.executeQuery();

			while (rs.next()) {

				recogidaBasuras = new RecogidaBasurasEIEL();
				recogidaBasuras.setClave(rs.getString("clave"));
				recogidaBasuras.setCodINEProvincia(rs.getString("codprov"));
				recogidaBasuras.setCodINEMunicipio(rs.getString("codmunic"));
				recogidaBasuras.setCodINEEntidad(rs.getString("codentidad"));
				recogidaBasuras.setCodINEPoblamiento((rs.getString("codpoblamiento")));

				recogidaBasuras.setTipo(rs.getString("tipo"));
				recogidaBasuras.setGestion(rs.getString("gestor"));

				recogidaBasuras.setPeriodicidad(rs.getString("periodicidad"));
				recogidaBasuras.setCalidad(rs.getString("calidad"));
				recogidaBasuras.setTonProducidas(rs.getFloat("tm_res_urb"));
				recogidaBasuras.setNumContenedores(rs.getInt("n_contenedores"));

				recogidaBasuras.setFecharevision(rs.getDate("fecha_revision"));
				recogidaBasuras.setObservaciones(rs.getString("observ"));
				recogidaBasuras.setEstadoRevision(rs.getInt("estado_revision"));
			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return recogidaBasuras;
	}


	public SaneamientoAutonomoEIEL getPanelSaneamientoAutonomoEIEL(String codprov, String codmunic, String entidad, String nucleo ) {
		SaneamientoAutonomoEIEL saneamientoAutonomo = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelSaneamientoAutonomo");

			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			ps.setString(3, entidad);
			ps.setString(4, nucleo);
			rs = ps.executeQuery();

			while (rs.next()) {

				saneamientoAutonomo = new SaneamientoAutonomoEIEL();
				saneamientoAutonomo.setClave(rs.getString("clave"));
				saneamientoAutonomo.setCodINEProvincia(rs.getString("codprov"));
				saneamientoAutonomo.setCodINEMunicipio(rs.getString("codmunic"));
				saneamientoAutonomo.setCodINEEntidad(rs.getString("codentidad"));
				saneamientoAutonomo.setCodINEPoblamiento((rs.getString("codpoblamiento")));

				saneamientoAutonomo.setTipo(rs.getString("tipo_sau"));
				saneamientoAutonomo.setEstado(rs.getString("estado_sau"));

				saneamientoAutonomo.setAdecuacion(rs.getString("adecuacion_sau"));

				saneamientoAutonomo.setViviendas(rs.getInt("sau_vivien"));
				saneamientoAutonomo.setPoblResidente(rs.getInt("sau_pob_re"));
				saneamientoAutonomo.setPoblEstacional(rs.getInt("sau_pob_es"));

				saneamientoAutonomo.setVivDeficitarias(rs.getInt("sau_vi_def"));
				saneamientoAutonomo.setPoblResDeficitaria(rs.getInt("sau_pob_re_def"));
				saneamientoAutonomo.setPoblEstDeficitaria(rs.getInt("sau_pob_es_def"));

				saneamientoAutonomo.setFechaInstalacion(rs.getDate("fecha_inst"));
				saneamientoAutonomo.setObservaciones(rs.getString("observ"));
				saneamientoAutonomo.setFechaRevision(rs.getDate("fecha_revision"));
				saneamientoAutonomo.setEstadoRevision(rs.getInt("estado_revision"));
			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return saneamientoAutonomo;
	}



	public ServiciosAbastecimientosEIEL getPanelServiciosAbastecimientoEIEL(String codprov, String codmunic, String entidad, String nucleo ) {
		ServiciosAbastecimientosEIEL serviciosAbastecimientos = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			ps = conn.prepareStatement("EIELgetPanelServiciosAbastecimiento");

			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			ps.setString(3, entidad);
			ps.setString(4, nucleo);
			rs = ps.executeQuery();

			while (rs.next()) {

				serviciosAbastecimientos = new ServiciosAbastecimientosEIEL();
				serviciosAbastecimientos.setCodINEProvincia(rs.getString("codprov"));
				serviciosAbastecimientos.setCodINEMunicipio(rs.getString("codmunic"));
				serviciosAbastecimientos.setCodINEEntidad(rs.getString("codentidad"));
				serviciosAbastecimientos.setCodINEPoblamiento((rs.getString("codpoblamiento")));

				serviciosAbastecimientos.setViviendasConectadas(rs.getInt("viviendas_c_conex"));
				serviciosAbastecimientos.setViviendasNoConectadas(rs.getInt("viviendas_s_conexion"));

				serviciosAbastecimientos.setConsumoInvierno(rs.getInt("consumo_inv"));
				serviciosAbastecimientos.setConsumoVerano(rs.getInt("consumo_verano"));

				serviciosAbastecimientos.setViviendasExcesoPresion(rs.getInt("viv_exceso_pres"));
				serviciosAbastecimientos.setViviendasDeficitPresion(rs.getInt("viv_defic_presion"));

				serviciosAbastecimientos.setPerdidasAgua(rs.getInt("perdidas_agua"));
				serviciosAbastecimientos.setCalidadServicio(rs.getString("calidad_serv"));
				serviciosAbastecimientos.setLongitudDeficitaria(rs.getInt("viv_deficitarias"));
				serviciosAbastecimientos.setPoblacionResidenteDeficitaria(rs.getInt("pobl_res_afect"));
				serviciosAbastecimientos.setPoblacionEstacionalDeficitaria(rs.getInt("pobl_est_afect"));

				serviciosAbastecimientos.setObservaciones(rs.getString("observ"));
				serviciosAbastecimientos.setFechaRevision(rs.getDate("fecha_revision"));
				serviciosAbastecimientos.setEstadoRevision(rs.getInt("estado_revision"));
			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return serviciosAbastecimientos;
	}



	public ServiciosRecogidaBasuraEIEL getPanelServiciosRecogidaBasuraEIEL(String codprov, String codmunic, String entidad, String nucleo ) {
		ServiciosRecogidaBasuraEIEL serviciosRecogidaBasuras = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			ps = conn.prepareStatement("EIELgetPanelServiciosRecogidaBasura");

			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			ps.setString(3, entidad);
			ps.setString(4, nucleo);
			rs = ps.executeQuery();

			while (rs.next()) {

				serviciosRecogidaBasuras = new ServiciosRecogidaBasuraEIEL();
				serviciosRecogidaBasuras.setCodINEProvincia(rs.getString("codprov"));
				serviciosRecogidaBasuras.setCodINEMunicipio(rs.getString("codmunic"));
				serviciosRecogidaBasuras.setCodINEEntidad(rs.getString("codentidad"));
				serviciosRecogidaBasuras.setCodINEPoblamiento((rs.getString("codpoblamiento")));

				serviciosRecogidaBasuras.setVivSinServicio(rs.getInt("srb_viviendas_afec"));
				serviciosRecogidaBasuras.setPoblResSinServicio(rs.getInt("srb_pob_res_afect"));
				serviciosRecogidaBasuras.setPoblEstSinServicio(rs.getInt("srb_pob_est_afect"));

				serviciosRecogidaBasuras.setServLimpCalles(rs.getString("serv_limp_calles"));
				serviciosRecogidaBasuras.setPlantilla(rs.getInt("plantilla_serv_limp"));

				serviciosRecogidaBasuras.setObservaciones(rs.getString("observ"));
				serviciosRecogidaBasuras.setFechaRevision(rs.getDate("fecha_revision"));
				serviciosRecogidaBasuras.setEstadoRevision(rs.getInt("estado_revision"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return serviciosRecogidaBasuras;
	}


	public ServiciosSaneamientoEIEL getPanelServiciosSaneamientoEIEL(String codprov, String codmunic, String entidad, String nucleo ) {
		ServiciosSaneamientoEIEL serviciosSaneamiento = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("EIELgetPanelServiciosSaneamiento");

			ps.setString(1, codprov);
			ps.setString(2, codmunic);
			ps.setString(3, entidad);
			ps.setString(4, nucleo);
			rs = ps.executeQuery();

			while (rs.next()) {

				serviciosSaneamiento = new ServiciosSaneamientoEIEL();
				serviciosSaneamiento.setCodINEProvincia(rs.getString("codprov"));
				serviciosSaneamiento.setCodINEMunicipio(rs.getString("codmunic"));
				serviciosSaneamiento.setCodINEEntidad(rs.getString("codentidad"));
				serviciosSaneamiento.setCodINEPoblamiento((rs.getString("codpoblamiento")));

				serviciosSaneamiento.setPozos(rs.getString("pozos_registro"));
				serviciosSaneamiento.setSumideros(rs.getString("sumideros"));

				serviciosSaneamiento.setAlivAcumulacion(rs.getString("aliv_c_acum"));
				serviciosSaneamiento.setAlivSinAcumulacion(rs.getString("aliv_s_acum"));

				serviciosSaneamiento.setCalidad(rs.getString("calidad_serv"));

				serviciosSaneamiento.setVivConectadas(rs.getInt("viviendas_c_conex"));
				serviciosSaneamiento.setVivNoConectadas(rs.getInt("viviendas_s_conex"));
				serviciosSaneamiento.setLongDeficitaria(rs.getInt("long_rs_deficit"));
				serviciosSaneamiento.setVivDeficitarias(rs.getInt("viviendas_def_conex"));
				serviciosSaneamiento.setPoblResDeficitaria(rs.getInt("pobl_res_def_afect"));
				serviciosSaneamiento.setPoblEstDeficitaria(rs.getInt("pobl_est_def_afect"));

				serviciosSaneamiento.setCaudalTotal(rs.getInt("caudal_total"));
				serviciosSaneamiento.setCaudalTratado(rs.getInt("caudal_tratado"));
				serviciosSaneamiento.setCaudalUrbano(rs.getInt("c_reutilizado_urb"));
				serviciosSaneamiento.setCaudalRustico(rs.getInt("c_reutilizado_rust"));
				serviciosSaneamiento.setCaudalIndustrial(rs.getInt("c_reutilizado_ind"));

				serviciosSaneamiento.setObservaciones(rs.getString("observ"));
				serviciosSaneamiento.setFechaRevision(rs.getDate("fecha_revision"));
				serviciosSaneamiento.setEstadoRevision(rs.getInt("estado_revision"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return serviciosSaneamiento;
	}



	public TanatoriosEIEL getPanelTanatoriosEIEL(String clave, String codprov, String codmunic,String entidad, String nucleo, String orden ) {
		TanatoriosEIEL tanatorios = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("EIELgetPanelTanatorios");

			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, entidad);
			ps.setString(5, nucleo);
			ps.setString(6, orden);
			rs = ps.executeQuery();

			while (rs.next()) {

				tanatorios = new TanatoriosEIEL();
				tanatorios.setClave(rs.getString("clave"));
				tanatorios.setCodINEProvincia(rs.getString("codprov"));
				tanatorios.setCodINEMunicipio(rs.getString("codmunic"));
				tanatorios.setCodINEEntidad(rs.getString("codentidad"));
				tanatorios.setCodINEPoblamiento((rs.getString("codpoblamiento")));
				tanatorios.setCodOrden(rs.getString("orden_ta"));

				tanatorios.setNombre(rs.getString("nombre"));
				tanatorios.setTitularidad(rs.getString("titular"));
				tanatorios.setGestion(rs.getString("gestor"));

				tanatorios.setSupCubierta(rs.getInt("s_cubierta"));
				tanatorios.setSupLibre(rs.getInt("s_aire"));
				tanatorios.setSupSolar(rs.getInt("s_solar"));

				tanatorios.setSalas(rs.getInt("salas"));

				tanatorios.setEstado(rs.getString("estado"));
				tanatorios.setFechaInstalacion(rs.getDate("fecha_inst"));
				tanatorios.setObservaciones(rs.getString("observ"));
				tanatorios.setFechaRevision(rs.getDate("fecha_revision"));
				tanatorios.setEstadoRevision(rs.getInt("estado_revision"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return tanatorios;
	}



	public VertederosEIEL getPanelVertederosEIEL(String clave, String codprov, String codmunic, String orden ) {
		VertederosEIEL vertederos = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelVertederos");

			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, orden);
			rs = ps.executeQuery();

			while (rs.next()) {

				vertederos = new VertederosEIEL();
				vertederos.setClave(rs.getString("clave"));
				vertederos.setCodINEProvincia(rs.getString("codprov"));
				vertederos.setCodINEMunicipio(rs.getString("codmunic"));
				vertederos.setCodOrden(rs.getString("orden_vt"));

				vertederos.setTipo(rs.getString("tipo"));

				vertederos.setTitularidad(rs.getString("titular"));
				vertederos.setGestion(rs.getString("gestor"));

				vertederos.setOlores(rs.getString("olores"));
				vertederos.setHumos(rs.getString("humos"));
				vertederos.setContAnimal(rs.getString("cont_anima"));
				vertederos.setRsgoInundacion(rs.getString("r_inun"));
				vertederos.setFiltraciones(rs.getString("filtracion"));
				vertederos.setImptVisual(rs.getString("impacto_v"));
				vertederos.setFrecAverias(rs.getString("frec_averia"));
				vertederos.setSaturacion(rs.getString("saturacion"));
				vertederos.setInestabilidad(rs.getString("inestable"));
				vertederos.setOtros(rs.getString("otros"));

				vertederos.setCapTotal(rs.getInt("capac_tot"));
				vertederos.setCapOcupada(rs.getInt("capac_tot_porc"));
				vertederos.setCapTransform(rs.getInt("capac_transf"));

				vertederos.setEstado(rs.getString("estado"));
				vertederos.setVidaUtil(rs.getInt("vida_util"));
				vertederos.setCategoria(rs.getString("categoria"));
				vertederos.setActividad(rs.getString("actividad"));
				vertederos.setFechaApertura(rs.getInt("fecha_apertura"));
				vertederos.setObservaciones(rs.getString("observ"));
				vertederos.setPosbAmpliacion(rs.getString("capac_ampl"));
				vertederos.setFechaRevision(rs.getDate("fecha_revision"));
				vertederos.setEstadoRevision(rs.getInt("estado_revision"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return vertederos;
	}


	public IncendiosProteccionEIEL getPanelCentrosIncendiosEIEL (String clave, String codprov, String codmunic,String entidad, String nucleo, String orden ) {
		IncendiosProteccionEIEL centrosIncendios = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetCentrosIncendios");

			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, entidad);
			ps.setString(5, nucleo);
			ps.setString(6, orden);
			rs = ps.executeQuery();

			while (rs.next()) {

				centrosIncendios = new IncendiosProteccionEIEL();
				centrosIncendios.setClave(rs.getString("clave"));
				centrosIncendios.setCodINEProvincia(rs.getString("codprov"));
				centrosIncendios.setCodINEMunicipio(rs.getString("codmunic"));
				centrosIncendios.setCodINEEntidad(rs.getString("codentidad"));
				centrosIncendios.setCodINEPoblamiento((rs.getString("codpoblamiento")));
				centrosIncendios.setOrden(rs.getString("orden_ip"));


				centrosIncendios.setNombre(rs.getString("nombre"));
				centrosIncendios.setTipo(rs.getString("tipo"));
				centrosIncendios.setTitular(rs.getString("titular"));
				centrosIncendios.setGestor(rs.getString("gestor"));
				centrosIncendios.setAmbito(rs.getString("ambito"));

				centrosIncendios.setSuperficieCubierta(rs.getInt("s_cubierta"));
				centrosIncendios.setSuperficieAireLibre(rs.getInt("s_aire"));
				centrosIncendios.setSuperficieSolar(rs.getInt("s_solar"));

				centrosIncendios.setPlantillaProfesionales(rs.getInt("plan_profe"));
				centrosIncendios.setPlantillaVoluntarios(rs.getInt("plan_volun"));

				centrosIncendios.setEstado(rs.getString("estado"));

				centrosIncendios.setVechiculosIncendios(rs.getInt("vehic_incendio"));
				centrosIncendios.setVechiculosRescate(rs.getInt("vehic_rescate"));
				centrosIncendios.setAmbulancias(rs.getInt("ambulancia"));
				centrosIncendios.setMediosAereos(rs.getInt("medios_aereos"));
				centrosIncendios.setOtrosVehiculos(rs.getInt("otros_vehic"));
				centrosIncendios.setQuitanieves(rs.getInt("quitanieve"));
				centrosIncendios.setSistemasDeteccionIncencios(rs.getInt("detec_ince"));
				centrosIncendios.setOtros(rs.getInt("otros"));

				centrosIncendios.setFechaInstalacion(rs.getDate("fecha_inst"));
				centrosIncendios.setObservaciones(rs.getString("observ"));
				centrosIncendios.setFechaRevision(rs.getDate("fecha_revision"));
				centrosIncendios.setEstadoRevision(rs.getInt("estado_revision"));
			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return centrosIncendios;
	}
	public TramosConduccionEIEL getPanelTramoConduccionEIEL(String clave, String codprov, String codmunic, String tramo_cn ) {
		TramosConduccionEIEL conduccion = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			ps = conn.prepareStatement("EIELgetPanelTramoConduccion");
			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, tramo_cn);
			rs = ps.executeQuery();
			while (rs.next()) {
				conduccion = new TramosConduccionEIEL();
				conduccion.setClave(rs.getString("clave"));
				conduccion.setCodINEProvincia(rs.getString("codprov"));
				conduccion.setCodINEMunicipio(rs.getString("codmunic"));
				conduccion.setTramo_cn(rs.getString("orden_ca"));
				conduccion.setTitular(rs.getString("titular"));
				conduccion.setGestor(rs.getString("gestor"));
				conduccion.setSist_trans(rs.getString("sist_impulsion"));
				conduccion.setEstado(rs.getString("estado"));
				conduccion.setMaterial(rs.getString("material"));
				conduccion.setFechaInstalacion(rs.getDate("fecha_inst"));
				conduccion.setObservaciones(rs.getString("observ"));
				conduccion.setFecha_revision(rs.getDate("fecha_revision"));
				conduccion.setEstado_revision(rs.getInt("estado_revision"));
				conduccion.setBloqueado(rs.getString("bloqueado"));
			}

		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return conduccion;
	}
	public ColectorEIEL getPanelColectorEIEL(String clave, String codprov, String codmunic, String tramo_cl ) {
		ColectorEIEL colector = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement("EIELgetPanelColector");
			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, tramo_cl);
			rs = ps.executeQuery();
			while (rs.next()) {
				colector = new ColectorEIEL();
				colector.setClave(rs.getString("clave"));
				colector.setCodINEProvincia(rs.getString("codprov"));
				colector.setCodINEMunicipio(rs.getString("codmunic"));
				colector.setCodOrden(rs.getString("tramo_cl"));
				colector.setTitularidad(rs.getString("titular"));
				colector.setGestion(rs.getString("gestor"));
				colector.setSist_impulsion(rs.getString("sist_impulsion"));
				colector.setEstado(rs.getString("estado"));
				colector.setMaterial(rs.getString("material"));
				colector.setTipo_red(rs.getString("tipo_red_interior"));
				colector.setTip_interceptor(rs.getString("tip_interceptor"));
				colector.setFecha_inst(rs.getDate("fecha_inst"));
				colector.setObservaciones(rs.getString("observ"));
				colector.setFechaRevision(rs.getDate("fecha_revision"));
				colector.setEstado_Revision(rs.getInt("estado_revision"));
				colector.setBloqueado(rs.getString("bloqueado"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return colector;
	}
	public EmisariosEIEL getPanelEmisariosEIEL(String clave, String codprov, String codmunic, String orden_em ) {
		EmisariosEIEL conduccion = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("EIELgetPanelEmisarios");
			ps.setString(1, clave);
			ps.setString(2, codprov);
			ps.setString(3, codmunic);
			ps.setString(4, orden_em);
			rs = ps.executeQuery();
			while (rs.next()) {
				conduccion = new EmisariosEIEL();
				conduccion.setClave(rs.getString("clave"));
				conduccion.setCodINEProvincia(rs.getString("codprov"));
				conduccion.setCodINEMunicipio(rs.getString("codmunic"));
				conduccion.setCodOrden(rs.getString("tramo_em"));
				conduccion.setTitularidad(rs.getString("titular"));
				conduccion.setGestion(rs.getString("gestor"));
				conduccion.setSistema(rs.getString("sist_impulsion"));
				conduccion.setEstado(rs.getString("estado"));
				conduccion.setMaterial(rs.getString("material"));
				conduccion.setFecha_inst(rs.getDate("fecha_inst"));
				conduccion.setObservaciones(rs.getString("observ"));
				conduccion.setFechaRevision(rs.getDate("fecha_revision"));
				conduccion.setEstado_Revision(rs.getInt("estado_revision"));
				conduccion.setBloqueado(rs.getString("bloqueado"));
			}
		} catch (SQLException ex) {
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return conduccion;
	}


	public void saveNucleoEmisario(NucleoEmisario nucleoEmisario) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{       
			boolean actualizar = false;

			if (nucleoEmisario != null){
				

				ps = conn.prepareStatement("EIELgetNucleosEmisario");
				ps.setString(1, nucleoEmisario.getClaveEmisario());
				ps.setString(2, nucleoEmisario.getCodProvEmisario());
				ps.setString(3, nucleoEmisario.getCodMunicEmisario());
				ps.setString(4, nucleoEmisario.getCodTramoEmisario());
				ps.setString(5, nucleoEmisario.getCodProvNucleo());
				ps.setString(6, nucleoEmisario.getCodMunicNucleo());
				ps.setString(7, nucleoEmisario.getCodEntNucleo());
				ps.setString(8, nucleoEmisario.getCodPoblNucleo());
				rs = ps.executeQuery(); 

				if (rs.next()){
					actualizar = true;
					ps = conn.prepareStatement("EIELupdateNucleosEmisario");
				}
				else{
					actualizar = false;
					ps = conn.prepareStatement("EIELinsertNucleosEmisario");
				}

				ps.setString(1, nucleoEmisario.getClaveEmisario());
				ps.setString(2, nucleoEmisario.getCodProvEmisario());
				ps.setString(3, nucleoEmisario.getCodMunicEmisario());
				ps.setString(4, nucleoEmisario.getCodTramoEmisario());

				ps.setString(5, nucleoEmisario.getCodProvNucleo());
				ps.setString(6, nucleoEmisario.getCodMunicNucleo());
				ps.setString(7, nucleoEmisario.getCodEntNucleo());
				ps.setString(8, nucleoEmisario.getCodPoblNucleo());
				
				if(nucleoEmisario.getPmi()!=null && !nucleoEmisario.getPmi().equals(""))
					ps.setFloat(9, nucleoEmisario.getPmi());
				else
					ps.setNull(9, java.sql.Types.FLOAT);
				if(nucleoEmisario.getPmi()!=null && !nucleoEmisario.getPmf().equals(""))
					ps.setFloat(10, nucleoEmisario.getPmf());
				else
					ps.setNull(10, java.sql.Types.FLOAT);
				
				ps.setString(11, nucleoEmisario.getObservaciones());
				ps.setDate(12, nucleoEmisario.getFechaRevision());
				if(nucleoEmisario.getEstadoRevision()!=null)
					ps.setInt(13, nucleoEmisario.getEstadoRevision().intValue());
				else
					ps.setNull(13, java.sql.Types.INTEGER);
				if (actualizar){
					ps.setString(14, nucleoEmisario.getClaveEmisario());
					ps.setString(15, nucleoEmisario.getCodProvEmisario());
					ps.setString(16, nucleoEmisario.getCodMunicEmisario());
					ps.setString(17, nucleoEmisario.getCodTramoEmisario());
					ps.setString(18, nucleoEmisario.getCodProvNucleo());
					ps.setString(19, nucleoEmisario.getCodMunicNucleo());
					ps.setString(20, nucleoEmisario.getCodEntNucleo());
					ps.setString(21, nucleoEmisario.getCodPoblNucleo());
				}
				ps.execute();

			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

	}


	public void deleteNucleoEmisario(NucleoEmisario nucleoEmisario) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{     	
			if (nucleoEmisario != null){
	
				ps = conn.prepareStatement("EIELgetNucleosEmisario");
				ps.setString(1, nucleoEmisario.getClaveEmisario());
				ps.setString(2, nucleoEmisario.getCodProvEmisario());
				ps.setString(3, nucleoEmisario.getCodMunicEmisario());
				ps.setString(4, nucleoEmisario.getCodTramoEmisario());
				ps.setString(5, nucleoEmisario.getCodProvNucleo());
				ps.setString(6, nucleoEmisario.getCodMunicNucleo());
				ps.setString(7, nucleoEmisario.getCodEntNucleo());
				ps.setString(8, nucleoEmisario.getCodPoblNucleo());
				rs = ps.executeQuery(); 
	
				if (rs.next()){
	
					ps = conn.prepareStatement("EIELdeleteNucleosEmisario");
	
					ps.setString(1, nucleoEmisario.getClaveEmisario());
					ps.setString(2, nucleoEmisario.getCodProvEmisario());
					ps.setString(3, nucleoEmisario.getCodMunicEmisario());
					ps.setString(4, nucleoEmisario.getCodTramoEmisario());
					ps.setString(5, nucleoEmisario.getCodProvNucleo());
					ps.setString(6, nucleoEmisario.getCodMunicNucleo());
					ps.setString(7, nucleoEmisario.getCodEntNucleo());
					ps.setString(8, nucleoEmisario.getCodPoblNucleo());
	
					ps.execute();
				}   
	
				if (ps!=null) ps.close();
				if (rs!= null) rs.close(); 
				conn.close();
	
			}
		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}
	}
	
	public ArrayList<NucleoEmisario> getLstNucleosEmisario(String clave, String codigoProvincia, String codigoMunicipio, String codigoOrden){

		ArrayList<NucleoEmisario> lstNucleos = new ArrayList<NucleoEmisario>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		
		if (clave == null){
			clave = "";
		}
		if (codigoProvincia == null){
			codigoProvincia = "";
		}
		if (codigoMunicipio == null){
			codigoMunicipio = "";
		}
		if (codigoOrden == null){
			codigoOrden = "";
		}

		try {
			ps = conn.prepareStatement("EIELgetListaNucleosEmisario");
			ps.setString(1, clave);
			ps.setString(2, codigoProvincia);
			ps.setString(3, codigoMunicipio);
			ps.setString(4, codigoOrden);
			rs = ps.executeQuery(); 
	
			while (rs.next())
			{
				NucleoEmisario nucleoEmisario = new NucleoEmisario();
				nucleoEmisario.setClaveEmisario(rs.getString("clave_tem"));
				nucleoEmisario.setCodProvEmisario(rs.getString("codprov_tem"));
				nucleoEmisario.setCodMunicEmisario(rs.getString("codmunic_tem"));
				nucleoEmisario.setCodTramoEmisario(rs.getString("tramo_em"));
				nucleoEmisario.setCodProvNucleo(rs.getString("codprov_pobl"));
				nucleoEmisario.setCodMunicNucleo(rs.getString("codmunic_pobl"));
				nucleoEmisario.setCodEntNucleo(rs.getString("codentidad_pobl"));
				nucleoEmisario.setCodPoblNucleo(rs.getString("codpoblamiento_pobl"));
				nucleoEmisario.setObservaciones(rs.getString("observ"));
				nucleoEmisario.setFechaRevision(rs.getDate("fecha_revision"));
				nucleoEmisario.setEstadoRevision(new Integer(rs.getInt("estado_revision")));
	
				lstNucleos.add(nucleoEmisario); 
			}  

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			safeClose(conn, ps, rs);
		}
		return lstNucleos;
	}

	public ArrayList getLstNucleosEmisario(int idEmisario){

		ArrayList lstNucleos = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{       
			EmisariosEIEL emisario = null;

			emisario = getEmisarioEIEL(idEmisario);

			if (emisario != null){

				ps = conn.prepareStatement("EIELgetListaNucleosEmisario");
				ps.setString(1, emisario.getClave());
				ps.setString(2, emisario.getCodINEProvincia());
				ps.setString(3, emisario.getCodINEMunicipio());
				ps.setString(4, emisario.getCodOrden());
				rs = ps.executeQuery(); 

				while (rs.next())
				{
					NucleoEmisario nucleoEmisario = new NucleoEmisario();
					nucleoEmisario.setClaveEmisario(rs.getString("clave_tem"));
					nucleoEmisario.setCodProvEmisario(rs.getString("codprov_tem"));
					nucleoEmisario.setCodMunicEmisario(rs.getString("codmunic_tem"));
					nucleoEmisario.setCodTramoEmisario(rs.getString("tramo_em"));
					nucleoEmisario.setCodProvNucleo(rs.getString("codprov_pobl"));
					nucleoEmisario.setCodMunicNucleo(rs.getString("codmunic_pobl"));
					nucleoEmisario.setCodEntNucleo(rs.getString("codentidad_pobl"));
					nucleoEmisario.setCodPoblNucleo(rs.getString("codpoblamiento_pobl"));
					nucleoEmisario.setObservaciones(rs.getString("observ"));
					nucleoEmisario.setFechaRevision(rs.getDate("fecha_revision"));
					nucleoEmisario.setEstadoRevision(new Integer(rs.getInt("estado_revision")));

					lstNucleos.add(nucleoEmisario); 
				}  
			}

		}
		catch (SQLException ex)
		{           
			new DataException(ex);
		}
		finally{
			safeClose(conn, ps, rs);
		}

		return lstNucleos;
	}

		
}


