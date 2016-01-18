package com.geopista.ui.dialogs;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.WindowConstants;

import com.geopista.app.AppContext;
import com.geopista.app.backup.ui.I18N;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.protocol.administrador.Entidad;
import com.vividsolutions.jump.util.StringUtil;



public class MunicipioDialog extends JDialog{
	private JButton cancelButton;
	private JButton aceptButton;
	private JComboBox muniNameJC;  
	private JLabel muniLabel;
	private boolean canceled = true;

	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JLabel errorLabel = null;
	private List listMunicipios = null;
	private List listEntidades = null;
	private boolean bEntidad = false;
	
	private I18N i18n = I18N.getInstance();
	
	public MunicipioDialog(Frame f, List listMunicipios)  {
		super(f);		
		setTitle(getExternalizedString("SystemConfigPanel.munCode"));
		this.listMunicipios = listMunicipios;
		initialize();
	}

	public MunicipioDialog(Frame f, List listEntidades, boolean bEntidad)  {
		super(f);		
		setTitle(getExternalizedString("SystemConfigPanel.entCode"));
		this.listEntidades = listEntidades;
		this.bEntidad = bEntidad;
		initialize();
	}

	private void initialize(){
		
		muniLabel = new JLabel();
		muniNameJC = createCombo();
		aceptButton = new JButton();
		cancelButton = new JButton();

		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cancelButton();
			}
		});

		aceptButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				acceptButton();
			}
		});

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setName(getExternalizedString("Login Dialog")); 
		setSize(new java.awt.Dimension(400,167));

		this.setContentPane(getJPanel());
		this.setResizable(false);  

		if (bEntidad)
			muniLabel.setText(getExternalizedString("importar.informacion.referencia.entidad")); 
		else
			muniLabel.setText(getExternalizedString("importar.informacion.referencia.municipio")); 
		muniLabel.setPreferredSize(new java.awt.Dimension(50,15));
		muniNameJC.setFocusCycleRoot(false);  
		aceptButton.setText(getExternalizedString("Aceptar")); 
		cancelButton.setText(getExternalizedString("Cancelar"));
		cancelButton.setSelected(true); 
	}

 
	private JPanel getJPanel() {
		if (jPanel == null) {
			errorLabel = new JLabel();
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 2;
			gridBagConstraints1.gridwidth = 2;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.gridwidth = 2;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.insets = new java.awt.Insets(0,0,0,5);
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 5;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.insets = new java.awt.Insets(10,0,0,0);
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 4;
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 6;
			errorLabel.setText("");
			jPanel.add(muniNameJC, gridBagConstraints1);
			jPanel.add(muniLabel, gridBagConstraints5);
			jPanel.add(getJPanel1(), gridBagConstraints7); 
			jPanel.add(errorLabel, gridBagConstraints11);
		}
		return jPanel;
	}
	
	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			java.awt.FlowLayout flowLayout8 = new FlowLayout();
			jPanel1 = new JPanel();
			jPanel1.setLayout(flowLayout8);
			flowLayout8.setAlignment(java.awt.FlowLayout.LEFT);
			jPanel1.add(aceptButton, null);
			jPanel1.add(cancelButton, null);
		}
		return jPanel1;
	}
	
	public void setMunicipio(String municipio)
	{
		muniNameJC.setSelectedItem(municipio);
	}
	public String getMunicipio()
	{
		String sMunicipio = (String)muniNameJC.getSelectedItem();
		StringTokenizer st = new StringTokenizer(sMunicipio,"-");
		if (st.hasMoreTokens())
			sMunicipio = st.nextToken();
		if (bEntidad)
			return sMunicipio;
		if (validateMunicipio(sMunicipio))
			return sMunicipio;
		return "";
	}


	private void cancelButton()
	{
		canceled = true;
		this.setVisible(false);
		this.dispose();
	}

	private void acceptButton()
	{
		canceled=false;  
		this.setVisible(false);
		canceled=false;
		this.dispose();
	}

	public boolean isCanceled()
	{
		return this.canceled;
	}


	/**
	 * @return Returns the errorLabel.
	 */
	
	 public String getErrorLabel()
	 {
		 return errorLabel.getText();
	 }
	 
	 /**
	  * @param errorLabelText The errorLabel to set.
	  */
	 
	 public void setErrorLabel(String errorLabelText)
	 {
		 this.errorLabel.setText(errorLabelText);
	 }



	 private String getExternalizedString(String key) {
		 return ((AppContext) AppContext.getApplicationContext()).getI18nString(key);
	 }
	 
	    /**
	     * Crea un comboBox con los municipios de la entidad a la que está asociado el usuario
	     */
	    private JComboBox createCombo(){
	    	JComboBox jc = new JComboBox();
	    	if (bEntidad == false){
	    		Iterator itMuni = listMunicipios.iterator();
		    	while(itMuni.hasNext()){
		    		Municipio municipio = (Municipio)itMuni.next();
			        String nombreMunicipio = String.valueOf(municipio.getId()+"-"+municipio.getNombreOficial());
			        jc.addItem(nombreMunicipio);
		    	}
	    	}else{
	    		Iterator itEntid = listEntidades.iterator();
		    	while(itEntid.hasNext()){
		    		Entidad entidad = (Entidad)itEntid.next();
			        String nombreEntidad = entidad.getId()+"-"+entidad.getNombre();
			        jc.addItem(nombreEntidad);
		    	}
	    		
	    	}
	    	return jc;
	    }
	 
		/**
		 * Compruebo si el municipio que introduce el usuario tiene el formato correcto
		 */

		private boolean validateMunicipio(String sMunicipio){
			boolean correcto = false;
			if (sMunicipio != null){
				if (StringUtil.isNumber(sMunicipio))
					correcto = true;
			}
			return correcto;
		}
}
