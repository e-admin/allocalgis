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

package com.geopista.app.inforeferencia;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.GeopistaDatosImportarPadron;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.task.TaskMonitor;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Esta clasae es la encargada de grabar las estadisticas del padron de habitantes en la base de datos
 * Creado por SATEC.
 * User: angeles
 * Date: 22-ago-2006
 * Time: 14:13:26
 */
public class SaveStatisticals {
	private static final Log logger = LogFactory.getLog(SaveStatisticals.class);

	private Blackboard blackboard;

	AppContext aplicacion;

	private Hashtable hashVias;

    private Boolean init=new Boolean (true);

    private static int grabadas;
	private static int nograbadas;
	private static int count;

	private static final String VARON = "1";

	private static final String MUJER = "6";

	private static final String NAC_ESPANOLA = "108";

	private static final int EST_NO_LEER_ESCRIBIR_1 = 10;

	private static final int EST_NO_LEER_ESCRIBIR_2 = 11;

	private static final int EST_NO_GRAD_ESCOLAR = 20;

	private static final int EST_SIN_ESTUDIOS = 21;

	private static final int EST_PRIMARIA_IMCOMPLETA = 22;

	private static final int EST_GRADUADO_ESCOLAR = 30;

	private static final int EST_BACHILLER_ELEMENTAL = 31;

	private static final int EST_FORMACION_PROFESIONAL1 = 32;

	private static final int EST_BACHILLER_FP2 = 40;

	private static final int EST_FP2 = 41;

	private static final int EST_BUP = 42;

	private static final int EST_TITULO_MEDIO = 43;

	private static final int EST_DIPLOMADO = 44;

	private static final int EST_ING_TEC = 45;

	private static final int EST_LICENCIADO = 46;

	private static final int EST_SUPERIORES = 47;

	private static final int EST_DOCTORADO = 48;

	/**
	 * Constructor de la clase realiza todas las operaciones necesarias para grabar los datos
	 */
	public SaveStatisticals() {
		//System.out.println("SaveStatisticals. Generando estadisticas");
		//logger.debug("SaveStatisticals. Generando estadisticas");
		aplicacion = (AppContext) AppContext.getApplicationContext();
		blackboard = aplicacion.getBlackboard();
		hashVias = (Hashtable) blackboard.get("estadisticas");
        try{init = (Boolean)blackboard.get("initEstadisticas");}catch (Exception ex){};
		try {
			delete();
			save(hashVias);
		} catch (Exception e) {
			logger.error(" Error al salvar las estadísticas", e);
		}
	}

