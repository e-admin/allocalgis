package com.geopista.app.cementerios.managers;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.geopista.app.cementerios.business.dao.DAO;
import com.geopista.app.cementerios.business.dao.implementations.ComunDAOImpl;
import com.geopista.app.cementerios.business.dao.interfaces.ComunDAO;
import com.geopista.app.cementerios.business.vo.Comun;
import com.geopista.protocol.cementerios.CampoFiltro;
	
public class ComunManager extends DAO {

	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ComunManager.class);

	private static ComunManager instance;
	@SuppressWarnings("rawtypes")
	private static ComunDAO comunDAO;
		    
	public static final int unidadEnterramiento=0;
	public static final int bloque=1;
	public static final int concesion=2;
	public static final int difunto=3;
	
	public static final int contenedor=4;
	public static final int inhumacion=5;
	public static final int exhumacion=6;
	public static final int servicios=7;
	

	public static ComunManager getInstance() {
		if (instance == null) {
			instance = new ComunManager();
		}
		return instance;
	}

	public ComunManager() {
		comunDAO = new ComunDAOImpl();
	}


	/**OBTENCION DE LOS CAMPOS DE CADA TIPO @throws SQLException **/
    
    public Collection getCamposUnidadEnterramiento() throws SQLException{
    	
    	Collection campos = new ArrayList();
    	
		Comun comun = new Comun();
		comun.setAttrelid("cementerio.unidadEnterramiento");

		List listColumns = new ArrayList();
		listColumns = comunDAO.selectByRegClass(comun);

        CampoFiltro campo;
		for (int i = 0; i < listColumns.size(); i++) {
			Comun elem = (Comun) listColumns.get(i);
			campo = new CampoFiltro();
			if (elem.getAttname().equalsIgnoreCase("tipo_unidad")){
				campo.setIsDominio();
				campo.setDominio(unidadEnterramiento);
			}else if (elem.getAttname().equalsIgnoreCase("estado")){
				campo.setBoolean();
			}
			else if ((elem.getAttname().startsWith("id")) || (elem.getAttname().equalsIgnoreCase("fult_modificacion"))){
   				continue;
   			}
			else{
				campo = setTipoColumna(campo, elem);
			}
	        campo.setNombre(elem.getAttname());
	        campo.setTabla(elem.getAttrelid());
	        campo = setDescripcionUnidadEnterramiento(campo, elem);
	        campos.add(campo);
		}
		Collections.sort((List)campos);
		return campos;
    }
	    
/**
 * 
 * @return
 * @throws SQLException
 */
    public Collection getCamposBloque() throws SQLException{
    	
    	Collection campos = new ArrayList();
    	
		Comun comun = new Comun();
		comun.setAttrelid("cementerio.bloque");

		List listColumns = new ArrayList();
		listColumns = comunDAO.selectByRegClass(comun);

        CampoFiltro campo;
		for (int i = 0; i < listColumns.size(); i++) {
			Comun elem = (Comun) listColumns.get(i);
			campo = new CampoFiltro();
			if (elem.getAttname().equalsIgnoreCase("tipo_bloque")){
				campo.setIsDominio();
				campo.setDominio(bloque);
			}
			else if (elem.getAttname().startsWith("id")){
   				continue;
   			}
			else{
				campo = setTipoColumna(campo, elem);
			}
	        campo.setNombre(elem.getAttname());
	        campo.setTabla(elem.getAttrelid());
	        campo = setDescripcionBloque(campo, elem);
	        campos.add(campo);
		}
		Collections.sort((List)campos);
		return campos;
    }
	    
