/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.cu.impl;

import java.lang.reflect.ParameterizedType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.dc.a21l.base.modelo.ExcepcionAplicacionBase;

public final class EstrategiaCreacionNuevo<S, D> implements EstrategiaCreacion<S, D> {
	private static final Logger log = LoggerFactory.getLogger(EstrategiaCreacionNuevo.class);

	private Class<D> clasePrototipo;

	public EstrategiaCreacionNuevo(Object obxectoBuscaPrototipo) {
		this.clasePrototipo = averiguaClasePrototipo(obxectoBuscaPrototipo);
	}

	public EstrategiaCreacionNuevo(Class<D> clasePrototipo) {
		this.clasePrototipo = clasePrototipo;
	}

	public final D crea(S s) {
		return getNovaInstancia();
	}

	private Class<D> averiguaClasePrototipo(Object obxectoBuscaPrototipo) {
		Class<?> clazz = obxectoBuscaPrototipo.getClass();

		while (!(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
			clazz = clazz.getSuperclass();
		}

		return (Class) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[1];
	}

	private D getNovaInstancia() {
		try {
			return this.clasePrototipo.newInstance();
		} catch (Exception ex) {
			log.debug("Error creando Dto", ex);
			throw new ExcepcionAplicacionBase("Error creando instancia: "+ex.getMessage(),ex);
		} 
	}
}
