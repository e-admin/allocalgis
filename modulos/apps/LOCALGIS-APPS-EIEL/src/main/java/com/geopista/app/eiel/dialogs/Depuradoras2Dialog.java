/**
 * Depuradoras2Dialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.Depuradora2EIEL;
import com.geopista.app.eiel.panels.Depuradora1NucleosPanel;
import com.geopista.app.eiel.panels.Depuradoras2Panel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class Depuradoras2Dialog extends JDialog
{

    
    private Depuradoras2Panel depuradoras2Panel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private JTabbedPane tabbedPane = null;
    private boolean isEditable = false;       
    
    public static final int DIM_X = 870;
    public static final int DIM_Y = 650;
        
    private OKCancelPanel getOkCancelPanel()
    {
        if (_okCancelPanel == null)
        {
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    
                    if (_okCancelPanel.wasOKPressed() && isEditable)
                    {
                        if(getDepuradoras2Panel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getDepuradoras2Panel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        Depuradoras2Dialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getDepuradoras2Panel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                            Depuradoras2Dialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(Depuradoras2Dialog.this,
                                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"));
                            return;
                        }
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
    public Depuradoras2Dialog(Depuradora2EIEL elemento, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.d2"));       
        this.isEditable = isEditable;
        getDepuradoras2Panel().loadData (elemento);
        if (getDepuradoras2Panel().getJComboBoxEpigInventario().isEnabled())    	
        	EdicionUtils.enablePanel(getDepuradoras2Panel(), isEditable);
        else{
        	EdicionUtils.enablePanel(getDepuradoras2Panel(), isEditable);
        	EdicionUtils.enablePanel(getDepuradorasNucleosPanel(), true);
        	getDepuradorasNucleosPanel().getJTableListaNucleos().setEnabled(true);
        	getDepuradoras2Panel().getJComboBoxEpigInventario().setEnabled(false);
        	getDepuradoras2Panel().getJComboBoxNumInventario().setEnabled(false);
        }
        if(elemento!=null){
        	EdicionUtils.enablePanel(getDepuradoras2Panel().getJPanelDatosIdentificacion(), false);
        	EdicionUtils.enablePanel(getDepuradorasNucleosPanel().getJPanelIdentDepuradora1(), false);
        }
        
        getDepuradoras2Panel().getJTextFieldClave().setEnabled(false);
        getDepuradoras2Panel().getJComboBoxProvincia().setEnabled(true);
        getDepuradoras2Panel().getJComboBoxMunicipio().setEnabled(true);

        
        this.setLocationRelativeTo(null);
    }
    
    public Depuradoras2Dialog()
    {
        this(false);
        
        this.setLocationRelativeTo(null);
    }
    public Depuradoras2Dialog(boolean isEditable)
    {
        this(null, isEditable);
        
        this.setLocationRelativeTo(null);
    }
    
   
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(String title)
    {
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setModal(true);
        this.setContentPane(getTabbedPane());
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(DIM_X, DIM_Y);
        this.setTitle(title);
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
    
	private JTabbedPane getTabbedPane() {
		if (this.tabbedPane == null){
			this.tabbedPane = new JTabbedPane();
			this.tabbedPane.add(I18N.get("LocalGISEIEL", "localgiseiel.dialogs.datatabbedpane"),getDepuradoras2Panel());
			JScrollPane scroll = new JScrollPane();
			scroll.setViewportView(getDepuradorasNucleosPanel());
			this.tabbedPane.add(I18N.get("LocalGISEIEL", "localgiseiel.dialogs.nucleostabbedpane"), scroll);
			
			tabbedPane.addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent e) {
					onTabbedPageChange(e);
				}
			});

		}
		return tabbedPane;
	}
    
    private void onTabbedPageChange(ChangeEvent e) {
		JTabbedPane pane = (JTabbedPane)e.getSource();
		// Get current tab
        int sel = pane.getSelectedIndex();
        if (sel == 1){
        	this.getDepuradorasNucleosPanel().tabbedChanged(getDepuradoras2Panel().getDepuradora2Data());
        }
	}
    
   
    public static void main(String[] args)
    {
        Depuradoras2Dialog dialog = 
            new Depuradoras2Dialog();
        dialog.setVisible(true);
        
    }
    
    public Depuradoras2Panel getDepuradoras2Panel()
    {
        if (depuradoras2Panel == null)
        {
        	depuradoras2Panel = new Depuradoras2Panel(new GridBagLayout());
        	depuradoras2Panel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return depuradoras2Panel;
    }
    
    private Depuradora1NucleosPanel depuradoraNucleosPanel = null;
	public Depuradora1NucleosPanel getDepuradorasNucleosPanel(){
		if (depuradoraNucleosPanel == null){
			depuradoraNucleosPanel = new Depuradora1NucleosPanel();
			
		}
		return depuradoraNucleosPanel;
	}
	
    
    public Depuradora2EIEL getDepuradora2(Depuradora2EIEL elemento)
    {
    	return getDepuradoras2Panel().getDepuradora2(elemento);
    }
    
}
