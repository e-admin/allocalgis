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
 * CrosstabReportElement.java
 * 
 * Created on 28 febbraio 2003, 22.53
 *
 */

package it.businesslogic.ireport;
import it.businesslogic.ireport.chart.Dataset;
import it.businesslogic.ireport.crosstab.CrosstabCell;
import it.businesslogic.ireport.crosstab.CrosstabColumnGroup;
import it.businesslogic.ireport.crosstab.CrosstabGroup;
import it.businesslogic.ireport.crosstab.CrosstabRowGroup;
import it.businesslogic.ireport.crosstab.Measure;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.event.CrosstabLayoutChangedEvent;
import it.businesslogic.ireport.util.*;
import java.awt.*;
import java.util.*;

public class CrosstabReportElement extends ReportElement {
    
	
        private Report report = null;
    
        private boolean repeatColumnHeaders = true;
        private boolean repeatRowHeaders = true;
        private int columnBreakOffset = 10;
        private Vector crosstabParameters=null;
        private String parametersMapExpression="";
        
        private String runDirection="LTR";

    public String getRunDirection() {
        return runDirection;
    }

    public void setRunDirection(String runDirection) {
        this.runDirection = runDirection;
    }
        
        
        // Crosstab properties...
        private boolean useDataset = false;
        private Dataset dataset = null;
        private boolean preSorted = false;
        private Vector measures=null;
        
        private Vector rowGroups= new Vector();
        private Vector columnGroups= new Vector();
        private Vector elements= new Vector();
        
        private Vector cells = new Vector();
        
        public static Image img=null;
	
	public CrosstabReportElement(int x, int y, int width, int height)
	{
		super(x, y, width, height);
	        setKey("crosstab");
                
                dataset = new Dataset();
                crosstabParameters = new Vector();
                measures = new Vector();
                
                if (img == null)
                      img = Misc.loadBufferedImageFromResources(new java.awt.Panel() ,"it/businesslogic/ireport/icons/crosstabTool1.png");
	}
        
        public void drawObject(Graphics2D g,double zoom_factor, int x_shift_origin, int y_shift_origin)
	{
           
                position.x -= 10;
		position.y -= 10;
		x_shift_origin -= 10;
		y_shift_origin -= 10;
		
		this.zoom_factor = zoom_factor;
                
                Color ccc = new Color(204,204,204,150);
                
                g.setColor( ccc );
                Paint paint=g.getPaint();
                g.setPaint( new GradientPaint( getZoomedDim(position.x)-x_shift_origin, 
                            getZoomedDim(position.y)-y_shift_origin,
                            ccc, getZoomedDim(position.x)-x_shift_origin+getZoomedDim(width),
                            getZoomedDim(position.x)-x_shift_origin + getZoomedDim(height), ccc.brighter() ) );

                g.fillRect( getZoomedDim(position.x)-x_shift_origin,
				getZoomedDim(position.y)-y_shift_origin,
				getZoomedDim(width),
				getZoomedDim(height));
                g.setPaint(paint);
                
		position.x += 10;
		position.y += 10;
		x_shift_origin += 10;
		y_shift_origin += 10;
                
                drawGraphicsElement(g,zoom_factor,  x_shift_origin,y_shift_origin);
	}
		
