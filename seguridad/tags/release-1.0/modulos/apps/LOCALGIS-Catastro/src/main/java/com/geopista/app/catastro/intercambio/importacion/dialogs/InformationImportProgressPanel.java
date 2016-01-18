package com.geopista.app.catastro.intercambio.importacion.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.vividsolutions.jump.I18N;


public class InformationImportProgressPanel extends JPanel 
{
    private JPanel jPanelLateral = null;
    private JPanel jPanelCentral = null;
    private JPanel jPanelProgresoIntercambioInfo = null;
    private JLabel jLabelImagen = null;
    private JLabel jLabelConectarOVC = null;  
    private JLabel jLabelValidarFichero = null;
    private JLabel jLabelProcesoFinalizado = null;
    private JLabel jLabelTickProcesoFinalizado = null;
    private JLabel jLabelActualizarSistema = null;
    private JLabel jLabelTickActualizarSistema = null;
    private JLabel jLabelTickValidarFichero = null;
    private JLabel jLabelTickConectarOVC = null;
    
    private static final String CHECKED = "tick.gif";
    private static final String NO_CHECKED = "notick.gif";
    
    public static final int DIM_X=500;
    public static final int DIM_Y=300;
    /**
     * This method initializes 
     */
    public InformationImportProgressPanel(String title) {
        super();
        //initialize(title, ModuloCatastralFrame.DIM_X/2,
        //        ModuloCatastralFrame.DIM_Y /2);
        initialize(title, 400,300);
    }
    /**
     * This method initializes 
     */
    public InformationImportProgressPanel(String title, int dimx, int dimy) {
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
        
        jPanelProgresoIntercambioInfo.setBorder(
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
            jPanelCentral.add(getJPanelProgresoIntercambioInfo(), 
                    new GridBagConstraints(0, 2, 1, 1, 1, 1,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 5, 0, 5), 0, 0));            
        }
        return jPanelCentral;
    }
    
    /**
     * This method initializes jPanelProgresoIntercambioInfo  
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanelProgresoIntercambioInfo()
    {
        if (jPanelProgresoIntercambioInfo == null)
        {   
            jPanelProgresoIntercambioInfo = new JPanel(new GridBagLayout());            
           
            jLabelTickConectarOVC = new JLabel();
            jLabelConectarOVC = new JLabel(I18N.get("Importacion","importar.intercambio.proceso.conectarOVC"), JLabel.LEFT);
            jLabelTickValidarFichero = new JLabel();
            jLabelValidarFichero = new JLabel(I18N.get("Importacion","importar.intercambio.proceso.validando"), JLabel.LEFT);            
            jLabelTickActualizarSistema = new JLabel();
            jLabelActualizarSistema = new JLabel(I18N.get("Importacion","importar.intercambio.proceso.actualizando"), JLabel.LEFT);
            jLabelTickProcesoFinalizado = new JLabel();
            jLabelProcesoFinalizado = new JLabel(I18N.get("Importacion","importar.intercambio.proceso.fin"), JLabel.LEFT);
            
            jPanelProgresoIntercambioInfo = new JPanel(new GridBagLayout()); 
            
            jPanelProgresoIntercambioInfo.add(jLabelTickConectarOVC, 
                    new GridBagConstraints(0, 0, 1, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
           
            jPanelProgresoIntercambioInfo.add(jLabelConectarOVC, 
                    new GridBagConstraints(1, 0, 1, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,  new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelProgresoIntercambioInfo.add(jLabelTickValidarFichero, 
                    new GridBagConstraints(0, 1, 1, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,  new Insets(5, 5, 5, 5), 0, 0));
           
            jPanelProgresoIntercambioInfo.add(jLabelValidarFichero, 
                    new GridBagConstraints(1, 1, 1, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,  new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelProgresoIntercambioInfo.add(jLabelTickActualizarSistema, 
                    new GridBagConstraints(0, 2, 1, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,  new Insets(5, 5, 5, 5), 0, 0));
           
            jPanelProgresoIntercambioInfo.add(jLabelActualizarSistema, 
                    new GridBagConstraints(1, 2, 1, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,  new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelProgresoIntercambioInfo.add(jLabelTickProcesoFinalizado, 
                    new GridBagConstraints(0, 3, 1, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,  new Insets(5, 5, 5, 5), 0, 0));
           
            jPanelProgresoIntercambioInfo.add(jLabelProcesoFinalizado, 
                    new GridBagConstraints(1, 3, 1, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,  new Insets(5, 5, 5, 5), 0, 0));
            
           
        }
        return jPanelProgresoIntercambioInfo;
    }
    
    public void loadIconChecked (JLabel label)
    {
        label.setIcon(IconLoader.icon(CHECKED));
    }
    
    public void loadIconNoChecked (JLabel label)
    {
        label.setIcon(IconLoader.icon(NO_CHECKED));
    }
	/**
	 * @return the jLabelActualizarSistema
	 */
	public JLabel getJLabelActualizarSistema() {
		return jLabelActualizarSistema;
	}
	/**
	 * @param labelActualizarSistema the jLabelActualizarSistema to set
	 */
	public void setJLabelActualizarSistema(JLabel labelActualizarSistema) {
		jLabelActualizarSistema = labelActualizarSistema;
	}
	/**
	 * @return the jLabelConectarOVC
	 */
	public JLabel getJLabelConectarOVC() {
		return jLabelConectarOVC;
	}
	/**
	 * @param labelConectarOVC the jLabelConectarOVC to set
	 */
	public void setJLabelConectarOVC(JLabel labelConectarOVC) {
		jLabelConectarOVC = labelConectarOVC;
	}
	/**
	 * @return the jLabelProcesoFinalizado
	 */
	public JLabel getJLabelProcesoFinalizado() {
		return jLabelProcesoFinalizado;
	}
	/**
	 * @param labelProcesoFinalizado the jLabelProcesoFinalizado to set
	 */
	public void setJLabelProcesoFinalizado(JLabel labelProcesoFinalizado) {
		jLabelProcesoFinalizado = labelProcesoFinalizado;
	}
	/**
	 * @return the jLabelTickActualizarSistema
	 */
	public JLabel getJLabelTickActualizarSistema() {
		return jLabelTickActualizarSistema;
	}
	/**
	 * @param labelTickActualizarSistema the jLabelTickActualizarSistema to set
	 */
	public void setJLabelTickActualizarSistema(JLabel labelTickActualizarSistema) {
		jLabelTickActualizarSistema = labelTickActualizarSistema;
	}
	/**
	 * @return the jLabelTickConectarOVC
	 */
	public JLabel getJLabelTickConectarOVC() {
		return jLabelTickConectarOVC;
	}
	/**
	 * @param labelTickConectarOVC the jLabelTickConectarOVC to set
	 */
	public void setJLabelTickConectarOVC(JLabel labelTickConectarOVC) {
		jLabelTickConectarOVC = labelTickConectarOVC;
	}
	/**
	 * @return the jLabelTickProcesoFinalizado
	 */
	public JLabel getJLabelTickProcesoFinalizado() {
		return jLabelTickProcesoFinalizado;
	}
	/**
	 * @param labelTickProcesoFinalizado the jLabelTickProcesoFinalizado to set
	 */
	public void setJLabelTickProcesoFinalizado(JLabel labelTickProcesoFinalizado) {
		jLabelTickProcesoFinalizado = labelTickProcesoFinalizado;
	}
	/**
	 * @return the jLabelTickValidarFichero
	 */
	public JLabel getJLabelTickValidarFichero() {
		return jLabelTickValidarFichero;
	}
	/**
	 * @param labelTickValidarFichero the jLabelTickValidarFichero to set
	 */
	public void setJLabelTickValidarFichero(JLabel labelTickValidarFichero) {
		jLabelTickValidarFichero = labelTickValidarFichero;
	}
	/**
	 * @return the jLabelValidarFichero
	 */
	public JLabel getJLabelValidarFichero() {
		return jLabelValidarFichero;
	}
	/**
	 * @param labelValidarFichero the jLabelValidarFichero to set
	 */
	public void setJLabelValidarFichero(JLabel labelValidarFichero) {
		jLabelValidarFichero = labelValidarFichero;
	}
    
}  //  @jve:decl-index=0:visual-constraint="10,10"
