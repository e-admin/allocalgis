/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 * 
 * Created on 18-jun-2004 by juacas
 *
 * 
 */
package com.geopista.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.geopista.ui.GeopistaLayerNamePanel;
import com.geopista.ui.plugin.LegendRenderer;
import com.geopista.ui.plugin.scalebar.GeopistaScaleBarRenderer;
import com.geopista.ui.renderer.UncachedLayerRenderer;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelContext;
import com.vividsolutions.jump.workbench.ui.renderer.Renderer;

/**
* Documento que representa un mapa a imprimir.
* 
* Las dimensiones del pintado utiliza las unidades del contexto gráfico	
*  Cuando es una impresora las unidades son puntos 1/72 de pulgada
* Cuando es un componente serán pixeles.
* Observar que la función de scale del Graphics permite independizar el pintado del dispositivo.
* @see PrintPreviewPanel
* @author juacas
*/
public class MapDocument implements Printable
{
	
	private GeopistaLayerNamePanel layerNamePanel = null;
	private Image layerPanelImage = null;
	private boolean leyenda;
  
	private LayerViewPanel layerViewPanel = null;
	private Collection layersToPrint = null;
	private ArrayList layersReversed=null;
  private int alturaArbol = 0;
  private PageFormat pf=null;
  private double imageableHeight = 0;
  private double imageableWidth = 0;

  private double margen = 0;
  private double printAreaWidth = 0;
  private double printAreaHeight = 0;
  private Envelope envelope = null;
	 /*
	  * TODO: El constructor necesita un parámetro de escala y para configurar escalómetros, etc.
	  */
	public MapDocument(GeopistaLayerNamePanel layerNamePanel, LayerViewPanel layerViewPanel, PageFormat pageFormat, int newScale)
	  {
	        
	    this.layerNamePanel = layerNamePanel;
	    this.layerViewPanel = layerViewPanel;

	    this.layersToPrint = layerViewPanel.getLayerManager().getVisibleLayers(false);
	    layersReversed = new ArrayList(layersToPrint);
		Collections.reverse(layersReversed);

     this.imageableHeight = (int) pageFormat.getImageableHeight();
    this.imageableWidth =  (int) pageFormat.getImageableWidth();

	

		
//		printAreaWidth = pageFormat.getWidth(); //Obtiene el tamaño en puntos (72 puntos por pulgada) de la página
//		printAreaHeight = pageFormat.getHeight();
		printAreaWidth = this.imageableWidth;
		printAreaHeight= this.imageableHeight;
		
		double margen_cm=0;// Margen que deseamos dejar libre en la pantalla en centímetros
		margen = margen_cm/2.54 * 72;

     
    //calculamos el ancho del envelope a partir de la escala
    if(newScale!=-1)
    {
      double px_cm =  ((printAreaWidth - 2*margen)/72 * 2.54);
      double py_cm =  ((printAreaHeight - 2*margen)/72 * 2.54);

      double envelopeWidth = newScale*px_cm/100; // en metros
      double envelopeHeight = newScale*py_cm/100; // en metros
      Envelope originalEnvelope = layerViewPanel.getViewport().getEnvelopeInModelCoordinates();
      double envelopeCenterX = originalEnvelope.getMinX()+(originalEnvelope.getMaxX()-originalEnvelope.getMinX())/2;
      double envelopeCenterY = originalEnvelope.getMinY()+(originalEnvelope.getMaxY()-originalEnvelope.getMinY())/2;
      
      // 
      envelope = new Envelope(envelopeCenterX-(envelopeWidth/2),envelopeCenterX+(envelopeWidth/2),
                      				envelopeCenterY+(envelopeHeight/2),envelopeCenterY-(envelopeHeight/2)); 
    }
    else
    {
      envelope=GeopistaFunctionUtils.getPreferredPrintEnvelope((int) (printAreaWidth-2*margen),(int) (printAreaHeight-2*margen),layerViewPanel);
    }
	   
	    
	  }


