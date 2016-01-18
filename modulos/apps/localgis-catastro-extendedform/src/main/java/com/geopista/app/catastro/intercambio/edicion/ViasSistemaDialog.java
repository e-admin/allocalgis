/**
 * ViasSistemaDialog.java
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
import com.geopista.app.catastro.intercambio.edicion.dialogs.ViasSistemaPanel;
import com.geopista.app.catastro.model.beans.Municipio;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class ViasSistemaDialog extends JDialog
{
    private OKCancelPanel _okCancelPanel = null;
    
    private ViasSistemaPanel viasSistemaPanel;
    private String nomVia = "";
   	private String tipoVia = "";
    private String idMunicipio = "";
    private Municipio municipio;
	
	private int codigoVia;


	public static final int DIM_X = 600;
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
                        String errorMessage = getViasSistemaPanel().validateInput();
                        
                        if (errorMessage != null)
                        {
                            JOptionPane
                            .showMessageDialog(
                                    ViasSistemaDialog.this,
                                    errorMessage,
                                    I18N.get("Expedientes", "mensajes.novalida.vias"),
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        } else
                        {
                            try
                            {
                                getViasSistemaPanel().okPressed();
                            } 
                            catch (Exception e1)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        ViasSistemaDialog.this,
                                        errorMessage,
                                        I18N.get("Expedientes", "mensajes.norecupera.vias"),
                                        JOptionPane.ERROR_MESSAGE);
                            }                            
                        }
                    }
                    else{
                    	getViasSistemaPanel().setMunicipio(null);
                    	getViasSistemaPanel().setNomVia(nomVia);
                    }
                    dispose();                    
                }
                    });
        }
        return _okCancelPanel;
    }
    
    /**
     * This method initializes
     * 
     */
    public ViasSistemaDialog(String nomVia)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        this.nomVia = nomVia;
        initialize();
        
    }
    
    public ViasSistemaDialog(String nomVia, String tipoVia)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        this.nomVia = nomVia;
        this.tipoVia = tipoVia;
        initialize();
        
    }
    
    public ViasSistemaDialog(String nomVia, String tipoVia, int codigoVia)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        this.nomVia = nomVia;
        this.tipoVia = tipoVia;
        this.codigoVia = codigoVia;
        initialize();
        
    }
    
    public ViasSistemaDialog(String nomVia, String tipoVia, String idMunicipio)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        this.nomVia = nomVia;
        this.tipoVia = tipoVia;
        this.idMunicipio = idMunicipio;
        initialize();
        
    }
    
    public ViasSistemaDialog()
    {
        this(null);
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
        this.setContentPane(getViasSistemaPanel());
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(DIM_X, DIM_Y);
        this.setTitle(I18N.get("Expedientes", "busqueda.vias.dialog.titulo"));
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
        ViasSistemaDialog dialog = new ViasSistemaDialog();
        dialog.setVisible(true);        
    }    
    
    private ViasSistemaPanel getViasSistemaPanel()
    {
        if (viasSistemaPanel == null)
        {
        	viasSistemaPanel = new ViasSistemaPanel(new GridBagLayout(), nomVia, tipoVia, codigoVia);
            viasSistemaPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));
        }
        return viasSistemaPanel;
    }  
    
    public String getVia()
    {
    	return getViasSistemaPanel().getNombreVia();
    }
    
    public String getTipoVia() {
		return getViasSistemaPanel().getTipoVia();
	}
    
    public int getCodigoVia(){
    	return getViasSistemaPanel().getCodigoVia();
    }

    public String getNomVia() {
		return getViasSistemaPanel().getNombreVia();
	}

	public void setNomVia(String nomVia) {
		this.nomVia = nomVia;
		getViasSistemaPanel().setNomVia(nomVia);
	}

	public void setTipoVia(String tipoVia) {
		this.tipoVia = tipoVia;
		getViasSistemaPanel().setTipoVia("");
	}

	public void setCodigoVia(int codigoVia) {
		this.codigoVia = codigoVia;
		getViasSistemaPanel().setCodigoVia(codigoVia);
	}

	public Municipio getMunicipio() {
		return getViasSistemaPanel().getMunicipio();
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}
}
