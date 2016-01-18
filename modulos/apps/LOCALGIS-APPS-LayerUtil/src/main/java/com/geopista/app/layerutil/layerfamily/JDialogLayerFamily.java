/**
 * JDialogLayerFamily.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.layerfamily;


/**
 * Diálogo en el que se muestran los datos relevantes de una tabla
 * 
 * @author cotesa
 *
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.geopista.app.AppConstants;
import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.layerutil.images.IconLoader;
import com.geopista.app.layerutil.util.JDialogTranslations;
import com.geopista.model.LayerFamily;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;

public class JDialogLayerFamily extends JDialog
{
    
    private JPanel jContentPane = null;
    private JButton btnAceptar = null;
    private JButton btnCancelar = null;
    private JButton btnIdiomasNombre = null;
    private JButton btnIdiomasDescripcion = null;
    
    private JPanel jPanel = null;
    private JLabel lblLayerfamilyName = null;
    private JLabel lblLayerfamilyDescription = null;
    private JTextField txtLayerfamilyName = null; 
    private JTextField txtLayerfamilyDescription = null;
        
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private boolean bAceptar = false;
    
    private static final String ICONO_BANDERA= "banderas.gif";
    private String locale = UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, AppConstants.DEFAULT_LOCALE, false);
    
    private Hashtable htNombres = new Hashtable();
    private Hashtable htDescripciones = new Hashtable();
    
    
    /**
     * Constructor por defecto
     */
    public JDialogLayerFamily()
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
    public JDialogLayerFamily(java.awt.Frame parent, boolean modal,
            LayerFamilyTable lft, boolean bEnabled)
    {        
        super(parent, modal);  
        initialize();
        initComponents(lft);        
        this.txtLayerfamilyDescription.setEnabled(bEnabled);
        this.txtLayerfamilyName.setEnabled(bEnabled);
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
        this.setTitle(I18N.get("GestorCapas","pantalla.layerfamily.titulo"));
        this.setModal(true);
    }
    
    /**
     * Inicializa los componentes
     * @param lft Objeto Table que contiene los datos de la tabla a mostrar
     */
    private void initComponents(LayerFamilyTable lft)
    {   
        if (lft!=null)
        {
            this.txtLayerfamilyDescription.setText(lft.getHtDescripcion().get(locale).toString());
            this.txtLayerfamilyName.setText(lft.getHtNombre().get(locale).toString());
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
        if (txtLayerfamilyDescription.getText().trim().equals("") 
                || txtLayerfamilyName.getText().trim().equals(""))
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
        
        if (txtLayerfamilyDescription.getText().trim().length()>0)
            htDescripciones.put(locale, txtLayerfamilyDescription.getText());  
        else
            htDescripciones.put(locale, "");               
        
         
        if (txtLayerfamilyName.getText().trim().length()>0)
            htNombres.put(locale, txtLayerfamilyName.getText());  
        else
            htNombres.put(locale, "");                
        
        //Valores por defecto para el locale por defecto 
        if (htDescripciones.get(AppConstants.DEFAULT_LOCALE)==null)
            htDescripciones.put(AppConstants.DEFAULT_LOCALE, txtLayerfamilyDescription.getText());
        if (htNombres.get(AppConstants.DEFAULT_LOCALE)==null)
            htNombres.put(AppConstants.DEFAULT_LOCALE, txtLayerfamilyName.getText());
                
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
            lblLayerfamilyDescription = new JLabel();
            lblLayerfamilyDescription.setBounds(new java.awt.Rectangle(17,64,91,23));
            lblLayerfamilyDescription.setText(I18N.get("GestorCapas","layerfamilies.crear.descripcion"));
            lblLayerfamilyName = new JLabel();
            lblLayerfamilyName.setBounds(new java.awt.Rectangle(17,31,91,23));
            lblLayerfamilyName.setText(I18N.get("GestorCapas","layerfamilies.crear.nombre"));
            jPanel = new JPanel();
            jPanel.setBounds(new java.awt.Rectangle(29,20,286,121));
            jPanel.setLayout(null);
            jPanel.setBorder(BorderFactory.createEtchedBorder());
            jPanel.add(lblLayerfamilyName, null);
            jPanel.add(lblLayerfamilyDescription, null);
            jPanel.add(getTxtName(), null);
            jPanel.add(getTxtDescription(), null);
            jPanel.add(getBtnIdiomasDescripcion(), null);
            jPanel.add(getBtnIdiomasNombre(), null);
        }
        return jPanel;
    }
    
    /**
     * This method initializes jTextField   
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getTxtName()
    {
        if (txtLayerfamilyName == null)
        {
            txtLayerfamilyName = new JTextField();
            txtLayerfamilyName.setBounds(new java.awt.Rectangle(113,31,134,22));  
            
            
            txtLayerfamilyName.getDocument().addDocumentListener(new DocumentListener() {
                
                public void insertUpdate(DocumentEvent evt) {
                    
                    int len = evt.getLength();
                    int off = evt.getOffset();
                    
                    if (txtLayerfamilyDescription.getText().length() >= txtLayerfamilyName.getText().length()-len)
                    {
                        
                        try {
                            // Cadena insertada
                            if (txtLayerfamilyDescription.getText().length()==0
                                    || off==txtLayerfamilyDescription.getText().length())
                            {
                                txtLayerfamilyDescription.setText(txtLayerfamilyDescription.getText()
                                        + evt.getDocument().getText(off, len));
                            }
                            else
                            {
                                txtLayerfamilyDescription.setText(txtLayerfamilyDescription.getText().substring(0, off)
                                        + evt.getDocument().getText(off, len) +
                                        txtLayerfamilyDescription.getText().substring(off));
                            }
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                
                public void removeUpdate(DocumentEvent evt) {
                    
                    int len = evt.getLength(); 
                    int off = evt.getOffset();   
                    
                    if (txtLayerfamilyDescription.getText().length()-len >= txtLayerfamilyName.getText().length())
                    {
                        txtLayerfamilyDescription.setText(txtLayerfamilyDescription.getText().substring(0, off)
                                + txtLayerfamilyDescription.getText().substring(off+len));
                    }
                }
                
                // This method is called after one or more attributes have changed.
                // This method is not called when characters are inserted with attributes.
                public void changedUpdate(DocumentEvent evt) {
                    
                }
                
            });
        }
        return txtLayerfamilyName;
    }
    
    /**
     * This method initializes jComboBox    
     *  
     * @return javax.swing.JComboBox    
     */
    private JTextField getTxtDescription()
    {
        if (txtLayerfamilyDescription == null)
        {
            txtLayerfamilyDescription = new JTextField();
            txtLayerfamilyDescription.setBounds(new java.awt.Rectangle(113,64,134,22));
            
        }
        return txtLayerfamilyDescription;
    }
    
    
    /**
     * This method initializes btnIdiomas    
     *  
     * @return javax.swing.JButton  
     */
    private JButton getBtnIdiomasDescripcion()
    {
        if (btnIdiomasDescripcion == null)
        {
            btnIdiomasDescripcion = new JButton();
            btnIdiomasDescripcion.setBounds(new java.awt.Rectangle(248,65,20,20));
            btnIdiomasDescripcion.setIcon(IconLoader.icon(ICONO_BANDERA));
            btnIdiomasDescripcion.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    //Llamar al metodo correspondiente
                    btnIdiomasDescripcion_actionPerformed(e);
                }
                    });
        }
        return btnIdiomasDescripcion;
    }
    
    /**
     * This method initializes btnIdiomasNombre    
     *  
     * @return javax.swing.JButton  
     */
    private JButton getBtnIdiomasNombre()
    {
        if (btnIdiomasNombre == null)
        {
            btnIdiomasNombre = new JButton();
            btnIdiomasNombre.setBounds(new java.awt.Rectangle(248,31,20,20));
            btnIdiomasNombre.setIcon(IconLoader.icon(ICONO_BANDERA));
            btnIdiomasNombre.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    //Llamar al metodo correspondiente
                    btnIdiomasNombre_actionPerformed(e);
                }
                    });
        }
        return btnIdiomasNombre;
    }
    
    
    /**
     * Lanza la pantalla que permite la inserción de datos en los diferentes
     * idiomas dados de alta en el sistema
     * @param e
     */
    private void btnIdiomasNombre_actionPerformed(ActionEvent e)
    {           
        if (txtLayerfamilyName.getText().length()>0)
            htNombres.put(locale, txtLayerfamilyName.getText());  
        else
            htNombres.put(locale, "");                
        
        JDialogTranslations jDiccionario= showIdiomasDescripcion(htNombres);
        
        Hashtable hDict=jDiccionario.getDiccionario();
        if (hDict!=null) 
            htNombres = hDict;
        jDiccionario=null;
        
        if (htNombres.get(locale)!=null)
            txtLayerfamilyName.setText(htNombres.get(locale).toString());
        else if (htNombres.get(AppConstants.DEFAULT_LOCALE)!=null)
            txtLayerfamilyName.setText(htNombres.get(AppConstants.DEFAULT_LOCALE).toString());
        else
            txtLayerfamilyName.setText("");    
        
    }
        
    /**
     * Lanza la pantalla que permite la inserción de datos en los diferentes
     * idiomas dados de alta en el sistema
     * @param e
     */
    private void btnIdiomasDescripcion_actionPerformed(ActionEvent e)
    {           
        if (txtLayerfamilyDescription.getText().length()>0)
            htDescripciones.put(locale, txtLayerfamilyDescription.getText());  
        else
            htDescripciones.put(locale, "");        
        
        JDialogTranslations jDiccionario= showIdiomasDescripcion(htDescripciones);
        
        Hashtable hDict=jDiccionario.getDiccionario();
        if (hDict!=null) 
            htDescripciones = hDict;
        jDiccionario=null;
        
        if (htDescripciones.get(locale)!=null)
            txtLayerfamilyDescription.setText(htDescripciones.get(locale).toString());
        else if (htNombres.get(AppConstants.DEFAULT_LOCALE)!=null)
            txtLayerfamilyDescription.setText(htDescripciones.get(AppConstants.DEFAULT_LOCALE).toString());
        else
            txtLayerfamilyDescription.setText("");            
    }   
        
    /**
     * Muestra el panel con los diferentes valores de un atributo en los distintos idiomas 
     * dados de alta en el sistema
     * @param nombres Hashtable con los nombres para cada idioma como valor y el locale correspondiente
     * como clave
     * @return Diálogo de diccionario para mostrar los valores del atributo en los distintos idiomas
     */
    private JDialogTranslations showIdiomasDescripcion(Hashtable nombres)
    {
        JDialogTranslations jDiccionario = new JDialogTranslations(aplicacion.getMainFrame(), true, nombres,true);
        jDiccionario.setSize(600,500);
        jDiccionario.setLocationRelativeTo(this);
        jDiccionario.show();
        return jDiccionario;        
    }
    
    /**
     * Obtiene los datos mostrados en el diálogo
     * @return Objeto Table con los datos de la tabla mostrada en el diálogo
     */
    public LayerFamilyTable getLayerFamily ()
    {
        if(bAceptar)
        {
            LayerFamilyTable lft = new LayerFamilyTable();
            lft.setHtNombre(getHtNombre());
            lft.setHtDescripcion(getHtDescripcion());
            LayerFamily lf = new LayerFamily();
            lf.setName(txtLayerfamilyName.getText());
            lf.setDescription(txtLayerfamilyDescription.getText());
            lft.setLayerFamily(lf);
            return lft;
        }
        else
            return null;
    }
    
  
    public Hashtable getHtNombre()
    {
        return htNombres;
    }
   
    public Hashtable getHtDescripcion()
    {
        return htDescripciones;
    }
}
