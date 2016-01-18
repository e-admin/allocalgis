package com.geopista.app.catastro.registroExpedientes.asociarBienInmueble;

import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.registroExpedientes.paneles.*;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.vividsolutions.jump.I18N;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Clase que extiende de JInternalFrame y crea una pantalla que permite buscar Bienes Inmuebles por
 * referencia, por direccion y por nif del titular y asociarlos al expediente con el que se esta trabajando.
 * La clase se encarga de cargar los paneles y los dialogos necesario para funcionar.
 * */
public class AsociarBienInmueble extends JInternalFrame implements IMultilingue{
    private JFrame desktop;
    private JPanel datosTablaPanel;
    private JPanel todoDatosMapaPanel;
    private JScrollPane todoDatosMapaScoll;
    private PanelDatosAsociarParcelasBienesInmuebles datosPanel;
    private TablaAsociarParcelasBienesInmuebles refCatasTablaBienesInmuebles;
    private JButton guardarExpedienteJButton;
    private JButton salirJButton;
    private Expediente expediente;
    private ArrayList referenciasBienesInmuebles;
    private JButton elimarBITablaJButton;

    /**
     * Constructor de la clase. Inicializa todos los paneles de la pantalla y asocia los eventos para ser tratados.
     * Se le pasa por parametro un JFrame y el expediente con el que se esta trabajando.
     *
     * @param desktop  el JFrame
     * @param exp el expediente
     */
    public AsociarBienInmueble(final JFrame desktop, Expediente exp){
        this.desktop= desktop;
        this.expediente= exp;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        UtilRegistroExp.menuBarSetEnabled(false, this.desktop);
        inicializaElementos();
        addInternalFrameListener(new javax.swing.event.InternalFrameListener()
        {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) { }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {}
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt){
                cierraInternalFrame();
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt){}
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt){}
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {}
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {}
        });
        this.setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarBienInmueble.titulo"));
        setClosable(true);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

   /**
    * Inicializa los elementos del panel.
    */
    private void inicializaElementos() {
        todoDatosMapaPanel= new JPanel();
        todoDatosMapaScoll= new JScrollPane();
        todoDatosMapaPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        todoDatosMapaPanel.setPreferredSize(new Dimension(540,610));
        inicializaPanelDatosTabla();
        inicializaBotones();
        todoDatosMapaPanel.add(datosTablaPanel,new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 530, 600));
        todoDatosMapaScoll.setViewportView(todoDatosMapaPanel);
        getContentPane().add(todoDatosMapaScoll);
        setSize(new Dimension(560,650));
    }

    
    /**
     * Inicializa los datos del panel y asocia los eventos de los botones de los paneles.
     */
    private void inicializaPanelDatosTabla() {
        datosTablaPanel= new JPanel();
        referenciasBienesInmuebles= new ArrayList();

        if(expediente.getListaReferencias()!=null)
            referenciasBienesInmuebles.addAll(expediente.getListaReferencias());

        datosTablaPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        datosPanel= new PanelDatosAsociarParcelasBienesInmuebles("Catastro.RegistroExpedientes.AsociarBienInmueble.datosPanel",
                String.valueOf(expediente.getNumeroExpediente()),expediente.getTipoExpediente().getConvenio());
        refCatasTablaBienesInmuebles = new TablaAsociarParcelasBienesInmuebles("Catastro.RegistroExpedientes.AsociarBienInmueble.refCatasTablaBienesInmuebles");
        datosTablaPanel.add(datosPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 10, 525, 145));
        datosTablaPanel.add(refCatasTablaBienesInmuebles.getTablaPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 155, 500, 300));

        checkeaRefCatasInicio();

        JButton linkBotonBuscarRefCatastral= datosPanel.getReferenciaCatastralJButton();
        linkBotonBuscarRefCatastral.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                linkBotonBuscarRefCatastralActionPerformed();
            }
        });

        JButton linkBotonBuscarRefCatastralPorDir= datosPanel.getDirTipoViaNombreViaJButton();
        linkBotonBuscarRefCatastralPorDir.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                linkBotonBuscarRefCatastralPorDirActionPerformed();
            }
        });

        JButton linkBotonBuscarRefCatastralPorTitular= datosPanel.getNifTitularJButton();
        linkBotonBuscarRefCatastralPorTitular.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkBotonBuscarRefCatastralPorTitularActionPerformed();
            }
        });

        JButton linkBotonBuscarRefCatastralPorPoligono= datosPanel.getPoligonoJButton();
        linkBotonBuscarRefCatastralPorPoligono.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                linkBotonBuscarRefCatastralPorPoligonoActionPerformed();
            }
        });
    }

    /**
     * Carga los datos de la tabla de referencias catastrales.
     */
    private void checkeaRefCatasInicio() {
        if(expediente.getListaReferencias()!=null && expediente.getListaReferencias().size()>0)
            refCatasTablaBienesInmuebles.cargaDatosTabla(referenciasBienesInmuebles);
    }

    
    /**
     * Inicializa los botones del panel
     */
    private void inicializaBotones() {
        guardarExpedienteJButton = new JButton();
        guardarExpedienteJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarBienInmueble.guardarExpedienteJButton.hint"));
        guardarExpedienteJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarBienInmueble.guardarExpedienteJButton"));

        salirJButton = new JButton();
        salirJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarBienInmueble.salirJButton.hint"));
        salirJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarBienInmueble.salirJButton"));

        datosTablaPanel.add(guardarExpedienteJButton,new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 480, 100, 30));
        datosTablaPanel.add(salirJButton,new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 480, 100, 30));

        elimarBITablaJButton = new JButton();
        elimarBITablaJButton.setToolTipText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.AsociarParcelas.elimarParcelaTablaJButton.hint"));
        elimarBITablaJButton.setIcon(UtilRegistroExp.iconoDeleteParcela);
        elimarBITablaJButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                elimarBITablaJButtonActionPerformed();
            }
        });

        datosTablaPanel.add(elimarBITablaJButton,new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 165, 20, 20));
    }

    private void elimarBITablaJButtonActionPerformed(){

        int i = refCatasTablaBienesInmuebles.getParcelaSeleccionada();

        if(i>=0) {
            String msg1= I18N.get("RegistroExpedientes",
                    "Catastro.RegistroExpedientes.AsociarParcelas.msg1");
            String msg2= I18N.get("RegistroExpedientes",
                    "Catastro.RegistroExpedientes.AsociarParcelas.msg2");
            String msg3= I18N.get("RegistroExpedientes",
                    "Catastro.RegistroExpedientes.AsociarBienInmueble.msg5");
            String msg4= I18N.get("RegistroExpedientes",
                    "Catastro.RegistroExpedientes.AsociarParcelas.msg4");
            Object[] options = {msg1, msg2};

            if (JOptionPane.showOptionDialog(this, msg3, msg4, JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[1])!=JOptionPane.OK_OPTION){
                return;
            }

            referenciasBienesInmuebles.remove(i);
            refCatasTablaBienesInmuebles.cargaDatosTabla(referenciasBienesInmuebles);
        }
        else
        {
            JOptionPane.showMessageDialog(this, I18N.get("RegistroExpedientes",
                    "Catastro.RegistroExpedientes.AsociarBienInmueble.msgEliminar"));
        }
    }

    /**
     * Cierra la ventana y habilita el menu de la aplicacion.
     */
    private void cierraInternalFrame() {
        try{
            this.setClosed(true);
        }
        catch(Exception e){e.printStackTrace();}
        UtilRegistroExp.menuBarSetEnabled(true, this.desktop);
    }

    /**
     * Devuelve el boton guardar de la pantalla.
     *
     * @return JButton guardarExpedienteJButton
     */
    public JButton getGuardarExpedienteJButton(){
        return guardarExpedienteJButton;
    }

    /**
     * Devuelve el boton salir de la pantalla.
     *
     * @return JButton salirJButton
     */
    public JButton getSalirJButton(){
        return salirJButton;
    }

    /**
     * Devuelve el expediente pasado por parametro.
     *
     * @return Expediente
     */
    public Expediente getExpediente(){
        return expediente;
    }

    /**
     * Renombra los elementos de la gui con el resourceBundle que se este utilizando.
     */
    public void renombrarComponentes(){
        this.setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarBienInmueble.titulo"));        
        datosPanel.renombrarComponentes();
        guardarExpedienteJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarBienInmueble.guardarExpedienteJButton.hint"));
        guardarExpedienteJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarBienInmueble.guardarExpedienteJButton"));
        salirJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarBienInmueble.salirJButton.hint"));
        salirJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarBienInmueble.salirJButton"));
    }

    /**
     * Evento asociado al botón de búsqueda de referencias catastrales. Abre el diálogo de búsqueda de
     * referencias catastrales.
     */
    private void linkBotonBuscarRefCatastralActionPerformed(){
        DialogoBuscarRefCatastrales dialogo = new DialogoBuscarRefCatastrales(desktop,true, expediente.getTipoExpediente().getConvenio());
		dialogo.setLocation(this.getWidth()/2-dialogo.getWidth()/2, this.getHeight()/2-dialogo.getHeight()/2);
		dialogo.setResizable(false);
		dialogo.show();
        if(dialogo.getReferenciasCatastralesSelecionadas()!=null)
        {
            if(!checkeaRefAniadiadas(dialogo.getReferenciasCatastralesSelecionadas()))
            {
                referenciasBienesInmuebles.addAll(dialogo.getReferenciasCatastralesSelecionadas());
                refCatasTablaBienesInmuebles.cargaDatosTabla(referenciasBienesInmuebles);
            }
            else
            {
                JOptionPane.showMessageDialog(desktop,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarBienInmueble.mensajeRefRepetida"));
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
                referenciasBienesInmuebles.addAll(dialogo.getReferenciasCatastralesSelecionadas());
                refCatasTablaBienesInmuebles.cargaDatosTabla(referenciasBienesInmuebles);
            }
            else
            {
                JOptionPane.showMessageDialog(desktop,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarBienInmueble.mensajeRefRepetida"));
            }
        }
    }
   
    /**
     * Evento asociado al botón de búsqueda de referencia catastral por titular. Abre un diálogo para
     * dicha búsqueda.
     */
    private void linkBotonBuscarRefCatastralPorTitularActionPerformed() {
        DialogoBuscarBienInmuebleNifTitular dialogo = new DialogoBuscarBienInmuebleNifTitular(desktop,true);
		dialogo.setLocation(this.getWidth()/2-dialogo.getWidth()/2, this.getHeight()/2-dialogo.getHeight()/2);
		dialogo.setResizable(false);
		dialogo.show();
        if(dialogo.getReferenciasCatastralesSelecionadas()!=null)
        {
            if(!checkeaRefAniadiadas(dialogo.getReferenciasCatastralesSelecionadas()))
            {
                referenciasBienesInmuebles.addAll(dialogo.getReferenciasCatastralesSelecionadas());
                refCatasTablaBienesInmuebles.cargaDatosTabla(referenciasBienesInmuebles);
            }
            else
            {
                JOptionPane.showMessageDialog(desktop,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarBienInmueble.mensajeRefRepetida"));
            }
        }
    }

    
    /**
     * Recopila la información de las referencias del expediente.
     */
    public void recopilaDatos(){
        expediente.setListaReferencias(referenciasBienesInmuebles);
    }

    
    /**
     * Devuelve un booleano indicando si el expediente ha sido modificado y se ha desasociado a todas las referencias
     * catastrales que tenia, lo cual solo puede suceder en los estados registrado y asociado.
     *
     * @return boolean
     */
    public boolean checkeaReferEstado() {

        return ConstantesRegistroExp.ESTADO_REGISTRADO==expediente.getIdEstado() ||
               ConstantesRegistroExp.ESTADO_ASOCIADO==expediente.getIdEstado() ||(referenciasBienesInmuebles!=null
                && referenciasBienesInmuebles.size()>0);
    }

    /**
     * Comprueba que la referencia que se desea asociar no este ya asociada a este expediente.
     *
     * @param refNuevas
     * @return boolean
     */
    private boolean checkeaRefAniadiadas(ArrayList refNuevas){
        for(int i = 0;i<refNuevas.size();i++) {
            BienInmuebleCatastro bien = (BienInmuebleCatastro)refNuevas.get(i);

            for(int j= 0; j<referenciasBienesInmuebles.size();j++){
                if(bien.getIdBienInmueble().getIdBienInmueble().equalsIgnoreCase

                        (((BienInmuebleCatastro)referenciasBienesInmuebles.get(j)).getIdBienInmueble().getIdBienInmueble()))
                    return true;
            }
        }
        return false;
    }

    /**
     * Evento asociado al botón de búsqueda de referencias por poligono. Abre un diálogo con la
     * búsqueda de referencias catastrales por poligono.
     */
    private void linkBotonBuscarRefCatastralPorPoligonoActionPerformed() {
        DialogoBuscarRefCatastralPorPoligono dialogo = new DialogoBuscarRefCatastralPorPoligono(desktop,true, expediente.getTipoExpediente().getConvenio());
		dialogo.setLocation(this.getWidth()/2-dialogo.getWidth()/2, this.getHeight()/2-dialogo.getHeight()/2);
		dialogo.setResizable(false);
		dialogo.show();
        if(dialogo.getReferenciasCatastralesSelecionadas()!=null){
            if(!checkeaRefAniadiadas(dialogo.getReferenciasCatastralesSelecionadas())){
                referenciasBienesInmuebles.addAll(dialogo.getReferenciasCatastralesSelecionadas());
                refCatasTablaBienesInmuebles.cargaDatosTabla(referenciasBienesInmuebles);
            }
            else
            {
                JOptionPane.showMessageDialog(desktop,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.AsociarBienInmueble.mensajeRefRepetida"));
            }
        }
    }

}
