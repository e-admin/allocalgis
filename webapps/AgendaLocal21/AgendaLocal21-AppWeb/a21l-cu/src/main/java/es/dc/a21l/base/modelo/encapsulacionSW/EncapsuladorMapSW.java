/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.modelo.encapsulacionSW;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@SuppressWarnings("restriction")
@XmlRootElement(name="encapsuladorMapSW")
public class EncapsuladorMapSW <K,V> implements Map<K, V> {

	private Map<K, V> mapa;
	
	public EncapsuladorMapSW(){
		mapa= new HashMap<K, V>();
	}
	
	@XmlJavaTypeAdapter(MapAdapter.class)
	public Map<K, V> getMapa() {
		return mapa;
	}
	
	public void setMapa(Map<K, V> mapa) {
		this.mapa = mapa;
	}



	public int size() {
		return mapa.size();
	}

	public boolean isEmpty() {
		return mapa.isEmpty();
	}

	public boolean containsKey(Object key) {
		return mapa.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return mapa.containsValue(value);
	}

	public V get(Object key) {
		return mapa.get(key);
	}

	public V put(K key, V value) {
		return mapa.put(key, value);
	}

	public V remove(Object key) {
		return mapa.remove(key);
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		mapa.putAll(m);
		
	}

	public void clear() {
		mapa.clear();
	}

	public Set<K> keySet() {
		return mapa.keySet();
	}

	public Collection<V> values() {
		return mapa.values();
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return mapa.entrySet();
	}

}
