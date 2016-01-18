package com.geopista.app.inventario.panel;

import com.geopista.app.AppContext;
import com.geopista.app.plantillas.ConstantesLocalGISPlantillas;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.Estructuras;
import com.geopista.app.inventario.JRInventario;

import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.licencias.CPlantilla;

import com.geopista.server.administradorCartografia.ACException;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.util.StringUtil;

import javax.swing.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import reso.jumpPlugIn.printLayoutPlugIn.DownloadFromServer;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;

import net.sf.jasperreports.engine.JasperCompileManager;


/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 17-oct-2006
 * Time: 10:30:57
 * To change this template use File | Settings | File Templates.
 */
public class InformesJPanel extends JPanel{
	
	private static final long serialVersionUID = -6176148551213054036L;

	private static final Log logger = LogFactory.getLog(InformesJPanel.class);
	
    private AppContext aplicacion;
    private javax.swing.JFrame desktop;
    private String locale;
    private String superpatron;
    private String patron;
    private InventarioClient inventarioClient;

    private FiltroJPanel filtroJPanel;
    private JPanel auxFiltroJPanel;
    private BienesJPanel bienesJPanel;
    private JPanel datosInformeJPanel;
    private JPanel auxDatosInformeJPanel;

    private String path = "";

    private Collection bienes;

    private ArrayList actionListeners= new ArrayList();

    /**
     * Método que genera el panel de informes
     */
    public InformesJPanel(JFrame desktop, String superpatron, String patron, String locale) throws Exception{
    	
    	   
        aplicacion= (AppContext) AppContext.getApplicationContext();
        this.desktop= desktop;
        this.locale= locale;
        this.superpatron= superpatron;
        this.patron= patron;
        inventarioClient= new InventarioClient(aplicacion.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
        		Constantes.INVENTARIO_SERVLET_NAME);

        initComponents();
        cargarPlantillas();
        renombrarComponentes();
    }

    private void initComponents() {

        plantillaJLabel = new javax.swing.JLabel();
        formatoJLabel = new javax.swing.JLabel();
        plantillaJCBox = new javax.swing.JComboBox();
        formatoEJCBox= new ComboBoxEstructuras(Estructuras.getListaFormatoInforme(), null, locale, false);

        bienesJPanel= new BienesJPanel(locale);
    	bienesJPanel.setPanelesVisibles(
    			superpatron==null || superpatron.equalsIgnoreCase(Const.SUPERPATRON_BIENES)
    			|| superpatron.equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO),
    			superpatron!=null && superpatron.equalsIgnoreCase(Const.SUPERPATRON_REVERTIBLES),
    			superpatron!=null && superpatron.equalsIgnoreCase(Const.SUPERPATRON_LOTES));
   
