/**
 * JDialogDBTable.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.dbtable;


/**
 * Diálogo en el que se muestran los datos relevantes de una tabla
 * 
 * @author cotesa
 *
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.feature.Table;
import com.vividsolutions.jump.I18N;

public class JDialogDBTable extends JDialog
{
    
    private JPanel jContentPane = null;
    private JButton btnAceptar = null;
    private JButton btnCancelar = null;
    private JPanel jPanel = null;
    private JLabel lblTableName = null;
    private JLabel lblGeometryType = null;
    private JTextField txtTableName = null; 
    private JComboBox cmbGeometryType = null;
    private boolean bAceptar = false;
    
    /**
     * Constructor por defecto
     */
    public JDialogDBTable()
    {
        super();
        initialize();
    }
    
    /**
     * Constructor de la clase
     * 
     * @param parent Frame padre del diálogo
     * @param modal Verdadero si se desea bloquear la pantalla padre hasta que se cierre
     * la pantalla hija
     * @param table Objeto Table que contiene los datos de la tabla a mostrar
     * @param bEnabled Verdadero si se desea que los campos del diálogo permanezcan habilitados
     */
    public JDialogDBTable(java.awt.Frame parent, boolean modal,
            Table table, boolean bEnabled)
    {        
        super(parent, modal);  
        initialize();
        initComponents(table);        
        this.cmbGeometryType.setEnabled(bEnabled);
        this.txtTableName.setEnabled(bEnabled);
    }
    
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        this.setSize(350, 220);
        this.setContentPane(getJContentPane());
        this.setTitle(I18N.get("GestorCapas","tablasBD.tabla.dialogo.titulo"));
        this.setModal(true);
    }
    
    /**
     * Inicializa los componentes
     * @param table Objeto Table que contiene los datos de la tabla a mostrar
     */
    private void initComponents(Table table)
    {   
        if (table!=null)
        {
            this.cmbGeometryType.setSelectedItem(getSelectedGeometryType(table.getGeometryType()));
            this.txtTableName.setText(table.getDescription());
        }
    }
    
    
    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane()
    {
        if (jContentPane == null)
        {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getBtnAceptar(), null);
            jContentPane.add(getBtnCancelar(), null);
            jContentPane.add(getJPanel(), null);
        }
        return jContentPane;
    }
    
    /**
     * This method initializes btnAceptar  
     *  
     * @return javax.swing.JButton  
     */
    private JButton getBtnAceptar()
    {
        if (btnAceptar == null)
        {
            btnAceptar = new JButton();
            btnAceptar.setBounds(new java.awt.Rectangle(55,151,100,30));
            btnAceptar.setText(I18N.get("GestorCapas","general.boton.aceptar"));
            
            btnAceptar.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    jButtonAceptar();
                }
                    });
        }
        return btnAceptar;
    }
        
    /**
     * Comprueba que se hayan introducido todos los datos obligatorios en el diálogo
     * @return
     */
    private  boolean comprobarDatos()
    {
        if (txtTableName.getText().trim().equals(""))
            return false;
        else
            return true;
    }
    
    /**
     * Acción a realizar cuando se pulsa el botón Aceptar
     *
     */
    private void jButtonAceptar() 
    {
        if (!comprobarDatos())
        {
            JOptionPane.showOptionDialog(this,
                    I18N.get("GestorCapas","tablasBD.columna.mensaje.completar"),
                    I18N.get("GestorCapas","general.mensaje.error"),
                    JOptionPane.ERROR_MESSAGE,
                    JOptionPane.DEFAULT_OPTION,null,new String[]{I18N.get("GestorCapas","general.mensaje.volver")},null);
            return;
        }
        
        bAceptar = true;
        dispose();
    }
        
    /**
     * This method initializes btnCancelar 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getBtnCancelar()
    {
        if (btnCancelar == null)
        {
            btnCancelar = new JButton();
            btnCancelar.setBounds(new java.awt.Rectangle(191,151,100,30));
            btnCancelar.setText(I18N.get("GestorCapas","general.boton.cancelar"));
            btnCancelar.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    bAceptar = false;
                    dispose();
                }
                    });
        }
        return btnCancelar;
    }
    
    /**
     * This method initializes jPanel   
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanel()
    {
        if (jPanel == null)
        {
            lblGeometryType = new JLabel();
            lblGeometryType.setBounds(new java.awt.Rectangle(17,64,119,23));
            lblGeometryType.setText(I18N.get("GestorCapas","tablasBD.tabla.tipogeom"));
            lblTableName = new JLabel();
            lblTableName.setBounds(new java.awt.Rectangle(17,31,119,23));
            lblTableName.setText(I18N.get("GestorCapas","tablasBD.tabla.nombre"));
            jPanel = new JPanel();
            jPanel.setBounds(new java.awt.Rectangle(29,20,286,121));
            jPanel.setLayout(null);
            jPanel.setBorder(BorderFactory.createTitledBorder(I18N.get("GestorCapas","tablasBD.tabla.dialogo.titulo")));
            jPanel.add(lblTableName, null);
            jPanel.add(lblGeometryType, null);
            jPanel.add(getJTextField(), null);
            jPanel.add(getJComboBox(), null);
        }
        return jPanel;
    }
    
    /**
     * This method initializes jTextField   
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextField()
    {
        if (txtTableName == null)
        {
            txtTableName = new JTextField();
            txtTableName.setBounds(new java.awt.Rectangle(150,31,119,22));            
        }
        return txtTableName;
    }
    
    /**
     * This method initializes jComboBox    
     *  
     * @return javax.swing.JComboBox    
     */
    private JComboBox getJComboBox()
    {
        if (cmbGeometryType == null)
        {
            cmbGeometryType = new JComboBox();
            cmbGeometryType.setBounds(new java.awt.Rectangle(150,64,119,22));
            
            cmbGeometryType.removeAllItems();
            cmbGeometryType.addItem(I18N.get("GestorCapas","tablasBD.tabla.dialogo.tipo.alfanumerico"));
            cmbGeometryType.addItem(ColumnDB.TAB_GEOMETRY);
            cmbGeometryType.addItem(ColumnDB.TAB_POINT);
            cmbGeometryType.addItem(ColumnDB.TAB_LINESTRING);
            cmbGeometryType.addItem(ColumnDB.TAB_POLYGON);
            cmbGeometryType.addItem(ColumnDB.TAB_COLLECTION);
            cmbGeometryType.addItem(ColumnDB.TAB_MULTIPOINT);
            cmbGeometryType.addItem(ColumnDB.TAB_MULTILINESTRING);
            cmbGeometryType.addItem(ColumnDB.TAB_MULTIPOLYGON);
            
        }
        return cmbGeometryType;
    }
    
    /**
     * Obtiene los datos mostrados en el diálogo
     * @return Objeto Table con los datos de la tabla mostrada en el diálogo
     */
    public Table getTable ()
    {
        if(bAceptar)
        {
            Table tab = new Table();
            tab.setDescription(txtTableName.getText());
            tab.setGeometryType(getGeometryType(cmbGeometryType.getSelectedItem().toString()));
            return tab;
        }
        else
            return null;
    }
    
    /**
     * Obtiene el tipo de geometría a partir del elemento seleccionado en el combo
     * de tipos de geometría
     * @param item Cadena que especifica el tipo de geometría
     * @return Identificador numérico del tipo de geometría, según lo definido en
     * com.geopista.feature.Table 
     */
    private int getGeometryType(String item)
    {
        if (item.equals(ColumnDB.TAB_POINT))
            return Table.POINT;
        else if (item.equals(ColumnDB.TAB_LINESTRING))
            return Table.LINESTRING;
        else if (item.equals(ColumnDB.TAB_POLYGON))
            return Table.POLYGON;
        else if (item.equals(ColumnDB.TAB_COLLECTION))
            return Table.COLLECTION;
        else if (item.equals(ColumnDB.TAB_MULTIPOINT))
            return Table.MULTIPOINT;
        else if (item.equals(ColumnDB.TAB_MULTILINESTRING))
            return Table.MULTILINESTRING;
        else if (item.equals(ColumnDB.TAB_MULTIPOLYGON))
            return Table.MULTIPOLYGON;
        else if (item.equals(ColumnDB.TAB_GEOMETRY))
            return Table.GEOMETRY;
        else 
            return -1;        
        
    }
    
    /**
     * Obtiene el tipo de geometría a partir de los tipos definidos en com.geopista.feature.Table
     * @param item Identificador del tipo de geometría
     * @return Tipo de geometría para mostrar en el combo
     */
    private String getSelectedGeometryType(int item)
    {
        if (item ==Table.POINT)
            return ColumnDB.TAB_POINT;
        else if (item ==Table.LINESTRING)
            return ColumnDB.TAB_LINESTRING;
        else if (item == Table.POLYGON)
            return ColumnDB.TAB_POLYGON;
        else if (item == Table.COLLECTION )
            return ColumnDB.TAB_COLLECTION;
        else if (item == Table.MULTIPOINT )
            return ColumnDB.TAB_MULTIPOINT;
        else if (item == Table.MULTILINESTRING)
            return ColumnDB.TAB_MULTILINESTRING;
        else if (item == Table.MULTIPOLYGON)
            return ColumnDB.TAB_MULTIPOLYGON;
        else if (item == Table.GEOMETRY)
            return ColumnDB.TAB_GEOMETRY;
        else 
            return I18N.get("GestorCapas","tablasBD.tabla.dialogo.tipo.alfanumerico");        
        
    }
}
