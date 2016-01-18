
/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

package com.vividsolutions.jump.workbench.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.app.administrador.init.Constantes;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.JUMPWorkbenchImpl;
import com.vividsolutions.jump.workbench.WorkbenchContext;

/**
 * Displays an About Dialog (Splash Screen).
 */
//<<TODO:AESTHETICS>> Modify the image so that the green and red pieces have
//a smooth sinusoidal interface. [Jon Aquino]
//<<TODO:AESTHETICS>> The lettering on the image is a bit blocky. Fix. [Jon Aquino]
public class AboutDialog extends JDialog {
    BorderLayout borderLayout2 = new BorderLayout();
    Border border1;
    JPanel buttonPanel = new JPanel();
    JButton okButton = new JButton();
    private JTabbedPane jTabbedPane1 = new JTabbedPane();
    private JPanel infoPanel = new JPanel();
    private BorderLayout borderLayout3 = new BorderLayout();
    private JPanel jPanel1 = new JPanel();
    private JLabel jLabel1 = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JLabel jLabel3 = new JLabel();
    private JLabel jLabel4 = new JLabel();
    private JLabel jLabel5 = new JLabel();
    private JLabel jLabel6 = new JLabel();
    private JLabel jLabel7 = new JLabel();
    private JLabel jLabel8 = new JLabel();
    private JPanel logoPanel = new JPanel();
    private BorderLayout borderLayout1 = new BorderLayout();
    private JLabel jLabel9 = new JLabel();
    private JLabel jLabel10 = new JLabel();
    private JLabel jLabel11 = new JLabel();
    private JLabel lblJavaVersion = new JLabel();
    private JLabel jLabel12 = new JLabel();
    private JLabel lblFreeMemory = new JLabel();
    private JLabel lblTotalMemory = new JLabel();
    private JLabel jLabel13 = new JLabel();
    private JLabel lblOSVersion = new JLabel();
    private JLabel jLabel14 = new JLabel();
    private JLabel lblCommittedMemory = new JLabel();
    private JPanel pnlButtons = new JPanel();
    private JButton btnGC = new JButton();
    private SplashPanel splashPanel;

    public static AboutDialog instance(WorkbenchContext context) {
        final String INSTANCE_KEY = AboutDialog.class.getName() + " - INSTANCE";
        if (context.getIWorkbench().getBlackboard().get(INSTANCE_KEY) == null) {
            AboutDialog aboutDialog = new AboutDialog(context.getIWorkbench().getGuiComponent());
            context.getIWorkbench().getBlackboard().put(INSTANCE_KEY, aboutDialog);
            GUIUtil.centreOnWindow(aboutDialog);
        }
        return (AboutDialog) context.getIWorkbench().getBlackboard().get(INSTANCE_KEY);
    }
    
