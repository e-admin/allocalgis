package com.geopista.app.backup;

import java.util.HashMap;
import java.util.Map;

public class InformacionTabla {

    private Map mapInformacion;
    
    public InformacionTabla() {
        mapInformacion = new HashMap();
    }

    public void putInformacion(String key, Object valor) {
        mapInformacion.put(key, valor);
    }

    public Object getInformacion(String key) {
        return mapInformacion.get(key);
    }
}
