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

import java.util.ArrayList;
import java.util.HashMap;

import org.deegree.services.wfs.filterencoding.Filter;
import org.deegree.services.wfs.protocol.WFSGetFeatureRequest;
import org.deegree.services.wfs.protocol.WFSNative;
import org.deegree.services.wfs.protocol.WFSQuery;
import org.deegree.xml.Marshallable;
import org.deegree_impl.tools.Debug;


/**
 * The GetFeature interface can be used to package one or more
 * query descriptions into a single request. The results of all
 * queries packaged in a GetFeature request are
 * concatenated to produce the result set.
 *
 * <p>--------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
public class WFSGetFeatureRequest_Impl extends WFSBasicRequest_Impl implements 
                                    WFSGetFeatureRequest, Marshallable {
    private ArrayList featureIds = null;
    private ArrayList propertyNames = null;
    private ArrayList query = null;
    private ArrayList typeNames = null;
    private Filter filter = null;
    private String handle = null;
    private String outputFormat = null;
    private int maxFeatures = 0;
    private int startPosition = 0;

    /**
     * constructor initializing the class with the <WFSGetFeatureRequest>
     */
    public WFSGetFeatureRequest_Impl(String version, String id, HashMap vendorSpecificParameter, 
                              WFSNative native_, String outputFormat, String handle, Filter filter, 
                              int maxFeatures, int startPosition, WFSQuery[] query, 
                              String[] propertyNames, String[] featureIds, String[] typeNames) {
        super("GetFeature", version, id, vendorSpecificParameter, native_);
        this.query = new ArrayList();
        this.propertyNames = new ArrayList();
        this.featureIds = new ArrayList();
        this.typeNames = new ArrayList();
        setOutputFormat(outputFormat);
        setHandle(handle);
        setFilter(filter);
        setMaxFeatures(maxFeatures);
        setStartPosition(startPosition);
        setQuery(query);
        setPropertyNames(propertyNames);
        setFeatureIds(featureIds);
        setTypeNames(typeNames);
    }

    /**
     * The outputFormat attribute defines the format to use to
     * generate the result set. Vendor specific formats, declared in
     * the capabilities document are possible. The WFS-specs implies
     * GML as default output format.
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
     * The handle attribute is included to allow a client to associate
     * a mnemonic name to the <Query> request. The purpose of the handle
     * attribute is to provide an error handling mechanism for locating
     * a statement that might fail.
     */
    public String getHandle() {
        return handle;
    }

    /**
     *
     *
     * @param handle 
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * The query defines which feature type to query, what properties
     * to retrieve and what constraints (spatial and non-spatial) to
     * apply to those properties. <p>
     * only used for xml-coded requests
     */
    public WFSQuery[] getQuery() {
        return (WFSQuery[]) query.toArray(new WFSQuery[query.size()]);
    }

    /**
     * adds the <Query>
     */
    public void addQuery(WFSQuery query) {
        this.query.add(query);
    }

    /**
     * sets the <Query>
     */
    public void setQuery(WFSQuery[] query) {
        this.query.clear();

        if (query != null) {
            for (int i = 0; i < query.length; i++) {
                this.query.add(query[i]);
            }
        }
    }

    /**
     * The optional maxFeatures attribute can be used to limit the number
     * of features that a GetFeature request retrieves. Once the maxFeatures
     * limit is reached, the result set is truncated at that point.
     */
    public int getMaxFeatures() {
        return maxFeatures;
    }

    /**
     * sets the <MaxFeatures>
     */
    public void setMaxFeatures(int maxFeatures) {
        this.maxFeatures = maxFeatures;
    }

    /**
     * The startPosition parameter identifies the first result set entry
     * to be returned specified the default is the first record
     */
    public int getStartPosition() {
        return startPosition;
    }

    /**
     * sets the <StartPosition>
     */
    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    /**
     * The property names is used to enumerate the feature properties
     * or attributes that should be selected. If no property names are
     * specified then all properties should be fetched.<p>
     * only use for name-value-pair encoded requests
     */
    public String[] getPropertyNames() {
        return (String[]) propertyNames.toArray(new String[propertyNames.size()]);
    }

    /**
     * adds the <PropertyNames>
     */
    public void addPropertyNames(String propertyNames) {
        this.propertyNames.add(propertyNames);
    }

    /**
     * sets the <PropertyNames>
     */
    public void setPropertyNames(String[] propertyNames) {
        this.propertyNames.clear();

        if (propertyNames != null) {
            for (int i = 0; i < propertyNames.length; i++) {
                this.propertyNames.add(propertyNames[i]);
            }
        }
    }

    /**
     * A list of feature identifiers upon which the specified operation
     * shall be applied. Optional. No default.<p>
     * Only used for name-value-pair encoded requests
     */
    public String[] getFeatureIds() {
        return (String[]) featureIds.toArray(new String[featureIds.size()]);
    }

    /**
     * adds the <FeatureIds>
     */
    public void addFeatureIds(String FeatureIds) {
        this.featureIds.add(featureIds);
    }

    /**
     * sets the <FeatureIds>
     */
    public void setFeatureIds(String[] featureIds) {
        this.featureIds.clear();

        if (featureIds != null) {
            for (int i = 0; i < featureIds.length; i++) {
                this.featureIds.add(featureIds[i]);
            }
        }
    }

    /**
     * A list of feature type names to query. Optional. No default.<p>
     * only used for name-value-pair encoded requests
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
     * A filter specification describes a set of features to operate upon.
     * The format of the filter is defined in the OGC Filter Encoding
     * Specification. Optional. No default. Prerequisite: TYPENAME
     */
    public Filter getFilter() {
        return filter;
    }

    /**
     * sets the <Filter>
     */
    public void setFilter(Filter filter) {
        this.filter = filter;
    }
    
    /** exports the request in its XML representation
     *
     */
    public String exportAsXML() {
        Debug.debugMethodBegin( this, "exportAsXML" );
        
        StringBuffer sb = new StringBuffer( 5000 );
        sb.append( "<wfs:GetFeature xmlns:wfs=\"http://www.opengis.net/wfs\" " );
        sb.append( "service=\"WFS\" version=\"" + getVersion() + "\" " );
        sb.append( "outputFormat=\"" + getOutputFormat() + "\" >");
        WFSQuery[] queries = getQuery();
        for (int i = 0; i < queries.length; i++) {
            sb.append( queries[i].exportAsXML() );
        }
        sb.append( "</wfs:GetFeature>" );
        
        Debug.debugMethodEnd(); 
        return sb.toString();
    }

    /**
     *
     *
     * @return 
     */
    public String toString() {
        String ret = null;
        ret = "WFSGetFeatureRequest: { \n ";                
        for (int i = 0; i < typeNames.size(); i++) {
            ret += ("typeName = " + typeNames.get(i) + "\n");
        }
        ret += ("featureIds = " + featureIds + "\n");
        ret += "outputFormat = " + outputFormat + "\n";
        ret += ("handle = " + handle + "\n");        
        ret += ("query = " + query + "\n");
        ret += "}\n";
        return ret;
    }
}

