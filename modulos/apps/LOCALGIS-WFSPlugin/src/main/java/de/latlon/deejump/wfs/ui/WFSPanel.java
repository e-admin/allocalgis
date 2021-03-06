/**
 * WFSPanel.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * (c) 2007 by lat/lon GmbH
 *
 * @author Ugo Taddei (taddei@latlon.de)
 *
 * This program is free software under the GPL (v2.0)
 * Read the file LICENSE.txt coming with the sources for details.
 */
package de.latlon.deejump.wfs.ui;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import org.apache.log4j.Logger;
import org.deegreewfs.datatypes.QualifiedName;
import org.deegreewfs.framework.xml.XMLFragment;
import org.deegreewfs.model.spatialschema.Geometry;
import org.deegreewfs.ogcwebservices.wfs.capabilities.WFSFeatureType;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.workbench.WorkbenchContext;

import de.latlon.deejump.wfs.DeeJUMPException;
import de.latlon.deejump.wfs.auth.LoginDialog;
import de.latlon.deejump.wfs.auth.MD5Hasher;
import de.latlon.deejump.wfs.auth.UserData;
import de.latlon.deejump.wfs.client.AbstractWFSWrapper;
import de.latlon.deejump.wfs.client.WFServiceWrapper_1_0_0;
import de.latlon.deejump.wfs.client.WFServiceWrapper_1_1_0;
import de.latlon.deejump.wfs.i18n.I18N;

/**
 * This is a panel which contains other basic GUIs for accessing Features of a WFS.
 * 
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * @author last edited by: $Author: satec $
 * 
 * @version $Revision: 1.1 $, $Date: 2012/09/12 10:52:04 $
 */
public class WFSPanel extends JPanel {

    private static final long serialVersionUID = 8204179552311582569L;

    protected static final Logger LOG = Logger.getLogger( WFSPanel.class );

    // Constants for spatial search criteria type
    // also used by child panels
    /** Search uses no spatial criteria */
    public static final String NONE = "NONE";

    /** Search uses bounding box as spatial criteria */
    public static final String BBOX = "BBOX";

    /** Search uses a selected (GML) geometry as spatial criteria */
    public static final String SELECTED_GEOM = "SELECTED_GEOM";

    private List<String> servers = new ArrayList<String>();

    private static File lastDirectory;

    /**
     * The standard geometry type name (used when getting schemata and creating filters with spatial clauses
     */
    public static final String GEOMETRY_PROPERTY_NAME = "GEOM";

    /** The panel containing the interface for attribute-based search */
    protected PropertyCriteriaPanel attributeResPanel;

    /** The panel containing the interface for geometry-based search */
    protected SpatialCriteriaPanel spatialResPanel;

    String[] attributeNames = new String[] {};

    protected QualifiedName[] geoProperties;

    private QualifiedName geoProperty;

    AbstractWFSWrapper wfService;

    protected RequestTextArea requestTextArea;

    protected JTextArea responseTextArea;

    protected JComboBox serverCombo;

    // private JButton okButton;

    private JTabbedPane tabs;

    private JComboBox featureTypeCombo;

    // private JPanel mainPanel;

    private Box box;

    // TODO remove dependency on JUMP/JTS use deegree Envelope
    /** The envelope of the current bounding box */
    private Envelope envelope = new Envelope( -1d, -1d, 1d, 1d );

    // private GMLGeometry gmlBbox;
    private Geometry selectedGeom;

    protected String srs = "EPSG:4326";

    protected PropertySelectionPanel propertiesPanel;

    private JButton capabilitiesButton;

    protected String wfsVersion;

    protected WFSOptions options;

    WFSPanelButtons controlButtons;

    private JTextArea xmlPane;

    private UserData logins;

    private WorkbenchContext context;

    /**
     * @param context
     * @param urlList
     *            the list of servers
     */
    public WFSPanel( WorkbenchContext context, List<String> urlList ) {
        this.context = context;
        setWFSList( urlList );
        initGUI();
        this.options = new WFSOptions();
    }

