package com.geopista.app.catastro.registroExpedientes.paneles;

import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.Estructuras;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp_LCGIII;
import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.model.beans.TipoExpediente;
import com.vividsolutions.jump.I18N;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.border.TitledBorder;
import java.util.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;


/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 18-dic-2006
 * Time: 16:20:24
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones, o metodos para recoger los datos que ha introducido el usuario.
 */

public class PanelDatosCrearExp extends JPanel implements IMultilingue {
    private String etiqueta;
    private JFrame desktop;
    private JLabel numeroExpedienteLabel;
    private JTextField numeroExpedienteJTField;
    private JLabel tipoDeExpedienteLabel;
    private JComboBox tipoDeExpedienteCBoxE;
    private JLabel estadoLabel;
    private JComboBox estadoCBox;
    private JLabel fechaRegistroLabel;
    private JTextField fechaRegistroJTfield;
    private JButton fechaRegistroButton;

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     */
    public PanelDatosCrearExp(String label)
    {
        etiqueta= label;
        inicializaPanel();
    }

    /**
     *  Inicializa todos los elementos del panel y los coloca en su posicion.
     */
    private void inicializaPanel()
    {
        //creacion de objetos.
        tipoDeExpedienteLabel = new JLabel();
        tipoDeExpedienteCBoxE = new JComboBox();
        estadoLabel = new JLabel();
        estadoCBox = new JComboBox();
        fechaRegistroLabel = new JLabel();
        fechaRegistroJTfield = new JTextField();
        fechaRegistroButton = new JButton();
        numeroExpedienteLabel = new JLabel();
        numeroExpedienteJTField = new JTextField();

        //Inicializacion de objetos.
        tipoDeExpedienteCBoxE.setEditable(false);
        estadoCBox.setEditable(false);
        fechaRegistroJTfield.setEnabled(false);
        fechaRegistroJTfield.setText(UtilRegistroExp.showToday());
        fechaRegistroButton.setIcon(UtilRegistroExp.iconoZoom);
        fechaRegistroButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fechaAltButtonActionPerformed(evt);
            }
        });

        numeroExpedienteJTField.setEditable(true);
        numeroExpedienteJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(numeroExpedienteJTField,13);
            }
        });

        cargaEstadosEstructuras();
        renombrarComponentes();

        //Inicializamos el panel con los elementos.
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.add(numeroExpedienteLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 195, 20));
        this.add(numeroExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 15, 150, -1));
        this.add(tipoDeExpedienteLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 37, 195, 20));
        this.add(tipoDeExpedienteCBoxE, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 37, 150, -1));
        this.add(estadoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 59, 195, 20));
        this.add(estadoCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 59, 150, -1));
        this.add(fechaRegistroLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 81, 195, 20));
        this.add(fechaRegistroJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 81, 150, -1));
        this.add(fechaRegistroButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 81, 20, 20));
    }

    /**
     * Metodo que trata el evento lanzado por el boton de la fecha de alteracion y muestra el dialogo para selecionar
     * una fecha valida, recogiendola de la constante estatica calendarValue.
     *
     * @param evt Evento lanzado
     * */
    private void fechaAltButtonActionPerformed(ActionEvent evt)
    {
        UtilRegistroExp.showCalendarDialog(desktop);

		if ((ConstantesRegistroExp.calendarValue != null) && (!ConstantesRegistroExp.calendarValue.trim().equals("")))
        {
			fechaRegistroJTfield.setText(ConstantesRegistroExp.calendarValue);
		}
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
        	UtilRegistroExp_LCGIII.retrocedeCaracter(comp, maxLong);
        }
    }

    /**
     * Carga el primer estado de las estruturas de estados cargadas al inicio de la aplicacion. Tambien carga los
     * tipos de expediente que se cargan al cargar la aplicacion.
     */
    private void cargaEstadosEstructuras()
    {
        estadoCBox.removeAllItems();
        String aux="";
        if(Estructuras.getListaEstadosExpediente().getDomainNode("1")!=null)
        {
            aux= Estructuras.getListaEstadosExpediente().getDomainNode("1")
                .getTerm(ConstantesRegistroExp.Locale);
        }
        estadoCBox.addItem(aux);
        tipoDeExpedienteCBoxE.removeAllItems();
        ArrayList auxTiposExp = ConstantesRegistroExp.tiposExpedientes;
        if(auxTiposExp!=null)
        {
            for(int i = 0; i< auxTiposExp.size();i++)
            {
                tipoDeExpedienteCBoxE.addItem(((TipoExpediente)auxTiposExp.get(i)).getCodigoTipoExpediente());
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
        fechaRegistroButton.setEnabled(edit);
        numeroExpedienteJTField.setEditable(edit);
    }

    /**
     * Asigna al atributo desktop el parametro pasado al metodo.
     *
     * @param desktop JFrame
     */
    public void setDesktop(JFrame desktop)
    {
        this.desktop = desktop;
    }

    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        cargaEstadosEstructuras();
        tipoDeExpedienteLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosCrearExp.tipoDeExpedienteLabel")));
        estadoLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosCrearExp.estadoLabel")));
        fechaRegistroLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosCrearExp.fechaRegistroLabel")));
        fechaRegistroButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosCrearExp.fechaRegistroButton.hint"));
        numeroExpedienteLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosCrearExp.numeroExpedienteLabel")));
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
            hashDatos.put(ConstantesRegistroExp.expedienteTipoExpediente,tipoDeExpedienteCBoxE.getSelectedItem());
            hashDatos.put(ConstantesRegistroExp.expedienteIdEstado,Estructuras.getListaEstadosExpediente().getDomainNodeByTraduccion((String)estadoCBox.getSelectedItem()).getPatron());
            hashDatos.put(ConstantesRegistroExp.expedienteFechaRegistro,UtilRegistroExp.getDate(fechaRegistroJTfield.getText()));
            hashDatos.put(ConstantesRegistroExp.expedienteNumeroExpediente, numeroExpedienteJTField.getText());

            return true;
        }
        else
        {
            JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosCrearExp.msg1"));
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
        return (!tipoDeExpedienteCBoxE.getSelectedItem().equals(""))&&(!estadoCBox.getSelectedItem().equals(""))
                &&(!fechaRegistroJTfield.getText().equals(""))&&(!numeroExpedienteJTField.getText().equals(""));
    }


    /**
     * Metodo que recibe una hashTable en la que estan introducidos los datos a mostrar en el panel. Las keys de la hash
     * son constantes de la clase ConstantesRegistroExp que hacen referencia al nombre de los atributos del bean expediente
     *
     * @param hashDatos Hashtable con los datos almacenados
     */
    public void inicializaDatos(Hashtable hashDatos)
    {
        tipoDeExpedienteCBoxE.removeAllItems();
        estadoCBox.removeAllItems();
        tipoDeExpedienteCBoxE.addItem((String)hashDatos.get(ConstantesRegistroExp.expedienteTipoExpediente));
        estadoCBox.addItem((String)hashDatos.get(ConstantesRegistroExp.expedienteIdEstado));
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormat = formatter.format((Date)hashDatos.get(ConstantesRegistroExp.expedienteFechaRegistro));
        fechaRegistroJTfield.setText(fechaFormat);
        numeroExpedienteJTField.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteNumeroExpediente));
    }
}
