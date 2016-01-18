package com.geopista.app.cementerios.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.apache.commons.lang.StringUtils;

import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.cementerios.Estructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.cementerios.CampoFiltro;
import com.geopista.protocol.cementerios.Const;

public class BuscarJDialog extends JDialog{
    private AppContext aplicacion;

    private String valor;
    private ArrayList campos;
    private String tipo;
    private String subtipo;
    private int dominio;
    private String locale;
    private Collection filtro;
    private ArrayList actionListeners= new ArrayList();

    /**
     * Método que genera el dialogo de busqueda
     */
    public BuscarJDialog(JFrame desktop, String tipo,String subtipo, ArrayList campos, String locale) {
        super(desktop);
        this.tipo= tipo;
        this.subtipo=subtipo;
        this.locale= locale;
        this.campos = campos;
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
        addAyudaOnline();
    }

    private void initComponents() {
    	filtro = new ArrayList();
        valorJLabel = new javax.swing.JLabel();
        valorJTField = new com.geopista.app.utilidades.TextField(254);
        aceptarJButton= new javax.swing.JButton();
        cancelarJButton= new javax.swing.JButton();
        aceptarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarJButtonActionPerformed();
            }
        });
        cancelarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarJButtonActionPerformed();
            }
        });

        getContentPane().setLayout(new BorderLayout());
        setModal(true);

        JPanel buscarJPanel= new JPanel();
        buscarJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        buscarJPanel.add(valorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 320, 20));
        buscarJPanel.add(valorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 320, -1));

        JPanel botoneraJPanel= new JPanel();
        botoneraJPanel.setLayout(new java.awt.FlowLayout());
        botoneraJPanel.add(aceptarJButton);
        botoneraJPanel.add(cancelarJButton);

        getContentPane().add(buscarJPanel, BorderLayout.CENTER);
        getContentPane().add(botoneraJPanel, BorderLayout.SOUTH);

        setSize(350, 150);
        setLocation(200, 140);

    }

    /**
     * Ayuda Online
     *
     */
    private void addAyudaOnline() {
        getRootPane()
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("F1"), "action F1");

        getRootPane().getActionMap().put("action F1", new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                String uriRelativa = "/Geocuenca:Inventario:Buscar";
                GeopistaBrowser.openURL(aplicacion
                        .getString("ayuda.geopista.web")
                        + uriRelativa);
            }
        });
    }    

    private void renombrarComponentes(){
        try{setTitle(aplicacion.getI18nString("cementerio.buscar.tag1"));}catch(Exception e){}
        try{valorJLabel.setText(aplicacion.getI18nString("cementerio.buscar.tag2"));}catch(Exception e){}
        try{aceptarJButton.setText(aplicacion.getI18nString("cementerio.buscar.tag3"));}catch(Exception e){}
        try{cancelarJButton.setText(aplicacion.getI18nString("cementerio.buscar.tag4"));}catch(Exception e){}

    }


    private void cancelarJButtonActionPerformed(){
        valor= null;
        fireActionPerformed();
    }

    private void aceptarJButtonActionPerformed(){
        if (valorJTField.getText().trim().equalsIgnoreCase("")) valor= null;
        else valor= valorJTField.getText().trim();
        generarFiltro();       
        fireActionPerformed();
    }

    public String getValor(){
        return valor;
    }

    public void addActionListener(ActionListener l) {
        this.actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        this.actionListeners.remove(l);
    }

    private void fireActionPerformed() {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(new ActionEvent(this, 0, null));
        }
    }


    public void  generarFiltro(){
    	CampoFiltro campoFiltro;
    	for (int i = 0; i < campos.size(); i++) {
			campoFiltro = (CampoFiltro) campos.get(i);
			if (campoFiltro.isDominio()){
				campoFiltro = cargarDominio(dominio, campoFiltro);
				campoFiltro.setOperador("=");
			}else if (campoFiltro.isVarchar()){
				campoFiltro.setOperador("LIKE");
				campoFiltro.setValorVarchar(valor);
			}else{
				campoFiltro.setOperador("=");
				campoFiltro.setValorVarchar(valor);
			}
			filtro.add(campoFiltro);
		}    	
    }

    
    private CampoFiltro cargarDominio(int dominio, CampoFiltro campoFiltro){
        Vector vDomaninTipo = null;
        switch (dominio){
        case Const.unidadEnterramiento:
        	vDomaninTipo = Estructuras.getListaCombosSorted(locale);
        	break;
    	case Const.bloque:
    		vDomaninTipo = Estructuras.getListaCombosBloqueSorted(locale);
    	break;
    	case Const.concesion:
        	vDomaninTipo = Estructuras.getListaComboConcesiones(locale);
    	break;
    	case Const.contenedor:
    		vDomaninTipo = Estructuras.getListaComboContenedores(locale);    		
    	break;
    	case Const.inhumacion:
    		vDomaninTipo = Estructuras.getListaCombosTipoExhumacionesSorted(locale);    		
    	break;
    	case Const.exhumacion:
    		vDomaninTipo = Estructuras.getListaCombosTipoExhumacionesSorted(locale);    		
    	break;
    	case Const.servicios:
    		vDomaninTipo = Estructuras.getListaCombosServiciosSorted(locale);    		
    	break;
    	}
        for (int i = 0; i < vDomaninTipo.size(); i++) {
      		DomainNode node = (DomainNode) vDomaninTipo.get(i);
	  		String nodeterm = node.getTerm(locale);
	  		if ((nodeterm.equalsIgnoreCase(valor)) || 
	  				(StringUtils.containsIgnoreCase(nodeterm, valor))) {
	  			campoFiltro.setValorVarchar(node.getPatron());
	  			break;
	  		}
        }
        return campoFiltro;
    }

    public Collection getFiltro() {
		return filtro;
	}

	private javax.swing.JLabel valorJLabel;
    private javax.swing.JTextField valorJTField;
    private javax.swing.JButton aceptarJButton;
    private javax.swing.JButton cancelarJButton;



}
