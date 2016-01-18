package com.geopista.app.catastro.intercambio.edicion.dialogs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
//import com.geopista.protocol.document.DocumentBean;

class DibujarImagenPanel extends JPanel
{
    /** The image to paint in the background */
    private Image bgImage;

    /* constructor de la clase (vacio)*/
    public DibujarImagenPanel()
    {
    	        
    }
    
    public DibujarImagenPanel(PintarImagenCatastro pintarImagenPanel)
    {
    	this.setPreferredSize(pintarImagenPanel.getSize());
    	this.setMaximumSize(this.getPreferredSize());
    	this.setMinimumSize(this.getPreferredSize());
    	
    }

    /* constructor de la clase (con imagen) */
    public DibujarImagenPanel(Image bgImage)
    {
        this.bgImage = bgImage;
    }

    /* constructor de la clas (con URL)*/
    public DibujarImagenPanel(String sUrl)
    {
        bgImage = getImage(getClass().getResource(sUrl));
    }

    /* determinamos la imagen a pintar */
    public void setImage(Image bgImage)
    {
        this.bgImage=bgImage;
        repaint();
    }

    /* determinamos la imagen a pintar */
    public void setImage()
    {
        repaint();
    }

    /* obtenemos la imagen del documento */
//    public void setDocument(DocumentBean doc) throws Exception
//    {
//        Image image = ImageIO.read(new ByteArrayInputStream(doc.getThumbnail()));
//        setImage(image);
//    }

    /* obtenemos la imagen de la URL */
    public void setImage(String sUrl)
    {
        bgImage = getImage(getClass().getResource(sUrl));
        repaint();
    }

    /* pintamos la imagen */
    public void paint(Graphics g)
    {
        // if a background image exists, paint it
        if (bgImage != null)
        {
            g.setColor(Color.WHITE);
            g.fillRect(0,0,getWidth(),getHeight());
            int width = getWidth();
            int height = getHeight();
            int imageW = bgImage.getWidth(null);
            int imageH = bgImage.getHeight(null);
            if (imageW>width || imageH > height)
            {
               if (width/imageW > height/imageH)
               {
                   imageW=(imageW*height)/imageH;
                   imageH=height;
               }
               else
               {
                   imageH=(imageH*width)/imageW;
                   imageW=width;
               }
            }
            int x=0;
            int y=0;
            try{x=(width-imageW) / 2;}catch(Exception e){};
            try{y=(height-imageH) /2;}catch(Exception e){};
            g.drawImage(bgImage, x, y,imageW,imageH,this);
                        
        }
        else
        {
            g.setColor(Color.LIGHT_GRAY);
            g.drawRect(0,0,getWidth(),getHeight());
            g.fillRect(0,0,getWidth(),getHeight());
        }

    }

    /* evento xa pintar la imagen */
    public void actionPerformed(ActionEvent ae)
    {
        bgImage = getImage(getClass().getResource(ae.getActionCommand()));
        repaint();
    }

    /* leemos la imagen q nos llega */
    protected static Image getImage(URL imageURL)
    {
        Image image = null;
        try
        {
            // use ImageIO to read in the image
            image = ImageIO.read(imageURL);
        }
        catch (Exception ioe)
        {
            ioe.printStackTrace();
        }
        return image;
    }
}
