package com.geopista.app.cementerios.utils;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.geopista.app.AppContext;
import com.geopista.app.cementerios.Constantes;
import com.geopista.app.cementerios.business.vo.UnidadEnterramientoExample.Criteria;
import com.geopista.protocol.cementerios.CampoFiltro;
import com.geopista.protocol.cementerios.Const;

public class AddFilter {

	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AddFilter.class);

	private static AddFilter instance;
    private AppContext aplicacion;
    private String locale;
    
	public static AddFilter getInstance() {
		if (instance == null) {
			instance = new AddFilter();
		}
		return instance;
	}

	public AddFilter(){
        this.aplicacion= (AppContext) AppContext.getApplicationContext();
        this.locale= aplicacion.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY, "es_ES", true);
	}
	
	private String mayusculaFirst(String c) {
		String resto = c.substring(1);
		char letra = c.charAt(0);
		letra = Character.toUpperCase(letra);
		return (Character.toString(letra) + resto);
	}

	private String removeGuiones(String c) {
		String[] lista;
		lista = c.split("_");
		String cadenaLista = "";

		for (int i = 0; i < lista.length; i++) {
			cadenaLista = cadenaLista.concat(mayusculaFirst(lista[i]));
		}
		return cadenaLista;
	}

	private String getOperacion(String nombreFiltro) {

		String operacion = "";

		if (nombreFiltro.equalsIgnoreCase("=")) {
			operacion = operacion.concat("EqualTo");
		} else if (nombreFiltro.equalsIgnoreCase("<>")) {
			operacion = operacion.concat("NotEqualTo");
		} else if (nombreFiltro.equalsIgnoreCase(">")) {
			operacion = operacion.concat("GreaterThan");
		} else if (nombreFiltro.equalsIgnoreCase(">=")) {
			operacion = operacion.concat("GreaterThanOrEqual");
		} else if (nombreFiltro.equalsIgnoreCase("<")) {
			operacion = operacion.concat("LessThan");
		} else if (nombreFiltro.equalsIgnoreCase("<=")) {
			operacion = operacion.concat("LessThanOrEqual");
		} else if (nombreFiltro.equalsIgnoreCase("LIKE")) {
			operacion = operacion.concat("Like");
		}
		return operacion;
	}

	private Parametros selectParams(CampoFiltro campoFiltro, String operacion) {

		Class[] tiposParams = new Class[1];
		Object[] params = null;

		Parametros parametros = new Parametros();
		
		/**ID**/
		if (campoFiltro.isID()) {
			String tabla = campoFiltro.getValorId();
		}
		
		/** Numeric **/
		if ((campoFiltro.isNumeric()) || (campoFiltro.isDominio())) {

			tiposParams[0] = Integer.class;
			params = new Object[1];

			if (campoFiltro.isNumeric()) {
				params[0] = new Integer((int) campoFiltro.getValorNumeric());
			} else {
				params[0] = Integer.parseInt((campoFiltro.getValorVarchar()));
			}

		} else if (campoFiltro.isVarchar()) {

			if (campoFiltro.getOperador().equalsIgnoreCase("LIKE")) {
				operacion = operacion.concat("Like");
			}
			if (campoFiltro.getOperador().equalsIgnoreCase("NOT LIKE")) {
				operacion = operacion.concat("NotLike");
			}
			tiposParams[0] = String.class;
			params = new Object[1];
			params[0] = (campoFiltro.getValorVarchar());

		} else if (campoFiltro.isDate()) {

			tiposParams[0] = Date.class;
			params = new Object[1];
			params[0] = (campoFiltro.getValorDate());

		} else if (campoFiltro.isBoolean()) {

			tiposParams[0] = Integer.class;
			params = new Object[1];

			if (campoFiltro.getValorBoolean()) {
				params[0] = new Integer(Constantes.TRUE);
			} else {
				params[0] = new Integer(Constantes.FALSE);
			}
		}

		parametros.setParams(params);
		parametros.setTiposParams(tiposParams);

		return parametros;

	}	
	
	private Parametros selectFindParams(CampoFiltro campoFiltro, String operacion) {

		Class[] tiposParams = new Class[1];
		Object[] params = null;

		Parametros parametros = new Parametros();

		/** Numeric **/
		if ((campoFiltro.isNumeric()) || (campoFiltro.isDominio())){
			
			tiposParams[0] = Integer.class;
			params = new Object[1];

			if (StringUtils.isNumeric(campoFiltro.getValorVarchar())){
				params[0] = Integer.parseInt((campoFiltro.getValorVarchar()));
			}
			
		} else if (campoFiltro.isVarchar()) {

			if (campoFiltro.getOperador().equalsIgnoreCase("LIKE")) {
				operacion = operacion.concat("Like");
				tiposParams[0] = String.class;
				params = new Object[1];
				String inicioYfin ="%";
				params[0] = (inicioYfin.concat(campoFiltro.getValorVarchar()).concat(inicioYfin));
			}
			else if (campoFiltro.getOperador().equalsIgnoreCase("NOT LIKE")) {
				operacion = operacion.concat("NotLike");
				tiposParams[0] = String.class;
				params = new Object[1];
				String inicioYfin ="%";
				params[0] = (inicioYfin.concat(campoFiltro.getValorVarchar()).concat(inicioYfin));

			}else{
				tiposParams[0] = String.class;
				params = new Object[1];
				params[0] = (campoFiltro.getValorVarchar());
			}
		} else if (campoFiltro.isDate()) {

			tiposParams[0] = Date.class;
			params = new Object[1];
			
			try {
				if (campoFiltro.getValorVarchar().length()==10){
					params[0] = Const.df.parse(campoFiltro.getValorVarchar());
				}else{
					params[0] = Const.df.parse("01-01-1700");
				}
			} catch (ParseException e) {
				logger.error("error parseando la fecha" + e);
			}
		} else if (campoFiltro.isBoolean()) {

			tiposParams[0] = Integer.class;
			params = new Object[1];
			if (campoFiltro.getValorVarchar().equalsIgnoreCase("true")){
				params[0] = new Integer(Constantes.TRUE);
			} else {
				params[0] = new Integer(Constantes.FALSE);
			}
		} 

		parametros.setParams(params);
		parametros.setTiposParams(tiposParams);

		return parametros;

	}	
	
	
	/**
	 * 
	 * @param criteria
	 * @param campoFiltro
	 * @return
	 */
	public com.geopista.app.cementerios.business.vo.BloqueExample.Criteria addBloqueFilter(
			com.geopista.app.cementerios.business.vo.BloqueExample.Criteria criteria,
			CampoFiltro campoFiltro, boolean esBuscar) {

		String methodCriteria = "and";
		String attr;
		String operacion = "";
		try {

			Parametros parametros = new Parametros();
			Class clase = com.geopista.app.cementerios.business.vo.BloqueExample.Criteria.class;
			
			Class[] tiposParams; // = new Class[1];
			Object[] params;// = null;

			attr = removeGuiones(campoFiltro.getNombre().trim());
			operacion = getOperacion(campoFiltro.getOperador());

			if (esBuscar){
				parametros = selectFindParams(campoFiltro, operacion);
			}else{
				parametros = selectParams(campoFiltro, operacion);
			}
			tiposParams = parametros.getTiposParams();
			params = parametros.getParams();

			methodCriteria = methodCriteria.concat(attr).concat(operacion);


			Method metodoCrit = null;

			metodoCrit = clase.getMethod(methodCriteria, tiposParams);

			if (params[0] == null) return null;
			
			criteria = (com.geopista.app.cementerios.business.vo.BloqueExample.Criteria) metodoCrit
					.invoke(criteria, params);

		} catch (Exception e) {
			logger.error("Error en el criteria" + e);
		}

		return criteria;
	}

	/**
	 * addUnidadEnterramientoFilter
	 * @param criteria
	 * @param campoFiltro
	 * @return
	 */
	public com.geopista.app.cementerios.business.vo.UnidadEnterramientoExample.Criteria
				addUnidadEnterramientoFilter(com.geopista.app.cementerios.business.vo.UnidadEnterramientoExample.Criteria criteria,
						CampoFiltro campoFiltro, boolean esBuscar) {
		
		String methodCriteria = "and";
		String attr;
		String operacion = "";
		try {

			Parametros parametros = new Parametros();
			Class clase = com.geopista.app.cementerios.business.vo.UnidadEnterramientoExample.Criteria.class;
			
			Class[] tiposParams; // = new Class[1];
			Object[] params;// = null;

			attr = removeGuiones(campoFiltro.getNombre().trim());
			operacion = getOperacion(campoFiltro.getOperador());
			
			if (esBuscar){
				parametros = selectFindParams(campoFiltro, operacion);
			}else{
				parametros = selectParams(campoFiltro, operacion);
			}
			tiposParams = parametros.getTiposParams();
			params = parametros.getParams();
			
			methodCriteria = methodCriteria.concat(attr).concat(operacion);

			Method metodoCrit = null;
			metodoCrit = clase.getMethod(methodCriteria, tiposParams);
			
			if (params[0] == null) return null;
			
			criteria = (Criteria) metodoCrit.invoke(criteria, params);
			

		} catch (Exception e) {
			logger.error("Error en el criteria" + e);
		}

		return criteria;
	}


	public com.geopista.app.cementerios.business.vo.ConcesionExample.Criteria addConcesionFilter(
			com.geopista.app.cementerios.business.vo.ConcesionExample.Criteria criteria,
			CampoFiltro campoFiltro, 
			boolean esBuscar) {

		String methodCriteria = "and";
		String attr;
		String operacion = "";
		try {

			Parametros parametros = new Parametros();
			Class clase = com.geopista.app.cementerios.business.vo.ConcesionExample.Criteria.class;

			Class[] tiposParams;// = new Class[1];
			Object[] params = null;

			attr = removeGuiones(campoFiltro.getNombre().trim());
			operacion = getOperacion(campoFiltro.getOperador());

			if (esBuscar){
				parametros = selectFindParams(campoFiltro, operacion);
			}else{
				parametros = selectParams(campoFiltro, operacion);
			}
			tiposParams = parametros.getTiposParams();
			params = parametros.getParams();

			methodCriteria = methodCriteria.concat(attr).concat(operacion);
			Method metodoCrit = null;
			metodoCrit = clase.getMethod(methodCriteria, tiposParams);

			if (params[0] == null) return null;
			
			criteria = (com.geopista.app.cementerios.business.vo.ConcesionExample.Criteria) metodoCrit
					.invoke(criteria, params);

		} catch (Exception e) {
			logger.error("Error en el criteria" + e);
		}

		return criteria;
	}
	
	
	/**
	 * 
	 * @param criteria
	 * @param campoFiltro
	 * @return
	 */
	public com.geopista.app.cementerios.business.vo.PersonaExample.Criteria addPersonaFilter(
			com.geopista.app.cementerios.business.vo.PersonaExample.Criteria criteria,
			CampoFiltro campoFiltro, boolean esBuscar) {

		String methodCriteria = "and";
		String attr;
		String operacion = "";
		try {

			Parametros parametros = new Parametros();
			Class clase = com.geopista.app.cementerios.business.vo.PersonaExample.Criteria.class;
			
			Class[] tiposParams; // = new Class[1];
			Object[] params;// = null;

			attr = removeGuiones(campoFiltro.getNombre().trim());
			operacion = getOperacion(campoFiltro.getOperador());
			
			if (esBuscar){
				parametros = selectFindParams(campoFiltro, operacion);
			}else{
				parametros = selectParams(campoFiltro, operacion);
			}
			tiposParams = parametros.getTiposParams();
			params = parametros.getParams();

			methodCriteria = methodCriteria.concat(attr).concat(operacion);


			Method metodoCrit = null;

			metodoCrit = clase.getMethod(methodCriteria, tiposParams);
			
			if (params[0] == null) return null;
			
			criteria = (com.geopista.app.cementerios.business.vo.PersonaExample.Criteria) metodoCrit
					.invoke(criteria, params);

		} catch (Exception e) {
			logger.error("Error en el criteria" + e);
		}

		return criteria;
	}
	
