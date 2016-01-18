/**
 * UpdateWFSLayerPlugIn.java
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

package de.latlon.deejump.wfs.plugin;

import static com.vividsolutions.jump.workbench.model.FeatureEventType.ADDED;
import static com.vividsolutions.jump.workbench.model.FeatureEventType.ATTRIBUTES_MODIFIED;
import static com.vividsolutions.jump.workbench.model.FeatureEventType.DELETED;
import static com.vividsolutions.jump.workbench.model.FeatureEventType.GEOMETRY_MODIFIED;
import static de.latlon.deejump.wfs.transaction.TransactionFactory.setCrs;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.deegreewfs.datatypes.QualifiedName;
import org.deegreewfs.framework.util.CharsetUtils;
import org.deegreewfs.framework.xml.DOMPrinter;
import org.deegreewfs.framework.xml.XMLException;
import org.deegreewfs.framework.xml.XMLFragment;
import org.deegreewfs.framework.xml.XMLTools;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.FeatureEventType;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEventType;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.HTMLFrame;
import com.vividsolutions.jump.workbench.ui.WorkbenchToolBar;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;

import de.latlon.deejump.wfs.i18n.I18N;
import de.latlon.deejump.wfs.jump.WFSLayer;
import de.latlon.deejump.wfs.transaction.TransactionFactory;

/**
 * Plug-in to update a wfs layer
 * 
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * 
 */
public class UpdateWFSLayerPlugIn extends ThreadedBasePlugIn {

    private static Logger LOG = Logger.getLogger( UpdateWFSLayerPlugIn.class );

    private List<String> requests = new LinkedList<String>();

    private WFSLayer layer;

    @Override
    public void initialize( PlugInContext context )
                            throws Exception {

        WorkbenchContext wbcontext = context.getWorkbenchContext();

        WorkbenchToolBar toolbar = wbcontext.getWorkbench().getFrame().getToolBar();
        toolbar.addPlugIn( getIcon(), this, createEnableCheck( wbcontext ), wbcontext );
    }

    @Override
    public boolean execute( PlugInContext context )
                            throws Exception {

        Layer candidatelayer = context.getSelectedLayer( 0 );

        if ( candidatelayer instanceof WFSLayer ) {
            layer = (WFSLayer) candidatelayer;
        } else {
            return false;
        }

        // flush: attempt to force events to be processed
        // LayerEventType.METADATA_CHANGED is not taken into account by
        // listener...
        context.getLayerManager().fireLayerChanged( layer, LayerEventType.METADATA_CHANGED );

        setCrs( layer.getCrs() );

        // get lists and maps with changed, deleted and/or inserted features
        HashMap<FeatureEventType, ArrayList<Feature>> changedFeaturesMap = layer.getLayerListener().getChangedFeaturesMap();

        ArrayList<Feature> updateGeomFeatures = changedFeaturesMap.get( GEOMETRY_MODIFIED );
        ArrayList<Feature> updateAttrFeatures = changedFeaturesMap.get( ATTRIBUTES_MODIFIED );
        HashMap<Feature, Feature> oldGeomFeatures = layer.getLayerListener().getOldGeomFeaturesMap();
        HashMap<Feature, Feature> oldAttrFeatures = layer.getLayerListener().getOldAttrFeaturesMap();
        ArrayList<Feature> delFeatures = changedFeaturesMap.get( DELETED );
        ArrayList<Feature> newFeatures = changedFeaturesMap.get( ADDED );

        QualifiedName geoPropName = layer.getGeoPropertyName();

        StringBuffer updateGeom = null, updateAttr = null;

        // UPDATE Geom
        if ( updateGeomFeatures.size() > 0 ) {
            updateGeom = TransactionFactory.createUpdateTransaction( GEOMETRY_MODIFIED, layer.getQualifiedName(),
                                                                     updateGeomFeatures, oldGeomFeatures );
        }

        // UPDATE Attr
        if ( updateAttrFeatures.size() > 0 ) {
            updateAttr = TransactionFactory.createUpdateTransaction( ATTRIBUTES_MODIFIED, layer.getQualifiedName(),
                                                                     updateAttrFeatures, oldAttrFeatures );
        }

        if ( updateAttrFeatures.size() > 0 || updateGeomFeatures.size() > 0 ) {
            // now CONCAT updates into one request
            requests.add( TransactionFactory.createCommonUpdateTransaction( context.getWorkbenchContext(),
                                                                            layer.getQualifiedName(), updateGeom,
                                                                            updateAttr ).toString() );
        }

        // DELETE
        if ( delFeatures.size() > 0 ) {
            requests.add( TransactionFactory.createTransaction( context.getWorkbenchContext(), DELETED,
                                                                layer.getQualifiedName(), null, delFeatures, false ).toString() );
        }

        // INSERT
        if ( newFeatures.size() > 0 ) {
            requests.add( TransactionFactory.createTransaction( context.getWorkbenchContext(), ADDED,
                                                                layer.getQualifiedName(), geoPropName, newFeatures,
                                                                false ).toString() );
        }

        return true;

    }

