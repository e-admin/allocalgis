package com.geopista.app.image;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import java.io.ByteArrayInputStream;
import java.net.URL;

import com.geopista.app.document.DocumentInterface;
import com.geopista.protocol.document.DocumentBean;

/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 06-abr-2005
 * Time: 12:52:54
 */

public class PintarImagen extends JPanel
{
    private DocumentBean documentBean;
    private DocumentInterface docI;

    private ShowImagen showImagen;
    private static PintarImagen lastSelected=null;
    private int indicePanel;

    /* constructor de la clase */
    public PintarImagen(DocumentInterface docInterface, int indiceImagenPanel)
    {
        indicePanel = indiceImagenPanel;
        setBorder(new EtchedBorder());
        setLayout(new BorderLayout());
        showImagen = new ShowImagen();
        add(showImagen);
        docI = docInterface;
        addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                if (docI != null && documentBean!=null){
                    docI.seleccionar(documentBean, indicePanel);
                    if (lastSelected!=null) lastSelected.setSelected(false);
                    lastSelected=(PintarImagen)evt.getSource();
                    lastSelected.setSelected(true);
                }
            }
        });
    }

    /* determinamos las imagenes de la lista */
    public void setThumbnail(DocumentBean document) throws Exception
    {
        documentBean = document;
        if (document==null || document.getThumbnail()==null)
            showImagen.setImage((Image)null);
        else
            showImagen.setImage(ImageIO.read(new ByteArrayInputStream(documentBean.getThumbnail())));
    }

    /* determinamos la imagen del panel */
    public void setImagen(DocumentBean document) throws Exception
    {
        documentBean = document;
        if (document==null || document.getContent()==null)
            showImagen.setImage((Image)null);
        else
            showImagen.setImage(ImageIO.read(new ByteArrayInputStream(documentBean.getContent())));
    }

    /* obtenemos el documento */
    public DocumentBean getDocumentBean()
    {
        return documentBean;
    }

    public void setPreferredSize(Dimension dimension)
    {
        super.setPreferredSize(dimension);
        showImagen.setPreferredSize(dimension);
    }

    public void setSelected(boolean selected)
    {
        if (selected)
            setBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(5, 5, 5, 5), new java.awt.Color(255, 0, 0)));
        else
            setBorder(new EtchedBorder());
    }

    public void setIndicePanel(int indicePanel)
    {
        this.indicePanel = indicePanel;
    }
}


class ShowImagen extends JPanel
{
    /** The image to paint in the background */
    private Image bgImage;

    /* constructor de la clase (vacio)*/
    public ShowImagen()
    {
        //no lleva nada
    }

    /* constructor de la clase (con imagen) */
    public ShowImagen(Image bgImage)
    {
        this.bgImage = bgImage;
    }

    /* constructor de la clas (con URL)*/
    public ShowImagen(String sUrl)
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
    public void setDocument(DocumentBean doc) throws Exception
    {
        Image image = ImageIO.read(new ByteArrayInputStream(doc.getThumbnail()));
        setImage(image);
    }

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



