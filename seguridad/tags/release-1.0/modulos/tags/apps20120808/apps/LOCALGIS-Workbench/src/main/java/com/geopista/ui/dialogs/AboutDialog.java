/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
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
 * 
 * www.geopista.com
 * 
 * Created on 19-nov-2004 by juacas
 *
 * 
 */
package com.geopista.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.GEOPISTAWorkbench;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.JUMPWorkbenchImpl;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.ui.ExtensionsAboutPanel;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.SplashPanel;
/**
 * Displays an About Dialog (Splash Screen).
 */
public class AboutDialog extends JDialog {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(AboutDialog.class);

    BorderLayout borderLayout2 = new BorderLayout();
    Border border1;
    JPanel buttonPanel = new JPanel();
    JButton okButton = new JButton();
    private JTabbedPane jTabbedPane1 = new JTabbedPane();
    private JPanel infoPanel = new JPanel();
    private BorderLayout borderLayout3 = new BorderLayout();
    private JPanel jPanel1 = new JPanel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
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
        final String INSTANCE_KEY = AboutDialog.class.getName() + " - INSTANCE"; //$NON-NLS-1$
        if (context.getIWorkbench().getBlackboard().get(INSTANCE_KEY) == null) {
            AboutDialog aboutDialog = new AboutDialog(context.getIWorkbench().getGuiComponent());
            context.getIWorkbench().getBlackboard().put(INSTANCE_KEY, aboutDialog);
            GUIUtil.centreOnWindow(aboutDialog);
        }
        return (AboutDialog) context.getIWorkbench().getBlackboard().get(INSTANCE_KEY);
    }
    
    private ExtensionsAboutPanel extensionsAboutPanel = new ExtensionsAboutPanel();
	private JTextPane jTextPane = null;
	private JScrollPane jScrollPane = null;
