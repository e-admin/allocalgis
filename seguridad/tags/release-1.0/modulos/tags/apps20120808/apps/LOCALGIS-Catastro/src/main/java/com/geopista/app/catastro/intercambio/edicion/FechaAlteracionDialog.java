package com.geopista.app.catastro.intercambio.edicion;

import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.vividsolutions.jump.I18N;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;


/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 29-jun-2007
 * Time: 11:37:52
 * To change this template use File | Settings | File Templates.
 */
public class FechaAlteracionDialog extends javax.swing.JDialog
{
    private JPanel todoPanel;
    private JLabel fechaAltLabel;
    private JTextField fechaAltJTfield;
    private JButton fechaAltButton;
    private JButton aceptarJButton;
    private JButton cancelarJButton;
    private JFrame desktop;
    private Date fechaReg;
    boolean cancelar = true;

    /**
     * Constructor de la calse. Inicializa el panel y lo muestra. Necesia el parametro parent (Frame)
     * para pasarselo al padre.
     *
     * @param parent JFrame
     */
    public FechaAlteracionDialog(java.awt.Frame parent, Date fechaReg)
    {
        super(parent, true);
        desktop = (JFrame)parent;
        this.fechaReg = fechaReg;
        inicializaDialogo();
	}

    /**
     * Inicializa los elemento de la gui con sus determinados valores y eventos.
     */
    private void inicializaDialogo()
    {
        todoPanel = new JPanel();
        fechaAltLabel = new JLabel();
        fechaAltJTfield = new JTextField();
        fechaAltButton = new JButton();
        aceptarJButton = new JButton();
        cancelarJButton = new JButton();

        fechaAltJTfield.setEnabled(false);
        fechaAltJTfield.setText(UtilRegistroExp.showToday());
        fechaAltButton.setIcon(UtilRegistroExp.iconoZoom);
        fechaAltButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fechaAltButtonActionPerformed(evt);
            }
        });

        aceptarJButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                aceptarJButtonActionPerformed();
            }
        });

        cancelarJButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cancelarJButtonActionPerformed();
            }
        });


        renombrarComponentes();


        todoPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        todoPanel.add(fechaAltLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 100, 20));
        todoPanel.add(fechaAltJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 100, 20));
        todoPanel.add(fechaAltButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, 20, 20));
        todoPanel.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 60, 90, 30));
        todoPanel.add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 60, 90, 30));

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(I18N.get("Expedientes",
                        "Catastro.Intercambio.Edicion.FechaAlteracionDialog.titulo"));
        getContentPane().add(todoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 100));
        pack();
    }

    /**
     * Cambia las etiquetas al idioma que el usuario tenga selecionado.
     */
    private void renombrarComponentes()
    {
        fechaAltLabel.setText(I18N.get("Expedientes",
                        "Catastro.Intercambio.Edicion.FechaAlteracionDialog.fechaAltLabel"));
        fechaAltButton.setToolTipText(I18N.get("Expedientes",
                        "Catastro.Intercambio.Edicion.FechaAlteracionDialog.fechaAltButton.hint"));
        aceptarJButton.setText(I18N.get("Expedientes",
                        "Catastro.Intercambio.Edicion.FechaAlteracionDialog.aceptarJButton"));
        aceptarJButton.setToolTipText(I18N.get("Expedientes",
                        "Catastro.Intercambio.Edicion.FechaAlteracionDialog.aceptarJButton.hint"));
        cancelarJButton.setText(I18N.get("Expedientes",
                        "Catastro.Intercambio.Edicion.FechaAlteracionDialog.cancelarJButton"));
        cancelarJButton.setToolTipText(I18N.get("Expedientes",
                        "Catastro.Intercambio.Edicion.FechaAlteracionDialog.cancelarJButton.hint"));        

    }

    /**
     * Metodo que trata el evento lanzado por el boton de la fecha de alteracion y muestra el dialogo para selecionar
     * una fecha valida, recogiendola de la constante estatica calendarValue.
     *
     * @param evt Evento lanzado
     * */
    private void fechaAltButtonActionPerformed(ActionEvent evt)
    {
        UtilRegistroExp.showCalendarDialog(desktop);

		if ((ConstantesRegistroExp.calendarValue != null) && (!ConstantesRegistroExp.calendarValue.trim().equals("")))
        {
			fechaAltJTfield.setText(ConstantesRegistroExp.calendarValue);
		}
    }

    /**
     * Metodo asociado al evento del boton aceptar que coge la selecion del usuario y cierra el dialogo.
     */
    private void aceptarJButtonActionPerformed()
    {
        if(!checkeaFechaAlt())
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String s_fecha_alteracion = sdf.format(fechaReg);
            JOptionPane.showMessageDialog(this,I18N.get("Expedientes",
                    "Error.J6")  + " La fecha de registro es: " + s_fecha_alteracion);  
            return;
        }
        cancelar = false;
        dispose();
    }

    public Date getFechatAlteracion()
    {
        return UtilRegistroExp.getDate(fechaAltJTfield.getText());
    }

    /**
     * Comprueba que la fecha de alteracion no sea la de hoy, ya que tiene que ser menor que la de registro y la de
     * registro es la actual.
     *
     * @return boolean El resultado de la comprobacion
     */
    private boolean checkeaFechaAlt()
    {
        Calendar aux= new GregorianCalendar();
        aux.setTime(UtilRegistroExp.getDate(fechaAltJTfield.getText()));
        int annoAlt = aux.get(Calendar.YEAR);
        int diaAlt = aux.get(Calendar.DAY_OF_YEAR);
        aux.setTime(new Date(fechaReg.getTime()));
        int annoHoy = aux.get(Calendar.YEAR);
        int diaHoy = aux.get(Calendar.DAY_OF_YEAR);
        if(annoAlt>=annoHoy)
        {
            if(diaAlt>=diaHoy)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Accion para el evento de cancelar. Cierra el dialogo.
     */
    private void cancelarJButtonActionPerformed()
    {
        cancelar = true;
        dispose();
	}

    public boolean getCancelar()
    {
        return cancelar;
    }
}
