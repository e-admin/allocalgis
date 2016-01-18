/**
 * GraphicsImportSelectPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.catastro.intercambio.edicion.utils.ParcelaListCellRenderer;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.vividsolutions.jump.I18N;


public class GraphicsImportSelectPanel extends JPanel 
{
    private JPanel jPanelLateral = null;
    private JPanel jPanelCentral = null;
    private JPanel jPanelAsociacionInfo = null;
    private JLabel jLabelImagen = null;
    private JLabel jLabelParcela = null;
    private JComboBox jComboBoxParcela = null;
    private JButton jButtonFichDXF = null;
    private JLabel jLabelFichFXCC = null;
    private JTextField jTextFieldFichDXF = null;
    
    private static final String OPEN_FILE = "abrir.gif";
    private JLabel jLabelASC = null;
    private JTextField jTextFieldFichASC = null;
    private JButton jButtonFichASC = null;
    /**
     * This method initializes 
     */
    public GraphicsImportSelectPanel(String title) {
        super();
        //initialize(title, ModuloCatastralFrame.DIM_X/2,
        //        ModuloCatastralFrame.DIM_Y /2);
        initialize(title, 400,300);
    }
    /**
     * This method initializes 
     */
    public GraphicsImportSelectPanel(String title, int dimx, int dimy) {
        super();
        initialize(title, dimx, dimy);        
    }
    
    /**
     * This method initializes this
     * 
     */    
    private void initialize(String title, int dimx, int dimy) {
        
        this.setPreferredSize(new Dimension(dimx, dimy));
        this.setLayout(new GridBagLayout());
        this.add(getJPanelLateral(), 
                new GridBagConstraints(0, 0, 1, 1, 0, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                        new Insets(0, 5, 0, 5), 0, 0));
        
        this.add(getJPanelCentral(), 
                new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 5, 0, 5), 0, 0));
        
        jPanelAsociacionInfo.setBorder(
                BorderFactory.createTitledBorder(title));        
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
            jPanelCentral.add(getJPanelIntercambioInfo(), 
                    new GridBagConstraints(0, 2, 1, 1, 1, 1,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 5, 0, 5), 0, 0));            
        }
        return jPanelCentral;
    }
    
    /**
     * This method initializes jPanelIntercambioInfo  
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanelIntercambioInfo()
    {
        if (jPanelAsociacionInfo == null)
        {               
            jLabelASC = new JLabel(I18N.get("Importacion","importar.infografica.ficheroasc"), JLabel.LEFT);
            jPanelAsociacionInfo = new JPanel(new GridBagLayout());            
           
            jLabelFichFXCC = new JLabel(I18N.get("Importacion","importar.infografica.ficherodxf"), JLabel.LEFT);            
            jLabelParcela = new JLabel(I18N.get("Importacion","importar.infografica.parcela"), JLabel.LEFT);
            jPanelAsociacionInfo = new JPanel(new GridBagLayout()); 
            jPanelAsociacionInfo.add(jLabelParcela, 
                    new GridBagConstraints(0, 0, 2, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelAsociacionInfo.add(getJComboBoxParcela(), 
                    new GridBagConstraints(0, 1, 2, 1, 1, 0, 
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 10, 30), 250, 0));
            
            jPanelAsociacionInfo.add(getJButtonFichDXF(), 
                    new GridBagConstraints(1, 3, 1, 1, 1, 0, 
                            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));
            
            jPanelAsociacionInfo.add(jLabelFichFXCC, 
                    new GridBagConstraints(0, 2, 2, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 5), 0, 0));
            
            jPanelAsociacionInfo.add(getJTextFieldFichDXF(), 
                    new GridBagConstraints(0, 3, 1, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 5), 220, 0));
            jPanelAsociacionInfo.add(jLabelASC, 
                    new GridBagConstraints(0, 4, 2, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 5), 0, 0));
            jPanelAsociacionInfo.add(getJTextFieldFichASC(), 
                    new GridBagConstraints(0, 5, 1, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 5), 220, 0));
            jPanelAsociacionInfo.add(getJButtonFichASC(), 
                    new GridBagConstraints(1, 5, 1, 1, 1, 0, 
                            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));
            
            
        }
        return jPanelAsociacionInfo;
    }
    /**
     * This method initializes jTextFieldCertDigital	
     * 	
     * @return javax.swing.JTextField	
     */
    public JComboBox getJComboBoxParcela()
    {
        if (jComboBoxParcela == null)
        {
            jComboBoxParcela = new JComboBox();  
            jComboBoxParcela.setRenderer(new ParcelaListCellRenderer());
        }
        return jComboBoxParcela;
    }
    /**
     * This method initializes jButtonFichDXF	
     * 	
     * @return javax.swing.JButton	
     */
    public JButton getJButtonFichDXF()
    {
        if (jButtonFichDXF == null)
        {
            jButtonFichDXF = new JButton();
            jButtonFichDXF.setIcon(IconLoader.icon(OPEN_FILE));
        }
        return jButtonFichDXF;
    }
    /**
     * This method initializes jTextFieldFichFXCC	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getJTextFieldFichDXF()
    {
        if (jTextFieldFichDXF == null)
        {
            jTextFieldFichDXF = new JTextField();
            jTextFieldFichDXF.setEditable(false);
            jTextFieldFichDXF.setBackground(new Color(200, 200, 200));
        }
        return jTextFieldFichDXF;
    }
    /**
     * This method initializes jTextFieldFichASC	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getJTextFieldFichASC()
    {
        if (jTextFieldFichASC == null)
        {
            jTextFieldFichASC = new JTextField();
            jTextFieldFichASC.setEditable(false);
            jTextFieldFichASC.setBackground(new Color(200, 200, 200));
        }
        return jTextFieldFichASC;
    }
    /**
     * This method initializes jButtonFichASC	
     * 	
     * @return javax.swing.JButton	
     */
    public JButton getJButtonFichASC()
    {
        if (jButtonFichASC == null)
        {
            jButtonFichASC = new JButton();
            jButtonFichASC.setIcon(IconLoader.icon(OPEN_FILE));
        }
        return jButtonFichASC;
    }    
    
}  //  @jve:decl-index=0:visual-constraint="10,10"
