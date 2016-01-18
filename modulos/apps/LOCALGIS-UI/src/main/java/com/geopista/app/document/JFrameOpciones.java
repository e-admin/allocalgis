/**
 * JFrameOpciones.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.document;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.geopista.app.AppContext;

/**
 * Created by IntelliJ IDEA.
 * User: lara
 * Date: 03-may-2006
 * Time: 15:56:13
 * To change this template use File | Settings | File Templates.
 */

public class JFrameOpciones  extends JDialog
{
    /* Panel */
    private JPanel jPanelSelected = new JPanel();

    /* Botones de opciones */
    private JRadioButton radioButtonLocal;
    private JRadioButton radioButtonBD;
    private JRadioButton radioButtonInternet;
    private ButtonGroup group = new ButtonGroup();

    /* Botones */
    private JButton bAceptar = new JButton();
    private JButton bCancelar = new JButton();

    /* variables auxiliares */
    private boolean aceptar=false;
    private boolean local=false;
    private boolean internet=false;
    private String sbd = "bd";
    private String slocal = "local";
    private String sinternet = "internet";

    /* constructor de la clase */
    public JFrameOpciones(JFrame frame)
    {
        super(frame);
        setModal(true);
        final AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
        setTitle(aplicacion.getI18nString("document.infodocument.panel.titulo"));
        jPanelSelected.setLayout(new FlowLayout());

        JPanel jSelect = new JPanel();
        JPanel grid = new JPanel();

        radioButtonLocal = new JRadioButton(aplicacion.getI18nString("document.infodocument.opciones.bd"));
        radioButtonLocal.setActionCommand(sbd);

        radioButtonBD = new JRadioButton(aplicacion.getI18nString("document.infodocument.opciones.local"));
        radioButtonBD.setActionCommand(slocal);
        radioButtonBD.setSelected(true);

        radioButtonInternet = new JRadioButton(aplicacion.getI18nString("document.infodocument.opciones.internet"));
        radioButtonInternet.setActionCommand(sinternet);

        
        group.add(radioButtonLocal);
        group.add(radioButtonBD);
        group.add(radioButtonInternet);

        grid.setLayout(new GridLayout(1, 2));
        grid.add(radioButtonBD);
        grid.add(radioButtonLocal);
        grid.add(radioButtonInternet);
        grid.setAlignmentX(0.0f);

        bAceptar.setText(aplicacion.getI18nString("document.infodocument.botones.aceptar"));
        bAceptar.setBounds(new Rectangle(30, 25, 123, 100));
        bAceptar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
               pulsarAceptar();
            }
        });
        bCancelar.setText(aplicacion.getI18nString("document.infodocument.botones.cancelar"));
        bCancelar.setBounds(new Rectangle(80, 25, 123, 100));
        bCancelar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                pulsarCancelar();
            }
        });
        jSelect.add(bAceptar, null);
        jSelect.add(bCancelar, null);

        jPanelSelected.add(grid, null);
        jPanelSelected.add(jSelect, null);

        getContentPane().add(jPanelSelected);

        setSize(450, 150);
        setLocation(200, 350);
    }

    /* método q guarda la opción seleccionada cd pulsamos Aceptar */
    public void pulsarAceptar()
    {
         aceptar=true;
         String command = group.getSelection().getActionCommand();
         local=(command == slocal);
         internet=(command == sinternet);
         this.hide();
    }

    /* método q guarda la opción seleccionada cd pulsamos Cancelar */
    public void pulsarCancelar()
    {
         aceptar=false;
         this.hide();
    }

    /* devolvemos true o false dependiendo de si se ha pulsado Aceptar o Cancelar */
    public boolean isAceptar()
    {
        return aceptar;
    }

    /* determinamos si se ha pulsado Aceptar o Cancelar */
    public void setAceptar(boolean aceptar)
    {
        this.aceptar = aceptar;
    }

    /* devolvemos true o false dependiendo de si se añade en local o en BD */
    public boolean isLocal()
    {
        return local;
    }

    /* determinamos si se añade en local o en BD */
    public void setLocal(boolean local)
    {
        this.local = local;
    }
    
    
    /* devolvemos true o false dependiendo de si se ha seleccionado añadir de internet*/
    public boolean isInternet()
    {
        return internet;
    }
    
    /* determinamos si se añade en local o en BD */
    public void setInternet(boolean internet)
    {
        this.internet = internet;
    }
}
