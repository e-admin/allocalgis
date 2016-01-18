package com.geopista.app.catastro.registroExpedientes.asociarParcela;

import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.registroExpedientes.paneles.*;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.I18N;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 31-ene-2007
 * Time: 17:14:14
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende de JInternalFrame y crea una pantalla que permite buscar Parcelas por
 * referencia y por direccion y asociarlos al expediente con el que se esta trabajando.
 * La clase se encarga de cargar los paneles y los dialogos necesario para funcionar.
 * */

public class AsociarParcelas extends JInternalFrame implements IMultilingue
{
    private JFrame desktop;
    private JPanel datosTablaPanel;
    private JPanel todoDatosMapaPanel;
    private JScrollPane todoDatosMapaScoll;
    private PanelDatosAsociarParcelasBienesInmuebles datosPanel;
    private TablaAsociarParcelasBienesInmuebles refCatasTablaBienesInmuebles;
    private JButton parcelaMapaATablaJButton;
    private JButton parcelaTablaAMapeJButton;
    private JButton elimarParcelaTablaJButton;
    private JButton guardarExpedienteJButton;
    private JButton salirJButton;
    private PanelMapa editorMapaPanel;
    private Expediente expediente;
    private ArrayList referenciasCatastrales;

    /**
     * Constructor de la clase. Inicializa todos los paneles de la pantalla y asocia los eventos para ser tratados.
     * Se le pasa por parametro un JFrame y el expediente con el que se esta trabajando.
     *
     * @param desktop
     * @param exp
     */
    public AsociarParcelas(final JFrame desktop, Expediente exp)
    {
        this.desktop= desktop;
        this.expediente= exp;
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
        this.setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.titulo"));
        setClosable(true);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    
    /**
     * Inicializa los elementos del panel.
     *
     */
    private void inicializaElementos()
    {
        todoDatosMapaPanel= new JPanel();
        todoDatosMapaScoll= new JScrollPane();
        todoDatosMapaPanel.setLayout(new BorderLayout());
        todoDatosMapaPanel.setPreferredSize(new Dimension(1000,600));
        inicializaPanelDatosTabla();
        inicializaPanelMap();
        inicializaBotones();
        todoDatosMapaPanel.add(datosTablaPanel, BorderLayout.WEST);
        todoDatosMapaPanel.add(editorMapaPanel,BorderLayout.CENTER);
        todoDatosMapaScoll.setViewportView(todoDatosMapaPanel);
        getContentPane().add(todoDatosMapaScoll);
    }

    /**
     * Inicializa los datos del panel y asocia los eventos de los botones de los paneles.
     */
    private void inicializaPanelDatosTabla()
    {
        datosTablaPanel= new JPanel();
        referenciasCatastrales= new ArrayList();
        if(expediente.getListaReferencias()!=null)
        {
            referenciasCatastrales.addAll(expediente.getListaReferencias());
        }
        datosTablaPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        datosPanel= new PanelDatosAsociarParcelasBienesInmuebles("Catastro.RegistroExpedientes.AsociarParcelas.datosPanel",
                String.valueOf(expediente.getNumeroExpediente()),expediente.getTipoExpediente().getConvenio());
        refCatasTablaBienesInmuebles = new TablaAsociarParcelasBienesInmuebles("Catastro.RegistroExpedientes.AsociarParcelas.refCatasTablaBienesInmuebles");
        datosTablaPanel.add(datosPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 530, 145));
        datosTablaPanel.add(refCatasTablaBienesInmuebles.getTablaPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 155, 500, 300));

        checkeaRefCatasInicio();

