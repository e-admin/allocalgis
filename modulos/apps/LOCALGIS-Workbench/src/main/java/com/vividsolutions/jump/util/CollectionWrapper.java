/**
 * CollectionWrapper.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.util;

import java.util.Collection;
import java.util.Iterator;

public abstract class CollectionWrapper implements Collection {
	public abstract Collection getCollection();

	public int size() {
		return getCollection().size();
	}

	public void clear() {
		getCollection().clear();
	}

	public boolean isEmpty() {
		return getCollection().isEmpty();
	}

	public Object[] toArray() {
		return getCollection().toArray();
	}

	public boolean add(Object o) {
		return getCollection().add(o);
	}

	public boolean contains(Object o) {
		return getCollection().contains(o);
	}

	public boolean remove(Object o) {
		return getCollection().remove(o);
	}

	public boolean addAll(Collection c) {
		return getCollection().addAll(c);
	}

	public boolean containsAll(Collection c) {
		return getCollection().containsAll(c);
	}

	public boolean removeAll(Collection c) {
		return getCollection().removeAll(c);
	}

	public boolean retainAll(Collection c) {
		return getCollection().retainAll(c);
	}

	public Iterator iterator() {
		return getCollection().iterator();
	}

	public Object[] toArray(Object[] a) {
		return getCollection().toArray(a);
	}
}