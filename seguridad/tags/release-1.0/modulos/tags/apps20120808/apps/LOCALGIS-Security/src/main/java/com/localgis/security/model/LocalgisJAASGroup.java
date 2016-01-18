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
