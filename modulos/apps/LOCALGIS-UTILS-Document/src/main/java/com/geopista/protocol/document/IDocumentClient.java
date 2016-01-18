/**
 * IDocumentClient.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.document;

import java.io.File;
import java.util.Collection;
import java.util.Vector;

import com.geopista.feature.GeopistaFeature;

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
