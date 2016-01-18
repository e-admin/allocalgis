/**
 * GeopistaEnableCheckFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.ui.plugin;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.model.GeopistaSystemLayers;
import com.geopista.ui.dialogs.SelectLayerFieldPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;

public class GeopistaEnableCheckFactory extends EnableCheckFactory
{
  private WorkbenchContext workbenchContext;
  private ApplicationContext aplicacion=AppContext.getApplicationContext();
  public GeopistaEnableCheckFactory(WorkbenchContext workbenchContext)
  {
    super(workbenchContext);
    this.workbenchContext = workbenchContext;
  }

  public EnableCheck createAtLeastNGeopistaLayerablesMustBeSelectedCheck(final int n) {
        return createAtLeastNGeopistaLayerablesMustBeSelectedCheck(n, Layer.class);
    }

  public EnableCheck createAtLeastNGeopistaLayerablesMustBeSelectedCheck(
        final int n,
        final Class layerableClass) {
        return new EnableCheck() {
            public String check(JComponent component) {

                if(n>(workbenchContext.getLayerNamePanel()).selectedNodes(layerableClass).size())
                {
                  return ("At least " + n + " LocalGISLayer" + StringUtil.s(n) + " must be selected");
                }
                else
                {
                  Layerable[] capasSeleccionadas = (workbenchContext.getLayerNamePanel()).getSelectedLayers();
                  for(int p = 0; p < capasSeleccionadas.length; p++)
                  {
                    Layerable capaActual = capasSeleccionadas[p];
                    if(!(capaActual instanceof GeopistaLayer))
                    {
                      return ("No se pueden activar capas que no sean de LocalGIS");
                    }
                  }
                }
                return null;
           }
        };
    }

    /*
     * TODO:Hacer un metodo general para todas las capas y eliminar los específicos
     */
    public EnableCheck createLayerBeExistCheck(final String layerName) {
        return new EnableCheck() {
            public String check(JComponent component) {

                if(workbenchContext.getLayerManager().getLayer(layerName)==null)
                {
                  return (aplicacion.getI18nString("GeopistaEnableCheckFactory.DebeEstarCargadaCapaParcelas"));
                }
                
                return null;
           }
        };
    }

   public EnableCheck createLayerParcelasBeExistCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {

                if(workbenchContext.getLayerManager().getLayer(GeopistaSystemLayers.PARCELAS)==null)
                {
                  return (aplicacion.getI18nString("GeopistaEnableCheckFactory.DebeEstarCargadaCapaParcelas"));
                }
                
                return null;
           }
        };
    } 

    public EnableCheck createLayerStreetBeExistCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {

                if(workbenchContext.getLayerManager().getLayer(GeopistaSystemLayers.VIAS)==null)
                {
                  return (aplicacion.getI18nString("GeopistaEnableCheckFactory.DebeEstarCargadaCapaVias"));
                }
                
                return null;
           }
        };
    } 

        public EnableCheck createLayerNumerosPoliciaBeExistCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {

                if(workbenchContext.getLayerManager().getLayer(GeopistaSystemLayers.NUMEROSPOLICIA)==null)
                {
                  return (aplicacion.getI18nString("GeopistaEnableCheckFactory.DebeEstarCargadaCapaNumerosPolicia"));
                }

                return null;
           }
        };
    }
    
    public EnableCheck createLayerInmueblesBeExistCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {

                if(workbenchContext.getLayerManager().getLayer(GeopistaSystemLayers.INMUEBLES)==null)
                {
                  return (aplicacion.getI18nString("GeopistaEnableCheckFactory.DebeEstarCargadaCapaInmuebles"));
                }

                return null;
           }
        };
    }


    public EnableCheck createExactlyOneFeaturesInLayerParcelasMustBeSelectedCheck()
    {
      return createExactlyNFeaturesInLayerParcelasMustBeSelectedCheck(1,Layer.class);
    }
    
    public EnableCheck createExactlyNFeaturesInLayerParcelasMustBeSelectedCheck(
        final int n,
        final Class layerableClass) {
        return new EnableCheck() {
            public String check(JComponent component) {
                Layer parcelas = workbenchContext.getLayerManager().getLayer("parcelas");
                if(parcelas==null)
                {
	               	 parcelas = workbenchContext.getLayerManager().getLayer("parcelasurbana");
	                 if(parcelas==null)
	                 {
	                	 parcelas = workbenchContext.getLayerManager().getLayer("parcelasrustica");
	                     if(parcelas==null)
	                     {
	                       return "";
	                     }
	                 }

                }
                if(n!=workbenchContext.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(parcelas).size())
                {
                  return ("Exactamente Features han de ser selecccionadas en la capa de parcelas");
                }
                
                return null;
           }
        };
    }

    public EnableCheck createAtLeastNGeopistaFeaturesInNotParcelasLayerMustBeSelectedCheck(
        final int n,
        final Class layerableClass) {
        return new EnableCheck() {
            public String check(JComponent component) {
                Layer capaParcelas = workbenchContext.getLayerManager().getLayer(GeopistaSystemLayers.PARCELAS);
                if(capaParcelas==null) return "";
                
                Collection featuresSource = workbenchContext.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(capaParcelas);
                Collection features = workbenchContext.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();

                Iterator featuresSourceIter = featuresSource.iterator();
                Feature sourceFeature = (Feature) featuresSourceIter.next();

                //Eliminamos la parcelas de las features seleccionadas
                features.remove(sourceFeature);

                
                if(n>features.size())
                {
                  return ("Al menos "+n+"Features han de ser selecccionadas en la capa de parcelas");
                }
                
                return null;
           
                
           }
        };
    }
    
    public EnableCheck createAtLeastNLayerablesMustExistCheck(final int n) {
        return new EnableCheck() {
            public String check(JComponent component) {
                return (n > workbenchContext.getLayerManager().getLayerables(Layerable.class).size())
                    ? ("At least " + n + " layer" + StringUtil.s(n) + " must exist")
                    : null;
            }
        };
    }
    
    public EnableCheck createOnlyLocalLayersMustBeSelected()
    {
        return new EnableCheck()
        {
            public String check(JComponent component) {
                Layerable[] capasSeleccionadas = (workbenchContext.getLayerNamePanel()).getSelectedLayers();
                for(int p = 0; p < capasSeleccionadas.length; p++)
                {
                  Layerable capaActual = capasSeleccionadas[p];
                  if((capaActual instanceof GeopistaLayer))
                  {
                    GeopistaLayer currentGeopistaLayer = (GeopistaLayer) capaActual;
                    if(!currentGeopistaLayer.isLocal())
                    {
                        return (aplicacion.getI18nString("SoloCapasLocales"));
                    }
                  }
                }
                    
                return null;
            }
        };
    }
    
    public EnableCheck createWindowWithNoSystemMapMustBeActiveCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {
                if(workbenchContext.getIWorkbench().getGuiComponent().getActiveTaskComponent() instanceof LayerNamePanelProxy)
                {
                    if(workbenchContext.getIWorkbench().getGuiComponent().getActiveTaskComponent().getTask() instanceof GeopistaMap)
                    {
                        if(!((GeopistaMap) workbenchContext.getIWorkbench().getGuiComponent().getActiveTaskComponent().getTask()).isSystemMap())
                        {
                            return null;
                        }
                    }
                }
                
                
                return aplicacion.getI18nString("SincronizarNoMapaSistema");
            }
        };
    }
    
    public EnableCheck createWindowWithAutoCADMapMustBeActiveCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {
                if(workbenchContext.getIWorkbench().getGuiComponent().getActiveTaskComponent() instanceof LayerNamePanelProxy)
                {
                    if(workbenchContext.getIWorkbench().getGuiComponent().getActiveTaskComponent().getTask() instanceof GeopistaMap)
                    {
                        if(((GeopistaMap) workbenchContext.getIWorkbench().getGuiComponent().getActiveTaskComponent().getTask()).getName().endsWith(".dxf"))
                        {
                            return null;
                        }
                    }
                }
                
                
                return aplicacion.getI18nString("SincronizarNoMapaSistema");
            }
        };
    }
    
    public EnableCheck createWindowWithSystemMapMustBeActiveCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {
                if(workbenchContext.getIWorkbench().getGuiComponent().getActiveTaskComponent() instanceof LayerNamePanelProxy)
                {
                    if(workbenchContext.getIWorkbench().getGuiComponent().getActiveTaskComponent().getTask() instanceof GeopistaMap)
                    {
                        if(((GeopistaMap) workbenchContext.getIWorkbench().getGuiComponent().getActiveTaskComponent().getTask()).isSystemMap())
                        {
                            return null;
                        }
                    }
                }
                
                
                return aplicacion.getI18nString("DesconectarMapaSistema");
            }
        };
    }
    
        /*
     * Este metodo pretende hacer una condicion de aceptacion siempre y cuando este seleccionado
     * algun articulo del combobx de las capas para la clase 
     * Parametros
     *  *   comboLayer 
     *      Este parametro es un objeto combobox y comprueba si fue seleccionado 
     * */
    public EnableCheck createComboEnableCheck(final JComboBox comboLayer) {
        return new EnableCheck() {
            public String check(JComponent component) {
                   if(comboLayer.getSelectedItem()!=null){
                       return null;
                   }else{
                       return aplicacion.getI18nString("Seleccionarcapa");
                   }
            }
        };
    }
    /*
     * Este metodo pretende hacer una condicion de aceptacion siempre y cuando este seleccionado
     * algun articulo de una lista para un objeto de una clase concreta SelectLayerFieldPanel
     * */
    public EnableCheck createFieldPanelCheck(final SelectLayerFieldPanel FieldPanel) {
        return new EnableCheck() {
            public String check(JComponent component) {
                   if(FieldPanel.getSelectedFieldName()!=null){
                       return null;
                   }else{
                       return aplicacion.getI18nString("Seleccionaratributo");
                   }
            }
        };
    }
 

    
    public EnableCheck createWindowWithExtractMapMustBeActiveCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {
                if(workbenchContext.getIWorkbench().getGuiComponent().getActiveTaskComponent() instanceof LayerNamePanelProxy)
                {
                    if(workbenchContext.getIWorkbench().getGuiComponent().getActiveTaskComponent().getTask() instanceof GeopistaMap)
                    {
                        if(((GeopistaMap) workbenchContext.getIWorkbench().getGuiComponent().getActiveTaskComponent().getTask()).isExtracted())
                        {
                            return null;
                        }
                    }
                }
                
                
                return aplicacion.getI18nString("GeopistaEnableCheckFactory.ExtractMapBeActive");
            }
        };
    }
    
    public EnableCheck createAtLeastLayerWithColumnReferenciaCatastralBeExits() {
        return new EnableCheck() {
            public String check(JComponent component) {
                Collection refLayers = workbenchContext.getLayerManager().getLayers();
                Iterator refLayersIterator = refLayers.iterator();
                while(refLayersIterator.hasNext())
                {
                    Layer currentLayer = (Layer) refLayersIterator.next();
                    if(currentLayer instanceof GeopistaLayer)
                    {
                        FeatureSchema featureSchema = currentLayer.getFeatureCollectionWrapper().getFeatureSchema();
                        if(featureSchema instanceof GeopistaSchema)
                        {
                            GeopistaSchema currentSchema = (GeopistaSchema)featureSchema;
                            String attributeName = currentSchema.getAttributeByColumn("referencia_catastral");
                            if(attributeName!=null)
                            {
                                return null;
                            }
                        }
                    }
                    
                }
                return aplicacion.getI18nString("GeopistaEnableCheckFactory.LayerWithColumnReferenciaCatastralBeExist");
            }
        };
    }

	public EnableCheck createAtLeastOneOfTheseLayersExistCheck(final String[] sNames) {
        return new EnableCheck() {
            public String check(JComponent component) {
            	int n = sNames.length;
            	for (int i=0;i<n;i++){
	        		Layer layer = workbenchContext.getLayerManager().getLayer(sNames[i]);
	        		if (layer != null)
	        			return null;
            	}
            	return "None of these layers exist";
            }
        };
	}    

}