package com.geopista.app.catastro.registroExpedientes.paneles;


import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp_LCGIII;
import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.AppContext;
import com.vividsolutions.jump.I18N;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.border.TitledBorder;
import java.util.Hashtable;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 10-ene-2007
 * Time: 11:48:33
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones, o metodos para recoger los datos que ha introducido el usuario.
 */

public class PanelDireccion  extends JPanel implements IMultilingue
{
    private String etiqueta;
    private JLabel tipoViaLabel;
    private ComboBoxEstructuras tipoViaJCBox;
    private JLabel nombreViaLabel;
    private JTextField nombreViaJTField;
    private JLabel primerNumeroLabel;
    private JTextField primerNumeroJTField;
    private JLabel primeraLetraLabel;
    private JTextField primeraLetraJTField;
    private JLabel segundoNumeroLabel;
    private JTextField segundoNumeroJTField;
    private JLabel segundaLetraLabel;
    private JTextField segundaLetraJTField;
    private JLabel kilometroLabel;
    private JTextField kilometroJTField;
    private JLabel dirNoEstrucLabel;
    private JTextField dirNoEstrucJTField;
    private boolean hayLocalizaInter;
    private PanelLocalizacionInterna localizacionInterPanel;

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     * @param hayLocalizaInter para mostrar el panel de localizacion interna o no.
     */
    public PanelDireccion(String label, boolean hayLocalizaInter)
    {
        etiqueta = label;
        this.hayLocalizaInter = hayLocalizaInter;
        inicializaPanel();
    }