    private ExtensionsAboutPanel extensionsAboutPanel = new ExtensionsAboutPanel();
// Cambiado por el interfaz general del Component raíz del framework. [Juan Pablo]
    private AboutDialog(WorkbenchGuiComponent frame) {
        super((JFrame)frame, "About JUMP", true);
        extensionsAboutPanel.setPlugInManager(frame.getContext().getIWorkbench().getPlugInManager());
        this.splashPanel =
            new SplashPanel(JUMPWorkbenchImpl.splashImage(), Constantes.VERSION_TEXT);
        try {
            jbInit(); 
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        border1 = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        this.getContentPane().setLayout(borderLayout2);
        this.setResizable(false);
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButton_actionPerformed(e);
            }
        });
        infoPanel.setLayout(borderLayout3);
        jLabel1.setToolTipText("");
        jLabel1.setText("Martin Davis");
        jLabel2.setFont(new java.awt.Font("Dialog", 3, 12));
        jLabel2.setToolTipText("");
        jLabel2.setText("Development Team");
        jPanel1.setLayout(gridBagLayout1);
        jLabel3.setText("Jon Aquino");
        jLabel4.setText("Alan Chang");
        jLabel5.setForeground(Color.blue);
        jLabel5.setText("Vivid Solutions Inc.");
        jLabel6.setToolTipText("");
        jLabel6.setText("David Blasby");
        jLabel7.setText("Paul Ramsey");
        jLabel8.setForeground(new Color(0, 77, 31));
        jLabel8.setToolTipText("");
        jLabel8.setText("Refractions Research Inc.");
        logoPanel.setLayout(borderLayout1);
        jLabel9.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel9.setText("Free Memory");
        jLabel10.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel10.setText("Java Version");
        jLabel11.setFont(new java.awt.Font("Dialog", 3, 12));
        jLabel11.setHorizontalAlignment(SwingConstants.LEFT);
        jLabel11.setText("System Info");
        lblJavaVersion.setToolTipText("");
        lblJavaVersion.setText("x");
        jLabel12.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel12.setText("Total Memory");
        lblFreeMemory.setToolTipText("");
        lblFreeMemory.setText("x");
        lblTotalMemory.setText("x");
        jLabel13.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel13.setText("OS");
        lblOSVersion.setText("x");
        jLabel14.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel14.setText("Committed Memory");
        lblCommittedMemory.setText("x");
        btnGC.setText("Garbage Collect");
        btnGC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnGC_actionPerformed(e);
            }
        });
        jTabbedPane1.add(logoPanel, "About");
        logoPanel.add(splashPanel, BorderLayout.CENTER);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(okButton, null);
        jTabbedPane1.setBounds(0, 0, 0, 0);
        jTabbedPane1.addTab("Info", infoPanel);
        jTabbedPane1.addTab(StringUtil.toFriendlyName(extensionsAboutPanel.getClass().getName(), "AboutPanel"), extensionsAboutPanel);
        infoPanel.add(jPanel1, BorderLayout.CENTER);
        jPanel1.add(
            jLabel2,
            new GridBagConstraints(
                0,
                6,
                2,
                1,
                0.0,
                0.0,
                GridBagConstraints.EAST,
                GridBagConstraints.NONE,
                new Insets(20, 0, 0, 20),
                0,
                0));
        jPanel1.add(
            jLabel1,
            new GridBagConstraints(
                2,
                6,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.SOUTHWEST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        jPanel1.add(
            jLabel3,
            new GridBagConstraints(
                2,
                7,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        jPanel1.add(
            jLabel4,
            new GridBagConstraints(
                2,
                8,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        jPanel1.add(
            jLabel5,
            new GridBagConstraints(
                3,
                6,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.SOUTHWEST,
                GridBagConstraints.NONE,
                new Insets(0, 10, 0, 0),
                0,
                0));
        jPanel1.add(
            jLabel6,
            new GridBagConstraints(
                2,
                9,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                new Insets(10, 0, 0, 0),
                0,
                0));
        jPanel1.add(
            jLabel7,
            new GridBagConstraints(
                2,
                10,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        jPanel1.add(
            jLabel8,
            new GridBagConstraints(
                3,
                9,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.NONE,
                new Insets(10, 10, 0, 0),
                0,
                0));
        jPanel1.add(
            jLabel10,
            new GridBagConstraints(
                2,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        jPanel1.add(
            jLabel11,
            new GridBagConstraints(
                0,
                0,
                2,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        jPanel1.add(
            lblJavaVersion,
            new GridBagConstraints(
                3,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.EAST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        this.getContentPane().add(jTabbedPane1, BorderLayout.NORTH);
        jPanel1.add(
            jLabel13,
            new GridBagConstraints(
                2,
                1,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        jPanel1.add(
            lblOSVersion,
            new GridBagConstraints(
                3,
                1,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.EAST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        jPanel1.add(
            jLabel9,
            new GridBagConstraints(
                2,
                4,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        jPanel1.add(
            jLabel12,
            new GridBagConstraints(
                2,
                2,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        jPanel1.add(
            lblFreeMemory,
            new GridBagConstraints(
                3,
                4,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.EAST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        jPanel1.add(
            lblTotalMemory,
            new GridBagConstraints(
                3,
                2,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.EAST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        jPanel1.add(
            jLabel14,
            new GridBagConstraints(
                2,
                3,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        jPanel1.add(
            lblCommittedMemory,
            new GridBagConstraints(
                3,
                3,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.EAST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        jPanel1.add(
            pnlButtons,
            new GridBagConstraints(
                2,
                5,
                2,
                1,
                0.0,
                0.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        pnlButtons.add(btnGC, null);
    }

    public void setVisible(boolean b) {
        if (b) {
            DecimalFormat format = new DecimalFormat("###,###");
            lblJavaVersion.setText(System.getProperty("java.version"));
            lblOSVersion.setText(
                System.getProperty("os.name")
                    + " ("
                    + System.getProperty("os.version")
                    + ")");

            long totalMem = Runtime.getRuntime().totalMemory();
            long freeMem = Runtime.getRuntime().freeMemory();
            lblTotalMemory.setText(format.format(totalMem) + " bytes");
            lblCommittedMemory.setText(format.format(totalMem - freeMem) + " bytes");
            lblFreeMemory.setText(format.format(freeMem) + " bytes");
        }

        super.setVisible(b);
    }

    void okButton_actionPerformed(ActionEvent e) {
        setVisible(false);
    }

    void btnGC_actionPerformed(ActionEvent e) {
        Runtime.getRuntime().gc();
        setVisible(true);
    }
}
