package com.geopista.app.ocupaciones;

import org.apache.log4j.Logger;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.geopista.app.ocupaciones.CConstantesOcupaciones;
import com.geopista.app.ocupaciones.Estructuras;
import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.geopista.protocol.licencias.CReferenciaCatastral;

import javax.swing.table.TableColumn;

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