	public void save(Hashtable hashVias) {
		if (hashVias == null){
			return;
		}
		StringBuffer mensajeAux = new StringBuffer(blackboard.get("MensajeEstadistica")
				.toString());
		TaskMonitor progressDialog = (TaskMonitor) blackboard
				.get("progressDialog");
        if (init.booleanValue())
        {
		    grabadas = 0;
		    nograbadas = 0;
		    count = 0;
        }
		//logger.debug("Numero de elementos en el hash de vias:"+hashVias.size());
		for (Enumeration e = hashVias.keys(); e.hasMoreElements();)
        {
			String tipoViaIne = (String) e.nextElement();
			Hashtable hashCalles = (Hashtable) hashVias.get(tipoViaIne);
			for (Enumeration e1 = hashCalles.keys(); e1.hasMoreElements();)
            {
				String nombreCalle = (String) e1.nextElement();
				Hashtable hashRotulos = (Hashtable) hashCalles.get(nombreCalle);
				for (Enumeration e2 = hashRotulos.keys(); e2.hasMoreElements();) {
					String numero = (String) e2.nextElement();
					String id_parcela = buscarParcela(tipoViaIne, nombreCalle,
							numero);
					logger.debug("Buscando dirección: " + " Tipo:" + tipoViaIne
							+ " Nombre: " + nombreCalle + ", Numero:" + numero);
					if (id_parcela == null || id_parcela.equals("0")) {
						try{count=count +((Vector)hashRotulos.get(numero)).size();}catch(Exception ex){}
						mensajeAux
								.append(aplicacion
										.getI18nString("importar.resultado.grabarestadisticas.parcelanoencontrada")
										+ " "
										+ " Tipo:"
										+ tipoViaIne
										+ " Nombre: "
										+ nombreCalle
										+ ", Numero:"
										+ numero
										+ ", count: "
										+ ((Vector) hashRotulos.get(numero))
												.size());
						try {
							nograbadas = nograbadas
									+ ((Vector) hashRotulos.get(numero)).size();
						} catch (Exception ex) {
						}
                        progressDialog.report(new Integer(count).toString()
                        	+ "/"
										+ blackboard.get("procesados")
												.toString()+" "+aplicacion.getI18nString("importar.resultado.grabarestadisticas"));
                        continue;
					} else
                        mensajeAux.append("\nDirección Georreferenciada"
                                                                + " "
                                                                + " Tipo:"
                                                                + tipoViaIne
                                                                + " Nombre: "
                                                                + nombreCalle
                                                                + ", Numero:"
                                                                + numero
                                                                + ", count: "
                                                                + ((Vector) hashRotulos.get(numero))
                                                                        .size()+ " Parcela: "+id_parcela);

						try {
							grabadas = grabadas
									+ ((Vector) hashRotulos.get(numero)).size();
						} catch (Exception ex) {
						}
					;

					Vector vectorDatos = (Vector) hashRotulos.get(numero);
					int mujeres = 0;
					int hombres = 0;
					int espanoles = 0;
					int extranjeros = 0;
					//int de0a20anios=0;
					//int de20a40anios=0;
					//int de40a60anios=0;
					//int masde60anios=0;
					// Las edades se clasifican en tramos de 10.
					// Hay 8 edades
					int[] clasificacionEdades = new int[] { 0, 0, 0, 0, 0, 0, 0 ,0};
					// Hay 17 niveles de estudio
					int[] nivelEstudios = new int[] { 0, 0, 0, 0, 0, 0, 0, 0,
							0, 0, 0, 0, 0, 0, 0, 0, 0 };
					//int estIngSupLicenciados=0;
					//int estIngTec=0;
					//int estDiplomados=0;
					//int estPrimarios=0;
					//int estSecundarios=0;
					//int estSinEstudios=0;
					//int estNoSabeLeer=0;

					for (Enumeration e3 = vectorDatos.elements(); e3
							.hasMoreElements();) {
						count++;
						progressDialog.report(new Integer(count).toString()
										+ "/"
										+ blackboard.get("procesados")
												.toString()+" "+aplicacion.getI18nString("importar.resultado.grabarestadisticas"));
						GeopistaDatosImportarPadron dato = (GeopistaDatosImportarPadron) e3
								.nextElement();
						//Sexo
						if (dato.getSexo() != null) {
							if (dato.getSexo().equals(VARON))
								hombres++;
							if (dato.getSexo().equals(MUJER))
								mujeres++;
						}
						//Nacimiento
						if (dato.getAnioNacimiento() != null) {
							try {
								logger.debug("Año nacimiento: "
										+ dato.getAnioNacimiento());
								int iAnioNacimiento = Integer.parseInt(dato
										.getAnioNacimiento());
								int iAnioActual = Integer
										.parseInt(new SimpleDateFormat("yyyy")
												.format(new Date()));
								int iDiferencia = iAnioActual - iAnioNacimiento;

								if (iDiferencia < 10)
									clasificacionEdades[0]++;
								else if (iDiferencia < 20)
									clasificacionEdades[1]++;
								else if (iDiferencia < 30)
									clasificacionEdades[2]++;
								else if (iDiferencia < 40)
									clasificacionEdades[3]++;
								else if (iDiferencia < 50)
									clasificacionEdades[4]++;
								else if (iDiferencia < 60)
									clasificacionEdades[5]++;
								else if (iDiferencia < 70)
									clasificacionEdades[6]++;
								else
									clasificacionEdades[7]++;
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
						//nacionalidad
						if (dato.getCodigoNacionalidad() != null) {
							if (dato.getCodigoNacionalidad().equals(
									NAC_ESPANOLA))
								espanoles++;
							else
								extranjeros++;
						}
						//Estudios
						if (dato.getCodigoNivelEstudios() != null) {
							switch (Integer.parseInt(dato
									.getCodigoNivelEstudios())) {
							case EST_NO_LEER_ESCRIBIR_1: {
								nivelEstudios[0]++;
								break;
							}
							case EST_NO_LEER_ESCRIBIR_2: {
								nivelEstudios[1]++;
								break;
							}
							case EST_NO_GRAD_ESCOLAR: {
								nivelEstudios[2]++;
								break;
							}
							case EST_SIN_ESTUDIOS: {
								nivelEstudios[3]++;
								break;
							}
							case EST_PRIMARIA_IMCOMPLETA: {
								nivelEstudios[4]++;
								break;
							}
							case EST_GRADUADO_ESCOLAR: {
								nivelEstudios[5]++;
								break;
							}
							case EST_BACHILLER_ELEMENTAL: {
								nivelEstudios[6]++;
								break;
							}
							case EST_FORMACION_PROFESIONAL1: {
								nivelEstudios[7]++;
								break;
							}
							case EST_BACHILLER_FP2: {
								nivelEstudios[8]++;
								break;
							}
							case EST_FP2: {
								nivelEstudios[9]++;
								break;
							}
							case EST_BUP: {
								nivelEstudios[10]++;
								break;
							}
							case EST_TITULO_MEDIO: {
								nivelEstudios[11]++;
								break;
							}
							case EST_DIPLOMADO: {
								nivelEstudios[12]++;
								break;
							}
							case EST_ING_TEC: {
								nivelEstudios[13]++;
								break;
							}
							case EST_LICENCIADO: {
								nivelEstudios[14]++;
								break;
							}
							case EST_SUPERIORES: {
								nivelEstudios[15]++;
								break;
							}
							case EST_DOCTORADO: {
								nivelEstudios[16]++;
								break;
							}
							}

						}
                        dato=null;
					}

					saveDB(id_parcela, mujeres, hombres, espanoles,
							extranjeros, clasificacionEdades, nivelEstudios);

					logger.debug("Tipo via: " + tipoViaIne + ", Nombre via: "
							+ nombreCalle + ", Numero: " + numero
							+ " Hombres: " + hombres + " Mujeres: " + mujeres
							+ ", De 0 a 10 años: " + clasificacionEdades[0]
							+ ", De 10 a 20 años: " + clasificacionEdades[1]
							+ ", De 20 a 30 años: " + clasificacionEdades[2]
							+ ", De 30 a 40 años: " + clasificacionEdades[3]
							+ ", De 40 a 50 años: " + clasificacionEdades[3]
							+ ", De 50 a 60 años: " + clasificacionEdades[5]
							+ ", De 60 a 70 años: " + clasificacionEdades[6]
							+ ", Mas de 70 años: " + clasificacionEdades[7]
							+ ", Españoles: " + espanoles + ", Extranjeros: "
							+ extranjeros);
					//", Ing. Sup: " + estIngSupLicenciados+ ", Ing Tec: "+ estIngTec+
					//", Diplomados: "+ estDiplomados+ ", Secundarios: "+estSecundarios+
					//", Primarios: "+ estPrimarios+ ", Sin estudios: "+estSinEstudios+
					//", No sabe leer ni escribir: "+estNoSabeLeer);
					String sNivelEstudios="";
					for (int i = 0; i < nivelEstudios.length; i++)
						sNivelEstudios+="["+i + "]"+ nivelEstudios[i]+" ";
					logger.debug("Nivel de estudios:"+sNivelEstudios);
				}

            }
			
		}
        blackboard.put("MensajeEstadistica", mensajeAux.toString());
	    blackboard.put("estgrabadas", new Integer(grabadas));
		blackboard.put("estnograbadas", new Integer(nograbadas));
        hashVias=null;
	}

	/**
	 * Esta funcion busca el id_parcela
	 * El select que ejecuta es
	 select parcela from vias,numeros_policia where (vias.tipoviaine=''NE'' or vias.tipoviaine=?)
	 and (nombreviaine=? or nombrecatastro=?) and (numeros_policia.id_Municipio=?) and (numeros_policia.rotulo=rotulo) and
	 numeros_policia.id_via=vias.id
	 * @param tipoViaIne
	 * @param nombreCalle
	 * @param numero
	 * @return
	 */
	public String buscarParcela(String tipoViaIne, String nombreCalle,
			String numero) { //

		try {
			if (nombreCalle == null) return null;
			nombreCalle = nombreCalle.toUpperCase();
            String[] remplazables = {"A","DE","LOS","EL","LA","EN"};
            nombreCalle= replace(nombreCalle.toUpperCase() ,remplazables);

			Connection conn = AppContext.getApplicationContext()
					.getConnection();
			PreparedStatement s = null;
			ResultSet rs = null;
			s = conn.prepareStatement("buscarParcelaHistograma");
			s.setString(1, (tipoViaIne==null?"%":tipoViaIne));
			s.setString(2, nombreCalle);
			s.setString(3, nombreCalle);
			s.setString(4, AppContext.getApplicationContext().getString(
					"geopista.DefaultCityId"));
			s.setString(5, (numero==null?"%":numero));
			//Si es posible buscamos el numeros sin ceros puedede que llegue 0002 en vez de 02
			try {
				s.setString(6, new Integer(Integer.parseInt(numero))
								.toString());
			} catch (Exception e) {
				s.setString(6, numero);
			}
			rs = s.executeQuery();
			if (rs.next()) {
				return rs.getString("parcela");
			}
			if ((nombreCalle != null) && (nombreCalle.length() > 22)) //En la base de datos no hay valores con mas de 22
			{
				return buscarParcela(tipoViaIne, nombreCalle.substring(0, 20),
						numero);
			}
			if ((nombreCalle != null) && !nombreCalle.startsWith("%"))
				return buscarParcela(tipoViaIne, "%" + nombreCalle + "%",
						numero);
            if (tipoViaIne!=null)
                        return buscarParcela (null,nombreCalle, numero);
            if (numero!=null)
                        return buscarParcela (tipoViaIne,nombreCalle, null);

            try{s.close();}catch(Exception es){}
            try{rs.close();}catch(Exception es){}
            try{conn.close();}catch(Exception es){}

		} catch (Exception e) {
			logger.error("Error al buscar la parcela TipoViaIne: " + tipoViaIne
					+ ", NombreCalle: " + nombreCalle + ", Rotulo: " + numero);
		}

		return "0";
	}

       private String replace(String valor, String[] remplazables)
    {
        String valorFinal=valor.replace(' ','%');
        valorFinal=valorFinal.replace('(','%');
        valorFinal=valorFinal.replace(')','%');
        valorFinal=valorFinal.replace(',','%');

        for (int i=0; i<remplazables.length;i++)
        {
                valorFinal=replace(valorFinal,remplazables[i],'%');
        }
        return valorFinal;
    }
    private String replace(String valor, String remplazable, char espacio)
       {
           String origen=valor.toUpperCase();
           remplazable=remplazable.toUpperCase();
           String remplazado=valor;
           if (origen.indexOf(espacio+remplazable+espacio)>=0)
           {
               remplazado=origen.substring(0,origen.indexOf(espacio+remplazable+espacio))+"%"+
                          origen.substring(origen.indexOf(espacio+remplazable+espacio)+(espacio+remplazable+espacio).length());
               System.out.println("REMPLAZADO="+remplazado);
               origen=remplazado;
           }
           if (origen.indexOf(remplazable+espacio)==0)
           {
               remplazado="%"+origen.substring((remplazable+espacio).length());
               origen=remplazado;
           }
           if (origen.lastIndexOf(espacio+remplazable)>=0)
           {
               if (origen.lastIndexOf(espacio+remplazable)==origen.length()-(espacio+remplazable).length())
               {
                   remplazado=origen.substring(0,origen.length()-(espacio+remplazable).length())+"%";
               }
           }
           return remplazado;
       }

	/**
	 * Salva la estadistica ya calculada en la base de datos.
	 * La sentencia SQL será:
	 insert into histograma  (id_parcela, num_hombres, num_mujeres, num_espanoles, num_extranjeros, ingenierossuperior,
	 ingenierostecnicos, diplomados, estudiosprimarios, estudiossecundarios, sinestudios, noSabeLeerEscribir,
	 de0a20anios, de20a40anios, de40a60anios, masde60anios,id_municipio) values (1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 16078);
	 *
	 * @return
	 */
	public boolean saveDB(String id_parcela, int mujeres, int hombres,
			int espanoles, int extranjeros, int[] clasificacionEdades,
			int[] nivelEstudios) {
		try {
			boolean update=false;
			Connection conn = AppContext.getApplicationContext()
					.getConnection();
			//primero comprobamos si existe o no una estadistica para esa parcela
			PreparedStatement s = null;
			ResultSet rs = null;
			s = conn.prepareStatement("buscaHistograma");
			s.setString(1, id_parcela);
			s.setString(2, AppContext.getApplicationContext().getString(
					"geopista.DefaultCityId"));
			rs = s.executeQuery();
			int insert = 1;
			if (rs.next()) {
				rs.close();
				s.close();
				s = conn.prepareStatement("updateHistograma");
				insert = 1;
				update=true;
				//System.out.println("Actualizacion de histograma");
			} else {
				rs.close();
				s.close();
				s = conn.prepareStatement("insertHistograma");
				s.setString(insert++, id_parcela);
				//System.out.println("Insercion de histograma:"+id_parcela);
			}
			s.setInt(insert++, hombres);
			s.setInt(insert++, mujeres);
			s.setInt(insert++, espanoles);
			s.setInt(insert++, extranjeros);
			for (int i = 0; i < nivelEstudios.length; i++)
				s.setInt(insert++, nivelEstudios[i]);
			for (int j = 0; j < clasificacionEdades.length; j++)
				s.setInt(insert++, clasificacionEdades[j]);
			if (update)
				s.setString(insert++, id_parcela);
			else
				s.setString(insert++, AppContext.getApplicationContext()
						.getString("geopista.DefaultCityId"));
			System.out.println("Valor de insert:"+insert);
			s.execute();
			s.close();
			conn.close();
		} catch (Exception ex) {
			logger.error("Error al salvar la estadistica en base de datos", ex);
			return false;
		}
		return true;
	}

	/**
	 * Borra las anteriores estadisticas para el municipio
	 * @return
	 */
	public boolean delete() {
        if (!init.booleanValue()) return true;
		try {
			Connection conn = AppContext.getApplicationContext()
					.getConnection();
			//primero comprobamos si existe o no una estadistica para esa parcela
			PreparedStatement s = null;
			s = conn.prepareStatement("deleteHistograma");
			s.setString(1, AppContext.getApplicationContext().getString(
					"geopista.DefaultCityId"));
			s.execute();
			s.close();
			conn.close();
		} catch (Exception ex) {
			logger.error("Error al borrar las estadistica del municipio", ex);
			return false;
		}
		return true;

	}


}
