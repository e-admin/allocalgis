package com.geopista.app.catastro.gestorcatastral;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.gestorcatastral.images.IconLoader;
import com.geopista.app.catastro.intercambio.edicion.dialogs.GestionExpedientePanel;
import com.geopista.app.catastro.intercambio.importacion.ImportarFINRetornoMasivo;
import com.geopista.app.catastro.intercambio.importacion.ImportarFINSalida;
import com.geopista.app.catastro.intercambio.importacion.ImportarInfoGraficaPanel;
import com.geopista.app.catastro.intercambio.importacion.ImportarInfoIntercambio;
import com.geopista.app.catastro.intercambio.importacion.ImportarMunicipios;
import com.geopista.app.catastro.intercambio.importacion.ImportarPadronMunicipal;
import com.geopista.app.catastro.intercambio.importacion.ImportarPonencia;
import com.geopista.app.catastro.intercambio.importacion.MostrarFINRetornoMasivo;
import com.geopista.app.catastro.intercambio.importacion.MostrarFINSalida;
import com.geopista.app.catastro.intercambio.importacion.MostrarMunicipios;
import com.geopista.app.catastro.intercambio.importacion.MostrarPadronMunicipal;
import com.geopista.app.catastro.intercambio.importacion.MostrarPonencia;
import com.geopista.app.catastro.intercambio.importacion.MostrarProgresoIntercambio;
import com.geopista.app.catastro.intercambio.importacion.ScreenComponent;
import com.geopista.app.catastro.intercambio.importacion.ValidarFichero;
import com.geopista.app.catastro.intercambio.importacion.dialogs.FileValidationPanel;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.IdBienInmueble;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.ui.wizard.WizardComponent;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.GUIUtil;


public class ModuloCatastralFrame extends JFrame
{
    
    private static AppContext application = (AppContext) AppContext.getApplicationContext();
    
    private JMenuBar jMenuBarCatastro = null;
    private JMenu jMenuRegistro = null;
    private JMenu jMenuImportacion = null;
    private JMenu jMenuGenerarFicheros = null;
    private JMenu jMenuAyuda = null;
    private JMenuItem jMenuItemCrearExpediente = null;
    private JMenuItem jMenuItemConsultarExpediente = null;
    private JMenuItem jMenuItemModificarExpediente = null;
    private JMenuItem jMenuItemAsociarParcelas = null;
    private JMenuItem jMenuItemValidarParcelas = null;
    private JMenuItem jMenuItemConsultarIntercambios = null;
    private JMenuItem jMenuItemFicherosMunicipios = null;
    private JMenuItem jMenuItemPonenciaValores = null;
    private JMenuItem jMenuItemPadronCatastral = null;
    private JMenuItem jMenuItemFinSalida = null;
    private JMenuItem jMenuItemFinRetornoMasivo = null;
    private JMenuItem jMenuItemFinEntradaRelativo = null;
    private JMenuItem jMenuItemAcercaDe = null;
    
    private JDialog dialog = null;
   
    private JPanel jPanelStatus = null;
    
    private JPanel contentPane = null;    
    private JPanel centerPane = null;
   
    /**
     * Guarda el último componente cargado en la pantalla
     */
    private Component oldComponent;

    private JMenuItem jMenuItemFicheroIntercambioInfoTitularidad = null;
    private JMenuItem jMenuItemFicheroIntercambioInfoCatastral = null;
    private JMenuItem jMenuItemFicheroIntercambioInfoCatastralRetorno = null;

    private JMenuItem jMenuItemAsociarInfoGrafica = null;
    
    
    public static final int DIM_X = 800;
    public static final int DIM_Y = 600;
    public static final Rectangle PICTURE_BORDER = new Rectangle(15, 5, 100, 440);
    public static final String BIG_PICTURE_LOCATION = "catastro.png";
    public static final String SMALL_PICTURE_LOCATION = "catastro_small.png";
    public static final String CATASTRO_LOGO = "catastro_logo.gif";
    
    //private JMenu jMenuEnvioFicheros = null;
    
