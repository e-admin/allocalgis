/**
 * DatosPersonalesSistemaDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.dialogs.DatosPersonalesSistemaPanel;
import com.geopista.protocol.datosPersonales.DatosPersonales;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

public class DatosPersonalesSistemaDialog extends JDialog{

	private OKCancelPanel _okCancelPanel = null;
    
	private DatosPersonalesSistemaPanel titularesSistemaPanel;
	private String nif = "";
	private String nombreApellido = "";
	DatosPersonales datosPersonales=null;
	
	
	public static final int DIM_X = 400;
    public static final int DIM_Y = 300;
	
	
    private OKCancelPanel getOkCancelPanel()
    {
        if (_okCancelPanel == null)
        {
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    
                    if (_okCancelPanel.wasOKPressed())
                    {
                        String errorMessage = getDatosPersonalesSistemaPanel().validateInput();
                        
                        if (errorMessage != null)
                        {
                            JOptionPane
                            .showMessageDialog(
                                    DatosPersonalesSistemaDialog.this,
                                    errorMessage,
                                    I18N.get("Expedientes", "mensajes.novalida.datosPersonales"),
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        } else
                        {
                            try
                            {
                            	getDatosPersonalesSistemaPanel().okPressed();
                            } 
                            catch (Exception e1)
                            {
                                JOptionPane
                                .showMessageDialog(
                                		DatosPersonalesSistemaDialog.this,
                                        errorMessage,
                                        I18N.get("Expedientes", "mensajes.norecupera.datosPersonales"),
                                        JOptionPane.ERROR_MESSAGE);
                            }                            
                        }
                    }
                    else{
                    	setNombreApellido(nombreApellido);
                    	setNif(nif);
                    	setDatosPersonales(datosPersonales);
                    }
                    dispose();                    
                }
                    });
        }
        return _okCancelPanel;
    }
    
	
    public DatosPersonalesSistemaDialog(String nif, String nombreApellido)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        this.nif = nif;
        this.nombreApellido = nombreApellido;
        initialize();
        
    }
    
   
    public DatosPersonalesSistemaDialog()
    {
        this(null, null);
    }
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
        
        this.setModal(true);
        this.setContentPane(getDatosPersonalesSistemaPanel());
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(DIM_X, DIM_Y);
        this.setTitle(I18N.get("Expedientes", "busqueda.titulares.dialog.titulo"));
        this.setResizable(true);
        this.getOkCancelPanel().setVisible(true);        
        this.addWindowListener(new java.awt.event.WindowAdapter()
                {
            public void windowClosing(java.awt.event.WindowEvent e)
            {
                dispose();
            }
                });
        
    }
    
    public static void main(String[] args)
    {
        DatosPersonalesSistemaDialog dialog = new DatosPersonalesSistemaDialog();
        dialog.setVisible(true);        
    }    
    
    private DatosPersonalesSistemaPanel getDatosPersonalesSistemaPanel()
    {
        if (titularesSistemaPanel == null)
        {
           
        	titularesSistemaPanel = new DatosPersonalesSistemaPanel(new GridBagLayout(), nif, nombreApellido);
        	titularesSistemaPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));
        }
        return titularesSistemaPanel;
    }  
    
    public String getNif() {
		return getDatosPersonalesSistemaPanel().getNif();
	}

	public String getNombreApellido() {
		return getDatosPersonalesSistemaPanel().getNombreApellidos();
	}
	
	public void setNif(String nif) {
		this.nif = nif;
		getDatosPersonalesSistemaPanel().setNif(nif);
	}


	public void setNombreApellido(String nombreApellido) {
		this.nombreApellido = nombreApellido;
		getDatosPersonalesSistemaPanel().setNombreApellidos(nombreApellido);
	}

	public DatosPersonales getDatosPersonales() {
		return getDatosPersonalesSistemaPanel().getDatosPersonales();
	}


	public void setDatosPersonales(DatosPersonales datosPersonales) {
		this.datosPersonales = datosPersonales;
		getDatosPersonalesSistemaPanel().setDatosPersonales(datosPersonales);
	}


    
}
