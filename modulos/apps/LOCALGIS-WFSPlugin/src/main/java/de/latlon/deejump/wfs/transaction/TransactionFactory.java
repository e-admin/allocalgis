/**
 * TransactionFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*----------------    FILE HEADER  ------------------------------------------

 Copyright (C) 2001-2005 by:
 lat/lon GmbH
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
 lat/lon GmbH
 Aennchenstra√üe 19
 53177 Bonn
 Germany


 ---------------------------------------------------------------------------*/

package de.latlon.deejump.wfs.transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.deegreewfs.datatypes.QualifiedName;
import org.deegreewfs.model.crs.CRSFactory;
import org.deegreewfs.model.crs.CoordinateSystem;
import org.deegreewfs.model.filterencoding.AbstractOperation;
import org.deegreewfs.model.filterencoding.Literal;
import org.deegreewfs.model.filterencoding.PropertyIsCOMPOperation;
import org.deegreewfs.model.filterencoding.PropertyName;
import org.deegreewfs.model.spatialschema.GMLGeometryAdapter;
import org.deegreewfs.model.spatialschema.GeometryImpl;
import org.deegreewfs.model.spatialschema.JTSAdapter;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.FeatureEventType;

import de.latlon.deejump.wfs.auth.UserData;

/**
 * Factory class to generate WFS transaction requests.
 * 
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * 
 */
public class TransactionFactory {

    private static Logger LOG = Logger.getLogger( TransactionFactory.class );

    /** the srs to be used in requests containing gml */
    private static String crs = "-1";

    /** common transaction header */
    private static final String REQUEST_HEADER = "<?xml version='1.0' encoding='ISO-8859-1'?>"
                                                 + "<wfs:Transaction version='1.1.0' service='WFS' "
                                                 + "xmlns:gml='http://www.opengis.net/gml' "
                                                 + "xmlns:ogc='http://www.opengis.net/ogc' xmlns:wfs='http://www.opengis.net/wfs' "
                                                 + "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                                                 + "xsi:schemaLocation='http://www.opengis.net/wfs/1.1.0/WFS-transaction.xsd' ";

    private static final SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd'T'hh:mm:ss" );

    /**
     * Combines geometry update xml with attribute update xml into a common transaction update xml
     * 
     * @param context
     * 
     * @param featureType
     * 
     * @param geomFragment
     *            the update fragment containing features with changed geometries
     * @param attrFragment
     *            the update fragment containing features with changed attributes
     * @return an xml containing a full xml update request
     */
    public static final StringBuffer createCommonUpdateTransaction( WorkbenchContext context,
                                                                    QualifiedName featureType,
                                                                    StringBuffer geomFragment, StringBuffer attrFragment ) {

        StringBuffer sb = new StringBuffer();

        if ( geomFragment == null && attrFragment == null ) {
            return sb;
        }

        sb.append( REQUEST_HEADER );

        UserData logins = (UserData) context.getBlackboard().get( "LOGINS" );
        if ( logins != null && logins.getUsername() != null && logins.getPassword() != null ) {
            sb.append( "\nuser=\"" ).append( logins.getUsername() );
            sb.append( "\"\npassword=\"" ).append( logins.getPassword() ).append( "\"\n" );
        }

        sb.append( "xmlns:" ).append( featureType.getPrefix() ).append( "='" ).append( featureType.getNamespace() ).append(
                                                                                                                            "'  >" );

        if ( geomFragment != null ) {
            sb.append( geomFragment );
        }
        if ( attrFragment != null ) {
            sb.append( attrFragment );
        }

        sb.append( "</wfs:Transaction>" );
        return sb;
    }

    /**
     * Generates an update transcation request.
     * 
     * @param fet
     *            the feature event type (FeatureEventType.GEOMETRY_MODIFIED or
     *            FeatureEventType.ATTRIBUTE_MODIFIED)
     * @param featureType
     *            the name of the WFS feature type
     * @param newFeatures
     *            list containing features to be updated
     * @param oldFeatures
     *            list containing original features (those are used as filter)
     * @return an XML fragment containing an update transaction
     */
    public static final StringBuffer createUpdateTransaction( FeatureEventType fet, QualifiedName featureType,
                                                              ArrayList<Feature> newFeatures,
                                                              HashMap<Feature, Feature> oldFeatures ) {
        StringBuffer sb = new StringBuffer();
        if ( featureType == null ) {
            return sb;
        }
        if ( newFeatures == null || newFeatures.size() < 1 ) {
            return sb;
        }

        appendUpdate( fet, sb, featureType, newFeatures, oldFeatures );

        return sb;
    }

