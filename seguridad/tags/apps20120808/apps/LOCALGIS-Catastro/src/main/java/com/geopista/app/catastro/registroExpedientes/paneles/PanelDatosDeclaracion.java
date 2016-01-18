package com.geopista.app.catastro.registroExpedientes.paneles;

import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp_LCGIII;
import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.app.AppContext;
import com.vividsolutions.jump.I18N;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.border.TitledBorder;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 09-ene-2007
 * Time: 16:41:35
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones, o metodos para recoger los datos que ha introducido el usuario.
 */

public class PanelDatosDeclaracion extends JPanel implements IMultilingue
{
    private String etiqueta;
    private JLabel tipoDocumentoOrigenAltLabel;
    private JRadioButton tipoDocumentoOrigenAltPublicoRButton;
    private JLabel tipoDocumentoOrigenAltPublicoLabel;
    private JRadioButton tipoDocumentoOrigenAltPrivadoRButton;
    private JLabel tipoDocumentoOrigenAltPrivadoLabel;
    private ButtonGroup tipoDocumentoOrigenAltGroupButton;
    private JLabel infDocumentoOrigenAltLabel;
    private JTextField infDocumentoOrigenAltJTfield;
    private JLabel numBienesInmueblesLabel;
    private JLabel numeroBienesInmuebleUrbLabel;
    private JTextField numeroBienesInmuebleUrbJTfield;
    private JLabel numeroBienesInmueblesRustLabel;
    private JTextField numeroBienesInmueblesRustJTfield;
    private JLabel numeroBienesInmueblesCELabel;
    private JTextField numeroBienesInmueblesCEJTfield;
    private JLabel codigoDescriptivoAltLabel;
    private ComboBoxEstructuras codigoDescriptivoAltJCBox;
    private JLabel descripcionAltLabel;
    private JTextPane descripcionAltJTPane;
    private JScrollPane descripcionAltJScrollPane;

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     */
    public PanelDatosDeclaracion(String label)
    {
        etiqueta= label;
        inicializaPanel();
    }