    /**
     *  Inicializa todos los elementos del panel y los coloca en su posicion.
     */
    private void inicializaPanel()
    {
        tipoViaLabel = new JLabel();
        nombreViaLabel = new JLabel();
        nombreViaJTField = new JTextField();
        primerNumeroLabel = new JLabel();
        primerNumeroJTField = new JTextField();
        primeraLetraLabel = new JLabel();
        primeraLetraJTField = new JTextField();
        segundoNumeroLabel = new JLabel();
        segundoNumeroJTField = new JTextField();
        segundaLetraLabel = new JLabel();
        segundaLetraJTField = new JTextField();
        kilometroLabel = new JLabel();
        kilometroJTField = new JTextField();
        dirNoEstrucLabel = new JLabel();
        dirNoEstrucJTField = new JTextField();
        inicializaComboBox();
        if(hayLocalizaInter)
        {
            localizacionInterPanel = new PanelLocalizacionInterna(null);
        }
        
        tipoViaJCBox.setEditable(false);
        inicializarEventos();
        setEditable(true);
        renombrarComponentes();

        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        this.add(tipoViaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 110, 20));
        this.add(tipoViaJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 340, -1));
        this.add(nombreViaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 45, 110, 20));
        this.add(nombreViaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 45, 340, -1));

        this.add(kilometroLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 73, 110, 20));
        this.add(kilometroJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 73, 100, -1));

        if(hayLocalizaInter)
        {
            this.add(localizacionInterPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 81, 470, 68));
        }

        this.add(primerNumeroLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 157, 150, 20));
        this.add(primerNumeroJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 157, 50, -1));
        this.add(primeraLetraLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 157, 180, 20));
        this.add(primeraLetraJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 157, 30, -1));
        
        this.add(segundoNumeroLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 185, 150, 20));
        this.add(segundoNumeroJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 185, 50, -1));
        this.add(segundaLetraLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 185, 180, 20));
        this.add(segundaLetraJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 185, 30, -1));

        this.add(dirNoEstrucLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 213, 160, 20));
        this.add(dirNoEstrucJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 213, 290, -1));


    }

    /**
     * Metodo que inicializa los eventos que producen los botones y los paneles editables para comprobar que los datos
     * introducidos son correctos.
     */
    private void inicializarEventos()
    {
        nombreViaJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(nombreViaJTField,25);
            }
        });

        kilometroJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongYNumCampoEdit(kilometroJTField,5);
            }
        });

        primerNumeroJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongYNumCampoEdit(primerNumeroJTField,4);
            }
        });

        primeraLetraJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongYLetra(primeraLetraJTField,1);
            }
        });

        segundoNumeroJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongYNumCampoEdit(segundoNumeroJTField,4);
            }
        });

        segundaLetraJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongYLetra(segundaLetraJTField,1);
            }
        });

        dirNoEstrucJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(dirNoEstrucJTField,25);
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
     * Checkea que el valor introducido en el campo no excede la longitud pasada
     * por parametro y que el valor es numerico.
     *
     * @param comp El componente editable
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
                        "Catastro.RegistroExpedientes.PanelDireccion.msg2")+ " " +  maxLong + " " + I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.msg3"));
                UtilRegistroExp_LCGIII.retrocedeCaracter(comp,numAnno.length()-1);
            }
            if(numAnno.length()>maxLong )
            {
            	UtilRegistroExp_LCGIII.retrocedeCaracter(comp,maxLong);
            }
        }
    }

    /**
     * Checkea que el valor introducido en el campo no excede la longitud pasada
     * por parametro y que es una letra
     *
     * @param comp El componente editable
     * @param maxLong El tamaño maximo
     */
    private void chequeaLongYLetra(final JTextComponent comp,final int maxLong)
    {
        String numAnno= comp.getText();
        if(!numAnno.equals(""))
        {
            if(numAnno.length()==maxLong&&((numAnno.toUpperCase().charAt(0)<'A')||(numAnno.toUpperCase().charAt(0)>'Z')))
            {
                JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.msg4"));
                UtilRegistroExp_LCGIII.retrocedeCaracter(comp,numAnno.length()-1);
            }
            if(numAnno.length()>maxLong)
            {
            	UtilRegistroExp_LCGIII.retrocedeCaracter(comp,maxLong);
            }
        }
    }

   /**
    * Devuelve el comboBox tipos de vias
    *
    * @return tipoViaJCBox
    * */
    public JComboBox getTipoViaJCBox()
    {
        return tipoViaJCBox;
    }

   /**
    * Devuelve el panel.
    *
    * @return this
    * */
    public JPanel getDatosDirPanel()
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
        nombreViaJTField.setEditable(edit);
        primerNumeroJTField.setEditable(edit);
        primeraLetraJTField.setEditable(edit);
        segundoNumeroJTField.setEditable(edit);
        segundaLetraJTField.setEditable(edit);
        kilometroJTField.setEditable(edit);
        dirNoEstrucJTField.setEditable(edit);
        if(hayLocalizaInter)
        {
            localizacionInterPanel.setEditable(edit);
        }
     }

    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        tipoViaLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.tipoViaLabel")));
        nombreViaLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.nombreViaLabel")));
        primerNumeroLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.primerNumeroLabel"));
        primeraLetraLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.primeraLetraLabel"));
        segundoNumeroLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.segundoNumeroLabel"));
        segundaLetraLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.segundaLetraLabel"));
        kilometroLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.kilometroLabel"));
        dirNoEstrucLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.dirNoEstrucLabel"));
        if(hayLocalizaInter)
        {
            localizacionInterPanel.renombrarComponentes();
        }
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
        if(checkeaParametrosNecesarios())
        {
            hashDatos.put(ConstantesRegistroExp.direccionTipoVia, (String)tipoViaJCBox.getSelectedPatron());
            hashDatos.put(ConstantesRegistroExp.direccionNombreVia, nombreViaJTField.getText());
            hashDatos.put(ConstantesRegistroExp.direccionPrimerNumero, primerNumeroJTField.getText());
            hashDatos.put(ConstantesRegistroExp.direccionPrimeraLetra, primeraLetraJTField.getText());
            hashDatos.put(ConstantesRegistroExp.direccionSegundoNumero, segundoNumeroJTField.getText());
            hashDatos.put(ConstantesRegistroExp.direccionSegundaLetra, segundaLetraJTField.getText());
            hashDatos.put(ConstantesRegistroExp.direccionKilometro, kilometroJTField.getText());
            hashDatos.put(ConstantesRegistroExp.direccionDireccionNoEstructurada, dirNoEstrucJTField.getText());
            if(hayLocalizaInter)
            {
                return localizacionInterPanel.recopilaDatosPanel(hashDatos);
            }
            return true;
        }
        else
        {
            JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.msg1"));
            return false;
        }
    }

    /**
     * Comprueba que los campos necesarios, marcados con asteriscos rojos, no son null o vacios.
     *
     * @return boolean El resultado de la comprobacion
     */
    private boolean checkeaParametrosNecesarios()
    {
       
        return  (!tipoViaJCBox.getSelectedItem().equals(""))&&(!nombreViaJTField.getText().equals(""));
    }



    /**
     * Carga los datos del arrayList pasado por parametro en el comboBox de tipos de vias
     *
     */
    private void inicializaComboBox()
    {
        AppContext app =(AppContext) AppContext.getApplicationContext();
        Estructuras.cargarEstructura("Tipos de via normalizados de Catastro");
        tipoViaJCBox = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                null, app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
    }

    /**
     * Metodo que recibe una hashTable en la que estan introducidos los datos a mostrar en el panel. Las keys de la hash
     * son constantes de la clase ConstantesRegistroExp que hacen referencia al nombre de los atributos del bean expediente
     *
     * @param hashDatos Hashtable con los datos almacenados
     */
    public void inicializaDatos(Hashtable hashDatos)
    {
        if(tipoViaJCBox.getItemCount()==0)
        {
            tipoViaJCBox.addItem(((String)hashDatos.get(ConstantesRegistroExp.direccionTipoVia)));
        }
        else
        {
            tipoViaJCBox.setSelectedPatron(((String)hashDatos.get(ConstantesRegistroExp.direccionTipoVia)));
        }
        nombreViaJTField.setText((String)hashDatos.get(ConstantesRegistroExp.direccionNombreVia));
        primerNumeroJTField.setText((String)hashDatos.get(ConstantesRegistroExp.direccionPrimerNumero));
        primeraLetraJTField.setText((String)hashDatos.get(ConstantesRegistroExp.direccionPrimeraLetra));
        segundoNumeroJTField.setText((String)hashDatos.get(ConstantesRegistroExp.direccionSegundoNumero));
        segundaLetraJTField.setText((String)hashDatos.get(ConstantesRegistroExp.direccionSegundaLetra));
        kilometroJTField.setText((String)hashDatos.get(ConstantesRegistroExp.direccionKilometro));
        dirNoEstrucJTField.setText((String)hashDatos.get(ConstantesRegistroExp.direccionDireccionNoEstructurada));
        if(hayLocalizaInter)
        {
            localizacionInterPanel.inicializaDatos(hashDatos);
        }
    }
}
