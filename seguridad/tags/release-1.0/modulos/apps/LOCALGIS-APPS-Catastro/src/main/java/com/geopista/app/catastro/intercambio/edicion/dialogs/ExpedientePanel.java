package com.geopista.app.catastro.intercambio.edicion.dialogs;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import com.geopista.app.catastro.intercambio.edicion.BienInmuebleExtendedInfoDialog;
import com.geopista.app.catastro.intercambio.edicion.FincaExtendedInfoDialog;
import com.geopista.app.catastro.intercambio.edicion.RazonSocialExtendedInfoDialog;
import com.geopista.app.catastro.intercambio.edicion.utils.TableBienInmuebleCatastroModel;
import com.geopista.app.catastro.intercambio.edicion.utils.TableFincaCatastroModel;
import com.geopista.app.catastro.intercambio.edicion.utils.TableLocalModel;
import com.geopista.app.catastro.intercambio.edicion.utils.TablePersonaModel;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.Persona;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.utilidades.TableSorted;
import com.vividsolutions.jump.I18N;


public class ExpedientePanel extends JPanel
{
    
    private JPanel jPanelDatosFinca = null;
    private JPanel jPanelDatosBienInmueble = null;
    private JLabel jLabelCodigoUC = null;
    private JPanel jPanelDatosLocal = null;
    private JPanel jPanelDatosTitular = null;
    private JButton jButtonMasDatosFinca = null;
    private JButton jButtonNuevaFinca = null;
    private JButton jButtonEliminarFinca = null;
    private JScrollPane jScrollPaneFincas = null;
    private JTable jTableFincas = null;
    private JPanel jPanelBotoneraFinca = null;
    private JScrollPane jScrollPaneLocal = null;
    private JTable jTableLocal = null;
    private JScrollPane jScrollPaneTitular = null;
    private JTable jTableTitular = null;
    private JScrollPane jScrollPaneBienInmueble = null;
    private JTable jTableBienInmueble = null;
    private JPanel jPanelBotoneraBienInmueble;
    private JButton jButtonEliminarInmueble;
    private JButton jButtonNuevoInmueble;
    private JButton jButtonDefinirRepresentante;
    private JButton jButtonMasDatosInmueble;
    private JPanel jPanelBotoneraLocal;
    private JPanel jPanelBotoneraTitular;
    private JButton jButtonEliminarTitular;
    private JButton jButtonNuevoTitular;
    private JButton jButtonMasDatosTitular;
    private JButton jButtonEliminarLocal;
    private JButton jButtonNuevoLocal;
    private JButton jButtonNuevoLocalComun;
    private JButton jButtonVistaCultivo;
    private JButton jButtonMasDatosLocales;
    
    
    private TableFincaCatastroModel tablefincamodel;
    private TableBienInmuebleCatastroModel tablebimodel;
    private TableLocalModel tablelocalmodel;
    private TablePersonaModel tablepersonamodel;
    
    public ExpedientePanel()
    {
        super();
        initialize();
    }
    
