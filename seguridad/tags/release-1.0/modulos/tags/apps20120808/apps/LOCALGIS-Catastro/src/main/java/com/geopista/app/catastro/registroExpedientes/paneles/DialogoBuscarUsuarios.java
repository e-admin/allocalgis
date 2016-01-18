package com.geopista.app.catastro.registroExpedientes.paneles;

import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.server.administradorCartografia.ACException;
import com.vividsolutions.jump.I18N;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 04-abr-2007
 * Time: 10:20:02
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que muestra un dialogo para cambiar el tecnico de un expediente. Se muestra un comboBox con todos los usuarios
 * de la aplicacion para que el usuario elija. Cuando pulsa aceptar el expediente se actualiza en base de datos.
 */

public class DialogoBuscarUsuarios extends javax.swing.JDialog
{
    private JPanel todoPanel;
    private JLabel usuariosLabel;
    private JComboBox usuariosJComboBox;
    private JButton cancelarJButton;
    private JButton aceptarJButton;
    private Hashtable usuarios;
    private Hashtable idTecnicos;
    private Expediente expediente;

    /**
     * Constructor de la calse. Inicializa el panel y lo muestra. Necesia el parametro parent (Frame) y modal (boolean)
     * para pasarselo al padre.
     *
     * @param parent JFrame
     * @param modal boolean
     * @param usuarios String
     * @param exp El expediente a actualizar.
     */
    public DialogoBuscarUsuarios(java.awt.Frame parent, boolean modal, Hashtable usuarios, Expediente exp)
    {
		super(parent, modal);
        this.usuarios= usuarios;
        this.expediente= exp;
        inicializaDialogo();
	}

    /**
     * Inicializa los elemento de la gui con sus determinados valores y eventos.
     */
    private void inicializaDialogo()
    {
        todoPanel = new JPanel();
        usuariosLabel = new JLabel();
        cancelarJButton = new JButton();
        aceptarJButton = new JButton();
        usuariosJComboBox = new JComboBox();

        recopilaDatosBD();
        cargaDatos();



        aceptarJButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                aceptarJButtonActionPerformed();
            }
        });

        cancelarJButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cancelarJButtonActionPerformed();
            }
        });

        renombrarComponentes();

        todoPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        todoPanel.add(usuariosLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 150, 20));
        todoPanel.add(usuariosJComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 150, 20));
        todoPanel.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 90, 30));
        todoPanel.add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, 90, 30));

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarUsuarios.titulo"));
        getContentPane().add(todoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 320, 120));
        pack();
    }

    /**
     * Recopila los datos, de base de datos, necesarios para el dialogo.
     */
    private void recopilaDatosBD()
    {
        try
        {
            idTecnicos = ConstantesRegistroExp.clienteCatastro.getTodosLosUsuarios();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Inicializa los datos de los elementos del gui.
     */
    private void cargaDatos()
    {
        Enumeration auxIdTec = idTecnicos.keys();
        while(auxIdTec.hasMoreElements())
        {
            usuariosJComboBox.addItem((String)auxIdTec.nextElement());
        }
    }

    /**
     * Cambia las etiquetas al idioma que el usuario tenga selecionado.
     */
    private void renombrarComponentes()
    {

        usuariosLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarUsuarios.usuariosLabel"));
        aceptarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarUsuarios.aceptarJButton"));
        aceptarJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarUsuarios.aceptarJButton.hint"));
        cancelarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarUsuarios.cancelarJButton"));
        cancelarJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarUsuarios.cancelarJButton.hint"));
    }

    /**
     * Accion para el evento de cancelar. Cierra el dialogo.
     */
    private void cancelarJButtonActionPerformed()
    {
		dispose();
	}

    /**
     * Metodo que cambia el tecnico de un expediente por el elegido en el dialogo por el usuario.
     */
    private void aceptarJButtonActionPerformed()
    {
        String idTecnicoCatastro = (String)idTecnicos.get(usuariosJComboBox.getSelectedItem());
        usuarios.put(idTecnicos.get(usuariosJComboBox.getSelectedItem()),usuariosJComboBox.getSelectedItem());
        expediente.setIdTecnicoCatastro(idTecnicoCatastro);
        updateExpedienteEnBD(expediente);
        dispose();
    }

    /**
     * Funcion que llama al ClienteCatastro (El objeto que realiza las peticiones a la base de datos) para actualizar
     * los datos del expediente, que el usuario ha estado tratando, en base de datos.
     *
     * @param expediente El expediente a actualizar.
     */
    public void updateExpedienteEnBD(Expediente expediente)
    {
        try
        {
            expediente= ConstantesRegistroExp.clienteCatastro.updateExpediente(expediente);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
