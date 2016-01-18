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
import com.geopista.app.eiel.beans.TramosConduccionEIEL;
import com.geopista.app.eiel.panels.TramosConduccionNucleosPanel;
import com.geopista.app.eiel.panels.TramosConduccionPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class TramosConduccionDialog extends JDialog
{

    
    private TramosConduccionPanel tramosConduccionPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private JTabbedPane tabbedPane = null;
    private boolean isEditable = false;       
    
    public static final int DIM_X = 700;
    public static final int DIM_Y = 450;
        
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
                        if(getTramosConduccionPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getTramosConduccionPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                		TramosConduccionDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getTramosConduccionPanel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                    		TramosConduccionDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(TramosConduccionDialog.this,
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
    public TramosConduccionDialog(TramosConduccionEIEL elemento, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.tc"));       
        this.isEditable = isEditable;
        getTramosConduccionPanel().loadData (elemento);
        EdicionUtils.enablePanel(getTramosConduccionPanel(), isEditable);
        
        //Deshabilitamos la clave, el codigo de provincia y el codigo de municipio
        getTramosConduccionPanel().getJTextFieldClave().setEnabled(false);
        getTramosConduccionPanel().getJComboBoxProvincia().setEnabled(false);
        getTramosConduccionPanel().getJComboBoxMunicipio().setEnabled(false);
        
        this.setLocationRelativeTo(null);

    }
    
    public TramosConduccionDialog()
    {
        this(false);
        this.setLocationRelativeTo(null);

    }
    public TramosConduccionDialog(boolean isEditable)
    {
        this(null, isEditable);
        this.setLocationRelativeTo(null);

    }
	private JTabbedPane getTabbedPane() {
		if (this.tabbedPane == null){
			this.tabbedPane = new JTabbedPane();
			this.tabbedPane.add(I18N.get("LocalGISEIEL", "localgiseiel.dialogs.datatabbedpane"),getTramosConduccionPanel());
			JScrollPane scroll = new JScrollPane();
			scroll.setViewportView(getTramosConduccionNucleosPanel());
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
        	this.getTramosConduccionNucleosPanel().tabbedChanged(getTramosConduccionPanel().getTramosConduccionData());
        }
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
//        this.setContentPane(getTramosConduccionPanel());
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
   
    public static void main(String[] args)
    {
    	TramosConduccionDialog dialog = 
            new TramosConduccionDialog();
        dialog.setVisible(true);
        
    }
    
    public TramosConduccionPanel getTramosConduccionPanel()
    {
        if (tramosConduccionPanel == null)
        {
        	tramosConduccionPanel = new TramosConduccionPanel(new GridBagLayout());
        	tramosConduccionPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return tramosConduccionPanel;
    }
    private TramosConduccionNucleosPanel tramosConduccionNucleosPanel = null;
	public TramosConduccionNucleosPanel getTramosConduccionNucleosPanel(){
		if (tramosConduccionNucleosPanel == null){
			tramosConduccionNucleosPanel = new TramosConduccionNucleosPanel();
			
		}
		return tramosConduccionNucleosPanel;
	}
    
    public TramosConduccionEIEL getTramosConduccion(TramosConduccionEIEL elemento)
    {
    	return getTramosConduccionPanel().getTramosConduccion(elemento);
    }
    
}
