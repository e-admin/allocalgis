package com.geopista.app.catastro.registroExpedientes.paneles;

import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.Persona;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.app.AppContext;
import com.geopista.server.administradorCartografia.ACException;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 13-mar-2007
 * Time: 12:02:15
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que muestra un dialogo para buscar referencias catastrales a partir de un patron del nif del titular
 *  dado por el usuario. El dialogo accede a base de datos para buscar las parcelas que contienen el patron que
 * el usuario a suministrado. Una vez que han aparecido los resultados en la tabla el usuario puede seleccionar
 * UNA entrada de la tabla y darle a aceptar para que la clase que le ha llamado al dialogo recoja la selecion
 * del usuario al cerrarse la ventana.
 */

public class DialogoBuscarBienInmuebleNifTitular extends javax.swing.JDialog
{
    private JPanel todoPanel;
    private JLabel nifTitularLabel;
    private JTextField nifTitularJTField;
    private JTable referenciasResutaldosTable;
    private JScrollPane referenciasResultadosScroll;
    private String[] identificadores;
    private DefaultTableModel modelo;
    private JButton buscarRefCatasJButton;
    private JButton cancelarJButton;
    private JButton aceptarJButton;
    private ArrayList referenciasCatastrales= new ArrayList();
    private ArrayList referenciasCatastralesSelecionadas;

    /**
     * Constructor de la calse. Inicializa el panel y lo muestra. Necesia el parametro parent (Frame) y modal (boolean)
     * para pasarselo al padre.
     *
     * @param parent JFrame
     * @param modal boolean
     */
    public DialogoBuscarBienInmuebleNifTitular(java.awt.Frame parent, boolean modal)
    {
		super(parent, modal);
		inicializaDialogo();
	}

