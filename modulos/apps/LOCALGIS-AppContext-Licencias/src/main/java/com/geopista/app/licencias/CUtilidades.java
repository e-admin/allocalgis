/**
 * CUtilidades.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.licencias;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.geopista.app.licencias.estructuras.Estructuras;
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
            String aux="";
            for (int i=0; i<s.length();i++)
            {
              if (s.charAt(i)!=' ')
                 aux+=s.charAt(i);
            }
            s=aux;
			new Integer(s).intValue();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean esDate(String fecha) {
		try {
			Date date = CConstantesLicencias.df.parse(fecha);
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
				return CConstantesLicencias.df.parse(fecha);
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

	public static String formatFechaH24(Date fecha) {
		if (fecha == null) {
			return "";
		}        
		return new SimpleDateFormat("dd/MM/yyyy H:mm:ss").format(fecha);
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
                    s+= ", " + s3.trim();
                }else{
                    s= s.trim();
                }
            }
        }else if (s3 != null){
           s= s3.trim();
        }

        return s.trim().toUpperCase();
    }

    public static String getEmplazamiento(CSolicitudLicencia solicitud, ResourceBundle literales){

        String emplazamiento= "";

        if (solicitud != null){
            // Comprobamos que la solicitud tenga una direccion de emplazamiento
            String tipoVia= "";
            try{
                //tipoVia= Estructuras.getListaTiposViaINE().getDomainNode(solicitud.getTipoViaAfecta()).getTerm(literales.getLocale().toString());
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
                                tipoVia= Estructuras.getListaTiposViaINE().getDomainNode(refCatastral.getTipoVia()).getTerm(literales.getLocale().toString());
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


    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
    




}
