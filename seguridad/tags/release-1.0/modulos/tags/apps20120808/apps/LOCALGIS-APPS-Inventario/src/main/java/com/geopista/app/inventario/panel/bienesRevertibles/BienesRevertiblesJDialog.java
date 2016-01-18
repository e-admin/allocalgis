package com.geopista.app.inventario.panel.bienesRevertibles;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.geopista.app.AppContext;

import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.InventarioInternalFrame;
import com.geopista.app.inventario.MainInventario;
import com.geopista.app.inventario.panel.DatosAmortizacionJPanel;
import com.geopista.app.inventario.panel.DatosSegurosJPanel;
import com.geopista.app.inventario.panel.GestionDocumentalJPanel;
import com.geopista.app.inventario.panel.ObservacionesJPanel;
import com.geopista.protocol.Version;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.InventarioClient;

import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;
public class BienesRevertiblesJDialog  extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(BienesRevertiblesJDialog.class);

    public OKCancelPanel okCancelPanel = new OKCancelPanel();
    private ApplicationContext aplicacion;

    private BienesRevertiblesPanel brp;
    private BienesRevertiblesBienesJPanel brBienes;
    private ObservacionesJPanel observacionesJPanel;
    private DatosAmortizacionJPanel amortizacionJPanel;
    private GestionDocumentalJPanel documentosJPanel;
    private DatosSegurosJPanel datosSegurosJPanel;
    
	private InventarioClient inventarioClient;
    private String tipoOperacion;
    private BienRevertible bienRevertible;
     /* constructor de la clase */
    public BienesRevertiblesJDialog(JFrame frame, char tipo,
    		String locale, BienRevertible bienRevertibleAux, String operacion, String tipoBien)  throws Exception
    {
        super(frame);
        this.bienRevertible=bienRevertibleAux;
        tipoOperacion=operacion;
        aplicacion = (AppContext) AppContext.getApplicationContext();
        renombrarComponentes();
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
        boolean enabled=Constantes.OPERACION_MODIFICAR.equals(operacion) || 
                        Constantes.OPERACION_ANNADIR.equals(operacion);
        brp = new BienesRevertiblesPanel( bienRevertible,locale);
        brp.setEnabled(enabled);
        JTabbedPane panelPestanias = new JTabbedPane();
        panelPestanias.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        panelPestanias.addTab(aplicacion.getI18nString("inventario.bienesrevertibles.bienrevertible"),brp);
        
        brBienes = new BienesRevertiblesBienesJPanel(locale,frame, bienRevertible, tipoBien);
        brBienes.setEnabled(enabled);
        panelPestanias.addTab(aplicacion.getI18nString("inventario.bienesrevertibles.listabienes"),brBienes);
        
        datosSegurosJPanel= new DatosSegurosJPanel();
        
        /** cargamos el seguro */
        datosSegurosJPanel.load(bienRevertible);
        datosSegurosJPanel.setEnabledDatos(enabled);
        datosSegurosJPanel.setOperacion(operacion);
        panelPestanias.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab6"), datosSegurosJPanel);

        observacionesJPanel = new ObservacionesJPanel();
        observacionesJPanel.setOperacion(operacion);
        observacionesJPanel.load(bienRevertible==null?null:bienRevertible.getObservaciones());
        observacionesJPanel.setEnabled(enabled);
        panelPestanias.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab8"), observacionesJPanel);
		
		amortizacionJPanel = new DatosAmortizacionJPanel(frame, locale);
		amortizacionJPanel.load(bienRevertible==null?null:bienRevertible.getCuentaAmortizacion());
		amortizacionJPanel.setEnabled(enabled);
		panelPestanias.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab5"), amortizacionJPanel);
		
		documentosJPanel = new GestionDocumentalJPanel(false);
		if (bienRevertible.getId()!=null)
			documentosJPanel.load(bienRevertible);
		documentosJPanel.setEnabled(enabled);
		panelPestanias.addTab(aplicacion.getI18nString("inventario.lote.documentos"),documentosJPanel);
	      
        getContentPane().add(panelPestanias, BorderLayout.CENTER);
        getContentPane().add(okCancelPanel, BorderLayout.SOUTH);
        setSize(490, 700);
        GUIUtil.centreOnWindow(this);
      
    }
    public void renombrarComponentes(){
    	setTitle(aplicacion.getI18nString("inventario.bienesrevertibles.bienesrevertibles"));//"Bienes revertibles");
    }

      /* sólo se realiza una accion si se ha seleccionado un elemento de la lista */
    void okCancelPanel_actionPerformed(ActionEvent e) 
    {
    	try{
    	 if (tipoOperacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
    		 setVisible(false);
    		 return;
    	 }
         if(okCancelPanel.wasOKPressed())
         {
        	 if (JOptionPane.showConfirmDialog(this, aplicacion.getI18nString("inventario.optionpane.tag1"), aplicacion.getI18nString("inventario.optionpane.tag2"), JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
                 return;
             }
        	 bienRevertible= brp.getBienRevertible();
        	 bienRevertible.setBienes(brBienes.getListaBienes());
        	 bienRevertible.setObservaciones(observacionesJPanel.getObservaciones());
        	 bienRevertible.setCuentaAmortizacion(amortizacionJPanel.getCuentaAmortizacion());
        	 datosSegurosJPanel.actualizarDatos(bienRevertible);
        	 documentosJPanel.actualizarDatos(bienRevertible);
        	 
        	 //Eliminamos la limitación de que deban existir biene asociados
        	 //if (bienRevertible.getBienes()==null || bienRevertible.getBienes().size()==0 ){
        	 //   	   JOptionPane optionPane= new JOptionPane(aplicacion.getI18nString("Debe seleccionar por lo menos un bien"),JOptionPane.WARNING_MESSAGE);
        	 //     	   JDialog dialog =optionPane.createDialog(this,aplicacion.getI18nString("inventario.bienesrevertibles.seleccionarbienes"));
        	 //     	   dialog.setVisible(true);
        	 //     	   return;   
        	 //}
        	 if (bienRevertible.getId()!=null && (bienRevertible.getNumInventario()==null
         			|| bienRevertible.getNumInventario().trim().length()==0))
         		{
         			 /** Mostramos mensaje de bloqueo del bien */
        		 JOptionPane.showMessageDialog(this, aplicacion.getI18nString("inventario.mensajes.tag5"));
        		 return;
         		}
         		BienRevertible auxBean=inventarioClient.getBienRevertibleByNumInventario(bienRevertible.getNumInventario());
         		
         		if (auxBean !=null && (bienRevertible.getId()==null ||
         				               auxBean.getId().longValue()!= bienRevertible.getId().longValue())){
        			 /** Mostramos mensaje de bloqueo del bien */
						 JOptionPane.showMessageDialog(this, aplicacion.getI18nString("inventario.mensajes.tag6"));
						 return;
         		}
        	 bienRevertible=(BienRevertible)inventarioClient.updateInventario(bienRevertible, documentosJPanel.getFilesInUp());
         }
         //No tengo claro que haya que actualizar aqui la version
         //updateVersion();
         setVisible(false);
         return;
    	}catch (Exception ex){
    		logger.error("Se ha producido un error al cargar el fichero",ex);
       	    ErrorDialog.show(this, "ERROR", aplicacion.getI18nString("inventario.bienesrevertibles.error"), StringUtil.stackTrace(ex));
         }
    	
    }
   

    public BienRevertible getBienRevertible() {
		return bienRevertible;
	}
	public void setBienRevertible(BienRevertible bienRevertible) {
		this.bienRevertible = bienRevertible;
	}
	/**
     * Este metodo solo se puede utilizar para la aplicacion de Inventario para el plugin
     * interno que se utiliza dentro del editor no funciona
     * @return
     */
    private InventarioInternalFrame getInventarioFrame(){
    	try{
    		return ((InventarioInternalFrame)((MainInventario)AppContext.getApplicationContext().getMainFrame()).getIFrame());
    	}catch(Exception ex){
    		return null;
    	}	
    }
    /* recogemos el evento cd se pulsa Aceptar */
    void this_componentShown(ComponentEvent e)
    {
        okCancelPanel.setOKPressed(false);
    }
    /**
     * Actualiza la version
     */
    private void updateVersion(){
    	try{
    		String hora = (String)inventarioClient.getHora(Const.ACTION_GET_HORA);
    		String fechaVersion = (String) new SimpleDateFormat("yyyy-MM-dd").format(new Date())+" "+hora;
    		Version version = new Version();
    		version.setFecha(fechaVersion);
    		version.setFeaturesActivas(true);
    		AppContext.getApplicationContext().getBlackboard().put(AppContext.VERSION,version);
    		com.geopista.protocol.inventario.Const.fechaVersion=fechaVersion;
    		if (getInventarioFrame()!=null)getInventarioFrame().setFecha((String) new SimpleDateFormat("dd-MM-yyyy").format(new Date())+" "+hora);
        }catch(Exception ex){
    		logger.error("Error al actualizar la version despues de la carga:", ex);
    	}
	}
}