    /**
     *  Inicializa todos los elementos del panel y los coloca en su posicion.
     */
    private void inicializaPanel()
    {
        tipoDocumentoOrigenAltLabel = new JLabel();
        tipoDocumentoOrigenAltPublicoRButton = new JRadioButton();
        tipoDocumentoOrigenAltPublicoLabel = new JLabel();
        tipoDocumentoOrigenAltPrivadoRButton = new JRadioButton();
        tipoDocumentoOrigenAltPrivadoLabel = new JLabel();
        tipoDocumentoOrigenAltGroupButton = new ButtonGroup();
        infDocumentoOrigenAltLabel = new JLabel();
        infDocumentoOrigenAltJTfield = new JTextField();
        numBienesInmueblesLabel = new JLabel();
        numeroBienesInmuebleUrbLabel = new JLabel();
        numeroBienesInmuebleUrbJTfield = new JTextField();
        numeroBienesInmueblesRustLabel = new JLabel();
        numeroBienesInmueblesRustJTfield = new JTextField();
        numeroBienesInmueblesCELabel = new JLabel();
        numeroBienesInmueblesCEJTfield = new JTextField();
        codigoDescriptivoAltLabel = new JLabel();
        descripcionAltLabel = new JLabel();
        descripcionAltJTPane = new JTextPane();
        descripcionAltJScrollPane = new JScrollPane();

        inicializaComboBox();
        inicializarEventos();
        setEditable(true);
        renombrarComponentes();

        tipoDocumentoOrigenAltGroupButton.add(tipoDocumentoOrigenAltPublicoRButton);
        tipoDocumentoOrigenAltGroupButton.add(tipoDocumentoOrigenAltPrivadoRButton);
        tipoDocumentoOrigenAltGroupButton.setSelected(tipoDocumentoOrigenAltPublicoRButton.getModel(), true);

        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.add(tipoDocumentoOrigenAltLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 195, 20));
        this.add(tipoDocumentoOrigenAltPublicoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, 50, 20));
        this.add(tipoDocumentoOrigenAltPublicoRButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, 20, 20));
        this.add(tipoDocumentoOrigenAltPrivadoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 50, 20));
        this.add(tipoDocumentoOrigenAltPrivadoRButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 20, 20, 20));

        this.add(infDocumentoOrigenAltLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 195, 20));
        this.add(infDocumentoOrigenAltJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, 250, -1));

        this.add(numBienesInmueblesLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 65, 200, -1));

        this.add(numeroBienesInmuebleUrbLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 60, 20));
        this.add(numeroBienesInmuebleUrbJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, 50, -1));
        this.add(numeroBienesInmueblesRustLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, 60, 20));
        this.add(numeroBienesInmueblesRustJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 90, 50, -1));
        this.add(numeroBienesInmueblesCELabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 90, 140, 20));
        this.add(numeroBienesInmueblesCEJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 90, 50, -1));

        this.add(codigoDescriptivoAltLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 115, 195, 20));
        this.add(codigoDescriptivoAltJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(230,115, 250, 20));
        this.add(descripcionAltLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 135, 195, 20));
        descripcionAltJScrollPane.setViewportView(descripcionAltJTPane);
        this.add(descripcionAltJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(230,140, 250, 50));
    }

    /**
     * Inicializa los eventos de los elementos de la gui si son necesarios.
     * En este caso inicializa los eventos para checkear la valided de los datos
     * introducidos en los campos.
     */
    private void inicializarEventos()
    {
        infDocumentoOrigenAltJTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(infDocumentoOrigenAltJTfield, 50);
            }
        });

        numeroBienesInmuebleUrbJTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongYNumCampoEdit(numeroBienesInmuebleUrbJTfield, 4);
            }
        });

        numeroBienesInmueblesRustJTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongYNumCampoEdit(numeroBienesInmueblesRustJTfield, 4);
            }
        });

        numeroBienesInmueblesCEJTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongYNumCampoEdit(numeroBienesInmueblesCEJTfield, 4);
            }
        });

        descripcionAltJTPane.setEditable(true);
        descripcionAltJTPane.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(descripcionAltJTPane,400);
            }
        });
    }

    /**
     * Carga los datos del arrayList pasado por parametro en el comboBox de tipos de vias
     *
     */
    private void inicializaComboBox()
    {
        AppContext app =(AppContext) AppContext.getApplicationContext();
        Estructuras.cargarEstructura("Codigo Descripcion Alteracion");
        codigoDescriptivoAltJCBox = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                null, app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        codigoDescriptivoAltJCBox.setEditable(false);        
    }

    /**
     * Checkea que el valor introducido en el campo no excede la longitud pasada
     * por parametro.
     *
     * @param comp El componente editable
     * @param maxLong El tamaño maximo
     */
    private void chequeaLongCampoEdit(final JTextComponent comp,final int maxLong)
    {
        String numAnno= comp.getText();
        if(numAnno.length()>maxLong)
        {
        	UtilRegistroExp_LCGIII.retrocedeCaracter(comp,maxLong);
        }
    }

    /**
     * Checkea que el valor introducido en el campo no excede la longitud pasada
     * por parametro y que solo contiene numeros.
     *
     * @param comp  El componente editable
     * @param maxLong El tamaño maximo
     */
    private void chequeaLongYNumCampoEdit(final JTextComponent comp, final int maxLong)
    {
        String numAnno= comp.getText();
        if(!numAnno.equals(""))
        {
            int x=-1;
            try
            {
                x= Integer.parseInt(numAnno);
            }
            catch(NumberFormatException nFE)
            {
                JOptionPane.showMessageDialog(this, I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.msg1")+ " " +  maxLong + " " + I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.msg2"));
                UtilRegistroExp_LCGIII.retrocedeCaracter(comp,numAnno.length()-1);
            }
            if(numAnno.length()>maxLong )
            {
            	UtilRegistroExp_LCGIII.retrocedeCaracter(comp,maxLong);
            }
        }
    }

   /**
    * Devuelve el panel.
    *
    * @return this
    * */
    public JPanel getDatosPanel()
    {
        return this;
    }

    /**
     * Cambia los campos editables del panel segun el parametro pasado.
     *
     * @param edit
     * */
    public void setEditable(boolean edit)
    {
        infDocumentoOrigenAltJTfield.setEditable(edit);
        numeroBienesInmuebleUrbJTfield.setEditable(edit);
        numeroBienesInmueblesRustJTfield.setEditable(edit);
        numeroBienesInmueblesCEJTfield.setEditable(edit);
        descripcionAltJTPane.setEditable(edit);        
    }

    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        tipoDocumentoOrigenAltLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.tipoDocumentoOrigenAltLabel"));
        tipoDocumentoOrigenAltPublicoLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.tipoDocumentoOrigenAltPublicoLabel"));
        tipoDocumentoOrigenAltPrivadoLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.tipoDocumentoOrigenAltPrivadoLabel"));
        infDocumentoOrigenAltLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.infDocumentoOrigenAltLabel"));
        numBienesInmueblesLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.numBienesInmueblesLabel"));
        numeroBienesInmuebleUrbLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.numeroBienesInmuebleUrbLabel"));
        numeroBienesInmueblesRustLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.numeroBienesInmueblesRustLabel"));
        numeroBienesInmueblesCELabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.numeroBienesInmueblesCELabel"));
        codigoDescriptivoAltLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.codigoDescriptivoAltLabel"));
        descripcionAltLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.descripcionAltLabel"));
    }

    /**
     * Recopila los datos que el usuario ha introducido. Los datos los almacena en la hash pasada por parametro, en
     * la que la clave es una constante en ConstantesRegistroExp que hace referencia a un atributo del bean
     * expediente y el valor lo que el usuario haya introducido. En este caso el valor para uno de los atributos solo
     * puede ser P o R, y se comprueba cual es la eleccion del usuario para almacenar en la hash el valor adecuado con
     * su key adecuada.
     *
     * @param hashDatos Hashtable donde se introducen los datos con keys almacenadas en la clase contantesRegistroExp
     * @return boolean Indica si no se ha producido ningun fallo
     */
    public boolean recopilaDatosPanel(Hashtable hashDatos)
    {
        String tipoDocOrig;
        if(tipoDocumentoOrigenAltGroupButton.isSelected(tipoDocumentoOrigenAltPublicoRButton.getModel()))
        {
            tipoDocOrig = ConstantesRegistroExp.TIPO_DOCUMENTO_PUBLICO;
        }
        else
        {
            tipoDocOrig = ConstantesRegistroExp.TIPO_DOCUMENTO_PRIVADO;
        }
        
        hashDatos.put(ConstantesRegistroExp.expedienteTipoDocumentoOrigenAlteracion, tipoDocOrig);
        hashDatos.put(ConstantesRegistroExp.expedienteInfoDocumentoOrigenAlteracion, infDocumentoOrigenAltJTfield.getText());
        hashDatos.put(ConstantesRegistroExp.expedienteNumBienesInmueblesUrbanos, numeroBienesInmuebleUrbJTfield.getText());
        hashDatos.put(ConstantesRegistroExp.expedienteNumBienesInmueblesRusticos, numeroBienesInmueblesRustJTfield.getText());
        hashDatos.put(ConstantesRegistroExp.expedienteNumBienesInmueblesCaractEsp, numeroBienesInmueblesCEJTfield.getText());

        if(codigoDescriptivoAltJCBox.getSelectedPatron() != null)
            hashDatos.put(ConstantesRegistroExp.expedienteCodigoDescriptivoAlteracion, codigoDescriptivoAltJCBox.getSelectedPatron());

        hashDatos.put(ConstantesRegistroExp.expedienteDescripcionAlteracion, descripcionAltJTPane.getText());

        return true;
    }

    /**
     * Metodo que recibe una hashTable en la que estan introducidos los datos a mostrar en el panel. Las keys de la hash
     * son constantes de la clase ConstantesRegistroExp que hacen referencia al nombre de los atributos del bean expediente
     *
     * @param hashDatos Hashtable con los datos almacenados
     */
    public void inicializaDatos(Hashtable hashDatos)
    {
        String tipoDocOrig = (String)hashDatos.get(ConstantesRegistroExp.expedienteTipoDocumentoOrigenAlteracion);
        if(tipoDocOrig.equalsIgnoreCase(ConstantesRegistroExp.TIPO_DOCUMENTO_PUBLICO))
        {
            tipoDocumentoOrigenAltGroupButton.setSelected(tipoDocumentoOrigenAltPublicoRButton.getModel(), true);
        }
        else
        {
            tipoDocumentoOrigenAltGroupButton.setSelected(tipoDocumentoOrigenAltPrivadoRButton.getModel(), true);            
        }
        infDocumentoOrigenAltJTfield.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteInfoDocumentoOrigenAlteracion));
        numeroBienesInmuebleUrbJTfield.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteNumBienesInmueblesUrbanos));
        numeroBienesInmueblesRustJTfield.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteNumBienesInmueblesRusticos));
        numeroBienesInmueblesCEJTfield.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteNumBienesInmueblesCaractEsp));
        String codigoDescriptivo = (String)hashDatos.get(ConstantesRegistroExp.expedienteCodigoDescriptivoAlteracion);
        if(codigoDescriptivo!=null)
        {
            codigoDescriptivoAltJCBox.setSelectedPatron(codigoDescriptivo);
        }
        else
        {
            codigoDescriptivoAltJCBox.setSelectedPatron("");            
        }
        descripcionAltJTPane.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteDescripcionAlteracion));
    }    
}
