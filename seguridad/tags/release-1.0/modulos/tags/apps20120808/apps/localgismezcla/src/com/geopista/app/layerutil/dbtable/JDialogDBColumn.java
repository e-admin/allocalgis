/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 *
 */


package com.geopista.app.layerutil.dbtable;


/**
 * Diálogo en el que se muestran los datos relevantes de una columna
 * 
 * @author cotesa
 *
 */
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.geopista.app.layerutil.schema.column.ColumnRow;
import com.vividsolutions.jump.I18N;


public class JDialogDBColumn extends JDialog
{
    private JPanel jContentPane = null;
    private JPanel jPanel = null;
    private JLabel lblNombre = null;
    private JLabel lblTipo = null;
    private JLabel lblDefecto = null;
    private JLabel lblComentario = null;
    private JTextField  txtNombreBD = null;
    private JComboBox cmbTipo = null;
    private JTextField txtValor = null;
    private JTextArea jTextAreaComentario = null;  //  @jve:decl-index=0:visual-constraint="389,10"
    private JScrollPane scrAreaComentario = null;
    private JCheckBox jCheckBoxNoNulo = null;
    private JCheckBox jCheckBoxUnico = null;
    private JLabel lblTamanio = null;
    private JSpinner spinnerTamanio = null;
    private JButton btnAceptar = null;
    private JButton btnCancelar = null;
    private JLabel lblPrecision = null;
    private JSpinner spinnerPrecision = null;
    private JLabel lblNombreSistema = null;
    private JTextField txtNombreSistema = null;
    
    private boolean bAceptar = false;
    
    
    /**
     * Constructor por defecto
     */
    public JDialogDBColumn()
    {
        super();        
        initialize();
    }
    
    /**
     * Constructor de la clase
     * 
     * @param parent Frame padre del diálogo
     * @param modal Verdadero si se desea bloquear la pantalla padre mientras se mantenga
     * abierta la pantalla hija
     * @param colrow Objeto ColumnRow con los datos de la columna a mostrar 
     * @param bEnabled Verdadero si se desea habilitar los campos del diálogo
     */
    public JDialogDBColumn(Frame parent, boolean modal,
            ColumnRow colrow, boolean bEnabled)
    {        
        super(parent, modal);  
        initialize();
        initComponents(colrow);        
        this.txtNombreBD.setEnabled(bEnabled);       
        this.txtValor.setEnabled(bEnabled);
        this.txtNombreSistema.setEnabled(bEnabled);
        this.jTextAreaComentario.setEnabled(bEnabled);
        this.jCheckBoxNoNulo.setEnabled(bEnabled);
        this.jCheckBoxUnico.setEnabled(bEnabled);
        
    }
    
    /**
     * Inicializa los componentes
     * @param colrow ColumnRow con los datos con los que se desea inicializar la pantalla
     */
    private void initComponents(ColumnRow colrow)
    {   
        if (colrow!=null)
        {
            this.txtNombreBD.setText(colrow.getColumnaBD().getName());
            this.cmbTipo.setSelectedItem(translateBDtoComboItem(colrow.getColumnaBD().getType()));
            
            
            this.spinnerTamanio.setValue(new Integer(colrow.getColumnaBD().getLength()));
            this.spinnerPrecision.setValue(new Integer(colrow.getColumnaBD().getPrecission()));
            String defValue = colrow.getColumnaBD().getDefaultValue();
            if (defValue!=null && defValue.trim().length()!=0)
            {
                int separatorPos = defValue.lastIndexOf("::");
                if (separatorPos>0)
                    defValue = defValue.substring(0, separatorPos);
                if (defValue.startsWith("'") && defValue.endsWith("'"))
                    defValue = defValue.substring(1, defValue.length()-1);                
            }
            
            this.txtValor.setText(defValue);
            this.jTextAreaComentario.setText(colrow.getColumnaBD().getDescription());
            this.jCheckBoxNoNulo.setSelected(colrow.getColumnaBD().isNotNull());
            this.jCheckBoxUnico.setSelected(colrow.getColumnaBD().isUnique());  
            this.txtNombreSistema.setText(colrow.getColumnaSistema().getName());
        }
    } 
    
