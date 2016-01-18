/**
 * FichasFilterPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.panels;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.InitEIEL;
import com.geopista.app.eiel.JREIEL;
import com.geopista.app.eiel.beans.filter.LCGNucleoEIEL;
import com.geopista.app.eiel.reports.UtilsReport;
import com.geopista.app.eiel.utils.NodoComparatorByDenominacion;
import com.geopista.app.filter.LCGFilter;
import com.geopista.app.plantillas.ConstantesLocalGISPlantillas;
import com.geopista.app.plantillas.LocalGISPlantillasClient;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.server.administradorCartografia.ACException;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;

public class FichasFilterPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private AppContext aplicacion;
    private javax.swing.JFrame desktop;
    private String locale;
    private String tipo;

    private LocalGISPlantillasClient plantillasClient;    
    
    private JLabel nucleoJLabel;
    private JLabel seleccionJLabel;
	private JComboBox nucleoJCBox;
    private JLabel plantillaJLabel;
	private JLabel formatoJLabel;

	private JLabel imagenesJLabel;
	private JLabel wmsJLabel;
	private JComboBox imagenesJCBox;
	private JComboBox wmsJCBox;
	
	private JComboBox plantillaJCBox;
	private ComboBoxEstructuras formatoEJCBox;
	private JPanel datosInformeJPanel;
	private JPanel datosSeleccionadosJPanel;
	private JPanel contenedorCentralSeleccionPanel;

	private JPanel contenedorInferiorInformePanel;

	private JButton cancelarJButton;
	private JButton informeJButton;
	private JPanel contenedorSuperiorPestañasPanel;

	private ArrayList<ActionListener> actionListeners= new ArrayList<ActionListener>();

	private Object[] imagenes;
	private Object[] plantillas;
	private Object[] subreports;

	private FiltroJPanel filtroJPanel;

	private HashMap<String,String> listaFiltros;

	private boolean disponeFiltro;

//	private String traduccionSelected;
	
	
	private static final Log logger = LogFactory.getLog(FichasFilterPanel.class);
	
	
	public FichasFilterPanel(JFrame desktop, String tipo, HashMap<String,String> listaFiltros, String locale, boolean disponefiltro) throws Exception{
		aplicacion= (AppContext) AppContext.getApplicationContext();
        this.desktop= desktop;
        this.locale= locale;
        this.tipo= tipo;
        this.listaFiltros=listaFiltros;
        this.disponeFiltro=disponeFiltro;
  
        
        plantillasClient= new LocalGISPlantillasClient(aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) + "/PlantillasServlet");
        initComponents();
        if (tipo.equals(ConstantesLocalGISEIEL.PATRON_FICHA_MUNICIPAL))
        	cargarPlantillas(tipo,null);
        else	
        	cargarPlantillas(null,tipo);
        
        renombrarComponentes();
	}

	private void initComponents(){		
		
		
		/*******Panel del filtro*******/
		
		contenedorSuperiorPestañasPanel= new JPanel();
        contenedorSuperiorPestañasPanel.setLayout(new java.awt.BorderLayout());
        
        //Panel Principal de Filtro (Pestañas)
        filtroJPanel= new FiltroJPanel(desktop,tipo,listaFiltros,locale);
        
        contenedorSuperiorPestañasPanel.add(filtroJPanel, BorderLayout.CENTER);
		
        
        //*********************************
        //Parte del medio donde aparece el nucleo y si vienen ya seleccionados los elementos
        //*********************************
        nucleoJLabel = new javax.swing.JLabel();
        nucleoJCBox = new javax.swing.JComboBox();
        seleccionJLabel = new javax.swing.JLabel();
        
       
        
        
        getNucleosEIEL();                      
        nucleoJCBox = new ComboBoxEstructuras(Estructuras.getListaTipos(), null, locale, false);

        
        
        if (listaFiltros.size()>0){
        	seleccionJLabel.setText("Se ha aplicado un filtro con los elementos seleccionados por el usuario");
        	nucleoJCBox.setEnabled(false);
        }
        else{
        	if (disponeFiltro)
        		seleccionJLabel.setText("No se ha seleccionado ningun elemento de la eiel especifico");
        	else
        		seleccionJLabel.setText("El elemento seleccionado no dispone de datos alfanumericos para poder filtrar.");
        }
        
        datosSeleccionadosJPanel= new JPanel();
        datosSeleccionadosJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        datosSeleccionadosJPanel.add(nucleoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 160, 20));
        datosSeleccionadosJPanel.add(nucleoJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 250, 20));
        datosSeleccionadosJPanel.add(seleccionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 25, 400, 20));
        
        
        contenedorCentralSeleccionPanel= new JPanel();
        contenedorCentralSeleccionPanel.setLayout(new java.awt.BorderLayout());
        contenedorCentralSeleccionPanel.add(datosSeleccionadosJPanel, BorderLayout.CENTER);

        //***************************
        //PARTE DE ABAJO DEL FILTRO
        //***************************

		plantillaJLabel = new javax.swing.JLabel();
        formatoJLabel = new javax.swing.JLabel();
        plantillaJCBox = new javax.swing.JComboBox();
        plantillaJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //System.out.println("ActionPerformed");
            	 if (plantillaJCBox.getSelectedIndex() != -1){
 			    	Object[] plantillaSelected= (Object[])plantillas[plantillaJCBox.getSelectedIndex()];
 			    	if (((String)plantillaSelected[0]).contains("FichaMunicipal")){
 			    		filtroJPanel.habilitarFiltros(false);
 			    	}
 			    	else{
 			    		filtroJPanel.habilitarFiltros(true);
 			    	}
 			    }
            }
        });

    	imagenesJLabel = new javax.swing.JLabel();
    	String []seleccionImagenes={"No","Si"};
        imagenesJCBox = new javax.swing.JComboBox(seleccionImagenes);

        wmsJLabel = new javax.swing.JLabel();
    	String []cargarWMS={"No","Si"};
    	wmsJCBox = new javax.swing.JComboBox(cargarWMS);

        
        Estructuras.cargarEstructura("Formato Informe",true);        
        formatoEJCBox = new ComboBoxEstructuras(Estructuras.getListaTipos(), null, locale, false);
        formatoEJCBox.setSelectedIndex(1);
        
        datosInformeJPanel= new JPanel();
        datosInformeJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        datosInformeJPanel.add(plantillaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 160, 20));
        datosInformeJPanel.add(formatoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 25, 160, 20));
        datosInformeJPanel.add(imagenesJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 160, 20));
        datosInformeJPanel.add(wmsJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 75, 160, 20));
        datosInformeJPanel.add(plantillaJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 250, 20));
        datosInformeJPanel.add(formatoEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 25, 250, 20));
        datosInformeJPanel.add(imagenesJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 250, 20));
        datosInformeJPanel.add(wmsJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 75, 250, 20));
        

        contenedorInferiorInformePanel= new JPanel();
        contenedorInferiorInformePanel.setLayout(new java.awt.BorderLayout());
        contenedorInferiorInformePanel.add(datosInformeJPanel, BorderLayout.CENTER);
        
        /*******Botones de Generar Informe y Salir*******/
        
        cancelarJButton= new javax.swing.JButton();
        cancelarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarJButtonActionPerformed();
            }
        });
        informeJButton= new javax.swing.JButton();
        informeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
            	generarInformeJButtonActionPerformed(tipo);
            }
        });
        
        JPanel botoneraJPanel= new JPanel();
        botoneraJPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        botoneraJPanel.add(informeJButton);
        botoneraJPanel.add(cancelarJButton);
        
        /*******Layout general*******/
        
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        if (!tipo.equals(ConstantesLocalGISEIEL.PATRON_FICHA_MUNICIPAL)){
        	add(contenedorSuperiorPestañasPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, 240));
        	add(contenedorCentralSeleccionPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 750, 80));
        	add(contenedorInferiorInformePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 750, 130));
        	add(botoneraJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 450, 300, 50));
        }
        else{
        	add(contenedorCentralSeleccionPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, 80));
        	add(contenedorInferiorInformePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 750, 150));
        	add(botoneraJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 240, 300, 50));
        	//add(botoneraJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 750, 50));
        }
        
	}
	
	private void cargarPlantillas(String filtro,String patron) throws Exception{
		ArrayList<?> patrones=filtroJPanel.getListaPatrones();
//    	Collection<?> c = eielClient.getPlantillas(ConstantesLocalGISEIEL.PATH_PLANTILLAS_EIEL + File.separator + getSubdir(),filtro,patron,patrones);
		
		String idAppType = (String)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.idAppType);
		Collection<?> c = plantillasClient.getPlantillas(ConstantesLocalGISPlantillas.PATH_PLANTILLAS + File.separator + idAppType,filtro,patron,patrones);
		
        if (c == null) return;
        Object[] objs= c.toArray();

        imagenes= ((ArrayList<?>)objs[0]).toArray();
        plantillas= ((ArrayList<?>)objs[1]).toArray();
        subreports= ((ArrayList<?>)objs[2]).toArray();
        for (int i=0; i<plantillas.length; i++){
             plantillaJCBox.addItem((String)((Object[])plantillas[i])[0]);
        }
    }
	
	private String getSubdir() {
		return "";
	}

	
	private void renombrarComponentes(){
        try{contenedorSuperiorPestañasPanel.setBorder(new javax.swing.border.TitledBorder("Filtro de Busqueda"));}catch(Exception e){}
        try{contenedorCentralSeleccionPanel.setBorder(new javax.swing.border.TitledBorder("Seleccion Espacial"));}catch(Exception e){}
        try{contenedorInferiorInformePanel.setBorder(new javax.swing.border.TitledBorder("Datos del Informe"));}catch(Exception e){}
        try{informeJButton.setText("Generar Informe");}catch(Exception e){}
        try{nucleoJLabel.setText("Nucleo:");}catch(Exception e){}
        try{plantillaJLabel.setText("Plantilla:");}catch(Exception e){}
        try{imagenesJLabel.setText("Imprimir Imagenes:");}catch(Exception e){}
        try{wmsJLabel.setText("Usar WMS Externos:");}catch(Exception e){}
        try{formatoJLabel.setText("Formato:");}catch(Exception e){}
        try{cancelarJButton.setText("Salir");}catch(Exception e){}
	}
	
	private void cancelarJButtonActionPerformed(){
        fireActionPerformed();
    }
	
    private void generarInformeJButtonActionPerformed(String nodo) {
        try{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            //**************************************************************************************
            //En este metodo se devuelve la lista de filtros (Sentencias SQL) de cada nodo (epigrafe)
            //Llegaria un ArrayList con filtro_captaciones,filtro_rd,.....
            //**************************************************************************************
            String nucleoSeleccionado=((DomainNode)nucleoJCBox.getSelectedItem()).getIdNode();
            
            String nucleoParaImprimir=((DomainNode)nucleoJCBox.getSelectedItem()).toString();
            
            String imprimirImagenes=(String)imagenesJCBox.getSelectedItem();
            
            String usarWMSExternos=(String)wmsJCBox.getSelectedItem();
            
            
    		String[] valores=nucleoSeleccionado.split("_");
            String codEntidad=valores[0];
            String codNucleo=valores[1];

            HashMap<String,String> listaFiltros=filtroJPanel.getFiltros(codEntidad,codNucleo);
                     
			 try {
			    if (plantillaJCBox.getSelectedIndex() == -1) return;
			    
			  
			   
//			    guardarSubreports();
			
			    Object[] plantillaSelected= (Object[])plantillas[plantillaJCBox.getSelectedIndex()];
			    
			    //if (((String)plantillaSelected[0]).contains("FichaMunicipal")){
			    if (((String)plantillaSelected[0]).contains("_A3")){
			    	 if (nucleoParaImprimir.contains("Todos")){
			    		 JOptionPane.showMessageDialog(this, "Debe seleccionar un nucleo para el Informe en A3",aplicacion.getI18nString("inventario.informes.tag10"), JOptionPane.INFORMATION_MESSAGE);
			    		 return;
			    	 }
			    }
			    guardarImagenes();
			    
			    String jrxmlfile= guardarPlantilla(plantillaSelected);
			    
			    //Obtención, copia y compilación de los subreports de la plantilla
			    ArrayList<String> listaSubReports = new ArrayList<String>();
			    JasperDesign jasperDesign = JRXmlLoader.load(jrxmlfile);
			    UtilsReport.obtenerSubreports(jasperDesign, listaSubReports,subreports,false);
			    
			    
			    /** Abrimos el dialogo showSaveDialog en el caso de que el formato no sea PREVIEW **/
			    boolean previsualizar = (new Integer(formatoEJCBox.getSelectedPatron()).intValue()==ConstantesLocalGISEIEL.formatoPREVIEW)?true:false;
			    
			    boolean resultado=true;
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
			        if (formatoEJCBox.getSelectedPatron().equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoPDF)){
			            nombreInforme+=".pdf";
			        }else if (formatoEJCBox.getSelectedPatron().equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoHTML)){
			            nombreInforme+=".html";
			        }else if (formatoEJCBox.getSelectedPatron().equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoXML)){
			            nombreInforme+=".xml";
			        }
			        /** añadimos al path, el nombre del fichero con extension */
			        pathDestino+= nombreInforme;
			        /** Si ya existe, borramos el fichero de datos */
			        if (file.exists()) {
			          file.delete();
			        }
			        resultado=JREIEL.generarReportConnectDB(jrxmlfile, pathDestino, 
			    			formatoEJCBox.getSelectedPatron(), listaFiltros, nodo,desktop,
			    			locale,codEntidad,codNucleo,nucleoParaImprimir,imprimirImagenes,usarWMSExternos);
			    }else{
			        /** Previsualizamos el documento */
			    	resultado=JREIEL.generarReportConnectDB(jrxmlfile, null, ""+ConstantesLocalGISEIEL.formatoPREVIEW, 
							  listaFiltros, nodo,desktop, locale,codEntidad,codNucleo,nucleoParaImprimir,imprimirImagenes,usarWMSExternos);
			      	
			    }
			    if (resultado){
				    if ((new Integer(formatoEJCBox.getSelectedPatron()).intValue() != ConstantesLocalGISEIEL.formatoPREVIEW)){
				    	JOptionPane.showMessageDialog(this, aplicacion.getI18nString("inventario.informes.tag11"), aplicacion.getI18nString("inventario.informes.tag10"), JOptionPane.INFORMATION_MESSAGE);
				    }
			    }
				else{
					JOptionPane.showMessageDialog(this, "Informe cancelado", aplicacion.getI18nString("inventario.informes.tag10"), JOptionPane.INFORMATION_MESSAGE);
				}
				    	
			}catch (Exception e) {
			    
			    ErrorDialog.show(this,aplicacion.getI18nString("inventario.informes.tag9"), aplicacion.getI18nString("inventario.informes.tag10"), StringUtil.stackTrace(e));
			    logger.error("Error al genera el informe",e);
			}
        
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, aplicacion.getI18nString("inventario.informes.tag9"), aplicacion.getI18nString("inventario.informes.tag10"), JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }
   

	
    public static String guardarPlantilla(Object[] plantilla) throws Exception{

        if ((byte[])plantilla[1] == null) throw new ACException("Plantilla vacia");
        String path= ConstantesLocalGISEIEL.PATH_PLANTILLAS_EIEL + File.separator;

        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        logger.debug("Guardando la plantilla:"+new File(path).getAbsolutePath()+(String)plantilla[0]);

        FileOutputStream out = new FileOutputStream(path + (String)plantilla[0]);
        out.write((byte[])plantilla[1]);
        out.close();

        return path + (String)plantilla[0];
    }
	

	

    private void guardarImagenes() throws Exception{

        if (imagenes == null) return;
        String path= ConstantesLocalGISEIEL.PATH_PLANTILLAS_EIEL + File.separator + "img" + File.separator;
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



    /**
     * Copia y compila TODOS los subinformes que existan
     * @deprecated Se ha sustituido por obtenerSubreports(), que copia y compila SOLO los necesarios
     * @throws Exception
     */
    @SuppressWarnings("unused")
	private void guardarSubreports() throws Exception {
        if (subreports == null) return;
        String path= ConstantesLocalGISEIEL.PATH_PLANTILLAS_EIEL + File.separator+"subreports"+File.separator;
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
            JasperCompileManager.compileReportToFile((new File(path)).getAbsolutePath() + File.separator+(String)subreport[0]);
        }
    }

    public File showSaveFileDialog(String formato){
        JFileChooser chooser = new JFileChooser();
        com.geopista.app.utilidades.GeoPistaFileFilter filter= new com.geopista.app.utilidades.GeoPistaFileFilter();

        if (formato.equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoPDF)){
            filter.addExtension("pdf");
            filter.setDescription("PDF");
        }else if (formato.equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoHTML)){
            filter.addExtension("html");
            filter.setDescription("HTML");
        }else if (formato.equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoXML)){
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
	
	public void actionPerformed(ActionEvent e){
    }

    public void addActionListener(ActionListener l) {
        this.actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        this.actionListeners.remove(l);
    }

    private void fireActionPerformed() {
        for (Iterator<ActionListener> i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(new ActionEvent(this, 0, null));
        }
    }	
    
    private void getNucleosEIEL(){
    	
    	boolean useCache=false;
    	
    	boolean existe=false;
    	if (useCache)
    		if (Estructuras.getEstructuraFromCache(ConstantesLocalGISEIEL.ESTRUCTURA_NUCLEOS)!=null)
    			existe=true;
    		
    	if (!existe){
			Collection<Object> c=null;
	        try {
				c = InitEIEL.clienteLocalGISEIEL.getNucleosEIELEncuestables();
			} catch (Exception e) {
				e.printStackTrace();
			}
	
	        //Los ordenamos alfabeticamente		
			Object[] arrayNodos= c.toArray();
	    	Arrays.sort(arrayNodos,new NodoComparatorByDenominacion());
	
	    	//Los devolvemos ordenados
	    	c.clear();
			for (int i = 0; i < arrayNodos.length; ++i) {
				LCGNucleoEIEL nucleoEIEL = (LCGNucleoEIEL) arrayNodos[i];
				c.add(nucleoEIEL);
			}
			
			LCGFilter.loadEstructuraNucleos(c);
    	}
    	 Estructuras.cargarEstructura(ConstantesLocalGISEIEL.ESTRUCTURA_NUCLEOS,true);    
       
    }
    
   
    
}
