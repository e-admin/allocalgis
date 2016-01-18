package com.vividsolutions.jump.workbench.ui.plugin.datastore;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.vividsolutions.jump.I18N;

import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.datastore.ConnectionDescriptor;
import com.vividsolutions.jump.workbench.datastore.ConnectionManager;
import com.vividsolutions.jump.workbench.ui.OKCancelDialog;


/**
 *  Base class for panels with no Connection combobox.
 */
public class WithOutConnectionPanel extends JPanel {

    protected final static int MAIN_COLUMN_WIDTH = 400;

    protected final static Insets INSETS = new Insets( 2, 2, 2, 2 );

    private WorkbenchContext context;

//    private JComboBox connectionComboBox = null;

    private int nextRow = 0;

//    private JButton chooseConnectionButton = null;


    public WithOutConnectionPanel( WorkbenchContext context ) {
        this.context = context;
        initialize();
    }

    public WorkbenchContext getContext() {
        return context;
    }



    public String validateInput() {
//        if ( getConnectionDescriptor() == null ) {
//            return I18N.get("jump.workbench.ui.plugin.datastore.ConnectionPanel.Required-field-missing-Connection");
//        }
        return null;
    }

    protected void addRow( String caption,
                           Component a,
                           Component b,
                           boolean aStretchesVertically ) {
        add( new JLabel( caption ),
             new GridBagConstraints( 0,
               nextRow,
               1,
               1,
               0,
               0,
               GridBagConstraints.NORTHWEST,
               GridBagConstraints.NONE,
               INSETS,
               0,
               0 )
            );

        if ( aStretchesVertically ) {
            add( a,
                 new GridBagConstraints( 1,
                 nextRow,
                 1,
                 1,
                 1,
                 1,
                 GridBagConstraints.NORTHWEST,
                 GridBagConstraints.BOTH,
                 INSETS,
                 0,
                 0 )
            );
        } else {
            add( a,
                 new GridBagConstraints( 1,
                 nextRow,
                 1,
                 1,
                 1,
                 0,
                 GridBagConstraints.NORTHWEST,
                 GridBagConstraints.HORIZONTAL,
                 INSETS,
                 0,
                 0 )
            );
        }

        if ( b != null ) {
            add( b, new GridBagConstraints( 2, nextRow, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                INSETS, 0, 0 ) );
        }

        nextRow++;
    }


   

    protected Object[] sortByString( Object[] objects ) {
        Arrays.sort( objects,
            new Comparator() {
                public int compare( Object o1, Object o2 ) {
                    return o1.toString().compareTo( o2.toString() );
                }
            } );
        return objects;
    }

  

    private void initialize() {
        setLayout( new GridBagLayout() );
      
    }

    private void chooseConnection() {
        ConnectionManagerPanel panel = new ConnectionManagerPanel(
            ConnectionManager.instance( getContext() ),
            getContext().getRegistry(), getContext().getErrorHandler(), context );
        OKCancelDialog dialog = new OKCancelDialog( ( Dialog ) SwingUtilities.windowForComponent( WithOutConnectionPanel.this ), 
        	I18N.get("jump.workbench.ui.plugin.datastore.ConnectionPanel.Connection-Manager"),
            true, panel,
            new OKCancelDialog.Validator() {
                public String validateInput( Component component ) {
                    return null;
                }
            } );
        dialog.setVisible( true );
        // Even if OK was not pressed, refresh the combobox.
        // [Jon Aquino 2005-03-16]
        if ( !dialog.wasOKPressed() ) {
            return;
        }
        if ( panel.getSelectedConnectionDescriptors().isEmpty() ) {
            return;
        }
  
    }
}
