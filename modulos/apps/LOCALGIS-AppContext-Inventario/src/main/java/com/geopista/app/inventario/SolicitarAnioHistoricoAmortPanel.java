/**
 * SolicitarAnioHistoricoAmortPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp_LCGIII;
import com.geopista.protocol.inventario.Const;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;
//import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;

public class SolicitarAnioHistoricoAmortPanel extends JPanel{

	private JLabel jLabelFase = null;
	private JTextField jNumberFieldFase = null;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();


	public SolicitarAnioHistoricoAmortPanel(GridBagLayout layout){
		 super(layout);
	        initialize();
	}

	 private void initialize(){      
//	        Locale loc=I18N.getLocaleAsObject();         
	        ResourceBundle bundle = AppContext.getApplicationContext().getI18NResource();
	        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
	        
//	        this.setName(I18N.get("LocalGISEIEL","localgiseiel.mpt.panel.title"));
	        
	        this.setLayout(new GridBagLayout());
//	        this.setSize(ExportMPTDialog.DIM_X,ExportMPTDialog.DIM_Y);
	        jLabelFase = new JLabel("");
	        jLabelFase.setText(I18N.get("LocalGISEIEL", "inventario.historicoamortizacion.panels.label.anioencuesta"));       
	        
	        this.add(jLabelFase,  
	        		new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
	                GridBagConstraints.CENTER, GridBagConstraints.NONE,
	                new Insets(5, 10, 5, 10), 0, 0));
	    	this.add(getJNumberFieldFase(),  
	    	        		new GridBagConstraints(0, 1, 1, 1, 1.0, 0.1,
	    	                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
	    	                new Insets(5, 20, 5, 20), 0, 0));
	        
//	        this.add(getFileChooser(), 
//	        		new GridBagConstraints(1, 0, 1, 3, 0.1, 0.1,
//	                GridBagConstraints.ABOVE_BASELINE, GridBagConstraints.BOTH,
//	                new Insets(0, 5, 0, 5), 0, 0));
//	              
	        
	 }
	 
	 private JTextField getJNumberFieldFase()
		{
		 if (jNumberFieldFase  == null)
			{
			 jNumberFieldFase  = new JTextField();
			 jNumberFieldFase.setText(UserPreferenceStore.getUserPreference(Const.KEY_ANIO_AMORTIZACION,"0000", true));
			 jNumberFieldFase.addCaretListener(new CaretListener()
				{
					public void caretUpdate(CaretEvent evt)
					{
						chequeaLongYNumCampoEdit(jNumberFieldFase, 4, aplicacion.getMainFrame());
					}
				});
			}
			return jNumberFieldFase;
		}
	 
	 public String getAnioEncuesta() {
		String anio=getJNumberFieldFase().getText();
		return anio;
	}
	    /**
	     * Checkea que el valor introducido en el campo no excede la longitud pasada
	     * por parametro y que solo contiene numeros.
	     *
	     * @param comp  El componente editable
	     * @param maxLong El tamaño maximo
	     */
	    public  void chequeaLongYNumCampoEdit(JTextComponent comp, int maxLong, JFrame parent)
	    {
	        String txt= comp.getText();
	        if(!txt.equals(""))
	        {
	            int x=-1;
	            try
	            {
	                x= Integer.parseInt(txt);
	            }
	            catch(NumberFormatException nFE)
	            {
	                JOptionPane.showMessageDialog(parent, I18N.get("RegistroExpedientes",
	                "Catastro.RegistroExpedientes.PanelExpAdminGerencia.msg3")+ " " +  maxLong + " " + I18N.get("RegistroExpedientes",
	                "Catastro.RegistroExpedientes.PanelExpAdminGerencia.msg4"));
	                UtilRegistroExp_LCGIII.retrocedeCaracter(comp,txt.length()-1);
	            }
	            if(txt.length()>maxLong )
	            {
	                UtilRegistroExp_LCGIII.retrocedeCaracter(comp, maxLong);
	            }
	        }
	    }
}
