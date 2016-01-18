/**
 * LegendPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.legend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import org.deegree.graphics.sld.FeatureTypeStyle;
import org.deegree.graphics.sld.Rule;
import org.deegree.graphics.sld.UserStyle;
import org.deegree_impl.graphics.sld.Rule_Impl;

import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.style.sld.model.SLDFactory;
import com.geopista.style.sld.model.SLDStyle;
import com.geopista.style.sld.ui.impl.UIUtils;
import com.geopista.util.Rango;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.IViewport;
import com.vividsolutions.jump.workbench.ui.Viewport;

/**
 * TODO Documentación
 * 
 * @author juacas
 * 
 */
public class LegendPanel extends JPanel {

	// this map is used to group rules with same name and filter
	// in order to render together although they were defined apart
	HashMap layersMap = new HashMap(); // Map of layers

	private JScrollPane jScrollPane = null;

	private JPanel legendListPanel = null;

	private boolean editingMode = false;

	private JPanel outerPanel = null;

	private int symbolWidth = 30;

	private int symbolHeight = 20;

	private IViewport viewport = null;

	private float ruleFontDivisor = 3.2f;

	private float layerFontDivisor = 2f;

	// Aqui se definen las reglas de pintado que no queremos que aparezcan en la leyenda
	private String[] valoresNoUsados = {"Revision Expirada"};
	
	/**
	 * This is the default constructor
	 */
	public LegendPanel() {
		super();
		initialize();
	}

	public LegendPanel(int symbolWidth, int symbolHeight) {
		this.symbolWidth = symbolWidth;
		this.symbolHeight = symbolHeight;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setOpaque(true);
		this.add(getOuterPanel(), java.awt.BorderLayout.CENTER);
		this.setBackground(Color.white);

	}

	public void reset() {
		legendListPanel.removeAll();
		layersMap = new HashMap();
		invalidate();
	}

