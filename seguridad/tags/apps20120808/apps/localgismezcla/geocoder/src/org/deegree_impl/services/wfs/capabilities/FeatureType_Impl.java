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

package org.deegree_impl.services.wfs.capabilities;

import java.util.*;
import java.net.*;

import org.deegree.services.capabilities.*;
import org.deegree.services.wfs.capabilities.*;

import org.deegree.model.geometry.*;


/**
 * The main purpose of the &lt;FeatureTypeList&gt; section is to define the
 * list of feature types that a WFS can service and define the operations
 * that are supported on each feature type. For possible operations see the
 * Operations interface.
 *
 * <p>-----------------------------------------------------</p>
 *
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */

class FeatureType_Impl implements FeatureType{
    
    private String name = null;
    private String title = null;
    private String abstract_ = null;
    private String srs = null;
    private GM_Envelope latLonBoundingBox = null;
    private FeatureTypeList parentList = null;
    private String responsibleClassName = null;
    private URL configURL = null;
    
    private ArrayList keyword = null;
    private ArrayList operation = null;
    private ArrayList metadataURL = null;
    
    
    /**
     * constructor initializing the class with the <FeatureType>
     */
    FeatureType_Impl(String name,
                    String title,
                    String abstract_,
                    String srs,
                    GM_Envelope latLonBoundingBox,
                    FeatureTypeList parentList,
                    String[] keyword,
                    Operation[] operation,
                    MetadataURL[] metadataURL,
                    String responsibleClassName,
                    URL configURL) {
        this.keyword = new ArrayList();
        this.metadataURL = new ArrayList();
        this.operation = new ArrayList();
        setName(name);
        setTitle(title);
        setAbstract(abstract_);
        setSrs(srs);
        setLatLonBoundingBox(latLonBoundingBox);
        setParentList(parentList);
        setResponsibleClassName( responsibleClassName );
        setConfigURL( configURL );
        setKeyword(keyword);
        setOperations(operation);
        setMetadataURL(metadataURL);
    }
    
    /**
     * The name of the feature type. This element is mandatory.
     */
    public String getName() {
        return name;
    }
    
    /**
     * sets the name of the feature type.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * The &lt;Title&gt; is a human-readable title to briefly identify
     * this feature type in menus.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * sets the title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * The &lt;Abstract&gt; is a descriptive narrative for more
     * information about the feature type.
     */
    public String getAbstract() {
        return abstract_;
    }
    
    /**
     * sets the abstract
     */
    public void setAbstract(String abstract_) {
        this.abstract_ = abstract_;
    }
    
    /**
     * The &lt;Keyword&gt; element delimits short words to aid
     * catalog searching.
     */
    public String[] getKeywords() {
        return (String[])keyword.toArray( new String[keyword.size()] );
    }
    
    /**
     * adds the keyword
     */
    public void addKeyword(String keyword) {
        this.keyword.add( keyword );
    }
    
    /**
     * sets the keyword
     */
    public void setKeyword(String[] keyword) {
        this.keyword.clear();
        if ( keyword != null ) {
            for (int i = 0; i < keyword.length; i++) {
                this.keyword.add( keyword[i] );
            }
        }
    }
    
    /**
     * The &lt;SRS&gt; element is used to indicate which spatial
     * reference system should be used to express the state of a
     * feature.
     */
    public String getSrs() {
        return srs;
    }
    
    /**
     * sets the srs
     */
    public void setSrs(String srs) {
        this.srs = srs;
    }
    
    /**
     * The &lt;Operations&gt; element defines which are operations
     * are supported on a feature type. Any locally defined
     * operations take precedence over any globally defined
     * operations.
     */
    public Operation[] getOperations() {
        return (Operation[])operation.toArray( new Operation[operation.size()] );
    }
    
    /**
     * Adds an operation (only if it has not been defined yet).
     */
    public void addOperation(Operation operation) {
    	if (!(this.operation.contains (operation))) {
        	this.operation.add( operation );
    	}
    }
    
    /**
     * sets the operation
     */
    public void setOperations(Operation[] operation) {
        this.operation.clear();
        if ( operation != null ) {
            for (int i = 0; i < operation.length; i++) {
                addOperation( operation[i] );
            }
        }
    }
    
    /**
     * The LatLonBoundingBox attributes indicate the edges
     * of the enclosing rectangle in latitude/longitude decimal
     * degrees (as in SRS EPSG:4326 [WGS1984 lat/lon]). Its
     * purpose is to facilitate geographic searches without
     * requiring coordinate transformations by the search
     * engine.
     */
    public GM_Envelope getLatLonBoundingBox() {
        return latLonBoundingBox;
    }
    
