package com.geopista.app.catastro.intercambio.importacion.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.vividsolutions.jump.I18N;


public class InformationImportSelectPanel extends JPanel 
{
    private JPanel jPanelLateral = null;
    private JPanel jPanelCentral = null;
    private JPanel jPanelIntercambioInfo;
    private JLabel jLabelImagen = null;
    private JLabel jLabelCertDigital = null;
    private JTextField jTextFieldCertDigital = null;
    private JButton jButtonCertDigital = null;
    private JLabel jLabelPassword = null;
    private JPasswordField jPasswordFieldPassword = null;
    private static final String OPEN_FILE = "abrir.gif";
    
    public static final int DIM_X=500;
    public static final int DIM_Y=300;
    /**
     * This method initializes 
     */
    public InformationImportSelectPanel(String title) {
        super();
        //initialize(title, ModuloCatastralFrame.DIM_X/2,
        //        ModuloCatastralFrame.DIM_Y /2);
        initialize(title, 400,300);
    }
    /**
     * This method initializes 
     */
    public InformationImportSelectPanel(String title, int dimx, int dimy) {
        super();
        initialize(title, dimx, dimy);
        
    }
    
    /**
     * This method initializes this
     * 
     */    
    private void initialize(String title, int dimx, int dimy) {
        
        //this.setSize(new Dimension(dimx, dimy));
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
        
        jPanelIntercambioInfo.setBorder(
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
        if (jPanelIntercambioInfo == null)
        {   
            jPanelIntercambioInfo = new JPanel(new GridBagLayout());            
           
            jLabelPassword = new JLabel(I18N.get("Importacion","importar.intercambio.password"), JLabel.LEFT);            
            jLabelCertDigital = new JLabel(I18N.get("Importacion","importar.intercambio.certificado"), JLabel.LEFT);
            jPanelIntercambioInfo = new JPanel(new GridBagLayout()); 
            jPanelIntercambioInfo.add(jLabelCertDigital, 
                    new GridBagConstraints(0, 0, 2, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelIntercambioInfo.add(getJTextFieldCertDigital(), 
                    new GridBagConstraints(0, 1, 1, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 250, 0));
            
            jPanelIntercambioInfo.add(getJButtonCertDigital(), 
                    new GridBagConstraints(1, 1, 1, 1, 1, 0, 
                            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelIntercambioInfo.add(jLabelPassword, 
                    new GridBagConstraints(0, 2, 2, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(20, 5, 0, 5), 0, 0));
            
            jPanelIntercambioInfo.add(getJPasswordFieldPassword(), 
                    new GridBagConstraints(0, 3, 2, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
            
        }
        return jPanelIntercambioInfo;
    }
    /**
     * This method initializes jTextFieldCertDigital	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getJTextFieldCertDigital()
    {
        if (jTextFieldCertDigital == null)
        {
            jTextFieldCertDigital = new JTextField();
            jTextFieldCertDigital.setEditable(false);
            jTextFieldCertDigital.setBackground(new Color(200, 200, 200));
        }
        return jTextFieldCertDigital;
    }
    /**
     * This method initializes jButtonCertDigital	
     * 	
     * @return javax.swing.JButton	
     */
    public JButton getJButtonCertDigital()
    {
        if (jButtonCertDigital == null)
        {
            jButtonCertDigital = new JButton();
            jButtonCertDigital.setIcon(IconLoader.icon(OPEN_FILE));
        }
        return jButtonCertDigital;
    }
    /**
     * This method initializes jPasswordFieldPassword	
     * 	
     * @return javax.swing.JPasswordField	
     */
    public JPasswordField getJPasswordFieldPassword()
    {
        if (jPasswordFieldPassword == null)
        {
            jPasswordFieldPassword = new JPasswordField();
        }
        return jPasswordFieldPassword;
    }
    
    
    
    
    
}  //  @jve:decl-index=0:visual-constraint="10,10"
