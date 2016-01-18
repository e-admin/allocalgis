/**
 * Depuradoras1Dialog.java
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
import com.geopista.app.eiel.beans.Depuradora1EIEL;
import com.geopista.app.eiel.panels.Depuradora1NucleosPanel;
import com.geopista.app.eiel.panels.Depuradoras1Panel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class Depuradoras1Dialog extends JDialog
{

    
    private Depuradoras1Panel depuradoras1Panel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private JTabbedPane tabbedPane = null;
    private boolean isEditable = false;       
    
    public static final int DIM_X = 920;
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
                        if(getDepuradoras1Panel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getDepuradoras1Panel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        Depuradoras1Dialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getDepuradoras1Panel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                            Depuradoras1Dialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(Depuradoras1Dialog.this,
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
    public Depuradoras1Dialog(Depuradora1EIEL elemento, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.d1"));       
        this.isEditable = isEditable;
        getDepuradoras1Panel().loadData (elemento);
        if (getDepuradoras1Panel().getJComboBoxEpigInventario().isEnabled())    	
        	EdicionUtils.enablePanel(getDepuradoras1Panel(), isEditable);
        else{
        	EdicionUtils.enablePanel(getDepuradoras1Panel(), isEditable);
        	EdicionUtils.enablePanel(getDepuradorasNucleosPanel(), true);
        	getDepuradorasNucleosPanel().getJTableListaNucleos().setEnabled(true);
        	getDepuradoras1Panel().getJComboBoxEpigInventario().setEnabled(false);
        	getDepuradoras1Panel().getJComboBoxNumInventario().setEnabled(false);
        }
        if(elemento!=null){
        	EdicionUtils.enablePanel(getDepuradoras1Panel().getJPanelDatosIdentificacion(), false);
        	EdicionUtils.enablePanel(getDepuradorasNucleosPanel().getJPanelIdentDepuradora1(), false);
        }
        if (!isEditable){
        	_okCancelPanel.setCancelEnabled(true);
        	//_okCancelPanel.setOKEnabled(true);
        }
        getDepuradoras1Panel().getJTextFieldClave().setEnabled(false);
        getDepuradoras1Panel().getJComboBoxProvincia().setEnabled(true);
        getDepuradoras1Panel().getJComboBoxMunicipio().setEnabled(true);
        
        this.setLocationRelativeTo(null);
    }
    
    public Depuradoras1Dialog()
    {
        this(false);
        this.setLocationRelativeTo(null);
    }
    public Depuradoras1Dialog(boolean isEditable)
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
			this.tabbedPane.add(I18N.get("LocalGISEIEL", "localgiseiel.dialogs.datatabbedpane"),getDepuradoras1Panel());
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
        	this.getDepuradorasNucleosPanel().tabbedChanged(getDepuradoras1Panel().getDepuradora1Data());
        }
	}
   
    public static void main(String[] args)
    {
        Depuradoras1Dialog dialog = 
            new Depuradoras1Dialog();
        dialog.setVisible(true);
        
    }
    
    public Depuradoras1Panel getDepuradoras1Panel()
    {
        if (depuradoras1Panel == null)
        {
        	depuradoras1Panel = new Depuradoras1Panel(new GridBagLayout());
        	depuradoras1Panel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return depuradoras1Panel;
    }
    
    private Depuradora1NucleosPanel depuradora1NucleosPanel = null;
	public Depuradora1NucleosPanel getDepuradorasNucleosPanel(){
		if (depuradora1NucleosPanel == null){
			depuradora1NucleosPanel = new Depuradora1NucleosPanel();
			
		}
		return depuradora1NucleosPanel;
	}
    
    public Depuradora1EIEL getDepuradora1(Depuradora1EIEL elemento)
    {
    	return getDepuradoras1Panel().getDepuradora1(elemento);
    }
    
}
