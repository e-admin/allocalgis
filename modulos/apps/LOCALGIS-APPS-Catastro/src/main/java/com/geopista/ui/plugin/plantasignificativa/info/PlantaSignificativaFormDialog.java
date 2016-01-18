package com.geopista.ui.plugin.plantasignificativa.info;



import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.edicion.utils.EstructuraDBListCellRenderer;
import com.geopista.app.catastro.model.beans.Planta;

public class PlantaSignificativaFormDialog extends JDialog implements ActionListener, PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9093158698087790146L;

	private String typedText = null;
	private JTextField numeroPlantasTextField;
	
	private JComboBox jComboBoxPlantaLocal = null;

	private JOptionPane optionPane;

	private static String btnString1 = "Aceptar";
	private static String btnString2 = "Cancelar";
	
	private PlantaSignificativaInfo plantaInfo= null;
	

	/**
	 * Returns null if the typed string was invalid;
	 * otherwise, returns the string as the user entered it.
	 */
	public String getValidatedText() {
		return typedText;
	}

	
	
	/**
	 * @return the plantaInfo
	 */
	public PlantaSignificativaInfo getPlantaInfo() {
		return plantaInfo;
	}



	/**
	 * @param plantaInfo the plantaInfo to set
	 */
	public void setPlantaInfo(PlantaSignificativaInfo plantaInfo) {
		this.plantaInfo = plantaInfo;
	}

    
	private JComboBox getJComboBoxPlantaLocal()
    {
        if (jComboBoxPlantaLocal  == null)
        {
        	jComboBoxPlantaLocal = new JComboBox();
        	jComboBoxPlantaLocal.setRenderer(new EstructuraDBListCellRenderer());
        	
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerPlanta();
            EdicionUtils.cargarLista(getJComboBoxPlantaLocal(), lst);
        }
        return jComboBoxPlantaLocal;
    }
    


	/** Creates the reusable dialog. */
	public PlantaSignificativaFormDialog(Frame aFrame, PlantaSignificativaInfo info) {
		super(aFrame, true);

		this.setTitle("Información de la planta significativa.");
		this.setSize(400, 200);
		this.setResizable(false);
		this.initializeDialogPosition();


		numeroPlantasTextField = new JTextField(3);

		//Create an array of the text and components to be displayed.
		String msgString1 = "Nombre de la planta: ";
		String msgString2 = "Número de plantas: ";
		Object[] array = {msgString1, getJComboBoxPlantaLocal(), msgString2, numeroPlantasTextField};

		//Create an array specifying the number of dialog buttons
		//and their text.
		Object[] options = {btnString1, btnString2};

		//Create the JOptionPane.
		optionPane = new JOptionPane(array,
				JOptionPane.QUESTION_MESSAGE,
				JOptionPane.YES_NO_OPTION,
				null,
				options,
				options[0]);

		//Make this dialog display it.
		setContentPane(optionPane);

		//Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				/*
				 * Instead of directly closing the window,
				 * we're going to change the JOptionPane's
				 * value property.
				 */
				optionPane.setValue(new Integer(
						JOptionPane.CLOSED_OPTION));
			}
		});

		//Ensure the text field always gets the first focus.
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				numeroPlantasTextField.requestFocusInWindow();
			}
		});

		//Register an event handler that puts the text into the option pane.
