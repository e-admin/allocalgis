package com.geopista.app.catastro.registroExpedientes.paneles;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.Hashtable;
import java.util.Date;
import java.sql.Time;

import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.Estructuras;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.vividsolutions.jump.I18N;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 19-dic-2006
 * Time: 17:17:12
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones, o metodos para recoger los datos que ha introducido el usuario.
 */

public class PanelDatosExpGestionExp extends JPanel implements IMultilingue {
    private String etiqueta;
    private JLabel numeroDeExpedienteLabel;
    private JTextField numeroDeExpedienteJtField;
    private JLabel estadoLabel;
    private JTextField estadoJTField;
    private JLabel tipoExpedienteLabel;
    private JTextField tipoExpedienteJTField;
    private JLabel annoExpedienteAdminOrigenAlt_EjercicioLabel;
    private JTextField annoExpedienteAdminOrigenAlt_EjercicioJTfield;
    private JLabel codigoEntidadGeneradora_ControlLabel;
    private JTextField codigoEntidadGeneradora_ControlJTField;
    private JLabel fechaRegistroLabel;
    private JTextField fechaRegistroJTfield;
    private JLabel fechaAlteracionLabel;
    private JTextField fechaAlteracionJTfield;
    private JLabel fechaMovimientoLabel;
    private JTextField fechaMovimientoJTfield;
    private JLabel horaMovimientoLabel;
    private JTextField horaMovimientoJTfield;

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     */
    public PanelDatosExpGestionExp(String label)
    {
        etiqueta= label;
        inicializaPanel();

    }

