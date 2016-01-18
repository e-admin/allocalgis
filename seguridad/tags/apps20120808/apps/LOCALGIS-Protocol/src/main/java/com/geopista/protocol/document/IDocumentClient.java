package com.geopista.protocol.document;

import com.geopista.feature.GeopistaFeature;
import com.geopista.server.administradorCartografia.ACException;

import java.util.Collection;
import java.util.Vector;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 25-abr-2006
 * Time: 12:11:21
 * To change this template use File | Settings | File Templates.
 */
public interface IDocumentClient {

    public static final String	mensajeXML	= "mensajeXML";
    public static final String	idMunicipio	= "idMunicipio";
    public static final String	IdApp		= "IdApp";

    public abstract Collection getDocuments() throws Exception;
    public abstract Collection getAttachedDocuments(GeopistaFeature feature) throws Exception;
    public Collection getAttachedImages(GeopistaFeature feature) throws Exception;
    public abstract byte[] getAttachedByteStream(DocumentBean document) throws Exception;
    public abstract DocumentBean attachDocument(Object[] features,  DocumentBean document) throws Exception;
    public abstract DocumentBean attachDocument(Object[] features, DocumentBean document, File f) throws Exception;
    public abstract DocumentBean attachDocumentByteStream(Object[] features,  DocumentBean document) throws Exception;
    public abstract DocumentBean updateDocument(DocumentBean document) throws Exception;
    public abstract DocumentBean updateDocument(DocumentBean document, File f) throws Exception;
    public abstract DocumentBean updateDocumentByteStream(DocumentBean document) throws Exception;
    public abstract void detachDocument(GeopistaFeature feature, DocumentBean document) throws Exception;
    public abstract void linkDocument(Vector features, DocumentBean document) throws Exception;
}