    public MapDocument(GeopistaLayerNamePanel layerNamePanel, LayerViewPanel layerViewPanel, PageFormat pageFormat, int newScale, Envelope sourceEnvelope)
	  {
	        
	    this.layerNamePanel = layerNamePanel;
	    this.layerViewPanel = layerViewPanel;

	    this.layersToPrint = layerViewPanel.getLayerManager().getVisibleLayers(false);
	    layersReversed = new ArrayList(layersToPrint);
		Collections.reverse(layersReversed);

     this.imageableHeight =  pageFormat.getImageableHeight();
    this.imageableWidth =   pageFormat.getImageableWidth();

	

		
//		printAreaWidth = pageFormat.getWidth(); //Obtiene el tamaño en puntos (72 puntos por pulgada) de la página
//		printAreaHeight = pageFormat.getHeight();
		printAreaWidth=this.imageableWidth;
		printAreaHeight=this.imageableHeight;
		
		double margen_cm=0;// Margen que deseamos dejar libre en la pantalla en centímetros
		margen = margen_cm/2.54 * 72;


    //calculamos el ancho del envelope a partir de la escala
    if(newScale!=-1)
    {
      double px_m =  ((printAreaWidth - 2*margen)/72 * 2.54/100);
      double py_m =  ((printAreaHeight - 2*margen)/72 * 2.54/100);

      double envelopeWidth = newScale*px_m;
      double envelopeHeight = newScale*py_m; 
      Envelope originalEnvelope = new Envelope(sourceEnvelope);
      double envelopeCenterX = (originalEnvelope.getMinX()+originalEnvelope.getMaxX())/2;
      double envelopeCenterY = (originalEnvelope.getMinY()+originalEnvelope.getMaxY())/2;
      envelope = new Envelope(envelopeCenterX-(envelopeWidth/2),envelopeCenterX+(envelopeWidth/2),
                      				envelopeCenterY+(envelopeHeight/2),envelopeCenterY-(envelopeHeight/2)); 
    }
    else
    {
      envelope = new Envelope(sourceEnvelope);
    }
	   
	    
	  }
	/**
	 * Las dimensiones del pintado utiliza las unidades del contexto gráfico
	 * Cuando es una impresora las unidades son puntos 1/72 de pulgada
	 * Cuando es un componente serán pixeles.
	 * Observar que la función de scale del Graphics permite independizar el pintado del dispositivo.
	 * Si se desea utilizar este método para dibujar en un componente hay que aplicar scale al Graphics 
	 * antes de llamar a la función print.
	 * 
	 * @see PrintPreviewPanel
	 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
	 */
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException
	{

      this.pf = pageFormat;
	    graphics.translate((int)margen+(int)pageFormat.getImageableX(),(int)margen+(int)pageFormat.getImageableY());
	    graphics.drawRect(0,
	    				0,
						(int)(pageFormat.getImageableWidth()-margen),
						(int)(pageFormat.getImageableHeight()-margen));
	   graphics.clipRect(0,
			0,
			(int)(pageFormat.getImageableWidth()-margen),
			(int)(pageFormat.getImageableHeight()-margen));
	   
	    try
	    {
	     print((Graphics2D) graphics,layerViewPanel.getLayerManager().getVisibleLayers(false),envelope,(int) (printAreaWidth-2*margen),(int) (printAreaHeight-2*margen));
	    }catch(Exception e) //capturo la excepcion porque no puedo lanzarla ya que el metodo paint sobreescrito no lo admite
	    {
	      e.printStackTrace();
	    }
	    double labelx= 1*72/2.54;
	    double labely= 1*72/2.54;
	    double labelwidth= 5*72/2.54;
	    double labelheight= 2*72/2.54;
	    graphics.setColor(Color.WHITE);
	    graphics.fill3DRect((int)labelx,(int)labely,(int)labelwidth,(int)labelheight,true);
	    graphics.setColor(Color.BLACK);
	    graphics.draw3DRect((int)labelx,(int)labely,(int)labelwidth,(int)labelheight,true);
	    graphics.drawString("Mapa GEOPISTA",(int)labelx+20,(int)labely+20);

       
        
	    /*if(layerNamePanel!=null)
	    {

         
        int numeroFilasVisibles = layerNamePanel.getTree().getRowCount() ;
         //tamano de cada columna
        int tamanoColumna = 17;

        alturaArbol = numeroFilasVisibles * tamanoColumna;

	    	//Crea un double buffer
	    	
	    		BufferedImage image = new BufferedImage((int)layerNamePanel.getComponent(0).getBounds().getWidth(),alturaArbol, BufferedImage.TYPE_INT_RGB);
	    	    Graphics2D offGraphics = image.createGraphics();
	    	    layerNamePanel.print(offGraphics);
	    	    layerPanelImage=image;
	    	 
	    	    // TODO: Revisar esta solución. Podría ser mejor un reset explícito en un método.
	    	    //limpia la referencia al componente y se queda con la imagen para 
	    	    // atender a los pintados múltiples del print. 
	    	    // Si se vuelve a asignar se sacará una nueva copia.
	    	    
	    
//      graphics.translate( 300,300);
//      graphics.clipRect(0,0,(int)layerNamePanel.getComponent(0).getBounds().getWidth(),(int)layerNamePanel.getComponent(0).getBounds().getHeight());
   
      
	    	
	    }*/

      //if (layerPanelImage!=null)
	    //		graphics.drawImage(layerPanelImage, (int)(pageFormat.getImageableWidth()-margen- layerPanelImage.getWidth(null))-10,(int) (pageFormat.getImageableHeight()-margen-alturaArbol)-10,null);
	  
	// Flush memory
	System.gc();
	return Printable.PAGE_EXISTS;
	}

  
	
