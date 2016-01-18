/**
 * GeopistaAddNewLayerPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin;

import java.util.Collection;

import com.geopista.app.AppContext;
import com.geopista.feature.Column;
import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.StringDomain;
import com.geopista.feature.Table;
import com.geopista.model.GeopistaLayer;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class GeopistaAddNewLayerPlugIn extends AbstractPlugIn
{

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();

    public GeopistaAddNewLayerPlugIn()
        {
        }

    public static FeatureCollection createBlankFeatureCollection()
    {
        GeopistaSchema featureSchema = new GeopistaSchema();

        Domain domainGeometry = new StringDomain("", "");
        Table tableGeometry = new Table("Dummy", "Dummy");
        Column columnGeometry = new Column("Dummy", "Dummy", tableGeometry,
                domainGeometry);
        featureSchema.addAttribute("GEOMETRY", AttributeType.GEOMETRY, columnGeometry,
                GeopistaSchema.READ_WRITE);

        return new FeatureDataset(featureSchema);
    }

    public boolean execute(PlugInContext context) throws Exception
    {
        Collection selectedCategories = context.getLayerNamePanel()
                .getSelectedCategories();

        String actualName = aplicacion.getI18nString("GeopistaAddNewLayerPlugIn.New");

        GeopistaLayer layer = new GeopistaLayer(actualName, context.getLayerManager()
                .generateLayerFillColor(), createBlankFeatureCollection(), context
                .getLayerManager());
        // Como es una capa creada localmente asignamos el flag isLocal a true
        layer.setLocal(true);

        // com.geopista.io.datasource.GeopistaStandarReaderWriteFileDataSource.GeoGML
        // h = new
        // com.geopista.io.datasource.GeopistaStandarReaderWriteFileDataSource.GeoGML();
        // layer.setDataSourceQuery(new DataSourceQuery());

        context.getLayerManager().addLayer(
                selectedCategories.isEmpty() ? StandardCategoryNames.WORKING
                        : selectedCategories.iterator().next().toString(), layer);

        ((EditingPlugIn) context.getWorkbenchContext().getBlackboard().get(
                EditingPlugIn.KEY)).getToolbox(context.getWorkbenchContext()).setVisible(
                true);

        return true;
    }

    public void initialize(PlugInContext context) throws Exception
    {

        FeatureInstaller featureInstaller = new FeatureInstaller(context
                .getWorkbenchContext());

        featureInstaller.addPopupMenuItem(context.getWorkbenchContext().getIWorkbench()
                .getGuiComponent().getCategoryPopupMenu(), this, GeopistaFunctionUtils.i18n_getname(this
                .getName()), false, null, null);

        featureInstaller.addLayerViewMenuItem(this, GeopistaFunctionUtils.i18n_getname("Layer"),
        		GeopistaFunctionUtils.i18n_getname(this.getName()));

    }

}
