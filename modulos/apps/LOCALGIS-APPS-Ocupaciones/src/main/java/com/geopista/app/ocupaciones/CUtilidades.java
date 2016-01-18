/**
 * CUtilidades.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.ocupaciones;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.protocol.licencias.CReferenciaCatastral;
import com.geopista.protocol.licencias.CSolicitudLicencia;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 19-jul-2004
 * Time: 13:08:34
 * To change this template use File | Settings | File Templates.
 */
public class CUtilidades {

	static Logger logger = Logger.getLogger(CUtilidades.class);

	public static boolean esNumerico(String s) {
		try {
			new Integer(s).intValue();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean esDate(String fecha) {
		try {
			Date date = CConstantesOcupaciones.df.parse(fecha);
			logger.debug("Fecha Original: " + fecha);
			logger.debug("Parsed date: " + date.toString());

		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	public static Date getDate(String fecha) {
		try {
			if (esDate(fecha))
				return CConstantesOcupaciones.df.parse(fecha);
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static Date getDateH24(String fecha) {
		try {
			if (esDate(fecha))
				return new SimpleDateFormat("dd/MM/yyyy H:mm:ss").parse(fecha);
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static String formatFecha(Date fecha) {
		if (fecha == null) {
			return "";
		}
		return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
	}


    public static String formatHora(Date hora) {
        if (hora == null) {
            return "";
        }
        return new SimpleDateFormat("H:mm").format(hora);
    }

	public static String formatFechaH24(Date fecha) {
		if (fecha == null) {
			return "";
		}        
		return new SimpleDateFormat("dd/MM/yyyy H:mm:ss").format(fecha);
	}

	public static Date parseFecha(String fecha) {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
		} catch (ParseException ex) {
			return null;
		}
	}


    public static Date parseFechaH24(String fecha) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy H:mm:ss").parse(fecha);
        } catch (ParseException ex) {
            return null;
        }
    }

	public static boolean excedeLongitud(String campo, int maxLengthPermitida) {
		if (campo.length() > maxLengthPermitida)
			return true;
		else
			return false;
	}

	public static String getFormatoYYYYMMDD(String fecha) throws Exception {
		java.util.Date date;
		date = (new SimpleDateFormat("dd/MM/yyyy")).parse(fecha);
		return new SimpleDateFormat("yyyy/MM/dd").format(date).toString();

	}

	public static String showToday() {
		Calendar cal = new GregorianCalendar();
		Locale locale = Locale.getDefault();
		cal = Calendar.getInstance(TimeZone.getTimeZone(locale.toString()));
        /*
		System.out.println("locale.toString()="+locale.toString());
		System.out.println("locale.getCountry()="+locale.getCountry());
		System.out.println("YEAR="+cal.get(Calendar.YEAR));
		System.out.println("MONTH="+cal.get(Calendar.MONTH));
		System.out.println("DAY="+cal.get(Calendar.DAY_OF_MONTH));
		System.out.println("HOUR_OF_DAY="+cal.get(Calendar.HOUR_OF_DAY));
        System.out.println("HOUR="+cal.get(Calendar.HOUR));
		System.out.println("MINUTOS="+cal.get(Calendar.MINUTE));
		System.out.println("SEGUNDOS="+cal.get(Calendar.SECOND));
        */
		int anno = cal.get(Calendar.YEAR);
		int mes = cal.get(Calendar.MONTH) + 1; // 0 == Enero
		int dia = cal.get(Calendar.DAY_OF_MONTH);
        int hora = cal.get(Calendar.HOUR);
		int minutos = cal.get(Calendar.MINUTE);
		int segundos = cal.get(Calendar.SECOND);

		String sMes = "";
		String sDia = "";
		if (mes < 10)
			sMes = "0" + mes;
		else
			sMes = "" + mes;
		if (dia < 10)
			sDia = "0" + dia;
		else
			sDia = "" + dia;

		return sDia + "/" + sMes + "/" + anno + " " + hora + ":" + minutos + ":" + segundos;

	}

	public static boolean esDouble(String s) {
		try {
			new Double(s).doubleValue();
		} catch (Exception ex) {
			return false;
		}
		return true;
	}


    public static Date parseFechaStringToDate(String fecha) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
        } catch (ParseException ex) {
            return null;
        }
    }

    public static String parseFechaStringToString(String fecha) {
        try {
            String s = "";
            if (fecha.trim().length() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date d = (Date) sdf.parse(fecha);
                s = sdf.format(d);
            }
            return s;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String componerCampo(String s1, String s2, String s3){
        String s= "";
        if(s1 != null){
            if (s1.trim().length() > 0){
                s+= s1.trim();
            }
        }
        if (s.trim().length() > 0) s+= " ";
        if (s2 != null){
            if (s2.trim().length() > 0){
                s+= s2.trim();
            }else{
                s= s.trim();
            }
        }
        if (s.trim().length() > 0){
            if (s3 != null){
                if (s3.trim().length() > 0){
                    s+= ", "+ s3.trim();
                }else{
                    s= s.trim();
                }
            }
        }else if (s3 != null){            
           s= s3.trim();
        }

        return s.trim().toUpperCase();
    }


    public static String getEmplazamiento(CSolicitudLicencia solicitud){
        String emplazamiento= "";

        if (solicitud != null){
            // Comprobamos que la solicitud tenga una direccion de emplazamiento
            String tipoVia= "";
            try{
                //tipoVia= Estructuras.getListaTiposViaINE().getDomainNode(solicitud.getTipoViaAfecta()).getTerm(CMainOcupaciones.currentLocale.toString());
                tipoVia= solicitud.getTipoViaAfecta();
            }catch(Exception e){}
            emplazamiento= CUtilidades.componerCampo(tipoVia, solicitud.getNombreViaAfecta(), solicitud.getNumeroViaAfecta());
            if (emplazamiento.trim().length() == 0){
                // si no la tiene, recogemos una de las referencias catastrales
                Vector vRef= solicitud.getReferenciasCatastrales();
                if (vRef != null){
                    boolean encontrado= false;
                    Enumeration e = vRef.elements();
                    while ((e.hasMoreElements()) && (!encontrado)){
                        CReferenciaCatastral refCatastral= (CReferenciaCatastral)e.nextElement();
                        if (refCatastral != null){
                            tipoVia= "";
                            try{
                                tipoVia= Estructuras.getListaTiposViaINE().getDomainNode(refCatastral.getTipoVia()).getTerm(CMainOcupaciones.currentLocale.toString());
                            }catch(Exception ex){}
                            emplazamiento= CUtilidades.componerCampo(tipoVia, refCatastral.getNombreVia(), refCatastral.getPrimerNumero());
                            if (emplazamiento.trim().length() > 0) encontrado= true;
                        }
                    };
                }
            }
        }

        return emplazamiento.trim().toUpperCase();

    }


    public static void resizeColumn(TableColumn column, int size){
        column.setMinWidth(size);
        column.setMaxWidth(size*2);
        column.setWidth(size);
        column.setPreferredWidth(size);
    }




}
