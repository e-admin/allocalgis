/**
 * InformesJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios.panel;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.sf.jasperreports.engine.JasperCompileManager;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.cementerios.Constantes;
import com.geopista.app.cementerios.Estructuras;
import com.geopista.app.cementerios.JRCementerio;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.global.ServletConstants;
import com.geopista.protocol.cementerios.CementerioClient;
import com.geopista.protocol.cementerios.Const;
import com.geopista.server.administradorCartografia.ACException;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


public class InformesJPanel extends JPanel{
    private AppContext aplicacion;
    private javax.swing.JFrame desktop;
    private String locale;
    private String superpatron;
    private String patron;
    private CementerioClient cementeriosClient;
    private int idCementerio;

    private FiltroJPanel filtroJPanel;
    private JPanel auxFiltroJPanel;
    private ElemCementeriosJPanel elemCementeriosJPanel;
    
//    private BienesJPanel bienesJPanel;
    private JPanel datosInformeJPanel;
    private JPanel auxDatosInformeJPanel;


    private Collection elementosCemen;
    private Object[] plantillas;
    private Object[] imagenes;
    private Object[] subreports;

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
//        inventarioClient= new InventarioClient(aplicacion.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
//                InventarioManagerPlugIn.INVENTARIO_SERVLET_NAME);

        cementeriosClient = new CementerioClient(aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) +
        		ServletConstants.CEMENTERIO_SERVLET_NAME);
        
        initComponents();
        cargarPlantillas();
        renombrarComponentes();
    }

    private void initComponents() {

        plantillaJLabel = new javax.swing.JLabel();
        formatoJLabel = new javax.swing.JLabel();
        plantillaJCBox = new javax.swing.JComboBox();
        
        formatoEJCBox= new ComboBoxEstructuras(Estructuras.getListaFormatoInforme(), null, locale, false);

        elemCementeriosJPanel = new ElemCementeriosJPanel(locale);

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
//        filtroJPanel= new FiltroJPanel(desktop,superpatron, patron, locale);
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
        add(elemCementeriosJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 750, 300));
        add(auxDatosInformeJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 500, 750, 80));
        add(botonera2JPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 580, 750, 50));


    }

    private void cargarPlantillas() throws Exception{
    	
        Collection c= cementeriosClient.getPlantillasJR(Constantes.PATH_PLANTILLAS_INVENTARIO + "/" + getSubDir());
        
        if (c == null) return;
        Object[] objs= c.toArray();

        imagenes= ((ArrayList)objs[0]).toArray();
        plantillas= ((ArrayList)objs[1]).toArray();
        subreports= ((ArrayList)objs[2]).toArray();

        for (int i=0; i<plantillas.length; i++){
             plantillaJCBox.addItem((String)((Object[])plantillas[i])[0]);
        }
    }

    private String getSubDir(){
//        if (patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS) ||
//            patron.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
//            return "inmuebles";
//        }else if (patron.equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART) ||
//                  patron.equalsIgnoreCase(Const.PATRON_BIENES_MUEBLES)){
//            return "muebles";
//        }else if (patron.equalsIgnoreCase(Const.PATRON_DERECHOS_REALES)){
//            return "derechos_reales";
//        }else if (patron.equalsIgnoreCase(Const.PATRON_VALOR_MOBILIARIO)){
//            return "valores_mobiliarios";
//        }else if (patron.equalsIgnoreCase(Const.PATRON_CREDITOS_DERECHOS_PERSONALES)){
//            return "creditos_derechos";
//        }else if (patron.equalsIgnoreCase(Const.PATRON_SEMOVIENTES)){
//            return "semovientes";
//        }else if (patron.equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_URBANAS) ||
//                  patron.equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_RUSTICAS)){
//            return "vias";
//        }else if (patron.equalsIgnoreCase(Const.PATRON_VEHICULOS)){
//            return "vehiculos";
//        }

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
    						progressDialog.report(aplicacion.getI18nString("cementerio.app.tag3"));
    						
    					     idCementerio = (Integer) aplicacion.getBlackboard().get(Constantes.ID_CEMENTERIO);
    					       
                            Collection subc = null; 

                            Collection filtro= filtroJPanel.getFiltro();

    						if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GELEMENTOS)){
                                if (patron.equalsIgnoreCase(Const.PATRON_BLOQUE)){
                                	elementosCemen = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, superpatron, patron, null, filtro, null, idCementerio);
                                }
                                else if (patron.equalsIgnoreCase(Const.PATRON_UENTERRAMIENTO)){
                                	elementosCemen= cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, superpatron, patron, null, filtro, null, idCementerio);
                                }
                                
                            }else if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)){
                                if (patron.equalsIgnoreCase(Const.PATRON_CONCESION)){
                            		//al pinchar en concesion recuperar las unidades de enterramiento
                                    subc= cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, superpatron, patron, null, filtro, null, idCementerio);
                                    elementosCemen = cementeriosClient.getElementosCementerio(Const.ACTION_GET_CONCESIONES,superpatron, patron, null, filtro, null, idCementerio);
                                }else if (patron.equalsIgnoreCase(Const.PATRON_TITULAR)){
                                	elementosCemen = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, superpatron, patron, null, filtro, null, idCementerio);
                                }
                                
                            }else if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GINTERVENCIONES)){
                                if (patron.equalsIgnoreCase(Const.PATRON_INTERVENCION)){
                            		//al pinchar en concesion recuperar las unidades de enterramiento
                                	elementosCemen= cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, superpatron, patron, null, filtro, null, idCementerio);
                                }    
                            }else if (superpatron.equalsIgnoreCase(Const.SUPERPATRON_GDIFUNTOS)){
                                if (patron.equalsIgnoreCase(Const.PATRON_DEFUNCION)){
                                	elementosCemen= cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, superpatron, patron, null, filtro, null, idCementerio);
                                }    
                            }

    						/** Cargamos la coleccion */
    						elemCementeriosJPanel.loadListaElemCementerios(elementosCemen);
    						
    					}
    					catch(Exception e){
    						ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"),
    								aplicacion.getI18nString("cementerio.SQLError.Aviso"), StringUtil.stackTrace(e));
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

            if ((elementosCemen != null) && (elementosCemen.toArray().length > 0) ){
                boolean error= false;
                try {
                    if (plantillaJCBox.getSelectedIndex() == -1) return;
                    guardarImagenes();
                    guardarSubreports();

                    Object[] plantillaSelected= (Object[])plantillas[plantillaJCBox.getSelectedIndex()];
                    String jrxmlfile= guardarPlantilla(plantillaSelected);
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

                        JRCementerio.generarReport(jrxmlfile, pathDestino, formatoEJCBox.getSelectedPatron(), elementosCemen, desktop, locale);
                    }else{
                        /** Previsualizamos el documento */
                        JRCementerio.generarReport(jrxmlfile, null, ""+Constantes.formatoPREVIEW, elementosCemen, desktop, locale);
                    }

                }catch (Exception e) {
                    error= true;
                    ErrorDialog.show(this,aplicacion.getI18nString("cementerio.informes.tag9"), aplicacion.getI18nString("cementerio.informes.tag10"), StringUtil.stackTrace(e));
                }

                if ((!error) && (new Integer(formatoEJCBox.getSelectedPatron()).intValue() != Constantes.formatoPREVIEW)){
                    JOptionPane.showMessageDialog(this, aplicacion.getI18nString("cementerio.informes.tag11"), aplicacion.getI18nString("cementerio.informes.tag10"), JOptionPane.INFORMATION_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(this, aplicacion.getI18nString("cementerio.informes.tag13"), aplicacion.getI18nString("cementerio.informes.tag10"), JOptionPane.INFORMATION_MESSAGE);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, aplicacion.getI18nString("cementerio.informes.tag9"), aplicacion.getI18nString("cementerio.informes.tag10"), JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }

    private String guardarPlantilla(Object[] plantilla) throws Exception{

        if ((byte[])plantilla[1] == null) throw new ACException("Plantilla vacia");
        String path= Constantes.PATH_PLANTILLAS_INVENTARIO + File.separator;

        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }

        FileOutputStream out = new FileOutputStream(path + (String)plantilla[0]);
        out.write((byte[])plantilla[1]);
        out.close();

        return path + (String)plantilla[0];
    }

    private void guardarImagenes() throws Exception{

        if (imagenes == null) return;
        String path= Constantes.PATH_PLANTILLAS_INVENTARIO + File.separator + "img" + File.separator;
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }

        for (int i=0; i<imagenes.length; i++){
            Object[] imagen= (Object[])imagenes[i];
            if ((byte[])imagen[1] == null) continue;

            FileOutputStream out = new FileOutputStream(path + (String)imagen[0]);
            out.write((byte[])imagen[1]);
            out.close();
        }
    }


    private void guardarSubreports() throws Exception{

        if (subreports == null) return;
        String path= Constantes.PATH_PLANTILLAS_INVENTARIO + File.separator;
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }

        for (int i=0; i<subreports.length; i++){
            Object[] subreport= (Object[])subreports[i];
            if ((byte[])subreport[1] == null) continue;

            FileOutputStream out = new FileOutputStream(path + (String)subreport[0]);
            out.write((byte[])subreport[1]);
            out.close();

            /** Compilamos los subreports */
            JasperCompileManager.compileReportToFile(path + (String)subreport[0]);
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
        try{auxFiltroJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("cementerio.informes.tag6")));}catch(Exception e){}
        try{elemCementeriosJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("cementerio.informes.tag7")));}catch(Exception e){}
        try{auxDatosInformeJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("cementerio.informes.tag8")));}catch(Exception e){}
        try{buscarJButton.setText(aplicacion.getI18nString("cementerio.informes.tag2"));}catch(Exception e){}
        try{informeJButton.setText(aplicacion.getI18nString("cementerio.informes.tag3"));}catch(Exception e){}
        try{plantillaJLabel.setText(aplicacion.getI18nString("cementerio.informes.tag4"));}catch(Exception e){}
        try{formatoJLabel.setText(aplicacion.getI18nString("cementerio.informes.tag5"));}catch(Exception e){}
        try{cancelarJButton.setText(aplicacion.getI18nString("cementerio.informes.tag12"));}catch(Exception e){}

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