    private void initGUI() {

        LayoutManager lm = new BoxLayout( this, BoxLayout.Y_AXIS );
        setLayout( lm );

        // combo box for WFS URLs
        serverCombo = createServerCombo();
        Dimension d = new Dimension( 400, 45 );
        serverCombo.setPreferredSize( d );
        serverCombo.setMaximumSize( d );
        String txt = I18N.getString( "FeatureResearchDialog.wfsService" );
        serverCombo.setBorder( BorderFactory.createTitledBorder( txt ) );
        txt = I18N.getString( "FeatureResearchDialog.wfsServiceToolTip" );
        serverCombo.setToolTipText( txt );

        add( serverCombo );

        // connect and capabilities button
        JButton connecButton = new JButton( I18N.getString( "FeatureResearchDialog.connect" ) );
        connecButton.setAlignmentX( 0.5f );
        connecButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                try {
                    reinitService( (String) serverCombo.getSelectedItem() );
                } catch ( DeeJUMPException e1 ) {
                    LOG.info( "Service could not be initialized." );
                    LOG.debug( "Stack trace: ", e1 );
                }
            }
        } );

        capabilitiesButton = new JButton( I18N.getString( "FeatureResearchDialog.capabilities" ) );
        capabilitiesButton.setEnabled( false );
        capabilitiesButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                createXMLFrame( WFSPanel.this, wfService.getCapabilitesAsString() );

            }
        } );

        JButton loginButton = new JButton( I18N.getString( "FeatureResearchDialog.login" ) );
        loginButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                performLogin();
            }
        } );

        loginButton.setEnabled( context != null );

        JPanel p = new JPanel();
        p.setLayout( new BoxLayout( p, BoxLayout.Y_AXIS ) );

        // version buttons
        p.add( createVersionButtons( new String[] { "1.0.0", "1.1.0" } ) );

        JPanel innerPanel = new JPanel();
        innerPanel.add( connecButton );
        innerPanel.add( capabilitiesButton );
        innerPanel.add( loginButton );
        p.add( innerPanel );

        featureTypeCombo = createFeatureTypeCombo();
        // featureTypeCombo.setVisible( false );
        featureTypeCombo.setEnabled( false );
        p.add( featureTypeCombo );

        // FIXME what's this???
        setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        add( p );

        final Dimension dim = new Dimension( 400, 570 );
        final Dimension minDim = new Dimension( 400, 500 );

        tabs = new JTabbedPane() {
            private static final long serialVersionUID = 6328063093445991041L;

            @Override
            public Dimension getPreferredSize() {
                return dim;
            }

            @Override
            public Dimension getMinimumSize() {
                return minDim;
            }

            @Override
            public void setEnabled( boolean e ) {
                super.setEnabled( e );
                attributeResPanel.setEnabled( e );
                propertiesPanel.setEnabled( e );
                propertiesPanel.setEnabled( e );
                spatialResPanel.setEnabled( e );
                requestTextArea.setEnabled( e );
                responseTextArea.setEnabled( e );
            }
        };

        attributeResPanel = new PropertyCriteriaPanel( this, featureTypeCombo );
        attributeResPanel.setEnabled( false );
        tabs.add( I18N.getString( "FeatureResearchDialog.attributeSearch" ), attributeResPanel );

        propertiesPanel = new PropertySelectionPanel( this );
        tabs.add( I18N.getString( "FeatureResearchDialog.properties" ), propertiesPanel );

        spatialResPanel = new SpatialCriteriaPanel( this );
        tabs.add( I18N.getString( "FeatureResearchDialog.spatialSearch" ), spatialResPanel );

        requestTextArea = new RequestTextArea( this );

        tabs.add( I18N.getString( "FeatureResearchDialog.request" ), requestTextArea );

        tabs.add( I18N.getString( "FeatureResearchDialog.response" ), createResponseTextArea() );
        tabs.setEnabled( false );

        box = Box.createHorizontalBox();
        box.setBorder( BorderFactory.createEmptyBorder( 20, 5, 10, 5 ) );

        add( tabs );
        tabs.setVisible( false );

        // setMinimumSize( new Dimension( 400, 300 ) );
        // setPreferredSize( new Dimension( 400, 600 ) );
    }

    protected void performLogin() {
        LoginDialog dialog = new LoginDialog( (Dialog) this.getTopLevelAncestor(),
                                              I18N.getString( "FeatureResearchDialog.login" ),
                                              serverCombo.getSelectedItem().toString() );

        String pass = dialog.getPassword();
        String user = dialog.getName();

        if ( user == null || user.equals( "" ) ) {
            JOptionPane.showMessageDialog( this, I18N.getString( "WFSPanel.userEmpty" ) );
            return;
        }

        if ( pass == null || pass.equals( "" ) ) {
            JOptionPane.showMessageDialog( this, I18N.getString( "WFSPanel.passEmpty" ) );
            return;
        }

        logins = new UserData( user, MD5Hasher.getMD5( pass ) );
        // logins = new UserData(user, pass);
        context.getBlackboard().put( "LOGINS", logins );

        try {
            reinitService( (String) serverCombo.getSelectedItem() );
        } catch ( DeeJUMPException ex ) {
            String msg = I18N.getString( "WFSPanel.loginFailed" ) + "\n" + ex.getLocalizedMessage() + "\n"
                         + I18N.getString( "WFSPanel.loginFailed2" );
            JOptionPane.showMessageDialog( this, msg );
            ex.printStackTrace();
            return;
        }

        JOptionPane.showMessageDialog( this, I18N.getString( "WFSPanel.loginSuccessful" ) );
    }

    // Gh 15.11.05
    private JComboBox createServerCombo() {
        // 
        if ( wfService != null ) {
            servers.add( 0, wfService.getCapabilitiesURL() );
        }
        String[] server = servers.toArray( new String[servers.size()] );
        final ExtensibleComboBox extensibleComboBox = new ExtensibleComboBox( server );
        extensibleComboBox.setSelectedIndex( 0 );
        extensibleComboBox.addItemListener( new ItemListener() {
            public void itemStateChanged( ItemEvent e ) {
                if ( e.getStateChange() == ItemEvent.SELECTED ) {
                    /*
                     * String selected = extensibleComboBox.getSelectedItem().toString(); reinitService( selected );
                     */
                }
            }
        } );
        return extensibleComboBox;
    }

    JTabbedPane getTabs() {
        return this.tabs;
    }

    protected void createXMLFrame( final Component parent, String txt ) {
        if ( txt == null )
            txt = "";

        // try to beautify the XML
        XMLFragment doc = new XMLFragment();
        try {
            doc.load( new StringReader( txt ), "http://www.systemid.org" );
            txt = doc.getAsPrettyString();
        } catch ( SAXException e ) {
            // ignore and use the old text
        } catch ( IOException e ) {
            // ignore and use the old text
        }

        if ( xmlPane == null ) {
            xmlPane = new JTextArea( txt );
        } else {
            xmlPane.setText( txt );
        }

        // ta.setLineWrap( true );
        JScrollPane sp = new JScrollPane( xmlPane, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED );
        sp.setMaximumSize( new Dimension( 600, 400 ) );
        sp.setPreferredSize( new Dimension( 800, 400 ) );

        String[] opts = new String[] { I18N.getString( "closeAndSave" ), I18N.getString( "OK" ) };

        int i = JOptionPane.showOptionDialog( parent, sp, I18N.getString( "FeatureResearchDialog.capabilities" ),
                                              JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, opts, opts[1] );

        if ( i == 0 ) { // save our capabilities!
            WFSPanel.saveTextToFile( parent, xmlPane.getText() );
        }
    }

    protected void reinitService( String url )
                            throws DeeJUMPException {
        wfService = "1.1.0".equals( this.wfsVersion ) ? new WFServiceWrapper_1_1_0( logins, url )
                                                     : new WFServiceWrapper_1_0_0( logins, url );
        refreshGUIs();
    }

    private Component createVersionButtons( String[] versions ) {
        JPanel p = new JPanel();

        p.add( new JLabel( I18N.getString( "FeatureResearchDialog.version" ) ) );
        ButtonGroup bg = new ButtonGroup();
        for ( int i = 0; i < versions.length; i++ ) {
            final JRadioButton b = new JRadioButton( versions[i] );
            if (i==1)
            	b.setSelected(true);
            else
            	b.setSelected(false);
           
            b.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    wfsVersion = b.getText();
                    // automatically switch to the default format for the version
                    if ( options != null ) {
                        if ( wfsVersion.equals( "1.0.0" ) ) {
                            options.setSelectedOutputFormat( options.getOutputFormats()[0] );
                        } else {
                            options.setSelectedOutputFormat( options.getOutputFormats()[1] );
                        }
                    }
                }
            } );
            
            bg.add( b );
            
            if ( i == 1 ) {// first is clicked
                b.doClick();
            }
            p.add( b );
        }
        
        
        return p;
    }

    private JComboBox createFeatureTypeCombo() {
        String[] start = { "            " };
        JComboBox tmpFeatureTypeCombo = new JComboBox( start );
        Dimension d = new Dimension( 300, 60 );
        tmpFeatureTypeCombo.setPreferredSize( d );
        tmpFeatureTypeCombo.setMaximumSize( d );

        Border border = BorderFactory.createTitledBorder( I18N.getString( "FeatureResearchDialog.featureType" ) );

        Border border2 = BorderFactory.createEmptyBorder( 5, 2, 10, 2 );

        border2 = BorderFactory.createCompoundBorder( border2, border );
        tmpFeatureTypeCombo.setBorder( border2 );
        tmpFeatureTypeCombo.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed( java.awt.event.ActionEvent evt ) {

                JComboBox combo = (JComboBox) evt.getSource();
                String selectFt = (String) combo.getSelectedItem();
                try {
                    attributeNames = wfService.getProperties( selectFt );

                    attributeResPanel.refreshPanel();
                    geoProperties = getGeoProperties();
                    propertiesPanel.setProperties( attributeNames, geoProperties );
                    spatialResPanel.resetGeoCombo( geoProperties );
                    // /hmmm repeated code...
                    WFSFeatureType ft = wfService.getFeatureTypeByName( selectFt );
                    if ( ft != null ) {
                        // UT could support other srrs, but not doing it now
                        String[] crs = new String[] { ft.getDefaultSRS().toString() };
                        srs = crs[0];
                        spatialResPanel.setCrs( crs );
                    }
                    requestTextArea.setRequestText( "" );
                } catch ( Exception e ) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog( WFSPanel.this, "Error loading schema: " + e.getMessage() );
                }
            }
        } );

        return tmpFeatureTypeCombo;
    }

    private JComponent createResponseTextArea() {

        JPanel p = new JPanel();
        p.setLayout( new BoxLayout( p, BoxLayout.Y_AXIS ) );

        responseTextArea = new JTextArea();
        responseTextArea.setLineWrap( true );
        responseTextArea.setWrapStyleWord( true );
        responseTextArea.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
        JScrollPane jsp = new JScrollPane( responseTextArea );

        p.add( jsp );

        return p;
    }

    /** Initializes the FeatureType combo box of the AttributeResearchPanel */

    private void refreshGUIs() {

        String[] featTypes = null;
        requestTextArea.setRequestText( "" );
        responseTextArea.setText( "" );
        try {
            featTypes = wfService.getFeatureTypes();
            Arrays.sort( featTypes );
            featureTypeCombo.setModel( new javax.swing.DefaultComboBoxModel( featTypes ) );

            controlButtons.okButton.setEnabled( true );
            capabilitiesButton.setEnabled( true );
            tabs.setEnabledAt( 1, true );

            featureTypeCombo.setEnabled( true );
            // featureTypeCombo.setVisible( true );
            attributeResPanel.setFeatureTypeComboEnabled( true );

        } catch ( Exception e ) {
            JOptionPane.showMessageDialog( this, "Could not connect to WFS server at \n'" + wfService.getBaseWfsURL()
                                                 + "'\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE );

            featureTypeCombo.setModel( new javax.swing.DefaultComboBoxModel( new String[0] ) );
            attributeResPanel.setFeatureTypeComboEnabled( false );
            tabs.setEnabledAt( 1, false );
            capabilitiesButton.setEnabled( false );
            controlButtons.okButton.setEnabled( false );

            tabs.setEnabled( false );

        }

        if ( featTypes != null && featTypes.length > 0 ) {
            try {

                attributeNames = wfService.getProperties( featTypes[0] );
                attributeResPanel.setEnabled( true );
                geoProperties = wfService.getGeometryProperties( featTypes[0] );

                propertiesPanel.setProperties( attributeNames, geoProperties );
                propertiesPanel.setEnabled( true );
                spatialResPanel.resetGeoCombo( geoProperties );

                // /hmmm repeated code...
                WFSFeatureType ft = wfService.getFeatureTypeByName( featTypes[0] );
                // UT could support other srs, but not doing it now
                if ( ft != null ) {
                    String[] crs = new String[] { ft.getDefaultSRS().toString() };
                    srs = crs[0];
                    spatialResPanel.setCrs( crs );
                }
                tabs.setEnabled( true );
            } catch ( Exception e ) {
                e.printStackTrace();
                // this is necessary to turn tabs click area on
                tabs.setEnabled( true );
                // if something went wrong, disable all but req/response tabs
                // featureTypeCombo.setModel( new javax.swing.DefaultComboBoxModel( new String[0] )
                // );

                attributeResPanel.setEnabled( false );
                propertiesPanel.setEnabled( false );
                spatialResPanel.setEnabled( false );

                requestTextArea.setEnabled( true );
                responseTextArea.setEnabled( true );

                controlButtons.okButton.setEnabled( true );

                JOptionPane.showMessageDialog( this, "Could not get DescribeFeatureType for '" + featTypes[0]
                                                     + "' from WFS server at \n'" + wfService.getBaseWfsURL() + "'\n"
                                                     + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE );
            }
        }

    }

    /** Creates a GetFeature request by concatenation of xlm elements */
    private StringBuffer concatenateRequest() {

        StringBuffer sb = new StringBuffer();
        if ( wfService == null ) {// not inited yet
            return sb;
        }
        sb.append( "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" );

        final String outputFormat = options.getSelectedOutputFormat();

        sb.append( "<wfs:GetFeature xmlns:ogc=\"http://www.opengis.net/ogc\" " );
        if ( logins != null && logins.getUsername() != null && logins.getPassword() != null ) {
            sb.append( "user=\"" + logins.getUsername() + "\" password=\"" + logins.getPassword() + "\" " );
        }
        sb.append( "xmlns:gml=\"http://www.opengis.net/gml\" " );
        sb.append( "xmlns:wfs=\"http://www.opengis.net/wfs\" service=\"WFS\" " );
        sb.append( "version=\"" ).append( wfService.getServiceVersion() ).append( "\" " );
        sb.append( "maxFeatures=\"" ).append( options.getMaxFeatures() ).append( "\" " );
        sb.append( "outputFormat=\"" ).append( outputFormat ).append( "\">" ).append( "<wfs:Query " );

        String ftName = (String) featureTypeCombo.getSelectedItem();
        QualifiedName ft = wfService.getFeatureTypeByName( ftName ).getName();

        if ( ft.getPrefix() != null && ft.getPrefix().length() > 0 ) {
            sb.append( "xmlns:" ).append( ft.getPrefix() ).append( "=\"" ).append( ft.getNamespace() );
            sb.append( "\" " );
        }
        sb.append( "srsName=\"" ).append( srs ).append( "\" " );
        sb.append( "typeName=\"" );

        String prefix = ft.getPrefix();
        if ( prefix != null && prefix.length() > 0 ) {
            sb.append( prefix ).append( ":" );
        }
        sb.append( ft.getLocalName() ).append( "\">" );

        sb.append( propertiesPanel.getXmlElement() );

        String spatCrit = attributeResPanel.getSpatialCriteria();

        int listSize = attributeResPanel.getListSize();

        String[] filterTags = new String[] { "", "" };

        if ( listSize > 0 || !NONE.equals( spatCrit ) ) {
            filterTags = createStartStopTags( "Filter" );
        }
        sb.append( filterTags[0] );

        boolean includesSpatialClause = !NONE.equals( spatCrit );
        if ( includesSpatialClause && listSize > 0 ) {
            sb.append( WFSPanel.createStartStopTags( "And" )[0] );
        }

        if ( BBOX.equals( spatCrit ) ) {
            sb.append( createBboxGml() );
        } else if ( SELECTED_GEOM.equals( spatCrit ) ) {
            sb.append( spatialResPanel.getXmlElement() );
        }

        sb.append( attributeResPanel.getXmlElement() );
        if ( includesSpatialClause && listSize > 0 ) {
            sb.append( WFSPanel.createStartStopTags( "And" )[1] );
        }

        sb.append( filterTags[1] );
        sb.append( "</wfs:Query></wfs:GetFeature>" );
        return sb;
    }

    /**
     * Creates the XML fragment containing a bounding box filter
     * 
     * @return the XML fragment containing a bounding box filter
     */
    private StringBuffer createBboxGml() {

        StringBuffer sb = new StringBuffer( 500 );

        QualifiedName ft = getFeatureType();
        QualifiedName qn = getChosenGeoProperty();

        if ( envelope != null ) {
            sb.append( "<ogc:BBOX>" ).append( "<ogc:PropertyName>" ).append( ft.getPrefix() ).append( ":" );
            sb.append( qn.getLocalName() ).append( "</ogc:PropertyName>" ).append( "<gml:Box><gml:coord>" );
            sb.append( "<gml:X>" ).append( envelope.getMinX() ).append( "</gml:X>" ).append( "<gml:Y>" );
            sb.append( envelope.getMinY() ).append( "</gml:Y>" ).append( "</gml:coord><gml:coord>" );
            sb.append( "<gml:X>" ).append( envelope.getMaxX() ).append( "</gml:X>" ).append( "<gml:Y>" );
            sb.append( envelope.getMaxY() ).append( "</gml:Y>" ).append( "</gml:coord></gml:Box></ogc:BBOX>" );
        }

        return sb;
    }

    /**
     * @param compo
     * @param txt
     */
    public static void saveTextToFile( Component compo, String txt ) {

        JFileChooser jfc = new JFileChooser();
        if ( lastDirectory != null ) {
            jfc.setCurrentDirectory( lastDirectory );
        }
        int i = jfc.showSaveDialog( compo );
        if ( i == JFileChooser.APPROVE_OPTION ) {
            try {

                FileWriter fw = new FileWriter( jfc.getSelectedFile() );
                fw.write( txt );
                fw.close();
                lastDirectory = jfc.getSelectedFile().getParentFile();
            } catch ( Exception e ) {
                JOptionPane.showMessageDialog( compo, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE );
                e.printStackTrace();
            }

        }

    }

    /**
     * Convenience method to create XML tags mit "ogc" namespace. For example an input like MyTag will return
     * <code>{"<ogc:MyTag>", "</ogc:MyTag>"}</code>
     * 
     * @param tagName
     *            the tag name
     * @return a String[] with start and stop tags
     */
    protected static final String[] createStartStopTags( String tagName ) {
        String[] tags = new String[] { "<ogc:" + tagName + ">", "</ogc:" + tagName + ">" };
        return tags;
    }

    /**
     * @return the geometry property name
     */
    public QualifiedName getChosenGeoProperty() {
        return geoProperty;
    }

    /**
     * @param geoProp
     */
    public void setGeoProperty( QualifiedName geoProp ) {
        this.geoProperty = geoProp;
    }

    /**
     * @return the geometry property names
     */
    public QualifiedName[] getGeoProperties() {
        return this.wfService.getGeometryProperties( (String) featureTypeCombo.getSelectedItem() );
    }

    /**
     * Returns the currently chosen feature type
     * 
     * @return the name of the currently chosen feature type
     */
    public QualifiedName getFeatureType() {
        String s = (String) featureTypeCombo.getSelectedItem();
        QualifiedName qn = null;
        WFSFeatureType ft = wfService.getFeatureTypeByName( s );
        if ( ft != null ) {
            qn = ft.getName();
        }
        return qn;
    }

    /**
     * @return the client
     */
    public AbstractWFSWrapper getWfService() {
        return this.wfService;
    }

    /**
     * Returns the currently selected geometry that serves as basis for spatial operation operations
     * 
     * @return the currently selected geometry
     */
    protected Geometry getSelectedGeometry() {
        return this.selectedGeom;
    }

    // GH 29.11.05
    private void setWFSList( List<String> serverURLS ) {
        servers = serverURLS;
    }

    /**
     * @return the current list of servers
     */
    public List<String> getWFSList() {
        LinkedList<String> list = new LinkedList<String>();

        String sel = serverCombo.getSelectedItem().toString();
        list.add( sel );

        for ( int i = 0; i < serverCombo.getItemCount(); ++i ) {
            String s = (String) serverCombo.getItemAt( i );
            if ( s != null && !sel.equals( s ) )
                list.add( s );
        }
        return list;
    }

    protected void setResposeText( String txt ) {
        responseTextArea.setText( txt );
        responseTextArea.setCaretPosition( 0 );
    }

    /**
     * @return the request
     */
    public String getRequest() {
        String t = requestTextArea.getText();
        if ( t == null || t.length() == 0 ) {
            t = concatenateRequest().toString();
        }
        return t.replaceAll( "\n", "" );
    }

    protected String createRequest() {
        String t = concatenateRequest().toString();
        return t.replaceAll( "\n", "" );
    }

    protected void setTabsVisible( boolean visible ) {
        tabs.setVisible( visible );
    }

    protected WFSOptions getOptions() {
        return this.options;
    }

    protected String getResponse() {
        return this.responseTextArea.getText();
    }

    /**
     * @return the srs
     */
    public String getGMLGeometrySRS() {
        return this.srs;
    }

    /**
     * @param env
     */
    public void setEnvelope( Envelope env ) {
        this.envelope = env;
    }

    /**
     * @param geom
     */
    public void setComparisonGeometry( Geometry geom ) {
        this.selectedGeom = geom;
    }

}
