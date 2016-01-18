/**
 * QueryDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package fr.michaelm.jump.query;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import buoy.event.CommandEvent;
import buoy.event.MouseEnteredEvent;
import buoy.event.ValueChangedEvent;
import buoy.event.WindowClosingEvent;
import buoy.widget.BButton;
import buoy.widget.BCheckBox;
import buoy.widget.BComboBox;
import buoy.widget.BDialog;
import buoy.widget.BFileChooser;
import buoy.widget.BFrame;
import buoy.widget.BLabel;
import buoy.widget.BOutline;
import buoy.widget.BProgressBar;
import buoy.widget.BorderContainer;
import buoy.widget.FormContainer;
import buoy.widget.LayoutInfo;

import com.geopista.feature.GeopistaFeature;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.CollectionMap;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.IFeatureSelection;
import com.vividsolutions.jump.workbench.ui.InfoFrame;
import com.vividsolutions.jump.workbench.ui.TaskFrame;

/**
 * QueryDialog
 * @author Michaël MICHAUD
 * @version 0.1.0 (4 Dec 2004)
 */ 
public class QueryDialog extends BDialog {
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory.getLog(QueryDialog.class);
 
	private PlugInContext context;

	// List of layers to search
	List layers = new ArrayList();
	// List of available attributes
	Set attributes = new TreeSet();
	// Selected attribute name
	String attribute;
	// Selected attribute type
	char attributeType;
	// Selected function
	Function function;
	// Selected operator
	Operator operator;
	// Selected value
	String value;
	// Map attributes with lists of available values read in the fc
	Map enumerations = new HashMap();
	// Flag indicating a query is running
	static boolean runningQuery = false;
	static boolean cancelQuery = false;

	// if mmpatch is used (mmpatch gives more attribute types), mmaptch must
	// be set to true
	boolean mmpatch = false; 

	// selected features initialized in execute query if "select" option is true
	Collection selection; 

	BCheckBox charFilter;
	BCheckBox caseSensitive;
	BCheckBox numFilter;
	BCheckBox geoFilter;

	BCheckBox display;
	BCheckBox select;
	BCheckBox create;

	BComboBox layerCB;
	BComboBox attributeCB;
	BComboBox functionCB;
	BComboBox operatorCB;
	BComboBox valueCB;

	BLabel comments;
	BLabel progressBarTitle;
	BProgressBar progressBar;

	BButton okButton;
	BButton refreshButton;
	BButton cancelButton;
	BButton stopButton;

	
	// Advanced
	BLabel commentsAdvanced;
	BOutline queryAdvancedConstructorPanelB = null;
	BCheckBox advancedCB;
	BComboBox layerAdvancedCB;
	BComboBox attributeAdvancedCB;
	BComboBox functionAdvancedCB;
	BComboBox operatorAdvancedCB;
	BComboBox valueAdvancedCB;
	BComboBox asociationWithLayer1CB;
	BComboBox asociationWithLayer2CB;
	
	
	// List of layers to search
	List layersAdvanced = new ArrayList();
	// List of available attributes
	Set attributesAdvanced = new TreeSet();
	// Selected attribute name
	String attributeAdvanced;
	// Selected attribute type
	char attributeTypeAdvanced;
	// Selected function
	Function functionAdvanced;
	// Selected operator
	Operator operatorAdvanced;
	// Selected value
	String valueAdvanced;
	// Map attributes with lists of available values read in the fc
	Map enumerationsAdvanced = new HashMap();
	
	
	
	private boolean showedAllredy = false;
	TaskFrame taskFrame = null;


	/**
	 * Main method : create a BeanShellEditor.
	 * <p>When BeanShellEditor is launched as a standalone application, the
	 * WindowClosingEvent shut the jvm down
	 * @param args if args[0] is -i18n, args[1] and args[2] must be the language
	 * and the country 2 letters code
	 */
	public static void main(String[] args) {
		BFrame frame = new BFrame();
		QueryDialog qd = new QueryDialog(null);
	}

