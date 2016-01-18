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

    
