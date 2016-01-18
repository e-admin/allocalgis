/**
 * DialogoModosConvenios.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.historico;

import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.vividsolutions.jump.I18N;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 02-mar-2007
 * Time: 11:55:39
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que muestra un dialogo para configurar los parametros de modos y convenios.
 * Estos clase solo sera accesible a los usuarios con permisos de administracion.
 */

public class DialogoModosConvenios extends javax.swing.JDialog
{
    private JPanel todoPanel;
    private JPanel tipoConvenioJPanel;
    private JPanel formaConvenioJPanel;
    private JPanel modoTrabajoJPanel;

    private ButtonGroup tipoConvenioGroupButton;
    private JRadioButton convenioNingunoJRadioButton;
    private JLabel convenioNingunoJLabel;
    private JRadioButton convenioTitularidadJRadioButton;
    private JLabel convenioTitularidadJLabel;
    private JRadioButton convenioFisicoEconomicoJRadioButton;
    private JLabel convenioFisicoEconomicoJLabel;

    private ButtonGroup formaConvenioGroupButton;
    private JRadioButton convenioDelegacionCompetenciaJRadioButton;
    private JLabel convenioDelegacionCompetenciaJLabel;
    private JRadioButton convenioEncomiendaGestionJRadioButton;
    private JLabel convenioEncomiendaGestionJLabel;

    private ButtonGroup modoTrabajoGroupButton;
    private JRadioButton modoTrabajoAcopladoJRadioButton;
    private JLabel modoTrabajoAcopladoJLabel;
    private JRadioButton modoTrabajoDesacopladoJRadioButton;
    private JLabel modoTrabajoDesacopladoJLabel;

    private JButton cancelarJButton;
    private JButton aceptarJButton;

    private JFrame desktop;
	private JPanel jPanelGenerarFxcc;
	private ButtonGroup buttonGroupGenerarFxcc;
	private JRadioButton jRadioButtonGenerarFxccFichero;
	private JLabel jLabelGenerarFxccFichero;
	private JRadioButton jRadioButtonGenerarFxccDirectorio;
	private JLabel jLabelGenerarFxccDirectorio;

