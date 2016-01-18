package com.geopista.app.catastro.registroExpedientes.paneles;

import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
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
 * Date: 01-feb-2007
 * Time: 10:05:35
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que muestra un dialogo para buscar referencias catastrales a partir de un patron de la misma dado por el usuario.
 *  El dialogo accede a base de datos para buscar las parcelas que contienen el patron que el usuario a suministrado.
 * Una vez que han aparecido los resultados en la tabla el usuario puede seleccionar UNA entrada de la tabla y darle a
 * aceptar para que la clase que le ha llamado al dialogo recoja la selecion del usuario al cerrarse la ventana.
 */

public class DialogoBuscarRefCatastrales extends javax.swing.JDialog
{
    private JPanel todoPanel;
    private JLabel referenciaCatastralLabel;
    private JTextField referenciaCatastralJTField;
    private JTable referenciasResutaldosTable;
    private JScrollPane referenciasResultadosScroll;
    private String[] identificadores;
    private DefaultTableModel modelo;
    private JButton buscarRefCatasJButton;
    private JButton cancelarJButton;
    private JButton aceptarJButton;
    private ArrayList referenciasCatastrales= new ArrayList();
    private ArrayList referenciasCatastralesSelecionadas;
    private String convenioExpediente;

    /**
     * Constructor de la calse. Inicializa el panel y lo muestra. Necesia el parametro parent (Frame) y modal (boolean)
     * para pasarselo al padre.
     *
     * @param parent JFrame
     * @param modal boolean
     * @param convenioExpediente String
     */
    public DialogoBuscarRefCatastrales(java.awt.Frame parent, boolean modal, String convenioExpediente)
    {
		super(parent, modal);
        this.convenioExpediente= convenioExpediente;
        inicializaDialogo();
	}

    /**
     * Inicializa los elemento de la gui con sus determinados valores y eventos.
     */
    private void inicializaDialogo()
    {
        todoPanel = new JPanel();
        referenciaCatastralLabel = new JLabel();
        referenciaCatastralJTField = new JTextField();
        referenciasResutaldosTable = new JTable();
        referenciasResultadosScroll = new JScrollPane(referenciasResutaldosTable);
        buscarRefCatasJButton = new JButton();
        cancelarJButton = new JButton();
        aceptarJButton = new JButton();
        identificadores = new String[4];
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
        todoPanel.add(referenciaCatastralLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 195, 20));
        todoPanel.add(referenciaCatastralJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 250, 20));
        todoPanel.add(buscarRefCatasJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 20, 20));

        todoPanel.add(referenciasResultadosScroll, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 480, 300));

        todoPanel.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 380, 90, 30));
        todoPanel.add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 380, 90, 30));

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastrales.titulo"));
        getContentPane().add(todoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 510, 420));
        pack();
    }

    /**
     * Cambia las etiquetas al idioma que el usuario tenga selecionado.
     */
    private void renombrarComponentes()
    {
        identificadores[0] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastrales.refCatastralLabel"));
        identificadores[1] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastrales.tipoViaLabel"));
        identificadores[2] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastrales.nombreViaLabel"));
        identificadores[3] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastrales.numeroLabel"));
        referenciaCatastralLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastrales.referenciaCatastralLabel"));
        buscarRefCatasJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastrales.buscarRefCatasJButton.hint"));
        aceptarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastrales.aceptarJButton"));
        aceptarJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastrales.aceptarJButton.hint"));
        cancelarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastrales.cancelarJButton"));
        cancelarJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastrales.cancelarJButton.hint"));
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
        String[][] datos= new String[referenciasCatastrales.size()][4];
        for(int i=0; i< referenciasCatastrales.size();i++)
        {
            DireccionLocalizacion auxDir = null;
            if(referenciasCatastrales.get(i) instanceof FincaCatastro)
            {
                FincaCatastro parcela = (FincaCatastro)referenciasCatastrales.get(i);
                auxDir = parcela.getDirParcela();
                datos[i][0] = parcela.getRefFinca().getRefCatastral();
            }
            else if(referenciasCatastrales.get(i) instanceof BienInmuebleCatastro)
            {
                BienInmuebleCatastro bienInmueble = (BienInmuebleCatastro)referenciasCatastrales.get(i);
                auxDir = bienInmueble.getDomicilioTributario();
                datos[i][0] = bienInmueble.getIdBienInmueble().getIdBienInmueble();
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
            datos[i][2] = auxDir.getNombreVia();
            datos[i][3] = String.valueOf(auxDir.getPrimerNumero());
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
        progressDialog.setTitle(I18N.get("RegistroExpedientes",
                    "Catastro.RegistroExpedientes.DialogoBuscarRefCatastrales.msgBuscando"));
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
                            "Catastro.RegistroExpedientes.DialogoBuscarRefCatastrales.msgBuscando"));
                            try
                            {
                                String patron = referenciaCatastralJTField.getText();
                                if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                                        &&convenioExpediente.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                                {
                                    referenciasCatastrales= (ArrayList)ConstantesRegistroExp.clienteCatastro.getFincaCatastroBuscadas(patron);
                                }
                                else if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)
                                        ||(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                                        &&convenioExpediente.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)))
                                {
                                    referenciasCatastrales= (ArrayList)ConstantesRegistroExp.clienteCatastro.getBienesInmueblesCatastroBuscadas(patron);
                                }
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
                referenciasCatastralesSelecionadas.add(referenciasCatastrales.get(filas[i]));
            }
            //PREV int fila = referenciasResutaldosTable.getSelectedRow();
            //PREV referenciasCatastralesSelecionadas.add(referenciasCatastrales.get(fila));
            dispose();
        }
        else
        {
            JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.DialogoBuscarRefCatastrales.BuscarYSelecMSG"));
        }
    }
}
