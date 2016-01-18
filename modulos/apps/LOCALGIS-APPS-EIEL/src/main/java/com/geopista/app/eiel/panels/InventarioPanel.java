/**
 * InventarioPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.geopista.app.inventario.ConstantesEIEL;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

public class InventarioPanel extends JPanel{
	
    public static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

	public JPanel jPanelInventario = null;
	public JLabel jLabelEpigInventario = null;
	public JLabel jLabelNumInventario = null;
	public JLabel jLabelTitularidadMunicipal = null;
	
	public ComboBoxEstructuras jComboBoxEpigInventario = null;
	public JComboBox jComboBoxNumInventario = null;
	public ComboBoxEstructuras jComboBoxTitularidadMunicipal = null;	
	
    public InventarioPanel(GridBagLayout layout) {
        super(layout);
	}


	public InventarioPanel() {
		super();
	}


	public ComboBoxEstructuras getJComboBoxTitularidadMunicipal()
    { 
        if (jComboBoxTitularidadMunicipal == null)
        {
            Estructuras.cargarEstructura("eiel_Titularidad Municipal");
            jComboBoxTitularidadMunicipal = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxTitularidadMunicipal.setPreferredSize(new Dimension(100, 20));
            jComboBoxEpigInventario.setEnabled(false);
            jComboBoxTitularidadMunicipal.addActionListener(new ActionListener()
            {
            	public void actionPerformed(ActionEvent e)
            	{   

            		if (jComboBoxTitularidadMunicipal.getSelectedPatron() != null && jComboBoxTitularidadMunicipal.getSelectedPatron().equals(ConstantesEIEL.TITULARIDAD_MUNICIPAL_SI))
            		{
            			jComboBoxEpigInventario.setEnabled(true);
            			jComboBoxNumInventario.setEnabled(false);
            
            		}
            		else
            		{
            			jComboBoxEpigInventario.setSelectedIndex(0);
            			jComboBoxEpigInventario.setEnabled(false);
            			jComboBoxNumInventario.removeAllItems();
            			jComboBoxNumInventario.setEnabled(false);
            			
            		}
            	}
            });
        }
        return jComboBoxTitularidadMunicipal;        
    }
    

    public ComboBoxEstructuras getJComboBoxEpigInventario()
    { 
        if (jComboBoxEpigInventario == null)
        {
            Estructuras.cargarEstructura("Tipo de bien patrimonial EIEL");
            jComboBoxEpigInventario = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxEpigInventario.setPreferredSize(new Dimension(100, 20));
            
            jComboBoxEpigInventario.addActionListener(new ActionListener()
            {
            	public void actionPerformed(ActionEvent e)
            	{   

            		if (jComboBoxTitularidadMunicipal.getSelectedPatron() != null && jComboBoxTitularidadMunicipal.getSelectedPatron().equals(ConstantesEIEL.TITULARIDAD_MUNICIPAL_SI)){           			
	            		if (jComboBoxEpigInventario.getSelectedIndex()==0)
	            		{
	            			jComboBoxNumInventario.removeAllItems();
	            			jComboBoxNumInventario.setEnabled(false);
	            		}
	            		else
	            		{
    
            				jComboBoxNumInventario.setEnabled(true);
            				EdicionOperations oper = new EdicionOperations();          			
            				EdicionUtils.cargarLista(getJComboBoxNumInventario(), oper.obtenerNumInventario(Integer.parseInt(jComboBoxEpigInventario.getSelectedPatron())));
            			}
            		}
	            	else{
	            		jComboBoxNumInventario.setEnabled(false);
	            		jComboBoxEpigInventario.setEnabled(false);
            		}
            	}
            });
        }
        return jComboBoxEpigInventario;        
    }    
    
    
    /* Rellenar con los Inventarios del epígrafe seleccionado:*/
    public JComboBox getJComboBoxNumInventario()
    {
        if (jComboBoxNumInventario == null)
        {
            jComboBoxNumInventario  = new JComboBox();
       //     jComboBoxNumInventario.setRenderer(new UbicacionListCellRenderer());  
            jComboBoxNumInventario.setEnabled(false);
            jComboBoxNumInventario.setPreferredSize(new Dimension(100, 20));
        }
        
        return jComboBoxNumInventario;
    }    

    public JPanel getJPanelDatosInventario()
    {
        if (jPanelInventario == null)
        {   
        	jPanelInventario = new JPanel(new GridBagLayout());
        	jPanelInventario.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.inventario"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	
            jLabelEpigInventario  = new JLabel("", JLabel.CENTER); 
            jLabelEpigInventario.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.epigrafe")); 
            
            jLabelTitularidadMunicipal  = new JLabel("", JLabel.CENTER); 
            jLabelTitularidadMunicipal.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.titularidadMunicipal")); 
            
            jLabelNumInventario  = new JLabel("", JLabel.CENTER); 
            jLabelNumInventario.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.numeroInventario"));
                               
            jPanelInventario.add(jLabelEpigInventario,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInventario.add(getJComboBoxEpigInventario(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInventario.add(jLabelTitularidadMunicipal,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInventario.add(getJComboBoxTitularidadMunicipal(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInventario.add(jLabelNumInventario,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInventario.add(getJComboBoxNumInventario(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
        }
        return jPanelInventario;
    }
    
}
