/*
 * PagesFrame.java
 *
 * Created on April 20, 2007, 1:37 AM
 */

package be.savat.components;

import it.businesslogic.ireport.util.Misc;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;

/**
 *
 * @author  gtoffoli
 */
public class PagesFrame extends javax.swing.JFrame {
    
    private JasperPrint jrPrint = null;
    private JRGraphics2DExporter exporter = null;
    private Point pageSize = new Point(350, 472);
    JBookPanel turnerNewsPaper = null;
    
    /** Creates new form PagesFrame */
    public PagesFrame(JasperPrint jrPrint) {
        initAll(jrPrint);
        
        Misc.centerFrame(this);
    }
    
    public void initAll(JasperPrint jrPrint) {

         initComponents();
        this.jrPrint = jrPrint;
        try {
            exporter = new JRGraphics2DExporter();
        } catch (Exception ex) {}
        
        setTitle("JBookPanel from Pieter-Jan Savat!!! Great job!");
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	setSize(800, 600);
        
        
        
        turnerNewsPaper = new JBookPanel() {
			
			protected Image loadPage(int index) {
                            
                            
                                BufferedImage img = new BufferedImage(getPageSize().x, getPageSize().y, BufferedImage.TYPE_INT_RGB);
                                Graphics gfx = img.getGraphics();
                                //gfx.setColor(Color.WHITE);
                                //gfx.fillRect(0, 0, img.getWidth(), img.getHeight());
                                exporter.setParameter(JRExporterParameter.JASPER_PRINT, getJrPrint());
                                exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(index));
                                exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, gfx);
                                exporter.setParameter(JRGraphics2DExporterParameter.ZOOM_RATIO, new Float((float)getPageSize().x / (float)getJrPrint().getPageWidth() ) );
                                
                                
                                try {
                                exporter.exportReport();
                                } catch (Exception ex) {}
                                return img;
			}
		};

                turnerNewsPaper.setMargins(30, 40);
                turnerNewsPaper.setBackground(new Color(157,185,235));
                turnerNewsPaper.setLeftPageIndex(-1);
                getContentPane().add(turnerNewsPaper, java.awt.BorderLayout.CENTER);
        
                updateViewerSize();
                
    }
    
        public JasperPrint getJrPrint() {
        return jrPrint;
    }
        
    void updateViewerSize()
    {
        System.out.println("Updating page size: " + getPageSize());
        System.out.flush();
        
        int imageWidth = jrPrint.getPageWidth();
        int imageHeight = jrPrint.getPageHeight();
        
        int width = getContentPane().getWidth()/2-40;
        int height = getContentPane().getHeight()-60;
        
        
        if (imageWidth <= width && imageHeight<= height)
        {
            getPageSize().x = width;
            getPageSize().y = height;
        }
        else if (width>0 && height>0)// Resize based on minor x/WIDTH... e y/HEIGHT
        {
                if ((double)((double)imageWidth/(double)width)> (double)((double)imageHeight/(double)height))
                {
                        getPageSize().x = width;
                        getPageSize().y = Math.min( (imageHeight*width)/imageWidth, height);

                }
                else
                {
                        getPageSize().x = Math.min( (imageWidth*height)/imageHeight, width);
                        getPageSize().y = height;

                }
        }
        
        int index = -1;
        index = turnerNewsPaper.getLeftPageIndex();
        //getContentPane().removeAll();
        
	turnerNewsPaper.setPages("", "", "", jrPrint.getPages().size()-1, getPageSize().x, getPageSize().y);
        
        turnerNewsPaper.setLeftPageIndex(index);
        
        System.out.println("New size: " + getPageSize());
        System.out.flush();
    }

    public void setJrPrint(JasperPrint jrPrint) {
        this.jrPrint = jrPrint;
    }

    public JRGraphics2DExporter getExporter() {
        return exporter;
    }

    public void setExporter(JRGraphics2DExporter exporter) {
        this.exporter = exporter;
    }

    public Point getPageSize() {
        return pageSize;
    }

    public void setPageSize(Point pageSize) {
        this.pageSize = pageSize;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized

        updateViewerSize();
    }//GEN-LAST:event_formComponentResized

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
