package com.geopista.app.eiel;

import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPrincipal;

public interface IMainLocalGISEIEL {
    public boolean applySecurityPolicy(GeopistaAcl acl, GeopistaPrincipal principal);
}
