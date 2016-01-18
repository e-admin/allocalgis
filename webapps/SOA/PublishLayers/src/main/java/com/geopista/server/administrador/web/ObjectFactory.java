/**
 * ObjectFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.server.administrador.web;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.geopista.server.administrador.web package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _LocalgisLayerService_QNAME = new QName("http://web.administrador.server.geopista.com", "service");
    private final static QName _LocalgisLayerXml_QNAME = new QName("http://web.administrador.server.geopista.com", "xml");
    private final static QName _LocalgisLayerMapaAtributos_QNAME = new QName("http://web.administrador.server.geopista.com", "mapaAtributos");
    private final static QName _LocalgisLayerTime_QNAME = new QName("http://web.administrador.server.geopista.com", "time");
    private final static QName _LocalgisLayerLayerquery_QNAME = new QName("http://web.administrador.server.geopista.com", "layerquery");
    private final static QName _LocalgisLayerSrid_QNAME = new QName("http://web.administrador.server.geopista.com", "srid");
    private final static QName _LocalgisLayerLayername_QNAME = new QName("http://web.administrador.server.geopista.com", "layername");
    private final static QName _LocalgisLayerColumnTime_QNAME = new QName("http://web.administrador.server.geopista.com", "columnTime");
    private final static QName _LocalgisLayerStyleName_QNAME = new QName("http://web.administrador.server.geopista.com", "styleName");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.geopista.server.administrador.web
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ConfigureLayerAndStylesFile }
     * 
     */
    public ConfigureLayerAndStylesFile createConfigureLayerAndStylesFile() {
        return new ConfigureLayerAndStylesFile();
    }

    /**
     * Create an instance of {@link ConfigureLayerAndStylesFileResponse }
     * 
     */
    public ConfigureLayerAndStylesFileResponse createConfigureLayerAndStylesFileResponse() {
        return new ConfigureLayerAndStylesFileResponse();
    }

    /**
     * Create an instance of {@link LocalgisLayer }
     * 
     */
    public LocalgisLayer createLocalgisLayer() {
        return new LocalgisLayer();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.administrador.server.geopista.com", name = "service", scope = LocalgisLayer.class)
    public JAXBElement<String> createLocalgisLayerService(String value) {
        return new JAXBElement<String>(_LocalgisLayerService_QNAME, String.class, LocalgisLayer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.administrador.server.geopista.com", name = "xml", scope = LocalgisLayer.class)
    public JAXBElement<String> createLocalgisLayerXml(String value) {
        return new JAXBElement<String>(_LocalgisLayerXml_QNAME, String.class, LocalgisLayer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.administrador.server.geopista.com", name = "mapaAtributos", scope = LocalgisLayer.class)
    public JAXBElement<String> createLocalgisLayerMapaAtributos(String value) {
        return new JAXBElement<String>(_LocalgisLayerMapaAtributos_QNAME, String.class, LocalgisLayer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.administrador.server.geopista.com", name = "time", scope = LocalgisLayer.class)
    public JAXBElement<String> createLocalgisLayerTime(String value) {
        return new JAXBElement<String>(_LocalgisLayerTime_QNAME, String.class, LocalgisLayer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.administrador.server.geopista.com", name = "layerquery", scope = LocalgisLayer.class)
    public JAXBElement<String> createLocalgisLayerLayerquery(String value) {
        return new JAXBElement<String>(_LocalgisLayerLayerquery_QNAME, String.class, LocalgisLayer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.administrador.server.geopista.com", name = "srid", scope = LocalgisLayer.class)
    public JAXBElement<String> createLocalgisLayerSrid(String value) {
        return new JAXBElement<String>(_LocalgisLayerSrid_QNAME, String.class, LocalgisLayer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.administrador.server.geopista.com", name = "layername", scope = LocalgisLayer.class)
    public JAXBElement<String> createLocalgisLayerLayername(String value) {
        return new JAXBElement<String>(_LocalgisLayerLayername_QNAME, String.class, LocalgisLayer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.administrador.server.geopista.com", name = "columnTime", scope = LocalgisLayer.class)
    public JAXBElement<String> createLocalgisLayerColumnTime(String value) {
        return new JAXBElement<String>(_LocalgisLayerColumnTime_QNAME, String.class, LocalgisLayer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://web.administrador.server.geopista.com", name = "styleName", scope = LocalgisLayer.class)
    public JAXBElement<String> createLocalgisLayerStyleName(String value) {
        return new JAXBElement<String>(_LocalgisLayerStyleName_QNAME, String.class, LocalgisLayer.class, value);
    }

}
