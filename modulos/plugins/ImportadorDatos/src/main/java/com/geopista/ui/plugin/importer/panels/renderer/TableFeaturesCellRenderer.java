/**
 * TableFeaturesCellRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.ui.plugin.importer.panels.renderer;


/**
 * Define el aspecto de la lista de atributos de una feature
 * 
 * @author cotesa
 *
 */

import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.geopista.feature.ValidationError;
import com.geopista.ui.plugin.importer.beans.ConstantesImporter;
import com.geopista.ui.plugin.importer.beans.TableFeaturesValue;
import com.vividsolutions.jump.I18N;

public class TableFeaturesCellRenderer extends JComponent implements TableCellRenderer
{   
	
	private boolean isMarked = false;
    /**
     * Constructor por defecto
     */
    public TableFeaturesCellRenderer()
    {
        super();
        setOpaque(true);           
    }
    
    /**
     * Obtiene el componente a pintar
     * 
     * @param table La tabla en la que está el componente
     * @param value Atributo a pintar
     * @param sel Verdadero si el componente está seleccionado
     * @param hasFocus Verdadero si el componente tiene el foco
     * @param rowIndex Índice de la fila
     * @param vColIndex Índice de la columna
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean sel, boolean hasFocus, int rowIndex, int vColIndex) {
       
    	isMarked = false;
        // Configure the component with the specified value
        if (value instanceof TableFeaturesValue)
        {    
            
        	TableFeaturesValue tfv = (TableFeaturesValue)value;  
            JComponent comp;
            
            
            if (tfv.getType() == TableFeaturesValue.DATE_TYPE)
            {
            	String fecha = "";
            	if (tfv.getObj()!=null && !tfv.getObj().toString().equals(""))
            	{
            		Format formatter = new SimpleDateFormat(ConstantesImporter.DATEFORMAT);
                    if (tfv.getObj() instanceof Date)
                    {
                    	fecha = formatter.format(tfv.getObj());
                    }
                    else
                    {
                    	DateFormat dateformat = new SimpleDateFormat(ConstantesImporter.DATEFORMAT);
                    	try {
							Date date = (Date)dateformat.parse(tfv.getObj().toString());
							fecha = formatter.format(date);
						} catch (ParseException e) {
							//e.printStackTrace();	
							fecha =tfv.getObj().toString();
							tfv.setVe(new ValidationError(tfv.getName(), 
									I18N.get("ImporterPlugIn","ImporterPlugIn.TableFeaturesCellRenderer.formatonovalido"), null));						}
                    }	
            	}  
            	
            	comp = new JLabel(fecha);
            }
            else if (tfv.getObj()!=null)
            {
            	comp = new JLabel(tfv.getObj().toString());
            }
            else
                comp = new JLabel("");
            
            if (tfv.getVe()!=null)
            {
            	isMarked = true;
            	comp.setForeground(Color.RED);
            	comp.setToolTipText(tfv.getVe().message);
            	//comp.setBorder(new LineBorder(Color.BLUE));
            	if (((JLabel)comp).getText().equals("") && tfv.getVe().message.equals(ConstantesImporter.errorAtributoNoNulo))
            	{
            		((JLabel)comp).setText(tfv.getVe().message);
            	}
            }
            
            if (       ((JLabel)comp).getText().startsWith(ConstantesImporter.erroresFeatureArray[0]) 
        			|| ((JLabel)comp).getText().startsWith(ConstantesImporter.erroresFeatureArray[1]) 
        			|| ((JLabel)comp).getText().startsWith(ConstantesImporter.erroresFeatureArray[2]) )
        	{
            	comp.setForeground(Color.RED);
            	isMarked = true;
        	}
            
            comp.setBackground(Color.WHITE);
            comp.setOpaque(true);
            return comp;
        }
        else if (value instanceof String)
        {
        	return new JLabel((String)value);
        }
        
        // Since the renderer is a component, return itself
        return this;
    }
    
    public boolean getMarked (){
    	return isMarked;
    }   		
    
}