        JButton linkBotonBuscarRefCatastral= datosPanel.getReferenciaCatastralJButton();
        linkBotonBuscarRefCatastral.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                linkBotonBuscarRefCatastralActionPerformed();
            }
        });

        JButton linkBotonBuscarRefCatastralPorDir= datosPanel.getDirTipoViaNombreViaJButton();
        linkBotonBuscarRefCatastralPorDir.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                linkBotonBuscarRefCatastralPorDirActionPerformed();
            }
        });

        JButton linkBotonBuscarRefCatastralPorPoligono= datosPanel.getPoligonoJButton();
        linkBotonBuscarRefCatastralPorPoligono.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                linkBotonBuscarRefCatastralPorPoligonoActionPerformed();
            }
        });
    }

    /**
     * Carga los datos de la tabla de referencias catastrales.
     */
    private void checkeaRefCatasInicio()
    {
        if(expediente.getListaReferencias()!=null && expediente.getListaReferencias().size()>0)
        {
            refCatasTablaBienesInmuebles.cargaDatosTabla(referenciasCatastrales);
        }
    }

    /**
     * Iniciliza el panel del editor de mapas.
     *
     */
    private void inicializaPanelMap()
    {
        editorMapaPanel= new PanelMapa("Catastro.RegistroExpedientes.AsociarParcelas.editorMapaPanel");
        editorMapaPanel.setDesktop(desktop);
        editorMapaPanel.load(false,editorMapaPanel);
    }

    /**
     * Inicializa los botones del panel
     */    
    private void inicializaBotones()
    {
        parcelaMapaATablaJButton = new JButton();
        parcelaMapaATablaJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.parcelaMapaATablaJButton.hint"));
        parcelaMapaATablaJButton.setIcon(UtilRegistroExp.iconoFlechaIzquierda);
        parcelaMapaATablaJButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                parcelaMapaATablaJButtonActionPerformed();
            }
        });

        parcelaTablaAMapeJButton = new JButton();
        parcelaTablaAMapeJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.parcelaTablaAMapeJButton.hint"));
        parcelaTablaAMapeJButton.setIcon(UtilRegistroExp.iconoFlechaDerecha);
        parcelaTablaAMapeJButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                parcelaTablaAMapeJButtonActionPerformed();
            }
        });

        elimarParcelaTablaJButton = new JButton();
        elimarParcelaTablaJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.elimarParcelaTablaJButton.hint"));
        elimarParcelaTablaJButton.setIcon(UtilRegistroExp.iconoDeleteParcela);
        elimarParcelaTablaJButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                elimarParcelaTablaJButtonActionPerformed();
            }
        });

        guardarExpedienteJButton = new JButton();
        guardarExpedienteJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.guardarExpedienteJButton.hint"));
        guardarExpedienteJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.guardarExpedienteJButton"));

        salirJButton = new JButton();
        salirJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.salirJButton.hint"));
        salirJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.salirJButton"));

        datosTablaPanel.add(parcelaMapaATablaJButton,new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 165, 20, 20));
        datosTablaPanel.add(parcelaTablaAMapeJButton,new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 185, 20, 20));
        datosTablaPanel.add(elimarParcelaTablaJButton,new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 215, 20, 20));
        datosTablaPanel.add(guardarExpedienteJButton,new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 480, 100, 30));
        datosTablaPanel.add(salirJButton,new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 480, 100, 30));
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
     * Devuelve el boton guardar expediente
     *
     * @return JButton guardarExpedienteJButton
     */
    public JButton getGuardarExpedienteJButton()
    {
        return guardarExpedienteJButton;
    }

    /**
     * Devuelve el boton salir
     *
     * @return JButton salirJButton
     */
    public JButton getSalirJButton()
    {
        return salirJButton;
    }

    /**
     * Devuelve el expediente pasado por parametro.
     *
     * @return Expediente
     */
    public Expediente getExpediente()
    {
        return expediente;
    }

    /**
     * Renombra los elementos de la gui con el resourceBundle que se este utilizando.
     */
    public void renombrarComponentes()
    {
        this.setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.titulo"));        
        datosPanel.renombrarComponentes();
        editorMapaPanel.renombrarComponentes();
        parcelaMapaATablaJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.parcelaMapaATablaJButton.hint"));
        parcelaTablaAMapeJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.parcelaTablaAMapeJButton.hint"));
        elimarParcelaTablaJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.elimarParcelaTablaJButton.hint"));
        guardarExpedienteJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.guardarExpedienteJButton.hint"));
        guardarExpedienteJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.guardarExpedienteJButton"));
        salirJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.salirJButton.hint"));
        salirJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.salirJButton"));
    }


    /**
     * Evento asociado al botón de búsqueda de referencias catastrales. Abre el diálogo de búsqueda de
     * referencias catastrales.
     */
    private void linkBotonBuscarRefCatastralActionPerformed()
    {
        DialogoBuscarRefCatastrales dialogo = new DialogoBuscarRefCatastrales(desktop,true, expediente.getTipoExpediente().getConvenio());
		dialogo.setLocation(this.getWidth()/2-dialogo.getWidth()/2, this.getHeight()/2-dialogo.getHeight()/2);
		dialogo.setResizable(false);
		dialogo.show();
        if(dialogo.getReferenciasCatastralesSelecionadas()!=null)
        {
            if(!checkeaRefAniadiadas(dialogo.getReferenciasCatastralesSelecionadas()))
            {
                referenciasCatastrales.addAll(dialogo.getReferenciasCatastralesSelecionadas());
                refCatasTablaBienesInmuebles.cargaDatosTabla(referenciasCatastrales);
                FincaCatastro parcela = (FincaCatastro)dialogo.getReferenciasCatastralesSelecionadas().get(0);
                editorMapaPanel.actualizarFeatureSelection("parcelas",parcela.getRefFinca().getRefCatastral());
            }
            else
            {
                JOptionPane.showMessageDialog(desktop,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.mensajeRefRepetida"));
            }
        }
    }


    /**
     * Evento asociado al botón de búsqueda de referencias catastrales por dirección. Abre un diálogo con la
     * búsqueda de referencias catastrales por dirección.
     */
    private void linkBotonBuscarRefCatastralPorDirActionPerformed()
    {
        DialogoBuscarRefCatastralesPorDir dialogo = new DialogoBuscarRefCatastralesPorDir(desktop,true, expediente.getTipoExpediente().getConvenio());
		dialogo.setLocation(this.getWidth()/2-dialogo.getWidth()/2, this.getHeight()/2-dialogo.getHeight()/2);
		dialogo.setResizable(false);
		dialogo.show();
        if(dialogo.getReferenciasCatastralesSelecionadas()!=null)
        {
            if(!checkeaRefAniadiadas(dialogo.getReferenciasCatastralesSelecionadas()))
            {
                referenciasCatastrales.addAll(dialogo.getReferenciasCatastralesSelecionadas());
                refCatasTablaBienesInmuebles.cargaDatosTabla(referenciasCatastrales);
                FincaCatastro parcela = (FincaCatastro)dialogo.getReferenciasCatastralesSelecionadas().get(0);
                editorMapaPanel.actualizarFeatureSelection("parcelas",parcela.getRefFinca().getRefCatastral());
            }
            else
            {
                JOptionPane.showMessageDialog(desktop,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.mensajeRefRepetida"));
            }
        }
    }


    /**
     * Evento asociado al botón parcelaMapaATabla que borra las referencias que tuviera el expediente y asocia las
     * parcelas seleccionadas en el mapa.
     */
    private void parcelaMapaATablaJButtonActionPerformed()
    {
        Collection aux = editorMapaPanel.getFeaturesSelecionados();
        if(aux.size()>=1)
        {
            /*String msg1= I18N.get("RegistroExpedientes",
                            "Catastro.RegistroExpedientes.AsociarParcelas.msg1");
            String msg2= I18N.get("RegistroExpedientes",
                            "Catastro.RegistroExpedientes.AsociarParcelas.msg2");
            String msg3= I18N.get("RegistroExpedientes",
                            "Catastro.RegistroExpedientes.AsociarParcelas.msg3");
            String msg4= I18N.get("RegistroExpedientes",
                            "Catastro.RegistroExpedientes.AsociarParcelas.msg4");
            Object[] options = {msg1, msg2};

            if (JOptionPane.showOptionDialog(this, msg3, msg4, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, //don't use a custom Icon
                    options, //the titles of buttons
                    options[1])!=JOptionPane.OK_OPTION)
            {
                return;
            } */

            Iterator it = aux.iterator();
            ArrayList refCatas = new ArrayList();
            ArrayList refCatasAux = new ArrayList();
            ArrayList referenciasCatastralesFinales = new ArrayList();
            while (it.hasNext())
            {
                Feature feature = (Feature) it.next();
                if (feature == null)
                {
                    continue;
                }
                String referencia= UtilRegistroExp.checkNull(feature.getAttribute(2));
                refCatas.clear();
                refCatas.add(referencia);
                try
                {
                    refCatasAux.clear();
                    refCatasAux = (ArrayList)ConstantesRegistroExp.clienteCatastro.getFincaCatastroPorReferenciaCatastral(refCatas);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                if ((refCatasAux != null) && (refCatasAux.size() > 0))
                {
                    /** Recogemos los valores de las tablas parcelas y vias */
                    //Todo en licencias se añade a otro objeto mas, por si hay null, yo creo que no puede dar problema.
                    FincaCatastro parcela= (FincaCatastro) refCatasAux.get(0);
                    referenciasCatastralesFinales.add(parcela);

                }
                else
                {
                    /** Recogemos los atributos del schema (attributeNameToIndexMap) de la feature */
                    FincaCatastro parcela = new FincaCatastro();
                    ReferenciaCatastral refCatastral = new ReferenciaCatastral(referencia);
                    parcela.setRefFinca(refCatastral);
                    DireccionLocalizacion dir = new DireccionLocalizacion();
                    dir.setNombreVia(UtilRegistroExp.checkNull(feature.getAttribute(14)));
                    dir.setTipoVia("");
                    dir.setPrimerNumero(Integer.parseInt(UtilRegistroExp.checkNull(feature.getAttribute(8))));
                    parcela.setDirParcela(dir);
                    referenciasCatastralesFinales.add(parcela);

                }
            }
            if(!checkeaRefAniadiadas(referenciasCatastralesFinales))
            {
                referenciasCatastrales.addAll(referenciasCatastralesFinales);
                if(referenciasCatastrales.size()>0)
                {
                    refCatasTablaBienesInmuebles.cargaDatosTabla(referenciasCatastrales);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(desktop,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.mensajeRefRepetida"));
            }            

        }
        else
        {
            JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.msgSeleccionMapa"));
        }
    }


    /**
     * Evento asociado al botón parcelaTablaAMapa que permite hacer zoom sobre la parcela de la tabla seleccionada.
     */
    private void parcelaTablaAMapeJButtonActionPerformed()
    {
        int i = refCatasTablaBienesInmuebles.getParcelaSeleccionada();
        if(i>=0)
        {
            editorMapaPanel.actualizarFeatureSelection("parcelas",((FincaCatastro)referenciasCatastrales.get(i)).getRefFinca().getRefCatastral());
        }
        else
        {
            JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.msgZoom"));
        }
    }


    /**
     * Evento asociado al botón eliminarParcela que permite eliminar la asociacion, de la parcela seleccionada en la
     * tabla, con el expediente que se esta trabajando.
     */
    private void elimarParcelaTablaJButtonActionPerformed()
    {
        int i = refCatasTablaBienesInmuebles.getParcelaSeleccionada();
        if(i>=0)
        {
            String msg1= I18N.get("RegistroExpedientes",
                            "Catastro.RegistroExpedientes.AsociarParcelas.msg1");
            String msg2= I18N.get("RegistroExpedientes",
                            "Catastro.RegistroExpedientes.AsociarParcelas.msg2");
            String msg3= I18N.get("RegistroExpedientes",
                            "Catastro.RegistroExpedientes.AsociarParcelas.msg5");
            String msg4= I18N.get("RegistroExpedientes",
                            "Catastro.RegistroExpedientes.AsociarParcelas.msg4");
            Object[] options = {msg1, msg2};

            if (JOptionPane.showOptionDialog(this, msg3, msg4, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, //don't use a custom Icon
                    options, //the titles of buttons
                    options[1])!=JOptionPane.OK_OPTION)
            {
                return;
            }
            referenciasCatastrales.remove(i);
            refCatasTablaBienesInmuebles.cargaDatosTabla(referenciasCatastrales);
            if(referenciasCatastrales.size()>0)
            {
                FincaCatastro parcela = (FincaCatastro)referenciasCatastrales.get(0);
                editorMapaPanel.actualizarFeatureSelection("parcelas",parcela.getRefFinca().getRefCatastral());
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.msgEliminar"));
        }
    }


    /**
     * Recopila la información de las referencias del expediente.
     */
    public void recopilaDatos()
    {
        expediente.setListaReferencias(referenciasCatastrales);
    }


    /**
     * Devuelve un booleano indicando si el expediente ha sido modificado y se ha desasociado a todas las referencias
     * catastrales que tenia, lo cual solo puede suceder en los estados registrado y asociado.
     *
     * @return boolean
     */
    public boolean checkeaReferEstado()
    {
        return ConstantesRegistroExp.ESTADO_REGISTRADO==expediente.getIdEstado() ||
               ConstantesRegistroExp.ESTADO_ASOCIADO==expediente.getIdEstado() ||(referenciasCatastrales!=null
                && referenciasCatastrales.size()>0);
    }

    /**
     * Comprueba que la referencia que se desea asociar no este ya asociada a este expediente.
     *
     * @param refNuevas
     * @return boolean
     */    
    private boolean checkeaRefAniadiadas(ArrayList refNuevas)
    {
        for(int i = 0;i<refNuevas.size();i++)
        {
            FincaCatastro auxFinca = (FincaCatastro)refNuevas.get(i);
            for(int j= 0; j<referenciasCatastrales.size();j++)
            {
                if(auxFinca.getRefFinca().getRefCatastral().equalsIgnoreCase
                        (((FincaCatastro)referenciasCatastrales.get(j)).getRefFinca().getRefCatastral()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Evento asociado al botón de búsqueda de referencias por poligono. Abre un diálogo con la
     * búsqueda de referencias catastrales por poligono.
     */
    private void linkBotonBuscarRefCatastralPorPoligonoActionPerformed()
    {
        DialogoBuscarRefCatastralPorPoligono dialogo = new DialogoBuscarRefCatastralPorPoligono(desktop,true, expediente.getTipoExpediente().getConvenio());
		dialogo.setLocation(this.getWidth()/2-dialogo.getWidth()/2, this.getHeight()/2-dialogo.getHeight()/2);
		dialogo.setResizable(false);
		dialogo.show();
        if(dialogo.getReferenciasCatastralesSelecionadas()!=null)
        {
            if(!checkeaRefAniadiadas(dialogo.getReferenciasCatastralesSelecionadas()))
            {
                referenciasCatastrales.addAll(dialogo.getReferenciasCatastralesSelecionadas());
                refCatasTablaBienesInmuebles.cargaDatosTabla(referenciasCatastrales);
                FincaCatastro parcela = (FincaCatastro)dialogo.getReferenciasCatastralesSelecionadas().get(0);
                editorMapaPanel.actualizarFeatureSelection("parcelas",parcela.getRefFinca().getRefCatastral());
            }
            else
            {
                JOptionPane.showMessageDialog(desktop,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarParcelas.mensajeRefRepetida"));
            }
        }
    }
}
