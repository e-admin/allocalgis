package com.geopista.app.inicio;

import java.net.URL;

public class HTMLLoader {
    public static URL getPath(String filename) {
        return HTMLLoader.class.getResource(filename);
    }
}