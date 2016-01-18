package com.geopista.app.catastro.registroExpedientes.paneles;

import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.vividsolutions.jump.I18N;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 12-feb-2007
 * Time: 11:32:26
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que muestra un dialogo para seleccionar una fecha correcta.
 */

public class DialogoCalendario extends javax.swing.JDialog
{
    private Date dFecha;
    private javax.swing.JButton aceptarJButton;
    private javax.swing.JButton cancelarJButton;
    private com.toedter.calendar.JCalendar jCalendar;
    private javax.swing.JPanel jPanel1;

    /**
     * Constructor de la calse. Inicializa el panel y lo muestra. Necesia el parametro parent (Frame) y modal (boolean)
     * para pasarselo al padre.
     *
     * @param parent JFrame
     * @param modal boolean
     */
	public DialogoCalendario(java.awt.Frame parent, boolean modal)
    {
		super(parent, modal);
		initComponents();
		configureComponents();
	}

    /**
     * Inicializa la variable global de donde se cogera el resultado de la consulta.
     */
    private void configureComponents()
    {
		ConstantesRegistroExp.calendarValue = "";
	}

    /**
     * Inicializa los componentes necesarios para mostrar el dialogo
     */
    private void initComponents()
    {
        jCalendar = new com.toedter.calendar.JCalendar();
        jPanel1 = new javax.swing.JPanel();
        cancelarJButton = new javax.swing.JButton();
        aceptarJButton = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.DialogoCalendario.titulo"));
        jCalendar.setBorder(new javax.swing.border.EtchedBorder());
        getContentPane().add(jCalendar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 230));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        cancelarJButton.setText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.DialogoCalendario.cancelarButton"));
        cancelarJButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cancelarJButtonActionPerformed(evt);
            }
        });

        jPanel1.add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, -1, -1));

        aceptarJButton.setText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.DialogoCalendario.aceptarButton"));
        aceptarJButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                aceptarJButtonActionPerformed(evt);
            }
        });

        jPanel1.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 240, 40));

        pack();
    }

    /**
     * Metodo que trata el evento del boton cancelar, no se almacena valor en la variable estatica y se cierra
     * el dialogo.
     *
     * @param evt evento capturado
     */
    private void cancelarJButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
		ConstantesRegistroExp.calendarValue = "";
        dFecha=null;
		dispose();
	}

    /**
     * Metodo que trata el evento del boton aceptar y almacena en la variable estatica el valor de la fecha
     * seleccionada por el usuario.
     *
     * @param evt evento capturado
     */
    private void aceptarJButtonActionPerformed(java.awt.event.ActionEvent evt)
    {

		DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
		String fecha=df.format(jCalendar.getCalendar().getTime());
        dFecha=jCalendar.getCalendar().getTime();
		ConstantesRegistroExp.calendarValue = fecha;
		dispose();

	}

    /**
     * Metodo que devuelve la fecha seleccionada por el usuario.
     *
     * @return Date
     */
    public Date getFecha()
    {
        return dFecha;
    }

    /**
     * Metodo que asigna una fecha a la variable dFecha
     *
     * @param dFecha
     */
    public void setFecha(Date dFecha)
    {
        this.dFecha = dFecha;
    }
}
