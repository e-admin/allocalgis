/**
 * OperadoresFiltros.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.filter;

import java.util.ArrayList;
import java.util.Collection;

public class OperadoresFiltros {

	
	public static final int TIPO_STRING=0;
	
	public static ArrayList<String> initOperadoresVarchar(){
        ArrayList<String> operadores= new ArrayList<String>();
        operadores.add("");
        operadores.add("=");
        operadores.add("<>");
        operadores.add("LIKE");
        
        return operadores;
    }

	public static ArrayList<String> initOperadoresNumericDouble(){
		ArrayList<String> operadores= new ArrayList<String>();
        operadores.add("");
        operadores.add("=");
        operadores.add("<>");
        operadores.add(">");
        operadores.add(">=");
        operadores.add("<");
        operadores.add("<=");
        return operadores;
    }

	public static ArrayList<String> initOperadoresDate(){
		ArrayList<String> operadores= new ArrayList<String>();
        operadores.add("");
        operadores.add("=");
        operadores.add("<>");
        operadores.add(">");
        operadores.add(">=");
        operadores.add("<");
        operadores.add("<=");
        //operadores.add("BETWEEN");
        return operadores;
    }

	public static ArrayList<String> initOperadoresBoolean(){
		ArrayList<String> operadores= new ArrayList<String>();
        operadores.add("");
        operadores.add("=");
        return operadores;
    }

	public static ArrayList<String> initOperadoresDominio(){
    	ArrayList<String> operadores= new ArrayList<String>();
        operadores.add("");
        operadores.add("=");
        operadores.add("<>");
        return operadores;
    }
	
	public static ArrayList<String> initOperadoresDominioInteger(){
    	ArrayList<String> operadores= new ArrayList<String>();
    	operadores.add("");
        operadores.add("=");
        operadores.add("<>");
        operadores.add(">");
        operadores.add(">=");
        operadores.add("<");
        operadores.add("<=");
        return operadores;
    }
	
	/**
	 * Obtenemos la sentencia SQL asociada al filtro
	 * @param collection
	 * @return
	 */
	public static String getSQLFromFiltro(Collection<CampoFiltro> collection){
    	String filtroSQL="";
    	if (collection!=null){
	        for (java.util.Iterator<CampoFiltro> it=collection.iterator();it.hasNext();){
	        	CampoFiltro campoFiltro=(CampoFiltro) it.next();
	        	filtroSQL += campoFiltro.getTabla()+"."+campoFiltro.getNombre()+" "+campoFiltro.getOperador();
	        	switch(campoFiltro.getTipo()){
		        	case CampoFiltro.VARCHAR_CODE:{
		        		filtroSQL += "'"+campoFiltro.getValorVarchar()+"'";
		        		break;
		        	}
		        	case CampoFiltro.NUMERIC_CODE:{
		        		filtroSQL += "'"+campoFiltro.getValorNumeric()+"'";
		        		break;
		        	}
		        	case CampoFiltro.DATE_CODE:{
		        		filtroSQL += "'"+campoFiltro.getValorDate()+"'";
		        		break;
		        	}
		        	case CampoFiltro.DOUBLE_CODE:{
		        		filtroSQL += "'"+campoFiltro.getValorDouble()+"'";
		        		break;
		        	}
		        	case CampoFiltro.DOMINIO_CODE:{
		        		filtroSQL += "'"+campoFiltro.getValorVarchar()+"'";
		        		break;
		        	}
		        	case CampoFiltro.DOMINIO_CODE_INTEGER:{
		        		filtroSQL += ""+campoFiltro.getValorVarchar()+"";
		        		break;
		        	}
		        	case CampoFiltro.BOOLEAN_CODE:{
		        		filtroSQL += "'"+campoFiltro.getValorBoolean()+"'";
		        		break;
		        	}
		        	case CampoFiltro.COMPUESTO_CODE:{
		        		filtroSQL += "'"+campoFiltro.getValorCompuesto()+"'";
		        		break;
		        	}
	        	}
	        	if(it.hasNext())filtroSQL +="\n and ";
	        }
        }
    	return filtroSQL;
	}
}
