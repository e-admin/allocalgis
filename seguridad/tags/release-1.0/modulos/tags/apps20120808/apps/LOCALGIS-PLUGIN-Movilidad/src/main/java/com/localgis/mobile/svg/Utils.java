package com.localgis.mobile.svg;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class Utils {
	
	/**
	 * Codifica una cadena de caracteres en UTF-8
	 * @param src
	 * @return
	 */
    public static String parseUTF8(String src) {
//    	String result = null;
//		try {
//			result = new String(line.getBytes("UTF-8"));
//		} catch (UnsupportedEncodingException e) {}
//		return result;
    	CharSequence chsec = src;
    	String s = "";
    	int v = 0;
    	for (int i = 0; i < chsec.length(); i++) {
    		v = chsec.charAt(i);
    		if(v<128){
    			s = s + chsec.charAt(i);
    		}
    		//a partir del carácter 128 pondremos la codificación en XML
    		else {
    			s = s + "&#"+v+";";
    		}
		}
    	
    	return s;
    }
    
    /**
     * Transforma una cadena de UTF8 a LATIN.
     * @param src
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String parseFromUtf8ToLatin(String src) throws UnsupportedEncodingException{
    	String out = new String(src.getBytes("8859_1"), "utf-8");
    	return out;
    }

	/**
	 * Codifica una cadena de caracteres en entidades HTML
	 * @param line
	 * @return
	 */
    public static String parseHTML(String line) {
    	String result = line.replace("<", "&lt;");
        result = result.replace("<","&lt;");
        result = result.replace(">", "&gt;");
        result = result.replace("<", "&lt;");
        result = result.replace("'", "&quot;");
        result = result.replace("ñ", "&ntilde;");
        result = result.replace("Ñ", "&Ntilde;");
        result = result.replace("Ç", "&Ccedil;");
        result = result.replace("ç", "&ccedil;");
        
        result = result.replace("á", "&aacute;");
        result = result.replace("é", "&eacute;");       
        result = result.replace("í", "&iacute;");     
        result = result.replace("ó", "&oacute;");     
        result = result.replace("ú", "&uacute;");
        
        result = result.replace("Á", "&Aacute;");
        result = result.replace("É", "&Eacute;");       
        result = result.replace("Í", "&Iacute;");     
        result = result.replace("Ó", "&Oacute;");     
        result = result.replace("Ú", "&Uacute;");     
        
        result = result.replace("à", "&agrave;");
        result = result.replace("è", "&egrave;");       
        result = result.replace("ì", "&igrave;");     
        result = result.replace("ò", "&ograve;");     
        result = result.replace("ù", "&ugrave;");     
        
        result = result.replace("À", "&Agrave;");
        result = result.replace("È", "&Egrave;");       
        result = result.replace("Ì", "&Igrave;");     
        result = result.replace("Ò", "&Ograve;");     
        result = result.replace("Ù", "&Ugrave;");         
        
        return result;
    }
    
	/**
	 * Redondea un double a nDec decimales
	 * @param nD
	 * @param nDec
	 * @return
	 */
	public static double redondear(double nD, int nDec){  
	  return Math.round(nD*Math.pow(10,nDec))/Math.pow(10,nDec);  
	} 
	
}
