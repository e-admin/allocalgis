package com.geopista.ui.plugin.io.dxf.reader;

import com.geopista.feature.GeopistaSchema;

public interface DxfXData {
    public boolean setGroup(short sGroup, String sData);
    public GeopistaSchema getSchema();
    public Object[] getAttributes();
    public String getAppID();
}