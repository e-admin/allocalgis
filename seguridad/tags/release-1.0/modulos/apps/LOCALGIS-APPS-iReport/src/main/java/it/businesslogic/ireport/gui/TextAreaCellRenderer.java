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
 * TextAreaCellRenderer.java
 * 
 * Created on September 21, 2006, 12:35 AM
 *
 */

package it.businesslogic.ireport.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.DefaultStyledDocument;

public class TextAreaCellRenderer extends JTextPane implements ListCellRenderer {
        private Color selectionBackground;
        private Color background;
        
        // Create a style object and then set the style attributes
        Style methodStyle = null;
        Style returnTypeStyle = null;

        public TextAreaCellRenderer(JList list) {
            super();
            selectionBackground = list.getSelectionBackground();
            background = list.getBackground();
            StyledDocument doc = new DefaultStyledDocument();
            this.setDocument( doc );
            methodStyle = doc.addStyle("methodStyle", null);
            StyleConstants.setBold(methodStyle, true);
            returnTypeStyle = doc.addStyle("returnType", null);
            StyleConstants.setForeground(returnTypeStyle, Color.gray);
        }
        public Component getListCellRendererComponent(JList list, Object object,
                int index, boolean isSelected, boolean cellHasFocus) {
            
            String s = (String)object;
            this.setText("");
            StyledDocument doc = (StyledDocument)this.getDocument();
            try {
            doc.insertString(doc.getLength(), s.substring(0, s.indexOf("(")), methodStyle);
            doc.insertString(doc.getLength(), s.substring(s.indexOf("("), s.lastIndexOf(")")+1), null);
            doc.insertString(doc.getLength(), s.substring(s.lastIndexOf(")")+1), returnTypeStyle);
            } catch (Exception ex){}
            setBackground(isSelected ? selectionBackground : background);
            return this;
        }
    }

