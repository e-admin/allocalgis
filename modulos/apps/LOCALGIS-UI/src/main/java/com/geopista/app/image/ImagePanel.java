/**
 * ImagePanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.image;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import java.util.*;

import com.geopista.app.document.DocumentInterface;
import com.geopista.app.document.JPanelComentarios;
import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;

import com.geopista.feature.GeopistaFeature;
import com.geopista.global.ServletConstants;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.DocumentClient;
//import com.geopista.ui.plugin.edit.DocumentManagerPlugin;
import com.geopista.ui.plugin.edit.ImageManagerPlugin;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 08-may-2006
 * Time: 15:03:17
 */

public class ImagePanel extends JPanel implements DocumentInterface
 {
    private static final Log logger = LogFactory.getLog(ImagePanel.class);

    DocumentClient documentClient = null;
    private AppContext aplicacion;

    /* Panel imagenes */
    private JPanel jPanelThumbnails = new JPanel();
    private PintarImagen jPanelImage1;
    private PintarImagen jPanelImage2;
    private PintarImagen jPanelImage3;
    private PintarImagen jPanelImage4;
    private PintarImagen jPanelImage5;
    private JButton jButtonThumMenos = new JButton();
    private JButton jButtonThumMas = new JButton();

    /* Panel imagen */
    private JPanel jPanelShowImage = new JPanel();
    private JPanel jPanelBotones = new JPanel();
    private JPanelComentarios jPanelComentarios = new JPanelComentarios();
    private PintarImagen jPanelImage;
    private JButton jButtonImageMas = new JButton();
    private JButton jButtonImageMenos = new JButton();

    GeopistaFeature gFeature = new GeopistaFeature();
    DocumentInterface docInt;
    DocumentBean imageSelected = null;

    public ArrayList imagenes;

    private Vector jPanelImgThumbnails = new Vector();

    boolean grande = false;
    int direccion = 0;

    int imagenInicialPanel = 0;
    int imagenFinalPanel = 4;

    final static int NEXT = 0;
    final static int PREVIOUS = 1;
    private int indiceImagenSeleccionada;


    /* constructor de la clase q inicializa el panel */
    public ImagePanel(GeopistaFeature feature, Collection coleccion, DocumentInterface docInt)
    {
        jPanelImage1 = new PintarImagen(this, 0);
        jPanelImgThumbnails.add(jPanelImage1);
        jPanelImage2 = new PintarImagen(this, 0);
        jPanelImgThumbnails.add(jPanelImage2);
        jPanelImage3 = new PintarImagen(this, 0);
        jPanelImgThumbnails.add(jPanelImage3);
        jPanelImage4 = new PintarImagen(this, 0);
        jPanelImgThumbnails.add(jPanelImage4);
        jPanelImage5 = new PintarImagen(this, 0);
        jPanelImgThumbnails.add(jPanelImage5);

        jPanelImage = new PintarImagen(null, indiceImagenSeleccionada);

        gFeature = feature;
        this.docInt = docInt;
        imagenes = (ArrayList) coleccion;

        /* Panel Imagen */
        jPanelShowImage.setLayout(new BorderLayout());

        jPanelImage.setBorder(new EtchedBorder());
        jPanelImage.setPreferredSize(new Dimension(200, 200));
        jPanelShowImage.add(jPanelImage, BorderLayout.WEST);

        jPanelComentarios.setPreferredSize(new Dimension(200, 200));
        jPanelComentarios.setEnabled(false);
        jPanelShowImage.add(jPanelComentarios, BorderLayout.EAST);

        jButtonImageMenos.setText("<<");
        jButtonImageMenos.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                desplazarImage(PREVIOUS);
            }
        });
        jPanelBotones.add(jButtonImageMenos);

        jButtonImageMas.setText(">>");
        jButtonImageMas.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                desplazarImage(NEXT);
            }
        });
        jPanelBotones.add(jButtonImageMas);

        jPanelShowImage.add(jPanelBotones, BorderLayout.SOUTH);
        add(jPanelShowImage, BorderLayout.CENTER);

        /* Panel imagenes */
        jPanelThumbnails.setLayout(new AbsoluteLayout());

        actualizarModelo();

        jPanelImage1.setBorder(new EtchedBorder());
        jPanelThumbnails.add(jPanelImage1, new AbsoluteConstraints(10, 10, 80, 80));

        jPanelImage2.setBorder(new EtchedBorder());
        jPanelThumbnails.add(jPanelImage2, new AbsoluteConstraints(100, 10, 80, 80));

        jPanelImage3.setBorder(new EtchedBorder());
        jPanelThumbnails.add(jPanelImage3, new AbsoluteConstraints(190, 10, 80, 80));

        jPanelImage4.setBorder(new EtchedBorder());
        jPanelThumbnails.add(jPanelImage4, new AbsoluteConstraints(280, 10, 80, 80));

        jPanelImage5.setBorder(new EtchedBorder());
        jPanelThumbnails.add(jPanelImage5, new AbsoluteConstraints(370, 10, 80, 80));

        jButtonThumMenos.setText("<<");
        jButtonThumMenos.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                desplazarImages(PREVIOUS);
            }
        });
        jPanelThumbnails.add(jButtonThumMenos, new AbsoluteConstraints(10, 100, 60, -1));

        jButtonThumMas.setText(">>");
        jButtonThumMas.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                desplazarImages(NEXT);
            }
        });
        jPanelThumbnails.add(jButtonThumMas, new AbsoluteConstraints(390, 100, 60, -1));

        add(jPanelThumbnails, BorderLayout.SOUTH);
        aplicacion = (AppContext) AppContext.getApplicationContext();
        
    	String sUrl = aplicacion
				.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL)
				+ ServletConstants.DOCUMENT_SERVLET_NAME;
    	
        /*String sUrl = aplicacion.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
                ImageManagerPlugin.DOCUMENT_SERVLET_NAME;*/
        documentClient = new DocumentClient(sUrl);

        indiceImagenSeleccionada=-1;
        habilitarBotones();
    }

    /* método de la interfaz sin implementar */
    public void seleccionar(DocumentBean documento)
    {
    }

    /**
     * método xa cargar los comentarios y la opción de público asociados a una imagen,
     * y xa pintar el thumbnail de la imagen seleccionada.
     */
    public void seleccionar(DocumentBean documento, int indicePanel)
    {
        try
        {
            setImageSelected(documento);
            jPanelComentarios.load(documento);
            pintarImagen(documento);
            this.indiceImagenSeleccionada = indicePanel;
            actualizarModelo();
        }
        catch (Exception e)
        {
            logger.error("Error al pintar la imagen", e);
        }
    }

    /* obtenemos el contenido de la imagen y la pintamos */
    public void pintarImagen(DocumentBean documento)
    {
        try
        {
        if (documento.getContent() == null)
            {
                try
                {
                	if (!documento.getFileName().startsWith("http"))
                		documento.setContent(documentClient.getAttachedByteStream(documento));
                }
                catch (Exception e)
                {
                    logger.error("Error al obtener el contenido de la imagen ", e);
                    ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
                }
            }
            jPanelImage.setImagen(documento);
        }
        catch(Exception e)
        {
            logger.error("Error",e);
        }
    }

    /* habilitamos o deshabilitamos los botones dependiendo de la situacion en la q nos encontremos */
    public void habilitarBotones()
    {
        /* jImagePanel */
        if(indiceImagenSeleccionada == imagenes.size()-1)
            jButtonImageMas.setEnabled(false);
        else
            jButtonImageMas.setEnabled(true);

        if(indiceImagenSeleccionada <= 0)
            jButtonImageMenos.setEnabled(false);
        else
            jButtonImageMenos.setEnabled(true);

        /* jPanelThumbnails */
        if(imagenInicialPanel == 0)
            jButtonThumMenos.setEnabled(false);
        else
            jButtonThumMenos.setEnabled(true);

        if(imagenFinalPanel >= imagenes.size()-1)
            jButtonThumMas.setEnabled(false);
        else
            jButtonThumMas.setEnabled(true);
    }

    /* añadimos una imagen a la lista */
    public void add(DocumentBean documento)
    {
        imagenes.add(documento);
        int index=imagenes.size();
        if(imagenFinalPanel<(index-1))
        {
           imagenInicialPanel=index-jPanelImgThumbnails.size();
           imagenFinalPanel= index-1;
        }
        seleccionar(documento,index-1);
    }

    /* actualizamos la lista de imagenes */
    public void actualizarModelo()
    {
    	actualizarModelo(imagenes);
    }
    
    /* actualizamos la lista de imagenes */
    public void actualizarModelo(ArrayList imagenesList)
    {
        Enumeration e = jPanelImgThumbnails.elements();
        int j=0;
        for (int i = imagenInicialPanel; i <= imagenFinalPanel; i++)
        {
            try
            {
                if(i == imagenesList.size()) break;
                DocumentBean auxDoc = (DocumentBean) imagenesList.get(i);
                PintarImagen auxPintar = (PintarImagen) e.nextElement();
                //auxPintar.setSelected(imageSelected==null?false:auxDoc.getId()==imageSelected.getId());
                if (imageSelected==null){
                      	auxPintar.setSelected(false);
            	}

                j++;
                auxPintar.setThumbnail(auxDoc);
                auxPintar.setIndicePanel(i);
            }
            catch (Exception ex)
            {
                logger.error("Error al actualizar el modelo", ex);
            }
        }
        habilitarBotones();
        for (int x=j; x<jPanelImgThumbnails.size();x++)
        {
             try{
                 PintarImagen aux=((PintarImagen) e.nextElement());
                 aux.setImagen(null);
                 aux.setSelected(false);
             }
             catch(Exception ex)
             {
                 logger.error("Error al pintar la imagen ", ex);
             }
        }
    }

    /* borramos la imagen de la lista q ha eliminado el usuario */
    public void borrar(DocumentBean doc)
    {
        if (doc == null) return;
        imagenes.remove(doc);
        imageSelected = null;
        indiceImagenSeleccionada=-1;
        jPanelComentarios.load(null);
        actualizarModelo();
        try{
            jPanelImage.setImagen(null);
        }
        catch(Exception e)
        {
            logger.error("Error al borrar la imagen ", e);
        }

    }

    /* obtenemos la imagen q ha sido seleccionada*/
    public DocumentBean getImageSelected()
    {
        return imageSelected;
    }

    /* determinamos la imagen seleccionada */
    public void setImageSelected(DocumentBean imageSelected)
    {
        this.imageSelected = imageSelected;
    }

    /* obtenemos la feature seleccionada */
    public GeopistaFeature getgFeature()
    {
        return gFeature;
    }

    /* determinamos la feature seleccionada */
    public void setgFeature(GeopistaFeature gFeature)
    {
        this.gFeature = gFeature;
    }

    /* actualizamos la lista de imagenes cada vez q se opere sobre ellas */
    public void update(DocumentBean imagen)
    {
        if (imagenes == null || imagen == null) return;
        int i=0;
        for(Iterator it = imagenes.iterator(); it.hasNext();)
        {
            if(((DocumentBean)it.next()).getId().equals(imagen.getId()))
            {
                indiceImagenSeleccionada = i;
                imagenes.set(indiceImagenSeleccionada, imagen);
                jPanelComentarios.load(imagen);
                pintarImagen(imagen);
                setImageSelected(imagen);
                break;
            }
            i++;
        }
        actualizarModelo();
    }

    /* cd lo q desplazamos es la imagen del panel */
    private void desplazarImage(int direccion)
    {
        try
        {
            if (imagenes == null) return;
            if (direccion == NEXT)
            {
                indiceImagenSeleccionada++;
                if (indiceImagenSeleccionada>imagenFinalPanel)
                    desplazarImages(NEXT);
            }
            else
            {
                indiceImagenSeleccionada--;
                if (indiceImagenSeleccionada<imagenInicialPanel)
                    desplazarImages(PREVIOUS);
            }
            DocumentBean document = (DocumentBean) imagenes.get(indiceImagenSeleccionada);
            seleccionar(document, indiceImagenSeleccionada);
        }
        catch (Exception e)
        {
            logger.error("Error al pulsar los botones de desplazamiento ", e);
        }
    }

    /* cd lo q desplazamos es la lista de imagenes */
    private void desplazarImages(int direccion)
    {
        try
        {
            if (imagenes == null) return;
            if (direccion == NEXT)
            {
                imagenInicialPanel++;
                imagenFinalPanel++;
            }
            else
            {
                imagenInicialPanel--;
                imagenFinalPanel--;
            }
            actualizarModelo();
        }
        catch (Exception e)
        {
            logger.error("Error al pulsar los botones de desplazamiento ", e);
        }
    }

    /* método xa comprobar si una imagen ya existe */
    public boolean existImage(DocumentBean document)
    {
        for (Iterator it = imagenes.iterator(); it.hasNext();)
        {
            DocumentBean auxDocument = (DocumentBean) it.next();
            if (auxDocument.getId() == document.getId())
                return true;
        }
        return false;
    }
}