    /**
     * @param context
     * @param transacType
     * @param featureType
     * @param geoPropName
     * @param newFeatures
     * @param useExisting
     * @return a StringBuffer representation of the transaction
     */
    public static final StringBuffer createTransaction( WorkbenchContext context, FeatureEventType transacType,
                                                        QualifiedName featureType, QualifiedName geoPropName,
                                                        ArrayList<Feature> newFeatures, boolean useExisting ) {

        StringBuffer sb = new StringBuffer();
        if ( featureType == null ) {
            return sb;
        }
        if ( newFeatures == null || newFeatures.size() < 1 ) {
            return sb;
        }

        sb.append( REQUEST_HEADER );

        UserData logins = (UserData) context.getBlackboard().get( "LOGINS" );
        if ( logins != null && logins.getUsername() != null && logins.getPassword() != null ) {
            sb.append( "\nuser=\"" ).append( logins.getUsername() );
            sb.append( "\"\npassword=\"" ).append( logins.getPassword() ).append( "\"\n" );
        }

        sb.append( "xmlns:" ).append( featureType.getPrefix() ).append( "='" ).append( featureType.getNamespace() ).append(
                                                                                                                            "'  >" );

        if ( transacType.equals( FeatureEventType.ADDED ) ) {
            appendInsert( sb, featureType, geoPropName, newFeatures, useExisting );

        } else if ( transacType.equals( FeatureEventType.DELETED ) ) {
            appendDelete( sb, featureType, newFeatures );
        }

        sb.append( "</wfs:Transaction>" );
        return sb;
    }

    /**
     * creates and append an update request to a string buffer
     * 
     * @param fet
     *            the feature event type
     * @param sb
     *            the StringBuffer to append to
     * @param featureType
     *            the feature type name
     * @param features
     *            the list of features
     * @param oldFeatures
     *            a ap containing the old features
     */
    private static final void appendUpdate( FeatureEventType fet, StringBuffer sb, QualifiedName featureType,
                                            ArrayList<Feature> features, HashMap<Feature, Feature> oldFeatures ) {

        for ( Iterator<Feature> iter = features.iterator(); iter.hasNext(); ) {

            Feature feat = iter.next();
            sb.append( "<wfs:Update typeName='" ).append( featureType.getPrefix() ).append( ":" ).append(
                                                                                                          featureType.getLocalName() ).append(
                                                                                                                                               "'>" );
            sb.append( createPropertiesFragment( featureType, fet, feat ) );

            Feature oldFeat = oldFeatures.get( feat );
            StringBuffer s = createOperationFragment( oldFeat, featureType );
            if ( s.length() > 0 ) {
                sb.append( "<ogc:Filter>" );
                sb.append( s );
                sb.append( "</ogc:Filter>" );
            }

            sb.append( "</wfs:Update>" );
        }
    }

    /**
     * Appends an insert transaction to a string buffer
     * 
     * @param sb
     *            the strign buffer to append to
     * @param featureType
     *            the feature type name
     * @param features
     *            the list of new features
     */
    private static final void appendInsert( StringBuffer sb, QualifiedName featureType, QualifiedName geoPropName,
                                            ArrayList<Feature> features, boolean useExisting ) {

        sb.append( "<wfs:Insert handle='insert1' idgen='" + ( useExisting ? "UseExisting" : "GenerateNew" ) + "' >" );

        for ( Iterator<Feature> iter = features.iterator(); iter.hasNext(); ) {
            Feature feat = iter.next();
            String s = featureType.getPrefix() + ":" + featureType.getLocalName();

            sb.append( "<" ).append( s );
            if ( useExisting ) {
                String id = (String) feat.getAttribute( "Internal ID" );
                if ( !id.startsWith( featureType.getLocalName().toUpperCase() ) ) {
                    id = featureType.getLocalName().toUpperCase() + "_" + id;
                    feat.setAttribute( "Internal ID", id );
                }
                sb.append( " gml:id=\"" ).append( id ).append( "\"" );
            }
            sb.append( ">" );
            sb.append( createInsertPropertiesFragment( geoPropName, featureType, feat ) );
            sb.append( "</" ).append( s ).append( ">" );
        }

        sb.append( "</wfs:Insert>" );
    }

    /**
     * Appends a delete transactio to an existing string buffer
     * 
     * @param sb
     *            the string buffer to append to
     * @param featureType
     *            the feature type name
     * @param features
     *            the list of new features to be deleted
     */
    private static final void appendDelete( StringBuffer sb, QualifiedName featureType, ArrayList<Feature> features ) {
        for ( Iterator<Feature> iter = features.iterator(); iter.hasNext(); ) {
            sb.append( "<wfs:Delete typeName='" ).append( featureType.getPrefix() ).append( ":" ).append(
                                                                                                          featureType.getLocalName() ).append(
                                                                                                                                               "'>" );

            Feature feat = iter.next();
            StringBuffer s = createOperationFragment( feat, featureType );
            if ( s.length() > 0 ) {
                sb.append( "<ogc:Filter>" );
                sb.append( s );
                sb.append( "</ogc:Filter>" );
            }

            sb.append( "</wfs:Delete>" );
        }
    }