	public void  drawGraphicsElement(Graphics2D g, double zoom_factor, int x_shift_origin, int y_shift_origin)
	{
                drawGraphicsElement(g,"Thin", zoom_factor,  x_shift_origin,y_shift_origin);
                int correction = 0; //(zoom_factor <= 1) ? -1 : 0;
                
		position.x -= 10;
		position.y -= 10;
		x_shift_origin -= 10;
		y_shift_origin -= 10;
               
                if (img != null)
                {/*
                    g.drawImage(img, 
                        getZoomedDim(position.x)-x_shift_origin,
                        getZoomedDim(position.y)-y_shift_origin,
                        getZoomedDim(width)+correction,getZoomedDim(height)+correction, null);
                */
                    int imageWidth = img.getWidth(null);
                    int imageHeight = img.getHeight(null);
                    /*
			if (imgx instanceof Image)
			{
				((Image)imgx).setTransparent(true);
			}
                     */
			
			// 
			if (imageWidth < width && imageHeight< height)
			{
				Rectangle destination = new Rectangle(getZoomedDim(position.x)-x_shift_origin,
                                                                      getZoomedDim(position.y)-y_shift_origin,getZoomedDim( imageWidth), getZoomedDim( imageHeight-1));
				Rectangle source = new Rectangle(0,0,imageWidth,imageHeight);
				// Calculate y shift based on hAlign...
				int elem_height = getZoomedDim(this.height);
				elem_height -= getZoomedDim( imageHeight);
				// Calculate x shift based on hAlign...
				int elem_width = getZoomedDim(this.width);
				elem_width -= getZoomedDim( imageWidth);
				g.drawImage( img,destination.x+1, destination.y+1, destination.x+destination.width+1, destination.y+destination.height+1, 
                                                       source.x,source.y,source.width,source.height,
                                                       null,null);
			}
			else if (width>0 && height>0)// Resize based on minor x/WIDTH... e y/HEIGHT
			{
				if ((double)((double)imageWidth/(double)width)> (double)((double)imageHeight/(double)height))
				{
					
					Rectangle source = new Rectangle(0,0,imageWidth,imageHeight);
					Rectangle destination = new Rectangle(getZoomedDim(position.x)-x_shift_origin,
                                                                      getZoomedDim(position.y)-y_shift_origin,getZoomedDim(width) ,getZoomedDim(Math.min( (imageHeight*width)/imageWidth, height-1)) );						
					
					// Calculate y shift based on hAlign...
					int elem_height = getZoomedDim(this.height);
					elem_height -= getZoomedDim(Math.min( (imageHeight*width)/imageWidth, height));
						
					g.drawImage( img,destination.x+1, destination.y+1, destination.x+destination.width, destination.y+destination.height, 
                                                        source.x,source.y,source.width,source.height,
                                                        null,null);
					}
					else
					{
						Rectangle source = new Rectangle(0,0,imageWidth,imageHeight);
						Rectangle destination = new Rectangle(getZoomedDim(position.x)-x_shift_origin,
                                                                      getZoomedDim(position.y)-y_shift_origin, getZoomedDim( Math.min( (imageWidth*height)/imageHeight, width)) ,getZoomedDim( height-1) );
						
						// Calculate x shift based on hAlign...
						int elem_width = getZoomedDim(this.width);
						elem_width -= getZoomedDim( Math.min( (imageWidth*height)/imageHeight, width));
					
						g.drawImage( img,destination.x+1, destination.y+1, destination.x+destination.width, destination.y+destination.height, 
                                                        source.x,source.y,source.width,source.height,
                                                        null,null);
					}
                                }
                        }                    							
                position.x += 10;
		position.y += 10;        
        }                
        
    
        
        
        public ReportElement cloneMe()
    {
	CrosstabReportElement newReportElement = new CrosstabReportElement(position.x, position.y, width, height);
	copyBaseReportElement(newReportElement, this);
	return newReportElement;
    }
        
