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
public class EntidadDialog extends JDialog {
    
    
    public EntidadDialog(Frame f,String[] entidades) {
        super(f);
    	initComponents();
        initActions();
        initModel(entidades);
    }
    
                        
    private void initModel(String[] entidades) {
		
    	entidadJComboBox.setModel(new DefaultComboBoxModel(entidades));
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

		entidadJLabel = new JLabel();
        entidadJComboBox = new JComboBox();
        okJButton = new JButton();
        cancelJButton = new JButton();

        getContentPane().setLayout(null);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        entidadJLabel.setText("Seleccione la entidad");
        getContentPane().add(entidadJLabel);
        entidadJLabel.setBounds(20, 50, 140, 20);

        entidadJComboBox.setModel(new DefaultComboBoxModel(new String[] { "Art\u00edculo 1", "Art\u00edculo 2", "Art\u00edculo 3", "Art\u00edculo 4" }));
        getContentPane().add(entidadJComboBox);
        entidadJComboBox.setBounds(160, 50, 180, 22);

        okJButton.setText("Aceptar");
        getContentPane().add(okJButton);
        okJButton.setBounds(90, 120, 80, 23);

        cancelJButton.setText("Cancelar");
        getContentPane().add(cancelJButton);
        cancelJButton.setBounds(180, 120, 80, 23);
        pack();
        setTitle("Selección de la entidad");
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

	public String getEntidadSelected() {
		return (String) entidadJComboBox.getSelectedItem();
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
    private JComboBox entidadJComboBox;
    private JLabel entidadJLabel;
    private JButton okJButton;


    
}
