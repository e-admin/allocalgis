/**
 * SQLParserPostGIS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.server.administrador.init.Constantes;
import com.vividsolutions.jts.geom.Geometry;



/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 19-oct-2005
 * Time: 11:33:43
 * To change this template use File | Settings | File Templates.
 */
public class SQLParserPostGIS {

    private Connection conn=null;
    private NewSRID srid=null;
    private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SQLParser.class);

    public SQLParserPostGIS(Connection conn, NewSRID srid){
        this.conn=conn;
        this.srid=srid;
    }

    /** Sirve para los insert, update y delete. */
    public PreparedStatement[] newUpdate(int iMunicipio,String sSQL, IACLayer schema, ACFeatureUpload featureUpload, Integer srid_destino, int sridDefecto, int sridInicial) throws SQLException, ACException, FileNotFoundException, IOException{
    	//Introduzco el parámetro L para la función length(geometry)
        String sQueries[]=sSQL.split(";");
        PreparedStatement[] apsRet=new PreparedStatement[sQueries.length];
        Pattern pattern=Pattern.compile("\\?(M|S|L|T|\\d+)");
        ArrayList alParams=new ArrayList();
        ArrayList alTypes=new ArrayList();
        Hashtable htAtts=schema.getAttributes();
      
        for(int i=0;i<sQueries.length;i++){
            logger.info(sQueries[i]);
            Matcher matcher=pattern.matcher(sQueries[i]);
            
            //HGH: Se limpian los parámetros y los tipos para tener en cuenta los
            //casos en los que existen varias queries SQL separadas por ;
            //Si no se limpian los tipos, se estarán asignando los de la primera query
            //para todas las demás
            alParams.clear();
            alTypes.clear();
            
            while(matcher.find()){
                String sMatch=matcher.group().substring(1);
                if (sMatch.equals("M")){
                    alParams.add(new Integer(iMunicipio));
                    alTypes.add(new Integer(IACLayer.TYPE_NUMERIC));
                }else if (sMatch.equals("S")){
//                    alParams.add(new Integer(srid.getSRID(iMunicipio)));
                    alParams.add(srid_destino);
                    alTypes.add(new Integer(IACLayer.TYPE_NUMERIC));
                }else if (sMatch.equals("T")) {
                    alParams.add(sridDefecto);                    
                    alTypes.add(new Integer(IACLayer.TYPE_NUMERIC));      
                }else if (sMatch.equals("L")) {
                    alParams.add(sridInicial);                    
                    alTypes.add(new Integer(IACLayer.TYPE_NUMERIC));      
                }else{
                    int iIndex = Integer.parseInt(sMatch);
                    if (featureUpload.getAttValues().length<iIndex)
                        throw new ACException("No value specified for parameter: "+iIndex);
                    Object valorAtributo = featureUpload.getAttValues()[iIndex-1];
                    ACAttribute acAtt=(ACAttribute)htAtts.get(new Integer(iIndex));
                    alTypes.add(new Integer(acAtt.getColumn().getType()));
                    if (((Integer)acAtt.getColumn().getType() == IACLayer.TYPE_NUMERIC) && (valorAtributo != null)) //Integer
                    	try{ 
                            alParams.add(Integer.parseInt(featureUpload.getAttValues()[iIndex-1].toString())); 
                       }catch(Exception e){ 
                    	   try{
                            alParams.add(Double.parseDouble(featureUpload.getAttValues()[iIndex-1].toString())); 
                    	   }catch(Exception e1){
                    		   alParams.add(featureUpload.getAttValues()[iIndex-1]);
                    	   }
                       }
                    else{
                    	if (featureUpload.getAttValues()[iIndex-1] instanceof String && ((Integer)acAtt.getColumn().getType() == IACLayer.TYPE_GEOMETRY) && ((String)featureUpload.getAttValues()[iIndex-1]).endsWith(" EMPTY")){
                    		alParams.add(null);
                    	}else
                    		alParams.add(featureUpload.getAttValues()[iIndex-1]);
                    }
                }
            }
            apsRet[i]=this.conn.prepareStatement(matcher.replaceAll("?"));
            int iIndex=0;
            for (Iterator it=alParams.iterator();it.hasNext();){
                ++iIndex;
                Object oValue=it.next();
                if ( oValue==null ||
                    (oValue instanceof java.lang.String && oValue.equals(""))){ // ""=No rellenado -> insertamos Null
                    SQLParser.setNullColumn(alTypes,apsRet[i],iIndex);
                    //logger.info("param "+iIndex+": NULL");
                } else{
                    if (oValue instanceof java.util.Date) {
                        oValue = new java.sql.Date(((java.util.Date)oValue).getTime());                        
                    }
                    apsRet[i].setObject(iIndex,oValue);
                    //logger.info("param "+iIndex+": " + oValue.toString());
                }
            }
        }
        
        for(int i=0;i<apsRet.length;i++){
      	  try {
    			logger.info("SQL Ejecutando query Filled: "+((org.postgresql.PGStatement)apsRet[i]).toString());
    		} catch (Exception e) {}

      }
        return apsRet;
    }

    /** Sirve para los insert, update y delete. */
    public PreparedStatement[] newUpdate(List listaMunicipios,String sSQL, IACLayer schema, ACFeatureUpload featureUpload, Integer srid_destino, int idMunicipio, int sridDefecto, int sridInicial) throws SQLException, ACException, FileNotFoundException, IOException{
    	//Introduzco el parámetro L para la función length(geometry)
        String sQueries[]=sSQL.split(";");
        PreparedStatement[] apsRet=new PreparedStatement[sQueries.length];
        Pattern pattern=Pattern.compile("\\?(M|S|T|L|\\d+)");
        ArrayList alParams=new ArrayList();
        ArrayList alTypes=new ArrayList();
        Hashtable htAtts=schema.getAttributes();

        for(int i=0;i<sQueries.length;i++){
            logger.info(sQueries[i]);
            Matcher matcher=pattern.matcher(sQueries[i]);
            
            //HGH: Se limpian los parámetros y los tipos para tener en cuenta los
            //casos en los que existen varias queries SQL separadas por ;
            //Si no se limpian los tipos, se estarán asignando los de la primera query
            //para todas las demás
            alParams.clear();
            alTypes.clear();
            
            //Si alguno de los atributos es el municipio, obtengo su valor
        	String iMunicipio = getIdMunicipio(featureUpload,htAtts, idMunicipio);

            if (!iMunicipio.equals("-1") && !comprobarMunicipioLista(listaMunicipios, iMunicipio))
                throw new ACException("El municipio de la feature no pertenece a ninguno de los municipios asociados al usuario");
            
            while(matcher.find()){
                String sMatch=matcher.group().substring(1);
                if (sMatch.equals("M")){
                    alParams.add(new Integer(iMunicipio));
                    alTypes.add(new Integer(IACLayer.TYPE_NUMERIC));
                }else if (sMatch.equals("S")){
//                    alParams.add(new Integer(srid.getSRID(Integer.parseInt(iMunicipio))));
                    alParams.add(srid_destino);                    
                    alTypes.add(new Integer(IACLayer.TYPE_NUMERIC));
                }else if (sMatch.equals("T")) {
                    alParams.add(sridDefecto);                    
                    alTypes.add(new Integer(IACLayer.TYPE_NUMERIC));                    
                }else if (sMatch.equals("L")) {
                    alParams.add(sridInicial);                    
                    alTypes.add(new Integer(IACLayer.TYPE_NUMERIC));                    
                }else{
                    int iIndex2 = Integer.parseInt(sMatch);
                    if (featureUpload.getAttValues().length<iIndex2)
                        throw new ACException("No value specified for parameter: "+iIndex2);
                    alParams.add(featureUpload.getAttValues()[iIndex2-1]);
                    ACAttribute acAtt=(ACAttribute)htAtts.get(new Integer(iIndex2));
                    alTypes.add(new Integer(acAtt.getColumn().getType()));
                }
            }
            apsRet[i]=this.conn.prepareStatement(matcher.replaceAll("?"));
            int iIndex=0;
            for (Iterator it=alParams.iterator();it.hasNext();){
                ++iIndex;
                Object oValue=it.next();
                if ( oValue==null ||
                    (oValue instanceof java.lang.String && oValue.equals(""))){ // ""=No rellenado -> insertamos Null
                    SQLParser.setNullColumn(alTypes,apsRet[i],iIndex);
                    
                    //logger.info("param "+iIndex+": NULL");
                } else{
                    if (oValue instanceof java.util.Date) {
                        oValue = new java.sql.Date(((java.util.Date)oValue).getTime());
                        apsRet[i].setObject(iIndex,oValue);
                    } 
                    else if (oValue instanceof java.lang.Double){
                    	double douValue = Double.parseDouble(oValue.toString().replace(',', '.'));                   		
                    	apsRet[i].setDouble(iIndex, douValue);
                    }else if (oValue instanceof java.lang.Integer){
                    	int intValue = Integer.parseInt(oValue.toString()); 
                    	apsRet[i].setInt(iIndex, intValue);     
                    } else if (oValue instanceof java.lang.String){
                    	String ovalueString=(String)oValue;
                    	if(!ovalueString.startsWith("0")){//Evita transformar los string que empiezan por 0. Muchos identificadores de nucleos empiezan así
	                    	try{                    	
		                    	int intValue = Integer.parseInt(oValue.toString()); 
		
		                    	apsRet[i].setInt(iIndex, intValue);                                    
	                    	}catch(Exception e1){
	                    		try{                    			
	                    			
	                    			 if (isDouble(oValue.toString())){
		                    				double douValue = Double.parseDouble(oValue.toString().replace(',', '.'));                   		
		                    				apsRet[i].setDouble(iIndex, douValue);
	                    	    	   }
	                    	    	   else{
	                    	    		   apsRet[i].setObject(iIndex,oValue);
	                    	    	   }
	                    			 	                    			
	                    		}catch(Exception e2){   	
	                    			apsRet[i].setObject(iIndex,oValue);
	                    		}
	                    	}
                    	}else
                    		 apsRet[i].setObject(iIndex,ovalueString);
                    //logger.info("param "+iIndex+": " + oValue.toString());                 
                    }else{
                    apsRet[i].setObject(iIndex,oValue);
                    }
                }
            }
        }
        
        for(int i=0;i<apsRet.length;i++){
        	  try {
      			logger.info("SQL Ejecutando query Filled: "+((org.postgresql.PGStatement)apsRet[i]).toString());
      		} catch (Exception e) {}

        }
      
        
        return apsRet;
    }
    
    private boolean comprobarMunicipioLista(List listaMunicipios, String idMunicipio){
    	Iterator itMunicipios = listaMunicipios.iterator();
    	int iMunicipio = Integer.parseInt(idMunicipio);
    	while (itMunicipios.hasNext()){
    		Municipio municipio = (Municipio)itMunicipios.next();
    		if (municipio.getId() == iMunicipio)
    			return true;
    	}
    	return false;
    }

    public PreparedStatement newSelect(String idEntidad, String iMunicipio,String sSQL, IACLayer schema,Geometry geometry, 
    		FilterNode fn, Integer srid_destino, org.postgis.Geometry geometryEntidad)
            throws SQLException, ACException, FileNotFoundException,IOException{
        String sPattern="\\?[MGTX]";
        StringBuffer sbSQL=new StringBuffer(sSQL);
        int iOffset=sSQL.toUpperCase().indexOf(" GROUP BY");
        if (iOffset==-1)
            iOffset=sSQL.length();
        if (fn!=null)
            sbSQL.insert(iOffset," and "+fn.toSQL());
        // Hay que ponerlo como texto para usar el operador '&&' en PostGIS
        //"\"GEOMETRY\" && GeometryFromText(?,23030) and";
        List lFilterParams=new ArrayList();
        Pattern pattern=Pattern.compile(sPattern);
        Matcher matcher=pattern.matcher(sSQL);
    	String stBinds = "";
        while(matcher.find()){
            String sChar=matcher.group().substring(1,2).toUpperCase();
            if (sChar.equals("G"))
                lFilterParams.add(geometry!=null?(Object)geometry.toText():new Boolean(true));
            else if (sChar.equals("X")){            	
            	StringBuffer noSRIDgeometry=new StringBuffer();
            	geometryEntidad.outerWKT(noSRIDgeometry);
                lFilterParams.add(geometryEntidad!=null?(Object)noSRIDgeometry:new Boolean(true));
            }
            else if (sChar.equals("M")){
            	StringTokenizer st = new StringTokenizer(iMunicipio,",");
            	while (st.hasMoreTokens()){
            		lFilterParams.add(Integer.parseInt(st.nextToken()));
            		if (stBinds.equals(""))
            			stBinds += "?";
            		else
        				stBinds += ",?";
            	}
            }
            else if (sChar.equals("T"))
                lFilterParams.add(srid_destino);
        }
        if (fn!=null)
            fn.values(lFilterParams);
        
		//No nos está cogiendo bien el srid. Lo metemos en una constante        
		//SRIDDefecto sridDefecto = new SRIDDefecto(Const.SRID_DEFECTO);
		String sGeomFilter="";
		String sGeomFilterEntidad="";
		//No nos está cogiendo bien el srid. Lo metemos en una constante
//		if (schema != null){
//			sGeomFilter="intersects("+(geometry==null? "?" // Se sustituirá por "true"
//                          :schema.findPrimaryTable()+".\"GEOMETRY\" , GeometryFromText(?,"+sridDefecto.getSRID()+"))");
//			sGeomFilterEntidad="intersects("+(geometryEntidad==null? "?" // Se sustituirá por "true"
//                    :schema.findPrimaryTable()+".\"GEOMETRY\" , GeometryFromText(?,"+sridDefecto.getSRID()+"))");
//		}
		//Lo de arriba pero utilizando una constante para el srid
		if (schema != null){
			sGeomFilter="intersects("+(geometry==null? "?" // Se sustituirá por "true"
                          :schema.findPrimaryTable()+".\"GEOMETRY\" , GeometryFromText(?,"+Const.SRID_POR_DEFECTO+"))");
			sGeomFilterEntidad="intersects("+(geometryEntidad==null? "?" // Se sustituirá por "true"
                    :schema.findPrimaryTable()+".\"GEOMETRY\" , GeometryFromText(?,"+Const.SRID_POR_DEFECTO+"))");
		}
        sbSQL = new StringBuffer(sbSQL.toString().replaceAll("\\?G",sGeomFilter).replaceAll("\\?[GT]","?"));
        sbSQL = new StringBuffer(sbSQL.toString().replaceAll("\\?X",sGeomFilterEntidad).replaceAll("\\?[TX]","?"));
        
        sbSQL = new StringBuffer(sbSQL.toString().replaceAll("\\?[M]",stBinds));
        
        if (geometry != null){
            iOffset = sbSQL.toString().toUpperCase().indexOf(" GROUP BY");
            if (iOffset == -1)
                iOffset = sbSQL.length();
            sbSQL.insert(iOffset," and "+sGeomFilter);
        }
        else if (geometryEntidad!=null){
        	 iOffset = sbSQL.toString().toUpperCase().indexOf(" GROUP BY");
             if (iOffset == -1)
                 iOffset = sbSQL.length();
             sbSQL.insert(iOffset," and "+sGeomFilterEntidad);
        }
        
        //Para pruebas 
        if (Constantes.TEST_MODE)
        	sbSQL.append(" LIMIT 200");
        
        PreparedStatement psRet=this.conn.prepareStatement(sbSQL.toString());
        int iIndex=0;
        for (Iterator it=lFilterParams.iterator();it.hasNext();)
            psRet.setObject(++iIndex,it.next());
        if (geometry != null){
            psRet.setString(++iIndex,geometry.toText());
        }
        if (geometryEntidad != null){
        	StringBuffer noSRIDgeometry=new StringBuffer();
        	geometryEntidad.outerWKT(noSRIDgeometry);
            psRet.setString(++iIndex,noSRIDgeometry.toString());
        }
        
		logger.info("SQL Ejecutando query Filled: "+((org.postgresql.PGStatement)psRet).toString());
        
        return psRet;
    }

    //Si alguno de los atributos es el municipio, obtengo su valor
    public String getIdMunicipio(ACFeatureUpload featureUpload, Hashtable htAtts, int idMunicipio){
		Enumeration enAtt = htAtts.elements();
		int iIndex = 1;
		String iMunicipio = "-1";
		boolean geometriaConMunicipio = false;
		while (enAtt.hasMoreElements()){
	        ACAttribute acAtt=(ACAttribute)enAtt.nextElement();
	        if (acAtt != null){
	        	ACDomain domain = (ACDomain) acAtt.getColumn().getDomain();
	        	if(domain!=null){
	                String patron = domain.getDomainNode().getPatron();
	                if (patron!= null && patron.contains(Const.KEY_ID_MUNI)){
	                	geometriaConMunicipio = true;
	                	iIndex = acAtt.getPosition();
	                    iMunicipio = featureUpload.getAttValues()[iIndex-1].toString();
	                	break;
	                }
	        	}
	        }
		}
		if (iMunicipio.equals("-1"))
			iMunicipio = String.valueOf(idMunicipio);
		return iMunicipio;
	}
    
    
    //Verificamos si es un double
    //Ver http://docs.oracle.com/javase/6/docs/api/java/lang/Double.html#valueOf(java.lang.String)
    //Como doble en mi caso solo contemplo estos casos 20.3
    //Si viene 20D (20Double) lo marcomo como String
    //Si viene 20F (20Float) lo marcomo como String
    //Si viene 20E11 (20Exponente) lo marcomo como String
    //Obviamente todas son representaciones validas pero en nuestro caso no queremos contemplarlas.
    public static boolean isDouble(String text){
    	
		final String Digits     = "(\\p{Digit}+)";
		final String HexDigits  = "(\\p{XDigit}+)";
		  // an exponent is 'e' or 'E' followed by an optionally 
		  // signed decimal integer.
		//final String Exp        = "[eE][+-]?"+Digits;
		final String fpRegex    = "("+Digits+"(\\.)+("+Digits+"+))";

		/*final String fpRegex    =
		      ("[\\x00-\\x20]*"+  // Optional leading "whitespace"
		   "[+-]?(" + // Optional sign character
		   "NaN|" +           // "NaN" string
		   "Infinity|" +      // "Infinity" string*/
		
		   // A decimal floating-point string representing a finite positive
		   // number without a leading sign has at most five basic pieces:
		   // Digits . Digits ExponentPart FloatTypeSuffix
		   // 
		   // Since this method allows integer-only strings as input
		   // in addition to strings of floating-point literals, the
		   // two sub-patterns below are simplifications of the grammar
		   // productions from the Java Language Specification, 2nd 
		   // edition, section 3.10.2.
		
		   // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
		   //"((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+
		
		   // . Digits ExponentPart_opt FloatTypeSuffix_opt
		   //"(\\.("+Digits+")("+Exp+")?)|"+
		
		 // Hexadecimal strings
		 /*"((" +
		  // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
		  "(0[xX]" + HexDigits + "(\\.)?)|" +*/
		
		  // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
		  /*"(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +*/
		
		  /*")[pP][+-]?" + Digits + "))" +
		   "[fFdD]?))" +
		   "[\\x00-\\x20]*");// Optional trailing "whitespace"*/
		
		if (Pattern.matches(fpRegex, text))
            return true;
        else 
            return false;
        
    }
    
    public static void main (String args[]){
    	
    	try {
    	   Object oValue="2.022";
    	   
    	   if (isDouble(oValue.toString())){
    		   double douValue=Double.valueOf(oValue.toString());
   				System.out.println("douValue:"+douValue);
    	   }
    	   else{
    			System.out.println("String Value:"+oValue.toString());
    	   }
	          
    	   //int intValue = Integer.parseInt(oValue.toString()); 
    	   //System.out.println("intValue"+intValue);
			//double douValue = Double.parseDouble(oValue.toString().replace(',', '.'));
			//double douValue=Double.valueOf(oValue.toString());
			//System.out.println("douValue"+douValue);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     	
    }

}
