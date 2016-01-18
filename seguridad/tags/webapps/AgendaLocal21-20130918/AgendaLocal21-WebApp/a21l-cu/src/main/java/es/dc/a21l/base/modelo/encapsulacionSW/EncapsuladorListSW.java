/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.modelo.encapsulacionSW;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement(name="encapsuladorListSW")
public class EncapsuladorListSW <T>  implements List<T>{
	
	private List<T> lista;
	
	public EncapsuladorListSW(){
		lista= new ArrayList<T>();
	}
	
	public EncapsuladorListSW(List<T> listaIncial){
		lista= new ArrayList<T>(listaIncial);
	}
	
	@XmlElement(name="lista")
	public List<T> getLista() {
		return lista;
	}

	public void setLista(List<T> lista) {
		this.lista = lista;
	}


//FUNCIONES DE LA INTERFAZ LIST

	public int size() {
		return lista.size();
	}

	public boolean isEmpty() {
		return lista.isEmpty();
	}

	public boolean contains(Object o) {
		return lista.contains(o);
	}

	public Iterator<T> iterator() {
		return lista.iterator();
	}

	public Object[] toArray() {
		return lista.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return lista.toArray(a);
	}

	public boolean add(T e) {
		return lista.add(e);
	}

	public boolean remove(Object o) {
		return lista.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return lista.containsAll(c);
	}

	public boolean addAll(Collection<? extends T> c) {
		return lista.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		return lista.addAll(index, c);
	}

	public boolean removeAll(Collection<?> c) {
		return lista.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return lista.retainAll(c);
	}

	public void clear() {
		 lista.clear();
		
	}

	public T get(int index) {
		return lista.get(index);
	}

	public T set(int index, T element) {
		return lista.set(index, element);
	}

	public void add(int index, T element) {
		lista.add(index, element);
	}

	public T remove(int index) {
		return lista.remove(index);
	}

	public int indexOf(Object o) {
		return lista.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return lista.lastIndexOf(o);
	}

	public ListIterator<T> listIterator() {
		return lista.listIterator();
	}

	public ListIterator<T> listIterator(int index) {
		return lista.listIterator(index);
	}

	public List<T> subList(int fromIndex, int toIndex) {
		return lista.subList(fromIndex, toIndex);
	}

}