    /**
     * sets the LatLonBoundingBox
     */
    public void setLatLonBoundingBox(GM_Envelope latLonBoundingBox) {
        this.latLonBoundingBox = latLonBoundingBox;
    }
    
    /**
     * A WFS may use zero or more &lt;MetadataURL&gt;
     * elements to offer detailed, standardized metadata about
     * the data inaparticularfeature type.The type attribute
     * indicates the standard to which the metadata complies;
     * the format attribute indicates how the metadata is
     * structured. Two types are defined at present: ’TC211’ =
     * ISO TC211 19115; FGDC = FGDC CSDGM.
     */
    public MetadataURL[] getMetadataURL() {
        return (MetadataURL[])metadataURL.toArray(new MetadataURL[metadataURL.size()]);
    }
    
    /**
     * adds the metadataURL
     */
    public void addMetadataURL(MetadataURL metadataURL) {
        this.metadataURL.add(metadataURL);
    }
    
    /**
     * sets the metadataURL
     */
    public void setMetadataURL(MetadataURL[] metadataURL) {
        this.metadataURL.clear();
        if ( metadataURL != null ) {
            for (int i = 0; i < metadataURL.length; i++){
                this.metadataURL.add(metadataURL[i]);
            }
        }
    }
    
    /**
     * returns an instance of the feature type list the feature
     * belongs to.
     */
    public FeatureTypeList getParentList() {
        return parentList;
    }
    
    /**
     * sets an instance of the feature type list the feature
     * belongs to.
     */
    public void setParentList(FeatureTypeList parentList) {
        this.parentList = parentList;
    }
    
    /**
     * returns the name of the class that's responsible for performing
     * the operation.
     */
    public String getResponsibleClassName() {
        return responsibleClassName;
    }
    
    /**
     * sets the name of the responsible class for handling this
     * operation.
     */
    public void setResponsibleClassName(String responsibleClassName) {
        this.responsibleClassName = responsibleClassName;
    }
    
    /**
     * sets the name of the configuration file of the responsible class
     */
    public void setConfigURL(URL configURL) {
        this.configURL = configURL;
    }
    
    /**
     * returns the name of the configuration file of the responsible class
     */
    public URL getConfigURL() {
        return configURL;
    }
    
    public String toString() {
        String ret = null;
        ret = "name = " + name + "\n";
        ret += "title = " + title + "\n";
        ret += "abstract_ = " + abstract_ + "\n";
        ret += "srs = " + srs + "\n";
        ret += "latLonBoundingBox = " + latLonBoundingBox + "\n";
        ret += "keyword = " + keyword + "\n";
        ret += "operation = " + operation + "\n";
        ret += "metadataURL = " + metadataURL + "\n";
        return ret;
    }
    
    /*#FeatureTypeList lnkWFS_FeatureTypeList_Cap;*/
}

/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: FeatureType_Impl.java,v $
 * Revision 1.1  2009/07/09 07:25:33  miriamperez
 * Rama única LocalGISDOS
 *
 * Revision 1.1  2009/04/13 10:16:51  miriamperez
 * Meto en LocalGISPrivado el módulo del geocoder al completo
 *
 * Revision 1.1  2006/08/31 11:15:30  angeles
 * *** empty log message ***
 *
 * Revision 1.1.1.1  2006/08/17 11:34:01  angeles
 * no message
 *
 * Revision 1.3  2004/01/12 10:21:57  mrsnyder
 * Operations that are added to a FeatureType are now checked for existence first.
 *
 * Revision 1.2  2002/11/06 17:08:56  poth
 * no message
 *
 * Revision 1.1.1.1  2002/09/25 16:01:22  poth
 * no message
 *
 * Revision 1.13  2002/08/19 15:58:51  ap
 * no message
 *
 * Revision 1.12  2002/08/15 10:01:40  ap
 * no message
 *
 * Revision 1.11  2002/08/09 15:36:30  ap
 * no message
 *
 * Revision 1.10  2002/07/04 14:55:07  ap
 * no message
 *
 * Revision 1.9  2002/05/22 16:05:02  ap
 * no message
 *
 * Revision 1.8  2002/05/16 15:52:13  ap
 * no message
 *
 * Revision 1.7  2002/05/13 16:10:47  ap
 * no message
 *
 * Revision 1.6  2002/05/06 16:02:07  ap
 * no message
 *
 * Revision 1.5  2002/04/26 09:05:10  ap
 * no message
 *
 * Revision 1.3  2002/04/25 16:18:47  ap
 * no message
 *
 * Revision 1.1  2002/04/04 16:17:15  ap
 * no message
 *
 */
