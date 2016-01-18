/**
 * EstadoImportFXCCPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion.paneles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.vividsolutions.jump.I18N;


public class EstadoImportFXCCPanel extends JPanel 
{
    private JPanel jPanelLateral = null;
    private JPanel jPanelCentral = null;
    private JPanel jPanelImportInformation = null;
    private JPanel jPanelFileInformation = null;

    private JPanel jPanelEstadoValidacion;
    private JEditorPane jEditorPaneErrores = null;
    private JLabel jLabelImagen = null;
    private JScrollPane jScrollPaneErrores = null;
    
    public static final int DIM_X=600;
    public static final int DIM_Y=400;

    private static final String OPEN_FILE = "abrir.gif";    
   
    
    /**
     * This method initializes 
     * 
     */
    public EstadoImportFXCCPanel(String title) {
    	super();
    	initialize(title);
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize(String title) {
        
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(DIM_X, DIM_Y));
        this.add(getJPanelLateral(), 
                new GridBagConstraints(0, 0, 1, 1, 0, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                        new Insets(0, 5, 0, 5), 0, 0));

        this.add(getJPanelCentral(), 
                new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 5, 0, 5), 0, 0));
        this.setBorder(BorderFactory.createTitledBorder(title));
    		
    }

    /**
     * This method initializes jPanelLateral	
     * 	
     * @return javax.swing.JPanel	
     */
    public JPanel getJPanelLateral()
    {
        if (jPanelLateral == null)
        {
            jLabelImagen = new JLabel();
            jPanelLateral = new JPanel();
            jPanelLateral.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            jPanelLateral.add(jLabelImagen, null);
           
        }
        return jPanelLateral;
    }
    
    public JLabel getLabelImagen()
    {
        return jLabelImagen;
    }

    /**
     * This method initializes jPanelCentral	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelCentral()
    {
        if (jPanelCentral == null)
        {           
            jPanelCentral = new JPanel(new GridBagLayout()); 

            jPanelCentral.add(getJPanelEstadoValidacion(), 
                    new GridBagConstraints(0, 2, 1, 1, 1, 1,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 5, 0, 5), 0, 0));
        }
        return jPanelCentral;
    }


    /**
     * This method initializes jPanelEstadoValidacion  
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanelEstadoValidacion()
    {
        if (jPanelEstadoValidacion == null)
        {   
            jPanelEstadoValidacion = new JPanel(new GridBagLayout());
            jPanelEstadoValidacion.setBorder(
                    BorderFactory.createTitledBorder(
                            I18N.get("Importacion","importar.fichero.fxcc.importacion.estado")));
            jPanelEstadoValidacion.add(getJScrollPaneErrores(), 
                    new GridBagConstraints(0, 0, 1, 1, 1, 1, 
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
                            new Insets(10, 5, 10, 5), 0, 0));
    
        }
        return jPanelEstadoValidacion;
    }

    /**
     * This method initializes jEditorPaneErrores	
     * 	
     * @return javax.swing.JEditorPane	
     */
    public JEditorPane getJEditorPaneErrores()
    {
        if (jEditorPaneErrores == null)
        {
            jEditorPaneErrores = new JEditorPane();
            jEditorPaneErrores.setText("<br><br>");
            jEditorPaneErrores.setContentType("text/html");
            jEditorPaneErrores.setEditable(false);
            jEditorPaneErrores.setSize(595, 100);
        }
        return jEditorPaneErrores;
    }

    /**
     * This method initializes jScrollPaneErrores	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneErrores()
    {
        if (jScrollPaneErrores == null)
        {
            jScrollPaneErrores = new JScrollPane();
            jScrollPaneErrores.getViewport().add(getJEditorPaneErrores());
            
        }
        return jScrollPaneErrores;
    }
  
    
}  //  @jve:decl-index=0:visual-constraint="10,10"
