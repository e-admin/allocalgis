/**
 * DialogoActualizacionPeriodica.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.historico;


import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.vividsolutions.jump.I18N;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 02-mar-2007
 * Time: 10:28:46
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que muestra un dialogo para configurar los parametros de actualizacion periodica. Los dias que restan para que
 * nos avise para realizar una actualizacion con catastro y para generar un envio. Tambien podemos configurar si se
 * desea ser avisado o no. Estos clase solo sera accesible a los usuarios con permisos de administracion.
 */

public class DialogoActualizacionPeriodica extends javax.swing.JDialog
{
    private JPanel todoPanel;
    private JPanel actualizacionPeriodicaJPanel;
    private JPanel enviosPeriodicosJPanel;
    private JLabel actPeriodicaFrecuenciaJLabel;
    private JLabel actPeriodicaDiasJLabel;
    private JTextField actPeriodicaFrecuenciaJTField;
    private JLabel actPeriodicaMostrarAvisoJLabel;
    private JCheckBox actPeriodicasMostratAvisoJCBox;
    private JTextField enviosPeriodicosFrecuenciaJTField;
    private JCheckBox enviosPeriodicosMostrarAvisosJCbox;
    private JLabel enviosPeriodicosFrecuenciaJLabel;
    private JLabel enviosPeriodicosDiasJLabel;
    private JLabel enviosPeriodicosMostrarAvisoJLabel;
    private JButton cancelarJButton;
    private JButton aceptarJButton;

    /**
     * Constructor de la calse. Inicializa el panel y lo muestra. Necesia el parametro parent (Frame) y modal (boolean)
     * para pasarselo al padre.
     *
     * @param parent JFrame
     * @param modal boolean
     */
    public DialogoActualizacionPeriodica(java.awt.Frame parent, boolean modal)
    {
		super(parent, modal);
		inicializaDialogo();
	}

