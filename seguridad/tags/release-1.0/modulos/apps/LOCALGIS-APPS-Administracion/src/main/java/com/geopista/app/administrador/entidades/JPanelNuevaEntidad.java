/**
 * 
 */
package com.geopista.app.administrador.entidades;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.geopista.app.utilidades.TextField;


/**
 * @author seilagamo
 *
 */
public class JPanelNuevaEntidad extends JPanel {
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(JPanelModificarEntidad.class);
	
   
    private JLabel nombreEntidadJLabel;
    private TextField nombreEntidadJText;
    private JLabel sridEntidadJLabel;
    private TextField sridEntidadJText;
    private JLabel avisoJLabel;
    private TextField avisoJText;
    private JLabel periodicidadJLabel;
    private TextField periodicidadJText;
    private JLabel intentosJLabel;
    private TextField intentosJText;
    private JLabel entidadExtJLabel;
    private TextField entidadExtJText;
    private JButton aniadirEntidadListaJButton;
    private JCheckBox backupJCheckBox;
    private ResourceBundle mensajesUsuario;
//    private JButton cancelarAniadirEntidadJButton;
    
    public JPanelNuevaEntidad(ResourceBundle messages, JFrame framePadre, boolean modoEdicion) {
        initialize(messages);
        changeScreenLang(messages);
        editable(false);
    }
    
    private void initialize(ResourceBundle messages){
    	mensajesUsuario = messages;
        this.setPreferredSize(new Dimension(238, 235));
        this.setMaximumSize(new Dimension(238, 235));
        this.setLayout(new GridBagLayout());
        nombreEntidadJLabel = new JLabel();
        nombreEntidadJText = new TextField(50);
        this.add(nombreEntidadJLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));     
        this.add(nombreEntidadJText, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        sridEntidadJLabel = new JLabel();
        sridEntidadJText = new TextField(5, true);
     
        
        this.add(sridEntidadJLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        this.add(sridEntidadJText, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        
        avisoJLabel = new JLabel();
        avisoJText = new TextField(3, true);

        this.add(avisoJLabel, new GridBagConstraints(0, 2, 1, 1, 0.0,
                    0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5,
                            5, 5), 0, 0) );
        this.add(avisoJText, new GridBagConstraints(1, 2, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5,
                        5, 5), 0, 0) );
        
        periodicidadJLabel = new JLabel();
        periodicidadJText = new TextField(3,true);

        this.add(periodicidadJLabel, new GridBagConstraints(0, 3, 1, 1, 0.0,
                    0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5,
                            5, 5), 0, 0) );
        this.add(periodicidadJText, new GridBagConstraints(1, 3, 1, 1, 0.0,
                0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5,
                        5, 5), 0, 0) );
        
        intentosJLabel = new JLabel();
        intentosJText = new TextField(2, true);


        this.add(intentosJLabel, new GridBagConstraints(0, 4, 1, 1, 0.0,
                    0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5,
                            5, 5), 0, 0) );
        this.add(intentosJText, new GridBagConstraints(1, 4, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5,
                        5, 5), 0, 0) );
        
        entidadExtJLabel = new JLabel();
        entidadExtJText = new TextField(30, true);
        
        this.add(entidadExtJLabel, new GridBagConstraints(0, 5, 1, 1, 0.0,
                    0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5,
                            5, 5), 0, 0) );
        this.add(entidadExtJText, new GridBagConstraints(1, 5, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5,
                        5, 5), 0, 0) );
        
        backupJCheckBox = new JCheckBox();
        backupJCheckBox.setSelected(true);
        this.add(backupJCheckBox, new GridBagConstraints(1, 6, 1, 1, 0.0,
                0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 5,
                        5, 5), 0, 0) );
            
        aniadirEntidadListaJButton = new JButton();
        this.add(aniadirEntidadListaJButton, new GridBagConstraints(0, 7, 2, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5,
                        5, 5), 0, 0) );
        
   }
    
    public void changeScreenLang(ResourceBundle messages) {
    	try{
    		this.setBorder(new TitledBorder(messages.getString("JPanelNuevaEntidad.nuevaEntidadPanel")));
    		nombreEntidadJLabel.setText(messages.getString("JPanelNuevaEntidad.nombreEntidadJLabel"));        
    		sridEntidadJLabel.setText(messages.getString("JPanelNuevaEntidad.sridEntidadJLabel"));
    		avisoJLabel.setText(messages.getString("JPanelNuevaEntidad.avisoJLabel"));
    		periodicidadJLabel.setText(messages.getString("JPanelNuevaEntidad.periodicidadJLabel"));
    		intentosJLabel.setText(messages.getString("JPanelNuevaEntidad.intentosJLabel"));
    		entidadExtJLabel.setText(messages.getString("JPanelNuevaEntidad.entidadExtJLabel"));    		
    		aniadirEntidadListaJButton.setText(messages.getString("JPanelNuevaEntidad.aniadirEntidadListaJButton"));
    		aniadirEntidadListaJButton.setToolTipText(messages.getString("JPanelNuevaEntidad.aniadirEntidadListaJButton"));
    		backupJCheckBox.setText(messages.getString("JPanelModificarEntidad.backupJCheckBox"));
    		backupJCheckBox.setToolTipText(messages.getString("JPanelModificarEntidad.backupJCheckBox"));
    	}catch (Exception ex){
    		logger.error("Error al poner los textos de la aplicacion",ex);
    	}

    }
    

    
    public void editable(boolean b){        
        this.setEnabled(b);
        nombreEntidadJLabel.setEnabled(b);
        nombreEntidadJText.setEnabled(b);
        aniadirEntidadListaJButton.setEnabled(b);
        backupJCheckBox.setEnabled(b);
        avisoJText.setEnabled(b);
        periodicidadJText.setEnabled(b);
        intentosJText.setEnabled(b);
        entidadExtJText.setEnabled(b);
//        cancelarAniadirEntidadJButton.setEnabled(b);
    }
    public void clean(){        
        nombreEntidadJText.setText("");
        sridEntidadJText.setText("");
        avisoJText.setText("");
        periodicidadJText.setText("");
        intentosJText.setText("");
        entidadExtJText.setText("");
    }
    
    public JButton getBotonAniadir() {
        return aniadirEntidadListaJButton;
    }
    
    public String getNombreEntidad() {
        return nombreEntidadJText.getText();
    }
    
    public String getSridEntidad() {
        return sridEntidadJText.getText();
    }
    
    public String getEntidadExt() {
        return entidadExtJText.getText();
    }
    
    public boolean isBackup(){
    	return backupJCheckBox.isSelected();
    }
    public int getAviso(){
    	if ((avisoJText.getText()==null) || (avisoJText.getText().trim().equals("")))
    		return 15;
    	return (new Integer(avisoJText.getText())).intValue();
    }
    public int getPeriodicidad(){
    	if ((periodicidadJText.getText()==null) || (periodicidadJText.getText().trim().equals("")))
    		return 365;
    	return (new Integer(periodicidadJText.getText())).intValue();
    }
    public int getIntentos(){
    	if ((intentosJText.getText()==null) || (intentosJText.getText().trim().equals("")))
    		return 5;
    	return (new Integer(intentosJText.getText())).intValue();
    }
}