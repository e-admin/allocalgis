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
 * ScrollableList.java
 * 
 * Created on 8 ottobre 2004, 2.55
 *
 */

package it.businesslogic.ireport.gui.listview;
import javax.swing.JList;

public class ScrollableList extends JList
{
   protected boolean trackWidth = true;
   protected boolean trackHeight = false;

   public ScrollableList()
   {
      super();
      setCellRenderer(
         new it.businesslogic.ireport.chart.gui.ChartCellRenderer());
   }
   
   public int getVisibleRowCount()
   {
      return 0;
   }
   
   public boolean 
      getScrollableTracksViewportWidth()
   {
      return trackWidth;
   }

   public void setScrollableTracksViewportWidth(
      boolean trackWidth)
   {
      this.trackWidth = trackWidth;
   }

   public boolean 
      getScrollableTracksViewportHeight()
   {
      return trackHeight;
   }

   public void setScrollableTracksViewportHeight(
      boolean trackHeight)
   {
      this.trackHeight = trackHeight;
   }
   
   public void setLayoutOrientation(
      int orientation)
   {
      super.setLayoutOrientation(orientation);
      if (orientation == VERTICAL)
      {
         setScrollableTracksViewportWidth(true);
         setScrollableTracksViewportHeight(false);
      }
      if (orientation == VERTICAL_WRAP)
      {
         setScrollableTracksViewportWidth(false);
         setScrollableTracksViewportHeight(true);
      }
      if (orientation == HORIZONTAL_WRAP)
      {
         setScrollableTracksViewportWidth(true);
         setScrollableTracksViewportHeight(false);
      }
      revalidate();
   }
}
