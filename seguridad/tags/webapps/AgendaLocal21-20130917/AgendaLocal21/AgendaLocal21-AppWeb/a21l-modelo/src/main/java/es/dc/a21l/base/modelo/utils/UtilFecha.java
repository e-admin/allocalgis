/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.modelo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UtilFecha {

    private UtilFecha() {
    }
    private static Date intentaParsear(String entrada, SimpleDateFormat format) {
        Date fecha  = null;
        try {
            fecha = format.parse(entrada);
        } catch (ParseException e) {
        	return null;
        }
        return fecha;
    }

    public static Date multiParse(String entrada)  {
        if (entrada == null || entrada.equals(""))
        	return null;
    	Date fecha = null;
        List<SimpleDateFormat> formatosAceptados = new ArrayList<SimpleDateFormat>();
        formatosAceptados.add(new SimpleDateFormat("dd/MM/yy"));
    	formatosAceptados.add(new SimpleDateFormat("dd/MM/yyyy"));
    	formatosAceptados.add(new SimpleDateFormat("dd-MM-yy"));
    	formatosAceptados.add(new SimpleDateFormat("dd-MM-yyyy"));            
    	
    	formatosAceptados.add(new SimpleDateFormat("dd/MM/yy HH:mm:ss"));
    	formatosAceptados.add(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
    	formatosAceptados.add(new SimpleDateFormat("dd-MM-yy HH:mm:ss"));
    	formatosAceptados.add(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
    	
    	formatosAceptados.add(new SimpleDateFormat("MM/dd/yy"));
    	formatosAceptados.add(new SimpleDateFormat("MM/dd/yyyy"));
    	formatosAceptados.add(new SimpleDateFormat("MM-dd-yy"));
    	formatosAceptados.add(new SimpleDateFormat("MM-dd-yyyy"));            
    	
    	formatosAceptados.add(new SimpleDateFormat("MM/dd/yy HH:mm:ss"));
    	formatosAceptados.add(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"));
    	formatosAceptados.add(new SimpleDateFormat("MM-dd-MM-yy HH:mm:ss"));
    	formatosAceptados.add(new SimpleDateFormat("MM-dd-yyyy HH:mm:ss"));
    	
    	formatosAceptados.add(new SimpleDateFormat("yy/dd/MM"));
    	formatosAceptados.add(new SimpleDateFormat("yyyy/dd/MM"));
    	formatosAceptados.add(new SimpleDateFormat("yy-dd-MM"));
    	formatosAceptados.add(new SimpleDateFormat("yyyy-dd-MM"));       
    	
    	formatosAceptados.add(new SimpleDateFormat("yy/dd/MM HH:mm:ss"));
    	formatosAceptados.add(new SimpleDateFormat("yyyy/dd/MM HH:mm:ss"));
    	formatosAceptados.add(new SimpleDateFormat("yy-dd-MM HH:mm:ss"));
    	formatosAceptados.add(new SimpleDateFormat("yyyy-dd-MM HH:mm:ss"));
    	
        for (SimpleDateFormat formato : formatosAceptados) {
            fecha = intentaParsear(entrada, formato);
            if (fecha != null) break;
        }
        return fecha;
    }
}