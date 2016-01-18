/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * ExpObjectCellRenderer.java
 * 
 * Created on September 21, 2006, 12:35 AM
 *
 */

package it.businesslogic.ireport.gui.expbuilder;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ExpObjectCellRenderer extends JTextPane implements ListCellRenderer {
        private Color selectionBackground;
        private Color background;
        
        // Create a style object and then set the style attributes
        Style typeStyle = null;
        Style classTypeStyle = null;
        
        Style parameterStyle = null;
        Style variableStyle = null;
        Style fieldStyle = null;
        Style whiteStyle = null;

        public ExpObjectCellRenderer(JList list) {
            super();
            selectionBackground = list.getSelectionBackground();
            background = list.getBackground();
            StyledDocument doc = new DefaultStyledDocument();
            this.setDocument( doc );
            
            typeStyle = doc.addStyle("typeStyle", null);
            StyleConstants.setItalic(typeStyle, true);
            StyleConstants.setForeground(typeStyle, Color.gray);
            
            classTypeStyle = doc.addStyle("classTypeStyle", null);
            StyleConstants.setForeground(classTypeStyle, Color.gray);
            
            parameterStyle = doc.addStyle("parameterStyle", null);
            StyleConstants.setForeground(parameterStyle, Color.red.darker());
            
            variableStyle = doc.addStyle("variableStyle", null);
            StyleConstants.setForeground(variableStyle, Color.blue);
            
            fieldStyle = doc.addStyle("fieldStyle", null);
            StyleConstants.setForeground(fieldStyle, Color.green.darker().darker());
            
            whiteStyle = doc.addStyle("whiteStyle", null);
            StyleConstants.setForeground(whiteStyle, Color.white);
            
        }
        public Component getListCellRendererComponent(JList list, Object object,
                int index, boolean isSelected, boolean cellHasFocus) {
            
            this.setText("");
            StyledDocument doc = (StyledDocument)this.getDocument();
            if (object instanceof ExpObject)
            {
                ExpObject eo = (ExpObject)object;
                 
                 try {
                     
                     doc.insertString(doc.getLength(), eo.getName() + "   ", (isSelected) ? whiteStyle : null);
                     
                     Style s = parameterStyle;
                     String type = "Parameter";
                     if (eo.getType() == eo.TYPE_FIELD) 
                     {
                         s = fieldStyle;
                         type = "Field";
                     }
                     else if (eo.getType() == eo.TYPE_VARIABLE)
                     {
                         s = variableStyle;
                         type = "Variable";
                     }
                     
                     if (isSelected) s = whiteStyle;
                     
                     
                     doc.insertString(doc.getLength(), type + " ", s);
                     
                     String tp = eo.getClassType() + "";
                     if (tp.lastIndexOf(".") > 0) tp = tp.substring(tp.lastIndexOf(".")+1);
                     
                     doc.insertString(doc.getLength(), tp, classTypeStyle);
                } catch (Exception ex){}
            }
            else
            {
                 try {
                doc.insertString(doc.getLength(), "" + object, null);
                 } catch (Exception ex){}
            }
            setBackground(isSelected ? selectionBackground : background);
            return this;
        }
    }

