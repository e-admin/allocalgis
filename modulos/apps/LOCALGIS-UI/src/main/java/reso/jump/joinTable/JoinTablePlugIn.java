/**
 * JoinTablePlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @author Olivier BEDEL
 * 	Laboratoire RESO UMR 6590 CNRS
 * 	Bassin Versant du Jaudy-Guindy-Bizien
 * 	26 oct. 2004
 * 
 */
package reso.jump.joinTable;

import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.georeference.FileGeoreferencePlugIn;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

/**
 * @author Olivier BEDEL
 * 	Laboratoire RESO UMR 6590 CNRS
 * 	Bassin Versant du Jaudy-Guindy-Bizien
 * 	26 oct. 2004
 * 
 */
public class JoinTablePlugIn extends AbstractPlugIn {
	protected static String name = "JoinTable"; 
	public static FileFilter JOIN_TABLE_FILE_FILTER = null; 
	private GeopistaLayer layer;
	private JFileChooser fileChooser;
	private MultiInputDialog dialog;
	private String LAYER_ATTRIBUTES = null;
	private String TABLE_ATTRIBUTES = null;

	public void initialize(PlugInContext context) throws Exception {
		//WorkbenchContext workbenchContext = context.getWorkbenchContext().getWorkbench().getFrame().getLayerNamePopupMenu();
		//I18N.setPlugInRessource(name, "reso.jump.joinTable.joinTable");
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        featureInstaller.addMainMenuItem(this,
                new String[]{"Tools", AppContext.getApplicationContext().getI18nString("ui.MenuNames.TOOLS.ANALYSIS")},
                AppContext.getApplicationContext().getI18nString("titleJoinTable"), 
                false,
                null,
                FileGeoreferencePlugIn.createEnableCheck(context.getWorkbenchContext()));
		I18NPlug.setPlugInRessource(JoinTablePlugIn.name, "reso.jump.joinTable.resources.joinTable");
		
		// initialisation du filtre de fichier
		JoinTablePlugIn.JOIN_TABLE_FILE_FILTER = GUIUtil.createFileFilter(I18NPlug.get(JoinTablePlugIn.name, "text_file"), new String[]{"txt", "text"});
		LAYER_ATTRIBUTES = I18NPlug.get(JoinTablePlugIn.name, "layer_field");
		TABLE_ATTRIBUTES = I18NPlug.get(JoinTablePlugIn.name,"table_field");
		
//		context.getFeatureInstaller().addPopupMenuItem(context.getWorkbenchContext().getWorkbench().getFrame().getLayerNamePopupMenu(), 
//			this, I18NPlug.get(JoinTablePlugIn.name, "Join_data") , false, null, new EnableCheckFactory(context.getWorkbenchContext()).createAtLeastNLayersMustExistCheck(1));
			//Joindre des données -> I18N.get(name,"reso.jump.joinTable.JoinTablePlugin.MenuName")
		fileChooser = GUIUtil.createJFileChooserWithExistenceChecking();
		fileChooser.setDialogTitle(I18NPlug.get(JoinTablePlugIn.name, "Choose_file_data_to_join"));
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		GUIUtil.removeChoosableFileFilters(fileChooser);
		fileChooser.addChoosableFileFilter(JOIN_TABLE_FILE_FILTER);
		fileChooser.addChoosableFileFilter(GUIUtil.ALL_FILES_FILTER);
		fileChooser.setFileFilter(JOIN_TABLE_FILE_FILTER);
	}

	public boolean execute(PlugInContext context) throws Exception {
		reportNothingToUndoYet(context);
		
		layer = (GeopistaLayer) context.getCandidateLayer(0);
		if (layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeCount()==0){
			ErrorDialog.show(context.getWorkbenchFrame(), I18NPlug.get(JoinTablePlugIn.name, "Unable_to_join_data"), I18NPlug.get(JoinTablePlugIn.name,"Layer_has_no_field"), "");
			return false; 
		}
		if (JFileChooser.APPROVE_OPTION != fileChooser.showOpenDialog(context
					.getWorkbenchFrame())) {
			return false;
		}
		
		JoinTable jt = new JoinTable(fileChooser.getSelectedFile().getAbsolutePath());
		
		initDialog(context,jt,layer);
		dialog.setVisible(true);
		if (!dialog.wasOKPressed()) {
			jt.dispose();
			return false;
		}
		//System.out.println("indice du champ de la table : " + dialog.getComboBox(TABLE_ATTRIBUTES).getSelectedIndex() );
		//System.out.println("indice du champ de la couche : " + dialog.getComboBox(LAYER_ATTRIBUTES).getSelectedIndex() );
		jt.setKeyIndex(dialog.getComboBox(TABLE_ATTRIBUTES).getSelectedIndex());
		jt.build();
		//jointure sur la couche en memoire
		jt.join(layer,dialog.getComboBox(LAYER_ATTRIBUTES).getSelectedIndex());
		//liberation memoire
		jt.dispose();
		System.gc(); 
		return true;
	}
	
	private void initDialog(PlugInContext context, JoinTable jt, Layer l) {
		FeatureSchema schema = layer.getFeatureCollectionWrapper().getFeatureSchema();
		ArrayList layerAttributes = new ArrayList(schema.getAttributeCount());
		
		for (int i=0; i<schema.getAttributeCount(); i++)
			layerAttributes.add(i,schema.getAttributeName(i));
		
		
		dialog = new MultiInputDialog(context.getWorkbenchFrame(), I18NPlug.get(JoinTablePlugIn.name, "Matching_fields"), true);
		//dialog.setSideBarImage(IconLoader.icon("hydroLoad.gif"));
		dialog.setSideBarDescription(I18NPlug.get(JoinTablePlugIn.name, "Choose_fields_to_join"));
				
		dialog.addComboBox(LAYER_ATTRIBUTES,layerAttributes.get(0), layerAttributes, null);
		dialog.addComboBox(TABLE_ATTRIBUTES,jt.getFieldName(0), jt.getFieldNames(), null);
		GUIUtil.centreOnWindow(dialog);
	}
}
