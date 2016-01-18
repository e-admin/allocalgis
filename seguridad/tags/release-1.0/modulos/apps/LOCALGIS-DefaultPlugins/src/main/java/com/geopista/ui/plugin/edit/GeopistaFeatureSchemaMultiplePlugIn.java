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
package com.geopista.ui.plugin.edit;

import java.awt.Frame;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

/**
 * GeopistaFeatureSchemaMultiplePlugIn
 * Plugin que permite editar múltiples features simultáneamente
 */

public class GeopistaFeatureSchemaMultiplePlugIn extends AbstractPlugIn
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
												.getLog(GeopistaFeatureSchemaMultiplePlugIn.class);

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
    
  static public final ImageIcon ICON=IconLoader.icon("SheetMultiple.gif");
  private String toolBarCategory = "GeopistaFeatureSchemaMultiplePlugIn.category";
  
	 public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
            .add(checkFactory.createWindowWithSelectionManagerMustBeActiveCheck())
            .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())            
            .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
            .add(checkFactory.createExactlyNLayersMustBeSelectedCheck(1))
            .add(checkFactory.createOnlyOneLayerMayHaveSelectedFeaturesCheck())
            .add(checkFactory.createAtLeastNItemsMustBeSelectedCheck(1));

    }


  
  public void initialize(PlugInContext context) throws Exception
  {
        String pluginCategory = aplicacion.getString(toolBarCategory);
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(
            getIcon(), this,
            createEnableCheck(context.getWorkbenchContext()),
        context.getWorkbenchContext());


        JPopupMenu popupMenu = context.getWorkbenchGuiComponent().getLayerViewPopupMenu();
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());

       
        featureInstaller.addPopupMenuItem(popupMenu, this,
        		aplicacion.getI18nString(getName()), false,
				GUIUtil.toSmallIcon(ICON),
				createEnableCheck(context.getWorkbenchContext()));
      
    
  }
  /**
   * execute (PlugInContext context)
   * Al ejecutarse el plugin se van recuperando una a una las features, cada feature es clonada
   * para poder recuperar su esquema. Este esquema se muestra en un dialog desde el cual, rellenando
   * los atributos deseados, podemos establecer el valor de dichos atributos para todas las features
   * seleccionadas
   * 
   * @param context : Contexto del PlugIn
   * @return Devuelve false si no se ha podido ejecutar la operación, true en caso contrario
   */
  public boolean execute (PlugInContext context) throws Exception
  {
      List capasVisibles = context.getWorkbenchContext().getLayerNamePanel().getLayerManager().getVisibleLayers(true);
      Iterator capasVisiblesIter = capasVisibles.iterator();
      boolean cancelWhile = false;
      while(capasVisiblesIter.hasNext())
      {
        Layer capaActual = (Layer) capasVisiblesIter.next();

        Collection featuresSeleccionadas = context.getWorkbenchContext().getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(capaActual);

        if(featuresSeleccionadas.size()!=0){

                    Iterator featuresSeleccionadasIter = featuresSeleccionadas.iterator();
                    // RECUPERAMOS UNA FEATURE Y LA CLONAMOS, LA CLONADA ES LA QUE PASAMOS AL DIALOG
                    Feature localFeature = (Feature) featuresSeleccionadasIter.next();
                    Feature nullFeature = localFeature.clone(true);
                    nullFeature.setSchema(localFeature.getSchema());

                    // LA FEATURE CLONADA LA VACIAMOS
                    for(int j=0;j<localFeature.getAttributes().length;j++){
                      if(!localFeature.getSchema().getAttributeType(j).equals(AttributeType.GEOMETRY))
                      {
                         nullFeature.setAttribute(j,null);
                      }
                    }

                    FeatureDialog featureDialog = new FeatureDialog((Frame)GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()),"Atributos",true,null,(LayerViewPanel)context.getWorkbenchContext().getLayerViewPanel(),(Layer)capaActual);
      
                    
                    try
                    {
                      ImageIcon icon=IconLoader.icon("logo_geopista.png");
                      featureDialog.setSideBarImage(null);
                    }catch(NullPointerException e)
                    {
					logger.error("execute(PlugInContext)", e);
                    }
          
                    if(capaActual instanceof GeopistaLayer)
                    {
                      String extendedForm = ((GeopistaLayer) capaActual).getFieldExtendedForm();
                      if(extendedForm==null) extendedForm="";
                      if(!extendedForm.equals(""))
                      {
                        featureDialog.setExtendedForm(extendedForm);
                      }
                    }

                        featureDialog.buildDialog();
                    featureDialog.setVisible(true);
          

                    //solo para GeopistaLayer
          
                    if (featureDialog.wasOKPressed())
                    {
                       
                     

                        Iterator featuresSeleccionadasIter2 = featuresSeleccionadas.iterator();
                        while(featuresSeleccionadasIter2.hasNext())
                        {
                            //obtenemos la feature con los cambios introducidos por el usuario
                            Feature cloneFeature = featureDialog.getModifiedFeature();
                            Feature localFeature2 = (Feature) featuresSeleccionadasIter2.next();   
                  
                            // Actualiza los parámetros
                            for(int k=0;k<cloneFeature.getAttributes().length;k++)
                            {
                              if((!localFeature2.getSchema().getAttributeType(k).equals(AttributeType.GEOMETRY))&&(!localFeature2.getSchema().getAttributeType(k).equals(AttributeType.DATE))){
                                  if(cloneFeature.getAttribute(k)!=null && !cloneFeature.getAttribute(k).equals(""))
                                  {
									if (logger.isDebugEnabled())
										{
										logger
												.debug("execute(PlugInContext)"
														+ localFeature2
																.getAttribute("ID_Elemento")
														+ ".-Actualizado el atributo de :"
														+ featureDialog
																.getModifiedFeature()
																.getAttribute(k));
										}
                                    localFeature2.setAttribute(k,cloneFeature.getAttribute(k));
                                  }
                              }
                            }
                            
                        }
                    }
                    else
                    {
                      // el usuario ha pedido cancelar la edición
                      cancelWhile = true;
                    }
          }
      }

      return false;
  }
  
   public ImageIcon getIcon() {
        return ICON;
    }

  
}
