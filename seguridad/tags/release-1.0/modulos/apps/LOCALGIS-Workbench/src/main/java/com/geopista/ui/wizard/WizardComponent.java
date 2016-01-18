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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
/**
 * Panel que se comporta como un Wizard.
 * Es conveniente utilizarse en un contenedor con CardLayout o similar ya
 * que cuando se finaliza el wizard simplemente se oculta el componente.
 * 
 * @see WizardDialog
 * @author juacas
 *
 */
public class WizardComponent extends JPanel implements WizardContext,
InputChangedListener {

	protected ArrayList completedWizardPanels;
	protected JPanel buttonPanel = new JPanel();
	protected GridBagLayout gridBagLayout1 = new GridBagLayout();
	protected JButton cancelButton = new JButton();
	protected JButton nextButton = new JButton();
	protected JButton backButton = new JButton();
	protected JPanel fillerPanel = new JPanel();
	protected Border border2;
	protected Border border3;
	protected JPanel outerCenterPanel = new JPanel();
	protected GridBagLayout gridBagLayout2 = new GridBagLayout();
	protected JPanel centerPanel = new JPanel();
	protected Border border4;
	protected JLabel titleLabel = new JLabel();
	protected CardLayout cardLayout1 = new CardLayout();
	protected WizardPanel currentWizardPanel;
	protected List allWizardPanels;
	protected ErrorHandler errorHandler;
	protected boolean finishPressed = false;
	protected HashMap dataMap = new HashMap();
	protected Border border5;
	protected Border border6;
	protected Border border7;
	protected ApplicationContext appContext =  AppContext.getApplicationContext();
	protected static final int FINISHED = 0;
	protected static final int CANCELED = 1;
	protected boolean canceled = false;
	protected boolean backenabled = true;

	public WizardComponent(ApplicationContext appContext, String title,
			ErrorHandler errorHandler) {
		super();
		canceled=false;
		this.errorHandler = errorHandler;

		try
		{
			jbInit();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		this.appContext = appContext;
		appContext.getMainFrame().addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				cancel();
			}
		});
	}



	protected void checkIDs(Collection wizardPanels) {
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

	protected void setCurrentWizardPanel(WizardPanel wizardPanel) {
		if (currentWizardPanel != null) {
			currentWizardPanel.remove(this);
		}

		titleLabel.setText(wizardPanel.getTitle());
		cardLayout1.show(centerPanel, wizardPanel.getID());
		currentWizardPanel = wizardPanel;
		updateButtons();
		currentWizardPanel.add(this);
		
		//Lo comento para que no haga el efecto de redimensionado a tamaño máximo de pantalla
		//instructionTextArea.setText(currentWizardPanel.getInstructions());
	}

	protected WizardPanel getCurrentWizardPanel() {
		return currentWizardPanel;
	}

	protected void updateButtons() {
		backButton.setEnabled(backenabled && !completedWizardPanels.isEmpty());
		nextButton.setEnabled(getCurrentWizardPanel().isInputValid());
		nextButton.setText((getCurrentWizardPanel().getNextID() == null)
				? appContext.getI18nString("WizardDialog.Finalizar") :appContext.getI18nString("WizardDialog.Siguiente>"));
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
		wizardPanels[0].enteredFromLeft(dataMap);
		setCurrentWizardPanel(wizardPanels[0]);
		//appContext.getMainFrame().pack();
	}

	public void addPanels(WizardPanel[] newWizardPanels) {

		List newAllWizardPanels = new ArrayList();
		newAllWizardPanels = Arrays.asList(newWizardPanels);
		checkIDs(newAllWizardPanels);

		((WizardPanel) allWizardPanels.get(allWizardPanels.size()-1)).setNextID(newWizardPanels[0].getID());

		List totalPanels = new ArrayList();

		for(int i=0; i<allWizardPanels.size();i++)
		{
			totalPanels.add(allWizardPanels.get(i));
		}


		int posicion=0;
		for (int i = 0; i < newWizardPanels.length; i++) {
			totalPanels.add(newWizardPanels[i]);
			centerPanel.add((Component) newWizardPanels[i], newWizardPanels[i].getID());
			newWizardPanels[i].setWizardContext(this);
			posicion++;
		}

		allWizardPanels = new ArrayList(totalPanels);

		completedWizardPanels = new ArrayList();
		((WizardPanel)allWizardPanels.get(0)).enteredFromLeft(dataMap);
		setCurrentWizardPanel((WizardPanel)allWizardPanels.get(0));
		//appContext.getMainFrame().pack();
	}

	protected void jbInit() throws Exception {
		border7 = BorderFactory.createEmptyBorder(5, 5, 20, 10); //PRIMERO, EL BLANCO
		border5 = BorderFactory.createLineBorder(Color.black, 2);
		border6 = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(148, 145, 140)),
				BorderFactory.createEmptyBorder(0, 0, 0, 0));
		centerPanel.setLayout(cardLayout1);
		border2 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		border3 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		border4 = BorderFactory.createBevelBorder(BevelBorder.LOWERED,
				Color.white, Color.white, new Color(103, 101, 98),
				new Color(148, 145, 140));



		buttonPanel.setLayout(gridBagLayout1);
		cancelButton.setText(appContext.getI18nString("WizardDialog.Cancelar"));
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButton_actionPerformed(e);
			}
		});
		nextButton.setText(appContext.getI18nString("WizardDialog.Siguiente>"));
		nextButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextButton_actionPerformed(e);
			}
		});
		backButton.setText(appContext.getI18nString("WizardDialog.<Anterior"));
		backButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				backButton_actionPerformed(e);
			}
		});
		buttonPanel.setBorder(border3);



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
		outerCenterPanel.setLayout(gridBagLayout2);
		titleLabel.setBackground(Color.white); //PRIMERO, LO BLANCO
		titleLabel.setForeground(Color.black);
		titleLabel.setFont(new java.awt.Font("Dialog", 1, 12));
		titleLabel.setBorder(border7);
		titleLabel.setOpaque(true);
		titleLabel.setText("TITULO: de <b>formato</b>");
		titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		titleLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		//titleLabel.setIcon(IconLoader.icon("app-icon.gif"));
		titleLabel.setToolTipText("Descripción");
		outerCenterPanel.setBorder(border6);

		outerCenterPanel.add(centerPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 36));

		this.setLayout(new BorderLayout());
		this.add(outerCenterPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.add(titleLabel, BorderLayout.NORTH);
	}

	void cancelButton_actionPerformed(ActionEvent e) {
		cancel();
	}

	protected void cancel() {
		setVisible(false);
		if(getCurrentWizardPanel()!=null)
			getCurrentWizardPanel().exiting();
		fire(this,WizardComponent.CANCELED,"canceled");
	}

	public void setWhiteBorder(boolean isSet)
	{
		titleLabel.setVisible(isSet);        
	}

	protected void nextButton_actionPerformed(ActionEvent e) {
		try {
			backenabled=true;
			getCurrentWizardPanel().exitingToRight();

			if (getCurrentWizardPanel().getNextID() == null) {
				finishPressed = true;
				setVisible(false);
				if(getCurrentWizardPanel()!=null)
					getCurrentWizardPanel().exiting();
				fire(this,WizardComponent.FINISHED,"finished");
				return;
			}
			if (getCurrentWizardPanel().getNextID().equals("")) {
				return;
			}

			completedWizardPanels.add(getCurrentWizardPanel());

			WizardPanel nextWizardPanel = find(getCurrentWizardPanel()
					.getNextID());
			Iterator it = allWizardPanels.iterator();
			while (it.hasNext()){
				Object objeto = it.next();			
				if (objeto instanceof ISeleccionInformeWizardPanel){
					ISeleccionInformeWizardPanel informe = (ISeleccionInformeWizardPanel) objeto;
				}
			}
			nextWizardPanel.enteredFromLeft(dataMap);
			setCurrentWizardPanel(nextWizardPanel);
		} catch (Throwable x) {
			if (errorHandler!=null)
				errorHandler.handleThrowable(x);
			else
				x.printStackTrace();   
		}
	}

	protected WizardPanel find(String id) {
		for (Iterator i = allWizardPanels.iterator(); i.hasNext();) {
			WizardPanel wizardPanel = (WizardPanel) i.next();

			if (wizardPanel.getID().equals(id)) {
				return wizardPanel;
			}
		}

		Assert.shouldNeverReachHere();

		return null;
	}

	protected boolean wasFinishPressed() {
		return finishPressed;
	}

	protected void backButton_actionPerformed(ActionEvent e) {
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
	protected ArrayList actionListeners = new ArrayList();
	/**
	 * Anota un ActionListener para los eventos de acción definidos en el WizardComponent
	 * ActionEvent:
	 * 			source: el WizardComponent que genera el evento.
	 * 			id puede ser WizardComponent.CANCELED o WizardComponent.FINISHED
	 * 			command: puede der "cancelled" o "finished"
	 * @param listener
	 */
	public void addActionListener(ActionListener listener) {
		actionListeners.add(listener);
	}

	public void remove(ActionListener listener) {
		actionListeners.remove(listener);
	}

	protected void fire(Object source, int id, String command) {
		for (Iterator i = actionListeners.iterator(); i.hasNext();) {
			ActionListener listener = (ActionListener) i.next();
			listener.actionPerformed(new ActionEvent(source, id, command));
		}
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
		 this.backenabled = status;
	 }
}
