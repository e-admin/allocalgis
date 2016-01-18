/**
 * JDialogTestQuery.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.layer;


/**
 * Diálogo que muestra el resultado de una operación sobre la Base de Datos de GeoPISTA
 * 
 * @author cotesa
 *
 */
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.I18N;

public class JDialogTestQuery extends JDialog
{
    
    private JPanel jContentPane = null;
    private JButton btnAceptar = null;
    private JScrollPane scrResultado = null;
    private JTextArea txtResultado = null;
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    
    private String titulo ="";
    
    /**
     * Constructor por defecto
     */
    public JDialogTestQuery()
    {
        super();
        initialize();
    }
    
    /**
     * Constructor de la clase
     * @param titulo Título de la pantalla
     */
    public JDialogTestQuery(String titulo)
    {
        super();
        this.titulo = titulo;
        initialize();
    }
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        this.setSize(649, 310);
        this.setContentPane(getJContentPane());
        this.setTitle(titulo);
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
            jContentPane.add(getScrResultado(), null);
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
            btnAceptar.setText(I18N.get("GestorCapas","general.boton.aceptar"));
            btnAceptar.setBounds(new java.awt.Rectangle(265,254,94,26));
            btnAceptar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    dispose();
                }
            });
        }
        return btnAceptar;
    }
    
    /**
     * This method initializes scrResultado	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getScrResultado()
    {
        if (scrResultado == null)
        {
            scrResultado = new JScrollPane();
            scrResultado.setBounds(new java.awt.Rectangle(5,0,636,247));
            scrResultado.setViewportView(getTxtResultado());
        }
        return scrResultado;
    }
    
    /**
     * This method initializes txtResultado	
     * 	
     * @return javax.swing.JTable	
     */
    private JTextArea getTxtResultado()
    {
        if (txtResultado == null)
        {
            txtResultado = new JTextArea();
        }
        return txtResultado;
    }
    
    /**
     * Establece el valor del cuadro de texto txtResultado
     * @param resultado Texto a mostrar
     */
    public void setResultado(String resultado)
    {        
        txtResultado.setText(resultado);       
    }
    
    /**
     * Obtiene el texto mostrado en el cuadro de texto
     * @return Texto mostrado
     */
    public String getResultado()
    {
        return txtResultado.getText();
    }
    
    
}  //  @jve:decl-index=0:visual-constraint="10,10"
