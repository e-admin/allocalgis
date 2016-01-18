/**
 * RuleEntryPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.legend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.deegree.graphics.sld.Rule;
import org.deegree.graphics.sld.Symbolizer;

import com.geopista.model.GeopistaLayer;
import com.geopista.style.sld.ui.impl.UIUtils;
import com.geopista.ui.components.JMultilineLabel;
import com.geopista.ui.components.MultiLineLabelUI;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class RuleEntryPanel extends LegendEntryPanel
{
	GeopistaLayer layer;
	
	/**
	 * 
	 */
	public RuleEntryPanel() {
		
		super();
	}
	
	/**
	 * @param w
	 * @param h
	 * @param elasticMode
	 */
	public RuleEntryPanel(int w, int h, boolean elasticMode) {
		super(w,h,elasticMode);
	}

	
	
	/**
	 * This is the default constructor
	 */
	public RuleEntryPanel(Rule rule, int w, int h) {
		super(w,h,false);
		font_divisor=3.2f;
		this.rule = rule;
		initialize();
		if (rule!=null)
		{
			addSymbolizers(rule.getSymbolizers());

			if (rule.getFilter()!=null)
			{
//				Alternativamente utiliza una sola etiqueta por motivos de espacio
				StringBuffer desc=new StringBuffer();

//				desc.append(rule.getName());
//				desc.append(":");
				desc.append("(");
				desc.append(UIUtils.createStringFromFilter(rule.getFilter()));
				desc.append(")");
				getRuleTitleLabel().setVisible(false);
				getFilterLabel().setText(desc.toString());
			}
			else
				getRuleTitleLabel().setText(rule.getName());
		}
	}
	

	//Constructor de una fila, con el nombre de la propiedad (el titulo del grupo sld)
	public RuleEntryPanel(Rule rule, int w, int h, String nomProperty)
	{
		super(w,h,false);
		font_divisor=1.4f;
		BorderLayout borderLayout= new BorderLayout(0,0);

		this.setBackground(Color.white);
		this.setLayout(borderLayout);

		addSymbolizers(rule.getSymbolizers());
		JPanel aux = new JPanel();
		aux.setBackground(Color.white);
		this.add(aux, BorderLayout.WEST);
		getRuleTitleLabel().setText(nomProperty);
		this.add(getTextPanel(), BorderLayout.CENTER);
		
	}
	
	
	
	
	//Constructor de una fila, con el nombre de la propiedad (el titulo del grupo sld)
	public RuleEntryPanel(Rule rule, int w, int h, boolean Property)
	{
		super(w,h,false);
		font_divisor=1.4f;
		BorderLayout borderLayout= new BorderLayout(0,0);

		this.setBackground(Color.white);
		this.setLayout(borderLayout);

		addSymbolizers(rule.getSymbolizers());
		JPanel aux = new JPanel();
		aux.setBackground(Color.white);
		this.add(aux, BorderLayout.WEST);
		this.add(getTextPanel(), BorderLayout.CENTER);

		if (rule.getFilter()!=null)
		{
			getRuleTitleLabel().setVisible(false);
			getFilterLabel().setText(getFilterProperty(rule));
		}
		else
		{
			getRuleTitleLabel().setText(rule.getName());
		}

	}
	
	//Constructor de una fila de un sdl de una capa de Geopista (Todos con iconos)
	public RuleEntryPanel(Rule rule, int w, int h, GeopistaLayer layer)
	{
		super(w,h,false);
		if (layer!=null)
		{
			this.layer=layer;
		}
		
		font_divisor=1.8f;
		this.rule = rule;
		initialize();
		addSymbolizers(rule.getSymbolizers());

		
		//this.add(getTextPanel(), BorderLayout.CENTER);
		
		if (rule.getFilter()!=null)
		{
			StringBuffer desc=new StringBuffer();
			desc.append("");
			desc.append(UIUtils.createStringDomainFromFilter2(rule.getFilter(),this.layer,false));
			getRuleTitleLabel().setVisible(false);
			getFilterLabel().setText(desc.toString());
		}
		else
			getRuleTitleLabel().setText(rule.getName());
	}




	//Devuelve el nombre de la propiedad del sdl
	public String getFilterProperty (Rule rule)
	{
		if (rule.getFilter()!=null)
			return UIUtils.getFilterProperty (rule.getFilter());
		else
			return "";
		
	}
	
	/**
	 * @param symbolizers
	 */
	public void addSymbolizers(Symbolizer[] symbs)
	{
		symbolizers.addAll(Arrays.asList(symbs));
		getSymbolPanel().setSymbolizers(symbolizers);
	}
	/**
	 * @return
	 */
	private JLabel getRuleTitleLabel()
	{
		if (ruleTitleLabel==null)
		{
			ruleNameFont= new Font("Arial",Font.PLAIN,(int) (getSymbolHeight()/font_divisor));
			ruleTitleLabel = new JLabel();
			ruleTitleLabel.setOpaque(true);
			ruleTitleLabel.setText("title");
			ruleTitleLabel.setFont(this.ruleNameFont);
			ruleTitleLabel.setUI(new MultiLineLabelUI());
			ruleTitleLabel.setVisible(true);	
			ruleTitleLabel.setBackground(Color.white);
		}
		return ruleTitleLabel;
	}
	
	
	private JLabel ruleTitleLabel = null;
	private Rule rule;
	private Font ruleNameFont;
	private Font filterDescriptionFont ;
	
	/**
	 * @return
	 */
	private JMultilineLabel getFilterLabel()
	{
		if (filterLabel==null)
			{
			filterDescriptionFont= new Font("Arial", Font.PLAIN, (int) (getSymbolHeight()/font_divisor));
			filterLabel=new JMultilineLabel();
			filterLabel.setMargin(new Insets(1,1,1,1));
			filterLabel.setFont(this.filterDescriptionFont);
			filterLabel.setBackground(Color.white);
			}
		return filterLabel;
	}
	private Vector symbolizers = new Vector();
	/**
	 * This method initializes symbolPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	protected SymbolizerPanel getSymbolPanel() {
		if (symbolPanel == null) {
			
			symbolPanel = new SymbolizerPanel();
			symbolPanel.setSymbolizers(symbolizers);
			
		}
		return symbolPanel;
	}
	
	protected JPanel getTextPanel() {
		if (textPanel == null) {
			textPanel = new JPanel();
			textPanel.setOpaque(true);

			BoxLayout bl=new BoxLayout(textPanel,BoxLayout.Y_AXIS);
			textPanel.setLayout(bl);
			
			textPanel.add(getRuleTitleLabel());	
			textPanel.add(getFilterLabel());
			textPanel.setBackground(Color.white);
			
			
		}
		return textPanel;
	}
	private JMultilineLabel filterLabel = null;
	/* (non-Javadoc)
	 * @see com.geopista.ui.legend.LegendEntryPanel#resizeTextPanel()
	 */
	protected void resizeTextPanel()
	{
		Font font=getRuleTitleLabel().getFont();
		ruleNameFont=font.deriveFont( (float)(getSymbolHeight()/font_divisor));
		getRuleTitleLabel().setFont(ruleNameFont);
		font=getFilterLabel().getFont();
		filterDescriptionFont=font.deriveFont( (float)(getSymbolHeight()/font_divisor));
		getFilterLabel().setFont(filterDescriptionFont);
	}
	
	
	public void setBackground(Color bg) {
		
		super.setBackground(bg);
		getFilterLabel().setBackground(bg);
		getRuleTitleLabel().setBackground(bg);				
	}	
	
}