	private LayerViewPanel virtualPanel=null;
	
	private void print(Graphics2D graphics, Collection layers, Envelope envelope, int extentInPixelsW,int extentInPixelsH)
	  throws Exception {
	  Assert.isTrue(!layers.isEmpty());

	  final Throwable[] throwable = new Throwable[] { null };
	 if (virtualPanel==null) // intenta ahorra algo de memoria en el caso de la impresion en la que se llaman muchas veces a print
	 {
	 	virtualPanel = new LayerViewPanel(((Layer) layers.iterator()
	                                                           .next()).getLayerManager(),
	          new LayerViewPanelContext() {
	              public void setStatusMessage(String message) {
	              }

	              public void warnUser(String warning) {
	              }

	              public void handleThrowable(Throwable t) {
	                  throwable[0] = t;
	              }
	          });
	 

	 }
   	virtualPanel.setSize(extentInPixelsW, extentInPixelsH);
	 	virtualPanel.getViewport().zoom(envelope);
	  //paintBackground(graphics, extentInPixelsW,extentInPixelsH);

	  for (Iterator i = layersReversed.iterator(); i.hasNext();) {
	      Layer layer = (Layer) i.next();

	      /*if(true)
        {
          Collection selections = layerViewPanel.getSelectionManager().getFeaturesWithSelectedItems(layer);

           virtualPanel.getSelectionManager().getFeatureSelection().selectItems(layer,selections);
           virtualPanel.getSelectionManager().updatePanel();

        }   */
        virtualPanel.getRenderingManager().putAboveLayerables(
                GeopistaScaleBarRenderer.CONTENT_ID,
                new Renderer.Factory() {
                public Renderer create() {
                    return new GeopistaScaleBarRenderer(virtualPanel);
                }
            });

            virtualPanel.getRenderingManager().putAboveLayerables(
                LegendRenderer.CONTENT_ID,
                new Renderer.Factory() {
                public Renderer create() {
                    return new LegendRenderer(virtualPanel,(int)imageableHeight,(int)imageableWidth);
                }
            });

            
        virtualPanel.getRenderingManager().render(GeopistaScaleBarRenderer.CONTENT_ID);   
        virtualPanel.getRenderingManager().render(LegendRenderer.CONTENT_ID);           
	      UncachedLayerRenderer renderer = new UncachedLayerRenderer(layer, virtualPanel);

	      //Wait for rendering to complete rather than running it in a separate thread. [Jon Aquino]
	      Runnable runnable = renderer.createRunnable();
	      if (runnable != null) { runnable.run(); } // realmente no lanza otro hilo, siempre es null
          //I hope no ImageObserver is needed. Set to null. [Jon Aquino]
    
	      renderer.copyTo(graphics);

            GeopistaScaleBarRenderer.setEnabled(true,virtualPanel);
            ((GeopistaScaleBarRenderer) virtualPanel.getRenderingManager().getRenderer(GeopistaScaleBarRenderer.CONTENT_ID)).paint(graphics,virtualPanel.getViewport().getScale());

            boolean showLegend = false;
            if(layerNamePanel!=null) showLegend=true;
            LegendRenderer.setEnabled(showLegend,virtualPanel);
            ((LegendRenderer) virtualPanel.getRenderingManager().getRenderer(LegendRenderer.CONTENT_ID)).paint(graphics,layerNamePanel);
          

        
        
        
	  }
    GeopistaFunctionUtils.pointsMeter(virtualPanel,this.pf);

	  if (throwable[0] != null) {
	      throw throwable[0] instanceof Exception ? (Exception) throwable[0]
	                                              : new Exception(throwable[0].getMessage());
	  }

	}

	private  void paintBackground(Graphics2D graphics, int extentW,int extentH) {
	  graphics.setColor(Color.white);
	  graphics.fillRect(0, 0, extentW, extentH);
	}

	public static void main(String[] args)
	{
	}
}
