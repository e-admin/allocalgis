package com.geopista.app.inventario.panel.lotes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;

import javax.swing.JTabbedPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.panel.DatosAmortizacionJPanel;
import com.geopista.app.inventario.panel.DatosSegurosJPanel;
import com.geopista.app.inventario.panel.GestionDocumentalJPanel;
import com.geopista.app.inventario.panel.ObservacionesJPanel;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.inventario.Lote;

import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

public class LoteJDialog  extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(LoteJDialog.class);

    public OKCancelPanel okCancelPanel = new OKCancelPanel();
    private ApplicationContext aplicacion;

    private LotePanel lotePanel;
    private LotesBienesJPanel lotesBienesJPanel;
    private ObservacionesJPanel observacionesJPanel;
    private DatosAmortizacionJPanel amortizacionJPanel;
    private DatosSegurosJPanel datosSegurosJPanel;
    private InventarioClient inventarioClient; 
    
    
     /* constructor de la clase */
    public LoteJDialog(JFrame frame, char tipo,
    		String locale, Lote lote, String operacion)  throws Exception
    {
        super(frame);
        aplicacion = (AppContext) AppContext.getApplicationContext();
        renombrarComponentes(operacion);
        inventarioClient= new InventarioClient(aplicacion.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
        		Constantes.INVENTARIO_SERVLET_NAME);
 
        getContentPane().setLayout(new BorderLayout());
        /* Cargamos la lista */
        
        okCancelPanel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
				okCancelPanel_actionPerformed(e);
			}
		});
		this.addComponentListener(new java.awt.event.ComponentAdapter()
        {
			public void componentShown(ComponentEvent e)
            {
				this_componentShown(e);
			}
		});

        setModal(true);
        boolean enabled=Constantes.OPERACION_MODIFICAR.equals(operacion);
        
        lotePanel = new LotePanel(lote, false);
        lotePanel.setEnabled(enabled);
        
        JTabbedPane panelPestanias = new JTabbedPane();
        panelPestanias.addTab(aplicacion.getI18nString("inventario.lote"),lotePanel);
        
        lotesBienesJPanel = new LotesBienesJPanel(locale,frame, lote);
       
        
        panelPestanias.addTab(aplicacion.getI18nString("inventario.bienesrevertibles.listabienes"), lotesBienesJPanel);
        
        datosSegurosJPanel= new DatosSegurosJPanel();
        /** cargamos el seguro */
        datosSegurosJPanel.load(lote);
        datosSegurosJPanel.setEnabledDatos(enabled);
        datosSegurosJPanel.setOperacion(operacion);
        panelPestanias.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab6"), datosSegurosJPanel);

        
        panelPestanias.addTab(aplicacion.getI18nString("inventario.lote.documentos"),lotePanel.getDocumentosJPanel());
      
        getContentPane().add(panelPestanias, BorderLayout.CENTER);
        getContentPane().add(okCancelPanel, BorderLayout.SOUTH);
        setSize(500, 650);
      
        
        GUIUtil.centreOnWindow(this);
      
    }
    public void renombrarComponentes(String operacion){
    	//TODO hay que mirar si es consulta o modificacion
    	if (Constantes.OPERACION_MODIFICAR.equals(operacion))
    		setTitle(aplicacion.getI18nString("inventario.dialogo.tag2"));//"Lotes");
    	if (Constantes.OPERACION_CONSULTAR.equals(operacion))
    		setTitle(aplicacion.getI18nString("inventario.dialogo.tag3"));//"Lotes");
    	
    }

      /* sólo se realiza una accion si se ha seleccionado un elemento de la lista */
    void okCancelPanel_actionPerformed(ActionEvent e) 
    {
    	try{
         if(okCancelPanel.wasOKPressed())
         {
        	 Lote lote= lotePanel.getLote();
        	 datosSegurosJPanel.actualizarDatos(lote);
        	 lote=(Lote)inventarioClient.updateInventario(lote,lotePanel.getDocumentosJPanel().getFilesInUp());
         }
         setVisible(false);
         return;
    	}catch (Exception ex){
    		logger.error("Se ha producido un error al cargar el fichero",ex);
       	    ErrorDialog.show(this, "ERROR", aplicacion.getI18nString("inventario.lote.error"), StringUtil.stackTrace(ex));
         }
    	
    }
   
    /* recogemos el evento cd se pulsa Aceptar */
    void this_componentShown(ComponentEvent e)
    {
        okCancelPanel.setOKPressed(false);
    }
}
