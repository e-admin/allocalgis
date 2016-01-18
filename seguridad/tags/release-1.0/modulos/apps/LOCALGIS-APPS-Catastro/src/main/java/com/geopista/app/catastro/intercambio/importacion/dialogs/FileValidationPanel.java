package com.geopista.app.catastro.intercambio.importacion.dialogs;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.dialogs.GestionExpedientePanel;
import com.geopista.app.catastro.intercambio.exception.DataException;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportacionOperations;
import com.geopista.app.catastro.model.beans.ParcelaAfectada;
import com.geopista.app.catastro.intercambio.importacion.utils.TableParcelasAfectadasModel;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


public class FileValidationPanel extends JPanel
{
    
    //private JPanel jPanel = null;
    private JLabel jLabelMensaje = null;
    private JScrollPane jScrollPaneParcelas = null;
    private JTable jTableParcelas = null;   
    private JButton jButtonVerExpediente = null;
    private JPanel jPanelParcelas = null;
    private TableParcelasAfectadasModel tablemodel;
    
    private ApplicationContext application = AppContext.getApplicationContext();    
    private Blackboard blackboard = application.getBlackboard();
    private Expediente expedienteSeleccionado = null;
    
    /**
     * Determina si se van a mostrar parcelas importadas afectadas por expedientes activos 
     * (actualizadas) o parcelas afectadas por expedientes activos
     */
    boolean isImport = false;
    
