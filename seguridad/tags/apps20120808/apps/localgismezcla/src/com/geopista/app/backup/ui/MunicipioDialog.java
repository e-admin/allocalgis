/*
 * MunicipioDialog.java
 *
 * Created on 2 de noviembre de 2007, 10:13
 */

package com.geopista.app.backup.ui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

/**
 *
 * @author  arubio
 */
public class MunicipioDialog extends JDialog {
    
    
    public MunicipioDialog(Frame f,String[] municipios) {
        super(f);
    	initComponents();
        initActions();
        initModel(municipios);
    }
    
                        
    private void initModel(String[] municipios) {
		
    	municipioJComboBox.setModel(new DefaultComboBoxModel(municipios));
	}


	private void initActions() {
    	
		cancelJButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				cancel();
			}
			
		});
    
		okJButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ok();				
			}
			
		});
		
	}

	private void initComponents() {

        municipioJLabel = new JLabel();
        municipioJComboBox = new JComboBox();
        okJButton = new JButton();
        cancelJButton = new JButton();

        getContentPane().setLayout(null);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        municipioJLabel.setText("Seleccione el Municipio");
        getContentPane().add(municipioJLabel);
        municipioJLabel.setBounds(20, 50, 140, 20);

        municipioJComboBox.setModel(new DefaultComboBoxModel(new String[] { "Art\u00edculo 1", "Art\u00edculo 2", "Art\u00edculo 3", "Art\u00edculo 4" }));
        getContentPane().add(municipioJComboBox);
        municipioJComboBox.setBounds(160, 50, 180, 22);

        okJButton.setText("Aceptar");
        getContentPane().add(okJButton);
        okJButton.setBounds(90, 120, 80, 23);

        cancelJButton.setText("Cancelar");
        getContentPane().add(cancelJButton);
        cancelJButton.setBounds(180, 120, 80, 23);
        pack();
        setTitle("Selección del municipio");
        setSize(370,200);
        setResizable(false);
        setModal(true);
    }                    
    
	private void cancel() {
		int option = JOptionPane.showConfirmDialog(this, "¿Desea Cancelar la operación de Backup?");
		if (option == JOptionPane.OK_OPTION) {
			cancelled = true;
			dispose();
		}
	}


	private void ok() {
		JFileChooser fileChooser = new JFileChooser();
		//JFileChooser fileChooser = new JFileChooser(new File("/"));
		fileChooser.setDialogTitle("Seleccione el directorio donde se guardará el Backup");

		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fileChooser.showDialog(this,"Aceptar");
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			directorySelected = fileChooser.getSelectedFile();
			int option = JOptionPane.showConfirmDialog(this, "Desea realizar la operacion de backup");
			if (option == JOptionPane.OK_OPTION) {
				ok = true;
				dispose();
			}
		}
	}

	public String getMunicipioSelected() {
		return (String) municipioJComboBox.getSelectedItem();
	}

	public boolean isOk() {
		return ok;
	}
	public boolean isCancelled() {
		return cancelled;
	}
           
	public File getDirectorySelected() {
		return directorySelected;
	}
	
	
	private boolean cancelled;
	private boolean ok;
	private File directorySelected;
	
    private JButton cancelJButton;
    private JComboBox municipioJComboBox;
    private JLabel municipioJLabel;
    private JButton okJButton;


    
}
