package com.gestorfip.ws.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.apache.log4j.Logger;


public class UtilsWS {
	
	private final static int TAMANIO_CODIGO = 10;
	static Logger logger = Logger.getLogger(UtilsWS.class);

	public static String completarCodigoConCeros(String codigo){
		
		if(codigo.length()<10){
			String ceroIzquierda = "0";
			ceroIzquierda=ceroIzquierda.concat(codigo);
			codigo = completarCodigoConCeros(ceroIzquierda);
		}
		return codigo;
	}

	
	public static String completarConCerosIzq(String value, int tam){
		
		if(value.length()<tam){
			String ceroIzquierda = "0";
			ceroIzquierda=ceroIzquierda.concat(value);
			value = completarConCerosIzq(ceroIzquierda, tam);
		}
		return value;
	}	
	public static boolean writeFicheroFIP1(String xml, String nameFicheroFip){
		
		boolean state = true;
		
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
        	FileWriter w = new FileWriter(nameFicheroFip);
        	BufferedWriter bw = new BufferedWriter(w);
        	PrintWriter wr = new PrintWriter(bw);  
        	wr.write(xml);     	

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("No se ha podido guardar el fichero; "+nameFicheroFip+" -- "+e.getMessage());
            state= false;
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
              logger.error("No se ha podido cerrar el fichero; "+nameFicheroFip+" -- "+e2.getMessage());
              state= false;
           }
        }
	    
		return state;
		
	}
	

	public static boolean validaFechaMascara(String sFecha, String sMascara)
    {
        boolean retorno = false;
        try {
            //Convertir la fecha al Calendar
            java.util.Locale locInstancia = new java.util.Locale("es","ES");
            java.text.DateFormat dfInstancia;
            java.util.Date dInstancia;
            dfInstancia = new java.text.SimpleDateFormat(sMascara,locInstancia);
            dInstancia = (java.util.Date)dfInstancia.parse(sFecha);
            java.util.Calendar cal = java.util.Calendar.getInstance(locInstancia);
            cal.setTime(dInstancia); //setear la fecha en cuestion al calendario
            retorno = true;
        } catch (java.text.ParseException excep) {
            retorno = false; //unparseable date
        } finally {
            return retorno;
        }
    }


}