    /**
     * Inicializa los elemento de la gui con sus determinados valores y eventos.
     */
    private void inicializaDialogo()
    {
        todoPanel = new JPanel();
        cancelarJButton = new JButton();
        aceptarJButton = new JButton();
        inicializaActualizacionPeriodicaPanel();
        inicializaEnviosPeriodicosPanel();

        renombrarComponentes();
        recopilaDatosBD();
        chekeaActualizacionEnModoTrabajo();

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

        todoPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        todoPanel.add(actualizacionPeriodicaJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 260, 90));
        todoPanel.add(enviosPeriodicosJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 260, 90));
        todoPanel.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 90, 30));
        todoPanel.add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 240, 90, 30));

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoActualizacionPeriodica.titulo"));
        getContentPane().add(todoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, 280, 290));
        pack();
    }

    /**
     * Metodo que inicializa la gui de actualizacion periodica con los valores guardados en base de datos.
     * */
    private void inicializaActualizacionPeriodicaPanel()
    {
        actualizacionPeriodicaJPanel = new JPanel();
        actPeriodicaFrecuenciaJTField = new JTextField();
        actPeriodicasMostratAvisoJCBox = new JCheckBox();
        actPeriodicaFrecuenciaJLabel =  new JLabel();
        actPeriodicaDiasJLabel =  new JLabel();
        actPeriodicaMostrarAvisoJLabel =  new JLabel();
        
        actualizacionPeriodicaJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        actualizacionPeriodicaJPanel.add(actPeriodicaFrecuenciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 70, 20));
        actualizacionPeriodicaJPanel.add(actPeriodicaFrecuenciaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 30, 20));
        actualizacionPeriodicaJPanel.add(actPeriodicaDiasJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, 50, 20));
        actualizacionPeriodicaJPanel.add(actPeriodicaMostrarAvisoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 70, 20));
        actualizacionPeriodicaJPanel.add(actPeriodicasMostratAvisoJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 20, 20));

    }

    /**
     * Metodo que inicializa la gui de envios periodica con los valores guardados en base de datos.
     * */
    private void inicializaEnviosPeriodicosPanel()
    {
        enviosPeriodicosJPanel = new JPanel();
        enviosPeriodicosFrecuenciaJTField = new JTextField();
        enviosPeriodicosMostrarAvisosJCbox = new JCheckBox();
        enviosPeriodicosFrecuenciaJLabel =  new JLabel();
        enviosPeriodicosDiasJLabel =  new JLabel();
        enviosPeriodicosMostrarAvisoJLabel =  new JLabel();

        enviosPeriodicosJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        enviosPeriodicosJPanel.add(enviosPeriodicosFrecuenciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 70, 20));
        enviosPeriodicosJPanel.add(enviosPeriodicosFrecuenciaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 30, 20));
        enviosPeriodicosJPanel.add(enviosPeriodicosDiasJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, 50, 20));
        enviosPeriodicosJPanel.add(enviosPeriodicosMostrarAvisoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 70, 20));
        enviosPeriodicosJPanel.add(enviosPeriodicosMostrarAvisosJCbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 20, 20));
    }

    /**
     * Metodo que realiza una peticion al clienteCatastro para recopilar los datos de la configuracion almacenados
     * en la BBDD.
     * */
    private void recopilaDatosBD()
    {
        try
        {
            DatosConfiguracion datos = ConstantesRegExp.clienteCatastro.getParametrosConfiguracion();
            if(datos!=null)
            {
                actPeriodicaFrecuenciaJTField.setText(String.valueOf(datos.getFrecuenciaActualizacion()));
                if(datos.getMostrarAvisoAct()==1)
                {
                    actPeriodicasMostratAvisoJCBox.setSelected(true);
                }
                else if(datos.getMostrarAvisoAct()==0)
                {
                    actPeriodicasMostratAvisoJCBox.setSelected(false);
                }
                enviosPeriodicosFrecuenciaJTField.setText(String.valueOf(datos.getFrecuenciaEnvio()));
                if(datos.getMostrarAvisoEnvio()==1)
                {
                    enviosPeriodicosMostrarAvisosJCbox.setSelected(true);
                }
                else if(datos.getMostrarAvisoEnvio()==0)
                {
                    enviosPeriodicosMostrarAvisosJCbox.setSelected(false);
                }        
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Cambia las etiquetas al idioma que el usuario tenga selecionado.
     */
    private void renombrarComponentes()
    {
        todoPanel.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoActualizacionPeriodica.todoPanel")));
        actualizacionPeriodicaJPanel.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoActualizacionPeriodica.actualizacionPeriodicaJPanel")));
        enviosPeriodicosJPanel.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoActualizacionPeriodica.enviosPeriodicosJPanel")));
        actPeriodicaFrecuenciaJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoActualizacionPeriodica.actPeriodicaFrecuenciaJLabel"));
        actPeriodicaDiasJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoActualizacionPeriodica.actPeriodicaDiasJLabel"));
        actPeriodicaMostrarAvisoJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoActualizacionPeriodica.actPeriodicaMostrarAvisoJLabel"));
        enviosPeriodicosFrecuenciaJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoActualizacionPeriodica.enviosPeriodicosFrecuenciaJLabel"));
        enviosPeriodicosDiasJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoActualizacionPeriodica.enviosPeriodicosDiasJLabel"));
        enviosPeriodicosMostrarAvisoJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoActualizacionPeriodica.enviosPeriodicosMostrarAvisoJLabel"));
        aceptarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoActualizacionPeriodica.aceptarJButton"));
        aceptarJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoActualizacionPeriodica.aceptarJButton.hint"));
        cancelarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoActualizacionPeriodica.cancelarJButton"));
        cancelarJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoActualizacionPeriodica.cancelarJButton.hint"));
    }

    /**
     * Metodo que cierra el dialogo.
     * */
    private void cancelarJButtonActionPerformed()
    {
		dispose();
	}

    /**
     * Metodo que recoge los parametros introducidos por el usuario y los almacena en BBDD.
     * */
    private void aceptarJButtonActionPerformed()
    {
        DatosConfiguracion datos = new DatosConfiguracion();
        if(actPeriodicaFrecuenciaJTField.getText().equalsIgnoreCase(""))
        {
            datos.setFrecuenciaActualizacion(-1);
        }
        else
        {
            datos.setFrecuenciaActualizacion(Integer.parseInt(actPeriodicaFrecuenciaJTField.getText()));
        }
        if(enviosPeriodicosFrecuenciaJTField.getText().equalsIgnoreCase(""))
        {
            datos.setFrecuenciaEnvio(-1);
        }
        else
        {
            datos.setFrecuenciaEnvio(Integer.parseInt(enviosPeriodicosFrecuenciaJTField.getText()));
        }
        if(actPeriodicasMostratAvisoJCBox.isSelected())
        {
            datos.setMostrarAvisoAct(1);
        }
        else
        {
            datos.setMostrarAvisoAct(0);
        }
        if(enviosPeriodicosMostrarAvisosJCbox.isSelected())
        {
            datos.setMostrarAvisoEnvio(1);
        }
        else
        {
            datos.setMostrarAvisoEnvio(0);
        }

        try
        {
        	ConstantesRegExp.clienteCatastro.guardarParametroConfiguracion(datos);
            dispose();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que inhabilita la parte de envios periodicos si estamos en modo acoplado.
     * */
    private void chekeaActualizacionEnModoTrabajo()
    {
        if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
        {
            enviosPeriodicosFrecuenciaJTField.setEnabled(false);
            enviosPeriodicosMostrarAvisosJCbox.setEnabled(false);
        }
    }
}
