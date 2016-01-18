/**
 * JDialogText.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.text;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.geopista.app.document.JPanelComentarios;
import com.geopista.protocol.document.DocumentBean;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;


/**
 * Created by IntelliJ IDEA.
 * User: lara
 * Date: 26-may-2006
 * Time: 10:04:08
 * To change this template use File | Settings | File Templates.
 */

public class JDialogText extends JDialog
{
    public JPanelComentarios jPanelComentario =new JPanelComentarios();
    public TextPanel jTextPanel;
    public OKCancelPanel okCancelPanel = new OKCancelPanel();

    /* constructor de la clase */
    public JDialogText(JFrame frame)
    {
        super(frame);
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
        jTextPanel = new TextPanel();
        jTextPanel.setEnabled(true);
        getContentPane().add(jTextPanel, BorderLayout.NORTH);
        getContentPane().add(okCancelPanel, BorderLayout.SOUTH);
        setSize(600, 475);
        setLocation(150, 90);
    }

    /* método xa salvar el texto */
    public DocumentBean save(DocumentBean documento)
    {
        documento = jPanelComentario.save(documento);
        documento = jTextPanel.save(documento);
        return documento;
    }

    /* método xa cargar el documento, imagen o texto */
    public void load(DocumentBean documento)
    {
        jPanelComentario.load(documento);
        jTextPanel.loadTextBD(documento);
    }

    /* sólo se realiza una accion si se ha seleccionado un elemento */
    void okCancelPanel_actionPerformed(ActionEvent e)
    {
        if(okCancelPanel.wasOKPressed())
        {
            if(jTextPanel.getjTextAreaDocumento() == null) return;
        }
        setVisible(false);
        return;
    }

    /* recogemos el evento cd se pulsa Aceptar */
    void this_componentShown(ComponentEvent e)
    {
        okCancelPanel.setOKPressed(false);
    }
}

