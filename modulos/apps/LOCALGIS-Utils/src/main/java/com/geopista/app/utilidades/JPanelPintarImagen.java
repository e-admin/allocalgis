/**
 * JPanelPintarImagen.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.utilidades;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.log4j.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 31-may-2005
 * Time: 19:53:45
 */
public class JPanelPintarImagen extends JPanel {
        private Logger logger = Logger.getLogger(JPanelPintarImagen.class);
        /** The image to paint in the background */
        private Image bgImage;

        public JPanelPintarImagen() {
            //bgImage = getImage(getClass().getResource("Madrid.jpg"));
        }
        public JPanelPintarImagen(Image bgImage) {
            this.bgImage = bgImage;
        }

        public JPanelPintarImagen(String sUrl) {
            ClassLoader cl =getClass().getClassLoader();
            bgImage = getImage(cl.getResource(sUrl));
         }
        public void setImage(Image bgImage)
        {
            this.bgImage=bgImage;
            repaint();
        }
        public void setImage()
        {
            bgImage = null;
            repaint();
        }


        public void setImage(String sUrl)
        {
            logger.info("Buscanoo la imagen: "+sUrl);
            ClassLoader cl =getClass().getClassLoader();
            bgImage = getImage(cl.getResource(sUrl));
            if (bgImage == null){
                /** No existe el fichero imagen para ese estado. Mostramos el workflow
                 * definido por defecto para el tipo de obra correspondiente (sUrl=path/tipoObra_estdo.jpg) */
                String porDefecto= "";
                int index= sUrl.lastIndexOf("/");
                int index2= sUrl.lastIndexOf("_");
                if ((index != -1) && (index2 != -1) && (index < index2)){
                    porDefecto= sUrl.substring(index, index2);
                    porDefecto+= ".jpg";
                    sUrl= sUrl.substring(0, index)+porDefecto;
                }
                bgImage = getImage(cl.getResource(sUrl));
            }

           repaint();
        }

        public void paint(Graphics g) {
            // if a background image exists, paint it

            if (bgImage != null) {
                 //int width = getWidth();
                 //int height = getHeight();
                 int imageW = bgImage.getWidth(null);
                 int imageH = bgImage.getHeight(null);
                 setSize(imageW,imageH);
                 /*if (imageW>width)
                 {
                     width=imageW;
                     setSize(new Dimension(width,height));
                 }
                 if (imageH>height)
                 {
                     height=imageH;
                     setSize(new Dimension(width,height));
                 }*/
                 int x=0;
                 int y=0;
                 //try{x=(width-imageW) / 2;}catch(Exception e){};
                 //try{y=(height-imageH) /2;}catch(Exception e){};
                 g.drawImage(bgImage, x, y,imageW,imageH,this);
                 setPreferredSize(new Dimension(imageW,imageH));
            }
        }

        public void actionPerformed(ActionEvent ae) {
            bgImage = getImage(getClass().getResource(ae.getActionCommand()));
            repaint();
        }
         protected static Image getImage(URL imageURL) {
        Image image = null;

        try {
            if (imageURL!=null)
                image = ImageIO.read(imageURL);
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

        return image;
    }
}

