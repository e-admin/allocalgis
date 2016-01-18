/**
 * GeopistaPrincipal.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
// ======================================================================
//  Copyright (C) 2002 by Mortbay Consulting Ltd
// $Id: GeopistaPrincipal.java,v 1.1 2009/07/03 12:32:04 miriamperez Exp $ 
// ======================================================================

package com.geopista.security;

import java.io.Serializable;
import java.security.Principal;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:32:04 $
 *          $Name:  $
 *          $RCSfile: GeopistaPrincipal.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class GeopistaPrincipal implements Principal, Serializable
{
    private String name = null;
    private String idSesion=null;

    public GeopistaPrincipal() {
    }

     public void setName(String name) {
        this.name = name;
    }

    public void setIdSesion(String idSesion) {
        this.idSesion = idSesion;
    }
    public GeopistaPrincipal(String userName)
    {
        this.name = userName;
    }
    public GeopistaPrincipal(String userName,String sIdSesion)
    {
        this.name = userName;
        this.idSesion = sIdSesion;
    }



    public boolean equals (Object p)
    {
        if (! (p instanceof Principal))
            return false;

        return getName().equals(((Principal)p).getName());
    }


    public int hashCode ()
    {
        return getName().hashCode();
    }


    public String getName ()
    {
        return this.name;
    }

    public String getIdSesion ()
    {
        return this.idSesion;
    }

    public String toString ()
    {
        return getName();
    }


}

    
