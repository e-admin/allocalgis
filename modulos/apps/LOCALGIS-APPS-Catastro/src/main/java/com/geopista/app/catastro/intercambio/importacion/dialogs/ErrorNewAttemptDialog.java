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

package com.geopista.app.catastro.intercambio.importacion.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrame;

/**
 * UI for Displaying an Error Dialog.  
 * @see WorkbenchFrame#warnUser warnUser 
 */


public class ErrorNewAttemptDialog extends JOptionPane {

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
    private static final String SHOW_DETAILS = aplicacion.getI18nString("ErrorNewAttemptDialog.showDetails");
    private static final String HIDE_DETAILS = aplicacion.getI18nString("ErrorNewAttemptDialog.hideDetails");
    private static final String BUGZILLA_DETAILS = aplicacion.getI18nString("ErrorNewAttemptDialog.errorSend");
    private static final String NEW_ATTEMPT = aplicacion.getI18nString("ErrorNewAttemptDialog.newAttempt");
    private static final String CANCEL = aplicacion.getI18nString("ErrorNewAttemptDialog.Cancel");
    private static final String OMIT = aplicacion.getI18nString("ErrorNewAttemptDialog.omit");

    private boolean new_attempt = false;
    //The actual width will be MIN_DIALOG_WIDTH or the width as determined by
    //the error message (after applying StringUtil#split to it), whichever is
    //larger. [Jon Aquino]
    private final static int MIN_DIALOG_WIDTH = 500;
    private String details;

    public ErrorNewAttemptDialog() {
        super("Message", ERROR_MESSAGE, DEFAULT_OPTION);
    }
        
    private void addDetailPanel(final JDialog dialog, final JButton detailButton) {
        final JPanel panel = new JPanel(new GridBagLayout());
        final JScrollPane scrollPane = new JScrollPane();
        final JTextArea textArea = new JTextArea();
        textArea.setOpaque(false);
        scrollPane.setOpaque(false);
        textArea.setFont(new Font("Monospaced", 0, 12));
        textArea.setEditable(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().add(textArea);
        panel.add(scrollPane,
            new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        textArea.setText(details);
        detailButton.addActionListener(new ActionListener() {
                private boolean showingDetails = false;

                public void actionPerformed(ActionEvent e) {
                    if (showingDetails) {
                        dialog.remove(panel);
                        detailButton.setText(SHOW_DETAILS);
                    } else {
                        scrollPane.setPreferredSize(new Dimension(
                                dialog.getWidth(), 200));
                        dialog.getContentPane().add(panel, BorderLayout.SOUTH);
                        detailButton.setText(HIDE_DETAILS);
                    }

                    showingDetails = !showingDetails;
                    dialog.pack();
                }
            });
    }

    private void addErrorAction(final JButton errorButton) {
        errorButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                	Clipboard clipboard=Toolkit.getDefaultToolkit().getSystemClipboard();
                	clipboard.setContents(new StringSelection(details),null);
                    GeopistaBrowser.openURL(aplicacion.getString("ErrorDialog.BugzillaWeb"));
                }
            });
    }

    private void setDetails(String details) {
        this.details = details;
    }
    
    private void setNewAttempt() {
    	
    	aplicacion.getBlackboard().put("newattempt", new Boolean(true));
    	
    }
    
    private void setCancel() {

    	aplicacion.getBlackboard().put("cancel", new Boolean(true));

    }

    public JDialog createDialog(Component parentComponent, String title) {
        JButton newAttemptButton = new JButton(NEW_ATTEMPT);
        JButton cancelButton = new JButton(CANCEL);
        JButton omitButton = new JButton(OMIT);
        JButton detailButton = new JButton(SHOW_DETAILS);
        JButton errorButton = new JButton(BUGZILLA_DETAILS);
        setOptions(new Object[] { newAttemptButton, cancelButton, omitButton, detailButton, errorButton });

        final JDialog dialog = super.createDialog(parentComponent, title);
        addDetailPanel(dialog, detailButton);
        addErrorAction(errorButton);
        omitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialog.setVisible(false);
                }
            });

        cancelButton.addActionListener(new ActionListener() {
            
			public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                setCancel();
                
            }
        });
        
        newAttemptButton.addActionListener(new ActionListener() {

        	public void actionPerformed(ActionEvent e) {
        		dialog.setVisible(false);
        		setNewAttempt();
        	}
        });

        JPanel horizontalStrut = new JPanel();
        horizontalStrut.setPreferredSize(new Dimension(MIN_DIALOG_WIDTH, 0));
        horizontalStrut.setMinimumSize(horizontalStrut.getPreferredSize());
        horizontalStrut.setMaximumSize(horizontalStrut.getPreferredSize());
        dialog.getContentPane().add(horizontalStrut, BorderLayout.NORTH);
        dialog.pack();

        if (parentComponent != null) {
            //For testing. [Jon Aquino]
            GUIUtil.centreOnWindow(dialog);
        }

        //The details panel doesn't properly resize, so just make the dialog
        //unresizable. [Jon Aquino]
        dialog.setResizable(false);

        return dialog;
    }

    public static void show(Component parentComponent, String title,
        String message, String details) {
        ErrorNewAttemptDialog dialog = new ErrorNewAttemptDialog();
        dialog.setMessage(StringUtil.split(message, 80));
        dialog.setDetails(details);
        dialog.createDialog(parentComponent, title).setVisible(true);
    }

}
