/**
 * OperacionesContaminantes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.contaminantes;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Vector;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.protocol.CEnvioOperacion;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.contaminantes.tipos.CTipoAnexo;
import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.licencias.CPersonaJuridicoFisica;
import com.geopista.protocol.net.EnviarSeguro;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 13-oct-2004
 * Time: 16:30:52
 */
public class OperacionesContaminantes {
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(OperacionesContaminantes.class);
	private String url;
	private final String servletName = "/CServletDB";
	private final String servletNameContaminantes = "/CServletContaminantes";
	private final String servletNameGetInspectores = "/GetInspectores";
	private final String servletNameSaveInspector = "/SaveInspector";
	private final String servletNameSaveActividad = "/SaveActividad";
	private final String servletNameDeleteActividad = "/DeleteActividad";
	private final String servletNameDeleteInspector = "/DeleteInspector";
	private final String servletNameSaveArbolado = "/SaveArbolado";
	private final String servletNameDeleteArbolado = "/DeleteArbolado";
	private final String servletNameSaveVertedero = "/SaveVertedero";
	private final String servletNameDeleteVertedero = "/DeleteVertedero";
	private final String servletNameGetPlantillas = "/GetPlantillas";
	private final String servletNameGetActividades = "/GetActividades";
    private final String servletNameGetMasCercana = "/GetDireccionMasCercana";
    private final String servletNameInsertHistorico = "/GestionarHistorico";
    private final String servletNameGetHistorico = "/GetHistorico";

   
	public OperacionesContaminantes(String sUrl) {
		url = sUrl;
		ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.SESION_KEY);
		com.geopista.security.SecurityManager.setIdMunicipio(iSesion.getIdMunicipio());
	}

	public Vector getSearchedAddressesByNumPolicia(Hashtable hash) throws Exception {
		try {
            
			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantes.CMD_GET_SEARCHED_ADDRESSES_BY_NUMPOLICIA);
			envioOperacion.setHashtable(hash);
			
			StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(envioOperacion);

			StringReader sr = EnviarSeguro.enviarPlano(url + servletNameContaminantes, sw.toString());
			CResultadoOperacion resultado = (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
			
			Vector datos = null;
			if (resultado.getResultado())
				datos = resultado.getVector();
			
			if (datos == null)
				datos = new Vector();
			
			return datos;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception busqueda direcciones: " + sw.toString());
			throw ex;
		}
	}
	
	public Vector getArbolados() throws Exception {
		try {
			Class.forName("com.geopista.sql.GEOPISTADriver");
			String sConn = "jdbc:pista:" + url + servletName;
			Connection conn = DriverManager.getConnection(sConn);
			// en vez de la sentencia SQL.
			// select idperm, def, type from UsrGrouPerm
			PreparedStatement ps = conn.prepareStatement("allarbolados");
			ps.setString(1, com.geopista.security.SecurityManager.getIdMunicipio());
			ResultSet rs = ps.executeQuery();
			Vector auxListaArbolados = new Vector();
			while (rs.next()) {
				Arbolado aux = new Arbolado(rs.getString("id"), rs.getString("observaciones"), rs.getFloat("extension_verde"));
                try
                {
                    aux.setFechaPlanta(rs.getTimestamp("fecha_plantacion"));
                    aux.setFechaUltimaTala(rs.getTimestamp("fecha_ultima_tala"));
                    aux.setPlantadoPor(rs.getString("plantado_por"));
                    aux.setIdTipo(rs.getString("id_tipo"));
                }catch(Exception e){logger.error("Error al cargar los nuevos datos de arbolado",e);}
				auxListaArbolados.add(aux);
			}
			rs.close();
			ps.close();
			conn.close();
            return auxListaArbolados;
		} catch (Exception e) {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("ERROR al recoger las zonas arboladas:" + sw.toString());
		}
		return null;
	}


	public CResultadoOperacion getInspectores() throws Exception {
		try {
			StringReader sr = EnviarSeguro.enviarPlano(url + servletNameGetInspectores, "");
			return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception al obtener la lista de inspectores: " + sw.toString());
			throw ex;
		}
	}

	public CResultadoOperacion saveInspector(Inspector inspector) throws Exception {
		try {
            /*
			StringWriter sw = new StringWriter();
			Marshaller.marshal(inspector, sw);
            */
            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(inspector);

			StringReader sr = EnviarSeguro.enviarPlano(url + servletNameSaveInspector, sw.toString());
			return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception al salvar el inspector: " + sw.toString());
			throw ex;
		}
	}

	public CResultadoOperacion saveActividad(Contaminante actividad) throws Exception {
		try {
            /*
			StringWriter sw = new StringWriter();
			Marshaller.marshal(actividad, sw);
            */
            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(actividad);

            Vector vInspecciones= actividad.getInspecciones();

            /* MultiPartPost */
            //StringReader sr = EnviarSeguro.enviarPlano(url + servletNameSaveActividad, sw.toString());
            StringReader sr = EnviarSeguro.enviarActividadContaminante(url + servletNameSaveActividad, sw.toString(), vInspecciones);
            /**/
			return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception al salvar la actividad: " + sw.toString());
			throw ex;
		}
	}
	
	

	public CResultadoOperacion saveVertedero(Vertedero vertedero) throws Exception {
		try {
            /*
			StringWriter sw = new StringWriter();
			Marshaller.marshal(vertedero, sw);
            */
            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(vertedero);

			StringReader sr = EnviarSeguro.enviarPlano(url + servletNameSaveVertedero, sw.toString());
			return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception al salvar el vertedero: " + sw.toString());
			throw ex;
		}
	}


	public CResultadoOperacion deleteVertedero(Vertedero vertedero) throws Exception {
		try {
            /*
			StringWriter sw = new StringWriter();
			Marshaller.marshal(vertedero, sw);
            */
            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(vertedero);

			StringReader sr = EnviarSeguro.enviarPlano(url + servletNameDeleteVertedero, sw.toString());
			return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception al salvar el vertedero: " + sw.toString());
			throw ex;
		}
	}

	public CResultadoOperacion deleteActividad(Contaminante contaminante) throws Exception {
		try {
            /*
			StringWriter sw = new StringWriter();
			Marshaller.marshal(contaminante, sw);
            */
            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(contaminante);

			StringReader sr = EnviarSeguro.enviarPlano(url + servletNameDeleteActividad, sw.toString());
			return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception al salvar el vertedero: " + sw.toString());
			throw ex;
		}
	}

	public CResultadoOperacion saveArbolado(Arbolado arbolado) throws Exception {
		try {
            /*
			StringWriter sw = new StringWriter();
			Marshaller.marshal(arbolado, sw);
            */
            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(arbolado);

			StringReader sr = EnviarSeguro.enviarPlano(url + servletNameSaveArbolado, sw.toString());
			return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception al salvar el inspector: " + sw.toString());
			throw ex;
		}
	}

	public CResultadoOperacion deleteInspector(Inspector inspector) throws Exception {
		try {
            /*
			StringWriter sw = new StringWriter();
			Marshaller.marshal(inspector, sw);
            */
            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(inspector);

			StringReader sr = EnviarSeguro.enviarPlano(url + servletNameDeleteInspector, sw.toString());
			return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception al borrar el inspector: " + sw.toString());
			throw ex;
		}
	}


	public CResultadoOperacion deleteArbolado(Arbolado arbolado) throws Exception {
		try {
            /*
			StringWriter sw = new StringWriter();
			Marshaller.marshal(arbolado, sw);
            */
            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(arbolado);

			StringReader sr = EnviarSeguro.enviarPlano(url + servletNameDeleteArbolado, sw.toString());
			return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception al borrar el inspector: " + sw.toString());
			throw ex;
		}
	}

	public Vector getVertederos() throws Exception {
		try {
			Class.forName("com.geopista.sql.GEOPISTADriver");
			String sConn = "jdbc:pista:" + url + servletName;
			Connection conn = DriverManager.getConnection(sConn);
			// en vez de la sentencia SQL.
			// select vertedero.id_vertedero as id_vertedero, vertedero.tipo as tipovertedero,
			//vertedero.titularidad as titularidad, vertedero.gestion_administrativa as gadministrativa,
			//vertedero.problemas_existentes as problemas, vertedero.capacidad as capacidad,
			//vertedero.grado_ocupacion as ocupacion, vertedero.posibilidad_ampliacion as ampliacion,
			//vertedero.estado_conservacion as estado, vertedero.vida_util as vida_util,
			//residuo_municipal.id_contenedor as id_contenedor, residuo_municipal.tipo as tipo_residuo,
			//residuo_municipal.ratio as ratio, residuo_municipal.situacion as situacion,
			//residuo_municipal.media_recoleccion_diaria as diaria, residuo_municipal.media_recoleccion_anual as anual
			//from vertedero
			//LEFT OUTER JOIN residuo_municipal on (vertedero.id_vertedero=residuo_municipal.id_vertedero)
			//where vertedero.id_municipio=?   order by vertedero.id_vertedero');

			PreparedStatement ps = conn.prepareStatement("allvertederos");
			ps.setString(1, com.geopista.security.SecurityManager.getIdMunicipio());
			ResultSet rs = ps.executeQuery();
			Vector auxListaVertederos = new Vector();
			Vertedero oldVertedero = null;

			while (rs.next()) {
				String newIdVertedero = rs.getString("id_vertedero");
				if ((oldVertedero == null) || (!oldVertedero.getId().equals(newIdVertedero))) {
					oldVertedero = new Vertedero();
					oldVertedero.setId(rs.getString("id_vertedero"));
					oldVertedero.setTipo(rs.getString("tipovertedero"));
					oldVertedero.setTitularidad(rs.getString("titularidad"));
					oldVertedero.setgAdm(rs.getString("gadministrativa"));
					oldVertedero.setpExistentes(rs.getString("problemas"));
					oldVertedero.setCapacidad((rs.getString("capacidad") == null ? null : new Long(rs.getLong("capacidad"))));
					oldVertedero.setgOcupacion((rs.getString("ocupacion") == null ? null : new Float(rs.getFloat("ocupacion"))));
					oldVertedero.setPosiAmplia(rs.getString("ampliacion"));
					oldVertedero.setEstado(rs.getString("estado"));
					oldVertedero.setVidaUtil((rs.getString("vida_util") == null ? null : new Integer(rs.getInt("vida_util"))));
					auxListaVertederos.add(oldVertedero);
				}
				if (rs.getString("id_contenedor") != null) {
					Residuo auxResiduo = new Residuo();
					auxResiduo.setId(rs.getString("id_contenedor"));
					auxResiduo.setTipo(rs.getString("tipo_residuo"));
					auxResiduo.setRatio((rs.getString("ratio") == null ? null : new Float(rs.getFloat("ratio"))));
					auxResiduo.setSituacion(rs.getString("situacion"));
					auxResiduo.setDiaria(rs.getString("diaria") == null ? null : new Long(rs.getLong("diaria")));
					auxResiduo.setAnual(rs.getString("anual") == null ? null : new Long(rs.getLong("anual")));
					oldVertedero.addResiduo(auxResiduo);
				}
			}
			rs.close();
			ps.close();
			conn.close();
			return auxListaVertederos;
		} catch (Exception e) {
			logger.error("ERROR al recoger los vertederos:" ,e);
		}
		return null;
	}

	public Contaminante getActividad(String idActividad) throws Exception {
		try {
			Class.forName("com.geopista.sql.GEOPISTADriver");
			String sConn = "jdbc:pista:" + url + servletName;
			Connection conn = DriverManager.getConnection(sConn);
			// en vez de la sentencia SQL.
			// select c.id as id_actividad,c.id_tipo_actividad as tipo_actividad,
			//c.id_razon_estudio as razon_estudio,c.num_administrativo as numeroadm,c.asunto as asunto,c.fecha_inicio as fechaini,
			//c.fecha_fin as fechafin,c.tipo_via_afecta as via,c.nombre_via_afecta as nombre_via,c.numero_via_afecta as numero_via,
			//c.cpostal_afecta as cpostal,i.id as id_inspeccion,i.id_responsable as id_responsable,i.num_folios as num_folios,
			//i.fecha_inicio as fecha_inicio,i.fecha_fin as fecha_fin,i.fecha_inicio_rec_datos as fecha_inicio_rec_datos,
			//i.fecha_fin_rec_datos as fecha_fin_rec_datos,i.num_dias_rec_datos as num_dias_rec_datos,i.puntos_fijos_medicion,i.puntos_moviles_medicion, i.sustancias_contaminantes,
			//i.concentracion_min, i.concentracion_max, i.es_zona_latente, i.motivos_zona_latente, i.es_zona_saturada
			//, i.motivos_zona_saturada, i.factores_de_riesgo, i.medidas_a_adoptar,i.resultados, i.observaciones  from actividad_contaminante as c LEFT OUTER JOIN inspeccion as i on (c.id=i.id_actividad)
			//where c.id_municipio=? and c.id=?;

			PreparedStatement ps = conn.prepareStatement("getcontaminante");
			ps.setInt(1, Integer.parseInt(com.geopista.security.SecurityManager.getIdMunicipio()));
			ps.setInt(2, Integer.parseInt(idActividad));
			ResultSet rs = ps.executeQuery();

            ResultSet rs2= null;
            PreparedStatement ps2= null;

			Contaminante actividad = null;
			while (rs.next()) {
				if (actividad == null) {
					actividad = new Contaminante();
					actividad.setAsunto(rs.getString("asunto"));
					actividad.setCpostal(rs.getString("cpostal"));
					actividad.setfFin(rs.getDate("fechafin"));
					actividad.setfInicio(rs.getDate("fechaini"));
					actividad.setId(rs.getString("id_actividad"));
					actividad.setId_razon(rs.getString("razon_estudio"));
					actividad.setId_tipo(rs.getString("tipo_actividad"));
					actividad.setNombrevia(rs.getString("nombre_via"));
					actividad.setNumeroAdm(rs.getString("numeroadm"));
					actividad.setNumerovia(rs.getString("numero_via"));
					actividad.setTipovia(rs.getString("via"));
				}
				if (rs.getString("id_inspeccion") != null) {
					Inspeccion inspeccion = new Inspeccion();
					inspeccion.setCmax(rs.getString("concentracion_max"));
					inspeccion.setCmin(rs.getString("concentracion_min"));
					inspeccion.setFfin(rs.getDate("fecha_fin"));
					inspeccion.setFfindatos(rs.getDate("fecha_fin_rec_datos"));
					inspeccion.setFinicio(rs.getDate("fecha_inicio"));
					inspeccion.setFinidatos(rs.getDate("fecha_inicio_rec_datos"));
					inspeccion.setFriesgo(rs.getString("factores_de_riesgo"));
					inspeccion.setId(rs.getString("id_inspeccion"));
					inspeccion.setId_res(String.valueOf(rs.getLong("id_responsable")));
					inspeccion.setMedidas(rs.getString("medidas_a_adoptar"));
					inspeccion.setMotlantente(rs.getString("motivos_zona_latente"));
					inspeccion.setMotsaturada(rs.getString("motivos_zona_saturada"));
					inspeccion.setNfolios((rs.getString("num_folios") == null ? null : new Integer(rs.getInt("num_folios"))));
					inspeccion.setNrec((rs.getString("num_dias_rec_datos") == null ? null : new Integer(rs.getInt("num_dias_rec_datos"))));
					inspeccion.setObs(rs.getString("observaciones"));
					inspeccion.setPfijos(rs.getString("puntos_fijos_medicion"));
					inspeccion.setPmoviles(rs.getString("puntos_moviles_medicion"));
					inspeccion.setResultados(rs.getString("resultados"));
					inspeccion.setSustancias(rs.getString("sustancias_contaminantes"));
					inspeccion.setZlatente((rs.getString("es_zona_latente") != null && (rs.getString("es_zona_latente").equals("1"))));
					inspeccion.setZsaturada((rs.getString("es_zona_saturada") != null && (rs.getString("es_zona_saturada").equals("1"))));
					actividad.addInspeccion(inspeccion);
                    try{
                        ps2 = conn.prepareStatement("getAnexos");
                        ps2.setString(1, inspeccion.getId());
                        rs2= ps2.executeQuery();
                        while (rs2.next()){
                            if (rs2.getString("id_anexo") != null) {
                                CAnexo anexo= new CAnexo();
                                anexo.setIdAnexo(new Long(rs2.getString("id_anexo")).longValue());
                                CTipoAnexo tipoAnexo= new CTipoAnexo();
                                tipoAnexo.setIdTipoAnexo(rs2.getInt("id_tipo_anexo"));
                                anexo.setTipoAnexo(tipoAnexo);
                                anexo.setFileName(rs2.getString("url_fichero"));
                                anexo.setObservacion(rs2.getString("observacion"));

                                inspeccion.addAnexo(anexo);
                            }
                        }
                        rs2.close();
                        ps2.close();

                    }catch(Exception ex){
                        logger.error("getAnexos.- Exception: " ,ex);
                    }
				}
            }
			rs.close();
			ps.close();

            if (actividad==null )
            {
                conn.close();
			    return actividad;
            }
             //Buscamos los posibles infractores
            try
            {
                //select persona_juridico_fisica.id_persona as id_persona, dni_cif, nombre, apellido1,apellido2 from persona_juridico_fisica, r_contaminante_infractor
                //where r_contaminante_infractor.id_actividad=1 and
                //persona_juridico_fisica.id_persona=r_contaminante_infractor.id_persona;
                ps2 = conn.prepareStatement("getInfractores");
                ps2.setInt(1, Integer.parseInt(actividad.getId()));
                rs2= ps2.executeQuery();
                Vector vInfractores = new Vector();
                while (rs2.next()){
                     CPersonaJuridicoFisica infractor= new CPersonaJuridicoFisica();
                     infractor.setIdPersona(rs2.getLong("id_persona"));
                     infractor.setDniCif(rs2.getString("dni_cif"));
                     infractor.setNombre(rs2.getString("nombre"));
                     infractor.setApellido1(rs2.getString("apellido1"));
                     infractor.setApellido2(rs2.getString("apellido2"));
                     vInfractores.add(infractor);
                }
                actividad.setInfractores(vInfractores);
                rs2.close();
                ps2.close();
            }catch(Exception ex){
                logger.error("Error al obtener los infractores: ", ex );
            }
			conn.close();
			return actividad;
		} catch (Exception e) {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("EXCEPTION:" + sw.toString());
		}
		return null;
	}


	public CResultadoOperacion getPlantillas(CPlantilla p) throws Exception {
		try {
            /*
			StringWriter sw = new StringWriter();
			Marshaller.marshal(p, sw);
            */
            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(p);

			StringReader sr = EnviarSeguro.enviarPlano(url + servletNameGetPlantillas, sw.toString());
			return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception al obtener las plantillas: " + sw.toString());
			throw ex;
		}
	}