/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: WFSGetFeatureRequest_Impl.java,v $
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
 * Revision 1.10  2004/02/09 08:00:21  poth
 * no message
 *
 * Revision 1.9  2004/01/26 08:10:37  poth
 * no message
 *
 * Revision 1.8  2003/11/11 17:12:56  poth
 * no message
 *
 * Revision 1.7  2003/07/21 07:50:47  poth
 * no message
 *
 * Revision 1.6  2003/06/10 07:52:16  poth
 * no message
 *
 * Revision 1.5  2003/05/30 07:28:20  poth
 * no message
 *
 * Revision 1.4  2003/05/05 15:52:50  poth
 * no message
 *
 * Revision 1.3  2003/04/10 07:32:35  poth
 * no message
 *
 * Revision 1.2  2003/04/07 07:26:54  poth
 * no message
 *
 * Revision 1.1.1.1  2002/09/25 16:01:26  poth
 * no message
 *
 * Revision 1.7  2002/08/15 10:01:40  ap
 * no message
 *
 * Revision 1.6  2002/08/09 15:36:30  ap
 * no message
 *
 * Revision 1.5  2002/05/14 14:39:51  ap
 * no message
 *
 * Revision 1.4  2002/05/13 16:11:02  ap
 * no message
 *
 * Revision 1.3  2002/04/26 09:05:36  ap
 * no message
 *
 * Revision 1.1  2002/04/04 16:17:15  ap
 * no message
 *
 */
