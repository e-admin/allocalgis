/**
 * ColorTableCellRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.utils;

import java.awt.Color;
import java.awt.Component;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

public class ColorTableCellRenderer  extends JTextField  implements TableCellRenderer {
	
        Color curColor;
        Vector redRows;
        ArrayList<Color> colorRows;

        public ColorTableCellRenderer(){
            super();
        }

        public ColorTableCellRenderer(Vector v){
            super();
            redRows= v;
        }
        public ColorTableCellRenderer(ArrayList<Color> colorRows){
            super();
            this.colorRows= colorRows;
        }


        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {

            if (isSelected) {
               
                setForeground(Color.white);
                setBackground(Color.blue);

            } else {
                if (redRows != null){

                    if (redRows.size()>rowIndex && redRows.get(rowIndex) != null){
                        curColor= Color.red;
                    }else
                    	curColor= Color.black;
                    setBackground(table.getBackground());
                }
                else if (colorRows!=null){
                	 if (colorRows.size()>rowIndex && colorRows.get(rowIndex) != null){
                         curColor= colorRows.get(rowIndex);
                         if (curColor==Color.blue){
                        	 setOpaque(true);
                        	 setBackground(Color.LIGHT_GRAY); 
                         }
                         else if (curColor==Color.green){
                        	 setOpaque(true);
                        	 setBackground(Color.LIGHT_GRAY); 
                         }
                         else{
                        	 setBackground(table.getBackground());
                        	// System.out.println("Set default background");
                         }
                	 }
                	 else{
                		setBackground(table.getBackground());
                     	curColor= Color.black;
                	 }
                }
                else{ 
                	setBackground(table.getBackground());
                	curColor= Color.black;
                }
                
                
                setForeground(curColor);
               
            }

            String printValue = "";
            if (value instanceof Integer){
            	printValue = ((Integer)value).toString();
            }
            else if (value instanceof Date){
            	printValue = ((Date)value).toString();
            }
            else if (value instanceof Double){
            	printValue = ((Double)value).toString();
            }
            else if (value instanceof Float){
            	printValue = ((Float)value).toString();
            }
            else if (value instanceof Long){
            	printValue = ((Long)value).toString();
            }
            else if (value instanceof String){
            	printValue = ((String)value);
            }
            setText(printValue);
            setBorder(new javax.swing.border.EmptyBorder(getBorder().getBorderInsets(this)));
            

            return this;
        }

}
