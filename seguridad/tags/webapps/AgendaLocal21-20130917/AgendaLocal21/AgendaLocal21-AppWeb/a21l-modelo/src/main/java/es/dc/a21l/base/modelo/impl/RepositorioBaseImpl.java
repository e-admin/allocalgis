/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.modelo.impl;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.dc.a21l.base.modelo.EntidadBase;
import es.dc.a21l.base.modelo.RepositorioBase;

public class RepositorioBaseImpl<T extends EntidadBase> implements RepositorioBase<T> {
	private static final Logger log = LoggerFactory.getLogger(RepositorioBaseImpl.class);


	private EntityManager entityManager;
	private Class<T> persistentClass;

	private String SELECT_TODOS;
	
	public RepositorioBaseImpl() {
	    Class<?> clazz = getClass();
	    while (!(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
	      clazz = clazz.getSuperclass();
	    }
	    this.persistentClass = ((Class)((ParameterizedType)clazz.getGenericSuperclass()).getActualTypeArguments()[0]);
		this.SELECT_TODOS = ("select Result from " + this.persistentClass.getName() + " as Result order by Id desc");
	    log.debug("Creado Repositorio con clase " + this.persistentClass.toString());
	}

	public T carga(long id) {
	    T entidad = getEntityManager().find(getPersistentClass(), id);
		return entidad;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public T guarda(T entidade) {
		log.debug("Gardando "+entidade);
		getEntityManager().persist(entidade);
		return entidade;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void borra(long id) {
		log.debug("Borrando por id: "+id);
		EntidadBase entidad = carga(id);
		log.debug("Entidade a borrar: "+entidad);
		if (entidad != null) {
			getEntityManager().remove(entidad);
		}
	}

	public boolean existe(long id) {
		log.debug("Comprobando si existe entidad "+getPersistentClass()+" por id: "+id);
		return carga(id) != null;
	}

	public List<T> cargaTodos() {
		log.debug("Cargando todos por tipo "+getPersistentClass());
		return new ArrayList(getEntityManager().createQuery(this.SELECT_TODOS).getResultList());
	}
	
	@Autowired
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	public void setPersistentClass(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}


}
