/**
 * OKCancelDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * Hosts a custom component in a dialog with OK and Cancel buttons. Also
 * validates the input if OK is pressed.
 */
public class OKCancelDialog extends JDialog
{

  /**
   *
   * @param owner
   * @param title
   * @param modal
   * @param customComponent
   * @param validator the {@link Validator} to use, or <code>null</code> if none required
   * @throws HeadlessException
   */
    public OKCancelDialog(Dialog owner, String title, boolean modal,
            Component customComponent, Validator validator)
            throws HeadlessException {
        super(owner, title, modal);
        initialize(customComponent, validator);
    }

    public OKCancelDialog(Frame owner, String title, boolean modal,
            Component customComponent, Validator validator)
            throws HeadlessException {
        super(owner, title, modal);
        initialize(customComponent, validator);
    }

    private OKCancelPanel okCancelPanel = new OKCancelPanel();
    private Component customComponent;

    private void initialize(final Component customComponent,
            final Validator validator) {
        getRootPane().setDefaultButton(okCancelPanel.getButton("OK"));
        this.customComponent = customComponent;
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(customComponent, BorderLayout.CENTER);
        okCancelPanel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (okCancelPanel.wasOKPressed() && validator != null) {
                    String errorMessage = validator
                            .validateInput(customComponent);
                    if (errorMessage != null) {
                        JOptionPane.showMessageDialog(OKCancelDialog.this,
                                errorMessage, getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                setVisible(false);
            }
        });

        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    okCancelPanel.setOKPressed(false);
                }
            }
        );

        getContentPane().add(okCancelPanel, BorderLayout.SOUTH);
        pack();
        // Don't centre dialog until its size has been determined
        // i.e. after calling #pack [Jon Aquino 2005-03-09]
        GUIUtil.centreOnWindow(this);
    }

    public static interface Validator {
        /**
         * @return an error message, or null if the input is valid
         */
        public String validateInput(Component component);
    }

    public boolean wasOKPressed() {
        return okCancelPanel.wasOKPressed();
    }
    public Component getCustomComponent() {
        return customComponent;
    }
}