/**
 * CaptacionesDialog.java
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
import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.panels.CaptacionesNucleosPanel;
import com.geopista.app.eiel.panels.CaptacionesPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class CaptacionesDialog extends JDialog
{

    
    private CaptacionesPanel captacionesPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private JTabbedPane tabbedPane = null;
    private boolean isEditable = false;       
    
    public static final int DIM_X = 800;
    public static final int DIM_Y = 550;
        
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
                        if(getCaptacionesPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getCaptacionesPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        CaptacionesDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getCaptacionesPanel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                            CaptacionesDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }

                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(CaptacionesDialog.this,
                                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"));
                            return;
                        }
                    }
                    else if (_okCancelPanel.wasCancelPressed()){
                    	 dispose(); 
                    }
                    else if (!isEditable) {
                            JOptionPane.showMessageDialog(CaptacionesDialog.this,
                                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.localgiseiel.mensajes.no_editable"));
                            return;
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
    public CaptacionesDialog(CaptacionesEIEL elemento, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.ca"));       
        this.isEditable = isEditable;
        getCaptacionesPanel().loadData (elemento);

        EdicionUtils.enablePanel(getCaptacionesPanel(), isEditable);
        EdicionUtils.enablePanel(getCaptacionesNucleosPanel(), true);
        getCaptacionesNucleosPanel().getJTableListaNucleos().setEnabled(true);
        
      //El panel de identificacion lo bloqueamos
        if(elemento!=null){
        	EdicionUtils.enablePanel(getCaptacionesPanel().getJPanelDatosIdentificacion(), false);
        	EdicionUtils.enablePanel(getCaptacionesNucleosPanel().getJPanelIdentCaptacion(), false);
        }
       
        //Deshabilitamos la clave, el codigo de provincia y el codigo de municipio
        getCaptacionesPanel().getJTextFieldClave().setEnabled(false);
    	getCaptacionesPanel().getJComboBoxProvincia().setEnabled(true);
    	getCaptacionesPanel().getJComboBoxMunicipio().setEnabled(true);
          
        
        if (!isEditable){
        	_okCancelPanel.setCancelEnabled(true);
        	//_okCancelPanel.setOKEnabled(true);
        }
        this.setLocationRelativeTo(null);
    }
    
    public CaptacionesDialog()
    {
        this(false);
        this.setLocationRelativeTo(null);
    }
    public CaptacionesDialog(boolean isEditable)
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
			this.tabbedPane.add(I18N.get("LocalGISEIEL", "localgiseiel.dialogs.datatabbedpane"),getCaptacionesPanel());
			JScrollPane scroll = new JScrollPane();
			scroll.setViewportView(getCaptacionesNucleosPanel());
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
        	this.getCaptacionesNucleosPanel().tabbedChanged(getCaptacionesPanel().getCaptacionData());
        }
	}
    
   
    public static void main(String[] args)
    {
        CaptacionesDialog dialog = 
            new CaptacionesDialog();
        dialog.setVisible(true);
        
    }
    
    public CaptacionesPanel getCaptacionesPanel()
    {
        if (captacionesPanel == null)
        {
        	captacionesPanel = new CaptacionesPanel(new GridBagLayout());
        	captacionesPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return captacionesPanel;
    }
    
    private CaptacionesNucleosPanel captacionesNucleosPanel = null;
	public CaptacionesNucleosPanel getCaptacionesNucleosPanel(){
		if (captacionesNucleosPanel == null){
			captacionesNucleosPanel = new CaptacionesNucleosPanel();
			
		}
		return captacionesNucleosPanel;
	}
    
    public CaptacionesEIEL getCaptacion(CaptacionesEIEL elemento)
    {
    	return getCaptacionesPanel().getCaptacion(elemento);
    }
    
}