	/**
	 * @param layersToPrint
	 */
	public void initializeFromLayers(Collection layersToPrint) {
		initializeFromLayers(layersToPrint, 0);
		
		/*Iterator layers = layersToPrint.iterator();
		while (layers.hasNext()) {
			Layer lay = (Layer) layers.next();
			addLayer(lay);
		}*/
	}
	public void initializeFromLayers(Collection layersToPrint, double escala) {
		Iterator layers = layersToPrint.iterator();
		while (layers.hasNext()) {
			Layer lay = (Layer) layers.next();
			addLayer(lay, escala);
		}
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getOuterPanel());
			jScrollPane.getViewport().setBackground(Color.white);
			jScrollPane.setBackground(Color.white);
		}

		return jScrollPane;
	}

	/**
	 * This method initializes legendListPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getLegendListPanel() {
		if (legendListPanel == null) {
			legendListPanel = new JPanel();
			// legendListPanel.setLayout(new BoxLayout(legendListPanel,
			// BoxLayout.Y_AXIS));
			legendListPanel.setLayout(new LegendLayout2());
			// legendListPanel.setBorder(BorderFactory.createLineBorder(Color.RED,3));

			legendListPanel.setOpaque(true);
			legendListPanel.setBackground(Color.WHITE);
			// legendListPanel.addMouseListener(new
			// java.awt.event.MouseAdapter() {
			// public void mouseExited(java.awt.event.MouseEvent e) {
			// setEditingMode(false);
			// validate();
			// }
			// public void mouseEntered(java.awt.event.MouseEvent e) {
			// setEditingMode(true);
			// validate();
			// }
			// });

		}
		return legendListPanel;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getOuterPanel() {
		if (outerPanel == null) {
			outerPanel = new JPanel();
			outerPanel.setBackground(java.awt.SystemColor.controlHighlight);
			outerPanel.setLayout(new BorderLayout());
			outerPanel.add(getLegendListPanel(), java.awt.BorderLayout.CENTER);
			outerPanel.setOpaque(true);
		}
		return outerPanel;
	}

	private boolean elasticMode = true;

	/**
	 * Activa el modo de autolayout o el gobernado por BreakEntry a
	 * 
	 * @param b
	 */
	public void setAutoLayout(boolean b) {
		elasticMode = b;
		((LegendLayout2) getLegendListPanel().getLayout()).setElasticMode(b);
		setEditingMode(editingMode);
		validate();
	}

	/**
	 * 
	 */
	public void addBreak() {
		BreakEntry br = new BreakEntry();
		br.setBackground(Color.white);
		br.setSymbolSize(symbolWidth, symbolHeight);
		br.setEditingMode(editingMode);
		addEntry(br);
		validate();
	}

	public void addLayer(Layer layer) {
		addLayer(layer, null);
	}
	public void addLayer(Layer layer, double escala) {
		addLayer(layer, escala, null);
	}

	public void addLayer(Layer layer, Viewport viewport) {

		addLayer (layer, 0, viewport);
	}
	
	public void addLayer(Layer layer, double escala, Viewport viewport) {

		// Hay que cambiarlo para que no muestre el icono en la capa, sino en el estilo
		LayerEntryPanel lyrEntry = new LayerEntryPanel(layer, symbolWidth,
				symbolHeight, false);
		lyrEntry.setFont_divisor(layerFontDivisor);
		lyrEntry.setIndentation(false);
		lyrEntry.setSymbolSize(symbolWidth, symbolHeight);
		lyrEntry.setSymbolPanel(new SymbolizerPanel());
		lyrEntry.setBackground(Color.white);

		layersMap.put(layer, lyrEntry);
		legendListPanel.add(lyrEntry);
		SLDStyle style = (SLDStyle) layer.getStyle(SLDStyle.class);
		if (style != null) {
			this.addStyle(layer, style, viewport, escala);
		} else {
			//System.out.println(layer.getName() + " -- Sin estilos");
		}
		lyrEntry.setEditingMode(editingMode);
		
		SymbolizerPanel symPanel = lyrEntry.getSymbolPanel();
		Iterator rulIt = lyrEntry.getRules().values().iterator();
		while (rulIt.hasNext()) {
			RuleEntryPanel ruleEntry = (RuleEntryPanel) rulIt.next();
			symPanel
			.addSymbolizers(ruleEntry.getSymbolPanel().getSymbolizers());
			ruleEntry.setBackground(Color.white);
		}		
	}
	
	
	public void addStyle(Layer layer, SLDStyle style, IViewport viewport, double escala) {

		UserStyle currentStyle = style
		.getUserStyle(style.getCurrentStyleName());
		LayerEntryPanel lyrEntry = (LayerEntryPanel) layersMap.get(layer);

		if (lyrEntry == null) {// añade un representador de capas
			addLayer(layer);
			lyrEntry = (LayerEntryPanel) layersMap.get(layer);
		}

		HashMap rulesMap = (HashMap) lyrEntry.getRules();

		if (currentStyle != null) {
			// TODO: There is no concept of FeatureTypeName in JUMP.
			// All features of a layer are generated form one table, hence, no
			// featureTypeName is needed.
			// However, in GeoPISTA, features from a Layer may come from
			// different
			// tables,
			// therefore, they must identified with a featureTypeName
			// String featureTypeName = f.getFeatureTypeName();
			FeatureTypeStyle[] fts = currentStyle.getFeatureTypeStyles();

			String pastRuleFilter = "";
			for (int k = 0; k < fts.length; k++) {

				Rule[] rules = fts[k].getRules(); // distintas reglas de pintado				

				double scaleDenominator = 10;
				double minScaleDenominator = 1;
				double maxScaleDenominator = 10000;
				// si no se especifica el viewport se usa el configurado
				// por defecto. (puede ser null tambien)
				if (viewport == null)
					viewport = this.viewport;

				Rango actualRange;
				if (viewport != null) {
					scaleDenominator = computeScaleDenominator(viewport);
				}
				if (escala !=0)
					scaleDenominator = escala;
				
				actualRange = new Rango(scaleDenominator);

				//Se agrupan por condición las reglas de la capa
				Hashtable htScales = sortRules(rules);

				//Para que primero muestre los estilos no temáticos (sin condiciones/reglas) 
				//para todas las escalas posibles:
				Iterator itScales = htScales.keySet().iterator();
				while (itScales.hasNext()) {
					Rango range = (Rango) itScales.next();
					if (range.contains(actualRange)) {
						Hashtable htRules = (Hashtable) htScales.get(range);
						if (htRules.containsKey("")) {
							insertLegendEntry(layer, (Vector) htRules.get(""),
							"");
						}
					}
				}

				itScales = htScales.keySet().iterator();
				int size = valoresNoUsados.length;
				while (itScales.hasNext()) {
					Rango range = (Rango) itScales.next();
					if (range.contains(actualRange)) {
						Hashtable htRules = (Hashtable) htScales.get(range);
						htRules.remove("");
						Iterator itRules = htRules.keySet().iterator();
						while (itRules.hasNext()) {
							String nomRule = (String) itRules.next();
							Vector vRules = (Vector) htRules.get(nomRule);
							
							// Con esto evitamos que aparezcan en la leyenda, ciertas reglas de pintado, por ejemplo, las reglas de pintado  
							// que se han definido para contemplar el estado temporal de la EIEL.		
							boolean encontrado = false;
							int i = 0;
							while ((i<size) && !encontrado){
								if (nomRule.equals(valoresNoUsados[i]))
									encontrado = true;
								i++;
							}
							if (!encontrado)
								insertLegendEntry(layer, vRules, nomRule);
						}
					}
				}

				/*//Para que primero muestre los estilos no temáticos (sin condiciones/reglas)
		 //(pero sin tener en cuenta que en los distintos rangos puede haber estilos no temáticos)
		Iterator itScales = htScales.keySet().iterator();
		while (itScales.hasNext())
		{
			Rango range = (Rango)itScales.next();
			if (range.contains(actualRange))
			{
				Hashtable htRules = (Hashtable)htScales.get(range);

				//Para que primero muestre los estilos no temáticos (sin condiciones/reglas):
				if (htRules.containsKey(""))
				{
					insertLegendEntry(layer, (Vector)htRules.get(""), "");
					htRules.remove("");
				}
				Iterator itRules = htRules.keySet().iterator();
				while (itRules.hasNext())
				{	
					String nomRule = (String)itRules.next();
					Vector vRules = (Vector)htRules.get(nomRule);
					insertLegendEntry(layer, vRules, nomRule);								
				}
			}
		}
				 */
			}
		}

	}
	

	/**
	 * Añade el estilo de una capa a la leyenda
	 * 
	 * @param style
	 */
	public void addStyle(Layer layer, SLDStyle style) {
		addStyle(layer, style, null);
	}

	public void addStyle(Layer layer, SLDStyle style, Viewport viewport) {
		
		addStyle (layer, style, viewport, 0);		
		
	}

	private void insertLegendEntry(Layer layer, Vector vRules, String nomRule) {

		RuleEntryPanel lgndEntry;
		if (layer.getFeatureCollectionWrapper().getFeatureSchema() instanceof GeopistaSchema) {
			//Si la capa es de geopista, tiene al menos un estilo sld. Creamos una entrada de leyenda
			//Con el nombre de la capa RuleEntryPanel (rule, int,int, GeopistaLayer)
			for (int j = 0; j < vRules.size(); j++) {
				Rule_Impl rule = (Rule_Impl) vRules.get(j);
						
				/* OPCION PARA NO PINTAR LAS REGLAS
				if ((rule!=null) && (rule.getName()!=null) && ((rule.getName().equals("Revision expirada Publicable Textos")) ||
						(rule.getName().equals("Revision expirada Temporal Textos"))))
					continue;
					*/

				if (j == 0 && nomRule.trim().length() != 0)
					addEntry(new RuleEntryPanel(rule, symbolWidth,
							symbolHeight, nomRule));

				lgndEntry = new RuleEntryPanel(rule, symbolWidth, symbolHeight,
						(GeopistaLayer) layer);
				lgndEntry.setEditingMode(editingMode);
				lgndEntry.setIndentation(true);
				lgndEntry.setBackground(Color.white);
				addEntry(lgndEntry);
			}
		} else //Si no es una capa de geopista
		{
			lgndEntry = new RuleEntryPanel(null, symbolWidth, symbolHeight);
		}
	}

	private Hashtable sortRules(Rule[] rules) {

		Hashtable htScales = new Hashtable();

		double scaleDenominator = 10;
		double minScaleDenominator = 1;
		double maxScaleDenominator = 100000;
		// si no se especifica el viewport se usa el configurado
		// por defecto. (puede ser null tambien)

		for (int i = 0; i < rules.length; i++) {
			Hashtable htRules = new Hashtable();
			minScaleDenominator = rules[i].getMinScaleDenominator();
			maxScaleDenominator = rules[i].getMaxScaleDenominator();
			//String scale = minScaleDenominator+"-"+maxScaleDenominator;			
			Rango range = new Rango(minScaleDenominator, maxScaleDenominator);

			Vector vRules = new Vector();
			if (htScales.get(range) != null)
				htRules = (Hashtable) htScales.get(range);

			String filterProperty = getFilterProperty(rules[i]);
			if (htRules.get(filterProperty) != null)
				vRules = (Vector) htRules.get(filterProperty);

			vRules.add(rules[i]);
			htRules.put(filterProperty, vRules);
			htScales.put(range, htRules);
		}

		return htScales;
	}

	public String getFilterProperty(Rule rule) {
		if (rule.getFilter() != null)
			return UIUtils.getFilterProperty(rule.getFilter());
		else
			return "";

	}

	// miguel

	public static String cogerDominio(GeopistaLayer layer, String col,
			String text) {
		Domain a;

		GeopistaSchema MySchema = (GeopistaSchema) layer
				.getFeatureCollectionWrapper().getFeatureSchema();
		a = MySchema.getColumnByAttribute(col).getDomain();
		int domainType = a.getType();

		Iterator domainChildren = a.getChildren().iterator();

		if (domainType == Domain.CODEDENTRY || domainType == Domain.CODEBOOK
				|| domainType == Domain.TREE) {
			while (domainChildren.hasNext()) {
				Domain domainChild = (Domain) domainChildren.next();
				if (text.equals(domainChild.getPattern())) {
					return domainChild.getRepresentation();
				}
			}

			return text;
		}

		return text;
	}

	////

	/**
	 * Método que calcula la escala de un mapa a partir del Bounding Box, la
	 * anchura y altura del mismo
	 * 
	 * @return La escala del mapa
	 */
	private double computeScaleDenominator(IViewport viewport) {

		Envelope envelope = viewport.getEnvelopeInModelCoordinates();
		double scale = 0.0;
		double xmin = envelope.getMinX();
		double ymin = envelope.getMinY();
		double xmax = envelope.getMaxX();
		double ymax = envelope.getMaxY();
		/* Calculate the width and height of the map in degrees */
		double widthMap = xmax - xmin;
		double heightMap = ymax - ymin;
		/* Now we must calculate the map extent in metres */
		// double widthMetres = (widthMap*(6378137*2*Math.PI))/360;
		// double heightMetres = (heightMap*(6378137*2*Math.PI))/360;
		double widthMetres = widthMap;
		double heightMetres = heightMap;
		/* Now we calculate the scale */
		scale = ((widthMetres / viewport.getPanel().getWidth()) / 0.00028);
		return scale;
	}

	/**
	 * @param lgndEntry
	 */
	private LegendEntryPanel addEntry(LegendEntryPanel lgndEntry) {
		legendListPanel.add(lgndEntry);
		return lgndEntry;
	}

	public void setEditingMode(boolean editingMode) {
		this.editingMode = editingMode;
		for (int i = 0; i < legendListPanel.getComponentCount(); i++) {
			if (legendListPanel.getComponent(i) instanceof LegendEntryPanel)
				((LegendEntryPanel) legendListPanel.getComponent(i))
						.setEditingMode(editingMode);
		}
		/**
		 * Hides sole rules
		 */
		Iterator iter = layersMap.values().iterator();
		while (iter.hasNext()) {
			LayerEntryPanel element = (LayerEntryPanel) iter.next();
			element.setEditingMode(editingMode);
		}
		validate();
	}

	public void setSymbolSize(int w, int h) {
		if (w == 0 || h == 0)
			return;

		this.symbolHeight = h;
		this.symbolWidth = w;
		for (int i = 0; i < legendListPanel.getComponentCount(); i++) {
			if (legendListPanel.getComponent(i) instanceof LegendEntryPanel)
				((LegendEntryPanel) legendListPanel.getComponent(i))
						.setSymbolSize(w, h);
		}
		validate();
	}

	public void setSymbolHeight(int h) {
		if (h == 0)
			return;

		this.symbolHeight = h;
		this.symbolWidth = (int) (h * LegendEntryPanel.SYMBOLPANEL_RATIO);
		for (int i = 0; i < legendListPanel.getComponentCount(); i++) {
			if (legendListPanel.getComponent(i) instanceof LegendEntryPanel)
				((LegendEntryPanel) legendListPanel.getComponent(i))
						.setSymbolHeigth(h);
		}
		validate();
	}

	/**
	 * @param viewport
	 */
	public void setViewport(IViewport viewport) {
		this.viewport = viewport;
	}

	public Collection getSelectedLayers() {
		ArrayList selectedLayers = new ArrayList();
		Iterator layers = layersMap.entrySet().iterator();
		while (layers.hasNext()) {
			Map.Entry element = (Map.Entry) layers.next();
			LegendEntryPanel entry = (LegendEntryPanel) element.getValue();
			if (entry.isSelected())
				selectedLayers.add((Layer) element.getKey());
		}
		return selectedLayers;
	}

	public void setOpaque(boolean arg0) {

		super.setOpaque(arg0);
		getOuterPanel().setOpaque(arg0);
		getJScrollPane().setOpaque(arg0);
		getLegendListPanel().setOpaque(arg0);
		if (legendListPanel == null)
			return;
		for (int i = 0; i < legendListPanel.getComponentCount(); i++) {
			if (legendListPanel.getComponent(i) instanceof LegendEntryPanel)
				((LegendEntryPanel) legendListPanel.getComponent(i))
						.setOpaque(arg0);
		}
	}

	/**
	 * @param panel
	 */
	public void setLegendListPanel(JPanel panel) {
		getOuterPanel().removeAll();
		getOuterPanel().add(panel, BorderLayout.NORTH);
	}

	public int getSymbolHeight() {
		return symbolHeight;
	}

	public int getSymbolWidth() {
		return symbolWidth;
	}

	/**
	 * 
	 */
	public void removeLastBreak() {
		Component[] comps = this.getLegendListPanel().getComponents();
		for (int i = comps.length - 1; i > 0; i--) {
			Component component = comps[i];
			if (component instanceof BreakEntry) {
				getLegendListPanel().remove(component);
				break;
			}
		}
		this.validate();
	}

	/**
	 * @param i
	 */
	public void setRuleFontDivisor(float i) {
		this.ruleFontDivisor = i;
		Component comp[] = getLegendListPanel().getComponents();
		for (int j = 0; j < comp.length; j++) {
			Component component = comp[j];
			if (component instanceof RuleEntryPanel) {
				RuleEntryPanel rulepanel = (RuleEntryPanel) component;
				rulepanel.setFont_divisor(i);
			}
		}
		validate();
	}

	public void setLayerFontDivisor(float i) {
		this.layerFontDivisor = i;
		Component comp[] = getLegendListPanel().getComponents();
		for (int j = 0; j < comp.length; j++) {
			Component component = comp[j];
			if (component instanceof LayerEntryPanel) {
				LayerEntryPanel layerpanel = (LayerEntryPanel) component;
				layerpanel.setFont_divisor(i);
			}
		}
		validate();
	}

	/**
	 * @return Returns the elasticMode.
	 */
	public boolean isAutoLayout() {
		return elasticMode;
	}

	public static void main(String[] args) {
		JFrame fr = new JFrame("prueba de legend");
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setSize(400, 200);
		fr.getContentPane().setBackground(Color.LIGHT_GRAY);
		final LegendPanel pan = new LegendPanel(40, 20);
		pan.setSymbolSize(40, 20);
		pan.setAutoLayout(true);
		pan.setEditingMode(false);
		pan.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
		LegendPanel.testInicialization(pan);
		JToggleButton title = new JToggleButton("Edit", true);
		title.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				pan.setEditingMode(e.getStateChange() == ItemEvent.SELECTED);

			}
		});
		title.setOpaque(true);
		pan.setOpaque(true);
		pan.add(title, BorderLayout.NORTH);

		fr.getContentPane().setLayout(new BorderLayout());
		fr.getContentPane().add(pan, BorderLayout.CENTER);
		// Carga los estilos
		// testInicialization(pan);

		fr.show();
		fr.pack();
	}

	/**
	 * @param pan
	 */
	public static void testInicialization(LegendPanel pan) {
		SLDStyle style = SLDFactory.createSLDStyle("testStyle.xml", "default",
				"tramosvia");
		SLDStyle style2 = SLDFactory.createSLDStyle("styles.xml",
				"default:AreaConstruida", "masa");

		Layer lay = new Layer();
		lay.addStyle(style);
		lay.setName("testLayer");
		Layer lay2 = new Layer();
		lay2.setName("testLayer 2");
		// pan.addStyle(lay,style);
		pan.addLayer(lay);
		pan.addBreak();
		lay2.addStyle(style2);
		// pan.addStyle(lay2, style);
		pan.addLayer(lay2);

	}

}
