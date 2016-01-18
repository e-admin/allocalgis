package com.geopista.protocol.metadatos;

import java.util.Vector;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 25-ago-2004
 * Time: 11:35:59
 */
public class MD_LegalConstraint {
    String id;
    String uselimitation;
    String otherconstraint;
    Vector access;
    Vector use;

    public MD_LegalConstraint() {
    }

    public Vector getAccess() {
        return access;
    }
    public void addAccess(String sAccess)
   {
       if (this.access==null) this.access=new Vector();
       access.add(sAccess);
   }


    public void setAccess(Vector access) {
        this.access = access;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOtherconstraint() {
        return otherconstraint;
    }

    public void setOtherconstraint(String otherconstraint) {
        this.otherconstraint = otherconstraint;
    }

    public Vector getUse() {
        return use;
    }

    public void setUse(Vector use) {
        this.use = use;
    }
    public void addUse(String sUse)
    {
        if (this.use==null) this.use=new Vector();
        use.add(sUse);
    }
    public String getUselimitation() {
        return uselimitation;
    }

    public void setUselimitation(String uselimitation) {
        this.uselimitation = uselimitation;
    }


}
