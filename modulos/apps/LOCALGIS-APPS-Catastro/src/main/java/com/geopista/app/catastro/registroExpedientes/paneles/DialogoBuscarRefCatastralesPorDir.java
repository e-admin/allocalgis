/**
 * DialogoBuscarRefCatastralesPorDir.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.registroExpedientes.paneles;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.server.administradorCartografia.ACException;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 01-feb-2007
 * Time: 14:42:05
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que muestra un dialogo para buscar referencias catastrales a partir del tipo de via y del nombre dado por el usuario.
 *  El dialogo accede a base de datos para buscar las parcelas que contienen el patron que el usuario a suministrado.
 * Una vez que han aparecido los resultados en la tabla el usuario puede seleccionar UNA entrada de la tabla y darle a
 * aceptar para que la clase que le ha llamado al dialogo recoja la selecion del usuario al cerrarse la ventana.
 */

public class DialogoBuscarRefCatastralesPorDir extends javax.swing.JDialog
{
    private JPanel todoPanel;
    private JLabel tipoViaJLabel;
    private ComboBoxEstructuras tipoViaJCBox;
    private JLabel nombreViaJLabel;
    private JTextField nombreViaJTField;
    private JTable referenciasResutaldosTable;
    private JScrollPane referenciasResultadosScroll;
    private String[] identificadores;
    private DefaultTableModel modelo;
    private JButton buscarRefCatasJButton;
    private JButton cancelarJButton;
    private JButton aceptarJButton;
    private ArrayList referenciasCatastrales= new ArrayList();
    private ArrayList referenciasCatastralesSelecionadas;
    private ArrayList tiposVias;
    private String convenioExpediente;

    /**
     * Constructor de la calse. Inicializa el panel y lo muestra. Necesia el parametro parent (Frame) y modal (boolean)
     * para pasarselo al padre.
     *
     * @param parent JFrame
     * @param modal boolean
     * @param convenioExpediente String
     */
    public DialogoBuscarRefCatastralesPorDir(java.awt.Frame parent, boolean modal, String convenioExpediente)
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
        tipoViaJLabel = new JLabel();
        nombreViaJLabel = new JLabel();
        nombreViaJTField = new JTextField();
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

        inicializaComboBox();
        //cargarTiposVias();
        renombrarComponentes();

        referenciasResutaldosTable.setModel(modelo);
		//referenciasResutaldosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		referenciasResutaldosTable.setCellSelectionEnabled(false);
		referenciasResutaldosTable.setColumnSelectionAllowed(false);
        referenciasResutaldosTable.setRowSelectionAllowed(true);

        todoPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        todoPanel.add(tipoViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 195, 20));
        todoPanel.add(tipoViaJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 250, 20));
        todoPanel.add(nombreViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 195, 20));
        todoPanel.add(nombreViaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, 250, 20));
        todoPanel.add(buscarRefCatasJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, 20, 20));

        todoPanel.add(referenciasResultadosScroll, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 480, 300));

        todoPanel.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 400, 90, 30));
        todoPanel.add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 400, 90, 30));

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastralesPorDir.titulo"));
        getContentPane().add(todoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 510, 440));
        pack();
    }

    /**
     * Cambia las etiquetas al idioma que el usuario tenga selecionado.
     */
    private void renombrarComponentes()
    {
        identificadores[0] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastralesPorDir.refCatastralLabel"));
        identificadores[1] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastralesPorDir.tipoViaLabel"));
        identificadores[2] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastralesPorDir.nombreViaLabel"));
        identificadores[3] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastralesPorDir.numeroLabel"));
        tipoViaJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastralesPorDir.tipoViaJLabel"));
        nombreViaJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastralesPorDir.nombreJViaLabel"));
        buscarRefCatasJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastralesPorDir.buscarRefCatasJButton.hint"));
        aceptarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastralesPorDir.aceptarJButton"));
        aceptarJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastralesPorDir.aceptarJButton.hint"));
        cancelarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastralesPorDir.cancelarJButton"));
        cancelarJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.DialogoBuscarRefCatastralesPorDir.cancelarJButton.hint"));
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
                        .getTerm(app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"));
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
                    "Catastro.RegistroExpedientes.DialogoBuscarRefCatastralesPorDir.msgBuscando"));
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
                            "Catastro.RegistroExpedientes.DialogoBuscarRefCatastralesPorDir.msgBuscando"));
                            try
                            {
                                String patronNombreVia = nombreViaJTField.getText();
                                String patronTipoVia = (String)tipoViaJCBox.getSelectedPatron();
                                if(patronTipoVia ==null)
                                {
                                    patronTipoVia = "";
                                }
                                if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                                        &&convenioExpediente.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
                                {
                                    referenciasCatastrales= (ArrayList)ConstantesRegExp.clienteCatastro.getFincasCatastroBuscadasPorDir(patronNombreVia, patronTipoVia);
                                }
                                else if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)
                                        ||(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                                        &&convenioExpediente.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)))
                                {
                                    referenciasCatastrales= (ArrayList)ConstantesRegExp.clienteCatastro.getBienInmuebleCatastroBuscadasPorDir(patronNombreVia, patronTipoVia);
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
            JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.DialogoBuscarRefCatastralesPorDir.BuscarYSelecMSG"));
        }
    }

    /**
     * Metodo que llama al cliente catastro para hacer una peticion a base de datos y obtener los tipos de vias
     * para mostrarselas al usuario y que elija. Esto se hace porque al buscar la parcela por direccion se compara
     * la calle y el tipo de via y si no es correcto daria fallo. Asi el usuario el tipo de via lo selecciona
     * de un ComboBox.
     */
    /* TODO eliminar si lo de las vias con el dominio esta bien.
    private void getTiposViasBD()
    {
        try
        {
            tiposVias= (ArrayList)ConstantesRegistroExp.clienteCatastro.getTiposVias();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }*/

    /**
     * Carga los datos del arrayList pasado por parametro en el comboBox de tipos de vias
     *
     */
    private void inicializaComboBox()
    {
        AppContext app =(AppContext) AppContext.getApplicationContext();
        Estructuras.cargarEstructura("Tipos de via normalizados de Catastro");
        tipoViaJCBox = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                null, app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
    }

    /**
     * Carga el tipo de vias recogido de base de datos en un comboBox para que el usuario pueda seleccionar el tipo
     * de via.
     */
    /*TOdo eliminar si lo de las vias con dominios esta bien
    private void cargarTiposVias()
    {
        getTiposViasBD();
        for(int i = 0;i< tiposVias.size();i++)
        {
            tipoViaJCBox.addItem(tiposVias.get(i));
        }
    }*/
}