    /**
     * This method initializes 
     * 
     */
    public FileValidationPanel() {
        super();        
        initialize();
    }
    
    
    public FileValidationPanel(boolean isImport) {
        this();
        this.isImport = isImport;
        
    }
    /**
     * This method initializes this
     * 
     */
    private void initialize() 
    {    
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Importacioni18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Importacion",bundle);
                
        //this.setSize(ModuloCatastralFrame.DIM_X-25, ModuloCatastralFrame.DIM_Y-250);
        this.setLayout(new GridBagLayout());
        this.add(getJPanelParcelas(), 
                new GridBagConstraints(0, 0, 1, 1, 1, 0.1,GridBagConstraints.NORTH,
                        GridBagConstraints.BOTH, new Insets(10,10,10,10),0,0));
        this.setPreferredSize(new java.awt.Dimension(600,420));       
    }
    
 
    /**
     * This method initializes jScrollPaneParcelas	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneParcelas()
    {
        if (jScrollPaneParcelas == null)
        {
            jScrollPaneParcelas = new JScrollPane();
            jScrollPaneParcelas.setViewportView(getJTableParcelas());
        }
        return jScrollPaneParcelas;
    }
    
    /**
     * This method initializes jTableParcelas	
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getJTableParcelas()
    {
        if (jTableParcelas == null)
        {            
            String[] columnNames = {I18N.get("Importacion","importar.validar.numeroexpediente"),
                    I18N.get("Importacion","importar.validar.parcela")};
            
            jTableParcelas = new JTable();   
            
            tablemodel = new TableParcelasAfectadasModel(columnNames);
            
            TableSorted tblSortedVias= new TableSorted(tablemodel);
            tblSortedVias.setTableHeader(jTableParcelas.getTableHeader());
            jTableParcelas.setModel(tblSortedVias);
            jTableParcelas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableParcelas.setCellSelectionEnabled(false);
            jTableParcelas.setColumnSelectionAllowed(false);
            jTableParcelas.setRowSelectionAllowed(true);
            jTableParcelas.getTableHeader().setReorderingAllowed(false);
            
            ListSelectionModel rowSM = jTableParcelas.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                    if (lsm.isSelectionEmpty()) {
                        //System.out.println("No rows are selected.");
                       getJButtonVerExpediente().setEnabled(false); 
                    } 
                    else 
                    {
                        getJButtonVerExpediente().setEnabled(true);
                        int i = lsm.getMinSelectionIndex();
                        expedienteSeleccionado = 
                            ((ParcelaAfectada)tablemodel.getValueAt(i)).getExpediente();
                        
                        
                    }
                }
            });       
            
            ImportacionOperations oper = new ImportacionOperations();
            try
            {         
                if(blackboard.get("ListaTotalFincasImportadas")!=null)
                {
                    ArrayList lstParcelasImportadas = (ArrayList)blackboard.get("ListaTotalFincasImportadas");
                    ArrayList lstParcelasAfectadas = oper.obtenerParcelasAfectadas(lstParcelasImportadas);
                    ((TableParcelasAfectadasModel)((TableSorted)jTableParcelas.getModel()).
                            getTableModel()).setData(lstParcelasAfectadas);      
                    blackboard.put("ListaTotalFincasImportadas", null);     
                    jLabelMensaje.setText(I18N.get("Importacion","importar.validar.parcelasactualizadasafectadas"));
                    
                }  
                else
                {
                    ArrayList lstParcelasAfectadas = oper.obtenerParcelasAfectadas();
                    ((TableParcelasAfectadasModel)((TableSorted)jTableParcelas.getModel()).
                            getTableModel()).setData(lstParcelasAfectadas);  
                    jLabelMensaje.setText(I18N.get("Importacion","importar.validar.parcelasafectadas"));
                    
                }
                
            } catch (DataException e1)
            {                
                e1.printStackTrace();
            }
        }
        return jTableParcelas;
    }
     
    
    /**
     * This method initializes jButtonVerExpediente	
     * 	
     * @return javax.swing.JButton	
     */
    public JButton getJButtonVerExpediente()
    {
        if (jButtonVerExpediente == null)
        {
            jButtonVerExpediente = new JButton();
            jButtonVerExpediente.setText(I18N.get("Importacion","importar.validar.boton.verexpediente"));
            jButtonVerExpediente.setPreferredSize(new Dimension(120, 30));
            jButtonVerExpediente.setEnabled(false);
            jButtonVerExpediente.addActionListener(new ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    if (!evt.getActionCommand().equals("VerExpedienteMain"))
                    {   
                        
                        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(application.getMainFrame(), null);
                        
                        progressDialog.setTitle(I18N.get("Expedientes","CargandoPantallaDeGestionDeExpedientes"));
                        progressDialog.report(I18N.get("Expedientes","CargandoPantallaDeGestionDeExpedientes"));
                        progressDialog.addComponentListener(new ComponentAdapter()
                                {
                            public void componentShown(ComponentEvent e)
                            {                
                                // Wait for the dialog to appear before starting the
                                // task. Otherwise
                                // the task might possibly finish before the dialog
                                // appeared and the
                                // dialog would never close. [Jon Aquino]
                                new Thread(new Runnable()
                                        {
                                    public void run()
                                    {
                                        try
                                        {                                            
                                            GestionExpedientePanel g = new GestionExpedientePanel(expedienteSeleccionado, false);
                                            JDialog jdialog = new JDialog(application.getMainFrame(), true);
                                            jdialog.setContentPane(g);
                                            jdialog.setSize(new Dimension(1000, 600));
                                            jdialog.show();                                           
                                        } 
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
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
                }
            });

        }
        return jButtonVerExpediente;
    }    
   
    /**
     * This method initializes jPanelParcelas	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelParcelas()
    {
        if (jPanelParcelas == null)
        {
            jPanelParcelas = new JPanel(new GridBagLayout());
            jLabelMensaje = new JLabel();
            jLabelMensaje.setText(I18N.get("Importacion","importar.validar.parcelasafectadas"));
                  
            jPanelParcelas.add(jLabelMensaje,
                    new GridBagConstraints(0, 0, 1, 1, 1, 0,GridBagConstraints.CENTER,
                            GridBagConstraints.BOTH, new Insets(10,10,10,10) ,0,0));
         
            jPanelParcelas.add(getJScrollPaneParcelas(),
                    new GridBagConstraints(0, 1, 3, 7, 1, 1,GridBagConstraints.CENTER,
                            GridBagConstraints.BOTH, new Insets(10,5,10,5) ,0,0));
          
            jPanelParcelas.add(getJButtonVerExpediente(),
                    new GridBagConstraints(1, 1, 4, 1, 0, 0,GridBagConstraints.CENTER,
                            GridBagConstraints.BOTH, new Insets(10,10,10,10) ,0,0));

        }
        return jPanelParcelas;
    }
    
    public Expediente getExpedienteSeleccionado()
    {
        return expedienteSeleccionado;
    }
    
}  
