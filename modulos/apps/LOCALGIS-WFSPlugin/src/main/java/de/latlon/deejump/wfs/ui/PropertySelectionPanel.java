/**
 * PropertySelectionPanel.java
 * © MINETUR, Government of Spain
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

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.deegreewfs.datatypes.QualifiedName;
import org.deegreewfs.model.feature.schema.FeatureType;
import org.deegreewfs.model.feature.schema.GMLSchema;
import org.deegreewfs.model.feature.schema.PropertyType;

import de.latlon.deejump.wfs.i18n.I18N;

/**
 * ...
 * 
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * @author last edited by: $Author: satec $
 * 
 * @version 2.0, $Revision: 1.1 $, $Date: 2012/09/12 10:52:04 $
 * 
 * @since 2.0
 */

public class PropertySelectionPanel extends JPanel {

    private static final long serialVersionUID = 2886180413810632383L;

    private WFSPanel parentDialog;

    protected JList propertiesList;

    protected JComboBox geoPropsCombo;

    /**
     * @param parentDialog
     */
    public PropertySelectionPanel( WFSPanel parentDialog ) {
        super();
        this.parentDialog = parentDialog;
        initGUI();
        setEnabled( false );
    }

    private void initGUI() {

        JPanel p = new JPanel();
        p.setLayout( new BoxLayout( p, BoxLayout.Y_AXIS ) );
        p.setBorder( BorderFactory.createTitledBorder( I18N.getString( "PropertySelectionPanel.downloadProps" ) ) );

        propertiesList = new JList();
        JScrollPane scrollPane = new JScrollPane( propertiesList );

        Dimension dim = new Dimension( 400, 200 );

        scrollPane.setMaximumSize( dim );
        scrollPane.setPreferredSize( dim );
        scrollPane.setMinimumSize( dim );

        p.add( scrollPane );

        add( p );

        geoPropsCombo = new JComboBox();

        dim = new Dimension( 200, 40 );
        geoPropsCombo.setMaximumSize( dim );
        geoPropsCombo.setPreferredSize( dim );
        geoPropsCombo.setMinimumSize( dim );

        geoPropsCombo.setBorder( BorderFactory.createTitledBorder( I18N.getString( "SpatialResearchPanel.geometryName" ) ) );

        add( geoPropsCombo );

    }

    /**
     * @param simpleProps
     * @param geoProps
     */
    public void setProperties( String[] simpleProps, QualifiedName[] geoProps ) {

        resetPropsList( simpleProps );
        resetGeoCombo( geoProps );
    }

    private void resetGeoCombo( QualifiedName[] geoProps ) {
        geoPropsCombo.removeAllItems();
        if ( geoProps != null ) {
            for ( int i = 0; i < geoProps.length; i++ ) {

                if ( i == 0 ) {
                    this.parentDialog.setGeoProperty( geoProps[i] );
                }
                geoPropsCombo.addItem( geoProps[i] );
            }
        }
    }

    private void resetPropsList( String[] props ) {
        propertiesList.removeAll();
        DefaultListModel listModel = new DefaultListModel();
        int[] selIndices = new int[props.length];
        for ( int i = 0; i < props.length; i++ ) {
            listModel.addElement( props[i] );
            selIndices[i] = i;
        }
        propertiesList.setModel( listModel );
        propertiesList.setSelectedIndices( selIndices );

    }

    /**
     * @return not sure, part of the internal 'XML' processing
     */
    public StringBuffer getXmlElement() {

        StringBuffer sb = new StringBuffer( 5000 );

        QualifiedName ftQualiName = parentDialog.getFeatureType();

        GMLSchema schema = parentDialog.getWfService().getSchemaForFeatureType( ftQualiName.getPrefixedName() );

        if ( schema == null ) {
            return sb;
        }
        FeatureType[] featTypes = schema.getFeatureTypes();

        if ( featTypes.length < 1 ) {
            throw new RuntimeException( "Schema doesn't define any FeatureType. Must have at least one." );
        }

        // put what's been chosen in a list
        Object[] objs = propertiesList.getSelectedValues();
        List<Object> chosenProps = Arrays.asList( objs );

        // and loop over the correct order, seing what's in the list
        PropertyType[] featProperties = featTypes[0].getProperties();
        for ( int i = 0; i < featProperties.length; i++ ) {
            if ( chosenProps.contains( featProperties[i].getName().getLocalName() ) ) {
                sb.append( "<wfs:PropertyName>" );
                if ( ftQualiName.getPrefix() != null && ftQualiName.getPrefix().length() > 0 ) {
                    sb.append( ftQualiName.getPrefix() ).append( ":" );
                }
                sb.append( featProperties[i].getName().getLocalName() ).append( "</wfs:PropertyName>" );
            }

            // geom prop
            QualifiedName qn = (QualifiedName) geoPropsCombo.getSelectedItem();
            if ( qn.equals( featProperties[i].getName() ) ) {
                sb.append( "<wfs:PropertyName>" );
                if ( ftQualiName.getPrefix() != null && ftQualiName.getPrefix().length() > 0 ) {
                    sb.append( ftQualiName.getPrefix() ).append( ":" );
                }
                sb.append( qn.getLocalName() ).append( "</wfs:PropertyName>" );
            }

        }

        return sb;
    }

    @Override
    public void setEnabled( boolean enabled ) {
        super.setEnabled( enabled );
        geoPropsCombo.setEnabled( enabled );
        propertiesList.setEnabled( enabled );

        if ( !enabled ) {
            propertiesList.removeAll();
            geoPropsCombo.setModel( new DefaultComboBoxModel( new String[0] ) );
        }

    }

}