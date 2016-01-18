/**
 * GeopistaGroup.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.security;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;



public class GeopistaGroup implements Group
{
    public static final String ROLES = "roles";
    
    private String name = null;
    private HashSet members = null;
    
    
   
    public GeopistaGroup(String n)
    {
        this.name = n;
        this.members = new HashSet();
    }
   
    /* ------------------------------------------------------------ */
    /**
     *
     * @param principal <description>
     * @return <description>
     */
    public synchronized boolean addMember(Principal principal)
    {
        return members.add(principal);
    }

    /**
     *
     * @param principal <description>
     * @return <description>
     */
    public synchronized boolean removeMember(Principal principal)
    {
        return members.remove(principal);
    }

    /**
     *
     * @param principal <description>
     * @return <description>
     */
    public boolean isMember(Principal principal)
    {
        return members.contains(principal);
    }


    
    /**
     *
     * @return <description>
     */
    public Enumeration members()
    {

        class MembersEnumeration implements Enumeration
        {
            private Iterator itor;
            
            public MembersEnumeration (Iterator itor)
            {
                this.itor = itor;
            }
            
            public boolean hasMoreElements ()
            {
                return this.itor.hasNext();
            }


            public Object nextElement ()
            {
                return this.itor.next();
            }
            
        }

        return new MembersEnumeration (members.iterator());
    }


    /**
     *
     * @return <description>
     */
    public int hashCode()
    {
        return getName().hashCode();
    }


    
    /**
     *
     * @param object <description>
          * @return <description>
     */
    public boolean equals(Object object)
    {
        if (! (object instanceof GeopistaGroup))
            return false;

        return ((GeopistaGroup)object).getName().equals(getName());
    }

    /**
     *
     * @return <description>
     */
    public String toString()
    {
        return getName();
    }

    /**
     *
     * @return <description>
     */
    public String getName()
    {
        
        return name;
    }

}
