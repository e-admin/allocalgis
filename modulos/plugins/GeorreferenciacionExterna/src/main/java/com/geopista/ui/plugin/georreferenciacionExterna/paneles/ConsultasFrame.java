/**
 * ConsultasFrame.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
