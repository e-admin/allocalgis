/**
 * GeopistaSaveDxfQueryChooser.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.io.dxf;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.geopista.ui.plugin.io.dxf.core.jump.io.DxfWriter;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.datasource.SaveFileDataSourceQueryChooser;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.PersistentBlackboardPlugIn;


public class GeopistaSaveDxfQueryChooser extends SaveFileDataSourceQueryChooser {
    private WorkbenchContext context;
    private static final String FILE_CHOOSER_DIRECTORY_KEY = SaveFileDataSourceQueryChooser.class.getName() +
        " - FILE CHOOSER DIRECTORY";

    public GeopistaSaveDxfQueryChooser(Class dataSourceClass, String description,
                                           String[] extensions, WorkbenchContext context){
            super(dataSourceClass,description,extensions,context);
            this.context=context;
        }

    public Collection getDataSourceQueries() {
        PersistentBlackboardPlugIn.get(context).put(FILE_CHOOSER_DIRECTORY_KEY,
            getFileChooserPanel().getChooser().getCurrentDirectory().toString());

        ArrayList queries = new ArrayList();
        File[] files = GUIUtil.selectedFiles(getFileChooserPanel().getChooser());

        for (int i = 0; i < files.length; i++) {
            queries.addAll(toDataSourceQueries(files[i]));
        }

        return queries;
    }

    protected Collection toDataSourceQueries(File file) {
        ArrayList alQueries=new ArrayList();
        alQueries.add(toDataSourceQuery(new DataSourceWrapper(context.getLayerNamePanel().getSelectedLayers(),file),file,""));
        return alQueries;
    }

    protected DataSourceQuery toDataSourceQuery(DataSource ds, File file, String sName) {
        HashMap properties=new HashMap();
        properties.put(DataSource.COORDINATE_SYSTEM_KEY,null);
        properties.put(DataSource.FILE_KEY,file.getPath());
        ds.setProperties(properties);
        return new DataSourceQuery(ds, file.getPath(),sName);
    }

    private class DataSourceWrapper extends DataSource{
        private Connection connection;
        private List layers;

        // TODO: Implementar las propiedades que necesita JUMP para el sistema de coordenadas
        public DataSourceWrapper(Layer[] aLayers, final File file){
            layers=Arrays.asList(aLayers);
            this.connection = new Connection(){

                public FeatureCollection executeQuery(String query, Collection exceptions, TaskMonitor monitor) {
                    return null;
                }

                public FeatureCollection executeQuery(String query, TaskMonitor monitor) throws Exception {
                    return null;
                }

                public ArrayList executeUpdate(String query, FeatureCollection featureCollection, TaskMonitor monitor) throws Exception {
                    
                    new DxfWriter().write(layers,(String) getProperties().get(DataSource.FILE_KEY));
                    return null;
                }

                public void close() {
                    // TODO: Cerrar fichero
                }

                public boolean isReadable(){
                    return false;
                }

                public boolean isWritable(){
                    return true;
                }

            };
        }
        public Connection getConnection() {
            return connection;
        }

    }//DataSourceWrapper
}
