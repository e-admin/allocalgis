package com.geopista.app.catastro.registroExpedientes.paneles;

import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp_LCGIII;
import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.vividsolutions.jump.I18N;

import javax.swing.border.TitledBorder;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 10-ene-2007
 * Time: 12:35:52
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones, o metodos para recoger los datos que ha introducido el usuario.
 */

public class PanelLocalizacionInterna extends JPanel implements IMultilingue {
    private String etiqueta;
    private JLabel bloqueLabel;
    private JTextField bloqueJTField;
    private JLabel escaleraLabel;
    private JTextField escaleraJTField;
    private JLabel plantaLabel;
    private JTextField plantaJTField;
    private JLabel puertaLabel;
    private JTextField puertaJTField;

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     */
    public PanelLocalizacionInterna(String label)
    {
        etiqueta = label;
        inicializaPanel();
    }

    /**
     *  Inicializa todos los elementos del panel y los coloca en su posicion.
     */
    private void inicializaPanel()
    {
        bloqueLabel = new JLabel();
        bloqueJTField = new JTextField();
        escaleraLabel = new JLabel();
        escaleraJTField = new JTextField();
        plantaLabel = new JLabel();
        plantaJTField = new JTextField();
        puertaLabel = new JLabel();
        puertaJTField = new JTextField();

        inicializarEventos();
        setEditable(true);
        renombrarComponentes();

        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.add(bloqueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 100, 20));
        this.add(bloqueJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 100, -1));
        this.add(plantaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 100, 20));
        this.add(plantaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 20, 120, -1));

        this.add(escaleraLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 48, 100, 20));
        this.add(escaleraJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 48, 100, -1));
        this.add(puertaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 48, 100, 20));
        this.add(puertaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 48, 120, -1));

    }

    /**
     * Metodo que inicializa los eventos que producen los botones y los paneles editables para comprobar que los datos
     * introducidos son correctos.
     */
    private void inicializarEventos()
    {
        bloqueJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(bloqueJTField,4);
            }
        });

        puertaJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(puertaJTField,3);
            }
        });

        escaleraJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(escaleraJTField,2);
            }
        });

        plantaJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(plantaJTField,3);
            }
        });

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
    * Devuelve el panel.
    *
    * @return this
    * */
    public JPanel getDatosLIPanel()
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
        bloqueJTField.setEditable(edit);
        escaleraJTField.setEditable(edit);
        plantaJTField.setEditable(edit);
        puertaJTField.setEditable(edit);
    }

    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        if(etiqueta!=null)
        {
            this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        }
        bloqueLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelLocalizacionInterna.bloqueLabel"));
        escaleraLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelLocalizacionInterna.escaleraLabel"));
        plantaLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelLocalizacionInterna.plantaLabel"));
        puertaLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelLocalizacionInterna.puertaLabel"));
    }

    /**
     * Recopila los datos que el usuario ha introducido. Los datos los almacena en la hash pasada por parametro, en
     * la que la clave es una constante en ConstantesRegistroExp que hace referencia a un atributo del bean
     * expediente y el valor lo que el usuario haya introducido.
     *
     * @param hashDatos Hashtable donde se introducen los datos con keys almacenadas en la clase contantesRegistroExp
     * @return boolean Indica si no se ha producido ningun fallo
     */
    public boolean recopilaDatosPanel(Hashtable hashDatos)
    {
        hashDatos.put(ConstantesRegistroExp.direccionBloque, bloqueJTField.getText());
        hashDatos.put(ConstantesRegistroExp.direccionEscalera, escaleraJTField.getText());
        hashDatos.put(ConstantesRegistroExp.direccionPlanta, plantaJTField.getText());
        hashDatos.put(ConstantesRegistroExp.direccionPuerta, puertaJTField.getText());
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
        bloqueJTField.setText((String)hashDatos.get(ConstantesRegistroExp.direccionBloque));
        escaleraJTField.setText((String)hashDatos.get(ConstantesRegistroExp.direccionEscalera));
        plantaJTField.setText((String)hashDatos.get(ConstantesRegistroExp.direccionPlanta));
        puertaJTField.setText((String)hashDatos.get(ConstantesRegistroExp.direccionPuerta));
    }
}