    /**
     * Traduce el tipo de datos devuelto por el catálogo de PostgreSQL al tipo de datos
     * utilizado en GeoPISTA
     * @param type Tipo de datos del catálogo de PostgreSQL
     * @return Tipo de datos de GeoPISTA
     */
    public static String translateBDtoComboItem(String type)
    {
        if (type.equalsIgnoreCase("int4"))
            return TablesDBPanel.COL_INTEGER;
        else if (type.equalsIgnoreCase("bpchar"))
            return TablesDBPanel.COL_CHAR;
        else if (type.equalsIgnoreCase("bool"))
            return TablesDBPanel.COL_BOOLEAN;
        else 
            return type.toUpperCase();
        
    }    
    
    /**
     * Traduce el tipo de datos devuelto por el catálogo de PostgreSQL al tipo de datos
     * utilizado en GeoPISTA
     * @param type Tipo de datos del catálogo de PostgreSQL
     * @return Tipo de datos de GeoPISTA
     */
    private String translateComboItemtoBD(String type)
    {
        if (type.equalsIgnoreCase(TablesDBPanel.COL_INTEGER))
            return "int4";
        else if (type.equalsIgnoreCase(TablesDBPanel.COL_CHAR))
            return "bpchar";
        else if (type.equalsIgnoreCase(TablesDBPanel.COL_BOOLEAN))
            return "bool";
        else 
            return type.toLowerCase();
        
    }    
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        this.setSize(380, 400);
        this.setContentPane(getJContentPane());
        this.setTitle(I18N.get("GestorCapas","tablasBD.columna.dialogo.titulo"));
        this.setModal(true);
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
            jContentPane.add(getJPanel(), null);
            jContentPane.add(getBtnAceptar(), null);
            jContentPane.add(getBtnCancelar(), null);
        }
        return jContentPane;
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
            lblNombreSistema = new JLabel();
            lblNombreSistema.setBounds(new java.awt.Rectangle(18,54,149,26));
            lblNombreSistema.setText(I18N.get("GestorCapas","tablasBD.columna.nombreSis"));
            lblPrecision = new JLabel();
            lblPrecision.setBounds(new java.awt.Rectangle(18,136,150,28));
            lblPrecision.setText(I18N.get("GestorCapas","tablasBD.columna.precision"));
            lblTamanio = new JLabel();
            lblTamanio.setBounds(new java.awt.Rectangle(18,109,150,28));
            lblTamanio.setText(I18N.get("GestorCapas","tablasBD.columna.tamanio"));
            lblComentario = new JLabel();
            lblComentario.setBounds(new java.awt.Rectangle(18,190,150,28));
            lblComentario.setText(I18N.get("GestorCapas","tablasBD.columna.comentario"));
            lblDefecto = new JLabel();
            lblDefecto.setBounds(new java.awt.Rectangle(18,163,150,28));
            lblDefecto.setText(I18N.get("GestorCapas","tablasBD.columna.defecto"));
            lblTipo = new JLabel();
            lblTipo.setBounds(new java.awt.Rectangle(18,82,150,28));
            lblTipo.setText(I18N.get("GestorCapas","tablasBD.columna.tipodatos"));
            lblNombre = new JLabel();
            lblNombre.setBounds(new java.awt.Rectangle(18,23,150,28));
            lblNombre.setText(I18N.get("GestorCapas","tablasBD.columna.nombreBD"));
            jPanel = new JPanel();
            jPanel.setBounds(new java.awt.Rectangle(8,9,352,322));
            jPanel.setLayout(null);
            jPanel.setBorder(BorderFactory.createTitledBorder(I18N.get("GestorCapas","tablasBD.columna.dialogo.titulo")));
            jPanel.add(lblNombre, null);
            jPanel.add(lblTipo, null);
            jPanel.add(lblDefecto, null);
            jPanel.add(lblComentario, null);
            jPanel.add(getTxtNombreBD(), null);            
            jPanel.add(getTxtFieldValor(), null);
            jPanel.add(getScrComentario(), null);
            jPanel.add(getJCheckBoxNoNulo(), null);
            jPanel.add(getJCheckBoxUnico(), null);
            
            jPanel.add(getCmbTipo(), null);  
            
            jPanel.add(lblTamanio, null);
            jPanel.add(getSpinnerTamanio(), null);
            jPanel.add(lblPrecision, null);
            jPanel.add(getSpinnerPrecision(), null);
            jPanel.add(lblNombreSistema, null);
            jPanel.add(getTxtNombreSistema(), null);
            
            
            
        }
        return jPanel;
    }
    
    /**
     * This method initializes txtNombre  
     *  
     * @return javax.swing.JTextField    
     */
    private JTextField getTxtNombreBD()
    {
        if (txtNombreBD == null)
        {
            txtNombreBD = new JTextField();
            txtNombreBD.setBounds(new java.awt.Rectangle(176,24,161,20));
            txtNombreBD.setBorder(BorderFactory.createLoweredBevelBorder());
            
            txtNombreBD.getDocument().addDocumentListener(new DocumentListener() {
                
                public void insertUpdate(DocumentEvent evt) {
                    
                    int len = evt.getLength();
                    int off = evt.getOffset();
                    
                    if (txtNombreSistema.getText().length() >= txtNombreBD.getText().length()-len)
                    {
                        
                        try {
                            // Cadena insertada
                            if (txtNombreSistema.getText().length()==0
                                    || off==txtNombreSistema.getText().length())
                            {
                                txtNombreSistema.setText(txtNombreSistema.getText()
                                        + evt.getDocument().getText(off, len));
                            }
                            else
                            {
                                txtNombreSistema.setText(txtNombreSistema.getText().substring(0, off)
                                        + evt.getDocument().getText(off, len) +
                                        txtNombreSistema.getText().substring(off));
                            }
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                
                public void removeUpdate(DocumentEvent evt) {
                    
                    int len = evt.getLength(); 
                    int off = evt.getOffset();   
                    
                    if (txtNombreSistema.getText().length()-len >= txtNombreBD.getText().length())
                    {
                        txtNombreSistema.setText(txtNombreSistema.getText().substring(0, off)
                                + txtNombreSistema.getText().substring(off+len));
                    }
                }
                
                // This method is called after one or more attributes have changed.
                // This method is not called when characters are inserted with attributes.
                public void changedUpdate(DocumentEvent evt) {
                    
                }
                
            });
            
            
        }
        return txtNombreBD;
    }
    
    /**
     * This method initializes jComboBoxTipo    
     *  
     * @return javax.swing.JComboBox    
     */
    private JComboBox getCmbTipo()
    {
        if (cmbTipo == null)
        {
            cmbTipo = new JComboBox();
            cmbTipo.setBounds(new java.awt.Rectangle(176,85,161,20));
            cmbTipo.addItem("");
            cmbTipo.addItem(TablesDBPanel.COL_NUMERIC);            
            cmbTipo.addItem(TablesDBPanel.COL_INTEGER);
            cmbTipo.addItem(TablesDBPanel.COL_VARCHAR);
            cmbTipo.addItem(TablesDBPanel.COL_CHAR);
            cmbTipo.addItem(TablesDBPanel.COL_DATE);
            cmbTipo.addItem(TablesDBPanel.COL_GEOMETRY);
            cmbTipo.addItem(TablesDBPanel.COL_BYTEA);
            cmbTipo.addItem(TablesDBPanel.COL_TIMESTAMP);
            cmbTipo.addItem(TablesDBPanel.COL_BOOLEAN);
            
            cmbTipo.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {                    
                    //Llamar al metodo correspondiente
                    cmbTipo_actionPerformed(e);
                }
                    });           
            
        }
        return cmbTipo;
    }
    /**
     * Acción a realizar en cuanto se produzca un cambio en el combo de tipos de geometría
     * @param e Evento
     */
    protected void cmbTipo_actionPerformed(ActionEvent e)
    {
        if (spinnerTamanio!=null &&
                (cmbTipo.getSelectedItem().toString().equals(TablesDBPanel.COL_NUMERIC)
                        || cmbTipo.getSelectedItem().toString().equals(TablesDBPanel.COL_VARCHAR)
                        || cmbTipo.getSelectedItem().toString().equals(TablesDBPanel.COL_CHAR)))
            
        {
            spinnerTamanio.setEnabled(true);            
        }
        else if (spinnerTamanio!=null)
        {
            spinnerTamanio.setEnabled(false);  
            spinnerTamanio.setValue(new Integer(0)); 
        }
        if (spinnerPrecision!=null && cmbTipo.getSelectedItem().toString().equals(TablesDBPanel.COL_NUMERIC))
            
        {
            spinnerPrecision.setEnabled(true);            
        }
        else if (spinnerPrecision!=null)
        {
            spinnerPrecision.setEnabled(false);  
            spinnerPrecision.setValue(new Integer(0)); 
        }
        
        repaint();
    }
    
    
    /**
     * This method initializes txtValor   
     *  
     * @return javax.swing.JTextField    
     */
    private JTextField getTxtFieldValor()
    {
        if (txtValor == null)
        {
            txtValor = new JTextField();
            txtValor.setBounds(new java.awt.Rectangle(176,165,161,20));
        }
        return txtValor;
    }
    
    /**
     * This method initializes scrAreaComentario 
     *  
     * @return javax.swing.JScrollPane   
     */
    private JScrollPane getScrComentario()
    {
        if (scrAreaComentario == null)
        {
            scrAreaComentario = new JScrollPane();
            scrAreaComentario.setBounds(new java.awt.Rectangle(175,195,161,83));
            jTextAreaComentario = getTxtComentario();
            scrAreaComentario.setViewportView(jTextAreaComentario);
        }
        return scrAreaComentario;
    }
    
    private JTextArea getTxtComentario()
    {
        if (jTextAreaComentario == null)
        {
            jTextAreaComentario = new JTextArea();
            jTextAreaComentario.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            jTextAreaComentario.setSize(new java.awt.Dimension(158,67));
        }
        return jTextAreaComentario;
    }
    
    
    /**
     * This method initializes jCheckBoxNoNulo  
     *  
     * @return javax.swing.JCheckBox    
     */
    private JCheckBox getJCheckBoxNoNulo()
    {
        if (jCheckBoxNoNulo == null)
        {
            jCheckBoxNoNulo = new JCheckBox();
            jCheckBoxNoNulo.setBounds(new java.awt.Rectangle(67,285,93,29));
            jCheckBoxNoNulo.setText(I18N.get("GestorCapas","tablasBD.columna.nonulo"));
        }
        return jCheckBoxNoNulo;
    }
    
    /**
     * This method initializes jCheckBoxUnico   
     *  
     * @return javax.swing.JCheckBox    
     */
    private JCheckBox getJCheckBoxUnico()
    {
        if (jCheckBoxUnico == null)
        {
            jCheckBoxUnico = new JCheckBox();
            jCheckBoxUnico.setBounds(new java.awt.Rectangle(175,286,112,28));
            jCheckBoxUnico.setText(I18N.get("GestorCapas","tablasBD.columna.unico"));
        }
        return jCheckBoxUnico;
    }
    
    /**
     * This method initializes spinnerTamanio 
     *  
     * @return javax.swing.JSpinner    
     */
    private JSpinner getSpinnerTamanio()
    {
        if (spinnerTamanio == null)
        {
            SpinnerModel numberModel = new SpinnerNumberModel(0, //initial value
                    0,      //min
                    1000,   //max
                    1);     //step
            
            
            spinnerTamanio = new JSpinner(numberModel);
            spinnerTamanio.setBounds(new java.awt.Rectangle(176,112,161,20));  
           
        }
        return spinnerTamanio;
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
            btnAceptar.setBounds(new java.awt.Rectangle(84,334,100,30));
            btnAceptar.setText(I18N.get("GestorCapas","general.boton.aceptar"));            
            btnAceptar.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    jButtonAceptar_actionPerformed();
                }
                    });
        }
        return btnAceptar;
    }
    
    /**
     * Comprueba que se hayan introducido los datos obligatorios
     * @return Verdadero si se han introducido todos los datos obligatorios
     */
    private  boolean comprobarDatos()
    {
        if (cmbTipo.getSelectedIndex()==0 || txtNombreBD.getText().trim().equals(""))
            return false;
        else
            return true;
    }
    
    /**
     * Acción a realizar en cuanto se pulsa el botón de Aceptar
     *
     */
    private void jButtonAceptar_actionPerformed() 
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
            btnCancelar.setBounds(new java.awt.Rectangle(189,334,100,30));
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
     * This method initializes spinnerPrecision	
     * 	
     * @return javax.swing.JSpinner	
     */
    private JSpinner getSpinnerPrecision()
    {
        if (spinnerPrecision == null)
        {
            SpinnerModel numberModel = new SpinnerNumberModel(0, //initial value
                    0,      //min
                    1000,   //max
                    1);     //step            
            
            spinnerPrecision = new JSpinner(numberModel);
            spinnerPrecision.setBounds(new java.awt.Rectangle(176,139,161,20));
        }
        return spinnerPrecision;
    }
    
    /**
     * Devuelve los datos mostrados en la pantalla en forma de bean de tipo ColumnRow
     * @return ColumnRow con los datos de la pantalla
     */
    public ColumnRow getColumnRow ()
    {
        if(bAceptar)
        {
            ColumnRow colRow = new ColumnRow();
            colRow.getColumnaBD().setName(txtNombreBD.getText());
            
            //translateBDtoComboItem(colrow.getColumnaBD().getType())
            colRow.getColumnaBD().setType(translateComboItemtoBD(cmbTipo.getSelectedItem().toString()));
            colRow.getColumnaBD().setLength(Integer.valueOf(spinnerTamanio.getValue().toString()).intValue());
            colRow.getColumnaBD().setPrecission(Integer.valueOf(spinnerPrecision.getValue().toString()).intValue());
            colRow.getColumnaBD().setDefaultValue(txtValor.getText());
            colRow.getColumnaBD().setDescription(jTextAreaComentario.getText());
            colRow.getColumnaBD().setNotNull(jCheckBoxNoNulo.isSelected());
            colRow.getColumnaBD().setUnique(jCheckBoxUnico.isSelected());
            
            if (!txtNombreSistema.getText().trim().equals(""))
            {
                colRow.getColumnaSistema().setDescription(txtNombreSistema.getText());
                colRow.getColumnaSistema().setName(txtNombreSistema.getText());   
            }
            //else
            //{
            //    colRow.getColumnaSistema().setDescription(txtNombreBD.getText());
            //    colRow.getColumnaSistema().setName(txtNombreBD.getText()); 
            //}                      
            
            return colRow;
        }
        else
            return null;
    }    
    
    /**
     * This method initializes txtNombreSistema	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getTxtNombreSistema()
    {
        if (txtNombreSistema == null)
        {
            txtNombreSistema = new JTextField();
            txtNombreSistema.setBounds(new java.awt.Rectangle(176,54,161,20));
        }
        return txtNombreSistema;
    }    
    
}  //  @jve:decl-index=0:visual-constraint="-2,10"
