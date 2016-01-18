package com.geopista.app.catastro.gestorcatastral;

import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPrincipal;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 09-feb-2007
 * Time: 11:02:44
 * To change this template use File | Settings | File Templates.
 */
public interface IMainCatastro {
    public boolean applySecurityPolicy(GeopistaAcl acl, GeopistaPrincipal principal);
}