// Cambiado por el interfaz general del Component raíz del framework. [Juan Pablo]
    private AboutDialog(WorkbenchGuiComponent frame) {
        super((JFrame)frame, AppContext.getApplicationContext().getI18nString("AboutDialog.AboutGeopista"), true); //$NON-NLS-1$
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
        java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        this.setResizable(false);
        okButton.setText(AppContext.getApplicationContext().getI18nString("AboutDialog.OK")); //$NON-NLS-1$
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButton_actionPerformed(e);
            }
        });
        infoPanel.setLayout(borderLayout3);
        jPanel1.setLayout(gridBagLayout1);
        logoPanel.setLayout(borderLayout1);
        jLabel9.setFont(new java.awt.Font("Dialog", 2, 12)); //$NON-NLS-1$
        jLabel9.setText(AppContext.getApplicationContext().getI18nString("AboutDialog.FreeMemory")); //$NON-NLS-1$
        jLabel10.setFont(new java.awt.Font("Dialog", 2, 12)); //$NON-NLS-1$
        jLabel10.setText(AppContext.getApplicationContext().getI18nString("AboutDialog.JavaVersion")); //$NON-NLS-1$
        jLabel11.setFont(new java.awt.Font("Dialog", 3, 12)); //$NON-NLS-1$
        jLabel11.setHorizontalAlignment(SwingConstants.LEFT);
        jLabel11.setText(AppContext.getApplicationContext().getI18nString("AboutDialog.SystemInfo")); //$NON-NLS-1$
        lblJavaVersion.setToolTipText(""); //$NON-NLS-1$
        lblJavaVersion.setText("x"); //$NON-NLS-1$
        jLabel12.setFont(new java.awt.Font("Dialog", 2, 12)); //$NON-NLS-1$
        jLabel12.setText(AppContext.getApplicationContext().getI18nString("AboutDialog.TotalMemory")); //$NON-NLS-1$
        lblFreeMemory.setToolTipText(""); //$NON-NLS-1$
        lblFreeMemory.setText("x"); //$NON-NLS-1$
        lblTotalMemory.setText("x"); //$NON-NLS-1$
        jLabel13.setFont(new java.awt.Font("Dialog", 2, 12)); //$NON-NLS-1$
        jLabel13.setText(AppContext.getApplicationContext().getI18nString("AboutDialog.OS")); //$NON-NLS-1$
        lblOSVersion.setText("x"); //$NON-NLS-1$
        jLabel14.setFont(new java.awt.Font("Dialog", 2, 12)); //$NON-NLS-1$
        jLabel14.setText(AppContext.getApplicationContext().getI18nString("AboutDialog.CommittedMemory")); //$NON-NLS-1$
        lblCommittedMemory.setText("x"); //$NON-NLS-1$
        btnGC.setText(AppContext.getApplicationContext().getI18nString("AboutDialog.GarbageCollect")); //$NON-NLS-1$
        btnGC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnGC_actionPerformed(e);
            }
        });
        jTabbedPane1.add(logoPanel, AppContext.getApplicationContext().getI18nString("AboutDialog.About")); //$NON-NLS-1$
        logoPanel.add(splashPanel, BorderLayout.CENTER);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(okButton, null);
        jTabbedPane1.setBounds(0, 0, 0, 0);
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 8;
        gridBagConstraints2.weightx = 1.0;
        gridBagConstraints2.weighty = 1.0;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints2.gridwidth = 3;
        gridBagConstraints2.insets = new java.awt.Insets(10,10,10,10);
        jTabbedPane1.addTab(AppContext.getApplicationContext().getI18nString("AboutDialog.Info"), infoPanel); //$NON-NLS-1$
        jTabbedPane1.addTab(StringUtil.toFriendlyName(extensionsAboutPanel.getClass().getName(), AppContext.getApplicationContext().getI18nString("AboutDialog.AboutPanel")), extensionsAboutPanel); //$NON-NLS-1$
        infoPanel.add(jPanel1, BorderLayout.CENTER);
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
        jPanel1.add(getJScrollPane(), gridBagConstraints2);
    }

    public void setVisible(boolean b) {
        if (b) {
            DecimalFormat format = new DecimalFormat(AppContext.getApplicationContext().getI18nString("AboutDialog.MemoryNumberFormat")); //$NON-NLS-1$
            lblJavaVersion.setText(System.getProperty("java.version")); //$NON-NLS-1$
            lblOSVersion.setText(
                System.getProperty("os.name") //$NON-NLS-1$
                    + " (" //$NON-NLS-1$
                    + System.getProperty("os.version") //$NON-NLS-1$
                    + ")"); //$NON-NLS-1$

            long totalMem = Runtime.getRuntime().totalMemory();
            long freeMem = Runtime.getRuntime().freeMemory();
            lblTotalMemory.setText(format.format(totalMem) + AppContext.getApplicationContext().getI18nString("AboutDialog.bytes")); //$NON-NLS-1$
            lblCommittedMemory.setText(format.format(totalMem - freeMem) + AppContext.getApplicationContext().getI18nString("AboutDialog.bytes")); //$NON-NLS-1$
            lblFreeMemory.setText(format.format(freeMem) + AppContext.getApplicationContext().getI18nString("AboutDialog.bytes")); //$NON-NLS-1$
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
	/**
	 * This method initializes jTextPane	
	 * 	
	 * @return javax.swing.JTextPane	
	 */    
	private JTextPane getJTextPane() {
		if (jTextPane == null) {
			jTextPane = new JTextPane();
			jTextPane.setBackground(java.awt.SystemColor.control);
			jTextPane.setContentType("text/html"); //$NON-NLS-1$
			jTextPane.setEditable(false);
			jTextPane.setText("<html><head>Credits</head><body>Desarrollado por: <p>"+AppContext.getApplicationContext().getI18nString("AboutDialog.desarrollado")+"</p><p>Contiene código de: JUMP by Vividsolutions and Net Refractions.</p></body></html>"); //$NON-NLS-1$
			jTextPane.setPreferredSize(new java.awt.Dimension(383,200));
			/*try
			{
			jTextPane.setPage(AboutDialog.class.getResource(AppContext.getApplicationContext().getI18nString("credits.html"))); //$NON-NLS-1$
			} catch (IOException e)
			{
				logger.error(
						"getJTextPane() - Falta el documento de Créditos.", e); //$NON-NLS-1$
			}*/
		}
		return jTextPane;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTextPane());
			jScrollPane.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.SoftBevelBorder.LOWERED));
		}
		return jScrollPane;
	}
  }