    /**
     * Inicializa los elemento de la gui con sus determinados valores y eventos.
     */
    private void inicializaDialogo()
    {
        todoPanel = new JPanel();
        nifTitularLabel = new JLabel();
        nifTitularJTField = new JTextField();
        referenciasResutaldosTable = new JTable();
        referenciasResultadosScroll = new JScrollPane(referenciasResutaldosTable);
        buscarRefCatasJButton = new JButton();
        cancelarJButton = new JButton();
        aceptarJButton = new JButton();
        identificadores = new String[5];
        modelo= new DefaultTableModel()
        {
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        buscarRefCatasJButton.setIcon(UtilRegistroExp.iconoZoom);
        buscarRefCatasJButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                buscarRefCatasJButtonActionPerformed();
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
        referenciasResutaldosTable.setModel(modelo);
		//referenciasResutaldosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		referenciasResutaldosTable.setCellSelectionEnabled(false);
		referenciasResutaldosTable.setColumnSelectionAllowed(false);
        referenciasResutaldosTable.setRowSelectionAllowed(true);

        todoPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        todoPanel.add(nifTitularLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 195, 20));
        todoPanel.add(nifTitularJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 250, 20));
        todoPanel.add(buscarRefCatasJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 20, 20));

        todoPanel.add(referenciasResultadosScroll, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 480, 300));

        todoPanel.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 380, 90, 30));
        todoPanel.add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 380, 90, 30));

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarBienInmuebleNifTitular.titulo"));
        getContentPane().add(todoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 510, 420));
        pack();
    }

    /**
     * Cambia las etiquetas al idioma que el usuario tenga selecionado.
     */
    private void renombrarComponentes()
    {
        identificadores[0] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarBienInmuebleNifTitular.nifLabel"));
        identificadores[1] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarBienInmuebleNifTitular.refCatastralLabel"));
        identificadores[2] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarBienInmuebleNifTitular.tipoViaLabel"));
        identificadores[3] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarBienInmuebleNifTitular.nombreViaLabel"));
        identificadores[4] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarBienInmuebleNifTitular.numeroLabel"));
        nifTitularLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarBienInmuebleNifTitular.nifTitularLabel"));
        buscarRefCatasJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarBienInmuebleNifTitular.buscarRefCatasJButton.hint"));
        aceptarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarBienInmuebleNifTitular.aceptarJButton"));
        aceptarJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarBienInmuebleNifTitular.aceptarJButton.hint"));
        cancelarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarBienInmuebleNifTitular.cancelarJButton"));
        cancelarJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarBienInmuebleNifTitular.cancelarJButton.hint"));
        modelo.setColumnIdentifiers(identificadores);
    }

    /**
     * Devuelve la selecion de referencias catastrales del usuario. Ahora mismo solo se permite una pero se crea un
     * ArrayList por si se desearan mas en un fufuro.
     *
     * @return ArrayList devuelve la referencia seleccionada por el usuario
     */
    public ArrayList getReferenciasCatastralesSelecionadas()
    {
        return referenciasCatastralesSelecionadas;
    }

    /**
     * Carga los datos de las referencias buscadas en la tabla para ser visualizados.
     */
    private void cargaDatosTabla()
    {
        AppContext app =(AppContext) AppContext.getApplicationContext();        
        Estructuras.cargarEstructura("Tipos de via normalizados de Catastro");
        String[][] datos= new String[referenciasCatastrales.size()][5];
        for(int i=0; i< referenciasCatastrales.size();i++)
        {
            DireccionLocalizacion auxDir = null;
            if(referenciasCatastrales.get(i) instanceof Persona)
            {
                Persona persona= (Persona) referenciasCatastrales.get(i);
                datos[i][0] = persona.getNif();
                BienInmuebleCatastro bienInmueble = persona.getBienInmueble();
                auxDir = bienInmueble.getDomicilioTributario();
                datos[i][1] = bienInmueble.getIdBienInmueble().getParcelaCatastral() + bienInmueble.getIdBienInmueble().getNumCargo();
            }

            if(Estructuras.getListaTipos().getDomainNode(auxDir.getTipoVia())!=null)
            {
                datos[i][1] = Estructuras.getListaTipos().getDomainNode(auxDir.getTipoVia())
                        .getTerm(app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"));
            }
            else
            {
                datos[i][1] = "";
            }
            datos[i][3] = auxDir.getNombreVia();
            if (auxDir.getPrimerNumero() == -1){
            	datos[i][4] = "";
            }
            else{
            	datos[i][4] = String.valueOf(auxDir.getPrimerNumero());
            }
        }
        modelo.setDataVector(datos,identificadores);
        referenciasResutaldosTable.setModel(modelo);
    }

    /**
     * Accion para el evento de cancelar. Cierra el dialogo.
     */
    private void cancelarJButtonActionPerformed()
    {
		dispose();
	}

    /**
     * Metodo que mediante clienteCatastro hace una llamada a base de datos para buscar las referencias catastrales
     * que contienen el patron del usuario.
     */
    private void buscarRefCatasJButtonActionPerformed()
    {
        AppContext app =(AppContext) AppContext.getApplicationContext();
        final JFrame desktop= (JFrame)app.getMainFrame();
        final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
        //progressDialog.setTitle(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.DialogoBuscarBienInmuebleNifTitular.msgBuscando"));
        progressDialog.setTitle("TaskMonitorDialog.Wait");
        progressDialog.addComponentListener(new ComponentAdapter()
        {
            public void componentShown(ComponentEvent e)
            {
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            progressDialog.report(I18N.get("RegistroExpedientes",
                            "Catastro.RegistroExpedientes.DialogoBuscarBienInmuebleNifTitular.msgBuscando"));
                            try
                            {
                                String patron = nifTitularJTField.getText();
                                //Todo mirar convenio
                                referenciasCatastrales= (ArrayList)ConstantesRegistroExp.clienteCatastro.getBienInmuebleBuscadosPorTitular(patron);
                                cargaDatosTabla();
                            }
                            catch(ACException e)
                            {
                                JOptionPane.showMessageDialog(desktop,e.getCause().getMessage());
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }

                        }
                        catch(Exception e)
                        {
                            ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
                            return;
                        }
                        finally
                        {
                            progressDialog.setVisible(false);
                        }
                    }
              }).start();
          }
       });
       GUIUtil.centreOnWindow(progressDialog);
       progressDialog.setVisible(true);
    }

    /**
     * Metodo asociado al evento del boton aceptar que coge la selecion del usuario de la tabla y lo almacena en el
     * atributo global referenciasCatastralesSelecionadas, para que la clase superior recoja la correcta.
     */
    private void aceptarJButtonActionPerformed()
    {
        if(referenciasCatastrales.size()>0 &&referenciasResutaldosTable.getSelectedRow()>=0)
        {
            referenciasCatastralesSelecionadas = new ArrayList();
            int filas[] = referenciasResutaldosTable.getSelectedRows();
            for(int i=0; i< filas.length; i++){
                if(referenciasCatastrales.get(filas[i]) instanceof Persona)
                    referenciasCatastralesSelecionadas.add(((Persona)referenciasCatastrales.get(filas[i])).getBienInmueble());
            }

            //PREV int fila = referenciasResutaldosTable.getSelectedRow();
            //PREV if(referenciasCatastrales.get(fila) instanceof Persona)
            //PREV     referenciasCatastralesSelecionadas.add(((Persona)referenciasCatastrales.get(fila)).getBienInmueble());

            dispose();
        }
        else
        {
            JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.DialogoBuscarBienInmuebleNifTitular.BuscarYSelecMSG"));
        }
    }
}
