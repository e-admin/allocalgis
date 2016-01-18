package com.geopista.app.catastro.registroExpedientes.imagenEstado;

import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.vividsolutions.jump.I18N;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 29-ene-2007
 * Time: 11:16:51
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende de JInternalFrame y que se usa para mostrar el estado de un expediente. La clase recibe como
 * parametro en el constructor el estado y carga la imagen con un path por defecto mas el nombre del estado. Para
 * cargar la imagen utiliza la clase UtilRegistroExp.
 */

public class MostrarImagenEstado extends JInternalFrame implements IMultilingue {
    private JLabel img;
    private final JFrame desktop;
    private JPanel panelImagen;
    private String estado;

    /**
     * Constructor de la clase que recibe el desktop (Frame) y el estado que se va a mostrar. Se asocian los eventos
     * de la pantalla y se llama al metodo para inicializar sus elementos.
     *
     * @param desktop JFrame
     * @param estado el nombre del estado del expediente
     */
    public MostrarImagenEstado(final JFrame desktop, String estado)
    {
        this.desktop = desktop;
        this.estado = estado;
        inicializaElementos();
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        UtilRegistroExp.menuBarSetEnabled(false, this.desktop);
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
        this.setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MostrarImagenEstado.titulo"));
        setClosable(true);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Metodo que inicializa los elementos de la gui y que asigna un tamaño y una posicion a la ventana. Tambien se
     * llama a la clase de utilidades para cargar la imagen del estado que se desea mostrar.
     */
    private void inicializaElementos()
    {
        img= new JLabel();
        img.setHorizontalAlignment(JLabel.CENTER);
        img.setOpaque(true);
        img.setBackground(Color.black);
        Icon iconoEstado= UtilRegistroExp.getIconoEstado(estado);
        img.setIcon(iconoEstado);
        int anchoPanel = iconoEstado.getIconWidth();
        int altoPanel = iconoEstado.getIconHeight();
        panelImagen= new JPanel();
        panelImagen.setOpaque(true);
        panelImagen.add(img, BorderLayout.CENTER);
        panelImagen.setPreferredSize(new Dimension(anchoPanel,altoPanel));
        getContentPane().add(panelImagen);
        setSize(new Dimension(anchoPanel+20,altoPanel+45));
        int w=(this.desktop.getWidth()/2)- (this.getWidth()/2);
        int d= (this.desktop.getHeight()/2) - (this.getHeight()/2)-30;
        this.setLocation(w,d);
    }

    /**
     * Normalmente en este metodo se habilita el menu de la aplicacion. En este caso no se hace porque esta ventana
     * no aparece solo, sino encima de otra y si se habita cuando se cierra se queda habilitado.
     */
    private void cierraInternalFrame()
    {
    }

    /**
     * Metodo que cambia las etiquetas al idioma que el usuario a selecionado. En este caso no hay ninguna etiqueta
     * que cambiar
     */
    public void renombrarComponentes()
    {
        this.setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MostrarImagenEstado.titulo"));
    }
}