    private void initialize()
    {
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
        
        this.setLayout(new GridBagLayout());
        this.setSize(new java.awt.Dimension(800,575));
        this.add(getJPanelDatosFinca(), 
                new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
                        GridBagConstraints.BOTH, new Insets(0, 5, 0, 5),80, 0));
        this.add(getJPanelDatosBienInmueble(), 
                new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
                        GridBagConstraints.BOTH, new Insets(0, 5, 0, 5),150, 0));
    }
    
    /**
     * This method initializes jPanelDatosFinca	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosFinca()
    {
        if (jPanelDatosFinca == null)
        {
            jLabelCodigoUC = new JLabel("", JLabel.CENTER); //$NON-NLS-1$
            jLabelCodigoUC.setText(I18N.get("Expedientes", "expediente.panel.datosfinca.codigouc")); //$NON-NLS-1$
            
            GridBagLayout layout = new GridBagLayout();
            jPanelDatosFinca = new JPanel(layout);
            
            jPanelDatosFinca.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "expediente.panel.datosfinca.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); //$NON-NLS-1$
                       
            jPanelDatosFinca.add(getJScrollPaneFincas(), 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
                            GridBagConstraints.BOTH, new Insets(0, 3, 0, 3), 80, 0));
            jPanelDatosFinca.add(getJPanelBotoneraFinca(), 
                    new GridBagConstraints(0, 1, 4, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
                            GridBagConstraints.HORIZONTAL, new Insets(0, 3, 0, 3), 0, 0));
            
          
        }
        return jPanelDatosFinca;
    }
    
   
    /**
     * This method initializes jPanelDatosBienInmueble	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosBienInmueble()
    {
        if (jPanelDatosBienInmueble == null)
        {
            jPanelDatosBienInmueble = new JPanel(new GridBagLayout());
            jPanelDatosBienInmueble.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "expediente.panel.datosbieninmueble.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); //$NON-NLS-1$
            jPanelDatosBienInmueble.add(getJScrollPaneBienInmueble(), 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.BOTH, new Insets (0,3,0,3), 100,0));
            jPanelDatosBienInmueble.add(getJPanelBotoneraBienInmueble(), 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.BOTH, new Insets (0,3,0,3), 0,0));
            
            
            jPanelDatosBienInmueble.add(getJPanelDatosLocal(), 
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, 
                            GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,3,0,3), 0,80));
            jPanelDatosBienInmueble.add(getJPanelDatosTitular(), 
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, 
                            GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,3,0,3), 0,80));
            
        }
        return jPanelDatosBienInmueble;
    }
    
    /**
     * This method initializes jPanelDatosLocal	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosLocal()
    {
        if (jPanelDatosLocal == null)
        {   
       
            jPanelDatosLocal = new JPanel(new GridBagLayout());
            jPanelDatosLocal.setBorder(BorderFactory.createTitledBorder(I18N.get("Expedientes", "expediente.panel.datoslocal.titulo"))); //$NON-NLS-1$
            jPanelDatosLocal.add(getJScrollPaneLocal(), 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets (0,5,0,5), 0,0));
            
            jPanelDatosLocal.add(getJPanelBotoneraLocal(), 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            
        }
        return jPanelDatosLocal;
    }
    
    /**
     * This method initializes jPanelDatosTitular	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosTitular()
    {
        if (jPanelDatosTitular == null)
        {         
            jPanelDatosTitular = new JPanel(new GridBagLayout());
            jPanelDatosTitular.setBorder(BorderFactory.createTitledBorder(I18N.get("Expedientes", "expediente.panel.datostitular.titulo"))); //$NON-NLS-1$
           
            jPanelDatosTitular.add(getJScrollPaneTitular(), 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets (0,5,0,5), 0,0));
            
            jPanelDatosTitular.add(getJPanelBotoneraTitular(), 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
                        
        }
        return jPanelDatosTitular;
    }
    
   
    /**
     * This method initializes jButtonMasDatosFinca	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonMasDatosFinca()
    {
        if (jButtonMasDatosFinca == null)
        {
            jButtonMasDatosFinca = new JButton();
            jButtonMasDatosFinca.setText(I18N.get("Expedientes", "expediente.boton.datosfinca.masdatos")); //$NON-NLS-1$
            jButtonMasDatosFinca.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    FincaExtendedInfoDialog feif = new FincaExtendedInfoDialog();
                    feif.setVisible(true);                        
                }
                    });
        }
        return jButtonMasDatosFinca;
    }

    /**
     * This method initializes jButtonNuevaFinca	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonNuevaFinca()
    {
        if (jButtonNuevaFinca == null)
        {
            jButtonNuevaFinca = new JButton();
            jButtonNuevaFinca.setText(I18N.get("Expedientes", "expediente.boton.datosfinca.nueva")); //$NON-NLS-1$
            jButtonNuevaFinca.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    FincaExtendedInfoDialog feif = new FincaExtendedInfoDialog();
                    feif.setVisible(true);                        
                }
                    });
        }
        return jButtonNuevaFinca;
    }

    /**
     * This method initializes jButtonEliminarFinca	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonEliminarFinca()
    {
        if (jButtonEliminarFinca == null)
        {
            jButtonEliminarFinca = new JButton();
            jButtonEliminarFinca.setText(I18N.get("Expedientes", "expediente.boton.datosfinca.eliminar")); //$NON-NLS-1$
        }
        return jButtonEliminarFinca;
    }

    
    /**
     * This method initializes jButtonMasDatosInmueble 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonMasDatosInmueble()
    {
        if (jButtonMasDatosInmueble == null)
        {
            jButtonMasDatosInmueble = new JButton();
            jButtonMasDatosInmueble.setText(I18N.get("Expedientes", "expediente.boton.datosinmueble.masdatos")); //$NON-NLS-1$
            jButtonMasDatosInmueble.addActionListener(new ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    BienInmuebleExtendedInfoDialog dialog = new BienInmuebleExtendedInfoDialog();
                    dialog.setVisible(true);                        
                }
                    });
        }
        return jButtonMasDatosInmueble;
    }
    
    /**
     * This method initializes jButtonDefinirRepresentante 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonDefinirRepresentante()
    {
        if (jButtonDefinirRepresentante == null)
        {
            jButtonDefinirRepresentante = new JButton();
            jButtonDefinirRepresentante.setText(I18N.get("Expedientes", "expediente.boton.datosinmueble.definirrepresentante")); //$NON-NLS-1$
            jButtonDefinirRepresentante.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    RazonSocialExtendedInfoDialog dialog = 
                        new RazonSocialExtendedInfoDialog(RazonSocialExtendedInfoDialog.MODO_REPRESENTANTE);
                    dialog.setVisible(true);                        
                }
                    });
        }
        return jButtonDefinirRepresentante;
    }
    
    /**
     * This method initializes jButtonNuevoInmueble 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonNuevoInmueble()
    {
        if (jButtonNuevoInmueble == null)
        {
            jButtonNuevoInmueble = new JButton();
            jButtonNuevoInmueble.setText(I18N.get("Expedientes", "expediente.boton.datosinmueble.nuevo")); //$NON-NLS-1$
            jButtonNuevoInmueble.addActionListener(new ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    BienInmuebleExtendedInfoDialog dialog = new BienInmuebleExtendedInfoDialog();
                    dialog.setVisible(true);                        
                }
                    });
        }
        return jButtonNuevoInmueble;
    }
    
    /**
     * This method initializes jButtonEliminarInmueble 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonEliminarInmueble()
    {
        if (jButtonEliminarInmueble == null)
        {
            jButtonEliminarInmueble = new JButton();
            jButtonEliminarInmueble.setText(I18N.get("Expedientes", "expediente.boton.datosinmueble.eliminar")); //$NON-NLS-1$
        }
        return jButtonEliminarInmueble;
    }
    
    /**
     * This method initializes jScrollPaneFincas	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneFincas()
    {
        if (jScrollPaneFincas == null)
        {
            jScrollPaneFincas = new JScrollPane();
            jScrollPaneFincas.setViewportView(getJTableFincas());
        }
        return jScrollPaneFincas;
    }

    /**
     * This method initializes jTableFincas	
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getJTableFincas()
    {
        if (jTableFincas == null)
        {
            jTableFincas = new JTable();

            tablefincamodel = new TableFincaCatastroModel();
            
            TableSorted tblSorted= new TableSorted(tablefincamodel);
            tblSorted.setTableHeader(jTableFincas.getTableHeader());
            jTableFincas.setModel(tblSorted);
            jTableFincas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableFincas.setCellSelectionEnabled(false);
            jTableFincas.setColumnSelectionAllowed(false);
            jTableFincas.setRowSelectionAllowed(true);
            jTableFincas.getTableHeader().setReorderingAllowed(false);
            
            ArrayList lst = new ArrayList();
            lst.add(new FincaCatastro());
            ((TableFincaCatastroModel)((TableSorted)jTableFincas.getModel()).
                    getTableModel()).setData(lst);
            
            
            /*
            EdicionOperations oper = new EdicionOperations();
            try
            {
                ArrayList lstVias = oper.obtenerViasCatastro();
                ((TableViasCatastroModel)((TableSorted)jTableViasCatastro.getModel()).getTableModel()).setData(lstVias);
                
            } catch (DataException e1)
            {                
                e1.printStackTrace();
            }
            */
        }
        return jTableFincas;
    }

    /**
     * This method initializes jPanelBotoneraFinca	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelBotoneraFinca()
    {
        if (jPanelBotoneraFinca == null)
        {
            jPanelBotoneraFinca = new JPanel(new GridBagLayout());
            
            jPanelBotoneraFinca.add(getJButtonMasDatosFinca(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraFinca.add(getJButtonNuevaFinca(), 
                    new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraFinca.add(getJButtonEliminarFinca(), 
                    new GridBagConstraints(5, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
          
            jPanelBotoneraFinca.add(new JPanel(),
                    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 0, 5, 0), 0, 0));        
        }
        return jPanelBotoneraFinca;
    }

    /**
     * This method initializes jScrollPaneLocal	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneLocal()
    {
        if (jScrollPaneLocal == null)
        {
            jScrollPaneLocal = new JScrollPane();
            jScrollPaneLocal.setViewportView(getJTableLocal());
        }
        return jScrollPaneLocal;
    }

    /**
     * This method initializes jTableLocal	
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getJTableLocal()
    {
        if (jTableLocal == null)
        {
            jTableLocal = new JTable();

            tablelocalmodel = new TableLocalModel();
            
            TableSorted tblSorted= new TableSorted(tablelocalmodel);
            tblSorted.setTableHeader(jTableLocal.getTableHeader());
            jTableLocal.setModel(tblSorted);
            jTableLocal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableLocal.setCellSelectionEnabled(false);
            jTableLocal.setColumnSelectionAllowed(false);
            jTableLocal.setRowSelectionAllowed(true);
            jTableLocal.getTableHeader().setReorderingAllowed(false);
            
            ArrayList lst = new ArrayList();
            lst.add(new ConstruccionCatastro());
            ((TableLocalModel)((TableSorted)jTableLocal.getModel()).
                    getTableModel()).setData(lst);
            
            
            /*
            EdicionOperations oper = new EdicionOperations();
            try
            {
                ArrayList lstVias = oper.obtenerViasCatastro();
                ((TableViasCatastroModel)((TableSorted)jTableViasCatastro.getModel()).getTableModel()).setData(lstVias);
                
            } catch (DataException e1)
            {                
                e1.printStackTrace();
            }
            */
        }
        return jTableLocal;
    }

    /**
     * This method initializes jScrollPaneTitular	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneTitular()
    {
        if (jScrollPaneTitular == null)
        {
            jScrollPaneTitular = new JScrollPane();
            jScrollPaneTitular.setViewportView(getJTableTitular());
        }
        return jScrollPaneTitular;
    }

    /**
     * This method initializes jTableTitular	
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getJTableTitular()
    {
        if (jTableTitular == null)
        {
            jTableTitular = new JTable();

            tablepersonamodel = new TablePersonaModel();
            
            TableSorted tblSorted= new TableSorted(tablepersonamodel);
            tblSorted.setTableHeader(jTableTitular.getTableHeader());
            jTableTitular.setModel(tblSorted);
            jTableTitular.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableTitular.setCellSelectionEnabled(false);
            jTableTitular.setColumnSelectionAllowed(false);
            jTableTitular.setRowSelectionAllowed(true);
            jTableTitular.getTableHeader().setReorderingAllowed(false);

            ArrayList lst = new ArrayList();
            lst.add(new Persona());
            ((TablePersonaModel)((TableSorted)jTableTitular.getModel()).
                    getTableModel()).setData(lst);
            
            
            /*
            EdicionOperations oper = new EdicionOperations();
            try
            {
                ArrayList lstVias = oper.obtenerViasCatastro();
                ((TableViasCatastroModel)((TableSorted)jTableViasCatastro.getModel()).getTableModel()).setData(lstVias);
                
            } catch (DataException e1)
            {                
                e1.printStackTrace();
            }
            */
        }
        return jTableTitular;
    }

    /**
     * This method initializes jScrollPaneBienInmueble	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneBienInmueble()
    {
        if (jScrollPaneBienInmueble == null)
        {
            jScrollPaneBienInmueble = new JScrollPane();
            jScrollPaneBienInmueble.setViewportView(getJTableBienInmueble());
        }
        return jScrollPaneBienInmueble;
    }

    /**
     * This method initializes jTableBienInmueble	
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getJTableBienInmueble()
    {
        if (jTableBienInmueble == null)
        {
            jTableBienInmueble = new JTable();

            tablebimodel = new TableBienInmuebleCatastroModel();
            
            TableSorted tblSorted= new TableSorted(tablebimodel);
            tblSorted.setTableHeader(jTableBienInmueble.getTableHeader());
            jTableBienInmueble.setModel(tblSorted);
            jTableBienInmueble.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableBienInmueble.setCellSelectionEnabled(false);
            jTableBienInmueble.setColumnSelectionAllowed(false);
            jTableBienInmueble.setRowSelectionAllowed(true);
            jTableBienInmueble.getTableHeader().setReorderingAllowed(false);

            ArrayList lst = new ArrayList();
            lst.add(new BienInmuebleCatastro());
            ((TableBienInmuebleCatastroModel)((TableSorted)jTableBienInmueble.getModel()).
                    getTableModel()).setData(lst);
            
            /*
            EdicionOperations oper = new EdicionOperations();
           
            
            try
            {
                
                ArrayList lst = oper.obtenerFincasExpediente(ex);
                ((TableBienInmuebleCatastroModel)((TableSorted)jTableBienInmueble.getModel()).
                        getTableModel()).setData(lst);
                       
                
            } catch (DataException e1)
            {                
                e1.printStackTrace();
            }
            */
        }
        return jTableBienInmueble;
    }
    
    
    /**
     * This method initializes jPanelBotoneraBienInmueble  
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanelBotoneraBienInmueble()
    {
        if (jPanelBotoneraBienInmueble == null)
        {
            jPanelBotoneraBienInmueble = new JPanel(new GridBagLayout());
            
            jPanelBotoneraBienInmueble.add(getJButtonMasDatosInmueble(), 
                    new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraBienInmueble.add(getJButtonDefinirRepresentante(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraBienInmueble.add(getJButtonNuevoInmueble(), 
                    new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraBienInmueble.add(getJButtonEliminarInmueble(), 
                    new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
          
            jPanelBotoneraBienInmueble.add(new JPanel(),
                    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 0, 5, 0), 0, 0));        
        }
        return jPanelBotoneraBienInmueble;
    }

    
    /**
     * This method initializes jPanelBotoneraLocal  
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanelBotoneraLocal()
    {
        if (jPanelBotoneraLocal == null)
        {
            jPanelBotoneraLocal = new JPanel(new GridBagLayout());
            
            jPanelBotoneraLocal.add(getJButtonMasDatosLocales(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraLocal.add(getJButtonVistaCultivo(), 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraLocal.add(getJButtonNuevoLocalComun(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraLocal.add(getJButtonNuevoLocal(), 
                    new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraLocal.add(getJButtonEliminarLocal(), 
                    new GridBagConstraints(5, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
          
            jPanelBotoneraLocal.add(new JPanel(),
                    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 0, 5, 0), 0, 0));        
        }
        return jPanelBotoneraLocal;
    }

    /**
     * This method initializes jPanelBotoneraTitular  
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanelBotoneraTitular()
    {
        if (jPanelBotoneraTitular == null)
        {
            jPanelBotoneraTitular = new JPanel(new GridBagLayout());
            
            jPanelBotoneraTitular.add(getJButtonMasDatosTitular(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
                    
            jPanelBotoneraTitular.add(getJButtonNuevoTitular(), 
                    new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraTitular.add(getJButtonEliminarTitular(), 
                    new GridBagConstraints(5, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
          
            jPanelBotoneraTitular.add(new JPanel(),
                    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 0, 5, 0), 0, 0));        
        }
        return jPanelBotoneraTitular;
    }

    /**
     * This method initializes jButtonMasDatosLocales 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonMasDatosLocales()
    {
        if (jButtonMasDatosLocales == null)
        {
            jButtonMasDatosLocales = new JButton();
            jButtonMasDatosLocales.setText(I18N.get("Expedientes", "expediente.boton.datoslocal.masdatos")); //$NON-NLS-1$
        }
        return jButtonMasDatosLocales;
    }
    
    /**
     * This method initializes jButtonVistaCultivo 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonVistaCultivo()
    {
        if (jButtonVistaCultivo == null)
        {
            jButtonVistaCultivo = new JButton();
            jButtonVistaCultivo.setText(I18N.get("Expedientes", "expediente.boton.datoslocal.vistacultivo")); //$NON-NLS-1$
        }
        return jButtonVistaCultivo;
    }
    
    /**
     * This method initializes jButtonNuevoLocalComun 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonNuevoLocalComun()
    {
        if (jButtonNuevoLocalComun == null)
        {
            jButtonNuevoLocalComun = new JButton();
            jButtonNuevoLocalComun.setText(I18N.get("Expedientes", "expediente.boton.datoslocal.nuevocomun")); //$NON-NLS-1$
        }
        return jButtonNuevoLocalComun;
    }
    
    /**
     * This method initializes jButtonNuevoLocal 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonNuevoLocal()
    {
        if (jButtonNuevoLocal == null)
        {
            jButtonNuevoLocal = new JButton();
            jButtonNuevoLocal.setText(I18N.get("Expedientes", "expediente.boton.datoslocal.nuevo")); //$NON-NLS-1$
        }
        return jButtonNuevoLocal;
    }
    
    /**
     * This method initializes jButtonEliminarLocal 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonEliminarLocal()
    {
        if (jButtonEliminarLocal == null)
        {
            jButtonEliminarLocal = new JButton();
            jButtonEliminarLocal.setText(I18N.get("Expedientes", "expediente.boton.datoslocal.eliminar")); //$NON-NLS-1$
        }
        return jButtonEliminarLocal;
    }
    
    /**
     * This method initializes jButtonMasDatosTitular 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonMasDatosTitular()
    {
        if (jButtonMasDatosTitular == null)
        {
            jButtonMasDatosTitular = new JButton();
            jButtonMasDatosTitular.setText(I18N.get("Expedientes", "expediente.boton.datostitular.masdatos")); //$NON-NLS-1$
            jButtonMasDatosTitular.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    RazonSocialExtendedInfoDialog dialog = new RazonSocialExtendedInfoDialog(RazonSocialExtendedInfoDialog.MODO_TITULAR);
                    dialog.setVisible(true);                        
                }
                    });
        }
        return jButtonMasDatosTitular;
    }
    
    /**
     * This method initializes jButtonNuevoTitular 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonNuevoTitular()
    {
        if (jButtonNuevoTitular == null)
        {
            jButtonNuevoTitular = new JButton();
            jButtonNuevoTitular.setText(I18N.get("Expedientes", "expediente.boton.datostitular.nuevo")); //$NON-NLS-1$
            jButtonNuevoTitular.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    RazonSocialExtendedInfoDialog dialog = new RazonSocialExtendedInfoDialog(RazonSocialExtendedInfoDialog.MODO_TITULAR);
                    dialog.setVisible(true);                        
                }
                    });
        }
        return jButtonNuevoTitular;
    }
    
    /**
     * This method initializes jButtonEliminarTitular 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonEliminarTitular()
    {
        if (jButtonEliminarTitular == null)
        {
            jButtonEliminarTitular = new JButton();
            jButtonEliminarTitular.setText(I18N.get("Expedientes", "expediente.boton.datostitular.eliminar")); //$NON-NLS-1$
        }
        return jButtonEliminarTitular;
    }
    
    
    
    public Expediente recopilarDatos()
    {
        Expediente exp = new Expediente();
        
        
      
        return exp;
    }

    
}  //  @jve:decl-index=0:visual-constraint="3,-86"
