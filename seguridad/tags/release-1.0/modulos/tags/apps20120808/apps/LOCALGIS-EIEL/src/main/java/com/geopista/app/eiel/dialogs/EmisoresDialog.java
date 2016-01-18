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
import com.geopista.app.eiel.beans.EmisariosEIEL;
import com.geopista.app.eiel.panels.EmisariosNucleosPanel;
import com.geopista.app.eiel.panels.EmisariosPanel;
import com.geopista.app.eiel.panels.EmisariosPuntosVertidoPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class EmisoresDialog extends JDialog
{

    
    private EmisariosPanel EmisariosPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;       
    
    public static final int DIM_X = 600;
    public static final int DIM_Y = 450;
    
	private JTabbedPane tabbedPane = null;

        
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
                        if(getEmisoresPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getEmisoresPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        EmisoresDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getEmisoresPanel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                            EmisoresDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(EmisoresDialog.this,
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
    public EmisoresDialog(EmisariosEIEL elemento, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.em"));       
        this.isEditable = isEditable;
        getEmisoresPanel().loadData (elemento);
        EdicionUtils.enablePanel(getEmisoresPanel(), isEditable);
        if(elemento!=null)
        	EdicionUtils.enablePanel(getEmisoresPanel().getJPanelDatosIdentificacion(), false);
       
        getEmisoresPanel().getJTextFieldClave().setEnabled(false);
        getEmisoresPanel().getJComboBoxProvincia().setEnabled(false);
        getEmisoresPanel().getJComboBoxMunicipio().setEnabled(false);
        
        this.setLocationRelativeTo(null);

    }
    
    public EmisoresDialog()
    {
        this(false);
        this.setLocationRelativeTo(null);

    }
    public EmisoresDialog(boolean isEditable)
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
			this.tabbedPane.add(I18N.get("LocalGISEIEL", "localgiseiel.dialogs.datatabbedpane"),getEmisoresPanel());

			JScrollPane scroll1 = new JScrollPane();
			scroll1.setViewportView(getEmisariosNucleosPanel());
			this.tabbedPane.add(I18N.get("LocalGISEIEL", "localgiseiel.dialogs.nucleostabbedpane"), scroll1);
			JScrollPane scroll2 = new JScrollPane();
			scroll2.setViewportView(getEmisariosPuntosVertidoPanel());
			this.tabbedPane.add(I18N.get("LocalGISEIEL", "localgiseiel.dialogs.puntovertidotabbedpane"), scroll2);
			
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
        	this.getEmisariosNucleosPanel().tabbedChanged(getEmisoresPanel().getEmisorData());
        }
        if (sel == 2){
        	this.getEmisariosPuntosVertidoPanel().tabbedChanged(getEmisoresPanel().getEmisorData());
        }
	}

   
    public static void main(String[] args)
    {
        EmisoresDialog dialog = 
            new EmisoresDialog();
        dialog.setVisible(true);
        
    }
    
    public EmisariosPanel getEmisoresPanel()
    {
        if (EmisariosPanel == null)
        {
        	EmisariosPanel = new EmisariosPanel(new GridBagLayout());
        	EmisariosPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return EmisariosPanel;
    }
    
    public EmisariosEIEL getEmisor(EmisariosEIEL elemento)
    {
    	return getEmisoresPanel().getEmisor(elemento);
    }
    
    
    private EmisariosPuntosVertidoPanel emisariosPuntoVertidoPanel = null;
	public EmisariosPuntosVertidoPanel getEmisariosPuntosVertidoPanel(){
		if (emisariosPuntoVertidoPanel == null){
			emisariosPuntoVertidoPanel = new EmisariosPuntosVertidoPanel();
			
		}
		return emisariosPuntoVertidoPanel;
	}
	private EmisariosNucleosPanel emisariosNucleosPanel = null;
	public EmisariosNucleosPanel getEmisariosNucleosPanel(){
		if (emisariosNucleosPanel == null){
			emisariosNucleosPanel = new EmisariosNucleosPanel();
			
		}
		return emisariosNucleosPanel;
	}
}