/**
 * 	
 * @return
 * @throws SQLException
 */
    public Collection getCamposConcesion() throws SQLException{
    	
    	Collection campos = new ArrayList();
    	
		Comun comun = new Comun();
		comun.setAttrelid("cementerio.concesion");

		List listColumns = new ArrayList();
		listColumns = comunDAO.selectByRegClass(comun);

        CampoFiltro campo;
		for (int i = 0; i < listColumns.size(); i++) {
			Comun elem = (Comun) listColumns.get(i);
			campo = new CampoFiltro();
			if (elem.getAttname().equalsIgnoreCase("tipo_concesion")){
				campo.setIsDominio();
				campo.setDominio(concesion);
			}else if (elem.getAttname().equalsIgnoreCase("estado")){
				campo.setBoolean();				
			}
			else if ((elem.getAttname().startsWith("id")) || (elem.getAttname().equalsIgnoreCase("ultimo_titular"))){
   				continue;
   			}
			else{
				campo = setTipoColumna(campo, elem);
			}
	        campo.setNombre(elem.getAttname());
	        campo.setTabla(elem.getAttrelid());
	        campo = setDescripcionConcesion(campo, elem);
	        campos.add(campo);
		}
		Collections.sort((List)campos);
		return campos;
    }
    
    /**
     * 
     * @return
     * @throws SQLException
     */
     public Collection getCamposTitular() throws SQLException{
     	
    	 Collection campos = new ArrayList();
    	 
 		Comun comun = new Comun();
 		comun.setAttrelid("cementerio.persona");

 		List listColumns = new ArrayList();
 		listColumns = comunDAO.selectByRegClass(comun);

         CampoFiltro campo;
 		for (int i = 0; i < listColumns.size(); i++) {
 			Comun elem = (Comun) listColumns.get(i);
 			campo = new CampoFiltro();
			if (elem.getAttname().startsWith("id")){
   				continue;
   			}
 			campo = setTipoColumna(campo, elem);
 	        campo.setNombre(elem.getAttname());
 	        campo.setTabla(elem.getAttrelid());
 	        campo = setDescripcionTitular(campo, elem);
 	        campos.add(campo);
 		}
 		Collections.sort((List)campos);
 		return campos;
     }
     
     /**
      * 
      * @return
      * @throws SQLException
      */
     public Collection getCamposPersona() throws SQLException{
      	
    	 Collection campos = new ArrayList();
     	 
  		Comun comun = new Comun();
  		comun.setAttrelid("cementerio.persona");

  		List listColumns = new ArrayList();
  		listColumns = comunDAO.selectByRegClass(comun);

          CampoFiltro campo;
  		for (int i = 0; i < listColumns.size(); i++) {
  			Comun elem = (Comun) listColumns.get(i);
  			campo = new CampoFiltro();
 			if (elem.getAttname().startsWith("id")){
    				continue;
    			}
  			campo = setTipoColumna(campo, elem);
  	        campo.setNombre(elem.getAttname());
  	        campo.setTabla(elem.getAttrelid());
  	        campo = setDescripcionTitular(campo, elem);
  	        campos.add(campo);
  		}
  		Collections.sort((List)campos);
  		return campos;
      }
     
