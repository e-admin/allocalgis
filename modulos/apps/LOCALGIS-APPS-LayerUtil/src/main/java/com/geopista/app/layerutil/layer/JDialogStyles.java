/**
 * JDialogStyles.java
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
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.geopista.app.AppContext;
import com.geopista.server.administrador.web.LocalgisLayer;
import com.geopista.ui.wms.Style;
import com.vividsolutions.jump.I18N;

public class JDialogStyles extends JDialog
{
    
    private JPanel jContentPane = null;
    private JButton btnAceptar = null;
    private JScrollPane scrResultado = null;
    private JComboBox jListStyles = null;
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    
    private String titulo ="";
    private LocalgisLayer localgisLayer;
    private List listStyles;
    
    /**
     * Constructor por defecto
     */
    public JDialogStyles(LocalgisLayer localgisLayer,String titulo)
    {
        super();
        this.titulo = titulo;
        this.localgisLayer = localgisLayer;
        initialize();
    }
    
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        this.setSize(250, 160);
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
            jContentPane.add(getJListStyles());
            jContentPane.add(getBtnAceptar());
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
            btnAceptar.setBounds(new java.awt.Rectangle(65,74,94,24));
            btnAceptar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                	Style style = (Style)listStyles.get(jListStyles.getSelectedIndex());
                	localgisLayer.setStyleName(style.getTitle());
                	localgisLayer.setXml(style.getName());
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
            scrResultado.setViewportView(getJListStyles());
        }
        return scrResultado;
    }
    
    /**
     * This method initializes JListStyles	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getJListStyles()
    {
        if (jListStyles == null)
        {
        	jListStyles = new JComboBox();
        	jListStyles.setBounds(new java.awt.Rectangle(45,25,160,25));
        }
        return jListStyles;
    }
    
    /**
     * Establece una lista con los estilos que puede tener la capa
     * @param resultado Texto a mostrar
     */
    public void setListaEstilos(List listStyles)
    {      
    	this.listStyles = listStyles;
    	Iterator itStyle = listStyles.iterator();
    	String[] arrayStyles = new String[listStyles.size()];
    	int i = 0;
    	while (itStyle.hasNext()){
    		Style style = (Style)itStyle.next();
    		arrayStyles[i] = style.getTitle();
    		jListStyles.addItem(style.getTitle());
    		i++;
    	}
    	//jListStyles = new JComboBox(arrayStyles);       
    }
        
    
}  //  @jve:decl-index=0:visual-constraint="10,10"
