package com.geopista.app.alptolocalgis;

import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPrincipal;

public interface IMainAlpToLocalGIS {
    public boolean applySecurityPolicy(GeopistaAcl acl, GeopistaPrincipal principal);
}