/*
    public Vector getContaminantes() throws Exception {
        try {
            Class.forName("com.geopista.sql.GEOPISTADriver");
            String sConn = "jdbc:pista:" + url + servletName;
            Connection conn = DriverManager.getConnection(sConn);
            // en vez de la sentencia SQL.
            // select idperm, def, type from UsrGrouPerm
            PreparedStatement ps = conn.prepareStatement("allContaminantes");
            ps.setString(1, com.geopista.security.SecurityManager.getIdMunicipio());
            ResultSet rs = ps.executeQuery();

            Vector listaActividades= new Vector();
            Contaminante actividad = null;
            while (rs.next()) {
                String newIdActividad= rs.getString("id_actividad");
                if ((actividad == null) || (!actividad.getId().equals(newIdActividad))) {
                    actividad = new Contaminante();
                    actividad.setId(newIdActividad);
                    actividad.setAsunto(rs.getString("asunto"));
                    actividad.setCpostal(rs.getString("cpostal"));
                    actividad.setfFin(rs.getDate("fechafin"));
                    actividad.setfInicio(rs.getDate("fechaini"));
                    actividad.setId(rs.getString("id_actividad"));
                    actividad.setId_razon(rs.getString("razon_estudio"));
                    actividad.setId_tipo(rs.getString("tipo_actividad"));
                    actividad.setNombrevia(rs.getString("nombre_via"));
                    actividad.setNumeroAdm(rs.getString("numeroadm"));
                    actividad.setNumerovia(rs.getString("numero_via"));
                    actividad.setTipovia(rs.getString("via"));
                    listaActividades.add(actividad);
                }
                if (rs.getString("id_inspeccion") != null) {
                    Inspeccion inspeccion = new Inspeccion();
                    inspeccion.setId(rs.getString("id_inspeccion"));
                    inspeccion.setCmax(rs.getString("concentracion_max"));
                    inspeccion.setCmin(rs.getString("concentracion_min"));
                    inspeccion.setFfin(rs.getDate("fecha_fin"));
                    inspeccion.setFfindatos(rs.getDate("fecha_fin_rec_datos"));
                    inspeccion.setFinicio(rs.getDate("fecha_inicio"));
                    inspeccion.setFinidatos(rs.getDate("fecha_inicio_rec_datos"));
                    inspeccion.setFriesgo(rs.getString("factores_de_riesgo"));
                    inspeccion.setId(rs.getString("id_inspeccion"));
                    inspeccion.setId_res(rs.getString("id_responsable"));
                    inspeccion.setMedidas(rs.getString("medidas_a_adoptar"));
                    inspeccion.setMotlantente(rs.getString("motivos_zona_latente"));
                    inspeccion.setMotsaturada(rs.getString("motivos_zona_saturada"));
                    inspeccion.setNfolios((rs.getString("num_dias_rec_datos")==null?null:new Integer(rs.getInt("num_dias_rec_datos"))));
                    inspeccion.setNrec((rs.getString("num_dias_rec_datos")==null?null:new Integer(rs.getInt("num_dias_rec_datos"))));
                    inspeccion.setObs(rs.getString("observaciones"));
                    inspeccion.setPfijos(rs.getString("puntos_fijos_medicion"));
                    inspeccion.setPmoviles(rs.getString("puntos_moviles_medicion"));
                    inspeccion.setResultados(rs.getString("resultados"));
                    inspeccion.setSustancias(rs.getString("sustancias_contaminantes"));
                    inspeccion.setZlatente((rs.getString("es_zona_latente")!=null&&(rs.getString("es_zona_latente").equals("1"))));
                    inspeccion.setZsaturada((rs.getString("es_zona_saturada")!=null&&(rs.getString("es_zona_saturada").equals("1"))));
                    actividad.addInspeccion(inspeccion);
                }
           }

            rs.close();
            ps.close();
            conn.close();
            return listaActividades;
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("ERROR al recoger las zonas arboladas:" + sw.toString());
        }
        return null;
    }

*/

	public CResultadoOperacion getSearchedActividadesContaminantes(Hashtable hashtable) throws Exception {
		try {

			CEnvioOperacion envioOperacion = new CEnvioOperacion();
			envioOperacion.setHashtable(hashtable);
            /*
			StringWriter sw = new StringWriter();
			Marshaller.marshal(envioOperacion, sw);
            */
            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(envioOperacion);

			StringReader sr = EnviarSeguro.enviarPlano(url + servletNameGetActividades, sw.toString());
			return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception al obtener las plantillas: " + sw.toString());
			throw ex;
		}
	}
    public CResultadoOperacion getDireccionMasCercana(String idContaminante) throws Exception {
        try {
            StringReader sr = EnviarSeguro.enviarPlano(url + servletNameGetMasCercana, idContaminante);
            return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception al salvar el inspector: " + sw.toString());
            throw ex;
        }
    }

     public CResultadoOperacion gestionarHistorico(Historico historico) throws Exception {
        try {
            /*
            StringWriter sw = new StringWriter();
            Marshaller.marshal(historico, sw);
            */
            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(historico);

		    StringReader sr = EnviarSeguro.enviarPlano(url + servletNameInsertHistorico, sw.toString());
            return (CResultadoOperacion) Unmarshaller.unmarshal(CResultadoOperacion.class, sr);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception al salvar el inspector: " + sw.toString());
            throw ex;
        }
    }
     public Vector getHistorico(PeticionBusquedaHistorico peticion) throws Exception
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

                       StringReader sr=EnviarSeguro.enviarPlano(url+servletNameGetHistorico,sw.toString());
                       CResultadoOperacion resultado=(CResultadoOperacion)Unmarshaller.unmarshal(CResultadoOperacion.class,sr);
                       if (resultado.getResultado())
                       {
                            return resultado.getVector();
                       }else
                       {
                           logger.error("Error al obtener la lista de historicos:"+ resultado.getDescripcion());
                           return null;
                       }
                   }
                   catch (Exception ex) {
                       StringWriter sw = new StringWriter();
                       PrintWriter pw = new PrintWriter(sw);
                       ex.printStackTrace(pw);
                       logger.error("Exception al obtener la lista de historicos: " + sw.toString());
                       throw ex;
                   }

        }

}