//     public void getCamposFrom(String tablaStr) throws SQLException{
//    	 
//    	 Collection camposAux = new ArrayList();
//    	 Comun comun = new Comun();
//    	 comun.setAttrelid("cementerio.".concat(tablaStr));
//    	 
//    	 List listColumns = new ArrayList();
//       	 listColumns = comunDAO.selectByRegClass(comun);
//       	 CampoFiltro campo;	
//    		for (int i = 0; i < listColumns.size(); i++) {
//    			Comun elem = (Comun) listColumns.get(i);
//    			campo = new CampoFiltro();
//    			/**Esto es si se quiere un nivel mas de profundidad en el filtrado -de momento solo un nivel de profundidad **/
////       			if ((elem.getAttname().contains("id_")) || (elem.getAttname().contains("dni_"))){
////    				campo.setIsID();
////    				tablaStr = StringUtils.substringAfterLast(elem.getAttname(), "_");
////    				campo.setValorId(tablaStr);
////    				getCamposFrom (tablaStr);
////    				
////       			}else{
//    			if (elem.getAttname().startsWith("id")){
//       				continue;
//       			}
//    			campo = setTipoColumna(campo, elem);
//    	        campo.setNombre(elem.getAttname());
//    	        campo.setTabla(elem.getAttrelid());
//    	        campo.setValorId(tablaStr);
//    	        
//    	        campo = setDescripcion(campo, elem , tablaStr);
//    	        
//    	        campos.add(campo);
////       			}
//    		}
//     }
     
    /**
     *  
     */
	private String removeGuiones(String c) {
		String[] lista;
		lista = c.split("_");
		String cadenaLista = "";

		for (int i = 0; i < lista.length; i++) {
			cadenaLista = cadenaLista.concat(StringUtils.capitalise(lista[i]));
		}
		return cadenaLista;
	}
     
	/**
	 * 	
	 * @param tablaStr
	 * @throws SQLException
	 */
     public Collection getCamposFrom(String tablaStr) throws SQLException{
    	 
 		String method = "getCampos";
		String attr;
		String operacion = "";
		Collection resultInvoke = new ArrayList();
		
		if (tablaStr.equalsIgnoreCase("tarifa") || tablaStr.equalsIgnoreCase("cementerio") || tablaStr.equalsIgnoreCase("elemcementerio")  || 
				tablaStr.equalsIgnoreCase("cementerio") || tablaStr.equalsIgnoreCase("elem")) {
			return resultInvoke;
		}
		
		try {

			Class clase = com.geopista.app.cementerios.managers.ComunManager.class;
			
			Class[] tiposParams = null;
			ComunManager object = ComunManager.getInstance();
			
			attr = removeGuiones(tablaStr);
			method = method.concat(StringUtils.capitalise(attr));

			Method metodoCrit = null;
			metodoCrit = clase.getMethod(method, tiposParams);

			resultInvoke = (Collection) metodoCrit.invoke(object, null);
			
		} catch (Exception e) {
			logger.error("Error" + e);
		}
    	
		Collections.sort((List) resultInvoke);
		
		Iterator it = resultInvoke.iterator();
		while (it.hasNext()){
			CampoFiltro campo = (CampoFiltro) it.next();
			
			String valorID = StringUtils.substringAfterLast(campo.getTabla(), ".");
			if (tablaStr.equalsIgnoreCase("plaza")) continue;
			campo.setValorId(valorID);
		}
    	return resultInvoke; 
     }
     
     /**
      * 
      * @return
      * @throws SQLException
      */
     public Collection getCamposDifunto() throws SQLException{
       	
    	 Collection campos = new ArrayList();
     	 
   		Comun comun = new Comun();
   		comun.setAttrelid("cementerio.difunto");

   		List listColumns = new ArrayList();
   		List listColumnsAux = new ArrayList();
   		listColumns = comunDAO.selectByRegClass(comun);

        CampoFiltro campo;
   		for (int i = 0; i < listColumns.size(); i++) {
   			Comun elem = (Comun) listColumns.get(i);
   			campo = new CampoFiltro();
   			String tablaStr;
   			if ((elem.getAttname().contains("id_")) || (elem.getAttname().contains("dni_"))){
				campo.setIsID();
				tablaStr = StringUtils.substringAfterLast(elem.getAttname(), "_");
				if (tablaStr.equalsIgnoreCase("plaza")) continue;
				campo.setValorId(tablaStr);
				listColumnsAux= (List) getCamposFrom (tablaStr);
				campos.addAll(listColumnsAux);
   			}
   			else if (elem.getAttname().startsWith("id")){
   				continue;
   			}
   			else{
   				campo = setTipoColumna(campo, elem);
   			
   				campo.setNombre(elem.getAttname());
   				campo.setTabla(elem.getAttrelid());
   				campo = setDescripcionDifunto(campo, elem);
   				campos.add(campo);
   			}
   		}
   		Collections.sort((List)campos);
   		return campos;
       }
     
     /**
      * 
      * @return
      * @throws SQLException
      */
     public Collection getCamposDatosfallecimiento() throws SQLException{
      	
    	 Collection campos = new ArrayList();
     	 
  		Comun comun = new Comun();
  		comun.setAttrelid("cementerio.datosfallecimiento");

  		List listColumns = new ArrayList();
  		listColumns = comunDAO.selectByRegClass(comun);

          CampoFiltro campo;
  		for (int i = 0; i < listColumns.size(); i++) {
  			Comun elem = (Comun) listColumns.get(i);
  			campo = new CampoFiltro();
 			if (elem.getAttname().startsWith("id")){
    				continue;
    			}
  			campo = setTipoColumna(campo, elem);
  	        campo.setNombre(elem.getAttname());
  	        campo.setTabla(elem.getAttrelid());
  	        campo = setDescripcionDatosFallecimiento(campo, elem);
  	        campos.add(campo);
  		}
  		Collections.sort((List)campos);
  		return campos;
      }
     
