/**
 * GeopistaImportarInfraFin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.infraestructuras;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.CodeBookDomain;
import com.geopista.feature.CodedEntryDomain;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.SchemaValidator;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaImportarInfraFin extends JPanel implements WizardPanel
{
  private AbstractValidator validator = new SchemaValidator(null);
  private ApplicationContext aplicacion = AppContext.getApplicationContext();  
  private Blackboard blackImportar =  aplicacion.getBlackboard();
  private JScrollPane scpErrores = new JScrollPane();
  private JEditorPane ediError = new JEditorPane();
  private GeopistaLayer tempLayer = null;
  private JPanel pnlVentana = new JPanel();
  private JLabel lblImportar = new JLabel();
  private GeopistaValidatePlugin geopistaValidatePlugIn = new GeopistaValidatePlugin();
  private GeopistaLayer capaSistema=null;
  private JLabel lblImagen = new JLabel();
  private GeopistaEditor geopistaEditor3 = null;
  private JSeparator jSeparator4 = new JSeparator();
  private JSeparator jSeparator5 = new JSeparator();
  private JLabel lblDatos = new JLabel();
  private boolean validaFeature;
  private int totalInsertados=0;
  private int totalNoInsertados=0;
  private String cadenaTexto;
  private String tipoInf;
  
  private WizardContext wizardContext;
  private static final int DUPLICAR = 0;
  private static final int BORRAR = 1;
  private static final int CANCELAR = 2;
  
  public GeopistaImportarInfraFin()
  {
    try
    {
      jbInit();
    }
    catch(Exception e)
     {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
      	setName(aplicacion.getI18nString("importar.infraestructura.titulo.2"));
		jSeparator4.setBounds(new Rectangle(135, 20, 605, 2));
		scpErrores.setBounds(new Rectangle(134,52,595,442));
		this.setLayout(null);
		
		
		
		lblDatos.setBounds(new Rectangle(135, 25, 260, 20));
		
		lblDatos.setText(aplicacion.getI18nString("importar.usuario.paso4.log"));
		lblImagen.setIcon(IconLoader.icon("infraestructuras.png"));
		lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
		lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		
		ediError.setContentType("text/html");
		this.setSize(750,600);
		
		jSeparator5.setBounds(new Rectangle(135, 505, 605, 2));
		scpErrores.getViewport().add(ediError, null);
		
		this.add(scpErrores,null);
		      this.add(lblDatos, null);
		      this.add(lblImagen, null);
		      this.add(jSeparator4, null);
		      this.add(jSeparator5, null);
		  }

  public void enteredFromLeft(Map dataMap)
  {
      wizardContext.previousEnabled(false);
      geopistaEditor3 = (GeopistaEditor) blackImportar.get("infraestructurasEditor");
      geopistaEditor3.addPlugIn(geopistaValidatePlugIn);
      
      final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
              .getMainFrame(), geopistaEditor3.getContext().getErrorHandler());

      progressDialog.setTitle(aplicacion.getI18nString("ImportandoDatos"));
      
      
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
                          boolean isMakeInsertion = geopistaValidatePlugIn.isMakeInsertion();
                          boolean firingEvents = false;
	                      try
	                      {
	                          DateFormat targetDateFormat = new SimpleDateFormat("dd-MM-yyyy");
						      String cadena="";
						      totalInsertados=0;
						      totalNoInsertados=0;
						      cadenaTexto = "";
						
						        boolean manualModification = ((Boolean) blackImportar.get("mostrarError")).booleanValue();
						        capaSistema = (GeopistaLayer) geopistaEditor3.getLayerManager().getLayer((String)blackImportar.get("nombreTabla"));
						        capaSistema.setActiva(false);
						        capaSistema.setVisible(true);
						        tempLayer = (GeopistaLayer) geopistaEditor3.getLayerManager().getLayer("sourceImportLayer");
						        tempLayer.setActiva(true);
						        tempLayer.setVisible(true);
						        
						        
						        int selectedValue = 0;
                                if (capaSistema.getFeatureCollectionWrapper().getFeatures().size() != 0)
                                {
                                    Object[] possibleValues = { aplicacion.getI18nString("GeopistaImportacionLog.Duplicar"), aplicacion.getI18nString("GeopistaImportacionLog.Borrar"), aplicacion.getI18nString("GeopistaImportacionLog.Cancelar") };
                                    selectedValue = JOptionPane.showOptionDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("GeopistaImportacionLog.LaCapaContieneFeatures"), aplicacion.getI18nString("GeopistaImportacionLog.BorrarDuplicar"), 0, JOptionPane.QUESTION_MESSAGE, null,
                                            possibleValues, possibleValues[0]);

                                }

                                if (selectedValue == CANCELAR)
                                {
                                    wizardContext.cancelWizard();
                                    return;
                                }
						        
						        FeatureSchema esquemaTemp = tempLayer.getFeatureCollectionWrapper().getFeatureSchema();
						        GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) capaSistema
					            .getDataSourceQuery().getDataSource();
						        
						        if (selectedValue == BORRAR)
                                {
                                    progressDialog.report(aplicacion.getI18nString("GeopistaImportarToponimosPanel.BorrandoRegistros"));
                                    Iterator lastFeaturesIter = capaSistema.getFeatureCollectionWrapper().getFeatures().iterator();
                                    while (lastFeaturesIter.hasNext())
                                    {
                                        GeopistaFeature deleteCurrentFeature = (GeopistaFeature) lastFeaturesIter.next();
                                        deleteCurrentFeature.setDeleted(true);
                                    }

                                    geopistaServerDataSource.getConnection().executeUpdate(capaSistema.getDataSourceQuery().getQuery(), capaSistema.getFeatureCollectionWrapper().getUltimateWrappee(), null);
                                    capaSistema.getFeatureCollectionWrapper().removeAll(capaSistema.getFeatureCollectionWrapper().getFeatures());

                                }
						        
			                    firingEvents = capaSistema.getLayerManager().isFiringEvents();
                                capaSistema.getLayerManager().setFiringEvents(false);

						
						        List listaLayer = tempLayer.getFeatureCollectionWrapper().getFeatures();
						        Iterator itLayer = listaLayer.iterator();
						        
						        geopistaValidatePlugIn.setMakeInsertion(false);
						        String nombreTabla = (String) blackImportar.get("nombreTabla");
					            String tipoFichero = (String)blackImportar.get("tipoF");
					            
					            
					            GeopistaSchema featureSchema = (GeopistaSchema) capaSistema.getFeatureCollectionWrapper().getFeatureSchema();
					            
					            if (nombreTabla.equals("piezas")|| nombreTabla.equals("tramosabastecimiento") ||
					                    nombreTabla.equals("colectores")|| nombreTabla.equals("tramossaneamiento") ||
					                    nombreTabla.equals("elementossaneamiento"))
					            {
					                SortedMap allNucleosMap = (SortedMap) blackImportar.get("totalNucleos");
					                CodeBookDomain cbNucleos=new CodeBookDomain("Nucleos","Todos los nucleos");
					                
					                Set allCodigosIne = allNucleosMap.keySet();
					                Iterator allCodigosIneIterator = allCodigosIne.iterator();
					                while(allCodigosIneIterator.hasNext())
					                {
					                    String currentCodigoIne = (String) allCodigosIneIterator.next();
					                    cbNucleos.addChild(new CodedEntryDomain(currentCodigoIne,currentCodigoIne));
					                }
					                
					                featureSchema.getColumnByAttribute(featureSchema.getAttributeByColumn("id_nucleo")).setDomain(cbNucleos);
					            }
					            
					                    
					                
						        
						        while (itLayer.hasNext())
						        {
						            try
						            {
					                
						                 
						            progressDialog.report(totalInsertados+totalNoInsertados,listaLayer.size(),aplicacion.getI18nString("ImportandoEntidad"));
						          Feature f = (Feature)itLayer.next();
						          
						          
						          GeopistaFeature featureToAdd = new GeopistaFeature(featureSchema);
						          featureToAdd.setGeometry(f.getGeometry());//geometria
						          if (nombreTabla.equals("captaciones"))
						          {
						           	if (tipoFichero.equals("jml"))
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("denominacion"),getTrimAttributeValue(f,"Denominacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipocaptacion"),getTrimAttributeValue(f,"TipoCaptacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipotitular"),getTrimAttributeValue(f,"TipoTitular"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),(Date)f.getAttribute("FechaInstalacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipogestion"),getTrimAttributeValue(f,"TipoGestion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("sistemacaptacion"),getTrimAttributeValue(f,"SistemaCaptacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoestado"),getTrimAttributeValue(f,"TipoEstado"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("id_subcuenca"),f.getAttribute("ID_Subcuenca"));
						            }
						            else
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("denominacion"),getTrimAttributeValue(f,"DENOMINA"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipocaptacion"),getTrimAttributeValue(f,"TIPCAP"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipotitular"),getTrimAttributeValue(f,"TITCAP"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),(Date)f.getAttribute("FECHA"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipogestion"),getTrimAttributeValue(f,"GESCAP"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("sistemacaptacion"),getTrimAttributeValue(f,"SISCAP"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoestado"),getTrimAttributeValue(f,"ESTCAP"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("id_subcuenca"),f.getAttribute("IDSUB"));
						            }
						          }
						          if (nombreTabla.equals("piezas"))
						          {
						           	if (tipoFichero.equals("jml"))
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("id_nucleo"),getTrimAttributeValue(f,"ID_Nucleo"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("seccion"),getTrimAttributeValue(f,"Seccion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipo"),getTrimAttributeValue(f,"Tipo"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),(Date)f.getAttribute("FechaInstalacion"));
							          
						            }
						            else
						            {
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("id_nucleo"),getTrimAttributeValue(f,"IDNUC"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("seccion"),getTrimAttributeValue(f,"SECCION"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipo"),getTrimAttributeValue(f,"TIPO"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),(Date)f.getAttribute("FECHA"));
						            }
						          }
						          if (nombreTabla.equals("potabilizadoras"))
						          {
						           	if (tipoFichero.equals("jml"))
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipotratamiento"),getTrimAttributeValue(f,"TipoTratamiento"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoubicacion"),getTrimAttributeValue(f,"TipoUbicacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),f.getAttribute("FechaInstalacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("floculacion"),f.getAttribute("Floculacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("decantacion"),f.getAttribute("Decantacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("filtracion"),f.getAttribute("Filtracion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("esterilizacion"),f.getAttribute("Esterilizacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("otros"),f.getAttribute("Otros"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoperiodicidad"),getTrimAttributeValue(f,"TipoPeriodicidad"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoorganismocontrol"),getTrimAttributeValue(f,"TipoOrganismoControl"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoestado"),getTrimAttributeValue(f,"TipoEstado"));
							            
						            }
						            else
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipotratamiento"),getTrimAttributeValue(f,"TRAPOT"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoubicacion"),getTrimAttributeValue(f,"TIPPOT"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),(Date) f.getAttribute("FECHA"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("floculacion"),f.getAttribute("FLOCUL"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("decantacion"),f.getAttribute("DECANT"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("filtracion"),f.getAttribute("FILTRAC"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("esterilizacion"),f.getAttribute("ESTERIL"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("otros"),f.getAttribute("OTROS"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoperiodicidad"),getTrimAttributeValue(f,"PERPOT"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoorganismocontrol"),getTrimAttributeValue(f,"ORGPOT"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoestado"),getTrimAttributeValue(f,"ESTPOT"));
						            }
						          }
						          if (nombreTabla.equals("depositos"))
						          {
						          	if (tipoFichero.equals("jml"))
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoubicacion"),getTrimAttributeValue(f,"TipoUbicacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipotitular"),getTrimAttributeValue(f,"TipoTitular"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),(Date) f.getAttribute("FechaInstalacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipogestion"),getTrimAttributeValue(f,"TipoGestion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoestado"),getTrimAttributeValue(f,"TipoEstado"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("capacidad"),f.getAttribute("Capacidad"));
						            }
						            else
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoubicacion"),getTrimAttributeValue(f,"TIPDEP"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipotitular"),getTrimAttributeValue(f,"TITDEP"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),(Date) f.getAttribute("FECHA"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipogestion"),getTrimAttributeValue(f,"GESDEP"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoestado"),getTrimAttributeValue(f,"ESTDEP"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("capacidad"),f.getAttribute("CAPDEP"));
						            }
						          }
						          if (nombreTabla.equals("tramosabastecimiento"))
						          {
						 
						          	if (tipoFichero.equals("jml"))
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("id_nucleo"),getTrimAttributeValue(f,"ID_Nucleo"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoestado"),getTrimAttributeValue(f,"TipoEstado"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipomaterial"),getTrimAttributeValue(f,"TipoMaterial"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipotitular"),getTrimAttributeValue(f,"TipoTitular"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),f.getAttribute("FechaInstalacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipogestion"),getTrimAttributeValue(f,"TipoGestion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("direccion"),getTrimAttributeValue(f,"Direccion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("diametro"),getTrimAttributeValue(f,"Diametro"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("presiontrabajo"),getTrimAttributeValue(f,"PresionTrabajo"));
						            }
						            else
						            {
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("id_nucleo"),getTrimAttributeValue(f,"IDNUC"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoestado"),getTrimAttributeValue(f,"ESTDIS"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipomaterial"),getTrimAttributeValue(f,"MATDIS"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipotitular"),getTrimAttributeValue(f,"TITDIS"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),f.getAttribute("FECHA"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipogestion"),getTrimAttributeValue(f,"GESDIS"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("direccion"),getTrimAttributeValue(f,"DIRECCION"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("diametro"),getTrimAttributeValue(f,"DIAMETRO"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("presiontrabajo"),getTrimAttributeValue(f,"PRESION"));
						            }
						          }
						          if (nombreTabla.equals("conducciones"))
						          {
						           
						          	if (tipoFichero.equals("jml"))
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoestado"),getTrimAttributeValue(f,"TipoEstado"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipomaterial"),getTrimAttributeValue(f,"TipoMaterial"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipotitular"),getTrimAttributeValue(f,"TipoTitular"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),f.getAttribute("FechaInstalacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipogestion"),getTrimAttributeValue(f,"TipoGestion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("direccion"),f.getAttribute("Direccion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("diametro"),f.getAttribute("Diametro"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("presiontrabajo"),f.getAttribute("PresionTrabajo"));
						            }
						            else
						            {
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoestado"),getTrimAttributeValue(f,"ESTCON"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipomaterial"),getTrimAttributeValue(f,"MATCON"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipotitular"),getTrimAttributeValue(f,"TITCON"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),f.getAttribute("FECHA"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipogestion"),getTrimAttributeValue(f,"GESCON"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("direccion"),f.getAttribute("DIRECCION"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("diametro"),f.getAttribute("DIAMETRO"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("presiontrabajo"),f.getAttribute("PRESION"));
						            }
						          }
						          
						          if (nombreTabla.equals("saneamientoautonomo"))
						          {
						            
						          	if (tipoFichero.equals("jml"))
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),f.getAttribute("FechaInstalacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoestado"),getTrimAttributeValue(f,"TipoEstado"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipomotivodeficit"),f.getAttribute("TipoMotivoDeficit"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tiposaneaauto"),getTrimAttributeValue(f,"TipoSaneaAuto"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("esadecuado"),f.getAttribute("EsAdecuado"));
						            }
						            else
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),f.getAttribute("FECHA"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoestado"),getTrimAttributeValue(f,"ESTSAU"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipomotivodeficit"),f.getAttribute("MOTIVO"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tiposaneaauto"),getTrimAttributeValue(f,"TIPO"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("esadecuado"),f.getAttribute("ADECUADO"));
							          
						            }
						          }
						          if (nombreTabla.equals("colectores"))
						          {
						          
						          	if (tipoFichero.equals("jml"))
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("id_nucleo"),getTrimAttributeValue(f,"ID_Nucleo"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipomaterial"),getTrimAttributeValue(f,"TipoMaterial"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("direccion"),f.getAttribute("Direccion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),f.getAttribute("FechaInstalacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoestado"),getTrimAttributeValue(f,"TipoEstado"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipotitular"),getTrimAttributeValue(f,"TipoTitular"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipogestion"),getTrimAttributeValue(f,"TipoGestion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("diametro"),f.getAttribute("Diametro"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("presiontrabajo"),f.getAttribute("PresionTrabajo"));
						            }
						            else
						            {
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("id_nucleo"),getTrimAttributeValue(f,"IDNUC"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipomaterial"),getTrimAttributeValue(f,"MATCOL"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("direccion"),f.getAttribute("DIRECCION"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),f.getAttribute("FECHA"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoestado"),getTrimAttributeValue(f,"ESTCOL"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipotitular"),getTrimAttributeValue(f,"TITCOL"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipogestion"),getTrimAttributeValue(f,"GESCOL"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("diametro"),f.getAttribute("DIAMETRO"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("presiontrabajo"),f.getAttribute("PRESION"));
						            }
					          }
						          if (nombreTabla.equals("tramossaneamiento"))
						          {
						            
						          	if (tipoFichero.equals("jml"))
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("id_nucleo"),getTrimAttributeValue(f,"ID_Nucleo"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipomaterial"),getTrimAttributeValue(f,"TipoMaterial"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("direccion"),f.getAttribute("Direccion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),(Date) f.getAttribute("FechaInstalacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoestado"),getTrimAttributeValue(f,"TipoEstado"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipotitular"),getTrimAttributeValue(f,"TipoTitular"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipogestion"),getTrimAttributeValue(f,"TipoGestion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("diametro"),f.getAttribute("Diametro"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("presiontrabajo"),f.getAttribute("PresionTrabajo"));
						            }
						            else
						            {
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("id_nucleo"),getTrimAttributeValue(f,"IDNUC"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipomaterial"),getTrimAttributeValue(f,"MATSAN"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("direccion"),f.getAttribute("DIRECCION"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),(Date) f.getAttribute("FECHA"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoestado"),getTrimAttributeValue(f,"ESTSAN"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipotitular"),getTrimAttributeValue(f,"TITSAN"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipogestion"),getTrimAttributeValue(f,"GESSAN"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("diametro"),f.getAttribute("DIAMETRO"));
						 	            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("presiontrabajo"),f.getAttribute("PRESION"));
						            }

						          }
						          
						          if (nombreTabla.equals("emisarios"))
						          {
						            
						          	if (tipoFichero.equals("jml"))
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("denominacion"),getTrimAttributeValue(f,"Denominacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipomaterial"),getTrimAttributeValue(f,"TipoMaterial"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoestado"),getTrimAttributeValue(f,"TipoEstado"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipovertido"),getTrimAttributeValue(f,"TipoVertido"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipotitular"),getTrimAttributeValue(f,"TipoTitular"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipogestion"),getTrimAttributeValue(f,"TipoGestion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),f.getAttribute("FechaInstalacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("longitudterrestre"),f.getAttribute("LongitudTerrestre"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("longitudmaritima"),f.getAttribute("LongitudMaritima"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("direccion"),f.getAttribute("Direccion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("diametro"),f.getAttribute("Diametro"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("distanciavertido"),f.getAttribute("DistanciaVertido"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("presiontrabajo"),f.getAttribute("PresionTrabajo"));
						            }
						            else
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("denominacion"),getTrimAttributeValue(f,"DNVEMI"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipomaterial"),getTrimAttributeValue(f,"MATEMI"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipoestado"),getTrimAttributeValue(f,"ESTEMI"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipovertido"),getTrimAttributeValue(f,"TVREMI"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipotitular"),getTrimAttributeValue(f,"TITEMI"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipogestion"),getTrimAttributeValue(f,"GESEMI"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),f.getAttribute("FECHA"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("longitudterrestre"),f.getAttribute("LONEMI"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("longitudmaritima"),f.getAttribute("LNMEMI"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("direccion"),f.getAttribute("DIRECCION"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("diametro"),f.getAttribute("DIAMETRO"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("distanciavertido"),f.getAttribute("DVREMI"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("presiontrabajo"),f.getAttribute("PRESION"));
						            }
						          }
						          if (nombreTabla.equals("depuradoras"))
						          {
						             
						          	if (tipoFichero.equals("jml"))
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipotitular"),getTrimAttributeValue(f,"TipoTitular"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipogestion"),getTrimAttributeValue(f,"TipoGestion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),f.getAttribute("FechaInstalacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tiposistemadepuradora"),getTrimAttributeValue(f,"TipoSistemaDepuradora"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("capacidad"),f.getAttribute("Capacidad"));
						            }
						            else
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipotitular"),getTrimAttributeValue(f,"TITDEP"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipogestion"),getTrimAttributeValue(f,"GESDEP"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),f.getAttribute("FECHA"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tiposistemadepuradora"),getTrimAttributeValue(f,"SISDEP"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("capacidad"),f.getAttribute("CAPDEP"));
						            }
						            
						
						          }
						          if (nombreTabla.equals("elementossaneamiento"))
						          {
						              
						          	if (tipoFichero.equals("jml"))
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("id_nucleo"),getTrimAttributeValue(f,"ID_Nucleo"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("cota"),f.getAttribute("Cota"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("seccion"),f.getAttribute("Seccion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),f.getAttribute("FechaInstalacion"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipo"),getTrimAttributeValue(f,"Tipo"));
						            }
						            else
						            {
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("id_nucleo"),getTrimAttributeValue(f,"IDNUC"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("cota"),f.getAttribute("COTA"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("seccion"),f.getAttribute("SECCION"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("FechaInstalacion"),f.getAttribute("FECHA"));
							            featureToAdd.setAttribute(featureSchema.getAttributeByColumn("tipo"),getTrimAttributeValue(f,"TIPO"));
						            }
						            
						            
						
						          }
						          
						          ((GeopistaFeature) featureToAdd).setLayer(capaSistema);
						          boolean validateResult = false;
						          boolean cancelImport = false;

                                  while (!(validateResult = validator.validateFeature(featureToAdd)))
                                  {
                                      if (!manualModification)
                                      {
                                          break;
                                      }
                                      FeatureDialog featureDialog = GeopistaUtil.showFeatureDialog(capaSistema, featureToAdd);
                                      if (featureDialog.wasOKPressed())
                                      {
                                          Feature clonefeature = featureDialog.getModifiedFeature();
                                          featureToAdd.setAttributes(clonefeature.getAttributes());
                                      } else
                                      {
                                          Object[] possibleValues = { aplicacion.getI18nString("CancelarEsteElemento"), aplicacion.getI18nString("CancelarTodaLaImportacion"), aplicacion.getI18nString("IgnorarFuturosErrores") };
                                          int selectedValueCancel = JOptionPane.showOptionDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("GeopistaImportacionLog.LaCapaContieneFeatures"), aplicacion.getI18nString("GeopistaImportacionLog.BorrarDuplicar"), 0, JOptionPane.QUESTION_MESSAGE, null,
                                                  possibleValues, possibleValues[0]);
                                          if(selectedValueCancel==2) manualModification=false;
                                          if(selectedValueCancel==1) cancelImport=true;
                                          break;
                                      }

                                  }

                                  if (validateResult)
                                  {
                                      totalInsertados++;
                                  } else
                                  {
                                      totalNoInsertados++;
                                  }
                                  
                                  if(cancelImport==true) break;
                                  
                                  if (validateResult)
                                  {
                                      capaSistema
                                              .getFeatureCollectionWrapper()
                                              .add(featureToAdd);
                                  }

                              } catch (Exception e)
                              {
                                  totalNoInsertados = totalNoInsertados + 1;
                                  e.printStackTrace();
                              }
						          
						      }//while
						        progressDialog.report(aplicacion.getI18nString("GrabandoDatosBaseDatos"));
						        Map driverProperties = geopistaServerDataSource.getProperties();
                                Object lastResfreshValue = driverProperties.get(Constantes.REFRESH_INSERT_FEATURES);
                               
						        try
                                {
                                    driverProperties.put(Constantes.REFRESH_INSERT_FEATURES, new Boolean(false));

                                    geopistaServerDataSource.getConnection().executeUpdate(capaSistema.getDataSourceQuery().getQuery(), capaSistema.getFeatureCollectionWrapper().getUltimateWrappee(), progressDialog);
                                } finally
                                {
                                    if (lastResfreshValue != null)
                                    {
                                        driverProperties.put(Constantes.REFRESH_INSERT_FEATURES, lastResfreshValue);
                                    } else
                                    {
                                        driverProperties.remove(Constantes.REFRESH_INSERT_FEATURES);
                                    }
                                }
					      }catch(Exception exc)
					      {
					          exc.printStackTrace();
                              JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("SeHanProducidoErrores"));
                              wizardContext.cancelWizard();
                              return;
					      }finally
					      {
					          progressDialog.setVisible(false);
					          geopistaValidatePlugIn.setMakeInsertion(isMakeInsertion);
					          capaSistema.getLayerManager().setFiringEvents(false);
					      }
                      }
                  }).start();
          }
      });
      GUIUtil.centreOnWindow(progressDialog);
      progressDialog.setVisible(true);

      finalMessage((String)blackImportar.get("nombreTabla"));
  }
  
  private void finalMessage(String modulo)
  {
      DateFormat formatter = new SimpleDateFormat("dd-MMM-yy hh:mm:ss");
      String date = (String) formatter.format(new Date());

      cadenaTexto = "<p><b>" + aplicacion.getI18nString(modulo) + "</b></p>";
      cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("GeopistaImportarInfraFin.fecha") + ":</b>" + date + "</p>";
      cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.usuario.paso4.Registros")+ ":</b>" + blackImportar.get("numeroRegistros").toString() + "</p>";
      cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.usuario.paso4.Insertados")+ ":</b>" + totalInsertados + "</p>";
      cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.usuario.paso4.NoInsertados")+ ":</b>" + totalNoInsertados + "</p>";
      ediError.setText(cadenaTexto);
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        capaSistema = null;
        geopistaEditor3 = null;
    }

    /**
     * Tip: Delegate to an InputChangedFirer.
     * @param listener a party to notify when the input changes (usually the
     * WizardDialog, which needs to know when to update the enabled state of
     * the buttons.
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
      return "3";
    }

    public String getInstructions()
    {
     return "";
    }

    public boolean isInputValid()
    {
      return true;
    }

    private String nextID=null;
    
    public void setNextID(String nextID)
    {
    	this.nextID=nextID;
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
         this.wizardContext = wd;
    }
     
    private String getTrimAttributeValue(Feature currentfeature, String attributeName)
    {
        return currentfeature.getString(attributeName).trim();
    }

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }
     

}

