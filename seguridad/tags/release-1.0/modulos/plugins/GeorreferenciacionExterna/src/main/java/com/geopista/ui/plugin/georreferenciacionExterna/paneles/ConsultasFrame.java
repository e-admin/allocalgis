package com.geopista.ui.plugin.georreferenciacionExterna.paneles;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class ConsultasFrame extends javax.swing.JInternalFrame{
	
	Logger logger = Logger.getLogger(ConsultasFrame.class);
	 private JFrame framePadre;
	
	 private javax.swing.JPanel templateJPanel;
	
	public ConsultasFrame(JFrame framePadre){
		 this.framePadre = framePadre;
		 initComponents();
		
	}
	
	 private void initComponents() {//GEN-BEGIN:initComponents
		templateJPanel = new JPanel();
		
		templateJPanel.setLayout(new GridBagLayout());
		templateJPanel.setSize(new Dimension(600, 550));
		templateJPanel.setPreferredSize(new Dimension(600, 550));
		 
		 
	 }
}
