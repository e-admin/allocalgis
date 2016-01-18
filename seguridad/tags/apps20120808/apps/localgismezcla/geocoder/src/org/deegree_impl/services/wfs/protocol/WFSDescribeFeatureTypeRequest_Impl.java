/*----------------    FILE HEADER  ------------------------------------------

This file is part of deegree.
Copyright (C) 2001 by:
EXSE, Department of Geography, University of Bonn
http://www.giub.uni-bonn.de/exse/
lat/lon Fitzke/Fretter/Poth GbR
http://www.lat-lon.de

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

Contact:

Andreas Poth
lat/lon Fitzke/Fretter/Poth GbR
Meckenheimer Allee 176
53115 Bonn
Germany
E-Mail: poth@lat-lon.de

Jens Fitzke
Department of Geography
University of Bonn
Meckenheimer Allee 166
53115 Bonn
Germany
E-Mail: jens.fitzke@uni-bonn.de

                 
 ---------------------------------------------------------------------------*/
package org.deegree_impl.services.wfs.protocol;

import java.util.*;

import org.deegree.services.wfs.protocol.*;


/**
 * The function of the DescribeFeatureType interface is to
 * provide a client the means to request a schema definition of
 * any feature type that a particular WFS can service. The description
 * that is generated will define how a WFS expects a client application
 * to express the state of a feature to be created or the new state of
 * a feature to be updated. The result of a DescribeFeatureType request
 * is an XML document, describing one or more feature types serviced by
 * the WFS.
 *
 * <p>--------------------------------------------------------</p>
 *
 * @author Katharina Lupp <a href="mailto:k.lupp@web.de>Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
public class WFSDescribeFeatureTypeRequest_Impl extends WFSBasicRequest_Impl
    implements WFSDescribeFeatureTypeRequest {
    private ArrayList typeNames = null;
    private HashMap properties = null;
    private String outputFormat = null;

    /**
     * constructor initializing the class with the 
     */
    public WFSDescribeFeatureTypeRequest_Impl(String version, String id, HashMap vendorSpecificParameter, 
                                       WFSNative native_, String outputFormat, String[] typeNames) {
        super("DescribeFeatureType", version, id, vendorSpecificParameter, native_);
        this.typeNames = new ArrayList();
        this.properties = new HashMap();
        setOutputFormat(outputFormat);
        setTypeNames(typeNames);
    }

    /**
     * constructor initializing the class with the 
     */
    WFSDescribeFeatureTypeRequest_Impl(String version, String id, HashMap vendorSpecificParameter, 
                                       WFSNative native_, String outputFormat, String[] typeNames, 
                                       HashMap properties) {
        super("DescribeFeatureType", version, id, vendorSpecificParameter, native_);
        this.typeNames = new ArrayList();
        setOutputFormat(outputFormat);
        setTypeNames(typeNames);
        setProperties(properties);
    }

    /**
     * The outputFormat attribute, is used to indicate the schema
     * description language that should be used to describe a feature
     * schema. The only mandated format is XML-Schema denoted by the
     * XMLSCHEMA element; other vendor specific formats specified in the
     * capabilities document are also possible.
     */
    public String getOutputFormat() {
        return outputFormat;
    }

    /**
     * sets the <outputFormat>
     */
    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    /**
     * If a filter is not specified, then the optional typeName attribute
     * can be used to specify that all feature instances of a particular
     * feature type should be locked.
     */
    public String[] getTypeNames() {
        return (String[]) typeNames.toArray(new String[typeNames.size()]);
    }

    /**
     * adds the <TypeNames>
     */
    public void addTypeNames(String TypeNames) {
        this.typeNames.add(TypeNames);
    }

    /**
     * sets the <TypeNames>
     */
    public void setTypeNames(String[] typeNames) {
        this.typeNames.clear();

        if (typeNames != null) {
            for (int i = 0; i < typeNames.length; i++) {
                this.typeNames.add(typeNames[i]);
            }
        }
    }

    /**
     * returns the field names of each feature that shall be described.
     * if all fields shall be described (default) <tt>null</tt> will be
     * returned.<p></p>
     * notice: the OGC WFS 1.0.0 isn't clear at this point. at chapter 8
     * that describes the DescribeFeatureType operation it isn't specified
     * to define properties names within the request. At chapter 9 that
     * describes the GetFeature operation it is possible to select a
     * subset of properties of a feature type, that may not be vaild
     * against the schema that describes the full feature type.
     */
    public String[] getProperties(String featureType) {
        return (String[]) properties.get(featureType);
    }

    /**
     *
     *
     * @param properties 
     */
    public void setProperties(HashMap properties) {
        this.properties = properties;
    }

    /**
     *
     *
     * @return 
     */
    public String toString() {
        String ret = this.getClass().getName() + ":\n";
        ret += (outputFormat + "\n");
        ret += (typeNames + "\n");
        return ret;
    }
}

/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: WFSDescribeFeatureTypeRequest_Impl.java,v $
 * Revision 1.1  2009/07/09 07:25:33  miriamperez
 * Rama única LocalGISDOS
 *
 * Revision 1.1  2009/04/13 10:16:50  miriamperez
 * Meto en LocalGISPrivado el módulo del geocoder al completo
 *
 * Revision 1.1  2006/08/31 11:15:30  angeles
 * *** empty log message ***
 *
 * Revision 1.1.1.1  2006/08/17 11:34:01  angeles
 * no message
 *
 * Revision 1.3  2003/11/11 17:12:56  poth
 * no message
 *
 * Revision 1.2  2003/04/07 07:26:53  poth
 * no message
 *
 * Revision 1.1.1.1  2002/09/25 16:01:26  poth
 * no message
 *
 * Revision 1.9  2002/08/15 10:01:40  ap
 * no message
 *
 * Revision 1.8  2002/08/09 15:36:30  ap
 * no message
 *
 * Revision 1.7  2002/08/05 16:11:30  ap
 * no message
 *
 * Revision 1.6  2002/05/14 14:39:51  ap
 * no message
 *
 * Revision 1.5  2002/05/13 16:11:02  ap
 * no message
 *
 * Revision 1.4  2002/05/06 07:56:41  ap
 * no message
 *
 * Revision 1.3  2002/04/26 09:05:36  ap
 * no message
 *
 * Revision 1.1  2002/04/04 16:17:15  ap
 * no message
 *
 */