//		jComboBoxPlantaLocal.addActionListener(this);

		//Register an event handler that reacts to option pane state changes.
		optionPane.addPropertyChangeListener(this);
		
		this.plantaInfo = info;
		
		this.setVisible(true);
		
		
	}

	/** This method handles events for the text field. */
	public void actionPerformed(ActionEvent e) {
		optionPane.setValue(btnString1);
	}

	/** This method reacts to state changes in the option pane. */
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();

		if (isVisible()
				&& (e.getSource() == optionPane)
				&& (JOptionPane.VALUE_PROPERTY.equals(prop) ||
						JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
			Object value = optionPane.getValue();

			if (value == JOptionPane.UNINITIALIZED_VALUE) {
				//ignore reset
				return;
			}



			if (btnString1.equals(value)) {
//				typedText = nombrePlantaTextField.getText();
//				String ucPlantaText = typedText.toUpperCase();
				
				String ucPlantaText = ((Planta) jComboBoxPlantaLocal.getSelectedItem()).getDescripcion();
//						+ " " +	((Planta) jComboBoxPlantaLocal.getSelectedItem()).getPatron();
				String ucNumPlantaTex = numeroPlantasTextField.getText();
				
				if (ucPlantaText.equals(" ") || ucPlantaText.equals("")) {
					//text was invalid
					jComboBoxPlantaLocal.setSelectedIndex(0);
					JOptionPane.showMessageDialog(
							PlantaSignificativaFormDialog.this,
							"Valor no válido, seleccione un nombre para la planta.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					typedText = null;
					jComboBoxPlantaLocal.requestFocusInWindow();
					//Reset the JOptionPane's value.
					//If you don't do this, then if the user
					//presses the same button next time, no
					//property change event will be fired.
					// javier aragon
					optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

				} else {
					//we're done; next step, validate nombre planta and after clear and dismiss the dialog
					this.plantaInfo.setNombrePlantas(ucPlantaText);
				}
				
				if (ucNumPlantaTex.equals("")) {
					//text was invalid
					numeroPlantasTextField.selectAll();
					JOptionPane.showMessageDialog(
							PlantaSignificativaFormDialog.this,
							"Valor no válido, introduzca de nuevo el número de plantas significativas.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					typedText = null;
					numeroPlantasTextField.requestFocusInWindow();
					//Reset the JOptionPane's value.
					//If you don't do this, then if the user
					//presses the same button next time, no
					//property change event will be fired.
					// javier aragon
					optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
				} else {
					//we're done; clear and dismiss the dialog
					try{
						this.plantaInfo.setNumPlantas(Integer.parseInt(ucNumPlantaTex));
						clearAndHide();
					}
					catch (NumberFormatException e2) {
						// TODO: handle exception
						//text was invalid
						numeroPlantasTextField.selectAll();
						JOptionPane.showMessageDialog(
								PlantaSignificativaFormDialog.this,
								"Valor no válido, introduzca de nuevo el número de plantas significativas.",
								"Error",
								JOptionPane.ERROR_MESSAGE);
						typedText = null;
						numeroPlantasTextField.requestFocusInWindow();
						//Reset the JOptionPane's value.
						//If you don't do this, then if the user
						//presses the same button next time, no
						//property change event will be fired.
						// javier aragon
						optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
					}
				}

			} else { //user closed dialog or clicked cancel
				typedText = null;
				clearAndHide();
			}
		}
	}

	/** This method clears the dialog and hides it. */
	public void clearAndHide() {
		
		numeroPlantasTextField.setText(null);
		
		this.plantaInfo = null;
		setVisible(false);
		
	}
	
	/**
	 * @param mainFrame parent frame for the dialog
	 * @param info info of a Planta Sifnificativa, to store data from form.
	 * @return 0 if accept button is pressed and form dat is ok, and false in any other case. 
	 */
	public static int showPlantaSignificativaFormDialog(Frame mainFrame,PlantaSignificativaInfo info){
		
		PlantaSignificativaFormDialog ps = new PlantaSignificativaFormDialog( mainFrame, info);
		
		if (ps.optionPane.getValue().equals(btnString1)){
			// accept
			return 0;
		} else if (ps.optionPane.getValue().equals(btnString2)){
			// cancel
			return 1;
		}
		
		// any other case
		return -1;
	}
	
	
	/**
	 * Try set default position to dialog, center on screen.
	 */
	private void initializeDialogPosition() {
		//Get the screen size  
		Toolkit toolkit = Toolkit.getDefaultToolkit();  
		Dimension screenSize = toolkit.getScreenSize();  
		int x = (screenSize.width - this.getWidth()) / 2;  
		int y = (screenSize.height - this.getHeight()) / 2;    
		//Set the new frame location  
		this.setLocation(x, y);
	}
}

