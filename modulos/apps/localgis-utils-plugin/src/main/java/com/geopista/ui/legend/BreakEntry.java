/**
 * BreakEntry.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.legend;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * TODO Documentación
 * @author juacas
 *
 */
public class BreakEntry extends LegendEntryPanel
{

	private JLabel jLabel = null;
	/**
	 * This method initializes 
	 * 
	 */
	public BreakEntry() {
		super();
		initialize();
	} 
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */

	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */    
	private JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new JLabel();
			jLabel.setText("---- Break ----");
			jLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
			jLabel.setOpaque(true);			
		}
		return jLabel;
	}
	/* (non-Javadoc)
	 * @see com.geopista.ui.legend.LegendEntryPanel#getSymbolPanel()
	 */
	protected SymbolizerPanel getSymbolPanel()
	{
		SymbolizerPanel sp= new SymbolizerPanel();
		sp.setVisible(false);
		return sp;
	}
	/* (non-Javadoc)
	 * @see com.geopista.ui.legend.LegendEntryPanel#getTextPanel()
	 */
	protected JPanel getTextPanel()
	{
		JPanel pan= new JPanel();
		pan.setLayout(new FlowLayout(FlowLayout.LEFT));
		pan.add(getJLabel());
		pan.setOpaque(true);
		pan.setBackground(Color.white);
		return pan;
	}
	public void setEditingMode(boolean editingMode)
	{	
		super.setEditingMode(editingMode);
		getCheckBox().setVisible(false);
		setVisible(editingMode);

	}
	protected void initialize()
	{
		super.initialize();
		getCheckBox().setVisible(false);
		getSymbolPanel().setVisible(false);
		getJLabel().setBackground(Color.white);
	}
	/* (non-Javadoc)
	 * @see com.geopista.ui.legend.LegendEntryPanel#resizeTextPanel()
	 */
	protected void resizeTextPanel()
	{
		Font font=getJLabel().getFont();
		getJLabel().setFont(font.deriveFont( (float)(getSymbolHeight()/2)));
	}
}
