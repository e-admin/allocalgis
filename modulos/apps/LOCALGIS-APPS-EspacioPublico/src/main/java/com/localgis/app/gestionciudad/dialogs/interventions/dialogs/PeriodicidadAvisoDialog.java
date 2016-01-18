/**
 * PeriodicidadAvisoDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.dialogs.interventions.dialogs;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.naming.ConfigurationException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import com.geopista.app.gestionciudad.utilidades.EdicionUtils;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.localgis.app.gestionciudad.utils.LocalGISWarning;
import com.localgis.app.gestionciudad.utils.RecursiveType;
import com.localgis.app.gestionciudad.utils.Weekdays;
import com.localgis.app.gestionciudad.utils.WeeklyRecursive;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

/**
 * @author javieraragon
 *
 */
public class PeriodicidadAvisoDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8321687816299929083L;
	private OKCancelPanel okCancelPanel = null;
	private LocalGISWarning recursiveIncident = null;
	//	private static Logger LOGGER = Logger.getLogger(PeriodicidadAvisoDialog.class);

	private JPanel rootPanel = null;
	private JPanel frecuencyPanel = null;

	private JComboBox startTimeComboBox = null;
	private JComboBox finishTimeComboBox = null;

	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	private DecimalFormat twoDigits = new DecimalFormat("00");
	private JLabel differenceLabel;

	private ButtonGroup radioFrecuencyTipesButtonsGroup = new ButtonGroup();
	private JRadioButton annualRadioButton = null;
	private JRadioButton monnthlyRadioButton = null;
	private JRadioButton weeklyRadioButton = null;
	private JRadioButton dailyRadioButton = null;

	private ButtonGroup weeklyButtonsGroup = new ButtonGroup();
	private JRadioButton workingDaysRadioButton = null;
	private JRadioButton weekendDaysRadioButton = null;
	private JRadioButton allDaysRadioButton = null;
	private JRadioButton selectionDaysRadioButton = null;

	private JCheckBox mondayCheckBox = null;
	private JCheckBox tuesdayCheckBox = null;
	private JCheckBox wednesdayCheckBox = null;
	private JCheckBox thursdayCheckBox = null;
	private JCheckBox fridayCheckBox = null;
	private JCheckBox saturdayCheckBox = null;
	private JCheckBox sundayCheckBox = null;

	private JPanel annualFrecuencyInformacionPanel = null;
	private JComboBox annualMonthStartComboBox = null;
	private JComboBox annualDayStartComboBox = null;
	//	private JComboBox annualMonthFinishComboBox = null;
	//	private JComboBox annualDayFinishComboBox = null;

	private JPanel monthlyFrecuencyInformationPanel = null;
	private JComboBox monthlyDayStartComboBox = null;
	//	private JComboBox monthlyDayFinishComboBox = null;


	private JPanel weeklyFrecuencyInformationPanel = null;
	private JPanel checkDaysPanel = null;

	public PeriodicidadAvisoDialog(Dialog parentPanel, String title, LocalGISWarning warning, boolean visualizar){
		super(parentPanel, title, true);
		UtilidadesAvisosPanels.inicializarIdiomaAvisosPanels();
		this.setSize(650, 500);
		this.setLocationRelativeTo(parentPanel);
		this.initialize();

		this.setResizable(false);
		this.pack();
		this.setEnabled(true);
		this.getAnnualRadioButton().setSelected(true);	
		getAllDaysRadioButton().setSelected(true);

		if (warning != null){
			this.recursiveIncident = warning;
			this.loadRecursiveIncidentData(warning);
		} else{
			recursiveIncident = new LocalGISWarning();
		}
		
		if (visualizar){
			this.disableEdition();
		}

		this.setVisible(true);
	}

	private void disableEdition() {
		this.getWeeklyRadioButton().setEnabled(false);
		this.getAnnualRadioButton().setEnabled(false);
		this.getDailyRadioButton().setEnabled(false);
		this.getMonthlyRadioButton().setEnabled(false);
		
		this.getAnnualDayStartComboBox().setEnabled(false);
		this.getAnnualDayStartComboBox().setEditable(true);
		this.getAnnualMonthStartComboBox().setEnabled(false);
		this.getAnnualMonthStartComboBox().setEditable(true);
		this.getMonthlyDayStartComboBox().setEnabled(false);
		this.getMonthlyDayStartComboBox().setEditable(true);

		
	}

	private void initialize() {
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});

		this.setLayout(new GridBagLayout());

		this.add(this.getRootPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 10));


		this.add(this.getOkCancelPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.LAST_LINE_END, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
	}

	private Component getRootPanel() {
		if (rootPanel == null){
			rootPanel = new JPanel(new GridBagLayout());

			//			rootPanel.add(this.getTimesPanel(), 
			//					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
			//							GridBagConstraints.HORIZONTAL, 
			//							new Insets(0, 5, 0, 5), 0, 10));

			rootPanel.add(this.getFrecuencysPanel(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 10));
			rootPanel.setSize(353, 632);
		}
		return rootPanel;
	}

	private Component getFrecuencysPanel() {
		if (frecuencyPanel == null){
			frecuencyPanel = new JPanel(new GridBagLayout());
			frecuencyPanel.setBorder(BorderFactory.createTitledBorder
					(null, I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.frecuencypaneltittle"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 

			frecuencyPanel.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.frecuencytypelabel")), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 10));
			frecuencyPanel.add(getFrecuenciesTypePanel(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 10));


			frecuencyPanel.add(getMonthlyFrecuencyInformationPanel(), 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 10));
			frecuencyPanel.add(getAnnualFrecuencyInformacionPanel(), 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 10));
			frecuencyPanel.add(getWeeklyFrecuencyInformationPanel(), 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 10));			
		}
		return frecuencyPanel;
	}


	private JComboBox getMonthlyDayStartComboBox(){
		if (monthlyDayStartComboBox == null){
			monthlyDayStartComboBox = new JComboBox();
			ArrayList<Integer> lista = new ArrayList<Integer>();
			for(int i=1; i <= 31; i++){
				lista.add(i);
			}
			EdicionUtils.cargarLista(monthlyDayStartComboBox, lista);
		}
		return monthlyDayStartComboBox;
	}


	//	private JComboBox getMonthlyDayFinishComboBox(){
	//		if (monthlyDayFinishComboBox == null){
	//			monthlyDayFinishComboBox = new JComboBox();
	//			ArrayList<Integer> lista = new ArrayList<Integer>();
	//			for(int i=1; i <= 31; i++){
	//				lista.add(i);
	//			}
	//			EdicionUtils.cargarLista(monthlyDayFinishComboBox, lista);
	//		}
	//		return monthlyDayFinishComboBox;
	//	}


	private JPanel getMonthlyFrecuencyInformationPanel(){
		if (monthlyFrecuencyInformationPanel == null){
			monthlyFrecuencyInformationPanel = new JPanel(new GridBagLayout());
			monthlyFrecuencyInformationPanel.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.monthlyfrecinfopaneltittle"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.ITALIC, 12)));
			monthlyFrecuencyInformationPanel.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.monthlystartdaylabel")), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			monthlyFrecuencyInformationPanel.add(getMonthlyDayStartComboBox(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));

			//			monthlyFrecuencyInformationPanel.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.monthlyenddaylabel")), 
			//					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
			//							GridBagConstraints.NONE, 
			//							new Insets(0, 5, 0, 5), 0, 0));
			//			monthlyFrecuencyInformationPanel.add(getMonthlyDayFinishComboBox(), 
			//					new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
			//							GridBagConstraints.NONE, 
			//							new Insets(0, 5, 0, 5), 0, 0));

			monthlyFrecuencyInformationPanel.add(new JLabel(""), 
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 150, 0));
		}
		return monthlyFrecuencyInformationPanel;
	}


	private JPanel getAnnualFrecuencyInformacionPanel(){
		if (annualFrecuencyInformacionPanel == null){
			annualFrecuencyInformacionPanel = new JPanel(new GridBagLayout());
			annualFrecuencyInformacionPanel.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.annualfrecinfopaneltittle"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.ITALIC, 12)));
			annualFrecuencyInformacionPanel.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.annualstartmonthlabel")), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			annualFrecuencyInformacionPanel.add(getAnnualMonthStartComboBox(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));

			annualFrecuencyInformacionPanel.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.annualstartdaylabel")), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			annualFrecuencyInformacionPanel.add(getAnnualDayStartComboBox(), 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			annualFrecuencyInformacionPanel.add(new JLabel(""), 
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 200, 0));
			//
			//			annualFrecuencyInformacionPanel.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.annualendmonthlabel")), 
			//					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
			//							GridBagConstraints.HORIZONTAL, 
			//							new Insets(0, 5, 0, 5), 0, 0));
			//			annualFrecuencyInformacionPanel.add(getAnnualMonthFinishComboBox(), 
			//					new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
			//							GridBagConstraints.NONE, 
			//							new Insets(0, 5, 0, 5), 0, 0));
			//
			//			annualFrecuencyInformacionPanel.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.annualenddaylabel")), 
			//					new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
			//							GridBagConstraints.HORIZONTAL, 
			//							new Insets(0, 5, 0, 5), 0, 0));
			//			annualFrecuencyInformacionPanel.add(getAnnualDayFinishComboBox(), 
			//					new GridBagConstraints(4, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
			//							GridBagConstraints.NONE, 
			//							new Insets(0, 5, 0, 5), 0, 0));
		}
		return annualFrecuencyInformacionPanel;
	}



	private JComboBox getAnnualMonthStartComboBox(){
		if (annualMonthStartComboBox == null){
			annualMonthStartComboBox = new JComboBox();
			ArrayList<Integer> lista = new ArrayList<Integer>();
			for(int i=1; i <= 12; i++){
				lista.add(i);
			}
			EdicionUtils.cargarLista(annualMonthStartComboBox, lista);
			annualMonthStartComboBox.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if (annualMonthStartComboBox != null ){
						if (annualMonthStartComboBox.getSelectedItem() != null){
							int month = (Integer) annualMonthStartComboBox.getSelectedItem();
							int maxdays = -1;
							if (month == 4 || month == 6 || month == 9 || month == 11){
								maxdays = 30;
							} else if (month == 2){
								maxdays = 29;
							} else{
								maxdays = 31;
							}
							ArrayList<Integer> listadays = new ArrayList<Integer>();
							for(int i=1; i <= maxdays; i++){
								listadays.add(i);
							}
							EdicionUtils.cargarLista(annualDayStartComboBox, listadays);
						}
					}
				}
			});
		}
		return annualMonthStartComboBox;
	}

	private JComboBox getAnnualDayStartComboBox(){
		if (annualDayStartComboBox == null){
			annualDayStartComboBox = new JComboBox();
			ArrayList<Integer> lista = new ArrayList<Integer>();
			for(int i=1; i <= 31; i++){
				lista.add(i);
			}
			EdicionUtils.cargarLista(annualDayStartComboBox, lista);
		}
		return annualDayStartComboBox;
	}

	//	private JComboBox getAnnualMonthFinishComboBox(){
	//		if (annualMonthFinishComboBox == null){
	//			annualMonthFinishComboBox = new JComboBox();
	//			ArrayList<Integer> lista = new ArrayList<Integer>();
	//			for(int i=1; i <= 12; i++){
	//				lista.add(i);
	//			}
	//			EdicionUtils.cargarLista(annualMonthFinishComboBox, lista);
	//			annualMonthFinishComboBox.addActionListener(new ActionListener(){
	//				@Override
	//				public void actionPerformed(ActionEvent e) {
	//					if (annualMonthFinishComboBox != null ){
	//						if (annualMonthFinishComboBox.getSelectedItem() != null){
	//							int month = (Integer) annualMonthFinishComboBox.getSelectedItem();
	//							int maxdays = -1;
	//							if (month == 4 || month == 6 || month == 9 || month == 11){
	//								maxdays = 30;
	//							} else if (month == 2){
	//								maxdays = 29;
	//							} else{
	//								maxdays = 31;
	//							}
	//							ArrayList<Integer> listadays = new ArrayList<Integer>();
	//							for(int i=1; i <= maxdays; i++){
	//								listadays.add(i);
	//							}
	//							EdicionUtils.cargarLista(annualDayFinishComboBox, listadays);
	//						}
	//					}
	//				}
	//			});
	//		}
	//		return annualMonthFinishComboBox;
	//	}

	//	private JComboBox getAnnualDayFinishComboBox(){
	//		if (annualDayFinishComboBox == null){
	//			annualDayFinishComboBox = new JComboBox();
	//			ArrayList<Integer> lista = new ArrayList<Integer>();
	//			for(int i=1; i <= 31; i++){
	//				lista.add(i);
	//			}
	//			EdicionUtils.cargarLista(annualDayFinishComboBox, lista);
	//		}
	//		return annualDayFinishComboBox;
	//	}



	private JPanel getWeeklyFrecuencyInformationPanel() {
		if (weeklyFrecuencyInformationPanel == null){
			weeklyFrecuencyInformationPanel = new JPanel(new GridBagLayout());
			weeklyFrecuencyInformationPanel.setBorder(BorderFactory.createTitledBorder
					(null, I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.weeklyfrecinfopaneltittle"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.ITALIC, 12)));
			//			weeklyFrecuencyInformationPanel.add(getWeeklyRadioButton(), 
			//					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
			//							GridBagConstraints.HORIZONTAL, 
			//							new Insets(0, 5, 0, 5), 0, 10));

			weeklyFrecuencyInformationPanel.add(getWeeklyInformationRadioButtonsPanel(), 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 10));
			weeklyFrecuencyInformationPanel.add(getWeeklyInformationCkesDaysPanel(), 
					new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 10));
		}
		return weeklyFrecuencyInformationPanel;
	}




	private JPanel getWeeklyInformationCkesDaysPanel() {
		if (checkDaysPanel == null){
			checkDaysPanel = new JPanel(new GridBagLayout());

			checkDaysPanel.add(getMondayCheckBox(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 10));
			checkDaysPanel.add(getTuesdayCheckBox(), 
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 10));
			checkDaysPanel.add(getWednesdayCheckBox(), 
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 10));
			checkDaysPanel.add(getThursdayCheckBox(), 
					new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 10));
			checkDaysPanel.add(getFridayCheckBox(), 
					new GridBagConstraints(5, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 10));
			checkDaysPanel.add(getSaturdayCheckBox(), 
					new GridBagConstraints(6, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 10));
			checkDaysPanel.add(getSundayCheckBox(), 
					new GridBagConstraints(7, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 10));
		}
		return checkDaysPanel;
	}

	private JCheckBox getMondayCheckBox(){
		if (mondayCheckBox == null){
			mondayCheckBox = new JCheckBox(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.weekday.monday"));
		}
		return mondayCheckBox;
	}
	private JCheckBox getTuesdayCheckBox(){
		if (tuesdayCheckBox == null){
			tuesdayCheckBox = new JCheckBox(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.weekday.tuesday"));
		}
		return tuesdayCheckBox;
	}
	private JCheckBox getWednesdayCheckBox(){
		if (wednesdayCheckBox == null){
			wednesdayCheckBox = new JCheckBox(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.weekday.wednesday"));
		}
		return wednesdayCheckBox;
	}
	private JCheckBox getThursdayCheckBox(){
		if (thursdayCheckBox == null){
			thursdayCheckBox = new JCheckBox(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.weekday.thursday"));
		}
		return thursdayCheckBox;
	}
	private JCheckBox getFridayCheckBox(){
		if (fridayCheckBox == null){
			fridayCheckBox = new JCheckBox(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.weekday.friday"));
		}
		return fridayCheckBox;
	}
	private JCheckBox getSaturdayCheckBox(){
		if (saturdayCheckBox == null){
			saturdayCheckBox = new JCheckBox(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.weekday.saturday"));
		}
		return saturdayCheckBox;
	}
	private JCheckBox getSundayCheckBox(){
		if (sundayCheckBox == null){
			sundayCheckBox = new JCheckBox(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.weekday.sunday"));
		}
		return sundayCheckBox;
	}

	private JPanel getWeeklyInformationRadioButtonsPanel() {
		JPanel panel = new JPanel(new GridBagLayout());

		panel.add(getAllDaysRadioButton(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 10));
		panel.add(getWorkingDaysRadioButton(), 
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 10));
		panel.add(getWeekendDaysRadioButton(), 
				new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 10));
		panel.add(getSelectionDaysRadioButton(), 
				new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 10));

		return panel;
	}

	private JRadioButton getWorkingDaysRadioButton(){
		if (workingDaysRadioButton	== null){
			workingDaysRadioButton = new JRadioButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.workingdayslabel"));
			this.weeklyButtonsGroup.add(workingDaysRadioButton);
			workingDaysRadioButton.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getItem().equals(workingDaysRadioButton)){
						onWorkingDaysRadioButtonDo();
					}
				}
			});
		}
		return workingDaysRadioButton;
	}

	private void onWorkingDaysRadioButtonDo() {
		this.setWeeklyInformationCkesDaysPanelEnabled(false);
		this.setDaysCheks(true, true, true, true, true, false, false);
	}

	private void setWeeklyInformationCkesDaysPanelEnabled(boolean flag) {
		this.getMondayCheckBox().setEnabled(flag);
		this.getTuesdayCheckBox().setEnabled(flag);
		this.getWednesdayCheckBox().setEnabled(flag);
		this.getThursdayCheckBox().setEnabled(flag);
		this.getFridayCheckBox().setEnabled(flag);
		this.getSaturdayCheckBox().setEnabled(flag);
		this.getSundayCheckBox().setEnabled(flag);
	}

	private JRadioButton getWeekendDaysRadioButton(){
		if (weekendDaysRadioButton	== null){
			weekendDaysRadioButton = new JRadioButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.weekendlabel"));
			this.weeklyButtonsGroup.add(weekendDaysRadioButton);
			weekendDaysRadioButton.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getItem().equals(weekendDaysRadioButton)){
						onWeekendDaysRadioButtonDo();
					}
				}
			});
		}
		return weekendDaysRadioButton;
	}

	private void onWeekendDaysRadioButtonDo() {
		this.setWeeklyInformationCkesDaysPanelEnabled(false);
		this.setDaysCheks(false, false, false, false, false, true, true);
	}

	private JRadioButton getAllDaysRadioButton(){
		if (allDaysRadioButton	== null){
			allDaysRadioButton = new JRadioButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.everydaylabel"));
			this.weeklyButtonsGroup.add(allDaysRadioButton);
			allDaysRadioButton.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getItem().equals(allDaysRadioButton)){
						onAllDaysRadioButtonDo();
					}
				}
			});
		}
		return allDaysRadioButton;
	}

	private void onAllDaysRadioButtonDo() {
		this.setWeeklyInformationCkesDaysPanelEnabled(false);
		this.setAllDaysChecks(true);
	}

	private void setAllDaysChecks (boolean flag){
		this.getMondayCheckBox().setSelected(flag);
		this.getTuesdayCheckBox().setSelected(flag);
		this.getWednesdayCheckBox().setSelected(flag);
		this.getThursdayCheckBox().setSelected(flag);
		this.getFridayCheckBox().setSelected(flag);
		this.getSaturdayCheckBox().setSelected(flag);
		this.getSundayCheckBox().setSelected(flag);
	}

	private void setDaysCheks(boolean mon, boolean tue, boolean wed, boolean thu, boolean fri,
			boolean sat, boolean sun){
		this.getMondayCheckBox().setSelected(mon);
		this.getTuesdayCheckBox().setSelected(tue);
		this.getWednesdayCheckBox().setSelected(wed);
		this.getThursdayCheckBox().setSelected(thu);
		this.getFridayCheckBox().setSelected(fri);
		this.getSaturdayCheckBox().setSelected(sat);
		this.getSundayCheckBox().setSelected(sun);
	}

	private JRadioButton getSelectionDaysRadioButton(){
		if (selectionDaysRadioButton	== null){
			selectionDaysRadioButton = new JRadioButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.selectdayslabel"));
			this.weeklyButtonsGroup.add(selectionDaysRadioButton);
			selectionDaysRadioButton.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getItem().equals(selectionDaysRadioButton)){
						onSelectionDaysRadioButtonDo();
					}
				}
			});
		}
		return selectionDaysRadioButton;
	}

	private void onSelectionDaysRadioButtonDo() {
		this.setWeeklyInformationCkesDaysPanelEnabled(true);
	}

	private JPanel getFrecuenciesTypePanel() {
		JPanel panel = new JPanel(new GridBagLayout());

		panel.add(getAnnualRadioButton(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 10));
		panel.add(getMonthlyRadioButton(), 
				new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 10));
		panel.add(getWeeklyRadioButton(), 
				new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 10));
		panel.add(getDailyRadioButton(), 
				new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 10));

		return panel;
	}

	private JRadioButton getAnnualRadioButton(){
		if (annualRadioButton == null){
			annualRadioButton = new JRadioButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.annuallabel"));
			radioFrecuencyTipesButtonsGroup.add(annualRadioButton);
			annualRadioButton.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getItem().equals(annualRadioButton)){
						onWeeklyInformation(e.getItem());	
					}
				}
			});
		}
		return annualRadioButton;
	}

	private JRadioButton getMonthlyRadioButton(){
		if (monnthlyRadioButton == null){
			monnthlyRadioButton = new JRadioButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.monthlylabel"));
			radioFrecuencyTipesButtonsGroup.add(monnthlyRadioButton);
			monnthlyRadioButton.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getItem().equals(monnthlyRadioButton)){
						onWeeklyInformation(e.getItem());	
					}
				}
			});
		}
		return monnthlyRadioButton;
	}

	private JRadioButton getWeeklyRadioButton(){
		if (weeklyRadioButton == null){
			weeklyRadioButton = new JRadioButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.weeklylabel"));
			radioFrecuencyTipesButtonsGroup.add(weeklyRadioButton);
			weeklyRadioButton.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getItem().equals(weeklyRadioButton)){
						onWeeklyInformation(e.getItem());	
						if (weeklyRadioButton.isSelected()){
							//							setWeeklyInformationCkesDaysPanelEnabled(true);
							setWeeklyOptionsCheksEnabled(true);
						} else{
							//							setWeeklyInformationCkesDaysPanelEnabled(false);
							setWeeklyOptionsCheksEnabled(false);
						}
					}
				}
			});

			weeklyRadioButton.setSelected(false);
			setWeeklyInformationCkesDaysPanelEnabled(false);
			setWeeklyOptionsCheksEnabled(false);
		}
		return weeklyRadioButton;
	}

	private void setWeeklyOptionsCheksEnabled(boolean flag){
		this.getAllDaysRadioButton().setEnabled(flag);
		this.getWorkingDaysRadioButton().setEnabled(flag);
		this.getWeekendDaysRadioButton().setEnabled(flag);
		this.getSelectionDaysRadioButton().setEnabled(flag);		
	}

	private void onWeeklyInformation(Object item){
		if (item.equals(annualRadioButton) && annualRadioButton.isSelected()){
			getMonthlyFrecuencyInformationPanel().setVisible(false);
			getAnnualFrecuencyInformacionPanel().setVisible(true);
			getWeeklyFrecuencyInformationPanel().setVisible(false);
		} else if (item.equals(monnthlyRadioButton) && monnthlyRadioButton.isSelected()){
			getMonthlyFrecuencyInformationPanel().setVisible(true);
			getAnnualFrecuencyInformacionPanel().setVisible(false);
			getWeeklyFrecuencyInformationPanel().setVisible(false);
		} else if (item.equals(weeklyRadioButton) && weeklyRadioButton.isSelected()){
			getMonthlyFrecuencyInformationPanel().setVisible(false);
			getAnnualFrecuencyInformacionPanel().setVisible(false);
			getWeeklyFrecuencyInformationPanel().setVisible(true);
		} else if (item.equals(dailyRadioButton) && dailyRadioButton.isSelected()){
			getMonthlyFrecuencyInformationPanel().setVisible(false);
			getAnnualFrecuencyInformacionPanel().setVisible(false);
			getWeeklyFrecuencyInformationPanel().setVisible(false);
		}
	}

	private JRadioButton getDailyRadioButton(){
		if (dailyRadioButton == null){
			dailyRadioButton = new JRadioButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.dailylabel"));
			radioFrecuencyTipesButtonsGroup.add(dailyRadioButton);
			dailyRadioButton.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getItem().equals(dailyRadioButton)){
						onWeeklyInformation(e.getItem());	
					}
				}
			});
		}
		return dailyRadioButton;
	}

	//	private JPanel getTimesPanel() {
	//		JPanel panel = new JPanel(new GridBagLayout());
	//
	//		panel.setBorder(BorderFactory.createTitledBorder
	//				(null, I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.incidenttimepaneltitle"), 
	//						TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
	//
	//		panel.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.incidentstarttimelabel")), 
	//				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
	//						GridBagConstraints.HORIZONTAL, 
	//						new Insets(0, 5, 0, 5), 0, 0));
	//		panel.add(this.getStartTimeComboBox(), 
	//				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
	//						GridBagConstraints.NONE, 
	//						new Insets(0, 5, 0, 5), 0, 0));
	//		panel.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.incidentendtimelabel")), 
	//				new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
	//						GridBagConstraints.HORIZONTAL, 
	//						new Insets(0, 5, 0, 5), 0, 0));
	//		panel.add(this.getFinishTimeComboBox(), 
	//				new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
	//						GridBagConstraints.NONE, 
	//						new Insets(0, 5, 0, 5), 0, 0));
	//
	//		JPanel difPanel = new JPanel();
	//		difPanel.add(getDifferenceLabel());
	//		panel.add(difPanel, 
	//				new GridBagConstraints(0, 1, 4, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
	//						GridBagConstraints.NONE, 
	//						new Insets(0, 5, 0, 5), 0, 0));
	//
	//		return panel;
	//	}

	private JLabel getDifferenceLabel(){
		if (differenceLabel == null){
			differenceLabel = new JLabel(getDifferenceBettwenHoursSelecteds());
		}
		return differenceLabel; 
	}

	@SuppressWarnings("deprecation")
	private String getDifferenceBettwenHoursSelecteds() {
		String resultado = I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.incidentendtimelengthlabel");
		if (getStartTimeComboBox().getSelectedItem()!= null && getFinishTimeComboBox().getSelectedItem() != null){	
			try {
				if (this.startTimeComboBox.getSelectedItem().equals("00:00") && 
						this.finishTimeComboBox.getSelectedItem().equals("24:00")){
					resultado = resultado + I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.incidentendtimelengthalldaylabel");
				} else{
					Date start = timeFormat.parse(
							(String) this.startTimeComboBox.getSelectedItem());
					Date end = timeFormat.parse(
							(String) this.finishTimeComboBox.getSelectedItem());

					int endHorus =-1;
					if (finishTimeComboBox.getSelectedItem().equals("24:00")){
						endHorus = 24;
					} else{
						endHorus = end.getHours();
					}

					double hoursDifference = endHorus - start.getHours();
					double startMinutes = 0;
					if (start.getMinutes() > 0 ){
						startMinutes = 60 - start.getMinutes();
					}
					double minutesDifference = 0;

					if (hoursDifference > 0){
						resultado = resultado + twoDigits.format(hoursDifference) + I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.incidentendtimelengthhourslabel");
						minutesDifference = (startMinutes) + end.getMinutes();
					} else{
						minutesDifference = end.getMinutes() - start.getMinutes();
					}

					if (minutesDifference > 0){
						resultado = resultado + twoDigits.format(minutesDifference) + I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.incidentendtimelengthminuteslabel");
					} else{
						resultado = resultado + 0 + I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.incidentendtimelengthminuteslabel");
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return resultado;
	}

	@SuppressWarnings("deprecation")
	private void loadRecursiveIncidentData(LocalGISWarning recursiveIncident) {
		if (recursiveIncident != null){
			if (recursiveIncident.getRecursivencident().getIncidentType().equals(RecursiveType.YEARLY)){
				this.getAnnualRadioButton().setSelected(true);
				try{
					if (recursiveIncident.getRecursivencident().getStart() instanceof GregorianCalendar){
						this.getAnnualMonthStartComboBox().setSelectedItem(((GregorianCalendar)recursiveIncident.getRecursivencident().getStart()).getTime().getMonth()+1);
						this.getAnnualDayStartComboBox().setSelectedItem(((GregorianCalendar)recursiveIncident.getRecursivencident().getStart()).getTime().getDate());
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			} else if (recursiveIncident.getRecursivencident().getIncidentType().equals(RecursiveType.MONTHLY)){
				this.getMonthlyRadioButton().setSelected(true);
				try{
					if (recursiveIncident.getRecursivencident().getStart() instanceof GregorianCalendar){
						this.getMonthlyDayStartComboBox().setSelectedItem(((GregorianCalendar)recursiveIncident.getRecursivencident().getStart()).getTime().getDate());
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			} else if (recursiveIncident.getRecursivencident().getIncidentType().equals(RecursiveType.WEEKLY)){
				this.getWeeklyRadioButton().setSelected(true);
				try{
					WeeklyRecursive weeklyRecursive = recursiveIncident.getRecursivencident().getWeeklyRecursiveIncident().get(0);
					if (weeklyRecursive!=null){
						if (weeklyRecursive.getWeekdayIncidents().contains(Weekdays.SATURDAY) && 
								weeklyRecursive.getWeekdayIncidents().contains(Weekdays.SUNDAY) &&
								weeklyRecursive.getWeekdayIncidents().size() == 2){
							this.getWeekendDaysRadioButton().setSelected(true);
						} else if (weeklyRecursive.getWeekdayIncidents().contains(Weekdays.MONDAY) && 
								weeklyRecursive.getWeekdayIncidents().contains(Weekdays.TUESDAY)&&
								weeklyRecursive.getWeekdayIncidents().contains(Weekdays.WEDNESDAY)&&
								weeklyRecursive.getWeekdayIncidents().contains(Weekdays.THURSDAY)&&
								weeklyRecursive.getWeekdayIncidents().contains(Weekdays.FRIDAY) &&
								weeklyRecursive.getWeekdayIncidents().size() == 5){
							this.getWorkingDaysRadioButton().setSelected(true);
						} else if (weeklyRecursive.getWeekdayIncidents().containsAll(Weekdays.weekdaysToCollection()) &&
								weeklyRecursive.getWeekdayIncidents().size() == 7){
							this.getAllDaysRadioButton().setSelected(true);
						} else{
							this.getSelectionDaysRadioButton().setSelected(true);
							this.setAllDaysChecks(false);
							java.util.Iterator<Weekdays> it = weeklyRecursive.getWeekdayIncidents().iterator();
							while(it.hasNext()){
								Weekdays day = it.next();
								if(day.equals(Weekdays.MONDAY))
									this.getMondayCheckBox().setSelected(true);
								if(day.equals(Weekdays.TUESDAY))
									this.getTuesdayCheckBox().setSelected(true);
								if(day.equals(Weekdays.WEDNESDAY))
									this.getWednesdayCheckBox().setSelected(true);
								if(day.equals(Weekdays.THURSDAY))
									this.getThursdayCheckBox().setSelected(true);
								if(day.equals(Weekdays.FRIDAY))
									this.getFridayCheckBox().setSelected(true);
								if(day.equals(Weekdays.SATURDAY))
									this.getSaturdayCheckBox().setSelected(true);
								if(day.equals(Weekdays.SUNDAY))
									this.getSundayCheckBox().setSelected(true);
							}
						}
					}

				}catch (Exception e) {
					e.printStackTrace();
				}
			} else if (recursiveIncident.getRecursivencident().getIncidentType().equals(RecursiveType.DAILY)){
				this.getDailyRadioButton().setSelected(true);
			}

		}
	}


	private JComboBox getStartTimeComboBox(){
		if (startTimeComboBox == null){
			startTimeComboBox = new JComboBox();
			startTimeComboBox.setEditable(true);
			EdicionUtils.cargarLista(startTimeComboBox, getTimesArrayList());
			startTimeComboBox.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if (startTimeComboBox != null){
						if (startTimeComboBox.getSelectedItem() != null){
							try{
								String startTime = (String) startTimeComboBox.getSelectedItem();
								Date start = timeFormat.parse(startTime);
								if (startTime.equals("")){
									showTimeFormatMessageError();
								} else{
									loadNewTimes(start, finishTimeComboBox);
								}
							}catch (ParseException ex1) {
								showTimeFormatMessageError();
							}
							catch (Exception ex2) {
								showTimeFormatMessageError();
							}
						}
					}
					getDifferenceLabel().setText(getDifferenceBettwenHoursSelecteds());
				}
			});

		}
		return startTimeComboBox;
	}

	@SuppressWarnings("deprecation")
	private void loadNewTimes(Date start, JComboBox finishTimeComboBox) {
		ArrayList<String> times = new ArrayList<String>();

		String endTime = (String) finishTimeComboBox.getSelectedItem();
		Date finish = null;
		try {
			if (endTime != null){
				finish = timeFormat.parse(endTime);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		int hours = start.getHours();
		if (hours < 24){
			int minutes = start.getMinutes();
			int desp = 1;
			if 	(minutes < 30){
				times.add( twoDigits.format(hours) + ":30");
				desp = 0;
			}
			for (int i=hours + desp ; i < 24; i++ ){
				times.add( twoDigits.format(i) + ":" + "00");
				times.add( twoDigits.format(i) + ":" + "30");
			}
			times.add("24:00");
		}

		if (finish != null && start != null){
			if (!(finishTimeComboBox.getSelectedItem().equals("24:00"))){
				if (finish.after(start) && !(finish.compareTo(start) == 0)){
					String formatedDate = twoDigits.format(finish.getHours()) + ":" + twoDigits.format(finish.getMinutes()); 
					if (!times.contains(formatedDate)){
						times.add(0, formatedDate);
					}
				}
			}
		}

		EdicionUtils.cargarLista(finishTimeComboBox, times);


	}

	private JComboBox getFinishTimeComboBox(){
		if (finishTimeComboBox == null){
			finishTimeComboBox = new JComboBox();
			finishTimeComboBox.addItem(null);
			finishTimeComboBox.setEditable(true);
			EdicionUtils.cargarLista(finishTimeComboBox, getTimesArrayList());
			finishTimeComboBox.addItem("24:00");

			finishTimeComboBox.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					getDifferenceLabel().setText(getDifferenceBettwenHoursSelecteds());
				}				
			});
			finishTimeComboBox.setSelectedItem("24:00");
		}
		return finishTimeComboBox;
	}

	private void showTimeFormatMessageError() {
		JOptionPane.showMessageDialog(this, 
				I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.errormessage.timeformat1")
				+ this.timeFormat.toPattern() 
				+I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.recurrencedialog.errormessage.timeformat2"));
	}

	private ArrayList<String> getTimesArrayList() {
		ArrayList<String> hours = new ArrayList<String>();
		for (int i = 0; i < 24 ; i++){
			hours.add( twoDigits.format(i) + ":" + "00");
			hours.add( twoDigits.format(i) + ":" + "30");
		}
		return hours;
	}

	public LocalGISWarning getRecursiveIncident(){
		return recursiveIncident;
	}

	private OKCancelPanel getOkCancelPanel(){
		if (this.okCancelPanel == null){ 
			this.okCancelPanel = new OKCancelPanel();
			this.okCancelPanel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					okCancelPanel_actionPerformed(e);
				}
			});
		} 
		return this.okCancelPanel;
	}

	void okCancelPanel_actionPerformed(ActionEvent e) {
		if (!okCancelPanel.wasOKPressed() || isInputValid()) {
			setVisible(false);
			return;
		}
	}

	protected boolean isInputValid() {
		if (!allDataIscorrect()){
			return false;
		}
		return setDataToIncident(); 
	}

	private boolean setDataToIncident(){
		try{
			//			GregorianCalendar start = new GregorianCalendar();
			//			start.setTime(timeFormat.parse(
			//					(String) this.startTimeComboBox.getSelectedItem()));
			//			this.recursiveIncident.setStart(start);
			//			String startHourString = (String) this.startTimeComboBox.getSelectedItem();
			//
			//			GregorianCalendar end = new GregorianCalendar();
			//			end.setTime(timeFormat.parse(
			//					(String) this.finishTimeComboBox.getSelectedItem()));
			//			this.recursiveIncident.setStart(end);
			//			String finishHourString = (String) this.finishTimeComboBox.getSelectedItem();
			//			if (finishHourString.equals("24:00")){
			//				finishHourString = "23:59";
			//			}

			//			String finishHourString = "23:59";
			String startHourString  = "00:00";

			if (this.getAnnualRadioButton().isSelected()){
				String dayStart = "";
				//				String dayFinish = "";
				String monthStart = "";
				//				String monthFinish = "";
				//				this.recursiveIncident.setIncidentType(RecursiveType.YEARLY);
				if (this.getAnnualMonthStartComboBox() != null && this.getAnnualMonthStartComboBox().getSelectedItem()!=null){
					if (this.getAnnualDayStartComboBox()!=null && this.getAnnualDayStartComboBox().getSelectedItem()!=null){											
						monthStart = twoDigits.format(this.getAnnualMonthStartComboBox().getSelectedItem());
						dayStart = twoDigits.format(this.getAnnualDayStartComboBox().getSelectedItem());
					}
				}
				//				if (this.getAnnualMonthFinishComboBox() != null && this.getAnnualMonthFinishComboBox().getSelectedItem()!=null){
				//					if (this.getAnnualDayFinishComboBox()!=null && this.getAnnualDayFinishComboBox().getSelectedItem()!=null){											
				//						monthFinish = twoDigits.format(this.getAnnualMonthFinishComboBox().getSelectedItem());
				//						dayFinish = twoDigits.format(this.getAnnualDayFinishComboBox().getSelectedItem());
				//					}
				//				}
				// Generar el String para crear periodicidad

				//				String annual = "[" + monthStart+"/"+dayStart+"-"+monthFinish+"/"+dayFinish+"]" 
				String annual = "[" + monthStart+"/"+dayStart+"-]"
				//				+ "("+startHourString+":00"+"-"+finishHourString+":00"+")";
				+ "("+startHourString+":00"+"-)";
				System.err.println(annual);
				try {
					LocalGISWarning annualRecursiveIncident = new LocalGISWarning(annual);
					this.recursiveIncident = annualRecursiveIncident;
				} catch (ConfigurationException e) {
					e.printStackTrace();
					return false;
				}

			} else if (this.getMonthlyRadioButton().isSelected()){
				String dayStart = "";
				//				String dayFinish = "";
				//				this.recursiveIncident.setIncidentType(RecursiveType.MONTHLY);
				if (this.getMonthlyDayStartComboBox() != null && this.getMonthlyDayStartComboBox().getSelectedItem() != null){
					dayStart = twoDigits.format(this.getMonthlyDayStartComboBox().getSelectedItem());
				}
				//				if (this.getMonthlyDayFinishComboBox() != null && this.getMonthlyDayFinishComboBox().getSelectedItem() != null){
				//					dayFinish = twoDigits.format(this.getMonthlyDayStartComboBox().getSelectedItem());
				//				}
				//				String monthly = "["+dayStart+"-"+dayFinish+"]"
				String monthly = "["+dayStart+"-]"
				//				+ "("+startHourString+":00"+"-"+finishHourString+":00"+")";
				+ "("+startHourString+":00"+"-)";
				System.err.println(monthly);
				try {
					LocalGISWarning monthlyRecursiveIncident = new LocalGISWarning(monthly);
					this.recursiveIncident = monthlyRecursiveIncident;
				} catch (ConfigurationException e) {
					e.printStackTrace();
					return false;
				}
			} else if (this.getDailyRadioButton().isSelected()){
				//				this.recursiveIncident.setIncidentType(RecursiveType.DAILY);
				String daily = "[-]"
					//					+ "("+startHourString+":00"+"-"+finishHourString+":00"+")";
					+ "("+startHourString+":00"+"-)";
				try {
					LocalGISWarning dailyRecursiveIncident = new LocalGISWarning(daily);
					this.recursiveIncident = dailyRecursiveIncident;
				} catch (ConfigurationException e) {
					e.printStackTrace();
					return false;
				}
			} else if (this.getWeeklyRadioButton().isSelected()){
				String days = "";
				if (this.getMondayCheckBox().isSelected()){
					days = days + "L";
				}
				if (this.getTuesdayCheckBox().isSelected()){
					days = days + "M";
				}
				if (this.getWednesdayCheckBox().isSelected()){
					days = days + "X";
				}
				if (this.getThursdayCheckBox().isSelected()){
					days = days + "J";
				}
				if (this.getFridayCheckBox().isSelected()){
					days = days + "V";
				}
				if (this.getSaturdayCheckBox().isSelected()){
					days = days + "S";
				}
				if (this.getSundayCheckBox().isSelected()){
					days = days + "D";
				}
				String weekly = "["+days+"]"
				//				+ "("+startHourString+":00"+"-"+finishHourString+":00"+")";
				+ "("+startHourString+":00"+"-)";
				//				ArrayList<WeeklyRecursive> weeklyRI = new ArrayList<WeeklyRecursive>();
				System.err.println(weekly);
				try {
					//					weeklyRI.add(new WeeklyRecursive(weekly));
					//					this.recursiveIncident.setWeeklyRecursiveIncident(weeklyRI);
					LocalGISWarning weeklyRecursiveWarning = new LocalGISWarning(weekly);
					weeklyRecursiveWarning.toString();
					this.recursiveIncident = weeklyRecursiveWarning;
				} catch (ConfigurationException e) {
					e.printStackTrace();
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		System.err.println(this.recursiveIncident.toString());
		return true;
	}

	private boolean allDataIscorrect() {
		return true;
	}

	public boolean wasOKPressed() {
		return okCancelPanel.wasOKPressed();
	}

	void this_componentShown(ComponentEvent e) {
		okCancelPanel.setOKPressed(false);
	}

}

