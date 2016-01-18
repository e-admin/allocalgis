/*
 * Created on 21-jun-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.catastro;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import org.jdom.*;
import org.jdom.input.*;
import com.geopista.app.AppContext;
import com.geopista.ui.components.UrlTextField;
import com.geopista.util.ApplicationContext;
import java.io.*;
import java.util.*;
import javax.swing.DefaultListModel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author dbaeza
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class GeopistaMostrarDatosCatastroXML extends JDialog
{
    private ApplicationContext aplicacion = AppContext.getApplicationContext();

    private JTabbedPane jTabbedPane = null;

    private JPanel jPanel = null;

    private JPanel jPanel1 = null;

    private JPanel jPanel2 = null;

    private JPanel jPanel3 = null;

    private JPanel jPanel4 = null;

    private JScrollPane jScrollPane = null;

    private JLabel lblBienes = null;

    private JLabel lblNaturaleza = null;

    private JLabel lblDesNaturaleza = null;

    private JLabel lblRefCat = null;

    private JLabel lblDesRefCat = null;

    private JLabel lblProvincia = null;

    private JLabel lblDesProvincia = null;

    private JLabel lblMunicipio = null;

    private JLabel lblDesMunicipio = null;

    private JLabel lblDomicilioTributario = null;

    private JLabel lblDesDomicilioTributario = null;

    private JLabel lblAnhoCatastral = null;

    private JLabel lblDesAnhoCatastral = null;

    private JLabel lblValorCatastral = null;

    private JLabel lblDesValorCatastral = null;

    private JLabel lblValCatSue = null;

    private JLabel lblDesValCatSue = null;

    private JLabel lblValCatCon = null;

    private JLabel lblDesValCatCon = null;

    private JLabel lblUso = null;

    private JLabel lblSupAso = null;

    private JLabel lblDesSupAso = null;

    private JPanel jPanel5 = null;

    private JScrollPane jScrollPane1 = null;

    private JLabel lblNif = null;

    private JLabel lblDesNif = null;

    private JLabel lblNombreColindante = null;

    private JLabel lblDesTitularLocalizacion = null;

    private JLabel lblSuperficieColindante = null;

    private JLabel lblDesSuperficieLocalizacion = null;

    private JLabel jLabel4 = null;

    private JLabel lblDesLocalizacion = null;

    private JPanel jPanel6 = null;

    private JLabel lblDesUso = null;

    private JPanel jPanel7 = null;

    private DefaultListModel model = new DefaultListModel();

    private DefaultListModel modelColindantes = new DefaultListModel();

    private DefaultListModel modelTitulares = new DefaultListModel();

    private DefaultListModel modeloConstrucciones = new DefaultListModel();

    private DefaultListModel modelSubparcelas = new DefaultListModel();

    private JList lstBienes = new JList(model);

    private Document doc = null;

    private JList lstColindantes = new JList(modelColindantes);

    private JScrollPane jScrollPane2 = null;

    private JLabel lblListadoTitulares = null;

    private JList lstTitulares = new JList(modelTitulares);

    private JLabel lblTitularNombre = null;

    private JLabel lblDesTitularNombre = null;

    private JPanel jPanel8 = null;

    private JPanel jPanel9 = null;

    private JLabel lblDerechoTitular = null;

    private JLabel lblDomicilioFiscal = null;

    private JTextArea lblDesDerechoTitular = null;

    private JTextArea lblDesDomicilioFiscal = null;

    private JLabel lblMovimiento = null;

    private JPanel jPanel10 = null;

    private JSeparator jSeparadorMovimiento = new JSeparator(); // @jve:decl-index=0:visual-constraint="772,10"

    private JSeparator jSeparator = null;

    private JLabel lblFechaMovimiento = null;

    private JLabel lblEntidadMovimiento = null;

    private JLabel lblDesEntidadMovimiento = null;

    private JLabel lblDesFechaMovimiento = null;

    private JPanel jPanel11 = null;

    private JLabel lblFinca = null;

    private JSeparator jSeparator1 = null;

    private JLabel lblDomicilioTributarioMovimiento = null;

    private JLabel lblDesDomicilioTributarioMovimiento = null;

    private JLabel lblTipoFinca = null;

    private JLabel lblDesTipoFinca = null;

    private JSeparator jSeparator2 = null;

    private JLabel lblSuperficieSolar = null;

    private JLabel lblDesSuperficieSolar = null;

    private JLabel lblSuperficieConstruida = null;

    private JLabel lblDesSuperficieConstruida = null;

    private JSeparator jSeparator3 = null;

    private JLabel lblEscala = null;

    private JLabel lblDesEscala = null;

    private JLabel lblPlanta = null;

    private UrlTextField lblDesPlantaGeneral = null;

    private JScrollPane jScrollPane3 = null;

    private JList lstConstrucciones = new JList(modeloConstrucciones);

    private JLabel jLabel = null;

    private JPanel jPanel12 = null;

    private JLabel lblDomicilioConstruccion = null;

    private JTextArea txtDomicilioConstruccion = null;

    private JLabel lblLocalizacionUrbana = null;

    private JTextArea txtLocalizacionUrbana = null;

    private JSeparator jSeparator4 = null;

    private JLabel lblLocalizacionInterna = null;

    private JLabel lblBloque = null;

    private JLabel lblEscalera = null;

    private JLabel lblPlantaConstruccion = null;

    private JLabel lblPuerta = null;

    private JLabel lblDesBloque = null;

    private JLabel lblDesEscalera = null;

    private JLabel lblDesPlantaConstruccion = null;

    private JLabel lblDesPuerta = null;

    private JScrollPane jScrollPane4 = null;

    private JLabel lstListadoSubparcelas = null;

    private JList lstSubparcelas = new JList(modelSubparcelas);

    private JPanel jPanel13 = null;

    private JLabel lblDatosSubparcelas = null;

    private JSeparator jSeparator5 = null;

    private JLabel lblCalificacionCultivo = null;

    private JLabel lblNombreCultivo = null;

    private JLabel lblIntensidadProductiva = null;

    private JLabel lblValorCatastralSubparcela = null;

    private JLabel lblDesIntensidadProductiva = null;

    private JLabel lblDesValorCatastralSubparcela = null;

    private JSeparator jSeparator6 = null;

    private JTextArea txtDenominacionCultivo = null;

    private JTextArea txtClasificacionCultivo = null;

    private JLabel lblAgregacion = null;

    private JLabel lblconcentracion = null;

    private JLabel lblIdRustico = null;

    private JLabel lblDesAgregacion = null;

    private JLabel lblDesConcentracion = null;

    private JLabel lblDesIdRustico = null;

    private JLabel lblParaje = null;

    private JLabel lblDesParaje = null;

    private JPanel jPanel14 = null;

    private JLabel lblDomicilioTributarioRustico = null;

    private JLabel lblRusticoAgregacion = null;

    private JLabel lblRusticoConcentracion = null;

    private JLabel lblRusticoIdentificacion = null;

    private JLabel lblRusticoParaje = null;

    private JLabel lblDesRusticoAgregacion = null;

    private JLabel lblDesRusticoConcentracion = null;

    private JLabel lblDesRusticoIdentificacion = null;

    private JLabel lblDesRusticoParaje = null;

    /**
     * This method initializes
     * 
     * @throws JDOMException
     * @throws IOException 
     */
    public GeopistaMostrarDatosCatastroXML(JFrame fondo, String sCadena)
            throws JDOMException, IOException
        {
            super(fondo,AppContext.getApplicationContext().getI18nString("GeopistaMostrarDatosCatastroXML.DatosParcela") ,true);
            initialize();
            initXML(sCadena);
        }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        jSeparadorMovimiento.setSize(13, 14);
        this.setContentPane(getJTabbedPane());
        this.setSize(693, 484);
        lstSubparcelas.addListSelectionListener(new ListSelectionListener()
            {

                public void valueChanged(ListSelectionEvent e)
                {
                    txtDenominacionCultivo.setText("");
                    txtClasificacionCultivo.setText("");
                    lblDesIntensidadProductiva.setText("");
                    lblDesValorCatastralSubparcela.setText("");
                    rellenarDatosSubParcela(doc, (String) lstSubparcelas.getModel()
                            .getElementAt(lstSubparcelas.getSelectedIndex()));
                }
            });

        lstConstrucciones.addListSelectionListener(new ListSelectionListener()
            {

                public void valueChanged(ListSelectionEvent e)
                {

                    txtDomicilioConstruccion.setText("");
                    txtLocalizacionUrbana.setText("");
                    lblDesPuerta.setText("");
                    lblDesBloque.setText("");
                    lblDesPlantaConstruccion.setText("");
                    lblDesEscalera.setText("");
                    rellenarDatosConstruccion(doc, (String) lstConstrucciones.getModel()
                            .getElementAt(lstConstrucciones.getSelectedIndex()),
                            lstConstrucciones.getSelectedIndex());

                }

            });
        lstTitulares.addListSelectionListener(new ListSelectionListener()
            {

                public void valueChanged(ListSelectionEvent e)
                {
                    // Por cada Nif seleccinado buscar sus datos.
                    lblDesTitularNombre.setText("");
                    lblDesDerechoTitular.setText("");
                    lblDesDomicilioFiscal.setText("");
                    rellenarDatosTitular(doc, (String) lstTitulares.getModel()
                            .getElementAt(lstTitulares.getSelectedIndex()));
                }
            });
        lstColindantes.addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(ListSelectionEvent e)
                {
                    Element raiz = doc.getRootElement();
                    String refCat = (String) lstBienes.getModel().getElementAt(
                            lstBienes.getSelectedIndex());
                    String refColindante = (String) lstColindantes.getModel()
                            .getElementAt(lstColindantes.getSelectedIndex());
                    rellenarColindantes(doc, refCat, refColindante);
                }
            });
        lstBienes.addListSelectionListener(new ListSelectionListener()
            {

                public void valueChanged(ListSelectionEvent e)
                {
                    // Para cada vez que se haga clic en la lista de bienes
                    // rellenar los datos de Naturaleza
                    Element raiz = doc.getRootElement();
                    String refCat = (String) lstBienes.getModel().getElementAt(
                            lstBienes.getSelectedIndex());
                    // Rellenamos los datos Generales y creo que todo el resto
                    // de solapas.
                    resetarValoresVentanaCatastro(true, true, true, true, true);
                    rellenarDatosGenerales(doc, refCat);
                    modelTitulares.clear();
                    cargarTitularesParcela(doc);
                    cargarMovimientoFinca(doc);
                    rellenarListaConstrucciones(doc);
                    rellenarListaSubparcelas(doc);

                }
            });

    }

    /**
     * @throws JDOMException
     * @throws IOException 
     * 
     */
    private void initXML(String sCadena) throws JDOMException, IOException
    {
        
        SAXBuilder builder = new SAXBuilder();

        doc = builder.build(new StringReader(sCadena));

        // Rellenamos la lista de Bienes
        // Txomin
        rellenarListaBienes(doc, lstBienes);

    }

    /**
     * This method initializes jTabbedPane
     * 
     * @return javax.swing.JTabbedPane
     */
    private JTabbedPane getJTabbedPane()
    {
        if (jTabbedPane == null)
        {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.addTab(aplicacion.getI18nString("certificados.datos.generales"),
                    null, getJPanel(), null);
            jTabbedPane.addTab(aplicacion.getI18nString("certificados.titulares"), null,
                    getJPanel1(), null);
            jTabbedPane.addTab(
                    aplicacion.getI18nString("certificados.movimientos.finca"), null,
                    getJPanel2(), null);
            jTabbedPane.addTab(aplicacion.getI18nString("certificados.construcciones"),
                    null, getJPanel3(), null);
            jTabbedPane.addTab(aplicacion.getI18nString("certificados.subparcelas"),
                    null, getJPanel4(), null);
            jTabbedPane.addTab(aplicacion.getI18nString("certificados.rusticos.solapa"),
                    null, getJPanel14(), null);

        }
        return jTabbedPane;
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel()
    {
        if (jPanel == null)
        {
            lblValorCatastral = new JLabel();
            lblDesValorCatastral = new JLabel();
            lblValCatSue = new JLabel();
            lblDesValCatSue = new JLabel();
            lblValCatCon = new JLabel();
            lblDesValCatCon = new JLabel();
            lblUso = new JLabel();

            lblSupAso = new JLabel();
            lblDesSupAso = new JLabel();
            lblBienes = new JLabel();
            lblNaturaleza = new JLabel();
            lblDesNaturaleza = new JLabel();
            lblRefCat = new JLabel();
            lblDesRefCat = new JLabel();
            lblProvincia = new JLabel();
            lblMunicipio = new JLabel();
            lblDesMunicipio = new JLabel();
            lblDesProvincia = new JLabel();
            lblDomicilioTributario = new JLabel();
            lblDesDomicilioTributario = new JLabel();
            lblAnhoCatastral = new JLabel();
            lblDesAnhoCatastral = new JLabel();
            jPanel = new JPanel();
            jPanel.setLayout(null);
            lblBienes.setBounds(14, 14, 106, 17);
            lblBienes.setText(aplicacion.getI18nString("certificados.Listado.bienes"));
            lblNaturaleza.setText(aplicacion.getI18nString("certificados.Naturaleza"));
            lblDesNaturaleza.setText("");
            lblRefCat.setText(aplicacion.getI18nString("certificados.RefCatastral"));
            lblRefCat.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
            lblDesRefCat.setText("");
            lblProvincia.setText(aplicacion.getI18nString("certificados.provincia"));
            lblProvincia.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
            lblDesProvincia.setText("");
            lblMunicipio.setText(aplicacion.getI18nString("certificados.municipio"));
            lblDesMunicipio.setText("");
            lblDomicilioTributario.setText(aplicacion
                    .getI18nString("certificados.domicilio.tributario"));
            lblDomicilioTributario.setFont(new java.awt.Font("Dialog",
                    java.awt.Font.BOLD, 12));
            lblDesDomicilioTributario.setText("");
            lblAnhoCatastral.setText(aplicacion
                    .getI18nString("certificados.anho.catastral"));
            lblAnhoCatastral.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
            lblAnhoCatastral.setBounds(302, 31, 88, 15);
            lblDesAnhoCatastral.setText("");
            lblDesAnhoCatastral.setBounds(441, 31, 73, 15);
            lblValorCatastral.setText(aplicacion
                    .getI18nString("certificados.valor.catastral"));
            lblValorCatastral
                    .setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
            lblValorCatastral.setBounds(9, 9, 122, 15);
            lblDesValorCatastral.setText("");
            lblDesValorCatastral.setBounds(149, 8, 116, 15);
            lblValCatSue.setText(aplicacion
                    .getI18nString("certificados.valor.catastral.suelo"));
            lblValCatSue.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
            lblValCatSue.setBounds(9, 29, 122, 15);
            lblValCatSue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            lblValCatSue.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
            lblDesValCatSue.setText("");
            lblDesValCatSue.setBounds(149, 27, 115, 15);
            lblValCatCon.setText(aplicacion
                    .getI18nString("certificados.valor.catastral.construccion"));
            lblValCatCon.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
            lblValCatCon.setBounds(9, 48, 122, 15);
            lblDesValCatCon.setText("");
            lblDesValCatCon.setBounds(224, 68, 284, 19);
            lblUso.setText(aplicacion.getI18nString("certificados.uso"));

            lblNaturaleza.setBounds(10, 6, 64, 16);
            lblDomicilioTributario.setBounds(10, 28, 122, 16);
            lblRefCat.setBounds(10, 76, 120, 16);
            lblDesDomicilioTributario.setBounds(148, 28, 364, 16);
            lblUso.setBounds(287, 8, 33, 16);
            lblDesNaturaleza.setBounds(147, 6, 132, 16);
            lblDesRefCat.setBounds(149, 76, 366, 16);
            lblProvincia.setBounds(11, 52, 67, 16);
            lblDesProvincia.setBounds(148, 51, 131, 16);
            lblMunicipio.setBounds(290, 50, 65, 16);
            lblDesMunicipio.setBounds(361, 48, 151, 16);

            jPanel.add(getJPanel7(), null);
            lblSupAso.setText(aplicacion
                    .getI18nString("certificados.superficie.asociada"));
            lblSupAso.setBounds(302, 10, 130, 15);
            lblDesSupAso.setText("");
            lblDesSupAso.setBounds(441, 10, 74, 15);
            jPanel.add(getJScrollPane(), null);
            jPanel.add(lblBienes, null);
            jPanel.add(getJPanel5(), null);
            jPanel.add(getJPanel6(), null);
        }
        return jPanel;
    }

    /**
     * This method initializes jPanel1
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1()
    {
        if (jPanel1 == null)
        {
            lblDesTitularNombre = new JLabel();
            lblTitularNombre = new JLabel();
            lblListadoTitulares = new JLabel();
            jPanel1 = new JPanel();
            jPanel1.setLayout(null);
            lblListadoTitulares.setBounds(11, 18, 129, 14);
            lblListadoTitulares.setText(aplicacion
                    .getI18nString("certificados.listado.titulares"));
            lblTitularNombre.setBounds(167, 46, 123, 19);
            lblTitularNombre.setText(aplicacion.getI18nString("certificados.Titular"));
            lblDesTitularNombre.setBounds(302, 46, 362, 20);
            lblDesTitularNombre.setText("");
            jPanel1.add(getJScrollPane2(), null);
            jPanel1.add(lblListadoTitulares, null);
            jPanel1.add(lblTitularNombre, null);
            jPanel1.add(lblDesTitularNombre, null);
            jPanel1.add(getJPanel8(), null);
            jPanel1.add(getJPanel9(), null);
        }
        return jPanel1;
    }

    /**
     * This method initializes jPanel2
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel2()
    {
        if (jPanel2 == null)
        {
            lblMovimiento = new JLabel();
            jPanel2 = new JPanel();
            jPanel2.setLayout(null);
            lblMovimiento.setText(aplicacion.getI18nString("certificados.movimiento"));
            lblMovimiento.setBounds(8, 5, 134, 15);
            jPanel2.add(getJPanel10(), null);
            jPanel2.add(getJPanel11(), null);
        }
        return jPanel2;
    }

    /**
     * This method initializes jPanel3
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel3()
    {
        if (jPanel3 == null)
        {
            jLabel = new JLabel();
            jPanel3 = new JPanel();
            jPanel3.setLayout(null);
            jLabel.setBounds(14, 14, 150, 17);
            jLabel.setText(aplicacion
                    .getI18nString("certificados.listado.Construcciones"));
            jPanel3.add(getJScrollPane3(), null);
            jPanel3.add(jLabel, null);
            jPanel3.add(getJPanel12(), null);
        }
        return jPanel3;
    }

    /**
     * This method initializes jPanel4
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel4()
    {
        if (jPanel4 == null)
        {
            lstListadoSubparcelas = new JLabel();
            jPanel4 = new JPanel();
            jPanel4.setLayout(null);
            lstListadoSubparcelas.setBounds(8, 5, 152, 15);
            lstListadoSubparcelas.setText(aplicacion
                    .getI18nString("certificados.listado.supbarcelas"));
            jPanel4.add(getJScrollPane4(), null);
            jPanel4.add(lstListadoSubparcelas, null);
            jPanel4.add(getJPanel13(), null);
        }
        return jPanel4;
    }

    /**
     * This method initializes jScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane()
    {
        if (jScrollPane == null)
        {
            jScrollPane = new JScrollPane();
            jScrollPane.setBounds(9, 41, 147, 384);
            jScrollPane.setViewportView(getJList());
        }
        return jScrollPane;
    }

    /**
     * This method initializes jPanel5
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel5()
    {
        if (jPanel5 == null)
        {
            lblAgregacion = new JLabel();
            lblconcentracion = new JLabel();
            lblDesParaje = new JLabel();
            lblParaje = new JLabel();
            lblDesIdRustico = new JLabel();
            lblDesConcentracion = new JLabel();
            lblDesAgregacion = new JLabel();
            lblIdRustico = new JLabel();
            lblNif = new JLabel();
            jPanel5 = new JPanel();
            lblDesNif = new JLabel();
            lblDesLocalizacion = new JLabel();
            jLabel4 = new JLabel();
            lblDesSuperficieLocalizacion = new JLabel();
            lblSuperficieColindante = new JLabel();
            lblDesTitularLocalizacion = new JLabel();
            lblNombreColindante = new JLabel();
            jPanel5.setLayout(null);
            jPanel5.setBounds(160, 227, 521, 198);
            jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(
                    java.awt.Color.black, 2));
            lblNif.setBounds(128, 11, 45, 15);
            lblNif.setText("N.I.F.");
            lblDesNif.setBounds(220, 11, 290, 15);
            lblDesNif.setText("");
            lblNombreColindante.setBounds(127, 30, 62, 15);
            lblNombreColindante.setText(aplicacion.getI18nString("certificados.Nombre"));
            lblDesTitularLocalizacion.setBounds(220, 29, 291, 15);
            lblDesTitularLocalizacion.setText("");
            lblSuperficieColindante.setBounds(126, 48, 62, 15);
            lblSuperficieColindante.setText(aplicacion
                    .getI18nString("certificados.superficie"));
            lblDesSuperficieLocalizacion.setBounds(223, 49, 285, 15);
            lblDesSuperficieLocalizacion.setText("");
            jLabel4.setBounds(125, 69, 64, 15);
            jLabel4.setText(aplicacion.getI18nString("certificados.localizacion"));
            lblDesLocalizacion.setBounds(129, 89, 381, 34);
            lblDesLocalizacion.setText("");
            lblAgregacion.setBounds(12, 135, 106, 18);
            lblAgregacion.setText("JLabel");
            lblconcentracion.setBounds(12, 160, 106, 16);
            lblconcentracion.setText("JLabel");
            lblIdRustico.setBounds(292, 135, 52, 17);
            lblIdRustico.setText("JLabel");
            lblDesAgregacion.setBounds(126, 135, 160, 17);
            lblDesAgregacion.setText("JLabel");
            lblDesConcentracion.setBounds(126, 160, 160, 17);
            lblDesConcentracion.setText("JLabel");
            lblDesIdRustico.setBounds(348, 135, 162, 13);
            lblDesIdRustico.setText("JLabel");
            lblParaje.setBounds(292, 160, 52, 16);
            lblParaje.setText("JLabel");
            lblDesParaje.setBounds(348, 160, 162, 15);
            lblDesParaje.setText("JLabel");

            lblAgregacion.setText(aplicacion.getI18nString("certificados.Agregacion"));
            lblconcentracion.setText(aplicacion
                    .getI18nString("certificados.concentracion"));
            lblIdRustico
                    .setText(aplicacion.getI18nString("certificados.id.bien.rustico"));
            lblParaje.setText(aplicacion.getI18nString("certificados.paraje"));

            jPanel5.add(getJScrollPane1(), null);
            jPanel5.add(lblNif, null);
            jPanel5.add(lblDesNif, null);
            jPanel5.add(lblNombreColindante, null);
            jPanel5.add(lblDesTitularLocalizacion, null);
            jPanel5.add(lblSuperficieColindante, null);
            jPanel5.add(lblDesValCatCon, null);
            jPanel5.add(lblDesSuperficieLocalizacion, null);
            jPanel5.add(jLabel4, null);
            jPanel5.add(lblDesLocalizacion, null);
            jPanel5.add(lblAgregacion, null);
            jPanel5.add(lblconcentracion, null);
            jPanel5.add(lblIdRustico, null);
            jPanel5.add(lblDesAgregacion, null);
            jPanel5.add(lblDesConcentracion, null);
            jPanel5.add(lblDesIdRustico, null);
            jPanel5.add(lblParaje, null);
            jPanel5.add(lblDesParaje, null);
        }
        return jPanel5;
    }

    /**
     * This method initializes jScrollPane1
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane1()
    {
        if (jScrollPane1 == null)
        {
            jScrollPane1 = new JScrollPane();
            jScrollPane1.setBounds(10, 9, 110, 116);
            jScrollPane1.setViewportView(getJList2());
        }
        return jScrollPane1;
    }

    /**
     * This method initializes jList
     * 
     * @return javax.swing.JList
     */
    private JList getJList()
    {
        if (lstBienes == null)
        {
            lstBienes = new JList();
        }
        return lstBienes;
    }

    /**
     * This method initializes jPanel6
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel6()
    {
        if (jPanel6 == null)
        {
            jPanel6 = new JPanel();
            lblDesUso = new JLabel();
            jPanel6.setLayout(null);
            jPanel6.setBounds(161, 41, 522, 100);
            jPanel6.setBorder(javax.swing.BorderFactory
                    .createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
            lblDesUso.setBounds(361, 8, 156, 17);
            lblDesUso.setText("");
            jPanel6.add(lblNaturaleza, null);
            jPanel6.add(lblDomicilioTributario, null);
            jPanel6.add(lblRefCat, null);
            jPanel6.add(lblDesDomicilioTributario, null);
            jPanel6.add(lblUso, null);
            jPanel6.add(lblDesNaturaleza, null);
            jPanel6.add(lblDesRefCat, null);
            jPanel6.add(lblProvincia, null);
            jPanel6.add(lblDesProvincia, null);
            jPanel6.add(lblMunicipio, null);
            jPanel6.add(lblDesMunicipio, null);
            jPanel6.add(lblDesUso, null);

        }
        return jPanel6;
    }

    /**
     * This method initializes jPanel7
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel7()
    {
        if (jPanel7 == null)
        {
            jPanel7 = new JPanel();
            jPanel7.setLayout(null);
            jPanel7.setBounds(162, 146, 522, 73);
            jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(
                    java.awt.Color.black, 2));
            jPanel7.add(lblSupAso, null);
            jPanel7.add(lblDesSupAso, null);
            jPanel7.add(lblValorCatastral, null);
            jPanel7.add(lblDesValorCatastral, null);
            jPanel7.add(lblValCatSue, null);
            jPanel7.add(lblDesValCatSue, null);
            jPanel7.add(lblValCatCon, null);
            jPanel7.add(lblAnhoCatastral, null);
            jPanel7.add(lblDesAnhoCatastral, null);
        }
        return jPanel7;
    }

    /**
     * Rellena la lista de los bienes que se ubican en la parcela consultada
     * 
     * @param doc
     *            Documento XML
     * @param JList
     *            lstBienes Lista de todos los bienes de catastro
     * @return null
     */

    private void rellenarListaBienes(Document doc, JList lstBienes)
    {
        Element raiz = doc.getRootElement(); // Es el sopa:Envelope
        Element soapBody = raiz.getChild("Body", raiz.getNamespace());
        Namespace nm = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element consultarRC = soapBody.getChild("Consultar_RC_Out", nm);
        Namespace nm2 = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element ovc_consulta_datos_out = consultarRC.getChild("ovc_consulta_datos_out",
                nm2);
        Element resp = ovc_consulta_datos_out.getChild("resp", ovc_consulta_datos_out
                .getNamespace());
        Element dsa = resp.getChild("dsa", resp.getNamespace());

        // Debo llegar al dsa
        Element lbi = dsa.getChild("lbi", dsa.getNamespace());
        List bienes = lbi.getChildren("dbi", lbi.getNamespace());
        Iterator iBienes = bienes.iterator();
        while (iBienes.hasNext())
        {
            Element dbi = (Element) iBienes.next();
            Element bir = dbi.getChild("bir", dbi.getNamespace());
            Element idine = bir.getChild("idine", bir.getNamespace());
            Element rc = idine.getChild("rc", idine.getNamespace());
            // Acceder a la Referencia Catastral.
            Element pc1 = rc.getChild("pc1", rc.getNamespace());
            Element pc2 = rc.getChild("pc2", rc.getNamespace());
            int pos = lstBienes.getModel().getSize();
            model.add(pos, (String) (pc1.getText() + pc2.getText()));
        }
        if (model.getSize() != 0)
            lstBienes.setSelectedIndex(0);
    }

    /**
     * Rellena los datos generales del bien seleccinado
     * 
     * @param doc
     *            Documento XML
     * @param refCat
     *            Referencia Catastral del Bien Inmueble
     * @return null
     */
    private void rellenarDatosGenerales(Document doc, String refCat)
    {

        Element raiz = doc.getRootElement(); // Es el sopa:Envelope
        Element soapBody = raiz.getChild("Body", raiz.getNamespace());
        Namespace nm = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element consultarRC = soapBody.getChild("Consultar_RC_Out", nm);
        Namespace nm2 = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element ovc_consulta_datos_out = consultarRC.getChild("ovc_consulta_datos_out",
                nm2);
        Element resp = ovc_consulta_datos_out.getChild("resp", ovc_consulta_datos_out
                .getNamespace());
        Element dsa = resp.getChild("dsa", resp.getNamespace());

        Element lbi = dsa.getChild("lbi", dsa.getNamespace());
        List bienes = lbi.getChildren("dbi", lbi.getNamespace());
        Iterator iBienes = bienes.iterator();
        while (iBienes.hasNext())
        {
            Element dbi = (Element) iBienes.next();
            Element bir = dbi.getChild("bir", dbi.getNamespace());
            Element idine = bir.getChild("idine", bir.getNamespace());
            Element rc = idine.getChild("rc", idine.getNamespace());
            // Acceder a la Referencia Catastral.
            Element pc1 = rc.getChild("pc1", rc.getNamespace());
            Element pc2 = rc.getChild("pc2", rc.getNamespace());
            if ((pc1.getText() + pc2.getText()).equals(refCat))
            {
                // Se ha encontrado los valores
                // Naturaleza
                Element cn = idine.getChild("cn", idine.getNamespace());
                lblDesNaturaleza.setText(cn.getText());
                // Referencia Castral Completa
                Element car = rc.getChild("car", rc.getNamespace());
                Element cc1 = rc.getChild("cc1", rc.getNamespace());
                Element cc2 = rc.getChild("cc2", rc.getNamespace());
                lblDesRefCat.setText(pc1.getText() + pc2.getText() + car.getText()
                        + cc1.getText() + cc2.getText());

                // Provincia y Municipio
                Element loine = idine.getChild("loine", idine.getNamespace());
                Element cpr = loine.getChild("cpr", loine.getNamespace());
                Element cm = loine.getChild("cm", loine.getNamespace());
                lblDesMunicipio.setText(cm.getText());
                lblDesProvincia.setText(cpr.getText());

                // Domicilio Rústico

                Element dt = bir.getChild("dt", bir.getNamespace());
                if (dt != null)
                {
                    Element lorus = dt.getChild("lorus", dt.getNamespace());
                    Element cma = lorus.getChild("cma", lorus.getNamespace());
                    Element czc = lorus.getChild("czc", lorus.getNamespace());
                    Element coid = lorus.getChild("coid", lorus.getNamespace());
                    Element cpa = coid.getChild("cpa", coid.getNamespace());
                    Element cpo = coid.getChild("cpo", coid.getNamespace());
                    Element npa = lorus.getChild("npa", lorus.getNamespace());

                    lblDesRusticoAgregacion.setText(cma.getText());
                    lblDesRusticoIdentificacion.setText(cpo.getText() + cpa.getText());
                    lblDesRusticoConcentracion.setText(czc.getText());
                    lblDesRusticoParaje.setText(npa.getText());
                }

                // Domicilio Tributario
                Element ltd = bir.getChild("ldt", bir.getNamespace());
                lblDesDomicilioTributario.setText(ltd == null ? "" : ltd.getText());

                // Valor y año catastral
                Element debi = bir.getChild("debi", bir.getNamespace());
                Element avc = debi.getChild("avc", debi.getNamespace());
                Element vc = debi.getChild("vc", debi.getNamespace());
                lblDesAnhoCatastral.setText(avc.getText());
                lblDesValorCatastral.setText(vc.getText());

                // Valores Catastrales de urbana
                Element vcu = debi.getChild("vcu", debi.getNamespace());
                if (vcu != null)
                {
                    Element vcs = vcu.getChild("vcs", vcu.getNamespace());
                    Element vcc = vcu.getChild("vcc", vcu.getNamespace());
                    lblDesValCatCon.setText(vcc == null ? "" : vcc.getText());
                    lblDesValCatSue.setText(vcs == null ? "" : vcs.getText());
                }
                // Uso y superficie asociada al cargo
                Element uso = debi.getChild("uso", debi.getNamespace());
                Element sfc = debi.getChild("sfc", debi.getNamespace());
                lblDesUso.setText(uso == null ? "" : uso.getText());
                lblDesSupAso.setText(sfc == null ? "" : sfc.getText());

                // Cargamos la lista de colindantes
                modelColindantes.clear();
                lblDesNif.setText("");
                lblDesTitularLocalizacion.setText("");
                lblDesSuperficieLocalizacion.setText("");
                lblDesLocalizacion.setText("");

                Element lcol = bir.getChild("lcol", bir.getNamespace());

                if (lcol != null)
                {
                    List colindantes = lcol.getChildren("col", lcol.getNamespace());

                    Iterator iCol = colindantes.iterator();
                    while (iCol.hasNext())
                    {
                        Element col = (Element) iCol.next();
                        // Para cada colindante localizar su refCat
                        Element rfin = col.getChild("rfin", col.getNamespace());
                        Element pc11 = rfin.getChild("pc1", rfin.getNamespace());
                        Element pc22 = rfin.getChild("pc2", rfin.getNamespace());
                        modelColindantes.add(lstColindantes.getModel().getSize(), pc11
                                .getText()
                                + pc22.getText());

                    }// del nulo de colindantes
                }

            }

        }
        if (modelColindantes.getSize() != 0)
            lstColindantes.setSelectedIndex(0);
    };

    /**
     * This method initializes jList
     * 
     * @return javax.swing.JList
     */
    private JList getJList2()
    {
        if (lstColindantes == null)
        {
            lstColindantes = new JList();
        }
        return lstColindantes;
    }

    /**
     * Rellena la lista de conlindantes para un bien pasado como RefCat
     * 
     * @param doc
     *            Documento XML
     * @param String
     *            refCat Referencia Catastral del Bien seleccionado.
     * @param String
     *            refColindante Referencia del Colindante
     * @return null
     */
    private void rellenarColindantes(Document doc, String refCat, String refColindante)
    {

        Element raiz = doc.getRootElement(); // Es el sopa:Envelope
        Element soapBody = raiz.getChild("Body", raiz.getNamespace());
        Namespace nm = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element consultarRC = soapBody.getChild("Consultar_RC_Out", nm);
        Namespace nm2 = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element ovc_consulta_datos_out = consultarRC.getChild("ovc_consulta_datos_out",
                nm2);
        Element resp = ovc_consulta_datos_out.getChild("resp", ovc_consulta_datos_out
                .getNamespace());
        Element dsa = resp.getChild("dsa", resp.getNamespace());

        Element lbi = dsa.getChild("lbi", dsa.getNamespace());
        List bienes = lbi.getChildren("dbi", lbi.getNamespace());
        Iterator iBienes = bienes.iterator();
        while (iBienes.hasNext())
        {
            Element dbi = (Element) iBienes.next();
            Element bir = dbi.getChild("bir", dbi.getNamespace());
            Element idine = bir.getChild("idine", bir.getNamespace());
            Element rc = idine.getChild("rc", idine.getNamespace());
            // Acceder a la Referencia Catastral.
            Element pc1 = rc.getChild("pc1", rc.getNamespace());
            Element pc2 = rc.getChild("pc2", rc.getNamespace());
            if ((pc1.getText() + pc2.getText()).equals(refCat))
            {
                // Hemos encontrado el Bien de partida,

                Element lcol = bir.getChild("lcol", bir.getNamespace());
                List colindantes = lcol.getChildren("col", lcol.getNamespace());

                Iterator iCol = colindantes.iterator();
                while (iCol.hasNext())
                {
                    Element col = (Element) iCol.next();
                    // Para cada colindante localizar su refCat
                    Element rfin = col.getChild("rfin", col.getNamespace());
                    Element pc11 = rfin.getChild("pc1", rfin.getNamespace());
                    Element pc22 = rfin.getChild("pc2", rfin.getNamespace());

                    // Buscamos si es correcto el colindante
                    if ((pc11.getText() + pc22.getText()).equals(refColindante))
                    {
                        Element idp_out = col.getChild("idp_out", col.getNamespace());
                        Element nif = idp_out.getChild("nif", idp_out.getNamespace());
                        Element nom = idp_out.getChild("nom", idp_out.getNamespace());
                        lblDesNif.setText(nif!=null?nif.getText():"");
                        lblDesTitularLocalizacion.setText(nom!=null?nom.getText():"");
                        // Superficie
                        Element sup = col.getChild("sup", col.getNamespace());
                        lblDesSuperficieLocalizacion.setText(sup!=null?sup.getText():"");
                        // Localizacion
                        Element lloc = col.getChild("lloc", col.getNamespace());
                        lblDesLocalizacion.setText(lloc!=null?lloc.getText():"");

                        // Buscamos los datos si es rústico del paraje y
                        // concentración

                        Element dtrus = col.getChild("dtrus", col.getNamespace());
                        
                        if(dtrus!=null)
                        {
                            Element lorus = dtrus.getChild("lorus", dtrus.getNamespace());
                            if(lorus!=null)
                            {
                                Element cma = lorus.getChild("cma", lorus.getNamespace());
                                if(cma!=null)
                                {
                                    lblDesAgregacion.setText(cma.getText());
                                }
                                Element czc = lorus.getChild("czc", lorus.getNamespace());
                                if(czc!=null)
                                {
                                    lblDesConcentracion.setText(czc.getText());
                                }
                                
                                Element npa = lorus.getChild("npa", lorus.getNamespace());
                                if(npa!=null)
                                {
                                    lblDesParaje.setText(npa.getText());
                                }
                                Element coid = lorus.getChild("coid", dtrus.getNamespace());

                                if(coid!=null)
                                {
                                    Element cpo = coid.getChild("cpo", coid.getNamespace());
                                    Element cpa = coid.getChild("cpa", coid.getNamespace());
                                    if(cpo!=null && cpa!=null)
                                    {
                                        lblDesIdRustico.setText(cpo.getText() + cpa.getText());        
                                    }
                                    
                                }

                                
                            }
                            
                        }
                        

                    }

                }

            }

        }

    };

    /**
     * This method initializes jScrollPane2
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane2()
    {
        if (jScrollPane2 == null)
        {
            jScrollPane2 = new JScrollPane();
            jScrollPane2.setBounds(9, 41, 147, 384);
            jScrollPane2.setViewportView(lstTitulares);
        }
        return jScrollPane2;
    }

    /**
     * This method initializes jPanel8
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel8()
    {
        if (jPanel8 == null)
        {
            jPanel8 = new JPanel();
            lblDerechoTitular = new JLabel();
            jPanel8.setLayout(null);
            jPanel8.setBounds(168, 88, 502, 142);
            jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(
                    java.awt.Color.black, 2));
            jPanel8.setName("");
            lblDerechoTitular.setBounds(5, 5, 165, 17);
            lblDerechoTitular.setText(aplicacion
                    .getI18nString("certificados.derecho.titular"));
            jPanel8.add(lblDerechoTitular, null);
            jPanel8.add(getLblDesDerechoTitular(), null);
        }
        return jPanel8;
    }

    /**
     * This method initializes jPanel9
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel9()
    {
        if (jPanel9 == null)
        {
            jPanel9 = new JPanel();
            lblDomicilioFiscal = new JLabel();
            jPanel9.setLayout(null);
            jPanel9.setBounds(170, 240, 501, 180);
            jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(
                    java.awt.Color.black, 2));
            lblDomicilioFiscal.setBounds(5, 5, 165, 17);
            lblDomicilioFiscal.setText(aplicacion
                    .getI18nString("certificados.domicilio.fiscal"));
            jPanel9.add(lblDomicilioFiscal, null);
            jPanel9.add(getLblDesDomicilioFiscal(), null);
        }
        return jPanel9;
    }

    /**
     * Rellena la lista de los Titulares con el Nif.
     * 
     * @param doc
     *            Documento XML
     * @return null
     */
    private void cargarTitularesParcela(Document doc)
    {

        Element raiz = doc.getRootElement(); // Es el sopa:Envelope
        Element soapBody = raiz.getChild("Body", raiz.getNamespace());
        Namespace nm = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element consultarRC = soapBody.getChild("Consultar_RC_Out", nm);
        Namespace nm2 = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element ovc_consulta_datos_out = consultarRC.getChild("ovc_consulta_datos_out",
                nm2);
        Element resp = ovc_consulta_datos_out.getChild("resp", ovc_consulta_datos_out
                .getNamespace());
        Element dsa = resp.getChild("dsa", resp.getNamespace());

        // Seleccionamos en la Lista de Bienes la posición y un bucle para
        // obtener los titulares
        int posicion = lstBienes.getSelectedIndex();
        int buscarBienHasta = 0;
        Element lbi = dsa.getChild("lbi", dsa.getNamespace());
        List bienes = lbi.getChildren("dbi", lbi.getNamespace());
        // Iteramos sobre los bienes hasta alcanzar la posición del Bien
        Iterator iBienes = bienes.iterator();
        while (iBienes.hasNext())
        {
            Element dbi = (Element) iBienes.next();
            if (posicion == buscarBienHasta++)
            {
                // Se ha alcanzado las posición
                Element ltit = dbi.getChild("ltit", dbi.getNamespace());
                List titulares = ltit.getChildren("titr", ltit.getNamespace());
                Iterator iTitulares = titulares.iterator();
                while (iTitulares.hasNext())
                {
                    Element titr = (Element) iTitulares.next();
                    Element idp_out = titr.getChild("idp_out", titr.getNamespace());
                    Element nif2 = idp_out.getChild("nif", idp_out.getNamespace());
                    int pos = lstTitulares.getModel().getSize();
                    modelTitulares.add(pos, nif2.getText());
                }

            }

        }
        if(modelTitulares.getSize()!=0) lstTitulares.setSelectedIndex(0);
    }

    /**
     * Rellena la lista de los Titulares con el Nif.
     * 
     * @param doc
     *            Documento XML
     * @return null
     */
    private void rellenarDatosTitular(Document doc, String sNif)
    {

        int posicionBien = 0;
        int posicionTitular = 0;
        int buscarTitularHasta = 0;
        int buscarBienHasta = 0;
        posicionBien = lstBienes.getSelectedIndex();
        posicionTitular = lstTitulares.getSelectedIndex();

        Element raiz = doc.getRootElement(); // Es el sopa:Envelope
        Element soapBody = raiz.getChild("Body", raiz.getNamespace());
        Namespace nm = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element consultarRC = soapBody.getChild("Consultar_RC_Out", nm);
        Namespace nm2 = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element ovc_consulta_datos_out = consultarRC.getChild("ovc_consulta_datos_out",
                nm2);
        Element resp = ovc_consulta_datos_out.getChild("resp", ovc_consulta_datos_out
                .getNamespace());
        Element dsa = resp.getChild("dsa", resp.getNamespace());

        // Buscar el bien hasta que coincida con el índice
        Element lbi = dsa.getChild("lbi", dsa.getNamespace());
        List bienes = lbi.getChildren("dbi", lbi.getNamespace());
        // Iteramos sobre los bienes hasta alcanzar la posición del Bien
        Iterator iBienes = bienes.iterator();
        while (iBienes.hasNext())
        {
            Element dbi = (Element) iBienes.next();
            if (posicionBien == buscarBienHasta++)
            {
                // Hemos encontrado el Bien, ahora hay que iterar sobre el
                // Titular.
                Element ltit = dbi.getChild("ltit", dbi.getNamespace());
                List titulares = ltit.getChildren("titr", ltit.getNamespace());
                Iterator iTitulares = titulares.iterator();
                while (iTitulares.hasNext())
                {
                    Element titr = (Element) iTitulares.next();
                    Element idp_out = titr.getChild("idp_out", titr.getNamespace());
                    Element nif2 = idp_out.getChild("nif", idp_out.getNamespace());

                    if (sNif.equals(nif2.getText()))
                    {
                        // Nombre
                        Element nom2 = idp_out.getChild("nom", idp_out.getNamespace());
                        lblDesTitularNombre.setText(nom2.getText());
                        // Derecho del Titular
                        Element lder = titr.getChild("lder", titr.getNamespace());

                        lblDesDerechoTitular.setText(lder.getText());
                        // Domicilio Fiscal
                        Element ldf = titr.getChild("ldf", titr.getNamespace());
                        lblDesDomicilioFiscal.setText(ldf.getText());

                    }

                }
            }
        }

    };

    /**
     * This method initializes jTextArea
     * 
     * @return javax.swing.JTextArea
     */
    private JTextArea getLblDesDerechoTitular()
    {
        if (lblDesDerechoTitular == null)
        {
            lblDesDerechoTitular = new JTextArea();
            lblDesDerechoTitular.setBounds(5, 25, 488, 112);
            lblDesDerechoTitular.setWrapStyleWord(true);
            lblDesDerechoTitular.setEditable(false);
            lblDesDerechoTitular.setBackground(new java.awt.Color(204, 204, 204));
            lblDesDerechoTitular.setLineWrap(true);
        }
        return lblDesDerechoTitular;
    }

    /**
     * This method initializes jTextArea
     * 
     * @return javax.swing.JTextArea
     */
    private JTextArea getLblDesDomicilioFiscal()
    {
        if (lblDesDomicilioFiscal == null)
        {
            lblDesDomicilioFiscal = new JTextArea();
            lblDesDomicilioFiscal.setBounds(5, 27, 484, 142);
            lblDesDomicilioFiscal.setBackground(new java.awt.Color(204, 204, 204));
            lblDesDomicilioFiscal.setWrapStyleWord(true);
            lblDesDomicilioFiscal.setLineWrap(true);
            lblDesDomicilioFiscal.setEditable(false);
        }
        return lblDesDomicilioFiscal;
    }

    /**
     * This method initializes jPanel10
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel10()
    {
        if (jPanel10 == null)
        {
            lblFechaMovimiento = new JLabel();
            lblEntidadMovimiento = new JLabel();
            lblDesFechaMovimiento = new JLabel();
            lblDesEntidadMovimiento = new JLabel();
            jPanel10 = new JPanel();
            jPanel10.setLayout(null);
            jPanel10.setBounds(9, 8, 671, 112);
            jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(
                    java.awt.Color.black, 2));
            lblFechaMovimiento.setBounds(8, 45, 134, 19);
            lblFechaMovimiento.setText(aplicacion.getI18nString("certificados.fecha"));
            lblEntidadMovimiento.setBounds(8, 74, 134, 20);
            lblEntidadMovimiento
                    .setText(aplicacion.getI18nString("certificados.entidad"));
            lblDesEntidadMovimiento.setBounds(154, 73, 509, 13);
            lblDesEntidadMovimiento.setText("");
            lblDesFechaMovimiento.setBounds(154, 47, 509, 13);
            lblDesFechaMovimiento.setText("");
            jPanel10.add(lblMovimiento, null);
            jPanel10.add(getJSeparator(), null);
            jPanel10.add(lblFechaMovimiento, null);
            jPanel10.add(lblEntidadMovimiento, null);
            jPanel10.add(lblDesEntidadMovimiento, null);
            jPanel10.add(lblDesFechaMovimiento, null);
        }
        return jPanel10;
    }

    /**
     * This method initializes jSeparator
     * 
     * @return javax.swing.JSeparator
     */
    private JSeparator getJSeparator()
    {
        if (jSeparator == null)
        {
            jSeparator = new JSeparator();
            jSeparator.setBounds(7, 33, 657, 8);
        }
        return jSeparator;
    }

    /**
     * This method initializes jPanel11
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel11()
    {
        if (jPanel11 == null)
        {
            lblDesEscala = new JLabel();
            lblPlanta = new JLabel();
            lblDesPlantaGeneral = new UrlTextField();
            jPanel11 = new JPanel();
            lblEscala = new JLabel();
            lblDesSuperficieConstruida = new JLabel();
            lblSuperficieConstruida = new JLabel();
            lblSuperficieSolar = new JLabel();
            lblDesSuperficieSolar = new JLabel();
            lblDesTipoFinca = new JLabel();
            lblTipoFinca = new JLabel();
            lblDomicilioTributarioMovimiento = new JLabel();
            lblDesDomicilioTributarioMovimiento = new JLabel();
            lblFinca = new JLabel();
            jPanel11.setLayout(null);
            lblFinca.setBounds(8, 5, 134, 15);
            lblFinca.setText(aplicacion.getI18nString("certificados.finca"));
            lblDomicilioTributarioMovimiento.setBounds(8, 39, 134, 13);
            lblDomicilioTributarioMovimiento.setText(aplicacion
                    .getI18nString("certificados.domicilio.tributario"));
            lblDesDomicilioTributarioMovimiento.setBounds(154, 39, 509, 13);
            lblDesDomicilioTributarioMovimiento.setText("");
            lblTipoFinca.setBounds(8, 80, 134, 13);
            lblTipoFinca.setText(aplicacion.getI18nString("certificados.tipo.finca"));
            lblDesTipoFinca.setBounds(154, 80, 509, 13);
            lblDesTipoFinca.setText("");
            lblSuperficieSolar.setBounds(8, 132, 134, 13);
            lblSuperficieSolar.setText(aplicacion
                    .getI18nString("certificados.superficie.solar"));
            lblDesSuperficieSolar.setBounds(154, 132, 509, 13);
            lblDesSuperficieSolar.setText("");
            lblSuperficieConstruida.setBounds(9, 160, 134, 14);
            lblSuperficieConstruida.setText(aplicacion
                    .getI18nString("certificados.superficie.construida"));
            lblDesSuperficieConstruida.setBounds(155, 160, 509, 13);
            lblDesSuperficieConstruida.setText("");
            lblEscala.setBounds(8, 201, 134, 13);
            lblEscala.setText(aplicacion.getI18nString("certificados.escala"));
            lblPlanta.setBounds(8, 236, 134, 13);
            lblPlanta.setText(aplicacion.getI18nString("certificados.plano.general"));
            lblDesPlantaGeneral.setBounds(154, 236, 509, 30);
            lblDesPlantaGeneral.setText("");
            jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(
                    java.awt.Color.black, 2));
            lblDesEscala.setBounds(154, 201, 509, 13);
            lblDesEscala.setText("");
            jPanel11.add(lblFinca, null);
            jPanel11.add(getJSeparator1(), null);
            jPanel11.add(lblDomicilioTributarioMovimiento, null);
            jPanel11.add(lblDesDomicilioTributarioMovimiento, null);
            jPanel11.add(lblTipoFinca, null);
            jPanel11.add(lblDesTipoFinca, null);
            jPanel11.add(getJSeparator2(), null);
            jPanel11.add(lblSuperficieSolar, null);
            jPanel11.add(lblDesSuperficieSolar, null);
            jPanel11.add(lblSuperficieConstruida, null);
            jPanel11.add(lblDesSuperficieConstruida, null);
            jPanel11.add(getJSeparator3(), null);
            jPanel11.add(lblEscala, null);
            jPanel11.add(lblPlanta, null);
            jPanel11.add(lblDesPlantaGeneral, null);
            jPanel11.add(lblDesEscala, null);
            jPanel11.setBounds(7, 129, 671, 302);
        }
        return jPanel11;
    }

    /**
     * This method initializes jSeparator1
     * 
     * @return javax.swing.JSeparator
     */
    private JSeparator getJSeparator1()
    {
        if (jSeparator1 == null)
        {
            jSeparator1 = new JSeparator();
            jSeparator1.setBounds(11, 28, 657, 8);
        }
        return jSeparator1;
    }

    /**
     * This method initializes jSeparator2
     * 
     * @return javax.swing.JSeparator
     */
    private JSeparator getJSeparator2()
    {
        if (jSeparator2 == null)
        {
            jSeparator2 = new JSeparator();
            jSeparator2.setBounds(10, 113, 653, 8);
        }
        return jSeparator2;
    }

    /**
     * This method initializes jSeparator3
     * 
     * @return javax.swing.JSeparator
     */
    private JSeparator getJSeparator3()
    {
        if (jSeparator3 == null)
        {
            jSeparator3 = new JSeparator();
            jSeparator3.setBounds(11, 188, 656, 8);
        }
        return jSeparator3;
    }

    /**
     * Rellena Los datos correspondiente al Movimiento y Fincas.
     * 
     * @param doc
     *            Documento XML
     * @return null
     */
    private void cargarMovimientoFinca(Document doc)
    {

        // Hay que recorrer la lista de los bienes para localizar el
        // seleccionado
        int posicionBien = 0;
        int buscarBienHasta = 0;
        posicionBien = lstBienes.getSelectedIndex();

        Element raiz = doc.getRootElement(); // Es el sopa:Envelope
        Element soapBody = raiz.getChild("Body", raiz.getNamespace());
        Namespace nm = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element consultarRC = soapBody.getChild("Consultar_RC_Out", nm);
        Namespace nm2 = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element ovc_consulta_datos_out = consultarRC.getChild("ovc_consulta_datos_out",
                nm2);
        Element resp = ovc_consulta_datos_out.getChild("resp", ovc_consulta_datos_out
                .getNamespace());
        Element dsa = resp.getChild("dsa", resp.getNamespace());

        // Debo llegar al dsa
        Element lbi = dsa.getChild("lbi", dsa.getNamespace());
        List bienes = lbi.getChildren("dbi", lbi.getNamespace());
        Iterator iBienes = bienes.iterator();
        while (iBienes.hasNext())
        {
            Element dbi = (Element) iBienes.next();
            if (posicionBien == buscarBienHasta++)
            {
                Element movr = dbi.getChild("movr", dbi.getNamespace());
                // Element om = movr.getChild("om");
                // Fecha de Alteración
                if (movr != null)
                {
                    Element fac = movr.getChild("fac", movr.getNamespace());
                    lblDesFechaMovimiento.setText(fac == null ? "" : fac.getText());
                }
                // Entidad origen del movimiento
                // Element lmot = movr.getChild("lmot",movr.getNamespace());
                // lblDesEntidadMovimiento.setText(lmot.getText());
                //

                // Datos de la Finca
                Element finca = dbi.getChild("finca", lbi.getNamespace());
                if (finca != null)
                {
                    Element ldt = finca.getChild("ldt", finca.getNamespace());
                    Element ltp = finca.getChild("ltp", finca.getNamespace());

                    lblDesDomicilioTributarioMovimiento.setText(ldt == null ? "" : ldt
                            .getText());
                    lblDesTipoFinca.setText(ltp == null ? "" : ltp.getText());

                    // Superficies de la finca
                    Element dff = finca.getChild("dff", finca.getNamespace());
                    if (dff != null)
                    {
                        Element ssf = dff.getChild("ssf", dff.getNamespace());
                        if (ssf != null)
                        {
                            Element ss = ssf.getChild("ss", ssf.getNamespace());
                            Element sct = ssf.getChild("sct", ssf.getNamespace());
                            lblDesSuperficieSolar.setText(ss == null ? "" : ss.getText());
                            lblDesSuperficieConstruida.setText(sct == null ? "" : sct
                                    .getText());
                        }
                    }// del dff
                    // Información Grafica
                    Element infgraf = finca.getChild("infgraf", finca.getNamespace());
                    if (infgraf != null)
                    {
                        Element esc = infgraf.getChild("esc", infgraf.getNamespace());
                        Element graf = infgraf.getChild("igraf", infgraf.getNamespace());
                        lblDesPlantaGeneral.setText(graf == null ? "" : graf.getText());
                        lblDesEscala.setText(esc == null ? "" : esc.getText());
                    }
                }// del Null de Finca
            }

        }

    };

    /**
     * Rellena la lista de las construcciones
     * 
     * @param doc
     *            Documento XML
     * @return null
     */
    private void rellenarListaConstrucciones(Document doc)
    {

        int posicionBien = 0;
        int buscarBienHasta = 0;
        posicionBien = lstBienes.getSelectedIndex();

        Element raiz = doc.getRootElement(); // Es el sopa:Envelope
        Element soapBody = raiz.getChild("Body", raiz.getNamespace());
        Namespace nm = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element consultarRC = soapBody.getChild("Consultar_RC_Out", nm);
        Namespace nm2 = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element ovc_consulta_datos_out = consultarRC.getChild("ovc_consulta_datos_out",
                nm2);
        Element resp = ovc_consulta_datos_out.getChild("resp", ovc_consulta_datos_out
                .getNamespace());
        Element dsa = resp.getChild("dsa", resp.getNamespace());

        Element lbi = dsa.getChild("lbi", dsa.getNamespace());
        List bienes = lbi.getChildren("dbi", lbi.getNamespace());
        // Iteramos sobre los bienes hasta alcanzar la posición del Bien
        Iterator iBienes = bienes.iterator();
        while (iBienes.hasNext())
        {
            Element dbi = (Element) iBienes.next();
            if (posicionBien == buscarBienHasta++)
            {
                // Se ha alcanzado las posición
                Element lcons = dbi.getChild("lcons", lbi.getNamespace());
                if (lcons != null)
                {
                    List construcciones = lcons
                            .getChildren("consr", lcons.getNamespace());
                    Iterator iConstrucciones = construcciones.iterator();

                    while (iConstrucciones.hasNext())
                    {
                        Element consr = (Element) iConstrucciones.next();
                        Element lcd = consr.getChild("lcd", consr.getNamespace());
                        int posicion = lstConstrucciones.getModel().getSize();
                        modeloConstrucciones.add(posicion, lcd.getText());
                    }
                }// del nulo de lcons
            }
        }
        if (modeloConstrucciones.getSize() != 0)
            lstConstrucciones.setSelectedIndex(0);

    };

    /**
     * Rellena la lista de las construcciones
     * 
     * @param doc
     *            Documento XML
     * @return null
     */
    private void rellenarListaSubparcelas(Document doc)
    {

        int posicionBien = 0;
        int buscarBienHasta = 0;
        posicionBien = lstBienes.getSelectedIndex();

        Element raiz = doc.getRootElement(); // Es el sopa:Envelope
        Element soapBody = raiz.getChild("Body", raiz.getNamespace());
        Namespace nm = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element consultarRC = soapBody.getChild("Consultar_RC_Out", nm);
        Namespace nm2 = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element ovc_consulta_datos_out = consultarRC.getChild("ovc_consulta_datos_out",
                nm2);
        Element resp = ovc_consulta_datos_out.getChild("resp", ovc_consulta_datos_out
                .getNamespace());
        Element dsa = resp.getChild("dsa", resp.getNamespace());

        Element lbi = dsa.getChild("lbi", dsa.getNamespace());
        List bienes = lbi.getChildren("dbi", lbi.getNamespace());
        // Iteramos sobre los bienes hasta alcanzar la posición del Bien
        Iterator iBienes = bienes.iterator();
        while (iBienes.hasNext())
        {
            Element dbi = (Element) iBienes.next();
            if (posicionBien == buscarBienHasta++)
            {
                Element lcons = dbi.getChild("lsubpar", lbi.getNamespace());
                if (lcons != null)
                {
                    List subparcelas = lcons.getChildren("subpar", lcons.getNamespace());
                    Iterator iSubparcelas = subparcelas.iterator();
                    while (iSubparcelas.hasNext())
                    {
                        Element consr = (Element) iSubparcelas.next();
                        Element csp = consr.getChild("csp", consr.getNamespace());
                        int posicion = lstSubparcelas.getModel().getSize();
                        modelSubparcelas.add(posicion, csp.getText());
                    }
                }
            }
        }
        if (modelSubparcelas.getSize() != 0)
            lstSubparcelas.setSelectedIndex(0);

    };

    /**
     * Rellena la los datos de las construcciones con el uso especificado.
     * 
     * @param doc
     *            Documento XML
     * @return null
     */
    private void rellenarDatosConstruccion(Document doc, String sUso, int nPosicion)
    {
        Element raiz = doc.getRootElement(); // Es el sopa:Envelope
        Element soapBody = raiz.getChild("Body", raiz.getNamespace());
        Namespace nm = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element consultarRC = soapBody.getChild("Consultar_RC_Out", nm);
        Namespace nm2 = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element ovc_consulta_datos_out = consultarRC.getChild("ovc_consulta_datos_out",
                nm2);
        Element resp = ovc_consulta_datos_out.getChild("resp", ovc_consulta_datos_out
                .getNamespace());
        Element dsa = resp.getChild("dsa", resp.getNamespace());

        int posicionBien = 0;
        int posicionConstruccion = 0;
        int buscarBienHasta = 0;
        int buscarConstruccionHasta = 0;

        posicionBien = lstBienes.getSelectedIndex();
        posicionConstruccion = lstConstrucciones.getSelectedIndex();
        // Recorrer todos los bienes hasta encontrar el seleccionado.

        Element lbi = dsa.getChild("lbi", dsa.getNamespace());
        List bienes = lbi.getChildren("dbi", lbi.getNamespace());
        // Iteramos sobre los bienes hasta alcanzar la posición del Bien
        Iterator iBienes = bienes.iterator();
        while (iBienes.hasNext())
        {
            Element dbi = (Element) iBienes.next();
            if (posicionBien == buscarBienHasta++)
            {
                Element lcons = dbi.getChild("lcons", dbi.getNamespace());
                List construcciones = lcons.getChildren("consr", lcons.getNamespace());
                Iterator iConstrucciones = construcciones.iterator();
                int posicion = 0;
                while (iConstrucciones.hasNext())
                {

                    Element consr = (Element) iConstrucciones.next();
                    Element lcd = consr.getChild("lcd", consr.getNamespace());
                    if (nPosicion == posicion++)
                    {
                        if (lcd.getText().equals(sUso))
                        {
                            // Estamos en el mismo valor
                            Element dt = consr.getChild("dt", consr.getNamespace());
                            // Domicilio Triburario
                            if (dt.getText() != null)
                            {
                                txtDomicilioConstruccion.setText(dt.getText().trim());
                            }
                            // Localizacion urbana
                            Element lourb = dt.getChild("lourb", dt.getNamespace());
                            if (lourb.getText() != null)
                            {
                                txtLocalizacionUrbana.setText(lourb.getText().trim());
                            }

                            // Localización Interna
                            Element loint = lourb.getChild("loint", lourb.getNamespace());
                            Element bq = loint.getChild("bq", loint.getNamespace());
                            Element es = loint.getChild("es", loint.getNamespace());
                            Element pl = loint.getChild("pl", loint.getNamespace());
                            Element pu = loint.getChild("pu", loint.getNamespace());
                            if (bq != null)
                            {
                                lblDesBloque.setText(bq.getText());
                            }
                            if (es != null)
                            {
                                lblDesEscalera.setText(es.getText());
                            }
                            if (pl != null)
                            {
                                lblDesPlantaConstruccion.setText(pl.getText());
                            }
                            if (pu != null)
                            {
                                lblDesPuerta.setText(pu.getText());
                            }
                            break;
                        }

                    }

                }

            }
        }

    };

    /**
     * Rellena la los datos de las subparcelas con el código especificado.
     * 
     * @param Document
     *            doc Documento XML
     * @param String
     *            sCodigo Codigo de la subparcela
     * @return null
     */
    private void rellenarDatosSubParcela(Document doc, String sCodigo)
    {

        // Localizar el bien y la subParcela correspondiente

        int posicionBien = 0;
        int buscarBienHasta = 0;
        posicionBien = lstBienes.getSelectedIndex();

        Element raiz = doc.getRootElement(); // Es el sopa:Envelope
        Element soapBody = raiz.getChild("Body", raiz.getNamespace());
        Namespace nm = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element consultarRC = soapBody.getChild("Consultar_RC_Out", nm);
        Namespace nm2 = Namespace.getNamespace("http://www.catastro.meh.es/");
        Element ovc_consulta_datos_out = consultarRC.getChild("ovc_consulta_datos_out",
                nm2);
        Element resp = ovc_consulta_datos_out.getChild("resp", ovc_consulta_datos_out
                .getNamespace());
        Element dsa = resp.getChild("dsa", resp.getNamespace());

        Element lbi = dsa.getChild("lbi", dsa.getNamespace());
        List bienes = lbi.getChildren("dbi", lbi.getNamespace());
        // Iteramos sobre los bienes hasta alcanzar la posición del Bien
        Iterator iBienes = bienes.iterator();
        while (iBienes.hasNext())
        {
            Element dbi = (Element) iBienes.next();
            if (posicionBien == buscarBienHasta++)
            {
                Element lsubpar = dbi.getChild("lsubpar", dbi.getNamespace());
                List subParcelas = lsubpar.getChildren("subpar", lsubpar.getNamespace());
                Iterator iSubparcelas = subParcelas.iterator();

                while (iSubparcelas.hasNext())
                {
                    Element subpar = (Element) iSubparcelas.next();
                    Element csp = subpar.getChild("csp", subpar.getNamespace());
                    if (csp.getText().equals(sCodigo))
                    {
                        // Estamos en el mismo valor de la suparcela
                        Element dsp = subpar.getChild("dsp", subpar.getNamespace());
                        Element ccult = dsp.getChild("ccult", dsp.getNamespace());
                        Element clc = ccult.getChild("clc", ccult.getNamespace());

                        // Calificación del Cultivo
                        if (clc.getText() != null)
                        {
                            txtClasificacionCultivo.setText(clc.getText().trim());
                        }
                        // Denominación del clase de cultivo
                        Element dcc = ccult.getChild("dcc", ccult.getNamespace());
                        if (dcc.getText() != null)
                        {
                            txtDenominacionCultivo.setText(dcc.getText().trim());
                        }
                        // Intensidad Productiva
                        Element ip = dsp.getChild("ip", dsp.getNamespace());
                        Element vcat = dsp.getChild("vcat", dsp.getNamespace());
                        lblDesIntensidadProductiva.setText(ip.getText());
                        lblDesValorCatastralSubparcela.setText(vcat.getText());

                    }

                }
            }
        }

    };

    /**
     * Limpia los valores de la ventana de Catastro.
     * 
     * @param boolean
     *            bGenerales Indicador para limpiar ventana de datos Generales
     * @param boolean
     *            bTitulares Indicador para limpiar ventana de Titulares
     * @param boolean
     *            bMovimiento Indicador para limpiar ventana de Movimientos
     * @param boolean
     *            bConstrucciones Indicador para limpiar ventana de
     *            Construcciones
     * @param boolean
     *            bSubparcelas Indicador para limpiar la ventana de Subarcelas.
     * @return null
     */

    private void resetarValoresVentanaCatastro(boolean bGenerales, boolean bTitulares,
            boolean bMovimientos, boolean bConstrucciones, boolean bSubparcelas)
    {
        // Para cada parámero si es true se deberá limpiar su solapa
        // correspondiente
        if (bGenerales)
        {
            lblDesNaturaleza.setText("");
            lblDesUso.setText("");
            lblDesDomicilioTributario.setText("");
            lblDesMunicipio.setText("");
            lblDesRefCat.setText("");
            lblDesValorCatastral.setText("");
            lblDesSupAso.setText("");
            // Reiniciar la lista de Colindantes
            modelColindantes.clear();
            lblDesNif.setText("");
            lblDesTitularLocalizacion.setText("");
            lblDesValCatCon.setText("");
            lblDesAgregacion.setText("");
            lblDesConcentracion.setText("");
            lblDesIdRustico.setText("");
            lblDesParaje.setText("");
            // Creo que me falta la última solapa
        }// de los Generales

        if (bTitulares)
        {
            modelTitulares.clear();
            lblDesTitularNombre.setText("");
            lblDesDerechoTitular.setText("");
            lblDesDomicilioFiscal.setText("");
        } // Del titular

        if (bMovimientos)
        {
            lblDesFechaMovimiento.setText("");
            lblDesEntidadMovimiento.setText("");
            lblDesDomicilioTributarioMovimiento.setText("");
            lblDesTipoFinca.setText("");
            lblDesSuperficieSolar.setText("");
            lblDesSuperficieConstruida.setText("");
            lblDesEscala.setText("");
            lblDesPlantaGeneral.setText("");
        }// Del Movimiento

        if (bConstrucciones)
        {
            modeloConstrucciones.clear();
            txtDomicilioConstruccion.setText("");
            txtLocalizacionUrbana.setText("");
            lblDesBloque.setText("");
            lblDesEscalera.setText("");
            lblDesPlantaConstruccion.setText("");
            lblDesPuerta.setText("");
        }
        if (bSubparcelas)
        {
            modelSubparcelas.clear();
            txtClasificacionCultivo.setText("");
            txtDenominacionCultivo.setText("");
            lblDesIntensidadProductiva.setText("");
            lblDesValorCatastralSubparcela.setText("");

        }

    }

    /**
     * This method initializes jScrollPane3
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane3()
    {
        if (jScrollPane3 == null)
        {
            jScrollPane3 = new JScrollPane();
            jScrollPane3.setBounds(9, 41, 147, 384);
            jScrollPane3.setViewportView(getJList3());
        }
        return jScrollPane3;
    }

    /**
     * This method initializes jList
     * 
     * @return javax.swing.JList
     */
    private JList getJList3()
    {
        if (lstConstrucciones == null)
        {
            lstConstrucciones = new JList();
        }
        return lstConstrucciones;
    }

    /**
     * This method initializes jPanel12
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel12()
    {
        if (jPanel12 == null)
        {
            lblDesPuerta = new JLabel();
            lblDesPlantaConstruccion = new JLabel();
            lblDesEscalera = new JLabel();
            lblDesBloque = new JLabel();
            lblPuerta = new JLabel();
            lblPlantaConstruccion = new JLabel();
            jPanel12 = new JPanel();
            lblEscalera = new JLabel();
            lblBloque = new JLabel();
            lblLocalizacionInterna = new JLabel();
            lblLocalizacionUrbana = new JLabel();
            lblDomicilioConstruccion = new JLabel();
            jPanel12.setLayout(null);
            jPanel12.setBounds(162, 41, 511, 384);
            lblDomicilioConstruccion.setBounds(14, 17, 134, 19);
            lblDomicilioConstruccion.setText(aplicacion
                    .getI18nString("certificados.domicilio.tributario"));

            jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(
                    java.awt.Color.black, 2));
            lblLocalizacionUrbana.setBounds(14, 108, 137, 25);
            lblLocalizacionUrbana.setText(aplicacion
                    .getI18nString("certificados.localizacion.urbana"));
            lblLocalizacionInterna.setBounds(16, 209, 137, 21);
            lblLocalizacionInterna.setText(aplicacion
                    .getI18nString("certificados.localizacion.interna"));
            lblBloque.setBounds(35, 240, 53, 26);
            lblBloque.setText(aplicacion.getI18nString("certificados.bloque"));
            lblEscalera.setBounds(35, 273, 66, 26);
            lblEscalera.setText(aplicacion.getI18nString("certificados.escalera"));
            lblPlantaConstruccion.setBounds(35, 303, 53, 26);
            lblPlantaConstruccion
                    .setText(aplicacion.getI18nString("certificados.planta"));
            lblPuerta.setBounds(35, 336, 53, 26);
            lblPuerta.setText(aplicacion.getI18nString("certificados.puerta"));
            lblDesBloque.setBounds(140, 241, 204, 26);
            lblDesBloque.setText("");
            lblDesEscalera.setBounds(140, 274, 204, 26);
            lblDesEscalera.setText("");
            lblDesPlantaConstruccion.setBounds(140, 304, 204, 26);
            lblDesPlantaConstruccion.setText("");
            lblDesPuerta.setBounds(140, 337, 204, 26);
            lblDesPuerta.setText("");
            jPanel12.add(lblDomicilioConstruccion, null);
            jPanel12.add(getTxtDomicilioConstruccion(), null);
            jPanel12.add(lblLocalizacionUrbana, null);
            jPanel12.add(getTxtLocalizacionUrbana(), null);
            jPanel12.add(getJSeparator4(), null);
            jPanel12.add(lblLocalizacionInterna, null);
            jPanel12.add(lblBloque, null);
            jPanel12.add(lblEscalera, null);
            jPanel12.add(lblPlantaConstruccion, null);
            jPanel12.add(lblPuerta, null);
            jPanel12.add(lblDesBloque, null);
            jPanel12.add(lblDesEscalera, null);
            jPanel12.add(lblDesPlantaConstruccion, null);
            jPanel12.add(lblDesPuerta, null);
        }
        return jPanel12;
    }

    /**
     * This method initializes jTextArea
     * 
     * @return javax.swing.JTextArea
     */
    private JTextArea getTxtDomicilioConstruccion()
    {
        if (txtDomicilioConstruccion == null)
        {
            txtDomicilioConstruccion = new JTextArea();
            txtDomicilioConstruccion.setBounds(16, 44, 480, 56);
            txtDomicilioConstruccion.setBackground(new java.awt.Color(204, 204, 204));
            txtDomicilioConstruccion.setWrapStyleWord(false);
            txtDomicilioConstruccion.setLineWrap(true);
            txtDomicilioConstruccion.setEditable(false);
        }
        return txtDomicilioConstruccion;
    }

    /**
     * This method initializes jTextArea
     * 
     * @return javax.swing.JTextArea
     */
    private JTextArea getTxtLocalizacionUrbana()
    {
        if (txtLocalizacionUrbana == null)
        {
            txtLocalizacionUrbana = new JTextArea();
            txtLocalizacionUrbana.setBounds(15, 140, 476, 53);
            txtLocalizacionUrbana.setWrapStyleWord(true);
            txtLocalizacionUrbana.setLineWrap(true);
            txtLocalizacionUrbana.setBackground(new java.awt.Color(204, 204, 204));
            txtLocalizacionUrbana.setEditable(false);
        }
        return txtLocalizacionUrbana;
    }

    /**
     * This method initializes jSeparator4
     * 
     * @return javax.swing.JSeparator
     */
    private JSeparator getJSeparator4()
    {
        if (jSeparator4 == null)
        {
            jSeparator4 = new JSeparator();
            jSeparator4.setBounds(14, 200, 482, 2);
        }
        return jSeparator4;
    }

    /**
     * This method initializes jScrollPane4
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane4()
    {
        if (jScrollPane4 == null)
        {
            jScrollPane4 = new JScrollPane();
            jScrollPane4.setBounds(9, 41, 147, 384);
            jScrollPane4.setViewportView(getJList4());
        }
        return jScrollPane4;
    }

    /**
     * This method initializes jList
     * 
     * @return javax.swing.JList
     */
    private JList getJList4()
    {
        if (lstSubparcelas == null)
        {
            lstSubparcelas = new JList();
        }
        return lstSubparcelas;
    }

    /**
     * This method initializes jPanel13
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel13()
    {
        if (jPanel13 == null)
        {
            jPanel13 = new JPanel();
            lblDatosSubparcelas = new JLabel();
            lblCalificacionCultivo = new JLabel();
            lblNombreCultivo = new JLabel();
            lblIntensidadProductiva = new JLabel();
            lblValorCatastralSubparcela = new JLabel();
            lblDesIntensidadProductiva = new JLabel();
            lblDesValorCatastralSubparcela = new JLabel();
            jPanel13.setLayout(null);
            jPanel13.setBounds(163, 45, 519, 378);
            jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(
                    java.awt.Color.black, 2));
            lblDatosSubparcelas.setText(aplicacion
                    .getI18nString("certificados.datos.subparcela"));
            lblDatosSubparcelas.setBounds(10, 13, 181, 16);
            lblCalificacionCultivo.setBounds(10, 48, 208, 23);
            lblCalificacionCultivo.setText(aplicacion
                    .getI18nString("certificados.cultivo"));
            lblNombreCultivo.setBounds(10, 144, 487, 26);
            lblNombreCultivo.setText(aplicacion
                    .getI18nString("certificados.denominacion.cultivo"));
            lblIntensidadProductiva.setBounds(10, 284, 131, 26);
            lblIntensidadProductiva.setText(aplicacion
                    .getI18nString("certificados.intensidad.productiva"));
            lblValorCatastralSubparcela.setBounds(10, 327, 133, 25);
            lblValorCatastralSubparcela.setText(aplicacion
                    .getI18nString("certificados.valor.catastral"));
            lblDesIntensidadProductiva.setBounds(153, 284, 344, 31);
            lblDesIntensidadProductiva.setText("");
            lblDesValorCatastralSubparcela.setBounds(153, 327, 344, 29);
            lblDesValorCatastralSubparcela.setText("");
            jPanel13.add(lblDatosSubparcelas, null);
            jPanel13.add(getJSeparator5(), null);
            jPanel13.add(lblCalificacionCultivo, null);
            jPanel13.add(lblNombreCultivo, null);
            jPanel13.add(lblIntensidadProductiva, null);
            jPanel13.add(lblValorCatastralSubparcela, null);
            jPanel13.add(lblDesIntensidadProductiva, null);
            jPanel13.add(lblDesValorCatastralSubparcela, null);
            jPanel13.add(getJSeparator6(), null);
            jPanel13.add(getTxtDenominacionCultivo(), null);
            jPanel13.add(getTxtClasificacionCultivo(), null);
        }
        return jPanel13;
    }

    /**
     * This method initializes jSeparator5
     * 
     * @return javax.swing.JSeparator
     */
    private JSeparator getJSeparator5()
    {
        if (jSeparator5 == null)
        {
            jSeparator5 = new JSeparator();
            jSeparator5.setBounds(9, 40, 495, 10);
        }
        return jSeparator5;
    }

    /**
     * This method initializes jSeparator6
     * 
     * @return javax.swing.JSeparator
     */
    private JSeparator getJSeparator6()
    {
        if (jSeparator6 == null)
        {
            jSeparator6 = new JSeparator();
            jSeparator6.setBounds(9, 256, 498, 4);
        }
        return jSeparator6;
    }

    /**
     * This method initializes jTextArea
     * 
     * @return javax.swing.JTextArea
     */
    private JTextArea getTxtDenominacionCultivo()
    {
        if (txtDenominacionCultivo == null)
        {
            txtDenominacionCultivo = new JTextArea();
            txtDenominacionCultivo.setBounds(10, 185, 487, 60);
            txtDenominacionCultivo.setBackground(new java.awt.Color(204, 204, 204));
            txtDenominacionCultivo.setEditable(false);
            txtDenominacionCultivo.setWrapStyleWord(true);
            txtDenominacionCultivo.setLineWrap(true);
        }
        return txtDenominacionCultivo;
    }

    /**
     * This method initializes jTextArea1
     * 
     * @return javax.swing.JTextArea
     */
    private JTextArea getTxtClasificacionCultivo()
    {
        if (txtClasificacionCultivo == null)
        {
            txtClasificacionCultivo = new JTextArea();
            txtClasificacionCultivo.setBounds(10, 78, 487, 54);
            txtClasificacionCultivo.setWrapStyleWord(true);
            txtClasificacionCultivo.setBackground(new java.awt.Color(204, 204, 204));
            txtClasificacionCultivo.setEditable(false);
            txtClasificacionCultivo.setLineWrap(true);
            // txtClasificacionCultivo.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.black,5));
        }
        return txtClasificacionCultivo;
    }

    /**
     * This method initializes jPanel14
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel14()
    {
        if (jPanel14 == null)
        {
            lblDomicilioTributarioRustico = new JLabel();
            lblDesRusticoParaje = new JLabel();
            lblDesRusticoIdentificacion = new JLabel();
            lblDesRusticoConcentracion = new JLabel();
            lblDesRusticoAgregacion = new JLabel();
            lblRusticoParaje = new JLabel();
            lblRusticoIdentificacion = new JLabel();
            lblRusticoConcentracion = new JLabel();
            lblRusticoAgregacion = new JLabel();
            jPanel14 = new JPanel();
            jPanel14.setLayout(null);

            lblDomicilioTributarioRustico.setText(aplicacion
                    .getI18nString("certificados.titulo.rustico"));
            lblDomicilioTributarioRustico.setBounds(19, 16, 452, 16);
            lblRusticoAgregacion.setBounds(22, 80, 161, 20);
            lblRusticoAgregacion.setText(aplicacion
                    .getI18nString("certificados.Agregacion"));
            lblRusticoConcentracion.setBounds(21, 112, 163, 19);
            lblRusticoConcentracion.setText(aplicacion
                    .getI18nString("certificados.concentracion"));
            lblRusticoIdentificacion.setBounds(20, 141, 164, 20);
            lblRusticoIdentificacion.setText(aplicacion
                    .getI18nString("certificados.id.bien.rustico"));
            lblRusticoParaje.setBounds(18, 176, 168, 20);
            lblRusticoParaje.setText(aplicacion.getI18nString(" certificados.paraje"));
            lblDesRusticoAgregacion.setBounds(203, 79, 358, 20);
            lblDesRusticoAgregacion.setText("");
            lblDesRusticoConcentracion.setBounds(202, 111, 358, 20);
            lblDesRusticoConcentracion.setText("");
            lblDesRusticoIdentificacion.setBounds(203, 141, 359, 20);
            lblDesRusticoIdentificacion.setText("");
            lblDesRusticoParaje.setBounds(202, 176, 361, 20);
            lblDesRusticoParaje.setText("");
            jPanel14.add(lblDomicilioTributarioRustico, null);
            jPanel14.add(lblRusticoAgregacion, null);
            jPanel14.add(lblRusticoConcentracion, null);
            jPanel14.add(lblRusticoIdentificacion, null);
            jPanel14.add(lblRusticoParaje, null);
            jPanel14.add(lblDesRusticoAgregacion, null);
            jPanel14.add(lblDesRusticoConcentracion, null);
            jPanel14.add(lblDesRusticoIdentificacion, null);
            jPanel14.add(lblDesRusticoParaje, null);
        }
        return jPanel14;
    }
} // @jve:decl-index=0:visual-constraint="10,10";
// @jve:decl-index=0:visual-constraint="10,10"

