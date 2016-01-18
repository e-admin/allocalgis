package com.geopista.app.catastro.intercambio.edicion;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.dialogs.RepartoPanel;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.RepartoCatastro;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class RepartoExtendedInfoDialog extends JDialog
{
    
    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    private RepartoPanel repartoPanel = null;
    
    private OKCancelPanel _okCancelPanel = null;    
   
    
    public static final int DIM_X = 600;
    public static final int DIM_Y = 500;
    
    
    private boolean isEditable = false;
    
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
                        if(getRepartoExtendedInfoPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getRepartoExtendedInfoPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        RepartoExtendedInfoDialog.this,
                                        errorMessage,
                                        I18N.get("Expedientes", "mensajes.novalida.reparto"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getRepartoExtendedInfoPanel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                            RepartoExtendedInfoDialog.this,
                                            errorMessage,
                                            I18N.get("Expedientes", "mensajes.norecupera.reparto"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(RepartoExtendedInfoDialog.this,
                                    I18N.get("Expedientes", "Catastro.Intercambio.Edicion.Dialogs.msgDatosMinimosYCorrectos"));
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
    public RepartoExtendedInfoDialog(boolean isEditable)
    {
        this(null, isEditable);
    }
    public RepartoExtendedInfoDialog()
    {
        this (false);
    }
    
    
    /**
     * This method initializes
     * 
     */
    public RepartoExtendedInfoDialog(Object o, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        initialize(I18N.get("Expedientes", "reparto.extended.dialog.titulo"));
        EdicionUtils.enablePanel(getRepartoExtendedInfoPanel(), isEditable);
        this.isEditable = isEditable;
        
        if (o!=null && o instanceof ConstruccionCatastro)
        {
            ConstruccionCatastro construccion = (ConstruccionCatastro)o;            
            getRepartoExtendedInfoPanel().load(construccion);
        }
        else if (o!=null && o instanceof Cultivo)
        {
            Cultivo cultivo = (Cultivo)o;            
            getRepartoExtendedInfoPanel().load(cultivo);
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
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
        
        this.setModal(true);
        this.setContentPane(getRepartoExtendedInfoPanel());
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(DIM_X, DIM_Y);
        this.setTitle(title);
        this.setResizable(true);
        this.getOkCancelPanel().setVisible(true);
        //this.getJPanelClose().setVisible(false);
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
        RepartoExtendedInfoDialog dialog = new RepartoExtendedInfoDialog();
        dialog.setVisible(true);        
    }
    
    private RepartoPanel getRepartoExtendedInfoPanel()
    {
        if (repartoPanel == null)
        {
            repartoPanel = new RepartoPanel();
            repartoPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));          
        }
        return repartoPanel;
    }
    
    public RepartoCatastro getReparto()
    {
    	return getRepartoExtendedInfoPanel().getReparto();
    }
    
    public RepartoCatastro getReparto(Object obj)
    {
    	return getRepartoExtendedInfoPanel().getReparto(obj);
    }
}