    public void copyBaseReportElement(ReportElement destination, ReportElement source)
        {
                super.copyBaseReportElement(destination, source);
                
                if (destination instanceof CrosstabReportElement &&
                    source instanceof CrosstabReportElement )
                {
                    
                    /*
                    ((SubReportElement)destination).setIsUsingCache( ((SubReportElement)source).isIsUsingCache());
                    ((SubReportElement)destination).setParametersMapExpression( new String(  ((SubReportElement)source).getParametersMapExpression() ));
                    ((SubReportElement)destination).setSubreportExpression ( new String(  ((SubReportElement)source).getSubreportExpression() ));
                    ((SubReportElement)destination).setSubreportExpressionClass( new String(  ((SubReportElement)source).getSubreportExpressionClass() ));
                    ((SubReportElement)destination).setUseConnection(  ((SubReportElement)source).isUseConnection() );
                    if ( ((SubReportElement)destination).isUseConnection())
                        ((SubReportElement)destination).setConnectionExpression(  new String( ((SubReportElement)source).getConnectionExpression() ));
                    else
                        ((SubReportElement)destination).setDataSourceExpression( new String(  ((SubReportElement)source).getDataSourceExpression() ));
                    
                    Enumeration e = ((SubReportElement)source).getSubreportParameters().elements();
                    while (e.hasMoreElements())
                    {
			JRSubreportParameter jp = (JRSubreportParameter)e.nextElement();
			((SubReportElement)destination).getSubreportParameters().addElement(jp.cloneMe());
                    }	
                     */
                }
        }

    public boolean isRepeatColumnHeaders() {
        return repeatColumnHeaders;
    }

    public void setRepeatColumnHeaders(boolean repeatColumnHeaders) {
        if (this.repeatColumnHeaders == repeatColumnHeaders) return;
        this.columnBreakOffset = columnBreakOffset;
        notifyChange();
    }

    public boolean isRepeatRowHeaders() {
        return repeatRowHeaders;
    }

    public int getColumnBreakOffset() {
        return columnBreakOffset;
    }

    public Vector getCrosstabParameters() {
        return crosstabParameters;
    }

    public String getParametersMapExpression() {
        return parametersMapExpression;
    }

    public void setParametersMapExpression(String parametersMapExpression) {

        if ((this.parametersMapExpression == null) ? parametersMapExpression == null : this.parametersMapExpression.equals(parametersMapExpression)) return;
        this.parametersMapExpression = parametersMapExpression;
        notifyChange();
    }

    public void setColumnBreakOffset(int columnBreakOffset) {
        if (this.columnBreakOffset == columnBreakOffset) return;
        this.columnBreakOffset = columnBreakOffset;
        notifyChange();
    }

    public void setRepeatRowHeaders(boolean repeatRowHeaders) {
        if (this.repeatRowHeaders == repeatRowHeaders) return;
        this.repeatRowHeaders = repeatRowHeaders;
        notifyChange();
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        if (this.dataset == dataset) return;
        this.dataset = dataset;
        notifyChange();
    }

    public boolean isUseDataset() {
        return useDataset;
    }

    public void setUseDataset(boolean useDataset) {
        if (this.useDataset == useDataset) return;
        this.useDataset = useDataset;
        notifyChange();
    }

    public boolean isPreSorted() {
        return preSorted;
    }

    public void setPreSorted(boolean preSorted) {
        if (this.preSorted == preSorted) return;
        this.preSorted = preSorted;
        notifyChange();

    }

    public Vector getMeasures() {
        return measures;
    }

    public void setMeasures(Vector measures) {
        this.measures = measures;
    }

    public Vector getRowGroups() {
        return rowGroups;
    }

    public void setRowGroups(Vector rowGroups) {
        this.rowGroups = rowGroups;
    }

    public Vector getColumnGroups() {
        return columnGroups;
    }

    public void setColumnGroups(Vector columnGroups) {
        this.columnGroups = columnGroups;
    }

    public Vector getElements() {
        return elements;
    }

    public void setElements(Vector elements) {
        this.elements = elements;
    }

    public Vector getCells() {
        return cells;
    }

    public void setCells(Vector cells) {
        this.cells = cells;
    }

    /**
     * Utility field used by event firing mechanism.
     */
    private javax.swing.event.EventListenerList listenerList =  null;

