/**
 * ListWrapper.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.util;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public abstract class ListWrapper extends CollectionWrapper implements List {
	public List getList() {
		return (List) getCollection();
	}

	public Object get(int index) {
		return getList().get(index);
	}

	public Object remove(int index) {
		return getList().remove(index);
	}

	public void add(int index, Object element) {
		getList().add(index, element);
	}

	public int indexOf(Object o) {
		return getList().indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return getList().lastIndexOf(o);
	}

	public boolean addAll(int index, Collection c) {
		return getList().addAll(index, c);
	}

	public List subList(int fromIndex, int toIndex) {
		return getList().subList(fromIndex, toIndex);
	}

	public ListIterator listIterator() {
		return getList().listIterator();
	}

	public ListIterator listIterator(int index) {
		return getList().listIterator(index);
	}

	public Object set(int index, Object element) {
		return getList().set(index, element);
	}
}