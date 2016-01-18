/**
 * JDialogLocal.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.dialogs.documents.dialogs.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.localgis.app.gestionciudad.beans.DocumentBean;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

/**
 * Created by IntelliJ IDEA.
 * User: lara
 * Date: 05-may-2006
 * Time: 11:37:07
 * To change this template use File | Settings | File Templates.
 */

public class JDialogLocal extends JDialog
{
    private JTextField jTextFieldFichero = new JTextField();
    private JButton jButtonFichero = new JButton();
    private JPanel jPanelFichero = new JPanel();
    public JPanelComentarios jPanelComentario =new JPanelComentarios();
    public OKCancelPanel okCancelPanel = new OKCancelPanel();
    private boolean newDocument = false;

    /* constructor de la clase */
    public JDialogLocal(JFrame frame, char tipo){
        super(frame);
        jbinit(frame, tipo);
    }


    public JDialogLocal(JFrame frame, char tipo, boolean fromInventario){
        super(frame);
        jPanelComentario= new JPanelComentarios(fromInventario);
        jbinit(frame, tipo);
    }

    public void jbinit(JFrame frame, char tipo)
    {
        //super(frame);
        final char tipos = tipo;
        okCancelPanel.addActionListener(new java.awt.event.ActionListener()
        {
			public void actionPerformed(ActionEvent e)
            {
                okCancelPanel_actionPerformed(e);
			}
		});
		this.addComponentListener(new java.awt.event.ComponentAdapter()
        {
			public void componentShown(ComponentEvent e)
            {
				this_componentShown(e);
			}
		});
        setModal(true);
        getContentPane().setLayout(new BorderLayout());
        jPanelComentario.setEnabled(true);
        getContentPane().add(jPanelComentario, BorderLayout.CENTER);
        getContentPane().add(okCancelPanel, BorderLayout.SOUTH);
        jPanelFichero.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelFichero.setPreferredSize(new Dimension(400, 50));
        jTextFieldFichero.setPreferredSize(new Dimension(300, 21));
        jPanelFichero.add(jTextFieldFichero, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
        jPanelFichero.add(jButtonFichero, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, 21, 21));
        jButtonFichero.setText("...");
        jButtonFichero.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                seleccionarFichero(tipos);
            }
        });
        getContentPane().add(jPanelFichero, BorderLayout.NORTH);
        setSize(500, 300);
        setLocation(150, 90);
    }

    /* método xa salvar el documento, imagen o texto */
    public DocumentBean save(DocumentBean documento)
    {
        documento = jPanelComentario.save(documento);
        documento.setFileName(jTextFieldFichero.getText());
        return documento;
    }

    /* método xa cargar el documento, imagen o texto */
    public void load(DocumentBean documento)
    {
        jPanelComentario.load(documento);
        jTextFieldFichero.setText(documento.getFileName());
    }

    /* abrimos una ventana y seleccionamos un documento, imagen o texto en una ubicación */
    private void seleccionarFichero(char tipo)
    {
        JFileChooser chooser = new JFileChooser();
        com.geopista.app.utilidades.GeoPistaFileFilter filter = new com.geopista.app.utilidades.GeoPistaFileFilter();

        if(tipo == DocumentBean.DOC_CODE)
        {
            /* filtro xa documentos */
            filter.addExtension("doc");
            filter.addExtension("ppt");
            filter.addExtension("pdf");
            filter.addExtension("txt");
            filter.addExtension("html");
        }
        else if(tipo == DocumentBean.IMG_CODE)
        {
            /* filtro xa imagenes */
            filter.addExtension("jpg");
            filter.addExtension("gif");
            filter.addExtension("bmp");
            filter.addExtension("png");
        }
        else
        {
            /* filtro xa texto plano */
            filter.addExtension("txt");
        }
        chooser.setFileFilter(filter);

        chooser.setMultiSelectionEnabled(false);
        if(chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        jTextFieldFichero.setText(chooser.getSelectedFile().getAbsolutePath());
        newDocument = true;
    }

    /* sólo se realiza una accion si se ha seleccionado un elemento */
    void okCancelPanel_actionPerformed(ActionEvent e)
    {
        if(okCancelPanel.wasOKPressed())
        {
            if(jTextFieldFichero.getText().length() == 0) return;
        }
        setVisible(false);
        return;
    }

    /* recogemos el evento cd se pulsa Aceptar */
    void this_componentShown(ComponentEvent e)
    {
        okCancelPanel.setOKPressed(false);
    }

    /* devolvemos true o false dependiendo de si el documento es nuevo o no */
    public boolean isNewDocument()
    {
        return newDocument;
    }
}
