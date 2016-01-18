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
 * JListView.java
 * 
 * Created on 8 ottobre 2004, 2.55
 *
 */

package it.businesslogic.ireport.gui.listview;
import java.awt.BorderLayout;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JListView extends JPanel
   implements ChangeListener
{
   protected JScrollPane scroll;
   private ScrollableList list;
   
   int listLayout = JList.HORIZONTAL_WRAP;
   
   public JListView()
   {
      setLayout(new BorderLayout());
      
      add(BorderLayout.CENTER, scroll = 
         new JScrollPane(
         list = new ScrollableList() ));
      scroll.getViewport().setBackground(
         getList().getBackground());
      stateChanged(new ChangeEvent(this));
   }
   
   public void stateChanged(ChangeEvent event)
   {
      getList().setLayoutOrientation(listLayout);
      if (listLayout == JList.VERTICAL)
      {
         scroll.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
         scroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      }
      if (listLayout == JList.VERTICAL_WRAP)
      {
         scroll.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_NEVER);
         scroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      }
      if (listLayout == JList.HORIZONTAL_WRAP)
      {
         scroll.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
         scroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      }
      scroll.revalidate();
   }

    public ScrollableList getList() {
        return list;
    }

    public void setList(ScrollableList list) {
        this.list = list;
    }
   

}