    private static void showLongMsg( Component parent, String msg, String title, int type ) {
        JTextArea area = new JTextArea( msg );
        area.setLineWrap( true );
        area.setEditable( false );
        area.setWrapStyleWord( true );
        area.setColumns( 40 );
        area.setRows( 5 );
        area.setBackground( Color.LIGHT_GRAY );
        showMessageDialog( parent, new JScrollPane( area ), title, type );
    }

    public void run( TaskMonitor monitor, PlugInContext context )
                            throws Exception {

        monitor.report( "UpdateWFSLayerPlugIn.message" );

        boolean error = false;

        while ( !requests.isEmpty() ) {
            if ( error ) {
                break;
            }

            String request = requests.remove( 0 );

            try {

                XMLFragment doc = doTransaction( request, layer.getServerURL() );

                if ( doc == null ) {
                    error = true;
                    continue;
                }

                String root = doc.getRootElement().getLocalName();
                String msg;

                if ( root.equals( "ServiceExceptionReport" ) || root.equals( "ExceptionReport" ) ) {
                    msg = XMLTools.getNodeAsString( doc.getRootElement(), "ServiceException", null, null );
                    if ( msg == null ) {
                        msg = XMLTools.getNodeAsString( doc.getRootElement(), "Exception", null, null );
                    }

                    showLongMsg( context.getWorkbenchFrame(), "The Server responded with an error: " + msg, "Error",
                                 ERROR_MESSAGE );

                    LOG.debug( "Complete answer from server: " + doc.getAsPrettyString() );
                    return;
                }

                showOutput( context, new StringBuffer( doc.getAsPrettyString() ) );

                // clean up listener and mark layer as saved
                layer.getLayerListener().reset();
                layer.setFeatureCollectionModified( false );

                LOG.debug( "Removing and re-adding layer." );
                layer.fireLayerChanged( LayerEventType.REMOVED );
                layer.fireLayerChanged( LayerEventType.ADDED );

            } catch ( HttpException e ) {
                showMessageDialog( null, "An HTTP error occurred:\n" + e.getLocalizedMessage(), "Error", ERROR_MESSAGE );
                error = true;
            } catch ( XMLException e ) {
                showMessageDialog( null, "The server did not answer with XML. An XML error occurred:\n"
                                         + e.getLocalizedMessage(), "Error", ERROR_MESSAGE );
                error = true;
            } catch ( SAXException e ) {
                showMessageDialog( null, "The server did not answer with XML. An XML error occurred:\n"
                                         + e.getLocalizedMessage(), "Error", ERROR_MESSAGE );
                error = true;
            } catch ( IOException e ) {
                showMessageDialog( null, "An IO error occurred:\n" + e.getLocalizedMessage(), "Error", ERROR_MESSAGE );
                error = true;
            }
        }
    }