    /**
     * Creates a StringBuffer containing the gml representation of a geometry
     * 
     * @return gml representing the input geometry
     * @param geometry
     *            the geometry
     */
    public static final StringBuffer createGeometryGML( Geometry geometry ) {
        org.deegreewfs.model.spatialschema.Geometry gg = null;
        try {
            LOG.debug( "Using crs " + crs );
            CoordinateSystem cs = CRSFactory.create( crs );
            gg = JTSAdapter.wrap( geometry );
            ( (GeometryImpl) gg ).setCoordinateSystem( cs );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        StringBuffer sb = null;
        try {
            sb = GMLGeometryAdapter.export( gg );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return sb;
    }

    /**
     * @return Returns the srs.
     */
    public static String getCrs() {
        return crs;
    }

    /**
     * @param crs
     *            The srs to set.
     */
    public static void setCrs( String crs ) {
        TransactionFactory.crs = crs;
    }

    private static final StringBuffer createPropertiesFragment( QualifiedName featureType, FeatureEventType fet,
                                                                Feature bf ) {

        StringBuffer sb = new StringBuffer();
        Object[] os = bf.getAttributes();
        FeatureSchema fs = bf.getSchema();

        for ( int j = 0; j < os.length; j++ ) {

            String attName = fs.getAttributeName( j );

            LOG.debug( "Shall we insert attribute " + attName + "?" );

            if ( ( ( !( fs.getAttributeType( j ) == AttributeType.GEOMETRY ) ) && fet == FeatureEventType.ATTRIBUTES_MODIFIED ) ) {
                if ( fs.getAttributeName( j ).equals( "Internal ID" ) ) {
                    LOG.debug( "Skipping fake id attribute." );
                    continue;
                }

                LOG.debug( "Inserting modified attribute." );

                if ( fs.getAttributeType( j ) == AttributeType.DATE ) {
                    Date attValue = (Date) bf.getAttribute( j );
                    if ( attValue != null ) {
                        String val = formatter.format( attValue );
                        LOG.debug( "Inserting date value of " + val );
                        sb.append( "<wfs:Property><wfs:Name>" ).append( featureType.getPrefix() ).append( ":" );
                        sb.append( attName ).append( "</wfs:Name>" ).append( "<wfs:Value>" ).append( val );
                        sb.append( "</wfs:Value></wfs:Property>" );
                    }
                } else {
                    Object attValue = bf.getAttribute( j );
                    if ( attValue != null ) {
                        sb.append( "<wfs:Property><wfs:Name>" ).append( featureType.getPrefix() ).append( ":" );
                        sb.append( attName ).append( "</wfs:Name>" ).append( "<wfs:Value>" ).append( attValue );
                        sb.append( "</wfs:Value></wfs:Property>" );
                    }
                }
            } else if ( ( fs.getAttributeType( j ) == AttributeType.GEOMETRY )
                        && fet == FeatureEventType.GEOMETRY_MODIFIED ) {
                LOG.debug( "Inserting modified geometry." );
                if ( fs.getAttributeName( j ).equals( "FAKE_GEOMETRY" ) ) {
                    LOG.debug( "Skipping fake geometry." );
                    continue;
                }
                sb.append( "<wfs:Property><wfs:Name>" );
                sb.append( featureType.getPrefix() );
                sb.append( ":" ).append( attName );
                sb.append( "</wfs:Name><wfs:Value>" );
                sb.append( createGeometryGML( bf.getGeometry() ) );
                sb.append( "</wfs:Value></wfs:Property>" );
            }
        }
        return sb;
    }

    private static final StringBuffer createInsertPropertiesFragment( QualifiedName geoAttName,
                                                                      QualifiedName featureType, Feature bf ) {

        LOG.debug( "Ok, creating insert properties." );

        StringBuffer sb = new StringBuffer();
        Object[] attributes = bf.getAttributes();
        FeatureSchema featSchema = bf.getSchema();

        for ( int j = 0; j < attributes.length; j++ ) {

            String attName = featSchema.getAttributeName( j );

            LOG.debug( "Pondering about property with name " + attName );

            if ( !( featSchema.getAttributeType( j ) == AttributeType.GEOMETRY ) ) {
                LOG.debug( "Not a geometry." );

                if ( featSchema.getAttributeType( j ) == AttributeType.DATE ) {
                    Date attValue = (Date) bf.getAttribute( j );
                    if ( attValue != null ) {
                        String val = formatter.format( attValue );
                        sb.append( "<" ).append( featureType.getPrefix() ).append( ":" ).append( attName ).append( ">" );
                        sb.append( val );
                        sb.append( "</" ).append( featureType.getPrefix() ).append( ":" ).append( attName ).append( ">" );
                    }
                } else {
                    if ( featSchema.getAttributeName( j ).equals( "Internal ID" ) ) {
                        LOG.debug( "Skipping fake id attribute." );
                        continue;
                    }

                    Object attValue = bf.getAttribute( j );
                    if ( attValue != null ) {
                        sb.append( "<" ).append( featureType.getPrefix() ).append( ":" ).append( attName ).append( ">" );
                        sb.append( attValue );
                        sb.append( "</" ).append( featureType.getPrefix() ).append( ":" ).append( attName ).append( ">" );
                    }
                }
            } else {
                LOG.debug( "It's a geometry." );
                LOG.debug( attName.equals( "GEOMETRY" ) ? "Schema not loaded? Using strange mechanisms here!"
                                                       : "Ok, using schema." );
                if ( featSchema.getAttributeName( j ).equals( "FAKE_GEOMETRY" ) ) {
                    LOG.debug( "Skipping fake geometry." );
                    continue;
                }
                sb.append( "<" ).append( featureType.getPrefix() ).append( ":" );
                sb.append( attName.equals( "GEOMETRY" ) ? geoAttName.getLocalName() : attName ).append( ">" );
                sb.append( createGeometryGML( bf.getGeometry() ) );
                sb.append( "</" ).append( featureType.getPrefix() ).append( ":" );
                sb.append( attName.equals( "GEOMETRY" ) ? geoAttName.getLocalName() : attName ).append( ">" );
            }
        }
        return sb;
    }

    private static final StringBuffer createOperationFragment( Feature bf, QualifiedName featureType ) {
        if ( bf.getSchema().hasAttribute( "Internal ID" ) ) {
            StringBuffer sb = new StringBuffer( 512 );
            sb.append( "<ogc:GmlObjectId gml:id='" + bf.getAttribute( "Internal ID" ) + "' />" );
            LOG.debug( "Using GML id as filter." );
            return sb;
        }

        StringBuffer sb = new StringBuffer();
        Object[] os = bf.getAttributes();
        FeatureSchema fs = bf.getSchema();
        int featCount = 0;
        for ( int j = 0; j < os.length; j++ ) {

            String attName = fs.getAttributeName( j );

            if ( attName.equals( "Internal ID" ) ) {
                LOG.debug( "Skipping fake id attribute." );
                continue;
            }

            if ( attName.equals( "FAKE_GEOMETRY" ) ) {
                LOG.debug( "Skipping fake geometry." );
                continue;
            }

            if ( !( fs.getAttributeType( j ) == AttributeType.GEOMETRY ) ) {
                if ( fs.getAttributeType( j ) == AttributeType.DATE ) {
                    Date attValue = (Date) bf.getAttribute( j );
                    if ( attValue != null ) {
                        String attNameWoPrefix = attName.substring( attName.indexOf( ":" ) + 1, attName.length() );
                        QualifiedName qn = new QualifiedName( featureType.getPrefix(), attNameWoPrefix,
                                                              featureType.getNamespace() );
                        PropertyIsCOMPOperation oper;
                        oper = new PropertyIsCOMPOperation( 100, new PropertyName( qn ),
                                                            new Literal( formatter.format( attValue ) ) );
                        sb.append( oper.toXML() );
                        featCount++;
                    }
                } else {
                    Object attValue = bf.getAttribute( j );

                    if ( attValue != null ) {

                        double value = 0.1;

                        try {
                            value = Double.parseDouble( attValue.toString() );

                        } catch ( NumberFormatException e ) {
                            value = 0d;
                        }

                        value = value - (int) value;
                        if ( value == 0d ) {
                            String attNameWoPrefix = attName.substring( attName.indexOf( ":" ) + 1, attName.length() );
                            QualifiedName qn = new QualifiedName( featureType.getPrefix(), attNameWoPrefix,
                                                                  featureType.getNamespace() );
                            AbstractOperation oper = new PropertyIsCOMPOperation( 100, new PropertyName( qn ),
                                                                                  new Literal( attValue.toString() ) );
                            sb.append( oper.toXML() );
                            featCount++;
                        }
                    }
                }
            }
        }

        if ( featCount > 1 ) {
            sb.insert( 0, "<ogc:And>" );
            sb.append( "</ogc:And>" );
        }

        return sb;
    }

}