//	public com.geopista.app.cementerios.business.vo.DifuntoExample.Criteria addDifuntoFilter(
//			com.geopista.app.cementerios.business.vo.DifuntoExample.Criteria criteria,
//			CampoFiltro campoFiltro, boolean esBuscar) {
//
//		String methodCriteria = "and";
//		String attr;
//		String operacion = "";
//		try {
//
//			Parametros parametros = new Parametros();
//			Class clase = com.geopista.app.cementerios.business.vo.DifuntoExample.Criteria.class;
//			
//			Class[] tiposParams; // = new Class[1];
//			Object[] params;// = null;
//
//			attr = removeGuiones(campoFiltro.getNombre().trim());
//			operacion = getOperacion(campoFiltro.getOperador());
//			
//			if (esBuscar){
//				parametros = selectFindParams(campoFiltro, operacion);
//			}else{
//				parametros = selectParams(campoFiltro, operacion);
//			}
//			tiposParams = parametros.getTiposParams();
//			params = parametros.getParams();
//
//			methodCriteria = methodCriteria.concat(attr).concat(operacion);
//			Method metodoCrit = null;
//			metodoCrit = clase.getMethod(methodCriteria, tiposParams);
//
//			if (params[0] == null) return null;
//			
//			criteria = (com.geopista.app.cementerios.business.vo.DifuntoExample.Criteria) metodoCrit
//					.invoke(criteria, params);
//
//		} catch (Exception e) {
//			logger.error("Error en el criteria" + e);
//		}
//
//		return criteria;
//	}
	
	
	public com.geopista.app.cementerios.business.vo.DifuntoExample.Criteria addDifuntoFilter(
			com.geopista.app.cementerios.business.vo.DifuntoExample.Criteria criteria,
			CampoFiltro campoFiltro, boolean esBuscar) {

		String methodCriteria = "and";
		String attr;
		String operacion = "";
		try {

			Parametros parametros = new Parametros();
			Class clase = com.geopista.app.cementerios.business.vo.DifuntoExample.Criteria.class;
			
			Class[] tiposParams; // = new Class[1];
			Object[] params;// = null;

			attr = removeGuiones(campoFiltro.getNombre().trim());
			operacion = getOperacion(campoFiltro.getOperador());
			
			if (esBuscar){
				parametros = selectFindParams(campoFiltro, operacion);
			}else{
				parametros = selectParams(campoFiltro, operacion);
			}
			tiposParams = parametros.getTiposParams();
			params = parametros.getParams();

			methodCriteria = methodCriteria.concat(attr).concat(operacion);
			Method metodoCrit = null;
			metodoCrit = clase.getMethod(methodCriteria, tiposParams);

			if (params[0] == null) return null;
			
			criteria = (com.geopista.app.cementerios.business.vo.DifuntoExample.Criteria) metodoCrit
					.invoke(criteria, params);

		} catch (Exception e) {
			logger.error("Error en el criteria" + e);
		}

		return criteria;
	}
	
	
	/**
	 * addInhumacionFilter
	 * @param criteria
	 * @param campoFiltro
	 * @param esBuscar
	 * @return
	 */
	public com.geopista.app.cementerios.business.vo.InhumacionExample.Criteria addInhumacionFilter(
			com.geopista.app.cementerios.business.vo.InhumacionExample.Criteria criteria, CampoFiltro campoFiltro, boolean esBuscar) {

		String methodCriteria = "and";
		String attr;
		String operacion = "";
		try {

			Parametros parametros = new Parametros();
			Class clase = com.geopista.app.cementerios.business.vo.InhumacionExample.Criteria.class;
			
			Class[] tiposParams; 
			Object[] params;

			attr = removeGuiones(campoFiltro.getNombre().trim());
			operacion = getOperacion(campoFiltro.getOperador());
			
			if (esBuscar){
				parametros = selectFindParams(campoFiltro, operacion);
			}else{
				parametros = selectParams(campoFiltro, operacion);
			}
			tiposParams = parametros.getTiposParams();
			params = parametros.getParams();

			methodCriteria = methodCriteria.concat(attr).concat(operacion);

			Method metodoCrit = null;
			metodoCrit = clase.getMethod(methodCriteria, tiposParams);

			if (params[0] == null) return null;
			
			criteria = (com.geopista.app.cementerios.business.vo.InhumacionExample.Criteria) metodoCrit
					.invoke(criteria, params);

		} catch (Exception e) {
			logger.error("Error en el criteria" + e);
		}

		return criteria;
	}
	
	/**
	 * 
	 * @param criteria
	 * @param campoFiltro
	 * @param esBuscar
	 * @return
	 */
	public com.geopista.app.cementerios.business.vo.ExhumacionExample.Criteria addExhumacionFilter(
			com.geopista.app.cementerios.business.vo.ExhumacionExample.Criteria criteria, CampoFiltro campoFiltro, boolean esBuscar) {

		String methodCriteria = "and";
		String attr;
		String operacion = "";
		try {

			Parametros parametros = new Parametros();
			Class clase = com.geopista.app.cementerios.business.vo.ExhumacionExample.Criteria.class;
			
			Class[] tiposParams; 
			Object[] params;

			attr = removeGuiones(campoFiltro.getNombre().trim());
			operacion = getOperacion(campoFiltro.getOperador());
			
			if (esBuscar){
				parametros = selectFindParams(campoFiltro, operacion);
			}else{
				parametros = selectParams(campoFiltro, operacion);
			}
			tiposParams = parametros.getTiposParams();
			params = parametros.getParams();

			methodCriteria = methodCriteria.concat(attr).concat(operacion);

			Method metodoCrit = null;
			metodoCrit = clase.getMethod(methodCriteria, tiposParams);

			if (params[0] == null) return null;
			
			criteria = (com.geopista.app.cementerios.business.vo.ExhumacionExample.Criteria) metodoCrit
					.invoke(criteria, params);

		} catch (Exception e) {
			logger.error("Error en el criteria" + e);
		}

		return criteria;
	}
	
	/**
	 * addHistoricoFilter
	 * @param criteria
	 * @param campoFiltro
	 * @param esBuscar
	 * @return
	 */
	public com.geopista.app.cementerios.business.vo.HistoricoDifuntosExample.Criteria addHistoricoDifuntoFilter(
			com.geopista.app.cementerios.business.vo.HistoricoDifuntosExample.Criteria criteria, CampoFiltro campoFiltro, boolean esBuscar) {

		String methodCriteria = "and";
		String attr;
		String operacion = "";
		try {

			Parametros parametros = new Parametros();
			Class clase = com.geopista.app.cementerios.business.vo.HistoricoDifuntosExample.Criteria.class;
			
			Class[] tiposParams; 
			Object[] params;

			attr = removeGuiones(campoFiltro.getNombre().trim());
			operacion = getOperacion(campoFiltro.getOperador());
			
			if (esBuscar){
				parametros = selectFindParams(campoFiltro, operacion);
			}else{
				parametros = selectParams(campoFiltro, operacion);
			}
			tiposParams = parametros.getTiposParams();
			params = parametros.getParams();

			methodCriteria = methodCriteria.concat(attr).concat(operacion);

			Method metodoCrit = null;
			metodoCrit = clase.getMethod(methodCriteria, tiposParams);

			if (params[0] == null) return null;
			
			criteria = (com.geopista.app.cementerios.business.vo.HistoricoDifuntosExample.Criteria) metodoCrit
					.invoke(criteria, params);

		} catch (Exception e) {
			logger.error("Error en el criteria" + e);
		}

		return criteria;
	}
	
	
	public com.geopista.app.cementerios.business.vo.HistoricoPropiedadExample.Criteria addHistoricoPropiedadFilter(
			com.geopista.app.cementerios.business.vo.HistoricoPropiedadExample.Criteria criteria, CampoFiltro campoFiltro, boolean esBuscar) {

		String methodCriteria = "and";
		String attr;
		String operacion = "";
		try {

			Parametros parametros = new Parametros();
			Class clase = com.geopista.app.cementerios.business.vo.HistoricoPropiedadExample.Criteria.class;
			
			Class[] tiposParams; 
			Object[] params;

			attr = removeGuiones(campoFiltro.getNombre().trim());
			operacion = getOperacion(campoFiltro.getOperador());
			
			if (esBuscar){
				parametros = selectFindParams(campoFiltro, operacion);
			}else{
				parametros = selectParams(campoFiltro, operacion);
			}
			tiposParams = parametros.getTiposParams();
			params = parametros.getParams();

			methodCriteria = methodCriteria.concat(attr).concat(operacion);

			Method metodoCrit = null;
			metodoCrit = clase.getMethod(methodCriteria, tiposParams);

			if (params[0] == null) return null;
			
			criteria = (com.geopista.app.cementerios.business.vo.HistoricoPropiedadExample.Criteria) metodoCrit
					.invoke(criteria, params);

		} catch (Exception e) {
			logger.error("Error en el criteria" + e);
		}

		return criteria;
	}
	
	/**
	 * 
	 * @param criteria
	 * @param campoFiltro
	 * @param esBuscar
	 * @return
	 */
	public com.geopista.app.cementerios.business.vo.TarifaExample.Criteria addTarifaFilter(
			com.geopista.app.cementerios.business.vo.TarifaExample.Criteria criteria, CampoFiltro campoFiltro, boolean esBuscar) {

		String methodCriteria = "and";
		String attr;
		String operacion = "";
		try {

			Parametros parametros = new Parametros();
			Class clase = com.geopista.app.cementerios.business.vo.TarifaExample.Criteria.class;
			
			Class[] tiposParams; 
			Object[] params;

			attr = removeGuiones(campoFiltro.getNombre().trim());
			operacion = getOperacion(campoFiltro.getOperador());
			
			if (esBuscar){
				parametros = selectFindParams(campoFiltro, operacion);
			}else{
				parametros = selectParams(campoFiltro, operacion);
			}
			tiposParams = parametros.getTiposParams();
			params = parametros.getParams();

			methodCriteria = methodCriteria.concat(attr).concat(operacion);

			Method metodoCrit = null;
			metodoCrit = clase.getMethod(methodCriteria, tiposParams);

			if (params[0] == null) return null;
			
			criteria = (com.geopista.app.cementerios.business.vo.TarifaExample.Criteria) metodoCrit
					.invoke(criteria, params);

		} catch (Exception e) {
			logger.error("Error en el criteria" + e);
		}

		return criteria;
	}
	
	/**
	 * 
	 * @param criteria
	 * @param campoFiltro
	 * @param esBuscar
	 * @return
	 */
	public com.geopista.app.cementerios.business.vo.IntervencionExample.Criteria addIntervencionFilter(
			com.geopista.app.cementerios.business.vo.IntervencionExample.Criteria criteria, CampoFiltro campoFiltro, boolean esBuscar) {

		String methodCriteria = "and";
		String attr;
		String operacion = "";
		try {

			Parametros parametros = new Parametros();
			Class clase = com.geopista.app.cementerios.business.vo.IntervencionExample.Criteria.class;
			
			Class[] tiposParams; 
			Object[] params;

			attr = removeGuiones(campoFiltro.getNombre().trim());
			operacion = getOperacion(campoFiltro.getOperador());
			
			if (esBuscar){
				parametros = selectFindParams(campoFiltro, operacion);
			}else{
				parametros = selectParams(campoFiltro, operacion);
			}
			tiposParams = parametros.getTiposParams();
			params = parametros.getParams();

			methodCriteria = methodCriteria.concat(attr).concat(operacion);

			Method metodoCrit = null;
			metodoCrit = clase.getMethod(methodCriteria, tiposParams);

			if (params[0] == null) return null;
			
			criteria = (com.geopista.app.cementerios.business.vo.IntervencionExample.Criteria) metodoCrit
					.invoke(criteria, params);

		} catch (Exception e) {
			logger.error("Error en el criteria" + e);
		}

		return criteria;
	}
	
	/**
	 * 
	 * @param criteria
	 * @param campoFiltro
	 * @param esBuscar
	 * @return
	 */
	public com.geopista.app.cementerios.business.vo.DatosFallecimientoExample.Criteria addDatosFallecimientoFilter(
			com.geopista.app.cementerios.business.vo.DatosFallecimientoExample.Criteria criteria, CampoFiltro campoFiltro, boolean esBuscar) {

		String methodCriteria = "and";
		String attr;
		String operacion = "";
		try {

			Parametros parametros = new Parametros();
			Class clase = com.geopista.app.cementerios.business.vo.DatosFallecimientoExample.Criteria.class;
			
			Class[] tiposParams; 
			Object[] params;

			attr = removeGuiones(campoFiltro.getNombre().trim());
			operacion = getOperacion(campoFiltro.getOperador());
			
			if (esBuscar){
				parametros = selectFindParams(campoFiltro, operacion);
			}else{
				parametros = selectParams(campoFiltro, operacion);
			}
			tiposParams = parametros.getTiposParams();
			params = parametros.getParams();

			methodCriteria = methodCriteria.concat(attr).concat(operacion);

			Method metodoCrit = null;
			metodoCrit = clase.getMethod(methodCriteria, tiposParams);

			if (params[0] == null) return null;
			
			criteria = (com.geopista.app.cementerios.business.vo.DatosFallecimientoExample.Criteria) metodoCrit
					.invoke(criteria, params);

		} catch (Exception e) {
			logger.error("Error en el criteria" + e);
		}

		return criteria;
	}
}
