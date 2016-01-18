/**
 * COperacionesLicencias.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.licencias;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.exolab.castor.xml.Marshaller;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CEnvioOperacion;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.licencias.estados.CEstado;
import com.geopista.protocol.net.EnviarSeguro;


/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: satec $
 *          Fecha Ultima Modificacion:$Date: 2012/02/13 14:20:28 $
 *          $Name:  $
 *          $RCSfile: COperacionesLicencias.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class COperacionesLicencias {
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(COperacionesLicencias.class);

	public static String webappContext = "/";
	
	/**
	 * @param solicitudLicencia
	 * @return CResultadoOperacion
	 */
	public static CResultadoOperacion crearExpediente(CSolicitudLicencia solicitudLicencia, CExpedienteLicencia expedienteLicencia) {
		try {

			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_CREAR_EXPEDIENTE);
			envioOperacion.setSolicitudLicencia(solicitudLicencia);
			envioOperacion.setExpedienteLicencia(expedienteLicencia);

			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			if (!resultadoOperacion.getResultado()) {
				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
			}


			return resultadoOperacion;

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new CResultadoOperacion(false, ex.toString());

		}
	}
	
	public static CResultadoOperacion insertaAnexos(CSolicitudLicencia solicitudLicencia) {
		try {
 
			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_ANEXO_ALFRESCO);
			envioOperacion.setSolicitudLicencia(solicitudLicencia);

			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			if (!resultadoOperacion.getResultado()) {
				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
			}

			return resultadoOperacion;

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new CResultadoOperacion(false, ex.toString());

		}
	}

	public static CResultadoOperacion modificarExpediente(CSolicitudLicencia solicitudLicencia, CExpedienteLicencia expedienteLicencia) {
		try {

			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_MODIFICAR_EXPEDIENTE);
			envioOperacion.setSolicitudLicencia(solicitudLicencia);
			envioOperacion.setExpedienteLicencia(expedienteLicencia);

			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			if (!resultadoOperacion.getResultado()) {
				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
			} else if (expedienteLicencia.getInformes()!=null && expedienteLicencia.getInformes().size()>0)//enviamos los informes nuevos
            {
                try
                {
                    for(Enumeration e=expedienteLicencia.getInformes().elements();e.hasMoreElements();)
                    {
                         Informe auxInforme=(Informe)e.nextElement();
                         if (auxInforme.getId()==null) //el informe es nuevo
                         {
                             System.out.println("Intentando grabar el fichero:"+auxInforme.getFichero());
                             auxInforme.setIdSolicitud(solicitudLicencia.getIdSolicitud());
                             auxInforme.setNumExpediente(expedienteLicencia.getNumExpediente());
                             CResultadoOperacion resInforme=insertarInforme(auxInforme);
                             if (!resInforme.getResultado())
                                 logger.warn("Ha fallado la inserciï¿½n del informe: "+auxInforme.getFichero()+" "+resInforme.getDescripcion());
                             else
                                 logger.info("Informe insertado con exito: "+auxInforme.getFichero()+" "+resInforme.getDescripcion());
                           }
                    }
                }catch(Exception ex)
                {
                    logger.error("Excepciï¿½n al enviar los informes.",ex);
                }
            }
			return resultadoOperacion;
		} catch (Exception ex) {
			logger.error("Exception al modificar el expediente: " ,ex);
			return new CResultadoOperacion(false, ex.toString());
		}
	}
	
	public static CResultadoOperacion actualizarIdSigem(CExpedienteLicencia expedienteLicencia) {
		try {

			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_ACTUALIZAR_IDSIGEM);
			envioOperacion.setExpedienteLicencia(expedienteLicencia);

			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			if (!resultadoOperacion.getResultado()) {
				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
			} 
			return resultadoOperacion;
		} catch (Exception ex) {
			logger.error("Exception al modificar el ID de SIGEM del expediente: " ,ex);
			return new CResultadoOperacion(false, ex.toString());
		}
	}
	
	public static CResultadoOperacion obtenerIdSigem(CExpedienteLicencia expedienteLicencia) {
		try {

			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_OBTENER_IDSIGEM);
			envioOperacion.setExpedienteLicencia(expedienteLicencia);

			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			if (!resultadoOperacion.getResultado()) {
				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
			} 
			return resultadoOperacion;
		} catch (Exception ex) {
			logger.error("Exception al modificar el ID de SIGEM del expediente: " ,ex);
			return new CResultadoOperacion(false, ex.toString());
		}
	}
	
	public static CResultadoOperacion publicarExpedienteSigem(CExpedienteLicencia expedienteLicencia, CSolicitudLicencia solicitudLicencia) {
		try {

			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_PUBLICAR_EXPEDEINTE_SIGEM);
			envioOperacion.setExpedienteLicencia(expedienteLicencia);
			envioOperacion.setSolicitudLicencia(solicitudLicencia);

			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			if (!resultadoOperacion.getResultado()) {
				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
			} 
			return resultadoOperacion;
		} catch (Exception ex) {
			logger.error("Exception al modificar el ID de SIGEM del expediente: " ,ex);
			return new CResultadoOperacion(false, ex.toString());
		}
	}

	public static CResultadoOperacion actualizarEstadoExpediente(CExpedienteLicencia expedienteLicencia) {
		try {

			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_ACTUALIZAR_ESTADOSIGEM);
			envioOperacion.setExpedienteLicencia(expedienteLicencia);

			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			if (!resultadoOperacion.getResultado()) {
				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
			} 
			return resultadoOperacion;
		} catch (Exception ex) {
			logger.error("Exception al modificar el ID de SIGEM del expediente: " ,ex);
			return new CResultadoOperacion(false, ex.toString());
		}
	}

    public static CResultadoOperacion insertarInforme(Informe informe)
    {
        try {
            if (informe.getFichero()==null) return new CResultadoOperacion(false, "Path no encontrado");
            CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_INSERTAR_INFORME);
            envioOperacion.setParametro(informe);
            File fichero=new File(informe.getFichero());
            StringWriter sw = new StringWriter();
            Marshaller.marshal(envioOperacion, sw);
            CResultadoOperacion resultadoOperacion = EnviarSeguro.enviarFichero(CConstantesComando.servletLicenciasUrl,sw.toString(), fichero);
            if (!resultadoOperacion.getResultado()) {
                logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
                logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
            }
            return resultadoOperacion;

        } catch (Exception ex) {
            logger.error("Exception al modificar el expediente: " ,ex);
            return new CResultadoOperacion(false, ex.toString());
        }
    }


	/**
	 * @return Vector
	 */
	public static Vector getTiposObra() {
		try {


			com.geopista.protocol.CEnvioOperacion comando = new com.geopista.protocol.CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_TIPOS_OBRA);

			CResultadoOperacion resultadoOperacion = sendOverHttp(comando);

			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();

			}


			Vector vector = resultadoOperacion.getVector();

			if (vector == null) {
				//logger.info("vector: " + vector);
				return new Vector();
			}

			//logger.info("vector.size(): " + vector.size());
			return vector;

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}


	/**
	 * @return Vector
	 */
	public static Vector getViaNotificacion() {
		try {


			com.geopista.protocol.CEnvioOperacion comando = new com.geopista.protocol.CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_VIAS_NOTIFICACION);

			CResultadoOperacion resultadoOperacion = sendOverHttp(comando);

			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();

			}


			Vector vector = resultadoOperacion.getVector();
			if (vector == null) {
				//logger.info("vector: " + vector);
				return new Vector();
			}

			//logger.info("vector.size(): " + vector.size());
			return vector;

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}


	/**
	 * @return Vector
	 */
	public static Vector getTiposLicencia() {
		try {


			com.geopista.protocol.CEnvioOperacion comando = new com.geopista.protocol.CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_TIPOS_LICENCIA);

			CResultadoOperacion resultadoOperacion = sendOverHttp(comando);

			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();

			}


			Vector vector = resultadoOperacion.getVector();
			if (vector == null) {
				//logger.info("vector: " + vector);
				return new Vector();
			}

			//logger.info("vector.size(): " + vector.size());
			return vector;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}


	/**
	 * @return Vector
	 */
	public static Vector getEstadosNotificacion() {
		try {


			com.geopista.protocol.CEnvioOperacion comando = new com.geopista.protocol.CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_ESTADOS_NOTIFICACION);

			CResultadoOperacion resultadoOperacion = sendOverHttp(comando);

			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();

			}


			Vector vector = resultadoOperacion.getVector();
			if (vector == null) {
				//logger.info("vector: " + vector);
				return new Vector();
			}

			//logger.info("vector.size(): " + vector.size());
			return vector;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}


	/**
	 * @return Vector
	 */
	public static Vector getEstadosResolucion() {
		try {


			com.geopista.protocol.CEnvioOperacion comando = new com.geopista.protocol.CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_ESTADOS_RESOLUCION);

			CResultadoOperacion resultadoOperacion = sendOverHttp(comando);

			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();

			}

			Vector vector = resultadoOperacion.getVector();
			if (vector == null) {
				//logger.info("vector: " + vector);
				return new Vector();
			}

			//logger.info("vector.size(): " + vector.size());
			return vector;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}


	/**
	 * @return Vector
	 */
	public static Vector getTiposAnexo() {
		try {


			com.geopista.protocol.CEnvioOperacion comando = new com.geopista.protocol.CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_TIPOS_ANEXO);

			CResultadoOperacion resultadoOperacion = sendOverHttp(comando);

			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();

			}


			Vector vector = resultadoOperacion.getVector();
			if (vector == null) {
				//logger.info("vector: " + vector);
				return new Vector();
			}

			//logger.info("vector.size(): " + vector.size());
			return vector;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}


	public static Vector getTiposFinalizacion() {
		try {


			com.geopista.protocol.CEnvioOperacion comando = new com.geopista.protocol.CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_TIPOS_FINALIZACION);

			CResultadoOperacion resultadoOperacion = sendOverHttp(comando);

			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();

			}

			Vector vector = resultadoOperacion.getVector();
			if (vector == null) {
				//logger.info("vector: " + vector);
				return new Vector();
			}

			//logger.info("vector.size(): " + vector.size());
			return vector;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}


	public static Vector getTiposNotificacion() {
		try {


			com.geopista.protocol.CEnvioOperacion comando = new com.geopista.protocol.CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_TIPOS_NOTIFICACION);

			CResultadoOperacion resultadoOperacion = sendOverHttp(comando);

			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();

			}

			Vector vector = resultadoOperacion.getVector();
			if (vector == null) {
				//logger.info("vector: " + vector);
				return new Vector();
			}

			//logger.info("vector.size(): " + vector.size());
			return vector;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}


	public static Vector getTiposTramitacion() {
		try {


			com.geopista.protocol.CEnvioOperacion comando = new com.geopista.protocol.CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_TIPOS_TRAMITACION);

			CResultadoOperacion resultadoOperacion = sendOverHttp(comando);

			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();

			}

			Vector vector = resultadoOperacion.getVector();
			if (vector == null) {
				//logger.info("vector: " + vector);
				return new Vector();
			}

			//logger.info("vector.size(): " + vector.size());
			return vector;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}


	public static Vector getEstadosDisponibles(CExpedienteLicencia expedienteLicencia, int idTipoLicencia) {
		try {
            if (expedienteLicencia==null)
            {
                expedienteLicencia= new CExpedienteLicencia();
                expedienteLicencia.setEstado(new CEstado(CConstantesComando.ESTADO_INICIAL));
            }
			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_ESTADOS_DISPONIBLES);
            expedienteLicencia.setTipoLicenciaDescripcion(new Integer(idTipoLicencia).toString());
			envioOperacion.setParametro(expedienteLicencia);

			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();

			}

			Vector vector = resultadoOperacion.getVector();
			if (vector == null) {
//				//logger.info("vector: " + vector);
				return new Vector();
			}

			//logger.info("vector.size(): " + vector.size());
			return vector;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}


	public static Vector getEstadosPermitidos(CExpedienteLicencia expedienteLicencia, int idTipoLicencia) {
		try {
            if (expedienteLicencia==null)
            {
                expedienteLicencia= new CExpedienteLicencia();
                expedienteLicencia.setEstado(new CEstado(CConstantesComando.ESTADO_INICIAL));
            }
			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_ESTADOS_PERMITIDOS);
            expedienteLicencia.setTipoLicenciaDescripcion(new Integer(idTipoLicencia).toString());
			envioOperacion.setParametro(expedienteLicencia);
            CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();

			}

			Vector vector = resultadoOperacion.getVector();
			if (vector == null) {
				//logger.info("vector: " + vector);
				return new Vector();
			}

			//logger.info("vector.size(): " + vector.size());
			return vector;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}



	public static Vector getEstados(Vector tiposLicencia) {
		try {

			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_ESTADOS);
            envioOperacion.setTiposLicencia(tiposLicencia);
			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();

			}

			Vector vector = resultadoOperacion.getVector();
			if (vector == null) {
				//logger.info("vector: " + vector);
				return new Vector();
			}

			//logger.info("vector.size(): " + vector.size());
			return vector;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}


	/**
	 * @param hash campos por los que filtrar
	 * @return
	 */
	public static Vector getSearchedExpedientes(Hashtable hash, Vector tiposLicencia) {
		try {


			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_SEARCHED_EXPEDIENTES);
			envioOperacion.setHashtable(hash);
            envioOperacion.setTiposLicencia(tiposLicencia);
			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();

			}

			Vector vector = resultadoOperacion.getVector();
			if (vector == null) {
				//logger.info("vector: " + vector);
				return new Vector();
			}

			//logger.info("vector.size(): " + vector.size());
			return vector;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}
    /**
	 * @param hash campos por los que filtrar
	 * @return
	 */
	public static CResultadoOperacion getSearchedExpedientesPlanos(Hashtable hash, Vector tiposLicencia) {
		try {
			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_SEARCHED_EXPEDIENTES_PLANOS);
			envioOperacion.setHashtable(hash);
            envioOperacion.setTiposLicencia(tiposLicencia);
			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);
            return resultadoOperacion;
		} catch (Exception ex) {
            logger.error("Exception: ",ex);
			return new CResultadoOperacion(false,ex.toString()); 
		}
	}


    public static Vector getSearchedLicenciasObra(Hashtable hash) {
        try {


            CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_SEARCHED_LICENCIAS_OBRA);
            envioOperacion.setHashtable(hash);

            CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

            if (!resultadoOperacion.getResultado()) {

                logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
                logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
                return new Vector();

            }

            Vector vector = resultadoOperacion.getVector();
            if (vector == null) {
                //logger.info("vector: " + vector);
                return new Vector();
            }

            //logger.info("vector.size(): " + vector.size());
            return vector;


        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return new Vector();

        }
    }


	public static Vector getSearchedReferenciasCatastrales(Hashtable hash)
    {
		try {
        	CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_SEARCHED_REFERENCIAS_CATASTRALES);
			envioOperacion.setHashtable(hash);
			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);
			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();
			}
			if (resultadoOperacion.getVector()==null) return new Vector();
			return resultadoOperacion.getVector();
		} catch (Exception ex) {
			logger.error("Exception: " ,ex);
			return new Vector();
		}
	}

	public static Vector getSearchedAddresses(Hashtable hash) {
		try {


			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_SEARCHED_ADDRESSES);
			envioOperacion.setHashtable(hash);

			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();

			}

			Vector vector = resultadoOperacion.getVector();
			if (vector == null) {
				//logger.info("vector: " + vector);
				return new Vector();
			}

			//logger.info("vector.size(): " + vector.size());
			return vector;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}



	public static Vector getSearchedAddressesByNumPolicia(Hashtable hash) {
		try {


			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_SEARCHED_ADDRESSES_BY_NUMPOLICIA);
			envioOperacion.setHashtable(hash);

			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();

			}

			Vector vector = resultadoOperacion.getVector();
			if (vector == null) {
				//logger.info("vector: " + vector);
				return new Vector();
			}

			//logger.info("vector.size(): " + vector.size());
			return vector;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}



	public static Vector getSearchedPersonas(Hashtable hash) {
		try {


			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_SEARCHED_PERSONAS);
			envioOperacion.setHashtable(hash);

			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();

			}

			Vector vector = resultadoOperacion.getVector();
			if (vector == null) {
				//logger.info("vector: " + vector);
				return new Vector();
			}

			//logger.info("vector.size(): " + vector.size());
			return vector;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}


	public static Vector getNotificaciones(Hashtable hash) {
		try {


			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_NOTIFICACIONES);
			envioOperacion.setHashtable(hash);

			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();

			}


			Vector vector = resultadoOperacion.getVector();
			if (vector == null) {
				//logger.info("vector: " + vector);
				return new Vector();
			}

			//logger.info("vector.size(): " + vector.size());
			return vector;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}


	public static Vector getParcelario(Hashtable hash) {
		try {


			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_PARCELARIO);
			envioOperacion.setHashtable(hash);

			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			if (!resultadoOperacion.getResultado()) {

				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
				return new Vector();

			}


			Vector vector = resultadoOperacion.getVector();
			if (vector == null) {
				//logger.info("vector: " + vector);
				return new Vector();
			}

			//logger.info("vector.size(): " + vector.size());
			return vector;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}


	public static CResultadoOperacion getExpedienteLicencia(String numExpediente, String locale, Vector tiposLicencia) {
		try {


			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_SOLICITUD_LICENCIA);
			envioOperacion.setParametro(numExpediente);
            envioOperacion.setParametro2(locale);
            envioOperacion.setTiposLicencia(tiposLicencia);

			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			return resultadoOperacion;

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return null;

		}
	}

	public static CResultadoOperacion getSolicitudesExpedientesInforme(Hashtable hash, Vector tiposLicencia) {
		try {
			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_SOLICITUDES_EXPEDIENTES_INFORME);
			envioOperacion.setHashtable(hash);
            envioOperacion.setTiposLicencia(tiposLicencia);

			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			return resultadoOperacion;

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return null;

		}
	}


	public static CResultadoOperacion getPlantillas(String aplicacion) {
		try {
			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_PLANTILLAS);
            envioOperacion.setParametro(new String(aplicacion));
			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			return resultadoOperacion;

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return null;

		}
	}


    public static Vector getNotificacionesMenu(Hashtable hash, Vector tiposLicencia) {
        try {
            CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_NOTIFICACIONES_MENU);
            envioOperacion.setHashtable(hash);
            envioOperacion.setTiposLicencia(tiposLicencia);

            CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

            if (!resultadoOperacion.getResultado()) {
                return new Vector();
            }

            Vector vector = resultadoOperacion.getVector();
            if (vector == null) {
                return new Vector();
            }

            return vector;

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return null;

        }
    }

    public static CResultadoOperacion getEventos(Hashtable hash, Vector tiposLicencia, String locale) {
        try {
            CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_EVENTOS);
            envioOperacion.setHashtable(hash);
            envioOperacion.setTiposLicencia(tiposLicencia);
            envioOperacion.setParametro(locale);
            CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

            return resultadoOperacion;

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return null;

        }
    }


    public static CResultadoOperacion getUltimosEventos(Hashtable hash, Vector tiposLicencia, String locale) {
        try {
            CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_ULTIMOS_EVENTOS);
            envioOperacion.setHashtable(hash);
            envioOperacion.setTiposLicencia(tiposLicencia);
            envioOperacion.setParametro(locale);
            CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

            return resultadoOperacion;

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return null;

        }
    }


    public static CResultadoOperacion getEventosSinRevisar(Hashtable hash, Vector tiposLicencia, String locale) {
        try {
            CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_EVENTOS_SIN_REVISAR);
            envioOperacion.setHashtable(hash);
            envioOperacion.setTiposLicencia(tiposLicencia);
            envioOperacion.setParametro(locale);
            CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

            return resultadoOperacion;

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return null;

        }
    }



    public static CResultadoOperacion getHistorico(Hashtable hash, Vector tiposLicencia, String locale) {
        try {
            CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_HISTORICO);
            envioOperacion.setHashtable(hash);
            envioOperacion.setTiposLicencia(tiposLicencia);
            envioOperacion.setParametro(locale);
            CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

            return resultadoOperacion;

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return null;

        }
    }
    

    public static CResultadoOperacion getHistoricoExpediente(Hashtable hash, Vector tiposLicencia, String locale) {
        try {
            CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_HISTORICO_EXPEDIENTE);
            envioOperacion.setHashtable(hash);
            envioOperacion.setTiposLicencia(tiposLicencia);
            envioOperacion.setParametro(locale);
            CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

            return resultadoOperacion;

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return null;

        }
    }


    public static CResultadoOperacion getNotificacionesPendientes(Vector tiposLicencia) {
        try {
            CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_NOTIFICACIONES_PENDIENTES);
            envioOperacion.setTiposLicencia(tiposLicencia);
            CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

            return resultadoOperacion;

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return null;

        }
    }


    public static CResultadoOperacion getEventosPendientes(Vector tiposLicencia, String locale) {
        try {
            CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_EVENTOS_PENDIENTES);
            envioOperacion.setTiposLicencia(tiposLicencia);
            envioOperacion.setParametro((String)locale);
            CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

            return resultadoOperacion;

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return null;

        }
    }


	private static CResultadoOperacion sendOverHttp(com.geopista.protocol.CEnvioOperacion comando) {

		try {

            /*
			StringWriter sw = new StringWriter();
			Marshaller.marshal(comando, sw);
            */

            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(comando);

			String text = sw.toString();
			//logger.info("text: " + text);

            /** MultipartPostMethod -- envio de ficheros */

            Vector vAnexos= new Vector();
            if (comando.getSolicitudLicencia() != null){
                vAnexos= comando.getSolicitudLicencia().getAnexos();
                if (vAnexos == null) vAnexos= new Vector();
                /** anexos de la mejora de datos */
                Vector mejoras= comando.getSolicitudLicencia().getMejoras();
                if (mejoras != null){
                    for (Enumeration e= mejoras.elements(); e.hasMoreElements();){
                        Mejora mejora= (Mejora)e.nextElement();
                        Vector aux= mejora.getAnexos();
                        if (aux != null){
                            for (Enumeration e1= aux.elements(); e1.hasMoreElements();){
                                CAnexo anexo= (CAnexo)e1.nextElement();
                                vAnexos.add(anexo);
                            }

                        }
                    }
                }
            }

            if (comando.getExpedienteLicencia() != null){
                /** anexos de la alegacion */
                Alegacion alegacion= comando.getExpedienteLicencia().getAlegacion();
                if (alegacion != null){
                    Vector aux= alegacion.getAnexos();
                    if (aux != null){
                        for (Enumeration e1= aux.elements(); e1.hasMoreElements();){
                            CAnexo anexo= (CAnexo)e1.nextElement();
                            vAnexos.add(anexo);
                        }
                    }
                }

            }
            
            return enviar(text, vAnexos);

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());

			return new CResultadoOperacion(false, ex.toString());

		}
	}	

	public static CResultadoOperacion enviar(String text, Vector vAnexos) throws Exception{
		return EnviarSeguro.enviar(AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + webappContext, text, vAnexos);
	}

    public static CResultadoOperacion getDireccionMasCercana(String geometria) {
        try {


            CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_GET_DIRECCION_MAS_CERCANA);
            envioOperacion.setParametro(geometria);

            return  sendOverHttp(envioOperacion);

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return new CResultadoOperacion(false, sw.toString());

        }
    }

    public static CResultadoOperacion bloquearExpediente(String numExpediente, boolean bloquear){
        try {
            CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_BLOQUEAR_EXPEDIENTE);
            CExpedienteLicencia expediente= new CExpedienteLicencia();
            expediente.setNumExpediente(numExpediente);
            envioOperacion.setExpedienteLicencia(expediente);
            envioOperacion.setParametro(new Boolean(bloquear));

            return  sendOverHttp(envioOperacion);

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return new CResultadoOperacion(false, sw.toString());

        }

    }


    public static CResultadoOperacion deleteGeometriaOcupacion(String idFeature) {
        try {
            CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_DELETE_GEOMETRIA_OCUPACION);
            envioOperacion.setParametro(idFeature);
            CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

            return resultadoOperacion;

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return null;

        }
    }

    public static String getAnexoId(String fileName, Long idSolicitud){
    	try {
            CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_LICENCIAS_ANEXO_GETID);
            envioOperacion.setParametro(fileName);
            envioOperacion.setParametro2(idSolicitud);
            CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);
            if (!resultadoOperacion.getResultado()) {
				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());				
			}
            return resultadoOperacion.getDescripcion();
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return null;

        }
    }

}


