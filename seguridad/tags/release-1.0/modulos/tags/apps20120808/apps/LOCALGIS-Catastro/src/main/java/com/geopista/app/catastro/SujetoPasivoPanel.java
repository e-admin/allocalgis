/*
 * Created on 16-ene-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.catastro;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.util.Blackboard;

/**
 * @author hgarcia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SujetoPasivoPanel extends JPanel implements FeatureExtendedPanel
{
    
    //Paneles
    private JPanel jPanelDomicilioFiscal = new JPanel();
    private JPanel jPanelDatosIdentSujeto = new JPanel();
    private JPanel jPanelDatosCatastro = new JPanel();
    
    
    //Datos de identificación del titular y su cónyuge
    private JLabel nombreApellidoslbl1 = new JLabel();
    private JTextField txtnombretitular = new JTextField();
    private JTextField txtniftitular = new JTextField();
    private JLabel niftitularlbl = new JLabel();
    private JLabel nombreApellidosConyugelbl1 = new JLabel();
    private JTextField txtnombretitularConyuge = new JTextField();
    private JTextField txtniftitularConyuge = new JTextField();
    private JLabel niftitularConyugelbl = new JLabel();
    
    
    //Datos del domicilio fiscal
    private JLabel escaleralbl2 = new JLabel();
    private JTextField txtescaleratitular = new JTextField();
    private JLabel plantaconslbl2 = new JLabel();
    private JTextField txtplantatitular = new JTextField();
    private JLabel puertaconslbl2 = new JLabel();
    private JTextField txtpuertatitular = new  JTextField();  
    private JTextField txtdirnoestructitular = new JTextField();
    private JLabel kmparlbl3 = new JLabel();
    private JLabel bloqueparlbl4 = new JLabel();
    private JTextField txtbloquetitular = new JTextField();
    private JTextField txtkilometrotitular = new JTextField();
    private JLabel dirnoestrucparlbl3 = new JLabel();
    private JLabel letra2parlbl3 = new JLabel();
    private JLabel numero2parlbl6 = new JLabel();
    private JTextField txtnumero2titular = new JTextField();
    private JTextField txtnumero1titular = new JTextField();
    private JTextField txtletra2titular = new JTextField();
    private JLabel letra1parlbl3 = new JLabel();
    private JTextField txtletra1titular = new JTextField();
    private JLabel numero1cargolbl1 = new JLabel();
    private JLabel codigopostalcargolbl1 = new JLabel();
    private JTextField txtcodigopostaltitular = new JTextField();
    private JLabel nombreviacargolbl1 = new JLabel();
    private JTextField txtnombreviatitular = new JTextField();
    private JTextField txtapartadocorreostitular = new JTextField();
    private JLabel apartadocorreostitularlbl = new JLabel();
    private JTextField nombreprovinciatitulartxt = new JTextField();
    private JLabel nombreprovinciatitularlbl = new JLabel();
    private JTextField nombremunicipiotitulartxt = new JTextField();
    private JLabel nombremunicipiotitularlbl = new JLabel();
    private JComboBox cmbTipoVia = new JComboBox();
    private JLabel lblTipoVia = new JLabel();
    private JTextField txtCodigoVia = new JTextField();
    private JLabel lblCodigoVia = new JLabel();
    
    
    //Datos del catastro
    private JLabel lblCodigoEntidadMenor = new JLabel();
    private JTextField txtCodigoEntidadMenor = new JTextField(); 
    private JLabel lblClaveInterna = new JLabel();
    private JTextField txtClaveInterna = new JTextField();
    private JLabel lblDerechoPrevalente = new JLabel();
    private JTextField txtDerechoPrevalente = new JTextField();
    private JLabel lblNumeroTitulares = new JLabel();
    private JTextField txtNumeroTitulares = new JTextField();
    private JLabel lblTipoTitulares = new JLabel();
    private JTextField txtTipoTitulares = new JTextField();
    private JLabel lblComplementoTitularidad = new JLabel();
    private JTextField txtComplementoTitularidad = new JTextField();
    
    
    //Botones 
    private JButton btnModificar = new JButton();
    private JButton btnNuevo = new JButton();
    private JButton btnBorrar = new JButton();
    
    
    //Otras variables
    public int ID_Titular;
    public boolean alta=false;
    public boolean change=false;
    //private JLabel lblMensaje = new JLabel();
    
    private ArrayList SujetoPasivo= new ArrayList();
    
    
    public SujetoPasivoPanel()
    {
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
    public static void main(String[] args)
    {
        AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
        JFrame frame1 = new JFrame(aplicacion.getI18nString("catastro.sujetopasivo.panel.titulo"));
        
        SujetoPasivoPanel geopistaEditarDatos = new SujetoPasivoPanel();
        
        frame1.getContentPane().add(geopistaEditarDatos);
        frame1.setSize(650, 475);
        frame1.setVisible(true); 
        frame1.setLocation(150, 90);
        
    }
    
    
    private void jbInit() throws Exception
    {
        AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
        this.setName(aplicacion.getI18nString("catastro.sujetopasivo.panel.nombre"));
        this.setLayout(null);
        this.setSize(new Dimension(600, 550));
        this.setBounds(new java.awt.Rectangle(5,10,636,493)); 
        
        //lblMensaje.setText("");
        //lblMensaje.setVisible(false);
        //lblMensaje.setBounds(new Rectangle(50, 382, 110, 20));
        
        SujetoPasivo.add(txtniftitular);
        SujetoPasivo.add(txtnombretitular);
        SujetoPasivo.add(txtletra1titular);
        SujetoPasivo.add(txtnumero1titular);
        SujetoPasivo.add(txtletra2titular);
        SujetoPasivo.add(txtnumero2titular);   
        SujetoPasivo.add(txtkilometrotitular);
        SujetoPasivo.add(txtbloquetitular);
        SujetoPasivo.add(txtdirnoestructitular);
        SujetoPasivo.add(txtescaleratitular);
        SujetoPasivo.add(txtplantatitular);
        SujetoPasivo.add(txtpuertatitular);
        SujetoPasivo.add(txtcodigopostaltitular);
        SujetoPasivo.add(txtapartadocorreostitular);
        SujetoPasivo.add(nombreprovinciatitulartxt);
        SujetoPasivo.add(nombremunicipiotitulartxt);
        SujetoPasivo.add(txtnombreviatitular);
        
        SujetoPasivo.add(txtCodigoVia);
        SujetoPasivo.add(txtCodigoEntidadMenor);
        SujetoPasivo.add(cmbTipoVia);
        
        SujetoPasivo.add(txtnombretitularConyuge);
        SujetoPasivo.add(txtniftitularConyuge);
        SujetoPasivo.add(txtClaveInterna);
        SujetoPasivo.add(txtDerechoPrevalente);
        SujetoPasivo.add(txtNumeroTitulares);
        SujetoPasivo.add(txtTipoTitulares);
        SujetoPasivo.add(txtComplementoTitularidad);
        
        
        
        jPanelDatosIdentSujeto.setBounds(new Rectangle(5, 10, 515, 80));
        jPanelDatosIdentSujeto.setLayout(null);
        jPanelDatosIdentSujeto.setBorder(BorderFactory.createTitledBorder(aplicacion.getI18nString("catastro.sujetopasivo.identificacion")));
        
        jPanelDomicilioFiscal.setBounds(new Rectangle(5, 88, 515, 186));
        jPanelDomicilioFiscal.setLayout(null);
        jPanelDomicilioFiscal.setBorder(BorderFactory.createTitledBorder(aplicacion.getI18nString("catastro.sujetopasivo.domicilio")));
        
        
        jPanelDatosCatastro.setBounds(new Rectangle(5, 274, 515, 107));
        jPanelDatosCatastro.setLayout(null);
        jPanelDatosCatastro.setBorder(BorderFactory.createTitledBorder(aplicacion.getI18nString("catastro.sujetopasivo.datos")));
        
        
        //Panel Identificacion del sujeto pasivo
        niftitularlbl.setText(aplicacion.getI18nString("catastro.sujetopasivo.identificacion.titular"));
        niftitularlbl.setBounds(new Rectangle(7, 24, 78, 15));
        txtniftitular.setText("");
        txtniftitular.setBounds(new Rectangle(90, 24, 90, 20));
        
        nombreApellidoslbl1.setText(aplicacion.getI18nString("catastro.sujetopasivo.nombre.apellidos"));
        nombreApellidoslbl1.setBounds(new Rectangle(192, 24, 100, 15));
        txtnombretitular.setText("");
        txtnombretitular.setBounds(new Rectangle(297, 24, 190, 20));  
        
        niftitularConyugelbl.setText(aplicacion.getI18nString("catastro.sujetopasivo.identificacion.conyuge"));
        niftitularConyugelbl.setBounds(new Rectangle(7, 52, 82, 15));
        txtniftitularConyuge.setText("");
        txtniftitularConyuge.setBounds(new Rectangle(90, 52, 90, 20));
        
        nombreApellidosConyugelbl1.setText(aplicacion.getI18nString("catastro.sujetopasivo.nombre.apellidos"));
        nombreApellidosConyugelbl1.setBounds(new Rectangle(194, 52, 100, 15));
        txtnombretitularConyuge.setText("");
        txtnombretitularConyuge.setBounds(new Rectangle(297, 52, 190, 20));  
        
        //Panel Domicilio fiscal del sujeto pasivo
        lblTipoVia.setText(aplicacion.getI18nString("catastro.titulares.tipo.via"));
        lblTipoVia.setBounds(new Rectangle(5, 26, 68, 15));
        cmbTipoVia.addItem("");
        cmbTipoVia.addItem("NE");
        cmbTipoVia.addItem("AVDA");
        cmbTipoVia.addItem("CALLE");
        cmbTipoVia.addItem("CLLON");
        cmbTipoVia.addItem("TRVA");
        cmbTipoVia.setBounds(new Rectangle(90, 26, 68, 20));
        
        lblCodigoVia.setText(aplicacion.getI18nString("catastro.titulares.codigo.via"));
        lblCodigoVia.setBounds(new Rectangle(175, 26, 68, 15));
        txtCodigoVia.setText("");
        txtCodigoVia.setBounds(new Rectangle(250, 26, 68, 20));
        
        codigopostalcargolbl1.setText(aplicacion.getI18nString("catastro.titulares.codigopostal"));
        codigopostalcargolbl1.setBounds(new Rectangle(335, 26, 85, 15));
        txtcodigopostaltitular.setText("");
        txtcodigopostaltitular.setBounds(new Rectangle(428, 26, 50, 20));
        
        nombreviacargolbl1.setText(aplicacion.getI18nString("catastro.titulares.direccion"));
        nombreviacargolbl1.setBounds(new Rectangle(5, 53, 70, 15));
        txtnombreviatitular.setText("");
        txtnombreviatitular.setBounds(new Rectangle(90, 53, 230, 20));
        
        apartadocorreostitularlbl.setText(aplicacion.getI18nString("catastro.titulares.apartadocorreos"));
        apartadocorreostitularlbl.setBounds(new Rectangle(335, 53, 100, 20));
        txtapartadocorreostitular.setText("");
        txtapartadocorreostitular.setBounds(new Rectangle(428, 53, 50, 20));
        
        numero1cargolbl1.setText(aplicacion.getI18nString("catastro.titulares.primernumero"));
        numero1cargolbl1.setBounds(new Rectangle(5, 80, 80, 15));
        txtnumero1titular.setText("");
        txtnumero1titular.setBounds(new Rectangle(90, 80, 40, 20));
        
        numero2parlbl6.setText(aplicacion.getI18nString("catastro.titulares.segundonumero"));
        numero2parlbl6.setBounds(new Rectangle(140, 80, 90, 15));
        txtnumero2titular.setText("");
        txtnumero2titular.setBounds(new Rectangle(235, 80, 40, 20));
        
        letra1parlbl3.setText(aplicacion.getI18nString("catastro.titulares.primeraletra"));
        letra1parlbl3.setBounds(new Rectangle(285, 80, 70, 15));
        txtletra1titular.setText("");
        txtletra1titular.setBounds(new Rectangle(360, 80, 20, 20));
        
        letra2parlbl3.setText(aplicacion.getI18nString("catastro.titulares.segundaletra"));
        letra2parlbl3.setBounds(new Rectangle(390, 80, 75, 15));
        txtletra2titular.setText("");
        txtletra2titular.setBounds(new Rectangle(470, 80, 20, 20));
        
        bloqueparlbl4.setText(aplicacion.getI18nString("catastro.titulares.bloque"));
        bloqueparlbl4.setBounds(new Rectangle(5, 107, 45, 15));
        txtbloquetitular.setText("");
        txtbloquetitular.setBounds(new Rectangle(55, 107, 45, 20));
        
        kmparlbl3.setText(aplicacion.getI18nString("catastro.titulares.kilometro"));
        kmparlbl3.setBounds(new Rectangle(110, 107, 55, 15));
        txtkilometrotitular.setText("");
        txtkilometrotitular.setBounds(new Rectangle(160, 107, 70, 20));
        
        escaleralbl2.setText(aplicacion.getI18nString("catastro.titulares.escalera"));
        escaleralbl2.setBounds(new Rectangle(240, 107, 45, 15));
        txtescaleratitular.setText("");
        txtescaleratitular.setBounds(new Rectangle(290, 107, 30, 20));
        
        plantaconslbl2.setText(aplicacion.getI18nString("catastro.titulares.planta"));
        plantaconslbl2.setBounds(new Rectangle(330, 107, 40, 15));
        txtplantatitular.setText("");
        txtplantatitular.setBounds(new Rectangle(370, 107, 30, 20));
        
        puertaconslbl2.setText(aplicacion.getI18nString("catastro.titulares.puerta"));
        puertaconslbl2.setBounds(new Rectangle(410, 107, 35, 15));
        txtpuertatitular.setText("");
        txtpuertatitular.setBounds(new Rectangle(450, 107, 30, 20));
        
        nombremunicipiotitularlbl.setText(aplicacion.getI18nString("catastro.titulares.municipio"));
        nombremunicipiotitularlbl.setBounds(new Rectangle(5, 134, 55, 15));
        nombremunicipiotitulartxt.setText("");
        nombremunicipiotitulartxt.setBounds(new Rectangle(70, 134, 200, 20));
        
        nombreprovinciatitularlbl.setText(aplicacion.getI18nString("catastro.titulares.provincia"));
        nombreprovinciatitularlbl.setBounds(new Rectangle(280, 134, 50, 15));
        nombreprovinciatitulartxt.setBounds(new Rectangle(335, 134, 120, 20));
        nombreprovinciatitulartxt.setText("");
        
        dirnoestrucparlbl3.setText(aplicacion.getI18nString("catastro.titulares.dirnoestructurada"));
        dirnoestrucparlbl3.setBounds(new Rectangle(5, 159, 130, 15));
        txtdirnoestructitular.setText("");
        txtdirnoestructitular.setBounds(new Rectangle(140, 159, 315, 20));
        
        
        //Panel Datos catastrales
        lblCodigoEntidadMenor.setText(aplicacion.getI18nString("catastro.titulares.codigoentidadmenor"));
        lblCodigoEntidadMenor.setBounds(new Rectangle(5, 25, 123, 15));
        txtCodigoEntidadMenor.setText("");
        txtCodigoEntidadMenor.setBounds(new Rectangle(135, 25, 25, 20));
        
        lblClaveInterna.setText(aplicacion.getI18nString("catastro.sujetopasivo.claveinterna"));
        lblClaveInterna.setBounds(new Rectangle(170, 25, 80, 15));
        txtClaveInterna.setText("");
        txtClaveInterna.setBounds(new Rectangle(255, 25, 80, 20));
        
        lblDerechoPrevalente.setText(aplicacion.getI18nString("catastro.sujetopasivo.derechoprevalente"));
        lblDerechoPrevalente.setBounds(new Rectangle(5, 52, 120, 15));
        txtDerechoPrevalente.setText("");
        txtDerechoPrevalente.setBounds(new Rectangle(135, 52, 25, 20));
        
        lblNumeroTitulares.setText(aplicacion.getI18nString("catastro.sujetopasivo.numerotitulares"));
        lblNumeroTitulares.setBounds(new Rectangle(170, 52, 100, 15));
        txtNumeroTitulares.setText("");
        txtNumeroTitulares.setBounds(new Rectangle(275, 52, 40, 20));
        
        lblTipoTitulares.setText(aplicacion.getI18nString("catastro.sujetopasivo.tipotitulares"));
        lblTipoTitulares.setBounds(new Rectangle(330, 52, 90, 15));
        txtTipoTitulares.setText("");
        txtTipoTitulares.setBounds(new Rectangle(425, 52, 20, 20));        
        
        
        lblComplementoTitularidad.setText(aplicacion.getI18nString("catastro.sujetopasivo.complemento"));
        lblComplementoTitularidad.setBounds(new Rectangle(5, 79, 210, 15));
        txtComplementoTitularidad.setText("");
        txtComplementoTitularidad.setBounds(new Rectangle(220, 79, 225, 20));
        
        
        btnModificar.setText(aplicacion.getI18nString("catastro.general.aplicar"));
        btnModificar.setBounds(new Rectangle(200, 382, 80, 20));
        btnModificar.addActionListener(new ActionListener()
                {
            public void actionPerformed(ActionEvent e)
            {
                btnModificar_actionPerformed(e);
            }
                });
        btnNuevo.setText(aplicacion.getI18nString("catastro.general.alta"));
        btnNuevo.setBounds(new Rectangle(300, 382, 80, 20));
        btnNuevo.addActionListener(new ActionListener()
                {
            public void actionPerformed(ActionEvent e)
            {
                btnNuevo_actionPerformed(e);
            }
                });
        btnBorrar.setText(aplicacion.getI18nString("catastro.general.baja"));
        btnBorrar.setBounds(new Rectangle(400, 382, 80, 20));
        
        
        
        jPanelDatosIdentSujeto.add(nombreApellidoslbl1, null);
        jPanelDatosIdentSujeto.add(txtnombretitular, null);
        jPanelDatosIdentSujeto.add(txtniftitular, null);
        jPanelDatosIdentSujeto.add(niftitularlbl, null);
        jPanelDatosIdentSujeto.add(nombreApellidosConyugelbl1, null);
        jPanelDatosIdentSujeto.add(txtnombretitularConyuge, null);
        jPanelDatosIdentSujeto.add(txtniftitularConyuge, null);
        jPanelDatosIdentSujeto.add(niftitularConyugelbl, null);
        
        
        //cmbpersonalidadtitular.addItem("F - Física");
        //cmbpersonalidadtitular.addItem("J - Jurídica");
        //cmbpersonalidadtitular.addItem("E - Entidades");
        
        jPanelDomicilioFiscal.add(cmbTipoVia, null);
        jPanelDomicilioFiscal.add(lblTipoVia, null);
        jPanelDomicilioFiscal.add(txtCodigoVia, null);
        jPanelDomicilioFiscal.add(lblCodigoVia, null);
        jPanelDomicilioFiscal.add(nombremunicipiotitularlbl, null);
        jPanelDomicilioFiscal.add(nombremunicipiotitulartxt, null);
        jPanelDomicilioFiscal.add(nombreprovinciatitularlbl, null);
        jPanelDomicilioFiscal.add(nombreprovinciatitulartxt, null);
        jPanelDomicilioFiscal.add(apartadocorreostitularlbl, null);
        jPanelDomicilioFiscal.add(txtapartadocorreostitular, null);
        jPanelDomicilioFiscal.add(escaleralbl2, null);
        jPanelDomicilioFiscal.add(txtescaleratitular, null);
        jPanelDomicilioFiscal.add(plantaconslbl2, null);
        jPanelDomicilioFiscal.add(txtplantatitular, null);
        jPanelDomicilioFiscal.add(puertaconslbl2, null);
        jPanelDomicilioFiscal.add(txtpuertatitular, null);
        jPanelDomicilioFiscal.add(txtdirnoestructitular, null);
        jPanelDomicilioFiscal.add(dirnoestrucparlbl3, null);
        jPanelDomicilioFiscal.add(kmparlbl3, null);
        jPanelDomicilioFiscal.add(txtbloquetitular, null);
        jPanelDomicilioFiscal.add(bloqueparlbl4, null);
        jPanelDomicilioFiscal.add(txtkilometrotitular, null);    
        jPanelDomicilioFiscal.add(letra2parlbl3, null);
        jPanelDomicilioFiscal.add(numero2parlbl6, null);
        jPanelDomicilioFiscal.add(txtnumero2titular, null);
        jPanelDomicilioFiscal.add(txtnumero1titular, null);
        jPanelDomicilioFiscal.add(txtletra2titular, null);
        jPanelDomicilioFiscal.add(letra1parlbl3, null);
        jPanelDomicilioFiscal.add(txtletra1titular, null);
        jPanelDomicilioFiscal.add(numero1cargolbl1, null);
        jPanelDomicilioFiscal.add(codigopostalcargolbl1, null);
        jPanelDomicilioFiscal.add(txtcodigopostaltitular, null);
        jPanelDomicilioFiscal.add(nombreviacargolbl1, null);
        jPanelDomicilioFiscal.add(txtnombreviatitular, null);
        
        jPanelDatosCatastro.add(txtCodigoEntidadMenor, null);
        jPanelDatosCatastro.add(lblCodigoEntidadMenor, null);
        jPanelDatosCatastro.add(txtClaveInterna, null);
        jPanelDatosCatastro.add(lblClaveInterna, null);
        jPanelDatosCatastro.add(txtDerechoPrevalente, null);
        jPanelDatosCatastro.add(lblDerechoPrevalente, null);
        jPanelDatosCatastro.add(txtNumeroTitulares, null);
        jPanelDatosCatastro.add(lblNumeroTitulares, null);
        jPanelDatosCatastro.add(txtTipoTitulares, null);
        jPanelDatosCatastro.add(lblTipoTitulares, null);
        jPanelDatosCatastro.add(txtComplementoTitularidad, null);
        jPanelDatosCatastro.add(lblComplementoTitularidad, null);
        
        
        this.add(jPanelDomicilioFiscal, null);
        this.add(jPanelDatosIdentSujeto, null);
        this.add(jPanelDatosCatastro, null);
        
        this.add(btnBorrar, null);
        this.add(btnNuevo, null);
        this.add(btnModificar, null);
        //this.add(lblMensaje, null);
        
        
        Iterator alIt1 = SujetoPasivo.iterator();
        
        
        btnBorrar.addActionListener(new ActionListener()
                {
            public void actionPerformed(ActionEvent e)
            {
                btnBorrar_actionPerformed(e);
            }
                });
        
        while (alIt1.hasNext()) 
        {
            try
            {
                JComponent comp=(JComponent)alIt1.next();
                //if (comp instanceof JTextField)((JTextField)comp).setText("");
                if (comp instanceof JCheckBox) ((JCheckBox)comp).setSelected(false);
                if (comp instanceof JComboBox)((JComboBox)comp).setSelectedIndex(-1);
            }
            
            catch(Exception A)
            {
                A.printStackTrace();
            }
        }
        
        
        
        txtnombretitular.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        txtniftitular.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        txtnombretitularConyuge.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        }); 
        txtniftitularConyuge.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        txtescaleratitular.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        txtplantatitular.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        txtpuertatitular.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        }); 
        txtdirnoestructitular.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        }); 
        txtbloquetitular.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        }); 
        txtkilometrotitular.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        txtnumero2titular.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        txtnumero1titular.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        txtletra2titular.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        txtletra1titular.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        txtcodigopostaltitular.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        txtnombreviatitular.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        txtapartadocorreostitular.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        }); 
        nombreprovinciatitulartxt.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        nombremunicipiotitulartxt.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        cmbTipoVia.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e)
            {
                componentModified_actionPerformed();
                
            }
        });
        txtCodigoVia.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        txtCodigoEntidadMenor.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        txtClaveInterna.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        txtDerechoPrevalente.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        txtNumeroTitulares.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        txtTipoTitulares.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        txtComplementoTitularidad.addKeyListener(new KeyListener() {            
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {                
            }
            public void keyTyped(KeyEvent e)
            {   
                componentModified_actionPerformed();                 
            }
        });
        
        
        
        
    }
    
    private void btnModificar_actionPerformed(ActionEvent e)
    
    {
        AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
        //lblMensaje.setVisible(false);
        ArrayList UC= new ArrayList();
        ArrayList UCTipo= new ArrayList();
        
        AppContext app =(AppContext) AppContext.getApplicationContext();
        Blackboard Identificadores = app.getBlackboard();
        String idSujetoPasivo= "";
        
        if (!alta){
            idSujetoPasivo= Identificadores.get("ID_SujetoPasivo").toString();  
            
        }
        
        
        //Con esta opción modificamos los datos del sujeto pasivo
        //Creamos un ArrayList con los datos recogidos del formulario en el mismo orden que la base de datos    
        UC.add(idSujetoPasivo);
        UCTipo.add("0");
        UC.add(txtniftitular.getText());
        UCTipo.add("1");
        UC.add(txtnombretitular.getText());
        UCTipo.add("1");   
        UC.add(txtletra1titular.getText());
        UCTipo.add("1");
        UC.add(txtnumero1titular.getText());
        UCTipo.add("0");  
        UC.add(txtletra2titular.getText());
        UCTipo.add("1");  
        UC.add(txtnumero2titular.getText());
        UCTipo.add("0");      
        UC.add(txtkilometrotitular.getText());
        UCTipo.add("2");    
        UC.add(txtbloquetitular.getText());
        UCTipo.add("1");      
        UC.add(txtdirnoestructitular.getText());
        UCTipo.add("1");    
        UC.add(txtescaleratitular.getText());
        UCTipo.add("1");
        UC.add(txtplantatitular.getText());
        UCTipo.add("1");  
        UC.add(txtpuertatitular.getText());
        UCTipo.add("1");   
        UC.add(txtcodigopostaltitular.getText());
        UCTipo.add("1");    
        UC.add(txtapartadocorreostitular.getText());
        UCTipo.add("1");
        UC.add(nombreprovinciatitulartxt.getText());
        UCTipo.add("3");   
        UC.add(nombremunicipiotitulartxt.getText());
        UCTipo.add("4");  
        UC.add(txtnombreviatitular.getText());
        UCTipo.add("1");  
        
        UC.add(txtCodigoVia.getText());
        UCTipo.add("0");  
        UC.add(txtCodigoEntidadMenor.getText());
        UCTipo.add("1");  
        UC.add(cmbTipoVia.getSelectedItem()==null?"":cmbTipoVia.getSelectedItem().toString());
        UCTipo.add("1");  
        
        UC.add(txtnombretitularConyuge.getText());
        UCTipo.add("1");  
        UC.add(txtniftitularConyuge.getText());
        UCTipo.add("1");  
        
        UC.add(txtClaveInterna.getText());
        UCTipo.add("0");  
        UC.add(txtDerechoPrevalente.getText());
        UCTipo.add("1");  
        
        
        UC.add(txtNumeroTitulares.getText());
        UCTipo.add("0");  
        UC.add(txtTipoTitulares.getText());
        UCTipo.add("0");  
        UC.add(txtComplementoTitularidad.getText());
        UCTipo.add("1");  
        
        
        
        //Actualizamos la información almacenada
        CatastroActualizarPostgre cap = new CatastroActualizarPostgre();
        
        //Actualizacion de datos de sujeto pasivo
        if (!alta){
            String Result = cap.ActualizarSujetoPasivo(UC, UCTipo);        
            if (Result.equals("Error-1")){
                nombreprovinciatitulartxt.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                nombremunicipiotitulartxt.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                nombremunicipiotitulartxt.setBorder(BorderFactory.createLoweredBevelBorder());
                //System.out.println(aplicacion.getI18nString("catastro.sujetopasivo.error.actualizar")+ aplicacion.getI18nString("catastro.general.error.provincia"));
                
            }else if (Result.equals("Error-2")){
                nombremunicipiotitulartxt.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                nombreprovinciatitulartxt.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                nombreprovinciatitulartxt.setBorder(BorderFactory.createLoweredBevelBorder());
                //System.out.println(aplicacion.getI18nString("catastro.sujetopasivo.error.actualizar")+ aplicacion.getI18nString("catastro.general.error.municipio"));
            }else{
                //System.out.println(Result);
                nombremunicipiotitulartxt.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                nombreprovinciatitulartxt.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                nombremunicipiotitulartxt.setBorder(BorderFactory.createLoweredBevelBorder());
                nombreprovinciatitulartxt.setBorder(BorderFactory.createLoweredBevelBorder());
                
                if (Result.substring(0,5).equals("Error")){
                    //lblMensaje.setText(aplicacion.getI18nString("catastro.general.error.aviso"));
                    //lblMensaje.setVisible(true);
                }
                else{
                    btnBorrar.setEnabled(true);
                }
                
                
            }
        }
        //Alta de sujeto pasivo 
        else{
            nombremunicipiotitulartxt.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            nombreprovinciatitulartxt.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            nombremunicipiotitulartxt.setBorder(BorderFactory.createLoweredBevelBorder());
            nombreprovinciatitulartxt.setBorder(BorderFactory.createLoweredBevelBorder());
            
            String Result = cap.AltaSujetoPasivo(UC, UCTipo);
            
            
            if (Result.equals("Error-1")){
                nombreprovinciatitulartxt.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                nombremunicipiotitulartxt.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                nombremunicipiotitulartxt.setBorder(BorderFactory.createLoweredBevelBorder());
                //System.out.println(aplicacion.getI18nString("catastro.sujetopasivo.error.alta")+ aplicacion.getI18nString("catastro.general.error.provincia"));
                
            }else if (Result.equals("Error-2")){
                nombremunicipiotitulartxt.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                nombreprovinciatitulartxt.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                nombreprovinciatitulartxt.setBorder(BorderFactory.createLoweredBevelBorder());
                //System.out.println(aplicacion.getI18nString("catastro.sujetopasivo.error.alta")+ aplicacion.getI18nString("catastro.general.error.municipio")); 
            }else{             
                nombremunicipiotitulartxt.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                nombreprovinciatitulartxt.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                nombremunicipiotitulartxt.setBorder(BorderFactory.createLoweredBevelBorder());
                nombreprovinciatitulartxt.setBorder(BorderFactory.createLoweredBevelBorder());
                
                if (Result.length()>=5 && Result.substring(0,5).equals("Error")){
                    //lblMensaje.setText(aplicacion.getI18nString("catastro.general.error.aviso"));
                    //lblMensaje.setVisible(true);
                    return;
                }
                else{
                    btnBorrar.setEnabled(true);
                    Identificadores.put("ID_SujetoPasivo", Result);
                }
            }        
        }
        alta=false;
        btnModificar.setEnabled(false);
    }
    
    private void btnNuevo_actionPerformed(ActionEvent e)
    {
        //lblMensaje.setVisible(false);
        
        
        alta=true; 
        Iterator alIt = SujetoPasivo.iterator();
        
        nombremunicipiotitulartxt.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        nombreprovinciatitulartxt.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        nombremunicipiotitulartxt.setBorder(BorderFactory.createLoweredBevelBorder());
        nombreprovinciatitulartxt.setBorder(BorderFactory.createLoweredBevelBorder());
        //lblMensaje.setText("");
               
        
        while (alIt.hasNext()) 
        {
            try
            {
                JComponent comp=(JComponent)alIt.next();
                if (comp instanceof JTextField)((JTextField)comp).setText("");
                if (comp instanceof JCheckBox) ((JCheckBox)comp).setSelected(false);
                if (comp instanceof JComboBox)((JComboBox)comp).setSelectedIndex(-1);
            }
            catch(Exception A)
            {
                A.printStackTrace();
            }
        }   
        
        btnBorrar.setEnabled(false);
        btnNuevo.setEnabled(false);
        btnModificar.setEnabled(false);
    }
    
    
    
    public void enter()
    {
        AppContext app =(AppContext) AppContext.getApplicationContext();
        Blackboard Identificadores = app.getBlackboard();
        
        
        
        alta=false;
        
        //Busca el sujeto pasivo de la parcela y sus datos
        CatastroActualizarPostgre cap = new CatastroActualizarPostgre();
        int ID_Parcela= Integer.parseInt(Identificadores.get("ID_Parcela").toString());
        
        
        ArrayList Datos= cap.DatosSujetoPasivo(ID_Parcela);
        if (Datos==null){ 
            
            btnBorrar.setEnabled(false);
            return;    
            
        }
        
        
        nombremunicipiotitulartxt.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        nombreprovinciatitulartxt.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        nombremunicipiotitulartxt.setBorder(BorderFactory.createLoweredBevelBorder());
        nombreprovinciatitulartxt.setBorder(BorderFactory.createLoweredBevelBorder());
        //lblMensaje.setText("");
        //btnBorrar.setEnabled(false);
        
        
        Iterator alIt = Datos.iterator();
        Iterator itControles = SujetoPasivo.iterator();
        while (itControles.hasNext()) 
        {
            try
            {
                JComponent comp=(JComponent)itControles.next();
                Object obj=alIt.next();
                if (comp instanceof JTextField)((JTextField)comp).setText((obj!=null)?obj.toString():"");
                if (comp instanceof JCheckBox){
                    String check = (obj!=null)?obj.toString():"";
                    if (check == "TRUE"){
                        ((JCheckBox)comp).setSelected(true);}
                    else{
                        ((JCheckBox)comp).setSelected(false);}}
                if (comp instanceof JComboBox)((JComboBox)comp).setSelectedItem((obj!=null)?obj.toString():"");
                
            }
            catch(Exception A)
            {
                A.printStackTrace();
            }	          
        }      
        
        
        if(Identificadores.get("ID_SujetoPasivo")==null){
            btnBorrar.setEnabled(false);
            btnModificar.setEnabled(false);
            btnNuevo.setEnabled(true);
            
        }else{
            btnBorrar.setEnabled(true);     
            btnModificar.setEnabled(false);
            btnNuevo.setEnabled(false);
        }
        
    }
    
    public void exit()
    {
    }
    
    private void btnBorrar_actionPerformed(ActionEvent e)
    {
        
        AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
        
        Object options[] = {aplicacion.getI18nString("catastro.confirmacion.si"), aplicacion.getI18nString("catastro.confirmacion.no")};
        int index =
            JOptionPane.showOptionDialog(null, // parent
                    aplicacion.getI18nString("catastro.confirmacion.mensaje"), // message object
                    aplicacion.getI18nString("catastro.confirmacion.titulo"), // string title
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, // Icon
                    options,
                    options[1]
            );
        switch (index) {
        case 0:
            
            Blackboard Identificadores = aplicacion.getBlackboard();
            int ID_Parcela= Integer.parseInt(Identificadores.get("ID_Parcela").toString());      
            
            //lblMensaje.setVisible(false);
            
            CatastroActualizarPostgre cap = new CatastroActualizarPostgre();
            String Result = cap.BajaSujetoPasivo (ID_Parcela); 
            
            if (Result.equals("OK")){
                enter();
            }
            
            break;
        case 1:
            break;
            
        }
        
        
    }
    
    private void componentModified_actionPerformed()
    {            
        btnModificar.setEnabled(true);
    }
    
    
    
}