    /**
     * Registers CrosstabLayoutChangedListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addCrosstabLayoutChangedListener(it.businesslogic.ireport.gui.event.CrosstabLayoutChangedListener listener) {

        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add (it.businesslogic.ireport.gui.event.CrosstabLayoutChangedListener.class, listener);
    }

    /**
     * Removes CrosstabLayoutChangedListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeCrosstabLayoutChangedListener(it.businesslogic.ireport.gui.event.CrosstabLayoutChangedListener listener) {

        listenerList.remove (it.businesslogic.ireport.gui.event.CrosstabLayoutChangedListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event The event to be fired
     */
    public void fireCrosstabLayoutChangedListenerCrosstabLayoutChanged(it.businesslogic.ireport.gui.event.CrosstabLayoutChangedEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.CrosstabLayoutChangedListener.class) {
                ((it.businesslogic.ireport.gui.event.CrosstabLayoutChangedListener)listeners[i+1]).crosstabLayoutChanged (event);
            }
        }
    }
    
    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event The event to be fired
     */
    public void fireCrosstabMeasureChangedListenerCrosstabLayoutChanged(it.businesslogic.ireport.gui.event.CrosstabLayoutChangedEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.CrosstabLayoutChangedListener.class) {
                ((it.businesslogic.ireport.gui.event.CrosstabLayoutChangedListener)listeners[i+1]).crosstabMeasureChanged(event);
            }
        }
    }
    
    /**
     * Add safetly a groups from a CrosstabRowGroup and fires a CrosstabLayoutChangedEvent.
     */ 
    public void addGroup(CrosstabRowGroup group)
    {        
        CrosstabCell headerCell = new CrosstabCell();
        headerCell.setName(group.getName()+" header");
        headerCell.setType(CrosstabCell.HEADER_CELL);
        headerCell.setWidth( group.getWidth() );
        //if (!group.isHasHeader()) headerCell.setHeight(0);
        group.setHeaderCell(headerCell);
        
        CrosstabCell totalHeaderCell = new CrosstabCell();
        totalHeaderCell.setName(group.getName()+" total header");
        totalHeaderCell.setType(CrosstabCell.HEADER_CELL);
        totalHeaderCell.setWidth( group.getWidth() );
        if (group.getTotalPosition().equals("None")) 
        {
            totalHeaderCell.setHeight(0);
        }
        else
        {
            totalHeaderCell.setHeight(30);
        }
        group.setTotalCell(totalHeaderCell);
        
        //intln("set total height to "  +totalHeaderCell.getHeight() + " position total: '" + group.getTotalPosition()+"'");
        
        this.getCells().add( group.getHeaderCell());
        this.getCells().add( group.getTotalCell());
        
        // We have to add a default cell... Detail / Column...
        CrosstabCell cell = new CrosstabCell();
        cell.setType( cell.DETAIL_CELL);
        cell.setParent( this );
        cell.setName(null);
        cell.setRowTotalGroup(group.getName());
        if (group.getTotalPosition().equals("None")) cell.setHeight(0);
        else cell.setHeight(30);
        
        this.getCells().add(cell);
        
        this.getRowGroups().add(group);
        
        fireCrosstabLayoutChangedListenerCrosstabLayoutChanged(new CrosstabLayoutChangedEvent(this, this));
        notifyChange();
    }
    
    /**
     * Add safetly a groups from a CrosstabColumnGroup and fires a CrosstabLayoutChangedEvent.
     */ 
    public void addGroup(CrosstabColumnGroup group) 
    {
        CrosstabCell headerCell = new CrosstabCell();
        headerCell.setName(group.getName()+" header");
        headerCell.setType(CrosstabCell.HEADER_CELL);
        headerCell.setParent( this );
        headerCell.setHeight( group.getHeight());
        group.setHeaderCell(headerCell);
        
        CrosstabCell totalHeaderCell = new CrosstabCell();
        totalHeaderCell.setName(group.getName()+" total header");
        totalHeaderCell.setType(CrosstabCell.HEADER_CELL);
        totalHeaderCell.setParent( this );
        totalHeaderCell.setHeight( group.getHeight() );
        if (group.getTotalPosition().equals("None")) totalHeaderCell.setWidth(0);
        else totalHeaderCell.setWidth(30);
        group.setTotalCell(totalHeaderCell);
        
        this.getCells().add( group.getHeaderCell());
        this.getCells().add( group.getTotalCell());
        
        // We have to add a default cell... Detail / Column...
        CrosstabCell cell = new CrosstabCell();
        cell.setType( cell.DETAIL_CELL);
        cell.setParent( this );
        cell.setColumnTotalGroup(group.getName());
        if (group.getTotalPosition().equals("None")) cell.setWidth(0);
        else cell.setWidth(30);
        this.getCells().add(cell);
        
        this.getColumnGroups().add(group);
        
        fireCrosstabLayoutChangedListenerCrosstabLayoutChanged(new CrosstabLayoutChangedEvent(this, this));
        notifyChange();
    }
       
    /**
     * Remove safetly a set of groups from a Crosstab and fires a CrosstabLayoutChangedEvent.
     */ 
    public void removeGroups(Collection groups)
    {
        Iterator i_groups = groups.iterator();
        while (i_groups.hasNext())
        {
            CrosstabGroup group = (CrosstabGroup)i_groups.next();
            
            removeCell(group.getHeaderCell());
            removeCell( group.getTotalCell() );
            
            for (int i = 0; i < getCells().size(); ++i)
            {
                CrosstabCell cell = (CrosstabCell)getCells().elementAt(i);
                if (cell.getColumnTotalGroup().equals(group.getName()) || 
                    cell.getRowTotalGroup().equals( group.getName()))
                {
                    i -= removeCell(cell) ? 1 : 0;
                }
            }
            
            if (group instanceof CrosstabRowGroup) this.getRowGroups().remove(group);
            if (group instanceof CrosstabColumnGroup) this.getColumnGroups().remove(group);
        }
        fireCrosstabLayoutChangedListenerCrosstabLayoutChanged(new CrosstabLayoutChangedEvent(this, this));
        notifyChange();
    }
    
    /**
     * Remove safetly a group from a Crosstab and fires a CrosstabLayoutChangedEvent.
     */ 
    public void removeGroup(CrosstabGroup group)
    {
        if (group == null) return;
        ArrayList v = new ArrayList();
        v.add(group);
        removeGroups(v);
        notifyChange();
    }
    
    public void addMeasure(Measure measure)
    {
        getMeasures().addElement( measure );
        fireCrosstabMeasureChangedListenerCrosstabLayoutChanged(new CrosstabLayoutChangedEvent(this, this));
        notifyChange();
    }
    
    public void measureModified(Measure measure)
    {
        fireCrosstabMeasureChangedListenerCrosstabLayoutChanged(new CrosstabLayoutChangedEvent(this, this));
        notifyChange();
    }
    
    public void removeMeasure(Measure measure)
    {
        getMeasures().removeElement( measure );
        fireCrosstabMeasureChangedListenerCrosstabLayoutChanged(new CrosstabLayoutChangedEvent(this, this));
        notifyChange();
    }
    
    
    public void modifyGroup(String oldGroupName, CrosstabRowGroup group)
    {
        
        group.getHeaderCell().setWidth(group.getWidth());
        group.getTotalCell().setWidth(group.getWidth());
        
        if (!group.isHasHeader())
        {
            for (int i=0; i <getElements().size(); ++i)
            {
                ReportElement re = (ReportElement)getElements().elementAt(i);
                if (re.getCell() == group.getHeaderCell())
                {
                    i -= getElements().remove(re) ? 1 : 0;
                }
                group.getHeaderCell().setBox(new Box());
                group.getHeaderCell().setBackcolor(null);
            }
        }
        
        
        if (!group.isHasTotal())
        {
            group.getTotalCell().setHeight(0);
        }
        else if (group.getTotalCell().getHeight() == 0)
        {
            group.getTotalCell().setHeight(30);
        }
        
        // 1. Rename all cells...
        Iterator i = getCells().iterator();
        while (i.hasNext())
        {
            CrosstabCell cell = (CrosstabCell)i.next();
            if (cell.getRowTotalGroup().equals( oldGroupName))
            {
                cell.setRowTotalGroup(group.getName());
                if (!group.isHasTotal())
                {
                    cell.setHeight(0);
                    // Remove all cell elements..
                    for (int t=0; t <getElements().size(); ++t)
                    {
                        ReportElement re = (ReportElement)getElements().elementAt(t);
                        if (re.getCell() ==  cell)
                        {
                            t -= getElements().remove(re) ? 1 : 0;
                        }
                    }
                }
                else if (cell.getHeight() == 0)
                {
                    cell.setHeight(30);
                }
            }
        }
        
        fireCrosstabLayoutChangedListenerCrosstabLayoutChanged(new CrosstabLayoutChangedEvent(this, this));
        notifyChange();
    }
    
    public void modifyGroup(String oldGroupName, CrosstabColumnGroup group)
    {
        
        group.getHeaderCell().setHeight(group.getHeight());
        group.getTotalCell().setHeight(group.getHeight());
        
        if (!group.isHasHeader())
        {
            for (int i=0; i <getElements().size(); ++i)
            {
                ReportElement re = (ReportElement)getElements().elementAt(i);
                if (re.getCell() == group.getHeaderCell())
                {
                    i -= getElements().remove(re) ? 1 : 0;
                }
                group.getHeaderCell().setBox(new Box());
                group.getHeaderCell().setBackcolor(null);
            }
        }
        
        
        if (!group.isHasTotal())
        {
            group.getTotalCell().setWidth(0);
        }
        else if (group.getTotalCell().getWidth() == 0)
        {
            group.getTotalCell().setWidth(30);
        }
        
        // 1. Rename all cells...
        Iterator i = getCells().iterator();
        while (i.hasNext())
        {
            CrosstabCell cell = (CrosstabCell)i.next();
            if (cell.getColumnTotalGroup().equals( oldGroupName))
            {
                cell.setColumnTotalGroup(group.getName());
                if (!group.isHasTotal())
                {
                    cell.setWidth(0);
                    // Remove all cell elements..
                    for (int t=0; t <getElements().size(); ++t)
                    {
                        ReportElement re = (ReportElement)getElements().elementAt(t);
                        if (re.getCell() ==  cell)
                        {
                            t -= getElements().remove(re) ? 1 : 0;
                        }
                    }
                }
                else if (cell.getWidth() == 0)
                {
                    cell.setWidth(30);
                }
            }
        }
        
        fireCrosstabLayoutChangedListenerCrosstabLayoutChanged(new CrosstabLayoutChangedEvent(this, this));
        notifyChange();
    }
    
    
    /**
     * This method remove the cell and all contained elements
     */
    private boolean removeCell(CrosstabCell cell)
    {
        for (int i=0; i <getElements().size(); ++i)
        {
            ReportElement re = (ReportElement)getElements().elementAt(i);
            if (re.getCell() == cell)
            {
                i -= getElements().remove(re) ? 1 : 0;
            }
        }
        notifyChange();
        return getCells().remove(cell);
    }
    
    public Point trasform(Point delta, int type)
    {
        Point point = super.trasform(delta, type);
        
        try {
            
            if (MainFrame.getMainInstance().getActiveReportFrame() != null)
            {
                MainFrame.getMainInstance().getActiveReportFrame().getCrosstabEditor(this).getPanelEditor().updateGrid();
            }
         } catch (Exception ex)
         {
         }
        
        return point;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
    
    public void notifyChange()
    {
        if (getReport() != null)
        {
            getReport().incrementReportChanges();
        }
    }
    
    
}