    /**
     * Constructor de la calse. Inicializa el panel y lo muestra. Necesia el parametro parent (Frame) y modal (boolean)
     * para pasarselo al padre.
     *
     * @param parent JFrame
     * @param modal boolean
     * @param desktop El jframe de swing para actualizar los menus.
     */
    public DialogoModosConvenios(java.awt.Frame parent, boolean modal, JFrame desktop)
    {
		super(parent, modal);
        this.desktop= desktop;
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
        inicializaTipoConvenioJPanel();
        inicializaFormaConvenioJPanel();
        inicializaModoTrabajoJPanel();
        inicializaModoGenerarFxcc();

        renombrarComponentes();
        recopilaDatosBD();
        checkeaModoYConvenio();

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
        todoPanel.add(tipoConvenioJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 470, 50));
        todoPanel.add(formaConvenioJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 470, 50));
        todoPanel.add(modoTrabajoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 470, 50));
        todoPanel.add(jPanelGenerarFxcc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 470, 50));
        
        todoPanel.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 240, 90, 30));
        todoPanel.add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 240, 90, 30));

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoModosConvenios.titulo"));
        getContentPane().add(todoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, 490, 300));
        pack();
    }

    /**
     * Metodo que inicializa el panel de convenio.
     * */
    private void inicializaTipoConvenioJPanel()
    {
        tipoConvenioJPanel= new JPanel();
        tipoConvenioGroupButton = new ButtonGroup();
        convenioNingunoJRadioButton = new JRadioButton();
        convenioNingunoJLabel =  new JLabel();
        convenioTitularidadJRadioButton = new JRadioButton();
        convenioTitularidadJLabel =  new JLabel();
        convenioFisicoEconomicoJRadioButton = new JRadioButton();
        convenioFisicoEconomicoJLabel =  new JLabel();

        tipoConvenioGroupButton.add(convenioNingunoJRadioButton);
        tipoConvenioGroupButton.add(convenioTitularidadJRadioButton);
        tipoConvenioGroupButton.add(convenioFisicoEconomicoJRadioButton);
        tipoConvenioGroupButton.setSelected(convenioNingunoJRadioButton.getModel(),true);

        convenioNingunoJRadioButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                checkeaModoYConvenio();
            }
        });

        convenioTitularidadJRadioButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                checkeaModoYConvenio();
            }
        });

        convenioFisicoEconomicoJRadioButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                checkeaModoYConvenio();
            }
        });

        tipoConvenioJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        tipoConvenioJPanel.add(convenioNingunoJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 20, 20));
        tipoConvenioJPanel.add(convenioNingunoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 70, 20));
        tipoConvenioJPanel.add(convenioTitularidadJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 20, 20));
        tipoConvenioJPanel.add(convenioTitularidadJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 130, 20));
        tipoConvenioJPanel.add(convenioFisicoEconomicoJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 20, 20));
        tipoConvenioJPanel.add(convenioFisicoEconomicoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 20, 150, 20));
    }

    /**
     * Metodo que inicializa el panel de forma de convenio.
     * */
    private void inicializaFormaConvenioJPanel()
    {
        formaConvenioJPanel= new JPanel();
        formaConvenioGroupButton = new ButtonGroup();
        convenioDelegacionCompetenciaJRadioButton = new JRadioButton();
        convenioDelegacionCompetenciaJLabel =  new JLabel();
        convenioEncomiendaGestionJRadioButton = new JRadioButton();
        convenioEncomiendaGestionJLabel =  new JLabel();

        formaConvenioGroupButton.add(convenioDelegacionCompetenciaJRadioButton);
        formaConvenioGroupButton.add(convenioEncomiendaGestionJRadioButton);
        formaConvenioGroupButton.setSelected(convenioDelegacionCompetenciaJRadioButton.getModel(),true);

        formaConvenioJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        formaConvenioJPanel.add(convenioDelegacionCompetenciaJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 20, 20));
        formaConvenioJPanel.add(convenioDelegacionCompetenciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 150, 20));
        formaConvenioJPanel.add(convenioEncomiendaGestionJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, 20, 20));
        formaConvenioJPanel.add(convenioEncomiendaGestionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, 150, 20));
    }

    /**
     * Metodo que inicializa el panel de modo de trabajo.
     * */
    private void inicializaModoTrabajoJPanel()
    {
        modoTrabajoJPanel= new JPanel();
        modoTrabajoGroupButton = new ButtonGroup();
        modoTrabajoAcopladoJRadioButton = new JRadioButton();
        modoTrabajoAcopladoJLabel =  new JLabel();
        modoTrabajoDesacopladoJRadioButton = new JRadioButton();
        modoTrabajoDesacopladoJLabel =  new JLabel();

        modoTrabajoGroupButton.add(modoTrabajoAcopladoJRadioButton);
        modoTrabajoGroupButton.add(modoTrabajoDesacopladoJRadioButton);
        modoTrabajoGroupButton.setSelected(modoTrabajoAcopladoJRadioButton.getModel(),true);

        modoTrabajoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        modoTrabajoJPanel.add(modoTrabajoAcopladoJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 20, 20));
        modoTrabajoJPanel.add(modoTrabajoAcopladoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 70, 20));
        modoTrabajoJPanel.add(modoTrabajoDesacopladoJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 20, 20));
        modoTrabajoJPanel.add(modoTrabajoDesacopladoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 70, 20));
    }
    
    private void inicializaModoGenerarFxcc()
    {
        jPanelGenerarFxcc= new JPanel();
        buttonGroupGenerarFxcc = new ButtonGroup();
        jRadioButtonGenerarFxccFichero = new JRadioButton();
        jLabelGenerarFxccFichero =  new JLabel();
        jRadioButtonGenerarFxccDirectorio = new JRadioButton();
        jLabelGenerarFxccDirectorio =  new JLabel();

        buttonGroupGenerarFxcc.add(jRadioButtonGenerarFxccFichero);
        buttonGroupGenerarFxcc.add(jRadioButtonGenerarFxccDirectorio);
        buttonGroupGenerarFxcc.setSelected(jRadioButtonGenerarFxccFichero.getModel(),true);

        jPanelGenerarFxcc.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelGenerarFxcc.add(jRadioButtonGenerarFxccFichero, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 20, 20));
        jPanelGenerarFxcc.add(jLabelGenerarFxccFichero, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 100, 20));
        jPanelGenerarFxcc.add(jRadioButtonGenerarFxccDirectorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 20, 20));
        jPanelGenerarFxcc.add(jLabelGenerarFxccDirectorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 100, 20));
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
                String tipoConvenio = datos.getTipoConvenio();
                String formaConvenio = datos.getFormaConvenio();
                String modoTrabajo = datos.getModoTrabajo();
                String modoGeneracion = datos.getModoGenerarFXCC();
                
                if(tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_NINGUNO))
                {
                    tipoConvenioGroupButton.setSelected(convenioNingunoJRadioButton.getModel(), true);
                }
                else if(tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                {
                    tipoConvenioGroupButton.setSelected(convenioTitularidadJRadioButton.getModel(), true);
                }
                else if(tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                {
                    tipoConvenioGroupButton.setSelected(convenioFisicoEconomicoJRadioButton.getModel(), true);
                }

                if(formaConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_DELEGACION_COMPETENCIAS))
                {
                    formaConvenioGroupButton.setSelected(convenioDelegacionCompetenciaJRadioButton.getModel(),true);
                }
                else if(formaConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_ENCOMIENDA_GESTION))
                {
                    formaConvenioGroupButton.setSelected(convenioEncomiendaGestionJRadioButton.getModel(), true);
                }

                if(modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
                {
                    modoTrabajoGroupButton.setSelected(modoTrabajoAcopladoJRadioButton.getModel(), true);
                }
                else if(modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_DESACOPLADO))
                {
                    modoTrabajoGroupButton.setSelected(modoTrabajoDesacopladoJRadioButton.getModel(), true);
                }
                
                if (modoGeneracion.equalsIgnoreCase(DatosConfiguracion.MODO_GENERAR_FXCC_FICHERO)){
                	buttonGroupGenerarFxcc.setSelected(jRadioButtonGenerarFxccFichero.getModel(), true);
                }
                else if (modoGeneracion.equalsIgnoreCase(DatosConfiguracion.MODO_GENERAR_FXCC_DIRECTORIO)){
                	buttonGroupGenerarFxcc.setSelected(jRadioButtonGenerarFxccDirectorio.getModel(), true);
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
                        "Catastro.RegistroExpedientes.DialogoModosConvenios.todoPanel")));
        tipoConvenioJPanel.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoModosConvenios.tipoConvenioJPanel")));
        formaConvenioJPanel.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoModosConvenios.formaConvenioJPanel")));
        modoTrabajoJPanel.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoModosConvenios.modoTrabajoJPanel")));
        jPanelGenerarFxcc.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoModosConvenios.modoGenerarFxccJPanel")));
        
        convenioNingunoJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoModosConvenios.convenioNingunoJLabel"));
        convenioTitularidadJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoModosConvenios.convenioTitularidadJLabel"));
        convenioFisicoEconomicoJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoModosConvenios.convenioFisicoEconomicoJLabel"));
        convenioDelegacionCompetenciaJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoModosConvenios.convenioDelegacionCompetenciaJLabel"));
        convenioEncomiendaGestionJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoModosConvenios.convenioEncomiendaGestionJLabel"));
        modoTrabajoAcopladoJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoModosConvenios.modoTrabajoAcopladoJLabel"));
        modoTrabajoDesacopladoJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoModosConvenios.modoTrabajoDesacopladoJLabel"));
        jLabelGenerarFxccFichero.setText(I18N.get("RegistroExpedientes",
        				"Catastro.RegistroExpedientes.DialogoModosConvenios.modoGenerarFxccFicheroJLabel"));
        jLabelGenerarFxccDirectorio.setText(I18N.get("RegistroExpedientes",
        				"Catastro.RegistroExpedientes.DialogoModosConvenios.modoGenerarFxccDirectorioJLabel"));
        
        aceptarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoModosConvenios.aceptarJButton"));
        aceptarJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoModosConvenios.aceptarJButton.hint"));
        cancelarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoModosConvenios.cancelarJButton"));
        cancelarJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoModosConvenios.cancelarJButton.hint"));
    }

    /**
     * Metodo que cierra el dialogo.
     * */
    private void cancelarJButtonActionPerformed()
    {
		dispose();
	}

    /**
     * Metodo que recoge los parametros introducidos por el usuario y los almacena en BBDD y actualiza componentes de
     * la aplicacion si ha cambiado el convenio o el modo.
     * */
    private void aceptarJButtonActionPerformed()
    {
        DatosConfiguracion datos = new DatosConfiguracion();
        
        if(tipoConvenioGroupButton.isSelected(convenioNingunoJRadioButton.getModel()))
        {
            datos.setTipoConvenio(DatosConfiguracion.CONVENIO_NINGUNO);
        }
        else if(tipoConvenioGroupButton.isSelected(convenioTitularidadJRadioButton.getModel()))
        {
            datos.setTipoConvenio(DatosConfiguracion.CONVENIO_TITULARIDAD);
        }
        else if(tipoConvenioGroupButton.isSelected(convenioFisicoEconomicoJRadioButton.getModel()))
        {
            datos.setTipoConvenio(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO);
        }

        if(formaConvenioGroupButton.isSelected(convenioDelegacionCompetenciaJRadioButton.getModel()))
        {
            datos.setFormaConvenio(DatosConfiguracion.CONVENIO_DELEGACION_COMPETENCIAS);
        }
        else if(formaConvenioGroupButton.isSelected(convenioEncomiendaGestionJRadioButton.getModel()))
        {
            datos.setFormaConvenio(DatosConfiguracion.CONVENIO_ENCOMIENDA_GESTION);
        }

        if(modoTrabajoGroupButton.isSelected(modoTrabajoAcopladoJRadioButton.getModel()))
        {
            datos.setModoTrabajo(DatosConfiguracion.MODO_TRABAJO_ACOPLADO);
        }
        else if(modoTrabajoGroupButton.isSelected(modoTrabajoDesacopladoJRadioButton.getModel()))
        {
            datos.setModoTrabajo(DatosConfiguracion.MODO_TRABAJO_DESACOPLADO);
        }
        
        if (buttonGroupGenerarFxcc.isSelected(jRadioButtonGenerarFxccFichero.getModel())){
        	
        	datos.setModoGenerarFXCC(DatosConfiguracion.MODO_GENERAR_FXCC_FICHERO);
        }
        else if (buttonGroupGenerarFxcc.isSelected(jRadioButtonGenerarFxccDirectorio.getModel())){
        	
        	datos.setModoGenerarFXCC(DatosConfiguracion.MODO_GENERAR_FXCC_DIRECTORIO);
        }

        try
        {
            datos.setFrecuenciaActualizacion(-1);
            datos.setFrecuenciaEnvio(-1);
            datos.setMostrarAvisoAct(-1);
            datos.setMostrarAvisoEnvio(-1);
            ConstantesRegExp.clienteCatastro.guardarParametroConfiguracion(datos);
            ConstantesCatastro.modoTrabajo = datos.getModoTrabajo();
            ConstantesCatastro.tipoConvenio = datos.getTipoConvenio();
            ConstantesCatastro.formaConvenio = datos.getFormaConvenio();
            ConstantesCatastro.modoGeneracion = datos.getModoGenerarFXCC();
            ConstantesCatastro.tiposExpedientes = ConstantesRegExp.clienteCatastro.getTiposExpedientes(ConstantesCatastro.tipoConvenio);
            UtilRegistroExp.menuBarSetEnabled(true,desktop);
            dispose();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que inhabilita los tipos de convenios y los modo de trabajo si el convenio es ninguno.
     * */
    private void checkeaModoYConvenio()
    {
        if(tipoConvenioGroupButton.isSelected(convenioNingunoJRadioButton.getModel()))
        {
            convenioDelegacionCompetenciaJRadioButton.setEnabled(false);
            convenioEncomiendaGestionJRadioButton.setEnabled(false);
            modoTrabajoAcopladoJRadioButton.setEnabled(false);
            modoTrabajoDesacopladoJRadioButton.setEnabled(false);
            jRadioButtonGenerarFxccDirectorio.setEnabled(false);
            jRadioButtonGenerarFxccFichero.setEnabled(false);
        }
        else
        {
            convenioDelegacionCompetenciaJRadioButton.setEnabled(true);
            convenioEncomiendaGestionJRadioButton.setEnabled(true);
            modoTrabajoAcopladoJRadioButton.setEnabled(true);
            modoTrabajoDesacopladoJRadioButton.setEnabled(true);
            jRadioButtonGenerarFxccDirectorio.setEnabled(true);
            jRadioButtonGenerarFxccFichero.setEnabled(true);
        }
    }
}
