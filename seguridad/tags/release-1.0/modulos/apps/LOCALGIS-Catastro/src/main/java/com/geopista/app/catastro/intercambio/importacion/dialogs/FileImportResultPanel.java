package com.geopista.app.catastro.intercambio.importacion.dialogs;

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


public class FileImportResultPanel extends JPanel 
{
    private JPanel jPanelLateral = null;
    private JPanel jPanelCentral = null;
    private JPanel jPanelResultadoImportacion;
    private JEditorPane jEditorPaneResultadoImportacion = null;
    private JLabel jLabelImagen = null;
    private JScrollPane jScrollPaneResultadoImportacion = null;

    public static final int DIM_X=600;
    public static final int DIM_Y=400;
    /**
     * This method initializes 
     * 
     */
    public FileImportResultPanel() {
    	super();
    	initialize();
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
       
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
            jPanelCentral.add(getJPanelResultadoImportacion(), 
                    new GridBagConstraints(0, 2, 1, 1, 1, 1,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return jPanelCentral;
    }

    /**
     * This method initializes jPanelResultadoImportacion  
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanelResultadoImportacion()
    {
        if (jPanelResultadoImportacion == null)
        {   
            jPanelResultadoImportacion = new JPanel(new GridBagLayout());
            jPanelResultadoImportacion.setBorder(
                    BorderFactory.createTitledBorder(I18N.get("Importacion","importar.general.resultado")));
            jPanelResultadoImportacion.add(getJScrollPaneResultadoImportacion(), 
                    new GridBagConstraints(0, 0, 1, 1, 1, 1, 
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 5, 10, 5), 0, 0));
        }
        return jPanelResultadoImportacion;
    }

    /**
     * This method initializes jEditorPaneResultadoImportacion	
     * 	
     * @return javax.swing.JEditorPane	
     */
    public JEditorPane getJEditorPaneResultadoImportacion()
    {
        if (jEditorPaneResultadoImportacion == null)
        {
            jEditorPaneResultadoImportacion = new JEditorPane();
            jEditorPaneResultadoImportacion.setText("<br><br>");
            jEditorPaneResultadoImportacion.setContentType("text/html");
            jEditorPaneResultadoImportacion.setEditable(false);
            jEditorPaneResultadoImportacion.setPreferredSize(new Dimension(100, 100));
        }
        return jEditorPaneResultadoImportacion;
    }

    /**
     * This method initializes jScrollPaneResultadoImportacion	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneResultadoImportacion()
    {
        if (jScrollPaneResultadoImportacion == null)
        {
            jScrollPaneResultadoImportacion = new JScrollPane();
            jScrollPaneResultadoImportacion.getViewport().add(getJEditorPaneResultadoImportacion());
            
        }
        return jScrollPaneResultadoImportacion;
    }
  
    
}  //  @jve:decl-index=0:visual-constraint="10,10"
