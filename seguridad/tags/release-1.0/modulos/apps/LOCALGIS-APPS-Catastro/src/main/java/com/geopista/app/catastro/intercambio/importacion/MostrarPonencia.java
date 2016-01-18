/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */

package com.geopista.app.catastro.intercambio.importacion;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import com.geopista.app.AppContext;
import com.geopista.app.catastro.gestorcatastral.MainCatastro;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.intercambio.importacion.dialogs.FileImportResultPanel;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils_LCGIII;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaCNT;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaPoligono;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaPostgre;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaTramos;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaUrbanistica;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaZonaValor;
import com.geopista.app.catastro.model.datos.ponencia.RUEvaluatorio;
import com.geopista.app.catastro.model.datos.ponencia.TipoValor;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class MostrarPonencia extends JPanel implements WizardPanel
{
    private ApplicationContext application = AppContext.getApplicationContext();    
    private Blackboard blackboard = application.getBlackboard();
    private JEditorPane jEditorPaneResultadoImportacion = new JEditorPane();    
    private StringBuffer sbMessage = new StringBuffer();  
    private StringBuffer errorMessage = new StringBuffer();
    
    private int totalRows=0;    
    private int insertedRows=0;
    private int notInsertedRows=0;
    private int readedRows=0;
    
    public Connection con = null;
    
    private String nextID = null;
    
    
    public MostrarPonencia()
    {
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Importacioni18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Importacion",bundle);
        
        
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(application
                .getMainFrame(), null);
        
        progressDialog.setTitle(application.getI18nString("CargandoDatosIniciales"));
        progressDialog.report(application.getI18nString("CargandoDatosIniciales"));
        progressDialog.addComponentListener(new ComponentAdapter()
                {
            public void componentShown(ComponentEvent e)
            {
                
                // Wait for the dialog to appear before starting the
                // task. Otherwise
                // the task might possibly finish before the dialog
                // appeared and the
                // dialog would never close. [Jon Aquino]
                new Thread(new Runnable()
                        {
                    public void run()
                    {
                        try
                        {
                            setName(I18N.get("Importacion","importar.asistente.ponencia.titulo.2"));
                            jbInit();
                        } 
                        catch (Exception e)
                        {
                            e.printStackTrace();
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
    
    private void jbInit() throws Exception
    {  
        this.setLayout(new GridBagLayout());
        FileImportResultPanel panel = new FileImportResultPanel();
        jEditorPaneResultadoImportacion = panel.getJEditorPaneResultadoImportacion();
        panel.getLabelImagen().setIcon(IconLoader.icon(MainCatastro.BIG_PICTURE_LOCATION));
        this.add(panel,  
                new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(10,10,10,10) ,0,0));
        
    }
    
    /**
     * Realiza el proceso de importación en cuanto se accede a esta pantalla 
     */    
    public void enteredFromLeft(Map dataMap)
    {
        sbMessage = new StringBuffer();
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(application
                .getMainFrame(), null);   
        
        progressDialog.setTitle(I18N.get("Importacion","importar.general.proceso.importando"));
        progressDialog.report(I18N.get("Importacion","importar.general.proceso.importando"));
        progressDialog.addComponentListener(new ComponentAdapter()
                {
            public void componentShown(ComponentEvent e)
            {
                
                // Wait for the dialog to appear before starting the
                // task. Otherwise
                // the task might possibly finish before the dialog
                // appeared and the
                // dialog would never close. [Jon Aquino]
                new Thread(new Runnable()
                        {
                    public void run()
                    {
                        try
                        {
                        	
                            String rutaImp = (String) blackboard.get(ImportarUtils_LCGIII.FILE_TO_IMPORT);
                            String linea;
                            File file = new File(rutaImp);
                            
                            totalRows=0;    
                            insertedRows=0;
                            notInsertedRows=0;
                            readedRows=0;                            
                            
                            PonenciaPostgre consultaBD = new PonenciaPostgre();
                            
                            //UNA O VARIAS LINEAS
                            InputStream is = new FileInputStream(file); 
                            int numRegistros = (int)file.length()/ImportarPonencia.LONGITUD_REGISTRO;
                            
                            byte[] bytes = new byte[ImportarPonencia.LONGITUD_REGISTRO];                                                
                            int len;    
                                                        
                            while ((len = is.read(bytes)) > 0 )
                            {
                            	linea = new String(bytes);
                            	
                            	progressDialog.report(readedRows, numRegistros-2,
                                        application.getI18nString("ImportandoDatos").toString());
                                    	
                                    	
                                    	if(!linea.substring(0,2).equals("05")){//Cabecera
                                        
                                    		if((!linea.substring(0,2).equals("01"))&&(!linea.substring(0,2).equals("90"))){
                                    			readedRows++;
                                    		}
                                    		
                                    		if(linea.substring(0,2).equals("02")){
                                    			PonenciaCNT ponenciaCNT = new PonenciaCNT();    
                                    			ponenciaCNT.setCodDelegacionMEH(linea.substring(2,4));
                                    			ponenciaCNT.setCodMunicipioMEH(linea.substring(4,7));
                                    			ponenciaCNT.setCodProvinciaINE(linea.substring(7,9));
                                    			ponenciaCNT.setCodMunicipioINE(linea.substring(9,12));  
                                    			try{
                                    				ponenciaCNT.setAnioAprobacion(Integer.valueOf(linea.substring(12,16)));                            		
                                    			}catch(Exception e){
                                    				ponenciaCNT.setAnioAprobacion(null);
                                    			}
                                    			ponenciaCNT.setTipoPonencia(linea.substring(16,17));
                                    			try{
                                    				ponenciaCNT.setAnioEfectos(Integer.valueOf(linea.substring(17,21)));
                                    			}catch(Exception e){
                                    				ponenciaCNT.setAnioEfectos(null);
                                    			}
                                    			try{
                                    				ponenciaCNT.setAnioAprobacionTotal(Integer.valueOf(linea.substring(21,25)));
                                    			}catch(Exception e){
                                    				ponenciaCNT.setAnioAprobacionTotal(null);
                                    			}
                                    			try{
                                    				ponenciaCNT.setAnioAprobacionTotal(Integer.valueOf(linea.substring(25,29)));
                                    			}catch(Exception e){
                                    				ponenciaCNT.setAnioAprobacionTotal(null);
                                    			}
                                    			try{
                                    				ponenciaCNT.setAnioEfectosTotal(Integer.valueOf(linea.substring(29,33)));
                                    			}catch(Exception e){
                                    				ponenciaCNT.setAnioEfectosTotal(null);
                                    			}
                                    			try{
                                    				ponenciaCNT.setAnioNormas(Integer.valueOf(linea.substring(33,37)));
                                    			}catch(Exception e){
                                    				ponenciaCNT.setAnioNormas(null);
                                    			}
                                    			try{
                                    				ponenciaCNT.setAnioCuadroMarco(Integer.valueOf(linea.substring(37,41)));
                                    			}catch(Exception e){
                                    				ponenciaCNT.setAnioCuadroMarco(null);
                                    			}
                                    			ponenciaCNT.setAplicFormula(linea.substring(41,42));
                                    			try{
                                    				ponenciaCNT.setInfraedificacion(Float.valueOf(linea.substring(42,44)+'.'+linea.substring(44,46)));
                                    			}catch(Exception e){
                                    				ponenciaCNT.setInfraedificacion(null);
                                    			}
                                    			ponenciaCNT.setPropVertical(linea.substring(46,47));
                                    			try{
                                    				ponenciaCNT.setAntiguedad(Integer.valueOf(linea.substring(47,51)));
                                    			}catch(Exception e){
                                    				ponenciaCNT.setAntiguedad(null);
                                    			}
                                    			ponenciaCNT.setRuinoso(linea.substring(51,52));
                                    			ponenciaCNT.setSinDesarrollar(linea.substring(52,53));
                                    			
                                    			//Grabar en la base de datos
                                    			if(consultaBD.insertPonenciaCNT(ponenciaCNT)){
                                        			insertedRows++;	                                			                                			
                                    			}
                                    			else{
                                    				errorMessage.append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.ponencia.ponenciacnt"), readedRows));
                                    			}                            			
                                    			
                                    		}
                                    		else{
                                    			if(linea.substring(0,2).equals("03")){
                                    				PonenciaPoligono ponenciaPoligono = new PonenciaPoligono();
                                    				ponenciaPoligono.setCodDelegacionMEH(linea.substring(2,4));
                                    				ponenciaPoligono.setCodMunicipioMEH(linea.substring(4,7));
                                    				ponenciaPoligono.setCodProvinciaINE(linea.substring(7,9));
                                    				ponenciaPoligono.setCodMunicipioINE(linea.substring(9,12));
                                    				try{
                                    					ponenciaPoligono.setAnioAprobacion(Integer.valueOf(linea.substring(12,16)));
                                    				}catch(Exception e){
                                    					ponenciaPoligono.setAnioAprobacion(null);
                                    				}
                                    				ponenciaPoligono.setCodPoligono(linea.substring(16,19));
                                    				try{
                                    					ponenciaPoligono.setImporteMBC(Double.valueOf(linea.substring(49,54)+'.'+linea.substring(54,60)));
                                    				}catch(Exception e){
                                    					ponenciaPoligono.setImporteMBC(null);
                                    				}
                                    				try{
                                    					ponenciaPoligono.setImporteMBR(Double.valueOf(linea.substring(60,65)+'.'+linea.substring(65,71)));
                                    				}catch(Exception e){
                                    					ponenciaPoligono.setImporteMBR(null);
                                    				}
                                    				ponenciaPoligono.setZonaVRB(linea.substring(71,76));
                                    				try{
                                    				ponenciaPoligono.setVrb(Double.valueOf(linea.substring(76,81)+'.'+linea.substring(81,87)));
                                    				}catch(Exception e){
                                    					ponenciaPoligono.setVrb(null);
                                    				}
                                    				ponenciaPoligono.setZonaVUB(linea.substring(87,92));
                                    				try{
                                    					ponenciaPoligono.setFlGB(Float.valueOf(linea.substring(103,105)+'.'+linea.substring(105,107)));
                                    				}catch(Exception e){
                                    					ponenciaPoligono.setFlGB(null);
                                    				}
                                    				try{
                                    				ponenciaPoligono.setFlGBUni(Float.valueOf(linea.substring(107,109)+'.'+linea.substring(109,111)));
                                    				}catch(Exception e){
                                    					ponenciaPoligono.setFlGBUni(null);
                                    				}
                                    				ponenciaPoligono.setDiseminado(linea.substring(111,112));
                                    				try{
                                    					ponenciaPoligono.setCodMBCPlan(Integer.valueOf(linea.substring(112,113)));
                                    				}catch(Exception e){
                                    					ponenciaPoligono.setCodMBCPlan(null);
                                    				}
                                    				try{
                                    					ponenciaPoligono.setEdifPlan(Float.valueOf(linea.substring(113,115)+'.'+linea.substring(115,117)));
                                    				}catch(Exception e){
                                    					ponenciaPoligono.setEdifPlan(null);
                                    				}
                                    				try{
                                    					ponenciaPoligono.setGrupoPlan(Integer.valueOf(linea.substring(117,118)));
                                    				}catch(Exception e){
                                    					ponenciaPoligono.setGrupoPlan(null);
                                    				}
                                    				ponenciaPoligono.setUsoPlan(linea.substring(118,119));
                                    				try{
                                    					ponenciaPoligono.setModPlan(Float.valueOf(linea.substring(119,126)));
                                    				}catch(Exception e){
                                    					ponenciaPoligono.setModPlan(null);
                                    				}
                                    				try{
                                    					ponenciaPoligono.setCoefCoordPlan(Float.valueOf(linea.substring(126,127)+'.'+linea.substring(127,129)));
                                    				}catch(Exception e){
                                    					ponenciaPoligono.setCoefCoordPlan(null);
                                    				}
                                    				
                                    				if(consultaBD.insertPonenciaPoligono(ponenciaPoligono)){
                                        				insertedRows++;
                                    				}
                                    				else{
                                        				errorMessage.append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.ponencia.ponenciapoligono"), readedRows));
                                        			}
                                    				                            				
                                    			}
                                    			else{
                                    				if(linea.substring(0,2).equals("04")){
                                    					PonenciaUrbanistica ponenciaUrbanistica = new PonenciaUrbanistica();
                                    					ponenciaUrbanistica.setCodDelegacionMEH(linea.substring(2,4));
                                    					ponenciaUrbanistica.setCodMunicipioMEH(linea.substring(4,7));
                                    					ponenciaUrbanistica.setCodProvinciaINE(linea.substring(7,9));
                                    					ponenciaUrbanistica.setCodMunicipioINE(linea.substring(9,12));
                                    					try{
                                    						ponenciaUrbanistica.setAnioAprobacion(Integer.valueOf(linea.substring(12,16)));
                                    					}catch(Exception e){
                                    						ponenciaUrbanistica.setAnioAprobacion(null);
                                    					}
                                    					ponenciaUrbanistica.setCodZona(linea.substring(16,26).trim());
                                    					ponenciaUrbanistica.setDenominacion(linea.substring(26,29));
                                    					ponenciaUrbanistica.setCodCalificacion(linea.substring(56,58));
                                    					ponenciaUrbanistica.setCodZonificacion(linea.substring(58,60));
                                    					ponenciaUrbanistica.setCodOcupacion(linea.substring(60,62));
                                    					ponenciaUrbanistica.setCodOrdenacion(linea.substring(62,63));
                                    					try{
                                    						ponenciaUrbanistica.setLongitud(Integer.valueOf(linea.substring(63,67)));
                                    					}catch(Exception e){
                                    						ponenciaUrbanistica.setLongitud(null);
                                    					}
                                    					try{
                                    						ponenciaUrbanistica.setFondo(Integer.valueOf(linea.substring(67,70)));
                                    					}catch(Exception e){
                                    						ponenciaUrbanistica.setFondo(null);
                                    					}
                                    					try{
                                    						ponenciaUrbanistica.setSupMinima(Integer.valueOf(linea.substring(70,75)));
                                    					}catch(Exception e){
                                    						ponenciaUrbanistica.setSupMinima(null);
                                    					}
                                    					try{
                                    						ponenciaUrbanistica.setNumPlantas(Float.valueOf(linea.substring(75,78)+'.'+linea.substring(78,79)));
                                    					}catch(Exception e){
                                    						ponenciaUrbanistica.setNumPlantas(null);
                                    					}
                                    					try{
                                    						ponenciaUrbanistica.setNumPlantasSolatInt(Float.valueOf(linea.substring(79,82)+'.'+linea.substring(82,83)));
                                    					}catch(Exception e){
                                    						ponenciaUrbanistica.setNumPlantasSolatInt(null);
                                    					}
                                    					TipoValor edificabilidad = new TipoValor();
                                    					try{
                                    						edificabilidad.setUsoComercial(Float.valueOf(linea.substring(83,85)+'.'+linea.substring(85,87)));
                                    					}catch(Exception e){
                                    						edificabilidad.setUsoComercial(null);
                                    					}
                                    					try{
                                    						edificabilidad.setUsoResidencial(Float.valueOf(linea.substring(87,89)+'.'+linea.substring(89,91)));
                                    					}catch(Exception e){
                                    						edificabilidad.setUsoResidencial(null);
                                    					}
                                    					try{
                                    						edificabilidad.setUsoOficinas(Float.valueOf(linea.substring(91,93)+'.'+linea.substring(93,95)));
                                    					}catch(Exception e){
                                    						edificabilidad.setUsoOficinas(null);
                                    					}
                                    					try{
                                    						edificabilidad.setUsoIndustrial(Float.valueOf(linea.substring(95,97)+'.'+linea.substring(97,99)));
                                    					}catch(Exception e){
                                    						edificabilidad.setUsoIndustrial(null);
                                    					}
                                    					try{
                                    						edificabilidad.setUsoTuristico(Float.valueOf(linea.substring(99,101)+'.'+linea.substring(101,103)));
                                    					}catch(Exception e){
                                    						edificabilidad.setUsoTuristico(null);
                                    					}
                                    					try{
                                    						edificabilidad.setOtrosUsos1(Float.valueOf(linea.substring(103,105)+'.'+linea.substring(105,107)));
                                    					}catch(Exception e){
                                    						edificabilidad.setOtrosUsos1(null);
                                    					}
                                    					try{
                                    						edificabilidad.setOtrosUsos2(Float.valueOf(linea.substring(107,109)+'.'+linea.substring(109,111)));
                                    					}catch(Exception e){
                                    						edificabilidad.setOtrosUsos2(null);
                                    					}
                                    					try{
                                    						edificabilidad.setOtrosUsos3(Float.valueOf(linea.substring(111,113)+'.'+linea.substring(113,115)));
                                    					}catch(Exception e){
                                    						edificabilidad.setOtrosUsos3(null);
                                    					}
                                    					try{
                                    						edificabilidad.setZonaVerde(Float.valueOf(linea.substring(115,117)+'.'+linea.substring(117,119)));
                                    					}catch(Exception e){
                                    						edificabilidad.setZonaVerde(null);
                                    					}
                                    					try{
                                    						edificabilidad.setEquipamientos(Float.valueOf(linea.substring(119,121)+'.'+linea.substring(121,123)));
                                    					}catch(Exception e){
                                    						edificabilidad.setEquipamientos(null);
                                    					}
                                    					ponenciaUrbanistica.setEdificabilidad(edificabilidad);
                                    					
                                    					if(consultaBD.insertPonenciaUrbanistica(ponenciaUrbanistica)){
                                        					insertedRows++;
                                    					}
                                    					else{
                                            				errorMessage.append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.ponencia.ponenciaurbanistica"), readedRows));
                                            			}
                                    					                            					
                                    				}
                                    				else{
                                    					if(linea.substring(0,2).equals("06")){
                                    						PonenciaZonaValor ponenciaZonaValor = new PonenciaZonaValor();
                                    						ponenciaZonaValor.setCodDelegacionMEH(linea.substring(2,4));
                                    						ponenciaZonaValor.setCodMunicipioMEH(linea.substring(4,7));
                                    						ponenciaZonaValor.setCodProvinciaINE(linea.substring(7,9));
                                    						ponenciaZonaValor.setCodMunicipioINE(linea.substring(9,12));
                                    						try{
                                    							ponenciaZonaValor.setAnioAprobacion(Integer.valueOf(linea.substring(12,16)));
                                    						}catch(Exception e){
                                    							ponenciaZonaValor.setAnioAprobacion(null);
                                    						}
                                    						ponenciaZonaValor.setCodZonaValor(linea.substring(16,21).trim());
                                    						TipoValor importesZonaValor = new TipoValor();
                                    						try{
                                    							importesZonaValor.setUsoComercial(Float.valueOf(linea.substring(21,26)+'.'+linea.substring(26,28)));
                                    						}catch(Exception e){
                                    							importesZonaValor.setUsoComercial(null);
                                    						}
                                    						try{
                                    							importesZonaValor.setUsoResidencial(Float.valueOf(linea.substring(28,33)+'.'+linea.substring(33,35)));
                                    						}catch(Exception e){
                                    							importesZonaValor.setUsoResidencial(null);
                                    						}
                                    						try{
                                    							importesZonaValor.setUsoOficinas(Float.valueOf(linea.substring(35,40)+'.'+linea.substring(40,42)));
                                    						}catch(Exception e){
                                    							importesZonaValor.setUsoOficinas(null);
                                    						}
                                    						try{
                                    							importesZonaValor.setUsoIndustrial(Float.valueOf(linea.substring(42,47)+'.'+linea.substring(47,49)));
                                    						}catch(Exception e){
                                    							importesZonaValor.setUsoIndustrial(null);
                                    						}
                                    						try{
                                    							importesZonaValor.setUsoTuristico(Float.valueOf(linea.substring(49,54)+'.'+linea.substring(54,56)));
                                    						}catch(Exception e){
                                    							importesZonaValor.setUsoTuristico(null);
                                    						}
                                    						try{
                                    							importesZonaValor.setOtrosUsos1(Float.valueOf(linea.substring(58,61)+'.'+linea.substring(61,63)));
                                    						}catch(Exception e){
                                    							importesZonaValor.setOtrosUsos1(null);
                                    						}
                                    						try{
                                    							importesZonaValor.setOtrosUsos2(Float.valueOf(linea.substring(63,68)+'.'+linea.substring(68,70)));
                                    						}catch(Exception e){
                                    							importesZonaValor.setOtrosUsos2(null);
                                    						}
                                    						try{
                                    							importesZonaValor.setOtrosUsos3(Float.valueOf(linea.substring(70,75)+'.'+linea.substring(75,77)));
                                    						}catch(Exception e){
                                    							importesZonaValor.setOtrosUsos3(null);
                                    						}
                                    						try{
                                    							importesZonaValor.setZonaVerde(Float.valueOf(linea.substring(77,82)+'.'+linea.substring(82,84)));
                                    						}catch(Exception e){
                                    							importesZonaValor.setZonaVerde(null);
                                    						}
                                    						try{
                                    							importesZonaValor.setEquipamientos(Float.valueOf(linea.substring(84,89)+'.'+linea.substring(89,91)));
                                    						}catch(Exception e){
                                    							importesZonaValor.setEquipamientos(null);
                                    						}
                                    						ponenciaZonaValor.setImportesZonaValor(importesZonaValor);
                                    						try{
                                    						ponenciaZonaValor.setValorUnitario(Float.valueOf(linea.substring(91,96)+'.'+linea.substring(96,98)));
                                    						}catch(Exception e){
                                    							ponenciaZonaValor.setValorUnitario(null);
                                    						}
                                    						try{
                                    							ponenciaZonaValor.setValorZonaVerde(Float.valueOf(linea.substring(98,103)+'.'+linea.substring(103,105)));
                                    						}catch(Exception e){
                                    							ponenciaZonaValor.setValorUnitario(null);
                                    						}
                                    						try{
                                    							ponenciaZonaValor.setValorEquipamientos(Float.valueOf(linea.substring(105,110)+'.'+linea.substring(110,112)));
                                    						}catch(Exception e){
                                    							ponenciaZonaValor.setValorEquipamientos(null);
                                    						}
                                    						try{
                                    							ponenciaZonaValor.setValorSinDesarrollar(Float.valueOf(linea.substring(112,117)+'.'+linea.substring(117,119)));
                                    						}catch(Exception e){
                                    							ponenciaZonaValor.setValorSinDesarrollar(null);
                                    						}
                                    						
                                    						if(consultaBD.insertPonenciaZonaValor(ponenciaZonaValor)){
                                    							insertedRows++;                                						
                                    						}
                                    						else{
                                                				errorMessage.append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.ponencia.ponenciazonavalor"), readedRows));
                                                			}
                                    						
                                    					}
                                    					else if (linea.substring(0,2).equals("07")){
                                    						RUEvaluatorio ruEvaluatorio = new RUEvaluatorio();
                                    						ruEvaluatorio.setCodDelegacionMEH(linea.substring(2,4));
                                    						ruEvaluatorio.setCodMunicipioMEH(linea.substring(4,7));
                                    						ruEvaluatorio.setCodProvinciaINE(linea.substring(7,9));
                                    						ruEvaluatorio.setCodMunicipioINE(linea.substring(9,12));
                                    						try{
                                    							ruEvaluatorio.setCodMunicipioAgregado(Integer.valueOf(linea.substring(12,15)));
                                    						}catch(Exception e){
                                    							ruEvaluatorio.setAnioAprobacion(null);
                                    						}                                    						
                                    						try{
                                    							ruEvaluatorio.setAnioAprobacion(Integer.valueOf(linea.substring(15,19)));
                                    						}catch(Exception e){
                                    							ruEvaluatorio.setAnioAprobacion(null);
                                    						}
                                    						try{
                                    							ruEvaluatorio.setCodCalificacionCatastral(linea.substring(19,21));
                                    						}catch(Exception e){
                                    							ruEvaluatorio.setCodCalificacionCatastral(null);
                                    						}
                                    						try{
                                    							ruEvaluatorio.setClaseClutivo(linea.substring(21,61));
                                    						}catch(Exception e){
                                    							ruEvaluatorio.setClaseClutivo(null);
                                    						}
                                    						try{
                                    							ruEvaluatorio.setIndicadorVueloPiesSueltos(linea.substring(61,62));
                                    						}catch(Exception e){
                                    							ruEvaluatorio.setIndicadorVueloPiesSueltos(null);
                                    						}
                                    						try{
                                    							ruEvaluatorio.setIntensidadProductivaBOE(Integer.valueOf(linea.substring(62,64)));
                                    						}catch(Exception e){
                                    							ruEvaluatorio.setIntensidadProductivaBOE(null);
                                    						}
                                    						try{
                                    							ruEvaluatorio.setImporteTipoEvaluatorio(Double.valueOf(linea.substring(64,70)+'.'+linea.substring(70,76)));
                                    						}catch(Exception e){
                                    							ruEvaluatorio.setImporteTipoEvaluatorio(null);
                                    						}					
                                    						if(consultaBD.insertRuEvaluatorio(ruEvaluatorio)){
                                    							insertedRows++;                                						
                                    						}
                                    						else{
                                                				errorMessage.append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.ponencia.ruevaluatorio"), readedRows));
                                                			}
                                    					}
                                    				}
                                    			}
                                    		}
                                        
                                    	}
                                    	
                                    	//si tiene retornos de carro, se salta la lectura de estos 2 bytes
                                        if((new Boolean(blackboard.get(ImportarUtils_LCGIII.FILE_TXT_MULTILINE).toString()))
                                                .booleanValue())
                                            is.skip(2);
                            }
                            is.close();
                           
                            is = new FileInputStream(file);                           
                            
                            while ((len = is.read(bytes)) > 0 )
                            {
                            	linea = new String(bytes);                            	
                            	
                            	totalRows++;
                            	progressDialog.report(readedRows, numRegistros-2,
                                application.getI18nString("ImportandoDatos").toString());                            	
                            	
                            	if(linea.substring(0,2).equals("05")){//Cabecera
                            		
                            		readedRows++;
                            		
                            		PonenciaTramos ponenciaTramos = new PonenciaTramos();
                            		ponenciaTramos.setCodDelegacionMEH(linea.substring(2,4));
                            		ponenciaTramos.setCodMunicipioMEH(linea.substring(4,7));
                            		ponenciaTramos.setCodProvinciaINE(linea.substring(7,9));
                            		ponenciaTramos.setCodMunicipioINE(linea.substring(9,12));
                            		try{
                            			ponenciaTramos.setAnioAprobacion(Integer.valueOf(linea.substring(12,16)));
                            		}catch(Exception e){
                            			ponenciaTramos.setAnioAprobacion(null);
                            		}
                            		ponenciaTramos.setCodVia(linea.substring(16,21));
                            		ponenciaTramos.setCodTramo(linea.substring(21,24));
                            		ponenciaTramos.setDenominacion(linea.substring(24,49));
                            		PonenciaPoligono ponPoligono = new PonenciaPoligono();
                            		ponPoligono.setCodPoligono(linea.substring(49,52));
                            		ponenciaTramos.setPonPoligono(ponPoligono);
                            		PonenciaUrbanistica ponUrbanistica = new PonenciaUrbanistica();
                            		ponUrbanistica.setCodZona(linea.substring(52,62).trim());
                            		ponenciaTramos.setPonUrbanistica(ponUrbanistica);
                            		ponenciaTramos.setSituacion(linea.substring(62,72));
                            		try{
                            			ponenciaTramos.setMaxPar(Integer.valueOf(linea.substring(72,76)));
                            		}catch(Exception e){
                            			ponenciaTramos.setMaxPar(null);
                            		}
                            		ponenciaTramos.setCMaxPar(linea.substring(76,77));
                            		try{
                            			ponenciaTramos.setMinPar(Integer.valueOf(linea.substring(77,81)));
                            		}catch(Exception e){
                            			ponenciaTramos.setMinPar(null);
                            		}
                            		ponenciaTramos.setCMinPar(linea.substring(81,82));
                            		try{
                            			ponenciaTramos.setMaxImpar(Integer.valueOf(linea.substring(82,86)));
                            		}catch(Exception e){
                            			ponenciaTramos.setMaxImpar(null);
                            		}
                            		ponenciaTramos.setCMaxImpar(linea.substring(86,87));
                            		try{
                            			ponenciaTramos.setMinImpar(Integer.valueOf(linea.substring(87,91)));
                            		}catch(Exception e){
                            			ponenciaTramos.setMinImpar(null);
                            		}
                            		ponenciaTramos.setCMinImpar(linea.substring(91,92));
                            		try{
                            			ponenciaTramos.setValorUnitario(Double.valueOf(linea.substring(92,97)+'.'+linea.substring(97,103)));
                            		}catch(Exception e){
                            			ponenciaTramos.setValorUnitario(null);
                            		}
                            		TipoValor banda = new TipoValor();
                            		try{
                            			banda.setUsoComercial(Float.valueOf(linea.substring(103,105)+'.'+linea.substring(105,107)));
                            		}catch(Exception e){
                            			banda.setUsoComercial(null);
                            		}
                            		try{
                            			banda.setUsoResidencial(Float.valueOf(linea.substring(107,109)+'.'+linea.substring(109,111)));
                            		}catch(Exception e){
                            			banda.setUsoComercial(null);
                            		}
                            		try{
                            			banda.setUsoOficinas(Float.valueOf(linea.substring(111,113)+'.'+linea.substring(113,115)));
                            		}catch(Exception e){
                            			banda.setUsoOficinas(null);
                            		}
                            		try{
                            			banda.setUsoIndustrial(Float.valueOf(linea.substring(115,117)+'.'+linea.substring(117,119)));
                            		}catch(Exception e){
                            			banda.setUsoIndustrial(null);
                            		}
                            		try{
                            			banda.setUsoTuristico(Float.valueOf(linea.substring(119,121)+'.'+linea.substring(121,123)));
                            		}catch(Exception e){
                            			banda.setUsoComercial(null);
                            		}
                            		try{
                            			banda.setOtrosUsos1(Float.valueOf(linea.substring(123,125)+'.'+linea.substring(125,127)));
                            		}catch(Exception e){
                            			banda.setOtrosUsos1(null);
                            		}
                            		try{
                            			banda.setOtrosUsos2(Float.valueOf(linea.substring(127,129)+'.'+linea.substring(129,131)));
                            		}catch(Exception e){
                            			banda.setOtrosUsos2(null);
                            		}
                            		try{
                            			banda.setOtrosUsos3(Float.valueOf(linea.substring(131,133)+'.'+linea.substring(133,135)));
                            		}catch(Exception e){
                            			banda.setOtrosUsos3(null);
                            		}
                            		ponenciaTramos.setBanda(banda);
                            		try{
                            			ponenciaTramos.setCorrApDepSuelo(Float.valueOf(linea.substring(135,136)+'.'+linea.substring(136,138)));
                            		}catch(Exception e){
                            			ponenciaTramos.setCorrApDepSuelo(null);
                            		}
                            		try{
                            			ponenciaTramos.setCorrApDepConst(Float.valueOf(linea.substring(138,139)+'.'+linea.substring(139,141)));
                            		}catch(Exception e){
                            			ponenciaTramos.setCorrApDepConst(null);
                            		}
                            		ponenciaTramos.setValorSuelo(linea.substring(141,142));
                            		ponenciaTramos.setAgua(linea.substring(142,143));
                            		ponenciaTramos.setElectricidad(linea.substring(143,144));
                            		ponenciaTramos.setAlumbrado(linea.substring(144,145));
                            		ponenciaTramos.setDesmonte(linea.substring(145,146));
                            		ponenciaTramos.setPavimentacion(linea.substring(146,147));
                            		ponenciaTramos.setAlcantarillado(linea.substring(147,148));
                            		try{
                            			ponenciaTramos.setCostesUrbanizacion(Double.valueOf(linea.substring(148,153)+'.'+linea.substring(153,159)));
                            		}catch(Exception e){
                            			ponenciaTramos.setCostesUrbanizacion(null);
                            		}
                            		
                            		if(consultaBD.insertPonenciaTramos(ponenciaTramos)){
                                		insertedRows++;
                            		}
                            		else{
                        				errorMessage.append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.ponencia.ponenciatramos"), readedRows));
                        			}
                            		
                            	}
                            	
                            	//si tiene retornos de carro, se salta la lectura de estos 2 bytes
                                if((new Boolean(blackboard.get(ImportarUtils_LCGIII.FILE_TXT_MULTILINE).toString()))
                                        .booleanValue())
                                    is.skip(2);
                            }
                              
                            notInsertedRows=totalRows-insertedRows;
                            progressDialog.report(I18N.get("Importacion","importar.general.proceso.grabando"));
                            
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }finally
                        {
                            progressDialog.setVisible(false);
                        }
                    }
                        }).start();
            }
                });
        GUIUtil.centreOnWindow(progressDialog);
                
        progressDialog.setVisible(true);
        
        printFinalMessage();
        
        try
        {
            // Iniciamos la ayuda
        	String helpHS = "help/catastro/importadores/importadoresHelp_es.hs";
            ClassLoader c1 = this.getClass().getClassLoader();
            URL hsURL = HelpSet.findHelpSet(c1, helpHS);
            HelpSet hs = new HelpSet(null, hsURL);
            HelpBroker hb = hs.createHelpBroker();
            // fin de la ayuda
            hb.enableHelpKey(this,"introduccion", hs);
        } 
        catch (Exception excp)
        {
            excp.printStackTrace();
        }
    }
    
    private void printFinalMessage()
    { 
        
        sbMessage.append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.general.fecha.finalizacion"), ImportarUtils.getDate()))
        .append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.general.numeroregistros.procesados"), totalRows))
        .append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.general.numeroregistros.insertados"), insertedRows))
        .append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.general.numeroregistros.no.insertados"), notInsertedRows));
        
        sbMessage.append(errorMessage);
        
        jEditorPaneResultadoImportacion.setText(sbMessage.toString());
        jEditorPaneResultadoImportacion.setVisible(true);
    }
    
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
    }
    
    /**
     * Tip: Delegate to an InputChangedFirer.
     *
     * @param listener
     *            a party to notify when the input changes (usually the
     *            WizardDialog, which needs to know when to update the enabled
     *            state of the buttons.
     */
    public void add(InputChangedListener listener)
    {
        
    }
    
    public void remove(InputChangedListener listener)
    {
        
    }
    
    public String getTitle()
    {
        return this.getName();
    }
    
    public String getID()
    {
        return "2";
    }
    
    public String getInstructions()
    {
        return "";
    }
    
    public boolean isInputValid()
    {
        return true;
    }       
    
    public void setNextID(String nextID)
    {
        this.nextID = nextID;
    }
    
    /**
     * @return null to turn the Next button into a Finish button
     */
    public String getNextID()
    {
        return nextID;
    }
    
    public void setWizardContext(WizardContext wd)
    {        
    }
    
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }    
    
    public static Connection getDBConnection() throws SQLException
    {
        
        ApplicationContext app = AppContext.getApplicationContext();
        Connection conn = app.getConnection();
        conn.setAutoCommit(true);
        return conn;
    }
}
