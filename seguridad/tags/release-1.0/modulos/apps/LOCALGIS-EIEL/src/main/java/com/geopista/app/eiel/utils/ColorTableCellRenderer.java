package com.geopista.app.eiel.utils;

import java.awt.Color;
import java.awt.Component;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
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
