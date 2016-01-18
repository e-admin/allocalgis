/**
 * ExportCuadrosMPTDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.dialogs;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.JREIEL;
import com.geopista.app.eiel.LocalGISEIELClient;
import com.geopista.app.eiel.panels.ExportCuadrosMPTPanel;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.plugin.Constantes;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.database.cuadros.CuadrosMPTBean;
import com.geopista.server.database.cuadros.Poblamiento_bean;
import com.geopista.util.GeopistaUtil;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;



public class ExportCuadrosMPTDialog extends JDialog{
	
	static Logger logger = Logger.getLogger(ExportCuadrosMPTDialog.class);
	private ExportCuadrosMPTPanel exportCuadrosMPTPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private JFileChooser fileChooser; 
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    public static final int DIM_X = 600;
    public static final int DIM_Y = 400;
        
    private Object[] imagenes;
	private Object[] plantillas;
    
    public LocalGISEIELClient clienteLocalGISEIEL;
    
    private OKCancelPanel getOkCancelPanel(){
        if (_okCancelPanel == null){
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent e){
                	            		                    
                if (_okCancelPanel.wasOKPressed()){  

                	final String anioEncuesta = exportCuadrosMPTPanel.getAnioEncuesta();
                	final ArrayList lstCuadros = exportCuadrosMPTPanel.getLstCuadrosSelected();
                	final ArrayList lstMunicipiosNucleos = exportCuadrosMPTPanel.getLstMunicipiosSelected();
                	final boolean radioSelectedMunicipio = exportCuadrosMPTPanel.getRadioSelected();

                	if(exportCuadrosMPTPanel.getLstMunicipiosSelected().size() != 0 &&  
                			exportCuadrosMPTPanel.getLstCuadrosSelected().size() != 0  &&
	                			((anioEncuesta!=null && !anioEncuesta.equals("") && anioEncuesta.length() == 4))){
                		
                		getFileChooser();
                		dispose();
	                	if (abrirJFileChooser() == JFileChooser.APPROVE_OPTION){
	                		
	                		final String path=getDirectorySelected();
	                		
	                		exportCuadrosMPTPanel.setVisible(false);
	    					final TaskMonitorDialog progressDialog= new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
	    					progressDialog.setTitle("TaskMonitorDialog.Wait");
	    					progressDialog.addComponentListener(new ComponentAdapter()
	    					{
	    						public void componentShown(ComponentEvent e)
	    						{
	    							new Thread(new Runnable()
	    							{
	    								public void run()
	    								{
	    									
	    									try
	    									{     
	    										dispose();
  										
	    										String fase = anioEncuesta;
	    										
	    										if(path != null && !path.equals("")){
												
	    											String formato = ""+ConstantesLocalGISEIEL.formatoPDF;
		    										if(radioSelectedMunicipio){
		    											//seleccionado  por municipio
		    											for(int i=0; i<lstMunicipiosNucleos.size(); i++)
		    											{
		    												Municipio municipio = (Municipio)lstMunicipiosNucleos.get(i);
		    												for (int j=0; j<lstCuadros.size(); j++){
		    													
		    													CuadrosMPTBean cuadro = (CuadrosMPTBean)lstCuadros.get(j);
		    													 
		    													Object[] plantillaSelected = null;
		    													for (int h=0; h<plantillas.length; h++){
		    														if( ("cuadro_"+cuadro.getNamejrxml()+".jrxml").equals((String)((Object[])plantillas[h])[0])){
		    															plantillaSelected= (Object[])plantillas[h];
		    														}
		    															
		    													}
		    									 			     String jrxmlfile= guardarPlantilla(plantillaSelected);

		    										    		String where = "";
		    										    		if(cuadro.getNamejrxml().equals("A") || cuadro.getNamejrxml().equals("G")){
		    										    			where = cuadro.getTabla()+".PROVINCIA='"+GeopistaUtil.completarConCeros(String.valueOf(municipio.getId()),5).substring(0, 2)+"'";
				    												
		    										    		}
		    										    		else{
		    										    			where = cuadro.getTabla()+".PROVINCIA='"+GeopistaUtil.completarConCeros(String.valueOf(municipio.getId()),5).substring(0, 2)+"' AND "+
		    										    			cuadro.getTabla()+".MUNICIPIO='"+GeopistaUtil.completarConCeros(String.valueOf(municipio.getId()),5).substring(2, String.valueOf(municipio.getId()).length())+"'";
					    												
		    										    		}
		    										    			
		    										    		String pathDestino = path+ File.separator+cuadro.getNombre()+"_"+municipio.getId()+".pdf";
		    										    		HashMap paramHash = new HashMap();
		    										    		paramHash.put("fase", fase);
		    										    		paramHash.put("isla", "00");
		    										    		paramHash.put("dipu", "Asturias");
		    										    		paramHash.put("codprov", String.valueOf(municipio.getId()).substring(0,2));
		    										    		paramHash.put("muni", municipio.getNombreOficial());
		    										    		paramHash.put("codmuni", String.valueOf(municipio.getId()).substring(2, 
								        								String.valueOf(municipio.getId()).length()));
		    										    		paramHash.put("where", where);
		    													
		    										    		generarInformeJButtonActionPerformed( jrxmlfile, paramHash,  pathDestino, formato);
	
		    													
		    												}
		    												
		    											}
		    												
		    										}
		    										else{
		    											//seleccionado  por nucleo
		    											for(int i=0; i<lstMunicipiosNucleos.size(); i++)
		    											{ 
		    												Poblamiento_bean poblamiento_bean = (Poblamiento_bean)lstMunicipiosNucleos.get(i);
		    												for (int j=0; j<lstCuadros.size(); j++){
		    													CuadrosMPTBean cuadro = (CuadrosMPTBean)lstCuadros.get(j);
		    													
		    													String pathDestino = path + File.separator + cuadro.getNombre()+"_"+poblamiento_bean.getProvincia()+
																	poblamiento_bean.getMunicipio()+poblamiento_bean.getEntidad()+poblamiento_bean.getPoblamient()+".pdf";
															
		    													 if (cuadro.getId() == 2 || cuadro.getId() == 3 || cuadro.getId() ==4)
		    									                 {
		    														 
	    															Object[] plantillaSelected = null;
			    													for (int h=0; h<plantillas.length; h++){
			    														if( ("cuadro_"+cuadro.getNamejrxml()+".jrxml").equals((String)((Object[])plantillas[h])[0])){
			    															plantillaSelected= (Object[])plantillas[h];
			    														}
			    															
			    													}
			    									 			     String jrxmlfile= guardarPlantilla(plantillaSelected);

		    														String where = cuadro.getTablasec()+".PROVINCIA='"+poblamiento_bean.getProvincia()+"' AND "+
			    										    			cuadro.getTablasec()+".MUNICIPIO='"+poblamiento_bean.getMunicipio()+"'";
		    													
		    														HashMap paramHash = new HashMap();
		    														paramHash.put("fase", fase);
			    										    		paramHash.put("isla", "00");
			    										    		paramHash.put("dipu", "Asturias");
			    										    		paramHash.put("codprov", poblamiento_bean.getProvincia());
			    										    		paramHash.put("muni", poblamiento_bean.getDenominacion());
			    										    		paramHash.put("codmuni", poblamiento_bean.getMunicipio());
			    										    		paramHash.put("where", where);
			    										    	
			    													generarInformeJButtonActionPerformed(jrxmlfile, paramHash,  pathDestino, formato);
		    									                   
		    									                  }
		    													 else{
		    														
	    															Object[] plantillaSelected = null;
			    													for (int h=0; h<plantillas.length; h++){
			    														if( ("cuadro_"+cuadro.getNamejrxml()+".jrxml").equals((String)((Object[])plantillas[h])[0])){
			    															plantillaSelected= (Object[])plantillas[h];
			    														}
			    															
			    													}
			    									 			     String jrxmlfile= guardarPlantilla(plantillaSelected);
		    												
		    														 String where = "";
			    													 if (cuadro.getId() == 5 || cuadro.getId() == 48 || (cuadro.getId() >= 49 && cuadro.getId() <= 66))
			    									                 {
			    														
				    													where = 	cuadro.getTablasec()+".POBLAMIENT='"+ poblamiento_bean.getPoblamient()+"' AND "+ 
				    																	cuadro.getTablasec()+".ENTIDAD='"+poblamiento_bean.getEntidad()+"' AND " +
				    																	cuadro.getTablasec()+".PROVINCIA='"+poblamiento_bean.getProvincia()+ "' AND "+
				    																	cuadro.getTablasec()+".MUNICIPIO='"+poblamiento_bean.getMunicipio()+"' AND "+
				    																	cuadro.getTabla()+".PROVINCIA='"+poblamiento_bean.getProvincia()+"' AND "+
				    																	cuadro.getTabla()+".MUNICIPIO='"+poblamiento_bean.getMunicipio()+"'";
				    													
			    														 
			    									                 }
			    													 else{
			    														 				    														 
				    													where = 	cuadro.getTablasec()+".NUCLEO='"+poblamiento_bean.getPoblamient()+"' AND "+ 
				    																	cuadro.getTablasec()+".ENTIDAD='"+poblamiento_bean.getEntidad()+"' AND " +
				    																	cuadro.getTablasec()+".PROVINCIA='"+poblamiento_bean.getProvincia()+"' AND "+
				    																	cuadro.getTablasec()+".MUNICIPIO='"+poblamiento_bean.getMunicipio()+"' AND "+
				    																	cuadro.getTabla()+".PROVINCIA='"+poblamiento_bean.getProvincia()+"' AND "+
				    																	cuadro.getTabla()+".MUNICIPIO='"+poblamiento_bean.getMunicipio()+"'";
				    													
					    													
			    													 }
			    													 	HashMap paramHash = new HashMap();
			    														paramHash.put("fase", fase);
				    										    		paramHash.put("isla", "00");
				    										    		paramHash.put("dipu", "Asturias");
				    										    		paramHash.put("codprov", poblamiento_bean.getProvincia());
				    										    		paramHash.put("muni", poblamiento_bean.getDenominacion());
				    										    		paramHash.put("codmuni", poblamiento_bean.getMunicipio());
				    										    		paramHash.put("where", where);
				    										    	
				    													generarInformeJButtonActionPerformed(jrxmlfile, paramHash,  pathDestino, formato);
		    													 }
		    												}
		    											}
		    										}
	    										
	    										}		
	    	
	    									}
	    									catch(Exception e)
	    									{
	    										logger.error("Error ", e);
	    										ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), "ERROR", "ERROR", StringUtil.stackTrace(e));
	    										return;
	    									}
	    									finally
	    									{
	    										progressDialog.setVisible(false);                                
	    										progressDialog.dispose();								
	    										dispose();
	    									}
	    								}
	    							}).start();
	    						}
	    					});
	    					GUIUtil.centreOnWindow(progressDialog);
	    					progressDialog.setVisible(true);
			
	    					show();
	                	}
				
	                }else{
	                	JOptionPane.showMessageDialog(
                              ExportCuadrosMPTDialog.this,
                              I18N.get("LocalGISEIEL", "localgiseiel.mpt.mensajes.exportcuadros.mptdatosnocorrectos"),
                              null,
                              JOptionPane.INFORMATION_MESSAGE);
	                }
                }
                else{//If press Cancel
                	dispose();
                }
            }
        });        	
            
    }//ok
        return _okCancelPanel;
}
    
    
/* CONSTRUCTORES */    
          
    public ExportCuadrosMPTDialog() throws Exception{
        	 this(ConstantesLocalGISEIEL.RUTA_EXPORT_MPT);
    }
    
    public ExportCuadrosMPTDialog(String rutaFichero) throws Exception{
    	 super(AppContext.getApplicationContext().getMainFrame());
    	 initialize(I18N.get("LocalGISEIEL", "localgiseiel.mpt.exportcuadro.panel.title"));         
        this.setLocationRelativeTo(null);
    }
    
   
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(String title) throws Exception{
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        this.setModal(true);
        this.setSize(DIM_X, DIM_Y);
        
        //clienteLocalGISEIEL = new LocalGISEIELClient(aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) +	"/EIELServlet");
        clienteLocalGISEIEL = new LocalGISEIELClient(aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) +	WebAppConstants.EIEL_WEBAPP_NAME+ ServletConstants.EIEL_SERVLET_NAME);
        cargarPlantillas(null);
        guardarImagenes();
        
        this.setLayout(new GridBagLayout());
        this.add(getExportCuadrosMPTPanel(), new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        
        
       // this.setContentPane(getValidateMPTPanel());
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle(title);
        this.setResizable(false);
        this.getOkCancelPanel().setVisible(true);
        this.addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClosing(java.awt.event.WindowEvent e){
                dispose();
            }
        });              
    }    
   
    public static void main(String[] args) throws Exception{
        ExportCuadrosMPTDialog dialog = new ExportCuadrosMPTDialog();
        dialog.setVisible(true);
    }
    
    /* PANEL */
    public ExportCuadrosMPTPanel getExportCuadrosMPTPanel(){
        if (exportCuadrosMPTPanel == null){
        	exportCuadrosMPTPanel = new ExportCuadrosMPTPanel(new GridBagLayout());

        	exportCuadrosMPTPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(1, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return exportCuadrosMPTPanel;
    }
    
    
    
    private void generarInformeJButtonActionPerformed( String jrxmlfile, HashMap paramHash, String pathDestino, String formato) {
        try{
          //  this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

             try {
            	 String locale =  UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES", true);
            	 
                 JREIEL.generarReportCuadrosConnectDB( jrxmlfile, paramHash, pathDestino, formato, AppContext.getApplicationContext().getMainFrame(), locale);

             }catch (Exception e) {
                
                ErrorDialog.show(this,I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.expErrorCuadrosMPT"), I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.expEielCuadrosMPT"), StringUtil.stackTrace(e));
                logger.error("Error al genera el cuadro mpt. Fichero: "+jrxmlfile,e);
            }

        }catch (Exception e){
            JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.expErrorCuadrosMPT"), I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.expEielCuadrosMPT"), JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }
    
    private JFileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
             fileChooser.setDialogTitle(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.expMPT.save"));
            GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
            filter.addExtension("pdf");
            filter.setDescription("PDF");
    		fileChooser.setFileHidingEnabled(false);
    		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setFileFilter(filter);
            File currentDirectory = (File) aplicacion.getBlackboard().get(Constantes.LAST_IMPORT_DIRECTORY);
            fileChooser.setCurrentDirectory(currentDirectory);

        }
        //return fileChooser.showOpenDialog(this);
        return (JFileChooser) fileChooser;
	  }
    
	 private int abrirJFileChooser(){
		 return getFileChooser().showOpenDialog(this);
	 }
	 
	 private String getDirectorySelected() {
		 String directory=null;
		 File file=null;
		 if(getFileChooser().getSelectedFile()!=null){
			 file=getFileChooser().getSelectedFile();
			 directory=file.getAbsolutePath();
			 aplicacion.getBlackboard().put(Constantes.LAST_IMPORT_DIRECTORY,file);
			 
		 }
		 
		return directory;
	}
	 
	 private void guardarImagenes() throws Exception{
	        if (imagenes == null) return;
	        String path= ConstantesLocalGISEIEL.PATH_PLANTILLAS_CUADROS_IMG_EIEL;
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
	 
	 
	 private String guardarPlantilla(Object[] plantilla) throws Exception{

        if ((byte[])plantilla[1] == null) throw new ACException("Plantilla vacia");
        String path= ConstantesLocalGISEIEL.PATH_PLANTILLAS_CUADROS_EIEL + File.separator;

        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        logger.debug("Guardando la plantilla:"+new File(path).getAbsolutePath()+(String)plantilla[0]);

        FileOutputStream out = new FileOutputStream(path + (String)plantilla[0]);
        out.write((byte[])plantilla[1]);
        out.close();

        return path + (String)plantilla[0];
    }
	 
	 private void cargarPlantillas(String filtro) throws Exception{
		
		
    	Collection c = clienteLocalGISEIEL.getPlantillasCuadros(ConstantesLocalGISEIEL.PATH_PLANTILLAS_CUADROS_EIEL + File.separator + getSubdir(),filtro );
        if (c == null) return;
        Object[] objs= c.toArray();

        imagenes= ((ArrayList)objs[0]).toArray();
        plantillas= ((ArrayList)objs[1]).toArray();

    }
   
	 private String getSubdir() {
			return "";
		}
}