    /**
     * @param xmlRequest
     * @param wfsUrl
     * @return an XML fragment containing the response
     * @throws HttpException
     * @throws IOException
     * @throws XMLException
     * @throws SAXException
     */
    public static XMLFragment doTransaction( String xmlRequest, String wfsUrl )
                            throws HttpException, IOException, XMLException, SAXException {
        XMLFragment result = new XMLFragment();

        if ( LOG.isDebugEnabled() ) {
            try {
                XMLFragment d = new XMLFragment( new StringReader( xmlRequest ), "http://www.debug.org" );
                LOG.debug( "WFS-T request to " + wfsUrl + ":\n" + d.getAsPrettyString() + "\n" );
            } catch ( Exception e ) {
                LOG.debug( "Oh oh, generated String request was not XML:\n" + xmlRequest );
            }
        }

        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod( wfsUrl );
        post.setRequestEntity( new StringRequestEntity( xmlRequest ) );

        int code = client.executeMethod( post );
        if ( code == 200 ) {
            result.load( post.getResponseBodyAsStream(), "http://www.systemid.org" );
        } else {
            showMessageDialog( null, I18N.getString( "UpdateWFSLayerPlugIn.answernotxml",
                                                     post.getResponseBodyAsString() ), I18N.getString( "error" ),
                               ERROR_MESSAGE );
            return null;
        }

        if ( LOG.isDebugEnabled() ) {
            LOG.debug( "WFS-T result:\n" + result.getAsPrettyString() );
        }

        return result;
    }

    /**
     * @param context
     * @param mesg
     * @throws IOException
     * @throws SAXException
     */
    public static void showOutput( PlugInContext context, StringBuffer mesg )
                            throws IOException, SAXException {
        HTMLFrame out = context.getOutputFrame();
        out.createNewDocument();
        out.addHeader( 2, "WFS Transaction" );

        Document doc = XMLTools.parse( new StringReader( mesg.toString() ) );

        URL url = UpdateWFSLayerPlugIn.class.getResource( "response2html.xsl" );

        String s = DOMPrinter.nodeToString( doc, CharsetUtils.getSystemCharset() );

        s = doXSLTransform( url, s );
        out.append( s );
    }

    @Override
    public String getName() {
        return "Update WFSLayer";
    }

    /**
     * @return an Icon object
     */
    public static ImageIcon getIcon() {
        return IconLoader.icon( "Data.gif" );
    }

    private static String doXSLTransform( URL xsltUrl, String content ) {

        StringWriter sw = new StringWriter();

        if ( xsltUrl == null ) {
            LOG.warn( "Could not transform output due to missing XSLT url!" );
            return "";
        }

        try {
            Source source = new StreamSource( new StringReader( content ) );
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer( new StreamSource( xsltUrl.openStream() ) );
            transformer.transform( source, new StreamResult( sw ) );

        } catch ( MalformedURLException e1 ) {
            e1.printStackTrace();

        } catch ( TransformerException e1 ) {
            e1.printStackTrace();

        } catch ( Exception e1 ) {
            e1.printStackTrace();
        } catch ( ExceptionInInitializerError ee ) {
            ee.printStackTrace();
        }

        return sw.toString();
    }

    private static EnableCheck createEnableCheck( final WorkbenchContext workbenchContext ) {
        MultiEnableCheck mec = new MultiEnableCheck();
        mec.add( createExactlyNWfsLayersMustBeSelectedCheck( workbenchContext, 1 ) );
        mec.add( createFeatureMustHaveChangedCheck( workbenchContext ) );

        return mec;
    }

    private static EnableCheck createExactlyNWfsLayersMustBeSelectedCheck( final WorkbenchContext workbenchContext,
                                                                           final int n ) {
        return new EnableCheck() {
            public String check( JComponent component ) {
                return (

                workbenchContext.getLayerNamePanel() == null

                ||

                n != ( workbenchContext.getLayerNamePanel() ).selectedNodes( WFSLayer.class ).size() )

                ? ( "Exactly " + n + " layer" + StringUtil.s( n ) + " must be selected" ) : null;
            }
        };
    }

    private static EnableCheck createFeatureMustHaveChangedCheck( final WorkbenchContext workbenchContext ) {
        return new EnableCheck() {
            public String check( JComponent component ) {

                return ( workbenchContext.getLayerNamePanel().selectedNodes( WFSLayer.class ).size() != 1

                ||

                !( (Layer) workbenchContext.getLayerNamePanel().selectedNodes( WFSLayer.class ).iterator().next() ).isFeatureCollectionModified()

                ) ? "FeatureMustHaveChangedCheck" : null;
            }
        };
    }

}
