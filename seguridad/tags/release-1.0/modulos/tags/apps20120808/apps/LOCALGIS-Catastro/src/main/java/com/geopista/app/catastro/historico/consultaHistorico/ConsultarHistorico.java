package com.geopista.app.catastro.historico.consultaHistorico;

import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.server.administradorCartografia.ACException;
import com.vividsolutions.jump.I18N;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 09-feb-2007
 * Time: 11:25:57
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende de JInternalFrame y crea una pantalla que muestra el historico de ficheros de la aplicacion.
 * Esta pantalla solo sera accesible a aquellos usuarios que tengan permiso de administracion.
 * La clase se encarga de cargar los paneles y los dialogos necesario para funcionar.
 * */

public class ConsultarHistorico extends JInternalFrame implements IMultilingue {
    private JFrame desktop;
    private JPanel consultaHistoricoPanelTodo;
    private JButton aceptarJButton;
    private com.geopista.app.catastro.historico.TablaConsultarHistorico historicoTabla;

    /**
     * Constructor de la clase. Inicializa todos los paneles de la pantalla y asocia los eventos para ser tratados.
     * Se le pasa por parametro un JFrame..
     *
     * @param desktop  JFrame
     */
    public ConsultarHistorico(final JFrame desktop)
    {
        this.desktop= desktop;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        UtilRegistroExp.menuBarSetEnabled(false, this.desktop);
        inicializaElementos();
        addInternalFrameListener(new javax.swing.event.InternalFrameListener()
        {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt)
            {
                cierraInternalFrame();
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
        });
        setClosable(true);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

   /**
    * Inicializa los elementos del panel.
    */
    private void inicializaElementos()
    {
        consultaHistoricoPanelTodo= new JPanel();
        consultaHistoricoPanelTodo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        aceptarJButton = new JButton(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ConsultarHistorico.aceptarJButton"));
        aceptarJButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                aceptarJButtonActionPerformed();
            }
        });

        inicializaTablaConsultarHistorico();

        consultaHistoricoPanelTodo.add(aceptarJButton,new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 330, 100, 30));
        consultaHistoricoPanelTodo.setPreferredSize(new Dimension(520,400));
        getContentPane().add(consultaHistoricoPanelTodo);
        setSize(new Dimension(530,410));
        this.setMaximizable(false);
        int w=(this.desktop.getWidth()/2)- (this.getWidth()/2);
        int d= (this.desktop.getHeight()/2) - (this.getHeight()/2)-30;
        this.setLocation(w,d);
        this.setMaximumSize(new Dimension(530,410));
    }

   /**
    * Inicializa la tabla con el historico de ficheros.
    */
    private void inicializaTablaConsultarHistorico()
    {
        historicoTabla = new com.geopista.app.catastro.historico.TablaConsultarHistorico("Catastro.RegistroExpedientes.ConsultarHistorico.historicoTabla");
        ArrayList ficheros= consultaHistoricoFicherosBD();
        if(ficheros!=null)
        {
            historicoTabla.cargaDatosTabla(ficheros);
        }
        else
        {
            JOptionPane.showMessageDialog(this,"No hay historico o se ha producido un error");
        }
        consultaHistoricoPanelTodo.add(historicoTabla,new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 500, 310));

    }

    /**
     * Metodo que llama a cierraInternalFrame para cerrar la ventana.
     * */
    private void aceptarJButtonActionPerformed()
    {
        cierraInternalFrame();
    }

    /**
     * Cierra la ventana y habilita el menu de la aplicacion.
     */
    private void cierraInternalFrame()
    {
        try
        {
            this.setClosed(true);
        }
        catch(Exception e){e.printStackTrace();}
        UtilRegistroExp.menuBarSetEnabled(true, this.desktop);
    }

    /**
     * Renombra los elementos de la gui con el resourceBundle que se este utilizando.
     */
    public void renombrarComponentes()
    {
        historicoTabla.renombrarComponentes();
        aceptarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ConsultarHistorico.aceptarJButton"));
    }

    /**
     * Metodo que realiza una peticion al clienteCatastro para que acceda a BBDD recoger la informacion del historico
     * de ficheros
     *
     * @return ArrayList Los ficheros generados hasta ahora.
     * */
    private ArrayList consultaHistoricoFicherosBD()
    {
        try
        {
            return ConstantesRegistroExp.clienteCatastro.consultaHistoricoFicheros();
        }
        catch(ACException e)
        {
            JOptionPane.showMessageDialog(this,e.getCause().getMessage());
            return null;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