	/**
	 * Default constructor with no argument.
	 * <p>Used by main() method for standalone applications.
	 */
	public QueryDialog(PlugInContext context) {
		super(new BFrame(), false);
		addEventLink(WindowClosingEvent.class, this, "exit");
		//addEventLink(WindowResizedEvent.class, this, "resize");
		if (AttributeType.allTypes().size()>6) {
			mmpatch = true;
		}
		this.context = context;
		setTitle(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.title"));
		initUI();
	}


	/**
	 * User Interface Initialization
	 */
	protected void initUI() {


		// LAYOUT DEFINITIONS
		LayoutInfo centerNone6 =
			new LayoutInfo(LayoutInfo.CENTER, LayoutInfo.NONE,
					new java.awt.Insets(6,6,6,6), new java.awt.Dimension());

		LayoutInfo centerBoth3 =
			new LayoutInfo(LayoutInfo.CENTER, LayoutInfo.BOTH,
					new java.awt.Insets(3,3,3,3), new java.awt.Dimension());

		LayoutInfo centerBoth1 =
			new LayoutInfo(LayoutInfo.CENTER, LayoutInfo.BOTH,
					new java.awt.Insets(1,1,1,1), new java.awt.Dimension());

		LayoutInfo nwBoth1 =
			new LayoutInfo(LayoutInfo.NORTHWEST, LayoutInfo.BOTH,
					new java.awt.Insets(1,1,1,1), new java.awt.Dimension());

		LayoutInfo centerNone3 =
			new LayoutInfo(LayoutInfo.CENTER, LayoutInfo.NONE,
					new java.awt.Insets(3,3,3,3), new java.awt.Dimension());

		LayoutInfo centerH3 =
			new LayoutInfo(LayoutInfo.CENTER, LayoutInfo.HORIZONTAL,
					new java.awt.Insets(3,3,3,3), new java.awt.Dimension());

		Border border = BorderFactory.createLineBorder(java.awt.Color.BLACK);
		Border border2 = BorderFactory.createLineBorder(java.awt.Color.BLACK, 2);

		// MAIN GUI CONTAINERS
		BorderContainer dialogContainer = new BorderContainer();
		// NORTH
		BorderContainer northPanel = new BorderContainer();
		BorderContainer titleContainer =  new BorderContainer();
		titleContainer.setDefaultLayout(centerNone6);
		// CENTER
		FormContainer centerPanel = new FormContainer(1,4);
		//OPTIONS
		FormContainer optionPanel = new FormContainer(4,1);
		optionPanel.setDefaultLayout(centerBoth1);
		BOutline optionPanelB = new BOutline(optionPanel, border);
		// MANAGER
		FormContainer managerPanel = new FormContainer(3,3);
		managerPanel.setDefaultLayout(nwBoth1);
		BOutline managerPanelB = new BOutline(managerPanel, 
				BorderFactory.createTitledBorder(border,
						I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.manager")));
		// ATTRIBUTE FILTER
		FormContainer attributeFilterPanel = new FormContainer(2,3);
		attributeFilterPanel.setDefaultLayout(nwBoth1);
		BOutline attributeFilterPanelB = new BOutline(attributeFilterPanel,
				BorderFactory.createTitledBorder(border,
						I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.filter")));
		// RESULT OPTIONS
		FormContainer resultPanel = new FormContainer(1,3);
		resultPanel.setDefaultLayout(nwBoth1);
		BOutline resultPanelB = new BOutline(resultPanel,
				BorderFactory.createTitledBorder(border,
						I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.result")));
		// QUERY CONSTRUCTOR
		FormContainer queryConstructorPanel = new FormContainer(5,3);
		queryConstructorPanel.setBackground(Color.decode(
				I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.color1")
		));
		queryConstructorPanel.setDefaultLayout(centerNone3);
		BOutline queryConstructorPanelB = new BOutline(queryConstructorPanel, border2);

		// QUERY ADVANCED CONSTRUCTOR 
		FormContainer queryAdvancedConstructorPanel = new FormContainer(5,6);
		queryAdvancedConstructorPanel.setBackground(Color.decode(
				I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.color1")
		));
		queryAdvancedConstructorPanel.setDefaultLayout(centerNone3);
		queryAdvancedConstructorPanelB = new BOutline(queryAdvancedConstructorPanel, border2);
		queryAdvancedConstructorPanelB.setVisible(false);
		// PROGRESS BAR
		FormContainer progressBarPanel = new FormContainer(5,1);

		// SOUTH PANEL (OK/CANCEL BUTTONS)
		FormContainer southPanel = new FormContainer(8,1);

		// TITLE PANEL
		//BLabel title = new BLabel(
		//    "<html><h3>" + i18n.getString("jump.query.dialog.title") + "</h3></html>"
		//);
		//titleContainer.add(title, BorderContainer.CENTER);
		//northPanel.add(titleContainer, BorderContainer.CENTER);

		// SET THE MANAGER BUTTONS
		BButton openButton = new BButton(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.open"));
		openButton.addEventLink(CommandEvent.class, this, "open");
		managerPanel.add(openButton, 1, 0, centerH3);
		BButton saveasButton = new BButton(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.saveas"));
		saveasButton.addEventLink(CommandEvent.class, this, "saveas");
		managerPanel.add(saveasButton, 1, 2, centerH3);

		// SET THE ATTRIBUTE FILTER CHECKBOXES
		charFilter = new BCheckBox(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.charFilter"), true);
		charFilter.addEventLink(ValueChangedEvent.class, this, "charFilterChanged");
		attributeFilterPanel.add(charFilter, 0, 0);
		caseSensitive = new BCheckBox(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.caseSensitive"), false);
		//caseSensitive.addEventLink(ValueChangedEvent.class, this, "caseSensitiveChanged");
		attributeFilterPanel.add(caseSensitive, 1, 0, centerNone3);
		numFilter = new BCheckBox(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.numFilter"), true);
		numFilter.addEventLink(ValueChangedEvent.class, this, "numFilterChanged");
		attributeFilterPanel.add(numFilter, 0, 1);
		geoFilter = new BCheckBox(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.geoFilter"), true);
		geoFilter.addEventLink(ValueChangedEvent.class, this, "geoFilterChanged");
		attributeFilterPanel.add(geoFilter, 0, 2);

		// SET THE RESULT OPTIONS
		display = new BCheckBox(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.display"), false);
		resultPanel.add(display, 0, 0);
		select = new BCheckBox(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.select"), true);
		resultPanel.add(select, 0, 1);
		create = new BCheckBox(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.create"), false);
		resultPanel.add(create, 0, 2);

		// SET THE COMBO BOXES
		BLabel layerLabel = new BLabel(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.layer.label"));
		queryConstructorPanel.add(layerLabel, 0, 0, centerNone3);
		BLabel attributeLabel = new BLabel(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.attribute.label"));
		queryConstructorPanel.add(attributeLabel, 1, 0, centerNone3);
		BLabel functionLabel = new BLabel(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.function.label"));
		queryConstructorPanel.add(functionLabel, 2, 0, centerNone3);
		BLabel operatorLabel = new BLabel(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.operator.label"));
		queryConstructorPanel.add(operatorLabel, 3, 0, centerNone3);
		BLabel valueLabel = new BLabel(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.value.label"));
		queryConstructorPanel.add(valueLabel, 4, 0, centerNone3);

		layerCB = new BComboBox();
		layerCB.addEventLink(ValueChangedEvent.class, this, "layerChanged");
		queryConstructorPanel.add(layerCB, 0, 1);
		attributeCB = new BComboBox();
		attributeCB.addEventLink(ValueChangedEvent.class, this, "attributeChanged");
		queryConstructorPanel.add(attributeCB, 1, 1);
		functionCB = new BComboBox();
		functionCB.addEventLink(ValueChangedEvent.class, this, "functionChanged");
		queryConstructorPanel.add(functionCB, 2, 1);
		operatorCB = new BComboBox();
		operatorCB.addEventLink(ValueChangedEvent.class, this, "operatorChanged");
		queryConstructorPanel.add(operatorCB, 3, 1);
		valueCB = new BComboBox();
		valueCB.addEventLink(ValueChangedEvent.class, this, "valueChanged");
		queryConstructorPanel.add(valueCB, 4, 1);

		
		
		
		
		// SET THE COMBO BOXES
		BLabel layerAdvancedLabel = new BLabel(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.layer.label"));
		queryAdvancedConstructorPanel.add(layerAdvancedLabel, 0, 0, centerNone3);
		BLabel attributeAdvancedLabel = new BLabel(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.attribute.label"));
		queryAdvancedConstructorPanel.add(attributeAdvancedLabel, 1, 0, centerNone3);
		BLabel functionAdvancedLabel = new BLabel(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.function.label"));
		queryAdvancedConstructorPanel.add(functionAdvancedLabel, 2, 0, centerNone3);
		BLabel operatorAdvancedLabel = new BLabel(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.operator.label"));
		queryAdvancedConstructorPanel.add(operatorAdvancedLabel, 3, 0, centerNone3);
		BLabel valueAdvancedLabel = new BLabel(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.value.label"));
		queryAdvancedConstructorPanel.add(valueAdvancedLabel, 4, 0, centerNone3);

		layerAdvancedCB = new BComboBox();
		layerAdvancedCB.addEventLink(ValueChangedEvent.class, this, "layerAdvancedChanged");
		queryAdvancedConstructorPanel.add(layerAdvancedCB, 0, 1);
		attributeAdvancedCB = new BComboBox();
		attributeAdvancedCB.addEventLink(ValueChangedEvent.class, this, "attributeAdvancedChanged");
		queryAdvancedConstructorPanel.add(attributeAdvancedCB, 1, 1);
		functionAdvancedCB = new BComboBox();
		functionAdvancedCB.addEventLink(ValueChangedEvent.class, this, "functionAdvancedChanged");
		queryAdvancedConstructorPanel.add(functionAdvancedCB, 2, 1);
		operatorAdvancedCB = new BComboBox();
		operatorAdvancedCB.addEventLink(ValueChangedEvent.class, this, "operatorAdvancedChanged");
		queryAdvancedConstructorPanel.add(operatorAdvancedCB, 3, 1);
		valueAdvancedCB = new BComboBox();
		valueAdvancedCB.addEventLink(ValueChangedEvent.class, this, "valueAdvancedChanged");
		queryAdvancedConstructorPanel.add(valueAdvancedCB, 4, 1);
		
		BLabel asociationWithLayer11Label = new BLabel(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.asociationWithLayer11"));
		queryAdvancedConstructorPanel.add(asociationWithLayer11Label, 1, 2, centerNone3);
		BLabel asociationWithLayer12Label = new BLabel(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.asociationWithLayer12"));
		queryAdvancedConstructorPanel.add(asociationWithLayer12Label, 1, 3, centerNone3);
		
		BLabel asociationWithLayer21Label = new BLabel(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.asociationWithLayer21"));
		queryAdvancedConstructorPanel.add(asociationWithLayer21Label, 2, 2, centerNone3);
		BLabel asociationWithLayer22Label = new BLabel(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.asociationWithLayer22"));
		queryAdvancedConstructorPanel.add(asociationWithLayer22Label, 2, 3, centerNone3);
		
		asociationWithLayer1CB = new BComboBox();
		asociationWithLayer1CB.addEventLink(ValueChangedEvent.class, this, "asociationWithLayer1Changed");
		queryAdvancedConstructorPanel.add(asociationWithLayer1CB, 1, 4);
		
		asociationWithLayer2CB = new BComboBox();
		asociationWithLayer2CB.addEventLink(ValueChangedEvent.class, this, "asociationWithLayer2Changed");
		queryAdvancedConstructorPanel.add(asociationWithLayer2CB, 2, 4);
		commentsAdvanced = new BLabel(" ");
		queryAdvancedConstructorPanel.add(commentsAdvanced, 0, 5, 5, 1, centerH3);

		comments = new BLabel(" ");
		queryConstructorPanel.add(comments, 0, 2, 5, 1, centerH3);

		// PROGRESS BAR PANEL
		
		advancedCB = new BCheckBox(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.optAdvanced"), false);
		advancedCB.addEventLink(ValueChangedEvent.class, this, "advancedChanged");
		advancedCB.setEnabled(false);
		progressBarPanel.add(advancedCB, 0, 0, 1, 1, centerH3);
		

		progressBarTitle = new BLabel(" ");
		progressBarPanel.add(progressBarTitle, 0, 0, 1, 1, centerH3);
		progressBar = new BProgressBar();
		progressBarPanel.add(progressBar, 1, 0, 4, 1, centerH3);

		// CENTER PANEL LAYOUT
		optionPanel.add(managerPanelB, 0, 0);
		optionPanel.add(attributeFilterPanelB, 1, 0, 2, 1);
		optionPanel.add(resultPanelB, 3, 0);

		centerPanel.setDefaultLayout(centerBoth3);
		centerPanel.add(optionPanel, 0, 0);
		centerPanel.add(queryConstructorPanelB, 0, 1);
		centerPanel.add(queryAdvancedConstructorPanelB, 0, 2);
		centerPanel.add(progressBarPanel, 0, 3);

		// SET THE OK/CANCEL BUTTONS
		// ok/cancel button process is done in QueryPlugIn
		okButton = new BButton(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.ok"));
		okButton.addEventLink(CommandEvent.class, this, "ok");
		cancelButton = new BButton(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.cancel"));
		cancelButton.addEventLink(CommandEvent.class, this, "cancel");
		stopButton = new BButton(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.stop"));
		stopButton.addEventLink(CommandEvent.class, this, "stop");
		refreshButton = new BButton(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.refresh"));
		refreshButton.addEventLink(CommandEvent.class, this, "refresh");

		southPanel.add(okButton, 2, 0);
		southPanel.add(refreshButton, 3, 0);
		southPanel.add(cancelButton, 4, 0);
		southPanel.add(stopButton, 5, 0);

		dialogContainer.add(northPanel, dialogContainer.NORTH);
		dialogContainer.add(centerPanel, dialogContainer.CENTER);
		dialogContainer.add(southPanel, dialogContainer.SOUTH);

		initComboBoxes();
		setContent(dialogContainer);
		addEventLink(MouseEnteredEvent.class, this, "toFront");
		pack();
		setVisible(true);
		toFront();


	}

	// To add an eventLink in the QueryPlugIn 
	BButton getOkButton() {return okButton;}
	BButton getCancelButton() {return cancelButton;} 


	void initVariables() {
		runningQuery = false;
		cancelQuery = false;
		//comments.setText("");
		progressBarTitle.setText("");
		progressBar.setIndeterminate(false);
		progressBar.setValue(0);
		progressBar.setProgressText("");
	}

	void initComboBoxes() {
		// INIT layerCB and attributeCB
		layerAdvancedCB.removeAll();
		layerCB.removeAll();
		layerCB.add(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.alllayers"));
		layerCB.add(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.selection"));
		layerCB.add(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.selectedlayers"));

		List layers = context.getLayerManager().getLayers();
		List layersAdvanced = context.getLayerManager().getLayers();
		for (int i = 0 ; i < layers.size() ; i++) {
			Layer layer = (Layer)layers.get(i);
			layerCB.add(layer);
			
		}
		
		for (int i = (layers.size()-1) ; i >= 0 ; i--) {
			Layer layer = (Layer)layers.get(i);
			layerAdvancedCB.add(layer);
		}

		this.layers = layers;
		this.attributes = authorizedAttributes(layers);
		//attributeType = "GEOMETRY";
		attributeType = 'G';
		attributeCB.setContents(attributes);

		// INIT functionCB
		functionCB.setContents(Function.GEOMETRIC_FUNCTIONS);
		function = (Function)functionCB.getSelectedValue();

		// INIT operatorCB
		operatorCB.setContents(Operator.GEOMETRIC_OP);
		operator = (Operator)operatorCB.getSelectedValue();

		// INIT valueCB
		valueCB.setContents(availableTargets());
		valueChanged();
		

		this.layersAdvanced.clear();
		this.layersAdvanced.add(layerAdvancedCB.getSelectedValue());
		//this.layersAdvanced = layersAdvanced;
		this.attributesAdvanced = authorizedAttributes(this.layersAdvanced);
		
		//attributeTypeAdvanced = "GEOMETRY";
		attributeTypeAdvanced = 'G';
		attributeAdvancedCB.setContents(attributesAdvanced);

		asociationWithLayer1CB.setContents(attributes);
		asociationWithLayer2CB.setContents(attributesAdvanced);
		
		
		// INIT functionCB
		functionAdvancedCB.setContents(Function.GEOMETRIC_FUNCTIONS);
		functionAdvanced = (Function)functionAdvancedCB.getSelectedValue();

		// INIT operatorCB
		operatorAdvancedCB.setContents(Operator.GEOMETRIC_OP);
		operatorAdvanced = (Operator)operatorAdvancedCB.getSelectedValue();
		
		// INIT valueCB
		
		List list = new ArrayList();
		for (int i = 0 ; i < layers.size() ; i++) {
			list.add(((Layer)layers.get(i)).getName());
		}
		valueAdvancedCB.setContents(list);
		valueAdvancedChanged();
		
		
		pack();

		initVariables();
	}

	public void layerChanged() {
		layers.clear();
		attributes.clear();
		// index 0 ==> all the layers
		if (layerCB.getSelectedIndex()==0) {
			layers.addAll(context.getLayerManager().getLayers());
		}
		// index 1 ==> all the selected features
		else if (layerCB.getSelectedIndex()==1) {
			layers.addAll(context.getLayerViewPanel()
					.getSelectionManager()
					.getLayersWithSelectedItems());
		}
		// index 2 ==> all the selected layers
		else if (layerCB.getSelectedIndex()==2) {
			Layer[] ll = context.getLayerNamePanel().getSelectedLayers();
			layers.addAll(Arrays.asList(ll));
		}
		// selected layer
		else {
			layers.add(layerCB.getSelectedValue());
		}
		attributes.addAll(authorizedAttributes(layers));
		attributeCB.setContents(attributes);
		asociationWithLayer1CB.setContents(attributes);
		attributeChanged();
		
		
		if(layerCB.getSelectedIndex()>2){
			advancedCB.setEnabled(true);
			if(advancedCB.getState()){
				advancedCB.setState(true);
			}
			else{
				advancedCB.setState(false);
			}
			
			
		}
		else{
			advancedCB.setEnabled(false);
			advancedCB.setState(false);
			queryAdvancedConstructorPanelB.setVisible(false);
		}
	}

	public void layerAdvancedChanged() {
		layersAdvanced.clear();
		attributesAdvanced.clear();
		// index 0 ==> all the layers
		/*if (layerAdvancedCB.getSelectedIndex()==0) {
			layersAdvanced.addAll(context.getLayerManager().getLayers());
		}
		// index 1 ==> all the selected features
		else if (layerAdvancedCB.getSelectedIndex()==1) {
			layersAdvanced.addAll(context.getLayerViewPanel()
					.getSelectionManager()
					.getLayersWithSelectedItems());
		}
		// index 2 ==> all the selected layers
		else if (layerAdvancedCB.getSelectedIndex()==2) {
			Layer[] ll = context.getLayerNamePanel().getSelectedLayers();
			layersAdvanced.addAll(Arrays.asList(ll));
		}
		// selected layer
		else {
		*/
			layersAdvanced.add(layerAdvancedCB.getSelectedValue());
		//}
		attributesAdvanced.addAll(authorizedAttributesAdvanced(layersAdvanced));
		attributeAdvancedCB.setContents(attributesAdvanced);
		asociationWithLayer2CB.setContents(attributesAdvanced);
		attributeAdvancedChanged();
	}
	

	private boolean isAttributeAuthorized(FeatureSchema fs, String attributeName) {
		AttributeType type = fs.getAttributeType(attributeName);
		if (type==AttributeType.GEOMETRY && geoFilter.getState()) return true;
		else if (type==AttributeType.STRING && charFilter.getState()) return true;
		else if (type==AttributeType.INTEGER && numFilter.getState()) return true;
		else if (type==AttributeType.LONG && numFilter.getState()) return true;
		else if (type==AttributeType.DOUBLE && numFilter.getState()) return true;
		else if (type==AttributeType.FLOAT && numFilter.getState()) return true;
		else if (type==AttributeType.DATE) return true;
		/* Special MM attributes, disabled for the moment
        else if (mmpatch && type==AttributeType.LONG && numFilter.getState()) return true;
        else if (mmpatch && type==AttributeType.BOOLEAN && numFilter.getState()) return true;
        else if (mmpatch && type instanceof AttributeType.Char && charFilter.getState()) return true;
        else if (mmpatch && type instanceof AttributeType.Decimal && numFilter.getState()) return true;
        else if (mmpatch && type instanceof AttributeType.Enumeration) return true;
		 */
		else return false;
	}

	private Set authorizedAttributes(List layers) {
		// set of authorized Attributes
		Set set = new TreeSet();
		// map of enumerations
		enumerations = new HashMap();
		attribute = "";
		
		set = authorizedAttributesCommons(layers);
		/*for (int i = 0 ; i < layers.size() ; i++) {
			Layer layer = (Layer)layers.get(i);
			FeatureSchema fs = layer.getFeatureCollectionWrapper().getFeatureSchema();
			for (int j = 0 ; j < fs.getAttributeCount() ; j++) {
				String att = fs.getAttributeName(j);
				AttributeType type = fs.getAttributeType(j);
				if (type!=AttributeType.GEOMETRY && isAttributeAuthorized(fs, att) ) {
					set.add(att + " (" + type.toString().split(" ")[0] + ")");
					*/
					/* disabled because these types are not part of the Jump-i18n 
                    if(mmpatch && type instanceof AttributeType.Enumeration) {
                        set.add(att + " (ENUM)");
                        enumerations.put(att, ((AttributeType.Enumeration)type).getEnumerationArray());
                    }
                    else {
                        set.add(att + " (" + type.toString().split(" ")[0] + ")");
                    }
					 */
			/*	}
			}
		}*/
		return set;
	}
	
	private Set authorizedAttributesCommons(List layers) {
		// set of authorized Attributes
		Set set = new TreeSet();
		
		for (int i = 0 ; i < layers.size() ; i++) {
			Layer layer = (Layer)layers.get(i);
			FeatureSchema fs = layer.getFeatureCollectionWrapper().getFeatureSchema();
			for (int j = 0 ; j < fs.getAttributeCount() ; j++) {
				String att = fs.getAttributeName(j);
				AttributeType type = fs.getAttributeType(j);
				if (isAttributeAuthorized(fs, att) ) {
					set.add(att + " (" + type.toString().split(" ")[0] + ")");
					/* disabled because these types are not part of the Jump-i18n 
                    if(mmpatch && type instanceof AttributeType.Enumeration) {
                        set.add(att + " (ENUM)");
                        enumerations.put(att, ((AttributeType.Enumeration)type).getEnumerationArray());
                    }
                    else {
                        set.add(att + " (" + type.toString().split(" ")[0] + ")");
                    }
					 */
				}
			}
		}
		
		return set;
	}

	private Set authorizedAttributesAdvanced(List layers) {
		// set of authorized Attributes
		Set set = new TreeSet();
		// map of enumerations
		enumerationsAdvanced = new HashMap();
		attributeAdvanced = "";
		
		set = authorizedAttributesCommons(layers);
		
		return set;
	}
	
	private void advancedChanged() {
		if(advancedCB.getState()){
			queryAdvancedConstructorPanelB.setVisible(true);
		}
		else{
			queryAdvancedConstructorPanelB.setVisible(false);
		}
	}

	private void charFilterChanged() {
		if (charFilter.getState()) caseSensitive.setVisible(true);
		else caseSensitive.setVisible(false);
		layerChanged();
	}

	private void numFilterChanged() {
		layerChanged();
	}

	private void geoFilterChanged() {
		layerChanged();
	}

	public void attributeChanged() {
		String att = (String)attributeCB.getSelectedValue();
		String attType = "";
		if(att != null){
		attribute = att.substring(0, att.lastIndexOf(' '));
			attType = att.substring(att.lastIndexOf('(')+1,
				att.lastIndexOf(')'));
		}
		else{
			attribute = "";
			attType = "";
		}
		
		
		char newat = 'B';
		if (attType.equals("BOOLEAN")) newat = 'B';
		else if (attType.equals("INTEGER")) newat = 'N';
		else if (mmpatch && attType.equals("LONG")) newat = 'N';
		else if (attType.equals("DOUBLE")) newat = 'N';
		else if (attType.equals("FLOAT")) newat = 'N';
		else if (mmpatch && attType.equals("DECIMAL")) newat = 'N';
		else if (attType.equals("STRING")) newat = 'S';
		else if (mmpatch && attType.equals("CHAR")) newat = 'S';
		else if (mmpatch && attType.equals("ENUM")) newat = 'E';
		else if (attType.equals("GEOMETRY")) newat = 'G';
		else if (attType.equals("DATE")) newat = 'D';
		else;
		if (newat==attributeType) {
			operatorChanged();
			return;
		}
		else attributeType = newat;
		switch (attributeType) {
		case 'B' :
			functionCB.setContents(Function.BOOLEAN_FUNCTIONS);
			break;
		case 'N' :
			functionCB.setContents(Function.NUMERIC_FUNCTIONS);
			break;
		case 'S' :
			functionCB.setContents(Function.STRING_FUNCTIONS);
			break;
		case 'E' :
			functionCB.setContents(Function.STRING_FUNCTIONS);
			break;
		case 'G' :
			functionCB.setContents(Function.GEOMETRIC_FUNCTIONS);
			break;
		case 'D' :
			functionCB.setContents(Function.DATE_FUNCTIONS);
			break;
		default :
		}
		functionChanged();
	}

	public void attributeAdvancedChanged() {
		String att = (String)attributeAdvancedCB.getSelectedValue();
		attributeAdvanced = att.substring(0, att.lastIndexOf(' '));
		String attType = att.substring(att.lastIndexOf('(')+1,
				att.lastIndexOf(')'));
		char newat = 'B';
		if (attType.equals("BOOLEAN")) newat = 'B';
		else if (attType.equals("INTEGER")) newat = 'N';
		else if (mmpatch && attType.equals("LONG")) newat = 'N';
		else if (attType.equals("DOUBLE")) newat = 'N';
		else if (attType.equals("FLOAT")) newat = 'N';
		else if (mmpatch && attType.equals("DECIMAL")) newat = 'N';
		else if (attType.equals("STRING")) newat = 'S';
		else if (mmpatch && attType.equals("CHAR")) newat = 'S';
		else if (mmpatch && attType.equals("ENUM")) newat = 'E';
		else if (attType.equals("GEOMETRY")) newat = 'G';
		else if (attType.equals("DATE")) newat = 'D';
		else;
		if (newat==attributeTypeAdvanced) {
			operatorAdvancedChanged();
			return;
		}
		else attributeTypeAdvanced = newat;
		switch (attributeTypeAdvanced) {
		case 'B' :
			functionAdvancedCB.setContents(Function.BOOLEAN_FUNCTIONS);
			break;
		case 'N' :
			functionAdvancedCB.setContents(Function.NUMERIC_FUNCTIONS);
			break;
		case 'S' :
			functionAdvancedCB.setContents(Function.STRING_FUNCTIONS);
			break;
		case 'E' :
			functionAdvancedCB.setContents(Function.STRING_FUNCTIONS);
			break;
		case 'G' :
			functionAdvancedCB.setContents(Function.GEOMETRIC_FUNCTIONS);
			break;
		case 'D' :
			functionAdvancedCB.setContents(Function.DATE_FUNCTIONS);
			break;
		default :
		}
		functionAdvancedChanged();
	}
	
	public void functionChanged() {
		String ft = functionCB.getSelectedValue().toString();
		try {
			function = (Function)functionCB.getSelectedValue();
		} catch(Exception e) {}
		switch(function.type) {
		case 'S' :
		case 'E' :
			operatorCB.setContents(Operator.STRING_OP);
			break;
		case 'B' :
			operatorCB.setContents(Operator.BOOLEAN_OP);
			break;
		case 'N' :
			operatorCB.setContents(Operator.NUMERIC_OP);
			break;
		case 'G' :
			operatorCB.setContents(Operator.GEOMETRIC_OP);
			break;
		case 'D' :
			operatorCB.setContents(Operator.DATE_OP);
			break;  
		default :
		}
		// SET IF FUNCTION IS EDITABLE OR NOT
		if(function==Function.SUBS) {
			functionCB.setEditable(true);
			String f = functionCB.getSelectedValue().toString();
			String sub = f.substring(f.lastIndexOf('(')+1, f.lastIndexOf(')'));
			String[] ss = sub.split(",");
			Function.SUBS.args = new int[ss.length];
			if (ss.length>0) Function.SUBS.args[0] = Integer.parseInt(ss[0]);
			if (ss.length>1) Function.SUBS.args[1] = Integer.parseInt(ss[1]);
			functionCB.setSelectedValue(Function.SUBS);
		}
		else if(function==Function.BUFF) {
			functionCB.setEditable(true);
			String f = functionCB.getSelectedValue().toString();
			String sub = f.substring(f.lastIndexOf('(')+1, f.lastIndexOf(')'));
			Function.BUFF.arg = Double.parseDouble(sub);
			functionCB.setSelectedValue(Function.BUFF);
		}
		else {functionCB.setEditable(false);}
		operatorChanged();
	}

	public void functionAdvancedChanged() {
		String ft = functionAdvancedCB.getSelectedValue().toString();
		try {
			functionAdvanced = (Function)functionAdvancedCB.getSelectedValue();
		} catch(Exception e) {}
		switch(functionAdvanced.type) {
		case 'S' :
		case 'E' :
			operatorAdvancedCB.setContents(Operator.STRING_OP);
			break;
		case 'B' :
			operatorAdvancedCB.setContents(Operator.BOOLEAN_OP);
			break;
		case 'N' :
			operatorAdvancedCB.setContents(Operator.NUMERIC_OP);
			break;
		case 'G' :
			operatorAdvancedCB.setContents(Operator.GEOMETRIC_OP);
			break;
		case 'D' :
			operatorAdvancedCB.setContents(Operator.DATE_OP);
			break;  
		default :
		}
		// SET IF FUNCTION IS EDITABLE OR NOT
		if(functionAdvanced==Function.SUBS) {
			functionAdvancedCB.setEditable(true);
			String f = functionAdvancedCB.getSelectedValue().toString();
			String sub = f.substring(f.lastIndexOf('(')+1, f.lastIndexOf(')'));
			String[] ss = sub.split(",");
			Function.SUBS.args = new int[ss.length];
			if (ss.length>0) Function.SUBS.args[0] = Integer.parseInt(ss[0]);
			if (ss.length>1) Function.SUBS.args[1] = Integer.parseInt(ss[1]);
			functionAdvancedCB.setSelectedValue(Function.SUBS);
		}
		else if(functionAdvanced==Function.BUFF) {
			functionAdvancedCB.setEditable(true);
			String f = functionAdvancedCB.getSelectedValue().toString();
			String sub = f.substring(f.lastIndexOf('(')+1, f.lastIndexOf(')'));
			Function.BUFF.arg = Double.parseDouble(sub);
			functionAdvancedCB.setSelectedValue(Function.BUFF);
		}
		else {functionAdvancedCB.setEditable(false);}
		operatorAdvancedChanged();
	}


	public void operatorChanged() {
		String op = operatorCB.getSelectedValue().toString();
		//System.out.println("operatorChanged() " + op + " (" + operatorCB.getSelectedValue().getClass() + ")");
		// The selected value has been modified in an editable item,
		// it has to be processed first
		try {
			operator = (Operator)operatorCB.getSelectedValue();
		}
		catch(Exception e) {System.out.println(e);}
		if(function==Function.EMPT || function==Function.SIMP || function==Function.VALI) {
			valueCB.setContents(new String[]{
					I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.value.true"),
					I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.value.false")
			});
			valueCB.setEditable(false);
		}
		else if (operator.type=='G') {
			valueCB.setContents(availableTargets());
			if (operator==Operator.WDIST) {
				operatorCB.setEditable(true);
				String f = operatorCB.getSelectedValue().toString();
				String sub = f.substring(f.lastIndexOf('(')+1, f.lastIndexOf(')'));
				Operator.WDIST.arg = Double.parseDouble(sub);
				operatorCB.setSelectedValue(Operator.WDIST);
			}
		}
		else if (attributeType=='E') {
			valueCB.setContents((Object[])enumerations.get(attribute));
			valueCB.setEditable(true);
		}
		else if (attributeType=='S') {
			valueCB.setContents(availableStrings(attribute, 12));
			if (operator==Operator.MATC || operator==Operator.FIND) {
				valueCB.setContents(new String[]{
						I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.value.regex")
				});
			}
			valueCB.setEditable(true);
		} else {
			valueCB.setContents(new String[]{""});
			valueCB.setEditable(true);
		}
		valueChanged();
	}

	public void operatorAdvancedChanged() {
		String op = operatorAdvancedCB.getSelectedValue().toString();
		//System.out.println("operatorChanged() " + op + " (" + operatorCB.getSelectedValue().getClass() + ")");
		// The selected value has been modified in an editable item,
		// it has to be processed first
		try {
			operatorAdvanced = (Operator)operatorAdvancedCB.getSelectedValue();
		}
		catch(Exception e) {System.out.println(e);}
		if(functionAdvanced==Function.EMPT || 
				functionAdvanced==Function.SIMP || 
				functionAdvanced==Function.VALI) {
			valueAdvancedCB.setContents(new String[]{
					I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.value.true"),
					I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.value.false")
			});
			valueAdvancedCB.setEditable(false);
		}
		else if (operatorAdvanced.type=='G') {
			valueAdvancedCB.setContents(availableTargets());
			if (operatorAdvanced==Operator.WDIST) {
				operatorAdvancedCB.setEditable(true);
				String f = operatorAdvancedCB.getSelectedValue().toString();
				String sub = f.substring(f.lastIndexOf('(')+1, f.lastIndexOf(')'));
				Operator.WDIST.arg = Double.parseDouble(sub);
				operatorAdvancedCB.setSelectedValue(Operator.WDIST);
			}
		}
		else if (attributeTypeAdvanced=='E') {
			valueAdvancedCB.setContents((Object[])enumerationsAdvanced.get(attributeAdvanced));
			valueAdvancedCB.setEditable(true);
		}
		else if (attributeTypeAdvanced=='S') {
			valueAdvancedCB.setContents(availableStrings(attributeAdvanced, 12));
			if (operatorAdvanced==Operator.MATC || operatorAdvanced==Operator.FIND) {
				valueAdvancedCB.setContents(new String[]{
						I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.value.regex")
				});
			}
			valueAdvancedCB.setEditable(true);
		} else {
			valueAdvancedCB.setContents(new String[]{""});
			valueAdvancedCB.setEditable(true);
		}
		valueAdvancedChanged();
	}

	private void valueChanged() {
		//if (operator.type=='G' && valueCB.getSelectedIndex()==0) {
		//selection = context.getLayerViewPanel().getSelectionManager().getSelectedItems();
		//}
		value = (String)valueCB.getSelectedValue();
	}

	private void valueAdvancedChanged() {
		//if (operator.type=='G' && valueCB.getSelectedIndex()==0) {
		//selection = context.getLayerViewPanel().getSelectionManager().getSelectedItems();
		//}
		valueAdvanced = (String)valueAdvancedCB.getSelectedValue();
	}
	
	
	private void asociationWithLayer1Changed(){
		
	}
	
	private void asociationWithLayer2Changed(){
		
	}

	private List availableTargets() {
		List list = new ArrayList();
		list.add(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.target.selection"));
		list.add(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.target.selectedlayers"));
		list.add(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.target.alllayers"));
		List layers = context.getLayerManager().getLayers();
		for (int i = 0 ; i < layers.size() ; i++) {
			list.add(((Layer)layers.get(i)).getName());
		}
		return list;
	}

	private Set availableStrings(String attribute, int maxsize) {
		Set set = new TreeSet();
		set.add("");
		for (int i = 0 ; i < layers.size() ; i++) {
			FeatureCollection fc = ((Layer)layers.get(i)).getFeatureCollectionWrapper();
			if (!fc.getFeatureSchema().hasAttribute(attribute)) continue;
			Iterator it = fc.iterator();
			while (it.hasNext()) {
				Feature feature = (Feature)it.next();
				if (feature.getAttribute(attribute) != null)
					set.add(feature.getAttribute(attribute));
			}
		}
		return set;
	}

	private void open() {
		BFileChooser bfc = new BFileChooser(BFileChooser.OPEN_FILE,
				I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.open"));
		Properties prop = new Properties();
		if (bfc.showDialog(this)) {
			try {
				File f = bfc.getSelectedFile();
				prop.load(new FileInputStream(f));
			}
			catch(IOException e){
				context.getWorkbenchFrame().warnUser(e.getMessage());
			}
		}
		if( bfc.getSelectedFile() != null){
		charFilter.setState(new Boolean(prop.getProperty("jump.query.dialog.charFilter")).booleanValue());
		caseSensitive.setState(new Boolean(prop.getProperty("jump.query.dialog.caseSensitive")).booleanValue());
		numFilter.setState(new Boolean(prop.getProperty("jump.query.dialog.numFilter")).booleanValue());
		geoFilter.setState(new Boolean(prop.getProperty("jump.query.dialog.geoFilter")).booleanValue());

		display.setState(new Boolean(prop.getProperty("jump.query.dialog.display")).booleanValue());
		select.setState(new Boolean(prop.getProperty("jump.query.dialog.select")).booleanValue());
		create.setState(new Boolean(prop.getProperty("jump.query.dialog.create")).booleanValue());

		initComboBoxes();

			boolean encontradoLayer = false;
		int layerIndex = Integer.parseInt(prop.getProperty("layer_index"));
		String layerName = prop.getProperty("layer_name");
			if (layerIndex<3){
				layerCB.setSelectedIndex(layerIndex);
				encontradoLayer = true;
			}
			else{ 
	
				for (int i=0; i< layerCB.getItemCount(); i++){
					if(i<3){
						String lay = (String)layerCB.getItem(i);
						if(lay.equals(layerName)){
							layerCB.setSelectedIndex(i);
							encontradoLayer = true;
						}
					}
					else{
						Layer lay = (Layer)layerCB.getItem(i);
						if(lay.getName().equals(layerName)){
							layerCB.setSelectedIndex(i);
							encontradoLayer = true;
						}
					}
					
				}
			}
		
			if(!encontradoLayer) {
			context.getWorkbenchFrame().warnUser(layerName + " " +
					I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.error.doesnotexist"));
			return;
		}
		layerChanged();

		int attributeIndex = Integer.parseInt(prop.getProperty("attribute_index"));
		String attributeName = prop.getProperty("attribute_name");
		attributeCB.setSelectedValue(attributeName);
		if(!attributeName.equals(attributeCB.getSelectedValue().toString())) {
			context.getWorkbenchFrame().warnUser(attributeName + " " +
					I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.error.doesnotexist"));
			return;
		}
		attributeChanged();

		int functionIndex = Integer.parseInt(prop.getProperty("function_index"));
		String functionName = prop.getProperty("function_name");
		functionCB.setSelectedIndex(functionIndex%functionCB.getItemCount());
		if(!functionName.equals(functionCB.getSelectedValue().toString())) {
			if (functionCB.getItem(functionIndex)==Function.SUBS) {
				functionCB.setEditable(true);
				functionCB.setSelectedValue(functionName);
			}
			else if (functionCB.getItem(functionIndex)==Function.BUFF) {
				functionCB.setEditable(true);
				functionCB.setSelectedValue(functionName);
			}
			else {
				context.getWorkbenchFrame().warnUser(functionName + " " +
						I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.error.doesnotexist"));
				return;
			}
		}
		functionChanged();

		int operatorIndex = Integer.parseInt(prop.getProperty("operator_index"));
		String operatorName = prop.getProperty("operator_name");
		operatorCB.setSelectedIndex(operatorIndex%operatorCB.getItemCount());
		if(!operatorName.equals(operatorCB.getSelectedValue().toString())) {
			if (operatorCB.getItem(operatorIndex)==Operator.WDIST) {
				operatorCB.setEditable(true);
				operatorCB.setSelectedValue(operatorName);
			}
			else {
				context.getWorkbenchFrame().warnUser(operatorName + " " +
						I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.error.doesnotexist"));
				return;
			}
		}
		operatorChanged();

		String value =  prop.getProperty("value");
		valueCB.setSelectedValue(value);

			
			//	opciones avanzadas
			
			int layerAdvancedIndex = Integer.parseInt(prop.getProperty("layerAdvanced_index"));
			String layerAdvancedName = prop.getProperty("layerAdvanced_name");
			//if (layerAdvancedIndex<3) layerAdvancedCB.setSelectedIndex(layerAdvancedIndex);
			//else 
			boolean encontradoAdvancedLayer = false;
			for(int j=0; j<layerAdvancedCB.getItemCount(); j++){
				Layer lay = (Layer)layerAdvancedCB.getItem(j);
				if(lay.getName().equals(layerAdvancedName)){
					layerAdvancedCB.setSelectedIndex(j);
					encontradoAdvancedLayer = true;
				}
			}
		
			//layerAdvancedCB.setSelectedValue(layerAdvancedName);
			if(!encontradoAdvancedLayer){
			//if(!layerAdvancedName.equals(layerAdvancedCB.getSelectedValue().toString())) {
				context.getWorkbenchFrame().warnUser(
						I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.optAdvanced") +": "+ layerAdvancedName + " " +
						I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.error.doesnotexist"));
				return;
			}
			layerAdvancedChanged();
			
			int attributeAdvancedIndex = Integer.parseInt(prop.getProperty("attributeAdvanced_index"));
			String attributeAdvancedName = prop.getProperty("attributeAdvanced_name");
			attributeAdvancedCB.setSelectedValue(attributeAdvancedName);
			if(!attributeAdvancedName.equals(attributeAdvancedCB.getSelectedValue().toString())) {
				context.getWorkbenchFrame().warnUser(attributeAdvancedName + " " +
						I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.error.doesnotexist"));
				return;
			}
			attributeAdvancedChanged();
			
			int functionAdvancedIndex = Integer.parseInt(prop.getProperty("functionAdvanced_index"));
			String functionAdvancedName = prop.getProperty("functionAdvanced_name");
			functionAdvancedCB.setSelectedIndex(functionAdvancedIndex%functionAdvancedCB.getItemCount());
			if(!functionAdvancedName.equals(functionAdvancedCB.getSelectedValue().toString())) {
				if (functionAdvancedCB.getItem(functionAdvancedIndex)==Function.SUBS) {
					functionAdvancedCB.setEditable(true);
					functionAdvancedCB.setSelectedValue(functionAdvancedName);
				}
				else if (functionAdvancedCB.getItem(functionAdvancedIndex)==Function.BUFF) {
					functionAdvancedCB.setEditable(true);
					functionAdvancedCB.setSelectedValue(functionAdvancedName);
				}
				else {
					context.getWorkbenchFrame().warnUser(functionAdvancedName + " " +
							I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.error.doesnotexist"));
					return;
				}
			}
			functionAdvancedChanged();
		
			int operatorAdvancedIndex = Integer.parseInt(prop.getProperty("operatorAdvanced_index"));
			String operatorAdvancedName = prop.getProperty("operatorAdvanced_name");
			operatorAdvancedCB.setSelectedIndex(operatorAdvancedIndex%operatorAdvancedCB.getItemCount());
			if(!operatorAdvancedName.equals(operatorAdvancedCB.getSelectedValue().toString())) {
				if (operatorAdvancedCB.getItem(operatorAdvancedIndex)==Operator.WDIST) {
					operatorAdvancedCB.setEditable(true);
					operatorAdvancedCB.setSelectedValue(operatorAdvancedName);
				}
				else {
					context.getWorkbenchFrame().warnUser(operatorAdvancedName + " " +
							I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.error.doesnotexist"));
					return;
				}
			}
			operatorAdvancedChanged();
		
			String valueAdvanced =  prop.getProperty("valueAdvanced");
			valueAdvancedCB.setSelectedValue(valueAdvanced);
	
			
			
			int attasociationWithLayer1Index = Integer.parseInt(prop.getProperty("attAsociationWithLayer1_index"));
			String attasociationWithLayer1Name = prop.getProperty("attAsociationWithLayer1_name");
			asociationWithLayer1CB.setSelectedValue(attasociationWithLayer1Name);
			if(!attasociationWithLayer1Name.equals(asociationWithLayer1CB.getSelectedValue().toString())) {
				context.getWorkbenchFrame().warnUser(attasociationWithLayer1Name + " " +
						I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.error.doesnotexist"));
				return;
			}
		
			int attasociationWithLayer2Index = Integer.parseInt(prop.getProperty("attasociationWithLayer2_index"));
			String attasociationWithLayer2Name = prop.getProperty("attasociationWithLayer2_name");
			asociationWithLayer2CB.setSelectedValue(attasociationWithLayer2Name);
			if(!attasociationWithLayer2Name.equals(asociationWithLayer2CB.getSelectedValue().toString())) {
				context.getWorkbenchFrame().warnUser(attasociationWithLayer2Name + " " +
						I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.error.doesnotexist"));
				return;
			}
			
			String advancedOptions =  prop.getProperty("advancedOptions");
			if(advancedOptions.equals("true")){
				advancedCB.setState(true);
				queryAdvancedConstructorPanelB.setVisible(true);
			}
			else if(advancedOptions.equals("false")){
				advancedCB.setState(false);
				queryAdvancedConstructorPanelB.setVisible(false);
			}
		}
		
	}

	private void saveas() {
		Properties prop = new Properties();

		prop.setProperty("jump.query.dialog.charFilter", ""+charFilter.getState());
		prop.setProperty("jump.query.dialog.caseSensitive", ""+caseSensitive.getState());
		prop.setProperty("jump.query.dialog.numFilter", ""+numFilter.getState());
		prop.setProperty("jump.query.dialog.geoFilter", ""+geoFilter.getState());
		prop.setProperty("jump.query.dialog.display", ""+display.getState());
		prop.setProperty("jump.query.dialog.select", ""+select.getState());
		prop.setProperty("jump.query.dialog.create", ""+create.getState());

		prop.setProperty("layer_index", ""+layerCB.getSelectedIndex());
		prop.setProperty("layer_name", ""+layerCB.getSelectedValue());
		prop.setProperty("attribute_index", ""+attributeCB.getSelectedIndex());
		prop.setProperty("attribute_name", ""+attributeCB.getSelectedValue());
		prop.setProperty("function_index", ""+functionCB.getSelectedIndex());
		prop.setProperty("function_name", ""+functionCB.getSelectedValue());
		prop.setProperty("operator_index", ""+operatorCB.getSelectedIndex());
		prop.setProperty("operator_name", ""+operatorCB.getSelectedValue());
		prop.setProperty("value", ""+valueCB.getSelectedValue());

		//guadamos las opciones avanzadas	
		prop.setProperty("layerAdvanced_index", ""+layerAdvancedCB.getSelectedIndex());
		prop.setProperty("layerAdvanced_name", ""+layerAdvancedCB.getSelectedValue());
		prop.setProperty("attributeAdvanced_index", ""+attributeAdvancedCB.getSelectedIndex());
		prop.setProperty("attributeAdvanced_name", ""+attributeAdvancedCB.getSelectedValue());
		prop.setProperty("functionAdvanced_index", ""+functionAdvancedCB.getSelectedIndex());
		prop.setProperty("functionAdvanced_name", ""+functionAdvancedCB.getSelectedValue());
		prop.setProperty("operatorAdvanced_index", ""+operatorAdvancedCB.getSelectedIndex());
		prop.setProperty("operatorAdvanced_name", ""+operatorAdvancedCB.getSelectedValue());
		prop.setProperty("valueAdvanced", ""+valueAdvancedCB.getSelectedValue());
		prop.setProperty("advancedOptions", ""+advancedCB.getState());
		prop.setProperty("attAsociationWithLayer1_index", ""+asociationWithLayer1CB.getSelectedIndex());
		prop.setProperty("attAsociationWithLayer1_name", ""+asociationWithLayer1CB.getSelectedValue());
		prop.setProperty("attasociationWithLayer2_index", ""+asociationWithLayer2CB.getSelectedIndex());
		prop.setProperty("attasociationWithLayer2_name", ""+asociationWithLayer2CB.getSelectedValue());

		BFileChooser bfc = new BFileChooser(BFileChooser.SAVE_FILE,
				I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.saveas"));
		if (bfc.showDialog(this)) {
			try {
				File f = bfc.getSelectedFile();
				prop.store(new FileOutputStream(f), "Query file for Sqi4jump");
			}
			catch(FileNotFoundException e) {
				context.getWorkbenchFrame().warnUser(e.getMessage());
			}
			catch(IOException e) {
				context.getWorkbenchFrame().warnUser(e.getMessage());
			}
		}

	}

	private List executeQueryAdvanced(QueryDialog queryDialog, PlugInContext context , 
			List okFeatures) {
		
		List okFeaturesEncontradas = new ArrayList();

		// New condition
		Condition conditionAux = new Condition(queryDialog, context, true);
		
		// The FeatureSelection before the query
		IFeatureSelection selectedFeatures = context.getLayerViewPanel()
		.getSelectionManager()
		.getFeatureSelection();

		// srcFeaturesMap keys are layers to query
		// srcFeaturesMap values are collection of features to query
		CollectionMap srcFeaturesMap = new CollectionMap();
		
		int total = 0; // total number of objects to scan
		int featuresfound = 0;
		for (int i = 0 ; i < layersAdvanced.size() ; i++) {
			total += ((Layer)layersAdvanced.get(i)).getFeatureCollectionWrapper().size();
		}
		// Set the selection used as target for geometric operations
		if (operator.type=='G' && valueAdvancedCB.getSelectedIndex()==0) {
			selection = context.getLayerViewPanel().getSelectionManager().getSelectedItems();
		}

		
		// Loop on the requested layers
		int count = 0;
		for (int i = 0 ; i < layersAdvanced.size() ; i++) {
			Layer layer = (Layer)layersAdvanced.get(i);
			FeatureCollection fc = layer.getFeatureCollectionWrapper();

			// When the user choose all layers, some attributes are not
			// available for all attributes
			if(attributeTypeAdvanced!='G' && !fc.getFeatureSchema().hasAttribute(attributeAdvanced)) continue;

			//monitor.report(layer.getName());
			Collection features = null;

			features = fc.getFeatures();

			FeatureCollection dataset = new FeatureDataset(fc.getFeatureSchema());

			// initialize a new list for the new selection
			List okFeaturesAdvanced = new ArrayList();
			try {
				for (Iterator it = features.iterator() ; it.hasNext() ; ) {
					count++;
					/*if (count%10==0) {
						progressBar.setProgressText(""+count+"/"+total);
						progressBar.setValue(count);
					}*/
					Feature f = (Feature)it.next();
					if (conditionAux.test(f)) {
						okFeaturesAdvanced.add(f);
						if (cancelQuery) break;
						Thread.yield();
					}
				}

			}
			catch(Exception e) {
				if (logger.isDebugEnabled())
				{
					logger.debug("run() - excepcion ",e);
				}
			}
			if (cancelQuery) break;

			if (okFeaturesAdvanced.size()==0) continue;

			
			Iterator okFeaturesIter = okFeatures.iterator();
			for(int j=0; okFeaturesIter.hasNext(); j++){
				GeopistaFeature gF= (GeopistaFeature)okFeaturesIter.next();
				
				String att1 = (String)asociationWithLayer1CB.getSelectedValue();
				String attributeWithLayer1 = att1.substring(0, att1.lastIndexOf(' '));
				
				Object ogf1 = gF.getAttribute(attributeWithLayer1);
				
				Iterator okFeaturesAdvancesIter = okFeaturesAdvanced.iterator();
				for(int k=0; okFeaturesAdvancesIter.hasNext(); k++){
					GeopistaFeature gFA = (GeopistaFeature)okFeaturesAdvancesIter.next();
					
					String att2 = (String)asociationWithLayer2CB.getSelectedValue();
					String attributeWithLayer2 = att2.substring(0, att2.lastIndexOf(' '));
					
					
					Object ogf2 =gFA.getAttribute(attributeWithLayer2);
					if(ogf2 != null){
						if(ogf1.toString().equals(ogf2.toString())){
							okFeaturesEncontradas.add(gFA);
							featuresfound++;
						}
					}
					
				}
				
			}
						
		}
		commentsAdvanced.setText(
				I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.comments.found") + 
				" : " + featuresfound
		);
		
		return okFeaturesEncontradas;
		
	}
	

	void executeQuery() {
		final QueryDialog queryDialog = this; 
		Runnable runnable = new Runnable() {


			public void run() {
				// QueryDialog static boolean flag which is true while a 
				// query is running
				runningQuery=true;
				cancelQuery=false;

				/*String att = (String)attributeCB.getSelectedValue();
				attribute = att.substring(0, att.lastIndexOf(' '));
				queryDialog.attribute = attribute;
				//operatorChanged();
				queryDialog.attributeType = attributeType;
				value = (String)valueCB.getSelectedValue();
				queryDialog.value = value;*/
				
				// New condition
				Condition condition = new Condition(queryDialog, context, false);
				if(!advancedCB.getState()){
				comments.setText(
						I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.comments.select") +
						" \"" + layerCB.getSelectedValue() + "\" " + 
						I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.comments.features") + " " +
						condition + "..."
				);
				}

				// The FeatureSelection before the query
				IFeatureSelection selectedFeatures = context.getLayerViewPanel()
				.getSelectionManager()
				.getFeatureSelection();

				// srcFeaturesMap keys are layers to query
				// srcFeaturesMap values are collection of features to query
				CollectionMap srcFeaturesMap = new CollectionMap();

				int total = 0; // total number of objects to scan
				int featuresfound = 0;
				if (layerCB.getSelectedIndex()==1) {
					for (Iterator it = selectedFeatures.getLayersWithSelectedItems().iterator() ; it.hasNext() ; ) {
						Layer layer = (Layer)it.next();
						srcFeaturesMap.put(layer, selectedFeatures.getFeaturesWithSelectedItems(layer));
					}
					total = srcFeaturesMap.size();
				}
				else {
					for (int i = 0 ; i < layers.size() ; i++) {
						total += ((Layer)layers.get(i)).getFeatureCollectionWrapper().size();
					}
				}

				// Set the selection used as target for geometric operations
				if (operator.type=='G' && valueCB.getSelectedIndex()==0) {
					selection = context.getLayerViewPanel().getSelectionManager().getSelectedItems();
				}

				System.out.println(condition);

				// initialize the selection if the select option is true
				if(select.getState()) {selectedFeatures.unselectItems();}

				// initialization for infoframe
				InfoFrame info = null;
				if(display.getState()) {
					if (!showedAllredy){
						info = new InfoFrame(context.getWorkbenchContext(),
								(LayerManagerProxy)context,
								(TaskFrame)context.getWorkbenchFrame().getActiveInternalFrame());
						showedAllredy = true;
						taskFrame = (TaskFrame)context.getWorkbenchFrame().getActiveInternalFrame();
					} else{
							info = new InfoFrame(context.getWorkbenchContext(),
									(LayerManagerProxy)context,
									taskFrame);
					}

				}

				// Loop on the requested layers
				int count = 0;
				for (int i = 0 ; i < layers.size() ; i++) {
					Layer layer = (Layer)layers.get(i);
					FeatureCollection fc = layer.getFeatureCollectionWrapper();

					// When the user choose all layers, some attributes are not
					// available for all attributes
					if(attributeType!='G' && !fc.getFeatureSchema().hasAttribute(attribute)) continue;

					//monitor.report(layer.getName());
					Collection features = null;
					// case 1 : query only selected features
					if (layerCB.getSelectedIndex()==1) {
						features = (Collection)srcFeaturesMap.get(layer);
					}
					// other cases : query the whole layer
					else {
						features = fc.getFeatures();
					}
					// initialize a new dataset
					progressBarTitle.setText(layer.getName());
					progressBarTitle.getParent().layoutChildren();
					progressBar.setMinimum(0);
					progressBar.setMaximum(total);
					progressBar.setValue(0);
					progressBar.setProgressText("0/"+total);
					progressBar.setShowProgressText(true);

					FeatureCollection dataset = new FeatureDataset(fc.getFeatureSchema());

					// initialize a new list for the new selection
					List okFeatures = new ArrayList();
					try {
						for (Iterator it = features.iterator() ; it.hasNext() ; ) {
							count++;
							if (count%10==0) {
								progressBar.setProgressText(""+count+"/"+total);
								progressBar.setValue(count);
							}
							Feature f = (Feature)it.next();
							if (condition.test(f)) {
								okFeatures.add(f);
								featuresfound++;
								if (cancelQuery) break;
								Thread.yield();
							}
						}
						progressBar.setProgressText(""+count+"/"+total);
						progressBar.setValue(count);
					}
					catch(Exception e) {
						if (logger.isDebugEnabled())
						{
							logger.debug("run() - excepcion ",e);
						}
					}
					if (cancelQuery) break;

					if (okFeatures.size()==0) continue;

					if(!advancedCB.getState()){
						//no se han configurado las opciones avanzadas
					if(select.getState()) {
						selectedFeatures.selectItems(layer, okFeatures);
					}

					if(create.getState() || display.getState()) {
						dataset.addAll(okFeatures);
					}
					if(create.getState()) {
						context.getLayerManager().addLayer(
								context.getLayerManager().getCategory(layer).getName(),
								layer.getName()+"_"+value, dataset
						);
					}
					if(display.getState()) {
						info.getModel().add(layer, okFeatures);
						//context.getWorkbenchFrame().addInternalFrame(info);
					}
					}
					else{
						// se han configurado las opciones avanzadas
						
						okFeatures = executeQueryAdvanced(queryDialog, context ,okFeatures);
						if (okFeatures.size()==0) continue;
						
						layersAdvanced.clear();
						layersAdvanced.add(layerAdvancedCB.getSelectedValue());
						for (int h = 0 ; h < layersAdvanced.size() ; h++) {
							Layer layerAdvanced = (Layer)layersAdvanced.get(h);
						
							
							
							if(select.getState()) {
								selectedFeatures.selectItems(layerAdvanced, okFeatures);
							}
		
							if(create.getState() || display.getState()) {
								dataset.addAll(okFeatures);
							}
							if(create.getState()) {
								context.getLayerManager().addLayer(
										context.getLayerManager().getCategory(layerAdvanced).getName(),
										layerAdvanced.getName()+"_"+value, dataset
								);
							}
							if(display.getState()) {
								info.getModel().add(layerAdvanced, okFeatures);
								//context.getWorkbenchFrame().addInternalFrame(info);
							}
						}
						
					}	

				}
				if (cancelQuery) {
					initVariables();
					comments.setText(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.comments.interruption")); 
					return;
				}
				progressBarTitle.setText(I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.comments.resultdisplay"));
				progressBar.setIndeterminate(true);
				if(!advancedCB.getState()){
				comments.setText(
						I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.comments.select") +
						" \"" + layerCB.getSelectedValue() + "\" " + 
						I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.comments.features") + " " +
						condition + " : " + featuresfound + " " +
						I18NPlug.get(QueryPlugIn.pluginname, "jump.query.dialog.comments.found")
				);
				}
				// update the selection attribute
				if(select.getState()) {
					selection = context.getLayerViewPanel().getSelectionManager().getSelectedItems();
				}

				if(display.getState()) {
					context.getWorkbenchFrame().addInternalFrame(info);
				}

				// init ComboBoxes to add new layers
				if (create.getState()) { initComboBoxes(); }  

				progressBar.setIndeterminate(false);
				progressBar.setValue(progressBar.getMaximum());

				//comments.setText("Select from " + layerCB.getSelectedValue() + " where " + condition + " : " + total + " features found");
				initVariables();
			}
		};
		Thread t = new Thread(runnable);
		// Set a low priority to the thread to keep a responsive interface
		// (progresBar progression and interruption command)
		t.setPriority(Thread.currentThread().getPriority()-1);
		// start the thread
		t.start();
	}

	private void ok() {executeQuery();}

	private void cancel() {setVisible(false);}

	private void stop() {if (runningQuery) cancelQuery = true;}

	private void refresh() {initComboBoxes();}

	private void exit() {setVisible(false);}
  
}