    /**
     *  Inicializa todos los elementos del panel y los coloca en su posicion.
     */
    public void inicializaPanel()
    {
        //creacion de objetos.
        numeroDeExpedienteLabel = new JLabel();
        numeroDeExpedienteJtField = new JTextField();
        estadoLabel = new JLabel();
        estadoJTField = new JTextField();
        tipoExpedienteLabel = new JLabel();
        tipoExpedienteJTField = new JTextField();
        annoExpedienteAdminOrigenAlt_EjercicioLabel = new JLabel();
        annoExpedienteAdminOrigenAlt_EjercicioJTfield = new JTextField();
        codigoEntidadGeneradora_ControlLabel = new JLabel();
        codigoEntidadGeneradora_ControlJTField = new JTextField();
        fechaRegistroLabel = new JLabel();
        fechaRegistroJTfield = new JTextField();
        fechaAlteracionLabel = new JLabel();
        fechaAlteracionJTfield = new JTextField();
        fechaMovimientoLabel = new JLabel();
        fechaMovimientoJTfield = new JTextField();
        horaMovimientoLabel = new JLabel();
        horaMovimientoJTfield = new JTextField();

        //Inicializacion de objetos.
        setEditable(false);
        renombrarComponentes();

        //Inicializamos el panel con los elementos.
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.add(numeroDeExpedienteLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 200, 20));
        this.add(numeroDeExpedienteJtField, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 15, 150, -1));

        this.add(annoExpedienteAdminOrigenAlt_EjercicioLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 38, 200, 20));
        this.add(annoExpedienteAdminOrigenAlt_EjercicioJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 38, 150, -1));
        this.add(codigoEntidadGeneradora_ControlLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 61, 200, 20));
        this.add(codigoEntidadGeneradora_ControlJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 61, 150, -1));

        this.add(estadoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 84, 100, 20));
        this.add(estadoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 84, 110, -1));
        this.add(tipoExpedienteLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 84, 100, 20));
        this.add(tipoExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 84, 100, -1));


        this.add(fechaRegistroLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 107, 150, 20));
        this.add(fechaRegistroJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 107, 70, -1));
        this.add(fechaAlteracionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 107, 150, 20));
        this.add(fechaAlteracionJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 107, 100, -1));

        this.add(fechaMovimientoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 150, 20));
        this.add(fechaMovimientoJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 70, -1));
        this.add(horaMovimientoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 130, 150, 20));
        this.add(horaMovimientoJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 130, 100, -1));
    }

    /**
     * Cambia los campos editables del panel segun el parametro pasado.
     *
     * @param edit
     * */
    public void setEditable(boolean edit)
    {
        numeroDeExpedienteJtField.setEditable(edit);
        estadoJTField.setEditable(edit);
        tipoExpedienteJTField.setEditable(edit);
        annoExpedienteAdminOrigenAlt_EjercicioJTfield.setEditable(edit);
        codigoEntidadGeneradora_ControlJTField.setEditable(edit);
        fechaRegistroJTfield.setEditable(edit);
        fechaAlteracionJTfield.setEditable(edit);
        fechaMovimientoJTfield.setEditable(edit);
        horaMovimientoJTfield.setEditable(edit);
    }

   /**
    * Devuelve el panel.
    *
    * @return this
    * */
    public JPanel getDatosExpPanel()
    {
        return this;
    }

    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        numeroDeExpedienteLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosExpGestionExp.numeroDeExpedienteLabel"));
        estadoLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosExpGestionExp.estadoLabel"));
        tipoExpedienteLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosExpGestionExp.tipoExpedienteLabel"));
        annoExpedienteAdminOrigenAlt_EjercicioLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosExpGestionExp.annoExpedienteAdminOrigenAlt_EjercicioLabel"));
        codigoEntidadGeneradora_ControlLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosExpGestionExp.codigoEntidadGeneradora_ControlLabel"));
        fechaRegistroLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosExpGestionExp.fechaRegistroLabel"));
        fechaAlteracionLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosExpGestionExp.fechaAlteracionLabel"));
        fechaMovimientoLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosExpGestionExp.fechaMovimientoLabel"));
        horaMovimientoLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosExpGestionExp.horaMovimientoLabel"));
    }

    /**
     * Carga los datos en los atributos del panel. Los datos los recoge de la hash pasada por parametro, con la key
     * correspondiente al atributo en las ConstantesRegistroExp que hacen referencia al nombre de los atributos del bean
     * expediente.
     *
     * @param hashDatos Hashtable con los datos almacenados
     */
    public void inicializaDatos(Hashtable hashDatos)
    {
        numeroDeExpedienteJtField.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteNumeroExpediente));
        estadoJTField.setText(Estructuras.getListaEstadosExpediente().getDomainNode((String)hashDatos.get(ConstantesRegistroExp.expedienteIdEstado)).getTerm(ConstantesRegistroExp.Locale));
        tipoExpedienteJTField.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteTipoExpediente));
        annoExpedienteAdminOrigenAlt_EjercicioJTfield.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteAnnoExpedienteAdminOrigenAlteracion));
        codigoEntidadGeneradora_ControlJTField.setText((String)hashDatos.get(ConstantesRegistroExp.entidadGeneradoraCodigo));
        if(hashDatos.get(ConstantesRegistroExp.expedienteFechaRegistro) instanceof Date){
        	fechaRegistroJTfield.setText(UtilRegistroExp.formatFecha((Date)hashDatos.get(ConstantesRegistroExp.expedienteFechaRegistro)));
        }
        else{
        	fechaRegistroJTfield.setText("");
        }
        //fechaRegistroJTfield.setText(UtilRegistroExp.formatFecha((Date)hashDatos.get(ConstantesRegistroExp.expedienteFechaRegistro)));
        if(hashDatos.get(ConstantesRegistroExp.expedienteFechaAlteracion) instanceof Date)
        {
            fechaAlteracionJTfield.setText(UtilRegistroExp.formatFecha((Date)hashDatos.get(ConstantesRegistroExp.expedienteFechaAlteracion)));
        }
        else
        {
            fechaAlteracionJTfield.setText("");            
        }
        if(hashDatos.get(ConstantesRegistroExp.expedienteFechaMovimiento) instanceof Date)
        {
            fechaMovimientoJTfield.setText(UtilRegistroExp.formatFecha((Date)hashDatos.get(ConstantesRegistroExp.expedienteFechaMovimiento)));
        }
        else
        {
            fechaMovimientoJTfield.setText("");
        }
        horaMovimientoJTfield.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteHoraMovimiento));

    }
}