    /**
     * This method initializes 
     * 
     */
    public ModuloCatastralFrame() {
        super();
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Couldn't use system look and feel.");
        }
       
        initialize();   
        show();
        
        AppContext.getApplicationContext().setMainFrame(this);
    }
    
    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        
        this.setIconImage(IconLoader.icon(CATASTRO_LOGO).getImage());
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.ModuloCatastrali18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("ModuloCatastral",bundle);
        
        this.setSize(new Dimension(DIM_X,DIM_Y-25));
        this.setContentPane(getJContentPane());
        this.setJMenuBar(getJMenuBarCatastro());
        this.setTitle(I18N.get("ModuloCatastral","modulocatastral.titulo"));   
        
        
        //this.getContentPane().setLayout(new BorderLayout());
        //Container cp = this.getContentPane() ;
        //cp.setLayout(new CardLayout());        
        addWindowListener( new WindowAdapter() {
            public void windowClosing( WindowEvent evt ) {
                System.exit(0);
            }
        } );
        application.setMainFrame((JFrame)this);       
    }
    
   
    /**
     * This method initializes contentPane    
     *  
     * @return java.awt.Container 
     */
    private Container getJContentPane()
    {
        /*
        desktopPane = new javax.swing.JDesktopPane();        
        return desktopPane;
        */
        
        if (contentPane == null)
        {
            contentPane = new JPanel(new BorderLayout());
            contentPane.setPreferredSize(new Dimension(DIM_X, DIM_Y-25));
            contentPane.setOpaque(true);
            contentPane.setBackground(Color.GRAY);
            
            contentPane.removeAll();
            centerPane = null;
            //Agrega el panel central como componente 0
            contentPane.add(getJCenterPane(), BorderLayout.CENTER);
            //Agrega el panel de status al sur del contenedor como componente 1
            contentPane.add(getJPanelStatus(), BorderLayout.SOUTH);
            
            
        }
        return contentPane;        
    }
    
    
    public void setCenterPane(JPanel panel)
    {
        contentPane.removeAll();
        //Componente 0
        contentPane.add(panel, BorderLayout.CENTER);
        //Componente 1
        contentPane.add(getJPanelStatus(), BorderLayout.SOUTH);
        
    }
    
    public Container getCenterPane()
    {
        return (Container)getContentPane().getComponent(0);
    }
    
    /**
     * This method initializes contentPane    
     *  
     * @return java.awt.Container 
     */
    private Container getJCenterPane()
    {        
        if (centerPane == null)
        {
            centerPane = new JPanel(new CardLayout());
            //centerPane.setPreferredSize(new Dimension(DIM_X, DIM_Y));
            centerPane.setOpaque(true);
            centerPane.setBackground(Color.GRAY);                       
        }
        return centerPane;        
    }
    
    
    private JPanel getJPanelStatus ()
    {
        if (jPanelStatus == null)
        {
            jPanelStatus = new JPanel();
            jPanelStatus.setLayout(new BorderLayout()); 
            JLabel lblMessage = new JLabel("Mensaje");
            jPanelStatus.add(lblMessage, BorderLayout.CENTER);
            jPanelStatus.setBorder(BorderFactory.createLoweredBevelBorder());
        }
        return jPanelStatus;
    }
    
    /**
     * This method initializes jMenuBarCatastro	
     * 	
     * @return javax.swing.JMenuBar	
     */
    private JMenuBar getJMenuBarCatastro()
    {
        if (jMenuBarCatastro == null)
        {
            jMenuBarCatastro = new JMenuBar();
            jMenuBarCatastro.add(getJMenuRegistro());
            jMenuBarCatastro.add(getJMenuImportacion());
            jMenuBarCatastro.add(getJMenuGenerarFicheros());
            //jJMenuBarCatastro.add(getJMenuEnvioFicheros());
            jMenuBarCatastro.add(getJMenuAyuda());      
            jMenuBarCatastro.setBorderPainted(false);
        }
        return jMenuBarCatastro;
    }
    
    /**
     * This method initializes jMenuRegistro	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getJMenuRegistro()
    {
        if (jMenuRegistro == null)
        {
            jMenuRegistro = new JMenu();
            jMenuRegistro.setText(I18N.get("ModuloCatastral","modulocatastral.menu.gestionexpedientes"));
            jMenuRegistro.add(getJMenuItemCrearExpediente());
            jMenuRegistro.add(getJMenuItemConsultarExpediente());
            jMenuRegistro.add(getJMenuItemModificarExpediente());
            jMenuRegistro.add(getJMenuItemAsociarParcelas());
            jMenuRegistro.add(getJMenuItemValidarParcelas());
            jMenuRegistro.add(getJMenuItemConsultarIntercambios());
        }
        return jMenuRegistro;
    }
    
    /**
     * This method initializes jMenuImportacion	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getJMenuImportacion()
    {
        if (jMenuImportacion == null)
        {
            jMenuImportacion = new JMenu();
            jMenuImportacion.setText(I18N.get("ModuloCatastral","modulocatastral.menu.importacionficheros"));
            jMenuImportacion.add(getJMenuFicherosMunicipios());
            jMenuImportacion.add(getJMenuItemPonenciaValores());
            jMenuImportacion.add(getJMenuItemPadronCatastral());
            jMenuImportacion.add(getJMenuItemFinSalida());
            jMenuImportacion.add(getJMenuItemFinRetornoMasivo()); 
            
            jMenuImportacion.addSeparator();
            
            jMenuImportacion.add(getJMenuItemFicheroIntercambioInfoTitularidad());
            jMenuImportacion.add(getJMenuItemFicheroIntercambioInfoCatastral());
            jMenuImportacion.add(getJMenuItemFicheroIntercambioInfoCatastralRetorno());
                   
            jMenuImportacion.addSeparator();
            jMenuImportacion.add(getJMenuItemAsociarInfoGrafica());
            
            
        }
        return jMenuImportacion;
    }
    
    /**
     * This method initializes jMenuGenerarFicheros	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getJMenuGenerarFicheros()
    {
        if (jMenuGenerarFicheros == null)
        {
            jMenuGenerarFicheros = new JMenu();
            jMenuGenerarFicheros.setText(I18N.get("ModuloCatastral","modulocatastral.menu.generacionyenvio"));
            jMenuGenerarFicheros.add(getJMenuItemFinEntradaRelativo());
        }
        return jMenuGenerarFicheros;
    }
    
    /**
     * This method initializes jMenuAyuda	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getJMenuAyuda()
    {
        if (jMenuAyuda == null)
        {
            jMenuAyuda = new JMenu();
            jMenuAyuda.setText(I18N.get("ModuloCatastral","modulocatastral.menu.ayuda"));
            jMenuAyuda.add(getJMenuItemAcercaDe());            
        }
        return jMenuAyuda;
    }
    
    /**
     * This method initializes jMenuItemCrearExpediente	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getJMenuItemCrearExpediente()
    {
        if (jMenuItemCrearExpediente == null)
        {
            jMenuItemCrearExpediente = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.crearexpedientelocal"));
            jMenuItemCrearExpediente.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {

                    Expediente exp = new Expediente();
                    ArrayList lstReferencias = new ArrayList();
                    IdBienInmueble id = new IdBienInmueble();
                    id.setIdBienInmueble("1234567890ABCD0001BC");
                    BienInmuebleCatastro bic = new BienInmuebleCatastro();
                    bic.setIdBienInmueble(id);
                    lstReferencias.add(bic);

                    id = new IdBienInmueble();
                    id.setIdBienInmueble("0987654321EFGH0002BC");
                    bic = new BienInmuebleCatastro();
                    bic.setIdBienInmueble(id);
                    lstReferencias.add(bic);
                    
                    exp.setListaReferencias(lstReferencias);
                    
                    GestionExpedientePanel g = new GestionExpedientePanel(exp, false);
                    openComponent(g, g.getJPanelBotones(), false);                    
                }
                    });
            
        }
        return jMenuItemCrearExpediente;
    }
    
    /**
     * This method initializes jMenuItemConsultarExpediente	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getJMenuItemConsultarExpediente()
    {
        if (jMenuItemConsultarExpediente == null)
        {
            jMenuItemConsultarExpediente = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.consultarexpediente"));
            jMenuItemConsultarExpediente.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {

                    Expediente exp = new Expediente();
                    ArrayList lstReferencias = new ArrayList();
                    ReferenciaCatastral rc = new ReferenciaCatastral("1234567890ABCD");
                    FincaCatastro fc = new FincaCatastro();
                    fc.setRefFinca(rc);
                    lstReferencias.add(fc);
                    rc = new ReferenciaCatastral("9876543210ECGH");
                    fc.setRefFinca(rc);
                    lstReferencias.add(fc);
                    
                    exp.setListaReferencias(lstReferencias);
                    
                    GestionExpedientePanel g = new GestionExpedientePanel(exp, false);
                    openComponent(g, g.getJPanelBotones(), false);                    
                }
                    });
        }
        return jMenuItemConsultarExpediente;
    }
    
    /**
     * This method initializes jMenuItemModificarExpediente	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenuItem getJMenuItemModificarExpediente()
    {
        if (jMenuItemModificarExpediente == null)
        {
            jMenuItemModificarExpediente = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.modificarexpediente"));
            jMenuItemModificarExpediente.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {

                    Expediente exp = new Expediente();
                    ArrayList lstReferencias = new ArrayList();
                    ReferenciaCatastral rc = new ReferenciaCatastral("1234567890ABCD");
                    FincaCatastro fc = new FincaCatastro();
                    fc.setRefFinca(rc);
                    lstReferencias.add(fc);
                    rc = new ReferenciaCatastral("9876543210ECGH");
                    fc.setRefFinca(rc);
                    lstReferencias.add(fc);
                    
                    exp.setListaReferencias(lstReferencias);
                    
                    GestionExpedientePanel g = new GestionExpedientePanel(exp, true);
                    openComponent(g, g.getJPanelBotones(), true);                    
                }
                    });
        }
        return jMenuItemModificarExpediente;
    }
    
    
    /**
     * This method initializes jMenuItemAsociarParcelas 
     *  
     * @return javax.swing.JMenu    
     */
    private JMenuItem getJMenuItemAsociarParcelas()
    {
        if (jMenuItemAsociarParcelas == null)
        {
            jMenuItemAsociarParcelas = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.asociarparcelas"));
        }
        return jMenuItemAsociarParcelas;
    }
    
    /**
     * This method initializes jMenuItemValidarParcelas 
     *  
     * @return javax.swing.JMenu    
     */
    private JMenuItem getJMenuItemValidarParcelas()
    {
        if (jMenuItemValidarParcelas == null)
        {
            jMenuItemValidarParcelas = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.validarparcelas"));
            jMenuItemValidarParcelas.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openComponent(new FileValidationPanel(), true);    
                    
                    
                }
                    });
        }
        return jMenuItemValidarParcelas;
    }
    
    /**
     * This method initializes jMenuItemConsultarIntercambios 
     *  
     * @return javax.swing.JMenu    
     */
    private JMenuItem getJMenuItemConsultarIntercambios()
    {
        if (jMenuItemConsultarIntercambios == null)
        {
            jMenuItemConsultarIntercambios = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.consultarintercambios"));
        }
        return jMenuItemConsultarIntercambios;
    }
    
    
    /**
     * This method initializes jMenuFicherosMunicipios	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenuItem getJMenuFicherosMunicipios()
    {
        if (jMenuItemFicherosMunicipios == null)
        {
            jMenuItemFicherosMunicipios = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.ficherosmunicipios"));
            jMenuItemFicherosMunicipios.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openWizard (new WizardPanel[] {
                            new ImportarMunicipios(), 
                            new MostrarMunicipios()}, true);                    
                }
                    });
        }
        return jMenuItemFicherosMunicipios;
    }
    
    
   
    /**
     * This method initializes jMenuItemPonenciaValores	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getJMenuItemPonenciaValores()
    {
        if (jMenuItemPonenciaValores == null)
        {
            jMenuItemPonenciaValores = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.ponenciavalores"));
            jMenuItemPonenciaValores.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openWizard (new WizardPanel[] {
                            new ImportarPonencia(), 
                            new MostrarPonencia()}, true);                    
                }
                    });
        }
        return jMenuItemPonenciaValores;
    }
    
    /**
     * This method initializes jMenuItemPadronCatastral	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getJMenuItemPadronCatastral()
    {
        if (jMenuItemPadronCatastral == null)
        {
            jMenuItemPadronCatastral = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.padroncatastral"));
            jMenuItemPadronCatastral.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openWizard (new WizardPanel[] {
                            new ImportarPadronMunicipal(), 
                            new MostrarPadronMunicipal()}, true);                    
                }
                    });
        }
        return jMenuItemPadronCatastral;
    }
    
    /**
     * This method initializes jMenuItemFinSalida	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getJMenuItemFinSalida()
    {
        if (jMenuItemFinSalida == null)
        {
            jMenuItemFinSalida = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.finsalida"));
            jMenuItemFinSalida.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openWizard (new WizardPanel[] {
                            new ImportarFINSalida(), 
                            new MostrarFINSalida(),
                            new ValidarFichero()}, true);                           
                }
                    });
        }
        return jMenuItemFinSalida;
    }
    
    /**
     * This method initializes jMenuItemFinRetornoMasivo	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getJMenuItemFinRetornoMasivo()
    {
        if (jMenuItemFinRetornoMasivo == null)
        {
            jMenuItemFinRetornoMasivo = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.finretorno"));
            jMenuItemFinRetornoMasivo.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openWizard (new WizardPanel[] {
                            new ImportarFINRetornoMasivo(), 
                            new MostrarFINRetornoMasivo(),
                            new ValidarFichero()}, true);                    
                }
                    });
        }
        return jMenuItemFinRetornoMasivo;
    }
    
    
    /**
     * This method initializes jMenuItemFicheroIntercambioInfoTitularidad   
     *  
     * @return javax.swing.JMenuItem    
     */
    private JMenuItem getJMenuItemFicheroIntercambioInfoTitularidad()
    {
        if (jMenuItemFicheroIntercambioInfoTitularidad == null)
        {
            jMenuItemFicheroIntercambioInfoTitularidad = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.infotitularidad"));
            jMenuItemFicheroIntercambioInfoTitularidad.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openModalWizard(I18N.get("ModuloCatastral","dialog.titulo.infotitularidad"), 
                            new WizardPanel[] 
                                            {
                            new ImportarInfoIntercambio(I18N.get("ModuloCatastral","panel.titulo.infotitularidad")),
                            new MostrarProgresoIntercambio(I18N.get("ModuloCatastral","panel.titulo.infotitularidad"))
                                            });                           
                }
                    });
        }
        return jMenuItemFicheroIntercambioInfoTitularidad;
    }
    
    
    /**
     * This method initializes jMenuItemFicheroIntercambioInfoCatastral   
     *  
     * @return javax.swing.JMenuItem    
     */
    private JMenuItem getJMenuItemFicheroIntercambioInfoCatastral()
    {
        if (jMenuItemFicheroIntercambioInfoCatastral == null)
        {
            jMenuItemFicheroIntercambioInfoCatastral = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.infocatastral"));
            jMenuItemFicheroIntercambioInfoCatastral.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openModalWizard(I18N.get("ModuloCatastral","dialog.titulo.infocatastral"), 
                            new WizardPanel[] 
                                            {
                            new ImportarInfoIntercambio(I18N.get("ModuloCatastral","panel.titulo.infocatastral")),
                            new MostrarProgresoIntercambio(I18N.get("ModuloCatastral","panel.titulo.infocatastral"))
                                            });                           
                }
                    });
        }
        return jMenuItemFicheroIntercambioInfoCatastral;
    }
    
    
    /**
     * This method initializes jMenuItemFicheroIntercambioInfoCatastralRetorno   
     *  
     * @return javax.swing.JMenuItem    
     */
    private JMenuItem getJMenuItemFicheroIntercambioInfoCatastralRetorno()
    {
        if (jMenuItemFicheroIntercambioInfoCatastralRetorno == null)
        {
            jMenuItemFicheroIntercambioInfoCatastralRetorno = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.inforetorno"));
            jMenuItemFicheroIntercambioInfoCatastralRetorno.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openModalWizard(I18N.get("ModuloCatastral","dialog.titulo.inforetorno"), 
                            new WizardPanel[] 
                                            {
                            new ImportarInfoIntercambio(I18N.get("ModuloCatastral","panel.titulo.inforetorno")),
                            new MostrarProgresoIntercambio(I18N.get("ModuloCatastral","panel.titulo.inforetorno"))
                                            });                           
                }
                    });
        }
        return jMenuItemFicheroIntercambioInfoCatastralRetorno;
    }
    
    /**
     * This method initializes jMenuItemAsociarInfoGrafica   
     *  
     * @return javax.swing.JMenuItem    
     */
    private JMenuItem getJMenuItemAsociarInfoGrafica()
    {
        if (jMenuItemAsociarInfoGrafica == null)
        {
            jMenuItemAsociarInfoGrafica = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.infografica"));
            jMenuItemAsociarInfoGrafica.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    ScreenComponent sc = new ScreenComponent();
                    
                    ArrayList lstRef = new ArrayList();
                    FincaCatastro fc1 = new FincaCatastro();
                    FincaCatastro fc2 = new FincaCatastro();
                    fc1.setRefFinca(new ReferenciaCatastral("ABCDEFGHIJKLMN"));
                    fc2.setRefFinca(new ReferenciaCatastral("1234567890ABCD"));
                    lstRef.add(fc1);
                    lstRef.add(fc2);
                    Expediente exp = new Expediente();
                    exp.setListaReferencias(lstRef);
                    exp.setIdExpediente(123456);
                    ImportarInfoGraficaPanel g =
                        new ImportarInfoGraficaPanel(exp);
                    sc.addComponent(g);                   
                    sc.setFillerPanel(g.getJPanelBotones());
                    
                    openModalComponent(I18N.get("ModuloCatastral","dialog.titulo.infografica"), sc);      
                    
                 /*   
                    openModalWizard(I18N.get("ModuloCatastral","dialog.titulo.infografica"), 
                            new WizardPanel[] 
                                            {
                            new ImportarInfoGraficaPanel(I18N.get("ModuloCatastral","panel.titulo.infografica"))                            
                                            });                           
                */
                }
                    });
        }
        return jMenuItemAsociarInfoGrafica;
    }
    
    
    
    /**
     * This method initializes jMenuItemFinEntradaRelativo    
     *  
     * @return javax.swing.JMenuItem    
     */
    private JMenuItem getJMenuItemFinEntradaRelativo()
    {
        if (jMenuItemFinEntradaRelativo == null)
        {
            jMenuItemFinEntradaRelativo = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.finentrada"));
        }
        return jMenuItemFinEntradaRelativo;
    }
    
    
    /**
     * This method initializes jMenuItemAcercaDe	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getJMenuItemAcercaDe()
    {
        if (jMenuItemAcercaDe == null)
        {
            jMenuItemAcercaDe = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.acercade"));
        }
        return jMenuItemAcercaDe;
    }
    
    /**
     * This method initializes jMenuEnvioFicheros	
     * 	
     * @return javax.swing.JMenu	
     */
    /*  private JMenu getJMenuEnvioFicheros()
     {
     if (jMenuEnvioFicheros == null)
     {
     jMenuEnvioFicheros = new JMenu("Envío de ficheros");
     jMenuEnvioFicheros.add(getJMenuItemFinEntradaRelativo());
     }
     return jMenuEnvioFicheros;
     }
     */
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        ModuloCatastralFrame lf = new ModuloCatastralFrame();
        lf.show();          
        
    }
   
    private void showComponent(Component d, boolean resize)
    {
        showComponent(d, null, resize);       
    }
    
    private void showComponent(Component d, JPanel p, boolean resize)
    {
        if(d instanceof WizardComponent && !((WizardComponent)d).isCanceled()
                || !(d instanceof WizardComponent))
        {            
            if (oldComponent!=null)
                getCenterPane().remove(oldComponent);
            
            getCenterPane().add(d,"appPanel");
            ((CardLayout)getCenterPane().getLayout())
                    .show(getCenterPane(),"appPanel");
            oldComponent=d;   
            
            if (p != null)
            {
                
            }
            d.setVisible(true);            
            pack();
            
            if (resize)
                this.setSize(new Dimension(DIM_X,DIM_Y-25));     
            
            //pack();
            repaint();
        }        
    }
    
    private void showComponentOver(Component d, String title)
    {
        if(d instanceof WizardComponent && !((WizardComponent)d).isCanceled())
               // || !(d instanceof WizardComponent))
        {            
            
            ((WizardComponent)d).addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    
                    String command = e.getActionCommand();
                    if ("canceled".equals(command)|| "finished".equals(command)){
                        dialog.dispose();
                    }                    
                }
            });
            
        }
        else if (d instanceof ScreenComponent)
        {
            ((ScreenComponent)d).addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    
                    String command = e.getActionCommand();
                    if ("canceled".equals(command)|| "finished".equals(command)){
                        dialog.dispose();
                    }                    
                }
            });
        }
        dialog = new JDialog(this, title, true);
        dialog.getContentPane().add(d);
       
        dialog.setSize(new Dimension(DIM_X*11/16,DIM_Y/2));  
        GUIUtil.centreOnWindow(dialog);
        dialog.setVisible(true);            
        pack();
        
    }
    
    protected void openWizard(WizardPanel[] wp, boolean resize)
    {
        if (application.isOnline()){
            WizardComponent d = new WizardComponent(application, "", null);
            d.init(wp);
            
            //Elimina el panel blanco con título que aparece en la zona superior de la pantalla
            d.setWhiteBorder(false);
            
            d.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    
                    String command = e.getActionCommand();
                    if ("canceled".equals(command)|| "finished".equals(command)){
                        contentPane = null;
                        ((JFrame)ModuloCatastralFrame.getFrames()[0]).setContentPane(getJContentPane());
                    }                    
                }
            });
           
            showComponent(d, resize);
        }
        else{
            JOptionPane.showMessageDialog(application.getMainFrame(),application.getI18nString("mensaje.no.conectado.a.la.base.datos"));
        }
    }
    
    protected void openModalWizard(String title, WizardPanel[] wp)
    {
        if (application.isOnline()){
            WizardComponent d = new WizardComponent(application, "", null);
            d.init(wp);
            
            //Elimina el panel blanco con título que aparece en la zona superior de la pantalla
            d.setWhiteBorder(false);
            
            showComponentOver(d, title);
        }
        else{
            JOptionPane.showMessageDialog(application.getMainFrame(),application.getI18nString("mensaje.no.conectado.a.la.base.datos"));
        }
    }

    
    protected void openModalComponent(String title, JPanel panel)
    {
        if (application.isOnline()){
            
            showComponentOver(panel, title);
        }
        else{
            JOptionPane.showMessageDialog(application.getMainFrame(),
                    application.getI18nString("mensaje.no.conectado.a.la.base.datos"));
        }
    }
    
    protected void openComponent (JComponent jComp, boolean resize)
    {
        openComponent(jComp, null, resize);
    }
    
    protected void openComponent(JComponent jComp, JPanel p, boolean resize)
    {
        if (application.isOnline())     
        {
            ScreenComponent sc = new ScreenComponent();
            sc.addComponent(jComp);
            sc.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    
                    String command = e.getActionCommand();
                    if ("finished".equals(command)){
                        contentPane = null;
                        ((JFrame)ModuloCatastralFrame.getFrames()[0]).setContentPane(getJContentPane());
                    }                    
                }
            });
            if (p!=null)
                sc.setFillerPanel(p);
            
            showComponent(sc, resize);
        }                    
        else        
            JOptionPane.showMessageDialog(application.getMainFrame(),application.getI18nString("mensaje.no.conectado.a.la.base.datos"));        
    }
    
}  //  @jve:decl-index=0:visual-constraint="10,10"