        buscarJButton= new javax.swing.JButton();
        buscarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarJButtonActionPerformed();
            }
        });

        cancelarJButton= new javax.swing.JButton();
        cancelarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarJButtonActionPerformed();
            }
        });


        informeJButton= new javax.swing.JButton();
        informeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                generarInformeJButtonActionPerformed();
            }
        });


        JPanel botoneraJPanel= new JPanel();
        botoneraJPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        botoneraJPanel.add(buscarJButton);

        auxFiltroJPanel= new JPanel();
        auxFiltroJPanel.setLayout(new java.awt.BorderLayout());
        filtroJPanel= new FiltroJPanel(desktop,superpatron,patron, locale);
        filtroJPanel.setVisibleAplicarFiltro(false);

        auxFiltroJPanel.add(filtroJPanel, BorderLayout.CENTER);
        auxFiltroJPanel.add(botoneraJPanel, BorderLayout.SOUTH);

        JPanel botonera2JPanel= new JPanel();
        botonera2JPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        botonera2JPanel.add(informeJButton);
        botonera2JPanel.add(cancelarJButton);

        datosInformeJPanel= new JPanel();
        datosInformeJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        datosInformeJPanel.add(plantillaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 160, 20));
        datosInformeJPanel.add(formatoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 25, 160, 20));

        datosInformeJPanel.add(plantillaJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 250, 20));
        datosInformeJPanel.add(formatoEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 25, 250, 20));


        auxDatosInformeJPanel= new JPanel();
        auxDatosInformeJPanel.setLayout(new java.awt.BorderLayout());
        auxDatosInformeJPanel.add(datosInformeJPanel, BorderLayout.CENTER);

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        add(auxFiltroJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, 200));
        add(bienesJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 750, 300));
        add(auxDatosInformeJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 500, 750, 80));
        add(botonera2JPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 580, 750, 50));


    }

	public Vector getPlantillas() {
		try {
			Vector vPlantillas = new Vector();

			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return (name.endsWith(".jrxml"));
				}
			};
			
			path = AppContext.getApplicationContext().getUserPreference(
					AppContext.PREFERENCES_DATA_PATH_KEY,
					AppContext.DEFAULT_DATA_PATH, true);
			String idAppType = (String)AppContext.getApplicationContext().getBlackboard().get(AppContext.idAppType);
			if (!idAppType.equals("")){
				path = path+idAppType+File.separator;
			}
			
            File dir = new File(path);
			if (dir.isDirectory()) {
				File[] children = dir.listFiles(filter);
				if (children == null) {
					// Either dir does not exist or is not a directory
				} else {
					for (int i = 0; i < children.length; i++) {
						// Get filename of file or directory
						File file = children[i];
                        // FRAN
                        String fname = file.getName();
                        long ftam = file.length();
                        FileInputStream fis = new FileInputStream(file);
                        byte data[] = new byte[(int)ftam];
                        fis.read(data);
                        String sdata = new String(data);
                      //  String sdef = sdata.replaceAll(CConstantesComando.PATRON_SUSTITUIR_BBDD, bdContext);
                        BufferedWriter bw = new BufferedWriter(new FileWriter(path+fname));
                      //  bw.write(sdef);
                        bw.write(sdata);
                        //FileOutputStream fos = new FileOutputStream(_path+dname);
                        //fos.write(sdef.getBytes());
                        //fos.flush();
                        //fos.close();
                        bw.flush();
                        bw.close();
                        //int pos = sdata.indexOf();
                        // FRAN
						CPlantilla plantilla = new CPlantilla(fname);//file.getName());
						vPlantillas.addElement(plantilla);
					}
				}
			}
			return vPlantillas;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			return new Vector();
		}
	}
    
    private void cargarPlantillas() throws Exception{
    	String path = ConstantesLocalGISPlantillas.PATH_INVENTARIO+File.separator+getSubDir();
    	AppContext.getApplicationContext().getBlackboard().put(AppContext.idAppType, path);  
		DownloadFromServer dfs = new DownloadFromServer();
		dfs.getServerPlantillas(ConstantesLocalGISPlantillas.EXTENSION_JRXML);	
		Vector plantillas = getPlantillas();
		if (plantillas != null) {
            Enumeration enumeration = plantillas.elements();
            while (enumeration.hasMoreElements()) {
                CPlantilla plantilla = (CPlantilla) enumeration.nextElement();
                logger.debug("plantilla.getFileName=" + plantilla.getFileName());
                plantillaJCBox.addItem(plantilla.getFileName());
            }
        }
    }

    private String getSubDir(){
    	if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_REVERTIBLES))
    		return "revertibles";
    	if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_LOTES))
    		return "lotes";
        if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS) ||
            patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
            return "inmuebles";
        }else if (patron.equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART) ||
                  patron.equalsIgnoreCase(Const.PATRON_BIENES_MUEBLES)){
            return "muebles";
        }else if (patron.equalsIgnoreCase(Const.PATRON_DERECHOS_REALES)){
            return "derechos_reales";
        }else if (patron.equalsIgnoreCase(Const.PATRON_VALOR_MOBILIARIO)){
            return "valores_mobiliarios";
        }else if (patron.equalsIgnoreCase(Const.PATRON_CREDITOS_DERECHOS_PERSONALES)){
            return "creditos_derechos";
        }else if (patron.equalsIgnoreCase(Const.PATRON_SEMOVIENTES)){
            return "semovientes";
        }else if (patron.equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_URBANAS) ||
                  patron.equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_RUSTICAS)){
            return "vias";
        }else if (patron.equalsIgnoreCase(Const.PATRON_VEHICULOS)){
            return "vehiculos";
        }

        return "";

    }



    private void buscarJButtonActionPerformed(){

    	AppContext app =(AppContext) AppContext.getApplicationContext();
    	final JFrame desktop= (JFrame)app.getMainFrame();
    	final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
    	progressDialog.setTitle("TaskMonitorDialog.Wait");
    	progressDialog.addComponentListener(new ComponentAdapter()
    	{
    		public void componentShown(ComponentEvent e) 
    		{
    			new Thread(new Runnable()
    			{
    				public void run()  //throws Exception
    				{
    					try{
    						progressDialog.report(aplicacion.getI18nString("inventario.app.tag3"));

    						Collection filtro= filtroJPanel.getFiltro();
                            if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_REVERTIBLES)){
                                bienes= inventarioClient.getBienesRevertibles(patron, null, filtro,null);
                                /** Cargamos la coleccion */
        						bienesJPanel.loadListaBienesRevertibles(bienes);
                            }
                            else if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_LOTES)){
                                bienes= inventarioClient.getLotes(patron, null, filtro,null);
                                /** Cargamos la coleccion */
        						bienesJPanel.loadListaLotes(bienes);
                            }else{
	                            if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS) ||
	    								patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
	    							bienes= inventarioClient.getBienesInventario(Const.ACTION_GET_INMUEBLES, superpatron, patron, null, filtro, null,null);
	    						}else if (patron.equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART) ||
	    								patron.equalsIgnoreCase(Const.PATRON_BIENES_MUEBLES)){
	    							bienes= inventarioClient.getBienesInventario(Const.ACTION_GET_MUEBLES, superpatron, patron, null, filtro, null,null);
	    						}else if (patron.equalsIgnoreCase(Const.PATRON_DERECHOS_REALES)){
	    							bienes= inventarioClient.getBienesInventario(Const.ACTION_GET_DERECHOS_REALES, superpatron, patron, null, filtro, null,null);
	    						}else if (patron.equalsIgnoreCase(Const.PATRON_VALOR_MOBILIARIO)){
	    							bienes= inventarioClient.getBienesInventario(Const.ACTION_GET_VALORES_MOBILIARIOS, superpatron, patron, null, filtro, null,null);
	    						}else if (patron.equalsIgnoreCase(Const.PATRON_CREDITOS_DERECHOS_PERSONALES)){
	    							bienes= inventarioClient.getBienesInventario(Const.ACTION_GET_CREDITOS_DERECHOS, superpatron, patron, null, filtro, null,null);
	    						}else if (patron.equalsIgnoreCase(Const.PATRON_SEMOVIENTES)){
	    							bienes= inventarioClient.getBienesInventario(Const.ACTION_GET_SEMOVIENTES, superpatron, patron, null, filtro, null,null);
	    						}else if (patron.equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_URBANAS) ||
	    								patron.equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_RUSTICAS)){
	    							bienes= inventarioClient.getBienesInventario(Const.ACTION_GET_VIAS, superpatron, patron, null, filtro, null,null);
	    						}else if (patron.equalsIgnoreCase(Const.PATRON_VEHICULOS)){
	    							bienes= inventarioClient.getBienesInventario(Const.ACTION_GET_VEHICULOS, superpatron, patron, null, filtro, null,null);
	    						}
	                            /** Cargamos la coleccion */
	    						bienesJPanel.loadListaBienes(bienes);
                            }	
    				 
    					}
    					catch(Exception e){
    						ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("inventario.SQLError.Titulo"),
    								aplicacion.getI18nString("inventario.SQLError.Aviso"), StringUtil.stackTrace(e));
    					}
    					finally
    					{
    						progressDialog.setVisible(false);
    					}
    				}
    			}).start();
    		}
    	});
    	GUIUtil.centreOnWindow(progressDialog);
    	progressDialog.setVisible(true);

    }

    private void cancelarJButtonActionPerformed(){
        fireActionPerformed();
    }


    private void generarInformeJButtonActionPerformed() {
        try{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if ((bienes != null) && (bienes.toArray().length > 0) ){
                 try {
                    if (plantillaJCBox.getSelectedIndex() == -1) return;

                    String report = ((String)plantillaJCBox.getSelectedItem());
    				

    				if (report != null && !report.equals("")){          
   						 String jrxmlfile = path + report;

	                    /** Abrimos el dialogo showSaveDialog en el caso de que el formato no sea PREVIEW **/
	                    boolean previsualizar= (new Integer(formatoEJCBox.getSelectedPatron()).intValue()==Constantes.formatoPREVIEW)?true:false;
	                    if (!previsualizar){
	                        File file= showSaveFileDialog(formatoEJCBox.getSelectedPatron());
	                        if ((file == null)) return;
	
	                        String nombreInforme= file.getName();
	                        /** quitamos la extension que haya puesto el usuario */
	                        int index= nombreInforme.indexOf(".");
	                        if (index != -1){
	                            nombreInforme= nombreInforme.substring(0, index);
	                        }
	                        /** nos quedamos sólo con el path, sin el nombre del fichero */
	                        String pathDestino= file.getAbsolutePath();
	                        index= pathDestino.indexOf(nombreInforme);
	                        if (index != -1){
	                            pathDestino= pathDestino.substring(0, index);
	                        }
	                        /** al nombre del fichero le añadimos la extension correspondiente al formato del fichero seleccionado por el usuario */
	                        if (formatoEJCBox.getSelectedPatron().equalsIgnoreCase(""+Constantes.formatoPDF)){
	                            nombreInforme+=".pdf";
	                        }else if (formatoEJCBox.getSelectedPatron().equalsIgnoreCase(""+Constantes.formatoHTML)){
	                            nombreInforme+=".html";
	                        }else if (formatoEJCBox.getSelectedPatron().equalsIgnoreCase(""+Constantes.formatoXML)){
	                            nombreInforme+=".xml";
	                        }
	                        /** añadimos al path, el nombre del fichero con extension */
	                        pathDestino+= nombreInforme;
	                        /** Si ya existe, borramos el fichero de datos */
	                        if (file.exists()) {
	                          file.delete();
	                        }
	                        if (jrxmlfile.indexOf("BD")>=0) 
	                        	JRInventario.generarReportConnectDB(jrxmlfile, pathDestino, formatoEJCBox.getSelectedPatron(), bienes, desktop, locale);
	                        else
	                        	JRInventario.generarReport(jrxmlfile, pathDestino, formatoEJCBox.getSelectedPatron(), bienes, desktop, locale);
	                    }else{
	                        /** Previsualizamos el documento */
	                    	  if (jrxmlfile.indexOf("BD")>=0) 
	                    		  JRInventario.generarReportConnectDB(jrxmlfile, null, ""+Constantes.formatoPREVIEW, bienes, desktop, locale);
	                    	  else
	                    		  JRInventario.generarReport(jrxmlfile, null, ""+Constantes.formatoPREVIEW, bienes, desktop, locale);
	                      	
	                    }
	                    if ((new Integer(formatoEJCBox.getSelectedPatron()).intValue() != Constantes.formatoPREVIEW)){
	                    	JOptionPane.showMessageDialog(this, aplicacion.getI18nString("inventario.informes.tag11"), aplicacion.getI18nString("inventario.informes.tag10"), JOptionPane.INFORMATION_MESSAGE);
	                    }
    				}
                }catch (Exception e) {
                    
                    ErrorDialog.show(this,aplicacion.getI18nString("inventario.informes.tag9"), aplicacion.getI18nString("inventario.informes.tag10"), StringUtil.stackTrace(e));
                    logger.error("Error al genera el informe",e);
                }
        
            }else{
                JOptionPane.showMessageDialog(this, aplicacion.getI18nString("inventario.informes.tag13"), aplicacion.getI18nString("inventario.informes.tag10"), JOptionPane.INFORMATION_MESSAGE);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, aplicacion.getI18nString("inventario.informes.tag9"), aplicacion.getI18nString("inventario.informes.tag10"), JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }

    public File showSaveFileDialog(String formato){
        JFileChooser chooser = new JFileChooser();
        com.geopista.app.utilidades.GeoPistaFileFilter filter= new com.geopista.app.utilidades.GeoPistaFileFilter();

        if (formato.equalsIgnoreCase(""+Constantes.formatoPDF)){
            filter.addExtension("pdf");
            filter.setDescription("PDF");
        }else if (formato.equalsIgnoreCase(""+Constantes.formatoHTML)){
            filter.addExtension("html");
            filter.setDescription("HTML");
        }else if (formato.equalsIgnoreCase(""+Constantes.formatoXML)){
            filter.addExtension("xml");
            filter.setDescription("XML");
        }
        chooser.setFileFilter(filter);

        /** Permite multiples selecciones */
        chooser.setMultiSelectionEnabled(false);

        int returnVal = chooser.showSaveDialog(desktop);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
           File selectedFile= chooser.getSelectedFile();
           return selectedFile;
        }
        return null;

    }


    private void renombrarComponentes(){
        try{auxFiltroJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.informes.tag6")));}catch(Exception e){}
        try{bienesJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.informes.tag7")));}catch(Exception e){}
        try{auxDatosInformeJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.informes.tag8")));}catch(Exception e){}
        try{buscarJButton.setText(aplicacion.getI18nString("inventario.informes.tag2"));}catch(Exception e){}
        try{informeJButton.setText(aplicacion.getI18nString("inventario.informes.tag3"));}catch(Exception e){}
        try{plantillaJLabel.setText(aplicacion.getI18nString("inventario.informes.tag4"));}catch(Exception e){}
        try{formatoJLabel.setText(aplicacion.getI18nString("inventario.informes.tag5"));}catch(Exception e){}
        try{cancelarJButton.setText(aplicacion.getI18nString("inventario.informes.tag12"));}catch(Exception e){}

    }

    public void actionPerformed(ActionEvent e){
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


    private javax.swing.JButton buscarJButton;
    private javax.swing.JButton cancelarJButton;
    private javax.swing.JButton informeJButton;
    private javax.swing.JLabel plantillaJLabel;
    private javax.swing.JLabel formatoJLabel;
    private javax.swing.JComboBox plantillaJCBox;
    private ComboBoxEstructuras formatoEJCBox ;



}