/**
 * getCamposInhumacion()    
 * @return
 * @throws SQLException
 */
     public Collection getCamposInhumacion() throws SQLException{
       	
    	 Collection campos = new ArrayList();
    	 Collection listColumnsAux = new ArrayList(); 
     	 
   		Comun comun = new Comun();
   		comun.setAttrelid("cementerio.inhumacion");

   		List listColumns = new ArrayList();
   		listColumns = comunDAO.selectByRegClass(comun);

           CampoFiltro campo;
   		for (int i = 0; i < listColumns.size(); i++) {
   			Comun elem = (Comun) listColumns.get(i);
   			campo = new CampoFiltro();
   			String tablaStr;
   			if ((elem.getAttname().contains("id_")) || (elem.getAttname().contains("dni_"))){
				campo.setIsID();
				tablaStr = StringUtils.substringAfterLast(elem.getAttname(), "_");
				if (tablaStr.equalsIgnoreCase("plaza")) continue;
				campo.setValorId(tablaStr);
				listColumnsAux= (List) getCamposFrom (tablaStr);
				campos.addAll(listColumnsAux);
   			}
   			else{
				if (elem.getAttname().startsWith("id")){
	   				continue;
	   			}
	   			else if (elem.getAttname().equalsIgnoreCase("contenedor")){
					campo.setIsDominio();
					campo.setDominio(contenedor);
	   			}else if (elem.getAttname().equalsIgnoreCase("tipo_inhumacion")){
	   				campo.setIsDominio();
	   				campo.setDominio(inhumacion);
	   			}
	   			else{
	   				campo = setTipoColumna(campo, elem);
	   			}
	   	        campo.setNombre(elem.getAttname());
	   	        campo.setTabla(elem.getAttrelid());
	   	        campo = setDescripcionInhumacion(campo, elem);
	   	        campos.add(campo);
   			}
   		}
   		Collections.sort((List)campos);
   		return campos;
       }
     
     /**
      * getCamposExhumacion
      * @return
      * @throws SQLException
      */
     public Collection getCamposExhumacion() throws SQLException{
        	
    	 Collection campos = new ArrayList();
    	 Collection listColumnsAux = new ArrayList(); 
      	 
    	 Comun comun = new Comun();
    	 comun.setAttrelid("cementerio.exhumacion");

    	 List listColumns = new ArrayList();
    	 listColumns = comunDAO.selectByRegClass(comun);

            CampoFiltro campo;
    		for (int i = 0; i < listColumns.size(); i++) {
    			Comun elem = (Comun) listColumns.get(i);
    			campo = new CampoFiltro();
       			String tablaStr;
       			if ((elem.getAttname().contains("id_")) || (elem.getAttname().contains("dni_"))){
    				campo.setIsID();
    				tablaStr = StringUtils.substringAfterLast(elem.getAttname(), "_");
    				if (tablaStr.equalsIgnoreCase("plaza")) continue;
    				campo.setValorId(tablaStr);
    				listColumnsAux= (List) getCamposFrom (tablaStr);
    				campos.addAll(listColumnsAux);
       			}
       			else{
	    			if (elem.getAttname().startsWith("id")){
	       				continue;
	       			}    			
	    			if (elem.getAttname().equalsIgnoreCase("contenedor")){
	 				campo.setIsDominio();
	 				campo.setDominio(contenedor);
	    			}else if (elem.getAttname().equalsIgnoreCase("tipo_exhumacion")){
	    				campo.setIsDominio();
	    				campo.setDominio(exhumacion);
	    			}else if (elem.getAttname().equalsIgnoreCase("red_restos")){
	    				campo.setBoolean();
	    			}else if (elem.getAttname().equalsIgnoreCase("traslado")){
	    				campo.setBoolean();
	    			}
	    			else{
	    				campo = setTipoColumna(campo, elem);
	    			}
	    	        campo.setNombre(elem.getAttname());
	    	        campo.setTabla(elem.getAttrelid());
	    	        campo = setDescripcionExhumacion(campo, elem);
	    	        campos.add(campo);
       			}
    		}  
    		Collections.sort((List)campos);
    		return campos;
        }

     /**
      * getCamposHistoricoDifuntos
      * @return
      * @throws SQLException
      */
     public Collection getCamposHistoricoDifuntos() throws SQLException{
     	
    	 Collection campos = new ArrayList();
    	 
       	 
 		Comun comun = new Comun();
 		comun.setAttrelid("cementerio.historico_difuntos");

 		List listColumns = new ArrayList();
 		Collection listColumnsAux = new ArrayList(); 
      	 
 		listColumns = comunDAO.selectByRegClass(comun);

        CampoFiltro campo;
 		for (int i = 0; i < listColumns.size(); i++) {
 			Comun elem = (Comun) listColumns.get(i);
 			campo = new CampoFiltro();
   			String tablaStr;
   			if ((elem.getAttname().contains("id_")) || (elem.getAttname().contains("dni_"))){
				campo.setIsID();
				tablaStr = StringUtils.substringAfterLast(elem.getAttname(), "_");
				if (tablaStr.equalsIgnoreCase("plaza")) continue;
				campo.setValorId(tablaStr);
				listColumnsAux= (List) getCamposFrom (tablaStr);
				campos.addAll(listColumnsAux);
   			}
   			else{
    			if (elem.getAttname().startsWith("id")){
       				continue;
       			}   
     			campo = setTipoColumna(campo, elem);
     	        campo.setNombre(elem.getAttname());
     	        campo.setTabla(elem.getAttrelid());
     	        campo = setDescripcionHistoricoDifunto(campo, elem);
     	        campos.add(campo);
   			}
 		}  
 		Collections.sort((List)campos);
 		return campos;
      }
     
     /**
      * getCamposHistoricoDifuntos
      * @return
      * @throws SQLException
      */
     public Collection getCamposHistoricoPropiedad() throws SQLException{
     	
    	 Collection campos = new ArrayList();
       	 
 		Comun comun = new Comun();
 		comun.setAttrelid("cementerio.historico_propiedad");

 		List listColumns = new ArrayList();
 		Collection listColumnsAux = new ArrayList();
 		listColumns = comunDAO.selectByRegClass(comun);

        CampoFiltro campo;
 		for (int i = 0; i < listColumns.size(); i++) {
 			Comun elem = (Comun) listColumns.get(i);
 			campo = new CampoFiltro();
   			String tablaStr;
   			if (elem.getAttname().equalsIgnoreCase("id_elem")){
   				elem.setAttname("id_concesion");
   			}
   			if ((elem.getAttname().contains("id_")) || (elem.getAttname().contains("dni_"))){
				campo.setIsID();
				tablaStr = StringUtils.substringAfterLast(elem.getAttname(), "_");
				if (tablaStr.equalsIgnoreCase("plaza")) continue;
				campo.setValorId(tablaStr);
				listColumnsAux= (List) getCamposFrom (tablaStr);
				campos.addAll(listColumnsAux);
   			}
   			else{
    			if (elem.getAttname().startsWith("id")){
       				continue;
       			}  

     			campo = setTipoColumna(campo, elem);
     	        campo.setNombre(elem.getAttname());
     	        campo.setTabla(elem.getAttrelid());
     	       campo = setDescripcionHistoricoPropiedad(campo, elem);
     	        campos.add(campo);
   			}
 		} 
     	Collections.sort((List)campos);
     	return campos;
    }
     
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
     public Collection getCamposTarifasDifuntos() throws SQLException{
      	
    	 Collection campos = new ArrayList();
     	 
		Comun comun = new Comun();
		comun.setAttrelid("cementerio.tarifa");

		List listColumns = new ArrayList();
		listColumns = comunDAO.selectByRegClass(comun);

        CampoFiltro campo;
		for (int i = 0; i < listColumns.size(); i++) {
			Comun elem = (Comun) listColumns.get(i);
			campo = new CampoFiltro();
			if (elem.getAttname().equalsIgnoreCase("categoria")){
				campo.setIsDominio();
				campo.setDominio(unidadEnterramiento);
			}else if (elem.getAttname().equalsIgnoreCase("tipo_calculo")){
				campo.setIsDominio();
				campo.setDominio(servicios);
			}
			else if (elem.getAttname().startsWith("id")){
   				continue;
   			}
			else{
				campo = setTipoColumna(campo, elem);
			}
	        campo.setNombre(elem.getAttname());
	        campo.setTabla(elem.getAttrelid());
	        campo = setDescripcionTarifa(campo, elem);
	        campos.add(campo);
		}   	
		Collections.sort((List)campos);
		return campos;
    }
    
     /**
      * getCamposTarifasPropiedad
      * @return
      * @throws SQLException
      */
     public Collection getCamposTarifasPropiedad() throws SQLException{
       	
    	Collection	campos = new ArrayList();
      	 
 		Comun comun = new Comun();
 		comun.setAttrelid("cementerio.tarifa");

 		List listColumns = new ArrayList();
 		listColumns = comunDAO.selectByRegClass(comun);

         CampoFiltro campo;
 		for (int i = 0; i < listColumns.size(); i++) {
 			Comun elem = (Comun) listColumns.get(i);
 			campo = new CampoFiltro();
 			if (elem.getAttname().equalsIgnoreCase("categoria")){
 				campo.setIsDominio();
 				campo.setDominio(unidadEnterramiento);
 			}else if (elem.getAttname().equalsIgnoreCase("tipo_calculo")){
 				campo.setIsDominio();
 				campo.setDominio(concesion);
 			}
			else if (elem.getAttname().startsWith("id")){
   				continue;
   			}
 			else{
 				campo = setTipoColumna(campo, elem);
 			}
 	        campo.setNombre(elem.getAttname());
 	        campo.setTabla(elem.getAttrelid());
 	        campo = setDescripcionTarifa(campo, elem);
 	        campos.add(campo);
 		}
 		  Collections.sort((List)campos);
 		return campos;
     }
     
     /**
      * 
      * @return
      * @throws SQLException
      */
     public Collection getCamposIntervenciones() throws SQLException{
        	
    	Collection campos = new ArrayList();
   	 
		Comun comun = new Comun();
		comun.setAttrelid("cementerio.intervencion");


 		List listColumns = new ArrayList();
 		listColumns = comunDAO.selectByRegClass(comun);

         CampoFiltro campo;
 		for (int i = 0; i < listColumns.size(); i++) {
 			Comun elem = (Comun) listColumns.get(i);
			if (elem.getAttname().startsWith("id")){
   				continue;
   			}
 			campo = new CampoFiltro();
 			campo = setTipoColumna(campo, elem);
 	        campo.setNombre(elem.getAttname());
 	        campo.setTabla(elem.getAttrelid());
 	        campo = setDescripcionIntervencion(campo, elem);
 	        campos.add(campo);
 		} 
 		Collections.sort((List)campos);
 		return campos;
     }
     
     
	/**TIPO COLUMNA**/

    private static CampoFiltro setTipoColumna(CampoFiltro campo, Comun comun){
    	
    	String format_type = comun.getFormat_type();
    	
    	if (format_type.contains("integer")){
    		campo.setNumeric();
    	}else if (format_type.contains("date")){
    		campo.setDate();
    	}
    	else if (format_type.contains("character")){
    		campo.setVarchar();
    	}
    	
    	return campo;
    	
    }
    
    /**DESCRIPCIONES**/
 
    /**
     * 
     */
    private static CampoFiltro setDescripcion(CampoFiltro campo, Comun comun , String tablaStr){

    	if (tablaStr.equalsIgnoreCase("persona")){
    		campo = setDescripcionTitular(campo, comun);
    	}
    	else if (tablaStr.equalsIgnoreCase("datosfallecimiento")){
    		campo = setDescripcionDatosFallecimiento(campo, comun);
    	}
    	else if (tablaStr.equalsIgnoreCase("difunto")){
    		campo = setDescripcionDifunto(campo, comun);
    	}
    	else if (tablaStr.equalsIgnoreCase("intervencion")){
    		campo = setDescripcionIntervencion(campo, comun);
    	}
    	else if (tablaStr.equalsIgnoreCase("concesion")){
    		campo = setDescripcionConcesion(campo, comun);
    	}
    	
    	return campo;
    }
    
    /**
     * 
     * @param campo
     * @param comun
     * @return
     */
    private static CampoFiltro setDescripcionBloque(CampoFiltro campo, Comun comun){
    	
    	String attname = comun.getAttname();
    	
    	if (attname.equalsIgnoreCase("tipo_bloque")){
    		campo.setDescripcion("Tipo de Bloque de unidades");
    	}
    	else if (attname.equalsIgnoreCase("num_filas")){
    		campo.setDescripcion("Número de filas");
    	}
    	else if (attname.equalsIgnoreCase("num_columnas")){
    		campo.setDescripcion("Número de columnas");
    	}
    	else if (attname.equalsIgnoreCase("descripcion")){
    		campo.setDescripcion("Descripción");
    	}
    	return campo;
    }
    
    /**
     * 
     * @param campo
     * @param comun
     * @return
     */
    private static CampoFiltro setDescripcionUnidadEnterramiento(CampoFiltro campo, Comun comun){
    	
    	String attname = comun.getAttname();
    	
    	if (attname.equalsIgnoreCase("columna")){
    		campo.setDescripcion("Columna");
    	}
    	else if (attname.equalsIgnoreCase("fila")){
    		campo.setDescripcion("Fila");
    	}
    	else if (attname.equalsIgnoreCase("numplazas")){
    		campo.setDescripcion("Número de plazas");
    	}
    	else if (attname.equalsIgnoreCase("fult_construccion")){
    		campo.setDescripcion("Fecha de construcción");
    	}
    	else if (attname.equalsIgnoreCase("fult_modificación")){
    		campo.setDescripcion("Fecha de última modificación");
    	}
    	else if (attname.equalsIgnoreCase("tipo_unidad")){
    		campo.setDescripcion("Tipo de unidad");
    	}
    	else if (attname.equalsIgnoreCase("descripcion")){
    		campo.setDescripcion("Descripción");
    	}
    	else if (attname.equalsIgnoreCase("codigo")){
    		campo.setDescripcion("Código");
    	}
    	else if (attname.equalsIgnoreCase("freforma")){
    		campo.setDescripcion("Fecha de última reforma");
    	}
    	else if (attname.equalsIgnoreCase("estado")){
    		campo.setDescripcion("Estado");
    	}
    	return campo;
    }
    
    /**
     * 
     * @param campo
     * @param comun
     * @return
     */
    private static CampoFiltro setDescripcionIntervencion(CampoFiltro campo, Comun comun){
    	
    	String attname = comun.getAttname();
    	
    	if (attname.equalsIgnoreCase("informe")){
    		campo.setDescripcion("Informe de interveción");
    	}
    	else if (attname.equalsIgnoreCase("fecha_inicio")){
    		campo.setDescripcion("Fecha de inicio ");
    	}
    	else if (attname.equalsIgnoreCase("fecha_fin")){
    		campo.setDescripcion("Fecha de fin ");
    	}
    	else if (attname.equalsIgnoreCase("localizacion")){
    		campo.setDescripcion("Localización");
    	}
    	else if (attname.equalsIgnoreCase("codigo")){
    		campo.setDescripcion("Código");
    	}
    	else if (attname.equalsIgnoreCase("estado")){
    		campo.setDescripcion("Estado");
    	}
    	return campo;
    }
    
    /**
     * 
     * @param campo
     * @param comun
     * @return
     */
    private static CampoFiltro setDescripcionTitular(CampoFiltro campo, Comun comun){
    	
    	String attname = comun.getAttname();
    	
    	if (attname.equalsIgnoreCase("nombre")){
    		campo.setDescripcion("Nombre");
    	}
    	else if (attname.equalsIgnoreCase("apellido1")){
    		campo.setDescripcion("Primer apellido ");
    	}
    	else if (attname.equalsIgnoreCase("apellido2")){
    		campo.setDescripcion("Segundo apellido");
    	}
    	else if (attname.equalsIgnoreCase("dni")){
    		campo.setDescripcion("Dni");
    	}
    	else if (attname.equalsIgnoreCase("estado_civil")){
    		campo.setDescripcion("Estado civil");
    	}
    	else if (attname.equalsIgnoreCase("fecha_nacimiento")){
    		campo.setDescripcion("Fecha de nacimiento");
    	}
    	else if (attname.equalsIgnoreCase("domicilio")){
    		campo.setDescripcion("Domicilio");
    	}
    	else if (attname.equalsIgnoreCase("poblacion")){
    		campo.setDescripcion("Población");
    	}
    	else if (attname.equalsIgnoreCase("telefono")){
    		campo.setDescripcion("Telefono");
    	}
    	else if (attname.equalsIgnoreCase("sexo")){
    		campo.setDescripcion("Sexo");
    	}
    	return campo;
    }
    
    /**
     * 
     * @param campo
     * @param comun
     * @return
     */
    private static CampoFiltro setDescripcionConcesion(CampoFiltro campo, Comun comun){
    	
    	String attname = comun.getAttname();
    	
    	if (attname.equalsIgnoreCase("localizado")){
    		campo.setDescripcion("Localizado");
    	}
    	else if (attname.equalsIgnoreCase("fecha_ini")){
    		campo.setDescripcion("Fecha de incio");
    	}
    	else if (attname.equalsIgnoreCase("fecha_fin")){
    		campo.setDescripcion("Fecha de fin");
    	}
    	else if (attname.equalsIgnoreCase("ultima_renova")){
    		campo.setDescripcion("Fecha de última renovación");
    	}
    	else if (attname.equalsIgnoreCase("estado")){
    		campo.setDescripcion("Estado");
    	}
    	else if (attname.equalsIgnoreCase("tipo_concesion")){
    		campo.setDescripcion("Tipo de concesión");
    	}
    	else if (attname.equalsIgnoreCase("descripcion")){
    		campo.setDescripcion("Descripción");
    	}
    	else if (attname.equalsIgnoreCase("codigo")){
    		campo.setDescripcion("Código de la concesión");
    	}
    	else if (attname.equalsIgnoreCase("precio_final")){
    		campo.setDescripcion("Precio final");
    	}
    	else if (attname.equalsIgnoreCase("bonificacion")){
    		campo.setDescripcion("Bonificación");
    	}
    	return campo;
    }

    /**
     * 
     * @param campo
     * @param comun
     * @return
     */
    private static CampoFiltro setDescripcionDifunto(CampoFiltro campo, Comun comun){
    	
    	String attname = comun.getAttname();
    	
    	if (attname.equalsIgnoreCase("fecha_defuncion")){
    		campo.setDescripcion("Fecha de defunción");
    	}
    	else if (attname.equalsIgnoreCase("edad_difunto")){
    		campo.setDescripcion("Edad difunto");
    	}
    	else if (attname.equalsIgnoreCase("grupo")){
    		campo.setDescripcion("Grupo");
    	}
    	else if (attname.equalsIgnoreCase("codigo")){
    		campo.setDescripcion("Código defunción");
    	}
    	return campo;
    }
   
    
    private static CampoFiltro setDescripcionDatosFallecimiento(CampoFiltro campo, Comun comun){
    	
    	String attname = comun.getAttname();
    	
    	if (attname.equalsIgnoreCase("lugar")){
    		campo.setDescripcion("Lugar del fallecimiento");
    	}
    	else if (attname.equalsIgnoreCase("poblacion")){
    		campo.setDescripcion("Población del fallecimiento");
    	}
    	else if (attname.equalsIgnoreCase("causa_fundamental")){
    		campo.setDescripcion("Causa fundamental");
    	}
    	else if (attname.equalsIgnoreCase("causa_inmediata")){
    		campo.setDescripcion("Causa inmediata");
    	}
    	else if (attname.equalsIgnoreCase("fecha")){
    		campo.setDescripcion("Fecha de fallecimiento");
    	}
    	else if (attname.equalsIgnoreCase("medico")){
    		campo.setDescripcion("Médico forense");
    	}
    	else if (attname.equalsIgnoreCase("num_colegiado")){
    		campo.setDescripcion("Número de colegiado");
    	}
    	else if (attname.equalsIgnoreCase("registro_civill")){
    		campo.setDescripcion("Registro civil");
    	}
    	else if (attname.equalsIgnoreCase("referencia")){
    		campo.setDescripcion("Referencia de fallecimiento");
    	}
    	return campo;
    }
    
    /**
     * 
     * @param campo
     * @param comun
     * @return
     */
    private static CampoFiltro setDescripcionInhumacion(CampoFiltro campo, Comun comun){
    	
    	String attname = comun.getAttname();
    	
    	if (attname.equalsIgnoreCase("informe")){
    		campo.setDescripcion("Informe de inhumación");
    	}
    	else if (attname.equalsIgnoreCase("contenedor")){
    		campo.setDescripcion("Tipo de contenedor");
    	}
    	else if (attname.equalsIgnoreCase("codigo")){
    		campo.setDescripcion("Código de inhumación");
    	}
    	else if (attname.equalsIgnoreCase("tipo_inhumacion")){
    		campo.setDescripcion("Tipo de inhumación");
    	}
    	else if (attname.equalsIgnoreCase("fecha_inhumacion")){
    		campo.setDescripcion("Fecha de inhumación");
    	}
    	else if (attname.equalsIgnoreCase("precio_final")){
    		campo.setDescripcion("Precio final");
    	}
    	else if (attname.equalsIgnoreCase("bonificacion")){
    		campo.setDescripcion("Bonificación");
    	}
    	return campo;
    }

    /**
     * 
     * @param campo
     * @param comun
     * @return
     */
    private static CampoFiltro setDescripcionExhumacion(CampoFiltro campo, Comun comun){
    	
    	String attname = comun.getAttname();
    	
    	if (attname.equalsIgnoreCase("informe")){
    		campo.setDescripcion("Informe de exhumación");
    	}
    	else if (attname.equalsIgnoreCase("contenedor")){
    		campo.setDescripcion("Tipo de contenedor");
    	}
    	else if (attname.equalsIgnoreCase("codigo")){
    		campo.setDescripcion("Código de exhumación");
    	}
    	else if (attname.equalsIgnoreCase("tipo_exhumacion")){
    		campo.setDescripcion("Tipo de exhumación");
    	}
    	else if (attname.equalsIgnoreCase("fecha_exhumacion")){
    		campo.setDescripcion("Fecha de exhumación");
    	}
    	else if (attname.equalsIgnoreCase("precio_final")){
    		campo.setDescripcion("Precio final");
    	}
    	else if (attname.equalsIgnoreCase("bonificacion")){
    		campo.setDescripcion("Bonificación");
    	}
    	else if (attname.equalsIgnoreCase("red_restos")){
    		campo.setDescripcion("Reducción de Restos");
    	}
    	else if (attname.equalsIgnoreCase("traslado")){
    		campo.setDescripcion("Traslado");
    	}
    	
    	return campo;
    }
    
    /**
     * 
     * @param campo
     * @param comun
     * @return
     */
    private static CampoFiltro setDescripcionTarifa(CampoFiltro campo, Comun comun){
    	
    	String attname = comun.getAttname();
    	
    	if (attname.equalsIgnoreCase("concepto")){
    		campo.setDescripcion("Concepto de tarifa");
    	}
    	else if (attname.equalsIgnoreCase("tipo_tarifa")){
    		campo.setDescripcion("Tipo de tarifa");
    	}
    	else if (attname.equalsIgnoreCase("tipo_calculo")){
    		campo.setDescripcion("Tipo de calculo");
    	}
    	else if (attname.equalsIgnoreCase("categoria")){
    		campo.setDescripcion("Tipo de unidad");
    	}
    	else if (attname.equalsIgnoreCase("precio")){
    		campo.setDescripcion("Precio de tarifa");
    	}
    	return campo;
    }
    
    private static CampoFiltro setDescripcionHistoricoDifunto(CampoFiltro campo, Comun comun){
    	
    	String attname = comun.getAttname();
    	
    	if (attname.equalsIgnoreCase("fecha_operacion")){
    		campo.setDescripcion("Fecha de registro");
    	}
    	else if (attname.equalsIgnoreCase("comentario")){
    		campo.setDescripcion("Comentario");
    	}

    	return campo;
    }
    
    private static CampoFiltro setDescripcionHistoricoPropiedad(CampoFiltro campo, Comun comun){
    	
    	String attname = comun.getAttname();
    	
    	if (attname.equalsIgnoreCase("fecha_operacion")){
    		campo.setDescripcion("Fecha de registro");
    	}
    	else if (attname.equalsIgnoreCase("comentario")){
    		campo.setDescripcion("Comentario");
    	}

    	return campo;
    }
}

