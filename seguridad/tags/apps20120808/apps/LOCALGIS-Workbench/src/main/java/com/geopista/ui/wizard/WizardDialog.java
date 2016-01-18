/* WIZARD DIALOG
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

package com.geopista.ui.wizard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class WizardDialog extends JDialog implements WizardContext,
    InputChangedListener {
    private JPanel panelPrincipal = new JPanel();
    private BorderLayout borderLayout1 = new BorderLayout();
    private ArrayList completedWizardPanels;
    private JPanel buttonPanel = new JPanel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JButton cancelButton = new JButton();
    private JButton nextButton = new JButton();
    private JButton backButton = new JButton();
    private JPanel fillerPanel = new JPanel();
    private Border border2;
    private Border border3;
    private JPanel outerCenterPanel = new JPanel();
    private GridBagLayout gridBagLayout2 = new GridBagLayout();
    private JPanel centerPanel = new JPanel();
    private Border border4;
    private JLabel titleLabel = new JLabel();
    private CardLayout cardLayout1 = new CardLayout();
    private WizardPanel currentWizardPanel;
    private List allWizardPanels;
    private ErrorHandler errorHandler;
    private JTextArea instructionTextArea = new JTextArea();
    private boolean finishPressed = false;
    private HashMap dataMap = new HashMap();
    private Border border5;
    private Border border6;
    private Border border7;
    private ApplicationContext aplicacion =  AppContext.getApplicationContext();
    private boolean canceled = false;
    
    public WizardDialog(Frame frame, String title, ErrorHandler errorHandler) {
        super(frame, title, true);
        canceled=false;
        this.errorHandler = errorHandler;

        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    cancel();
                }
            });
    }

 public static void main(String[] args)
  {
    /*Frame hola = new Frame();
    WizardDialog d = new WizardDialog(hola,
                "TEST dialog", null);
   
        d.init(new WizardPanel[] {
        		new printPanel01(),
                new printPanel02(),
                new GeopistaPrintJPanel03(),
                new GeopistaVistaPreliminarPanel(null)}
        );
                d.setSize(750,600);
               d.setVisible(true);*/
}
  
    private void checkIDs(Collection wizardPanels) {
        ArrayList ids = new ArrayList();

        for (Iterator i = wizardPanels.iterator(); i.hasNext();) {
            WizardPanel panel = (WizardPanel) i.next();
            ids.add(panel.getID());
        }

        for (Iterator i = wizardPanels.iterator(); i.hasNext();) {
            WizardPanel panel = (WizardPanel) i.next();

            if (panel.getNextID() == null) {
                continue;
            }

            Assert.isTrue(ids.contains(panel.getNextID()),
                "Required panel missing: " + panel.getNextID());
        }
    }

    private void setCurrentWizardPanel(WizardPanel wizardPanel) {
        if (currentWizardPanel != null) {
            currentWizardPanel.remove(this);
        }

        titleLabel.setText(wizardPanel.getTitle());
        cardLayout1.show(centerPanel, wizardPanel.getID());
        currentWizardPanel = wizardPanel;
        updateButtons();
        currentWizardPanel.add(this);
        instructionTextArea.setText(currentWizardPanel.getInstructions());
    }

    private WizardPanel getCurrentWizardPanel() {
        return currentWizardPanel;
    }

    private void updateButtons() {
        backButton.setEnabled(!completedWizardPanels.isEmpty());
        nextButton.setEnabled(getCurrentWizardPanel().isInputValid());
        nextButton.setText((getCurrentWizardPanel().getNextID() == null)
            ? aplicacion.getI18nString("WizardDialog.Finalizar") : aplicacion.getI18nString("WizardDialog.Siguiente >"));
    }

    public void inputChanged() {
        updateButtons();
    }

    /**
     * @param wizardPanels the first of which will be the first WizardPanel that is displayed
     */
    public void init(WizardPanel[] wizardPanels) {
        allWizardPanels = Arrays.asList(wizardPanels);
        checkIDs(allWizardPanels);

        for (int i = 0; i < wizardPanels.length; i++) {
            centerPanel.add((Component) wizardPanels[i], wizardPanels[i].getID());
            wizardPanels[i].setWizardContext(this);
        }

        completedWizardPanels = new ArrayList();

        //Se ha cambiado el orden de estas dos lineas por al hacer antes el enteredFromLeft
        //que la asignacion setCurrenWizardPanel, si se intenta inicializar un elemento del Panel
        //lanza una excepcion al intentar acceder al currentWizardPanel [Juan A. Lopez]
        setCurrentWizardPanel(wizardPanels[0]);        
        wizardPanels[0].enteredFromLeft(dataMap);

        pack();
    }

    private void jbInit() throws Exception {
      border7 = BorderFactory.createEmptyBorder(5, 5, 20, 10); //PRIMERO, EL BLANCO
      border5 = BorderFactory.createLineBorder(Color.black, 2);
      border6 = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(
                    Color.white, new Color(148, 145, 140)),
                BorderFactory.createEmptyBorder(0, 0, 0, 0));
      border2 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
      border3 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
     border4 = BorderFactory.createBevelBorder(BevelBorder.LOWERED,
              Color.white, Color.white, new Color(103, 101, 98),
              new Color(148, 145, 140));
              
      instructionTextArea.setEnabled(false);
      instructionTextArea.setFont(new JLabel().getFont());
      instructionTextArea.setOpaque(false);
   //   instructionTextArea.setToolTipText("Zona de las instrucciones.");
      instructionTextArea.setDisabledTextColor(Color.black);
      instructionTextArea.setEditable(false);
      instructionTextArea.setLineWrap(true);
      instructionTextArea.setWrapStyleWord(true);
      instructionTextArea.setText("instructionTextArea");
      instructionTextArea.setBorder(border7);
      
    this.getContentPane().setLayout(new BorderLayout());
      centerPanel.setLayout(cardLayout1);
      panelPrincipal.setLayout(borderLayout1);
      buttonPanel.setLayout(gridBagLayout1);
        cancelButton.setText(aplicacion.getI18nString("WizardDialog.Cancelar"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancelButton_actionPerformed(e);
                }
            });
        nextButton.setText(aplicacion.getI18nString("WizardDialog.Siguiente >"));
        nextButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    nextButton_actionPerformed(e);
                }
            });
        backButton.setText(aplicacion.getI18nString("WizardDialog.< Anterior"));
        backButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    backButton_actionPerformed(e);
                }
            });
        buttonPanel.setBorder(border3);
        outerCenterPanel.setLayout(gridBagLayout2);
       titleLabel.setBackground(Color.white); //PRIMERO, LO BLANCO
       titleLabel.setFont(new java.awt.Font("Dialog", 1, 12));
       titleLabel.setBorder(border7);
       titleLabel.setOpaque(true);
       titleLabel.setText("TITULO");
       titleLabel.setIcon(com.geopista.ui.images.IconLoader.icon(aplicacion.getString("WizardDialog.Icon")));
        outerCenterPanel.setBorder(border6);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
       getContentPane().add(panelPrincipal,BorderLayout.CENTER);
        panelPrincipal.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(cancelButton,
            new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 20, 0, 0), 0, 0));
        buttonPanel.add(nextButton,
            new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        buttonPanel.add(backButton,
            new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        buttonPanel.add(fillerPanel,
            new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        panelPrincipal.add(outerCenterPanel, BorderLayout.CENTER);
    outerCenterPanel.add(centerPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 36));
//    outerCenterPanel.add(instructionTextArea,
//            new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
//                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
//                new Insets(0, 0, 20, 0), 0, 0));   
    this.getContentPane().add(instructionTextArea,BorderLayout.WEST);
    this.getContentPane().add(titleLabel, BorderLayout.NORTH);
    }

    void cancelButton_actionPerformed(ActionEvent e) {
        cancel();
    }

    private void cancel() {
        setVisible(false);
        if(getCurrentWizardPanel()!=null)
            getCurrentWizardPanel().exiting();
    }

    void nextButton_actionPerformed(ActionEvent e) {
        try {
            getCurrentWizardPanel().exitingToRight();

            if (getCurrentWizardPanel().getNextID() == null) {
                finishPressed = true;
                if(getCurrentWizardPanel()!=null)
                    getCurrentWizardPanel().exiting();
                setVisible(false);

                return;
            }
             if (getCurrentWizardPanel().getNextID().equals("")) {
                return;
            }

            completedWizardPanels.add(getCurrentWizardPanel());

            String nextId=getCurrentWizardPanel().getNextID();
            //System.out.println("El siguiente id es: "+nextId);
            WizardPanel nextWizardPanel = find(nextId);
            nextWizardPanel.enteredFromLeft(dataMap);
            setCurrentWizardPanel(nextWizardPanel);
        } catch (Throwable x) {
        	x.printStackTrace();
            errorHandler.handleThrowable(x);
        }
    }

    private WizardPanel find(String id) {
        for (Iterator i = allWizardPanels.iterator(); i.hasNext();) {
            WizardPanel wizardPanel = (WizardPanel) i.next();

            if (wizardPanel.getID().equals(id)) {
                return wizardPanel;
            }
        }

        Assert.shouldNeverReachHere();

        return null;
    }

    public boolean wasFinishPressed() {
        return finishPressed;
    }

    void backButton_actionPerformed(ActionEvent e) {
        WizardPanel prevPanel = (WizardPanel) completedWizardPanels.remove(completedWizardPanels.size() -
                1);
        setCurrentWizardPanel(prevPanel);

        //Don't init panel if we're going back. [Jon Aquino]
    }

    public void setData(String name, Object value) {
        dataMap.put(name, value);
    }

    public Object getData(String name) {
        return dataMap.get(name);
    }
    
    public void cancelWizard()
    {
        canceled = true;
        if(getCurrentWizardPanel()!=null)
            getCurrentWizardPanel().exiting();
        cancel();
    }
    
    /**
     * @return Returns the canceled.
     */
    public boolean isCanceled()
    {
        return canceled;
    }
    /**
     * @param canceled The canceled to set.
     */
    public void setCanceled(boolean canceled)
    {
        this.canceled = canceled;
    }
    
    public void previousEnabled(boolean status)
    {
        backButton.setEnabled(status);
    }    

	public JTextArea getInstructionTextArea() {
		return instructionTextArea;
	}

	public void setInstructionTextArea(JTextArea instructionTextArea) {
		this.instructionTextArea = instructionTextArea;
	}
}
