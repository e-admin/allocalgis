/**
 * LocalgisJAASGroup.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.security.model;

import java.io.Serializable;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.jetty.plus.jaas.JAASGroup;

public class LocalgisJAASGroup implements Serializable, Group {

	public static final String ROLES = "__roles__";

	private String _name = null;
	private HashSet<Principal> _members = null;

	public LocalgisJAASGroup() {
		this._name = "";
		this._members = new HashSet<Principal>();
	}
	
	public LocalgisJAASGroup(String n) {
		this._name = n;
		this._members = new HashSet<Principal>();
	}

	/* ------------------------------------------------------------ */
	/**
	 * 
	 * @param members
	 *            <description>
	 * @return <description>
	 */
	public void setMembers(Enumeration<? extends Principal> membersEnum) {
		while(membersEnum.hasMoreElements()){
			 Principal principal = membersEnum.nextElement();
			 addMember(principal);			
		}
	}
	
	/**
	 * 
	 * @param principal
	 *            <description>
	 * @return <description>
	 */
	public synchronized boolean addMember(Principal principal) {
		return _members.add(principal);
	}

	/**
	 * 
	 * @param principal
	 *            <description>
	 * @return <description>
	 */
	public synchronized boolean removeMember(Principal principal) {
		return _members.remove(principal);
	}

	/**
	 * 
	 * @param principal
	 *            <description>
	 * @return <description>
	 */
	public boolean isMember(Principal principal) {
		return _members.contains(principal);
	}

	/**
	 * 
	 * @return <description>
	 */
	public Enumeration<? extends Principal> members() {

		class MembersEnumeration implements Enumeration<Principal> {
			private Iterator<? extends Principal> itor;

			public MembersEnumeration(Iterator<? extends Principal> itor) {
				this.itor = itor;
			}

			public boolean hasMoreElements() {
				return this.itor.hasNext();
			}

			public Principal nextElement() {
				return this.itor.next();
			}

		}

		return new MembersEnumeration(_members.iterator());
	}

	/**
	 * 
	 * @return <description>
	 */
	public int hashCode() {
		return getName().hashCode();
	}

	/**
	 * 
	 * @param object
	 *            <description>
	 * @return <description>
	 */
	public boolean equals(Object object) {
		if (!(object instanceof JAASGroup))
			return false;

		return ((LocalgisJAASGroup) object).getName().equals(getName());
	}

	/**
	 * 
	 * @return <description>
	 */
	public String toString() {
		return getName();
	}

	/**
	 * 
	 * @return <description>
	 */
	public String getName() {

		return _name;
	}

}
