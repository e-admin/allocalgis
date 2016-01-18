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
    private ButtonGroup group = new ButtonGroup();

    /* Botones */
    private JButton bAceptar = new JButton();
    private JButton bCancelar = new JButton();

    /* variables auxiliares */
    private boolean aceptar=false;
    private boolean local=false;
    private String sbd = "bd";
    private String slocal = "local";

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

        group.add(radioButtonLocal);
        group.add(radioButtonBD);

        grid.setLayout(new GridLayout(0, 2));
        grid.add(radioButtonBD);
        grid.add(radioButtonLocal);
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

        setSize(330, 190);
        setLocation(150, 90);
    }

    /* método q guarda la opción seleccionada cd pulsamos Aceptar */
    public void pulsarAceptar()
    {
         aceptar=true;
         String command = group.getSelection().getActionCommand();
         local=(command == slocal);
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
}
